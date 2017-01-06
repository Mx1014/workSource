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



