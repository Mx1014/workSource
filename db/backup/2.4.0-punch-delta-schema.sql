
-- added by wh  2016-10-19  申请表添加时长和生效日期  

ALTER TABLE `eh_approval_requests` ADD COLUMN `effective_date` DATE COMMENT 'when the request approval effective';
ALTER TABLE `eh_approval_requests` ADD COLUMN `hour_length` DOUBLE COMMENT 'how long (hours) does the request effective'; 

-- added by wh 2016-11-09 打卡计算结果还要对异常的设备进行计算

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `device_change_flag` TINYINT DEFAULT 0 COMMENT '0- unchange 1-changed' ;

-- 3.11.0
-- merge from pmtask-delta-schema.sql
ALTER TABLE eh_pm_tasks ADD COLUMN `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id';
ALTER TABLE eh_pm_tasks ADD COLUMN `reserve_time` DATETIME;
ALTER TABLE eh_pm_tasks ADD COLUMN `priority` TINYINT NOT NULL DEFAULT 0 COMMENT 'task rank of request';
ALTER TABLE eh_pm_tasks ADD COLUMN `source_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'task come from ,such as app ,email';
ALTER TABLE eh_pm_tasks ADD COLUMN `organization_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_pm_tasks ADD COLUMN `requestor_name` VARCHAR(64) COMMENT 'the name of requestor';
ALTER TABLE eh_pm_tasks ADD COLUMN `requestor_phone` VARCHAR(64) COMMENT 'the phone of requestor';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address id';
ALTER TABLE eh_pm_tasks ADD COLUMN `revisit_content` TEXT COMMENT 'revisit content';
ALTER TABLE eh_pm_tasks ADD COLUMN `revisit_time` DATETIME;
ALTER TABLE eh_pm_task_statistics ADD COLUMN `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id';

