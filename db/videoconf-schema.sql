CREATE TABLE `eh_videoconfaccount_rule` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `account_type` TINYINT COMMENT '0-single 1-multiple',
  `conf_tpye` TINYINT COMMENT '0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话',
  `minimum_months` INT,
  `package_price` DOUBLE,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_warning_contacts` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `contactor` VARCHAR(20),
  `mobile` VARCHAR(20),
  `email` VARCHAR(20),
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_source_account` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `source_account` VARCHAR(20),
  `password` VARCHAR(20),
  `conf_tpye` TINYINT COMMENT '0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话',
  `valid_date` DATETIME,
  `valid_flag` TINYINT COMMENT '0-invalid 1-valid',
  `occupy_flag` TINYINT COMMENT '0-available 1-occupied',
  `occupy_account_id` BIGINT,
  `conf_id` INT,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_account_vedioconf` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `account_id` BIGINT,
  `source _account_id` BIGINT,
  `conf_id` INT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;