-- AUTHOR: 黄鹏宇 2018-11-6
-- REMARK: 创建 客户统计表，每日
CREATE TABLE `eh_customer_statistics_daily` (
 `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `community_id` BIGINT COMMENT '统计字段锁管理的园区ID',
  `new_customer_num` INT COMMENT '新增客户增量',
  `registered_customer_num` INT COMMENT '成交客户增量',
  `loss_customer_num` INT COMMENT '流失客户增量',
  `history_customer_num` INT COMMENT '历史客户增量',
  `delete_customer_num` INT COMMENT '删除客户增量',
  `tracking_num` INT COMMENT '跟进次数增量',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日';

-- REMARK: 创建 客户统计表，每日，按管理公司汇总
CREATE TABLE `eh_customer_statistics_daily_total` (
 `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `organization_id` BIGINT COMMENT '统计字段所在的管理公司ID',
  `community_num` INT COMMENT '管理公司下的园区数量',
  `new_customer_num` INT COMMENT '新增客户增量',
  `registered_customer_num` INT COMMENT '成交客户增量',
  `loss_customer_num` INT COMMENT '流失客户增量',
  `history_customer_num` INT COMMENT '历史客户增量',
  `delete_customer_num` INT COMMENT '删除客户增量',
  `tracking_num` INT COMMENT '跟进次数增量',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日，按管理公司汇总';

-- REMARK: 创建 客户统计表，每月
CREATE TABLE `eh_customer_statistics_monthly` (
  `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `community_id` BIGINT COMMENT '统计字段锁管理的园区ID',
  `new_customer_num` INT COMMENT '新增客户增量',
  `registered_customer_num` INT COMMENT '成交客户增量',
  `loss_customer_num` INT COMMENT '流失客户增量',
  `history_customer_num` INT COMMENT '历史客户增量',
  `delete_customer_num` INT COMMENT '删除客户增量',
  `tracking_num` INT COMMENT '跟进次数增量',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每月';

-- REMARK: 创建 客户统计表，每月，按管理公司汇总
CREATE TABLE `eh_customer_statistics_monthly_total` (
 `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `organization_id` BIGINT COMMENT '统计字段所在的管理公司ID',
  `community_num` INT COMMENT '管理公司下的园区数量',
  `new_customer_num` INT COMMENT '新增客户增量',
  `registered_customer_num` INT COMMENT '成交客户增量',
  `loss_customer_num` INT COMMENT '流失客户增量',
  `history_customer_num` INT COMMENT '历史客户增量',
  `delete_customer_num` INT COMMENT '删除客户增量',
  `tracking_num` INT COMMENT '跟进次数增量',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每月，按管理公司汇总';

-- REMARK: 创建 客户状态改变记录表
CREATE TABLE `eh_customer_level_change_records` (
  `id` BIGINT NOT NULL,
  `customer_id` BIGINT COMMENT '被改变的用户id',
  `namespace_id` int COMMENT '域空间',
  `community_id` BIGINT COMMENT '所在园区',
  `change_date` datetime COMMENT '被改变状态的日期',
  `old_status` BIGINT COMMENT '被改变之前的状态',
  `new_status` BIGINT COMMENT '被改变之后的状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户状态改变记录表';


-- REMARK: 创建客户累积记录表
CREATE TABLE `eh_customer_statistics_total` (
  `id` BIGINT NOT NULL,
  `namespace_id` int COMMENT '域空间',
  `date_str` DATE COMMENT '统计的日期',
  `organization_id` BIGINT COMMENT '统计字段锁管理的园区ID',
  `community_num` INT COMMENT '管理公司下的园区数量',
  `new_customer_num` INT COMMENT '新增客户 数',
  `registered_customer_num` INT COMMENT '成交客户总数',
  `loss_customer_num` INT COMMENT '流失客户总数',
  `history_customer_num` INT COMMENT '历史客户总数',
  `delete_customer_num` INT COMMENT '删除客户总数',
  `tracking_num` INT COMMENT '跟进总次数',
  `customer_count` INT COMMENT '园区内的总客户数',
  `create_date` DATETIME COMMENT '创建日期',
  `create_uid` BIGINT COMMENT '创建人',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT = '客户统计表，每日累积';
