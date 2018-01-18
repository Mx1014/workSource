-- domain 增加name, icon
ALTER TABLE `eh_domains` ADD COLUMN `icon_uri`  varchar(255) NULL AFTER `create_time`;
ALTER TABLE `eh_domains` ADD COLUMN `name`  varchar(255) NULL AFTER `namespace_id`;

ALTER TABLE `eh_web_menu_scopes` ADD COLUMN `app_id`  bigint(20) NULL COMMENT 'eh_service_module_app id';
ALTER TABLE `eh_web_menu_scopes` DROP INDEX `u_menu_scope_owner` , ADD UNIQUE INDEX `u_menu_scope_owner` (`menu_id`, `owner_type`, `owner_id`, `app_id`) USING BTREE ;

-- 账单item关联滞纳金 by wentian
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `late_fine_standard_id` BIGINT DEFAULT NULL COMMENT '滞纳金标准id';
-- 滞纳金表 by wentian
DROP TABLE IF EXISTS `eh_payment_late_fine`;
CREATE TABLE `eh_payment_late_fine`(
  `id` BIGINT NOT NULL COMMENT 'primary key',
  `name` VARCHAR(20) COMMENT '滞纳金名称',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount of overdue payment',
  `bill_id` BIGINT NOT NULL COMMENT 'the id of the corresponding bill, one to one',
  `bill_item_id` BIGINT NOT NULL COMMENT 'the id of the corresponding bill item id, one to one',
  `create_time` DATETIME DEFAULT NOW(),
  `upate_time` DATETIME DEFAULT NOW(),
  `update_uid` BIGINT DEFAULT NULL,
  `namespace_id` INTEGER DEFAULT NULL COMMENT 'location info, for possible statistics later',
  `community_id` BIGINT DEFAULT NULL,
  `customer_id` BIGINT NOT NULL COMMENT 'allows searching taking advantage of it',
  `customer_type` VARCHAR(20) NOT NULL COMMENT 'break of user info benefits',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

-- 增加项目区分 by st.zheng
ALTER TABLE `eh_categories`
ADD COLUMN `owner_type` VARCHAR(32) NULL DEFAULT NULL AFTER `namespace_id`;
ALTER TABLE `eh_categories`
ADD COLUMN `owner_id` BIGINT(20) NULL DEFAULT '0' AFTER `owner_type`;

-- by st.zheng
ALTER TABLE `eh_pm_tasks`
  ADD COLUMN `refer_type` VARCHAR(32) NULL COMMENT '引用类型' AFTER `if_use_feelist`;
ALTER TABLE `eh_pm_tasks`
  ADD COLUMN `refer_id` BIGINT(20) NULL COMMENT '引用id' AFTER `refer_type`;

-- 物业巡检V3.1
-- 设备巡检计划表
CREATE TABLE `eh_equipment_inspection_plans` (
  `id` bigint(20) NOT NULL,
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'organization_id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'organization',
  `target_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'zone resource_type ',
  `target_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'zone  resource_id',
  `plan_number` varchar(128) NOT NULL DEFAULT '0' COMMENT 'the plans number ',
  `plan_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'the type of plan 0: 巡检  1: 保养',
  `name` varchar(1024) DEFAULT NULL COMMENT 'the name of plan_number',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'status of plans  0:waitting for starting 1: waitting for approving  2: QUALIFIED 3:UN_QUALIFIED',
  `reviewer_uid` bigint(20) NOT NULL DEFAULT '0',
  `review_time` datetime DEFAULT NULL,
  `repeat_setting_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refers to eh_repeatsetting ',
  `remarks` text,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleter_uid` bigint(20) DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `last_create_taskTime` datetime DEFAULT NULL COMMENT 'the last time when gen task',
  `inspection_category_id` bigint(20) DEFAULT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 设备巡检计划--设备 关联表
CREATE TABLE `eh_equipment_inspection_equipment_plan_map` (
  `id` bigint(20) NOT NULL,
  `equiment_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '',
  `target_id` bigint(20) NOT NULL DEFAULT '0',
  `target_type` varchar(32) NOT NULL DEFAULT '',
  `plan_id` bigint(20) NOT NULL DEFAULT '0',
  `standard_id` bigint(20) NOT NULL DEFAULT '0',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `default_order` bigint(20) NOT NULL DEFAULT '0' COMMENT 'show order of equipment_maps',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 巡检计划 执行组审批组 关联表 start   by jiarui
CREATE TABLE `eh_equipment_inspection_plan_group_map` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `group_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: executive group, 2: review group',
  `plan_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_equipment_inspection_plans',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `position_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 巡检计划 执行组审批组 关联表 end  by jiarui

CREATE TABLE `eh_equipment_inspection_review_date` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasksReviewExpireDays...',
  `scope_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all; 1: namespace; 2: community',
  `scope_id` bigint(20) NOT NULL,
  `review_expired_days` int(11) NOT NULL DEFAULT '0' COMMENT 'review_expired_days',
  `status` tinyint(4) NOT NULL COMMENT '0: invalid, 1: valid',
  `create_time` datetime NOT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_equipment_inspection_equipment_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `target_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'standard, etc',
  `target_id` bigint(20) NOT NULL DEFAULT '0',
  `process_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record operator user id',
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- eh_equipment_inspection_tasks 增加plan_id字段 用于关联task和equipments
ALTER TABLE `eh_equipment_inspection_tasks`
  ADD COLUMN `plan_id`  bigint(20) NOT NULL ;


-- 标准增加周期类型
ALTER TABLE `eh_equipment_inspection_standards`
  ADD COLUMN `repeat_type` tinyint(4) NOT NULL COMMENT ' 0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year';
-- 操作记录表增加设备id表
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `equipment_id`  bigint(20) NULL DEFAULT 0 ;

-- 离线支持  jiarui
ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `update_uid` BIGINT (20) NULL AFTER `status`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `update_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP AFTER `update_uid`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `delete_uid` BIGINT (20) NULL AFTER `update_uid`;

ALTER TABLE `eh_quality_inspection_specifications`
  ADD COLUMN `delete_time` datetime NULL ON UPDATE CURRENT_TIMESTAMP AFTER `delete_uid`;

-- 离线支持  jiarui


-- 文档管理1.0 add by nan.rong 01/12/2018
-- DROP TABLE eh_file_management_catalogs;
CREATE TABLE `eh_file_management_catalogs` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) ,
  `name` VARCHAR(64) COMMENT 'the name of the catalog',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid 1-valid',
  `creator_uid` BIGINT  DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- DROP TABLE eh_file_management_catalog_scopes;
CREATE TABLE `eh_file_management_catalog_scopes` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `catalog_id` BIGINT NOT NULL COMMENT 'the id of the file catalog',
  `source_id` BIGINT NOT NULL COMMENT 'the id of the source',
  `source_description` VARCHAR(128) COMMENT 'the description of the scope class',
  `download_permission` TINYINT NOT NULL DEFAULT 0 COMMENT '0-refuse, 1-allow',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- DROP TABLE eh_file_management_contents;
CREATE TABLE `eh_file_management_contents` (
  `id` BIGINT,
  `namespace_id` INT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR (64),
  `catalog_id` BIGINT COMMENT 'the id of the catalog',
  `name` VARCHAR(256) NOT NULL COMMENT 'the name of the content',
  `size` INT NOT NULL COMMENT 'the size of the content',
  `parent_id` BIGINT COMMENT 'the parent id of the folder',
  `content_type` VARCHAR(32) COMMENT 'file, folder',
  `content_uri` VARCHAR(2048) COMMENT 'the uri of the content',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

-- end by nan.rong

-- 能耗离线 add by xiongying 01/18/2018
CREATE TABLE `eh_sync_offline_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `type` VARCHAR(64) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL,
  `result` LONGTEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- end by xiongying


