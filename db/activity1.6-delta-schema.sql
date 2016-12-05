ALTER TABLE eh_activities ADD COLUMN `achievement` text;

-- 活动附件表
DROP TABLE IF EXISTS  `eh_activity_attachments`;
CREATE TABLE `eh_activity_attachments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`activity_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
	`content_type` VARCHAR(32) COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
	`creator_uid` BIGINT NOT NULL,
	`create_time` DATETIME NOT NULL, 
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 活动物资管理表
DROP TABLE IF EXISTS  `eh_activity_goods`;
CREATE TABLE `eh_activity_goods` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`activity_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
	`name` VARCHAR(64) COMMENT 'attachment object content type',
	`price` DECIMAL,
    `quantity` INTEGER NOT NULL DEFAULT 0,
    `total_price` DECIMAL,
	`handlers` VARCHAR(64),
	`create_time` DATETIME NOT NULL, 
    `creator_uid` BIGINT,
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


 