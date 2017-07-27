-- 表单 by dengs,20170711
set @eh_general_forms_id = (select max(id) from eh_general_forms);
set @namespace_id = 999971; -- 此处应该修改为张江的namespace
set @appKey = 'd923d02c-453d-4cd8-a948-a34ca312058e';
set @secretKey = 'N0S7cUQKjz1k80MvDrnZ19Qc5d5F1DlqQorfTtr9K9NgYRmNTtE7soE1fRxwPG0E9kAoGsxIHpX8i7o6JploTQ==';

-- appkey
set @eh_apps_id = (select MAX(id) FROM eh_apps);
INSERT INTO `ehcore`.`eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES ((@eh_apps_id:=@eh_apps_id+1), '1', @appKey, @secretKey, 'zjgk', 'zjgk app key', '1', NOW(), NULL, NULL);
-- appkey mapping namespace
set @eh_app_namespace_mappings_id = (select MAX(id) FROM eh_app_namespace_mappings);
INSERT INTO `ehcore`.`eh_app_namespace_mappings` (`id`, `namespace_id`, `app_key`, `community_id`) VALUES ((@eh_app_namespace_mappings_id:=@eh_app_namespace_mappings_id+1), @namespace_id, @appKey, '1');

-- json 需要重新写
INSERT INTO `eh_general_forms` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `form_origin_id`, `form_version`, `template_type`, `template_text`, `status`, `update_time`, `create_time`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES ((@eh_general_forms_id := @eh_general_forms_id+1), @namespace_id, 0, 0, 'personal_auth', NULL, NULL, '张江个人认证', 448, 0, 'DEFAULT_JSON', '[{"dataSourceType": "USER_PHONE","fieldDesc": "请输入手机号码","fieldDisplayName": "手机号","fieldExtra": "{\\"limitWord\\":50}","fieldName": "phone","fieldType": "MULTI_LINE_TEXT","renderType": "DEFAULT","requiredFlag": 1,"validatorType": "TEXT_LIMIT","visibleType": "READONLY"},{"fieldDesc": "请输入真实姓名","fieldDisplayName": "姓名","fieldExtra": "{\\"limitWord\\":50}","fieldName": "name","fieldType": "MULTI_LINE_TEXT","renderType": "DEFAULT","requiredFlag": 1,"validatorType": "TEXT_LIMIT","visibleType": "EDITABLE"},{"fieldDesc": "身份证","fieldDisplayName": "证件类型","fieldExtra": "{\\"limitWord\\":30}","fieldName": "certificateType","fieldType": "MULTI_LINE_TEXT","renderType": "DEFAULT","requiredFlag": 1,"validatorType": "TEXT_LIMIT","visibleType": "EDITABLE"},{"fieldDesc": "请输入证件号码","fieldDisplayName": "证件号码","fieldExtra": "{\\"limitWord\\":50}","fieldName": "certificateNo","fieldType": "MULTI_LINE_TEXT","renderType": "DEFAULT","requiredFlag": 1,"validatorType": "TEXT_LIMIT","visibleType": "EDITABLE"}]', 1, NOW(), NOW(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `eh_general_forms` (`id`, `namespace_id`, `organization_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `form_name`, `form_origin_id`, `form_version`, `template_type`, `template_text`, `status`, `update_time`, `create_time`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`) VALUES ((@eh_general_forms_id := @eh_general_forms_id+1), @namespace_id, 0, 0, 'organization_auth', NULL, NULL, '张江企业认证', 449, 0, 'DEFAULT_JSON', '[{"fieldDesc": "请输入准确的组织机构代码","fieldDisplayName": "组织机构代码","fieldExtra": "{\\"limitWord\\":50}","fieldName": "organizationCode","fieldType": "MULTI_LINE_TEXT","renderType": "DEFAULT","requiredFlag": 1,"validatorType": "TEXT_LIMIT","visibleType": "EDITABLE"},{"fieldDesc": "请输入承租时填写的企业联系人","fieldDisplayName": "企业联系人","fieldExtra": "{\\"limitWord\\":50}","fieldName": "organizationContact","fieldType": "MULTI_LINE_TEXT","renderType": "DEFAULT","requiredFlag": 1,"validatorType": "TEXT_LIMIT","visibleType": "EDITABLE"},{"fieldDesc": "请输入承租时填写的企业联系电话","fieldDisplayName": "企业联系电话","fieldExtra": "{\\"limitWord\\":50}","fieldName": "organizationPhone","fieldType": "MULTI_LINE_TEXT","renderType": "DEFAULT","requiredFlag": 1,"validatorType": "TEXT_LIMIT","visibleType": "EDITABLE"}]', 1, NOW(), NOW(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

-- 张江表单和张江namespace关联
INSERT INTO `eh_authorization_third_party_forms` (`id`, `namespace_id`, `owner_type`, `owner_id`, `source_type`, `source_id`, `authorization_url`, `app_key`, `secret_key`, `title`, `detail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1, @namespace_id, 'EhNamespaces', @namespace_id, 'personal_auth', 0, 'http://139.129.220.146:3578/openapi/Authenticate', 'ee4c8905-9aa4-4d45-973c-ede4cbb3cf21', '2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==', '个人认证', '认证我的居住地址', 0, NOW(), NULL, NOW());
INSERT INTO `eh_authorization_third_party_forms` (`id`, `namespace_id`, `owner_type`, `owner_id`, `source_type`, `source_id`, `authorization_url`, `app_key`, `secret_key`, `title`, `detail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (2, @namespace_id, 'EhNamespaces', @namespace_id, 'organization_auth', 0, 'http://139.129.220.146:3578/openapi/Authenticate', 'ee4c8905-9aa4-4d45-973c-ede4cbb3cf21', '2CQ7dgiGCIfdKyHfHzO772IltqC50e9w7fswbn6JezdEAZU+x4+VHsBE/RKQ5BCkz/irj0Kzg6te6Y9JLgAvbQ==', '企业认证', '认证企业承租地址', 0, NOW(), NULL, NOW());

-- buttons状态
INSERT INTO `eh_authorization_third_party_buttons` (`id`, `namespace_id`, `owner_type`, `owner_id`, `title`, `modify_flag`, `families_flag`, `qrcode_flag`, `delete_flag`, `blank_detail`,`button_detail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (1, @namespace_id, 'EhNamespaces', @namespace_id, '公寓信息', 0, 1, 0, 0,'您还未加入任何公寓，快来加入吧！','申请认证', NULL, NOW(), NULL, NOW());

-- 文本
set @eh_locale_strings_id = (select MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
	((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'third.party.authorization', 'work_flow_title', 'zh_CN', '姓名 : |证件号码 : |个人认证申请|组织机构代码 : |企业联系人 : |企业认证申请');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
	((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'third.party.authorization', 'personal_back_code_detail', 'zh_CN', '很遗憾，您的认证未成功|1、请检测信息填写是否正确，如填写有误，请重新提交|2、如填写无误，请到携带相关证件管理处核对信息|您已退租，如有疑问请联系客服|恭喜您，成为我们的一员，您承租的地址信息如下|未定义的返回码|园区[|]不存在|园区[|]名称存在多个|地址|请求认证失败！|系统认证异常，已退出家庭');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
	((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'third.party.authorization', 'organization_back_code_detail', 'zh_CN', '提交信息匹配不成功|1、请检测信息填写是否正确，如填写有误，请重新提交|2、如填写无误，请到携带相关证件管理处核对信息|您已退租，如有疑问请联系客服|认证成功！您承租地址信息如下|未定义的返回码|园区[|]不存在|园区[|]名称存在多个|地址|请求认证失败！|系统认证异常，已退出公司');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
	((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'third.party.authorization', 'work_flow_content', 'zh_CN', '组织机构代码|企业联系人|企业联系电话|认证反馈结果|手机号|姓名|证件类型|证件号码|认证反馈结果|身份证|未知证件类型');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
	((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'third.party.authorization', 'family_detail', 'zh_CN', '家庭信息|您还未加入任何家庭，快去加入吧!|添加住址');

--
-- eh_user_activities 数据处理(这些表的数据比较多，可能会比较费时) add by xq.tian 2017/07/25
--
-- 第一步
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX i_eh_imei_number(imei_number);
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX i_eh_create_time(create_time);
ALTER TABLE eh_terminal_app_version_cumulatives ALGORITHM=inplace, LOCK=NONE, ADD INDEX i_eh_imei_number(imei_number);
ALTER TABLE eh_terminal_app_version_actives ALGORITHM=inplace, LOCK=NONE, ADD INDEX i_eh_imei_number(imei_number);

-- 第二步
UPDATE `eh_terminal_app_version_actives` AS t, `eh_user_activities` AS u
SET t.`imei_number` = u.`uid`
WHERE t.`imei_number` = u.`imei_number` AND u.`uid` <> 0;

UPDATE `eh_terminal_app_version_cumulatives` AS t, `eh_user_activities` AS u
SET t.`imei_number` = u.uid
WHERE t.`imei_number` = u.`imei_number` AND u.uid <> 0;

DELETE FROM `eh_user_activities` WHERE `uid` = 0;

UPDATE `eh_user_activities` AS u SET u.`imei_number` = u.`uid`;


-- by xiongying
update eh_service_modules set level = 3 where id = 20811;
update eh_service_modules set path = '/20000/20800/20811' where id = 20811;

SET @template_id = (SELECT MAX(id) FROM `eh_locale_templates`); 
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@template_id := @template_id + 1), 'equipment.notification', '6', 'zh_CN', '通知过期任务', '“${taskName}”过期未执行，请到后台查看详情', '0');



-- 开放业务责任部门的模块权限分配 add by sfyan 20150725
SET @service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `apply_policy`) select (@service_module_scopes_id := @service_module_scopes_id + 1), ewms.owner_id, ifnull(ewm.module_id, ewm.id),2 from eh_web_menu_scopes ewms left join eh_web_menus ewm on ewms.menu_id = ewm.id where owner_type = 'EhNamespaces' and (ewm.id = 60200) group by ifnull(ewm.module_id, ewm.id),ewms.owner_id;

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES(60210,'责任部门配置',60200,'/60000/60200/60210','1','3','2','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40023, '0', 'module.conf.relation.list', '责任部门配置列表查询', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60210','0',40023,'责任部门配置列表查询','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40024, '0', 'module.conf.relation.create', '创建修改责任部门配置', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60210','0',40024,'创建修改责任部门配置','0',NOW());
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (40026, '0', 'module.conf.relation.delete', '删除责任部门配置', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'60210','0',40026,'删除责任部门配置','0',NOW());
    
-- fix 13160 数据交错问题 add by xiongying 20170727
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) 
VALUES ('20220', '任务列表', '20200', NULL, 'react:/repair-management/task-list/1', '0', '2', '/20000/20230/20220', 'park', '220', '20100', '3', NULL, 'module');

update `eh_web_menu_scopes` set menu_id = 20220 where menu_id = 20210 and owner_id = 999983;

-- 添加一个手机号黑名单  add by xq.tian 2017/07/27
SET @max_id = IFNULL((select max(id) from `eh_sms_black_lists`), 1);
INSERT INTO `eh_sms_black_lists` (`id`, `namespace_id`, `contact_token`, `reason`, `status`, `create_type`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
VALUES ((@max_id := @max_id + 1), 0, '13111116818', 'manual', 1, 1, 0, NOW(), NULL, NULL);