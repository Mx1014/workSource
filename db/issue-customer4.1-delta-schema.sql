-- AUTHOR: 杨崇鑫
-- REMARK: 瑞安CM对接 为每个域空间初始化一个默认账单组，因此加上一个标识是否是默认账单组的字段
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `is_default` TINYINT DEFAULT 0 COMMENT '标识是否是默认账单组的字段：1：默认；0：非默认';
-- REMARK: 瑞安CM对接 账单、费项表增加是否是只读字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `is_readonly` TINYINT DEFAULT 0 COMMENT '只读状态：0：非只读；1：只读';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `is_readonly` TINYINT DEFAULT 0 COMMENT '只读状态：0：非只读；1：只读';
-- AUTHOR: djm
alter table eh_contracts modify column sponsor_uid varchar(50);