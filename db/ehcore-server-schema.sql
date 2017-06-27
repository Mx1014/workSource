--
-- Special notes about the schema design below (KY)
--
-- Custom fileds
--   To balance performance and flexibility, some tables carry general purpose integer fields and string fields,
--   interpretation of these fields will be determined by the applications on top of, at database level, we only
--   provide general indexing support for these fields, it is the responsibility of the application to map queries that
--   are against to these fields.
--
--   Initially, only two of integral-type and string-type fields are indexed, more indices can be added during operating
--   time, tuning changes about the indexing will be sync-ed back into schema design afterwards
--
-- namespaces and application modules
--  Reusable modules are abstracted under the concept of application. The platform provides built-in application modules
--  such as messaging application module, forum application module, etc. These built-in application modules are running
--  in the context of core server. When a application module has external counterpart at third-party servers or remote client endpoints,
--  the API it provides requires to go through the authentication system via appkey/secret key pair mechanism
--
--   Namespace is used to put related resources into distinct domains
--
-- namespace and application design rules
--  Shared resources (usually system defined) that are common to all namespaces do not need namespace_id field
--  First level resources usually have namespace_id field
--  Secondary level resources do not need namespace_id field
--  objects that can carry information generated from multiple application modules usualy have app_id field
--  all profile items have app_id field, so that it allows other application modules to attach application specific
--  profile information into it
--
-- name convention
--  index prefix: i_eh_
--  unique index prefix: u_eh_
--  foreign key constraint prefix: fk_eh_
--   table prefix: eh_
--
-- record deletion
--  There are two deletion policies in regards to deletion
--  mark-deletion: mark it as deleted, wait for lazy cleanup or archive
--  remove-deletion: completely remove it from database
--
--   for the mark-deletion policy, the convention is to have a delete_time field which not only marks up the deletion
--  but also the deletion time
--
--

SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS `eh_acl_privileges`;
CREATE TABLE `eh_acl_privileges` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `app_id` BIGINT,
  `name` VARCHAR(32) NOT NULL COMMENT 'name of the operation privilege',
  `description` VARCHAR(512) COMMENT 'privilege description',
  `tag` VARCHAR(32),

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_acl_priv_app_id_name` (`app_id`,`name`),
  KEY `u_eh_acl_priv_tag` (`tag`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_acl_role_assignments`;
CREATE TABLE `eh_acl_role_assignments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'owner resource(i.e., forum) type',
  `owner_id` BIGINT COMMENT 'owner resource(i.e., forum) id',
  `target_type` VARCHAR(32) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` BIGINT COMMENT 'target object(user/group) id',
  `role_id` BIGINT NOT NULL COMMENT 'role id that is assigned',
  `creator_uid` BIGINT NOT NULL COMMENT 'assignment creator uid',
  `create_time` DATETIME COMMENT 'record create time',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_acl_role_asgn_unique` (`owner_type`,`owner_id`,`target_type`,`target_id`,`role_id`),
  KEY `i_eh_acl_role_asgn_owner` (`owner_type`,`owner_id`),
  KEY `i_eh_acl_role_asgn_creator` (`creator_uid`),
  KEY `i_eh_acl_role_asgn_create_time` (`create_time`),
  KEY `i_eh_acl_role_asgn_namespace_id` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_acl_roles`;
CREATE TABLE `eh_acl_roles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `app_id` BIGINT,
  `name` VARCHAR(32) NOT NULL COMMENT 'name of hte operating role',
  `description` VARCHAR(512),
  `tag` VARCHAR(32),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_acl_role_name` (`namespace_id`,`app_id`,`name`,`owner_type`,`owner_id`),
  KEY `u_eh_acl_role_tag` (`tag`),
  KEY `i_eh_ach_role_owner` (`namespace_id`,`app_id`,`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_aclink_firmware`;
CREATE TABLE `eh_aclink_firmware` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `major` INTEGER NOT NULL DEFAULT 0,
  `minor` INTEGER NOT NULL DEFAULT 0,
  `revision` INTEGER NOT NULL DEFAULT 0,
  `checksum` BIGINT NOT NULL,
  `md5sum` VARCHAR(64),
  `download_url` VARCHAR(128),
  `info_url` VARCHAR(128),
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME,
  `creator_id` BIGINT,
  `owner_id` BIGINT,
  `owner_type` TINYINT,
  `firmware_type` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_aclink_logs`;
CREATE TABLE `eh_aclink_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `event_type` BIGINT DEFAULT 0,

  `door_type` TINYINT NOT NULL COMMENT '0: Zuolin aclink with wifi, 1: Zuolink aclink without wifi',
  `door_id` BIGINT NOT NULL DEFAULT 0,
  `hardware_id` VARCHAR(64) NOT NULL COMMENT 'mac address of aclink',
  `door_name` VARCHAR(128) COMMENT 'door name of aclink',

  `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
  `owner_id` BIGINT NOT NULL,
  `owner_name` VARCHAR(128) COMMENT 'addition name for owner',

  `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'user_id of user key',
  `key_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'key_id of auth',
  `auth_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'auth id of user key',
  `user_name` VARCHAR(128) COMMENT 'username of logs',
  `user_identifier` VARCHAR(128) COMMENT 'useridentifier of user',

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `string_tag6` VARCHAR(128),

  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,
  `integral_tag6` BIGINT DEFAULT 0,
    
  `remark` VARCHAR(1024) COMMENT 'extra information',

  `log_time` BIGINT DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_aclink_undo_key`;
CREATE TABLE `eh_aclink_undo_key` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `key_id` INTEGER NOT NULL COMMENT 'cancel a key, must notify all users for this key_id to update',
  `door_id` BIGINT NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: requesting, 2: confirm',
  `expire_time_ms` BIGINT NOT NULL,
  `create_time_ms` BIGINT NOT NULL,

  PRIMARY KEY (`id`),
  KEY `fk_eh_aclink_undo_key_door_id` (`door_id`),
  CONSTRAINT `eh_aclink_undo_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- Partion of eh_door_access
--
DROP TABLE IF EXISTS `eh_aclinks`;
CREATE TABLE `eh_aclinks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `door_id` BIGINT NOT NULL,
  `device_name` VARCHAR(32) NOT NULL,
  `manufacturer` VARCHAR(32) NOT NULL,
  `firware_ver` VARCHAR(32) NOT NULL,
  `create_time` DATETIME,
  `status` TINYINT NOT NULL,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `driver` VARCHAR(32) NOT NULL DEFAULT 'zuolin',
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`),
  KEY `fk_eh_aclink_door_id` (`door_id`),
  CONSTRAINT `eh_aclinks_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_acls`;
