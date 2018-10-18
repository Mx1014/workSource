-- AUTHOR: 梁燕龙
-- REMARK: 微信分享配置中增加主题色字段
ALTER TABLE `eh_app_urls` ADD COLUMN `theme_color` VARCHAR(64) COMMENT '主题色';
ALTER TABLE `eh_app_urls` ADD COLUMN `package_name` VARCHAR(64) COMMENT '包名';