-- AUTHOR: 杨崇鑫 2018-12-06
-- REMARK: 瑞安CM对接 :APP端显示的支付状态是“已支付，待确认”。
SET @id = ifnull((SELECT MAX(id) FROM `eh_payment_app_views`),0); 
INSERT INTO `eh_payment_app_views`(`id`, `namespace_id`, `community_id`, `has_view`, `view_item`, `remark1_type`, `remark1_identifier`, `remark2_type`, `remark2_identifier`, `remark3_type`, `remark3_identifier`) 
	VALUES (@id := @id + 1, 999929, NULL, 1, 'CONFIRM', NULL, NULL, NULL, NULL, NULL, NULL);