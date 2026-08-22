## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Keyset Pagination Optimization
**Learning:** Keyset pagination queries that sort by multiple columns (e.g., `created_at DESC, id DESC`) require an explicit composite database index. Adding `index=True` to a single SQLAlchemy column is insufficient for fully optimizing multi-column sorts.
**Action:** Always create a composite index (e.g., via `__table_args__ = (Index('...', col1.desc(), col2.desc()),)`) when using multi-column sorting for pagination.
