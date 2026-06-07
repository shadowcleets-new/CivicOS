## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-07 - Index frequently sorted columns for keyset pagination
**Learning:** The grievances API endpoint uses cursor-based (keyset) pagination which sorts by `created_at DESC`. Without an index on `created_at`, the database must perform an O(N log N) full table sort for every page request, which is a significant performance bottleneck for public feeds.
**Action:** Always add `index=True` to the SQLAlchemy model and the corresponding `CREATE INDEX` statement in raw SQL schema files for columns used in `.order_by()` clauses for pagination.
