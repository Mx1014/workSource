-- 薪酬2.2 begin
-- added by wh :薪酬工资条发放消息
SET @template_id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@template_id := @template_id + 1, 'salary.notification', 1, 'zh_CN', '后台发工资条', '${salaryDate} 工资已发放。', 0);

-- added by wh 薪酬的模块对应action_type 改为74(工资条)
UPDATE eh_service_modules SET action_type = 74 WHERE id = 51400;
UPDATE eh_service_module_apps SET action_type = 74 WHERE module_id = 51400;
-- 薪酬2.2 end


-- 客户管理第三方数据跟进人  by jiarui
UPDATE  eh_enterprise_customers set tracking_uid = NULL  WHERE  tracking_uid = -1;
update eh_enterprise_customer_admins  set namespace_id  = (select namespace_id from eh_enterprise_customers where id = customer_id);

update eh_enterprise_customer_admins t2  set contact_type  = (select target_type from eh_organization_members t1 where t1.contact_token = t2.contact_token and t1.namespace_id = t2.namespace_id limit 1);




-- 模块Id 41900
-- 新增模块 eh_service_modules
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `update_time`, `operator_uid`, `creator_uid`, `multiple_flag`, `module_control_type`) VALUES ('41900', '政务服务', '40000', '/40000/41900', '1', '2', '2', '0', UTC_TIMESTAMP(), UTC_TIMESTAMP(), '0', '0', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41910', '政策管理', '41900', '/40000/41900/41910', '1', '3', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41920', '查询记录', '41900', '/40000/41900/41920', '1', '3', '2', '0', UTC_TIMESTAMP(), '', NULL, UTC_TIMESTAMP(), '0', '0', '', '0', 'community_control');
-- 新增模块菜单 eh_web_menus
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `data_type`, `leaf_flag`, `path`, `sort_num`, `module_id`, `level`, `condition_type`, `category`) VALUES ('16032200', '政务服务', '16030000', 'policy-service', '1', '/16000000/16030000/16032200', '22', '41900', '3', 'system', 'module');
-- 权限
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) VALUES ('4190041910', '0', '政策管理', '政策管理 全部权限');
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`) VALUES ('4190041920', '0', '查询记录', '查询记录 全部权限');
-- 权限中间表 设置Id
SET @pri_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@pri_id := @pri_id + 1, '41910', '0', '4190041910', '全部权限', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@pri_id := @pri_id + 1, '41920', '0', '4190041920', '全部权限', '0', UTC_TIMESTAMP());

-- 合同的开始结束时间改为，开始在00：00：00， 结束在23：59：59 by wentian
update eh_contracts set contract_start_date = date_format(contract_start_date,'%Y-%m-%d 00:00:00');
update eh_contracts set contract_end_date = date_format(contract_end_date,'%Y-%m-%d 23:59:59');

-- ------------------------------
-- 服务联盟V3.3（新增需求提单功能）     
-- 产品功能 #26469 add by huangmingbo  2018/05/29
-- ------------------------------
-- 添加错误信息提示
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11000', 'zh_CN', '新事件申请用户不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11001', 'zh_CN', '未找到工作流信息');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11002', 'zh_CN', '服务商功能并未开启');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11003', 'zh_CN', '该服务商不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11004', 'zh_CN', '上传的文件地址为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11005', 'zh_CN', '邮件附件获取失败');

-- 删除样式管理，模块，权限，模块权限表，更新module的顺序  #26469 add by huangmingbo  2018/05/29
delete from eh_service_modules where parent_id = 40500 and id = 40510;
update eh_service_modules m set m.default_order = 0 where m.parent_id = 40500 and m.id = 40520;
update eh_service_modules m set m.default_order = 1 where m.parent_id = 40500 and m.id = 40540;
update eh_service_modules m set m.default_order = 2 where m.parent_id = 40500 and m.id = 40530;
delete from eh_service_module_privileges where module_id = 40510;
delete from  eh_acl_privileges  where id =  4050040510;

SET @ns_id = 999954;
SET @module_id = 40500;

-- 将服务联盟都变成community_control  #26469 add by huangmingbo  2018/05/29
UPDATE `eh_service_modules` SET `module_control_type`='community_control' WHERE  `id`=@module_id;
update eh_service_module_apps set  module_control_type='community_control' where module_id = @module_id and  module_control_type = 'unlimit_control';

-- 新建事件  #26469 add by huangmingbo  2018/05/29
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', 'flow_button', '新建事件', 'new event', '{"nodeType":"NEW_EVENT"}', 2, NULL, NULL, NULL, NULL);  

-- “停车缴费”、“物业报修”、“物品放行”、“园区入驻”  #26469 add by huangmingbo  2018/05/29
SET @jump_id = IFNULL((SELECT MAX(id) FROM `eh_service_alliance_jump_module`), 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '停车缴费', 40800, 'zl://parking/query?displayName=停车缴费', NULL, 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '物业报修', 20100, NULL, '{"taskCategoryId":6,"prefix":"/property-repair-web/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '物品放行', 49200, NULL, '{"prefix":"/goods-move/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '园区入驻', 40100, NULL, '{"skipRoute":"zl://park-service/settle"}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '投诉建议', 20100, NULL, '{"taskCategoryId":9,"prefix":"/property-repair-web/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);



-- 合同动态表单修改  by jiarui
UPDATE  eh_var_field_groups set title = '收款合同' WHERE  module_name ='contract' and title ='合同信息' AND  parent_id =0;
UPDATE `eh_var_fields`  SET `field_param`='{\"fieldParamType\": \"text\", \"length\": 32}' WHERE `module_name`='contract' AND `name`='contractType';
UPDATE `eh_var_field_scopes`  SET `field_param`='{\"fieldParamType\": \"text\", \"length\": 32}' WHERE  field_id IN (SELECT id FROM eh_var_fields   WHERE `module_name`='contract' AND `name`='contractType');
UPDATE `eh_var_fields`  SET `field_param`='{\"fieldParamType\": \"text\", \"length\": 32}' WHERE `module_name`='contract' AND `name`='status';
UPDATE `eh_var_field_scopes`  SET `field_param`='{\"fieldParamType\": \"text\", \"length\": 32}' WHERE  field_id IN (SELECT id FROM eh_var_fields   WHERE `module_name`='contract' AND `name`='status');
-- 表单select 自定义
UPDATE  eh_var_fields  SET field_param = REPLACE(field_param,'select','customizationSelect') WHERE module_name = 'enterprise_customer' AND `name` <> 'levelItemId';
UPDATE  eh_var_fields  SET field_param = REPLACE(field_param,'select','customizationSelect') WHERE module_name = 'contract' AND `name` NOT  IN ('contractType','status');

UPDATE  eh_var_field_scopes SET field_param = (SELECT field_param FROM eh_var_fields WHERE id = field_id);
-- 表单default  by jiarui

SET  @id = (SELECT MAX(id) FROM  eh_var_field_group_scopes);
INSERT INTO `eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '1', '客户信息', '1', '2', '1', NOW(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '基本信息', '1', '2', '1', NOW(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '企业情况', '3', '2', '1',NOW(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_group_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '12', '员工情况', '4', '2', '1', NOW(), NULL, NULL, NULL, NULL);

SET  @id = (SELECT MAX(id) FROM  eh_var_field_scopes);
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '2', '{\"fieldParamType\": \"text\", \"length\": 32}', '客户名称', '1', '1', '2', '1', now(), '1', NULL , NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '3', '{\"fieldParamType\": \"text\", \"length\": 32}', '简称', '0', '3', '2', '1', now(), NULL, NULL, NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '4', '{\"fieldParamType\": \"select\", \"length\": 32}', '客户类型', '0', '3', '2', '1', now(), NULL, NULL, NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '5', '{\"fieldParamType\": \"select\", \"length\": 32}', '客户级别', '1', '4', '2', '1', now(), '1', NULL , NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '7', '{\"fieldParamType\": \"text\", \"length\": 32}', '联系人', '1', '5', '2', '1', now(), '1', NULL , NULL, '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '10', '{\"fieldParamType\": \"text\", \"length\": 32}', '联系电话', '1', '6', '2', '1',now(), '1', NULL , NULL , '/1/10/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '10', '211', '{\"fieldParamType\": \"text\", \"length\": 32}', '跟进人', '0', '8', '2', '1', now(), '1', NULL , NULL, '/1/10/');

INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '15', '{\"fieldParamType\": \"text\", \"length\": 32}', '地址', '1', '8', '2', '1', now(), NULL, NULL, NULL, '/1/11/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '17', '{\"fieldParamType\": \"text\", \"length\": 32}', '企业网址', '0', '11', '2', '1', now(), NULL, NULL, NULL, '/1/11/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '24', '{\"fieldParamType\": \"select\", \"length\": 32}', '行业类型', '0', '13', '2', '1',now(), NULL, NULL, NULL, '/1/11/');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '36', '{\"fieldParamType\": \"image\", \"length\": 1}', '企业LOGO', '0', '46', '2', '1', now(), '1', NULL , NULL , '/1/11//');
INSERT INTO `eh_var_field_scopes` (`id`, `namespace_id`, `module_name`, `group_id`, `field_id`, `field_param`, `field_display_name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_path`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '11', '37', '{\"fieldParamType\": \"multiText\", \"length\": 2048}', '企业简介', '0', '26', '2', '1', now(), '1', NULL , NULL , '/1/11/');

SET  @id = (SELECT MAX(id) FROM  eh_var_field_item_scopes);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '5', '3', '普通客户', '3', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '5', '4', '重要客户', '4', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '5', '5', '意向客户', '5', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '5', '6', '已成交客户', '6', '2', '1',now(), NULL, NULL, NULL, NULL);

INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '202', '集成电路', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '203', '软件', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '204', '通信技术', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '205', '生物医药', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '206', '医疗器械', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '207', '光机电', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '208', '金融服务', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '209', '新能源与环保', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '210', '文化创意', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '211', '商业-餐饮', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '212', '商业-超市', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '213', '商业-食堂', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '214', '商业-其他', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '24', '215', '其他', '1', '2', '1', now(), NULL, NULL, NULL, NULL);


INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '197', '业主', '1', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '198', '办公', '2', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '199', '商业', '3', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '200', '孵化器', '4', '2', '1', now(), NULL, NULL, NULL, NULL);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`) VALUES ((@id:=@id+1), '0', 'enterprise_customer', '4', '201', '物业公司', '5', '2', '1', now(), NULL, NULL, NULL, NULL);

-- 能耗更新离线包版本   by jiarui
update eh_version_urls set download_url = replace(download_url,'1-0-1','1-0-3'),
  info_url = replace(info_url,'1-0-1','1-0-3'),
  target_version = '1.0.3' where realm_id = (select id from eh_version_realm where realm = 'energyManagement');

-- 合同管理2.7， by dingjianmin
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_service_module_exclude_functions`),0);
-- 同步合同 98
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999953', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999952', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999959', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999948', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '2', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '11', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999956', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999949', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999950', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999945', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999943', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999946', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999963', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999951', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999941', NULL, '21200', '98');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999942', NULL, '21200', '98');
-- 同步客户 99                                                                                                                            
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999953', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999952', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999959', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999948', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '2', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '11', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999956', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999949', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999950', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999945', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999943', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999946', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999963', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999951', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999941', NULL, '21200', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999942', NULL, '21200', '99');
-- 企业客户管理，去掉相应的权限（新增，导入客户，下载模板）
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999983', NULL, '21100', '21101');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999983', NULL, '21100', '21103');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1000000', NULL, '21100', '21101');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1000000', NULL, '21100', '21103');
-- 企业客户管理，去掉相应的权限（同步客户99）
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999953', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999952', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999959', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999948', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '2', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '11', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999956', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999949', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999950', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999945', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999943', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999946', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999963', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999951', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999941', NULL, '21100', '99');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999942', NULL, '21100', '99');

