## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-18 - Composite Index for Keyset Pagination
**Learning:** Keyset pagination queries that sort by multiple columns (e.g., `created_at DESC, id DESC`) require an explicit composite database index in the raw SQL schema. Adding `index=True` to a single SQLAlchemy column is insufficient for fully optimizing multi-column sorts, as it forces the database to perform an inefficient in-memory sort.
**Action:** Always verify if multi-column sorting operations in frequently accessed queries (like pagination) have corresponding composite indexes, and use `__table_args__ = (Index(...),)` in SQLAlchemy to define them.
