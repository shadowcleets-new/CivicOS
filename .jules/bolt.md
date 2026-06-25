## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.
## 2026-06-25 - Add index on created_at for grievances keyset pagination
**Learning:** Keyset pagination requires sorting by `created_at`, which causes an O(N log N) full table sort when unindexed.
**Action:** Always ensure frequently sorted columns used in pagination have an index, and that `index=True` is applied in SQLAlchemy models and index creation in DB schema.
