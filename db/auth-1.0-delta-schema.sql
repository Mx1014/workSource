ALTER TABLE `eh_service_modules` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;
ALTER TABLE `eh_service_module_apps` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;


ALTER TABLE `eh_authorizations` ADD COLUMN `module_app_id`  bigint(20) COMMENT 'eh_service_module_apps id';
ALTER TABLE `eh_authorizations` ADD COLUMN `module_control_type`  varchar(64)  DEFAULT '' COMMENT 'community_control;org_control;unlimit' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `all_community_control_flag` tinyint(4) DEFAULT 0 COMMENT '0 not all, 1 all' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `community_control_json` text ;
ALTER TABLE `eh_authorizations` ADD COLUMN `all_org_control_flag` tinyint(4) DEFAULT 0 COMMENT '0 not all, 1 all' ;
ALTER TABLE `eh_authorizations` ADD COLUMN `org_control` text ;