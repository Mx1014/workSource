-- 企业福利1.0 建表sql
CREATE TABLE `eh_welfares` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER ,
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `subject` VARCHAR(128) COMMENT '主题名称',
  `content` TEXT COMMENT '祝福语',
  `sender_name` VARCHAR(128) COMMENT '发放人姓名',
  `sender_uid` BIGINT COMMENT '发放人userId',
  `sender_detail_id` BIGINT COMMENT '发放人detailId',
  `img_uri` VARCHAR(1024) COMMENT '附图uri',
  `status` TINYINT COMMENT '0-草稿 1-发送',
  `welfare_type` TINYINT COMMENT '1.0暂时只支持发放一种福利,福利主表加入福利类型 0-卡券 1-积分',
  `is_delete` TINYINT DEFAULT 0 COMMENT '0-没删除 1-删除',
  `send_time` DATETIME DEFAULT NULL, 
  `coupon_orders` TEXT COMMENT '卡券系统的交易id 列表',
  `point_orders` TEXT COMMENT '卡券系统的交易id 列表',
  `creator_name` VARCHAR(128)  DEFAULT NULL, 
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL, 
  `operator_name` VARCHAR(128)  DEFAULT NULL,  
  `operator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利表';

CREATE TABLE `eh_welfare_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER ,
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `receiver_uid` BIGINT COMMENT '接收者userId(必有项目)',
  `receiver_name` VARCHAR(128) COMMENT '接收者姓名',
  `receiver_detail_id` BIGINT COMMENT '接收者detailId', 
  `status` TINYINT COMMENT '0-未接收 1-已接受',
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY i_receiver_welfare_id (`welfare_id`),
  KEY i_receiver_user_id (`organization_id`,`receiver_uid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利接收人表';

CREATE TABLE `eh_welfare_coupons` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER ,
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `coupon_id` BIGINT COMMENT '卡券的id',
  `coupon_type` VARCHAR(128) COMMENT '卡券类型',
  `coupon_name` VARCHAR(128) COMMENT '卡券名称',
  `denomination` VARCHAR(128) COMMENT '卡券面额',
  `sub_type` VARCHAR(128) COMMENT '购物卡类别，目前仅用于购物卡， 1-小时卡、2-金额卡、3-次数卡',
  `service_supply_name` VARCHAR(128) COMMENT '适用地点',
  `service_range` VARCHAR(128) COMMENT '适用范围',
  `consumption_limit` VARCHAR(128) COMMENT '满多少可以用',
  `valid_date_type` TINYINT COMMENT '截止日期类型',
  `valid_date` DATE COMMENT '截止日期',
  `begin_date` DATE COMMENT '开始日期',
  `amount` INTEGER COMMENT '发放数量(每人)',
  PRIMARY KEY (`id`),
  KEY i_coupon_welfare_id (`welfare_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利卡券表';

CREATE TABLE `eh_welfare_points` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER ,
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `point_id` BIGINT COMMENT '积分的id',
  `point_name` VARCHAR(128) COMMENT '积分名称',
  `coupon_type` VARCHAR(128) COMMENT '卡券类型',
  `point_content` VARCHAR(128) COMMENT '积分内容字段',
  `valid_date` DATE COMMENT '截止日期',
  `begin_date` DATE COMMENT '开始日期',
  `amount` VARCHAR(128) COMMENT '发放数量(每人)',
  PRIMARY KEY (`id`),
  KEY i_point_welfare_id (`welfare_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利积分表';