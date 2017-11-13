-- by dengs,问卷调查添加属性。2017.11.06
ALTER TABLE `eh_questionnaires` ADD COLUMN `organization_scope` TEXT COMMENT 'targetType是organization的时候，发布公司的列表' AFTER `user_scope`;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN user_scope MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN scope_sent_message_users MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN scope_resent_message_users MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN organization_scope MEDIUMTEXT;

DROP TABLE IF EXISTS `eh_archives_sticky_contacts`;
CREATE TABLE `eh_archives_sticky_contacts` (
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
	`contract_party_id` BIGINT COMMENT '合同主体',
	`check_in_time` DATE COMMENT '入职日期',
	`dismiss_time` DATE COMMENT '离职日期',
	`dismiss_type` TINYINT COMMENT '离职类型',
	`dismiss_reason` TINYINT COMMENT '离职原因',
	`dismiss_remarks` VARCHAR (256) COMMENT '备注',
	`detail_id` BIGINT NOT NULL COMMENT 'the id of member in eh_organization_member_details',
	`create_time` DATETIME COMMENT 'the time of data creating',
	`operator_uid` BIGINT COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

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

DROP TABLE IF EXISTS `eh_archives_configurations`;
CREATE TABLE `eh_archives_configurations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT'the id of organization',
	`operation_type` TINYINT NOT NULL COMMENT'the type of operation',
	`operation_time` DATE COMMENT 'the time to execute the operation',
  `operation_information` TEXT COMMENT 'information about the operation',
  `remind_time` DATETIME COMMENT 'time to send email to the corresponding member',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'pending, execution',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT COMMENT 'the id of the operator',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `eh_archives_logs`;
CREATE TABLE `eh_archives_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the organization',
	`detail_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the organization_member_detail',
	`operation_type` TINYINT NOT NULL COMMENT'the type of the operation',
	`operation_time` DATE NOT NULL COMMENT 'the time of the operation',
	`operation_category` TINYINT COMMENT'the category of the operation',
	`operation_reason` VARCHAR(64) COMMENT 'the reason of the operation',
	`operation_remark` VARCHAR(256) COMMENT 'the remark',
	`operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the operator',
	`operator_name` VARCHAR(64) NOT NULL DEFAULT 0 COMMENT 'the name of the operator',
	`create_time` DATETIME COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

DROP TABLE IF EXISTS `eh_archives_notifications`;
CREATE TABLE `eh_archives_notifications` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the organization',
	`notify_day` INTEGER COMMENT 'the day of sending emails',
	`notify_hour` INTEGER COMMENT 'the hour of sending emails',
	`notify_emails` TEXT COMMENT 'email targets',
	`operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the operator',
	`create_time` DATETIME COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET = utf8mb4;

-- eh_organization_member_details 表的字段添加
ALTER TABLE eh_organization_member_details CHANGE political_status political_flag VARCHAR(128) COMMENT '政治面貌';
ALTER TABLE eh_organization_member_details ADD COLUMN procreative VARCHAR(64) COMMENT '生育状况';
ALTER TABLE eh_organization_member_details ADD COLUMN ethnicity VARCHAR(128) COMMENT '民族';
ALTER TABLE eh_organization_member_details ADD COLUMN id_type VARCHAR(64) COMMENT '证件类型';
ALTER TABLE eh_organization_member_details ADD COLUMN id_expiry_date DATE COMMENT '证件有效期';
ALTER TABLE eh_organization_member_details ADD COLUMN degree VARCHAR(64) COMMENT '学历';
ALTER TABLE eh_organization_member_details ADD COLUMN graduation_school VARCHAR(256) COMMENT '毕业学校';
ALTER TABLE eh_organization_member_details ADD COLUMN graduation_time DATE COMMENT '毕业时间';
-- ALTER TABLE eh_organization_member_details ADD COLUMN region_code VARCHAR(64) COMMENT '手机区号';
ALTER TABLE eh_organization_member_details ADD COLUMN emergency_relationship VARCHAR(128) COMMENT '紧急联系人关系';
ALTER TABLE eh_organization_member_details ADD COLUMN department VARCHAR(256) COMMENT '部门';
ALTER TABLE eh_organization_member_details ADD COLUMN department_ids VARCHAR(256) COMMENT '部门Id';
ALTER TABLE eh_organization_member_details ADD COLUMN job_position VARCHAR(256) COMMENT '岗位';
ALTER TABLE eh_organization_member_details ADD COLUMN job_position_ids VARCHAR(256) COMMENT '岗位Id';
ALTER TABLE eh_organization_member_details ADD COLUMN job_level VARCHAR(256) COMMENT '职级';
ALTER TABLE eh_organization_member_details ADD COLUMN job_level_ids VARCHAR(256) COMMENT '职级Id';
ALTER TABLE eh_organization_member_details ADD COLUMN contact_short_token VARCHAR(128) COMMENT '短号';
ALTER TABLE eh_organization_member_details ADD COLUMN work_email VARCHAR(128) COMMENT '工作邮箱';
ALTER TABLE eh_organization_member_details ADD COLUMN contract_party_id BIGINT COMMENT '合同主体';
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


-- lei.lv 机构表加排序
ALTER TABLE `eh_organizations` ADD COLUMN `order` int(11) NULL DEFAULT '0' COMMENT 'order';

-- 用户管理1.4 add by yanjun 201711071007
ALTER TABLE `eh_user_organizations` ADD COLUMN `executive_tag`  tinyint(4) NULL, ADD COLUMN `position_tag`  varchar(128) NULL;

-- flow 加校验状态字段   add by xq.tian  2017/10/31
ALTER TABLE eh_flows ADD COLUMN `validation_status` TINYINT NOT NULL DEFAULT 2 COMMENT 'flow validation status';

-- 停车6.1 add by sw 20171108
CREATE TABLE `eh_parking_car_verifications` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `requestor_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the requestor is in',
  `requestor_enterprise_name` varchar(64) DEFAULT NULL COMMENT 'the enterprise name of plate owner',
  `requestor_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'requestor id',
  `plate_number` varchar(64) DEFAULT NULL,
  `plate_owner_name` varchar(64) DEFAULT NULL COMMENT 'the name of plate owner',
  `plate_owner_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of plate owner',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: queueing, 2: notified, 3: issued',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `flow_case_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'flow case id',
  `source_type` tinyint(4) DEFAULT NULL COMMENT '1: card request, 2: car verify',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- by dengs,园区快讯多入口，2017.11.13
ALTER TABLE eh_news_categories ADD COLUMN `entry_id` INTEGER;