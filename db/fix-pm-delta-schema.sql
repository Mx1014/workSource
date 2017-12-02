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
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'status of plans  0:waitting for starting 1: waitting for approving  2: Active 3:inActive',
  `review_result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:none, 1:qualified 2:unqualified 3:review_delay',
  `repeat_setting_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refers to eh_repeatsetting ',
  `remarks` text,
  `plan_version` bigint(20) DEFAULT NULL COMMENT 'the version of plan for modifying plan',
  `plan_main_id` bigint(20) DEFAULT NULL COMMENT 'refer to old version plan for modifying plan',
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



-- 增加设备字段
ALTER TABLE `ehcore`.`eh_equipment_inspection_equipments`
ADD COLUMN `brand_name` varchar(1024) COMMENT 'brand_name',
ADD COLUMN `construction_party` varchar(1024) COMMENT 'construction party',
ADD COLUMN `discard_time` datetime COMMENT 'discard time ',
ADD COLUMN `manager_contact` varchar(1024) ,
ADD COLUMN `detail` varchar(1024) ,
ADD COLUMN `factory_time` datetime ,
ADD COLUMN `provenance` varchar(1024) ,
ADD COLUMN `price` decimal  ,
ADD COLUMN `buy_time` datetime ,
ADD COLUMN `depreciation_years` bigint(10) COMMENT '折旧年限' ;

-- eh_equipment_inspection_tasks 增加plan_id字段 用于关联task和equipments
ALTER TABLE `ehcore`.`eh_equipment_inspection_tasks`
ADD COLUMN `plan_id`  bigint(20) NOT NULL ;


-- 标准增加周期类型
ALTER TABLE `ehcore`.`eh_equipment_inspection_standards`
ADD COLUMN `repeat_type` tinyint(4) NOT NULL COMMENT ' 0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year';

