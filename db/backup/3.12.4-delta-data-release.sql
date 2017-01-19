INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('155', 'community', '240111044331055940', '0', '办事指南', '办事指南', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999983', '');

SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);   
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055940', '办事指南', '办事指南', '155', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

    
update eh_launch_pad_items set action_type = 33 where id in(112845, 112875) and namespace_id = 999983;
update eh_launch_pad_items set action_data = '{"type":155,"parentId":155,"displayType": "list"}' where id in(112845, 112875) and namespace_id = 999983;


INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`) VALUES (1, 999983, '物业报修', 'zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修');

--
-- 科兴物业缴费2.0  add by xq.tian  2016/12/28
--
SELECT max(id) FROM `eh_configurations` INTO @max_id;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@max_id := @max_id + 1), 'kexing.pmbill.api.host', 'http://120.24.88.192:15902', 'kexing pm bill api host', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@max_id := @max_id + 1), 'kexing.pmbill.api.billlist', '/tss/rest/chargeRecordInfo/billListForZhenzhong', 'kexin pm bill api', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@max_id := @max_id + 1), 'kexing.pmbill.api.billcount', '/tss/rest/chargeRecordInfo/billCountForZhenzhong', 'kexing pm bill api', '999983', NULL);

--
-- 服务联盟详情页面配置   add by xq.tian  2016/12/28
--
UPDATE `eh_configurations` SET `value` = '/service-alliance/index.html#/service_detail/%s/%s?_k=%s' WHERE `name` = 'serviceAlliance.serviceDetail.url';

SELECT max(id) FROM `eh_locale_strings` INTO @max_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@max_id := @max_id + 1), 'organization', '500004', 'zh_CN', '公司不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@max_id := @max_id + 1), 'pmkexing', '0', 'zh_CN', '待缴');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@max_id := @max_id + 1), 'pmkexing', '1', 'zh_CN', '已缴');

--
-- 更新费用查询的icon
--
UPDATE `eh_launch_pad_items` SET `action_data` = '{"url":"http://core.zuolin.com/property-bill/index.html?hideNavigationBar=1#verify_account#sign_suffix"}' WHERE `namespace_id` = '999983' AND `item_name` = '费用查询';

-- add by xionging 2017.1.4
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`) VALUES (2, 999983, '月卡充值', 'zl://parking/query?displayName=停车');


-- merge project by sfyan 20170105 
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) values('30550','子项目管理','30000',NULL,'react:/child-project/project-list/30550','0','2','/30000/30550','park',306,30550);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 30550, '', 'EhNamespaces', 1000000, 2);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10091, 0, '子项目管理', '子项目 管理员权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10091, 30550, '子项目管理', 1, 1, '子项目 管理员权限', 306);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10091, 1001,'EhAclRoles', 0, 1, NOW());

DELETE FROM `eh_service_module_scopes` WHERE `module_id` in (10000, 20000, 40000);
SET @service_module_scope_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
SELECT (@service_module_scope_id := @service_module_scope_id + 1), id, 10000, '', NULL, NULL, NULL, '2' FROM `eh_namespaces`; 

INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
SELECT (@service_module_scope_id := @service_module_scope_id + 1), id, 20000, '', NULL, NULL, NULL, '2' FROM `eh_namespaces`; 

INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) 
SELECT (@service_module_scope_id := @service_module_scope_id + 1), id, 40000, '', NULL, NULL, NULL, '2' FROM `eh_namespaces`; 

-- 科兴配置发件邮箱 by xiongying20170106
SET @eh_configurations = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@eh_configurations := @eh_configurations + 1), 'mail.smtp.address', 'smtp.genzon.com.cn', '', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@eh_configurations := @eh_configurations + 1), 'mail.smtp.port', '25', '', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@eh_configurations := @eh_configurations + 1), 'mail.smtp.account', 'caoxiuran@genzon.com.cn', '', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ((@eh_configurations := @eh_configurations + 1), 'mail.smtp.passwod', '789651q@', '', '999983', NULL);

