-- ------------------------------
-- 每日统计表     add by mingbo.huang  2018/07/25
-- ------------------------------
CREATE TABLE `eh_alliance_stat` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `type` bigint(20) NOT NULL COMMENT '服务联盟类型id',
  `owner_id` bigint(20) NOT NULL COMMENT '所属项目id',
  `category_id` bigint(20) NOT NULL COMMENT '服务类型id',
  `service_id` bigint(20) DEFAULT NULL COMMENT '服务id',
  `click_type` tinyint(4) NOT NULL COMMENT '点击类型： 3-进入详情 4-点击提交 5-点击咨询 6-点击分享 20-提交申请',
  `click_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '点击总数/提交申请次数',
  `click_date` date NOT NULL COMMENT '点击日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '该记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_click_date` (`click_date`),
  KEY `i_eh_service_id` (`service_id`),
  KEY `i_eh_category_id` (`category_id`),
  KEY `i_eh_owner_id` (`owner_id`),
  KEY `i_eh_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='统计各个服务每天的各类型用户行为点击数。';

-- ------------------------------
-- 用户点击明细表     add by mingbo.huang  2018/07/25
-- ------------------------------
CREATE TABLE `eh_alliance_stat_details` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `type` bigint(20) NOT NULL COMMENT '服务联盟类型id',
  `owner_id` bigint(20) NOT NULL COMMENT '所属项目id',
  `category_id` bigint(20) NOT NULL COMMENT '服务类型id',
  `service_id` bigint(20) DEFAULT NULL COMMENT '服务id',
  `user_id` bigint(20) NOT NULL,
  `user_name` varchar(64) DEFAULT NULL,
  `user_phone` varchar(20) DEFAULT NULL,
  `click_type` tinyint(4) NOT NULL COMMENT '点击类型：1-首页点击服务 3-进入详情 4-点击提交 5-点击咨询 6-点击分享',
  `click_time` bigint(20) NOT NULL COMMENT '点击时间戳',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录生成时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_service_id` (`service_id`),
  KEY `i_eh_category_id` (`category_id`),
  KEY `i_eh_click_time` (`click_time`),
  KEY `i_eh_owner_id` (`owner_id`),
  KEY `i_eh_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户的点击明细';
