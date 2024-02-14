

CREATE TABLE `core_configurations`
(
    `id`                       bigint(20) NOT NULL AUTO_INCREMENT,
    `created_by`               varchar(255) NOT NULL,
    `date_created`             datetime     NOT NULL,
    `date_modified`            datetime     DEFAULT NULL,
    `deleted`                  varchar(1)   DEFAULT '0',
    `modified_by`              varchar(255) DEFAULT NULL,
    `configuration_group`      varchar(30)  DEFAULT NULL,
    `configuration_key`        varchar(50)  NOT NULL,
    `configuration_name`       varchar(100) NOT NULL,
    `description`              varchar(255) DEFAULT NULL,
    `sort_order`               int(11) NOT NULL,
    `configration_type` varchar(255) DEFAULT NULL,
    `value`                    varchar(255) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
