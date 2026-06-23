# Build and Run Instructions

## Requirements

1. **Android Studio**: Hedgehog version or later
2. **JDK**: Java Development Kit 11 or later
3. **Android SDK**: API Level 36 (Android 14)

## Build Steps

### Option 1: Via Android Studio

1. Open the project in Android Studio
2. Wait for Gradle to sync
3. Connect the device/emulator
4. Click Run (green triangle)

### Option 2: Via the command line

```bash
cd /Users/vasiliikarpenko/AndroidStudioProjects/PocketPsychologist

# Check Java installation
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
echo $JAVA_HOME

# Sync Gradle
./gradlew clean

# Build the debug version
./gradlew assembleDebug

# Install on the device
./gradlew installDebug

# Run the application
adb shell am start -n com.zx_tole.pocketpsychologist/.ui.MainActivity
```

## Using the application

### Mood Analysis

1. Open the application
2. Click the "Start Recording" button
3. Speak for 30 seconds
4. Wait for the analysis to complete
5. See your mood in the "Daily Mood" widget

### Breathing Exercises

1. Click the "Take a deep breath" card
2. Follow the on-screen instructions:
- Inhale for 4 seconds
- Hold your breath for 4 seconds
- Exhale for 4 seconds
- Hold for 4 seconds
3. Repeat the exercise for 3-5 minutes

### Widget Mood

1. Launch the app and run the analysis
2. Long press on the home screen
3. Select "Mood of the day"
4. Place the widget on the home screen

## Permissions

On first launch, the app will request the following permissions:
- **Record audio**: for voice analysis
- **Vibration**: for rhythmic exercises
- **Notifications**: for background recording service

## Troubleshooting

### Gradle sync not working

```bash
./gradlew --stop
rm -rf ~/.gradle/caches
./gradlew clean build --refresh-dependencies
```

### Java errors

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
export PATH=$JAVA_HOME/bin:$PATH
```

### Device not defined

```bash
adb kill-server
adb start-server
adb devices
```

## Logs

To view logs:

```bash
adb logcat | grep "PocketPsychologist"
```

## Testing

### Unit tests

```bash
./gradlew test
```

### Instrumented tests

```bash
./gradlew connectedAndroidTest
```

## Release build

```bash
# Build the release version
./gradlew assembleRelease

# Alternatively, via Android Studio:
# Build > Generate Signed Bundle / APK
# Select APK
# Create or select a keystore
```

## Known limitations (MVP)

- Sentiment analysis based on simplified logic (without ML Kit Voice Match)
- Maximum recording length: 30 seconds
- Storing history in the local Room database
- Simple visualization of breathing exercises

## Development plans

- Integration with ML Kit for more accurate analysis
- Advanced sentiment classification
- Integration with Google Fit
- Export Mood History
- Notifications and Reminders