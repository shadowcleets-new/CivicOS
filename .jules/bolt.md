## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Keyset Pagination Indexing
**Learning:** Keyset pagination queries that sort by `created_at.desc()` cause O(N log N) full table sorts without an index on that column.
**Action:** Always ensure frequently sorted columns used in keyset pagination (like `created_at`) have `index=True` in SQLAlchemy models and corresponding `CREATE INDEX` in raw SQL schemas.
