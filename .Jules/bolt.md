## 2024-10-24 - Keyset Pagination Indexes
**Learning:** Missing indexes on keyset pagination sort columns result in O(N log N) full table sorts.
**Action:** Ensure columns used in .order_by() have index=True and explicit CREATE INDEX statements.
