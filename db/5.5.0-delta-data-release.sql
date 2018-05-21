-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8  start
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10001', 'zh_CN', '相同号码已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10002', 'zh_CN', '查询记录时未指定客服或热线');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10003', 'zh_CN', '查询记录时未指定用户');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10004', 'zh_CN', '需要更新/删除的记录不存在');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40310, '公共热线', 40300, '/40000/40300/40310', 1, 3, 2, 0, '2018-04-02 17:18:58', NULL, NULL, '2018-05-02 17:18:58', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40320, '专属客服', 40300, '/40000/40300/40320', 1, 3, 2, 0, '2018-04-02 17:18:58', NULL, NULL, '2018-05-02 17:18:58', 0, 1, '1', NULL, '');

SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40310, 0, 4030040310, '公共热线', 0, '2018-04-02 17:18:58');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40320, 0, 4030040320, '客服管理', 0, '2018-04-02 17:18:58');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40320, 0, 4030040321, '历史会话', 0, '2018-04-02 17:18:58');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040310, 0, '公共热线 全部权限', '公共热线 全部权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040320, 0, '专属客服 客服管理', '专属客服 客服管理', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040321, 0, '专属客服 历史会话', '专属客服 历史会话', NULL);


-- 路福联合广场对接的秘钥 by 杨崇鑫
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) 
VALUES ('openapi.lufu.key', '70f2ea6d54fb44d5a18ac11f66d25154', '路福联合广场对接的秘钥', 999963, NULL);

-- issue-29029 物业报修工作流状态修改
UPDATE eh_pm_tasks t, eh_flow_cases f SET t.`status` = IFNULL(f.`status`, 3) WHERE t.`status` = 3 AND f.id = t.flow_case_id;
UPDATE eh_pm_tasks t SET t.`status` = 2 WHERE t.`status` IN (1,3);

