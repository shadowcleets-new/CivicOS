## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-07-27 - Indexing Paginated Endpoints
**Learning:** Adding an index on `created_at` significantly improves keyset/cursor pagination performance, as the database no longer needs to perform a full table scan to order by date before applying the limit.
**Action:** Always add an index to fields (like `created_at` or `id`) that are used in `ORDER BY` clauses, particularly for endpoints that rely on keyset pagination and are expected to scale.
