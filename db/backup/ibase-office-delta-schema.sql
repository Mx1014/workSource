 
-- 
-- 工位预定空间表
-- 
DROP TABLE IF EXISTS `eh_office_cubicle_spaces`;

CREATE TABLE `eh_office_cubicle_spaces` (
  `id` BIGINT COMMENT 'id',
  `namespace_id` INTEGER COMMENT '',
  `name` VARCHAR (128) COMMENT '工位空间名称',
  `province_id` BIGINT COMMENT '省份id',
  `province_name` VARCHAR (128) COMMENT '省份名称',
  `city_id` BIGINT COMMENT '城市id',
  `city_name` VARCHAR (128) COMMENT '城市名称',
  `cover_uri` VARCHAR (1024) COMMENT '封面图片',
  `address` VARCHAR (1024) COMMENT '地址',
  `longitude` DOUBLE COMMENT '经度',
  `latitude` DOUBLE COMMENT '纬度',
  `geohash` VARCHAR (32) COMMENT '',
  `contact_phone` VARCHAR (32) COMMENT '咨询电话',
  `manager_uid` BIGINT COMMENT '负责人uid',
  `description` TEXT COMMENT '详情-html片',
  `status` TINYINT COMMENT '状态: 2-正常 ,0-不可用',
  `creator_uid` BIGINT COMMENT '',
  `create_time` DATETIME COMMENT '',
  `operator_uid` BIGINT COMMENT '',
  `operate_time` DATETIME COMMENT '',
  PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 ;

  
-- 
-- 工位预定  附件-banner图表
-- 
DROP TABLE IF EXISTS `eh_office_cubicle_attachments`;

CREATE TABLE `eh_office_cubicle_attachments` (  
`id` BIGINT  COMMENT 'id',
`owner_id` BIGINT  COMMENT '工位空间id',
`content_type` VARCHAR(32)  COMMENT '内容类型',
`content_uri` VARCHAR(1024)  COMMENT 'uri',
`creator_uid` BIGINT   COMMENT '',
`create_time` DATETIME   COMMENT '',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;  

-- 
-- 工位预定 出租空间表
-- 
DROP TABLE IF EXISTS `eh_office_cubicle_categories`;

CREATE TABLE `eh_office_cubicle_categories` ( 
`id` BIGINT  COMMENT 'id',
`namespace_id` INTEGER  COMMENT '',
`space_id` BIGINT  COMMENT '工位空间id',
`rent_type` TINYINT  COMMENT '租赁类别: 1-开放式（默认space_type 1）,2-办公室',
`space_type` TINYINT  COMMENT '空间类别: 1-工位,2-面积',
`space_size` INTEGER  COMMENT '工位数或面积数',
`creator_uid` BIGINT   COMMENT '',
`create_time` DATETIME   COMMENT '',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;  

-- 
-- 工位预定 订单表
-- 
DROP TABLE IF EXISTS `eh_office_cubicle_orders`;

CREATE TABLE `eh_office_cubicle_orders` ( 
`id` BIGINT  COMMENT 'id',
`namespace_id` INTEGER  COMMENT '',
`space_id` BIGINT  COMMENT '工位空间id',
`space_name` VARCHAR(128)  COMMENT '工位空间名称',
`province_id` BIGINT   COMMENT '省份id',
`province_name` VARCHAR(100)  COMMENT '省份名称',
`city_id` BIGINT   COMMENT '城市id',
`city_name` VARCHAR(128)  COMMENT '城市名称',
`cover_uri` VARCHAR(1024)  COMMENT '封面图片',
`address` VARCHAR(1024)  COMMENT '地址',
`longitude` DOUBLE  COMMENT '经度',
`latitude` DOUBLE  COMMENT '纬度',
`geohash` VARCHAR(32)  COMMENT '',
`contact_phone` VARCHAR(32)  COMMENT '咨询电话',
`manager_uid` BIGINT   COMMENT '负责人uid',
`description` TEXT  COMMENT '详情-html片',
`rent_type` TINYINT  COMMENT '租赁类别: 1-开放式（默认space_type 1）,2-办公室',
`space_type` TINYINT  COMMENT '空间类别: 1-工位,2-面积',
`space_size` VARCHAR(32)  COMMENT '工位数或面积数',
`status` TINYINT  COMMENT '状态: 2-用户可见,0-用户不可见',
`order_type` TINYINT  COMMENT '预定类别: 1-参观 2-预定',
`reserver_uid` BIGINT   COMMENT '预订人uid',
`reserve_time` DATETIME  COMMENT '预定时间',
`reserver_name` VARCHAR(64)  COMMENT '预订人姓名',
`reserve_contact_token` VARCHAR(32)  COMMENT '预定联系方式',
`reserve_enterprise` VARCHAR(512)  COMMENT '预订人公司',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;