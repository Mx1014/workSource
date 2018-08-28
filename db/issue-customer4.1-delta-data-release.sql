-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruiancm
-- DESCRIPTION: 此SECTION只在瑞安新天地-999929执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 配置客户V4.1瑞安CM对接的访问地址
SET @id = ifnull((SELECT MAX(id) FROM `eh_configurations`),0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES (@id := @id + 1, 'RuiAnCM.sync.url', 'http://10.50.12.39/cm/WebService/OfficeApp-CM/OfficeApp_CMService.asmx', '瑞安新天地对接的第三方地址', 0, NULL, 1);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES (@id:=@id+1, 'contractService', '999929', NULL, 999929, NULL, 1);
	
SET @id = ifnull((SELECT MAX(id) FROM `eh_asset_vendor`),0);
INSERT INTO `eh_asset_vendor`(`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) 
	VALUES (@id:=@id+1, 'community', 999929, '瑞安CM对接', 'RUIANCM', 2, 999929);	
	
-- AUTHOR: 杨崇鑫
-- REMARK: 初始化瑞安CM对接的默认账单组
set @id = IFNULL((SELECT MAX(`id`) FROM `eh_payment_bill_groups`),0);
INSERT INTO `eh_payment_bill_groups`(`id`, `namespace_id`, `owner_id`, `owner_type`, `name`, `balance_date_type`, `bills_day`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`, `due_day`, `due_day_type`, `brother_group_id`, `bills_day_type`, `category_id`, `biz_payee_type`, `biz_payee_id`) 
VALUES (@id:=@id+1, 999929, 240111044332063578, 'community', '默认账单组', 2, 5, 67663, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 1, 5, 1, NULL, 4, 3, NULL, NULL);
	
	
	
-- --------------------- SECTION END ---------------------------------------------------------