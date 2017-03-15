--
-- 梯度价格表
--
CREATE TABLE `eh_energy_meter_price_config` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  
  `name`         VARCHAR(255) COMMENT 'formula name',
  `explain`      VARCHAR(512) COMMENT 'explain formula',
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
ALTER TABLE eh_energy_meter_formulas ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0; 
ALTER TABLE eh_energy_meter_formulas ADD COLUMN `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc';
ALTER TABLE eh_energy_meter_formulas ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_energy_meter_categories ADD COLUMN `community_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_energy_meter_categories ADD COLUMN `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the price formulas, enterprise, etc';
ALTER TABLE eh_energy_meter_categories ADD COLUMN `owner_id` BIGINT NOT NULL DEFAULT 0;