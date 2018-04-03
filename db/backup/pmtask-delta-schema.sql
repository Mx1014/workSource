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


INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('313', 'pmtask.notification', '8', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone} 已回访该任务', '0');
