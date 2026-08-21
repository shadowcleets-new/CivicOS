## 2024-05-24 - Accessibility and Visual Feedback on Actions
**Learning:** Icon-only buttons or custom interactive widgets (like a `GestureDetector` over a colored container) lack essential accessibility cues out of the box. Users of screen readers need text descriptions (via `tooltip` for icons or `Semantics` wrappers for custom shapes) to know what a button does. Similarly, sighted users benefit immensely from visual feedback upon interaction (like the Material ripple effect from `InkWell`) which `GestureDetector` alone doesn't provide.
**Action:** When creating custom interactive buttons, default to using `Material` and `InkWell` to get built-in tap feedback, and always wrap custom buttons in `Semantics(button: true, label: ...)` or use `tooltip` properties on built-in icon buttons so assistive technologies can read them.

## 2025-01-16 - Transparent Feedback for Stubbed Features
**Learning:** Silent failures on stubbed or unimplemented features (like empty `onPressed` callbacks) confuse users and make them think the app is broken.
**Action:** Always provide explicit, transparent messaging like a 'Feature coming soon' SnackBar or Toast for stubbed buttons or unimplemented features instead of silent failures.
