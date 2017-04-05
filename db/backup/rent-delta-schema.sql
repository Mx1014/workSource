-- 物业报修2.8 add by sw 20170306
ALTER TABLE eh_pm_tasks ADD COLUMN `building_name` VARCHAR(128);
ALTER TABLE eh_pm_tasks ADD COLUMN `organization_uid` BIGINT;

ALTER TABLE eh_pm_task_targets ADD COLUMN `create_time` DATETIME;
ALTER TABLE eh_pm_task_targets ADD COLUMN `creator_uid` BIGINT;

-- 修改dataType 长度 add by sw 20170306
ALTER TABLE eh_web_menus MODIFY COLUMN data_type VARCHAR (256);


-- 活动报名表添加活动报名信息等字段, add by tt, 20170227
ALTER TABLE `eh_activities` ADD COLUMN `signup_end_time` DATETIME;
ALTER TABLE `eh_activity_roster` ADD COLUMN `phone` VARCHAR(32);
ALTER TABLE `eh_activity_roster` ADD COLUMN `real_name` VARCHAR(128);
ALTER TABLE `eh_activity_roster` ADD COLUMN `gender` TINYINT;
ALTER TABLE `eh_activity_roster` ADD COLUMN `community_name` VARCHAR(64);
ALTER TABLE `eh_activity_roster` ADD COLUMN `organization_name` VARCHAR(128);
ALTER TABLE `eh_activity_roster` ADD COLUMN `position` VARCHAR(64);
ALTER TABLE `eh_activity_roster` ADD COLUMN `leader_flag` TINYINT;
ALTER TABLE `eh_activity_roster` ADD COLUMN `source_flag` TINYINT;

ALTER TABLE `eh_activity_roster` DROP INDEX `u_eh_act_roster_user`;

CREATE TABLE `eh_docking_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',  
  `scope` VARCHAR(64) NOT NULL,
  `name` VARCHAR(256),
  `mapping_value` VARCHAR(256),
  `mapping_json` VARCHAR(1024),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
