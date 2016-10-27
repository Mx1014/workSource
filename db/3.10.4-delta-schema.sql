--
-- 服务联盟category添加显示类型字段   add by xq.tian  2016/10/18
--
ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `display_mode` TINYINT DEFAULT 1;

-- 服务联盟添加新的预约表 add by xiongying20161027
CREATE TABLE `eh_service_alliance_reservation_requests` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `type` bigint(20) NOT NULL DEFAULT '0',
  `category_id` bigint(20) NOT NULL DEFAULT '0',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `creator_name` varchar(128) DEFAULT NULL,
  `creator_mobile` varchar(128) DEFAULT NULL,
  `creator_organization_id` bigint(20) NOT NULL DEFAULT '0',
  `service_alliance_id` bigint(20) NOT NULL DEFAULT '0',
  `reserve_type` varchar(128) DEFAULT NULL,
  `reserve_organization` varchar(128) DEFAULT NULL,
  `reserve_time` varchar(128) DEFAULT NULL,
  `contact` varchar(128) DEFAULT NULL,
  `mobile` varchar(128) DEFAULT NULL,
  `remarks` varchar(1024) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;