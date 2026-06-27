## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-23 - Indexing Pagination Sort Keys
**Learning:** Frequently sorted columns (like `created_at` used for keyset pagination) need explicit indexes to prevent O(N log N) full table sorts, especially for performance-critical public feeds.
**Action:** Always add `index=True` to SQLAlchemy models and `CREATE INDEX` in raw SQL schema for columns used in `ORDER BY` clauses for pagination.
