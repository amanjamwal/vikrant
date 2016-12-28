--liquibase formatted sql

--changeset ajamwal:1
CREATE TABLE basic_authentication (
  api_key   VARCHAR(255) NOT NULL,
  is_active bool NOT NULL DEFAULT 1,
  PRIMARY KEY (api_key)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
--rollback drop table `basic_authentication`;
