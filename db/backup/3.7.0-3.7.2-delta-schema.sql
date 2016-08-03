-- merge from rental2.0-delta-schema.sql 20160714
-- DROP TABLE IF EXISTS `eh_news`;
CREATE TABLE `eh_news` (
	`id` BIGINT NOT NULL,
	`namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
	`owner_type` VARCHAR(32) COMMENT 'ORGANIZATION',
	`owner_id` BIGINT DEFAULT 0 COMMENT 'organization_id',
	`title` VARCHAR(1024) COMMENT 'title',
	`author` VARCHAR(128) COMMENT 'author',
	`cover_uri` VARCHAR(1024) COMMENT 'cover image uri',
	`content_type` VARCHAR(32) COMMENT 'object content type: link url、rich text',
	`content` LONGTEXT COMMENT 'content data, depends on value of content_type',
	`content_abstract` TEXT COMMENT 'abstract of content data',
	`source_desc` VARCHAR(128) COMMENT 'where the news comes from',
	`source_url` VARCHAR(256) COMMENT 'the url of source',
	`child_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'comment count',
	`forward_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'forward count',
	`like_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'like count',
	`view_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'view count',
	`publish_time` DATETIME COMMENT 'the time when the news was created, now equals to create_time',
	`top_index` BIGINT NOT NULL DEFAULT 0 COMMENT 'if has this value, go to first, order from big to small',
	`top_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'whether it is top',
	`status` TINYINT NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'news creator uid',
	`create_time` DATETIME COMMENT 'create time',
	`deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter uid',
	`delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
	
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_news_attachments`;
CREATE TABLE `eh_news_attachments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g comment_id',
	`content_type` VARCHAR(32) COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
	`creator_uid` BIGINT NOT NULL,
	`create_time` DATETIME NOT NULL,
	
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_news_comment`;
CREATE TABLE `eh_news_comment` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id, e.g news_id',
	`content_type` VARCHAR(32) COMMENT 'object content type',
	`content` TEXT COMMENT 'content data, depends on value of content_type',
	`status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
	`create_time` DATETIME,
	`deleter_uid` BIGINT COMMENT 'deleter uid',
	`delete_time` DATETIME COMMENT 'delete time',
	
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;