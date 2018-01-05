-- 运营后台数据版本
CREATE TABLE `eh_portal_versions` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `sync_version` int(11) DEFAULT NULL,
  `publish_version` int(11) DEFAULT NULL,
  `sync_time` datetime DEFAULT NULL,
  `publish_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0-init,1-edit,2-publis success, 3-publish fail',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_portal_content_scopes` ADD COLUMN `version_id`  bigint(20) NULL;
ALTER TABLE `eh_portal_item_categories` ADD COLUMN `version_id`  bigint(20) NULL;
ALTER TABLE `eh_portal_item_groups` ADD COLUMN `version_id`  bigint(20) NULL;
ALTER TABLE `eh_portal_items` ADD COLUMN `version_id`  bigint(20) NULL;
ALTER TABLE `eh_portal_launch_pad_mappings` ADD COLUMN `version_id`  bigint(20) NULL;
ALTER TABLE `eh_portal_layouts` ADD COLUMN `version_id`  bigint(20) NULL;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `version_id`  bigint(20) NULL;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `origin_id`  bigint(20) NULL;
