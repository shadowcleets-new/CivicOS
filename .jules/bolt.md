## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2026-06-26 - Missing index on keyset pagination sort keys
**Learning:** Missing index on pagination sort keys (created_at) causes O(N log N) full table sorts.
**Action:** Add index=True to the SQLAlchemy model and update the schema file.
