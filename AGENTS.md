# AGENT GUIDANCE

These instructions are tuned for further agentic coding assistants working across the backend and frontend of `transaction-management`. The goal is to keep the build/test workflow predictable, honor existing style choices, and highlight the places that need care (credentials, cache behavior, request wrapping, etc.).

## Tooling & Common Commands

- **Backend (Spring Boot, Maven 3.10+, JDK 17)**
  - Use the included Maven wrapper from `/workspace/transaction-management-backend` whenever possible so everyone shares `settings.xml` and `maven.config`.
  - Standard iterate-build: `./mvnw -pl transaction-management-backend-web -am clean package -DskipTests=false` (note `.mvn/maven.config` injects `-DskipTests`, so explicitly set it to `false` before running tests).
  - When you need a quick smoke build for more modules: `./mvnw -pl transaction-management-backend-service,transaction-management-backend-dao -am clean package -DskipTests=false`.
- **Frontend (Vue 2 + ant-design-vue, Node 16)**
  - Install deps once: `cd transaction-management-frontend && npm install`.
  - Start dev server with hot reload: `npm run serve` (equivalent `serve:dev`). Production preview: `npm run serve:prod` or build with `npm run build:prod`.
  - Lint/fix: `npm run lint` (auto-fixes where possible) or `npm run lint:nofix` to keep existing formatting intact.
- **Container / Deploy**
  - The root `deploy.sh` clones the repo, builds backend via Maven, then builds the Docker image `transaction-backend:1.0` and runs it on port 80. Run it from the repo root only.
  - Docker build is triggered by `transaction-management-backend/Dockerfile`; use `DOCKER_BUILDKIT=1` when re-building manually to match the script.
- **Additional backend helpers**
  - When iterating on the job module, run `./mvnw -pl transaction-management-backend-job -am clean package -DskipTests=false` and start it via its own `JobApplication` main class.
  - RPC work is scoped inside `transaction-management-backend-rpc`; compile it with `./mvnw -pl transaction-management-backend-rpc -am clean package -DskipTests=false` before publishing SDKs.

## Running a Single Test

- **Backend single test**: `./mvnw -pl transaction-management-backend-web -am -DskipTests=false -Dtest=OrderControllerTest test` (you can swap the `-pl` module and the `-Dtest` value to target other packages; always include `-am` so shared modules compile).
- **Frontend single unit spec**: `cd transaction-management-frontend && npm run test:unit -- --runTestsByPath tests/unit/path/to/SpecName.spec.js` (the CLI will error if the file does not exist; use this pattern to scope test runs before committing).
  - There are currently no unit specs under `tests/unit`, so add to this convention when you introduce new Jest suites.

## Build Flags & Profiles

- `.mvn/maven.config` injects `-s.mvn/settings.xml` plus `-DskipTests` for every invocation, so always override with `-DskipTests=false` when tests must run.
- `settings.xml` wires the `buukle-rdc` mirrors and servers; stay on the provided profile unless you add a new repository, and never commit credentials elsewhere.
- When you change module dependencies, re-run `./mvnw -pl <module> -am dependency:tree` locally to ensure transitive artifacts resolve via the shared settings.

## Directory Layout Notes

- The backend is a multi-module Maven repo. Key modules are `-config`, `-dao`, `-service`, `-web`, `-rpc`, and `-job`. Keep shared DTOs/VOs under `-dtvo` so controller/service layers can depend on them without circular references.
- Frontend uses an Ant Design Pro-inspired layout. Views live under `src/views`, reusable components under `src/components`, and shared utilities in `src/utils`.
- API requests originate from `src/utils/request.js`; treat it as the single axios instance and retain its global error handler/`message` configuration when adding new features.
- `transaction-management-backend-common` houses shared wiring such as `Result`, `Result.Builder`, and any cross-module constants.
- `transaction-management-backend-config` centralizes MVC config, interceptors, caches, and exception translation so that `web`, `rpc`, and `job` are lightweight shells.
- `transaction-management-backend-dao` exposes MyBatis-Plus entities, mappers, and repositories while `-service` holds converters plus transactional business logic. `-web`, `-rpc`, and `-job` modules depend on `-service` and `-config` but not on `-dao` directly.
- The job module is for heavyweight async tasks, while rpc exposes outward-facing APIs or SDKs.

