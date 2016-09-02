-- merge from community-import-2.0.0-delta-schema.sql 20160826
-- 增加一列存储升级内容
ALTER TABLE `eh_version_urls` ADD COLUMN `upgrade_description` TEXT NULL DEFAULT NULL AFTER `info_url`;

-- 增加app名称和发布时间列
ALTER TABLE `eh_version_urls` ADD COLUMN `app_name` VARCHAR(50) NULL,ADD COLUMN `publish_time` DATETIME NULL;


-- merge from 2.0.0-punch-delta-schema.sql 20160826
ALTER TABLE `eh_punch_statistics` CHANGE `work_count` `work_count` DOUBLE DEFAULT NULL COMMENT '实际上班天数';


-- merge from repair-delta-schema.sql 20160826
-- DROP TABLE IF EXISTS `eh_pm_tasks`;
CREATE TABLE `eh_pm_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `category_id` BIGINT NOT NULL DEFAULT 0,

  `address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'detail address',
  `content` TEXT COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `star` TINYINT COMMENT 'evaluate score',

  `unprocessed_time` DATETIME,
  `processing_time` DATETIME,
  `processed_time` DATETIME,
  `closed_time` DATETIME,

  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_pm_task_statistics`;
CREATE TABLE `eh_pm_task_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `category_id` BIGINT NOT NULL DEFAULT 0,
  `total_count` INT NOT NULL DEFAULT 0,
  `unprocess_count` INT NOT NULL DEFAULT 0,
  `processing_count` INT NOT NULL DEFAULT 0,
  `processed_count` INT NOT NULL DEFAULT 0,
  `close_count` INT NOT NULL DEFAULT 0,

  `star1` INT NOT NULL DEFAULT 0,
  `star2` INT NOT NULL DEFAULT 0,
  `star3` INT NOT NULL DEFAULT 0,
  `star4` INT NOT NULL DEFAULT 0,
  `star5` INT NOT NULL DEFAULT 0,

  `date_str` DATETIME,

  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_pm_task_logs`;
CREATE TABLE `eh_pm_task_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `task_id` BIGINT NOT NULL DEFAULT 0,

  `content` TEXT COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `target_type` VARCHAR(32) COMMENT 'user',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'target user id if target_type is a user',

  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_pm_task_attachments`;
CREATE TABLE `eh_pm_task_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- merge from serviceAlliance3.0-delta-schema.sql 20160826
-- DROP TABLE IF EXISTS `eh_service_alliance_categories`;
CREATE TABLE `eh_service_alliance_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_service_alliances`;
CREATE TABLE `eh_service_alliances` (
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'organization name',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '',
  `type` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id reference to eh_service_alliance_categories',
  `address` VARCHAR(255) NOT NULL DEFAULT '',
  `contact` VARCHAR(64) ,
  `description` text,
  `poster_uri` VARCHAR(128) ,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `default_order` int(11) ,
  `longitude` double ,
  `latitude` double ,
  `geohash` VARCHAR(32) ,
  `discount` BIGINT ,
  `category_id` BIGINT ,
  `contact_name` VARCHAR(128) ,
  `contact_mobile` VARCHAR(128) ,
  `service_type` VARCHAR(128) ,
  `service_url` VARCHAR(128) ,
  `discount_desc` VARCHAR(128) ,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128) DEFAULT NULL,
  `string_tag2` VARCHAR(128) DEFAULT NULL,
  `string_tag3` VARCHAR(128) DEFAULT NULL,
  `string_tag4` VARCHAR(128) DEFAULT NULL,
  `string_tag5` VARCHAR(128) DEFAULT NULL,
  `creator_uid` BIGINT ,
  `create_time` datetime ,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_service_alliance_attachments`;
CREATE TABLE `eh_service_alliance_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id reference to eh_service_alliances',
  `content_type` VARCHAR(32)  COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024)  COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT '0',
  `create_time` datetime ,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- merge from videoconf3.0-delta-schema.sql 20160829
ALTER TABLE `eh_conf_orders` ADD COLUMN `expired_date` DATETIME;




