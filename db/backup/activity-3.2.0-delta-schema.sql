
-- 增加活动是否支持微信报名标识  add by yanjun 20170627
ALTER TABLE `eh_roster_order_settings`  ADD COLUMN `wechat_signup` TINYINT(4) DEFAULT '0' NULL COMMENT 'is support wechat signup 0:no, 1:yes';
ALTER TABLE `eh_activities` ADD COLUMN `wechat_signup` TINYINT(4) DEFAULT '0' NULL COMMENT 'is support wechat signup 0:no, 1:yes';

-- 增加订单类型  add by yanjun 20170713
ALTER TABLE `eh_activity_roster` ADD COLUMN `order_type` VARCHAR(128) NULL COMMENT 'orderType';