## 2024-05-24 - Accessibility and Visual Feedback on Actions
**Learning:** Icon-only buttons or custom interactive widgets (like a `GestureDetector` over a colored container) lack essential accessibility cues out of the box. Users of screen readers need text descriptions (via `tooltip` for icons or `Semantics` wrappers for custom shapes) to know what a button does. Similarly, sighted users benefit immensely from visual feedback upon interaction (like the Material ripple effect from `InkWell`) which `GestureDetector` alone doesn't provide.
**Action:** When creating custom interactive buttons, default to using `Material` and `InkWell` to get built-in tap feedback, and always wrap custom buttons in `Semantics(button: true, label: ...)` or use `tooltip` properties on built-in icon buttons so assistive technologies can read them.

## 2024-05-27 - Preventing Silent Failures on Stubbed Features
**Learning:** Having an interactive element (like an `IconButton`) that visually responds to a click but performs no action (e.g., an empty `onPressed: () {}` callback) creates severe user confusion. Users might think the app is broken, freezing, or lagging.
**Action:** When a feature is mocked or "coming soon," always provide explicit, transparent messaging to the user (e.g., a `SnackBar` saying "Feature coming soon") instead of leaving the callback empty.
