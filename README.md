# ShippingTracker — Android Technical Assessment

A native Android application for tracking shipments in real time, built as part of a technical assessment.

## Features

- **Authentication** — Email/password login with forgot-password flow
- **Dashboard** — Active shipment list with interactive Google Maps integration
- **Shipment detail** — Real-time location tracking on map
- **Notifications** — In-app notification center

## Architecture

Clean Architecture with a unidirectional data flow:

```
ui/          → Compose screens + ViewModels (MVVM)
domain/      → Models + Repository interfaces
data/        → Retrofit API implementation + DTOs + Mappers
```

## Tech Stack

| Layer | Technology |
|---|---|
| UI | Jetpack Compose + Material 3 |
| State | ViewModel + StateFlow |
| Navigation | Compose Navigation |
| Networking | Retrofit + kotlinx.serialization |
| Maps | Maps Compose + Google Play Services |
| Language | Kotlin 2.x |
| Min SDK | 23 (Android 6.0) |

## Setup

1. Clone the repo
2. Get a Google Maps API key from [Google Cloud Console](https://console.cloud.google.com/)
3. Add it to `local.properties`:
   ```
   MAPS_API_KEY=your_key_here
   ```
4. Open in Android Studio Ladybug or later and run on a device/emulator (API 23+)
