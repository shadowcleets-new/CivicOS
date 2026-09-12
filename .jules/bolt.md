## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.
## 2024-05-24 - Optimize keyset pagination
**Learning:** In SQLAlchemy, fetching entire entities just to resolve a cursor timestamp adds unnecessary hydration overhead, and missing composite indexes on keyset pagination fields leads to expensive sorts.
**Action:** Use `.scalar()` to select only necessary columns and always ensure a composite index matching the exact `ORDER BY` clause exists for pagination.
