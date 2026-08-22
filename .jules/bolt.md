## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-07 - Indexing sorting columns for keyset pagination
**Learning:** When using keyset/cursor pagination on endpoints (e.g., `/api/v1/grievances/`), the SQLAlchemy models must have database indexes (e.g., `index=True`) on the columns used for sorting (like `created_at`) to prevent full table scans and slow sort operations.
**Action:** Always verify that columns used in `.order_by()` and keyset cursor comparisons are indexed in the database model.
