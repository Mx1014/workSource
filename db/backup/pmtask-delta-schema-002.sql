
ALTER TABLE eh_pm_tasks ADD COLUMN `operator_star` TINYINT NOT NULL DEFAULT 0 COMMENT 'task star of operator';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_type` TINYINT COMMENT '1: family , 2:organization';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_org_id` BIGINT NOT NUll DEFAULT 0 COMMENT 'organization of address';

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;






