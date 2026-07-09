# Windows Setup, Build, and USB Install

This guide covers the Windows workflow for setting up HEXplore, building the debug APK, and installing it on a phone over USB.

If you want the shortest path, use [scripts/windows/README.md](../../scripts/windows/README.md). That folder is the single place to run the Windows setup and build commands.

## What you need

- Windows 10 or Windows 11
- Android Studio installed, or at minimum the Android SDK, platform tools, and build tools
- JDK 17
- USB debugging enabled on the Android phone
- A USB cable that supports data transfer

If you are unsure whether your SDK is configured correctly, open Android Studio once and let it install the missing components through SDK Manager.

## Install Android Studio and the SDK

If you do not already have the Android SDK on your Windows machine, follow these steps first:

1. Download and install Android Studio from the official Android developer site.
2. Launch Android Studio once so it can finish the first-time setup.
3. Open `More Actions -> SDK Manager`.
4. Install these packages:
	- Android SDK Platform 36
	- Android SDK Platform-Tools
	- Android SDK Build-Tools 36.0.0
5. Confirm the SDK location shown in SDK Manager. The default path is usually:

```text
C:\Users\<your-user>\AppData\Local\Android\Sdk
```

6. Install or confirm JDK 17. If Android Studio is already installed, it usually bundles a compatible JDK.
7. Open a new PowerShell window and verify the tools:

```powershell
java -version
adb version
```

## First-time setup

1. Clone the repository and open it in VS Code.
2. Run the setup task from VS Code:

```text
Terminal -> Run Task -> HEXplore: Setup Windows Environment
```

3. If you prefer the terminal, run the setup wrapper:

```powershell
.\scripts\windows\setup.bat
```

The setup command will:

- copy `.env.example` to `.env` if `.env` is missing
- install JDK 17 automatically if it is missing and winget is available
- download the Android command-line tools if the SDK is missing
- resolve the Android SDK path from your environment or the default Android Studio location
- write the Windows `local.properties` file with the detected SDK path
- verify that Java and ADB are available

If the script cannot find your SDK, it will create the default SDK location under your user profile and bootstrap the command-line tools there. If a company policy blocks downloads, install Android Studio once and rerun the setup task.

## Build the app

To build a debug APK without installing it on a device, use the VS Code task or the wrapper:

```text
Terminal -> Run Task -> HEXplore: Build Debug APK
```

```powershell
.\scripts\windows\build.bat
```

This produces:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Install over USB

1. Enable Developer Options on the phone.
2. Turn on USB debugging.
3. Connect the phone to the PC with a data cable.
4. Accept the USB debugging prompt on the phone.
5. If Windows asks for a driver, let it install the Google USB driver from SDK Manager or use your device OEM driver.
6. Verify that ADB sees the device:

```powershell
.\scripts\windows\devices.bat
```

7. Install the app:

```powershell
.\scripts\windows\install.bat
```

If you have more than one device connected, use the PowerShell helper directly and pass the serial:

```powershell
.\scripts\windows\hexplore.ps1 install -DeviceSerial <serial-from-adb-devices>
```

## Manual Gradle commands

If you prefer Gradle directly, these are the equivalent commands:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

## Troubleshooting

If the device shows as `unauthorized`, unlock the phone and accept the debugging prompt again.

If `adb` is not found, install Android SDK Platform-Tools or re-run the setup script after setting `ANDROID_SDK_ROOT`.

If Gradle cannot find the SDK, make sure `local.properties` points to the Windows SDK location and not a Linux path from another machine.

If the setup script still says no Android SDK is installed, open Android Studio, install the SDK packages above in SDK Manager, then run the setup script again.

If you are blocked by a corporate proxy or antivirus, run the setup task once more after allowing PowerShell downloads from `dl.google.com`.
