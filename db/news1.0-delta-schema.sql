DROP TABLE IF EXISTS `eh_news`;
CREATE TABLE `eh_news` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0' COMMENT 'namespace id',
	`owner_type` VARCHAR(32) NULL DEFAULT '0' COMMENT 'ORGANIZATION',
	`owner_id` BIGINT(20) NULL DEFAULT '0' COMMENT 'organization_id',
	`title` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'title',
	`author` VARCHAR(128) NULL DEFAULT NULL COMMENT 'author',
	`cover_uri` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'cover image uri',
	`content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'object content type: link url、rich text',
	`content` LONGTEXT NULL COMMENT 'content data, depends on value of content_type',
	`content_abstract` TEXT NULL COMMENT 'abstract of content data',
	`source_desc` VARCHAR(128) NULL DEFAULT NULL COMMENT 'where the news comes from',
	`source_url` VARCHAR(256) NULL DEFAULT NULL COMMENT 'the url of source',
	`child_count` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'comment count',
	`forward_count` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'forward count',
	`like_count` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'like count',
	`view_count` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'view count',
	`publish_time` DATETIME NULL DEFAULT NULL COMMENT 'the time when the news was created, now equals to create_time',
	`top_index` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'if has this value, go to first, order from big to small',
	`top_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'whether it is top',
	`status` TINYINT(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'news creator uid',
	`create_time` DATETIME NULL DEFAULT NULL COMMENT 'create time',
	`deleter_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'deleter uid',
	`delete_time` DATETIME NULL DEFAULT NULL COMMENT 'mark-deletion policy. historic data may be useful',
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `eh_news_attachments`;
CREATE TABLE `eh_news_attachments` (
	`id` BIGINT(20) NOT NULL COMMENT 'id of the record',
	`owner_id` BIGINT(20) NOT NULL COMMENT 'owner id, e.g comment_id',
	`content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'attachment object link info on storage',
	`creator_uid` BIGINT(20) NOT NULL,
	`create_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`)
);

DROP TABLE IF EXISTS `eh_news_comment`;
CREATE TABLE `eh_news_comment` (
	`id` BIGINT(20) NOT NULL COMMENT 'id of the record',
	`owner_id` BIGINT(20) NOT NULL COMMENT 'owner id, e.g news_id',
	`content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'object content type',
	`content` TEXT NULL COMMENT 'content data, depends on value of content_type',
	`status` TINYINT(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`creator_uid` BIGINT(20) NOT NULL COMMENT 'creator uid',
	`create_time` DATETIME NOT NULL,
	`deleter_uid` BIGINT(20) NULL DEFAULT NULL COMMENT 'deleter uid',
	`delete_time` DATETIME NULL DEFAULT NULL COMMENT 'delete time',
	PRIMARY KEY (`id`)
);