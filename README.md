# TicketWave

TicketWave is a modern Android application built with Jetpack Compose and Clean Architecture, designed for discovering events, managing shopping baskets, secure checkout, and digital ticket wallets with barcode generation.

---

## Architecture & Technical Choices

* **Jetpack Compose (UI):** 
  The standard declarative UI toolkit for Android. It eliminates manual UI tree updates and maximizes performance by only recomposing affected components. It encourages building small, reusable, stateless composables.

* **MVVM with Clean Architecture:** 
  Provides a clear separation of concerns, making the codebase structured, maintainable, and easy for new developers to onboard and understand.

* **Multi-Module Project (`:app`, `:ui`, `:domain`, `:api`):** 
  Structured into multiple modules to significantly improve build times as the project grows. Changes in downstream modules prevent unnecessary recompilation in upstream modules.

* **Apollo Kotlin (Network):** 
  A robust GraphQL client chosen for handling type-safe queries and mutations with seamless Kotlin integration.

* **Kotlin Coroutines & Flows (Concurrency):** 
  The modern standard for asynchronous programming. Offers structured concurrency, allowing asynchronous code to be written sequentially, with smooth integration into Compose states and unidirectional data flow (UDF).

* **Coil (Image Loading):** 
  A lightweight, fast, and modern image loading library built natively for Jetpack Compose.

* **Kotlinx Serialization:** 
  A Kotlin-first serialization library operating without runtime reflection overhead.

* **Koin (Dependency Injection):** 
  Selected for its simplicity, fast setup, and ease of configuration. *(Note: While Dagger/Hilt generates code at compile-time for runtime speed, Koin was chosen here for developer velocity and minimal boilerplate).*

* **Jetpack DataStore:** 
  Used for securely persisting authentication tokens across app sessions.

---

## Future Improvements

* **Advanced Caching & Offline Support:** 
  Expand beyond Apollo's in-memory normalized cache by integrating Room as a local SQLite database, paired with `WorkManager` for offline synchronization with the backend.
* **Dependency Injection Migration:** 
  Consider migrating from Koin to Dagger/Hilt for compile-time dependency verification and enhanced runtime performance.
* **UI Performance Optimization:** 
  Audit and optimize UI rendering to eliminate any unnecessary recompositions.
* **Comprehensive Testing Suite:** 
  Extend unit test coverage to ViewModels and add instrumented UI tests alongside screenshot testing.
* **Accessibility & Contrast Audit:** 
  Enhance accessibility tags, content descriptions, and verify color contrast ratios across all themes.
* **Secure Token Storage:** 
  Upgrade token persistence to use `EncryptedSharedPreferences` or Encrypted DataStore for heightened security.
* **Expanded Unit Testing:** 
  Further expand the unit test suite with rigorous tests for ViewModel state management and domain logic.
