RENAME TABLE eh_archives_forms TO eh_archives_form_navigation;

CREATE TABLE `eh_archives_forms` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`form_name` VARCHAR(64) NOT NULL COMMENT 'name of the form',
	`static_fields` TEXT NOT NULL  COMMENT 'static form fields in json format',
	`dynamic_fields` TEXT COMMENT 'dynamic form fields in json format',
	`status` TINYINT NOT NULL DEFAULT 1 COMMENT '0. inactive, 1.active',
	`operator_uid` BIGINT,
	`operator_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_archives_form_vals`(
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`form_id` BIGINT COMMENT 'the id of the archives form',
	`field_name` VARCHAR(128) COMMENT 'the name of the field',
	`field_type` VARCHAR(128) COMMENT 'the type of the field',
	`field_value` TEXT COMMENT 'the value of the field',
	`form_name` VARCHAR(64) NOT NULL COMMENT 'name of the form',
	`static_fields` NOT NULL TEXT COMMENT 'static form fields in json format',
	`dynamic_fields` TEXT COMMENT 'dynamic form fields in json format',
	`create_time` DATETIME,
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;