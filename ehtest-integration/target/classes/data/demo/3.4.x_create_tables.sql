-- MySQL dump 10.13  Distrib 5.6.26, for Linux (x86_64)
--
-- Host: localhost    Database: ehcore_20160524
-- ------------------------------------------------------
-- Server version	5.6.26-log

SET foreign_key_checks = 0;
SET autocommit=0;

--
-- Table structure for table `eh_acl_privileges`
--

DROP TABLE IF EXISTS `eh_acl_privileges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_acl_privileges` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `app_id` bigint(20) DEFAULT NULL,
  `name` varchar(32) NOT NULL COMMENT 'name of the operation privilege',
  `description` varchar(512) DEFAULT NULL COMMENT 'privilege description',
  `tag` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_acl_priv_app_id_name` (`app_id`,`name`),
  KEY `u_eh_acl_priv_tag` (`tag`)
) ENGINE=InnoDB AUTO_INCREMENT=4096 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_acl_role_assignments`
--

DROP TABLE IF EXISTS `eh_acl_role_assignments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_acl_role_assignments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL COMMENT 'owner resource(i.e., forum) type',
  `owner_id` bigint(20) DEFAULT NULL COMMENT 'owner resource(i.e., forum) id',
  `target_type` varchar(32) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` bigint(20) DEFAULT NULL COMMENT 'target object(user/group) id',
  `role_id` bigint(20) NOT NULL COMMENT 'role id that is assigned',
  `creator_uid` bigint(20) NOT NULL COMMENT 'assignment creator uid',
  `create_time` datetime DEFAULT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_acl_role_asgn_unique` (`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`),
  KEY `i_eh_acl_role_asgn_owner` (`owner_type`,`owner_id`),
  KEY `i_eh_acl_role_asgn_creator` (`creator_uid`),
  KEY `i_eh_acl_role_asgn_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_acl_roles`
--

DROP TABLE IF EXISTS `eh_acl_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_acl_roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `app_id` bigint(20) DEFAULT NULL,
  `name` varchar(32) NOT NULL COMMENT 'name of hte operating role',
  `description` varchar(512) DEFAULT NULL,
  `tag` varchar(32) DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_acl_role_name` (`namespace_id`,`app_id`,`name`,`owner_type`,`owner_id`),
  KEY `u_eh_acl_role_tag` (`tag`),
  KEY `i_eh_ach_role_owner` (`namespace_id`,`app_id`,`owner_type`,`owner_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4101 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_aclink_firmware`
--

DROP TABLE IF EXISTS `eh_aclink_firmware`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_aclink_firmware` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `firmware_type` tinyint(4) NOT NULL COMMENT 'firmware type',
  `major` int(11) NOT NULL DEFAULT '0',
  `minor` int(11) NOT NULL DEFAULT '0',
  `revision` int(11) NOT NULL DEFAULT '0',
  `checksum` bigint(20) NOT NULL,
  `md5sum` varchar(64) DEFAULT NULL,
  `download_url` varchar(128) DEFAULT NULL,
  `info_url` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL COMMENT '0: invalid, 1: valid',
  `create_time` datetime DEFAULT NULL,
  `creator_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `owner_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_aclink_undo_key`
--

DROP TABLE IF EXISTS `eh_aclink_undo_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_aclink_undo_key` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `key_id` int(11) NOT NULL COMMENT 'cancel a key, must notify all users for this key_id to update',
  `door_id` bigint(20) NOT NULL,
  `status` tinyint(4) NOT NULL COMMENT '0: invalid, 1: requesting, 2: confirm',
  `expire_time_ms` bigint(20) NOT NULL,
  `create_time_ms` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_aclink_undo_key_door_id` (`door_id`),
  CONSTRAINT `eh_aclink_undo_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_aclinks`
--

DROP TABLE IF EXISTS `eh_aclinks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_aclinks` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `door_id` bigint(20) NOT NULL,
  `device_name` varchar(32) NOT NULL,
  `manufacturer` varchar(32) NOT NULL,
  `firware_ver` varchar(32) NOT NULL,
  `driver` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'identify the hardware driver of aclink, not used now',
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_aclink_door_id` (`door_id`),
  CONSTRAINT `eh_aclinks_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_acls`
--

DROP TABLE IF EXISTS `eh_acls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_acls` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `grant_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0 - decline, 1 - grant',
  `privilege_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `order_seq` int(11) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) NOT NULL COMMENT 'assignment creator uid',
  `create_time` datetime NOT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`),
  KEY `i_eh_acl_owner_privilege` (`owner_type`,`owner_id`),
  KEY `i_eh_acl_owner_order_seq` (`order_seq`),
  KEY `i_eh_acl_creator` (`creator_uid`),
  KEY `i_eh_acl_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_activities`
--

DROP TABLE IF EXISTS `eh_activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_activities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `namespace_id` int(11) DEFAULT NULL,
  `subject` varchar(512) DEFAULT NULL,
  `description` text,
  `poster_uri` varchar(1024) DEFAULT NULL COMMENT 'poster uri',
  `tag` varchar(32) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(64) DEFAULT NULL,
  `location` text,
  `contact_person` varchar(128) DEFAULT NULL,
  `contact_number` varchar(64) DEFAULT NULL,
  `start_time_ms` bigint(20) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time_ms` bigint(20) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `signup_flag` tinyint(4) DEFAULT NULL,
  `confirm_flag` tinyint(4) DEFAULT NULL,
  `max_attendee_count` int(11) DEFAULT NULL,
  `signup_attendee_count` int(11) DEFAULT NULL,
  `signup_family_count` int(11) DEFAULT NULL,
  `checkin_attendee_count` int(11) DEFAULT NULL,
  `checkin_family_count` int(11) DEFAULT NULL,
  `confirm_attendee_count` int(11) DEFAULT NULL,
  `confirm_family_count` int(11) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `creator_family_id` bigint(20) DEFAULT NULL,
  `post_id` bigint(20) DEFAULT NULL COMMENT 'associated post id',
  `group_discriminator` varchar(32) DEFAULT NULL COMMENT 'associated group if any',
  `group_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: drafting, 2: active',
  `change_version` int(11) NOT NULL DEFAULT '1',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  `guest` varchar(2048) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  KEY `i_eh_act_start_time_ms` (`start_time_ms`),
  KEY `i_eh_act_end_time_ms` (`end_time_ms`),
  KEY `i_eh_act_creator_uid` (`creator_uid`),
  KEY `i_eh_act_post_id` (`post_id`),
  KEY `i_eh_act_group` (`group_discriminator`,`group_id`),
  KEY `i_eh_act_create_time` (`create_time`),
  KEY `i_eh_act_delete_time` (`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_activity_roster`
--

DROP TABLE IF EXISTS `eh_activity_roster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_activity_roster` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(36) NOT NULL,
  `activity_id` bigint(20) NOT NULL,
  `uid` bigint(20) DEFAULT NULL,
  `family_id` bigint(20) DEFAULT NULL,
  `adult_count` int(11) NOT NULL DEFAULT '0',
  `child_count` int(11) NOT NULL DEFAULT '0',
  `checkin_flag` tinyint(4) NOT NULL DEFAULT '0',
  `checkin_uid` bigint(20) DEFAULT NULL COMMENT 'id of checkin user',
  `confirm_flag` bigint(20) NOT NULL DEFAULT '0',
  `confirm_uid` bigint(20) DEFAULT NULL,
  `confirm_family_id` bigint(20) DEFAULT NULL,
  `confirm_time` datetime DEFAULT NULL,
  `lottery_flag` tinyint(4) NOT NULL DEFAULT '0',
  `lottery_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_act_roster_uuid` (`uuid`),
  UNIQUE KEY `u_eh_act_roster_user` (`activity_id`,`uid`),
  KEY `i_eh_act_roster_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_address_messages`
--

DROP TABLE IF EXISTS `eh_address_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_address_messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appId` bigint(20) DEFAULT NULL,
  `address_id` bigint(11) NOT NULL,
  `sender_type` varchar(32) DEFAULT NULL,
  `sender_token` varchar(32) DEFAULT NULL,
  `sender_login_id` int(11) DEFAULT NULL,
  `sender_identify` varchar(128) DEFAULT NULL,
  `body` varchar(256) DEFAULT NULL,
  `meta` varchar(512) DEFAULT NULL,
  `extra` varchar(512) DEFAULT NULL,
  `sender_tag` varchar(32) DEFAULT NULL,
  `body_type` varchar(32) DEFAULT NULL,
  `deliveryOption` int(11) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_addresses`
--

DROP TABLE IF EXISTS `eh_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `community_id` bigint(20) DEFAULT NULL COMMENT 'NULL: means it is an independent street address, otherwise, it is an appartment address',
  `city_id` bigint(20) DEFAULT NULL,
  `city_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `area_id` bigint(20) NOT NULL COMMENT 'area id in region table',
  `area_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `zipcode` varchar(16) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `address_alias` varchar(128) DEFAULT NULL,
  `building_name` varchar(128) DEFAULT NULL,
  `building_alias_name` varchar(128) DEFAULT NULL,
  `apartment_name` varchar(128) DEFAULT NULL,
  `apartment_floor` varchar(16) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `operator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'uid of the user who process the address',
  `operate_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'uid of the user who has suggested address, NULL if it is system created',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `area_size` double DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `i_eh_addr_city` (`city_id`),
  KEY `i_eh_addr_community` (`community_id`),
  KEY `i_eh_addr_address_alias` (`address_alias`),
  KEY `i_eh_addr_building_apt_name` (`building_name`,`apartment_name`),
  KEY `i_eh_addr_building_alias_apt_name` (`building_alias_name`,`apartment_name`),
  KEY `i_eh_addr_address` (`address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_aes_server_key`
--

DROP TABLE IF EXISTS `eh_aes_server_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_aes_server_key` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record, also as secret_ver',
  `door_id` bigint(20) NOT NULL,
  `device_ver` tinyint(4) NOT NULL COMMENT 'ver of aclink: 0x0 or 0x1',
  `secret_ver` bigint(20) NOT NULL COMMENT 'ignore it',
  `secret` varchar(64) NOT NULL COMMENT 'The base64 secret 16B',
  `create_time_ms` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_aes_server_key_door_id` (`door_id`),
  CONSTRAINT `eh_aes_server_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_aes_user_key`
--

DROP TABLE IF EXISTS `eh_aes_user_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_aes_user_key` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `key_id` int(11) NOT NULL COMMENT 'lazy load for aes_user_key',
  `key_type` tinyint(4) NOT NULL COMMENT '0: aclink normal key',
  `door_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `auth_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'auth id of user key',
  `expire_time_ms` bigint(20) NOT NULL,
  `create_time_ms` bigint(20) NOT NULL,
  `creator_uid` bigint(20) NOT NULL,
  `secret` varchar(64) NOT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_aes_user_key_door_id` (`door_id`),
  CONSTRAINT `eh_aes_user_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_app_profiles`
--

DROP TABLE IF EXISTS `eh_app_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_app_profiles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `app_id` bigint(20) NOT NULL COMMENT 'owner app id',
  `item_group` varchar(32) DEFAULT NULL COMMENT 'for profile grouping purpose',
  `item_name` varchar(32) DEFAULT NULL,
  `item_value` text,
  `item_tag` varchar(32) DEFAULT NULL COMMENT 'for profile value tagging purpose',
  PRIMARY KEY (`id`),
  KEY `i_eh_appprof_app_id_group` (`app_id`,`item_group`),
  CONSTRAINT `eh_app_profiles_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `eh_apps` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_app_promotions`
--

DROP TABLE IF EXISTS `eh_app_promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_app_promotions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `channel` int(11) DEFAULT NULL COMMENT '1: offical site, 2: app store, 3: manual',
  `version` varchar(256) DEFAULT NULL,
  `path` varchar(1024) DEFAULT NULL,
  `file_name` varchar(128) DEFAULT NULL,
  `link` varchar(1024) DEFAULT NULL,
  `download_count` int(11) NOT NULL DEFAULT '0',
  `register_count` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  PRIMARY KEY (`id`),
  KEY `i_app_promo_create_time` (`create_time`),
  KEY `i_app_promo_delete_time` (`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_apps`
--

DROP TABLE IF EXISTS `eh_apps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_apps` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `creator_uid` bigint(20) DEFAULT NULL,
  `app_key` varchar(64) DEFAULT NULL,
  `secret_key` varchar(1024) DEFAULT NULL,
  `name` varchar(128) NOT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0 - inactive, 1 - active',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_app_reg_name` (`name`),
  UNIQUE KEY `u_eh_app_reg_app_key` (`app_key`),
  KEY `i_eh_app_reg_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=4101 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_audit_logs`
--

DROP TABLE IF EXISTS `eh_audit_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_audit_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) DEFAULT NULL COMMENT 'application that provides the operation',
  `operator_uid` bigint(20) DEFAULT NULL,
  `requestor_uid` bigint(20) DEFAULT NULL COMMENT 'user who initiated the original request',
  `requestor_comment` text,
  `operation_type` varchar(32) DEFAULT NULL,
  `result_code` int(11) DEFAULT NULL COMMENT '0: common positive result, otherwise, application defined result code',
  `reason` varchar(256) DEFAULT NULL,
  `resource_type` varchar(32) DEFAULT NULL COMMENT 'operation related resource type',
  `resource_id` bigint(20) DEFAULT NULL COMMENT 'operation related resource id',
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'time of the operation that was performed',
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  PRIMARY KEY (`id`),
  KEY `i_eh_audit_operator_uid` (`operator_uid`),
  KEY `i_eh_audit_requestor_uid` (`requestor_uid`),
  KEY `i_eh_audit_create_time` (`create_time`),
  KEY `i_eh_audit_delete_time` (`delete_time`),
  KEY `i_eh_audit_itag1` (`integral_tag1`),
  KEY `i_eh_audit_itag2` (`integral_tag2`),
  KEY `i_eh_audit_stag1` (`string_tag1`),
  KEY `i_eh_audit_stag2` (`string_tag2`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_banner_clicks`
--

DROP TABLE IF EXISTS `eh_banner_clicks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_banner_clicks` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(36) NOT NULL,
  `banner_id` bigint(20) NOT NULL,
  `uid` bigint(20) NOT NULL,
  `family_id` bigint(20) DEFAULT NULL COMMENT 'redundant info for query optimization',
  `click_count` bigint(20) DEFAULT NULL,
  `last_click_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_banner_clk_uuid` (`uuid`),
  UNIQUE KEY `u_eh_banner_clk_user` (`banner_id`,`uid`),
  KEY `i_eh_banner_clk_last_time` (`last_click_time`),
  KEY `i_eh_banner_clk_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_banner_orders`
--

