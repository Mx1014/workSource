
ALTER TABLE eh_pm_tasks ADD COLUMN `operator_star` TINYINT NOT NULL DEFAULT 0 COMMENT 'task star of operator';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_type` TINYINT COMMENT '1: family , 2:organization';
