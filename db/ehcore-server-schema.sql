#
# Special notes about the schema design below (KY)
#
# Custom fileds
# 	To balance performance and flexibility, some tables carry general purpose integer fields and string fields,
# 	interpretation of these fields will be determined by the applications on top of, at database level, we only
# 	provide general indexing support for these fields, it is the responsibility of the application to map queries that
# 	are against to these fields.
#
# 	Initially, only two of integral-type and string-type fields are indexed, more indices can be added during operating
# 	time, tuning changes about the indexing will be sync-ed back into schema design afterwards
#
# namespaces and application modules
#	Reusable modules are abstracted under the concept of application. The platform provides built-in application modules
#	such as messaging application module, forum application module, etc. These built-in application modules are running 
#   in the context of core server. When a application module has external counterpart at third-party servers or remote client endpoints, 
#	the API it provides requires to go through the authentication system via appkey/secret key pair mechanism
#
#   Namespace is used to put related resources into distinct domains
#
# namespace and application design rules
#	Shared resources (usually system defined) that are common to all namespaces do not need namespace_id field
#	First level resources usually have namespace_id field
#	Secondary level resources do not need namespace_id field
#	objects that can carry information generated from multiple application modules usualy have app_id field
#	all profile items have app_id field, so that it allows other application modules to attach application specific
#	profile information into it
#
# name convention
#	index prefix: i_eh_
#	unique index prefix: u_eh_
#	foreign key constraint prefix: fk_eh_
# 	table prefix: eh_
#
# record deletion
# 	There are two deletion policies in regards to deletion
#		mark-deletion: mark it as deleted, wait for lazy cleanup or archive
#		remove-deletion: completely remove it from database
#
#   for the mark-deletion policy, the convention is to have a delete_time field which not only marks up the deletion
#	but also the deletion time
#
#

SET foreign_key_checks = 0;

use ehcore;

DROP TABLE IF EXISTS `eh_devices`;

