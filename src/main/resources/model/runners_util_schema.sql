CREATE TABLE routes (
    route_id VARCHAR(255) NOT NULL,
    create_date TIMESTAMP(6) NOT NULL,
    created_by_user_id VARCHAR(255),
    is_deleted BOOLEAN,
    update_date TIMESTAMP(6),
    updated_by_user_id VARCHAR(255),
    distance FLOAT(53),
    name VARCHAR(255),
    user_id VARCHAR(255),
    PRIMARY KEY (route_id)
);

CREATE TABLE runs (
    run_id VARCHAR(255) NOT NULL,
    create_date TIMESTAMP(6) NOT NULL,
    created_by_user_id VARCHAR(255),
    is_deleted BOOLEAN,
    update_date TIMESTAMP(6),
    updated_by_user_id VARCHAR(255),
    distance FLOAT(53),
    duration BIGINT,
    start_date_time TIMESTAMP(6),
    user_id VARCHAR(255),
    route_id VARCHAR(255),
    PRIMARY KEY (run_id)
);

ALTER TABLE runs 
    ADD CONSTRAINT FKlspgexrfehy7nck0qkvpsuyg4 
    FOREIGN KEY (route_id) 
    REFERENCES routes (route_id);
