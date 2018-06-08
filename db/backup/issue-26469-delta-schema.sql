CREATE TABLE `eh_service_alliance_providers` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '\'\'',
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0',
	`app_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'module_app id, new type of alliance, represent one kine fo alliance',
	`type` BIGINT(20) NOT NULL COMMENT 'old type of Alliance，represent one kind of alliance',
	`name` VARCHAR(50) NOT NULL COMMENT 'provider name',
	`category_id` BIGINT(20) NOT NULL COMMENT '见 categories表',
	`mail` VARCHAR(50) NOT NULL COMMENT 'enterprise mail',
	`contact_number` VARCHAR(50) NOT NULL COMMENT 'mobile or contact phone',
	`contact_name` VARCHAR(50) NOT NULL COMMENT 'contact name',
	`total_score` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'total score',
	`score_times` INT(11) NOT NULL DEFAULT '0' COMMENT 'the num of times make the score',
	`score_flow_case_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'the final flow case id that make score',
	`status` TINYINT(4) NOT NULL DEFAULT '1' COMMENT '0-deleted 1-active',
	`create_time` DATETIME NOT NULL,
	`create_uid` BIGINT(20) NOT NULL COMMENT 'create user id',
	PRIMARY KEY (`id`)
)
COMMENT='服务商信息'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `eh_alliance_extra_events` (
	`id` BIGINT(20) NOT NULL,
	`flow_case_id` BIGINT(20) NOT NULL,
	`topic` VARCHAR(200) NOT NULL COMMENT 'topic of current event',
	`time` DATETIME NOT NULL COMMENT 'the time that event happen',
	`address` VARCHAR(200) NULL DEFAULT NULL,
	`provider_id` BIGINT(20) NULL DEFAULT NULL COMMENT 'id of alliance_providers',
	`provider_name` VARCHAR(50) NULL DEFAULT NULL COMMENT 'name of alliance_provider',
	`members` VARCHAR(500) NOT NULL COMMENT 'those who participate in',
	`content` MEDIUMTEXT NOT NULL COMMENT 'main body',
	`enable_read` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '0-hide for applier  1-show for applier',
	`enable_notify_by_email` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '0-not send email  1-send email to provider',
	`create_time` DATETIME NOT NULL,
	`create_uid` BIGINT(20) NOT NULL,
	PRIMARY KEY (`id`)
)
COMMENT='工作流中，新建事件表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


CREATE TABLE `eh_alliance_extra_event_attachment` (
	`id` BIGINT(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL COMMENT 'the id of eh_alliance_extra_events',
	`file_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'like image,jpg. in lower case',
	`file_uri` VARCHAR(1024) NOT NULL COMMENT 'like cs://1/...',
	`file_name` VARCHAR(200) NULL DEFAULT NULL,
	`file_size` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'file size (Byte)',
	`create_uid` BIGINT(20) NOT NULL,
	`create_time` DATETIME NOT NULL COMMENT 'create time',
	PRIMARY KEY (`id`)
)
COMMENT='用于服务联盟工作流中新建事件时保存附件使用'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;


ALTER TABLE `eh_service_alliance_jump_module`
	ADD COLUMN `module_id` BIGINT NOT NULL DEFAULT '0' AFTER `module_name`;
	
	
ALTER TABLE `eh_service_alliance_jump_module`
	ADD COLUMN `instance_config` TEXT NULL DEFAULT NULL AFTER `module_url`;
