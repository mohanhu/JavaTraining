# 02 - Test Scenarios Matrix

| Scenario ID | Category | Description | Priority |
| :--- | :--- | :--- | :--- |
| **TS_001** | Unit (ViewModel) | Verify movie data loading, success response, error handling, and loading state transitions. | High |
| **TS_002** | Unit (ViewModel) | Verify movie selection toggling, single item select, multi-select, and invalid ID handling. | High |
| **TS_003** | Unit (ViewModel) | Verify movie deletion logic for single item, multiple items, and zero items selected. | High |
| **TS_004** | Unit (ViewModel) | Verify movie search input handling across matching, non-matching, and empty query strings. | Medium |
| **TS_005** | Integration (API) | Verify Repository response mapping across HTTP 200, 400, 500, empty body, and malformed JSON using MockWebServer. | High |
| **TS_006** | UI (Espresso) | Verify HomeFragment view hierarchy elements (Search bar, RecyclerView, Delete container). | Medium |
| **TS_007** | UI (Espresso) | Verify UI multi-selection mode activation on item long-press. | High |
| **TS_008** | E2E (System) | Verify complete user workflow of browsing, selecting multiple movies, deleting, and searching. | Critical |
