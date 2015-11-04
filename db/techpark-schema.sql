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
	`creator_uid` BIGINT,
	`create_time` DATETIME,
	`operator_uid` BIGINT,
	`operate_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;




/*Table structure for table `eh_punch_logs` */

DROP TABLE IF EXISTS `eh_punch_logs`;

CREATE TABLE `eh_punch_logs` (
  `id` BIGINT(20) NOT NULL COMMENT 'id',
  `user_id` BIGINT(20) COMMENT 'user''s id',
  `company_id` BIGINT(20) COMMENT 'compay id',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `punch_date` DATE COMMENT 'user punch date',
  `punch_time` DATETIME COMMENT 'user check time',
  `punch_status` TINYINT(4) COMMENT '1:Normal ;  0:Not in punch area',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_day_logs`;

CREATE TABLE `eh_punch_day_logs` (
  `id` BIGINT(20) NOT NULL COMMENT 'id',
  `user_id` BIGINT(20) COMMENT 'user''s id',
  `company_id` BIGINT(20) COMMENT 'compay id',
  `punch_date` DATE COMMENT 'user punch date',
  `arrive_time` TIME ,
  `leave_time` TIME ,
  `work_time` TIME COMMENT 'how long did employee work',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
	`view_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'is view(0) not view(1)',
 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


/*Table structure for table `eh_punch_rules` */

DROP TABLE IF EXISTS `eh_punch_rules`;

CREATE TABLE `eh_punch_rules` (
  `id` BIGINT(20) NOT NULL COMMENT 'id',
  `company_id` BIGINT(20) NOT NULL COMMENT 'rule company id', 
  `start_early_time` TIME COMMENT 'how early can i arrive',
  `start_late_time` TIME COMMENT 'how late can i arrive ',
  `work_time` TIME COMMENT 'how long do i must be work',
  `time_tag1` TIME,
  `time_tag2` TIME,
  `time_tag3` TIME,
	`creator_uid` BIGINT,
	`create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_geopoints`;

CREATE TABLE `eh_punch_geopoints` (
  `id` BIGINT(20) NOT NULL COMMENT 'id of the record',
  `company_id` BIGINT(20) DEFAULT NULL,
  `description` VARCHAR(256) DEFAULT NULL,
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `geohash` VARCHAR(32) DEFAULT NULL,
  `distance` DOUBLE ,
	`creator_uid` BIGINT,
	`create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_punch_exception_requests`;

CREATE TABLE `eh_punch_exception_requests` (
  `id` BIGINT(20) NOT NULL COMMENT 'id',
  `user_id` BIGINT(20) COMMENT 'user''s id',
  `company_id` BIGINT(20) COMMENT 'compay id',
  `punch_date` DATE COMMENT 'user punch date',
  `request_type` TINYINT(4) COMMENT '0:request ;  1:approval',
  `description` VARCHAR(256) ,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: waitingForApproval, 2:active',
  `process_code` TINYINT COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `process_details` TEXT,
	`creator_uid` BIGINT,
	`create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
	`view_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'is view(0) not view(1)',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_punch_exception_approvals`;

CREATE TABLE `eh_punch_exception_approvals` (
  `id` BIGINT(20) NOT NULL COMMENT 'id',
  `user_id` BIGINT(20) COMMENT 'user''s id',
  `company_id` BIGINT(20) COMMENT 'compay id',
  `punch_date` DATE COMMENT 'user punch date',
  `approval_status` TINYINT NOT NULL DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
	`view_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'is view(0) not view(1)',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_punch_workday`;

CREATE TABLE `eh_punch_workday` (
  `id` BIGINT(20) NOT NULL COMMENT 'id',
  `date_status` TINYINT(4) COMMENT '0:weekend work date ;  1:holiday',
  `date_tag` DATE COMMENT 'date',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_rental_rules`;


CREATE TABLE `eh_rental_rules` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_community_id` BIGINT NOT NULL COMMENT ' enterprise  community id', 
  `site_type` VARCHAR(20) COMMENT 'rule for what function ',
  `rental_start_time` BIGINT,
  `rental_end_time` BIGINT,
  `pay_start_time` BIGINT,
  `pay_end_time` BIGINT,
  `payment_ratio` INT COMMENT 'payment ratio',
  `refund_flag` TINYINT(4)  NOT NULL  DEFAULT 1 COMMENT '0 allow refund , 1 can not refund ',
  `contact_num` VARCHAR(20) COMMENT 'phone number',
  `time_tag1` TIME,
  `time_tag2` TIME,
  `time_tag3` TIME,
  `date_tag1` DATE,
  `date_tag2` DATE,
  `date_tag3` DATE,
  `datetime_tag1` DATETIME,
  `datetime_tag2` DATETIME,
  `datetime_tag3` DATETIME,
  `integral_tag1` BIGINT(20) ,
  `integral_tag2` BIGINT(20) ,
  `integral_tag3` BIGINT(20) ,
  `integral_tag4` BIGINT(20) ,
  `string_tag1` VARCHAR(128) ,
  `string_tag2` VARCHAR(128) ,
  `string_tag3` VARCHAR(128) ,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

 

 
DROP TABLE IF EXISTS `eh_rental_sites`;


CREATE TABLE `eh_rental_sites`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `parent_id` BIGINT ,
  `enterprise_community_id` BIGINT(20) NOT NULL COMMENT ' enterprise  community id', 
  `site_type`  VARCHAR(128) ,
  `site_name` VARCHAR(127),
  `site_type2` TINYINT(4),
  `building_name` VARCHAR(128) ,
  `building_id` BIGINT, 
  `address` VARCHAR(128) ,
  `address_id` BIGINT,
  `spec` VARCHAR(255)  COMMENT 'spec ,user setting ,maybe meetingroom seats ,KTV ROOM: big small VIP and so on',
  `own_company_name` VARCHAR(60) ,
  `contact_name` VARCHAR(40) ,
  `contact_phonenum` VARCHAR(20),
  `contact_phonenum2` VARCHAR(20),
  `contact_phonenum3` VARCHAR(20),
  `status` TINYINT(4),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rental_site_items`;


CREATE TABLE `eh_rental_site_items`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `rental_site_id` BIGINT NOT NULL COMMENT '  rental_site id', 
  `name` VARCHAR(128) ,
  `price` DOUBLE, 
  `counts` INT  COMMENT 'item count',
  `status` TINYINT(4), 
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_site_rules`;


CREATE TABLE `eh_rental_site_rules`(
  `id` BIGINT  NOT NULL COMMENT 'id',
  `enterprise_community_id` BIGINT(20) NOT NULL COMMENT ' enterprise  community id', 
  `site_type`  VARCHAR(128) ,
  `rental_site_id` BIGINT NOT NULL COMMENT 'rental_site id', 
  `rental_type` tinyint(4) COMMENT '0: as hour:min  1-as half day 2-as day',
  `amorpm` tinyint(4)  COMMENT '0:am  1:pm',
  `rentalStep` int(11) DEFAULT 1 COMMENT 'how much every order',
  `begin_time` DATETIME,
  `end_time` DATETIME,
  `counts` DOUBLE  COMMENT 'site count',
  `unit` DOUBLE COMMENT '1 or 0.5 basketball yard can rental half',
  `price` DOUBLE COMMENT 'how much every step every unit', 
  `site_rental_date` DATE COMMENT 'which day',
  `status` TINYINT(4) COMMENT 'unuse 0:open  1:closed', 
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_bills`;


CREATE TABLE `eh_rental_bills`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_community_id` BIGINT(20) NOT NULL COMMENT ' enterprise  community id', 
  `site_type`  VARCHAR(128) ,
  `rental_site_id` BIGINT NOT NULL COMMENT 'id',
  `rental_uid` BIGINT COMMENT 'rental user id',
  `rental_date` DATE COMMENT 'rental target date',
  `start_time` DATETIME COMMENT 'begin datetime unuse ',
  `end_time` DATETIME COMMENT 'end datetime unuse',
  `rental_count` DOUBLE COMMENT 'amount of rental sites ',
  `pay_total_money` double DEFAULT NULL COMMENT 'total money ,include items and site',
  `site_total_money` double DEFAULT NULL,
  `reserve_money` DOUBLE COMMENT 'total money * reserve ratio',
  `reserve_time` DATETIME COMMENT 'reserve time ',
  `pay_start_time` DATETIME ,
  `pay_end_time` DATETIME,
  `pay_time` DATETIME ,  
  `cancel_time` DATETIME ,
  `paid_money` DOUBLE COMMENT'already paid money ',
  `status` TINYINT(4) COMMENT'0:wait for reserve 1:paid reserve  2:paid all money reserve success  3:wait for final payment 4:unlock reserve fail',
  `visible_flag` TINYINT(4) DEFAULT '0' COMMENT '0:visible 1:unvisible',
  `invoice_flag` TINYINT(4) DEFAULT '1' COMMENT '0:want invocie 1 no need',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS  `eh_rental_bill_attachments`;

CREATE TABLE `eh_rental_bill_attachments`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_community_id` BIGINT(20) NOT NULL COMMENT ' enterprise  community id', 
  `site_type`  VARCHAR(128) ,
  `rental_bill_id` BIGINT ,
  `attachment_type` TINYINT COMMENT '0:String 1:email 2:attachment file',  
  `content`  VARCHAR(500) ,
  `file_path` VARCHAR(500),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4; 


DROP TABLE IF EXISTS  `eh_rental_sites_bills`;

CREATE TABLE `eh_rental_sites_bills`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_community_id` BIGINT(20) NOT NULL COMMENT ' enterprise  community id', 
  `site_type`  VARCHAR(128) ,
  `rental_bill_id` BIGINT ,
  `rental_site_rule_id` BIGINT ,  
  `rental_count` DOUBLE ,
  `total_money` DOUBLE,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
DROP TABLE IF EXISTS  `eh_rental_items_bills`;

CREATE TABLE `eh_rental_items_bills`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_community_id` BIGINT(20) NOT NULL COMMENT ' enterprise  community id', 
  `site_type`  VARCHAR(128) ,
  `rental_bill_id` BIGINT ,
  `rental_site_item_id` BIGINT ,  
  `rental_count` INT,
  `total_money` DOUBLE,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;




DROP TABLE IF EXISTS `eh_park_charge`;

CREATE TABLE `eh_park_charge`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `months` TINYINT,
  `amount` DOUBLE,
  `enterprise_community_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
DROP TABLE IF EXISTS `eh_recharge_info`;

CREATE TABLE `eh_recharge_info`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `bill_id` BIGINT,
  `plate_number` VARCHAR(20),
  `number_type` TINYINT COMMENT '0-car plate',
  `owner_name` VARCHAR(20) COMMENT 'plate number owner name',
  `recharge_userid` BIGINT,
  `recharge_username` VARCHAR(20) ,
  `recharge_phone` VARCHAR(20) ,
  `recharge_time` DATETIME,
  `recharge_month` TINYINT,
  `recharge_amount` DOUBLE,
  `old_validityperiod` DATETIME,
  `new_validityperiod` DATETIME,
  `payment_status` TINYINT COMMENT '3rd plat :0-fail 1-unpay 2-success',
  `recharge_status` TINYINT COMMENT '0-fail 1-waiting paying 2-refreshing data 3-success',
  `enterprise_community_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_park_apply_card`;

CREATE TABLE `eh_park_apply_card` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `applier_id` BIGINT,
  `applier_name` VARCHAR(20),
  `applier_phone` VARCHAR(20),
  `plate_number` VARCHAR(20),
  `apply_time` DATETIME,
  `apply_status` TINYINT,
  `fetch_status` TINYINT,
  `deadline` DATETIME,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
