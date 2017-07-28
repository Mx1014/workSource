ALTER TABLE `eh_web_menus` ADD `level` int(11) NOT NULL DEFAULT '0';
ALTER TABLE `eh_web_menus` ADD `condition_type` varchar(32) DEFAULT NULL;
ALTER TABLE `eh_web_menus` ADD `category` varchar(32) DEFAULT NULL;

ALTER TABLE `eh_service_module_assignments` ADD `all_module_flag` tinyint(4) COMMENT '0 not all, 1 all';
ALTER TABLE `eh_service_module_assignments` ADD `include_child_flag` tinyint(4) COMMENT '0 not include, 1 include';
ALTER TABLE `eh_service_module_assignments` ADD `relation_id` bigint(20) NOT NULL;

ALTER TABLE `eh_acl_roles` ADD COLUMN `creator_uid` BIGINT DEFAULT 0 COMMENT 'creator uid' ;
ALTER TABLE `eh_acl_roles` ADD COLUMN `create_time` DATETIME DEFAULT now() COMMENT 'record create time';

ALTER TABLE `eh_acl_roles` ADD INDEX `i_eh_acl_role_creator_uid`(`creator_uid`);
ALTER TABLE `eh_acl_roles` ADD INDEX `i_eh_acl_role_create_time`(`create_time`);


-- 授权表，包括模块管理员角色管理员授权
CREATE TABLE `eh_authorizations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `target_type` varchar(32) NOT NULL COMMENT 'EhOrganizations, EhUsers',
  `target_id` bigint(20) NOT NULL,
  `target_name` varchar(128),
  `owner_type` varchar(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` bigint(20) NOT NULL,
  `auth_type` varchar(64) NOT NULL COMMENT 'EhServiceModules, EhRoles',
  `auth_id` bigint(20) NOT NULL,
  `identity_type` varchar(64) NOT NULL COMMENT 'manage, ordinary',
  `all_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `scope` varchar(128) DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 用户授权关系
CREATE TABLE `eh_authorization_relations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` bigint(20) NOT NULL,
  `module_id` bigint(20) NOT NULL,
  `target_json` text,
  `project_json` text,
  `privilege_json` text,
  `all_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `all_project_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 业务授权关系授权表
CREATE TABLE `eh_service_module_assignment_relations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` bigint(20) NOT NULL,
  `all_module_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `all_project_flag` tinyint(4) COMMENT '0 not all, 1 all',
  `target_json` text,
  `project_json` text,
  `module_json` text,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 域名配置表
CREATE TABLE `eh_domains` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `portal_type` varchar(32) NOT NULL COMMENT 'zuolin, pm, enterprise, user',
  `portal_id` bigint(20) NOT NULL,
  `domain` varchar(32) NOT NULL COMMENT 'domain',
  `create_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
