SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for eh_user_organization
-- ----------------------------
-- DROP TABLE IF EXISTS `eh_user_organizations`;
CREATE TABLE `eh_user_organizations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `user_id` bigint(20) NOT NULL,
  `organization_id` bigint(20) DEFAULT 0,
  `group_path` varchar(128) COMMENT 'refer to the organization path',
  `group_type` varchar(64) COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER',
  `status` tinyint(4) COMMENT '0: inactive, 1: confirming, 2: active',
  `namespace_id` int(11) DEFAULT '0',
  `create_time` datetime,
  `visible_flag` tinyint(4) DEFAULT '0' COMMENT '0 show 1 hide',
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_uniongroup_configures`;
CREATE TABLE `eh_uniongroup_configures` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_id` bigint(20) DEFAULT 0,
  `group_type` varchar(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` bigint(20) NOT NULL COMMENT 'id of group',
  `target_id` bigint(20) DEFAULT NULL COMMENT 'id of target, organization or memberDetail',
  `target_type` varchar(32) COMMENT 'organziation,memberDetail',
  `operator_uid` bigint(20),
  `update_time` datetime,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;