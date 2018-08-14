-- 工作流节点添加表单字段  ADD BY xq.tian  2018/07/11
ALTER TABLE eh_flow_nodes ADD COLUMN form_status TINYINT NOT NULL DEFAULT 0 COMMENT '0: disable, 1: enable';
ALTER TABLE eh_flow_nodes ADD COLUMN form_origin_id BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_origin_id';
ALTER TABLE eh_flow_nodes ADD COLUMN form_version BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_version';
ALTER TABLE eh_flow_nodes ADD COLUMN form_update_time DATETIME;

ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_type1 VARCHAR(32);
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_id1 BIGINT DEFAULT 0;
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_type2 VARCHAR(32);
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_id2 BIGINT DEFAULT 0;

-- END

--
-- 工作流和业务映射表  add by xq.tian  20180724
--
-- DROP TABLE `eh_flow_service_mappings`;
CREATE TABLE `eh_flow_service_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `display_name` VARCHAR(64) NOT NULL,
  `flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the real flow id for all copy, the first flow_main_id=0',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_goto_node_id BIGINT COMMENT 'only sub flow node, when sub flow absort go to node id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_step_type VARCHAR(32) COMMENT 'only sub flow node, when sub flow absort step type';

ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_project_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings project_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_project_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings project_id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_module_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings module_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_module_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings module_id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_owner_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings owner_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_owner_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings owenr_id';

ALTER TABLE eh_flow_cases ADD COLUMN sub_flow_parent_id BIGINT NOT NULL DEFAULT 0 ;
ALTER TABLE eh_flow_cases ADD COLUMN sub_flow_path VARCHAR(128) NOT NULL DEFAULT '';

ALTER TABLE eh_flows ADD COLUMN config_status TINYINT NOT NULL DEFAULT 0 COMMENT 'config status, 2: config, 3: snapshot';

-- ---

--
-- 工作流 key-value 表  add by xq.tian  20180814
--
DROP TABLE `eh_flow_kv_configs`;
CREATE TABLE `eh_flow_kv_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 表单 key-value 表  add by xq.tian  20180814
--
DROP TABLE `eh_general_form_kv_configs`;
CREATE TABLE `eh_general_form_kv_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `key` VARCHAR(64) NOT NULL,
  `value` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME(3) COMMENT 'record update time',
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_general_forms ADD COLUMN project_type VARCHAR(64) NOT NULL DEFAULT 'EhCommunities';
ALTER TABLE eh_general_forms ADD COLUMN project_id BIGINT NOT NULL DEFAULT 0;