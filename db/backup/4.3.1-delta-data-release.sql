
-- 电商运营数据api   add by xq.tian 2017/03/01
SET @conf_id := (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES(@conf_id := @conf_id+1, 'biz.business.promotion.api', '/Zl-MallMgt/shopCommo/admin/queryRecommendList.ihtml', 'biz promotion data api', '0', '电商首页运营数据api');

UPDATE `eh_launch_pad_items` SET `action_data`='{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fmicroshop%2fhome%3fisfromindex%3d0%26_k%3dzlbiz#sign_suffix"}'
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

SET @eh_service_module_privileges = (SELECT MAX(id) FROM `eh_service_module_privileges`);

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20100', '0', '10138', '老的物业报修权限', '0', '2017-02-22 10:51:38');


INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('techpark.oa.url', 'http://oa.ssipc.com.cn:8890/oa/service/WorkflowAppDraftWebService?wsdl', NULL, '0', NULL);


-- merge from sa2.1 by xiongying20170301 & issues7061

SET @layout_id = (SELECT max(id) FROM `eh_launch_pad_layouts`);  
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES ((@layout_id := @layout_id + 1), 999985, 'SecondServiceMarketLayout', '{"versionCode":"2017030701","versionName":"4.1.3","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21,"columnCount":2},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"Gallery", "entityCount": 6, "newsSize":  5},"style":"LargeImageListView","defaultOrder":2,"separatorFlag":0,"separatorHeight":0}]}', '2017030701', '0', '2', UTC_TIMESTAMP(), 'pm_admin');	
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type) 
	VALUES ((@layout_id := @layout_id + 1), 999985, 'SecondServiceMarketLayout', '{"versionCode":"2017030701","versionName":"4.1.3","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21,"columnCount":2},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"Gallery", "entityCount": 6, "newsSize":  5},"style":"LargeImageListView","defaultOrder":2,"separatorFlag":0,"separatorHeight":0}]}', '2017030701', '0', '2', UTC_TIMESTAMP(), 'park_tourist');	

    
    
    
    
SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 

SET @layout_id = (SELECT max(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type)
	VALUES ((@layout_id := @layout_id + 1), 999985, 'SecondServiceMarketLayout', '{"versionCode":"2017022202","versionName":"4.1.3","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"Gallery", "entityCount": 6, "newsSize":  5},"style":"LargeImageListView","defaultOrder":2,"separatorFlag":0,"separatorHeight":0}]}', '2017022202', '0', '2', UTC_TIMESTAMP(), 'pm_admin');
INSERT INTO `eh_launch_pad_layouts`(id, namespace_id, name, layout_json, version_code, min_version_code, status, create_time, scene_type)
	VALUES ((@layout_id := @layout_id + 1), 999985, 'SecondServiceMarketLayout', '{"versionCode":"2017022202","versionName":"4.1.3","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"GovAgencies"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":21,"columnCount":4},{"groupName":"","widget":"OPPush","instanceConfig":{"itemGroup":"Gallery", "entityCount": 6, "newsSize":  5},"style":"LargeImageListView","defaultOrder":2,"separatorFlag":0,"separatorHeight":0}]}', '2017022202', '0', '2', UTC_TIMESTAMP(), 'park_tourist');





SET @item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)

    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '业主查询', '业主查询', 'cs://1/image/aW1hZ2UvTVRwbE5ESmpNamRqTURobU5qWXlZMlkyT0dNME1HRTRNV1kwWldWbE1qZGhPUQ', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '租户查询', '租户查询', 'cs://1/image/aW1hZ2UvTVRvNE1tUTBNR016T1dKaVptRXpZamxsTmpNMU5HSXpZV0V5TTJRNFkyVTRPUQ', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@item_id := @item_id + 1), 999985, 0, 0, 0, '/secondhome', 'Gallery', '项目介绍', '项目介绍', 'cs://1/image/aW1hZ2UvTVRvMVkyVm1Oak5oWlRoaE56UTNNV1EwWVRKaU4yRmtNalptT1RZNFpEazFPQQ', 1, 1, 33, '{"type":200971,"parentId":200971,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '0', 'pm_admin');


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '业主查询', '业主查询', 'cs://1/image/aW1hZ2UvTVRwbE5ESmpNamRqTURobU5qWXlZMlkyT0dNME1HRTRNV1kwWldWbE1qZGhPUQ', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@item_id := @item_id + 1), 999985, '0', '0', '0', '/secondhome', 'GovAgencies', '租户查询', '租户查询', 'cs://1/image/aW1hZ2UvTVRvNE1tUTBNR016T1dKaVptRXpZamxsTmpNMU5HSXpZV0V5TTJRNFkyVTRPUQ', '1', '1', 14,'{"url":"http://zuolin.com/mobile/static/coming_soon/index.html"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');



INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@item_id := @item_id + 1), 999985, 0, 0, 0, '/secondhome', 'Gallery', '项目介绍', '项目介绍', 'cs://1/image/aW1hZ2UvTVRvMVkyVm1Oak5oWlRoaE56UTNNV1EwWVRKaU4yRmtNalptT1RZNFpEazFPQQ', 1, 1, 33, '{"type":200971,"parentId":200971,"displayType": "grid"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '0', 'park_tourist');


-- app url更新数据
delete from eh_app_urls;


INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('1', '0', '左邻', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.yjtc.everhomes', 'cs://1/image/aW1hZ2UvTVRwbU5qQXhOVFJtWW1FNU5UazNObUkyTldFeU5HWTFOekJpTWpWaU5XUTNNUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('2', '0', '左邻', '1', 'https://itunes.apple.com/cn/app/%E5%B7%A6%E9%82%BB/id767285002?mt=8', 'cs://1/image/aW1hZ2UvTVRwbU5qQXhOVFJtWW1FNU5UazNObUkyTldFeU5HWTFOekJpTWpWaU5XUTNNUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('3', '1000000', '深圳科技园', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.park.tec_park', 'cs://1/image/aW1hZ2UvTVRvMllqaG1NalpoT1RkaU9HVm1OalJoTVRnek1tWTNPV1l5WmpOalpHUTNOZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('4', '1000000', '深圳科技园', '1', 'https://itunes.apple.com/cn/app/%E6%B7%B1%E5%9C%B3%E7%A7%91%E6%8A%80%E5%9B%AD/id1067766906?mt=8', 'cs://1/image/aW1hZ2UvTVRvMllqaG1NalpoT1RkaU9HVm1OalJoTVRnek1tWTNPV1l5WmpOalpHUTNOZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('5', '999993', '海岸馨服务', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.haian.park', 'cs://1/image/aW1hZ2UvTVRvME9XTTRNV0ZsTm1FMU9XWTFNR1ExT0RGa05ERTFZMlppTVRBMFpqVXhZUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('6', '999993', '海岸馨服务', '1', 'https://itunes.apple.com/cn/app/%E6%B5%B7%E5%B2%B8%E9%A6%A8%E6%9C%8D%E5%8A%A1/id1084272463?mt=8', 'cs://1/image/aW1hZ2UvTVRvME9XTTRNV0ZsTm1FMU9XWTFNR1ExT0RGa05ERTFZMlppTVRBMFpqVXhZUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('7', '999992', '深业物业', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.shenye', 'cs://1/image/aW1hZ2UvTVRvM09EZ3daR1JrWkRZM01qZGxObUU1T0dFeFlqWTBOemxoWW1JMU1qZzNOdw', '做最值得托付的物业服务集成商');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('8', '999992', '深业物业', '1', 'https://itunes.apple.com/cn/app/%E6%B7%B1%E4%B8%9A%E7%89%A9%E4%B8%9A/id1113956010?mt=8', 'cs://1/image/aW1hZ2UvTVRvM09EZ3daR1JrWkRZM01qZGxObUU1T0dFeFlqWTBOemxoWW1JMU1qZzNOdw', '做最值得托付的物业服务集成商');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('9', '999999', '中洲智邦', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.park.xmtec', 'cs://1/image/aW1hZ2UvTVRveE1tRTBOVGN6TmpBMllqVXpOems1WlRnMVpUZzJaalF5WW1SbE56Z3lPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('10', '999999', '中洲智邦', '1', 'https://itunes.apple.com/cn/app/%E4%B8%AD%E6%B4%B2%E6%99%BA%E9%82%A6/id1070099370?mt=8', 'cs://1/image/aW1hZ2UvTVRveE1tRTBOVGN6TmpBMllqVXpOems1WlRnMVpUZzJaalF5WW1SbE56Z3lPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('11', '999990', 'UFine', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.chuneng.park', 'cs://1/image/aW1hZ2UvTVRwak1ESXhNelEzT1dVMU5XTmlZbUk1TldNNE1EQXpZMkUwTkdWaE5qVXpNQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('12', '999990', 'UFine', '1', 'https://itunes.apple.com/cn/app/ufine/id1112434179?mt=8', 'cs://1/image/aW1hZ2UvTVRwak1ESXhNelEzT1dVMU5XTmlZbUk1TldNNE1EQXpZMkUwTkdWaE5qVXpNQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('13', '999989', 'Ibase', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.ibase', 'cs://1/image/aW1hZ2UvTVRveU5tSTRaVFZoTWpRM05EaGpOVEUxTlRKaE16RTFZVE5oTW1WbVlUQTJOdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('14', '999989', 'Ibase', '1', 'https://itunes.apple.com/cn/app/ibase/id1133031529?mt=8', 'cs://1/image/aW1hZ2UvTVRveU5tSTRaVFZoTWpRM05EaGpOVEUxTlRKaE16RTFZVE5oTW1WbVlUQTJOdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('15', '999991', '威新LINK+', '2', 'http://www.wandoujia.com/apps/com.everhomes.android.jindi.park', 'cs://1/image/aW1hZ2UvTVRwaFl6VXhaV0ZpTldNNVpXSTVNMkppTnpSak1qVmtNREkyTldRMU1qTXlPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('16', '999991', '威新LINK+', '1', 'https://itunes.apple.com/cn/app/%E5%A8%81%E6%96%B0link/id1112433619?mt=8', 'cs://1/image/aW1hZ2UvTVRwaFl6VXhaV0ZpTldNNVpXSTVNMkppTnpSak1qVmtNREkyTldRMU1qTXlPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('17', '999988', 'π星球爱特家', '2', 'http://app.mi.com/details?id=com.everhomes.android.atmini', 'cs://1/image/aW1hZ2UvTVRveU5qa3lZVFUzTlRRd01XWXpNRGd6WldWaU5ETTBNMk13TVdZd1lqUTVOZw', 'π星球智慧社区');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('18', '999988', 'π星球爱特家', '1', 'https://itunes.apple.com/cn/app/%CF%80%E6%98%9F%E7%90%83%E7%88%B1%E7%89%B9%E5%AE%B6/id1153945560?mt=8', 'cs://1/image/aW1hZ2UvTVRveU5qa3lZVFUzTlRRd01XWXpNRGd6WldWaU5ETTBNMk13TVdZd1lqUTVOZw', 'π星球智慧社区');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('19', '999987', '深圳湾', '2', 'http://www.wandoujia.com/apps/com.everhomes.android.shenzhenbay', 'cs://1/image/aW1hZ2UvTVRvNVlUSTNOR1l4TlRsaVlUTXlPRFJrWWpRMk9EQmtPRGd5T1dZMU9HWTBZdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('20', '999987', '深圳湾', '1', 'https://itunes.apple.com/cn/app/%E6%B7%B1%E5%9C%B3%E6%B9%BE%E5%88%9B%E4%B8%9A%E5%B9%BF%E5%9C%BA/id1163458833?mt=8', 'cs://1/image/aW1hZ2UvTVRvNVlUSTNOR1l4TlRsaVlUTXlPRFJrWWpRMk9EQmtPRGd5T1dZMU9HWTBZdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('21', '999986', '创源', '2', 'http://sj.qq.com/myapp/detail.htm?apkName=com.everhomes.android.innospring', 'cs://1/image/aW1hZ2UvTVRvd1pXRTVOV1UxWVdJeU0ySXlZemxoWlRKaFlqazBZVGswTlRobVlqZGpaZw', '全球创业梦想，从创源开始');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('22', '999986', '创源', '1', 'https://itunes.apple.com/cn/app/%E5%88%9B%E6%BA%90/id1160563917?mt=8', 'cs://1/image/aW1hZ2UvTVRvd1pXRTVOV1UxWVdJeU0ySXlZemxoWlRKaFlqazBZVGswTlRobVlqZGpaZw', '全球创业梦想，从创源开始');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('23', '999985', 'Officeasy', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.officeasy', 'cs://1/image/aW1hZ2UvTVRvd1pUVm1OemRtWXpWak5UZGhOMlF5TkRNNVlqTTNZalV4T0RNM05qZzFPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('24', '999985', 'Officeasy', '1', 'https://itunes.apple.com/cn/app/officeasy/id1168934037?mt=8', 'cs://1/image/aW1hZ2UvTVRvd1pUVm1OemRtWXpWak5UZGhOMlF5TkRNNVlqTTNZalV4T0RNM05qZzFPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('25', '999983', '正中会', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.kexin', 'cs://1/image/aW1hZ2UvTVRwa01tSTJZVFUzTVRrMk1qZ3pZekJpWXpkaVpUY3pOemc1WVdVeU1qQTFZUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('26', '999983', '正中会', '1', 'https://itunes.apple.com/cn/app/%E6%AD%A3%E4%B8%AD%E4%BC%9A/id1186600259?mt=8', 'cs://1/image/aW1hZ2UvTVRwa01tSTJZVFUzTVRrMk1qZ3pZekJpWXpkaVpUY3pOemc1WVdVeU1qQTFZUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('27', '999984', '清华信息港', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.tsinghua.infobay', 'cs://1/image/aW1hZ2UvTVRwaU1UVmpNelk1T0dOa00ySXlaVE5pWlRVMU1XRXpZV1ZsTUdZMU1HUmlaQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('28', '999984', '清华信息港', '1', 'https://itunes.apple.com/cn/app/%E7%B4%AB%E8%8D%86/id1187170142?mt=8', 'cs://1/image/aW1hZ2UvTVRwaU1UVmpNelk1T0dOa00ySXlaVE5pWlRVMU1XRXpZV1ZsTUdZMU1HUmlaQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('29', '999982', 'T+SPACE', '2', 'http://www.wandoujia.com/apps/com.everhomes.android.tspace', 'cs://1/image/aW1hZ2UvTVRvME5ETTJOR1pqTVRJM1ptUm1NbUk0WldSbU1tTXlZV0k0T1RCbVlqZzJPQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('30', '999982', 'T+SPACE', '1', 'https://itunes.apple.com/cn/app/t-space/id1185749287?mt=8', 'cs://1/image/aW1hZ2UvTVRvME5ETTJOR1pqTVRJM1ptUm1NbUk0WldSbU1tTXlZV0k0T1RCbVlqZzJPQQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('31', '999981', '星商汇园区', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.vanke.xsh', 'cs://1/image/aW1hZ2UvTVRvNU5UaGlaV0U0WlRWaU1UZzNOVEU0Tm1Zd1lqTTRNVFZqWVRjek1EUXlPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`)
    VALUES ('32', '999981', '星商汇园区', '1', 'https://itunes.apple.com/cn/app/%E6%98%9F%E5%95%86%E6%B1%87%E5%9B%AD%E5%8C%BA/id1194430405?mt=8', 'cs://1/image/aW1hZ2UvTVRvNU5UaGlaV0U0WlRWaU1UZzNOVEU0Tm1Zd1lqTTRNVFZqWVRjek1EUXlPUQ', '移动平台聚合服务，助力园区效能提升');


--
-- 华润OE首页布局修改   add by xq.tian 2017/03/01
--
UPDATE `eh_launch_pad_layouts` SET version_code='2017030101', `layout_json`='{"versionCode": "2017030101","versionName": "4.3.1","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 0,"separatorHeight": 0}, {"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default","rowCount": 1},"style": "Default","defaultOrder": 3,"separatorFlag": 1,"separatorHeight": 2}, {"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Default","defaultOrder": 5,"separatorFlag": 1,"separatorHeight": 21}, {"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize": 3,"entityCount": 3,"subjectHeight": 1,"descriptionHeight": 0},"style": "ListView","defaultOrder": 6,"separatorFlag": 1,"separatorHeight": 21,"columnCount": 1}, {"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushBiz","newsSize": 6,"entityCount": 6,"subjectHeight": 1,"descriptionHeight": 0},"style": "HorizontalScrollView","defaultOrder": 7,"separatorFlag": 1,"separatorHeight": 0,"columnCount": 0}]}'
WHERE `namespace_id`=999985 AND `name`='ServiceMarketLayout';

-- 创源停车缴费 add by sw 20170302
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`, `tempfee_flag`, `rate_flag`, `recharge_month_count`, `recharge_type`, `namespace_id`, `is_support_recharge`)
	VALUES ('10023', 'community', '240111044331054735', '创源停车场', 'INNOSPRING', '', '1', '2', '1025', '2017-03-02 17:07:20', '1', '1', '0', '1', '1', '999986', '0');
UPDATE eh_launch_pad_items set action_data = '', action_type = 30 where namespace_id = 999986 and item_label = '智能停车场';
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.innospring.version', '1.4', '', '999986', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.innospring.licensekey', '44030520000420161231235959102643', '', '999986', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.innospring.parkName', '未来科技城123', '', '999986', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('parking.innospring.serverUrl', 'http://120.55.114.140:8085/kesb_req', '', '999986', NULL);


--  新增删帖删评论权限 add by sfyan 20170228
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10140, '0', '普通贴删除权限', '路演直播 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10141, '0', '公告贴删除权限', '行业动态 业务模块权限', NULL);
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10142, '0', '非官方活动贴删除权限', '企业管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10143, '0', '官方活动贴删除权限', '业主管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10150, '0', '普通评论删除权限', '园区报 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10151, '0', '公告评论删除权限', '公告评论删除权限', NULL);
-- INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10152, '0', '非官方活动评论删除权限', '非官方活动评论删除权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10153, '0', '官方活动评论删除权限', '官方活动评论删除权限', NULL);

SET @service_module_privilege_id = (SELECT max(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10100', '0', '10140',  '普通贴删除权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10100', '0', '10141', '公告贴删除权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10100', '0', '10150', '普通评论删除权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10100', '0', '10151', '公告评论删除权限', '0', UTC_TIMESTAMP());
-- INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10600', '0', '10142', '非官方活动贴删除权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10600', '0', '10143', '官方活动贴删除权限', '0', UTC_TIMESTAMP());
-- INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10600', '0', '10152', '非官方活动评论删除权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10600', '0', '10153', '官方活动评论删除权限', '0', UTC_TIMESTAMP());

SET @menu_privilege_id = (SELECT max(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10100', '1', '10140',  '普通贴删除权限', 1,'普通贴删除权限', 1);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10100', '1', '10141','公告贴删除权限', 1, '公告贴删除权限', 2);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10100', '1', '10150','普通评论删除权限', 1, '普通评论删除权限', 3);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10100', '1', '10151','公告评论删除权限', 1, '公告评论删除权限', 4);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10600', '1', '10143','官方活动贴删除权限', 1, '官方活动贴删除权限', 5);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10600', '1', '10153','官方活动评论删除权限', 1, '官方活动评论删除权限', 6);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10140', '1001', '0', '1', UTC_TIMESTAMP());
	SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10141', '1001', '0', '1', UTC_TIMESTAMP());
	SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
-- INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10142', '1001', '0', '1', UTC_TIMESTAMP());
	SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10143', '1001', '0', '1', UTC_TIMESTAMP());
	SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10150', '1001', '0', '1', UTC_TIMESTAMP());
	SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10151', '1001', '0', '1', UTC_TIMESTAMP());
	SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
-- INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10152', '1001', '0', '1', UTC_TIMESTAMP());
	SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10153', '1001', '0', '1', UTC_TIMESTAMP());

SET @locale_string_id = (SELECT max(id) FROM `eh_locale_strings`);
insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values((@locale_string_id := @locale_string_id + 1),'forum','108','zh_CN','园区圈');
insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values((@locale_string_id := @locale_string_id + 1),'forum','109','zh_CN','社区圈');

DELETE FROM `eh_service_module_privileges` where `module_id` = 10100 AND `privilege_id` = 200;
DELETE FROM `eh_service_module_privileges` where `module_id` = 10600 AND `privilege_id` = 310;

-- 位置预订工作流设置菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
	VALUES ('40050', '位置预订', '40000', '/40000/40050', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
	VALUES ('100', '40050', '1', '10145', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
	VALUES (10145, '0', '位置预订 管理员', '位置预订 业务模块权限', NULL);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
	SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1001,0,1,now(),'EhAclRoles' FROM `eh_acl_privileges` WHERE id =10145;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
	SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1005,0,1,now(),'EhAclRoles' FROM `eh_acl_privileges` WHERE id =10145;

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
	VALUES ('40050', '位置预订', '40000', null, 'react:/working-flow/flow-list/reserver-place/40050?ownerType=RESERVER_PLACE&ownerId=0', '0', '2', '/40000/40050', 'park', '479', 40050);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10145, 40050, '位置预订', 1, 1, '位置预订  全部权限', 202);
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40050, '', 'EhNamespaces', 999983, 2);

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('position.reserver.appKey', 'd80e06ca-3766-11e5-b18f-b083fe4e159f', '', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('position.reserver.secretKey', 'g1JOZUM3BYzWpZD5Q7p3z+i/z0nj2TcokTFx2ic53FCMRIKbMhSUCi7fSu9ZklFCZ9tlj68unxur9qmOji4tNg==', '', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('position.reserver.serverUrl', 'http://biz.zuolin.com', '', '0', NULL);

-- 更新威新Link 物业报修-工作流 add by sw 20170308
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('pmtask.handler-999991', 'flow', '', '0', NULL);
DELETE from eh_web_menu_scopes where menu_id = 20160 and owner_id = 999991;
DELETE from eh_web_menu_scopes where menu_id = 20192 and owner_id = 999991;

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20158, '', 'EhNamespaces', 999991, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70000, '', 'EhNamespaces', 999991, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70100, '', 'EhNamespaces', 999991, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70200, '', 'EhNamespaces', 999991, 2);

SET @eh_launch_pad_items = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999991', '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRvd09HUTNNRGs1TURZeVpUTmhNbU5pWkdWbVpXUmhNMlU1T1RSaE4yTTFNZw', '1', '1', '56', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);
update eh_launch_pad_items set action_type = 60, action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=提交服务"}' where item_label = '物业报修' and namespace_id = 999991;



-- 活动截止报名时间错误提示，add by tt, 20170228
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10018', 'zh_CN', '报名截止时间应早于或等于活动结束时间!');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10019', 'zh_CN', '活动报名已截止!');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10020', 'zh_CN', '手机号码错误!');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10021', 'zh_CN', '该活动还没有报名信息哦!');


--
-- 短信发送次数校验配置   add by xq.tian  2017/03/09
--
SELECT max(id) FROM eh_configurations INTO @max_id;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.minDuration.seconds', '60', '注册时最小短信间隔时间', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.device.timesForAnHour', '10', '每个设备每小时发送短信最大次数', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.device.timesForADay', '20', '每个设备每天发送短信最大次数', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.phone.timesForAnHour', '3', '每个手机号每小时发送短信最大次数', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.phone.timesForADay', '5', '每个手机号每天发送短信最大次数', 0, NULL);

SELECT max(id) FROM eh_locale_strings INTO @max_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES (@max_id := @max_id + 1, 'user', '300001', 'zh_CN', '发送验证码时间不得小于60s');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES (@max_id := @max_id + 1, 'user', '300002', 'zh_CN', '验证码请求过于频繁，请1小时后重试');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES (@max_id := @max_id + 1, 'user', '300003', 'zh_CN', '验证码请求过于频繁，请明天重试');

UPDATE `eh_locale_strings` SET `text` = '签名过期，请重新获取' WHERE `scope` = 'user' AND `code` = '10000' AND `locale` = 'zh_CN';

-- 物业报修对接工作流短信 add by sw 20170314
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999991, 'flow:20100', 11, 'zh_CN', '物业报修短信', '30092');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999991, 'flow:20100', 34, 'zh_CN', '物业报修短信', '38833');

-- added by janson 20170315
UPDATE `eh_configurations` SET `value`='259200' WHERE `name`='aclink.user_key_timeout' and `namespace_id`=0 limit 1;
UPDATE `eh_configurations` SET `value`='259200' WHERE `name`='aclink.user_key_timeout' and `namespace_id`=1000000 limit 1;
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (999983, 'aclink.user_key_timeout', '259200', 'timeout in second for qr kexingkexueyuan');

-- add by sfyan 20170328 重整缴费管理菜单 配置
DELETE FROM eh_web_menu_scopes WHERE owner_id  = 999985 AND menu_id IN (20400, 20410, 20420);
DELETE FROM `eh_service_module_scopes` WHERE `namespace_id`  = 999985 AND `module_id` IN (20400);
DELETE FROM `eh_acls` WHERE `role_id` = 1001 AND `role_type` = 'EhAclRoles' AND `privilege_id` = 10095;
DELETE FROM eh_web_menu_scopes WHERE owner_id  = 999985 AND menu_id IN (SELECT id FROM `eh_web_menus` WHERE `name` = '缴费管理');
DELETE FROM eh_service_module_scopes WHERE namespace_id  = 999985 AND module_id IN (SELECT id FROM `eh_service_modules` WHERE `name` = '缴费管理');
DELETE FROM `eh_web_menus` WHERE `name` = '缴费管理';
DELETE FROM `eh_service_modules` WHERE `name` = '缴费管理';

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
VALUES (20700, '缴费管理', 20000, NULL, 'react:/property-service/payment-management', 0, 2, '/20000/20700', 'park', 252, 20700);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('20700', '缴费管理', '20000', '/20000/20700', '0', '2', '2', '0', UTC_TIMESTAMP());
SET @service_module_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes`(`id`, `module_id`,`module_name`, `namespace_id`, `apply_policy`) VALUES( @service_module_id := @service_module_id + 1,20700,'', 999985,2);
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES( @menu_scope_id := @menu_scope_id + 1,20700,'', 'EhNamespaces', 999985,2);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES((@web_menu_privilege_id := @web_menu_privilege_id + 1),'20027','20700','缴费管理','1','1','缴费管理 全部权限','710');
SET @eh_service_module_privileges = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
	VALUES ((@eh_service_module_privileges := @eh_service_module_privileges + 1), '20700', '0', '20027', '缴费管理', '0', UTC_TIMESTAMP());

	
	
