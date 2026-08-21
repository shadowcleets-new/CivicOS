## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.
## 2024-05-23 - Index on pagination cursor columns
**Learning:** SQLAlchemy models using keyset/cursor pagination based on time (like `created_at`) can suffer from full table scans if those columns are not indexed.
**Action:** Always ensure columns used for sorting in paginated queries (e.g., `created_at`, `updated_at`) have a database index (`index=True`).
