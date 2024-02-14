CREATE TABLE `payment_account_references`
(
    `user_id`                bigint(20) NOT NULL,
    `bank_code`              varchar(10)  NOT NULL,
    `bank_name`              varchar(100) NULL,
    `account_no`             varchar(10)  NOT NULL,
    `account_name`           varchar(255) NOT NULL,
    `payment_recipient_code` varchar(255) NULL,
    `currency`               char(3) NULL,
    `created_by`             varchar(255) DEFAULT NULL,
    `date_created`           datetime     DEFAULT NULL,
    `date_modified`          datetime     DEFAULT NULL,
    `deleted`                varchar(1)   DEFAULT '0',
    `modified_by`            varchar(255) DEFAULT NULL,
    `payment_in_progress`            bit(1) NULL,
    PRIMARY KEY (`user_id`),
    CONSTRAINT FOREIGN KEY (`user_id`) REFERENCES `rentors` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
