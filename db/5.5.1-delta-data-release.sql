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


-- 添加错误信息提示
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11000', 'zh_CN', '新事件申请用户不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11001', 'zh_CN', '未找到工作流信息');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11002', 'zh_CN', '服务商功能并未开启');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11003', 'zh_CN', '该服务商不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11004', 'zh_CN', '上传的文件地址为空');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('yellowPage', '11005', 'zh_CN', '邮件附件获取失败');

-- 删除样式管理，模块，权限，模块权限表，更新module的顺序
delete from eh_service_modules where parent_id = 40500 and id = 40510;
update eh_service_modules m set m.default_order = 0 where m.parent_id = 40500 and m.id = 40520;
update eh_service_modules m set m.default_order = 1 where m.parent_id = 40500 and m.id = 40540;
update eh_service_modules m set m.default_order = 2 where m.parent_id = 40500 and m.id = 40530;
delete from eh_service_module_privileges where module_id = 40510;
delete from  eh_acl_privileges  where id =  4050040510;

SET @ns_id = 999954;
SET @module_id = 40500;

-- 将服务联盟都变成community_control
UPDATE `eh_service_modules` SET `module_control_type`='community_control' WHERE  `id`=@module_id;
update eh_service_module_apps set  module_control_type='community_control' where module_id = @module_id and  module_control_type = 'unlimit_control';

-- 新建事件
SET @flow_predefined_params_id = IFNULL((SELECT MAX(id) FROM `eh_flow_predefined_params`), 1);
INSERT INTO `eh_flow_predefined_params` (`id`, `namespace_id`, `owner_id`, `owner_type`, `module_id`, `module_type`, `entity_type`, `display_name`, `name`, `text`, `status`, `creator_uid`, `create_time`, `update_uid`, `update_time`)
  VALUES ((@flow_predefined_params_id := @flow_predefined_params_id + 1), @ns_id, 0, '', @module_id, 'any-module', 'flow_node', '新建事件', 'new event', '{"nodeType":"NEW_EVENT"}', 2, NULL, NULL, NULL, NULL);  

-- “停车缴费”、“物业报修”、“物品放行”、“园区入驻”
SET @jump_id = IFNULL((SELECT MAX(id) FROM `eh_service_alliance_jump_module`), 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '停车缴费', 40800, 'zl://parking/query?displayName=停车缴费', NULL, 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '物业报修', 20100, NULL, '{"taskCategoryId":6,"prefix":"/property-repair-web/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '物品放行', 49200, NULL, '{"prefix":"/goods-move/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '园区入驻', 40100, NULL, '{"skipRoute":"zl://park-service/settle"}', 0, 1);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_id`, `module_url`, `instance_config`,`parent_id`, `signal`) VALUES ((@jump_id := @jump_id + 1), @ns_id, '投诉建议', 20100, NULL, '{"taskCategoryId":9,"prefix":"/property-repair-web/build/index.html","skipRoute":"zl://browser/i?url="}', 0, 1);


