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


-- eh_equipment_inspection_tasks 增加plan_id字段 用于关联task和equipments
ALTER TABLE eh_equipment_inspection_tasks
ADD COLUMN `plan_id`  bigint(20) NOT NULL ;


-- 标准增加周期类型
ALTER TABLE eh_equipment_inspection_standards
ADD COLUMN `repeat_type` tinyint(4) NOT NULL COMMENT ' 0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year';
-- 操作记录表增加设备id表
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `equipment_id`  bigint(20) NULL DEFAULT 0 ;
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `maintance_type`  varchar(255) NULL DEFAULT '';
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `flow_case_id`  bigint(20) NULL AFTER `equipment_id`;
ALTER TABLE `eh_equipment_inspection_task_logs`
  ADD COLUMN `maintance_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed';

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

-- by st.zheng
ALTER TABLE `eh_pm_tasks`
  ADD COLUMN `refer_type` VARCHAR(32) NULL COMMENT '引用类型' AFTER `if_use_feelist`;
ALTER TABLE `eh_pm_tasks`
  ADD COLUMN `refer_id` BIGINT(20) NULL COMMENT '引用id' AFTER `refer_type`;

-- 新增权限  by jiarui 20180205

DELETE from  eh_service_module_privileges
WHERE  privilege_id IN (30070,30076,30071,30077,30078,30079);

UPDATE eh_service_modules
SET name = '巡检计划'
WHERE id = 20840;

INSERT INTO `eh_acl_privileges` VALUES ('30083', '0', '设备巡检 巡检计划创建', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` VALUES ('30084', '0', '设备巡检 巡检计划修改', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` VALUES ('30085', '0', '设备巡检 巡检计划查看', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` VALUES ('30086', '0', '设备巡检 巡检计划删除', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` VALUES ('30087', '0', '设备巡检 巡检计划审批', '设备巡检 业务模块权限', NULL);

set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30083, '设备巡检 巡检计划创建', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30084, '设备巡检 巡检计划修改', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30085, '设备巡检 巡检计划查看', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30086, '设备巡检 巡检计划删除', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30087, '设备巡检 巡检计划审批', '0', NOW());

-- 新增权限  by jiarui 20180205
