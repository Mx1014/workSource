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