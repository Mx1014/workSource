ALTER TABLE `eh_web_menus` ADD `level` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_web_menus` ADD `condition_type` varchar(32) DEFAULT NULL;
ALTER TABLE `eh_web_menus` ADD `category` varchar(32) DEFAULT NULL;

ALTER TABLE `eh_service_module_assignments` ADD `all_flag` tinyint(4) COMMENT '0 not all, 1 all';
ALTER TABLE `eh_service_module_assignments` ADD `include_child_flag` tinyint(4) COMMENT '0 not include, 1 include';

ALERT TABLE `ehcore`.`eh_acl_roles` ADD COLUMN `creator_uid` BIGINT COMMENT 'creator uid';
ALERT TABLE `ehcore`.`eh_acl_roles` ADD COLUMN `create_time` DATETIME COMMENT 'record create time';

ALTER TABLE `ehcore`.`eh_acl_roles` ADD INDEX `i_eh_acl_role_creator_uid`(`creator_uid`);
ALTER TABLE `ehcore`.`eh_acl_roles` ADD INDEX `i_eh_acl_role_create_time`(`create_time`);
