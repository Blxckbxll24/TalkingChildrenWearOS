# TalkingChildrenWearOS

A comprehensive Wear OS application for children's health and activity monitoring, built with modern Android development practices.

## Features

### 🏠 **Main Features**
- **Circular Navigation UI** - Optimized for Wear OS with circular and square screen support
- **Heart Rate Monitoring** - Real-time heart rate tracking with Health Services integration
- **Activity Tracking** - Steps, distance, and calorie monitoring
- **Settings Management** - Customizable preferences and thresholds
- **Data Persistence** - Local Room database for health data storage
- **Background Services** - Continuous sensor monitoring
- **Wear OS Tiles** - Quick access complications

### 📱 **Technology Stack**
- **Kotlin** - Primary development language
- **Jetpack Compose for Wear OS** - Modern declarative UI
- **Room Database** - Local data persistence
- **WorkManager** - Background task management
- **Health Services** - Sensor integration
- **Material Design for Wear OS** - Native design system
- **Coroutines** - Asynchronous programming

### 🎯 **Target Specifications**
- **Target SDK**: Android 13+ (API 33+)
- **Minimum SDK**: Wear OS 3.0+ (API 30+)
- **Screen Support**: Circular and square displays
- **Battery Optimized**: Efficient sensor usage
- **Permissions**: Heart rate, activity recognition, body sensors

## Architecture

### 📦 **Project Structure**
```
app/src/main/kotlin/com/blxckbxll24/talkingchildrenwearos/
├── TalkingChildrenApplication.kt          # Application class
├── data/
│   ├── database/                          # Room database components
│   │   ├── Converters.kt                  # Data type converters
│   │   ├── Daos.kt                        # Database access objects
│   │   └── TalkingChildrenDatabase.kt     # Database configuration
│   ├── repository/                        # Data repositories
│   │   ├── HealthDataRepository.kt        # Health data management
│   │   └── UserPreferencesRepository.kt   # Settings management
│   └── service/
│       └── SensorMonitoringService.kt     # Background sensor service
├── domain/
│   └── model/
│       └── Models.kt                      # Data models and entities
└── presentation/
    ├── MainActivity.kt                    # Main activity
    ├── composable/                        # Compose UI screens
    │   ├── ActivityScreen.kt              # Activity tracking UI
    │   ├── HeartRateScreen.kt            # Heart rate monitoring UI
    │   ├── HomeScreen.kt                 # Main dashboard
    │   ├── SettingsScreen.kt             # Settings configuration
    │   └── WearAppNavigation.kt          # Navigation setup
    ├── theme/
    │   └── Theme.kt                      # Material theme configuration
    └── tile/
        └── MainTileService.kt            # Wear OS tiles service
```

### 🗂️ **Resources Structure**
```
app/src/main/res/
├── drawable/                             # Vector icons and backgrounds
├── mipmap-*/                            # Application icons
├── values/                              # Strings, colors, themes
├── values-round/                        # Circular screen resources
├── values-notround/                     # Square screen resources
└── xml/                                 # Backup and data extraction rules
```

## Features Detail

### 🏠 **Home Screen**
- Welcome message with current time
- Quick access navigation to all features
- Permission status and request handling
- Real-time health stats preview
- Circular layout optimized for Wear OS

### ❤️ **Heart Rate Monitoring**
- Real-time heart rate measurement
- Manual start/stop controls
- Heart rate history tracking
- Threshold-based color coding (normal, elevated, high)
- Integration with Health Services API

### 🏃 **Activity Tracking**
- Step counter with daily goals
- Distance calculation (kilometers)
- Calorie burn estimation
- Progress indicators and achievements
- Weekly activity summaries

### ⚙️ **Settings**
- Notification preferences
- Automatic heart rate monitoring toggle
- Companion device sync options
- Heart rate threshold configuration
- Daily step goal customization
- Data management options

### 🛠️ **Background Services**
- Continuous sensor monitoring
- Automatic data collection
- Battery-optimized operation
- WorkManager integration
- Notification system

## Installation & Setup

### Prerequisites
- Android Studio with Wear OS SDK
- Wear OS emulator or physical device
- API level 30+ (Wear OS 3.0+)

### Build Instructions
```bash
# Clone the repository
git clone https://github.com/Blxckbxll24/TalkingChildrenWearOS.git

# Navigate to project directory
cd TalkingChildrenWearOS

# Build the project
./gradlew build

# Install on Wear OS device/emulator
./gradlew installDebug
```

### Permissions
The app requires the following permissions:
- `BODY_SENSORS` - Heart rate monitoring
- `ACTIVITY_RECOGNITION` - Step tracking
- `WAKE_LOCK` - Background operation
- `VIBRATE` - Notifications

## Development

### 🏗️ **Architecture Patterns**
- **MVVM** - Model-View-ViewModel pattern
- **Repository Pattern** - Data access abstraction
- **Dependency Injection** - Modular component design
- **Clean Architecture** - Separation of concerns

### 🧪 **Code Quality**
- Kotlin coding standards
- Compose best practices
- Material Design for Wear OS guidelines
- Battery optimization techniques
- Accessibility support

### 📊 **Data Management**
- Room database for local persistence
- SharedPreferences for user settings
- Automatic data cleanup policies
- Export capabilities for data backup

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Future Enhancements

- [ ] Companion mobile app synchronization
- [ ] Advanced health analytics
- [ ] Sleep tracking integration
- [ ] Social features and challenges
- [ ] Voice commands support
- [ ] Additional sensor integrations
- [ ] Cloud data backup
- [ ] Parental controls and monitoring