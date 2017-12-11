
-- 积分系统
DROP TABLE IF EXISTS `eh_point_systems`;
CREATE TABLE `eh_point_systems` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `display_name` VARCHAR(32) NOT NULL,
  `point_name` VARCHAR(32) NOT NULL,
  `point_exchange_flag` TINYINT NOT NULL DEFAULT 2 COMMENT 'point exchange cash flag, 1: disable, 2: enabled',
  `exchange_point` INTEGER NOT NULL DEFAULT 0 COMMENT 'point exchange cash rate',
  `exchange_cash` INTEGER NOT NULL DEFAULT 0 COMMENT 'point exchange cash rate',
  `user_agreement` TEXT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 用户积分
DROP TABLE IF EXISTS `eh_point_scores`;
CREATE TABLE `eh_point_scores` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `user_id` BIGINT NOT NULL DEFAULT 0,
  `score` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 积分规则分类
DROP TABLE IF EXISTS `eh_point_rule_categories`;
CREATE TABLE `eh_point_rule_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `display_name` VARCHAR(32) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `server_id` VARCHAR(128) NOT NULL DEFAULT 'default',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 积分规则
DROP TABLE IF EXISTS `eh_point_rules`;
CREATE TABLE `eh_point_rules` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rule_categories id',
  `module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_service_modules id',
  `display_name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(64) NOT NULL,
  `arithmetic_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: add, 2: subtract',
  `points` BIGINT NOT NULL DEFAULT 0,
  `limit_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: times per day, 2: times',
  `limit_data` TEXT,
  `event_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'event name',
  `binding_event_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'binding event name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `display_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: hidden, 1: display',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 积分动作
DROP TABLE IF EXISTS `eh_point_actions`;
CREATE TABLE `eh_point_actions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `action_type` VARCHAR(64),
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `display_name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(64) NOT NULL,
  `content` TEXT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 积分记录
DROP TABLE IF EXISTS `eh_point_logs`;
CREATE TABLE `eh_point_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rule_categories id',
  `category_name` VARCHAR(32) NOT NULL DEFAULT '',
  `rule_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rules id',
  `rule_name` VARCHAR(32) NOT NULL DEFAULT '',
  `event_name` VARCHAR(128) NOT NULL DEFAULT '',
  `entity_type` VARCHAR(64) NOT NULL DEFAULT '',
  `entity_id` BIGINT NOT NULL DEFAULT 0,
  `arithmetic_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: plus, 2: minus',
  `points` BIGINT NOT NULL DEFAULT 0,
  `target_uid` BIGINT NOT NULL DEFAULT 0,
  `target_name` VARCHAR(32) NOT NULL DEFAULT '',
  `target_phone` VARCHAR(32) NOT NULL DEFAULT '',
  `operator_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: system, 2: manually',
  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_name` VARCHAR(32) NOT NULL DEFAULT '',
  `operator_phone` VARCHAR(32) NOT NULL DEFAULT '',
  `description` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 积分商品
DROP TABLE IF EXISTS `eh_point_goods`;
CREATE TABLE `eh_point_goods` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `number` VARCHAR(32) NOT NULL DEFAULT '',
  `display_name` VARCHAR(32) NOT NULL DEFAULT '',
  `poster_uri` VARCHAR(512) NOT NULL DEFAULT '',
  `poster_url` VARCHAR(512) NOT NULL DEFAULT '',
  `detail_url` VARCHAR(512) NOT NULL DEFAULT '',
  `points` BIGINT NOT NULL DEFAULT 0,
  `sold_amount` BIGINT NOT NULL DEFAULT 0,
  `original_price` DECIMAL NOT NULL DEFAULT 0,
  `discount_price` DECIMAL NOT NULL DEFAULT 0,
  `point_rule` VARCHAR(256) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `top_status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `top_time` DATETIME(3),
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 积分指南
DROP TABLE IF EXISTS `eh_point_tutorials`;
CREATE TABLE `eh_point_tutorials` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `display_name` VARCHAR(32) NOT NULL DEFAULT '',
  `description` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'description',
  `poster_uri` VARCHAR(256) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 积分指南和积分规则的映射表
DROP TABLE IF EXISTS `eh_point_tutorial_to_point_rule_mappings`;
CREATE TABLE `eh_point_tutorial_to_point_rule_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `tutorial_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_tutorials id',
  `rule_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rules id',
  `description` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'description',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 积分事件
DROP TABLE IF EXISTS `eh_point_event_logs`;
CREATE TABLE `eh_point_event_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rule_categories id',
  `event_name` VARCHAR(128),
  `event_json` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: waiting for process, 2: processing, 3: processed',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 系统事件
DROP TABLE IF EXISTS `eh_system_events`;
CREATE TABLE `eh_system_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_service_modules id',
  `display_name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(128) NOT NULL DEFAULT '',
  `event_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'event name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
