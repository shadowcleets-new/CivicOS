## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-23 - Index on pagination sort columns
**Learning:** When using keyset/cursor pagination, columns used for sorting like `created_at` require database indexes to prevent full table scans.
**Action:** Always add `index=True` to SQLAlchemy columns used in the `order_by` clause for pagination.
