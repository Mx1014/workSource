-- merge from 4.5.2 start
-- 消息会话推送参数设置（免打扰等）  add by xq.tian  2017/04/17
-- DROP TABLE IF EXISTS `eh_user_notification_settings`;
CREATE TABLE `eh_user_notification_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `app_id` BIGINT NOT NULL DEFAULT '1' COMMENT 'default to messaging app itself',
  `owner_type` VARCHAR(128) NOT NULL COMMENT 'e.g: EhUsers',
  `owner_id` BIGINT NOT NULL COMMENT 'owner type identify token',
  `target_type` VARCHAR(128) NOT NULL COMMENT 'e.g: EhUsers',
  `target_id` BIGINT NOT NULL COMMENT 'target type identify token',
  `mute_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: mute',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME(3) DEFAULT NULL,
  `update_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME(3) DEFAULT NULL,
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

-- merge from shenye201704 by xiongying20170509
ALTER TABLE eh_equipment_inspection_task_logs ADD COLUMN `inspection_category_id` BIGINT;
ALTER TABLE eh_equipment_inspection_task_logs ADD COLUMN `community_id` BIGINT;
ALTER TABLE eh_equipment_inspection_task_logs ADD COLUMN `namespace_id` INTEGER;

ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `create_time` DATETIME;
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `community_id` BIGINT;
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `standard_id` BIGINT;
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `equipment_id` BIGINT;
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `inspection_category_id` BIGINT;
ALTER TABLE eh_equipment_inspection_item_results ADD COLUMN `namespace_id` INTEGER;


-- merge form feedback-1.0
-- 举报管理增加“处理状态status”、“核实情况”、“处理方式”和“域空间”四个字段 add by yanjun 20170425
ALTER TABLE `eh_feedbacks` ADD COLUMN `status` TINYINT(4) DEFAULT '0' NULL COMMENT '0: does not handle, 1: have handled';
ALTER TABLE `eh_feedbacks` ADD COLUMN `verify_type` TINYINT(4) NULL COMMENT '0: verify false, 1: verify true';
ALTER TABLE `eh_feedbacks` ADD COLUMN `handle_type` TINYINT(4) NULL COMMENT '0: none, 1 delete';
ALTER TABLE `eh_feedbacks` ADD COLUMN `namespace_id` INT(11) NULL;
ALTER TABLE `eh_feedbacks` ADD COLUMN `handle_time` DATETIME NULL;

-- merge form feedback-1.0
-- 举报管理中有用到target_id进行查询 add by yanjun 20170427
ALTER TABLE `eh_feedbacks` ADD INDEX i_eh_feedbacks_target_id(`target_id`);

-- merge form feedback-1.0
-- 修改举报content_category的注释 add by yanjun 20170427
ALTER TABLE `eh_feedbacks` CHANGE `content_category` `content_category` BIGINT(20) DEFAULT '0' NOT NULL COMMENT '0-其它、1-产品bug、2-产品改进、3-版本问题;11-敏感信息、12-版权问题、13-暴力色情、14-诈骗和虚假信息、15-骚扰；16-谣言、17-恶意营销、18-诱导分享；19-政治';
-- merge from 4.5.2 start

-- merge from 4.5.4 start
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


-- merge from activity-3.0.0 20170524 start
-- 在eh_activity_categories增加一个实际id，现在要将入口数据添加到此表。不同的namespace会用重复   add by yanjun 20170523
ALTER TABLE `eh_activity_categories` ADD COLUMN `entry_id` BIGINT(20) NOT NULL COMMENT 'entry id, Differ from each other\n in the same namespace' AFTER `owner_id`;

-- merge from activity-3.0.0 20170524 end


-- 修改日志表中备注长度，add by tt, 20170526
ALTER TABLE `eh_express_order_logs`	CHANGE COLUMN `remark` `remark` TEXT;
-- merge from 4.5.4 end

-- merge from 4.5.5 start
-- merge from warehouse by xiongying20170527
-- 仓库表
CREATE TABLE `eh_warehouses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `warehouse_number` VARCHAR(32) DEFAULT '',
  `volume` DOUBLE NOT NULL DEFAULT 0,
  `location` VARCHAR(512) NOT NULL DEFAULT '',
  `manager` VARCHAR(64),
  `contact` VARCHAR(64),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: disable, 2: enable',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 物品表
