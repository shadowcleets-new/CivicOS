## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-06-11 - Missing Index on Keyset Pagination Column
**Learning:** The backend uses `created_at.desc()` for keyset pagination in the `read_grievances` endpoint, but the `created_at` column in the `grievances` table lacked an index, which would lead to an O(N log N) full table sort and severe performance degradation on large datasets.
**Action:** Always ensure that columns used for keyset pagination (and frequent sorting) are indexed both in the SQLAlchemy model (`index=True`) and the raw SQL schema.
