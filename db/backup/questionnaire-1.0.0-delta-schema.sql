
-- 问卷调查表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 题目表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 选项表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 目标选中的选项表
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
