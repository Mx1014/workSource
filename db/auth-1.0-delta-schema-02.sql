-- by dengs,问卷调查添加属性。2017.11.06
ALTER TABLE `eh_questionnaires` ADD COLUMN `organization_scope` TEXT COMMENT 'targetType是organization的时候，发布公司的列表' AFTER `user_scope`;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN user_scope MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN scope_sent_message_users MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN scope_resent_message_users MEDIUMTEXT;
ALTER TABLE `eh_questionnaires` MODIFY COLUMN organization_scope MEDIUMTEXT;

-- added by R. 人事档案与表单字段组 start 2017.11.13
-- DROP TABLE IF EXISTS `eh_archives_sticky_contacts`;
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

-- DROP TABLE IF EXISTS `eh_archives_dismiss_employees`;
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

-- DROP TABLE IF EXISTS `eh_general_form_groups`;
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

-- DROP TABLE IF EXISTS `eh_archives_forms`;
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

-- DROP TABLE IF EXISTS `eh_archives_configurations`;
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

-- DROP TABLE IF EXISTS `eh_archives_logs`;
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

-- DROP TABLE IF EXISTS `eh_archives_notifications`;
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
-- added by R. 人事档案与表单字段组 end 2017.11.13

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

-- by wentian 物业缴费模块2.0
-- payment_wentian_v2 new sql after 4.10.4

ALTER TABLE `eh_payment_charging_standards_scopes` ADD COLUMN `namespace_id` INTEGER DEFAULT 0;
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `bill_group_rule_id` BIGINT DEFAULT NULL;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `bill_group_rule_id` BIGINT DEFAULT NULL;
ALTER TABLE `eh_payment_charging_standards_scopes` ADD COLUMN  `brother_standard_id` BIGINT DEFAULT NULL COMMENT '兄弟收费标准id，联动效果';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN  `brother_group_id` BIGINT DEFAULT NULL COMMENT '兄弟账单组id，联动效果';
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是';
ALTER TABLE `eh_payment_bills` ADD COLUMN `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是';
ALTER TABLE `eh_payment_contract_receiver` ADD COLUMN `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是';
DROP TABLE IF EXISTS `eh_payment_notice_config`;
CREATE TABLE `eh_payment_notice_config` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT NULL,
  `owner_type` VARCHAR(255) DEFAULT NULL,
  `owner_id` BIGINT DEFAULT NULL,
  `notice_day_before` INTEGER DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 能耗3.0的表结构 by 熊颖
ALTER TABLE eh_energy_meters ADD COLUMN `cost_formula_source` TINYINT DEFAULT '0' COMMENT '0: 能耗设置, 1: 缴费模块';
ALTER TABLE eh_energy_meter_setting_logs ADD COLUMN `formula_source` TINYINT DEFAULT '0' COMMENT '0: 能耗设置, 1: 缴费模块';
ALTER TABLE eh_energy_meter_reading_logs ADD COLUMN `task_id` BIGINT DEFAULT '0';

-- 表计关联门牌
CREATE TABLE `eh_energy_meter_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `meter_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_groups',
  `building_id` BIGINT NOT NULL DEFAULT '0',
  `building_name` VARCHAR(128) DEFAULT NULL,
  `address_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_addresses',
  `apartment_name` VARCHAR(128) DEFAULT NULL,
  `apartment_floor` VARCHAR(16) DEFAULT NULL,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT DEFAULT NULL COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'redundant auditing info',
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `meter_address_meter_id` (`meter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 能耗抄表计划
CREATE TABLE `eh_energy_plans` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the plan, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the plan, community, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the plan',
  `name` VARCHAR(1024),
  `repeat_setting_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_repeat_settings',
  `notify_tick_minutes` INTEGER COMMENT '提前多少分钟',
  `notify_tick_unit` TINYINT COMMENT '提醒时间显示单位',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: not completed, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_plan_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `plan_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_plans',
  `group_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_energy_plan_meter_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `plan_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_plans',
  `meter_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_energy_meters',
  `default_order` INTEGER DEFAULT 0,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 按计划生成工单
CREATE TABLE `eh_energy_meter_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, community, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `plan_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_plans',
  `meter_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_meters',
  `executive_start_time` DATETIME,
  `executive_expire_time` DATETIME,
  `last_task_reading` DECIMAL(10,1),
  `reading` DECIMAL(10,1),
  `generate_payment_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未生成, 1: 已生成',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未抄, 1: 已抄',
  `default_order` INTEGER DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  KEY `status` (`status`),
  KEY `target_id` (`target_id`),
  KEY `executive_expire_time` (`executive_expire_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 操作记录表 目前只有计划操作记录
CREATE TABLE `eh_energy_meter_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'plan, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record operator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 缴费、客户里面增加计价条款的设置
CREATE TABLE `eh_default_charging_item_properties` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `default_charging_item_id` BIGINT NOT NULL COMMENT 'id of eh_contract_charging_items',
  `property_type` TINYINT COMMENT '0: community; 1: building; 2: apartment',
  `property_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_default_charging_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the charging, organizationowner,asset, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `charging_item_id` BIGINT COMMENT '收费项',
  `charging_standard_id` BIGINT COMMENT '收费标准',
  `formula` VARCHAR(1024),
  `formula_type` TINYINT COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` TINYINT,
  `late_fee_standard_id` BIGINT COMMENT '滞纳金标准',
  `charging_variables` VARCHAR(1024) COMMENT '计费金额参数 json: {"variables":[{"variableIdentifier":"22","variableName":"面积","variableValue":"960.00"}]}',
  `charging_start_time` DATETIME,
  `charging_expired_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 打卡加入当前生效版本号
