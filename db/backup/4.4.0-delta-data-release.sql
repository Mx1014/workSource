-- 发公告权限 add by sfyan 20170329
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10155, '0', '发公告权限', '发公告权限', NULL);
SET @service_module_privilege_id = (SELECT max(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10100', '0', '10155',  '发公告权限', '0', UTC_TIMESTAMP());

SET @menu_privilege_id = (SELECT max(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `menu_id`, `show_flag`, `privilege_id`, `name`, `status`, `discription`, `sort_num`) VALUES ((@menu_privilege_id := @menu_privilege_id + 1), '10100', '1', '10155',  '发公告权限', 1,'发公告权限', 1);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', NULL, '1', '10155', '1001', '0', '1', UTC_TIMESTAMP());


-- 能耗管理 add by xiongying 20170329
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('energy', '10015', 'zh_CN', '单价方案已被引用，不可删除');

-- 更新用户管理菜单 add by sw 20170330
update eh_web_menus set data_type = 'user--user_identify' where id = 35000;
update eh_web_menus set data_type = 'user--user_management' where id = 34000;

-- 园区入驻 add by sw 20170331
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`) 
	VALUES ('1', '1000000', '0', '0', '0', '1', '1');
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`) 
	VALUES ('2', '999983', '0', '0', '0', '1', '1');
	
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('expansion.applyType', '5', 'zh_CN', '该信息已存在！');
delete from eh_locale_templates where `scope` = 'expansion';
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('expansion', '2', 'zh_CN', '园区入驻工作流详情内容', '[{\"key\":\"预约  楼栋\",\"value\":\"${applyBuilding}\",\"entityType\":\"list\"},{\"key\":\"姓名\",\"value\":\"${applyUserName}\",\"entityType\":\"list\"},{\"key\":\"手机号\",\"value\":\"${contactPhone}\",\"entityType\":\"list\"},{\"key\":\"公司名称\",\"value\":\"${enterpriseName}\",\"entityType\":\"list\"},{\"key\":\"申请类型\",\"value\":\"${applyType}\",\"entityType\":\"list\"},{\"key\":\"备注\",\"value\":\"${description}\",\"entityType\":\"multi_line\"}]', '0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('expansion', '1', 'zh_CN', '园区入驻工作流摘要内容', '企业: ${enterpriseName}\n电话: ${contactPhone}', '0');
UPDATE eh_web_menus set name = '入驻申请' where id = 40120;

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('apply.entry.detail.url', '/park-entry/dist/index.html#/rent_detail/%s', '', '0', NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('apply.entry.building.detail.url', '/park-entry/dist/index.html#/building_detail/%s', '', '0', NULL);

-- 更新活动选项中的图标 update avatal.all from avatar.organization    add by yanjun
UPDATE eh_configurations a
SET a.value = (SELECT
                 b.value
               FROM (SELECT
                       c.value
                     FROM eh_configurations c
                     WHERE c.name = 'post.menu.avatar.organization'
                     LIMIT 1) AS b)
WHERE a.NAME = 'post.menu.avatar.all';


-- 招租管理 add by sw 20170401
update eh_lease_promotions set issuer_type = 'ORGANIZATION';

-- add by yanjun 2017-03-31
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('shake.open.door', '0', 'use shake open door function  0:not, 1:yes', '999983', NULL);

-- add by sw 20170401
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('app.route.browser.outer.uri', 'zl://browser/i/%s', '', '0', NULL);
	
update eh_configurations set `value` = '/park-entry/dist/index.html#/rent_detail/%s' WHERE `name` = 'apply.entry.detail.url';
update eh_configurations set `value` = '/park-entry/dist/index.html#/building_detail/%s' WHERE `name` = 'apply.entry.building.detail.url';

-- 华润招租管理 add by sw 20170405
UPDATE eh_launch_pad_items SET action_type = 28, action_data = '' where item_label = '看房' and namespace_id = 999985;
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`) 
	VALUES ('3', '999985', '1', '1', '1', '1', '0');
SET @eh_web_menu_scopes = (SELECT max(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@eh_web_menu_scopes := @eh_web_menu_scopes + 1), '40100', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@eh_web_menu_scopes := @eh_web_menu_scopes + 1), '40110', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@eh_web_menu_scopes := @eh_web_menu_scopes + 1), '40120', '', 'EhNamespaces', '999985', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@eh_web_menu_scopes := @eh_web_menu_scopes + 1), '40130', '', 'EhNamespaces', '999985', '2');	

-- 全志添加 文件管理菜单 add by sw 20170405
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`)
VALUES (41500, '文件管理', 40000, NULL, 'react:/file-management/file-upload/41500', 0, 2, '/40000/41500', 'park', 390);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10135, 0, '文件管理', '文件管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10135, 41500, '文件管理', 1, 1, '文件管理  全部权限', 202);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10135, 1001, 'EhAclRoles', 0, 1, NOW());

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 41500, '', 'EhNamespaces', 999980, 2);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`)
VALUES (41500, '文件管理', 40000, '/40000/41500', 0, 2, 2, 0, UTC_TIMESTAMP());

SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), 41500, 1, 10135, NULL, '0', UTC_TIMESTAMP());

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999980, 41500, '文件管理', 'EhNamespaces', 999980, NULL, 2);

	
update eh_configurations set `value` = '/park-entry/dist/index.html#/rent_detail/%s' WHERE `name` = 'apply.entry.detail.url';
update eh_configurations set `value` = '/park-entry/dist/index.html#/building_detail/%s' WHERE `name` = 'apply.entry.building.detail.url';

-- 视频会议加入会议协议格式 by xiongying20170405
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES ('videoconf.notification', '4', 'zh_CN', '加入会议协议格式', 'cfcloud://www.confcloud.cn/join?confno=${meetingNo}', '0');

