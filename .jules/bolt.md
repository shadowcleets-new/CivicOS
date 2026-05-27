## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-27 - Indexing missing on keyset pagination endpoints
**Learning:** The `read_grievances` endpoint used keyset pagination via `.order_by(Grievance.created_at.desc(), Grievance.id.desc())` but lacked an index on `created_at`. In SQLAlchemy models, indexing frequently sorted columns is crucial to avoid O(N log N) full table sorts, particularly for public feeds.
**Action:** Add `index=True` to the column definition in the SQLAlchemy model (e.g. `created_at = Column(..., index=True)`) and ensure it's applied before production load scales.
