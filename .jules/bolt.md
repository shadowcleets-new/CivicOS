## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-22 - Index created_at for Keysets
**Learning:** Keyset pagination queries that sort by `created_at` will trigger O(N log N) full table scans if the column is not indexed, causing severe performance bottlenecks on large public feeds.
**Action:** Always ensure that columns used for sorting in frequently hit pagination endpoints (like `created_at` and `id`) have `index=True` in the SQLAlchemy model and corresponding database schema files.
