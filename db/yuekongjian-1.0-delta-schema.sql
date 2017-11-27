--
CREATE TABLE `eh_me_web_menus` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `action_path` varchar(255) NOT NULL,
  `action_data` varchar(255) DEFAULT NULL,
  `icon_uri` varchar(255) DEFAULT NULL,
  `position_flag` tinyint(4) DEFAULT '1' COMMENT 'position, 1-NORMAL, 2-bottom',
  `sort_num` int(11) DEFAULT '0',
  `status` tinyint(4) DEFAULT '2' COMMENT '0: inactive, 2: active',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- layout 增加广场的背景图片 add by yanjun 201711271158
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `bg_image_uri`  varchar(255) NULL;

-- added by Janson
ALTER TABLE `eh_door_access` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_access` ADD COLUMN `display_name` VARCHAR(128) NULL DEFAULT NULL AFTER `name`;
ALTER TABLE `eh_door_auth` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
-- ALTER TABLE `eh_door_auth_level` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_auth_logs` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_auth_command` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_door_auth_user_permission` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclink_firmware` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclink_logs` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclinks` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;
ALTER TABLE `eh_aclink_undo_key` ADD COLUMN `namespace_id` int(11) NOT NULL DEFAULT 0 AFTER `id`;


