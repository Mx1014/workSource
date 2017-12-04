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

-- merge from asset-org by xiongying20171129
CREATE TABLE `eh_community_organization_detail_display` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `detail_flag` TINYINT NOT NULL DEFAULT 0,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_communities` ADD COLUMN `community_number` VARCHAR(64) COMMENT '项目编号';
ALTER TABLE `eh_buildings` ADD COLUMN `building_number` VARCHAR(64) COMMENT '楼栋编号';


ALTER TABLE `eh_enterprise_customers` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';
ALTER TABLE `eh_contracts` ADD COLUMN `version` VARCHAR(32) COMMENT '版本号';
ALTER TABLE `eh_contracts` ADD COLUMN `building_rename` VARCHAR(64) COMMENT '房间别名';

ALTER TABLE `eh_contracts` ADD COLUMN `namespace_contract_type` VARCHAR(128);
ALTER TABLE `eh_contracts` ADD COLUMN `namespace_contract_token` VARCHAR(128);


-- merge from forum-2.6 start 201711291416
-- 论坛发布类型  add by yanjun 20171122
CREATE TABLE `eh_forum_service_types` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `module_type` tinyint(4) DEFAULT NULL COMMENT 'module type, 1-forum,2-activity......',
  `category_id` bigint(20) DEFAULT NULL,
  `service_type` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `sort_num` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 热门标签，增加模块类型，1-论坛、2-活动、3-公告...等等  add by yanjun 20171123
ALTER TABLE `eh_hot_tags` ADD COLUMN `module_type`  tinyint(4) DEFAULT NULL COMMENT 'module type, 1-forum 2-activity...........';
-- merge from forum-2.6 end 201711291416



-- merge from  fixPm-1.0   by jiarui  20171130
-- 增加设备字段  by jiarui
ALTER TABLE `eh_equipment_inspection_equipments`
  ADD COLUMN `brand_name` varchar(1024) COMMENT 'brand_name',
  ADD COLUMN `construction_party` varchar(1024) COMMENT 'construction party',
  ADD COLUMN `discard_time` datetime COMMENT 'discard time ',
  ADD COLUMN `manager_contact` varchar(1024) ,
  ADD COLUMN `detail` varchar(1024) ,
  ADD COLUMN `factory_time` datetime ,
  ADD COLUMN `provenance` varchar(1024) ,
  ADD COLUMN `price` decimal  ,
  ADD COLUMN `buy_time` datetime ,
  ADD COLUMN `depreciation_years` bigint(10) COMMENT '折旧年限' ;

ALTER TABLE `eh_equipment_inspection_equipments`
  MODIFY COLUMN `category_id`  bigint(20) NULL DEFAULT 0 COMMENT 'reference to the id of eh_categories' AFTER `equipment_model`;

-- 选项表增加业务自定义枚举值字段
ALTER TABLE `eh_var_field_items` ADD COLUMN `business_value` TINYINT COMMENT 'the value defined in special business like status';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `business_value` TINYINT COMMENT 'the value defined in special business like status';
-- merge from  fixPm-1.0   by jiarui  20171130

-- merge from yuekongjian 1.0  by yanjun 201711301748  start

-- web页面“我的”里面的菜单表
CREATE TABLE `eh_me_web_menus` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `action_path` varchar(255) NOT NULL,
  `action_data` varchar(255) DEFAULT NULL,
  `icon_uri` varchar(255) DEFAULT NULL,
  `position_flag` tinyint(4) DEFAULT '1' COMMENT 'position, 1-NORMAL, 2-bottom',
  `sort_num` int(11) DEFAULT '0',
  `status` tinyint(4) DEFAULT '2' COMMENT '0: inactive, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- layout 增加广场的背景图片 add by yanjun 201711271158
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `bg_image_uri`  varchar(255) NULL;

-- added by Janson
ALTER TABLE `eh_door_access` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_access` ADD COLUMN `display_name` VARCHAR(128) NULL DEFAULT NULL AFTER `name`;
ALTER TABLE `eh_door_auth` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
-- ALTER TABLE `eh_door_auth_level` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_auth_logs` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_command` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclink_firmware` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclinks` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclink_undo_key` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;

-- merge from yuekongjian 1.0  by yanjun 201711301748  start

-- by wentian
ALTER TABLE `eh_payment_bill_groups_rules` ADD `brother_rule_id` BIGINT DEFAULT NULL COMMENT '兄弟账单组收费项id';

-- 二维码加路由   add by xq.tian  2017/11/15
ALTER TABLE eh_qrcodes ADD COLUMN `route_uri` VARCHAR(256) COMMENT 'route uri, like zl://xxx/xxx';
ALTER TABLE eh_qrcodes ADD COLUMN `handler` VARCHAR(32) COMMENT 'module handler';
ALTER TABLE eh_qrcodes ADD COLUMN `extra` TEXT COMMENT 'module handler';

ALTER TABLE eh_flow_cases ADD COLUMN `route_uri` VARCHAR(128) COMMENT 'route uri';

-- from club 3.2 start
-- 行业协会类型
CREATE TABLE `eh_industry_types` (
  `id` BIGINT(20) NOT NULL,
  `uuid` VARCHAR(128) NOT NULL,
  `namespace_id` INT(11) NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `create_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_guild_applies` (
  `id` BIGINT(20) NOT NULL,
  `uuid` VARCHAR(128) NOT NULL,
  `namespace_id` INT(11) NOT NULL,
  `group_id` BIGINT(22) NOT NULL,
  `applicant_uid` BIGINT(22) NOT NULL,
  `group_member_id` BIGINT(22) NOT NULL,
  `avatar` VARCHAR(255) DEFAULT NULL,
  `name` VARCHAR(255) DEFAULT NULL,
  `phone` VARCHAR(18) DEFAULT NULL,
  `email` VARCHAR(255) DEFAULT NULL,
  `organization_name` VARCHAR(255) DEFAULT NULL,
  `registered_capital` VARCHAR(255) DEFAULT NULL,
  `industry_type` VARCHAR(255) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_uid` BIGINT(22) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_group_settings` ADD COLUMN `club_type`  TINYINT(4) NOT NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;
-- 未加入俱乐部成员在俱乐部论坛的权限 0-不可见，1-可见，2-可交互
ALTER TABLE `eh_groups` ADD COLUMN `tourist_post_policy`  TINYINT(4) NULL DEFAULT 2 COMMENT '0-hide, 1-see only, 2-interact';
-- 俱乐部类型，普通俱乐部、行业协会
ALTER TABLE `eh_groups` ADD COLUMN `club_type`  TINYINT(4) NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club' ;

ALTER TABLE `eh_groups` ADD COLUMN `phone_number`  VARCHAR(18) NULL ;

ALTER TABLE `eh_groups`  ADD COLUMN `description_type`  TINYINT(4) NULL DEFAULT 0;

-- 拒绝理由
ALTER TABLE `eh_group_member_logs` ADD COLUMN `reject_text`  VARCHAR(255) NULL;

-- from club 3.2 end

-- 加表单关联字段  add by xq.tian  2017/11/20
ALTER TABLE eh_flows ADD COLUMN `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form id';
ALTER TABLE eh_flows ADD COLUMN `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form version';
ALTER TABLE eh_flows ADD COLUMN `form_update_time` DATETIME COMMENT 'form update time';

ALTER TABLE eh_flow_event_logs ADD COLUMN `extra` TEXT COMMENT 'extra data, json format';

ALTER TABLE eh_flow_condition_expressions ADD COLUMN `variable_extra1` VARCHAR(256) COMMENT 'variable 1 extra';
ALTER TABLE eh_flow_condition_expressions ADD COLUMN `variable_extra2` VARCHAR(256) COMMENT 'variable 2 extra';

-- added by nan.rong for approval-1.6
ALTER TABLE `eh_general_forms` ADD COLUMN `form_template_id` BIGINT COMMENT 'the id in eh_general_form_templates';
ALTER TABLE `eh_general_forms` ADD COLUMN `form_template_version` BIGINT COMMENT 'the version in eh_general_form_templates';
ALTER TABLE `eh_general_forms` ADD COLUMN `form_attribute` VARCHAR(128) DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE';
ALTER TABLE `eh_general_forms` ADD COLUMN `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be modified from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_forms` ADD COLUMN `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be deleted from desk, 0: no, 1: yes';

ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_template_id` BIGINT COMMENT 'the id in eh_general_approval_templates';
ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_template_version` BIGINT COMMENT 'the version in eh_general_approval_templates';
ALTER TABLE `eh_general_approvals` ADD COLUMN `approval_attribute` VARCHAR(128) DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE';
ALTER TABLE `eh_general_approvals` ADD COLUMN `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be modified from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_approvals` ADD COLUMN `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be deleted from desk, 0: no, 1: yes';
ALTER TABLE `eh_general_approvals` ADD COLUMN `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval';

