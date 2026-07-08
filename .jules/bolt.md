## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2026-07-08 - Composite index for Keyset Pagination
**Learning:** Keyset pagination queries that sort by multiple columns require an explicit composite database index in the raw SQL schema (e.g., CREATE INDEX ON table (created_at DESC, id DESC)). Adding index=True to a single SQLAlchemy column is insufficient.
**Action:** Ensure both the SQLAlchemy model and raw SQL schema have composite indexes for pagination fields.
