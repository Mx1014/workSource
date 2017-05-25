-- 会话推送参数设置  add by xq.tian  2017/04/17
-- DROP TABLE IF EXISTS `eh_user_notification_settings`;
CREATE TABLE `eh_user_notification_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_id` BIGINT NOT NULL DEFAULT '1' COMMENT 'default to messaging app itself',
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'e.g: EhUsers',
  `owner_id` BIGINT NOT NULL COMMENT 'owner type identify token',
  `target_type` VARCHAR(32) NOT NULL COMMENT 'e.g: EhUsers',
  `target_id` BIGINT NOT NULL COMMENT 'target type identify token',
  `mute_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: mute',
  `creator_uid` BIGINT DEFAULT NULL COMMENT 'creator uid',
  `create_time` DATETIME(3) DEFAULT NULL COMMENT 'message creation time',
  `update_uid` BIGINT DEFAULT NULL COMMENT 'update uid',
  `update_time` DATETIME(3) DEFAULT NULL COMMENT 'message creation time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 用于记录groupMember表记录的删除还是拒绝的状态
-- DROP TABLE IF EXISTS `eh_group_member_logs`;
CREATE TABLE `eh_group_member_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `group_member_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'the same as group member status',
  `creator_uid` BIGINT,
  `process_message` TEXT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

 ALTER TABLE `eh_punch_time_rules` ADD COLUMN `description` VARCHAR(128)  COMMENT 'rule description';
 ALTER TABLE `eh_punch_time_rules` ADD COLUMN `target_type` VARCHAR(128)  COMMENT 'target resource(user/organization) type';
 ALTER TABLE `eh_punch_time_rules` ADD COLUMN `target_id` BIGINT  COMMENT 'target resource(user/organization) id';
 
 --  打卡排班表
 CREATE TABLE `eh_punch_schedulings` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128)  COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT  COMMENT 'owner resource(user/organization) id',
  `target_type` VARCHAR(128)  COMMENT 'target resource(user/organization) type',
  `target_id` BIGINT  COMMENT 'target resource(user/organization) id', 
  `rule_date` DATE  COMMENT 'date',
  `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT COMMENT 'eh_punch_time_rules id --null:not work day',
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME , 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4
;
-- 考勤增加字段
ALTER TABLE `eh_punch_statistics` ADD COLUMN `exts` VARCHAR(1024) COMMENT 'json string exts:eq[{"name":"事假","timeCount":"1天2小时"},{"name":"丧假","timeCount":"3天2小时30分钟"}]';
ALTER TABLE `eh_punch_statistics` ADD COLUMN `user_status` TINYINT DEFAULT 0 COMMENT '0:normal普通 1:NONENTRY未入职 2:RESIGNED已离职';

ALTER TABLE `eh_punch_time_rules` ADD COLUMN `start_early_time_long` BIGINT  COMMENT 'how early can i arrive';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `start_late_time_long` BIGINT  COMMENT 'how late can i arrive ';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `work_time_long` BIGINT  COMMENT 'how long do i must be work';
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `noon_leave_time_long` BIGINT ;
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `afternoon_arrive_time_long` BIGINT ;
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `day_split_time_long` BIGINT DEFAULT 18000000 COMMENT 'the time a day begin'  ;

-- 请假申请增加统计表,用以每月的考勤统计
-- 这个表在申请被审批的时候修改.
CREATE TABLE `eh_approval_range_statistics` (
  `id` BIGINT NOT NULL,
  `punch_month` VARCHAR(8) DEFAULT NULL COMMENT 'yyyymm',
  `owner_type` VARCHAR(128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user id',   
  `approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
  `category_id` BIGINT DEFAULT NULL COMMENT 'concrete category id',
  `actual_result` VARCHAR(128) DEFAULT NULL COMMENT 'actual result, e.g 1day3hours 1.3.0',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 ;


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
