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
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_room` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  `room_id` BIGINT COMMENT '办公室ID',
  `station_name` VARCHAR(127) COMMENT '办公室名臣',
  `cover_uri` VARCHAR(1024) COMMENT '封面图uri',
  `rent_flag` TINYINT COMMENT '是否开放预定 1是 0否',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `description` TEXT COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_station_rent` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `space_id` BIGINT COMMENT '空间ID',
  `order_id` BIGINT COMMENT '订单号',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  `rent_type` TINYINT COMMENT '1-长租，0-短租',
  `station_type` TINYINT COMMENT '1-普通工位，0-办公室',
  `station_id` BIGINT COMMENT '工位/办公室Id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_office_cubicle_rent_order` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL,
  `owner_type`  VARCHAR(255) COMMENT 'owner type: community',
  `owner_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `space_id` BIGINT COMMENT '空间ID',
  `begin_time` DATETIME COMMENT '预定开始时间',
  `end_time` DATETIME COMMENT '预定结束时间',
  `rent_type` TINYINT COMMENT '1-长租，0-短租',
  `station_type` TINYINT COMMENT '1-普通工位，0-办公室',
  `station_id` BIGINT COMMENT '工位/办公室Id',
  `rental_order_no` BIGINT COMMENT '资源预约订单号',
  `order_status` TINYINT COMMENT '订单状态',
  `price` DECIMAL(10,2) COMMENT '价格',
  `remark` TEXT COMMENT '备注',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_office_cubicle_payee_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT NOT NULL COMMENT '园区id或者其他id',
  `space_id` BIGINT NOT NULL COMMENT '空间id',
  `space_name` VARCHAR(512) NOT NULL COMMENT '空间名称',
  `payee_user_type` VARCHAR(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `merchant_id` BIGINT COMMENT '商户ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工位预定收款账户表';
