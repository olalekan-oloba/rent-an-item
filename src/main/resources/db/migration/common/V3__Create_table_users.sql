CREATE TABLE `users`
(
    `id`                        bigint(20) NOT NULL AUTO_INCREMENT,
    `created_by`                varchar(255) DEFAULT NULL,
    `date_created`              datetime     DEFAULT NULL,
    `date_modified`             datetime     DEFAULT NULL,
    `deleted`                   varchar(1)   DEFAULT '0',
    `modified_by`               varchar(255) DEFAULT NULL,
    `first_name`                varchar(100) NOT NULL,
    `last_name`                 varchar(100) NOT NULL,
    `email`                     varchar(100) NOT NULL,
    `password`                  varchar(64)  DEFAULT NULL,
    `password_reset_token`      varchar(100) DEFAULT NULL,
    `password_reset_vldty_term` datetime     DEFAULT NULL,
    `last_login`                datetime     DEFAULT NULL,
    `status_date`               date         DEFAULT NULL,
    `email_verified`            bit(1) NULL,
    `status`                    varchar(255) NOT NULL,
    `is_rentor`                bit(1) NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
