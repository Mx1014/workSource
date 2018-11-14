-- AUTHOR: 杨崇鑫 20181114
-- REMARK: 物业缴费V7.5（中天-资管与财务EAS系统对接） ： 不支持同一笔账单即在左邻支付一半，又在EAS支付一半，不允许两边分别支付
ALTER TABLE `eh_payment_bills` ADD COLUMN `third_paid` INT(11) COMMENT '不支持同一笔账单即在左邻支付一半，又在EAS支付一半，不允许两边分别支付，0：没有任何支付，1：已在EAS支付';