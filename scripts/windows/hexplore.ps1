[CmdletBinding()]
param(
  [ValidateSet('setup', 'build', 'install', 'devices')]
  [string]$Command = 'setup',
  [string]$DeviceSerial
)

Set-StrictMode -Version Latest
$ErrorActionPreference = 'Stop'

$AndroidSdkPackages = @(
  'platform-tools',
  'platforms;android-36',
  'build-tools;36.0.0'
)

$AndroidUsbDriverPackage = 'extras;google;usb_driver'
$CommandLineToolsDownloadUrl = 'https://dl.google.com/android/repository/commandlinetools-win-14742923_latest.zip'

$ScriptRoot = Split-Path -Parent $MyInvocation.MyCommand.Path
$RepoRoot = (Resolve-Path (Join-Path $ScriptRoot '..\..')).Path
$GradleWrapper = Join-Path $RepoRoot 'gradlew.bat'
$LocalPropertiesPath = Join-Path $RepoRoot 'local.properties'
$EnvExamplePath = Join-Path $RepoRoot '.env.example'
$EnvPath = Join-Path $RepoRoot '.env'

function Test-CommandAvailable {
  param([string]$Name)

  return $null -ne (Get-Command $Name -ErrorAction SilentlyContinue)
}

function Write-Section {
  param([string]$Title)
  Write-Host ""
  Write-Host "== $Title =="
}

function Get-LocalSdkDir {
  if (-not (Test-Path $LocalPropertiesPath)) {
    return $null
  }

  $match = Select-String -Path $LocalPropertiesPath -Pattern '^\s*sdk\.dir\s*=\s*(.+)\s*$' | Select-Object -First 1
  if ($null -eq $match) {
    return $null
  }

  return $match.Matches[0].Groups[1].Value.Trim()
}

function Resolve-SdkDir {
  $defaultSdkDir = $null
  if (-not [string]::IsNullOrWhiteSpace($env:LOCALAPPDATA)) {
    $defaultSdkDir = Join-Path $env:LOCALAPPDATA 'Android\Sdk'
  }

  $candidates = @(
    $env:ANDROID_SDK_ROOT,
    $env:ANDROID_HOME,
    $defaultSdkDir,
    (Get-LocalSdkDir)
  )

  foreach ($candidate in $candidates) {
    if ([string]::IsNullOrWhiteSpace($candidate)) {
      continue
    }

    if (Test-Path $candidate) {
      return (Resolve-Path $candidate).Path
    }
  }

  return $defaultSdkDir
}