CREATE TABLE `eh_acls` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT,
  `grant_type` TINYINT NOT NULL DEFAULT 1 COMMENT '0 - decline, 1 - grant',
  `privilege_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  `order_seq` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL COMMENT 'assignment creator uid',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `role_type` VARCHAR(32) COMMENT 'NULL: EhAclRole',
  `scope` VARCHAR(128),
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `comment_tag1` VARCHAR(128),
  `comment_tag2` VARCHAR(128),
  `comment_tag3` VARCHAR(128),
  `comment_tag4` VARCHAR(128),
  `comment_tag5` VARCHAR(128),
  
  PRIMARY KEY (`id`),
  KEY `i_eh_acl_owner_privilege` (`owner_type`,`owner_id`),
  KEY `i_eh_acl_owner_order_seq` (`order_seq`),
  KEY `i_eh_acl_creator` (`creator_uid`),
  KEY `i_eh_acl_create_time` (`create_time`),
  KEY `i_eh_acl_namespace_id` (`namespace_id`),
  KEY `i_eh_acl_scope` (`scope`),
  KEY `i_eh_acl_itag1` (`integral_tag1`),
  KEY `i_eh_acl_itag2` (`integral_tag2`),
  KEY `i_eh_acl_ctag1` (`comment_tag1`),
  KEY `i_eh_acl_ctag2` (`comment_tag2`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- key table of activity sharding group
-- first level resource objects
--
DROP TABLE IF EXISTS `eh_activities`;

CREATE TABLE `eh_activities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `namespace_id` INTEGER,
  `subject` VARCHAR(512),
  `description` TEXT,
  `poster_uri` VARCHAR(1024) COMMENT 'poster uri',
  `tag` VARCHAR(32),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(64),
  `location` TEXT,
  `contact_person` VARCHAR(128),
  `contact_number` VARCHAR(64),
  `start_time_ms` BIGINT,
  `start_time` DATETIME,
  `end_time_ms` BIGINT,
  `end_time` DATETIME,
  `signup_flag` TINYINT,
  `confirm_flag` TINYINT,
  `max_attendee_count` INTEGER,
  `signup_attendee_count` INTEGER,
  `signup_family_count` INTEGER,
  `checkin_attendee_count` INTEGER,
  `checkin_family_count` INTEGER,
  `confirm_attendee_count` INTEGER,
  `confirm_family_count` INTEGER,
  `creator_uid` BIGINT,
  `creator_family_id` BIGINT,
  `post_id` BIGINT COMMENT 'associated post id',
  `group_discriminator` VARCHAR(32) COMMENT 'associated group if any',
  `group_id` BIGINT,
  `status` TINYINT COMMENT '0: inactive, 1: drafting, 2: active',
  `change_version` INTEGER NOT NULL DEFAULT 1,
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',
  `guest` VARCHAR(2048),
  `media_url` VARCHAR(1024),
  `official_flag` TINYINT DEFAULT 0 COMMENT 'whether it is an official activity, 0 not, 1 yes',
  `video_url` VARCHAR(128) COMMENT 'url of video support',
  `is_video_support` TINYINT NOT NULL DEFAULT 0 COMMENT 'is video support',
  `max_quantity` INTEGER COMMENT 'max person quantity',
  `content_type` VARCHAR(128) COMMENT 'content type, text/rich_text',
  `version` VARCHAR(128) COMMENT 'version',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity category id',
  `forum_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity post forum that it belongs',
  `creator_tag` VARCHAR(128) COMMENT 'activity post creator tag',
  `target_tag` VARCHAR(128) COMMENT 'activity post target tag',
  `visible_region_type` TINYINT COMMENT 'define the visible region type',
  `visible_region_id` BIGINT COMMENT 'visible region id',
  `achievement` TEXT,
  `achievement_type` VARCHAR(32) COMMENT 'richtext, link',
  `achievement_richtext_url` VARCHAR(512) COMMENT 'richtext page',
  `update_time` DATETIME,
  `content_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'content category id',
  `signup_end_time` DATETIME,
  `all_day_flag` TINYINT DEFAULT 0 COMMENT 'whether it is an all day activity, 0 not, 1 yes',
  `charge_flag` TINYINT DEFAULT 0 COMMENT '0: no charge, 1: charge',
  `charge_price` DECIMAL(10,2) COMMENT 'charge_price',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  KEY `i_eh_act_start_time_ms` (`start_time_ms`),
  KEY `i_eh_act_end_time_ms` (`end_time_ms`),
  KEY `i_eh_act_creator_uid` (`creator_uid`),
  KEY `i_eh_act_post_id` (`post_id`),
  KEY `i_eh_act_group` (`group_discriminator`,`group_id`),
  KEY `i_eh_act_create_time` (`create_time`),
  KEY `i_eh_act_delete_time` (`delete_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 活动附件表
DROP TABLE IF EXISTS `eh_activity_attachments`;
CREATE TABLE `eh_activity_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `activity_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g application_id',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_activity_categories`;
CREATE TABLE `eh_activity_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `entry_id` BIGINT NOT NULL COMMENT 'entry id, Differ from each other\n in the same namespace',
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `default_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no , 1: yes',
  `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',		  
  `icon_uri` VARCHAR(1024),		
  `selected_icon_uri` VARCHAR(1024),		
  `show_name` VARCHAR(64),		
  `all_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no, 1: yes',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 活动物资管理表
DROP TABLE IF EXISTS  `eh_activity_goods`;
CREATE TABLE `eh_activity_goods` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `activity_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
  `name` VARCHAR(64) COMMENT 'attachment object content type',
  `price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `quantity` INTEGER NOT NULL DEFAULT 0,
  `total_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `handlers` VARCHAR(64),
  `create_time` DATETIME NOT NULL,
  `creator_uid` BIGINT,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_activities sharding group
-- secondary resource objects (after eh_events)
--
DROP TABLE IF EXISTS `eh_activity_roster`;
CREATE TABLE `eh_activity_roster` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(36) NOT NULL,
  `activity_id` BIGINT NOT NULL,
  `uid` BIGINT,
  `family_id` BIGINT,
  `adult_count` INTEGER NOT NULL DEFAULT 0,
  `child_count` INTEGER NOT NULL DEFAULT 0,
  `checkin_flag` TINYINT NOT NULL DEFAULT 0,
  `checkin_uid` BIGINT COMMENT 'id of checkin user',
  `confirm_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未确认, 1: 确认',
  `confirm_uid` BIGINT,
  `confirm_family_id` BIGINT,
  `confirm_time` DATETIME,
  `lottery_flag` TINYINT NOT NULL DEFAULT 0,
  `lottery_time` DATETIME,
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
  `phone` VARCHAR(32),
  `real_name` VARCHAR(128),
  `gender` TINYINT,
  `community_name` VARCHAR(64),
  `organization_name` VARCHAR(128),
  `position` VARCHAR(64),
  `leader_flag` TINYINT,
  `source_flag` TINYINT,
  `email` VARCHAR(128),
  `pay_flag` TINYINT DEFAULT 0 COMMENT '0: no pay, 1:have pay, 2:refund',
  `order_no` BIGINT,
  `order_start_time` DATETIME,
  `order_expire_time` DATETIME,
  `vendor_type` VARCHAR(32) COMMENT '10001: alipay, 10002: wechatpay',
  `pay_amount` DECIMAL(10,2),
  `pay_time` DATETIME,
  `refund_order_no` BIGINT,
  `refund_amount` DECIMAL(10,2),
  `refund_time` DATETIME,
  `status` TINYINT DEFAULT 2 COMMENT '0: cancel, 1: reject, 2:normal',
  `organization_id` BIGINT,
  `cancel_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_act_roster_uuid` (`uuid`),
  KEY `i_eh_act_roster_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_activity_video`;
CREATE TABLE `eh_activity_video` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `video_state` TINYINT NOT NULL DEFAULT 0 COMMENT '0:UN_READY, 1:DEBUG, 2:LIVE, 3:RECORDING, 4:EXCEPTION, 5:INVALID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'activity',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `start_time` BIGINT NOT NULL DEFAULT 0,
  `end_time` BIGINT NOT NULL DEFAULT 0,
  `room_type` VARCHAR(64),
  `room_id` VARCHAR(64),
  `video_sid` VARCHAR(64),
  `manufacturer_type` VARCHAR(64) COMMENT 'YZB',
  `extra` TEXT,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of address partition
-- the message of this address
--
DROP TABLE IF EXISTS `eh_address_messages`;
CREATE TABLE `eh_address_messages` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `appId` BIGINT,
  `address_id` BIGINT NOT NULL,
  `sender_type` VARCHAR(32),
  `sender_token` VARCHAR(32),
  `sender_login_id` INTEGER,
  `sender_identify` VARCHAR(128),
  `body` VARCHAR(256),
  `meta` VARCHAR(512),
  `extra` VARCHAR(512),
  `sender_tag` VARCHAR(32),
  `body_type` VARCHAR(32),
  `deliveryOption` INTEGER NOT NULL,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- Key table in address related sharding group
-- shared resources, custom fields may not really be needed
--
DROP TABLE IF EXISTS `eh_addresses`;
CREATE TABLE `eh_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `community_id` BIGINT COMMENT 'NULL: means it is an independent street address, otherwise, it is an appartment address',
  `city_id` BIGINT,
  `city_name` VARCHAR(64) COMMENT 'redundant for query optimization',
  `area_id` BIGINT NOT NULL COMMENT 'area id in region table',
  `area_name` VARCHAR(64) COMMENT 'redundant for query optimization',
  `zipcode` VARCHAR(16),
  `address` VARCHAR(128),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),
  `address_alias` VARCHAR(128),
  `building_name` VARCHAR(128),
  `building_alias_name` VARCHAR(128),
  `apartment_name` VARCHAR(128),
  `apartment_floor` VARCHAR(16),
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: confirming, 2: active',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'uid of the user who process the address',
  `operate_time` DATETIME,
  `creator_uid` BIGINT COMMENT 'uid of the user who has suggested address, NULL if it is system created',
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  `area_size` DOUBLE,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `rent_area` DOUBLE,
  `build_area` DOUBLE,
  `inner_area` DOUBLE,
  `layout` VARCHAR(128),
  `living_status` TINYINT,
  `namespace_address_type` VARCHAR(128),
  `namespace_address_token` VARCHAR(128),
  `business_building_name` VARCHAR(128),		  
  `business_apartment_name` VARCHAR(128),
  PRIMARY KEY (`id`),
  KEY `i_eh_addr_city` (`city_id`),
  KEY `i_eh_addr_community` (`community_id`),
  KEY `i_eh_addr_address_alias` (`address_alias`),
  KEY `i_eh_addr_building_apt_name` (`building_name`,`apartment_name`),
  KEY `i_eh_addr_building_alias_apt_name` (`building_alias_name`,`apartment_name`),
  KEY `i_eh_addr_address` (`address`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- partition of eh_door_access
--
DROP TABLE IF EXISTS `eh_aes_server_key`;
CREATE TABLE `eh_aes_server_key` (
  `id` BIGINT NOT NULL COMMENT 'id of the record, also as secret_ver',
  `door_id` BIGINT NOT NULL,
  `device_ver` TINYINT NOT NULL COMMENT 'ver of aclink: 0x0 or 0x1',
  `secret_ver` BIGINT NOT NULL COMMENT 'ignore it',
  `secret` VARCHAR(64) NOT NULL COMMENT 'The base64 secret 16B',
  `create_time_ms` BIGINT,

  PRIMARY KEY (`id`),
  KEY `fk_eh_aes_server_key_door_id` (`door_id`),
  CONSTRAINT `eh_aes_server_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- partition of eh_door_access
--
DROP TABLE IF EXISTS `eh_aes_user_key`;
CREATE TABLE `eh_aes_user_key` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `key_id` INTEGER NOT NULL COMMENT 'lazy load for aes_user_key',
  `key_type` TINYINT NOT NULL COMMENT '0: aclink normal key',
  `door_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `auth_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'auth id of user key',
  `expire_time_ms` BIGINT NOT NULL,
  `create_time_ms` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `secret` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL,

  PRIMARY KEY (`id`),
  KEY `fk_eh_aes_user_key_door_id` (`door_id`),
  CONSTRAINT `eh_aes_user_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_app_namespace_mappings`;
CREATE TABLE `eh_app_namespace_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `app_key` VARCHAR(64),
  `community_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_app_profiles`;
CREATE TABLE `eh_app_profiles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `app_id` BIGINT NOT NULL COMMENT 'owner app id',
  `item_group` VARCHAR(32) COMMENT 'for profile grouping purpose',
  `item_name` VARCHAR(32),
  `item_value` TEXT,
  `item_tag` VARCHAR(32) COMMENT 'for profile value tagging purpose',

  PRIMARY KEY (`id`),
  KEY `i_eh_appprof_app_id_group` (`app_id`,`item_group`),
  CONSTRAINT `eh_app_profiles_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `eh_apps` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
-- for compatibility reason, this table is basically cloned from old DB
--
DROP TABLE IF EXISTS `eh_app_promotions`;
CREATE TABLE `eh_app_promotions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(128),
  `channel` INTEGER COMMENT '1: offical site, 2: app store, 3: manual',
  `version` VARCHAR(256),
  `path` VARCHAR(1024),
  `file_name` VARCHAR(128),
  `link` VARCHAR(1024),
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `register_count` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',

  PRIMARY KEY (`id`),
  KEY `i_app_promo_create_time` (`create_time`),
  KEY `i_app_promo_delete_time` (`delete_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_app_urls`;
CREATE TABLE `eh_app_urls` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `name` VARCHAR(128) NOT NULL,
  `os_type` TINYINT NOT NULL DEFAULT 0,
  `download_url` VARCHAR(128),
  `logo_url` VARCHAR(128),
  `description` TEXT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- app版本
DROP TABLE IF EXISTS `eh_app_version`;
CREATE TABLE `eh_app_version` (
  `id` BIGINT NOT NULL,
  `type` VARCHAR(20) NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `realm` VARCHAR(64) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 审批附件表
DROP TABLE IF EXISTS  `eh_approval_attachments`;
CREATE TABLE `eh_approval_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 审批具体类别表
DROP TABLE IF EXISTS  `eh_approval_categories`;
CREATE TABLE `eh_approval_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
  `category_name` VARCHAR(64) NOT NULL COMMENT 'name of category',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 申请时间具体到每一天的实际时长
DROP TABLE IF EXISTS  `eh_approval_day_actual_time`;
CREATE TABLE `eh_approval_day_actual_time` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g request_id',
  `user_id` BIGINT NOT NULL,
  `time_date` DATE NOT NULL COMMENT 'concrete date',
  `actual_result` VARCHAR(128) COMMENT 'actual result, e.g 1day3hours',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 审批流程对应每级的人/角色表
DROP TABLE IF EXISTS  `eh_approval_flow_levels`;
CREATE TABLE `eh_approval_flow_levels` (
  `id` BIGINT NOT NULL,
  `flow_id` BIGINT NOT NULL COMMENT 'id of flow',
  `level` TINYINT NOT NULL COMMENT '1,2,3,4,5...',
  `target_type` TINYINT NOT NULL COMMENT '1. user, 2. role',
  `target_id` BIGINT NOT NULL COMMENT 'id of target, e.g id of user',
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 审批流程表
DROP TABLE IF EXISTS `eh_approval_flows`;
CREATE TABLE `eh_approval_flows` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `name` VARCHAR(64) NOT NULL COMMENT 'name of flow',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 申请处理日志表
DROP TABLE IF EXISTS `eh_approval_op_requests`;
CREATE TABLE `eh_approval_op_requests` (
  `id` BIGINT NOT NULL,
  `request_id` BIGINT NOT NULL COMMENT 'id of request',
  `requestor_comment` TEXT COMMENT 'comment of reqeust',
  `process_message` TEXT COMMENT 'process message',
  `flow_id` BIGINT COMMENT 'id of flow',
  `level` TINYINT COMMENT 'which level approved',
  `operator_uid` BIGINT,
  `create_time` DATETIME,
  `approval_status` TINYINT NOT NULL COMMENT '0. waitingForApproving, 1. agreement, 2. rejection',
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_approval_range_statistics`;
CREATE TABLE `eh_approval_range_statistics` (
  `id` BIGINT NOT NULL,
  `punch_month` VARCHAR(8) COMMENT 'yyyymm',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `user_id` BIGINT COMMENT 'user id',
  `approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
  `category_id` BIGINT COMMENT 'concrete category id',
  `actual_result` VARCHAR(128) COMMENT 'actual result, e.g 1day3hours 1.3.0',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 申请记录表
DROP TABLE IF EXISTS `eh_approval_requests`;
CREATE TABLE `eh_approval_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
  `category_id` BIGINT COMMENT 'concrete category id',
  `reason` VARCHAR(256) COMMENT 'approval reason',
  `content_json` LONGTEXT COMMENT 'json of concrete category content',
  `attachment_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'if there are attachments, 0. no, 1. yes',
  `time_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'if there are time ranges, 0. no, 1. yes',
  `flow_id` BIGINT COMMENT 'id of flow',
  `current_level` TINYINT COMMENT 'current level of flow',
  `next_level` TINYINT COMMENT 'next level of flow',
  `approval_status` TINYINT NOT NULL COMMENT '0. waitingForApproving, 1. agreement, 2. rejection',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `long_tag1` BIGINT COMMENT 'put some condition here',
  `string_tag1` VARCHAR(256) COMMENT 'put some condition here',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  `effective_date` DATE COMMENT 'when the request approval effective',
  `hour_length` DOUBLE COMMENT 'how long (hours) does the request effective',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 审批规则与流程关联表
DROP TABLE IF EXISTS `eh_approval_rule_flow_map`;
CREATE TABLE `eh_approval_rule_flow_map` (
  `id` BIGINT NOT NULL,
  `rule_id` BIGINT NOT NULL COMMENT 'id of rule',
  `approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
  `flow_id` BIGINT NOT NULL COMMENT 'id of flow',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 审批规则表
DROP TABLE IF EXISTS  `eh_approval_rules`;
CREATE TABLE `eh_approval_rules` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `name` VARCHAR(64) NOT NULL COMMENT 'name of approval rule',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 审批时间表（请假时间表）
DROP TABLE IF EXISTS `eh_approval_time_ranges`;
CREATE TABLE `eh_approval_time_ranges` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
  `from_time` DATETIME NOT NULL COMMENT 'must store concrete time',
  `end_time` DATETIME NOT NULL COMMENT 'must store concrete time',
  `type` TINYINT NOT NULL COMMENT '1. all day, 2. morning half day, 3. afternoon half day, 4. time',
  `actual_result` VARCHAR(128) COMMENT 'actual result, e.g 1day3hours',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_apps`;
CREATE TABLE `eh_apps` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `creator_uid` BIGINT,
  `app_key` VARCHAR(64),
  `secret_key` VARCHAR(1024),
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(2048),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0 - inactive, 1 - active',
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_app_reg_name` (`name`),
  UNIQUE KEY `u_eh_app_reg_app_key` (`app_key`),
  KEY `i_eh_app_reg_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 物业公司催缴记录
DROP TABLE IF EXISTS `eh_asset_bill_notify_records`;
CREATE TABLE `eh_asset_bill_notify_records` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `owner_type` VARCHAR(32) COMMENT 'notify record owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'notify record owner id',
  `target_type` VARCHAR(32) COMMENT 'notify record target type: community',
  `target_id` BIGINT NOT NULL COMMENT 'notify record target id: community id',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 用户模板字段表（给一组初始数据）
DROP TABLE IF EXISTS `eh_asset_bill_template_fields`;
CREATE TABLE `eh_asset_bill_template_fields` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `required_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: optional, 1: required',
  `selected_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: unselected, 1: selected',
  `owner_type` VARCHAR(32) COMMENT 'template field owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'template field owner id',
  `target_type` VARCHAR(32) COMMENT 'template field target type: community',
  `target_id` BIGINT NOT NULL COMMENT 'template field target id: community id',
  `field_name` VARCHAR(64) NOT NULL,
  `field_display_name` VARCHAR(64) NOT NULL,
  `field_custom_name` VARCHAR(64),
  `field_type` VARCHAR(64),
  `default_order` INTEGER,
  `template_version` BIGINT NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 物业账单3.0 by xiongying 20170320
-- 账单表
DROP TABLE IF EXISTS `eh_asset_bills`;
CREATE TABLE `eh_asset_bills` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `owner_type` VARCHAR(32) COMMENT 'bill owner type: enterprise',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'bill owner id: enterprise id',
  `target_type` VARCHAR(32) COMMENT 'bill target type: community',
  `target_id` BIGINT NOT NULL COMMENT 'bill target id: community id',
  `tenant_type` VARCHAR(32) COMMENT 'bill tenant type: family、enterprise',
  `tenant_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'bill tenant id: family、enterprise id',
  `source` TINYINT NOT NULL DEFAULT 0 COMMENT '0: auto, 1: third party, 2: manual',
  `account_period` DATETIME NOT NULL,
  `building_name` VARCHAR(128),
  `apartment_name` VARCHAR(128),
  `address_id` BIGINT NOT NULL,
  `contact_no` VARCHAR(32),
  `rental` DECIMAL(10,2) COMMENT '租金',
  `property_management_fee` DECIMAL(10,2) COMMENT '物业管理费',
  `unit_maintenance_fund` DECIMAL(10,2) COMMENT '本体维修基金',
  `late_fee` DECIMAL(10,2) COMMENT '滞纳金',
  `private_water_fee` DECIMAL(10,2) COMMENT '自用水费',
  `private_electricity_fee` DECIMAL(10,2) COMMENT '自用电费',
  `public_water_fee` DECIMAL(10,2) COMMENT '公共部分水费',
  `public_electricity_fee` DECIMAL(10,2) COMMENT '公共部分电费',
  `waste_disposal_fee` DECIMAL(10,2) COMMENT '垃圾处理费',
  `pollution_discharge_fee` DECIMAL(10,2) COMMENT '排污处理费',
  `extra_air_condition_fee` DECIMAL(10,2) COMMENT '加时空调费',
  `cooling_water_fee` DECIMAL(10,2) COMMENT '冷却水使用费',
  `weak_current_slot_fee` DECIMAL(10,2) COMMENT '弱电线槽使用费',
  `deposit_from_lease` DECIMAL(10,2) COMMENT '租赁保证金',
  `maintenance_fee` DECIMAL(10,2) COMMENT '维修费',
  `gas_oil_process_fee` DECIMAL(10,2) COMMENT '燃气燃油加工费',
  `hatch_service_fee` DECIMAL(10,2) COMMENT '孵化服务费',
  `pressurized_fee` DECIMAL(10,2) COMMENT '加压费',
  `parking_fee` DECIMAL(10,2) COMMENT '停车费',
  `other` DECIMAL(10,2) COMMENT '其他',
  `period_account_amount` DECIMAL(10,2),
  `period_unpaid_account_amount` DECIMAL(10,2),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: unpaid, 2: paid',
  `template_version` BIGINT NOT NULL,
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  `delete_uid` BIGINT,
  `delete_time` DATETIME(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_bill_account_period_address` (`account_period`,`address_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_asset_vendor`;
CREATE TABLE `eh_asset_vendor` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the vendor, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'used to display',
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_asset_vendor_owner_id` (`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_audit_logs`;
CREATE TABLE `eh_audit_logs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `app_id` BIGINT COMMENT 'application that provides the operation',
  `operator_uid` BIGINT,
  `requestor_uid` BIGINT COMMENT 'user who initiated the original request',
  `requestor_comment` TEXT,
  `operation_type` VARCHAR(32),
  `result_code` INTEGER COMMENT '0: common positive result, otherwise, application defined result code',
  `reason` VARCHAR(256),
  `resource_type` VARCHAR(32) COMMENT 'operation related resource type',
  `resource_id` BIGINT COMMENT 'operation related resource id',

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  `create_time` DATETIME COMMENT 'time of the operation that was performed',
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',

  PRIMARY KEY (`id`),
  KEY `i_eh_audit_operator_uid` (`operator_uid`),
  KEY `i_eh_audit_requestor_uid` (`requestor_uid`),
  KEY `i_eh_audit_create_time` (`create_time`),
  KEY `i_eh_audit_delete_time` (`delete_time`),
  KEY `i_eh_audit_itag1` (`integral_tag1`),
  KEY `i_eh_audit_itag2` (`integral_tag2`),
  KEY `i_eh_audit_stag1` (`string_tag1`),
  KEY `i_eh_audit_stag2` (`string_tag2`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- Record banner clicks at per user basis, due to amount of potential users, may
-- need to be put at user partition
--
--
DROP TABLE IF EXISTS `eh_banner_clicks`;
CREATE TABLE `eh_banner_clicks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(36) NOT NULL,
  `banner_id` BIGINT NOT NULL,
  `uid` BIGINT NOT NULL,
  `family_id` BIGINT COMMENT 'redundant info for query optimization',
  `click_count` BIGINT,
  `last_click_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_banner_clk_uuid` (`uuid`),
  UNIQUE KEY `u_eh_banner_clk_user` (`banner_id`,`uid`),
  KEY `i_eh_banner_clk_last_time` (`last_click_time`),
  KEY `i_eh_banner_clk_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- Record banner related user purchase, due to amount of potential users, may
-- need to be put at user partition
--
DROP TABLE IF EXISTS `eh_banner_orders`;
CREATE TABLE `eh_banner_orders` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `banner_id` BIGINT NOT NULL,
  `uid` BIGINT NOT NULL,
  `vendor_order_tag` VARCHAR(64),
  `amount` DECIMAL(10,0),
  `description` TEXT,
  `purchase_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `i_eh_banner_order_banner` (`banner_id`),
  KEY `i_eh_banner_order_user` (`uid`),
  KEY `i_eh_banner_order_purchase_time` (`purchase_time`),
  KEY `i_eh_banner_order_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
-- (scope_type, scope_id) : specifies the banner distribution scope
-- name : specifies an administrative banner name if needed
-- vendor_tag : a tag specified by external vendor to help associate everhome system and third-party system on the banner
-- poster_path: banner display image
-- action_name: action name defined by the banner serving application module
-- action_uri: action uri, for Everhomes application modules, action URI scheme uses evh:// as prefix, for external systems, URI scheme uses standard http:// or https://
-- (start_time, end_time) banner active time period, effective only after status has been put into active state
-- status: banner administrative status
-- order: default listing order, depends on banner slide-showing algorithm
--
DROP TABLE IF EXISTS `eh_banners`;
CREATE TABLE `eh_banners` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `appId` BIGINT,
  `banner_location` VARCHAR(2048),
  `banner_group` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the type to filter item when querying: GA, BIZ, PM, GARC, GANC, GAPS',
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` BIGINT,
  `name` VARCHAR(128),
  `vendor_tag` VARCHAR(64),
  `poster_path` VARCHAR(128),
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `start_time` DATETIME,
  `end_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: closed, 1: waiting for confirmation, 2: active',
  `order` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',
  `scene_type` VARCHAR(64) DEFAULT 'default',
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: default, 1: override, 2: revert 3:customized',
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- Resource management
-- key table of binary resource management sharding group
--
DROP TABLE IF EXISTS `eh_binary_resources`;
CREATE TABLE `eh_binary_resources` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `checksum` VARCHAR(128),
  `store_type` VARCHAR(32) COMMENT 'content store type',
  `store_uri` VARCHAR(32) COMMENT 'identify the store instance',
  `content_type` VARCHAR(32) COMMENT 'object content type',
  `content_length` BIGINT,
  `content_uri` VARCHAR(1024) COMMENT 'object link info on storage',
  `reference_count` BIGINT,
  `create_time` DATETIME,
  `access_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `i_eh_bin_res_checksum` (`checksum`),
  KEY `i_eh_bin_res_create_time` (`create_time`),
  KEY `i_eh_bin_res_access_time` (`access_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_borders`;
CREATE TABLE `eh_borders` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `private_address` VARCHAR(128) NOT NULL,
  `private_port` INTEGER NOT NULL DEFAULT '8086',
  `public_address` VARCHAR(128) NOT NULL,
  `public_port` INTEGER NOT NULL DEFAULT '80',
  `status` INTEGER NOT NULL DEFAULT 0 COMMENT '0 : disabled, 1: enabled',
  `config_tag` VARCHAR(32),
  `description` VARCHAR(256),

  PRIMARY KEY (`id`),
  KEY `i_eh_border_config_tag` (`config_tag`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 广播消息，add by tt, 20161028
DROP TABLE IF EXISTS `eh_broadcasts`;
CREATE TABLE `eh_broadcasts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(32) COMMENT 'group',
  `owner_id` BIGINT DEFAULT 0 COMMENT 'group_id',
  `title` VARCHAR(1024) COMMENT 'title',
  `content_type` VARCHAR(32) COMMENT 'object content type: text、rich text',
  `content` LONGTEXT COMMENT 'content data, depends on value of content_type',
  `content_abstract` TEXT COMMENT 'abstract of content data',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_communities sharding group
--
DROP TABLE IF EXISTS `eh_building_attachments`;
CREATE TABLE `eh_building_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `building_id` BIGINT NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_communities partition
--
DROP TABLE IF EXISTS `eh_buildings`;
CREATE TABLE `eh_buildings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refering to eh_communities',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'building name',
  `alias_name` VARCHAR(128),
  `manager_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the manager of the building',
  `contact` VARCHAR(128) COMMENT 'the phone number',
  `address` VARCHAR(1024),
  `area_size` DOUBLE,
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),
  `description` TEXT,
  `poster_uri` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: confirming, 2: active',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'uid of the user who process the address',
  `operate_time` DATETIME,
  `creator_uid` BIGINT COMMENT 'uid of the user who has suggested address, NULL if it is system created',
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `product_type` VARCHAR(128),
  `complete_date` DATETIME,
  `joinin_date` DATETIME,
  `floor_count` VARCHAR(64),
  `namespace_building_type` VARCHAR(128),
  `namespace_building_token` VARCHAR(128),
  `traffic_description` TEXT,
  `lift_description` TEXT,
  `pm_description` TEXT,
  `parking_lot_description` TEXT,
  `environmental_description` TEXT,
  `power_description` TEXT,
  `telecommunication_description` TEXT,
  `air_condition_description` TEXT,
  `security_description` TEXT,
  `fire_control_description` TEXT,
  `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form',
  `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field',
  `default_order` BIGINT NOT NULL,
  `manager_name` VARCHAR(128),
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_community_id_name` (`community_id`,`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_business_assigned_namespaces`;
CREATE TABLE `eh_business_assigned_namespaces` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner business id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `visible_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'business can see in namespace or not.0-hide,1-visible',
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_business_namespace_id` (`owner_id`,`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_business_assigned_scopes`;
CREATE TABLE `eh_business_assigned_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner business id',
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city',
  `scope_id` BIGINT,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_scope_owner_code_id` (`owner_id`,`scope_code`,`scope_id`),
  KEY `i_eh_bussiness_scope_owner_id` (`owner_id`),
  KEY `i_eh_bussiness_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_business_assigned_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_businesses` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_business_categories`;
CREATE TABLE `eh_business_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner business id',
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `category_path` VARCHAR(128),

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_bussiness_category_id` (`owner_id`,`category_id`),
  KEY `i_eh_bussiness_owner_id` (`owner_id`),
  CONSTRAINT `eh_business_categories_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_businesses` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 电商运营   add by xq.tian  2017/01/09
--
DROP TABLE IF EXISTS `eh_business_promotions`;
CREATE TABLE `eh_business_promotions`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `subject` VARCHAR(128) COMMENT 'commodity subject',
  `description` TEXT COMMENT 'commodity description',
  `poster_uri` VARCHAR(1024) COMMENT 'commodity poster uri',
  `price` DECIMAL(10,2) COMMENT 'commodity price',
  `commodity_url` VARCHAR(1024) COMMENT 'commodity url',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_business_visible_scopes`;
CREATE TABLE `eh_business_visible_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner business id',
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city',
  `scope_id` BIGINT,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_scope_owner_code_id` (`owner_id`,`scope_code`,`scope_id`),
  KEY `i_eh_bussiness_scope_owner_id` (`owner_id`),
  KEY `i_eh_bussiness_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_business_visible_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_businesses` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_businesses`;
CREATE TABLE `eh_businesses` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `target_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: zuolin biz, 2: third part url',
  `target_id` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT 'the original biz id',
  `biz_owner_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the owner of the shop',
  `contact` VARCHAR(128) COMMENT 'the name of shop owner',
  `phone` VARCHAR(128) COMMENT 'the phone of shop owner',
  `name` VARCHAR(512) NOT NULL DEFAULT '',
  `display_name` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the name used to display in desk',
  `logo_uri` VARCHAR(1024) COMMENT 'the logo uri of the shop',
  `url` VARCHAR(1024) COMMENT 'the url to access shop',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),
  `address` VARCHAR(1024),
  `description` TEXT NOT NULL,
  `update_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `visible_distance` DOUBLE DEFAULT '5000' COMMENT 'the distance between shop and user who can find the shop, unit: meter',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_categories`;
CREATE TABLE `eh_categories`(
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `link_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'point to the linked category (similar to soft link in file system)',
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy. It is much more safer to do so if an allocated category is broadly used',
  `logo_uri` VARCHAR(1024) COMMENT 'the logo uri of the category',
  `description` TEXT,

  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  KEY `i_eh_category_path` (`path`),
  KEY `i_eh_category_order` (`default_order`),
  KEY `i_eh_category_delete_time` (`delete_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_certs`;
CREATE TABLE `eh_certs` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `cert_type` INTEGER NOT NULL,
  `cert_pass` VARCHAR(128),
  `data` BLOB NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_certs_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_client_package_files`;
CREATE TABLE `eh_client_package_files`(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `package_id` BIGINT,
  `file_location` VARCHAR(256),
  `file_name` VARCHAR(128),
  `file_size` BIGINT,
  `file_md5` VARCHAR(64),

  PRIMARY KEY (`id`),
  KEY `fk_eh_cpkg_file_package` (`package_id`),
  CONSTRAINT `eh_client_package_files_ibfk_1` FOREIGN KEY (`package_id`) REFERENCES `eh_client_packages` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_client_packages`;
CREATE TABLE `eh_client_packages`(
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` VARCHAR(64),
  `version_code` BIGINT,
  `package_edition` TINYINT NOT NULL DEFAULT 1 COMMENT '1: user edition, 2: business edition, 3: community edition',
  `device_platform` TINYINT NOT NULL DEFAULT 1 COMMENT '1: andriod, 2: ios',
  `distribution_channel` INTEGER NOT NULL DEFAULT 1 COMMENT '1: official site',
  `tag` VARCHAR(128),
  `json_params` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- key table of the sharding group
-- shared resource objects, custom fields may not really be needed
--
DROP TABLE IF EXISTS `eh_communities`;
CREATE TABLE `eh_communities`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `city_id` BIGINT NOT NULL COMMENT 'city id in region table',
  `city_name` VARCHAR(64) COMMENT 'redundant for query optimization',
  `area_id` BIGINT NOT NULL COMMENT 'area id in region table',
  `area_name` VARCHAR(64) COMMENT 'redundant for query optimization',
  `name` VARCHAR(64),
  `alias_name` VARCHAR(64),
  `address` VARCHAR(512),
  `zipcode` VARCHAR(16),
  `description` TEXT,
  `detail_description` TEXT,
  `apt_segment1` VARCHAR(64),
  `apt_segment2` VARCHAR(64),
  `apt_segment3` VARCHAR(64),
  `apt_seg1_sample` VARCHAR(64),
  `apt_seg2_sample` VARCHAR(64),
  `apt_seg3_sample` VARCHAR(64),
  `apt_count` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT COMMENT 'user who suggested the creation',
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  `community_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: residential, 1: commercial',
  `default_forum_id` BIGINT NOT NULL DEFAULT 1 COMMENT 'the default forum for the community, forum-1 is system default forum',
  `feedback_forum_id` BIGINT NOT NULL DEFAULT 2 COMMENT 'the default forum for the community, forum-2 is system feedback forum',
  `update_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `area_size` DOUBLE COMMENT 'area size',

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_communities partition
--
DROP TABLE IF EXISTS `eh_community_geopoints`;
CREATE TABLE `eh_community_geopoints` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `community_id` BIGINT,
  `description` VARCHAR(64),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),

  PRIMARY KEY (`id`),
  KEY `i_eh_comm_description` (`description`),
  KEY `i_eh_comm_geopoints` (`geohash`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_communities partition
--
DROP TABLE IF EXISTS `eh_community_profiles`;
CREATE TABLE `eh_community_profiles` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `app_id` BIGINT,
  `owner_id` BIGINT NOT NULL COMMENT 'owner community id',
  `item_name` VARCHAR(32),
  `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque json object, 1: entity',
  `item_value` TEXT,
  `target_type` VARCHAR(32),
  `target_id` BIGINT,

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`),
  KEY `i_eh_cprof_item` (`app_id`,`owner_id`,`item_name`),
  KEY `i_eh_cprof_owner` (`owner_id`),
  KEY `i_eh_cprof_itag1` (`integral_tag1`),
  KEY `i_eh_cprof_itag2` (`integral_tag2`),
  KEY `i_eh_cprof_stag1` (`string_tag1`),
  KEY `i_eh_cprof_stag2` (`string_tag2`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_community_services`;
CREATE TABLE `eh_community_services` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` BIGINT,
  `item_name` VARCHAR(32),
  `item_label` VARCHAR(64),
  `icon_uri` VARCHAR(1024),
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_account_categories`;
CREATE TABLE `eh_conf_account_categories` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `multiple_account_threshold` INTEGER NOT NULL DEFAULT 0 COMMENT 'the limit value of mutiple buy channel',
  `conf_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 25方仅视频, 1: 25方支持电话, 2: 100方仅视频, 3: 100方支持电话, 4: 6方仅视频, 5: 50方仅视频, 6: 50方支持电话',
  `min_period` INTEGER NOT NULL DEFAULT 1 COMMENT 'the minimum count of months',
  `single_account_price` DECIMAL(10,2),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'display when online or offline, 0: all, 1: online, 2: offline',
  `multiple_account_price` DECIMAL(10,2),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_account_histories`;
CREATE TABLE `eh_conf_account_histories` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id',
  `expired_date` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 1: active, 2: locked',
  `account_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_conf_account_categories',
  `account_type` TINYINT NOT NULL DEFAULT 2 COMMENT '0: none, 1: trial, 2: normal',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who use the account, reference to the id of eh_users',
  `own_time` DATETIME COMMENT 'the time when the user own the account',
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the account',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operation_type` VARCHAR(32),
  `process_details` TEXT,
  `operate_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_accounts`;
CREATE TABLE `eh_conf_accounts` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id',
  `expired_date` DATETIME,
  `status` TINYINT COMMENT '0-inactive 1-active 2-locked',
  `account_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_conf_account_categories',
  `account_type` TINYINT NOT NULL DEFAULT 2 COMMENT '0: none, 1: trial, 2: normal',
  `assigned_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether there is a source account assiged to the account, 0: none, 1: assigned',
  `assigned_source_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the source account id assigned to the account, reference to the id of eh_source_accounts',
  `assigned_time` DATETIME COMMENT 'the time when the source account is assigned to the account',
  `assigned_conf_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'referenece to the id of eh_conf_conferences',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who use the account, reference to the id of eh_users',
  `own_time` DATETIME COMMENT 'the time when the user own the account',
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the account',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_conferences`;
CREATE TABLE `eh_conf_conferences` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `meeting_no` BIGINT NOT NULL DEFAULT 0 COMMENT 'the meeting no from 3rd conference provider',
  `subject` VARCHAR(128) COMMENT 'the conference subject from 3rd conference provider',
  `description` TEXT,
  `start_time` DATETIME,
  `end_time` DATETIME,
  `expect_duration` INTEGER NOT NULL DEFAULT 0 COMMENT 'how long the conference expected to last, unit: minute',
  `real_duration` INTEGER NOT NULL DEFAULT 0 COMMENT 'how long the conference really lasted, unit: minute',
  `conf_host_id` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'the conf host id from 3rd conference provider',
  `conf_host_name` VARCHAR(256) NOT NULL DEFAULT 0 COMMENT 'the conf host name of the conference',
  `max_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'the max amount of allowed attendees',
  `conf_host_key` VARCHAR(128) COMMENT 'the password of the conference, set by the creator',
  `join_policy` INTEGER NOT NULL DEFAULT 1 COMMENT '0: free join, 1: conf host first',
  `source_account_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to eh_source_accounts',
  `conf_account_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to eh_conf_accounts',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the account',
  `join_url` VARCHAR(256) COMMENT 'user use the url to join the meeting',
  `start_url` VARCHAR(256) COMMENT 'user who start the meeting use this url',
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: close, 1: on progress, 2: failed',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `conference_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the conference id from 3rd conference provider',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_enterprises`;
CREATE TABLE `eh_conf_enterprises` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id, reference to the id of eh_groups, unique',
  `contact_name` VARCHAR(128),
  `contact` VARCHAR(128),
  `account_amount` INTEGER NOT NULL DEFAULT 0 COMMENT 'the total amount of active or inactive accounts the enterprise owned',
  `trial_account_amount` INTEGER NOT NULL DEFAULT 0 COMMENT 'the total amount of trial accounts the enterprise owned',
  `active_account_amount` INTEGER NOT NULL DEFAULT 0 COMMENT 'the total amount of active accounts the enterprise owned',
  `buy_channel` TINYINT NOT NULL DEFAULT 0 COMMENT '0: offline, 1: online',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active, 2: locked',
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME,
  `update_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the enterrpise',
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_enterprise_id` (`enterprise_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_invoices`;
CREATE TABLE `eh_conf_invoices` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `order_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_conf_orders',
  `taxpayer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: INDIVIDUAL_TAXPAYER, 2: COMPANY_TAXPAYER',
  `vat_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: GENERAL_TAXPAYER, 2: NON_GENERAL_TAXPAYER',
  `expense_type` TINYINT COMMENT '0: none, 1: CONF',
  `company_name` VARCHAR(20),
  `vat_code` VARCHAR(20),
  `vat_address` VARCHAR(128),
  `vat_phone` VARCHAR(20),
  `vat_bank_name` VARCHAR(20),
  `vat_bank_account` VARCHAR(20),
  `address` VARCHAR(128),
  `zip_code` VARCHAR(20),
  `consignee` VARCHAR(20),
  `contact` VARCHAR(20),
  `contract_flag` TINYINT COMMENT '0-dont need 1-need',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;












DROP TABLE IF EXISTS `eh_conf_order_account_map`;
CREATE TABLE `eh_conf_order_account_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `order_id` BIGINT NOT NULL DEFAULT 0,
  `enterprise_id` BIGINT NOT NULL DEFAULT 0,
  `conf_account_id` BIGINT NOT NULL DEFAULT 0,
  `assiged_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether the account has assiged to user, 0: none, 1: assigned',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `conf_account_namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_orders`;
CREATE TABLE `eh_conf_orders` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the enterprise id who own the order',
  `payer_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who pay the order',
  `paid_time` DATETIME COMMENT 'the pay time of the bill',
  `quantity` INTEGER NOT NULL DEFAULT 0 COMMENT 'the quantity of accounts which going to buy',
  `period` INTEGER NOT NULL DEFAULT 0 COMMENT 'the months which every account can be used',
  `amount` DECIMAL(10,2) COMMENT 'the paid money amount',
  `description` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: waiting for pay, 2: paid',
  `invoice_req_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether the order needs invoice or not, 0: none, 1: request',
  `invoice_issue_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether the invoice is issued or not, 0: none, 1: invoiced',
  `account_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_conf_account_categories',
  `online_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: offline, 1: online',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who make the order',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `buyer_name` VARCHAR(128),
  `buyer_contact` VARCHAR(128),
  `vendor_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'vendor type 0: none, 1: Alipay, 2: Wechat',
  `email` VARCHAR(128),
  `expired_date` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_reservations`;
CREATE TABLE `eh_conf_reservations` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id, reference to the id of eh_groups, unique',
  `creator_phone` VARCHAR(20),
  `conf_account_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'referenece to id of eh_conf_accounts',
  `subject` VARCHAR(128) COMMENT 'the conference subject',
  `description` TEXT,
  `conf_host_name` VARCHAR(256) NOT NULL DEFAULT 0 COMMENT 'the conf host name of the conference',
  `conf_host_key` VARCHAR(128) COMMENT 'the password of the conference, set by the creator',
  `start_time` DATETIME,
  `time_zone` VARCHAR(64),
  `duration` INTEGER NOT NULL DEFAULT 0 COMMENT 'how long the conference expected to last, unit: minute',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active, 2: locked',
  `update_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who create the reservation',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_conf_source_accounts`;
CREATE TABLE `eh_conf_source_accounts` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `account_name` VARCHAR(128) NOT NULL DEFAULT '',
  `password` VARCHAR(128),
  `account_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_conf_account_categories',
  `expired_date` DATETIME,
  `status` TINYINT COMMENT '0: inactive 1: active',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_configurations`;
CREATE TABLE `eh_configurations` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL,
  `value` VARCHAR(512) NOT NULL,
  `description` VARCHAR(256),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `display_name` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_content_server`;
CREATE TABLE  `eh_content_server` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'content server id',
  `name` VARCHAR(32),
  `description` VARCHAR(40),
  `private_address` VARCHAR(32),
  `private_port` INTEGER,
  `public_address` VARCHAR(32) NOT NULL,
  `public_port` INTEGER NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_content_server_resources`;
CREATE  TABLE  `eh_content_server_resources` (
  `id` BIGINT NOT NULL COMMENT 'the id of record',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `resource_id` VARCHAR(512),
  `resource_md5` VARCHAR(256) NOT NULL,
  `resource_type` INTEGER NOT NULL COMMENT 'current support audio,image and video',
  `resource_size` INTEGER NOT NULL,
  `resource_name` VARCHAR(128) NOT NULL,
  `metadata` TEXT,
  
  PRIMARY KEY (`id`),
  KEY `i_eh_resource_id` (`resource_id`(20))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_content_shard_map`;
CREATE TABLE `eh_content_shard_map` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `sharding_domain` VARCHAR(32) NOT NULL,
  `sharding_page` BIGINT,
  `shard_id` INTEGER,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_csm_domain_page` (`sharding_domain`,`sharding_page`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 合同与楼栋门牌对应表， add by tt, 20161117
DROP TABLE IF EXISTS `eh_contract_building_mappings`;
CREATE TABLE `eh_contract_building_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `organization_name` VARCHAR(64),
  `contract_id` BIGINT,
  `contract_number` VARCHAR(128),
  `building_name` VARCHAR(128),
  `apartment_name` VARCHAR(128),
  `area_size` DOUBLE,
  `status` TINYINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_contracts`;
CREATE TABLE `eh_contracts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `organization_name` VARCHAR(64),
  `contract_number` VARCHAR(128) NOT NULL,
  `contract_end_date` DATETIME NOT NULL,
  `status` TINYINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_cooperation_requests`;
CREATE TABLE `eh_cooperation_requests` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `cooperation_type` VARCHAR(64) NOT NULL COMMENT 'coperation type, NONE, BIZ, PARK, PM, GA',
  `province_name` VARCHAR(64) COMMENT 'province',
  `city_name` VARCHAR(64) COMMENT 'city',
  `area_name` VARCHAR(64) COMMENT 'area',
  `community_names` TEXT COMMENT 'community name, split with comma if there are multiple communties',
  `address` VARCHAR(128) COMMENT 'address of the cooperator',
  `name` VARCHAR(128) COMMENT 'name of the cooperator entity',
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'contact type of cooperator entity, 0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address of cooperator entity',
  `applicant_name` VARCHAR(128) COMMENT 'the name of applicant',
  `applicant_occupation` VARCHAR(128) COMMENT 'the occupation of applicant',
  `applicant_phone` VARCHAR(64) COMMENT 'the phone number of applicant',
  `applicant_email` VARCHAR(128) COMMENT 'the email address of applicant',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_devices`;
CREATE TABLE `eh_devices` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `device_id` VARCHAR(128) NOT NULL,
  `platform` VARCHAR(32) NOT NULL,
  `product` VARCHAR(32),
  `brand` VARCHAR(32),
  `device_model` VARCHAR(32),
  `system_version` VARCHAR(32),
  `meta` VARCHAR(256),
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_dev_id` (`device_id`),
  KEY `u_eh_dev_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 对接映射表 
DROP TABLE IF EXISTS `eh_docking_mappings`;
CREATE TABLE `eh_docking_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',  
  `scope` VARCHAR(64) NOT NULL,
  `name` VARCHAR(256),
  `mapping_value` VARCHAR(256),
  `mapping_json` VARCHAR(1024),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- key table partition of eh_door_access
--
DROP TABLE IF EXISTS `eh_door_access`;
CREATE TABLE `eh_door_access` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(64) NOT NULL,
  `door_type` TINYINT NOT NULL COMMENT '0: Zuolin aclink with wifi, 1: Zuolink aclink without wifi',
  `hardware_id` VARCHAR(64) NOT NULL COMMENT 'mac address of aclink',
  `name` VARCHAR(128) NOT NULL,
  `description` VARCHAR(1024),
  `avatar` VARCHAR(128),
  `address` VARCHAR(128),
  `active_user_id` BIGINT NOT NULL,
  `creator_user_id` BIGINT NOT NULL,
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(64),
  `aes_iv` VARCHAR(64) NOT NULL,
  `link_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: linked, 1: failed',

  `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
  `owner_id` BIGINT NOT NULL,
  `role` TINYINT NOT NULL DEFAULT 0,

  `create_time` DATETIME,
  `status` TINYINT NOT NULL COMMENT '0:activing, 1: active',

  `acking_secret_version` INTEGER NOT NULL DEFAULT 1,
  `expect_secret_key` INTEGER NOT NULL DEFAULT 1,
  `groupId` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_door_access_uuid` (`uuid`),
  KEY `i_eh_door_access_name` (`name`),
  KEY `i_eh_door_hardware_id` (`hardware_id`),
  KEY `i_eh_door_access_owner` (`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- Global table for relationship of owner 1<-->n door_auth
--
DROP TABLE IF EXISTS `eh_door_auth`;
CREATE TABLE `eh_door_auth` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `door_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `approve_user_id` BIGINT NOT NULL,
  `auth_type` TINYINT NOT NULL COMMENT '0: forever, 1: temperate',
  `valid_from_ms` BIGINT NOT NULL DEFAULT 0,
  `valid_end_ms` BIGINT NOT NULL DEFAULT 0,

  `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
  `owner_id` BIGINT NOT NULL,

  `organization` VARCHAR(128),
  `description` VARCHAR(1024),

  `nickname` VARCHAR(64),
  `phone` VARCHAR(64),

  `create_time` DATETIME,
  `status` TINYINT NOT NULL,
  `driver` VARCHAR(32) NOT NULL DEFAULT 'zuolin',
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `string_tag6` TEXT,
  `right_open` TINYINT NOT NULL DEFAULT 0,
  `right_visitor` TINYINT NOT NULL DEFAULT 0,
  `right_remote` TINYINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  KEY `fk_eh_door_auth_door_id` (`door_id`),
  KEY `fk_eh_door_auth_user_id` (`user_id`),
  CONSTRAINT `eh_door_auth_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_door_auth_logs`;
CREATE TABLE `eh_door_auth_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `door_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `is_auth` TINYINT NOT NULL DEFAULT 0,
  `right_content` VARCHAR(1024) NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `create_uid` BIGINT NOT NULL,
  `discription` TEXT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- Partion of eh_door_command
-- Any action will generate a command, new command maybe override old command
--
DROP TABLE IF EXISTS `eh_door_command`;
CREATE TABLE `eh_door_command` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `door_id` BIGINT NOT NULL,
  `cmd_id` TINYINT NOT NULL COMMENT 'cmd id for aclink',
  `cmd_type` TINYINT NOT NULL COMMENT 'cmd type for aclink',
  `cmd_body` TEXT COMMENT 'json type of cmd body',
  `cmd_resp` TEXT COMMENT 'json resp of cmd resp body',
  `server_key_ver` BIGINT NOT NULL COMMENT 'cmd of server key',
  `aclink_key_ver` TINYINT NOT NULL COMMENT 'cmd of aclink key',
  `status` TINYINT NOT NULL COMMENT '0: creating, 1: sending, 2: response, 3: process, 4: invalid',
  `user_id` BIGINT,
  `owner_id` BIGINT,
  `owner_type` TINYINT,

  PRIMARY KEY (`id`),
  KEY `fk_eh_door_command_id` (`door_id`),
  CONSTRAINT `eh_door_command_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- global table. 添加门禁特殊权限相关用户类型 add by Janson 20161028
--
DROP TABLE IF EXISTS `eh_door_user_permission`;
CREATE TABLE `eh_door_user_permission` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `user_id` BIGINT NOT NULL,
  `approve_user_id` BIGINT NOT NULL,
  `auth_type` TINYINT NOT NULL COMMENT '0: Door Guard',
  `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `description` VARCHAR(1024),
  `create_time` DATETIME,
  `status` TINYINT NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 总量记录表
--
DROP TABLE IF EXISTS `eh_energy_count_statistics`;
CREATE TABLE `eh_energy_count_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `meter_type` TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `date_str` VARCHAR(20),
  `statistic_type` TINYINT COMMENT '1:bill, 2: service, 3:burden',
  `bill_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `bill_category_name` VARCHAR(255),
  `service_category_name` VARCHAR(255),
  `amount` DECIMAL(10,1),
  `cost` DECIMAL(10,1),
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 每日水电报表
--
DROP TABLE IF EXISTS `eh_energy_date_statistics`;
CREATE TABLE `eh_energy_date_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `meter_id` BIGINT NOT NULL COMMENT '水表id',
  `meter_type` TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `stat_date` DATE COMMENT '改成用日期存,方便过滤和计算',
  `bill_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `formula_id` TINYINT COMMENT 'eh_energy_meter_formulas id',
  `meter_name` VARCHAR(255),
  `meter_number` VARCHAR(255),
  `meter_bill` VARCHAR(255) COMMENT 'meter bill category name',
  `meter_service` VARCHAR(255) COMMENT 'meter service category name',
  `meter_rate` DECIMAL(10,2) COMMENT '表的倍率',
  `meter_price` DECIMAL(10,2) COMMENT '表的单价',
  `last_reading` DECIMAL(10,1) COMMENT '上次读数',
  `current_reading` DECIMAL(10,1) COMMENT '这次读数',
  `current_amount` DECIMAL(10,1) COMMENT '用量',
  `current_cost` DECIMAL(10,1) COMMENT '费用',
  `reset_meter_flag` TINYINT COMMENT '0: normal, 1: reset',
  `change_meter_flag` TINYINT COMMENT '0: normal, 1: change',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费',
  `config_id` BIGINT COMMENT 'if setting_type is price and  have this value',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 表记分类属性
--
DROP TABLE IF EXISTS `eh_energy_meter_categories`;
CREATE TABLE `eh_energy_meter_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(255) COMMENT 'name',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `category_type` TINYINT COMMENT '1: bill, 2: service',
  `delete_flag` TINYINT COMMENT '0: can delete, 1: can not delete',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 换表记录
--
DROP TABLE IF EXISTS `eh_energy_meter_change_logs`;
CREATE TABLE `eh_energy_meter_change_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `meter_id` BIGINT,
  `reading_log_id` BIGINT COMMENT 'eh_energy_meter_reading_logs id',
  `old_reading` DECIMAL(10,1) COMMENT 'The reading of the old meter',
  `new_reading` DECIMAL(10,1) COMMENT 'The initial reading of the new meter',
  `max_reading` DECIMAL(10,1) COMMENT 'The maximum range of the new meter',
  `operator_id` BIGINT,
  `operate_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 默认显示的属性值
--
DROP TABLE IF EXISTS `eh_energy_meter_default_settings`;
CREATE TABLE `eh_energy_meter_default_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `meter_type` TINYINT COMMENT '1:WATER, 2: ELECTRIC, 3: ALL',
  `setting_type` TINYINT COMMENT '1: price, 2: rate, 3: amountFormula, 4: costFormula, 5: dayPrompt, 6: monthPrompt etc',
  `name` VARCHAR(255) COMMENT 'setting name',
  `setting_value` DECIMAL(10,2) COMMENT 'if setting_type is price or rate have this value',
  `formula_id` BIGINT COMMENT 'if setting_type is amountFormula or costFormula have this value',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active, 3: disabled',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费',
  `config_id` BIGINT COMMENT 'if setting_type is price and  have this value',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 公式变量
--
DROP TABLE IF EXISTS `eh_energy_meter_formula_variables`;
CREATE TABLE `eh_energy_meter_formula_variables` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(255) COMMENT 'variable name',
  `display_name` VARCHAR(255) COMMENT 'display name',
  `description` VARCHAR(255) COMMENT 'description',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 表记计算公式
--
DROP TABLE IF EXISTS `eh_energy_meter_formulas`;
CREATE TABLE `eh_energy_meter_formulas` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(255) COMMENT 'formula name',
  `expression` VARCHAR(255) COMMENT 'expression',
  `display_expression` VARCHAR(255) COMMENT 'user input expression',
  `formula_type` TINYINT COMMENT '1: amount, 2: cost',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 梯度价格表
--
DROP TABLE IF EXISTS `eh_energy_meter_price_config`;
CREATE TABLE `eh_energy_meter_price_config` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price configs, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `name`         VARCHAR(255) COMMENT 'config name',
  `description`      VARCHAR(512) COMMENT 'description config',
  `expression`   VARCHAR(1024) COMMENT 'expression json',
  `status`       TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`  BIGINT,
  `create_time`  DATETIME,
  `update_uid`   BIGINT,
  `update_time`  DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 抄表记录
--
DROP TABLE IF EXISTS `eh_energy_meter_reading_logs`;
CREATE TABLE `eh_energy_meter_reading_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `meter_id` BIGINT,
  `reading` DECIMAL(10,1),
  `operator_id` BIGINT,
  `operate_time` DATETIME,
  `reset_meter_flag` TINYINT DEFAULT 0 COMMENT '0: normal, 1: reset',
  `change_meter_flag` TINYINT DEFAULT 0 COMMENT '0: normal, 1: change',
  `status` TINYINT DEFAULT 2 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 表记属性
--
DROP TABLE IF EXISTS `eh_energy_meter_setting_logs`;
CREATE TABLE `eh_energy_meter_setting_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `meter_id` BIGINT,
  `setting_type` TINYINT COMMENT '1: price, 2: rate, 3: amountFormula, 4: costFormula',
  `setting_value` DECIMAL(10,2) COMMENT 'if setting_type is price or rate have this value',
  `formula_id` BIGINT COMMENT 'if setting_type is amountFormula or costFormula have this value',
  `start_time` DATETIME COMMENT 'The start of the time period',
  `end_time` DATETIME COMMENT 'The end of the time period',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费',
  `config_id` BIGINT COMMENT 'if setting_type is price and  have this value',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 表记   add by xq.tian  2016/10/25
--
DROP TABLE IF EXISTS `eh_energy_meters`;
CREATE TABLE `eh_energy_meters` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `name` VARCHAR(100),
  `meter_number` VARCHAR(50) COMMENT 'meter number',
  `meter_type` TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `bill_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `max_reading` DECIMAL(10,1) COMMENT 'The maximum range of the meter',
  `start_reading` DECIMAL(10,1) COMMENT 'The initial reading of the meter',
  `rate` DECIMAL(10,2) COMMENT 'Calculate magnification',
  `price` DECIMAL(10,2),
  `cost_formula_id` BIGINT COMMENT 'Cost calculation formula',
  `amount_formula_id` BIGINT COMMENT 'Amount calculation formula',
  `last_read_time` DATETIME,
  `last_reading` DECIMAL(10,1),
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price energy meter, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费',
  `config_id` BIGINT COMMENT 'if setting_type is price and  have this value',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
--  每月水电报表
--
DROP TABLE IF EXISTS `eh_energy_month_statistics`;
CREATE TABLE `eh_energy_month_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `meter_id` BIGINT NOT NULL COMMENT '水表id',
  `meter_type` TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `date_str` VARCHAR(20) COMMENT 'YYYMM 例如 201608',
  `bill_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `formula_id` TINYINT COMMENT 'eh_energy_meter_formulas id',
  `meter_name` VARCHAR(255),
  `meter_number` VARCHAR(255),
  `meter_bill` VARCHAR(255) COMMENT 'meter bill category name',
  `meter_service` VARCHAR(255) COMMENT 'meter service category name',
  `meter_rate` DECIMAL(10,2),
  `meter_price` DECIMAL(10,2),
  `last_reading` DECIMAL(10,1),
  `current_reading` DECIMAL(10,1),
  `current_amount` DECIMAL(10,1),
  `current_cost` DECIMAL(10,1),
  `reset_meter_flag` TINYINT COMMENT '0: normal, 1: reset',
  `change_meter_flag` TINYINT COMMENT '0: normal, 1: change',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费',
  `config_id` BIGINT COMMENT 'if setting_type is price and  have this value',
  
  PRIMARY KEY (`id`)
  
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 各项目月水电能耗情况-同比
--
DROP TABLE IF EXISTS `eh_energy_yoy_statistics`;
CREATE TABLE `eh_energy_yoy_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `date_str` VARCHAR(20),
  `area_size` DOUBLE COMMENT 'Community area size',
  `water_receivable_amount` DECIMAL(10,1) COMMENT '水表本月应收用量',
  `water_payable_amount` DECIMAL(10,1) COMMENT '水表本月应付用量',
  `water_burden_amount` DECIMAL(10,1) COMMENT '水表本月负担公共用量',
  `water_average_amount` DECIMAL(10,1) COMMENT '水表本月每平米平均用量',
  `water_last_receivable_amount` DECIMAL(10,1) COMMENT '水表去年同期应收用量',
  `water_last_payable_amount` DECIMAL(10,1) COMMENT '水表去年同期应付用量',
  `water_last_burden_amount` DECIMAL(10,1) COMMENT '水表去年同期负担公共用量',
  `water_last_average_amount` DECIMAL(10,1) COMMENT '水表去年同期每平米平均用量',
  `electric_receivable_amount` DECIMAL(10,1) COMMENT '电表应收用量',
  `electric_payable_amount` DECIMAL(10,1) COMMENT '电表应付用量',
  `electric_burden_amount` DECIMAL(10,1) COMMENT '电表负担公共用量',
  `electric_average_amount` DECIMAL(10,1) COMMENT '电表每平米平均用量',
  `electric_last_receivable_amount` DECIMAL(10,1) COMMENT '电表去年同期应收用量',
  `electric_last_payable_amount` DECIMAL(10,1) COMMENT '电表去年同期应付用量',
  `electric_last_burden_amount` DECIMAL(10,1) COMMENT '电表去年同期负担公共用量',
  `electric_last_average_amount` DECIMAL(10,1) COMMENT '电表去年同期每平米平均用量',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups partition
-- the relationship between eh_enterprises and eh_enterprise_communities
--
DROP TABLE IF EXISTS `eh_enterprise_addresses`;
CREATE TABLE `eh_enterprise_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_groups',
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_addresses',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'redundant auditing info',
  `process_code` TINYINT,
  `process_details` TEXT,
  `proof_resource_uri` VARCHAR(1024),
  `approve_time` DATETIME COMMENT 'redundant auditing info',
  `update_time` DATETIME,

  `building_id` BIGINT NOT NULL DEFAULT 0,
  `building_name` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups sharding group
--
DROP TABLE IF EXISTS `eh_enterprise_attachments`;
CREATE TABLE `eh_enterprise_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_id` BIGINT NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_communities partition
-- the relationship between eh_enterprises and eh_enterprise_communities
--
DROP TABLE IF EXISTS `eh_enterprise_community_map`;
CREATE TABLE `eh_enterprise_community_map` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_communities',
  `member_type` VARCHAR(32) NOT NULL COMMENT 'enterprise',
  `member_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise_id etc',
  `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'redundant auditing info',
  `process_code` TINYINT,
  `process_details` TEXT,
  `proof_resource_uri` VARCHAR(1024),
  `approve_time` DATETIME COMMENT 'redundant auditing info',
  `requestor_comment` TEXT,
  `operation_type` TINYINT COMMENT '1: request to join, 2: invite to join',
  `inviter_uid` BIGINT COMMENT 'record inviter user id',
  `invite_time` DATETIME COMMENT 'the time the member is invited',
  `update_time` DATETIME NOT NULL,

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups partition
-- entry info of eh_enterprise_contacts
--
DROP TABLE IF EXISTS `eh_enterprise_contact_entries`;
CREATE TABLE `eh_enterprise_contact_entries` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `contact_id` BIGINT NOT NULL COMMENT 'contact id',
  `entry_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `entry_value` VARCHAR(128),
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups partition
-- role of member inside the group (internal)
--
DROP TABLE IF EXISTS `eh_enterprise_contact_group_members`;
CREATE TABLE `eh_enterprise_contact_group_members` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `contact_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_contact_groups',
  `contact_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_contacts',
  `role` BIGINT NOT NULL DEFAULT 7 COMMENT 'The role in company',
  `contact_avatar` VARCHAR(128) COMMENT 'contact avatar image identifier in storage sub-system',
  `contact_nick_name` VARCHAR(128) COMMENT 'contact nick name within the group',
  `contact_status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups partition
-- internal group in enterprise, use for department
--
DROP TABLE IF EXISTS `eh_enterprise_contact_groups`;
CREATE TABLE `eh_enterprise_contact_groups` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `role` BIGINT NOT NULL DEFAULT 7 COMMENT 'The role in company',
  `name` VARCHAR(256),
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `path` VARCHAR(128) COMMENT 'path from the root',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups partition
-- the relationship between eh_enterprises and eh_enterprise_communities
--
DROP TABLE IF EXISTS `eh_enterprise_contacts`;
CREATE TABLE `eh_enterprise_contacts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `name` VARCHAR(256) COMMENT 'real name',
  `nick_name` VARCHAR(256) COMMENT 'display name',
  `avatar` VARCHAR(128) COMMENT 'avatar uri',
  `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'user id reference to eh_users, it determine the contact authenticated or not',
  `role` BIGINT NOT NULL DEFAULT 7 COMMENT 'The role in company',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups partition
-- the supplement enterprise info of eh_groups
--
DROP TABLE IF EXISTS `eh_enterprise_details`;
CREATE TABLE `eh_enterprise_details` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_id` BIGINT NOT NULL COMMENT 'group id',
  `description` TEXT COMMENT 'description',
  `contact` VARCHAR(128) COMMENT 'the phone number',
  `address` VARCHAR(256) COMMENT 'address str',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 园区入驻申请的关联楼栋表
--

DROP TABLE IF EXISTS `eh_enterprise_op_request_buildings`;
CREATE TABLE `eh_enterprise_op_request_buildings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_op_requests_id` BIGINT NOT NULL COMMENT 'eh_enterprise_op_requests id',
  `building_id` BIGINT COMMENT 'building id ',
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- member of global partition
--

DROP TABLE IF EXISTS `eh_enterprise_op_requests`;
CREATE TABLE `eh_enterprise_op_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `source_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'enterprise, marker zone',
  `source_id` BIGINT NOT NULL DEFAULT 0,
  `enterprise_name` VARCHAR(128) COMMENT 'enterprise name',
  `enterprise_id` BIGINT NOT NULL DEFAULT 0,
  `apply_contact` VARCHAR(128) COMMENT 'contact phone',
  `apply_user_id` BIGINT COMMENT 'user id',
  `apply_user_name` VARCHAR(128) COMMENT 'apply user name',
  `apply_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'apply type 1:apply 2:The expansion of rent 3:Renew',
  `description` TEXT COMMENT 'description',
  `size_unit` TINYINT COMMENT '1:singleton 2:square meters',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: requesting, 2: accepted',
  `area_size` DOUBLE,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `process_message` TEXT,
  `process_time` DATETIME,
  `contract_id` BIGINT COMMENT 'eh_contracts id',
  `issuer_type` VARCHAR(128) COMMENT '1: organization 2: normal_user',
  `building_id` BIGINT NOT NULL DEFAULT 0,
  `address_id` BIGINT NOT NULL DEFAULT 0,
  `flowcase_id` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 备品备件表：
DROP TABLE IF EXISTS `eh_equipment_inspection_accessories`;
CREATE TABLE `eh_equipment_inspection_accessories` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the spare parts, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the accessory, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(1024),
  `manufacturer` VARCHAR(1024),
  `model_number` VARCHAR(1024),
  `specification` VARCHAR(1024),
  `location` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 设备-备件 关联表：equipment_id和accessory_id共同确立一条记录
DROP TABLE IF EXISTS `eh_equipment_inspection_accessory_map`;
CREATE TABLE `eh_equipment_inspection_accessory_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `accessory_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_accessories',
  `quantity` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检对象类型表
DROP TABLE IF EXISTS `eh_equipment_inspection_categories`;		
CREATE TABLE `eh_equipment_inspection_categories` (		
  `id` BIGINT NOT NULL,		
  `namespace_id` INTEGER NOT NULL DEFAULT 0,		
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',		
  `owner_id` BIGINT NOT NULL DEFAULT 0,		
  `parent_id` BIGINT NOT NULL DEFAULT 0,		
  `name` VARCHAR(64) NOT NULL,		
  `path` VARCHAR(128),		
  `default_order` INTEGER,		
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',		
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',		
  `create_time` DATETIME,		
  `deletor_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',		
  `delete_time` DATETIME,		
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 设备操作图示表 attachments 及说明书  type区分
DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_attachments`;
CREATE TABLE `eh_equipment_inspection_equipment_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `attachment_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: tu shi, 2: shuo ming shu',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 设备参数表
DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_parameters`;
CREATE TABLE `eh_equipment_inspection_equipment_parameters` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `parameter_name` VARCHAR(128),
  `parameter_unit` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 设备-标准映射表
DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_standard_map`;
CREATE TABLE `eh_equipment_inspection_equipment_standard_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_equipment_inspection_standards',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the standard',
  `review_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: reviewed，3: delete',
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `reviewer_uid` BIGINT NOT NULL DEFAULT 0,
  `review_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `last_create_task_time` DATETIME,
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 设备表
DROP TABLE IF EXISTS `eh_equipment_inspection_equipments`;
CREATE TABLE `eh_equipment_inspection_equipments` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the equipment, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `equipment_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: equipment, 1: equipment group',
  `custom_number` VARCHAR(128),
  `name` VARCHAR(1024),
  `manufacturer` VARCHAR(1024),
  `location` VARCHAR(1024),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(64),
  `equipment_model` VARCHAR(1024),
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_categories',
  `category_path` VARCHAR(128) COMMENT 'reference to the path of eh_categories',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: incomplete(不完整), 2: in use(使用中), 3: in maintenance(维修中), 4: discarded(报废), 5: disabled(停用), 6: standby(备用)',
  `installation_time` DATETIME,
  `repair_time` DATETIME,
  `initial_asset_value` VARCHAR(128),
  `parameter` VARCHAR(1024),
  `quantity` BIGINT NOT NULL DEFAULT 0,
  `remarks` TEXT,
  `sequence_no` VARCHAR(128),
  `version_no` VARCHAR(128),
  `manager` VARCHAR(128),
  `qr_code_token` TEXT,
  `qr_code_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `inspection_category_id` BIGINT,
  `namespace_id` INTEGER,
  `picture_flag` TINYINT DEFAULT 1 COMMENT 'whether need to take a picture while report equipment task, 0 not, 1 yes',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
  

-- 巡检项结果
DROP TABLE IF EXISTS `eh_equipment_inspection_item_results`;
CREATE TABLE `eh_equipment_inspection_item_results` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `task_log_id` BIGINT NOT NULL COMMENT 'id of the eh_equipment_inspection_task_logs',
  `task_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_tasks',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the result, equipment, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the result',
  `item_id` BIGINT NOT NULL DEFAULT 0,
  `item_name` VARCHAR(512) NOT NULL,
  `item_value_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0-none、1-two-tuple、2-range',
  `item_unit` VARCHAR(32),
  `item_value` VARCHAR(128),
  `normal_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: abnormal; 1: normal',
  `create_time` DATETIME,
  `community_id` BIGINT,
  `standard_id` BIGINT,
  `equipment_id` BIGINT,
  `inspection_category_id` BIGINT,
  `namespace_id` INTEGER,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 巡检项
DROP TABLE IF EXISTS `eh_equipment_inspection_items`;
CREATE TABLE `eh_equipment_inspection_items` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(512) NOT NULL,
  `value_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0-none、1-two-tuple、2-range',
  `unit` VARCHAR(32),
  `value_jason` VARCHAR(512),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 巡检标准和部门岗位关联关系表
DROP TABLE IF EXISTS `eh_equipment_inspection_standard_group_map`;		
CREATE TABLE `eh_equipment_inspection_standard_group_map` (		
  `id` BIGINT NOT NULL COMMENT 'id',		
  `group_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: executive group, 2: review group',		
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_standards',		
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',		
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',		
  `create_time` DATETIME,
  		
  PRIMARY KEY (`id`)		
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 参考标准表：
DROP TABLE IF EXISTS `eh_equipment_inspection_standards`;
CREATE TABLE `eh_equipment_inspection_standards` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `standard_number` VARCHAR(128),
  `name` VARCHAR(1024),
  `standard_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: routing inspection, 2:maintain',
  `repeat_setting_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_repeat_settings',
  `description` TEXT COMMENT 'content data',
  `remarks` TEXT,
  `standard_source` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: not completed, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `review_expired_days` INTEGER NOT NULL DEFAULT 0,
  `template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_template',
  `inspection_category_id` BIGINT,		  
  `namespace_id` INTEGER,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 任务attachments表
DROP TABLE IF EXISTS `eh_equipment_inspection_task_attachments`;
CREATE TABLE `eh_equipment_inspection_task_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `log_id` BIGINT NOT NULL COMMENT 'id of the eh_equipment_inspection_task_logs',
  `task_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_tasks',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 记录表
DROP TABLE IF EXISTS `eh_equipment_inspection_task_logs`;
CREATE TABLE `eh_equipment_inspection_task_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `task_id` BIGINT NOT NULL DEFAULT 0,
  `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who operates the task, USER, etc',
  `operator_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who is the target of processing the task, USER, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: complete, 2: complete maintenance, 3: review, 4: need maintenance ',
  `process_end_time` DATETIME,
  `process_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: complete ok, 2: complete delay, 3: need maintenance ok, 4: need maintenance delay, 5：need maintenance ok complete ok, 6: need maintenance ok complete delay, 7: need maintenance delay complete ok, 8: need maintenance delay complete delay, 9: review qualified, 10: review unqualified',
  `process_message` TEXT,
  `parameter_value` VARCHAR(1024),
  `process_time` DATETIME,
  `create_time` DATETIME,
  `inspection_category_id` BIGINT,
  `community_id` BIGINT,
  `namespace_id` INTEGER,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 任务表
DROP TABLE IF EXISTS `eh_equipment_inspection_tasks`;
CREATE TABLE `eh_equipment_inspection_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `task_number` VARCHAR(128),
  `task_name` VARCHAR(1024),
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '0: parent task, others children-task',
  `child_count` BIGINT NOT NULL DEFAULT 0,
  `executive_group_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, etc',
  `executive_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `executive_start_time` DATETIME,
  `executive_expire_time` DATETIME,
  `executive_time` DATETIME,
  `executor_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `executor_id` BIGINT NOT NULL DEFAULT 0,
  `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `operator_id` BIGINT NOT NULL DEFAULT 0,
  `process_expire_time` DATETIME,
  `process_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: waiting for executing, 2: waiting for maintenance, 3: in maintenance, 4: closed',
  `result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: complete ok, 2: complete delay, 3: need maintenance ok, 4: need maintenance delay, 5：need maintenance ok complete ok, 6: need maintenance ok complete delay, 7: need maintenance delay complete ok, 8: need maintenance delay complete delay',
  `reviewer_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
  `reviewer_id`  BIGINT NOT NULL DEFAULT 0,
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_time` DATETIME,
  `create_time` DATETIME,
  `review_expired_date` DATETIME,
  `inspection_category_id` BIGINT,		
  `namespace_id` INTEGER,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, etc',		
  `target_id` BIGINT NOT NULL DEFAULT 0,		
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  		
  PRIMARY KEY (`id`),		
  KEY `standard_id` (`standard_id`),		
  KEY `status` (`status`),		
  KEY `target_id` (`target_id`),		
  KEY `inspection_category_id` (`inspection_category_id`),		
  KEY `executive_expire_time` (`executive_expire_time`),		
  KEY `process_expire_time` (`process_expire_time`),		
  KEY `operator_id` (`operator_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 巡检模板-巡检项映射表
DROP TABLE IF EXISTS `eh_equipment_inspection_template_item_map`;
CREATE TABLE `eh_equipment_inspection_template_item_map` (
    `id` BIGINT NOT NULL COMMENT 'id',
    `template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_template',
    `item_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_inspection_items',
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 巡检模板
DROP TABLE IF EXISTS `eh_equipment_inspection_templates`;
CREATE TABLE `eh_equipment_inspection_templates` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_events sharding group
-- entity profile for eh_events
--
DROP TABLE IF EXISTS `eh_event_profiles`;
CREATE TABLE `eh_event_profiles`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `app_id` BIGINT,
  `owner_id` BIGINT NOT NULL COMMENT 'owner event id',
  `item_name` VARCHAR(32),
  `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque json object, 1: entity',
  `item_value` TEXT,
  `target_type` VARCHAR(32),
  `target_id` BIGINT,

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`),
  KEY `i_eh_evt_prof_item`(`owner_id`, `item_name`),
  KEY `i_eh_evt_prof_owner`(`owner_id`),
  KEY `i_eh_evt_prof_itag1`(`integral_tag1`),
  KEY `i_eh_evt_prof_itag2`(`integral_tag2`),
  KEY `i_eh_evt_prof_stag1`(`string_tag1`),
  KEY `i_eh_evt_prof_stag2`(`string_tag2`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of event sharding group
--
DROP TABLE IF EXISTS `eh_event_roster`;
CREATE TABLE `eh_event_roster`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(36) NOT NULL,
  `event_id` BIGINT NOT NULL,
  `uid` BIGINT,
  `family_id` BIGINT,
  `adult_count` INTEGER NOT NULL DEFAULT 0,
  `child_count` INTEGER NOT NULL DEFAULT 0,
  `signup_flag` TINYINT NOT NULL DEFAULT 0,
  `signup_uid` BIGINT,
  `signup_time` DATETIME,
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_evt_roster_uuid`(`uuid`),
  UNIQUE KEY `u_eh_evt_roster_attendee`(`event_id`, `uid`),
  KEY `i_eh_evt_roster_signup_time`(`signup_time`),
  KEY `i_eh_evt_roster_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of event sharding group
--
DROP TABLE IF EXISTS `eh_event_ticket_groups`;
CREATE TABLE `eh_event_ticket_groups`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `event_id` BIGINT,
  `name` VARCHAR(32),
  `total_count` INTEGER,
  `allocated_count` INTEGER,
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_evt_tg_name`(`event_id`, `name`),
  KEY `i_eh_evt_tg_event_id`(`event_id`),
  KEY `i_eh_evt_tg_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of event sharding group
-- secondary resource objects (after eh_events)
--
DROP TABLE IF EXISTS `eh_event_tickets`;
CREATE TABLE `eh_event_tickets`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `event_id` BIGINT,
  `ticket_group_id` BIGINT,
  `ticket_number` VARCHAR(128),
  `uid` BIGINT,
  `family_id` BIGINT,
  `status` TINYINT COMMENT '0: free, 1: allocated',
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_evt_ticket_ticket`(`ticket_group_id`, `ticket_number`),
  KEY `i_eh_evt_ticket_event`(`event_id`),
  KEY `i_eh_evt_ticket_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- key table of event sharding group
-- old event subscription table and other event related profile items will be consolidated
-- in eh_event_profiles table
--
-- Only if there are queries from event to other entities, there is a need to have
-- associated field in eh_events table, otherwise, store associated references in
-- eh_event_profiles table, for example, associated groups, forums of the event
--
DROP TABLE IF EXISTS `eh_events`;
CREATE TABLE `eh_events`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `subject` VARCHAR(512),
  `description` TEXT,
  `location` TEXT,
  `contact_person` VARCHAR(128),
  `contact_number` VARCHAR(64),
  `start_time_ms` BIGINT,
  `start_time` DATETIME,
  `end_time_ms` BIGINT,
  `end_time` DATETIME,
  `max_attendee_count` INTEGER NOT NULL DEFAULT 0,
  `signup_attendee_count` INTEGER NOT NULL DEFAULT 0,
  `signup_family_count` INTEGER NOT NULL DEFAULT 0,
  `checkin_attendee_count` INTEGER NOT NULL DEFAULT 0,
  `checkin_family_count` INTEGER NOT NULL DEFAULT 0,
  `ticket_flag` TINYINT NOT NULL DEFAULT 0,
  `max_ticket_per_family` INTEGER NOT NULL DEFAULT 0,
  `ticket_group_id` BIGINT,
  `banner_id` BIGINT,
  `creator_uid` BIGINT,
  `creator_family_id` BIGINT,
  `order` INTEGER NOT NULL DEFAULT 0,
  `status` INTEGER COMMENT '0: inactive, 1: drafting, 2: active',
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',

  PRIMARY KEY (`id`),
  KEY `i_eh_evt_start_time_ms`(`start_time_ms`),
  KEY `i_eh_evt_end_time_ms`(`end_time_ms`),
  KEY `i_eh_evt_creator_uid`(`creator_uid`),
  KEY `i_eh_evt_create_time`(`create_time`),
  KEY `i_eh_evt_delete_time`(`delete_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 快递地址表，add by tt, 20170413
DROP TABLE IF EXISTS `eh_express_addresses`;
CREATE TABLE `eh_express_addresses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `user_name` VARCHAR(128),
  `organization_id` BIGINT,
  `organization_name` VARCHAR(128),
  `phone` VARCHAR(16),
  `province_id` BIGINT,
  `city_id` BIGINT,
  `county_id` BIGINT,
  `province` VARCHAR(64),
  `city` VARCHAR(64),
  `county` VARCHAR(64),
  `detail_address` VARCHAR(512),
  `default_flag` TINYINT COMMENT '0. false, 1 true',
  `category` TINYINT COMMENT '1. send address, 2. receive address',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 快递公司表，左邻配一套全局的，各园区在此选择，add by tt, 20170413
DROP TABLE IF EXISTS `eh_express_companies`;
CREATE TABLE `eh_express_companies` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parent id, the id of express company under zuolin',
  `name` VARCHAR(128) COMMENT 'the name of express company name',
  `logo` VARCHAR(512),
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_express_order_logs`;
CREATE TABLE `eh_express_order_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `order_id` BIGINT,
  `action` VARCHAR(64),
  `remark` TEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 快递订单表，add by tt, 20170413
DROP TABLE IF EXISTS `eh_express_orders`;
CREATE TABLE `eh_express_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `order_no` VARCHAR(64) COMMENT 'order number',
  `bill_no` VARCHAR(64) COMMENT 'bill number',
  `send_name` VARCHAR(128),
  `send_phone` VARCHAR(16),
  `send_organization` VARCHAR(128),
  `send_province` VARCHAR(64),
  `send_city` VARCHAR(64),
  `send_county` VARCHAR(64),
  `send_detail_address` VARCHAR(512),
  `receive_name` VARCHAR(128),
  `receive_phone` VARCHAR(16),
  `receive_organization` VARCHAR(128),
  `receive_province` VARCHAR(64),
  `receive_city` VARCHAR(64),
  `receive_county` VARCHAR(64),
  `receive_detail_address` VARCHAR(512),
  `service_address_id` BIGINT COMMENT 'service address id',
  `express_company_id` BIGINT COMMENT 'express company id',
  `send_type` TINYINT COMMENT '1. standard express',
  `send_mode` TINYINT COMMENT '1. self send',
  `pay_type` TINYINT COMMENT '1. cash',
  `pay_summary` DECIMAL(10,2) COMMENT 'pay money',
  `internal` VARCHAR(256) COMMENT 'internal things',
  `insured_price` DECIMAL(10,2) COMMENT 'insured price',
  `status` TINYINT NOT NULL COMMENT '1. waiting for pay, 2. paid, 3. printed, 4. cancelled',
  `paid_flag` TINYINT COMMENT 'whether the user has pushed the pay button, 0. false, 1 true',
  `print_time` DATETIME,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 快递查询历史表，add by tt, 20170413
DROP TABLE IF EXISTS `eh_express_query_histories`;
CREATE TABLE `eh_express_query_histories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `express_company_id` BIGINT COMMENT 'express company id',
  `bill_no` VARCHAR(64) COMMENT 'bill number',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 自寄服务地址表，add by tt, 20170413
DROP TABLE IF EXISTS `eh_express_service_addresses`;
CREATE TABLE `eh_express_service_addresses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `name` VARCHAR(128) COMMENT 'the name of express service address',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 快递员表，add by tt, 20170413
DROP TABLE IF EXISTS `eh_express_users`;
CREATE TABLE `eh_express_users` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `service_address_id` BIGINT,
  `express_company_id` BIGINT,
  `organization_id` BIGINT COMMENT 'the id of organization',
  `organization_member_id` BIGINT COMMENT 'the id of organization member',
  `user_id` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of address related sharding group
--
DROP TABLE IF EXISTS `eh_family_billing_accounts`;
CREATE TABLE `eh_family_billing_accounts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `account_number` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'the account number which use to identify the unique account',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address id',
  `balance` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `update_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_account_number`(`account_number`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of address related sharding group
-- the transaction history of paid the bills
--
DROP TABLE IF EXISTS `eh_family_billing_transactions`;
CREATE TABLE `eh_family_billing_transactions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `tx_sequence` VARCHAR(128) NOT NULL COMMENT 'the sequence binding the two records of a single transaction',
  `tx_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: online, 2: offline',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address id',
  `owner_account_id` BIGINT NOT NULL DEFAULT 0,
  `target_account_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: user, 2: family, 3: organization',
  `target_account_id` BIGINT NOT NULL DEFAULT 0,
  `order_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: bills in eh_organization_bills',
  `order_id` BIGINT NOT NULL DEFAULT 0,
  `charge_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid to target(negative) or received from target(positive)',
  `description` TEXT COMMENT 'the description for the transaction',
  `vendor` VARCHAR(128) DEFAULT '' COMMENT 'which third-part pay vendor is used',
  `pay_account` VARCHAR(128) DEFAULT '' COMMENT 'the pay account from third-part pay vendor',
  `result_code_scope` VARCHAR(128) DEFAULT '' COMMENT 'the scope of result code, defined in zuolin',
  `result_code_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'the code id occording to scope',
  `result_desc` VARCHAR(2048) DEFAULT '' COMMENT 'the description of the transaction',
  `operator_uid` BIGINT COMMENT 'the user is who paid the bill, including help others pay the bill',
  `paid_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: selfpay, 2: agent',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups(eh_families) sharding group
-- secondary resource objects (after eh_families)
--
DROP TABLE IF EXISTS `eh_family_followers`;
CREATE TABLE `eh_family_followers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_family` BIGINT NOT NULL,
  `follower_uid` BIGINT NOT NULL,
  `create_time` DATETIME COMMENT 'remove-deletion, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `i_fm_follower_follower` (`owner_family`,`follower_uid`),
  KEY `i_eh_fm_follower_owner` (`owner_family`),
  KEY `i_eh_fm_follower_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of user-related sharding group
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_feedbacks`;
CREATE TABLE `eh_feedbacks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `owner_uid` BIGINT DEFAULT 0,
  `contact` VARCHAR(128) DEFAULT '',
  `subject` VARCHAR(512),
  `content` TEXT,
  `create_time` DATETIME,
  `feedback_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: report, 2-complaint, 3-correct',
  `target_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: post, 2: address, 3: forum',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `content_category` BIGINT NOT NULL DEFAULT 0 COMMENT '0-其它、1-产品bug、2-产品改进、3-版本问题;11-敏感信息、12-版权问题、13-暴力色情、14-诈骗和虚假信息、15-骚扰；16-谣言、17-恶意营销、18-诱导分享；19-政治',
  `proof_resource_uri` VARCHAR(1024),
  `status` TINYINT DEFAULT 0 COMMENT '0: does not handle, 1: have handled',
  `verify_type` TINYINT COMMENT '0: verify false, 1: verify true',
  `handle_type` TINYINT COMMENT '0: none, 1 delete',
  `namespace_id` INTEGER,
  `handle_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_feedbacks_target_id` (`target_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_actions`;
CREATE TABLE `eh_flow_actions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `action_type` VARCHAR(64) NOT NULL COMMENT 'sms, message, tick_sms, tick_message, tracker, scripts',
  `belong_to` BIGINT NOT NULL,
  `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
  `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
  `action_step_type` VARCHAR(64) NOT NULL COMMENT 'step_none, step_timeout, step_enter, step_leave',
  `status` TINYINT NOT NULL COMMENT 'invalid, valid',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `render_text` VARCHAR(256) COMMENT 'the content for this message that have variables',

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_attachments`;
CREATE TABLE `eh_flow_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_buttons`;
CREATE TABLE `eh_flow_buttons` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `flow_main_id` BIGINT NOT NULL,
    `flow_version` INTEGER NOT NULL,
    `flow_node_id` BIGINT NOT NULL,
    `button_name` VARCHAR(64),
    `description` VARCHAR(1024),
    `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
    `flow_user_type` VARCHAR(64) COMMENT 'applier, processor',
    `goto_level` INTEGER NOT NULL DEFAULT 0,
    `goto_node_id` BIGINT NOT NULL DEFAULT 0,
    `need_subject` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not need subject for this step, 1: need subject for this step',
    `need_processor` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not need processor, 1: need only one processor',
    `remind_count` INTEGER NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL COMMENT 'record create time',
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: disabled, 2: enabled',

    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,
  `subject_required_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, subject required flag',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_cases`;
CREATE TABLE `eh_flow_cases` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),
  `module_name` VARCHAR(64),
  `applier_name` VARCHAR(64),
  `applier_phone` VARCHAR(64),
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `apply_user_id` BIGINT NOT NULL,
  `process_user_id` BIGINT NOT NULL DEFAULT 0,
  `refer_id` BIGINT NOT NULL DEFAULT 0,
  `refer_type` VARCHAR(64) NOT NULL,
  `current_node_id` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, initial, process, end',
  `reject_count` INTEGER NOT NULL DEFAULT 0,
  `reject_node_id` BIGINT NOT NULL DEFAULT 0,
  `step_count` BIGINT NOT NULL DEFAULT 0,
  `last_step_time` DATETIME NOT NULL COMMENT 'state change time',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `case_type` VARCHAR(64) COMMENT 'inner, outer etc',
  `content` TEXT,
  `evaluate_score` INTEGER NOT NULL DEFAULT 0,
  `title` VARCHAR(64),
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,
  `organization_id` BIGINT COMMENT 'the same as eh_flows organization_id',
  `applier_organization_id` BIGINT COMMENT 'applier current organization_id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_evaluate_items`;
CREATE TABLE `eh_flow_evaluate_items` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `input_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, input evaluate content flag',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_evaluates`;
CREATE TABLE `eh_flow_evaluates` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,

  `star` TINYINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `flow_node_id` BIGINT NOT NULL DEFAULT 0,
  `flow_case_id` BIGINT NOT NULL,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `project_id` BIGINT NOT NULL,
  `project_type` VARCHAR(64),
  `evaluate_item_id` BIGINT NOT NULL,
  `content` VARCHAR(1024) COMMENT 'evaluate content',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_event_logs`;
CREATE TABLE `eh_flow_event_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `flow_node_id` BIGINT NOT NULL DEFAULT 0,
  `flow_case_id` BIGINT NOT NULL DEFAULT 0,
  `flow_button_id` BIGINT NOT NULL DEFAULT 0,
  `flow_action_id` BIGINT NOT NULL DEFAULT 0,
  `flow_user_id` BIGINT NOT NULL DEFAULT 0,
  `flow_user_name` VARCHAR(64),
  `flow_selection_id` BIGINT NOT NULL DEFAULT 0,
  `subject_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the post id for this event',
  `step_count` BIGINT NOT NULL DEFAULT 0,
  `log_type` VARCHAR(64) NOT NULL COMMENT 'flow_step, button_click, action_result',
  `log_title` VARCHAR(64) COMMENT 'the title of this log',
  `log_content` TEXT,
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_forms`;
CREATE TABLE `eh_flow_forms` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `form_name` VARCHAR(64),
  `form_type` VARCHAR(64) COMMENT 'text, datetime, checkbox, radiobox, selection',
  `form_default` TEXT,
  `form_render` TEXT,
  `belong_to` BIGINT NOT NULL,
  `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_nodes`;
CREATE TABLE `eh_flow_nodes` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `node_name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(1024),
  `node_level` INTEGER NOT NULL,
  `auto_step_minute` INTEGER NOT NULL DEFAULT 0 COMMENT 'after hour, step next',
  `auto_step_type` VARCHAR(64) COMMENT 'ApproveStep, RejectStep, EndStep',
  `allow_applier_update` TINYINT NOT NULL DEFAULT 0 COMMENT 'allow applier update content',
  `allow_timeout_action` TINYINT NOT NULL DEFAULT 0 COMMENT '1: allow timeout action',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `params` VARCHAR(64) COMMENT 'the params from other module',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'invalid, valid',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- scripts for this module
DROP TABLE IF EXISTS `eh_flow_scripts`;
CREATE TABLE `eh_flow_scripts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `script_type` VARCHAR(64) NOT NULL COMMENT 'bean_id, prototype',
  `script_cls` VARCHAR(1024) NOT NULL COMMENT 'the class prototype in java',
  `flow_step_type` VARCHAR(64) COMMENT 'no_step, start_step, approve_step, reject_step, transfer_step, comment_step, end_step, notify_step',
  `step_type` VARCHAR(64) NOT NULL COMMENT 'step_none, step_timeout, step_enter, step_leave',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_stats`;
CREATE TABLE `eh_flow_stats` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,

  `node_level` INTEGER NOT NULL,
  `running_count` INTEGER NOT NULL,
  `enter_count` INTEGER NOT NULL,
  `leave_count` INTEGER NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_subjects`;
CREATE TABLE `eh_flow_subjects` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,

    `title` VARCHAR(64),
    `content` TEXT,

    `belong_to` BIGINT NOT NULL,
    `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button, flow',
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
    `create_time` DATETIME NOT NULL COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_timeouts`;
CREATE TABLE `eh_flow_timeouts` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `belong_to` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other flow object id',
    `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow, flow_node, flow_button, flow_action',
    `timeout_type` VARCHAR(64) NOT NULL COMMENT 'flow_step_timeout',
    `timeout_tick` DATETIME NOT NULL,
    `json` TEXT,
    `create_time` DATETIME NOT NULL,
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_user_selections`;
CREATE TABLE `eh_flow_user_selections` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `select_type` VARCHAR(64) NOT NULL COMMENT 'department, position, manager, variable',
  `source_id_a` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other user object id',
  `source_type_a` VARCHAR(64) COMMENT 'community, organization, user, variable',
  `source_id_b` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other user object id',
  `source_type_b` VARCHAR(64) COMMENT 'community, organization, user, variable',
  `belong_to` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to other flow object id',
  `belong_entity` VARCHAR(64) NOT NULL COMMENT 'flow, flow_node, flow_button, flow_action',
  `belong_type` VARCHAR(64) NOT NULL COMMENT 'flow_superviser, flow_node_processor, flow_node_applier, flow_button_clicker, flow_action_processor',
  `selection_name` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT 'invalid, valid',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  `params` VARCHAR(64),
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_variables`;
CREATE TABLE `eh_flow_variables` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `name` VARCHAR(64),
  `label` VARCHAR(64),
  `var_type` VARCHAR(64) NOT NULL COMMENT 'text, node_user',
  `script_type` VARCHAR(64) NOT NULL COMMENT 'bean_id, prototype',
  `script_cls` VARCHAR(1024) NOT NULL COMMENT 'the class prototype in java',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- eh_flows
DROP TABLE IF EXISTS `eh_flows`;
CREATE TABLE `eh_flows` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),

  `flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the real flow id for all copy, the first flow_main_id=0',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version',
  `flow_name` VARCHAR(64) NOT NULL COMMENT 'the name of flow',

  `status` TINYINT NOT NULL COMMENT 'invalid, config, running, pending, stop',
  `stop_time` DATETIME NOT NULL COMMENT 'last stop time',
  `run_time` DATETIME NOT NULL COMMENT 'last run time',
  `update_time` DATETIME NOT NULL COMMENT 'last run time',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',

  `start_node` BIGINT NOT NULL DEFAULT 0,
  `end_node` BIGINT NOT NULL DEFAULT 0,
  `last_node` BIGINT NOT NULL DEFAULT 0,

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `integral_tag4` BIGINT NOT NULL DEFAULT 0,
  `integral_tag5` BIGINT NOT NULL DEFAULT 0,
  `need_evaluate` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no evaluate, 1: need evaluate',
  `evaluate_start` BIGINT NOT NULL DEFAULT 0,
  `evaluate_end` BIGINT NOT NULL DEFAULT 0,
  `evaluate_step` VARCHAR(64) COMMENT 'NoStep, ApproveStep',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of forum post sharding group
--
DROP TABLE IF EXISTS `eh_forum_assigned_scopes`;
CREATE TABLE `eh_forum_assigned_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner post id',
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city',
  `scope_id` BIGINT,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_scope_owner_code_id` (`owner_id`,`scope_code`,`scope_id`),
  KEY `i_eh_post_scope_owner_id` (`owner_id`),
  KEY `i_eh_post_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_forum_assigned_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_forum_posts` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of forum post sharding group
--
DROP TABLE IF EXISTS `eh_forum_attachments`;
CREATE TABLE `eh_forum_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `post_id` BIGINT NOT NULL,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `orignial_path` VARCHAR(1024) COMMENT 'attachment file path in 2.8 version, keep it for migration',

  PRIMARY KEY (`id`),
  KEY `i_eh_frmatt_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- key table of forum post sharding group
-- forum posts form its own sharding group, due to nature of timely content
-- delete column `dislike_count` BIGINT NOT NULL DEFAULT 0,
--
DROP TABLE IF EXISTS `eh_forum_posts`;
CREATE TABLE `eh_forum_posts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `app_id` BIGINT NOT NULL DEFAULT 2 COMMENT 'default to forum application itself',
  `forum_id` BIGINT NOT NULL COMMENT 'forum that it belongs',
  `parent_post_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'replied post id',
  `creator_uid` BIGINT NOT NULL COMMENT 'post creator uid',
  `creator_tag` VARCHAR(128) COMMENT 'post creator tag',
  `target_tag` VARCHAR(128) COMMENT 'post target tag',

  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(64),

  `visible_region_type` TINYINT COMMENT 'define the visible region type',
  `visible_region_id` BIGINT COMMENT 'visible region id',
  `visible_region_path` VARCHAR(128) COMMENT 'visible region path',

  `category_id` BIGINT,
  `category_path` VARCHAR(128),

  `modify_seq` BIGINT NOT NULL,
  `child_count` BIGINT NOT NULL DEFAULT 0,
  `forward_count` BIGINT NOT NULL DEFAULT 0,
  `like_count` BIGINT NOT NULL DEFAULT 0,
  `view_count` BIGINT NOT NULL DEFAULT 0,

  `subject` VARCHAR(512),
  `content_type` VARCHAR(32) COMMENT 'object content type',
  `content` TEXT COMMENT 'content data, depends on value of content_type',
  `content_abstract` TEXT COMMENT 'abstract of content data',

  `embedded_app_id` BIGINT,
  `embedded_id` BIGINT,
  `embedded_json` LONGTEXT,
  `embedded_version` INTEGER NOT NULL DEFAULT 1,

  `integral_tag1` BIGINT COMMENT 'user for action category id',
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128) COMMENT 'user for action category path',
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  `private_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
  `assigned_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the flag indicate the topic is recommanded, 0: none, 1: manual recommand',
  `floor_number` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `update_time` DATETIME,
  `create_time` DATETIME NOT NULL,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `tag` VARCHAR(32),
  `start_time` DATETIME COMMENT 'publish start time',
  `end_time` DATETIME COMMENT 'publish end time',
  `official_flag` TINYINT DEFAULT 0 COMMENT 'whether it is an official activity, 0 not, 1 yes',
  `media_display_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'whether display image',
  `max_quantity` INTEGER COMMENT 'max person quantity',
  `activity_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity category id',		
  `activity_content_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity content category id',
  `parent_comment_id` BIGINT COMMENT 'parent comment id',
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of forum post sharding group
--
DROP TABLE IF EXISTS `eh_forum_visible_scopes`;
CREATE TABLE `eh_forum_visible_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner post id',
  `scope_code` TINYINT,
  `scope_id` BIGINT,

  PRIMARY KEY (`id`),
  KEY `fk_eh_post_scope_owner` (`owner_id`),
  KEY `i_eh_post_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_forum_visible_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_forum_posts` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- key table for forum sharding group
--
DROP TABLE IF EXISTS `eh_forums`;
CREATE TABLE `eh_forums` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_id` BIGINT NOT NULL DEFAULT 2 COMMENT 'default to forum application itself',
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT,
  `name` VARCHAR(64) NOT NULL,
  `description` TEXT,
  `post_count` BIGINT NOT NULL DEFAULT 0,
  `modify_seq` BIGINT NOT NULL,
  `update_time` DATETIME NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid`(`uuid`),
  KEY `i_eh_frm_namespace`(`namespace_id`),
  KEY `i_eh_frm_owner`(`owner_type`, `owner_id`),
  KEY `i_eh_frm_post_count` (`post_count`),
  KEY `i_eh_frm_modify_seq` (`modify_seq`),
  KEY `i_eh_frm_update_time` (`update_time`),
  KEY `i_eh_frm_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- the values of form from request
DROP TABLE IF EXISTS `eh_general_approval_vals`;
CREATE TABLE `eh_general_approval_vals` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `flow_case_id` BIGINT DEFAULT 0,
  `request_id` BIGINT ,
  `approval_id` BIGINT ,
  `form_origin_id` BIGINT ,
  `form_version` BIGINT ,
  `field_name` VARCHAR(128),
  `field_type` VARCHAR(128),
  `data_source_type` VARCHAR(128),
  `val_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: invalid, 1: request_root, 2: request_item',

  `field_str1` VARCHAR(128),
  `field_str2` VARCHAR(128),
  `field_str3` TEXT,
  `field_int1` BIGINT NOT NULL DEFAULT 0,
  `field_int2` BIGINT NOT NULL DEFAULT 0,
  `field_int3` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_general_approvals`;
CREATE TABLE `eh_general_approvals` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT DEFAULT 0 COMMENT 'the module id',
  `module_type` VARCHAR(64)  ,
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),

  `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the original form',
  `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'the current using version',
  `support_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'APP:0, WEB:1, APP_WEB: 2',
  `approval_name` VARCHAR(128) NOT NULL,

  `status` TINYINT NOT NULL COMMENT 'invalid, config, running',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_general_form_vals`;
CREATE TABLE `eh_general_form_vals` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT COMMENT 'the module id',
  `module_type` VARCHAR(64),
  `source_id` BIGINT NOT NULL,
  `source_type` VARCHAR(64) NOT NULL,
  `form_origin_id` BIGINT,
  `form_version` BIGINT,
  `field_name` VARCHAR(128),
  `field_type` VARCHAR(128),
  `field_value` TEXT,
  `create_time` DATETIME COMMENT 'record create time',
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- general forms support
-- 表单
DROP TABLE IF EXISTS `eh_general_forms`;
CREATE TABLE `eh_general_forms` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT COMMENT 'the module id',
  `module_type` VARCHAR(64) ,

  `form_name` VARCHAR(64) NOT NULL,
  `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the original form',
  `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'the current using version',
  `template_type` VARCHAR(128) NOT NULL COMMENT 'the type of template text',
  `template_text` TEXT COMMENT 'json 存放表单字段',

  `status` TINYINT NOT NULL  COMMENT 'invalid, config, running',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',

  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_group_member_logs`;
CREATE TABLE `eh_group_member_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `group_member_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'the same as group member status',
  `creator_uid` BIGINT,
  `process_message` TEXT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- member of eh_groups sharding group
--
DROP TABLE IF EXISTS `eh_group_members`;
CREATE TABLE `eh_group_members` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `group_id` BIGINT NOT NULL,
  `member_type` VARCHAR(32) NOT NULL COMMENT 'member object type, for example, type could be User, Group, etc',
  `member_id` BIGINT,
  `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'Default to ResourceUser role',
  `member_avatar` VARCHAR(128) COMMENT 'avatar image identifier in storage sub-system',
  `member_nick_name` VARCHAR(128) COMMENT 'member nick name within the group',
  `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `operator_uid` BIGINT COMMENT 'redundant auditing info',
  `process_code` TINYINT,
  `process_details` TEXT,
  `proof_resource_uri` VARCHAR(1024),
  `approve_time` DATETIME COMMENT 'redundant auditing info',
  `requestor_comment` TEXT,
  `operation_type` TINYINT COMMENT '1: request to join, 2: invite to join',
  `inviter_uid` BIGINT COMMENT 'record inviter user id',
  `invite_time` DATETIME COMMENT 'the time the member is invited',
  `update_time` DATETIME NOT NULL,

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid`(`uuid`),
  UNIQUE KEY `u_eh_grp_member` (`group_id`, `member_type`, `member_id`),
  KEY `i_eh_grp_member_group_id` (`group_id`),
  KEY `i_eh_grp_member_member` (`member_type`, `member_id`),
  KEY `i_eh_grp_member_create_time` (`create_time`),
  KEY `i_eh_grp_member_approve_time` (`approve_time`),
  KEY `i_eh_gprof_itag1`(`integral_tag1`),
  KEY `i_eh_gprof_itag2`(`integral_tag2`),
  KEY `i_eh_gprof_stag1`(`string_tag1`),
  KEY `i_eh_gprof_stag2`(`string_tag2`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups sharding group
--
DROP TABLE IF EXISTS `eh_group_op_requests`;
CREATE TABLE `eh_group_op_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `group_id` BIGINT,
  `requestor_uid` BIGINT,
  `requestor_comment` TEXT,
  `target_uid` BIGINT,
  `operation_type` TINYINT COMMENT '1: request for admin role, 2: invite to become admin',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: requesting, 2: accepted',
  `operator_uid` BIGINT,
  `process_message` TEXT,
  `create_time` DATETIME,
  `process_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `i_eh_grp_op_group` (`group_id`),
  KEY `i_eh_grp_op_requestor` (`requestor_uid`),
  KEY `i_eh_grp_op_create_time` (`create_time`),
  KEY `i_eh_grp_op_process_time` (`process_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups sharding group
--
DROP TABLE IF EXISTS `eh_group_profiles`;
CREATE TABLE `eh_group_profiles` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `app_id` BIGINT,
  `owner_id` BIGINT NOT NULL COMMENT 'owner group id',
  `item_name` VARCHAR(32),
  `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque json object, 1: entity',
  `item_value` TEXT,
  `target_type` VARCHAR(32),
  `target_id` BIGINT,

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`),
  KEY `i_eh_gprof_item` (`app_id`,`owner_id`,`item_name`),
  KEY `i_eh_gprof_owner` (`owner_id`),
  KEY `i_eh_gprof_itag1` (`integral_tag1`),
  KEY `i_eh_gprof_itag2` (`integral_tag2`),
  KEY `i_eh_gprof_stag1` (`string_tag1`),
  KEY `i_eh_gprof_stag2` (`string_tag2`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 设置圈（俱乐部）参数，add by tt, 20161028
DROP TABLE IF EXISTS `eh_group_settings`;
CREATE TABLE `eh_group_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `create_flag` TINYINT COMMENT 'whether allow create club',
  `verify_flag` TINYINT COMMENT 'whether need verify',
  `member_post_flag` TINYINT COMMENT 'whether allow members create post',
  `member_comment_flag` TINYINT COMMENT 'whether allow members comment on the post',
  `admin_broadcast_flag` TINYINT COMMENT 'whether allow admin broadcast',
  `broadcast_count` INTEGER COMMENT 'how many broadcasts can be created per day',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'news creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_groups sharding group
--
DROP TABLE IF EXISTS `eh_group_visible_scopes`;
CREATE TABLE `eh_group_visible_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner group id',
  `scope_code` TINYINT,
  `scope_id` BIGINT,

  PRIMARY KEY (`id`),
  KEY `fk_eh_grp_scope_owner` (`owner_id`),
  KEY `i_eh_grp_scope_target` (`scope_code`,`scope_id`),
  CONSTRAINT `eh_group_visible_scopes_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `eh_groups` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- key table of grouping related sharding group
-- Usually there is no need for group object to carry information for other applications, therefore there is
-- not an app_id field
--
-- Group custom fields are used by inherited classes
--
DROP TABLE IF EXISTS `eh_groups`;
CREATE TABLE `eh_groups` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL,
  `display_name` VARCHAR(64),
  `avatar` VARCHAR(256),
  `description` TEXT,
  `creator_uid` BIGINT NOT NULL,
  `private_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
  `join_policy` INTEGER NOT NULL DEFAULT 0 COMMENT '0: free join(public group), 1: should be approved by operator/owner, 2: invite only',
  `discriminator` VARCHAR(32),

  `visibility_scope` TINYINT COMMENT 'define the group visibiliy region',
  `visibility_scope_id` BIGINT COMMENT 'region information, could be an id in eh_regions table or an id in eh_communities',
  `category_id` BIGINT COMMENT 'group category',
  `category_path` VARCHAR(128),

  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
  `member_count` BIGINT NOT NULL DEFAULT 0,
  `share_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'How many times the group card is shared',
  `post_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: admin only',
  `tag` VARCHAR(256),

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  `update_time` DATETIME NOT NULL,
  `create_time` DATETIME NOT NULL,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, multi-purpose base entity',
  `visible_region_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of region where the group belong to',
  `visible_region_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of region where the group belong to',
  `approval_status` TINYINT COMMENT 'approval status',
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  KEY `i_eh_group_creator` (`creator_uid`),
  KEY `i_eh_group_create_time` (`create_time`),
  KEY `i_eh_group_delete_time` (`delete_time`),
  KEY `i_eh_group_itag1` (`integral_tag1`),
  KEY `i_eh_group_itag2` (`integral_tag2`),
  KEY `i_eh_group_stag1` (`string_tag1`),
  KEY `i_eh_group_stag2` (`string_tag2`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_hot_tags`;
CREATE TABLE `eh_hot_tags` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'service type, eg: activity',
  `name` VARCHAR(128) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `create_uid` BIGINT,
  `delete_time` DATETIME,
  `delete_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_import_file_tasks`;
CREATE TABLE `eh_import_file_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` VARCHAR(64) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL,
  `result` LONGTEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- item 类别 by sfyan 20161025
DROP TABLE IF EXISTS `eh_item_service_categries`;
CREATE TABLE `eh_item_service_categries` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL COMMENT 'service categry name',
  `icon_uri` VARCHAR(1024) COMMENT 'service categry icon uri',
  `order` INTEGER COMMENT 'order ',
  `align` TINYINT DEFAULT 0 COMMENT '0: left, 1: center',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
  `namespace_id` INTEGER,
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_journal_configs`;
CREATE TABLE `eh_journal_configs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `description` TEXT COMMENT 'description',
  `poster_path` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT 'poster_path',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_journals`;
CREATE TABLE `eh_journals` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `title` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'title',
  `content_type` TINYINT NOT NULL DEFAULT 1 COMMENT ' 1:link ',
  `content` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'link',
  `cover_uri` VARCHAR(1024) COMMENT 'cover file uri',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive,1:active ',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- App启动广告   add by xq.tian  2016/11/28
--
DROP TABLE IF EXISTS `eh_launch_advertisements`;
CREATE TABLE `eh_launch_advertisements` (
  `id` BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT '1: IMAGE, 2: VIDEO',
  `content_uri` VARCHAR(1024) COMMENT 'advertisement image/gif/video uri',
  `times_per_day` INTEGER DEFAULT 0 COMMENT 'The maximum number of times to display per day',
  `display_interval` INTEGER DEFAULT 0 COMMENT 'Minimum display time interval, ',
  `duration_time` INTEGER COMMENT 'duration time',
  `skip_flag` TINYINT COMMENT '0: can not skip, 1: skip',
  `action_type` TINYINT COMMENT '0: can not click, 1: click',
  `action_data` TEXT COMMENT 'If allow click, the jumped url',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_launch_pad_items`;
CREATE TABLE `eh_launch_pad_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_id` BIGINT,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` BIGINT,
  `item_location` VARCHAR(2048),
  `item_group` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the type to filter item when querying: Default、GovAgencies、Bizs、GaActions',
  `item_name` VARCHAR(32),
  `item_label` VARCHAR(64),
  `icon_uri` VARCHAR(1024),
  `item_width` INTEGER NOT NULL DEFAULT 1,
  `item_height` INTEGER NOT NULL DEFAULT 1,
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: default, 1: override, 2: revert',
  `min_version` BIGINT NOT NULL DEFAULT 1 COMMENT 'the min version of the item, it will be not supported if current version is less than this',
  `display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'default display on the pad, 0: hide, 1:display',
  `display_layout` VARCHAR(128) DEFAULT 1 COMMENT 'how many grids it takes at the layout, format: 2x3',
  `bgcolor` INTEGER NOT NULL DEFAULT 0,
  `tag` VARCHAR(1024),
  `target_type` VARCHAR(32),
  `target_id` VARCHAR(64) COMMENT 'the entity id linked back to the orginal resource',
  `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'whether the item can be deleted from desk, 0: no, 1: yes',
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',
  `scale_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 不需要, 1: 需要',
  `service_categry_id` BIGINT COMMENT 'service categry id',
  `selected_icon_uri` VARCHAR(1024),
  `more_order` INTEGER NOT NULL DEFAULT 0,
  `alias_icon_uri` VARCHAR(1024) COMMENT '原有icon_uri有圆形、方形等，展现风格不一致。应对这样的场景增加alias_icon_uri，存储圆形默认图片。',
  
  PRIMARY KEY (`id`),
  KEY `i_eh_scoped_cfg_combo` (`namespace_id`,`app_id`,`scope_code`,`scope_id`,`item_name`),
  KEY `i_eh_scoped_cfg_order` (`default_order`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_launch_pad_layouts`;
CREATE TABLE `eh_launch_pad_layouts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(32),
  `layout_json` TEXT,
  `version_code` BIGINT NOT NULL DEFAULT 0 COMMENT 'the current version code',
  `min_version_code` BIGINT NOT NULL DEFAULT 0 COMMENT 'the minmum version code which is compatible',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` DATETIME,
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user',
  `scope_id` BIGINT DEFAULT 0,
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: default, 1: override, 2: revert 3:customized',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_lease_configs`;
CREATE TABLE `eh_lease_configs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `rent_amount_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `issuing_lease_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `issuer_manage_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `park_indroduce_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `renew_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `area_search_flag` TINYINT NOT NULL DEFAULT 0 COMMENT ' 1: support, 0: not ',
  `display_name_str` VARCHAR(128),
  `display_order_str` VARCHAR(128),
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_lease_configs2`;

CREATE TABLE `eh_lease_configs2` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',
  `config_name` VARCHAR(128),
  `config_value` VARCHAR(128),
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_lease_form_requests`;
CREATE TABLE `eh_lease_form_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `source_id` BIGINT NOT NULL,
  `source_type` VARCHAR(64) NOT NULL,
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_lease_issuer_addresses`;
CREATE TABLE `eh_lease_issuer_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `lease_issuer_id` BIGINT NOT NULL COMMENT 'eh_enterprise_op_requests id',
  `building_id` BIGINT COMMENT 'building id ',
  `address_id` BIGINT COMMENT 'address id ',
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_lease_issuers`;
CREATE TABLE `eh_lease_issuers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `issuer_contact` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'For rent',
  `issuer_name` VARCHAR(128),
  `enterprise_id` BIGINT COMMENT 'enterprise id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 2: active',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_lease_promotion_attachments`;
CREATE TABLE `eh_lease_promotion_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `lease_id` BIGINT NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_lease_promotions` ;
CREATE TABLE `eh_lease_promotions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `rent_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'For rent',
  `poster_uri` VARCHAR(128),
  `subject` VARCHAR(512),
  `rent_areas` VARCHAR(128),
  `description` TEXT,
  `create_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `building_id` BIGINT NOT NULL DEFAULT 0,
  `rent_position` VARCHAR(128) COMMENT 'rent position',
  `contacts` VARCHAR(128),
  `contact_phone` VARCHAR(128),
  `enter_time` DATETIME COMMENT 'enter time',
  `namespace_type` VARCHAR(128),
  `namespace_token` VARCHAR(256),
  `enter_time_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `address_id` BIGINT NOT NULL DEFAULT 0,
  `orientation` VARCHAR(128),
  `rent_amount` DECIMAL(10,2),
  `issuer_type` VARCHAR(128) COMMENT '1: organization 2: normal_user',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `address` VARCHAR(512),
  `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form',
  `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field',
  `default_order` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_links`;
CREATE TABLE `eh_links` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `source_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the source type who refers the link, 0: none, 1: post',
  `source_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the source id depends on source type',
  `title` VARCHAR(1024),
  `author` VARCHAR(128),
  `cover_uri` VARCHAR(1024) COMMENT 'cover image uri',
  `content_type` VARCHAR(32) COMMENT 'object content type: link url、rich text',
  `content` LONGTEXT COMMENT 'content data, depends on value of content_type',
  `content_abstract` TEXT COMMENT 'abstract of content data',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `rich_content` LONGTEXT COMMENT 'rich_content',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_locale_strings`;
CREATE TABLE `eh_locale_strings`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `scope` VARCHAR(64),
  `code` VARCHAR(64),
  `locale` VARCHAR(16),
  `text` TEXT,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_lstr_identifier`(`scope`, `code`, `locale`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_locale_templates`;
CREATE TABLE `eh_locale_templates`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `scope` VARCHAR(64),
  `code` INTEGER,
  `locale` VARCHAR(16),
  `description` VARCHAR(2048),
  `text` TEXT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_template_identifier`(`namespace_id`, `scope`, `code`, `locale`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_message_boxs`;
CREATE TABLE `eh_message_boxs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `box_key` VARCHAR(128) COMMENT 'message box unique identifier',
  `message_id` BIGINT NOT NULL COMMENT 'foreign key to message record',
  `message_seq` BIGINT NOT NULL COMMENT 'message sequence id that identifies the message',
  `box_seq` BIGINT NOT NULL COMMENT 'sequence of the message inside the box',
  `create_time` DATETIME NOT NULL COMMENT 'time that message goes into the box, taken from create time of the message',

  PRIMARY KEY (`id`),
  KEY `fk_eh_mbx_msg_id` (`message_id`),
  KEY `i_eh_mbx_namespace` (`namespace_id`),
  KEY `i_eh_mbx_msg_seq` (`message_seq`),
  KEY `i_eh_mbx_box_seq` (`box_seq`),
  KEY `i_eh_mbx_create_time` (`create_time`),
  CONSTRAINT `eh_message_boxs_ibfk_1` FOREIGN KEY (`message_id`) REFERENCES `eh_messages` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_messages`;
CREATE TABLE `eh_messages` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_id` BIGINT NOT NULL DEFAULT 1 COMMENT 'default to messaging app itself',
  `message_seq` BIGINT NOT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` BIGINT NOT NULL,
  `context_type` VARCHAR(32),
  `context_token` VARCHAR(32),
  `channel_type` VARCHAR(32),
  `channel_token` VARCHAR(32),
  `message_text` MEDIUMTEXT COMMENT 'message content',
  `meta_app_id` BIGINT COMMENT 'app that is in charge of message content and meta intepretation',
  `message_meta` MEDIUMTEXT COMMENT 'JSON encoded message meta info, in format of string to string map',
  `encode_version` INTEGER NOT NULL DEFAULT 1 COMMENT 'message meta encode version',
  `sender_tag` VARCHAR(32) COMMENT 'sender generated tag',
  `create_time` DATETIME NOT NULL COMMENT 'message creation time',

  PRIMARY KEY (`id`),
  KEY `i_eh_msgs_namespace` (`namespace_id`),
  KEY `i_eh_msgs_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_namespace_details`;
CREATE TABLE `eh_namespace_details` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `resource_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the type of resource in the namespace, community_residential, community_commercial, community_mix',
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_namespace_id` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
-- the resources related to the namespace
--
DROP TABLE IF EXISTS `eh_namespace_resources`;
CREATE TABLE `eh_namespace_resources` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `resource_type` VARCHAR(128) COMMENT 'COMMUNITY',
  `resource_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_namespace_resource_id`(`namespace_id`, `resource_type`, `resource_id`),
  KEY `i_eh_resource_id`(`resource_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_namespaces`;
CREATE TABLE `eh_namespaces` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` VARCHAR(64),

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_ns_name`(`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_communities partition
--
DROP TABLE IF EXISTS `eh_nearby_community_map`;
CREATE TABLE `eh_nearby_community_map` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `nearby_community_id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_community_relation`(`community_id`, `nearby_community_id`),
  KEY `u_eh_community_id`(`community_id`),
  KEY `u_eh_nearby_community_id`(`nearby_community_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_news`;
CREATE TABLE `eh_news` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(32) COMMENT 'ORGANIZATION',
  `owner_id` BIGINT DEFAULT 0 COMMENT 'organization_id',
  `title` VARCHAR(1024) COMMENT 'title',
  `author` VARCHAR(128) COMMENT 'author',
  `cover_uri` VARCHAR(1024) COMMENT 'cover image uri',
  `content_type` VARCHAR(32) COMMENT 'object content type: link url、rich text',
  `content` LONGTEXT COMMENT 'content data, depends on value of content_type',
  `content_abstract` TEXT COMMENT 'abstract of content data',
  `source_desc` VARCHAR(128) COMMENT 'where the news comes from',
  `source_url` VARCHAR(256) COMMENT 'the url of source',
  `child_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'comment count',
  `forward_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'forward count',
  `like_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'like count',
  `view_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'view count',
  `publish_time` DATETIME COMMENT 'the time when the news was created, now equals to create_time',
  `top_index` BIGINT NOT NULL DEFAULT 0 COMMENT 'if has this value, go to first, order from big to small',
  `top_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether it is top',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'news creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter uid',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'category id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_news_attachments`;
CREATE TABLE `eh_news_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 园区快讯的建表语句 by wh 2016-9-21
--
DROP TABLE IF EXISTS `eh_news_categories`;
CREATE TABLE `eh_news_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `logo_uri` VARCHAR(1024) COMMENT 'default cover uri',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_news_comment`;
CREATE TABLE `eh_news_comment` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g news_id',
  `content_type` VARCHAR(32) COMMENT 'object content type',
  `content` TEXT COMMENT 'content data, depends on value of content_type',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME,
  `deleter_uid` BIGINT COMMENT 'deleter uid',
  `delete_time` DATETIME COMMENT 'delete time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- oauth2client 1.0   add by xq.tian 2017/03/09
--
-- oauth2 client AccessToken
--
DROP TABLE IF EXISTS `eh_oauth2_client_tokens`;
CREATE TABLE `eh_oauth2_client_tokens` (
  `id` BIGINT NOT NULL,
  `token_string` VARCHAR(128) NOT NULL COMMENT 'token string issued to requestor',
  `vendor` VARCHAR(32) NOT NULL COMMENT 'OAuth2 server name',
  `grantor_uid` BIGINT NOT NULL COMMENT 'eh_users id',
  `expiration_time` DATETIME NOT NULL COMMENT 'a successful acquire of access token by the code should immediately expires it',
  `scope` VARCHAR(256) COMMENT 'space-delimited scope tokens per RFC 6749',
  `type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: access token, 1: refresh token',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_oauth2_codes`;
CREATE TABLE `eh_oauth2_codes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `code` VARCHAR(128) NOT NULL COMMENT 'authorization code issued to requestor',
  `app_id` BIGINT NOT NULL COMMENT 'corresponding to client_id in OAuth2',
  `grantor_uid` BIGINT NOT NULL COMMENT 'user who authorizes the grant',
  `expiration_time` DATETIME NOT NULL COMMENT 'a successful acquire of access token by the code should immediately expires it',
  `redirect_uri` VARCHAR(256) COMMENT 'original redirect URI in OAuth2 authorization request',
  `scope` VARCHAR(256) COMMENT 'space-delimited scope tokens per RFC 6749',

  `create_time` DATETIME,
  `modify_time` DATETIME,

  PRIMARY KEY(`id`),
  UNIQUE KEY `u_eh_ocode_code`(`code`),
  KEY `i_eh_ocode_expiration_time`(`expiration_time`),
  KEY `i_eh_ocode_create_time`(`create_time`),
  KEY `i_eh_ocode_modify_time`(`modify_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_oauth2_servers`;
CREATE TABLE `eh_oauth2_servers` (
  `id` BIGINT NOT NULL,
  `vendor` VARCHAR(32) NOT NULL COMMENT 'OAuth2 server name',
  `client_id` VARCHAR(128) NOT NULL COMMENT 'third part provided',
  `client_secret` VARCHAR(128) NOT NULL COMMENT 'third part provided',
  `redirect_uri` VARCHAR(1024) NOT NULL COMMENT 'authorize success redirect to this url',
  `response_type` VARCHAR(128) NOT NULL COMMENT 'e.g: code',
  `grant_type` VARCHAR(128) NOT NULL COMMENT 'e.g: authorization_code',
  `state` VARCHAR(128) NOT NULL COMMENT 'e.g: OAuth server will response this filed original',
  `scope` VARCHAR(256) COMMENT 'space-delimited scope tokens per RFC 6749',
  `authorize_url` VARCHAR(1024) COMMENT 'OAuth server provided authorize url',
  `token_url` VARCHAR(1024) COMMENT 'OAuth server provided get token url',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_oauth2_tokens`;
CREATE TABLE `eh_oauth2_tokens` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `token_string` VARCHAR(128) NOT NULL COMMENT 'token string issued to requestor',
  `app_id` BIGINT NOT NULL COMMENT 'corresponding to client_id in OAuth2',
  `grantor_uid` BIGINT NOT NULL COMMENT 'user who authorizes the grant',
  `expiration_time` DATETIME NOT NULL COMMENT 'a successful acquire of access token by the code should immediately expires it',
  `scope` VARCHAR(256) COMMENT 'space-delimited scope tokens per RFC 6749',

  `type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: access token, 1: refresh token',

  `create_time` DATETIME,

  PRIMARY KEY(`id`),
  UNIQUE KEY `u_eh_otoken_token_string`(`token_string`),
  KEY `i_eh_otoken_expiration_time`(`expiration_time`),
  KEY `i_eh_otoken_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 工位预定  附件-banner图表
--
DROP TABLE IF EXISTS `eh_office_cubicle_attachments`;
CREATE TABLE `eh_office_cubicle_attachments` (
  `id` BIGINT  NOT NULL DEFAULT 0  COMMENT 'id',
  `owner_id` BIGINT  COMMENT '工位空间id',
  `content_type` VARCHAR(32)  COMMENT '内容类型',
  `content_uri` VARCHAR(1024)  COMMENT 'uri',
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 工位预定 出租空间表
--
DROP TABLE IF EXISTS `eh_office_cubicle_categories`;
CREATE TABLE `eh_office_cubicle_categories` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER,
  `space_id` BIGINT  COMMENT '工位空间id',
  `rent_type` TINYINT  COMMENT '租赁类别:1-开放式（默认space_type 1）,2-办公室',
  `space_type` TINYINT  COMMENT '空间类别:1-工位,2-面积',
  `space_size` INTEGER  COMMENT '工位数或面积数',
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 工位预定 订单表
--
DROP TABLE IF EXISTS `eh_office_cubicle_orders`;
CREATE TABLE `eh_office_cubicle_orders` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER,
  `space_id` BIGINT COMMENT '工位空间id',
  `space_name` VARCHAR(128) COMMENT '工位空间名称',
  `province_id` BIGINT COMMENT '省份id',
  `province_name` VARCHAR(100) COMMENT '省份名称',
  `city_id` BIGINT COMMENT '城市id',
  `city_name` VARCHAR(128) COMMENT '城市名称',
  `cover_uri` VARCHAR(1024) COMMENT '封面图片',
  `address` VARCHAR(1024) COMMENT '地址',
  `longitude` DOUBLE COMMENT '经度',
  `latitude` DOUBLE COMMENT '纬度',
  `geohash` VARCHAR(32),
  `contact_phone` VARCHAR(32) COMMENT '咨询电话',
  `manager_uid` BIGINT COMMENT '负责人uid',
  `description` TEXT COMMENT '详情-html片',
  `rent_type` TINYINT COMMENT '租赁类别:1-开放式（默认space_type 1）,2-办公室',
  `space_type` TINYINT COMMENT '空间类别:1-工位,2-面积',
  `space_size` VARCHAR(32) COMMENT '工位数或面积数',
  `status` TINYINT COMMENT '状态:2-用户可见,0-用户不可见',
  `order_type` TINYINT COMMENT '预定类别：1-参观 2-预定',
  `reserver_uid` BIGINT COMMENT '预订人uid',
  `reserve_time` DATETIME COMMENT '预定时间',
  `reserver_name` VARCHAR(64) COMMENT '预订人姓名',
  `reserve_contact_token` VARCHAR(32) COMMENT '预定联系方式',
  `reserve_enterprise` VARCHAR(512) COMMENT '预订人公司',
  `create_time` DATETIME,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 工位预定空间表
--
DROP TABLE IF EXISTS `eh_office_cubicle_spaces`;
CREATE TABLE `eh_office_cubicle_spaces` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER,
  `name` VARCHAR(128) COMMENT '工位空间名称',
  `province_id` BIGINT COMMENT '省份id',
  `province_name` VARCHAR(128) COMMENT '省份名称',
  `city_id` BIGINT COMMENT '城市id',
  `city_name` VARCHAR(128) COMMENT '城市名称',
  `cover_uri` VARCHAR(1024) COMMENT '封面图片',
  `address` VARCHAR(1024) COMMENT '地址',
  `longitude` DOUBLE COMMENT '经度',
  `latitude` DOUBLE COMMENT '纬度',
  `geohash` VARCHAR(32),
  `contact_phone` VARCHAR(32) COMMENT '咨询电话',
  `manager_uid` BIGINT COMMENT '负责人uid',
  `description` TEXT COMMENT '详情-html片',
  `status` TINYINT COMMENT '状态：2-正常 ,0-不可用',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_op_promotion_activities`;
CREATE TABLE `eh_op_promotion_activities`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `title` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the title of the activity',
  `description` TEXT,
  `start_time` DATETIME,
  `end_time` DATETIME,
  `policy_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: registerd, 2: reach amount of consumption in zuolin-biz',
  `policy_data` TEXT COMMENT 'json format, the parameters which help executing the policy',
  `icon_uri` VARCHAR(1024),
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of what to do, according to document',
  `action_data` TEXT COMMENT 'the parameters depend on ation type, json format',
  `push_message` VARCHAR(1024) COMMENT 'the message need to push',
  `push_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'how many user received the push',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `process_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: on process, 2: finished, 3: close',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `integral_tag1` BIGINT DEFAULT 0,
  `integral_tag2` BIGINT DEFAULT 0,
  `integral_tag3` BIGINT DEFAULT 0,
  `integral_tag4` BIGINT DEFAULT 0,
  `integral_tag5` BIGINT DEFAULT 0,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_op_promotion_assigned_scopes`;
CREATE TABLE `eh_op_promotion_assigned_scopes`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `promotion_id` BIGINT NOT NULL COMMENT 'promotion id',
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city',
  `scope_id` BIGINT,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_scope_promotion_code_id` (`promotion_id`,`scope_code`,`scope_id`),
  CONSTRAINT `eh_op_promotion_assigned_scopes_ibfk_1` FOREIGN KEY (`promotion_id`) REFERENCES `eh_op_promotion_activities` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
-- the operation promotion message send to user
--
DROP TABLE IF EXISTS `eh_op_promotion_messages`;
CREATE TABLE `eh_op_promotion_messages`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `message_seq` BIGINT NOT NULL COMMENT 'message sequence id generated at server side',
  `sender_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of user who send the message',
  `target_uid` BIGINT NOT NULL DEFAULT 0,
  `message_text` TEXT COMMENT 'message content',
  `meta_app_id` BIGINT COMMENT 'app that is in charge of message content and meta intepretation',
  `message_meta` TEXT COMMENT 'JSON encoded message meta info, in format of string to string map',
  `result_data` TEXT COMMENT 'sender generated tag',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_order_account`;
CREATE TABLE `eh_order_account` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `order_id` BIGINT,
  `enterprise_id` BIGINT,
  `account_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_order_invoice`;
CREATE TABLE `eh_order_invoice` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `order_id` BIGINT NOT NULL COMMENT 'order_id',
  `taxpayer_type` TINYINT COMMENT '0-COMPANY_TAXPAYER 1-INDIVIDUAL_TAXPAYER',
  `vat_type` TINYINT COMMENT '0-GENERAL_TAXPAYER 1-NON_GENERAL_TAXPAYER',
  `expense_type` TINYINT COMMENT '0-CONF',
  `company_name` VARCHAR(20),
  `vat_code` VARCHAR(20),
  `vat_address` VARCHAR(128),
  `vat_phone` VARCHAR(20),
  `vat_bankName` VARCHAR(20),
  `vat_bankAccount` VARCHAR(20),
  `address` VARCHAR(128),
  `zip_code` VARCHAR(20),
  `consignee` VARCHAR(20),
  `contact` VARCHAR(20),
  `contract_flag` TINYINT COMMENT '0-dont need 1-need',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organization_address_mappings`;
CREATE TABLE `eh_organization_address_mappings` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL COMMENT 'community id',
  `address_id` BIGINT NOT NULL COMMENT 'address id',

  `organization_address` VARCHAR(128) COMMENT 'organization address used in organization',
  `living_status` TINYINT NOT NULL,
  `namespace_type` VARCHAR(64),
  `create_time` DATETIME,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  KEY `address_id` (`address_id`),
  CONSTRAINT `eh_organization_address_mappings_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_addresses`;
CREATE TABLE `eh_organization_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_groups',
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_addresses',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'redundant auditing info',
  `process_code` TINYINT,
  `process_details` TEXT,
  `proof_resource_uri` VARCHAR(1024),
  `approve_time` DATETIME COMMENT 'redundant auditing info',
  `update_time` DATETIME,
  `building_id` BIGINT NOT NULL DEFAULT 0,
  `building_name` VARCHAR(128),

  PRIMARY KEY (`id`),
  KEY `fk_eh_orgaddr_owner` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_assigned_scopes`;
CREATE TABLE `eh_organization_assigned_scopes` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: building',
  `scope_id` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_attachments`;
CREATE TABLE `eh_organization_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organization_bill_items`;
CREATE TABLE `eh_organization_bill_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `bill_id` BIGINT NOT NULL,
  `item_name` VARCHAR(128) COMMENT 'the tile of bill item',
  `start_count` DECIMAL(10,2) COMMENT 'the start count of bill item for the specific month',
  `end_count` DECIMAL(10,2) COMMENT 'the end count of bill item for the specific month',
  `use_count` DECIMAL(10,2) COMMENT 'the count of bill item which end_count substract start_count',
  `price` DECIMAL(10,2),
  `total_amount` DECIMAL(10,2) COMMENT 'the money amount of the bill item',
  `description` TEXT,
  `creator_uid` BIGINT COMMENT 'uid of the user who has the bill',
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `fk_eh_organization_bill` (`bill_id`),
  CONSTRAINT `eh_organization_bill_items_ibfk_1` FOREIGN KEY (`bill_id`) REFERENCES `eh_organization_bills` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organization_billing_accounts`;
CREATE TABLE `eh_organization_billing_accounts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `account_number` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'the account number which use to identify the unique account',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'organization id',
  `balance` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `update_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_account_number`(`account_number`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
-- the transaction history of paid the bills
--
DROP TABLE IF EXISTS `eh_organization_billing_transactions`;
CREATE TABLE `eh_organization_billing_transactions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `tx_sequence` VARCHAR(128) NOT NULL COMMENT 'uuid, the sequence binding the two records of a single transaction',
  `tx_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: online, 2: offline',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'organization id',
  `owner_account_id` BIGINT NOT NULL DEFAULT 0,
  `target_account_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: user, 2: family, 3: organization',
  `target_account_id` BIGINT NOT NULL DEFAULT 0,
  `order_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: orders in eh_organization_orders',
  `order_id` BIGINT NOT NULL DEFAULT 0,
  `charge_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid to target(negative) or received from target(positive)',
  `description` TEXT COMMENT 'the description for the transaction',
  `vendor` VARCHAR(128) DEFAULT '' COMMENT 'which third-part pay vendor is used',
  `pay_account` VARCHAR(128) DEFAULT '' COMMENT 'the pay account from third-part pay vendor',
  `result_code_scope` VARCHAR(128) DEFAULT '' COMMENT 'the scope of result code, defined in zuolin',
  `result_code_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'the code id occording to scope',
  `result_desc` VARCHAR(2048) DEFAULT '' COMMENT 'the description of the transaction',
  `operator_uid` BIGINT COMMENT 'the user is who paid the bill, including help others pay the bill',
  `paid_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: selfpay, 2: agent',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organization_bills`;
CREATE TABLE `eh_organization_bills` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `entity_type` VARCHAR(32),
  `entity_id` BIGINT NOT NULL COMMENT 'target address id if target_type is a address',
  `address` VARCHAR(128),
  `name` VARCHAR(128) COMMENT 'the tile of bill',
  `date_str` VARCHAR(128) COMMENT 'the date string in bill',
  `start_date` DATE COMMENT 'the start date of the bill',
  `end_date` DATE COMMENT 'the end date of the bill',
  `pay_date` DATE COMMENT 'the pay date of the bill, the bill must be paid at the end of the date',
  `description` TEXT,
  `due_amount` DECIMAL(10,2) COMMENT 'the money amount of the bill for the current month',
  `owe_amount` DECIMAL(10,2) COMMENT 'the paid money amount of the paid bill',
  `creator_uid` BIGINT COMMENT 'uid of the user who has the bill',
  `notify_count` INTEGER COMMENT 'how many times of notification is sent for the bill',
  `notify_time` DATETIME COMMENT 'the last time of notification for the bill',
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_bills_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_communities`;
CREATE TABLE `eh_organization_communities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL,
  `community_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_org_community_id`(`organization_id`, `community_id`),
  KEY `i_eh_orgc_dept`(`organization_id`),
  KEY `i_eh_orgc_community`(`community_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_community_requests`;
CREATE TABLE `eh_organization_community_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_communities',
  `member_type` VARCHAR(32) NOT NULL COMMENT 'enterprise',
  `member_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise_id etc',
  `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'redundant auditing info',
  `process_code` TINYINT,
  `process_details` TEXT,
  `proof_resource_uri` VARCHAR(1024),
  `approve_time` DATETIME COMMENT 'redundant auditing info',
  `requestor_comment` TEXT,
  `operation_type` TINYINT COMMENT '1: request to join, 2: invite to join',
  `inviter_uid` BIGINT COMMENT 'record inviter user id',
  `invite_time` DATETIME COMMENT 'the time the member is invited',
  `update_time` DATETIME,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`),
  KEY `community_id` (`community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organization_contacts`;
CREATE TABLE `eh_organization_contacts` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `contact_name` VARCHAR(64),
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `creator_uid` BIGINT COMMENT 'uid of the user who has the bill',
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_contacts_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_details`;
CREATE TABLE `eh_organization_details` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL COMMENT 'group id',
  `description` TEXT COMMENT 'description',
  `contact` VARCHAR(128) COMMENT 'the phone number',
  `address` VARCHAR(256) COMMENT 'address str',
  `create_time` DATETIME,
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),
  `display_name` VARCHAR(64),
  `contactor` VARCHAR(64),
  `member_count` BIGINT DEFAULT 0,
  `checkin_date` DATETIME COMMENT 'checkin date',
  `avatar` VARCHAR(128),
  `post_uri` VARCHAR(128),
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `service_user_id` BIGINT COMMENT 'customer service staff',
  `namespace_organization_type` VARCHAR(128),
  `namespace_organization_token` VARCHAR(128),
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_job_position_maps`;
CREATE TABLE `eh_organization_job_position_maps` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `job_position_id` BIGINT NOT NULL,
  `organization_id` BIGINT NOT NULL COMMENT 'orgnaization member id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_job_positions`;
CREATE TABLE `eh_organization_job_positions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'organization',
  `owner_id` BIGINT NOT NULL COMMENT 'orgnaization member id',
  `name` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 2: active',
  `discription` VARCHAR(128),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_member_logs`;
CREATE TABLE `eh_organization_member_logs` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `user_id` BIGINT COMMENT 'organization member target id (type user)',
  `contact_name` VARCHAR(64),
  `contact_type` TINYINT DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `operation_type` TINYINT DEFAULT 0 COMMENT '0-退出企业 1-加入企业',
  `request_type` TINYINT DEFAULT 0 COMMENT '0-管理员操作 1-用户操作',
  `operate_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organization_members`;
CREATE TABLE `eh_organization_members` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL,
  `target_type` VARCHAR(32) COMMENT 'untrack, user',
  `target_id` BIGINT NOT NULL COMMENT 'target user id if target_type is a user',
  `member_group` VARCHAR(32) COMMENT 'pm group the member belongs to',
  `contact_name` VARCHAR(64),
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `contact_description` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: confirming, 2: active',
  `group_id` BIGINT DEFAULT 0 COMMENT 'refer to the organization id',
  `employee_no` VARCHAR(128),
  `avatar` VARCHAR(128),
  `group_path` VARCHAR(128) COMMENT 'refer to the organization path',
  `gender` TINYINT DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
  `update_time` DATETIME,
  `create_time` DATETIME,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `namespace_id` INTEGER DEFAULT 0,
  `visible_flag` TINYINT DEFAULT 0 COMMENT '0 show 1 hide',
  `group_type` VARCHAR(64) COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER',
  `creator_uid` BIGINT,
  `operator_uid` BIGINT,  
  
  PRIMARY KEY (`id`),
  KEY `fk_eh_orgm_owner` (`organization_id`),
  KEY `i_eh_corg_group` (`member_group`),
  KEY `i_target_id` (`target_id`),
  KEY `i_contact_token` (`contact_token`),
  CONSTRAINT `eh_organization_members_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organization_orders`;
CREATE TABLE `eh_organization_orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `bill_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to id of eh_organization_bills',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who make the order',
  `payer_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id who pay the order',
  `paid_time` DATETIME COMMENT 'the pay time of the bill',
  `amount` DECIMAL(10,2) COMMENT 'the paid money amount',
  `description` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: waiting for pay, 2: paid',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 创建eh_organization_owner与eh_address的多对多表    by xq.tian
--
DROP TABLE IF EXISTS `eh_organization_owner_address`;
CREATE TABLE `eh_organization_owner_address` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `address_id` BIGINT,
  `living_status` TINYINT,
  `auth_type` TINYINT COMMENT 'Auth type',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 客户资料管理中的附件上传记录表    by xq.tian
--
DROP TABLE IF EXISTS `eh_organization_owner_attachments`;
CREATE TABLE `eh_organization_owner_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 客户的活动记录表   by xq.tian
--
DROP TABLE IF EXISTS `eh_organization_owner_behaviors`;
CREATE TABLE `eh_organization_owner_behaviors` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `address_id` BIGINT COMMENT 'address id',
  `behavior_type` VARCHAR(20) COMMENT 'immigration, emigration..',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `behavior_time` DATETIME,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 车辆管理中的附件上传记录表    by xq.tian
--
DROP TABLE IF EXISTS `eh_organization_owner_car_attachments`;
CREATE TABLE `eh_organization_owner_car_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'car id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 创建eh_organization_owner_cars表,汽车管理的汽车表    by xq.tian
--
DROP TABLE IF EXISTS `eh_organization_owner_cars`;
CREATE TABLE `eh_organization_owner_cars` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `brand` VARCHAR(20),
  `parking_space` VARCHAR(20),
  `parking_type` TINYINT,
  `plate_number` VARCHAR(20),
  `contacts` VARCHAR(20),
  `contact_number` VARCHAR(20),
  `content_uri` VARCHAR(1024),
  `color` VARCHAR(20),
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 创建eh_organization_owner_owner_car与eh_organization_owner_cars的多对多表    by xq.tian
--
DROP TABLE IF EXISTS `eh_organization_owner_owner_car`;
CREATE TABLE `eh_organization_owner_owner_car` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `car_id` BIGINT,
  `primary_flag` TINYINT COMMENT 'primary flag, yes: 1, no: 0',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 客户类型表    by xq.tian
--
DROP TABLE IF EXISTS `eh_organization_owner_type`;
CREATE TABLE `eh_organization_owner_type` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(20) COMMENT 'owner, tenant, relative, friend',
  `display_name` VARCHAR(20) COMMENT 'display name',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
-- the residents living in the commmunity owned by organization
--
DROP TABLE IF EXISTS `eh_organization_owners`;
CREATE TABLE `eh_organization_owners` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `contact_name` VARCHAR(64),
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `contact_description` TEXT,
  `address_id` BIGINT COMMENT 'address id',
  `address` VARCHAR(128),
  `creator_uid` BIGINT COMMENT 'uid of the user who has the bill',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `registered_residence` VARCHAR(128) COMMENT 'registered residence',
  `org_owner_type_id` BIGINT COMMENT 'owner type id',
  `gender` TINYINT COMMENT 'male, female',
  `birthday` DATE COMMENT 'birthday',
  `marital_status` VARCHAR(10),
  `job` VARCHAR(10) COMMENT 'job',
  `company` VARCHAR(100) COMMENT 'company',
  `id_card_number` VARCHAR(32) COMMENT 'id card number',
  `avatar` VARCHAR(1024) COMMENT 'avatar',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'delete: 0, normal: 1',
  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_owners_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_role_map`;
CREATE TABLE `eh_organization_role_map` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `role_id` BIGINT NOT NULL DEFAULT 0,
  `private_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',
  `create_time` DATETIME,
  `role_name` VARCHAR(128) COMMENT 'role name',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_organization_task_targets`;
CREATE TABLE `eh_organization_task_targets`(
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT,
  `target_type` VARCHAR(64) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` BIGINT COMMENT 'target object(user/group) id',
  `task_type` VARCHAR(64) NOT NULL,
  `message_type` VARCHAR(64) COMMENT 'PUSH COMMENT SMS ',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organization_tasks`;
CREATE TABLE `eh_organization_tasks` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `organization_type` VARCHAR(64) COMMENT 'NONE, PM(Property Management), GARC(Resident Committee), GANC(Neighbor Committee), GAPS(Police Station)',
  `apply_entity_type` VARCHAR(32) COMMENT 'the entity who apply the task, like TOPIC',
  `apply_entity_id` BIGINT NOT NULL COMMENT 'target topic id if target_type is a topic',
  `target_type` VARCHAR(32) COMMENT 'user',
  `target_id` BIGINT NOT NULL COMMENT 'target user id if target_type is a user',
  `task_type` VARCHAR(32) COMMENT 'task type assigned by organization',
  `description` TEXT,
  `task_status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: unprocessed, 2: processing；3 已处理；4 其他',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'uid of the user who process the task',
  `operate_time` DATETIME,
  `unprocessed_time` DATETIME,
  `processing_time` DATETIME,
  `processed_time` DATETIME,
  `creator_uid` BIGINT COMMENT 'uid of the user who create the task',
  `create_time` DATETIME,
  `task_category` VARCHAR(128) COMMENT '1:PUBLIC_AREA 2:PRIVATE_OWNER',
  `visible_region_type` TINYINT COMMENT 'define the visible region type',
  `visible_region_id` BIGINT COMMENT 'visible region id',

  PRIMARY KEY (`id`),
  KEY `fk_eh_organization` (`organization_id`),
  CONSTRAINT `eh_organization_tasks_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_organizations`;
CREATE TABLE `eh_organizations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `parent_id` BIGINT COMMENT 'id of the parent region',
  `organization_type` VARCHAR(64) COMMENT 'NONE, PM(Property Management), GARC(Resident Committee), GANC(Neighbor Committee), GAPS(Police Station)',
  `name` VARCHAR(128),
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address for department',
  `description` TEXT,
  `path` VARCHAR(128) COMMENT 'path from the root',
  `level` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: inactive, 2: active, 3: locked, 4: mark as deleted',
  `department_type` VARCHAR(64),
  `group_type` VARCHAR(64) COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `directly_enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'directly under the company',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `group_id` BIGINT COMMENT 'eh_group id',
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `show_flag` TINYINT DEFAULT 1,
  `namespace_organization_token` VARCHAR(256) COMMENT 'the token from third party',
  `namespace_organization_type` VARCHAR(128) COMMENT 'the type of organization',
  `size` INTEGER COMMENT 'job level size',
  `creator_uid` BIGINT,
  `operator_uid` BIGINT,
  `set_admin_flag` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `i_eh_org_name_level` (`name`,`level`),
  KEY `i_eh_org_path` (`path`),
  KEY `i_eh_org_path_level` (`path`,`level`),
  KEY `i_eh_org_parent` (`parent_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 对象下载记录
DROP TABLE IF EXISTS `eh_os_object_download_logs`;
CREATE TABLE `eh_os_object_download_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',
  `service_type` VARCHAR(32) COMMENT '需要存储对象的业务类型',
  `service_id` BIGINT COMMENT '业务类型对应的业务id',
  `object_id` BIGINT NOT NULL COMMENT 'eh_os_objects id',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 对象存储(object storage)  add by xq.tian  2017/02/24
-- 对象表
DROP TABLE IF EXISTS `eh_os_objects`;
CREATE TABLE `eh_os_objects` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',
  `service_type` VARCHAR(32) COMMENT '需要存储对象的业务类型',
  `service_id` BIGINT COMMENT '业务类型对应的业务id',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parent id, eh_os_objects id',
  `object_type` TINYINT NOT NULL DEFAULT 2 COMMENT '0: dir, 1: file',
  `md5` VARCHAR(64) COMMENT 'md5',
  `path` VARCHAR(2048) COMMENT 'e.g: a/b/c/d/e',
  `name` VARCHAR(256) NOT NULL COMMENT 'object name',
  `size` INTEGER NOT NULL DEFAULT 0 COMMENT 'The unit is byte',
  `content_type` VARCHAR(32) COMMENT 'file object content type',
  `content_uri` VARCHAR(1024) COMMENT 'file object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `delete_uid` BIGINT,
  `delete_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- Global table for relationship of owner 1<-->n door
--
DROP TABLE IF EXISTS `eh_owner_doors`;
CREATE TABLE `eh_owner_doors` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
  `owner_id` BIGINT NOT NULL,
  `door_id` BIGINT NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `i_uq_door_id_owner_id`(`door_id`, `owner_id`, `owner_type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_park_apply_card`;
CREATE TABLE `eh_park_apply_card` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `applier_id` BIGINT,
  `applier_name` VARCHAR(20),
  `applier_phone` VARCHAR(20),
  `plate_number` VARCHAR(20),
  `apply_time` DATETIME,
  `apply_status` TINYINT,
  `fetch_status` TINYINT,
  `deadline` DATETIME,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `company_name` VARCHAR(256) NOT NULL DEFAULT '',

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_park_charge`;
CREATE TABLE `eh_park_charge`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `months` TINYINT,
  `amount` DOUBLE,
  `community_id` BIGINT,
  `card_type` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_parking_activities`;
CREATE TABLE `eh_parking_activities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `start_time` DATETIME COMMENT 'start time',
  `end_time` DATETIME COMMENT 'end time',
  `top_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'Top N user can join the activity',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_parking_attachments`;
CREATE TABLE `eh_parking_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g comment_id',
  `data_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: 身份证, 2: 行驶证 3:驾驶证',

  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_parking_car_series`;
CREATE TABLE `eh_parking_car_series`(
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `create_time` DATETIME,
  `delete_time` DATETIME,

  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- merge from customer-manage-1.1-delta-schema.sql 20161025 by lqs
--
-- 车辆停车类型     add by xq.tian 2016/10/11
--
DROP TABLE IF EXISTS `eh_parking_card_categories`;
CREATE TABLE `eh_parking_card_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `card_type` TINYINT NOT NULL COMMENT '1. temp, 2. month, 3. free ,etc.',
  `category_name` VARCHAR(64) NOT NULL COMMENT 'name of category',
  `status` TINYINT NOT NULL COMMENT '1: normal, 0: delete',
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_parking_card_requests`;
CREATE TABLE `eh_parking_card_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `requestor_enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of organization where the requestor is in',
  `requestor_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'requestor id',
  `plate_number` VARCHAR(64),
  `plate_owner_entperise_name` VARCHAR(64) COMMENT 'the enterprise name of plate owner',
  `plate_owner_name` VARCHAR(64) COMMENT 'the name of plate owner',
  `plate_owner_phone` VARCHAR(64) COMMENT 'the phone of plate owner',
  `status` TINYINT COMMENT '0: inactive, 1: queueing, 2: notified, 3: issued',
  `issue_flag` TINYINT COMMENT 'whether the applier fetch the card or not, 0: unissued, 1: issued',
  `issue_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `car_brand` VARCHAR(64) COMMENT 'car brand',
  `car_color` VARCHAR(64) COMMENT 'car color',
  `car_serie_name` VARCHAR(64) COMMENT 'car serie name',
  `car_serie_id` BIGINT COMMENT 'car serie id',
  `flow_id` BIGINT COMMENT 'flow id',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version',
  `flow_case_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow case id',
  `audit_succeed_time` DATETIME,
  `process_succeed_time` DATETIME,
  `open_card_time` DATETIME,
  `cancel_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 车辆放行记录
--
DROP TABLE IF EXISTS `eh_parking_clearance_logs`;
CREATE TABLE `eh_parking_clearance_logs` (
  `id`             BIGINT  NOT NULL,
  `namespace_id`   INTEGER NOT NULL DEFAULT 0,
  `community_id`   BIGINT,
  `parking_lot_id` BIGINT COMMENT 'eh_parking_lots id',
  `applicant_id`   BIGINT COMMENT 'applicant id',
  `operator_id`    BIGINT COMMENT 'operator id',
  `plate_number`   VARCHAR(32) COMMENT 'plate number',
  `apply_time`     DATETIME COMMENT 'apply time',
  `clearance_time` DATETIME COMMENT 'The time the vehicle passed',
  `remarks`        VARCHAR(1024) COMMENT 'remarks',
  `status`         TINYINT COMMENT '0: inactive, 1: processing, 2: completed, 3: cancelled, 4: pending',
  `creator_uid`    BIGINT,
  `create_time`    DATETIME,
  `update_uid`     BIGINT,
  `update_time`    DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 车辆放行申请人员与处理人员
--
DROP TABLE IF EXISTS `eh_parking_clearance_operators`;
CREATE TABLE `eh_parking_clearance_operators` (
  `id`              BIGINT  NOT NULL,
  `namespace_id`    INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `community_id`    BIGINT,
  `parking_lot_id`  BIGINT COMMENT 'eh_parking_lots id',
  `operator_type`   VARCHAR(32) COMMENT 'applicant, handler',
  `operator_id`     BIGINT,
  `status`          TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`     BIGINT,
  `create_time`     DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_parking_flow`;
CREATE TABLE `eh_parking_flow` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',

  `request_month_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'organization of address',
  `request_recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: all month, 2: number of days',
  `card_request_tip_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `card_agreement_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `max_request_num_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `max_issue_num_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not ',
  `card_request_tip` TEXT,
  `card_agreement` TEXT,
  `max_issue_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the issue card',
  `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card',
  `flow_id` BIGINT NOT NULL COMMENT 'flow id',


  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_parking_lots`;
CREATE TABLE `eh_parking_lots` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'used to display',
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `vendor_lot_token`  VARCHAR(128) COMMENT 'parking lot id from vendor',
  `card_reserve_days` INTEGER NOT NULL DEFAULT 0 COMMENT 'how may days the parking card is reserved for the applicant',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card',
  `tempfee_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is support temp fee',
  `rate_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is support add or delete rate',
  `recharge_month_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'organization of address',
  `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: all month, 2: number of days',
  `is_support_recharge` TINYINT NOT NULL DEFAULT 0 COMMENT 'out date card recharge flag , 1: support recharge , 0: not ',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `lock_car_flag` TINYINT NOT NULL DEFAULT 0 COMMENT ' 1: support, 0: not ',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_parking_recharge_orders`;
CREATE TABLE `eh_parking_recharge_orders` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `order_no` BIGINT,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `plate_number` VARCHAR(64),
  `plate_owner_name` VARCHAR(64) COMMENT 'the name of plate owner',
  `plate_owner_phone` VARCHAR(64) COMMENT 'the phone of plate owner',
  `payer_enterprise_id` BIGINT DEFAULT 0 COMMENT 'the id of organization where the payer is in',
  `payer_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user id of payer',
  `payer_phone` VARCHAR(64) COMMENT 'the phone of payer',
  `paid_time` DATETIME COMMENT 'the pay time',
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of eh_parking_vendors',
  `card_number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be number of virtual card or location number',
  `rate_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from eh_parking_recharge_rates or 3rd system, according to vendor_name',
  `rate_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'rate name for recharging the parking card',
  `month_count` DECIMAL(10,2) COMMENT 'how many months in the rate for recharging the parking card',
  `price` DECIMAL(10,2) COMMENT 'the total price in the item for recharging the parking card',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `recharge_status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: none, 1: unrecharged 1: recharged',
  `recharge_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `old_expired_time` DATETIME,
  `new_expired_time` DATETIME,
  `paid_type` VARCHAR(32) COMMENT 'the type of payer',
  `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT 'the order is delete, 0 : is not deleted, 1: deleted',
  `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: monthly, 2: temporary',
  `order_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from 3rd system',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
-- the recharging rates which is convenient for the user picking while recharging the card
--
DROP TABLE IF EXISTS `eh_parking_recharge_rates`;
CREATE TABLE `eh_parking_recharge_rates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `rate_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'rate name for recharging the parking card',
  `month_count` DECIMAL(10,2) COMMENT 'how many months in the rate for recharging the parking card',
  `price` DECIMAL(10,2) COMMENT 'the total price for recharging the parking card',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `card_type` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_parking_statistics`;
CREATE TABLE `eh_parking_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',

  `amount` DECIMAL(10,2),

  `date_str` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
-- the vendors who service the parking lot, zuolin is the default vendor
--
DROP TABLE IF EXISTS `eh_parking_vendors`;
CREATE TABLE `eh_parking_vendors` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'the identifier name of the vendor',
  `display_name` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the name used to display in desk',
  `description` VARCHAR(2048) COMMENT 'the description of the vendor',
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_vender_name`(`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_payment_card_issuer_communities`;
CREATE TABLE `eh_payment_card_issuer_communities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `issuer_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the card issuer',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_payment_card_issuers`;
CREATE TABLE `eh_payment_card_issuers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'name',
  `description` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'description',
  `pay_url` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'pay_url',
  `alipay_recharge_account` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'alipay_recharge_account',
  `weixin_recharge_account` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'weixin_recharge_account',

  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of vendors taotaogu',
  `vendor_data` TEXT COMMENT 'the extra information of card for example make_no',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive,1:active ',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_card_recharge_orders`;
CREATE TABLE `eh_payment_card_recharge_orders` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `order_no` BIGINT NOT NULL DEFAULT 0 COMMENT 'order no',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `user_id` BIGINT NOT NULL DEFAULT 0,
  `user_name` VARCHAR(64) COMMENT 'the name of user',
  `mobile` VARCHAR(64) COMMENT 'the mobile of user',
  `card_no` VARCHAR(256) NOT NULL COMMENT 'the number of card',
  `card_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the eh_payment_cards record',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of order',

  `payer_uid` BIGINT NOT NULL DEFAULT 0,
  `payer_name` VARCHAR(64) COMMENT 'the name of user',
  `paid_time` DATETIME COMMENT 'the pay time',
  `pay_status` TINYINT NOT NULL DEFAULT 1 COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',

  `recharge_time` DATETIME COMMENT 'recharge time',
  `recharge_status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: fail, 1: unrecharged 2: recharged 3:COMPLETE 4:REFUNDED',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `paid_type` VARCHAR(32) COMMENT 'the type of paid 10001:zhifubao 10002: weixin',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_payment_card_transactions`;
CREATE TABLE `eh_payment_card_transactions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `payer_uid` BIGINT NOT NULL DEFAULT 0,
  `user_name` VARCHAR(64) COMMENT 'the name of user',
  `mobile` VARCHAR(64) COMMENT 'the mobile of user',
  `item_name` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'the name of item',
  `merchant_no` VARCHAR(256) COMMENT 'the merchant no',
  `merchant_name` VARCHAR(256) COMMENT 'the merchant name',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid',
  `transaction_no` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'transaction serial number',
  `transaction_time` DATETIME COMMENT 'the pay time',
  `card_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the eh_payment_cards record',
  `card_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'the number of card',

  `token` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the token of card token to pay',

  `order_no` VARCHAR(512) COMMENT 'order no',
  `consume_Type` TINYINT NOT NULL DEFAULT 1 COMMENT 'the type of merchant',

  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: fail, 1: waitting 2: sucess',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of vendors taotaogu',
  `vendor_result` TEXT COMMENT 'the extra information of transactions',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_payment_cards`;
CREATE TABLE `eh_payment_cards` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `issuer_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the card issuer',
  `user_name` VARCHAR(64) COMMENT 'the name of user',
  `mobile` VARCHAR(64) COMMENT 'the mobile of user',
  `card_no` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'the id of card ,according the third system',
  `balance` DECIMAL(10,2) COMMENT 'the balance of card',
  `password` VARCHAR(1024) NOT NULL DEFAULT '' COMMENT 'the password of user',
  `user_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `activate_time` DATETIME,
  `expired_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive,1:wating 2: active ',
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of vendors taotaogu',
  `vendor_card_data` TEXT COMMENT 'the extra information of card for example make_no',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pm_task_attachments`;
CREATE TABLE `eh_pm_task_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pm_task_history_addresses`;
CREATE TABLE `eh_pm_task_history_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `building_name` VARCHAR(128),
  `address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'detail address',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: active ',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pm_task_logs`;
CREATE TABLE `eh_pm_task_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `task_id` BIGINT NOT NULL DEFAULT 0,

  `content` TEXT COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `target_type` VARCHAR(32) COMMENT 'user',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'target user id if target_type is a user',

  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_time` DATETIME,
  `operator_name` VARCHAR(64) COMMENT 'the name of user',
  `operator_phone` VARCHAR(64) COMMENT 'the phone of user',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pm_task_statistics`;
CREATE TABLE `eh_pm_task_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `category_id` BIGINT NOT NULL DEFAULT 0,
  `total_count` INTEGER NOT NULL DEFAULT 0,
  `unprocess_count` INTEGER NOT NULL DEFAULT 0,
  `processing_count` INTEGER NOT NULL DEFAULT 0,
  `processed_count` INTEGER NOT NULL DEFAULT 0,
  `close_count` INTEGER NOT NULL DEFAULT 0,
  `star1` INTEGER NOT NULL DEFAULT 0,
  `star2` INTEGER NOT NULL DEFAULT 0,
  `star3` INTEGER NOT NULL DEFAULT 0,
  `star4` INTEGER NOT NULL DEFAULT 0,
  `star5` INTEGER NOT NULL DEFAULT 0,
  `date_str` DATETIME,
  `create_time` DATETIME,
  `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pm_task_target_statistics`;
CREATE TABLE `eh_pm_task_target_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `avg_star` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id',
  `date_str` DATETIME,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pm_task_targets`;
CREATE TABLE `eh_pm_task_targets` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT,
  `target_type` VARCHAR(64) NOT NULL COMMENT 'target object(user/group) type',
  `target_id` BIGINT COMMENT 'target object(user/group) id',
  `role_id` TINYINT NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pm_tasks`;
CREATE TABLE `eh_pm_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'detail address',
  `content` TEXT COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `star` TINYINT COMMENT 'evaluate score',
  `unprocessed_time` DATETIME,
  `processing_time` DATETIME,
  `processed_time` DATETIME,
  `closed_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_time` DATETIME,
  `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id',
  `reserve_time` DATETIME,
  `priority` TINYINT NOT NULL DEFAULT 0 COMMENT 'task rank of request',
  `source_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'task come from ,such as app ,email',
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `requestor_name` VARCHAR(64) COMMENT 'the name of requestor',
  `requestor_phone` VARCHAR(64) COMMENT 'the phone of requestor',
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address id',
  `revisit_content` TEXT COMMENT 'revisit content',
  `revisit_time` DATETIME,
  `operator_star` TINYINT NOT NULL DEFAULT 0 COMMENT 'task star of operator',
  `address_type` TINYINT COMMENT '1: family , 2:organization',
  `address_org_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'organization of address',
  `flow_case_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow case id',
  `string_tag1` VARCHAR(128),
  `building_name` VARCHAR(128),
  `organization_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pmsy_communities`;
CREATE TABLE `eh_pmsy_communities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `community_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of community according the third system, siyuan',
  `contact` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'the contact of user fill in',
  `bill_tip` VARCHAR(256) NOT NULL DEFAULT '' COMMENT 'the bill_tip of user fill in',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pmsy_order_items`;
CREATE TABLE `eh_pmsy_order_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `order_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'eh_pmsy_orders id',
  `bill_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of bill according the third system, siyuan',
  `item_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of item according the third system, siyuan',
  `bill_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the money of bill',
  `resource_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of resource,door according the third system, siyuan',
  `customer_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of customer according the third system, siyuan',
  `create_time` DATETIME,
  `bill_date` DATETIME COMMENT 'the date of bill',
  `status` TINYINT COMMENT 'the status of the order, 0: fail, 1: success',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pmsy_orders`;
CREATE TABLE `eh_pmsy_orders` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `user_name` VARCHAR(64) COMMENT 'the name of address resource',
  `user_contact` VARCHAR(64) COMMENT 'the phone of address resource',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'the amount of money paid',
  `paid_time` DATETIME COMMENT 'the pay time',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `paid_type` VARCHAR(32) COMMENT 'the type of paid 10001:zhifubao 10002: weixin',
  `project_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of siyuan project',
  `customer_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the id of customer according the third system, siyuan',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_pmsy_payers`;
CREATE TABLE `eh_pmsy_payers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `user_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'the name of user fill in',
  `user_contact` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'the contact of user fill in',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'the status of the payer, 0: inactive, 1: wating, 2: active',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_polls partition
-- secondary resource objects (after eh_polls)
--
DROP TABLE IF EXISTS `eh_poll_items`;
CREATE TABLE `eh_poll_items`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `poll_id` BIGINT,
  `subject` VARCHAR(512),
  `resource_id` BIGINT,
  `resource_url` VARCHAR(512),
  `vote_count` INTEGER NOT NULL DEFAULT 0,
  `change_version` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  KEY `i_eh_poll_item_poll`(`poll_id`),
  KEY `i_eh_poll_item_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_polls partition
-- secondary resource objects(after eh_pools)
--
DROP TABLE IF EXISTS `eh_poll_votes`;
CREATE TABLE `eh_poll_votes`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `poll_id` BIGINT,
  `item_id` BIGINT,
  `voter_uid` BIGINT,
  `voter_family_id` BIGINT,
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `i_eh_poll_vote_voter`(`poll_id`, `item_id`, `voter_uid`),
  KEY `i_eh_poll_vote_poll`(`poll_id`),
  KEY `i_eh_poll_vote_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- key table of polling management sharding group
-- First level resource objects
--
DROP TABLE IF EXISTS `eh_polls`;
CREATE TABLE `eh_polls` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `subject` VARCHAR(512),
  `description` TEXT,
  `start_time_ms` BIGINT,
  `start_time` DATETIME,
  `end_time_ms` BIGINT,
  `end_time` DATETIME,
  `multi_select_flag` TINYINT NOT NULL DEFAULT 0,
  `anonymous_flag` TINYINT NOT NULL DEFAULT 0,
  `poll_count` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT,
  `creator_family_id` BIGINT,
  `post_id` BIGINT COMMENT 'associated post in forum',
  `status` TINYINT COMMENT '0: inactive, 1: drafting, 2: active',
  `change_version` INTEGER NOT NULL DEFAULT 1,
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid`(`uuid`),
  KEY `i_eh_poll_start_time_ms`(`start_time_ms`),
  KEY `i_eh_poll_end_time_ms`(`end_time_ms`),
  KEY `i_eh_poll_creator_uid`(`creator_uid`),
  KEY `i_eh_poll_post_id`(`post_id`),
  KEY `i_eh_poll_create_time`(`create_time`),
  KEY `i_eh_poll_delete_time`(`delete_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
--
-- Park activity rules parameter
--
DROP TABLE IF EXISTS `eh_preferential_rules`;
CREATE TABLE `eh_preferential_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'community',
  `owner_id` BIGINT NOT NULL ,
  `start_time` DATETIME COMMENT 'start time',
  `end_time` DATETIME COMMENT 'end time',
  `type` VARCHAR(256) COMMENT 'PARKING',
  `before_nember` BIGINT,
  `params_json` TEXT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 
-- 储存预览内容 
-- 
DROP TABLE IF EXISTS `eh_previews`;
CREATE TABLE `eh_previews` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `content` TEXT,
  `content_type` VARCHAR(128) COMMENT 'content type',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_day_logs`;
CREATE TABLE `eh_punch_day_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT COMMENT 'user''s id',
  `enterprise_id` BIGINT COMMENT 'compay id',
  `punch_date` DATE COMMENT 'user punch date',
  `arrive_time` TIME,
  `leave_time` TIME,
  `work_time` TIME COMMENT 'how long did employee work',
  `status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `view_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'is view(0) not view(1)',
  `morning_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `afternoon_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT '2 or 4 times',
  `noon_leave_time` TIME,
  `afternoon_arrive_time` TIME,
  `exception_status` TINYINT COMMENT '异常状态: 0-正常;1-异常',
  `device_change_flag` TINYINT DEFAULT 0 COMMENT '0- unchange 1-changed',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_punch_exception_approvals`;
CREATE TABLE `eh_punch_exception_approvals` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT COMMENT 'user''s id',
  `enterprise_id` BIGINT COMMENT 'compay id',
  `punch_date` DATE COMMENT 'user punch date',
  `approval_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `morning_approval_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `afternoon_approval_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `view_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'is view(0) not view(1)',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT '2 or 4 times',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_punch_exception_requests`;
CREATE TABLE `eh_punch_exception_requests` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT COMMENT 'user''s id',
  `enterprise_id` BIGINT COMMENT 'compay id',
  `punch_date` DATE COMMENT 'user punch date',
  `request_type` TINYINT COMMENT '0:request, 1:approval',
  `description` VARCHAR(256) ,
  `status` TINYINT DEFAULT 1 COMMENT '0: inactive, 1: waitingForApproval, 2:active',
  `approval_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `morning_approval_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `afternoon_approval_status` TINYINT DEFAULT 1 COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `process_details` TEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `view_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'is view(0) not view(1)',
  `request_id` BIGINT COMMENT 'approval request id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_punch_geopoints`;
CREATE TABLE `eh_punch_geopoints` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_id` BIGINT,
  `description` VARCHAR(256),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),
  `distance` DOUBLE ,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `location_rule_id` BIGINT COMMENT 'fk:eh_punch_geopoints id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 假期表
--
DROP TABLE IF EXISTS `eh_punch_holidays`;
CREATE TABLE `eh_punch_holidays` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `workday_rule_id` BIGINT COMMENT 'fk:eh_punch_workday_rules id',
  `status` TINYINT COMMENT 'its holiday or workday:0-workday ; 1-holiday',
  `rule_date` DATE COMMENT 'date',
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 考勤地点表
--
DROP TABLE IF EXISTS `eh_punch_location_rules`;
CREATE TABLE `eh_punch_location_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'location rule name ',
  `description` VARCHAR(256) ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_punch_logs`;
CREATE TABLE `eh_punch_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT COMMENT 'user''s id',
  `enterprise_id` BIGINT COMMENT 'compay id',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `punch_date` DATE COMMENT 'user punch date',
  `punch_time` DATETIME COMMENT 'user check time',
  `punch_status` TINYINT COMMENT '1:Normal, 0:Not in punch area',
  `identification` VARCHAR(255) COMMENT 'unique identification for a phone',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 打卡规则和owner的映射表
--
DROP TABLE IF EXISTS `eh_punch_rule_owner_map`;
CREATE TABLE `eh_punch_rule_owner_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `target_type` VARCHAR(128) COMMENT 'target resource(user/organization) type',
  `target_id` BIGINT COMMENT 'target resource(user/organization) id',
  `punch_rule_id` BIGINT COMMENT 'fk:eh_punch_rules id',
  `review_rule_id` BIGINT COMMENT 'fk:review rule id',
  `description` VARCHAR(256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 ;


--
-- 打卡总规则表
--
DROP TABLE IF EXISTS `eh_punch_rules`;
CREATE TABLE `eh_punch_rules` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `time_rule_id` BIGINT COMMENT 'fk:eh_punch_time_rules id',
  `location_rule_id` BIGINT COMMENT 'fk:eh_punch_geopoints id',
  `wifi_rule_id` BIGINT COMMENT 'fk:eh_punch_wifi_rules id',
  `workday_rule_id` BIGINT COMMENT 'fk:eh_punch_workday_rules id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT ,
  `operate_time` DATETIME ,

  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 ;


DROP TABLE IF EXISTS `eh_punch_schedulings`;
CREATE TABLE `eh_punch_schedulings` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `target_type` VARCHAR(128) COMMENT 'target resource(user/organization) type',
  `target_id` BIGINT COMMENT 'target resource(user/organization) id',
  `rule_date` DATE COMMENT 'date',
  `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT COMMENT 'eh_punch_time_rules id --null:not work day',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 打卡统计表-个人报表-每日生成
--
DROP TABLE IF EXISTS `eh_punch_statistics`;
CREATE TABLE `eh_punch_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `punch_month` VARCHAR(8) COMMENT 'yyyymm',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `user_id` BIGINT COMMENT 'user id',
  `user_name` VARCHAR(128) COMMENT 'user name',
  `dept_id` BIGINT COMMENT 'user department id',
  `dept_name` VARCHAR(128) COMMENT 'user department name',
  `work_day_count` INTEGER COMMENT '应上班天数',
  `work_count` DOUBLE COMMENT '实际上班天数',
  `belate_count` INTEGER COMMENT '迟到次数',
  `leave_early_count` INTEGER COMMENT '早退次数',
  `blandle_count` INTEGER COMMENT '迟到且早退次数',
  `unpunch_count` DOUBLE COMMENT '缺勤天数',
  `absence_count` DOUBLE COMMENT '事假天数',
  `sick_count` DOUBLE COMMENT '病假天数',
  `exchange_count` DOUBLE COMMENT '调休天数',
  `outwork_count` DOUBLE COMMENT '公出天数',
  `over_time_sum` BIGINT COMMENT '加班累计(非工作日上班)',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT 'how many times should punch everyday :2/4',
  `exception_status` TINYINT COMMENT '异常状态: 0-正常;1-异常',
  `description` VARCHAR(256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `exts` VARCHAR(1024) COMMENT 'json string exts:eq[{"name":"事假","timeCount":"1天2小时"},{"name":"丧假","timeCount":"3天2小时30分钟"}]',
  `user_status` TINYINT DEFAULT 0 COMMENT '0:normal普通 1:NONENTRY未入职 2:RESIGNED已离职',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 考勤时间管理
--
DROP TABLE IF EXISTS `eh_punch_time_rules`;
CREATE TABLE `eh_punch_time_rules` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'time rule name ',
  `start_early_time` TIME COMMENT 'how early can i arrive',
  `start_late_time` TIME COMMENT 'how late can i arrive ',
  `work_time` TIME COMMENT 'how long do i must be work',
  `noon_leave_time` TIME,
  `afternoon_arrive_time` TIME,
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT 'how many times should punch everyday :2/4',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `day_split_time` TIME DEFAULT '05:00:00' COMMENT 'the time a day begin',
  `description` VARCHAR(128) COMMENT 'rule description',
  `target_type` VARCHAR(128) COMMENT 'target resource(user/organization) type',
  `target_id` BIGINT COMMENT 'target resource(user/organization) id',
  `start_early_time_long` BIGINT COMMENT 'how early can i arrive',
  `start_late_time_long` BIGINT COMMENT 'how late can i arrive ',
  `work_time_long` BIGINT COMMENT 'how long do i must be work',
  `noon_leave_time_long` BIGINT,
  `afternoon_arrive_time_long` BIGINT,
  `day_split_time_long` BIGINT DEFAULT '18000000' COMMENT 'the time a day begin',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 考勤wifi表
--
DROP TABLE IF EXISTS `eh_punch_wifi_rules`;
CREATE TABLE `eh_punch_wifi_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `description` VARCHAR(256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;


--
-- 具体wifi列表
--
DROP TABLE IF EXISTS `eh_punch_wifis`;
CREATE TABLE `eh_punch_wifis` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `wifi_rule_id` BIGINT COMMENT 'fk:eh_punch_wifi_rules id',
  `ssid` VARCHAR(64) COMMENT 'wifi ssid',
  `mac_address` VARCHAR(32) COMMENT 'wifi mac address',
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_punch_workday`;
CREATE TABLE `eh_punch_workday` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `date_status` TINYINT COMMENT '0:weekend work date, 1:holiday',
  `date_tag` DATE COMMENT 'date',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `enterprise_id` BIGINT COMMENT 'compay id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 打卡排班表
--
DROP TABLE IF EXISTS `eh_punch_workday_rules`;
CREATE TABLE `eh_punch_workday_rules` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `name` VARCHAR(128) COMMENT 'wifi rule name ',
  `description` VARCHAR(256),
  `work_week_dates` VARCHAR(8) DEFAULT '0000000' COMMENT '7位，从左至右每一位表示星期7123456,值:0-关闭 1-开放 example:周12345上班[0111110]',
  `creator_uid` BIGINT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_push_message_results`;
CREATE TABLE `eh_push_message_results` (
  `id` BIGINT NOT NULL COMMENT 'id of the push message result, not auto increment',
  `message_id` BIGINT NOT NULL DEFAULT 0,
  `user_id` BIGINT NOT NULL DEFAULT 0,
  `identifier_token` VARCHAR(128) COMMENT 'The mobile phone of user',
  `send_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_push_messages`;
CREATE TABLE `eh_push_messages` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `message_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'NORMAL_MESSAGE, UPGRADE_MESSAGE, NOTIFY_MESSAGE',
  `title` VARCHAR(128) COMMENT 'title of message',
  `content` VARCHAR(4096) COMMENT 'content for message',
  `target_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'CITY, COMMUNITY, FAMILY, USER',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `status` INTEGER NOT NULL DEFAULT 0 COMMENT 'WAITING, RUNNING, FINISHED',
  `create_time` DATETIME,
  `start_time` DATETIME,
  `finish_time` DATETIME,
  `device_type` VARCHAR(64),
  `device_tag` VARCHAR(64),
  `app_version` VARCHAR(64),
  `push_count` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_qrcodes`;
CREATE TABLE `eh_qrcodes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `description` VARCHAR(1024),
  `view_count` BIGINT NOT NULL DEFAULT 0,
  `logo_uri` VARCHAR(1024),
  `expire_time` DATETIME COMMENT 'it is permanent if there is no expired time, else it is temporary',
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `creator_uid` BIGINT NOT NULL COMMENT 'createor user id',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_categories`;
CREATE TABLE `eh_quality_inspection_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `score` DOUBLE NOT NULL DEFAULT 0,
  `description` TEXT COMMENT 'content data',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_evaluation_factors`;
CREATE TABLE `eh_quality_inspection_evaluation_factors` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_categories',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `weight` DOUBLE NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_evaluations`;
CREATE TABLE `eh_quality_inspection_evaluations` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `date_str` VARCHAR(32) NOT NULL DEFAULT '',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `group_name` VARCHAR(64),
  `score` DOUBLE NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_logs`;
CREATE TABLE `eh_quality_inspection_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'standard, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record operator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_specification_item_results`;
CREATE TABLE `eh_quality_inspection_specification_item_results` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the item, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `task_record_id` BIGINT NOT NULL COMMENT 'id of the eh_quality_inspection_task_records',
  `task_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_tasks',
  `specification_parent_id` BIGINT NOT NULL DEFAULT 0,
  `specification_id` BIGINT NOT NULL DEFAULT 0,
  `specification_path` VARCHAR(128),
  `item_description` VARCHAR(512),
  `item_score` DOUBLE NOT NULL DEFAULT 0,
  `quantity` INTEGER NOT NULL DEFAULT 0,
  `total_score` DOUBLE NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_specifications`;
CREATE TABLE `eh_quality_inspection_specifications` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community',
  `scope_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL DEFAULT '',
  `path` VARCHAR(128),
  `score` DOUBLE NOT NULL DEFAULT '100',
  `description` TEXT COMMENT 'content data',
  `weight` DOUBLE NOT NULL DEFAULT 1,
  `inspection_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: category, 1: specification, 2: specification item',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: add, 1: modify, 2: delete',
  `refer_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for approval, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_standard_group_map`;
CREATE TABLE `eh_quality_inspection_standard_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `group_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: executive group, 2: review group',
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `inspector_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `create_time` DATETIME,
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_standard_specification_map`;
CREATE TABLE `eh_quality_inspection_standard_specification_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_equipment_inspection_standards',
  `specification_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of eh_quality_inspection_specifications',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for approval, 2: active',
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_standards`;
CREATE TABLE `eh_quality_inspection_standards` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `standard_number` VARCHAR(128),
  `name` VARCHAR(1024),
  `repeat_setting_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_repeat_settings',
  `description` TEXT COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `reviewer_uid` BIGINT NOT NULL DEFAULT 0,
  `review_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_task_attachments`;
CREATE TABLE `eh_quality_inspection_task_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `record_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_tasks',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_task_records`;
CREATE TABLE `eh_quality_inspection_task_records` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `task_id` BIGINT NOT NULL DEFAULT 0,
  `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who operates the task, organization, etc',
  `operator_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who is the target of processing the task, organization, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: inspect, 2: retify, 3: review, 4: assgin, 5: forward',
  `process_end_time` DATETIME,
  `process_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
  `process_message` TEXT,
  `process_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_task_templates`;
CREATE TABLE `eh_quality_inspection_task_templates` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `task_number` VARCHAR(128),
  `task_name` VARCHAR(1024),
  `task_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: verify task, 2: rectify task',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '0: parent task, others childrean-task',
  `child_count` BIGINT NOT NULL DEFAULT 0,
  `executive_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `executive_start_time` DATETIME,
  `executive_expire_time` DATETIME,
  `executive_time` DATETIME,
  `executor_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `executor_id` BIGINT NOT NULL DEFAULT 0,
  `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `operator_id` BIGINT NOT NULL DEFAULT 0,
  `process_expire_time` DATETIME,
  `process_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
  `process_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: waiting for executing, 2: rectifing, 3: rectified and waiting approval, 4: rectify closed and waiting approval, 5: closed',
  `result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay',
  `reviewer_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
  `reviewer_id` BIGINT NOT NULL DEFAULT 0,
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_time` DATETIME,
  `create_time` DATETIME,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_categories',
  `category_path` VARCHAR(128) COMMENT 'refernece to the path of eh_categories',
  `manual_flag` BIGINT NOT NULL DEFAULT 0 COMMENT '0: auto 1:manual',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `executive_position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_tasks`;
CREATE TABLE `eh_quality_inspection_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_standards',
  `task_number` VARCHAR(128),
  `task_name` VARCHAR(1024),
  `task_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: verify task, 2: rectify task',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '0: parent task, others childrean-task',
  `child_count` BIGINT NOT NULL DEFAULT 0,
  `executive_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `executive_start_time` DATETIME,
  `executive_expire_time` DATETIME,
  `executive_time` DATETIME,
  `executor_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `executor_id` BIGINT NOT NULL DEFAULT 0,
  `operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
  `operator_id` BIGINT NOT NULL DEFAULT 0,
  `process_expire_time` DATETIME,
  `process_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
  `process_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: waiting for executing, 2: rectifing, 3: rectified and waiting approval, 4: rectify closed and waiting approval, 5: closed',
  `result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay',
  `reviewer_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
  `reviewer_id` BIGINT NOT NULL DEFAULT 0,
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_time` DATETIME,
  `create_time` DATETIME,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_categories',
  `category_path` VARCHAR(128) COMMENT 'refernece to the path of eh_categories',
  `manual_flag` BIGINT NOT NULL DEFAULT 0 COMMENT '0: auto 1:manual',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `executive_position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`),		  
  KEY `standard_id` (`standard_id`),
  KEY `status` (`status`),
  KEY `target_id` (`target_id`),		
  KEY `executive_expire_time` (`executive_expire_time`),		
  KEY `process_expire_time` (`process_expire_time`),		
  KEY `operator_id` (`operator_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 目标选中的选项表, add by tt, 20170223
-- DROP TABLE IF EXISTS  `eh_questionnaire_answers`;
DROP TABLE IF EXISTS `eh_questionnaire_answers`;
CREATE TABLE `eh_questionnaire_answers` (
  `id` BIGINT NOT NULL,
  `questionnaire_id` BIGINT NOT NULL,
  `question_id` BIGINT NOT NULL,
  `option_id` BIGINT NOT NULL,
  `target_type` VARCHAR(32) COMMENT 'organization',
  `target_id` BIGINT NOT NULL,
  `target_name` VARCHAR(128),
  `option_content` VARCHAR(1024) COMMENT 'if question_type is blank, then this field has value',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_questionnaire_id` (`questionnaire_id`),
  KEY `i_question_id` (`question_id`),
  KEY `i_option_id` (`option_id`),
  KEY `i_target` (`target_type`,`target_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 选项表, add by tt, 20170223
-- DROP TABLE IF EXISTS  `eh_questionnaire_options`;
DROP TABLE IF EXISTS `eh_questionnaire_options`;
CREATE TABLE `eh_questionnaire_options` (
  `id` BIGINT NOT NULL,
  `questionnaire_id` BIGINT NOT NULL,
  `question_id` BIGINT NOT NULL,
  `option_name` VARCHAR(50),
  `option_uri` VARCHAR(1024),
  `checked_count` INTEGER,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_question_id` (`question_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 题目表, add by tt, 20170223
-- DROP TABLE IF EXISTS  `eh_questionnaire_questions`;
DROP TABLE IF EXISTS `eh_questionnaire_questions`;
CREATE TABLE `eh_questionnaire_questions` (
  `id` BIGINT NOT NULL,
  `questionnaire_id` BIGINT NOT NULL,
  `question_type` TINYINT COMMENT '1. radio， 2. check_box， 3. blank, 4. image_radio, 5. image_check_box',
  `question_name` VARCHAR(50) NOT NULL,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_questionnaire_id` (`questionnaire_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



-- 问卷调查表, add by tt, 20170223
-- DROP TABLE IF EXISTS  `eh_questionnaires`;
DROP TABLE IF EXISTS `eh_questionnaires`;
CREATE TABLE `eh_questionnaires` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'community',
  `owner_id` BIGINT,
  `questionnaire_name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(512),
  `collection_count` INTEGER,
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. draft, 2. active',
  `publish_time` DATETIME,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_recharge_info`;
CREATE TABLE `eh_recharge_info`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `bill_id` BIGINT,
  `plate_number` VARCHAR(20),
  `number_type` TINYINT COMMENT '0-car plate',
  `owner_name` VARCHAR(20) COMMENT 'plate number owner name',
  `recharge_userid` BIGINT,
  `recharge_username` VARCHAR(20) ,
  `recharge_phone` VARCHAR(20) ,
  `recharge_time` DATETIME,
  `recharge_month` TINYINT,
  `recharge_amount` DOUBLE,
  `old_validityperiod` DATETIME,
  `new_validityperiod` DATETIME,
  `payment_status` TINYINT COMMENT '3rd plat :0-fail 1-unpay 2-success',
  `recharge_status` TINYINT COMMENT '0-fail 1-waiting paying 2-refreshing data 3-success',
  `community_id` BIGINT,
  `card_type` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
-- the configurations recommended by admin
--
DROP TABLE IF EXISTS `eh_recommendation_configs`;
CREATE TABLE `eh_recommendation_configs` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `appId` BIGINT,
  `suggest_type` INTEGER NOT NULL DEFAULT 0,
  `source_type` INTEGER NOT NULL DEFAULT 0,
  `source_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` INTEGER NOT NULL DEFAULT 0,
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `period_type` INTEGER NOT NULL DEFAULT 0,
  `period_value` INTEGER NOT NULL DEFAULT 0,
  `status` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `start_time` DATETIME,
  `running_time` DATETIME,
  `expire_time` DATETIME,
  `embedded_json` TEXT,
  `description` VARCHAR(1024) DEFAULT '',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users partition
-- the result of recommendations
--
DROP TABLE IF EXISTS `eh_recommendations`;
CREATE TABLE `eh_recommendations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `appId` BIGINT,
  `suggest_type` INTEGER NOT NULL DEFAULT 0,
  `user_id` BIGINT NOT NULL DEFAULT 0,
  `source_type` INTEGER NOT NULL DEFAULT 0,
  `source_id` BIGINT NOT NULL DEFAULT 0,
  `embedded_json` TEXT,
  `max_count` INTEGER NOT NULL DEFAULT 0,
  `score` DOUBLE NOT NULL DEFAULT 0,
  `status` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `expire_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `fk_eh_recommendations_user_idx` (`user_id`),
  CONSTRAINT `fk_eh_recommendations_user_idx` FOREIGN KEY (`user_id`) REFERENCES `eh_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_region_codes`;
CREATE TABLE `eh_region_codes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64) NOT NULL COMMENT 'region name',
  `code` INTEGER NOT NULL COMMENT 'region code',
  `pinyin` VARCHAR(256) NOT NULL COMMENT 'region name pinyin',
  `first_letter` CHAR(2) NOT NULL COMMENT 'region name pinyin first letter',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active', 
  `hot_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no, 1: yes', 
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_regions`;
CREATE TABLE `eh_regions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `parent_id` BIGINT COMMENT 'id of the parent region',
  `name` VARCHAR(64),
  `pinyin_name` VARCHAR(64) COMMENT 'the full pinyin of the name',
  `pinyin_prefix` VARCHAR(64) COMMENT 'the prefix letter of every pinyin word',
  `path` VARCHAR(128) COMMENT 'path from the root',
  `level` INTEGER NOT NULL DEFAULT 0,
  `scope_code` TINYINT COMMENT '0 : country, 1: state/province, 2: city, 3: area, 4: neighborhood (community)',
  `iso_code` VARCHAR(32) COMMENT 'international standard code for the region if exists',
  `tel_code` VARCHAR(32) COMMENT 'primary telephone area code',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: inactive, 2: active, 3: locked, 4: mark as deleted',
  `hot_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not hot, 1: hot',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY(`id`),
  UNIQUE KEY `u_eh_region_name`(`namespace_id`, `parent_id`, `name`),
  KEY `i_eh_region_name_level`(`name`, `level`),
  KEY `i_eh_region_path`(`path`),
  KEY `i_eh_region_path_level`(`path`, `level`),
  KEY `i_eh_region_parent`(`parent_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_bill_attachments`;
CREATE TABLE `eh_rental_bill_attachments`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL COMMENT 'community id or organization id',
  `site_type` VARCHAR(128),
  `rental_bill_id` BIGINT,
  `attachment_type` TINYINT COMMENT '0:String 1:email 2:attachment file',
  `content` VARCHAR(500),
  `file_path` VARCHAR(500),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_bill_paybill_map`;
CREATE TABLE `eh_rental_bill_paybill_map`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL COMMENT 'community id or organization id',
  `site_type` VARCHAR(128),
  `rental_bill_id` BIGINT,
  `online_pay_bill_id` BIGINT ,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_bills`;
CREATE TABLE `eh_rental_bills`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL COMMENT 'community id or organization id',
  `site_type` VARCHAR(128),
  `rental_site_id` BIGINT NOT NULL COMMENT 'id',
  `rental_uid` BIGINT COMMENT 'rental user id',
  `rental_date` DATE COMMENT 'rental target date',
  `start_time` DATETIME COMMENT 'begin datetime unuse',
  `end_time` DATETIME COMMENT 'end datetime unuse',
  `rental_count` DOUBLE COMMENT 'amount of rental sites',
  `pay_total_money` DECIMAL(10,2) COMMENT 'total money ,include items and site',
  `site_total_money` DECIMAL(10,2),
  `reserve_money` DECIMAL(10,2) COMMENT 'total money * reserve ratio',
  `reserve_time` DATETIME COMMENT 'reserve time',
  `pay_start_time` DATETIME ,
  `pay_end_time` DATETIME,
  `pay_time` DATETIME,
  `cancel_time` DATETIME,
  `paid_money` DECIMAL(10,2) COMMENT'already paid money',
  `status` TINYINT COMMENT'0:wait for reserve, 1:paid reserve, 2:paid all money reserve success, 3:wait for final payment, 4:unlock reserve fail',
  `visible_flag` TINYINT DEFAULT 0 COMMENT '0:visible 1:unvisible',
  `invoice_flag` TINYINT DEFAULT 1 COMMENT '0:want invocie 1 no need',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_items_bills`;
CREATE TABLE `eh_rental_items_bills`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `community_id` BIGINT NOT NULL COMMENT 'enterprise, community id',
  `site_type` VARCHAR(128),
  `rental_bill_id` BIGINT,
  `rental_site_item_id` BIGINT,
  `rental_count` INTEGER,
  `total_money` DECIMAL(10,2),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_rules`;
CREATE TABLE `eh_rental_rules` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL COMMENT 'community id or organization id',
  `site_type` VARCHAR(20) COMMENT 'rule for what function',
  `rental_start_time` BIGINT,
  `rental_end_time` BIGINT,
  `pay_start_time` BIGINT,
  `pay_end_time` BIGINT,
  `payment_ratio` INTEGER COMMENT 'payment ratio',
  `refund_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: allow refund, 1: can not refund',
  `contact_num` VARCHAR(20) COMMENT 'phone number',
  `time_tag1` TIME,
  `time_tag2` TIME,
  `time_tag3` TIME,
  `date_tag1` DATE,
  `date_tag2` DATE,
  `date_tag3` DATE,
  `datetime_tag1` DATETIME,
  `datetime_tag2` DATETIME,
  `datetime_tag3` DATETIME,
  `integral_tag1` BIGINT ,
  `integral_tag2` BIGINT ,
  `integral_tag3` BIGINT ,
  `integral_tag4` BIGINT ,
  `string_tag1` VARCHAR(128) ,
  `string_tag2` VARCHAR(128) ,
  `string_tag3` VARCHAR(128) ,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',
  `rental_type` TINYINT COMMENT '0: as hour:min, 1-as half day 2-as day',
  `cancel_time` BIGINT,
  `overtime_time` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_site_items`;
CREATE TABLE `eh_rental_site_items`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `rental_site_id` BIGINT NOT NULL COMMENT 'rental_site id',
  `name` VARCHAR(128),
  `price` DECIMAL(10,2),
  `counts` INTEGER COMMENT 'item count',
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_site_rules`;
CREATE TABLE `eh_rental_site_rules`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL COMMENT 'community id or organization id',
  `site_type` VARCHAR(128),
  `rental_site_id` BIGINT NOT NULL COMMENT 'rental_site id',
  `rental_type` TINYINT COMMENT '0: as hour:min, 1: as half day, 2: as day',
  `amorpm` TINYINT COMMENT '0: am, 1: pm',
  `rental_step` INTEGER DEFAULT 1 COMMENT 'how many time_step must be rental every time',
  `begin_time` DATETIME,
  `end_time` DATETIME,
  `counts` DOUBLE COMMENT 'site count',
  `unit` DOUBLE COMMENT '1 or 0.5 basketball yard can rental half',
  `price` DECIMAL(10,2) COMMENT 'how much every step every unit',
  `site_rental_date` DATE COMMENT 'which day',
  `status` TINYINT COMMENT 'unuse 0:open, 1:closed',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',
  `time_step` DOUBLE,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_sites`;
CREATE TABLE `eh_rental_sites`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `parent_id` BIGINT ,
  `owner_id` BIGINT NOT NULL COMMENT 'community id or organization id',
  `site_type` VARCHAR(128),
  `site_name` VARCHAR(127),
  `site_type2` TINYINT,
  `building_name` VARCHAR(128) ,
  `building_id` BIGINT,
  `address` VARCHAR(128) ,
  `address_id` BIGINT,
  `spec` VARCHAR(255) COMMENT 'spec ,user setting ,maybe meetingroom seats ,KTV ROOM: big small VIP and so on',
  `own_company_name` VARCHAR(60) ,
  `contact_name` VARCHAR(40) ,
  `contact_phonenum` VARCHAR(20),
  `contact_phonenum2` VARCHAR(20),
  `contact_phonenum3` VARCHAR(20),
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',
  `introduction` TEXT,
  `notice` TEXT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rental_sites_bills`;
CREATE TABLE `eh_rental_sites_bills`(
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL COMMENT 'community id or organization id',
  `site_type` VARCHAR(128),
  `rental_bill_id` BIGINT,
  `rental_site_rule_id` BIGINT,
  `rental_count` DOUBLE,
  `total_money` DECIMAL(10,2),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 场所设定好的单元格表
--
DROP TABLE IF EXISTS `eh_rentalv2_cells`;
CREATE TABLE `eh_rentalv2_cells` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `rental_resource_id` BIGINT COMMENT 'rental_resource id',
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
  `amorpm` TINYINT COMMENT '0:am 1:pm 2:night',
  `rental_step` INTEGER COMMENT 'how many time_step must be rental every time',
  `begin_time` DATETIME COMMENT '开始时间 对于按时间定',
  `end_time` DATETIME COMMENT '结束时间 对于按时间定',
  `counts` DOUBLE COMMENT '共多少个',
  `unit` DOUBLE COMMENT '是否支持0.5个',
  `price` DECIMAL(10,2) COMMENT '折后价',
  `resource_rental_date` DATE COMMENT 'which day',
  `status` TINYINT COMMENT 'unuse 0:open 1:closed',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `time_step` DOUBLE,
  `original_price` DECIMAL(10,2) COMMENT '原价（如果不为null则price为打折价）',
  `exclusive_flag` TINYINT COMMENT '是否为独占资源0否 1 是',
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段 1是 0否',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `resource_number` VARCHAR(100)  COMMENT '场所号',
  `halfresource_price` DECIMAL(10,2) COMMENT '半场折后价',
  `halfresource_original_price` DECIMAL(10,2) COMMENT '半场原价（如果不为null则price为打折价）',
  `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源',
  `group_lock_flag` TINYINT COMMENT '一个资源被预约是否锁整个group,0-否,1-是',
  `org_member_original_price` DECIMAL(10,2) COMMENT '原价-如果打折则有(企业内部价)',
  `org_member_price` DECIMAL(10,2) COMMENT '实际价格-打折则为折后价(企业内部价)',
  `approving_user_original_price` DECIMAL(10,2) COMMENT '原价-如果打折则有（外部客户价）',
  `approving_user_price` DECIMAL(10,2) COMMENT '实际价格-打折则为折后价（外部客户价）',
  `half_org_member_original_price` DECIMAL(10,2) COMMENT '半场-原价-如果打折则有(企业内部价)',
  `half_org_member_price` DECIMAL(10,2) COMMENT '半场-实际价格-打折则为折后价(企业内部价)',
  `half_approving_user_original_price` DECIMAL(10,2) COMMENT '半场-原价-如果打折则有（外部客户价）',
  `half_approving_user_price` DECIMAL(10,2) COMMENT '半场-实际价格-打折则为折后价（外部客户价）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 保存默认设置的关闭时间
--
DROP TABLE IF EXISTS `eh_rentalv2_close_dates`;
CREATE TABLE `eh_rentalv2_close_dates` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT '"default_rule","resource_rule"',
  `close_date` DATE,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 保存场所附件设置
--
DROP TABLE IF EXISTS `eh_rentalv2_config_attachments`;
CREATE TABLE `eh_rentalv2_config_attachments` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT '"default_rule","resource_rule"',
  `attachment_type` TINYINT,
  `content` TEXT COMMENT '根据type，这里可能是文本或者附件url',
  `must_options` TINYINT COMMENT '0 非必须 1 必选',
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 保存一个公司的一个场所图标的默认设置
--
DROP TABLE IF EXISTS `eh_rentalv2_default_rules`;
CREATE TABLE `eh_rentalv2_default_rules` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `owner_type` VARCHAR(255) COMMENT 'owner type: community, organization',
  `owner_id` BIGINT COMMENT 'community id or organization id',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `rental_start_time` BIGINT COMMENT '最多提前多少时间预定',
  `rental_end_time` BIGINT COMMENT '最少提前多少时间预定',
  `pay_start_time` BIGINT,
  `pay_end_time` BIGINT,
  `payment_ratio` INTEGER COMMENT 'payment ratio',
  `refund_flag` TINYINT COMMENT '是否支持退款: 1-是, 0-否',
  `refund_ratio` INTEGER COMMENT '退款比例',
  `contact_num` VARCHAR(20) COMMENT 'phone number',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `rental_type` TINYINT COMMENT '0-as hour:min, 1-as half day, 2-as day, 3-支持晚上的半天',
  `cancel_time` BIGINT COMMENT '至少提前取消时间',
  `overtime_time` BIGINT COMMENT '超期时间',
  `exclusive_flag` TINYINT COMMENT '是否为独占资源: 0-否, 1-是',
  `unit` DOUBLE COMMENT '1-整租, 0.5-可半个租',
  `auto_assign` TINYINT COMMENT '是否动态分配: 1-是, 0-否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所: 1-是, 0-否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段: 1-是, 0-否',
  `cancel_flag` TINYINT COMMENT '是否允许取消: 1-是, 0-否',
  `rental_step` INTEGER COMMENT 'how many time_step must be rental every time',
  `need_pay` TINYINT COMMENT '是否需要支付: 1-是, 0-否',
  `workday_price` DECIMAL(10,2) COMMENT '工作日价格',
  `weekend_price` DECIMAL(10,2) COMMENT '周末价格',
  `resource_counts` DOUBLE COMMENT '可预约个数',
  `begin_date` DATE COMMENT '开始日期',
  `end_date` DATE COMMENT '结束日期',
  `open_weekday` VARCHAR(7) COMMENT '7位二进制，0000000每一位表示星期7123456',
  `time_step` DOUBLE COMMENT '步长，每个单元格是多少小时（半小时是0.5）',
  `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制',
  `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制',
  `org_member_workday_price` DECIMAL(10,2) COMMENT '企业内部工作日价格',
  `org_member_weekend_price` DECIMAL(10,2) COMMENT '企业内部节假日价格',
  `approving_user_workday_price` DECIMAL(10,2) COMMENT '外部客户工作日价格',
  `approving_user_weekend_price` DECIMAL(10,2) COMMENT '外部客户节假日价格',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 物品表
--
DROP TABLE IF EXISTS `eh_rentalv2_items`;
CREATE TABLE `eh_rentalv2_items` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `rental_resource_id` BIGINT COMMENT 'rental_resource id',
  `name` VARCHAR(128),
  `price` DECIMAL(10,2),
  `counts` INTEGER COMMENT 'item count',
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `default_order` INTEGER COMMENT '排序字段',
  `img_uri` VARCHAR(1024) COMMENT '图片uri',
  `item_type` TINYINT COMMENT '1购买型 2租用型',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 订单分表-物品订单表
--
DROP TABLE IF EXISTS `eh_rentalv2_items_orders`;
CREATE TABLE `eh_rentalv2_items_orders` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `rental_order_id` BIGINT,
  `rental_resource_item_id` BIGINT,
  `rental_count` INTEGER,
  `total_money` DECIMAL(10,2),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `item_name` VARCHAR(128),
  `img_uri` VARCHAR(1024),
  `item_type` TINYINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 订单附件表
--
DROP TABLE IF EXISTS `eh_rentalv2_order_attachments`;
CREATE TABLE `eh_rentalv2_order_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `rental_order_id` BIGINT,
  `attachment_type` TINYINT COMMENT '0:文本 1:车牌 2:显示内容 3：附件链接',
  `content` VARCHAR(500),
  `file_path` VARCHAR(500),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `resource_type_id` BIGINT COMMENT 'resource type id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 订单-支付关联表
-- 订单可能多次支付
--
DROP TABLE IF EXISTS `eh_rentalv2_order_payorder_map`;
CREATE TABLE `eh_rentalv2_order_payorder_map` (
  `id` BIGINT NOT NULL  COMMENT 'id',
  `order_id` BIGINT,
  `order_no` BIGINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `vendor_type` VARCHAR(255) COMMENT '支付方式,10001-支付宝，10002-微信',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 订单主表
--
DROP TABLE IF EXISTS `eh_rentalv2_orders`;
CREATE TABLE `eh_rentalv2_orders` (
  `id` BIGINT NOT NULL  COMMENT 'id',
  `order_no` VARCHAR(20) NOT NULL COMMENT '订单编号',
  `rental_resource_id` BIGINT NOT NULL  COMMENT 'id',
  `rental_uid` BIGINT COMMENT 'rental user id',
  `rental_date` DATE COMMENT '使用日期',
  `start_time` DATETIME COMMENT '使用开始时间',
  `end_time` DATETIME COMMENT '使用结束时间',
  `rental_count` DOUBLE COMMENT '预约数',
  `pay_total_money` DECIMAL(10,2) COMMENT '总价',
  `resource_total_money` DECIMAL(10,2),
  `reserve_money` DECIMAL(10,2),
  `reserve_time` DATETIME COMMENT 'reserve time',
  `pay_start_time` DATETIME,
  `pay_end_time` DATETIME,
  `pay_time` DATETIME,
  `cancel_time` DATETIME,
  `paid_money` DECIMAL(10,2),
  `status` TINYINT COMMENT '0:wait for reserve 1:paid reserve 2:paid all money reserve success 3:wait for final payment 4:unlock reserve fail',
  `visible_flag` TINYINT COMMENT '0:visible 1:unvisible',
  `invoice_flag` TINYINT COMMENT '0:want invocie 1 no need',
  `creator_uid` BIGINT COMMENT '预约人',
  `create_time` DATETIME COMMENT '下单时间',
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `resource_name` VARCHAR(255) COMMENT '名称',
  `use_detail` VARCHAR(255) COMMENT '使用时间',
  `vendor_type` VARCHAR(255) COMMENT '支付方式,10001-支付宝，10002-微信 --多次支付怎木办，估计产品都没想清楚',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `organization_id` BIGINT COMMENT '所属公司的ID',
  `spec` VARCHAR(255) COMMENT '规格',
  `address` VARCHAR(192) COMMENT '地址',
  `longitude` DOUBLE  COMMENT '地址经度',
  `latitude` DOUBLE  COMMENT '地址纬度',
  `contact_phonenum` VARCHAR(20) COMMENT '咨询电话',
  `introduction` TEXT COMMENT '详情',
  `notice` TEXT,
  `community_id` BIGINT COMMENT '资源所属园区的ID',
  `namespace_id` INTEGER COMMENT '域空间',
  `refund_flag` TINYINT COMMENT '是否支持退款 1是 0否',
  `refund_ratio` INTEGER COMMENT '退款比例',
  `cancel_flag` TINYINT COMMENT '是否允许取消 1是 0否',
  `reminder_time` DATETIME COMMENT '消息提醒时间',
  `pay_mode` TINYINT DEFAULT 0 COMMENT 'pay mode :0-online pay 1-offline',
  `offline_cashier_address` VARCHAR(200),
  `offline_payee_uid` BIGINT,
  `flow_case_id` BIGINT COMMENT 'id of the flow_case',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 订单-退款表
--
DROP TABLE IF EXISTS `eh_rentalv2_refund_orders`;
CREATE TABLE `eh_rentalv2_refund_orders` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `order_id` BIGINT COMMENT '订单id',
  `refund_order_no` BIGINT COMMENT '退款的refoundOrderNo-服务端退款时候生成',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `order_no` BIGINT COMMENT '支付的orderno-下单时候生成',
  `online_pay_style_no` VARCHAR(20) COMMENT '支付方式,alipay-支付宝,wechat-微信',
  `amount` DECIMAL(10,2) COMMENT '退款金额',
  `url` VARCHAR(1024) COMMENT '支付宝的退款链接',
  `status` TINYINT COMMENT '退款的状态，和订单状态保持一致',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 对于按小时预定的场所默认设置，保存时间段
--
DROP TABLE IF EXISTS `eh_rentalv2_resource_numbers`;
CREATE TABLE `eh_rentalv2_resource_numbers` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT 'EhRentalv2DefaultRules-默认规则,EhRentalv2Resources-具体场所',
  `resource_number` VARCHAR(255) COMMENT '场所编号',
  `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源',
  `group_lock_flag` TINYINT COMMENT '一个资源被预约是否锁整个group,0-否,1-是',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 订单分表-场所订单表
--
DROP TABLE IF EXISTS `eh_rentalv2_resource_orders`;
CREATE TABLE `eh_rentalv2_resource_orders` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `rental_order_id` BIGINT,
  `rental_resource_rule_id` BIGINT,
  `rental_count` DOUBLE,
  `total_money` DECIMAL(10,2),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `price` DECIMAL(10,2) COMMENT '折后价',
  `resource_rental_date` DATE COMMENT 'which day',
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
  `amorpm` TINYINT COMMENT '0:am 1:pm 2:night',
  `rental_step` INTEGER COMMENT 'how many time_step must be rental every time',
  `begin_time` DATETIME COMMENT '开始时间 对于按时间定',
  `end_time` DATETIME COMMENT '结束时间 对于按时间定',
  `exclusive_flag` TINYINT COMMENT '是否为独占资源0否 1 是',
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段 1是 0否',
  `status` TINYINT DEFAULT 0 COMMENT '状态 0-普通预定订单 1-不显示给用户的',
  
  PRIMARY KEY (`id`),
  KEY `i_eh_rental_order_rule_id` (`rental_resource_rule_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 保存场所详情图片
--
DROP TABLE IF EXISTS `eh_rentalv2_resource_pics`;
CREATE TABLE `eh_rentalv2_resource_pics` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT 'EhRentalv2Resources',
  `uri` VARCHAR(1024),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 场所和归属园区的关联表
--
DROP TABLE IF EXISTS `eh_rentalv2_resource_ranges`;
CREATE TABLE `eh_rentalv2_resource_ranges` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization',
  `owner_id` BIGINT COMMENT 'community id or organization id',
  `rental_resource_id` BIGINT COMMENT 'rental_resource id',
  PRIMARY KEY (`id`)

) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 资源类型表
--
DROP TABLE IF EXISTS `eh_rentalv2_resource_types`;
CREATE TABLE `eh_rentalv2_resource_types` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `name` VARCHAR(50) COMMENT '名称',
  `page_type` TINYINT COMMENT '预定展示0代表默认页面DefaultType, 1代表定制页面CustomType',
  `icon_uri` VARCHAR(1024) COMMENT '工作日价格',
  `status` TINYINT COMMENT '状态：0关闭 2开启',
  `namespace_id` INTEGER COMMENT '域空间',
  `pay_mode` TINYINT DEFAULT 0 COMMENT 'pay mode :0-online pay 1-offline',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 场所表
--
DROP TABLE IF EXISTS `eh_rentalv2_resources`;
CREATE TABLE `eh_rentalv2_resources` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `parent_id` BIGINT,
  `resource_name` VARCHAR(127) COMMENT '名称：',
  `resource_type2` TINYINT,
  `building_name` VARCHAR(128),
  `building_id` BIGINT,
  `address` VARCHAR(192) COMMENT '地址',
  `address_id` BIGINT,
  `spec` VARCHAR(255) COMMENT '规格',
  `own_company_name` VARCHAR(60),
  `contact_name` VARCHAR(40),
  `contact_phonenum` VARCHAR(20) COMMENT '咨询电话',
  `contact_phonenum2` VARCHAR(20),
  `contact_phonenum3` VARCHAR(20),
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `introduction` TEXT COMMENT '详情',
  `notice` TEXT,
  `charge_uid` BIGINT COMMENT '负责人id',
  `cover_uri` VARCHAR(1024) COMMENT '封面图uri',
  `discount_type` TINYINT COMMENT '折扣信息：0不打折 1满减优惠2比例折扣',
  `full_price` DECIMAL(10,2) COMMENT '满XX元',
  `cut_price` DECIMAL(10,2) COMMENT '减XX元',
  `discount_ratio` DOUBLE COMMENT '折扣比例',
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
  `time_step` DOUBLE COMMENT '按小时预约：最小单元格是多少小时，浮点型',
  `exclusive_flag` TINYINT COMMENT '是否为独占资源0否 1 是',
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段 1是 0否',
  `cancel_flag` TINYINT COMMENT '是否允许取消 1是 0否',
  `need_pay` TINYINT COMMENT '是否需要支付 1是 0否',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `cancel_time` BIGINT COMMENT '至少提前取消时间',
  `rental_start_time` BIGINT COMMENT '最多提前多少时间预定',
  `rental_end_time` BIGINT COMMENT '最少提前多少时间预定',
  `refund_flag` TINYINT COMMENT '是否支持退款 1是 0否',
  `refund_ratio` INTEGER COMMENT '退款比例',
  `longitude` DOUBLE COMMENT '地址经度',
  `latitude` DOUBLE COMMENT '地址纬度',
  `organization_id` BIGINT COMMENT '所属公司的ID',
  `day_begin_time` TIME COMMENT '对于按小时预定的每天开始时间',
  `day_end_time` TIME COMMENT '对于按小时预定的每天结束时间',
  `community_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `resource_counts` DOUBLE COMMENT '可预约个数',
  `cell_begin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'cells begin id',
  `cell_end_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'cells end id',
  `unit` DOUBLE DEFAULT 1 COMMENT '1-整租, 0.5-可半个租',
  `begin_date` DATE COMMENT '开始日期',
  `end_date` DATE COMMENT '结束日期',
  `open_weekday` VARCHAR(7) COMMENT '7位二进制，0000000每一位表示星期7123456',
  `workday_price` DECIMAL(10,2) COMMENT '工作日价格',
  `weekend_price` DECIMAL(10,2) COMMENT '周末价格',
  `avg_price_str` VARCHAR(1024) COMMENT '平均价格计算好的字符串',
  `confirmation_prompt` VARCHAR(200),
  `offline_cashier_address` VARCHAR(200),
  `offline_payee_uid` BIGINT,
  `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制',
  `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制',
  `org_member_workday_price` DECIMAL(10,2) COMMENT '企业内部工作日价格',
  `org_member_weekend_price` DECIMAL(10,2) COMMENT '企业内部节假日价格',
  `approving_user_workday_price` DECIMAL(10,2) COMMENT '外部客户工作日价格',
  `approving_user_weekend_price` DECIMAL(10,2) COMMENT '外部客户节假日价格',
  `default_order` BIGINT NOT NULL DEFAULT 0 COMMENT 'order',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- 对于按小时预定的场所默认设置，保存时间段
--
DROP TABLE IF EXISTS `eh_rentalv2_time_interval`;
CREATE TABLE `eh_rentalv2_time_interval` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT '"default_rule","resource_rule"',
  `begin_time` DOUBLE COMMENT '开始时间-24小时制',
  `end_time` DOUBLE COMMENT '结束时间-24小时制',
  `time_step` DOUBLE COMMENT '按小时预约：最小单元格是多少小时，浮点型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_repeat_settings`;
CREATE TABLE `eh_repeat_settings` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the setting, QA, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `forever_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true',
  `repeat_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'how many times of repeating before the end, 0:inactive, others as times',
  `start_date` DATE COMMENT 'the whole span of the event, including repeat time',
  `end_date` DATE COMMENT 'ineffective if forever_flag is set true, forever_flag/repeat_count/end_date are exclusive, only one is used',
  `time_ranges` VARCHAR(2048) COMMENT 'multiple time ranges in a day, json format, {"ranges":[{"startTime":"08:00:00","endTime":"09:30:00","duration":"3m"},{"startTime":"18:30:00","endTime":"19:30:00","duration":"2d"}]}',
  `repeat_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year',
  `repeat_interval` INTEGER NOT NULL DEFAULT 1 COMMENT 'every N day/week/month/year',
  `every_workday_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, only effective if repeat_type is by-day',
  `expression` VARCHAR(2048) COMMENT 'the expression for the repeat details, json format, should be parsed with repeat_type and repeat_interval',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 申请附件信息(通用，所有用模板进行申请带有的附件都放入此表)
DROP TABLE IF EXISTS `eh_request_attachments`;
CREATE TABLE `eh_request_attachments` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'owner resource(i.e. EhServiceAllianceApplies) type',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_field_name` VARCHAR(128) NOT NULL DEFAULT '',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 保存用户申请模板(通用，不仅限于服务联盟)
DROP TABLE IF EXISTS `eh_request_templates`;
CREATE TABLE `eh_request_templates` (
  `id` BIGINT NOT NULL,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `name` VARCHAR(128) NOT NULL,
  `button_title` VARCHAR(128) NOT NULL,
  `email_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no, 1: yes',
  `msg_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no, 1: yes',
  `fields_json` TEXT,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 模板和域空间映射表 没配的域空间表示支持所有模板 配了的则仅支持配了的部分
DROP TABLE IF EXISTS `eh_request_templates_namespace_mapping`;
CREATE TABLE `eh_request_templates_namespace_mapping` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `template_id` BIGINT NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_resource_categories`;
CREATE TABLE `eh_resource_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL COMMENT 'resource categry name',
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `path` VARCHAR(128) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 2: active',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  `type` TINYINT DEFAULT 1 COMMENT '1:分类, 2：子项目',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_resource_category_assignments`;
CREATE TABLE `eh_resource_category_assignments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `resource_categry_id` BIGINT NOT NULL COMMENT 'service categry id',
  `resource_type` VARCHAR(32),
  `resource_id` BIGINT,
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rich_texts`;
CREATE TABLE `eh_rich_texts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(128),
  `resource_type` VARCHAR(128) COMMENT 'about, introduction, agreement',
  `content_type` VARCHAR(128) COMMENT 'richtext, link',
  `content` TEXT,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 订单过期时间的设置表  add by yanjun 20170502
DROP TABLE IF EXISTS `eh_roster_order_settings`; 
CREATE TABLE `eh_roster_order_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT 'namespace id',
  `time` BIGINT COMMENT 'millisecond',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- Resource management
-- key table of rich text resource management sharding group
--
DROP TABLE IF EXISTS `eh_rtxt_resources`;
CREATE TABLE `eh_rtxt_resources`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `checksum` VARCHAR(128),
  `tile` TEXT,
  `author` TEXT,
  `description` TEXT,
  `cover_res_id` BIGINT,
  `store_type` VARCHAR(32) COMMENT 'content store type',
  `store_uri` VARCHAR(32) COMMENT 'identify the store instance',
  `content_type` VARCHAR(32) COMMENT 'object content type',
  `content_length` BIGINT,
  `content_uri` VARCHAR(1024) COMMENT 'object link info on storage',
  `reference_count` BIGINT,
  `create_time` DATETIME,
  `access_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `i_eh_rtxt_res_checksum` (`checksum`),
  KEY `i_eh_rtxt_res_create_time` (`create_time`),
  KEY `i_eh_rtxt_res_access_time` (`access_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_scene_types`;
CREATE TABLE `eh_scene_types` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL COMMENT 'the identifier of the scene type',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name used to display',
  `create_time` DATETIME,
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the parent id of scene, it is used to inherit something from the parent scene',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ns_scene` (`namespace_id`,`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
-- the operation promotion message send to user
--
DROP TABLE IF EXISTS `eh_schedule_task_logs`;
CREATE TABLE `eh_schedule_task_logs`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `task_id` BIGINT NOT NULL DEFAULT 0,
  `resource_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
  `resource_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `start_time` DATETIME,
  `end_time` DATETIME,
  `result_data` TEXT COMMENT 'the data need to keep after task finished',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
-- the operation promotion message send to user
--
DROP TABLE IF EXISTS `eh_schedule_tasks`;
CREATE TABLE `eh_schedule_tasks`(
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `resource_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
  `resource_id` BIGINT NOT NULL DEFAULT 0,
  `process_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'the count of process',
  `progress` INTEGER NOT NULL DEFAULT 0 COMMENT '0~100 percentage',
  `progress_data` TEXT COMMENT 'the data at the point of progress, it can recover the task if it interupted in the middle, json format',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: new, 2: on progress',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
-- <scope_type, scope_id> defines the scope that the configuration is applied to, for example,
--  <'system', NULL> may be used to identify a global system scope
--  <'community', cumminity-id> may be used to dentify a porticular community
--
--
DROP TABLE IF EXISTS `eh_scoped_configurations`;
CREATE TABLE `eh_scoped_configurations`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_id` BIGINT,
  `scope_type` VARCHAR(32),
  `scope_id` BIGINT,
  `item_name` VARCHAR(32),
  `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque json value, 1: entity',
  `item_value` TEXT,
  `target_type` VARCHAR(32),
  `target_id` BIGINT,
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: default, 1: override, 2: revert',

  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`),
  KEY `i_eh_scoped_cfg_combo` (`namespace_id`,`app_id`,`scope_type`,`scope_id`,`item_name`),
  KEY `i_eh_scoped_cfg_itag1` (`integral_tag1`),
  KEY `i_eh_scoped_cfg_itag2` (`integral_tag2`),
  KEY `i_eh_scoped_cfg_stag1` (`string_tag1`),
  KEY `i_eh_scoped_cfg_stag2` (`string_tag2`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_search_keywords`;
CREATE TABLE `eh_search_keywords`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `scope` VARCHAR(32),
  `scope_id` BIGINT,
  `keyword` VARCHAR(128),
  `weight` INTEGER,
  `frequency` INTEGER,
  `version` INTEGER,
  `update_time` DATETIME,
  `create_time` DATETIME COMMENT 'remove-deletion policy',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_kword_scoped_kword` (`scope`,`scope_id`,`keyword`),
  KEY `i_kword_weight_frequency` (`weight`,`frequency`),
  KEY `i_kword_update_time` (`update_time`),
  KEY `i_kword_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_search_types`;
CREATE TABLE `eh_search_types` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the type, community, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `content_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'search content type',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `create_time` DATETIME,
  `delete_time` DATETIME,
  `order` TINYINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_sequences`;
CREATE TABLE `eh_sequences` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `domain` VARCHAR(32) NOT NULL,
  `start_seq` BIGINT NOT NULL DEFAULT 1,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_server_shard_map`;
CREATE TABLE `eh_server_shard_map` (
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `server_id` INTEGER NOT NULL,
  `shard_id` INTEGER NOT NULL,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ssm_server_shard` (`server_id`,`shard_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `eh_servers`;
CREATE TABLE `eh_servers`(
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `master_id` INTEGER COMMENT 'master server id',
  `address_uri` VARCHAR(256),
  `address_port` INTEGER,
  `server_type` INTEGER NOT NULL DEFAULT 0 COMMENT '0: DB, 1: redis storage server, 2: redis cache server',
  `status` INTEGER NOT NULL DEFAULT 0 COMMENT '0 : disabled, 1: enabled',
  `config_tag` VARCHAR(32),
  `description` VARCHAR(256),

  PRIMARY KEY (`id`),
  KEY `i_eh_servers_config_tag`(`config_tag`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliance_apartment_requests`;
CREATE TABLE `eh_service_alliance_apartment_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `area_size` DOUBLE COMMENT 'area size',
  `remarks` VARCHAR(1024),
  `create_time` DATETIME,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliance_attachments`;
CREATE TABLE `eh_service_alliance_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id reference to eh_service_alliances',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `attachment_type` TINYINT DEFAULT 0 COMMENT '0: banner; 1: file attachment',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `download_count` INTEGER NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliance_categories`;
CREATE TABLE `eh_service_alliance_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `logo_url` VARCHAR(1024) COMMENT 'the logo url of the category',
  `display_mode` TINYINT DEFAULT 1,
  `display_destination` TINYINT DEFAULT 0 COMMENT '0: both, 1: client only, 2: browser only',
  `selected_logo_url` VARCHAR(1024) COMMENT 'the selected logo url of the category',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 服务联盟 表单 add by sw 20170322
DROP TABLE IF EXISTS `eh_service_alliance_golf_requests`;
CREATE TABLE `eh_service_alliance_golf_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `organization_floor` INTEGER,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_alliance_gym_requests`;
CREATE TABLE `eh_service_alliance_gym_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `profession` VARCHAR(128),
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_alliance_invest_requests`;
CREATE TABLE `eh_service_alliance_invest_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '',
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `industry` VARCHAR(128),
  `financing_amount` DECIMAL(10,2),
  `invest_period` INTEGER,
  `annual_yield` DECIMAL(10,2),
  `remarks` VARCHAR(1024),
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliance_jump_module`;
CREATE TABLE `eh_service_alliance_jump_module` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '',
  `module_url` VARCHAR(512),
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 保存服务联盟大类下设置的推送邮箱和推送消息的管理员信息
DROP TABLE IF EXISTS `eh_service_alliance_notify_targets`;
CREATE TABLE `eh_service_alliance_notify_targets` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL,
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number or email address',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 服务联盟模板申请信息
DROP TABLE IF EXISTS `eh_service_alliance_requests`;
CREATE TABLE `eh_service_alliance_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `city_name` VARCHAR(128),
  `industry` VARCHAR(128),
  `financing_stage` VARCHAR(32),
  `financing_amount` DECIMAL(10,2),
  `transfer_shares` DOUBLE,
  `project_desc` TEXT,
  `create_time` DATETIME,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '',
  `remarks` VARCHAR(1024),
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliance_reservation_requests`;
CREATE TABLE `eh_service_alliance_reservation_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `reserve_type` VARCHAR(128),
  `reserve_organization` VARCHAR(128),
  `reserve_time` VARCHAR(128),
  `contact` VARCHAR(128),
  `mobile` VARCHAR(128),
  `remarks` VARCHAR(1024),
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliance_server_requests`;
CREATE TABLE `eh_service_alliance_server_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `email` VARCHAR(128),
  `destination` VARCHAR(128),
  `departure_city` VARCHAR(128),
  `departure_date` INTEGER,
  `departure_days` INTEGER,
  `estimated_cost` INTEGER,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_alliance_skip_rule`;
CREATE TABLE `eh_service_alliance_skip_rule` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_alliance_category_id` BIGINT NOT NULL DEFAULT 0,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliances`;
CREATE TABLE `eh_service_alliances` (
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'organization name',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '',
  `type` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id reference to eh_service_alliance_categories',
  `address` VARCHAR(255) NOT NULL DEFAULT '',
  `contact` VARCHAR(64),
  `description` TEXT,
  `poster_uri` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `default_order` BIGINT COMMENT 'default value is id',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),
  `discount` BIGINT,
  `category_id` BIGINT,
  `contact_name` VARCHAR(128),
  `contact_mobile` VARCHAR(128),
  `service_type` VARCHAR(128),
  `service_url` VARCHAR(128),
  `discount_desc` VARCHAR(128),
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `module_url` VARCHAR(512),
  `contact_memid` BIGINT,
  `support_type` TINYINT NOT NULL DEFAULT 2 COMMENT 'APP:0, WEB:1, APP_WEB: 2',
  `button_title` VARCHAR(64),
  `description_height` INTEGER DEFAULT 2 COMMENT '0:not collapse , N: collapse N lines',
  `display_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0:hide,1:display',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 
-- 服务热线配置表
-- 
DROP TABLE IF EXISTS `eh_service_configurations`;
CREATE TABLE `eh_service_configurations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `owner_type` VARCHAR(64) COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT DEFAULT 0,
  `name` VARCHAR(64),
  `value` VARCHAR(64),
  `description` VARCHAR(256),
  `namespace_id` INTEGER DEFAULT 0,
  `display_name` VARCHAR(128),
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_config` (`owner_type`,`owner_id`,`name`,`value`,`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 
-- 服务热线表
-- 
DROP TABLE IF EXISTS `eh_service_hotlines`;
CREATE TABLE `eh_service_hotlines` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `namespace_id` INTEGER DEFAULT 0,
  `owner_type` VARCHAR(64) COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT DEFAULT 0,
  `service_type` INTEGER COMMENT '1-公共热线 2-专属客服 4- 8-',
  `name` VARCHAR(64) COMMENT '热线/客服名称',
  `contact` VARCHAR(64) COMMENT '热线/客服 联系电话',
  `user_id` BIGINT COMMENT '客服 userid',
  `description` VARCHAR(400) COMMENT '客服 描述',
  `default_order` INTEGER DEFAULT 0 COMMENT '排序字段',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_module_assignments`;
CREATE TABLE `eh_service_module_assignments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL COMMENT 'organization id',
  `target_type` VARCHAR(32) NOT NULL COMMENT 'organization user',
  `target_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL,
  `module_id` BIGINT NOT NULL,
  `create_uid` BIGINT NOT NULL,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `assignment_type` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_module_privileges`;
CREATE TABLE `eh_service_module_privileges` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `module_id` BIGINT NOT NULL COMMENT 'service module id',
  `privilege_type` TINYINT NOT NULL COMMENT '0: general, 1: super',
  `privilege_id` BIGINT NOT NULL COMMENT 'privilege id',
  `remark` VARCHAR(128) COMMENT 'remark',
  `default_order` INTEGER COMMENT 'order number',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_module_scopes`;
CREATE TABLE `eh_service_module_scopes` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_id` BIGINT,
  `module_name` VARCHAR(64),
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `default_order` INTEGER COMMENT 'order number',
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: delete , 1: override, 2: revert',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_modules`;
CREATE TABLE `eh_service_modules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(64),
  `parent_id` BIGINT NOT NULL,
  `path` VARCHAR(128) NOT NULL,
  `type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: park, 1: organization, 2:manager',
  `level` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',
  `default_order` INTEGER COMMENT 'order number',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 入驻申请信息
DROP TABLE IF EXISTS `eh_settle_requests`;
CREATE TABLE `eh_settle_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `create_time` DATETIME,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '',
  `remarks` VARCHAR(1024),
  `string_tag1` VARCHAR(256),
  `integral_tag1` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_shards`;
CREATE TABLE `eh_shards`(
  `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `sharding_domain` VARCHAR(64) NOT NULL,
  `anchor` BIGINT,
  `create_time` DATETIME COMMENT 'time that shard has been created',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_shards_domain_anchor` (`sharding_domain`,`anchor`),
  KEY `i_eh_shards_anchor` (`anchor`),
  KEY `i_eh_shards_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_sms_logs`;
CREATE TABLE `eh_sms_logs`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `scope` VARCHAR(64),
  `code` INTEGER,
  `locale` VARCHAR(16),
  `mobile` VARCHAR(128),
  `text` TEXT,
  `variables` VARCHAR(512),
  `result` TEXT,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_source_account`;
CREATE TABLE `eh_source_account` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `source_account` VARCHAR(20),
  `password` VARCHAR(20),
  `conf_tpye` TINYINT COMMENT '0-25方仅视频 1-25方支持电话 2-100方仅视频 3-100方支持电话',
  `valid_date` DATETIME,
  `valid_flag` TINYINT COMMENT '0-invalid 1-valid',
  `status` TINYINT COMMENT '0-available 1-occupied',
  `occupy_account_id` BIGINT,
  `conf_id` INTEGER,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- add by wh 20161011
-- 用户活动率计算表
DROP TABLE IF EXISTS `eh_stat_active_users`;
CREATE TABLE `eh_stat_active_users` (
  `id` BIGINT NOT NULL,
  `stat_date` DATE COMMENT '统计日期',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `active_count` INTEGER NOT NULL DEFAULT 0 COMMENT '活动人数',
  `total_count` INTEGER NOT NULL DEFAULT 0 COMMENT '总人数-当天的',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique` (`stat_date`,`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 订单交易流水表
DROP TABLE IF EXISTS `eh_stat_orders`;
CREATE TABLE `eh_stat_orders` (
  `id` BIGINT NOT NULL,
  `community_id` BIGINT DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `order_date` VARCHAR(20) COMMENT '处理成日期 比如2016-07-09',
  `resource_type` VARCHAR(64) COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) COMMENT '来源实体店ID',
  `payer_uid` BIGINT COMMENT '支付用户编号',
  `ware_json` TEXT COMMENT '商品',
  `vendor_code` VARCHAR(64) COMMENT '供应商编号',
  `order_no` VARCHAR(100) COMMENT '订单号',
  `order_type` VARCHAR(64) COMMENT '订单类型  transaction refund',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单金额',
  `status` TINYINT NOT NULL DEFAULT 0,
  `shop_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1-platform shop,2-self shop',
  `order_time` DATETIME,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 结算退款流水表
DROP TABLE IF EXISTS `eh_stat_refunds`;
CREATE TABLE `eh_stat_refunds` (
  `id` BIGINT NOT NULL,
  `community_id` BIGINT DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `refund_date` VARCHAR(20) COMMENT '处理成日期 比如2016-07-09',
  `service_type` VARCHAR(64) COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` VARCHAR(64) COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) COMMENT '来源实体店ID',
  `paid_channel` TINYINT COMMENT '支付渠道类型 0支付宝 1微信',
  `ware_json` TEXT COMMENT '商品',
  `payer_uid` BIGINT COMMENT '支付用户编号',
  `refund_no` VARCHAR(100) COMMENT '平台退款流水号',
  `order_no` VARCHAR(100) COMMENT '订单号',
  `vendor_refund_no` VARCHAR(100) COMMENT '第三方退款流水号',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单金额',
  `refund_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '退款金额',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `refund_time` DATETIME,
  `update_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_stat_service`;
CREATE TABLE `eh_stat_service` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT,
  `service_type` VARCHAR(64) NOT NULL,
  `service_name` VARCHAR(64),
  `status` TINYINT DEFAULT 0 COMMENT '0 无效 1 正常',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 结算統計結果表
DROP TABLE IF EXISTS `eh_stat_service_settlement_results`;
CREATE TABLE `eh_stat_service_settlement_results` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT 0,
  `community_id` BIGINT DEFAULT 0,
  `paid_date` VARCHAR(20) COMMENT '处理成日期 比如2016-07-09',
  `service_type` VARCHAR(64) COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` VARCHAR(64) COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) COMMENT '来源实体店ID',
  `alipay_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付寶支付金額',
  `alipay_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付寶退款金額',
  `wechat_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '微信支付金額',
  `wechat_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '微信退款金額',
  `payment_card_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '一卡通交易金額',
  `payment_card_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '一卡通退款金額',
  `total_paid_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易总金额',
  `total_refund_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易总金额',
  `update_time` DATETIME,
  `create_time` DATETIME,
  `alipay_paid_count` BIGINT DEFAULT 0 COMMENT '支付宝消费笔数',
  `alipay_refund_count` BIGINT DEFAULT 0 COMMENT '支付宝退款笔数',
  `wechat_paid_count` BIGINT DEFAULT 0 COMMENT '微信消费笔数',
  `wechat_refund_count` BIGINT DEFAULT 0 COMMENT '微信退款笔数',
  `payment_card_paid_count` BIGINT DEFAULT 0 COMMENT '一卡通消费笔数',
  `payment_card_refund_count` BIGINT DEFAULT 0 COMMENT '一卡通退款笔数',
  `total_paid_count` BIGINT DEFAULT 0 COMMENT '总消费笔数',
  `total_refund_count` BIGINT DEFAULT 0 COMMENT '总退款消费笔数',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 结算支付退款详情表
DROP TABLE IF EXISTS `eh_stat_settlements`;
CREATE TABLE `eh_stat_settlements` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT 0,
  `community_id` BIGINT DEFAULT 0,
  `paid_date` VARCHAR(20) COMMENT '处理成日期 比如2016-07-09',
  `service_type` VARCHAR(64) COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` VARCHAR(64) COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) COMMENT '来源实体店ID',
  `paid_channel` TINYINT COMMENT '支付渠道类型 0支付宝 1微信',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单总金额',
  `paid_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易总金额',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易总结算金额，交易总金额-交易总手续费',
  `paid_count` BIGINT NOT NULL DEFAULT 0 COMMENT '交易总笔数',
  `refund_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '退款总金额',
  `refund_fee_rate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '退款费率',
  `refund_fee_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '退款总手续费',
  `refund_settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '退款总结算金额，退款总金额-退款总手续费',
  `refund_count` BIGINT NOT NULL DEFAULT 0 COMMENT '退款总笔数',
  `update_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_stat_task_logs`;
CREATE TABLE `eh_stat_task_logs` (
  `id` BIGINT NOT NULL,
  `task_no` VARCHAR(20) NOT NULL,
  `status` TINYINT COMMENT '10 同步物业缴费订单到结算订单表 20 同步电商订单订单到结算订单表 30 同步停车充值订单到结算订单表 40 同步一卡通订单到结算订单表 50 同步支付平台交易流水到结算交易流水表 60 同步一卡通交易流水到结算交易流水表 70 同步支付平台退款流水到结算退款流水表 80 同步一卡通退款流水到结算退款流水表 90 生成结算数据 100 生成结算数据结果 110 完成',
  `islock` TINYINT DEFAULT 0 COMMENT '0 未锁 1 有锁',
  `update_Time` DATETIME,
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 结算交易流水表
DROP TABLE IF EXISTS `eh_stat_transactions`;
CREATE TABLE `eh_stat_transactions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT 0,
  `community_id` BIGINT DEFAULT 0,
  `paid_date` VARCHAR(20) COMMENT '处理成日期 比如2016-07-09',
  `service_type` VARCHAR(64) COMMENT '0 左邻小站 1其他店铺 3第三方服务 4社区服务',
  `resource_type` VARCHAR(64) COMMENT '交易来源类型 0电商 1停车充值 2资源预定 3物业缴费',
  `resource_id` VARCHAR(64) COMMENT '来源实体店ID',
  `ware_json` TEXT COMMENT '商品',
  `vendor_code` VARCHAR(64) COMMENT '供应商编号',
  `payer_uid` BIGINT COMMENT '支付用户编号',
  `transaction_no` VARCHAR(100) COMMENT '平台流水号',
  `vendor_transaction_no` VARCHAR(100) COMMENT '第三方支付流水号',
  `order_no` VARCHAR(100) COMMENT '订单号',
  `order_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '订单金额',
  `paid_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '支付金额',
  `paid_channel` TINYINT COMMENT '支付渠道类型 0支付宝 1微信',
  `paid_account` VARCHAR(100) COMMENT '第三方支付账号 ',
  `paid_type` TINYINT NOT NULL DEFAULT 1 COMMENT '支付类型 二维码支付 等。。 ',
  `fee_rate` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易费率',
  `fee_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '交易总手续费',
  `settlement_amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT '结算金额',
  `paid_status` TINYINT NOT NULL DEFAULT 0 COMMENT '支付状态',
  `paid_time` DATETIME,
  `update_time` DATETIME,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_state_triggers`;
CREATE TABLE `eh_state_triggers` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `object_type` VARCHAR(32),
  `object_id` BIGINT,
  `trigger_state` INTEGER,
  `flow_type` INTEGER,
  `flow_data` TEXT,
  `order` INTEGER,
  `create_time` DATETIME COMMENT 'remove-deletion policy, it is used to control program logic, makes more sense to just remove it',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
-- for compatibility reason, this table is basically cloned from old DB
--
DROP TABLE IF EXISTS `eh_stats_by_city`;
CREATE TABLE `eh_stats_by_city` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `city_id` BIGINT COMMENT 'id in eh_regions table of the city',
  `stats_date` VARCHAR(32),
  `stats_type` INTEGER,
  `reg_user_count` BIGINT,
  `addr_user_count` BIGINT,
  `pending_user_count` BIGINT,
  `community_count` BIGINT,
  `apt_count` BIGINT,
  `pending_apt_count` BIGINT,
  `post_count` BIGINT,
  `post_comment_count` BIGINT,
  `post_like_count` BIGINT,
  `create_time` DATETIME,
  `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_stats_city_report` (`city_id`,`stats_date`,`stats_type`),
  KEY `u_stats_delete_time` (`delete_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_suggestions`;
CREATE TABLE `eh_suggestions` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `SUGGEST_TYPE` INTEGER NOT NULL DEFAULT 0,
  `USER_ID` BIGINT NOT NULL DEFAULT 0,
  `TARGET_TYPE` INTEGER NOT NULL DEFAULT 0,
  `TARGET_ID` BIGINT NOT NULL DEFAULT 0,
  `REASON_JSON` VARCHAR(1024) DEFAULT '',
  `MAX_COUNT` INTEGER NOT NULL DEFAULT 0,
  `SCORE` DOUBLE NOT NULL DEFAULT 0,
  `STATUS` INTEGER NOT NULL DEFAULT 0,
  `CREATE_TIME` DATETIME,
  `EXPIRED_TIME` VARCHAR(64) NOT NULL DEFAULT 0,

  PRIMARY KEY (`ID`),
  KEY `fk_eh_suggestions_user_idx` (`USER_ID`),
  CONSTRAINT `fk_eh_suggestions_user_idx` FOREIGN KEY (`USER_ID`) REFERENCES `eh_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_talent_categories`;
CREATE TABLE `eh_talent_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `name` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_talent_query_histories`;

CREATE TABLE `eh_talent_query_histories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `keyword` VARCHAR(64),
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_talents`;
CREATE TABLE `eh_talents` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `name` VARCHAR(64),
  `avatar_uri` VARCHAR(2048),
  `phone` VARCHAR(32),
  `gender` TINYINT,
  `position` VARCHAR(64),
  `category_id` BIGINT,
  `experience` INTEGER,
  `graduate_school` VARCHAR(64),
  `degree` TINYINT,
  `remark` TEXT,
  `enabled` TINYINT,
  `default_order` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 科技园同步数据备份表，add by tt, 20161212
DROP TABLE IF EXISTS `eh_techpark_syncdata_backup`;
CREATE TABLE `eh_techpark_syncdata_backup` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `data_type` TINYINT NOT NULL COMMENT '1: building, 2: apartment, 3: contract',
  `all_flag` TINYINT NOT NULL COMMENT '1: all data, 0: new insert or update',
  `next_page` INTEGER COMMENT 'next page',
  `var_data_list` LONGTEXT COMMENT 'insert or update data',
  `del_data_list` LONGTEXT COMMENT 'delete data',
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_templates`;
CREATE TABLE `eh_templates`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(64),
  `path` VARCHAR(256),
  `type` TINYINT,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_template_name`(`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 终端app版本活跃用户
DROP TABLE IF EXISTS `eh_terminal_app_version_actives`;
CREATE TABLE `eh_terminal_app_version_actives` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_version_realm` VARCHAR(128),
  `app_version` VARCHAR(128),
  `imei_number` VARCHAR(128) DEFAULT '',
  `date` VARCHAR(32),
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 运营统计 by sfyan 20161214
-- 终端app版本累计用户
DROP TABLE IF EXISTS `eh_terminal_app_version_cumulatives`;
CREATE TABLE `eh_terminal_app_version_cumulatives` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_version_realm` VARCHAR(128),
  `app_version` VARCHAR(128),
  `imei_number` VARCHAR(128) DEFAULT '',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 终端app版本统计
DROP TABLE IF EXISTS `eh_terminal_app_version_statistics`;
CREATE TABLE `eh_terminal_app_version_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_version_realm` VARCHAR(128),
  `app_version` VARCHAR(128),
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative of active users',
  `version_cumulative_rate` DECIMAL(10,2) NOT NULL,
  `version_active_rate` DECIMAL(10,2) NOT NULL,
  `date` VARCHAR(32),
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 终端日统计
DROP TABLE IF EXISTS `eh_terminal_day_statistics`;
CREATE TABLE `eh_terminal_day_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `start_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `new_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `cumulative_change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_rate` DECIMAL(10,2) NOT NULL COMMENT 'active_user_number/accumulative_user_number',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative number of users',
  `seven_active_user_number` BIGINT NOT NULL COMMENT '7 active number of users',
  `thirty_active_user_number` BIGINT NOT NULL COMMENT '30 active number of users',
  `date` VARCHAR(32),
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 终端时统计
DROP TABLE IF EXISTS `eh_terminal_hour_statistics`;
CREATE TABLE `eh_terminal_hour_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `new_user_number` BIGINT NOT NULL COMMENT 'number of new users',
  `active_user_number` BIGINT NOT NULL COMMENT 'number of active users',
  `change_rate` DECIMAL(10,2) NOT NULL COMMENT 'today compared to yesterdays rate of change.',
  `active_rate` DECIMAL(10,2) NOT NULL COMMENT 'active_user_number/accumulative_user_number',
  `start_number` BIGINT NOT NULL COMMENT 'number of starts',
  `cumulative_user_number` BIGINT NOT NULL COMMENT 'cumulative number of users',
  `date` VARCHAR(32),
  `hour` VARCHAR(16),
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 统计任务记录
DROP TABLE IF EXISTS `eh_terminal_statistics_tasks`;
CREATE TABLE `eh_terminal_statistics_tasks` (
  `id` BIGINT NOT NULL,
  `task_no` VARCHAR(20) NOT NULL,
  `status` TINYINT COMMENT '10 生成日统计数据 20 生成时统计数据 30 生成版本统计数据 100 完成',
  `update_Time` DATETIME,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_no` (`task_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_thirdpart_users`;
CREATE TABLE `eh_thirdpart_users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `vendor_tag` VARCHAR(64),
  `name` VARCHAR(128) COMMENT 'name of user',
  `phone` VARCHAR(64) COMMENT 'phone of user',
  `city_name` VARCHAR(64) COMMENT 'city',
  `area_name` VARCHAR(64) COMMENT 'area',
  `community_names` TEXT COMMENT 'community name, split with comma if there are multiple communties',
  `building_name` VARCHAR(128),
  `unit_name` VARCHAR(64),
  `apartment_name` VARCHAR(64),
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_user_activities`;
CREATE TABLE `eh_user_activities` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `uid` BIGINT NOT NULL DEFAULT 0,
  `activity_type` TINYINT NOT NULL DEFAULT 0,
  `app_version_code` BIGINT NOT NULL DEFAULT 0,
  `app_version_name` VARCHAR(128) DEFAULT '',
  `channel_id` BIGINT NOT NULL DEFAULT 0,
  `imei_number` VARCHAR(128) DEFAULT '',
  `device_type` VARCHAR(512) DEFAULT '',
  `os_info` VARCHAR(512) DEFAULT '',
  `os_type` TINYINT NOT NULL DEFAULT 0,
  `mkt_data_version` BIGINT NOT NULL DEFAULT 0,
  `report_config_version` BIGINT NOT NULL DEFAULT 0,
  `internal_ip` VARCHAR(128) DEFAULT '',
  `external_ip` VARCHAR(128) DEFAULT '',
  `user_agent` VARCHAR(1024) DEFAULT '',
  `collect_time_ms` BIGINT NOT NULL DEFAULT 0,
  `report_time_ms` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `version_realm` VARCHAR(128),
  
  PRIMARY KEY  (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS  `eh_user_behaviors`;
CREATE TABLE `eh_user_behaviors`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `uid` BIGINT NOT NULL DEFAULT 0,
  `content_type` TINYINT NOT NULL DEFAULT 0,
  `content` TEXT,
  `collect_time_ms` BIGINT NOT NULL DEFAULT 0,
  `report_time_ms` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

   PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_user_blacklist`;
CREATE TABLE `eh_user_blacklist` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `target_type` VARCHAR(32),
  `target_id` BIGINT,
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_blk_owner_target` (`owner_uid`,`target_type`,`target_id`),
  KEY `i_eh_usr_blk_owner` (`owner_uid`),
  KEY `i_eh_usr_blk_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users sharding group
--
-- DROP TABLE IF EXISTS `eh_user_blacklist`;
-- CREATE TABLE `eh_user_blacklist` (
--   `id` BIGINT NOT NULL COMMENT 'id of the record',
--   `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
--   `target_type` VARCHAR(32),
--   `target_id` BIGINT,
--   `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

--   PRIMARY KEY (`id`),
--   UNIQUE KEY `u_eh_usr_blk_owner_target`(`owner_uid`, `target_type`, `target_id`),
--   KEY `i_eh_usr_blk_owner`(`owner_uid`),
--   KEY `i_eh_usr_blk_create_time`(`create_time`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_user_blacklists`;
CREATE TABLE `eh_user_blacklists` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `scope_type` VARCHAR(64),
  `scope_id` BIGINT,
  `owner_uid` BIGINT NOT NULL,
  `contact_type` TINYINT NOT NULL DEFAULT 0,
  `contact_token` VARCHAR(128) DEFAULT '',
  `contact_name` VARCHAR(64),
  `gender` TINYINT DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: confirming, 2: active',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users partition
-- Used for duplicated recording of group membership that user is involved in order to store
-- it in the same shard as of its owner user
--
DROP TABLE IF EXISTS `eh_user_communities`;
CREATE TABLE `eh_user_communities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `community_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'redendant info for quickly distinguishing associated community',
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `join_policy` TINYINT NOT NULL DEFAULT 1 COMMENT '1: register, 2: request to join',
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_community`(`owner_uid`, `community_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS  `eh_user_contacts`;
CREATE TABLE `eh_user_contacts`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `uid` BIGINT NOT NULL DEFAULT 0,
  `contact_id` BIGINT NOT NULL DEFAULT 0,
  `contact_phone` VARCHAR(32) DEFAULT '',
  `contact_name` VARCHAR(128) DEFAULT '',
  `create_time` DATETIME,

  PRIMARY KEY  (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users sharding group
--
DROP TABLE IF EXISTS `eh_user_favorites`;
CREATE TABLE `eh_user_favorites` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `target_type` VARCHAR(32),
  `target_id` BIGINT,
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_favorite_target`(`owner_uid`, `target_type`, `target_id`),
  KEY `i_eh_usr_favorite_owner`(`owner_uid`),
  KEY `i_eh_usr_favorite_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users sharding group
-- secondary resource objects (after eh_users)
--
DROP TABLE IF EXISTS `eh_user_followed_families`;
CREATE TABLE `eh_user_followed_families`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL,
  `followed_family` BIGINT NOT NULL,
  `alias_name` VARCHAR(64),

  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `i_eh_usr_ffmy_followed`(`owner_uid`, `followed_family`),
  KEY `i_eh_usr_ffmy_owner`(`owner_uid`),
  KEY `i_eh_usr_ffmy_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users partition
-- Used for duplicated recording of group membership that user is involved in order to store
-- it in the same shard as of its owner user
--
DROP TABLE IF EXISTS `eh_user_group_histories`;
CREATE TABLE `eh_user_group_histories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `group_discriminator` VARCHAR(32) COMMENT 'redendant info for quickly distinguishing associated group',
  `group_id` BIGINT,
  `community_id` BIGINT,
  `address_id` BIGINT,
  `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users partition
-- Used for duplicated recording of group membership that user is involved in order to store
-- it in the same shard as of its owner user
--
DROP TABLE IF EXISTS `eh_user_groups`;
CREATE TABLE `eh_user_groups` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `group_discriminator` VARCHAR(32) COMMENT 'redendant info for quickly distinguishing associated group',
  `group_id` BIGINT,
  `region_scope` TINYINT COMMENT 'redundant group info to help region-based group user search',
  `region_scope_id` BIGINT COMMENT 'redundant group info to help region-based group user search',

  `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'default to ResourceUser role',
  `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance, 3: active',
  `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_grp_owner_group`(`owner_uid`, `group_id`),
  KEY `i_eh_usr_grp_owner`(`owner_uid`),
  KEY `i_eh_usr_grp_create_time`(`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- member of eh_users partition
--
DROP TABLE IF EXISTS `eh_user_identifiers`;
CREATE TABLE `eh_user_identifiers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `identifier_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `identifier_token` VARCHAR(128),
  `verification_code` VARCHAR(16),
  `claim_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: free standing, 1: claiming, 2: claim verifying, 3: claimed',
  `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
  `notify_time` DATETIME,

  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `region_code` INTEGER DEFAULT '86' COMMENT 'region code 86 852',
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_user_idf_owner_type_token`(`owner_uid`, `identifier_type`, `identifier_token`),
  KEY `i_eh_user_idf_owner`(`owner_uid`),
  KEY `i_eh_user_idf_type_token`(`identifier_type`, `identifier_token`),
  KEY `i_eh_user_idf_create_time`(`create_time`),
  KEY `i_eh_user_idf_notify_time` (`notify_time`),
  KEY `i_eh_user_owner_status` (`owner_uid`,`claim_status`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
-- Pretending someone to login and operate as real user
--
DROP TABLE IF EXISTS  `eh_user_impersonations`;
CREATE TABLE `eh_user_impersonations`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'USER',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(64) NOT NULL COMMENT 'USER',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `description` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS  `eh_user_installed_apps`;
CREATE TABLE `eh_user_installed_apps`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `uid` BIGINT NOT NULL DEFAULT 0,
  `app_name` VARCHAR(1024) DEFAULT '',
  `app_version` VARCHAR(128) DEFAULT '',
  `app_size` VARCHAR(128) DEFAULT '',
  `app_installed_time` VARCHAR(128) DEFAULT '',
  `collect_time_ms` BIGINT NOT NULL DEFAULT 0,
  `report_time_ms` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY  (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_user_invitation_roster`;
CREATE TABLE `eh_user_invitation_roster` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `invite_id` BIGINT COMMENT 'owner invitation record id',
  `name` VARCHAR(64),
  `contact` VARCHAR(64),

  PRIMARY KEY (`id`),
  KEY `fk_eh_invite_roster_invite_id` (`invite_id`),
  CONSTRAINT `eh_user_invitation_roster_ibfk_1` FOREIGN KEY (`invite_id`) REFERENCES `eh_user_invitations` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users partition
--
DROP TABLE IF EXISTS `eh_user_invitations`;
CREATE TABLE `eh_user_invitations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `invite_code` VARCHAR(128),
  `invite_type` TINYINT COMMENT '1: SMS, 2: wechat, 3, wechat friend circle, 4: weibo, 5: phone contact',
  `expiration` DATETIME COMMENT 'expiration time of the invitation',
  `target_entity_type` VARCHAR(32),
  `target_entity_id` BIGINT,
  `max_invite_count` INTEGER NOT NULL DEFAULT 1,
  `current_invite_count` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',

  `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_invite_code`(`invite_code`),
  KEY `u_eh_invite_expiration`(`expiration`),
  KEY `u_eh_invite_code_status`(`invite_code`, `status`),
  KEY `u_eh_invite_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_user_launch_pad_items`;
CREATE TABLE `eh_user_launch_pad_items`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `item_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community, organization',
  `owner_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: default, 1: override, 2: revert 3:customized',
  `display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'default display on the pad, 0: hide, 1:display',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',
  `update_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_user sharding group
--
DROP TABLE IF EXISTS `eh_user_likes`;
CREATE TABLE `eh_user_likes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `target_type` VARCHAR(32),
  `target_id` BIGINT,
  `like_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: dislike, 2: like',
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_like_target` (`owner_uid`,`target_type`,`target_id`),
  KEY `i_eh_usr_like_owner` (`owner_uid`),
  KEY `i_eh_usr_like_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of global parition
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_user_locations`;
CREATE TABLE `eh_user_locations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `uid` BIGINT NOT NULL DEFAULT 0,
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(128) DEFAULT '',
  `create_time` DATETIME,
  `collect_time_ms` BIGINT NOT NULL DEFAULT 0,
  `report_time_ms` BIGINT NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_user_notification_settings`;
CREATE TABLE `eh_user_notification_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_id` BIGINT NOT NULL DEFAULT 1 COMMENT 'default to messaging app itself',
  `owner_type` VARCHAR(128) NOT NULL COMMENT 'e.g: EhUsers',
  `owner_id` BIGINT NOT NULL COMMENT 'owner type identify token',
  `target_type` VARCHAR(128) NOT NULL COMMENT 'e.g: EhUsers',
  `target_id` BIGINT NOT NULL COMMENT 'target type identify token',
  `mute_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: mute',
  `creator_uid` BIGINT,
  `create_time` datetime(3),
  `update_uid` BIGINT,
  `update_time` datetime(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- member of eh_users partition
-- Used for duplicated recording of post membership that user is involved in order to store
-- it in the same shard as of its owner user
--
DROP TABLE IF EXISTS `eh_user_posts`;
CREATE TABLE `eh_user_posts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `target_type` VARCHAR(32),

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_post_id` (`owner_uid`,`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users sharding group
--
DROP TABLE IF EXISTS `eh_user_profiles`;
CREATE TABLE `eh_user_profiles` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `app_id` BIGINT,
  `owner_id` BIGINT NOT NULL COMMENT 'owner user id',
  `item_name` VARCHAR(128),
  `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque json object, 1: entity',
  `item_value` TEXT,
  `target_type` VARCHAR(32),
  `target_id` BIGINT,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),

  PRIMARY KEY (`id`),
  KEY `i_eh_uprof_item` (`app_id`,`owner_id`,`item_name`),
  KEY `i_eh_uprof_owner` (`owner_id`),
  KEY `i_eh_uprof_itag1` (`integral_tag1`),
  KEY `i_eh_uprof_itag2` (`integral_tag2`),
  KEY `i_eh_uprof_stag1` (`string_tag1`),
  KEY `i_eh_uprof_stag2` (`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of user-related sharding group
-- shared among namespaces, no application module specific information
--
DROP TABLE IF EXISTS `eh_user_scores`;
CREATE TABLE `eh_user_scores` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `owner_uid` BIGINT NOT NULL DEFAULT 0,
  `score_type` VARCHAR(32) NOT NULL,
  `score` INTEGER NOT NULL DEFAULT 0,
  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operate_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of eh_users partition
-- Used for duplicated recording of post membership that user is involved in order to store
-- it in the same shard as of its owner user
--
DROP TABLE IF EXISTS `eh_user_service_addresses`;
CREATE TABLE `eh_user_service_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `address_id` BIGINT NOT NULL DEFAULT 0,
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'phone number or email address',
  `contact_name` VARCHAR(64),
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_usr_service_address_id` (`owner_uid`,`address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- key table of user-related sharding group
--
DROP TABLE IF EXISTS `eh_users`;
CREATE TABLE `eh_users` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `account_name` VARCHAR(64) NOT NULL,
  `nick_name` VARCHAR(128),
  `avatar` VARCHAR(2048),
  `status_line` VARCHAR(128) COMMENT 'status line to express who you are',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0 - inactive, 1 - active',
  `points` INTEGER NOT NULL DEFAULT 0 COMMENT 'points',
  `level` TINYINT NOT NULL DEFAULT 1,

  --
  -- for gender/age based matching
  --
  `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
  `birthday` DATE,

  --
  -- for current residency matching (corresponding to current family)
  --
  `address_id` BIGINT COMMENT 'current address id',
  `address` VARCHAR(128) COMMENT 'redundant current address description',

  `community_id` BIGINT COMMENT 'if current family has been setup, it is the community id from address',

  --
  -- for home town based matching
  --
  `home_town` BIGINT COMMENT 'region id',
  `home_town_path` VARCHAR(128) COMMENT 'redundant region path for recursive matching',

  `occupation` VARCHAR(128),
  `company` VARCHAR(128),
  `school` VARCHAR(128),

  `locale` VARCHAR(16) COMMENT 'zh_CN, en_US etc',

  `invite_type` TINYINT COMMENT '1: SMS, 2: wechat, 3, wechat friend circle, 4: weibo, 5: phone contact',
  `invitor_uid` BIGINT,
  `create_time` DATETIME NOT NULL,
  `delete_time` DATETIME COMMENT 'mark-deletion policy. may be valuable for user to restore account',
  `last_login_time` DATETIME,
  `last_login_ip` VARCHAR(64),
  `reg_ip` VARCHAR(64) DEFAULT '' COMMENT 'the channel at the time of register',
  `reg_city_id` BIGINT DEFAULT 0 COMMENT 'the city at the time of register',
  `reg_channel_id` BIGINT DEFAULT 0 COMMENT 'the channel at the time of register',
  `original_avatar` VARCHAR(128) COMMENT 'the path of avatar in 2.8 version, keep it for migration',

  `salt` VARCHAR(64),
  `password_hash` VARCHAR(128) DEFAULT '' COMMENT 'Note, password is stored as salted hash, salt is appended by hash together',

  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `namespace_user_token` VARCHAR(2048) NOT NULL DEFAULT '',
  `namespace_user_type` VARCHAR(128) COMMENT 'the type of user',
  `executive_tag` TINYINT DEFAULT 0 COMMENT '0-不是高管 1-是高管',
  `position_tag` VARCHAR(128) COMMENT '职位',
  `identity_number_tag` VARCHAR(20) COMMENT '身份证号',
  `update_time` DATETIME,
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  UNIQUE KEY `u_eh_user_account_name` (`account_name`),
  KEY `i_eh_user_create_time` (`create_time`),
  KEY `i_eh_user_nick_name` (`nick_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_version_realm`;
CREATE TABLE `eh_version_realm` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `realm` VARCHAR(128),
  `description` TEXT,
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ver_realm` (`realm`,`namespace_id`),
  KEY `i_eh_ver_realm_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_version_upgrade_rules`;
CREATE TABLE `eh_version_upgrade_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `realm_id` BIGINT NOT NULL,
  `matching_lower_bound` DOUBLE NOT NULL,
  `matching_upper_bound` DOUBLE NOT NULL,
  `order` INTEGER NOT NULL DEFAULT 0,

  `target_version` VARCHAR(128),
  `force_upgrade` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  KEY `fk_eh_ver_upgrade_realm` (`realm_id`),
  KEY `i_eh_ver_upgrade_order` (`order`),
  KEY `i_eh_ver_upgrade_create_time` (`create_time`),
  CONSTRAINT `eh_version_upgrade_rules_ibfk_1` FOREIGN KEY (`realm_id`) REFERENCES `eh_version_realm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_version_urls`;
CREATE TABLE `eh_version_urls` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `realm_id` BIGINT NOT NULL,
  `target_version` VARCHAR(128),
  `download_url` VARCHAR(128) COMMENT 'example configuration: http://serviceurl/download/client-packages/${locale}/andriod-${major}-${minor}-${revision}.apk',
  `info_url` VARCHAR(128) COMMENT 'example configuration: http://serviceurl/download/client-package-info/${locale}/andriod-${major}-${minor}-${revision}.html',
  `upgrade_description` TEXT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_name` VARCHAR(50),
  `publish_time` DATETIME,
  `icon_url` VARCHAR(50),
  
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ver_url` (`realm_id`,`target_version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_versioned_content`;
CREATE TABLE `eh_versioned_content` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `realm_id` BIGINT NOT NULL,
  `matching_lower_bound` DOUBLE NOT NULL,
  `matching_upper_bound` DOUBLE NOT NULL,
  `order` INTEGER NOT NULL DEFAULT 0,
  `content` TEXT,
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`),
  KEY `fk_eh_ver_content_realm` (`realm_id`),
  KEY `i_eh_ver_content_order` (`order`),
  KEY `i_eh_ver_content_create_time` (`create_time`),
  CONSTRAINT `eh_versioned_content_ibfk_1` FOREIGN KEY (`realm_id`) REFERENCES `eh_version_realm` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warehouse_material_categories`;
CREATE TABLE `eh_warehouse_material_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `category_number` VARCHAR(32) DEFAULT '',
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `path` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warehouse_materials`;
CREATE TABLE `eh_warehouse_materials` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `material_number` VARCHAR(32) NOT NULL DEFAULT '',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_warehouse_material_categories',
  `category_path` VARCHAR(128) COMMENT 'path of eh_warehouse_material_categories',
  `brand` VARCHAR(128),
  `item_no` VARCHAR(64),
  `reference_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `unit_id` BIGINT COMMENT 'id of eh_warehouse_units',
  `specification_information` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warehouse_request_materials`;
CREATE TABLE `eh_warehouse_request_materials` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_id` BIGINT NOT NULL COMMENT 'id of eh_warehouse_requests',
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_source` TINYINT NOT NULL DEFAULT 0 COMMENT '0: request, 1: manual input',
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `amount` BIGINT NOT NULL DEFAULT 0,
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_uid` BIGINT,
  `review_time` DATETIME,
  `delivery_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0:no, 1: yes',
  `delivery_uid` BIGINT,
  `delivery_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warehouse_requests`;
CREATE TABLE `eh_warehouse_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_uid` BIGINT,
  `request_organization_id` BIGINT,
  `remark` VARCHAR(512),
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `delivery_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0:no, 1: yes',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warehouse_stock_logs`;
CREATE TABLE `eh_warehouse_stock_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_warehouse_requests',
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_source` TINYINT NOT NULL DEFAULT 0 COMMENT '0: request, 1: manual input',
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `delivery_amount` BIGINT NOT NULL DEFAULT 0,
  `stock_amount` BIGINT NOT NULL DEFAULT 0 COMMENT 'rest amount after delivery',
  `request_uid` BIGINT,
  `delivery_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warehouse_stocks`;
CREATE TABLE `eh_warehouse_stocks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `amount` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warehouse_units`;
CREATE TABLE `eh_warehouse_units` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `deletor_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warehouses`;
CREATE TABLE `eh_warehouses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `warehouse_number` VARCHAR(32) DEFAULT '',
  `volume` DOUBLE NOT NULL DEFAULT 0,
  `location` VARCHAR(512) NOT NULL DEFAULT '',
  `manager` VARCHAR(64),
  `contact` VARCHAR(64),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: disable, 2: enable',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warning_contacts`;
CREATE TABLE `eh_warning_contacts` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `contactor` VARCHAR(20),
  `mobile` VARCHAR(20),
  `email` VARCHAR(20),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warning_settings`;
CREATE TABLE `eh_warning_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT 'namespace id',
  `type` VARCHAR(64) COMMENT 'type',
  `time` BIGINT COMMENT 'millisecond',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- the relationship between web menus and privileges
--
DROP TABLE IF EXISTS `eh_web_menu_privileges`;
CREATE TABLE `eh_web_menu_privileges` (
  `id` BIGINT NOT NULL,
  `privilege_id` BIGINT NOT NULL,
  `menu_id` BIGINT NOT NULL,
  `name` VARCHAR(64),
  `show_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: Not related menu display, 1: Associated menu display',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
  `discription` VARCHAR(128),
  `sort_num` INTEGER COMMENT 'sort number',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- the custom scope of the web menu
--
DROP TABLE IF EXISTS `eh_web_menu_scopes`;
CREATE TABLE `eh_web_menu_scopes` (
  `id` BIGINT NOT NULL,
  `menu_id` BIGINT,
  `menu_name` VARCHAR(64),
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT,
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: delete , 1: override, 2: revert',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_menu_scope_owner` (`menu_id`,`owner_type`,`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- web menu
--
DROP TABLE IF EXISTS `eh_web_menus`;
CREATE TABLE `eh_web_menus` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(64),
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `icon_url` VARCHAR(64),
  `data_type` VARCHAR(256),
  `leaf_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'Whether leaf nodes, non leaf nodes can be folded 0: false, 1: true',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 2: active',
  `path` VARCHAR(64),
  `type` VARCHAR(64) NOT NULL DEFAULT 'zuolin' COMMENT 'zuolin, park',
  `sort_num` INTEGER COMMENT 'sort number',
  `module_id` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_wifi_settings`;
CREATE TABLE `eh_wifi_settings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `ssid` VARCHAR(128) NOT NULL COMMENT 'the name of address resource',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive, 1: active',

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_yellow_page_attachments`;
CREATE TABLE `eh_yellow_page_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id reference to eh_yellow_pages',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


--
-- member of global sharding group
--
DROP TABLE IF EXISTS `eh_yellow_pages`;
CREATE TABLE `eh_yellow_pages` (
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'organization name',
  `nick_name` VARCHAR(128) NOT NULL DEFAULT '',
  `type` TINYINT NOT NULL DEFAULT 0 COMMENT '1 chuangkekongjian; 2 fuwulianmeng; 3 yuanquqiye',
  `address` VARCHAR(255) NOT NULL DEFAULT '',
  `contact` VARCHAR(64),
  `description` TEXT,
  `poster_uri` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `default_order` INTEGER,
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `geohash` VARCHAR(32),
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `building_id` BIGINT COMMENT 'eh_buildings id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_yzb_devices`;
CREATE TABLE `eh_yzb_devices` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `device_id` VARCHAR(64) NOT NULL COMMENT 'device_id of yzb',
  `room_id` VARCHAR(64) NOT NULL COMMENT 'room_id of this devices',
  `relative_id` BIGINT NOT NULL COMMENT 'activity_id',
  `relative_type` VARCHAR(64) NOT NULL DEFAULT 'activity',
  `last_vid` VARCHAR(64) COMMENT 'the last vid',
  `state` TINYINT NOT NULL DEFAULT 0 COMMENT 'the current state of this devices',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'INVALID, VALID',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


SET FOREIGN_KEY_CHECKS = 1;