## Backend Layer Guidance

- **Entities & DTO/VO flow**: Entities use Lombok annotations (`@Data`, `@Builder`, `@TableName`). Converters sit in `service.impl.converter` to map between DTOs, entities, and VO wrappers so controllers/services never operate on raw persistence objects in responses.
- **Controllers**: Annotate with `@Validated`, use Jakarta validation on inputs, return `Result.Builder().success(...)`, and keep payloads strictly typed (all paths use DTO/VO objects). Avoid direct `ResponseEntity` manipulation unless a filter already handles wrapper semantics.
- **Services**: Prefer service interfaces (`OrderService`) with concrete `@Service` implementations. Protect mutation operations with `@Transactional(rollbackFor = Exception.class)` and `@CacheEvict` when any `orderCache` data could become stale.
- **DAO/Repository**: `TransactionOrderRepository` wraps MyBatis-Plus operations, so use `LambdaQueryWrapper`/`LambdaUpdateWrapper` to remain type-safe. Name repository methods to reflect the query intent rather than the verb (`getById`, `getByRequestNo`, `page`).
- **Caching**: `CacheConfig` wires a Caffeine cache named `orderCache` (max 500 entries, expire-after-write 10 minutes). `@Cacheable` keys must uniquely identify arguments (combine nullable strings via ternary selectors) and use `unless` guards to skip caching null results.
- **Logging & MDC**: Adopt `@Slf4j`. Log decision points with `log.info`/`log.error`, include IDs or request numbers, and honor MDC tracing via `TraceInterceptor` if present.

## Code Style Guidelines

## Code Style Guidelines

### Backend Java (Spring Boot + MyBatis-Plus)

- **Imports & formatting**: keep package imports grouped logically (Spring, third-party, local) and maintain the current vertical spacing. Use 4-space indents for Java files.
- **Annotations**: prefer `@Service`, `@Slf4j`, `@Builder`, `@Transactional`, `@Cacheable`, `@CacheEvict`, `@Validated`, `@RequestBody`, and Jakarta `@NotNull/@Valid` exactly as seen in `OrderController` and `OrderServiceImpl`. Follow the pattern of method-level annotations immediately above the declaration.
- **Naming**: DTOs/VOs end with `DTO`/`VO`, repositories end with `Repository`, mappers `Mapper`, converters `Converter`, and entities use descriptive nouns like `TransactionOrder`. Keep camelCase for fields and camelCase methods with verbs. Prefer `log.info`/`log.error` with contextual info (IDs, request numbers) as current logging demonstrates.
- **Error handling**: surface business failures via `ServiceException` backed by `ServiceExceptionCodeEnums`. Throw the exception after logging context-rich messages, never swallow runtime errors. Controller responses wrap everything with `Result.Builder().success(...)` or let the global `Result` wrapper translate thrown `ServiceException` instances.
- **Converters and Cache**: conversions between DTO/Entity/VO should happen through dedicated `OrderConverter` style helpers; controllers/services rarely manipulate entities directly. Cache keys concatenate method arguments (see `@Cacheable` on `getPage`). When evicting, clear the entire cache region (`@CacheEvict(..., allEntries = true)`).
- **Transactions**: annotated services that mutate the database should use `@Transactional(rollbackFor = Exception.class)` on the service method and keep repository calls in try/catch blocks only when handling expected constraints (e.g., `DuplicateKeyException`).
- **Packages**: stay under the `homework.bank` base package; pick the module-specific suffixes (`controller`, `service`, `dao`, `dtvo`) so imports remain predictable and the component scan remains happy.
- **Result wrapper**: prefer `Result.Builder().success(...)`/`.error(...)` to keep responses uniform rather than crafting raw `ResponseEntity` payloads. The global `GlobalExceptionHandler` already produces consistent `code/info/data` structures.

