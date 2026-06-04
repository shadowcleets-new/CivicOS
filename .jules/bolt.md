## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-04 - Keyset Pagination Indexing
**Learning:** Keysets sorted by multiple attributes (e.g. `created_at` and `id`) in large public feed datasets can suffer extreme performance drops, falling back to O(N log N) full table sorts without explicit indexes. Modifying the SQLAlchemy model definition with `index=True` does NOT automatically apply to existing database schema or raw sql init files.
**Action:** When making model changes like adding database indexes, always ensure the corresponding raw database scripts (`v1_schema.sql` or migrations) are also updated manually.
