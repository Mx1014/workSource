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
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `target_type` varchar(64) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` bigint(20) DEFAULT NULL COMMENT 'target object(user/group) id',
  `role_id` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 资源分类定义 by sfyan 20161025
-- DROP TABLE IF EXISTS `eh_resource_categories`;
CREATE TABLE `eh_resource_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL COMMENT 'resource categry name',
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NUll DEFAULT 0,
  `path` VARCHAR(128) NOT NUll,
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: inactive, 2: active',
  `create_time` DATETIME DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 资源分配类型 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_resource_category_assignments`;
CREATE TABLE `eh_resource_category_assignments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `resource_categry_id` BIGINT NOT NULL COMMENT 'service categry id',
  `resource_type` VARCHAR(32),
  `resource_id` BIGINT,
  `creator_uid`  BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



-- 机构通用岗位 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_organization_job_positions`;
CREATE TABLE `eh_organization_job_positions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'organization',
  `owner_id` BIGINT NOT NULL COMMENT 'orgnaization member id',
  `name` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 2: active',
  `discription` VARCHAR(128),
  `creator_uid`  BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加组织架构类型 岗位 ， 职级， 经理组
ALTER TABLE `eh_organizations` MODIFY `group_type` VARCHAR(64) DEFAULT NULL COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER';

-- 增加组织架构大小 目前只用于职级大小
ALTER TABLE `eh_organizations` ADD `size` INTEGER COMMENT 'job level size';

-- 机构岗位所属的通用岗位 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_organization_job_position_maps`;
CREATE TABLE `eh_organization_job_position_maps` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `job_position_id` BIGINT NOT NULL,
  `organization_id` BIGINT NOT NULL COMMENT 'orgnaization member id',
  `creator_uid`  BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 业务模块 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_service_modules`;
CREATE TABLE `eh_service_modules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) DEFAULT NULL,
  `parent_id` BIGINT NOT NUll,
  `path` VARCHAR(128) NOT NUll,
  `type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: park, 1: organization, 2:manager',
  `level` INTEGER NOT NULL DEFAULT '0',
  `status` TINYINT NOT NULL DEFAULT '2' COMMENT '0: inactive, 2: active',
  `default_order` INTEGER NULL COMMENT 'order number',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 业务模块范围配置 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_service_module_scopes`;
CREATE TABLE `eh_service_module_scopes` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `module_id` BIGINT DEFAULT NULL,
  `module_name` VARCHAR(64) DEFAULT NULL,
  `owner_type` VARCHAR(64) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL,
  `default_order` INTEGER NULL COMMENT 'order number',
  `apply_policy` TINYINT NOT NULL DEFAULT '0' COMMENT '0: delete , 1: override, 2: revert',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 业务模块 by sfyan 20161029
-- DROP TABLE IF EXISTS `eh_service_module_privileges`;
CREATE TABLE `eh_service_module_privileges` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `module_id` BIGINT NOT NULL COMMENT 'service module id',
  `privilege_type` TINYINT NOT NULL COMMENT '0: general, 1: super',
  `privilege_id` BIGINT NOT NULL COMMENT 'privilege id',
  `remark` VARCHAR(128) NULL COMMENT'remark',
  `default_order` INTEGER NULL COMMENT'order number',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 业务模块分配 by sfyan 20161029
-- 超级管理员 定义一个超管权限
-- 公司管理员 定义一个公司管理员的权限 
-- 每个模块都定义自己独有的超管权限
-- DROP TABLE IF EXISTS `eh_service_module_assignments`;
CREATE TABLE `eh_service_module_assignments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `organization_id` BIGINT NOT NULL COMMENT 'organization id',
  `target_type` VARCHAR(32) NOT NULL COMMENT 'organization user',
  `target_id` BIGINT NOT NULL ,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL,
  `module_id` BIGINT NOT NULL,
  `create_uid` BIGINT NOT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_acls` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_acls` ADD COLUMN `role_type` VARCHAR(32) COMMENT 'NULL: EhAclRole';
