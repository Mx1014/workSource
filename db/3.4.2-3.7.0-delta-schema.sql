-- merge from 3.4.x.paymentcard-delta-schema.sql 20160628
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
  `merchant_no` VARCHAR(256) COMMENT 'the merchant no',
  `merchant_name` VARCHAR(256) COMMENT 'the merchant name',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid',
  `transaction_no` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'transaction serial number',
  `transaction_time` DATETIME COMMENT 'the pay time',
  `card_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'id of the eh_payment_cards record',
  `card_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'the number of card',
  
  `token` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the token of card token to pay',
  
  `order_no` VARCHAR(512) COMMENT 'order no',
  `consume_Type` TINYINT NOT NULL DEFAULT 1 COMMENT 'the type of merchant',

  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: fail, 1: waitting 2: sucess',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of vendors taotaogu',
  `vendor_result` TEXT COMMENT 'the extra information of transactions',

  PRIMARY KEY (`id`)             
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;



-- merge from 3.4.x.sfyan-delta-schema.sql  20160628
-- add by xiongying
ALTER TABLE `eh_activities` ADD COLUMN media_url VARCHAR(1024);
ALTER TABLE `eh_user_posts` ADD COLUMN target_type VARCHAR(32);
ALTER TABLE `eh_user_posts` CHANGE post_id target_id BIGINT NOT NULL DEFAULT 0;

CREATE TABLE `eh_hot_tags` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'service type, eg: activity',  
  `name` VARCHAR(128) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `create_uid` BIGINT,
  `delete_time` DATETIME,
  `delete_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- modified 20160622
CREATE TABLE `eh_app_urls` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `name` VARCHAR(128) NOT NULL,
  `os_type` TINYINT NOT NULL DEFAULT 0,
  `download_url` VARCHAR(128),
  `logo_url` VARCHAR(128),
  `description` TEXT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- add by yanshaofan
-- user items
-- 
CREATE TABLE `eh_user_launch_pad_items`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `item_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community, organization',
  `owner_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: default, 1: override, 2: revert 3:customized',
  `display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'default display on the pad, 0: hide, 1:display',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',
  `update_time` DATETIME,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 20160627
ALTER TABLE `eh_banners` ADD COLUMN `apply_policy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: default, 1: override, 2: revert 3:customized';
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city, 3: user';
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scope_id` bigint(20) DEFAULT 0;
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `apply_policy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: default, 1: override, 2: revert 3:customized';





-- merge from quality1.1-delta-schema.sql  20160628
-- category表增加description字段记录规范内容；增加分数字段记录规范所占分值。
ALTER TABLE `eh_quality_inspection_categories` ADD COLUMN `score` DOUBLE NOT NULL DEFAULT 0;
ALTER TABLE `eh_quality_inspection_categories` ADD COLUMN `description` TEXT COMMENT 'content data';
 
-- task表新增category_id字段记录任务所属类型；category_path字段记录类型路径；增加字段记录创建任务的人的id，0为根据标准自动创建的任务；增加manual_flag字段标识是否是自动生成的任务
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_categories';
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `category_path` VARCHAR(128) COMMENT 'refernece to the path of eh_categories';
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `create_uid` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE `eh_quality_inspection_tasks` ADD COLUMN `manual_flag` BIGINT NOT NULL DEFAULT 0 COMMENT '0: auto 1:manual';
 
-- 新增表记录修改的记录
CREATE TABLE `eh_quality_inspection_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'standard, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record operator user id',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

