-- MaruJang 스키마 정의 (참고용 DDL, ERD 기준)
-- 개발 중에는 spring.jpa.hibernate.ddl-auto=update 로 JPA가 테이블을 자동 생성/반영합니다.
-- 이 파일은 자동 실행되지 않으며, 스키마를 한눈에 보기 위한 문서 및 운영 반영용 참고 파일입니다.

CREATE TABLE users (
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255),
    role          VARCHAR(20)  NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE merchant (
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT       NOT NULL UNIQUE REFERENCES users(id),
    market_name    VARCHAR(100) NOT NULL,
    business_type  VARCHAR(100),
    phone          VARCHAR(20)
);

CREATE TABLE supplier (
    id               BIGSERIAL PRIMARY KEY,
    user_id          BIGINT       NOT NULL UNIQUE REFERENCES users(id),
    company_name     VARCHAR(100) NOT NULL,
    business_number  VARCHAR(20),
    phone            VARCHAR(20)
);

CREATE TABLE item (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    category    VARCHAR(50),
    unit        VARCHAR(20)  NOT NULL
);

CREATE TABLE inventory (
    id           BIGSERIAL PRIMARY KEY,
    supplier_id  BIGINT        NOT NULL REFERENCES supplier(id),
    item_id      BIGINT        NOT NULL REFERENCES item(id),
    quantity     INTEGER       NOT NULL DEFAULT 0,
    price        NUMERIC(12,2) NOT NULL,
    updated_at   TIMESTAMP     NOT NULL DEFAULT now(),
    CONSTRAINT uk_inventory_supplier_item UNIQUE (supplier_id, item_id)
);

CREATE TABLE orders (
    id           BIGSERIAL PRIMARY KEY,
    merchant_id  BIGINT       NOT NULL REFERENCES merchant(id),
    supplier_id  BIGINT       NOT NULL REFERENCES supplier(id),
    status       VARCHAR(20)  NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE order_item (
    id              BIGSERIAL PRIMARY KEY,
    order_id        BIGINT        NOT NULL REFERENCES orders(id),
    inventory_id    BIGINT        NOT NULL REFERENCES inventory(id),
    quantity        INTEGER       NOT NULL,
    price_snapshot  NUMERIC(12,2) NOT NULL
);
