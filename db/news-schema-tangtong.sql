drop table if exists `eh_news`;
CREATE TABLE `eh_news` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`owner_uid` BIGINT(20) NOT NULL COMMENT 'owner user id',
	`source_type` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'the source type who refers the link, 0: none, 1: post',
	`source_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'the source id depends on source type',
	`title` VARCHAR(1024) NULL DEFAULT NULL,
	`author` VARCHAR(128) NULL DEFAULT NULL,
	`cover_uri` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'cover image uri',
	`content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'object content type: link url、rich text',
	`content` LONGTEXT NULL COMMENT 'content data, depends on value of content_type',
	`content_abstract` TEXT NULL COMMENT 'abstract of content data',
	`original` VARCHAR(128) NULL DEFAULT NULL COMMENT 'where the news comes from',
	`original_link` VARCHAR(256) NULL DEFAULT NULL COMMENT 'the url of original',
	`status` TINYINT(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
	`release_time` DATETIME NULL DEFAULT NULL COMMENT 'the time when the news was created, now equals to create_time',
	`create_time` DATETIME NULL DEFAULT NULL,
	`deleter_uid` BIGINT(20) NOT NULL COMMENT 'deleter id',
	`delete_time` DATETIME NULL DEFAULT NULL COMMENT 'mark-deletion policy. historic data may be useful',
	`sort_index` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'if has this value, go to first, order from big to small',
	PRIMARY KEY (`id`)
);

