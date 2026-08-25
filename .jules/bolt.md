## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-18 - Database Index for Cursor Pagination
**Learning:** Keys (like `created_at`) used in cursor-based pagination queries for sorting or filtering MUST be indexed to avoid sequential table scans.
**Action:** Always add `index=True` to columns heavily used in order_by or inequality filters for pagination.