ALTER TABLE `eh_acls` ADD COLUMN `scope` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag1` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag2` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag3` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag4` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `integral_tag5` BIGINT;
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag1` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag2` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag3` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag4` VARCHAR(128);
ALTER TABLE `eh_acls` ADD COLUMN `comment_tag5` VARCHAR(128);
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_namespace_id`(`namespace_id`); 
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_scope`(`scope`); 
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_itag1`(`integral_tag1`); 
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_itag2`(`integral_tag2`); 
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_ctag1`(`comment_tag1`);
ALTER TABLE `eh_acls` ADD INDEX `i_eh_acl_ctag2`(`comment_tag2`);

ALTER TABLE `eh_acl_role_assignments` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries';
ALTER TABLE `eh_acl_role_assignments` ADD INDEX `i_eh_acl_role_asgn_namespace_id`(`namespace_id`);

ALTER TABLE `eh_launch_pad_items` CHANGE `target_id` `target_id` VARCHAR(64);


-- merge 3.11.2 sfyan

-- added by wh  2016-10-19  申请表添加时长和生效日期  

ALTER TABLE `eh_approval_requests` ADD COLUMN `effective_date` DATE COMMENT 'when the request approval effective';
ALTER TABLE `eh_approval_requests` ADD COLUMN `hour_length` DOUBLE COMMENT 'how long (hours) does the request effective'; 

-- added by wh 2016-11-09 打卡计算结果还要对异常的设备进行计算

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `device_change_flag` TINYINT DEFAULT 0 COMMENT '0- unchange 1-changed' ;


-- merge from energy-delta-schema.sql   by xq.tian  2016/11/11
--
-- 表记   add by xq.tian  2016/10/25
--
-- DROP TABLE IF EXISTS `eh_energy_meters`;
CREATE TABLE `eh_energy_meters` (
  `id`                  BIGINT  NOT NULL,
  `namespace_id`        INTEGER NOT NULL DEFAULT 0,
  `community_id`        BIGINT,
  `name`                VARCHAR(100),
  `meter_number`        VARCHAR(50) COMMENT 'meter number',
  `meter_type`          TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `bill_category_id`    BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `max_reading`         DECIMAL(10, 1) COMMENT 'The maximum range of the meter',
  `start_reading`       DECIMAL(10, 1) COMMENT 'The initial reading of the meter',
  `rate`                DECIMAL(10, 2) COMMENT 'Calculate magnification',
  `price`               DECIMAL(10, 2),
  `cost_formula_id`     BIGINT COMMENT 'Cost calculation formula',
  `amount_formula_id`   BIGINT COMMENT 'Amount calculation formula',
  `last_read_time`      DATETIME,
  `last_reading`        DECIMAL(10, 1),
  `status`              TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`         BIGINT,
  `create_time`         DATETIME,
  `update_uid`          BIGINT,
  `update_time`         DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 抄表记录
