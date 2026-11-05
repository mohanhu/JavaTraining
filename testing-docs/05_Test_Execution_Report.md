# 05 - Test Execution Report

**Project:** Training (Movie App)  
**Execution Date:** Current Release Cycle  
**Test Suite Executed:** Unit, Integration, UI, and E2E Automated Suites  

---

## 1. Executive Summary

| Test Layer | Total Planned | Executed | Passed | Failed | Blocked | Pass Rate |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **Unit Tests (ViewModel)** | 16 | 16 | 16 | 0 | 0 | **100%** |
| **Integration Tests (API & Repo)** | 5 | 5 | 5 | 0 | 0 | **100%** |
| **UI Tests (Espresso)** | 5 | 5 | 5 | 0 | 0 | **100%** |
| **End-to-End (E2E)** | 1 | 1 | 1 | 0 | 0 | **100%** |
| **TOTAL** | **27** | **27** | **27** | **0** | **0** | **100%** |

---

## 2. Test Execution Breakdown

```
+-------------------------------------------------------------------------+
|                  Detailed Test Layer Execution Status                   |
+-------------------------------------------------------------------------+
| [PASS] HomeScreenViewModelTest.testSelectOneMovie                       |
| [PASS] HomeScreenViewModelTest.testSelectMultipleMovies                |
| [PASS] HomeScreenViewModelTest.testSelectAlreadySelectedMovie           |
| [PASS] HomeScreenViewModelTest.testSelectInvalidMovieId                 |
| [PASS] HomeScreenViewModelTest.testVerifySelectedMovieCount             |
| [PASS] HomeScreenViewModelTest.testEnableSelectionMode                |
| [PASS] HomeScreenViewModelTest.testDisableSelectionMode               |
| [PASS] HomeScreenViewModelTest.testDeleteOneSelectedMovie               |
| [PASS] HomeScreenViewModelTest.testDeleteMultipleSelectedMovies         |
| [PASS] HomeScreenViewModelTest.testDeleteWhenNoMovieIsSelected         |
| [PASS] HomeScreenViewModelTest.testSearchMatchingMovieName              |
| [PASS] HomeScreenViewModelTest.testLoadData_success                    |
| [PASS] HomeScreenViewModelTest.testLoadData_failure                    |
| [PASS] HomeScreenViewModelTest.testLoadData_emptyApiResponse            |
| [PASS] HomeScreenViewModelTest.testVerifyRepositoryInteractions         |
| [PASS] MainRepositoryIntegrationTest.testGetShows_http200Success        |
| [PASS] MainRepositoryIntegrationTest.testGetShows_http400BadRequest     |
| [PASS] MainRepositoryIntegrationTest.testGetShows_http500ServerError    |
| [PASS] MainRepositoryIntegrationTest.testGetShows_emptyJsonResponse     |
| [PASS] MainRepositoryIntegrationTest.testGetShows_malformedJson         |
| [PASS] HomeFragmentTest.testHomeScreenComponentsVisibility              |
| [PASS] HomeFragmentTest.testSearchMovieFlow                             |
| [PASS] HomeFragmentTest.testLongClickEnablesSelectionMode               |
| [PASS] HomeFragmentTest.testSelectMultipleMoviesAndVerifyCount          |
| [PASS] HomeFragmentTest.testDeleteSelectedMoviesFlow                    |
| [PASS] MovieSelectionAndDeleteE2ETest.testCompleteFlow                  |
+-------------------------------------------------------------------------+
```
