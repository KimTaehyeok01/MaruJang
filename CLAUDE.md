# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MaruJang(마루장)은 서울 중랑구 전통시장 상인과 식자재 도매업체를 연결하는 B2B 매칭 플랫폼이다. 도매업체가 재고와 가격을 등록하면, 상인이 검색·필터링해서 필요한 식자재를 주문한다. 백엔드(Spring Boot)와 프론트엔드(React)가 분리된 모노레포 구조다.

학습·포트폴리오 목적의 프로젝트다. 사용자는 모든 설계 결정을 면접에서 직접 설명할 수 있어야 하며, React는 처음 배우는 중이다. 그래서 난이도를 의도적으로 CRUD 수준으로 제한한다(아래 Scope constraints 참고).

진행 순서는 ERD(완료) → 백엔드 API → 프론트엔드다. 백엔드가 기능적으로 거의 완성된 뒤에 프론트 작업을 시작한다.

## Commands

### Backend

아직 Gradle wrapper(`gradlew`)가 없다. 로컬에 Gradle 전역 설치가 없어 `gradle-wrapper.jar`를 생성하지 못했다. IntelliJ로 `backend/`를 열면 wrapper 설정을 잡아주거나, Gradle 설치 후 `gradle wrapper`를 한 번 실행하면 된다. wrapper가 생기기 전까지는 아래 명령을 `gradle`로 실행한다.

```bash
# 백엔드 디렉토리에서 실행
cd backend

# API 실행 (포트 8080)
gradle bootRun

# 빌드 + 테스트
gradle build

# 테스트만 실행
gradle test
```

JDK 21이 필요하다(`build.gradle`의 Gradle toolchain이 21로 고정). 이 PC는 시스템 PATH/JAVA_HOME이 JDK 17을 가리키지만, IntelliJ가 관리하는 JDK 21(`C:\Users\User\.jdks\ms-21.0.10`)이 설치돼 있어 IntelliJ 빌드는 정상이다. 터미널에서 직접 빌드하려면 그 셸의 `JAVA_HOME`을 21로 맞춰야 한다.

### Frontend

```bash
# 프론트엔드 디렉토리에서 실행
cd frontend

# 의존성 설치
npm install

# 개발 서버 (포트 5173, /api → localhost:8080 프록시)
npm run dev

# 프로덕션 빌드
npm run build
```

## Architecture

### Request Flow

```
Client → Vite Proxy (/api) → Spring Boot :8080
                ↓
       JwtAuthenticationFilter
       (Authorization: Bearer <token> 헤더 검증)
                ↓
       Domain Controller → Service → Repository → MySQL
```

OAuth2 로그인(Google/Kakao) 성공 시 `OAuth2SuccessHandler`가 JWT를 발급해 프론트엔드로 리다이렉트한다. 이후 REST 요청은 `Authorization: Bearer <token>` 헤더로 인증한다. 세션은 사용하지 않는다(STATELESS).

WebSocket은 Spring Security 필터체인을 우회하므로 `StompHandler`(ChannelInterceptor)에서 별도로 JWT 검증한다. 클라이언트는 STOMP CONNECT 시 `Authorization: Bearer <token>` 헤더를 포함해야 한다.

### Backend Structure (`backend/src/main/java/com/marujang/`)

- `domain/` — 비즈니스 도메인. 각 도메인은 `controller / service / repository / entity / dto` 로 구성
- `global/` — 전체 공통 인프라
  - `config/` — `SecurityConfig`(OAuth2 + JWT 필터체인 + CORS), `WebSocketConfig`(STOMP 엔드포인트 + 메시지 브로커)
  - `security/jwt/` — `JwtAuthenticationFilter`, `JwtTokenProvider`, `JwtProperties`, `StompHandler`
  - `security/oauth2/` — `CustomOAuth2UserService`, `OAuth2SuccessHandler`
  - `exception/` — 전역 예외 처리(`GlobalExceptionHandler`)

도메인 목록: `user`, `merchant`, `supplier`, `item`, `inventory`, `order`

패키지는 레이어형이 아니라 도메인형이다. 프론트도 동일하게 도메인별로 나눈다.

### Frontend Structure (`frontend/src/`)

