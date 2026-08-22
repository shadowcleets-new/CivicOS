## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2026-08-13 - Keyset pagination performance bottleneck
**Learning:** Using keyset/cursor pagination relies on sorting and filtering by timestamp columns like `created_at`. Without an index on these columns, the database must perform a full table scan and sort, negating the performance benefits of cursor pagination as the table grows.
**Action:** Always add `index=True` to columns used for sorting and filtering in cursor-based pagination to ensure O(log n) performance instead of O(n) full table scans.
