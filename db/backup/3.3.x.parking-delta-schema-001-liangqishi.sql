
#
# member of global partition
# the vendors who service the parking lot, zuolin is the default vendor
#
DROP TABLE IF EXISTS `eh_parking_vendors`;
CREATE TABLE `eh_parking_vendors` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
    `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'the identifier name of the vendor',
	`display_name` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the name used to display in desk',
	`description` VARCHAR(2048) COMMENT 'the description of the vendor',
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`),
    UNIQUE `u_vender_name`(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_parking_lots`;
CREATE TABLE `eh_parking_lots` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',	
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
    `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'used to display',
	`vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
	`vendor_lot_token`  VARCHAR(128) COMMENT 'parking lot id from vendor',
	`card_reserve_days` INTEGER NOT NULL DEFAULT 0 COMMENT 'how may days the parking card is reserved for the applicant',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
# the recharging rates which is convenient for the user picking while recharging the card
#
DROP TABLE IF EXISTS `eh_parking_recharge_rates`;
CREATE TABLE `eh_parking_recharge_rates` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',	
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
	`rate_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'rate name for recharging the parking card',
	`month_count` DECIMAL(10,2) COMMENT 'how many months in the rate for recharging the parking card',
	`price` DECIMAL(10,2) COMMENT 'the total price for recharging the parking card',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_parking_recharge_orders`;
CREATE TABLE `eh_parking_recharge_orders` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',	
	`order_no` BIGINT(20) DEFAULT NULL,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
	`plate_number` VARCHAR(64),
	`plate_owner_name` VARCHAR(64) COMMENT 'the name of plate owner',
	`plate_owner_phone` VARCHAR(64) COMMENT 'the phone of plate owner',
	`payer_enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of organization where the payer is in',
	`payer_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id of payer',
	`payer_phone` VARCHAR(64) COMMENT 'the phone of payer',
	`paid_time` DATETIME COMMENT 'the pay time',
	`vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
	`card_number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be number of virtual card or location number',
	`rate_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from eh_parking_recharge_rates or 3rd system, according to vendor_name',
	`rate_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'rate name for recharging the parking card',
	`month_count` DECIMAL(10,2) COMMENT 'how many months in the rate for recharging the parking card',
	`price` DECIMAL(10,2) COMMENT 'the total price in the item for recharging the parking card',
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
	`recharge_status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: none, 1: unrecharged 1: recharged',
	`recharge_time` DATETIME,
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
    `old_expired_time` DATETIME,
    `new_expired_time` DATETIME,
	`paid_type` VARCHAR(32) COMMENT 'the type of payer',
	`is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT 'the order is delete, 0 : is not deleted, 1: deleted',
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_parking_card_requests`;
CREATE TABLE `eh_parking_card_requests` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',

	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
	`requestor_enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of organization where the requestor is in',
	`requestor_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'requestor id',
	`plate_number` VARCHAR(64),
	`plate_owner_entperise_name` VARCHAR(64) COMMENT 'the enterprise name of plate owner',
	`plate_owner_name` VARCHAR(64) COMMENT 'the name of plate owner',
	`plate_owner_phone` VARCHAR(64) COMMENT 'the phone of plate owner',
	`status` TINYINT COMMENT '0: inactive, 1: queueing, 2: notified, 3: issued',
	`issue_flag` TINYINT COMMENT 'whether the applier fetch the card or not, 0: unissued, 1: issued',
    `issue_time` DATETIME,
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_parking_activities`;
CREATE TABLE `eh_parking_activities` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',	
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
	`start_time` DATETIME COMMENT 'start time',
	`end_time` DATETIME COMMENT 'end time',
	`top_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'Top N user can join the activity',
    `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_forum_posts` ADD COLUMN `start_time` DATETIME COMMENT 'publish start time';
