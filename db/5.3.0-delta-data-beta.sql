
-- 2、根据实际域名更新eh_domains表

-- offline  by jiarui  {home.url}换成域名 这里需要换成部署环境的域名信息 如：core.zuolin.com
-- 物业巡检和品质核查的分别有两条insert语句，分别同时执行
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

-- 5、在运营后台添加活动或论坛的主页签应用后需要刷新应用id，配置为空的应用设置为'{}'
/*

 UPDATE eh_service_module_apps set instance_config = replace(instance_config, '"entryId":2,', '"categoryId":2,') where module_id = 10600 and  custom_tag = '2' and instance_config NOT LIKE '%"categoryId":2%';
 UPDATE eh_service_module_apps set custom_tag = 1, instance_config = '{"categoryId":1,"publishPrivilege":1,"livePrivilege":0,"listStyle":2,"scope":3,"style":4,"title": "活动管理"}' where module_id = 10600 and (instance_config is NULL or instance_config not LIKE '%categoryId%');
 UPDATE eh_service_module_apps set custom_tag = 0, instance_config = '{"forumEntryId":0}' where module_id = 10100 and (instance_config is NULL or instance_config not LIKE '%forumEntryId%');
 UPDATE eh_service_module_apps set custom_tag = 0 where module_id = 10100 and (instance_config = '{"forumEntryId":"0"}' OR instance_config = '{"forumEntryId":0}');
 UPDATE eh_service_module_apps set custom_tag = 1 where module_id = 10100 and (instance_config = '{"forumEntryId":"1"}' OR instance_config = '{"forumEntryId":1}');
 UPDATE eh_service_module_apps set custom_tag = 2 where module_id = 10100 and (instance_config = '{"forumEntryId":"2"}' OR instance_config = '{"forumEntryId":2}');
 UPDATE eh_service_module_apps set instance_config = '{}' where instance_config is NULL or instance_config = '';

*/

-- 6、同步数据库id
-- /evh/admin/syncSequence

-- 7、同步运营后台数据
-- /evh/portal/syncLaunchPadData


-- 8、同步服务联盟和工位预定数据，以下接口请勿重复调用！！请按照顺序调用。
-- /yellowPage/syncOldForm
-- /yellowPage/syncServiceAllianceApplicationRecords
-- /yellowPage/syncSARequestInfo
-- /officecubicle/dataMigration

/*9、
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
 5)/pmtask/syncTaskStatistics
*/

/* 搜索初始化脚本
执行/db/search 下的文件：
paymentApplication.sh
contract.sh
enterpriseCustomer.sh
enterprise.sh
并执行api：
/org/syncIndex
/contract/syncContracts
/customer/syncEnterpriseCustomer
/payment_application/syncIndex
*/


-- 11、同步审批范围及离职人员状态
-- /admin/general_approval/initializeGeneralApprovalScope
-- /archives/syncArchivesDismissStatus

-- 12、以下按照顺序执行

-- flush redis    清空掉redis

-- 同步以下接口 （执行完sql之后）

-- /equipment/syncStandardToEqiupmentPlan

-- /equipment/syncEquipmentStandardIndex

-- /equipment/syncEquipmentStandardMapIndex

-- /equipment/syncEquipmentPlansIndex

-- /equipment/syncEquipmentTasksIndex

-- syncStandardToEqiupmentPlan 同步如果发生异常 eh_equipment_inspection_plans    eh_equipment_inspection_equipment_plan_map eh_equipment_inspection_plan_group_map 清空重新同步
-- 执行脚本物业巡检离线的脚本equipment-inspection改成equipmentInspection，放到nar下面  品质核查的qualityInspection

--
-- 以下SQL只在深圳湾独立部署的环境执行
--
-- 工作流新增两个变量 add by xq.tian  2017/06/09
SET @flow_var_max_id = (SELECT MAX(id) FROM `eh_flow_variables`);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_var_max_id := @flow_var_max_id + 1), 0, 0, '', 0, '', 'user_applier_organization_manager', '发起人的企业管理员', 'node_user_processor', 'bean_id', 'flow-variable-applier-organization-manager', 1);
INSERT INTO `eh_flow_variables` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `name`, `label`, `var_type`, `script_type`, `script_cls`, `status`)
VALUES ((@flow_var_max_id := @flow_var_max_id + 1), 0, 0, '', 0, '', 'user_applier_department_manager', '发起人的部门经理', 'node_user_processor', 'bean_id', 'flow-variable-applier-department-manager', 1);

SET @eh_configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
    VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'flow.stepname.approve_step', '下一步', 'approve-step', 0, NULL);

UPDATE eh_flow_buttons SET button_name = '下一步' WHERE button_name = 'approve_step';
