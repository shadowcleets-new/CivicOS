## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2026-08-10 - Keyset Pagination Indexing
**Learning:** Keyset pagination queries using `created_at` were performing full table scans and sorts because the column lacked a database index.
**Action:** Always ensure columns used in `order_by` for keyset/cursor pagination have `index=True` to prevent full table scans and enable efficient O(log N) lookups.
