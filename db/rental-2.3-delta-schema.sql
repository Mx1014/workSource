
-- added by wh 2016-11-24 增加资源编号关联性
 
ALTER TABLE `eh_rentalv2_resource_numbers` ADD COLUMN `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源';

ALTER TABLE `eh_rentalv2_orders` ADD COLUMN `reminder_time` DATETIME DEFAULT NULL COMMENT '消息提醒时间';

 