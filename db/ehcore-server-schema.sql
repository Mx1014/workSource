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

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_categories`;
CREATE TABLE `eh_categories`(
    `id` BIGINT NOT NULL,
    `parent_id` BIGINT,
    `link_id` BIGINT COMMENT 'point to the linked category (similar to soft link in file system)',
    `name` VARCHAR(64) NOT NULL,
    `path` VARCHAR(128),
    `default_order` INTEGER,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
    `create_time` DATETIME,
    `delete_time` DATETIME COMMENT 'mark-deletion policy. It is much more safer to do so if an allocated category is broadly used', 
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_category_name`(`parent_id`, `name`),
    INDEX `i_eh_category_path`(`path`),
    INDEX `i_eh_category_order`(`default_order`),
    INDEX `i_eh_category_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
    `namespace_id` INTEGER,
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
);

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_feedbacks`;
CREATE TABLE `eh_feedbacks`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `uid` BIGINT,
    `business_id` BIGINT,
    `subject` VARCHAR(256),
    `content` TEXT,
    `create_time` DATETIME,
    `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_feedback_create_time`(`create_time`),
    INDEX `i_eh_feedback_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_audit_logs`;
CREATE TABLE `eh_audit_logs`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `app_id` BIGINT COMMENT 'application that provides the operation',
    `operator_uid` BIGINT,
	`requestor_uid` BIGINT COMMENT 'user who initiated the original request',
    `operation_name` VARCHAR(32),
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of user-related partition group
#
DROP TABLE IF EXISTS `eh_users`;
CREATE TABLE `eh_users` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `account_name` VARCHAR(64) NOT NULL,
    `nick_name` VARCHAR(32),
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

    #
    # for work/school based matching
    #
    `occupation_id` BIGINT COMMENT 'id in category table',
    `company` VARCHAR(128),
    `school` VARCHAR(128),
    
    `locale` VARCHAR(16) COMMENT 'zh_CN, en_US etc',
    
    `invite_type` TINYINT COMMENT '1: SMS, 2: wechat, 3, wechat friend circle, 4: weibo, 5: phone contact',
    `invitor_uid` BIGINT,
    `create_time` DATETIME NOT NULL,
    `delete_time` DATETIME COMMENT 'mark-deletion policy. may be valuable for user to restore account',
    `last_login_time` DATETIME,
    `last_login_ip` VARCHAR(16),

    `salt` VARCHAR(64),
    `password_hash` VARCHAR(128) DEFAULT '' COMMENT 'Note, password is stored as salted hash, salt is appended by hash together',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_user_account_name`(`account_name`),
    INDEX `i_eh_user_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# populate default system user root/password
#
INSERT INTO `eh_users`(`id`, `account_name`, `nick_name`, `status`, `create_time`, `password_hash`) VALUES (1, 'root', 'system user', 1, 
    NOW(), '10:8e70e9c1ebf861202a28ed0020c4db0f4d9a3a3d29fb1c4d:40d84ad3b14b8da5575274136678ca1ab07d114e1d04ef70');

#
# Reserve first 1000 user ids
#
INSERT INTO `eh_sequences`(`domain`, `start_seq`) VALUES ('EhUsers', 1000);

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
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_user_idf_owner_type_token`(`owner_uid`, `identifier_type`, `identifier_token`),
    INDEX `i_eh_user_idf_owner`(`owner_uid`),
    INDEX `i_eh_user_idf_type_token`(`identifier_type`, `identifier_token`),
    INDEX `i_eh_user_idf_create_time`(`create_time`),
    INDEX `i_eh_user_idf_notify_time`(`notify_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
    `leaf_region_path` VARCHAR(128) COMMENT 'redundant group info to help region-based group user search',
    
    `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'default to ResourceUser role', 
    `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance, 3: active',
    `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_grp_owner_group`(`owner_uid`, `group_id`),
    INDEX `i_eh_usr_grp_owner`(`owner_uid`),
    INDEX `i_eh_usr_grp_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_user_invitation_roster`;
