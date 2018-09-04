-- AUTHOR: 梁燕龙 20180830
-- REMARK: 增加字段判断认证的来源
ALTER TABLE `eh_organization_members` ADD COLUMN `source_type` TINYINT COMMENT '认证来源';
