## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-11 - Database Indexes for Keyset Pagination
**Learning:** When implementing keyset/cursor pagination that sorts and filters by a specific column (like `created_at`), that column MUST have a database index to prevent full table scans and expensive sort operations on large datasets.
**Action:** Always verify that columns used in pagination `order_by` clauses have `index=True` in the SQLAlchemy model definition.
