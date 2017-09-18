-- 蒙版提示信息修改 by lei.lv
UPDATE `eh_namespace_masks` SET `id`='1', `namespace_id`='999971', `item_name`='人才公寓', `image_type`='3', `tips`='快速切换至人才公寓首页', `scene_type`='park_tourist' WHERE (`id`='1');
UPDATE `eh_namespace_masks` SET `id`='2', `namespace_id`='999971', `item_name`='园区服务', `image_type`='3', `tips`='快速切换至园区首页', `scene_type`='default' WHERE (`id`='2');
UPDATE `eh_configurations` SET VALUE = 0 WHERE namespace_id = 999971 AND NAME = 'mask.key';

-- 把有管理员权限的用户，在eh_organization_members表里面标识成 manager add by sfyan 20170911
UPDATE `eh_organization_members` eom SET `member_group` = '';
UPDATE `eh_organization_members` eom SET `member_group` = IFNULL((SELECT 'manager' FROM eh_acls WHERE `owner_type` = 'EhOrganizations' AND `owner_id` = eom.organization_id AND role_type = 'EhUsers' AND role_id = eom.target_id AND privilege_id IN (10,15)  LIMIT 1), '') WHERE group_type = 'ENTERPRISE' AND target_type = 'USER' AND STATUS = 3;


-- 用户注册已存在提示 add by sfyan 20170913 
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '100019', 'zh_CN', '手机号已被注册！');



-- 张江高科的app入口配置 by wentian
-- 这是上到beta的入口配置
-- update `eh_launch_pad_items` set action_data='{"url":"http://beta.zuolin.com/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix"}'
-- where item_label = '费用查缴' and scene_type != 'pm_admin' and namespace_id='999971';
--
-- update `eh_launch_pad_items` set action_data='{"url":"http://beta.zuolin.com/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix"}'
-- where item_label = '企业账单' and scene_type != 'pm_admin' and namespace_id='999971';

-- 这是上到线网的入口
UPDATE `eh_launch_pad_items` SET action_data='{"url":"http://core.zuolin.com/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix"}'
WHERE item_label = '费用查缴' AND scene_type != 'pm_admin' AND namespace_id='999971';

UPDATE `eh_launch_pad_items` SET action_data='{"url":"http://core.zuolin.com/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix"}'
WHERE item_label = '企业账单' AND scene_type != 'pm_admin' AND namespace_id='999971';

-- 张江高科的菜单配置 by wentian

-- 删除张江高科下的旧的缴费下的菜单 by wentian
DELETE FROM eh_web_menu_scopes WHERE menu_id IN ('20410','20420') AND owner_id = '999971';

-- 删除账单管理在保集和嘉定，和张江的缴费的菜单 by wentian
DELETE FROM eh_web_menu_scopes WHERE menu_id IN (SELECT id FROM eh_web_menus WHERE path LIKE '/20700/%' AND (NAME = '账单管理' OR NAME = '账单统计')) AND owner_id IN ('999971','999973','999974');

-- 配置新的张江的菜单 by wentian
UPDATE `eh_web_menus` SET leaf_flag = '0' WHERE id = '20400';

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`,`module_id`,`level`,`condition_type`,`category`)
VALUES ('204011', '账单管理', 20400, NULL, 'react:/payment-management/bills-manage', 1, 2, '/20000/20400/204011', 'park', 990454,20400,3,NULL,'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`,`module_id`,`level`,`condition_type`,`category`)
VALUES ('204021', '账单统计', 20400, NULL, 'react:/payment-management/bills-statistics', 1, 2, '/20000/20400/204021', 'park', 999243,20400,3,NULL,'module');

SET @eh_web_menus_id_paym = '204011';
SET @eh_web_menus_id_payb = '204021';

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), @eh_web_menus_id_paym, '', 'EhNamespaces', 999971, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), @eh_web_menus_id_payb, '', 'EhNamespaces', 999971, 2);

SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, 'zjgk账单管理', 'zjgk账单管理 全部权限', NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, @eh_web_menus_id_paym, '账单管理', 1, 1, '账单管理 全部权限', 990);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, UTC_TIMESTAMP());
SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), @eh_web_menus_id_paym, '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());

SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, 'zjgk账单统计', 'zjgk账单统计 全部权限', NULL);
SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, @eh_web_menus_id_payb, '账单统计', 1, 1, '账单统计 全部权限', 999);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, UTC_TIMESTAMP());
SET @eh_service_module_privileges_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), @eh_web_menus_id_payb, '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`,`operator_uid`,`creator_uid`)
VALUES (@eh_web_menus_id_paym, '账单管理', '20400', '/20000/20400/', '0', '2', '2', '0', UTC_TIMESTAMP(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`,`operator_uid`,`creator_uid`)
VALUES (@eh_web_menus_id_payb, '账单统计', '20400', '/20000/20400/', '0', '2', '2', '0', UTC_TIMESTAMP(),1,1);

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999971, @eh_web_menus_id_paym, '账单管理', 'EhNamespaces', 999971, NULL, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999971, @eh_web_menus_id_payb, '账单统计', 'EhNamespaces', 999971, NULL, 2);

-- 张江高科的账单组设置 by wentian
TRUNCATE `eh_payment_variables`;
INSERT INTO `eh_payment_variables` VALUES ('1', '1', '1', '月单价', NULL, NULL, NULL, NULL, 'ydj');
INSERT INTO `eh_payment_variables` VALUES ('2', '1', '1', '计费面积', NULL, NULL, NULL, NULL, 'mj');
INSERT INTO `eh_payment_variables` VALUES ('3', '2', '1', '固定金额', NULL, NULL, NULL, NULL, 'gdje');

TRUNCATE `eh_payment_charging_standards_scopes`;
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('1', '1', 'community', '240111044331050388', '0', UTC_TIMESTAMP(), NULL, NULL);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('2', '2', 'community', '240111044331050388', '0', UTC_TIMESTAMP(), NULL, NULL);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('3', '1', 'community', '240111044332059779', '0', UTC_TIMESTAMP(), NULL, NULL);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('4', '2', 'community', '240111044332059779', '0', UTC_TIMESTAMP(), NULL, NULL);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('5', '1', 'community', '240111044332059780', '0', UTC_TIMESTAMP(), NULL, NULL);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('6', '2', 'community', '240111044332059780', '0', UTC_TIMESTAMP(), NULL, NULL);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('7', '1', 'community', '240111044332059781', '0', UTC_TIMESTAMP(), NULL, NULL);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('8', '2', 'community', '240111044332059781', '0', UTC_TIMESTAMP(), NULL, NULL);

TRUNCATE `eh_payment_charging_standards`;
INSERT INTO `eh_payment_charging_standards` VALUES ('1', '租金(月单价*面积)', '1', '月单价*面积', 'ydj*mj', '2', '1', '2', '0', UTC_TIMESTAMP(), NULL, NULL);
INSERT INTO `eh_payment_charging_standards` VALUES ('2', '租金(固定金额)', '1', '固定金额', 'gdje', '1', '1', '2', '0', UTC_TIMESTAMP(), NULL, NULL);

TRUNCATE `eh_payment_charging_items`;
INSERT INTO `eh_payment_charging_items` VALUES ('1', '租金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');

TRUNCATE `eh_payment_charging_item_scopes`;
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('1', '1', '999971', '240111044331050388', 'community');
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('2', '1', '999971', '240111044332059779', 'community');
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('3', '1', '999971', '240111044332059780', 'community');
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('4', '1', '999971', '240111044332059781', 'community');

TRUNCATE `eh_payment_bill_groups_rules`;
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('1', '999971', '1', '1', '1', '租金月单价*面积', '{\"ydj\":\"1000\",\"mj\":\"0\"}', 'community', '240111044331050388');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('2', '999971', '1', '1', '2', '租金固定金额', '{\"gdje\":\"10\"}', 'community', '240111044331050388');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('3', '999971', '2', '1', '1', '租金月单价*面积', '{\"ydj\":\"1000\",\"mj\":\"0\"}', 'community', '240111044332059779');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('4', '999971', '2', '1', '2', '租金固定金额', '{\"gdje\":\"10\"}', 'community', '240111044332059779');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('5', '999971', '3', '1', '1', '租金月单价*面积', '{\"ydj\":\"1000\",\"mj\":\"0\"}', 'community', '240111044332059780');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('6', '999971', '3', '1', '2', '租金固定金额', '{\"gdje\":\"10\"}', 'community', '240111044332059780');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('7', '999971', '4', '1', '1', '租金月单价*面积', '{\"ydj\":\"1000\",\"mj\":\"0\"}', 'community', '240111044332059781');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('8', '999971', '4', '1', '2', '租金固定金额', '{\"gdje\":\"10\"}', 'community', '240111044332059781');


TRUNCATE `eh_payment_bill_groups`;
INSERT INTO `eh_payment_bill_groups` VALUES ('1', '999971', '240111044331050388', 'community', '租金', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('2', '999971', '240111044332059779', 'community', '租金', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('3', '999971', '240111044332059780', 'community', '租金', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('4', '999971', '240111044332059781', 'community', '租金', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('5', '999971', '240111044331050388', 'community', '物业费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '2');
INSERT INTO `eh_payment_bill_groups` VALUES ('6', '999971', '240111044332059779', 'community', '物业费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '2');
INSERT INTO `eh_payment_bill_groups` VALUES ('7', '999971', '240111044332059780', 'community', '物业费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '2');
INSERT INTO `eh_payment_bill_groups` VALUES ('8', '999971', '240111044332059781', 'community', '物业费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '2');
INSERT INTO `eh_payment_bill_groups` VALUES ('9', '999971', '240111044331050388', 'community', '水电费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '3');
INSERT INTO `eh_payment_bill_groups` VALUES ('10', '999971', '240111044332059779', 'community', '水电费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '3');
INSERT INTO `eh_payment_bill_groups` VALUES ('11', '999971', '240111044332059780', 'community', '水电费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '3');
INSERT INTO `eh_payment_bill_groups` VALUES ('12', '999971', '240111044332059781', 'community', '水电费', '2', '5', '0', UTC_TIMESTAMP(), NULL, NULL, '3');

-- 同步神州数码的小区标识
UPDATE `eh_communities` SET namespace_community_token='D6B7C7C4-F469-4979-9624-BF6214FB0CEB' , namespace_community_type = 'shenzhou' WHERE id = '240111044331050388';
UPDATE `eh_communities` SET namespace_community_token='7866D7BC-2544-4208-A41E-94ED2A25D2A4' , namespace_community_type = 'shenzhou' WHERE id = '240111044332059779';
UPDATE `eh_communities` SET namespace_community_token='1C465757-4EAD-47F9-B967-F1A08A2997CC' , namespace_community_type = 'shenzhou' WHERE id = '240111044332059780';
UPDATE `eh_communities` SET namespace_community_token='4941ABFE-0C36-44B4-BE2D-FDC90178E233' , namespace_community_type = 'shenzhou' WHERE id = '240111044332059781';
-- wentian's script above is end here which is a gorgeous cut-off line


-- 去掉张江高科合同设置 add by xiongying20170914
DELETE FROM eh_web_menu_scopes WHERE menu_id = 21220 AND owner_id = 999971;

-- 设置动态表单的下拉选项 add by xiongying20170914
DELETE FROM eh_var_field_items WHERE field_id = 4;
DELETE FROM eh_var_field_item_scopes WHERE field_id = 4;
DELETE FROM eh_var_field_items WHERE field_id = 24;
DELETE FROM eh_var_field_item_scopes WHERE field_id = 24;
SET @item_id = (SELECT MAX(id) FROM `eh_var_field_items`); 
SET @item_scope_id = (SELECT MAX(id) FROM `eh_var_field_item_scopes`); 
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '4', '业主', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '4', @item_id, '业主', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '4', @item_id, '业主', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '4', @item_id, '业主', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '4', '办公', '2', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '4', @item_id, '办公', '2', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '4', @item_id, '办公', '2', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '4', @item_id, '办公', '2', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '4', '商业', '3', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '4', @item_id, '商业', '3', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '4', @item_id, '商业', '3', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '4', @item_id, '商业', '3', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '4', '孵化器', '4', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '4', @item_id, '孵化器', '4', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '4', @item_id, '孵化器', '4', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '4', @item_id, '孵化器', '4', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '4', '物业公司', '5', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '4', @item_id, '物业公司', '5', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '4', @item_id, '物业公司', '5', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '4', @item_id, '物业公司', '5', '2', '1', UTC_TIMESTAMP());


INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '集成电路', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '集成电路', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '集成电路', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '集成电路', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '软件', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '软件', '2', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '软件', '2', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '软件', '2', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '通信技术', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '通信技术', '3', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '通信技术', '3', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '通信技术', '3', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '生物医药', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '生物医药', '4', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '生物医药', '4', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '生物医药', '4', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '医疗器械', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '医疗器械', '5', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '医疗器械', '5', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '医疗器械', '5', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '光机电', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '光机电', '6', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '光机电', '6', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '光机电', '6', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '金融服务', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '金融服务', '7', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '金融服务', '7', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '金融服务', '7', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '新能源与环保', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '新能源与环保', '8', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '新能源与环保', '8', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '新能源与环保', '8', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '文化创意', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '文化创意', '9', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '文化创意', '9', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '文化创意', '9', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '商业-餐饮', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '商业-餐饮', '10', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '商业-餐饮', '10', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '商业-餐饮', '10', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '商业-超市', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '商业-超市', '11', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '商业-超市', '11', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '商业-超市', '11', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '商业-食堂', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '商业-食堂', '12', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '商业-食堂', '12', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '商业-食堂', '12', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '商业-其他', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '商业-其他', '13', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '商业-其他', '13', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '商业-其他', '13', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', '24', '其他', '1', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999971', 'enterprise_customer', '24', @item_id, '其他', '14', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999973', 'enterprise_customer', '24', @item_id, '其他', '14', '2', '1', UTC_TIMESTAMP());
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`) VALUES ((@item_scope_id := @item_scope_id + 1), '999974', 'enterprise_customer', '24', @item_id, '其他', '14', '2', '1', UTC_TIMESTAMP());

UPDATE eh_var_field_item_scopes SET item_display_name = '先生' WHERE namespace_id = 999971 AND module_name = 'enterprise_customer' AND item_display_name = '男';
UPDATE eh_var_field_item_scopes SET item_display_name = '女士' WHERE namespace_id = 999971 AND module_name = 'enterprise_customer' AND item_display_name = '女';


-- 张江高科合同管理和客户管理的菜单加上 add by xiongying20170914
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21200', '', 'EhNamespaces', '999971', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21210', '', 'EhNamespaces', '999971', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21100', '', 'EhNamespaces', '999971', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21110', '', 'EhNamespaces', '999971', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@menu_scope_id := @menu_scope_id + 1), '21120', '', 'EhNamespaces', '999971', '2');


