## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-07-30 - Pagination Performance Bottleneck
**Learning:** Keyset/cursor pagination querying on large tables without indexes on the sorted columns (`created_at`) forces the database to perform a full table scan and in-memory sort, destroying performance at scale.
**Action:** Always add an index to columns used in `order_by` clauses, especially timestamps used for cursor pagination.
