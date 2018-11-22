-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: xq.tian
-- REMARK: 新增跟踪变量
SET @eh_flow_variables_id = (SELECT MAX(id) FROM eh_flow_variables);
INSERT INTO eh_flow_variables (id, namespace_id, owner_id, owner_type, module_id, module_type, name, label, var_type, script_type, script_cls, status)
  VALUES ((@eh_flow_variables_id := @eh_flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_tracker_input_content', '文本备注内容', 'text_tracker', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);

update eh_locale_strings set text='当前流程未经过预设待驳回节点。' where scope='flow' and code='100015';

-- --------------------- SECTION END ---------------------------------------------------------