-- 合同基础参数配置 工作流配置 权限 by dingjianmin
SELECT id from eh_service_modules WHERE path IN ('/110000/21200/21230','/110000/21200/21220');
SELECT id from EH_SERVICE_MODULE_PRIVILEGES  WHERE module_id='21210' AND privilege_id=21213 AND privilege_type=0;
-- 对接第三方 权限
SELECT id from EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS  WHERE namespace_id IN(999971,1000000) AND module_id IN (21100,21200)AND function_id IN(98,99);
-- 免租期字段删除 权限
SELECT id from  EH_VAR_FIELDS WHERE module_name='contract' AND group_id=15 AND group_path='/13/15/' AND `name`='freeDays';
-- 客户管理 同步客户权限
SELECT id from  EH_SERVICE_MODULE_PRIVILEGES WHERE module_id=21110 and privilege_id=21104;

-- 合同基础参数配置 工作流配置 权限 by dingjianmin
DELETE FROM EH_SERVICE_MODULES WHERE id IN(SELECT id FROM (SELECT id from eh_service_modules WHERE path IN ('/110000/21200/21230','/110000/21200/21220')) sm);
DELETE FROM EH_SERVICE_MODULE_PRIVILEGES WHERE id IN(SELECT id FROM (SELECT id from EH_SERVICE_MODULE_PRIVILEGES  WHERE module_id='21210' AND privilege_id=21213 AND privilege_type=0) smp);
-- 对接第三方 权限
DELETE FROM EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS WHERE id IN(SELECT id FROM (SELECT id from EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS  WHERE namespace_id IN(999971,1000000) AND module_id IN (21100,21200)AND function_id IN(98,99))smef);
-- 免租期字段删除 权限
DELETE FROM EH_VAR_FIELDS WHERE id IN(SELECT id FROM (SELECT id from  EH_VAR_FIELDS WHERE module_name='contract' AND group_id=15 AND group_path='/13/15/' AND `name`='freeDays')vf);
-- 客户管理 同步客户权限
DELETE FROM EH_SERVICE_MODULE_PRIVILEGES WHERE id IN(SELECT id FROM (SELECT id from  EH_SERVICE_MODULE_PRIVILEGES WHERE module_id=21110 and privilege_id=21104)smp);

