-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR: 杨崇鑫 20181108 
-- REMARK: 物业缴费V7.5（中天-资管与财务EAS系统对接）：查看账单列表（只传租赁账单）
SET @id = (SELECT MAX(id) FROM eh_asset_vendor);
INSERT INTO `eh_asset_vendor`(`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) 
	VALUES (@id:=@id+1, 'community', 240111044332061061, '物业缴费V7.5（中天-资管与财务EAS系统对接）', 'ZHONGTIAN', 2, 999944);



-- --------------------- SECTION END ---------------------------------------------------------
