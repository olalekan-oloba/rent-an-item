CREATE TABLE `zones`
(
    `id`               int(11) NOT NULL AUTO_INCREMENT,
    `name`             varchar(100) NOT NULL,
    `zone_code`        varchar(255) NOT NULL,
    `zone_supported`   bit(1)       NOT NULL,
    `country_id`       int(11) DEFAULT NULL,
    `country_iso_code` char(3)      NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
