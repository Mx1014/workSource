CREATE TABLE `eh_aclink_logs` (
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
    `event_type` BIGINT DEFAULT 0,

    `door_type` TINYINT NOT NULL COMMENT '0: Zuolin aclink with wifi, 1: Zuolink aclink without wifi',
    `door_id` BIGINT NOT NULL DEFAULT 0,
    `hardware_id` VARCHAR(64) NOT NULL COMMENT 'mac address of aclink',
    `door_name` VARCHAR(128) COMMENT 'door name of aclink',

    `owner_type` TINYINT NOT NULL COMMENT '0:community, 1:enterprise, 2: family',
    `owner_id` BIGINT NOT NULL,
    `owner_name` VARCHAR(128) COMMENT 'addition name for owner',

    `user_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'user_id of user key',
    `key_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'key_id of auth',
    `auth_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'auth id of user key',
    `user_name` VARCHAR(128) COMMENT 'username of logs',
    `user_identifier` VARCHAR(128) COMMENT 'useridentifier of user',

    `string_tag1` VARCHAR(128),
    `string_tag2` VARCHAR(128),
    `string_tag3` VARCHAR(128),
    `string_tag4` VARCHAR(128),
    `string_tag5` VARCHAR(128),
    `string_tag6` VARCHAR(128),

    `integral_tag1` BIGINT DEFAULT 0,
    `integral_tag2` BIGINT DEFAULT 0,
    `integral_tag3` BIGINT DEFAULT 0,
    `integral_tag4` BIGINT DEFAULT 0,
    `integral_tag5` BIGINT DEFAULT 0,
    `integral_tag6` BIGINT DEFAULT 0,

    `remark` VARCHAR(1024) COMMENT 'extra information',

    `log_time` BIGINT DEFAULT 0,
    `create_time` DATETIME,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_version_urls` ADD COLUMN `upgrade_description` TEXT NULL DEFAULT NULL AFTER `info_url`;

-- 增加app名称和发布时间列
ALTER TABLE `eh_version_urls` ADD COLUMN `app_name` VARCHAR(50) NULL,ADD COLUMN `publish_time` DATETIME NULL;
