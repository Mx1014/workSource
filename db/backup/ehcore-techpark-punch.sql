#TechPark Punch Sql
# 
DROP TABLE IF EXISTS `eh_punch_rules`;

CREATE TABLE `eh_punch_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `company_id` bigint(20) NOT NULL COMMENT 'rule company id',
  `check_type` tinyint(4) DEFAULT NULL COMMENT '0:standard ;  1:flextime',
  `start_time` time DEFAULT NULL COMMENT 'type=0:work start time ;1:work start earlist time',
  `end_time` time DEFAULT NULL COMMENT 'type=0:work end time ;1:work start latest time',
  `noon_end_time` time DEFAULT NULL,
  `afternoon_start_time` time DEFAULT NULL,
  `work_time` time DEFAULT NULL COMMENT 'how long must I work',
  `late_time` time DEFAULT NULL COMMENT 'how long can I be late',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_punch_logs`;

CREATE TABLE `eh_punch_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user''s id',
  `check_time` datetime DEFAULT NULL COMMENT 'user check time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;