CREATE TABLE `eh_pm_task_targets` (
  `id` BIGINT(20) NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT(20) DEFAULT NULL,
  `target_type` VARCHAR(64) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` BIGINT(20) DEFAULT NULL COMMENT 'target object(user/group) id',
  `role_id` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- merge from customer-manage-1.1-delta-schema.sql 20161025 by lqs
--
-- 车辆停车类型     add by xq.tian 2016/10/11
--
CREATE TABLE `eh_parking_card_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INT NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL,
  `card_type` TINYINT NOT NULL COMMENT '1. temp, 2. month, 3. free ,etc.',
  `category_name` VARCHAR(64) NOT NULL COMMENT 'name of category',
  `status` TINYINT NOT NULL COMMENT '1: normal, 0: delete',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- item 类别 by sfyan 20161025
-- DROP TABLE IF EXISTS `eh_item_service_categries`;
CREATE TABLE `eh_item_service_categries` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL COMMENT 'service categry name',
  `icon_uri` VARCHAR(1024) COMMENT 'service categry icon uri',
  `order` INTEGER COMMENT 'order ',
  `align` TINYINT DEFAULT '0' COMMENT '0: left, 1: center',
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `namespace_id` INTEGER,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_launch_pad_items` ADD COLUMN `service_categry_id` BIGINT COMMENT 'service categry id';




-- 以下为3.10.4合过来的脚本-------------

--
-- 服务联盟category添加显示类型字段   add by xq.tian  2016/10/18
--
-- ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `display_mode` TINYINT DEFAULT 1;


-- 服务联盟添加新的预约表 add by xiongying20161027
-- CREATE TABLE `eh_service_alliance_reservation_requests` (
--   `id` bigint(20) NOT NULL,
--   `namespace_id` int(11) NOT NULL DEFAULT '0',
--   `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
--   `owner_id` bigint(20) NOT NULL DEFAULT '0',
--   `type` bigint(20) NOT NULL DEFAULT '0',
--   `category_id` bigint(20) NOT NULL DEFAULT '0',
--   `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
--   `creator_name` varchar(128) DEFAULT NULL,
--   `creator_mobile` varchar(128) DEFAULT NULL,
--   `creator_organization_id` bigint(20) NOT NULL DEFAULT '0',
--   `service_alliance_id` bigint(20) NOT NULL DEFAULT '0',
--   `reserve_type` varchar(128) DEFAULT NULL,
--   `reserve_organization` varchar(128) DEFAULT NULL,
--   `reserve_time` varchar(128) DEFAULT NULL,
--   `contact` varchar(128) DEFAULT NULL,
--   `mobile` varchar(128) DEFAULT NULL,
--   `remarks` varchar(1024) DEFAULT NULL,
--   `create_time` datetime DEFAULT NULL,
-- 
--   PRIMARY KEY (`id`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 
-- -- 添加人数限制字段, add by tt, 20161027
-- -- ALTER TABLE `eh_forum_posts` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
-- -- ALTER TABLE `eh_activities` ADD COLUMN `max_quantity` INT COMMENT 'max person quantity';
-- -- ALTER TABLE `eh_activities` ADD COLUMN `content_type` varchar(128) COMMENT 'content type, text/rich_text';
-- -- ALTER TABLE `eh_activities` ADD COLUMN `version` varchar(128) COMMENT 'version';
-- 
-- -- 添加消息提醒设置表, add by tt, 20161027
-- -- DROP TABLE IF EXISTS `eh_warning_settings`;
-- -- CREATE TABLE `eh_warning_settings` (
-- --   `id` BIGINT NOT NULL,
-- --   `namespace_id` INTEGER NOT NULL COMMENT 'namespace id',
-- --   `type` varchar(64) COMMENT 'type',
-- --   `time` BIGINT COMMENT 'millisecond',
-- --   `create_time` DATETIME,
-- --   `creator_uid` BIGINT,
-- --   `update_time` DATETIME,
-- --   `operator_uid` BIGINT,
-- 
-- --   PRIMARY KEY (`id`)
-- -- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 
-- -- 以上为3.10.4合过来的脚本-------------
-- 
-- -- global table. 添加门禁特殊权限相关用户类型 add by Janson 20161028
-- -- DROP TABLE IF EXISTS `eh_door_user_permission`;
CREATE TABLE `eh_door_user_permission` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INT NOT NULL DEFAULT '0',
    `user_id` BIGINT NOT NULL,
    `approve_user_id` BIGINT NOT NULL,
    `auth_type` TINYINT NOT NULL COMMENT '0: Door Guard',
    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
    `owner_id` BIGINT NOT NULL DEFAULT 0,

    `integral_tag1` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag2` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag3` BIGINT DEFAULT 0 NOT NULL,
    `integral_tag4` BIGINT DEFAULT 0 NOT NULL,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),

    `description` VARCHAR(1024),

    `create_time` DATETIME,
    `status` TINYINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- merge from  equipment2-delta-schema.sql by lqs 20161031
ALTER TABLE `eh_equipment_inspection_standards` ADD COLUMN `review_expired_days` INTEGER NOT NULL DEFAULT '0';
ALTER TABLE `eh_equipment_inspection_standards` ADD COLUMN `template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_template';

ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `review_expired_date` DATETIME;

-- 设备表
DROP TABLE IF EXISTS `eh_equipment_inspection_equipments`;
CREATE TABLE `eh_equipment_inspection_equipments` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the equipment, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT '0',
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `equipment_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: equipment, 1: equipment group',
  `custom_number` VARCHAR(128),
  `name` VARCHAR(1024),
  `manufacturer` VARCHAR(1024),
  `location` VARCHAR(1024),
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `geohash` VARCHAR(64) DEFAULT NULL,
  `equipment_model` VARCHAR(1024),
  `category_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to the id of eh_categories',
  `category_path` VARCHAR(128) DEFAULT NULL COMMENT 'reference to the path of eh_categories',  
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: incomplete(不完整), 2: in use(使用中), 3: in maintenance(维修中), 4: discarded(报废), 5: disabled(停用), 6: standby(备用)',
  `installation_time` DATETIME,
  `repair_time` DATETIME,
  `initial_asset_value` VARCHAR(128) DEFAULT NULL,
  `parameter` VARCHAR(1024),
  `quantity` BIGINT(20) NOT NULL DEFAULT '0',
  `remarks` TEXT,
  `sequence_no` VARCHAR(128),
  `version_no` VARCHAR(128),
  `manager` VARCHAR(128),
  `qr_code_token` TEXT,
  `qr_code_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful', 
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 设备-标准映射表
-- DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_standard_map`;
CREATE TABLE `eh_equipment_inspection_equipment_standard_map` (
    `id` BIGINT NOT NULL COMMENT 'id',
    `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_equipment_inspection_standards',
    `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, equipment, etc',
    `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the standard',
    `review_status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: reviewed，3: delete',
    `review_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified',
    `reviewer_uid` BIGINT NOT NULL DEFAULT '0',
    `review_time` DATETIME DEFAULT NULL,
    `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
    `create_time` DATETIME,
    `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
    `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
    `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
	
    PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检模板
-- DROP TABLE IF EXISTS `eh_equipment_inspection_templates`;
CREATE TABLE `eh_equipment_inspection_templates` (
    `id` BIGINT NOT NULL,
    `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
    `owner_id` BIGINT NOT NULL DEFAULT '0',
    `name` VARCHAR(128) NOT NULL,
    `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
    `create_time` DATETIME,
    `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
    `delete_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
    `delete_time` DATETIME,
	
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检模板-巡检项映射表
-- DROP TABLE IF EXISTS `eh_equipment_inspection_template_item_map`;
CREATE TABLE `eh_equipment_inspection_template_item_map` (
    `id` BIGINT NOT NULL COMMENT 'id',
    `template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_template',
    `item_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_items',
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检项
-- DROP TABLE IF EXISTS `eh_equipment_inspection_items`;
CREATE TABLE `eh_equipment_inspection_items` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(512) NOT NULL,
  `value_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0-none、1-two-tuple、2-range',
  `unit` VARCHAR(32),
  `value_jason` VARCHAR(512),
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检项结果
-- DROP TABLE IF EXISTS `eh_equipment_inspection_item_results`;
CREATE TABLE `eh_equipment_inspection_item_results` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `task_log_id` BIGINT NOT NULL COMMENT 'id of the eh_equipment_inspection_task_logs',
  `task_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_tasks',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the result, equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the result',
  `item_id` BIGINT NOT NULL DEFAULT '0',
  `item_name` VARCHAR(512) NOT NULL,
  `item_value_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0-none、1-two-tuple、2-range',
  `item_unit` VARCHAR(32),
  `item_value` VARCHAR(128),
  `normal_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: abnormal; 1: normal',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
