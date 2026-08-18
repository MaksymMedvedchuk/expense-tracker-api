# Tasks: PDP Strong Junior → Java Backend Developer (Expense Tracker API)

Source file: `/home/user/pdp-junior-java/pdp_junior_java_backend.md`
Generated from the source document.

## 1. Summary

Реалізувати 12-тижневий план професійного розвитку strong junior Java backend розробника через pet-проєкт **Expense Tracker API** (REST API обліку витрат: категорії, статистика, конвертація валют через зовнішній API, звіти, кешування).

- Business goal: за 12 тижнів (5–7 год/тиж) поставити інженерну культуру розробника — тести як норму, чисте найменування, свідомі патерни проєктування, ітеративну розробку через git-бренчування і PR-flow з code review ментора.
- Technical goal: робочий Spring Boot 3 / Java 21 застосунок з PostgreSQL, Flyway, покриттям сервісного шару ≥ 70% (JaCoCo), інтеграційними тестами на Testcontainers, ≥ 6 застосованими патернами і CI (тести + Checkstyle) на кожен PR.
- Expected result: pet-проєкт end-to-end, ≥ 20 змерджених PR, пройдені 4 чек-поінти, самостійна capstone-фіча і ретро-документ з напрямом подальшого розвитку.

## 2. Confirmed requirements

- Період — 12 тижнів, бюджет 5–7 годин на тиждень (~72 години).
- Ментор робить code review усіх pull request-ів; sync 30 хв раз на 2 тижні.
- Кожна фіча — в окремій feature-гілці, мердж лише через PR після approve ментора і зеленого CI; жодних пушів напряму в `main`.
- PR ≤ 300 рядків; conventional commits; PR-шаблон (що зроблено, чому, як перевірити); self-review перед відправкою.
- ≥ 20 змерджених PR за період PDP.
- Тести: JUnit 5 + AssertJ + Mockito (unit), MockMvc + Testcontainers + WireMock (integration); покриття сервісного/доменного шару ≥ 70% за JaCoCo; ≥ 5 інтеграційних тестів на реальній PostgreSQL; PR з логікою без тестів не мерджиться.
- Хоча б один сервіс написаний через TDD (історія комітів red → green → refactor).
- Найменування: Checkstyle без порушень у CI; DTO ≠ Entity; пакети за фічами; рефакторинг-вправа тижня 2.
- Патерни: ≥ 6 (Builder, Factory Method, Strategy, Decorator, Adapter, Observer/події + Repository, Cache-Aside); кожен — окремий PR з обґрунтуванням «проблема → патерн → альтернативи»; ментор має право відхилити PR з overengineering.
- Стек: Java 21 (LTS), Spring Boot 3.x, Gradle (Kotlin DSL), PostgreSQL + Spring Data JPA, Flyway, JaCoCo, Checkstyle, GitHub Actions.
- Pet-проєкт: Expense Tracker API (або аналог зі структурою CRUD + бізнес-логіка + зовнішній API + БД).
- Чек-поінти на тижнях 3, 7, 10, 12 з критеріями «не пройшов → не біжимо далі».
- Capstone-фіча самостійно end-to-end (дизайн → задачі → гілки → ≥ 3 PR → тести → мердж) + ретро-документ.
- Поза scope: мікросервіси, Kafka, Kubernetes, WebFlux, перформанс-тюнінг, advanced Spring Security, frontend, NoSQL, реальні продакшен-проєкти.

## 3. Assumptions

- [Припущення] Публічний API курсів валют — будь-який безкоштовний (наприклад, exchangerate-api, frankfurter.app або НБУ API); фінальний вибір за розробником з погодженням у PR.
- [Припущення] Локальна PostgreSQL піднімається через Docker Compose.
- [Припущення] Хостинг репозиторію — GitHub (згадано GitHub Actions).
- [Припущення] Auth (Spring Security + JWT) — лише опція для capstone, не обов'язкова частина основного плану.
- [Припущення] Capstone-фіча обирається розробником на тижні 11 (приклади: спільні бюджети, регулярні витрати) і затверджується ментором.

## 4. Open questions

- Яку базову валюту застосунку взяти за замовчуванням (UAH чи налаштовувана per-user)?
- Чи потрібен деплой pet-проєкту кудись (навіть локальний Docker-образ), чи достатньо запуску локально?
- Чи входить CSV-експорт звітів в основний scope Strategy-патерну, чи це P2-розширення?

## 5. Story roadmap

### STORY-001: Середовище, репозиторій і git-flow

Goal:
Готове середовище розробки і робочий PR-flow з CI.

Business value:
Фундамент ітеративної розробки — головної звички, яку ставить PDP.

Scope:
- Репозиторій із захистом `main`, PR-шаблон, conventional commits
- Скелет Spring Boot (Java 21, Gradle Kotlin DSL)
- CI на GitHub Actions

Out of scope:
- Деплой, Docker-образи продакшен-рівня

Sub-tasks:
- TASK-001
- TASK-002
- TASK-003
- TASK-004

Dependencies:
- None

Exit criteria:
- Перший PR змерджено через feature-гілку, CI зелений (Milestone тижня 1)

### STORY-002: Доменна модель і чистий код

Goal:
Доменна модель з чистими іменами і автоматичний контроль стилю.

Business value:
Найменування — 80% читабельності коду і головний предмет code review.

Scope:
- Checkstyle у CI
- Доменні класи Expense, Category, User
- Рефакторинг-вправа «поганий код → читабельний»

Out of scope:
- Persistence (БД підключається у STORY-005)

Sub-tasks:
- TASK-005
- TASK-006
- TASK-007

Dependencies:
- STORY-001

Exit criteria:
- Checkstyle у CI зелений (Milestone тижня 2)

### STORY-003: Unit-тести і TDD

Goal:
Unit-тести як норма: JUnit 5, AssertJ, Mockito, TDD-практика, вимірюване покриття.

Business value:
Головний акцент PDP — тести як невід'ємна частина коду.

Scope:
- Тестова інфраструктура і конвенції given-when-then
- Сервіс статистики через TDD
- Mockito, параметризовані тести, edge cases
- JaCoCo з порогом 70%

Out of scope:
- Інтеграційні тести (STORY-006)

Sub-tasks:
- TASK-008
- TASK-009
- TASK-010
- TASK-011
- TASK-012

Dependencies:
- STORY-002

Exit criteria:
- Checkpoint #1 (тиждень 3) пройдено; покриття сервісного шару росте до ≥ 70%

### STORY-004: REST API

Goal:
CRUD-ендпоінти витрат і категорій з валідацією, обробкою помилок і тестами MockMvc.

Business value:
Перша повноцінна користувацька функціональність pet-проєкту.

Scope:
- Контролери, DTO ↔ Entity, валідація, глобальна обробка помилок
- Тести MockMvc

Out of scope:
- Auth, зовнішні API

Sub-tasks:
- TASK-013
- TASK-014
- TASK-015
- TASK-016

Dependencies:
- STORY-003

Exit criteria:
- API v1 працює (Milestone тижня 5), всі ендпоінти покриті тестами

### STORY-005: Persistence

Goal:
Реальна PostgreSQL зі Spring Data JPA і Flyway-міграціями.

