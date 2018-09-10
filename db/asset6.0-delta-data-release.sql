-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 账单区分数据来源
-- REMARK: 如果合同ID或合同编号为空，那么账单来源于缴费新增（默认是缴费新增，因为没有办法区分是新增还是导入）
update eh_payment_bills set source_type='asset' where contract_id is null or contract_num is null;
update eh_payment_bills set source_id='1' where contract_id is null or contract_num is null;
update eh_payment_bills set source_name='缴费新增' where contract_id is null or contract_num is null;
-- REMARK: 如果合同ID和合同编号都不为空，并且如果根据合同ID能够在eh_energy_meter_tasks表找到数据，说明是来源于能耗
update eh_payment_bills set source_type='energy' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);
update eh_payment_bills set source_name='来源能耗' where contract_id in (select id from eh_energy_meter_tasks) and (contract_id is not null and contract_num is not null);
-- REMARK: 如果合同ID和合同编号都不为空，并且如果根据合同ID能够在eh_contracts表找到数据，说明是来源于合同
update eh_payment_bills set source_type='contract' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);
update eh_payment_bills set source_name='来源合同' where contract_id in (select id from eh_contracts) and (contract_id is not null and contract_num is not null);

-- REMARK:物业缴费V6.0 账单区分数据来源
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10001', 'zh_CN', '缴费新增');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10002', 'zh_CN', '缴费导入');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10003', 'zh_CN', '来源合同');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'asset.source', '10004', 'zh_CN', '来源能耗');
	
	