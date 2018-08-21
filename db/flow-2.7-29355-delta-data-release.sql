-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: xq.tian  20180725
-- REMARK: 工作流 2.7
SET @eh_locale_templates_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20008, 'zh_CN', '子业务流程进行中', '${serviceName} 进行中', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20009, 'zh_CN', '子流程创建成功，点击此处查看父流程详情。', '子流程创建成功，点击此处查看父流程详情', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20010, 'zh_CN', '${serviceName} 已完成', '${serviceName} 已完成', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20011, 'zh_CN', '${serviceName} 已终止', '${serviceName} 已终止', 0);
INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
  VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20012, 'zh_CN', '子流程循环层级过多，流程已终止，详情请联系管理员', '子流程循环层级过多，流程已终止，详情请联系管理员', 0);

SET @eh_locale_strings_id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100025', 'zh_CN', '子流程异常，请检查设置');
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100026', 'zh_CN', '请先发布新版本后再启用');
INSERT INTO eh_locale_strings (id, scope, code, locale, text)
  VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100027', 'zh_CN', '当前工作流未被修改，请修改后发布新版本');
-- --------------------- SECTION END ---------------------------------------------------------