CREATE TABLE `eh_user_invitation_roster` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `invite_id` BIGINT COMMENT 'owner invitation record id',
    `name` VARCHAR(64),
    `contact` VARCHAR(64),
    
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_invite_roster_invite_id`(`invite_id`) REFERENCES `eh_user_invitations`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_users partition group
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_users partition group
#
DROP TABLE IF EXISTS `eh_user_favorates`;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_users partition group
#
DROP TABLE IF EXISTS `eh_user_profiles`;
CREATE TABLE `eh_user_profiles`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
	`app_id` BIGINT,    
    `owner_id` BIGINT NOT NULL COMMENT 'owner user id',
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
    INDEX `i_eh_uprof_item`(`app_id`, `owner_id`, `item_name`),
    INDEX `i_eh_uprof_owner`(`owner_id`),
    INDEX `i_eh_uprof_itag1`(`integral_tag1`),
    INDEX `i_eh_uprof_itag2`(`integral_tag2`),
    INDEX `i_eh_uprof_stag1`(`string_tag1`),
    INDEX `i_eh_uprof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of grouping related partition group
# Usually there is no need for group object to carry information for other applications, therefore there is
# not an app_id field, we still put some custom fields here, as group table may be inherited(records of subclassed object
# will be identified by discriminator), these custom fields may be used for such purpose
# 
DROP TABLE IF EXISTS `eh_groups`;
CREATE TABLE `eh_groups` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER,
    `name` VARCHAR(128) NOT NULL,
    `display_name` VARCHAR(64),
    `avatar` VARCHAR(256),
    `description` TEXT,
    `creator_uid` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL,
    `delete_time` DATETIME COMMENT 'mark-deletion policy, multi-purpose base entity',
    `private_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
    `join_policy` INTEGER NOT NULL DEFAULT 0 COMMENT '0: free join(public group), 1: should be approved by operator/owner, 2: invite only',
    `discriminator` VARCHAR(32),
    
    `region_scope` TINYINT COMMENT 'define the group visibiliy region',
    `region_scope_id` BIGINT COMMENT 'region information, could be an id in eh_regions table or an id in eh_communities',
    `leaf_region_path` VARCHAR(128) COMMENT 'leaf region path if the group is aassociated with a managed region',
    
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
    `member_count` BIGINT NOT NULL DEFAULT 0,
    
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
    UNIQUE `u_eh_group_name`(`namespace_id`, `name`, `discriminator`),
    INDEX `i_eh_group_creator`(`creator_uid`),
    INDEX `i_eh_group_leaf_region_path`(`leaf_region_path`),
    INDEX `i_eh_group_create_time` (`create_time`),
    INDEX `i_eh_group_delete_time` (`delete_time`),
    INDEX `i_eh_group_itag1`(`integral_tag1`),
    INDEX `i_eh_group_itag2`(`integral_tag2`),
    INDEX `i_eh_group_stag1`(`string_tag1`),
    INDEX `i_eh_group_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_groups partition group
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of its own partition group
# The reason for it to have its own partition group is that there is potential for group to
# have large amount of members
#
DROP TABLE IF EXISTS `eh_group_members`;
CREATE TABLE `eh_group_members` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `group_id` BIGINT NOT NULL,
    `member_type` VARCHAR(32) NOT NULL COMMENT 'member object type, for example, type could be User, Group, etc',
    `member_id` BIGINT,
    `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'Default to ResourceUser role',
    `member_avatar` VARCHAR(128) COMMENT 'avatar image identifier in storage sub-system',
  	`member_nick_name` VARCHAR(32) COMMENT 'member nick name within the group',
    `member_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
    `create_time` DATETIME NOT NULL COMMENT 'remove-deletion policy, user directly managed data',
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `operator_uid` BIGINT COMMENT 'redundant auditing info',
    `process_code` TINYINT,
    `process_details` TEXT,
    `proof_resource_url` VARCHAR(128),
    `approve_time` DATETIME COMMENT 'redundant auditing info',
    `requestor_comment` TEXT,
    
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
    UNIQUE `u_eh_grp_member` (`group_id`, `member_type`, `member_id`),
    INDEX `i_eh_grp_member_group_id` (`group_id`),
    INDEX `i_eh_grp_member_member` (`member_type`, `member_id`),
    INDEX `i_eh_grp_member_create_time` (`create_time`),
    INDEX `i_eh_grp_member_approve_time` (`approve_time`),
    INDEX `i_eh_gprof_itag1`(`integral_tag1`),
    INDEX `i_eh_gprof_itag2`(`integral_tag2`),
    INDEX `i_eh_gprof_stag1`(`string_tag1`),
    INDEX `i_eh_gprof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_regions`;
