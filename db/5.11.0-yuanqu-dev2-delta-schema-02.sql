CREATE TABLE `eh_rentalv2_price_classification` (
`id` bigint(20) NOT NULL ,
`namespace_id`  int NULL ,
`source_id`  bigint(20) NULL ,
`source_type`  varchar(255) NULL ,
`owner_id`  bigint(20) NULL ,
`owner_type`  varchar(255) NULL ,
`user_price_type`  tinyint(4) NULL ,
`classification`  varchar(255) NULL ,
`workday_price`  decimal(10,2) NULL ,
`original_price`  decimal(10,2) NULL ,
`initiate_price`  decimal(10,2) NULL ,
`discount_type`  tinyint(4) NULL ,
`full_price`  decimal(10,2) NULL ,
`cut_price`  decimal(10,2) NULL ,
`discount_ratio`  double NULL ,
`resource_type`  varchar(255) NULL,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `vip_level`  varchar(255) NULL COMMENT '会员等级 白金卡 金卡 银卡' AFTER `pay_channel`;
ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `source`  tinyint(4) NULL COMMENT '0 用户发起 1后台录入' AFTER `vip_level`;
ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `cross_commu_flag`  tinyint(4) NULL COMMENT '是否支持跨项目' AFTER `identify`;


ALTER TABLE `eh_rentalv2_resources`
MODIFY COLUMN `charge_uid`  varchar(256) NULL DEFAULT NULL COMMENT '负责人id' AFTER `notice`;
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `file_flag`  tinyint(4) NULL COMMENT '附件是否必传 0否 1是' AFTER `remark`;

CREATE TABLE `eh_vip_priority` (
  `id`  bigint(20) NOT NULL ,
  `namespace_id`  int NULL ,
  `vip_level` INT COMMENT '会员等级',
  `vip_level_text` VARCHAR(64)  COMMENT '会员等级文本',
  `priority` INT COMMENT '优先级,数字越大，优先级越高',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '会员等级优先级表';


-- 资源预约3.8
ALTER TABLE `eh_rentalv2_resources`
ADD COLUMN `people_spec`  integer(10) NULL COMMENT '容纳人数' AFTER `address_id`;

CREATE TABLE `eh_rentalv2_structure_template` (
`id`  bigint(20) NOT NULL ,
`name`  varchar(255) NULL ,
`display_name`  varchar(255) NULL ,
`icon_uri`  varchar(255) NULL ,
`default_order`  bigint(20) NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_rentalv2_structures` (
`id`  bigint(20) NOT NULL ,
`template_id`  bigint(20) NOT NULL ,
`source_type`  varchar(45) NULL ,
`source_id`  bigint(20) NULL ,
`name`  varchar(255) NULL ,
`display_name`  varchar(255) NULL ,
`icon_uri`  varchar(255) NULL ,
`is_surport`  tinyint(4) NULL ,
`default_order`  bigint(20) NULL ,
`resource_type`  varchar(45) NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



-- AUTHOR: 黄良铭
-- REMARK: 用户当前场景表添加一个字段
ALTER TABLE eh_user_current_scene ADD COLUMN  extra TEXT;


-- AUTHOR: 黄明波
-- REMARK: 服务联盟v3.7
-- start
CREATE TABLE `eh_alliance_faqs` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL,
	`owner_type` VARCHAR(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL,
	`type` BIGINT(20) NOT NULL COMMENT '服务联盟类型',
	`title` VARCHAR(128) NOT NULL COMMENT '标题',
	`type_id` BIGINT(20) NOT NULL COMMENT '分类id',
	`type_name` VARCHAR(50) NOT NULL COMMENT '分类名称',
	`content` MEDIUMTEXT NOT NULL COMMENT '内容详情',
	`solve_times` INT(11) NOT NULL DEFAULT '0' COMMENT '解决次数',
	`un_solve_times` INT(11) NOT NULL DEFAULT '0' COMMENT '未解决次数',
	`top_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-非热门 1-热门',
	`default_order` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '默认排序 数字小的在上面',
	`top_order` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '热门排序 数字小的在上面',
	`status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-已被删除 2-正常',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`create_uid` BIGINT(20) NOT NULL COMMENT '创建人',
	PRIMARY KEY (`id`),
	INDEX `i_eh_top_order` (`top_order`),
	INDEX `i_eh_default_order` (`default_order`),
	INDEX `i_eh_type_id_type` (`owner_type`, `owner_id`, `type`)
)
COMMENT='问题表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `eh_alliance_faq_types` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL,
	`owner_type` VARCHAR(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL,
	`type` BIGINT(20) NOT NULL,
	`name` VARCHAR(50) NOT NULL COMMENT '分类名称',
	`status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-已被删除 2-正常',
	`default_order` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '默认排序 数字小的在上面',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`create_uid` BIGINT(20) NOT NULL COMMENT '创建人',
	PRIMARY KEY (`id`)
)
COMMENT='问题分类表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `eh_alliance_operate_services` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL,
	`owner_type` VARCHAR(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL,
	`type` BIGINT(20) NOT NULL,
	`service_id`  BIGINT(20) NOT NULL COMMENT '服务id',
	`default_order` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '排序，数字小的在上面',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`create_uid` BIGINT(20) NOT NULL COMMENT '创建人',
	PRIMARY KEY (`id`)
)
COMMENT='运营模块展示服务'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;



CREATE TABLE `eh_alliance_faq_service_customers` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL,
	`owner_type` VARCHAR(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL,
	`type` BIGINT(20) NOT NULL,
	`user_id` BIGINT(20) NOT NULL,
	`user_name` VARCHAR(50) NOT NULL,
	`hotline_number` VARCHAR(50) NOT NULL,
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`create_uid` BIGINT(20) NOT NULL COMMENT '创建人',
	PRIMARY KEY (`id`)
)
COMMENT='faq的客服'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

ALTER TABLE `eh_service_alliances` ADD COLUMN `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间' ;
ALTER TABLE `eh_service_alliances`	ADD COLUMN `update_uid` BIGINT NULL COMMENT '更新用户id' ;
ALTER TABLE `eh_service_alliance_application_records` ADD COLUMN `update_time` DATETIME NULL DEFAULT NULL COMMENT '更新时间' ;
-- end

-- AUTHOR: 黄明波
-- REMARK: issue-41586
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `user_notify_flag` TINYINT(4) NULL DEFAULT '0' COMMENT '0-未起定时器通知用户 1-已起定时器通知用户';

-- AUTHOR: xq.tian  20181115
-- REMARK: issue-42372,增加 target_data 的长度
ALTER TABLE eh_banners MODIFY COLUMN target_data TEXT;