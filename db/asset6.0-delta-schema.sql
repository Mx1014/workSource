-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 收费项配置可手动新增
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `namespace_id` INTEGER COMMENT '增加域空间ID作为标识';
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `owner_id` BIGINT COMMENT '增加园区ID作为标识';
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `owner_type` VARCHAR(64)  COMMENT '增加园区ID作为标识';
ALTER TABLE `eh_payment_charging_items` ADD COLUMN `category_id` BIGINT COMMENT '多入口应用id';
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.0 账单、费项增加是否可以删除、是否可以编辑状态字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `can_delete` TINYINT DEFAULT 0 COMMENT '0：不可删除；1：可删除';
ALTER TABLE `eh_payment_bills` ADD COLUMN `can_modify` TINYINT DEFAULT 0 COMMENT '0：不可编辑；1：可编辑';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `can_delete` TINYINT DEFAULT 0 COMMENT '0：不可删除；1：可删除';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `can_modify` TINYINT DEFAULT 0 COMMENT '0：不可编辑；1：可编辑';



