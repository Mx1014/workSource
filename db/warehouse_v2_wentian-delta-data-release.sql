-- initially added four kinds of requisiton types to schema eh_requistion_types
INSERT INTO `ehcore`.`eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (1, '0', 'EhNamespaces', '0', '采购申请', NOW(), '0', NULL, NULL, 1);
set @type_id = (SELECT max(`id`) FROM `eh_requisition_types`);
set @order_id = (SELECT max(`default_order`) FROM `eh_requisition_types`);
INSERT INTO `ehcore`.`eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '领用申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);
INSERT INTO `ehcore`.`eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '付款申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);
INSERT INTO `ehcore`.`eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '合同申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);


