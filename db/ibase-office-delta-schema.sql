 
-- 
-- 工位预定空间表
-- 
DROP TABLE IF EXISTS `eh_office_cubicle_spaces`;

CREATE TABLE `eh_office_cubicle_spaces` (
`id` BIGINT(20)  COMMENT 'id',
`name` VARCHAR(100)  COMMENT '工位空间名称',
`province_id` BIGINT(20)   COMMENT '省份id',
`province_name` VARCHAR(100)  COMMENT '省份名称',
`city_id` BIGINT(20)   COMMENT '城市id',
`city_name` VARCHAR(100)  COMMENT '城市名称',
`address` VARCHAR(1000)  COMMENT '地址',
`longitude` DOUBLE  COMMENT '经度',
`latitude` DOUBLE  COMMENT '纬度',
`contact_phone` VARCHAR(20)  COMMENT '咨询电话',
`charge_uid` BIGINT(20)   COMMENT '负责人uid',
`details` TEXT  COMMENT '详情-html片',
`cover_uri` VARCHAR(1000)  COMMENT '封面图片',
`creator_uid` BIGINT(20)   COMMENT '',
`create_time` DATETIME   COMMENT '',
`operator_uid` BIGINT(20)   COMMENT '',
`operate_time` DATETIME   COMMENT '',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;
  
-- 
-- 工位预定空间表
-- 
DROP TABLE IF EXISTS `eh_office_cubicle_banners`;

CREATE TABLE `eh_office_cubicle_banners` (  
`id` BIGINT(20)  COMMENT 'id',
`space_id` BIGINT(20)  COMMENT '工位空间id',
`banner_uri` VARCHAR(1000)  COMMENT 'banner图的uri',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;  

-- 
-- 工位预定空间表
-- 
DROP TABLE IF EXISTS `eh_office_cubicle_sites`;

CREATE TABLE `eh_office_cubicle_sites` ( 
`id` BIGINT(20)  COMMENT 'id',
`space_id` BIGINT(20)  COMMENT '工位空间id',
`site_type` TINYINT  COMMENT '开放场所类别0开放式工位 1工位办公室 2面积办公室',
`size` VARCHAR(10)  COMMENT '工位数或面积数',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;  

-- 
-- 工位预定空间表
-- 
DROP TABLE IF EXISTS `eh_office_cubicle_orders`;

CREATE TABLE `eh_office_cubicle_orders` ( 
`id` BIGINT(20)  COMMENT 'id',
`name` VARCHAR(100)  COMMENT '工位空间名称',
`province_id` BIGINT(20)   COMMENT '省份id',
`province_name` VARCHAR(100)  COMMENT '省份名称',
`city_id` BIGINT(20)   COMMENT '城市id',
`city_name` VARCHAR(100)  COMMENT '城市名称',
`address` VARCHAR(1000)  COMMENT '地址',
`longitude` DOUBLE  COMMENT '经度',
`latitude` DOUBLE  COMMENT '纬度',
`contact_phone` VARCHAR(20)  COMMENT '咨询电话',
`charge_uid` BIGINT(20)   COMMENT '负责人uid',
`details` TEXT  COMMENT '详情-html片',
`cover_uri` VARCHAR(1000)  COMMENT '封面图片',
`site_type` TINYINT  COMMENT '开放场所类别0开放式工位 1工位办公室 2面积办公室',
`size` VARCHAR(10)  COMMENT '工位数或面积数',
`status` TINYINT  COMMENT '0：用户可见 -1用户不可见',
`order_type` TINYINT  COMMENT '预定类别：0：参观 1：预定',


  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4	
;