- `domains/` — 도메인별 폴더(`auth`, `merchant`, `supplier`, `item`, `inventory`, `order`). 각 도메인은 `api / components / pages` 로 구성
- `common/` — 공통 인프라
  - `api/axiosInstance.js` — Axios 인스턴스. 요청 인터셉터가 저장된 accessToken을 자동으로 헤더에 붙인다
  - `context/AuthContext.jsx` — 전역 인증 상태(accessToken, login/logout)
- `routes/AppRouter.jsx` — 라우트 정의

### Core Entities

- `User` — 공통 계정. `role`: `MERCHANT` / `SUPPLIER`. 테이블명은 `users`(예약어 회피)
- `Merchant` — `User`와 1:1(JPA 상속이 아니라 FK 연관). market_name, business_type, phone
- `Supplier` — `User`와 1:1(FK 연관). company_name, business_number, phone
- `Item` — 업체 공용 식자재 카탈로그. name, category, unit
- `Inventory` — 업체별 품목 재고/가격. `(supplier_id, item_id)` 유니크
- `Order` — 상인이 생성하며 **단일 공급자에 묶인다**(`Order.supplier_id`). `status`: REQUESTED / CONFIRMED / COMPLETED / CANCELLED. "주문 = 단일 공급자" 규칙은 DB 제약이 아니라 서비스 계층에서 강제한다 — `OrderItem` 추가 시 `inventory.supplier_id == order.supplier_id`를 검증한다.
- `OrderItem` — `Item`이 아니라 `Inventory`를 참조한다(가격·공급자가 재고에 종속되므로). `price_snapshot`은 주문 시점 가격 복사본이라, 이후 가격이 바뀌어도 과거 주문 금액은 변하지 않는다.

`backend/src/main/resources/db.sql`에 전체 DDL이 사람이 읽기 좋은 참고용으로 있다. 자동 실행되지 않으며, 개발 스키마는 `spring.jpa.hibernate.ddl-auto: update`로 JPA가 생성한다.

### Authentication

- OAuth2 로그인(Google/Kakao) → 성공 시 백엔드가 JWT(access) 발급 → 프론트로 리다이렉트
- 프론트는 accessToken을 `localStorage`에 저장하고, `axiosInstance` 요청 인터셉터가 `Authorization` 헤더에 자동 첨부
- 최초 로그인 사용자는 역할(MERCHANT/SUPPLIER)을 선택해야 한다 — OAuth2로는 이메일만 오므로 별도 흐름이 필요

## Swagger

백엔드 실행 후 `http://localhost:8080/swagger-ui/index.html` 에서 API 문서 확인 가능. 인증 없이 접근할 수 있도록 SecurityConfig에서 `/swagger-ui/**`, `/v3/api-docs/**` 를 permitAll 처리해뒀다.

## WebSocket

- 엔드포인트: `/ws` (SockJS 폴백 지원)
- 구독: `/topic/**` — 서버가 클라이언트로 브로드캐스트하는 경로
- 발행: `/app/**` — 클라이언트가 서버로 메시지를 보내는 경로
- STOMP CONNECT 시 반드시 `Authorization: Bearer <token>` 헤더 포함 (`StompHandler`에서 검증)

## Scope Constraints (의도적 제약 — 이 이상 확장하지 말 것)

- Redis 분산락, 메시지 큐 금지.
- 재고 동시 수정은 기본 `@Transactional`만 사용. 커스텀 락 금지.
- 검색/매칭은 QueryDSL 동적 쿼리(조건별 필터링)까지만. 그 이상은 안 한다.
- 프론트 상태관리 라이브러리(Redux 등) 금지. `useState` / `useContext`만 사용한다.

## Working with this codebase

- 진행 순서는 ERD → 백엔드 API → 프론트다. 사용자가 달리 말하지 않는 한 이 순서를 지킨다.
- React 패턴을 처음 도입할 때(새 컴포넌트, 새 API 호출 패턴)는 _왜 그렇게 나눴는지_(컴포넌트를 왜 여기서 쪼갰는지, state가 왜 여기 있는지)를 코드 설명이 아니라 이유 중심으로 설명한다. 한 번 설명한 패턴의 반복은 사용자가 직접 작성하게 둔다.
- 본질적으로 복잡한 부분(QueryDSL 동적 쿼리, Spring Security 설정, React 상태 흐름)은 코드를 작성하되 라인별 주석으로 각 부분이 무엇을 하는지 설명한다.
- 사용자가 이해 못 한다고 하면 멈추고 개념부터 설명한다. 그냥 진도 나가지 않는다.