-- 以前的模板也加上dynamic字段 by xiongying20170106
update eh_request_templates set fields_json = '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业名称","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入企业全称","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"cityName","fieldDisplayName":"企业城市","fieldType":"string","fieldContentType":"text","fieldDesc":"企业所在城市","requiredFlag":"1"},{"fieldName":"industry","fieldDisplayName":"企业行业","fieldType":"string","fieldContentType":"text","fieldDesc":"企业所属行业","requiredFlag":"1"},{"fieldName":"financingStage","fieldDisplayName":"融资阶段","fieldType":"string","fieldContentType":"text","fieldDesc":"融资阶段","requiredFlag":"1"},{"fieldName":"financingAmount","fieldDisplayName":"融资金额(万元)","fieldType":"decimal","fieldContentType":"text","fieldDesc":"融资金额（万元）","requiredFlag":"1"},{"fieldName":"transferShares","fieldDisplayName":"出让股份(%)","fieldType":"number","fieldContentType":"text","fieldDesc":"出让股份 %","requiredFlag":"1"},{"fieldName":"projectDesc","fieldDisplayName":"项目描述","fieldType":"string","fieldContentType":"text","fieldDesc":"项目描述","requiredFlag":"1"},{"fieldName":"attachments","fieldDisplayName":"附件图片","fieldType":"blob","fieldContentType":"image","fieldDesc":"","requiredFlag":"0"},{"fieldName":"attachments","fieldDisplayName":"商业计划书","fieldType":"blob","fieldContentType":"file","fieldDesc":"","requiredFlag":"0"}]}' where id = 1;
update eh_request_templates set fields_json = '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入姓名","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"手机号","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入手机号","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业名称","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入企业全称","requiredFlag":"1","dynamicFlag":"1"}]}' where id = 2;


