SET foreign_key_checks = 0;

use ehcore;

DROP TABLE IF EXISTS `eh_users`;
CREATE TABLE `eh_users` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `name` VARCHAR(128) NOT NULL,
    `description` VARCHAR(192) DEFAULT '',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0 - inactive, 1 - active',
    `create_time` DATETIME NOT NULL,
    `password_hash` VARCHAR(128) DEFAULT '' COMMENT 'Note, password is stored as salted hash, salt is appended by hash together',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_user_devices`;
CREATE TABLE `eh_user_devices` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_uid` BIGINT NOT NULL COMMENT 'owner user id',
    `device_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: mobile, 1: email',
    `device_number` VARCHAR(128),
    `device_manufacture_id` VARCHAR(64),
    `verification_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not verified, 1: waiting for verification, 2: verified',
    `verification_code` VARCHAR(16),
    `create_time` DATETIME NOT NULL,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_user_device_type_number`(`device_type`, `device_number`),
    INDEX `i_eh_user_device_manufacture_id`(`device_manufacture_id`),
    INDEX `i_eh_user_device_create_time`(`create_time`),
    INDEX `i_eh_user_device_last_user_time`(`last_use_time`),
    INDEX `i_eh_user_device_last_notify_time`(`last_use_time`)
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
    `member_role_flag` INTEGER NOT NULL DEFAULT 0, 
    `create_time` DATETIME NOT NULL,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_usr_grp_owner_group`(`owner_uid`, `group_id`),
    INDEX `i_usr_grp_owner`(`owner_uid`),
    INDEX `i_usr_grp_create_time`(`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_groups`;
CREATE TABLE `eh_groups` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `name` VARCHAR(128) NOT NULL,
    `creator_uid` BIGINT NOT NULL,
    `member_count` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME NOT NULL,
    `private_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: public, 1: private',
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_group_name` (`name`),
    INDEX `i_eh_group_creator` (`creator_uid`),
    INDEX `i_eh_group_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `eh_group_members`;
CREATE TABLE `eh_group_members` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `group_id` BIGINT NOT NULL,
    `member_type` VARCHAR(32),
    `member_id` BIGINT ,
    `member_role_flag` INTEGER NOT NULL DEFAULT 0, 
    `create_time` DATETIME NOT NULL,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_grp_member` (`group_id`, `member_type`, `member_id`),
    INDEX `i_eh_grp_member_group_id` (`group_id`),
    INDEX `i_eh_grp_member_member` (`member_type`, `member_id`),
    INDEX `i_eh_grp_member_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET foreign_key_checks = 1;
