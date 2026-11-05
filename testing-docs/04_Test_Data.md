# 04 - Test Data Documentation

This document defines mock datasets and JSON payloads used across Unit, Integration, UI, and E2E testing suites.

---

## 1. Mock Movie List (Unit Tests)

```json
[
  {
    "id": 1,
    "name": "Inception",
    "image": "https://example.com/inception.jpg",
    "rating": 8.8,
    "toggle": false,
    "isSelected": false
  },
  {
    "id": 2,
    "name": "Interstellar",
    "image": "https://example.com/interstellar.jpg",
    "rating": 8.6,
    "toggle": false,
    "isSelected": false
  },
  {
    "id": 3,
    "name": "The Dark Knight",
    "image": "https://example.com/darkknight.jpg",
    "rating": 9.0,
    "toggle": false,
    "isSelected": false
  }
]
```

---

## 2. MockWebServer HTTP 200 API Response Body

```json
[
  {
    "id": 101,
    "name": "Breaking Bad",
    "image": {
      "medium": "https://example.com/bb.jpg"
    },
    "rating": {
      "average": 9.5
    }
  },
  {
    "id": 102,
    "name": "Better Call Saul",
    "image": {
      "medium": "https://example.com/bcs.jpg"
    },
    "rating": {
      "average": 8.9
    }
  }
]
```

---

## 3. MockWebServer Malformed JSON Response Payload

```json
[
  {
    "id": 101,
    "name": "Unfinished JSON string object...
```
