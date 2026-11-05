# 03 - Detailed Test Cases Specifications

## Test Cases Suite

| Test Case ID | Scenario ID | Title | Preconditions | Steps | Test Data | Expected Result | Actual Result | Status |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC_001** | TS_001 | API Load Data Success | Mock repository returning success payload | 1. Call `loadData()` | List of 3 Movie DTOs | `movieList` updated with 3 items, `isLoading`=false, `error`=null | As Expected | **PASS** |
| **TC_002** | TS_001 | API Load Data Failure | Mock repository returning exception | 1. Call `loadData()` | `Exception("Network Error")` | `error` LiveData="Network Error", `isLoading`=false | As Expected | **PASS** |
| **TC_003** | TS_002 | Select Single Movie | ViewModel loaded with sample movies | 1. Call `toggleSelection(1)` | Movie ID = 1 | Item #1 `isSelected`=true, `toggle`=true for all items | As Expected | **PASS** |
| **TC_004** | TS_002 | Select Multiple Movies | ViewModel loaded with sample movies | 1. `toggleSelection(1)`<br>2. `toggleSelection(2)` | Movie IDs = 1, 2 | Both Item #1 and Item #2 `isSelected`=true | As Expected | **PASS** |
| **TC_005** | TS_002 | Toggle Already Selected Movie | Movie #1 is currently selected | 1. `toggleSelection(1)` | Movie ID = 1 | Item #1 `isSelected` toggles back to `false` | As Expected | **PASS** |
| **TC_006** | TS_003 | Delete Single Selected Movie | Movie #1 selected out of 3 | 1. Call `deleteItem()` | Selected Movie #1 | Movie list size decreases to 2; Movie #1 removed | As Expected | **PASS** |
| **TC_007** | TS_003 | Delete with Zero Selection | 3 movies in list, none selected | 1. Call `deleteItem()` | Zero selections | Movie list size remains 3; no items deleted | As Expected | **PASS** |
| **TC_008** | TS_004 | Search Query Update | ViewModel initialized | 1. Call `search("Inception")` | Query="Inception" | `search` LiveData receives "Inception" | As Expected | **PASS** |
| **TC_009** | TS_005 | HTTP 200 MockWebServer Integration | MockWebServer running on localhost | 1. Enqueue HTTP 200 with JSON body<br>2. Call `repository.getShows()` | JSON payload with 2 shows | Returns `ResourceResult.Success` with 2 parsed items | As Expected | **PASS** |
| **TC_010** | TS_005 | HTTP 500 MockWebServer Integration | MockWebServer running on localhost | 1. Enqueue HTTP 500 response<br>2. Call `repository.getShows()` | HTTP 500 error | Returns `ResourceResult.Error` | As Expected | **PASS** |
| **TC_011** | TS_006 | Espresso Verify Home UI Components | App launched on device/emulator | 1. Observe HomeFragment | N/A | Search field and RecyclerView visible | As Expected | **PASS** |
| **TC_012** | TS_007 | Espresso Verify Long Click Multiselect Mode | HomeFragment visible with loaded list | 1. Long click item position 0 | RecyclerView Item 0 | Delete bar container displays count "1" | As Expected | **PASS** |
| **TC_013** | TS_008 | End-to-End Multiselect & Delete Flow | App launched on emulator | 1. Long click Item 0<br>2. Click Item 1<br>3. Verify counter=2<br>4. Click Delete | Item 0 and Item 1 | Items removed, delete bar disappears, search functions | As Expected | **PASS** |
