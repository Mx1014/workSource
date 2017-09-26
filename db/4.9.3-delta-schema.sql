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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
ALTER TABLE eh_parking_recharge_orders ADD COLUMN original_price decimal(10,2) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN card_request_id bigint(20) DEFAULT NULL;
ALTER TABLE eh_parking_recharge_orders ADD COLUMN invoice_type bigint(4) DEFAULT NULL;

ALTER TABLE eh_parking_card_requests ADD COLUMN card_type_id VARCHAR(64) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN address_id bigint(20) DEFAULT NULL;
ALTER TABLE eh_parking_card_requests ADD COLUMN invoice_type bigint(4) DEFAULT NULL;

ALTER TABLE eh_parking_lots CHANGE COLUMN `expired_recharge_json` recharge_json varchar(1024) DEFAULT NULL;

ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip_flag tinyint(4) NOT NULL DEFAULT '0' COMMENT '1: support , 0: not';
ALTER TABLE eh_parking_flow ADD COLUMN card_type_tip text;


CREATE TABLE `eh_parking_invoice_types` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `invoice_token` varchar(256) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_user_invoices` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `user_id` bigint(20) DEFAULT NULL,
  `invoice_type_id` bigint(20) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_card_types` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `card_type_id` varchar(128) DEFAULT NULL,
  `card_type_name` varchar(128) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

