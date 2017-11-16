ALTER TABLE `eh_service_modules` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;


ALTER TABLE `eh_authorizations` ADD COLUMN `module_app_id`  bigint(20) COMMENT 'eh_service_module_apps id';
ALTER TABLE `eh_authorizations` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `all_control_flag` tinyint(4) DEFAULT 0 COMMENT '0 not all, 1 all' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `control_id` bigint(20) ;
ALTER TABLE `eh_authorizations` ADD COLUMN `control_option` bigint(20) ;
-- eh_authorization_control_configs
-- DROP TABLE IF EXISTS `eh_authorization_control_configs`;
CREATE TABLE `eh_authorization_control_configs` (
  `id` bigint(20) NOT NULL,
  `control_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `target_type` varchar(32) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `include_child_flag` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 应用表改造 by lei.lv
ALTER TABLE `eh_service_module_apps` ADD COLUMN `custom_tag` varchar(64)  DEFAULT '';
ALTER TABLE `eh_service_module_apps` ADD COLUMN `custom_path` varchar(128) DEFAULT '';