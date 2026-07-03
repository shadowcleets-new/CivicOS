## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Composite Indexes for Keyset Pagination
**Learning:** Keyset pagination queries that sort by multiple columns (e.g., `created_at DESC, id DESC`) require an explicit composite database index in the raw SQL schema. Adding `index=True` to a single SQLAlchemy column is insufficient for fully optimizing multi-column sorts.
**Action:** Always add a composite index in the raw SQL schema files when optimizing pagination queries with multiple sort conditions.
