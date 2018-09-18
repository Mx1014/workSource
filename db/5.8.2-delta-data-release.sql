-- 因为 5.7.0对模块和菜单重构了，这里要重新执行模块了菜单的sql。

-- 客户端处理方式
-- 默认所有是navtive应用
update eh_service_modules set client_handler_type = 0;
-- 内部链接
update eh_service_modules set client_handler_type = 2 WHERE id in (41400, 20400, 21200, 32500);
-- 离线包
update eh_service_modules set client_handler_type = 3 WHERE id in (49100, 20800, 20600);


-- 设置应用类型
-- 默认为园区模块
UPDATE eh_service_modules set app_type = 1;
-- 设置oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id in (50100,  50300, 50500, 50400, 52000, 50600, 54000, 51300, 51400, 55000, 57000, 60100, 60200, 60210, 13000, 20650, 20830, 41020, 50700, 41410, 41420, 41430, 41400);
-- 更新应用信息
UPDATE eh_service_module_apps a set a.app_type = IFNULL((SELECT b.app_type from eh_service_modules b where b.id = a.module_id), 1);


-- 增加应用、项目和公司菜单  (仅在标准版执行)
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15010000', '左邻标准版管理', '15000000', NULL, NULL, '1', '2', '/15000000/15010000', 'zuolin', '20', NULL, '2', 'system', 'classify', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15040000', '项目管理', '15010000', NULL, 'project-management', '1', '2', '/15000000/15010000/15040000', 'zuolin', '10', NULL, '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15030000', '应用管理', '15010000', NULL, 'application-management', '1', '2', '/15000000/15010000/15030000', 'zuolin', '20', NULL, '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15025000', '应用入口', '15010000', NULL, 'servicemodule-entry', '1', '2', '/15000000/15010000/15025000', 'zuolin', '30', NULL, '3', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('15050000', '企业管理', '15010000', NULL, 'business-admin', '1', '2', '/15000000/15010000/15050000', 'zuolin', '40', NULL, '3', 'system', 'module', NULL);
DELETE FROM `eh_web_menus` WHERE `id`='15030100';
DELETE FROM `eh_web_menus` WHERE `id`='15030200';
DELETE FROM `eh_web_menus` WHERE `id`='15030300';

-- 公司门禁 员工认证 职级管理 任务管理 设置为org控制  add by yanjun 20180614
UPDATE eh_service_modules SET module_control_type = 'org_control' WHERE id in (41020, 50500, 50300, 13000);
UPDATE eh_service_module_apps SET module_control_type = 'org_control' WHERE module_id in (41020, 50500, 50300, 13000);

-- janson 物品搬迁
UPDATE `eh_service_modules` SET `instance_config`='{"url":"${home.url}/goods-move/build/index.html?ns=2&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix"}' WHERE `id`='49200';
UPDATE `eh_service_module_apps` SET `instance_config`='{"url":"${home.url}/goods-move/build/index.html?ns=2&hideNavigationBar=1&ehnavigatorstyle=0#/home#sign_suffix"}' WHERE `module_id`='49200';
UPDATE `eh_service_modules` SET `client_handler_type`='2' WHERE `id`='49200';
-- end


