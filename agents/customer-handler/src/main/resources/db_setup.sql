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

create table offer
(
  id                  bigint(20)  not null auto_increment,
  offer_id            binary(16)  not null,
  customer_id         bigint(20)  not null,
  valid_until         datetime    not null,
  state               varchar(15) not null,
  type                varchar(15) not null,
  size                decimal     not null,
  price               decimal     not null,
  demand_change_since datetime    not null,
  demand_change_until datetime    not null,
  primary key (id)
);

CREATE TABLE `obligation`
(
  `id`              bigint(20)     NOT NULL AUTO_INCREMENT,
  `customer_id`     varchar(32)    NOT NULL,
  `state`           varchar(25)    NOT NULL,
  `size`            decimal(10, 2) NOT NULL,
  `percentage_kept` decimal(10, 2) NOT NULL,
  `offer_id`        bigint(20)     NOT NULL,
  PRIMARY KEY (`id`),
  foreign key (offer_id) references offer (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

create view obligation_view as
SELECT ob.*, of.demand_change_since as since, of.demand_change_until as until
FROM obligation ob
       join offer `of` on ob.offer_id = of.id;

