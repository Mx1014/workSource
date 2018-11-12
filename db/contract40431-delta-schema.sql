
-- AUTHOR: djm
-- REMARK: 合同初始化，审批，复制进度记录表
CREATE TABLE `eh_contract_task_operate_logs` (
	`id` BIGINT (20) NOT NULL,
	`namespace_id` INT (11) DEFAULT NULL,
	`owner_id` BIGINT (20) DEFAULT NULL COMMENT '园区id',
	`owner_type` VARCHAR (64) NOT NULL,
	`org_id` BIGINT (20) DEFAULT NULL,
	`name` VARCHAR (64),
	`process` INTEGER COMMENT 'rate of progress',
	`operate_type` TINYINT (4) DEFAULT NULL COMMENT '1 合同初始化, ２　合同免批,3　合同复制',
	`processed_number` INT (11) DEFAULT NULL COMMENT '已处理记录数',
	`total_number` INT (11) DEFAULT NULL COMMENT '总记录数',
	`status` TINYINT (4) DEFAULT NULL COMMENT '0:无效状态，2：激活状态',
	`start_time` DATETIME DEFAULT NULL,
	`finish_time` DATETIME DEFAULT NULL,
	`execute_time` DATETIME DEFAULT NULL,
	`category_id` BIGINT COMMENT 'asset category id',
	`creator_uid` BIGINT (20) DEFAULT NULL,
	`create_time` DATETIME DEFAULT NULL,
	`operator_uid` BIGINT (20) DEFAULT NULL,
	`operator_time` DATETIME DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '合同初始化,审批,复制进度记录表';
