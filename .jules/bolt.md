## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Missing index on frequently sorted column
**Learning:** Frequently sorted columns (like `created_at` used for keyset pagination in public feeds) require database indexes to prevent O(N log N) full table sorts.
**Action:** Always add `index=True` to SQLAlchemy models and the corresponding `CREATE INDEX` statement in raw SQL schemas for columns used in `ORDER BY` clauses for pagination.
