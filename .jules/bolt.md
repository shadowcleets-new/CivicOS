## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-28 - Backend Database Index Optimization
**Learning:** The `read_grievances` endpoint relies on keyset pagination, querying the database and sorting by `created_at`. Without an index on `created_at`, this caused a full table scan and in-memory sorting, creating O(N log N) database performance issues for a frequently called endpoint.
**Action:** When inspecting backend endpoints that paginate or filter by specific date/time columns, always verify the database model includes an index (e.g. `index=True` in SQLAlchemy) on those columns to optimize database queries to O(log N).
