-- 删除以前的通过授权规则授权的数据
delete from `eh_acls` where scope like '%.M%';

-- 删除以前的通过授权规则的数据
delete from `eh_service_module_assignments` where relation_id = 0;