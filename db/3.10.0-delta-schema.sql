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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批流程对应每级的人/角色表
-- DROP TABLE IF EXISTS  `eh_approval_flow_levels`;
CREATE TABLE `eh_approval_flow_levels` (
	`id` BIGINT NOT NULL,
	`flow_id` BIGINT NOT NULL COMMENT 'id of flow',
	`level` TINYINT NOT NULL COMMENT '1,2,3,4,5...',
	`target_type` TINYINT NOT NULL COMMENT '1. user, 2. role',
	`target_id` BIGINT NOT NULL COMMENT 'id of target, e.g id of user', 
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批规则与流程关联表
-- DROP TABLE IF EXISTS  `eh_approval_rule_flow_map`;
CREATE TABLE `eh_approval_rule_flow_map` (
	`id` BIGINT NOT NULL,
	`rule_id` BIGINT NOT NULL COMMENT 'id of rule',
	`approval_type` TINYINT NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
	`flow_id` BIGINT NOT NULL COMMENT 'id of flow', 
	`status` TINYINT NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 原打卡异常申请表增加一个字段用于存储审批申请的id，便于审批那边可以找到
ALTER TABLE `eh_punch_exception_requests`	ADD COLUMN `request_id` BIGINT NULL COMMENT 'approval request id';

--
-- 给eh_organization_owners增加一些客户资料管理增加的字段       by xq.tian
--
ALTER TABLE `eh_organization_owners` ADD COLUMN `registered_residence` VARCHAR(128) COMMENT 'registered residence';
ALTER TABLE `eh_organization_owners` ADD COLUMN `org_owner_type_id` BIGINT COMMENT '业主类型id';
ALTER TABLE `eh_organization_owners` ADD COLUMN `gender` TINYINT COMMENT '男,女';
ALTER TABLE `eh_organization_owners` ADD COLUMN `birthday` DATE COMMENT 'birthday';
ALTER TABLE `eh_organization_owners` ADD COLUMN `marital_status` VARCHAR(10);
ALTER TABLE `eh_organization_owners` ADD COLUMN `job` VARCHAR(10) COMMENT 'job';
ALTER TABLE `eh_organization_owners` ADD COLUMN `company` VARCHAR(100) COMMENT 'company';
ALTER TABLE `eh_organization_owners` ADD COLUMN `id_card_number` VARCHAR(18) COMMENT 'id card number';
ALTER TABLE `eh_organization_owners` ADD COLUMN `avatar` VARCHAR(128) COMMENT 'avatar';
ALTER TABLE `eh_organization_owners` ADD COLUMN `status` TINYINT COMMENT 'delete: 0, normal: 1';
ALTER TABLE `eh_organization_owners`  MODIFY COLUMN `address_id`  bigint(20) NULL COMMENT 'address id';

--
-- 创建eh_organization_owner_cars表,汽车管理的汽车表    by xq.tian
--
CREATE TABLE `eh_organization_owner_cars` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `community_id` BIGINT,
  `brand` VARCHAR(20) COMMENT '汽车品牌',
  `parking_space` VARCHAR(20) COMMENT '停车位',
  `parking_type` TINYINT COMMENT '停车类型',
  `plate_number` VARCHAR(20) COMMENT '车牌号',
  `contacts` VARCHAR(20) COMMENT '联系人姓名',
  `contact_number` VARCHAR(20) COMMENT '联系电话',
  `content_uri` VARCHAR(20) COMMENT '照片uri',
  `color` VARCHAR(20) COMMENT '汽车颜色',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 创建eh_organization_owner与eh_address的多对多表    by xq.tian
--
CREATE TABLE `eh_organization_owner_address` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `address_id` BIGINT,
  `living_status` TINYINT,
  `auth_type` TINYINT COMMENT 'Auth type',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 创建eh_organization_owner_owner_car与eh_organization_owner_cars的多对多表    by xq.tian
--
CREATE TABLE `eh_organization_owner_owner_car` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `organization_owner_id` BIGINT,
  `car_id` BIGINT,
  `primary_flag` TINYINT COMMENT 'primary flag, yes: 1, no: 0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户资料管理中的附件上传记录表    by xq.tian
--
CREATE TABLE `eh_organization_owner_attachments` (
  `id` BIGINT(20) NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(256) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 车辆管理中的附件上传记录表    by xq.tian
--
CREATE TABLE `eh_organization_owner_car_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'car id',
  `attachment_name` VARCHAR(100) COMMENT 'attachment name',
  `content_type` VARCHAR(32) COMMENT 'attachment object content type',
  `content_uri` VARCHAR(256) COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户的活动记录表   by xq.tian
--
CREATE TABLE `eh_organization_owner_behaviors` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_id` BIGINT COMMENT 'organization owner id',
  `address_id` BIGINT COMMENT 'address id',
  `behavior_type` VARCHAR(20) COMMENT '迁入, 迁出..',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `behavior_time` DATETIME,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 客户类型表    by xq.tian
--
CREATE TABLE `eh_organization_owner_type` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name` VARCHAR(20) COMMENT 'owner, tenant, relative, friend',
  `display_name` VARCHAR(20) COMMENT '业主, 租户, 亲戚, 朋友',
  `status` TINYINT COMMENT 'delete: 0, normal: 1',
  `create_time` DATETIME,
  `update_time` DATETIME,
  `update_uid` BIGINT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;