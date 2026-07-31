## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Add database index on pagination fields
**Learning:** Fields used for cursor pagination and default sorting (like `created_at`) cause full table scans if unindexed, degrading read performance linearly as the table grows.
**Action:** Always add `index=True` to timestamp columns used in `order_by` and cursor `filter` clauses for pagination endpoints.
