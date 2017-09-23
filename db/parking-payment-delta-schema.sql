
ALTER TABLE eh_parking_recharge_orders ADD COLUMN original_price decimal(10,2) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN card_request_id bigint(20) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN invoice_type bigint(4) DEFAULT NULL;

ALTER TABLE eh_parking_card_requests ADD COLUMN card_type_id VARCHAR(64) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN address_id bigint(20) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN invoice_type bigint(4) DEFAULT NULL;

ALTER TABLE eh_parking_lots CHANGE COLUMN `expired_recharge_json` recharge_json varchar(1024) DEFAULT NULL;

ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip_flag tinyint(4) NOT NULL DEFAULT '0' COMMENT '1: support , 0: not';
ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip text;


CREATE TABLE `eh_parking_invoice_types` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `invoice_token` varchar(256) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_user_invoices` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `user_id` bigint(20) DEFAULT NULL,
  `invoice_type_id` bigint(20) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_card_types` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `card_type_id` varchar(128) DEFAULT NULL,
  `card_type_name` varchar(128) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 增加域空间左上角显示场景名称的配置项
ALTER TABLE eh_namespace_details ADD COLUMN name_type tinyint(4) DEFAULT 0;

-- fix 15631 & 15636 add by xiongying20170919
ALTER TABLE eh_organizations ADD COLUMN website VARCHAR(256);
ALTER TABLE eh_organizations ADD COLUMN unified_social_credit_code VARCHAR(256);

-- 同步数据时为企业添加门牌时太慢，添加索引 add by xiongying20170922
ALTER TABLE `eh_addresses` ADD INDEX namespace_address ( `namespace_address_type`, `namespace_address_token`);

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