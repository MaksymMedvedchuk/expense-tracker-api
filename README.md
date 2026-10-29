# Expense Tracker API

REST API for tracking personal expenses.

## Git Flow

- `main` — stable branch
- `feature/...` — new functionality
- `fix/...` — bug fixes
- `refactor/...` — refactoring

Direct pushes to `main` forbidden

## Commits

Use conventional commits:

- `feat:` — new functionality
- `fix:` — bug fix
- `refactor:` — refactoring
- `test:` — tests
- `chore:` — maintenance
- `docs:` — documentation

## Pull Requests

- Every change requires a PR
- PR requires review
- CI must be green
- PR should be ≤ 300 lines

## Code Style

Code style is checked by Checkstyle as part of `./gradlew build` — any violation fails the build locally and in CI.

- Run checks only: `./gradlew checkstyleMain checkstyleTest`
- Config: `config/checkstyle/checkstyle.xml` (based on Google Java Style, 4-space indent)
- Report: `build/reports/checkstyle/` (also uploaded as a CI artifact when the build fails)
- Rules are tightened gradually, each change in a separate PR

## Testing

Tests use JUnit 5 and AssertJ (no `assertEquals` / Hamcrest). They run with `./gradlew test` locally and as part of `./gradlew build` in CI.

- **Naming:** `methodNameShouldExpectedBehaviorWhenCondition`, camelCase without underscores, e.g.
  `constructorShouldThrowExceptionWhenAmountIsNull`, `equalsShouldIgnoreScaleWhenAmountsAreNumericallyEqual`
- **Structure:** given / when / then, separated by blank lines, without `// given` comments.
  For exceptions use `assertThatThrownBy`, which combines when and then.
- **One logical assert block:** several `assertThat` in a row are fine if they verify one behavior.
- Test behavior, not implementation.
