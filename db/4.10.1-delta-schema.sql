-- 增加咨询电话 by st.zheng
ALTER TABLE `eh_news` ADD COLUMN `phone` BIGINT(20) NULL DEFAULT '0' AFTER `source_url`;

-- 新建标签表 by st.zheng
CREATE TABLE `eh_news_tag` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) DEFAULT NULL,
  `owner_id` bigint(20) DEFAULT '0',
  `parent_id` bigint(20)  DEFAULT '0',
  `value` varchar(32) DEFAULT NULL,
  `is_search` tinyint(8)  DEFAULT '0' COMMENT '是否开启筛选',
  `is_default` tinyint(8)  DEFAULT '0' COMMENT '是否是默认选项',
  `delete_flag` tinyint(8) NOT NULL DEFAULT '0',
  `default_order` bigint(20)  DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_news_tag_vals` (
  `id` BIGINT(20) NOT NULL,
  `news_id` BIGINT(20) NOT NULL DEFAULT '0',
  `news_tag_id` BIGINT(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`));

-- 园区地图add by sw 20171009
CREATE TABLE `eh_community_map_shops` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT 0,
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the type, community, enterprise, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT 0,
  `shop_name` varchar(128) DEFAULT NULL,
  `shop_type` varchar(128) DEFAULT NULL,
  `business_hours` varchar(512) DEFAULT NULL,
  `contact_name` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(128) DEFAULT NULL,
  `building_id` bigint(20) NOT NULL,
  `address_id` bigint(20) NOT NULL,
  `description` text,
  `shop_Avatar_uri` varchar(1024) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT 0,
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) NOT NULL DEFAULT 0,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;