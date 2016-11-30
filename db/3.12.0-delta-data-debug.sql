-- merge from organization-delta-data-release.sql by lqs 20161128
-- by sfyan 获取电商平台的店铺信息接口
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'get.businesses.info.api', 'zl-ec/rest/openapi/shop/listByCondition', '获取店铺信息');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
VALUES ('300', 'organization', '600001', 'zh_CN', '通用岗位已存在');

-- merge from activityentry-delta-data-release.sql by xiongying20161128
update eh_activities a set forum_id = (select forum_id from eh_forum_posts where id = a.post_id);
update eh_activities a set creator_tag = (select creator_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set target_tag = (select target_tag from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_type = (select visible_region_type from eh_forum_posts where id = a.post_id);
update eh_activities a set visible_region_id = (select visible_region_id from eh_forum_posts where id = a.post_id);

-- merge from sa1.7-delta-data-release.sql by xiongying20161128
-- 新增预约看楼模板 add by xiongying 20161116
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('9', 'Apartment', '预约看楼', '预约看楼', '1', '1', ' {"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"userName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"string","fieldContentType":"text","fieldDesc":"mobile","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业名称","fieldType":"string","fieldContentType":"text","fieldDesc":"organizationName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"areaSize","fieldDisplayName":"面积需求","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入面积需求","requiredFlag":"1","dynamicFlag":"0"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"（选填）其他说明","requiredFlag":"0","dynamicFlag":"0"}]}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             ', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (10, '999985', '9');

-- 物业报修2.6 merge from pmtask-delta-data.sql by sw 20161128 
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ('340', 'pmtask', '10007', 'zh_CN', '该单已被其他人处理，请返回主界面刷新任务');

delete from eh_locale_templates where scope = 'pmtask.notification' and code = 7;

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('205', 'pmtask.notification', '7', 'zh_CN', '任务操作模版', '${creatorName} ${creatorPhone}已发起一个${categoryName}单，请尽快处理', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('350', 'sms.default.yzx', '15', 'zh_CN', '物业任务3-深业', '32949', '999992');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`) 
VALUES ('20190', '统计', '20100', NULL, null, '0', '2', '/20000/20100/20190', 'park', '245');
INSERT INTO `eh_web_menus` VALUES ('20191', '服务统计', '20190', null, 'task_statistics', '0', '2', '/20000/20100/20190/20191', 'park', '180');
INSERT INTO `eh_web_menus` VALUES ('20192', '人员评分统计', '20190', null, 'staffScore_statistics', '0', '2', '/20000/20100/20190/20192', 'park', '181');


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1020, '0', '人员评分统计', '人员评分统计', NULL);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '1020', '1001', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '1020', '1002', '0', '1', UTC_TIMESTAMP());

-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10056, '0', '俱乐部管理', '俱乐部管理 全部权限', NULL);
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10057, '0', '审核俱乐部', '审核俱乐部 全部权限', NULL);

INSERT INTO `eh_web_menu_privileges` VALUES ('300', '907', '20191', '服务统计', '1', '1', '服务统计 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ('301', '907', '20191', '人员评分统计', '1', '1', '人员评分统计 全部权限', '710');

--
-- 修改能耗管理的入口页面地址  add by xq.tian  2016/11/30
--
UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"http://alpha.lab.everhomes.com/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' WHERE `item_name` = 'Energy' AND `namespace_id` = '999992';
