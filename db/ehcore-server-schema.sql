SET foreign_key_checks = 0;

use ehcore;

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
# Used for duplicated recording of group membership that user is involved in
# stored in the same shard as of its owner user
#
DROP TABLE IF EXISTS `eh_user_groups`;
CREATE TABLE `eh_user_groups` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `group_id` BIGINT,
    `member_role` BIGINT NOT NULL DEFAULT 7 COMMENT 'default to ResourceUser role', 
    `member_status` INTEGER NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
    `create_time` DATETIME NOT NULL,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_usr_grp_owner_group`(`owner_uid`, `group_id`),
    INDEX `i_eh_usr_grp_owner`(`owner_uid`),
    INDEX `i_eh_usr_grp_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_groups`;
CREATE TABLE `eh_groups` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `name` VARCHAR(128) NOT NULL,
    `creator_uid` BIGINT NOT NULL,
    `member_count` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL,
    `private_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
    `join_policy` INTEGER NOT NULL DEFAULT 0 COMMENT '0: free join(public group), 1: should be approved by operator/owner',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_group_name` (`name`),
    INDEX `i_eh_group_creator` (`creator_uid`),
    INDEX `i_eh_group_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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

SET foreign_key_checks = 1;