-- 更新合同列表为收款合同
UPDATE EH_SERVICE_MODULES SET `name`='收款合同' WHERE `level`=3 and parent_id=21200 and path='/110000/21200/21210';

-- 更新合同列表为付款合同 下的相关权限 by dingjianmin
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '签约、修改' ,default_order=1 WHERE module_id = 21210 AND privilege_id = 21201;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '发起审批' ,default_order=2 WHERE module_id = 21210 AND privilege_id = 21202;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '删除' ,default_order=3 WHERE module_id = 21210 AND privilege_id = 21204;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '作废' ,default_order=4 WHERE module_id = 21210 AND privilege_id = 21205;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '入场' ,default_order=5 WHERE module_id = 21210 AND privilege_id = 21206;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '查看' ,default_order=0 WHERE module_id = 21210 AND privilege_id = 21207;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '续约' ,default_order=6 WHERE module_id = 21210 AND privilege_id = 21208;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '变更' ,default_order=7 WHERE module_id = 21210 AND privilege_id = 21209;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '退约' ,default_order=8 WHERE module_id = 21210 AND privilege_id = 21214;
-- 更新付款合同下的相关权限显示
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '新增' ,default_order=5 WHERE module_id = 21215 AND privilege_id = 21215;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '签约、发起审批',default_order=1 WHERE module_id = 21215 AND privilege_id = 21216;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '修改' ,default_order=2 WHERE module_id = 21215 AND privilege_id = 21217;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '删除' ,default_order=3 WHERE module_id = 21215 AND privilege_id = 21218;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '作废' ,default_order=4 WHERE module_id = 21215 AND privilege_id = 21219;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '查看' ,default_order=0 WHERE module_id = 21215 AND privilege_id = 21220;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '续约' ,default_order=6 WHERE module_id = 21215 AND privilege_id = 21221;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '变更' ,default_order=7 WHERE module_id = 21215 AND privilege_id = 21222;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '退约' ,default_order=8 WHERE module_id = 21215 AND privilege_id = 21223;

