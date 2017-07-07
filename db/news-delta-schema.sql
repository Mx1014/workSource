CREATE TABLE `eh_news_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `news_id` bigint(20) NOT NULL COMMENT 'news id',
  `community_id` bigint(20) NOT NULL COMMENT 'community id',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_news_comment_rule` (
  `id` bigint(20) NOT NULL,
  `category_id` bigint(20) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` datetime DEFAULT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_news ADD COLUMN `visible_type` VARCHAR(32);
