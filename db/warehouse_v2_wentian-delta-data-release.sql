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

-- 供应商，采购，请示单的规则的细化
-- 采购的权限细化
set @module_id = 26000;
set @p_id = 260001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '查看详情', '查看详情', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
  (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看详情', '0', NOW());

set @module_id = 26000;
set @p_id = 260001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '新增、修改、删除申请', '新增、修改、删除申请', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
  (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增、修改、删除申请', '0', NOW());

set @module_id = 26000;
set @p_id = 260001003;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '入库', '入库', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
  (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '入库', '0', NOW());


-- 供应商的权限细化规则
set @module_id = 27000;
set @p_id = 270001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '查看详情', '查看详情', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
  (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看详情', '0', NOW());

set @module_id = 27000;
set @p_id = 270001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '新增、修改、删除供应商', '新增、修改、删除供应商', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
  (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增、修改、删除供应商', '0', NOW());

-- 请示单管理
set @module_id = 25000;
set @p_id = 250001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '查看详情', '查看详情', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
  (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看详情', '0', NOW());

set @module_id = 25000;
set @p_id = 250001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '新增请示', '新增请示', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
  (@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增请示', '0', NOW());




