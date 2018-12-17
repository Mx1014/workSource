-- AUTHOR: 孟千翔
-- REMARK: eh_payment_bill_groups表添加字段，作用自定义账单周期
ALTER TABLE eh_payment_bill_groups ADD `billing_cycle_expression` VARCHAR(100) COMMENT "账单周期表达式";
ALTER TABLE eh_payment_bill_groups ADD `bills_day_expression` VARCHAR(100) COMMENT "出账单日表达式";
ALTER TABLE eh_payment_bill_groups ADD `due_day_expression` VARCHAR(100) COMMENT "最晚还款日表达式";