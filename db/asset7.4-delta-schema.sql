-- AUTHOR: 杨崇鑫 2018-12-05
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) ： 一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_sign` TINYINT COMMENT '一个特殊error标记给左邻系统，左邻系统以此标记判断该条数据下一次同步不再传输（0：不传输）';