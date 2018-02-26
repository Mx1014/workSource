-- 1、覆盖eh_web_menus、eh_service_modules和eh_domains，从88覆盖到新的数据库表
-- 2、更新eh_domains中namespace_id等于0的数据的domain的值为服务器实际的ip

-- 3、清理数据
/*
delete from `eh_portal_content_scopes`;
delete from `eh_portal_item_categories`;
delete from `eh_portal_item_groups`;
delete from `eh_portal_items`;
delete from `eh_portal_launch_pad_mappings`;
delete from `eh_portal_layouts`;
delete from `eh_service_module_apps`;
delete from `eh_portal_versions`;
delete from `eh_portal_version_users`;
*/

-- 4、刷新应用id
-- UPDATE eh_service_module_apps a SET a.origin_id = IFNULL((SELECT b.active_app_id FROM eh_reflection_service_module_apps b WHERE	a.namespace_id = b.namespace_id AND a.module_id = b.module_id AND a.custom_tag = b.custom_tag), a.origin_id );



-- 5、在运营后台添加活动或论坛的主页签应用后需要刷新应用id（选做）
-- UPDATE eh_service_module_apps set custom_tag = 1, instance_config = '{"categoryId":1,"publishPrivilege":1,"livePrivilege":0,"listStyle":2,"scope":3,"style":4,"title": "活动管理"}' where module_id = 10600 and (instance_config is NULL or instance_config not LIKE '%categoryId%');
-- UPDATE eh_service_module_apps set custom_tag = 0, instance_config = '{"forumEntryId":0}' where module_id = 10100 and (instance_config is NULL or instance_config not LIKE '%forumEntryId%');
