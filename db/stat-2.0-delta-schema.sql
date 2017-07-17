--
-- 设备日志表
--
-- DROP TABLE IF EXISTS `eh_stat_event_device_logs`;
CREATE TABLE `eh_stat_event_device_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `app_version` VARCHAR(64) NOT NULL COMMENT 'app version',
  `app_version_code` INTEGER NOT NULL DEFAULT 0 COMMENT 'app version code',
  `device_id` VARCHAR(128) NOT NULL COMMENT 'device id',
  `device_brand` VARCHAR(64) NOT NULL COMMENT 'brand',
  `device_model` VARCHAR(64) COMMENT 'cellPhone model model',
  `os_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: Android, 2: ios',
  `os_version` VARCHAR(64) COMMENT 'system version',
  `access` VARCHAR(64) NOT NULL COMMENT 'access',
  `imei` VARCHAR(64) NOT NULL COMMENT 'imei',
  `client_ip` VARCHAR(64) NOT NULL COMMENT 'ip address',
  `server_ip` VARCHAR(64) NOT NULL COMMENT 'ip address',
  `country` VARCHAR(64) NOT NULL COMMENT 'country',
  `language` VARCHAR(64) NOT NULL COMMENT 'language',
  `mc` VARCHAR(64) NOT NULL COMMENT 'mc',
  `resolution` VARCHAR(64) NOT NULL COMMENT 'resolution',
  `timezone` VARCHAR(64) NOT NULL COMMENT 'timezone',
  `carrier` VARCHAR(64) NOT NULL COMMENT 'carrier',
  `version_realm` VARCHAR(64) NOT NULL COMMENT 'version_realm',
  `device_time` BIGINT NOT NULL COMMENT 'device time',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 日志上传策略表
--
-- DROP TABLE IF EXISTS `eh_stat_event_upload_strategies`;
CREATE TABLE `eh_stat_event_upload_strategies` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'e.g: EhUsers',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner identifier',
  `access` VARCHAR(32) NOT NULL COMMENT 'WIFI, GSM',
  `scope` INTEGER NOT NULL COMMENT '1: GENERAL_EVENT, 2: LOG_FILE',
  `strategy` VARCHAR(32) NOT NULL COMMENT 'NO, STARTUP, INTERVAL, IMMEDIATE',
  `interval_seconds` INTEGER COMMENT 'interval seconds',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件表
--
-- DROP TABLE IF EXISTS `eh_stat_events`;
CREATE TABLE `eh_stat_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `event_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '1: general event',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_display_name` VARCHAR(64) NOT NULL COMMENT 'event name',
  `event_version` TINYINT NOT NULL DEFAULT 1,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件参数表
--
-- DROP TABLE IF EXISTS `eh_stat_event_params`;
CREATE TABLE `eh_stat_event_params` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `event_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '1: general event',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `multiple` INTEGER NOT NULL DEFAULT 1 COMMENT 'multiple',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `param_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: string param, 2: value param',
  `param_key` VARCHAR(64) NOT NULL COMMENT 'param key',
  `param_name` VARCHAR(64) NOT NULL COMMENT 'param name',
  `event_version` TINYINT NOT NULL DEFAULT 1,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件记录表
--
-- DROP TABLE IF EXISTS `eh_stat_event_log_contents`;
CREATE TABLE `eh_stat_event_log_contents` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `content` TEXT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件日志表
--
-- DROP TABLE IF EXISTS `eh_stat_event_logs`;
CREATE TABLE `eh_stat_event_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `device_gen_id` BIGINT NOT NULL COMMENT 'id of device generate',
  `device_time` BIGINT NOT NULL COMMENT 'device time',
  `acc` INTEGER COMMENT 'acc',
  `event_version` TINYINT NOT NULL DEFAULT 1,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件参数日志表
