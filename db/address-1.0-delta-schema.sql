-- 关系表建表脚本
DROP TABLE IF EXISTS `eh_community_default`;
CREATE TABLE `eh_community_default` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `origin_community_id` bigint(20) NOT NULL,
  `target_community_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for eh_namespace_masks
-- ----------------------------
DROP TABLE IF EXISTS `eh_namespace_masks`;
CREATE TABLE `eh_namespace_masks` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `item_name` varchar(64) NOT NULL,
  `image_type` tinyint(4) DEFAULT NULL,
  `tips` varchar(255) DEFAULT NULL,
  `scene_type` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
