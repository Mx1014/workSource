 
USE `ehcore`;

 

/*Table structure for table `eh_company_phone_list` */

DROP TABLE IF EXISTS `eh_company_phone_list`;

CREATE TABLE `eh_company_phone_list` (
  `id` bigint(20) DEFAULT NULL,
  `company_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `telephone` varchar(20) DEFAULT NULL COMMENT 'user telephone',
  `name` varchar(50) DEFAULT NULL COMMENT 'real name',
  `department` varchar(200) DEFAULT NULL,
  `create_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


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
  `late_time` time DEFAULT NULL COMMENT 'how long can I be late',
  `create_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;