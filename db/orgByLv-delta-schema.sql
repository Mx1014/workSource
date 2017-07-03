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
