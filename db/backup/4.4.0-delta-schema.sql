--
-- 梯度价格表
--
CREATE TABLE `eh_energy_meter_price_config` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price configs, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,

  `name`         VARCHAR(255) COMMENT 'config name',
  `description`      VARCHAR(512) COMMENT 'description config',
  `expression`   VARCHAR(1024) COMMENT 'expression json',
  `status`       TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`  BIGINT,
  `create_time`  DATETIME,
  `update_uid`   BIGINT,
  `update_time`  DATETIME,
  PRIMARY KEY (`id`)
);

ALTER TABLE eh_energy_meter_default_settings ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_energy_meter_default_settings ADD COLUMN `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc';
ALTER TABLE eh_energy_meter_default_settings ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_energy_meter_default_settings ADD COLUMN `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费';
ALTER TABLE eh_energy_meter_default_settings ADD COLUMN `config_id` BIGINT COMMENT 'if setting_type is price and  have this value';

ALTER TABLE eh_energy_meter_formulas ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_energy_meter_formulas ADD COLUMN `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc';
ALTER TABLE eh_energy_meter_formulas ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT 0;

ALTER TABLE eh_energy_meter_categories ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_energy_meter_categories ADD COLUMN `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc';
-- merge from energy2.0 by xiongying20170328
ALTER TABLE eh_energy_meter_categories ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT 0;

ALTER TABLE eh_energy_meter_setting_logs ADD COLUMN `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费';
ALTER TABLE eh_energy_meter_setting_logs ADD COLUMN `config_id` BIGINT COMMENT 'if setting_type is price and  have this value';

ALTER TABLE eh_energy_meters ADD COLUMN `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price energy meter, enterprise, etc';
ALTER TABLE eh_energy_meters ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_energy_meters ADD COLUMN `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费';
ALTER TABLE eh_energy_meters ADD COLUMN `config_id` BIGINT COMMENT 'if setting_type is price and  have this value';

ALTER TABLE eh_energy_month_statistics ADD COLUMN `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费';
ALTER TABLE eh_energy_month_statistics ADD COLUMN `config_id` BIGINT COMMENT 'if setting_type is price and  have this value';

ALTER TABLE eh_energy_date_statistics ADD COLUMN `calculation_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: standing charge tariff 固定费用, 1: block tariff 阶梯收费';
ALTER TABLE eh_energy_date_statistics ADD COLUMN `config_id` BIGINT COMMENT 'if setting_type is price and  have this value';

-- 用户管理 add by sw
ALTER TABLE `eh_organization_members` ADD INDEX i_target_id (target_id);

-- add by janson 03-30
ALTER TABLE `eh_flow_cases` ADD COLUMN `title` VARCHAR(64) NULL AFTER `evaluate_score`;

-- add by sw 20170331

UPDATE eh_lease_promotions SET rent_areas = 190 where id = 51;
UPDATE eh_lease_promotions SET rent_areas = 90.44 where id = 52;
UPDATE eh_lease_promotions SET rent_areas = 400 where id = 280;

CREATE TABLE `eh_lease_issuers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `issuer_contact` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'For rent',
  `issuer_name` VARCHAR(128),
  `enterprise_id` BIGINT COMMENT 'enterprise id',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `status` TINYINT COMMENT '0: inactive, 2: active',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_lease_issuer_addresses` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `lease_issuer_id` BIGINT NOT NULL COMMENT 'eh_enterprise_op_requests id',
  `building_id` BIGINT COMMENT 'building id ',
  `address_id` BIGINT COMMENT 'address id ',
  `status` TINYINT,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_lease_promotions MODIFY COLUMN rent_areas DECIMAL(10,2);
ALTER TABLE eh_lease_promotions ADD COLUMN `enter_time_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled';
ALTER TABLE eh_lease_promotions ADD COLUMN `address_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_lease_promotions ADD COLUMN `orientation` VARCHAR(128);
ALTER TABLE eh_lease_promotions ADD COLUMN `rent_amount` DECIMAL(10,2);
ALTER TABLE eh_lease_promotions ADD COLUMN `issuer_type` VARCHAR(128) COMMENT '1: organization 2: normal_user';

ALTER TABLE eh_enterprise_op_requests ADD COLUMN `issuer_type` VARCHAR(128) COMMENT '1: organization 2: normal_user';
ALTER TABLE eh_enterprise_op_requests ADD COLUMN `building_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_enterprise_op_requests ADD COLUMN `address_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_enterprise_op_requests ADD COLUMN `flowcase_id` BIGINT NOT NULL DEFAULT 0;


CREATE TABLE `eh_lease_configs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `rent_amount_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `issuing_lease_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `issuer_manage_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `park_indroduce_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',
  `renew_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_buildings` ADD COLUMN `traffic_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `lift_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `pm_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `parking_lot_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `environmental_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `power_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `telecommunication_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `air_condition_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `security_description` text;
ALTER TABLE `eh_buildings` ADD COLUMN `fire_control_description` text;