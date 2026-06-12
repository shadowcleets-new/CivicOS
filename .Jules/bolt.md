## 2026-06-12 - Keyset Pagination Test Flakiness
**Learning:** SQLite creates exact same timestamps for bulk inserts, breaking strict pagination tests relying on `created_at` ordering.
**Action:** Use set-based assertions (`issubset`, `isdisjoint`) instead of index-based assertions when testing keyset pagination on mocked DBs.