CREATE TABLE `eh_general_approval_templates` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_id` BIGINT,
	`owner_type` VARCHAR(64),
	`organization_id` BIGINT NOT NULL DEFAULT 0,
	`module_id` BIGINT DEFAULT 0 COMMENT 'the module id',
	`module_type` VARCHAR(64),
	`project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),
  `form_template_id` BIGINT(20) NOT NULL DEFAULT 0 COMMENT 'The id of the template form',
  `support_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'APP:0, WEB:1, APP_WEB: 2',
  `approval_name` VARCHAR(128) NOT NULL,
  `approval_attribute` VARCHAR(128) DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE',
  `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be modified from desk, 0: no, 1: yes',
  `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be deleted from desk, 0: no, 1: yes',
  `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval',
  `update_time` DATETIME DEFAULT NULL COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `eh_general_form_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT '0',
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT DEFAULT 0 COMMENT 'the module id',
  `module_type` VARCHAR(64),
  `form_name` VARCHAR(64) NOT NULL,
  `version` BIGINT NOT NULL DEFAULT '0' COMMENT 'the version of the form template',
  `template_type` VARCHAR(128) NOT NULL COMMENT 'the type of template text',
  `template_text` TEXT COMMENT 'json 存放表单字段',
  `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be modified from desk, 0: no, 1: yes',
  `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be deleted from desk, 0: no, 1: yes',
  `update_time` DATETIME DEFAULT NULL COMMENT 'last update time',
  `create_time` DATETIME DEFAULT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- ended by nan.rong












-- added by wh punch-3.4
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `begin_time` DATETIME COMMENT ' 请假/加班 生效开始时间';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `end_time` DATETIME COMMENT ' 请假/加班 生效结束时间';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `duration` DOUBLE COMMENT ' 请假/加班 时长-可供计算';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `category_id` BIGINT COMMENT ' 请假类型';  
ALTER TABLE `eh_punch_exception_requests` ADD COLUMN `approval_attribute` VARCHAR(128) COMMENT 'DEFAULT,CUSTOMIZE'; 
ALTER TABLE `eh_punch_exception_requests` CHANGE `view_flag` `view_flag` TINYINT DEFAULT '1' COMMENT 'is view(0) not view(1)';

ALTER TABLE `eh_punch_exception_approvals` ADD COLUMN `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡';  

