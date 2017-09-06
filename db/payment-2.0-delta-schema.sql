DROP TABLE IF EXISTS `eh_payment_users`;
CREATE TABLE `eh_payment_users` (
	`id` BIGINT NOT NULL,
	`owner_type` VARCHAR(256) NOT NULL,
	`owner_id` BIGINT NOT NULL,
	`payment_user_type` INTEGER NOT NULL COMMENT '1-普通会员,2-企业会员',
	`payment_user_id` BIGINT NOT NULL,
	`create_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `i_owner_type_owner_id` (`owner_type`,`owner_id`),
	UNIQUE KEY `i_payment_user_type_payment_user_id` (`payment_user_type`,`payment_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_accounts`;
CREATE TABLE `eh_payment_accounts` (
	`id` BIGINT NOT NULL,
	`name` VARCHAR(128) DEFAULT NULL,
	`account_id` BIGINT DEFAULT NULL,
	`system_id` INTEGER DEFAULT NULL,
	`app_key` VARCHAR(256) DEFAULT NULL,
	`secret_key` VARCHAR(1024) DEFAULT NULL,
	`create_time` datetime NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_service_configs`;
CREATE TABLE `eh_payment_service_configs` (
	`id` BIGINT NOT NULL,
	`name` VARCHAR(256) DEFAULT NULL COMMENT '服务名称',
	`type` VARCHAR(128) NOT NULL COMMENT '服务类型,填parking/rentalOrder等',
	`namespace_id` INTEGER DEFAULT NULL,
	`owner_type` VARCHAR(256)DEFAULT NULL,
	`owner_id` BIGINT DEFAULT NULL,
	`resource_type` VARCHAR(256) DEFAULT NULL,
	`resource_id` BIGINT DEFAULT NULL,
	`payment_split_rule_id` BIGINT NOT NULL,
	`payment_user_type` INTEGER NOT NULL COMMENT '1-普通会员,2-企业会员',
	`payment_user_id` BIGINT NOT NULL,
	`create_time` datetime NOT NULL,
	`update_time` datetime DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_order_records`;
CREATE TABLE `eh_payment_order_records` (
	`id` BIGINT NOT NULL,
	`service_id` BIGINT NOT NULL COMMENT '业务服务类型,eh_payment_service.id',
	`order_id` BIGINT NOT NULL COMMENT '业务订单编号',
	`payment_order_id` BIGINT NOT NULL COMMENT '支付系统支付单号',
	`create_time` datetime NOT NULL,
	`order_commit_url` VARCHAR(1024) DEFAULT NULL COMMENT '支付接口',
	`order_commit_token` VARCHAR(1024) DEFAULT NULL COMMENT '支付token',
	`order_commit_nonce` VARCHAR(128) DEFAULT NULL COMMENT '支付随机数',
	`order_commit_timestamp` BIGINT DEFAULT NULL COMMENT '支付时间戳',
	`pay_info` TEXT COMMENT '微信公众号支付,扫码支付等支付信息',
	PRIMARY KEY (`id`),
	UNIQUE KEY `u_payment_order_id` (`payment_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_payment_types` (
	`id` BIGINT NOT NULL,
	`type` VARCHAR(128) NOT NULL COMMENT '服务类型,填parking/rentalOrder等',
	`namespace_id` INTEGER DEFAULT NULL,
	`owner_type` VARCHAR(256)DEFAULT NULL,
	`owner_id` BIGINT DEFAULT NULL,
	`resource_type` VARCHAR(256) DEFAULT NULL,
	`resource_id` BIGINT DEFAULT NULL,
	`payment_type` INTEGER DEFAULT NULL COMMENT '支付方式',
	`payment_name` VARCHAR(128) DEFAULT NULL COMMENT '支付方式名称',
	`payment_logo` VARCHAR(512) DEFAULT NULL COMMENT '支付方式logo',
	`paymentParams` VARCHAR(512) DEFAULT NULL COMMENT '支付方式附加信息',
	`create_time` datetime NOT NULL,
	`update_time` datetime DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;