#
# member of global partition
#
DROP TABLE IF EXISTS `eh_namespace_profiles`;
CREATE TABLE `eh_namespace_profiles`(
    `id` BIGINT NOT NULL COMMENT 'id of the record',
	`app_id` BIGINT,    
    `owner_id` BIGINT NOT NULL COMMENT 'owner user id',
    `item_name` VARCHAR(128),
    `item_kind` TINYINT NOT NULL DEFAULT 0 COMMENT '0, opaque json object, 1: entity',
    `item_value` TEXT,
    `target_type` VARCHAR(32),
    `target_id` BIGINT,
    
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
    
    PRIMARY KEY (`id`),
    INDEX `i_eh_uprof_item`(`app_id`, `owner_id`, `item_name`),
    INDEX `i_eh_uprof_owner`(`owner_id`),
    INDEX `i_eh_uprof_itag1`(`integral_tag1`),
    INDEX `i_eh_uprof_itag2`(`integral_tag2`),
    INDEX `i_eh_uprof_stag1`(`string_tag1`),
    INDEX `i_eh_uprof_stag2`(`string_tag2`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
