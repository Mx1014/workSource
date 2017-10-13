
-- 增加结束时间提醒 by st.zheng
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `reminder_end_time` DATETIME NULL DEFAULT NULL AFTER `reminder_time`;
