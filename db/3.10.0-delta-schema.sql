-- DROP TABLE IF EXISTS `eh_search_types`;
CREATE TABLE `eh_search_types` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the type, community, enterprise, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(128) NOT NULL DEFAULT '',
  `content_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'search content type',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active', 
  `create_time` DATETIME,
  `delete_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 园区快讯的建表语句 by wh 2016-9-21
--
-- DROP TABLE IF EXISTS `eh_news_categories`;
CREATE TABLE `eh_news_categories` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `name` VARCHAR(64) NOT NULL,
  `path` VARCHAR(128),
  `default_order` INTEGER,
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 给园区快讯增加类型字段 by wh 2016-9-21
ALTER TABLE `eh_news` ADD COLUMN `category_id` BIGINT(20) NOT NULL DEFAULT '0' COMMENT 'category id';

-- merge from sa4.0 by xiongying
-- 保存用户申请模板(通用，不仅限于服务联盟)
DROP TABLE IF EXISTS `eh_request_templates`;
CREATE TABLE `eh_request_templates` (
  `id` BIGINT NOT NULL,
  `template_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'i.e. EhServiceAllianceApplies type',
  `name` VARCHAR(128) NOT NULL,
  `button_title` VARCHAR(128) NOT NULL,
  `email_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: no, 1: yes',
  `msg_flag` TINYINT NOT NULL DEFAULT '0' COMMENT '0: no, 1: yes',
  `fields_json` TEXT,
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `create_time` DATETIME,
  `delete_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record deleter user id',
  `delete_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 模板和域空间映射表 没配的域空间表示支持所有模板 配了的则仅支持配了的部分
DROP TABLE IF EXISTS `eh_request_templates_namespace_mapping`;
CREATE TABLE `eh_request_templates_namespace_mapping` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `template_id` BIGINT NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 申请附件信息(通用，所有用模板进行申请带有的附件都放入此表)
DROP TABLE IF EXISTS `eh_request_attachments`;
CREATE TABLE `eh_request_attachments` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'owner resource(i.e. EhServiceAllianceApplies) type',
  `owner_id` BIGINT NOT NULL DEFAULT 0,
  `target_field_name` VARCHAR(128) NOT NULL DEFAULT '',
  `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- 服务联盟模板申请信息
DROP TABLE IF EXISTS `eh_service_alliance_requests`;
CREATE TABLE `eh_service_alliance_requests` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `type` BIGINT NOT NULL DEFAULT '0',
  `category_id` BIGINT NOT NULL DEFAULT '0',
  `creator_uid` BIGINT NOT NULL DEFAULT '0' COMMENT 'record creator user id',
  `creator_name` VARCHAR(128) NOT NULL,
  `creator_mobile` VARCHAR(128) NOT NULL,
  `creator_organization_id` BIGINT NOT NULL DEFAULT '0',
  `service_alliance_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(128),
  `mobile` VARCHAR(128),
  `organization_name` VARCHAR(128),
  `city_name` VARCHAR(128),
  `industry` VARCHAR(128),
  `financing_stage` VARCHAR(32),
  `financing_amount` DECIMAL(10,2) DEFAULT NULL,
  `transfer_shares` DOUBLE DEFAULT NULL,
  `project_desc` TEXT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 保存服务联盟大类下设置的推送邮箱和推送消息的管理员信息
DROP TABLE IF EXISTS `eh_service_alliance_notify_targets`;
CREATE TABLE `eh_service_alliance_notify_targets` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, community, etc',
  `owner_id` BIGINT NOT NULL DEFAULT '0',
  `category_id` BIGINT NOT NULL DEFAULT '0',
  `name` VARCHAR(128) NOT NULL,  
  `contact_type` TINYINT NOT NULL DEFAULT '0' COMMENT '0: mobile, 1: email',
  `contact_token` VARCHAR(128) DEFAULT NULL COMMENT 'phone number or email address',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: active',
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `logo_url` VARCHAR(1024) COMMENT 'the logo url of the category';
