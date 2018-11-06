 
-- AUTHOR: 吴寒
-- REMARK: 公告1.8 修改表结构
ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是';
ALTER TABLE `eh_enterprise_notices` ADD COLUMN `stick_time` DATETIME;