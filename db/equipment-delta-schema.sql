-- 1、参考标准表：
DROP TABLE IF EXISTS `eh_equipment_inspection_standards`;
CREATE TABLE `eh_equipment_inspection_standards` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `standard_number` VARCHAR(128),
  `name` VARCHAR(1024),
  `standard_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: routing inspection, 2:maintain',
  `repeat_setting_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_repeat_settings',
  `description` TEXT COMMENT 'content data',
  `remarks` TEXT,
  `standard_source` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: not completed, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2、设备表：
DROP TABLE IF EXISTS `eh_equipment_inspection_equipments`;
CREATE TABLE `eh_equipment_inspection_equipments` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the equipment, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(1024),
  `manufacturer` VARCHAR(1024),
  `equipment_model` VARCHAR(1024),
  `category_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_categories',
  `category_path` VARCHAR(128) DEFAULT NULL COMMENT 'refernece to the path of eh_categories',
  `location` VARCHAR(1024),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(64),
  `qr_code_token` TEXT,
  `qr_code_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: incomplete(不完整), 2: in use(使用中), 3: in maintenance(维修中), 4: discarded(报废), 5: disabled(停用), 6: standby(备用)',
  `installation_time` DATETIME,
  `repair_time` DATETIME,
  `initial_asset_value` VARCHAR(128),
  `remarks` TEXT,
  `manager` VARCHAR(128),
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `standard_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `review_status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: reviewed，3: delete',
  `review_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified',
  `reviewer_uid` BIGINT NOT NULL DEFAULT '0',
  `review_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3、备品备件表：
DROP TABLE IF EXISTS `eh_equipment_inspection_accessories`;
CREATE TABLE `eh_equipment_inspection_accessories` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the spare parts, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the accessory, etc',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(1024),
  `manufacturer` VARCHAR(1024),
  `model_number` VARCHAR(1024),
  `specification` VARCHAR(1024),
  `location` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4、设备-备件 关联表：equipment_id和accessory_id共同确立一条记录
DROP TABLE IF EXISTS `eh_equipment_inspection_accessory_map`;
CREATE TABLE `eh_equipment_inspection_accessory_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `equipment_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `accessory_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_accessories',
  `quantity` INTEGER NOT NULL DEFAULT '0', 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5、设备参数表
DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_parameters`;
CREATE TABLE `eh_equipment_inspection_equipment_parameters` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `equipment_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `parameter_name` VARCHAR(128),
  `parameter_unit` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 6、设备操作图示表 attachments 及说明书  type区分
DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_attachments`;
CREATE TABLE `eh_equipment_inspection_equipment_attachments` (
     `id` BIGINT NOT NULL COMMENT 'id of the record',
     `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
     `attachment_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: tu shi, 2: shuo ming shu',
     `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
     `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
     `creator_uid` BIGINT NOT NULL DEFAULT 0,
     `create_time` DATETIME,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 7、任务表
DROP TABLE IF EXISTS `eh_equipment_inspection_tasks`;
CREATE TABLE `eh_equipment_inspection_tasks` (
     `id` BIGINT NOT NULL COMMENT 'id',
     `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
     `owner_id` BIGINT NOT NULL DEFAULT 0,
     `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_standards',
     `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
     `task_number` VARCHAR(128),
     `task_name` VARCHAR(1024),
     `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '0: parent task, others children-task',
     `child_count` BIGINT NOT NULL DEFAULT 0,
     `executive_group_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, etc',
     `executive_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
     `executive_start_time` DATETIME,
     `executive_expire_time` DATETIME,
     `executive_time` DATETIME,
     `executor_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
     `executor_id` BIGINT NOT NULL DEFAULT 0,
     `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
     `operator_id` BIGINT NOT NULL DEFAULT 0,
     `process_expire_time` DATETIME,
     `process_time` DATETIME,
     `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: waiting for executing, 2: waiting for maintenance, 3: in maintenance, 4: closed',
     `result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: complete ok, 2: complete delay, 3: need maintenance ok, 4: need maintenance delay, 5：need maintenance ok complete ok, 6: need maintenance ok complete delay, 7: need maintenance delay complete ok, 8: need maintenance delay complete delay',
     `reviewer_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
     `reviewer_id`  BIGINT NOT NULL DEFAULT 0,
     `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
     `review_time` DATETIME,
     `create_time` DATETIME,
     PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
    
-- 8、任务attachments表
DROP TABLE IF EXISTS `eh_equipment_inspection_task_attachments`;
CREATE TABLE `eh_equipment_inspection_task_attachments` (
     `id` BIGINT NOT NULL COMMENT 'id',
     `log_id` BIGINT NOT NULL COMMENT 'id of the eh_equipment_inspection_task_logs',
     `task_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_tasks',
     `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
     `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
     `creator_uid` BIGINT NOT NULL DEFAULT 0,
     `create_time` DATETIME,
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 9、记录表
DROP TABLE IF EXISTS `eh_equipment_inspection_task_logs`;
CREATE TABLE `eh_equipment_inspection_task_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `task_id` BIGINT NOT NULL DEFAULT '0',
  `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who operates the task, USER, etc',
  `operator_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who is the target of processing the task, USER, etc',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `process_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: complete, 2: complete maintenance, 3: review, 4: need maintenance ',
  `process_end_time` DATETIME DEFAULT NULL,
  `process_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0: none, 1: complete ok, 2: complete delay, 3: need maintenance ok, 4: need maintenance delay, 5：need maintenance ok complete ok, 6: need maintenance ok complete delay, 7: need maintenance delay complete ok, 8: need maintenance delay complete delay, 9: review qualified, 10: review unqualified',
  `process_message` TEXT,
  `parameter_value` VARCHAR(1024),
  `process_time` DATETIME DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