-- 结算表  by sfyan 20160829
-- 订单交易流水表
DROP TABLE IF EXISTS `eh_stat_orders`;
CREATE TABLE `eh_stat_orders` (
  `id` bigint(20) NOT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` int(11) DEFAULT 0,
  `order_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `payer_uid` bigint(20) COMMENT '支付用户编号',
  `ware_json` text  COMMENT '商品',
  `vendor_code` varchar(64) DEFAULT null COMMENT '供应商编号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `order_type` varchar(64) DEFAULT NULL COMMENT '订单类型  transaction refund',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `shop_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1-platform shop,2-self shop',
  `order_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算交易流水表
DROP TABLE IF EXISTS `eh_stat_transactions`;
CREATE TABLE `eh_stat_transactions` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `ware_json` text  COMMENT '商品',
  `vendor_code` varchar(64) DEFAULT null COMMENT '供应商编号',
  `payer_uid` bigint(20) COMMENT '支付用户编号',
  `transaction_no` varchar(100) DEFAULT NULL COMMENT '平台流水号',
  `vendor_transaction_no` varchar(100) DEFAULT NULL COMMENT '第三方支付流水号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付金额',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `paid_account` varchar(100) DEFAULT NULL COMMENT '第三方支付账号 ',
  `paid_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '支付类型 二维码支付 等。。 ',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '结算金额',
  `paid_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付状态',
  `paid_time` DATETIME DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算退款流水表
DROP TABLE IF EXISTS `eh_stat_refunds`;
CREATE TABLE `eh_stat_refunds` (
  `id` bigint(20) NOT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` int(11) DEFAULT 0,
  `refund_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `ware_json` text  COMMENT '商品',
  `payer_uid` bigint(20) COMMENT '支付用户编号',
  `refund_no` varchar(100) DEFAULT NULL COMMENT '平台退款流水号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `vendor_refund_no` varchar(100) DEFAULT NULL COMMENT'第三方退款流水号',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单金额',
  `refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款金额',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `refund_time` DATETIME DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算支付退款详情表
DROP TABLE IF EXISTS `eh_stat_settlements`;
CREATE TABLE `eh_stat_settlements` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单总金额',
  `paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `paid_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '交易总笔数',
  `refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总金额',
  `refund_fee_rate` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款费率',
  `refund_fee_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总手续费',
  `refund_settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '退款总结算金额，退款总金额-退款总手续费',
  `refund_count` bigint(20) NOT NULL DEFAULT 0 COMMENT '退款总笔数',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算統計結果表
DROP TABLE IF EXISTS `eh_stat_service_settlement_results`;
CREATE TABLE `eh_stat_service_settlement_results` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `alipay_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付寶支付金額',
  `alipay_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '支付寶退款金額',
  `wechat_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '微信支付金額',
  `wechat_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '微信退款金額',
  `payment_card_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '一卡通交易金額',
  `payment_card_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '一卡通退款金額',
  `total_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `total_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '交易总金额',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_stat_task_logs`;
CREATE TABLE `eh_stat_task_logs` (
  `id` bigint(20) NOT NULL,
  `task_no` varchar(20) NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '10 同步物业缴费订单到结算订单表 20 同步电商订单订单到结算订单表 30 同步停车充值订单到结算订单表 40 同步一卡通订单到结算订单表 50 同步支付平台交易流水到结算交易流水表 60 同步一卡通交易流水到结算交易流水表 70 同步支付平台退款流水到结算退款流水表 80 同步一卡通退款流水到结算退款流水表 90 生成结算数据 100 生成结算数据结果 110 完成',
  `islock` tinyint(4) DEFAULT '0' COMMENT '0 未锁 1 有锁',
  `update_Time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_stat_service`;
CREATE TABLE `eh_stat_service` (
  `id` bigint(20) NOT NULL,
   `namespace_id` int(11) NOT NULL,
  `owner_type` varchar(64) NOT NULL ,
  `owner_id` bigint(20) DEFAULT NULL,
  `service_type` varchar(64) NOT NULL ,
  `service_name` varchar(64) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0' COMMENT '0 无效 1 正常',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



