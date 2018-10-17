-- AUTHOR: 杨崇鑫   20181017
-- REMARK: 缴费管理V7.0（新增缴费相关统计报表） 
-- REMARK: 增加项目-时间段（月份）统计结果集表
CREATE TABLE `eh_payment_bill_statistic_community` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),  
  `date_str` VARCHAR(10),  
  `amount_receivable` DECIMAL(10,2)COMMENT 'amount should be received',
  `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）',
  `tax_amount` DECIMAL(10,2) COMMENT '税额',
  `amount_received` DECIMAL(10,2) COMMENT 'amount actually received by far',
  `amount_received_without_tax` decimal(10,2) COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) COMMENT 'unpaid amount',
  `amount_owed_without_tax` decimal(10,2)  COMMENT '待收（不含税）',
  `amount_exemption` DECIMAL(10,2) DEFAULT '0.00' COMMENT 'amount reduced',
  `amount_supplement` DECIMAL(10,2) DEFAULT '0.00' COMMENT 'amount increased',   
  `notice_times` INTEGER COMMENT 'times bill owner has been called for dued payments',  
  `creator_id` BIGINT,
  `creat_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,  
  `due_day_count` BIGINT COMMENT '欠费天数', 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目-时间段（月份）统计结果集表';
-- REMARK: 账单表增删改触发器---》更新项目-时间段（月份）统计结果集表数据
DELIMITER $
create trigger eh_payment_bill_statistic_community_trigger before update 
on eh_payment_bills for each row
begin
	update eh_payment_bill_statistic_community 
    set amount_receivable = IFNULL(amount_receivable,0) + (IFNULL(NEW.amount_receivable,0) - IFNULL(OLD.amount_receivable,0)),
    amount_receivable_without_tax = IFNULL(amount_receivable_without_tax,0) + (IFNULL(NEW.amount_receivable_without_tax,0) - IFNULL(OLD.amount_receivable_without_tax,0)),
    amount_owed = IFNULL(amount_owed,0) + (IFNULL(NEW.amount_owed,0) - IFNULL(OLD.amount_owed,0));
end;
 
 
  