-- AUTHOR: 杨崇鑫 2018-12-05
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_sign` TINYINT COMMENT '一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输（0：不传输）';

-- AUTHOR: 杨崇鑫 2018-12-05
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 增加一个支付状态是否已确认字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `confirm_flag` TINYINT COMMENT '支付状态是否已确认字段，1：已确认；0：待确认';

-- AUTHOR: mengqianxiang
-- REMARK: 增加eh_payment_exemption_items表的状态字段
ALTER TABLE eh_payment_exemption_items ADD `merchant_order_id` BIGINT  COMMENT "账单明细ID";
ALTER TABLE eh_payment_exemption_items ADD `delete_flag` TINYINT DEFAULT 1 COMMENT "删除状态：0：已删除；1：正常使用";