## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.
## 2024-05-15 - [Keyset Pagination Optimization]
**Learning:** Keyset pagination queries loading full models just to resolve cursor values causes unnecessary hydration overhead. Adding a composite index matching the exact order by clause enables index-only scans.
**Action:** Use .scalar() in SQLAlchemy to fetch only necessary cursor values and add exact matching composite indexes to support index-only scans.
