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


-- AUTHOR: 吴寒
-- REMARK: 福利字段扩容
ALTER TABLE eh_welfare_coupons CHANGE `service_supply_name` `service_supply_name` VARCHAR(4096) COMMENT '适用地点';
ALTER TABLE eh_welfare_coupons CHANGE `service_range` `service_range` VARCHAR(4096) COMMENT '适用范围';
-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 到访类型修改
ALTER TABLE `eh_visitor_sys_visit_reason` ADD COLUMN `reason_type`  tinyint NULL DEFAULT null COMMENT '类型 0为住宅小区,1为商业小区';
ALTER TABLE `eh_visitor_sys_visit_reason` ADD COLUMN `visit_reason_code`  tinyint NULL DEFAULT null COMMENT '到访原因类型码';

-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 添加访客园区类型字段与园区Id
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `community_type`  tinyint NULL DEFAULT NULL COMMENT '园区类型 0为住宅小区,1为商业小区';
ALTER TABLE `eh_visitor_sys_visitors` ADD COLUMN `community_id`  bigint NULL DEFAULT NULL COMMENT '园区Id';


-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 第三方对接映射表
CREATE TABLE `eh_visitor_sys_third_mapping` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INT (11) NOT NULL DEFAULT '0' COMMENT 'namespace id',
    `owner_type` VARCHAR (64) NOT NULL COMMENT 'community or organization',
    `owner_id` BIGINT (20) NOT NULL DEFAULT '0' COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
    `visitor_id` BIGINT NOT NULL COMMENT '主键',
    `third_type` VARCHAR (128) NULL COMMENT '关联类型',
    `third_value` VARCHAR (128) NULL COMMENT '关联值',
    `creator_uid` BIGINT (20) DEFAULT NULL,
    `create_time` datetime DEFAULT NULL,
    `operator_uid` BIGINT (20) DEFAULT NULL,
    `operate_time` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COMMENT = '访客管理对接映射表';

-- AUTHOR: 马世亨 20181122
-- REMARK: 访客1.4 访客管理对接海康威视人员表
CREATE TABLE `eh_visitor_sys_hkws_user` (
`person_id` int NULL DEFAULT NULL COMMENT '人员ID',
`person_no` varchar(256) NULL DEFAULT NULL COMMENT '人员编号',
`person_name` varchar(256) NULL DEFAULT NULL COMMENT '姓名',
`gender` tinyint NULL DEFAULT NULL COMMENT '性别',
`certificate_type` int NULL DEFAULT NULL COMMENT '证件类型',
`certificate_no` varchar(256) NULL DEFAULT NULL COMMENT '证件号码',
`birthday` bigint NULL DEFAULT NULL COMMENT '出生日期',
`person_pinyin` varchar(256) NULL DEFAULT NULL COMMENT '姓名拼音',
`phone_no` varchar(256) NULL DEFAULT NULL COMMENT '联系电话',
`address` varchar(256) NULL DEFAULT NULL COMMENT '联系地址',
`photo` varchar(256) NULL DEFAULT NULL COMMENT '免冠照',
`english_name` varchar(256) NULL DEFAULT NULL COMMENT '英文名',
`email` varchar(256) NULL DEFAULT NULL COMMENT '邮箱',
`entry_date` bigint NULL DEFAULT NULL COMMENT '入职日期',
`leave_date` bigint NULL DEFAULT NULL COMMENT '离职日期',
`education` varchar(256) NULL DEFAULT NULL COMMENT '学历',
`nation` varchar(256) NULL DEFAULT NULL COMMENT '民族',
`dept_uuid` varchar(256) NULL DEFAULT NULL COMMENT '所属部门UUID',
`dept_no` varchar(256) NULL DEFAULT NULL COMMENT '所属部门编号',
`dept_name` varchar(256) NULL DEFAULT NULL COMMENT '所属部门名称',
`dept_path` varchar(256) NULL DEFAULT NULL COMMENT '所属部门路径',
`status` tinyint NULL DEFAULT NULL COMMENT '人员状态',
`identity_uuid` varchar(256) NULL DEFAULT NULL COMMENT '身份UUID',
`identity_name` varchar(256) NULL DEFAULT NULL COMMENT '身份名称',
`create_time` bigint NULL DEFAULT NULL COMMENT '创建时间',
`update_time` bigint NULL DEFAULT NULL COMMENT '更新时间',
`remark` varchar(256) NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`person_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='访客管理对接海康威视人员表';

-- AUTHOR: 张智伟
-- REMARK: 一些云部署的mysql没有开启支持TRIGGER脚本，修改实现方式，删除原有的trigger
DROP TRIGGER IF EXISTS employee_dismiss_trigger_for_auto_remove_payment_limit;