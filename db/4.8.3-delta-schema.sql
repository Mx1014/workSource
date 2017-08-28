-- 快递公司对应业务表，add by dengs, 20170718
-- DROP TABLE IF EXISTS `eh_express_company_businesses`;
CREATE TABLE `eh_express_company_businesses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '目前是EhNamespaces',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT '域空间',
  `express_company_id` BIGINT COMMENT 'id of the express company id,是parent_id = 0 的快递公司的id',
  `send_type` TINYINT COMMENT '业务类型id',
  `send_type_name` VARCHAR(128) COMMENT '业务类型名称(/（华润）标准快递/（国贸）EMS标准快递/（国贸）邮政快递包裹/（国贸）同城信筒快件)',
  `package_types` TEXT COMMENT '封装类型，参考 ExpressPackageType.class,json数组',
  `insured_documents` VARCHAR(1024) COMMENT '保价文案，目前只有国贸ems和国贸邮政的邮政快递包裹有保价文案，所以跟着业务走',
  `order_status_collections` TEXT COMMENT '订单状态集合,json数组 参考 ExpressOrderStatus.class',
  `pay_type` TINYINT COMMENT '支付方式， 参考,ExpressPayType.class,寄付现结=1,线下支付=2',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递服务热线表，add by dengs, 20170718
-- DROP TABLE IF EXISTS `eh_express_hotlines`;
CREATE TABLE `eh_express_hotlines` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `service_name` VARCHAR(512) COMMENT '服务热线的服务名称',
  `hotline` VARCHAR(128) COMMENT '热线电话',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 快递设置表，add by dengs, 20170718
-- DROP TABLE IF EXISTS `eh_express_param_settings`;
CREATE TABLE `eh_express_param_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community or namespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community or namespace id',
  `express_user_setting_show_flag` TINYINT DEFAULT 0  COMMENT '快递员设置 是否在后台管理 显示标志',
  `business_note_setting_show_flag` TINYINT DEFAULT 1  COMMENT '业务说明 是否在后台管理 显示标志',
  `hotline_setting_show_flag` TINYINT DEFAULT 1  COMMENT '客服热线 是否在后台管理 显示标志',
  `hotline_flag` TINYINT DEFAULT 0  COMMENT '热线是否在app显示标志，可在后台修改',
  `business_note` TEXT DEFAULT NULL  COMMENT '业务说明',
  `business_note_flag` TINYINT DEFAULT 0 COMMENT '业务说明是否在app显示标志，可在后台修改',
  `send_mode` TINYINT DEFAULT 1 COMMENT '1,服务点自寄 2，快递员上门收件',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_express_companies` ADD COLUMN `description` VARCHAR(512) COMMENT '快递公司描述信息，比如华润ems和国贸ems需要区别一下，描述给后来的人看懂' AFTER `logo`;
ALTER TABLE `eh_express_companies` ADD COLUMN `authorization` VARCHAR(512) COMMENT '授权码' AFTER `description`;
ALTER TABLE `eh_express_companies` ADD COLUMN `app_secret` VARCHAR(512) COMMENT '' AFTER `description`;
ALTER TABLE `eh_express_companies` ADD COLUMN `app_key` VARCHAR(512) COMMENT '' AFTER `description`;
ALTER TABLE `eh_express_companies` ADD COLUMN `order_url` VARCHAR(2048) COMMENT '快递公司订单服务器地址' AFTER `description`;
ALTER TABLE `eh_express_companies` ADD COLUMN `logistics_url` VARCHAR(2048) COMMENT '快递公司物流服务器地址' AFTER `description`;

ALTER TABLE `eh_express_orders`	ADD COLUMN `package_type` TINYINT COMMENT '封装类型，参考 ExpressPackageType.class' AFTER `internal`;
ALTER TABLE `eh_express_orders`	ADD COLUMN `invoice_flag` TINYINT COMMENT '需要发票选项,0:不需要 1：需要手撕发票 2：需要税票 ExpressInvoiceFlagType.class' AFTER `internal` ;
ALTER TABLE `eh_express_orders`	ADD COLUMN `invoice_head` VARCHAR(512) COMMENT '税票的发票抬头' AFTER `internal`;
-- 这里华润需要处理老数据，赋值为 send_type_name =  标准快递
ALTER TABLE `eh_express_orders`	ADD COLUMN `send_type_name` VARCHAR(128) COMMENT '业务类型名称(/（华润）标准快递/（国贸）EMS标准快递/（国贸）邮政快递包裹/（国贸）同城信筒快件)' AFTER `send_type`;
ALTER TABLE `eh_express_orders`	ADD COLUMN `quantity_and_weight` VARCHAR(128) COMMENT '数量和重量' AFTER `send_type_name`;

--
ALTER TABLE `eh_express_orders` ADD COLUMN `status_desc` TEXT COMMENT '状态描述信息，国贸ems使用' AFTER `status`;

-- end by dengs,2017.08.28 快递

-- 停车 add by sw 20170828
ALTER TABLE eh_parking_lots DROP COLUMN `lock_car_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `contact`;
ALTER TABLE eh_parking_lots DROP COLUMN `tempfee_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `rate_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `max_request_num`;

ALTER TABLE eh_parking_lots DROP COLUMN `card_reserve_days`;
ALTER TABLE eh_parking_lots DROP COLUMN `recharge_month_count`;
ALTER TABLE eh_parking_lots DROP COLUMN `recharge_type`;
ALTER TABLE eh_parking_lots DROP COLUMN `is_support_recharge`;

ALTER TABLE eh_parking_lots ADD COLUMN `expired_recharge_json` VARCHAR(1024) DEFAULT NULL;
ALTER TABLE eh_parking_lots ADD COLUMN `config_json` VARCHAR(1024) DEFAULT NULL;

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `order_type` tinyint(4) DEFAULT 1;

-- 临时增加功能：可以投票增加"重复投票"选项和"投票间隔时间"   add by yanjun 20170825
ALTER TABLE `eh_poll_votes` DROP INDEX `i_eh_poll_vote_voter` , ADD INDEX `i_eh_poll_vote_voter` (`poll_id`, `item_id`, `voter_uid`) USING BTREE ;
ALTER TABLE `eh_polls` ADD COLUMN `repeat_flag`  tinyint(4) NULL COMMENT 'is support repeat poll. 0-no, 1-yes', ADD COLUMN `repeat_period`  int(11) NULL COMMENT 'repeat_period,  day';