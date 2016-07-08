-- 订单交易流水表
CREATE TABLE `eh_order_trade_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT,
  `order_no` varchar(100) DEFAULT NULL,
  `order_amount` double(10,2) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL,
  `resource_type` varchar(64) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT 0,
  `order_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 订单退款流水表
CREATE TABLE `eh_order_refund_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT,
  `refund_order_no` varchar(100) DEFAULT NULL,
  `refund_order_amount` double(10,2) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  `resource_type` varchar(64) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT 0,
  `order_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 结算交易流水表
CREATE TABLE `eh_balance_trade_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT,
  `pay_no` varchar(100) DEFAULT NULL,
  `order_no` varchar(100) DEFAULT NULL,
  `order_amount` double(10,2) DEFAULT NULL,
  `pay_amount` double(10,2) DEFAULT NULL,
  `fee_rate` double(10,2) DEFAULT NULL,
  `fee_amount` double(10,2) DEFAULT NULL,
  `balance_amount` double(10,2) DEFAULT NULL,
  `pay_channel` tinyint(4) DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  `trade_no` varchar(100) DEFAULT NULL,
  `pay_account` varchar(100) DEFAULT NULL,
  `pay_type` tinyint(4) NOT NULL DEFAULT '1',
  `resource_type` varchar(64) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT 0,
  `project_type` varchar(64) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT 0,
  `pay_status` tinyint(4) NOT NULL DEFAULT '0',
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` bigint(20) DEFAULT 0,
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 结算交易流水表
CREATE TABLE `eh_balance_refund_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT,
  `refund_no` varchar(100) DEFAULT NULL,
  `order_no` varchar(100) DEFAULT NULL,
  `order_amount` double(10,2) DEFAULT NULL,
  `refund_amount` double(10,2) DEFAULT NULL,
  `fee_rate` double(10,2) DEFAULT NULL,
  `fee_amount` double(10,2) DEFAULT NULL,
  `balance_amount` double(10,2) DEFAULT NULL,
  `refund_channel` tinyint(4) DEFAULT NULL,
  `refund_date` datetime DEFAULT NULL,
  `trade_no` varchar(100) DEFAULT NULL,
  `resource_type` varchar(64) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT 0,
  `project_type` varchar(64) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT 0,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` bigint(20) DEFAULT 0,
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 结算用户详情表
CREATE TABLE `eh_balance_trade_user_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT 0,
  `order_amount` double(10,2) DEFAULT NULL,
  `pay_amount` double(10,2) DEFAULT NULL,
  `fee_rate` double(10,2) DEFAULT NULL,
  `fee_amount` double(10,2) DEFAULT NULL,
  `balance_amount` double(10,2) DEFAULT NULL,
  `pay_amount` double(10,2) DEFAULT NULL,
  `pay_count` bigint(20) DEFAULT 0,
  `refund_channel` tinyint(4) DEFAULT NULL,
  `resource_type` varchar(64) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT 0,
  `project_type` varchar(64) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` bigint(20) DEFAULT 0,
  `pay_date` date DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 结算支付退款详情表
CREATE TABLE `eh_balance_trade_refund_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_amount` double(10,2) DEFAULT NULL COMMENT '订单总金额',
  `pay_amount` double(10,2) DEFAULT NULL COMMENT '交易总金额',
  `fee_rate` double(10,2) DEFAULT NULL COMMENT '交易费率',
  `fee_amount` double(10,2) DEFAULT NULL COMMENT '交易总手续费',
  `balance_amount` double(10,2) DEFAULT NULL COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `pay_count` bigint(20) DEFAULT 0 COMMENT '交易总笔数',
  `refund_amount` double(10,2) DEFAULT NULL COMMENT '退款总金额',
  `refund_fee_rate` double(10,2) DEFAULT NULL COMMENT '退款费率',
  `refund_fee_amount` double(10,2) DEFAULT NULL COMMENT '退款总手续费',
  `refund_balance_amount` double(10,2) DEFAULT NULL COMMENT '退款总结算金额，退款总金额-退款总手续费',
  `refund_count` bigint(20) DEFAULT 0 COMMENT '退款总笔数',
  `pay_channel` tinyint(4) DEFAULT NULL COMMENT '支付渠道类型 0支付宝 1微信',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` bigint(20) DEFAULT 0 COMMENT '来源实体店ID',
  `project_type` varchar(64) DEFAULT NULL COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `community_id` bigint(20) DEFAULT 0,
  `namespace_id` bigint(20) DEFAULT 0,
  `pay_date` date DEFAULT NULL COMMENT 'payment date',
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- 同步任务日志跟踪表
CREATE TABLE `eh_sync_task_logs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `task_type` VARCHAR(64) DEFAULT NULL,
  `request_params` VARCHAR(1024) DEFAULT NULL,
  `response_params` text,
  `api_url` VARCHAR(1024) DEFAULT NULL COMMENT 'request url',
  `exception_log` text COMMENT 'exception info',
  `status` TINYINT(4) DEFAULT NULL COMMENT '0：start 1：fail 2：succeed 3：finish',
  `anchor` BIGINT DEFAULT 0,
  `update_date` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

