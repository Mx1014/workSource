ALTER TABLE eh_parking_lots ADD COLUMN `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card';

ALTER TABLE eh_parking_lots ADD COLUMN `tempfee_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is support temp fee';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: monthly, 2: temporary';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `order_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from 3rd system';


-- add by wh 20161011
-- 用户活动率计算表
-- DROP TABLE IF EXISTS `eh_stat_active_users`;
CREATE TABLE `eh_stat_active_users` (
  `id` BIGINT NOT NULL,
  `stat_date` DATE COMMENT '统计日期',
  `namespace_id` INTEGER NOT NULL DEFAULT '0', 
  `active_count` INTEGER NOT NULL DEFAULT '0' COMMENT '活动人数',
  `total_count` INTEGER NOT NULL DEFAULT '0' COMMENT '总人数-当天的', 
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
