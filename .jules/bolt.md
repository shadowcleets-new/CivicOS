## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Keyset Pagination Indexing
**Learning:** Keyset pagination queries that sort by multiple columns (e.g., `created_at DESC, id DESC`) require an explicit composite database index to prevent full table scans and optimize lookups.
**Action:** Always add a composite `Index` in SQLAlchemy `__table_args__` for the exact columns and sort directions used in keyset pagination.