-- asset_5.1_wentian by wentian 2018/5/15
SET @id = ifnull((SELECT max(`id`) from `eh_locale_templates`),0);
set @code_id = ifnull((SELECT max(`code`) from `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'user.notification.app.asset', @code_id:=@code_id+1, 'zh_CN', '物业账单通知用户模板，欠费前', '尊敬的租户${targetName}先生/小姐：您好，您至今未缴：${dateStr}月服务费共计：${amount}元。请及时缴纳，谢谢您的配合！', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'user.notification.app.asset', @code_id:=@code_id+1, 'zh_CN', '物业账单通知用户模板，欠费后', '尊敬的租户${targetName}先生/小姐：您好，请尽快缴纳${dateStr}月服务费：${amount}元。如您明日仍未缴纳，将视为逾期，我司将停止相关物业服务，并计收相应滞纳金，引起一切后果由租户自行承担。谢谢！', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'user.notification.app.asset', @code_id:=@code_id+1, 'zh_CN', '物业账单通知用户模板，欠费很久后', '尊敬的租户${targetName}先生/小姐：您好，您已拖欠${dateStr}月服务费：${amount}元，我司至今未收到您的欠款。物业管理公司将按《物业管理条例》的规定，在欠费后次日对贵租户暂停各项服务，出现任何后果，责任自负。我公司并保留通过法律途径追缴的权利。', 0);

-- 短信模板 申请成功过的，物业缴费模块 by wentian @2018/5/10
-- 模板： 尊敬的租户xx先生/小姐：您好，您至今未缴：{2018-5}月服务费共计：xx元。请及时缴纳，谢谢您的配合！
SET @id = ifnull((SELECT max(`id`) from `eh_locale_templates`),0);
set @code_id = ifnull((SELECT max(`code`) from `eh_locale_templates`),0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'sms.default', @code_id:=@code_id+1, 'zh_CN', '短信通知模板，用于欠费前', '尊敬的租户${targetName}先生/小姐：您好，您至今未缴：${dateStr}月服务费共计：${amount}元。请及时缴纳，谢谢您的配合！', 0);
-- 用来查找模板用的
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'asset.sms', @code_id, 'zh_CN', '短信通知模板，用于欠费前', '尊敬的租户${targetName}先生/小姐：您好，您至今未缴：${dateStr}月服务费共计：${amount}元。请及时缴纳，谢谢您的配合！', 0);

-- 999958，999983，999966，999954不缴费，999955不看合同不缴费 by wentian 2018/5/15
set @id = IFNULL((select max(`id`) from `eh_payment_app_views`),0);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999983, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999958, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999966, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999954, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999955, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999955, NULL, 0, 'CONTRACT', NULL , NULL , NULL, NULL, NULL, NULL);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, 999950, NULL, 0, 'PAY', NULL , NULL , NULL, NULL, NULL, NULL);

-- 物业报修应用配置设置默认值
UPDATE `eh_service_modules` SET `instance_config`='{\"angetSwitch\":1}' WHERE (`id`='20100');




-- 删除管理员设置界面的企业管理module
DELETE FROM  eh_web_menus WHERE module_id = '33000';

-- 企业客户管理增加的权限 将企业管理中的管理员作为权限项  by jiarui
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21115', '0', '设置管理员', '企业客户管理 设置管理员', NULL);

SET  @id = (SELECT  max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@id:=@id+1), '21110', '0', '21115', '设置管理员', '14', now());


-- 企业客户类型由必选更改成非必选 合并动态表单多个字段   by jiarui
UPDATE eh_var_fields SET mandatory_flag = 0 WHERE name = 'categoryItemId' AND module_name = 'enterprise_customer';
UPDATE eh_var_fields SET display_name = '联系电话' WHERE name = 'contactPhone' AND module_name = 'enterprise_customer';

set @fieldId = IFNULL((SELECT id FROM eh_var_fields WHERE name = 'contactMobile' AND module_name = 'enterprise_customer' LIMIT 1),0);
DELETE FROM eh_var_field_scopes  where field_id = @fieldId and  module_name = 'enterprise_customer';

DELETE FROM eh_var_fields  WHERE name = 'contactMobile' AND module_name = 'enterprise_customer';

-- 迁移合并之后的字段数据  联系人电话 座机电话  by jiarui
UPDATE eh_enterprise_customers
SET contact_phone = CONCAT(contact_mobile,',',contact_phone)
WHERE contact_phone IS NOT NULL AND contact_mobile IS NOT NULL;

UPDATE eh_enterprise_customers
SET contact_phone = contact_mobile
WHERE contact_phone IS NULL AND contact_mobile IS NOT NULL;

UPDATE eh_enterprise_customers SET contact_phone = NULL WHERE contact_phone = '';
-- 迁移企业管理的是否设置管理员
UPDATE  eh_enterprise_customers SET admin_flag = IFNULL((SELECT set_admin_flag FROM eh_organizations WHERE id = eh_enterprise_customers.organization_id) ,0);

-- 增加企业管理中的动态字段  by jiarui

SET  @id = (SELECT MAX(id) FROM eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'unifiedSocialCreditCode', '统一社会信用代码', 'String', '11', '/1/11/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'postUri', '标题图', 'String', '11', '/1/11/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"image\", \"length\": 1}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'banner', 'banner图', 'String', '11', '/1/11/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"image\", \"length\": 9}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'enterprise_customer', 'hotline', '咨询电话', 'String', '11', '/1/11/', '0', NULL, '2', '1', now(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');


-- 企业简介改成richText by jiarui
UPDATE `eh_var_fields` SET `field_param`='{\"fieldParamType\": \"richText\", \"length\": 204800}' WHERE `name`='corpDescription' and `module_name`='enterprise_customer';

UPDATE eh_var_field_scopes set mandatory_flag = 1 where  field_id in (10966,10965);
UPDATE  eh_enterprise_customers set tracking_uid = NULL  WHERE  tracking_uid = -1;

-- issue-28517 越空间门禁二维码防截图 by liuyilin 20180521
SET @var_id = (SELECT MAX(`id`) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@var_id:=@var_id+1, 'aclink.qr_driver_zuolin_inner', 'zuolin_v2', 'use version2 of zuolin driver', '999957', NULL);