-- 运营统计菜单配置到科技园 add by sfyan 20170106
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 41300, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes`    (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 41310, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 41320, '', 'EhNamespaces', 1000000, 2);


-- 科兴新增服务联盟菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
VALUES ('80000', '园区服务', '0', 'fa fa-group', NULL, '1', '2', '/80000', 'park', '800', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80100', '服务联盟', '80000', NULL, NULL, '1', '2', '/80000/80100', 'park', '810', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80110', '服务列表', '80100', NULL, 'service_list/155', '0', '2', '/80000/80100/80110', 'park', '820', NULL);

SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10120, '0', '园区服务 管理员', '园区服务 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10121, '0', '服务联盟-用户版 管理员', '服务联盟 业务模块权限', NULL);

INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10120', '80000', '园区服务', '1', '1', '园区服务 全部权限', '710');

INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10121', '80100', '服务联盟', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10121', '80110', '服务联盟', '1', '1', '服务联盟 全部权限', '711');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80000, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80100, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 80110, '', 'EhNamespaces', 999983, 2);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1005,0,1,now(),'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10120;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1005,0,1,now(),'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10121;

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10122, '0', '企业服务 管理员', '企业服务 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10123, '0', '厂房出租 管理员', '厂房出租 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10124, '0', '公寓出租 管理员', '公寓出租 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10125, '0', '医疗 管理员', '医疗 业务模块权限', NULL);

INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10122', '80200', '企业服务', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10122', '80210', '企业服务', '1', '1', '服务联盟 全部权限', '711');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10123', '80300', '厂房出租', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10123', '80310', '厂房出租', '1', '1', '服务联盟 全部权限', '711');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10124', '80400', '公寓出租', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10124', '80410', '公寓出租', '1', '1', '服务联盟 全部权限', '711');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10125', '80500', '医疗', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10125', '80510', '医疗', '1', '1', '服务联盟 全部权限', '711');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80200', '企业服务', '80000', NULL, NULL, '1', '2', '/80000/80200', 'park', '810', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80210', '服务列表', '80200', NULL, 'service_list/151', '0', '2', '/80000/80200/80210', 'park', '820', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80300', '厂房出租', '80000', NULL, NULL, '1', '2', '/80000/80300', 'park', '810', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80310', '服务列表', '80300', NULL, 'service_list/152', '0', '2', '/80000/80300/80310', 'park', '820', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80400', '公寓出租', '80000', NULL, NULL, '1', '2', '/80000/80400', 'park', '810', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80410', '服务列表', '80400', NULL, 'service_list/153', '0', '2', '/80000/80400/80410', 'park', '820', NULL);

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80500', '医疗', '80000', NULL, NULL, '1', '2', '/80000/80500', 'park', '810', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80510', '服务列表', '80500', NULL, 'service_list/154', '0', '2', '/80000/80500/80510', 'park', '820', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1005,0,1,now(),'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10122;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1005,0,1,now(),'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10123;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1005,0,1,now(),'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10124;
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1005,0,1,now(),'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10125;

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80200, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80210, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80300, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80310, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80400, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80410, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80500, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80510, '', 'EhNamespaces', 999983, 2);


INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80120', '我的申请', '80100', NULL, 'apply_list/155', '0', '2', '/80000/80100/80120', 'park', '820', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80220', '我的申请', '80200', NULL, 'apply_list/151', '0', '2', '/80000/80200/80220', 'park', '820', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80320', '我的申请', '80300', NULL, 'apply_list/152', '0', '2', '/80000/80300/80320', 'park', '820', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80420', '我的申请', '80400', NULL, 'apply_list/153', '0', '2', '/80000/80400/80420', 'park', '820', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('80520', '我的申请', '80500', NULL, 'apply_list/154', '0', '2', '/80000/80500/80520', 'park', '820', NULL);

SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10121', '80120', '服务联盟', '1', '1', '服务联盟 全部权限', '711');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10122', '80220', '企业服务', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10123', '80320', '厂房出租', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10124', '80420', '公寓出租', '1', '1', '服务联盟 全部权限', '710');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10125', '80520', '医疗', '1', '1', '服务联盟 全部权限', '710');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80120, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80220, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80320, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80420, '', 'EhNamespaces', 999983, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 80520, '', 'EhNamespaces', 999983, 2);


-- 更新菜单datatype
update eh_web_menus set data_type = 'access_manage/inside' where data_type = 'access_manage_inside' and name = '门禁管理';
update eh_web_menus set data_type = 'version_manage/inside' where data_type = 'version_manage_inside' and name = '版本管理';
update eh_web_menus set data_type = 'access_group/inside' where data_type = 'access_group_inside' and name = '门禁分组';
update eh_web_menus set data_type = 'user_auth/inside' where data_type = 'user_auth_inside' and name = '用户授权';
update eh_web_menus set data_type = 'visitor_auth/inside' where data_type = 'visitor_auth_inside' and name = '访客授权';
update eh_web_menus set data_type = 'access_log/inside' where data_type = 'access_log_inside' and name = '门禁日志';

update eh_web_menus set data_type = 'forum_activity/road' where data_type = 'road_show' and name = '路演表演';
update eh_web_menus set data_type = 'forum_activity/whiteCollar' where data_type = 'white_collar_activity' and name = '白领活动';
update eh_web_menus set data_type = 'forum_activity/OE' where data_type = 'OE_auditorium' and name = 'OE大讲堂';
update eh_web_menus set data_type = 'news_management/industry' where data_type = 'industry_dynamics' and name = '行业动态';

-- 物业报修与工作流对接
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('1325', 'pmtask.handler-1000000', 'flow', '', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('1326', 'pmtask.handler-999983', 'flow', '', '0', NULL);
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) 
	VALUES ( 'pmtask', '10008', 'zh_CN', '请启用工作流！');

INSERT INTO `eh_web_menus` VALUES ('40850', '工作流设置', '40800', null, 'react:/working-flow/flow-list', '0', '2', '/40000/40800/40850', 'park', '475');

SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10028', '40850', '停车缴费', '1', '1', '停车缴费 全部权限', '710');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 40850, '', 'EhNamespaces', 1000000, 2);

delete from eh_web_menu_scopes where menu_id = 20160 and owner_id = 1000000;
delete from eh_web_menu_scopes where menu_id = 20192 and owner_id = 1000000;

	
-- -- add by xionging 2017.1.9
delete from eh_service_alliance_jump_module where id = 1;
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`) VALUES (3, 999983, '投诉建议', 'zl://propertyrepair/create?type=user&taskCategoryId=202564&displayName=投诉建议');
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`) VALUES (4, 999983, '物业报修', 'zl://propertyrepair/create?type=user&taskCategoryId=202565&displayName=物业报修');

-- 科技园更新物业报修 add by sw 2017/01/10
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
	VALUES ('20158', '工作流设置', '20155', NULL, 'react:/working-flow/flow-list/property-service/20100', '0', '2', '/20000/20100/20155/20158', 'park', '230', NULL);

delete from eh_web_menu_scopes where menu_id = 20160 and owner_id = 1000000;
delete from eh_web_menu_scopes where menu_id = 20192 and owner_id = 1000000;
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20158, '', 'EhNamespaces', 1000000, 2);

update eh_launch_pad_items set action_type = 60, action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}' where namespace_id= 1000000 and item_label = '物业报修';


-- 把能耗管理菜单由原来在“运营服务”下 移到 “物业服务”菜单下 by lqs 20170114 已上到现网
UPDATE `eh_web_menus` SET parent_id=20000, `path`='/20000/49100' WHERE `id`=49100; -- 能耗管理
UPDATE `eh_web_menus` SET `path`='/20000/49100/49110' WHERE `id`=49110; -- 表计管理
UPDATE `eh_web_menus` SET `path`='/20000/49100/49120' WHERE `id`=49120; -- 抄表记录
UPDATE `eh_web_menus` SET `path`='/20000/49100/49130' WHERE `id`=49130; -- 统计信息
UPDATE `eh_web_menus` SET `path`='/20000/49100/49140' WHERE `id`=49140; -- 参数设置
UPDATE `eh_service_modules` SET parent_id=20000, `path`='/20000/49100' WHERE `id`=49100; -- 能耗管理


-- move from db/3.12.4-delta-data-release.sql for it's already released by lqs 20170116
-- 添加更新时间，add by tt, 20170111
update eh_users set update_time = now() where update_time is null;
update eh_organizations set update_time = now() where update_time is null;
update eh_organization_address_mappings set create_time = now(), update_time = now() where update_time is null;
update eh_rentalv2_orders set operate_time = now() where operate_time is null;
update eh_activities set update_time = now() where update_time is null;
update eh_office_cubicle_orders set create_time = now(), update_time = now() where update_time is null;

-- 金地同步数据代理的appKey，add by tt, 20170111
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES (5001, 1, 'dca1f405-7675-4ac2-ab72-e16c918fd7c2', 'VgAddn6IGPK/gO44eUWutfjLuotMcyKz3ZpwAr2jAUSsJgIi50cvntxL4QOqgZYEXkcislDwrDmLzSeHuFWQgQ==', 'jin di idata proxy', 'jin di idata proxy', 1, '2016-11-09 11:49:16', NULL, NULL);

-- 金地同步数据限制域空间，add by tt, 20170111
select max(id) into @id from `eh_configurations`;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id+1, 'jindi.sync.namespace', '999989,999991', 'jindi sync data namespace', 0, NULL);

-- 活动管理权限管理模块 add sfyan 20170112
SET @service_module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@service_module_privilege_id := @service_module_privilege_id + 1), '10600', '0', '310', NULL, '0', UTC_TIMESTAMP());

-- 物业报修 add by sw 20170112
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) 
	VALUES ( 'pmtask', '10009', 'zh_CN', '用户已有该权限，不能重复添加！');
	
	

	
	

-- merge blacklist sfyan 20170114
-- 黑名单权限
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1001, '0', '禁止话题、投票帖子发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1002, '0', '禁止活动帖子发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1003, '0', '禁止公告帖子发言', '包括发帖、评论、回复评论', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1004, '0', '禁止园区快讯板块发言', '包括发帖、评论、回复评论', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1005, '0', '禁止新建俱乐部板块', '包括新建俱乐部', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1006, '0', '禁止物业服务板块发言', '包括【包括投诉建议、紧急求助、咨询求助、报修】发帖', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1007, '0', '禁止恶意留言', '包括在消息模块（包括群聊或私信）中发言', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1008, '0', '禁止意见反馈', '包括发帖、评论、回复评论', NULL);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1001', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1002', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1003', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1004', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1005', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1006', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1007', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`) 
	VALUES ((@acl_id := @acl_id + 1), 'system', NULL, '1', '1008', '2000', 'EhAclRoles', '0', '1', UTC_TIMESTAMP());
	
