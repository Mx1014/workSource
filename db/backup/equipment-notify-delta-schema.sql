CREATE TABLE `eh_pm_notify_configurations` (
  `id` BIGINT NOT NULL,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasks/EhQualityInspectionTasks...',
  `scope_type` TINYINT NOT NULL DEFAULT 0 COMMENT '0: all; 1: namespace; 2: community',
  `scope_id` BIGINT NOT NULL,
  `notify_type` TINYINT NOT NULL COMMENT '0: before start; 1: before delay; 2: after delay',
  `notify_mode` TINYINT NOT NULL COMMENT '0:message; 1:sms',
  `repeat_type`TINYINT COMMENT '0: once; 1: repeat',
  `repeat_id` BIGINT,
  `receiver_json` TEXT COMMENT 'notify receivers:{"receivers":[{"receiverType":"3","receiverId":["1","2"]},{"receiverType":"0","receiverId":[]}]}', 
  `notify_tick_minutes` INTEGER COMMENT '提前多少分钟',
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  `create_time` datetime NOT NULL COMMENT 'record create time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_pm_notify_records` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasks/EhQualityInspectionTasks...',
  `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refer to object id',
  `notify_type` TINYINT NOT NULL COMMENT '0: before start; 1: before delay; 2: after delay',
  `notify_mode` TINYINT NOT NULL COMMENT '0:message; 1:sms',
  `notify_time` DATETIME NOT NULL,
  `receiver_json` TEXT COMMENT 'notify receivers:{"receivers":[{"receiverType":"3","receiverId":["1","2"]},{"receiverType":"0","receiverId":[]}]}', 
  `create_time` DATETIME NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: waiting for send out, 2: already sended',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_pm_notify_logs` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'refer to object type EhEquipmentInspectionTasks/EhQualityInspectionTasks...',
  `owner_id` BIGINT NOT NULL DEFAULT '0' COMMENT 'refer to object id',
  `notify_text` TEXT,
  `receiver_id` BIGINT NOT NULL,
  `create_time` DATETIME NOT NULL,
  `status` TINYINT NOT NULL COMMENT '0: invalid, 1: valid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


