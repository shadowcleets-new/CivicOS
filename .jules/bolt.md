## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Add index to created_at for keyset pagination
**Learning:** In SQLAlchemy models, frequently sorted columns (like `created_at` used for keyset pagination) must have `index=True` to prevent O(N log N) full table sorts, particularly for performance-critical public feeds. Modifying the SQLAlchemy model alone does not apply to the DB, raw SQL schema files (e.g. `v1_schema.sql`) must also be updated.
**Action:** Always ensure indexes are added to both SQLAlchemy models and raw SQL schema definitions for columns used in `.order_by()` clauses for pagination.
