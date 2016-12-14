-- merge from organization-delta-schema.sql by lqs 20161128
-- 资源分类定义 by sfyan 20161025
-- DROP TABLE IF EXISTS `eh_resource_categories`;
CREATE TABLE `eh_resource_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL COMMENT 'resource categry name',
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `path` VARCHAR(128) NOT NULL,
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
  `parent_id` BIGINT NOT NULL,
  `path` VARCHAR(128) NOT NULL,
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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

ALTER TABLE `eh_launch_pad_items` CHANGE `target_id` `target_id` VARCHAR(64);






-- merge from quality2-delta-schema.sql by lqs 20161128
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `review_result` TINYINT NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `reviewer_uid` BIGINT NOT NULL DEFAULT '0';
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `review_time` DATETIME;
ALTER TABLE eh_quality_inspection_standards ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT '0';
ALTER TABLE eh_quality_inspection_standards DROP COLUMN `category_id`;

ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '';
ALTER TABLE eh_quality_inspection_tasks ADD COLUMN `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id';

-- DROP TABLE IF EXISTS `eh_quality_inspection_standard_specification_map`;
CREATE TABLE `eh_quality_inspection_standard_specification_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `standard_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to the id of eh_equipment_inspection_standards',
  `specification_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to the id of eh_quality_inspection_specifications',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for approval, 2: active',
  `deleter_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_quality_inspection_specifications`;
CREATE TABLE `eh_quality_inspection_specifications` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `scope_code` TINYINT NOT NULL DEFAULT '0' COMMENT '0: all, 1: community',
  `scope_id` BIGINT NOT NULL DEFAULT '0',
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL DEFAULT '',
  `path` VARCHAR(128),
  `score` DOUBLE NOT NULL DEFAULT '100',
  `description` TEXT COMMENT 'content data',
  `weight` DOUBLE NOT NULL DEFAULT '1.00',
  `inspection_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: category, 1: specification, 2: specification item',
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `apply_policy` TINYINT NOT NULL DEFAULT '0' COMMENT '0: add, 1: modify, 2: delete',
  `refer_id` BIGINT NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for approval, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_quality_inspection_specification_item_results`;
CREATE TABLE `eh_quality_inspection_specification_item_results` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `task_record_id` BIGINT NOT NULL COMMENT 'id of the eh_quality_inspection_task_records',
  `task_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_tasks',
  `specification_parent_id` BIGINT NOT NULL DEFAULT '0',
  `specification_id` BIGINT NOT NULL DEFAULT '0',
  `specification_path` VARCHAR(128),
  `item_description` VARCHAR(512),
  `item_score` DOUBLE NOT NULL DEFAULT '0',
  `quantity` INTEGER NOT NULL DEFAULT '0',
  `total_score` DOUBLE NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- merge from activityentry-delta-schema.sql by xiongying 20161128
ALTER TABLE eh_activities ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity category id';
ALTER TABLE eh_activities ADD COLUMN `forum_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity post forum that it belongs';
ALTER TABLE eh_activities ADD COLUMN `creator_tag` VARCHAR(128) COMMENT 'activity post creator tag';
ALTER TABLE eh_activities ADD COLUMN `target_tag` VARCHAR(128) COMMENT 'activity post target tag';
ALTER TABLE eh_activities ADD COLUMN `visible_region_type` TINYINT COMMENT 'define the visible region type';
ALTER TABLE eh_activities ADD COLUMN `visible_region_id` BIGINT COMMENT 'visible region id';

-- DROP TABLE IF EXISTS `eh_activity_categories`;
CREATE TABLE `eh_activity_categories` (
  `id` BIGINT(20) NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parent_id` BIGINT(20) NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128) DEFAULT NULL,
  `default_order` INT(11) DEFAULT NULL,
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `delete_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME DEFAULT NULL,
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- merge from sa1.7-delta-schema.sql by xiongying 20161128
-- 预约看楼信息
-- DROP TABLE IF EXISTS `eh_service_alliance_apartment_requests`;
CREATE TABLE `eh_service_alliance_apartment_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,

  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `area_size` DOUBLE COMMENT 'area size',
  `remarks` VARCHAR(1024),

  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_service_alliance_skip_rule`;
CREATE TABLE `eh_service_alliance_skip_rule` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_alliance_category_id` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 物业报修2.6 merge from pmtask-delta-schema.sql by sw 20161128
UPDATE eh_pm_task_targets SET role_id = 1 WHERE role_id = 1017;
UPDATE eh_pm_task_targets SET role_id = 2 WHERE role_id = 1018;
ALTER TABLE eh_pm_task_targets CHANGE role_id role_id TINYINT NOT NULL;

