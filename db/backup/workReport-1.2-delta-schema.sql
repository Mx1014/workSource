ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `organization_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the orgId for the user' AFTER `namespace_id`;
ALTER TABLE `eh_work_report_val_receiver_map` ADD INDEX `i_work_report_receiver_id` (`receiver_user_id`) ;

ALTER TABLE `eh_work_reports` ADD COLUMN `validity_setting` VARCHAR(512) COMMENT 'the expiry date of the work report' AFTER `form_version`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the receiver message settings' AFTER `validity_setting`;
ALTER TABLE `eh_work_reports` ADD COLUMN `receiver_msg_seeting` VARCHAR(512) COMMENT 'the time range of the receiver message' AFTER `receiver_msg_type`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of the author message settings' AFTER `receiver_msg_seeting`;
ALTER TABLE `eh_work_reports` ADD COLUMN `author_msg_seeting` VARCHAR(512) COMMENT 'the time range of the author message' AFTER `author_msg_type`;
-- ALTER TABLE `eh_work_reports` ADD COLUMN `icon_uri` VARCHAR(1024) COMMENT 'the icon of the work report' AFTER `delete_flag`;

ALTER TABLE `eh_work_report_vals` ADD COLUMN `receiver_avatar` VARCHAR(1024) COMMENT 'the avatar of the fisrt receiver' AFTER `report_type`;
ALTER TABLE `eh_work_report_vals` ADD COLUMN `applier_avatar` VARCHAR(1024) COMMENT 'the avatar of the author' AFTER `receiver_avatar`;

ALTER TABLE `eh_work_report_vals` MODIFY COLUMN `report_time` DATE COMMENT 'the target time of the report';


CREATE TABLE `eh_work_report_val_receiver_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_val_id` BIGINT NOT NULL COMMENT 'id of the report val',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` DATE NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `receiver_user_id` BIGINT NOT NULL COMMENT 'the id of the receiver',
  `create_time` DATETIME COMMENT 'record create time',

  KEY `i_eh_work_report_val_receiver_msg_report_id`(`report_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_val_id`(`report_val_id`),
  KEY `i_eh_work_report_val_receiver_msg_report_time`(`report_time`),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_work_report_scope_msg` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `organization_id` BIGINT NOT NULL DEFAULT 0,
  `report_id` BIGINT NOT NULL COMMENT 'the id of the report',
  `report_name` VARCHAR(128) NOT NULL,
  `report_type` TINYINT COMMENT '0-Day, 1-Week, 2-Month',
  `report_time` DATE NOT NULL COMMENT 'the target time of the report',
  `reminder_time` DATETIME COMMENT 'the reminder time of the record',
  `end_time` DATETIME COMMENT 'the deadline of the report',
  `scope_ids` TEXT COMMENT 'the id list of the receiver',
  `create_time` DATETIME COMMENT 'record create time',

  KEY `i_eh_work_report_scope_msg_report_id`(`report_id`),
  KEY `i_eh_work_report_scope_msg_report_time`(`report_time`),
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `report_id` BIGINT DEFAULT 0 NOT NULL COMMENT 'the report id' AFTER `organization_id`;
-- ALTER TABLE `eh_work_report_val_receiver_map` ADD COLUMN `reminder_time` DATETIME AFTER `read_status`;
--
-- ALTER TABLE `eh_work_report_val_receiver_map` ADD INDEX `i_reminder_time` (`reminder_time`) ;