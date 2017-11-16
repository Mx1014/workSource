-- 增加param by st.zheng
set @eh_flow_predefined_params_id = (select max(id) from eh_flow_predefined_params )
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`) VALUES (@eh_flow_predefined_params_id+1, '0', '0', '', '20100', 'repair', 'flow_node', '待确认费用', '待确认费用', '{\"nodeType\":\"CONFIRMFEE\"}', '2');