## Rules

- 커밋은 사용자가 직접 한다. 코드 수정만 하고 `git commit`과 `git push`는 실행하지 않는다.
- 커밋 메시지에 `Co-Authored-By: Claude` 를 절대 추가하지 않는다.

## Behavioral Guidelines

Behavioral guidelines to reduce common LLM coding mistakes.

**Tradeoff:** These guidelines bias toward caution over speed. For trivial tasks, use judgment.

### 1. Think Before Coding

**Don't assume. Don't hide confusion. Surface tradeoffs.**

Before implementing:

- State your assumptions explicitly. If uncertain, ask.
- If multiple interpretations exist, present them - don't pick silently.
- If a simpler approach exists, say so. Push back when warranted.
- If something is unclear, stop. Name what's confusing. Ask.

### 2. Simplicity First

**Minimum code that solves the problem. Nothing speculative.**

- No features beyond what was asked.
- No abstractions for single-use code.
- No "flexibility" or "configurability" that wasn't requested.
- No error handling for impossible scenarios.
- If you write 200 lines and it could be 50, rewrite it.

Ask yourself: "Would a senior engineer say this is overcomplicated?" If yes, simplify.

### 3. Surgical Changes

**Touch only what you must. Clean up only your own mess.**

When editing existing code:

- Don't "improve" adjacent code, comments, or formatting.
- Don't refactor things that aren't broken.
- Match existing style, even if you'd do it differently.
- If you notice unrelated dead code, mention it - don't delete it.

When your changes create orphans:

- Remove imports/variables/functions that YOUR changes made unused.
- Don't remove pre-existing dead code unless asked.

The test: Every changed line should trace directly to the user's request.

### 4. Goal-Driven Execution

**Define success criteria. Loop until verified.**

Transform tasks into verifiable goals:

- "Add validation" → "Write tests for invalid inputs, then make them pass"
- "Fix the bug" → "Write a test that reproduces it, then make it pass"
- "Refactor X" → "Ensure tests pass before and after"

For multi-step tasks, state a brief plan:

```
1. [Step] → verify: [check]
2. [Step] → verify: [check]
3. [Step] → verify: [check]
```

### 5. No Closing Colons (Korean Output)

**End Korean sentences with a period, not a colon.**

When the user writes in Korean, output is also Korean:

- Don't end sentences with `:` even if the next line is a list or example.
- The test: every Korean sentence terminator should be `.`, `?`, or `!` — not `:`.
- Colons are fine inside code, key-value pairs, or labels. Not as sentence enders.

### 6. File Header Comments in Korean

**First line of every new source file: a one-line Korean comment stating its role.**

When creating a new file:

- TypeScript/JavaScript: `// 사용자 인증 상태를 관리하는 Context Provider`
- Python: `# KIS API 호출을 비동기로 래핑하는 클라이언트`
- SQL: `-- 일별 집계 결과를 저장하는 머티리얼라이즈드 뷰`
- Place it directly under required directives (`'use client'`, `'use server'`, shebang).
- Skip config files (`*.config.ts`, `package.json`, etc.).

### 7. Plan + Checklist + Context Notes

**Before any non-trivial task, produce three artifacts. Don't start coding without them.**

- **Plan** — what we're building and why.
- **Checklist** (`checklist.md`) — concrete tasks as checkboxes. Tick as you go.
- **Context Notes** (`context-notes.md`) — decisions made during the work and the reasoning behind them. Append continuously.

### 8. Run Tests Before Marking Complete

**If you touched code, run the tests before saying "done".**

- `npm test`, `pytest`, `cargo test`, whatever the project uses — run it.
- If tests pass, report results. If they fail, fix and re-run.
- No test setup? At minimum, verify the project builds/compiles.
- Run tests proactively — not after the user signals completion.

### 9. Semantic Commits

**Note: The user commits manually. Never run `git commit`.**

- The test: "Can I describe this change in one sentence?" If yes, it's one logical unit. If no, the changes are still mixed.
- Don't accumulate unrelated edits — keep changes focused for easy rollback.

### 10. Read Errors, Don't Guess

**Read the actual error/log line. Don't pattern-match from memory.**

When something fails:

- Read the full error message and stack trace.
- Check the actual log output, not what you assume it should say.
- Don't apply a "common fix" before confirming the cause.
- If unclear, add a print/log to verify state — then fix.
