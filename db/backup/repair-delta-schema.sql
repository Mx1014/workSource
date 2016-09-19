
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
  `operator_name` VARCHAR(64) COMMENT 'the name of user',
  `operator_phone` VARCHAR(64) COMMENT 'the phone of user',
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



-- 增加一列存储升级内容
ALTER TABLE `eh_version_urls` ADD COLUMN `upgrade_description` TEXT NULL DEFAULT NULL AFTER `info_url`;

-- 增加app名称和发布时间列
ALTER TABLE `eh_version_urls` ADD COLUMN `app_name` VARCHAR(50) NULL,ADD COLUMN `publish_time` DATETIME NULL;

DROP TABLE IF EXISTS `eh_user_impersonations`;
CREATE TABLE `eh_user_impersonations`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'USER',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(64) NOT NULL COMMENT 'USER',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `description` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;