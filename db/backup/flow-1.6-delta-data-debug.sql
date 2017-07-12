-- 工作流新增两个变量 add by xq.tian  2017/06/09
SET @flow_var_max_id = (SELECT MAX(id) FROM `eh_flow_variables`);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_var_max_id := @flow_var_max_id + 1), 0, 0, '', 0, '', 'user_applier_organization_manager', '发起人的企业管理员', 'node_user_processor', 'bean_id', 'flow-variable-applier-organization-manager', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_var_max_id := @flow_var_max_id + 1), 0, 0, '', 0, '', 'user_applier_department_manager', '发起人的部门经理', 'node_user_processor', 'bean_id', 'flow-variable-applier-department-manager', 1);
