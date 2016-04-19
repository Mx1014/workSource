DROP TABLE IF EXISTS `eh_op_promotion_policies`;
DROP TABLE IF EXISTS `eh_op_promotions`;
DROP TABLE IF EXISTS `eh_op_promotion_settings`;

#
# member of global parition
# shared among namespaces, no application module specific information
#
DROP TABLE IF EXISTS `eh_op_promotion_activities`;
CREATE TABLE `eh_op_promotion_activities`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `title` VARCHAR(512) NOT NULL DEFAULT '' COMMENT 'the title of the activity',
	`description` TEXT,
	`start_time` DATETIME,
    `end_time` DATETIME,
	`scope_code` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all, 1: community, 2: city, 3: user, 4: organization',
    `scope_id` BIGINT,
	`policy_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: registerd, 2: reach amount of consumption in zuolin-biz',
	`policy_data` TEXT COMMENT 'json format, the parameters which help executing the policy',
    `icon_uri` VARCHAR(1024),
	`action_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of what to do, according to document',
    `action_data` TEXT COMMENT 'the parameters depend on ation type, json format',
	`push_message` VARCHAR(1024) COMMENT 'the message need to push',
	`push_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'how many user received the push',
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: waiting for approval, 2: active',
	`process_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: none, 1: on process, 2: finished, 3: close',
    `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'record creator user id',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
# the operation promotion message send to user
#
DROP TABLE IF EXISTS `eh_schedule_tasks`;
CREATE TABLE `eh_schedule_tasks`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`resource_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
	`resource_id` BIGINT NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the owner of the task, user, group, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`process_count` INTEGER NOT NULL DEFAULT 0 COMMENT 'the count of process',
	`progress` INTEGER NOT NULL DEFAULT 0 COMMENT '0~100 percentage',
    `progress_data` TEXT COMMENT 'the data at the point of progress, it can recover the task if it interupted in the middle, json format',
	`status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: inactive, 1: new, 2: on progress',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# member of global parition
# shared among namespaces, no application module specific information
# the operation promotion message send to user
#
DROP TABLE IF EXISTS `eh_schedule_task_logs`;
CREATE TABLE `eh_schedule_task_logs`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
    `task_id` BIGINT NOT NULL DEFAULT 0,
	`resource_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
	`resource_id` BIGINT NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
	`start_time` DATETIME,
    `end_time` DATETIME,
    `result_data` TEXT COMMENT 'the data need to keep after task finished',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


#    `embedded_app_id` BIGINT,
#    `embedded_id` BIGINT,
#    `embedded_json` LONGTEXT,
#    `embedded_version` INTEGER NOT NULL DEFAULT 1,

#
# member of global parition
# shared among namespaces, no application module specific information
# the operation promotion message send to user
#
DROP TABLE IF EXISTS `eh_op_promotion_messages`;
CREATE TABLE `eh_op_promotion_messages`(
    `id` BIGINT NOT NULL,
    `namespace_id` INTEGER NOT NULL DEFAULT 0,
	`owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'the type refer to, eh_op_promotion_activities, etc',
	`owner_id` BIGINT NOT NULL DEFAULT 0,
    `message_seq` BIGINT NOT NULL COMMENT 'message sequence id generated at server side',
    `sender_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'the id of user who send the message',
    `target_uid` BIGINT NOT NULL DEFAULT 0,
    `message_text` TEXT COMMENT 'message content',
    `meta_app_id` BIGINT COMMENT 'app that is in charge of message content and meta intepretation',
    `message_meta` TEXT COMMENT 'JSON encoded message meta info, in format of string to string map',
    `result_data` TEXT COMMENT 'sender generated tag',
    `create_time` DATETIME,
    
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

