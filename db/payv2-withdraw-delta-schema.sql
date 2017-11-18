DROP TABLE IF EXISTS `eh_payment_withdraw_orders`;
CREATE TABLE `eh_payment_withdraw_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `order_no` VARCHAR(64) COMMENT 'order number',
  `user_name` VARCHAR(128),
  `user_phone` VARCHAR(16),
  `amount` DECIMAL(10,2) COMMENT 'the amount to withdraw',
  `status` TINYINT NOT NULL COMMENT '0-inactive, 1-waiting for confirm, 2-success, 3-failed',
  `callback_time` DATETIME,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;