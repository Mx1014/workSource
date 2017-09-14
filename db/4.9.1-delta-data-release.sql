-- 蒙版提示信息修改 by lei.lv
UPDATE `eh_namespace_masks` SET `id`='1', `namespace_id`='999971', `item_name`='人才公寓', `image_type`='3', `tips`='快速切换至人才公寓首页', `scene_type`='park_tourist' WHERE (`id`='1');
UPDATE `eh_namespace_masks` SET `id`='2', `namespace_id`='999971', `item_name`='园区服务', `image_type`='3', `tips`='快速切换至园区首页', `scene_type`='default' WHERE (`id`='2');


-- 把有管理员权限的用户，在eh_organization_members表里面标识成 manager add by sfyan 20170911
update `eh_organization_members` eom set `member_group` = '';
update `eh_organization_members` eom set `member_group` = ifnull((select 'manager' from eh_acls where `owner_type` = 'EhOrganizations' and `owner_id` = eom.organization_id and role_type = 'EhUsers' and role_id = eom.target_id and privilege_id in (10,15)  limit 1), '') where group_type = 'ENTERPRISE' and target_type = 'USER' and status = 3;


-- 用户注册已存在提示 add by sfyan 20170913 
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'user', '100019', 'zh_CN', '手机号已被注册！');



-- 张江高科的app入口配置 by wentian
update `eh_launch_pad_items` set action_data='{"url":"http://zhangjiang-beta.zuolin.com/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix"}'
where item_label = '费用查缴' and scene_type != 'pm_admin' and namespace_id='999971';

update `eh_launch_pad_items` set action_data='{"url":"http://zhangjiang-beta.zuolin.com/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix"}'
where item_label = '企业账单' and scene_type != 'pm_admin' and namespace_id='999971';

-- 张江高科的菜单配置 by wentian

-- 删除张江高科下的旧的缴费下的菜单 by wentian
delete from eh_web_menu_scopes where menu_id in ('20410','20420') and owner_id = '999971';

-- 删除账单管理在保集和嘉定，和张江的缴费的菜单 by wentian
delete from eh_web_menu_scopes where menu_id in (select id from eh_web_menus where path like '/20700/%' and (name = '账单管理' or name = '账单统计'));

-- 配置新的张江的菜单 by wentian
UPDATE `eh_web_menus` set leaf_flag = '0' where id = '20400';

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`,`module_id`,`level`,`condition_type`,`category`)
VALUES ('204011', '账单管理', 20400, NULL, 'react:/payment-management/bills-manage', 1, 2, '/20000/20400/204011', 'park', 990454,20400,3,null,'module');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`,`module_id`,`level`,`condition_type`,`category`)
VALUES ('204021', '账单统计', 20400, NULL, 'react:/payment-management/bills-statistics', 1, 2, '/20000/20400/204021', 'park', 999243,20400,3,null,'module');

SET @eh_web_menus_id_paym = '204011';
SET @eh_web_menus_id_payb = '204021';

SET @menu_scope_id = (SELECT MAX FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), @eh_web_menus_id_paym, '', 'EhNamespaces', 999971, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), @eh_web_menus_id_payb, '', 'EhNamespaces', 999971, 2);

SET @privilege_id = (SELECT MAX FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, 'zjgk账单管理', 'zjgk账单管理 全部权限', NULL);
SET @web_menu_privilege_id = (SELECT MAX FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, @eh_web_menus_id_paym, '账单管理', 1, 1, '账单管理 全部权限', 990);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW);
SET @eh_service_module_privileges_id = (SELECT MAX FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), @eh_web_menus_id_paym, '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());

SET @privilege_id = (SELECT MAX FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES ((@privilege_id := @privilege_id + 1), 0, 'zjgk账单统计', 'zjgk账单统计 全部权限', NULL);
SET @web_menu_privilege_id = (SELECT MAX FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @privilege_id, @eh_web_menus_id_payb, '账单统计', 1, 1, '账单统计 全部权限', 999);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
SET @acl_id = (SELECT MAX from `eh_acls`);
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1001, 'EhAclRoles', 0, 1, NOW);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @privilege_id, 1005, 'EhAclRoles', 0, 1, NOW);
SET @eh_service_module_privileges_id = (SELECT MAX FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES ((@eh_service_module_privileges_id := @eh_service_module_privileges_id + 1), @eh_web_menus_id_payb, '1', @privilege_id, NULL, '0', UTC_TIMESTAMP());

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`,`operator_uid`,`creator_uid`)
VALUES (@eh_web_menus_id_paym, '账单管理', '20400', '/20000/20400/', '0', '2', '2', '0', UTC_TIMESTAMP(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`,`operator_uid`,`creator_uid`)
VALUES (@eh_web_menus_id_payb, '账单统计', '20400', '/20000/20400/', '0', '2', '2', '0', UTC_TIMESTAMP(),1,1);

SET @eh_service_module_scopes_id = (SELECT MAX FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999971, @eh_web_menus_id_paym, '账单管理', 'EhNamespaces', 999971, NULL, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999971, @eh_web_menus_id_payb, '账单统计', 'EhNamespaces', 999971, NULL, 2);

-- 张江高科的账单组设置 by wentian
truncate `eh_payment_variables`;
INSERT INTO `eh_payment_variables` VALUES ('1', '1', '1', '月单价', null, null, null, null, 'ydj');
INSERT INTO `eh_payment_variables` VALUES ('2', '1', '1', '计费面积', null, null, null, null, 'mj');
INSERT INTO `eh_payment_variables` VALUES ('3', '2', '1', '固定金额', null, null, null, null, 'gdje');

