
-- 设置应用类型 add by yanjun 201804081646
UPDATE eh_service_modules set app_type = 0 where id=50000 OR parent_id = 50000 OR path LIKE '%/50000/%';
UPDATE eh_service_modules set app_type = 1 WHERE app_type is NULL;
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 0);