## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Optimize keyset pagination sorting
**Learning:** Columns used for sorting in keyset/cursor pagination (like `created_at`) must have a database index (`index=True` in SQLAlchemy) to prevent full table scans and expensive sort operations as the table grows.
**Action:** Always verify that pagination sorting columns are indexed when reviewing database models.