CREATE TABLE `eh_warehouse_materials` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `material_number` VARCHAR(32) NOT NULL DEFAULT '',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_warehouse_material_categories',
  `category_path` VARCHAR(128) COMMENT 'path of eh_warehouse_material_categories',
  `brand` VARCHAR(128),
  `item_no` VARCHAR(64),
  `reference_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `unit_id` BIGINT COMMENT 'id of eh_warehouse_units',
  `specification_information` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--  物品分类表
CREATE TABLE `eh_warehouse_material_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `category_number` VARCHAR(32) DEFAULT '',
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `path` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 仓库库存表
CREATE TABLE `eh_warehouse_stocks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `amount` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 仓库库存日志表
CREATE TABLE `eh_warehouse_stock_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_warehouse_requests',
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_source` TINYINT NOT NULL DEFAULT 0 COMMENT '0: request, 1: manual input',
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `delivery_amount` BIGINT NOT NULL DEFAULT 0,
  `stock_amount` BIGINT NOT NULL DEFAULT 0 COMMENT 'rest amount after delivery',
  `request_uid` BIGINT,
  `delivery_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 申请表
CREATE TABLE `eh_warehouse_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_uid` BIGINT,
  `request_organization_id` BIGINT,
  `remark` VARCHAR(512),
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `delivery_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0:no, 1: yes',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 申请物品表
CREATE TABLE `eh_warehouse_request_materials` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_id` BIGINT NOT NULL COMMENT 'id of eh_warehouse_requests',
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_source` TINYINT NOT NULL DEFAULT 0 COMMENT '0: request, 1: manual input',
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `amount` BIGINT NOT NULL DEFAULT 0,
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_uid` BIGINT,
  `review_time` DATETIME,
  `delivery_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0:no, 1: yes',
  `delivery_uid` BIGINT,
  `delivery_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 单位表
CREATE TABLE `eh_warehouse_units` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL,
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `deletor_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


ALTER TABLE eh_import_file_tasks MODIFY COLUMN result LONGTEXT;


ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag5` VARCHAR(128);


ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `org_member_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部工作日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `org_member_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部节假日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `approving_user_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户工作日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `approving_user_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户节假日价格';

ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `org_member_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部工作日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `org_member_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部节假日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `approving_user_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户工作日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `approving_user_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户节假日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `default_order` BIGINT NOT NULL DEFAULT 0 COMMENT 'order';

ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `org_member_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价-如果打折则有(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `org_member_price` DECIMAL(10,2) DEFAULT NULL COMMENT '实际价格-打折则为折后价(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `approving_user_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价-如果打折则有（外部客户价）';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `approving_user_price` DECIMAL(10,2) DEFAULT NULL COMMENT '实际价格-打折则为折后价（外部客户价）';

ALTER TABLE `eh_rentalv2_orders`  ADD COLUMN `flow_case_id` BIGINT DEFAULT NULL COMMENT 'id of the flow_case';

ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_org_member_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-原价-如果打折则有(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_org_member_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-实际价格-打折则为折后价(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_approving_user_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-原价-如果打折则有（外部客户价）';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_approving_user_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-实际价格-打折则为折后价（外部客户价）';
-- merge from 4.5.5 end


-- merge from 4.6.0 start
-- 给工作流评价项增加允许输入评价内容flag字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_evaluate_items` ADD COLUMN `input_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, input evaluate content flag';

-- 给工作流评价增加评价内容字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_evaluates` ADD COLUMN `content` VARCHAR(1024) DEFAULT NULL COMMENT 'evaluate content';

-- 给工作流按钮增加文字及图片是否必填字段  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_buttons` ADD COLUMN `subject_required_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, subject required flag';

-- 给flowCase增加公司字段，以便在普通公司下按照公司进行搜索  add by xq.tian  2017/05/12
ALTER TABLE `eh_flow_cases` ADD COLUMN `organization_id` BIGINT DEFAULT NULL COMMENT 'the same as eh_flows organization_id';

-- eh_configurations表value字段加长
ALTER TABLE `eh_configurations` MODIFY COLUMN `value` VARCHAR(512) NOT NULL;

-- 增加菜单配置的唯一约束 add by sfyan 20170609
ALTER TABLE eh_web_menu_scopes ADD UNIQUE KEY u_menu_scope_owner(menu_id, owner_type, owner_id);

-- merge from 4.6.0 end