function Write-LocalProperties {
  param([string]$SdkDir)

  $normalizedSdkDir = ([System.IO.Path]::GetFullPath($SdkDir)).Replace('\', '/')
  Set-Content -Path $LocalPropertiesPath -Value "sdk.dir=$normalizedSdkDir" -Encoding ASCII
}

function Ensure-EnvFile {
  if ((Test-Path $EnvPath) -or -not (Test-Path $EnvExamplePath)) {
    return
  }

  Copy-Item -Path $EnvExamplePath -Destination $EnvPath
}

function Get-CommonJavaLocations {
  $roots = @()

  if (-not [string]::IsNullOrWhiteSpace($env:JAVA_HOME)) {
    $roots += $env:JAVA_HOME
  }

  if (-not [string]::IsNullOrWhiteSpace($env:ProgramFiles)) {
    $roots += (Join-Path $env:ProgramFiles 'Android\Android Studio')
    $roots += (Join-Path $env:ProgramFiles 'Eclipse Adoptium')
    $roots += (Join-Path $env:ProgramFiles 'Java')
    $roots += (Join-Path $env:ProgramFiles 'Microsoft')
  }

  if (-not [string]::IsNullOrWhiteSpace($env:LOCALAPPDATA)) {
    $roots += (Join-Path $env:LOCALAPPDATA 'Programs\Android\Android Studio')
    $roots += (Join-Path $env:LOCALAPPDATA 'Programs\Eclipse Adoptium')
    $roots += (Join-Path $env:LOCALAPPDATA 'Programs\Microsoft')
  }

  return $roots | Where-Object { -not [string]::IsNullOrWhiteSpace($_) } | Select-Object -Unique
}

function Resolve-JavaExecutable {
  $command = Get-Command java -ErrorAction SilentlyContinue
  if ($null -ne $command) {
    return $command.Source
  }

  foreach ($root in Get-CommonJavaLocations) {
    if (-not (Test-Path $root)) {
      continue
    }

    $candidate = Get-ChildItem -Path $root -Filter java.exe -Recurse -File -ErrorAction SilentlyContinue | Select-Object -First 1
    if ($null -ne $candidate) {
      return $candidate.FullName
    }
  }

  return $null
}

function Ensure-Java {
  $javaPath = Resolve-JavaExecutable
  if ($null -ne $javaPath) {
    $javaHome = Split-Path (Split-Path $javaPath -Parent) -Parent
    $env:JAVA_HOME = $javaHome
    $env:Path = (Join-Path $javaHome 'bin') + ';' + $env:Path
    return $javaPath
  }

  if (-not (Test-CommandAvailable 'winget')) {
    throw 'Java is missing and winget is not available. Install JDK 17 or Android Studio, then rerun setup.'
  }

  Write-Host 'Java is missing. Installing Eclipse Temurin 17 via winget...'
  & winget install --id EclipseAdoptium.Temurin.17.JDK -e --accept-package-agreements --accept-source-agreements
  if ($LASTEXITCODE -ne 0) {
    throw 'winget could not install JDK 17. Install Android Studio or JDK 17 manually, then rerun setup.'
  }

  $javaPath = Resolve-JavaExecutable
  if ($null -eq $javaPath) {
    throw 'JDK 17 installation completed, but java.exe was still not found. Restart VS Code or open a new terminal and rerun setup.'
  }

  $javaHome = Split-Path (Split-Path $javaPath -Parent) -Parent
  $env:JAVA_HOME = $javaHome
  $env:Path = (Join-Path $javaHome 'bin') + ';' + $env:Path

  return $javaPath
}

function Get-SdkManagerPath {
  param([string]$SdkDir)

  $sdkManagerPath = Join-Path $SdkDir 'cmdline-tools\latest\bin\sdkmanager.bat'
  if (Test-Path $sdkManagerPath) {
    return $sdkManagerPath
  }

  return $null
}

function Install-CommandLineTools {
  param([string]$SdkDir)

  Write-Host 'Downloading Android command-line tools...'
  $tempRoot = Join-Path ([System.IO.Path]::GetTempPath()) ('hexplore-android-sdk-' + [System.Guid]::NewGuid().ToString('N'))
  $zipPath = Join-Path $tempRoot 'commandlinetools.zip'
  $extractRoot = Join-Path $tempRoot 'extract'
  $latestDir = Join-Path $SdkDir 'cmdline-tools\latest'

  New-Item -ItemType Directory -Force -Path $tempRoot | Out-Null
  New-Item -ItemType Directory -Force -Path $extractRoot | Out-Null
  New-Item -ItemType Directory -Force -Path $latestDir | Out-Null

  Invoke-WebRequest -Uri $CommandLineToolsDownloadUrl -OutFile $zipPath
  Expand-Archive -Path $zipPath -DestinationPath $extractRoot -Force

  $sourceDir = Join-Path $extractRoot 'cmdline-tools'
  if (-not (Test-Path $sourceDir)) {
    throw 'Downloaded command-line tools archive was not in the expected layout.'
  }

  Copy-Item -Path (Join-Path $sourceDir '*') -Destination $latestDir -Recurse -Force
  Remove-Item -Path $tempRoot -Recurse -Force -ErrorAction SilentlyContinue
}

function Invoke-SdkManager {
  param(
    [string]$SdkDir,
    [string[]]$Args,
    [switch]$AcceptLicenses
  )

  $sdkManagerPath = Get-SdkManagerPath -SdkDir $SdkDir
  if ($null -eq $sdkManagerPath) {
    Install-CommandLineTools -SdkDir $SdkDir
    $sdkManagerPath = Get-SdkManagerPath -SdkDir $SdkDir
  }

  if ($null -eq $sdkManagerPath) {
    throw 'sdkmanager was not found after installing the Android command-line tools.'
  }

  if ($AcceptLicenses) {
    1..40 | ForEach-Object { 'y' } | & $sdkManagerPath --sdk_root=$SdkDir --licenses
  }

  & $sdkManagerPath --sdk_root=$SdkDir @Args
  if ($LASTEXITCODE -ne 0) {
    throw "sdkmanager command failed: $($Args -join ' ')"
  }
}

function Ensure-AndroidSdk {
  param([string]$SdkDir)

  if (-not (Test-Path $SdkDir)) {
    New-Item -ItemType Directory -Force -Path $SdkDir | Out-Null
  }

  if (-not (Test-Path (Get-SdkManagerPath -SdkDir $SdkDir))) {
    Install-CommandLineTools -SdkDir $SdkDir
  }

  Invoke-SdkManager -SdkDir $SdkDir -Args $AndroidSdkPackages -AcceptLicenses

  if ($IsWindows) {
    Invoke-SdkManager -SdkDir $SdkDir -Args @($AndroidUsbDriverPackage) -AcceptLicenses
  }
}

function Ensure-Bootstrap {
  Ensure-EnvFile

  $sdkDir = Resolve-SdkDir
  $javaPath = Ensure-Java
  Ensure-AndroidSdk -SdkDir $sdkDir
  Write-LocalProperties -SdkDir $sdkDir

  Write-Host "Java: $javaPath"
  Write-Host "SDK: $sdkDir"
  return $sdkDir
}

function Invoke-Gradle {
  param([string[]]$Args)

  & $GradleWrapper @Args
  if ($LASTEXITCODE -ne 0) {
    throw "Gradle command failed: $($Args -join ' ')"
  }
}

function Invoke-Adb {
  param(
    [string]$SdkDir,
    [string[]]$Args
  )

  $AdbPath = Join-Path $SdkDir 'platform-tools\adb.exe'
  if (-not (Test-Path $AdbPath)) {
    throw "ADB not found at $AdbPath. Install Android SDK Platform-Tools."
  }

  & $AdbPath @Args
  if ($LASTEXITCODE -ne 0) {
    throw "ADB command failed: $($Args -join ' ')"
  }
}

function Invoke-Setup {
  Write-Section 'Setup'
  $sdkDir = Ensure-Bootstrap

  Write-Host 'Java version:'
  & java -version
  if ($LASTEXITCODE -ne 0) {
    throw 'Java verification failed after setup.'
  }

  Write-Host 'ADB version:'
  Invoke-Adb -SdkDir $sdkDir -Args @('version')

  Write-Host ''
  Write-Host 'Setup complete. Next commands:'
  Write-Host '  build   -> .\scripts\windows\build.bat'
  Write-Host '  install -> .\scripts\windows\install.bat'
  Write-Host '  devices -> .\scripts\windows\devices.bat'
}

function Invoke-Build {
  Write-Section 'Build debug APK'
  Ensure-Bootstrap | Out-Null
  Invoke-Gradle -Args @('assembleDebug')
}

function Invoke-Install {
  Write-Section 'Install over USB'

  $sdkDir = Ensure-Bootstrap

  if (-not [string]::IsNullOrWhiteSpace($DeviceSerial)) {
    $env:ANDROID_SERIAL = $DeviceSerial
    Write-Host "Using device serial: $DeviceSerial"
  }

  Invoke-Adb -SdkDir $sdkDir -Args @('devices')
  Invoke-Gradle -Args @('installDebug')
}

function Invoke-Devices {
  Write-Section 'Connected devices'
  $sdkDir = Ensure-Bootstrap
  Invoke-Adb -SdkDir $sdkDir -Args @('devices')
}

switch ($Command) {
  'setup' { Invoke-Setup }
  'build' { Invoke-Build }
  'install' { Invoke-Install }
  'devices' { Invoke-Devices }
}