### Backend Testing & Validation

- Controller tests use Spring Boot/MockMvc under `transaction-management-backend-web/src/test/java`, while service tests live in `transaction-management-backend-service/src/test/java`. Follow the `@SpringBootTest` + `@Autowired` style already present.
- Keep validation annotations (`@NotNull`, `@Valid`, `@RequestParam` defaults) on controller parameters with descriptive messages.
- For new tests, mimic `OrderControllerTest` structure: fetch the Builder-based `Result`, inspect `code/info/data`, and assert on the service-level logic with mocks as needed.
- Focus on the HTTP wrapper structure everywhere: each test should assert that the `Result` `code`/`info`/`data` contract matches the API doc rather than relying on raw body strings.
- Service layer tests can reuse `LocalDateTimeAdapter` and `JsonUtil` utilities from `service.util` to keep payloads stable when asserting date formatting.
- When exercising edge cases, expect `ServiceException` to bubble up and verify the expected `ServiceExceptionCodeEnums` value alongside any custom info message.

### Frontend Vue & UI Style

- **Formatting**: rely on the existing ESLint configuration (`plugin:vue/strongly-recommended` + `@vue/standard`, overrides in `.eslintrc.js`) and follow `quotes: single`, `semi: never`, and 2-space indentation. Keep components lean and names PascalCase (e.g., `AddOrEditForm`).
- **Components**: register child components inside `components: {}` and expose methods via `methods` with concise verbs. Use `data()` functions returning plain objects; avoid inline object mutation outside Vue-managed data (when necessary, wrap in `this.$set`).
- **Templates**: keep directives aligned (e.g., `v-model`, `@click`). Limit multiple attributes per line using the `vue/max-attributes-per-line` rule already configured.
- **State/requests**: centralize API calls through `request` (`axios` instance). When posting data, include the shared `commonRequest` structure (with `head.operationTime`, `body`, etc.) since back-end endpoints expect it.
- **Notifications**: use `ant-design-vue` `message`, `notification`, or `Modal.confirm` for user feedback; mimic the existing `message.success`/`message.error` pattern.

## Frontend Experience Expectations

- When adding or reworking pages, avoid bland, default layouts—aim for purposeful typography, distinct color direction, and subtle backgrounds so each view feels intentional.
- Define CSS variables for new palettes, limit purple/dark-mode bias, and keep animation/motion to a few meaningful transitions (page load, staggered reveals) rather than micro-motions.
- Ensure pages render well on both desktop and mobile; prefer responsive flex/ant layout grids rather than fixed pixel widths.
- Respect existing layout/system patterns when working within core dashboards; only deviate when creating distinctly new experiences.

### Frontend Data & Naming Conventions

- Keep booleans prefixed with `is`/`has` and use camelCase in JS. Template variables map to the data returned by `loadData`/`request` wrappers (e.g., `queryParam`, `initvalue`).
- Filters are defined under `filters` and referenced directly in templates. Custom formatter helpers (like `gmtDateFormat`) live in `methods` and should stay stateless (use external libs such as `moment`).
- Prefer `const`/`let` per block scope rules (`prefer-const` rule is enforced). Avoid `var` unless you must maintain legacy code.

## Frontend Interaction Patterns

- Keep the shared `commonRequest` object in sync with the backend expectation (`head.operationTime`, `head.appId`, `body`). When calling request helpers, mutate `body` just before sending rather than storing stale references.
- Configure `message` globally (see `list.vue`) and favor `message.success/error` for deterministic UX. Wrap long strings in `ellipsis` components where tables display IDs or timestamps.
- Custom filters such as `statusFilter`/`statusTypeFilter` live under `filters` to keep templates declarative. Simple variance logic belongs in filters, heavier logic belongs in `methods`.
- `request` sets a fake `Authorization` header and a `User` header; keep these for local dev consistency unless security requirements change, and log to console in the interceptor only when troubleshooting.