ALTER TABLE eh_pm_tasks ADD COLUMN `operator_star` TINYINT NOT NULL DEFAULT 0 COMMENT 'task star of operator';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_type` TINYINT COMMENT '1: family , 2:organization';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_org_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'organization of address';
-- DROP TABLE IF EXISTS `eh_pm_task_target_statistics`;
CREATE TABLE `eh_pm_task_target_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `target_id` BIGINT NOT NULL DEFAULT 0,
  `avg_star` DECIMAL(10,2) NOT NULL DEFAULT 0,
  `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id',

  `date_str` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 为activity category增加default_flag add by xiongying20161130
ALTER TABLE eh_activity_categories ADD COLUMN `default_flag` TINYINT  NOT NULL DEFAULT 0 COMMENT '0: no , 1: yes';




--
-- 用户认证记录表:记录用户加入/退出企业的log
--

CREATE TABLE `eh_organization_member_logs` (
  `id` BIGINT(20)  COMMENT 'id of the record',
  `namespace_id` INT(11) DEFAULT '0',
  `organization_id` BIGINT(20) ,
  `user_id` BIGINT(20) COMMENT 'organization member target id (type user)',
  `contact_name` VARCHAR(64) DEFAULT NULL,
  `contact_type` TINYINT(4) DEFAULT '0' COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `operation_type` TINYINT(4) DEFAULT '0' COMMENT '0-退出企业 1-加入企业',
  `request_type` TINYINT(4) DEFAULT '0' COMMENT '0-管理员操作 1-用户操作',
  `operate_time` DATETIME ,
  `operator_uid` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 增加字段  `eh_users`
ALTER TABLE `eh_users` ADD COLUMN `executive_tag` TINYINT(4) DEFAULT '0' COMMENT '0-不是高管 1-是高管';
ALTER TABLE `eh_users` ADD COLUMN `position_tag` VARCHAR(128) DEFAULT NULL COMMENT '职位';
ALTER TABLE `eh_users` ADD COLUMN `identity_number_tag` VARCHAR(20) DEFAULT NULL COMMENT '身份证号';



-- 金蝶数据同步， add by tt, 20161117
ALTER TABLE `eh_buildings` ADD COLUMN `product_type` VARCHAR(128);
ALTER TABLE `eh_buildings` ADD COLUMN `complete_date` DATETIME;
ALTER TABLE `eh_buildings` ADD COLUMN `joinin_date` DATETIME;
ALTER TABLE `eh_buildings` ADD COLUMN `floor_count` VARCHAR(64);
ALTER TABLE `eh_buildings` ADD COLUMN `namespace_building_type` VARCHAR(128);
ALTER TABLE `eh_buildings` ADD COLUMN `namespace_building_token` VARCHAR(128);

-- 下面这种写法速度更快点， add by tt, 20161117
ALTER TABLE `eh_addresses` ADD COLUMN `rent_area` DOUBLE,
  ADD COLUMN `build_area` DOUBLE,
  ADD COLUMN `inner_area` DOUBLE,
  ADD COLUMN `layout` VARCHAR(128),
  ADD COLUMN `living_status` TINYINT,
  ADD COLUMN `namespace_address_type` VARCHAR(128),
  ADD COLUMN `namespace_address_token` VARCHAR(128);

ALTER TABLE `eh_organization_details` ADD COLUMN `service_user_id` BIGINT NULL COMMENT 'customer service staff';
ALTER TABLE `eh_organization_details` ADD COLUMN `namespace_organization_type` VARCHAR(128);
ALTER TABLE `eh_organization_details` ADD COLUMN `namespace_organization_token` VARCHAR(128);

-- 合同表， add by tt, 20161117
-- DROP TABLE IF EXISTS `eh_contracts`;
CREATE TABLE `eh_contracts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `organization_name` VARCHAR(64),
  `contract_number` VARCHAR(128) NOT NULL,
  `contract_end_date` DATETIME NOT NULL,
  `status` TINYINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 合同与楼栋门牌对应表， add by tt, 20161117
