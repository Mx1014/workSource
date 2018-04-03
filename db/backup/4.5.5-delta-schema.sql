-- merge from warehouse by xiongying20170527
-- 仓库表
CREATE TABLE `eh_warehouses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `warehouse_number` VARCHAR(32) DEFAULT '',
  `volume` DOUBLE NOT NULL DEFAULT 0,
  `location` VARCHAR(512) NOT NULL DEFAULT '',
  `manager` VARCHAR(64),
  `contact` VARCHAR(64),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: disable, 2: enable',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
-- 物品表
CREATE TABLE `eh_warehouse_materials` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `material_number` VARCHAR(32) NOT NULL DEFAULT '',
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_warehouse_material_categories',
  `category_path` VARCHAR(128) COMMENT 'path of eh_warehouse_material_categories',
  `brand` VARCHAR(128),
  `item_no` VARCHAR(64),
  `reference_price` DECIMAL(10,2) NOT NULL DEFAULT '0.00',
  `unit_id` BIGINT COMMENT 'id of eh_warehouse_units',
  `specification_information` VARCHAR(1024),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
--  物品分类表
CREATE TABLE `eh_warehouse_material_categories` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `category_number` VARCHAR(32) DEFAULT '',
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `path` VARCHAR(128),
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
-- 仓库库存表
CREATE TABLE `eh_warehouse_stocks` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `amount` BIGINT NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `delete_uid` BIGINT,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
-- 仓库库存日志表
CREATE TABLE `eh_warehouse_stock_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_warehouse_requests',
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_source` TINYINT NOT NULL DEFAULT 0 COMMENT '0: request, 1: manual input',
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `delivery_amount` BIGINT NOT NULL DEFAULT 0,
  `stock_amount` BIGINT NOT NULL DEFAULT 0 COMMENT 'rest amount after delivery',
  `request_uid` BIGINT,
  `delivery_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
-- 申请表 
CREATE TABLE `eh_warehouse_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_uid` BIGINT,
  `request_organization_id` BIGINT,
  `remark` VARCHAR(512),
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `delivery_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0:no, 1: yes',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
 
-- 申请物品表
CREATE TABLE `eh_warehouse_request_materials` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `request_id` BIGINT NOT NULL COMMENT 'id of eh_warehouse_requests',
  `request_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: stock in, 1: stock out',
  `request_source` TINYINT NOT NULL DEFAULT 0 COMMENT '0: request, 1: manual input',
  `warehouse_id` BIGINT NOT NULL COMMENT 'id of eh_warehouses',
  `material_id` BIGINT NOT NULL COMMENT 'eh_warehouse_materials',
  `amount` BIGINT NOT NULL DEFAULT 0,
  `review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified',
  `review_uid` BIGINT,
  `review_time` DATETIME,
  `delivery_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0:no, 1: yes',
  `delivery_uid` BIGINT,
  `delivery_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 单位表 
CREATE TABLE `eh_warehouse_units` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INT(11) NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
  `owner_id` BIGINT(20) NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL,
  `status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `deletor_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


ALTER TABLE eh_import_file_tasks MODIFY COLUMN result LONGTEXT;


ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag1` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag2` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag3` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag4` VARCHAR(128);
ALTER TABLE `eh_rentalv2_config_attachments`  ADD COLUMN `string_tag5` VARCHAR(128);


ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `org_member_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部工作日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `org_member_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部节假日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `approving_user_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户工作日价格';
ALTER TABLE `eh_rentalv2_default_rules`  ADD COLUMN `approving_user_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户节假日价格';

ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `rental_start_time_flag` TINYINT DEFAULT 0 COMMENT '至少提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `rental_end_time_flag` TINYINT DEFAULT 0 COMMENT '最多提前预约时间标志: 1-限制, 0-不限制';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `org_member_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部工作日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `org_member_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '企业内部节假日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `approving_user_workday_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户工作日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `approving_user_weekend_price` DECIMAL(10,2) DEFAULT NULL COMMENT '外部客户节假日价格';
ALTER TABLE `eh_rentalv2_resources`  ADD COLUMN `default_order` BIGINT NOT NULL DEFAULT 0 COMMENT 'order';

ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `org_member_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价-如果打折则有(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `org_member_price` DECIMAL(10,2) DEFAULT NULL COMMENT '实际价格-打折则为折后价(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `approving_user_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价-如果打折则有（外部客户价）';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `approving_user_price` DECIMAL(10,2) DEFAULT NULL COMMENT '实际价格-打折则为折后价（外部客户价）';

ALTER TABLE `eh_rentalv2_orders`  ADD COLUMN `flow_case_id` BIGINT DEFAULT NULL COMMENT 'id of the flow_case';

ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_org_member_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-原价-如果打折则有(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_org_member_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-实际价格-打折则为折后价(企业内部价)';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_approving_user_original_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-原价-如果打折则有（外部客户价）';
ALTER TABLE `eh_rentalv2_cells`  ADD COLUMN `half_approving_user_price` DECIMAL(10,2) DEFAULT NULL COMMENT '半场-实际价格-打折则为折后价（外部客户价）';
