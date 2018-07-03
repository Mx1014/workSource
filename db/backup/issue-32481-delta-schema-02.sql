-- 物品放行 1.1 新增配置表
-- by shiheng.ma
CREATE TABLE `eh_relocation_configs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL COMMENT 'attachment object owner type',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'owner id',
	`agreement_flag` tinyint(4) DEFAULT 0 COMMENT '0: inactive, 1: active',
	`agreement_content` text COMMENT '协议内容',
	`tips_flag` tinyint(4) DEFAULT 0 COMMENT '0: inactive, 1: active',
	`tips_content` varchar(100) DEFAULT NULL COMMENT '提示内容',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
	`operator_uid` bigint(20) NOT NULL DEFAULT '0',
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 物品放行 1.1 增加新字段 用于小区场景
-- by shiheng.ma 20180620
alter table eh_relocation_requests add column org_owner_type_id BIGINT(20) DEFAULT NULL COMMENT '客户类型（小区场景）';



-- Designer: zhiwei zhang
-- Description: ISSUE#28363 会议管理V1.0

CREATE TABLE `eh_meeting_rooms` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
    `organization_id` BIGINT NOT NULL COMMENT '总公司ID',
    `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations，表示会议室归属的企业',
    `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID  会议室归属的分公司',
    `name` VARCHAR(128) NOT NULL COMMENT '会议室名称',
    `seat_count` INTEGER NOT NULL COMMENT '可容纳座位数',
    `description` VARCHAR(512) COMMENT '描述',
    `open_begin_time` TIME NOT NULL COMMENT '会议室可预订的起始时间',
    `open_end_time` TIME NOT NULL COMMENT '会议室可预订的结束时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:  0: DELETED 删除  1:CLOSED 不可用  2 : OPENING 可用',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    `operator_name` VARCHAR(64) NULL COMMENT '操作人姓名',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `u_eh_namespace_owner_name` (`namespace_id` , `organization_id`, `owner_type` , `owner_id` , `name`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议室资源管理表';


CREATE TABLE `eh_meeting_reservations` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
    `organization_id` BIGINT NOT NULL COMMENT '总公司ID',
    `subject` VARCHAR(256)  COMMENT '会议主题',
    `content` VARCHAR(1024) COMMENT '会议详细内容',
    `meeting_room_name` VARCHAR(128) NULL COMMENT '会议室名称，预约会议室后保存会议室名称，后期该值不随着会议室编辑而改变',
    `meeting_room_seat_count` INTEGER NULL COMMENT '可容纳座位数，预约会议室后保存会议室名称，后期该值不随着会议室编辑而改变',
    `meeting_room_id` BIGINT NULL COMMENT '会议室id,id of eh_meeting_rooms',
    `meeting_sponsor_user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '会议发起人的user_id',
    `meeting_sponsor_detail_id` BIGINT NOT NULL COMMENT '会议发起人的detail_id',
    `meeting_sponsor_name` VARCHAR(64) NOT NULL COMMENT '会议发起人的姓名',
    `meeting_recorder_user_id` BIGINT NULL COMMENT '会议纪要人user_id',
    `meeting_recorder_detail_id` BIGINT NULL COMMENT '会议纪要人detail_id',
    `meeting_recorder_name` VARCHAR(64) NULL COMMENT '会议纪要人的姓名',
    `invitation_user_count` INT COMMENT '会议受邀人数',
    `meeting_date` DATE NOT NULL COMMENT '会议预定日期',
    `expect_begin_time` DATETIME NOT NULL COMMENT '预计开始时间（预订会议室的时间）',
    `expect_end_time` DATETIME NOT NULL COMMENT '预计结束时间（预订会议室的时间）',
    `lock_begin_time` DATETIME NOT NULL COMMENT '实际锁定开始时间',
    `lock_end_time` DATETIME NOT NULL COMMENT '实际锁定结束时间',
    `act_begin_time` DATETIME NULL COMMENT '实际开始时间，只有用户操作了开始会议才有值',
    `act_end_time` DATETIME NULL COMMENT '实际结束时间，只有用户操作了结束会议才有值',
    `system_message_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启系统消息通知：0-关闭消息通知 1-开启消息通知',
    `email_message_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启邮件通知：0-关闭邮件通知 1-开启邮件通知',
    `act_remind_time` DATETIME NULL COMMENT '实际发出提醒的时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0:DELETED 删除，1:时间锁定， 2:CANCELED 取消,3:NORMAL 正常状态',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    PRIMARY KEY (`id`),
    INDEX `i_eh_namespace_organization_id` (`namespace_id`,`organization_id`),
    INDEX `i_eh_meeting_date`(`meeting_date`),
    INDEX `i_eh_meeting_room_id` (`meeting_room_id`),
    INDEX `i_eh_meeting_sponsor_detail_id` (`meeting_sponsor_detail_id`),
    INDEX `i_eh_meeting_recorder_detail_id` (`meeting_recorder_detail_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议室预约表';


CREATE TABLE `eh_meeting_invitations` (
    `id` BIGINT NOT NULL,
    `meeting_reservation_id` BIGINT NOT NULL COMMENT '会议预约eh_meeting_reservations的id',
    `source_type` VARCHAR(45) NOT NULL COMMENT '机构或者个人：ORGANIZATION OR MEMBER_DETAIL',
    `source_id` BIGINT NOT NULL COMMENT '机构id或员工detail_id',
    `source_name` VARCHAR(64) NOT NULL COMMENT '机构名称或者员工的姓名',
    `role_type` VARCHAR(16) NOT NULL COMMENT '参会人或抄送人',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    PRIMARY KEY (`id`),
    INDEX `i_eh_meeting_reservation_id` (`meeting_reservation_id`),
    INDEX `i_eh_source_id`(`source_type`,`source_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议邀请清单，即参会人和抄送人清单';


CREATE TABLE `eh_meeting_records` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `meeting_reservation_id` BIGINT NOT NULL COMMENT '会议预约ID，id of eh_meeting_reservations',
    `meeting_subject` VARCHAR(256)  COMMENT '会议主题，冗余字段，用于纪要列表展示主题名称',
    `content` TEXT COMMENT '会议纪要详细内容',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `operate_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    `operator_name` VARCHAR(64) NULL COMMENT '操作人姓名',
    PRIMARY KEY (`id`),
    INDEX `i_eh_meeting_reservation_id` (`meeting_reservation_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='会议纪要表';


-- End by: zhiwei zhang

-- Designer: wuhan
-- Description:  考勤4.2


-- 考勤月报表
CREATE TABLE `eh_punch_month_reports` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `punch_month` VARCHAR (8) DEFAULT NULL COMMENT 'yyyymm',
  `owner_type` VARCHAR (128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id',
  `process` INT COMMENT '进度百分比',
  `error_info` VARCHAR (200) COMMENT '错误信息(如果归档错误)',
  `status` TINYINT COMMENT '状态:0-创建更新中 1-创建完成 2-已归档',
  `creator_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `create_time` DATETIME DEFAULT NULL,
  `punch_member_number` INT COMMENT '考勤人数',
  `filer_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `file_time` DATETIME DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  `creator_Name` VARCHAR (128) DEFAULT NULL COMMENT '创建者姓名',
  `filer_Name` VARCHAR (128) DEFAULT NULL COMMENT '归档者姓名',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '考勤月报表' ;

ALTER TABLE `eh_punch_statistics` CHANGE status_list status_list TEXT COMMENT '校正后状态列表(月初到月末)';
-- ALTER TABLE `eh_punch_statistics` ADD COLUMN month_report_id BIGINT COMMENT 'eh_punch_month_reports id';
-- ALTER TABLE `eh_punch_statistics` ADD INDEX i_eh_report_id(`month_report_id`)  ;
-- end by wuhan


-- 审批3.0 start by ryan.
-- 审批类型组表
-- DROP TABLE IF EXISTS `eh_enterprise_approval_groups`;
CREATE TABLE `eh_enterprise_approval_groups` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`name` VARCHAR(64) NOT NULL COMMENT 'name of the approval group',
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0. inactive, 1.active',
	`group_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT, CUSTOMIZE',
	`approval_icon` VARCHAR(1024) COMMENT 'the default icon that belongs to the group',
	`operator_uid` BIGINT,
	`operator_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_general_approvals` ADD COLUMN `integral_tag1` BIGINT NOT NULL DEFAULT 0 AFTER `default_order`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `integral_tag2` BIGINT NOT NULL DEFAULT 0 AFTER `integral_tag1`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `integral_tag3` BIGINT NOT NULL DEFAULT 0 AFTER `integral_tag2`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag1` VARCHAR(128) AFTER `integral_tag3`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag2` VARCHAR(128) AFTER `string_tag1`;
ALTER TABLE `eh_general_approvals` ADD COLUMN `string_tag3` VARCHAR(128) AFTER `string_tag2`;

RENAME TABLE `eh_general_approval_templates` to `eh_enterprise_approval_templates`;
ALTER TABLE `eh_enterprise_approval_templates` ADD COLUMN `group_id` BIGINT NOT NULL DEFAULT 5 COMMENT 'the enterprise group id' AFTER `approval_name`;
ALTER TABLE `eh_enterprise_approval_templates` ADD COLUMN `approval_remark` VARCHAR(256) COMMENT 'the remark of the approval' AFTER `approval_name`;
-- 审批3.0 end by ryan.

-- 人事2.7 start by ryan.
-- ALTER TABLE `eh_archives_logs` ADD COLUMN `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0-cancel,1-pending,2-finish' AFTER `operation_remark` ;

-- DROP TABLE IF EXISTS `eh_archives_operational_configurations`;
CREATE TABLE `eh_archives_operational_configurations` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`organization_id` BIGINT NOT NULL DEFAULT '0',
  `detail_id` BIGINT NOT NULL COMMENT 'the detail id that belongs to the employee which is the change target',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of operation',
  `operation_date` DATE COMMENT 'the date of executing the operation',
  `additional_info` TEXT COMMENT 'the addition information for the operation',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-cancel, 1-pending, 2-complete',
  `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'the id of the operator',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_archives_operational_logs`;
CREATE TABLE `eh_archives_operational_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the log',
  `namespace_id` INT NOT NULL DEFAULT '0',
  `organization_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the organization',
  `detail_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the detail id that belongs to the employee',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of the operate',
  `operation_time` DATE NOT NULL COMMENT 'the time of the operate',
  `string_tag1` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag2` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag3` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag4` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag5` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag6` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `operator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
  `operator_name` VARCHAR(64) NOT NULL DEFAULT '0' COMMENT 'the id of the operator',
  `create_time` DATETIME DEFAULT NULL COMMENT 'create time',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 人事2.7 end by ryan.

-- 通用脚本
-- ADD BY 梁燕龙
-- issue-26754 敏感词日志记录
CREATE TABLE `eh_sensitive_filter_record` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `sensitive_words` VARCHAR(128) COMMENT '敏感词',
  `module_id` BIGINT COMMENT '模块ID',
  `community_id` BIGINT COMMENT '项目ID',
  `creator_uid` BIGINT COMMENT '记录发布人userId' ,
  `creator_name` VARCHAR(32) COMMENT '发布人姓名',
  `phone` VARCHAR(128) COMMENT '发布人手机号',
  `publish_time` DATETIME COMMENT '记录发布时间' ,
  `text` TEXT COMMENT '文本内容',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT = '敏感词过滤日志表';
-- END BY 梁燕龙


-- 通用脚本
-- ADD BY 梁燕龙
-- issue-30013 初始化短信白名单配置项
-- 短信白名单 #30013
CREATE TABLE `eh_phone_white_list` (
	`id` BIGINT NOT NULL COMMENT '主键',
	`namespace_id` INT NOT NULL DEFAULT 0 COMMENT '域空间',
	`phone_number` VARCHAR(128) NOT NULL COMMENT '白名单手机号码',
	`creator_uid` BIGINT COMMENT '记录创建人userID',
	`create_time` DATETIME COMMENT '记录创建时间',
	PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '短信白名单';
-- END BY 梁燕龙

-- 通用脚本
-- ADD BY 黄良铭
-- issue-30013 初始化短信白名单配置项
-- 20180522-huangliangming-配置项管理-#30016
-- 创建配置项信息变更记录表
CREATE TABLE `eh_configurations_record_change` (
  `id` INT(11)  NOT NULL COMMENT '主键',
  `namespace_id` INT(11) NOT NULL COMMENT '域空间ID',
  `conf_pre_json` VARCHAR(1024)  COMMENT '变动前信息JSON字符串',
  `conf_aft_json` VARCHAR(1024)  COMMENT '变动后信息JSON字符串',
  `record_change_type` INT(3) COMMENT '变动类型。0，新增；1，修改；3，删除',
  `operator_uid` BIGINT(20)   COMMENT '操作人userId',
  `operate_time` DATETIME    COMMENT '操作时间',
  `operator_ip` VARCHAR(50)   COMMENT '操作者的IP地址',

  PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '配置项信息变更记录表';

-- 配置项信息表新增一列（字段 ） is_readyonly
ALTER  TABLE eh_configurations  ADD  is_readonly  INT(3)  COMMENT '是否只读：1，是 ；null 或其他值为 否';
-- END BY 黄良铭

--
-- 通用脚本
-- ADD BY xq.tian  2018/06/15
-- #30750 代码仓库管理 v1.0
--
CREATE TABLE `eh_gogs_repos` (
  `id` BIGINT,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the module id',
  `owner_type` VARCHAR(64) DEFAULT NULL,
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `repo_type` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) DEFAULT NULL COMMENT 'name',
  `full_name` VARCHAR(512) DEFAULT NULL COMMENT 'full name',
  `description` TEXT COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT '1' COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME(3) DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `update_time` DATETIME(3) DEFAULT NULL,
  `update_uid` BIGINT DEFAULT NULL,
  `string_tag1` VARCHAR(128) DEFAULT NULL,
  `string_tag2` VARCHAR(128) DEFAULT NULL,
  `string_tag3` VARCHAR(128) DEFAULT NULL,
  `string_tag4` VARCHAR(128) DEFAULT NULL,
  `string_tag5` VARCHAR(128) DEFAULT NULL,
  `integral_tag1` BIGINT NOT NULL DEFAULT '0',
  `integral_tag2` BIGINT NOT NULL DEFAULT '0',
  `integral_tag3` BIGINT NOT NULL DEFAULT '0',
  `integral_tag4` BIGINT NOT NULL DEFAULT '0',
  `integral_tag5` BIGINT NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='gogs repository';

ALTER TABLE eh_flow_scripts ADD COLUMN last_commit VARCHAR(40) COMMENT 'repository last commit id';

-- #30750 END

-- 跟进信息增加字段 by jiarui 20180620
ALTER TABLE `eh_customer_trackings`  ADD COLUMN `contact_phone`  varchar(255) NULL AFTER `content`;
ALTER TABLE `eh_customer_trackings`  ADD COLUMN `visit_time_length`  decimal(10,2) NULL AFTER `contact_phone`;
ALTER TABLE `eh_customer_trackings`  ADD COLUMN `visit_person_name`  varchar(64) NULL AFTER `contact_phone`;


-- bydeng,20180622
CREATE TABLE `eh_parking_hubs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `hub_name` varchar(64) NOT NULL COMMENT 'hub的名称',
  `hub_mac` varchar(128) NOT NULL COMMENT 'hub的mac地址',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE eh_parking_spaces ADD COLUMN parking_hubs_id BIGINT;

-- 多入口 by dingjianmin  start
-- from asset_multi
CREATE TABLE `eh_service_module_app_mappings`(
  `id` BIGINT NOT NULL ,
  `app_origin_id_male` BIGINT NOT NULL COMMENT 'the origin id of app',
  `app_module_id_male` BIGINT NOT NULL COMMENT 'the module id of app',
  `app_origin_id_female` BIGINT NOT NULL COMMENT 'the origin id of app',
  `app_module_id_female` BIGINT NOT NULL COMMENT 'the module id of app',
  `create_time` DATETIME NOT NULL DEFAULT now(),
  `create_uid` BIGINT NOT NULL,
  `update_time` DATETIME NOT NULL DEFAULT now(),
  `update_uid` BIGINT DEFAULT NULL,
  UNIQUE KEY `origin_id_mapping` (`app_origin_id_male`, `app_origin_id_female`),
  UNIQUE KEY `i_origin_module` (`app_origin_id_male`, `app_module_id_male`),
  UNIQUE KEY `i_origin_module_reverse` (`app_origin_id_female`, `app_module_id_male`),
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'relation mappings among applications';

-- multiple entry category id for asset module by wentian
CREATE TABLE `eh_asset_app_categories`(
  `id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT now(),
  `create_uid` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `instance_flag` VARCHAR(1024) DEFAULT NULL ,
  UNIQUE KEY `i_category_id` (`category_id`),
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'relation mappings among applications';

-- 账单数据添加categoryId by wentian 2018/5/25
ALTER TABLE `eh_payment_bills` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id';

-- 账单催缴设置添加categoryId by wentian 2018/5/25
ALTER TABLE `eh_payment_notice_config` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id';

ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';
ALTER TABLE `eh_payment_charging_standards_scopes` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';
ALTER TABLE `eh_payment_bill_groups` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id';


ALTER TABLE `eh_payment_bill_items` ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id';

-- end of wentian's script

-- from testByDingjianminThree
-- --合同管理 基础设置合同规则
ALTER TABLE `eh_contract_params` ADD COLUMN `payorreceive_contract_type` tinyint(2) DEFAULT '0' COMMENT '0 收款合同 1付款合同';
ALTER TABLE `eh_contract_params` ADD COLUMN `contract_number_rulejson` text NULL COMMENT '合同规则';
ALTER TABLE `eh_contract_params` ADD COLUMN `update_time` datetime NULL COMMENT '更新时间';
ALTER TABLE `eh_contract_params` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'contract category id';
-- --合同管理 合同多入口设置
ALTER TABLE `eh_contracts` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'contract category id';
-- --合同管理，表单设置，动态字段
ALTER TABLE `eh_var_field_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';
ALTER TABLE `eh_var_field_item_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';
ALTER TABLE `eh_var_field_group_scopes` ADD COLUMN `category_id` bigint(20) NULL COMMENT 'category id';

CREATE TABLE `eh_contract_categories` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL,
  `path` varchar(128) DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `delete_uid` bigint(20) DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `logo_uri` varchar(1024) DEFAULT NULL COMMENT 'default cover uri',
  `entry_id` int(11) DEFAULT NULL,
  `contract_application_scene` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 租赁合同场景 1 物业合同场景 2 综合合同场景',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 多入口 by dingjianmin end


