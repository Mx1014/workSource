#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_scenes`;
CREATE TABLE `eh_scenes`(
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `name` VARCHAR(64) NOT NULL COMMENT 'the identifier of the scene',
	`display_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name used to display',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`),
    UNIQUE `u_eh_ns_scene`(`namespace_id`, `name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scene` VARCHAR(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_launch_pad_items` ADD COLUMN `scene` VARCHAR(64) NOT NULL DEFAULT 'default';
ALTER TABLE `eh_banners` ADD COLUMN `scene` VARCHAR(64) NOT NULL DEFAULT 'default';
