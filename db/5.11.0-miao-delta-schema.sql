CREATE TABLE `eh_office_cubicle_default_rules` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `owner_type` VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT 'community id ',
  `cubicle_start_time` BIGINT COMMENT '最多提前多少时间预定',
  `cubicle_end_time` BIGINT COMMENT '最少提前多少时间预定',
  `refund_flag` TINYINT COMMENT '是否支持退款: 1-是, 0-否',
  `refund_ratio` INTEGER COMMENT '退款比例',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段: 1-是, 0-否',
  `need_pay` TINYINT COMMENT '是否需要支付: 1-是, 0-否',
  `begin_date` DATE COMMENT '开始日期',
  `end_date` DATE COMMENT '结束日期',
  `cubicle_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制',
  `holiday_open_flag` TINYINT COMMENT '节假日是否开放预约: 1-是, 0-否',
  `holiday_type` TINYINT COMMENT '1-普通双休, 2-同步中国节假日',
  `refund_strategy` TINYINT COMMENT '1-custom, 2-full',
  `overtime_strategy` TINYINT COMMENT '1-custom, 2-full',
  `remark_flag` TINYINT COMMENT '备注是否必填 0否 1是',
  `remark` VARCHAR(255) COMMENT '备注显示文案',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_time_interval` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `rule_id` BIGINT,
  `morning_begin_time` DOUBLE COMMENT '上午开始时间-24小时制',
  `morning_end_time` DOUBLE COMMENT '上午结束时间-24小时制',
  `afternoon_begin_time` DOUBLE COMMENT '下午开始时间-24小时制',
  `afternoon_end_time` DOUBLE COMMENT '下午结束时间-24小时制',
  `night_begin_time` DOUBLE COMMENT '晚上开始时间-24小时制',
  `night_end_time` DOUBLE COMMENT '晚上结束时间-24小时制',
  `begin_time` DOUBLE COMMENT '整天开始时间-24小时制',
  `end_time`  DOUBLE COMMENT '整天开始时间-24小时制',
  `cubicle_type` TINYINT COMMENT '0按半天，1按天，3按半天带晚上',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_price_rules` (
  `id` BIGINT NOT NULL,
  `rule_id` BIGINT NOT NULL DEFAULT 0,
  `cubicle_type` TINYINT COMMENT '0按半天，1按天，3按半天带晚上',
  `price_type` TINYINT,
  `workday_price` DECIMAL(10,2) COMMENT '工作日价格',
  `original_price` DECIMAL(10,2),
  `initiate_price` DECIMAL(10,2),
  `full_price` DECIMAL(10,2) COMMENT '满XX',
  `cut_price` DECIMAL(10,2) COMMENT '减XX元',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `user_price_type` TINYINT COMMENT '用户价格类型, 1:统一价格 2：用户类型价格',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_office_cubicle_order_rules` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `rule_id` BIGINT,
  `handle_type` TINYINT COMMENT '1: 退款, 2: 加收',
  `duration_type` TINYINT COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` TINYINT COMMENT '时长单位，比如 天，小时',
  `duration` DOUBLE COMMENT '时长',
  `factor` DOUBLE COMMENT '价格系数',
  `status` TINYINT,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_refund_tips` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `rule_id` BIGINT,
  `refund_strategy` TINYINT,
  `tips` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_office_cubicle_stations` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `station_id` BIGINT COMMENT '工位ID',
  `station_name` VARCHAR(127) COMMENT '名称：',
  `price` DECIMAL(10,2) COMMENT '价格',
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `description` TEXT COMMENT '描述',
  `cover_uri` VARCHAR(1024) COMMENT '封面图uri',
  `rent_flag` TINYINT COMMENT '是否开放预定 1是 0否',
  `rent_type` TINYINT COMMENT '预定属性 1长租 0短租',
  `station_type` TINYINT COMMENT '工位属性 1办公室 0普通工位',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


ALTER TABLE eh_office_cubicle_orders ADD COLUMN price DECIMAL(10,2) COMMENT '价格';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN begin_time DATETIME COMMENT '预定开始时间';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN end_time DATETIME COMMENT '预定结束时间';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN rent_count TINYINT COMMENT '预定数量';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN cubicle_type TINYINT COMMENT '工位性质';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN remark TEXT COMMENT '备注';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN order_status TINYINT COMMENT '订单状态';

CREATE TABLE `eh_office_cubicle_payee_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT NOT NULL COMMENT '园区id或者其他id',
  `space_id` BIGINT NOT NULL COMMENT '空间id',
  `space_name` VARCHAR(512) NOT NULL COMMENT '空间名称',
  `payee_id` BIGINT NOT NULL COMMENT '支付帐号id',
  `payee_user_type` VARCHAR(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `merchant_id` BIGINT COMMENT '商户ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工位预定收款账户表';
