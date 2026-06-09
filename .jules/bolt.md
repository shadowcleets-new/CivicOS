## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-10 - Missing Indexes for Keyset Pagination
**Learning:** Frequently sorted columns used for keyset pagination (like `created_at`) can cause O(N log N) full table sorts if not indexed, which creates severe performance bottlenecks on public feeds.
**Action:** Always add `index=True` to sorted columns in SQLAlchemy models, and ensure corresponding manual SQL schema scripts like `v1_schema.sql` are also updated with `CREATE INDEX` statements directly after the `CREATE TABLE` definition.
