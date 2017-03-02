-- 账单表
DROP TABLE IF EXISTS `eh_asset_bills`;
CREATE TABLE `eh_asset_bills` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `owner_type` VARCHAR(32) COMMENT 'bill owner type: enterprise',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'bill owner id: enterprise id',
  `target_type` VARCHAR(32) COMMENT 'bill target type: community',
  `target_id` BIGINT NOT NULL COMMENT 'bill target id: community id',
  `tenant_type` VARCHAR(32) COMMENT 'bill tenant type: family、enterprise',
  `tenant_id` BIGINT NOT NULL COMMENT 'bill tenant id: family、enterprise id',
  `source` TINYINT NOT NULL DEFAULT 0 COMMENT '0: auto, 1: third party, 2: manual',
  
  `account_period` DATETIME NOT NULL,
  `building_name` VARCHAR(128),
  `apartment_name` VARCHAR(128),
  `address_id` BIGINT NOT NULL,
  `contact_no` VARCHAR(32),
  
  `rental` DECIMAL(10,2) COMMENT '租金',
  `property_management_fee` DECIMAL(10,2) COMMENT '物业管理费',
  `unit_maintenance_fund` DECIMAL(10,2) COMMENT '本体维修基金',
  `late_fee` DECIMAL(10,2) COMMENT '滞纳金',
  `private_water_fee` DECIMAL(10,2) COMMENT '自用水费',
  `private_electricity_fee` DECIMAL(10,2) COMMENT '自用电费',
  `public_water_fee` DECIMAL(10,2) COMMENT '公共部分水费',
  `public_electricity_fee` DECIMAL(10,2) COMMENT '公共部分电费',
  `waste_disposal_fee` DECIMAL(10,2) COMMENT '垃圾处理费',
  `pollution_discharge_fee` DECIMAL(10,2) COMMENT '排污处理费',
  `extra_air_condition_fee` DECIMAL(10,2) COMMENT '加时空调费',
  `cooling_water_fee` DECIMAL(10,2) COMMENT '冷却水使用费',
  `weak_current_slot_fee` DECIMAL(10,2) COMMENT '弱电线槽使用费',
  `deposit_from_lease` DECIMAL(10,2) COMMENT '租赁保证金',
  `maintenance_fee` DECIMAL(10,2) COMMENT '维修费',
  `gas_oil_process_fee` DECIMAL(10,2) COMMENT '燃气燃油加工费',
  `hatch_service_fee` DECIMAL(10,2) COMMENT '孵化服务费',
  `pressurized_fee` DECIMAL(10,2) COMMENT '加压费',
  `parking_fee` DECIMAL(10,2) COMMENT '停车费',
  `other` DECIMAL(10,2) COMMENT '其他',
  
  `period_account_amount` DECIMAL(10,2),
  `period_unpaid_account_amount` DECIMAL(10,2),
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: unpaid, 2: paid',

  `template_version` BIGINT NOT NULL,
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  `delete_uid` BIGINT,
  `delete_time` DATETIME(3),

  PRIMARY KEY (`id`),
  UNIQUE KEY `u_eh_bill_account_period_address`(`account_period`, `address_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- 用户模板字段表（给一组初始数据）
DROP TABLE IF EXISTS `eh_asset_bill_template_fields`;
CREATE TABLE `eh_asset_bill_template_fields` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  `required_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: optional, 1: required',
  `selected_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: unselected, 1: selected',
  
  `owner_type` VARCHAR(32) COMMENT 'template field owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'template field owner id',
  `target_type` VARCHAR(32) COMMENT 'template field target type: community',
  `target_id` BIGINT NOT NULL COMMENT 'template field target id: community id',
  
  `field_name` VARCHAR(64) NOT NULL,
  `field_display_name` VARCHAR(64) NOT NULL,
  `field_custom_name` VARCHAR(64),
  `field_type` VARCHAR(64),
  `default_order` INTEGER,
  `template_version` BIGINT NOT NULL,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 物业公司催缴记录
DROP TABLE IF EXISTS `eh_asset_bill_notify_record`;
CREATE TABLE `eh_asset_bill_notify_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace of owner resource, redundant info to quick namespace related queries',
  
  `owner_type` VARCHAR(32) COMMENT 'notify record owner type',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'notify record owner id',
  `target_type` VARCHAR(32) COMMENT 'notify record target type: community',
  `target_id` BIGINT NOT NULL COMMENT 'notify record target id: community id',
  
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;