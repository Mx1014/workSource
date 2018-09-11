CREATE TABLE `eh_visitor_sys_door_access` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace id',
  `owner_type` varchar(64) NOT NULL COMMENT 'community or organization',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `door_access_id` bigint(20) DEFAULT NOT NULL COMMENT '门禁组Id',
  `door_access_name` varchar(256) DEFAULT NULL COMMENT '门禁组名称',
  `auth_rule_type` tinyint(4) DEFAULT NULL COMMENT '授权规则种类，0 时长，1 次数',
  `max_duration` int DEFAULT 0 COMMENT '访客授权最长有效期',
  `max_count` int DEFAULT 0 COMMENT '访客授权最大次数',
  `default_door_access_flag` tinyint(4) DEFAULT 0 COMMENT '默认门禁组 0 非默认 1 默认',
  `status` tinyint(4) DEFAULT '2' COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门禁对接表';