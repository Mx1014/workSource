#
# A speical note about the schema design below (KY)
#
# To balance performance and flexibility, some tables carry general purpose integer fields and string fields,
# interpretation of these fields will be determined by the application on top of, at database level, we only
# provide general indexing support for these fields, it is the responsibility of the application to map queries that
# are against to these fields.
#
# Initially, only two of string-type general purpose fields are indexed, more indices can be added during operating
# time, tuning changes about the indexing will be sync-ed back into schema design afterwards
#
#

SET foreign_key_checks = 0;

use ehcore;

#
# member of global parition
#
DROP TABLE IF EXISTS `eh_categories`;
CREATE TABLE `eh_categories`(
    `id` INTEGER NOT NULL,
    `parent_id` INTEGER,
    `name` VARCHAR(128) NOT NULL,
    `path` VARCHAR(1024),
    `default_order` INTEGER,
    `status` INTEGER NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_category_name`(`parent_id`, `name`),
    INDEX `i_eh_category_path`(`path`),
    INDEX `i_eh_category_order`(`default_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_state_triggers`;
CREATE TABLE `eh_state_triggers` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `object_type` VARCHAR(32),
    `object_id` BIGINT,
    `trigger_state` INTEGER,
    `flow_type` INTEGER,
    `flow_data` TEXT,
    `order` INTEGER,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_search_keywords`;
CREATE TABLE `eh_search_keywords`(
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `scope` VARCHAR(32),
    `scope_id` BIGINT,
    `keyword` VARCHAR(128),
    `weight` INTEGER,
    `frequency` INTEGER,
    `version` INTEGER,
    `update_time` DATETIME,
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_kword_scoped_kword`(`scope`, `scope_id`, `keyword`),
    INDEX `i_kword_weight_frequency`(`weight`, `frequency`),
    INDEX `i_kword_update_time`(`update_time`),
    INDEX `i_kword_create_time`(`create_time`),
    INDEX `i_kword_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
# for compatibility reason, this table is basically cloned from old DB
#
DROP TABLE IF EXISTS `eh_app_promotions`;
CREATE TABLE `eh_app_promotions` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(128),
    `channel` INTEGER COMMENT '1: offical site, 2: app store, 3: manual',
    `version` VARCHAR(256),
    `path` VARCHAR(1024),
    `file_name` VARCHAR(128),
    `link` VARCHAR(1024),
    `download_count` INTEGER NOT NULL DEFAULT 0,
    `register_count` INTEGER NOT NULL DEFAULT 0,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_app_promo_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
# for compatibility reason, this table is basically cloned from old DB
#
DROP TABLE IF EXISTS `eh_stats_by_city`;
CREATE TABLE `eh_stats_by_city` (
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `city_id` INTEGER,
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
    
    PRIMARY KEY (`id`),
    UNIQUE `u_stats_city_report`(`city_id`, `stats_date`, `stats_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_templates`;
CREATE TABLE `eh_templates`(
    `id` INTEGER NOT NULL AUTO_INCREMENT,
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
    `id` INTEGER NOT NULL AUTO_INCREMENT,
    `uid` BIGINT,
    `business_id` BIGINT,
    `subject` VARCHAR(256),
    `content` TEXT,
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_feedback_create_time`(`create_time`),
    INDEX `i_eh_feedback_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of user-related partition group
#
DROP TABLE IF EXISTS `eh_users`;
CREATE TABLE `eh_users` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `account_name` VARCHAR(64) NOT NULL,
    `nick_name` VARCHAR(32),
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0 - inactive, 1 - active',
    `create_time` DATETIME NOT NULL,
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
    `create_time` DATETIME NOT NULL,
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
#
# Used for duplicated recording of group membership that user is involved in
# stored in the same shard as of its owner user
#
DROP TABLE IF EXISTS `eh_user_groups`;
CREATE TABLE `eh_user_groups` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `group_discriminator` VARCHAR(32) COMMENT 'redendant info for quickly distinguishing associated group', 
    `group_id` BIGINT,
    `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'default to ResourceUser role', 
    `member_status` INTEGER NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
    `create_time` DATETIME NOT NULL,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_grp_owner_group`(`owner_uid`, `group_id`),
    INDEX `i_eh_usr_grp_owner`(`owner_uid`),
    INDEX `i_eh_usr_grp_create_time`(`create_time`)
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
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_usr_blk_owner_target`(`owner_uid`, `target_type`, `target_id`),
    INDEX `i_usr_blk_owner`(`owner_uid`),
    INDEX `i_usr_blk_create_time`(`create_time`)
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
    `create_time` DATETIME,
    `delete_time` DATETIME,

    PRIMARY KEY (`id`),
    UNIQUE `u_usr_favorite_target`(`owner_uid`, `target_type`, `target_id`),
    INDEX `i_usr_favorite_owner`(`owner_uid`),
    INDEX `i_usr_favorite_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_users partition group
#
DROP TABLE IF EXISTS `eh_user_profiles`;
CREATE TABLE `eh_user_profiles`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner user id',
    `item_name` VARCHAR(32),
    `item_group` VARCHAR(32) COMMENT 'tag the profile item group that item belongs to',
    `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque value, 1: entity',
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    `target_value` TEXT,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_user_prof_item`(`owner_id`, `item_name`, `item_group`, `item_kind`),
    INDEX `i_user_prof_owner`(`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of grouping related partition group
#
DROP TABLE IF EXISTS `eh_groups`;
CREATE TABLE `eh_groups` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `name` VARCHAR(128) NOT NULL,
    `avatar` VARCHAR(256),
    `description` TEXT,
    `creator_uid` BIGINT NOT NULL,
    `member_count` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL,
    `delete_time` DATETIME,
    `private_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
    `join_policy` INTEGER NOT NULL DEFAULT 0 COMMENT '0: free join(public group), 1: should be approved by operator/owner',
    `city_id` BIGINT,
    `discriminator` VARCHAR(32),
    
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
    UNIQUE `u_eh_group_name`(`name`, `city_id`, `discriminator`),
    INDEX `i_eh_group_creator`(`creator_uid`),
    INDEX `i_eh_group_create_time` (`create_time`),
    INDEX `i_eh_group_delete_time` (`delete_time`),
    INDEX `i_eh_group_itag1`(`integral_tag1`),
    INDEX `i_eh_group_itag2`(`integral_tag2`),
    INDEX `i_eh_group_ctag1`(`comment_tag1`),
    INDEX `i_eh_group_ctag2`(`comment_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_groups partition group
#
DROP TABLE IF EXISTS `eh_group_members`;
CREATE TABLE `eh_group_members` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `group_id` BIGINT NOT NULL,
    `member_type` VARCHAR(32) NOT NULL COMMENT 'member object type, for example, type could be User, Group, etc',
    `member_id` BIGINT,
    `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'Default to ResourceUser role',    
    `member_status` INTEGER NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
    `create_time` DATETIME NOT NULL,
    `approve_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_grp_member` (`group_id`, `member_type`, `member_id`),
    INDEX `i_eh_grp_member_group_id` (`group_id`),
    INDEX `i_eh_grp_member_member` (`member_type`, `member_id`),
    INDEX `i_eh_grp_member_create_time` (`create_time`),
    INDEX `i_eh_grp_member_approve_time` (`approve_time`)
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
DROP TABLE IF EXISTS `eh_countries`;
CREATE TABLE `eh_countries` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `name` VARCHAR(64),
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: created, 2: active, 3: locked, 4: deleted',
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_country_name`(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_provinces`;
CREATE TABLE `eh_provinces` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `country_id` INTEGER NOT NULL,
    `name` VARCHAR(64),
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: created, 2: active, 3: locked, 4: deleted',
    
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_province_country_id`(`country_id`) REFERENCES `eh_countries`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_province_name`(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_cities`;
CREATE TABLE `eh_cities` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `country_id` INTEGER NOT NULL,
    `province_id` INTEGER,
    `name` VARCHAR(64),
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: created, 2: active, 3: locked, 4: deleted',
    
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_city_country_id`(`country_id`) REFERENCES `eh_countries`(`id`) ON DELETE CASCADE,
    FOREIGN KEY `fk_city_province_id`(`province_id`) REFERENCES `eh_provinces`(`id`) ON DELETE CASCADE,
    
    INDEX `i_eh_city_name`(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of global partition
#
DROP TABLE IF EXISTS `eh_areas`;
CREATE TABLE `eh_areas` (
    `id` INTEGER NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `city_id` INTEGER NOT NULL,
    `name` VARCHAR(64),
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '1: created, 2: active, 3: locked, 4: deleted',
    
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_area_city_id`(`city_id`) REFERENCES `eh_cities`(`id`) ON DELETE CASCADE,
    
    INDEX `i_eh_area_name`(`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of the partition group
#
DROP TABLE IF EXISTS `eh_communities`;
CREATE TABLE `eh_communities`(
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
    `area_id` INTEGER NOT NULL,
    `area_name` VARCHAR(64) COMMENT 'redundant for query optimization',
    `name` VARCHAR(64),
    `alias_name` VARCHAR(64),
    `address` VARCHAR(512),
    `zipcode` VARCHAR(64),
    `description` TEXT,
    `detail_description` TEXT,
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
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
    INDEX `i_eh_community_area_name`(`area_name`),
    INDEX `i_eh_community_name`(`name`),
    INDEX `i_eh_community_alias_name`(`alias_name`),
    INDEX `i_eh_community_address`(`address`),
    INDEX `i_eh_community_zipcode`(`zipcode`),
    INDEX `i_eh_community_create_time`(`create_time`),
    INDEX `i_eh_community_delete_time`(`delete_time`),
    INDEX `i_eh_community_itag1`(`integral_tag1`),
    INDEX `i_eh_community_itag2`(`integral_tag2`),
    INDEX `i_eh_community_ctag1`(`comment_tag1`),
    INDEX `i_eh_community_ctag2`(`comment_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_communities partition
# information of community forum, admin group will be managed in community profile 
#
DROP TABLE IF EXISTS `eh_community_profiles`;
CREATE TABLE `eh_community_profiles` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner community id',
    `item_name` VARCHAR(32),
    `item_group` VARCHAR(32) COMMENT 'tag the profile item group that item belongs to',
    `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque value, 1: entity',
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    `target_value` TEXT,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_community_prof_item`(`owner_id`, `item_name`, `item_group`, `item_kind`),
    INDEX `i_community_prof_owner`(`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Key table in address related partition group
#
DROP TABLE IF EXISTS `eh_addresses`;
CREATE TABLE `eh_addresses` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `community_id` BIGINT,
    `address` VARCHAR(1024),
    `address_alias` VARCHAR(1024),
    `building_name` VARCHAR(128),
    `building_alias_name` VARCHAR(128),
    `appartment_name` VARCHAR(128),
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
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
    INDEX `i_eh_address_address`(`address`),
    INDEX `i_eh_address_address_alias`(`address_alias`),
    INDEX `i_eh_address_building_apt_name`(`building_name`, `appartment_name`),
    INDEX `i_eh_address_building_alias_apt_name`(`building_alias_name`, `appartment_name`),
    INDEX `i_eh_address_create_name`(`create_time`),
    INDEX `i_eh_address_delete_name`(`delete_time`),
    INDEX `i_eh_address_itag1`(`integral_tag1`),
    INDEX `i_eh_address_itag2`(`integral_tag2`),
    INDEX `i_eh_address_ctag1`(`comment_tag1`),
    INDEX `i_eh_address_ctag2`(`comment_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_address partition group
#
DROP TABLE IF EXISTS `eh_address_claims`;
CREATE TABLE `eh_address_claims` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `address_id` BIGINT NOT NULL,
    `entity_type` VARCHAR(32) NOT NULL,
    `entity_id` BIGINT NOT NULL,
    `initiator_uid` BIGINT NOT NULL,
    `claim_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: unclaimed, 1: claiming, 2: claimed',
    `operator_uid` BIGINT,
    `process_code` TINYINT,
    `process_details` TEXT,
    `proof_resource_id` BIGINT,
    `proof_resource_url` VARCHAR(512),
    `create_time` DATETIME,
    `process_time` DATETIME,
    `delete_time` DATETIME,
    
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
    FOREIGN KEY `fk_eh_addr_claim_address_id`(`address_id`) REFERENCES `eh_addresses`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_addr_claim_target_entity`(`entity_type`, `entity_id`),
    INDEX `i_eh_addr_claim_initiator_uid`(`initiator_uid`),
    INDEX `i_eh_addr_claim_operator_uid`(`operator_uid`),
    INDEX `i_eh_addr_claim_create_time`(`create_time`),
    INDEX `i_eh_addr_claim_process_time`(`process_time`),
    INDEX `i_eh_addr_claim_itag1`(`integral_tag1`),
    INDEX `i_eh_addr_claim_itag2`(`integral_tag2`),
    INDEX `i_eh_addr_claim_ctag1`(`comment_tag1`),
    INDEX `i_eh_addr_claim_ctag2`(`comment_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Member of eh_groups partition group, inherited from eh_groups
# pseudo table, its records can be distinguished by descriminator field in eh_groups table  
#
DROP TABLE IF EXISTS `eh_families`;

#
# member of eh_groups(eh_families) partition group
#
DROP TABLE IF EXISTS `eh_family_followers`;
CREATE TABLE eh_family_followers (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_family` BIGINT NOT NULL,
    `follower_family` BIGINT NOT NULL,
    `alias_name` VARCHAR(64),
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `i_fm_follower_follower`(`owner_family`, `follower_family`),
    INDEX `i_fm_follower_owner`(`owner_family`),
    INDEX `i_fm_follower_create_time`(`create_time`),
    INDEX `i_fm_follower_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_groups(eh_families) partition group
#
DROP TABLE IF EXISTS `eh_followed_families`;
CREATE TABLE `eh_followed_families`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_family` BIGINT NOT NULL,
    `followed_family` BIGINT NOT NULL,
    `alias_name` VARCHAR(64),
    
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `i_fm_followed_followed`(`owner_family`, `followed_family`),
    INDEX `i_fm_followed_owner`(`owner_family`),
    INDEX `i_fm_followed_create_time`(`create_time`),
    INDEX `i_fm_followed_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of the partition group
#
DROP TABLE IF EXISTS `eh_banners`;
CREATE TABLE `eh_banners` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
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
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_banner_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_banners partition
# banner distribution scope will be managed through banner profile in item group named as 'scope'
#
DROP TABLE IF EXISTS `eh_banner_profiles`;
CREATE TABLE `eh_banner_profiles` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner banner id',
    `item_name` VARCHAR(32),
    `item_group` VARCHAR(32) COMMENT 'tag the profile item group that item belongs to',
    `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque value, 1: entity',
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    `target_value` TEXT,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_banner_prof_item`(`owner_id`, `item_name`, `item_group`, `item_kind`),
    INDEX `i_banner_prof_owner`(`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_banners partition
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
    UNIQUE `u_banner_clk_uuid`(`uuid`),
    UNIQUE `u_banner_clk_user`(`banner_id`, `uid`),
    INDEX `i_banner_clk_last_time`(`last_click_time`),
    INDEX `i_banner_clk_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_banners partition
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
    INDEX `i_bin_res_checksum`(`checksum`),
    INDEX `i_bin_res_create_time`(`create_time`),
    INDEX `i_bin_res_access_time`(`access_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Resource management
# key table of rich text resource management partition group
#
DROP TABLE IF EXISTS `eh_rtxt_resources`;
CREATE TABLE `eh_rtxt_resources`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
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
    INDEX `i_rtxt_res_checksum`(`checksum`),
    INDEX `i_rtxt_res_create_time`(`create_time`),
    INDEX `i_rtxt_res_access_time`(`access_time`)
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
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_event_start_time_ms`(`start_time_ms`),
    INDEX `i_event_end_time_ms`(`end_time_ms`),
    INDEX `i_event_creator_uid`(`creator_uid`),
    INDEX `i_activity_create_time`(`create_time`),
    INDEX `i_activity_delete_time`(`delete_time`)
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
    `create_time` DATETIME,

    PRIMARY KEY (`id`),
    UNIQUE `u_event_roster_uuid`(`uuid`),
    UNIQUE `u_event_roster_attendee`(`event_id`, `uid`),
    INDEX `i_event_roster_signup_time`(`signup_time`),
    INDEX `i_event_roster_create_time`(`create_time`)
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
    `create_time` DATETIME,

    PRIMARY KEY (`id`),
    UNIQUE `u_event_tg_name`(`event_id`, `name`),
    INDEX `i_event_tg_event_id`(`event_id`),
    INDEX `i_event_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of event partition group
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
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_event_ticket_ticket`(`ticket_group_id`, `ticket_number`),
    INDEX `i_event_ticket_event`(`event_id`),
    INDEX `i_event_ticket_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_events partition group
#
DROP TABLE IF EXISTS `eh_event_profiles`;
CREATE TABLE `eh_event_profiles`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner event id',
    `item_name` VARCHAR(32),
    `item_group` VARCHAR(32) COMMENT 'tag the profile item group that item belongs to',
    `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque value, 1: entity',
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    `target_value` TEXT,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_event_prof_item`(`owner_id`, `item_name`, `item_group`, `item_kind`),
    INDEX `i_event_prof_owner`(`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of activity partition group
#
DROP TABLE IF EXISTS `eh_activities`;
CREATE TABLE `eh_activities` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
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
    `status` INTEGER COMMENT '0: inactive, 1: drafting, 2: active',
    `change_version` INTEGER NOT NULL DEFAULT 1,
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_activity_start_time_ms`(`start_time_ms`),
    INDEX `i_activity_end_time_ms`(`end_time_ms`),
    INDEX `i_activity_creator_uid`(`creator_uid`),
    INDEX `i_activity_post_id`(`post_id`),
    INDEX `i_activity_group`(`group_discriminator`, `group_id`),
    INDEX `i_activity_create_time`(`create_time`),
    INDEX `i_activity_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_activities partition group
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
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_activity_uuid`(`uuid`),
    UNIQUE `u_activity_user`(`activity_id`, `uid`),
    INDEX `i_activity_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of polling management partition group
#
DROP TABLE IF EXISTS `eh_polls`;
CREATE TABLE `eh_polls` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
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
    `status` INTEGER COMMENT '0: inactive, 1: drafting, 2: active',
    `change_version` INTEGER NOT NULL DEFAULT 1,
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_poll_start_time_ms`(`start_time_ms`),
    INDEX `i_poll_end_time_ms`(`end_time_ms`),
    INDEX `i_poll_creator_uid`(`creator_uid`),
    INDEX `i_poll_post_id`(`post_id`),
    INDEX `i_poll_create_time`(`create_time`),
    INDEX `i_poll_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_polls partition
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
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_poll_item_poll`(`poll_id`),
    INDEX `i_poll_item_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_polls partition
#
DROP TABLE IF EXISTS `eh_poll_votes`;
CREATE TABLE `eh_poll_votes`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `poll_id` BIGINT,
    `item_id` BIGINT,
    `voter_uid` BIGINT,
    `voter_family_id` BIGINT,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `i_poll_vote_vote`(`poll_id`, `item_id`, `voter_uid`),
    INDEX `i_poll_vote_poll`(`poll_id`),
    INDEX `i_poll_vote_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# key table of business management partition group
#
DROP TABLE IF EXISTS `eh_business`;
CREATE TABLE `eh_business`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `name` VARCHAR(128),
    `contact_number` VARCHAR(64), 
    `category_id` BIGINT,
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `geohash` VARCHAR(64),
    `change_version` INTEGER,
    `create_time` DATETIME,
    `delete_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_biz_name`(`name`),
    INDEX `i_eh_biz_geohash`(`geohash`),
    INDEX `i_eh_biz_create_time`(`create_time`),
    INDEX `i_eh_biz_delete_time`(`delete_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_business partition group
#
DROP TABLE IF EXISTS `eh_business_profiles`;
CREATE TABLE `eh_business_profiles`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_id` BIGINT NOT NULL COMMENT 'owner user id',
    `item_name` VARCHAR(32),
    `item_group` VARCHAR(32) COMMENT 'tag the profile item group that item belongs to',
    `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque value, 1: entity',
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    `target_value` TEXT,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_biz_prof_item`(`owner_id`, `item_name`, `item_group`, `item_kind`),
    INDEX `i_biz_prof_owner`(`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_business partition group
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
    `status` INTEGER,
    `start_time` DATETIME,
    `expire_time` DATETIME,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_biz_coupon_business_id`(`business_id`),
    INDEX `i_eh_biz_name`(`name`),
    INDEX `i_eh_biz_start_time`(`start_time`),
    INDEX `i_eh_biz_expire_time`(`expire_time`),
    INDEX `i_eh_biz_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# member of eh_business partition group
#
DROP TABLE IF EXISTS `eh_biz_coupon`;
CREATE TABLE `eh_biz_coupon`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `business_id` BIGINT,
    `coupon_group_id` BIGINT,
    `coupon_number` VARCHAR(128),
    `uid` BIGINT,
    `family_id` BIGINT,
    `status` INTEGER,
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_biz_coupon_business_id`(`business_id`),    
    INDEX `i_eh_biz_coupon_number`(`coupon_group_id`, `coupon_number`),    
    INDEX `i_eh_biz_coupon_create_time`(`create_time`)    
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET foreign_key_checks = 1;
