#
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
DROP TABLE IF EXISTS `eh_enterprise_community_members`;
CREATE TABLE `eh_enterprise_community_members` (
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
ALTER TABLE `ehcore`.`eh_communities` ADD COLUMN `community_type` TINYINT NOT NULL DEFAULT 0;
