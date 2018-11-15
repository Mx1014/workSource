-- AUTHOR: 黄明波
-- REMARK: issue-41586
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `user_notify_flag` TINYINT(4) NULL DEFAULT '0' COMMENT '0-未起定时器通知用户 1-已起定时器通知用户';
