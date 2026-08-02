## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-08-02 - Optimize Keyset Pagination with Composite Indexes
**Learning:** In endpoints utilizing keyset pagination sorting by `created_at DESC, id DESC`, failing to add a matching composite index forces the database into an expensive O(N log N) sort operation for every page load. Also, fetching the full ORM model to evaluate a single cursor field adds unnecessary I/O overhead.
**Action:** Always create a composite index that strictly matches the `ORDER BY` clause of keyset pagination, and project only the specifically required columns when querying for the cursor's state.
