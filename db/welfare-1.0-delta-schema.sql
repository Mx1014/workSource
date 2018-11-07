-- 企业福利1.0 建表sql
CREATE TABLE `eh_welfares` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `subject` VARCHAR(128) COMMENT '主题名称',
  `content` TEXT COMMENT '祝福语',
  `sender_name` VARCHAR(128) COMMENT '发放人姓名',
  `sender_uid` BIGINT COMMENT '发放人userId',
  `sender_detail_id` BIGINT COMMENT '发放人detailId',
  `img_uri` VARCHAR(1024) COMMENT '附图uri',
  `status` TINYINT COMMENT '0-草稿 1-发送',
  `welfare_type` TINYINT COMMENT '1.0暂时只支持发放一种福利,福利主表加入福利类型 0-卡券 1-积分',
  `send_time` DATETIME DEFAULT NULL, 
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
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `receiver_uid` BIGINT COMMENT '接收者userId(必有项目)',
  `receiver_name` VARCHAR(128) COMMENT '接收者姓名',
  `receiver_detail_id` BIGINT COMMENT '接收者detailId', 
  `status` TINYINT COMMENT '0-未接收 1-已接受',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利接收人表';

CREATE TABLE `eh_welfare_coupons` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `c_id` BIGINT COMMENT '卡券的id',
  `coupon_id` BIGINT COMMENT '卡券的coupon_id',
  `coupon_name` VARCHAR(128) COMMENT '卡券名称',
  `coupon_type` VARCHAR(128) COMMENT '卡券类型',
  `coupon_content` VARCHAR(128) COMMENT '卡券内容字段',
  `valid_date_type` TINYINT COMMENT '截止日期类型',
  `valid_date` DATE COMMENT '截止日期',
  `begin_date` DATE COMMENT '开始日期',
  `amount` DATE COMMENT '发放数量(每人)',
  `order_no` VARCHAR(128) COMMENT '卡券系统的交易id(暂定)',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利卡券表';

CREATE TABLE `eh_welfare_points` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `welfare_id` BIGINT NOT NULL COMMENT '福利id',
  `organization_id` BIGINT DEFAULT NULL COMMENT 'organization id',
  `point_id` BIGINT COMMENT '积分的id',
  `point_name` VARCHAR(128) COMMENT '积分名称',
  `coupon_type` VARCHAR(128) COMMENT '卡券类型',
  `point_content` VARCHAR(128) COMMENT '积分内容字段',
  `valid_date` DATE COMMENT '截止日期',
  `begin_date` DATE COMMENT '开始日期',
  `amount` DATE COMMENT '发放数量(每人)',
  `order_no` VARCHAR(128) COMMENT '积分系统的交易id(暂定)',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '企业福利积分表';