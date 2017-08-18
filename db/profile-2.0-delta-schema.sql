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

ALTER TABLE eh_organization_member_details CHANGE dimission_time dismiss_time DATE;
ALTER TABLE eh_organization_member_details ADD COLUMN procreative DATE COMMENT '生育状况';
ALTER TABLE eh_organization_member_details ADD COLUMN ethnicity VARCHAR(128) COMMENT '民族';
ALTER TABLE eh_organization_member_details ADD COLUMN id_type VARCHAR(64) COMMENT '证件类型';
ALTER TABLE eh_organization_member_details ADD COLUMN id_expirt_date DATE COMMENT '证件有效期';
ALTER TABLE eh_organization_member_details ADD COLUMN education VARCHAR(64) COMMENT '学历';
ALTER TABLE eh_organization_member_details ADD COLUMN graduation_school VARCHAR(256) COMMENT '毕业学校';
ALTER TABLE eh_organization_member_details ADD COLUMN graduation_time DATE COMMENT '毕业时间';
ALTER TABLE eh_organization_member_details ADD COLUMN emergency_relationship VARCHAR(128) COMMENT '紧急联系人关系';
ALTER TABLE eh_organization_member_details ADD COLUMN work_email VARCHAR(64) COMMENT '工作邮箱';
ALTER TABLE eh_organization_member_details ADD COLUMN work_place BIGINT COMMENT '工作地点';
ALTER TABLE eh_organization_member_details ADD COLUMN contract_party BIGINT COMMENT '合同主体';
ALTER TABLE eh_organization_member_details ADD COLUMN work_start_time DATE COMMENT '参加工作日期';
ALTER TABLE eh_organization_member_details ADD COLUMN contract_start_time DATE COMMENT '合同开始日期';
ALTER TABLE eh_organization_member_details ADD COLUMN contract_end_time DATE COMMENT '合同终止日期';
ALTER TABLE eh_organization_member_details ADD COLUMN reg_residence_type VARCHAR(64) COMMENT '户籍类型';
