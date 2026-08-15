## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2026-08-15 - Add missing database index on cursor pagination sort column
**Learning:** Keyset/cursor pagination queries that order by unindexed columns (like `created_at`) will cause expensive full table scans and sort operations in the database as the dataset grows.
**Action:** Ensure columns used for sorting in paginated endpoints (e.g. `created_at`) always have `index=True` set in their SQLAlchemy models.
