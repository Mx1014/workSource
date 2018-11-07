SET foreign_key_checks = 0;

use `ehcore`;

DROP TABLE IF EXISTS `eh_conf_account_categories`;
CREATE TABLE `eh_conf_account_categories` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`channel_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: single, 1: multiple',
	`conf_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: 25方仅视频, 2: 25方支持电话, 3: 100方仅视频, 4: 100方支持电话',
	`min_period` INTEGER NOT NULL DEFAULT 1 COMMENT 'the minimum count of months',
	`amount` DECIMAL(10,2),
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
DROP TABLE IF EXISTS `eh_conf_invoices`;
CREATE TABLE `eh_conf_invoices` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`order_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_conf_orders',
	`taxpayer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: INDIVIDUAL_TAXPAYER, 2: COMPANY_TAXPAYER',
	`vat_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: GENERAL_TAXPAYER, 2: NON_GENERAL_TAXPAYER',
	`expense_type` TINYINT COMMENT '0: none, 1: CONF',
	`company_name` VARCHAR(20),
	`vat_code` VARCHAR(20),
	`vat_address` VARCHAR(128),
	`vat_phone` VARCHAR(20),
	`vat_bank_name` VARCHAR(20),
	`vat_bank_account` VARCHAR(20),
	`address` VARCHAR(128),
	`zip_code` VARCHAR(20),
	`consignee` VARCHAR(20),
	`contact` VARCHAR(20),
	`contract_flag` TINYINT COMMENT '0-dont need 1-need',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_conf_orders`;
CREATE TABLE `eh_conf_orders` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the enterprise id who own the order',
    `payer_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who pay the order',
	`paid_time` DATETIME COMMENT 'the pay time of the bill',
	`quantity` INTEGER NOT NULL DEFAULT 0 COMMENT 'the quantity of accounts which going to buy',
	`period` INTEGER NOT NULL DEFAULT 0 COMMENT 'the months which every account can be used',
	`amount` DECIMAL(10,2) COMMENT 'the paid money amount',
	`description` TEXT,
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: waiting for pay, 2: paid',
	`invoice_req_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether the order needs invoice or not, 0: none, 1: request',
	`invoice_issue_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether the invoice is issued or not, 0: none, 1: invoiced',
	`account_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_conf_account_categories',
	`online_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: offline, 1: online',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who make the order',
    `create_time` DATETIME,
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_conf_order_account_map`;
CREATE TABLE `eh_conf_order_account_map` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`order_id` BIGINT NOT NULL DEFAULT 0,
	`enterprise_id` BIGINT NOT NULL DEFAULT 0,
	`conf_account_id` BIGINT NOT NULL DEFAULT 0,
	`assiged_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether the account has assiged to user, 0: none, 1: assigned',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`conf_account_namespace_id` INT NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_conf_source_accounts`;
CREATE TABLE `eh_conf_source_accounts` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`account_name` VARCHAR(128) NOT NULL DEFAULT '',
	`password` VARCHAR(128),
	`account_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_conf_account_categories',
	`expired_date` DATETIME,
	`status` TINYINT COMMENT '0: inactive 1: active',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
DROP TABLE IF EXISTS `eh_conf_accounts`;
CREATE TABLE `eh_conf_accounts` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id',
	`expired_date` DATETIME,
	`status` TINYINT COMMENT '0-inactive 1-active 2-locked',
	`account_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_conf_account_categories',
	`account_type` TINYINT NOT NULL DEFAULT 2 COMMENT '0: none, 1: trial, 2: normal',
	`assigned_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether there is a source account assiged to the account, 0: none, 1: assigned',
	`assigned_source_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the source account id assigned to the account, reference to the id of eh_source_accounts',
	`assigned_time` DATETIME COMMENT 'the time when the source account is assigned to the account',
	`assigned_conf_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'referenece to the id of eh_conf_conferences',
	`owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who use the account, reference to the id of eh_users',
	`own_time` DATETIME COMMENT 'the time when the user own the account',
	`delete_uid` BIGINT NOT NULL DEFAULT 0,
	`delete_time` DATETIME,
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the account',
	`create_time` DATETIME,
	`update_time` DATETIME,
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_conf_account_histories`;
CREATE TABLE `eh_conf_account_histories` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id',
	`expired_date` DATETIME,
	`status` TINYINT COMMENT '0: inactive, 1: active, 2: locked',
	`account_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_conf_account_categories',
	`account_type` TINYINT NOT NULL DEFAULT 2 COMMENT '0: none, 1: trial, 2: normal',
	`owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who use the account, reference to the id of eh_users',
	`own_time` DATETIME COMMENT 'the time when the user own the account',
	`deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
	`delete_time` DATETIME,
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the account',
	`create_time` DATETIME,
	`operator_uid` BIGINT,
    `operation_type` VARCHAR(32),
    `process_details` TEXT,
	`operate_time` DATETIME,
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_conf_conferences`;
CREATE TABLE `eh_conf_conferences` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`conf_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the conference id from 3rd conference provider',
	`subject` VARCHAR(128) COMMENT 'the conference subject from 3rd conference provider',
	`description` TEXT,
	`start_time` DATETIME,
	`end_time` DATETIME,
	`expect_duration` INTEGER NOT NULL DEFAULT 0 COMMENT 'how long the conference expected to last, unit: minute',
	`real_duration` INTEGER NOT NULL DEFAULT 0 COMMENT 'how long the conference really lasted, unit: minute',
	`conf_host_id` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'the conf host id from 3rd conference provider',
	`conf_host_name` VARCHAR(256) NOT NULL DEFAULT 0 COMMENT 'the conf host name of the conference',
	`max_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'the max amount of allowed attendees',
	`conf_host_key` VARCHAR(128) COMMENT 'the password of the conference, set by the creator',
    `join_policy` INTEGER NOT NULL DEFAULT 1 COMMENT '0: free join, 1: conf host first',
	`source_account_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to eh_source_accounts',
	`conf_account_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to eh_conf_accounts',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the account',
	`join_url` VARCHAR(256) COMMENT 'user use the url to join the meeting',
	`start_url` VARCHAR(256) COMMENT 'user who start the meeting use this url',
	`create_time` DATETIME,
	`status` TINYINT COMMENT '0: close, 1: on progress, 2: failed',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_conf_enterprises`;
