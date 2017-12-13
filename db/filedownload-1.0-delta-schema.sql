-- 文件下载任务 add by yanjun 20171207
CREATE TABLE `eh_file_download_jobs` (
  `id` bigint(20) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'owner',
  `file_name` varchar(255) DEFAULT NULL,
  `job_class_name` varchar(255) DEFAULT NULL,
  `job_params` text,
  `repeat_flag` tinyint(4) DEFAULT NULL,
  `rate` int(11) DEFAULT NULL COMMENT 'rate of progress',
  `uri` varchar(255) DEFAULT NULL,
  `size` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `error_code` tinyint(4) DEFAULT NULL,
  `error_description` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `finish_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;





