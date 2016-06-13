DROP TABLE IF EXISTS `eh_organization_addresses`;
CREATE TABLE `eh_organization_addresses` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_groups',
  `address_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_addresses',
  `status` TINYINT NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` BIGINT DEFAULT NULL COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` TINYINT DEFAULT NULL,
  `process_details` TEXT,
  `proof_resource_uri` VARCHAR(1024) DEFAULT NULL,
  `approve_time` DATETIME DEFAULT NULL COMMENT 'redundant auditing info',
  `update_time` DATETIME DEFAULT NULL,
  `building_id` BIGINT NOT NULL DEFAULT '0',
  `building_name` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_attachments`;
CREATE TABLE `eh_organization_attachments` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL DEFAULT '0',
  `content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_organization_community_requests`;
CREATE TABLE `eh_organization_community_requests` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `community_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_enterprise_communities',
  `member_type` VARCHAR(32) NOT NULL COMMENT 'enterprise',
  `member_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'enterprise_id etc',
  `member_status` TINYINT(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: waitingForAcceptance 3: active',
  `creator_uid` BIGINT DEFAULT NULL COMMENT 'record creator user id',
  `create_time` DATETIME DEFAULT NULL,
  `operator_uid` BIGINT DEFAULT NULL COMMENT 'redundant auditing info',
  `process_code` TINYINT(4) DEFAULT NULL,
  `process_details` TEXT,
  `proof_resource_uri` VARCHAR(1024) DEFAULT NULL,
  `approve_time` DATETIME DEFAULT NULL COMMENT 'redundant auditing info',
  `requestor_comment` TEXT,
  `operation_type` TINYINT(4) DEFAULT NULL COMMENT '1: request to join, 2: invite to join',
  `inviter_uid` BIGINT DEFAULT NULL COMMENT 'record inviter user id',
  `invite_time` DATETIME DEFAULT NULL COMMENT 'the time the member is invited',
  `update_time` DATETIME NOT NULL,
  `integral_tag1` BIGINT DEFAULT NULL,
  `integral_tag2` BIGINT DEFAULT NULL,
  `integral_tag3` BIGINT DEFAULT NULL,
  `integral_tag4` BIGINT DEFAULT NULL,
  `integral_tag5` BIGINT DEFAULT NULL,
  `string_tag1` VARCHAR(128) DEFAULT NULL,
  `string_tag2` VARCHAR(128) DEFAULT NULL,
  `string_tag3` VARCHAR(128) DEFAULT NULL,
  `string_tag4` VARCHAR(128) DEFAULT NULL,
  `string_tag5` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



DROP TABLE IF EXISTS `eh_organization_details`;
CREATE TABLE `eh_organization_details` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `organization_id` BIGINT NOT NULL COMMENT 'group id',
  `description` TEXT COMMENT 'description',
  `contact` VARCHAR(128) DEFAULT NULL COMMENT 'the phone number',
  `address` VARCHAR(256) DEFAULT NULL COMMENT 'address str',
  `create_time` DATETIME DEFAULT NULL,
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `geohash` VARCHAR(32) DEFAULT NULL,
  `display_name` VARCHAR(64) DEFAULT NULL,
  `contactor` VARCHAR(64) DEFAULT NULL,
  `member_count` BIGINT DEFAULT '0',
  `checkin_date` DATETIME DEFAULT NULL COMMENT 'checkin date',
  `avatar` VARCHAR(128) DEFAULT NULL,
  `post_uri` VARCHAR(128) DEFAULT NULL,
  `integral_tag1` BIGINT DEFAULT NULL,
  `integral_tag2` BIGINT DEFAULT NULL,
  `integral_tag3` BIGINT DEFAULT NULL,
  `integral_tag4` BIGINT DEFAULT NULL,
  `integral_tag5` BIGINT DEFAULT NULL,
  `string_tag1` VARCHAR(128) DEFAULT NULL,
  `string_tag2` VARCHAR(128) DEFAULT NULL,
  `string_tag3` VARCHAR(128) DEFAULT NULL,
  `string_tag4` VARCHAR(128) DEFAULT NULL,
  `string_tag5` VARCHAR(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


#
# 
# Park activity rules parameter
#
DROP TABLE IF EXISTS `eh_preferential_rules`;
CREATE TABLE `eh_preferential_rules` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(128) DEFAULT NULL COMMENT 'community',
  `owner_id` BIGINT NOT NULL ,
  `start_time` DATETIME COMMENT 'start time',
  `end_time` DATETIME COMMENT 'end time',
  `type` VARCHAR(256) DEFAULT NULL COMMENT 'PARKING',
  `before_nember` BIGINT DEFAULT NULL,
  `params_json` TEXT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;




