---
paths:
  - "src/main/java/**"
---
# Код
- Доменні класи: імена з предметної області; без data, info, manager, util
  (технічні назви на кшталт InfoController для /info допустимі)
- Методи — дієслова з бізнес-змістом (changeAmount)
- Value objects — records; для сутностей спосіб equals/hashCode — свідоме рішення, обґрунтоване в PR
- Гроші — BigDecimal + валюта, ніколи double (value object Money — з TASK-006)
- Доменні класи без імпортів org.springframework і jakarta.persistence
- Не повертай змінювані колекції назовні (List.copyOf)
- Constructor injection для Spring-компонентів.