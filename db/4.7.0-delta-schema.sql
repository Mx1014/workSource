-- 停车充值 add by sw 20170703
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `parking_time` INT DEFAULT NULL COMMENT 'parking-time';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `error_description` TEXT DEFAULT NULL COMMENT 'error description';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `error_description_json` TEXT DEFAULT NULL COMMENT 'error description';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `refund_time` datetime DEFAULT NULL COMMENT 'refund time';
ALTER TABLE eh_parking_recharge_orders ADD COLUMN `delay_time` INT DEFAULT NULL COMMENT 'delay time';


ALTER TABLE eh_parking_lots ADD COLUMN `contact` VARCHAR(128) DEFAULT NULL COMMENT 'service contact';


ALTER TABLE eh_parking_recharge_orders CHANGE old_expired_time start_period datetime;
ALTER TABLE eh_parking_recharge_orders CHANGE new_expired_time end_period datetime;


-- merge from forum-2.0 by yanjun 20170703
-- 投票增加标签字段  add by yanjun 20170613
ALTER TABLE `eh_polls` ADD COLUMN `tag` VARCHAR(32) NULL;
-- merge from forum-2.0 by yanjun 20170703


-- 资源预约价格规则表，add by tt, 20170613
-- DROP TABLE IF EXISTS `eh_rentalv2_price_rules`;
CREATE TABLE `eh_rentalv2_price_rules` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'default, resource, cell',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'default_rule_id, resource_id, cell_id',  
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天 4按月',
  `workday_price` DECIMAL(10,2) COMMENT '工作日价格',
  `weekend_price` DECIMAL(10,2) COMMENT '周末价格',
  `org_member_workday_price` DECIMAL(10,2) COMMENT '企业内部工作日价格',
  `org_member_weekend_price` DECIMAL(10,2) COMMENT '企业内部节假日价格',
  `approving_user_workday_price` DECIMAL(10,2) COMMENT '外部客户工作日价格',
  `approving_user_weekend_price` DECIMAL(10,2) COMMENT '外部客户节假日价格',
  `discount_type` TINYINT COMMENT '折扣信息：0不打折 1满减优惠 2满天减 3比例折扣',
  `full_price` DECIMAL(10,2) COMMENT '满XX',
  `cut_price` DECIMAL(10,2) COMMENT '减XX元',
  `discount_ratio` DOUBLE COMMENT '折扣比例',
  `cell_begin_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'cells begin id',
  `cell_end_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'cells end id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 非认证用户是否可见，add by tt, 20170623
ALTER TABLE `eh_rentalv2_resource_types` ADD COLUMN `unauth_visible` TINYINT DEFAULT '0';


