-- AUTHOR: 缪洲
-- REMARK: 停车缴费V6.7，增加用户须知
ALTER TABLE `eh_parking_lots` DROP COLUMN `notice_json` varchar(1024) DEFAULT NULL COMMENT '用户须知与联系电话（用json数组的方式存储）';	

ALTER TABLE `eh_parking_lots` ADD COLUMN `summary` text COMMENT '用户须知';

ALTER TABLE `eh_parking_lots` ADD COLUMN `notice_contact` text COMMENT '用户须知联系电话';