-- 文件下载任务 add by yanjun 20171207
CREATE TABLE `eh_jobs` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL COMMENT 'owner',
  `name` varchar(255) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL COMMENT '0-default,1-filedownload',
  `class_name` varchar(255) DEFAULT NULL,
  `params` text,
  `repeat_flag` tinyint(4) DEFAULT NULL,
  `rate` int(11) DEFAULT NULL COMMENT 'rate of progress',
  `result_string_1` varchar(255) DEFAULT NULL,
  `result_string_2` varchar(255) DEFAULT NULL,
  `result_long_1` bigint(20) DEFAULT NULL,
  `result_long_2` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `error_code` tinyint(4) DEFAULT NULL,
  `error_description` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `finish_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





