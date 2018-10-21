-- AUTHOR: 黄明波
-- REMARK: 服务联盟通用配置修复
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `enable_provider` TINYINT NOT NULL DEFAULT '0' COMMENT '0-关闭服务商功能 1-开启' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `enable_comment` TINYINT NOT NULL DEFAULT '0' COMMENT '0-关闭评论功能 1-开启评论功能' ;
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `description` MEDIUMTEXT NULL COMMENT '首页样式描述文字';
