 
-- 自寄服务地址表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_service_addresses`;
CREATE TABLE `eh_express_service_addresses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `name` VARCHAR(128) COMMENT 'the name of express service address',
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递公司表，左邻配一套全局的，各园区在此选择，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_companies`;
CREATE TABLE `eh_express_companies` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parent id, the id of express company under zuolin',
  `name` VARCHAR(128) COMMENT 'the name of express company name',
  `logo` VARCHAR(512),
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递员表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_users`;
CREATE TABLE `eh_express_users` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `service_address_id` BIGINT,
  `express_company_id` BIGINT,
  `organization_id` BIGINT COMMENT 'the id of organization',
  `organization_member_id` BIGINT COMMENT 'the id of organization member',
  `user_id` BIGINT,
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递地址表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_addresses`;
CREATE TABLE `eh_express_addresses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `user_name` VARCHAR(128),
  `organization_id` BIGINT,
  `organization_name` VARCHAR(128),
  `phone` VARCHAR(16),
  `province_id` BIGINT,
  `city_id` BIGINT,
  `county_id` BIGINT,
  `province` VARCHAR(64),
  `city` VARCHAR(64),
  `county` VARCHAR(64),
  `detail_address` VARCHAR(512),
  `default_flag` TINYINT COMMENT '0. false, 1 true',
  `category` TINYINT COMMENT '1. send address, 2. receive address',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递订单表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_orders`;
CREATE TABLE `eh_express_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `order_no` VARCHAR(64) COMMENT 'order number',
  `bill_no` VARCHAR(64) COMMENT 'bill number',
  `send_name` VARCHAR(128),
  `send_phone` VARCHAR(16),
  `send_organization` VARCHAR(128),
  `send_province` VARCHAR(64),
  `send_city` VARCHAR(64),
  `send_county` VARCHAR(64),
  `send_detail_address` VARCHAR(512),
  `receive_name` VARCHAR(128),
  `receive_phone` VARCHAR(16),
  `receive_organization` VARCHAR(128),
  `receive_province` VARCHAR(64),
  `receive_city` VARCHAR(64),
  `receive_county` VARCHAR(64),
  `receive_detail_address` VARCHAR(512),
  `service_address_id` BIGINT COMMENT 'service address id',
  `express_company_id` BIGINT COMMENT 'express company id',
  `send_type` TINYINT COMMENT '1. standard express',
  `send_mode` TINYINT COMMENT '1. self send',
  `pay_type` TINYINT COMMENT '1. cash',
  `pay_summary` DECIMAL(10,2) COMMENT 'pay money',
  `internal` VARCHAR(256) COMMENT 'internal things',
  `insured_price` DECIMAL(10,2) COMMENT 'insured price',
  `status` TINYINT NOT NULL COMMENT '1. waiting for pay, 2. paid, 3. printed, 4. cancelled',
  `paid_flag` TINYINT COMMENT 'whether the user has pushed the pay button, 0. false, 1 true',
  `print_time` DATETIME,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`),
  UNIQUE INDEX `order_no` (`order_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递查询历史表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_query_histories`;
CREATE TABLE `eh_express_query_histories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `express_company_id` BIGINT COMMENT 'express company id',
  `bill_no` VARCHAR(64) COMMENT 'bill number',
  `status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递订单日志表，add by tt, 20170413
-- DROP TABLE IF EXISTS `eh_express_order_logs`;
CREATE TABLE `eh_express_order_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `order_id` BIGINT,
  `action` VARCHAR(64),
  `remark` VARCHAR(512),
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- add by dengs 20170511 添加服务联盟描述信息折叠高度
ALTER TABLE `eh_service_alliances` ADD COLUMN `description_height` INT NULL DEFAULT '2' COMMENT '0:not collapse , N: collapse N lines';


-- merge from activity-3.0.0 20170522  start  
-- 活动增加支付相关字段 add by yanjun 20170502
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_flag` TINYINT(4) DEFAULT '0' NULL COMMENT '0: no pay, 1:have pay, 2:refund';
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_no` BIGINT(20) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_start_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_expire_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `vendor_type` VARCHAR(32) NULL COMMENT '10001: alipay, 10002: wechatpay';
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_amount` DECIMAL(10, 2) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `pay_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_order_no` BIGINT(20) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_amount` DECIMAL(10, 2) NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `refund_time` DATETIME NULL;
ALTER TABLE `eh_activity_roster` ADD COLUMN `status` TINYINT(4) DEFAULT '2' NULL COMMENT '0: cancel, 1: reject, 2:normal';
ALTER TABLE `eh_activity_roster` ADD COLUMN `organization_id` BIGINT(20) NULL;

ALTER TABLE `eh_activities` ADD COLUMN `charge_flag` TINYINT(4) DEFAULT '0' NULL COMMENT '0: no charge, 1: charge';
ALTER TABLE `eh_activities` ADD COLUMN `charge_price` DECIMAL(10, 2) NULL COMMENT 'charge_price';


-- 订单过期时间的设置表  add by yanjun 20170502
CREATE TABLE `eh_roster_order_settings` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) NOT NULL COMMENT 'namespace id',
  `time` BIGINT(20) DEFAULT NULL COMMENT 'millisecond',
  `create_time` DATETIME DEFAULT NULL,
  `creator_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加取消时间，用于统计  add by yanjun 20170519
ALTER TABLE `eh_activity_roster` ADD COLUMN `cancel_time` DATETIME NULL;

-- merge from activity-3.0.0 20170522  end

-- 给工作流评价项增加允许输入评价内容flag字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_evaluate_items` ADD COLUMN `input_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, input evaluate content flag';

-- 给工作流评价增加评价内容字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_evaluates` ADD COLUMN `content` VARCHAR(1024) DEFAULT NULL COMMENT 'evaluate content';

-- 给工作流按钮增加文字及图片是否必填字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_buttons` ADD COLUMN `subject_required_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, subject required flag';

-- 给flowCase增加公司字段，以便在普通公司下按照公司进行搜索  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_cases` ADD COLUMN `organization_id` BIGINT DEFAULT NULL COMMENT 'the same as eh_flows organization_id';
