-- AUTHOR: 孟千翔
-- REMARK:减免金额表添加bill_item_id和delete_flag字段
ALTER TABLE eh_payment_exemption_items ADD `bill_item_id` BIGINT COMMENT "账单明细ID"
ALTER TABLE eh_payment_exemption_items ADD `delete_flag` TINYINT COMMENT "删除状态：0：已删除；1：正常使用"