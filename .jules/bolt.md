## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-14 - Indexing columns used in keyset pagination sorting
**Learning:** The `Grievance` endpoint uses `created_at` for ordering in cursor-based pagination. Without an index on `created_at`, the database performs a full table scan and in-memory sort which becomes a bottleneck on large datasets.
**Action:** Always add `index=True` to SQLAlchemy model columns used for sorting and keyset pagination to prevent full table scans.
