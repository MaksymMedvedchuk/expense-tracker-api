---
paths:
  - "src/test/**"
---
# Тести
- Структура given-when-then, AssertJ замість assertEquals
- Іменування тестів: `methodNameShouldExpectedBehaviorWhenCondition`, camelCase без підкреслень (див. README → Testing)
- Секції given / when / then розділяються порожніми рядками, без коментарів; кілька `assertThat` поспіль — лише для однієї поведінки
- Тестуй поведінку, а не реалізацію; verify лише для побічних ефектів, які і є контрактом