## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-10-26 - Add index to created_at for cursor pagination
**Learning:** Keyset/cursor pagination relies heavily on sorting by specific columns (like `created_at` and `id`). Without an index on these columns, the database must perform a full table scan and sort the entire dataset for every page requested, which causes significant performance degradation as the dataset grows.
**Action:** Always add an index (`index=True` in SQLAlchemy) to columns used for sorting in keyset/cursor pagination queries.
