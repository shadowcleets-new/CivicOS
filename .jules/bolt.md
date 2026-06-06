## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-06 - Missing index on keyset pagination sorted column
**Learning:** In SQLAlchemy models, modifying the definition to include an index does not apply changes to the existing database. Keys sorted upon for pagination, such as `created_at` in the `grievances` table, must have an index to prevent O(N log N) full table sorts.
**Action:** Always verify that raw database schema files (e.g. `v1_schema.sql`) are updated along with the SQLAlchemy models when creating performance-critical indexes.
