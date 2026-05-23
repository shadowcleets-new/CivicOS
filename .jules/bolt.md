# Bolt's Journal

## 2024-05-18 - Atomic Updates for Counters
**Learning:** In a high-concurrency app like CivicOS, incrementing counters (like upvotes) by first reading the row, modifying it in Python, and saving it back leads to both race conditions (lost updates) and unnecessary database round-trips (SELECT then UPDATE).
**Action:** Use database-level atomic increments `UPDATE ... SET upvotes = upvotes + 1 RETURNING upvotes;` via SQLAlchemy's `update().values().returning()` to halve database queries and make operations thread-safe.
