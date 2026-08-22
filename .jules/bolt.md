## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2026-08-09 - Indexing Keyset Pagination Columns
**Learning:** Endpoints implementing keyset/cursor pagination rely on columns used for sorting. If these columns (like `created_at`) lack an index, it triggers expensive full table scans and sort operations.
**Action:** Always ensure that columns used in pagination sorting (`ORDER BY`) have a database index (e.g., `index=True` in SQLAlchemy) to maintain scalable performance.
