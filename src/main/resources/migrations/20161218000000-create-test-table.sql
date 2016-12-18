--liquibase formatted sql

--changeset ajamwal:1
CREATE TABLE cache (
  id   VARCHAR(255) NOT NULL,
  item VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8;
--rollback drop table `cache`;
