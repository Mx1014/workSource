#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_repeat_settings`;
CREATE TABLE `eh_repeat_settings`(
    `id` BIGINT NOT NULL,
    `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the setting, QA, etc',
    `owner_id` BIGINT NOT NULL DEFAULT 0,
    `forever_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true',
    `repeat_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'how many times of repeating before the end, 0:inactive, others as times',
    `start_date` DATE COMMENT 'the whole span of the event, including repeat time',    
    `end_date` DATE COMMENT 'ineffective if forever_flag is set true, forever_flag/repeat_count/end_date are exclusive, only one is used',
    `time_ranges` VARCHAR(2048) COMMENT 'multiple time ranges in a day, json format, {"ranges":[{"startTime":"08:00:00","endTime":"09:30:00","duration":"3m"},{"startTime":"18:30:00","endTime":"19:30:00","duration":"2d"}]}',
    `repeat_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: no repeat, 1: by day, 2: by week, 3: by month, 4: by year',
    `repeat_interval` INTEGER NOT NULL DEFAULT 1 COMMENT 'every N day/week/month/year',
    `every_workday_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '0: false, 1: true, only effective if repeat_type is by-day',
    `expression` VARCHAR(2048) COMMENT 'the expression for the repeat details, json format, should be parsed with repeat_type and repeat_interval',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
    `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
    `create_time` DATETIME,

    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_standards`;
CREATE TABLE `eh_quality_inspection_standards` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`standard_number` VARCHAR(128),
	`name` VARCHAR(1024),
	`category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_categories',
	`repeat_setting_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_repeat_settings',
	`description` TEXT COMMENT 'content data',
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
    `create_time` DATETIME,
	`operator_uid` BIGINT COMMENT 'operator uid of last operation',
    `update_time` DATETIME,
	`deleter_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'deleter id',
    `delete_time` DATETIME COMMENT 'mark-deletion policy. historic data may be useful',
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_standard_group_map`;
CREATE TABLE `eh_quality_inspection_standard_group_map` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`group_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: executive group, 2: review group',
	`standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_standards',
	`group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
    `create_time` DATETIME,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_tasks`;
CREATE TABLE `eh_quality_inspection_tasks` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, organization, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`standard_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_standards',
	`task_number` VARCHAR(128),
	`task_name` VARCHAR(1024),
	`task_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: verify task, 2: rectify task',
	`parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '0: parent task, others childrean-task',
    `child_count` BIGINT NOT NULL DEFAULT 0,
	`executive_group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
	`executive_start_time` DATETIME,
	`executive_expire_time` DATETIME,
	`executive_time` DATETIME,
	`executor_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
	`executor_id` BIGINT NOT NULL DEFAULT 0,
	`operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who executes the task, organization, etc',
	`operator_id` BIGINT NOT NULL DEFAULT 0, 
	`process_expire_time` DATETIME, 
	`process_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)',
	`process_time` DATETIME, 
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: waiting for executing, 2: rectifing, 3: rectified and waiting approval, 4: rectify closed and waiting approval, 5: closed',
	`result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay',
	`reviewer_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who review the task, organization, etc',
	`reviewer_id`  BIGINT NOT NULL DEFAULT 0,
	`review_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0:none, 1: qualified, 2: unqualified', 
	`review_time` DATETIME,
    `create_time` DATETIME,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
	
DROP TABLE IF EXISTS `eh_quality_inspection_evaluation_factors`;
CREATE TABLE `eh_quality_inspection_evaluation_factors` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_categories',
	`group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
	`weight` DOUBLE NOT NULL DEFAULT 0,
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
    `create_time` DATETIME,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_evaluations`;
CREATE TABLE `eh_quality_inspection_evaluations` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, enterprise, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`date_str` VARCHAR(32) NOT NULL DEFAULT '',
	`group_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_organizations',
	`group_name` VARCHAR(64),
	`score` DOUBLE NOT NULL DEFAULT 0,
    `create_time` DATETIME,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_task_records`;
CREATE TABLE `eh_quality_inspection_task_records` (
	`id` BIGINT NOT NULL COMMENT 'id',
	`task_id` BIGINT NOT NULL DEFAULT 0,
	`operator_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who operates the task, organization, etc',
	`operator_id` BIGINT NOT NULL DEFAULT 0,
	`target_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who is the target of processing the task, organization, etc',
	`target_id` BIGINT NOT NULL DEFAULT 0,
	`process_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: inspect, 2: retify, 3: review, 4: assgin, 5: forward',
	`process_end_time` DATETIME, 
	`process_result` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: inspect ok, 2: inspect close, 3: rectified ok, 4: rectify closed, 5: inspect delay, 6: rectify delay, 11: rectified ok(waiting approval), 12: rectify closed(waiting approval)', 
	`process_message` TEXT,   
	`process_time` DATETIME,
    `create_time` DATETIME,
	
	PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_task_attachments`;
CREATE TABLE `eh_quality_inspection_task_attachments` (
	`id` BIGINT NOT NULL COMMENT 'id of the record',
	`task_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'refernece to the id of eh_quality_inspection_tasks',
	`content_type` VARCHAR(32) DEFAULT NULL COMMENT 'attachment object content type',
	`content_uri` VARCHAR(1024) DEFAULT NULL COMMENT 'attachment object link info on storage',
	`creator_uid` BIGINT NOT NULL DEFAULT 0,
	`create_time` DATETIME,
  
	PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `eh_quality_inspection_categories`;
CREATE TABLE `eh_quality_inspection_categories`(
    `id` BIGINT NOT NULL,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the category, enterprise, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
    `parent_id` BIGINT NOT NULL DEFAULT 0,
    `name` VARCHAR(64) NOT NULL,
    `path` VARCHAR(128),
    `default_order` INTEGER,
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: disabled, 1: waiting for confirmation, 2: active',
	`creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
 





