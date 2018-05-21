-- for 鼎峰汇APP初始化账单组缴费方式 by 杨崇鑫  start
-- 根据手机号返回房产
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`) 
VALUES ( 'asset.dingfenghui.appshowpay', '2', '2代表APP展示为全部缴费', '999951');

SET @config_id = IFNULL((SELECT MAX(id) FROM `eh_payment_service_configs`),1);
INSERT INTO `eh_payment_service_configs`(`id`, `name`, `order_type`, `namespace_id`, `owner_type`, `owner_id`, `resource_type`, `resource_id`, `payment_split_rule_id`, `payment_user_type`, `payment_user_id`, `create_time`, `update_time`) 
VALUES (@config_id:=@config_id+1, '物业缴费', 'wuyeCode', 999951, 'EhOrganizations', 1023080, NULL, NULL, NULL, 2, 2327, UTC_TIMESTAMP(), NULL);

-- for 鼎峰汇APP初始化账单组缴费方式 by 杨崇鑫  end
