## 2024-05-24 - Accessibility and Visual Feedback on Actions
**Learning:** Icon-only buttons or custom interactive widgets (like a `GestureDetector` over a colored container) lack essential accessibility cues out of the box. Users of screen readers need text descriptions (via `tooltip` for icons or `Semantics` wrappers for custom shapes) to know what a button does. Similarly, sighted users benefit immensely from visual feedback upon interaction (like the Material ripple effect from `InkWell`) which `GestureDetector` alone doesn't provide.
**Action:** When creating custom interactive buttons, default to using `Material` and `InkWell` to get built-in tap feedback, and always wrap custom buttons in `Semantics(button: true, label: ...)` or use `tooltip` properties on built-in icon buttons so assistive technologies can read them.

## 2024-06-17 - Transparent Messaging for Stubbed Features
**Learning:** When a button or feature is unimplemented or mocked, failing silently or providing fake success messages leads to user confusion and deceptive UI states.
**Action:** Always provide transparent, immediate UX feedback (like a SnackBar saying "Feature coming soon") for empty callbacks so users know their interaction was registered but the feature isn't ready.
