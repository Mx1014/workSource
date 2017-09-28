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
	`order_num` VARCHAR(255) DEFAULT NULL COMMENT '订单的编号',
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

DROP TABLE IF EXISTS `eh_asset_payment_order`;
CREATE TABLE `eh_asset_payment_order` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `payer_name` varchar(128) DEFAULT NULL,
  `payer_type` varchar(32) DEFAULT NULL,
  `uid` bigint(20) DEFAULT NULL,
  `contract_id` varchar(2048) DEFAULT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `client_app_name` varchar(128) DEFAULT NULL,
  `u_name` varchar(64) DEFAULT NULL,
  `family_id` bigint(20) DEFAULT NULL,
  `organization_name` varchar(128) DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `community_id` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  `phone` varchar(32) DEFAULT NULL,
  `real_name` varchar(128) DEFAULT NULL,
  `gender` tinyint(4) DEFAULT NULL COMMENT '0:female;1:male',
  `position` varchar(64) DEFAULT NULL,
  `email` varchar(128) DEFAULT NULL,
  `pay_flag` tinyint(4) DEFAULT '0' COMMENT '0: have not paid, 1:have paid',
  `order_no` bigint(20) DEFAULT NULL,
  `order_type` varchar(20) DEFAULT NULL,
  `order_start_time` datetime DEFAULT NULL,
  `order_expire_time` datetime DEFAULT NULL,
  `payment_type` varchar(32) DEFAULT NULL COMMENT '10001: alipay, 10002: wechatpay',
  `pay_amount` decimal(10,2) DEFAULT NULL,
  `paid_time` datetime DEFAULT NULL,
  `refund_order_no` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '0：新建；1：失败；2：支付成功但张江高科的全部失败；3：支付成功但张江高科的部分成功；4：支付成功张江高科的也全部成功;5：取消',
  `cancel_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for eh_asset_payment_order_bills
-- ----------------------------
DROP TABLE IF EXISTS `eh_asset_payment_order_bills`;
CREATE TABLE `eh_asset_payment_order_bills` (
  `id` bigint(20) NOT NULL,
  `bill_id` varchar(20) DEFAULT NULL,
  `bill_description` varchar(255) DEFAULT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `namespace_id` int(10) DEFAULT NULL,
  `status` int(11) DEFAULT 0 COMMENT '0:没有支付；1：支付成功；',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




ALTER TABLE `eh_var_field_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `community_id` BIGINT COMMENT '园区id';


-- 4.9.3的sql
-- by dengs, 20170925 服务联盟2.9
-- DROP TABLE IF EXISTS `eh_service_alliance_comments`;
CREATE TABLE `eh_service_alliance_comments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g servicealliance id',
	`parent_comment_id` BIGINT COMMENT 'parent comment Id',
	`content_type` VARCHAR(32) COMMENT 'object content type',
	`content` TEXT COMMENT 'content data, depends on value of content_type',
	`status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
	`create_time` DATETIME,
	`deleter_uid` BIGINT COMMENT 'deleter uid',
	`delete_time` DATETIME COMMENT 'delete time',

	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_service_alliance_comment_attachments`;
CREATE TABLE `eh_service_alliance_comment_attachments` (
	`id` BIGINT(20) NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
	`content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'attachment object link info on storage',
	`status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT NOT NULL,
	`create_time` DATETIME NOT NULL,
	`operator_uid` BIGINT,
	`update_time` DATETIME,

	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 服务联盟	添加概要描述
ALTER TABLE `eh_service_alliances` ADD COLUMN `summary_description` VARCHAR(1024) COMMENT '';
ALTER TABLE `eh_service_alliances` ADD COLUMN `enable_comment` TINYINT DEFAULT 0 COMMENT '1,enable;0,disable';
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `entry_id` INTEGER;
-- end by dengs, 20170925 服务联盟2.9

--
-- 短信日志 add by xq.tian  2017/08/23
--
ALTER TABLE `eh_sms_logs` ADD COLUMN `handler` VARCHAR(128) NOT NULL COMMENT 'YunZhiXun, YouXunTong, LianXinTong';
ALTER TABLE `eh_sms_logs` ADD COLUMN `sms_id` VARCHAR(128) COMMENT 'sms identifier';
ALTER TABLE `eh_sms_logs` ADD COLUMN `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: send success, 2: send failed, 4: report success, 5: report failed';
ALTER TABLE `eh_sms_logs` ADD COLUMN `report_text` TEXT COMMENT 'report text';
ALTER TABLE `eh_sms_logs` ADD COLUMN `report_time` DATETIME(3);

