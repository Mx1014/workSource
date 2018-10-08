CREATE TABLE `eh_visitor_sys_door_access` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT 'namespace id',
  `owner_type` varchar(64) NOT NULL COMMENT 'community or organization',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `door_access_id` bigint(20) NOT NULL COMMENT '门禁组Id',
  `door_access_name` varchar(256) DEFAULT NULL COMMENT '门禁组名称',
	`default_auth_duration_type` TINYINT(4) DEFAULT 0 COMMENT '默认访客授权有效期种类,0 天数，1 小时数',
  `default_auth_duration` int DEFAULT 0 COMMENT '默认访客授权有效期',
	`default_enable_auth_count` TINYINT(4) DEFAULT 0 COMMENT '默认访客授权次数开关 0 关 1 开',
  `default_auth_count` int DEFAULT 0 COMMENT '默认访客授权次数',
  `default_door_access_flag` tinyint(4) DEFAULT 0 COMMENT '默认门禁组 0 非默认 1 默认',
  `status` tinyint(4) DEFAULT '2' COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客门禁授权默认方案表';

ALTER TABLE `eh_visitor_sys_visitors`
ADD COLUMN `door_access_auth_duration_type` tinyint(4) NULL COMMENT '访客授权有效期种类,0 天数，1 小时数',
ADD COLUMN `door_access_auth_duration` int NULL COMMENT '访客授权有效期',
ADD COLUMN `door_access_enable_auth_count` TINYINT(4) DEFAULT 0 COMMENT '访客授权次数开关 0 关 1 开',
ADD COLUMN `door_access_auth_count` int NULL COMMENT '访客授权次数';