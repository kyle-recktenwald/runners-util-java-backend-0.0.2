CREATE TABLE abstract_crud_entity (
    create_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP,
    created_by_user_id VARCHAR(255),
    updated_by_user_id VARCHAR(255),
    is_deleted BOOLEAN
);

CREATE TABLE routes (
    route_id VARCHAR(36) NOT NULL,
    name VARCHAR(255),
    distance DOUBLE,
    user_id VARCHAR(255),
    create_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP,
    created_by_user_id VARCHAR(255),
    updated_by_user_id VARCHAR(255),
    is_deleted BOOLEAN,
    PRIMARY KEY (route_id)
);

CREATE TABLE runs (
    run_id VARCHAR(36) NOT NULL,
    distance DOUBLE,
    start_date_time TIMESTAMP,
    duration BIGINT,
    route_id VARCHAR(36),
    user_id VARCHAR(255),
    create_date TIMESTAMP NOT NULL,
    update_date TIMESTAMP,
    created_by_user_id VARCHAR(255),
    updated_by_user_id VARCHAR(255),
    is_deleted BOOLEAN,
    PRIMARY KEY (run_id),
    FOREIGN KEY (route_id) REFERENCES routes (route_id)
);
