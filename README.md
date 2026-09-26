## Download

[Download the latest APK](https://github.com/priyankJhajharia/NoteKeeper/releases/latest)

# NoteKeeper 📝

A modern Android notes app built entirely in Jetpack Compose, with Firebase cloud sync and REST API integration.

## Features

- ✅ Create, edit, delete, and search notes
- ✅ Firebase Authentication (email/password)
- ✅ Firestore cloud sync — notes persist across reinstalls and devices
- ✅ Per-user data isolation
- ✅ REST API integration via Retrofit (live quote fetch)
- ✅ Custom Material3 theme with light/dark mode support
- ✅ Offline-first architecture (Room + Firestore)

## Tech Stack

- **UI:** Jetpack Compose, Material3, Compose Navigation
- **Architecture:** MVVM, Repository pattern
- **Local storage:** Room
- **Cloud:** Firebase Authentication, Cloud Firestore
- **Networking:** Retrofit, Gson, Kotlin Coroutines
- **Language:** Kotlin

## Architecture

The app follows an offline-first pattern:
- Room serves as the local cache for instant, offline access
- Firestore acts as the source of truth for cross-device sync
- On login, cloud data syncs down to Room; on every write, changes sync up to Firestore

## Screens

| Login | Notes List | Add/Edit Note |
|-------|-----------|---------------|
| Email/password auth | Search, view, delete notes | Create or edit notes |

## Setup

1. Clone the repo
2. Add your own `google-services.json` to the `app/` directory (Firebase project required)
3. Enable Email/Password auth and Firestore in your Firebase Console
4. Run the app

## Notes on scope

This is a portfolio project built to demonstrate Compose, Firebase, and Retrofit integration end-to-end. Firestore security rules are scoped per-user but kept simple for demo purposes; no automated test suite is included yet.

---
Built by [Priyank Jhajharia](https://github.com/priyankJhajharia)
