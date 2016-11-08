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
-- DROP TABLE IF EXISTS `eh_request_templates`;
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 模板和域空间映射表 没配的域空间表示支持所有模板 配了的则仅支持配了的部分
-- DROP TABLE IF EXISTS `eh_request_templates_namespace_mapping`;
CREATE TABLE `eh_request_templates_namespace_mapping` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT '0',
  `template_id` BIGINT NOT NULL,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 申请附件信息(通用，所有用模板进行申请带有的附件都放入此表)
-- DROP TABLE IF EXISTS `eh_request_attachments`;
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 服务联盟模板申请信息
-- DROP TABLE IF EXISTS `eh_service_alliance_requests`;
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 保存服务联盟大类下设置的推送邮箱和推送消息的管理员信息
-- DROP TABLE IF EXISTS `eh_service_alliance_notify_targets`;
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
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_service_alliance_categories` ADD COLUMN `logo_url` VARCHAR(1024) COMMENT 'the logo url of the category';

-- 审批流程表
-- DROP TABLE IF EXISTS `eh_approval_flows`;
CREATE TABLE `eh_approval_flows` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`name` VARCHAR(64) NOT NULL COMMENT 'name of flow',
	`status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
	`update_time` DATETIME,
	`operator_uid` BIGINT, 
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 审批流程对应每级的人/角色表
-- DROP TABLE IF EXISTS  `eh_approval_flow_levels`;
CREATE TABLE `eh_approval_flow_levels` (
	`id` BIGINT NOT NULL,
	`flow_id` BIGINT NOT NULL COMMENT 'id of flow',
	`level` TINYINT NOT NULL COMMENT '1,2,3,4,5...',
	`target_type` TINYINT NOT NULL COMMENT '1. user, 2. role',
	`target_id` BIGINT NOT NULL COMMENT 'id of target, e.g id of user', 
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 审批规则表
-- DROP TABLE IF EXISTS  `eh_approval_rules`;
CREATE TABLE `eh_approval_rules` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`name` VARCHAR(64) NOT NULL COMMENT 'name of approval rule',
	`status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
	`update_time` DATETIME,
	`operator_uid` BIGINT, 
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 审批规则与流程关联表
-- DROP TABLE IF EXISTS  `eh_approval_rule_flow_map`;
CREATE TABLE `eh_approval_rule_flow_map` (
	`id` BIGINT NOT NULL,
	`rule_id` BIGINT NOT NULL COMMENT 'id of rule',
	`approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
	`flow_id` BIGINT NOT NULL COMMENT 'id of flow', 
	`status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 审批具体类别表
-- DROP TABLE IF EXISTS  `eh_approval_categories`;
CREATE TABLE `eh_approval_categories` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
	`category_name` VARCHAR(64) NOT NULL COMMENT 'name of category',
	`status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`creator_uid` BIGINT,
	`create_time` DATETIME, 
	`update_time` DATETIME,
	`operator_uid` BIGINT, 
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 申请记录表
-- DROP TABLE IF EXISTS  `eh_approval_requests`;
CREATE TABLE `eh_approval_requests` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
	`category_id` BIGINT COMMENT 'concrete category id',
	`reason` VARCHAR(256) COMMENT 'approval reason',
	`content_json` LONGTEXT COMMENT 'json of concrete category content',
	`attachment_flag` TINYINT NOT NULL DEFAULT '0' COMMENT 'if there are attachments, 0. no, 1. yes',
	`time_flag` TINYINT NOT NULL DEFAULT '0' COMMENT 'if there are time ranges, 0. no, 1. yes',
	`flow_id` BIGINT COMMENT 'id of flow',
	`current_level` TINYINT COMMENT 'current level of flow',
	`next_level` TINYINT COMMENT 'next level of flow',
	`approval_status` TINYINT NOT NULL COMMENT '0. waitingForApproving, 1. agreement, 2. rejection',
	`status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`long_tag1` BIGINT COMMENT 'put some condition here',
	`string_tag1` VARCHAR(256) COMMENT 'put some condition here',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
	`update_time` DATETIME,
	`operator_uid` BIGINT, 
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 审批附件表
-- DROP TABLE IF EXISTS  `eh_approval_attachments`;
CREATE TABLE `eh_approval_attachments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
	`content_type` VARCHAR(32) COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
	`creator_uid` BIGINT NOT NULL,
	`create_time` DATETIME NOT NULL, 
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 审批时间表（请假时间表）
-- DROP TABLE IF EXISTS  `eh_approval_time_ranges`;
CREATE TABLE `eh_approval_time_ranges` (
	`id` BIGINT NOT NULL,
	`owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g application_id',
	`from_time` DATETIME NOT NULL COMMENT 'must store concrete time',
	`end_time` DATETIME NOT NULL COMMENT 'must store concrete time',
	`type` TINYINT NOT NULL COMMENT '1. all day, 2. morning half day, 3. afternoon half day, 4. time',
	`actual_result` VARCHAR(128) COMMENT 'actual result, e.g 1day3hours',
	`creator_uid` BIGINT NOT NULL,
	`create_time` DATETIME NOT NULL,
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 申请时间具体到每一天的实际时长
-- DROP TABLE IF EXISTS  `eh_approval_day_actual_time`;
CREATE TABLE `eh_approval_day_actual_time` (
	`id` BIGINT NOT NULL,
	`owner_id` BIGINT NOT NULL COMMENT 'owner id, e.g request_id',
	`user_id`  BIGINT NOT NULL,
	`time_date` DATE NOT NULL COMMENT 'concrete date',
	`actual_result` VARCHAR(128) COMMENT 'actual result, e.g 1day3hours',
	`creator_uid` BIGINT NOT NULL,
	`create_time` DATETIME NOT NULL,
	
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 申请处理日志表
-- DROP TABLE IF EXISTS  `eh_approval_op_requests`;
CREATE TABLE `eh_approval_op_requests` (
	`id` BIGINT NOT NULL,
	`request_id` BIGINT NOT NULL COMMENT 'id of request',
	`requestor_comment` TEXT COMMENT 'comment of reqeust',
	`process_message` TEXT COMMENT 'process message',
	`flow_id` BIGINT COMMENT 'id of flow',
	`level` TINYINT COMMENT 'which level approved',
	`operator_uid` BIGINT,
	`create_time` DATETIME,
	`approval_status` TINYINT NOT NULL COMMENT '0. waitingForApproving, 1. agreement, 2. rejection',
	PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 原打卡异常申请表增加一个字段用于存储审批申请的id，便于审批那边可以找到
ALTER TABLE `eh_punch_exception_requests`	ADD COLUMN `request_id` BIGINT NULL COMMENT 'approval request id';

--
-- 给eh_organization_owners增加一些客户资料管理增加的字段       by xq.tian
--
ALTER TABLE `eh_organization_owners` ADD COLUMN `registered_residence` VARCHAR(128) COMMENT 'registered residence';
ALTER TABLE `eh_organization_owners` ADD COLUMN `org_owner_type_id` BIGINT COMMENT 'owner type id';
ALTER TABLE `eh_organization_owners` ADD COLUMN `gender` TINYINT COMMENT 'male, female';
ALTER TABLE `eh_organization_owners` ADD COLUMN `birthday` DATE COMMENT 'birthday';
ALTER TABLE `eh_organization_owners` ADD COLUMN `marital_status` VARCHAR(10);
ALTER TABLE `eh_organization_owners` ADD COLUMN `job` VARCHAR(10) COMMENT 'job';
ALTER TABLE `eh_organization_owners` ADD COLUMN `company` VARCHAR(100) COMMENT 'company';
ALTER TABLE `eh_organization_owners` ADD COLUMN `id_card_number` VARCHAR(18) COMMENT 'id card number';
ALTER TABLE `eh_organization_owners` ADD COLUMN `avatar` VARCHAR(1024) COMMENT 'avatar';
ALTER TABLE `eh_organization_owners` ADD COLUMN `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'delete: 0, normal: 1';
ALTER TABLE `eh_organization_owners`  MODIFY COLUMN `address_id`  BIGINT(20) NULL COMMENT 'address id';

--
-- 创建eh_organization_owner_cars表,汽车管理的汽车表    by xq.tian
--
-- DROP TABLE IF EXISTS `eh_organization_owner_cars`;
CREATE TABLE `eh_organization_owner_cars` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `brand` VARCHAR(20),
  `parking_space` VARCHAR(20),
  `parking_type` TINYINT,
  `plate_number` VARCHAR(20),
  `contacts` VARCHAR(20),
  `contact_number` VARCHAR(20),
  `content_uri` VARCHAR(1024),
  `color` VARCHAR(20),
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 创建eh_organization_owner与eh_address的多对多表    by xq.tian
--
-- DROP TABLE IF EXISTS `eh_organization_owner_address`;
CREATE TABLE `eh_organization_owner_address` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `address_id` BIGINT,
  `living_status` TINYINT,
  `auth_type` TINYINT COMMENT 'Auth type',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 创建eh_organization_owner_owner_car与eh_organization_owner_cars的多对多表    by xq.tian
--
-- DROP TABLE IF EXISTS `eh_organization_owner_owner_car`;
CREATE TABLE `eh_organization_owner_owner_car` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `car_id` BIGINT,
  `primary_flag` TINYINT COMMENT 'primary flag, yes: 1, no: 0',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 客户资料管理中的附件上传记录表    by xq.tian
--
-- DROP TABLE IF EXISTS `eh_organization_owner_attachments`;
CREATE TABLE `eh_organization_owner_attachments` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 车辆管理中的附件上传记录表    by xq.tian
--
-- DROP TABLE IF EXISTS `eh_organization_owner_car_attachments`;
CREATE TABLE `eh_organization_owner_car_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'car id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 客户的活动记录表   by xq.tian
--
-- DROP TABLE IF EXISTS `eh_organization_owner_behaviors`;
CREATE TABLE `eh_organization_owner_behaviors` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `address_id` BIGINT COMMENT 'address id',
  `behavior_type` VARCHAR(20) COMMENT 'immigration, emigration..',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `behavior_time` DATETIME,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 客户类型表    by xq.tian
--
-- DROP TABLE IF EXISTS `eh_organization_owner_type`;
CREATE TABLE `eh_organization_owner_type` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(20) COMMENT 'owner, tenant, relative, friend',
  `display_name` VARCHAR(20) COMMENT 'display name',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- DROP TABLE IF EXISTS `eh_activity_video`;
CREATE TABLE `eh_activity_video` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `video_state` TINYINT NOT NULL DEFAULT 0 COMMENT '0:UN_READY, 1:DEBUG, 2:LIVE, 3:RECORDING, 4:EXCEPTION, 5:INVALID',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'activity',
  `owner_id` BIGINT NOT NULL DEFAULT 0 ,
  `creator_uid` BIGINT NOT NULL DEFAULT 0 ,
  `start_time` BIGINT NOT NULL DEFAULT 0,
  `end_time` BIGINT NOT NULL DEFAULT 0,
  `room_type` VARCHAR(64),
  `room_id` VARCHAR(64),
  `video_sid` VARCHAR(64),
  `manufacturer_type` VARCHAR(64) COMMENT 'YZB',
  `extra` TEXT,
  `integral_tag1` BIGINT,
  `integral_tag2` BIGINT,
  `integral_tag3` BIGINT,
  `integral_tag4` BIGINT,
  `integral_tag5` BIGINT,
  `string_tag1` VARCHAR(128),
  `string_tag2` VARCHAR(128),
  `string_tag3` VARCHAR(128),
  `string_tag4` VARCHAR(128),
  `string_tag5` VARCHAR(128),
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_activities` ADD COLUMN `video_url` VARCHAR(128) COMMENT 'url of video support' AFTER `official_flag`;
ALTER TABLE `eh_activities` ADD COLUMN `is_video_support` TINYINT NOT NULL DEFAULT 0 COMMENT 'is video support' AFTER `video_url`;

-- DROP TABLE IF EXISTS `eh_yzb_devices`;
CREATE TABLE `eh_yzb_devices` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `device_id` VARCHAR(64) NOT NULL COMMENT 'device_id of yzb',
  `room_id` VARCHAR(64) NOT NULL COMMENT 'room_id of this devices',
  `relative_id` BIGINT NOT NULL COMMENT 'activity_id',
  `relative_type` VARCHAR(64) NOT NULL DEFAULT "activity",
  `last_vid` VARCHAR(64) COMMENT 'the last vid',
  `state` TINYINT NOT NULL DEFAULT 0 COMMENT 'the current state of this devices',
  `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'INVALID, VALID',
  `create_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- add by xiongying20160926
-- 入驻申请信息
-- DROP TABLE IF EXISTS `eh_settle_requests`;
CREATE TABLE `eh_settle_requests` (
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
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 给打卡时间设置增加一天临界时间 by wh 2016-9-22
ALTER TABLE `eh_punch_time_rules` ADD COLUMN `day_split_time` TIME DEFAULT '05:00:00' COMMENT 'the time a day begin';

-- 给快讯类型增加默认封面图
ALTER TABLE `eh_news_categories` ADD COLUMN `logo_uri` VARCHAR(1024) COMMENT 'default cover uri';

-- 加默认值 by xiongying20160928
ALTER TABLE eh_service_alliance_requests MODIFY creator_name VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_service_alliance_requests MODIFY creator_mobile VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_settle_requests MODIFY creator_name VARCHAR(128) DEFAULT NULL;
ALTER TABLE eh_settle_requests MODIFY creator_mobile VARCHAR(128) DEFAULT NULL;


-- 从第三份文件合并过来，20161012

-- add by wh 20161011
-- 用户活动率计算表
-- DROP TABLE IF EXISTS `eh_stat_active_users`;
CREATE TABLE `eh_stat_active_users` (
  `id` BIGINT NOT NULL,
  `stat_date` DATE COMMENT '统计日期',
  `namespace_id` INTEGER NOT NULL DEFAULT '0', 
  `active_count` INTEGER NOT NULL DEFAULT '0' COMMENT '活动人数',
  `total_count` INTEGER NOT NULL DEFAULT '0' COMMENT '总人数-当天的', 
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

