## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2026-07-01 - Keyset Pagination Indexing
**Learning:** Keyset pagination queries sorting by `created_at DESC, id DESC` require an explicit composite database index. Relying on SQLAlchemy to query large unindexed tables results in O(N log N) full table sorts, creating a significant performance bottleneck for public feeds.
**Action:** Always add `index=True` to frequently sorted columns (like `created_at`) in SQLAlchemy models and ensure corresponding `CREATE INDEX` statements are added to raw SQL schema scripts.
