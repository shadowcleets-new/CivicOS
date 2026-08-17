## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-17 - Missing database index on keyset pagination
**Learning:** When optimizing SQLAlchemy models for keyset/cursor pagination, ensure that columns used for sorting (e.g., `created_at`) have a database index to prevent full table scans.
**Action:** Always add `index=True` to SQLAlchemy columns used in `.order_by()` clauses for large datasets.
