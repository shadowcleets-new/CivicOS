## 2024-05-24 - Accessibility and Visual Feedback on Actions
**Learning:** Icon-only buttons or custom interactive widgets (like a `GestureDetector` over a colored container) lack essential accessibility cues out of the box. Users of screen readers need text descriptions (via `tooltip` for icons or `Semantics` wrappers for custom shapes) to know what a button does. Similarly, sighted users benefit immensely from visual feedback upon interaction (like the Material ripple effect from `InkWell`) which `GestureDetector` alone doesn't provide.
**Action:** When creating custom interactive buttons, default to using `Material` and `InkWell` to get built-in tap feedback, and always wrap custom buttons in `Semantics(button: true, label: ...)` or use `tooltip` properties on built-in icon buttons so assistive technologies can read them.

## 2024-05-24 - Transparent Feedback on Unimplemented Features
**Learning:** Users experience confusion and frustration when tapping a button results in a silent failure or no response. Explicitly communicating that a feature is in development prevents users from thinking the app is broken.
**Action:** When adding UX feedback to stubbed buttons or unimplemented features, provide explicit, transparent messaging like a 'Feature coming soon' SnackBar instead of silent failures or fake success messages.
