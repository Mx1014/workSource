-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 账单区分数据来源
-- REMARK: 如果合同ID或合同编号为空，那么账单来源于缴费新增（默认是缴费新增，因为没有办法区分是新增还是导入）
update eh_payment_bills set source_type='asset' where contract_id is null or contract_num is null;
update eh_payment_bills set source_id='1' where contract_id is null or contract_num is null;
update eh_payment_bills set source_name='手动新增' where contract_id is null or contract_num is null;

update eh_payment_bill_items set source_type='asset' where contract_id is null or contract_num is null;
update eh_payment_bill_items set source_id='1' where contract_id is null or contract_num is null;
update eh_payment_bill_items set source_name='手动新增' where contract_id is null or contract_num is null;
-- REMARK: 如果合同ID和合同编号都不为空，并且如果根据合同ID能够在eh_energy_meter_tasks表找到数据，说明是来源于能耗
update eh_payment_bills set source_type='energy' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);
update eh_payment_bills set source_name='能耗产生' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);

update eh_payment_bill_items set source_type='energy' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);
update eh_payment_bill_items set source_name='能耗产生' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);
-- REMARK: 如果合同ID和合同编号都不为空，并且如果根据合同ID能够在eh_contracts表找到数据，说明是来源于合同
update eh_payment_bills set source_type='contract' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);
update eh_payment_bills set source_name='合同产生' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);

update eh_payment_bill_items set source_type='contract' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);
update eh_payment_bill_items set source_name='合同产生' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);

-- REMARK:物业缴费V6.0 账单区分数据来源
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10001', 'zh_CN', '手动新增');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10002', 'zh_CN', '批量导入');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10003', 'zh_CN', '合同产生');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10004', 'zh_CN', '能耗产生');
	
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段，需做历史数据初始化
-- REMARK: 如果来源于缴费新增/导入，支持删除、支持修改
update eh_payment_bills set can_delete = 1 where source_type='asset';
update eh_payment_bills set can_modify = 1 where source_type='asset';
update eh_payment_bill_items set can_delete = 1 where source_type='asset';
update eh_payment_bill_items set can_modify = 1 where source_type='asset';
-- REMARK: 如果来源于能耗，不支持删除、不支持修改
update eh_payment_bills set can_delete = 0 where source_type='energy';
update eh_payment_bills set can_modify = 0 where source_type='energy';
update eh_payment_bill_items set can_delete = 0 where source_type='energy';
update eh_payment_bill_items set can_modify = 0 where source_type='energy';
-- REMARK: 如果来源于合同，支持删除账单/修改账单的减免增收等配置，不支持删除/修改费项
update eh_payment_bills set can_delete = 1 where source_type='contract';
update eh_payment_bills set can_modify = 1 where source_type='contract';
update eh_payment_bill_items set can_delete = 0 where source_type='contract';
update eh_payment_bill_items set can_modify = 0 where source_type='contract';
-- REMARK: 如果是已出已缴账单，则不允许删除/修改
update eh_payment_bills set can_delete = 0 where switch=1 and status=1;
update eh_payment_bills set can_modify = 0 where switch=1 and status=1;
update eh_payment_bill_items set can_delete = 0 where bill_id in (select id from eh_payment_bills where switch=1 and status=1 );
update eh_payment_bill_items set can_modify = 0 where bill_id in (select id from eh_payment_bills where switch=1 and status=1 );
	
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 权限控制
-- REMARK: 物业缴费V6.0 将“ 账单查看、筛选”的权限去掉，因为有此模块权限的用户默认就会有查看和筛选的权限；
delete from eh_acl_privileges where id=204001001;
delete from eh_service_module_privileges where privilege_id=204001001;
-- REMARK: 物业缴费V6.0 将“新增账单”改为“新增账单、批量导入”权限；
update eh_acl_privileges set name='新增账单、批量导入',description='账单管理 新增账单、批量导入' where id=204001002;
update eh_service_module_privileges set remark='新增账单、批量导入' where privilege_id=204001002;
-- REMARK: 物业缴费V6.0 新增以下权限：“账单修改、删除”
SET @p_id = 204001007;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '账单修改、删除', '账单管理 账单修改、删除', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '账单修改、删除', '0', NOW());
-- REMARK: 物业缴费V6.0 新增以下权限：“未出批量转已出”
SET @p_id = 204001008;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '未出批量转已出', '账单管理 未出批量转已出', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '未出批量转已出', '0', NOW());
-- REMARK: 物业缴费V6.0 新增以下权限：“未缴批量转已缴”
SET @p_id = 204001009;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '未缴批量转已缴', '账单管理 未缴批量转已缴', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '未缴批量转已缴', '0', NOW());
-- REMARK: 物业缴费V6.0 新增以下权限：“批量减免”权限；
SET @p_id = 204001010;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '批量减免', '账单管理 批量减免', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '批量减免', '0', NOW());



	
	