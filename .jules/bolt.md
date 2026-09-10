## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2025-02-27 - Optimize keyset pagination query
**Learning:** Hydrating full SQLAlchemy models to just extract a timestamp cursor adds unnecessary overhead.
**Action:** Use `.scalar()` to query only the required timestamp column for keyset pagination, and pair it with a composite index matching the `ORDER BY` clause to avoid full table scans.