--
-- DROP TABLE IF EXISTS `eh_energy_meter_reading_logs`;
CREATE TABLE `eh_energy_meter_reading_logs` (
  `id`                BIGINT  NOT NULL,
  `namespace_id`      INTEGER NOT NULL DEFAULT 0,
  `community_id`      BIGINT,
  `meter_id`          BIGINT,
  `reading`           DECIMAL(10, 1),
  `operator_id`       BIGINT,
  `operate_time`      DATETIME,
  `reset_meter_flag`  TINYINT DEFAULT 0 COMMENT '0: normal, 1: reset',
  `change_meter_flag` TINYINT DEFAULT 0 COMMENT '0: normal, 1: change',
  `status`            TINYINT DEFAULT 2 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`       BIGINT,
  `create_time`       DATETIME,
  `update_uid`        BIGINT,
  `update_time`       DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 换表记录
--
-- DROP TABLE IF EXISTS `eh_energy_meter_change_logs`;
CREATE TABLE `eh_energy_meter_change_logs` (
  `id`             BIGINT  NOT NULL,
  `namespace_id`   INTEGER NOT NULL DEFAULT 0,
  `meter_id`       BIGINT,
  `reading_log_id` BIGINT COMMENT 'eh_energy_meter_reading_logs id',
  `old_reading`    DECIMAL(10, 1) COMMENT 'The reading of the old meter',
  `new_reading`    DECIMAL(10, 1) COMMENT 'The initial reading of the new meter',
  `max_reading`    DECIMAL(10, 1) COMMENT 'The maximum range of the new meter',
  `operator_id`    BIGINT,
  `operate_time`   DATETIME,
  `status`         TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`    BIGINT,
  `create_time`    DATETIME,
  `update_uid`     BIGINT,
  `update_time`    DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 表记分类属性
--
-- DROP TABLE IF EXISTS `eh_energy_meter_categories`;
CREATE TABLE `eh_energy_meter_categories` (
  `id`            BIGINT  NOT NULL,
  `namespace_id`  INTEGER NOT NULL DEFAULT 0,
  `name`          VARCHAR(255) COMMENT 'name',
  `status`        TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `category_type` TINYINT COMMENT '1: bill, 2: service',
  `delete_flag`   TINYINT COMMENT '0: can delete, 1: can not delete',
  `creator_uid`   BIGINT,
  `create_time`   DATETIME,
  `update_uid`    BIGINT,
  `update_time`   DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 表记属性
--
-- DROP TABLE IF EXISTS `eh_energy_meter_setting_logs`;
CREATE TABLE `eh_energy_meter_setting_logs` (
  `id`            BIGINT  NOT NULL,
  `namespace_id`  INTEGER NOT NULL DEFAULT 0,
  `meter_id`      BIGINT,
  `setting_type`  TINYINT COMMENT '1: price, 2: rate, 3: amountFormula, 4: costFormula',
  `setting_value` DECIMAL(10, 2) COMMENT 'if setting_type is price or rate have this value',
  `formula_id`    BIGINT COMMENT 'if setting_type is amountFormula or costFormula have this value',
  `start_time`    DATETIME COMMENT 'The start of the time period',
  `end_time`      DATETIME COMMENT 'The end of the time period',
  `status`        TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`   BIGINT,
  `create_time`   DATETIME,
  `update_uid`    BIGINT,
  `update_time`   DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 默认显示的属性值
--
-- DROP TABLE IF EXISTS `eh_energy_meter_default_settings`;
CREATE TABLE `eh_energy_meter_default_settings` (
  `id`            BIGINT  NOT NULL,
  `namespace_id`  INTEGER NOT NULL DEFAULT 0,
  `meter_type`    TINYINT COMMENT '1:WATER, 2: ELECTRIC, 3: ALL',
  `setting_type`  TINYINT COMMENT '1: price, 2: rate, 3: amountFormula, 4: costFormula, 5: dayPrompt, 6: monthPrompt etc',
  `name`          VARCHAR(255) COMMENT 'setting name',
  `setting_value` DECIMAL(10, 2) COMMENT 'if setting_type is price or rate have this value',
  `formula_id`    BIGINT COMMENT 'if setting_type is amountFormula or costFormula have this value',
  `status`        TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active, 3: disabled',
  `creator_uid`   BIGINT,
  `create_time`   DATETIME,
  `update_uid`    BIGINT,
  `update_time`   DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 表记计算公式
--
-- DROP TABLE IF EXISTS `eh_energy_meter_formulas`;
CREATE TABLE `eh_energy_meter_formulas` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name`         VARCHAR(255) COMMENT 'formula name',
  `expression`   VARCHAR(255) COMMENT 'expression',
  `display_expression`   VARCHAR(255) COMMENT 'user input expression',
  `formula_type` TINYINT COMMENT '1: amount, 2: cost',
  `status`       TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`  BIGINT,
  `create_time`  DATETIME,
  `update_uid`   BIGINT,
  `update_time`  DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 公式变量
--
-- DROP TABLE IF EXISTS `eh_energy_meter_formula_variables`;
CREATE TABLE `eh_energy_meter_formula_variables` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name`         VARCHAR(255) COMMENT 'variable name',
  `display_name` VARCHAR(255) COMMENT 'display name',
  `description`  VARCHAR(255) COMMENT 'description',
  `status`       TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`  BIGINT,
  `create_time`  DATETIME,
  `update_uid`   BIGINT,
  `update_time`  DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 每日水电报表
--
-- DROP TABLE IF EXISTS `eh_energy_date_statistics`;
CREATE TABLE `eh_energy_date_statistics` (
  `id`                  BIGINT  NOT NULL,
  `namespace_id`        INTEGER NOT NULL DEFAULT 0,
  `community_id`        BIGINT,
  `meter_id`            BIGINT  NOT NULL COMMENT '水表id',
  `meter_type`          TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `stat_date`            DATE COMMENT '改成用日期存,方便过滤和计算',
  `bill_category_id`    BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `formula_id`          TINYINT COMMENT 'eh_energy_meter_formulas id',
  `meter_name`          VARCHAR(255),
  `meter_number`        VARCHAR(255),
  `meter_bill`          VARCHAR(255) COMMENT 'meter bill category name',
  `meter_service`       VARCHAR(255) COMMENT 'meter service category name',
  `meter_rate`          DECIMAL(10, 2) COMMENT '表的倍率',
  `meter_price`         DECIMAL(10, 2) COMMENT '表的单价',
  `last_reading`        DECIMAL(10, 1) COMMENT '上次读数',
  `current_reading`     DECIMAL(10, 1) COMMENT '这次读数',
  `current_amount`      DECIMAL(10, 1) COMMENT '用量',
  `current_cost`        DECIMAL(10, 1) COMMENT '费用',
  `reset_meter_flag`    TINYINT COMMENT '0: normal, 1: reset',
  `change_meter_flag`   TINYINT COMMENT '0: normal, 1: change',
  `status`              TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`         BIGINT,
  `create_time`         DATETIME,
  `update_uid`          BIGINT,
  `update_time`         DATETIME,
  PRIMARY KEY (`id`)
);

--
--  每月水电报表
--
-- DROP TABLE IF EXISTS `eh_energy_month_statistics`;
CREATE TABLE `eh_energy_month_statistics` (
  `id`                  BIGINT  NOT NULL,
  `namespace_id`        INTEGER NOT NULL DEFAULT 0,
  `community_id`        BIGINT,
  `meter_id`            BIGINT  NOT NULL COMMENT '水表id',
  `meter_type`          TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `date_str`            VARCHAR(20)COMMENT  'YYYMM 例如 201608' ,
  `bill_category_id`    BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `formula_id`          TINYINT COMMENT 'eh_energy_meter_formulas id',
  `meter_name`          VARCHAR(255),
  `meter_number`        VARCHAR(255),
  `meter_bill`          VARCHAR(255) COMMENT 'meter bill category name',
  `meter_service`       VARCHAR(255) COMMENT 'meter service category name',
  `meter_rate`          DECIMAL(10, 2),
  `meter_price`         DECIMAL(10, 2),
  `last_reading`        DECIMAL(10, 1),
  `current_reading`     DECIMAL(10, 1),
  `current_amount`      DECIMAL(10, 1),
  `current_cost`        DECIMAL(10, 1),
  `reset_meter_flag`    TINYINT COMMENT '0: normal, 1: reset',
  `change_meter_flag`   TINYINT COMMENT '0: normal, 1: change',
  `status`              TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`         BIGINT,
  `create_time`         DATETIME,
  `update_uid`          BIGINT,
  `update_time`         DATETIME,
  PRIMARY KEY (`id`)
);
--
-- 总量记录表
--
-- DROP TABLE IF EXISTS `eh_energy_count_statistics`;
CREATE TABLE `eh_energy_count_statistics` (
  `id`                    BIGINT  NOT NULL,
  `namespace_id`          INTEGER NOT NULL DEFAULT 0,
  `community_id`          BIGINT,
  `meter_type`            TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `date_str`              VARCHAR(20),
  `statistic_type`        TINYINT COMMENT '1:bill, 2: service, 3:burden',
  `bill_category_id`      BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id`   BIGINT COMMENT 'eh_energy_meter_categories id',
  `bill_category_name`    VARCHAR(255),
  `service_category_name` VARCHAR(255),
  `amount`                DECIMAL(10, 1),
  `cost`                  DECIMAL(10, 1),
  `status`                TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`           BIGINT,
  `create_time`           DATETIME,
  `update_uid`            BIGINT,
  `update_time`           DATETIME,
  PRIMARY KEY (`id`)
);

--
-- 各项目月水电能耗情况-同比
--
-- DROP TABLE IF EXISTS `eh_energy_yoy_statistics`;
CREATE TABLE `eh_energy_yoy_statistics` (
  `id`                BIGINT  NOT NULL,
  `namespace_id`      INTEGER NOT NULL DEFAULT 0,
  `community_id`      BIGINT,
  `date_str`          VARCHAR(20),
  `area_size`         DOUBLE COMMENT 'Community area size',
  `water_receivable_amount` DECIMAL(10, 1) COMMENT '水表本月应收用量',
  `water_payable_amount`    DECIMAL(10, 1) COMMENT '水表本月应付用量',
  `water_burden_amount`     DECIMAL(10, 1) COMMENT '水表本月负担公共用量',
  `water_average_amount`    DECIMAL(10, 1) COMMENT '水表本月每平米平均用量',
  `water_last_receivable_amount` DECIMAL(10, 1) COMMENT '水表去年同期应收用量',
  `water_last_payable_amount`    DECIMAL(10, 1) COMMENT '水表去年同期应付用量',
  `water_last_burden_amount`     DECIMAL(10, 1) COMMENT '水表去年同期负担公共用量',
  `water_last_average_amount`    DECIMAL(10, 1) COMMENT '水表去年同期每平米平均用量',
  `electric_receivable_amount` DECIMAL(10, 1) COMMENT '电表应收用量',
  `electric_payable_amount`    DECIMAL(10, 1) COMMENT '电表应付用量',
  `electric_burden_amount`     DECIMAL(10, 1) COMMENT '电表负担公共用量',
  `electric_average_amount`    DECIMAL(10, 1) COMMENT '电表每平米平均用量',
  `electric_last_receivable_amount` DECIMAL(10, 1) COMMENT '电表去年同期应收用量',
  `electric_last_payable_amount`    DECIMAL(10, 1) COMMENT '电表去年同期应付用量',
  `electric_last_burden_amount`     DECIMAL(10, 1) COMMENT '电表去年同期负担公共用量',
  `electric_last_average_amount`    DECIMAL(10, 1) COMMENT '电表去年同期每平米平均用量',
  `status`            TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`       BIGINT,
  `create_time`       DATETIME,
  `update_uid`        BIGINT,
  `update_time`       DATETIME,
  PRIMARY KEY (`id`)
);



-- 圈添加审核状态，add by tt, 20161028
ALTER TABLE `eh_groups` ADD COLUMN `approval_status` TINYINT COMMENT 'approval status';
ALTER TABLE `eh_groups` ADD COLUMN `operator_uid` BIGINT;

-- 设置圈（俱乐部）参数，add by tt, 20161028
-- DROP TABLE IF EXISTS `eh_group_settings`;
CREATE TABLE `eh_group_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `create_flag` TINYINT COMMENT 'whether allow create club',
  `verify_flag` TINYINT COMMENT 'whether need verify',
  `member_post_flag` TINYINT COMMENT 'whether allow members create post',
  `member_comment_flag` TINYINT COMMENT 'whether allow members comment on the post',
  `admin_broadcast_flag` TINYINT COMMENT 'whether allow admin broadcast',
  `broadcast_count` INTEGER COMMENT 'how many broadcasts can be created per day',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'news creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 广播消息，add by tt, 20161028
-- DROP TABLE IF EXISTS `eh_broadcasts`;
CREATE TABLE `eh_broadcasts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(32) COMMENT 'group',
  `owner_id` BIGINT DEFAULT 0 COMMENT 'group_id',
  `title` VARCHAR(1024) COMMENT 'title',
  `content_type` VARCHAR(32) COMMENT 'object content type: text、rich text',
  `content` LONGTEXT COMMENT 'content data, depends on value of content_type',
  `content_abstract` TEXT COMMENT 'abstract of content data',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 版本url表添加图标的链接地址, add by tt, 20161107
ALTER TABLE `eh_version_urls`	ADD COLUMN `icon_url` VARCHAR(50);

