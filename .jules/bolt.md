## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Keyset Pagination Index Missing
**Learning:** Keyset pagination endpoints using `created_at` sorting lack database indexes on the timestamp column, which causes full table scans during cursor pagination.
**Action:** Always add `index=True` to timestamp columns used for cursor-based pagination and update raw SQL schemas to prevent O(N log N) sorts.