-- 错误提示语配置
UPDATE `eh_locale_strings` SET `text` = '您权限不足' WHERE `scope` = 'general' AND `code` = '505';
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600000', 'zh_CN', '由于您已被禁言，不能正常使用该功能。');	
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600020', 'zh_CN', '未查询到符合条件的对象，请查证。');	
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'blacklist', '600010', 'zh_CN', '黑名单已存在。');	

-- 黑名单权限 
insert into `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) values('30600','黑名单管理','30000',NULL,'react:/blacklist-management/black-list','0','2','/30000/30600','park','361','30600');
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`)
VALUES (10090, 0, '黑名单管理 管理员', '黑名单管理 管理员权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), 10090, 30600, '黑名单管理', 1, 1, '黑名单管理 管理员权限', 361);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 10090, 1001,'EhAclRoles', 0, 1, NOW());


-- SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
-- VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 0, 2);

INSERT INTO `eh_locale_templates` (`scope`, `code`,`locale`,`description`, `text`, `namespace_id`) VALUES( 'blacklist.notification', '1', 'zh_CN', '通知用户已经被加入黑名单', '由于您的发言涉及部分违反相关版规行为，您已被禁言，将不能正常使用部分板块的发言功能。如有疑问，请联系左邻客服。', 0);	
INSERT INTO `eh_locale_templates` (`scope`, `code`,`locale`,`description`, `text`, `namespace_id`) VALUES( 'blacklist.notification', '2', 'zh_CN', '通知用户已经被解除黑名单', '您的禁言已被解除，可继续使用各大板块的发言功能。如有疑问，请联系左邻客服。', 0);	
	
	



