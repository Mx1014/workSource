#
# user items
#
DROP TABLE IF EXISTS `eh_user_launch_pad_items`;

CREATE TABLE `eh_user_launch_pad_items`(
    `id` BIGINT NOT NULL,
    `item_id` BIGINT NOT NULL,
    `owner_type` varchar(64) NOT NULL COMMENT 'community, organization',
    `owner_id` tinyint(4) NOT NULL,
    `user_id` tinyint(4) NOT NULL,
    `apply_policy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: default, 1: override, 2: revert 3:customized',
    `display_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'default display on the pad, 0: hide, 1:display',
    `default_order` int(11) NOT NULL DEFAULT '0',
    `scene_type` varchar(64) NOT NULL DEFAULT 'default',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;