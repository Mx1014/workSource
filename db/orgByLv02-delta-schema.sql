/*
Navicat MySQL Data Transfer

Source Server         : my
Source Server Version : 50626
Source Host           : 10.1.10.37:3306
Source Database       : ehcore

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2017-06-19 11:04:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for eh_user_organization
-- ----------------------------
DROP TABLE IF EXISTS `eh_user_organization`;
CREATE TABLE `eh_user_organization` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `user_id` bigint(20) NOT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  `group_path` varchar(128) DEFAULT NULL COMMENT 'refer to the organization path',
  `group_type` varchar(64) DEFAULT NULL COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: confirming, 2: active',
  `namespace_id` int(11) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `visible_flag` tinyint(4) DEFAULT '0' COMMENT '0 show 1 hide',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
