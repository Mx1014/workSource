--
-- 用户输入内容变量 add by xq.tian  2017/08/05
--
SELECT MAX(id) FROM `eh_flow_variables` INTO @flow_variables_id;
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_variables_id := @flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_msg_input_content', '文本备注内容', 'text_button_msg', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);
