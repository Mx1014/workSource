-- merge from organization-delta-data-release.sql by lqs 20161128
-- by sfyan 获取电商平台的店铺信息接口
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'get.businesses.info.api', 'zl-ec/rest/openapi/shop/listByCondition', '获取店铺信息');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
VALUES ('448', 'organization', '600005', 'zh_CN', '通用岗位已存在');

-- merge from activityentry-delta-data-release.sql by xiongying20161128
UPDATE eh_activities a SET forum_id = (SELECT forum_id FROM eh_forum_posts WHERE id = a.post_id) WHERE EXISTS(SELECT forum_id FROM eh_forum_posts WHERE id = a.post_id);
UPDATE eh_activities a SET creator_tag = (SELECT creator_tag FROM eh_forum_posts WHERE id = a.post_id);
UPDATE eh_activities a SET target_tag = (SELECT target_tag FROM eh_forum_posts WHERE id = a.post_id);
UPDATE eh_activities a SET visible_region_type = (SELECT visible_region_type FROM eh_forum_posts WHERE id = a.post_id);
UPDATE eh_activities a SET visible_region_id = (SELECT visible_region_id FROM eh_forum_posts WHERE id = a.post_id);

-- merge from sa1.7-delta-data-release.sql by xiongying20161128
-- 新增预约看楼模板 add by xiongying 20161116
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('9', 'Apartment', '预约看楼', '预约看楼', '1', '1', ' {"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"userName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"string","fieldContentType":"text","fieldDesc":"mobile","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业名称","fieldType":"string","fieldContentType":"text","fieldDesc":"organizationName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"areaSize","fieldDisplayName":"面积需求","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入面积需求","requiredFlag":"1","dynamicFlag":"0"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0","dynamicFlag":"0"}]}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (10, '999985', '9');


--
-- 修改能耗管理的入口页面地址  add by xq.tian  2016/11/30
--
UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"http://core.zuolin.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' WHERE `item_name` = 'Energy' AND `namespace_id` = '999992';

-- 物业报修2.6 merge from pmtask-delta-data.sql by sw 20161128
UPDATE eh_pm_tasks SET address_type = 1 WHERE address_type IS NULL;
SET @eh_locale_strings = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings := @eh_locale_strings + 1), 'pmtask', '10007', 'zh_CN', '该单已被其他人处理，请返回主界面刷新任务');

DELETE FROM eh_locale_templates WHERE scope = 'pmtask.notification' AND CODE = 7;

SET @eh_locale_templates = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'pmtask.notification', '7', 'zh_CN', '任务操作模版', '${creatorName} ${creatorPhone}已发起一个${categoryName}单，请尽快处理', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ((@eh_locale_templates := @eh_locale_templates + 1), 'sms.default.yzx', '15', 'zh_CN', '物业任务3-深业', '32949', '999992');

	SELECT * FROM eh_locale_templates WHERE scope = 'pmtask.notification' AND CODE IN (5,6);
UPDATE eh_locale_templates SET namespace_id = 0 WHERE scope = 'pmtask.notification' AND CODE IN (5,6);
	

-- SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
-- INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10008', '20191', '服务统计', '1', '1', '服务统计 全部权限', '710');
-- INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10008', '20192', '人员评分统计', '1', '1', '人员评分统计 全部权限', '710');

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