-- issue-30235 新增模块 "我的钥匙" by liuyilin 20180524
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `instance_config`, `action_type`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('42000', '我的钥匙', '40000', '/10000/42000', '1', '2', '2', '10', '{\"isSupportQR\":1,\"isSupportSmart\":0}', 76, '0', '0', '0', '0', 'community_control');

-- issue-30573 路福门禁切换成zuolin_v2 by liuyilin 20180521
SET @var_id = (SELECT MAX(`id`) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@var_id:=@var_id+1, 'aclink.qr_driver_zuolin_inner', 'zuolin_v2', 'use version2 of zuolin driver', '999963', NULL);

-- 动态表单数据不一致修复  by jiarui 20180531
update  eh_var_fields set mandatory_flag =1 where id = 10;

update eh_var_field_scopes set field_param = (SELECT t1.field_param from eh_var_fields t1 where id = field_id);

-- for 鼎峰汇APP初始化账单组缴费方式 by 杨崇鑫  start
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) 
VALUES ( 'asset.dingfenghui.appshowpay', '2', '2代表APP展示为全部缴费', '999951');
-- for 鼎峰汇APP初始化账单组缴费方式 by 杨崇鑫  end

-- script by Steve Tang
-- 添加 上传了缴费凭证的账单tab 功能,function_id为100
INSERT INTO eh_service_module_functions (id,module_id,privilege_id,`explain`) VALUES (100,20400,0,'上传了缴费凭证的账单tab');

