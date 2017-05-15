
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

ALTER TABLE `eh_buildings` ADD COLUMN `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form';
ALTER TABLE `eh_buildings` ADD COLUMN `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field';

ALTER TABLE `eh_lease_promotions` ADD COLUMN `longitude` DOUBLE DEFAULT NULL;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `latitude` DOUBLE DEFAULT NULL;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form';
ALTER TABLE `eh_lease_promotions` ADD COLUMN `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field';

CREATE TABLE `eh_lease_configs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',  
  `config_name` VARCHAR(128),
  `config_value` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: enabled',  
  `title` VARCHAR(128),
  
  `create_time` DATETIME,
  `creator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;