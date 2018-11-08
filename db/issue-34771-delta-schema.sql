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

-- AUTHOR: 李清岩
-- REMARK: 门禁v3.0.2 issue-34771
-- REMARK: 增加门禁组门禁关系表
CREATE TABLE `eh_aclink_group_doors` (
	`id` BIGINT  NOT NULL,
	`namespace_id` INT  DEFAULT NULL COMMENT '域空间id',
	`owner_id` BIGINT  NOT NULL DEFAULT '0' COMMENT '门禁组所属机构id',
	`owner_type` TINYINT DEFAULT NULL COMMENT '门禁组所属机构类型 0园区 1公司',
	`group_id` BIGINT DEFAULT NULL COMMENT '门禁组id',
	`door_id` BIGINT DEFAULT NULL COMMENT '门禁组所属门禁id',
	`status` TINYINT NOT NULL DEFAULT '1' COMMENT '状态 0删除 1有效',
	`creator_uid` BIGINT  DEFAULT NULL COMMENT '创建者id',
	`create_time` datetime DEFAULT NULL COMMENT '创建时间',
	`operator_uid` BIGINT  DEFAULT NULL COMMENT '修改者id',
	`operator_time` datetime DEFAULT NULL COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '门禁组门禁关系表';