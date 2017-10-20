ALTER TABLE eh_energy_meters ADD COLUMN `cost_formula_source` TINYINT DEFAULT '0' COMMENT '0: 能耗设置, 1: 缴费模块';
ALTER TABLE eh_energy_meter_setting_logs ADD COLUMN `formula_source` TINYINT DEFAULT '0' COMMENT '0: 能耗设置, 1: 缴费模块';

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
