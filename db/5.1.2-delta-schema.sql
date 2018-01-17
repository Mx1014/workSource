-- added by wh
-- 考勤管理员操作日志 记录更新考勤组,导入排班表等操作 
CREATE TABLE `eh_punch_operation_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the report template',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_name` VARCHAR(64) COMMENT 'the module type',
  `rule_id` BIGINT COMMENT 'the module id',
  `rule_name` VARCHAR(64) COMMENT 'the module type',
  `operate_api` VARCHAR(128) ,
  `request_parameter` TEXT ,
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 工作流加索引 add by xq.tian 2018/01/16
ALTER TABLE eh_flow_links ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);
ALTER TABLE eh_flow_links ADD INDEX i_eh_flow_to_node_id(to_node_id);
ALTER TABLE eh_flow_links ADD INDEX i_eh_flow_from_node_id(from_node_id);

ALTER TABLE eh_flow_conditions ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);

ALTER TABLE eh_flow_lanes ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);

ALTER TABLE eh_flow_branches ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);

ALTER TABLE eh_flow_nodes ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);
ALTER TABLE eh_flow_actions ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);
ALTER TABLE eh_flow_condition_expressions ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);
ALTER TABLE eh_flow_user_selections ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);

ALTER TABLE eh_flow_actions ADD INDEX i_eh_belong_entity_belong_to_action_type_step_type(belong_entity, belong_to, action_type, action_step_type);

ALTER TABLE eh_flow_buttons ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);
ALTER TABLE eh_flow_buttons ADD INDEX i_eh_flow_node_id(flow_node_id);


-- 以下sql可能会失败
ALTER TABLE eh_flow_buttons DROP INDEX i_eh_flow_node_id_ver_type;
ALTER TABLE eh_flow_buttons DROP INDEX i_eh_flow_node_status;

