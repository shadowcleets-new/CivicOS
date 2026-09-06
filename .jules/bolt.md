## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-09-06 - Optimize SQLAlchemy keyset pagination
**Learning:** When resolving cursors for keyset pagination, fetching entire ORM objects hydtrates large fields (like text or blobs) unnecessarily. Selecting only the needed timestamp column using `.scalar()` significantly reduces overhead. Additionally, a composite index matching the exact ORDER BY clause (created_at DESC, id DESC) is crucial to prevent expensive sorts and enable index-only scans.
**Action:** Use `.scalar()` on the specific column needed for cursor resolution and ensure matching composite indexes exist for pagination queries.
