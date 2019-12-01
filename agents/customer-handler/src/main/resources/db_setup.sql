CREATE SCHEMA wsd_dsm;

CREATE
ROLE IF NOT EXISTS 'wsd_write', 'wsd_read', 'wsd_tables';
GRANT SELECT ON wsd_dsm.* TO 'wsd_read';
GRANT INSERT, UPDATE, DELETE ON wsd_dsm.* TO 'wsd_write';
GRANT CREATE ON wsd_dsm.* TO 'wsd_tables';

CREATE USER 'wsd_dsm_user'@'%' IDENTIFIED BY 'user_!234';
CREATE USER 'wsd_dsm_dev'@'%' IDENTIFIED BY 'dev_!234';

GRANT 'wsd_read' TO 'wsd_dsm_user';
GRANT 'wsd_write' TO 'wsd_dsm_user';
GRANT 'wsd_read' TO 'wsd_dsm_dev';
GRANT 'wsd_write' TO 'wsd_dsm_dev';
GRANT 'wsd_tables' TO 'wsd_dsm_dev';

SET DEFAULT ROLE 'wsd_read', 'wsd_write' TO 'wsd_dsm_user';
SET DEFAULT ROLE 'wsd_read', 'wsd_write', 'wsd_tables' TO 'wsd_dsm_dev';

FLUSH PRIVILEGES;

CREATE TABLE `obligation`
(
  `id`              bigint(20)     NOT NULL AUTO_INCREMENT,
  `CUSTOMER_ID`     varchar(32)    NOT NULL,
  `STATE`           varchar(25)    NOT NULL,
  `SIZE`            decimal(10, 2) NOT NULL,
  `PERCENTAGE_KEPT` decimal(10, 2) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
