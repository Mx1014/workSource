ALTER TABLE eh_activities ADD COLUMN `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity category id';
ALTER TABLE eh_activities ADD COLUMN `forum_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'activity post forum that it belongs';
ALTER TABLE eh_activities ADD COLUMN `creator_tag` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'activity post creator tag';
ALTER TABLE eh_activities ADD COLUMN `target_tag` VARCHAR(128) NOT NULL DEFAULT 0 COMMENT 'activity post target tag';
ALTER TABLE eh_activities ADD COLUMN `visible_region_type` TINYINT COMMENT 'define the visible region type';
ALTER TABLE eh_activities ADD COLUMN `visible_region_id` BIGINT COMMENT 'visible region id';

DROP TABLE IF EXISTS `eh_activity_categories`;
CREATE TABLE `eh_activity_categories` (
  `id` bigint(20) NOT NULL,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parent_id` bigint(20) NOT NULL DEFAULT '0',
  `name` varchar(64) NOT NULL,
  `path` varchar(128) DEFAULT NULL,
  `default_order` int(11) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `delete_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
