# It is mainly for enterprise communities, which is a little different from communities;
# so the enterprise related tables are added below.

USE ehcore;

SET foreign_key_checks = 0;


# member of eh_groups partition
# the relationship between eh_enterprises and eh_enterprise_communities
#
DROP TABLE IF EXISTS `eh_enterprise_addresses`;
CREATE TABLE `eh_enterprise_addresses` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `enterprise_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'reference to id of eh_groups',
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

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups partition
# TODO for index field
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
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: waiting_auth, 2: authenticated',
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
# member of eh_enterprises partition
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
# member of eh_enterprises partition
# internal group in enterprise
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
# member of eh_enterprises partition
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

# reuse eh_communities for eh_enterprise_communities
ALTER TABLE `eh_communities` ADD COLUMN `community_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: residential, 1: commercial';
ALTER TABLE `eh_communities` ADD COLUMN `default_forum_id` BIGINT NOT NULL DEFAULT 1 COMMENT 'the default forum for the community, forum-1 is system default forum';
ALTER TABLE `eh_communities` ADD COLUMN `feedback_forum_id` BIGINT NOT NULL DEFAULT 2 COMMENT 'the default forum for the community, forum-2 is system feedback forum';
ALTER TABLE `eh_communities` ADD COLUMN `update_time` DATETIME;
ALTER TABLE `eh_groups` ADD COLUMN `visible_region_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of region where the group belong to';
ALTER TABLE `eh_groups` ADD COLUMN `visible_region_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of region where the group belong to';

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
    `creator_uid` BIGINT NOT NULL,
    `create_time` DATETIME NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `eh_organization_assigned_scopes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: building',
  `scope_id` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4;

# TODO move to somewhare?
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'enterprise', '10001', 'zh_CN', '公司不存在');

INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 1, 'zh_CN', '用户加入企业，用户自己的消息', '您已加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 2, 'zh_CN', '发给企业其它所有成员', '${userName}已加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 3, 'zh_CN', '拒绝加入公司', '$您被拒绝加入公司“${enterpriseName}”。');

SET foreign_key_checks = 1;