-- 增加电商模块，用于运营板块 add by yanjun 201807041725
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92000', '电商', '0', '/92000', '1', '1', '2', '0', '2018-07-04 17:22:11', NULL, NULL, '2018-07-04 17:22:20', '0', '0', '0', '0', NULL, '1', '0', NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `app_type`, `client_handler_type`, `system_app_flag`) VALUES ('92100', '微商城', '92000', '/92000/92100', '1', '2', '2', '0', '2018-07-04 17:23:28', '{\"url\":\"${stat.biz.server.url}zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=${stat.biz.server.url}nar/biz/web/app/user/index.html?clientrecommend=1#/recommend?_k=zlbiz#sign_suffix\"}', NULL, '2018-07-04 17:23:33', '0', '0', '0', '0', NULL, '1', '2', NULL);


-- 更新企业客户管理模块
UPDATE eh_service_modules set client_handler_type = 2 where id = 21100;


-- 园区入驻从“敬请期待”改为“上线”
UPDATE eh_service_modules set client_handler_type = 0, instance_config = '{}' WHERE id = 40100;
UPDATE eh_service_module_apps set instance_config = '{}' WHERE module_id = 40100;


-- PC工作台入口分类基础数据
UPDATE eh_service_module_entries SET app_category_id = 0 where location_type not in(2, 4);
UPDATE eh_service_module_entries SET app_category_id = 16 WHERE location_type = 2 and app_category_id = 6;
UPDATE eh_service_module_entries SET app_category_id = 17 WHERE location_type = 2 and app_category_id = 7;
UPDATE eh_service_module_entries SET app_category_id = 18 WHERE location_type = 2 and app_category_id = 13;

INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('1', '协同办公', '104', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('2', '人力资源', '104', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('3', 'OA管理', '104', '4', NULL, '3', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('4', 'HR管理', '104', '4', NULL, '4', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('5', 'ERP', '104', '4', NULL, '5', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('6', '信息发布', '102', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('7', '社群运营', '102', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('8', '基础数据管理', '100', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('9', '招商租赁', '0', '2', NULL, '3', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('10', '招商与租赁管理', '100', '4', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('11', '客服管理', '102', '4', NULL, '3', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('12', '园区服务', '0', '2', NULL, '4', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('13', '服务联盟', '103', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('14', '物业管理', '101', '4', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('15', '运营统计', '102', '4', NULL, '4', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('16', '信息发布', '0', '2', NULL, '1', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('17', '社群运营', '0', '2', NULL, '2', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('18', '服务联盟', '0', '2', NULL, '5', '1');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('100', '资产管理', '0', '4', NULL, '2', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('101', '物业服务', '0', '4', NULL, '3', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('102', '园区运营', '0', '4', NULL, '4', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('103', '服务联盟', '0', '4', NULL, '5', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('104', '企业办公', '0', '4', NULL, '1', '0');
INSERT INTO `eh_app_categories` (`id`, `name`, `parent_id`, `location_type`, `app_type`, `default_order`, `leaf_flag`) VALUES ('500000002', '分类123', '0', '1', NULL, '1', '1');


INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58000000', '协同办公', '40000010', NULL, NULL, '1', '2', '/40000010/58000000', 'park', '4', NULL, '2', 'system', 'classify', '2', '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('59000000', '人力资源', '40000010', NULL, NULL, '1', '2', '/40000010/59000000', 'park', '7', NULL, '2', 'system', 'classify', '2', '1');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58010000', '审批', '58000000', NULL, 'approval-work', '0', '2', '/40000010/58000000/58010000', 'park', '10', '52000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58020000', '工作汇报', '58000000', NULL, 'working-conference', '0', '2', '/40000010/58000000/58020000', 'park', '20', '54000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58030000', '文档', '58000000', NULL, 'document-work', '0', '2', '/40000010/58000000/58030000', 'park', '30', '55000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58040000', '企业公告', '58000000', NULL, 'notice-work', '0', '2', '/40000010/58000000/58040000', 'park', '40', '57000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58050000', '日程提醒', '58000000', NULL, 'time-schedule', '0', '2', '/40000010/58000000/58050000', 'park', '50', '59100', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('58060000', '会议预订', '58000000', NULL, 'meeting-work', '0', '2', '/40000010/58000000/58060000', 'park', '60', '53000', '3', 'system', 'module', '2', '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES ('59010000', '通讯录', '59000000', NULL, 'address-book', '0', '2', '/40000010/59000000/59010000', 'park', '10', '50100', '3', 'system', 'module', '2', '2');



-- 微商城设置为内部链接
UPDATE eh_service_modules SET client_handler_type = 2 WHERE id = 92100;


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: xq.tian  20180725
-- REMARK: 工作流 2.7

-- 脚本重复了  先注释  edit by yanjun

-- SET @eh_locale_templates_id = (SELECT MAX(id) FROM eh_locale_templates);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20008, 'zh_CN', '子业务流程进行中', '${serviceName} 进行中', 0);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20009, 'zh_CN', '子流程创建成功，点击此处查看父流程详情。', '子流程创建成功，点击此处查看父流程详情', 0);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20010, 'zh_CN', '${serviceName} 已完成', '${serviceName} 已完成', 0);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20011, 'zh_CN', '${serviceName} 已终止', '${serviceName} 已终止', 0);
-- INSERT INTO eh_locale_templates (id, scope, code, locale, description, text, namespace_id)
--   VALUES ((@eh_locale_templates_id := @eh_locale_templates_id + 1), 'flow', 20012, 'zh_CN', '子流程循环层级过多，流程已终止，详情请联系管理员', '子流程循环层级过多，流程已终止，详情请联系管理员', 0);
--
-- SET @eh_locale_strings_id = (SELECT MAX(id) FROM eh_locale_strings);
-- INSERT INTO eh_locale_strings (id, scope, code, locale, text)
--   VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100025', 'zh_CN', '子流程异常，请检查设置');
-- INSERT INTO eh_locale_strings (id, scope, code, locale, text)
--   VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100026', 'zh_CN', '请先发布新版本后再启用');
-- INSERT INTO eh_locale_strings (id, scope, code, locale, text)
--   VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'flow', '100027', 'zh_CN', '当前工作流未被修改，请修改后发布新版本')

-- AUTHOR: shiheng.ma  20180824
-- REMARK: 工位预订 应用配置迁移
UPDATE `eh_service_modules` SET `instance_config`='{\"url\":\"${home.url}/station-booking-web/build/index.html#/home#sign_suffix\",\"currentProjectOnly\":0}' WHERE `id`='40200';
UPDATE `eh_service_module_apps` SET `instance_config`='{\"url\":\"${home.url}/station-booking-web/build/index.html#/home#sign_suffix\",\"currentProjectOnly\":0}' WHERE `module_id`='40200';
-- --------------------- SECTION END ---------------------------------------------------------



-- 更新 layout
SET @versionCode = '201809170100';

SET @bizAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 92100 AND `namespace_id` = 2);
SET @activityAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10600 AND `namespace_id` = 2);
SET @forumAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10100 AND `namespace_id` = 2);
SET @communityBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 10300 AND `namespace_id` = 2);
SET @enterpriseBulletinsAppId = (SELECT IFNULL(MIN(origin_id),0) from eh_service_module_apps WHERE module_id = 57000 AND `namespace_id` = 2);

UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"工作台\",\"groups\":[{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":57000.0,\"appId\":', @enterpriseBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"columnCount\":1,\"defaultOrder\":2,\"groupName\":\"园区运营\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"},{\"columnCount\":1,\"defaultOrder\":4,\"groupName\":\"企业办公\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":16.0,\"paddingLeft\":16.0,\"paddingBottom\":16.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"backgroundColor\":\"#ffffff\",\"appType\":0.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Card\"}]}') WHERE type = 4 AND namespace_id = 2;
UPDATE eh_launch_pad_layouts set version_code = @versionCode, layout_json  = CONCAT('{"versionCode":"',@versionCode,'","layoutName":\"ServiceMarketLayout\",\"displayName\":\"服务广场\",\"groups\":[{\"defaultOrder\":1,\"groupName\":\"banner图片1\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31056\",\"widthRatio\":16.0,\"heightRatio\":9.0,\"shadowFlag\":1.0,\"paddingFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Banners\"},{\"groupId\":0,\"groupName\":\"容器\",\"columnCount\":4,\"defaultOrder\":2,\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31058\",\"paddingTop\":0.0,\"paddingLeft\":16.0,\"paddingBottom\":0.0,\"paddingRight\":16.0,\"lineSpacing\":0.0,\"columnSpacing\":0.0,\"cssStyleFlag\":1.0,\"backgroundColor\":\"#ffffff\",\"allAppFlag\":1.0},\"separatorFlag\":0,\"separatorHeight\":0,\"style\":\"Default\",\"widget\":\"Navigator\"},{\"defaultOrder\":3,\"groupName\":\"公告\",\"instanceConfig\":{\"itemGroup\":\"EhPortalItemGroups31057\",\"rowCount\":1.0,\"style\":2.0,\"shadow\":1.0,\"moduleId\":10300.0,\"appId\":', @communityBulletinsAppId ,'},\"separatorFlag\":0,\"separatorHeight\":0,\"widget\":\"Bulletins\"},{\"groupName\":\"电商入口\",\"widget\":\"NavigatorTemp\"},{\"defaultOrder\":4,\"groupName\":\"商品精选\",\"instanceConfig\":{\"itemGroup\":\"OPPushBiz\",\"moduleId\":92100.0,\"appId\":', @bizAppId, ',\"entityCount\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"HorizontalScrollSquareView\",\"widget\":\"OPPush\"},{\"defaultOrder\":5,\"groupName\":\"活动\",\"instanceConfig\":{\"itemGroup\":\"OPPushActivity\",\"entityCount\":5.0,\"subjectHeight\":0.0,\"descriptionHeight\":0.0,\"newsSize\":5.0,\"moduleId\":10600.0,\"appId\":', @activityAppId, ',\"actionType\":61.0,\"appConfig\":{\"categoryId\":1.0,\"publishPrivilege\":1.0,\"livePrivilege\":0.0,\"listStyle\":2.0,\"scope\":3.0,\"style\":4.0}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"HorizontalScrollWideView\",\"widget\":\"OPPush\"},{\"defaultOrder\":7,\"groupName\":\"论坛\",\"instanceConfig\":{\"moduleId\":10100.0,\"appId\":', @forumAppId, ',\"actionType\":62.0,\"newsSize\":5.0,\"appConfig\":{}},\"separatorFlag\":1,\"separatorHeight\":24,\"style\":\"TextImageWithTagListView\",\"widget\":\"OPPush\"}]}') WHERE type = 5 AND namespace_id = 2 ;



-- 物业巡检，品质核查从coming soon恢复成正式的离线包
UPDATE eh_service_modules set instance_config = '{"realm":"qualityInspection","entryUrl":"https://park-std.zuolin.com/nar/qualityInspection/build/index.html?hideNavigationBar=1#/home#sign_suffix"}' WHERE id = 20600;

UPDATE eh_service_modules set instance_config = '{"realm":"equipmentInspection","entryUrl":"https://park-std.zuolin.com/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix"}' WHERE id = 20800;

UPDATE eh_service_module_apps set instance_config = '{"realm":"qualityInspection","entryUrl":"https://park-std.zuolin.com/nar/qualityInspection/build/index.html?hideNavigationBar=1#/home#sign_suffix"}' WHERE module_id = 20600;

UPDATE eh_service_module_apps set instance_config = '{"realm":"equipmentInspection","entryUrl":"https://park-std.zuolin.com/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix"}' WHERE module_id = 20800;


-- 设置oa模块
UPDATE eh_service_modules set app_type = 0 WHERE id in (59100, 53000);
-- 更新应用信息
UPDATE eh_service_module_apps a set app_type = 0 WHERE module_id in (59100, 53000);