ALTER TABLE `eh_sms_logs` ADD INDEX i_eh_mobile_handler(`mobile`, `handler`);


-- 资源预约 add by sw 20170925
ALTER TABLE eh_rentalv2_orders ADD COLUMN paid_version tinyint(4) DEFAULT NULL;

-- 停车 add by sw 20170925
ALTER TABLE eh_parking_recharge_orders ADD COLUMN original_price DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN card_request_id BIGINT(20) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN invoice_type BIGINT(4) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN paid_version tinyint(4) DEFAULT NULL;

ALTER TABLE eh_parking_card_requests ADD COLUMN card_type_id VARCHAR(64) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN address_id BIGINT(20) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN invoice_type BIGINT(4) DEFAULT NULL;

ALTER TABLE eh_parking_lots CHANGE COLUMN `expired_recharge_json` recharge_json VARCHAR(1024) DEFAULT NULL;

ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip_flag TINYINT(4) NOT NULL DEFAULT '0' COMMENT '1: support , 0: not';
ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip TEXT;


CREATE TABLE `eh_parking_invoice_types` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parking_lot_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `invoice_token` VARCHAR(256) DEFAULT NULL,
  `name` VARCHAR(128) DEFAULT NULL,
  `status` TINYINT(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_user_invoices` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parking_lot_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `user_id` BIGINT(20) DEFAULT NULL,
  `invoice_type_id` BIGINT(20) DEFAULT NULL,
  `creator_uid` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_card_types` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parking_lot_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `card_type_id` VARCHAR(128) DEFAULT NULL,
  `card_type_name` VARCHAR(128) DEFAULT NULL,
  `status` TINYINT(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- merge from incubator-1.0 成都孵化器 start  by yanjun

-- 入孵申请表  add by yanjun 20170913
CREATE TABLE `eh_incubator_applies` (
`id`  BIGINT(22) NOT NULL ,
`uuid`  VARCHAR(128) NOT NULL DEFAULT '' ,
`namespace_id`  INT(11) NULL ,
`community_id`  BIGINT(22) NULL,
`apply_user_id` BIGINT(22)  NOT NULL,
`team_name`  VARCHAR(255) NULL ,
`project_type`  VARCHAR(255) NULL ,
`project_name`  VARCHAR(255) NULL ,
`business_licence_uri`  VARCHAR(255) NULL ,
`plan_book_uri`  VARCHAR(255) NULL ,
`charger_name`  VARCHAR(255) NULL ,
`charger_phone`  VARCHAR(18) NULL ,
`charger_email`  VARCHAR(255) NULL ,
`charger_wechat`  VARCHAR(255) NULL ,
`approve_user_id`  BIGINT(22) NULL ,
`approve_status`  TINYINT(4) NULL COMMENT '审批状态，0-待审批，1-拒绝，2-通过' ,
`approve_time`  DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
`approve_opinion`  VARCHAR(255) NULL ,
`create_time`  DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
`re_apply_id`  BIGINT(22) NULL COMMENT '重新申请的Id',
PRIMARY KEY (`id`)
);
-- 入孵申请项目类型 add by yanjun 20170913
CREATE TABLE `eh_incubator_project_types` (
`id`  BIGINT(22) NOT NULL ,
`uuid`  VARCHAR(128) NOT NULL DEFAULT '' ,
`name`  VARCHAR(255) NOT NULL ,
`create_time`  DATETIME NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
);

CREATE TABLE `eh_incubator_apply_attachments` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `incubator_apply_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'owner id, e.g incubator_apply_id',
  `type`  TINYINT(4) NULL COMMENT '类型，1-business_licence，2-plan_book',
  `name` VARCHAR(128) DEFAULT NULL,
  `file_size` INT(11) NOT NULL DEFAULT '0',
  `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `download_count` INT(11) NOT NULL DEFAULT '0',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- merge from incubator-1.0 成都孵化器 end by yanjun

-- 打卡
-- 增加考勤统计字段
ALTER TABLE eh_punch_statistics ADD COLUMN exception_day_count INT  DEFAULT NULL COMMENT '异常天数';




