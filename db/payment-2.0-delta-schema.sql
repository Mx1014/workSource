
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for eh_payment_accounts
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_accounts`;
CREATE TABLE `eh_payment_accounts` (
  `id` bigint(20) NOT NULL,
  `name` varchar(128) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  `system_id` int(11) DEFAULT NULL,
  `app_key` varchar(256) DEFAULT NULL,
  `secret_key` varchar(1024) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for eh_payment_order_records
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_order_records`;
CREATE TABLE `eh_payment_order_records` (
  `id` bigint(20) NOT NULL,
  `service_id` bigint(20) NOT NULL COMMENT '业务服务类型,eh_payment_service.id',
  `order_id` bigint(20) NOT NULL COMMENT '业务订单编号',
  `payment_order_id` bigint(20) NOT NULL COMMENT '支付系统支付单号',
  `create_time` datetime NOT NULL,
  `order_commit_url` varchar(1024) DEFAULT NULL COMMENT '支付接口',
  `order_commit_token` varchar(1024) DEFAULT NULL COMMENT '支付token',
  `order_commit_nonce` varchar(128) DEFAULT NULL COMMENT '支付随机数',
  `order_commit_timestamp` bigint(20) DEFAULT NULL COMMENT '支付时间戳',
  `pay_info` text COMMENT '微信公众号支付,扫码支付等支付信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_payment_order_id` (`payment_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for eh_payment_service_configs
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_service_configs`;
CREATE TABLE `eh_payment_service_configs` (
  `id` bigint(20) NOT NULL,
  `name` varchar(256) DEFAULT NULL COMMENT '服务名称',
  `type` varchar(128) NOT NULL COMMENT '服务类型,填parking/rentalOrder等',
  `namespace_id` int(11) DEFAULT NULL,
  `owner_type` varchar(256) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `resource_type` varchar(256) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `payment_split_rule_id` bigint(20) NOT NULL,
  `payment_user_type` int(11) NOT NULL COMMENT '1-普通会员,2-企业会员',
  `payment_user_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for eh_payment_types
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_types`;
CREATE TABLE `eh_payment_types` (
  `id` bigint(20) NOT NULL,
  `type` varchar(128) NOT NULL COMMENT '服务类型,填parking/rentalOrder等',
  `namespace_id` int(11) DEFAULT NULL,
  `owner_type` varchar(256) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `resource_type` varchar(256) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `payment_type` int(11) DEFAULT NULL COMMENT '支付方式',
  `payment_name` varchar(128) DEFAULT NULL COMMENT '支付方式名称',
  `payment_logo` varchar(512) DEFAULT NULL COMMENT '支付方式logo',
  `paymentParams` varchar(512) DEFAULT NULL COMMENT '支付方式附加信息',
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for eh_payment_users
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_users`;
CREATE TABLE `eh_payment_users` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(256) NOT NULL,
  `owner_id` bigint(20) NOT NULL,
  `payment_user_type` int(11) NOT NULL COMMENT '1-普通会员,2-企业会员',
  `payment_user_id` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_owner_type_owner_id` (`owner_type`,`owner_id`),
  UNIQUE KEY `i_payment_user_type_payment_user_id` (`payment_user_type`,`payment_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