Business value:
Стандартна продакшен-зв'язка зберігання даних.

Scope:
- Docker Compose з PostgreSQL
- Репозиторії Spring Data JPA
- Flyway-міграції

Out of scope:
- NoSQL, тюнінг запитів

Sub-tasks:
- TASK-017
- TASK-018
- TASK-019

Dependencies:
- STORY-004

Exit criteria:
- Дані переживають рестарт застосунку; схема версіонується Flyway

### STORY-006: Інтеграційні тести

Goal:
Інтеграційні тести на реальній БД через Testcontainers.

Business value:
Довіра до системи цілком, а не лише до окремих класів.

Scope:
- Інфраструктура Testcontainers
- Тести репозиторіїв і наскрізні API-тести

Out of scope:
- Performance-тести

Sub-tasks:
- TASK-020
- TASK-021
- TASK-022

Dependencies:
- STORY-005

Exit criteria:
- Checkpoint #2 (тиждень 7) пройдено; ≥ 5 інтеграційних тестів на реальній PostgreSQL

### STORY-007: Зовнішній API курсів валют

Goal:
Конвертація витрат у базову валюту через публічний API з обробкою збоїв і WireMock-тестами.

Business value:
Типовий продакшен-кейс — інтеграція з ненадійним зовнішнім сервісом.

Scope:
- HTTP-клієнт курсів валют
- Конвертація витрат
- WireMock-тести, обробка таймаутів і помилок

Out of scope:
- Кешування (STORY-009)

Sub-tasks:
- TASK-023
- TASK-024
- TASK-025

Dependencies:
- STORY-006

Exit criteria:
- Ціль 2 (integration) досягнута (кінець тижня 8)

### STORY-008: Патерни I — Builder, Factory Method, Strategy

Goal:
Перші три патерни, застосовані до реальних проблем проєкту.

Business value:
Патерни — спільна мова з командою; застосовуються «після болю», а не з теорії.

Scope:
- Strategy для звітів
- Factory Method для провайдерів курсів
- Builder для складних об'єктів

Out of scope:
- Патерни заради патернів (ментор відхиляє overengineering)

Sub-tasks:
- TASK-026
- TASK-027
- TASK-028

Dependencies:
- STORY-007

Exit criteria:
- Кожен патерн — окремий PR з описом «проблема → патерн → альтернативи»

### STORY-009: Патерни II — Decorator, Adapter, події, Cache-Aside

Goal:
Ще три-чотири патерни + кешування курсів валют.

Business value:
Закриває Ціль 4 (≥ 6 патернів) і додає продакшен-патерн кешування.

Scope:
- Cache-Aside для курсів
- Decorator (логування/метрики), Adapter (другий провайдер), події (Observer)

Out of scope:
- Розподілений кеш (Redis)

Sub-tasks:
- TASK-029
- TASK-030
- TASK-031
- TASK-032

Dependencies:
- STORY-008

Exit criteria:
- Checkpoint #3 (тиждень 10) пройдено

### STORY-010: Capstone + ретроспектива

Goal:
Самостійна фіча end-to-end і ретроспектива 12 тижнів.

Business value:
Закриває цикл навчання самостійним кейсом, максимально наближеним до продакшену.

Scope:
- Дизайн-документ, розбиття на задачі
- Реалізація ≥ 3 послідовними PR з тестами
- Ретро-документ і вибір напряму

Out of scope:
- Нові технології поза стеком PDP

Sub-tasks:
- TASK-033
- TASK-034
- TASK-035
- TASK-036

Dependencies:
- STORY-009

Exit criteria:
- Checkpoint #4 (тиждень 12) пройдено: всі обов'язкові пункти закриті

## 6. Stories with sub-tasks

### STORY-001: Середовище, репозиторій і git-flow

Priority: P0
Status: Todo

Goal:
Готове середовище розробки і робочий PR-flow з CI.

Business value:
Фундамент ітеративної розробки через бренчування — головної звички PDP.

Scope:
- Репозиторій, захист `main`, PR-шаблон, conventional commits
- Скелет Spring Boot, health-endpoint
- CI GitHub Actions

Out of scope:
- Деплой

Dependencies:
- None

Acceptance criteria:
- Пуш напряму в `main` заборонений налаштуваннями репозиторію
- Перший PR пройшов цикл: гілка → PR → review ментора → merge
- CI запускає збірку і тести на кожен PR

Sub-tasks:

#### TASK-001: Створити репозиторій з захистом main і PR-шаблоном

Parent: STORY-001
Type: Sub-task
Category: DevOps
Priority: P0
Estimate: S
Status: Todo

