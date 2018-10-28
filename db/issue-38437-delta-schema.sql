
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
	`service_id` VARCHAR(50) NOT NULL COMMENT '服务id',
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
	`hotline` VARCHAR(50) NOT NULL,
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
