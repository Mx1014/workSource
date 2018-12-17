-- AUTHOR: 黄鹏宇 20181217
-- REMARK: 物业缴费V7.4(瑞安项目-资产管理对接CM系统) -41302：增加第三方客户分类
ALTER TABLE eh_enterprise_customers ADD COLUMN namespace_customer_group VARCHAR(128) COMMENT '第三方客户分类' AFTER namespace_customer_type;