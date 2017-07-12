-- merge from activity-2.0.0-delta-schema.sql by sql 20170119
-- 增加主题分类id，added by tt, 20170106
ALTER TABLE `eh_activities` ADD COLUMN `content_category_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'content category id';

-- 增加活动分类及子分类，因为后台管理接口还是从帖子这里查的，added by tt, 20170116
ALTER TABLE `eh_forum_posts` ADD COLUMN `activity_category_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'activity category id';
ALTER TABLE `eh_forum_posts` ADD COLUMN `activity_content_category_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'activity content category id';

-- 增加一些字段用于主题分类，added by tt, 20170106
-- 此表对于parent_id为0的表示入口id，否则表示主题分类id
ALTER TABLE `eh_activity_categories` ADD COLUMN `enabled` TINYINT NOT NULL DEFAULT '1' COMMENT '0: no, 1: yes';
ALTER TABLE `eh_activity_categories` ADD COLUMN `icon_uri` VARCHAR(1024) NULL;
ALTER TABLE `eh_activity_categories` ADD COLUMN `selected_icon_uri` VARCHAR(1024) NULL;
ALTER TABLE `eh_activity_categories` ADD COLUMN `show_name` VARCHAR(64) NULL;
ALTER TABLE `eh_activity_categories` ADD COLUMN `all_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: no, 1: yes';

-- 增加选中时的图片，added by tt, 20170106
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `selected_icon_uri` VARCHAR(1024) NULL DEFAULT NULL;




-- merge from equipment2.1-delta-schema.sql by lqs 20170119
-- DROP TABLE IF EXISTS `eh_equipment_inspection_categories`;
CREATE TABLE `eh_equipment_inspection_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `deletor_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_equipment_inspection_equipments` ADD COLUMN `inspection_category_id` BIGINT;
ALTER TABLE `eh_equipment_inspection_standards` ADD COLUMN `inspection_category_id` BIGINT;
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `inspection_category_id` BIGINT;
ALTER TABLE `eh_equipment_inspection_equipments` ADD COLUMN `namespace_id` INTEGER;
ALTER TABLE `eh_equipment_inspection_standards` ADD COLUMN `namespace_id` INTEGER;
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `namespace_id` INTEGER;
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, etc';
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `target_id` BIGINT NOT NULL DEFAULT '0';
ALTER TABLE `eh_equipment_inspection_tasks` ADD COLUMN `position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions';


  
-- DROP TABLE IF EXISTS `eh_equipment_inspection_standard_group_map`;
CREATE TABLE `eh_equipment_inspection_standard_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `group_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: executive group, 2: review group',
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_standards',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;





-- merge from authorization-delta-schema.sql by lqs 20170120
ALTER TABLE `eh_service_module_assignments` ADD COLUMN `assignment_type` TINYINT NOT NULL DEFAULT '0';


-- 地址表添加两列存储电商使用的楼栋和门牌， add by tt, 20170213（这样写速度快点）
ALTER TABLE `eh_addresses` ADD COLUMN `business_building_name` VARCHAR(128),
	ADD COLUMN `business_apartment_name` VARCHAR(128);
    
-- 设备巡检和品质核查任务表建索引 add by xiongying20170215
ALTER TABLE eh_equipment_inspection_tasks ADD INDEX(standard_id);
ALTER TABLE eh_equipment_inspection_tasks ADD INDEX(status);
ALTER TABLE eh_equipment_inspection_tasks ADD INDEX(target_id);
ALTER TABLE eh_equipment_inspection_tasks ADD INDEX(inspection_category_id);
ALTER TABLE eh_equipment_inspection_tasks ADD INDEX(executive_expire_time);
ALTER TABLE eh_equipment_inspection_tasks ADD INDEX(process_expire_time);
ALTER TABLE eh_equipment_inspection_tasks ADD INDEX(operator_id); 

ALTER TABLE eh_quality_inspection_tasks ADD INDEX(standard_id);
ALTER TABLE eh_quality_inspection_tasks ADD INDEX(status);
ALTER TABLE eh_quality_inspection_tasks ADD INDEX(target_id);
ALTER TABLE eh_quality_inspection_tasks ADD INDEX(executive_expire_time);
ALTER TABLE eh_quality_inspection_tasks ADD INDEX(process_expire_time); 
ALTER TABLE eh_quality_inspection_tasks ADD INDEX(operator_id); 

-- fix bug6188 add by xiongying20170216
ALTER TABLE eh_equipment_inspection_templates ALTER COLUMN name SET DEFAULT '';

-- 设备-标准关系表新增字段记录最近一次生成任务的时间 add by xiongying20170217
ALTER TABLE `eh_equipment_inspection_equipment_standard_map` ADD COLUMN `last_create_task_time` DATETIME;