CREATE TABLE `eh_regions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `parent_id` BIGINT COMMENT 'id of the parent region', 
	`name` VARCHAR(64),
	`path` VARCHAR(128) COMMENT 'path from the root',
	`level` INTEGER NOT NULL DEFAULT 0,
    `scope_code` TINYINT COMMENT '0 : country, 1: state/province, 2: city, 3: area, 4: community neighborhood',
	`iso_code` VARCHAR(32) COMMENT 'international standard code for the region if exists',
	`tel_code` VARCHAR(32) COMMENT 'primary telephone area code',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: inactive, 2: active, 3: locked, 4: mark as deleted',
    
    PRIMARY KEY(`id`),
    UNIQUE `u_eh_region_name`(`parent_id`, `name`),
 	INDEX `i_eh_region_name_level`(`name`, `level`),   
    INDEX `i_eh_region_path`(`path`),
 	INDEX `i_eh_region_path_level`(`path`, `level`),   
 	INDEX `i_eh_region_parent`(`parent_id`)   
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of the partition group
# shared resource objects, custom fields may not really be needed
#
DROP TABLE IF EXISTS `eh_communities`;
CREATE TABLE `eh_communities`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `city_id` BIGINT NOT NULL COMMENT 'city id in region table',
    `city_name` VARCHAR(64) COMMENT 'redundant for query optimization',
    `area_id` BIGINT NOT NULL COMMENT 'area id in region table',
    `area_name` VARCHAR(64) COMMENT 'redundant for query optimization',
    `name` VARCHAR(64),
    `alias_name` VARCHAR(64),
    `address` VARCHAR(128),
    `zipcode` VARCHAR(16),
    `description` TEXT,
    `detail_description` TEXT,
    `apt_segment1` VARCHAR(64),
    `apt_segment2` VARCHAR(64),
    `apt_segment3` VARCHAR(64),
    `apt_seg1_sample` VARCHAR(64),
    `apt_seg2_sample` VARCHAR(64),
    `apt_seg3_sample` VARCHAR(64),
    `apt_count` INTEGER,
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
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_community_area_name`(`area_name`),
    INDEX `i_eh_community_name`(`name`),
    INDEX `i_eh_community_alias_name`(`alias_name`),
    INDEX `i_eh_community_address`(`address`),
    INDEX `i_eh_community_zipcode`(`zipcode`),
    INDEX `i_eh_community_create_time`(`create_time`),
    INDEX `i_eh_community_delete_time`(`delete_time`),
    INDEX `i_eh_community_itag1`(`integral_tag1`),
    INDEX `i_eh_community_itag2`(`integral_tag2`),
    INDEX `i_eh_community_stag1`(`string_tag1`),
    INDEX `i_eh_community_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_communities partition
#
DROP TABLE IF EXISTS `eh_community_geopoints`;
CREATE TABLE `eh_community_geopoints` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `community_id` BIGINT,
	`description` VARCHAR(256),
	`longitude` DOUBLE,
	`latitude` DOUBLE,
	`geohash` VARCHAR(32),
	
    PRIMARY KEY (`id`),
    INDEX `i_eh_comm_description`(`description`),
	INDEX `i_eh_comm_geopoints`(`geohash`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_communities partition
# information of community forum, admin group will be managed in community profile 
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Key table in address related partition group
# shared resources, custom fields may not really be needed
#
DROP TABLE IF EXISTS `eh_addresses`;
CREATE TABLE `eh_addresses` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `community_id` BIGINT COMMENT 'NULL: means it is an independent street address, otherwise, it is an appartment address',
    `city_id` BIGINT,
    `zipcode` VARCHAR(16),
    `address` VARCHAR(128),
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `geohash` VARCHAR(32),
    `address_alias` VARCHAR(128),
    `building_name` VARCHAR(128),
    `building_alias_name` VARCHAR(128),
    `appartment_name` VARCHAR(128),
    `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: confirming, 2: active',
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
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_addr_city`(`city_id`),
    INDEX `i_eh_addr_zipcode`(`zipcode`),
    INDEX `i_eh_addr_address`(`address`),
    INDEX `i_eh_addr_geohash`(`geohash`),
    INDEX `i_eh_addr_address_alias`(`address_alias`),
    INDEX `i_eh_addr_building_apt_name`(`building_name`, `appartment_name`),
    INDEX `i_eh_addr_building_alias_apt_name`(`building_alias_name`, `appartment_name`),
    INDEX `i_eh_addr_create_name`(`create_time`),
    INDEX `i_eh_addr_delete_name`(`delete_time`),
    INDEX `i_eh_addr_itag1`(`integral_tag1`),
    INDEX `i_eh_addr_itag2`(`integral_tag2`),
    INDEX `i_eh_addr_stag1`(`string_tag1`),
    INDEX `i_eh_addr_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_groups(eh_families) partition group
