/*
Navicat MySQL Data Transfer

Source Server         : localMysql
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : payment2.0

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-08-09 14:49:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for eh_payment_bills
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bills`;
CREATE TABLE `eh_payment_bills` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  `bill_group_id` bigint(20) DEFAULT NULL,
  `date_str` varchar(10) DEFAULT NULL,
  `contract_id` bigint(20) NOT NULL DEFAULT '0',
  `amount_receivable` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount should be received',
  `amount_received` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount actually received by far',
  `amount_owed` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'unpaid amount',
  `amount_exemption` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount reduced',
  `amount_supplement` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount increased',
  `target_type` varchar(32) DEFAULT NULL COMMENT 'untrack, user',
  `target_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'target user id if target_type is a user',
  `targetName` varchar(32) DEFAULT '' COMMENT '客户名称',
  `noticeTel` varchar(32) DEFAULT '' COMMENT '催缴电话',
  `status` tinyint(4) DEFAULT '0' COMMENT '0: upfinished; 1: paid off',
  `notice_times` int(11) DEFAULT '0' COMMENT 'times bill owner has been called for dued payments',
  `switch` tinyint(4) DEFAULT '0' COMMENT '0:未出账单；1：已出账单；3：其他作用状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

-- ----------------------------
-- Table structure for eh_payment_bill_groups
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bill_groups`;
CREATE TABLE `eh_payment_bill_groups` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `balance_date_type` tinyint(4) DEFAULT NULL COMMENT '1:pay each month; 2:each quarter; 3:each year',
  `bills_day` int(11) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单组表';

-- ----------------------------
-- Table structure for eh_payment_bill_groups_rules
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bill_groups_rules`;
CREATE TABLE `eh_payment_bill_groups_rules` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `bill_group_id` bigint(20) NOT NULL DEFAULT '0',
  `charging_item_id` bigint(20) NOT NULL DEFAULT '0',
  `charging_standards_id` bigint(20) NOT NULL DEFAULT '0',
  `charging_item_name` varchar(32) DEFAULT NULL,
  `variables_json_string` varchar(2048) DEFAULT NULL COMMENT 'json strings of variables injected for a particular formula',
  `ownerType` varchar(32) NOT NULL,
  `ownerId` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='变量注入表';

-- ----------------------------
-- Table structure for eh_payment_bill_items
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bill_items`;
CREATE TABLE `eh_payment_bill_items` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(20) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  `bill_group_id` bigint(20) DEFAULT NULL,
  `charging_items_id` bigint(20) NOT NULL DEFAULT '0',
  `bill_id` bigint(20) NOT NULL DEFAULT '0',
  `amount_receivable` decimal(10,2) DEFAULT '0.00',
  `amount_received` decimal(10,2) DEFAULT '0.00',
  `amount_owed` decimal(10,2) DEFAULT '0.00',
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `targetName` varchar(32) DEFAULT NULL COMMENT '客户名称，客户没有在系统中时填写',
  `addressId` bigint(20) DEFAULT NULL,
  `date_str` varchar(10) DEFAULT NULL COMMENT '账期',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `charging_item_name` varchar(32) NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0：未缴费;1:已缴费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='明细';

-- ----------------------------
-- Table structure for eh_payment_bill_notice_records
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_bill_notice_records`;
CREATE TABLE `eh_payment_bill_notice_records` (
  `id` bigint(20) NOT NULL,
  `bill_id` bigint(20) NOT NULL DEFAULT '0',
  `notice_date` datetime DEFAULT NULL,
  `target_type` varchar(32) DEFAULT NULL COMMENT 'untrack, user',
  `target_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'target user id if target_type is a user',
  `target_name` varchar(32) DEFAULT NULL,
  `target_contact_tel` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发送记录表';

-- ----------------------------
-- Table structure for eh_payment_charging_items
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_charging_items`;
CREATE TABLE `eh_payment_charging_items` (
  `id` bigint(20) NOT NULL,
  `name` varchar(15) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费项目表';

-- ----------------------------
-- Table structure for eh_payment_charging_item_scopes
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_charging_item_scopes`;
CREATE TABLE `eh_payment_charging_item_scopes` (
  `id` bigint(20) NOT NULL,
  `charging_item_id` bigint(20) NOT NULL DEFAULT '0',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费项目范围表';

-- ----------------------------
-- Table structure for eh_payment_charging_standards
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_charging_standards`;
CREATE TABLE `eh_payment_charging_standards` (
  `id` bigint(20) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `charging_items_id` bigint(20) NOT NULL DEFAULT '0',
  `formula` varchar(1024) DEFAULT NULL,
  `formula_type` tinyint(4) DEFAULT NULL COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` tinyint(4) DEFAULT NULL,
  `price_unit_type` tinyint(4) DEFAULT NULL COMMENT '1:日单价; 2:月单价; 3:季单价; 4:年单价',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';

-- ----------------------------
-- Table structure for eh_payment_charging_standards_scopes
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_charging_standards_scopes`;
CREATE TABLE `eh_payment_charging_standards_scopes` (
  `id` bigint(20) NOT NULL,
  `charging_standard_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL,
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';

-- ----------------------------
-- Table structure for eh_payment_exemption_items
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_exemption_items`;
CREATE TABLE `eh_payment_exemption_items` (
  `id` bigint(20) NOT NULL,
  `bill_id` bigint(20) NOT NULL DEFAULT '0',
  `bill_group_id` bigint(20) DEFAULT NULL,
  `target_type` varchar(255) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `targetName` varchar(255) DEFAULT NULL COMMENT '客户名称，客户没有在系统中时填写',
  `remarks` varchar(255) DEFAULT NULL,
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='额外项';

-- ----------------------------
-- Table structure for eh_payment_variables
-- ----------------------------
DROP TABLE IF EXISTS `eh_payment_variables`;
CREATE TABLE `eh_payment_variables` (
  `id` bigint(20) NOT NULL,
  `charging_standard_id` bigint(20) DEFAULT NULL,
  `charging_items_id` bigint(20) NOT NULL,
  `name` varchar(10) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='变量表';