-- ALTER TABLE eh_punch_rules ADD COLUMN `version_code` INT DEFAULT 0 COMMENT '当前生效版本号';

-- 维护uniongroup现在使用的是哪个version
-- drop TABLE `eh_uniongroup_version`;
CREATE TABLE `eh_uniongroup_version` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `enterprise_id` BIGINT(20) DEFAULT '0',
  `group_type` VARCHAR(32) DEFAULT NULL COMMENT 'SalaryGroup,PunchGroup', 
  `current_version_code` INT(11) DEFAULT '0' COMMENT '当前使用的版本号 从1开始 , 0默认是config版本', 
  `operator_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- by dengs,问卷调查1.1
ALTER TABLE `eh_questionnaires` ADD COLUMN `cut_off_time` DATETIME DEFAULT now() COMMENT '问卷截止日期'  AFTER `publish_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `user_scope` TEXT COMMENT '需要填写的问卷调查的用户[userid,nickname|userid,nickname]' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `support_share` TINYINT COMMENT '是否支持分享, 0:不支持分享,2:支持分享' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `support_anonymous` TINYINT COMMENT '是否支持匿名, 0:不支持匿名,2:支持匿名' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `target_type` VARCHAR(32) DEFAULT 'organization' COMMENT '调查对象 organization:企业 user:个人' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `poster_uri` VARCHAR(1024) COMMENT '问卷调查的封面uri' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `target_user_num` INTEGER COMMENT '目标用户收集数量' AFTER `cut_off_time`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `scope_sent_message_users` TEXT COMMENT '已发送消息的用户列表（发布时发送的消息用户）' AFTER `target_user_num`;
ALTER TABLE `eh_questionnaires` ADD COLUMN `scope_resent_message_users` TEXT COMMENT '问卷到期前发送消息的用户列表（问卷到期一天前发送的消息用户）' AFTER `target_user_num`;


ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `target_from` TINYINT COMMENT '用户来源（1:app，2:wx）' AFTER `target_name`;
ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `target_phone` VARCHAR(128) COMMENT '用户电话' AFTER `target_name`;
ALTER TABLE `eh_questionnaire_answers` ADD COLUMN `anonymous_flag` TINYINT DEFAULT 0 COMMENT '是否匿名回答, 0:不是匿名回答,2:是匿名回答' AFTER `target_name`;

-- 问卷调查范围表
-- DROP TABLE IF EXISTS  `eh_questionnaire_ranges`;
CREATE TABLE `eh_questionnaire_ranges` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`questionnaire_id` BIGINT NOT NULL COMMENT '关联问卷调查的id',
  `community_id` BIGINT COMMENT '园区id，查询楼栋（range_type=building）下的企业的时候，使用的是楼栋的名称查询，这里必须保存community一起查询才正确。',
  `range_type` VARCHAR(64) COMMENT 'community_all(项目),community_authenticated(项目下已认证的用户),community_unauthorized(未认证),building(楼栋),enterprise(企业),user 范围类型',
  `range` VARCHAR(512) COMMENT '对应项目id,楼栋名称，企业ID，用户id',
	`range_description` VARCHAR(1024) COMMENT '范围描述信息，用于显示在问卷详情页',
	`rid` BIGINT COMMENT '范围为building的时候，存buildingid，给web做逻辑，后端没有必要存储',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. draft, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 版本号  add by xq.tian  2017/10/26
ALTER TABLE eh_version_urls ADD COLUMN version_encoded_value BIGINT NOT NULL DEFAULT 0;


-- merge from forum-2.4 add by yanjun 201710311836

CREATE TABLE `eh_forum_categories` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `forum_id` bigint(20) NOT NULL COMMENT 'forum id',
  `entry_id` bigint(20) NOT NULL COMMENT 'entry id',
  `name` varchar(255) DEFAULT NULL,
  `activity_entry_id` bigint(20) DEFAULT '0' COMMENT 'activity entry id',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 是否支持评论功能
CREATE TABLE `eh_interact_settings` (
  `id` bigint(20) NOT NULL,
  `uuid` varchar(128) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `forum_id` bigint(20) NOT NULL,
  `type` varchar(32) NOT NULL COMMENT 'forum, activity, announcement',
  `entry_id` bigint(20) DEFAULT NULL,
  `interact_flag` tinyint(4) NOT NULL COMMENT 'support interact, 0-no, 1-yes',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_forum_posts` ADD COLUMN `forum_entry_id`  bigint(20) NULL DEFAULT 0 COMMENT 'forum_category  entry_id' ;

ALTER TABLE `eh_forum_posts` ADD COLUMN `interact_flag`  tinyint(4) NOT NULL DEFAULT 1 COMMENT 'support interact, 0-no, 1-yes' ;

ALTER TABLE `eh_forum_posts` ADD COLUMN `stick_time`  datetime NULL;
ALTER TABLE `eh_activities` ADD COLUMN `stick_time`  datetime NULL;

-- merge from forum-2.4 add by yanjun 201710311836