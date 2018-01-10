-- domain 增加name, icon
ALTER TABLE `eh_domains` ADD COLUMN `icon_uri`  varchar(255) NULL AFTER `create_time`;
ALTER TABLE `eh_domains` ADD COLUMN `name`  varchar(255) NULL AFTER `namespace_id`;

ALTER TABLE `eh_web_menu_scopes` ADD COLUMN `app_id`  bigint(20) NULL COMMENT 'eh_service_module_app id';
ALTER TABLE `eh_web_menu_scopes` DROP INDEX `u_menu_scope_owner` , ADD UNIQUE INDEX `u_menu_scope_owner` (`menu_id`, `owner_type`, `owner_id`, `app_id`) USING BTREE ;