DROP TABLE IF EXISTS `eh_aclinks`;
CREATE TABLE `eh_aclinks` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `device_name` VARCHAR(32) NOT NULL,
    `manufacturer` VARCHAR(32) NOT NULL,
    `firware_ver` VARCHAR(32) NOT NULL,
    `driver` TINYINT NOT NULL DEFAULT 0 COMMENT 'identify the hardware driver of aclink, not used now',
    `create_time` DATETIME,
    `status` TINYINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_door_access`;
CREATE TABLE `eh_door_access` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `door_type` TINYINT NOT NULL COMMENT '0: Zuolin aclink',
    `name` VARCHAR(128) NOT NULL,
    `description` VARCHAR(1024) NOT NULL,
    `avatar` VARCHAR(128),
    `address` VARCHAR(128),
    `active_user_id` BIGINT NOT NULL,
    `creator_user_id` BIGINT NOT NULL,
    `aclink_id` BIGINT COMMENT 'the hardware id of aclink',
    `longitude` DOUBLE,
    `latitude` DOUBLE,
    `geohash` VARCHAR(64),

    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
    `owner_id` BIGINT NOT NULL,
    `role` TINYINT NOT NULL DEFAULT 0,

    `create_time` DATETIME,
    `status` TINYINT NOT NULL COMMENT '0:activing, 1: active',

    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_door_access_aclink_id`(`aclink_id`) REFERENCES `eh_aclinks`(`id`) ON DELETE CASCADE,
    INDEX `i_eh_door_access_name`(`name`),
    INDEX `i_eh_door_access_owner`(`owner_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_aes_server_key`;
CREATE TABLE `eh_aes_server_key` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `door_id` BIGINT NOT NULL,
    `device_ver` TINYINT NOT NULL COMMENT 'ver of aclink: 0x0 or 0x1',
    `secret_ver` BIGINT NOT NULL COMMENT 'server ver of door',
    `secret` VARCHAR(64) NOT NULL COMMENT 'The base64 secret 16B',
    `create_time_ms` BIGINT,
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_aes_server_key_door_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_aes_user_key`;
CREATE TABLE `eh_aes_user_key` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `key_id` TINYINT NOT NULL,
    `key_type` TINYINT NOT NULL COMMENT '0: aclink normal key',
    `door_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
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
    `key_id` TINYINT NOT NULL,
    `door_id` BIGINT NOT NULL,
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: requesting, 2: confirm',
    `expire_time_ms` BIGINT NOT NULL,
    `create_time_ms` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_aclink_undo_key_door_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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

    `create_time` DATETIME,
    `status` TINYINT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY `fk_eh_door_auth_door_id`(`door_id`) REFERENCES `eh_door_access`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_owner_doors`;
CREATE TABLE `eh_owner_doors` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
    `owner_id` BIGINT NOT NULL,
    `door_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_owner_door_auth`;
CREATE TABLE `eh_owner_door_auth` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
    `owner_id` BIGINT NOT NULL,
    `door_id` BIGINT NOT NULL,
    `door_auth_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
