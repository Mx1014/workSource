--
-- 节点条件
--
-- DROP TABLE IF EXISTS `eh_flow_node_conditions`;
CREATE TABLE `eh_flow_node_conditions` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `flow_node_id` BIGINT NOT NULL,
  `node_level` INTEGER NOT NULL,
  `display_name` VARCHAR(128) COMMENT 'e.g: if 天数 > 5',
  `goto_node_id` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 节点条件表达式
--
-- DROP TABLE IF EXISTS `eh_flow_node_condition_expressions`;
CREATE TABLE `eh_flow_node_condition_expressions` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `flow_node_id` BIGINT NOT NULL,
  `node_level` INTEGER NOT NULL,

  `flow_node_condition_id` BIGINT NOT NULL COMMENT 'ref eh_flow_node_conditions',

  `logic_operator` VARCHAR(24) NOT NULL COMMENT '&&, ||, !',
  `relational_operator` VARCHAR(24) NOT NULL COMMENT '>, <, ==, !=',
  `variable_type1` TINYINT NOT NULL DEFAULT 1 COMMENT '1: const, 2: variable',
  `variable1` VARCHAR(64) NOT NULL DEFAULT 1 COMMENT '${varName} or 2',
  `variable_type2` TINYINT NOT NULL DEFAULT 1 COMMENT '1: const, 2: variable',
  `variable2` VARCHAR(64) NOT NULL DEFAULT 1 COMMENT '${varName} or 2',
  `expression` VARCHAR(128) NOT NULL COMMENT 'e.g: ${varName} >= 3',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 泳道
--
-- DROP TABLE IF EXISTS `eh_flow_lanes`;
CREATE TABLE `eh_flow_lanes` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `display_name` VARCHAR(128) COMMENT 'lane name',
  `lane_level` INTEGER COMMENT 'lane level',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 节点之间的连接
--
-- DROP TABLE IF EXISTS `eh_flow_node_links`;
CREATE TABLE `eh_flow_node_links` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `display_name` VARCHAR(128) COMMENT 'display name',
  `from_node_id` BIGINT NOT NULL,
  `to_node_id` BIGINT NOT NULL,
  `from_node_level` INTEGER NOT NULL,
  `to_node_level` INTEGER NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_flow_nodes` ADD COLUMN `node_type` TINYINT NOT NULL DEFAULT 4 COMMENT '1: start, 2: end, 3: normal, 4: condition front, 5: condition back';
ALTER TABLE `eh_flow_nodes` ADD COLUMN `flow_lane_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes';
ALTER TABLE `eh_flow_nodes` ADD COLUMN `need_all_processor_complete` BIGINT NOT NULL DEFAULT 0 COMMENT 'need_all_processor_complete';