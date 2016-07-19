-- 订单交易流水表
CREATE TABLE `eh_stat_orders` (
  `id` bigint(20) NOT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` bigint(20) DEFAULT 0,
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` bigint(20) DEFAULT 0 COMMENT '来源实体店ID',
  `payer_uid` bigint(20) NOT NULL COMMENT '支付用户编号',
  `item_code` varchar(64) DEFAULT null COMMENT '商品编号',
  `vendor_code` varchar(64) DEFAULT null COMMENT '供应商编号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `order_type` varchar(64) DEFAULT NULL COMMENT '订单类型  transaction refund',
  `order_amount` DECIMAL DEFAULT NULL COMMENT '订单金额',
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `order_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算交易流水表
CREATE TABLE `eh_stat_transactions` (
  `id` bigint(20) NOT NULL,
  `namespace_id` bigint(20) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` DATE DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` bigint(20) DEFAULT 0 COMMENT '来源实体店ID',
  `item_code` varchar(64) DEFAULT null COMMENT '商品编号',
  `vendor_code` varchar(64) DEFAULT null COMMENT '供应商编号',
  `payer_uid` bigint(20) NOT NULL COMMENT '支付用户编号',
  `transaction_no` varchar(100) DEFAULT NULL COMMENT '平台流水号',
  `vendor_transaction_no` varchar(100) DEFAULT NULL COMMENT '第三方支付流水号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `order_amount` DECIMAL DEFAULT NULL COMMENT '订单金额',
  `paid_amount` DECIMAL DEFAULT NULL COMMENT '支付金额',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `paid_account` varchar(100) DEFAULT NULL COMMENT '第三方支付账号 ',
  `paid_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '支付类型 二维码支付 等。。 ',
  `fee_rate` DECIMAL DEFAULT NULL COMMENT '交易费率',
  `fee_amount` DECIMAL DEFAULT NULL COMMENT '交易总手续费',
  `settlement_amount` DECIMAL DEFAULT NULL COMMENT '结算金额',
  `paid_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付状态',
  `paid_time` DATETIME DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算退款流水表
CREATE TABLE `eh_stat_refunds` (
  `id` bigint(20) NOT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` bigint(20) DEFAULT 0,
  `refund_date` DATE DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` bigint(20) DEFAULT 0 COMMENT '来源实体店ID',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `payer_uid` bigint(20) NOT NULL COMMENT '支付用户编号',
  `refund_no` varchar(100) DEFAULT NULL COMMENT '平台退款流水号',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  `vendor_refund_no` varchar(100) DEFAULT NULL COMMENT'第三方退款流水号',
  `order_amount` DECIMAL DEFAULT NULL COMMENT '订单金额',
  `refund_amount` DECIMAL DEFAULT NULL COMMENT '退款金额',
  `fee_rate` DECIMAL DEFAULT NULL COMMENT '交易费率',
  `fee_amount` DECIMAL DEFAULT NULL COMMENT '交易总手续费',
  `settlement_amount` DECIMAL DEFAULT NULL COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `refund_time` DATETIME DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算支付退款详情表
CREATE TABLE `eh_stat_settlements` (
  `id` bigint(20) NOT NULL,
  `namespace_id` bigint(20) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` DATE DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` bigint(20) DEFAULT 0 COMMENT '来源实体店ID',
  `paid_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `order_amount` DECIMAL DEFAULT NULL COMMENT '订单总金额',
  `paid_amount` DECIMAL DEFAULT NULL COMMENT '交易总金额',
  `fee_rate` DECIMAL DEFAULT NULL COMMENT '交易费率',
  `fee_amount` DECIMAL DEFAULT NULL COMMENT '交易总手续费',
  `settlement_amount` DECIMAL DEFAULT NULL COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `paid_count` bigint(20) DEFAULT 0 COMMENT '交易总笔数',
  `refund_amount` DECIMAL DEFAULT NULL COMMENT '退款总金额',
  `refund_fee_rate` DECIMAL DEFAULT NULL COMMENT '退款费率',
  `refund_fee_amount` DECIMAL DEFAULT NULL COMMENT '退款总手续费',
  `refund_settlement_amount` DECIMAL DEFAULT NULL COMMENT '退款总结算金额，退款总金额-退款总手续费',
  `refund_count` bigint(20) DEFAULT 0 COMMENT '退款总笔数',
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 结算統計結果表
CREATE TABLE `eh_stat_service_settlement_results` (
  `id` bigint(20) NOT NULL,
  `namespace_id` bigint(20) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `paid_date` DATE DEFAULT NULL COMMENT '处理成日期 比如2016-07-09',
  `service_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` bigint(20) DEFAULT 0 COMMENT '来源实体店ID',
  `alipay_paid_amount` DECIMAL DEFAULT NULL COMMENT '支付寶支付金額',
  `alipay_refund_amount` DECIMAL DEFAULT NULL COMMENT '支付寶退款金額',
  `wechat_paid_amount` DECIMAL DEFAULT NULL COMMENT '微信支付金額',
  `wechat_refund_amount` DECIMAL DEFAULT NULL COMMENT '微信退款金額',
  `payment_card_paid_amount` DECIMAL DEFAULT NULL COMMENT '一卡通交易金額',
  `payment_card_refund_amount` DECIMAL DEFAULT NULL COMMENT '一卡通退款金額',
  `total_paid_amount` DECIMAL DEFAULT NULL COMMENT '交易总金额',
  `total_refund_amount` DECIMAL DEFAULT NULL COMMENT '交易总金额',
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 同步任务日志跟踪表
CREATE TABLE `eh_sync_task_logs` (
  `id` BIGINT NOT NULL,
  `task_type` VARCHAR(64) DEFAULT NULL COMMENT '任务类型  1同步订单 2同步交易 3交易数据处理',
  `request_params` VARCHAR(1024) DEFAULT NULL COMMENT '请求参数',
  `response_datas` text COMMENT '响应结果数据',
  `api_url` VARCHAR(1024) DEFAULT NULL COMMENT '请求url',
  `exception_log` text COMMENT '记录异常',
  `status` TINYINT(4) DEFAULT NULL COMMENT '0：start 1：fail 2：succeed 3：finish',
  `anchor` BIGINT DEFAULT 0 COMMENT '响应的开始锚点， 用于二次数据同步',
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

