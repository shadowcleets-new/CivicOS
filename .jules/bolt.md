## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Missing Index on Keyset Pagination Field
**Learning:** When using keyset/cursor pagination (e.g., `order_by(created_at.desc(), id.desc())`), omitting an index on the primary sort column (`created_at`) forces the database to perform a full table scan or file sort for every page request, which severely degrades performance on large datasets.
**Action:** Always ensure database indexes are applied to fields that are frequently used in `ORDER BY` clauses for pagination.
