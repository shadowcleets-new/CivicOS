## 2024-05-24 - Accessibility and Visual Feedback on Actions
**Learning:** Icon-only buttons or custom interactive widgets (like a `GestureDetector` over a colored container) lack essential accessibility cues out of the box. Users of screen readers need text descriptions (via `tooltip` for icons or `Semantics` wrappers for custom shapes) to know what a button does. Similarly, sighted users benefit immensely from visual feedback upon interaction (like the Material ripple effect from `InkWell`) which `GestureDetector` alone doesn't provide.
**Action:** When creating custom interactive buttons, default to using `Material` and `InkWell` to get built-in tap feedback, and always wrap custom buttons in `Semantics(button: true, label: ...)` or use `tooltip` properties on built-in icon buttons so assistive technologies can read them.

## 2024-08-06 - Provide Explicit Feedback for Unimplemented Actions
**Learning:** Stubbed buttons or unimplemented features with empty callbacks (like `onPressed: () {}`) create confusion, as users might think the app is unresponsive or broken. Providing an explicit message via a SnackBar confirms the interaction and manages expectations transparently.
**Action:** When adding or encountering stubbed UI elements, always implement a visual feedback mechanism (e.g., 'Feature coming soon' SnackBar) instead of relying on silent failures.
