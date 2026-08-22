## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-12 - Indexing for Keyset Pagination
**Learning:** Keyset/cursor pagination uses `ORDER BY` and inequality filters (`<`, `>`) on sorting columns (like `created_at`). Without an index on these columns, the database must perform a full table scan and sort for every page request, which becomes a severe bottleneck as the dataset grows.
**Action:** Always ensure that columns used for ordering in keyset pagination (e.g., `created_at`) have an explicit database index (e.g., `index=True` in SQLAlchemy, and a `CREATE INDEX` in SQL schemas).
