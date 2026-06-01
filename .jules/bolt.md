## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-01 - Add Database Index on frequently sorted column for Keyset Pagination
**Learning:** In SQLAlchemy models, columns that are frequently used for ordering (like `created_at` used for keyset pagination queries) can lead to O(N log N) full table sorts, especially for performance-critical public feeds.
**Action:** Ensure frequently sorted columns in SQLAlchemy models have `index=True` configured to prevent full table sorts and improve query execution times on large datasets.