truncate `eh_payment_charging_standards_scopes`;
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('1', '1', 'community', '240111044331050388', '0', UTC_TIMESTAMP(), null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('2', '2', 'community', '240111044331050388', '0', UTC_TIMESTAMP(), null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('3', '1', 'community', '240111044332059779', '0', UTC_TIMESTAMP(), null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('4', '2', 'community', '240111044332059779', '0', UTC_TIMESTAMP(), null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('5', '1', 'community', '240111044332059780', '0', UTC_TIMESTAMP(), null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('6', '2', 'community', '240111044332059780', '0', UTC_TIMESTAMP(), null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('7', '1', 'community', '240111044332059781', '0', UTC_TIMESTAMP(), null, null);
INSERT INTO `eh_payment_charging_standards_scopes` VALUES ('8', '2', 'community', '240111044332059781', '0', UTC_TIMESTAMP(), null, null);

truncate `eh_payment_charging_standards`;
INSERT INTO `eh_payment_charging_standards` VALUES ('1', '租金(月单价*面积)', '1', '月单价*面积', 'ydj*mj', '2', '1', '2', '0', UTC_TIMESTAMP(), null, null);
INSERT INTO `eh_payment_charging_standards` VALUES ('2', '租金(固定金额)', '1', '固定金额', 'gdje', '1', '1', '2', '0', UTC_TIMESTAMP(), null, null);

truncate `eh_payment_charging_items`;
INSERT INTO `eh_payment_charging_items` VALUES ('1', '租金', '0', UTC_TIMESTAMP(), null, null, '1');

truncate `eh_payment_charging_item_scopes`;
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('1', '1', '999971', '240111044331050388', 'community');
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('2', '1', '999971', '240111044332059779', 'community');
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('3', '1', '999971', '240111044332059780', 'community');
INSERT INTO `eh_payment_charging_item_scopes` VALUES ('4', '1', '999971', '240111044332059781', 'community');

truncate `eh_payment_bill_groups_rules`;
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('1', '999971', '1', '1', '1', '租金月单价*面积', '{\"ydj\":\"1000\",\"mj\":\"0\"}', 'community', '240111044331050388');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('2', '999971', '1', '1', '2', '租金固定金额', '{\"gdje\":\"10\"}', 'community', '240111044331050388');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('3', '999971', '2', '1', '1', '租金月单价*面积', '{\"ydj\":\"1000\",\"mj\":\"0\"}', 'community', '240111044332059779');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('4', '999971', '2', '1', '2', '租金固定金额', '{\"gdje\":\"10\"}', 'community', '240111044332059779');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('5', '999971', '3', '1', '1', '租金月单价*面积', '{\"ydj\":\"1000\",\"mj\":\"0\"}', 'community', '240111044332059780');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('6', '999971', '3', '1', '2', '租金固定金额', '{\"gdje\":\"10\"}', 'community', '240111044332059780');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('7', '999971', '4', '1', '1', '租金月单价*面积', '{\"ydj\":\"1000\",\"mj\":\"0\"}', 'community', '240111044332059781');
INSERT INTO `eh_payment_bill_groups_rules` VALUES ('8', '999971', '4', '1', '2', '租金固定金额', '{\"gdje\":\"10\"}', 'community', '240111044332059781');


truncate `eh_payment_bill_groups`;
INSERT INTO `eh_payment_bill_groups` VALUES ('1', '999971', '240111044331050388', 'community', '租金', '2', '5', '0', UTC_TIMESTAMP(), null, null, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('2', '999971', '240111044332059779', 'community', '租金', '2', '5', '0', UTC_TIMESTAMP(), null, null, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('3', '999971', '240111044332059780', 'community', '租金', '2', '5', '0', UTC_TIMESTAMP(), null, null, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('4', '999971', '240111044332059781', 'community', '租金', '2', '5', '0', UTC_TIMESTAMP(), null, null, '1');
INSERT INTO `eh_payment_bill_groups` VALUES ('5', '999971', '240111044331050388', 'community', '物业费', '2', '5', '0', UTC_TIMESTAMP(), null, null, '2');
INSERT INTO `eh_payment_bill_groups` VALUES ('6', '999971', '240111044332059779', 'community', '物业费', '2', '5', '0', UTC_TIMESTAMP(), null, null, '2');
INSERT INTO `eh_payment_bill_groups` VALUES ('7', '999971', '240111044332059780', 'community', '物业费', '2', '5', '0', UTC_TIMESTAMP(), null, null, '2');
INSERT INTO `eh_payment_bill_groups` VALUES ('8', '999971', '240111044332059781', 'community', '物业费', '2', '5', '0', UTC_TIMESTAMP(), null, null, '2');
INSERT INTO `eh_payment_bill_groups` VALUES ('9', '999971', '240111044331050388', 'community', '水电费', '2', '5', '0', UTC_TIMESTAMP(), null, null, '3');
INSERT INTO `eh_payment_bill_groups` VALUES ('10', '999971', '240111044332059779', 'community', '水电费', '2', '5', '0', UTC_TIMESTAMP(), null, null, '3');
INSERT INTO `eh_payment_bill_groups` VALUES ('11', '999971', '240111044332059780', 'community', '水电费', '2', '5', '0', UTC_TIMESTAMP(), null, null, '3');
INSERT INTO `eh_payment_bill_groups` VALUES ('12', '999971', '240111044332059781', 'community', '水电费', '2', '5', '0', UTC_TIMESTAMP(), null, null, '3');

-- wentian's script above is end here which is a gorgeous cut-off line



