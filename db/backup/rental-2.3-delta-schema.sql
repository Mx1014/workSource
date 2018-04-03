
-- added by wh 2016-11-24 增加资源编号关联性
 
ALTER TABLE `eh_rentalv2_resource_numbers` ADD COLUMN `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源';
ALTER TABLE `eh_rentalv2_resource_numbers` ADD COLUMN `group_lock_flag` TINYINT COMMENT '一个资源被预约是否锁整个group,0-否,1-是';


ALTER TABLE `eh_rentalv2_cells` ADD COLUMN `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源';
ALTER TABLE `eh_rentalv2_cells` ADD COLUMN `group_lock_flag` TINYINT COMMENT '一个资源被预约是否锁整个group,0-否,1-是';

ALTER TABLE `eh_rentalv2_orders` ADD COLUMN `reminder_time` DATETIME     COMMENT '消息提醒时间';

ALTER TABLE `eh_rentalv2_resource_orders` ADD COLUMN `status` TINYINT  DEFAULT 0 COMMENT '状态 0-普通预定订单 1-不显示给用户的';
