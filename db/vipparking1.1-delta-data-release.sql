-- 公告管理 add by dengs
-- 通用脚本
-- 删除权限vip车位管理
delete from eh_acl_privileges WHERE id=4080040830;
DELETE from eh_service_module_privileges WHERE privilege_id=4080040830 AND module_id=40830;
DELETE from eh_service_modules WHERE id=40830;
-- end by dengs