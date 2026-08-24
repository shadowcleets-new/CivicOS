## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-24 - Optimize keyset pagination with created_at index
**Learning:** Keyset/cursor pagination endpoints that sort by `created_at.desc()` cause full table scans if the column isn't indexed, which severely impacts read performance at scale.
**Action:** Ensure that columns used for sorting keyset pagination queries (like `created_at`) always have `index=True` in SQLAlchemy models.
