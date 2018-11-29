-- AUTHOR: 杨崇鑫 20181128
-- REMARK: 缺陷 #42424 【智谷汇】保证金设置为固定金额，但是实际会以合同签约门牌的数量计价。实际上保证金是按照合同收费，不是按照门牌的数量进行重复计费。 给智谷汇的权限
ALTER TABLE `eh_contract_charging_items` ADD COLUMN `one_time_bill_status` TINYINT COMMENT '是否是一次性产生费用，1：是，0：否';

