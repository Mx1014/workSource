
CREATE TABLE `eh_punch_vacation_balances` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT COMMENT 'organization_id',
  `owner_type` VARCHAR(32) DEFAULT '' COMMENT 'organization', 
  `user_id` BIGINT COMMENT 'user_id',
  `detail_id` BIGINT COMMENT 'user_id',
  `annual_leave_balance` DOUBLE COMMENT '年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '调休余额',
  `creator_uid` BIGINT DEFAULT '0',
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `update_time` DATETIME ,
  `namespace_id` INT(11) ,
  PRIMARY KEY (`id`),
  KEY `ix_detail_id`(`detail_id`) 
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '假期余额表';


CREATE TABLE `eh_punch_vacation_balance_logs` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT COMMENT 'organization_id',
  `owner_type` VARCHAR(32) DEFAULT '' COMMENT 'organization', 
  `user_id` BIGINT COMMENT 'user_id',
  `detail_id` BIGINT COMMENT 'user_id',
  `annual_leave_balance_correction` DOUBLE COMMENT'年假余额修改',
  `overtime_compensation_balanc_correctione` DOUBLE COMMENT '调休余额修改',
  `annual_leave_balance` DOUBLE COMMENT '修改后年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '修改后调休余额',
  `description` TEXT COMMENT '备注',
  `creator_uid` BIGINT DEFAULT '0',
  `create_time` DATETIME ,
  `operator_uid` BIGINT ,
  `update_time` DATETIME ,
  `namespace_id` INT(11) ,
  PRIMARY KEY (`id`),
  KEY `ix_detail_id`(`detail_id`) 
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '假期余额操作日志表';