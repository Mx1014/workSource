-- 论坛发布类型  add by yanjun 20171122
CREATE TABLE `eh_forum_service_types` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `module_type` tinyint(4) DEFAULT NULL COMMENT 'module type, 1-forum,2-activity......',
  `category_id` bigint(20) DEFAULT NULL,
  `service_type` varchar(255) NOT NULL,
  `sort_num` int(11) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 热门标签，增加模块类型，1-论坛、2-活动、3-俱乐部...等等  add by yanjun 20171123
ALTER TABLE `eh_hot_tags`
ADD COLUMN `module_type`  tinyint(4) NULL DEFAULT 1 COMMENT 'module type, 1-forum 2-activity...........';