
USE `ehcore`;


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
  `punch_status` tinyint(4) COMMENT '2:Normal ;  1:Not in punch area',
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
  `create_uid` bigint(20),
  `create_time` datetime,
  `update_uid` bigint(20),
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_punch_geopoints`;

CREATE TABLE `eh_punch_geopoints` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `company_id` bigint(20) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `create_uid` bigint(20),
  `create_time` datetime,
  `update_uid` bigint(20),
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4



DROP TABLE IF EXISTS `eh_punch_alter_requests`;

CREATE TABLE `eh_punch_alter_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) COMMENT 'user''s id',
  `company_id` bigint(20) COMMENT 'compay id',
  `punch_date` date COMMENT 'user punch date',
  `request_status` tinyint(4) COMMENT '0:alter request ;  1:illustrate',
  `request_answer` tinyint(4) COMMENT '0:undo ;  1:refuse ;2:consent ;3:sick absence 4: leave of absence 5:annual leave ;6: other absence',
  `description` varchar(128) ,
  `string_tag1` varchar(128) ,
  `string_tag2` varchar(128) ,
  `create_uid` bigint(20),
  `create_time` datetime,
  `update_uid` bigint(20),
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_punch_alter_requests`;

CREATE TABLE `eh_punch_alter_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) COMMENT 'user''s id',
  `company_id` bigint(20) COMMENT 'compay id',
  `punch_date` date COMMENT 'user punch date',
  `request_status` tinyint(4) COMMENT '0:alter request ;  1:illustrate',
  `request_answer` tinyint(4) COMMENT '0:undo ;  1:refuse ;2:consent ;3:sick absence 4: leave of absence 5:annual leave ;6: other absence',
  `description` varchar(128) ,
  `string_tag1` varchar(128) ,
  `string_tag2` varchar(128) ,
  `punch_time` datetime COMMENT 'user check time',
  `punch_status` tinyint(4) COMMENT '2:Normal ;  1:Not in punch area',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `eh_china_workdate`;

CREATE TABLE `eh_china_workdate` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `request_status` tinyint(4) COMMENT '0:weekend work date ;  1:holiday',
  `date_tag` date COMMENT 'date',
  `create_uid` bigint(20),
  `create_time` datetime,
  `update_uid` bigint(20),
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;