DROP TABLE IF EXISTS `eh_banner_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_banner_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `banner_id` bigint(20) NOT NULL,
  `uid` bigint(20) NOT NULL,
  `vendor_order_tag` varchar(64) DEFAULT NULL,
  `amount` decimal(10,0) DEFAULT NULL,
  `description` text,
  `purchase_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_banner_order_banner` (`banner_id`),
  KEY `i_eh_banner_order_user` (`uid`),
  KEY `i_eh_banner_order_purchase_time` (`purchase_time`),
  KEY `i_eh_banner_order_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_banners`
--

DROP TABLE IF EXISTS `eh_banners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_banners` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `appId` bigint(20) DEFAULT NULL,
  `banner_location` varchar(2048) DEFAULT NULL,
  `banner_group` varchar(128) NOT NULL DEFAULT '' COMMENT 'the type to filter item when querying: GA, BIZ, PM, GARC, GANC, GAPS',
  `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` bigint(20) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  `vendor_tag` varchar(64) DEFAULT NULL,
  `poster_path` varchar(128) DEFAULT NULL,
  `action_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'according to document',
  `action_data` text COMMENT 'the parameters depend on item_type, json format',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: closed, 1: waiting for confirmation, 2: active',
  `order` int(11) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  `scene_type` varchar(64) DEFAULT 'default',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1026 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_binary_resources`
--

DROP TABLE IF EXISTS `eh_binary_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_binary_resources` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `checksum` varchar(128) DEFAULT NULL,
  `store_type` varchar(32) DEFAULT NULL COMMENT 'content store type',
  `store_uri` varchar(32) DEFAULT NULL COMMENT 'identify the store instance',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'object content type',
  `content_length` bigint(20) DEFAULT NULL,
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'object link info on storage',
  `reference_count` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `access_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_bin_res_checksum` (`checksum`),
  KEY `i_eh_bin_res_create_time` (`create_time`),
  KEY `i_eh_bin_res_access_time` (`access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_borders`
--

DROP TABLE IF EXISTS `eh_borders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_borders` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `private_address` varchar(128) NOT NULL,
  `private_port` int(11) NOT NULL DEFAULT '8086',
  `public_address` varchar(128) NOT NULL,
  `public_port` int(11) NOT NULL DEFAULT '80',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0 : disabled, 1: enabled',
  `config_tag` varchar(32) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_border_config_tag` (`config_tag`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_building_attachments`
--

DROP TABLE IF EXISTS `eh_building_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_building_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `building_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_buildings`
--

DROP TABLE IF EXISTS `eh_buildings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_buildings` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refering to eh_communities',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT 'building name',
  `alias_name` varchar(128) DEFAULT NULL,
  `manager_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the manager of the building',
  `contact` varchar(128) DEFAULT NULL COMMENT 'the phone number',
  `address` varchar(1024) DEFAULT NULL,
  `area_size` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `description` text,
  `poster_uri` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `operator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'uid of the user who process the address',
  `operate_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'uid of the user who has suggested address, NULL if it is system created',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_community_id_name` (`community_id`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_business_assigned_namespaces`
--

DROP TABLE IF EXISTS `eh_business_assigned_namespaces`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_business_assigned_namespaces` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner business id',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `visible_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'business can see in namespace or not.0-hide,1-visible',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_business_namespace_id` (`owner_id`,`namespace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_business_assigned_scopes`
--

DROP TABLE IF EXISTS `eh_business_assigned_scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_business_assigned_scopes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner business id',
  `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city',
  `scope_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_scope_owner_code_id` (`owner_id`,`scope_code`,`scope_id`),
  KEY `i_eh_bussiness_scope_owner_id` (`owner_id`),
  KEY `i_eh_bussiness_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_business_assigned_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_businesses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_business_categories`
--

DROP TABLE IF EXISTS `eh_business_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_business_categories` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner business id',
  `category_id` bigint(20) NOT NULL DEFAULT '0',
  `category_path` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_bussiness_category_id` (`owner_id`,`category_id`),
  KEY `i_eh_bussiness_owner_id` (`owner_id`),
  CONSTRAINT `eh_business_categories_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_businesses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_business_visible_scopes`
--

DROP TABLE IF EXISTS `eh_business_visible_scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_business_visible_scopes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner business id',
  `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city',
  `scope_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_scope_owner_code_id` (`owner_id`,`scope_code`,`scope_id`),
  KEY `i_eh_bussiness_scope_owner_id` (`owner_id`),
  KEY `i_eh_bussiness_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_business_visible_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_businesses` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_businesses`
--

DROP TABLE IF EXISTS `eh_businesses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_businesses` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `target_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: zuolin biz, 2: third part url',
  `target_id` varchar(1024) NOT NULL DEFAULT '' COMMENT 'the original biz id',
  `biz_owner_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the owner of the shop',
  `contact` varchar(128) DEFAULT NULL COMMENT 'the name of shop owner',
  `phone` varchar(128) DEFAULT NULL COMMENT 'the phone of shop owner',
  `name` varchar(512) NOT NULL DEFAULT '',
  `display_name` varchar(512) NOT NULL DEFAULT '' COMMENT 'the name used to display in desk',
  `logo_uri` varchar(1024) DEFAULT NULL COMMENT 'the logo uri of the shop',
  `url` varchar(1024) DEFAULT NULL COMMENT 'the url to access shop',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `address` varchar(1024) DEFAULT NULL,
  `description` text NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `visible_distance` double DEFAULT '5000',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=120 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_categories`
--

DROP TABLE IF EXISTS `eh_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_categories` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `link_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'point to the linked category (similar to soft link in file system)',
  `name` varchar(64) NOT NULL,
  `path` varchar(128) DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy. It is much more safer to do so if an allocated category is broadly used',
  `logo_uri` varchar(1024) DEFAULT NULL COMMENT 'the logo uri of the category',
  `description` text,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `i_eh_category_path` (`path`),
  KEY `i_eh_category_order` (`default_order`),
  KEY `i_eh_category_delete_time` (`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_certs`
--

DROP TABLE IF EXISTS `eh_certs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_certs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  `cert_type` int(11) NOT NULL,
  `cert_pass` varchar(128) DEFAULT NULL,
  `data` blob NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_certs_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_client_package_files`
--

DROP TABLE IF EXISTS `eh_client_package_files`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_client_package_files` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `package_id` bigint(20) DEFAULT NULL,
  `file_location` varchar(256) DEFAULT NULL,
  `file_name` varchar(128) DEFAULT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `file_md5` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_cpkg_file_package` (`package_id`),
  CONSTRAINT `eh_client_package_files_ibfk_1` FOREIGN KEY (`package_id`) REFERENCES `eh_client_packages` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_client_packages`
--

DROP TABLE IF EXISTS `eh_client_packages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_client_packages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` varchar(64) DEFAULT NULL,
  `version_code` bigint(20) DEFAULT NULL,
  `package_edition` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: user edition, 2: business edition, 3: community edition',
  `device_platform` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: andriod, 2: ios',
  `distribution_channel` int(11) NOT NULL DEFAULT '1' COMMENT '1: official site',
  `tag` varchar(128) DEFAULT NULL,
  `json_params` text,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_communities`
--

DROP TABLE IF EXISTS `eh_communities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `city_id` bigint(20) NOT NULL COMMENT 'city id in region table',
  `city_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `area_id` bigint(20) NOT NULL COMMENT 'area id in region table',
  `area_name` varchar(64) DEFAULT NULL COMMENT 'redundant for query optimization',
  `name` varchar(64) DEFAULT NULL,
  `alias_name` varchar(64) DEFAULT NULL,
  `address` varchar(512) DEFAULT NULL,
  `zipcode` varchar(16) DEFAULT NULL,
  `description` text,
  `detail_description` text,
  `apt_segment1` varchar(64) DEFAULT NULL,
  `apt_segment2` varchar(64) DEFAULT NULL,
  `apt_segment3` varchar(64) DEFAULT NULL,
  `apt_seg1_sample` varchar(64) DEFAULT NULL,
  `apt_seg2_sample` varchar(64) DEFAULT NULL,
  `apt_seg3_sample` varchar(64) DEFAULT NULL,
  `apt_count` int(11) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'user who suggested the creation',
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'operator uid of last operation',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy. historic data may be useful',
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `community_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: residential, 1: commercial',
  `default_forum_id` bigint(20) NOT NULL DEFAULT '1' COMMENT 'the default forum for the community, forum-1 is system default forum',
  `feedback_forum_id` bigint(20) NOT NULL DEFAULT '2' COMMENT 'the default forum for the community, forum-2 is system feedback forum',
  `update_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `area_size` double DEFAULT NULL COMMENT 'area size',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  KEY `i_eh_community_area_name` (`area_name`),
  KEY `i_eh_community_name` (`name`),
  KEY `i_eh_community_alias_name` (`alias_name`),
  KEY `i_eh_community_zipcode` (`zipcode`),
  KEY `i_eh_community_create_time` (`create_time`),
  KEY `i_eh_community_delete_time` (`delete_time`),
  KEY `i_eh_community_itag1` (`integral_tag1`),
  KEY `i_eh_community_itag2` (`integral_tag2`),
  KEY `i_eh_community_stag1` (`string_tag1`),
  KEY `i_eh_community_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_community_geopoints`
--

DROP TABLE IF EXISTS `eh_community_geopoints`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_community_geopoints` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `community_id` bigint(20) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_comm_description` (`description`),
  KEY `i_eh_comm_geopoints` (`geohash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_community_profiles`
--

DROP TABLE IF EXISTS `eh_community_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_community_profiles` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `app_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL COMMENT 'owner community id',
  `item_name` varchar(32) DEFAULT NULL,
  `item_kind` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0, opaque json object, 1: entity',
  `item_value` text,
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_cprof_item` (`app_id`,`owner_id`,`item_name`),
  KEY `i_eh_cprof_owner` (`owner_id`),
  KEY `i_eh_cprof_itag1` (`integral_tag1`),
  KEY `i_eh_cprof_itag2` (`integral_tag2`),
  KEY `i_eh_cprof_stag1` (`string_tag1`),
  KEY `i_eh_cprof_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_account_categories`
--

DROP TABLE IF EXISTS `eh_conf_account_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_account_categories` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `multiple_account_threshold` int(11) NOT NULL DEFAULT '0' COMMENT 'the limit value of mutiple buy channel',
  `conf_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 25方仅视频, 1: 25方支持电话, 2: 100方仅视频, 3: 100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话',
  `min_period` int(11) NOT NULL DEFAULT '1' COMMENT 'the minimum count of months',
  `single_account_price` decimal(10,2) DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `display_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'display when online or offline, 0: all, 1: online, 2: offline',
  `multiple_account_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_account_histories`
--

DROP TABLE IF EXISTS `eh_conf_account_histories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_account_histories` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `enterprise_id` bigint(20) NOT NULL COMMENT 'enterprise_id',
  `expired_date` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: active, 2: locked',
  `account_category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_conf_account_categories',
  `account_type` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: none, 1: trial, 2: normal',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who use the account, reference to the id of eh_users',
  `own_time` datetime DEFAULT NULL COMMENT 'the time when the user own the account',
  `deleter_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who create the account',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operation_type` varchar(32) DEFAULT NULL,
  `process_details` text,
  `operate_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_accounts`
--

DROP TABLE IF EXISTS `eh_conf_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_accounts` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `enterprise_id` bigint(20) NOT NULL COMMENT 'enterprise_id',
  `expired_date` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0-inactive 1-active 2-locked',
  `account_category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_conf_account_categories',
  `account_type` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: none, 1: trial, 2: normal',
  `assigned_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'whether there is a source account assiged to the account, 0: none, 1: assigned',
  `assigned_source_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the source account id assigned to the account, reference to the id of eh_source_accounts',
  `assigned_time` datetime DEFAULT NULL COMMENT 'the time when the source account is assigned to the account',
  `assigned_conf_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'referenece to the id of eh_conf_conferences',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who use the account, reference to the id of eh_users',
  `own_time` datetime DEFAULT NULL COMMENT 'the time when the user own the account',
  `delete_uid` bigint(20) NOT NULL DEFAULT '0',
  `delete_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who create the account',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_conferences`
--

DROP TABLE IF EXISTS `eh_conf_conferences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_conferences` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `meeting_no` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the meeting no from 3rd conference provider',
  `subject` varchar(128) DEFAULT NULL COMMENT 'the conference subject from 3rd conference provider',
  `description` text,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `expect_duration` int(11) NOT NULL DEFAULT '0' COMMENT 'how long the conference expected to last, unit: minute',
  `real_duration` int(11) NOT NULL DEFAULT '0' COMMENT 'how long the conference really lasted, unit: minute',
  `conf_host_id` varchar(128) NOT NULL DEFAULT '0' COMMENT 'the conf host id from 3rd conference provider',
  `conf_host_name` varchar(256) NOT NULL DEFAULT '0' COMMENT 'the conf host name of the conference',
  `max_count` int(11) NOT NULL DEFAULT '0' COMMENT 'the max amount of allowed attendees',
  `conf_host_key` varchar(128) DEFAULT NULL COMMENT 'the password of the conference, set by the creator',
  `join_policy` int(11) NOT NULL DEFAULT '1' COMMENT '0: free join, 1: conf host first',
  `source_account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to eh_source_accounts',
  `conf_account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to eh_conf_accounts',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who create the account',
  `join_url` varchar(256) DEFAULT NULL COMMENT 'user use the url to join the meeting',
  `start_url` varchar(256) DEFAULT NULL COMMENT 'user who start the meeting use this url',
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: close, 1: on progress, 2: failed',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `conference_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the conference id from 3rd conference provider',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_enterprises`
--

DROP TABLE IF EXISTS `eh_conf_enterprises`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_enterprises` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `enterprise_id` bigint(20) NOT NULL COMMENT 'enterprise_id, reference to the id of eh_groups, unique',
  `contact_name` varchar(128) DEFAULT NULL,
  `contact` varchar(128) DEFAULT NULL,
  `account_amount` int(11) NOT NULL DEFAULT '0' COMMENT 'the total amount of active or inactive accounts the enterprise owned',
  `trial_account_amount` int(11) NOT NULL DEFAULT '0' COMMENT 'the total amount of trial accounts the enterprise owned',
  `active_account_amount` int(11) NOT NULL DEFAULT '0' COMMENT 'the total amount of active accounts the enterprise owned',
  `buy_channel` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: offline, 1: online',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active, 2: locked',
  `deleter_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who create the enterrpise',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_enterprise_id` (`enterprise_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_invoices`
--

DROP TABLE IF EXISTS `eh_conf_invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_invoices` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_conf_orders',
  `taxpayer_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: INDIVIDUAL_TAXPAYER, 2: COMPANY_TAXPAYER',
  `vat_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: GENERAL_TAXPAYER, 2: NON_GENERAL_TAXPAYER',
  `expense_type` tinyint(4) DEFAULT NULL COMMENT '0: none, 1: CONF',
  `company_name` varchar(20) DEFAULT NULL,
  `vat_code` varchar(20) DEFAULT NULL,
  `vat_address` varchar(128) DEFAULT NULL,
  `vat_phone` varchar(20) DEFAULT NULL,
  `vat_bank_name` varchar(20) DEFAULT NULL,
  `vat_bank_account` varchar(20) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `zip_code` varchar(20) DEFAULT NULL,
  `consignee` varchar(20) DEFAULT NULL,
  `contact` varchar(20) DEFAULT NULL,
  `contract_flag` tinyint(4) DEFAULT NULL COMMENT '0-dont need 1-need',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_order_account_map`
--

DROP TABLE IF EXISTS `eh_conf_order_account_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_order_account_map` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0',
  `conf_account_id` bigint(20) NOT NULL DEFAULT '0',
  `assiged_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'whether the account has assiged to user, 0: none, 1: assigned',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `conf_account_namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_orders`
--

DROP TABLE IF EXISTS `eh_conf_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the enterprise id who own the order',
  `payer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who pay the order',
  `paid_time` datetime DEFAULT NULL COMMENT 'the pay time of the bill',
  `quantity` int(11) NOT NULL DEFAULT '0' COMMENT 'the quantity of accounts which going to buy',
  `period` int(11) NOT NULL DEFAULT '0' COMMENT 'the months which every account can be used',
  `amount` decimal(10,2) DEFAULT NULL COMMENT 'the paid money amount',
  `description` text,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: waiting for pay, 2: paid',
  `invoice_req_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'whether the order needs invoice or not, 0: none, 1: request',
  `invoice_issue_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'whether the invoice is issued or not, 0: none, 1: invoiced',
  `account_category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_conf_account_categories',
  `online_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: offline, 1: online',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who make the order',
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `buyer_name` varchar(128) DEFAULT NULL,
  `buyer_contact` varchar(128) DEFAULT NULL,
  `vendor_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'vendor type 0: none, 1: Alipay, 2: Wechat',
  `email` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_reservations`
--

DROP TABLE IF EXISTS `eh_conf_reservations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_reservations` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `enterprise_id` bigint(20) NOT NULL COMMENT 'enterprise_id, reference to the id of eh_groups, unique',
  `creator_phone` varchar(20) DEFAULT NULL,
  `conf_account_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'referenece to id of eh_conf_accounts',
  `subject` varchar(128) DEFAULT NULL COMMENT 'the conference subject',
  `description` text,
  `conf_host_name` varchar(256) NOT NULL DEFAULT '0' COMMENT 'the conf host name of the conference',
  `conf_host_key` varchar(128) DEFAULT NULL COMMENT 'the password of the conference, set by the creator',
  `start_time` datetime DEFAULT NULL,
  `time_zone` varchar(64) DEFAULT NULL,
  `duration` int(11) NOT NULL DEFAULT '0' COMMENT 'how long the conference expected to last, unit: minute',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active, 2: locked',
  `update_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who create the reservation',
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_conf_source_accounts`
--

DROP TABLE IF EXISTS `eh_conf_source_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_conf_source_accounts` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `account_name` varchar(128) NOT NULL DEFAULT '',
  `password` varchar(128) DEFAULT NULL,
  `account_category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_conf_account_categories',
  `expired_date` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive 1: active',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_configurations`
--

DROP TABLE IF EXISTS `eh_configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_configurations` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` varchar(64) NOT NULL,
  `value` varchar(256) NOT NULL,
  `description` varchar(256) DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `display_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_content_server`
--

DROP TABLE IF EXISTS `eh_content_server`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_content_server` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'content server id',
  `name` varchar(32) DEFAULT NULL,
  `description` varchar(40) DEFAULT NULL,
  `private_address` varchar(32) DEFAULT NULL,
  `private_port` int(11) DEFAULT NULL,
  `public_address` varchar(32) NOT NULL,
  `public_port` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_content_server_resources`
--

DROP TABLE IF EXISTS `eh_content_server_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_content_server_resources` (
  `id` bigint(20) NOT NULL COMMENT 'the id of record',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `resource_id` text NOT NULL,
  `resource_md5` varchar(256) NOT NULL,
  `resource_type` int(11) NOT NULL COMMENT 'current support audio,image and video',
  `resource_size` int(11) NOT NULL,
  `resource_name` varchar(128) NOT NULL,
  `metadata` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_content_shard_map`
--

DROP TABLE IF EXISTS `eh_content_shard_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_content_shard_map` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `sharding_domain` varchar(32) NOT NULL,
  `sharding_page` bigint(20) DEFAULT NULL,
  `shard_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_csm_domain_page` (`sharding_domain`,`sharding_page`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_cooperation_requests`
--

DROP TABLE IF EXISTS `eh_cooperation_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_cooperation_requests` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cooperation_type` varchar(64) NOT NULL COMMENT 'coperation type, NONE, BIZ, PARK, PM, GA',
  `province_name` varchar(64) DEFAULT NULL COMMENT 'province',
  `city_name` varchar(64) DEFAULT NULL COMMENT 'city',
  `area_name` varchar(64) DEFAULT NULL COMMENT 'area',
  `community_names` text COMMENT 'community name, split with comma if there are multiple communties',
  `address` varchar(128) DEFAULT NULL COMMENT 'address of the cooperator',
  `name` varchar(128) DEFAULT NULL COMMENT 'name of the cooperator entity',
  `contact_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'contact type of cooperator entity, 0: mobile, 1: email',
  `contact_token` varchar(128) DEFAULT NULL COMMENT 'phone number or email address of cooperator entity',
  `applicant_name` varchar(128) DEFAULT NULL COMMENT 'the name of applicant',
  `applicant_occupation` varchar(128) DEFAULT NULL COMMENT 'the occupation of applicant',
  `applicant_phone` varchar(64) DEFAULT NULL COMMENT 'the phone number of applicant',
  `applicant_email` varchar(128) DEFAULT NULL COMMENT 'the email address of applicant',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_devices`
--

DROP TABLE IF EXISTS `eh_devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_devices` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(128) NOT NULL,
  `platform` varchar(32) NOT NULL,
  `product` varchar(32) DEFAULT NULL,
  `brand` varchar(32) DEFAULT NULL,
  `device_model` varchar(32) DEFAULT NULL,
  `system_version` varchar(32) DEFAULT NULL,
  `meta` varchar(256) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_dev_id` (`device_id`),
  KEY `u_eh_dev_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=12149 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_door_access`
--

DROP TABLE IF EXISTS `eh_door_access`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_door_access` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(64) NOT NULL,
  `door_type` tinyint(4) NOT NULL COMMENT '0: Zuolin aclink with wifi, 1: Zuolink aclink without wifi',
  `hardware_id` varchar(64) NOT NULL COMMENT 'mac address of aclink',
  `name` varchar(128) NOT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `avatar` varchar(128) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `active_user_id` bigint(20) NOT NULL,
  `creator_user_id` bigint(20) NOT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(64) DEFAULT NULL,
  `aes_iv` varchar(64) NOT NULL,
  `link_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: linked, 1: failed',
  `owner_type` tinyint(4) NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
  `owner_id` bigint(20) NOT NULL,
  `role` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL COMMENT '0:activing, 1: active',
  `acking_secret_version` int(11) NOT NULL DEFAULT '1',
  `expect_secret_key` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_door_access_uuid` (`uuid`),
  KEY `i_eh_door_access_name` (`name`),
  KEY `i_eh_door_hardware_id` (`hardware_id`),
  KEY `i_eh_door_access_owner` (`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_door_auth`
--

DROP TABLE IF EXISTS `eh_door_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_door_auth` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `door_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `approve_user_id` bigint(20) NOT NULL,
  `auth_type` tinyint(4) NOT NULL COMMENT '0: forever, 1: temperate',
  `valid_from_ms` bigint(20) NOT NULL DEFAULT '0',
  `valid_end_ms` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` tinyint(4) NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
  `owner_id` bigint(20) NOT NULL,
  `organization` varchar(128) DEFAULT NULL,
  `description` varchar(1024) DEFAULT NULL,
  `nickname` varchar(64) DEFAULT NULL,
  `phone` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_door_auth_door_id` (`door_id`),
  CONSTRAINT `eh_door_auth_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_door_command`
--

DROP TABLE IF EXISTS `eh_door_command`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_door_command` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `door_id` bigint(20) NOT NULL,
  `cmd_id` tinyint(4) NOT NULL COMMENT 'cmd id for aclink',
  `cmd_type` tinyint(4) NOT NULL COMMENT 'cmd type for aclink',
  `cmd_body` text COMMENT 'json type of cmd body',
  `cmd_resp` text COMMENT 'json resp of cmd resp body',
  `server_key_ver` bigint(20) NOT NULL COMMENT 'cmd of server key',
  `aclink_key_ver` tinyint(4) NOT NULL COMMENT 'cmd of aclink key',
  `status` tinyint(4) NOT NULL COMMENT '0: creating, 1: sending, 2: response, 3: process, 4: invalid',
  `user_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `owner_type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_door_command_id` (`door_id`),
  CONSTRAINT `eh_door_command_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_addresses`
--

DROP TABLE IF EXISTS `eh_enterprise_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_groups',
  `address_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_addresses',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` tinyint(4) DEFAULT NULL,
  `process_details` text,
  `proof_resource_uri` varchar(1024) DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL COMMENT 'redundant auditing info',
  `update_time` datetime DEFAULT NULL,
  `building_id` bigint(20) NOT NULL DEFAULT '0',
  `building_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_attachments`
--

DROP TABLE IF EXISTS `eh_enterprise_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_community_map`
--

DROP TABLE IF EXISTS `eh_enterprise_community_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_community_map` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_enterprise_communities',
  `member_type` varchar(32) NOT NULL COMMENT 'enterprise',
  `member_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'enterprise_id etc',
  `member_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` tinyint(4) DEFAULT NULL,
  `process_details` text,
  `proof_resource_uri` varchar(1024) DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL COMMENT 'redundant auditing info',
  `requestor_comment` text,
  `operation_type` tinyint(4) DEFAULT NULL COMMENT '1: request to join, 2: invite to join',
  `inviter_uid` bigint(20) DEFAULT NULL COMMENT 'record inviter user id',
  `invite_time` datetime DEFAULT NULL COMMENT 'the time the member is invited',
  `update_time` datetime NOT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_contact_entries`
--

DROP TABLE IF EXISTS `eh_enterprise_contact_entries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_contact_entries` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'enterprise id',
  `contact_id` bigint(20) NOT NULL COMMENT 'contact id',
  `entry_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `entry_value` varchar(128) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_contact_group_members`
--

DROP TABLE IF EXISTS `eh_enterprise_contact_group_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_contact_group_members` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'enterprise id',
  `contact_group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_enterprise_contact_groups',
  `contact_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_enterprise_contacts',
  `role` bigint(20) NOT NULL DEFAULT '7' COMMENT 'The role in company',
  `contact_avatar` varchar(128) DEFAULT NULL COMMENT 'contact avatar image identifier in storage sub-system',
  `contact_nick_name` varchar(128) DEFAULT NULL COMMENT 'contact nick name within the group',
  `contact_status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_contact_groups`
--

DROP TABLE IF EXISTS `eh_enterprise_contact_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_contact_groups` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'enterprise id',
  `role` bigint(20) NOT NULL DEFAULT '7' COMMENT 'The role in company',
  `name` varchar(256) DEFAULT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `path` varchar(128) DEFAULT NULL COMMENT 'path from the root',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_contacts`
--

DROP TABLE IF EXISTS `eh_enterprise_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_contacts` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'enterprise id',
  `name` varchar(256) DEFAULT NULL COMMENT 'real name',
  `nick_name` varchar(256) DEFAULT NULL COMMENT 'display name',
  `avatar` varchar(128) DEFAULT NULL COMMENT 'avatar uri',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'user id reference to eh_users, it determine the contact authenticated or not',
  `role` bigint(20) NOT NULL DEFAULT '7' COMMENT 'The role in company',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_details`
--

DROP TABLE IF EXISTS `eh_enterprise_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_details` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL COMMENT 'group id',
  `description` text COMMENT 'description',
  `contact` varchar(128) DEFAULT NULL COMMENT 'the phone number',
  `address` varchar(256) DEFAULT NULL COMMENT 'address str',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_enterprise_op_requests`
--

DROP TABLE IF EXISTS `eh_enterprise_op_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_enterprise_op_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0',
  `source_type` varchar(128) NOT NULL DEFAULT '' COMMENT 'enterprise, marker zone',
  `source_id` bigint(20) NOT NULL DEFAULT '0',
  `enterprise_name` varchar(128) DEFAULT NULL COMMENT 'enterprise name',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0',
  `apply_contact` varchar(128) DEFAULT NULL COMMENT 'contact phone',
  `apply_user_id` bigint(20) DEFAULT NULL COMMENT 'user id',
  `apply_user_name` varchar(128) DEFAULT NULL COMMENT 'apply user name',
  `apply_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'apply type 1:apply 2:The expansion of rent 3:Renew',
  `description` text COMMENT 'description',
  `size_unit` tinyint(4) DEFAULT NULL COMMENT '1:singleton 2:square meters',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: requesting, 2: accepted',
  `area_size` double DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `process_message` text,
  `process_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_event_profiles`
--

DROP TABLE IF EXISTS `eh_event_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_event_profiles` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `app_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL COMMENT 'owner event id',
  `item_name` varchar(32) DEFAULT NULL,
  `item_kind` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0, opaque json object, 1: entity',
  `item_value` text,
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_evt_prof_item` (`owner_id`,`item_name`),
  KEY `i_eh_evt_prof_owner` (`owner_id`),
  KEY `i_eh_evt_prof_itag1` (`integral_tag1`),
  KEY `i_eh_evt_prof_itag2` (`integral_tag2`),
  KEY `i_eh_evt_prof_stag1` (`string_tag1`),
  KEY `i_eh_evt_prof_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_event_roster`
--

DROP TABLE IF EXISTS `eh_event_roster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_event_roster` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(36) NOT NULL,
  `event_id` bigint(20) NOT NULL,
  `uid` bigint(20) DEFAULT NULL,
  `family_id` bigint(20) DEFAULT NULL,
  `adult_count` int(11) NOT NULL DEFAULT '0',
  `child_count` int(11) NOT NULL DEFAULT '0',
  `signup_flag` tinyint(4) NOT NULL DEFAULT '0',
  `signup_uid` bigint(20) DEFAULT NULL,
  `signup_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_evt_roster_uuid` (`uuid`),
  UNIQUE KEY `u_eh_evt_roster_attendee` (`event_id`,`uid`),
  KEY `i_eh_evt_roster_signup_time` (`signup_time`),
  KEY `i_eh_evt_roster_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_event_ticket_groups`
--

DROP TABLE IF EXISTS `eh_event_ticket_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_event_ticket_groups` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `event_id` bigint(20) DEFAULT NULL,
  `name` varchar(32) DEFAULT NULL,
  `total_count` int(11) DEFAULT NULL,
  `allocated_count` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_evt_tg_name` (`event_id`,`name`),
  KEY `i_eh_evt_tg_event_id` (`event_id`),
  KEY `i_eh_evt_tg_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_event_tickets`
--

DROP TABLE IF EXISTS `eh_event_tickets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_event_tickets` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `event_id` bigint(20) DEFAULT NULL,
  `ticket_group_id` bigint(20) DEFAULT NULL,
  `ticket_number` varchar(128) DEFAULT NULL,
  `uid` bigint(20) DEFAULT NULL,
  `family_id` bigint(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0: free, 1: allocated',
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_evt_ticket_ticket` (`ticket_group_id`,`ticket_number`),
  KEY `i_eh_evt_ticket_event` (`event_id`),
  KEY `i_eh_evt_ticket_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_events`
--

DROP TABLE IF EXISTS `eh_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_events` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `subject` varchar(512) DEFAULT NULL,
  `description` text,
  `location` text,
  `contact_person` varchar(128) DEFAULT NULL,
  `contact_number` varchar(64) DEFAULT NULL,
  `start_time_ms` bigint(20) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time_ms` bigint(20) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `max_attendee_count` int(11) NOT NULL DEFAULT '0',
  `signup_attendee_count` int(11) NOT NULL DEFAULT '0',
  `signup_family_count` int(11) NOT NULL DEFAULT '0',
  `checkin_attendee_count` int(11) NOT NULL DEFAULT '0',
  `checkin_family_count` int(11) NOT NULL DEFAULT '0',
  `ticket_flag` tinyint(4) NOT NULL DEFAULT '0',
  `max_ticket_per_family` int(11) NOT NULL DEFAULT '0',
  `ticket_group_id` bigint(20) DEFAULT NULL,
  `banner_id` bigint(20) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `creator_family_id` bigint(20) DEFAULT NULL,
  `order` int(11) NOT NULL DEFAULT '0',
  `status` int(11) DEFAULT NULL COMMENT '0: inactive, 1: drafting, 2: active',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  PRIMARY KEY (`id`),
  KEY `i_eh_evt_start_time_ms` (`start_time_ms`),
  KEY `i_eh_evt_end_time_ms` (`end_time_ms`),
  KEY `i_eh_evt_creator_uid` (`creator_uid`),
  KEY `i_eh_evt_create_time` (`create_time`),
  KEY `i_eh_evt_delete_time` (`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_family_billing_accounts`
--

DROP TABLE IF EXISTS `eh_family_billing_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_family_billing_accounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `account_number` varchar(128) NOT NULL DEFAULT '0' COMMENT 'the account number which use to identify the unique account',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'address id',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_account_number` (`account_number`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_family_billing_transactions`
--

DROP TABLE IF EXISTS `eh_family_billing_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_family_billing_transactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `tx_sequence` varchar(128) NOT NULL COMMENT 'the sequence binding the two records of a single transaction',
  `tx_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: online, 2: offline',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'address id',
  `owner_account_id` bigint(20) NOT NULL DEFAULT '0',
  `target_account_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: user, 2: family, 3: organization',
  `target_account_id` bigint(20) NOT NULL DEFAULT '0',
  `order_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: bills in eh_organization_bills',
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `charge_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid to target(negative) or received from target(positive)',
  `description` text COMMENT 'the description for the transaction',
  `vendor` varchar(128) DEFAULT '' COMMENT 'which third-part pay vendor is used',
  `pay_account` varchar(128) DEFAULT '' COMMENT 'the pay account from third-part pay vendor',
  `result_code_scope` varchar(128) DEFAULT '' COMMENT 'the scope of result code, defined in zuolin',
  `result_code_id` int(11) NOT NULL DEFAULT '0' COMMENT 'the code id occording to scope',
  `result_desc` varchar(2048) DEFAULT '' COMMENT 'the description of the transaction',
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'the user is who paid the bill, including help others pay the bill',
  `paid_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: selfpay, 2: agent',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_family_followers`
--

DROP TABLE IF EXISTS `eh_family_followers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_family_followers` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_family` bigint(20) NOT NULL,
  `follower_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_fm_follower_follower` (`owner_family`,`follower_uid`),
  KEY `i_eh_fm_follower_owner` (`owner_family`),
  KEY `i_eh_fm_follower_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_feedbacks`
--

DROP TABLE IF EXISTS `eh_feedbacks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_feedbacks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_uid` bigint(20) DEFAULT '0',
  `contact` varchar(128) DEFAULT '',
  `subject` varchar(512) DEFAULT NULL,
  `content` text,
  `create_time` datetime DEFAULT NULL,
  `feedback_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: report, 2-complaint, 3-correct',
  `target_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: post, 2: address, 3: forum',
  `target_id` bigint(20) NOT NULL DEFAULT '0',
  `content_category` bigint(20) NOT NULL DEFAULT '0' COMMENT '0: other, 1: product bug, 2: product improvement, 3: version problem, 11: sensitive info, 12: copyright problem, 13: violent pornography, 14: fraud&fake, 15: disturbance, 21: rumor, 22: malicious marketing, 23: induction',
  `proof_resource_uri` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_forum_assigned_scopes`
--

DROP TABLE IF EXISTS `eh_forum_assigned_scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_forum_assigned_scopes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner post id',
  `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city',
  `scope_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_scope_owner_code_id` (`owner_id`,`scope_code`,`scope_id`),
  KEY `i_eh_post_scope_owner_id` (`owner_id`),
  KEY `i_eh_post_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_forum_assigned_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_forum_posts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_forum_attachments`
--

DROP TABLE IF EXISTS `eh_forum_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_forum_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `post_id` bigint(20) NOT NULL,
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  `orignial_path` varchar(1024) DEFAULT NULL COMMENT 'attachment file path in 2.8 version, keep it for migration',
  PRIMARY KEY (`id`),
  KEY `i_eh_frmatt_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_forum_posts`
--

DROP TABLE IF EXISTS `eh_forum_posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_forum_posts` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `app_id` bigint(20) NOT NULL DEFAULT '2' COMMENT 'default to forum application itself',
  `forum_id` bigint(20) NOT NULL COMMENT 'forum that it belongs',
  `parent_post_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'replied post id',
  `creator_uid` bigint(20) NOT NULL COMMENT 'post creator uid',
  `creator_tag` varchar(128) DEFAULT NULL COMMENT 'post creator tag',
  `target_tag` varchar(128) DEFAULT NULL COMMENT 'post target tag',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(64) DEFAULT NULL,
  `visible_region_type` tinyint(4) DEFAULT NULL COMMENT 'define the visible region type',
  `visible_region_id` bigint(20) DEFAULT NULL COMMENT 'visible region id',
  `visible_region_path` varchar(128) DEFAULT NULL COMMENT 'visible region path',
  `category_id` bigint(20) DEFAULT NULL,
  `category_path` varchar(128) DEFAULT NULL,
  `modify_seq` bigint(20) NOT NULL,
  `child_count` bigint(20) NOT NULL DEFAULT '0',
  `forward_count` bigint(20) NOT NULL DEFAULT '0',
  `like_count` bigint(20) NOT NULL DEFAULT '0',
  `view_count` bigint(20) NOT NULL DEFAULT '0',
  `subject` varchar(512) DEFAULT NULL,
  `content_type` varchar(32) DEFAULT NULL COMMENT 'object content type',
  `content` text COMMENT 'content data, depends on value of content_type',
  `content_abstract` text COMMENT 'abstract of content data',
  `embedded_app_id` bigint(20) DEFAULT NULL,
  `embedded_id` bigint(20) DEFAULT NULL,
  `embedded_json` longtext,
  `embedded_version` int(11) NOT NULL DEFAULT '1',
  `integral_tag1` bigint(20) DEFAULT NULL COMMENT 'user for action category id',
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL COMMENT 'user for action category path',
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `private_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: public, 1: private',
  `assigned_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'the flag indicate the topic is recommanded, 0: none, 1: manual recommand',
  `floor_number` bigint(20) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `deleter_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy. historic data may be useful',
  `tag` varchar(32) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL COMMENT 'publish start time',
  `end_time` datetime DEFAULT NULL COMMENT 'publish end time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  KEY `i_eh_post_seqs` (`modify_seq`),
  KEY `i_eh_post_geohash` (`geohash`),
  KEY `i_eh_post_creator` (`creator_uid`),
  KEY `i_eh_post_itag1` (`integral_tag1`),
  KEY `i_eh_post_itag2` (`integral_tag2`),
  KEY `i_eh_post_stag1` (`string_tag1`),
  KEY `i_eh_post_stag2` (`string_tag2`),
  KEY `i_eh_post_update_time` (`update_time`),
  KEY `i_eh_post_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_forum_visible_scopes`
--

DROP TABLE IF EXISTS `eh_forum_visible_scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_forum_visible_scopes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner post id',
  `scope_code` tinyint(4) DEFAULT NULL,
  `scope_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_post_scope_owner` (`owner_id`),
  KEY `i_eh_post_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_forum_visible_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_forum_posts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_forums`
--

DROP TABLE IF EXISTS `eh_forums`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_forums` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `app_id` bigint(20) NOT NULL DEFAULT '2' COMMENT 'default to forum application itself',
  `owner_type` varchar(32) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `name` varchar(64) NOT NULL,
  `description` text,
  `post_count` bigint(20) NOT NULL DEFAULT '0',
  `modify_seq` bigint(20) NOT NULL,
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  KEY `i_eh_frm_namespace` (`namespace_id`),
  KEY `i_eh_frm_owner` (`owner_type`,`owner_id`),
  KEY `i_eh_frm_post_count` (`post_count`),
  KEY `i_eh_frm_modify_seq` (`modify_seq`),
  KEY `i_eh_frm_update_time` (`update_time`),
  KEY `i_eh_frm_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_group_members`
--

DROP TABLE IF EXISTS `eh_group_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_group_members` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `group_id` bigint(20) NOT NULL,
  `member_type` varchar(32) NOT NULL COMMENT 'member object type, for example, type could be User, Group, etc',
  `member_id` bigint(20) DEFAULT NULL,
  `member_role` bigint(20) NOT NULL DEFAULT '7' COMMENT 'Default to ResourceUser role',
  `member_avatar` varchar(128) DEFAULT NULL COMMENT 'avatar image identifier in storage sub-system',
  `member_nick_name` varchar(128) DEFAULT NULL COMMENT 'member nick name within the group',
  `member_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `create_time` datetime NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` tinyint(4) DEFAULT NULL,
  `process_details` text,
  `proof_resource_uri` varchar(1024) DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL COMMENT 'redundant auditing info',
  `requestor_comment` text,
  `operation_type` tinyint(4) DEFAULT NULL COMMENT '1: request to join, 2: invite to join',
  `inviter_uid` bigint(20) DEFAULT NULL COMMENT 'record inviter user id',
  `invite_time` datetime DEFAULT NULL COMMENT 'the time the member is invited',
  `update_time` datetime NOT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  UNIQUE KEY `u_eh_grp_member` (`group_id`,`member_type`,`member_id`),
  KEY `i_eh_grp_member_group_id` (`group_id`),
  KEY `i_eh_grp_member_member` (`member_type`,`member_id`),
  KEY `i_eh_grp_member_create_time` (`create_time`),
  KEY `i_eh_grp_member_approve_time` (`approve_time`),
  KEY `i_eh_gprof_itag1` (`integral_tag1`),
  KEY `i_eh_gprof_itag2` (`integral_tag2`),
  KEY `i_eh_gprof_stag1` (`string_tag1`),
  KEY `i_eh_gprof_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_group_op_requests`
--

DROP TABLE IF EXISTS `eh_group_op_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_group_op_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `group_id` bigint(20) DEFAULT NULL,
  `requestor_uid` bigint(20) DEFAULT NULL,
  `requestor_comment` text,
  `target_uid` bigint(20) DEFAULT NULL,
  `operation_type` tinyint(4) DEFAULT NULL COMMENT '1: request for admin role, 2: invite to become admin',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: requesting, 2: accepted',
  `operator_uid` bigint(20) DEFAULT NULL,
  `process_message` text,
  `create_time` datetime DEFAULT NULL,
  `process_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_grp_op_group` (`group_id`),
  KEY `i_eh_grp_op_requestor` (`requestor_uid`),
  KEY `i_eh_grp_op_create_time` (`create_time`),
  KEY `i_eh_grp_op_process_time` (`process_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_group_profiles`
--

DROP TABLE IF EXISTS `eh_group_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_group_profiles` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `app_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL COMMENT 'owner group id',
  `item_name` varchar(32) DEFAULT NULL,
  `item_kind` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0, opaque json object, 1: entity',
  `item_value` text,
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_gprof_item` (`app_id`,`owner_id`,`item_name`),
  KEY `i_eh_gprof_owner` (`owner_id`),
  KEY `i_eh_gprof_itag1` (`integral_tag1`),
  KEY `i_eh_gprof_itag2` (`integral_tag2`),
  KEY `i_eh_gprof_stag1` (`string_tag1`),
  KEY `i_eh_gprof_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_group_visible_scopes`
--

DROP TABLE IF EXISTS `eh_group_visible_scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_group_visible_scopes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL COMMENT 'owner group id',
  `scope_code` tinyint(4) DEFAULT NULL,
  `scope_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_grp_scope_owner` (`owner_id`),
  KEY `i_eh_grp_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_group_visible_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_groups` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_groups`
--

DROP TABLE IF EXISTS `eh_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_groups` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(128) NOT NULL,
  `display_name` varchar(64) DEFAULT NULL,
  `avatar` varchar(256) DEFAULT NULL,
  `description` text,
  `creator_uid` bigint(20) NOT NULL,
  `private_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: public, 1: private',
  `join_policy` int(11) NOT NULL DEFAULT '0' COMMENT '0: free join(public group), 1: should be approved by operator/owner, 2: invite only',
  `discriminator` varchar(32) DEFAULT NULL,
  `visibility_scope` tinyint(4) DEFAULT NULL COMMENT 'define the group visibiliy region',
  `visibility_scope_id` bigint(20) DEFAULT NULL COMMENT 'region information, could be an id in eh_regions table or an id in eh_communities',
  `category_id` bigint(20) DEFAULT NULL COMMENT 'group category',
  `category_path` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `member_count` bigint(20) NOT NULL DEFAULT '0',
  `share_count` bigint(20) NOT NULL DEFAULT '0' COMMENT 'How many times the group card is shared',
  `post_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: admin only',
  `tag` varchar(256) DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `update_time` datetime NOT NULL,
  `create_time` datetime NOT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, multi-purpose base entity',
  `visible_region_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'the type of region where the group belong to',
  `visible_region_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of region where the group belong to',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  KEY `i_eh_group_creator` (`creator_uid`),
  KEY `i_eh_group_create_time` (`create_time`),
  KEY `i_eh_group_delete_time` (`delete_time`),
  KEY `i_eh_group_itag1` (`integral_tag1`),
  KEY `i_eh_group_itag2` (`integral_tag2`),
  KEY `i_eh_group_stag1` (`string_tag1`),
  KEY `i_eh_group_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_launch_pad_items`
--

DROP TABLE IF EXISTS `eh_launch_pad_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_launch_pad_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `app_id` bigint(20) DEFAULT NULL,
  `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` bigint(20) DEFAULT NULL,
  `item_location` varchar(2048) DEFAULT NULL,
  `item_group` varchar(128) NOT NULL DEFAULT '' COMMENT 'the type to filter item when querying: Default、GovAgencies、Bizs、GaActions',
  `item_name` varchar(32) DEFAULT NULL,
  `item_label` varchar(64) DEFAULT NULL,
  `icon_uri` varchar(1024) DEFAULT NULL,
  `item_width` int(11) NOT NULL DEFAULT '1',
  `item_height` int(11) NOT NULL DEFAULT '1',
  `action_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'according to document',
  `action_data` text COMMENT 'the parameters depend on item_type, json format',
  `default_order` int(11) NOT NULL DEFAULT '0',
  `apply_policy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: default, 1: override, 2: revert',
  `min_version` bigint(20) NOT NULL DEFAULT '1' COMMENT 'the min version of the item, it will be not supported if current version is less than this',
  `display_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'default display on the pad, 0: hide, 1:display',
  `display_layout` varchar(128) DEFAULT '1' COMMENT 'how many grids it takes at the layout, format: 2x3',
  `bgcolor` int(11) NOT NULL DEFAULT '0',
  `tag` varchar(1024) DEFAULT NULL,
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL COMMENT 'the entity id linked back to the orginal resource',
  `delete_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'whether the item can be deleted from desk, 0: no, 1: yes',
  `scene_type` varchar(64) NOT NULL DEFAULT 'default',
  PRIMARY KEY (`id`),
  KEY `i_eh_scoped_cfg_combo` (`namespace_id`,`app_id`,`scope_code`,`scope_id`,`item_name`),
  KEY `i_eh_scoped_cfg_order` (`default_order`)
) ENGINE=InnoDB AUTO_INCREMENT=10670 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_launch_pad_layouts`
--

DROP TABLE IF EXISTS `eh_launch_pad_layouts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_launch_pad_layouts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(32) DEFAULT NULL,
  `layout_json` text,
  `version_code` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the current version code',
  `min_version_code` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the minmum version code which is compatible',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` datetime DEFAULT NULL,
  `scene_type` varchar(64) NOT NULL DEFAULT 'default',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=205 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_lease_promotion_attachments`
--

DROP TABLE IF EXISTS `eh_lease_promotion_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_lease_promotion_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `lease_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_lease_promotions`
--

DROP TABLE IF EXISTS `eh_lease_promotions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_lease_promotions` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0',
  `rent_type` varchar(128) NOT NULL DEFAULT '' COMMENT 'For rent',
  `poster_uri` varchar(128) DEFAULT NULL,
  `subject` varchar(512) DEFAULT NULL,
  `rent_areas` varchar(100) DEFAULT NULL,
  `description` text,
  `create_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `building_id` bigint(20) NOT NULL DEFAULT '0',
  `rent_position` varchar(128) DEFAULT NULL COMMENT 'rent position',
  `contacts` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(128) DEFAULT NULL,
  `enter_time` datetime DEFAULT NULL COMMENT 'enter time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_links`
--

DROP TABLE IF EXISTS `eh_links`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_links` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `source_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'the source type who refers the link, 0: none, 1: post',
  `source_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the source id depends on source type',
  `title` varchar(1024) DEFAULT NULL,
  `author` varchar(128) DEFAULT NULL,
  `cover_uri` varchar(1024) DEFAULT NULL COMMENT 'cover image uri',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'object content type: link url、rich text',
  `content` longtext COMMENT 'content data, depends on value of content_type',
  `content_abstract` text COMMENT 'abstract of content data',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` datetime DEFAULT NULL,
  `deleter_uid` bigint(20) NOT NULL COMMENT 'deleter id',
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_locale_strings`
--

DROP TABLE IF EXISTS `eh_locale_strings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_locale_strings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scope` varchar(64) DEFAULT NULL,
  `code` varchar(64) DEFAULT NULL,
  `locale` varchar(16) DEFAULT NULL,
  `text` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_lstr_identifier` (`scope`,`code`,`locale`)
) ENGINE=InnoDB AUTO_INCREMENT=189 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_locale_templates`
--

DROP TABLE IF EXISTS `eh_locale_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_locale_templates` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scope` varchar(64) DEFAULT NULL,
  `code` int(11) DEFAULT NULL,
  `locale` varchar(16) DEFAULT NULL,
  `description` varchar(2048) DEFAULT NULL,
  `text` text,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_template_identifier` (`namespace_id`,`scope`,`code`,`locale`)
) ENGINE=InnoDB AUTO_INCREMENT=148 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_message_boxs`
--

DROP TABLE IF EXISTS `eh_message_boxs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_message_boxs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `box_key` varchar(128) DEFAULT NULL,
  `message_id` bigint(20) NOT NULL COMMENT 'foreign key to message record',
  `message_seq` bigint(20) NOT NULL COMMENT 'message sequence id that identifies the message',
  `box_seq` bigint(20) NOT NULL COMMENT 'sequence of the message inside the box',
  `create_time` datetime NOT NULL COMMENT 'time that message goes into the box, taken from create time of the message',
  PRIMARY KEY (`id`),
  KEY `fk_eh_mbx_msg_id` (`message_id`),
  KEY `i_eh_mbx_namespace` (`namespace_id`),
  KEY `i_eh_mbx_msg_seq` (`message_seq`),
  KEY `i_eh_mbx_box_seq` (`box_seq`),
  KEY `i_eh_mbx_create_time` (`create_time`),
  CONSTRAINT `eh_message_boxs_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `eh_messages` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_messages`
--

DROP TABLE IF EXISTS `eh_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_messages` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `app_id` bigint(20) NOT NULL DEFAULT '1' COMMENT 'default to messaging app itself',
  `message_seq` bigint(20) NOT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` bigint(20) NOT NULL,
  `context_type` varchar(32) DEFAULT NULL,
  `context_token` varchar(32) DEFAULT NULL,
  `channel_type` varchar(32) DEFAULT NULL,
  `channel_token` varchar(32) DEFAULT NULL,
  `message_text` mediumtext COMMENT 'message content',
  `meta_app_id` bigint(20) DEFAULT NULL COMMENT 'app that is in charge of message content and meta intepretation',
  `message_meta` mediumtext COMMENT 'JSON encoded message meta info, in format of string to string map',
  `encode_version` int(11) NOT NULL DEFAULT '1' COMMENT 'message meta encode version',
  `sender_tag` varchar(32) DEFAULT NULL COMMENT 'sender generated tag',
  `create_time` datetime NOT NULL COMMENT 'message creation time',
  PRIMARY KEY (`id`),
  KEY `i_eh_msgs_namespace` (`namespace_id`),
  KEY `i_eh_msgs_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_namespace_details`
--

DROP TABLE IF EXISTS `eh_namespace_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_namespace_details` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `resource_type` varchar(128) NOT NULL DEFAULT '' COMMENT 'the type of resource in the namespace, community_residential, community_commercial, community_mix',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_namespace_id` (`namespace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_namespace_resources`
--

DROP TABLE IF EXISTS `eh_namespace_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_namespace_resources` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `resource_type` varchar(128) DEFAULT NULL COMMENT 'COMMUNITY',
  `resource_id` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_namespace_resource_id` (`namespace_id`,`resource_type`,`resource_id`),
  KEY `i_eh_resource_id` (`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_namespaces`
--

DROP TABLE IF EXISTS `eh_namespaces`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_namespaces` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_ns_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1000002 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_nearby_community_map`
--

DROP TABLE IF EXISTS `eh_nearby_community_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_nearby_community_map` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `community_id` bigint(20) NOT NULL DEFAULT '0',
  `nearby_community_id` bigint(20) NOT NULL DEFAULT '0',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_community_relation` (`community_id`,`nearby_community_id`),
  KEY `u_eh_community_id` (`community_id`),
  KEY `u_eh_nearby_community_id` (`nearby_community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_oauth2_codes`
--

DROP TABLE IF EXISTS `eh_oauth2_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_oauth2_codes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `code` varchar(128) NOT NULL COMMENT 'authorization code issued to requestor',
  `app_id` bigint(20) NOT NULL COMMENT 'corresponding to client_id in OAuth2',
  `grantor_uid` bigint(20) NOT NULL COMMENT 'user who authorizes the grant',
  `expiration_time` datetime NOT NULL COMMENT 'a successful acquire of access token by the code should immediately expires it',
  `redirect_uri` varchar(256) DEFAULT NULL COMMENT 'original redirect URI in OAuth2 authorization request',
  `scope` varchar(256) DEFAULT NULL COMMENT 'space-delimited scope tokens per RFC 6749',
  `create_time` datetime DEFAULT NULL,
  `modify_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ocode_code` (`code`),
  KEY `i_eh_ocode_expiration_time` (`expiration_time`),
  KEY `i_eh_ocode_create_time` (`create_time`),
  KEY `i_eh_ocode_modify_time` (`modify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_oauth2_tokens`
--

DROP TABLE IF EXISTS `eh_oauth2_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_oauth2_tokens` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `token_string` varchar(128) NOT NULL COMMENT 'token string issued to requestor',
  `app_id` bigint(20) NOT NULL COMMENT 'corresponding to client_id in OAuth2',
  `grantor_uid` bigint(20) NOT NULL COMMENT 'user who authorizes the grant',
  `expiration_time` datetime NOT NULL COMMENT 'a successful acquire of access token by the code should immediately expires it',
  `scope` varchar(256) DEFAULT NULL COMMENT 'space-delimited scope tokens per RFC 6749',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: access token, 1: refresh token',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_otoken_token_string` (`token_string`),
  KEY `i_eh_otoken_expiration_time` (`expiration_time`),
  KEY `i_eh_otoken_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_op_promotion_activities`
--

DROP TABLE IF EXISTS `eh_op_promotion_activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_op_promotion_activities` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `title` varchar(512) NOT NULL DEFAULT '' COMMENT 'the title of the activity',
  `description` text,
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `policy_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: registerd, 2: reach amount of consumption in zuolin-biz',
  `policy_data` text COMMENT 'json format, the parameters which help executing the policy',
  `icon_uri` varchar(1024) DEFAULT NULL,
  `action_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'the type of what to do, according to document',
  `action_data` text COMMENT 'the parameters depend on ation type, json format',
  `push_message` varchar(1024) DEFAULT NULL COMMENT 'the message need to push',
  `push_count` int(11) NOT NULL DEFAULT '0' COMMENT 'how many user received the push',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `process_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: on process, 2: finished, 3: close',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT '0',
  `integral_tag2` bigint(20) DEFAULT '0',
  `integral_tag3` bigint(20) DEFAULT '0',
  `integral_tag4` bigint(20) DEFAULT '0',
  `integral_tag5` bigint(20) DEFAULT '0',
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_op_promotion_assigned_scopes`
--

DROP TABLE IF EXISTS `eh_op_promotion_assigned_scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_op_promotion_assigned_scopes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `promotion_id` bigint(20) NOT NULL COMMENT 'promotion id',
  `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city',
  `scope_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_scope_promotion_code_id` (`promotion_id`,`scope_code`,`scope_id`),
  CONSTRAINT `eh_op_promotion_assigned_scopes_ibfk_1` FOREIGN KEY (`promotion_id`) REFERENCES `eh_op_promotion_activities` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_op_promotion_messages`
--

DROP TABLE IF EXISTS `eh_op_promotion_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_op_promotion_messages` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `message_seq` bigint(20) NOT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of user who send the message',
  `target_uid` bigint(20) NOT NULL DEFAULT '0',
  `message_text` text COMMENT 'message content',
  `meta_app_id` bigint(20) DEFAULT NULL COMMENT 'app that is in charge of message content and meta intepretation',
  `message_meta` text COMMENT 'JSON encoded message meta info, in format of string to string map',
  `result_data` text COMMENT 'sender generated tag',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_order_account`
--

DROP TABLE IF EXISTS `eh_order_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_order_account` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `order_id` bigint(20) DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT NULL,
  `account_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_order_invoice`
--

DROP TABLE IF EXISTS `eh_order_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_order_invoice` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `order_id` bigint(20) NOT NULL COMMENT 'order_id',
  `taxpayer_type` tinyint(4) DEFAULT NULL COMMENT '0-COMPANY_TAXPAYER 1-INDIVIDUAL_TAXPAYER',
  `vat_type` tinyint(4) DEFAULT NULL COMMENT '0-GENERAL_TAXPAYER 1-NON_GENERAL_TAXPAYER',
  `expense_type` tinyint(4) DEFAULT NULL COMMENT '0-CONF',
  `company_name` varchar(20) DEFAULT NULL,
  `vat_code` varchar(20) DEFAULT NULL,
  `vat_address` varchar(128) DEFAULT NULL,
  `vat_phone` varchar(20) DEFAULT NULL,
  `vat_bankName` varchar(20) DEFAULT NULL,
  `vat_bankAccount` varchar(20) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `zip_code` varchar(20) DEFAULT NULL,
  `consignee` varchar(20) DEFAULT NULL,
  `contact` varchar(20) DEFAULT NULL,
  `contract_flag` tinyint(4) DEFAULT NULL COMMENT '0-dont need 1-need',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_address_mappings`
--

DROP TABLE IF EXISTS `eh_organization_address_mappings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_address_mappings` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL COMMENT 'community id',
  `address_id` bigint(20) NOT NULL COMMENT 'address id',
  `organization_address` varchar(128) DEFAULT NULL COMMENT 'organization address used in organization',
  `living_status` tinyint(4) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_address_mappings_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15623 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_addresses`
--

DROP TABLE IF EXISTS `eh_organization_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_groups',
  `address_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_addresses',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` tinyint(4) DEFAULT NULL,
  `process_details` text,
  `proof_resource_uri` varchar(1024) DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL COMMENT 'redundant auditing info',
  `update_time` datetime DEFAULT NULL,
  `building_id` bigint(20) NOT NULL DEFAULT '0',
  `building_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_assigned_scopes`
--

DROP TABLE IF EXISTS `eh_organization_assigned_scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_assigned_scopes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL,
  `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: building',
  `scope_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_attachments`
--

DROP TABLE IF EXISTS `eh_organization_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_bill_items`
--

DROP TABLE IF EXISTS `eh_organization_bill_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_bill_items` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `bill_id` bigint(20) NOT NULL,
  `item_name` varchar(128) DEFAULT NULL COMMENT 'the tile of bill item',
  `start_count` decimal(10,2) DEFAULT NULL COMMENT 'the start count of bill item for the specific month',
  `end_count` decimal(10,2) DEFAULT NULL COMMENT 'the end count of bill item for the specific month',
  `use_count` decimal(10,2) DEFAULT NULL COMMENT 'the count of bill item which end_count substract start_count',
  `price` decimal(10,2) DEFAULT NULL,
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT 'the money amount of the bill item',
  `description` text,
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'uid of the user who has the bill',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_organization_bill` (`bill_id`),
  CONSTRAINT `eh_organization_bill_items_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `eh_organization_bills` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_billing_accounts`
--

DROP TABLE IF EXISTS `eh_organization_billing_accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_billing_accounts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `account_number` varchar(128) NOT NULL DEFAULT '0' COMMENT 'the account number which use to identify the unique account',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'organization id',
  `balance` decimal(10,2) NOT NULL DEFAULT '0.00',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_account_number` (`account_number`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_billing_transactions`
--

DROP TABLE IF EXISTS `eh_organization_billing_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_billing_transactions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `tx_sequence` varchar(128) NOT NULL COMMENT 'uuid, the sequence binding the two records of a single transaction',
  `tx_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: online, 2: offline',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'organization id',
  `owner_account_id` bigint(20) NOT NULL DEFAULT '0',
  `target_account_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: user, 2: family, 3: organization',
  `target_account_id` bigint(20) NOT NULL DEFAULT '0',
  `order_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: orders in eh_organization_orders',
  `order_id` bigint(20) NOT NULL DEFAULT '0',
  `charge_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid to target(negative) or received from target(positive)',
  `description` text COMMENT 'the description for the transaction',
  `vendor` varchar(128) DEFAULT '' COMMENT 'which third-part pay vendor is used',
  `pay_account` varchar(128) DEFAULT '' COMMENT 'the pay account from third-part pay vendor',
  `result_code_scope` varchar(128) DEFAULT '' COMMENT 'the scope of result code, defined in zuolin',
  `result_code_id` int(11) NOT NULL DEFAULT '0' COMMENT 'the code id occording to scope',
  `result_desc` varchar(2048) DEFAULT '' COMMENT 'the description of the transaction',
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'the user is who paid the bill, including help others pay the bill',
  `paid_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: selfpay, 2: agent',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_bills`
--

DROP TABLE IF EXISTS `eh_organization_bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_bills` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `entity_type` varchar(32) DEFAULT NULL,
  `entity_id` bigint(20) NOT NULL COMMENT 'target address id if target_type is a address',
  `address` varchar(128) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL COMMENT 'the tile of bill',
  `date_str` varchar(128) DEFAULT NULL COMMENT 'the date string in bill',
  `start_date` date DEFAULT NULL COMMENT 'the start date of the bill',
  `end_date` date DEFAULT NULL COMMENT 'the end date of the bill',
  `pay_date` date DEFAULT NULL COMMENT 'the pay date of the bill, the bill must be paid at the end of the date',
  `description` text,
  `due_amount` decimal(10,2) DEFAULT NULL COMMENT 'the money amount of the bill for the current month',
  `owe_amount` decimal(10,2) DEFAULT NULL COMMENT 'the paid money amount of the paid bill',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'uid of the user who has the bill',
  `notify_count` int(11) DEFAULT NULL COMMENT 'how many times of notification is sent for the bill',
  `notify_time` datetime DEFAULT NULL COMMENT 'the last time of notification for the bill',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_bills_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_communities`
--

DROP TABLE IF EXISTS `eh_organization_communities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_communities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL,
  `community_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_org_community_id` (`organization_id`,`community_id`),
  KEY `i_eh_orgc_dept` (`organization_id`),
  KEY `i_eh_orgc_community` (`community_id`)
) ENGINE=InnoDB AUTO_INCREMENT=138 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_community_requests`
--

DROP TABLE IF EXISTS `eh_organization_community_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_community_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_enterprise_communities',
  `member_type` varchar(32) NOT NULL COMMENT 'enterprise',
  `member_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'enterprise_id etc',
  `member_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` tinyint(4) DEFAULT NULL,
  `process_details` text,
  `proof_resource_uri` varchar(1024) DEFAULT NULL,
  `approve_time` datetime DEFAULT NULL COMMENT 'redundant auditing info',
  `requestor_comment` text,
  `operation_type` tinyint(4) DEFAULT NULL COMMENT '1: request to join, 2: invite to join',
  `inviter_uid` bigint(20) DEFAULT NULL COMMENT 'record inviter user id',
  `invite_time` datetime DEFAULT NULL COMMENT 'the time the member is invited',
  `update_time` datetime DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_contacts`
--

DROP TABLE IF EXISTS `eh_organization_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_contacts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `contact_name` varchar(64) DEFAULT NULL,
  `contact_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `contact_token` varchar(128) DEFAULT NULL COMMENT 'phone number or email address',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'uid of the user who has the bill',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_contacts_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_details`
--

DROP TABLE IF EXISTS `eh_organization_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_details` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL COMMENT 'group id',
  `description` text COMMENT 'description',
  `contact` varchar(128) DEFAULT NULL COMMENT 'the phone number',
  `address` varchar(256) DEFAULT NULL COMMENT 'address str',
  `create_time` datetime DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `display_name` varchar(64) DEFAULT NULL,
  `contactor` varchar(64) DEFAULT NULL,
  `member_count` bigint(20) DEFAULT '0',
  `checkin_date` datetime DEFAULT NULL COMMENT 'checkin date',
  `avatar` varchar(128) DEFAULT NULL,
  `post_uri` varchar(128) DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_members`
--

DROP TABLE IF EXISTS `eh_organization_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_members` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL,
  `target_type` varchar(32) DEFAULT NULL COMMENT 'untrack, user',
  `target_id` bigint(20) NOT NULL COMMENT 'target user id if target_type is a user',
  `member_group` varchar(32) DEFAULT NULL COMMENT 'pm group the member belongs to',
  `contact_name` varchar(64) DEFAULT NULL,
  `contact_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `contact_token` varchar(128) DEFAULT NULL COMMENT 'phone number or email address',
  `contact_description` text,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: confirming, 2: active',
  `group_id` bigint(20) DEFAULT '0' COMMENT 'refer to the organization id',
  `employee_no` varchar(128) DEFAULT NULL,
  `avatar` varchar(128) DEFAULT NULL,
  `group_path` varchar(128) DEFAULT NULL COMMENT 'refer to the organization path',
  `gender` tinyint(4) DEFAULT '0' COMMENT '0: undisclosured, 1: male, 2: female',
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `namespace_id` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_eh_orgm_owner` (`organization_id`),
  KEY `i_eh_corg_group` (`member_group`),
  CONSTRAINT `eh_organization_members_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2102645 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_orders`
--

DROP TABLE IF EXISTS `eh_organization_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `bill_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refer to id of eh_organization_bills',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who make the order',
  `payer_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id who pay the order',
  `paid_time` datetime DEFAULT NULL COMMENT 'the pay time of the bill',
  `amount` decimal(10,2) DEFAULT NULL COMMENT 'the paid money amount',
  `description` text,
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: waiting for pay, 2: paid',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_owners`
--

DROP TABLE IF EXISTS `eh_organization_owners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_owners` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `contact_name` varchar(64) DEFAULT NULL,
  `contact_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `contact_token` varchar(128) DEFAULT NULL COMMENT 'phone number or email address',
  `contact_description` text,
  `address_id` bigint(20) NOT NULL COMMENT 'address id',
  `address` varchar(128) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'uid of the user who has the bill',
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_owners_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_role_map`
--

DROP TABLE IF EXISTS `eh_organization_role_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_role_map` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `role_id` bigint(20) NOT NULL DEFAULT '0',
  `private_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: public, 1: private',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 2: active',
  `create_time` datetime DEFAULT NULL,
  `role_name` varchar(128) DEFAULT NULL COMMENT 'role name',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_task_targets`
--

DROP TABLE IF EXISTS `eh_organization_task_targets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_task_targets` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(64) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `target_type` varchar(64) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` bigint(20) DEFAULT NULL COMMENT 'target object(user/group) id',
  `task_type` varchar(64) NOT NULL,
  `message_type` varchar(64) DEFAULT NULL COMMENT 'PUSH COMMENT SMS ',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organization_tasks`
--

DROP TABLE IF EXISTS `eh_organization_tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organization_tasks` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL DEFAULT '0',
  `organization_type` varchar(64) DEFAULT NULL COMMENT 'NONE, PM(Property Management), GARC(Resident Committee), GANC(Neighbor Committee), GAPS(Police Station)',
  `apply_entity_type` varchar(32) DEFAULT NULL COMMENT 'the entity who apply the task, like TOPIC',
  `apply_entity_id` bigint(20) NOT NULL COMMENT 'target topic id if target_type is a topic',
  `target_type` varchar(32) DEFAULT NULL COMMENT 'user',
  `target_id` bigint(20) NOT NULL COMMENT 'target user id if target_type is a user',
  `task_type` varchar(32) DEFAULT NULL COMMENT 'task type assigned by organization',
  `description` text,
  `task_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: unprocessed, 2: processing；3 已处理；4 其他',
  `operator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'uid of the user who process the task',
  `operate_time` datetime DEFAULT NULL,
  `unprocessed_time` datetime DEFAULT NULL,
  `processing_time` datetime DEFAULT NULL,
  `processed_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL COMMENT 'uid of the user who create the task',
  `create_time` datetime DEFAULT NULL,
  `task_category` varchar(128) DEFAULT NULL COMMENT '1:PUBLIC_AREA 2:PRIVATE_OWNER',
  `visible_region_type` tinyint(4) DEFAULT NULL COMMENT 'define the visible region type',
  `visible_region_id` bigint(20) DEFAULT NULL COMMENT 'visible region id',
  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_tasks_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=696 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_organizations`
--

DROP TABLE IF EXISTS `eh_organizations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_organizations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'id of the parent region',
  `organization_type` varchar(64) DEFAULT NULL COMMENT 'NONE, PM(Property Management), GARC(Resident Committee), GANC(Neighbor Committee), GAPS(Police Station)',
  `name` varchar(64) DEFAULT NULL,
  `address_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'address for department',
  `description` text,
  `path` varchar(128) DEFAULT NULL COMMENT 'path from the root',
  `level` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: inactive, 2: active, 3: locked, 4: mark as deleted',
  `department_type` varchar(64) DEFAULT NULL,
  `group_type` varchar(64) DEFAULT NULL COMMENT 'enterprise, department, service_group',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `directly_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'directly under the company',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `group_id` bigint(20) DEFAULT NULL COMMENT 'eh_group id',
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `show_flag` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `i_eh_org_name_level` (`name`,`level`),
  KEY `i_eh_org_path` (`path`),
  KEY `i_eh_org_path_level` (`path`,`level`),
  KEY `i_eh_org_parent` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001051 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_owner_doors`
--

DROP TABLE IF EXISTS `eh_owner_doors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_owner_doors` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` tinyint(4) NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
  `owner_id` bigint(20) NOT NULL,
  `door_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_uq_door_id_owner_id` (`door_id`,`owner_id`,`owner_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_park_apply_card`
--

DROP TABLE IF EXISTS `eh_park_apply_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_park_apply_card` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `applier_id` bigint(20) DEFAULT NULL,
  `applier_name` varchar(20) DEFAULT NULL,
  `applier_phone` varchar(20) DEFAULT NULL,
  `plate_number` varchar(20) DEFAULT NULL,
  `apply_time` datetime DEFAULT NULL,
  `apply_status` tinyint(4) DEFAULT NULL,
  `fetch_status` tinyint(4) DEFAULT NULL,
  `deadline` datetime DEFAULT NULL,
  `community_id` bigint(20) NOT NULL DEFAULT '0',
  `company_name` varchar(256) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_park_charge`
--

DROP TABLE IF EXISTS `eh_park_charge`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_park_charge` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `months` tinyint(4) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `card_type` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_parking_activities`
--

DROP TABLE IF EXISTS `eh_parking_activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_parking_activities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `start_time` datetime DEFAULT NULL COMMENT 'start time',
  `end_time` datetime DEFAULT NULL COMMENT 'end time',
  `top_count` int(11) NOT NULL DEFAULT '0' COMMENT 'Top N user can join the activity',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_parking_card_requests`
--

DROP TABLE IF EXISTS `eh_parking_card_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_parking_card_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `requestor_enterprise_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id of organization where the requestor is in',
  `requestor_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'requestor id',
  `plate_number` varchar(64) DEFAULT NULL,
  `plate_owner_entperise_name` varchar(64) DEFAULT NULL COMMENT 'the enterprise name of plate owner',
  `plate_owner_name` varchar(64) DEFAULT NULL COMMENT 'the name of plate owner',
  `plate_owner_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of plate owner',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: queueing, 2: notified, 3: issued',
  `issue_flag` tinyint(4) DEFAULT NULL COMMENT 'whether the applier fetch the card or not, 0: unissued, 1: issued',
  `issue_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_parking_lots`
--

DROP TABLE IF EXISTS `eh_parking_lots`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_parking_lots` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'used to display',
  `vendor_name` varchar(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `vendor_lot_token` varchar(128) DEFAULT NULL COMMENT 'parking lot id from vendor',
  `card_reserve_days` int(11) NOT NULL DEFAULT '0' COMMENT 'how may days the parking card is reserved for the applicant',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_parking_recharge_orders`
--

DROP TABLE IF EXISTS `eh_parking_recharge_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_parking_recharge_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `order_no` bigint(20) DEFAULT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `plate_number` varchar(64) DEFAULT NULL,
  `plate_owner_name` varchar(64) DEFAULT NULL COMMENT 'the name of plate owner',
  `plate_owner_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of plate owner',
  `payer_enterprise_id` bigint(20) DEFAULT '0' COMMENT 'the id of organization where the payer is in',
  `payer_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the user id of payer',
  `payer_phone` varchar(64) DEFAULT NULL COMMENT 'the phone of payer',
  `paid_time` datetime DEFAULT NULL COMMENT 'the pay time',
  `vendor_name` varchar(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `card_number` varchar(128) NOT NULL DEFAULT '' COMMENT 'it may be number of virtual card or location number',
  `rate_token` varchar(128) NOT NULL DEFAULT '' COMMENT 'it may be from eh_parking_recharge_rates or 3rd system, according to vendor_name',
  `rate_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'rate name for recharging the parking card',
  `month_count` decimal(10,2) DEFAULT NULL COMMENT 'how many months in the rate for recharging the parking card',
  `price` decimal(10,2) DEFAULT NULL COMMENT 'the total price in the item for recharging the parking card',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `recharge_status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: none, 1: unrecharged 1: recharged',
  `recharge_time` datetime DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `old_expired_time` datetime DEFAULT NULL,
  `new_expired_time` datetime DEFAULT NULL,
  `paid_type` varchar(32) DEFAULT NULL COMMENT 'the type of payer',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'the order is delete, 0 : is not deleted, 1: deleted',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_parking_recharge_rates`
--

DROP TABLE IF EXISTS `eh_parking_recharge_rates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_parking_recharge_rates` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `rate_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'rate name for recharging the parking card',
  `month_count` decimal(10,2) DEFAULT NULL COMMENT 'how many months in the rate for recharging the parking card',
  `price` decimal(10,2) DEFAULT NULL COMMENT 'the total price for recharging the parking card',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_parking_vendors`
--

DROP TABLE IF EXISTS `eh_parking_vendors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_parking_vendors` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'the identifier name of the vendor',
  `display_name` varchar(512) NOT NULL DEFAULT '' COMMENT 'the name used to display in desk',
  `description` varchar(2048) DEFAULT NULL COMMENT 'the description of the vendor',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_vender_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_pmsy_communities`
--

DROP TABLE IF EXISTS `eh_pmsy_communities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_pmsy_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `community_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'community id',
  `community_token` varchar(128) NOT NULL DEFAULT '' COMMENT 'the id of community according the third system, siyuan',
  `contact` varchar(64) NOT NULL DEFAULT '' COMMENT 'the contact of user fill in',
  `bill_tip` varchar(256) NOT NULL DEFAULT '' COMMENT 'the bill_tip of user fill in',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_pmsy_order_items`
--

DROP TABLE IF EXISTS `eh_pmsy_order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_pmsy_order_items` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `order_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'eh_pmsy_orders id',
  `bill_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'the id of bill according the third system, siyuan',
  `item_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'the name of item according the third system, siyuan',
  `bill_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the money of bill',
  `resource_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'the id of resource,door according the third system, siyuan',
  `customer_id` varchar(128) NOT NULL DEFAULT '' COMMENT 'the id of customer according the third system, siyuan',
  `create_time` datetime DEFAULT NULL,
  `bill_date` datetime DEFAULT NULL COMMENT 'the date of bill',
  `status` tinyint(4) DEFAULT NULL COMMENT 'the status of the order, 0: fail, 1: success',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_pmsy_orders`
--

DROP TABLE IF EXISTS `eh_pmsy_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_pmsy_orders` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `user_name` varchar(64) DEFAULT NULL COMMENT 'the name of address resource',
  `user_contact` varchar(64) DEFAULT NULL COMMENT 'the phone of address resource',
  `order_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid',
  `paid_time` datetime DEFAULT NULL COMMENT 'the pay time',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `paid_type` varchar(32) DEFAULT NULL COMMENT 'the type of paid 10001:zhifubao 10002: weixin',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_pmsy_payers`
--

DROP TABLE IF EXISTS `eh_pmsy_payers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_pmsy_payers` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `user_name` varchar(64) NOT NULL DEFAULT '' COMMENT 'the name of user fill in',
  `user_contact` varchar(64) NOT NULL DEFAULT '' COMMENT 'the contact of user fill in',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'the status of the payer, 0: inactive, 1: wating, 2: active',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_poll_items`
--

DROP TABLE IF EXISTS `eh_poll_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_poll_items` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `poll_id` bigint(20) DEFAULT NULL,
  `subject` varchar(512) DEFAULT NULL,
  `resource_id` bigint(20) DEFAULT NULL,
  `resource_url` varchar(512) DEFAULT NULL,
  `vote_count` int(11) NOT NULL DEFAULT '0',
  `change_version` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  KEY `i_eh_poll_item_poll` (`poll_id`),
  KEY `i_eh_poll_item_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_poll_votes`
--

DROP TABLE IF EXISTS `eh_poll_votes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_poll_votes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `poll_id` bigint(20) DEFAULT NULL,
  `item_id` bigint(20) DEFAULT NULL,
  `voter_uid` bigint(20) DEFAULT NULL,
  `voter_family_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_eh_poll_vote_voter` (`poll_id`,`item_id`,`voter_uid`),
  KEY `i_eh_poll_vote_poll` (`poll_id`),
  KEY `i_eh_poll_vote_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_polls`
--

DROP TABLE IF EXISTS `eh_polls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_polls` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `subject` varchar(512) DEFAULT NULL,
  `description` text,
  `start_time_ms` bigint(20) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `end_time_ms` bigint(20) DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `multi_select_flag` tinyint(4) NOT NULL DEFAULT '0',
  `anonymous_flag` tinyint(4) NOT NULL DEFAULT '0',
  `poll_count` int(11) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) DEFAULT NULL,
  `creator_family_id` bigint(20) DEFAULT NULL,
  `post_id` bigint(20) DEFAULT NULL COMMENT 'associated post in forum',
  `status` tinyint(4) DEFAULT NULL COMMENT '0: inactive, 1: drafting, 2: active',
  `change_version` int(11) NOT NULL DEFAULT '1',
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  KEY `i_eh_poll_start_time_ms` (`start_time_ms`),
  KEY `i_eh_poll_end_time_ms` (`end_time_ms`),
  KEY `i_eh_poll_creator_uid` (`creator_uid`),
  KEY `i_eh_poll_post_id` (`post_id`),
  KEY `i_eh_poll_create_time` (`create_time`),
  KEY `i_eh_poll_delete_time` (`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_preferential_rules`
--

DROP TABLE IF EXISTS `eh_preferential_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_preferential_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(128) DEFAULT NULL COMMENT 'community',
  `owner_id` bigint(20) NOT NULL,
  `start_time` datetime DEFAULT NULL COMMENT 'start time',
  `end_time` datetime DEFAULT NULL COMMENT 'end time',
  `type` varchar(256) DEFAULT NULL COMMENT 'PARKING',
  `before_nember` bigint(20) DEFAULT NULL,
  `params_json` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_punch_day_logs`
--

DROP TABLE IF EXISTS `eh_punch_day_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_punch_day_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user''s id',
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT 'compay id',
  `punch_date` date DEFAULT NULL COMMENT 'user punch date',
  `arrive_time` time DEFAULT NULL,
  `leave_time` time DEFAULT NULL,
  `work_time` time DEFAULT NULL COMMENT 'how long did employee work',
  `status` tinyint(4) DEFAULT '1' COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `view_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'is view(0) not view(1)',
  `morning_status` tinyint(4) DEFAULT NULL COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `afternoon_status` tinyint(4) DEFAULT NULL COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `punch_times_per_day` tinyint(4) NOT NULL DEFAULT '2' COMMENT '2 or  4 times',
  `noon_leave_time` time DEFAULT NULL,
  `afternoon_arrive_time` time DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_punch_exception_approvals`
--

DROP TABLE IF EXISTS `eh_punch_exception_approvals`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_punch_exception_approvals` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user''s id',
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT 'compay id',
  `punch_date` date DEFAULT NULL COMMENT 'user punch date',
  `approval_status` tinyint(4) DEFAULT '1' COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `morning_approval_status` tinyint(4) DEFAULT '1' COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `afternoon_approval_status` tinyint(4) DEFAULT '1' COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `view_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'is view(0) not view(1)',
  `punch_times_per_day` tinyint(4) NOT NULL DEFAULT '2' COMMENT '2 or  4 times',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_punch_exception_requests`
--

DROP TABLE IF EXISTS `eh_punch_exception_requests`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_punch_exception_requests` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user''s id',
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT 'compay id',
  `punch_date` date DEFAULT NULL COMMENT 'user punch date',
  `request_type` tinyint(4) DEFAULT NULL COMMENT '0:request ;  1:approval',
  `description` varchar(256) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1' COMMENT '0: inactive, 1: waitingForApproval, 2:active',
  `approval_status` tinyint(4) DEFAULT NULL COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `morning_approval_status` tinyint(4) DEFAULT '1' COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `afternoon_approval_status` tinyint(4) DEFAULT '1' COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `process_details` text,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `view_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'is view(0) not view(1)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_punch_geopoints`
--

DROP TABLE IF EXISTS `eh_punch_geopoints`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_punch_geopoints` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_punch_logs`
--

DROP TABLE IF EXISTS `eh_punch_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_punch_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `user_id` bigint(20) DEFAULT NULL COMMENT 'user''s id',
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT 'compay id',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `punch_date` date DEFAULT NULL COMMENT 'user punch date',
  `punch_time` datetime DEFAULT NULL COMMENT 'user check time',
  `punch_status` tinyint(4) DEFAULT NULL COMMENT '1:Normal ;  0:Not in punch area',
  `identification` varchar(255) DEFAULT NULL COMMENT 'unique identification for a phone',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_punch_rules`
--

DROP TABLE IF EXISTS `eh_punch_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_punch_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `enterprise_id` bigint(20) NOT NULL COMMENT 'rule company id',
  `start_early_time` time DEFAULT NULL COMMENT 'how early can i arrive',
  `start_late_time` time DEFAULT NULL COMMENT 'how late can i arrive ',
  `work_time` time DEFAULT NULL COMMENT 'how long do i must be work',
  `noon_leave_time` time DEFAULT NULL,
  `afternoon_arrive_time` time DEFAULT NULL,
  `time_tag1` time DEFAULT NULL,
  `time_tag2` time DEFAULT NULL,
  `time_tag3` time DEFAULT NULL,
  `punch_times_per_day` tinyint(4) NOT NULL DEFAULT '2' COMMENT '2 or  4 times',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_punch_workday`
--

DROP TABLE IF EXISTS `eh_punch_workday`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_punch_workday` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `date_status` tinyint(4) DEFAULT NULL COMMENT '0:weekend work date ;  1:holiday',
  `date_tag` date DEFAULT NULL COMMENT 'date',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT 'compay id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_push_message_results`
--

DROP TABLE IF EXISTS `eh_push_message_results`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_push_message_results` (
  `id` bigint(20) NOT NULL COMMENT 'id of the push message result, not auto increment',
  `message_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `identifier_token` varchar(128) DEFAULT NULL COMMENT 'The mobile phone of user',
  `send_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_push_messages`
--

DROP TABLE IF EXISTS `eh_push_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_push_messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'NORMAL_MESSAGE, UPGRADE_MESSAGE, NOTIFY_MESSAGE',
  `title` varchar(128) DEFAULT NULL COMMENT 'title of message',
  `content` varchar(4096) DEFAULT NULL COMMENT 'content for message',
  `target_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'CITY, COMMUNITY, FAMILY, USER',
  `target_id` bigint(20) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT 'WAITING, RUNNING, FINISHED',
  `create_time` datetime DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `finish_time` datetime DEFAULT NULL,
  `device_type` varchar(64) DEFAULT NULL,
  `device_tag` varchar(64) DEFAULT NULL,
  `app_version` varchar(64) DEFAULT NULL,
  `push_count` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_qrcodes`
--

DROP TABLE IF EXISTS `eh_qrcodes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_qrcodes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `description` varchar(1024) DEFAULT NULL,
  `view_count` bigint(20) NOT NULL DEFAULT '0',
  `logo_uri` varchar(1024) DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL COMMENT 'it is permanent if there is no expired time, else it is temporary',
  `action_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'according to document',
  `action_data` text COMMENT 'the parameters depend on item_type, json format',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `creator_uid` bigint(20) NOT NULL COMMENT 'createor user id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_quality_inspection_categories`
--

DROP TABLE IF EXISTS `eh_quality_inspection_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_quality_inspection_categories` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL,
  `path` varchar(128) DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_quality_inspection_evaluation_factors`
--

DROP TABLE IF EXISTS `eh_quality_inspection_evaluation_factors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_quality_inspection_evaluation_factors` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_categories',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `weight` double NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_quality_inspection_evaluations`
--

DROP TABLE IF EXISTS `eh_quality_inspection_evaluations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_quality_inspection_evaluations` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `date_str` varchar(32) NOT NULL DEFAULT '',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `group_name` varchar(64) DEFAULT NULL,
  `score` double NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_quality_inspection_standard_group_map`
--

DROP TABLE IF EXISTS `eh_quality_inspection_standard_group_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_quality_inspection_standard_group_map` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `group_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: executive group, 2: review group',
  `standard_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `inspector_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_quality_inspection_standards`
--

DROP TABLE IF EXISTS `eh_quality_inspection_standards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_quality_inspection_standards` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `standard_number` varchar(128) DEFAULT NULL,
  `name` varchar(1024) DEFAULT NULL,
  `category_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_categories',
  `repeat_setting_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_repeat_settings',
  `description` text COMMENT 'content data',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL COMMENT 'operator uid of last operation',
  `update_time` datetime DEFAULT NULL,
  `deleter_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_quality_inspection_task_attachments`
--

DROP TABLE IF EXISTS `eh_quality_inspection_task_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_quality_inspection_task_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `record_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_tasks',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_quality_inspection_task_records`
--

DROP TABLE IF EXISTS `eh_quality_inspection_task_records`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_quality_inspection_task_records` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `task_id` bigint(20) NOT NULL DEFAULT '0',
  `operator_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who operates the task, organization, etc',
  `operator_id` bigint(20) NOT NULL DEFAULT '0',
  `target_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who is the target of processing the task, organization, etc',
  `target_id` bigint(20) NOT NULL DEFAULT '0',
  `process_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: inspect, 2: retify, 3: review, 4: assgin, 5: forward',
  `process_end_time` datetime DEFAULT NULL,
  `process_result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
  `process_message` text,
  `process_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_quality_inspection_tasks`
--

DROP TABLE IF EXISTS `eh_quality_inspection_tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_quality_inspection_tasks` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `standard_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `task_number` varchar(128) DEFAULT NULL,
  `task_name` varchar(1024) DEFAULT NULL,
  `task_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: verify task, 2: rectify task',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '0: parent task, others childrean-task',
  `child_count` bigint(20) NOT NULL DEFAULT '0',
  `executive_group_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'refernece to the id of eh_organizations',
  `executive_start_time` datetime DEFAULT NULL,
  `executive_expire_time` datetime DEFAULT NULL,
  `executive_time` datetime DEFAULT NULL,
  `executor_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `executor_id` bigint(20) NOT NULL DEFAULT '0',
  `operator_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `operator_id` bigint(20) NOT NULL DEFAULT '0',
  `process_expire_time` datetime DEFAULT NULL,
  `process_result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:none, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
  `process_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: waiting for executing, 2: rectifing, 3: rectified and waiting approval, 4: rectify closed and waiting approval, 5: closed',
  `result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay',
  `reviewer_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
  `reviewer_id` bigint(20) NOT NULL DEFAULT '0',
  `review_result` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_recharge_info`
--

DROP TABLE IF EXISTS `eh_recharge_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_recharge_info` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `bill_id` bigint(20) DEFAULT NULL,
  `plate_number` varchar(20) DEFAULT NULL,
  `number_type` tinyint(4) DEFAULT NULL COMMENT '0-car plate',
  `owner_name` varchar(20) DEFAULT NULL COMMENT 'plate number owner name',
  `recharge_userid` bigint(20) DEFAULT NULL,
  `recharge_username` varchar(20) DEFAULT NULL,
  `recharge_phone` varchar(20) DEFAULT NULL,
  `recharge_time` datetime DEFAULT NULL,
  `recharge_month` tinyint(4) DEFAULT NULL,
  `recharge_amount` double DEFAULT NULL,
  `old_validityperiod` datetime DEFAULT NULL,
  `new_validityperiod` datetime DEFAULT NULL,
  `payment_status` tinyint(4) DEFAULT NULL COMMENT '3rd plat :0-fail 1-unpay 2-success',
  `recharge_status` tinyint(4) DEFAULT NULL COMMENT '0-fail 1-waiting paying 2-refreshing data 3-success',
  `community_id` bigint(20) DEFAULT NULL,
  `card_type` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_recommendation_configs`
--

DROP TABLE IF EXISTS `eh_recommendation_configs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_recommendation_configs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appId` bigint(20) DEFAULT NULL,
  `suggest_type` int(11) NOT NULL DEFAULT '0',
  `source_type` int(11) NOT NULL DEFAULT '0',
  `source_id` bigint(11) NOT NULL DEFAULT '0',
  `target_type` int(11) NOT NULL DEFAULT '0',
  `target_id` bigint(20) NOT NULL DEFAULT '0',
  `period_type` int(11) NOT NULL DEFAULT '0',
  `period_value` int(11) NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `running_time` datetime DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  `embedded_json` text,
  `description` varchar(1024) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_recommendations`
--

DROP TABLE IF EXISTS `eh_recommendations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_recommendations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appId` bigint(20) DEFAULT NULL,
  `suggest_type` int(11) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `source_type` int(11) NOT NULL DEFAULT '0',
  `source_id` bigint(20) NOT NULL DEFAULT '0',
  `embedded_json` text,
  `max_count` int(11) NOT NULL DEFAULT '0',
  `score` double NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_recommendations_user_idx` (`user_id`),
  CONSTRAINT `fk_eh_recommendations_user_idx` FOREIGN KEY (`user_id`) REFERENCES `eh_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=998192 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_regions`
--

DROP TABLE IF EXISTS `eh_regions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_regions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `parent_id` bigint(20) DEFAULT NULL COMMENT 'id of the parent region',
  `name` varchar(64) DEFAULT NULL,
  `pinyin_name` varchar(64) DEFAULT NULL COMMENT 'the full pinyin of the name',
  `pinyin_prefix` varchar(64) DEFAULT NULL COMMENT 'the prefix letter of every pinyin word',
  `path` varchar(128) DEFAULT NULL COMMENT 'path from the root',
  `level` int(11) NOT NULL DEFAULT '0',
  `scope_code` tinyint(4) DEFAULT NULL COMMENT '0 : country, 1: state/province, 2: city, 3: area, 4: neighborhood (community)',
  `iso_code` varchar(32) DEFAULT NULL COMMENT 'international standard code for the region if exists',
  `tel_code` varchar(32) DEFAULT NULL COMMENT 'primary telephone area code',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: inactive, 2: active, 3: locked, 4: mark as deleted',
  `hot_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: not hot, 1: hot',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_region_name` (`namespace_id`,`parent_id`,`name`),
  KEY `i_eh_region_name_level` (`name`,`level`),
  KEY `i_eh_region_path` (`path`),
  KEY `i_eh_region_path_level` (`path`,`level`),
  KEY `i_eh_region_parent` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14966 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_bill_attachments`
--

DROP TABLE IF EXISTS `eh_rental_bill_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_bill_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ',
  `site_type` varchar(128) DEFAULT NULL,
  `rental_bill_id` bigint(20) DEFAULT NULL,
  `attachment_type` tinyint(4) DEFAULT NULL COMMENT '0:String 1:email 2:attachment file',
  `content` varchar(500) DEFAULT NULL,
  `file_path` varchar(500) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_bill_paybill_map`
--

DROP TABLE IF EXISTS `eh_rental_bill_paybill_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_bill_paybill_map` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ',
  `site_type` varchar(128) DEFAULT NULL,
  `rental_bill_id` bigint(20) DEFAULT NULL,
  `online_pay_bill_id` bigint(20) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_bills`
--

DROP TABLE IF EXISTS `eh_rental_bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_bills` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ',
  `site_type` varchar(128) DEFAULT NULL,
  `rental_site_id` bigint(20) NOT NULL COMMENT 'id',
  `rental_uid` bigint(20) DEFAULT NULL COMMENT 'rental user id',
  `rental_date` date DEFAULT NULL COMMENT 'rental target date',
  `start_time` datetime DEFAULT NULL COMMENT 'begin datetime unuse ',
  `end_time` datetime DEFAULT NULL COMMENT 'end datetime unuse',
  `rental_count` double DEFAULT NULL COMMENT 'amount of rental sites ',
  `pay_total_money` decimal(10,2) DEFAULT NULL,
  `site_total_money` decimal(10,2) DEFAULT NULL,
  `reserve_money` decimal(10,2) DEFAULT NULL,
  `reserve_time` datetime DEFAULT NULL COMMENT 'reserve time ',
  `pay_start_time` datetime DEFAULT NULL,
  `pay_end_time` datetime DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  `cancel_time` datetime DEFAULT NULL,
  `paid_money` decimal(10,2) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0:wait for reserve 1:paid reserve  2:paid all money reserve success  3:wait for final payment 4:unlock reserve fail',
  `visible_flag` tinyint(4) DEFAULT '0' COMMENT '0:visible 1:unvisible',
  `invoice_flag` tinyint(4) DEFAULT '1' COMMENT '0:want invocie 1 no need',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_items_bills`
--

DROP TABLE IF EXISTS `eh_rental_items_bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_items_bills` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `community_id` bigint(20) NOT NULL COMMENT ' enterprise  community id',
  `site_type` varchar(128) DEFAULT NULL,
  `rental_bill_id` bigint(20) DEFAULT NULL,
  `rental_site_item_id` bigint(20) DEFAULT NULL,
  `rental_count` int(11) DEFAULT NULL,
  `total_money` decimal(10,2) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_rules`
--

DROP TABLE IF EXISTS `eh_rental_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ',
  `site_type` varchar(20) DEFAULT NULL COMMENT 'rule for what function ',
  `rental_start_time` bigint(20) DEFAULT NULL,
  `rental_end_time` bigint(20) DEFAULT NULL,
  `pay_start_time` bigint(20) DEFAULT NULL,
  `pay_end_time` bigint(20) DEFAULT NULL,
  `payment_ratio` int(11) DEFAULT NULL COMMENT 'payment ratio',
  `refund_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0 allow refund , 1 can not refund ',
  `contact_num` varchar(20) DEFAULT NULL COMMENT 'phone number',
  `time_tag1` time DEFAULT NULL,
  `time_tag2` time DEFAULT NULL,
  `time_tag3` time DEFAULT NULL,
  `date_tag1` date DEFAULT NULL,
  `date_tag2` date DEFAULT NULL,
  `date_tag3` date DEFAULT NULL,
  `datetime_tag1` datetime DEFAULT NULL,
  `datetime_tag2` datetime DEFAULT NULL,
  `datetime_tag3` datetime DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  `rental_type` tinyint(4) DEFAULT NULL COMMENT '0: as hour:min  1-as half day 2-as day',
  `cancel_time` bigint(20) DEFAULT NULL,
  `overtime_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_site_items`
--

DROP TABLE IF EXISTS `eh_rental_site_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_site_items` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `rental_site_id` bigint(20) NOT NULL COMMENT '  rental_site id',
  `name` varchar(128) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `counts` int(11) DEFAULT NULL COMMENT 'item count',
  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_site_rules`
--

DROP TABLE IF EXISTS `eh_rental_site_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_site_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ',
  `site_type` varchar(128) DEFAULT NULL,
  `rental_site_id` bigint(20) NOT NULL COMMENT 'rental_site id',
  `rental_type` tinyint(4) DEFAULT NULL COMMENT '0: as hour:min  1-as half day 2-as day',
  `amorpm` tinyint(4) DEFAULT NULL COMMENT '0:am  1:pm',
  `rental_step` int(11) DEFAULT '1' COMMENT 'how many time_step must be rental every time ',
  `begin_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `counts` double DEFAULT NULL COMMENT 'site count',
  `unit` double DEFAULT NULL COMMENT '1 or 0.5 basketball yard can rental half',
  `price` decimal(10,2) DEFAULT NULL,
  `site_rental_date` date DEFAULT NULL COMMENT 'which day',
  `status` tinyint(4) DEFAULT NULL COMMENT 'unuse 0:open  1:closed',
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  `time_step` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_sites`
--

DROP TABLE IF EXISTS `eh_rental_sites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_sites` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `parent_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ',
  `site_type` varchar(128) DEFAULT NULL,
  `site_name` varchar(127) DEFAULT NULL,
  `site_type2` tinyint(4) DEFAULT NULL,
  `building_name` varchar(128) DEFAULT NULL,
  `building_id` bigint(20) DEFAULT NULL,
  `address` varchar(128) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `spec` varchar(255) DEFAULT NULL COMMENT 'spec ,user setting ,maybe meetingroom seats ,KTV ROOM: big small VIP and so on',
  `own_company_name` varchar(60) DEFAULT NULL,
  `contact_name` varchar(40) DEFAULT NULL,
  `contact_phonenum` varchar(20) DEFAULT NULL,
  `contact_phonenum2` varchar(20) DEFAULT NULL,
  `contact_phonenum3` varchar(20) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  `introduction` text,
  `notice` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rental_sites_bills`
--

DROP TABLE IF EXISTS `eh_rental_sites_bills`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rental_sites_bills` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `owner_id` bigint(20) NOT NULL COMMENT '    community id or organization id ',
  `site_type` varchar(128) DEFAULT NULL,
  `rental_bill_id` bigint(20) DEFAULT NULL,
  `rental_site_rule_id` bigint(20) DEFAULT NULL,
  `rental_count` double DEFAULT NULL,
  `total_money` decimal(10,2) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint(20) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'owner type : community ; organization',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_repeat_settings`
--

DROP TABLE IF EXISTS `eh_repeat_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_repeat_settings` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the setting, QA, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `forever_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: false, 1: true',
  `repeat_count` int(11) NOT NULL DEFAULT '0' COMMENT 'how many times of repeating before the end, 0:inactive, others as times',
  `start_date` date DEFAULT NULL COMMENT 'the whole span of the event, including repeat time',
  `end_date` date DEFAULT NULL COMMENT 'ineffective if forever_flag is set true, forever_flag/repeat_count/end_date are exclusive, only one is used',
  `time_ranges` varchar(2048) DEFAULT NULL COMMENT 'multiple time ranges in a day, json format, {"ranges":[{"startTime":"08:00:00","endTime":"09:30:00","duration":"3m"},{"startTime":"18:30:00","endTime":"19:30:00","duration":"2d"}]}',
  `repeat_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year',
  `repeat_interval` int(11) NOT NULL DEFAULT '1' COMMENT 'every N day/week/month/year',
  `every_workday_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: false, 1: true, only effective if repeat_type is by-day',
  `expression` varchar(2048) DEFAULT NULL COMMENT 'the expression for the repeat details, json format, should be parsed with repeat_type and repeat_interval',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_rtxt_resources`
--

DROP TABLE IF EXISTS `eh_rtxt_resources`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_rtxt_resources` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `checksum` varchar(128) DEFAULT NULL,
  `tile` text,
  `author` text,
  `description` text,
  `cover_res_id` bigint(20) DEFAULT NULL,
  `store_type` varchar(32) DEFAULT NULL COMMENT 'content store type',
  `store_uri` varchar(32) DEFAULT NULL COMMENT 'identify the store instance',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'object content type',
  `content_length` bigint(20) DEFAULT NULL,
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'object link info on storage',
  `reference_count` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `access_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_rtxt_res_checksum` (`checksum`),
  KEY `i_eh_rtxt_res_create_time` (`create_time`),
  KEY `i_eh_rtxt_res_access_time` (`access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_scene_types`
--

DROP TABLE IF EXISTS `eh_scene_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_scene_types` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL COMMENT 'the identifier of the scene type',
  `display_name` varchar(128) NOT NULL DEFAULT '' COMMENT 'the name used to display',
  `create_time` datetime DEFAULT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the parent id of scene, it is used to inherit something from the parent scene',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ns_scene` (`namespace_id`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1004 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_schedule_task_logs`
--

DROP TABLE IF EXISTS `eh_schedule_task_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_schedule_task_logs` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `task_id` bigint(20) NOT NULL DEFAULT '0',
  `resource_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
  `resource_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `result_data` text COMMENT 'the data need to keep after task finished',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_schedule_tasks`
--

DROP TABLE IF EXISTS `eh_schedule_tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_schedule_tasks` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `resource_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
  `resource_id` bigint(20) NOT NULL DEFAULT '0',
  `process_count` int(11) NOT NULL DEFAULT '0' COMMENT 'the count of process',
  `progress` int(11) NOT NULL DEFAULT '0' COMMENT '0~100 percentage',
  `progress_data` text COMMENT 'the data at the point of progress, it can recover the task if it interupted in the middle, json format',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: new, 2: on progress',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_scoped_configurations`
--

DROP TABLE IF EXISTS `eh_scoped_configurations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_scoped_configurations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `app_id` bigint(20) DEFAULT NULL,
  `scope_type` varchar(32) DEFAULT NULL,
  `scope_id` bigint(20) DEFAULT NULL,
  `item_name` varchar(32) DEFAULT NULL,
  `item_kind` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0, opaque json value, 1: entity',
  `item_value` text,
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `apply_policy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: default, 1: override, 2: revert',
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_scoped_cfg_combo` (`namespace_id`,`app_id`,`scope_type`,`scope_id`,`item_name`),
  KEY `i_eh_scoped_cfg_itag1` (`integral_tag1`),
  KEY `i_eh_scoped_cfg_itag2` (`integral_tag2`),
  KEY `i_eh_scoped_cfg_stag1` (`string_tag1`),
  KEY `i_eh_scoped_cfg_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_search_keywords`
--

DROP TABLE IF EXISTS `eh_search_keywords`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_search_keywords` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `scope` varchar(32) DEFAULT NULL,
  `scope_id` bigint(20) DEFAULT NULL,
  `keyword` varchar(128) DEFAULT NULL,
  `weight` int(11) DEFAULT NULL,
  `frequency` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_kword_scoped_kword` (`scope`,`scope_id`,`keyword`),
  KEY `i_kword_weight_frequency` (`weight`,`frequency`),
  KEY `i_kword_update_time` (`update_time`),
  KEY `i_kword_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_sequences`
--

DROP TABLE IF EXISTS `eh_sequences`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_sequences` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `domain` varchar(32) NOT NULL,
  `start_seq` bigint(20) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_server_shard_map`
--

DROP TABLE IF EXISTS `eh_server_shard_map`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_server_shard_map` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `server_id` int(11) NOT NULL,
  `shard_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ssm_server_shard` (`server_id`,`shard_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_servers`
--

DROP TABLE IF EXISTS `eh_servers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_servers` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `master_id` int(11) DEFAULT NULL COMMENT 'master server id',
  `address_uri` varchar(256) DEFAULT NULL,
  `address_port` int(11) DEFAULT NULL,
  `server_type` int(11) NOT NULL DEFAULT '0' COMMENT '0: DB, 1: redis storage server, 2: redis cache server',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '0 : disabled, 1: enabled',
  `config_tag` varchar(32) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_servers_config_tag` (`config_tag`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_shards`
--

DROP TABLE IF EXISTS `eh_shards`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_shards` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `sharding_domain` varchar(64) NOT NULL,
  `anchor` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'time that shard has been created',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_shards_domain_anchor` (`sharding_domain`,`anchor`),
  KEY `i_eh_shards_anchor` (`anchor`),
  KEY `i_eh_shards_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_source_account`
--

DROP TABLE IF EXISTS `eh_source_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_source_account` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `source_account` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `conf_tpye` tinyint(4) DEFAULT NULL COMMENT '0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话',
  `valid_date` datetime DEFAULT NULL,
  `valid_flag` tinyint(4) DEFAULT NULL COMMENT '0-invalid 1-valid',
  `status` tinyint(4) DEFAULT NULL COMMENT '0-available 1-occupied',
  `occupy_account_id` bigint(20) DEFAULT NULL,
  `conf_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_state_triggers`
--

DROP TABLE IF EXISTS `eh_state_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_state_triggers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `object_type` varchar(32) DEFAULT NULL,
  `object_id` bigint(20) DEFAULT NULL,
  `trigger_state` int(11) DEFAULT NULL,
  `flow_type` int(11) DEFAULT NULL,
  `flow_data` text,
  `order` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, it is used to control program logic, makes more sense to just remove it',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_stats_by_city`
--

DROP TABLE IF EXISTS `eh_stats_by_city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_stats_by_city` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city_id` bigint(20) DEFAULT NULL COMMENT 'id in eh_regions table of the city',
  `stats_date` varchar(32) DEFAULT NULL,
  `stats_type` int(11) DEFAULT NULL,
  `reg_user_count` bigint(20) DEFAULT NULL,
  `addr_user_count` bigint(20) DEFAULT NULL,
  `pending_user_count` bigint(20) DEFAULT NULL,
  `community_count` bigint(20) DEFAULT NULL,
  `apt_count` bigint(20) DEFAULT NULL,
  `pending_apt_count` bigint(20) DEFAULT NULL,
  `post_count` bigint(20) DEFAULT NULL,
  `post_comment_count` bigint(20) DEFAULT NULL,
  `post_like_count` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy, historic data may be valuable',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_stats_city_report` (`city_id`,`stats_date`,`stats_type`),
  KEY `u_stats_delete_time` (`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_suggestions`
--

DROP TABLE IF EXISTS `eh_suggestions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_suggestions` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SUGGEST_TYPE` int(11) NOT NULL DEFAULT '0',
  `USER_ID` bigint(20) NOT NULL DEFAULT '0',
  `TARGET_TYPE` int(11) NOT NULL DEFAULT '0',
  `TARGET_ID` bigint(20) NOT NULL DEFAULT '0',
  `REASON_JSON` varchar(1024) DEFAULT '',
  `MAX_COUNT` int(11) NOT NULL DEFAULT '0',
  `SCORE` double NOT NULL DEFAULT '0',
  `STATUS` int(11) NOT NULL DEFAULT '0',
  `CREATE_TIME` datetime DEFAULT NULL,
  `EXPIRED_TIME` varchar(64) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `fk_eh_suggestions_user_idx` (`USER_ID`),
  CONSTRAINT `fk_eh_suggestions_user_idx` FOREIGN KEY (`USER_ID`) REFERENCES `eh_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_templates`
--

DROP TABLE IF EXISTS `eh_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_templates` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `path` varchar(256) DEFAULT NULL,
  `type` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_template_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_thirdpart_users`
--

DROP TABLE IF EXISTS `eh_thirdpart_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_thirdpart_users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `vendor_tag` varchar(64) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL COMMENT 'name of user',
  `phone` varchar(64) DEFAULT NULL COMMENT 'phone of user',
  `city_name` varchar(64) DEFAULT NULL COMMENT 'city',
  `area_name` varchar(64) DEFAULT NULL COMMENT 'area',
  `community_names` text COMMENT 'community name, split with comma if there are multiple communties',
  `building_name` varchar(128) DEFAULT NULL,
  `unit_name` varchar(64) DEFAULT NULL,
  `apartment_name` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_activities`
--

DROP TABLE IF EXISTS `eh_user_activities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_activities` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL DEFAULT '0',
  `activity_type` tinyint(4) NOT NULL DEFAULT '0',
  `app_version_code` bigint(20) NOT NULL DEFAULT '0',
  `app_version_name` varchar(128) DEFAULT '',
  `channel_id` bigint(20) NOT NULL DEFAULT '0',
  `imei_number` varchar(128) DEFAULT '',
  `device_type` varchar(512) DEFAULT '',
  `os_info` varchar(512) DEFAULT '',
  `os_type` tinyint(4) NOT NULL DEFAULT '0',
  `mkt_data_version` bigint(20) NOT NULL DEFAULT '0',
  `report_config_version` bigint(20) NOT NULL DEFAULT '0',
  `internal_ip` varchar(128) DEFAULT '',
  `external_ip` varchar(128) DEFAULT '',
  `user_agent` varchar(1024) DEFAULT '',
  `collect_time_ms` bigint(20) NOT NULL DEFAULT '0',
  `report_time_ms` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=98880 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_behaviors`
--

DROP TABLE IF EXISTS `eh_user_behaviors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_behaviors` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL DEFAULT '0',
  `content_type` tinyint(4) NOT NULL DEFAULT '0',
  `content` text,
  `collect_time_ms` bigint(20) NOT NULL DEFAULT '0',
  `report_time_ms` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_blacklist`
--

DROP TABLE IF EXISTS `eh_user_blacklist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_blacklist` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_blk_owner_target` (`owner_uid`,`target_type`,`target_id`),
  KEY `i_eh_usr_blk_owner` (`owner_uid`),
  KEY `i_eh_usr_blk_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_communities`
--

DROP TABLE IF EXISTS `eh_user_communities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `community_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'redendant info for quickly distinguishing associated community',
  `community_id` bigint(20) NOT NULL DEFAULT '0',
  `join_policy` tinyint(4) NOT NULL DEFAULT '1' COMMENT '1: register, 2: request to join',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_community` (`owner_uid`,`community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_contacts`
--

DROP TABLE IF EXISTS `eh_user_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_contacts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL DEFAULT '0',
  `contact_id` bigint(20) NOT NULL DEFAULT '0',
  `contact_phone` varchar(32) DEFAULT '',
  `contact_name` varchar(128) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73668 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_favorites`
--

DROP TABLE IF EXISTS `eh_user_favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_favorites` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_favorite_target` (`owner_uid`,`target_type`,`target_id`),
  KEY `i_eh_usr_favorite_owner` (`owner_uid`),
  KEY `i_eh_usr_favorite_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_followed_families`
--

DROP TABLE IF EXISTS `eh_user_followed_families`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_followed_families` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL,
  `followed_family` bigint(20) NOT NULL,
  `alias_name` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_eh_usr_ffmy_followed` (`owner_uid`,`followed_family`),
  KEY `i_eh_usr_ffmy_owner` (`owner_uid`),
  KEY `i_eh_usr_ffmy_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_group_histories`
--

DROP TABLE IF EXISTS `eh_user_group_histories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_group_histories` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `group_discriminator` varchar(32) DEFAULT NULL COMMENT 'redendant info for quickly distinguishing associated group',
  `group_id` bigint(20) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_groups`
--

DROP TABLE IF EXISTS `eh_user_groups`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_groups` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `group_discriminator` varchar(32) DEFAULT NULL COMMENT 'redendant info for quickly distinguishing associated group',
  `group_id` bigint(20) DEFAULT NULL,
  `region_scope` tinyint(4) DEFAULT NULL COMMENT 'redundant group info to help region-based group user search',
  `region_scope_id` bigint(20) DEFAULT NULL COMMENT 'redundant group info to help region-based group user search',
  `member_role` bigint(20) NOT NULL DEFAULT '7' COMMENT 'default to ResourceUser role',
  `member_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance, 3: active',
  `create_time` datetime NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_grp_owner_group` (`owner_uid`,`group_id`),
  KEY `i_eh_usr_grp_owner` (`owner_uid`),
  KEY `i_eh_usr_grp_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_identifiers`
--

DROP TABLE IF EXISTS `eh_user_identifiers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_identifiers` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `identifier_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `identifier_token` varchar(128) DEFAULT NULL,
  `verification_code` varchar(16) DEFAULT NULL,
  `claim_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: free standing, 1: claiming, 2: claim verifying, 3: claimed',
  `create_time` datetime NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
  `notify_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_user_idf_owner_type_token` (`owner_uid`,`identifier_type`,`identifier_token`),
  KEY `i_eh_user_idf_owner` (`owner_uid`),
  KEY `i_eh_user_idf_type_token` (`identifier_type`,`identifier_token`),
  KEY `i_eh_user_idf_create_time` (`create_time`),
  KEY `i_eh_user_idf_notify_time` (`notify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_installed_apps`
--

DROP TABLE IF EXISTS `eh_user_installed_apps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_installed_apps` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL DEFAULT '0',
  `app_name` varchar(1024) DEFAULT '',
  `app_version` varchar(128) DEFAULT '',
  `app_size` varchar(128) DEFAULT '',
  `app_installed_time` varchar(128) DEFAULT '',
  `collect_time_ms` bigint(20) NOT NULL DEFAULT '0',
  `report_time_ms` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_invitation_roster`
--

DROP TABLE IF EXISTS `eh_user_invitation_roster`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_invitation_roster` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `invite_id` bigint(20) DEFAULT NULL COMMENT 'owner invitation record id',
  `name` varchar(64) DEFAULT NULL,
  `contact` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_invite_roster_invite_id` (`invite_id`),
  CONSTRAINT `eh_user_invitation_roster_ibfk_1` FOREIGN KEY (`invite_id`) REFERENCES `eh_user_invitations` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_invitations`
--

DROP TABLE IF EXISTS `eh_user_invitations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_invitations` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `invite_code` varchar(128) DEFAULT NULL,
  `invite_type` tinyint(4) DEFAULT NULL COMMENT '1: SMS, 2: wechat, 3, wechat friend circle, 4: weibo, 5: phone contact',
  `expiration` datetime DEFAULT NULL COMMENT 'expiration time of the invitation',
  `target_entity_type` varchar(32) DEFAULT NULL,
  `target_entity_id` bigint(20) DEFAULT NULL,
  `max_invite_count` int(11) NOT NULL DEFAULT '1',
  `current_invite_count` int(11) NOT NULL DEFAULT '0',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `create_time` datetime NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_invite_code` (`invite_code`),
  KEY `u_eh_invite_expiration` (`expiration`),
  KEY `u_eh_invite_code_status` (`invite_code`,`status`),
  KEY `u_eh_invite_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_likes`
--

DROP TABLE IF EXISTS `eh_user_likes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_likes` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `like_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0:none, 1: dislike, 2: like',
  `create_time` datetime DEFAULT NULL COMMENT 'remove-deletion policy, user directly managed data',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_like_target` (`owner_uid`,`target_type`,`target_id`),
  KEY `i_eh_usr_like_owner` (`owner_uid`),
  KEY `i_eh_usr_like_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_locations`
--

DROP TABLE IF EXISTS `eh_user_locations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_locations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) NOT NULL DEFAULT '0',
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(128) DEFAULT '',
  `create_time` datetime DEFAULT NULL,
  `collect_time_ms` bigint(20) NOT NULL DEFAULT '0',
  `report_time_ms` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33017 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_posts`
--

DROP TABLE IF EXISTS `eh_user_posts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_posts` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `post_id` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_post_id` (`owner_uid`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_profiles`
--

DROP TABLE IF EXISTS `eh_user_profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_profiles` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `app_id` bigint(20) DEFAULT NULL,
  `owner_id` bigint(20) NOT NULL COMMENT 'owner user id',
  `item_name` varchar(128) DEFAULT NULL,
  `item_kind` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0, opaque json object, 1: entity',
  `item_value` text,
  `target_type` varchar(32) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_uprof_item` (`app_id`,`owner_id`,`item_name`),
  KEY `i_eh_uprof_owner` (`owner_id`),
  KEY `i_eh_uprof_itag1` (`integral_tag1`),
  KEY `i_eh_uprof_itag2` (`integral_tag2`),
  KEY `i_eh_uprof_stag1` (`string_tag1`),
  KEY `i_eh_uprof_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_scores`
--

DROP TABLE IF EXISTS `eh_user_scores`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_scores` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `owner_uid` bigint(20) NOT NULL DEFAULT '0',
  `score_type` varchar(32) NOT NULL,
  `score` int(11) NOT NULL DEFAULT '0',
  `operator_uid` bigint(20) NOT NULL DEFAULT '0',
  `operate_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=79306 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_user_service_addresses`
--

DROP TABLE IF EXISTS `eh_user_service_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_user_service_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_uid` bigint(20) NOT NULL COMMENT 'owner user id',
  `address_id` bigint(20) NOT NULL DEFAULT '0',
  `contact_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `contact_token` varchar(128) NOT NULL DEFAULT '' COMMENT 'phone number or email address',
  `contact_name` varchar(64) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `deleter_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'deleter id',
  `delete_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_service_address_id` (`owner_uid`,`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_users`
--

DROP TABLE IF EXISTS `eh_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_users` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `uuid` varchar(128) NOT NULL DEFAULT '',
  `account_name` varchar(64) NOT NULL,
  `nick_name` varchar(256) DEFAULT NULL,
  `avatar` varchar(128) DEFAULT NULL,
  `status_line` varchar(128) DEFAULT NULL COMMENT 'status line to express who you are',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0 - inactive, 1 - active',
  `points` int(11) NOT NULL DEFAULT '0' COMMENT 'points',
  `level` tinyint(4) NOT NULL DEFAULT '1',
  `gender` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: undisclosured, 1: male, 2: female',
  `birthday` date DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL COMMENT 'current address id',
  `address` varchar(128) DEFAULT NULL COMMENT 'redundant current address description',
  `community_id` bigint(20) DEFAULT NULL COMMENT 'if current family has been setup, it is the community id from address',
  `home_town` bigint(20) DEFAULT NULL COMMENT 'region id',
  `home_town_path` varchar(128) DEFAULT NULL COMMENT 'redundant region path for recursive matching',
  `occupation` varchar(128) DEFAULT NULL,
  `company` varchar(128) DEFAULT NULL,
  `school` varchar(128) DEFAULT NULL,
  `locale` varchar(16) DEFAULT NULL COMMENT 'zh_CN, en_US etc',
  `invite_type` tinyint(4) DEFAULT NULL COMMENT '1: SMS, 2: wechat, 3, wechat friend circle, 4: weibo, 5: phone contact',
  `invitor_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime NOT NULL,
  `delete_time` datetime DEFAULT NULL COMMENT 'mark-deletion policy. may be valuable for user to restore account',
  `last_login_time` datetime DEFAULT NULL,
  `last_login_ip` varchar(64) DEFAULT NULL,
  `reg_ip` varchar(64) DEFAULT '' COMMENT 'the channel at the time of register',
  `reg_city_id` bigint(20) DEFAULT '0' COMMENT 'the city at the time of register',
  `reg_channel_id` bigint(20) DEFAULT '0' COMMENT 'the channel at the time of register',
  `original_avatar` varchar(128) DEFAULT NULL COMMENT 'the path of avatar in 2.8 version, keep it for migration',
  `salt` varchar(64) DEFAULT NULL,
  `password_hash` varchar(128) DEFAULT '' COMMENT 'Note, password is stored as salted hash, salt is appended by hash together',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `namespace_user_token` varchar(2048) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  UNIQUE KEY `u_eh_user_account_name` (`account_name`),
  KEY `i_eh_user_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_version_realm`
--

DROP TABLE IF EXISTS `eh_version_realm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_version_realm` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `realm` varchar(128) DEFAULT NULL,
  `description` text,
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ver_realm` (`realm`,`namespace_id`),
  KEY `i_eh_ver_realm_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_version_upgrade_rules`
--

DROP TABLE IF EXISTS `eh_version_upgrade_rules`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_version_upgrade_rules` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `realm_id` bigint(20) NOT NULL,
  `matching_lower_bound` double NOT NULL,
  `matching_upper_bound` double NOT NULL,
  `order` int(11) NOT NULL DEFAULT '0',
  `target_version` varchar(128) DEFAULT NULL,
  `force_upgrade` tinyint(4) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_eh_ver_upgrade_realm` (`realm_id`),
  KEY `i_eh_ver_upgrade_order` (`order`),
  KEY `i_eh_ver_upgrade_create_time` (`create_time`),
  CONSTRAINT `eh_version_upgrade_rules_ibfk_1` FOREIGN KEY (`realm_id`) REFERENCES `eh_version_realm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_version_urls`
--

DROP TABLE IF EXISTS `eh_version_urls`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_version_urls` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `realm_id` bigint(20) NOT NULL,
  `target_version` varchar(128) DEFAULT NULL,
  `download_url` varchar(128) DEFAULT NULL COMMENT 'example configuration: http://serviceurl/download/client-packages/${locale}/andriod-${major}-${minor}-${revision}.apk',
  `info_url` varchar(128) DEFAULT NULL COMMENT 'example configuration: http://serviceurl/download/client-package-info/${locale}/andriod-${major}-${minor}-${revision}.html',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ver_url` (`realm_id`,`target_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_versioned_content`
--

DROP TABLE IF EXISTS `eh_versioned_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_versioned_content` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `realm_id` bigint(20) NOT NULL,
  `matching_lower_bound` double NOT NULL,
  `matching_upper_bound` double NOT NULL,
  `order` int(11) NOT NULL DEFAULT '0',
  `content` text,
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_eh_ver_content_realm` (`realm_id`),
  KEY `i_eh_ver_content_order` (`order`),
  KEY `i_eh_ver_content_create_time` (`create_time`),
  CONSTRAINT `eh_versioned_content_ibfk_1` FOREIGN KEY (`realm_id`) REFERENCES `eh_version_realm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_warning_contacts`
--

DROP TABLE IF EXISTS `eh_warning_contacts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_warning_contacts` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `contactor` varchar(20) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `email` varchar(20) DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_web_menu_privileges`
--

DROP TABLE IF EXISTS `eh_web_menu_privileges`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_web_menu_privileges` (
  `id` bigint(20) NOT NULL,
  `privilege_id` bigint(20) NOT NULL,
  `menu_id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `show_flag` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: Not related menu display, 1: Associated menu display',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '0: inactive, 1: active',
  `discription` varchar(128) DEFAULT NULL,
  `sort_num` int(11) DEFAULT NULL COMMENT 'sort number',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_web_menu_scopes`
--

DROP TABLE IF EXISTS `eh_web_menu_scopes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_web_menu_scopes` (
  `id` bigint(20) NOT NULL,
  `menu_id` bigint(20) DEFAULT NULL,
  `menu_name` varchar(64) DEFAULT NULL,
  `owner_type` varchar(64) NOT NULL,
  `owner_id` bigint(20) DEFAULT NULL,
  `apply_policy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: delete , 1: override, 2: revert',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_web_menus`
--

DROP TABLE IF EXISTS `eh_web_menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_web_menus` (
  `id` bigint(20) NOT NULL,
  `name` varchar(64) DEFAULT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `icon_url` varchar(64) DEFAULT NULL,
  `data_type` varchar(64) DEFAULT NULL,
  `leaf_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Whether leaf nodes, non leaf nodes can be folded 0: false, 1: true',
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 2: active',
  `path` varchar(64) DEFAULT NULL,
  `type` varchar(64) NOT NULL DEFAULT 'zuolin' COMMENT 'zuolin, park',
  `sort_num` int(11) DEFAULT NULL COMMENT 'sort number',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_wifi_settings`
--

DROP TABLE IF EXISTS `eh_wifi_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_wifi_settings` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `ssid` varchar(128) NOT NULL COMMENT 'the name of address resource',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `delete_uid` bigint(20) DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0 : inactive, 1: active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_yellow_page_attachments`
--

DROP TABLE IF EXISTS `eh_yellow_page_attachments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_yellow_page_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `owner_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'the id reference to eh_yellow_pages',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `eh_yellow_pages`
--

DROP TABLE IF EXISTS `eh_yellow_pages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `eh_yellow_pages` (
  `id` bigint(20) NOT NULL,
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `owner_type` varchar(64) NOT NULL COMMENT 'community;group,organaization,exhibition,',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(128) NOT NULL DEFAULT '' COMMENT 'organization name',
  `nick_name` varchar(128) NOT NULL DEFAULT '',
  `type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '1 chuangkekongjian; 2 fuwulianmeng; 3 yuanquqiye',
  `address` varchar(255) NOT NULL DEFAULT '',
  `contact` varchar(64) DEFAULT NULL,
  `description` text,
  `poster_uri` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `default_order` int(11) DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `geohash` varchar(32) DEFAULT NULL,
  `integral_tag1` bigint(20) DEFAULT NULL,
  `integral_tag2` bigint(20) DEFAULT NULL,
  `integral_tag3` bigint(20) DEFAULT NULL,
  `integral_tag4` bigint(20) DEFAULT NULL,
  `integral_tag5` bigint(20) DEFAULT NULL,
  `string_tag1` varchar(128) DEFAULT NULL,
  `string_tag2` varchar(128) DEFAULT NULL,
  `string_tag3` varchar(128) DEFAULT NULL,
  `string_tag4` varchar(128) DEFAULT NULL,
  `string_tag5` varchar(128) DEFAULT NULL,
  `creator_uid` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


SET autocommit=1;
SET foreign_key_checks = 1;

-- Dump completed on 2016-05-31 17:04:08
