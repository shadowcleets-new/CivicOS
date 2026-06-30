## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-30 - Keyset pagination database index
**Learning:** Keysert pagination queries sorting by `created_at` need a database index to prevent O(N log N) full table sorts. Simply updating the SQLAlchemy model with `index=True` doesn't automatically update existing schema files.
**Action:** Always add indexes on fields used for keyset pagination sorting, and ensure schema files (like `v1_schema.sql`) are also updated.
