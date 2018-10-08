-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户联系人表

CREATE TABLE `eh_customer_contacts`
(
	`id`                   BIGINT NOT NULL,
	`namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	`community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	`customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
	`name`             		VARCHAR(64)  COMMENT '联系人名称',
	`phone_number`         BIGINT  COMMENT '联系人电话',
	`email`           			VARCHAR(128)  COMMENT '联系人邮箱',
	`position`         		VARCHAR(128)  COMMENT '联系人职务',
	`address`							VARCHAR(256)  COMMENT '联系人通讯地址',
	`contact_type`				   TINYINT  COMMENT '联系人类型，0-客户联系人、1-渠道联系人',
	`customer_source`					TINYINT  COMMENT '联系人来源，0-客户管理，1-租客管理',
	`status`								TINYINT  COMMENT '联系人状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
	primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '招商客户联系人表';
ALTER TABLE `eh_customer_contacts` ADD INDEX idx_namespace_id(namespace_id);

-- end


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户跟进人表

CREATE TABLE `eh_customer_trackers`
(
	`id`                   BIGINT NOT NULL,
	`namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	`community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	`customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
	`tracker_uid`           BIGINT COMMENT '跟进人id',
	`tracker_type`				   TINYINT COMMENT '跟进人类型，0-招商跟进人、1-租户拜访人',
	`customer_source`					TINYINT  COMMENT '联系人来源，0-客户管理，1-租客管理',
	`status`								TINYINT  COMMENT '状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
	primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '招商客户跟进人表';
ALTER TABLE `eh_customer_trackers` ADD INDEX idx_namespace_id(namespace_id);

-- end

-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户需求信息表


CREATE TABLE `eh_customer_requirements`
(
   `id`                   BIGINT NOT NULL,
	 `namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	 `community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	 `customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
   `intention_location`    VARCHAR(256) COMMENT '期望地段',
	 `min_area`			DECIMAL(10,2) COMMENT '期望最小面积',
	 `max_area`			DECIMAL(10,2) COMMENT '期望最大面积',
	 `min_rent_price`			DECIMAL(10,2) COMMENT '期望最小租金-单价',
	 `max_rent_price`			DECIMAL(10,2) COMMENT '期望最大租金-单价',
	 `rent_price_unit`		TINYINT COMMENT '期望租金单位，0-元/㎡，1-元/㎡/月,2-元/天，3-元/月，4-元',
	 `rent_type`					TINYINT COMMENT '租赁/购买：0-租赁，1-购买',
	 `version`				      BIGINT COMMENT '记录版本',
	 `status`								TINYINT  COMMENT '状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
   primary key (id)

) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_enterprise_investment_demand in dev mode';

ALTER TABLE `eh_customer_requirements` ADD INDEX idx_namespace_id(namespace_id);


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户需求房源关系表

CREATE TABLE `eh_customer_requirement_addresses`
(
   `id`                   BIGINT NOT NULL,
	 `namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	 `community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	 `requirement_id` 			BIGINT NOT NULL DEFAULT '0' COMMENT '关联的需求ID',
	 `customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
	 `address_id`			      BIGINT COMMENT '意向房源',
	 `status`								TINYINT  COMMENT '状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '招商客户需求房源关系表';
ALTER TABLE `eh_customer_requirement_addresses` ADD INDEX idx_namespace_id(namespace_id);

-- END


-- AUTHOR 黄鹏宇 2018-9-6
-- REMARK 动态表单公用组件表

CREATE TABLE `eh_var_field_ranges`
(
   `id`                   BIGINT NOT NULL,
	 `group_path` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'refer to eh_var_fields',
    `field_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refer to eh_var_fields',
    `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
    `module_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '一组公用表单的类型',
    primary key (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '动态表单公用组件表';
ALTER TABLE `eh_var_field_ranges` ADD INDEX idx_module_name(module_name);
ALTER TABLE `eh_var_field_ranges` ADD INDEX idx_module_type(module_type);

-- AUTHOR 黄鹏宇 2018-9-6
-- REMARK 动态表单公用组件表

CREATE TABLE `eh_var_field_group_ranges`
(
   `id`                   BIGINT NOT NULL,
	 `group_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refer to eh_var_field_groups',
    `module_name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'the module which the field belong to',
    `module_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT '一组公用表单的类型',
    primary key (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '动态表单公用分组表';
ALTER TABLE `eh_var_field_group_ranges` ADD INDEX idx_module_name(module_name);
ALTER TABLE `eh_var_field_group_ranges` ADD INDEX idx_module_type(module_type);


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 招商客户当前信息表

CREATE TABLE `eh_customer_current_rents`
(
   `id`                   BIGINT NOT NULL,
	 `namespace_id` 				INT NOT NULL DEFAULT '0' COMMENT 'namespaceId',
	 `community_id`         BIGINT NOT NULL DEFAULT '0' COMMENT 'communityId',
	 `customer_id` 					BIGINT NOT NULL DEFAULT '0' COMMENT '关联的客户ID',
   `address`    			    VARCHAR(256) COMMENT '当前地址',
	 `rent_price`						DECIMAL(10,2) COMMENT '当前租金',
	 `rent_price_unit`		  TINYINT COMMENT '租金单位，0-元/㎡，1-元/㎡/月,2-元/天，3-元/月，4-元',
	 `rent_area`							DECIMAL(10,2) COMMENT '当前租赁面积',
	 `contract_intention_date` DATETIME COMMENT '当前合同到期日',
	 `version`				      BIGINT COMMENT '记录版本',
	 `status`								TINYINT  COMMENT '状态，0-invalid ,2-valid',
	`create_time`          DATETIME  COMMENT '创建日期',
	`creator_uid` 						BIGINT  COMMENT '创建人',
	`operator_time` 				DATETIME  COMMENT '最近修改时间',
	`operator_uid`					BIGINT  COMMENT '最近修改人',
   primary key (id)

) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '招商客户当前信息表';
ALTER TABLE `eh_customer_current_rents` ADD INDEX idx_namespace_id(namespace_id);

-- end


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 对客户表增加招商客户和成交租客的表示加以区分

ALTER TABLE `eh_enterprise_customers` ADD `customer_source` TINYINT COMMENT '跟进信息类型，0-招商客户，1-成交租客';

-- end


-- AUTHOR 黄鹏宇 2018-8-31
-- REMARK 对当前跟进表加上类型标识

ALTER TABLE `eh_customer_trackings` ADD `customer_source` TINYINT COMMENT '跟进信息类型，0-客户跟进信息，1-租客跟进信息';

-- end


-- AUTHOR: jiarui 20180831
-- REMARK: 客户表增加相关字段
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `transaction_ratio` VARCHAR(64);

ALTER TABLE `eh_enterprise_customers` ADD COLUMN `expected_sign_date` DATETIME;
-- end

-- AUTHOR: jiarui  20180831
-- REMARK: 园区入驻字段及数据迁移
-- ALTER TABLE eh_lease_promotions MODIFY rent_amount VARCHAR(1024) ;
-- end

-- 黄鹏宇 2018-9-4
-- 日志表增加操作对象类型
ALTER TABLE `eh_customer_events` ADD COLUMN investment_type TINYINT COMMENT '操作客户类型，0-客户管理，1-租客管理';

-- REMARK: 客户表增加是否入驻状态
ALTER TABLE `eh_enterprise_customers` ADD COLUMN `entry_status_item_id` BIGINT COMMENT '该用户是否入驻，1-未入驻，2-入驻';

-- end

-- AUTHOR: 杨崇鑫  20180920
-- REMARK: 账单表增加一个企业客户ID的字段
ALTER TABLE `eh_payment_bills` ADD COLUMN `customer_id` BIGINT COMMENT '企业客户ID';