-- 修改招租管理模块名称 add by xq.tian  2017/09/14
UPDATE eh_flow_cases SET module_name = '招租管理' WHERE module_id = 40100;

-- 神州数码的配置数据 add by xiongying20170914
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'shenzhou.host.url', 'http://139.129.220.146:3578', NULL, '999971', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'shenzhoushuma.app.key', 'ee4c8905-9aa4-4d45-973c-ede4cbb3cf21', NULL, '999971', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'shenzhoushuma.secret.key', '2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==', NULL, '999971', NULL);

-- fix 15413 add by xiongying20170914
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('13908', '13905', '南山区', 'NANSHANQU', 'NSQ', '/广东/深圳市/南山区', '3', '3', NULL, '0755', '2', '0', '999966');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('13905', '13874', '深圳市', 'SHENZHENSHI', 'SZS', '/广东/深圳市', '2', '2', NULL, '0755', '2', '1', '999966');
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) VALUES ('13874', '0', '广东', 'GUANGDONG', 'GD', '/广东', '1', '1', NULL, NULL, '2', '0', '999966');

-- merge from equipment-notify by xiongying20170915
-- 增加消息提醒菜单 add by xiongying20170914
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `category`) VALUES (20860, '消息提醒配置', '20800', 'react:/equipment-inspection/message-call', '0', '2', '/20000/20800/20860', 'park', '420', '20800', '3', 'module');

SET @privilege_id = (SELECT DISTINCT privilege_id FROM eh_web_menu_privileges WHERE NAME='设备巡检' );
SET @eh_web_menu_privilege_id = (SELECT MAX(id) FROM eh_web_menu_privileges);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) VALUES (@eh_web_menu_privilege_id+1, @privilege_id, '20860', '物业巡检', '1', '1', '物业巡检 管理员权限', '720');

SET @menu_scope_id = (SELECT MAX(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@menu_scope_id  + 1, '20860', '', 'EhNamespaces', '999992', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@menu_scope_id  + 2, '20860', '', 'EhNamespaces', '999975', '2');

-- 增加消息提醒模板
SET @id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'equipment.notification', '7', 'zh_CN', '任务开始前提醒', '你的任务${taskName}将在${time}开始', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'equipment.notification', '8', 'zh_CN', '任务过期前提醒', '你的任务${taskName}将在${time}结束', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@id := @id + 1), 'equipment.notification', '9', 'zh_CN', '任务过期后提醒', '你的任务${taskName}已在${time}结束', '0');



-- 打卡3.0 
-- 节假日

DELETE FROM eh_punch_holidays;

INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES('1000',NULL,NULL,NULL,'1','2017-10-02','1','2017-09-16 10:50:51',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES('1001',NULL,NULL,NULL,'1','2017-10-03','1','2017-09-16 10:50:51',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES('1002',NULL,NULL,NULL,'1','2017-10-04','1','2017-09-16 10:50:51',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES('1003',NULL,NULL,NULL,'1','2017-10-05','1','2017-09-16 10:50:51',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES('1004',NULL,NULL,NULL,'1','2017-10-06','1','2017-09-16 10:50:51',NULL);
INSERT INTO `eh_punch_holidays` (`id`, `owner_type`, `owner_id`, `workday_rule_id`, `status`, `rule_date`, `creator_uid`, `create_time`, `exchange_from_date`) VALUES('1005',NULL,NULL,NULL,'0','2017-09-30','1','2017-09-16 10:50:51','2017-10-05');


-- 打卡
SET @id =(SELECT MAX(id) FROM eh_locale_templates); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.tool.uri','1','zh_CN','tools跳转uri','zl://attendance/punchClockTool?type=${qrtype}&token=${token}',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.excel','1','zh_CN','排班excel说明','${timeRules}休息（只能按现有班次排班，否则无法识别。班次信息可以在管理后台修改）',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.message','1','zh_CN','排班规则推送模板-新增固定',
'Hi，公司为你设置了打卡规则，上班时间如下：
${timeRules}
规则即时生效，请准时打卡哟～',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.message','2','zh_CN','排班规则推送模板-新增排班',
'Hi，公司为你设置了打卡规则，上班时间如下：
${timeRules}
规则即时生效，请准时打卡哟～
具体排班请前往打卡-打卡详情页面查看',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.message','3','zh_CN','排班规则推送模板-修改固定',
'Hi，公司修改了你的打卡规则，规则如下：
${timeRules}
规则即时生效，请准时打卡哟～',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.message','4','zh_CN','排班规则推送模板-修改固定',
'Hi，公司修改了你的打卡规则，规则如下：
${timeRules}
规则即时生效，请准时打卡哟～
具体排班请前往打卡-打卡详情页面查看',0); 
INSERT INTO `eh_locale_templates` (`id`,`scope`,`code`,`locale`,`description`,`text`,`namespace_id`)VALUES ((@id:=@id+1),'punch.message','5','zh_CN','排班规则推送模板-班次',
'${timeRule}（需打卡${intervalNo}次）。',0); 



DELETE FROM eh_web_menus WHERE path LIKE '/500000/506000/%';
DELETE FROM eh_web_menus WHERE path LIKE '/50000/50600/%';
UPDATE eh_web_menus SET data_type = 'react:/attendance-management/attendance-record/1' WHERE id IN (506000,50600);

SET @id =(SELECT MAX(id) FROM eh_locale_strings); 
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.status','17','zh_CN','非工作日');
INSERT INTO `eh_locale_strings` (`id`,`scope`,`code`,`locale`,`text`)VALUES ((@id:=@id+1),'punch.message','6','zh_CN','休息时间'); 

UPDATE eh_locale_strings SET TEXT = '缺卡' WHERE scope = 'punch.status' AND  CODE = '14' ;

-- 更改工作流显示
UPDATE eh_locale_templates SET TEXT = '异常日期：${exceptionDate}
打卡时间：${punchDetail}' WHERE scope = 'approval.flow.context' AND CODE = 2 ;

UPDATE eh_locale_strings SET TEXT ='打卡时间' WHERE scope ='approval.flow' AND  CODE = 'punchDetail';

ALTER TABLE eh_uniongroup_member_details ADD UNIQUE INDEX `uniqueIndex` (group_type, group_id,contact_token) ;

-- 对历史数据进行处理
UPDATE eh_punch_day_logs SET status_list = STATUS WHERE punch_times_per_day = 2 AND status_list IS NULL	;
UPDATE eh_punch_day_logs SET status_list = CONCAT(morning_status , "/" ,afternoon_status)  WHERE punch_times_per_day = 4 AND status_list IS NULL	;

UPDATE eh_punch_day_logs SET rule_type = 1 ,time_rule_id = 1;




 
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025925','1010608','PM','刘平波考勤','0','/1010608/1025925','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010608','999992','1009352'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025926','1010608','PM','吴丽沙考勤','0','/1010608/1025926','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010608','999992','1009352'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025931','1010608','PM','杨海斌考勤','0','/1010608/1025931','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010608','999992','1009352'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025934','1010608','PM','广哥考勤','0','/1010608/1025934','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010608','999992','1009352'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025935','1010608','PM','贺海霞考勤','0','/1010608/1025935','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010608','999992','1009352'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025936','1010608','PM','彭颂文考勤','0','/1010608/1025936','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010608','999992','1009352'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025998','1000750','PM','刘平波考勤','0','/1000750/1025998','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 
 



UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025925,STATUS =2 WHERE id ='153' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025926,STATUS =2 WHERE id ='154' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025931,STATUS =2 WHERE id ='159' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025934,STATUS =2 WHERE id ='162' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025935,STATUS =2 WHERE id ='163' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025936,STATUS =2 WHERE id ='164' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025998,STATUS =2 WHERE id ='218' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027035,STATUS =2 WHERE id ='1150' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027041,STATUS =2 WHERE id ='1152' ;

 
 

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025925
WHERE  location_rule_id =244 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025926
WHERE  location_rule_id =221 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025931
WHERE  location_rule_id =227 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025934
WHERE  location_rule_id =232 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025935
WHERE  location_rule_id =233 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025936
WHERE  location_rule_id =234 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025998
WHERE  location_rule_id =301 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027035
WHERE  location_rule_id =1174 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027041
WHERE  location_rule_id =1176 ;

 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025751','178395','PM','深圳科技工业园（集团）有限公司考勤','0','/178395/1025751','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','178395','1000000','178395'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025752','178945','PM','深圳市永佳天成科技发展有限公司考勤','0','/178945/1025752','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','178945','1000000','178945'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025753','1000001','PM','深圳科技工业园（集团）有限公司考勤','0','/1000001/1025753','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000001','1000000','1000796'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025754','180041','PM','深业集团（深圳）物业管理有限公司考勤','0','/180041/1025754','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','180041','0','180041'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025755','1000750','PM','深业集团（深圳）物业管理有限公司考勤','0','/1000750/1025755','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025756','1001706','PM','左邻小站考勤','0','/1001706/1025756','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1001706','1000000','1002015'); 


INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025768','1002797','PM','深圳市嘉宏达建材有限公司考勤','0','/1002797/1025768','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1002797','0','1003199'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025784','1000750','PM','深业中心大厦考勤','0','/1000750/1025784','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025785','1000750','PM','管理部考勤','0','/1000750/1025785','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025809','1000750','PM','安管队白班考勤','0','/1000750/1025809','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025810','1000750','PM','安管队一班考勤','0','/1000750/1025810','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025811','1000750','PM','安管队二班考勤','0','/1000750/1025811','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025812','1000750','PM','安管队三班考勤','0','/1000750/1025812','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025815','1000750','PM','深业中心安管队考勤','0','/1000750/1025815','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025819','1005034','PM','深圳湾科技发展有限公司考勤','0','/1005034/1025819','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1005034','999987','1004907'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025831','1004005','PM','怡景管理处考勤','0','/1004005/1025831','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1004005','0','1004375'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025846','1004140','PM','南通创源科技园发展有限公司考勤','0','/1004140/1025846','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1004140','999986','1004537'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025857','1000750','PM','行政部考勤','0','/1000750/1025857','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025858','1000750','PM','人力资源部考勤','0','/1000750/1025858','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025859','1000750','PM','经营部考勤','0','/1000750/1025859','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025860','1000750','PM','法律事务部考勤','0','/1000750/1025860','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025861','1000750','PM','发展部考勤','0','/1000750/1025861','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025862','1000750','PM','财务部考勤','0','/1000750/1025862','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025863','1000750','PM','安保部考勤','0','/1000750/1025863','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025864','1000750','PM','电梯分公司考勤','0','/1000750/1025864','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025865','1000750','PM','绿化分公司考勤','0','/1000750/1025865','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025868','1000750','PM','机电早班考勤','0','/1000750/1025868','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025869','1000750','PM','机电中班考勤','0','/1000750/1025869','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025870','1000750','PM','机电晚班考勤','0','/1000750/1025870','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025871','1000750','PM','长白班考勤','0','/1000750/1025871','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025882','1000750','PM','深发花园考勤','0','/1000750/1025882','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025891','1009943','PM','御河硅谷（上海）建设发展有限公司考勤','0','/1009943/1025891','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1009943','999981','1009318'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025904','1007144','PM','华润置地考勤','0','/1007144/1025904','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1007144','999985','1007016'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025915','1000750','PM','长沙市委市政府考勤','0','/1000750/1025915','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025916','1000750','PM','湖南分公司考勤','0','/1000750/1025916','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025917','1000750','PM','安全执勤部考勤','0','/1000750/1025917','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025922','1000750','PM','长沙市府二办考勤','0','/1000750/1025922','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025937','1010608','PM','长沙市人大常委会考勤','0','/1010608/1025937','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010608','999992','1009352'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025939','1000750','PM','长沙市人大常委会考勤','0','/1000750/1025939','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025946','1012057','PM','35B1国海证券股份有限公司深圳铜鼓路证券营业部考勤','0','/1012057/1025946','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1012057','999985','1010421'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025947','1010608','PM','综合管理部考勤','0','/1010608/1025947','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010608','999992','1009352'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025959','1023080','PM','深圳市永佳天成科技发展有限公司考勤','0','/1023080/1025959','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1023080','1','1012469'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025962','1000750','PM','管理处考勤','0','/1000750/1025962','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025968','1000750','PM','深业大厦片区考勤','0','/1000750/1025968','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025969','1000750','PM','深业花园片区考勤','0','/1000750/1025969','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025972','1022080','PM','深圳康利置地有限公司考勤','0','/1022080/1025972','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1022080','999978','1011479'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025973','1000750','PM','机电班考勤','0','/1000750/1025973','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025974','1000750','PM','值班组考勤','0','/1000750/1025974','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025985','1023082','PM','华润网络（深圳）有限公司考勤','0','/1023082/1025985','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1023082','1','1012471'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025992','1000750','PM','长沙市公共资源交易中心考勤','0','/1000750/1025992','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1025997','1023967','PM','上海创源新城科技有限公司考勤','0','/1023967/1025997','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1023967','999974','1020903'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026006','1024227','PM','华润置地前海有限公司考勤','0','/1024227/1026006','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1024227','999985','1021205'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026183','1000750','PM','机电班考勤','0','/1000750/1026183','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026184','1023080','PM','火柴测试打卡分公司考勤','0','/1023080/1026184','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1023080','1','1012469'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026185','1023080','PM','火柴测试打卡部门考勤','0','/1023080/1026185','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1023080','1','1012469'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026196','1000750','PM','清洁班考勤','0','/1000750/1026196','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026220','1024564','PM','深圳市嘉宏达建材有限公司考勤','0','/1024564/1026220','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1024564','1','1021289'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026258','1000750','PM','安全班考勤','0','/1000750/1026258','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026259','1000750','PM','纪委岗考勤','0','/1000750/1026259','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026260','1000750','PM','车场岗考勤','0','/1000750/1026260','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026267','1000750','PM','综合管理部考勤','0','/1000750/1026267','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026270','1000750','PM','中控室考勤','0','/1000750/1026270','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026271','1000750','PM','安全2班考勤','0','/1000750/1026271','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026272','1000750','PM','安全3班考勤','0','/1000750/1026272','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026273','1000750','PM','大门班考勤','0','/1000750/1026273','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026377','1000750','PM','坪山区委区政府考勤','0','/1000750/1026377','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026378','1000750','PM','服务中心考勤','0','/1000750/1026378','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026402','1000750','PM','主管考勤','0','/1000750/1026402','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026883','1010579','PM','上海保集智能科技发展有限公司考勤','0','/1010579/1026883','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1010579','999973','1041239'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026947','1000750','PM','长沙市政协警备区考勤','0','/1000750/1026947','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026949','1000750','PM','机电班考勤','0','/1000750/1026949','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1026976','1000750','PM','机电班考勤','0','/1000750/1026976','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1027003','1000631','PM','海岸环庆大厦考勤','0','/1000631/1027003','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000631','999993','1000799'); 

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `path`, `level`, `status`, `group_type`, `create_time`, `update_time`, `directly_enterprise_id`, `namespace_id`, `group_id`) VALUES('1027006','1000750','PM','财务部考勤','0','/1000750/1027006','2','2',
'PUNCHGROUP','2017-09-13 17:42:07','2017-09-13 17:29:13','1000750','999992','1000795'); 

 
 

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025751,STATUS =2 WHERE id ='1' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025752,STATUS =2 WHERE id ='2' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025753,STATUS =2 WHERE id ='3' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025754,STATUS =2 WHERE id ='4' ;


UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025756,STATUS =2 WHERE id ='6' ;


UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025768,STATUS =2 WHERE id ='15' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025769,STATUS =2 WHERE id ='5' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025784,STATUS =2 WHERE id ='96' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025785,STATUS =2 WHERE id ='5' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025809,STATUS =2 WHERE id ='33' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025810,STATUS =2 WHERE id ='44' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025811,STATUS =2 WHERE id ='32' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025812,STATUS =2 WHERE id ='31' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025815,STATUS =2 WHERE id ='99' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025819,STATUS =2 WHERE id ='58' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025831,STATUS =2 WHERE id ='61' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025846,STATUS =2 WHERE id ='66' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025857,STATUS =2 WHERE id ='87' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025858,STATUS =2 WHERE id ='88' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025859,STATUS =2 WHERE id ='89' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025860,STATUS =2 WHERE id ='90' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025861,STATUS =2 WHERE id ='91' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025862,STATUS =2 WHERE id ='92' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025863,STATUS =2 WHERE id ='93' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025864,STATUS =2 WHERE id ='94' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025865,STATUS =2 WHERE id ='95' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025868,STATUS =2 WHERE id ='100' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025869,STATUS =2 WHERE id ='101' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025870,STATUS =2 WHERE id ='102' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025871,STATUS =2 WHERE id ='103' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025882,STATUS =2 WHERE id ='114' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025891,STATUS =2 WHERE id ='123' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025904,STATUS =2 WHERE id ='136' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025915,STATUS =2 WHERE id ='147' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025916,STATUS =2 WHERE id ='148' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025917,STATUS =2 WHERE id ='149' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025922,STATUS =2 WHERE id ='152' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025937,STATUS =2 WHERE id ='165' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025939,STATUS =2 WHERE id ='167' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025946,STATUS =2 WHERE id ='174' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025947,STATUS =2 WHERE id ='175' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025959,STATUS =2 WHERE id ='187' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025962,STATUS =2 WHERE id ='190' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025968,STATUS =2 WHERE id ='195' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025969,STATUS =2 WHERE id ='196' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025972,STATUS =2 WHERE id ='197' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025973,STATUS =2 WHERE id ='198' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025974,STATUS =2 WHERE id ='199' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025985,STATUS =2 WHERE id ='206' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025992,STATUS =2 WHERE id ='212' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025997,STATUS =2 WHERE id ='217' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026006,STATUS =2 WHERE id ='225' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026183,STATUS =2 WHERE id ='336' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026184,STATUS =2 WHERE id ='337' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026185,STATUS =2 WHERE id ='338' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026196,STATUS =2 WHERE id ='341' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026220,STATUS =2 WHERE id ='359' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026258,STATUS =2 WHERE id ='391' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026259,STATUS =2 WHERE id ='392' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026260,STATUS =2 WHERE id ='393' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026267,STATUS =2 WHERE id ='394' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026270,STATUS =2 WHERE id ='396' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026271,STATUS =2 WHERE id ='398' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026272,STATUS =2 WHERE id ='399' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026273,STATUS =2 WHERE id ='400' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026377,STATUS =2 WHERE id ='455' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026378,STATUS =2 WHERE id ='456' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026402,STATUS =2 WHERE id ='497' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026645,STATUS =2 WHERE id ='606' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026679,STATUS =2 WHERE id ='628' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026696,STATUS =2 WHERE id ='633' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026698,STATUS =2 WHERE id ='635' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026833,STATUS =2 WHERE id ='936' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026834,STATUS =2 WHERE id ='937' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026835,STATUS =2 WHERE id ='938' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026840,STATUS =2 WHERE id ='943' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026841,STATUS =2 WHERE id ='944' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026842,STATUS =2 WHERE id ='945' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026843,STATUS =2 WHERE id ='947' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026844,STATUS =2 WHERE id ='948' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026845,STATUS =2 WHERE id ='949' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026846,STATUS =2 WHERE id ='950' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026847,STATUS =2 WHERE id ='951' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026848,STATUS =2 WHERE id ='952' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026849,STATUS =2 WHERE id ='953' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026850,STATUS =2 WHERE id ='954' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026851,STATUS =2 WHERE id ='955' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026852,STATUS =2 WHERE id ='956' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026854,STATUS =2 WHERE id ='957' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026855,STATUS =2 WHERE id ='959' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026883,STATUS =2 WHERE id ='988' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026888,STATUS =2 WHERE id ='993' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026911,STATUS =2 WHERE id ='1017' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026930,STATUS =2 WHERE id ='1039' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026931,STATUS =2 WHERE id ='1040' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026936,STATUS =2 WHERE id ='1046' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026938,STATUS =2 WHERE id ='1047' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026939,STATUS =2 WHERE id ='1049' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026940,STATUS =2 WHERE id ='1050' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026941,STATUS =2 WHERE id ='1051' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026947,STATUS =2 WHERE id ='1057' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026949,STATUS =2 WHERE id ='1060' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026952,STATUS =2 WHERE id ='1063' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026953,STATUS =2 WHERE id ='1064' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026954,STATUS =2 WHERE id ='1065' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026955,STATUS =2 WHERE id ='1066' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026956,STATUS =2 WHERE id ='1067' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026957,STATUS =2 WHERE id ='1068' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026958,STATUS =2 WHERE id ='1069' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026959,STATUS =2 WHERE id ='1070' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026960,STATUS =2 WHERE id ='1071' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026962,STATUS =2 WHERE id ='1073' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026963,STATUS =2 WHERE id ='1074' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026964,STATUS =2 WHERE id ='1075' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026965,STATUS =2 WHERE id ='1076' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026966,STATUS =2 WHERE id ='1077' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026975,STATUS =2 WHERE id ='1086' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026976,STATUS =2 WHERE id ='1087' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026989,STATUS =2 WHERE id ='1100' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026990,STATUS =2 WHERE id ='1101' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026991,STATUS =2 WHERE id ='1102' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026997,STATUS =2 WHERE id ='1108' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026998,STATUS =2 WHERE id ='1109' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1026999,STATUS =2 WHERE id ='1110' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027000,STATUS =2 WHERE id ='1111' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027001,STATUS =2 WHERE id ='1112' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027002,STATUS =2 WHERE id ='1113' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027003,STATUS =2 WHERE id ='1114' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027006,STATUS =2 WHERE id ='1119' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027011,STATUS =2 WHERE id ='1125' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027033,STATUS =2 WHERE id ='1146' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027037,STATUS =2 WHERE id ='1147' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027038,STATUS =2 WHERE id ='1148' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027039,STATUS =2 WHERE id ='1149' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027040,STATUS =2 WHERE id ='1151' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027042,STATUS =2 WHERE id ='1153' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027043,STATUS =2 WHERE id ='1154' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027045,STATUS =2 WHERE id ='1157' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027046,STATUS =2 WHERE id ='1158' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027047,STATUS =2 WHERE id ='1159' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027048,STATUS =2 WHERE id ='1160' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027049,STATUS =2 WHERE id ='1161' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027050,STATUS =2 WHERE id ='1162' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027051,STATUS =2 WHERE id ='1163' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1027052,STATUS =2 WHERE id ='1164' ;

UPDATE eh_punch_rules  SET rule_type = 0,punch_organization_id = 1025755,STATUS =2 WHERE id ='5' ;


UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025751,punch_organization_id = 1025751,punch_rule_id = 1,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '178395' AND target_type = 'organization' AND target_id = '178395' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025752,punch_organization_id = 1025752,punch_rule_id = 2,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '178945' AND target_type = 'organization' AND target_id = '178945' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025753,punch_organization_id = 1025753,punch_rule_id = 3,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000001' AND target_type = 'organization' AND target_id = '1000001' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025754,punch_organization_id = 1025754,punch_rule_id = 4,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '180041' AND target_type = 'organization' AND target_id = '180041' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025755,punch_organization_id = 1025755,punch_rule_id = 5,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1000750' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025756,punch_organization_id = 1025756,punch_rule_id = 6,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1001706' AND target_type = 'organization' AND target_id = '1001706' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025761,punch_organization_id = 1025761,punch_rule_id = 5,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1001024' AND target_type = 'organization' AND target_id = '1001024' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025768,punch_organization_id = 1025768,punch_rule_id = 15,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1002797' AND target_type = 'organization' AND target_id = '1002797' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025769,punch_organization_id = 1025769,punch_rule_id = 5,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1002821' AND target_type = 'organization' AND target_id = '1002821' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025784,punch_organization_id = 1025784,punch_rule_id = 96,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1003859' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025785,punch_organization_id = 1025785,punch_rule_id = 5,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1001015' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025809,punch_organization_id = 1025809,punch_rule_id = 33,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1004034' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025810,punch_organization_id = 1025810,punch_rule_id = 44,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1004033' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025811,punch_organization_id = 1025811,punch_rule_id = 32,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1004032' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025812,punch_organization_id = 1025812,punch_rule_id = 31,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1004031' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025815,punch_organization_id = 1025815,punch_rule_id = 99,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1003876' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025819,punch_organization_id = 1025819,punch_rule_id = 58,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1005034' AND target_type = 'organization' AND target_id = '1005034' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025831,punch_organization_id = 1025831,punch_rule_id = 61,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1004005' AND target_type = 'organization' AND target_id = '1004005' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025846,punch_organization_id = 1025846,punch_rule_id = 66,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1004140' AND target_type = 'organization' AND target_id = '1004140' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025857,punch_organization_id = 1025857,punch_rule_id = 87,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1001016' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025858,punch_organization_id = 1025858,punch_rule_id = 88,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1001019' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025859,punch_organization_id = 1025859,punch_rule_id = 89,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1001017' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025860,punch_organization_id = 1025860,punch_rule_id = 90,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1001013' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025861,punch_organization_id = 1025861,punch_rule_id = 91,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1001012' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025862,punch_organization_id = 1025862,punch_rule_id = 92,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1001011' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025863,punch_organization_id = 1025863,punch_rule_id = 93,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1001010' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025864,punch_organization_id = 1025864,punch_rule_id = 94,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1007231' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025865,punch_organization_id = 1025865,punch_rule_id = 95,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1007230' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025868,punch_organization_id = 1025868,punch_rule_id = 100,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1005050' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025869,punch_organization_id = 1025869,punch_rule_id = 101,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1005051' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025870,punch_organization_id = 1025870,punch_rule_id = 102,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1005052' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025871,punch_organization_id = 1025871,punch_rule_id = 103,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1007328' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025882,punch_organization_id = 1025882,punch_rule_id = 114,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1008875' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025891,punch_organization_id = 1025891,punch_rule_id = 123,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1009943' AND target_type = 'organization' AND target_id = '1009943' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025904,punch_organization_id = 1025904,punch_rule_id = 136,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1007144' AND target_type = 'organization' AND target_id = '1007144' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025915,punch_organization_id = 1025915,punch_rule_id = 147,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011811' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025916,punch_organization_id = 1025916,punch_rule_id = 148,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1010608' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025917,punch_organization_id = 1025917,punch_rule_id = 149,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011803' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025922,punch_organization_id = 1025922,punch_rule_id = 152,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011823' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025937,punch_organization_id = 1025937,punch_rule_id = 165,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1010608' AND target_type = 'organization' AND target_id = '1011972' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025939,punch_organization_id = 1025939,punch_rule_id = 167,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011972' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025946,punch_organization_id = 1025946,punch_rule_id = 174,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1012057' AND target_type = 'organization' AND target_id = '1012057' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025947,punch_organization_id = 1025947,punch_rule_id = 175,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1010608' AND target_type = 'organization' AND target_id = '1011805' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025959,punch_organization_id = 1025959,punch_rule_id = 187,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1023080' AND target_type = 'organization' AND target_id = '1023080' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025962,punch_organization_id = 1025962,punch_rule_id = 190,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1023419' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025968,punch_organization_id = 1025968,punch_rule_id = 195,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1008873' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025969,punch_organization_id = 1025969,punch_rule_id = 196,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1007233' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025972,punch_organization_id = 1025972,punch_rule_id = 197,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1022080' AND target_type = 'organization' AND target_id = '1022080' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025973,punch_organization_id = 1025973,punch_rule_id = 198,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011817' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025974,punch_organization_id = 1025974,punch_rule_id = 199,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1023631' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025985,punch_organization_id = 1025985,punch_rule_id = 206,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1023082' AND target_type = 'organization' AND target_id = '1023082' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025992,punch_organization_id = 1025992,punch_rule_id = 212,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011894' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1025997,punch_organization_id = 1025997,punch_rule_id = 217,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1023967' AND target_type = 'organization' AND target_id = '1023967' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026006,punch_organization_id = 1026006,punch_rule_id = 225,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1024227' AND target_type = 'organization' AND target_id = '1024227' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026183,punch_organization_id = 1026183,punch_rule_id = 336,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011866' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026184,punch_organization_id = 1026184,punch_rule_id = 337,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1023080' AND target_type = 'organization' AND target_id = '1024305' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026185,punch_organization_id = 1026185,punch_rule_id = 338,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1023080' AND target_type = 'organization' AND target_id = '1024307' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026196,punch_organization_id = 1026196,punch_rule_id = 341,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011821' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026220,punch_organization_id = 1026220,punch_rule_id = 359,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1024564' AND target_type = 'organization' AND target_id = '1024564' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026258,punch_organization_id = 1026258,punch_rule_id = 391,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011813' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026259,punch_organization_id = 1026259,punch_rule_id = 392,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025025' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026260,punch_organization_id = 1026260,punch_rule_id = 393,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025023' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026267,punch_organization_id = 1026267,punch_rule_id = 394,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011805' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026270,punch_organization_id = 1026270,punch_rule_id = 396,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025021' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026271,punch_organization_id = 1026271,punch_rule_id = 398,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025019' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026272,punch_organization_id = 1026272,punch_rule_id = 399,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025017' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026273,punch_organization_id = 1026273,punch_rule_id = 400,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025015' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026377,punch_organization_id = 1026377,punch_rule_id = 455,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1024782' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026378,punch_organization_id = 1026378,punch_rule_id = 456,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025685' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026402,punch_organization_id = 1026402,punch_rule_id = 497,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025725' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026645,punch_organization_id = 1026645,punch_rule_id = 606,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034306' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026679,punch_organization_id = 1026679,punch_rule_id = 628,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034347' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026696,punch_organization_id = 1026696,punch_rule_id = 633,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034353' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026698,punch_organization_id = 1026698,punch_rule_id = 635,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034351' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026833,punch_organization_id = 1026833,punch_rule_id = 936,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034344' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026834,punch_organization_id = 1026834,punch_rule_id = 937,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034371' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026835,punch_organization_id = 1026835,punch_rule_id = 938,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034369' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026840,punch_organization_id = 1026840,punch_rule_id = 943,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034720' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026841,punch_organization_id = 1026841,punch_rule_id = 944,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034718' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026842,punch_organization_id = 1026842,punch_rule_id = 945,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034716' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026843,punch_organization_id = 1026843,punch_rule_id = 947,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034731' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026844,punch_organization_id = 1026844,punch_rule_id = 948,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034729' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026845,punch_organization_id = 1026845,punch_rule_id = 949,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034727' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026846,punch_organization_id = 1026846,punch_rule_id = 950,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034725' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026847,punch_organization_id = 1026847,punch_rule_id = 951,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034324' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026848,punch_organization_id = 1026848,punch_rule_id = 952,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034326' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026849,punch_organization_id = 1026849,punch_rule_id = 953,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034328' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026850,punch_organization_id = 1026850,punch_rule_id = 954,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034743' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026851,punch_organization_id = 1026851,punch_rule_id = 955,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034737' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026852,punch_organization_id = 1026852,punch_rule_id = 956,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034735' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026854,punch_organization_id = 1026854,punch_rule_id = 957,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034739' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026855,punch_organization_id = 1026855,punch_rule_id = 959,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034741' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026883,punch_organization_id = 1026883,punch_rule_id = 988,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1010579' AND target_type = 'organization' AND target_id = '1010579' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026888,punch_organization_id = 1026888,punch_rule_id = 993,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034820' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026911,punch_organization_id = 1026911,punch_rule_id = 1017,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034836' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026930,punch_organization_id = 1026930,punch_rule_id = 1039,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1024227' AND target_type = 'organization' AND target_id = '1035819' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026931,punch_organization_id = 1026931,punch_rule_id = 1040,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1034681' AND target_type = 'organization' AND target_id = '1034681' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026936,punch_organization_id = 1026936,punch_rule_id = 1046,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035745' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026938,punch_organization_id = 1026938,punch_rule_id = 1047,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035747' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026939,punch_organization_id = 1026939,punch_rule_id = 1049,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035753' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026940,punch_organization_id = 1026940,punch_rule_id = 1050,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035751' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026941,punch_organization_id = 1026941,punch_rule_id = 1051,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035749' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026947,punch_organization_id = 1026947,punch_rule_id = 1057,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1025206' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026949,punch_organization_id = 1026949,punch_rule_id = 1060,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011978' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026952,punch_organization_id = 1026952,punch_rule_id = 1063,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035914' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026953,punch_organization_id = 1026953,punch_rule_id = 1064,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035916' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026954,punch_organization_id = 1026954,punch_rule_id = 1065,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035918' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026955,punch_organization_id = 1026955,punch_rule_id = 1066,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035920' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026956,punch_organization_id = 1026956,punch_rule_id = 1067,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035946' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026957,punch_organization_id = 1026957,punch_rule_id = 1068,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035955' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026958,punch_organization_id = 1026958,punch_rule_id = 1069,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035926' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026959,punch_organization_id = 1026959,punch_rule_id = 1070,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035924' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026960,punch_organization_id = 1026960,punch_rule_id = 1071,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035928' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026962,punch_organization_id = 1026962,punch_rule_id = 1073,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035959' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026963,punch_organization_id = 1026963,punch_rule_id = 1074,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035961' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026964,punch_organization_id = 1026964,punch_rule_id = 1075,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035963' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026965,punch_organization_id = 1026965,punch_rule_id = 1076,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035965' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026966,punch_organization_id = 1026966,punch_rule_id = 1077,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035967' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026975,punch_organization_id = 1026975,punch_rule_id = 1086,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1034365' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026976,punch_organization_id = 1026976,punch_rule_id = 1087,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011898' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026989,punch_organization_id = 1026989,punch_rule_id = 1100,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035998' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026990,punch_organization_id = 1026990,punch_rule_id = 1101,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1035996' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026991,punch_organization_id = 1026991,punch_rule_id = 1102,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036000' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026997,punch_organization_id = 1026997,punch_rule_id = 1108,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036003' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026998,punch_organization_id = 1026998,punch_rule_id = 1109,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036017' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1026999,punch_organization_id = 1026999,punch_rule_id = 1110,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036015' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027000,punch_organization_id = 1027000,punch_rule_id = 1111,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036007' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027001,punch_organization_id = 1027001,punch_rule_id = 1112,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036005' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027002,punch_organization_id = 1027002,punch_rule_id = 1113,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036019' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027003,punch_organization_id = 1027003,punch_rule_id = 1114,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000631' AND target_type = 'organization' AND target_id = '1012084' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027006,punch_organization_id = 1027006,punch_rule_id = 1119,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1011807' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027011,punch_organization_id = 1027011,punch_rule_id = 1125,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036032' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027033,punch_organization_id = 1027033,punch_rule_id = 1146,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1036184' AND target_type = 'organization' AND target_id = '1036184' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027037,punch_organization_id = 1027037,punch_rule_id = 1147,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036254' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027038,punch_organization_id = 1027038,punch_rule_id = 1148,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1036184' AND target_type = 'organization' AND target_id = '1036193' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027039,punch_organization_id = 1027039,punch_rule_id = 1149,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036239' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027040,punch_organization_id = 1027040,punch_rule_id = 1151,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036237' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027042,punch_organization_id = 1027042,punch_rule_id = 1153,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1036184' AND target_type = 'organization' AND target_id = '1036217' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027043,punch_organization_id = 1027043,punch_rule_id = 1154,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1036184' AND target_type = 'organization' AND target_id = '1036195' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027045,punch_organization_id = 1027045,punch_rule_id = 1157,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036293' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027046,punch_organization_id = 1027046,punch_rule_id = 1158,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036291' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027047,punch_organization_id = 1027047,punch_rule_id = 1159,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036289' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027048,punch_organization_id = 1027048,punch_rule_id = 1160,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036287' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027049,punch_organization_id = 1027049,punch_rule_id = 1161,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036285' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027050,punch_organization_id = 1027050,punch_rule_id = 1162,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036283' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027051,punch_organization_id = 1027051,punch_rule_id = 1163,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036295' ;

UPDATE eh_punch_time_rules  SET begin_punch_time = 7200000,end_punch_time =  7200000, rule_type = 0 ,owner_id =  1027052,punch_organization_id = 1027052,punch_rule_id = 1164,STATUS =2,hommization_type = 0  
WHERE owner_type = 'organization' AND owner_id = '1000750' AND target_type = 'organization' AND target_id = '1036281' ;

 

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025751
WHERE  location_rule_id =1 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025752
WHERE  location_rule_id =226 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025753
WHERE  location_rule_id =181 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025754
WHERE  location_rule_id =4 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025755
WHERE  location_rule_id =228 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025756
WHERE  location_rule_id =229 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025761
WHERE  location_rule_id =228 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025768
WHERE  location_rule_id =16 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025769
WHERE  location_rule_id =228 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025784
WHERE  location_rule_id =184 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025785
WHERE  location_rule_id =228 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025809
WHERE  location_rule_id =108 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025810
WHERE  location_rule_id =196 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025811
WHERE  location_rule_id =193 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025812
WHERE  location_rule_id =197 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025815
WHERE  location_rule_id =112 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025819
WHERE  location_rule_id =49 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025831
WHERE  location_rule_id =52 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025846
WHERE  location_rule_id =57 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025857
WHERE  location_rule_id =98 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025858
WHERE  location_rule_id =97 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025859
WHERE  location_rule_id =96 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025860
WHERE  location_rule_id =101 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025861
WHERE  location_rule_id =102 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025862
WHERE  location_rule_id =103 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025863
WHERE  location_rule_id =104 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025864
WHERE  location_rule_id =99 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025865
WHERE  location_rule_id =100 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025868
WHERE  location_rule_id =118 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025869
WHERE  location_rule_id =119 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025870
WHERE  location_rule_id =120 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025871
WHERE  location_rule_id =121 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025882
WHERE  location_rule_id =149 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025891
WHERE  location_rule_id =158 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025904
WHERE  location_rule_id =176 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025915
WHERE  location_rule_id =217 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025916
WHERE  location_rule_id =240 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025917
WHERE  location_rule_id =210 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025922
WHERE  location_rule_id =216 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025937
WHERE  location_rule_id =236 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025939
WHERE  location_rule_id =243 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025946
WHERE  location_rule_id =251 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025947
WHERE  location_rule_id =253 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025959
WHERE  location_rule_id =270 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025962
WHERE  location_rule_id =273 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025968
WHERE  location_rule_id =278 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025969
WHERE  location_rule_id =279 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025972
WHERE  location_rule_id =280 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025973
WHERE  location_rule_id =281 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025974
WHERE  location_rule_id =282 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025985
WHERE  location_rule_id =289 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025992
WHERE  location_rule_id =295 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1025997
WHERE  location_rule_id =300 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026006
WHERE  location_rule_id =308 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026183
WHERE  location_rule_id =1162 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026184
WHERE  location_rule_id =419 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026185
WHERE  location_rule_id =420 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026196
WHERE  location_rule_id =423 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026220
WHERE  location_rule_id =441 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026258
WHERE  location_rule_id =473 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026259
WHERE  location_rule_id =475 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026260
WHERE  location_rule_id =476 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026267
WHERE  location_rule_id =474 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026270
WHERE  location_rule_id =478 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026271
WHERE  location_rule_id =480 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026272
WHERE  location_rule_id =481 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026273
WHERE  location_rule_id =482 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026377
WHERE  location_rule_id =537 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026378
WHERE  location_rule_id =538 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026402
WHERE  location_rule_id =578 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026645
WHERE  location_rule_id =685 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026679
WHERE  location_rule_id =707 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026696
WHERE  location_rule_id =712 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026698
WHERE  location_rule_id =714 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026833
WHERE  location_rule_id =966 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026834
WHERE  location_rule_id =967 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026835
WHERE  location_rule_id =968 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026840
WHERE  location_rule_id =972 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026841
WHERE  location_rule_id =973 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026842
WHERE  location_rule_id =974 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026843
WHERE  location_rule_id =975 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026844
WHERE  location_rule_id =976 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026845
WHERE  location_rule_id =977 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026846
WHERE  location_rule_id =978 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026847
WHERE  location_rule_id =979 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026848
WHERE  location_rule_id =980 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026849
WHERE  location_rule_id =981 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026850
WHERE  location_rule_id =982 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026851
WHERE  location_rule_id =983 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026852
WHERE  location_rule_id =984 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026854
WHERE  location_rule_id =985 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026855
WHERE  location_rule_id =987 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026883
WHERE  location_rule_id =1019 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026888
WHERE  location_rule_id =1030 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026911
WHERE  location_rule_id =1046 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026930
WHERE  location_rule_id =1068 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026931
WHERE  location_rule_id =1069 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026936
WHERE  location_rule_id =1074 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026938
WHERE  location_rule_id =1075 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026939
WHERE  location_rule_id =1077 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026940
WHERE  location_rule_id =1078 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026941
WHERE  location_rule_id =1087 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026947
WHERE  location_rule_id =1084 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026949
WHERE  location_rule_id =1085 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026952
WHERE  location_rule_id =1089 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026953
WHERE  location_rule_id =1090 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026954
WHERE  location_rule_id =1091 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026955
WHERE  location_rule_id =1092 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026956
WHERE  location_rule_id =1093 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026957
WHERE  location_rule_id =1094 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026958
WHERE  location_rule_id =1095 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026959
WHERE  location_rule_id =1096 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026960
WHERE  location_rule_id =1097 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026962
WHERE  location_rule_id =1099 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026963
WHERE  location_rule_id =1100 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026964
WHERE  location_rule_id =1101 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026965
WHERE  location_rule_id =1102 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026966
WHERE  location_rule_id =1103 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026975
WHERE  location_rule_id =1114 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026976
WHERE  location_rule_id =1115 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026989
WHERE  location_rule_id =1123 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026990
WHERE  location_rule_id =1124 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026991
WHERE  location_rule_id =1125 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026997
WHERE  location_rule_id =1131 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026998
WHERE  location_rule_id =1132 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1026999
WHERE  location_rule_id =1133 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027000
WHERE  location_rule_id =1134 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027001
WHERE  location_rule_id =1135 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027002
WHERE  location_rule_id =1136 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027003
WHERE  location_rule_id =1137 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027006
WHERE  location_rule_id =1141 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027011
WHERE  location_rule_id =1146 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027033
WHERE  location_rule_id =1170 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027037
WHERE  location_rule_id =1171 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027038
WHERE  location_rule_id =1172 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027039
WHERE  location_rule_id =1173 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027040
WHERE  location_rule_id =1175 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027042
WHERE  location_rule_id =1177 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027043
WHERE  location_rule_id =1178 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027045
WHERE  location_rule_id =1181 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027046
WHERE  location_rule_id =1182 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027047
WHERE  location_rule_id =1183 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027048
WHERE  location_rule_id =1184 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027049
WHERE  location_rule_id =1185 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027050
WHERE  location_rule_id =1186 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027051
WHERE  location_rule_id =1187 ;

UPDATE eh_punch_geopoints SET  owner_type = 'organization',  owner_id = 1027052
WHERE  location_rule_id =1188 ;

 

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1025752
WHERE  wifi_rule_id =142 ;

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1025753
WHERE  wifi_rule_id =292 ;

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1025846
WHERE  wifi_rule_id =40 ;

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1025891
WHERE  wifi_rule_id =293 ;

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1025959
WHERE  wifi_rule_id =141 ;

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1025985
WHERE  wifi_rule_id =153 ;

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1026883
WHERE  wifi_rule_id =329 ;

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1026931
WHERE  wifi_rule_id =337 ;

UPDATE eh_punch_wifis SET  owner_type = 'organization',  owner_id = 1027003
WHERE  wifi_rule_id =350 ;
 
-- add by xiongying20170915
SET @config_id = (SELECT MAX(id) FROM eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@config_id :=@config_id+1), 'contractService', '999971', NULL, '999971', NULL);
 
-- 修复detail的数据 by lei.lv
UPDATE eh_organization_member_details md
INNER JOIN (
	SELECT
		m.namespace_id,
		m.detail_id
	FROM
		eh_organization_members m
	INNER JOIN eh_organization_member_details d ON d.id = m.detail_id
	AND m.`status` = '3'
	AND m.namespace_id != '' AND m.organization_id = d.organization_id
) AS t1 ON t1.detail_id = md.id
SET md.namespace_id = t1.namespace_id;

UPDATE eh_organization_member_details set namespace_id = 1 where contact_token in(13980969342,18588466508,13916541564,15221040896);