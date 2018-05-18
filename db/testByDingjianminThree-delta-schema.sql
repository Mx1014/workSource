-- --合同管理 添加合同的收付款规则用
ALTER TABLE `eh_contract_params` ADD COLUMN `payorreceive_contract_type` tinyint(2) NULL COMMENT '0 收款合同 1付款合同';