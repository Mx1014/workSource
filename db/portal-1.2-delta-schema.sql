-- 运营后台数据版本
CREATE TABLE `eh_portal_versions` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `big_version` int(11) DEFAULT NULL,
  `minor_version` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `sync_time` datetime DEFAULT NULL,
  `publish_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0-init,1-edit,2-publis success, 3-publish fail',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 预览版本可见用户表
CREATE TABLE `eh_portal_version_users` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `version_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `u_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_portal_layouts` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_item_categories` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_items` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `origin_id`  bigint(20) NULL AFTER `version_id`;
ALTER TABLE `eh_portal_publish_logs` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_publish_logs` ADD COLUMN `process`  int(11) NULL AFTER `version_id`;

ALTER TABLE `eh_web_menu_scopes` ADD COLUMN `config_id`  bigint(20) NULL COMMENT 'get config, eg multiple application.';

ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `preview_portal_version_id`  bigint(20) NULL COMMENT '预览版本的id，正式版本数据不要配置该数据';
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `preview_portal_version_id`  bigint(20) NULL COMMENT '预览版本的id，正式版本数据不要配置该数据';
ALTER TABLE `eh_item_service_categries` ADD COLUMN `preview_portal_version_id`  bigint(20) NULL COMMENT '预览版本的id，正式版本数据不要配置该数据';

ALTER TABLE `eh_web_menus` ADD COLUMN `config_type`  tinyint(4) NULL COMMENT 'null, 1-config by application, 2-all namespace have';