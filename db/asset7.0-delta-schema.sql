-- AUTHOR: 杨崇鑫   20181017
-- REMARK: 缴费管理V7.0（新增缴费相关统计报表） 
-- REMARK: 增加项目-时间段（月份）统计结果集表
CREATE TABLE `eh_payment_bill_statistic_community` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),  
  `date_str` VARCHAR(10),  
  `amount_receivable` DECIMAL(10,2) COMMENT '应收（含税)',
  `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）',
  `tax_amount` DECIMAL(10,2) COMMENT '税额',
  `amount_received` DECIMAL(10,2) COMMENT '已收（含税）',
  `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) COMMENT '待收（含税）',
  `amount_owed_without_tax` DECIMAL(10,2)  COMMENT '待收（不含税）',
  `amount_exemption` DECIMAL(10,2) COMMENT 'amount reduced',
  `amount_supplement` DECIMAL(10,2) COMMENT 'amount increased',  
  `due_day_count` BIGINT COMMENT '总欠费天数', 
  `notice_times` INTEGER COMMENT '总催缴次数',
  `collection_rate` DECIMAL(10,2) COMMENT '收缴率=已收金额/应收含税金额*100%',
  `creat_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP, 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目-时间段（月份）统计结果集表';

 
  