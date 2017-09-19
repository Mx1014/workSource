-- 用途:请求左邻支付系统接口,需要使用到如下4个字段
-- 支付分配的账号信息,accountId/systemId/appKey/secretKey
-- system_id园区系统填1,电商系统填2
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

-- 用途:为每个owner生成一个唯一的id,作为暴露给支付系统的用户id,用户创建会员
-- ownerType为普通用户/企业/商家等,ownerId填对应的owner编号
-- id为支付系统的会员bizUserId,paymentUserId为支付系统User表的id
DROP TABLE IF EXISTS `eh_payment_users`;
CREATE TABLE `eh_payment_users` (
	`id` BIGINT NOT NULL,
	`owner_type` VARCHAR(64) NOT NULL,
	`owner_id` BIGINT NOT NULL,
	`payment_user_type` INTEGER NOT NULL COMMENT '1-普通会员,2-企业会员',
	`payment_user_id` BIGINT NOT NULL,
	`create_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `i_owner` (`owner_type`,`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用途:根据业务需求,设置不同分账规则
-- 将id传给支付系统的createOrder接口的orderRemark1参数,后续根据业务需求查账单数据用到
-- 按namespaceId/owner/resource三个维度,满足不同业务需求设置不同分账规则
DROP TABLE IF EXISTS `eh_payment_service_configs`;
CREATE TABLE `eh_payment_service_configs` (
	`id` BIGINT NOT NULL,
	`name` VARCHAR(256) DEFAULT NULL COMMENT '服务名称',
	`order_type` VARCHAR(64) NOT NULL COMMENT '服务类型,填parking/rentalOrder等',
	`namespace_id` INTEGER DEFAULT NULL,
	`owner_type` VARCHAR(64)DEFAULT NULL,
	`owner_id` BIGINT DEFAULT NULL,
	`resource_type` VARCHAR(64) DEFAULT NULL,
	`resource_id` BIGINT DEFAULT NULL,
	`payment_split_rule_id` BIGINT NOT NULL,
	`payment_user_type` INTEGER NOT NULL COMMENT '1-普通会员,2-企业会员',
	`payment_user_id` BIGINT NOT NULL,
	`create_time` datetime NOT NULL,
	`update_time` datetime DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用途:记录业务订单/业务类型/支付单关联关系,便于支付成功回调接口通过支付单号找到业务订单及后续特定业务订单处理
-- 记录这4个参数值,用于重新支付:order_commit_url,order_commit_token,order_commit_nonce,order_commit_timestamp
-- 记录微信公众号支付,扫码支付等支付信息: pay_info
DROP TABLE IF EXISTS `eh_payment_order_records`;
CREATE TABLE `eh_payment_order_records` (
	`id` BIGINT NOT NULL,
	`service_config_id` BIGINT NOT NULL COMMENT '业务服务类型,eh_payment_service.id',
	`payment_order_type` INTEGER NOT NULL COMMENT '支付系统单据类型,1-充值,2-提现,3-支付,4-退款',
	`order_type` VARCHAR(64) NOT NULL COMMENT '服务类型,填parking/rentalOrder等',
	`order_id` BIGINT NOT NULL COMMENT '业务订单编号',
	`payment_order_id` BIGINT NOT NULL COMMENT '支付系统支付单号',
	`order_commit_url` VARCHAR(1024) DEFAULT NULL COMMENT '支付接口',
	`order_commit_token` VARCHAR(1024) DEFAULT NULL COMMENT '支付token',
	`order_commit_nonce` VARCHAR(128) DEFAULT NULL COMMENT '支付随机数',
	`order_commit_timestamp` BIGINT DEFAULT NULL COMMENT '支付时间戳',
	`pay_info` TEXT COMMENT '微信公众号支付,扫码支付等支付信息',
	`create_time` datetime NOT NULL,
	PRIMARY KEY (`id`),
	UNIQUE KEY `u_payment_order_id` (`payment_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_types`;
CREATE TABLE `eh_payment_types` (
	`id` BIGINT NOT NULL,
	`order_type` VARCHAR(64) DEFAULT NULL COMMENT '服务类型,填parking/rentalOrder等',
	`namespace_id` INTEGER DEFAULT NULL,
	`owner_type` VARCHAR(64)DEFAULT NULL,
	`owner_id` BIGINT DEFAULT NULL,
	`resource_type` VARCHAR(64) DEFAULT NULL,
	`resource_id` BIGINT DEFAULT NULL,
	`payment_type` INTEGER DEFAULT NULL COMMENT '支付方式',
	`payment_name` VARCHAR(128) DEFAULT NULL COMMENT '支付方式名称',
	`payment_logo` VARCHAR(512) DEFAULT NULL COMMENT '支付方式logo',
	`paymentParams` VARCHAR(512) DEFAULT NULL COMMENT '支付方式附加信息',
	`create_time` datetime NOT NULL,
	`update_time` datetime DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 活动报名表，添加支付版本，用于退款 add by yanjun 20170919
ALTER TABLE `eh_activity_roster`ADD COLUMN `pay_version`  tinyint(4) NULL COMMENT '支付版本，用于退款';