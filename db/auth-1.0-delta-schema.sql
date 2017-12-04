ALTER TABLE `eh_service_modules` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;


ALTER TABLE `eh_authorizations` ADD COLUMN `module_app_id`  bigint(20) COMMENT 'eh_service_module_apps id';
ALTER TABLE `eh_authorizations` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `all_control_flag` tinyint(4) DEFAULT 0 COMMENT '0 not all, 1 all' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `control_id` bigint(20) ;
ALTER TABLE `eh_authorizations` ADD COLUMN `control_option` tinyint(4);
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

-- ----------------------------
-- Table structure for eh_reflection_service_module_apps
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_reflection_service_module_apps`;
CREATE TABLE `eh_reflection_service_module_apps` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `active_app_id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(64) DEFAULT NULL,
  `module_id` bigint(20) DEFAULT NULL,
  `instance_config` text COMMENT '应用入口需要的配置参数',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `action_type` tinyint(4) DEFAULT NULL,
  `action_data` text COMMENT 'the parameters depend on item_type, json format',
  `update_time` datetime DEFAULT NULL,
  `module_control_type` varchar(64) DEFAULT '' COMMENT 'community_control;org_control;unlimit',
  `multiple_flag` tinyint(4) DEFAULT NULL,
  `custom_tag` varchar(64) DEFAULT '',
  `custom_path` varchar(128) DEFAULT '',
  `menu_id` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5007 DEFAULT CHARSET=utf8mb4;


-- 权限细化改造 by lei.lv
ALTER TABLE `eh_authorization_relations` ADD COLUMN `app_id` bigint(20);