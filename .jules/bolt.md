## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-25 - Keyset Pagination Query Sorting Optimization
**Learning:** Missing database indexes on fields frequently used for query sorting (like `created_at` used for keyset pagination) cause O(N log N) full table sorts, becoming a critical performance bottleneck for public feeds as the dataset grows.
**Action:** Always add `index=True` in the SQLAlchemy model for pagination sorting fields and create a corresponding index via schema SQL (`CREATE INDEX ON table(column DESC)`).