--
-- DROP TABLE IF EXISTS `eh_stat_event_param_logs`;
CREATE TABLE `eh_stat_event_param_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_log_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_stat_event_logs id',
  `param_key` VARCHAR(64) COMMENT 'key',
  `string_value` VARCHAR(64) COMMENT 'string value',
  `number_value` INTEGER COMMENT 'number value',
  `event_version` TINYINT NOT NULL DEFAULT 1,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件统计表
--
-- DROP TABLE IF EXISTS `eh_stat_event_statistics`;
# CREATE TABLE `eh_stat_event_statistics` (
#   `id` BIGINT NOT NULL,
#   `namespace_id` INTEGER NOT NULL DEFAULT 0,
#   `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
#   `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
#   `event_display_name` VARCHAR(64) NOT NULL COMMENT 'event display name',
#   `event_config_stat_id` VARCHAR(64) NOT NULL COMMENT 'event_config_stat_id',
#   `time_interval` VARCHAR(32) NOT NULL COMMENT 'HOURLY, DAILY, WEEKLY, MONTHLY',
#   `stat_date` DATE NOT NULL COMMENT 'stat date',
#   `stat_hour` INTEGER COMMENT 'stat hour',
#   `total_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'event total count',
#   `unique_users` BIGINT NOT NULL DEFAULT 0 COMMENT 'unique users',
#   `completed_sessions` BIGINT NOT NULL DEFAULT 0 COMMENT 'completed sessions',
#   `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
#   `create_time` DATETIME(3),
#   `integral_tag1` BIGINT,
#   `integral_tag2` BIGINT,
#   `integral_tag3` BIGINT,
#   `integral_tag4` BIGINT,
#   `integral_tag5` BIGINT,
#   `string_tag1` VARCHAR(128),
#   `string_tag2` VARCHAR(128),
#   `string_tag3` VARCHAR(128),
#   `string_tag4` VARCHAR(128),
#   `string_tag5` VARCHAR(128),
#   PRIMARY KEY (`id`)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件参数统计表
--
-- DROP TABLE IF EXISTS `eh_stat_event_param_statistics`;
# CREATE TABLE `eh_stat_event_param_statistics` (
#   `id` BIGINT NOT NULL,
#   `namespace_id` INTEGER NOT NULL DEFAULT 0,
#   `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
#   `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
#   `time_interval` VARCHAR(32) NOT NULL COMMENT 'HOURLY, DAILY, WEEKLY, MONTHLY',
#   `stat_date` DATE NOT NULL COMMENT 'stat date',
#   `stat_hour` INTEGER COMMENT 'stat hour',
#   `param_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: string param, 2: value param',
#   `param_name` VARCHAR(64) COMMENT 'param name',
#   `param_key` VARCHAR(64) COMMENT 'param key',
#   `unique_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0. general, 1. unique',
#   `total_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'param total times',
#   `total_value` BIGINT NOT NULL DEFAULT 0 COMMENT 'total value',
#   `unique_users` BIGINT NOT NULL DEFAULT 0 COMMENT 'unique users',
#   `completed_sessions` BIGINT NOT NULL DEFAULT 0 COMMENT 'completed sessions',
#   `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
#   `create_time` DATETIME(3),
#   `integral_tag1` BIGINT,
#   `integral_tag2` BIGINT,
#   `integral_tag3` BIGINT,
#   `integral_tag4` BIGINT,
#   `integral_tag5` BIGINT,
#   `string_tag1` VARCHAR(128),
#   `string_tag2` VARCHAR(128),
#   `string_tag3` VARCHAR(128),
#   `string_tag4` VARCHAR(128),
#   `string_tag5` VARCHAR(128),
#   PRIMARY KEY (`id`)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- App日志附件
--
-- DROP TABLE IF EXISTS `eh_stat_app_log_attachments`;
CREATE TABLE `eh_stat_event_app_log_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT(20) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 配置表
--
-- DROP TABLE IF EXISTS `eh_stat_event_portal_configs`;
CREATE TABLE `eh_stat_event_portal_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL,
  `config_type` TINYINT NOT NULL COMMENT '1: 底部导航栏的子项, 2: 顶部工具栏的子项',
  `config_name` VARCHAR(64) NOT NULL COMMENT 'config name',
  `identifier` VARCHAR(64) NOT NULL COMMENT 'identifier',
  `remark` VARCHAR(64) NOT NULL COMMENT 'remark',
  `config_display_name` VARCHAR(64) NOT NULL COMMENT 'config display name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 配置历史记录
