
USE `ehcore`;



/*Table structure for table `eh_company_phone_list` */

DROP TABLE IF EXISTS `eh_group_contacts`;

CREATE TABLE `eh_group_contacts` (
	`id` BIGINT(20) NOT NULL,
	`owner_type` VARCHAR(64) NOT NULL,
	`owner_id` BIGINT NOT NULL COMMENT 'company id',
	`contact_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id related to the contact',	
	`contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
	`contact_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'phone number or email address',
	`contact_name` VARCHAR(64),
    `integral_tag1` BIGINT,
    `integral_tag2` BIGINT,
    `integral_tag3` BIGINT,
    `integral_tag4` BIGINT,
    `integral_tag5` BIGINT,
    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
	`creator_uid` bigint(20) COMMENT 'the user id who create the contact',
	`create_time` datetime,
	`update_uid` bigint(20),
	`update_time` datetime,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


