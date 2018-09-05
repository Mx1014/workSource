-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户联系人表

CREATE TABLE `eh_enterprise_investment_contact`
(
	`id`                   bigint not null,
	`namespace_id` 				int(11) NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	`community_id`         bigint not null DEFAULT '0' COMMENT 'communityId',
	`name`             		VARCHAR(64) null comment '联系人名称',
	`phone_number`         bigint null comment '联系人电话',
	`email`           			VARCHAR(128) null comment '联系人邮箱',
	`position`         		VARCHAR(64) null comment '联系人职务',
	`address`							VARCHAR(256) null comment '联系人通讯地址',
	`type`				   TINYINT NULL COMMENT '联系人类型，0-客户联系人、1-渠道联系人',
	`origin_type`					TINYINT NULL COMMENT '联系人来源，0-客户管理，1-租客管理',
	`status`								TINYINT NULL COMMENT '联系人状态，0-invalid ,2-valid',
	`customer_id` 					BIGINT not null comment '关联的客户ID',
	`create_time`          DATETIME null comment '创建日期',
	`create_by` 						VARCHAR(64) null comment '创建人',
	`operator_time` 				DATETIME null comment '最近修改时间',
	`operator_by`					VARCHAR(64) NULL COMMENT '最近修改人',
	primary key (id)

) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_enterprise_investment_contact in dev mode';

-- end

-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户需求信息表


CREATE TABLE `eh_enterprise_investment_demand`
(
     `id`                   bigint not null,
	 `namespace_id` 				int(11) NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	 `community_id`         bigint not null DEFAULT '0' COMMENT 'communityId',
     `expected_location`    VARCHAR(256) null comment '期望地段',
	 `demand_area_min`			DOUBLE NULL COMMENT '期望最小面积',
	 `demand_area_max`			DOUBLE NULL COMMENT '期望最大面积',
	 `demand_price_min`			decimal(10,2) DEFAULT NULL COMMENT '期望最小租金-单价',
	 `demand_price_max`			decimal(10,2) DEFAULT NULL COMMENT '期望最大租金-单价',
	 `demand_price_unit`		TINYINT NULL COMMENT '期望租金单位，0-元/㎡，1-元/㎡/月,2-元/天，3-元/月，4-元',
	 `buy_or_lease`					TINYINT NULL COMMENT '租赁/购买：0-租赁，1-购买',
	 `expect_address`			  Long NULL COMMENT '意向房源',
	 `demand_version`				LONG NULL COMMENT '客户需求版本',
	 `status`								TINYINT NULL COMMENT '需求状态，0-invalid ,2-valid',
	 `customer_id` 					BIGINT not null comment '关联的客户ID',
	 `create_time`          DATETIME null comment '创建日期',
	 `create_by` 						VARCHAR(64) null comment '创建人',
	 `operator_time` 				DATETIME null comment '最近修改时间',
	 `operator_by`					VARCHAR(64) NULL COMMENT '最近修改人',
   primary key (id)

) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_enterprise_investment_demand in dev mode';

-- END


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户当前信息表

CREATE TABLE `eh_enterprise_investment_now_info`
(
     `id`                   bigint not null,
	 `namespace_id` 				int(11) NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	 `community_id`         bigint not null DEFAULT '0' COMMENT 'communityId',
     `now_address`    			VARCHAR(256) null comment '当前地址',
	 `now_rental`						decimal(10,2) NULL COMMENT '当前租金',
	 `now_rental_unit`		  TINYINT NULL COMMENT '期望租金单位，0-元/㎡，1-元/㎡/月,2-元/天，3-元/月，4-元',
	 `now_area`							DOUBLE NULL COMMENT '当前租赁面积',
	 `now_contract_end_date` DATETIME NULL COMMENT '当前合同到期日',
	 `status`								TINYINT NULL COMMENT '生效状态，0-invalid ,2-valid',
	 `customer_id` 					BIGINT not null comment '关联的客户ID',
	 `create_time`          DATETIME null comment '创建日期',
	 `create_by` 						VARCHAR(64) null comment '创建人',
	 `operator_time` 				DATETIME null comment '最近修改时间',
	 `operator_by`					VARCHAR(64) NULL COMMENT '最近修改人',
   primary key (id)

) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_enterprise_investment_now_info in dev mode';

-- end


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 对客户表增加招商客户和成交租客的表示加以区分

ALTER TABLE `eh_enterprise_customers` ADD `investment_type` TINYINT null comment '跟进信息类型，0-招商客户，1-成交租客';

-- end


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 对当前跟进表加上类型标识

ALTER TABLE `eh_customer_trackings` ADD `investment_type` TINYINT null comment '跟进信息类型，0-客户跟进信息，1-租客跟进信息';

-- end


-- AUTHOR: jiarui 20180831
-- REMARK: 客户表增加相关字段
ALTER TABLE `eh_enterprise_customers`ADD COLUMN `transaction_probability`  varchar(1024) NULL AFTER `financing_demand_item_id`;

ALTER TABLE `eh_enterprise_customers` ADD COLUMN `expected_sign_date` datetime NULL  AFTER `transaction_probability`;
-- end

-- AUTHOR: jiarui  20180831
-- REMARK: 园区入驻字段及数据迁移
ALTER TABLE eh_lease_promotions MODIFY rent_amount VARCHAR(1024) ;
-- end

-- 黄鹏宇 2018-9-4
-- 日志表增加操作对象类型
ALTER TABLE `eh_customer_events` ADD COLUMN investment_type tinyint null comment '操作客户类型，0-客户管理，1-租客管理';

-- REMARK: 客户表增加是否入驻状态
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `admission_item_id` BIGINT null default null ;

-- end