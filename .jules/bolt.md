## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-27 - Database Indexes for Sorting
**Learning:** Ordering by a non-indexed column like `created_at` in heavily queried endpoints (e.g., `read_grievances`) causes significant database overhead and slower response times as the dataset grows, because it forces full table scans and in-memory sorting.
**Action:** Identify endpoints that perform sorting or filtering on large datasets and add `index=True` to the relevant SQLAlchemy model columns.