--
-- DROP TABLE IF EXISTS `eh_stat_event_portal_statistics`;
CREATE TABLE `eh_stat_event_portal_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'owner type',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id',
  `stat_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: 门户, 2: 门户的子项',
  `identifier` VARCHAR(64) NOT NULL COMMENT 'identifier',
  `display_name` VARCHAR(64) NOT NULL COMMENT 'display_name',
  `time_interval` VARCHAR(32) NOT NULL COMMENT 'HOURLY, DAILY, WEEKLY, MONTHLY',
  `stat_date` DATE NOT NULL COMMENT 'stat date',
  `remark` VARCHAR(64) NOT NULL COMMENT 'remark',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件统计表
--
-- DROP TABLE IF EXISTS `eh_stat_event_statistics`;
CREATE TABLE `eh_stat_event_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_display_name` VARCHAR(64) NOT NULL COMMENT 'event display name',
  `event_version` TINYINT NOT NULL DEFAULT 1,
  `event_portal_stat_id` BIGINT NOT NULL COMMENT 'event_config_stat_id',
  `time_interval` VARCHAR(32) NOT NULL COMMENT 'HOURLY, DAILY, WEEKLY, MONTHLY',
  `stat_date` DATE NOT NULL COMMENT 'stat date',
  `stat_hour` INTEGER COMMENT 'stat hour',
  `total_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'event total count',
  `unique_users` BIGINT NOT NULL DEFAULT 0 COMMENT 'unique users',
  `completed_sessions` BIGINT NOT NULL DEFAULT 0 COMMENT 'completed sessions',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件参数统计表
--
-- DROP TABLE IF EXISTS `eh_stat_param_statistics`;
CREATE TABLE `eh_stat_event_param_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `event_stat_id` BIGINT NOT NULL COMMENT 'event_stat_id',
  `stat_key` VARCHAR(64) COMMENT 'stat_name',
  `stat_value` VARCHAR(64) COMMENT 'stat_value',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 少凡的表================================================

-- 门户item group
CREATE TABLE `eh_portal_item_groups` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `layout_id` bigint(20) NOT NULL,
  `label` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL  COMMENT 'item_group_${id}，对应eh_launch_pad_layouts里面的layout_json里面item_group的 groups[x].instanceConfig.itemGroup 和 eh_launch_pad_items里的item_group',
  `separator_flag` tinyint(4) DEFAULT 0,
  `separator_height` decimal(10,2) DEFAULT NULL,
  `widget` varchar(64) DEFAULT NULL,
  `content_type` varchar(64) DEFAULT NULL,
  `style` varchar(64) DEFAULT NULL,
  `instance_config` text COMMENT '参数配置',
  `default_order` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户item
CREATE TABLE `eh_portal_items` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `item_group_id` bigint(20) NOT NULL,
  `label` varchar(64) DEFAULT NULL,
  `item_location` varchar(2048) DEFAULT NULL COMMENT '/eh_portal_item_groups  name 对应eh_launch_pad_items里的item_location',
  `group_name` varchar(64) DEFAULT NULL  COMMENT 'eh_portal_item_groups  name 对应eh_launch_pad_layouts里面的layout_json里面item_group的 groups[x].instanceConfig.itemGroup 和 eh_launch_pad_items里的item_group',
  `name` varchar(64) DEFAULT NULL  COMMENT 'item_${id}',
  `icon_uri` varchar(1024) DEFAULT NULL,
  `item_width` int(11) NOT NULL DEFAULT '1',
  `item_height` int(11) NOT NULL DEFAULT '1',
  `bgcolor` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `action_type` varchar(64) DEFAULT NULL,
  `action_data` text,
  `default_order` int(11) NOT NULL DEFAULT '0',
  `display_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'default display on the pad, 0: hide, 1:display',
  `selected_icon_uri` varchar(1024) DEFAULT NULL,
  `more_order` int(11) NOT NULL DEFAULT '0',
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` varchar(64) DEFAULT NULL COMMENT 'the entity id linked back to the orginal resource',
  `item_category_id` bigint(20),
  `description` varchar(1024),
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户导航栏
CREATE TABLE `eh_portal_navigation_bars` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `label` varchar(64) DEFAULT NULL,
  `target_type` varchar(64) NOT NULL,
  `target_id` bigint(20) NOT NULL,
  `icon_uri` varchar(1024) DEFAULT NULL,
  `selected_icon_uri` varchar(1024) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 门户 layout
CREATE TABLE `eh_portal_layouts` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `label` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT 'layout_${id}，eh_launch_pad_layouts里面的name',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `description` varchar(1024),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ================================================