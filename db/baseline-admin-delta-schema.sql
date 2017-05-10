ALTER TABLE `eh_web_menus` ADD `level` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_web_menus` ADD `condition_type` varchar(32) DEFAULT NULL;
ALTER TABLE `eh_web_menus` ADD `style` varchar(32) DEFAULT NULL;

ALTER TABLE `eh_acl_roles` ADD `creator_uid` bigint(20) NOT NULL COMMENT 'record creator uid';
ALTER TABLE `eh_acl_roles` ADD `create_time` datetime DEFAULT NULL COMMENT 'record create time';