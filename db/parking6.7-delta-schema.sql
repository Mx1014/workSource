-- AUTHOR: 缪洲
-- REMARK: 停车缴费V6.7，增加用户须知

ALTER TABLE `eh_parking_lots` ADD COLUMN `notice_contact` varchar(20) COMMENT '用户须知联系电话';
ALTER TABLE `eh_parking_lots` ADD COLUMN `summary` text COMMENT '用户须知';

