

-- 薪酬组加上版本 By lei.lv 
ALTER TABLE eh_uniongroup_configures ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '版本号';
ALTER TABLE eh_uniongroup_member_details ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '版本号';

-- 打卡加入当前生效版本号
ALTER TABLE eh_punch_rules ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '当前生效版本号';