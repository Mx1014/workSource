-- by dengs, 20170925 服务联盟2.9
-- DROP TABLE IF EXISTS `eh_service_alliance_comments`;
CREATE TABLE `eh_service_alliance_comments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g servicealliance id',
	`parent_comment_id` BIGINT COMMENT 'parent comment Id',
	`content_type` VARCHAR(32) COMMENT 'object content type',
	`content` TEXT COMMENT 'content data, depends on value of content_type',
	`status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
	`create_time` DATETIME,
	`deleter_uid` BIGINT COMMENT 'deleter uid',
	`delete_time` DATETIME COMMENT 'delete time',
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_service_alliance_comment_attachments`;
CREATE TABLE `eh_service_alliance_comment_attachments` (
	`id` BIGINT(20) NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
	`content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'attachment object link info on storage',
	`status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT NOT NULL,
	`create_time` DATETIME NOT NULL,
	`operator_uid` BIGINT,
	`update_time` DATETIME,

	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 服务联盟	添加概要描述
ALTER TABLE `eh_service_alliances` ADD COLUMN `summary_description` VARCHAR(1024) COMMENT '';
ALTER TABLE `eh_service_alliances` ADD COLUMN `enable_comment` TINYINT DEFAULT 0 COMMENT '1,enable;0,disable';
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `entry_id` INTEGER;
-- end by dengs, 20170925 服务联盟2.9

--
-- 短信日志 add by xq.tian  2017/08/23
--
ALTER TABLE `eh_sms_logs` ADD COLUMN `handler` VARCHAR(128) NOT NULL COMMENT 'YunZhiXun, YouXunTong, LianXinTong';
ALTER TABLE `eh_sms_logs` ADD COLUMN `sms_id` VARCHAR(128) COMMENT 'sms identifier';
ALTER TABLE `eh_sms_logs` ADD COLUMN `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: send success, 2: send failed, 4: report success, 5: report failed';
ALTER TABLE `eh_sms_logs` ADD COLUMN `report_text` TEXT COMMENT 'report text';
ALTER TABLE `eh_sms_logs` ADD COLUMN `report_time` DATETIME(3);

ALTER TABLE `eh_sms_logs` ADD INDEX i_eh_mobile_handler(`mobile`, `handler`);

-- 停车 add by sw 20170925
ALTER TABLE eh_parking_recharge_orders ADD COLUMN original_price DECIMAL(10,2) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN card_request_id BIGINT(20) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN invoice_type BIGINT(4) DEFAULT NULL;

ALTER TABLE eh_parking_card_requests ADD COLUMN card_type_id VARCHAR(64) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN address_id BIGINT(20) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN invoice_type BIGINT(4) DEFAULT NULL;

ALTER TABLE eh_parking_lots CHANGE COLUMN `expired_recharge_json` recharge_json VARCHAR(1024) DEFAULT NULL;

ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip_flag TINYINT(4) NOT NULL DEFAULT '0' COMMENT '1: support , 0: not';
ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip TEXT;


CREATE TABLE `eh_parking_invoice_types` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parking_lot_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `invoice_token` VARCHAR(256) DEFAULT NULL,
  `name` VARCHAR(128) DEFAULT NULL,
  `status` TINYINT(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_user_invoices` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parking_lot_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `user_id` BIGINT(20) DEFAULT NULL,
  `invoice_type_id` BIGINT(20) DEFAULT NULL,
  `creator_uid` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_card_types` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `parking_lot_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `card_type_id` VARCHAR(128) DEFAULT NULL,
  `card_type_name` VARCHAR(128) DEFAULT NULL,
  `status` TINYINT(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT(20) DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `update_uid` BIGINT(20) DEFAULT NULL,
  `update_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- merge from incubator-1.0 成都孵化器 start  by yanjun

-- 入孵申请表  add by yanjun 20170913
CREATE TABLE `eh_incubator_applies` (
`id`  BIGINT(22) NOT NULL ,
`uuid`  VARCHAR(128) NOT NULL DEFAULT '' ,
`namespace_id`  INT(11) NULL ,
`community_id`  BIGINT(22) NULL,
`apply_user_id` BIGINT(22)  NOT NULL,
`team_name`  VARCHAR(255) NULL ,
`project_type`  VARCHAR(255) NULL ,
`project_name`  VARCHAR(255) NULL ,
`business_licence_uri`  VARCHAR(255) NULL ,
`plan_book_uri`  VARCHAR(255) NULL ,
`charger_name`  VARCHAR(255) NULL ,
`charger_phone`  VARCHAR(18) NULL ,
`charger_email`  VARCHAR(255) NULL ,
`charger_wechat`  VARCHAR(255) NULL ,
`approve_user_id`  BIGINT(22) NULL ,
`approve_status`  TINYINT(4) NULL COMMENT '审批状态，0-待审批，1-拒绝，2-通过' ,
`approve_time`  DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
`approve_opinion`  VARCHAR(255) NULL ,
`create_time`  DATETIME NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP ,
`re_apply_id`  BIGINT(22) NULL COMMENT '重新申请的Id',
PRIMARY KEY (`id`)
);
-- 入孵申请项目类型 add by yanjun 20170913
CREATE TABLE `eh_incubator_project_types` (
`id`  BIGINT(22) NOT NULL ,
`uuid`  VARCHAR(128) NOT NULL DEFAULT '' ,
`name`  VARCHAR(255) NOT NULL ,
`create_time`  DATETIME NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
);

CREATE TABLE `eh_incubator_apply_attachments` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `incubator_apply_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'owner id, e.g incubator_apply_id',
  `type`  TINYINT(4) NULL COMMENT '类型，1-business_licence，2-plan_book',
  `name` VARCHAR(128) DEFAULT NULL,
  `file_size` INT(11) NOT NULL DEFAULT '0',
  `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `download_count` INT(11) NOT NULL DEFAULT '0',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- merge from incubator-1.0 成都孵化器 end by yanjun

-- 打卡
-- 增加考勤统计字段
ALTER TABLE eh_punch_statistics ADD COLUMN exception_day_count INT  DEFAULT NULL COMMENT '异常天数';

