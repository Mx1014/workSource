# It is mainly for enterprise communities, which is a little different from communities;
# so the enterprise related tables are added below.

use ehcore;

SET foreign_key_checks = 0;

# member of eh_groups partition
# the relationship between eh_enterprises and eh_enterprise_communities
#
-- DROP TABLE IF EXISTS `eh_enterprise_addresses`;
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

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of eh_groups partition
# TODO for index field
#
-- DROP TABLE IF EXISTS `eh_enterprise_contacts`;
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
# member of eh_enterprises partition
# entry info of eh_enterprise_contacts
#
-- DROP TABLE IF EXISTS `eh_enterprise_contact_entries`;
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
-- DROP TABLE IF EXISTS `eh_enterprise_contact_groups`;
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
-- DROP TABLE IF EXISTS `eh_enterprise_contact_group_members`;
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
-- DROP TABLE IF EXISTS `eh_enterprise_community_map`;
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
-- DROP TABLE IF EXISTS `eh_buildings`;
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
-- DROP TABLE IF EXISTS `eh_building_attachments`;
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
# member of eh_groups sharding group
#
-- DROP TABLE IF EXISTS `eh_enterprise_attachments`;
CREATE TABLE `eh_enterprise_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `enterprise_id` bigint(20) NOT NULL DEFAULT '0',
  `content_type` varchar(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` varchar(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
#
-- DROP TABLE IF EXISTS `eh_organization_assigned_scopes`;
CREATE TABLE `eh_organization_assigned_scopes` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `organization_id` bigint(20) NOT NULL,
  `scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: building',
  `scope_id` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

# 
# member of eh_users partition
# Used for duplicated recording of group membership that user is involved in order to store 
# it in the same shard as of its owner user
#
-- DROP TABLE IF EXISTS `eh_user_communities`;
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
# member of global partition
#
-- DROP TABLE IF EXISTS `eh_namespace_resources`;
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



-- reuse eh_communities for eh_enterprise_communities
ALTER TABLE `eh_communities` ADD COLUMN `community_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: residential, 1: commercial';
ALTER TABLE `eh_communities` ADD COLUMN `default_forum_id` BIGINT NOT NULL DEFAULT 1 COMMENT 'the default forum for the community, forum-1 is system default forum';
ALTER TABLE `eh_communities` ADD COLUMN `feedback_forum_id` BIGINT NOT NULL DEFAULT 2 COMMENT 'the default forum for the community, forum-2 is system feedback forum';
ALTER TABLE `eh_communities` ADD COLUMN `update_time` DATETIME;
ALTER TABLE `eh_groups` ADD COLUMN `visible_region_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of region where the group belong to';
ALTER TABLE `eh_groups` ADD COLUMN `visible_region_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of region where the group belong to';
ALTER TABLE `eh_user_profiles` MODIFY COLUMN `item_name` VARCHAR(128) ;
ALTER TABLE `eh_addresses` ADD COLUMN `area_size` DOUBLE COMMENT 'the area size of the room according to the address';

-- ALTER TABLE `eh_users` DROP COLUMN `site_uri`;
-- ALTER TABLE `eh_users` DROP COLUMN `site_user_token`;
-- ALTER TABLE `eh_launch_pad_layouts` DROP COLUMN `site_uri`;
-- ALTER TABLE `eh_launch_pad_items` DROP COLUMN `site_uri`;
-- ALTER TABLE `eh_banners` DROP COLUMN `site_uri`;

-- ALTER TABLE `eh_users` ADD COLUMN `site_uri` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site uri of third-part system';
-- ALTER TABLE `eh_users` ADD COLUMN `site_user_token` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site user token of third-part system';
-- ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `site_uri` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site uri of third-part system';
-- ALTER TABLE `eh_launch_pad_items` ADD COLUMN `site_uri` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site uri of third-part system';
-- ALTER TABLE `eh_banners` ADD COLUMN `site_uri` VARCHAR(2048) NOT NULL DEFAULT '' COMMENT 'the site uri of third-part system';
ALTER TABLE `eh_users` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_users` ADD COLUMN `namespace_user_token` VARCHAR(2048) NOT NULL DEFAULT '';
ALTER TABLE `eh_user_identifiers` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_version_realm` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_version_upgrade_rules` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_version_urls` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_versioned_content` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_categories` ADD COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;

ALTER TABLE `eh_scoped_configurations` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_launch_pad_layouts` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_launch_pad_items` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
UPDATE `eh_groups` SET namespace_id=0 WHERE namespace_id IS NULL;
ALTER TABLE `eh_groups` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_forums` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_banners` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_binary_resources` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_rtxt_resources` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_events` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;
ALTER TABLE `eh_polls` MODIFY COLUMN `namespace_id` INTEGER NOT NULL DEFAULT 0;


INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 1, 'zh_CN', '用户加入企业，用户自己的消息', '您已加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 2, 'zh_CN', '发给企业其它所有成员', '${userName}已加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 3, 'zh_CN', '拒绝加入公司', '您被拒绝加入公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 4, 'zh_CN', '发给企业其它所有成员', '您已离开公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'enterprise.notification', 5, 'zh_CN', '发给企业其它所有成员', '${userName}已离开公司“${enterpriseName}”。');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES( 'park.notification', 1, 'zh_CN', '有新的月卡发放通知排队用户申请月卡成功', '月卡申报成功，请于“${deadline}” 18:00前去领取，否则自动失效。');


INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4012, 4, 0, '科技', '活动/科技', 0, 2, UTC_TIMESTAMP());	
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4013, 4, 0, '论坛', '活动/论坛', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4014, 4, 0, '创客', '活动/创客', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4015, 4, 0, '项目', '活动/项目', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4016, 4, 0, '联谊', '活动/联谊', 0, 2, UTC_TIMESTAMP());
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`)
    VALUES(4017, 4, 0, '沙龙', '活动/沙龙', 0, 2, UTC_TIMESTAMP());     

INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'enterprise', '10001', 'zh_CN', '公司不存在');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'parking', '10001', 'zh_CN', '车牌号位数错误');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'parking', '10002', 'zh_CN', '该车牌已有月卡');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'parking', '10003', 'zh_CN', '该车牌已申请月卡');

SET foreign_key_checks = 1;