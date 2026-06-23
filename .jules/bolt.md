## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.
## 2026-06-23 - Index on keyset pagination columns
**Learning:** When using keyset/cursor pagination based on frequently sorted timestamp columns like `created_at`, omitting an index leads to O(N log N) full table sorts.
**Action:** Always add an index to `created_at` (or similar sorting columns) in both SQLAlchemy models and raw SQL schemas to prevent performance bottlenecks on public feeds.