Description:
Створити GitHub-репозиторій `expense-tracker-api`. Увімкнути branch protection для `main` (заборона прямих пушів, обов'язковий PR + 1 approve + зелений CI). Додати `.github/PULL_REQUEST_TEMPLATE.md` з розділами: що зроблено, чому так, як перевірити, чого свідомо не зроблено. Додати `README.md` з коротким описом проєкту і правилами git-flow (гілки `feature/…`, `fix/…`, `refactor/…`; conventional commits; PR ≤ 300 рядків).

Source requirement:
Розділ 6 «Git-flow і правила ітерацій» PDP.

Acceptance criteria:
- Прямий пуш у `main` відхиляється
- PR-шаблон автоматично підставляється при створенні PR
- README описує git-flow і conventional commits

Implementation notes:
- Branch protection: Settings → Branches → Add rule для `main`

Dependencies:
- None

Risks:
- None

#### TASK-002: Скелет Spring Boot (Java 21, Gradle Kotlin DSL) + health-endpoint

Parent: STORY-001
Type: Sub-task
Category: Backend
Priority: P0
Estimate: M
Status: Todo

Description:
Згенерувати проєкт через start.spring.io: Java 21, Spring Boot 3.x, Gradle Kotlin DSL, залежності web + actuator. Структурувати пакети за фічами (`expense`, `category`, `user`, `common`), не за шарами. Перевірити, що `GET /actuator/health` повертає `UP`.

Source requirement:
Розділ 5 «Стек pet-проєкту», roadmap тиждень 1.

Acceptance criteria:
- `./gradlew build` проходить локально
- Застосунок стартує, health-endpoint повертає `UP`
- Пакети організовані за фічами

Implementation notes:
- Java Toolchain у Gradle зафіксувати на 21

Dependencies:
- TASK-001

Risks:
- Фрустрація на Gradle — за планом виділити окремі 2 години на основи, коли вперше стане боляче

#### TASK-003: Налаштувати CI на GitHub Actions

Parent: STORY-001
Type: Sub-task
Category: DevOps
Priority: P0
Estimate: M
Status: Todo

Description:
Створити workflow `.github/workflows/ci.yml`: тригер на PR і push у `main`; кроки — checkout, JDK 21, `./gradlew build` (збірка + тести). Зробити CI обов'язковим статус-чеком для мерджу.

Source requirement:
Roadmap тиждень 1; розділ 6 п.5 «мердж лише після зеленого CI».

Acceptance criteria:
- CI запускається на кожен PR
- Червоний CI блокує мердж
- Час прогону < 5 хв

Implementation notes:
- Використати actions/setup-java з кешем Gradle

Dependencies:
- TASK-002

Risks:
- None

#### TASK-004: Перший PR через feature-гілку за conventional commits

Parent: STORY-001
Type: Sub-task
Category: Backend
Priority: P0
Estimate: S
Status: Todo

Description:
Тренувальний повний цикл git-flow: гілка `feature/project-info-endpoint`, невеликий ендпоінт `GET /api/v1/info` (назва застосунку, версія), коміти за conventional commits, self-review власного diff, PR за шаблоном, виправлення зауважень ментора follow-up комітами, merge.

Source requirement:
Roadmap тиждень 1, Milestone «перший PR змерджено»; розділ 6 п.6–7.

Acceptance criteria:
- PR пройшов review ментора і змерджений
- Історія комітів відповідає conventional commits
- Зауваження review виправлені follow-up комітами в тій самій гілці

Implementation notes:
- Мета задачі — процес, а не код; ендпоінт максимально простий

Dependencies:
- TASK-003

Risks:
- None

### STORY-002: Доменна модель і чистий код

Priority: P0
Status: Todo

Goal:
Доменна модель з чистими іменами і автоматичний контроль стилю в CI.

Business value:
Закладає Ціль 3 (найменування) з першого коду проєкту.

Scope:
- Checkstyle у CI
- Доменні класи
- Рефакторинг-вправа

Out of scope:
- Persistence

Dependencies:
- STORY-001

Acceptance criteria:
- Checkstyle зелений у CI (Milestone тижня 2)
- Доменна модель читається без коментарів

Sub-tasks:

#### TASK-005: Підключити Checkstyle до збірки і CI

Parent: STORY-002
Type: Sub-task
Category: DevOps
Priority: P0
Estimate: M
Status: Todo

Description:
Додати Checkstyle-плагін у Gradle з конфігом на основі Google Java Style (адаптувати: naming conventions, заборона скорочень в іменах, довжина методів). Зробити `checkstyleMain`/`checkstyleTest` частиною `build`, щоб порушення валили CI.

Source requirement:
Ціль 3 M-критерій «Checkstyle без порушень у CI»; розділ 5 стеку.

Acceptance criteria:
- Порушення Checkstyle валить збірку локально і в CI
- Конфіг лежить у репозиторії (`config/checkstyle/checkstyle.xml`)

Implementation notes:
- Почати з м'якого набору правил, посилювати поступово через окремі PR

Dependencies:
- TASK-004

Risks:
- Занадто суворий конфіг на старті демотивує — калібрувати з ментором

#### TASK-006: Доменна модель Expense, Category, User

Parent: STORY-002
Type: Sub-task
Category: Backend
Priority: P0
Estimate: M
Status: Todo

Description:
Створити доменні класи: `Expense` (сума як `BigDecimal` + валюта, дата, опис, категорія), `Category` (назва, тип), `User` (мінімально). Використати records де доречно. Імена — доменні, без `data`/`info`/`manager`; методи — дієслова. Без анотацій JPA на цьому етапі (чистий домен).

Source requirement:
Roadmap тиждень 2 «Доменна модель з чистими іменами»; Ціль 3.

Acceptance criteria:
- Модель компілюється, Checkstyle зелений
- Гроші — `BigDecimal` + код валюти, не `double`
- Review ментора щодо найменування пройдено

Implementation notes:
- Розглянути value object `Money` (сума + валюта) — перший крок до Builder у STORY-008

Dependencies:
- TASK-005

Risks:
- None

#### TASK-007: Рефакторинг-вправа «поганий код → читабельний»

Parent: STORY-002
Type: Sub-task
Category: Backend
Priority: P1
Estimate: M
Status: Todo

Description:
Ментор надає клас зі свідомо поганими іменами і структурою (~100 рядків). Розробник рефакторить: перейменування, виділення методів, усунення магічних чисел — окремим PR з поясненням кожного рішення в описі.

Source requirement:
Roadmap тиждень 2 «рефакторинг-вправа»; Ціль 3 M-критерій.

Acceptance criteria:
- Поведінка коду не змінилась
- Кожне перейменування обґрунтоване в PR
- Ментор approve з ≤ 2 зауваженнями щодо імен

Implementation notes:
- Ihor готує вихідний «поганий» клас заздалегідь

Dependencies:
- TASK-006

Risks:
- None

### STORY-003: Unit-тести і TDD

Priority: P0
Status: Todo

Goal:
Unit-тести як норма; один сервіс через повний TDD-цикл; вимірюване покриття.

Business value:
Головний акцент PDP — тести пишуться разом з кодом, а не «потім».

Scope:
- JUnit 5 + AssertJ, конвенції
- TDD-сервіс статистики
- Mockito, параметризовані тести
- JaCoCo ≥ 70%

Out of scope:
- Інтеграційні тести

Dependencies:
- STORY-002

Acceptance criteria:
- Checkpoint #1 (тиждень 3) пройдено
- Історія комітів TDD-сервісу показує red → green → refactor

Sub-tasks:

#### TASK-008: Тестова інфраструктура: JUnit 5 + AssertJ + конвенції

Parent: STORY-003
Type: Sub-task
Category: QA
Priority: P0
Estimate: S
Status: Todo

Description:
Підключити JUnit 5 і AssertJ. Зафіксувати в README конвенції тестів: структура given-when-then, іменування `methodName_shouldExpectedBehavior_whenCondition` (або узгоджений з ментором стиль), один логічний assert-блок на тест. Написати перші 2–3 тести на доменну модель як зразок.

Source requirement:
Ціль 2; roadmap тиждень 3; розділ 5 стеку.

Acceptance criteria:
- Тести запускаються через `./gradlew test` і в CI
- Конвенції зафіксовані в README
- Зразкові тести відповідають конвенціям

Implementation notes:
- AssertJ замість hamcrest/junit-assertions — читабельніші ланцюжки

Dependencies:
- TASK-007

Risks:
- None

#### TASK-009: Сервіс статистики витрат через TDD

Parent: STORY-003
Type: Sub-task
Category: Backend
Priority: P0
Estimate: L
Status: Todo

Description:
Реалізувати `ExpenseStatisticsService` (сума за період, розбивка за категоріями, середнє за день) строго через red-green-refactor: спочатку тест, що падає (окремий коміт `test:`), потім мінімальна реалізація (`feat:`), потім рефакторинг (`refactor:`). Дані — поки що in-memory (без БД).

Source requirement:
Roadmap тиждень 3 «сервіс через TDD»; Checkpoint #2 «історія комітів red → green → refactor».

Acceptance criteria:
- Історія комітів демонструє TDD-цикли
- Покриття сервісу ~100%
- Edge cases: порожній період, одна витрата, різні валюти (поки — виняток)

Implementation notes:
- Це найважливіша задача блоку — не поспішати, 2 тижневі сесії

Dependencies:
- TASK-008

Risks:
- Спокуса написати код раніше тесту — ментор перевіряє порядок комітів

#### TASK-010: Mockito: тести сервісів з моками залежностей

Parent: STORY-003
Type: Sub-task
Category: QA
Priority: P0
Estimate: M
Status: Todo

Description:
Виділити інтерфейс `ExpenseRepository` (in-memory реалізація), переписати тести сервісів з моками Mockito. Показати різницю: mock vs stub vs фейкова реалізація. Тестувати поведінку (результат), а не реалізацію (не перевіряти внутрішні виклики без потреби).

Source requirement:
Ціль 2 S-критерій; roadmap тиждень 4; Checkpoint #2 «тестує поведінку, а не реалізацію».

Acceptance criteria:
- Сервісні тести не залежать від реальної реалізації репозиторію
- `verify` використовується лише там, де побічний ефект і є контрактом
- Розробник пояснює mock/stub/fake своїми словами

Implementation notes:
- Інтерфейс репозиторію тут — підготовка до Spring Data JPA у STORY-005

Dependencies:
- TASK-009

Risks:
- Over-mocking — обговорити на review

#### TASK-011: Параметризовані тести і edge cases валідації

Parent: STORY-003
Type: Sub-task
Category: QA
Priority: P0
Estimate: M
Status: Todo

Description:
Додати бізнес-правила (сума > 0, дата не в майбутньому, назва категорії непорожня і унікальна) і покрити їх `@ParameterizedTest` (`@CsvSource`/`@MethodSource`): межові значення, null, порожні рядки, від'ємні суми.

Source requirement:
Roadmap тиждень 4 «валідація і бізнес-правила з повним набором тестів».

Acceptance criteria:
- Кожне бізнес-правило має параметризований тест з ≥ 4 кейсами
- Негативні сценарії кидають доменні винятки з інформативними повідомленнями

Implementation notes:
- Доменна валідація тут ≠ Bean Validation на DTO (буде в TASK-015) — розрізнити ці рівні

Dependencies:
- TASK-010

Risks:
- None

#### TASK-012: JaCoCo-звіт покриття з порогом 70% у CI

Parent: STORY-003
Type: Sub-task
Category: DevOps
Priority: P0
Estimate: S
Status: Todo

Description:
Підключити JaCoCo, налаштувати `jacocoTestCoverageVerification` з порогом 70% по інструкціях для пакетів сервісного/доменного шару (контролери і конфігурація — виключені). Звіт публікується як артефакт CI.

Source requirement:
Ціль 2 M-критерій «покриття ≥ 70% (JaCoCo)».

Acceptance criteria:
- Падіння покриття нижче 70% валить CI
- HTML-звіт доступний як артефактワークфлоу
- Виключення (config, DTO) зафіксовані в білд-скрипті

Implementation notes:
- Поріг вмикати після TASK-009–011, коли покриття вже реально ≥ 70%

Dependencies:
- TASK-011

Risks:
- Гонитва за відсотком замість якості тестів — обговорювати на review

### STORY-004: REST API

Priority: P0
Status: Todo

Goal:
CRUD-ендпоінти витрат і категорій з валідацією, обробкою помилок і тестами MockMvc.

Business value:
Перша повноцінна користувацька функціональність.

Scope:
- Контролери, DTO ↔ Entity, валідація, ProblemDetail
- MockMvc-тести

Out of scope:
- Auth

Dependencies:
- STORY-003

Acceptance criteria:
- API v1 працює (Milestone тижня 5)
- Всі ендпоінти покриті MockMvc-тестами

Sub-tasks:

#### TASK-013: CRUD-ендпоінти категорій

Parent: STORY-004
Type: Sub-task
Category: Backend
Priority: P0
Estimate: M
Status: Todo

Description:
`GET/POST/PUT/DELETE /api/v1/categories` з окремими DTO (`CategoryRequest`, `CategoryResponse`) і мапінгом на домен. Правильні статуси: 201 + Location на створення, 404 на відсутній id, 409 на дублікат назви.

Source requirement:
Roadmap тиждень 5 «CRUD-ендпоінти витрат і категорій».

Acceptance criteria:
- Всі операції працюють через curl/HTTP-клієнт
- DTO не протікають у домен і навпаки
- Статуси відповідають семантиці HTTP

Implementation notes:
- Мапінг вручну (без MapStruct) — щоб бачити ціну і сенс розділення DTO/домену

Dependencies:
- TASK-012

Risks:
- None

#### TASK-014: CRUD-ендпоінти витрат + статистика

Parent: STORY-004
Type: Sub-task
Category: Backend
Priority: P0
Estimate: L
Status: Todo

Description:
`/api/v1/expenses` (CRUD + фільтри за періодом і категорією) та `GET /api/v1/expenses/statistics?from=&to=` поверх `ExpenseStatisticsService` з TASK-009. Пагінація списку витрат.

Source requirement:
Roadmap тиждень 5; опис pet-проєкту (розділ 5).

Acceptance criteria:
- Фільтри і пагінація працюють
- Статистика повертає розбивку за категоріями за період
- Сервісний шар не залежить від web-шару

Implementation notes:
- Розбити на 2 PR: CRUD і статистика — тримати PR ≤ 300 рядків

Dependencies:
- TASK-013

Risks:
- PR розростається — різати заздалегідь

#### TASK-015: Валідація запитів і глобальна обробка помилок

Parent: STORY-004
Type: Sub-task
Category: Backend
Priority: P0
Estimate: M
Status: Todo

Description:
Bean Validation на DTO (`@NotNull`, `@Positive`, `@Size`…) + `@RestControllerAdvice` з RFC 7807 `ProblemDetail`: єдиний формат помилок для валідації, 404, 409, доменних винятків. Без стектрейсів у відповідях.

Source requirement:
Roadmap тиждень 5 «валідація, обробка помилок».

Acceptance criteria:
- Невалідний запит → 400 зі списком полів і причин
- Доменні винятки мапляться на коректні статуси
- Формат помилок єдиний для всіх ендпоінтів

Implementation notes:
- Розрізнити валідацію DTO (форма) і доменну валідацію (бізнес-правила з TASK-011)

Dependencies:
- TASK-014

Risks:
- None

#### TASK-016: MockMvc-тести контролерів

Parent: STORY-004
Type: Sub-task
Category: QA
Priority: P0
Estimate: M
Status: Todo

Description:
`@WebMvcTest` для обох контролерів з моками сервісів: happy path, валідаційні помилки, 404/409, формат ProblemDetail, серіалізація дат і `BigDecimal`.

Source requirement:
Ціль 2 S-критерій «інтеграційні тести (MockMvc…)»; roadmap тиждень 5.

Acceptance criteria:
- Кожен ендпоінт має тести на успіх і на помилки
- Тести падають при зміні контракту API (статуси, поля JSON)

Implementation notes:
- Це тести web-шару; наскрізні тести з БД будуть у TASK-022

Dependencies:
- TASK-015

Risks:
- None

### STORY-005: Persistence

Priority: P0
Status: Todo

Goal:
Реальна PostgreSQL зі Spring Data JPA і Flyway.

Business value:
Стандартна продакшен-зв'язка зберігання даних.

Scope:
- Docker Compose, JPA-ентіті, репозиторії, Flyway

Out of scope:
- Тюнінг запитів, NoSQL

Dependencies:
- STORY-004

Acceptance criteria:
- Дані переживають рестарт
- Схема створюється лише міграціями (ddl-auto validate)

Sub-tasks:

#### TASK-017: PostgreSQL у Docker Compose + підключення JPA

Parent: STORY-005
Type: Sub-task
Category: DevOps
Priority: P0
Estimate: M
Status: Todo

Description:
`compose.yaml` з PostgreSQL 16; залежності `spring-boot-starter-data-jpa` + драйвер; конфіг датасорсу через змінні оточення; `spring.jpa.hibernate.ddl-auto=validate`.

Source requirement:
Розділ 5 стеку «PostgreSQL + Spring Data JPA»; [Припущення] Docker Compose.

Acceptance criteria:
- `docker compose up` піднімає БД, застосунок конектиться
- Схема ніколи не генерується Hibernate-ом

Implementation notes:
- Можна використати spring-boot-docker-compose для локального старту

Dependencies:
- TASK-016

Risks:
- None

#### TASK-018: Flyway-міграції початкової схеми

Parent: STORY-005
Type: Sub-task
Category: Backend
Priority: P0
Estimate: M
Status: Todo

Description:
Підключити Flyway; `V1__init.sql` зі схемою для users, categories, expenses (FK, індекси за датою і категорією, unique на назву категорії). Конвенція іменування міграцій — у README.

Source requirement:
Розділ 5 стеку «Flyway — версіонування схеми БД як норма».

Acceptance criteria:
- Міграції застосовуються на чисту БД при старті
- Повторний старт не ламає застосунок
- Обмеження БД відповідають доменним правилам

Implementation notes:
- Грошові суми — `numeric(19,2)`; валюта — `char(3)`

Dependencies:
- TASK-017

Risks:
- None

#### TASK-019: Перевести сервіси на Spring Data JPA репозиторії

Parent: STORY-005
Type: Sub-task
Category: Backend
Priority: P0
Estimate: M
Status: Todo

Description:
JPA-ентіті (окремо від доменних records або акуратне суміщення — рішення обґрунтувати в PR), Spring Data інтерфейси замість in-memory реалізацій з TASK-010. Unit-тести сервісів не змінюються (моки лишаються) — це і є перевірка, що абстракція була правильною.

Source requirement:
Roadmap тиждень 6 «збереження в реальну БД; репозиторії».

Acceptance criteria:
- API працює з реальною БД end-to-end
- Unit-тести сервісів пройшли без змін
- Статистика рахується запитом до БД, не в пам'яті

Implementation notes:
- Repository-патерн тут проявляється практично — зафіксувати це в описі PR

Dependencies:
- TASK-018

Risks:
- Витік JPA-ентіті у web-шар — ловити на review

### STORY-006: Інтеграційні тести

Priority: P0
Status: Todo

Goal:
Інтеграційні тести на реальній PostgreSQL через Testcontainers.

Business value:
Довіра до системи цілком; закриває integration-частину Цілі 2.

Scope:
- Інфраструктура Testcontainers, тести репозиторіїв, наскрізні API-тести

Out of scope:
- Performance-тести

Dependencies:
- STORY-005

Acceptance criteria:
- Checkpoint #2 (тиждень 7) пройдено
- ≥ 5 інтеграційних тестів на реальній БД

Sub-tasks:

#### TASK-020: Інфраструктура Testcontainers

Parent: STORY-006
Type: Sub-task
Category: QA
Priority: P0
Estimate: M
Status: Todo

Description:
Підключити Testcontainers (модуль postgresql + junit-jupiter); базовий клас/анотація для інтеграційних тестів з `@ServiceConnection`; контейнер перевикористовується між тестами; Flyway-міграції ганяються в тестах.

Source requirement:
Ціль 2 S-критерій «Testcontainers з PostgreSQL»; розділ 5 стеку.

Acceptance criteria:
- Інтеграційні тести піднімають реальну PostgreSQL і проходять у CI
- Міграції Flyway перевіряються самими тестами

Implementation notes:
- Розділити unit і integration у різні Gradle-таски (`test` / `integrationTest`)

Dependencies:
- TASK-019

Risks:
- Docker у CI — GitHub Actions підтримує з коробки

#### TASK-021: Інтеграційні тести репозиторіїв

Parent: STORY-006
Type: Sub-task
Category: QA
Priority: P0
Estimate: M
Status: Todo

Description:
`@DataJpaTest` + Testcontainers для кастомних запитів: фільтри за періодом/категорією, агрегація статистики, унікальність назви категорії, каскади видалення. Тестові дані — через builder-и/фікстури, не через SQL-дампи.

Source requirement:
Roadmap тиждень 7 «інтеграційні тести репозиторіїв».

Acceptance criteria:
- Кожен кастомний запит покритий тестом на реальній БД
- Порушення unique/FK перевірені

Implementation notes:
- Фікстури тестових даних стануть у пригоді для TASK-028 (Builder)

Dependencies:
- TASK-020

Risks:
- None

#### TASK-022: Наскрізні API-тести

Parent: STORY-006
Type: Sub-task
Category: QA
Priority: P0
Estimate: M
Status: Todo

Description:
`@SpringBootTest(webEnvironment = RANDOM_PORT)` + Testcontainers: 3–4 наскрізні сценарії (створити категорію → додати витрати → отримати статистику → видалити), перевірка реальних HTTP-відповідей і стану БД.

Source requirement:
Checkpoint #2; roadmap тиждень 7 «інтеграційні тести API».

Acceptance criteria:
- Сценарії проходять на реальній БД у CI
- Розробник пояснює піраміду тестів проєкту: unit → web → integration

Implementation notes:
- Не дублювати MockMvc-тести — тут лише ключові наскрізні сценарії

Dependencies:
- TASK-021

Risks:
- None

### STORY-007: Зовнішній API курсів валют

Priority: P0
Status: Todo

Goal:
Конвертація витрат у базову валюту через публічний API з обробкою збоїв.

Business value:
Типовий продакшен-кейс інтеграції з ненадійним зовнішнім сервісом.

Scope:
- HTTP-клієнт, конвертація, WireMock-тести

Out of scope:
- Кешування (STORY-009)

Dependencies:
- STORY-006

Acceptance criteria:
- Конвертація працює; збої зовнішнього API не кладуть застосунок

Sub-tasks:

#### TASK-023: HTTP-клієнт публічного API курсів валют

Parent: STORY-007
Type: Sub-task
Category: Backend
Priority: P0
Estimate: M
Status: Todo

Description:
Інтерфейс `ExchangeRateProvider` (метод `rate(from, to, date)`) і реалізація на Spring `RestClient` для обраного публічного API. Таймаути, мапінг помилок API на доменний виняток `ExchangeRateUnavailableException`.

Source requirement:
Roadmap тиждень 8; опис pet-проєкту «конвертація через публічний API курсів».

Acceptance criteria:
- Курс отримується для основних валют (UAH, USD, EUR)
- Таймаут і 5xx зовнішнього API → контрольований доменний виняток
- API-ключ (якщо є) — лише через змінні оточення

Implementation notes:
- Інтерфейс provider-а — заділ під Factory Method (TASK-027) і Adapter (TASK-031)

Dependencies:
- TASK-022

Risks:
- Ліміти безкоштовного API — обрати API з запасом за лімітами

#### TASK-024: Конвертація витрат у базову валюту

Parent: STORY-007
Type: Sub-task
Category: Backend
Priority: P0
Estimate: M
Status: Todo

Description:
Сервіс конвертації поверх `ExchangeRateProvider`; статистика (TASK-014) вміє повертати суми в базовій валюті; параметр `targetCurrency` в API статистики. Округлення — `RoundingMode.HALF_EVEN`, зафіксоване тестами.

Source requirement:
Опис pet-проєкту «конвертація витрат у базову валюту».

Acceptance criteria:
- Статистика в базовій валюті коректна (unit-тести з моком provider-а)
- Правила округлення покриті тестами
- Недоступність курсу → зрозуміла помилка API, не 500

Implementation notes:
- Базова валюта — з open question; за замовчуванням UAH, конфігурована properties

Dependencies:
- TASK-023

Risks:
- None

#### TASK-025: WireMock-тести зовнішньої інтеграції

Parent: STORY-007
Type: Sub-task
Category: QA
Priority: P0
Estimate: M
Status: Todo

Description:
WireMock-тести для `ExchangeRateProvider`: успішна відповідь, 500, таймаут, невалідний JSON, невідома валюта. Жоден тест не ходить у реальний інтернет.

Source requirement:
Розділ 5 стеку «WireMock — зовнішні API в тестах»; roadmap тиждень 8 «обробка збоїв».

Acceptance criteria:
- Всі сценарії збоїв покриті
- Тести детерміновані і проходять офлайн
- CI зелений з новими тестами

Implementation notes:
- wiremock-standalone або spring-cloud-contract-wiremock — на вибір

Dependencies:
- TASK-024

Risks:
- None

### STORY-008: Патерни I — Builder, Factory Method, Strategy

Priority: P1
Status: Todo

Goal:
Перші три патерни, застосовані до реальних проблем проєкту.

Business value:
Практична частина Цілі 4; патерни «після болю», не з теорії.

Scope:
- Strategy (звіти), Factory Method (провайдери курсів), Builder (тестові дані/звіти)

Out of scope:
- Патерни без реальної проблеми

Dependencies:
- STORY-007

Acceptance criteria:
- Кожен патерн — окремий PR з обґрунтуванням «проблема → патерн → альтернативи»

Sub-tasks:

#### TASK-026: Strategy: стратегії формування звітів

Parent: STORY-008
Type: Sub-task
Category: Backend
Priority: P1
Estimate: M
Status: Todo

Description:
Ендпоінт звітів `GET /api/v1/reports?type=monthly|by-category&format=json|csv`. Інтерфейс `ReportStrategy` з реалізаціями за типом; вибір стратегії — за параметром запиту. До патерну — показати в PR «біль»: як виглядав би if/else-ланцюжок.

Source requirement:
Roadmap тиждень 9 «стратегії звітів (місяць/категорія/CSV-експорт)»; Ціль 4.

Acceptance criteria:
- Додавання нового типу звіту не змінює існуючі класи (демонстрація OCP)
- Кожна стратегія покрита unit-тестами
- Опис PR: проблема → патерн → альтернативи

Implementation notes:
- CSV-формат — окремим маленьким PR (відповідь на open question)

Dependencies:
- TASK-025

Risks:
- Overengineering — якщо стратегій лише 2, обґрунтувати чому патерн все одно доречний

#### TASK-027: Factory Method: фабрика провайдерів курсів валют

Parent: STORY-008
Type: Sub-task
Category: Backend
Priority: P1
Estimate: M
Status: Todo

Description:
`ExchangeRateProviderFactory`, що обирає реалізацію `ExchangeRateProvider` за конфігурацією (`app.exchange.provider=primary|fallback|fake`). Fake-провайдер з фіксованими курсами — для локальної розробки і тестів.

Source requirement:
Roadmap тиждень 9 «фабрика провайдерів курсів»; Ціль 4.

Acceptance criteria:
- Провайдер перемикається конфігурацією без зміни коду
- Fake-провайдер використовується в інтеграційних тестах
- PR пояснює відмінність Factory Method від простого Spring-бінa з @ConditionalOnProperty

Implementation notes:
- Чесно обговорити в PR: Spring DI частково і є фабрикою — де межа

Dependencies:
- TASK-026

Risks:
- None

#### TASK-028: Builder: тестові дані і складні об'єкти звіту

Parent: STORY-008
Type: Sub-task
Category: Backend
Priority: P1
Estimate: S
Status: Todo

Description:
Builder для `ReportRequest` (період, фільтри, валюта, формат) і test-data builder-и (`anExpense().withAmount(...).build()`) для фікстур з TASK-021. Порівняти в PR з телескопічними конструкторами і сеттерами.

Source requirement:
Ціль 4 (Builder у списку патернів); roadmap тиждень 9.

Acceptance criteria:
- Тестові фікстури переведені на builder-и
- Обов'язкові поля не можуть бути пропущені (компіляція або виняток)

Implementation notes:
- Для records — статичний builder або окремий клас

Dependencies:
- TASK-027

Risks:
- None

### STORY-009: Патерни II — Decorator, Adapter, події, Cache-Aside

Priority: P1
Status: Todo

Goal:
Решта патернів Цілі 4 + продакшен-патерн кешування.

Business value:
Закриває Checkpoint #3; кеш знімає залежність від лімітів зовнішнього API.

Scope:
- Cache-Aside, Decorator, Adapter, події (Observer)

Out of scope:
- Redis / розподілений кеш

Dependencies:
- STORY-008

Acceptance criteria:
- Checkpoint #3 (тиждень 10) пройдено: ≥ 6 патернів, пояснення на whiteboard

Sub-tasks:

#### TASK-029: Cache-Aside: кеш курсів валют

Parent: STORY-009
Type: Sub-task
Category: Backend
Priority: P1
Estimate: M
Status: Todo

Description:
Кешувати курси за ключем (from, to, date) у in-memory кеші (Caffeine) за схемою Cache-Aside: читання → промах → похід у API → запис у кеш. TTL конфігурований. Тести: hit, miss, протухання.

Source requirement:
Roadmap тиждень 10 «Cache-Aside для курсів валют»; Checkpoint #3 «кеш працює і покритий тестами».

Acceptance criteria:
- Повторний запит курсу не ходить у зовнішній API (перевірено тестом з WireMock verify)
- TTL працює
- PR пояснює Cache-Aside vs Read-Through

Implementation notes:
- Свідомо руками через Caffeine, а не @Cacheable — щоб побачити патерн; @Cacheable згадати в PR як альтернативу

Dependencies:
- TASK-028

Risks:
- None

#### TASK-030: Decorator: логування і метрики поверх провайдера курсів

Parent: STORY-009
Type: Sub-task
Category: Backend
Priority: P1
Estimate: M
Status: Todo

Description:
`LoggingExchangeRateProvider` і `TimingExchangeRateProvider` — декоратори над `ExchangeRateProvider`, композиція: cache → logging → timing → real. Показати в PR, що кеш з TASK-029 теж можна оформити декоратором.

Source requirement:
Roadmap тиждень 10 (Decorator у Патернах II); Ціль 4.

Acceptance criteria:
- Декоратори комбінуються в будь-якому порядку
- Базова реалізація не знає про логування/тайминг
- Unit-тести на кожен декоратор

Implementation notes:
- Пояснити на whiteboard різницю Decorator vs успадкування (Checkpoint #3)

Dependencies:
- TASK-029

Risks:
- None

#### TASK-031: Adapter: другий провайдер курсів під спільний інтерфейс

Parent: STORY-009
Type: Sub-task
Category: Backend
Priority: P1
Estimate: M
Status: Todo

Description:
Підключити другий публічний API курсів з іншим форматом відповіді через Adapter до `ExchangeRateProvider`. Зареєструвати у фабриці з TASK-027 як fallback.

Source requirement:
Ціль 4 (Adapter у списку); roadmap тиждень 10.

Acceptance criteria:
- Обидва провайдери працюють через один інтерфейс
- WireMock-тести на формат другого API
- Фабрика вміє fallback при недоступності основного

Implementation notes:
- Fallback-логіка проста (try primary → catch → secondary) — без Resilience4j

Dependencies:
- TASK-030

Risks:
- None

#### TASK-032: Події: «ліміт бюджету перевищено» (Observer)

Parent: STORY-009
Type: Sub-task
Category: Backend
Priority: P1
Estimate: M
Status: Todo

Description:
Місячний ліміт бюджету на категорію; при перевищенні сервіс публікує `BudgetExceededEvent` (Spring ApplicationEvent). Слухачі: лог-нотифікація і запис у таблицю notifications. Тести на публікацію і обробку.

Source requirement:
Roadmap тиждень 10 «подія "ліміт бюджету перевищено"»; Ціль 4 (Observer/події).

Acceptance criteria:
- Додавання нового слухача не змінює сервіс витрат
- Подія публікується лише при перетині ліміту (межові тести: рівно ліміт, ліміт + 0.01)
- Flyway-міграція для limits/notifications

Implementation notes:
- Пояснити зв'язок Spring events ↔ класичний Observer у PR

Dependencies:
- TASK-031

Risks:
- None

### STORY-010: Capstone + ретроспектива

Priority: P0
Status: Todo

Goal:
Самостійна фіча end-to-end і ретроспектива 12 тижнів.

Business value:
Закриває цикл навчання самостійним кейсом; фінальна перевірка всіх цілей PDP.

Scope:
- Дизайн-документ, реалізація ≥ 3 PR, тести, ретро

Out of scope:
- Технології поза стеком PDP

Dependencies:
- STORY-009

Acceptance criteria:
- Checkpoint #4 (тиждень 12): фіча самостійно, ≥ 20 PR сумарно за PDP, whiteboard-пояснення, ретро-документ

Sub-tasks:

#### TASK-033: Дизайн-документ capstone-фічі і розбиття на задачі

Parent: STORY-010
Type: Sub-task
Category: Architecture
Priority: P0
Estimate: M
Status: Todo

Description:
Самостійно обрати фічу ([Припущення] спільні бюджети або регулярні витрати), написати короткий дизайн-док (1–2 стор.): проблема, API-контракт, зміни схеми БД, план тестування, розбиття на 3+ PR. Затвердити з ментором до початку коду.

Source requirement:
Ціль 5; roadmap тиждень 11 «самостійний дизайн фічі, розбиття на задачі».

Acceptance criteria:
- Дизайн-док у репозиторії (`docs/capstone.md`), затверджений ментором
- Фіча розбита на PR-и ≤ 300 рядків кожен

Implementation notes:
- Це єдина задача, де ментор допомагає лише питаннями, не рішеннями

Dependencies:
- TASK-032

Risks:
- Занадто амбітна фіча — ментор ріже scope на затвердженні

#### TASK-034: Реалізація capstone ітераціями (≥ 3 PR)

Parent: STORY-010
Type: Sub-task
Category: Backend
Priority: P0
Estimate: L
Status: Todo

Description:
Реалізувати фічу за дизайн-доком послідовними PR-ами (міграції → домен + сервіси → API), без підказок ментора; review — як звичайно. Кожен PR самодостатній: застосунок працює після кожного мерджу.

Source requirement:
Ціль 5 M-критерій «фіча готова без підказок, ≥ 3 послідовні PR».

Acceptance criteria:
- ≥ 3 змерджені PR, кожен ≤ 300 рядків, CI зелений
- Застосунок робочий після кожного мерджу
- Відхилення від дизайн-доку зафіксовані в PR

Implementation notes:
- Ihor на review свідомо не підказує рішення — лише якість коду

Dependencies:
- TASK-033

Risks:
- Не влазить у тиждень — ріжеться scope, не якість (без тестів не мерджимо)

#### TASK-035: Тестове покриття capstone-фічі

Parent: STORY-010
Type: Sub-task
Category: QA
Priority: P0
Estimate: M
Status: Todo

Description:
Повний набір тестів фічі за пірамідою проєкту: unit (сервіси), web (MockMvc), інтеграційні (Testcontainers). Поріг JaCoCo 70% утримується. План тестування з дизайн-доку виконано.

Source requirement:
Checkpoint #4 «capstone покрита тестами»; Ціль 2.

Acceptance criteria:
- Всі рівні піраміди присутні для фічі
- JaCoCo ≥ 70% після мерджу
- Негативні сценарії покриті

Implementation notes:
- Тести пишуться в тих самих PR, що і код (принцип «тест — частина фічі»)

Dependencies:
- TASK-034

Risks:
- None

#### TASK-036: Ретроспектива PDP і план подальшого розвитку

Parent: STORY-010
Type: Sub-task
Category: Documentation
Priority: P0
Estimate: S
Status: Todo

Description:
Ретро-документ (`docs/retro.md`): що вийшло / що ні по кожній з 5 цілей, метрики (кількість PR, покриття, патерни), топ-5 уроків з code review, обраний напрям після PDP (тестування вглиб / архітектура / меседжинг / спостережуваність). Фінальний sync з ментором + whiteboard-сесія (шари, потік запиту, де що тестується).

Source requirement:
Ціль 5; Checkpoint #4 «ретро-документ і вибір напряму на потім».

Acceptance criteria:
- Ретро-документ у репозиторії
- Whiteboard-сесія пройдена
- Наступний крок розвитку зафіксований

Implementation notes:
- Розділ 10 PDP містить варіанти напрямів без рейтингу

Dependencies:
- TASK-035

Risks:
- None

## 7. Sub-task index

- TASK-001 — STORY-001 — Створити репозиторій з захистом main і PR-шаблоном — P0 — S
- TASK-002 — STORY-001 — Скелет Spring Boot (Java 21, Gradle Kotlin DSL) + health-endpoint — P0 — M
- TASK-003 — STORY-001 — Налаштувати CI на GitHub Actions — P0 — M
- TASK-004 — STORY-001 — Перший PR через feature-гілку за conventional commits — P0 — S
- TASK-005 — STORY-002 — Підключити Checkstyle до збірки і CI — P0 — M
- TASK-006 — STORY-002 — Доменна модель Expense, Category, User — P0 — M
- TASK-007 — STORY-002 — Рефакторинг-вправа «поганий код → читабельний» — P1 — M
- TASK-008 — STORY-003 — Тестова інфраструктура: JUnit 5 + AssertJ + конвенції — P0 — S
- TASK-009 — STORY-003 — Сервіс статистики витрат через TDD — P0 — L
- TASK-010 — STORY-003 — Mockito: тести сервісів з моками залежностей — P0 — M
- TASK-011 — STORY-003 — Параметризовані тести і edge cases валідації — P0 — M
- TASK-012 — STORY-003 — JaCoCo-звіт покриття з порогом 70% у CI — P0 — S
- TASK-013 — STORY-004 — CRUD-ендпоінти категорій — P0 — M
- TASK-014 — STORY-004 — CRUD-ендпоінти витрат + статистика — P0 — L
- TASK-015 — STORY-004 — Валідація запитів і глобальна обробка помилок — P0 — M
- TASK-016 — STORY-004 — MockMvc-тести контролерів — P0 — M
- TASK-017 — STORY-005 — PostgreSQL у Docker Compose + підключення JPA — P0 — M
- TASK-018 — STORY-005 — Flyway-міграції початкової схеми — P0 — M
- TASK-019 — STORY-005 — Перевести сервіси на Spring Data JPA репозиторії — P0 — M
- TASK-020 — STORY-006 — Інфраструктура Testcontainers — P0 — M
- TASK-021 — STORY-006 — Інтеграційні тести репозиторіїв — P0 — M
- TASK-022 — STORY-006 — Наскрізні API-тести — P0 — M
- TASK-023 — STORY-007 — HTTP-клієнт публічного API курсів валют — P0 — M
- TASK-024 — STORY-007 — Конвертація витрат у базову валюту — P0 — M
- TASK-025 — STORY-007 — WireMock-тести зовнішньої інтеграції — P0 — M
- TASK-026 — STORY-008 — Strategy: стратегії формування звітів — P1 — M
- TASK-027 — STORY-008 — Factory Method: фабрика провайдерів курсів валют — P1 — M
- TASK-028 — STORY-008 — Builder: тестові дані і складні об'єкти звіту — P1 — S
- TASK-029 — STORY-009 — Cache-Aside: кеш курсів валют — P1 — M
- TASK-030 — STORY-009 — Decorator: логування і метрики поверх провайдера курсів — P1 — M
- TASK-031 — STORY-009 — Adapter: другий провайдер курсів під спільний інтерфейс — P1 — M
- TASK-032 — STORY-009 — Події: «ліміт бюджету перевищено» (Observer) — P1 — M
- TASK-033 — STORY-010 — Дизайн-документ capstone-фічі і розбиття на задачі — P0 — M
- TASK-034 — STORY-010 — Реалізація capstone ітераціями (≥ 3 PR) — P0 — L
- TASK-035 — STORY-010 — Тестове покриття capstone-фічі — P0 — M
- TASK-036 — STORY-010 — Ретроспектива PDP і план подальшого розвитку — P0 — S

## 8. Testing checklist

- Unit tests: сервіси і домен на JUnit 5 + AssertJ + Mockito; TDD для сервісу статистики; параметризовані тести бізнес-правил; покриття ≥ 70% (JaCoCo, поріг у CI).
- Integration tests: Testcontainers з реальною PostgreSQL (≥ 5 тестів); репозиторії, кастомні запити, Flyway-міграції; наскрізні сценарії API.
- API tests: MockMvc для всіх ендпоінтів — happy path, валідація (400), 404, 409, формат ProblemDetail.
- UI tests: не релевантно (backend-only).
- Migration tests: міграції Flyway ганяються в інтеграційних тестах на чистій БД; повторний старт не ламає застосунок.
- Permissions and access control: базово — поза scope; якщо capstone включає auth — тести 401/403 обов'язкові.
- Edge cases: порожні періоди статистики, від'ємні/нульові суми, дата в майбутньому, дублікати категорій, недоступний зовнішній API (таймаут, 5xx, битий JSON), межа ліміту бюджету (рівно ліміт / ліміт + 0.01), округлення валют.
- Regression checks: повний прогін unit + integration у CI на кожен PR; JaCoCo-поріг не знижується.

## 9. Definition of Done

- All P0 tasks are implemented.
- Acceptance criteria are met.
- Tests are added or updated.
- API contracts are documented.
- Database migrations are reviewed.
- Permission checks are covered.
- Logs and error handling are added.
- Documentation is updated.

## 10. Recommended implementation order

1. TASK-001 — репозиторій, захист main, PR-шаблон
2. TASK-002 — скелет Spring Boot + health-endpoint
3. TASK-003 — CI GitHub Actions
4. TASK-004 — перший PR через feature-гілку
5. TASK-005 — Checkstyle у CI
6. TASK-006 — доменна модель
7. TASK-007 — рефакторинг-вправа
8. TASK-008 — тестова інфраструктура JUnit 5 + AssertJ
9. TASK-009 — сервіс статистики через TDD
10. TASK-010 — Mockito
11. TASK-011 — параметризовані тести і edge cases
12. TASK-012 — JaCoCo з порогом 70%
13. TASK-013 — CRUD категорій
14. TASK-014 — CRUD витрат + статистика
15. TASK-015 — валідація і обробка помилок
16. TASK-016 — MockMvc-тести
17. TASK-017 — PostgreSQL + JPA
18. TASK-018 — Flyway-міграції
19. TASK-019 — сервіси на Spring Data JPA
20. TASK-020 — інфраструктура Testcontainers
21. TASK-021 — інтеграційні тести репозиторіїв
22. TASK-022 — наскрізні API-тести
23. TASK-023 — клієнт API курсів валют
24. TASK-024 — конвертація в базову валюту
25. TASK-025 — WireMock-тести
26. TASK-026 — Strategy: звіти
27. TASK-027 — Factory Method: провайдери курсів
28. TASK-028 — Builder: тестові дані
29. TASK-029 — Cache-Aside: кеш курсів
30. TASK-030 — Decorator: логування/метрики
31. TASK-031 — Adapter: другий провайдер
32. TASK-032 — події: ліміт бюджету
33. TASK-033 — дизайн-док capstone
34. TASK-034 — реалізація capstone (≥ 3 PR)
35. TASK-035 — тестове покриття capstone
36. TASK-036 — ретроспектива і план розвитку

## 11. Notes for developers

- Кожна задача = окрема feature-гілка = окремий PR ≤ 300 рядків; мердж лише після approve ментора і зеленого CI. Це не бюрократія — це і є навичка, яку тренуємо.
- Тест — частина фічі: PR з логікою без тестів не мерджиться. Пишіть тест до або разом з кодом, порядок комітів це покаже.
- Найменування — предмет кожного review: імена мають читатися без коментарів; повторювані зауваження виписуйте у власний чекліст і проганяйте його на self-review перед відправкою PR.
- Патерни застосовуємо «після болю»: спершу покажіть у PR проблему (if/else-ланцюжок, дублювання), потім патерн і альтернативи. Патерн без проблеми — привід відхилити PR.
- Метрика проти залипання в теорії: годин коду / годин відео-статей > 2.
- Не наздоганяйте пропущений тиждень — переносьте план; чек-поінти (тижні 3, 7, 10, 12) не перескакуємо.
- Орієнтовний темп: ~3 задачі на тиждень на початку (S/M), 1–2 на тижнях з L-задачами.
