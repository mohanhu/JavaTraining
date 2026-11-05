# 06 - MNC Standard Bug Report Template

Below is the standard Bug Defect Report format used in MNC enterprise projects (JIRA / Azure DevOps format).

---

## Sample Bug Defect Ticket

**Bug ID:** BUG-TRN-104  
**Title:** App crashes with `NullPointerException` when deleting selected items while movie search filter is active  
**Severity:** High  
**Priority:** P1 - High  
**Environment:** Android 14 (API 34), Pixel 7 Emulator, App Build v1.0.0-debug  
**Component:** `feature.home.presentation.HomeScreenViewModel`  

### Description
When a user filters movies by typing a query into the search bar, long-presses an item to enter selection mode, and clicks the "Delete" button, the app crashes due to an unhandled null list index access in ViewModel.

### Steps to Reproduce
1. Open Movie App on Android Emulator.
2. Type `"Batman"` in search input field.
3. Long press on the first filtered movie item.
4. Tap the "Delete" button.

### Expected Result
The selected movie item is deleted from the master list, search results refresh, and the delete selection bar closes smoothly without crashing.

### Actual Result
App crashes immediately. `Logcat` trace shows:
```
java.lang.NullPointerException: Attempt to invoke virtual method 'boolean java.lang.Boolean.booleanValue()' on a null object reference
    at com.codelooper.training.feature.home.presentation.HomeScreenViewModel.deleteItem(HomeScreenViewModel.java:120)
```

### Root Cause Analysis (RCA)
`deleteItem()` was attempting to inspect `.isSelected` on items filtered out of active reference list without checking for null item references.

### Proof / Artifacts
- **Logcat Dump:** `logcat_crash_BUG-TRN-104.txt`
- **Screenshot:** `bug_104_crash_screen.png`

### Fix Verification Status
- **Status:** Resolved & Verified by Automated Unit Test `testDeleteOneSelectedMovie_removesSelectedItemOnly`.
