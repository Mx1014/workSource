DROP TABLE IF EXISTS `eh_archives_contacts_sticky`;
CREATE TABLE `eh_archives_contacts_sticky` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL COMMENT 'organizationId',
	`detail_id` BIGINT NOT NULL COMMENT 'the id of member in eh_organization_member_details',
  `create_time` DATETIME COMMENT 'the time of data creating',
  `update_time` DATETIME COMMENT 'the time of data updating',
  `operator_uid` BIGINT COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `eh_archives_dismiss_employees`;
CREATE TABLE `eh_archives_dismiss_employees` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL COMMENT 'organizationId',
	`contact_name` VARCHAR (32) COMMENT 'the name of the employee',
	`employee_status` TINYINT NOT NULL COMMENT 'the status of the employee before dismissing',
	`department` VARCHAR (32) COMMENT 'department',
	`check_in_time` DATE COMMENT '入职日期',
	`dismiss_time` DATE COMMENT '离职日期',
	`dismiss_type` TINYINT COMMENT '离职类型',
	`dismiss_reason` VARCHAR(64) COMMENT '离职原因',
	`dismiss_remarks` VARCHAR (256) COMMENT '备注',
	`detail_id` BIGINT NOT NULL COMMENT 'the id of member in eh_organization_member_details',
	`create_time` DATETIME COMMENT 'the time of data creating',
	`operator_uid` BIGINT COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

/*DROP TABLE IF EXISTS `eh_archives_organization_places`;
CREATE TABLE `eh_archives_organization_places` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL COMMENT 'organizationId',
	`detail_id` BIGINT NOT NULL COMMENT 'the id of member in eh_organization_member_details',
	`update_time` DATETIME COMMENT 'the time of data creating',
	`operator_uid` BIGINT COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;*/

DROP TABLE IF EXISTS `eh_general_form_groups`;
CREATE TABLE `eh_general_form_groups` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`form_origin_id` BIGINT DEFAULT 0 COMMENT 'the id of the original form',
	`form_version` BIGINT DEFAULT 0 COMMENT 'the current using version',
	`template_type` VARCHAR(128) NOT NULL COMMENT 'the type of template text',
	`template_text` TEXT COMMENT 'json 存放表单字段组',
	`update_time` DATETIME COMMENT 'last update time',
	`create_time` DATETIME COMMENT 'the time of data creating',
	`operator_uid` BIGINT COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `eh_archives_forms`;
CREATE TABLE `eh_archives_forms` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL COMMENT'the id of organization',
  `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the original form',
  `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'the current using version',
  `status` TINYINT NOT NULL COMMENT 'invalid, config, running',
  `update_time` DATETIME COMMENT 'last update time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

ALTER TABLE eh_organization_member_details CHANGE dimission_time dismiss_time DATE;
ALTER TABLE eh_organization_member_details ADD COLUMN procreative DATE COMMENT '生育状况';
ALTER TABLE eh_organization_member_details ADD COLUMN ethnicity VARCHAR(128) COMMENT '民族';
ALTER TABLE eh_organization_member_details ADD COLUMN id_type VARCHAR(64) COMMENT '证件类型';
ALTER TABLE eh_organization_member_details ADD COLUMN id_expirt_date DATE COMMENT '证件有效期';
ALTER TABLE eh_organization_member_details ADD COLUMN degree VARCHAR(64) COMMENT '学历';
ALTER TABLE eh_organization_member_details ADD COLUMN graduation_school VARCHAR(256) COMMENT '毕业学校';
ALTER TABLE eh_organization_member_details ADD COLUMN graduation_time DATE COMMENT '毕业时间';
ALTER TABLE eh_organization_member_details ADD COLUMN region_code VARCHAR(64) COMMENT '手机区号';
ALTER TABLE eh_organization_member_details ADD COLUMN emergency_relationship VARCHAR(128) COMMENT '紧急联系人关系';
ALTER TABLE eh_organization_member_details ADD COLUMN department VARCHAR(256) COMMENT '部门';
ALTER TABLE eh_organization_member_details ADD COLUMN job_position VARCHAR(256) COMMENT '职务/岗位';
ALTER TABLE eh_organization_member_details ADD COLUMN report_target VARCHAR(128) COMMENT '汇报对象';
ALTER TABLE eh_organization_member_details ADD COLUMN contact_short_token VARCHAR(128) COMMENT '短号';
ALTER TABLE eh_organization_member_details ADD COLUMN work_email VARCHAR(128) COMMENT '工作邮箱';
ALTER TABLE eh_organization_member_details ADD COLUMN work_place BIGINT COMMENT '工作地点';
ALTER TABLE eh_organization_member_details ADD COLUMN contract_id BIGINT COMMENT '合同主体';
ALTER TABLE eh_organization_member_details ADD COLUMN work_start_time DATE COMMENT '参加工作日期';
ALTER TABLE eh_organization_member_details ADD COLUMN contract_start_time DATE COMMENT '合同开始日期';
ALTER TABLE eh_organization_member_details ADD COLUMN contract_end_time DATE COMMENT '合同终止日期';
ALTER TABLE eh_organization_member_details ADD COLUMN salary_card_bank VARCHAR(64) COMMENT '开户行';
ALTER TABLE eh_organization_member_details ADD COLUMN reg_residence_type VARCHAR(64) COMMENT '户籍类型';
ALTER TABLE eh_organization_member_details ADD COLUMN id_photo TEXT COMMENT '身份证照片';
ALTER TABLE eh_organization_member_details ADD COLUMN visa_photo TEXT COMMENT '一寸免冠照';
ALTER TABLE eh_organization_member_details ADD COLUMN life_photo TEXT COMMENT '生活照';
ALTER TABLE eh_organization_member_details ADD COLUMN entry_form TEXT COMMENT '入职登记表';
ALTER TABLE eh_organization_member_details ADD COLUMN graduation_certificate TEXT COMMENT '毕业证书';
ALTER TABLE eh_organization_member_details ADD COLUMN degree_certificate TEXT COMMENT '学位证书';
ALTER TABLE eh_organization_member_details ADD COLUMN contract_certificate TEXT COMMENT '劳动合同';
