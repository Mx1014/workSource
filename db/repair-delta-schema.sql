
DROP TABLE IF EXISTS `eh_community_tasks`;
CREATE TABLE `eh_community_tasks` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,

	`service_category_id` BIGINT NOT NULL DEFAULT 0,
	`service_category_name` VARCHAR(128) NOT NULL DEFAULT '',
	`category_id` BIGINT NOT NULL DEFAULT 0,
	`category_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the name of category',
	`address` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'detail address',
	`content` TEXT NOT NULL DEFAULT '' COMMENT 'content data,
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
	`evaluate_score` BIGINT COMMENT 'evaluate score',

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


DROP TABLE IF EXISTS `eh_community_task_logs`;
CREATE TABLE `eh_community_task_logs` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
 	`repair_id` BIGINT NOT NULL,

	`content` TEXT NOT NULL DEFAULT '' COMMENT 'content data,
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive 1: wating, 2: allocated 3: completed 4: closed',
  	`target_id` BIGINT NOT NULL COMMENT 'target user id if target_type is a user',

	`operator_uid` BIGINT NOT NULL DEFAULT 0,
    `operator_time` DATETIME,

	`creator_uid` BIGINT NOT NULL DEFAULT 0,
    `create_time` DATETIME,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_community_task_attachments`;
CREATE TABLE `eh_community_task_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




