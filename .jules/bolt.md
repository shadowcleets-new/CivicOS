## 2024-05-23 - Setup testing for recommend_schemes endpoint\n**Learning:** Missing test client setup causes early blockers. Adding `conftest.py` with the TestClient fixture sets up the testing environment properly. \n**Action:** Always create test setup files and ensure basic dependency packages like pytest, pydantic-settings, and psycopg2-binary are installed.

## 2024-05-24 - Handle identical test data timestamps when paginating via created_at
**Learning:** When tests bulk insert data, rows receive the exact same  timestamp. If testing keyset pagination that relies on sorting by , array-index assertions (e.g. ) will become flaky or fail because identical timestamps cause the database to sort purely by UUIDs, which are non-deterministic.
**Action:** Use set-based validation for array result content and check specific constraints instead of strict order for pagination tests containing simultaneous row creation.

## 2024-05-24 - Handle identical test data timestamps when paginating via created_at
**Learning:** When tests bulk insert data, rows receive the exact same `created_at` timestamp. If testing keyset pagination that relies on sorting by `(created_at.desc(), id.desc())`, array-index assertions (e.g., `assert data[0]['title'] == 'Expected'`) will become flaky or fail because identical timestamps cause the database to sort purely by UUIDs, which are non-deterministic.
**Action:** Use set-based validation for array result content and check specific constraints instead of strict order for pagination tests containing simultaneous row creation.
