
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

  `pay_start_time` bigint(20) DEFAULT NULL,
  `pay_end_time` bigint(20) DEFAULT NULL,
  `payment_ratio` int(11) DEFAULT NULL COMMENT 'payment ratio',

  `contact_num` varchar(20) DEFAULT NULL COMMENT 'phone number',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `cancel_time` bigint(20) DEFAULT NULL COMMENT '至少提前取消时间',
  `overtime_time` bigint(20) DEFAULT NULL COMMENT '超期时间',
  `exclusive_flag` tinyint(4) DEFAULT NULL COMMENT '是否为独占资源: 0-否, 1-是',
  `unit` double DEFAULT NULL COMMENT '1-整租, 0.5-可半个租',
  `auto_assign` tinyint(4) DEFAULT NULL COMMENT '是否动态分配: 1-是, 0-否',
  `multi_unit` tinyint(4) DEFAULT NULL COMMENT '是否允许预约多个场所: 1-是, 0-否',
  `multi_time_interval` tinyint(4) DEFAULT NULL COMMENT '是否允许预约多个时段: 1-是, 0-否',

  `open_weekday` varchar(7) DEFAULT NULL COMMENT '7位二进制，0000000每一位表示星期7123456',
  `time_step` double DEFAULT NULL COMMENT '步长，每个单元格是多少小时（半小时是0.5）',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_reserve_discount_users` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT '"default_rule","resource_rule"',
  `owner_id` bigint(20) DEFAULT NULL,
  `duration_type` tinyint(4) DEFAULT NULL COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` varchar(20) DEFAULT NULL COMMENT '时长单位，比如 天，小时',
  `duration` double DEFAULT NULL COMMENT '时长',
  `discount` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_reserve_rule_strategies` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT '"default_rule","resource_rule"',
  `owner_id` bigint(20) DEFAULT NULL,
  `duration_type` tinyint(4) DEFAULT NULL COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` varchar(20) DEFAULT NULL COMMENT '时长单位，比如 天，小时',
  `duration` double DEFAULT NULL COMMENT '时长',
  `discount` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_reserve_close_dates` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(255) DEFAULT NULL COMMENT '"default_rule","resource_rule"',
  `owner_id` bigint(20) DEFAULT NULL,
  `close_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_reserve_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `namespace_id` int(11) DEFAULT NULL COMMENT '域空间',
  `order_no` varchar(20) NOT NULL COMMENT '订单编号',
  `reserve_resource_id` bigint(20) NOT NULL COMMENT 'resource id',
--  `rental_date` date DEFAULT NULL COMMENT '使用日期',
  `start_time` datetime DEFAULT NULL COMMENT '使用开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '使用结束时间',
  `reserve_cell_count` double DEFAULT NULL COMMENT '预约数',

  `pay_time` datetime DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '总金额',
  `vendor_type` varchar(255) DEFAULT NULL COMMENT '支付方式,10001-支付宝，10002-微信',
  `status` tinyint(4) DEFAULT NULL,

  `creator_uid` bigint(20) DEFAULT NULL COMMENT '预约人',
  `create_time` datetime DEFAULT NULL COMMENT '下单时间',
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `cancel_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'cancel user id',
  `cancel_time` datetime DEFAULT NULL,

  `resource_name` varchar(255) DEFAULT NULL COMMENT '名称',
  `use_detail` varchar(255) DEFAULT NULL COMMENT '使用时间',
  `resource_type_id` bigint(20) DEFAULT NULL COMMENT 'resource type id',
  `organization_id` bigint(20) DEFAULT NULL COMMENT '所属公司的ID',
  `spec` varchar(255) DEFAULT NULL COMMENT '规格',
  `address` varchar(192) DEFAULT NULL COMMENT '地址',
  `longitude` double DEFAULT NULL COMMENT '地址经度',
  `latitude` double DEFAULT NULL COMMENT '地址纬度',
  `contact_phonenum` varchar(20) DEFAULT NULL COMMENT '咨询电话',
  `introduction` text COMMENT '详情',
  `notice` text,
  `community_id` bigint(20) DEFAULT NULL COMMENT '资源所属园区的ID',
  `refund_flag` tinyint(4) DEFAULT NULL COMMENT '是否支持退款 1是 0否',
  `refund_ratio` int(11) DEFAULT NULL COMMENT '退款比例',
  `cancel_flag` tinyint(4) DEFAULT NULL COMMENT '是否允许取消 1是 0否',
  `reminder_time` datetime DEFAULT NULL COMMENT '消息提醒时间',
  `reminder_end_time` datetime DEFAULT NULL,
  `auth_start_time` datetime DEFAULT NULL,
  `auth_end_time` datetime DEFAULT NULL,
  `door_auth_id` bigint(20) DEFAULT NULL,
  `package_name` varchar(45) DEFAULT NULL,
  `pay_mode` tinyint(4) DEFAULT '0' COMMENT 'pay mode :0-online pay 1-offline',
  `offline_cashier_address` varchar(200) DEFAULT NULL,
  `offline_payee_uid` bigint(20) DEFAULT NULL,
  `flow_case_id` bigint(20) DEFAULT NULL COMMENT 'id of the flow_case',
  `requestor_organization_id` bigint(20) DEFAULT NULL COMMENT 'id of the requestor organization',
  `paid_version` tinyint(4) DEFAULT NULL,
  `resource_type2` tinyint(4) DEFAULT NULL,
  `rental_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



















CREATE TABLE `eh_relocation_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `request_no` varchar(128) NOT NULL,
  `requestor_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the requestor is in',
  `requestor_enterprise_name` varchar(64) DEFAULT NULL COMMENT 'the enterprise name of requestor',
  `requestor_enterprise_address` varchar(256) DEFAULT NULL COMMENT 'the enterprise address of requestor',
  `requestor_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'requestor id',
  `requestor_name` varchar(64) DEFAULT NULL COMMENT 'the name of requestor',
  `contact_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of requestor',
  `relocation_date` datetime NOT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: processing, 2: completed',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `flow_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'flow case id',
  `cancel_time` datetime DEFAULT NULL,
  `cancel_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'cancel user id',
  `qr_code_url` varchar(256) DEFAULT NULL COMMENT 'url of the qr record',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_relocation_request_items` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `request_id` bigint(20) NOT NULL COMMENT 'id of the relocation request record',
  `item_name` varchar(64) DEFAULT NULL COMMENT 'the name of item',
  `item_quantity` int(11) DEFAULT 0 COMMENT 'the quantity of item',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_relocation_request_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE eh_enterprise_op_requests ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_promotions ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_projects ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_project_communities ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_issuers ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_form_requests ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_configs ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
ALTER TABLE eh_lease_buildings ADD COLUMN `category_id` bigint(20) DEFAULT NULL;
