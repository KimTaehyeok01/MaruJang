-- MaruJang 스키마 정의 (참고용 DDL, ERD 기준, MySQL 8)
-- 개발 중에는 spring.jpa.hibernate.ddl-auto=update 로 JPA가 테이블을 자동 생성/반영합니다.
-- 이 파일은 자동 실행되지 않으며, 스키마를 한눈에 보기 위한 문서 및 운영 반영용 참고 파일입니다.

CREATE TABLE users (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    email         VARCHAR(255) NOT NULL UNIQUE,
    password      VARCHAR(255),
    role          VARCHAR(20)  NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE merchant (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id        BIGINT       NOT NULL UNIQUE,
    market_name    VARCHAR(100) NOT NULL,
    business_type  VARCHAR(100),
    phone          VARCHAR(20),
    CONSTRAINT fk_merchant_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE supplier (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT       NOT NULL UNIQUE,
    company_name     VARCHAR(100) NOT NULL,
    business_number  VARCHAR(20),
    phone            VARCHAR(20),
    CONSTRAINT fk_supplier_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE item (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    category    VARCHAR(50),
    unit        VARCHAR(20)  NOT NULL
);

CREATE TABLE inventory (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    supplier_id  BIGINT        NOT NULL,
    item_id      BIGINT        NOT NULL,
    quantity     INT           NOT NULL DEFAULT 0,
    price        DECIMAL(12,2) NOT NULL,
    updated_at   TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT uk_inventory_supplier_item UNIQUE (supplier_id, item_id),
    CONSTRAINT fk_inventory_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id),
    CONSTRAINT fk_inventory_item FOREIGN KEY (item_id) REFERENCES item(id)
);

CREATE TABLE orders (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    merchant_id  BIGINT       NOT NULL,
    supplier_id  BIGINT       NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    created_at   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_merchant FOREIGN KEY (merchant_id) REFERENCES merchant(id),
    CONSTRAINT fk_orders_supplier FOREIGN KEY (supplier_id) REFERENCES supplier(id)
);

CREATE TABLE order_item (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id        BIGINT        NOT NULL,
    inventory_id    BIGINT        NOT NULL,
    quantity        INT           NOT NULL,
    price_snapshot  DECIMAL(12,2) NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_item_inventory FOREIGN KEY (inventory_id) REFERENCES inventory(id)
);
