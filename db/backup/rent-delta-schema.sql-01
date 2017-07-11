ALTER TABLE `eh_lease_configs` ADD COLUMN `display_name_str` VARCHAR(128);
ALTER TABLE `eh_lease_configs` ADD COLUMN `display_order_str` VARCHAR(128);


ALTER TABLE `eh_buildings` ADD COLUMN `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form';
ALTER TABLE `eh_buildings` ADD COLUMN `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field';
ALTER TABLE `eh_buildings` ADD COLUMN `default_order` BIGINT NOT NULL;
ALTER TABLE `eh_buildings` ADD COLUMN `manager_name` VARCHAR(128);


ALTER TABLE `eh_lease_promotions` ADD COLUMN `longitude` DOUBLE DEFAULT NULL;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `latitude` DOUBLE DEFAULT NULL;
ALTER TABLE `eh_lease_promotions` ADD COLUMN `address` VARCHAR(512);

ALTER TABLE `eh_lease_promotions` ADD COLUMN `general_form_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'id of eh_general_form';
ALTER TABLE `eh_lease_promotions` ADD COLUMN `custom_form_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not add custom field, 1: add custom field';
ALTER TABLE `eh_lease_promotions` ADD COLUMN `default_order` BIGINT NOT NULL DEFAULT 0;

CREATE TABLE `eh_lease_form_requests` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_id` BIGINT NOT NULL,
	`owner_type` VARCHAR (64) NOT NULL,

    `source_id` BIGINT NOT NULL,
	`source_type` VARCHAR (64) NOT NULL,

	`create_time` datetime DEFAULT NULL COMMENT 'record create time',

	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `eh_lease_configs2` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',  
  `config_name` VARCHAR(128),
  `config_value` VARCHAR(128),  

  `create_time` DATETIME,
  `creator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_general_form_mappings` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_id` BIGINT NOT NULL,
	`owner_type` VARCHAR (64) NOT NULL,

    `source_id` BIGINT NOT NULL,
	`source_type` VARCHAR (64) NOT NULL,
	`form_origin_id` BIGINT DEFAULT NULL,
	`form_version` BIGINT DEFAULT NULL,
	`create_time` datetime DEFAULT NULL COMMENT 'record create time',

	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

CREATE TABLE `eh_general_form_vals` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`namespace_id` INT NOT NULL DEFAULT '0',
	`organization_id` BIGINT NOT NULL DEFAULT '0',
	`owner_id` BIGINT NOT NULL,
	`owner_type` VARCHAR (64) NOT NULL,
	`module_id` BIGINT DEFAULT NULL COMMENT 'the module id',
	`module_type` VARCHAR (64) DEFAULT NULL,

    `source_id` BIGINT NOT NULL,
	`source_type` VARCHAR (64) NOT NULL,

	`form_origin_id` BIGINT DEFAULT NULL,
	`form_version` BIGINT DEFAULT NULL,
	`field_name` VARCHAR (128) DEFAULT NULL,
	`field_type` VARCHAR (128) DEFAULT NULL,
	`field_value` text,
	`create_time` datetime DEFAULT NULL COMMENT 'record create time',

    `string_tag1` VARCHAR (128) DEFAULT NULL,
	`string_tag2` VARCHAR (128) DEFAULT NULL,
	`string_tag3` VARCHAR (128) DEFAULT NULL,
	`string_tag4` VARCHAR (128) DEFAULT NULL,
	`string_tag5` VARCHAR (128) DEFAULT NULL,
	`integral_tag1` BIGINT (20) DEFAULT '0',
	`integral_tag2` BIGINT (20) DEFAULT '0',
	`integral_tag3` BIGINT (20) DEFAULT '0',
	`integral_tag4` BIGINT (20) DEFAULT '0',
	`integral_tag5` BIGINT (20) DEFAULT '0',
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;
