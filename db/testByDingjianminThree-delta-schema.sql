-- --合同管理 添加合同的收付款规则用
ALTER TABLE `eh_contract_params` ADD COLUMN `payorreceive_contract_type` tinyint(2) NULL COMMENT '0 收款合同 1付款合同';
ALTER TABLE `eh_contract_params` ADD COLUMN `contract_Number_RuleJson` text NULL COMMENT '合同规则';
ALTER TABLE `eh_contract_params` ADD COLUMN `update_Time` datetime NULL COMMENT '更新时间';