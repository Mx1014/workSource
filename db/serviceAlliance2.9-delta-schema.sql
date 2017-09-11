-- DROP TABLE IF EXISTS `eh_service_alliance_comments`;
CREATE TABLE `eh_service_alliance_comments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g servicealliance id',
	`content_type` VARCHAR(32) COMMENT 'object content type',
	`content` TEXT COMMENT 'content data, depends on value of content_type',
	`status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
	`create_time` DATETIME,
	`deleter_uid` BIGINT COMMENT 'deleter uid',
	`delete_time` DATETIME COMMENT 'delete time',
	
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_service_alliance_comment_attachments`;
CREATE TABLE `eh_service_alliance_comment_attachments` (
	`id` BIGINT(20) NOT NULL COMMENT 'id of the record',
	`namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(64),
	`owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
	`content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'attachment object link info on storage',
	`status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT NOT NULL,
	`create_time` DATETIME NOT NULL,
	`operator_uid` BIGINT,
	`update_time` DATETIME,

	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 服务联盟	添加概要描述
ALTER TABLE `eh_service_alliances` ADD COLUMN `summary_description` VARCHAR(1024) COMMENT '';
ALTER TABLE `eh_service_alliances` ADD COLUMN `enable_comment` TINYINT DEFAULT 0 COMMENT '1,enable;0,disable';
