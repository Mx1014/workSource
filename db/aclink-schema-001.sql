#
# Global table eh_aclink_firmware
#
DROP TABLE IF EXISTS `eh_aclink_firmware`;
CREATE TABLE `eh_aclink_firmware` (
    `id` BIGINT NOT NULL COMMENT 'id of the record',
    `firmware_type` TINYINT NOT NULL COMMENT 'firmware type',
    `major` TINYINT NOT NULL,
    `minor` TINYINT NOT NULL,
    `revision` TINYINT NOT NULL,
    `checksum` BIGINT NOT NULL,
    `md5sum` VARCHAR(64),
    `download_url` VARCHAR(128),
    `info_url` VARCHAR(128),
    `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
    `create_time` DATETIME,
    `creator_id` BIGINT,
    `owner_id` BIGINT,
    `owner_type` TINYINT,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
