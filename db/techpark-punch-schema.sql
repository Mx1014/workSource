
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
	`creator_uid` bigint(20) DEFAULT NULL COMMENT 'the user id who create the contact',
	`create_time` datetime DEFAULT NULL,
	`update_uid` bigint(20) DEFAULT NULL,
	`update_time` datetime DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `eh_punch_logs` */

DROP TABLE IF EXISTS `eh_punch_logs`;

CREATE TABLE `eh_punch_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user''s id',
  `company_id` bigint(20) DEFAULT NULL COMMENT 'compay id',
  `punch_date` date DEFAULT NULL COMMENT 'user punch date',
  `punch_time` datetime DEFAULT NULL COMMENT 'user check time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `eh_punch_rules` */

DROP TABLE IF EXISTS `eh_punch_rules`;

CREATE TABLE `eh_punch_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `company_id` bigint(20) NOT NULL COMMENT 'rule company id',
  `check_type` tinyint(4) DEFAULT NULL COMMENT '0:standard ;  1:flextime',
  `punch_times` int(1) DEFAULT NULL COMMENT 'how many times everyday emloyee need punched. 2 times or 4 times',
  `start_time` time DEFAULT NULL COMMENT 'type=0:work start time ;1:work start earlist time',
  `end_time` time DEFAULT NULL COMMENT 'type=0:work end time ;1:work start latest time',
  `noon_end_time` time DEFAULT NULL,
  `afternoon_start_time` time DEFAULT NULL,
  `work_time` time DEFAULT NULL COMMENT 'how long must I work',
  `late_arrive_time` time DEFAULT NULL COMMENT 'how long can I be late',
  `early_leave_time` time DEFAULT NULL COMMENT 'how long can I be early leave from work',
  `create_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;