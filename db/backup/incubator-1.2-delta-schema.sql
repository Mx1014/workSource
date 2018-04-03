-- 增加入孵申请类型  add by yanjun 20171215
ALTER TABLE `eh_incubator_applies` ADD COLUMN `apply_type`  tinyint(4) NOT NULL DEFAULT 0 ;
ALTER TABLE `eh_incubator_applies` ADD COLUMN `parent_id`  bigint(22) NULL AFTER `uuid`;
ALTER TABLE `eh_incubator_applies` ADD COLUMN `root_id`  bigint(22) NULL COMMENT 'tree root id' AFTER `parent_id`;
ALTER TABLE `eh_incubator_applies` ADD COLUMN `organization_id`  bigint(22) NULL;

-- 文件下载任务 add by yanjun 20171207
CREATE TABLE `eh_tasks` (
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
  `process` int(11) DEFAULT NULL COMMENT 'rate of progress',
  `result_string1` varchar(255) DEFAULT NULL,
  `result_string2` varchar(255) DEFAULT NULL,
  `result_long1` bigint(20) DEFAULT NULL,
  `result_long2` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `error_description` varchar(255) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `finish_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;







