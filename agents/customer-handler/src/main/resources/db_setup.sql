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

create table customer
(
  id          bigint(20)   not null auto_increment,
  first_name  varchar(50)  not null,
  second_name varchar(50)  not null,
  password    varchar(500) not null,
  primary key (id)
);

create table offer
(
  id                  bigint(20)     not null auto_increment,
  offer_id            binary(16)     not null,
  customer_id         bigint(20)     not null,
  valid_until         datetime       not null,
  state               varchar(15)    not null,
  type                varchar(15)    not null,
  size                decimal(10, 2) not null,
  price               decimal(10, 2) not null,
  demand_change_since datetime       not null,
  demand_change_until datetime       not null,
  primary key (id),
  foreign key (customer_id) references customer (id)
);

CREATE TABLE `obligation`
(
  `id`              bigint(20)     NOT NULL AUTO_INCREMENT,
  `customer_id`     bigint(20)     NOT NULL,
  `state`           varchar(25)    NOT NULL,
  `size`            decimal(10, 2) NOT NULL,
  `percentage_kept` decimal(10, 2) NOT NULL,
  `offer_id`        bigint(20)     NOT NULL,
  PRIMARY KEY (`id`),
  foreign key (offer_id) references offer (id),
  foreign key (customer_id) references customer (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `customer_trust`
(
  `id`            bigint(20)     NOT NULL AUTO_INCREMENT,
  `customer_id`   bigint(20)     NOT NULL,
  `current_value` decimal(10, 2) NOT NULL,
  `kws_processed` decimal(10, 2) NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`),
  foreign key (customer_id) references customer (id)
);


create view obligation_view as
SELECT ob.*, of.demand_change_since as since, of.demand_change_until as until
FROM obligation ob
       join offer `of` on ob.offer_id = of.id;

insert into customer(id, first_name, second_name, password)
values (1, 'Anrold', 'Boczek', 'arnib@xd');
insert into customer(id, first_name, second_name, password)
values (2, 'Celina', 'Dudek', 'celdud@xd');
insert into customer(id, first_name, second_name, password)
values (3, 'Ewa', 'Farna', 'ewcia@xd');
insert into customer(id, first_name, second_name, password)
values (4, 'Grzegorz', 'Huśtawka', 'ghus@xd');
insert into customer(id, first_name, second_name, password)
values (5, 'Iga', 'Jaworska', 'ijaw@xd');
insert into customer(id, first_name, second_name, password)
values (6, 'Karol', 'Lunatyk', 'arnib@xd');
insert into customer(id, first_name, second_name, password)
values (7, 'Łukasz', 'Mariański', 'ł@xd');
insert into customer(id, first_name, second_name, password)
values (8, 'Natalia', 'Oparus', 'mnon@xd');
insert into customer(id, first_name, second_name, password)
values (9, 'Paweł', 'Ruchał', 'pruchacz@xd');
insert into customer(id, first_name, second_name, password)
values (10, 'Stefan', 'Trawka', 'szielnik@xd');

INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('1', '1', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('2', '2', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('3', '3', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('4', '4', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('5', '5', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('6', '6', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('7', '7', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('8', '8', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('9', '9', '0', '0');
INSERT INTO `wsd_dsm`.`customer_trust` (`id`, `customer_id`, `current_value`, `kws_processed`)
VALUES ('10', '10', '0', '0');
