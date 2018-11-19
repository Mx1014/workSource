CREATE TABLE `eh_office_cubicle_default_rules` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',
  `owner_id` BIGINT COMMENT 'community id or organization id',
  `resource_type_id` BIGINT COMMENT 'resource type id',
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
  `day_open_time` DOUBLE,
  `day_close_time` DOUBLE,
  `cubicle_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制',
  `cubicle_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制',
  `source_type` VARCHAR(255) COMMENT 'default_rule, resource_rule',
  `source_id` BIGINT,
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT '"default_rule","resource_rule"',
  `morning_begin_time` DOUBLE COMMENT '上午开始时间-24小时制',
  `morning_end_time` DOUBLE COMMENT '上午结束时间-24小时制',
  `afternoon_begin_time` DOUBLE COMMENT '下午开始时间-24小时制',
  `afternoon_end_time` DOUBLE COMMENT '下午结束时间-24小时制',
  `night_begin_time` DOUBLE COMMENT '晚上开始时间-24小时制',
  `night_end_time` DOUBLE COMMENT '晚上结束时间-24小时制',
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  `begin_time` DOUBLE COMMENT '整天开始时间-24小时制',
  `end_time`  DOUBLE COMMENT '整天开始时间-24小时制',
  `cubicle_type` TINYINT COMMENT '0按半天，1按天，3按半天带晚上',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_price_rules` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'default, resource, cell',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'default_rule_id, resource_id, cell_id',
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
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_close_dates` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT '"default_rule","resource_rule"',
  `close_date` DATE,
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_order_rules` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  `owner_type` VARCHAR(255) COMMENT 'default_rule, resource_rule',
  `owner_id` BIGINT,
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
  `source_type` VARCHAR(20),
  `source_id` BIGINT,
  `refund_strategy` TINYINT,
  `tips` VARCHAR(255),
  `resource_type` VARCHAR(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_office_cubicle_resource_types` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `name` VARCHAR(50) COMMENT '名称',
  `page_type` TINYINT COMMENT '预定展示0代表默认页面DefaultType, 1代表定制页面CustomType',
  `icon_uri` VARCHAR(1024) COMMENT '工作日价格',
  `status` TINYINT COMMENT '状态：0关闭 2开启',
  `namespace_id` INTEGER COMMENT '域空间',
  `pay_mode` TINYINT DEFAULT 0 COMMENT 'pay mode :0-online pay 1-offline',
  `unauth_visible` TINYINT DEFAULT 0,
  `station_type` TINYINT DEFAULT 1 COMMENT '1: 通用 2:公司会议室',
  `identify` VARCHAR(64) COMMENT '类型标识',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_stations` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `parent_id` BIGINT,
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
  `organization_id` BIGINT COMMENT '所属公司的ID',
  `community_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


ALTER TABLE eh_office_cubicle_orders ADD COLUMN price DECIMAL(10,2) COMMENT '价格';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN begin_time DATETIME COMMENT '预定开始时间';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN end_time DATETIME COMMENT '预定结束时间';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN rent_count TINYINT COMMENT '预定数量';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN cubicle_type TINYINT COMMENT '工位性质';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN remark TEXT COMMENT '备注';
ALTER TABLE eh_office_cubicle_orders ADD COLUMN order_status TINYINT COMMENT '订单状态';
