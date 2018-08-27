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
	
	
-- --------------------- SECTION END ---------------------------------------------------------