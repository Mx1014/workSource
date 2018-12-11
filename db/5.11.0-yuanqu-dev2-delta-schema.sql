CREATE TABLE `eh_office_cubicle_station` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  `station_id` BIGINT COMMENT '工位ID',
  `station_name` VARCHAR(127) COMMENT '名称：',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `description` TEXT COMMENT '描述',
  `cover_uri` VARCHAR(1024) COMMENT '封面图uri',
  `rent_flag` TINYINT COMMENT '是否开放预定 1是 0否',
  `price` DECIMAL(10,2) COMMENT '价格',
  `associate_room_id` BIGINT COMMENT '关联办公室id',
  `status` TINYINT COMMENT '1-未预定，2-已预定',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_room` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  `room_name` VARCHAR(127) COMMENT '办公室名臣',
  `cover_uri` VARCHAR(1024) COMMENT '封面图uri',
  `rent_flag` TINYINT COMMENT '是否开放预定 1是 0否',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `description` TEXT COMMENT '描述',
  `price` DECIMAL(10,2) COMMENT '价格',
  `status` TINYINT COMMENT '1-未预定，2-已预定',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_station_rent` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `space_id` BIGINT COMMENT '空间ID',
  `order_id` BIGINT COMMENT '订单号',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  `station_id` BIGINT COMMENT '工位/办公室Id',
  `station_name` VARCHAR(127),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_office_cubicle_rent_orders` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `order_no` BIGINT,
  `biz_order_no` VARCHAR(128),
  `namespace_id` INTEGER NOT NULL,
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  `space_name` VARCHAR(255) COMMENT '空间名称',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  `rent_type` TINYINT COMMENT '1-长租，0-短租',
  `station_type` TINYINT COMMENT '1-普通工位，0-办公室',
  `station_id` BIGINT COMMENT '工位/办公室Id',
  `rental_order_no` BIGINT COMMENT '资源预约订单号',
  `order_status` TINYINT COMMENT '订单状态',
  `request_type` TINYINT COMMENT '订单来源',
  `paid_type` VARCHAR(32) COMMENT '支付方式',
  `paid_mode` TINYINT COMMENT '支付类型',
  `price` DECIMAL(10,2) COMMENT '价格',
  `rent_count` BIGINT COMMENT '预定数量',
  `remark` TEXT COMMENT '备注',
  `reserver_uid` BIGINT COMMENT '预定人id',
  `reserver_name` VARCHAR(32) COMMENT '预定人姓名',
  `reserver_enterprise_name` VARCHAR(64) COMMENT '预定人公司名称',
  `reserver_enterprise_Id` BIGINT COMMENT '预定人公司ID',
  `reserver_contact_token` VARCHAR(64) COMMENT '预定人联系方式',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `use_detail` VARCHAR(255),
  `refund_strategy` TINYINT COMMENT '1-custom, 2-full',
  `account_name` VARCHAR(64) COMMENT '收款账户名称',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_payee_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT NOT NULL COMMENT '园区id或者其他id',
  `account_id` BIGINT,
  `merchant_id` BIGINT,
  `account_name` VARCHAR(64) COMMENT '收款账户名称',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_charge_users` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT COMMENT '园区id或者其他id',
  `space_id` BIGINT,
  `charge_uid` BIGINT,
  `charge_name` VARCHAR(32),
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_refund_rule` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT NOT NULL COMMENT '园区id或者其他id',
  `space_id` BIGINT,
  `refund_strategy` TINYINT,
  `duration_type` TINYINT COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` TINYINT COMMENT '时长单位，比如 天，小时',
  `duration` DOUBLE COMMENT '时长',
  `factor` DOUBLE COMMENT '价格系数',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_refund_tips` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `space_id` BIGINT,
  `refund_strategy` TINYINT,
  `tips` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_office_cubicle_spaces ADD COLUMN open_flag TINYINT COMMENT '是否开启空间，1是，0否';
ALTER TABLE eh_office_cubicle_spaces ADD COLUMN short_rent_nums VARCHAR(32) COMMENT '短租工位数量';
ALTER TABLE eh_office_cubicle_spaces ADD COLUMN long_rent_price DECIMAL(10,2) COMMENT '长租工位价格';
ALTER TABLE eh_office_cubicle_spaces ADD COLUMN refund_strategy TINYINT;


ALTER TABLE eh_office_cubicle_attachments ADD COLUMN type TINYINT COMMENT '1,空间，2短租工位，3开放式工位';

ALTER TABLE eh_office_cubicle_orders MODIFY COLUMN employee_number VARCHAR(64) COMMENT '雇员数量';