-- merge doorAuth by sfyan 20170112
-- 运营统计的数据修改 add sfyan 20170112
UPDATE `eh_terminal_day_statistics` SET `start_change_rate` = `start_change_rate` * -1, `new_change_rate` = `new_change_rate` * -1, `active_change_rate` = `active_change_rate` * -1, `cumulative_change_rate` = `cumulative_change_rate` * -1;

-- 重复审批提示 add sfyan 20170114
SELECT max(id) FROM eh_locale_strings INTO @max_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@max_id := @max_id + 1), 'organization', '500005', 'zh_CN', '已被其他管理员审批！');
  

-- app版本数据整理  add sfyan 20170117
SET @version_id = (SELECT MAX(id) FROM `eh_app_version`);
INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '3.12.0', id, 3158016.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '3.12.4', id, 3158020.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '4.1.0', id, 4195328.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '4.1.2', id, 4195330.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'ios', '4.2.2', id, 4196354.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '3.12.0', id, 3158016.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '3.12.4', id, 3158020.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '4.1.0', id, 4195328.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '4.1.2', id, 4195330.0, now() FROM `eh_namespaces`;

INSERT INTO eh_app_version (`id`,`type`,`name`,`namespace_id`,`default_order`,`create_time`) SELECT (@version_id := @version_id + 1), 'android', '4.2.2', id, 4196354.0, now() FROM `eh_namespaces`;
  

  -- 黑名单配置到清华信息港 by sfyan 20170117
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999984, 2);
 
 -- 黑名单配置到创源 by sfyan 20170117
 SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999986, 2);
 
 -- 黑名单配置到左邻 by sfyan 20170117
  SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
 INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
 VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 0, 2);


-- 机构人员字段填值   by sfyan 20170118
UPDATE `eh_organization_members` eom SET `group_type` = (SELECT `group_type` FROM `eh_organizations` where `id` = eom.organization_id), `group_path` = (SELECT `path` FROM `eh_organizations` where `id` = eom.organization_id);

-- merge from quality2.1 by xiongying 20170118 
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('quality', '10012', 'zh_CN', '任务模板不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('quality', '10013', 'zh_CN', '只能删除自己创建的模板');

	