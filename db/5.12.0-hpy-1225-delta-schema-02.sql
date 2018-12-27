-- AUTHOR: 黄鹏宇 20181227
-- REMARK: 增加最近拜访时间将拜访时间和跟进时间分开
ALTER TABLE eh_enterprise_customers ADD COLUMN `last_visiting_time` DATETIME COMMENT '最近拜访时间';