-- 将 “修改缴费状态” 改成 “修改缴费状态与审核”
UPDATE `eh_acl_privileges` SET `name`='修改缴费状态与审核', `description`='账单管理 修改缴费状态与审核' WHERE (`id`='204001004');
UPDATE `eh_service_module_privileges` SET `remark`='账单管理 修改缴费状态与审核' WHERE (`id`='220');

-- 设置eh_service_module_exclude_functions表的主键id
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_service_module_exclude_functions`),0);

-- 设置 上传了缴费凭证的账单tab 对应的黑名单（除了天企汇（中天）这个域空间，其他域空间全部加入该tab的黑名单）
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999973', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999955', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999986', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999995', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999998', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999953', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999981', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999970', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999990', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999980', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999972', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999965', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999964', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999958', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999952', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999985', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999976', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999959', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999974', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999968', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999948', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999967', NULL, '20400', '100');
-- 这一句是 屏蔽 上传了缴费凭证的账单tab 在 天企汇（中天） 中显示的sql，因为测试不在 天企汇（中天） 中屏蔽 上传了缴费凭证的账单tab
-- INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999944', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999991', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '2', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999997', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '11', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1000001', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999978', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999971', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999956', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999949', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999954', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999969', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999961', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999947', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999950', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999945', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999957', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999993', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999992', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999987', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '1000000', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999982', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999943', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999988', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999962', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999983', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999977', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999946', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999975', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999941', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999999', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999942', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999963', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999996', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999989', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999951', NULL, '20400', '100');
INSERT INTO `eh_service_module_exclude_functions` (`id`, `namespace_id`, `community_id`, `module_id`, `function_id`) VALUES (@id:=@id+1, '999994', NULL, '20400', '100');

set @id = ifnull((select max(`id`) from eh_payment_app_views), 0);
INSERT INTO `eh_payment_app_views` (`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) VALUES (@id:=@id+1, '999944', NULL, '1', 'CERTIFICATE', NULL, NULL, NULL, NULL, NULL, NULL);
-- end of script
