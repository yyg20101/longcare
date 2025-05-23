# Code Guidelines

## Internationalization (I18n)

All user-visible text **must** be defined as string resources in `strings.xml` files. Avoid hardcoding strings directly in Kotlin or Composable functions. This is crucial for supporting multiple languages and ensuring a consistent user experience.

## Accessibility (A11y)

All interactive UI elements (e.g., Buttons, ImageButtons, clickable Icons) **must** have a `contentDescription` attribute set in XML layouts or via the `contentDescription` parameter in Jetpack Compose. This description should be localized using string resources.

For informative images (ImageViews that convey information, not purely decorative), provide a meaningful `contentDescription`. For purely decorative images, set `android:importantForAccessibility="no"` in XML or use `Modifier.clearAndSetSemantics {}` in Compose if they should be ignored by accessibility services.

In Jetpack Compose, actively use the `contentDescription` parameter for Composables like `Icon` and `Image`.

### Using `Modifier.semantics` in Jetpack Compose

For complex custom Composables or when default accessibility information is insufficient, use `Modifier.semantics` to provide richer semantic information. This can include custom descriptions, state descriptions, actions, and more. For example, you can define that a custom element is a 'button' or provide specific state information like 'checked' or 'disabled'.

Example: `Modifier.semantics { contentDescription = "Play button"; role = Role.Button }`

### Focus Management

Ensure logical focus order for users navigating with a keyboard (Tab key) or accessibility services. In XML, `android:nextFocusForward`, `android:nextFocusUp`, etc., can be used if the default order is not optimal.

In Jetpack Compose, the focus order is generally determined by the declaration order. Use `FocusRequester` and `Modifier.focusRequester()` for programmatic focus control and `Modifier.focusOrder()` for customizing the traversal order if needed.

Test focus navigation thoroughly using a physical keyboard or emulator keyboard tabbing and with TalkBack enabled.
