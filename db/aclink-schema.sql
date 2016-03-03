#
# main partition of eh_door_access
#
DROP TABLE IF EXISTS `eh_door_access`;
CREATE TABLE `eh_door_access` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `uuid` VARCHAR(64) NOT NULL,
    `door_type` TINYINT NOT NULL COMMENT '0: Zuolin aclink with wifi, 1: Zuolink aclink without wifi',
    `hardware_id` VARCHAR(64) NOT NULL COMMENT 'mac address of aclink',
    `name` VARCHAR(128) NOT NULL,
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

    `acking_secret_version` INT NOT NULL DEFAULT 1,
    `expect_secret_key` INT NOT NULL DEFAULT 1,

    PRIMARY KEY (`id`),
    UNIQUE `u_eh_door_access_uuid`(`uuid`),
    INDEX `i_eh_door_access_name`(`name`),
    INDEX `i_eh_door_hardware_id`(`hardware_id`),
    INDEX `i_eh_door_access_owner`(`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Partion of eh_door_access
#
DROP TABLE IF EXISTS `eh_aclinks`;
CREATE TABLE `eh_aclinks` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `door_id` BIGINT NOT NULL,
    `device_name` VARCHAR(32) NOT NULL,
    `manufacturer` VARCHAR(32) NOT NULL,
    `firware_ver` VARCHAR(32) NOT NULL,
    `driver` TINYINT NOT NULL DEFAULT 0 COMMENT 'identify the hardware driver of aclink, not used now',
    `create_time` DATETIME,
    `status` TINYINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_aclink_door_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# partition of eh_door_access
#
DROP TABLE IF EXISTS `eh_aes_server_key`;
CREATE TABLE `eh_aes_server_key` (
    `id` BIGINT NOT NULL COMMENT 'id of the record, also as secret_ver',
    `door_id` BIGINT NOT NULL,
    `device_ver` TINYINT NOT NULL COMMENT 'ver of aclink: 0x0 or 0x1',
    `secret_ver` BIGINT NOT NULL COMMENT 'ignore it',
    `secret` VARCHAR(64) NOT NULL COMMENT 'The base64 secret 16B',
    `create_time_ms` BIGINT,
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_aes_server_key_door_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# partition of eh_door_access
#
DROP TABLE IF EXISTS `eh_aes_user_key`;
CREATE TABLE `eh_aes_user_key` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `key_id` INT NOT NULL COMMENT 'lazy load for aes_user_key',
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
    FOREIGN KEY `fk_eh_aes_user_key_door_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_aclink_undo_key`;
CREATE TABLE `eh_aclink_undo_key` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `key_id` INT NOT NULL COMMENT 'cancel a key, must notify all users for this key_id to update',
    `door_id` BIGINT NOT NULL,
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: requesting, 2: confirm',
    `expire_time_ms` BIGINT NOT NULL,
    `create_time_ms` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_aclink_undo_key_door_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Global table for relationship of owner 1<-->n door_auth
#
DROP TABLE IF EXISTS `eh_door_auth`;
CREATE TABLE `eh_door_auth` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
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
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_door_auth_door_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Global table for relationship of owner 1<-->n door
#
DROP TABLE IF EXISTS `eh_owner_doors`;
CREATE TABLE `eh_owner_doors` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
    `owner_id` BIGINT NOT NULL,
    `door_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE `i_uq_door_id_owner_id`(`door_id`, `owner_id`, `owner_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Partion of eh_door_command
# Any action will generate a command, new command maybe override old command
#
DROP TABLE IF EXISTS `eh_door_command`;
CREATE TABLE `eh_door_command` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
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
    FOREIGN KEY `fk_eh_door_command_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
