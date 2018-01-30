-- 1、覆盖eh_web_menus、eh_service_modules和eh_domains，从88覆盖到新的数据库表
-- 2、更新eh_domains中namespace_id等于0的数据的domain的值为服务器实际的ip

-- 清理数据
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
