-- 菜单显示不全
UPDATE eh_service_module_privileges
SET module_id = 20810
WHERE module_id = 20811;

UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20840;
UPDATE eh_service_modules
SET LEVEL =4
WHERE id = 20841;

UPDATE eh_service_modules
SET STATUS =2
WHERE id = 20841;

-- 删除旧的里面的  privilege_id 为管理员
DELETE  from eh_acls
where privilege_id = 10011;