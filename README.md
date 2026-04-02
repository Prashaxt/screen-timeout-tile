# Screen Timeout Tile

A minimal Android Quick Settings tile that lets you cycle through screen timeout durations with a single tap — no need to dig into Settings every time.

---

## What it does

Adds a tile to your Quick Settings panel. Every tap cycles through a set of screen timeout values:

| Tap | Timeout |
|-----|---------|
| 1 | 15 seconds |
| 2 | 30 seconds |
| 3 | 1 minute |
| 4 | 2 minutes |
| 5 | 5 minutes |
| 6 | 10 minutes |

Each level has its own icon so you can tell at a glance what timeout is currently active.

---

## Features

- One tap to change screen timeout directly from Quick Settings
- 6 timeout presets with matching icons
- Syncs with system timeout on tile load — stays accurate even if you change it elsewhere
- Shows a custom icon if the system timeout doesn't match any preset
- Permission check built in — tile shows as unavailable if permission is revoked

---

## Setup

### 1. Grant Permission

On first launch, the app will ask for **Write Settings** permission. Tap **Grant Permission** and allow it from the system screen.

### 2. Add the Tile

1. Swipe down twice to open the Quick Settings panel
2. Tap the pencil / edit icon to enter edit mode
3. Find **Screen Timeout** in the inactive tiles
4. Drag it into your active tiles where WiFi and Bluetooth are, tap Done

### 3. Use It

Tap the tile anytime to cycle to the next timeout duration.

---

## Requirements

- Android 7.0 (API 24) or higher
- Write Settings permission

---

## Tech Stack

- Kotlin
- Jetpack Compose
- TileService API
- Material3

---

## Project Structure

```
app/src/main/java/com/example/screentimeouttile/
├── MainActivity.kt         # Permission screen + onboarding UI
└── TimeoutTileService.kt   # Quick Settings tile logic
```

---

## Build

```bash
# Debug
./gradlew assembleDebug

# Release
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/`

---

## License

MIT