DROP TABLE IF EXISTS `eh_rich_text`;
CREATE TABLE `eh_rich_texts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT '0',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(128) COMMENT '',
  `resource_type` VARCHAR(128) COMMENT 'about, introduction, agreement', 
  `content_type` VARCHAR(128) COMMENT 'richtext, link', 
  `content` TEXT,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;