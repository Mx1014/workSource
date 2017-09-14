-- 薪酬组加上版本 By lei.lv 
ALTER TABLE eh_uniongroup_configures ADD COLUMN `version_code` INT COMMENT '版本号';
ALTER TABLE eh_uniongroup_member_details ADD COLUMN `version_code` INT COMMENT '版本号';