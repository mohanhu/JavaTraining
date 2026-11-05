# 00 - Beginner's Guide to Android Testing

Welcome to Android Testing! This guide is written specifically for developers starting from scratch. It explains testing concepts using simple English, real-world MNC examples, Movie App context, and Java code snippets.

---

## 1. What is Software Testing?
Software Testing is the process of verifying that your Android application works as expected under various conditions before delivering it to end users.

- **Why it is needed:** Prevents crash reports on Google Play Console, avoids user frustration, and saves fixing costs late in development.
- **MNC Example:** Banking apps (like HSBC or Google Pay) test financial transaction flows millions of times to prevent money transfer errors.
- **Movie App Example:** Verifying that clicking the "Delete" button removes selected movies without crashing the app.

---

## 2. Manual Testing vs. Automation Testing

| Feature | Manual Testing | Automation Testing |
| :--- | :--- | :--- |
| **Execution** | Human tester taps on device screen manually | Computer code runs tests automatically |
| **Speed** | Slow (takes hours/days) | Extremely fast (takes seconds/minutes) |
| **Repeatability**| Error-prone over time | 100% consistent every time |
| **Best For** | Exploratory UI & UX feel | Regression, Unit, API, and E2E flows |

---

## 3. Test Scenarios vs. Test Cases

- **Test Scenario:** A high-level description of what feature to test.
  - *Example:* "Verify movie selection and deletion functionality."
- **Test Case:** Step-by-step set of conditions and expected results to test a specific behavior under a scenario.
  - *Example:* "Select Movie ID 1 -> Click Delete -> Assert Movie ID 1 is no longer in list."

---

## 4. The Testing Pyramid

```
        / \
       /   \        E2E Tests (10%) - Slowest, real device/emulator
      / UI  \       UI Tests (20%) - Espresso, Fragment/View tests
     /------- \     Integration Tests (20%) - Repository + API (MockWebServer)
    /  Unit    \    Unit Tests (50%) - ViewModel, Utilities (Fastest, JVM)
   /------------\
```

1. **Unit Testing (50%):** Tests single methods/classes in isolation (e.g. `HomeScreenViewModelTest.java`) running on host JVM.
2. **Integration Testing (20%):** Tests multiple components working together (e.g. `MainRepositoryIntegrationTest.java` using `MockWebServer`).
3. **UI Testing (20%):** Tests view interactions on device/emulator using Espresso (`HomeFragmentTest.java`).
4. **End-to-End (E2E) Testing (10%):** Tests complete user journey from app launch to final action (`MovieSelectionAndDeleteE2ETest.java`).

---

## 5. Core Testing Tools & Frameworks

### JUnit
JUnit is the standard testing framework for Java. It provides annotations:
- `@Test`: Marks a method as a test case.
- `@Before`: Runs code before every test method (e.g., initializing mocks).
- `@After`: Runs code after every test method (e.g., closing servers).

### Mockito & Mocking
- **What is Mocking?** Mocking creates a "fake" object that simulates real class behavior without running real logic or network calls.
- **Why Mock?** When testing `HomeScreenViewModel`, we don't want to make real API calls to internet servers. We mock `MainRepository`.
```java
// Mocking example in Java:
MainRepository mockRepository = Mockito.mock(MainRepository.class);
when(mockRepository.getShows()).thenReturn(new ResourceResult.Success<>(sampleList));
```

### MockWebServer
Spins up a real local HTTP web server on `http://localhost` inside your test process to verify Retrofit API calls, HTTP status codes (200, 400, 500), and JSON response parsing.

### Espresso
Android UI testing framework provided by Google.
- `onView(withId(R.id.et_search))`: Locates view.
- `.perform(click())`: Simulates user tap.
- `.check(matches(isDisplayed()))`: Asserts view state.

---

## 6. The Arrange-Act-Assert (AAA) Pattern

Every clean unit test follows 3 distinct steps:

```java
@Test
public void testSearchMovie() {
    // 1. ARRANGE: Set up test data & mocks
    String query = "Inception";

    // 2. ACT: Call the method being tested
    viewModel.search(query);

    // 3. ASSERT: Verify actual result matches expected result
    assertEquals("Inception", viewModel.search.getValue());
}
```

---

## 7. Key Testing Terminology

- **Assertion:** Statement verifying condition truth (e.g. `assertEquals(expected, actual)`).
- **Regression Testing:** Re-running existing tests after code changes to ensure nothing broke.
- **Smoke Testing:** Quick preliminary test to verify basic app stability.
- **Test Coverage:** Percentage of production code lines executed by tests.
- **Flaky Test:** Test that randomly passes or fails without code changes (avoid using `Thread.sleep` where possible!).
