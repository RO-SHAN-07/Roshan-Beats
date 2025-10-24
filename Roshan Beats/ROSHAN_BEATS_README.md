# Roshan Beats 🎵

> **Feel the Future of Sound**

A complete, futuristic, high-quality personal music streaming application powered by Jamendo API, created by Roshan.

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![Platform](https://img.shields.io/badge/platform-Android%20|%20iOS%20|%20Web-brightgreen)
![Kotlin](https://img.shields.io/badge/Kotlin-Multiplatform-purple)

---

## ✨ Features

### 🎨 Premium Design
- **Futuristic UI**: Matte black backgrounds with electric blue and violet accents
- **Smooth Animations**: Parallax effects, fade transitions, and microinteractions
- **Dark/Light Themes**: Fully customizable appearance
- **Cinematic Experience**: Blurred backgrounds and glowing effects
- **Responsive Layout**: Optimized for all screen sizes

### 🎵 Music Features
- **Jamendo Integration**: Access to vast music library
- **AI-Enhanced Discovery**: Smart recommendations based on listening habits
- **Lossless Playback**: Multiple audio quality options
- **Offline Support**: Download tracks for offline listening
- **Queue Management**: Drag-to-reorder, add/remove tracks
- **Smart Crossfade**: Seamless transitions between tracks

### 📱 Screens

#### 1. Splash Screen
- Animated "Roshan Beats" logo with glow effect
- Soundwave pulse animation
- Smooth transition to home

#### 2. Home Screen
- Dynamic greeting based on time of day
- Featured carousel with trending tracks
- Sections:
  - Trending Now
  - Recently Played
  - Top Artists
  - New Releases
  - Chill & Focus Mixes

#### 3. Search Screen
- Real-time search with debouncing
- Tabs for Songs, Albums, and Artists
- Trending searches and genre suggestions
- Grid layout for visual browsing

#### 4. Player Screen
- Fullscreen immersive player
- Blurred album art background
- Animated waveform visualizer
- Complete playback controls:
  - Play/Pause, Next, Previous
  - Shuffle, Repeat (Off/One/All)
  - Progress bar with seek
  - AI Mode toggle
- Bottom actions: Like, Download, Queue, Share

#### 5. Library Screen
- Liked Songs collection
- User-created Playlists
- Recently Played history
- Playlist management (create, rename, delete)
- Drag-and-drop reordering

#### 6. Settings Screen
- Audio quality selector (Low/Medium/High/Lossless)
- Crossfade duration control
- Theme toggle (Dark/Light)
- About and developer info

#### 7. Detail Screens
- **Album Details**: Track list, Play All, Shuffle
- **Artist Details**: Top songs and albums
- **Playlist Details**: Track management

### 🎛️ Player Controls
- **Mini Player**: Always visible at bottom, tap to expand
- **Queue Modal**: View and manage upcoming tracks
- **Smart Shuffle**: Randomize while maintaining flow
- **Repeat Modes**: Off, Repeat All, Repeat One
- **AI Mode**: Auto-generate next tracks based on mood and genre

---

## 🏗️ Technical Architecture

### Tech Stack
- **Framework**: Kotlin Multiplatform Compose
- **Architecture**: MVVM (Model-View-ViewModel)
- **Navigation**: Jetpack Navigation Compose
- **Networking**: Ktor Client
- **Image Loading**: Coil 3
- **State Management**: Kotlin StateFlow
- **Serialization**: kotlinx.serialization
- **UI**: Material 3 Design System

### Project Structure
```
composeApp/src/commonMain/kotlin/org/roshan/beats/
├── data/
│   ├── api/
│   │   └── JamendoApi.kt          # API service layer
│   ├── models/
│   │   ├── Track.kt               # Track data model
│   │   ├── Album.kt               # Album data model
│   │   ├── Artist.kt              # Artist data model
│   │   ├── Playlist.kt            # Playlist data model
│   │   └── JamendoResponse.kt     # API response models
│   └── repository/
│       └── MusicRepository.kt     # Data repository
├── viewmodel/
│   ├── MusicViewModel.kt          # Music data state
│   └── PlayerViewModel.kt         # Player state management
├── ui/
│   ├── components/
│   │   ├── BottomNavigationBar.kt # Bottom nav bar
│   │   ├── MiniPlayer.kt          # Persistent mini player
│   │   ├── QueueModal.kt          # Queue management
│   │   ├── TrackCard.kt           # Track display card
│   │   ├── AlbumCard.kt           # Album display card
│   │   └── ArtistCard.kt          # Artist display card
│   ├── screens/
│   │   ├── SplashScreen.kt        # Animated splash
│   │   ├── HomeScreen.kt          # Main hub
│   │   ├── SearchScreen.kt        # Search interface
│   │   ├── PlayerScreen.kt        # Full player
│   │   ├── LibraryScreen.kt       # User library
│   │   ├── SettingsScreen.kt      # App settings
│   │   ├── AlbumDetailScreen.kt   # Album details
│   │   ├── ArtistDetailScreen.kt  # Artist details
│   │   └── PlaylistDetailScreen.kt # Playlist details
│   ├── theme/
│   │   ├── Color.kt               # Color definitions
│   │   ├── Theme.kt               # Theme configuration
│   │   └── Type.kt                # Typography
│   └── navigation/
│       └── Screen.kt              # Navigation routes
└── App.kt                         # Main app entry point
```

### Data Flow
1. **UI Layer**: Compose screens and components
2. **ViewModel Layer**: State management and business logic
3. **Repository Layer**: Data aggregation and caching
4. **API Layer**: Jamendo API communication

---

## 🚀 Getting Started

### Prerequisites
- JDK 11 or higher
- Android Studio (for Android development)
- Xcode (for iOS development)
- Node.js (for web development)

### Building the App

#### Android
```bash
./gradlew :composeApp:assembleDebug
```

#### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run

#### Web (WASM)
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

#### Web (JS)
```bash
./gradlew :composeApp:jsBrowserDevelopmentRun
```

---

## 🎨 Design Language

### Color Palette
- **Background**: Deep Charcoal (#0A0B0E) to Jet Black (#000000)
- **Primary Accent**: Electric Blue (#00D9FF)
- **Secondary Accent**: Electric Violet (#8B5CF6)
- **Text**: White (#FFFFFF) with various opacities

### Typography
- **Font Family**: System default (geometric sans-serif)
- **Weights**: Light (300), Regular (400), Medium (500), SemiBold (600), Bold (700)
- **Scales**: Material 3 typography scale

### UI Principles
- High contrast for accessibility
- Smooth 60fps animations
- Minimal and cinematic
- Consistent spacing and shadows
- Touch-friendly targets (48dp minimum)

---

## 🎵 Music Source

Roshan Beats uses the **Jamendo API** to provide:
- 200,000+ free music tracks
- Independent artists from around the world
- Multiple genres and moods
- High-quality audio streams
- Legal and licensed content

### API Features Used
- Track search and discovery
- Album and artist information
- Genre-based filtering
- Popularity-based recommendations
- Streaming URLs

---

## 🤖 AI Features

### Smart Recommendations
- Analyzes current track tags
- Suggests similar music
- Adapts to listening patterns
- Genre-aware mixing

### AI Mode
When enabled, the player automatically:
- Queues similar tracks
- Maintains mood and tempo
- Creates infinite playlists
- Learns from user preferences

---

## 📦 Dependencies

### Main Libraries
```kotlin
// Compose Multiplatform
compose.runtime
compose.foundation
compose.material3
compose.ui

// Navigation
navigation-compose = "2.8.0"

// Networking
ktor-client = "3.3.0"
kotlinx-serialization = "1.7.3"

// Image Loading
coil-compose = "3.0.4"

// Async
kotlinx-coroutines = "1.10.1"

// Media (Android)
androidx-media3 = "1.6.0"
```

---

## 🎯 Roadmap

### Completed ✅
- [x] Full UI implementation
- [x] Jamendo API integration
- [x] Player with queue management
- [x] Search and discovery
- [x] Playlist creation
- [x] Theme system
- [x] AI recommendations

### Future Enhancements 🚀
- [ ] Audio visualizer with FFT analysis
- [ ] Lyrics integration
- [ ] Social sharing features
- [ ] Custom equalizer
- [ ] Sleep timer
- [ ] Car mode interface
- [ ] Chromecast support
- [ ] Last.fm scrobbling

---

## 👨‍💻 Developer

**Created by Roshan**

A passion project to explore modern mobile development and create a premium music experience.

### Contact
- GitHub: [@RO-SHAN-07](https://github.com/RO-SHAN-07)

---

## 📄 License

This project is for educational and personal use. Music content is provided by Jamendo under their respective licenses.

---

## 🙏 Acknowledgments

- **Jamendo** for the amazing music API
- **JetBrains** for Kotlin and Compose Multiplatform
- **Material Design** for design guidelines
- **Coil** for efficient image loading

---

## 📸 Screenshots

*Screenshots to be added*

---

## 🔧 Configuration

### Jamendo API
The app uses a public Jamendo API client ID. For production use, register your own at [Jamendo Developer Portal](https://developer.jamendo.com/).

### Build Configuration
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36
- **Compile SDK**: 36
- **JVM Target**: 11

---

## 🐛 Known Issues

- Audio playback engine is UI-only (actual playback requires platform-specific implementation)
- Download feature is simulated (requires storage permissions and implementation)
- Some animations may vary across platforms

---

## 💡 Tips

1. **Best Experience**: Use dark theme for the intended futuristic aesthetic
2. **AI Mode**: Enable AI Mode for continuous discovery
3. **Playlists**: Create mood-based playlists for different occasions
4. **Search**: Use genre keywords for better discovery
5. **Queue**: Build your queue ahead for uninterrupted listening

---

**Roshan Beats** - Feel the Future of Sound 🎵

*Built with ❤️ using Kotlin Multiplatform Compose*
