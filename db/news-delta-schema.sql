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

-- 增加域空间左上角显示场景名称的配置项
ALTER TABLE eh_namespace_details ADD COLUMN name_type tinyint(4) DEFAULT 0;

-- fix 15631 & 15636 add by xiongying20170919
ALTER TABLE eh_organizations ADD COLUMN website VARCHAR(256);
ALTER TABLE eh_organizations ADD COLUMN unified_social_credit_code VARCHAR(256);

-- 同步数据时为企业添加门牌时太慢，添加索引 add by xiongying20170922
ALTER TABLE `eh_addresses` ADD INDEX namespace_address ( `namespace_address_type`, `namespace_address_token`);

