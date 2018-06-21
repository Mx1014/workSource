-- 物品放行 1.1 修改模块名
-- by shiheng.ma
update eh_service_modules set name = '物品放行' where id = 49200;
update eh_service_module_apps set name = '物品放行' where module_id = 49200;
update eh_web_menus set name = '物品放行' where module_id = 49200;
update eh_acl_privileges set name = '物品放行 全部权限',description = '物品放行 全部权限' where id = 4920049200;

-- 物品放行 1.1 修改工作流模板
-- by shiheng.ma
update eh_locale_templates t set t.description = '物品放行' , t.text = '物品放行' where t.scope = 'relocation' and t.code = 1;

update eh_locale_templates t set t.description = '物品放行工作流申请人显示内容' , t.text = '放行物品：${items} 共${totalNum}件
 放行时间：${relocationDate}' where t.scope = 'relocation' and t.code = 2;

update eh_locale_templates t set t.description = '物品放行工作流处理人显示内容' , t.text = '申请人：${requestorName}  企业名称：${requestorEnterpriseName}
 放行物品：${items} 共${totalNum}件
放行时间：${relocationDate}' where t.scope = 'relocation' and t.code = 3;