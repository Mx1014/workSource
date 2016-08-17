
DROP TABLE IF EXISTS `eh_community_tasks`;
CREATE TABLE `eh_pm_tasks` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`category_id` BIGINT NOT NULL DEFAULT 0,

	`address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'detail address',
	`content` TEXT NOT NULL COMMENT 'content data',
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


 DROP TABLE IF EXISTS `eh_community_task_logs`;
CREATE TABLE `eh_pm_task_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `task_id` BIGINT NOT NULL DEFAULT 0,

  `content` TEXT NOT NULL  COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `task_type` VARCHAR(32) COMMENT 'task type assigned by organization',
  `target_id` BIGINT NOT NULL DEFAULT COMMENT 'target user id if target_type is a user',

  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_time` DATETIME,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

 DROP TABLE IF EXISTS `eh_community_task_attachments`;
CREATE TABLE `eh_pm_task_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- DROP TABLE IF EXISTS `eh_community_services`;
CREATE TABLE `eh_community_services` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` BIGINT,
  `item_name` VARCHAR(32),
  `item_label` VARCHAR(64),
  `icon_uri` VARCHAR(1024),
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_users ADD COLUMN `namespace_user_type` VARCHAR(128) DEFAULT NULL COMMENT 'the type of user';

ALTER TABLE eh_organizations ADD COLUMN `namespace_organization_token` VARCHAR(256) DEFAULT NULL COMMENT 'the token from third party';

ALTER TABLE eh_organizations ADD COLUMN `namespace_organization_type` VARCHAR(128) DEFAULT NULL COMMENT 'the type of organization';


-- 结算表  by sfyan 2016010
-- 订单交易流水表
CREATE TABLE `eh_stat_orders` (
  `id` bigint(20) NOT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` int(11) DEFAULT 0,
  `order_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `payer_uid` bigint(20) COMMENT '支付用户编号',
  `item_code` varchar(64) DEFAULT null COMMENT '商品编号',
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
CREATE TABLE `eh_stat_transactions` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `item_code` varchar(64) DEFAULT null COMMENT '商品编号',
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
CREATE TABLE `eh_stat_refunds` (
  `id` bigint(20) NOT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` int(11) DEFAULT 0,
  `refund_date` varchar(20) DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '来源实体店ID',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
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

DROP TABLE IF EXISTS `eh_journals`;
CREATE TABLE `eh_journals` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0, 

  `title` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'title',
  `content_type` TINYINT NOT NULL DEFAULT 1 COMMENT ' 1:link ',
  `content` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'link',

  `cover_uri` VARCHAR(1024) COMMENT 'cover file uri',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,

  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive,1:active ',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_journal_configs`;
CREATE TABLE `eh_journal_configs` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0, 

  `description` TEXT  COMMENT 'description',
  `poster_path` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT 'poster_path',

  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- 停车充值费率添加卡类型
ALTER TABLE `eh_parking_recharge_rates` ADD COLUMN `card_type` VARCHAR(128);


