-- 上线完成后请调用以下两个接口做停车缴费收款方数据迁移
-- /parking/initPayeeAccount attachment是文档 db/xls/core_server收款帐号.xlsx 中的文档，接口中有逻辑判断，不会导入所有账号，只会停车导入使用的账号
-- /parking/rechargeOrderMigration 迁移支付系统订单号到停车订单表

-- 上线完成后请调用以下接口做物业缴费以前订单支付方式的数据迁移 by 杨崇鑫
-- /asset/transferOrderPaymentType