ALTER TABLE `eh_punch_logs` ADD COLUMN `approval_status` TINYINT DEFAULT NULL COMMENT '校正后的打卡状态 0-正常 null-没有异常校准';
ALTER TABLE `eh_punch_logs` ADD COLUMN `smart_alignment` TINYINT DEFAULT 0 COMMENT '只能校准状态 0-非校准 1-校准';
ALTER TABLE `eh_punch_day_logs` ADD COLUMN `smart_alignment` VARCHAR(128) DEFAULT NULL COMMENT '智能校准状态:1-未智能校准 0-未校准 例如:0;1/0;1/1/0/1';

-- 工作流业务类型字段 add by xq.tian 2017/22/21
ALTER TABLE eh_flow_service_types ADD COLUMN `owner_type` VARCHAR(64) COMMENT 'ownerType, e.g: EhOrganizations';
ALTER TABLE eh_flow_service_types ADD COLUMN `owner_id` BIGINT COMMENT 'ownerId, e.g: eh_organizations id';

-- 条件表达式的默认值  add by xq.tian  2017/11/20
ALTER TABLE eh_flow_condition_expressions MODIFY variable1 VARCHAR(64) NOT NULL DEFAULT '';
ALTER TABLE eh_flow_condition_expressions MODIFY variable2 VARCHAR(64) NOT NULL DEFAULT '';

ALTER TABLE eh_flow_condition_expressions MODIFY variable_type1 VARCHAR(64) NOT NULL DEFAULT '';
ALTER TABLE eh_flow_condition_expressions MODIFY variable_type2 VARCHAR(64) NOT NULL DEFAULT '';


-- by dengs, 20171120 已在线网执行的sql，放这里只是产生对应的persist包  (4.11.1已有)
-- ALTER TABLE eh_news_tag ADD COLUMN `category_id` BIGINT default 0;

-- 工作流条件字段 add by xq.tian 2017/22/24
ALTER TABLE eh_flow_conditions MODIFY next_node_id BIGINT;
ALTER TABLE eh_flow_conditions MODIFY next_node_level INTEGER;

-- add column by lqs 20171124
ALTER TABLE `eh_payment_users` ADD COLUMN `settlement_type` INTEGER NOT NULL DEFAULT 7 COMMENT '0-DAILY, 7-WEEKLY';

-- 表增加索引 add by xiongying20171128
ALTER TABLE eh_equipment_inspection_tasks ADD INDEX `equipment_id_uniqueIndex` (`equipment_id`) ;
ALTER TABLE eh_energy_date_statistics ADD INDEX `unionmeter_stat_uniqueIndex` (`meter_id`,`stat_date`) ;
ALTER TABLE eh_equipment_inspection_item_results ADD INDEX `task_log_uniqueIndex` (`task_log_id`) ;
