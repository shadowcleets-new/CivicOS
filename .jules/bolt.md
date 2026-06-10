## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-10-27 - Indexing frequently sorted columns
**Learning:** The `created_at` column in the `Grievance` model was used heavily for keyset pagination sorting (`order_by(Grievance.created_at.desc())`) but lacked a database index, causing O(N log N) full table sorts.
**Action:** Always ensure frequently sorted columns used in pagination have database indices (`index=True` in SQLAlchemy and `CREATE INDEX` in raw SQL schema).
