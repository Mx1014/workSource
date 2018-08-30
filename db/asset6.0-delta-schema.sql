-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 收费项配置可手动新增
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `namespace_id` INTEGER COMMENT '增加域空间ID作为标识';



