
CREATE TABLE `eh_reserve_rules` (
  `id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'id',
  `namespace_id` int(11) DEFAULT NULL COMMENT '域空间',
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type: community, organization',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'community id or organization id',
  `resource_type_id` bigint(20) DEFAULT NULL COMMENT 'resource type id',
  `at_most_advance_time` bigint(20) DEFAULT NULL COMMENT '最多提前多少时间预定,存的提前时间的时间戳',
  `at_least_advance_time` bigint(20) DEFAULT NULL COMMENT '最少提前多少时间预定,存的提前时间的时间戳',

  `day_open_start_time` double DEFAULT NULL COMMENT '每天开放开始时间',
  `day_open_end_time` double DEFAULT NULL COMMENT '每天开放结束时间',
  `holiday_open_flag` tinyint(4) DEFAULT NULL COMMENT '节假日是否开放预约: 1-是, 0-否',
  `holiday_type` tinyint(4) DEFAULT NULL COMMENT '1-普通双休, 0-同步中国节假日',

  `reserve_type` tinyint(4) DEFAULT NULL COMMENT '1: 时, 2: 半天, 3: 天',
  `reserve_unit` int(11) DEFAULT NULL COMMENT '预约时间单元，例如当在小时模式时，半个小时，一个小时,（半小时是0.5）',
  `workday_price` decimal(10,2) DEFAULT NULL COMMENT '工作日价格',
  `holiday_price` decimal(10,2) DEFAULT NULL COMMENT '节假日价格',

  `refund_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full',
  `overtime_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full',

  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,

  `auto_assign` tinyint(4) DEFAULT NULL COMMENT '是否动态分配: 1-是, 0-否',
  `multi_unit` tinyint(4) DEFAULT NULL COMMENT '是否允许预约多个场所: 1-是, 0-否',

  `open_weekday` varchar(7) DEFAULT NULL COMMENT '7位二进制，0000000每一位表示星期7123456',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_reserve_discount_users` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT '"default_rule","resource_rule"',
  `owner_id` bigint(20) DEFAULT NULL,
  `user_str` text COMMENT '存取用户信息，以逗号隔开 域空间-手机号',
  `discount` double DEFAULT NULL COMMENT '折扣',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_reserve_rule_strategies` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT '"default_rule","resource_rule"',
  `owner_id` bigint(20) DEFAULT NULL,
  `strategy_type` tinyint(4) DEFAULT NULL COMMENT '1: 退款, 2: 加收',
  `duration_type` tinyint(4) DEFAULT NULL COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` varchar(20) DEFAULT NULL COMMENT '时长单位，比如 天，小时',
  `duration` double DEFAULT NULL COMMENT '时长',
  `factor` double DEFAULT NULL COMMENT '价格系数',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_reserve_close_dates` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT '"default_rule","resource_rule"',
  `owner_id` bigint(20) DEFAULT NULL,
  `close_date` date DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_spaces` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `space_no` varchar(64) NOT NULL DEFAULT '' COMMENT 'used to display',
  `space_address` varchar(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `lock_id` varchar(128) DEFAULT NULL COMMENT 'parking lot id from vendor',
  `lock_status` varchar(128) DEFAULT NULL COMMENT 'parking lot id from vendor',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_space_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `contact_phone` varchar(64) DEFAULT NULL,
  `contact_name` varchar(64) DEFAULT NULL,
  `contact_enterprise_name` varchar(128) DEFAULT NULL,
  `operate_type` tinyint(4) NOT NULL COMMENT '1: raise, 2: down',
  `user_type` tinyint(4) NOT NULL COMMENT '1: reserve, 2: plate owner',
  `operate_uid` bigint(20) NOT NULL DEFAULT '0',
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_reserve_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `namespace_id` int(11) DEFAULT NULL COMMENT '域空间',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `order_no` varchar(20) NOT NULL COMMENT '订单编号',

  `resource_type` tinyint(4) DEFAULT NULL,
  `reserve_resource_id` bigint(20) NOT NULL COMMENT 'resource id',
  `resource_json` text COMMENT '资源json数据',

--  `reserve_date` date DEFAULT NULL COMMENT '使用日期',
  `reserve_start_time` datetime DEFAULT NULL COMMENT '预订开始时间',
  `reserve_end_time` datetime DEFAULT NULL COMMENT '预订结束时间',
  `reserve_cell_count` double DEFAULT NULL COMMENT '预约单元格数',
  `actual_start_time` datetime DEFAULT NULL COMMENT '实际开始时间',
  `actual_end_time` datetime DEFAULT NULL COMMENT '实际结束时间',

  `applicant_enterprise_id` bigint(20) DEFAULT NULL COMMENT '申请人公司ID',
  `applicant_enterprise_name` varchar(64) DEFAULT NULL COMMENT '申请人公司名称',
  `applicant_phone` varchar(20) DEFAULT NULL COMMENT '申请人手机',
  `applicant_name` varchar(20) DEFAULT NULL COMMENT '申请人姓名',
  `address_id` bigint(20) DEFAULT NULL COMMENT '楼栋门牌ID',

  `over_time` bigint(20) DEFAULT NULL COMMENT '超时时间，存时间戳',
  `pay_time` datetime DEFAULT NULL,
  `pay_type` varchar(20) DEFAULT NULL COMMENT '支付方式,10001-支付宝，10002-微信',
  `paid_amount` decimal(10,2) DEFAULT NULL COMMENT '已支付金额',
  `owing_amount` decimal(10,2) DEFAULT NULL COMMENT '欠费金额',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '总金额',
  `status` tinyint(4) DEFAULT NULL,

  `creator_uid` bigint(20) DEFAULT NULL COMMENT '预约人',
  `create_time` datetime DEFAULT NULL COMMENT '下单时间',
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `cancel_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'cancel user id',
  `cancel_time` datetime DEFAULT NULL,

  `flow_mode` tinyint(4) DEFAULT '0' COMMENT 'flow mode, 1:online pay 2:approve offline 3: approve online',
  `flow_case_id` bigint(20) DEFAULT NULL COMMENT 'id of the flow_case',
  `paid_version` tinyint(4) DEFAULT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