CREATE TABLE `eh_conf_enterprises` (
	`id` BIGINT NOT NULL COMMENT 'id',
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id, reference to the id of eh_groups, unique',
	`contact_name` VARCHAR(128),
	`contact` VARCHAR(128),
	`account_amount` INTEGER NOT NULL DEFAULT 0 COMMENT 'the total amount of active or inactive accounts the enterprise owned',
	`trial_account_amount` INTEGER NOT NULL DEFAULT 0 COMMENT 'the total amount of trial accounts the enterprise owned',
	`active_account_amount` INTEGER NOT NULL DEFAULT 0 COMMENT 'the total amount of active accounts the enterprise owned',
	`buy_channel` TINYINT NOT NULL DEFAULT 0 COMMENT '0: offline, 1: online',
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active, 2: locked',
	`deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
	`delete_time` DATETIME,
	`update_time` DATETIME,
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the enterrpise',
	`create_time` DATETIME,
	
	UNIQUE `u_eh_enterprise_id`(`enterprise_id`),
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_warning_contacts`;
CREATE TABLE `eh_warning_contacts` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`contactor` VARCHAR(20) DEFAULT NULL,
	`mobile` VARCHAR(20) DEFAULT NULL,
	`email` VARCHAR(20) DEFAULT NULL,
	`namespace_id` int(11) NOT NULL DEFAULT '0',
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
DROP TABLE IF EXISTS `eh_conf_reservations`;
CREATE TABLE `eh_conf_reservations` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id, reference to the id of eh_groups, unique',
	`creator_phone` VARCHAR(20),
	`conf_account_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'referenece to id of eh_conf_accounts',
	`subject` VARCHAR(128) COMMENT 'the conference subject',
	`description` TEXT,
	`conf_host_name` VARCHAR(256) NOT NULL DEFAULT 0 COMMENT 'the conf host name of the conference',
	`conf_host_key` VARCHAR(128) COMMENT 'the password of the conference, set by the creator',
	`start_time` DATETIME,
	`time_zone` VARCHAR(64),
	`duration` INTEGER NOT NULL DEFAULT 0 COMMENT 'how long the conference expected to last, unit: minute',
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active, 2: locked',
	`update_time` DATETIME,
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the reservation',
	`create_time` DATETIME,
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



-- 召开会议
-- int ret = meetingService.startMeeting(context, USER_ID, ZOOM_TOKEN, STYPE, meetingId, displayName, opts);
-- 加入会议
-- int ret = meetingService.joinMeeting(context, meetingId, displayName, opts);
-- web domain
-- public final static String WEB_DOMAIN = "www.zoomus.cn";
-- APP Key
-- public final static String APP_KEY = "G3cZNdsjd4nXk7EOoVq1T0Z2z8sI4UJ5C9H6";
-- APP Secret
-- public final static String APP_SECRET = "YYaxK6HCYY3MFDTAYg04xeavjluYNJroxoLk";
-- user ID
-- public final static String USER_ID = "8EubJ8t9RkWXneUEpY6m7Q";
-- token
-- public final static String ZOOM_TOKEN = "n7m8_1qELfzv0uzc-niIQ3DjevC9LtHeQjl0FpC1eYM.BgIgWE1JL2I5aDNRNW5sd1ZNZ3p0Z3dtWTM4TE5WVHlYZ2lANWVhMTA5MDJhZTYwNDAwMjEwYzg2MmVhZTA3ZjY3OTFkNjJkZTYyYjUxM2U5YTFhZWRlMDBiODg1MTFkNjIzYgA";


SET foreign_key_checks = 1;