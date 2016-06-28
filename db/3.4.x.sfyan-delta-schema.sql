-- add by xiongying
ALTER TABLE `eh_activities` ADD COLUMN media_url VARCHAR(1024);
ALTER TABLE `eh_user_posts` ADD COLUMN target_type VARCHAR(32);
ALTER TABLE `eh_user_posts` CHANGE post_id target_id BIGINT NOT NULL DEFAULT 0;

CREATE TABLE `eh_hot_tags` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `service_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'service type, eg: activity',  
  `name` VARCHAR(128) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: active',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `create_uid` BIGINT,
  `delete_time` DATETIME,
  `delete_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- modified 20160622
CREATE TABLE `eh_app_urls` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `name` VARCHAR(128) NOT NULL,
  `os_type` TINYINT NOT NULL DEFAULT 0,
  `download_url` VARCHAR(128),
  `logo_url` VARCHAR(128),
  `description` TEXT,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- add by yanshaofan
-- user items
-- 
CREATE TABLE `eh_user_launch_pad_items`(
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `item_id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community, organization',
  `owner_id` BIGINT NOT NULL,
  `user_id` BIGINT NOT NULL,
  `apply_policy` TINYINT NOT NULL DEFAULT 0 COMMENT '0: default, 1: override, 2: revert 3:customized',
  `display_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'default display on the pad, 0: hide, 1:display',
  `default_order` INTEGER NOT NULL DEFAULT 0,
  `scene_type` VARCHAR(64) NOT NULL DEFAULT 'default',
  `update_time` DATETIME,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 20160627
ALTER TABLE `eh_banners` ADD COLUMN `apply_policy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: default, 1: override, 2: revert 3:customized';
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scope_code` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: all, 1: community, 2: city, 3: user';
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `scope_id` bigint(20) DEFAULT 0;
ALTER TABLE `eh_launch_pad_layouts` ADD COLUMN `apply_policy` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: default, 1: override, 2: revert 3:customized';

