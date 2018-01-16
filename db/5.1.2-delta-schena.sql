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

ALTER TABLE eh_flow_nodes ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);

ALTER TABLE eh_flow_actions ADD INDEX i_eh_belong_entity_belong_to_action_type_step_type(belong_entity, belong_to, action_type, action_step_type);

DROP INDEX i_eh_flow_node_id_ver_type ON eh_flow_buttons;
DROP INDEX i_eh_flow_node_status ON eh_flow_buttons;

ALTER TABLE eh_flow_buttons ADD INDEX i_eh_flow_main_id_flow_ver(flow_main_id, flow_version);
ALTER TABLE eh_flow_buttons ADD INDEX i_eh_flow_node_id(flow_node_id);