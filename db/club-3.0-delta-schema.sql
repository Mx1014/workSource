-- 物业报修2.8 add by sw 20170306
ALTER TABLE eh_pm_tasks ADD COLUMN `building_name` VARCHAR(128);
ALTER TABLE eh_pm_tasks ADD COLUMN `organization_uid` BIGINT;

ALTER TABLE eh_pm_task_targets ADD COLUMN `create_time` DATETIME;
ALTER TABLE eh_pm_task_targets ADD COLUMN `creator_uid` BIGINT;

-- 修改dataType 长度 add by sw 20170306
ALTER TABLE eh_web_menus MODIFY COLUMN data_type VARCHAR (256);


-- 活动报名表添加活动报名信息等字段, add by tt, 20170227
ALTER TABLE `eh_activities` ADD COLUMN `signup_end_time` DATETIME;
ALTER TABLE `eh_activity_roster` ADD COLUMN `phone` VARCHAR(32);
ALTER TABLE `eh_activity_roster` ADD COLUMN `real_name` VARCHAR(128);
ALTER TABLE `eh_activity_roster` ADD COLUMN `gender` TINYINT;
ALTER TABLE `eh_activity_roster` ADD COLUMN `community_name` VARCHAR(64);
ALTER TABLE `eh_activity_roster` ADD COLUMN `organization_name` VARCHAR(128);
ALTER TABLE `eh_activity_roster` ADD COLUMN `position` VARCHAR(64);
ALTER TABLE `eh_activity_roster` ADD COLUMN `leader_flag` TINYINT;
ALTER TABLE `eh_activity_roster` ADD COLUMN `source_flag` TINYINT;

ALTER TABLE `eh_activity_roster` DROP INDEX `u_eh_act_roster_user`;


-- 对接映射表 add by sw 20170302
CREATE TABLE `eh_docking_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `scope` VARCHAR(64) NOT NULL,
  `name` VARCHAR(256),
  `mapping_value` VARCHAR(256),
  `mapping_json` VARCHAR(1024),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;