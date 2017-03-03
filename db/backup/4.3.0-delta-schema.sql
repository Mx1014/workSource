
-- 园区入驻2.4
ALTER TABLE `eh_enterprise_op_requests` ADD COLUMN `contract_id` BIGINT COMMENT 'eh_contracts id';

ALTER TABLE `eh_yellow_pages` ADD COLUMN `building_id` BIGINT COMMENT 'eh_buildings id';

--
-- 园区入驻申请的关联楼栋表
--
CREATE TABLE `eh_enterprise_op_request_buildings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `enterprise_op_requests_id` BIGINT NOT NULL COMMENT 'eh_enterprise_op_requests id',
  `building_id` BIGINT  COMMENT 'building id ', 
  `status` TINYINT ,
  `creator_uid` BIGINT ,
  `create_time` DATETIME ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4; 

-- 问卷调查表, add by tt, 20170223
-- DROP TABLE IF EXISTS  `eh_questionnaires`;
CREATE TABLE `eh_questionnaires` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'community',
  `owner_id` BIGINT,
  `questionnaire_name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(512),
  `collection_count` INTEGER,
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. draft, 2. active',
  `publish_time` DATETIME,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
	
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 题目表, add by tt, 20170223
-- DROP TABLE IF EXISTS  `eh_questionnaire_questions`;
CREATE TABLE `eh_questionnaire_questions` (
  `id` BIGINT NOT NULL,
  `questionnaire_id` BIGINT NOT NULL,
  `question_type` TINYINT COMMENT '1. radio， 2. check_box， 3. blank, 4. image_radio, 5. image_check_box',
  `question_name` VARCHAR(50) NOT NULL,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
	
  PRIMARY KEY (`id`),
  INDEX `i_questionnaire_id` (`questionnaire_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 选项表, add by tt, 20170223
-- DROP TABLE IF EXISTS  `eh_questionnaire_options`;
CREATE TABLE `eh_questionnaire_options` (
  `id` BIGINT NOT NULL,  
  `questionnaire_id` BIGINT NOT NULL,
  `question_id` BIGINT NOT NULL,
  `option_name` VARCHAR(50),
  `option_uri` VARCHAR(1024),
  `checked_count` INTEGER,
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`),  
  INDEX `i_question_id` (`question_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 目标选中的选项表, add by tt, 20170223
-- DROP TABLE IF EXISTS  `eh_questionnaire_answers`;
CREATE TABLE `eh_questionnaire_answers` (
  `id` BIGINT NOT NULL,
  `questionnaire_id` BIGINT NOT NULL,
  `question_id` BIGINT NOT NULL,
  `option_id` BIGINT NOT NULL,
  `target_type` VARCHAR(32) COMMENT 'organization',
  `target_id` BIGINT NOT NULL,
  `target_name` VARCHAR(128),
  `option_content` VARCHAR(1024) COMMENT 'if question_type is blank, then this field has value',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  
  PRIMARY KEY (`id`),
  INDEX `i_questionnaire_id` (`questionnaire_id`),
  INDEX `i_question_id` (`question_id`),
  INDEX `i_option_id` (`option_id`),
  INDEX `i_target` (`target_type`, `target_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 对象存储(object storage)  add by xq.tian  2017/02/24
-- 对象表
CREATE TABLE `eh_os_objects` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',
  `service_type` VARCHAR(32) COMMENT '需要存储对象的业务类型',
  `service_id` BIGINT COMMENT '业务类型对应的业务id',
  `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'parent id, eh_os_objects id',
  `object_type` TINYINT NOT NULL DEFAULT 2 COMMENT '0: dir, 1: file',
  `md5` VARCHAR(64) COMMENT 'md5',
  `path` VARCHAR(2048) COMMENT 'e.g: a/b/c/d/e',
  `name` VARCHAR(256) NOT NULL COMMENT 'object name',
  `size` INTEGER NOT NULL DEFAULT 0 COMMENT 'The unit is byte',
  `content_type` VARCHAR(32) COMMENT 'file object content type',
  `content_uri` VARCHAR(1024) COMMENT 'file object link info on storage',
  `download_count` INTEGER NOT NULL DEFAULT 0,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: inactive, 1: waitingForConfirmation, 2: active',
  `delete_uid` BIGINT,
  `delete_time` DATETIME(3),
  `update_uid` BIGINT,
  `update_time` DATETIME(3),
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHAR SET ='utf8mb4';

-- 对象下载记录
CREATE TABLE `eh_os_object_download_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER DEFAULT 0,
  `owner_type` VARCHAR(32) COMMENT 'owner type, e.g EhCommunities',
  `owner_id` BIGINT COMMENT 'owner id, e.g eh_communities id',
  `service_type` VARCHAR(32) COMMENT '需要存储对象的业务类型',
  `service_id` BIGINT COMMENT '业务类型对应的业务id',
  `object_id` BIGINT NOT NULL COMMENT 'eh_os_objects id',
  `creator_uid` BIGINT,
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHAR SET ='utf8mb4';

-- 对接映射表
CREATE TABLE `eh_docking_mappings` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',  
  `scope` VARCHAR(64) NOT NULL,
  `name` VARCHAR(256),
  `mapping_value` VARCHAR(256),
  `mapping_json` VARCHAR(1024),
  `namespace_id` INTEGER NOT NULL DEFAULT 0,

  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;