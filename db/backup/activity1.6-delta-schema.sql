ALTER TABLE eh_activities ADD COLUMN `achievement` text;
ALTER TABLE eh_activities ADD COLUMN `achievement_type` VARCHAR(32) COMMENT 'richtext, link';
ALTER TABLE eh_activities ADD COLUMN `achievement_richtext_url` VARCHAR(512) COMMENT 'richtext page';

-- 活动附件表
DROP TABLE IF EXISTS  `eh_activity_attachments`;
CREATE TABLE `eh_activity_attachments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`activity_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g application_id',
    `name` VARCHAR(128),
    `file_size` INTEGER NOT NULL DEFAULT 0,
	`content_type` VARCHAR(32) COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
    `download_count` INTEGER NOT NULL DEFAULT 0,
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
	`create_time` DATETIME NOT NULL, 
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 活动物资管理表
DROP TABLE IF EXISTS  `eh_activity_goods`;
CREATE TABLE `eh_activity_goods` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`activity_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
	`name` VARCHAR(64) COMMENT 'attachment object content type',
	`price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
    `quantity` INTEGER NOT NULL DEFAULT 0,
    `total_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
	`handlers` VARCHAR(64),
	`create_time` DATETIME NOT NULL, 
    `creator_uid` BIGINT,
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
