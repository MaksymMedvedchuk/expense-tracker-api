---
paths:
  - "src/test/**"
---
# Тести
- Структура given-when-then, AssertJ замість assertEquals
- Іменування тестів: `methodName_shouldExpectedBehavior_whenCondition` — гібридний snake_case: частини camelCase, розділені `_`; `_whenCondition` можна опустити, якщо умови немає (див. README → Testing)
- Допоміжні методи в тестах — звичайний camelCase без `_`
- Секції given / when / then розділяються порожніми рядками, без коментарів; кілька `assertThat` поспіль — лише для однієї поведінки
- Тестуй поведінку, а не реалізацію; verify лише для побічних ефектів, які і є контрактом