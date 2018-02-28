-- initially added four kinds of requisiton types to schema eh_requistion_types
INSERT INTO `eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (1, '0', 'EhNamespaces', '0', '采购申请', NOW(), '0', NULL, NULL, 1);
set @type_id = (SELECT max(`id`) FROM `eh_requisition_types`);
set @order_id = (SELECT max(`default_order`) FROM `eh_requisition_types`);
INSERT INTO `eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '领用申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);
INSERT INTO `eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '付款申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);
INSERT INTO `eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '合同申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);

-- error codes added by wentian
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'requisition', '1001', 'zh_CN', '未找到请示单工作流');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'purchase', '1001', 'zh_CN', '该采购单未完成或者已取消,不能进行入库操作');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'purchase', '1002', 'zh_CN', '未找到用户的采购模块工作流');

-- 新模块
INSERT INTO `eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`)
VALUES
(25000, '请示单管理', '20000', '/20000/25000', '1', '3', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control');
INSERT INTO `eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`)
VALUES
(26000, '采购管理', '20000', '/20000/26000', '1', '3', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control');
INSERT INTO `eh_service_modules`
(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`)
VALUES
(27000, '供应商管理', '20000', '/20000/27000', '1', '3', '2', '0', NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', 'community_control');