-- DROP TABLE IF EXISTS `eh_contract_building_mappings`;
CREATE TABLE `eh_contract_building_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `organization_name` VARCHAR(64),
  `contract_id` BIGINT,
  `contract_number` VARCHAR(128),
  `building_name` VARCHAR(128),
  `apartment_name` VARCHAR(128),
  `area_size` DOUBLE,
  `status` TINYINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- app与namespace映射关系表， add by tt, 20161117
-- DROP TABLE IF EXISTS `eh_app_namespace_mappings`;
CREATE TABLE `eh_app_namespace_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INT(11),
  `app_key` VARCHAR(64),
  `community_id` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- eh_flows
DROP TABLE IF EXISTS `eh_flows`;
CREATE TABLE `eh_flows` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),

  `flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the real flow id for all copy, the first flow_main_id=0',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version',
  `flow_name` VARCHAR(64) NOT NULL COMMENT 'the name of flow',

  `status` TINYINT NOT NULL COMMENT 'invalid, config, running, pending, stop',
  `stop_time` DATETIME NOT NULL COMMENT 'last stop time',
  `run_time` DATETIME NOT NULL COMMENT 'last run time',
  `update_time` DATETIME NOT NULL COMMENT 'last run time',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  `start_node` BIGINT NOT NULL DEFAULT 0,
  `end_node` BIGINT NOT NULL DEFAULT 0,
  `last_node` BIGINT NOT NULL DEFAULT 0,

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_stats`;
CREATE TABLE `eh_flow_stats` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,

  `node_level` INTEGER NOT NULL,
  `running_count` INTEGER NOT NULL,
  `enter_count` INTEGER NOT NULL,
  `leave_count` INTEGER NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_nodes`;
CREATE TABLE `eh_flow_nodes` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `node_name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(1024),
  `node_level` INTEGER NOT NULL,
  `auto_step_minute` INTEGER NOT NULL DEFAULT 0 COMMENT 'after hour, step next',
  `auto_step_type` VARCHAR(64) COMMENT 'ApproveStep, RejectStep, EndStep',
  `allow_applier_update` TINYINT NOT NULL DEFAULT 0 COMMENT 'allow applier update content',
  `allow_timeout_action` TINYINT NOT NULL DEFAULT 0 COMMENT '1: allow timeout action',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `params` VARCHAR(64) COMMENT 'the params from other module',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, valid',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_buttons`;
CREATE TABLE `eh_flow_buttons` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `flow_node_id` BIGINT NOT NULL,
  `button_name` VARCHAR(64),
  `description` VARCHAR(1024),
  `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
  `flow_user_type` VARCHAR(64) COMMENT 'applier, processor',
  `goto_level` INTEGER NOT NULL DEFAULT 0,
  `goto_node_id` BIGINT NOT NULL DEFAULT 0,
  `need_subject` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not need subject for this step, 1: need subject for this step',
  `need_processor` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not need processor, 1: need only one processor',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: disabled, 2: enabled',
  `remind_count` INTEGER NOT NULL DEFAULT 0,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_forms`;
CREATE TABLE `eh_flow_forms` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,

  `form_name` VARCHAR(64),
  `form_type` VARCHAR(64) COMMENT 'text, datetime, checkbox, radiobox, selection',
  `form_default` TEXT,
  `form_render` TEXT,
  `belong_to` BIGINT NOT NULL,
  `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_actions`;
CREATE TABLE `eh_flow_actions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `action_type` VARCHAR(64) NOT NULL COMMENT 'sms, message, tick_sms, tick_message, tracker, scripts',
  `belong_to` BIGINT NOT NULL,
  `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
  `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
  `action_step_type` VARCHAR(64) NOT NULL COMMENT 'step_none, step_timeout, step_enter, step_leave',
  `status` TINYINT NOT NULL COMMENT 'invalid, valid',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `render_text` VARCHAR(256) COMMENT 'the content for this message that have variables',

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_user_selections`;
CREATE TABLE `eh_flow_user_selections` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,

  `select_type` VARCHAR(64) NOT NULL COMMENT 'department, position, manager, variable',
  `source_id_a` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other user object id',
  `source_type_a` VARCHAR(64) COMMENT 'community, organization, user, variable',
  `source_id_b` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other user object id',
  `source_type_b` VARCHAR(64) COMMENT 'community, organization, user, variable',
  `belong_to` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other flow object id',
  `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow, flow_node, flow_button, flow_action',
  `belong_type` VARCHAR(64) NOT NULL COMMENT 'flow_superviser, flow_node_processor, flow_node_applier, flow_button_clicker, flow_action_processor',
  `selection_name` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT 'invalid, valid',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_cases`;