# secondary resource objects (after eh_families)
#
DROP TABLE IF EXISTS `eh_family_followers`;
CREATE TABLE `eh_family_followers` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_family` BIGINT NOT NULL,
    `follower_family` BIGINT NOT NULL,
    `alias_name` VARCHAR(64),
    `create_time` DATETIME COMMENT 'remove-deletion, user directly managed data',
    
    PRIMARY KEY (`id`),
    UNIQUE `i_fm_follower_follower`(`owner_family`, `follower_family`),
    INDEX `i_eh_fm_follower_owner`(`owner_family`),
    INDEX `i_eh_fm_follower_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_groups(eh_families) partition group
# secondary resource objects (after eh_families)
#
DROP TABLE IF EXISTS `eh_followed_families`;
CREATE TABLE `eh_followed_families`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_family` BIGINT NOT NULL,
    `followed_family` BIGINT NOT NULL,
    `alias_name` VARCHAR(64),
    
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`),
    UNIQUE `i_eh_fm_followed_followed`(`owner_family`, `followed_family`),
    INDEX `i_eh_fm_followed_owner`(`owner_family`),
    INDEX `i_eh_fm_followed_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of the partition group
# first level resource objects
#
DROP TABLE IF EXISTS `eh_banners`;
CREATE TABLE `eh_banners` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER,
    `appId` BIGINT,
    `name` VARCHAR(128),
    `description` TEXT,
    `banner_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: advertisement, 2: backend',
    `vendor_tag` VARCHAR(64),
    `flow_type` TINYINT COMMENT '1: event, 2: slot machine, 3: merchandiser',
    `flow_data` TEXT,
    `resource_id` BIGINT,
    `resource_url` VARCHAR(512), 
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: closed, 1: waiting for confirmation, 2: active',
    `group_id` BIGINT COMMENT 'point to the group created for the banner',
    `forum_id` BIGINT COMMENT 'point to the forum created for the banner',
    `order` INTEGER NOT NULL DEFAULT 0,
    `create_time` DATETIME,
    `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_banner_create_time`(`create_time`),
    INDEX `i_eh_banner_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_banners partition
# banner distribution scope will be managed through banner profile in item group named as 'scope'
#
DROP TABLE IF EXISTS `eh_banner_profiles`;
CREATE TABLE `eh_banner_profiles` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `app_id` BIGINT,
    `owner_id` BIGINT NOT NULL COMMENT 'owner banner id',
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
    INDEX `i_eh_bprof_item`(`app_id`, `owner_id`, `item_name`),
    INDEX `i_eh_bprof_owner`(`owner_id`),
    INDEX `i_eh_bprof_itag1`(`integral_tag1`),
    INDEX `i_eh_bprof_itag2`(`integral_tag2`),
    INDEX `i_eh_bprof_stag1`(`string_tag1`),
    INDEX `i_eh_bprof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_banners partition
