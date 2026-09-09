## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Keyset Pagination Index Optimization
**Learning:** When using keyset pagination (cursor-based), a composite index on the exact sort keys (e.g., created_at DESC, id DESC) is necessary to avoid full table scans and expensive sort operations.
**Action:** Always add a composite index matching the exact ORDER BY clause when implementing keyset pagination.
