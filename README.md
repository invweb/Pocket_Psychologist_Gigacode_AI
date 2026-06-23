# Pocket Psychologist

Voice mood Analysis using ML Kit

## Description

The app analyzes the timbre and speed of the user's speech, determines the mood and offers breathing exercises. Feature: the "Mood of the day" widget and vibration in the rhythm of a soothing heart rate.

## Project structure

```
PocketPsychologist/
├── app/
│   ├── src/main/
│   │   ├── java/com/zx_tole/pocketpsychologist/
│   │   │   ,── ui/ # UI components
│   │   │   │   ├── theme/              # Themes and colors
│   │   │   │   ├── home/               # Main screen
│   │   │   │   ├── breathing/          # Breathing Exercises screen
│   │   │   │   └── viewmodel/          # ViewModels
│   │   │   ├── data/                   # Data and repositories
│   │   │   │   ├── model/              # Data models
│   │   │   │   ├── repository/         # Repositories
│   │   │   │   └── repository/
│   │   │   ,── voice/ # Voice analysis
│   │   │   ,── service/ # Services
│   │   │   ├── widget/                 # Widgets
│   │   │   └── di/                     # DI (Hilt)
│   │   ├── res/                        # Resources
│   │   │   ├── layout/                 # Layouts
│   │   │   ├── values/                 # Strings, colors, themes
│   │   │   ,── xml/ # XML configs
│   │   │   ,── drawable/ # Icons
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle/libs.versions.toml
```

## Dependencies

- AndroidX Core KTX
- Material Components
- Jetpack Compose (BOM)
- Lifecycle ViewModel & Runtime
- Kotlin Coroutines
- Room Database
- WorkManager
- Hilt (DI)
- AndroidX Activity Compose

## Permissions

- RECORD_AUDIO - voice recording
- VIBRATE - vibration for exercise
- POST_NOTIFICATIONS - notifications
- FOREGROUND_SERVICE - background service

## Functions

1. **Voice Analysis**: Speech recording and mood detection
2. **Mood Widget**: Displays the latest mood on the home screen
3. **Breathing Exercises**: An animated exercise with vibration
4. **History**: Saving the mood history in the database

## Usage

1. Launch the app
2. Press the "Start Recording"
button 3. Say something for 30 seconds.
4. After the analysis, you will see your mood
5. Use a breathing exercise to relax
6. Add the "Mood of the Day" widget to the home screen

## Setting up

To build the project, you need:
- Android Studio Hedgehog or later
- JDK 11 or later
- Android SDK with API 36

## Important files

### Key Kotlin files
- `ui/HomeScreen.kt` - the main screen of the application
- `voice/VoiceAnalyzer.kt' - voice analyzer
- `data/repository/MoodRepository.kt` - sentiment repository
- `ui/viewmodel/HomeViewModel.kt` - ViewModel for the home screen

### Resources
- `res/values/strings.xml ` - interface strings
- `res/values/colors.xml ` - theme colors
- `res/values/themes.xml ` - application themes
- `res/xml/mood_widget_info.xml ` - widget configuration

### Configuration
- `gradle/libs.versions.toml` - dependency versions
- `app/build.gradle.kts` - build configuration

## Development plans

- Integration with ML Kit Voice Match API for more accurate analysis
- Classification of moods using a custom model
- Integration with Google Fit for health tracking
- Export sentiment history to PDF
- Notifications and reminders about breathing exercises

## License

This project was created for educational purposes.

| Screenshots |
|-------------|
| ![Splash Screenshot](].png) |
| ![Main screen Screenshot](2.png) |
