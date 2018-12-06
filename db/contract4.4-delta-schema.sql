
-- AUTHOR: djm 20181206
-- REMARK: 合同套打开关
ALTER TABLE eh_contract_categories ADD COLUMN print_switch_status tinyint(4) NOT NULL DEFAULT 0 COMMENT '合同套打(0 关闭，任何合同状态都能打印 1 打开 只有审批通过，正常合同、即将到期合同才能进行此操作)打开关';
ALTER TABLE eh_contract_categories ADD COLUMN editor_switch_status tinyint(4) NOT NULL DEFAULT 0 COMMENT '合同文档编辑开关';
