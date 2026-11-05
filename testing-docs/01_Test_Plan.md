# 01 - Master Test Plan

**Project Name:** Training (Movie App)  
**Document Version:** 1.0  
**Author:** QA Automation & Mentorship Team  
**Target Application Version:** 1.0.0  

---

## 1. Objective
The primary objective of this Test Plan is to outline the testing strategy, scope, environment, and execution framework for the Movie Android Application. It ensures high software quality, architecture compliance, robust error handling, and flawless user interaction.

---

## 2. Scope

### 2.1 In Scope
- **Unit Testing:** `HomeScreenViewModel` selection, deletion, search, and API state mapping logic.
- **Integration Testing:** `DefaultMainRepository` and `DefaultMainRemoteDataSource` interaction with Retrofit using `MockWebServer`.
- **UI Testing:** `HomeFragment` RecyclerView rendering, checkbox states, delete bar visibility, and search debounce.
- **End-to-End (E2E) Testing:** Complete end-to-end user flow: Launch -> Load -> Long Click -> Multiselect -> Delete -> Verify.

### 2.2 Out of Scope
- Performance benchmarking under 100,000 items.
- Payment gateway integrations (not present in current app).
- Multi-language localization / internationalization (RTL layout verification).

---

## 3. Test Strategy & Architecture

```
+-------------------------------------------------------------------+
|                        Master Test Strategy                       |
+-------------------------------------------------------------------+
| 1. Unit Tests (JVM): JUnit 4 + Mockito + InstantTaskExecutorRule  |
| 2. Integration Tests (JVM): Retrofit + Gson + MockWebServer       |
| 3. UI Tests (Android): Espresso + ActivityScenarioRule            |
| 4. E2E Tests (Android): Espresso Full System Flow                 |
+-------------------------------------------------------------------+
```

---

## 4. Test Environment Requirements

| Environment Component | Specification |
| :--- | :--- |
| **JDK Version** | Java 17 |
| **Android SDK Target** | API Level 36 (Android 16) |
| **Minimum SDK** | API Level 26 (Android 8.0) |
| **Test Runner** | AndroidJUnitRunner / JUnit 4 |
| **Device / Emulator** | Pixel 6 Emulator (API 34/35/36) |

---

## 5. Entry & Exit Criteria

### Entry Criteria
- Build completes with zero compilation errors.
- Gradle sync is successful.
- Test dependencies (`JUnit`, `Mockito`, `MockWebServer`, `Espresso`) declared in `build.gradle.kts`.

### Exit Criteria
- 100% of critical path Unit and Integration tests PASS.
- Minimum 85% Code Coverage on ViewModel and Repository logic.
- Zero open Critical or High severity defects.
