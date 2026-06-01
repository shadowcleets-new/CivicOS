## 2024-05-24 - Accessibility and Visual Feedback on Actions
**Learning:** Icon-only buttons or custom interactive widgets (like a `GestureDetector` over a colored container) lack essential accessibility cues out of the box. Users of screen readers need text descriptions (via `tooltip` for icons or `Semantics` wrappers for custom shapes) to know what a button does. Similarly, sighted users benefit immensely from visual feedback upon interaction (like the Material ripple effect from `InkWell`) which `GestureDetector` alone doesn't provide.
**Action:** When creating custom interactive buttons, default to using `Material` and `InkWell` to get built-in tap feedback, and always wrap custom buttons in `Semantics(button: true, label: ...)` or use `tooltip` properties on built-in icon buttons so assistive technologies can read them.

## 2024-11-09 - Feedback on Stubbed Buttons
**Learning:** Unimplemented features with empty `onPressed` callbacks or stubbed buttons cause users to click and see no response, feeling that the app is broken or silently ignoring them. Showing a "Feature coming soon" or transparent status is necessary for good UX while stubbing features.
**Action:** Always add simple SnackBar or Toast messaging that transparently states "Feature coming soon" to interactive elements that lack an actual implementation.
