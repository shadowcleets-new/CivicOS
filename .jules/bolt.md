## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-08 - Keyset Pagination Indexing
**Learning:** Keyset/cursor pagination requires database indexes on sorted columns (like `created_at`) to avoid full table scans and expensive sort operations.
**Action:** Always add `index=True` to SQLAlchemy columns used in `order_by` clauses for paginated endpoints.
