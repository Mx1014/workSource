-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR:tangcen
-- REMARK:招商广告表
CREATE TABLE `eh_investment_advertisement` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) DEFAULT NULL,
  `community_id` bigint(20) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '广告主题',
  `advertisement_type` tinyint(4) DEFAULT NULL COMMENT '广告类型（1-招租广告，2-招商广告）',
  `business_status` tinyint(4) DEFAULT NULL COMMENT '招商状态',
  `available_area_min` decimal(14,4) DEFAULT NULL COMMENT '招商面积起点',
  `available_area_max` decimal(14,4) DEFAULT NULL COMMENT '招商面积终点',
  `asset_price_min` decimal(14,4) DEFAULT NULL COMMENT '招商价格起点',
  `asset_price_max` decimal(14,4) DEFAULT NULL COMMENT '招商价格终点',
  `price_unit` tinyint(4) DEFAULT NULL COMMENT '价格单位（1：元/平方米/月）',
  `apartment_floor_min` int(11) DEFAULT NULL COMMENT '招商楼层起点',
  `apartment_floor_max` int(11) DEFAULT NULL COMMENT '招商楼层终点',
  `orientation` varchar(64) DEFAULT NULL COMMENT '朝向',
  `address` varchar(255) DEFAULT NULL COMMENT '详细地址',
  `longitude` double DEFAULT NULL COMMENT '经度',
  `latitude` double DEFAULT NULL COMMENT '纬度',
  `contacts` varchar(128) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(128) DEFAULT NULL COMMENT '联系电话',
  `description` text COMMENT '广告内容描述',
  `post_uri` varchar(128) DEFAULT NULL COMMENT '封面图uri',
  `asset_related` tinyint(4) DEFAULT NULL COMMENT '是否关联楼宇房源（0：否，1：是）',
  `custom_form_flag` tinyint(4) DEFAULT NULL COMMENT '是否添加自定义表单（0：否，1：是）',
  `general_form_id` bigint(20) DEFAULT NULL COMMENT '关联的自定义表单id',
  `default_order` bigint(20) DEFAULT NULL COMMENT '排序字段（初始值等于主键id）',
  `status` tinyint(4) DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_uid` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告表';

-- AUTHOR:tangcen
-- REMARK:招商广告附件表
CREATE TABLE `eh_investment_advertisement_attachment` (
  `id` bigint(20) NOT NULL,
  `advertisement_id` bigint(20) DEFAULT NULL COMMENT '关联的广告id',
  `content_type` varchar(32) DEFAULT NULL COMMENT '附件类型',
  `content_uri` varchar(256) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告附件表';

-- AUTHOR:tangcen
-- REMARK:招商广告关联资产表
CREATE TABLE `eh_investment_advertisement_asset` (
  `id` bigint(20) NOT NULL,
  `advertisement_id` bigint(20) DEFAULT NULL COMMENT '关联的广告id',
  `asset_type` tinyint(4) DEFAULT NULL COMMENT '关联的资产类型（1：apartment，2：building，3：community）',
  `asset_id` int(11) DEFAULT NULL COMMENT '关联的资产id',
  `status` varchar(255) DEFAULT '2' COMMENT '0: inactive, 1: confirming, 2: active',
  `create_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='招商广告关联资产表';

-- --------------------- SECTION END ---------------------------------------------------------
