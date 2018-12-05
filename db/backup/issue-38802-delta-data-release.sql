-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 梁燕龙
-- REMARK: issue-38802
INSERT INTO `eh_locale_strings` (scope, code, locale, text)
    VALUES ('activity', 10033,'zh_CN','请到活动详情页扫码签到');

-- --------------------- SECTION END ---------------------------------------------------------

SET @eh_flow_variables_id = (SELECT MAX(id) FROM eh_flow_variables);
INSERT INTO eh_flow_variables (id, namespace_id, owner_id, owner_type, module_id, module_type, name, label, var_type, script_type, script_cls, status)
  VALUES ((@eh_flow_variables_id := @eh_flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_tracker_input_content', '文本备注内容', 'text_tracker', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);