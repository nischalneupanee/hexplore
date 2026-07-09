# HEXplore Windows Scripts

Use this folder when you want the Windows setup, build, install, and device-check commands in one place.

## What each script does

| Script | Purpose |
|---|---|
| [setup.bat](setup.bat) | Installs or configures the Android SDK, JDK 17, licenses, and `local.properties` |
| [build.bat](build.bat) | Builds the debug APK |
| [install.bat](install.bat) | Installs the debug APK on a connected phone over USB |
| [devices.bat](devices.bat) | Lists connected ADB devices |
| [hexplore.ps1](hexplore.ps1) | PowerShell implementation used by all wrappers |

## Recommended way to run it in VS Code

Open the repository in VS Code and run the setup task first:

```text
Terminal -> Run Task -> HEXplore: Setup Windows Environment
```

After setup, you can run these tasks from the same menu:

- `HEXplore: Build Debug APK`
- `HEXplore: Install Debug APK over USB`
- `HEXplore: List USB Devices`

## Script order

The normal workflow is:

1. Run [setup.bat](setup.bat)
2. Connect the phone with USB debugging enabled
3. Run [devices.bat](devices.bat)
4. Run [install.bat](install.bat)

If you only want to compile the app, run [build.bat](build.bat) after setup.

## How to use the scripts

Run the wrappers from PowerShell in this folder or from the repo root:

```powershell
.\scripts\windows\setup.bat
.\scripts\windows\build.bat
.\scripts\windows\install.bat
.\scripts\windows\devices.bat
```

Each wrapper calls [hexplore.ps1](hexplore.ps1), which does the actual setup and Gradle/ADB work.

## What setup installs

The setup script is self-contained. It will:

1. Find or install Java 17.
2. Find the Android SDK, or create the default SDK folder under your user profile.
3. Download the Android command-line tools if `sdkmanager` is missing.
4. Install these packages:
   - `platform-tools`
   - `platforms;android-36`
   - `build-tools;36.0.0`
   - `extras;google;usb_driver`
5. Write `local.properties` so Gradle knows where the SDK is.

## What build and install do

- [build.bat](build.bat) runs `./gradlew.bat assembleDebug`
- [install.bat](install.bat) runs setup first, then `./gradlew.bat installDebug`
- [devices.bat](devices.bat) runs `adb devices`

## Common Windows paths

- Android SDK: `C:\Users\<you>\AppData\Local\Android\Sdk`
- Android Studio: `C:\Program Files\Android\Android Studio`
- JDK from winget: usually under `C:\Program Files\Eclipse Adoptium\` or `C:\Program Files\Java\`

## Troubleshooting

- If `winget` is missing, install Java 17 or Android Studio manually, then rerun setup.
- If downloads are blocked, install Android Studio once and rerun the setup task.
- If `adb devices` shows `unauthorized`, unlock the phone and accept the debugging prompt.
- If the SDK path is wrong, delete `local.properties` and run setup again.
