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

# use ehcore;

#
# member of eh_enterprises partition
#
DROP TABLE IF EXISTS `eh_enterprise_contacts`;
CREATE TABLE `eh_enterprise_contacts` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
    `name` VARCHAR(256) COMMENT 'real name',
    `nick_name` VARCHAR(256) COMMENT 'display name',
    `avatar` VARCHAR(128) COMMENT 'avatar uri',
    `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'user id reference to eh_users, it determine the contact authenticated or not',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
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
	
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_contact_type_token`(`contact_id`, `identifier_type`, `identifier_token`),
    INDEX `i_eh_user_idf_owner`(`contact_id`),
    INDEX `i_eh_user_idf_type_token`(`identifier_type`, `identifier_token`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_enterprises partition
# supplementry info of eh_enterprise_contacts
#
DROP TABLE IF EXISTS `eh_enterprise_contact_entries`;
CREATE TABLE `eh_enterprise_contact_entries` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
    `contact_id` BIGINT NOT NULL COMMENT 'contact id',
    `entry_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
    `entry_value` VARCHAR(128),
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: free standing, 1: claiming, 2: claim verifying, 3: claimed',
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_contact_type_token`(`contact_id`, `identifier_type`, `identifier_token`),
    INDEX `i_eh_user_idf_owner`(`contact_id`),
    INDEX `i_eh_user_idf_type_token`(`identifier_type`, `identifier_token`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_enterprises partition
#
DROP TABLE IF EXISTS `eh_enterprise_contact_groups`;
CREATE TABLE `eh_enterprise_contact_groups` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
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
    
    PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_enterprises partition
#
DROP TABLE IF EXISTS `eh_enterprise_contact_group_members`;
CREATE TABLE `eh_enterprise_contact_group_members` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'enterprise id',
    `contact_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_contact_groups',
    `contact_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprise_contacts',
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
    
    PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_communties` ADD COLUMN `community_type` TINYINT DEFAULT 0 COMMENT '0: community, 1: enterprise community';

#
# member of eh_enterprises partition
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
    
    PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_enterprises partition
# the relationship between eh_enterprises and eh_enterprise_communities
#
DROP TABLE IF EXISTS `eh_enterprise_addresses`;
CREATE TABLE `eh_enterprise_addresses` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_enterprises',
    `address_id` VARCHAR(32) NOT NULL COMMENT 'reference to id of eh_addresses',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
    `creator_uid` BIGINT COMMENT 'record creator user id',
    `create_time` DATETIME,
    `operator_uid` BIGINT COMMENT 'redundant auditing info',
    `process_code` TINYINT,
    `process_details` TEXT,
    `proof_resource_uri` VARCHAR(1024),
    `approve_time` DATETIME COMMENT 'redundant auditing info',
    `update_time` DATETIME NOT NULL,
    
    PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET foreign_key_checks = 1;