# secondary resource objects (after eh_banners)
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_banners partition
# secondary resource objects(after eh_banners)
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Resource management
# key table of binary resource management partition group
#
DROP TABLE IF EXISTS `eh_binary_resources`;
CREATE TABLE `eh_binary_resources` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Resource management
# key table of rich text resource management partition group
#
DROP TABLE IF EXISTS `eh_rtxt_resources`;
CREATE TABLE `eh_rtxt_resources`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of event partition group
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
    `namespace_id` INTEGER,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of event partition group
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of event partition group
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of event partition group
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_events partition group
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
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of activity partition group
# first level resource objects
#
DROP TABLE IF EXISTS `eh_activities`;
CREATE TABLE `eh_activities` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER,
    `subject` VARCHAR(512),
    `description` TEXT,
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
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_act_start_time_ms`(`start_time_ms`),
    INDEX `i_eh_act_end_time_ms`(`end_time_ms`),
    INDEX `i_eh_act_creator_uid`(`creator_uid`),
    INDEX `i_eh_act_post_id`(`post_id`),
    INDEX `i_eh_act_group`(`group_discriminator`, `group_id`),
    INDEX `i_eh_act_create_time`(`create_time`),
    INDEX `i_eh_act_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_activities partition group
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
    `signup_flag` TINYINT NOT NULL DEFAULT 0,
    `signup_uid` BIGINT,
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of polling management partition group
# First level resource objects
#
DROP TABLE IF EXISTS `eh_polls`;
CREATE TABLE `eh_polls` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER,
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
    INDEX `i_eh_poll_start_time_ms`(`start_time_ms`),
    INDEX `i_eh_poll_end_time_ms`(`end_time_ms`),
    INDEX `i_eh_poll_creator_uid`(`creator_uid`),
    INDEX `i_eh_poll_post_id`(`post_id`),
    INDEX `i_eh_poll_create_time`(`create_time`),
    INDEX `i_eh_poll_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of business management partition group
# First level resource objects
#
DROP TABLE IF EXISTS `eh_business`;
CREATE TABLE `eh_business`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `namespace_id` INTEGER,
    `name` VARCHAR(128),
    `contact_number` VARCHAR(64), 
    `category_id` BIGINT,
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `geohash` VARCHAR(64),
    `change_version` INTEGER,
    `create_time` DATETIME,
    `delete_time` DATETIME COMMENT 'mark-deletion policy, historic data may be valuable',
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_biz_name`(`name`),
    INDEX `i_eh_biz_geohash`(`geohash`),
    INDEX `i_eh_biz_create_time`(`create_time`),
    INDEX `i_eh_biz_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_business partition group
# entity profile info for eh_business objects
#
DROP TABLE IF EXISTS `eh_business_profiles`;
CREATE TABLE `eh_business_profiles`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `app_id` BIGINT,
    `owner_id` BIGINT NOT NULL COMMENT 'owner user id',
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
    INDEX `i_eh_biz_prof_item`(`app_id`, `owner_id`, `item_name`),
    INDEX `i_eh_biz_prof_owner`(`owner_id`),
    INDEX `i_eh_biz_prof_itag1`(`integral_tag1`),
    INDEX `i_eh_biz_prof_itag2`(`integral_tag2`),
    INDEX `i_eh_biz_prof_stag1`(`string_tag1`),
    INDEX `i_eh_biz_prof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_business partition group
# secondary resource objects (after owner eh_business object)
#
DROP TABLE IF EXISTS `eh_biz_coupon_groups`;
CREATE TABLE `eh_biz_coupon_groups`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `business_id` BIGINT,
    `name` VARCHAR(128),
    `description` TEXT,
    `verification_code` VARCHAR(64),
    `tag` VARCHAR(500),
    `rank` INTEGER,
    `link_url` VARCHAR(256),
    `status` TINYINT,
    `start_time` DATETIME,
    `expire_time` DATETIME,
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_biz_grp_business_id`(`business_id`),
    INDEX `i_eh_biz_grp_name`(`name`),
    INDEX `i_eh_biz_grp_start_time`(`start_time`),
    INDEX `i_eh_biz_grp_expire_time`(`expire_time`),
    INDEX `i_eh_biz_grp_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_business partition group
# secondary resource objects (after owner eh_business object)
#
DROP TABLE IF EXISTS `eh_biz_coupon`;
CREATE TABLE `eh_biz_coupon`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `business_id` BIGINT,
    `coupon_group_id` BIGINT,
    `coupon_number` VARCHAR(128),
    `uid` BIGINT,
    `family_id` BIGINT,
    `status` TINYINT,
    `create_time` DATETIME COMMENT 'remove-deletion policy, user directly managed data',
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_biz_coupon_business_id`(`business_id`),    
    INDEX `i_eh_biz_coupon_number`(`coupon_group_id`, `coupon_number`),    
    INDEX `i_eh_biz_coupon_create_time`(`create_time`)    
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET foreign_key_checks = 1;
