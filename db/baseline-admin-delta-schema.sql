ALTER TABLE `eh_web_menus` ADD `level` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_web_menus` ADD `condition_type` varchar(32) DEFAULT NULL;
ALTER TABLE `eh_web_menus` ADD `category` varchar(32) DEFAULT NULL;

ALTER TABLE `eh_service_module_assignments` ADD `all_flag` tinyint(4) COMMENT '0 not all, 1 all';
ALTER TABLE `eh_service_module_assignments` ADD `include_child_flag` tinyint(4) COMMENT '0 not include, 1 include';