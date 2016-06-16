DROP TABLE IF EXISTS `eh_payment_card_issuers`;
CREATE TABLE `eh_payment_card_issuers` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'name',
  `description` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'description',
  `pay_url` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'pay_url',
  `alipay_recharge_account` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'alipay_recharge_account',
  `weixin_recharge_account` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'weixin_recharge_account',

  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of vendors taotaogu',
  `vendor_data` TEXT COMMENT 'the extra information of card for example make_no',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive,1:active ',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_card_issuer_communities`;
CREATE TABLE `eh_payment_card_issuer_communities` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `issuer_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'id of the card issuer',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_cards`;   
CREATE TABLE `eh_payment_cards` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0, 
  `issuer_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'id of the card issuer',

  `user_name` VARCHAR(64) COMMENT 'the name of user',
  `mobile` VARCHAR(64) COMMENT 'the mobile of user',
  `card_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'the id of card ,according the third system',
  `balance` DECIMAL(10,2) COMMENT 'the balance of card',
  `password` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT 'the password of user',
  `user_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `activate_time` DATETIME,
  `expired_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive,1:wating 2: active ',
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of vendors taotaogu',
  `vendor_card_data` TEXT COMMENT 'the extra information of card for example make_no',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_card_recharge_orders`;
CREATE TABLE `eh_payment_card_recharge_orders` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `order_no` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'order no',  
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  
  `user_id` BIGINT NOT NULL DEFAULT 0,  
  `user_name` VARCHAR(64) COMMENT 'the name of user',
  `mobile` VARCHAR(64) COMMENT 'the mobile of user',
  `card_no` VARCHAR(256) NOT NULL COMMENT 'the number of card',
  `card_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'id of the eh_payment_cards record',          
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of order',

  `payer_uid` BIGINT NOT NULL DEFAULT 0,  
  `payer_name` VARCHAR(64) COMMENT 'the name of user',
  `paid_time` DATETIME COMMENT 'the pay time',
  `pay_status` TINYINT NOT NULL DEFAULT 1 COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',

  `recharge_time` DATETIME COMMENT 'recharge time',
  `recharge_status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: fail, 1: unrecharged 2: recharged 3:COMPLETE 4:REFUNDED',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `paid_type` VARCHAR(32) COMMENT 'the type of paid 10001:zhifubao 10002: weixin',


  PRIMARY KEY (`id`)             
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_card_transactions`;
CREATE TABLE `eh_payment_card_transactions` (   
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `payer_uid` BIGINT NOT NULL DEFAULT 0,
  `user_name` VARCHAR(64) COMMENT 'the name of user',
  `mobile` VARCHAR(64) COMMENT 'the mobile of user',
  `item_name` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'the name of item',
  `merchant` VARCHAR(64) COMMENT 'the merchant',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid',
  `transaction_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'transaction serial number',
  `transaction_time` DATETIME COMMENT 'the pay time',
  `transcation_type` TINYINT NOT NULL DEFAULT 1 COMMENT 'the type of recharged card or consume',
  `card_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'id of the eh_payment_cards record',
  `card_no` VARCHAR(256) NOT NULL COMMENT 'the number of card',
  `token` VARCHAR(512) NOT NULL COMMENT 'the token of card token to pay',
  `order_no` BIGINT(30) NOT NULL DEFAULT '0' COMMENT 'order no',

  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: fail, 1: waitting 2: sucess',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of vendors taotaogu',
  `vendor_result` TEXT COMMENT 'the extra information of transactions',

  PRIMARY KEY (`id`)             
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;


