## 2024-05-24 - Accessibility and Visual Feedback on Actions
**Learning:** Icon-only buttons or custom interactive widgets (like a `GestureDetector` over a colored container) lack essential accessibility cues out of the box. Users of screen readers need text descriptions (via `tooltip` for icons or `Semantics` wrappers for custom shapes) to know what a button does. Similarly, sighted users benefit immensely from visual feedback upon interaction (like the Material ripple effect from `InkWell`) which `GestureDetector` alone doesn't provide.
**Action:** When creating custom interactive buttons, default to using `Material` and `InkWell` to get built-in tap feedback, and always wrap custom buttons in `Semantics(button: true, label: ...)` or use `tooltip` properties on built-in icon buttons so assistive technologies can read them.

## 2024-06-03 - Transparent Feedback for Stubbed Features
**Learning:** Users can become confused or frustrated when interactive elements (like buttons) fail silently because the underlying feature isn't implemented yet. It's unclear if the app is broken, loading, or ignoring their input.
**Action:** Always provide explicit, transparent messaging (like a "Feature coming soon" SnackBar) on stubbed buttons to set correct expectations and reassure the user that the interface registered their action.
