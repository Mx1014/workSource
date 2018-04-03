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

