

-- 通用脚本
-- ADD BY 黄明波
-- issue-29989
-- 服务联盟v3.4 服务新增客服会话表 add by huangmingbo 2018.07.03
CREATE TABLE `eh_alliance_online_service` (
	`id` BIGINT(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'id of eh_service_alliances',
	`user_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'id of eh_users',
	`user_name` VARCHAR(64) NOT NULL DEFAULT '""' COMMENT 'organization_members\'s contact name',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0-inactive 1-active  currently not used',
	`update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'last update time',
	PRIMARY KEY (`id`),
	UNIQUE INDEX `u_eh_owner_user` (`owner_id`, `user_id`)
)
COMMENT='服务联盟客服表，新增服务时会指派客服专员。这个表保存服务添加过的客服信息。'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

ALTER TABLE `eh_service_alliance_attachments` CHANGE COLUMN `attachment_type` `attachment_type` TINYINT(4) NULL DEFAULT '0' COMMENT '0: banner; 1: file attachment; 2: cover' ;
ALTER TABLE `eh_service_alliance_attachments` ADD COLUMN `default_order` TINYINT NOT NULL DEFAULT '0' COMMENT 'the order of image; the smaller the toper;0,1,2,3,...' ;
ALTER TABLE `eh_service_alliance_attachments` ADD COLUMN `skip_url` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'the url to skip' ;

ALTER TABLE `eh_service_alliances` ADD COLUMN `start_time` DATETIME NULL DEFAULT NULL COMMENT 'for policydeclare ; start time of the policy' ;
ALTER TABLE `eh_service_alliances` ADD COLUMN `end_time` DATETIME NULL DEFAULT NULL COMMENT 'for policydeclare ; end time of the policy' ;
ALTER TABLE `eh_service_alliances` ADD INDEX `i_eh_default_order` (`default_order`);

CREATE TABLE `eh_alliance_tag` (
	`id` BIGINT(20) NOT NULL,
	`namespace_id` INT(11) NOT NULL DEFAULT '0',
	`parent_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT '0-parent node , others-child node',
	`value` VARCHAR(32) NOT NULL DEFAULT '""' COMMENT 'tag name',
	`type` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'type of service alliances ',
	`is_default` TINYINT(8) NOT NULL DEFAULT '0' COMMENT 'default chosen',
	`default_order` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'show order; the smaller the toper;like 0,1,2',
	`create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`create_uid` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'user_id of creater',
	`delete_flag` TINYINT(8) NOT NULL DEFAULT '0' COMMENT '0-active 1-deleted',
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;

CREATE TABLE `eh_alliance_tag_vals` (
	`id` BIGINT(20) NOT NULL,
	`owner_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'id of service alliance',
	`tag_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'id of eh_alliance_tag',
	`tag_parent_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'parent id of eh_alliance_tag',
	PRIMARY KEY (`id`)
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB;
-- END
