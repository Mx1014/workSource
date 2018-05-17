-- issue-26471 服务热线V1.5（客服聊天记录保存和导出) by huangmingbo 2018.5.8  start
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10001', 'zh_CN', '相同号码已存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10002', 'zh_CN', '查询记录时未指定客服或热线');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10003', 'zh_CN', '查询记录时未指定用户');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('hotline', '10004', 'zh_CN', '需要更新/删除的记录不存在');

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40310, '公共热线', 40300, '/40000/40300/40310', 1, 3, 2, 0, '2018-04-02 17:18:58', NULL, NULL, '2018-05-02 17:18:58', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40320, '专属客服', 40300, '/40000/40300/40320', 1, 3, 2, 0, '2018-04-02 17:18:58', NULL, NULL, '2018-05-02 17:18:58', 0, 1, '1', NULL, '');

SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40310, 0, 4030040310, '公共热线', 0, '2018-04-02 17:18:58');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40320, 0, 4030040320, '专属客服', 0, '2018-04-02 17:18:58');
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@mp_id:=@mp_id+1, 40320, 0, 4030040321, '历史会话', 0, '2018-04-02 17:18:58');

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040310, 0, '公共热线 全部权限', '公共热线 全部权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040320, 0, '专属客服 客服管理', '专属客服 客服管理', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040321, 0, '专属客服 历史会话', '专属客服 历史会话', NULL);


-- issue-26498 停车缴费V6.5.2（深圳湾项目小猫接口更新） by huangmingbo 2018.5.10 
UPDATE `eh_configurations` SET `value`='http://119.23.144.8' WHERE  name='parking.xiaomao.url';
UPDATE `eh_configurations` SET `value`='0755000021433988491' WHERE  name='parking.xiaomao.parkId.10011';
UPDATE `eh_configurations` SET `value`='07550002501499136602' WHERE  name='parking.xiaomao.parkId.10012';

INSERT INTO `eh_configurations` (`name`, `value`) VALUES ('parking.xiaomao.accessKeyId.10011', 'rjcy0504');
INSERT INTO `eh_configurations` (`name`, `value`) VALUES ('parking.xiaomao.accessKeyId.10012', 'ctds0503');
INSERT INTO `eh_configurations` (`name`, `value`) VALUES ('parking.xiaomao.accessKeyValue.10011', '7c9f305115024764a3a74a44040f986b');
INSERT INTO `eh_configurations` (`name`, `value`) VALUES ('parking.xiaomao.accessKeyValue.10012', '1accf1d97436aadf2a31936b3c3e4184');
INSERT INTO `eh_configurations` (`name`, `value`, `description`) VALUES ('parking.xiaomao.free.time', '15', '临时停车默认免费时长');

delete from eh_configurations where name = 'parking.xiaomao.accessKeyId' ;
delete from eh_configurations where name = 'parking.xiaomao.accessKeyValue' ;
delete from eh_configurations where name = 'parking.xiaomao.types.10011' ;
delete from eh_configurations where name = 'parking.xiaomao.types.10012' ;

--  issue-26498 add by huangmingbo 
SET @ruanji_id = 10011;
SET @chuangtou_id = 10012;
SET @ruanji_community_id = 240111044331050370;
SET @chuangtou_community_id = 240111044331050371;
SET @szw_namespace_id = 999966;

-- issue-26498 add by huangmingbo 2018-05-09
SET @car_type_id = (SELECT IFNULL(MAX(id),0) FROM eh_parking_card_types);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, @szw_namespace_id, 'community', @chuangtou_community_id, @chuangtou_id, '02', 'VIP月卡', 2, 1, '2018-05-09 10:49:48', NULL, NULL);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, @szw_namespace_id, 'community', @ruanji_community_id, @ruanji_id, '11', 'VIP月卡', 2, 1, '2018-05-09 10:49:48', NULL, NULL);
INSERT INTO `eh_parking_card_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `parking_lot_id`, `card_type_id`, `card_type_name`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`) VALUES (@car_type_id:=@car_type_id+1, @szw_namespace_id, 'community', @ruanji_community_id, @ruanji_id, '5', '普通月卡', 2, 1, '2018-05-09 10:49:48', NULL, NULL);

-- issue-26498 add by huangmingbo 2018-05-09
UPDATE `eh_parking_lots` SET `config_json`='{"tempfeeFlag": 1, "rateFlag": 1, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 2, "contact": "18665331243","identityCardFlag":0}' WHERE  `id`in (10011, 10012);

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


-- 企业简介改成richText
UPDATE `eh_var_fields` SET `field_param`='{\"fieldParamType\": \"richText\", \"length\": 204800}' WHERE `name`='corpDescription' and `module_name`='enterprise_customer';