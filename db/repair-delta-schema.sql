
DROP TABLE IF EXISTS `eh_pm_tasks`;
CREATE TABLE `eh_pm_tasks` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`category_id` BIGINT NOT NULL DEFAULT 0,

	`address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'detail address',
	`content` TEXT COMMENT 'content data',
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
	`star` TINYINT COMMENT 'evaluate score',

	`unprocessed_time` DATETIME,
	`processing_time` DATETIME,
	`processed_time` DATETIME,
	`closed_time` DATETIME,

	`creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0,
  `delete_time` DATETIME,
	
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_pm_task_statistics`;
CREATE TABLE `eh_pm_task_statistics` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `category_id` BIGINT NOT NULL DEFAULT 0,
  `total_count` INT NOT NULL DEFAULT 0,
  `unprocess_count` INT NOT NULL DEFAULT 0,
  `processing_count` INT NOT NULL DEFAULT 0,
  `processed_count` INT NOT NULL DEFAULT 0,
  `close_count` INT NOT NULL DEFAULT 0,

  `star1` INT NOT NULL DEFAULT 0,
  `star2` INT NOT NULL DEFAULT 0,
  `star3` INT NOT NULL DEFAULT 0,
  `star4` INT NOT NULL DEFAULT 0,
  `star5` INT NOT NULL DEFAULT 0,

  `date_str` DATETIME,

  `create_time` DATETIME,
  
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


 DROP TABLE IF EXISTS `eh_pm_task_logs`;
CREATE TABLE `eh_pm_task_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `task_id` BIGINT NOT NULL DEFAULT 0,

  `content` TEXT COMMENT 'content data',
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  `target_type` VARCHAR(32) COMMENT 'user',
  `target_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'target user id if target_type is a user',

  `operator_uid` BIGINT NOT NULL DEFAULT 0,
  `operator_time` DATETIME,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

 DROP TABLE IF EXISTS `eh_pm_task_attachments`;
CREATE TABLE `eh_pm_task_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(32) COMMENT 'attachment object owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999991, 0, 0, 0, '/home', 'Bizs', '物业报修', '物业报修', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://10.1.10.102/property_service/index.html?hidenavigatIonbar=1#/My_service#sIgn_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999991, 0, 0, 0, '/home', 'Bizs', '物业报修', '物业报修', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://10.1.10.102/property_service/index.html?hidenavigatIonbar=1#/My_service#sIgn_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');    
http://10.1.10.102/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix

http://10.1.10.102/property_service/index.html#/?_k=e0laah