## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.
## 2024-05-24 - Keyset Pagination Optimization
**Learning:** Using `.first()` to resolve a cursor fetches the entire entity, including large text fields. Also, missing a composite index on the exact `ORDER BY` clause causes full table scans on every page load.
**Action:** Use `.scalar()` to select only the necessary cursor column, and ensure a composite index matching the `ORDER BY` clause exists for efficient index-only scans.
