# 08 - How to Run Tests in Android Studio & Terminal

This document provides step-by-step instructions on how to execute all newly created test suites.

---

## 1. Prerequisites & Preparation

1. Open **Android Studio**.
2. Open your project: `/Users/mohan/AndroidStudioProjects/Training`.
3. Wait for **Gradle Sync** to finish.
4. Verify project builds cleanly via menu: **Build -> Rebuild Project**.

---

## 2. Running Unit Tests (JVM)

Unit tests run on your local computer CPU (no emulator required) and complete in seconds.

### Option A: From Android Studio GUI
1. In the Project tool window (left panel), navigate to:
   `app -> src -> test -> java -> com.codelooper.training -> feature -> home -> presentation`
2. Right-click on `HomeScreenViewModelTest.java`.
3. Click **Run 'HomeScreenViewModelTest'**.
4. The **Run** tab at the bottom of Android Studio will open showing green checkmarks for all 16 test cases!

### Option B: From Terminal (Gradle Command)
Open the Terminal inside Android Studio or terminal emulator, and run:
```bash
./gradlew testDebugUnitTest
```
To run only `HomeScreenViewModelTest`:
```bash
./gradlew testDebugUnitTest --tests "com.codelooper.training.feature.home.presentation.HomeScreenViewModelTest"
```

---

## 3. Running Integration Tests (MockWebServer)

Integration tests verify repository network parsing using local HTTP server mock sockets.

### Option A: From Android Studio GUI
1. Navigate to:
   `app -> src -> test -> java -> com.codelooper.training -> feature -> train -> data -> repository`
2. Right-click on `MainRepositoryIntegrationTest.java`.
3. Click **Run 'MainRepositoryIntegrationTest'**.

### Option B: From Terminal
```bash
./gradlew testDebugUnitTest --tests "com.codelooper.training.feature.train.data.repository.MainRepositoryIntegrationTest"
```

---

## 4. Running UI Tests (Espresso) & E2E Tests

UI & E2E tests require a running **Android Emulator** or physical USB device.

### Step 1: Start Android Emulator
1. Open **Device Manager** in Android Studio.
2. Click the Play button next to your Pixel emulator.
3. Wait for the emulator home screen to appear.

### Step 2: Run Espresso UI Tests
1. In Android Studio, navigate to:
   `app -> src -> androidTest -> java -> com.codelooper.training -> feature -> home -> presentation`
2. Right-click on `HomeFragmentTest.java`.
3. Click **Run 'HomeFragmentTest'**.
4. Observe the emulator screen as Espresso automatically taps, searches, long-presses, and deletes items!

### Step 3: Run E2E Test
1. Navigate to:
   `app -> src -> androidTest -> java -> com.codelooper.training -> e2e`
2. Right-click on `MovieSelectionAndDeleteE2ETest.java`.
3. Click **Run 'MovieSelectionAndDeleteE2ETest'**.

### Option B: From Terminal (All Android Instrumented Tests)
```bash
./gradlew connectedDebugAndroidTest
```

---

## 5. Viewing Test Reports

After running unit tests via Gradle terminal commands, Android Studio generates HTML test reports:
- Report path: `app/build/reports/tests/testDebugUnitTest/index.html`
- Open this file in your Web Browser (Chrome / Safari) to view execution charts, timing details, and passing status!
