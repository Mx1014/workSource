-- AUTHOR: 张智伟 20180912
-- REMARK: issue-37602 审批单支持编辑
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '终止并重新提交', '终止并重新提交', '{"nodeType":"APPROVAL_CANCEL_AND_RESUMBIT"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '复制审批单', '复制审批单', '{"nodeType":"APPROVAL_COPY_FORM_VALUES"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '显示撤销规则', '显示撤销规则', '{"nodeType":"APPROVAL_SHOW_CANCEL_INFO"}', 2);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '编辑当前节点表单', '编辑当前节点表单', '{"nodeType":"APPROVAL_EDIT_CURRENT_FORM"}', 2);
-- INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`)
--   VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1),0, 0, '', 52000, 'any-module', 'flow_button', '编辑全部节点表单', '编辑全部节点表单', '{"nodeType":"APPROVAL_EDIT_ALL_FORMS"}', 2);

SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '30002', 'zh_CN', '关联表单需要填写才能进入下一步');
