## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.
## 2024-05-24 - Keyset Pagination Optimization
**Learning:** When optimizing SQLAlchemy models for keyset/cursor pagination, ensure that the columns used for sorting (like `created_at`) have a database index (`index=True`) to prevent full table scans and sort operations.
**Action:** Always verify database indexes on sorting columns when implementing or reviewing keyset pagination.
