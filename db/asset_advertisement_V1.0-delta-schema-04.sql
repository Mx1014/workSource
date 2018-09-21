alter table eh_payment_bills modify column noticeTel varchar(255) COMMENT '催缴手机号码';

-- AUTHOR: 杨崇鑫  20180920
-- REMARK: 账单表增加一个企业客户ID的字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `customer_id` BIGINT COMMENT '企业客户ID';

alter table eh_contracts add sponsor_uid BIGINT COMMENT '发起人id';
alter table eh_contracts add sponsor_time DATETIME COMMENT '发起时间';

CREATE TABLE `eh_address_properties` (
	`id` BIGINT (20) NOT NULL COMMENT 'id of the record',
	`namespace_id` INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	`community_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	`building_id` BIGINT (20) DEFAULT NULL COMMENT '楼栋id',
	`address_id` BIGINT (20) DEFAULT NULL COMMENT '房源id',
	`charging_items_id` BIGINT (20) COMMENT '费项id',
	`authorize_price` DECIMAL (10, 2) COMMENT '授权价',
	`apartment_authorize_type` TINYINT (4) DEFAULT NULL COMMENT '房源授权价类型（1:每天; 2:每月; 3:每个季度; 4:每年;)',
	`status` TINYINT (4) COMMENT '0-无效状态 ,2-有效状态',
	`create_time` DATETIME COMMENT '创建日期',
	`creator_uid` BIGINT COMMENT '创建人',
	`operator_time` DATETIME COMMENT '最近修改时间',
	`operator_uid` BIGINT COMMENT '最近修改人',
	PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT '楼宇属性信息表';

-- AUTHOR: 黄明波
-- REMARK: 服务联盟样式列表添加排序
ALTER TABLE `eh_service_alliance_categories`	CHANGE COLUMN `default_order` `default_order` BIGINT NOT NULL DEFAULT '0' ;

ALTER TABLE `eh_service_alliances` CHANGE COLUMN `address` `address` VARCHAR(255) NULL DEFAULT NULL ;
-- END

ALTER TABLE `eh_enterprise_op_requests` ADD COLUMN `transform_flag`  tinyint NULL DEFAULT 0 COMMENT '是否转化为意向客户：0-否，1-是';
ALTER TABLE `eh_enterprise_op_requests` ADD COLUMN `customer_name`  varchar(255) NULL COMMENT '承租方';

ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `organization_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the orgId for the user' AFTER `namespace_id`;
ALTER TABLE `eh_work_report_val_receiver_map` ADD INDEX `i_work_report_receiver_id` (`receiver_user_id`) ;

ALTER TABLE `eh_work_reports` ADD COLUMN `validity_setting` VARCHAR(512) COMMENT 'the expiry date of the work report' AFTER `form_version`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the receiver message settings' AFTER `validity_setting`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_seeting` VARCHAR(512) COMMENT 'the time range of the receiver message' AFTER `receiver_msg_type`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the author message settings' AFTER `receiver_msg_seeting`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_seeting` VARCHAR(512) COMMENT 'the time range of the author message' AFTER `author_msg_type`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `icon_uri` VARCHAR(1024) COMMENT 'the icon of the work report' AFTER `delete_flag`;

ALTER TABLE `eh_work_report_vals` ADD COLUMN `receiver_avatar` VARCHAR(1024) COMMENT 'the avatar of the fisrt receiver' AFTER `report_type`;
ALTER TABLE `eh_work_report_vals` ADD COLUMN `applier_avatar` VARCHAR(1024) COMMENT 'the avatar of the author' AFTER `receiver_avatar`;

ALTER TABLE `eh_work_report_vals` MODIFY COLUMN `report_time` DATE COMMENT 'the target time of the report';


CREATE TABLE `eh_work_report_val_receiver_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_val_id` BIGINT NOT NULL COMMENT 'id of the report val',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` DATE NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
  `create_time` DATETIME COMMENT 'record create time',

  KEY `i_eh_work_report_val_receiver_msg_report_id`(`report_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_val_id`(`report_val_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_time`(`report_time`),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_work_report_scope_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` DATE NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `end_time` DATETIME COMMENT 'the deadline of the report',
  `scope_ids` TEXT COMMENT 'the id list of the receiver',
  `create_time` DATETIME COMMENT 'record create time',

  KEY `i_eh_work_report_scope_msg_report_id`(`report_id`),
  KEY `i_eh_work_report_scope_msg_report_time`(`report_time`),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `report_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the report id' AFTER `organization_id`;
-- ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `reminder_time` DATETIME AFTER `read_status`;
--
-- ALTER TABLE `eh_work_report_val_receiver_map` ADD INDEX `i_reminder_time` (`reminder_time`) ;