## Frontend Build Notes

- The front-end bundle is pre-built and copied into `transaction-management-backend-web/src/main/resources/static` before the web module ships. Update this folder any time you adjust layout assets or vendor bundles.
- Use `npm run build` (or `npm run build:prod`) to produce the compiled distribution; the backend already assumes `/index.html` is served from Spring Boot static resources.
- Keep an eye on `.env` files (e.g., `VUE_APP_API_BASE_URL`, `VUE_APP_BUUKLE_APP_ID`) when running locally; they drive the `request` base URL and header values.

## Testing Expectations

- Backend service tests should assert against `ServiceExceptionCodeEnums` whenever a failure path is hit. Use `OrderServiceImplTest` as the blueprint: verify happy path returns expected VO and edge cases throw the right `ServiceException`.
- Controller integration tests should hit real endpoints (`OrderControllerTest`/`OrderControllerCrossLayerTest`) and assert on the uniform `Result` JSON structure (`code`, `info`, `data`). Mocked dependencies belong to service-layer unit tests only.
- Frontend tests are currently absent—add new Jest specs under `tests/unit` before relying on `test:unit`. Use `vue-test-utils` and shallow mounting to keep UI tests fast.

## Working with Credentials & Configuration

- `.mvn/settings.xml` contains repository credentials for `buukle-rdc`. Do not edit or expose these credentials elsewhere. If you need new repositories, add them via a new profile in `settings.xml` and share the update before committing.
- Maven always runs with `-DskipTests` (see `.mvn/maven.config`). When invoking `./mvnw` directly, append `-DskipTests=false` to run tests. Single-test runs should include the same flag.

## Observability & Logging

- Services log at key business decision points (`log.info` for start/finish, `log.error` before throwing). Use `{}` placeholders instead of string concatenation and include identifiers or request numbers when available.
- Controllers log only when the handler needs extra debugging context; most responses rely on the uniform `Result` structure and exception handlers.
- When adding cacheable endpoints, mention the key contract in the log (e.g., `Cache key = transactionOrder:${id}`). Logs should never leak sensitive request data (no full headers or tokens).

## Deployment & Observability

- `deploy.sh` is the go-to script to produce a Docker image (`transaction-backend:1.0`) and run it on port 80. Always run it from the repo root so relative paths inside the script resolve correctly.
- When running the Docker image manually, remember it exposes port 8080 inside the container and maps to host port 80, matching the script's `docker run -p 80:8080`.
- Production logging/config relies on the `transaction-management-backend-config` module; altering `GlobalExceptionHandler`, `TraceInterceptor`, or `CacheConfig` should be done conservatively.

## Cursor & Copilot Rules

- There are no `.cursor/rules` or `.cursorrules` directories, and `.github/copilot-instructions.md` is absent, so rely solely on this document for agent guidance.

## Next-Step Suggestions for Agents

- After you finish implementing changes, run the linters/test commands above as sanity checks.
- If you touched backend modules, ensure the cached `TransactionOrder` entry is invalidated (`@CacheEvict`).
- For new API work, add DTO/VO definitions to `transaction-management-backend-dtvo` and keep converters in `transaction-management-backend-service/src/main/java/.../converter`.

## Documentation & References

- The root `README.md` documents system goals, module responsibilities, and containerized deployment (including the `deploy.sh` workflow). Keep it in sync with any architectural shifts.
- The `doc/` directory holds PDFs and PNGs for system architecture, test coverage, and performance reports if you need visual reference when explaining behavior.
- Front-end behaviors are sketched in `doc/show.mp4`; use it if you need to describe the current UI flow to stakeholders.
- System design notes (e.g., `doc/application-arc-design.png`) are especially helpful when you're adding new modules or dashboards because they show how the microservices are expected to interact.
