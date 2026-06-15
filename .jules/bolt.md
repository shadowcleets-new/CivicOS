## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-15 - Missing DB Index on Keyset Pagination
**Learning:** The public grievances feed uses keyset/cursor pagination sorting by `created_at DESC`. Without an index on `created_at`, the database performs an O(N log N) full table sort for every page request, which is a major bottleneck as the table grows.
**Action:** Always verify that columns used in `.order_by()` for pagination (like `created_at`) have explicit database indexes both in SQLAlchemy models and raw SQL schema definitions.
