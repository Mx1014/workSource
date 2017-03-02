
-- 电商运营数据api   add by xq.tian 2017/03/01
SET @conf_id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES(@conf_id := @conf_id+1, 'biz.business.promotion.api', '/Zl-MallMgt/shopCommo/admin/queryRecommendList.ihtml', 'biz promotion data api', '0', '电商首页运营数据api');

UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fmicroshop%2fhome%3fisfromindex%3d0%26_k%3dzlbiz%23sign_suffix"}'
WHERE `namespace_id`=999985 AND `item_group`='OPPushBiz' AND `item_label`='OE优选';

-- 问卷调查的错误提示， add by tt, 20170220
SELECT MAX(id) INTO @id FROM eh_locale_strings;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '1', 'zh_CN', '问卷名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '2', 'zh_CN', '问卷名称不能超过50个字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '3', 'zh_CN', '题目名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '4', 'zh_CN', '至少需要有一个题目');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '5', 'zh_CN', '至少需要有一个选项');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '6', 'zh_CN', '选项名称不能为空');


--
-- 问卷调查菜单，add by tt, 20170221
--
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (40150, '企业问卷调查', 40000, NULL, 'react:/questionnaire-survey/questionnaire-management/40150', 1, 2, '/40000/40150', 'park', 495);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (80800, '问卷调查', 80000, NULL, 'react:/questionnaire-survey/questionnaire-list', 1, 2, '/80000/80800', 'park', 810);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (20025, 0, '企业问卷调查 管理员', '企业问卷调 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (20026, 0, '问卷调查 管理员', '企业问卷调 业务模块权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES((@web_menu_privilege_id := @web_menu_privilege_id + 1),'20025','40150','企业问卷调查','1','1','企业问卷调查 全部权限','710');
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES((@web_menu_privilege_id := @web_menu_privilege_id + 1),'20026','80800','问卷调查','1','1','问卷调查 全部权限','710');

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 20025, 1001, 0, 1, NOW(), 'EhAclRoles');
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `role_type`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 20026, 1005, 0, 1, NOW(), 'EhAclRoles');

-- 配置到清华信息港，add by tt, 20170221
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 40150, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80800, '', 'EhNamespaces', 999984, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80000, '', 'EhNamespaces', 999984, 2);

-- 以下数据不一定用得到，我看alpha环境是有的，我本地没有，add by tt, 20170221
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) VALUES (80000, '园区服务', 0, 'fa fa-group', NULL, 1, 2, '/80000', 'park', 800, NULL);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (11911, 10120, 80000, '园区服务', 1, 1, '园区服务 全部权限', 710);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10120, 0, '园区服务 管理员', '园区服务 业务模块权限', NULL);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES (9983, 'EhOrganizations', NULL, 1, 10120, 1005, 0, 1, '2017-01-06 18:36:05', 0, 'EhAclRoles', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
-- 以上数据不一定用得到

-- 物业报修2.8 add by sw 20170301
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10138, '0', '物业报修 代发权限', '物业报修 代发权限', NULL);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
	SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1001, 0, 1, now(), 'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10138;

INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10012', 'zh_CN', '没有代发权限！');
INSERT INTO `eh_locale_strings` ( `scope`, `code`, `locale`, `text`) VALUES ('pmtask', '10013', 'zh_CN', '查不到该用户信息！');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('techpark.oa.url', 'http://oa.ssipc.com.cn:8890/oa/service/WorkflowAppDraftWebService?wsdl', NULL, '0', NULL);


--
-- 华润OE首页布局修改   add by xq.tian 2017/03/01
--
UPDATE `eh_launch_pad_layouts` SET version_code='2017030101', `layout_json`='{"versionCode": "2017030101","versionName": "4.3.1","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 0,"separatorHeight": 0}, {"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 3,"separatorFlag": 1,"separatorHeight": 2}, {"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Default","defaultOrder": 5,"separatorFlag": 1,"separatorHeight": 21}, {"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize": 3,"entityCount": 3,"subjectHeight": 1,"descriptionHeight": 0},"style": "ListView","defaultOrder": 6,"separatorFlag": 1,"separatorHeight": 21,"columnCount": 1}, {"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushBiz","newsSize": 6,"entityCount": 6,"subjectHeight": 1,"descriptionHeight": 0},"style": "HorizontalScrollView","defaultOrder": 7,"separatorFlag": 1,"separatorHeight": 0,"columnCount": 0}]}'
WHERE `namespace_id`=999985 AND `name`='ServiceMarketLayout';

-- 创源停车缴费 add by sw 20170302
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`, `tempfee_flag`, `rate_flag`, `recharge_month_count`, `recharge_type`, `namespace_id`, `is_support_recharge`) 
	VALUES ('10023', 'community', '240111044331054735', '创源停车场', 'INNOSPRING', '', '1', '2', '1025', '2017-03-02 17:07:20', '1', '1', '1', '1', '1', '999986', '0');
UPDATE eh_launch_pad_items set action_data = '', action_type = 30 where namespace_id = 999986 and item_label = '智能停车场';
