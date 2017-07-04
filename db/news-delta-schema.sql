CREATE TABLE `eh_news_communities` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `news_id` bigint(20) NOT NULL COMMENT 'news id',
  `community_id` bigint(20) NOT NULL COMMENT 'community id',
  `creator_uid` bigint(20) NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


