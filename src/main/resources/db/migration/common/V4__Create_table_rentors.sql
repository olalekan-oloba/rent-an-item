CREATE TABLE `rentors`
(
    `id`                bigint(20) NOT NULL,
    `gender`            varchar(10)  NOT NULL,
    `phone`             varchar(100) NOT NULL,
    `contact_address`   varchar(255) NOT NULL,
    `customer_agreement` bit(1) NOT NULL,

    PRIMARY KEY (`id`),
    CONSTRAINT FOREIGN KEY (`id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
