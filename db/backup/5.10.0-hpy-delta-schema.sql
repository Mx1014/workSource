-- AUTHOR: 黄鹏宇 2018-11-6
-- REMARK: 创建 客户统计表，每日
CREATE TABLE `eh_customer_statistics_daily` (
 `id` bigint NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` datetime COMMENT '统计的日期',
  `community_id` bigint COMMENT '统计字段锁管理的园区ID',
  `new_customer_num` bigint COMMENT '新增客户数',
  `registered_customer_num` bigint COMMENT '成交客户总数',
  `loss_customer_num` bigint COMMENT '流失客户总数',
  `history_customer_num` bigint COMMENT '历史客户总数',
  `delete_customer_num` bigint COMMENT '删除客户总数',
  `tracking_num` bigint COMMENT '跟进总次数',
  `customer_conut` bigint COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` bigint COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日';
-- REMARK: 创建 客户统计表，每月
CREATE TABLE `eh_customer_statistics_monthly` (
  `id` bigint NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` datetime COMMENT '统计的日期',
  `community_id` bigint COMMENT '统计字段锁管理的园区ID',
  `new_customer_num` bigint COMMENT '新增客户 数',
  `registered_customer_num` bigint COMMENT '成交客户总数',
  `loss_customer_num` bigint COMMENT '流失客户总数',
  `history_customer_num` bigint COMMENT '历史客户总数',
  `delete_customer_num` bigint COMMENT '删除客户总数',
  `tracking_num` bigint COMMENT '跟进总次数',
  `customer_conut` bigint COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` bigint COMMENT '创建人',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每月';


-- REMARK: 创建 客户状态改变记录表
CREATE TABLE `eh_customer_level_change_records` (
  `id` bigint NOT NULL,
  `customer_id` bigint DEFAULT NULL COMMENT '被改变的用户id',
  `namespace_id` int COMMENT '域空间',
  `change_date` datetime DEFAULT NULL COMMENT '被改变状态的日期',
  `old_status` bigint DEFAULT NULL COMMENT '被改变之前的状态',
  `new_status` bigint DEFAULT NULL COMMENT '被改变之后的状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户状态改变记录表';


-- REMARK: 创建客户累积记录表
CREATE TABLE `eh_customer_statistics_total` (
  `id` bigint NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` datetime COMMENT '统计的日期',
  `community_id` bigint COMMENT '统计字段锁管理的园区ID',
  `new_customer_num` bigint COMMENT '新增客户 数',
  `registered_customer_num` bigint COMMENT '成交客户总数',
  `loss_customer_num` bigint COMMENT '流失客户总数',
  `history_customer_num` bigint COMMENT '历史客户总数',
  `delete_customer_num` bigint COMMENT '删除客户总数',
  `tracking_num` bigint COMMENT '跟进总次数',
  `customer_conut` bigint COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` bigint COMMENT '创建人',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日累积';

ALTER TABLE eh_general_form_val_requests MODIFY created_time DATETIME;

