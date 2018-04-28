-- 停车订单编号修改 by dengs,2018.04.27
ALTER TABLE `eh_parking_lots` ADD COLUMN `order_tag` VARCHAR(3) NOT NULL COMMENT '停车场订单生成标识，固定3位';
ALTER TABLE `eh_parking_lots` ADD COLUMN `order_code` BIGINT NOT NULL DEFAULT 0 COMMENT '停车场订单生成码,从0开始，最多8位';

-- 增加发票 by wentian
ALTER TABLE `eh_payment_bills` ADD COLUMN `invoice_number` VARCHAR(128) COMMENT '发票编号';
