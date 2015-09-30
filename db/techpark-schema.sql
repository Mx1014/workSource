
USE `ehcore`;


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
	`creator_uid` bigint,
	`create_time` datetime,
	`operator_uid` bigint,
	`operate_time` datetime,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




/*Table structure for table `eh_punch_logs` */

DROP TABLE IF EXISTS `eh_punch_logs`;

CREATE TABLE `eh_punch_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) COMMENT 'user''s id',
  `company_id` bigint(20) COMMENT 'compay id',
  `longitude` double,
  `latitude` double,
  `punch_date` date COMMENT 'user punch date',
  `punch_time` datetime COMMENT 'user check time',
  `punch_status` tinyint(4) COMMENT '1:Normal ;  0:Not in punch area',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_punch_day_logs`;

CREATE TABLE `eh_punch_day_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) COMMENT 'user''s id',
  `company_id` bigint(20) COMMENT 'compay id',
  `punch_date` date COMMENT 'user punch date',
  `arrive_time` time ,
  `leave_time` time ,
  `work_time` time COMMENT 'how long did employee work',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
	`creator_uid` bigint,
	`create_time` datetime,
 
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


/*Table structure for table `eh_punch_rules` */

DROP TABLE IF EXISTS `eh_punch_rules`;

CREATE TABLE `eh_punch_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `company_id` bigint(20) NOT NULL COMMENT 'rule company id', 
  `start_early_time` time COMMENT 'how early can i arrive',
  `start_late_time` time COMMENT 'how late can i arrive ',
  `work_time` time COMMENT 'how long do i must be work',
  `time_tag1` time,
  `time_tag2` time,
  `time_tag3` time,
	`creator_uid` bigint,
	`create_time` datetime,
  `operator_uid` bigint,
  `operate_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_punch_geopoints`;

CREATE TABLE `eh_punch_geopoints` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `company_id` bigint(20) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `distance` double ,
	`creator_uid` bigint,
	`create_time` datetime,
  `operator_uid` bigint,
  `operate_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_punch_exception_requests`;

CREATE TABLE `eh_punch_exception_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) COMMENT 'user''s id',
  `company_id` bigint(20) COMMENT 'compay id',
  `punch_date` date COMMENT 'user punch date',
  `request_type` tinyint(4) COMMENT '0:request ;  1:approval',
  `description` varchar(256) ,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: waitingForApproval, 2:active',
  `process_code` TINYINT COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `process_details` TEXT,
	`creator_uid` bigint,
	`create_time` datetime,
  `operator_uid` bigint,
  `operate_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `eh_punch_exception_approvals`;

CREATE TABLE `eh_punch_exception_approvals` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) COMMENT 'user''s id',
  `company_id` bigint(20) COMMENT 'compay id',
  `punch_date` date COMMENT 'user punch date',
  `approval_status` TINYINT NOT NULL DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
	`creator_uid` bigint,
	`create_time` datetime,
  `operator_uid` bigint,
  `operate_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `eh_punch_workday`;

CREATE TABLE `eh_punch_workday` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `date_status` tinyint(4) COMMENT '0:weekend work date ;  1:holiday',
  `date_tag` date COMMENT 'date',
	`creator_uid` bigint,
	`create_time` datetime,
  `operator_uid` bigint,
  `operate_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;