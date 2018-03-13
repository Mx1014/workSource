
-- 2、根据实际域名更新eh_domains表

-- 3、清理老数据
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

/*

-- 增加大版本，版本id取域空间id
INSERT INTO `eh_portal_versions` (`id`, `namespace_id`, `parent_id`, `date_version`, `big_version`, `minor_version`, `create_time`, `sync_time`, `publish_time`, `status`)
SELECT id, id, NULL, '20180227', '1', '0', NOW(), NOW(), NULL, '2' from eh_namespaces;

-- 增加小版本，版本id取域空间id+100
INSERT INTO `eh_portal_versions` (`id`, `namespace_id`, `parent_id`, `date_version`, `big_version`, `minor_version`, `create_time`, `sync_time`, `publish_time`, `status`)
SELECT id + 100, id, id, '20180227', '1', '1', NOW(), NOW(), NULL, NULL from eh_namespaces;

-- 同步大版本应用，版本id取域空间id
SET @appid = 0;
INSERT INTO `eh_service_module_apps` (`id`, `namespace_id`, `version_id`, `origin_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `module_control_type`, `custom_tag`, `custom_path`)
SELECT @appid := @appid + 1, namespace_id, namespace_id, `active_app_id`,`name`, `module_id`, `instance_config`, `status`, `action_type`, NOW(), NOW(), 1, 1, `module_control_type`, `custom_tag`, `custom_path` from eh_reflection_service_module_apps;

-- 同步小版本应用，版本id取域空间id+100
INSERT INTO `eh_service_module_apps` (`id`, `namespace_id`, `version_id`, `origin_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `module_control_type`, `custom_tag`, `custom_path`)
SELECT @appid := @appid + 1, namespace_id, namespace_id + 100,  `active_app_id`,`name`, `module_id`, `instance_config`, `status`, `action_type`, NOW(), NOW(), 1, 1, `module_control_type`, `custom_tag`, `custom_path` from eh_reflection_service_module_apps;

*/

-- 5、在运营后台添加活动或论坛的主页签应用后需要刷新应用id，配置为空的应用设置为'{}'
-- UPDATE eh_service_module_apps set custom_tag = 1, instance_config = '{"categoryId":1,"publishPrivilege":1,"livePrivilege":0,"listStyle":2,"scope":3,"style":4,"title": "活动管理"}' where module_id = 10600 and (instance_config is NULL or instance_config not LIKE '%categoryId%');
-- UPDATE eh_service_module_apps set custom_tag = 0, instance_config = '{"forumEntryId":0}' where module_id = 10100 and (instance_config is NULL or instance_config not LIKE '%forumEntryId%');
-- UPDATE eh_service_module_apps set instance_config = '{}' where instance_config is NULL or instance_config = '';

-- 6、同步数据库id
-- /evh/admin/syncSequence

-- 7、同步运营后台数据
-- /evh/portal/syncLaunchPadData


-- 8、同步服务联盟和工位预定数据，以下接口请勿重复调用！！请按照顺序调用。
-- /yellowPage/syncOldForm
-- /yellowPage/syncServiceAllianceApplicationRecords
-- /yellowPage/syncSARequestInfo
-- /officecubicle/dataMigration
