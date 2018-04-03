-- 工作流添加变量  add by xq.tian  2017/10/31
SET @flow_variables_id = IFNULL((SELECT MAX(id) FROM `eh_flow_variables`), 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'user_prefix_processors', '上一步处理人', 'node_user_processor', 'bean_id', 'flow-variable-prefix-node-processors', 1);

INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'applier_department_manager_level_1', '发起人的上一级部门经理', 'node_user_processor', 'bean_id', 'flow-variable-applier-department-manager-level-1', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'applier_department_manager_level_2', '发起人的上二级部门经理', 'node_user_processor', 'bean_id', 'flow-variable-applier-department-manager-level-2', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
  VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'applier_department_manager_level_3', '发起人的上三级部门经理', 'node_user_processor', 'bean_id', 'flow-variable-applier-department-manager-level-3', 1);

SET @locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 1);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '100017', 'zh_CN', '工作流设置存在异常，请修改后再试');
