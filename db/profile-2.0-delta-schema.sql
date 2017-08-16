DROP TABLE IF EXISTS `eh_profile_contacts_sticky`;
CREATE TABLE `eh_profile_contacts_sticky` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL COMMENT '节点id',
	`detail_id` BIGINT NOT NULL COMMENT '成员detailId',
  `create_time` datetime COMMENT '创建时间',
  `update_time` datetime COMMENT '修改时间',
  `operator_uid` BIGINT COMMENT '操作人id',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `eh_profile_dismiss_employees`;
CREATE TABLE `eh_profile_dismiss_employees` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL COMMENT '节点id',
	`contact_name` VARCHAR (32) COMMENT '姓名',
	`employee_status` TINYINT NOT NULL COMMENT '离职前状态',
	`department` VARCHAR (32) COMMENT '部门',
	`check_in_time` DATE COMMENT '入职日期',
	`dismiss_time` DATE COMMENT '离职日期',
	`dismiss_type` TINYINT COMMENT '离职类型',
	`dismiss_reason` VARCHAR(64) COMMENT '离职原因',
	`dismiss_remarks` VARCHAR (256) COMMENT '备注',
	`detail_id` BIGINT NOT NULL COMMENT '成员detailId',
	`create_time` datetime COMMENT '创建时间',
	`operator_uid` BIGINT COMMENT '操作人id',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;