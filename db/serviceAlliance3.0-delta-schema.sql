CREATE TABLE `eh_user_impersonations`(
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'USER',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_type` VARCHAR(64) NOT NULL COMMENT 'USER',
  `target_id` BIGINT NOT NULL DEFAULT 0,
  `description` TEXT,
  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '0: inactive, 1: active',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_service_alliance_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_service_alliances` (
  `id` BIGINT NOT NULL,
  `parent_id` BIGINT NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community;group,organaization,exhibition,',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'organization name',
  `display_name` VARCHAR(128) NOT NULL DEFAULT '',
  `type` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id reference to eh_service_alliance_categories',
  `address` VARCHAR(255) NOT NULL DEFAULT '',
  `contact` VARCHAR(64) ,
  `description` text,
  `poster_uri` VARCHAR(128) ,
  `status` tinyint(4) NOT NULL DEFAULT '2' COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `default_order` int(11) ,
  `longitude` double ,
  `latitude` double ,
  `geohash` VARCHAR(32) ,
  `discount` BIGINT ,
  `category_id` BIGINT ,
  `contact_name` VARCHAR(128) ,
  `contact_mobile` VARCHAR(128) ,
  `service_type` VARCHAR(128) ,
  `service_url` VARCHAR(128) ,
  `discount_desc` VARCHAR(128) ,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128) DEFAULT NULL,
  `string_tag2` VARCHAR(128) DEFAULT NULL,
  `string_tag3` VARCHAR(128) DEFAULT NULL,
  `string_tag4` VARCHAR(128) DEFAULT NULL,
  `string_tag5` VARCHAR(128) DEFAULT NULL,
  `creator_uid` BIGINT ,
  `create_time` datetime ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_service_alliance_attachments` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'the id reference to eh_service_alliances',
  `content_type` VARCHAR(32)  COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024)  COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT '0',
  `create_time` datetime ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

