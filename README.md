# WeatherPlus 🌤️

A modern Android weather application built with Kotlin and Jetpack Compose, following
Clean Architecture and MVI patterns. WeatherPlus displays real-time weather data for
15 cities worldwide, supports offline access, favorites management, and hourly
background notifications.

---

## 📸 Screenshots

<table>
  <tr>
    <td><img src="https://github.com/user-attachments/assets/2fcb3717-9c62-4fc3-9f0f-177b2ecf4411" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/7b1694cc-3748-4b8f-9e89-583bc2588946" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/1c01753e-ba44-4940-97e2-8f6d31bc9cfc" width="200"/></td>
    <td><img src="https://github.com/user-attachments/assets/67e74ab6-d492-46fd-a824-25a90611c274" width="200"/></td>
  </tr>
  <tr>
    <td align="center">City List</td>
    <td align="center">City Details</td>
    <td align="center">Search</td>
    <td align="center">Notification</td>
  </tr>
</table>
---

## ✨ Features

- 🌍 Real-time weather data for 15 global cities
- 🔍 Search and filter cities instantly
- ⭐ Mark cities as favourites — favourites always appear at the top
- 📄 Detailed weather info per city (temperature, humidity, wind, pressure, visibility)
- 📶 Offline-first — cached data is shown when there is no internet connection
- 🔔 Background notification every 15 minutes showing your top favourite city's weather
- 🧪 Unit tested — ViewModels, Repository, and fake implementations

---

## 🏗️ Architecture

WeatherPlus follows **Clean Architecture** with **MVI (Model-View-Intent)** for
presentation state management.

```
app/
├── data/
│   ├── local/          # Room database, DAOs, entities
│   ├── remote/         # Retrofit API service, DTOs, mappers
│   └── repository/     # Repository implementation
├── domain/
│   ├── model/          # Pure Kotlin domain models
│   └── repository/     # Repository interface
├── ui/
│   ├── citylist/       # City list screen, ViewModel, UiState, Intent
│   ├── citydetail/     # City detail screen, ViewModel, UiState
│   ├── navigation/     # Type-safe NavRoutes and NavGraph
│   └── theme/          # Material 3 dark theme
├── worker/             # WorkManager hourly notification worker
├── notification/       # NotificationHelper
└── di/                 # Hilt dependency injection modules
```

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| State Management | MVI with StateFlow |
| Dependency Injection | Hilt |
| Networking | Retrofit + OkHttp |
| Local Storage | Room |
| Background Work | WorkManager |
| Navigation | Navigation Compose (Type-Safe, Serializable routes) |
| Image Loading | Coil |
| Testing | JUnit4 + MockK + Turbine + Coroutines Test |

---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 26+
- A free OpenWeatherMap API key

### Getting an API Key

1. Go to [https://openweathermap.org](https://openweathermap.org)
2. Click **Sign Up** and create a free account
3. After signing in go to **My Profile → API Keys**
4. Copy your default API key or generate a new one
5. **Important:** New API keys take up to **2 hours to activate**. If you get a
   401 error immediately after setup, wait 2 hours and try again.

### Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/weatherplus.git
cd weatherplus
```

2. Create a `local.properties` file in the root of the project if it does not
   already exist. It should already contain your `sdk.dir`. Add your API key:
```
sdk.dir=/your/android/sdk/path
WEATHER_API_KEY=your_openweathermap_api_key_here
```

> ⚠️ Never commit `local.properties` to version control. It is already in `.gitignore`.

3. Open the project in Android Studio and let Gradle sync complete.

4. Run the app on an emulator or physical device (API 26+).

---

## 🔔 Notification Testing

The app is configured to send a background weather notification every **15 minutes**
(WorkManager's minimum interval) for easy testing during development.

> In production this would be set to **1 hour** aligned to the top of the hour.

### How to test notifications

1. Launch the app
2. Mark at least **one city as a favourite** by tapping the star icon
3. **Close the app completely** (remove from recents)
4. Wait **up to 15 minutes**
5. A notification will appear in your notification tray showing the current
   weather conditions for your top favourite city

### If you want to test immediately

In `WorkManagerScheduler.kt` change the worker to a one-time request:

```kotlin
val request = OneTimeWorkRequestBuilder<WeatherNotificationWorker>()
    .setInitialDelay(5, TimeUnit.SECONDS)
    .build()

WorkManager.getInstance(context).enqueue(request)
```

This fires the notification after **5 seconds**. Remember to revert this before
submitting.

### Notification Permission (Android 13+)

On Android 13 and above the app will request notification permission on first launch.
Make sure to **Allow** when prompted, otherwise notifications will not appear.

---

## 🧪 Running Unit Tests

### Run all tests

```bash
./gradlew test
```

### Run with detailed output (see each test pass/fail)

```bash
./gradlew testDebugUnitTest --info
```

### Run a specific test class

```bash
./gradlew testDebugUnitTest --tests "com.emmanueliyke.weatherplus.presentation.citylist.CityListViewModelTest"
./gradlew testDebugUnitTest --tests "com.emmanueliyke.weatherplus.presentation.citydetail.CityDetailViewModelTest"
./gradlew testDebugUnitTest --tests "com.emmanueliyke.weatherplus.data.repository.WeatherRepositoryImplTest"
```

### What is tested

| Test Class | What it covers |
|---|---|
| `CityListViewModelTest` | Search filtering, case insensitivity, favorite toggle, offline state, empty state |
| `CityDetailViewModelTest` | City loading, error state, favorite toggle, loading state |
| `WeatherRepositoryImplTest` | Refresh success, favorite preservation on refresh, total failure, toggle favorite, observe cities mapping |

### Test structure

```
test/
└── com.emmanueliyke.weatherplus/
    ├── data/
    │   └── repository/
    │       └── WeatherRepositoryImplTest.kt
    ├── presentation/
    │   ├── citylist/
    │   │   └── CityListViewModelTest.kt
    │   └── citydetail/
    │       └── CityDetailViewModelTest.kt
    └── util/
        ├── TestDispatcherRule.kt
        ├── FakeWeatherRepository.kt
        └── TestData.kt
```

---

## 🌍 Cities Included

The app displays weather for the following 15 cities:

| City | Country |
|---|---|
| Lagos | Nigeria |
| London | United Kingdom |
| Dubai | UAE |
| New York | United States |
| Tokyo | Japan |
| Paris | France |
| Berlin | Germany |
| Toronto | Canada |
| Sydney | Australia |
| Mumbai | India |
| Cairo | Egypt |
| Nairobi | Kenya |
| Singapore | Singapore |
| São Paulo | Brazil |
| Mexico City | Mexico |

---

## 📶 Offline Behaviour

WeatherPlus uses a **Room-first offline strategy**:

- On launch the app immediately displays cached data from the local Room database
- It then fires API calls in parallel for all 15 cities to refresh data
- If the API calls fail (no internet) a yellow offline banner appears at the top
- The cached data remains visible and fully usable while offline
- Favorites and search work fully offline

---

## 👨🏽‍💻 Author

**Emmanuel Iyke**
Senior Android Engineer
[GitHub](https://github.com/emmanueldav) • [LinkedIn](https://linkedin.com/in/emmanueldav)

---

## 📄 License

```
MIT License — feel free to use this project as a reference or learning resource.
```
