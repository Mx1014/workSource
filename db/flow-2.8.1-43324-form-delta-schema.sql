
-- AUTHOR: 胡琪
-- REMARK: 工作流-2.8.1，工作流节点关联的全局表单字段配置信息
CREATE TABLE `eh_general_form_fields_config` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `module_id` bigint(20) DEFAULT NULL COMMENT 'the module id',
  `module_type` varchar(64) DEFAULT NULL,
  `project_id` bigint(20) NOT NULL DEFAULT '0',
  `project_type` varchar(64) NOT NULL DEFAULT 'EhCommunities',
  `form_origin_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'The id of the original form',
  `form_version` bigint(20) NOT NULL DEFAULT '0',
  `config_type` varchar(64) NOT NULL COMMENT '表单节点配置类型',
  `form_fields` text COMMENT 'json 存放表单字段',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: invalid, 1: valid',
  `create_time` datetime(3) NOT NULL COMMENT 'record create time',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime(3) DEFAULT NULL COMMENT 'record update time',
  `updater_uid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE `eh_general_form_fields_config`;

-- AUTHOR: 胡琪
-- REMARK: 工作流-2.8.1，工作流节点关联的全局表单字段配置信息
CREATE TABLE `eh_general_form_fields_config` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `module_id` bigint(20) DEFAULT NULL COMMENT 'the module id',
  `module_type` varchar(64) DEFAULT NULL,
  `project_id` bigint(20) NOT NULL DEFAULT '0',
  `project_type` varchar(64) NOT NULL DEFAULT 'EhCommunities',
  `form_origin_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'The id of the original form',
  `form_version` bigint(20) NOT NULL DEFAULT '0',
  `config_type` varchar(64) NOT NULL COMMENT '表单节点配置类型',
  `form_fields` text COMMENT 'json 存放表单字段',
  `status` tinyint(4) NOT NULL COMMENT '0: invalid, 1: config, 2: running',
  `create_time` datetime(3) NOT NULL COMMENT 'record create time',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `update_time` datetime(3) DEFAULT NULL COMMENT 'record update time',
  `updater_uid` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;