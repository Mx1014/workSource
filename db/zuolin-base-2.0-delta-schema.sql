 -- 应用公司项目授权表 by lei.lv
 CREATE TABLE `eh_service_module_app_authorizations` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) DEFAULT NULL,
  `organization_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'organization_id',
  `project_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'community_id',
  `app_id` BIGINT(20)  NOT NULL DEFAULT '0' COMMENT 'app_id',
  `control_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'control type',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_organization_apps` (
  `id` bigint(20) NOT NULL,
  `app_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `app_type` varchar(255) DEFAULT NULL,
  `alias_name` varchar(255) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_organization_apps` ADD INDEX `org_app_orgid` (`org_id`) ;
ALTER TABLE `eh_organization_apps` ADD INDEX `org_app_appid` (`app_id`) ;


CREATE TABLE `eh_app_community_config` (
  `id` bigint(20) NOT NULL,
  `organization_app_id` bigint(20) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `visibilityFlag` tinyint(4) DEFAULT NULL,
  `display_name` varchar(255) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operator_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_app_community_config` ADD INDEX `organization_app_id` (`organization_app_id`) ;
ALTER TABLE `eh_app_community_config` ADD INDEX `community_id` (`community_id`) ;