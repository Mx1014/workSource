-- 删除以前的通过授权规则授权的数据
delete from `eh_acls` where scope like '%.M%';

-- 删除以前的通过授权规则的数据
delete from `eh_service_module_assignments` where relation_id = 0;

-- 补充菜单数据
update `eh_web_menus` set level = (length(path)-length(replace(path,'/','')));
update `eh_web_menus` set `category` = 'module' where level > 1;
update `eh_web_menus` set `category` = 'classify' where level = 1;
