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
  `creator_uid` BIGINT DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'record create time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_acl_role_name` (`namespace_id`,`app_id`,`name`,`owner_type`,`owner_id`),
  KEY `u_eh_acl_role_tag` (`tag`),
  KEY `i_eh_ach_role_owner` (`namespace_id`,`app_id`,`owner_type`,`owner_id`),
  KEY `i_eh_acl_role_creator_uid` (`creator_uid`),
  KEY `i_eh_acl_role_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_aclink_cameras`;

-- 内网摄像头表创建
CREATE TABLE `eh_aclink_cameras` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(32),
  `door_access_id` BIGINT NOT NULL COMMENT '关联门禁id',
  `enter_status` TINYINT DEFAULT 0 COMMENT '进出标识 1进0出',
  `link_status` TINYINT NOT NULL DEFAULT 0 COMMENT '联网状态',
  `ip_address` VARCHAR(128) COMMENT 'IP地址',
  `server_id` BIGINT COMMENT '服务器id',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态1正常2已删除',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `key_code` VARCHAR(128) NOT NULL COMMENT '摄像头密钥',
  `account` VARCHAR(128) NOT NULL DEFAULT 'admin' COMMENT '摄像头账号',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='内网摄像头表';

DROP TABLE IF EXISTS `eh_aclink_firmware`;


CREATE TABLE `eh_aclink_firmware` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `major` INTEGER NOT NULL DEFAULT 0,
  `minor` INTEGER NOT NULL DEFAULT 0,
  `revision` INTEGER NOT NULL DEFAULT 0,
  `checksum` BIGINT NOT NULL,
  `md5sum` VARCHAR(64),
  `download_url` VARCHAR(1024),
  `info_url` VARCHAR(1024),
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME,
  `creator_id` BIGINT,
  `owner_id` BIGINT,
  `owner_type` TINYINT,
  `firmware_type` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_aclink_ipads`;

-- 内网ipad表创建
CREATE TABLE `eh_aclink_ipads` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(32),
  `door_access_id` BIGINT NOT NULL COMMENT '关联门禁id',
  `enter_status` TINYINT DEFAULT 0 COMMENT '进出标识 1进0出',
  `uuid` VARCHAR(6) NOT NULL COMMENT '配对码',
  `link_status` TINYINT NOT NULL DEFAULT 0 COMMENT '联网状态',
  `server_id` BIGINT COMMENT '服务器id',
  `active_time` DATETIME COMMENT '激活时间',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '激活状态0未激活1已激活2已删除',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_aclink_ipads_uuid` (`uuid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='内网ipad表';

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

DROP TABLE IF EXISTS `eh_aclink_servers`;

-- 内网服务器表创建
CREATE TABLE `eh_aclink_servers` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(32),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `uuid` VARCHAR(64) NOT NULL,
  `ip_address` VARCHAR(128) COMMENT 'IP地址',
  `link_status` TINYINT NOT NULL DEFAULT 0 COMMENT '联网状态',
  `active_time` DATETIME COMMENT '激活时间',
  `sync_time` DATETIME COMMENT '上次同步时间',
  `version` VARCHAR(8) COMMENT '版本号',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT '组织id',
  `owner_type` TINYINT NOT NULL DEFAULT 0 COMMENT '组织类型',
  `aes_server_key` VARCHAR(64) COMMENT 'AES公钥',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '激活状态0未激活1已激活2已删除',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='内网服务器表';

DROP TABLE IF EXISTS `eh_aclink_undo_key`;


CREATE TABLE `eh_aclink_undo_key` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `key_id` INTEGER NOT NULL COMMENT 'cancel a key, must notify all users for this key_id to update',
  `door_id` BIGINT NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: requesting, 2: confirm',
  `expire_time_ms` BIGINT NOT NULL,
  `create_time_ms` BIGINT NOT NULL,

  PRIMARY KEY (`id`),
  KEY `fk_eh_aclink_undo_key_door_id` (`door_id`),
  CONSTRAINT `eh_aclink_undo_key_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_aclinks`;


CREATE TABLE `eh_aclinks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `door_id` BIGINT NOT NULL,
  `device_name` VARCHAR(32) NOT NULL,
  `manufacturer` VARCHAR(32) NOT NULL,
  `firware_ver` VARCHAR(32) NOT NULL,
  `create_time` DATETIME,
  `status` TINYINT NOT NULL,
  `string_tag1` VARCHAR(1024),
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

DROP TABLE IF EXISTS `eh_activities`;


CREATE TABLE `eh_activities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `namespace_id` INTEGER,
  `subject` VARCHAR(512),
  `description` LONGTEXT,
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
  `wechat_signup` TINYINT DEFAULT 0 COMMENT 'is support wechat signup 0:no, 1:yes',
  `clone_flag` TINYINT COMMENT 'clone_flag post 0-real post, 1-clone post',
  `stick_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是',
  `stick_time` DATETIME,
  `organization_id` BIGINT COMMENT '企业ID',
  `min_quantity` INTEGER  COMMENT '最低限制人数',
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

DROP TABLE IF EXISTS `eh_activity_biz_payee`;


CREATE TABLE `eh_activity_biz_payee` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `owner_id` BIGINT NOT NULL COMMENT '应用类型id',
  `biz_payee_id` BIGINT NOT NULL COMMENT '收款方账户ID',
  `biz_payee_type` VARCHAR(128) COMMENT '收款方账户类型：EhUsers/EhOrganizations',
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

DROP TABLE IF EXISTS `eh_activity_goods`;


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
  `order_no` VARCHAR(64),
  `order_start_time` DATETIME,
  `order_expire_time` DATETIME,
  `vendor_type` VARCHAR(32) COMMENT '10001: alipay, 10002: wechatpay',
  `pay_amount` DECIMAL(10,2),
  `pay_time` DATETIME,
  `refund_order_no` VARCHAR(64),
  `refund_amount` DECIMAL(10,2),
  `refund_time` DATETIME,
  `status` TINYINT DEFAULT 2 COMMENT '0: cancel, 1: reject, 2:normal',
  `organization_id` BIGINT,
  `cancel_time` DATETIME,
  `order_type` VARCHAR(128) COMMENT 'orderType',
  `pay_version` TINYINT COMMENT '支付版本，用于退款',
  `pay_order_id` BIGINT COMMENT '支付系统订单ID',
  `refund_pay_order_id` BIGINT COMMENT '支付系统退款订单ID',
  `form_id` BIGINT COMMENT '表单ID',
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

DROP TABLE IF EXISTS `eh_address_arrangement`;


CREATE TABLE `eh_address_arrangement` (
  `id` BIGINT NOT NULL COMMENT '主键id',
  `address_id` BIGINT COMMENT '要执行拆分/合并计划的房源id',
  `original_id` VARCHAR(2048) COMMENT '被拆分的房源id或者被合并的房源id（以json数组方式存储）',
  `target_id` VARCHAR(2048) COMMENT '拆分后产生的房源id或者合并后产生的房源id（以json数组方式存储）',
  `operation_type` TINYINT COMMENT '操作类型：拆分（0），合并（1）',
  `date_begin` DATETIME NOT NULL COMMENT '拆分合并计划的生效日期',
  `operation_flag` TINYINT COMMENT '计划是否执行标志（0：否，1：是）',
  `status` tinyint(255) COMMENT '计划状态',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间id',
  `creator_uid` BIGINT COMMENT '创建人',
  `create_time` DATETIME COMMENT '创建时间',
  `update_uid` BIGINT COMMENT '更新人',
  `update_time` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='房源拆分/合并计划表';

DROP TABLE IF EXISTS `eh_address_attachments`;


CREATE TABLE `eh_address_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to the id of eh_addresses',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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

DROP TABLE IF EXISTS `eh_address_properties`;


CREATE TABLE `eh_address_properties` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespaceId',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'communityId',
  `building_id` BIGINT COMMENT '楼栋id',
  `address_id` BIGINT COMMENT '房源id',
  `charging_items_id` BIGINT COMMENT '费项id',
  `authorize_price` DECIMAL(10,2) COMMENT '授权价',
  `apartment_authorize_type` TINYINT COMMENT '房源授权价类型（1:每天; 2:每月; 3:每个季度; 4:每年;)',
  `status` TINYINT COMMENT '0-无效状态 ,2-有效状态',
  `create_time` DATETIME COMMENT '创建日期',
  `creator_uid` BIGINT COMMENT '创建人',
  `operator_time` DATETIME COMMENT '最近修改时间',
  `operator_uid` BIGINT COMMENT '最近修改人',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='楼宇属性信息表';

DROP TABLE IF EXISTS `eh_addresses`;


CREATE TABLE `eh_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `community_id` BIGINT COMMENT 'NULL: means it is an independent street address, otherwise, it is an appartment address',
  `community_name` VARCHAR(64) COMMENT '房源所在园区名称',
  `building_id` BIGINT COMMENT '房源所在楼宇id',
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
  `shared_area` DOUBLE COMMENT '公摊面积',
  `charge_area` DOUBLE COMMENT '收费面积',
  `category_item_id` BIGINT COMMENT '资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他..., refer to the id of eh_var_field_items',
  `category_item_name` VARCHAR(128) COMMENT '资产类型：住宅、写字楼、酒店式公寓、厂房、库房、车位、其他..., refer to the display_name of eh_var_field_items',
  `source_item_id` BIGINT COMMENT '资产来源：自管、业主放盘、大业主交管、其他..., refer to the id of eh_var_field_items',
  `source_item_name` VARCHAR(128) COMMENT '资产来源：自管、业主放盘、大业主交管、其他..., refer to the display_name of eh_var_field_items',
  `decorate_status` TINYINT COMMENT '装修状态',
  `orientation` VARCHAR(32) COMMENT '朝向',
  `apartment_number` VARCHAR(32),
  `address_unit` VARCHAR(32),
  `address_ownership_id` BIGINT COMMENT '产权归属: 自有、出售、非产权..., refer to the id of eh_var_field_items',
  `address_ownership_name` VARCHAR(128) COMMENT '产权归属: 自有、出售、非产权..., refer to the display_name of eh_var_field_items',
  `remark` VARCHAR(128),
  `version` VARCHAR(32) COMMENT '版本号',
  `free_area` DOUBLE COMMENT '可招租面积',
  `is_future_apartment` TINYINT DEFAULT 0 COMMENT '未来房源标记（0：否，1：是）',
  PRIMARY KEY (`id`),
  KEY `i_eh_addr_city` (`city_id`),
  KEY `i_eh_addr_community` (`community_id`),
  KEY `i_eh_addr_address_alias` (`address_alias`),
  KEY `i_eh_addr_building_apt_name` (`building_name`,`apartment_name`),
  KEY `i_eh_addr_building_alias_apt_name` (`building_alias_name`,`apartment_name`),
  KEY `i_eh_addr_address` (`address`),
  KEY `namespace_address` (`namespace_address_type`,`namespace_address_token`)
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

DROP TABLE IF EXISTS `eh_alliance_extra_event_attachment`;


CREATE TABLE `eh_alliance_extra_event_attachment` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL COMMENT 'the id of eh_alliance_extra_events',
  `file_type` VARCHAR(32) COMMENT 'like image,jpg. in lower case',
  `file_uri` VARCHAR(1024) NOT NULL COMMENT 'like cs://1/...',
  `file_name` VARCHAR(200),
  `file_size` BIGINT NOT NULL DEFAULT 0 COMMENT 'file size (Byte)',
  `create_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用于服务联盟工作流中新建事件时保存附件使用';

DROP TABLE IF EXISTS `eh_alliance_extra_events`;


CREATE TABLE `eh_alliance_extra_events` (
  `id` BIGINT NOT NULL,
  `flow_case_id` BIGINT NOT NULL,
  `topic` VARCHAR(200) NOT NULL COMMENT 'topic of current event',
  `time` DATETIME NOT NULL COMMENT 'the time that event happen',
  `address` VARCHAR(200),
  `provider_id` BIGINT COMMENT 'id of alliance_providers',
  `provider_name` VARCHAR(50) COMMENT 'name of alliance_provider',
  `members` VARCHAR(500) NOT NULL COMMENT 'those who participate in',
  `content` MEDIUMTEXT NOT NULL COMMENT 'main body',
  `enable_read` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0-hide for applier  1-show for applier',
  `enable_notify_by_email` tinyint(3) NOT NULL DEFAULT 0 COMMENT '0-not send email  1-send email to provider',
  `create_time` DATETIME NOT NULL,
  `create_uid` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工作流中，新建事件表';

DROP TABLE IF EXISTS `eh_alliance_online_service`;

-- 服务联盟v3.4 服务新增客服会话表 add by huangmingbo 2018.07.03
CREATE TABLE `eh_alliance_online_service` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_service_alliances',
  `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_users',
  `user_name` VARCHAR(64) NOT NULL DEFAULT '""' COMMENT 'organization_members contact name',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-inactive 1-active  currently not used',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'last update time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_owner_user` (`owner_id`,`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='服务联盟客服表，新增服务时会指派客服专员。这个表保存服务添加过的客服信息。';

DROP TABLE IF EXISTS `eh_alliance_stat`;


CREATE TABLE `eh_alliance_stat` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL COMMENT '服务联盟类型id',
  `owner_id` BIGINT NOT NULL COMMENT '所属项目id',
  `category_id` BIGINT NOT NULL COMMENT '服务类型id',
  `service_id` BIGINT COMMENT '服务id',
  `click_type` TINYINT NOT NULL COMMENT '点击类型： 3-进入详情 4-点击提交 5-点击咨询 6-点击分享 20-提交申请',
  `click_count` BIGINT NOT NULL DEFAULT 0 COMMENT '点击总数/提交申请次数',
  `click_date` date NOT NULL COMMENT '点击日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '该记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_click_date` (`click_date`),
  KEY `i_eh_service_id` (`service_id`),
  KEY `i_eh_category_id` (`category_id`),
  KEY `i_eh_owner_id` (`owner_id`),
  KEY `i_eh_type` (`type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='统计各个服务每天的各类型用户行为点击数。';

DROP TABLE IF EXISTS `eh_alliance_stat_details`;


CREATE TABLE `eh_alliance_stat_details` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `type` BIGINT NOT NULL COMMENT '服务联盟类型id',
  `owner_id` BIGINT NOT NULL COMMENT '所属项目id',
  `category_id` BIGINT NOT NULL COMMENT '服务类型id',
  `service_id` BIGINT COMMENT '服务id',
  `user_id` BIGINT NOT NULL,
  `user_name` VARCHAR(64),
  `user_phone` VARCHAR(20),
  `click_type` TINYINT NOT NULL COMMENT '点击类型：1-首页点击服务 3-进入详情 4-点击提交 5-点击咨询 6-点击分享',
  `click_time` BIGINT NOT NULL COMMENT '点击时间戳',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录生成时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_service_id` (`service_id`),
  KEY `i_eh_category_id` (`category_id`),
  KEY `i_eh_click_time` (`click_time`),
  KEY `i_eh_owner_id` (`owner_id`),
  KEY `i_eh_type` (`type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户的点击明细';

DROP TABLE IF EXISTS `eh_alliance_tag`;


CREATE TABLE `eh_alliance_tag` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '0-parent node , others-child node',
  `value` VARCHAR(32) NOT NULL DEFAULT '""' COMMENT 'tag name',
  `type` BIGINT NOT NULL DEFAULT 0 COMMENT 'type of service alliances ',
  `is_default` tinyint(8) NOT NULL DEFAULT 0 COMMENT 'default chosen',
  `default_order` TINYINT NOT NULL DEFAULT 0 COMMENT 'show order; the smaller the toper;like 0,1,2',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'user_id of creater',
  `delete_flag` tinyint(8) NOT NULL DEFAULT 0 COMMENT '0-active 1-deleted',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_alliance_tag_vals`;


CREATE TABLE `eh_alliance_tag_vals` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of service alliance',
  `tag_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_alliance_tag',
  `tag_parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parent id of eh_alliance_tag',
  PRIMARY KEY (`id`)
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
  `download_url` VARCHAR(256),
  `logo_url` VARCHAR(1024),
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

DROP TABLE IF EXISTS `eh_app_white_list`;


CREATE TABLE `eh_app_white_list` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `link` VARCHAR(128) NOT NULL COMMENT '第三方应用链接',
  `name` VARCHAR(128) NOT NULL COMMENT '第三方应用名称',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='第三方应用白名单';

DROP TABLE IF EXISTS `eh_approval_attachments`;


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
  `time_unit` VARCHAR(10) NOT NULL DEFAULT 'DAY' COMMENT '请假单位，DAY:天，HOUR:小时',
  `time_step` DECIMAL(6,2) NOT NULL DEFAULT '0.50' COMMENT '最小请假时长',
  `remainder_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用余额设置，0不支持，1未启用，2启用',
  `origin_id` BIGINT COMMENT '旧版本的请假类型是公共的,即共用namespace_id=0，该字段用于兼容这部分数据，将旧数据的id关联过来',
  `STATUS` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-已删除 1 不启用 2 启用 3 不可禁用',
  `hander_type` VARCHAR(32) COMMENT '和处理逻辑对应的名称',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`),
  KEY `i_eh_namespace_owner_id` (`namespace_id`,`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_approval_category_init_logs`;


CREATE TABLE `eh_approval_category_init_logs` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认organization',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_id` (`namespace_id`,`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='记录每个公司是否已经初始化了请假列表，避免重复初始化';

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
  UNIQUE KEY `u_eh_app_reg_app_key` (`app_key`),
  KEY `i_eh_app_reg_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_archives_dismiss_employees`;


CREATE TABLE `eh_archives_dismiss_employees` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL COMMENT 'organizationId',
  `contact_name` VARCHAR(32) COMMENT 'the name of the employee',
  `employee_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0:probation, 1:on the job, 2:internship, 3:dismissal',
  `department` VARCHAR(128) COMMENT '离职前部门',
  `contract_party_id` BIGINT COMMENT '合同主体',
  `check_in_time` DATE COMMENT '入职日期',
  `dismiss_time` DATE COMMENT '离职日期',
  `dismiss_type` TINYINT COMMENT '离职类型',
  `dismiss_reason` TINYINT COMMENT '离职原因',
  `dismiss_remarks` VARCHAR(1024) COMMENT '备注',
  `detail_id` BIGINT NOT NULL COMMENT 'the id of member in eh_organization_member_details',
  `create_time` DATETIME COMMENT 'the time of data creating',
  `operator_uid` BIGINT COMMENT 'the id of the operator',
  `department_id` BIGINT COMMENT '离职前部门id',
  `job_position` VARCHAR(128) COMMENT '离职前职位',
  `job_level` VARCHAR(128) COMMENT '离职前职级',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_archives_form_groups`;


CREATE TABLE `eh_archives_form_groups` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `form_id` BIGINT COMMENT 'the id of the archives form',
  `form_groups` TEXT NOT NULL COMMENT 'the group of the form in json format',
  `operator_uid` BIGINT,
  `operator_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_archives_form_vals`;


CREATE TABLE `eh_archives_form_vals` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `form_id` BIGINT COMMENT 'the id of the archives form',
  `source_id` BIGINT COMMENT 'the source id ',
  `field_name` VARCHAR(128) COMMENT 'the name of the field',
  `field_type` VARCHAR(128) COMMENT 'the type of the field',
  `field_value` TEXT COMMENT 'the value of the field',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_archives_forms`;


CREATE TABLE `eh_archives_forms` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `form_name` VARCHAR(64) NOT NULL COMMENT 'name of the form',
  `static_fields` TEXT NOT NULL COMMENT 'static form fields in json format',
  `dynamic_fields` TEXT COMMENT 'dynamic form fields in json format',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0. inactive, 1.active',
  `operator_uid` BIGINT,
  `operator_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_archives_notifications`;


CREATE TABLE `eh_archives_notifications` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the organization',
  `notify_day` INTEGER COMMENT 'the day of sending emails',
  `notify_time` INTEGER COMMENT 'the hour of sending notifications',
  `mail_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'email sending, 0-no 1-yes',
  `message_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'message sending, 0-no 1-yes',
  `notify_target` TEXT COMMENT 'the target email address',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the operator',
  `create_time` DATETIME COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_archives_operational_configurations`;


CREATE TABLE `eh_archives_operational_configurations` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `detail_id` BIGINT NOT NULL COMMENT 'the detail id that belongs to the employee which is the change target',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of operation',
  `operation_date` DATE COMMENT 'the date of executing the operation',
  `additional_info` TEXT COMMENT 'the addition information for the operation',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-cancel, 1-pending, 2-complete',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT COMMENT 'the id of the operator',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_archives_operational_logs`;


CREATE TABLE `eh_archives_operational_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the log',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the organization',
  `detail_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the detail id that belongs to the employee',
  `operation_type` TINYINT NOT NULL COMMENT 'the type of the operate',
  `operation_time` date NOT NULL COMMENT 'the time of the operate',
  `string_tag1` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag2` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag3` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag4` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag5` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `string_tag6` VARCHAR(2048) COMMENT 'redundant information for the operate',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of the operator',
  `operator_name` VARCHAR(64) NOT NULL DEFAULT 0 COMMENT 'the id of the operator',
  `create_time` DATETIME COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_archives_sticky_contacts`;


CREATE TABLE `eh_archives_sticky_contacts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL COMMENT 'organizationId',
  `detail_id` BIGINT NOT NULL COMMENT 'the id of member in eh_organization_member_details',
  `create_time` DATETIME COMMENT 'the time of data creating',
  `update_time` DATETIME COMMENT 'the time of data updating',
  `operator_uid` BIGINT COMMENT 'the id of the operator',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_asset_app_categories`;


CREATE TABLE `eh_asset_app_categories` (
  `id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `instance_flag` VARCHAR(1024),
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_category_id` (`category_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='relation mappings among applications';

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

DROP TABLE IF EXISTS `eh_asset_module_app_mappings`;


CREATE TABLE `eh_asset_module_app_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `asset_category_id` BIGINT,
  `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）',
  `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）',
  `config` VARCHAR(1024) COMMENT '各个业务系统自定义的JSON配置',
  `owner_id` BIGINT COMMENT '园区ID',
  `owner_type` VARCHAR(64) COMMENT '园区类型',
  `energy_flag` TINYINT COMMENT '在每个域空间，只有一个energy flag为1，0为不启用',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '2:active; 0:inactive',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT NOT NULL,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  `contract_originId` BIGINT COMMENT '合同管理应用的originId',
  `contract_changeFlag` TINYINT COMMENT '是否走合同变更，1、0',
  `bill_group_id` BIGINT COMMENT '账单组ID',
  `charging_item_id` BIGINT COMMENT '费项ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='relation mappings among applications';


DROP TABLE IF EXISTS `eh_asset_payment_order`;


CREATE TABLE `eh_asset_payment_order` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `payer_name` VARCHAR(128),
  `payer_type` VARCHAR(32),
  `uid` BIGINT,
  `contract_id` VARCHAR(2048),
  `namespace_id` INTEGER,
  `client_app_name` VARCHAR(128),
  `u_name` VARCHAR(64),
  `family_id` BIGINT,
  `organization_name` VARCHAR(128),
  `organization_id` BIGINT,
  `community_id` VARCHAR(64),
  `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
  `phone` VARCHAR(32),
  `real_name` VARCHAR(128),
  `gender` TINYINT COMMENT '0:female;1:male',
  `position` VARCHAR(64),
  `email` VARCHAR(128),
  `pay_flag` TINYINT DEFAULT 0 COMMENT '0: have not paid, 1:have paid',
  `order_no` BIGINT,
  `order_type` VARCHAR(20),
  `order_start_time` DATETIME,
  `order_expire_time` DATETIME,
  `payment_type` VARCHAR(32) COMMENT '10001: alipay, 10002: wechatpay',
  `pay_amount` DECIMAL(10,2),
  `paid_time` DATETIME,
  `refund_order_no` BIGINT,
  `status` TINYINT DEFAULT 0 COMMENT '0：新建；1：失败；2：支付成功但张江高科的全部失败；3：支付成功但张江高科的部分成功；4：支付成功张江高科的也全部成功;5：取消',
  `cancel_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- Table structure for eh_asset_payment_order_bills
DROP TABLE IF EXISTS `eh_asset_payment_order_bills`;


CREATE TABLE `eh_asset_payment_order_bills` (
  `id` BIGINT NOT NULL,
  `bill_id` VARCHAR(255),
  `bill_description` VARCHAR(255),
  `order_id` BIGINT,
  `amount` DECIMAL(10,2),
  `namespace_id` INT(10),
  `status` INTEGER DEFAULT 0 COMMENT '0:没有支付；1：支付成功；',

  PRIMARY KEY (`id`)
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

DROP TABLE IF EXISTS `eh_authorization_control_configs`;


CREATE TABLE `eh_authorization_control_configs` (
  `id` BIGINT NOT NULL,
  `control_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `target_type` VARCHAR(32) NOT NULL,
  `target_id` BIGINT NOT NULL,
  `include_child_flag` TINYINT,
  PRIMARY KEY (`id`),
  KEY `control_id_index` (`control_id`),
  KEY `user_id_index` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_authorization_relations`;


CREATE TABLE `eh_authorization_relations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` BIGINT NOT NULL,
  `module_id` BIGINT NOT NULL,
  `target_json` TEXT,
  `project_json` TEXT,
  `privilege_json` TEXT,
  `all_flag` TINYINT COMMENT '0 not all, 1 all',
  `all_project_flag` TINYINT COMMENT '0 not all, 1 all',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  `app_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_authorization_third_party_buttons`;


CREATE TABLE `eh_authorization_third_party_buttons` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'Ehnamespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `title` VARCHAR(128) COMMENT 'ren cai gong yu',
  `modify_flag` TINYINT COMMENT '0,hidden,1,show',
  `families_flag` TINYINT COMMENT '0,hidden,1,show',
  `qrcode_flag` TINYINT COMMENT '0,hidden,1,show',
  `delete_flag` TINYINT COMMENT '0,hidden,1,show',
  `blank_detail` VARCHAR(128) COMMENT 'ni hai mei jia ru jia ting',
  `button_detail` VARCHAR(128) COMMENT 'shen qing ren zheng ',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_authorization_third_party_forms`;


CREATE TABLE `eh_authorization_third_party_forms` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'Ehnamespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `source_type` VARCHAR(32) COMMENT 'zj_personal_auth zj_organization_auth,form ownertype',
  `source_id` BIGINT COMMENT 'form owner id',
  `authorization_url` VARCHAR(512) COMMENT 'third party authorization url',
  `app_key` VARCHAR(128) COMMENT 'app key',
  `secret_key` VARCHAR(512) COMMENT 'secret_key',
  `title` VARCHAR(512) COMMENT 'form title',
  `detail` VARCHAR(512) COMMENT 'form detail',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_authorization_third_party_records`;


CREATE TABLE `eh_authorization_third_party_records` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'Ehnamespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `flow_case_id` BIGINT,
  `type` VARCHAR(128) COMMENT '1, personal authorization, 2, organization authorization',
  `phone` VARCHAR(128) COMMENT 'phone',
  `name` VARCHAR(128) COMMENT 'user name',
  `certificate_type` VARCHAR(128) COMMENT '1.id card',
  `certificate_no` VARCHAR(128) COMMENT 'id card number',
  `organization_code` VARCHAR(128) COMMENT 'organization Code',
  `organization_contact` VARCHAR(128) COMMENT 'organization Contact',
  `organization_phone` VARCHAR(128) COMMENT 'organization Phone',
  `error_code` INTEGER COMMENT '200 success,201 invaild param,202 unrent',
  `address_id` BIGINT COMMENT 'authorization success, and save address id',
  `full_address` VARCHAR(256) COMMENT 'authorization success, and save full_address',
  `user_count` INTEGER COMMENT 'authorization success, and save user_count',
  `result_json` TEXT,
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_authorizations`;


CREATE TABLE `eh_authorizations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL COMMENT 'EhOrganizations, EhUsers',
  `target_id` BIGINT NOT NULL,
  `target_name` VARCHAR(128),
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` BIGINT NOT NULL,
  `auth_type` VARCHAR(64) NOT NULL COMMENT 'EhServiceModules, EhRoles',
  `auth_id` BIGINT NOT NULL,
  `identity_type` VARCHAR(64) NOT NULL COMMENT 'manage, ordinary',
  `all_flag` TINYINT COMMENT '0 not all, 1 all',
  `scope` VARCHAR(128),
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  `module_app_id` BIGINT COMMENT 'eh_service_module_apps id',
  `module_control_type` VARCHAR(64) DEFAULT '' COMMENT 'community_control;org_control;unlimit',
  `all_control_flag` TINYINT DEFAULT 0 COMMENT '0 not all, 1 all',
  `control_id` BIGINT,
  `control_option` TINYINT,
  PRIMARY KEY (`id`),
  KEY `owner_type_index` (`owner_type`),
  KEY `owner_id_index` (`owner_id`),
  KEY `target_type_index` (`target_type`),
  KEY `target_id_index` (`target_id`)
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
  `scene_type` VARCHAR(32),
  `apply_policy` TINYINT,
  `update_time` DATETIME,
  `target_type` VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE',
  `target_data` VARCHAR(1024) COMMENT 'It is different by different target_type',
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
  `default_order` BIGINT NOT NULL,
  `manager_name` VARCHAR(128),
  `construction_company` VARCHAR(128) COMMENT '施工单位',
  `height` DOUBLE COMMENT '楼高 单位米',
  `entry_date` DATETIME COMMENT '入驻时间',
  `shared_area` DOUBLE COMMENT '公摊面积',
  `charge_area` DOUBLE COMMENT '收费面积',
  `build_area` DOUBLE COMMENT '建筑面积',
  `rent_area` DOUBLE COMMENT '出租面积',
  `building_number` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin COMMENT '楼栋编号',
  `floor_number` INTEGER COMMENT '该楼栋的楼层数目',
  `free_area` DOUBLE COMMENT '可招租面积',
  PRIMARY KEY (`id`),
  KEY `building_name` (`name`,`alias_name`),
  KEY `u_eh_community_id_name` (`community_id`,`name`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 创建 bundleId 映射信息表

DROP TABLE IF EXISTS `eh_bundleid_mapper`;


CREATE TABLE `eh_bundleid_mapper` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `namespace_id` INTEGER COMMENT '域空间ID',
  `identify` VARCHAR(40) COMMENT 'pusherIdentify中截取的标志字符串',
  `bundle_id` VARCHAR(100) COMMENT '关联应用',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='bundleId 映射信息表';

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

DROP TABLE IF EXISTS `eh_butt_info_type_event_mapping`;


CREATE TABLE `eh_butt_info_type_event_mapping` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `info_type` VARCHAR(64) COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` INTEGER COMMENT '域空间ID',
  `event_name` VARCHAR(128) COMMENT '触发该脚本的事件',
  `sync_flag` TINYINT COMMENT '0 同步;1异步  同步执行还是异执行',
  `describe` VARCHAR(256) COMMENT '描述',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='脚本与事件映射表';

DROP TABLE IF EXISTS `eh_butt_script_config`;


CREATE TABLE `eh_butt_script_config` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `info_type` VARCHAR(64) COMMENT '分类',
  `info_describe` VARCHAR(128) COMMENT '描述',
  `namespace_id` INTEGER COMMENT '域空间ID',
  `module_id` BIGINT COMMENT '这个没啥意思,自己定义,因为建库入参需要,应该是作区分用',
  `module_type` VARCHAR(64) COMMENT '这个没啥意思,自己定义,因为建库入参需要,应该是作区分用 ',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `remark` VARCHAR(240) COMMENT '备注',
  `status` TINYINT COMMENT '状态;0失效,1生效',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='存储创建GOGS仓库时所需的指定相关表';

DROP TABLE IF EXISTS `eh_butt_script_last_commit`;


CREATE TABLE `eh_butt_script_last_commit` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `info_type` VARCHAR(64) COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` INTEGER COMMENT '域空间ID',
  `last_commit` VARCHAR(128) COMMENT '最后一次提交版本号',
  `commit_msg` VARCHAR(256) COMMENT '提交相关信息',
  `commit_time` DATETIME COMMENT '提交时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='last_commit 存储表';

DROP TABLE IF EXISTS `eh_butt_script_publish_info`;


CREATE TABLE `eh_butt_script_publish_info` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `info_type` VARCHAR(64) COMMENT '分类 ,对应 eh_butt_script_config 表',
  `namespace_id` INTEGER COMMENT '域空间ID',
  `commit_version` VARCHAR(64) COMMENT '版本号',
  `publish_time` DATETIME COMMENT '版本发布 时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='版本信息及发布信息表';

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
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT DEFAULT 0,
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
  `shared_area` DOUBLE COMMENT '公摊面积',
  `charge_area` DOUBLE COMMENT '收费面积',
  `build_area` DOUBLE COMMENT '建筑面积',
  `rent_area` DOUBLE COMMENT '出租面积',
  `namespace_community_type` VARCHAR(128),
  `namespace_community_token` VARCHAR(128),
  `community_number` VARCHAR(64) COMMENT '项目编号',
  `free_area` DOUBLE COMMENT '可招租面积',
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
  KEY `i_eh_community_stag2` (`string_tag2`),
  KEY `i_feedback_forum_id_status` (`feedback_forum_id`,`status`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_community_approve`;


CREATE TABLE `eh_community_approve` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `organization_id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT,
  `module_type` VARCHAR(64),
  `project_id` BIGINT DEFAULT 0,
  `project_type` VARCHAR(64),
  `approve_name` VARCHAR(64),
  `status` TINYINT NOT NULL DEFAULT 1,
  `form_origin_id` BIGINT,
  `form_version` BIGINT,
  `update_time` DATETIME,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_community_approve_requests`;


CREATE TABLE `eh_community_approve_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT,
  `module_type` VARCHAR(64),
  `flow_case_id` BIGINT DEFAULT 0,
  `form_origin_id` BIGINT,
  `form_version` BIGINT,
  `approve_id` BIGINT DEFAULT 0,
  `approve_name` VARCHAR(64),
  `requestor_name` VARCHAR(64),
  `requestor_phone` VARCHAR(64),
  `requestor_company` VARCHAR(64),
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_community_building_geos`;


CREATE TABLE `eh_community_building_geos` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `building_id` BIGINT NOT NULL DEFAULT 0,
  `building_name` VARCHAR(128),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: confirming, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- By lei.lv
-- 关系表建表脚本
DROP TABLE IF EXISTS `eh_community_default`;


CREATE TABLE `eh_community_default` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `origin_community_id` BIGINT NOT NULL,
  `target_community_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_community_general_form`;


CREATE TABLE `eh_community_general_form` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `community_id` BIGINT COMMENT '项目ID',
  `form_origin_id` BIGINT COMMENT '表单formOriginID',
  `type` VARCHAR(32) COMMENT '类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='表单与项目关系表';

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


DROP TABLE IF EXISTS `eh_community_map_infos`;


CREATE TABLE `eh_community_map_infos` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refering to eh_communities',
  `map_uri` VARCHAR(128),
  `map_name` VARCHAR(128),
  `version` VARCHAR(128),
  `center_longitude` DOUBLE,
  `center_latitude` DOUBLE,
  `north_east_longitude` DOUBLE,
  `north_east_latitude` DOUBLE,
  `south_west_longitude` DOUBLE,
  `south_west_latitude` DOUBLE,
  `longitude_delta` DOUBLE,
  `latitude_delta` DOUBLE,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: confirming, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_community_map_search_types`;


CREATE TABLE `eh_community_map_search_types` (
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

DROP TABLE IF EXISTS `eh_community_map_shops`;
--
-- member of eh_communities partition
CREATE TABLE `eh_community_map_shops` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the type, community, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `shop_name` VARCHAR(128),
  `shop_type` VARCHAR(128),
  `business_hours` VARCHAR(512),
  `contact_name` VARCHAR(128),
  `contact_phone` VARCHAR(128),
  `building_id` BIGINT NOT NULL,
  `address_id` BIGINT NOT NULL,
  `description` TEXT,
  `shop_Avatar_uri` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
--
DROP TABLE IF EXISTS `eh_community_organization_detail_display`;
CREATE TABLE `eh_community_organization_detail_display` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `detail_flag` TINYINT NOT NULL DEFAULT 0,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `value` VARCHAR(1024),
  `description` VARCHAR(256),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `display_name` VARCHAR(128),
  `is_readonly` int(3) COMMENT '是否只读：1，是 ；null 或其他值为 否',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_configurations_record_change`;


CREATE TABLE `eh_configurations_record_change` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `conf_pre_json` VARCHAR(1024) COMMENT '变动前信息JSON字符串',
  `conf_aft_json` VARCHAR(1024) COMMENT '变动后信息JSON字符串',
  `record_change_type` int(3) COMMENT '变动类型。0，新增；1，修改；3，删除',
  `operator_uid` BIGINT COMMENT '操作人userId',
  `operate_time` DATETIME COMMENT '操作时间',
  `operator_ip` VARCHAR(50) COMMENT '操作者的IP地址',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='配置项信息变更记录表';

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
  `resource_md5` VARCHAR(128),
  `resource_type` INTEGER NOT NULL COMMENT 'current support audio,image and video',
  `resource_size` INTEGER NOT NULL,
  `resource_name` VARCHAR(1024) NOT NULL,
  `metadata` TEXT,

  PRIMARY KEY (`id`),
  KEY `i_eh_resource_id` (`resource_id`(20)),
  KEY `i_eh_content_resource_owner` (`owner_id`),
  KEY `i_eh_content_resource_md5` (`resource_md5`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_content_shard_map`;


CREATE TABLE `eh_content_shard_map` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `sharding_domain` VARCHAR(32) NOT NULL,
  `sharding_page` BIGINT,
  `shard_id` INTEGER,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_csm_domain_page` (`sharding_domain`,`sharding_page`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_contract_attachments`;


CREATE TABLE `eh_contract_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `contract_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
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
  `address_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_contract_categories`;


CREATE TABLE `eh_contract_categories` (
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
  `delete_uid` BIGINT DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `logo_uri` VARCHAR(1024) COMMENT 'default cover uri',
  `entry_id` INTEGER,
  `contract_application_scene` TINYINT NOT NULL DEFAULT 0 COMMENT '0 租赁合同场景 1 物业合同场景 2 综合合同场景',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_contract_charging_change_addresses`;

CREATE TABLE `eh_contract_charging_change_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `charging_change_id` BIGINT NOT NULL COMMENT 'id of eh_contract_charging_changes',
  `address_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_contract_charging_changes`;

CREATE TABLE `eh_contract_charging_changes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `charging_item_id` BIGINT COMMENT '收费项',
  `change_type` TINYINT COMMENT '1: 调租; 2: 免租',
  `change_method` TINYINT COMMENT '1: 按金额递增; 2: 按金额递减; 3: 按比例递增; 4: 按比例递减',
  `change_period` INTEGER,
  `period_unit` TINYINT COMMENT '1: 天; 2: 月; 3: 年',
  `change_range` DECIMAL(10,2),
  `change_start_time` DATETIME,
  `change_expired_time` DATETIME,
  `remark` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `change_duration_days` INTEGER COMMENT '变化的天数，例如免租了xx天',
  `bill_group_id` BIGINT COMMENT '账单组ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_contract_charging_item_addresses`;


CREATE TABLE `eh_contract_charging_item_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_charging_item_id` BIGINT NOT NULL COMMENT 'id of eh_contract_charging_items',
  `address_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_contract_charging_items`;


CREATE TABLE `eh_contract_charging_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `charging_item_id` BIGINT COMMENT '收费项',
  `charging_standard_id` BIGINT COMMENT '收费标准',
  `formula` VARCHAR(1024),
  `formula_type` TINYINT COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` TINYINT,
  `late_fee_standard_id` BIGINT COMMENT '滞纳金标准',
  `charging_variables` VARCHAR(1024) COMMENT '计费金额参数 json: {"variables":[{"variableIdentifier":"22","variableName":"面积","variableValue":"960.00"}]}',
  `charging_start_time` DATETIME,
  `charging_expired_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `bill_group_id` BIGINT COMMENT '账单组ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 合同管理V2.9 #30013

DROP TABLE IF EXISTS `eh_contract_events`;


CREATE TABLE `eh_contract_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `contract_id` BIGINT NOT NULL COMMENT '该日志事件对应的合同id',
  `operator_uid` BIGINT COMMENT '修改人员id',
  `opearte_time` DATETIME NOT NULL COMMENT '修改时间',
  `opearte_type` TINYINT NOT NULL COMMENT '操作类型（1：增加，2：删除，3：修改）',
  `content` TEXT COMMENT '修改内容描述'
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同日志记录表';

DROP TABLE IF EXISTS `eh_contract_param_group_map`;


CREATE TABLE `eh_contract_param_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `param_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_contract_params',
  `group_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: notify group, 2: pay group',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  `name` VARCHAR(256) COMMENT '部门名',
  `create_time` DATETIME,
  `user_id` BIGINT DEFAULT 0 COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_contract_params`;


CREATE TABLE `eh_contract_params` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT COMMENT '园区id',
  `expiring_period` INTEGER NOT NULL DEFAULT 0 COMMENT '合同到期日前多久为即将到期合同',
  `expiring_unit` TINYINT NOT NULL DEFAULT 0 COMMENT '单位：0: 分; 1: 小时; 2: 天; 3: 月; 4: 年',
  `notify_period` INTEGER NOT NULL DEFAULT 0 COMMENT '提醒时间',
  `notify_unit` TINYINT NOT NULL DEFAULT 0 COMMENT '单位：0: 分; 1: 小时; 2: 天; 3: 月; 4: 年',
  `expired_period` INTEGER NOT NULL DEFAULT 0 COMMENT '审批通过合同转为过期的时间',
  `expired_unit` TINYINT NOT NULL DEFAULT 0 COMMENT '单位：0: 分; 1: 小时; 2: 天; 3: 月; 4: 年',
  `receivable_date` INTEGER NOT NULL DEFAULT 0 COMMENT '合同费用清单应收日期',
  `receivable_unit` TINYINT NOT NULL DEFAULT 0 COMMENT '单位：0: 分; 1: 小时; 2: 天; 3: 月; 4: 年',
  `paid_period` INTEGER NOT NULL DEFAULT 0 COMMENT '付款日期',
  `payorreceive_contract_type` tinyint(2) DEFAULT 0 COMMENT '0 收款合同 1付款合同',
  `contract_number_rulejson` TEXT COMMENT '合同规则',
  `update_time` DATETIME COMMENT '更新时间',
  `category_id` BIGINT COMMENT 'contract category id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_contract_payment_plans`;


CREATE TABLE `eh_contract_payment_plans` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `paid_amount` DECIMAL(10,2) COMMENT '应付金额',
  `paid_time` DATETIME COMMENT '应付日期',
  `remark` VARCHAR(256) COMMENT '备注',
  `notify_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no; 1: notified',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_contract_templates`;


CREATE TABLE `eh_contract_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `category_id` BIGINT NOT NULL COMMENT 'contract category id 用于多入口',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `name` VARCHAR(64) NOT NULL COMMENT '合同模板名称',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: confirming, 2: active',
  `content_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'gogs:存储gogs的commitId txt:存储文本内容',
  `contents` LONGTEXT COMMENT '模板内容',
  `last_commit` VARCHAR(40) COMMENT 'repository last commit id',
  `parent_id` BIGINT DEFAULT 0 COMMENT '复制于哪个合同模板',
  `version` INTEGER DEFAULT 0 COMMENT '版本记录',
  `contract_template_type` tinyint(2) DEFAULT 0 COMMENT '0 收款合同模板 1付款合同模板',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `delete_uid` BIGINT DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME COMMENT '删除时间',
  `update_uid` BIGINT DEFAULT 0 COMMENT 'record update user id',
  `update_time` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同模板打印表';

DROP TABLE IF EXISTS `eh_contracts`;


CREATE TABLE `eh_contracts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `contract_number` VARCHAR(128) NOT NULL,
  `contract_end_date` DATETIME COMMENT '合同结束日期',
  `status` TINYINT,
  `create_time` DATETIME,
  `community_id` BIGINT COMMENT '园区id',
  `contract_start_date` DATETIME COMMENT '合同开始日期',
  `rent_cycle` INTEGER COMMENT '租赁周期',
  `name` VARCHAR(128) COMMENT '合同名称',
  `contract_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0:新签合同、1:续约合同、2:变更合同、3:退约合同',
  `create_uid` BIGINT COMMENT '经办人id',
  `party_a_type` TINYINT NOT NULL DEFAULT 0 COMMENT '合同甲方类型 0: organization; 1: individual',
  `party_a_id` BIGINT COMMENT '合同甲方id',
  `party_a_name` VARCHAR(64) COMMENT '合同甲方名称',
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `contract_situation` TEXT COMMENT '合同情况',
  `category_item_id` BIGINT COMMENT '合同类型: 资源租赁合同、物业服务合同、车位服务合同..., refer to the id of eh_var_field_items',
  `category_item_name` VARCHAR(128) COMMENT '合同类型: 资源租赁合同、物业服务合同、车位服务合同..., refer to the display_name of eh_var_field_items',
  `advanced_notify_days` INTEGER NOT NULL DEFAULT 0 COMMENT '提前提醒天数',
  `filing_place` VARCHAR(64) COMMENT '归档地',
  `record_number` VARCHAR(32) COMMENT '备案号',
  `invalid_uid` BIGINT COMMENT '作废人id',
  `invalid_time` DATETIME COMMENT '作废时间',
  `invalid_reason` VARCHAR(256) COMMENT '作废原因',
  `review_uid` BIGINT COMMENT '审阅人id',
  `review_time` DATETIME COMMENT '审阅时间',
  `delete_uid` BIGINT COMMENT '删除人id',
  `delete_time` DATETIME COMMENT '删除时间',
  `signed_time` DATETIME COMMENT '签约时间',
  `parent_id` BIGINT COMMENT '父合同id',
  `root_parent_id` BIGINT COMMENT '根合同id',
  `rent_size` DOUBLE COMMENT '出租面积',
  `rent` DECIMAL(10,2) COMMENT '租金',
  `downpayment` DECIMAL(10,2) COMMENT '首付款',
  `downpayment_time` DATETIME COMMENT '首付截止日期',
  `deposit` DECIMAL(10,2) COMMENT '定金',
  `deposit_time` DATETIME COMMENT '定金最迟收取日期',
  `contractual_penalty` DECIMAL(10,2) COMMENT '违约金',
  `penalty_remark` VARCHAR(256) COMMENT '违约说明',
  `commission` DECIMAL(10,2) COMMENT '佣金',
  `paid_type` VARCHAR(32) COMMENT '付款方式',
  `free_days` INTEGER COMMENT '免租期天数',
  `free_parking_space` INTEGER COMMENT '赠送车位数量',
  `decorate_begin_date` DATETIME COMMENT '装修开始日期',
  `decorate_end_date` DATETIME COMMENT '装修结束日期',
  `signed_purpose` VARCHAR(128) COMMENT '签约原因',
  `source` VARCHAR(32) COMMENT 'contract source like zuolin...',
  `source_id` VARCHAR(128) COMMENT 'contract source unique identifier...',
  `denunciation_reason` VARCHAR(256) COMMENT '为退约合同的时候',
  `denunciation_time` DATETIME COMMENT '为退约合同的时候',
  `denunciation_uid` BIGINT COMMENT '为退约合同的时候',
  `remark` TEXT COMMENT '备注',
  `settled` VARCHAR(128),
  `layout` VARCHAR(128),
  `version` VARCHAR(32) COMMENT '版本号',
  `building_rename` VARCHAR(64) COMMENT '房间别名',
  `namespace_contract_type` VARCHAR(128),
  `namespace_contract_token` VARCHAR(128),
  `remaining_amount` DECIMAL(10,2) COMMENT '剩余金额',
  `bid_item_id` BIGINT COMMENT '是否通过招投标',
  `create_org_id` BIGINT COMMENT '经办部门',
  `create_position_id` BIGINT COMMENT '岗位',
  `our_legal_representative` VARCHAR(256) COMMENT '我方法人代表',
  `taxpayer_identification_code` VARCHAR(256) COMMENT '纳税人识别码',
  `registered_address` VARCHAR(512) COMMENT '注册地址',
  `registered_phone` VARCHAR(256) COMMENT '注册电话',
  `payee` VARCHAR(256) COMMENT '收款单位',
  `payer` VARCHAR(256) COMMENT '付款单位',
  `due_bank` VARCHAR(256) COMMENT '收款银行',
  `bank_account` VARCHAR(256) COMMENT '银行账号',
  `exchange_rate` DECIMAL(10,2) COMMENT '兑换汇率',
  `age_limit` INTEGER COMMENT '年限',
  `application_id` BIGINT COMMENT '关联请示单',
  `payment_mode_item_id` BIGINT COMMENT '预计付款方式',
  `paid_time` DATETIME COMMENT '预计付款时间',
  `lump_sum_payment` DECIMAL(10,2) COMMENT '一次性付款金额',
  `treaty_particulars` TEXT COMMENT '合同摘要',
  `payment_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0:普通合同；1：付款合同',
  `update_time` DATETIME,
  `category_id` BIGINT COMMENT 'contract category id',
  `template_id` BIGINT COMMENT 'contract template_id',
  `cost_generation_method` TINYINT COMMENT '合同截断时的费用收取方式，0：按计费周期，1：按实际天数',
  `sponsor_uid` BIGINT COMMENT '发起人id',
  `sponsor_time` DATETIME COMMENT '发起时间',
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


DROP TABLE IF EXISTS `eh_customer_accounts`;


CREATE TABLE `eh_customer_accounts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `bank_name` VARCHAR(128) COMMENT '开户行名称',
  `branch_name` VARCHAR(128) COMMENT '开户网点',
  `account_holder` VARCHAR(128) COMMENT '开户人',
  `account_number` VARCHAR(128) COMMENT '账号',
  `account_number_type_id` BIGINT COMMENT '账号类型',
  `branch_province` VARCHAR(128) COMMENT '开户行所在省',
  `branch_city` VARCHAR(128) COMMENT '开户行所在市',
  `account_type_id` BIGINT COMMENT '账户类型 refer to the id of eh_var_field_items',
  `contract_id` BIGINT COMMENT '合同 refer to the id of eh_contracts',
  `memo` VARCHAR(128) COMMENT '备注',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_apply_projects`;


CREATE TABLE `eh_customer_apply_projects` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `project_name` VARCHAR(128) COMMENT '获批项目名称',
  `project_source` VARCHAR(128) COMMENT 'json of id list from eh_var_field_items and customer input text, split by ,',
  `project_establish_date` DATETIME COMMENT '项目立项日期',
  `project_complete_date` DATETIME COMMENT '项目完成日期',
  `project_amount` DECIMAL(10,2) COMMENT '获批项目金额 “万元”为单位',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `description` TEXT COMMENT '主要项目介绍',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_attachments`;


CREATE TABLE `eh_customer_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `name` VARCHAR(1024),
  `namespace_id` INTEGER NOT NULL COMMENT 'namespaceId',
  `customer_id` BIGINT NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `status` TINYINT NOT NULL COMMENT '0:inactive 2:active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='企业管理1.0 附件表';

DROP TABLE IF EXISTS `eh_customer_certificates`;


CREATE TABLE `eh_customer_certificates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `name` VARCHAR(128) COMMENT '证书名称',
  `certificate_number` VARCHAR(128) COMMENT '证书编号',
  `registe_date` DATETIME COMMENT '注册日期',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `buy_patents` INTEGER COMMENT '购买国外专利',
  `technology_contracts` INTEGER COMMENT '技术合同交易数量',
  `technology_contract_amount` INTEGER COMMENT '技术合同交易总金额（万元）',
  `national_projects` INTEGER COMMENT '当年承担国家级科技计划项目数',
  `provincial_awards` INTEGER COMMENT '当年获得省级以上奖励',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_commercials`;


CREATE TABLE `eh_customer_commercials` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: enterprise; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `enterprise_type_item_id` BIGINT COMMENT '企业类型: 企业、事业单位、政府机关、社会团体、民办非企业单位、基金会、其他组织机构..., refer to the id of eh_var_field_items',
  `enterprise_type_item_name` VARCHAR(128) COMMENT '企业类型: 企业、事业单位、政府机关、社会团体、民办非企业单位、基金会、其他组织机构..., refer to the display_name of eh_var_field_items',
  `share_type_item_id` BIGINT COMMENT '控股情况: 国有控股、集体控股、私人控股、港澳台商控股、外商投资、其他..., refer to the id of eh_var_field_items',
  `share_type_item_name` VARCHAR(128) COMMENT '控股情况: 国有控股、集体控股、私人控股、港澳台商控股、外商投资、其他..., refer to the display_name of eh_var_field_items',
  `contact` VARCHAR(32) COMMENT '联系人',
  `contact_number` VARCHAR(32) COMMENT '联系电话',
  `unified_social_credit_code` VARCHAR(64) COMMENT '统一社会信用代码',
  `business_scope` VARCHAR(128) COMMENT '主营业务',
  `foundation_date` DATETIME COMMENT '成立日期',
  `tax_registration_date` DATETIME COMMENT '税务登记日期',
  `validity_begin_date` DATETIME,
  `validity_end_date` DATETIME,
  `registered_addr` VARCHAR(128),
  `registered_capital` DECIMAL(10,2) COMMENT '注册资金',
  `paidup_apital` DECIMAL(10,2) COMMENT '实到资金',
  `property_type` BIGINT COMMENT ' refer to the id of eh_var_field_items',
  `change_date` DATETIME COMMENT '变更日期',
  `business_licence_date` DATETIME,
  `liquidation_committee_recored_date` DATETIME COMMENT '清算组备案日期',
  `cancel_date` DATETIME COMMENT '注销日期',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `main_business` VARCHAR(256) COMMENT '主营业务',
  `branch_company_name` VARCHAR(256) COMMENT '分公司名称',
  `branch_registered_date` DATETIME COMMENT '分公司登记日期',
  `legal_representative_name` VARCHAR(256) COMMENT '法人代表名称',
  `legal_representative_contact` VARCHAR(256) COMMENT '法人联系方式',
  `shareholder_name` VARCHAR(256) COMMENT '股东姓名',
  `actual_capital_injection_situation` VARCHAR(256) COMMENT '实际注资情况',
  `shareholding_situation` VARCHAR(256) COMMENT '股权占比情况',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_configutations`;


CREATE TABLE `eh_customer_configutations` (
  `id` BIGINT NOT NULL,
  `scope_type` VARCHAR(64) COMMENT 'service_alliance or activity',
  `scope_id` BIGINT NOT NULL COMMENT 'code',
  `value` TINYINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: invalid, 2: valid',
  `namespace_id` INTEGER NOT NULL,
  `create_time` DATETIME COMMENT 'record create time',
  `creator_uid` BIGINT COMMENT 'creatorUid',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_contacts`;


CREATE TABLE `eh_customer_contacts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespaceId',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'communityId',
  `customer_id` BIGINT NOT NULL DEFAULT 0 COMMENT '关联的客户ID',
  `name` VARCHAR(64) COMMENT '联系人名称',
  `phone_number` VARCHAR(64),
  `email` VARCHAR(128) COMMENT '联系人邮箱',
  `position` VARCHAR(128) COMMENT '联系人职务',
  `address` VARCHAR(256) COMMENT '联系人通讯地址',
  `contact_type` TINYINT COMMENT '联系人类型，0-客户联系人、1-渠道联系人',
  `customer_source` TINYINT COMMENT '联系人来源，0-客户管理，1-租客管理',
  `status` TINYINT COMMENT '联系人状态，0-invalid ,2-valid',
  `create_time` DATETIME COMMENT '创建日期',
  `creator_uid` BIGINT COMMENT '创建人',
  `operator_time` DATETIME COMMENT '最近修改时间',
  `operator_uid` BIGINT COMMENT '最近修改人',
  PRIMARY KEY (`id`),
  KEY `idx_namespace_id` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='招商客户联系人表';

DROP TABLE IF EXISTS `eh_customer_current_rents`;


CREATE TABLE `eh_customer_current_rents` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespaceId',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'communityId',
  `customer_id` BIGINT NOT NULL DEFAULT 0 COMMENT '关联的客户ID',
  `address` VARCHAR(256) COMMENT '当前地址',
  `rent_price` DECIMAL(10,2) COMMENT '当前租金',
  `rent_price_unit` TINYINT COMMENT '租金单位，0-元/㎡，1-元/㎡/月,2-元/天，3-元/月，4-元',
  `rent_area` DECIMAL(10,2) COMMENT '当前租赁面积',
  `contract_intention_date` DATETIME COMMENT '当前合同到期日',
  `version` BIGINT COMMENT '记录版本',
  `status` TINYINT COMMENT '状态，0-invalid ,2-valid',
  `create_time` DATETIME COMMENT '创建日期',
  `creator_uid` BIGINT COMMENT '创建人',
  `operator_time` DATETIME COMMENT '最近修改时间',
  `operator_uid` BIGINT COMMENT '最近修改人',
  PRIMARY KEY (`id`),
  KEY `idx_namespace_id` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='招商客户当前信息表';

DROP TABLE IF EXISTS `eh_customer_departure_infos`;


CREATE TABLE `eh_customer_departure_infos` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `review_time` DATETIME COMMENT '评审时间',
  `hatch_months` INTEGER COMMENT '孵化时间(X月)',
  `departure_nature_id` BIGINT COMMENT '离场性质',
  `departure_direction_id` BIGINT COMMENT '离场去向',
  `remark` VARCHAR(512) COMMENT '备注',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_economic_indicator_statistics`;


CREATE TABLE `eh_customer_economic_indicator_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `turnover` DECIMAL(10,2) COMMENT '营业额',
  `tax_payment` DECIMAL(10,2) COMMENT '纳税额',
  `start_time` DATETIME,
  `end_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 2,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_economic_indicators`;


CREATE TABLE `eh_customer_economic_indicators` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `total_assets` DECIMAL(10,2) COMMENT '资产总计',
  `total_profit` DECIMAL(10,2) COMMENT '利润总额',
  `sales` DECIMAL(10,2) COMMENT '销售额',
  `turnover` DECIMAL(10,2) COMMENT '营业额',
  `tax_index` DECIMAL(10,2) COMMENT '税收指标',
  `tax_payment` DECIMAL(10,2) COMMENT '纳税额',
  `value_added_tax` DECIMAL(10,2) COMMENT '增值税',
  `business_tax` DECIMAL(10,2) COMMENT '营业税',
  `business_income_tax` DECIMAL(10,2) COMMENT '企业所得税',
  `foreign_company_income_tax` DECIMAL(10,2) COMMENT '外企所得税',
  `individual_income_tax` DECIMAL(10,2) COMMENT '个人所得税',
  `total_tax_amount` DECIMAL(10,2) COMMENT '税额合计',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `month` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_entry_infos`;


CREATE TABLE `eh_customer_entry_infos` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `area` VARCHAR(128) COMMENT '区域',
  `address` VARCHAR(128) COMMENT '地址',
  `building_id` BIGINT COMMENT '楼栋id',
  `address_id` BIGINT COMMENT '楼栋门牌id',
  `area_size` DECIMAL(10,2) COMMENT '面积',
  `contract_start_date` DATETIME COMMENT '合同开始日期',
  `contract_end_date` DATETIME COMMENT '合同结束日期',
  `contract_end_month` INTEGER COMMENT '合同结束月份',
  `remark` VARCHAR(512) COMMENT '企业评级',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_events`;


CREATE TABLE `eh_customer_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id` BIGINT COMMENT '所属客户id',
  `customer_name` VARCHAR(128) COMMENT '客户名称',
  `contact_name` VARCHAR(64),
  `content` TEXT,
  `device_type` TINYINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT COMMENT '创建人uid',
  `create_time` DATETIME,
  `investment_type` TINYINT COMMENT '操作客户类型，0-客户管理，1-租客管理',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_investments`;


CREATE TABLE `eh_customer_investments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `government_project` VARCHAR(128),
  `bank_loans` DECIMAL(10,2) COMMENT '银行贷款',
  `equity_financing` DECIMAL(10,2) COMMENT '股权融资',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `investment_time` DATETIME COMMENT '时间',
  `investment_round` VARCHAR(64) COMMENT 'xx轮',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_patents`;


CREATE TABLE `eh_customer_patents` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `name` VARCHAR(128) COMMENT '证书名称',
  `registe_date` DATETIME COMMENT '注册日期',
  `patent_status_item_id` BIGINT COMMENT '专利状态 申请 授权..., refer to the id of eh_var_field_items',
  `patent_status_item_name` VARCHAR(128) COMMENT '专利状态 申请 授权..., refer to the display_name of eh_var_field_items',
  `patent_type_item_id` BIGINT COMMENT '专利类型 发明专利实用新型外观设计集成电路布图软件著作权证书..., refer to the id of eh_var_field_items',
  `patent_type_item_name` VARCHAR(128) COMMENT '专利类型 发明专利实用新型外观设计集成电路布图软件著作权证书..., refer to the display_name of eh_var_field_items',
  `patent_name` VARCHAR(128) COMMENT '专利名称',
  `application_number` VARCHAR(64) COMMENT '授权号',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `effective_intellectual_properties` INTEGER COMMENT '拥有有效知识产权总数',
  `patents` INTEGER COMMENT '发明专利',
  `software_copyrights` INTEGER COMMENT '软件著作权',
  `ic_layout` INTEGER COMMENT '集成电路布图',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_potential_datas`;


CREATE TABLE `eh_customer_potential_datas` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` TEXT COMMENT 'potential customer name',
  `source_id` BIGINT COMMENT 'refer to service allance activity categoryId',
  `source_type` VARCHAR(1024) COMMENT 'service_alliance or activity',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: invalid, 2: valid',
  `operate_uid` BIGINT,
  `update_time` DATETIME,
  `create_time` DATETIME NOT NULL,
  `delete_time` DATETIME,
  `delete_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_requirement_addresses`;


CREATE TABLE `eh_customer_requirement_addresses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespaceId',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'communityId',
  `requirement_id` BIGINT NOT NULL DEFAULT 0 COMMENT '关联的需求ID',
  `customer_id` BIGINT NOT NULL DEFAULT 0 COMMENT '关联的客户ID',
  `address_id` BIGINT COMMENT '意向房源',
  `status` TINYINT COMMENT '状态，0-invalid ,2-valid',
  `create_time` DATETIME COMMENT '创建日期',
  `creator_uid` BIGINT COMMENT '创建人',
  `operator_time` DATETIME COMMENT '最近修改时间',
  `operator_uid` BIGINT COMMENT '最近修改人',
  PRIMARY KEY (`id`),
  KEY `idx_namespace_id` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='招商客户需求房源关系表';

DROP TABLE IF EXISTS `eh_customer_requirements`;


CREATE TABLE `eh_customer_requirements` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespaceId',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'communityId',
  `customer_id` BIGINT NOT NULL DEFAULT 0 COMMENT '关联的客户ID',
  `intention_location` VARCHAR(256) COMMENT '期望地段',
  `min_area` DECIMAL(10,2) COMMENT '期望最小面积',
  `max_area` DECIMAL(10,2) COMMENT '期望最大面积',
  `min_rent_price` DECIMAL(10,2) COMMENT '期望最小租金-单价',
  `max_rent_price` DECIMAL(10,2) COMMENT '期望最大租金-单价',
  `rent_price_unit` TINYINT COMMENT '期望租金单位，0-元/㎡，1-元/㎡/月,2-元/天，3-元/月，4-元',
  `rent_type` TINYINT COMMENT '租赁/购买：0-租赁，1-购买',
  `version` BIGINT COMMENT '记录版本',
  `status` TINYINT COMMENT '状态，0-invalid ,2-valid',
  `create_time` DATETIME COMMENT '创建日期',
  `creator_uid` BIGINT COMMENT '创建人',
  `operator_time` DATETIME COMMENT '最近修改时间',
  `operator_uid` BIGINT COMMENT '最近修改人',
  PRIMARY KEY (`id`),
  KEY `idx_namespace_id` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='eh_enterprise_investment_demand in dev mode';

DROP TABLE IF EXISTS `eh_customer_talents`;


CREATE TABLE `eh_customer_talents` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `name` VARCHAR(64),
  `gender` BIGINT COMMENT '性别 refer to the id of eh_var_field_items',
  `phone` VARCHAR(32),
  `nationality_item_id` BIGINT COMMENT '国籍, refer to the id of eh_var_field_items',
  `degree_item_id` BIGINT COMMENT '最高学历, refer to the id of eh_var_field_items',
  `graduate_school` VARCHAR(128) COMMENT '毕业学校',
  `major` VARCHAR(128) COMMENT '所属专业',
  `experience` INTEGER COMMENT '工作经验',
  `returnee_flag` BIGINT COMMENT '是否海归 refer to the id of eh_var_field_items',
  `abroad_item_id` BIGINT COMMENT '留学国家, refer to the id of eh_var_field_items',
  `job_position` VARCHAR(128),
  `technical_title_item_id` BIGINT COMMENT '技术职称, refer to the id of eh_var_field_items',
  `individual_evaluation_item_id` BIGINT COMMENT '个人评定, refer to the id of eh_var_field_items',
  `personal_certificate` VARCHAR(256) COMMENT '个人证书',
  `career_experience` TEXT COMMENT '主要职业经历',
  `remark` TEXT,
  `status` TINYINT DEFAULT 2 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `total_employees` INTEGER COMMENT '从业人员总人数',
  `junior_colleges` INTEGER COMMENT '大专',
  `undergraduates` INTEGER COMMENT '本科',
  `masters` INTEGER COMMENT '硕士',
  `doctors` INTEGER COMMENT '博士',
  `overseas` INTEGER COMMENT '留学人员',
  `thousand_talents_program` INTEGER COMMENT '千人计划人数',
  `fresh_graduates` INTEGER COMMENT '吸纳应届大学毕业生',
  `age` INTEGER COMMENT '年龄',
  `register_status` TINYINT NOT NULL DEFAULT 0,
  `origin_source_type` VARCHAR(64) COMMENT 'service_alliance or activity',
  `origin_source_id` BIGINT COMMENT 'origin potential data primary key',
  `talent_source_item_id` BIGINT COMMENT 'categoryId',
  `member_id` BIGINT NOT NULL DEFAULT 0 COMMENT '通讯录表中的id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_taxes`;


CREATE TABLE `eh_customer_taxes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `tax_name` VARCHAR(128) COMMENT '报税人',
  `tax_no` VARCHAR(128) COMMENT '报税人税号',
  `tax_address` VARCHAR(128) COMMENT '地址',
  `tax_phone` VARCHAR(128) COMMENT '联系电话',
  `tax_bank` VARCHAR(128) COMMENT '开户行名称',
  `tax_bank_no` VARCHAR(128) COMMENT '开户行账号',
  `tax_payer_type_id` BIGINT COMMENT '报税人类型 refer to the id of eh_var_field_items',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_trackers`;


CREATE TABLE `eh_customer_trackers` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespaceId',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'communityId',
  `customer_id` BIGINT NOT NULL DEFAULT 0 COMMENT '关联的客户ID',
  `tracker_uid` BIGINT COMMENT '跟进人id',
  `tracker_type` TINYINT COMMENT '跟进人类型，0-招商跟进人、1-租户拜访人',
  `customer_source` TINYINT COMMENT '联系人来源，0-客户管理，1-租客管理',
  `status` TINYINT COMMENT '状态，0-invalid ,2-valid',
  `create_time` DATETIME COMMENT '创建日期',
  `creator_uid` BIGINT COMMENT '创建人',
  `operator_time` DATETIME COMMENT '最近修改时间',
  `operator_uid` BIGINT COMMENT '最近修改人',
  PRIMARY KEY (`id`),
  KEY `idx_namespace_id` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='招商客户跟进人表';

DROP TABLE IF EXISTS `eh_customer_tracking_plans`;


CREATE TABLE `eh_customer_tracking_plans` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id` BIGINT COMMENT '所属客户id',
  `customer_name` VARCHAR(128) COMMENT '客户名称',
  `contact_name` VARCHAR(64) COMMENT '联系人',
  `tracking_type` BIGINT COMMENT '计划跟进类型',
  `tracking_time` DATETIME COMMENT '跟进时间',
  `notify_time` DATETIME COMMENT '提醒时间',
  `title` VARCHAR(128) COMMENT '标题',
  `content` TEXT COMMENT '内容',
  `status` TINYINT NOT NULL DEFAULT 2,
  `creator_uid` BIGINT COMMENT '创建人uid',
  `create_time` DATETIME COMMENT '创建时间',
  `update_uid` BIGINT COMMENT '修改人uid',
  `update_time` DATETIME COMMENT '修改时间',
  `delete_uid` BIGINT COMMENT '删除人uid',
  `delete_time` DATETIME COMMENT '删除时间',
  `notify_status` TINYINT COMMENT '提醒状态  0:无需提醒   1:待提醒   2:已提醒',
  `read_status` TINYINT DEFAULT 0 COMMENT 'is read?  0:no  1:yes',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_trackings`;


CREATE TABLE `eh_customer_trackings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT '域空间id',
  `customer_type` TINYINT NOT NULL COMMENT '所属客户类型',
  `customer_id` BIGINT COMMENT '所属客户id',
  `customer_name` VARCHAR(128) COMMENT '客户名称',
  `contact_name` VARCHAR(64) COMMENT '联系人',
  `tracking_type` BIGINT COMMENT '跟进类型',
  `tracking_uid` BIGINT COMMENT '跟进人uid ',
  `intention_grade` INTEGER COMMENT '意向等级',
  `tracking_time` DATETIME COMMENT '跟进时间',
  `content` TEXT COMMENT '跟进内容',
  `contact_phone` VARCHAR(255),
  `visit_person_name` VARCHAR(64),
  `visit_time_length` DECIMAL(10,2),
  `content_img_uri` VARCHAR(2048) COMMENT '跟进内容图片uri',
  `status` TINYINT NOT NULL DEFAULT 2,
  `creator_uid` BIGINT COMMENT '创建人uid',
  `create_time` DATETIME COMMENT '创建时间',
  `update_uid` BIGINT COMMENT '修改人uid',
  `update_time` DATETIME COMMENT '修改时间',
  `delete_uid` BIGINT COMMENT '删除人uid',
  `delete_time` DATETIME COMMENT '删除时间',
  `customer_source` TINYINT COMMENT '跟进信息类型，0-客户跟进信息，1-租客跟进信息',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_customer_trademarks`;


CREATE TABLE `eh_customer_trademarks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `customer_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: organization; 1: individual',
  `customer_id` BIGINT,
  `customer_name` VARCHAR(64),
  `name` VARCHAR(128) COMMENT '商标名称',
  `registe_date` DATETIME COMMENT '注册日期',
  `trademark_type_item_id` BIGINT COMMENT '商标类型: 文字商标、图片商标、品牌商标、著名商标..., refer to the id of eh_var_field_items',
  `trademark_type_item_name` VARCHAR(128) COMMENT '商标类型: 文字商标、图片商标、品牌商标、著名商标..., refer to the display_name of eh_var_field_items',
  `trademark_amount` INTEGER COMMENT '商标数量',
  `status` TINYINT NOT NULL DEFAULT 2,
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `applications` INTEGER COMMENT '知识产权申请总数',
  `patent_applications` INTEGER COMMENT '申请发明专利数',
  `authorizations` INTEGER COMMENT '知识产权授权总数',
  `patent_authorizations` INTEGER COMMENT '授权发明专利数',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_decoration_approval_vals`;


CREATE TABLE `eh_decoration_approval_vals` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `request_id` BIGINT,
  `approval_id` BIGINT,
  `approval_name` VARCHAR(64),
  `flow_case_id` BIGINT,
  `form_origin_id` BIGINT,
  `form_version` BIGINT,
  `delete_flag` TINYINT COMMENT '0未取消 1取消',
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_decoration_atttachment`;


CREATE TABLE `eh_decoration_atttachment` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `setting_id` BIGINT NOT NULL,
  `name` VARCHAR(64),
  `attachment_type` VARCHAR(64) COMMENT '''file''文件 ''fee''费用',
  `size` VARCHAR(32),
  `file_uri` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_decoration_companies`;


CREATE TABLE `eh_decoration_companies` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `organization_id` BIGINT,
  `name` VARCHAR(64),
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_decoration_company_chiefs`;


CREATE TABLE `eh_decoration_company_chiefs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `company_id` BIGINT NOT NULL COMMENT '装修公司的id',
  `name` VARCHAR(64),
  `phone` VARCHAR(64),
  `uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_decoration_fee`;


CREATE TABLE `eh_decoration_fee` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `request_id` BIGINT NOT NULL,
  `fee_name` VARCHAR(64),
  `fee_price` VARCHAR(64),
  `amount` VARCHAR(64),
  `total_price` DECIMAL(20,2),
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_decoration_requests`;


CREATE TABLE `eh_decoration_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `community_id` BIGINT NOT NULL,
  `create_time` DATETIME,
  `start_time` DATETIME,
  `end_time` DATETIME,
  `apply_uid` BIGINT,
  `apply_name` VARCHAR(64),
  `apply_phone` VARCHAR(64),
  `apply_company` VARCHAR(255),
  `address` VARCHAR(255),
  `decorator_uid` BIGINT,
  `decorator_name` VARCHAR(64),
  `decorator_phone` VARCHAR(64),
  `decorator_company_id` BIGINT,
  `decorator_company` VARCHAR(255),
  `decorator_qrid` VARCHAR(255) COMMENT '二维码id',
  `status` TINYINT,
  `cancel_flag` TINYINT COMMENT '0未取消 1工作流取消 2后台取消',
  `cancel_reason` VARCHAR(1024),
  `refound_amount` DECIMAL(18,2) COMMENT '退款金额',
  `refound_comment` VARCHAR(1024) COMMENT '退款备注',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_decoration_setting`;


CREATE TABLE `eh_decoration_setting` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `community_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '''basic'' 基础设置 ''file''装修资料 ''fee''缴费 ''apply''施工申请 ''complete''竣工验收 ''refound''押金退回',
  `owner_id` BIGINT COMMENT '当owner_type为apply 时 表示审批id',
  `content` TEXT,
  `address` VARCHAR(255) COMMENT '收款地址或资料提交地址',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `phone` VARCHAR(64) COMMENT '咨询电话',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_decoration_workers`;


CREATE TABLE `eh_decoration_workers` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `request_id` BIGINT NOT NULL,
  `worker_type` VARCHAR(64),
  `uid` BIGINT,
  `name` VARCHAR(64),
  `phone` VARCHAR(64),
  `image` VARCHAR(255),
  `qrid` VARCHAR(255) COMMENT '二维码id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_default_charging_item_properties`;


CREATE TABLE `eh_default_charging_item_properties` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `default_charging_item_id` BIGINT NOT NULL COMMENT 'id of eh_contract_charging_items',
  `property_type` TINYINT COMMENT '0: community; 1: building; 2: apartment',
  `property_id` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_default_charging_items`;


CREATE TABLE `eh_default_charging_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the charging, organizationowner,asset, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `charging_item_id` BIGINT COMMENT '收费项',
  `charging_standard_id` BIGINT COMMENT '收费标准',
  `formula` VARCHAR(1024),
  `formula_type` TINYINT COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` TINYINT,
  `late_fee_standard_id` BIGINT COMMENT '滞纳金标准',
  `charging_variables` VARCHAR(1024) COMMENT '计费金额参数 json: {"variables":[{"variableIdentifier":"22","variableName":"面积","variableValue":"960.00"}]}',
  `charging_start_time` DATETIME,
  `charging_expired_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: active',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 创建 开发者账号信息表
DROP TABLE IF EXISTS `eh_developer_account_info`;


CREATE TABLE `eh_developer_account_info` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `bundle_ids` VARCHAR(200) COMMENT '关联应用',
  `team_id` VARCHAR(100) COMMENT '关联开发者帐号',
  `authkey_id` VARCHAR(100) COMMENT 'authkey_id',
  `authkey` BLOB COMMENT 'authkey',
  `create_time` DATETIME COMMENT '创建时间',
  `create_name` VARCHAR(50) COMMENT '创建者',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='开发者账号信息表';

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
  `pusher_service_type` VARCHAR(40) COMMENT '推送服务类型：develop或productiom',
  `bundle_id` VARCHAR(100) COMMENT '关联应用',
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

DROP TABLE IF EXISTS `eh_domains`;


CREATE TABLE `eh_domains` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(255),
  `portal_type` VARCHAR(32) NOT NULL COMMENT 'zuolin, pm, enterprise, user',
  `portal_id` BIGINT NOT NULL,
  `domain` VARCHAR(32) NOT NULL COMMENT 'domain',
  `create_uid` BIGINT NOT NULL,
  `create_time` DATETIME,
  `favicon_uri` VARCHAR(255),
  `login_bg_uri` VARCHAR(255),
  `login_logo_uri` VARCHAR(255),
  `menu_logo_uri` VARCHAR(255),
  `menu_logo_collapsed_uri` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_door_access`;


CREATE TABLE `eh_door_access` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `uuid` VARCHAR(64) NOT NULL,
  `door_type` TINYINT NOT NULL COMMENT '0: Zuolin aclink with wifi, 1: Zuolink aclink without wifi',
  `hardware_id` VARCHAR(64) NOT NULL COMMENT 'mac address of aclink',
  `name` VARCHAR(128) NOT NULL,
  `display_name` VARCHAR(128),
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
  `floor_id` VARCHAR(2000) COMMENT '授权楼层',
  `mac_copy` VARCHAR(128) COMMENT '原mac地址',
  `enable_amount` TINYINT COMMENT '是否支持按次数开门的授权',
  `local_server_id` BIGINT COMMENT '服务器id',
  `has_qr` TINYINT NOT NULL DEFAULT 1 COMMENT '门禁二维码能力0无1有',
  `max_duration` INTEGER COMMENT '有效时间最大值(天)',
  `max_count` INTEGER COMMENT '按次授权最大次数',
  `defualt_invalid_duration` INTEGER COMMENT '按次授权默认有效期(天)',
  `enable_duration` TINYINT DEFAULT 1 COMMENT '是否支持按有效期开门',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_door_access_uuid` (`uuid`),
  KEY `i_eh_door_access_name` (`name`),
  KEY `i_eh_door_hardware_id` (`hardware_id`),
  KEY `i_eh_door_access_owner` (`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_door_auth`;


CREATE TABLE `eh_door_auth` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
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
  `key_u` VARCHAR(16) COMMENT '第三方用户秘钥',
  `auth_rule_type` TINYINT COMMENT '授权规则的种类,0 按时间,1 按次数',
  `total_auth_amount` INTEGER COMMENT '授权的总开门次数',
  `valid_auth_amount` INTEGER COMMENT '剩余的开门次数',
  PRIMARY KEY (`id`),
  KEY `fk_eh_door_auth_door_id` (`door_id`),
  KEY `fk_eh_door_auth_user_id` (`user_id`),
  KEY `string_tag3_index` (`string_tag3`),
  CONSTRAINT `eh_door_auth_ibfk_1` FOREIGN KEY (`door_id`) REFERENCES `eh_door_access` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_door_auth_level`;


CREATE TABLE `eh_door_auth_level` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
    `door_id` BIGINT NOT NULL,
    `level_id` BIGINT NOT NULL,
    `level_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
    `operator_id` BIGINT NOT NULL DEFAULT 0,
    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family, 3: user',
    `owner_id` BIGINT NOT NULL,
    `right_open` TINYINT NOT NULL DEFAULT 1,
    `right_visitor` TINYINT NOT NULL DEFAULT 0,
    `right_remote` TINYINT NOT NULL DEFAULT 0,
    `description` VARCHAR(1024),
    `create_time` DATETIME,
    `status` TINYINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_door_auth_logs`;


CREATE TABLE `eh_door_auth_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `door_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `is_auth` TINYINT NOT NULL DEFAULT 0,
  `right_content` VARCHAR(1024) NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `create_uid` BIGINT NOT NULL,
  `discription` TEXT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_door_command`;


CREATE TABLE `eh_door_command` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
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
  PRIMARY KEY (`id`),
  KEY `unionmeter_stat_uniqueIndex` (`meter_id`,`stat_date`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_energy_meter_addresses`;


CREATE TABLE `eh_energy_meter_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `meter_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_groups',
  `building_id` BIGINT NOT NULL DEFAULT 0,
  `building_name` VARCHAR(128),
  `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_addresses',
  `apartment_name` VARCHAR(128),
  `apartment_floor` VARCHAR(16),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `burden_rate` DECIMAL(10,2),
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'redundant auditing info',
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `meter_address_meter_id` (`meter_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
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


DROP TABLE IF EXISTS `eh_energy_meter_category_map`;
CREATE TABLE `eh_energy_meter_category_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'category id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
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

DROP TABLE IF EXISTS `eh_energy_meter_fomular_map`;
CREATE TABLE `eh_energy_meter_fomular_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `fomular_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'fomular id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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

DROP TABLE IF EXISTS `eh_energy_meter_logs`;


CREATE TABLE `eh_energy_meter_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the log, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'plan, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: insert, 2: update, 3: delete',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record operator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
--
DROP TABLE IF EXISTS `eh_energy_meter_price_config`;


CREATE TABLE `eh_energy_meter_price_config` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price configs, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(255) COMMENT 'config name',
  `description` VARCHAR(512) COMMENT 'description config',
  `expression` VARCHAR(1024) COMMENT 'expression json',
  `status` TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_energy_meter_reading_logs`;


CREATE TABLE `eh_energy_meter_reading_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `meter_id` BIGINT,
  `reading` DECIMAL(10,2),
  `operator_id` BIGINT,
  `operate_time` DATETIME,
  `reset_meter_flag` TINYINT DEFAULT 0 COMMENT '0: normal, 1: reset',
  `change_meter_flag` TINYINT DEFAULT 0 COMMENT '0: normal, 1: change',
  `status` TINYINT DEFAULT 2 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `task_id` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_energy_meter_setting_logs`;


CREATE TABLE `eh_energy_meter_setting_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
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
  `formula_source` TINYINT DEFAULT 0 COMMENT '0: 能耗设置, 1: 缴费模块',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_energy_meter_tasks`;


CREATE TABLE `eh_energy_meter_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the group of who own the task, community, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `plan_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_plans',
  `meter_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_meters',
  `executive_start_time` DATETIME,
  `executive_expire_time` DATETIME,
  `last_task_reading` DECIMAL(10,2),
  `reading` DECIMAL(10,2),
  `generate_payment_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未生成, 1: 已生成',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: 未抄, 1: 已抄',
  `default_order` INTEGER DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `plan_id` (`plan_id`),
  KEY `status` (`status`),
  KEY `target_id` (`target_id`),
  KEY `executive_expire_time` (`executive_expire_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `auto_flag` TINYINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price energy meter, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费',
  `config_id` BIGINT COMMENT 'if setting_type is price and  have this value',
  `cost_formula_source` TINYINT DEFAULT 0 COMMENT '0: 能耗设置, 1: 缴费模块',
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

DROP TABLE IF EXISTS `eh_energy_plan_group_map`;


CREATE TABLE `eh_energy_plan_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `plan_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_plans',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_energy_plan_meter_map`;


CREATE TABLE `eh_energy_plan_meter_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `plan_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_plans',
  `meter_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_energy_meters',
  `default_order` INTEGER DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_energy_plans`;


CREATE TABLE `eh_energy_plans` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the plan, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the plan, community, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the plan',
  `name` VARCHAR(1024),
  `repeat_setting_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_repeat_settings',
  `notify_tick_minutes` INTEGER COMMENT '提前多少分钟',
  `notify_tick_unit` TINYINT COMMENT '提醒时间显示单位',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: not completed, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `operator_uid` BIGINT COMMENT 'operator uid of last operation',
  `update_time` DATETIME,
  `deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
  `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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

DROP TABLE IF EXISTS `eh_enterprise_approval_groups`;


CREATE TABLE `eh_enterprise_approval_groups` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `name` VARCHAR(64) NOT NULL COMMENT 'name of the approval group',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0. inactive, 1.active',
  `group_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT, CUSTOMIZE',
  `approval_icon` VARCHAR(1024) COMMENT 'the default icon that belongs to the group',
  `operator_uid` BIGINT,
  `operator_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_enterprise_approval_templates`;


CREATE TABLE `eh_enterprise_approval_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT DEFAULT 0 COMMENT 'the module id',
  `module_type` VARCHAR(64),
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64),
  `form_template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the template form',
  `support_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'APP:0, WEB:1, APP_WEB: 2',
  `approval_name` VARCHAR(128) NOT NULL,
  `approval_remark` VARCHAR(256) COMMENT 'the remark of the approval',
  `group_id` BIGINT NOT NULL DEFAULT '5' COMMENT 'the enterprise group id',
  `approval_attribute` VARCHAR(128) DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE',
  `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be modified from desk, 0: no, 1: yes',
  `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the approval can be deleted from desk, 0: no, 1: yes',
  `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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

DROP TABLE IF EXISTS `eh_enterprise_customer_admins`;

-- 企业管理员列表 by jiarui
CREATE TABLE `eh_enterprise_customer_admins` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `customer_id` BIGINT NOT NULL DEFAULT 0,
  `contact_name` VARCHAR(256),
  `contact_token` VARCHAR(256),
  `contact_type` VARCHAR(256),
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_enterprise_customer_aptitude_flag`;


CREATE TABLE `eh_enterprise_customer_aptitude_flag` (
  `id` BIGINT NOT NULL,
  `value` TINYINT NOT NULL DEFAULT 0 COMMENT '是否筛选，1-筛选，0-不筛选',
  `owner_id` BIGINT NOT NULL COMMENT 'communityId',
  `owner_type` VARCHAR(64) COMMENT 'owner_type',
  `namespace_id` INTEGER COMMENT 'namespace_id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='eh_enterprise_customer_aptitude_flag in dev mode';

DROP TABLE IF EXISTS `eh_enterprise_customer_attachments`;


CREATE TABLE `eh_enterprise_customer_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `customer_id` BIGINT NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_enterprise_customers`;



CREATE TABLE `eh_enterprise_customers` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_organizations',
  `community_id` BIGINT NOT NULL COMMENT 'id of eh_communities',
  `customer_number` VARCHAR(128) COMMENT 'default is id if not defined',
  `name` VARCHAR(128),
  `nick_name` VARCHAR(128),
  `category_item_id` BIGINT COMMENT '客户类型：业主、租户, refer to the id of eh_var_field_items',
  `category_item_name` VARCHAR(128) COMMENT '客户类型：业主、租户, refer to the display_name of eh_var_field_items',
  `level_item_id` BIGINT COMMENT '客户级别：普通客户、重要客户、意向客户、已成交客户、其他, refer to the id of eh_var_field_items',
  `level_item_name` VARCHAR(128) COMMENT '客户级别：普通客户、重要客户、意向客户、已成交客户、其他, refer to the display_name of eh_var_field_items',
  `source_item_id` BIGINT COMMENT '来源途径, refer to the id of eh_var_field_items',
  `source_item_name` VARCHAR(128) COMMENT '来源途径, refer to the display_name of eh_var_field_items',
  `contact_avatar_uri` VARCHAR(2048) COMMENT '联系人头像',
  `contact_name` VARCHAR(128) COMMENT '联系人名称',
  `contact_gender_item_id` BIGINT COMMENT '联系人性别, refer to the id of eh_var_field_items',
  `contact_gender_item_name` VARCHAR(128) COMMENT '联系人性别, refer to the display_name of eh_var_field_items',
  `contact_mobile` VARCHAR(64) COMMENT '联系人手机号码',
  `contact_duty` VARCHAR(64),
  `contact_phone` VARCHAR(64) COMMENT '联系人座机号码',
  `contact_offfice_phone` VARCHAR(64) COMMENT '办公电话',
  `contact_family_phone` VARCHAR(64) COMMENT '家庭电话',
  `contact_email` VARCHAR(128) COMMENT '电子邮件',
  `contact_fax` VARCHAR(128) COMMENT '传真',
  `contact_address_id` BIGINT COMMENT 'refer to id of eh_addresses',
  `contact_address` VARCHAR(1024) COMMENT '地址',
  `corp_email` VARCHAR(128) COMMENT '企业邮箱',
  `corp_website` VARCHAR(128) COMMENT '企业网址',
  `corp_reg_address` VARCHAR(1024) COMMENT '企业注册地址',
  `corp_op_address` VARCHAR(1024) COMMENT '企业运营地址',
  `corp_legal_person` VARCHAR(128) COMMENT '法人代表',
  `corp_reg_capital` DECIMAL(10,2) COMMENT '注册资金(万元)',
  `corp_nature_item_id` BIGINT COMMENT '企业性质: 国企、外企、港企、合资、民企、自然人、其他, refer to the id of eh_var_field_items',
  `corp_nature_item_name` VARCHAR(128) COMMENT '企业性质: 国企、外企、港企、合资、民企、自然人、其他, refer to the display_name of eh_var_field_items',
  `corp_scale` DECIMAL(10,2) COMMENT '企业规模',
  `corp_industry_item_id` BIGINT COMMENT '行业类型: 科技类、服务类, refer to the id of eh_var_field_items',
  `corp_industry_item_name` VARCHAR(128) COMMENT '行业类型: 科技类、服务类, refer to the display_name of eh_var_field_items',
  `corp_purpose_item_id` BIGINT COMMENT '企业定位, refer to the id of eh_var_field_items',
  `corp_purpose_item_name` VARCHAR(128) COMMENT '企业定位, refer to the display_name of eh_var_field_items',
  `corp_annual_turnover` DECIMAL(10,2) COMMENT '年营业额（万元）',
  `corp_business_scope` TEXT COMMENT '营业范围',
  `corp_business_license` VARCHAR(128) COMMENT '营业执照号',
  `corp_site_area` DECIMAL(10,2) COMMENT '场地面积',
  `corp_entry_date` DATETIME COMMENT '入住园区日期',
  `corp_product_category_item_id` BIGINT COMMENT '产品类型, refer to the id of eh_var_field_items',
  `corp_product_category_item_name` VARCHAR(128) COMMENT '产品类型, refer to the display_name of eh_var_field_items',
  `corp_product_desc` TEXT COMMENT '主要技术及产品',
  `corp_qualification_item_id` BIGINT COMMENT '企业资质认证: 高新技术企业、软件企业..., refer to the id of eh_var_field_items',
  `corp_qualification_item_name` VARCHAR(128) COMMENT '企业资质认证: 高新技术企业、软件企业..., refer to the display_name of eh_var_field_items',
  `corp_logo_uri` VARCHAR(2048) COMMENT '企业LOGO',
  `corp_description` TEXT COMMENT '企业简介',
  `corp_employee_amount` INTEGER COMMENT '员工总数',
  `corp_employee_amount_male` INTEGER COMMENT '男员工总数',
  `corp_employee_amount_female` INTEGER COMMENT '女员工总数',
  `corp_employee_amount_rd` INTEGER COMMENT '研发员工总数',
  `corp_employee_returnee_rate` DOUBLE COMMENT '海归人数占比(%)',
  `corp_employee_average_age` DOUBLE COMMENT '员工平均年龄',
  `corp_manager_average_age` DOUBLE COMMENT '高管平均年龄',
  `manager_name` VARCHAR(128) COMMENT '总经理名称',
  `manager_phone` VARCHAR(64) COMMENT '总经理电话',
  `manager_email` VARCHAR(128) COMMENT '总经理邮箱',
  `remark` TEXT COMMENT '备注',
  `namespace_customer_type` VARCHAR(128),
  `namespace_customer_token` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 2,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `tracking_uid` BIGINT COMMENT 'tracking uid',
  `tracking_name` VARCHAR(32) COMMENT '跟进人姓名',
  `property_area` DOUBLE COMMENT '资产面积',
  `property_unit_price` DOUBLE COMMENT '资产单价',
  `property_type` BIGINT COMMENT '资产类型',
  `longitude` DOUBLE COMMENT '经度',
  `latitude` DOUBLE COMMENT '纬度',
  `geohash` VARCHAR(32),
  `last_tracking_time` DATETIME COMMENT '最后一次跟进时间',
  `contact_position` VARCHAR(64),
  `version` VARCHAR(32) COMMENT '版本号',
  `founder_introduce` TEXT COMMENT '创始人介绍',
  `founding_time` DATETIME COMMENT '企业成立时间',
  `registration_type_id` BIGINT COMMENT '企业登记注册类型',
  `technical_field_id` BIGINT COMMENT '企业所属技术领域',
  `taxpayer_type_id` BIGINT COMMENT '企业纳税人类型',
  `relation_willing_id` BIGINT COMMENT '是否愿意与创业导师建立辅导关系',
  `high_and_new_tech_id` BIGINT COMMENT '是否高新技术企业',
  `entrepreneurial_characteristics_id` BIGINT COMMENT '企业主要负责人创业特征',
  `serial_entrepreneur_id` BIGINT COMMENT '企业主要负责人是否为连续创业者',
  `risk_investment_amount` DECIMAL(10,2) COMMENT '获天使或风险投资总金额（万元）',
  `hotline` VARCHAR(256),
  `post_uri` VARCHAR(128),
  `unified_social_credit_code` VARCHAR(256),
  `admin_flag` TINYINT NOT NULL DEFAULT 0,
  `source_id` BIGINT,
  `source_type` VARCHAR(64),
  `buy_or_lease_item_id` BIGINT,
  `biz_address` VARCHAR(1024),
  `biz_life` VARCHAR(32),
  `customer_intention_level` VARCHAR(32),
  `enter_dev_goal` TEXT,
  `controller_name` VARCHAR(32),
  `controller_sun_birth` VARCHAR(32),
  `controller_lunar_birth` VARCHAR(32),
  `financing_demand_item_id` BIGINT,
  `string_tag1` VARCHAR(32),
  `string_tag2` VARCHAR(32),
  `string_tag3` VARCHAR(32),
  `string_tag4` VARCHAR(32),
  `string_tag5` VARCHAR(32),
  `string_tag6` VARCHAR(32),
  `string_tag7` VARCHAR(32),
  `string_tag8` VARCHAR(32),
  `string_tag9` VARCHAR(32),
  `string_tag10` VARCHAR(32),
  `string_tag11` VARCHAR(32),
  `string_tag12` VARCHAR(32),
  `string_tag13` VARCHAR(32),
  `string_tag14` VARCHAR(32),
  `string_tag15` VARCHAR(32),
  `string_tag16` VARCHAR(32),
  `drop_box1_item_id` BIGINT,
  `drop_box2_item_id` BIGINT,
  `drop_box3_item_id` BIGINT,
  `drop_box4_item_id` BIGINT,
  `drop_box5_item_id` BIGINT,
  `drop_box6_item_id` BIGINT,
  `drop_box7_item_id` BIGINT,
  `drop_box8_item_id` BIGINT,
  `drop_box9_item_id` BIGINT,
  `aptitude_flag_item_id` BIGINT DEFAULT 0 COMMENT '0-无资质，1-有资质',
  `customer_source` TINYINT COMMENT '跟进信息类型，0-招商客户，1-成交租客',
  `transaction_ratio` VARCHAR(64),
  `expected_sign_date` DATETIME,
  `entry_status_item_id` BIGINT COMMENT '该用户是否入驻，1-未入驻，2-入驻',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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

DROP TABLE IF EXISTS `eh_enterprise_notice_attachments`;


CREATE TABLE `eh_enterprise_notice_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `notice_id` BIGINT NOT NULL COMMENT 'key of the table eh_enterprise_notices',
  `content_name` VARCHAR(256) NOT NULL COMMENT 'the name of the content',
  `content_suffix` VARCHAR(64) COMMENT 'the suffix of the file',
  `size` INTEGER NOT NULL DEFAULT 0 COMMENT 'the size of the content',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_notice_attachment_notice_id` (`notice_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_enterprise_notice_receivers`;


CREATE TABLE `eh_enterprise_notice_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `notice_id` BIGINT NOT NULL COMMENT 'key of table the eh_enterprise_notices',
  `receiver_type` VARCHAR(64) NOT NULL COMMENT 'DEPARTMENT OR MEMBER',
  `receiver_id` BIGINT NOT NULL,
  `name` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_notice_receivers_notice_id` (`notice_id`),
  KEY `i_notice_receivers_receiver_id` (`receiver_type`,`receiver_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_enterprise_notices`;


CREATE TABLE `eh_enterprise_notices` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL,
  `title` VARCHAR(256) NOT NULL COMMENT '企业公告标题',
  `summary` VARCHAR(512) COMMENT '摘要',
  `content_type` VARCHAR(32),
  `content` TEXT COMMENT '公告正文',
  `publisher` VARCHAR(256) COMMENT '公告发布者',
  `secret_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '状态 : 0-(PUBLIC)公开, 1-(PRIVATE)保密',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态 : 0-(DELETED)已删除, 1-(DRAFT)草稿, 2-(ACTIVE)已发送, 3-(INACTIVE)已撤销',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `operator_name` VARCHAR(128) COMMENT 'the name of the operator',
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `stick_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是',
  `stick_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_notices_namespace_id` (`namespace_id`),
  KEY `i_notices_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `address_id` BIGINT NOT NULL DEFAULT 0,
  `flowcase_id` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT,
  `transform_flag` TINYINT DEFAULT 0 COMMENT '是否转化为意向客户：0-否，1-是',
  `customer_name` VARCHAR(255) COMMENT '承租方',
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
  `name` VARCHAR(255) DEFAULT '',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_logs`;


CREATE TABLE `eh_equipment_inspection_equipment_logs` (
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

DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_parameters`;


CREATE TABLE `eh_equipment_inspection_equipment_parameters` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `equipment_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_equipment',
  `parameter_name` VARCHAR(128),
  `parameter_unit` VARCHAR(128),

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_equipment_inspection_equipment_plan_map`;


CREATE TABLE `eh_equipment_inspection_equipment_plan_map` (
  `id` BIGINT NOT NULL,
  `equipment_id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `plan_id` BIGINT NOT NULL DEFAULT 0,
  `standard_id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `default_order` BIGINT NOT NULL DEFAULT 0 COMMENT 'show order of equipment_maps',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `coordinate` VARCHAR(1024),
  `equipment_model` VARCHAR(1024),
  `category_id` BIGINT DEFAULT 0 COMMENT 'reference to the id of eh_categories',
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
  `brand_name` VARCHAR(1024) COMMENT 'brand_name',
  `construction_party` VARCHAR(1024) COMMENT 'construction party',
  `discard_time` DATETIME COMMENT 'discard time ',
  `manager_contact` VARCHAR(1024),
  `detail` VARCHAR(1024),
  `factory_time` DATETIME,
  `provenance` VARCHAR(1024),
  `price` DECIMAL(10,0),
  `buy_time` DATETIME,
  `depreciation_years` BIGINT(10) COMMENT '折旧年限',
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
  PRIMARY KEY (`id`),
  KEY `task_log_uniqueIndex` (`task_log_id`)
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
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_equipment_inspection_plan_group_map`;


CREATE TABLE `eh_equipment_inspection_plan_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `group_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: executive group, 2: review group',
  `plan_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_plans',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_equipment_inspection_plans`;


CREATE TABLE `eh_equipment_inspection_plans` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'organization_id',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'organization',
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'zone resource_type ',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'zone  resource_id',
  `plan_number` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'the plans number ',
  `plan_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of plan 0: 巡检  1: 保养',
  `name` VARCHAR(1024) COMMENT 'the name of plan_number',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'status of plans  0:waitting for starting 1: waitting for approving  2: QUALIFIED 3:UN_QUALIFIED',
  `reviewer_uid` BIGINT NOT NULL DEFAULT 0,
  `review_time` DATETIME,
  `repeat_setting_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refers to eh_repeatsetting ',
  `remarks` TEXT,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `deleter_uid` BIGINT,
  `delete_time` DATETIME,
  `last_create_taskTime` DATETIME COMMENT 'the last time when gen task',
  `inspection_category_id` BIGINT,
  `namespace_id` INTEGER,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_equipment_inspection_review_date`;


CREATE TABLE `eh_equipment_inspection_review_date` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasksReviewExpireDays...',
  `scope_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all; 1: namespace; 2: community',
  `scope_id` BIGINT NOT NULL,
  `review_expired_days` INTEGER NOT NULL DEFAULT 0 COMMENT 'review_expired_days',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: invalid, 1: valid',
  `refer_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, etc',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to the id of who own the standard',
  `refer_id` BIGINT,
  `repeat_type` TINYINT NOT NULL COMMENT ' 0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year',
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
  `equipment_id` BIGINT NOT NULL DEFAULT 0,
  `flow_case_id` BIGINT,
  `standard_id` BIGINT NOT NULL DEFAULT 0,
  `maintance_type` VARCHAR(255) DEFAULT '',
  `maintance_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `pm_task_id` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `eq_log_pm_task_id` (`pm_task_id`),
  KEY `eq_log_task_id` (`task_id`)
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
  `plan_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `standard_id` (`standard_id`),
  KEY `status` (`status`),
  KEY `target_id` (`target_id`),
  KEY `inspection_category_id` (`inspection_category_id`),
  KEY `executive_expire_time` (`executive_expire_time`),
  KEY `process_expire_time` (`process_expire_time`),
  KEY `operator_id` (`operator_id`),
  KEY `equipment_id_uniqueIndex` (`equipment_id`),
  KEY `eq_task_plan_id` (`plan_id`)
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
  `refer_id` BIGINT,
  `target_type` VARCHAR(32) NOT NULL DEFAULT '',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_equipment_model_community_map`;
CREATE TABLE `eh_equipment_model_community_map` (
  `id` BIGINT NOT NULL,
  `model_id` BIGINT NOT NULL DEFAULT 0,
  `model_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0:standard 1:template',
  `target_type` VARCHAR(255),
  `target_id` BIGINT COMMENT 'community id ',
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
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
  `description` VARCHAR(512) COMMENT '快递公司描述信息，比如华润ems和国贸ems需要区别一下，描述给后来的人看懂',
  `logistics_url` VARCHAR(2048) COMMENT '快递公司物流服务器地址',
  `order_url` VARCHAR(2048) COMMENT '快递公司订单服务器地址',
  `app_key` VARCHAR(512),
  `app_secret` VARCHAR(512),
  `authorization` VARCHAR(512) COMMENT '授权码',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递公司对应业务表，add by dengs, 20170718
DROP TABLE IF EXISTS `eh_express_company_businesses`;


CREATE TABLE `eh_express_company_businesses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '目前是EhNamespaces',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT '域空间',
  `express_company_id` BIGINT COMMENT 'id of the express company id,是parent_id = 0 的快递公司的id',
  `send_type` TINYINT COMMENT '业务类型id',
  `send_type_name` VARCHAR(128) COMMENT '业务类型名称(/（华润）标准快递/（国贸）EMS标准快递/（国贸）邮政快递包裹/（国贸）同城信筒快件)',
  `package_types` TEXT COMMENT '封装类型，参考 ExpressPackageType.class,json数组',
  `insured_documents` VARCHAR(1024) COMMENT '保价文案，目前只有国贸ems和国贸邮政的邮政快递包裹有保价文案，所以跟着业务走',
  `order_status_collections` TEXT COMMENT '订单状态集合,json数组 参考 ExpressOrderStatus.class',
  `pay_type` TINYINT COMMENT '支付方式， 参考,ExpressPayType.class,寄付现结=1,线下支付=2',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递服务热线表，add by dengs, 20170718
DROP TABLE IF EXISTS `eh_express_hotlines`;


CREATE TABLE `eh_express_hotlines` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `service_name` VARCHAR(512) COMMENT '服务热线的服务名称',
  `hotline` VARCHAR(128) COMMENT '热线电话',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `send_type_name` VARCHAR(128) COMMENT '业务类型名称(/（华润）标准快递/（国贸）EMS标准快递/（国贸）邮政快递包裹/（国贸）同城信筒快件)',
  `quantity_and_weight` VARCHAR(128) COMMENT '数量和重量',
  `send_mode` TINYINT COMMENT '1. self send',
  `pay_type` TINYINT COMMENT '1. cash',
  `pay_summary` DECIMAL(10,2) COMMENT 'pay money',
  `internal` VARCHAR(256) COMMENT 'internal things',
  `invoice_head` VARCHAR(512) COMMENT '税票的发票抬头',
  `invoice_flag` TINYINT COMMENT '需要发票选项,0:不需要 1：需要手撕发票 2：需要税票 ExpressInvoiceFlagType.class',
  `package_type` TINYINT COMMENT '封装类型，参考 ExpressPackageType.class',
  `insured_price` DECIMAL(10,2) COMMENT 'insured price',
  `status` TINYINT NOT NULL COMMENT '1. waiting for pay, 2. paid, 3. printed, 4. cancelled',
  `status_desc` TEXT COMMENT '状态描述信息，国贸ems使用',
  `paid_flag` TINYINT COMMENT 'whether the user has pushed the pay button, 0. false, 1 true',
  `print_time` DATETIME,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  `flow_case_id` BIGINT COMMENT '工作流id 国贸快递专用',
  `express_type` TINYINT COMMENT '0:物品 1:文件 2:其他  国贸快递专用',
  `express_way` TINYINT COMMENT '0:陆运 1:空运  国贸快递专用',
  `express_target` TINYINT COMMENT '0:同城 1:外埠  国贸快递专用',
  `express_remark` TEXT COMMENT '备注 国贸快递专用',
  `pay_dto` TEXT COMMENT '支付2.0下单详情',
  `general_order_id` VARCHAR(64) COMMENT '统一订单系统订单编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递设置表，add by dengs, 20170718
DROP TABLE IF EXISTS `eh_express_param_settings`;


CREATE TABLE `eh_express_param_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community or namespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community or namespace id',
  `express_user_setting_show_flag` TINYINT DEFAULT 0 COMMENT '快递员设置 是否在后台管理 显示标志',
  `business_note_setting_show_flag` TINYINT DEFAULT 1 COMMENT '业务说明 是否在后台管理 显示标志',
  `hotline_setting_show_flag` TINYINT DEFAULT 1 COMMENT '客服热线 是否在后台管理 显示标志',
  `hotline_flag` TINYINT DEFAULT 0 COMMENT '热线是否在app显示标志，可在后台修改',
  `business_note` TEXT COMMENT '业务说明',
  `business_note_flag` TINYINT DEFAULT 0 COMMENT '业务说明是否在app显示标志，可在后台修改',
  `send_mode` TINYINT DEFAULT 1 COMMENT '1,服务点自寄 2，快递员上门收件',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_express_payee_accounts`;


CREATE TABLE `eh_express_payee_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT NOT NULL COMMENT '园区id或者其他id',
  `payee_id` BIGINT NOT NULL COMMENT '支付帐号id',
  `payee_user_type` VARCHAR(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='快递收款账户表';


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

DROP TABLE IF EXISTS `eh_face_recognition_photos`;


CREATE TABLE `eh_face_recognition_photos` (
  `id` BIGINT NOT NULL,
  `user_id` BIGINT COMMENT '用户id(正式用户)',
  `auth_id` BIGINT COMMENT '授权id(访客)',
  `user_type` TINYINT NOT NULL DEFAULT 0 COMMENT '照片关联的用户类型,0正式用户,1访客',
  `img_uri` VARCHAR(2048) COMMENT '照片uri',
  `img_url` VARCHAR(2048) NOT NULL COMMENT '照片url',
  `sync_time` DATETIME COMMENT '上次同步时间时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态1正常2已删除',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='人脸识别照片表';

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
  `target_param` TEXT,
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

DROP TABLE IF EXISTS `eh_file_icons`;


CREATE TABLE `eh_file_icons` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `file_type` VARCHAR(64) NOT NULL COMMENT 'the type of the file',
  `icon_name` VARCHAR(128) COMMENT 'the name of the icon',
  `icon_uri` VARCHAR(2048) NOT NULL COMMENT 'the uri of the type',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_file_management_catalog_scopes`;


CREATE TABLE `eh_file_management_catalog_scopes` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `catalog_id` BIGINT NOT NULL COMMENT 'the id of the file catalog',
  `source_id` BIGINT NOT NULL COMMENT 'the id of the source',
  `source_type` VARCHAR(64) COMMENT'the type of the source',
  `source_description` VARCHAR(128) COMMENT 'the description of the scope class',
  `download_permission` TINYINT NOT NULL DEFAULT 0 COMMENT '0-refuse, 1-allow',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_file_management_catalogs`;


CREATE TABLE `eh_file_management_catalogs` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64),
  `name` VARCHAR(64) COMMENT 'the name of the catalog',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid 1-valid',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `operator_name` VARCHAR(256),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_file_management_contents`;


CREATE TABLE `eh_file_management_contents` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `catalog_id` BIGINT COMMENT 'the id of the catalog',
  `content_name` VARCHAR(256) NOT NULL COMMENT 'the name of the content',
  `size` INTEGER NOT NULL DEFAULT 0 COMMENT 'the size of the content',
  `parent_id` BIGINT COMMENT 'the parent id of the folder',
  `content_type` VARCHAR(32) COMMENT 'file, folder',
  `content_suffix` VARCHAR(64) COMMENT 'the suffix of the file',
  `content_uri` VARCHAR(2048) COMMENT 'the uri of the content',
  `path` VARCHAR(128) NOT NULL COMMENT 'the path of the content',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `operator_name` VARCHAR(256),
  PRIMARY KEY (`id`),
  KEY `file_management_contents_path` (`path`),
  KEY `i_eh_content_catalog_id` (`catalog_id`),
  KEY `i_eh_content_parent_id` (`parent_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_fixed_asset_categories`;


CREATE TABLE `eh_fixed_asset_categories` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations ',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `parent_id` INTEGER NOT NULL DEFAULT 0 COMMENT '父级分类id',
  `path` VARCHAR(128) NOT NULL COMMENT '分类层级路径，如 /123/1234',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0 : IN_ACTIVE 1: ACTIVE',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY (`id`),
  KEY `i_eh_namespace_owner_id` (`namespace_id`,`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='固定资产分类表';

DROP TABLE IF EXISTS `eh_fixed_asset_default_categories`;


CREATE TABLE `eh_fixed_asset_default_categories` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `name` VARCHAR(64) NOT NULL COMMENT '分类名称',
  `parent_id` INTEGER NOT NULL DEFAULT 0 COMMENT '父级分类id',
  `path` VARCHAR(128) NOT NULL COMMENT '分类层级路径，如 /123/1234',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='固定资产默认分类表';

DROP TABLE IF EXISTS `eh_fixed_asset_operation_logs`;


CREATE TABLE `eh_fixed_asset_operation_logs` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `fixed_asset_id` BIGINT NOT NULL COMMENT '资产ID',
  `operation_info` TEXT NOT NULL COMMENT '变更记录JSON格式记录',
  `operation_type` VARCHAR(16) NOT NULL COMMENT '新增、编辑或者删除',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_name` VARCHAR(45) NOT NULL COMMENT '操作人姓名 ',
  PRIMARY KEY (`id`),
  KEY `i_eh_namespace_asset_id` (`namespace_id`,`fixed_asset_id`),
  KEY `i_eh_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='固定资产信息变更记录表';

DROP TABLE IF EXISTS `eh_fixed_assets`;


CREATE TABLE `eh_fixed_assets` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `item_no` VARCHAR(20) NOT NULL COMMENT '资产编号',
  `name` VARCHAR(64) NOT NULL COMMENT '资产名称',
  `fixed_asset_category_id` INTEGER NOT NULL COMMENT '资产分类  id of the table eh_fixed_asset_categories',
  `specification` VARCHAR(128) COMMENT '规格',
  `price` DECIMAL(14,2) COMMENT '单价',
  `buy_date` DATE COMMENT '购买日期 格式:yyyy-MM-dd',
  `vendor` VARCHAR(128) COMMENT '所属供应商',
  `add_from` TINYINT NOT NULL DEFAULT 0 COMMENT '来源 :0-其它,1-购入,2-自建,3-租赁,4-捐赠',
  `image_uri` VARCHAR(1024) COMMENT '图片uri',
  `barcode_uri` VARCHAR(1024) COMMENT '条形码uri',
  `other_info` VARCHAR(512) COMMENT '其它',
  `remark` VARCHAR(512) COMMENT '备注',
  `status` TINYINT NOT NULL COMMENT '状态:1-闲置,2-使用中,3-维修中,4-已出售,5-已报废,6-遗失',
  `location` VARCHAR(256) COMMENT '存放地点',
  `occupied_date` DATE COMMENT '领用时间',
  `occupied_department_id` BIGINT COMMENT '领用部门ID',
  `occupied_member_detail_id` BIGINT COMMENT '领用人 id of the table eh_organization_member_details',
  `occupied_member_name` VARCHAR(64) COMMENT '领用人姓名',
  `operator_name` VARCHAR(64) NOT NULL COMMENT '操作人姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT '删除操作人userId',
  `delete_time` DATETIME COMMENT '记录删除时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_namespace_owner_id` (`namespace_id`,`owner_type`,`owner_id`),
  KEY `i_eh_fixed_asset_category_id` (`fixed_asset_category_id`),
  KEY `i_eh_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='固定资产表';

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
  `render_text` TEXT COMMENT 'the content for this message that have variables',
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
  `script_type` VARCHAR(64),
  `script_main_id` BIGINT NOT NULL DEFAULT 0,
  `script_version` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`),
  KEY `i_eh_belong_entity_belong_to_action_type_step_type` (`belong_entity`,`belong_to`,`action_type`,`action_step_type`)
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


DROP TABLE IF EXISTS `eh_flow_branches`;

CREATE TABLE `eh_flow_branches` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `original_node_id` BIGINT NOT NULL COMMENT '分发开始节点id',
  `original_node_level` INTEGER NOT NULL COMMENT '分发开始节点level',
  `convergence_node_id` BIGINT NOT NULL COMMENT '最终收敛节点id',
  `convergence_node_level` INTEGER NOT NULL COMMENT '最终收敛节点level',
  `process_mode` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '单一路径：single, 并发执行：concurrent',
  `branch_decider` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'processor, condition',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`)
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
  `param` VARCHAR(64) COMMENT 'the params from other module',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT 'default order',
  `evaluate_step` VARCHAR(64) COMMENT 'default order',
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`),
  KEY `i_eh_flow_node_id` (`flow_node_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `last_step_time` DATETIME(3) COMMENT 'state change time',
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
  `current_lane_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parentId, ref eh_flow_cases',
  `start_node_id` BIGINT NOT NULL DEFAULT 0 COMMENT '开始节点id',
  `end_node_id` BIGINT NOT NULL DEFAULT 0 COMMENT '结束节点id',
  `start_link_id` BIGINT NOT NULL DEFAULT 0 COMMENT '开始linkId',
  `end_link_id` BIGINT NOT NULL DEFAULT 0 COMMENT '结束linkId',
  `evaluate_status` TINYINT NOT NULL DEFAULT 0 COMMENT '评价状态，一般指结束后还可以评价的情况',
  `service_type` VARCHAR(64) COMMENT 'service type',
  `string_tag6` VARCHAR(128),
  `string_tag7` VARCHAR(128),
  `string_tag8` VARCHAR(128),
  `string_tag9` VARCHAR(128),
  `string_tag10` VARCHAR(128),
  `string_tag11` VARCHAR(128),
  `string_tag12` VARCHAR(128),
  `string_tag13` VARCHAR(128),
  `integral_tag6` BIGINT,
  `integral_tag7` BIGINT,
  `integral_tag8` BIGINT,
  `integral_tag9` BIGINT,
  `integral_tag10` BIGINT,
  `integral_tag11` BIGINT,
  `integral_tag12` BIGINT,
  `integral_tag13` BIGINT,
  `delete_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '删除状态',
  `route_uri` VARCHAR(128) COMMENT 'route uri',
  `path` VARCHAR(1024) COMMENT 'flow case path',
  `sub_flow_parent_id` BIGINT NOT NULL DEFAULT 0,
  `sub_flow_path` VARCHAR(128) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_condition_expressions`;

CREATE TABLE `eh_flow_condition_expressions` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `flow_condition_id` BIGINT NOT NULL COMMENT 'ref eh_flow_conditions',
  `logic_operator` VARCHAR(24) NOT NULL COMMENT '&&, ||, !',
  `relational_operator` VARCHAR(24) NOT NULL COMMENT '>, <, ==, !=',
  `variable_type1` VARCHAR(32) NOT NULL DEFAULT 1 COMMENT 'const, variable',
  `variable1` VARCHAR(64) NOT NULL DEFAULT '',
  `variable_type2` VARCHAR(32) NOT NULL DEFAULT 1 COMMENT 'const, variable',
  `variable2` VARCHAR(64) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  `variable_extra1` VARCHAR(256) COMMENT 'variable 1 extra',
  `variable_extra2` VARCHAR(256) COMMENT 'variable 2 extra',
  `entity_type1` VARCHAR(32),
  `entity_id1` BIGINT DEFAULT 0,
  `entity_type2` VARCHAR(32),
  `entity_id2` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_conditions`;

CREATE TABLE `eh_flow_conditions` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `condition_level` INTEGER NOT NULL DEFAULT 0,
  `flow_node_id` BIGINT NOT NULL,
  `flow_node_level` INTEGER,
  `flow_link_id` BIGINT,
  `flow_link_level` INTEGER,
  `next_node_id` BIGINT,
  `next_node_level` INTEGER,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_evaluate_items`;


CREATE TABLE `eh_flow_evaluate_items` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `name` VARCHAR(128) NOT NULL,
  `input_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, input evaluate content flag',
  `flow_case_id` BIGINT,
  `string_tag6` VARCHAR(128),
  `string_tag7` VARCHAR(128),
  `string_tag8` VARCHAR(128),
  `string_tag9` VARCHAR(128),
  `string_tag10` VARCHAR(128),
  `integral_tag6` BIGINT NOT NULL DEFAULT 0,
  `integral_tag7` BIGINT NOT NULL DEFAULT 0,
  `integral_tag8` BIGINT NOT NULL DEFAULT 0,
  `integral_tag9` BIGINT NOT NULL DEFAULT 0,
  `integral_tag10` BIGINT NOT NULL DEFAULT 0,
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
  `extra` TEXT COMMENT 'extra data, json format',
  `enter_log_complete_flag` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `i_eh_namespace_id` (`namespace_id`),
  KEY `i_eh_flow_main_id` (`flow_main_id`),
  KEY `i_eh_flow_version` (`flow_version`),
  KEY `i_eh_flow_case_id` (`flow_case_id`),
  KEY `i_eh_flow_user_id` (`flow_user_id`),
  KEY `i_eh_step_count` (`step_count`),
  KEY `i_eh_log_type` (`log_type`)
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



DROP TABLE IF EXISTS `eh_flow_lanes`;

CREATE TABLE `eh_flow_lanes` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `display_name` VARCHAR(128) COMMENT 'lane name',
  `display_name_absort` VARCHAR(128) COMMENT 'when flowCase absort display this',
  `flow_node_level` INTEGER COMMENT 'flow_node_level',
  `identifier_node_level` INTEGER COMMENT '标识这个用泳道里的那个节点里的申请人按钮',
  `identifier_node_id` BIGINT COMMENT '标识这个用泳道里的那个节点里的申请人按钮',
  `lane_level` INTEGER COMMENT 'lane level',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_links`;

CREATE TABLE `eh_flow_links` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `flow_main_id` BIGINT NOT NULL,
  `flow_version` INTEGER NOT NULL,
  `display_name` VARCHAR(64) COMMENT 'display name',
  `link_level` INTEGER NOT NULL,
  `from_node_id` BIGINT NOT NULL,
  `from_node_level` INTEGER NOT NULL,
  `to_node_id` BIGINT NOT NULL,
  `to_node_level` INTEGER NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`),
  KEY `i_eh_flow_to_node_id` (`to_node_id`),
  KEY `i_eh_flow_from_node_id` (`from_node_id`)
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
  `node_type` VARCHAR(32) NOT NULL DEFAULT 'normal' COMMENT 'start, end, normal, condition_front, condition_back',
  `goto_process_button_name` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'start, end, normal, condition_front, condition_back',
  `flow_lane_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes',
  `flow_lane_level` INTEGER NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_lanes',
  `need_all_processor_complete` TINYINT NOT NULL DEFAULT 0 COMMENT '节点会签开关',
  `form_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disable, 1: enable',
  `form_origin_id` BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_origin_id',
  `form_version` BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_version',
  `form_update_time` DATETIME,
  `sub_flow_goto_node_id` BIGINT COMMENT 'only sub flow node, when sub flow absort go to node id',
  `sub_flow_step_type` VARCHAR(32) COMMENT 'only sub flow node, when sub flow absort step type',
  `sub_flow_project_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings project_type',
  `sub_flow_project_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings project_id',
  `sub_flow_module_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings module_type',
  `sub_flow_module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings module_id',
  `sub_flow_owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings owner_type',
  `sub_flow_owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings owenr_id',
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_flow_predefined_params`;

CREATE TABLE `eh_flow_predefined_params` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `module_type` VARCHAR(64) NOT NULL,
  `entity_type` VARCHAR(64) NOT NULL COMMENT 'flow_node, flow_button',
  `display_name` VARCHAR(128) COMMENT 'display name',
  `name` VARCHAR(64) COMMENT 'param name',
  `text` TEXT,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_flow_script_configs`;


CREATE TABLE `eh_flow_script_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
  `flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'flow version',
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',
  `script_name` VARCHAR(128) COMMENT 'export script name, only for script type of java',
  `script_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_scripts',
  `script_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'script version',
  `field_name` VARCHAR(1024) COMMENT 'field name',
  `field_desc` TEXT COMMENT 'field description',
  `field_value` VARCHAR(1024) COMMENT 'field value',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `create_time` datetime(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='flow scripts config in dev mode';

DROP TABLE IF EXISTS `eh_flow_scripts`;


CREATE TABLE `eh_flow_scripts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `script_category` VARCHAR(64) NOT NULL COMMENT 'system_script, user_script',
  `script_type` VARCHAR(64) NOT NULL COMMENT 'javascript, groovy, java and other',
  `script_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_scripts',
  `script_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'script version',
  `name` VARCHAR(128) COMMENT 'script name',
  `description` TEXT COMMENT 'script description',
  `script` LONGTEXT COMMENT 'script content',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `create_time` datetime(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
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
  `last_commit` VARCHAR(40) COMMENT 'repository last commit id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='flow scripts in dev mode';

DROP TABLE IF EXISTS `eh_flow_service_mappings`;


CREATE TABLE `eh_flow_service_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `display_name` VARCHAR(64) NOT NULL,
  `flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the real flow id for all copy, the first flow_main_id=0',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- scripts for this module
DROP TABLE IF EXISTS `eh_flow_service_types`;

CREATE TABLE `eh_flow_service_types` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_name` VARCHAR(64),
  `module_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) COMMENT 'ownerType, e.g: EhOrganizations',
  `owner_id` BIGINT COMMENT 'ownerId, e.g: eh_organizations id',
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
  PRIMARY KEY (`id`),
  KEY `i_eh_flow_main_id_flow_ver` (`flow_main_id`,`flow_version`)
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
  `description` VARCHAR(128) COMMENT 'flow description',
  `allow_flow_case_end_evaluate` TINYINT NOT NULL DEFAULT 0 COMMENT 'allow_flow_case_end_evaluate',
  `validation_status` TINYINT NOT NULL DEFAULT 2 COMMENT 'flow validation status',
  `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form id',
  `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form version',
  `form_update_time` DATETIME COMMENT 'form update time',
  `config_status` TINYINT NOT NULL DEFAULT 0 COMMENT 'config status, 2: config, 3: snapshot',
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

DROP TABLE IF EXISTS `eh_forum_categories`;

CREATE TABLE `eh_forum_categories` (
  `id` BIGINT NOT NULL,
  `uuid` VARCHAR(128) NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `forum_id` BIGINT NOT NULL COMMENT 'forum id',
  `entry_id` BIGINT NOT NULL COMMENT 'entry id',
  `name` VARCHAR(255),
  `activity_entry_id` BIGINT DEFAULT 0 COMMENT 'activity entry id',
  `create_time` DATETIME NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
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
  `clone_flag` TINYINT COMMENT 'clone_flag post 0-real post, 1-clone post',
  `real_post_id` BIGINT COMMENT 'if this is clone post, then it should have a real post id',
  `stick_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否置顶，0-否，1-是',
  `forum_entry_id` BIGINT DEFAULT 0 COMMENT 'forum_category  entry_id',
  `interact_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'support interact, 0-no, 1-yes',
  `stick_time` DATETIME,
  `module_type` TINYINT,
  `module_category_id` BIGINT,
  `min_quantity` INTEGER COMMENT '最低限制人数',
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

DROP TABLE IF EXISTS `eh_forum_service_types`;
CREATE TABLE `eh_forum_service_types` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_type` TINYINT COMMENT 'module type, 1-forum,2-activity......',
  `category_id` BIGINT,
  `service_type` VARCHAR(255) NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `sort_num` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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

DROP TABLE IF EXISTS `eh_general_approval_scope_map`;


CREATE TABLE `eh_general_approval_scope_map` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `approval_id` BIGINT NOT NULL COMMENT 'id of the approval',
  `source_type` VARCHAR(64) NOT NULL COMMENT 'ORGANIZATION, MEMBERDETAIL',
  `source_id` BIGINT NOT NULL COMMENT 'id of the source',
  `source_description` VARCHAR(128) COMMENT 'the description of the source',
  `create_time` DATETIME COMMENT 'create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `approval_template_id` BIGINT COMMENT 'the id in eh_general_approval_templates',
  `approval_template_version` BIGINT COMMENT 'the version in eh_general_approval_templates',
  `approval_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'CUSTOMIZE, DEFAULT',
  `modify_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval',
  `approval_remark` VARCHAR(256) COMMENT 'the remark of the approval',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `integral_tag1` BIGINT NOT NULL DEFAULT 0,
  `integral_tag2` BIGINT NOT NULL DEFAULT 0,
  `integral_tag3` BIGINT NOT NULL DEFAULT 0,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the userId of the operator',
  `operator_name` VARCHAR(128) COMMENT 'the real name of the operator',
  `creater_name` VARCHAR(64) COMMENT '新增人',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_general_form_filter_user_map`;


CREATE TABLE `eh_general_form_filter_user_map` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT NOT NULL COMMENT 'owner_id',
  `owner_type` VARCHAR(64) COMMENT 'owner_type',
  `namespace_id` INTEGER NOT NULL COMMENT 'namespace_id',
  `module_id` BIGINT NOT NULL COMMENT 'module_id',
  `module_type` VARCHAR(64) COMMENT 'module_type',
  `form_origin_id` BIGINT NOT NULL COMMENT '关联的表id',
  `form_version` BIGINT NOT NULL COMMENT '关联的表version',
  `field_name` VARCHAR(64) NOT NULL COMMENT '被选中的字段名',
  `user_uuid` VARCHAR(128) NOT NULL COMMENT '当前登录的用户用于获取字段',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='eh_general_form_filter_user_map in dev mode';

DROP TABLE IF EXISTS `eh_general_form_print_templates`;


CREATE TABLE `eh_general_form_print_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `name` VARCHAR(64) NOT NULL COMMENT '表单打印模板名称',
  `last_commit` VARCHAR(40) COMMENT '最近一次提交ID',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT '表单打印模板所属ID',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '表单打印所属类型',
  `status` TINYINT DEFAULT 2 COMMENT '打印模板状态,0为失效，2为生效',
  `creator_uid` BIGINT COMMENT 'record creator user id',
  `create_time` DATETIME COMMENT '创建时间',
  `delete_uid` BIGINT COMMENT 'record deleter user id',
  `delete_time` DATETIME COMMENT '删除时间',
  `update_uid` BIGINT COMMENT 'record update user id',
  `update_time` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='通用表单打印模板表';

DROP TABLE IF EXISTS `eh_general_form_templates`;


CREATE TABLE `eh_general_form_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT DEFAULT 0 COMMENT 'the module id',
  `module_type` VARCHAR(64),
  `form_name` VARCHAR(64) NOT NULL,
  `version` BIGINT NOT NULL DEFAULT 0 COMMENT 'the version of the form template',
  `template_type` VARCHAR(128) NOT NULL COMMENT 'the type of template text',
  `template_text` TEXT COMMENT 'json 存放表单字段',
  `modify_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be modified from desk, 0: no, 1: yes',
  `delete_flag` TINYINT DEFAULT 1 COMMENT 'whether the form can be deleted from desk, 0: no, 1: yes',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_general_form_val_requests`;


CREATE TABLE `eh_general_form_val_requests` (
  `id` BIGINT NOT NULL,
  `organization_id` BIGINT COMMENT 'organization_id',
  `owner_id` BIGINT COMMENT 'owner_id',
  `owner_type` VARCHAR(64) COMMENT 'owner_type',
  `namespace_id` INTEGER COMMENT 'namespace_id',
  `module_id` BIGINT COMMENT 'module_id',
  `module_type` VARCHAR(64) COMMENT 'module_type',
  `source_id` BIGINT COMMENT 'source_id',
  `source_type` VARCHAR(64) COMMENT 'source_type',
  `approval_status` TINYINT DEFAULT 0 COMMENT '该表单的审批状态,0-待发起，1-审批中，2-审批通过，3-审批终止',
  `status` TINYINT DEFAULT 1 COMMENT '该表单的状态，0-删除，1-生效',
  `form_origin_id` BIGINT COMMENT '该表单所属的表单模板id',
  `form_version` BIGINT COMMENT '该表单所属的表单模板version',
  `integral_tag1` BIGINT DEFAULT 0 COMMENT '业务字段（用于表示招商租赁的预约申请记录状态）',
  `integral_tag2` BIGINT COMMENT '业务字段（用于表示招商租赁的预约记录的来源广告的id）',
  `created_time` DATE COMMENT '创建时间',
  `creator_uid` BIGINT COMMENT '创建人ID',
  `operator_time` DATE COMMENT '操作时间',
  `operator_uid` BIGINT COMMENT '操作人ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='eh_general_form_val_requests in dev mode';

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `form_template_id` BIGINT COMMENT 'the id in eh_general_form_templates',
  `form_template_version` BIGINT COMMENT 'the version in eh_general_form_templates',
  `form_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'CUSTOMIZE, DEFAULT',
  `modify_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `operator_name` VARCHAR(64) COMMENT '修改人',
  `creater_name` VARCHAR(64) COMMENT '新增人',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_gogs_repos`;


CREATE TABLE `eh_gogs_repos` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the module id',
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `repo_type` VARCHAR(32) NOT NULL,
  `name` VARCHAR(128) COMMENT 'name',
  `full_name` VARCHAR(512) COMMENT 'full name',
  `description` TEXT COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `create_time` datetime(3),
  `creator_uid` BIGINT,
  `update_time` datetime(3),
  `update_uid` BIGINT,
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='gogs repository';

DROP TABLE IF EXISTS `eh_group_member_logs`;


CREATE TABLE `eh_group_member_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `group_member_id` BIGINT,
  `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive; 1: waitingForApproval; 2: waitingForAcceptance 3: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `address_id` BIGINT NOT NULL DEFAULT 0,
  `group_id` BIGINT NOT NULL DEFAULT 0,
  `member_type` VARCHAR(32) NOT NULL DEFAULT '',
  `member_id` BIGINT,
  `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'Default to ResourceUser role',
  `member_avatar` VARCHAR(128) COMMENT 'avatar image identifier in storage sub-system',
  `member_nick_name` VARCHAR(128) COMMENT 'member nick name within the group',
  `operator_uid` BIGINT COMMENT 'redundant auditing info',
  `process_code` TINYINT,
  `process_details` TEXT,
  `proof_resource_uri` VARCHAR(1024),
  `approve_time` DATETIME COMMENT 'redundant auditing info',
  `requestor_comment` TEXT,
  `operation_type` TINYINT COMMENT '1: request to join; 2: invite to join',
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
  `reject_text` VARCHAR(255),
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
  `club_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0-normal club, 1-guild club',
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
  `name` VARCHAR(128),
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
  `tourist_post_policy` TINYINT DEFAULT 2 COMMENT '0-hide, 1-see only, 2-interact',
  `club_type` TINYINT DEFAULT 0 COMMENT '0-normal club, 1-guild club',
  `phone_number` VARCHAR(18),
  `description_type` TINYINT DEFAULT 0,
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

DROP TABLE IF EXISTS `eh_guild_applies`;


CREATE TABLE `eh_guild_applies` (
  `id` BIGINT NOT NULL,
  `uuid` VARCHAR(128) NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `group_id` BIGINT NOT NULL,
  `applicant_uid` BIGINT NOT NULL,
  `group_member_id` BIGINT NOT NULL,
  `avatar` VARCHAR(255),
  `name` VARCHAR(255),
  `phone` VARCHAR(18),
  `email` VARCHAR(255),
  `organization_name` VARCHAR(255),
  `registered_capital` VARCHAR(255),
  `industry_type` VARCHAR(255),
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `category_id` BIGINT,
  `module_type` TINYINT COMMENT 'module type, 1-forum 2-activity...........',
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


-- 入孵申请表  add by yanjun 20170913
DROP TABLE IF EXISTS `eh_incubator_applies`;


CREATE TABLE `eh_incubator_applies` (
  `id` BIGINT NOT NULL,
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `parent_id` BIGINT,
  `root_id` BIGINT COMMENT 'tree root id',
  `namespace_id` INTEGER,
  `community_id` BIGINT,
  `apply_user_id` BIGINT NOT NULL,
  `team_name` VARCHAR(255),
  `project_type` VARCHAR(255),
  `project_name` VARCHAR(255),
  `business_licence_uri` VARCHAR(255),
  `plan_book_uri` VARCHAR(255),
  `charger_name` VARCHAR(255),
  `charger_phone` VARCHAR(18),
  `charger_email` VARCHAR(255),
  `charger_wechat` VARCHAR(255),
  `approve_user_id` BIGINT,
  `approve_status` TINYINT COMMENT '审批状态，0-待审批，1-拒绝，2-通过',
  `approve_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `approve_opinion` VARCHAR(1024),
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `re_apply_id` BIGINT COMMENT '重新申请的Id，现已弃用该字段',
  `apply_type` TINYINT NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `check_flag` TINYINT DEFAULT 0 COMMENT '查看状态，0-未查看、1-已查看。这是一个很傻逼的功能，用于区分都是未审核的时候，管理员有没有看过这条记录。',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_incubator_apply_attachments`;


CREATE TABLE `eh_incubator_apply_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `incubator_apply_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g incubator_apply_id',
  `type` TINYINT COMMENT '类型，1-business_licence，2-plan_book',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 入孵申请项目类型 add by yanjun 20170913
DROP TABLE IF EXISTS `eh_incubator_project_types`;


CREATE TABLE `eh_incubator_project_types` (
  `id`  BIGINT NOT NULL ,
  `uuid` VARCHAR(128) NOT NULL DEFAULT '',
  `name` VARCHAR(255) NOT NULL,
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_industry_types`;


CREATE TABLE `eh_industry_types` (
  `id` BIGINT NOT NULL,
  `uuid` VARCHAR(128) NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `name` VARCHAR(32) NOT NULL,
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_interact_settings`;

-- 是否支持评论功能
CREATE TABLE `eh_interact_settings` (
  `id` BIGINT NOT NULL,
  `uuid` VARCHAR(128) NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `module_type` TINYINT NOT NULL COMMENT 'forum, activity, announcement',
  `category_id` BIGINT,
  `interact_flag` TINYINT NOT NULL COMMENT 'support interact, 0-no, 1-yes',
  `create_time` DATETIME NOT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_investment_advertisement_assets`;


CREATE TABLE `eh_investment_advertisement_assets` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `advertisement_id` BIGINT COMMENT '关联的广告id',
  `asset_type` TINYINT COMMENT '关联的资产类型 : 1-community,2-building,3-apartment',
  `asset_id` BIGINT COMMENT '关联的资产id',
  `status` TINYINT DEFAULT 2 COMMENT '该条的记录状态：0- inactive, 1- confirming, 2- active',
  `creator_uid` BIGINT COMMENT '创建人',
  `create_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告关联资产表';

DROP TABLE IF EXISTS `eh_investment_advertisement_banners`;


CREATE TABLE `eh_investment_advertisement_banners` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `advertisement_id` BIGINT COMMENT '关联的广告id',
  `content_uri` VARCHAR(256),
  `status` TINYINT DEFAULT 2 COMMENT '该条的记录状态：0: inactive, 1: confirming, 2: active',
  `creator_uid` BIGINT COMMENT '创建人',
  `create_time` DATETIME COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告轮播图表';

DROP TABLE IF EXISTS `eh_investment_advertisements`;


CREATE TABLE `eh_investment_advertisements` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `community_id` BIGINT,
  `owner_type` VARCHAR(64) COMMENT '默认为EhOrganizations',
  `owner_id` BIGINT COMMENT '默认为organizationId',
  `title` VARCHAR(255) COMMENT '广告主题',
  `investment_type` TINYINT COMMENT '广告类型 : 1-招租广告，2-招商广告',
  `investment_status` TINYINT COMMENT '招商状态 : 1-招商中，2-已下线，3-已出租',
  `available_area_min` DECIMAL(10,2) COMMENT '招商面积起点',
  `available_area_max` DECIMAL(10,2) COMMENT '招商面积终点',
  `asset_price_min` DECIMAL(10,2) COMMENT '招商价格起点',
  `asset_price_max` DECIMAL(10,2) COMMENT '招商价格终点',
  `price_unit` TINYINT COMMENT '价格单位：1-元/平*月',
  `apartment_floor_min` INTEGER COMMENT '招商楼层起点',
  `apartment_floor_max` INTEGER COMMENT '招商楼层终点',
  `orientation` VARCHAR(64) COMMENT '朝向',
  `address` VARCHAR(255) COMMENT '详细地址',
  `longitude` DOUBLE COMMENT '经度',
  `latitude` DOUBLE COMMENT '纬度',
  `geohash` VARCHAR(32) COMMENT 'geohash值，用于GPS定位',
  `contact_name` VARCHAR(128) COMMENT '联系人',
  `contact_phone` VARCHAR(128) COMMENT '联系电话',
  `description` TEXT COMMENT '广告内容描述',
  `poster_uri` VARCHAR(256) COMMENT '封面图uri',
  `asset_dispaly_flag` TINYINT COMMENT '是否显示楼宇房源：0-否，1-是',
  `custom_form_flag` TINYINT COMMENT '是否添加自定义表单：0-否，1-是',
  `general_form_id` BIGINT COMMENT '关联的自定义表单id',
  `default_order` BIGINT COMMENT '排序字段（初始值等于主键id）',
  `status` TINYINT DEFAULT 2 COMMENT '该条的记录状态：0: inactive, 1: confirming, 2: active',
  `creator_uid` BIGINT COMMENT '创建人',
  `create_time` DATETIME COMMENT '创建时间',
  `operator_uid` BIGINT COMMENT '更新人',
  `operate_time` DATETIME COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告表';

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
  `label` VARCHAR(64),
  `item_location` VARCHAR(2048),
  `item_group` VARCHAR(128) NOT NULL DEFAULT '',
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `description` VARCHAR(1024),
  `scope_code` TINYINT NOT NULL DEFAULT 0,
  `scope_id` BIGINT,
  `preview_portal_version_id` BIGINT COMMENT '预览版本的id，正式版本数据不要配置该数据',
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
  `target_type` VARCHAR(32) NOT NULL COMMENT 'e.g: NONE, POST_DETAIL, ACTIVITY_DETAIL, APP, URL, ROUTE',
  `target_data` VARCHAR(1024) COMMENT 'It is different by different target_type',
  `content_uri_origin` VARCHAR(1024) COMMENT 'Content uri for origin file.',
  `resource_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'Resource name',
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
  `categry_name` VARCHAR(64),
  `preview_portal_version_id` BIGINT COMMENT '预览版本的id，正式版本数据不要配置该数据',
  `access_control_type` TINYINT DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth',
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
  `bg_image_uri` VARCHAR(255),
  `preview_portal_version_id` BIGINT COMMENT '预览版本的id，正式版本数据不要配置该数据',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_lease_buildings`;


CREATE TABLE `eh_lease_buildings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refering to eh_communities',
  `building_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refering to eh_buildings',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'building name',
  `alias_name` VARCHAR(128),
  `manager_name` VARCHAR(128),
  `manager_contact` VARCHAR(128) COMMENT 'the phone number',
  `manager_uid` BIGINT,
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `address` VARCHAR(1024),
  `area_size` DOUBLE,
  `description` TEXT,
  `poster_uri` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: confirming, 2: active',
  `traffic_description` TEXT,
  `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form',
  `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field',
  `default_order` BIGINT NOT NULL,
  `creator_uid` BIGINT COMMENT 'uid of the user who has suggested address, NULL if it is system created',
  `create_time` DATETIME,
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'uid of the user who process the address',
  `operate_time` DATETIME,
  `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: forbidden 1: support delete',
  `category_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_lease_configs`;


CREATE TABLE `eh_lease_configs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',
  `config_name` VARCHAR(128),
  `config_value` VARCHAR(128),
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `category_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_lease_form_requests`;


CREATE TABLE `eh_lease_form_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `source_id` BIGINT NOT NULL,
  `source_type` VARCHAR(64) NOT NULL,
  `create_time` DATETIME COMMENT 'record create time',
  `category_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `category_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_lease_project_communities`;


CREATE TABLE `eh_lease_project_communities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `lease_project_id` BIGINT NOT NULL COMMENT 'lease project id',
  `community_id` BIGINT NOT NULL COMMENT 'community id',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `category_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_lease_projects`;


CREATE TABLE `eh_lease_projects` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_id` BIGINT NOT NULL COMMENT 'id of the record',
  `city_id` BIGINT NOT NULL COMMENT 'city id in region table',
  `city_name` VARCHAR(64) COMMENT 'redundant for query optimization',
  `area_id` BIGINT NOT NULL COMMENT 'area id in region table',
  `area_name` VARCHAR(64) COMMENT 'redundant for query optimization',
  `address` VARCHAR(512),
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `contact_name` VARCHAR(128),
  `contact_phone` VARCHAR(128) COMMENT 'the phone number',
  `contact_uid` BIGINT,
  `description` TEXT,
  `traffic_description` TEXT,
  `poster_uri` VARCHAR(256),
  `extra_info_json` TEXT,
  `creator_uid` BIGINT COMMENT 'user who suggested the creation',
  `create_time` DATETIME,
  `category_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_lease_promotion_attachments`;


CREATE TABLE `eh_lease_promotion_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(128) NOT NULL,
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_lease_promotion_communities`;


CREATE TABLE `eh_lease_promotion_communities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `lease_promotion_id` BIGINT NOT NULL COMMENT 'lease promotion id',
  `community_id` BIGINT NOT NULL COMMENT 'community id',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_lease_promotions`;


CREATE TABLE `eh_lease_promotions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `rent_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'For rent',
  `poster_uri` VARCHAR(128),
  `rent_areas` VARCHAR(128),
  `description` TEXT,
  `create_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `building_id` BIGINT NOT NULL DEFAULT 0,
  `building_name` VARCHAR(512),
  `contacts` VARCHAR(128),
  `contact_phone` VARCHAR(128),
  `contact_uid` BIGINT,
  `enter_time` DATETIME COMMENT 'enter time',
  `namespace_type` VARCHAR(128),
  `namespace_token` VARCHAR(256),
  `enter_time_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `address_id` BIGINT NOT NULL DEFAULT 0,
  `apartment_name` VARCHAR(128),
  `orientation` VARCHAR(128),
  `rent_amount` DECIMAL(10,2),
  `issuer_type` VARCHAR(128) COMMENT '1: organization 2: normal_user',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `address` VARCHAR(512),
  `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form',
  `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field',
  `default_order` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT,
  `house_resource_type` VARCHAR(256) COMMENT '房源类型  rentHouse 出租房源   sellHouse 出售房源',
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

DROP TABLE IF EXISTS `eh_me_web_menus`;
CREATE TABLE `eh_me_web_menus` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `name` VARCHAR(255) NOT NULL,
  `action_path` VARCHAR(255) NOT NULL,
  `action_data` VARCHAR(255),
  `icon_uri` VARCHAR(255),
  `position_flag` TINYINT DEFAULT 1 COMMENT 'position, 1-NORMAL, 2-bottom',
  `sort_num` INTEGER DEFAULT 0,
  `status` TINYINT DEFAULT 2 COMMENT '0: inactive, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_meeting_attachments`;


CREATE TABLE `eh_meeting_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'owner type EhMeetingRecords/EhMeetingReservations',
  `owner_id` BIGINT NOT NULL COMMENT 'key of the owner',
  `content_name` VARCHAR(1024) COMMENT 'attachment object content name like: abc.jpg',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `content_size` INTEGER COMMENT 'attachment object size',
  `content_icon_uri` VARCHAR(1024) COMMENT 'attachment object link of content icon',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_meeting_invitations`;


CREATE TABLE `eh_meeting_invitations` (
  `id` BIGINT NOT NULL,
  `meeting_reservation_id` BIGINT NOT NULL COMMENT '会议预约eh_meeting_reservations的id',
  `source_type` VARCHAR(45) NOT NULL COMMENT '机构或者个人：ORGANIZATION OR MEMBER_DETAIL',
  `source_id` BIGINT NOT NULL COMMENT '机构id或员工detail_id',
  `source_name` VARCHAR(64) NOT NULL COMMENT '机构名称或者员工的姓名',
  `role_type` VARCHAR(16) NOT NULL COMMENT '参会人或抄送人',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY (`id`),
  KEY `i_eh_meeting_reservation_id` (`meeting_reservation_id`),
  KEY `i_eh_source_id` (`source_type`,`source_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='会议邀请清单，即参会人和抄送人清单';

DROP TABLE IF EXISTS `eh_meeting_records`;


CREATE TABLE `eh_meeting_records` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `meeting_reservation_id` BIGINT NOT NULL COMMENT '会议预约ID，id of eh_meeting_reservations',
  `meeting_subject` VARCHAR(256) COMMENT '会议主题，冗余字段，用于纪要列表展示主题名称',
  `content` TEXT COMMENT '会议纪要详细内容',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operator_name` VARCHAR(64) COMMENT '操作人姓名',
  `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否',
  PRIMARY KEY (`id`),
  KEY `i_eh_meeting_reservation_id` (`meeting_reservation_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='会议纪要表';

DROP TABLE IF EXISTS `eh_meeting_reservations`;


CREATE TABLE `eh_meeting_reservations` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `organization_id` BIGINT NOT NULL COMMENT '总公司ID',
  `subject` VARCHAR(256) COMMENT '会议主题',
  `content` TEXT COMMENT '会议详细内容',
  `meeting_room_name` VARCHAR(128) COMMENT '会议室名称，预约会议室后保存会议室名称，后期该值不随着会议室编辑而改变',
  `meeting_room_seat_count` INTEGER COMMENT '可容纳座位数，预约会议室后保存会议室名称，后期该值不随着会议室编辑而改变',
  `meeting_room_id` BIGINT COMMENT '会议室id,id of eh_meeting_rooms',
  `meeting_sponsor_user_id` BIGINT NOT NULL DEFAULT 0 COMMENT '会议发起人的user_id',
  `meeting_sponsor_detail_id` BIGINT NOT NULL COMMENT '会议发起人的detail_id',
  `meeting_sponsor_name` VARCHAR(64) NOT NULL COMMENT '会议发起人的姓名',
  `meeting_recorder_user_id` BIGINT COMMENT '会议纪要人user_id',
  `meeting_recorder_detail_id` BIGINT COMMENT '会议纪要人detail_id',
  `meeting_recorder_name` VARCHAR(64) COMMENT '会议纪要人的姓名',
  `invitation_user_count` INTEGER COMMENT '会议受邀人数',
  `meeting_date` date NOT NULL COMMENT '会议预定日期',
  `expect_begin_time` DATETIME NOT NULL COMMENT '预计开始时间（预订会议室的时间）',
  `expect_end_time` DATETIME NOT NULL COMMENT '预计结束时间（预订会议室的时间）',
  `lock_begin_time` DATETIME NOT NULL COMMENT '实际锁定开始时间',
  `lock_end_time` DATETIME NOT NULL COMMENT '实际锁定结束时间',
  `act_begin_time` DATETIME COMMENT '实际开始时间，只有用户操作了开始会议才有值',
  `act_end_time` DATETIME COMMENT '实际结束时间，只有用户操作了结束会议才有值',
  `system_message_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启系统消息通知：0-关闭消息通知 1-开启消息通知',
  `email_message_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启邮件通知：0-关闭邮件通知 1-开启邮件通知',
  `act_remind_time` DATETIME COMMENT '实际发出提醒的时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0:DELETED 删除，1:时间锁定， 2:CANCELED 取消,3:NORMAL 正常状态',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `attachment_flag` TINYINT DEFAULT 0 COMMENT '是否有附件 1-是 0-否',
  PRIMARY KEY (`id`),
  KEY `i_eh_namespace_organization_id` (`namespace_id`,`organization_id`),
  KEY `i_eh_meeting_date` (`meeting_date`),
  KEY `i_eh_meeting_room_id` (`meeting_room_id`),
  KEY `i_eh_meeting_sponsor_detail_id` (`meeting_sponsor_detail_id`),
  KEY `i_eh_meeting_recorder_detail_id` (`meeting_recorder_detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='会议室预约表';

DROP TABLE IF EXISTS `eh_meeting_rooms`;


CREATE TABLE `eh_meeting_rooms` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `organization_id` BIGINT NOT NULL COMMENT '总公司ID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations，表示会议室归属的企业',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID  会议室归属的分公司',
  `name` VARCHAR(128) NOT NULL COMMENT '会议室名称',
  `seat_count` INTEGER NOT NULL COMMENT '可容纳座位数',
  `description` VARCHAR(512) COMMENT '描述',
  `open_begin_time` time NOT NULL COMMENT '会议室可预订的起始时间',
  `open_end_time` time NOT NULL COMMENT '会议室可预订的结束时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:  0: DELETED 删除  1:CLOSED 不可用  2 : OPENING 可用',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operator_name` VARCHAR(64) COMMENT '操作人姓名',
  PRIMARY KEY (`id`),
  KEY `i_eh_namespace_owner_name` (`namespace_id`,`organization_id`,`owner_type`,`owner_id`,`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='会议室资源管理表';

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

DROP TABLE IF EXISTS `eh_message_records`;


CREATE TABLE `eh_message_records` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER DEFAULT 0,
  `dst_channel_token` VARCHAR(32),
  `dst_channel_type` VARCHAR(32),
  `status` VARCHAR(32) COMMENT 'message status',
  `app_id` BIGINT DEFAULT 1 COMMENT 'default to messaging app itself',
  `message_seq` BIGINT COMMENT 'message sequence id generated at server side',
  `sender_uid` BIGINT,
  `sender_tag` VARCHAR(32) COMMENT 'sender generated tag',
  `channels_info` VARCHAR(2048),
  `body_type` VARCHAR(32),
  `body` VARCHAR(2048),
  `delivery_option` int(2) DEFAULT 0,
  `create_time` DATETIME NOT NULL COMMENT 'message creation time',
  `session_token` VARCHAR(128),
  `device_id` VARCHAR(2048),
  `index_id` BIGINT,
  PRIMARY KEY (`id`),
  KEY `i_eh_index_id_index` (`index_id`) USING BTREE,
  KEY `i_eh_sender_uid_index` (`sender_uid`) USING BTREE,
  KEY `i_eh_dst_channel_token_index` (`dst_channel_token`) USING BTREE,
  KEY `i_en_namespace_id_index` (`namespace_id`) USING BTREE
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
  `name_type` TINYINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_namespace_id` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for eh_namespace_masks
-- ----------------------------
DROP TABLE IF EXISTS `eh_namespace_masks`;


CREATE TABLE `eh_namespace_masks` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `item_name` VARCHAR(64) NOT NULL,
  `image_type` TINYINT,
  `tips` VARCHAR(255),
  `scene_type` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`)
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
  `default_order` INTEGER DEFAULT 0,
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

DROP TABLE IF EXISTS `eh_new_preview`;


CREATE TABLE `eh_new_preview` (
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
  `phone` VARCHAR(32) DEFAULT 0,
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
  `visible_type` VARCHAR(32),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `phone` VARCHAR(32) DEFAULT 0,
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
  `visible_type` VARCHAR(32),
  `create_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0-后台创建 1-第三方调用接口',
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
  `entry_id` INTEGER,
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

DROP TABLE IF EXISTS `eh_news_comment_rule`;


CREATE TABLE `eh_news_comment_rule` (
  `id` BIGINT NOT NULL,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_news_communities`;


CREATE TABLE `eh_news_communities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `news_id` BIGINT NOT NULL COMMENT 'news id',
  `community_id` BIGINT NOT NULL COMMENT 'community id',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_news_tag`;
-- oauth2client 1.0   add by xq.tian 2017/03/09
--
CREATE TABLE `eh_news_tag` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT DEFAULT 0,
  `parent_id` BIGINT DEFAULT 0,
  `value` VARCHAR(32),
  `is_search` TINYINT(8) DEFAULT 0 COMMENT '是否开启筛选',
  `is_default` TINYINT(8) DEFAULT 0 COMMENT '是否是默认选项',
  `delete_flag` TINYINT(8) NOT NULL DEFAULT 0,
  `default_order` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `category_id` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- oauth2 client AccessToken
DROP TABLE IF EXISTS `eh_news_tag_vals`;
--

CREATE TABLE `eh_news_tag_vals` (
  `id` BIGINT NOT NULL,
  `news_id` BIGINT NOT NULL DEFAULT 0,
  `news_tag_id` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `status` TINYINT,
  `position_nums` INTEGER,
  `name` VARCHAR(256),
  `unit_price` DECIMAL(10,2),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_office_cubicle_cities`;


CREATE TABLE `eh_office_cubicle_cities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `province_id` BIGINT COMMENT '省份id',
  `province_name` VARCHAR(100) COMMENT '省份名称',
  `city_id` BIGINT COMMENT '城市id',
  `city_name` VARCHAR(128) COMMENT '城市名称',
  `icon_uri` VARCHAR(1024) COMMENT '城市图片uri',
  `default_order` BIGINT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `flow_case_Id` BIGINT,
  `work_flow_status` TINYINT,
  `owner_type` VARCHAR(128),
  `owner_id` BIGINT,
  `position_nums` INTEGER,
  `category_name` VARCHAR(256),
  `category_id` BIGINT,
  `employee_number` INTEGER,
  `financing_flag` TINYINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_office_cubicle_ranges`;


CREATE TABLE `eh_office_cubicle_ranges` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the ranges, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `space_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_office_cubicle_selected_cities`;


CREATE TABLE `eh_office_cubicle_selected_cities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `province_name` VARCHAR(100) COMMENT '省份名称',
  `city_name` VARCHAR(128) COMMENT '城市名称',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `owner_type` VARCHAR(128),
  `owner_id` BIGINT,
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
  `building_id` BIGINT NOT NULL DEFAULT 0,
  `building_name` VARCHAR(128),
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
  KEY `fk_eh_orgaddr_owner` (`organization_id`),
  KEY `organization_address_orgnaization_id` (`organization_id`)
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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

  PRIMARY KEY (`id`),
  KEY `organization_detail_orgnaization_id` (`organization_id`)
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

DROP TABLE IF EXISTS `eh_organization_member_contracts`;


CREATE TABLE `eh_organization_member_contracts` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `contract_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the type of the contract',
  `contract_number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the number of the contract',
  `start_time` DATE NOT NULL COMMENT '生效时间',
  `end_time` DATE NOT NULL COMMENT '到期时间',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 3: active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_member_details`;


CREATE TABLE `eh_organization_member_details` (
  `id` BIGINT NOT NULL COMMENT 'id for members， reference for eh_organization_member detail_id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `target_type` VARCHAR(64),
  `target_id` BIGINT NOT NULL,
  `birthday` DATE COMMENT 'the birthday of the member',
  `birthday_index` VARCHAR(64) COMMENT 'only month like 0304',
  `organization_id` BIGINT NOT NULL COMMENT 'reference for eh_organization_member organization_id',
  `contact_name` VARCHAR(64) COMMENT 'the name of the member',
  `contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) COMMENT 'phone number, reference for eh_organization_member contact_token',
  `contact_description` TEXT,
  `employee_no` VARCHAR(128) COMMENT 'the employee number for the member',
  `avatar` VARCHAR(128) COMMENT '头像',
  `gender` TINYINT DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
  `marital_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: undisclosured, 1: married, 2: unmarried',
  `political_flag` VARCHAR(128) COMMENT '政治面貌',
  `native_place` VARCHAR(128) COMMENT '籍贯',
  `en_name` VARCHAR(128) COMMENT 'english name',
  `reg_residence` VARCHAR(128) COMMENT '户口',
  `id_number` VARCHAR(64) COMMENT 'ID Card number',
  `email` VARCHAR(128) COMMENT 'email for members',
  `wechat` VARCHAR(128),
  `qq` VARCHAR(128),
  `emergency_name` VARCHAR(128) COMMENT 'emergency contact name',
  `emergency_contact` VARCHAR(128) COMMENT 'emergency contact tel-number',
  `address` VARCHAR(255) COMMENT 'address for the member',
  `employee_type` TINYINT COMMENT '0: full-time, 1: part-time, 2: internship, 3: labor dispatch',
  `employee_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0:probation, 1:on the job, 2:internship, 3:dismissal',
  `employment_time` DATE COMMENT '转正日期',
  `dismiss_time` DATE,
  `salary_card_number` VARCHAR(128) COMMENT '工资卡号',
  `social_security_number` VARCHAR(128) COMMENT '社保号',
  `provident_fund_number` VARCHAR(128) COMMENT '公积金号',
  `check_in_time` DATE COMMENT '入职日期',
  `check_in_time_index` VARCHAR(64) COMMENT '入职日期索引字段',
  `region_code` VARCHAR(64) COMMENT '手机区号',
  `procreative` VARCHAR(64) COMMENT '生育状况',
  `ethnicity` VARCHAR(128) COMMENT '民族',
  `id_type` VARCHAR(64) COMMENT '证件类型',
  `id_expiry_date` DATE COMMENT '证件有效期',
  `degree` VARCHAR(64) COMMENT '学历',
  `graduation_school` VARCHAR(256) COMMENT '毕业学校',
  `graduation_time` DATE COMMENT '毕业时间',
  `emergency_relationship` VARCHAR(128) COMMENT '紧急联系人关系',
  `contact_short_token` VARCHAR(128) COMMENT '短号',
  `work_email` VARCHAR(128) COMMENT '工作邮箱',
  `contract_party_id` BIGINT COMMENT '合同主体',
  `work_start_time` DATE COMMENT '参加工作日期',
  `contract_start_time` DATE COMMENT '合同开始日期',
  `contract_end_time` DATE COMMENT '合同终止日期',
  `salary_card_bank` VARCHAR(64) COMMENT '开户行',
  `reg_residence_type` VARCHAR(64) COMMENT '户籍类型',
  `id_photo` TEXT COMMENT '身份证照片',
  `visa_photo` TEXT COMMENT '一寸免冠照',
  `life_photo` TEXT COMMENT '生活照',
  `entry_form` TEXT COMMENT '入职登记表',
  `graduation_certificate` TEXT COMMENT '毕业证书',
  `degree_certificate` TEXT COMMENT '学位证书',
  `contract_certificate` TEXT COMMENT '劳动合同',
  `social_security_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-pending, 1-paying',
  `accumulation_fund_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-pending, 1-paying',
  PRIMARY KEY (`id`),
  KEY `target_id_index` (`target_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_member_educations`;


CREATE TABLE `eh_organization_member_educations` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `school_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the member''s school name',
  `degree` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'doctor, master, bachelor, etc',
  `major` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the major of the member',
  `enrollment_time` DATE NOT NULL COMMENT 'the time to start a new semester',
  `graduation_time` DATE NOT NULL COMMENT 'when the member graduated form the school',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 3: active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_member_insurances`;


CREATE TABLE `eh_organization_member_insurances` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of the insurance',
  `enterprise` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the company name of the insurance',
  `number` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the number of the insurance',
  `start_time` DATE NOT NULL COMMENT '生效时间',
  `end_time` DATE NOT NULL COMMENT '到期时间',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 3: active',
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
  `contact_description` TEXT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_member_profile_logs`;


CREATE TABLE `eh_organization_member_profile_logs` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `person_change_type` VARCHAR(64) COMMENT '人员变动类型:入职,转正,离职;变更部门,岗位,职级',
  `person_change_reason` VARCHAR(256) COMMENT 'person change reason',
  `operation_type` VARCHAR(32) COMMENT 'add,update,delete',
  `operation_time` DATETIME NOT NULL COMMENT 'when the information of the employee has been changed',
  `operator_uid` BIGINT COMMENT 'id of operator',
  `resource_type` VARCHAR(32) COMMENT 'the name of the table',
  `resource_id` BIGINT COMMENT 'reference for table_id',
  `result_code` INTEGER COMMENT '0: unsucceed, 1: succeed',
  `original_content` LONGTEXT COMMENT 'original records, use json format',
  `audit_content` LONGTEXT COMMENT 'modified records, use json format',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_member_work_experiences`;


CREATE TABLE `eh_organization_member_work_experiences` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `detail_id` BIGINT NOT NULL COMMENT 'id for members, reference for eh_organization_member_details id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of company',
  `position` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the position of the member',
  `job_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: full-time, 1: part-time',
  `entry_time` DATE NOT NULL COMMENT 'timing of start the job',
  `departure_time` DATE NOT NULL COMMENT 'timing of quit the job',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 3: active',
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
  `detail_id` BIGINT COMMENT 'id for detail records',
  `source_type` TINYINT COMMENT '认证来源',
  PRIMARY KEY (`id`),
  KEY `fk_eh_orgm_owner` (`organization_id`),
  KEY `i_eh_corg_group` (`member_group`),
  KEY `i_target_id` (`target_id`),
  KEY `i_contact_token` (`contact_token`),
  KEY `group_type_index` (`group_type`),
  KEY `detail_id_index` (`detail_id`),
  KEY `group_path_index` (`group_path`),
  CONSTRAINT `eh_organization_members_ibfk_1` FOREIGN KEY (`organization_id`) REFERENCES `eh_organizations` (`id`) ON DELETE CASCADE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_members_test`;
--
-- member of global partition
CREATE TABLE `eh_organization_members_test` (
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
  `detail_id` BIGINT COMMENT 'id for detail records',
  PRIMARY KEY (`id`),
  KEY `fk_eh_orgm_owner` (`organization_id`),
  KEY `i_eh_corg_group` (`member_group`),
  KEY `i_target_id` (`target_id`),
  KEY `i_contact_token` (`contact_token`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
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
  `community_id` VARCHAR(256),
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
  `namespace_customer_type` VARCHAR(128),
  `namespace_customer_token` VARCHAR(128),
  `contact_extra_tels` VARCHAR(1024) COMMENT '客户多手机号，以jsonarray方式存储',
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
  `email_content` TEXT COMMENT '工资条发送邮件内容',
  `website` VARCHAR(256),
  `unified_social_credit_code` VARCHAR(256),
  `order` INTEGER DEFAULT 0 COMMENT 'order',
  `admin_target_id` BIGINT,
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

DROP TABLE IF EXISTS `eh_parking_business_payee_accounts`;


CREATE TABLE `eh_parking_business_payee_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` BIGINT NOT NULL COMMENT '园区id或者其他id',
  `parking_lot_id` BIGINT NOT NULL COMMENT '停车场id',
  `parking_lot_name` VARCHAR(512) NOT NULL COMMENT '停车场名称',
  `business_type` VARCHAR(32) NOT NULL COMMENT '业务 tempfee:临时车缴费 vipParking:vip车位预约 monthRecharge:月卡充值',
  `payee_id` BIGINT NOT NULL COMMENT '支付帐号id',
  `payee_user_type` VARCHAR(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='停车充值收款账户表';

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

DROP TABLE IF EXISTS `eh_parking_car_verifications`;


CREATE TABLE `eh_parking_car_verifications` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `requestor_enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of organization where the requestor is in',
  `requestor_enterprise_name` VARCHAR(64) COMMENT 'the enterprise name of plate owner',
  `requestor_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'requestor id',
  `plate_number` VARCHAR(64),
  `plate_owner_name` VARCHAR(64) COMMENT 'the name of plate owner',
  `plate_owner_phone` VARCHAR(64) COMMENT 'the phone of plate owner',
  `status` TINYINT COMMENT '0: inactive, 1: queueing, 2: notified, 3: issued',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `flow_case_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow case id',
  `source_type` TINYINT COMMENT '1: card request, 2: car verify',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `flow_case_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow case id',
  `audit_succeed_time` DATETIME,
  `process_succeed_time` DATETIME,
  `open_card_time` DATETIME,
  `cancel_time` DATETIME,
  `card_type_id` VARCHAR(64),
  `address_id` BIGINT,
  `invoice_type` bigint(4),
  `identity_card` VARCHAR(40),
  `card_type_name` VARCHAR(128) COMMENT '冗余存储卡类型名称',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_parking_card_types`;


CREATE TABLE `eh_parking_card_types` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `card_type_id` VARCHAR(128),
  `card_type_name` VARCHAR(128),
  `status` TINYINT COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 车辆放行记录
--
DROP TABLE IF EXISTS `eh_parking_clearance_logs`;


CREATE TABLE `eh_parking_clearance_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `parking_lot_id` BIGINT COMMENT 'eh_parking_lots id',
  `applicant_id`   BIGINT COMMENT 'applicant id',
  `operator_id`    BIGINT COMMENT 'operator id',
  `plate_number`   VARCHAR(32) COMMENT 'plate number',
  `apply_time`     DATETIME COMMENT 'apply time',
  `clearance_time` DATETIME COMMENT 'The time the vehicle passed',
  `remarks` VARCHAR(1024) COMMENT 'remarks',
  `status` TINYINT COMMENT '0: inactive, 1: processing, 2: completed, 3: cancelled, 4: pending',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `log_token` VARCHAR(1024) COMMENT 'The info from third',
  `log_json` TEXT COMMENT 'The info from third',
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
  `card_type_tip_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '1: support , 0: not',
  `card_type_tip` TEXT,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_parking_hubs`;


CREATE TABLE `eh_parking_hubs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `hub_name` VARCHAR(64) NOT NULL COMMENT 'hub的名称',
  `hub_mac` VARCHAR(128) NOT NULL COMMENT 'hub的mac地址',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_parking_invoice_types`;


CREATE TABLE `eh_parking_invoice_types` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `invoice_token` VARCHAR(256),
  `name` VARCHAR(128),
  `status` TINYINT COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,

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
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `recharge_json` VARCHAR(1024),
  `config_json` VARCHAR(1024),
  `order_tag` VARCHAR(3) NOT NULL COMMENT '停车场订单生成标识，固定3位',
  `order_code` BIGINT NOT NULL DEFAULT 0 COMMENT '停车场订单生成码,从0开始，最多8位',
  `id_hash` VARCHAR(128) COMMENT 'id_hash',
  `func_list` TEXT COMMENT '此停车场对接的功能列表Json,如["tempfee","monthRecharge"]',
  `notice_contact` VARCHAR(20) COMMENT '用户须知联系电话',
  `summary` TEXT COMMENT '用户须知',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


--
-- member of global partition
--
DROP TABLE IF EXISTS `eh_parking_recharge_orders`;


CREATE TABLE `eh_parking_recharge_orders` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `order_no` BIGINT,
  `biz_order_no` VARCHAR(64),
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
  `start_period` DATETIME,
  `end_period` DATETIME,
  `paid_type` VARCHAR(32) COMMENT 'the type of payer',
  `is_delete` TINYINT NOT NULL DEFAULT 0 COMMENT 'the order is delete, 0 : is not deleted, 1: deleted',
  `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: monthly, 2: temporary',
  `order_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from 3rd system',
  `parking_time` INTEGER COMMENT 'parking-time',
  `error_description` TEXT COMMENT 'error description',
  `error_description_json` TEXT COMMENT 'error description',
  `refund_time` DATETIME COMMENT 'refund time',
  `delay_time` INTEGER COMMENT 'delay time',
  `order_type` TINYINT DEFAULT 1,
  `original_price` DECIMAL(10,2),
  `card_request_id` BIGINT,
  `invoice_type` BIGINT(4),
  `paid_version` TINYINT,
  `pay_order_no` VARCHAR(64) COMMENT '支付系统单号',
  `payee_id` BIGINT COMMENT '收款方id',
  `invoice_status` TINYINT COMMENT '0 =发票未开，2发票已开',
  `invoice_create_time` DATETIME COMMENT '发票开票时间',
  `pay_source` VARCHAR(16) DEFAULT 'app' COMMENT 'app:APP支付, qrcode:扫码支付',
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

DROP TABLE IF EXISTS `eh_parking_space_logs`;


CREATE TABLE `eh_parking_space_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `space_no` VARCHAR(64) NOT NULL,
  `lock_id` VARCHAR(128),
  `contact_phone` VARCHAR(64),
  `contact_name` VARCHAR(64),
  `contact_enterprise_name` VARCHAR(128),
  `operate_type` TINYINT NOT NULL COMMENT '1: up, 2: down',
  `user_type` TINYINT NOT NULL COMMENT '1: booking person, 2: plate owner',
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_parking_spaces`;


CREATE TABLE `eh_parking_spaces` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `space_no` VARCHAR(64) NOT NULL DEFAULT '',
  `space_address` VARCHAR(64) NOT NULL DEFAULT '',
  `lock_id` VARCHAR(128),
  `lock_status` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  `parking_hubs_id` BIGINT,
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


DROP TABLE IF EXISTS `eh_parking_user_invoices`;


CREATE TABLE `eh_parking_user_invoices` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parking_lot_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_parking_lots',
  `user_id` BIGINT,
  `invoice_type_id` BIGINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,

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


-- 支付2.0 （还会改），和合同字段 -- by wentian
-- 用途:请求左邻支付系统接口,需要使用到如下4个字段
-- 支付分配的账号信息,accountId/systemId/appKey/secretKey
-- system_id园区系统填1,电商系统填2
DROP TABLE IF EXISTS `eh_payment_accounts`;


CREATE TABLE `eh_payment_accounts` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(128),
  `account_id` BIGINT,
  `system_id` INTEGER,
  `app_key` VARCHAR(256),
  `secret_key` VARCHAR(1024),
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_app_views`;


CREATE TABLE `eh_payment_app_views` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'primary key',
  `namespace_id` INTEGER,
  `community_id` BIGINT,
  `has_view` TINYINT NOT NULL,
  `view_item` VARCHAR(16) NOT NULL,
  `remark1_type` VARCHAR(16),
  `remark1_identifier` VARCHAR(128),
  `remark2_type` VARCHAR(16),
  `remark2_identifier` VARCHAR(128),
  `remark3_type` VARCHAR(16),
  `remark3_identifier` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='缴费app端支付按钮和合同查看隐藏';

DROP TABLE IF EXISTS `eh_payment_applications`;


CREATE TABLE `eh_payment_applications` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `community_id` BIGINT COMMENT '园区id',
  `owner_id` BIGINT COMMENT '公司id',
  `title` VARCHAR(256) COMMENT '标题',
  `contract_id` BIGINT NOT NULL COMMENT 'id of eh_contracts',
  `request_id` BIGINT,
  `applicant_uid` BIGINT NOT NULL COMMENT '申请人id',
  `applicant_org_id` BIGINT NOT NULL COMMENT '申请人所属部门id',
  `payee` VARCHAR(256) COMMENT '收款单位',
  `payer` VARCHAR(256) COMMENT '付款单位',
  `due_bank` VARCHAR(256) COMMENT '收款银行',
  `bank_account` VARCHAR(256) COMMENT '银行账号',
  `payment_amount` DECIMAL(10,2) COMMENT '付款金额',
  `payment_rate` DOUBLE COMMENT '付款百分比',
  `remark` VARCHAR(256) COMMENT '备注',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive; 1: waiting for approval; 2: QUALIFIED; 3: UNQUALIFIED',
  `create_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  `application_number` VARCHAR(32),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_bill_attachments`;


CREATE TABLE `eh_payment_bill_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `filename` VARCHAR(1024) COMMENT '附件名称',
  `content_type` VARCHAR(64) COMMENT '附件类型：word/pdf/png...',
  `content_uri` VARCHAR(1024) COMMENT '附件uri',
  `content_url` VARCHAR(1024) COMMENT '附件url',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_bill_certificate`;


CREATE TABLE `eh_payment_bill_certificate` (
  `id` BIGINT NOT NULL,
  `bill_id` BIGINT NOT NULL COMMENT '该凭证记录对应的账单id',
  `certificate_uri` VARCHAR(255) COMMENT '上传凭证图片的uri',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_bill_groups`;


CREATE TABLE `eh_payment_bill_groups` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL,
  `name` VARCHAR(50) COMMENT '账单组名称',
  `balance_date_type` TINYINT COMMENT '1:pay each month; 2:each quarter; 3:each year',
  `bills_day` INTEGER,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `default_order` INTEGER,
  `due_day` INTEGER COMMENT '最晚还款日，距离账单日的距离，单位可以为月 ',
  `due_day_type` TINYINT DEFAULT 1 COMMENT '1:日，2：月 ',
  `brother_group_id` BIGINT COMMENT '兄弟账单组id，联动效果',
  `bills_day_type` TINYINT NOT NULL DEFAULT '4' COMMENT '1. 本周期前几日；2.本周期第几日；3.本周期结束日；4.下周期首月第几日',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id',
  `biz_payee_type` VARCHAR(128) COMMENT '收款方账户类型：EhUsers/EhOrganizations',
  `biz_payee_id` BIGINT COMMENT '收款方账户id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='账单组表';

DROP TABLE IF EXISTS `eh_payment_bill_groups_rules`;


CREATE TABLE `eh_payment_bill_groups_rules` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `bill_group_id` BIGINT NOT NULL DEFAULT 0,
  `charging_item_id` BIGINT NOT NULL DEFAULT 0,
  `charging_standards_id` BIGINT NOT NULL DEFAULT 0,
  `charging_item_name` VARCHAR(32),
  `variables_json_string` VARCHAR(2048) COMMENT 'json strings of variables injected for a particular formula',
  `ownerType` VARCHAR(32) NOT NULL,
  `ownerId` BIGINT NOT NULL DEFAULT 0,
  `bill_item_month_offset` INTEGER COMMENT '收费项产生时间偏离当前月的月数',
  `bill_item_day_offset` INTEGER COMMENT '收费项产生时间偏离当前月的日数',
  `brother_rule_id` BIGINT COMMENT '兄弟账单组收费项id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='变量注入表';

DROP TABLE IF EXISTS `eh_payment_bill_items`;


CREATE TABLE `eh_payment_bill_items` (
  `id` BIGINT NOT NULL,
  `namespace_id` INT(20) NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL,
  `bill_group_id` BIGINT,
  `charging_items_id` BIGINT NOT NULL DEFAULT 0,
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `amount_receivable` DECIMAL(10,2) DEFAULT '0.00',
  `amount_receivable_without_tax` decimal(10,2)  COMMENT '应收（不含税）',
  `tax_amount` decimal(10,2)  COMMENT '税额',
  `tax_rate` decimal(10,2)  COMMENT '税率',
  `amount_received` DECIMAL(10,2) DEFAULT '0.00',
  `amount_received_without_tax` decimal(10,2)  COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) DEFAULT '0.00',
  `amount_owed_without_tax` decimal(10,2) COMMENT '待收（不含税）',
  `target_type` VARCHAR(32),
  `target_id` BIGINT,
  `target_name` VARCHAR(32) COMMENT '客户名称，客户没有在系统中时填写',
  `contract_id` BIGINT,
  `contract_num` VARCHAR(255),
  `property_identifer` VARCHAR(255) DEFAULT '' COMMENT '资产标识',
  `address_id` BIGINT,
  `building_name` VARCHAR(255),
  `apartment_name` VARCHAR(255),
  `date_str` VARCHAR(20) COMMENT '账期',
  `date_str_begin` VARCHAR(20),
  `date_str_end` VARCHAR(20),
  `date_str_due` VARCHAR(20),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `charging_item_name` VARCHAR(32),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0：未缴费;1:已缴费',
  `due_day_deadline` VARCHAR(30) COMMENT '最后付款日期',
  `date_str_generation` VARCHAR(40) COMMENT '费用产生日期',
  `bill_group_rule_id` BIGINT,
  `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是',
  `late_fine_standard_id` BIGINT COMMENT '滞纳金标准id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `energy_consume` VARCHAR(1024) COMMENT '能耗用量',
  `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）',
  `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）',
  `source_name` VARCHAR(1024) COMMENT '账单来源（如：停车缴费，缴费的新增/导入等）',
  `consume_user_id` BIGINT COMMENT '企业下面的某个人的ID',
  `can_delete` TINYINT DEFAULT 0 COMMENT '0：不可删除；1：可删除',
  `can_modify` TINYINT DEFAULT 0 COMMENT '0：不可编辑；1：可编辑',
  `delete_flag` TINYINT DEFAULT 1 COMMENT '删除状态：0：已删除；1：正常使用',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='明细';

DROP TABLE IF EXISTS `eh_payment_bill_notice_records`;


CREATE TABLE `eh_payment_bill_notice_records` (
  `id` BIGINT NOT NULL,
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `notice_date` DATETIME,
  `target_type` VARCHAR(32) COMMENT 'untrack, user',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'target user id if target_type is a user',
  `target_name` VARCHAR(32),
  `target_contact_tel` VARCHAR(32),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='发送记录表';

DROP TABLE IF EXISTS `eh_payment_bill_orders`;


CREATE TABLE `eh_payment_bill_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` int(10),
  `bill_id` VARCHAR(255),
  `order_number` VARCHAR(255) COMMENT '业务订单编号，如：WUF00000000000004926',
  `payment_order_id` BIGINT COMMENT '支付系统订单ID',
  `general_order_id` BIGINT COMMENT '统一订单ID',
  `amount` DECIMAL(10,2),
  `payment_status` INTEGER DEFAULT 0 COMMENT '支付状态，0-待支付、1-支付成功、2-支付中、5-支付失败',
  `payment_order_type` INTEGER NOT NULL DEFAULT 0 COMMENT '支付系统中的订单类型：1-RECHARGE(充值)、2-WITHDRAW(提现)、3-PURCHACE(支付), 4-REFUND(退款)',
  `payment_type` INTEGER NOT NULL DEFAULT 0 COMMENT '支付类型，由支付系统定义（参考通联），如0-未选择支付方式、1-WECHAT_APPPAY(微信APP支付)、8-ALI_SCAN_PAY(阿里扫码支付)、9-WECHAT_JS_PAY(微信公众号支付)、21-WECHAT_JS_ORG_PAY(微信公众号集团支付)',
  `payment_time` DATETIME COMMENT '支付时间（缴费时间）',
  `payment_channel` INTEGER DEFAULT 0 COMMENT '支付渠道: 0-未知、1-微信、2-支付宝、3-现金',
  `uid` BIGINT COMMENT '缴费人',
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_bills`;


CREATE TABLE `eh_payment_bills` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL,
  `bill_group_id` BIGINT,
  `date_str` VARCHAR(10),
  `address_id` BIGINT,
  `amount_receivable` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount should be received',
  `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）',
  `tax_amount` DECIMAL(10,2) COMMENT '税额',
  `amount_received` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount actually received by far',
  `amount_received_without_tax` decimal(10,2)  COMMENT '已收（不含税）',
  `amount_owed` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'unpaid amount',
  `amount_owed_without_tax` decimal(10,2)  COMMENT '待收（不含税）',
  `amount_exemption` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount reduced',
  `amount_supplement` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount increased',
  `target_type` VARCHAR(32) COMMENT 'untrack, user',
  `target_id` BIGINT DEFAULT 0 COMMENT 'target user id if target_type is a user',
  `contract_id` BIGINT,
  `contract_num` VARCHAR(255),
  `target_name` VARCHAR(32) DEFAULT '' COMMENT '客户名称',
  `apartment_name` VARCHAR(255),
  `building_name` VARCHAR(255),
  `noticeTel` VARCHAR(255) COMMENT '催缴手机号码',
  `status` TINYINT DEFAULT 0 COMMENT '0: upfinished; 1: paid off',
  `notice_times` INTEGER DEFAULT 0 COMMENT 'times bill owner has been called for dued payments',
  `switch` TINYINT DEFAULT 0 COMMENT '0:未出账单；1：已出账单；3：其他作用状态',
  `creator_id` BIGINT,
  `creat_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `operator_uid` BIGINT,
  `next_switch` TINYINT DEFAULT 0 COMMENT '下一次switch的值',
  `date_str_begin` VARCHAR(30) COMMENT '账期开始日期',
  `date_str_end` VARCHAR(30) COMMENT '账期结束日期',
  `date_str_due` VARCHAR(30) COMMENT '出账单日期',
  `due_day_deadline` VARCHAR(30) COMMENT '最后付款日期',
  `due_day_count` BIGINT COMMENT '欠费天数',
  `charge_status` TINYINT DEFAULT 0 COMMENT '缴费状态，0：正常；1：欠费',
  `real_paid_time` DATETIME COMMENT '实际付款时间',
  `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是',
  `customer_tel` VARCHAR(32) COMMENT '客户的手机号，用于存储个人客户的信息',
  `invoice_number` VARCHAR(128) COMMENT '发票编号',
  `payment_type` INTEGER COMMENT '账单的支付方式（0-线下缴费，1-微信支付，2-对公转账，8-支付宝支付）',
  `certificate_note` VARCHAR(255) COMMENT '上传凭证图片时附加的留言',
  `is_upload_certificate` TINYINT COMMENT '该账单是否上传了缴费凭证（0:否，1：是）',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `source_type` VARCHAR(1024) COMMENT '各个业务系统定义的唯一标识（类型）',
  `source_id` BIGINT COMMENT '各个业务系统定义的唯一标识（id）',
  `source_name` VARCHAR(1024) COMMENT '账单来源（如：停车缴费，缴费的新增/导入等）',
  `consume_user_id` BIGINT COMMENT '企业下面的某个人的ID',
  `can_delete` TINYINT DEFAULT 0 COMMENT '0：不可删除；1：可删除',
  `can_modify` TINYINT DEFAULT 0 COMMENT '0：不可编辑；1：可编辑',
  `delete_flag` TINYINT DEFAULT 1 COMMENT '删除状态：0：已删除；1：正常使用',
  `third_bill_id` VARCHAR(1024) COMMENT '账单表增加第三方唯一标识字段',
  `customer_id` BIGINT COMMENT '企业客户ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

DROP TABLE IF EXISTS `eh_payment_card_accounts`;


CREATE TABLE `eh_payment_card_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `account_id` BIGINT,
  `craete_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_card_issuer_communities`;


CREATE TABLE `eh_payment_card_issuer_communities` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `issuer_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the card issuer',
  `hotline` VARCHAR(255),
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
  `biz_order_no` VARCHAR(128),
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
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `activate_time` DATETIME,
  `expired_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0 : inactive,1:wating 2: active ',
  `vendor_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'reference to name of vendors taotaogu',
  `vendor_card_data` TEXT COMMENT 'the extra information of card for example make_no',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_payment_charging_item_scopes`;


CREATE TABLE `eh_payment_charging_item_scopes` (
  `id` BIGINT NOT NULL,
  `charging_item_id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL,
  `project_level_name` VARCHAR(30) COMMENT '园区自定义的收费项目名字',
  `decoupling_flag` TINYINT DEFAULT 0 COMMENT '解耦标志，0:耦合中，收到域名下全部设置的影响;1:副本解耦',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id',
  `tax_rate` DECIMAL(10,2) COMMENT '税率',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='收费项目范围表';

DROP TABLE IF EXISTS `eh_payment_charging_items`;


CREATE TABLE `eh_payment_charging_items` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(15),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `default_order` INTEGER,
  `namespace_id` INTEGER COMMENT '增加域空间ID作为标识',
  `owner_id` BIGINT COMMENT '增加园区ID作为标识',
  `owner_type` VARCHAR(64) COMMENT '增加园区ID作为标识',
  `category_id` BIGINT COMMENT '多入口应用id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='收费项目表';

DROP TABLE IF EXISTS `eh_payment_charging_standards`;


CREATE TABLE `eh_payment_charging_standards` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(10),
  `charging_items_id` BIGINT NOT NULL DEFAULT 0,
  `formula` VARCHAR(1024),
  `formula_json` VARCHAR(2048),
  `formula_type` TINYINT COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `billing_cycle` TINYINT,
  `price_unit_type` TINYINT COMMENT '1:日单价; 2:月单价; 3:季单价; 4:年单价',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `instruction` VARCHAR(1024) COMMENT '说明',
  `suggest_unit_price` DECIMAL(10,2) COMMENT '建议单价',
  `area_size_type` INTEGER DEFAULT 1 COMMENT '计费面积类型,1：合同面积；2.建筑面积；3：使用面积；4：出租面积',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';

DROP TABLE IF EXISTS `eh_payment_charging_standards_scopes`;


CREATE TABLE `eh_payment_charging_standards_scopes` (
  `id` BIGINT NOT NULL,
  `charging_standard_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `namespace_id` INTEGER DEFAULT 0,
  `brother_standard_id` BIGINT COMMENT '兄弟收费标准id，联动效果',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准表';

DROP TABLE IF EXISTS `eh_payment_contract_receiver`;


CREATE TABLE `eh_payment_contract_receiver` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255),
  `target_id` BIGINT,
  `target_type` VARCHAR(255),
  `variables_json_string` VARCHAR(2048),
  `eh_payment_charging_standard_id` BIGINT,
  `eh_payment_charging_item_id` BIGINT,
  `contract_id` BIGINT,
  `contract_num` VARCHAR(255),
  `target_name` VARCHAR(255),
  `notice_tel` VARCHAR(255),
  `status` TINYINT DEFAULT 0 COMMENT '1:有效；0：无效',
  `address_ids_json` VARCHAR(2048),
  `in_work` TINYINT COMMENT '0:工作完成；1：正在生成',
  `is_recorder` TINYINT DEFAULT 1 COMMENT '0：合同状态记录者，不保存计价数据；1：不是合同状态记录者',
  `bill_group_rule_id` BIGINT,
  `contract_id_type` TINYINT DEFAULT 1 COMMENT '1:contract_id为合同id；0：不是',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_exemption_items`;


CREATE TABLE `eh_payment_exemption_items` (
  `id` BIGINT NOT NULL,
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `target_type` VARCHAR(255),
  `target_id` BIGINT,
  `targetName` VARCHAR(255) COMMENT '客户名称，客户没有在系统中时填写',
  `remarks` VARCHAR(255),
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='额外项';

DROP TABLE IF EXISTS `eh_payment_formula`;


CREATE TABLE `eh_payment_formula` (
  `id` BIGINT NOT NULL,
  `charging_standard_id` BIGINT,
  `name` VARCHAR(1024) COMMENT '公式名称',
  `constraint_variable_identifer` VARCHAR(255),
  `start_constraint` TINYINT COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `start_num` DECIMAL(10,2) DEFAULT '0.00',
  `end_constraint` TINYINT COMMENT '1:大于；2：大于等于；3：小于；4：小于等于',
  `end_num` DECIMAL(10,2) DEFAULT '0.00',
  `variables_json_string` VARCHAR(2048) COMMENT 'json strings of variables injected for a particular formula',
  `formula` VARCHAR(1024),
  `formula_json` VARCHAR(2048),
  `formula_type` TINYINT COMMENT '1: fixed fee; 2: normal formula; 3: gradient varied on variable price; 4: gradients varied functions on each variable section',
  `price_unit_type` TINYINT COMMENT '1:日单价; 2:月单价; 3:季单价; 4:年单价',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='收费标准公式表';

DROP TABLE IF EXISTS `eh_payment_late_fine`;


CREATE TABLE `eh_payment_late_fine` (
  `id` BIGINT NOT NULL COMMENT 'primary key',
  `name` VARCHAR(20) COMMENT '滞纳金名称',
  `amount` DECIMAL(10,2) NOT NULL DEFAULT '0.00' COMMENT 'amount of overdue payment',
  `bill_id` BIGINT NOT NULL COMMENT 'the id of the corresponding bill, one to one',
  `bill_item_id` BIGINT NOT NULL COMMENT 'the id of the corresponding bill item id, one to one',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `upate_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_uid` BIGINT,
  `namespace_id` INTEGER COMMENT 'location info, for possible statistics later',
  `community_id` BIGINT,
  `customer_id` BIGINT COMMENT 'allows searching taking advantage of it',
  `customer_type` VARCHAR(20) NOT NULL COMMENT 'break of user info benefits',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_notice_config`;


CREATE TABLE `eh_payment_notice_config` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(255),
  `owner_id` BIGINT,
  `notice_day_before` INTEGER,
  `notice_day_after` INTEGER COMMENT '欠费日期后多少天',
  `notice_day_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1:欠费前；2：欠费后',
  `notice_objs` VARCHAR(3064) COMMENT '催缴对象,格式为{type+id,type+id,...}',
  `notice_app_id` BIGINT COMMENT '催缴app信息模板的id',
  `notice_msg_id` BIGINT COMMENT '催缴sms信息模板的id',
  `create_time` DATETIME COMMENT '创建时间',
  `create_uid` BIGINT COMMENT '创建账号id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_order_records`;


CREATE TABLE `eh_payment_order_records` (
  `id` BIGINT NOT NULL,
  `service_config_id` BIGINT NOT NULL COMMENT '业务服务类型,eh_payment_service.id',
  `payment_order_type` INTEGER NOT NULL COMMENT '支付系统单据类型,1-充值,2-提现,3-支付,4-退款',
  `order_type` VARCHAR(64) NOT NULL COMMENT '服务类型,填parking/rentalOrder等',
  `order_id` BIGINT NOT NULL COMMENT '业务订单编号',
  `payment_order_id` BIGINT NOT NULL COMMENT '支付系统支付单号',
  `order_commit_url` VARCHAR(1024) COMMENT '支付接口',
  `order_commit_token` VARCHAR(1024) COMMENT '支付token',
  `order_commit_nonce` VARCHAR(128) COMMENT '支付随机数',
  `order_commit_timestamp` BIGINT COMMENT '支付时间戳',
  `pay_info` TEXT COMMENT '微信公众号支付,扫码支付等支付信息',
  `create_time` DATETIME NOT NULL,
  `order_num` VARCHAR(255) COMMENT '订单编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_payment_order_id` (`payment_order_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_service_configs`;


CREATE TABLE `eh_payment_service_configs` (
  `id` BIGINT NOT NULL,
  `name` VARCHAR(256) COMMENT '服务名称',
  `order_type` VARCHAR(64) NOT NULL COMMENT '服务类型,填parking/rentalOrder等',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `resource_type` VARCHAR(64),
  `resource_id` BIGINT,
  `payment_split_rule_id` BIGINT,
  `payment_user_type` INTEGER NOT NULL COMMENT '1-普通会员,2-企业会员',
  `payment_user_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME,

	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_subtraction_items`;


CREATE TABLE `eh_payment_subtraction_items` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `subtraction_type` VARCHAR(255) COMMENT '减免费项类型，eh_payment_bill_items：费项（如：物业费），eh_payment_late_fine：减免滞纳金（如：物业费滞纳金）',
  `charging_item_id` BIGINT COMMENT '减免费项的id，存的都是charging_item_id，因为滞纳金是跟着费项走，所以可以通过subtraction_type类型，判断是否减免费项滞纳金',
  `charging_item_name` VARCHAR(255) COMMENT '减免费项名称',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='减免费项配置表';

DROP TABLE IF EXISTS `eh_payment_types`;


CREATE TABLE `eh_payment_types` (
  `id` BIGINT NOT NULL,
  `order_type` VARCHAR(64) COMMENT '服务类型,填parking/rentalOrder等',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `resource_type` VARCHAR(64),
  `resource_id` BIGINT,
  `payment_type` INTEGER COMMENT '支付方式',
  `payment_name` VARCHAR(128) COMMENT '支付方式名称',
  `payment_logo` VARCHAR(512) COMMENT '支付方式logo',
  `paymentParams` VARCHAR(512) COMMENT '支付方式附加信息',
  `create_time` DATETIME NOT NULL,
  `update_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 用途:为每个owner生成一个唯一的id,作为暴露给支付系统的用户id,用户创建会员
-- ownerType为普通用户/企业/商家等,ownerId填对应的owner编号
-- id为支付系统的会员bizUserId,paymentUserId为支付系统User表的id
DROP TABLE IF EXISTS `eh_payment_users`;


CREATE TABLE `eh_payment_users` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `payment_user_type` INTEGER NOT NULL COMMENT '1-普通会员,2-企业会员',
  `payment_user_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `bank_name` VARCHAR(512) COMMENT 'the name of bank where enterprise has a account',
  `bank_number` VARCHAR(128) COMMENT 'the number of bank where enterprise has a account',
  `bank_card_number` VARCHAR(128) COMMENT 'the card number of enterprise bank account',
  `enterprise_name` VARCHAR(512) COMMENT 'the name of enterprise',
  `enterprise_business_licence` VARCHAR(128) COMMENT 'the business licence number of enterprise',
  `enterprise_business_licence_uri` VARCHAR(128) COMMENT 'the image of business licence of enterprise',
  `enterprise_account_licence_uri` VARCHAR(128) COMMENT 'the image of account licence of enterprise',
  `legal_person_name` VARCHAR(512) COMMENT 'the real name of legal person in an enterprise',
  `legal_person_phone` VARCHAR(512) COMMENT 'the phone number of legal person in an enterprise',
  `legal_person_identity_type` VARCHAR(512) COMMENT 'the identity type of legal person in an enterprise',
  `legal_person_identity_number` VARCHAR(512) COMMENT 'the identity number of legal person in an enterprise',
  `legal_person_identity_front_uri` VARCHAR(1024) COMMENT 'the front side of identity image of legal person in an enterprise',
  `legal_person_identity_back_uri` VARCHAR(1024) COMMENT 'the back side identity image of legal person in an enterprise',
  `status` TINYINT COMMENT '0-inactive, 1-waiting for approval, 2-active',
  `creator_uid` BIGINT,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `settlement_type` INTEGER NOT NULL DEFAULT 7 COMMENT '0-DAILY, 7-WEEKLY',
  PRIMARY KEY (`id`),
  UNIQUE KEY `i_owner` (`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_payment_variables`;


CREATE TABLE `eh_payment_variables` (
  `id` BIGINT NOT NULL,
  `charging_standard_id` BIGINT,
  `charging_items_id` BIGINT,
  `name` VARCHAR(10),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `identifier` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='变量表';

-- record the withdraw orders
DROP TABLE IF EXISTS `eh_payment_withdraw_orders`;


CREATE TABLE `eh_payment_withdraw_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `order_no` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `payment_user_type` INTEGER NOT NULL COMMENT 'the account type to withdraw the monty: 1-普通会员,2-企业会员',
  `payment_user_id` BIGINT NOT NULL COMMENT 'the account in pay-system to withdraw the monty',
  `amount` DECIMAL(10,2) COMMENT 'the amount to withdraw',
  `status` TINYINT NOT NULL COMMENT '0-inactive, 1-waiting for confirm, 2-success, 3-failed',
  `callback_time` DATETIME,
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the user who withdraw the money',
  `operate_time` DATETIME COMMENT 'the time to withdraw the money',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_personal_center_settings`;


CREATE TABLE `eh_personal_center_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `name` VARCHAR(32) COMMENT '展示名称',
  `function_name` VARCHAR(32) COMMENT '功能名称',
  `region` TINYINT NOT NULL DEFAULT 0 COMMENT '个人中心展示区域',
  `group_type` TINYINT COMMENT '展示区域分组',
  `sort_num` INTEGER NOT NULL DEFAULT 0 COMMENT '展示顺序',
  `showable` TINYINT COMMENT '是否展示',
  `editable` TINYINT COMMENT '是否可编辑',
  `type` INTEGER NOT NULL COMMENT '功能所属类型',
  `icon_uri` VARCHAR(1024) COMMENT '图标URI',
  `version` INTEGER NOT NULL DEFAULT 0 COMMENT '版本号',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态',
  `create_uid` BIGINT COMMENT '创建人ID',
  `create_time` DATETIME COMMENT '创建时间',
  `update_uid` BIGINT COMMENT '修改人ID',
  `update_time` DATETIME COMMENT '修改时间',
  `link_url` VARCHAR(1024) COMMENT '跳转链接',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='个人中心配置表';

DROP TABLE IF EXISTS `eh_phone_white_list`;


CREATE TABLE `eh_phone_white_list` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `phone_number` VARCHAR(128) NOT NULL COMMENT '白名单手机号码',
  `creator_uid` BIGINT COMMENT '记录创建人userID',
  `create_time` DATETIME COMMENT '记录创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='短信白名单';

DROP TABLE IF EXISTS `eh_pm_notify_configurations`;


CREATE TABLE `eh_pm_notify_configurations` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasks/EhQualityInspectionTasks...',
  `scope_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all; 1: namespace; 2: community',
  `scope_id` BIGINT NOT NULL,
  `notify_type` TINYINT NOT NULL COMMENT '0: before start; 1: before delay; 2: after delay',
  `notify_mode` TINYINT NOT NULL COMMENT '0:message; 1:sms',
  `repeat_type` TINYINT COMMENT '0: once; 1: repeat',
  `repeat_id` BIGINT,
  `receiver_json` TEXT COMMENT 'notify receivers:{"receivers":[{"receiverType":"3","receiverId":["1","2"]},{"receiverType":"0","receiverId":[]}]}',
  `notify_tick_minutes` INTEGER COMMENT '提前多少分钟',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `create_time` DATETIME NOT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_pm_notify_logs`;


CREATE TABLE `eh_pm_notify_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasks/EhQualityInspectionTasks...',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to object id',
  `notify_text` TEXT,
  `receiver_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_pm_notify_records`;


CREATE TABLE `eh_pm_notify_records` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasks/EhQualityInspectionTasks...',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to object id',
  `notify_type` TINYINT NOT NULL COMMENT '0: before start; 1: before delay; 2: after delay',
  `notify_mode` TINYINT NOT NULL COMMENT '0:message; 1:sms',
  `notify_time` DATETIME NOT NULL,
  `receiver_json` TEXT COMMENT 'notify receivers:{"receivers":[{"receiverType":"3","receiverId":["1","2"]},{"receiverType":"0","receiverId":[]}]}',
  `create_time` DATETIME NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: waiting for send out, 2: already sended',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 资产管理V2.8
DROP TABLE IF EXISTS `eh_pm_resoucre_reservations`;


CREATE TABLE `eh_pm_resoucre_reservations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_customer_id` BIGINT COMMENT 'primary id of eh_enterprise_customer',
  `address_id` BIGINT COMMENT 'id of eh_addresses',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'start time of this reservation',
  `end_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT 'end time of this reservation',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '1. inactive; 2: active; 3: deleted;',
  `previous_living_status` TINYINT COMMENT 'previous living status in eh_organization_address_mapping',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `community_id` BIGINT COMMENT 'id of community',
  `entry_id` BIGINT COMMENT '其他入口的备用字段',
  `creator_uid` BIGINT COMMENT '创建者id，可以为空',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '事件发生时间',
  `update_time` DATETIME COMMENT '事件update时间',
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理预约编码表';

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

DROP TABLE IF EXISTS `eh_pm_task_configs`;


CREATE TABLE `eh_pm_task_configs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id',
  `payment_flag` TINYINT DEFAULT 0 COMMENT '0: inactive, 1: active',
  `payment_account` BIGINT COMMENT '收款方账号',
  `payment_account_type` TINYINT COMMENT '收款方类型',
  `content_hint` VARCHAR(64) DEFAULT '请描述服务具体内容' COMMENT '服务内容提示文本',
  `creator_id` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `updater_id` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME,
  `task_category_id` BIGINT DEFAULT '6' COMMENT '应用类型：6为物业报修（1为正中会报修），9为投诉建议',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='物业报修通用配置表';

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

DROP TABLE IF EXISTS `eh_pm_task_order_details`;


CREATE TABLE `eh_pm_task_order_details` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(32),
  `task_id` BIGINT NOT NULL COMMENT '报修单Id',
  `order_id` BIGINT NOT NULL COMMENT '资源预订订单id',
  `product_name` VARCHAR(60) COMMENT '产品名称',
  `product_amount` INTEGER COMMENT '产品数量',
  `product_price` DECIMAL(16,0) COMMENT '产品单价',
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='物业报修订单明细表';

DROP TABLE IF EXISTS `eh_pm_task_orders`;


CREATE TABLE `eh_pm_task_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `task_id` BIGINT NOT NULL COMMENT '报修单Id',
  `biz_order_num` VARCHAR(64) COMMENT '处理过的资源预订订单号',
  `pay_order_id` BIGINT COMMENT '支付系统订单号',
  `payment_order_type` tinyint(8) COMMENT '订单类型 1续费订单 2欠费订单 3支付订单 4退款订单',
  `status` tinyint(8) COMMENT '订单状态0未支付 1已支付',
  `amount` DECIMAL(16,0) COMMENT '订单金额',
  `service_fee` DECIMAL(16,0) COMMENT '服务费',
  `product_fee` DECIMAL(16,0) COMMENT '产品费',
  `account_id` BIGINT COMMENT '收款方账号',
  `order_commit_url` VARCHAR(1024),
  `order_commit_token` VARCHAR(1024),
  `order_commit_nonce` VARCHAR(128),
  `order_commit_timestamp` BIGINT,
  `pay_info` TEXT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `general_order_id` VARCHAR(64) COMMENT '统一订单系统订单id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='物业报修订单表';

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
  `star` VARCHAR(4) COMMENT 'evaluate score',
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
  `remark_source` VARCHAR(32),
  `remark` VARCHAR(1024),
  `organization_name` VARCHAR(128) COMMENT '报修的任务的公司名称',
  `if_use_feelist` TINYINT DEFAULT 0 COMMENT '是否使用费用清单 0不使用 1 使用',
  `refer_type` VARCHAR(32) COMMENT '引用类型',
  `refer_id` BIGINT COMMENT '引用id',
  `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT '需求人公司Id',
  `amount` DECIMAL(16,0) NOT NULL DEFAULT 0 COMMENT '订单费用',
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

DROP TABLE IF EXISTS `eh_point_actions`;


CREATE TABLE `eh_point_actions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `action_type` VARCHAR(64),
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `display_name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(64) NOT NULL,
  `content` TEXT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_banners`;
CREATE TABLE `eh_point_banners` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT,
  `name` VARCHAR(128),
  `poster_uri` VARCHAR(128),
  `action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `creator_uid` BIGINT,
  `create_time` datetime(3),
  `update_uid` BIGINT,
  `update_time` datetime(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_event_logs`;


CREATE TABLE `eh_point_event_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rule_categories id',
  `event_name` VARCHAR(128),
  `subscription_path` VARCHAR(128),
  `event_json` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: waiting for process, 2: processing, 3: processed',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  PRIMARY KEY (`id`),
  KEY `i_category_id_status` (`category_id`,`status`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_goods`;


CREATE TABLE `eh_point_goods` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `number` VARCHAR(32) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `top_status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `top_time` datetime(3),
  `create_time` datetime(3),
  `creator_uid` BIGINT,
  `update_time` datetime(3),
  `update_uid` BIGINT,
  `shop_number` VARCHAR(128),
  `system_id` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_logs`;


CREATE TABLE `eh_point_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rule_categories id',
  `category_name` VARCHAR(32) NOT NULL DEFAULT '',
  `rule_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rules id',
  `rule_name` VARCHAR(32) NOT NULL DEFAULT '',
  `event_name` VARCHAR(128) NOT NULL DEFAULT '',
  `entity_type` VARCHAR(64) NOT NULL DEFAULT '',
  `entity_id` BIGINT NOT NULL DEFAULT 0,
  `arithmetic_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: plus, 2: minus',
  `points` BIGINT NOT NULL DEFAULT 0,
  `target_uid` BIGINT NOT NULL DEFAULT 0,
  `target_name` VARCHAR(32) NOT NULL DEFAULT '',
  `target_phone` VARCHAR(32) NOT NULL DEFAULT '',
  `operator_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: system, 2: manually',
  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_name` VARCHAR(32) NOT NULL DEFAULT '',
  `operator_phone` VARCHAR(32) NOT NULL DEFAULT '',
  `description` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` datetime(3),
  `event_happen_time` BIGINT,
  `extra` TEXT,
  `binding_log_id` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_rule_categories`;


CREATE TABLE `eh_point_rule_categories` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `display_name` VARCHAR(32) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `server_id` VARCHAR(128) NOT NULL DEFAULT 'default',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_rule_configs`;


CREATE TABLE `eh_point_rule_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rule_categories id',
  `rule_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rules id',
  `description` VARCHAR(64) NOT NULL,
  `points` BIGINT NOT NULL DEFAULT 0,
  `limit_type` TEXT,
  `limit_data` TEXT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_rule_to_event_mappings`;


CREATE TABLE `eh_point_rule_to_event_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rule_categories id',
  `rule_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rules id',
  `event_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'event name',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_rules`;


CREATE TABLE `eh_point_rules` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rule_categories id',
  `module_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_service_modules id',
  `display_name` VARCHAR(64) NOT NULL,
  `description` VARCHAR(64) NOT NULL,
  `arithmetic_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: add, 2: subtract',
  `points` BIGINT NOT NULL DEFAULT 0,
  `limit_type` TEXT,
  `limit_data` TEXT,
  `binding_rule_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'binding rule id',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `display_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: hidden, 1: display',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  `extra` text,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_scores`;


CREATE TABLE `eh_point_scores` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `user_id` BIGINT NOT NULL DEFAULT 0,
  `score` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_systems`;


CREATE TABLE `eh_point_systems` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `display_name` VARCHAR(32) NOT NULL,
  `point_name` VARCHAR(32) NOT NULL,
  `point_exchange_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'point exchange cash flag, 0: disable, 1: enabled',
  `exchange_point` INTEGER NOT NULL DEFAULT 0 COMMENT 'point exchange cash rate',
  `exchange_cash` INTEGER NOT NULL DEFAULT 0 COMMENT 'point exchange cash rate',
  `user_agreement` TEXT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: disabled, 2: enabled',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_tutorial_to_point_rule_mappings`;


CREATE TABLE `eh_point_tutorial_to_point_rule_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `tutorial_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_tutorials id',
  `rule_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_rules id',
  `description` VARCHAR(128),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_point_tutorials`;


CREATE TABLE `eh_point_tutorials` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `system_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_point_systems id',
  `display_name` VARCHAR(32) NOT NULL DEFAULT '',
  `description` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'description',
  `poster_uri` VARCHAR(256) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `create_time` DATETIME(3),
  `creator_uid` BIGINT,
  `update_time` DATETIME(3),
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_policies`;


CREATE TABLE `eh_policies` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT,
  `title` VARCHAR(64) NOT NULL DEFAULT '',
  `outline` VARCHAR(100) NOT NULL DEFAULT '',
  `content` TEXT COMMENT 'content data',
  `priority` BIGINT NOT NULL DEFAULT 0 COMMENT 'the rank of policy',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `updater_uid` BIGINT NOT NULL DEFAULT 0,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_policy_agent_rules`;


CREATE TABLE `eh_policy_agent_rules` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization',
  `owner_id` BIGINT COMMENT 'community id or organization id',
  `agent_flag` TINYINT COMMENT '是否代办:0为不可代办，1为可代办',
  `agent_phone` VARCHAR(64) COMMENT '联系方式',
  `agent_info` TEXT COMMENT '代办介绍',
  `creator_id` BIGINT NOT NULL COMMENT '创建人',
  `create_time` DATETIME COMMENT '创建时间',
  `updater_uid` BIGINT COMMENT '修改人',
  `update_time` DATETIME COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_policy_categories`;


CREATE TABLE `eh_policy_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `policy_id` BIGINT NOT NULL COMMENT 'id of the policy',
  `category_id` BIGINT NOT NULL COMMENT 'category of policy',
  `active_flag` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_policy_records`;


CREATE TABLE `eh_policy_records` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT,
  `creator_id` BIGINT NOT NULL,
  `creator_name` VARCHAR(128) NOT NULL,
  `creator_phone` VARCHAR(100) NOT NULL DEFAULT '',
  `creator_org_id` BIGINT NOT NULL,
  `creator_org_name` VARCHAR(128) NOT NULL,
  `turnover` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '营业额',
  `tax` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '纳税总额',
  `qualification` VARCHAR(60) NOT NULL DEFAULT '' COMMENT '单位资质',
  `financing` VARCHAR(60) NOT NULL DEFAULT '' COMMENT 'A轮融资',
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
  KEY `i_eh_poll_vote_poll` (`poll_id`),
  KEY `i_eh_poll_vote_create_time` (`create_time`),
  KEY `i_eh_poll_vote_voter` (`poll_id`,`item_id`,`voter_uid`) USING BTREE

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
  `tag` VARCHAR(32),
  `repeat_flag` TINYINT COMMENT 'is support repeat poll. 0-no, 1-yes',
  `repeat_period` INTEGER COMMENT 'repeat_period,  day',
  `wechat_poll` TINYINT DEFAULT 0 COMMENT 'is support wechat poll 0:no, 1:yes',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid`(`uuid`),
  KEY `i_eh_poll_start_time_ms`(`start_time_ms`),
  KEY `i_eh_poll_end_time_ms`(`end_time_ms`),
  KEY `i_eh_poll_creator_uid`(`creator_uid`),
  KEY `i_eh_poll_post_id`(`post_id`),
  KEY `i_eh_poll_create_time`(`create_time`),
  KEY `i_eh_poll_delete_time`(`delete_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_content_scopes`;


CREATE TABLE `eh_portal_content_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `scope_type` VARCHAR(64),
  `scope_id` BIGINT NOT NULL,
  `content_type` VARCHAR(64),
  `content_id` BIGINT NOT NULL,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `version_id` BIGINT,
  PRIMARY KEY (`id`),
  KEY `i_eh_content_id_index` (`content_id`),
  KEY `i_eh_version_id_index` (`version_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_item_categories`;


CREATE TABLE `eh_portal_item_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER,
  `version_id` BIGINT,
  `item_group_id` BIGINT NOT NULL,
  `name` VARCHAR(64) NOT NULL,
  `label` VARCHAR(64) NOT NULL COMMENT 'item categry label',
  `icon_uri` VARCHAR(1024) COMMENT 'service categry icon uri',
  `default_order` INTEGER COMMENT 'order ',
  `align` VARCHAR(64) COMMENT 'left, center',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_item_group_id_index` (`item_group_id`),
  KEY `i_eh_version_id_index` (`version_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_item_groups`;


CREATE TABLE `eh_portal_item_groups` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `version_id` BIGINT,
  `layout_id` BIGINT NOT NULL,
  `label` VARCHAR(64),
  `name` VARCHAR(64) COMMENT 'item_group_${id}，对应eh_launch_pad_layouts里面的layout_json里面item_group的 groups[x].instanceConfig.itemGroup 和 eh_launch_pad_items里的item_group',
  `separator_flag` TINYINT DEFAULT 0,
  `separator_height` DECIMAL(10,2),
  `widget` VARCHAR(64),
  `content_type` VARCHAR(64),
  `style` VARCHAR(64),
  `instance_config` TEXT COMMENT '参数配置',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `description` VARCHAR(1024),
  PRIMARY KEY (`id`),
  KEY `i_eh_layout_id_index` (`layout_id`),
  KEY `i_eh_version_id_index` (`version_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_items`;


CREATE TABLE `eh_portal_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `version_id` BIGINT,
  `item_group_id` BIGINT NOT NULL,
  `label` VARCHAR(64),
  `item_location` VARCHAR(2048) COMMENT 'eh_portal_layouts的item_location 对应eh_launch_pad_items里的item_location',
  `group_name` VARCHAR(64) COMMENT 'eh_portal_item_groups  name 对应eh_launch_pad_layouts里面的layout_json里面item_group的 groups[x].instanceConfig.itemGroup 和 eh_launch_pad_items里的item_group',
  `name` VARCHAR(64) COMMENT 'item_${id}',
  `icon_uri` VARCHAR(1024),
  `item_width` INTEGER NOT NULL DEFAULT 1,
  `item_height` INTEGER NOT NULL DEFAULT 1,
  `bgcolor` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0,
  `action_type` VARCHAR(64),
  `action_data` TEXT,
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'default display on the pad, 0: hide, 1:display',
  `selected_icon_uri` VARCHAR(1024),
  `more_order` INTEGER NOT NULL DEFAULT 0,
  `target_type` VARCHAR(32),
  `target_id` VARCHAR(64) COMMENT 'the entity id linked back to the orginal resource',
  `item_category_id` BIGINT,
  `description` VARCHAR(1024),
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `i_eh_item_group_id_index` (`item_group_id`),
  KEY `i_eh_item_category_id_index` (`item_category_id`),
  KEY `i_eh_version_id_index` (`version_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_launch_pad_mappings`;


CREATE TABLE `eh_portal_launch_pad_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `content_type` VARCHAR(64),
  `portal_content_id` BIGINT NOT NULL,
  `launch_pad_content_id` BIGINT NOT NULL,
  `create_time` DATETIME,
  `creator_uid` BIGINT NOT NULL,
  `version_id` BIGINT,
  PRIMARY KEY (`id`),
  KEY `i_eh_portal_content_id_index` (`portal_content_id`),
  KEY `i_eh_version_id_index` (`version_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_layout_templates`;


CREATE TABLE `eh_portal_layout_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `label` VARCHAR(64),
  `template_json` TEXT COMMENT '模板json',
  `show_uri` VARCHAR(64),
  `status` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `description` VARCHAR(1024),
  `type` TINYINT COMMENT '1-渐变导航栏（服务广场），2-自定义门户（二级门户），3-分页签门户（社群Tab）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_layouts`;


CREATE TABLE `eh_portal_layouts` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `version_id` BIGINT,
  `label` VARCHAR(64),
  `location` VARCHAR(2048) COMMENT '用于item_location查询eh_launch_pad_items',
  `name` VARCHAR(64) COMMENT 'layout_${id}，eh_launch_pad_layouts里面的name',
  `status` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `description` VARCHAR(1024),
  `type` TINYINT COMMENT '1-渐变导航栏（服务广场），2-自定义门户（二级门户），3-分页签门户（社群Tab）',
  `index_flag` TINYINT COMMENT 'index flag, 0-no, 1-yes',
  PRIMARY KEY (`id`),
  KEY `i_eh_version_id_index` (`version_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_navigation_bars`;


CREATE TABLE `eh_portal_navigation_bars` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `label` VARCHAR(64),
  `target_type` VARCHAR(64) NOT NULL,
  `target_id` BIGINT NOT NULL,
  `icon_uri` VARCHAR(1024),
  `selected_icon_uri` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `description` VARCHAR(1024),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_publish_logs`;


CREATE TABLE `eh_portal_publish_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `version_id` BIGINT,
  `process` INTEGER,
  `content_type` VARCHAR(64),
  `content_data` TEXT,
  `status` TINYINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_version_users`;


CREATE TABLE `eh_portal_version_users` (
  `id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `version_id` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `u_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_portal_versions`;


CREATE TABLE `eh_portal_versions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `parent_id` BIGINT,
  `date_version` INTEGER,
  `big_version` INTEGER,
  `minor_version` INTEGER,
  `create_time` DATETIME,
  `sync_time` DATETIME,
  `publish_time` DATETIME,
  `status` TINYINT COMMENT '0-init,1-edit,2-publis success, 3-publish fail',
  `preview_count` INTEGER DEFAULT 0 COMMENT '预览版本发布次数',
  PRIMARY KEY (`id`),
  KEY `namespace_id_index` (`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `content` LONGTEXT,
  `content_type` VARCHAR(128) COMMENT 'content type',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_day_log_files`;


CREATE TABLE `eh_punch_day_log_files` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT COMMENT 'user''s id',
  `detail_id` BIGINT COMMENT '员工 的detail Id',
  `enterprise_id` BIGINT COMMENT 'compay id',
  `dept_id` BIGINT COMMENT '所属部门Id',
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
  `status_list` VARCHAR(120) COMMENT '多次打卡的状态用/分隔 example: 1 ; 1/13 ; 13/3/4 ',
  `punch_count` INTEGER COMMENT '打卡次数',
  `punch_organization_id` BIGINT,
  `rule_type` TINYINT DEFAULT 0 COMMENT '0- 排班制 ; 1- 固定班次',
  `time_rule_name` VARCHAR(64) COMMENT '排班规则名称',
  `time_rule_id` BIGINT COMMENT '排班规则id',
  `approval_status_list` VARCHAR(120) COMMENT '1-未审批 0-审批正常 例如:0/1;1/1/0/1',
  `smart_alignment` VARCHAR(128) COMMENT '智能校准状态:1-未智能校准 0-未校准 例如:0;1/0;1/1/0/1',
  `month_report_id` BIGINT COMMENT 'eh_punch_month_reports id',
  `filer_uid` BIGINT COMMENT '创建者',
  `file_time` DATETIME,
  `filer_Name` VARCHAR(128) COMMENT '归档者姓名',
  `belate_time_total` BIGINT NOT NULL DEFAULT 0 COMMENT '当天迟到时长总计，单位毫秒',
  `leave_early_time_total` BIGINT NOT NULL DEFAULT 0 COMMENT '当天早退时长总计，单位毫秒',
  `overtime_total_workday` BIGINT NOT NULL DEFAULT 0 COMMENT '工作日加班时长，单位毫秒数',
  `overtime_total_restday` BIGINT NOT NULL DEFAULT 0 COMMENT '休息日加班时长，单位毫秒数',
  `overtime_total_legal_holiday` BIGINT NOT NULL DEFAULT 0 COMMENT '节假日加班时长，单位毫秒数',
  `rest_flag` TINYINT COMMENT '是否是休息日，1：是 0：否',
  `absent_flag` TINYINT COMMENT '是否全天旷工，1：是 0：否',
  `normal_flag` TINYINT COMMENT '全天是否出勤正常，1：是 0：否',
  `belate_count` INTEGER COMMENT '当天迟到次数',
  `leave_early_count` INTEGER COMMENT '早退次数',
  `forgot_punch_count_on_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '上班缺卡次数',
  `forgot_punch_count_off_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '下班缺卡次数',
  `ask_for_leave_request_count` INTEGER COMMENT '当天请假申请次数',
  `go_out_request_count` INTEGER COMMENT '当天外出申请次数',
  `business_trip_request_count` INTEGER COMMENT '当天出差申请次数',
  `overtime_request_count` INTEGER COMMENT '当天加班申请次数',
  `punch_exception_request_count` INTEGER COMMENT '当天异常打卡申请次数',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`),
  KEY `i_eh_enterprise_user_punch_date` (`enterprise_id`,`user_id`,`punch_date`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='打卡日统计的归档表';

DROP TABLE IF EXISTS `eh_punch_day_logs`;


CREATE TABLE `eh_punch_day_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT COMMENT 'user''s id',
  `detail_id` BIGINT COMMENT '员工 的detail Id',
  `enterprise_id` BIGINT COMMENT 'compay id',
  `dept_id` BIGINT COMMENT '所属部门Id',
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
  `status_list` VARCHAR(120) COMMENT '多次打卡的状态用/分隔 example: 1 ; 1/13 ; 13/3/4 ',
  `punch_count` INTEGER COMMENT '打卡次数',
  `punch_organization_id` BIGINT,
  `rule_type` TINYINT DEFAULT 0 COMMENT '0- 排班制 ; 1- 固定班次',
  `time_rule_name` VARCHAR(64) COMMENT '排班规则名称',
  `time_rule_id` BIGINT COMMENT '排班规则id',
  `approval_status_list` VARCHAR(120) COMMENT '1-未审批 0-审批正常 例如:0/1;1/1/0/1',
  `smart_alignment` VARCHAR(128) COMMENT '智能校准状态:1-未智能校准 0-未校准 例如:0;1/0;1/1/0/1',
  `belate_time_total` BIGINT NOT NULL DEFAULT 0 COMMENT '当天迟到时长总计，单位毫秒',
  `leave_early_time_total` BIGINT NOT NULL DEFAULT 0 COMMENT '当天早退时长总计，单位毫秒',
  `overtime_total_workday` BIGINT NOT NULL DEFAULT 0 COMMENT '工作日加班时长，单位毫秒数',
  `overtime_total_restday` BIGINT NOT NULL DEFAULT 0 COMMENT '休息日加班时长，单位毫秒数',
  `overtime_total_legal_holiday` BIGINT NOT NULL DEFAULT 0 COMMENT '节假日加班时长，单位毫秒数',
  `rest_flag` TINYINT COMMENT '是否是休息日，1：是 0：否',
  `absent_flag` TINYINT COMMENT '是否全天旷工，1：是 0：否',
  `normal_flag` TINYINT COMMENT '全天是否出勤正常，1：是 0：否',
  `belate_count` INTEGER COMMENT '当天迟到次数',
  `leave_early_count` INTEGER COMMENT '早退次数',
  `forgot_punch_count_on_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '上班缺卡次数',
  `forgot_punch_count_off_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '下班缺卡次数',
  `ask_for_leave_request_count` INTEGER COMMENT '当天请假申请次数',
  `go_out_request_count` INTEGER COMMENT '当天外出申请次数',
  `business_trip_request_count` INTEGER COMMENT '当天出差申请次数',
  `overtime_request_count` INTEGER COMMENT '当天加班申请次数',
  `punch_exception_request_count` INTEGER COMMENT '当天异常打卡申请次数',
  `split_date_time` DATETIME COMMENT '当天考勤时间的分界点',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`),
  KEY `i_eh_enterprise_punch_date_user_id` (`enterprise_id`,`punch_date`,`user_id`),
  KEY `i_eh_enterprise_punch_date_detail_id` (`enterprise_id`,`punch_date`,`detail_id`)
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
  `approval_status_list` VARCHAR(120) COMMENT '1-未审批 0-审批正常 例如:0/1;1/1/0/1',
  `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡',
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
  `view_flag` TINYINT DEFAULT 1 COMMENT 'is view(0) not view(1)',
  `request_id` BIGINT COMMENT 'approval request id',
  `punch_interval_no` INTEGER DEFAULT 1 COMMENT '第几次排班的打卡',
  `punch_type` TINYINT DEFAULT 2 COMMENT ' 0- 上班打卡 ; 1- 下班打卡',
  `begin_time` DATETIME COMMENT ' 请假/加班 生效开始时间',
  `end_time` DATETIME COMMENT ' 请假/加班 生效结束时间',
  `duration_day` decimal(10,4) DEFAULT '0.0000' COMMENT '申请时长-单位天',
  `category_id` BIGINT COMMENT ' 请假类型',
  `approval_attribute` VARCHAR(128) COMMENT 'DEFAULT,CUSTOMIZE',
  `duration_minute` BIGINT DEFAULT 0 COMMENT '申请时长-单位分钟',
  PRIMARY KEY (`id`),
  KEY `i_eh_enterprise_user_punch_date` (`enterprise_id`,`user_id`,`punch_date`)
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
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `exchange_from_date` DATE COMMENT '特殊上班日:上原本哪天的班次',
  `legal_flag` TINYINT DEFAULT 0 COMMENT '是否法定假日:1-是 0-否',
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

DROP TABLE IF EXISTS `eh_punch_log_files`;


CREATE TABLE `eh_punch_log_files` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT COMMENT 'user''s id',
  `detail_id` BIGINT COMMENT '员工 的detail Id',
  `enterprise_id` BIGINT COMMENT 'compay id',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `punch_date` DATE COMMENT 'user punch date',
  `punch_time` DATETIME COMMENT 'user check time',
  `punch_status` TINYINT COMMENT '1:Normal, 0:Not in punch area',
  `identification` VARCHAR(255) COMMENT 'unique identification for a phone',
  `punch_type` TINYINT DEFAULT 0 COMMENT '0- 上班打卡 ; 1- 下班打卡',
  `punch_interval_no` INTEGER DEFAULT 1 COMMENT '第几次排班的打卡',
  `rule_time` BIGINT COMMENT '规则设置的该次打卡时间',
  `status` TINYINT COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡',
  `approval_status` TINYINT COMMENT '校正后的打卡状态 0-正常 null-没有异常校准',
  `smart_alignment` TINYINT DEFAULT 0 COMMENT '只能校准状态 0-非校准 1-校准',
  `wifi_info` VARCHAR(1024) COMMENT '打卡用到的WiFi信息',
  `location_info` VARCHAR(1024) COMMENT '打卡用到的地址定位',
  `should_punch_time` BIGINT COMMENT '应该打卡时间(用以计算早退迟到时长)',
  `month_report_id` BIGINT COMMENT 'eh_punch_month_reports id',
  `filer_uid` BIGINT COMMENT '创建者',
  `file_time` DATETIME,
  `filer_Name` VARCHAR(128) COMMENT '归档者姓名',
  `device_change_flag` TINYINT DEFAULT 0 COMMENT '0- unchange 1-changed',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='打卡的归档表';

DROP TABLE IF EXISTS `eh_punch_logs`;


CREATE TABLE `eh_punch_logs` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT COMMENT 'user''s id',
  `detail_id` BIGINT COMMENT '员工 的detail Id',
  `enterprise_id` BIGINT COMMENT 'compay id',
  `longitude` DOUBLE,
  `latitude` DOUBLE,
  `punch_date` DATE COMMENT 'user punch date',
  `punch_organization_id` BIGINT,
  `punch_time` DATETIME COMMENT 'user check time',
  `punch_status` TINYINT COMMENT '1:Normal, 0:Not in punch area',
  `identification` VARCHAR(255) COMMENT 'unique identification for a phone',
  `punch_type` TINYINT DEFAULT 0 COMMENT '0- 上班打卡 ; 1- 下班打卡',
  `punch_interval_no` INTEGER DEFAULT 1 COMMENT '第几次排班的打卡',
  `rule_time` BIGINT COMMENT '规则设置的该次打卡时间',
  `status` TINYINT COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡',
  `approval_status` TINYINT COMMENT '校正后的打卡状态 0-正常 null-没有异常校准',
  `smart_alignment` TINYINT DEFAULT 0 COMMENT '只能校准状态 0-非校准 1-校准',
  `wifi_info` VARCHAR(1024) COMMENT '打卡用到的WiFi信息',
  `location_info` VARCHAR(1024) COMMENT '打卡用到的地址定位',
  `should_punch_time` BIGINT COMMENT '应该打卡时间(用以计算早退迟到时长)',
  `device_change_flag` TINYINT DEFAULT 0 COMMENT '0- unchange 1-changed',
  `update_date` DATE,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_month_reports`;


CREATE TABLE `eh_punch_month_reports` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `punch_month` VARCHAR(8) COMMENT 'yyyymm',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `process` INTEGER COMMENT '进度百分比',
  `error_info` VARCHAR(200) COMMENT '错误信息(如果归档错误)',
  `status` TINYINT COMMENT '状态:0-创建更新中 1-创建完成 2-已归档',
  `creator_uid` BIGINT COMMENT '创建者',
  `create_time` DATETIME,
  `punch_member_number` INTEGER COMMENT '考勤人数',
  `filer_uid` BIGINT COMMENT '创建者',
  `file_time` DATETIME,
  `update_time` DATETIME,
  `creator_Name` VARCHAR(128) COMMENT '创建者姓名',
  `filer_Name` VARCHAR(128) COMMENT '归档者姓名',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='考勤月报表';

DROP TABLE IF EXISTS `eh_punch_notifications`;


CREATE TABLE `eh_punch_notifications` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `enterprise_id` BIGINT NOT NULL COMMENT '总公司id',
  `user_id` BIGINT NOT NULL COMMENT '被提醒人的uid',
  `detail_id` BIGINT NOT NULL COMMENT '被提醒人的detailId',
  `punch_rule_id` BIGINT NOT NULL COMMENT '所属考勤规则',
  `punch_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0- 上班打卡 ; 1- 下班打卡',
  `punch_interval_no` INTEGER DEFAULT 1 COMMENT '第几次排班的打卡',
  `punch_date` date NOT NULL COMMENT '打卡日期',
  `rule_time` DATETIME NOT NULL COMMENT '规则设置的该次打卡时间',
  `except_remind_time` DATETIME NOT NULL COMMENT '规则设置的打卡提醒时间',
  `act_remind_time` DATETIME COMMENT '实际提醒时间',
  `invalid_reason` VARCHAR(512) COMMENT '提醒记录失效的原因',
  `invalid_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0- 有效 ; 1- 无效',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_enterprise_detail_id` (`namespace_id`,`enterprise_id`,`detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='打卡提醒队列，该数据只保留一天';

DROP TABLE IF EXISTS `eh_punch_operation_logs`;


CREATE TABLE `eh_punch_operation_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the report template',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_name` VARCHAR(64) COMMENT 'the module type',
  `rule_id` BIGINT COMMENT 'the module id',
  `rule_name` VARCHAR(64) COMMENT 'the module type',
  `operate_api` VARCHAR(128),
  `request_parameter` TEXT,
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_overtime_rules`;


CREATE TABLE `eh_punch_overtime_rules` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `punch_rule_id` BIGINT NOT NULL COMMENT '考勤规则Id',
  `type` VARCHAR(24) NOT NULL DEFAULT 'WORKDAY' COMMENT 'WORKDAY:工作日加班，HOLIDAY:非工作日加班',
  `name` VARCHAR(64) NOT NULL COMMENT '加班类型名称',
  `overtime_approval_type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型：1、需审批，时长以打卡为准，但不能超过审批单时长；2、 需审批，时长以审批单为准；3、 无需审批，时长以打卡为准',
  `valid_interval` INTEGER NOT NULL DEFAULT 0 COMMENT '加班起算时间（有效间隔），单位分钟',
  `overtime_min_unit` INTEGER NOT NULL DEFAULT 0 COMMENT '最小加班单位（步长），单位分钟',
  `overtime_max` INTEGER NOT NULL DEFAULT 0 COMMENT '最大加班时长，单位分钟',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1-已删除 2-正常 3-次日更新 4-新规则次日生效',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY (`id`),
  KEY `i_eh_punch_rule_id` (`punch_rule_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='考勤加班规则';

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
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `rule_type` TINYINT DEFAULT 0 COMMENT '0- 排班制 ; 1- 固定班次',
  `punch_organization_id` BIGINT,
  `china_holiday_flag` TINYINT COMMENT '同步法定节假日0- no  ; 1- yes ',
  `punch_remind_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否开启上下班打卡提醒：1 开启 0 关闭',
  `remind_minutes_on_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '上班提前分钟数打卡提醒',
  `status` TINYINT DEFAULT 2 COMMENT ' 规则状态 1-已删除 2-正常 3-次日更新 4-新规则次日生效',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET=utf8mb4 ;

DROP TABLE IF EXISTS `eh_punch_rules_bak`;


CREATE TABLE `eh_punch_rules_bak` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `enterprise_id` BIGINT NOT NULL COMMENT 'rule company id',
  `start_early_time` TIME COMMENT 'how early can i arrive',
  `start_late_time` TIME COMMENT 'how late can i arrive ',
  `work_time` TIME COMMENT 'how long do i must be work',
  `noon_leave_time` TIME,
  `afternoon_arrive_time` TIME,
  `time_tag1` TIME,
  `time_tag2` TIME,
  `time_tag3` TIME,
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT '2 or  4 times',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` datetime
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `status` TINYINT DEFAULT 2 COMMENT ' 规则状态 1-已删除 2-正常 3-次日更新 4-新规则次日生效',
  PRIMARY KEY (`id`),
  KEY `i_eh_time_rule_id` (`time_rule_id`),
  KEY `i_eh_target_id` (`target_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_special_days`;


CREATE TABLE `eh_punch_special_days` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `punch_organization_id` BIGINT COMMENT 'fk:eh_punch_workday_rules id',
  `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT COMMENT 'eh_punch_time_rules id ',
  `status` TINYINT COMMENT 'its holiday or workday:0-workday ; 1-holiday',
  `rule_date` DATE COMMENT 'date',
  `description` TEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_statistic_files`;


CREATE TABLE `eh_punch_statistic_files` (
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
  `rest_day_count` INTEGER COMMENT '应休息天数',
  `full_normal_flag` TINYINT COMMENT '当月是否全勤',
  `ask_for_leave_request_count` INTEGER COMMENT '当月请假申请次数',
  `go_out_request_count` INTEGER COMMENT '当月外出申请次数',
  `business_trip_request_count` INTEGER COMMENT '当月出差申请次数',
  `overtime_request_count` INTEGER COMMENT '当月加班申请次数',
  `punch_exception_request_count` INTEGER COMMENT '当月异常打卡申请次数',
  `over_time_sum` BIGINT COMMENT '加班累计(非工作日上班)',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT 'how many times should punch everyday :2/4',
  `exception_status` TINYINT COMMENT '异常状态: 0-正常;1-异常',
  `description` VARCHAR(256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `exts` VARCHAR(1024) COMMENT 'json string exts:eq[{"name":"事假","timeCount":"1天2小时"},{"name":"丧假","timeCount":"3天2小时30分钟"}]',
  `user_status` TINYINT DEFAULT 0 COMMENT '0:normal普通 1:NONENTRY未入职 2:RESIGNED已离职',
  `punch_org_name` VARCHAR(64) COMMENT '所属规则-考勤组',
  `detail_id` BIGINT COMMENT '用户detailId',
  `exception_day_count` INTEGER COMMENT '异常天数',
  `annual_leave_balance` DOUBLE COMMENT '年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '调休余额',
  `device_change_counts` INTEGER COMMENT '设备异常次数',
  `exception_request_counts` INTEGER COMMENT '异常申报次数',
  `belate_time` BIGINT COMMENT '迟到时长(毫秒数)',
  `leave_early_time` BIGINT COMMENT '早退时长(毫秒数)',
  `forgot_punch_count_off_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '下班缺卡次数',
  `forgot_punch_count_on_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '上班缺卡次数',
  `status_list` TEXT COMMENT '校正后状态列表(月初到月末)',
  `month_report_id` BIGINT COMMENT 'eh_punch_month_reports id',
  `filer_uid` BIGINT COMMENT '创建者',
  `file_time` DATETIME,
  `filer_Name` VARCHAR(128) COMMENT '归档者姓名',
  `overtime_total_workday` BIGINT NOT NULL DEFAULT 0 COMMENT '工作日加班时长，单位毫秒数',
  `overtime_total_restday` BIGINT NOT NULL DEFAULT 0 COMMENT '休息日加班时长，单位毫秒数',
  `overtime_total_legal_holiday` BIGINT NOT NULL DEFAULT 0 COMMENT '节假日加班时长，单位毫秒数',
  PRIMARY KEY (`id`),
  KEY `i_eh_report_id` (`month_report_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='打卡月统计的归档表';

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
  `rest_day_count` INTEGER COMMENT '应休息天数',
  `full_normal_flag` TINYINT COMMENT '当月是否全勤',
  `ask_for_leave_request_count` INTEGER COMMENT '当月请假申请次数',
  `go_out_request_count` INTEGER COMMENT '当月外出申请次数',
  `business_trip_request_count` INTEGER COMMENT '当月出差申请次数',
  `overtime_request_count` INTEGER COMMENT '当月加班申请次数',
  `punch_exception_request_count` INTEGER COMMENT '当月异常打卡申请次数',
  `over_time_sum` BIGINT COMMENT '加班累计(非工作日上班)',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT 'how many times should punch everyday :2/4',
  `exception_status` TINYINT COMMENT '异常状态: 0-正常;1-异常',
  `description` VARCHAR(256),
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `exts` VARCHAR(1024) COMMENT 'json string exts:eq[{"name":"事假","timeCount":"1天2小时"},{"name":"丧假","timeCount":"3天2小时30分钟"}]',
  `user_status` TINYINT DEFAULT 0 COMMENT '0:normal普通 1:NONENTRY未入职 2:RESIGNED已离职',
  `punch_org_name` VARCHAR(64) COMMENT '所属规则-考勤组',
  `detail_id` BIGINT COMMENT '用户detailId',
  `exception_day_count` INTEGER COMMENT '异常天数',
  `annual_leave_balance` DOUBLE COMMENT '年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '调休余额',
  `device_change_counts` INTEGER COMMENT '设备异常次数',
  `exception_request_counts` INTEGER COMMENT '异常申报次数',
  `belate_time` BIGINT COMMENT '迟到时长(毫秒数)',
  `leave_early_time` BIGINT COMMENT '早退时长(毫秒数)',
  `forgot_punch_count_off_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '下班缺卡次数',
  `forgot_punch_count_on_duty` INTEGER NOT NULL DEFAULT 0 COMMENT '上班缺卡次数',
  `status_list` TEXT COMMENT '校正后状态列表(月初到月末)',
  `overtime_total_workday` BIGINT NOT NULL DEFAULT 0 COMMENT '工作日加班时长，单位毫秒数',
  `overtime_total_restday` BIGINT NOT NULL DEFAULT 0 COMMENT '休息日加班时长，单位毫秒数',
  `overtime_total_legal_holiday` BIGINT NOT NULL DEFAULT 0 COMMENT '节假日加班时长，单位毫秒数',
  PRIMARY KEY (`id`),
  KEY `i_eh_punch_month_detail_id` (`punch_month`,`owner_type`,`owner_id`,`detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_time_intervals`;


CREATE TABLE `eh_punch_time_intervals` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `owner_type` VARCHAR(128) COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT COMMENT 'owner resource(user/organization) id',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT 2 COMMENT 'how many times should punch everyday :2/4/6',
  `punch_organization_id` BIGINT COMMENT 'fk:eh_punch_workday_rules id',
  `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id  ',
  `time_rule_id` BIGINT COMMENT 'eh_punch_time_rules id  ',
  `arrive_time_long` BIGINT COMMENT ' arrive',
  `leave_time_long` BIGINT COMMENT 'leave',
  `description` TEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `rule_type` TINYINT DEFAULT 1 COMMENT '0- 排班制 ; 1- 固定班次',
  `hommization_type` TINYINT DEFAULT 0 COMMENT '人性化设置:0-无 1-弹性 2晚到晚走',
  `flex_time_long` BIGINT COMMENT '弹性时间 ',
  `begin_punch_time` BIGINT COMMENT '上班多久之前可以打卡',
  `end_punch_time` BIGINT COMMENT '下班多久之后可以打卡',
  `punch_organization_id` BIGINT COMMENT 'fk:eh_punch_workday_rules id',
  `punch_rule_id` BIGINT COMMENT 'eh_punch_rules id  ',
  `open_weekday` VARCHAR(7) COMMENT '7位二进制，0000000每一位表示星期7123456',
  `status` TINYINT DEFAULT 2 COMMENT ' 规则状态 1-已删除 2-正常 3-次日更新 4-新规则次日生效',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_punch_vacation_balance_logs`;


CREATE TABLE `eh_punch_vacation_balance_logs` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT COMMENT 'organization_id',
  `owner_type` VARCHAR(32) DEFAULT '' COMMENT 'organization',
  `user_id` BIGINT COMMENT 'user_id',
  `detail_id` BIGINT COMMENT 'user_id',
  `annual_leave_balance_correction` DOUBLE COMMENT '年假余额修改',
  `overtime_compensation_balance_correction` DOUBLE COMMENT '调休余额修改',
  `annual_leave_balance` DOUBLE COMMENT '修改后年假余额',
  `overtime_compensation_balance` DOUBLE COMMENT '修改后调休余额',
  `description` TEXT COMMENT '备注',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `namespace_id` INTEGER,
  PRIMARY KEY (`id`),
  KEY `ix_detail_id` (`detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='假期余额操作日志表';

DROP TABLE IF EXISTS `eh_punch_vacation_balances`;


CREATE TABLE `eh_punch_vacation_balances` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT COMMENT 'organization_id',
  `owner_type` VARCHAR(32) DEFAULT '' COMMENT 'organization',
  `user_id` BIGINT COMMENT 'user_id',
  `detail_id` BIGINT COMMENT 'user_id',
  `annual_leave_balance` DOUBLE COMMENT '年假余额',
  `annual_leave_history_count` DECIMAL(10,4) NOT NULL DEFAULT '0.0000' COMMENT '已请年假总和，单位天',
  `overtime_compensation_balance` DOUBLE COMMENT '调休余额',
  `overtime_compensation_history_count` DECIMAL(10,4) NOT NULL DEFAULT '0.0000' COMMENT '已请调休总和，单位天',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `namespace_id` INTEGER,
  PRIMARY KEY (`id`),
  KEY `ix_detail_id` (`detail_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='假期余额表';

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

DROP TABLE IF EXISTS `eh_push_message_log`;


CREATE TABLE `eh_push_message_log` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `content` TEXT COMMENT '推送内容',
  `push_type` int(2) COMMENT '推送方式（1表示应用消息推送，2表示短信推送）',
  `receiver_type` int(2) COMMENT '推送对象的类型（0表示所有人，1表示按项目，2表示按手机号）',
  `operator_id` INTEGER COMMENT '操作者',
  `create_time` DATETIME COMMENT '推送创建时间',
  `push_status` int(2) COMMENT '推送状态(1表示等待推送，2表示推送中，3表示推送完成)',
  `receivers` VARCHAR(600) COMMENT '推送对象(与推送对象类型对应，所有人为空，按项目为项目ID，按手机号为手机号)',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='一键推送消息(短信)记录表';

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
  `route_uri` VARCHAR(256) COMMENT 'route uri, like zl://xxx/xxx',
  `handler` VARCHAR(32) COMMENT 'module handler',
  `extra` TEXT COMMENT 'module handler',

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
  `scope_id` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_quality_inspection_model_community_map`;
CREATE TABLE `eh_quality_inspection_model_community_map` (
  `id` BIGINT NOT NULL,
  `model_id` BIGINT NOT NULL DEFAULT 0,
  `model_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0:standard',
  `target_type` VARCHAR(255),
  `target_id` BIGINT COMMENT 'community id ',
  `create_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_quality_inspection_sample_community_map`;


CREATE TABLE `eh_quality_inspection_sample_community_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `sample_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_sample',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_communities',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_sample_community_specification_stat`;


CREATE TABLE `eh_quality_inspection_sample_community_specification_stat` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `sample_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_sample',
  `community_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_communities',
  `specification_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_specifications',
  `deduct_score` DOUBLE NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `specification_path` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_sample_group_map`;


CREATE TABLE `eh_quality_inspection_sample_group_map` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `sample_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_sample',
  `organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
  `position_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organization_job_positions',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_sample_score_stat`;


CREATE TABLE `eh_quality_inspection_sample_score_stat` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `sample_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_sample',
  `community_count` INTEGER NOT NULL DEFAULT 0,
  `task_count` INTEGER NOT NULL DEFAULT 0,
  `correction_count` INTEGER NOT NULL DEFAULT 0,
  `deduct_score` DOUBLE NOT NULL DEFAULT 0,
  `highest_score` DOUBLE NOT NULL DEFAULT 0,
  `lowest_score` DOUBLE NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `correction_qualified_count` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_samples`;


CREATE TABLE `eh_quality_inspection_samples` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the template, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `sample_number` VARCHAR(128),
  `start_time` DATETIME NOT NULL,
  `end_time` DATETIME NOT NULL,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
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
  `manual_flag` BIGINT NOT NULL DEFAULT 0 COMMENT '0: auto 1:manual 2:sample',
  `sample_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_equipment_inspection_sample',
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
  `update_uid` BIGINT,
  `delete_uid` BIGINT,
  `delete_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,
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
  `refer_id` BIGINT,
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
  `anonymous_flag` TINYINT DEFAULT 0 COMMENT '是否匿名回答, 0:不是匿名回答,2:是匿名回答',
  `target_phone` VARCHAR(128) COMMENT '用户电话',
  `target_from` TINYINT COMMENT '用户来源（1:app，2:wx）',
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

-- 问卷调查范围表
DROP TABLE IF EXISTS  `eh_questionnaire_ranges`;

CREATE TABLE `eh_questionnaire_ranges` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`questionnaire_id` BIGINT NOT NULL COMMENT '关联问卷调查的id',
  `community_id` BIGINT COMMENT '园区id，查询楼栋（range_type=building）下的企业的时候，使用的是楼栋的名称查询，这里必须保存community一起查询才正确。',
  `range_type` VARCHAR(64) COMMENT 'community_all(项目),community_authenticated(项目下已认证的用户),community_unauthorized(未认证),building(楼栋),enterprise(企业),user 范围类型',
  `range` VARCHAR(512) COMMENT '对应项目id,楼栋名称，企业ID，用户id',
	`range_description` VARCHAR(1024) COMMENT '范围描述信息，用于显示在问卷详情页',
	`rid` BIGINT COMMENT '范围为building的时候，存buildingid，给web做逻辑，后端没有必要存储',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. draft, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,

  PRIMARY KEY (`id`)
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
  `cut_off_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '问卷截止日期',
  `target_user_num` INTEGER COMMENT '目标用户收集数量',
  `scope_resent_message_users` MEDIUMTEXT,
  `scope_sent_message_users` MEDIUMTEXT,
  `poster_uri` VARCHAR(1024) COMMENT '问卷调查的封面uri',
  `target_type` VARCHAR(32) DEFAULT 'organization' COMMENT '调查对象 organization:企业 user:个人',
  `support_anonymous` TINYINT COMMENT '是否支持匿名, 0:不支持匿名,2:支持匿名',
  `support_share` TINYINT COMMENT '是否支持分享, 0:不支持分享,2:支持分享',
  `user_scope` MEDIUMTEXT,
  `organization_scope` MEDIUMTEXT,
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

DROP TABLE IF EXISTS `eh_reflection_service_module_apps`;


CREATE TABLE `eh_reflection_service_module_apps` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `active_app_id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(64),
  `module_id` BIGINT,
  `instance_config` TEXT COMMENT '应用入口需要的配置参数',
  `status` TINYINT NOT NULL DEFAULT 0,
  `action_type` TINYINT,
  `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
  `update_time` DATETIME,
  `module_control_type` VARCHAR(64) DEFAULT '' COMMENT 'community_control;org_control;unlimit',
  `multiple_flag` TINYINT,
  `custom_tag` VARCHAR(64) DEFAULT '',
  `custom_path` VARCHAR(128) DEFAULT '',
  `menu_id` BIGINT DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_activeAppId` (`active_app_id`),
  UNIQUE KEY `index_active_union` (`namespace_id`,`module_id`,`custom_tag`)
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
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_region_name` (`namespace_id`,`parent_id`,`name`),
  KEY `i_eh_region_name_level` (`name`,`level`),
  KEY `i_eh_region_path` (`path`),
  KEY `i_eh_region_path_level` (`path`,`level`),
  KEY `i_eh_region_parent` (`parent_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_relocation_configs`;


CREATE TABLE `eh_relocation_configs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id',
  `agreement_flag` TINYINT DEFAULT 0 COMMENT '0: inactive, 1: active',
  `agreement_content` TEXT COMMENT '协议内容',
  `tips_flag` TINYINT DEFAULT 0 COMMENT '0: inactive, 1: active',
  `tips_content` TEXT COMMENT '提示内容',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_relocation_request_attachments`;


CREATE TABLE `eh_relocation_request_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `status` TINYINT COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_relocation_request_items`;


CREATE TABLE `eh_relocation_request_items` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id',
  `request_id` BIGINT NOT NULL COMMENT 'id of the relocation request record',
  `item_name` VARCHAR(64) COMMENT 'the name of item',
  `item_quantity` INTEGER DEFAULT 0 COMMENT 'the quantity of item',
  `status` TINYINT COMMENT '0: inactive, 1: , 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_relocation_requests`;


CREATE TABLE `eh_relocation_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id',
  `request_no` VARCHAR(128) NOT NULL,
  `requestor_enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of organization where the requestor is in',
  `requestor_enterprise_name` VARCHAR(64) COMMENT 'the enterprise name of requestor',
  `requestor_enterprise_address` VARCHAR(256) COMMENT 'the enterprise address of requestor',
  `requestor_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'requestor id',
  `requestor_name` VARCHAR(64) COMMENT 'the name of requestor',
  `contact_phone` VARCHAR(64) COMMENT 'the phone of requestor',
  `relocation_date` DATETIME NOT NULL,
  `status` TINYINT COMMENT '0: inactive, 1: processing, 2: completed',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `flow_case_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow case id',
  `cancel_time` DATETIME,
  `cancel_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'cancel user id',
  `qr_code_url` VARCHAR(256) COMMENT 'url of the qr record',
  `org_owner_type_id` BIGINT COMMENT '客户类型（小区场景）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_remind_categories`;


CREATE TABLE `eh_remind_categories` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `user_id` BIGINT NOT NULL COMMENT '分类拥有人的用户ID',
  `name` VARCHAR(64) NOT NULL COMMENT '日程分类的名称',
  `colour` VARCHAR(16) COMMENT '日程分类的颜色RGB的argb-hex值，如 #FFF58F3E',
  `share_short_display` VARCHAR(64) COMMENT '默认共享人概要信息：如 xx等3人',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_name` (`namespace_id`,`owner_type`,`owner_id`,`user_id`,`name`),
  KEY `i_eh_owner_user_id` (`namespace_id`,`owner_type`,`owner_id`,`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='日程分类表';

DROP TABLE IF EXISTS `eh_remind_category_default_shares`;


CREATE TABLE `eh_remind_category_default_shares` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `remind_category_id` BIGINT NOT NULL COMMENT '日程分类的ID,id of eh_remind_categories',
  `shared_source_type` VARCHAR(32) NOT NULL COMMENT '默认MEMBER_DETAIL',
  `shared_source_id` BIGINT NOT NULL COMMENT 'source_type对应的ID，员工档案ID',
  `shared_contract_name` VARCHAR(45) NOT NULL COMMENT '默认共享人的姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_remind_category_id` (`remind_category_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='日程分类默认共享人设置表';

DROP TABLE IF EXISTS `eh_remind_demo_create_logs`;


CREATE TABLE `eh_remind_demo_create_logs` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `user_id` BIGINT NOT NULL COMMENT '日程所有人的用户ID',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_user_id` (`namespace_id`,`owner_type`,`owner_id`,`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='日程案例创建记录，避免案例重复创建';

DROP TABLE IF EXISTS `eh_remind_settings`;


CREATE TABLE `eh_remind_settings` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `name` VARCHAR(64) NOT NULL COMMENT '名称，如 提前一天（09:00）',
  `offset_day` TINYINT NOT NULL DEFAULT 0 COMMENT '提前几天',
  `fix_time` TIME COMMENT '提醒的固定时间，格式:09:00:00',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  `app_version` VARCHAR(32) DEFAULT '5.8.0' COMMENT '对应app版本(历史数据5.8.0),根据APP版本选择性展示',
  `before_time` BIGINT COMMENT '提前多少时间(毫秒数)不超过1天的部分在这里减',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_name` (`name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='日程提醒时间设置表';

DROP TABLE IF EXISTS `eh_remind_shares`;


CREATE TABLE `eh_remind_shares` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `remind_id` BIGINT NOT NULL COMMENT '日程ID',
  `owner_user_id` BIGINT NOT NULL COMMENT '分享人的userId',
  `owner_contract_name` VARCHAR(64) NOT NULL COMMENT '分享人的姓名',
  `shared_source_type` VARCHAR(32) NOT NULL COMMENT '默认MEMBER_DETAIL，被分享人ID类型',
  `shared_source_id` BIGINT NOT NULL COMMENT 'source_type对应的ID，被分享人员工档案ID',
  `shared_source_name` VARCHAR(128) NOT NULL COMMENT '被分享人员工姓名',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_shared_source_id` (`namespace_id`,`owner_type`,`owner_id`,`shared_source_type`,`shared_source_id`),
  KEY `i_eh_remind_id` (`remind_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='日程共享人记录表';

DROP TABLE IF EXISTS `eh_reminds`;


CREATE TABLE `eh_reminds` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认EhOrganizations',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `user_id` BIGINT NOT NULL COMMENT '日程所有人的用户ID',
  `contact_name` VARCHAR(64) NOT NULL COMMENT '日程所有人的姓名',
  `plan_description` VARCHAR(512) NOT NULL COMMENT '日程描述',
  `plan_date` DATETIME COMMENT '日程的计划日期',
  `expect_day_of_month` TINYINT COMMENT '最初的重复日程计划的日期，DAY值',
  `remind_summary` VARCHAR(64) COMMENT '提醒的文本概要',
  `repeat_type` TINYINT NOT NULL DEFAULT 0 COMMENT '重复类型：0-无，1-每日，2-每周，3-每月，4-每年',
  `remind_type_id` INTEGER COMMENT '提醒类型ID',
  `remind_type` VARCHAR(32) COMMENT '提醒类型的名称，如  提前一天（09:00）',
  `remind_time` DATETIME COMMENT '提醒时间，根据选择的提醒类型计算得到',
  `act_remind_time` DATETIME COMMENT '实际提醒时间，即实际出发提醒的时间',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT ' 1: UNDO 未完成 , 2 : DONE 已完成  ',
  `remind_category_id` BIGINT NOT NULL COMMENT '日程分类ID',
  `track_remind_id` BIGINT COMMENT '关注的日程ID',
  `track_remind_user_id` BIGINT COMMENT '关注的日程的所有人的uid',
  `track_contract_name` VARCHAR(45) COMMENT '关注的日程所有人的姓名',
  `share_short_display` VARCHAR(64) COMMENT '共享人概要信息：如 xx等3人',
  `share_count` INTEGER NOT NULL DEFAULT 0 COMMENT '共享人数量',
  `default_order` INTEGER NOT NULL DEFAULT 0 COMMENT '排序字段',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  `operate_time` DATETIME COMMENT '记录更新时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_user_id` (`namespace_id`,`owner_type`,`owner_id`,`user_id`),
  KEY `i_eh_remind_time` (`remind_time`),
  KEY `i_eh_track_remind_id` (`track_remind_id`),
  KEY `i_eh_remind_category_id` (`remind_category_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='日程表';

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
  `cell_id` BIGINT,
  `rental_resource_id` BIGINT COMMENT 'rental_resource id',
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天',
  `price_type` TINYINT,
  `amorpm` TINYINT COMMENT '0:am 1:pm 2:night',
  `begin_time` DATETIME COMMENT '开始时间 对于按时间定',
  `end_time` DATETIME COMMENT '结束时间 对于按时间定',
  `counts` DOUBLE COMMENT '共多少个',
  `price` DECIMAL(10,2) COMMENT '折后价',
  `initiate_price` DECIMAL(10,2),
  `resource_rental_date` DATE COMMENT 'which day',
  `status` TINYINT COMMENT 'unuse 0:open 1:closed',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `time_step` DOUBLE,
  `original_price` DECIMAL(10,2) COMMENT '原价（如果不为null则price为打折价）',
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段 1是 0否',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `resource_number` VARCHAR(100) COMMENT '场所号',
  `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源',
  `group_lock_flag` TINYINT COMMENT '一个资源被预约是否锁整个group,0-否,1-是',
  `org_member_original_price` DECIMAL(10,2) COMMENT '原价-如果打折则有(企业内部价)',
  `org_member_price` DECIMAL(10,2) COMMENT '实际价格-打折则为折后价(企业内部价)',
  `org_member_initiate_price` DECIMAL(10,2),
  `approving_user_original_price` DECIMAL(10,2) COMMENT '原价-如果打折则有（外部客户价）',
  `approving_user_price` DECIMAL(10,2) COMMENT '实际价格-打折则为折后价（外部客户价）',
  `approving_user_initiate_price` DECIMAL(10,2),
  `price_package_id` BIGINT,
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  `user_price_type` TINYINT COMMENT '用户价格类型, 1:统一价格 2：用户类型价格',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_cell_id` (`id`)
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
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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
  `default_order` INTEGER DEFAULT 0,
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_dayopen_time`;

CREATE TABLE `eh_rentalv2_dayopen_time` (
  `id` BIGINT NOT NULL,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255),
  `open_time` DOUBLE,
  `close_time` DOUBLE,
  `rental_type` TINYINT,
  `resource_type` VARCHAR(64),
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
  `refund_flag` TINYINT COMMENT '是否支持退款: 1-是, 0-否',
  `refund_ratio` INTEGER COMMENT '退款比例',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段: 1-是, 0-否',
  `need_pay` TINYINT COMMENT '是否需要支付: 1-是, 0-否',
  `begin_date` DATE COMMENT '开始日期',
  `end_date` DATE COMMENT '结束日期',
  `day_open_time` DOUBLE,
  `day_close_time` DOUBLE,
  `open_weekday` VARCHAR(7) COMMENT '7位二进制，0000000每一位表示星期7123456',
  `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制',
  `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制',
  `source_type` VARCHAR(255) COMMENT 'default_rule, resource_rule',
  `source_id` BIGINT,
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  `holiday_open_flag` TINYINT COMMENT '节假日是否开放预约: 1-是, 0-否',
  `holiday_type` TINYINT COMMENT '1-普通双休, 2-同步中国节假日',
  `refund_strategy` TINYINT COMMENT '1-custom, 2-full',
  `overtime_strategy` TINYINT COMMENT '1-custom, 2-full',
  `remark_flag` TINYINT COMMENT '备注是否必填 0否 1是',
  `remark` VARCHAR(255) COMMENT '备注显示文案',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_holiday`;


CREATE TABLE `eh_rentalv2_holiday` (
  `id` INTEGER NOT NULL,
  `holiday_type` tinyint(8) COMMENT '1:普通双休 2:法定节假日',
  `close_date` TEXT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_items`;


CREATE TABLE `eh_rentalv2_items` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `source_type` VARCHAR(45) COMMENT 'DEFAULT: default_rule  RESOURCE: resource_rule',
  `source_id` BIGINT,
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
  `description` VARCHAR(1024),
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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


-- 新增表保存支付订单信息
DROP TABLE IF EXISTS `eh_rentalv2_order_records`;


CREATE TABLE `eh_rentalv2_order_records` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `order_id` BIGINT COMMENT '资源预订订单id',
  `order_no` BIGINT COMMENT '资源预订订单号',
  `biz_order_num` VARCHAR(64) COMMENT '处理过的资源预订订单号',
  `pay_order_id` BIGINT COMMENT '支付系统订单号',
  `payment_order_type` tinyint(8) COMMENT '订单类型 1续费订单 2欠费订单 3支付订单 4退款订单',
  `status` tinyint(8) COMMENT '订单状态0未支付 1已支付',
  `amount` DECIMAL(16,0) COMMENT '订单金额',
  `account_id` BIGINT COMMENT '收款方账号',
  `account_name` VARCHAR(255),
  `order_commit_url` VARCHAR(1024),
  `order_commit_token` VARCHAR(1024),
  `order_commit_nonce` VARCHAR(128),
  `order_commit_timestamp` BIGINT,
  `pay_info` TEXT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_order_rules`;


CREATE TABLE `eh_rentalv2_order_rules` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  `owner_type` VARCHAR(255) COMMENT 'default_rule, resource_rule',
  `owner_id` BIGINT,
  `handle_type` TINYINT COMMENT '1: 退款, 2: 加收',
  `duration_type` TINYINT COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` TINYINT COMMENT '时长单位，比如 天，小时',
  `duration` DOUBLE COMMENT '时长',
  `factor` DOUBLE COMMENT '价格系数',
  `status` TINYINT,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `update_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_order_statistics`;


CREATE TABLE `eh_rentalv2_order_statistics` (
  `id` BIGINT NOT NULL,
  `order_id` BIGINT NOT NULL,
  `rental_resource_id` BIGINT NOT NULL,
  `rental_uid` BIGINT,
  `rental_date` DATE,
  `start_time` DATETIME,
  `end_time` DATETIME,
  `reserve_time` DATETIME,
  `valid_time_long` BIGINT,
  `community_id` BIGINT,
  `namespace_id` INTEGER,
  `user_enterprise_id` BIGINT,
  `rental_type` TINYINT,
  `resource_type` VARCHAR(64),
  `resource_type_id` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_orders`;


CREATE TABLE `eh_rentalv2_orders` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `order_no` VARCHAR(20) NOT NULL COMMENT '订单编号',
  `rental_resource_id` BIGINT NOT NULL COMMENT 'id',
  `rental_uid` BIGINT COMMENT 'rental user id',
  `rental_date` DATE COMMENT '使用日期',
  `start_time` DATETIME COMMENT '使用开始时间',
  `end_time` DATETIME COMMENT '使用结束时间',
  `rental_count` DOUBLE COMMENT '预约数',
  `pay_total_money` DECIMAL(10,2) COMMENT '总价',
  `resource_total_money` DECIMAL(10,2),
  `reserve_time` DATETIME COMMENT 'reserve time',
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
  `longitude` DOUBLE COMMENT '地址经度',
  `latitude` DOUBLE COMMENT '地址纬度',
  `contact_phonenum` VARCHAR(20) COMMENT '咨询电话',
  `introduction` TEXT COMMENT '详情',
  `notice` TEXT,
  `community_id` BIGINT COMMENT '资源所属园区的ID',
  `namespace_id` INTEGER COMMENT '域空间',
  `refund_flag` TINYINT COMMENT '是否支持退款 1是 0否',
  `refund_ratio` INTEGER COMMENT '退款比例',
  `cancel_flag` TINYINT COMMENT '是否允许取消 1是 0否',
  `reminder_time` DATETIME COMMENT '消息提醒时间',
  `reminder_end_time` DATETIME,
  `auth_start_time` DATETIME,
  `auth_end_time` DATETIME,
  `door_auth_id` VARCHAR(128),
  `package_name` VARCHAR(45),
  `pay_mode` TINYINT DEFAULT 0 COMMENT 'pay mode :0-online pay 1-offline',
  `offline_cashier_address` VARCHAR(200),
  `offline_payee_uid` BIGINT,
  `flow_case_id` BIGINT COMMENT 'id of the flow_case',
  `user_enterprise_id` BIGINT COMMENT '申请人公司ID',
  `paid_version` TINYINT,
  `rental_type` TINYINT,
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  `custom_object` TEXT,
  `user_enterprise_name` VARCHAR(64) COMMENT '申请人公司名称',
  `user_phone` VARCHAR(20) COMMENT '申请人手机',
  `user_name` VARCHAR(20) COMMENT '申请人姓名',
  `address_id` BIGINT COMMENT '楼栋门牌ID',
  `refund_amount` DECIMAL(10,2),
  `actual_start_time` DATETIME COMMENT '实际使用开始时间',
  `actual_end_time` DATETIME COMMENT '实际使用结束时间',
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `scene` VARCHAR(64) COMMENT '下单时场景信息，用来计算价格',
  `refund_strategy` TINYINT COMMENT '1-custom, 2-full',
  `overtime_strategy` TINYINT COMMENT '1-custom, 2-full',
  `old_end_time` DATETIME COMMENT '延长订单时，存老的使用结束时间',
  `old_custom_object` TEXT,
  `account_name` VARCHAR(255),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_pay_accounts`;


CREATE TABLE `eh_rentalv2_pay_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `community_id` BIGINT,
  `resource_type` VARCHAR(20),
  `resource_type_id` BIGINT,
  `source_type` VARCHAR(20) COMMENT 'default_rule:默认规则 resource_rule:资源规则',
  `source_id` BIGINT,
  `resource_name` VARCHAR(20),
  `account_id` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_price_packages`;

CREATE TABLE `eh_rentalv2_price_packages` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL,
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(45),
  `rental_type` TINYINT,
  `price_type` TINYINT,
  `price` DECIMAL(10,2),
  `initiate_price` DECIMAL(10,2),
  `original_price` DECIMAL(10,2),
  `org_member_price` DECIMAL(10,2),
  `org_member_initiate_price` DECIMAL(10,2),
  `org_member_original_price` DECIMAL(10,2),
  `approving_user_price` DECIMAL(10,2),
  `approving_user_initiate_price` DECIMAL(10,2),
  `approving_user_original_price` DECIMAL(10,2),
  `discount_type` TINYINT,
  `full_price` DECIMAL(10,2),
  `cut_price` DECIMAL(10,2),
  `discount_ratio` DOUBLE,
  `org_member_discount_type` TINYINT,
  `org_member_full_price` DECIMAL(10,2),
  `org_member_cut_price` DECIMAL(10,2),
  `org_member_discount_ratio` DOUBLE,
  `approving_user_discount_type` TINYINT,
  `approving_user_full_price` DECIMAL(10,2),
  `approving_user_cut_price` DECIMAL(10,2),
  `approving_user_discount_ratio` DOUBLE,
  `cell_begin_id` BIGINT NOT NULL DEFAULT 0,
  `cell_end_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `user_price_type` TINYINT COMMENT '用户价格类型, 1:统一价格 2：用户类型价格',
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_rentalv2_price_rules`;


CREATE TABLE `eh_rentalv2_price_rules` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'default, resource, cell',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'default_rule_id, resource_id, cell_id',
  `rental_type` TINYINT COMMENT '0: as hour:min 1-as half day 2-as day 3-支持晚上的半天 4按月',
  `price_type` TINYINT,
  `workday_price` DECIMAL(10,2) COMMENT '工作日价格',
  `original_price` DECIMAL(10,2),
  `initiate_price` DECIMAL(10,2),
  `org_member_workday_price` DECIMAL(10,2) COMMENT '企业内部工作日价格',
  `org_member_original_price` DECIMAL(10,2),
  `org_member_initiate_price` DECIMAL(10,2),
  `approving_user_workday_price` DECIMAL(10,2) COMMENT '外部客户工作日价格',
  `approving_user_original_price` DECIMAL(10,2),
  `approving_user_initiate_price` DECIMAL(10,2),
  `discount_type` TINYINT COMMENT '折扣信息：0不打折 1满减优惠 2满天减 3比例折扣',
  `full_price` DECIMAL(10,2) COMMENT '满XX',
  `cut_price` DECIMAL(10,2) COMMENT '减XX元',
  `discount_ratio` DOUBLE COMMENT '折扣比例',
  `org_member_discount_type` TINYINT,
  `org_member_full_price` DECIMAL(10,2),
  `org_member_cut_price` DECIMAL(10,2),
  `org_member_discount_ratio` DOUBLE,
  `approving_user_discount_type` TINYINT,
  `approving_user_full_price` DECIMAL(10,2),
  `approving_user_cut_price` DECIMAL(10,2),
  `approving_user_discount_ratio` DOUBLE,
  `cell_begin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'cells begin id',
  `cell_end_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'cells end id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `user_price_type` TINYINT COMMENT '用户价格类型, 1:统一价格 2：用户类型价格',
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_refund_tips`;


CREATE TABLE `eh_rentalv2_refund_tips` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `source_type` VARCHAR(20),
  `source_id` BIGINT,
  `refund_strategy` TINYINT,
  `tips` VARCHAR(255),
  `resource_type` VARCHAR(20),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_resource_numbers`;


CREATE TABLE `eh_rentalv2_resource_numbers` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT 'EhRentalv2DefaultRules-默认规则,EhRentalv2Resources-具体场所',
  `resource_number` VARCHAR(255) COMMENT '场所编号',
  `number_group` INTEGER COMMENT '同一个groupid的两个编号资源被认为是一组资源',
  `group_lock_flag` TINYINT COMMENT '一个资源被预约是否锁整个group,0-否,1-是',
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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
  `begin_time` DATETIME COMMENT '开始时间 对于按时间定',
  `end_time` DATETIME COMMENT '结束时间 对于按时间定',
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `multi_time_interval` TINYINT COMMENT '是否允许预约多个时段 1是 0否',
  `status` TINYINT DEFAULT 0 COMMENT '状态 0-普通预定订单 1-不显示给用户的',
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  `resource_number` VARCHAR(64),
  PRIMARY KEY (`id`),
  KEY `i_eh_rental_order_rule_id` (`rental_resource_rule_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_resource_ranges`;


CREATE TABLE `eh_rentalv2_resource_ranges` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `owner_type` VARCHAR(255) COMMENT 'owner type : community ; organization',
  `owner_id` BIGINT COMMENT 'community id or organization id',
  `rental_resource_id` BIGINT COMMENT 'rental_resource id',
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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
  `unauth_visible` TINYINT DEFAULT 0,
  `menu_type` TINYINT DEFAULT 1 COMMENT '1: 通用 2:公司会议室',
  `identify` VARCHAR(64) COMMENT '类型标识',
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
  `auto_assign` TINYINT COMMENT '是否动态分配 1是 0否',
  `multi_unit` TINYINT COMMENT '是否允许预约多个场所 1是 0否',
  `resource_type_id` BIGINT COMMENT 'resource type id',
  `longitude` DOUBLE COMMENT '地址经度',
  `latitude` DOUBLE COMMENT '地址纬度',
  `organization_id` BIGINT COMMENT '所属公司的ID',
  `community_id` BIGINT COMMENT '所属的社区ID（和可见范围的不一样）',
  `resource_counts` DOUBLE COMMENT '可预约个数',
  `avg_price_str` VARCHAR(1024) COMMENT '平均价格计算好的字符串',
  `confirmation_prompt` VARCHAR(200),
  `offline_cashier_address` VARCHAR(200),
  `offline_payee_uid` BIGINT,
  `default_order` BIGINT NOT NULL DEFAULT 0 COMMENT 'order',
  `aclink_id` VARCHAR(128),
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_site_resources`;


CREATE TABLE `eh_rentalv2_site_resources` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT 'EhRentalv2Resources',
  `type` VARCHAR(64) DEFAULT 'pic',
  `name` VARCHAR(64),
  `size` VARCHAR(64),
  `uri` VARCHAR(1024),
  `resource_type` VARCHAR(64) COMMENT '资源类型',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_rentalv2_time_interval`;


CREATE TABLE `eh_rentalv2_time_interval` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(255) COMMENT '"default_rule","resource_rule"',
  `begin_time` DOUBLE COMMENT '开始时间-24小时制',
  `end_time` DOUBLE COMMENT '结束时间-24小时制',
  `time_step` DOUBLE COMMENT '按小时预约：最小单元格是多少小时，浮点型',
  `amorpm` TINYINT,
  `name` VARCHAR(10),
  `resource_type` VARCHAR(64) COMMENT '资源类型',
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

DROP TABLE IF EXISTS `eh_requisition_types`;


CREATE TABLE `eh_requisition_types` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `name` VARCHAR(32) NOT NULL COMMENT '类型名字',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_uid` BIGINT,
  `default_order` INTEGER DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_requisitions`;


CREATE TABLE `eh_requisitions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `identity` VARCHAR(32) NOT NULL COMMENT '请示单号',
  `theme` VARCHAR(32) NOT NULL COMMENT '请示主题',
  `requisition_type_id` BIGINT COMMENT '请示类型,参考eh_requisition_type表',
  `applicant_name` VARCHAR(128) NOT NULL COMMENT '请示人id',
  `applicant_department` VARCHAR(256) COMMENT '请示人部门',
  `amount` DECIMAL(20,2) DEFAULT '0.00' COMMENT '申请金额',
  `description` TEXT COMMENT '申请说明',
  `attachment_url` VARCHAR(2048) COMMENT '附件地址',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '审批状态，1:处理中；2:已完成; 3:已取消;',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_uid` BIGINT,
  `default_order` INTEGER DEFAULT 0,
  `community_id` BIGINT COMMENT '园区id',
  `file_name` VARCHAR(256) COMMENT '文件名称',
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
  `wechat_signup` TINYINT DEFAULT 0 COMMENT 'is support wechat signup 0:no, 1:yes',
  `category_id` BIGINT COMMENT 'category_id',
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

DROP TABLE IF EXISTS `eh_salary_default_entities`;


CREATE TABLE `eh_salary_default_entities` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `editable_flag` TINYINT COMMENT '是否可编辑:-1 数值也不能编辑 0-否   1-是',
  `delete_flag` TINYINT COMMENT '是否可删除:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `name` VARCHAR(64),
  `description` TEXT COMMENT '说明文字',
  `template_name` VARCHAR(32),
  `default_order` INTEGER,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '默认是否开启0不开启 2-开启',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_depart_statistics`;


CREATE TABLE `eh_salary_depart_statistics` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `dept_id` BIGINT COMMENT 'user department id',
  `dept_name` VARCHAR(128) COMMENT 'user department name',
  `namespace_id` INTEGER,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `regular_salary` DECIMAL(10,2) COMMENT '固定工资合计',
  `should_pay_salary` DECIMAL(10,2) COMMENT '应发工资合计',
  `real_pay_salary` DECIMAL(10,2) COMMENT '实发工资合计',
  `cost_salary` DECIMAL(10,2) COMMENT '人工成本合计',
  `cost_mom_salary` DECIMAL(10,2) COMMENT '人工成本环比',
  `cost_yoy_salary` DECIMAL(10,2) COMMENT '人工成本同比',
  `creator_uid` BIGINT COMMENT '人员id',
  `create_time` DATETIME,
  `is_file` TINYINT COMMENT '0-普通 1-归档',
  `filer_uid` BIGINT COMMENT '创建者',
  `file_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_employee_origin_vals`;


CREATE TABLE `eh_salary_employee_origin_vals` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `namespace_id` INTEGER,
  `user_id` BIGINT,
  `user_detail_id` BIGINT,
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk',
  `group_entity_name` VARCHAR(64),
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `salary_value` TEXT COMMENT '值:如果次月清空则在新建报表时候置为null',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_employee_period_vals`;


CREATE TABLE `eh_salary_employee_period_vals` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INTEGER,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `user_id` BIGINT,
  `user_detail_id` BIGINT,
  `salary_employee_id` BIGINT COMMENT '标签(统计分类) salary_employee表pk',
  `group_entity_id` BIGINT COMMENT '标签(统计分类) salary group entity表pk',
  `group_entity_name` VARCHAR(64),
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `salary_value` TEXT COMMENT '值:如果次月清空则在新建报表时候置为null',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_employees`;


CREATE TABLE `eh_salary_employees` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `namespace_id` INTEGER,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `user_id` BIGINT,
  `user_detail_id` BIGINT,
  `regular_salary` DECIMAL(10,2) COMMENT '固定工资合计',
  `should_pay_salary` DECIMAL(10,2) COMMENT '应发工资合计',
  `real_pay_salary` DECIMAL(10,2) COMMENT '实发工资合计',
  `cost_salary` DECIMAL(10,2) COMMENT '人工成本合计',
  `creator_uid` BIGINT COMMENT '人员id',
  `create_time` DATETIME,
  `status` TINYINT COMMENT '状态0-正常 1-实发合计为负  2-未定薪',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_employees_files`;


CREATE TABLE `eh_salary_employees_files` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `namespace_id` INTEGER,
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `user_id` BIGINT,
  `user_detail_id` BIGINT,
  `regular_salary` DECIMAL(10,2) COMMENT '固定工资合计',
  `should_pay_salary` DECIMAL(10,2) COMMENT '应发工资合计',
  `real_pay_salary` DECIMAL(10,2) COMMENT '实发工资合计',
  `cost_salary` DECIMAL(10,2) COMMENT '人工成本合计',
  `creator_uid` BIGINT COMMENT '人员id',
  `create_time` DATETIME,
  `status` TINYINT COMMENT '状态0-正常 1-实发合计为负  2-未定薪',
  `filer_uid` BIGINT COMMENT '创建者',
  `file_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_entity_categories`;


CREATE TABLE `eh_salary_entity_categories` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT,
  `namespace_id` INTEGER COMMENT '并不用,现在是所有域空间通用',
  `category_name` VARCHAR(64) COMMENT 'name of category',
  `description` TEXT COMMENT '说明文字',
  `custom_flag` TINYINT COMMENT '是否可以自定义: 0-否 1-是',
  `custom_type` TINYINT COMMENT '自定义字段的类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_group_entities`;


CREATE TABLE `eh_salary_group_entities` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `default_id` BIGINT COMMENT 'id of the eh_salary_default_entities',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `origin_entity_id` BIGINT,
  `namespace_id` INTEGER COMMENT '并不用,现在是所有域空间通用',
  `default_flag` TINYINT COMMENT '是否是缺省启用参数:0-否 1-是',
  `editable_flag` TINYINT COMMENT '是否可编辑:0-否   1-是',
  `delete_flag` TINYINT COMMENT '是否可删除:0-否   1-是',
  `type` TINYINT COMMENT '字段类型:0-发放项;1-扣款项;2-成本项;3-冗余项',
  `data_policy` TINYINT COMMENT '数据策略:0-次月延用 1-次月清空',
  `grant_policy` TINYINT COMMENT '发放策略:0-税前 1-税后',
  `tax_policy` TINYINT COMMENT '纳税策略:0-工资 1-年终',
  `category_id` BIGINT COMMENT '标签(薪酬结构) category表pk',
  `category_name` VARCHAR(64) COMMENT '标签(统计分类)名称 example:固定工资,浮动工资,津补贴',
  `name` VARCHAR(64),
  `description` TEXT COMMENT '说明文字',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `status` TINYINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_groups`;


CREATE TABLE `eh_salary_groups` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `creator_uid` BIGINT COMMENT '创建者',
  `create_time` DATETIME,
  `creator_Name` VARCHAR(128) COMMENT '创建者姓名',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_groups_files`;


CREATE TABLE `eh_salary_groups_files` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `creator_Name` VARCHAR(128) COMMENT '创建者姓名',
  `creator_uid` BIGINT COMMENT '创建者',
  `create_time` DATETIME,
  `filer_Name` VARCHAR(128) COMMENT '归档者姓名',
  `filer_uid` BIGINT COMMENT '归档者',
  `file_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_salary_groups_report_resources`;


CREATE TABLE `eh_salary_groups_report_resources` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(8) COMMENT 'example:201705',
  `report_type` TINYINT COMMENT '文件类型:0-工资明细 1-部门汇总',
  `uri` VARCHAR(1024),
  `url` VARCHAR(1024),
  `creator_Name` VARCHAR(128) COMMENT '创建者姓名',
  `creator_uid` BIGINT COMMENT '创建者',
  `create_time` DATETIME,
  `filer_Name` VARCHAR(128) COMMENT '归档者姓名',
  `filer_uid` BIGINT COMMENT '归档者',
  `file_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- Description: ISSUE#25515: 薪酬V2.2（工资条发放管理；app支持工资条查看/确认）
DROP TABLE IF EXISTS `eh_salary_payslip_details`;


CREATE TABLE `eh_salary_payslip_details` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `payslip_id` BIGINT NOT NULL COMMENT '父键;工资条id',
  `namespace_id` INTEGER,
  `salary_period` VARCHAR(12) COMMENT 'example:201705',
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `user_id` BIGINT,
  `user_detail_id` BIGINT,
  `name` VARCHAR(512) NOT NULL COMMENT '姓名',
  `user_contact` VARCHAR(20) NOT NULL COMMENT '手机号',
  `payslip_content` TEXT COMMENT '导入的工资条数据(key-value对的json字符串)',
  `viewed_flag` TINYINT COMMENT '已查看0-否 1-是',
  `status` TINYINT COMMENT '状态0-已发送 1-已撤回  2-已确认',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY (`id`),
  KEY `i_eh_payslip_id` (`payslip_id`),
  KEY `i_eh_organization_user` (`user_id`,`organization_id`),
  KEY `i_eh_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工资条详情表';

DROP TABLE IF EXISTS `eh_salary_payslips`;


CREATE TABLE `eh_salary_payslips` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32) COMMENT 'organization',
  `owner_id` BIGINT COMMENT '属于哪一个分公司的',
  `organization_id` BIGINT COMMENT '属于哪一个总公司的',
  `salary_period` VARCHAR(12) COMMENT 'example:201705',
  `name` VARCHAR(1024) NOT NULL COMMENT '工资表名称',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  `update_time` DATETIME COMMENT '记录更新时间',
  `operator_uid` BIGINT COMMENT '记录更新人userId',
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_period` (`owner_id`,`salary_period`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工资条表';

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

DROP TABLE IF EXISTS `eh_schema_versions`;


CREATE TABLE `eh_schema_versions` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` VARCHAR(32) NOT NULL,
  `version` INTEGER,
  `description` TEXT,
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

DROP TABLE IF EXISTS `eh_sensitive_filter_record`;


CREATE TABLE `eh_sensitive_filter_record` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `sensitive_words` VARCHAR(128) COMMENT '敏感词',
  `module_id` BIGINT COMMENT '模块ID',
  `community_id` BIGINT COMMENT '项目ID',
  `creator_uid` BIGINT COMMENT '记录发布人userId',
  `creator_name` VARCHAR(32) COMMENT '发布人姓名',
  `phone` VARCHAR(128) COMMENT '发布人手机号',
  `publish_time` DATETIME COMMENT '记录发布时间',
  `text` TEXT COMMENT '文本内容',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='敏感词过滤日志表';

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

DROP TABLE IF EXISTS `eh_service_agreement`;


CREATE TABLE `eh_service_agreement` (
  `id` INTEGER NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL COMMENT '域空间ID',
  `agreement_content` MEDIUMTEXT COMMENT '协议内容',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='服务协议信息表';

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

DROP TABLE IF EXISTS `eh_service_alliance_application_records`;


CREATE TABLE `eh_service_alliance_application_records` (
  `id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id',
  `namespace_id` INTEGER,
  `jump_type` BIGINT,
  `template_type` VARCHAR(128),
  `type` BIGINT,
  `owner_type` VARCHAR(128),
  `owner_id` BIGINT,
  `creator_name` VARCHAR(128),
  `creator_organization_id` BIGINT,
  `creator_mobile` VARCHAR(128),
  `create_time` DATETIME,
  `create_date` DATETIME,
  `creator_uid` BIGINT,
  `flow_case_id` BIGINT,
  `second_category_id` BIGINT,
  `second_category_name` VARCHAR(128),
  `workflow_status` TINYINT,
  `creator_organization` VARCHAR(128),
  `service_organization` VARCHAR(128),
  `service_alliance_id` BIGINT,
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
  `attachment_type` TINYINT DEFAULT 0 COMMENT '0: banner; 1: file attachment; 2: cover',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT 0,
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `default_order` TINYINT NOT NULL DEFAULT 0 COMMENT 'the order of image; the smaller the toper;0,1,2,3,...',
  `skip_url` VARCHAR(1024) COMMENT 'the url to skip',
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
  `default_order` BIGINT NOT NULL DEFAULT 0,
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
  `entry_id` INTEGER,
  PRIMARY KEY (`id`),
  KEY `i_eh_parent_id` (`parent_id`),
  KEY `i_eh_default_order` (`default_order`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliance_comment_attachments`;


CREATE TABLE `eh_service_alliance_comment_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `operator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- by dengs, 20170925 服务联盟2.9
DROP TABLE IF EXISTS `eh_service_alliance_comments`;


CREATE TABLE `eh_service_alliance_comments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g servicealliance id',
  `parent_comment_id` BIGINT COMMENT 'parent comment Id',
  `content_type` VARCHAR(32) COMMENT 'object content type',
  `content` TEXT COMMENT 'content data, depends on value of content_type',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME,
  `deleter_uid` BIGINT COMMENT 'deleter uid',
  `delete_time` DATETIME COMMENT 'delete time',

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
  `module_id` BIGINT NOT NULL DEFAULT 0,
  `module_url` VARCHAR(512),
  `instance_config` TEXT,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `signal` TINYINT DEFAULT 1 COMMENT '标志 0:删除 1:普通 2:审批',
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

DROP TABLE IF EXISTS `eh_service_alliance_providers`;


CREATE TABLE `eh_service_alliance_providers` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '''''',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `app_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'module_app id, new type of alliance, represent one kine fo alliance',
  `type` BIGINT NOT NULL COMMENT 'old type of Alliance，represent one kind of alliance',
  `name` VARCHAR(50) NOT NULL COMMENT 'provider name',
  `category_id` BIGINT NOT NULL COMMENT '见 categories表',
  `mail` VARCHAR(50) NOT NULL COMMENT 'enterprise mail',
  `contact_number` VARCHAR(50) NOT NULL COMMENT 'mobile or contact phone',
  `contact_name` VARCHAR(50) NOT NULL COMMENT 'contact name',
  `total_score` BIGINT NOT NULL DEFAULT 0 COMMENT 'total score',
  `score_times` INTEGER NOT NULL DEFAULT 0 COMMENT 'the num of times make the score',
  `score_flow_case_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the final flow case id that make score',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-deleted 1-active',
  `create_time` DATETIME NOT NULL,
  `create_uid` BIGINT NOT NULL COMMENT 'create user id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='服务商信息';

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
  PRIMARY KEY (`id`),
  KEY `i_eh_category_index` (`service_alliance_category_id`,`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_service_alliances`;


CREATE TABLE `eh_service_alliances` (
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `range` VARCHAR(512),
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'organization name',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '',
  `type` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id reference to eh_service_alliance_categories',
  `address` VARCHAR(255),
  `contact` VARCHAR(64),
  `description` MEDIUMTEXT,
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
  `service_url` VARCHAR(512),
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
  `summary_description` VARCHAR(1024),
  `enable_comment` TINYINT DEFAULT 0 COMMENT '1,enable;0,disable',
  `jump_service_alliance_routing` VARCHAR(2048) COMMENT 'jump to other service alliance routing',
  `online_service_uid` BIGINT COMMENT 'online service user id',
  `online_service_uname` VARCHAR(64) COMMENT 'online service user name',
  `start_time` DATETIME COMMENT 'for policydeclare ; start time of the policy',
  `end_time` DATETIME COMMENT 'for policydeclare ; end time of the policy',
  PRIMARY KEY (`id`),
  KEY `i_eh_default_order` (`default_order`),
  KEY `i_eh_type` (`type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-deleted 1-active',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_module_apps`;


CREATE TABLE `eh_service_module_apps` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `version_id` BIGINT,
  `origin_id` BIGINT,
  `name` VARCHAR(64),
  `module_id` BIGINT,
  `instance_config` TEXT COMMENT '应用入口需要的配置参数',
  `status` TINYINT NOT NULL DEFAULT 0,
  `action_type` TINYINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `module_control_type` VARCHAR(64) DEFAULT '' COMMENT 'community_control;org_control;unlimit',
  `custom_tag` VARCHAR(256) DEFAULT '',
  `custom_path` VARCHAR(128) DEFAULT '',
  `access_control_type` TINYINT DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth',
  PRIMARY KEY (`id`),
  KEY `origin_id_index` (`origin_id`),
  KEY `i_eh_version_id_index` (`version_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_module_assignment_relations`;


CREATE TABLE `eh_service_module_assignment_relations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'EhOrganizations, EhCommunities',
  `owner_id` BIGINT NOT NULL,
  `all_module_flag` TINYINT COMMENT '0 not all, 1 all',
  `all_project_flag` TINYINT COMMENT '0 not all, 1 all',
  `target_json` TEXT,
  `project_json` TEXT,
  `module_json` TEXT,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME,
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
  `all_module_flag` TINYINT COMMENT '0 not all, 1 all',
  `include_child_flag` TINYINT COMMENT '0 not include, 1 include',
  `relation_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_module_exclude_functions`;


CREATE TABLE `eh_service_module_exclude_functions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `module_id` BIGINT NOT NULL DEFAULT 0,
  `function_id` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_module_functions`;


CREATE TABLE `eh_service_module_functions` (
  `id` BIGINT NOT NULL,
  `module_id` BIGINT NOT NULL DEFAULT 0,
  `privilege_id` BIGINT NOT NULL DEFAULT 0,
  `explain` VARCHAR(64) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_service_module_include_functions`;


CREATE TABLE `eh_service_module_include_functions` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL COMMENT 'namespace_id',
  `module_id` BIGINT NOT NULL COMMENT 'module_id',
  `community_id` BIGINT COMMENT 'community_id',
  `function_id` BIGINT NOT NULL COMMENT '关联的按钮id',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='eh_service_module_include_functions in dev mode';

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
  `instance_config` TEXT,
  `action_type` TINYINT,
  `update_time` DATETIME,
  `operator_uid` BIGINT NOT NULL,
  `creator_uid` BIGINT NOT NULL,
  `description` VARCHAR(1024),
  `multiple_flag` TINYINT,
  `module_control_type` VARCHAR(64) DEFAULT '' COMMENT 'community_control;org_control;unlimit',
  `access_control_type` TINYINT DEFAULT 1 COMMENT '0-all, 1-logon, 2-auth',
  `menu_auth_flag` TINYINT NOT NULL DEFAULT 1 COMMENT 'if its menu need auth',
  `category` VARCHAR(255) COMMENT 'classify, module, subModule',
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

DROP TABLE IF EXISTS `eh_shard_dependency`;


CREATE TABLE `eh_shard_dependency` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `sharding_domain` VARCHAR(64),
  `depends_on` VARCHAR(64),
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_shard_dep_domain` (`sharding_domain`),
  KEY `i_eh_shard_dep_depends` (`depends_on`)
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

DROP TABLE IF EXISTS `eh_siyin_print_business_payee_accounts`;


CREATE TABLE `eh_siyin_print_business_payee_accounts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL COMMENT 'community 园区',
  `owner_id` BIGINT NOT NULL COMMENT '园区id',
  `payee_id` BIGINT NOT NULL COMMENT '支付帐号id',
  `payee_user_type` VARCHAR(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='云打印收款账户表';

DROP TABLE IF EXISTS `eh_siyin_print_emails`;


CREATE TABLE `eh_siyin_print_emails` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `user_id` BIGINT,
  `email` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_siyin_print_orders`;


CREATE TABLE `eh_siyin_print_orders` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `order_no` BIGINT COMMENT 'order number',
  `job_type` TINYINT COMMENT 'siyin returned,PRINT(1),COPY(2),SCAN(3)',
  `print_document_name` VARCHAR(256) COMMENT 'print document name',
  `detail` TEXT COMMENT 'print/copy/scan details',
  `email` VARCHAR(128),
  `order_total_fee` DECIMAL(10,2) COMMENT 'order price',
  `order_type` VARCHAR(128) COMMENT 'order type: print(1)',
  `order_body` VARCHAR(128) COMMENT 'order body: print(1)',
  `order_subject` VARCHAR(256) COMMENT 'print order',
  `order_status` TINYINT COMMENT 'the status of the order, 0: inactive, 1: unpaid, 2: paid',
  `lock_flag` TINYINT COMMENT 'lock the order, and can not merge order 0(unlocked),1(locked)',
  `paid_type` VARCHAR(32) COMMENT '10001:alipay,10002:weixin',
  `paid_time` DATETIME,
  `nick_name` VARCHAR(128) COMMENT 'creator nick name',
  `creator_company` TEXT COMMENT 'creator companys',
  `creator_phone` VARCHAR(128) COMMENT 'create phone',
  `creator_uid` BIGINT COMMENT 'creator/initiator id',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `pay_order_no` VARCHAR(64) COMMENT '支付系统单号',
  `pay_dto` TEXT COMMENT '支付系统返回预付单信息',
  `general_order_id` VARCHAR(64) COMMENT '统一订单系统订单编号',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_siyin_print_printers`;


CREATE TABLE `eh_siyin_print_printers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `reader_name` VARCHAR(128) COMMENT 'printer reader name',
  `module_port` VARCHAR(16) COMMENT 'port of the mfpModuleManager interface return',
  `login_context` VARCHAR(128) COMMENT 'siyin login url location',
  `trademark` VARCHAR(128) COMMENT 'trade mark',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `qrcode_logo_uri` VARCHAR(500),
  `qrcode_logo_url` VARCHAR(500),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_siyin_print_records`;


CREATE TABLE `eh_siyin_print_records` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) COMMENT 'community',
  `owner_id` BIGINT,
  `order_id` BIGINT,
  `job_id` VARCHAR(128) COMMENT 'siyin returned, uniqueness flag',
  `job_status` VARCHAR(128) COMMENT 'siyin returned, siyin job status',
  `group_name` VARCHAR(128) COMMENT 'siyin returned, user group name',
  `user_display_name` VARCHAR(128) COMMENT 'siyin returned, user display name',
  `client_ip` VARCHAR(128) COMMENT 'siyin returned, documents sended source commputer ip',
  `client_name` VARCHAR(128) COMMENT 'siyin returned, documents sended source commputer name',
  `client_mac` VARCHAR(128) COMMENT 'siyin returned, documents sended source commputer mac',
  `driver_name` VARCHAR(128) COMMENT 'siyin returned, driver name',
  `job_type` TINYINT COMMENT 'siyin returned,PRINT(1),COPY(2),SCAN(3)',
  `start_time` VARCHAR(128) COMMENT 'siyin returned, job start time',
  `end_time` VARCHAR(128) COMMENT 'siyin returned, job end time',
  `document_name` VARCHAR(512) COMMENT 'siyin returned, print document name',
  `printer_name` VARCHAR(256) COMMENT 'siyin returned, printer name',
  `paper_size` TINYINT COMMENT 'siyin returned, paper size, A3(3),A4(4),A5(5),A6(6)',
  `duplex` TINYINT COMMENT 'siyin returned, 1,one surface,2:DOUBLE surface',
  `copy_count` INTEGER COMMENT 'siyin returned, copy count',
  `surface_count` INTEGER COMMENT 'siyin returned, surface count',
  `color_surface_count` INTEGER,
  `mono_surface_count` INTEGER,
  `page_count` INTEGER,
  `color_page_count` INTEGER,
  `mono_page_count` INTEGER,
  `status` TINYINT COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT COMMENT 'creator/initiator id',
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_siyin_print_settings`;


CREATE TABLE `eh_siyin_print_settings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `setting_type` TINYINT COMMENT '1(scan/print/copy),2(course/hotline)',
  `job_type` TINYINT COMMENT 'job type, PRINT(1),COPY(2),SCAN(3)',
  `paper_size` TINYINT COMMENT 'paper size, A3(3),A4(4),A5(5),A6(6)',
  `black_white_price` DECIMAL(10,2) COMMENT 'black white price',
  `color_price` DECIMAL(10,2) COMMENT 'color price',
  `hotline` VARCHAR(32) COMMENT 'contact number',
  `print_course` TEXT COMMENT 'print course',
  `scan_copy_course` TEXT COMMENT 'scan or copy course',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_siyin_user_printer_mappings`;


CREATE TABLE `eh_siyin_user_printer_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `user_id` BIGINT,
  `reader_name` VARCHAR(128) COMMENT 'printer reader name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0:INACTIVE,2:ACTIVE',
  `unlock_times` BIGINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_sms_black_lists`;


CREATE TABLE `eh_sms_black_lists` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `contact_token` VARCHAR(32) NOT NULL COMMENT 'contact token',
  `reason` VARCHAR(128) COMMENT 'reason',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: pass, 1: block',
  `create_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: Created by system, 1: Manually created',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_contact_token` (`contact_token`)
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
  `handler` VARCHAR(128) NOT NULL COMMENT 'YunZhiXun, YouXunTong, LianXinTong',
  `sms_id` VARCHAR(128) COMMENT 'sms identifier',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: send success, 2: send failed, 4: report success, 5: report failed',
  `report_text` TEXT COMMENT 'report text',
  `report_time` DATETIME(3),

  PRIMARY KEY (`id`),
  KEY `i_eh_mobile_handler` (`mobile`,`handler`),
  KEY `i_sms_id_handler` (`sms_id`,`handler`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_bases`;


CREATE TABLE `eh_social_security_bases` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `city_id` BIGINT DEFAULT 0,
  `household_type` VARCHAR(32) COMMENT '户籍类型',
  `pay_item` VARCHAR(32) COMMENT '缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗',
  `company_radix_min` DECIMAL(10,2) COMMENT '企业基数最小值',
  `company_radix_max` DECIMAL(10,2) COMMENT '企业基数最大值',
  `company_ratio_min` INTEGER COMMENT '企业比例最小值 万分之 eq:100=1%;1=0.01%',
  `company_ratio_max` INTEGER COMMENT '企业比例最大值 万分之 eq:100=1%;1=0.01%',
  `employee_radix_min` DECIMAL(10,2) COMMENT '个人基数最小值',
  `employee_radix_max` DECIMAL(10,2) COMMENT ' 个人基数最大值',
  `employee_ratio_min` INTEGER COMMENT '个人比例最小值 万分之 eq:100=1%;1=0.01%',
  `employee_ratio_max` INTEGER COMMENT '个人比例最大值 万分之 eq:100=1%;1=0.01%',
  `editable_flag` TINYINT COMMENT '是否可编辑',
  `is_default` TINYINT COMMENT '是否是默认选项(1-是:普通社保;0-否:补充保险)',
  `effect_time_begin` DATETIME COMMENT '生效起始日期',
  `effect_time_end` DATETIME COMMENT '生效结束日期',
  `ratio_options` TEXT COMMENT '比例可选项,如果为null就是手动填写:eq:[120,230,380,480,520]',
  `accum_or_socail` TINYINT COMMENT '社保/公积金标识 : 1-社保 2-公积金',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_city_id` (`city_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_department_summary`;


CREATE TABLE `eh_social_security_department_summary` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `owner_id` BIGINT COMMENT '所属企业',
  `dept_id` BIGINT COMMENT 'user department id',
  `dept_name` VARCHAR(128) COMMENT 'user department name',
  `pay_month` VARCHAR(8) COMMENT 'yyyymm',
  `employee_count` INTEGER COMMENT '人数',
  `social_security_sum` DECIMAL(10,2) COMMENT '社保合计',
  `social_security_company_sum` DECIMAL(10,2) COMMENT '社保企业合计',
  `social_security_employee_sum` DECIMAL(10,2) COMMENT '社保个人合计',
  `pension_company_sum` DECIMAL(10,2) COMMENT '养老企业合计',
  `pension_employee_sum` DECIMAL(10,2) COMMENT '养老个人合计',
  `medical_company_sum` DECIMAL(10,2) COMMENT '医疗企业合计',
  `medical_employee_sum` DECIMAL(10,2) COMMENT '医疗个人合计',
  `injury_company_sum` DECIMAL(10,2) COMMENT '工伤企业合计',
  `injury_employee_sum` DECIMAL(10,2) COMMENT '工伤个人合计',
  `unemployment_company_sum` DECIMAL(10,2) COMMENT '失业企业合计',
  `unemployment_employee_sum` DECIMAL(10,2) COMMENT '失业个人合计',
  `birth_company_sum` DECIMAL(10,2) COMMENT '生育企业合计',
  `birth_employee_sum` DECIMAL(10,2) COMMENT '生育个人合计',
  `critical_illness_company_sum` DECIMAL(10,2) COMMENT '大病企业合计',
  `critical_illness_employee_sum` DECIMAL(10,2) COMMENT '大病个人合计',
  `after_social_security_company_sum` DECIMAL(10,2) COMMENT '补缴社保企业合计',
  `after_social_security_employee_sum` DECIMAL(10,2) COMMENT '补缴社保个人合计',
  `after_pension_company_sum` DECIMAL(10,2) COMMENT '补缴养老企业合计',
  `after_pension_employee_sum` DECIMAL(10,2) COMMENT '补缴养老个人合计',
  `after_medical_company_sum` DECIMAL(10,2) COMMENT '补缴医疗企业合计',
  `after_medical_employee_sum` DECIMAL(10,2) COMMENT '补缴医疗个人合计',
  `after_injury_company_sum` DECIMAL(10,2) COMMENT '补缴工伤企业合计',
  `after_injury_employee_sum` DECIMAL(10,2) COMMENT '补缴工伤个人合计',
  `after_unemployment_company_sum` DECIMAL(10,2) COMMENT '补缴失业企业合计',
  `after_unemployment_employee_sum` DECIMAL(10,2) COMMENT '补缴失业个人合计',
  `after_birth_company_sum` DECIMAL(10,2) COMMENT '补缴生育企业合计',
  `after_birth_employee_sum` DECIMAL(10,2) COMMENT '补缴生育个人合计',
  `after_critical_illness_company_sum` DECIMAL(10,2) COMMENT '补缴大病企业合计',
  `after_critical_illness_employee_sum` DECIMAL(10,2) COMMENT '补缴大病个人合计',
  `disability_sum` DECIMAL(10,2) COMMENT '残障金',
  `commercial_insurance` DECIMAL(10,2) COMMENT '商业保险',
  `accumulation_fund_sum` DECIMAL(10,2) COMMENT '公积金合计',
  `accumulation_fund_company_sum` DECIMAL(10,2) COMMENT '公积金企业合计',
  `accumulation_fund_employee_sum` DECIMAL(10,2) COMMENT '公积金个人合计',
  `after_accumulation_fund_company_sum` DECIMAL(10,2) COMMENT '补缴公积金企业合计',
  `after_accumulation_fund_employee_sum` DECIMAL(10,2) COMMENT '补缴公积金个人合计',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `file_uid` BIGINT COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_id` (`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_groups`;


CREATE TABLE `eh_social_security_groups` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `organization_id` BIGINT,
  `pay_month` VARCHAR(8) COMMENT 'yyyymm',
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `file_uid` BIGINT COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_inout_log`;


CREATE TABLE `eh_social_security_inout_log` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `type` TINYINT NOT NULL COMMENT '0社保增员1社保减员2公积金增员3公积金减员4社保补缴5公积金补缴',
  `log_month` VARCHAR(8) COMMENT 'the start month, yyyyMM',
  `log_date` DATE COMMENT 'log具体日期',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_inout_report`;


CREATE TABLE `eh_social_security_inout_report` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `user_name` VARCHAR(128) COMMENT '姓名',
  `entry_date` DATE COMMENT '入职日期',
  `out_work_date` DATE COMMENT '离职日期',
  `contact_token` VARCHAR(128) COMMENT '手机号',
  `id_number` VARCHAR(128) COMMENT '身份证号',
  `degree` VARCHAR(64) COMMENT '学历',
  `salary_card_bank` VARCHAR(64) COMMENT '开户行',
  `salary_card_number` VARCHAR(128) COMMENT '工资卡号',
  `dept_name` VARCHAR(128) COMMENT '部门',
  `social_security_number` VARCHAR(128) COMMENT '社保号',
  `provident_fund_number` VARCHAR(128) COMMENT '公积金号',
  `household_type` VARCHAR(32) COMMENT '户籍类型',
  `social_security_city_id` BIGINT COMMENT '参保城市id',
  `social_security_city_name` VARCHAR(32) COMMENT '参保城市',
  `pay_month` VARCHAR(8) COMMENT '社保月份',
  `social_security_radix` DECIMAL(10,2) COMMENT '社保基数',
  `social_security_increase` VARCHAR(8) COMMENT '社保增',
  `social_security_decrease` VARCHAR(8) COMMENT '社保减',
  `social_security_after` DECIMAL(10,2) COMMENT '社保补缴',
  `accumulation_fund_city_id` BIGINT COMMENT '公积金城市id',
  `accumulation_fund_city_name` VARCHAR(32) COMMENT '公积金城市',
  `accumulation_fund_radix` DECIMAL(10,2) COMMENT '公积金基数',
  `accumulation_fund_increase` VARCHAR(8) COMMENT '公积金增',
  `accumulation_fund_decrease` VARCHAR(8) COMMENT '公积金减',
  `accumulation_fund_after` DECIMAL(10,2) COMMENT '公积金补缴',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `file_uid` BIGINT COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_inout_time`;


CREATE TABLE `eh_social_security_inout_time` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `type` TINYINT NOT NULL COMMENT '0-SOCIAL SECURITY, 1-ACCUMULATION FUND',
  `start_month` VARCHAR(8) COMMENT 'the start month, yyyyMM',
  `end_month` VARCHAR(8) COMMENT 'the end month, yyyyMM',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_payment_logs`;


CREATE TABLE `eh_social_security_payment_logs` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `city_id` BIGINT DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `pay_month` VARCHAR(8) COMMENT 'yyyymm',
  `after_pay_flag` TINYINT DEFAULT 0 COMMENT '补缴标记',
  `household_type` VARCHAR(32) COMMENT '户籍类型',
  `pay_item` VARCHAR(32) COMMENT '缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗',
  `company_radix` DECIMAL(10,2) COMMENT '企业基数',
  `company_ratio` INTEGER COMMENT '企业比例万分之 eq:100=1%;1=0.01%',
  `employee_radix` DECIMAL(10,2) COMMENT '个人基数',
  `employee_ratio` INTEGER COMMENT '个人比例 万分之 eq:100=1%;1=0.01%',
  `editable_flag` TINYINT COMMENT '是否可编辑',
  `is_default` TINYINT COMMENT '是否是默认选项(1-是:普通社保;0-否:补充保险)',
  `accum_or_socail` TINYINT COMMENT '社保/公积金标识 : 1-社保 2-公积金',
  `is_new` TINYINT COMMENT '增减员:0正常,1增员,-1减员',
  `is_work` TINYINT COMMENT '入职离职:0正常,1入职,-1离职',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `file_uid` BIGINT COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_payments`;


CREATE TABLE `eh_social_security_payments` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `city_id` BIGINT DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `pay_month` VARCHAR(8) NOT NULL COMMENT 'yyyymm',
  `after_pay_flag` TINYINT DEFAULT 0 COMMENT '补缴标记',
  `household_type` VARCHAR(32) COMMENT '户籍类型',
  `pay_item` VARCHAR(32) COMMENT '缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗',
  `company_radix` DECIMAL(10,2) COMMENT '企业基数',
  `company_ratio` INTEGER COMMENT '企业比例万分之 eq:100=1%;1=0.01%',
  `employee_radix` DECIMAL(10,2) COMMENT '个人基数',
  `employee_ratio` INTEGER COMMENT '个人比例 万分之 eq:100=1%;1=0.01%',
  `editable_flag` TINYINT COMMENT '是否可编辑',
  `is_default` TINYINT COMMENT '是否是默认选项(1-是:普通社保;0-否:补充保险)',
  `accum_or_socail` TINYINT COMMENT '社保/公积金标识 : 1-社保 2-公积金',
  `is_new` TINYINT COMMENT '增减员:0正常,1增员,-1减员',
  `is_work` TINYINT COMMENT '入职离职:0正常,1入职,-1离职',
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `file_uid` BIGINT COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_report`;


CREATE TABLE `eh_social_security_report` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `user_name` VARCHAR(128) COMMENT '姓名',
  `entry_date` DATE COMMENT '入职日期',
  `contact_token` VARCHAR(128) COMMENT '手机号',
  `id_number` VARCHAR(128) COMMENT '身份证号',
  `degree` VARCHAR(64) COMMENT '学历',
  `salary_card_bank` VARCHAR(64) COMMENT '开户行',
  `salary_card_number` VARCHAR(128) COMMENT '工资卡号',
  `dept_name` VARCHAR(128) COMMENT '部门',
  `social_security_number` VARCHAR(128) COMMENT '社保号',
  `provident_fund_number` VARCHAR(128) COMMENT '公积金号',
  `out_work_date` DATE COMMENT '离职日期',
  `household_type` VARCHAR(32) COMMENT '户籍类型',
  `social_security_city_id` BIGINT COMMENT '参保城市id',
  `social_security_city_name` VARCHAR(32) COMMENT '参保城市',
  `pay_month` VARCHAR(8) COMMENT '社保月份',
  `social_security_radix` DECIMAL(10,2) COMMENT '社保基数',
  `social_security_sum` DECIMAL(10,2) COMMENT '社保合计',
  `social_security_company_sum` DECIMAL(10,2) COMMENT '社保企业合计',
  `social_security_employee_sum` DECIMAL(10,2) COMMENT '社保个人合计',
  `accumulation_fund_city_id` BIGINT COMMENT '公积金城市id',
  `accumulation_fund_city_name` VARCHAR(32) COMMENT '公积金城市',
  `accumulation_fund_radix` DECIMAL(10,2) COMMENT '公积金基数',
  `accumulation_fund_company_radix` DECIMAL(10,2) COMMENT '公积金企业基数',
  `accumulation_fund_company_ratio` INTEGER COMMENT '公积金企业比例万分之 eq:100=1%;1=0.01%',
  `accumulation_fund_employee_radix` DECIMAL(10,2) COMMENT '公积金个人基数',
  `accumulation_fund_employee_ratio` INTEGER COMMENT '公积金个人比例 万分之 eq:100=1%;1=0.01%',
  `accumulation_fund_sum` DECIMAL(10,2) COMMENT '公积金合计',
  `accumulation_fund_company_sum` DECIMAL(10,2) COMMENT '公积金企业合计',
  `accumulation_fund_employee_sum` DECIMAL(10,2) COMMENT '公积金个人合计',
  `accumulation_fund_tax` DECIMAL(10,2) COMMENT '公积金需纳税额',
  `pension_company_radix` DECIMAL(10,2) COMMENT '养老保险企业基数',
  `pension_company_ratio` INTEGER COMMENT '养老保险企业比例万分之 eq:100=1%;1=0.01%',
  `pension_employee_radix` DECIMAL(10,2) COMMENT '养老保险个人基数',
  `pension_employee_ratio` INTEGER COMMENT '养老保险个人比例 万分之 eq:100=1%;1=0.01%',
  `pension_company_sum` DECIMAL(10,2) COMMENT '养老保险企业合计',
  `pension_employee_sum` DECIMAL(10,2) COMMENT '养老保险个人合计',
  `medical_company_radix` DECIMAL(10,2) COMMENT '医疗保险企业基数',
  `medical_company_ratio` INTEGER COMMENT '医疗保险企业比例万分之 eq:100=1%;1=0.01%',
  `medical_employee_radix` DECIMAL(10,2) COMMENT '医疗保险个人基数',
  `medical_employee_ratio` INTEGER COMMENT '医疗保险个人比例 万分之 eq:100=1%;1=0.01%',
  `medical_company_sum` DECIMAL(10,2) COMMENT '医疗保险企业合计',
  `medical_employee_sum` DECIMAL(10,2) COMMENT '医疗保险个人合计',
  `injury_company_radix` DECIMAL(10,2) COMMENT '工伤保险企业基数',
  `injury_company_ratio` INTEGER COMMENT '工伤保险企业比例万分之 eq:100=1%;1=0.01%',
  `injury_employee_radix` DECIMAL(10,2) COMMENT '工伤保险个人基数',
  `injury_employee_ratio` INTEGER COMMENT '工伤保险个人比例 万分之 eq:100=1%;1=0.01%',
  `injury_company_sum` DECIMAL(10,2) COMMENT '工伤保险企业合计',
  `injury_employee_sum` DECIMAL(10,2) COMMENT '工伤保险个人合计',
  `unemployment_company_radix` DECIMAL(10,2) COMMENT '失业保险企业基数',
  `unemployment_company_ratio` INTEGER COMMENT '失业保险企业比例万分之 eq:100=1%;1=0.01%',
  `unemployment_employee_radix` DECIMAL(10,2) COMMENT '失业保险个人基数',
  `unemployment_employee_ratio` INTEGER COMMENT '失业保险个人比例 万分之 eq:100=1%;1=0.01%',
  `unemployment_company_sum` DECIMAL(10,2) COMMENT '失业保险企业合计',
  `unemployment_employee_sum` DECIMAL(10,2) COMMENT '失业保险个人合计',
  `birth_company_radix` DECIMAL(10,2) COMMENT '生育保险企业基数',
  `birth_company_ratio` INTEGER COMMENT '生育保险企业比例万分之 eq:100=1%;1=0.01%',
  `birth_employee_radix` DECIMAL(10,2) COMMENT '生育保险个人基数',
  `birth_employee_ratio` INTEGER COMMENT '生育保险个人比例 万分之 eq:100=1%;1=0.01%',
  `birth_company_sum` DECIMAL(10,2) COMMENT '生育保险企业合计',
  `birth_employee_sum` DECIMAL(10,2) COMMENT '生育保险个人合计',
  `critical_illness_company_radix` DECIMAL(10,2) COMMENT '大病保险企业基数',
  `critical_illness_company_ratio` INTEGER COMMENT '大病保险企业比例万分之 eq:100=1%;1=0.01%',
  `critical_illness_employee_radix` DECIMAL(10,2) COMMENT '大病保险个人基数',
  `critical_illness_employee_ratio` INTEGER COMMENT '大病保险个人比例 万分之 eq:100=1%;1=0.01%',
  `critical_illness_company_sum` DECIMAL(10,2) COMMENT '大病保险企业合计',
  `critical_illness_employee_sum` DECIMAL(10,2) COMMENT '大病保险个人合计',
  `after_social_security_company_sum` DECIMAL(10,2) COMMENT '补缴社保企业合计',
  `after_social_security_employee_sum` DECIMAL(10,2) COMMENT '补缴社保个人合计',
  `after_accumulation_fund_company_sum` DECIMAL(10,2) COMMENT '补缴公积金企业合计',
  `after_accumulation_fund_employee_sum` DECIMAL(10,2) COMMENT '补缴公积金个人合计',
  `after_pension_company_sum` DECIMAL(10,2) COMMENT '补缴养老企业合计',
  `after_pension_employee_sum` DECIMAL(10,2) COMMENT '补缴养老个人合计',
  `after_medical_company_sum` DECIMAL(10,2) COMMENT '补缴医疗企业合计',
  `after_medical_employee_sum` DECIMAL(10,2) COMMENT '补缴医疗个人合计',
  `after_injury_company_sum` DECIMAL(10,2) COMMENT '补缴工伤企业合计',
  `after_injury_employee_sum` DECIMAL(10,2) COMMENT '补缴工伤个人合计',
  `after_unemployment_company_sum` DECIMAL(10,2) COMMENT '补缴失业企业合计',
  `after_unemployment_employee_sum` DECIMAL(10,2) COMMENT '补缴失业个人合计',
  `after_birth_company_sum` DECIMAL(10,2) COMMENT '补缴生育企业合计',
  `after_birth_employee_sum` DECIMAL(10,2) COMMENT '补缴生育个人合计',
  `after_critical_illness_company_sum` DECIMAL(10,2) COMMENT '补缴大病企业合计',
  `after_critical_illness_employee_sum` DECIMAL(10,2) COMMENT '补缴大病个人合计',
  `disability_sum` DECIMAL(10,2) COMMENT '残障金',
  `commercial_insurance` DECIMAL(10,2) COMMENT '商业保险',
  `is_work` TINYINT COMMENT '入职离职:0正常,1入职,-1离职',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `file_uid` BIGINT COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_settings`;


CREATE TABLE `eh_social_security_settings` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `city_id` BIGINT DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `user_id` BIGINT,
  `detail_id` BIGINT,
  `household_type` VARCHAR(32) COMMENT '户籍类型',
  `pay_item` VARCHAR(32) COMMENT '缴费项:医疗/养老/失业/工伤/生育/大病/残障金/补充医疗',
  `radix` DECIMAL(10,2) COMMENT '基数',
  `company_radix` DECIMAL(10,2) COMMENT '企业基数',
  `company_ratio` INTEGER COMMENT '企业比例万分之 eq:100=1%;1=0.01%',
  `employee_radix` DECIMAL(10,2) COMMENT '个人基数',
  `employee_ratio` INTEGER COMMENT '个人比例 万分之 eq:100=1%;1=0.01%',
  `editable_flag` TINYINT COMMENT '是否可编辑',
  `is_default` TINYINT COMMENT '是否是默认选项(1-是:普通社保;0-否:补充保险)',
  `accum_or_socail` TINYINT COMMENT '社保/公积金标识 : 1-社保 2-公积金',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`),
  KEY `i_eh_user_detail_id` (`detail_id`),
  KEY `i_eh_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_social_security_summary`;


CREATE TABLE `eh_social_security_summary` (
  `id` BIGINT NOT NULL DEFAULT 0,
  `namespace_id` INTEGER DEFAULT 0,
  `organization_id` BIGINT,
  `pay_month` VARCHAR(8) COMMENT 'yyyymm',
  `company_payment` DECIMAL(10,2) COMMENT '企业缴纳',
  `employee_payment` DECIMAL(10,2) COMMENT '个人缴纳',
  `creator_uid` BIGINT DEFAULT 0,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `is_filed` TINYINT DEFAULT 0 COMMENT '是否归档 0-否 1是',
  `file_uid` BIGINT COMMENT '归档人',
  `file_time` DATETIME COMMENT '归档时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_organization_id` (`organization_id`)
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

--
-- App日志附件
--
DROP TABLE IF EXISTS `eh_stat_event_app_attachment_logs`;


CREATE TABLE `eh_stat_event_app_attachment_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 事件记录表
--
DROP TABLE IF EXISTS `eh_stat_event_content_logs`;


CREATE TABLE `eh_stat_event_content_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `content` TEXT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '1: loaded to eh_stat_event_logs, 2: did not load to eh_stat_event_logs',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 设备日志表  add by xq.tian  2017/08/28
--
DROP TABLE IF EXISTS `eh_stat_event_device_logs`;


CREATE TABLE `eh_stat_event_device_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `app_version` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'app version',
  `device_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'device id',
  `device_brand` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'brand',
  `device_model` VARCHAR(64) DEFAULT '' COMMENT 'cellPhone model model',
  `os_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: Android, 2: ios',
  `os_version` VARCHAR(64) DEFAULT '' COMMENT 'system version',
  `access` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'access',
  `imei` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'imei',
  `client_ip` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'ip address',
  `server_ip` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'ip address',
  `country` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'country',
  `language` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'language',
  `mc` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'mc',
  `resolution` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'resolution',
  `timezone` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'timezone',
  `carrier` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'carrier',
  `version_realm` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'version_realm',
  `device_time` BIGINT NOT NULL DEFAULT 0 COMMENT 'device time',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 事件日志表
--
DROP TABLE IF EXISTS `eh_stat_event_logs`;


CREATE TABLE `eh_stat_event_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_version` VARCHAR(32) NOT NULL DEFAULT 1,
  `device_gen_id` BIGINT NOT NULL COMMENT 'id of device generate',
  `device_time` BIGINT NOT NULL COMMENT 'device time',
  `acc` INTEGER COMMENT 'acc',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `upload_time` DATETIME(3),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 事件参数日志表
--
DROP TABLE IF EXISTS `eh_stat_event_param_logs`;


CREATE TABLE `eh_stat_event_param_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_version` VARCHAR(32) NOT NULL DEFAULT 1,
  `event_log_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_stat_event_logs id',
  `param_key` VARCHAR(64) COMMENT 'key',
  `string_value` VARCHAR(64) COMMENT 'string value',
  `number_value` INTEGER COMMENT 'number value',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `upload_time` DATETIME(3),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 事件参数表
--
DROP TABLE IF EXISTS `eh_stat_event_params`;


CREATE TABLE `eh_stat_event_params` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `event_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '1: general event',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_version` VARCHAR(32) NOT NULL DEFAULT 1,
  `multiple` INTEGER NOT NULL DEFAULT 1 COMMENT 'multiple',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `param_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: string param, 2: value param',
  `param_key` VARCHAR(64) NOT NULL COMMENT 'param key',
  `param_name` VARCHAR(64) NOT NULL COMMENT 'param name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 配置表
--
DROP TABLE IF EXISTS `eh_stat_event_portal_configs`;


CREATE TABLE `eh_stat_event_portal_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `config_type` TINYINT NOT NULL COMMENT '1: 顶部工具栏的子项',
  `config_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'config name',
  `identifier` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'identifier',
  `display_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'config display name',
  `description` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 配置历史记录
--
DROP TABLE IF EXISTS `eh_stat_event_portal_statistics`;


CREATE TABLE `eh_stat_event_portal_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'owner type',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id',
  `stat_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: 门户, 2: 门户的子项, 3: 底部导航栏的子项, 4: 顶部工具栏的子项',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'name',
  `identifier` VARCHAR(64) NOT NULL COMMENT 'identifier',
  `display_name` VARCHAR(64) NOT NULL COMMENT 'display_name',
  `time_interval` VARCHAR(32) NOT NULL COMMENT 'HOURLY, DAILY, WEEKLY, MONTHLY',
  `stat_date` DATE NOT NULL COMMENT 'stat date',
  `description` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  `scene_type` VARCHAR(64) COMMENT 'default, pm_admin, park_tourist...',
  `widget` VARCHAR(64) COMMENT 'Navigator, Banner, OPPush...',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 事件统计表
--
DROP TABLE IF EXISTS `eh_stat_event_statistics`;


CREATE TABLE `eh_stat_event_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_version` VARCHAR(32) NOT NULL DEFAULT 1,
  `event_display_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'event display name',
  `event_portal_stat_id` BIGINT NOT NULL COMMENT 'ref eh_stat_event_portal_statistics id',
  `time_interval` VARCHAR(32) NOT NULL COMMENT 'HOURLY, DAILY, WEEKLY, MONTHLY',
  `stat_date` DATE NOT NULL COMMENT 'stat date',
  `total_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'event total count',
  `unique_users` BIGINT NOT NULL DEFAULT 0 COMMENT 'unique users',
  `completed_sessions` BIGINT NOT NULL DEFAULT 0 COMMENT 'completed sessions',
  `param` TEXT COMMENT 'param',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_stat_event_task_logs`;


CREATE TABLE `eh_stat_event_task_logs` (
  `id` BIGINT NOT NULL,
  `task_date` DATE NOT NULL,
  `step_name` VARCHAR(256) NOT NULL,
  `status` VARCHAR(32) NOT NULL,
  `task_meta` MEDIUMTEXT,
  `exception_stacktrace` TEXT,
  `duration_seconds` INTEGER,
  `update_Time` DATETIME(3),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 日志上传策略表
--
DROP TABLE IF EXISTS `eh_stat_event_upload_strategies`;


CREATE TABLE `eh_stat_event_upload_strategies` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'e.g: EhUsers',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner identifier',
  `access` VARCHAR(32) NOT NULL COMMENT 'WIFI, GSM',
  `log_type` TINYINT NOT NULL COMMENT '1: GENERAL_EVENT, 2: CRASH_LOG, 3:ERROR_LOG',
  `strategy` TINYINT NOT NULL COMMENT '0: NO, 1: INTERVAL, 2: IMMEDIATE, 3: TIMES_PER_DAY',
  `interval_seconds` INTEGER COMMENT 'interval seconds',
  `times_per_day` INTEGER COMMENT 'times_per_day',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 事件表
--
DROP TABLE IF EXISTS `eh_stat_events`;


CREATE TABLE `eh_stat_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `event_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '1: general event',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_version` VARCHAR(32) NOT NULL DEFAULT 1,
  `event_display_name` VARCHAR(64) NOT NULL COMMENT 'event name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
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

DROP TABLE IF EXISTS `eh_sync_data_errors`;


CREATE TABLE `eh_sync_data_errors` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `module_id` BIGINT,
  `sync_type` VARCHAR(64) NOT NULL COMMENT '同步类型，对应同步的任务类型，如sync_contract',
  `owner_id` BIGINT NOT NULL COMMENT '错误对应的id，如：contractId，对应同步的任务类型',
  `owner_type` VARCHAR(64),
  `error_message` VARCHAR(512) NOT NULL COMMENT '发生错误的信息',
  `task_id` BIGINT NOT NULL COMMENT '同步版本',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_sync_data_tasks`;


CREATE TABLE `eh_sync_data_tasks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `type` VARCHAR(64) NOT NULL DEFAULT '',
  `status` TINYINT NOT NULL,
  `result` LONGTEXT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `view_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否被查看',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_sync_offline_tasks`;


CREATE TABLE `eh_sync_offline_tasks` (
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_talent_message_senders`;


CREATE TABLE `eh_talent_message_senders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `organization_member_id` BIGINT NOT NULL,
  `user_id` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_talent_requests`;


CREATE TABLE `eh_talent_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `requestor` VARCHAR(64),
  `phone` VARCHAR(64),
  `organization_name` VARCHAR(128),
  `content` TEXT,
  `talent_id` BIGINT,
  `form_origin_id` BIGINT,
  `flow_case_id` BIGINT,
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_tasks`;
CREATE TABLE `eh_tasks` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `community_id` BIGINT,
  `org_id` BIGINT,
  `user_id` BIGINT COMMENT 'owner',
  `name` VARCHAR(255),
  `type` TINYINT COMMENT '0-default,1-filedownload',
  `class_name` VARCHAR(255),
  `params` TEXT,
  `repeat_flag` TINYINT,
  `process` INTEGER COMMENT 'rate of progress',
  `result_string1` VARCHAR(255),
  `result_string2` VARCHAR(255),
  `result_long1` BIGINT,
  `result_long2` BIGINT,
  `status` TINYINT,
  `error_description` VARCHAR(255),
  `create_time` DATETIME NOT NULL,
  `finish_time` DATETIME,
  `update_time` DATETIME,
  `execute_start_time` DATETIME,
  `upload_file_start_time` DATETIME,
  `upload_file_finish_time` DATETIME,
  `read_status` TINYINT COMMENT '阅读状态',
  `download_times` INTEGER COMMENT '下载次数',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  PRIMARY KEY (`id`),
  KEY `i_eh_imei_number` (`imei_number`)
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
  PRIMARY KEY (`id`),
  KEY `i_eh_imei_number` (`imei_number`)
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
  `average_active_user_number` BIGINT NOT NULL DEFAULT 0,
  `average_active_user_change_rate` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
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
  `cumulative_active_user_number` BIGINT NOT NULL DEFAULT 0,
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
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_task_no_namespace_id` (`task_no`,`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_thirdpart_configurations`;


CREATE TABLE `eh_thirdpart_configurations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `name` VARCHAR(64),
  `value` VARCHAR(512) NOT NULL,
  `description` VARCHAR(256),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
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


DROP TABLE IF EXISTS `eh_tracking_notify_logs`;

CREATE TABLE `eh_tracking_notify_logs` (
  `id` BIGINT NOT NULL,
  `customer_type` TINYINT NOT NULL,
  `customer_id` BIGINT NOT NULL,
  `notify_text` TEXT,
  `receiver_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `status` TINYINT NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_uniongroup_configures`;


CREATE TABLE `eh_uniongroup_configures` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_id` BIGINT DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` BIGINT NOT NULL COMMENT 'id of group',
  `current_id` BIGINT COMMENT 'id of target, organization or memberDetail',
  `current_type` VARCHAR(32) COMMENT 'organziation,memberDetail',
  `current_name` VARCHAR(132) COMMENT 'name',
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `version_code` INTEGER DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_uniongroup_member_details`;


CREATE TABLE `eh_uniongroup_member_details` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `group_id` BIGINT NOT NULL COMMENT 'id of group',
  `detail_id` BIGINT COMMENT 'id of target, only memberDetail',
  `target_type` VARCHAR(64),
  `target_id` BIGINT NOT NULL,
  `enterprise_id` BIGINT NOT NULL COMMENT 'enterprise_id',
  `contact_name` VARCHAR(164) COMMENT 'the name of the member',
  `contact_token` VARCHAR(128) COMMENT 'phone number, reference for eh_organization_member contact_token',
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  `version_code` INTEGER DEFAULT 0 COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniongroup_member_uniqueIndex` (`group_type`,`group_id`,`detail_id`,`contact_token`,`version_code`),
  KEY `index_detailId` (`detail_id`),
  KEY `i_eh_namespace_detail_id_version_code` (`namespace_id`,`group_type`,`detail_id`,`version_code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 维护uniongroup现在使用的是哪个version
DROP TABLE IF EXISTS `eh_uniongroup_version`;

CREATE TABLE `eh_uniongroup_version` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `enterprise_id` BIGINT DEFAULT 0,
  `group_type` VARCHAR(32) COMMENT 'SalaryGroup,PunchGroup',
  `current_version_code` INTEGER DEFAULT 0 COMMENT '当前使用的版本号 从1开始 , 0默认是config版本',
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  PRIMARY KEY (`id`),
  KEY `user_activitie_user_id` (`uid`),
  KEY `i_eh_namespace_id` (`namespace_id`) USING HASH,
  KEY `i_eh_imei_number` (`imei_number`) USING HASH,
  KEY `i_eh_app_version_name` (`app_version_name`),
  KEY `i_eh_create_time` (`create_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_user_appeal_logs`;


CREATE TABLE `eh_user_appeal_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `identifier_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `old_identifier` VARCHAR(128),
  `old_region_code` INTEGER DEFAULT '86' COMMENT 'region code 86 852',
  `new_identifier` VARCHAR(128),
  `new_region_code` INTEGER DEFAULT '86' COMMENT 'region code 86 852',
  `name` VARCHAR(128) COMMENT 'user name',
  `email` VARCHAR(128) COMMENT 'user email',
  `remarks` VARCHAR(512) COMMENT 'remarks',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_user_behaviors`;


CREATE TABLE `eh_user_behaviors` (
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
-- ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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

DROP TABLE IF EXISTS `eh_user_current_scene`;


CREATE TABLE `eh_user_current_scene` (
  `id` bigint(32) NOT NULL COMMENT '主键',
  `uid` bigint(32) NOT NULL COMMENT '用户ID',
  `namespace_id` INTEGER COMMENT '域空间ID',
  `community_id` bigint(32) COMMENT '园区ID',
  `community_type` TINYINT COMMENT '园区类型',
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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

DROP TABLE IF EXISTS `eh_user_identifier_logs`;


CREATE TABLE `eh_user_identifier_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
  `identifier_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
  `identifier_token` VARCHAR(128),
  `verification_code` VARCHAR(16),
  `claim_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: free standing, 1: claiming, 2: claim verifying, 3: claimed',
  `region_code` INTEGER NOT NULL DEFAULT '86' COMMENT 'region code 86 852',
  `notify_time` DATETIME(3),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `item_name` VARCHAR(32),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_user_organizations`;


CREATE TABLE `eh_user_organizations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `user_id` BIGINT NOT NULL,
  `organization_id` BIGINT DEFAULT 0,
  `group_path` VARCHAR(128) COMMENT 'refer to the organization path',
  `group_type` VARCHAR(64) COMMENT 'ENTERPRISE, DEPARTMENT, GROUP, JOB_POSITION, JOB_LEVEL, MANAGER',
  `status` TINYINT COMMENT '0: inactive, 1: confirming, 2: active',
  `namespace_id` INTEGER DEFAULT 0,
  `create_time` DATETIME,
  `visible_flag` TINYINT DEFAULT 0 COMMENT '0 show 1 hide',
  `update_time` DATETIME,
  `executive_tag` TINYINT,
  `position_tag` VARCHAR(128),
  PRIMARY KEY (`id`),
  KEY `user_organization_user_id` (`user_id`),
  KEY `user_organization_organization_id` (`organization_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `third_data` VARCHAR(2048) COMMENT 'third_data for AnBang',
  `show_company_flag` TINYINT COMMENT '是否展示公司名称',
  `company_id` BIGINT COMMENT '公司ID',
  `vip_level` INTEGER COMMENT '会员等级',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_uuid` (`uuid`),
  UNIQUE KEY `u_eh_user_account_name` (`account_name`),
  KEY `i_eh_user_create_time` (`create_time`),
  KEY `i_eh_user_nick_name` (`nick_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_var_field_group_ranges`;


CREATE TABLE `eh_var_field_group_ranges` (
  `id` BIGINT NOT NULL,
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `module_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '一组公用表单的类型',
  PRIMARY KEY (`id`),
  KEY `idx_module_name` (`module_name`),
  KEY `idx_module_type` (`module_type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='动态表单公用分组表';

DROP TABLE IF EXISTS `eh_var_field_group_scopes`;


CREATE TABLE `eh_var_field_group_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `group_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the group name, it will use the name in eh_var_field_groups if not defined',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `community_id` BIGINT COMMENT '园区id',
  `group_parent_id` BIGINT COMMENT '父组系统id',
  `category_id` BIGINT COMMENT 'category id',
  PRIMARY KEY (`id`),
  KEY `i_eh_field_group_scope` (`module_name`,`status`,`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_var_field_groups`;


CREATE TABLE `eh_var_field_groups` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module who has many fields need to be grouped',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of the parent group, it is 0 when there is no parent',
  `path` VARCHAR(128) COMMENT 'path from the root',
  `title` VARCHAR(128) COMMENT 'the title of the group',
  `name` VARCHAR(128) COMMENT 'user name',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_var_field_item_scopes`;


CREATE TABLE `eh_var_field_item_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_fields',
  `item_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_items',
  `item_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the field name, it will use the name in eh_var_field_items if not defined',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `community_id` BIGINT COMMENT '园区id',
  `business_value` TINYINT COMMENT 'the value defined in special business like status',
  `category_id` BIGINT COMMENT 'category id',
  PRIMARY KEY (`id`),
  KEY `i_eh_field_item_scope` (`module_name`,`status`,`namespace_id`),
  KEY `i_eh_item_scope_item_id` (`module_name`,`status`,`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_var_field_items`;


CREATE TABLE `eh_var_field_items` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The field which the item belong to',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The item display name',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `business_value` TINYINT COMMENT 'the value defined in special business like status',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_var_field_ranges`;


CREATE TABLE `eh_var_field_ranges` (
  `id` BIGINT NOT NULL,
  `group_path` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'refer to eh_var_fields',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_fields',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `module_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '一组公用表单的类型',
  PRIMARY KEY (`id`),
  KEY `idx_module_name` (`module_name`),
  KEY `idx_module_type` (`module_type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='动态表单公用组件表';

DROP TABLE IF EXISTS `eh_var_field_scopes`;


CREATE TABLE `eh_var_field_scopes` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `field_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_fields',
  `field_param` VARCHAR(128),
  `field_display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the field name, it will use the name in eh_var_fields if not defined',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `community_id` BIGINT COMMENT '园区id',
  `group_path` VARCHAR(128) COMMENT 'path from the root',
  `category_id` BIGINT COMMENT 'category id',
  PRIMARY KEY (`id`),
  KEY `i_eh_filed_scope` (`module_name`,`status`,`namespace_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_var_fields`;


CREATE TABLE `eh_var_fields` (
  `id` BIGINT NOT NULL COMMENT 'id for records',
  `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The field logic name, it map to the field in db',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'The field display name',
  `field_type` VARCHAR(128),
  `group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refer to eh_var_field_groups',
  `group_path` VARCHAR(128) COMMENT 'path from the root',
  `mandatory_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'the field is mandatory to input something or not, 0-not mandatory, 1-mandatory',
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `update_time` DATETIME,
  `field_param` VARCHAR(128),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_version_urls`;


CREATE TABLE `eh_version_urls` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `realm_id` BIGINT NOT NULL,
  `target_version` VARCHAR(128),
  `download_url` VARCHAR(1024) COMMENT 'example configuration: http://serviceurl/download/client-packages/${locale}/andriod-${major}-${minor}-${revision}.apk',
  `info_url` VARCHAR(1024) COMMENT 'example configuration: http://serviceurl/download/client-package-info/${locale}/andriod-${major}-${minor}-${revision}.html',
  `upgrade_description` TEXT,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `app_name` VARCHAR(50),
  `publish_time` DATETIME,
  `icon_url` VARCHAR(50),
  `version_encoded_value` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_ver_url` (`realm_id`,`target_version`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_visitor_sys_actions`;


CREATE TABLE `eh_visitor_sys_actions` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `visitor_id` BIGINT NOT NULL COMMENT '访客表id',
  `action_type` TINYINT NOT NULL COMMENT '1,园区自助登记，2,园区到访确认，3,园区拒绝到访  4,企业自助登记，5,企业到访确认，6,企业拒绝到访',
  `creator_name` VARCHAR(256) COMMENT '事件操作者',
  `creator_uid` BIGINT COMMENT '创建者id，可以为空',
  `create_time` DATETIME COMMENT '事件发生时间',
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理预约编码表';

DROP TABLE IF EXISTS `eh_visitor_sys_black_list`;


CREATE TABLE `eh_visitor_sys_black_list` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `visitor_name` VARCHAR(256) COMMENT '黑名单访客姓名',
  `visitor_phone` VARCHAR(32) COMMENT '黑名单访客电话',
  `reason` TEXT COMMENT '上黑名单原因',
  `status` TINYINT DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理黑名单表 ';

DROP TABLE IF EXISTS `eh_visitor_sys_coding`;


CREATE TABLE `eh_visitor_sys_coding` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `day_mark` VARCHAR(16) COMMENT 'yyyymmdd 形式的字符串,日标识',
  `serial_code` INTEGER NOT NULL DEFAULT 0 COMMENT '流水码',
  `status` TINYINT DEFAULT 2 COMMENT '0:未使用状态,2:使用状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理预约流水码表';

DROP TABLE IF EXISTS `eh_visitor_sys_configurations`;


CREATE TABLE `eh_visitor_sys_configurations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `config_version` BIGINT COMMENT '配置版本',
  `guide_info` VARCHAR(2048) COMMENT '指引信息',
  `owner_token` VARCHAR(32) NOT NULL COMMENT '自助登记二维码ownerToken地址',
  `logo_uri` VARCHAR(1024) COMMENT '客户端封面图uri地址',
  `welcome_pic_uri` VARCHAR(1024) COMMENT '客户端欢迎页面uri地址',
  `ipad_theme_rgb` VARCHAR(32) COMMENT '客户端主题rgb',
  `secrecy_agreement` TEXT COMMENT '保密协议',
  `welcome_pages` TEXT COMMENT '欢迎页面',
  `welcome_show_type` VARCHAR(32) COMMENT '欢迎页面类型，image显示图片，text显示富文本',
  `config_json` TEXT COMMENT '是否配置项的json配置,访客二维码，访客信息，交通指引，手机扫码自助登记，ipad启动欢迎页面，签署保密协议,允许拍照，允许跳过拍照，输入随访人数，是否启用门禁,门禁id',
  `config_form_json` TEXT COMMENT '表单内容是否显示的配置项，有效期，车牌号码，证件号码，来访备注，到访楼层，到访门牌',
  `config_pass_card_json` TEXT COMMENT '通行证配置，品牌形象，左侧图像，自定义字段，自定义字段，备注信息',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `owner_token` (`owner_token`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理配置表 ';

DROP TABLE IF EXISTS `eh_visitor_sys_devices`;


CREATE TABLE `eh_visitor_sys_devices` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `device_type` VARCHAR(256) COMMENT '设备类型，ipad or printer',
  `device_type_name` VARCHAR(256) COMMENT '设备类型名称，如ipad mini,printer,ipad pro',
  `device_id` VARCHAR(256) COMMENT '设备id',
  `device_version` VARCHAR(128) COMMENT '设备版本',
  `device_name` VARCHAR(128) COMMENT '设备名称',
  `app_version` VARCHAR(128) COMMENT 'app版本',
  `device_pic_uri` VARCHAR(1024) COMMENT '设备图片uri',
  `pairing_code` VARCHAR(32) COMMENT '配对成功时候使用的配对码',
  `app_key` VARCHAR(256) COMMENT '设备后台请求接口appkey',
  `secret_key` VARCHAR(256) COMMENT '设备后台请求接口secretkey',
  `status` TINYINT DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理设备表 ';

DROP TABLE IF EXISTS `eh_visitor_sys_door_access`;


CREATE TABLE `eh_visitor_sys_door_access` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `door_access_id` BIGINT NOT NULL COMMENT '门禁组Id',
  `door_access_name` VARCHAR(256) COMMENT '门禁组名称',
  `default_auth_duration_type` TINYINT DEFAULT 0 COMMENT '默认访客授权有效期种类,0 天数，1 小时数',
  `default_auth_duration` INTEGER DEFAULT 0 COMMENT '默认访客授权有效期',
  `default_enable_auth_count` TINYINT DEFAULT 0 COMMENT '默认访客授权次数开关 0 关 1 开',
  `default_auth_count` INTEGER DEFAULT 0 COMMENT '默认访客授权次数',
  `default_door_access_flag` TINYINT DEFAULT 0 COMMENT '默认门禁组 0 非默认 1 默认',
  `status` TINYINT DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客门禁授权默认方案表';

DROP TABLE IF EXISTS `eh_visitor_sys_message_receivers`;


CREATE TABLE `eh_visitor_sys_message_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `creator_uid` BIGINT COMMENT '创建者/访客管理者id',
  `create_time` DATETIME COMMENT '事件发生时间',
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理 管理者消息接受表';

DROP TABLE IF EXISTS `eh_visitor_sys_office_locations`;


CREATE TABLE `eh_visitor_sys_office_locations` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `office_location_name` VARCHAR(256) COMMENT '办公地点名称',
  `addresses` VARCHAR(512) COMMENT '办公地点地址',
  `longitude` DOUBLE COMMENT '办公地点经度',
  `latitude` DOUBLE COMMENT '办公地点维度',
  `geohash` VARCHAR(32) COMMENT '办公地点经纬度hash值',
  `map_addresses` VARCHAR(512) COMMENT '办公地点地图选点地址',
  `status` TINYINT DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理办公地点表 ';

DROP TABLE IF EXISTS `eh_visitor_sys_owner_code`;


CREATE TABLE `eh_visitor_sys_owner_code` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `random_code` VARCHAR(16) NOT NULL COMMENT '随机码',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  PRIMARY KEY (`id`),
  UNIQUE KEY `random_code` (`random_code`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理预约编码表';

DROP TABLE IF EXISTS `eh_visitor_sys_visit_reason`;


CREATE TABLE `eh_visitor_sys_visit_reason` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) COMMENT 'community or organization',
  `owner_id` BIGINT COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `visit_reason` VARCHAR(256) COMMENT '事由描述',
  `status` TINYINT DEFAULT 2 COMMENT '0:被删除状态,2:正常状态',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理到访是由表 ';

DROP TABLE IF EXISTS `eh_visitor_sys_visitors`;


CREATE TABLE `eh_visitor_sys_visitors` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父id,没有则为0，有则为父预约id，一般用于园区访客下访问公司预约',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `enterprise_id` BIGINT COMMENT '到访公司id',
  `enterprise_name` VARCHAR(256) COMMENT '到访公司名称,园区则无',
  `visitor_name` VARCHAR(256) COMMENT '访客姓名',
  `visitor_phone` VARCHAR(32) COMMENT '访客电话',
  `visitor_type` TINYINT COMMENT '访客类型,0,临时访客；1,预约访客',
  `visitor_sign_uri` VARCHAR(1024) COMMENT '访客签名图片uri（ipad签名）',
  `visitor_sign_character` VARCHAR(1024) COMMENT '访客签名字符（客户端使用），也是自助签名姓名',
  `visitor_pic_uri` VARCHAR(1024) COMMENT '访客头像图片uri（ipad上传访客照片）',
  `door_guard_id` VARCHAR(1024) COMMENT '门禁二维码对应门禁id',
  `door_guard_qrcode` VARCHAR(1024) COMMENT '门禁二维码字符串',
  `door_guard_end_time` DATETIME COMMENT '门禁二维码失效时间',
  `inviter_id` BIGINT COMMENT '邀请者的用户id',
  `inviter_name` VARCHAR(256) COMMENT '邀请者的用户姓名',
  `planned_visit_time` DATETIME COMMENT '计划到访时间',
  `visit_time` DATETIME COMMENT '实际到访时间',
  `visit_status` TINYINT COMMENT '访客状态，0，已删除; 1，未到访;2，等待确认; 3，已到访; 4，已拒绝; ',
  `booking_status` TINYINT COMMENT '预约状态，0，已删除; 1，未到访;2，等待确认; 3，已到访;',
  `send_message_inviter_flag` TINYINT COMMENT '确认到访时是否发送消息给邀请人，0，不发送; 1，发送',
  `send_sms_Flag` TINYINT COMMENT '是否发送访客邀请函给邀请人，0，不发送; 1，发送',
  `office_location_id` BIGINT COMMENT '办公地点ID',
  `office_location_name` VARCHAR(512) COMMENT '办公地点名称',
  `visit_reason_id` BIGINT COMMENT '到访是由Id',
  `visit_reason` VARCHAR(512) COMMENT '到访是由',
  `follow_up_numbers` BIGINT COMMENT '随访人员数量',
  `invitation_no` VARCHAR(32) COMMENT '预约编号RG201804280001',
  `invalid_time` VARCHAR(32) COMMENT '访邀有效时长',
  `plate_no` VARCHAR(32) COMMENT '车牌号码',
  `id_number` VARCHAR(64) COMMENT '证件号码',
  `visit_floor` VARCHAR(128) COMMENT '到访楼层',
  `visit_addresses` VARCHAR(1024) COMMENT '到访门牌',
  `form_json_value` TEXT COMMENT '表单提交的json',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `operator_uid` BIGINT,
  `operate_time` DATETIME,
  `source` TINYINT DEFAULT 0 COMMENT '访客来源，0:内部录入 1:外部对接',
  `notify_third_success_flag` TINYINT DEFAULT 0 COMMENT '访客来源为外部对接，0：未回调到第三方 1：回调失败 2:回调成功',
  `door_access_auth_duration_type` TINYINT COMMENT '访客授权有效期种类,0 天数，1 小时数',
  `door_access_auth_duration` INTEGER COMMENT '访客授权有效期',
  `door_access_enable_auth_count` TINYINT DEFAULT 0 COMMENT '访客授权次数开关 0 关 1 开',
  `door_access_auth_count` INTEGER COMMENT '访客授权次数',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理访客表 ';

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `community_id` BIGINT DEFAULT 0 COMMENT '园区id',
  `supplier_id` BIGINT COMMENT '物品的供应商的主键id',
  `supplier_name` VARCHAR(128) COMMENT '物品的供应商的名字',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_warehouse_orders`;


CREATE TABLE `eh_warehouse_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `identity` VARCHAR(128) NOT NULL COMMENT '出入库单号',
  `executor_id` BIGINT COMMENT '执行人id',
  `executor_name` VARCHAR(128) COMMENT '执行人姓名',
  `executor_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '执行时间',
  `service_type` TINYINT COMMENT '服务类型，1. 普通入库,2.领用出库，3.采购入库',
  `community_id` BIGINT COMMENT '园区id',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_uid` BIGINT,
  `default_order` INTEGER DEFAULT 0,

  PRIMARY KEY (`id`),
  KEY `i_service_type` (`service_type`) COMMENT '出入库状态得索引，用于搜索'
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_warehouse_purchase_items`;


CREATE TABLE `eh_warehouse_purchase_items` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `purchase_request_id` BIGINT NOT NULL COMMENT '所属的采购单id',
  `material_id` BIGINT COMMENT '采购物品id',
  `purchase_quantity` BIGINT DEFAULT 0 COMMENT '采购数量',
  `unit_price` DECIMAL(20,2) DEFAULT '0.00' COMMENT '单价',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_uid` BIGINT,
  `default_order` INTEGER DEFAULT 0,
  `warehouse_id` BIGINT COMMENT '仓库id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_warehouse_purchase_orders`;


CREATE TABLE `eh_warehouse_purchase_orders` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `applicant_id` BIGINT COMMENT '申请人id',
  `applicant_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '申请时间',
  `supplier_id` BIGINT COMMENT '供应商id',
  `submission_status` TINYINT COMMENT '审核状态',
  `total_amount` DECIMAL(20,2) DEFAULT '0.00' COMMENT '总金额',
  `warehouse_status` TINYINT COMMENT '库存状态',
  `delivery_date` DATETIME COMMENT '交付日期',
  `community_id` BIGINT,
  `applicant_name` VARCHAR(128),
  `contact_tel` VARCHAR(128),
  `contact_name` VARCHAR(128),
  `remark` VARCHAR(2048) COMMENT '备注',
  `approval_order_id` BIGINT COMMENT '关联的审批单的id',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_uid` BIGINT,
  `default_order` INTEGER DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `community_id` BIGINT DEFAULT 0 COMMENT '园区id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `community_id` BIGINT DEFAULT 0 COMMENT '园区id',
  `requisition_id` BIGINT COMMENT '关联的请示单的id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `community_id` BIGINT DEFAULT 0 COMMENT '园区id',
  `warehouse_order_id` BIGINT COMMENT '关联的出入库单的id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `community_id` BIGINT DEFAULT 0 COMMENT '园区id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_warehouse_suppliers`;


CREATE TABLE `eh_warehouse_suppliers` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `owner_type` VARCHAR(32),
  `owner_id` BIGINT,
  `identity` VARCHAR(128) COMMENT '供应商编号',
  `name` VARCHAR(128) NOT NULL COMMENT '供应商名称',
  `enterprise_business_licence` VARCHAR(32) COMMENT '企业营业执照注册号',
  `legal_person_name` VARCHAR(32) COMMENT '法人的名字',
  `contact_name` VARCHAR(32) COMMENT '联系人',
  `contact_tel` VARCHAR(64) COMMENT '联系电话',
  `enterprise_register_address` VARCHAR(256) COMMENT '注册地址',
  `email` VARCHAR(128) COMMENT '邮箱',
  `corp_address` VARCHAR(256) COMMENT '公司地址',
  `corp_intro_info` TEXT COMMENT '公司简介',
  `industry` VARCHAR(128) COMMENT '所属行业',
  `bank_name` VARCHAR(128) COMMENT '开户行',
  `bank_card_number` VARCHAR(64) COMMENT '银行账号',
  `evaluation_category` TINYINT COMMENT '评定类别， 1：合格；2：临时',
  `evaluation_levle` TINYINT COMMENT '供应商等级 1：A类；2：B类；3：C类',
  `main_biz_scope` VARCHAR(1024) COMMENT '主要经营范围',
  `attachment_url` VARCHAR(2048) COMMENT '附件地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `create_uid` BIGINT,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_uid` BIGINT,
  `default_order` INTEGER DEFAULT 0,
  `file_name` VARCHAR(256) COMMENT '文件名称',
  `community_id` BIGINT COMMENT '供应商所在园区的id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `community_id` BIGINT DEFAULT 0 COMMENT '园区id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `eh_warning_contacts`;


CREATE TABLE `eh_warning_contacts` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `contactor` VARCHAR(20),
  `mobile` VARCHAR(20),
  `email` VARCHAR(20),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `category_id` BIGINT COMMENT '入口id',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
  `app_id` BIGINT COMMENT 'eh_service_module_app id',
  `config_id` BIGINT COMMENT 'get config, eg multiple application.',

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_menu_scope_owner` (`menu_id`,`owner_type`,`owner_id`,`app_id`) USING BTREE
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
  `level` INTEGER NOT NULL DEFAULT 0,
  `condition_type` VARCHAR(32),
  `category` VARCHAR(32),
  `config_type` TINYINT COMMENT 'null, 1-config by application, 2-all namespace have',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_report_scope_map`;
CREATE TABLE `eh_work_report_scope_map` (
  `id` BIGINT NOT NULL COMMENT 'id of the report scope id',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'id of the report',
  `source_type` VARCHAR(64) NOT NULL COMMENT 'ORGANIZATION, MEMBERDETAIL',
  `source_id` BIGINT NOT NULL COMMENT 'id of the scope',
  `source_description` VARCHAR(128) COMMENT 'the description of the scope class',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_report_scope_msg`;


CREATE TABLE `eh_work_report_scope_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` date NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `end_time` DATETIME COMMENT 'the deadline of the report',
  `scope_ids` TEXT COMMENT 'the id list of the receiver',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`),
  KEY `i_eh_work_report_scope_msg_report_id` (`report_id`),
  KEY `i_eh_work_report_scope_msg_report_time` (`report_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_report_templates`;
CREATE TABLE `eh_work_report_templates` (
  `id` BIGINT NOT NULL COMMENT 'id of the report template',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT COMMENT 'the module id',
  `module_type` VARCHAR(64) COMMENT 'the module type',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE',
  `form_template_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the template form',
  `modify_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_report_val_comment_attachments`;
CREATE TABLE `eh_work_report_val_comment_attachments` (
  `id` BIGINT NOT NULL COMMENT 'the id of the report val map',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `comment_id` BIGINT NOT NULL COMMENT 'the id of the comment',
  `content_type` VARCHAR(32) COMMENT 'the type of the content',
  `content_uri` VARCHAR(2048) COMMENT 'attachment object link info on storage',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `creator_user_id` BIGINT NOT NULL COMMENT 'the user id of the creator',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_report_val_comments`;
CREATE TABLE `eh_work_report_val_comments` (
  `id` BIGINT NOT NULL COMMENT 'the id of the report val map',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `report_val_id` BIGINT NOT NULL COMMENT 'the id of the report val',
  `parent_comment_id` BIGINT COMMENT 'the parent id of the',
  `content_type` VARCHAR(32) COMMENT 'the type of the content',
  `content` TEXT COMMENT 'the comment',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: invalid, 1: valid',
  `creator_user_id` BIGINT NOT NULL COMMENT 'the user id of the creator',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_report_val_receiver_map`;
CREATE TABLE `eh_work_report_val_receiver_map` (
  `id` BIGINT NOT NULL COMMENT 'the id of the report val map',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the orgId for the user',
  `report_val_id` BIGINT NOT NULL COMMENT 'the id of the report val',
  `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
  `receiver_name` VARCHAR(256) COMMENT 'the name of the receiver',
  `receiver_avatar` VARCHAR(2048) COMMENT 'the avatar of the receiver',
  `read_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0-unread 1-read',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`),
  KEY `i_work_report_receiver_id` (`receiver_user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_report_val_receiver_msg`;


CREATE TABLE `eh_work_report_val_receiver_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_val_id` BIGINT NOT NULL COMMENT 'id of the report val',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` date NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
  `create_time` DATETIME COMMENT 'record create time',
  PRIMARY KEY (`id`),
  KEY `i_eh_work_report_val_receiver_msg_report_id` (`report_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_val_id` (`report_val_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_time` (`report_time`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_report_vals`;
CREATE TABLE `eh_work_report_vals` (
  `id` BIGINT NOT NULL COMMENT 'id of the report val',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT COMMENT 'the module id',
  `module_type` VARCHAR(64) COMMENT 'the module type',
  `module_name` VARCHAR(64),
  `content` VARCHAR(128) COMMENT 'the content of the report',
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_time` DATE COMMENT 'the target time of the report',
  `applier_name` VARCHAR(64) COMMENT 'the name of the applier',
  `applier_user_id` BIGINT COMMENT 'the userId of the applier',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid',
  `report_type` TINYINT NOT NULL COMMENT '0-Day, 1-Week, 2-Month',
  `receiver_avatar` VARCHAR(1024) COMMENT 'the avatar of the fisrt receiver',
  `applier_avatar` VARCHAR(1024) COMMENT 'the avatar of the author',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_work_reports`;
CREATE TABLE `eh_work_reports` (
  `id` BIGINT NOT NULL COMMENT 'id of the report',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `module_id` BIGINT COMMENT 'the module id',
  `module_type` VARCHAR(64) COMMENT 'the module type',
  `report_name` VARCHAR(128) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0-invalid, 1-valid, 2-running',
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_attribute` VARCHAR(128) NOT NULL DEFAULT 'CUSTOMIZE' COMMENT 'DEFAULT,CUSTOMIZE',
  `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'The id of the original form',
  `form_version` BIGINT NOT NULL DEFAULT 0 COMMENT 'the current using version',
  `validity_setting` VARCHAR(512) COMMENT 'the expiry date of the work report',
  `receiver_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the receiver message settings',
  `receiver_msg_seeting` VARCHAR(512) COMMENT 'the time range of the receiver message',
  `author_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the author message settings',
  `author_msg_seeting` VARCHAR(512) COMMENT 'the time range of the author message',
  `report_template_id` BIGINT DEFAULT 0 COMMENT 'the id in eh_general_approval_templates',
  `modify_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `delete_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '0: no, 1: yes',
  `icon_uri` VARCHAR(1024) COMMENT 'the avatar of the approval',
  `operator_user_id` BIGINT DEFAULT 0 COMMENT 'the user id of the operator',
  `operator_name` VARCHAR(128) COMMENT 'the name of the operator',
  `update_time` DATETIME COMMENT 'last update time',
  `create_time` DATETIME COMMENT 'record create time',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_yzx_sms_logs`;


CREATE TABLE `eh_yzx_sms_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `scope` VARCHAR(64),
  `code` INTEGER,
  `locale` VARCHAR(16),
  `mobile` VARCHAR(128),
  `text` TEXT,
  `variables` VARCHAR(512),
  `resp_code` VARCHAR(32),
  `failure` TINYINT,
  `create_date` VARCHAR(32),
  `sms_id` VARCHAR(128),
  `type` TINYINT COMMENT '1:状态报告，2：上行',
  `status` TINYINT COMMENT '0:成功；1：提交失败，4：失败，5：关键字（keys），6：黑/白名单，7：超频（overrate），8：unknown',
  `desc` TEXT,
  `report_time` DATETIME,
  `create_time` DATETIME,

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_contact_token` (`sms_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_zj_syncdata_backup`;


CREATE TABLE `eh_zj_syncdata_backup` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `data_type` TINYINT NOT NULL COMMENT '1: community, 2: building, 3: apartment, 4: enterprise',
  `all_flag` TINYINT NOT NULL COMMENT '1: all data, 0: special community',
  `update_community` VARCHAR(64) COMMENT 'if all flag is 0, the special community identifier',
  `next_page_offset` INTEGER COMMENT 'next page offset',
  `name` VARCHAR(64),
  `data` LONGTEXT COMMENT 'data list',
  `status` TINYINT NOT NULL COMMENT '0: inactive, 2: active',
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  `update_time` DATETIME,

  PRIMARY KEY (`id`),
  KEY `i_eh_namespaceid_data_type` (`namespace_id`,`update_community`,`data_type`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
