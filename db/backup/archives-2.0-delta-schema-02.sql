-- by dengs,问卷调查添加属性。2017.11.06
ALTER TABLE `eh_questionnaires` ADD COLUMN `organization_scope` TEXT COMMENT 'targetType是organization的时候，发布公司的列表' AFTER `user_scope`;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN user_scope MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN scope_sent_message_users MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN scope_resent_message_users MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN organization_scope MEDIUMTEXT;

-- 用户管理1.4 add by yanjun 201711071007
ALTER TABLE `eh_user_organizations` ADD COLUMN `executive_tag`  tinyint(4) NULL, ADD COLUMN `position_tag`  varchar(128) NULL;

-- flow 加校验状态字段   add by xq.tian  2017/10/31
ALTER TABLE ehcore.eh_flows ADD COLUMN `validation_status` TINYINT NOT NULL DEFAULT 2 COMMENT 'flow validation status';

-- 停车6.1 add by sw 20171108
CREATE TABLE `eh_parking_car_verifications` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `requestor_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the requestor is in',
  `requestor_enterprise_name` varchar(64) DEFAULT NULL COMMENT 'the enterprise name of plate owner',
  `requestor_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'requestor id',
  `plate_number` varchar(64) DEFAULT NULL,
  `plate_owner_name` varchar(64) DEFAULT NULL COMMENT 'the name of plate owner',
  `plate_owner_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of plate owner',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: queueing, 2: notified, 3: issued',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `flow_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'flow case id',
  `source_type` tinyint(4) DEFAULT NULL COMMENT '1: card request, 2: car verify',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- by dengs,园区快讯多入口，2017.11.13
ALTER TABLE eh_news_categories ADD COLUMN `entry_id` INTEGER;

-- by wentian 物业缴费模块2.0
-- payment_wentian_v2 new sql after 4.10.4

ALTER TABLE `eh_payment_charging_standards_scopes` ADD COLUMN `namespace_id` INTEGER DEFAULT 0;
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `bill_group_rule_id` BIGINT DEFAULT NULL;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `bill_group_rule_id` BIGINT DEFAULT NULL;
ALTER TABLE `eh_payment_charging_standards_scopes` ADD COLUMN  `brother_standard_id` BIGINT DEFAULT NULL COMMENT '兄弟收费标准id，联动效果';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN  `brother_group_id` BIGINT DEFAULT NULL COMMENT '兄弟账单组id，联动效果';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是';
ALTER TABLE `eh_payment_bills` ADD COLUMN `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是';
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是';
DROP TABLE IF EXISTS `eh_payment_notice_config`;
CREATE TABLE `eh_payment_notice_config` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL,
  `owner_type` VARCHAR(255) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL,
  `notice_day_before` INTEGER DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 能耗3.0的表结构 by 熊颖
ALTER TABLE eh_energy_meters ADD COLUMN `cost_formula_source` TINYINT DEFAULT '0' COMMENT '0: 能耗设置, 1: 缴费模块';
ALTER TABLE eh_energy_meter_setting_logs ADD COLUMN `formula_source` TINYINT DEFAULT '0' COMMENT '0: 能耗设置, 1: 缴费模块';
ALTER TABLE eh_energy_meter_reading_logs ADD COLUMN `task_id` BIGINT DEFAULT '0';

-- 表计关联门牌
CREATE TABLE `eh_energy_meter_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `meter_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_groups',
  `building_id` BIGINT NOT NULL DEFAULT '0',
  `building_name` VARCHAR(128) DEFAULT NULL,
  `address_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_addresses',
  `apartment_name` VARCHAR(128) DEFAULT NULL,
  `apartment_floor` VARCHAR(16) DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT DEFAULT NULL COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'redundant auditing info',
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `meter_address_meter_id` (`meter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 能耗抄表计划
CREATE TABLE `eh_energy_plans` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the plan, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the plan, community, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the plan',
  `name` VARCHAR(1024),
  `repeat_setting_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_repeat_settings',
  `notify_tick_minutes` INTEGER COMMENT '提前多少分钟',
  `notify_tick_unit` TINYINT COMMENT '提醒时间显示单位',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: not completed, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_plan_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `plan_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_plans',
  `group_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_plan_meter_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `plan_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_plans',
  `meter_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_meters',
  `default_order` INTEGER DEFAULT 0,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 按计划生成工单
CREATE TABLE `eh_energy_meter_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, community, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `plan_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_plans',
  `meter_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_meters',
  `executive_start_time` DATETIME,
  `executive_expire_time` DATETIME,
  `last_task_reading` DECIMAL(10,1),
  `reading` DECIMAL(10,1),
  `generate_payment_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未生成, 1: 已生成',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未抄, 1: 已抄',
  `default_order` INTEGER DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  KEY `status` (`status`),
  KEY `target_id` (`target_id`),
  KEY `executive_expire_time` (`executive_expire_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 操作记录表 目前只有计划操作记录
CREATE TABLE `eh_energy_meter_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'plan, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record operator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 缴费、客户里面增加计价条款的设置
CREATE TABLE `eh_default_charging_item_properties` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `default_charging_item_id` BIGINT NOT NULL COMMENT 'id of eh_contract_charging_items',
  `property_type` TINYINT COMMENT '0: community; 1: building; 2: apartment',
  `property_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_default_charging_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the charging, organizationowner,asset, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `charging_item_id` BIGINT COMMENT '收费项',
  `charging_standard_id` BIGINT COMMENT '收费标准',
  `formula` VARCHAR(1024),
  `formula_type` TINYINT COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` TINYINT,
  `late_fee_standard_id` BIGINT COMMENT '滞纳金标准',
  `charging_variables` VARCHAR(1024) COMMENT '计费金额参数 json: {"variables":[{"variableIdentifier":"22","variableName":"面积","variableValue":"960.00"}]}',
  `charging_start_time` DATETIME,
  `charging_expired_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;