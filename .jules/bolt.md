## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Missing index on keyset pagination columns
**Learning:** Implementing keyset pagination on `created_at` without indexing the column causes expensive sequential scans on large tables, defeating the performance benefits of cursor-based pagination.
**Action:** Always add database indexes (`index=True`) to columns used in `ORDER BY` clauses for pagination.
