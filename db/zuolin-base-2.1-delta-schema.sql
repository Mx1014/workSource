
-- 模块icon
ALTER TABLE `eh_service_modules` ADD COLUMN `icon_uri`  varchar(255) NULL;

-- 分类结构
ALTER TABLE `eh_second_app_types` ADD COLUMN `parent_id`  bigint(22) NOT NULL DEFAULT 0 ;
ALTER TABLE `eh_second_app_types` ADD COLUMN `location_type`  tinyint(4) NULL COMMENT '参考枚举ServiceModuleLocationType';
ALTER TABLE `eh_second_app_types` ADD COLUMN `default_order`  bigint(22) NULL DEFAULT 0;



CREATE TABLE `eh_app_categories` (
  `id` bigint(22) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` bigint(22) NOT NULL DEFAULT '0',
  `location_type` tinyint(4) DEFAULT NULL COMMENT '参考枚举ServiceModuleLocationType',
  `app_type` tinyint(4) DEFAULT NULL COMMENT '一级分类，0-oa，1-community，2-service。参考ServiceModuleAppType',
  `default_order` bigint(22) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;