-- 华润oe 原活动默认为白领活动 by xiongying20161209
update eh_activity_categories set default_flag = 1 where id = 1000000 and namespace_id = 999985;
INSERT INTO `eh_acls`(`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
SELECT (@acl_id := @acl_id + 1), 'EhCommunities', owner_id, '1', '904', target_id, '0', '1', '2016-11-29 19:50:55', '0', 'EhUsers', CONCAT('EhCommunities',owner_id,'.pmtask')
 FROM eh_pm_task_targets WHERE role_id = 1;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
SELECT (@acl_id := @acl_id + 1), 'EhCommunities', owner_id, '1', '805', target_id, '0', '1', '2016-11-29 19:50:55', '0', 'EhUsers', CONCAT('EhCommunities',owner_id,'.pmtask')
 FROM eh_pm_task_targets WHERE role_id = 1;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
 SELECT (@acl_id := @acl_id + 1), 'EhCommunities', owner_id, '1', '331', target_id, '0', '1', '2016-11-29 19:50:55', '0', 'EhUsers', CONCAT('EhCommunities',owner_id,'.pmtask')
 FROM eh_pm_task_targets WHERE role_id = 1;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
SELECT (@acl_id := @acl_id + 1), 'EhCommunities', owner_id, '1', '332', target_id, '0', '1', '2016-11-29 19:50:55', '0', 'EhUsers', CONCAT('EhCommunities',owner_id,'.pmtask')
 FROM eh_pm_task_targets WHERE role_id = 1;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
 SELECT (@acl_id := @acl_id + 1), 'EhCommunities', owner_id, '1', '333', target_id, '0', '1', '2016-11-29 19:50:55', '0', 'EhUsers', CONCAT('EhCommunities',owner_id,'.pmtask')
 FROM eh_pm_task_targets WHERE role_id = 1;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
SELECT (@acl_id := @acl_id + 1), 'EhCommunities', owner_id, '1', '920', target_id, '0', '1', '2016-11-29 19:50:55', '0', 'EhUsers', CONCAT('EhCommunities',owner_id,'.pmtask')
 FROM eh_pm_task_targets WHERE role_id = 1;

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
SELECT (@acl_id := @acl_id + 1), 'EhCommunities', owner_id, '1', '805', target_id, '0', '1', '2016-11-29 19:50:55', '0', 'EhUsers', CONCAT('EhCommunities',owner_id,'.pmtask')
 FROM eh_pm_task_targets WHERE role_id = 2;
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
SELECT (@acl_id := @acl_id + 1), 'EhCommunities', owner_id, '1', '332', target_id, '0', '1', '2016-11-29 19:50:55', '0', 'EhUsers', CONCAT('EhCommunities',owner_id,'.pmtask')
 FROM eh_pm_task_targets WHERE role_id = 2;


-- 组织架构 add by sw 20161128
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10000', '信息发布', '0', '/10000', '0', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10100', '论坛/公告', '10000', '/10000/10100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10200', '园区简介', '40000', '/40000/10200', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10400', '广告管理', '10000', '/10000/10400', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10600', '活动管理', '10000', '/10000/10600', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10700', '路演直播', '10000', '/10000/10700', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10800', '新闻管理', '10000', '/10000/10800', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10850', '园区报', '10000', '/10000/10850', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10900', '行业动态', '10000', '/10000/10900', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('11000', '一键推送', '10000', '/10000/11000', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20000', '物业服务', '0', '/20000', '0', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20100', '物业报修', '20000', '/20000/20100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20400', '物业缴费', '20000', '/20000/20400', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20600', '品质核查', '20000', '/20000/20600', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20800', '设备巡检', '20000', '/20000/20800', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('30000', '项目管理', '0', '/30000', '2', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('30500', '项目信息', '30000', '/30000/30500', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('31000', '楼栋管理', '30000', '/30000/31000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('32000', '门牌管理', '30000', '/30000/32000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('33000', '企业管理', '30000', '/30000/33000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('34000', '用户管理', '30000', '/30000/34000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('35000', '用户认证', '30000', '/30000/35000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('37000', '客户资料', '30000', '/30000/37000', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('38000', '业主管理', '30000', '/30000/38000', '2', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40000', '运营服务', '0', '/40000', '0', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40100', '招租管理', '40000', '/40000/40100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40200', '工位预订', '40000', '/40000/40200', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40300', '服务热线', '40000', '/40000/40300', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40400', '资源预订', '40000', '/40000/40400', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40500', '服务联盟', '40000', '/40000/40500', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40600', '创客空间', '40000', '/40000/40600', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40700', '结算管理', '40000', '/40000/40700', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40750', '运营统计', '40000', '/40000/40750', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40800', '停车缴费', '40000', '/40000/40800', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('40900', '车辆管理', '40000', '/40000/40900', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41000', '大堂门禁', '40000', '/40000/41000', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41100', 'Wifi热点', '40000', '/40000/41100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('41200', '一卡通', '40000', '/40000/43500', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50000', '内部管理', '0', '/50000', '1', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50100', '组织架构', '50000', '/50000/50100', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50200', '岗位管理', '50000', '/50000/50200', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50300', '职级管理', '50000', '/50000/50300', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50400', '人员管理', '50000', '/50000/50400', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50500', '认证管理', '50000', '/50000/50500', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50600', '考勤管理', '50000', '/50000/50600', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50700', '视频会议', '50000', '/50000/50700', '1', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('50800', '公司门禁', '50000', '/50000/50800', '1', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('60000', '系统管理', '0', '/60000', '2', '1', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('60100', '管理员管理', '60000', '/60000/60100', '2', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('60200', '业务授权', '60000', '/60000/60200', '2', '2', '2', '0', UTC_TIMESTAMP());


INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('1', '10000', '1', '10001', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('2', '10100', '1', '10002', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('3', '10400', '1', '10003', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('4', '10600', '1', '10004', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('5', '10800', '1', '10005', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('6', '11000', '1', '10006', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('7', '20000', '1', '10007', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('8', '20100', '1', '10008', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('9', '20400', '1', '10009', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('10', '20600', '1', '10010', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('11', '20800', '1', '10011', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('12', '30000', '1', '10012', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('13', '30500', '1', '10013', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('14', '31000', '1', '10014', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('15', '32000', '1', '10015', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('16', '34000', '1', '10016', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('17', '35000', '1', '10017', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('18', '37000', '1', '10018', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('19', '40000', '1', '10019', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('20', '40100', '1', '10020', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('21', '40200', '1', '10021', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('22', '40300', '1', '10022', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('23', '40400', '1', '10023', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('24', '40500', '1', '10024', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('25', '40600', '1', '10025', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('26', '40700', '1', '10026', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('27', '40750', '1', '10027', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('28', '40800', '1', '10028', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('29', '40900', '1', '10029', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('30', '41000', '1', '10030', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('31', '41100', '1', '10031', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('32', '41200', '1', '10032', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('33', '50000', '1', '10033', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('34', '50100', '1', '10034', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('35', '50200', '1', '10035', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('36', '50300', '1', '10036', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('37', '50400', '1', '10037', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('38', '50500', '1', '10038', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('39', '50600', '1', '10039', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('40', '50700', '1', '10040', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('41', '50800', '1', '10041', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('42', '60000', '1', '10042', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('43', '60100', '1', '10043', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('44', '60200', '1', '10044', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('45', '10700', '1', '10045', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('46', '10900', '1', '10046', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('47', '33000', '1', '10047', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('48', '38000', '1', '10048', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('49', '10850', '1', '10049', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('52', '10200', '1', '10052', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_web_menu_privileges` VALUES ('1008', '10002', '10100', '论坛/公告', '1', '1', '论坛/公告 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1009', '10003', '10400', '广告管理', '1', '1', '广告管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1010', '10004', '10600', '活动管理', '1', '1', '活动管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1011', '10005', '10800', '新闻管理', '1', '1', '新闻管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1012', '10006', '11000', '一键推送', '1', '1', '一键推送 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1013', '10045', '10700', '路演直播', '1', '1', '路演直播 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1014', '10046', '10900', '行业动态', '1', '1', '行业动态 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1015', '10049', '10850', '园区报', '1', '1', '园区报 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1021', '10008', '20100', '物业报修', '1', '1', '物业报修 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1022', '10009', '20400', '物业缴费', '1', '1', '物业缴费 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1023', '10010', '20600', '品质核查', '1', '1', '品质核查 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1024', '10011', '20800', '设备巡检', '1', '1', '设备巡检 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1034', '10013', '30500', '项目信息', '1', '1', '项目信息 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1035', '10014', '31000', '楼栋管理', '1', '1', '楼栋管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1036', '10015', '32000', '门牌管理', '1', '1', '门牌管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1037', '10016', '34000', '用户管理', '1', '1', '用户管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1038', '10017', '35000', '用户认证', '1', '1', '用户认证 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1039', '10018', '37000', '客户资料', '1', '1', '客户资料 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1040', '10047', '33000', '企业管理', '1', '1', '企业管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1041', '10048', '38000', '业主管理', '1', '1', '业主管理 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1059', '10020', '40100', '招租管理', '1', '1', '招租管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1060', '10021', '40200', '工位预订', '1', '1', '工位预订 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1061', '10022', '40300', '服务热线', '1', '1', '服务热线 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1062', '10023', '40400', '资源预订', '1', '1', '资源预订 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1063', '10024', '40500', '服务联盟', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1064', '10025', '40600', '创客空间', '1', '1', '创客空间 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1065', '10026', '40700', '结算管理', '1', '1', '结算管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1066', '10027', '40750', '运营统计', '1', '1', '运营统计 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1067', '10028', '40800', '停车缴费', '1', '1', '停车缴费 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1068', '10029', '40900', '车辆管理', '1', '1', '车辆管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1069', '10030', '41000', '大堂门禁', '1', '1', '大堂门禁 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1070', '10031', '41100', 'Wifi热点', '1', '1', 'Wifi热点 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1071', '10032', '41200', '一卡通', '1', '1', '一卡通 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1074', '10052', '10200', '园区简介', '1', '1', '园区简介 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1084', '10034', '50100', '组织架构', '1', '1', '组织架构 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1085', '10035', '50200', '岗位管理', '1', '1', '岗位管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1086', '10036', '50300', '职级管理', '1', '1', '职级管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1087', '10037', '50400', '人员管理', '1', '1', '人员管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1088', '10038', '50500', '认证管理', '1', '1', '认证管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1089', '10039', '50600', '考勤管理', '1', '1', '考勤管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1090', '10040', '50700', '视频会议', '1', '1', '视频会议 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1091', '10041', '50800', '公司门禁', '1', '1', '公司门禁 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ('1094', '10043', '60100', '管理员管理', '1', '1', '管理员管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('1095', '10044', '60200', '业务授权', '1', '1', '业务授权 全部权限', '710');

DELETE FROM eh_web_menus;
INSERT INTO `eh_web_menus` VALUES ('10000', '信息发布', '0', 'fa fa-volume-up', NULL, '1', '2', '/10000', 'park', '100');
INSERT INTO `eh_web_menus` VALUES ('10100', '论坛/公告', '10000', NULL, 'forum_notice', '0', '2', '/10000/10100', 'park', '110');
INSERT INTO `eh_web_menus` VALUES ('10200', '园区简介', '10000', NULL, 'park-intro', '0', '2', '/10000/10200', 'park', '111');
INSERT INTO `eh_web_menus` VALUES ('10400', '广告管理', '10000', NULL, 'banner_management', '0', '2', '/10000/10400', 'park', '140');
INSERT INTO `eh_web_menus` VALUES ('10600', '活动管理', '10000', NULL, 'forum_activity', '0', '2', '/10000/10600', 'park', '160');
INSERT INTO `eh_web_menus` VALUES ('10700', '路演直播', '10000', NULL, 'road_show', '0', '2', '/10000/10700', 'park', '170');
INSERT INTO `eh_web_menus` VALUES ('10800', '新闻管理', '10000', NULL, 'news_management', '0', '2', '/10000/10800', 'park', '180');

INSERT INTO `eh_web_menus` VALUES ('10850', '园区报', '10000', NULL, NULL, '1', '2', '/10000/10850', 'park', '181');
INSERT INTO `eh_web_menus` VALUES ('10851', '园区报管理', '10850', NULL, 'park_epaper_management', '0', '2', '/10000/10850/10851', 'park', '182');
INSERT INTO `eh_web_menus` VALUES ('10852', '约稿须知', '10850', NULL, 'manuscripts_notice', '0', '2', '/10000/10850/10852', 'park', '183');

INSERT INTO `eh_web_menus` VALUES ('10900', '行业动态', '10000', NULL, 'industry_dynamics', '0', '2', '/10000/10900', 'park', '185');
INSERT INTO `eh_web_menus` VALUES ('11000', '一键推送', '10000', NULL, 'message_push', '0', '2', '/10000/11000', 'park', '190');

INSERT INTO `eh_web_menus` VALUES ('20000', '物业服务', '0', 'fa fa-coffee', NULL, '1', '2', '/20000', 'park', '200');
INSERT INTO `eh_web_menus` VALUES ('20100', '物业报修', '20000', NULL, NULL, '1', '2', '/20000/20100', 'park', '201');
INSERT INTO `eh_web_menus` VALUES ('20110', '全部任务', '20100', NULL, 'all_task', '0', '2', '/20000/20100/20110', 'park', '205');
INSERT INTO `eh_web_menus` VALUES ('20120', '我的任务', '20100', NULL, 'my_task', '0', '2', '/20000/20100/20120', 'park', '210');
INSERT INTO `eh_web_menus` VALUES ('20130', '统计', '20100', NULL, 'statistics', '0', '2', '/20000/20100/20130', 'park', '215');
INSERT INTO `eh_web_menus` VALUES ('20140', '任务列表', '20100', NULL, 'task_management_list', '0', '2', '/20000/20100/20140', 'park', '220');
INSERT INTO `eh_web_menus` VALUES ('20150', '服务录入', '20100', NULL, 'task_management_service_entry', '0', '2', '/20000/20100/20150', 'park', '225');
INSERT INTO `eh_web_menus` VALUES ('20155', '设置', '20100', NULL, NULL, '0', '2', '/20000/20100/20155', 'park', '228');
INSERT INTO `eh_web_menus` VALUES ('20160', '执行人员设置', '20155', NULL, 'executive_setting', '0', '2', '/20000/20100/20155/20160', 'park', '230');
INSERT INTO `eh_web_menus` VALUES ('20170', '服务类型设置', '20155', NULL, 'service_type_setting', '0', '2', '/20000/20100/20155/20170', 'park', '235');
INSERT INTO `eh_web_menus` VALUES ('20180', '分类设置', '20155', NULL, 'classify_setting', '0', '2', '/20000/20100/20155/20180', 'park', '240');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
	VALUES ('20190', '统计', '20100', NULL, NULL, '0', '2', '/20000/20100/20190', 'park', '245');
INSERT INTO `eh_web_menus` VALUES ('20191', '服务统计', '20190', NULL, 'task_statistics', '0', '2', '/20000/20100/20190/20191', 'park', '180');
INSERT INTO `eh_web_menus` VALUES ('20192', '人员评分统计', '20190', NULL, 'staffScore_statistics', '0', '2', '/20000/20100/20190/20192', 'park', '181');

INSERT INTO `eh_web_menus` VALUES ('20400', '物业缴费', '20000', NULL, NULL, '1', '2', '/20000/20400', 'park', '250');
INSERT INTO `eh_web_menus` VALUES ('20410', '缴费记录', '20400', NULL, 'property_fee_record', '0', '2', '/20000/20400/20410', 'park', '252');
INSERT INTO `eh_web_menus` VALUES ('20420', '设置', '20400', NULL, 'property_fee_config', '0', '2', '/20000/20400/20420', 'park', '255');

INSERT INTO `eh_web_menus` VALUES ('20600', '品质核查', '20000', NULL, NULL, '1', '2', '/20000/20600', 'park', '260');
INSERT INTO `eh_web_menus` VALUES ('20610', '类型管理', '20600', NULL, 'react:/type-management/type-list', '1', '2', '/20000/20600/20610', 'park', '261');
INSERT INTO `eh_web_menus` VALUES ('20620', '规范管理', '20600', NULL, 'react:/specification-management/specification-list', '1', '2', '/20000/20600/20620', 'park', '265');
INSERT INTO `eh_web_menus` VALUES ('20630', '标准管理', '20600', NULL, 'reference_standard', '0', '2', '/20000/20600/20630', 'park', '266');
INSERT INTO `eh_web_menus` VALUES ('20640', '标准审批', '20600', NULL, 'react:/standard-check/standard-check-list', '0', '2', '/20000/20600/20640', 'park', '267');
INSERT INTO `eh_web_menus` VALUES ('20650', '任务查询', '20600', NULL, 'task_list', '0', '2', '/20000/20600/20650', 'park', '268');

INSERT INTO `eh_web_menus` VALUES ('20660', '统计', '20600', NULL, NULL, '1', '2', '/20000/20600/20660', 'park', '269');
INSERT INTO `eh_web_menus` VALUES ('20661', '分数统计', '20660', NULL, 'react:/statistics-management/fractional-report', '0', '2', '/20000/20600/20660/20661', 'park', '270');
INSERT INTO `eh_web_menus` VALUES ('20662', '任务数统计', '20660', NULL, 'react:/statistics-management/task-report', '0', '2', '/20000/20600/20660/20662', 'park', '271');

INSERT INTO `eh_web_menus` VALUES ('20670', '修改记录', '20600', NULL, 'edit_record', '0', '2', '/20000/20600/20670', 'park', '272');

INSERT INTO `eh_web_menus` VALUES ('20800', '设备巡检', '20000', NULL, NULL, '1', '2', '/20000/20800', 'park', '280');
INSERT INTO `eh_web_menus` VALUES ('20810', '参考标准', '20800', NULL, NULL, '1', '2', '/20000/20800/20810', 'park', '281');
INSERT INTO `eh_web_menus` VALUES ('20811', '标准列表', '20810', NULL, 'equipment_inspection_standard_list', '0', '2', '/20000/20800/20810/20811', 'park', '282');
INSERT INTO `eh_web_menus` VALUES ('20812', '设备关联审批', '20810', NULL, 'equipment_inspection_check_attachment', '0', '2', '/20000/20800/20810/20812', 'park', '283');
INSERT INTO `eh_web_menus` VALUES ('20820', '设备台帐', '20800', NULL, NULL, '1', '2', '/20000/20800/20820', 'park', '284');
INSERT INTO `eh_web_menus` VALUES ('20821', '设备列表', '20820', NULL, 'equipment_inspection_equipment_list', '0', '2', '/20000/20800/20820/20821', 'park', '285');
INSERT INTO `eh_web_menus` VALUES ('20822', '备品备件', '20820', NULL, 'equipment_inspection_sparepart_list', '0', '2', '/20000/20800/20820/20822', 'park', '286');
INSERT INTO `eh_web_menus` VALUES ('20830', '任务列表', '20800', NULL, NULL, '1', '2', '/20000/20800/20830', 'park', '287');
INSERT INTO `eh_web_menus` VALUES ('20831', '任务列表', '20830', NULL, 'equipment_inspection_task_list', '0', '2', '/20000/20800/20830/20831', 'park', '288');
INSERT INTO `eh_web_menus` VALUES ('20840', '巡检项资料库管理', '20800', NULL, NULL, '1', '2', '/20000/20800/20840', 'park', '289');
INSERT INTO `eh_web_menus` VALUES ('20841', '巡检项设置', '20840', NULL, 'equipment_inspection_inspection_item_list', '0', '2', '/20000/20800/20840/20841', 'park', '290');

INSERT INTO `eh_web_menus` VALUES ('30000', '项目管理', '0', 'fa fa-building', NULL, '1', '2', '/30000', 'park', '300');
INSERT INTO `eh_web_menus` VALUES ('30500', '项目信息', '30000', NULL, 'react:/project-classification/projects', '0', '2', '/30000/30500', 'park', '305');
INSERT INTO `eh_web_menus` VALUES ('31000', '楼栋管理', '30000', NULL, 'building_management', '0', '2', '/30000/31000', 'park', '310');
INSERT INTO `eh_web_menus` VALUES ('32000', '门牌管理', '30000', NULL, 'apartment_statistics', '0', '2', '/30000/32000', 'park', '320');
INSERT INTO `eh_web_menus` VALUES ('33000', '企业管理', '30000', NULL, 'enterprise_management', '0', '2', '/30000/33000', 'park', '330');
INSERT INTO `eh_web_menus` VALUES ('34000', '用户管理', '30000', NULL, 'user_management', '0', '2', '/30000/34000', 'park', '340');
INSERT INTO `eh_web_menus` VALUES ('35000', '用户认证', '30000', NULL, 'user_identify', '0', '2', '/30000/35000', 'park', '350');
INSERT INTO `eh_web_menus` VALUES ('37000', '客户资料', '30000', NULL, 'customer_management', '0', '2', '/30000/37000', 'park', '370');
INSERT INTO `eh_web_menus` VALUES ('38000', '业主管理', '30000', NULL, 'apartment_info', '0', '2', '/30000/38000', 'park', '360');

INSERT INTO `eh_web_menus` VALUES ('40000', '运营服务', '0', 'fa fa-comment', NULL, '1', '2', '/40000', 'park', '400');

INSERT INTO `eh_web_menus` VALUES ('40100', '招租管理', '40000', NULL, NULL, '1', '2', '/40000/40100', 'park', '410');
INSERT INTO `eh_web_menus` VALUES ('40110', '招租管理', '40100', NULL, 'rent_manage', '0', '2', '/40000/40100/40110', 'park', '412');
INSERT INTO `eh_web_menus` VALUES ('40120', '入住申请', '40100', NULL, 'enter_apply', '0', '2', '/40000/40100/40120', 'park', '414');

INSERT INTO `eh_web_menus` VALUES ('40200', '工位预订', '40000', NULL, NULL, '1', '2', '/40000/40200', 'park', '420');
INSERT INTO `eh_web_menus` VALUES ('40210', '空间管理', '40200', NULL, 'project_management', '0', '2', '/40000/40200/40210', 'park', '424');
INSERT INTO `eh_web_menus` VALUES ('40220', '预订详情', '40200', NULL, 'project_detail', '0', '2', '/40000/40200/40220', 'park', '426');

INSERT INTO `eh_web_menus` VALUES ('40300', '服务热线', '40000', NULL, 'service_hotline', '0', '2', '/40000/40300', 'park', '430');

INSERT INTO `eh_web_menus` VALUES ('40400', '资源预订', '40000', NULL, NULL, '1', '2', '/40000/40400', 'park', '440');
INSERT INTO `eh_web_menus` VALUES ('40410', '默认参数', '40400', NULL, 'default_parameters', '0', '2', '/40000/40400/40410', 'park', '441');
INSERT INTO `eh_web_menus` VALUES ('40420', '资源发布', '40400', NULL, 'resource_publish', '0', '2', '/40000/40400/40420', 'park', '444');
INSERT INTO `eh_web_menus` VALUES ('40430', '预订详情', '40400', NULL, 'rental_info', '0', '2', '/40000/40400/40430', 'park', '446');
INSERT INTO `eh_web_menus` VALUES ('40440', '退款处理', '40400', NULL, 'refund_management', '0', '2', '/40000/40400/40440', 'park', '448');

INSERT INTO `eh_web_menus` VALUES ('40500', '服务联盟', '40000', NULL, NULL, '1', '2', '/40000/40500', 'park', '450');
INSERT INTO `eh_web_menus` VALUES ('40510', '类型管理', '40500', NULL, 'service_type_management', '0', '2', '/40000/40500/40510', 'park', '451');
INSERT INTO `eh_web_menus` VALUES ('40520', '机构管理', '40500', NULL, 'service_alliance', '0', '2', '/40000/40500/40520', 'park', '453');
INSERT INTO `eh_web_menus` VALUES ('40530', '消息推送设置', '40500', NULL, 'message_push_setting', '0', '2', '/40000/40500/40530', 'park', '455');
INSERT INTO `eh_web_menus` VALUES ('40540', '申请记录', '40500', NULL, 'apply_record', '0', '2', '/40000/40500/40540', 'park', '457');

INSERT INTO `eh_web_menus` VALUES ('40600', '创客空间', '40000', NULL, 'market_zone', '0', '2', '/40000/40600', 'park', '460');

INSERT INTO `eh_web_menus` VALUES ('40700', '结算管理', '40000', NULL, 'settlement_management', '1', '2', '/40000/40700', 'park', '462');

INSERT INTO `eh_web_menus` VALUES ('40750', '运营统计', '40000', NULL, 'dailyActive--dailyActive', '0', '2', '/40000/40750', 'park', '464');

INSERT INTO `eh_web_menus` VALUES ('40800', '停车缴费', '40000', NULL, NULL, '1', '2', '/40000/40800', 'park', '470');
INSERT INTO `eh_web_menus` VALUES ('40810', '充值项管理', '40800', NULL, 'park_setting', '0', '2', '/40000/40800/40810', 'park', '471');
INSERT INTO `eh_web_menus` VALUES ('40820', '活动规则', '40800', NULL, 'park_rules', '0', '2', '/40000/40800/40820', 'park', '472');
INSERT INTO `eh_web_menus` VALUES ('40830', '月卡申请', '40800', NULL, 'park_card', '0', '2', '/40000/40800/40830', 'park', '473');
INSERT INTO `eh_web_menus` VALUES ('40840', '缴费记录', '40800', NULL, 'park_recharge', '0', '2', '/40000/40800/40840', 'park', '474');

INSERT INTO `eh_web_menus` VALUES ('40900', '车辆管理', '40000', NULL, 'car_management', '0', '2', '/40000/40900', 'park', '476');

INSERT INTO `eh_web_menus` VALUES ('41000', '大堂门禁', '40000', NULL, NULL, '1', '2', '/40000/41000', 'park', '478');
INSERT INTO `eh_web_menus` VALUES ('41010', '门禁管理', '41000', NULL, 'access_manage', '0', '2', '/40000/41000/41010', 'park', '479');
INSERT INTO `eh_web_menus` VALUES ('41020', '版本管理', '41000', NULL, 'version_manage', '0', '2', '/40000/41000/41020', 'park', '480');
INSERT INTO `eh_web_menus` VALUES ('41030', '门禁分组', '41000', NULL, 'access_group', '0', '2', '/40000/41000/41030', 'park', '481');
INSERT INTO `eh_web_menus` VALUES ('41040', '用户授权', '41000', NULL, 'user_auth', '0', '2', '/40000/41000/41040', 'park', '482');
INSERT INTO `eh_web_menus` VALUES ('41050', '访客授权', '41000', NULL, 'visitor_auth', '0', '2', '/40000/41000/41050', 'park', '483');
INSERT INTO `eh_web_menus` VALUES ('41060', '门禁日志', '41000', NULL, 'access_log', '0', '2', '/40000/41000/41060', 'park', '484');

INSERT INTO `eh_web_menus` VALUES ('41100', 'Wifi热点', '40000', NULL, 'wifi_hotspot', '0', '2', '/40000/41100', 'park', '485');

INSERT INTO `eh_web_menus` VALUES ('41200', '一卡通', '40000', NULL, NULL, '1', '2', '/40000/43500', 'park', '486');
INSERT INTO `eh_web_menus` VALUES ('41210', '开卡用户', '41200', NULL, 'card_user', '0', '2', '/40000/41200/41210', 'park', '487');
INSERT INTO `eh_web_menus` VALUES ('41220', '充值记录', '41200', NULL, 'card_recharge_record', '0', '2', '/40000/41200/41220', 'park', '488');
INSERT INTO `eh_web_menus` VALUES ('41230', '消费记录', '41200', NULL, 'card_purchase_record', '0', '2', '/40000/41200/41230', 'park', '489');

INSERT INTO `eh_web_menus` VALUES ('50000', '内部管理', '0', 'fa fa-group', NULL, '1', '2', '/50000', 'park', '505');

INSERT INTO `eh_web_menus` VALUES ('50100', '组织架构', '50000', NULL, NULL, '1', '2', '/50000/50100', 'park', '510');
INSERT INTO `eh_web_menus` VALUES ('50110', '组织架构', '50100', NULL, 'react:/system-architect/architect-list', '0', '2', '/50000/50100/50110', 'park', '511');

INSERT INTO `eh_web_menus` VALUES ('50200', '岗位管理', '50000', NULL, NULL, '0', '2', '/50000/50200', 'park', '521');
INSERT INTO `eh_web_menus` VALUES ('50210', '通用岗位', '50200', NULL, 'react:/rank-management/general', '0', '2', '/50000/50200/50210', 'park', '522');
INSERT INTO `eh_web_menus` VALUES ('50220', '岗位管理', '50200', NULL, 'react:/rank-management/rank-list', '0', '2', '/50000/50200/50220', 'park', '523');

INSERT INTO `eh_web_menus` VALUES ('50300', '职级管理', '50000', NULL, 'react:/level-management/level-list', '0', '2', '/50000/50300', 'park', '530');

INSERT INTO `eh_web_menus` VALUES ('50400', '人员管理', '50000', NULL, 'react:/employee-management/employee-list', '0', '2', '/50000/50400', 'park', '540');

INSERT INTO `eh_web_menus` VALUES ('50500', '认证管理', '50000', NULL, 'user_check', '0', '2', '/50000/50500', 'park', '541');

INSERT INTO `eh_web_menus` VALUES ('50600', '考勤管理', '50000', NULL, NULL, '1', '2', '/50000/50600', 'park', '543');
INSERT INTO `eh_web_menus` VALUES ('50630', '考勤规则', '50600', NULL, NULL, '1', '2', '/50000/50600/50630', 'park', '554');
INSERT INTO `eh_web_menus` VALUES ('50631', '通用规则设置', '50630', NULL, 'punch--generalSetting', '0', '2', '/50000/50600/50630/50631', 'park', '555');
INSERT INTO `eh_web_menus` VALUES ('50632', '特殊个人设置', '50630', NULL, 'punch--personalSetting', '0', '2', '/50000/50600/50630/50632', 'park', '556');
INSERT INTO `eh_web_menus` VALUES ('50633', '请假类型设置', '50630', NULL, 'leave_setting', '0', '2', '/50000/50600/50630/50633', 'park', '556');
INSERT INTO `eh_web_menus` VALUES ('50640', '打卡详情', '50600', NULL, 'punch_detail', '1', '2', '/50000/50600/50640', 'park', '557');
INSERT INTO `eh_web_menus` VALUES ('50650', '申请处理', '50600', NULL, NULL, '1', '2', '/50000/50600/50650', 'park', '559');
INSERT INTO `eh_web_menus` VALUES ('50651', '异常申请', '50650', NULL, 'abnormal_apply', '0', '2', '/50000/50600/50650/50651', 'park', '560');
INSERT INTO `eh_web_menus` VALUES ('50652', '请假申请', '50650', NULL, 'leave_apply', '0', '2', '/50000/50600/50650/50652', 'park', '561');
INSERT INTO `eh_web_menus` VALUES ('50653', '加班申请', '50650', NULL, 'punch--overTimeApply', '0', '2', '/50000/50600/50650/50653', 'park', '561');
INSERT INTO `eh_web_menus` VALUES ('50660', '考勤统计', '50600', NULL, 'punch_statistics', '1', '2', '/50000/50600/50660', 'park', '562');

INSERT INTO `eh_web_menus` VALUES ('50700', '视频会议', '50000', NULL, NULL, '1', '2', '/50000/50700', 'park', '570');
INSERT INTO `eh_web_menus` VALUES ('50710', '账号管理', '50700', NULL, 'account_manage', '0', '2', '/50000/50700/50710', 'park', '571');
INSERT INTO `eh_web_menus` VALUES ('50720', '我的订单', '50700', NULL, 'video_detail', '0', '2', '/50000/50700/50720', 'park', '572');
INSERT INTO `eh_web_menus` VALUES ('50730', '会议官网', '50700', NULL, 'url:http://meeting.zuolin.com', '0', '2', '/50000/50700/50730', 'park', '573');

INSERT INTO `eh_web_menus` VALUES ('50800', '公司门禁', '50000', NULL, NULL, '1', '2', '/50000/50800', 'park', '580');
INSERT INTO `eh_web_menus` VALUES ('50810', '门禁管理', '50800', NULL, 'access_manage_inside', '0', '2', '/50000/50800/50810', 'park', '581');
INSERT INTO `eh_web_menus` VALUES ('50820', '版本管理', '50800', NULL, 'version_manage_inside', '0', '2', '/50000/50800/50820', 'park', '582');
INSERT INTO `eh_web_menus` VALUES ('50830', '门禁分组', '50800', NULL, 'access_group_inside', '0', '2', '/50000/50800/50830', 'park', '583');
INSERT INTO `eh_web_menus` VALUES ('50840', '用户授权', '50800', NULL, 'user_auth_inside', '0', '2', '/50000/50800/50840', 'park', '584');
INSERT INTO `eh_web_menus` VALUES ('50850', '访客授权', '50800', NULL, 'visitor_auth_inside', '0', '2', '/50000/50800/50850', 'park', '585');
INSERT INTO `eh_web_menus` VALUES ('50860', '门禁日志', '50800', NULL, 'access_log_inside', '0', '2', '/50000/50800/50860', 'park', '586');

INSERT INTO `eh_web_menus` VALUES ('60000', '系统管理', '0', 'fa fa-group', NULL, '1', '2', '/60000', 'park', '600');

INSERT INTO `eh_web_menus` VALUES ('60100', '管理员管理', '60000', NULL, 'react:/admin-management/administrator', '0', '2', '/60000/60100', 'park', '610');
INSERT INTO `eh_web_menus` VALUES ('60200', '业务授权', '60000', NULL, 'react:/bussiness-authorization/department', '0', '2', '/60000/60200', 'park', '620');


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10001, '0', '信息发布 管理员', '信息发布 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10002, '0', '论坛/公告 管理员', '论坛/公告 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10003, '0', '广告管理 管理员', '广告管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10004, '0', '活动管理 管理员', '活动管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10005, '0', '新闻管理 管理员', '新闻管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10006, '0', '一键推送 管理员', '一键推送 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10007, '0', '物业服务 管理员', '物业服务 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10008, '0', '物业报修 管理员', '物业报修 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10009, '0', '物业缴费 管理员', '物业缴费 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10010, '0', '品质核查 管理员', '品质核查 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10011, '0', '设备巡检 管理员', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10012, '0', '项目管理 管理员', '项目管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10013, '0', '项目信息 管理员', '项目信息 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10014, '0', '楼栋管理 管理员', '楼栋管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10015, '0', '门牌管理 管理员', '门牌管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10016, '0', '用户管理 管理员', '用户管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10017, '0', '用户认证 管理员', '用户认证 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10018, '0', '客户资料 管理员', '客户资料 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10019, '0', '运营服务 管理员', '运营服务 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10020, '0', '招租管理 管理员', '招租管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10021, '0', '工位预订 管理员', '工位预订 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10022, '0', '服务热线 管理员', '服务热线 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10023, '0', '资源预订 管理员', '资源预订 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10024, '0', '服务联盟 管理员', '服务联盟 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10025, '0', '创客空间 管理员', '创客空间 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10026, '0', '结算管理 管理员', '结算管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10027, '0', '运营统计 管理员', '运营统计 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10028, '0', '停车缴费 管理员', '停车缴费 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10029, '0', '车辆管理 管理员', '车辆管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10030, '0', '大堂门禁 管理员', '大堂门禁 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10031, '0', 'Wifi热点 管理员', 'Wifi热点 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10032, '0', '一卡通 管理员', '一卡通 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10033, '0', '内部管理 管理员', '内部管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10034, '0', '组织架构 管理员', '组织架构 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10035, '0', '岗位管理 管理员', '岗位管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10036, '0', '职级管理 管理员', '职级管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10037, '0', '人员管理 管理员', '人员管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10038, '0', '认证管理 管理员', '认证管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10039, '0', '考勤管理 管理员', '考勤管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10040, '0', '视频会议 管理员', '视频会议 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10041, '0', '公司门禁 管理员', '公司门禁 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10042, '0', '系统管理 管理员', '系统管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10043, '0', '管理员管理 管理员', '管理员管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10044, '0', '业务授权 管理员', '业务授权 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10045, '0', '路演直播 管理员', '路演直播 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10046, '0', '行业动态 管理员', '行业动态 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10047, '0', '企业管理 管理员', '企业管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10048, '0', '业主管理 管理员', '业主管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10049, '0', '园区报 管理员', '园区报 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10050, '0', '场所预订 管理员', '场所预订 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10051, '0', '服务预约 管理员', '服务预约 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10052, '0', '园区简介 管理员', '园区简介 业务模块权限', NULL);

DELETE FROM eh_web_menu_privileges;

-- 考勤管理：打卡 菜单改变
DELETE FROM eh_acl_privileges WHERE id >= 790 AND id <= 794;
DELETE FROM eh_acl_privileges WHERE id >= 822 AND id <= 823;

SELECT * FROM eh_acl_privileges WHERE id >= 10001 AND id <= 10052;
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1001,0,1,NOW() FROM `eh_acl_privileges` WHERE id >= 10001 AND id <= 10052;

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,NOW() FROM `eh_service_module_privileges` WHERE module_id IN (SELECT id FROM eh_service_modules WHERE TYPE = 1);



-- 新增俱乐部菜单
INSERT INTO `eh_web_menus` VALUES ('10750', '俱乐部', '10000', NULL, 'groups', '0', '2', '/10000/10750', 'park', '180');
INSERT INTO `eh_web_menus` VALUES ('10751', '俱乐部管理', '10750', NULL, 'groups_management', '0', '2', '/10000/10750/10751', 'park', '181');
INSERT INTO `eh_web_menus` VALUES ('10752', '审核俱乐部', '10750', NULL, 'audit_groups', '0', '2', '/10000/10750/10752', 'park', '182');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('10750', '俱乐部', '10000', '/10000/10750', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10055, '0', '俱乐部 管理员', '俱乐部 业务模块权限', NULL);

INSERT INTO `eh_web_menu_privileges` VALUES ('166', '10055', '10750', '俱乐部管理', '1', '1', '俱乐部管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('167', '10055', '10751', '俱乐部管理', '1', '1', '俱乐部管理 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('168', '10055', '10752', '审核俱乐部', '1', '1', '审核俱乐部 全部权限', '710');

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('53', '10750', '1', '10055', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('54', '20100', '0', '904', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('55', '20100', '0', '805', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('56', '20100', '0', '331', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('57', '20100', '0', '332', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('58', '20100', '0', '333', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('59', '20100', '0', '920', '新的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('60', '20100', '0', '303', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('61', '20100', '0', '304', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('62', '20100', '0', '305', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('63', '20100', '0', '306', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('64', '20100', '0', '307', '老的物业报修权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('65', '10600', '0', '310', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('66', '10100', '0', '200', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('67', '41000', '0', '720', NULL, '0', UTC_TIMESTAMP());

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10055', '1001', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10055', '1005', '0', '1', UTC_TIMESTAMP());
	
DELETE FROM `eh_acls` WHERE `privilege_id` IN (604, 605) AND `role_id` = 1005;

-- 合同管理菜单, add by tt, 20161201
INSERT INTO `eh_web_menus` VALUES ('32500', '合同管理', '30000', NULL, 'contract_management', '0', '2', '/30000/32500', 'park', '325');
INSERT INTO `eh_web_menu_privileges` VALUES ('185', '10065', '32500', '合同管理', '1', '1', '合同管理 全部权限', '710');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('32500', '合同管理', '30000', '/30000/32500', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('75', '32500', '1', '10065', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10065, '0', '合同 管理员', '合同管理 业务模块权限', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10065', '1001', '0', '1', UTC_TIMESTAMP(), 'EhAclRoles');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10065', '1005', '0', '1', UTC_TIMESTAMP(), 'EhAclRoles');


-- 短信推送菜单, add by tt, 20161201
INSERT INTO `eh_web_menus` VALUES ('12200', '短信推送', '10000', NULL, 'sms_push', '0', '2', '/10000/12200', 'park', '325');
INSERT INTO `eh_web_menu_privileges` VALUES ('195', '10075', '12200', '短信推送', '1', '1', '短信推送 全部权限', '710');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('12200', '短信推送', '10000', '/10000/12200', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ('76', '12200', '1', '10075', NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10075, '0', '短信推送 管理员', '短信推送 业务模块权限', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10075', '1001', '0', '1', UTC_TIMESTAMP(), 'EhAclRoles');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10075', '1005', '0', '1', UTC_TIMESTAMP(), 'EhAclRoles');

-- 更新acl表
UPDATE `eh_acls` SET `role_type` = 'EhAclRoles' WHERE `role_type` IS NULL AND `owner_type` = 'EhOrganizations';


-- 设备巡检增加设备类型 add by xiongying20161129
SET @category_id = (SELECT MAX(id) FROM `eh_categories`);
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
    VALUES ((@category_id := @category_id + 1), '7', '0', '空调', '设备类型/空调', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
    VALUES ((@category_id := @category_id + 1), '7', '0', '给排水', '设备类型/给排水', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '0');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
    VALUES ((@category_id := @category_id + 1), '7', '0', '电梯', '设备类型/电梯', '0', '2', UTC_TIMESTAMP(), NULL, NULL, NULL, '0');

-- Officeasy白领活动和OE大讲堂
UPDATE eh_launch_pad_items SET action_data = '{"categoryId":1000001}' WHERE id IN(112574, 112585) AND namespace_id = 999985;

INSERT INTO `eh_activity_categories` (`id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`) VALUES ('1000001', 'OE大讲堂', '/1000001', '0', '2', '1', UTC_TIMESTAMP(), '999985');
INSERT INTO `eh_activity_categories` (`id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`) VALUES ('1000000', '白领活动', '/1000000', '0', '2', '1', UTC_TIMESTAMP(), '999985');


-- 初始化数据, add by tt, 20161117
INSERT INTO `eh_app_namespace_mappings` (`id`, `namespace_id`, `app_key`, `community_id`) VALUES (1, 1000000, '7757a75f-b79a-42fd-896e-107f4bfedd59', 240111044331048623);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES (5002, 1, '7757a75f-b79a-42fd-896e-107f4bfedd59', 'nM9PpqGaV2Qe5QqmNSHfWEJyvJjyo0r0f1wJgRadN9zWqcIwdU08FZYjyRSpa2vKmC/Mblh535WMKLiG/Ymr2Q==', 'jin die', 'kingdee', 1, '2016-11-09 11:49:16', NULL, NULL);
-- 短信模板，text后面需要改成实际的templateId, add by tt, 20161117
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 16, 'zh_CN', '发送短信给业务联系人和管理员', '18077', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 17, 'zh_CN', '合同到期前两个月发送短信（有客服人员）', '33376', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 18, 'zh_CN', '合同到期前两个月发送短信（无客服人员）', '33377', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 19, 'zh_CN', '合同到期前一个月发送短信（有客服人员）', '33378', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 20, 'zh_CN', '合同到期前一个月发送短信（无客服人员）', '33379', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 21, 'zh_CN', '发送短信给新企业（有客服人员）', '33380', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 22, 'zh_CN', '发送短信给新企业（无客服人员）', '33381', 1000000);

-- 添加 白领活动 & OE大讲堂 菜单 add by sw 20161201
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10058, '0', '白领活动 管理员', '白领活动 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10059, '0', 'OE大讲堂 管理员', 'OE大讲堂 业务模块权限', NULL);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10058', '1001', '0', '1', UTC_TIMESTAMP(), 'EhAclRoles');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10058', '1005', '0', '1', UTC_TIMESTAMP(), 'EhAclRoles');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10059', '1001', '0', '1', UTC_TIMESTAMP(), 'EhAclRoles');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10059', '1005', '0', '1', UTC_TIMESTAMP(), 'EhAclRoles');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
	VALUES ('10610', '白领活动', '10000', '/10000/10610', '0', '2', '2', '0', '2016-11-28 10:21:45');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) 
	VALUES ('10620', 'OE大讲堂', '10000', '/10000/10620', '0', '2', '2', '0', '2016-11-28 10:21:45');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES ('70', '10610', '1', '10058', NULL, '0', '2016-11-28 10:21:48');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES ('71', '10620', '1', '10059', NULL, '0', '2016-11-28 10:21:48');
SET @eh_service_module_scopes = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
	VALUES ((@eh_service_module_scopes := @eh_service_module_scopes + 1), '0', '10610', '', 'EhNamespaces', '999985', NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
	VALUES ((@eh_service_module_scopes := @eh_service_module_scopes + 1), '0', '10620', '', 'EhNamespaces', '999985', NULL, '2');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
	VALUES ('10610', '白领活动', '10000', NULL, 'white_collar_activity', '0', '2', '/10000/10600', 'park', '161');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
	VALUES ('10620', 'OE大讲堂', '10000', NULL, 'OE_auditorium', '0', '2', '/10000/10600', 'park', '162');

SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
	VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10058', '10610', '白领活动', '1', '1', '白领活动 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
	VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10059', '10620', '白领活动', '1', '1', '白领活动 全部权限', '710');

--
-- 能耗管理菜单   add by xq.tian  2016/11/29
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49100, '能耗管理', 40000, NULL, 'energy_management', 1, 2, '/40000/49100', 'park', 390);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49110, '表计管理', 49100, NULL, 'energy_table_management', 0, 2, '/40000/49100/49110', 'park', 391);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49120, '抄表记录', 49100, NULL, 'energy_table_record', 0, 2, '/40000/49100/49120', 'park', 392);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49130, '统计信息', 49100, NULL, 'energy_statistics_info', 0, 2, '/40000/49100/49130', 'park', 393);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (49140, '参数设置', 49100, NULL, 'energy_param_setting', 0, 2, '/40000/49100/49140', 'park', 394);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (422, 0, '能耗管理', '能耗管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 422, 49100, '能耗管理', 1, 1, '能耗管理  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 422, 49110, '能耗管理', 1, 1, '能耗管理  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 422, 49120, '能耗管理', 1, 1, '能耗管理  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 422, 49130, '能耗管理', 1, 1, '能耗管理  全部权限', 202);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 422, 49140, '能耗管理', 1, 1, '能耗管理  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 422, 1001, 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49100, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49110, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49120, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49130, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 49140, '', 'EhNamespaces', 999992, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES ('49100', '能耗管理', '40000', '/40000/49100', '0', '2', '2', '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ('68', '49100', '1', '422', NULL, '0', UTC_TIMESTAMP());



-- 自认证跳转页面，add by wh, 20161205
SET @id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'auth.success','http://core.zuolin.com/mobile/static/email_page/success.html','email auth verify success','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'auth.fail','http://core.zuolin.com/mobile/static/email_page/fail.html','email auth verify success','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES(@id:=@id+1,'auth.overtime','http://core.zuolin.com/mobile/static/email_page/overtime.html','email auth verify success','0',NULL);

-- 海岸 物业报修升级2.6  add by sw 20131205
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ('202500', '6', '0', '家政服务', '任务/家政服务', '0', '2', '2016-12-05 10:22:23', NULL, NULL, NULL, '999993');
INSERT INTO `eh_categories` (`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `delete_time`, `logo_uri`, `description`, `namespace_id`) 
	VALUES ('202501', '6', '0', '综合维修', '任务/综合维修', '0', '2', '2016-12-05 10:22:23', NULL, NULL, NULL, '999993');

delete from eh_launch_pad_items where id in (10373, 10700,10701,10702,10703, 10704,10705,10706,10707) and namespace_id = 999993;
update eh_launch_pad_items set action_type = 14, action_data = '{"url":"http://core.zuolin.com/property_service/index.html?taskCategoryId=202500&hideNavigationBar=1#/my_service#sign_suffix"}'
where id = 10370 and namespace_id = 999993;

update eh_launch_pad_items set action_type = 14, action_data = '{"url":"http://core.zuolin.com/property_service/index.html?taskCategoryId=202501&hideNavigationBar=1#/my_service#sign_suffix"}'
where id = 10372 and namespace_id = 999993;

update eh_launch_pad_items set action_type = 51, action_data = '', icon_uri = 'cs://1/image/aW1hZ2UvTVRvNU1UTmlOMlU0WkRjd05tRXhZVEZtWm1Jd016ZzJPVGt5WWpneU1UUXlZZw', item_label = '任务管理',item_name = 'PM_TASK'
where id = 10371 and namespace_id = 999993;


-- 华润oe只有一个企业时跳过列表页 by xiongying20161206
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ('1', '999985', '0');

-- 添加 业务权限下面的子菜单 add by sw 20161206
SET @web_menu_privilegel_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
	
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
SELECT (@web_menu_privilegel_id := @web_menu_privilegel_id + 1), privilege_id, mm.id , tm.name, '1', '1', tm.discription, tm.sort_num FROM
(SELECT t.* FROM eh_web_menus m
JOIN (SELECT * FROM eh_web_menu_privileges WHERE privilege_id >= 10001 AND privilege_id <= 10052 AND privilege_id NOT IN (10001, 10007, 10012, 10019, 10033, 10042)) t
ON m.id = t.menu_id
) tm JOIN eh_web_menus mm ON tm.menu_id = SUBSTRING_INDEX(SUBSTRING_INDEX(mm.path,'/',3), '/', -1) WHERE mm.id NOT IN (SELECT menu_id FROM eh_web_menu_privileges WHERE privilege_id>=10000);



-- 华润oe 原活动默认为白领活动 by xiongying20161209
update eh_activity_categories set default_flag = 1 where id = 1000000 and namespace_id = 999985;

-- 更新科技园 停车车主昵称 add by sw 20101206
update eh_parking_recharge_orders set recharge_type =1 where recharge_type = 0;
update eh_parking_recharge_orders set plate_owner_name = '陈程伟' where plate_number = '粤BD225W' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '李义鸿' where plate_number = '粤B1H0V6' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '胡益群' where plate_number = '粤BMP272' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '欧阳莉文' where plate_number = '粤B726ZF' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '杨川' where plate_number = '粤BG7Y32' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '武文彬' where plate_number = '粤BJ659P' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '周先生' where plate_number = '粤B182VN' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '张先生' where plate_number = '粤BVN732' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '张浩' where plate_number = '粤B217EL' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '谢科军' where plate_number = '粤B590VB' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '樊春来' where plate_number = '粤BT65Y0' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '王新涛' where plate_number = '粤B20P15' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '陈进光' where plate_number = '粤BV51E8' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '林庞慧' where plate_number = '粤BM05P5' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '凌杰' where plate_number = '粤BJ270M' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '罗勇峰' where plate_number = '粤B105CU' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '张哲楷' where plate_number = '粤B8Y1T1' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '杨先生' where plate_number = '粤BQ5G52' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '袁新旋' where plate_number = '粤BD898K' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '华先生' where plate_number = '粤BK256P' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '张学斌' where plate_number = '粤B36P88' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '李莎' where plate_number = '粤B2UC59' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '安先生' where plate_number = '粤B898LG' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '周先生' where plate_number = '粤B45G62' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '柯先生' where plate_number = '粤BK122T' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '胡浩' where plate_number = '粤B482E6' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '赵赛军' where plate_number = '粤B692BW' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '公车' where plate_number = '粤B3AM70' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '李小姐' where plate_number = '粤BKP750' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '万先生' where plate_number = '粤B636SZ' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '刘超成' where plate_number = '粤BP3N50' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '万先生' where plate_number = '粤B07W05' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '熊军' where plate_number = '粤BRZ660' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '曾恋帜' where plate_number = '粤B0C15W' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '胡鹏杰' where plate_number = '粤BP2218' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '陶祥' where plate_number = '粤BE83S8' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '徐申翔' where plate_number = '粤BN97R9' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '王旭' where plate_number = '粤B0640P' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '陈彬彬' where plate_number = '粤B6RL21' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '李新文' where plate_number = '粤B8X9K6' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '吴廷禹' where plate_number = '粤B9WW21' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '张立军' where plate_number = '粤B7JM09' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '王宇晨' where plate_number = '粤B850CY' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '吉高建' where plate_number = '粤A985S5' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '赖国俊' where plate_number = '粤BH77Z1' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '丁学刚' where plate_number = '粤A3910Y' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '周延斌' where plate_number = '粤BN377Y' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '王希' where plate_number = '粤BG539K' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '雷浩' where plate_number = '粤B251NP' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '程志兵' where plate_number = '粤B852TV' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '陈泽龙' where plate_number = '粤B618KX' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '郑强' where plate_number = '粤B739YU' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '川先生' where plate_number = '粤BV220M' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '刘江宾' where plate_number = '粤B4Z332' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '刘森华' where plate_number = '粤BE06K6' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '刘森华' where plate_number = '粤BE06K6' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '公车' where plate_number = '粤B700NB' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '叶芳' where plate_number = '粤B967NN' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '林先生' where plate_number = '粤BH79B5' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '周健' where plate_number = '粤B4271Q' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '熊锋' where plate_number = '粤B8726J' and create_time > '2016-11-01 00:00:00'; 
update eh_parking_recharge_orders set plate_owner_name = '陶耀军' where plate_number = '粤BM22N8' and create_time > '2016-11-01 00:00:00';

--
-- 能耗管理菜单调整  add by xq.tian  2016/12/08
--
UPDATE `eh_web_menus` SET `sort_num` = '490' WHERE `id` = '49100';
UPDATE `eh_web_menus` SET `sort_num` = '491' WHERE `id` = '49110';
UPDATE `eh_web_menus` SET `sort_num` = '492' WHERE `id` = '49120';
UPDATE `eh_web_menus` SET `sort_num` = '493' WHERE `id` = '49130';
UPDATE `eh_web_menus` SET `sort_num` = '494' WHERE `id` = '49140';


-- 储能科兴删除业务授权菜单 add by sfyan 20161230
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 60200 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999990;
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` = 60200 AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999983;

-- 深业增加两个item  add by sfyan 20170105
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1), '999992', '0', '0', '0', '/home', 'Bizs', '请示审批', '请示审批', 'cs://1/image/aW1hZ2UvTVRvMk5USXdNR0ZqWXpnMU5UTXdNemM0WXpKbFltUmpaalJqWVRVeVptVmpOdw', '1', '1', '14', '{\"url\":\"http://121.199.40.86/kpoa/app/hoa_appintegrated.nsf/FM_MyFlow?openform\"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1), '999992', '0', '0', '0', '/home', 'Bizs', '请示审批', '请示审批', 'cs://1/image/aW1hZ2UvTVRvMk5USXdNR0ZqWXpnMU5UTXdNemM0WXpKbFltUmpaalJqWVRVeVptVmpOdw', '1', '1', '14', '{\"url\":\"http://121.199.40.86/kpoa/app/hoa_appintegrated.nsf/FM_MyFlow?openform\"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', NULL);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1), '999992', '0', '0', '0', '/home', 'Bizs', '企业公告', '企业公告', 'cs://1/image/aW1hZ2UvTVRveU9EZGxPRE15WlRSa09UQmlPRGN6TVRoaFlqTTJObUUyWVdKa1pHTXdaUQ', '1', '1', '14', '{\"url\":\"http://121.199.40.86/kpoa/app/hoa_appintegrated.nsf/FM_MyNews?openform\"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '0', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`) VALUES ((@item_id := @item_id + 1), '999992', '0', '0', '0', '/home', 'Bizs', '企业公告', '企业公告', 'cs://1/image/aW1hZ2UvTVRveU9EZGxPRE15WlRSa09UQmlPRGN6TVRoaFlqTTJObUUyWVdKa1pHTXdaUQ', '1', '1', '14', '{\"url\":\"http://121.199.40.86/kpoa/app/hoa_appintegrated.nsf/FM_MyNews?openform\"}', '2', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', NULL);
