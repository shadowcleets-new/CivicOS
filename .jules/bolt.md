## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Database index for Keyset/Cursor Pagination
**Learning:** When using keyset pagination or sorting large tables (like ordering by `created_at` then `id`), omitting database indexes on the sort columns leads to expensive full table scans.
**Action:** Always ensure columns used for sorting in `order_by` clauses, particularly in pagination logic, have a database index (`index=True`).