CREATE TABLE `eh_flow_cases` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),
  `module_name` VARCHAR(64),
  `applier_name` VARCHAR(64),
  `applier_phone` VARCHAR(64),

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,

  `apply_user_id` BIGINT NOT NULL,
  `process_user_id` BIGINT NOT NULL DEFAULT 0,
  `refer_id` BIGINT NOT NULL DEFAULT 0,
  `refer_type` VARCHAR(64) NOT NULL,
  `current_node_id` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, initial, process, end',
  `reject_count` INTEGER NOT NULL DEFAULT 0,
  `reject_node_id` BIGINT NOT NULL DEFAULT 0,
  `step_count` BIGINT NOT NULL DEFAULT 0,
  `last_step_time` DATETIME NOT NULL COMMENT 'state change time',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `case_type` VARCHAR(64) COMMENT 'inner, outer etc',
  `content` TEXT,
  `evaluate_score` INTEGER NOT NULL DEFAULT 0,

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_event_logs`;
CREATE TABLE `eh_flow_event_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `flow_node_id` BIGINT NOT NULL DEFAULT 0,
  `flow_case_id` BIGINT NOT NULL DEFAULT 0,
  `flow_button_id` BIGINT NOT NULL DEFAULT 0,
  `flow_action_id` BIGINT NOT NULL DEFAULT 0,
  `flow_user_id` BIGINT NOT NULL DEFAULT 0,
  `flow_user_name` VARCHAR(64),
  `flow_selection_id` BIGINT NOT NULL DEFAULT 0,
  `subject_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the post id for this event',
  `step_count` BIGINT NOT NULL DEFAULT 0,
  `log_type` VARCHAR(64) NOT NULL COMMENT 'flow_step, button_click, action_result',
  `log_title` VARCHAR(64) COMMENT 'the title of this log',
  `log_content` TEXT,
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_variables`;
CREATE TABLE `eh_flow_variables` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,

  `name` VARCHAR(64),
  `label` VARCHAR(64),
  `var_type` VARCHAR(64) NOT NULL COMMENT 'text, node_user',
  `script_type` VARCHAR(64) NOT NULL COMMENT 'bean_id, prototype',
  `script_cls` VARCHAR(1024) NOT NULL COMMENT 'the class prototype in java',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_evaluates`;
CREATE TABLE `eh_flow_evaluates` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,

  `star` TINYINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `flow_node_id` BIGINT NOT NULL,
  `flow_case_id` BIGINT NOT NULL,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- scripts for this module
DROP TABLE IF EXISTS `eh_flow_scripts`;
CREATE TABLE `eh_flow_scripts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,

  `name` VARCHAR(64) NOT NULL,
  `script_type` VARCHAR(64) NOT NULL COMMENT 'bean_id, prototype',
  `script_cls` VARCHAR(1024) NOT NULL COMMENT 'the class prototype in java',
  `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
  `step_type` VARCHAR(64) NOT NULL COMMENT 'step_none, step_timeout, step_enter, step_leave',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_subjects`;
CREATE TABLE `eh_flow_subjects` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `title` VARCHAR(64),
  `content` TEXT,

  `belong_to` BIGINT NOT NULL,
  `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_attachments`;
CREATE TABLE `eh_flow_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_timeouts`;
CREATE TABLE `eh_flow_timeouts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `belong_to` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other flow object id',
  `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow, flow_node, flow_button, flow_action',
  `timeout_type` VARCHAR(64) NOT NULL COMMENT 'flow_step_timeout',
  `timeout_tick` DATETIME NOT NULL,
  `json` TEXT,
  `create_time` DATETIME NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



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