#
# member of global partition
#
CREATE TABLE `eh_devices` (
  	`id` BIGINT NOT NULL AUTO_INCREMENT,
  	`device_id` VARCHAR(128) NOT NULL,
  	`platform` VARCHAR(32) NOT NULL,
  	`product` VARCHAR(32) NULL,
  	`brand` VARCHAR(32) NULL,
  	`device_model` VARCHAR(32) NULL,
  	`system_version` VARCHAR(32) NULL,
  	`meta` VARCHAR(256) NULL,
  	`create_time` DATETIME,
  
  	PRIMARY KEY (`id`),
    UNIQUE `u_eh_dev_id`(`device_id`),
    INDEX `u_eh_dev_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_certs`;

#
# member of global partition
#
CREATE TABLE `eh_certs` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(32) NOT NULL,
  `cert_type` INT NOT NULL,
  `cert_pass` VARCHAR(128),
  `data` BLOB NOT NULL,
  
  PRIMARY KEY (`id`),
  UNIQUE `u_eh_certs_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_locale_strings`;
CREATE TABLE `eh_locale_strings`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `scope` VARCHAR(64),
    `code` VARCHAR(64),
    `locale` VARCHAR(16),
    `text` TEXT,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_lstr_identifier`(`scope`, `code`, `locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
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
    UNIQUE `u_eh_template_identifier`(`namespace_id`, `scope`, `code`, `locale`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 
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
    INDEX `i_eh_category_path`(`path`),
    INDEX `i_eh_category_order`(`default_order`),
    INDEX `i_eh_category_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
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
    UNIQUE `u_kword_scoped_kword`(`scope`, `scope_id`, `keyword`),
    INDEX `i_kword_weight_frequency`(`weight`, `frequency`),
    INDEX `i_kword_update_time`(`update_time`),
    INDEX `i_kword_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
# for compatibility reason, this table is basically cloned from old DB
#
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
    INDEX `i_app_promo_create_time`(`create_time`),
    INDEX `i_app_promo_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
# 
# <scope_type, scope_id> defines the scope that the configuration is applied to, for example, 
#	<'system', NULL> may be used to identify a global system scope
#	<'community', cumminity-id> may be used to dentify a porticular community
#
#
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
    INDEX `i_eh_scoped_cfg_combo`(`namespace_id`, `app_id`, `scope_type`, `scope_id`, `item_name`),
    INDEX `i_eh_scoped_cfg_itag1`(`integral_tag1`),
    INDEX `i_eh_scoped_cfg_itag2`(`integral_tag2`),
    INDEX `i_eh_scoped_cfg_stag1`(`string_tag1`),
    INDEX `i_eh_scoped_cfg_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global sharding group
#
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

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global sharding group
#
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
	`display_layout` VARCHAR(128) DEFAULT '1' COMMENT 'how many grids it takes at the layout, format: 2x3',
	`bgcolor` INTEGER NOT NULL DEFAULT 0,
    `tag` VARCHAR(1024),
    `target_type` VARCHAR(32),
    `target_id` BIGINT COMMENT 'the entity id linked back to the orginal resource',

    PRIMARY KEY (`id`),
    INDEX `i_eh_scoped_cfg_combo`(`namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_name`),
    INDEX `i_eh_scoped_cfg_order`(`default_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
# for compatibility reason, this table is basically cloned from old DB
#
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
    UNIQUE `u_stats_city_report`(`city_id`, `stats_date`, `stats_type`),
    INDEX `u_stats_delete_time`(`delete_time`)    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_templates`;
CREATE TABLE `eh_templates`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(64),
    `path` VARCHAR(256),
    `type` TINYINT,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_template_name`(`name`)    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_audit_logs`;
CREATE TABLE `eh_audit_logs`(
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
    INDEX `i_eh_audit_operator_uid`(`operator_uid`),
    INDEX `i_eh_audit_requestor_uid`(`requestor_uid`),
    INDEX `i_eh_audit_create_time`(`create_time`),
    INDEX `i_eh_audit_delete_time`(`delete_time`),
    INDEX `i_eh_audit_itag1`(`integral_tag1`),
    INDEX `i_eh_audit_itag2`(`integral_tag2`),
    INDEX `i_eh_audit_stag1`(`string_tag1`),
    INDEX `i_eh_audit_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# key table of user-related sharding group
#
DROP TABLE IF EXISTS `eh_users`;
CREATE TABLE `eh_users` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
	`uuid` VARCHAR(128) NOT NULL DEFAULT '',
    `account_name` VARCHAR(64) NOT NULL,
    `nick_name` VARCHAR(256),
    `avatar` VARCHAR(128),
    `status_line` VARCHAR(128) COMMENT 'status line to express who you are',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0 - inactive, 1 - active',
    `points` INTEGER NOT NULL DEFAULT 0 COMMENT 'points',
    `level` TINYINT NOT NULL DEFAULT 1,
    
    #
    # for gender/age based matching
    #
    `gender` TINYINT NOT NULL DEFAULT 0 COMMENT '0: undisclosured, 1: male, 2: female',
    `birthday` DATE,
    
    #
    # for current residency matching (corresponding to current family)
    #
    `address_id` BIGINT COMMENT 'current address id',
    `address` VARCHAR(128) COMMENT 'redundant current address description',
    
    `community_id` BIGINT COMMENT 'if current family has been setup, it is the community id from address',
    
    #
    # for home town based matching
    #
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
	
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_uuid`(`uuid`),
    UNIQUE `u_eh_user_account_name`(`account_name`),
    INDEX `i_eh_user_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_users partition
#
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
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_user_idf_owner_type_token`(`owner_uid`, `identifier_type`, `identifier_token`),
    INDEX `i_eh_user_idf_owner`(`owner_uid`),
    INDEX `i_eh_user_idf_type_token`(`identifier_type`, `identifier_token`),
    INDEX `i_eh_user_idf_create_time`(`create_time`),
    INDEX `i_eh_user_idf_notify_time`(`notify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 
# member of eh_users partition
# Used for duplicated recording of group membership that user is involved in order to store 
# it in the same shard as of its owner user
#
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
    UNIQUE `u_eh_usr_grp_owner_group`(`owner_uid`, `group_id`),
    INDEX `i_eh_usr_grp_owner`(`owner_uid`),
    INDEX `i_eh_usr_grp_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 
# member of eh_users partition
# Used for duplicated recording of group membership that user is involved in order to store 
# it in the same shard as of its owner user
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 
# member of eh_users partition
# Used for duplicated recording of post membership that user is involved in order to store 
# it in the same shard as of its owner user
#
DROP TABLE IF EXISTS `eh_user_posts`;
CREATE TABLE `eh_user_posts` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `post_id` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_post_id`(`owner_uid`, `post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_users sharding group
# secondary resource objects (after eh_users)
#
DROP TABLE IF EXISTS `eh_user_followed_families`;
CREATE TABLE `eh_user_followed_families`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL,
    `followed_family` BIGINT NOT NULL,
    `alias_name` VARCHAR(64),
    
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`),
    UNIQUE `i_eh_usr_ffmy_followed`(`owner_uid`, `followed_family`),
    INDEX `i_eh_usr_ffmy_owner`(`owner_uid`),
    INDEX `i_eh_usr_ffmy_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_users partition
#
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
    UNIQUE `u_eh_invite_code`(`invite_code`),
    INDEX `u_eh_invite_expiration`(`expiration`),
    INDEX `u_eh_invite_code_status`(`invite_code`, `status`),
    INDEX `u_eh_invite_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_user_invitation_roster`;
CREATE TABLE `eh_user_invitation_roster` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `invite_id` BIGINT COMMENT 'owner invitation record id',
    `name` VARCHAR(64),
    `contact` VARCHAR(64),
    
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_invite_roster_invite_id`(`invite_id`) REFERENCES `eh_user_invitations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_users sharding group
#
DROP TABLE IF EXISTS `eh_user_blacklist`;
CREATE TABLE `eh_user_blacklist` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_blk_owner_target`(`owner_uid`, `target_type`, `target_id`),
    INDEX `i_eh_usr_blk_owner`(`owner_uid`),
    INDEX `i_eh_usr_blk_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_users sharding group
#
DROP TABLE IF EXISTS `eh_user_favorites`;
CREATE TABLE `eh_user_favorites` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_favorite_target`(`owner_uid`, `target_type`, `target_id`),
    INDEX `i_eh_usr_favorite_owner`(`owner_uid`),
    INDEX `i_eh_usr_favorite_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_user sharding group
#
DROP TABLE IF EXISTS `eh_user_likes`;
CREATE TABLE `eh_user_likes` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    `like_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: dislike, 2: like',
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_like_target`(`owner_uid`, `target_type`, `target_id`),
    INDEX `i_eh_usr_like_owner`(`owner_uid`),
    INDEX `i_eh_usr_like_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_users sharding group
#
DROP TABLE IF EXISTS `eh_user_profiles`;
CREATE TABLE `eh_user_profiles`(
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
    INDEX `i_eh_uprof_item`(`app_id`, `owner_id`, `item_name`),
    INDEX `i_eh_uprof_owner`(`owner_id`),
    INDEX `i_eh_uprof_itag1`(`integral_tag1`),
    INDEX `i_eh_uprof_itag2`(`integral_tag2`),
    INDEX `i_eh_uprof_stag1`(`string_tag1`),
    INDEX `i_eh_uprof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 
# member of eh_users partition
# Used for duplicated recording of post membership that user is involved in order to store 
# it in the same shard as of its owner user
#
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
	`update_time` datetime DEFAULT NULL,
	`deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
    `delete_time` DATETIME COMMENT '',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_service_address_id`(`owner_uid`, `address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 
# member of eh_users partition
# Used for duplicated recording of group membership that user is involved in order to store 
# it in the same shard as of its owner user
#
DROP TABLE IF EXISTS `eh_user_communities`;
CREATE TABLE `eh_user_communities` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `community_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'redendant info for quickly distinguishing associated community', 
    `community_id` BIGINT NOT NULL DEFAULT 0,
    `join_policy` TINYINT NOT NULL DEFAULT 1 COMMENT '1: register, 2: request to join',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_community`(`owner_uid`, `community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# key table of grouping related sharding group
# Usually there is no need for group object to carry information for other applications, therefore there is
# not an app_id field
#
# Group custom fields are used by inherited classes
# 
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
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_uuid`(`uuid`),
    INDEX `i_eh_group_creator`(`creator_uid`),
    INDEX `i_eh_group_create_time` (`create_time`),
    INDEX `i_eh_group_delete_time` (`delete_time`),
    INDEX `i_eh_group_itag1`(`integral_tag1`),
    INDEX `i_eh_group_itag2`(`integral_tag2`),
    INDEX `i_eh_group_stag1`(`string_tag1`),
    INDEX `i_eh_group_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups sharding group
#
DROP TABLE IF EXISTS `eh_group_visible_scopes`;
CREATE TABLE `eh_group_visible_scopes` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner group id',
    `scope_code` TINYINT,
    `scope_id` BIGINT,
    
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_grp_scope_owner`(`owner_id`) REFERENCES `eh_groups`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_grp_scope_target`(`scope_code`, `scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups sharding group
#
DROP TABLES IF EXISTS `eh_group_profiles`;
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
    INDEX `i_eh_gprof_item`(`app_id`, `owner_id`, `item_name`),
    INDEX `i_eh_gprof_owner`(`owner_id`),
    INDEX `i_eh_gprof_itag1`(`integral_tag1`),
    INDEX `i_eh_gprof_itag2`(`integral_tag2`),
    INDEX `i_eh_gprof_stag1`(`string_tag1`),
    INDEX `i_eh_gprof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups sharding group
#
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
	UNIQUE `u_eh_uuid`(`uuid`),
    UNIQUE `u_eh_grp_member` (`group_id`, `member_type`, `member_id`),
    INDEX `i_eh_grp_member_group_id` (`group_id`),
    INDEX `i_eh_grp_member_member` (`member_type`, `member_id`),
    INDEX `i_eh_grp_member_create_time` (`create_time`),
    INDEX `i_eh_grp_member_approve_time` (`approve_time`),
    INDEX `i_eh_gprof_itag1`(`integral_tag1`),
    INDEX `i_eh_gprof_itag2`(`integral_tag2`),
    INDEX `i_eh_gprof_stag1`(`string_tag1`),
    INDEX `i_eh_gprof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups sharding group
#
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
    INDEX `i_eh_grp_op_group`(`group_id`),
    INDEX `i_eh_grp_op_requestor`(`requestor_uid`),
    INDEX `i_eh_grp_op_create_time`(`create_time`),
    INDEX `i_eh_grp_op_process_time`(`process_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# key table for forum sharding group
#
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
	UNIQUE `u_eh_uuid`(`uuid`),
    INDEX `i_eh_frm_namespace`(`namespace_id`),
    INDEX `i_eh_frm_owner`(`owner_type`, `owner_id`),
    INDEX `i_eh_frm_post_count` (`post_count`),
    INDEX `i_eh_frm_modify_seq` (`modify_seq`),
    INDEX `i_eh_frm_update_time` (`update_time`),
    INDEX `i_eh_frm_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# key table of forum post sharding group
# forum posts form its own sharding group, due to nature of timely content
# delete column `dislike_count` BIGINT NOT NULL DEFAULT 0,
#
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
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_uuid`(`uuid`),
    INDEX `i_eh_post_seqs`(`modify_seq`),
    INDEX `i_eh_post_geohash`(`geohash`),
    INDEX `i_eh_post_creator`(`creator_uid`),
    INDEX `i_eh_post_itag1`(`integral_tag1`),
    INDEX `i_eh_post_itag2`(`integral_tag2`),
    INDEX `i_eh_post_stag1`(`string_tag1`),
    INDEX `i_eh_post_stag2`(`string_tag2`),
    INDEX `i_eh_post_update_time`(`update_time`),
    INDEX `i_eh_post_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of forum post sharding group
#
DROP TABLE IF EXISTS `eh_forum_visible_scopes`;
CREATE TABLE `eh_forum_visible_scopes` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner post id',
    `scope_code` TINYINT,
    `scope_id` BIGINT,
    
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_post_scope_owner`(`owner_id`) REFERENCES `eh_forum_posts`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_post_scope_target`(`scope_code`, `scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of forum post sharding group
#
DROP TABLE IF EXISTS `eh_forum_assigned_scopes`;
CREATE TABLE `eh_forum_assigned_scopes` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner post id',
    `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city',
    `scope_id` BIGINT,
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_scope_owner_code_id`(`owner_id`, `scope_code`, `scope_id`),
    FOREIGN KEY `fk_eh_post_scope_owner`(`owner_id`) REFERENCES `eh_forum_posts`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_post_scope_owner_id`(`owner_id`),
    INDEX `i_eh_post_scope_target`(`scope_code`, `scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of forum post sharding group
#
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
    INDEX `i_eh_frmatt_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_borders`;
CREATE TABLE `eh_borders` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `private_address` VARCHAR(128) NOT NULL,
    `private_port` INTEGER NOT NULL DEFAULT 8086,
    `public_address` VARCHAR(128) NOT NULL,
    `public_port` INTEGER NOT NULL DEFAULT 80,
    `status` INTEGER NOT NULL DEFAULT 0 COMMENT '0 : disabled, 1: enabled',
    `config_tag` VARCHAR(32),
    `description` VARCHAR(256),
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_border_config_tag`(`config_tag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
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
	`namespace_id` int(11) NOT NULL DEFAULT '0',
    
    PRIMARY KEY(`id`),
    UNIQUE `u_eh_region_name`(`namespace_id`, `parent_id`, `name`),
 	INDEX `i_eh_region_name_level`(`name`, `level`),   
    INDEX `i_eh_region_path`(`path`),
 	INDEX `i_eh_region_path_level`(`path`, `level`),   
 	INDEX `i_eh_region_parent`(`parent_id`)   
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# key table of the sharding group
# shared resource objects, custom fields may not really be needed
#
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
	`namespace_id` int(11) NOT NULL DEFAULT '0',
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_uuid`(`uuid`),
    INDEX `i_eh_community_area_name`(`area_name`),
    INDEX `i_eh_community_name`(`name`),
    INDEX `i_eh_community_alias_name`(`alias_name`),
    INDEX `i_eh_community_zipcode`(`zipcode`),
    INDEX `i_eh_community_create_time`(`create_time`),
    INDEX `i_eh_community_delete_time`(`delete_time`),
    INDEX `i_eh_community_itag1`(`integral_tag1`),
    INDEX `i_eh_community_itag2`(`integral_tag2`),
    INDEX `i_eh_community_stag1`(`string_tag1`),
    INDEX `i_eh_community_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities partition
#
DROP TABLE IF EXISTS `eh_community_geopoints`;
CREATE TABLE `eh_community_geopoints` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `community_id` BIGINT,
	`description` VARCHAR(64),
	`longitude` DOUBLE,
	`latitude` DOUBLE,
	`geohash` VARCHAR(32),
	
    PRIMARY KEY (`id`),
    INDEX `i_eh_comm_description`(`description`),
	INDEX `i_eh_comm_geopoints`(`geohash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities partition
#
DROP TABLE IF EXISTS `eh_nearby_community_map`;
CREATE TABLE `eh_nearby_community_map` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `community_id` BIGINT NOT NULL DEFAULT 0,
    `nearby_community_id` BIGINT NOT NULL DEFAULT 0,
	`namespace_id` int(11) NOT NULL DEFAULT '0',
	
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_community_relation`(`community_id`, `nearby_community_id`),
    INDEX `u_eh_community_id`(`community_id`),
    INDEX `u_eh_nearby_community_id`(`nearby_community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities partition
#
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
    INDEX `i_eh_cprof_item`(`app_id`, `owner_id`, `item_name`),
    INDEX `i_eh_cprof_owner`(`owner_id`),
    INDEX `i_eh_cprof_itag1`(`integral_tag1`),
    INDEX `i_eh_cprof_itag2`(`integral_tag2`),
    INDEX `i_eh_cprof_stag1`(`string_tag1`),
    INDEX `i_eh_cprof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_organizations`;
CREATE TABLE `eh_organizations` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `parent_id` BIGINT COMMENT 'id of the parent region', 
    `organization_type` VARCHAR(64) COMMENT 'NONE, PM(Property Management), GARC(Resident Committee), GANC(Neighbor Committee), GAPS(Police Station)',
    `name` VARCHAR(64),
    `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address for department', 
	`description` TEXT,
    `path` VARCHAR(128) COMMENT 'path from the root',
    `level` INTEGER NOT NULL DEFAULT 0,
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: inactive, 2: active, 3: locked, 4: mark as deleted',
	`department_type` VARCHAR(64),
    
    PRIMARY KEY(`id`),
    UNIQUE `u_eh_org_name`(`parent_id`, `name`),
    INDEX `i_eh_org_name_level`(`name`, `level`),   
    INDEX `i_eh_org_path`(`path`),
    INDEX `i_eh_org_path_level`(`path`, `level`),   
    INDEX `i_eh_org_parent`(`parent_id`)   
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_communities`;
CREATE TABLE `eh_organization_communities` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `organization_id` BIGINT NOT NULL,
    `community_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_org_community_id`(`organization_id`, `community_id`),
    INDEX `i_eh_orgc_dept`(`organization_id`),
    INDEX `i_eh_orgc_community`(`community_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
# the resourses(building, community, etc.) assigned to the organization
#
DROP TABLE IF EXISTS `eh_organization_assigned_scopes`;
CREATE TABLE `eh_organization_assigned_scopes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: building',
  `scope_id` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
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
	
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_orgm_owner`(`organization_id`) REFERENCES `eh_organizations`(`id`) ON DELETE CASCADE,
	INDEX `i_eh_corg_group`(`member_group`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
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
	
    PRIMARY KEY (`id`),
	FOREIGN KEY `fk_eh_organization`(`organization_id`) REFERENCES `eh_organizations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_organization_address_mappings`;
CREATE TABLE `eh_organization_address_mappings` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `organization_id` BIGINT NOT NULL DEFAULT 0,
    `community_id` BIGINT NOT NULL COMMENT 'community id',
    `address_id` BIGINT NOT NULL COMMENT 'address id',
    
    `organization_address` VARCHAR(128) COMMENT 'organization address used in organization',
	`living_status` TINYINT NOT NULL,
    
    PRIMARY KEY (`id`),
	FOREIGN KEY `fk_eh_organization`(`organization_id`) REFERENCES `eh_organizations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
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
	`notify_count` INT COMMENT 'how many times of notification is sent for the bill',
    `notify_time` DATETIME COMMENT 'the last time of notification for the bill',
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`),
	FOREIGN KEY `fk_eh_organization`(`organization_id`) REFERENCES `eh_organizations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_organization_billing_accounts`;
CREATE TABLE `eh_organization_billing_accounts` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
	`account_number` VARCHAR(128) NOT NULL DEFAULT '0' COMMENT 'the account number which use to identify the unique account',
    `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'organization id',
	`balance` DECIMAL(10,2) NOT NULL DEFAULT 0,
    `update_time` DATETIME,
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_account_number`(`account_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
# the transaction history of paid the bills
#
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
	`charge_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT 'the amount of money paid to target(negative) or received from target(positive)',
	`description` TEXT COMMENT 'the description for the transaction',
	`vendor` VARCHAR(128) DEFAULT '' COMMENT 'which third-part pay vendor is used',
	`pay_account` VARCHAR(128) DEFAULT '' COMMENT 'the pay account from third-part pay vendor',
	`result_code_scope` VARCHAR(128) DEFAULT '' COMMENT 'the scope of result code, defined in zuolin',
	`result_code_id` INT NOT NULL DEFAULT 0 COMMENT 'the code id occording to scope',
	`result_desc` VARCHAR(2048) DEFAULT '' COMMENT 'the description of the transaction',
    `operator_uid` BIGINT COMMENT 'the user is who paid the bill, including help others pay the bill',
	`paid_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: selfpay, 2: agent',
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


#
# member of global partition 
#
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
	FOREIGN KEY `fk_eh_organization_bill`(`bill_id`) REFERENCES `eh_organization_bills`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
# the residents living in the commmunity owned by organization
#
DROP TABLE IF EXISTS `eh_organization_owners`;
CREATE TABLE `eh_organization_owners` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `organization_id` BIGINT NOT NULL DEFAULT 0,
	`contact_name` VARCHAR(64),
	`contact_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
	`contact_token` VARCHAR(128) COMMENT 'phone number or email address',
	`contact_description` TEXT,
    `address_id` BIGINT NOT NULL COMMENT 'address id',
    `address` VARCHAR(128),
    `creator_uid` BIGINT COMMENT 'uid of the user who has the bill',
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`),
	FOREIGN KEY `fk_eh_organization`(`organization_id`) REFERENCES `eh_organizations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
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
	FOREIGN KEY `fk_eh_organization`(`organization_id`) REFERENCES `eh_organizations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Key table in address related sharding group
# shared resources, custom fields may not really be needed
#
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
	
	`area_size` DOUBLE COMMENT 'the area size of the room according to the address',
	`namespace_id` int(11) NOT NULL DEFAULT '0',
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_uuid`(`uuid`),
    INDEX `i_eh_addr_city`(`city_id`),
    INDEX `i_eh_addr_community`(`community_id`),
    INDEX `i_eh_addr_zipcode`(`zipcode`),
    INDEX `i_eh_addr_address`(`address`),
    INDEX `i_eh_addr_geohash`(`geohash`),
    INDEX `i_eh_addr_address_alias`(`address_alias`),
    INDEX `i_eh_addr_building_apt_name`(`building_name`, `apartment_name`),
    INDEX `i_eh_addr_building_alias_apt_name`(`building_alias_name`, `apartment_name`),
    INDEX `i_eh_addr_create_time`(`create_time`),
    INDEX `i_eh_addr_delete_time`(`delete_time`),
    INDEX `i_eh_addr_itag1`(`integral_tag1`),
    INDEX `i_eh_addr_itag2`(`integral_tag2`),
    INDEX `i_eh_addr_stag1`(`string_tag1`),
    INDEX `i_eh_addr_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


#
# member of address related sharding group
#
DROP TABLE IF EXISTS `eh_family_billing_accounts`;
CREATE TABLE `eh_family_billing_accounts` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
	`account_number` VARCHAR(128) NOT NULL DEFAULT '0' COMMENT 'the account number which use to identify the unique account',
    `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address id',
	`balance` DECIMAL(10,2) NOT NULL DEFAULT 0,
    `update_time` DATETIME,
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_account_number`(`account_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of address related sharding group
# the transaction history of paid the bills
#
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
	`charge_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT 'the amount of money paid to target(negative) or received from target(positive)',
	`description` TEXT COMMENT 'the description for the transaction',
	`vendor` VARCHAR(128) DEFAULT '' COMMENT 'which third-part pay vendor is used',
	`pay_account` VARCHAR(128) DEFAULT '' COMMENT 'the pay account from third-part pay vendor',
	`result_code_scope` VARCHAR(128) DEFAULT '' COMMENT 'the scope of result code, defined in zuolin',
	`result_code_id` INT NOT NULL DEFAULT 0 COMMENT 'the code id occording to scope',
	`result_desc` VARCHAR(2048) DEFAULT '' COMMENT 'the description of the transaction',
    `operator_uid` BIGINT COMMENT 'the user is who paid the bill, including help others pay the bill',
	`paid_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: selfpay, 2: agent',
    `create_time` DATETIME,
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups(eh_families) sharding group
# secondary resource objects (after eh_families)
#
DROP TABLE IF EXISTS `eh_family_followers`;
CREATE TABLE `eh_family_followers` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_family` BIGINT NOT NULL,
    `follower_uid` BIGINT NOT NULL,
    `create_time` DATETIME COMMENT 'remove-deletion, user directly managed data',
    
    PRIMARY KEY (`id`),
    UNIQUE `i_fm_follower_follower`(`owner_family`, `follower_uid`),
    INDEX `i_eh_fm_follower_owner`(`owner_family`),
    INDEX `i_eh_fm_follower_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
#
# (scope_type, scope_id) : specifies the banner distribution scope
# name : specifies an administrative banner name if needed
# vendor_tag : a tag specified by external vendor to help associate everhome system and third-party system on the banner
# poster_path: banner display image
# action_name: action name defined by the banner serving application module
# action_uri: action uri, for Everhomes application modules, action URI scheme uses evh:// as prefix, for external systems, URI scheme uses standard http:// or https://
# (start_time, end_time) banner active time period, effective only after status has been put into active state
# status: banner administrative status
# order: default listing order, depends on banner slide-showing algorithm
#
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
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Record banner clicks at per user basis, due to amount of potential users, may
# need to be put at user partition
#
#
DROP TABLE IF EXISTS `eh_banner_clicks`;
CREATE TABLE `eh_banner_clicks`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `uuid` VARCHAR(36) NOT NULL,
    `banner_id` BIGINT NOT NULL,
    `uid` BIGINT NOT NULL,
    `family_id` BIGINT COMMENT 'redundant info for query optimization',
    `click_count` BIGINT,
    `last_click_time` DATETIME,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_banner_clk_uuid`(`uuid`),
    UNIQUE `u_eh_banner_clk_user`(`banner_id`, `uid`),
    INDEX `i_eh_banner_clk_last_time`(`last_click_time`),
    INDEX `i_eh_banner_clk_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 
# Record banner related user purchase, due to amount of potential users, may
# need to be put at user partition
#
DROP TABLE IF EXISTS `eh_banner_orders`;
CREATE TABLE `eh_banner_orders` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `banner_id` BIGINT NOT NULL,
    `uid` BIGINT NOT NULL,
    `vendor_order_tag` VARCHAR(64),
    `amount` DECIMAL,
    `description` TEXT,
    `purchase_time` DATETIME,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_banner_order_banner`(`banner_id`),
    INDEX `i_eh_banner_order_user`(`uid`),
    INDEX `i_eh_banner_order_purchase_time`(`purchase_time`),
    INDEX `i_eh_banner_order_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Resource management
# key table of binary resource management sharding group
#
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
    INDEX `i_eh_bin_res_checksum`(`checksum`),
    INDEX `i_eh_bin_res_create_time`(`create_time`),
    INDEX `i_eh_bin_res_access_time`(`access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Resource management
# key table of rich text resource management sharding group
#
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
    INDEX `i_eh_rtxt_res_checksum`(`checksum`),
    INDEX `i_eh_rtxt_res_create_time`(`create_time`),
    INDEX `i_eh_rtxt_res_access_time`(`access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# key table of event sharding group
# old event subscription table and other event related profile items will be consolidated
# in eh_event_profiles table
#
# Only if there are queries from event to other entities, there is a need to have
# associated field in eh_events table, otherwise, store associated references in
# eh_event_profiles table, for example, associated groups, forums of the event
#
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
    INDEX `i_eh_evt_start_time_ms`(`start_time_ms`),
    INDEX `i_eh_evt_end_time_ms`(`end_time_ms`),
    INDEX `i_eh_evt_creator_uid`(`creator_uid`),
    INDEX `i_eh_evt_create_time`(`create_time`),
    INDEX `i_eh_evt_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of event sharding group
#
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
    UNIQUE `u_eh_evt_roster_uuid`(`uuid`),
    UNIQUE `u_eh_evt_roster_attendee`(`event_id`, `uid`),
    INDEX `i_eh_evt_roster_signup_time`(`signup_time`),
    INDEX `i_eh_evt_roster_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of event sharding group
#
DROP TABLE IF EXISTS `eh_event_ticket_groups`;
CREATE TABLE `eh_event_ticket_groups`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `event_id` BIGINT,
    `name` VARCHAR(32),
    `total_count` INTEGER,
    `allocated_count` INTEGER,
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',

    PRIMARY KEY (`id`),
    UNIQUE `u_eh_evt_tg_name`(`event_id`, `name`),
    INDEX `i_eh_evt_tg_event_id`(`event_id`),
    INDEX `i_eh_evt_tg_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of event sharding group
# secondary resource objects (after eh_events)
#
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
    UNIQUE `u_eh_evt_ticket_ticket`(`ticket_group_id`, `ticket_number`),
    INDEX `i_eh_evt_ticket_event`(`event_id`),
    INDEX `i_eh_evt_ticket_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_events sharding group
# entity profile for eh_events
#
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
    INDEX `i_eh_evt_prof_item`(`owner_id`, `item_name`),
    INDEX `i_eh_evt_prof_owner`(`owner_id`),
    INDEX `i_eh_evt_prof_itag1`(`integral_tag1`),
    INDEX `i_eh_evt_prof_itag2`(`integral_tag2`),
    INDEX `i_eh_evt_prof_stag1`(`string_tag1`),
    INDEX `i_eh_evt_prof_stag2`(`string_tag2`)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# key table of activity sharding group
# first level resource objects
#
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
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_uuid`(`uuid`),
    INDEX `i_eh_act_start_time_ms`(`start_time_ms`),
    INDEX `i_eh_act_end_time_ms`(`end_time_ms`),
    INDEX `i_eh_act_creator_uid`(`creator_uid`),
    INDEX `i_eh_act_post_id`(`post_id`),
    INDEX `i_eh_act_group`(`group_discriminator`, `group_id`),
    INDEX `i_eh_act_create_time`(`create_time`),
    INDEX `i_eh_act_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_activities sharding group
# secondary resource objects (after eh_events)
#
DROP TABLE IF EXISTS `eh_activity_roster`;
CREATE TABLE `eh_activity_roster`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `uuid` VARCHAR(36) NOT NULL,
    `activity_id` BIGINT NOT NULL,
    `uid` BIGINT,
    `family_id` BIGINT,
    `adult_count` INTEGER NOT NULL DEFAULT 0,
    `child_count` INTEGER NOT NULL DEFAULT 0,
    `checkin_flag` TINYINT NOT NULL DEFAULT 0,
    `checkin_uid` BIGINT comment 'id of checkin user',
    `confirm_flag` BIGINT NOT NULL DEFAULT 0,
    `confirm_uid` BIGINT,
    `confirm_family_id` BIGINT,
    `confirm_time` DATETIME,
    `lottery_flag` TINYINT NOT NULL DEFAULT 0,
    `lottery_time` DATETIME,
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_act_roster_uuid`(`uuid`),
    UNIQUE `u_eh_act_roster_user`(`activity_id`, `uid`),
    INDEX `i_eh_act_roster_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# key table of polling management sharding group
# First level resource objects
#
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
	UNIQUE `u_eh_uuid`(`uuid`),
    INDEX `i_eh_poll_start_time_ms`(`start_time_ms`),
    INDEX `i_eh_poll_end_time_ms`(`end_time_ms`),
    INDEX `i_eh_poll_creator_uid`(`creator_uid`),
    INDEX `i_eh_poll_post_id`(`post_id`),
    INDEX `i_eh_poll_create_time`(`create_time`),
    INDEX `i_eh_poll_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_polls partition
# secondary resource objects (after eh_polls)
#
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
    INDEX `i_eh_poll_item_poll`(`poll_id`),
    INDEX `i_eh_poll_item_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_polls partition
# secondary resource objects(after eh_pools)
#
DROP TABLE IF EXISTS `eh_poll_votes`;
CREATE TABLE `eh_poll_votes`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `poll_id` BIGINT,
    `item_id` BIGINT,
    `voter_uid` BIGINT,
    `voter_family_id` BIGINT,
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`),
    UNIQUE `i_eh_poll_vote_voter`(`poll_id`, `item_id`, `voter_uid`),
    INDEX `i_eh_poll_vote_poll`(`poll_id`),
    INDEX `i_eh_poll_vote_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


#
# member of global parition
# shared among namespaces, no application module specific information
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_client_package_files`;
CREATE TABLE `eh_client_package_files`(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
	`package_id` BIGINT,
	`file_location` VARCHAR(256),
	`file_name` VARCHAR(128),
	`file_size` BIGINT,
	`file_md5` VARCHAR(64),
	
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_cpkg_file_package`(`package_id`) REFERENCES `eh_client_packages`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_user_locations`;
CREATE TABLE `eh_user_locations`(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uid` BIGINT(20) NOT NULL DEFAULT '0',
    `longitude` DOUBLE DEFAULT NULL,
    `latitude` DOUBLE DEFAULT NULL,
    `geohash` VARCHAR(128) DEFAULT '',
    `create_time` DATETIME DEFAULT NULL,
    `collect_time_ms` BIGINT(20) NOT NULL DEFAULT '0',
    `report_time_ms` BIGINT(20) NOT NULL DEFAULT '0',   
    PRIMARY KEY  (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS  `eh_user_behaviors`;
CREATE TABLE `eh_user_behaviors`(
    `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `uid` BIGINT(20) NOT NULL DEFAULT '0',
    `content_type` TINYINT(4) NOT NULL DEFAULT '0',
    `content` TEXT,
    `collect_time_ms` BIGINT(20) NOT NULL DEFAULT '0',
    `report_time_ms` BIGINT(20) NOT NULL DEFAULT '0',
    `create_time` DATETIME DEFAULT NULL,
     PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS  `eh_user_contacts`;
CREATE TABLE `eh_user_contacts`(
      `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
      `uid` BIGINT(20) NOT NULL DEFAULT '0',
      `contact_id` BIGINT(20) NOT NULL DEFAULT '0',
      `contact_phone` VARCHAR(32) DEFAULT '',
      `contact_name` VARCHAR(128) DEFAULT '',
      `create_time` DATETIME DEFAULT NULL,
      PRIMARY KEY  (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS  `eh_user_installed_apps`;
CREATE TABLE `eh_user_installed_apps`(
      `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
      `uid` BIGINT(20) NOT NULL DEFAULT '0',
      `app_name` VARCHAR(1024) DEFAULT '',
      `app_version` VARCHAR(128) DEFAULT '',
      `app_size` VARCHAR(128) DEFAULT '',
      `app_installed_time` VARCHAR(128) DEFAULT '',
      `collect_time_ms` BIGINT(20) NOT NULL DEFAULT '0',
      `report_time_ms` BIGINT(20) NOT NULL DEFAULT '0',
      `create_time` DATETIME DEFAULT NULL,
      PRIMARY KEY  (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_user_activities`;
CREATE TABLE if NOT exists `eh_user_activities` (
      `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
      `uid` BIGINT(20) NOT NULL DEFAULT '0',
      `activity_type` TINYINT(4) NOT NULL DEFAULT '0',
      `app_version_code` BIGINT(20) NOT NULL DEFAULT '0',
      `app_version_name` VARCHAR(128) DEFAULT '',
      `channel_id` BIGINT(20) NOT NULL DEFAULT '0',
      `imei_number` VARCHAR(128) DEFAULT '',
      `device_type` VARCHAR(512) DEFAULT '',
      `os_info` VARCHAR(512) DEFAULT '',
      `os_type` TINYINT(4) NOT NULL DEFAULT '0',
      `mkt_data_version` BIGINT(20) NOT NULL DEFAULT '0',
      `report_config_version` BIGINT(20) NOT NULL DEFAULT '0',
      `internal_ip` VARCHAR(128) DEFAULT '',
      `external_ip` VARCHAR(128) DEFAULT '',
      `user_agent` VARCHAR(1024) DEFAULT '',
      `collect_time_ms` BIGINT(20) NOT NULL DEFAULT '0',
      `report_time_ms` BIGINT(20) NOT NULL DEFAULT '0',
      `create_time` DATETIME DEFAULT NULL,
      PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_content_server_resources`;
CREATE  TABLE  `eh_content_server_resources` (
	`id` BIGINT NOT NULL COMMENT "the id of record",
    `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
	`resource_id` TEXT NOT NULL,
	`resource_md5` VARCHAR(256) NOT NULL,
	`resource_type` INT NOT NULL COMMENT 'current support audio,image and video',
	`resource_size` INT NOT NULL,
	`resource_name` VARCHAR(128) NOT NULL, 
	`metadata` text,
	PRIMARY  KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_content_server`;
CREATE TABLE  `eh_content_server` (
        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'content server id',
	  `name` VARCHAR(32),
	  `description` VARCHAR(40),
        `private_address` VARCHAR(32),
        `private_port` INT(11),
        `public_address` VARCHAR(32) NOT NULL,
        `public_port` INT(11) NOT NULL,
        PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_suggestions`;
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

#
# member of eh_users partition
# the result of recommendations
#
DROP TABLE IF EXISTS `eh_recommendations`;
CREATE TABLE `eh_recommendations` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appId` BIGINT,
  `suggest_type` int(11) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL DEFAULT '0',
  `source_type` int(11) NOT NULL DEFAULT '0',
  `source_id` bigint(20) NOT NULL DEFAULT '0',
  `embedded_json` TEXT,
  `max_count` int(11) NOT NULL DEFAULT '0',
  `score` double NOT NULL DEFAULT '0',
  `status` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `expire_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_eh_recommendations_user_idx` (`user_id`),
  CONSTRAINT `fk_eh_recommendations_user_idx` FOREIGN KEY (`user_id`) REFERENCES `eh_users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
# the configurations recommended by admin
#
DROP TABLE IF EXISTS `eh_recommendation_configs`;
CREATE TABLE `eh_recommendation_configs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appId` BIGINT,
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
  `embedded_json` TEXT,
  `description` varchar(1024) DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
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
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of address partition
# the message of this address
#
DROP TABLE IF EXISTS `eh_address_messages`;
CREATE TABLE `eh_address_messages` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `appId` BIGINT,
  `address_id` bigint(11) NOT NULL,
  `sender_type` VARCHAR(32),
  `sender_token` VARCHAR(32),
  `sender_login_id` int,
  `sender_identify` VARCHAR(128),
  `body` VARCHAR(256),
  `meta` VARCHAR(512),
  `extra` VARCHAR(512),
  `sender_tag` VARCHAR(32),
  `body_type` VARCHAR(32),
  `deliveryOption` INT NOT NULL,
  `create_time` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of user-related sharding group
# shared among namespaces, no application module specific information
#
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
) engine=innodb default charset=utf8mb4;

#
# member of user-related sharding group
# shared among namespaces, no application module specific information
#
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
  `content_category` BIGINT NOT NULL DEFAULT 0 COMMENT '0: other, 1: product bug, 2: product improvement, 3: version problem, 11: sensitive info, 12: copyright problem, 13: violent pornography, 14: fraud&fake, 15: disturbance, 21: rumor, 22: malicious marketing, 23: induction',
  `proof_resource_uri` VARCHAR(1024),
  PRIMARY KEY (`id`)
) engine=innodb DEFAULT charset=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
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
  `address` VARCHAR(1024) COMMENT '',
  `description` TEXT NOT NULL,
  `update_time` DATETIME,
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `visible_distance` DOUBLE DEFAULT 5000 COMMENT 'the distance between shop and user who can find the shop, unit: meter',
  
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_business_visible_scopes`;
CREATE TABLE `eh_business_visible_scopes` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner business id',
    `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city',
    `scope_id` BIGINT,
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_scope_owner_code_id`(`owner_id`, `scope_code`, `scope_id`),
    FOREIGN KEY `fk_eh_bussiness_scope_owner`(`owner_id`) REFERENCES `eh_businesses`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_bussiness_scope_owner_id`(`owner_id`),
    INDEX `i_eh_bussiness_scope_target`(`scope_code`, `scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_business_assigned_scopes`;
CREATE TABLE `eh_business_assigned_scopes` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner business id',
    `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city',
    `scope_id` BIGINT,
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_scope_owner_code_id`(`owner_id`, `scope_code`, `scope_id`),
    FOREIGN KEY `fk_eh_bussiness_scope_owner`(`owner_id`) REFERENCES `eh_businesses`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_bussiness_scope_owner_id`(`owner_id`),
    INDEX `i_eh_bussiness_scope_target`(`scope_code`, `scope_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_business_categories`;
CREATE TABLE `eh_business_categories` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner business id',
    `category_id` BIGINT NOT NULL DEFAULT 0,
    `category_path` VARCHAR(128),
    
    PRIMARY KEY (`id`),
	UNIQUE `u_eh_bussiness_category_id`(`owner_id`, `category_id`),
    FOREIGN KEY `fk_eh_bussiness_category`(`owner_id`) REFERENCES `eh_businesses`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_bussiness_owner_id`(`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global sharding group
#
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
    UNIQUE `u_eh_ocode_code`(`code`),
    INDEX `i_eh_ocode_expiration_time`(`expiration_time`),
    INDEX `i_eh_ocode_create_time`(`create_time`),
    INDEX `i_eh_ocode_modify_time`(`modify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global sharding group
#
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
    UNIQUE `u_eh_otoken_token_string`(`token_string`),
    INDEX `i_eh_otoken_expiration_time`(`expiration_time`),
    INDEX `i_eh_otoken_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_qrcodes`;
CREATE TABLE `eh_qrcodes` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`description` VARCHAR(1024) COMMENT '',
    `view_count` BIGINT NOT NULL DEFAULT 0,
	`logo_uri` VARCHAR(1024) COMMENT '',
	`expire_time` DATETIME COMMENT 'it is permanent if there is no expired time, else it is temporary',
	`action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'according to document',
    `action_data` TEXT COMMENT 'the parameters depend on item_type, json format',
    `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
    `creator_uid` BIGINT NOT NULL COMMENT 'createor user id',
	`create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global sharding group
#
DROP TABLE IF EXISTS `eh_version_realm`;
CREATE TABLE `eh_version_realm` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `realm` VARCHAR(128),
    `description` TEXT,
    
    `create_time` DATETIME,
	
    PRIMARY KEY(`id`),
    UNIQUE `u_eh_ver_realm`(`realm`),
    INDEX `i_eh_ver_realm_create_time`(`create_time`)
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
    
    PRIMARY KEY(`id`),
    FOREIGN KEY `fk_eh_ver_upgrade_realm`(`realm_id`) REFERENCES `eh_version_realm`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_ver_upgrade_order`(`order`),
    INDEX `i_eh_ver_upgrade_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_version_urls`;
CREATE TABLE `eh_version_urls` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `realm_id` BIGINT NOT NULL,
    `target_version` VARCHAR(128),
    `download_url` VARCHAR(128) COMMENT 'example configuration: http://serviceurl/download/client-packages/${locale}/andriod-${major}-${minor}-${revision}.apk',
    `info_url` VARCHAR(128) COMMENT 'example configuration: http://serviceurl/download/client-package-info/${locale}/andriod-${major}-${minor}-${revision}.html',
    	
    PRIMARY KEY(`id`),
    UNIQUE `u_eh_ver_url`(`realm_id`, `target_version`)
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
    
    PRIMARY KEY(`id`),
    FOREIGN KEY `fk_eh_ver_content_realm`(`realm_id`) REFERENCES `eh_version_realm`(`id`) ON DELETE CASCADE ,
    INDEX `i_eh_ver_content_order`(`order`),
    INDEX `i_eh_ver_content_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

DROP TABLE IF EXISTS `eh_push_messages`;
CREATE TABLE `eh_push_messages` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `message_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'NORMAL_MESSAGE, UPGRADE_MESSAGE, NOTIFY_MESSAGE',
    `title` VARCHAR(128) COMMENT 'title of message',
    `content` VARCHAR(4096) COMMENT 'content for message',
    `target_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'CITY, COMMUNITY, FAMILY, USER',
    `target_id` BIGINT NOT NULL DEFAULT 0,
    `status` INT NOT NULL DEFAULT 0 COMMENT 'WAITING, RUNNING, FINISHED',
    `create_time` DATETIME DEFAULT NULL,
    `start_time` DATETIME DEFAULT NULL,
    `finish_time` DATETIME DEFAULT NULL,
    `device_type` VARCHAR(64),
    `device_tag` VARCHAR(64),
    `app_version` VARCHAR(64),
    `push_count` BIGINT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_push_message_results`;
CREATE TABLE `eh_push_message_results` (
    `id` BIGINT NOT NULL COMMENT 'id of the push message result, not auto increment',
    `message_id` BIGINT NOT NULL DEFAULT 0,
    `user_id` BIGINT NOT NULL DEFAULT 0,
    `identifier_token` VARCHAR(128) COMMENT 'The mobile phone of user',
    `send_time` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups sharding group
#
DROP TABLE IF EXISTS `eh_enterprise_attachments`;
CREATE TABLE `eh_enterprise_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;# 

#
# member of eh_groups partition
# the supplement enterprise info of eh_groups
#
DROP TABLE IF EXISTS `eh_enterprise_details`;
CREATE TABLE `eh_enterprise_details` ( 
	`id` BIGINT NOT NULL COMMENT 'id of the record', 
	`enterprise_id` BIGINT NOT NULL COMMENT 'group id', 
	`description` text COMMENT 'description', 
	`contact` VARCHAR(128) COMMENT 'the phone number',
	`address` VARCHAR(256) COMMENT 'address str', 
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `geohash` VARCHAR(32),
    `create_time` DATETIME,
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4; 

# 
# member of global partition
#
DROP TABLE IF EXISTS `eh_enterprise_op_requests`;
CREATE TABLE `eh_enterprise_op_requests` ( 
	`id` BIGINT NOT NULL COMMENT 'id of the record', 
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `community_id` BIGINT NOT NULL DEFAULT 0,
    `source_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'enterprise, marker zone',
    `source_id` BIGINT NOT NULL DEFAULT 0,
	`enterprise_name` VARCHAR(128) NOT NULL COMMENT 'enterprise name', 
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
	
	PRIMARY KEY (`id`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups partition
# the relationship between eh_enterprises and eh_enterprise_communities
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups partition
# the relationship between eh_enterprises and eh_enterprise_communities
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups partition
# entry info of eh_enterprise_contacts
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups partition
# internal group in enterprise, use for department
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups partition
# role of member inside the group (internal)
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities partition
# the relationship between eh_enterprises and eh_enterprise_communities
#
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities partition
#
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
	`namespace_id` int(11) NOT NULL DEFAULT '0',
    
	UNIQUE `u_eh_community_id_name`(`community_id`, `name`),
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_communities sharding group
#
DROP TABLE IF EXISTS `eh_building_attachments`;
CREATE TABLE `eh_building_attachments` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `building_id` BIGINT NOT NULL DEFAULT 0,
    `content_type` VARCHAR(32) COMMENT 'attachment object content type',
    `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
    `creator_uid` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global partition
# the resources related to the namespace
#
DROP TABLE IF EXISTS `eh_namespace_resources`;
CREATE TABLE `eh_namespace_resources` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `resource_type` VARCHAR(128) COMMENT 'COMMUNITY',
    `resource_id` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_namespace_resource_id`(`namespace_id`, `resource_type`, `resource_id`),
    INDEX `i_eh_resource_id`(`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


SET foreign_key_checks = 1;
