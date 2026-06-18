## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.
## 2024-06-18 - Add index to created_at for pagination
**Learning:** Sorting by an unindexed column (like `created_at`) during keyset pagination causes an O(N log N) full table sort, which degrades performance for large tables.
**Action:** Ensure frequently sorted columns, especially those used in keyset pagination, have `index=True` in SQLAlchemy and corresponding indexes in the raw SQL schema.
