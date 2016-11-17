
ALTER TABLE eh_pm_tasks ADD COLUMN `operator_star` TINYINT NOT NULL DEFAULT 0 COMMENT 'task star of operator';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_type` TINYINT COMMENT '1: family , 2:organization';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_org_id` BIGINT NOT NUll DEFAULT 0 COMMENT 'organization of address';


INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES ('205', 'pmtask.notification', '7', 'zh_CN', '任务操作模版', '${creatorName} ${creatorPhone}已发起一个${categoryName}单，请尽快处理', '0');


