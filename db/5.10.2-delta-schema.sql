-- AUTHOR: 杨崇鑫
-- REMARK: 缺陷 #42416 【中天】更新自然季，合同刷新账单报错。
ALTER TABLE `preupdate`.`eh_contracts` MODIFY COLUMN `rent` decimal(20, 2) NULL DEFAULT NULL COMMENT '租金' AFTER `rent_size`;
