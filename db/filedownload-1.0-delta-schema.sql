-- 文件下载任务 add by yanjun 20171207
CREATE TABLE `eh_file_download_jobs` (
  `id` bigint(20) NOT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `job_class` varchar(255) DEFAULT NULL,
  `params` text,
  `size` bigint(20) DEFAULT NULL,
  `count` bigint(20) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `finish_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;