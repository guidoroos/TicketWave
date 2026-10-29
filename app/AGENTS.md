# Project Context & AI Coding Guidelines

## 0. Project
The project is an app for buying concert tickets via an api. 
The app has the following screens: 
- LoginScreen (username and password, submitbutton, no new registrations)
- HomeScreen (is bottombar first item, searchbar, chips, list of events sorted by recency)
- TicketsScreen (is bottombar second item, list of purchased tickets, linking to detail, no further actions)
- BasketScreen (is bottombar third item, incremented/decrement ticket count, remove ticket, set payment method, continue to checkout)
- CheckoutScreen (only from BasketScreen, overview of basket, change payment method, confirm payment)
- PaymentMethodScreen (current card if available, input creditcard data, change card, cancelbutton)
- EventDetailScreen (event data, available ticket types with price,  add to basket, add to basket and pay  )
- SettingScreen (userdata, payment method link, my tickets link, logout)


## 1. Core Tech Stack
- **Language:** Kotlin 2.4+ (Strict Null-Safety & Idiomatic Kotlin)
- **UI Framework:** Jetpack Compose (Material 3)
- **Architecture:** MVVM + Clean Architecture (Unidirectional Data Flow)
- **Dependency Injection:** Koin (DSL-based injection)
- **Networking:** ApolloKotlin for graphql + kotlinx.serialization 
- **Async & Reactive:** Kotlin Coroutines & Flow 
- **Lazy Image Loading** Coil

---

## 2. Architecture & Layer Boundaries

### Presentation Layer (`ui/`)
- Pure Jetpack Compose UI. **No business or network logic inside Composables.**
- ViewModels expose UI state using `StateFlow` and single-shot events using `SharedFlow` or `Channel`.
- All Composables must be **stateless** (enforce State Hoisting). Pass lambdas for user actions.
- Use `collectAsStateWithLifecycle()` in Compose to observe flows safely.
- When fetching data, do so via flow variable, instead of in viewmodel init method or via launchedeffect in view

### Domain Layer (`domain/`)
- Pure Kotlin code (**Zero Android or UI imports**).
- Contains Domain Models, Repository Interfaces, and Use Cases (`operator fun invoke()`).
- High-level business logic lives here. Repositories in this layer return `Result<DomainModel>`.

## 3. API layer
- GraphQL is used, the schema is in api/src/main/graphql/schema.graphql
