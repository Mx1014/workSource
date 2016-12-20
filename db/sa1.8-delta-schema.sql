ALTER TABLE `eh_service_alliance_apartment_requests` ADD COLUMN `template_type` VARCHAR(128) NOT NULL DEFAULT '';
ALTER TABLE `eh_settle_requests` ADD COLUMN `template_type` VARCHAR(128) NOT NULL DEFAULT '';
ALTER TABLE `eh_service_alliance_requests` ADD COLUMN `template_type` VARCHAR(128) NOT NULL DEFAULT '';
ALTER TABLE `eh_service_alliance_requests` ADD COLUMN `remarks` VARCHAR(1024);
ALTER TABLE `eh_settle_requests` ADD COLUMN `remarks` VARCHAR(1024);
ALTER TABLE `eh_settle_requests` ADD COLUMN `string_tag1` VARCHAR(256);
ALTER TABLE `eh_settle_requests` ADD COLUMN `integral_tag1` BIGINT;


DROP TABLE IF EXISTS `eh_service_alliance_invest_requests`;
CREATE TABLE `eh_service_alliance_invest_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '',
  `type` BIGINT NOT NULL DEFAULT 0,
  `category_id` BIGINT NOT NULL DEFAULT 0,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `creator_name` VARCHAR(128),
  `creator_mobile` VARCHAR(128),
  `creator_organization_id` BIGINT NOT NULL DEFAULT 0,
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `industry` VARCHAR(128),
  `financing_amount` DECIMAL(10,2),
  `invest_period` INTEGER,
  `annual_yield` DOUBLE,
  `remarks` VARCHAR(1024),
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `display_destination` TINYINT DEFAULT '0' COMMENT '0: both, 1: client only, 2: browner only';

CREATE TABLE `eh_service_alliance_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `service_alliance_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner id',
  `name` VARCHAR(128),
  `file_size` INTEGER NOT NULL DEFAULT '0',
  `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0',
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
