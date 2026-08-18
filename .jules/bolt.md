## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Keyset Pagination Indexing
**Learning:** When using keyset/cursor pagination sorting by `created_at`, omitting a database index on the sort column causes expensive full table scans and O(N log N) in-memory sorts.
**Action:** Always add `index=True` to columns used as primary sort keys in keyset pagination to ensure O(log N) lookups.
