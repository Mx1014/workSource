-- 0、执行search文件夹下的pmtask.sh脚本
-- 1、覆盖eh_web_menus、eh_service_modules和eh_domains，从88覆盖到新的数据库表
-- 2、更新eh_domains中namespace_id等于0的数据的domain的值为服务器实际的ip
-- offline  by jiarui  {home.url}换成域名 这里需要换成部署环境的域名信息 如：core.zuolin.com
/*SET  @id = (SELECT  MAX(id) FROM eh_version_realm);
INSERT INTO `eh_version_realm` VALUES (@id:=@id+1, 'equipmentInspection', NULL, NOW(), '0');

SET  @vId = (SELECT  MAX(id) FROM eh_version_urls);
INSERT INTO `eh_version_urls` VALUES (@vId:=@vId+1, @id, '1.0.0', 'http://{home.url}/nar/equipmentInspection/inspectionOffLine/equipmentInspection-1-0-0.zip', 'http://opv2-test.zuolin.com/nar/equipmentInspection/inspectionOffLine/equipmentInspection-1-0-0.zip', '物业巡检巡检离线', '0', '物业巡检', NOW(), NULL, '0');

UPDATE eh_launch_pad_items
SET action_data = '{\"realm\":\"equipmentInspection\",\"entryUrl\":\"http://{home.url}/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}'
WHERE item_label LIKE '%巡检%';
update eh_service_module_apps
set instance_config = '{\"realm\":\"quality\",\"entryUrl\":\"http://{home.url}/nar/quality/index.html?hideNavigationBar=1#/select_community#sign_suffix\"}'
where module_id = 20600;


update eh_service_module_apps
set instance_config = '{\"realm\":\"equipmentInspection\",\"entryUrl\":\"http://{home.url}/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}'
where module_id = 20800;*/
-- offline  by jiarui
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

-- 5、同步数据库id
-- /evh/admin/syncSequence

-- 6、同步运营后台数据
-- /evh/portal/syncLaunchPadData

-- 7、在运营后台添加活动或论坛的主页签应用后需要刷新应用id（选做）
-- UPDATE eh_service_module_apps set custom_tag = 1, instance_config = '{"categoryId":1,"publishPrivilege":1,"livePrivilege":0,"listStyle":2,"scope":3,"style":4,"title": "活动管理"}' where module_id = 10600 and (instance_config is NULL or instance_config not LIKE '%categoryId%');
-- UPDATE eh_service_module_apps set custom_tag = 0, instance_config = '{"forumEntryId":0}' where module_id = 10100 and (instance_config is NULL or instance_config not LIKE '%forumEntryId%');

/*8、
1)/pmtask/syncCategories
2) 备份eh_pm_tasks表
 3)UPDATE eh_pm_tasks
        LEFT JOIN
    (SELECT
       distinct (a.id) as aid,b.id as bid,b.owner_id as owner_id
    FROM
        eh_categories a, eh_categories b
    WHERE
        a.path = b.path ) c ON eh_pm_tasks.task_category_id = c.aid
        AND eh_pm_tasks.owner_id = c.owner_id
SET
    eh_pm_tasks.task_category_id = c.bid
WHERE
    eh_pm_tasks.task_category_id!=0;

UPDATE eh_pm_tasks
        RIGHT JOIN
    (SELECT
       distinct (a.id) as aid,b.id as bid,b.owner_id as owner_id
    FROM
        eh_categories a, eh_categories b
    WHERE
        a.path = b.path ) c ON eh_pm_tasks.category_id = c.aid
        AND eh_pm_tasks.owner_id = c.owner_id
SET
    eh_pm_tasks.category_id = c.bid
WHERE
    eh_pm_tasks.category_id!=0;

 4)/pmtask/syncFromDb
*/


-- 9、同步服务联盟和工位预定数据，以下接口请勿重复调用！！请按照顺序调用。
-- /yellowPage/syncOldForm
-- /yellowPage/syncServiceAllianceApplicationRecords
-- /yellowPage/syncSARequestInfo
-- /officecubicle/dataMigration

-- 10、执行文件 db/search/enterpriseCustomer.sh 然后同步/customer/syncEnterpriseCustomer

-- 11、同步审批范围及离职人员状态
-- /admin/general_approval/initializeGeneralApprovalScope
-- /archives/syncArchivesDismissStatus

-- 12、以下按照顺序执行

-- flush redis    清空掉redis

-- 同步以下接口 （执行完sql之后）

-- /equipment/syscStandardToEqiupmentPlan

-- /equipment/syncEquipmentStandardIndex

-- /equipment/syncEquipmentStandardMapIndex

-- /equipment/syncEquipmentPlansIndex

-- /equipment/syncEquipmentTasksIndex

-- syscStandardToEqiupmentPlan 同步如果发生异常 eh_equipment_inspection_plans    eh_equipment_inspection_equipment_plan_map eh_equipment_inspection_plan_group_map 清空重新同步
-- 执行脚本物业巡检离线的脚本equipment-inspection改成equipmentInspection，放到nar下面