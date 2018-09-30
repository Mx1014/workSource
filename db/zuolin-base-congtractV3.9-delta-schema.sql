-- AUTHOR: djm
-- REMARK: 合同添加押金状态字段
ALTER TABLE eh_contracts ADD COLUMN `deposit_status`  tinyint(4) NULL COMMENT '押金状态, 0-未缴, 2-已缴' AFTER deposit;
-- END