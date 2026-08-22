## 2024-08-04 - Explicit Feedback for Unimplemented Features
**Learning:** Silent failures on stubbed buttons (like empty `onPressed` callbacks) lead to user confusion as they might think the app is frozen or the button is broken.
**Action:** Always provide explicit, transparent messaging like a "Feature coming soon" SnackBar or Toast for unimplemented features instead of silent failures.
