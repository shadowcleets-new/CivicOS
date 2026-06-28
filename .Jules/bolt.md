## 2024-06-28 - Add index for keyset pagination fields
**Learning:** For endpoints using keyset pagination, database queries frequently sort by `created_at` descending. Without an index, this results in O(N log N) full table sorts, particularly impacting performance on public feeds.
**Action:** Always ensure frequently sorted columns used in keyset pagination have an index in both SQLAlchemy models (`index=True`) and database schema files (`CREATE INDEX`).
