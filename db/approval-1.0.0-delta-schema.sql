-- 审批流程表
CREATE TABLE `eh_approval_flows` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`name` VARCHAR(64) NOT NULL COMMENT 'name of flow',
	`status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
	`update_time` DATETIME,
	`operator_uid` BIGINT, 
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批流程对应每级的人/角色表
CREATE TABLE `eh_approval_flow_levels` (
	`id` BIGINT NOT NULL,
	`flow_id` BIGINT NOT NULL COMMENT 'id of flow',
	`level` TINYINT(4) NOT NULL COMMENT '1,2,3,4,5...',
	`target_type` TINYINT(4) NOT NULL COMMENT '1. user, 2. role',
	`target_id` BIGINT NOT NULL COMMENT 'id of target, e.g id of user', 
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批规则表
CREATE TABLE `eh_approval_rules` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`name` VARCHAR(64) NOT NULL COMMENT 'name of approval rule',
	`status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
	`update_time` DATETIME,
	`operator_uid` BIGINT, 
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批规则与流程关联表
CREATE TABLE `eh_approval_rule_flow_map` (
	`id` BIGINT NOT NULL,
	`rule_id` BIGINT NOT NULL COMMENT 'id of rule',
	`approval_type` TINYINT(4) NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
	`flow_id` BIGINT NOT NULL COMMENT 'id of flow', 
	`status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批具体类别表
CREATE TABLE `eh_approval_categories` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`approval_type` TINYINT(4) NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
	`category_name` VARCHAR(64) NOT NULL COMMENT 'name of category',
	`status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`creator_uid` BIGINT,
	`create_time` DATETIME, 
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 申请记录表
CREATE TABLE `eh_approval_requests` (
	`id` BIGINT NOT NULL,
	`namespace_id` INT NOT NULL DEFAULT '0',
	`owner_type` VARCHAR(32),
	`owner_id` BIGINT,
	`approval_type` TINYINT(4) NOT NULL COMMENT '1. ask for leave, 2. forget to punch',
	`category_id` BIGINT COMMENT 'concrete category id',
	`content_json` LONGTEXT COMMENT 'json of concrete category content',
	`attachment_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'if there are attachments, 0. no, 1. yes',
	`time_flag` TINYINT(4) NOT NULL DEFAULT '0' COMMENT 'if there are time ranges, 0. no, 1. yes',
	`flow_id` BIGINT COMMENT 'id of flow',
	`current_level` BIGINT COMMENT 'current levle of flow',
	`next_level` BIGINT COMMENT 'next level of flow',
	`approval_status` TINYINT(4) NOT NULL COMMENT '0. waitingForApproving, 1. agreement, 2. rejection',
	`status` TINYINT(4) NOT NULL COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
	`creator_uid` BIGINT,
	`create_time` DATETIME,
	`update_time` DATETIME,
	`operator_uid` BIGINT, 
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 审批附件表
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

-- 申请处理日志表
CREATE TABLE `eh_approval_op_requests` (
	`id` BIGINT NOT NULL,
	`request_id` BIGINT NOT NULL COMMENT 'id of request',
	`requestor_comment` TEXT COMMENT 'comment of reqeust',
	`process_message` TEXT COMMENT 'process message',
	`operator_uid` BIGINT,
	`create_time` DATETIME,
	`approval_status` TINYINT(4) NOT NULL COMMENT '0. waitingForApproving, 1. agreement, 2. rejection',
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
