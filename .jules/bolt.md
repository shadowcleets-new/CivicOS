## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2026-07-20 - Composite Indexes for Keyset Pagination
**Learning:** Keyset pagination queries that sort by multiple columns (e.g., `created_at DESC, id DESC`) require an explicit composite database index in the raw SQL schema to be fully optimized. Adding `index=True` to a single SQLAlchemy column is insufficient for multi-column sorts.
**Action:** Always create a composite index using `Index(..., col1.desc(), col2.desc())` when implementing keyset pagination on multiple columns to ensure database-level optimization.
