-- 运营后台数据版本
CREATE TABLE `eh_portal_versions` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sync_time` datetime DEFAULT NULL,
  `publish_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0-init,1-edit,2-publis success, 3-publish fail',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_portal_layouts` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_item_categories` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_items` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `origin_id`  bigint(20) NULL AFTER `version_id`;
ALTER TABLE `eh_portal_publish_logs` ADD COLUMN `version_id`  bigint(20) NULL AFTER `namespace_id`;
ALTER TABLE `eh_portal_publish_logs` ADD COLUMN `process`  int(11) NULL AFTER `version_id`;


ALTER TABLE `eh_web_menu_scopes` ADD COLUMN `app_id`  bigint(20) NULL COMMENT 'eh_service_module_app id';
ALTER TABLE `eh_web_menu_scopes` DROP INDEX `u_menu_scope_owner` , ADD UNIQUE INDEX `u_menu_scope_owner` (`menu_id`, `owner_type`, `owner_id`, `app_id`) USING BTREE ;