## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-23 - SQLAlchemy Pagination Indexing
**Learning:** The read_grievances endpoint uses keyset pagination on the created_at column. Without an index, this forces expensive full table scans and sorts as the table grows.
**Action:** Always add an index to timestamp columns used for default ordering or keyset pagination to maintain O(1) sort performance.
