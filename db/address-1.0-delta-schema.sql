-- 关系表建表脚本
DROP TABLE IF EXISTS `eh_community_default`;
CREATE TABLE `eh_community_default` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `origin_community_id` bigint(20) NOT NULL,
  `target_community_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;