-- 快递公司对应业务表，add by dengs, 20170718
-- DROP TABLE IF EXISTS `eh_express_company_businesses`;
CREATE TABLE `eh_express_company_businesses` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT '目前是EhNamespaces',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT '域空间',
  `express_company_id` BIGINT COMMENT 'id of the express company id,是parent_id = 0 的快递公司的id',
  `send_type` TINYINT COMMENT '业务类型id',
  `send_type_name` VARCHAR(128) COMMENT '业务类型名称(/（华润）标准快递/（国贸）EMS标准快递/（国贸）邮政快递包裹/（国贸）同城信筒快件)',
  `package_types` TEXT COMMENT '封装类型，参考 ExpressPackageType.class,json数组',
  `insured_documents` VARCHAR(1024) COMMENT '保价文案，目前只有国贸ems和国贸邮政的邮政快递包裹有保价文案，所以跟着业务走',
  `order_status_collections` TEXT COMMENT '订单状态集合,json数组 参考 ExpressOrderStatus.class',
  `pay_type` TINYINT COMMENT '支付方式， 参考,ExpressPayType.class,寄付现结=1,线下支付=2',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 快递服务热线表，add by dengs, 20170718
-- DROP TABLE IF EXISTS `eh_express_hotlines`;
CREATE TABLE `eh_express_hotlines` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community id',
  `service_name` VARCHAR(512) COMMENT '服务热线的服务名称',
  `hotline` VARCHAR(128) COMMENT '热线电话',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 快递设置表，add by dengs, 20170718
-- DROP TABLE IF EXISTS `eh_express_param_settings`;
CREATE TABLE `eh_express_param_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'community or namespace',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'community or namespace id',
  `express_user_setting_show_flag` TINYINT DEFAULT 0  COMMENT '快递员设置 是否在后台管理 显示标志',
  `business_note_setting_show_flag` TINYINT DEFAULT 1  COMMENT '业务说明 是否在后台管理 显示标志',
  `hotline_setting_show_flag` TINYINT DEFAULT 1  COMMENT '客服热线 是否在后台管理 显示标志',
  `hotline_flag` TINYINT DEFAULT 0  COMMENT '热线是否在app显示标志，可在后台修改',
  `business_note` TEXT DEFAULT NULL  COMMENT '业务说明',
  `business_note_flag` TINYINT DEFAULT 0 COMMENT '业务说明是否在app显示标志，可在后台修改',
  `send_mode` TINYINT DEFAULT 1 COMMENT '1,服务点自寄 2，快递员上门收件',
  `status` TINYINT NOT NULL COMMENT '0. inactive, 1. waiting for approval, 2. active',
  `creator_uid` BIGINT,
  `create_time` DATETIME,
  `update_time` DATETIME,
  `operator_uid` BIGINT,
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_express_companies` ADD COLUMN `description` VARCHAR(512) COMMENT '快递公司描述信息，比如华润ems和国贸ems需要区别一下，描述给后来的人看懂' AFTER `logo`;
ALTER TABLE `eh_express_companies` ADD COLUMN `authorization` VARCHAR(512) COMMENT '授权码' AFTER `description`;
ALTER TABLE `eh_express_companies` ADD COLUMN `app_secret` VARCHAR(512) COMMENT '' AFTER `description`;
ALTER TABLE `eh_express_companies` ADD COLUMN `app_key` VARCHAR(512) COMMENT '' AFTER `description`;
ALTER TABLE `eh_express_companies` ADD COLUMN `order_url` VARCHAR(2048) COMMENT '快递公司订单服务器地址' AFTER `description`;
ALTER TABLE `eh_express_companies` ADD COLUMN `logistics_url` VARCHAR(2048) COMMENT '快递公司物流服务器地址' AFTER `description`;

ALTER TABLE `eh_express_orders`	ADD COLUMN `package_type` TINYINT COMMENT '封装类型，参考 ExpressPackageType.class' AFTER `internal`;
ALTER TABLE `eh_express_orders`	ADD COLUMN `invoice_flag` TINYINT COMMENT '需要发票选项,0:不需要 1：需要手撕发票 2：需要税票 ExpressInvoiceFlagType.class' AFTER `internal` ;
ALTER TABLE `eh_express_orders`	ADD COLUMN `invoice_head` VARCHAR(512) COMMENT '税票的发票抬头' AFTER `internal`;
-- 这里华润需要处理老数据，赋值为 send_type_name =  标准快递
ALTER TABLE `eh_express_orders`	ADD COLUMN `send_type_name` VARCHAR(128) COMMENT '业务类型名称(/（华润）标准快递/（国贸）EMS标准快递/（国贸）邮政快递包裹/（国贸）同城信筒快件)' AFTER `send_type`;
ALTER TABLE `eh_express_orders`	ADD COLUMN `quantity_and_weight` VARCHAR(128) COMMENT '数量和重量' AFTER `send_type_name`;

--
ALTER TABLE `eh_express_orders` ADD COLUMN `status_desc` TEXT COMMENT '状态描述信息，国贸ems使用' AFTER `status`;

-- end by dengs,2017.08.28 快递

-- 停车 add by sw 20170828
ALTER TABLE eh_parking_lots DROP COLUMN `lock_car_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `contact`;
ALTER TABLE eh_parking_lots DROP COLUMN `tempfee_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `rate_flag`;
ALTER TABLE eh_parking_lots DROP COLUMN `max_request_num`;

ALTER TABLE eh_parking_lots DROP COLUMN `card_reserve_days`;
ALTER TABLE eh_parking_lots DROP COLUMN `recharge_month_count`;
ALTER TABLE eh_parking_lots DROP COLUMN `recharge_type`;
ALTER TABLE eh_parking_lots DROP COLUMN `is_support_recharge`;

ALTER TABLE eh_parking_lots DROP COLUMN `is_support_recharge`;
ALTER TABLE eh_parking_lots DROP COLUMN `is_support_recharge`;

ALTER TABLE eh_parking_lots ADD COLUMN `expired_recharge_json` VARCHAR(1024) DEFAULT NULL;
ALTER TABLE eh_parking_lots ADD COLUMN `config_json` VARCHAR(1024) DEFAULT NULL;

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `order_type` tinyint(4) DEFAULT 1;

-- 临时增加功能：可以投票增加"重复投票"选项和"投票间隔时间"   add by yanjun 20170825
ALTER TABLE `eh_poll_votes` DROP INDEX `i_eh_poll_vote_voter` , ADD INDEX `i_eh_poll_vote_voter` (`poll_id`, `item_id`, `voter_uid`) USING BTREE ;
ALTER TABLE `eh_polls` ADD COLUMN `repeat_flag`  tinyint(4) NULL COMMENT 'is support repeat poll. 0-no, 1-yes', ADD COLUMN `repeat_period`  int(11) NULL COMMENT 'repeat_period,  day';


--
-- 运营表添加域空间字段  add by xq.tian  2017/08/24
--
-- 第一步
ALTER TABLE eh_terminal_statistics_tasks ADD COLUMN namespace_id INTEGER NOT NULL DEFAULT 0;
ALTER TABLE eh_terminal_statistics_tasks DROP INDEX `task_no`;
ALTER TABLE eh_terminal_statistics_tasks ALGORITHM=inplace, LOCK=NONE, ADD UNIQUE INDEX `u_eh_task_no_namespace_id`(`task_no`, `namespace_id`);

-- 第二步
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, DROP INDEX i_eh_imei_number;
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, DROP INDEX i_eh_create_time;

-- 第三步
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX `i_eh_namespace_id`(`namespace_id`) USING HASH;
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX `i_eh_imei_number`(`imei_number`) USING HASH;
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX `i_eh_app_version_name`(`app_version_name`);
ALTER TABLE eh_user_activities ALGORITHM=inplace, LOCK=NONE, ADD INDEX `i_eh_create_time`(`create_time`);

-- 第四步
OPTIMIZE TABLE eh_user_activities;


--
-- 设备日志表  add by xq.tian  2017/08/28
--
-- DROP TABLE IF EXISTS `eh_stat_event_device_logs`;
CREATE TABLE `eh_stat_event_device_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `app_version` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'app version',
  `device_id` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'device id',
  `device_brand` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'brand',
  `device_model` VARCHAR(64) DEFAULT '' COMMENT 'cellPhone model model',
  `os_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: Android, 2: ios',
  `os_version` VARCHAR(64) DEFAULT '' COMMENT 'system version',
  `access` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'access',
  `imei` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'imei',
  `client_ip` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'ip address',
  `server_ip` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'ip address',
  `country` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'country',
  `language` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'language',
  `mc` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'mc',
  `resolution` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'resolution',
  `timezone` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'timezone',
  `carrier` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'carrier',
  `version_realm` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'version_realm',
  `device_time` BIGINT NOT NULL DEFAULT 0 COMMENT 'device time',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 日志上传策略表
--
-- DROP TABLE IF EXISTS `eh_stat_event_upload_strategies`;
CREATE TABLE `eh_stat_event_upload_strategies` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'e.g: EhUsers',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'owner identifier',
  `access` VARCHAR(32) NOT NULL COMMENT 'WIFI, GSM',
  `log_type` TINYINT NOT NULL COMMENT '1: GENERAL_EVENT, 2: CRASH_LOG, 3:ERROR_LOG',
  `strategy` TINYINT NOT NULL COMMENT '0: NO, 1: INTERVAL, 2: IMMEDIATE, 3: TIMES_PER_DAY',
  `interval_seconds` INTEGER COMMENT 'interval seconds',
  `times_per_day` INTEGER COMMENT 'times_per_day',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件表
--
-- DROP TABLE IF EXISTS `eh_stat_events`;
CREATE TABLE `eh_stat_events` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `event_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '1: general event',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_version` VARCHAR(32) NOT NULL DEFAULT '1',
  `event_display_name` VARCHAR(64) NOT NULL COMMENT 'event name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件参数表
--
-- DROP TABLE IF EXISTS `eh_stat_event_params`;
CREATE TABLE `eh_stat_event_params` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `event_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '1: general event',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_version` VARCHAR(32) NOT NULL DEFAULT '1',
  `multiple` INTEGER NOT NULL DEFAULT 1 COMMENT 'multiple',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `param_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: string param, 2: value param',
  `param_key` VARCHAR(64) NOT NULL COMMENT 'param key',
  `param_name` VARCHAR(64) NOT NULL COMMENT 'param name',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件记录表
--
-- DROP TABLE IF EXISTS `eh_stat_event_content_logs`;
CREATE TABLE `eh_stat_event_content_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `content` TEXT,
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '1: loaded to eh_stat_event_logs, 2: did not load to eh_stat_event_logs',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件日志表
--
-- DROP TABLE IF EXISTS `eh_stat_event_logs`;
CREATE TABLE `eh_stat_event_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_version` VARCHAR(32) NOT NULL DEFAULT '1',
  `device_gen_id` BIGINT NOT NULL COMMENT 'id of device generate',
  `device_time` BIGINT NOT NULL COMMENT 'device time',
  `acc` INTEGER COMMENT 'acc',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `upload_time` DATETIME(3),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件参数日志表
--
-- DROP TABLE IF EXISTS `eh_stat_event_param_logs`;
CREATE TABLE `eh_stat_event_param_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_version` VARCHAR(32) NOT NULL DEFAULT '1',
  `event_log_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_stat_event_logs id',
  `param_key` VARCHAR(64) COMMENT 'key',
  `string_value` VARCHAR(64) COMMENT 'string value',
  `number_value` INTEGER COMMENT 'number value',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `upload_time` DATETIME(3),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- App日志附件
--
-- DROP TABLE IF EXISTS `eh_stat_event_app_attachment_logs`;
CREATE TABLE `eh_stat_event_app_attachment_logs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `session_id` VARCHAR(64) NOT NULL COMMENT 'session id',
  `uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_users id',
  `content_type` VARCHAR(32) NULL DEFAULT NULL COMMENT 'attachment object content type',
  `content_uri` VARCHAR(1024) NULL DEFAULT NULL COMMENT 'attachment object link info on storage',
  `creator_uid` BIGINT(20) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 配置表
--
-- DROP TABLE IF EXISTS `eh_stat_event_portal_configs`;
CREATE TABLE `eh_stat_event_portal_configs` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `config_type` TINYINT NOT NULL COMMENT '1: 顶部工具栏的子项',
  `config_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'config name',
  `identifier` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'identifier',
  `display_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'config display name',
  `description` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `creator_uid` BIGINT,
  `update_uid` BIGINT,
  `create_time` DATETIME(3),
  `update_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 配置历史记录
--
-- DROP TABLE IF EXISTS `eh_stat_event_portal_statistics`;
CREATE TABLE `eh_stat_event_portal_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `parent_id` BIGINT NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'owner type',
  `owner_id` BIGINT NOT NULL COMMENT 'owner id',
  `stat_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: 门户, 2: 门户的子项, 3: 底部导航栏的子项, 4: 顶部工具栏的子项',
  `name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'name',
  `identifier` VARCHAR(64) NOT NULL COMMENT 'identifier',
  `display_name` VARCHAR(64) NOT NULL COMMENT 'display_name',
  `time_interval` VARCHAR(32) NOT NULL COMMENT 'HOURLY, DAILY, WEEKLY, MONTHLY',
  `stat_date` DATE NOT NULL COMMENT 'stat date',
  `description` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'description',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- 事件统计表
--
-- DROP TABLE IF EXISTS `eh_stat_event_statistics`;
CREATE TABLE `eh_stat_event_statistics` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `owner_type` VARCHAR(64),
  `owner_id` BIGINT,
  `event_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1: count event, 2: calculate event',
  `event_name` VARCHAR(64) NOT NULL COMMENT 'event identifier',
  `event_version` VARCHAR(32) NOT NULL DEFAULT '1',
  `event_display_name` VARCHAR(64) NOT NULL DEFAULT '' COMMENT 'event display name',
  `event_portal_stat_id` BIGINT NOT NULL COMMENT 'ref eh_stat_event_portal_statistics id',
  `time_interval` VARCHAR(32) NOT NULL COMMENT 'HOURLY, DAILY, WEEKLY, MONTHLY',
  `stat_date` DATE NOT NULL COMMENT 'stat date',
  `total_count` BIGINT NOT NULL DEFAULT 0 COMMENT 'event total count',
  `unique_users` BIGINT NOT NULL DEFAULT 0 COMMENT 'unique users',
  `completed_sessions` BIGINT NOT NULL DEFAULT 0 COMMENT 'completed sessions',
  `param` TEXT COMMENT 'param',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0. inactive, 1. waitingForConfirmation, 2. active',
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- task log
-- DROP TABLE IF EXISTS `eh_stat_event_task_logs`;
CREATE TABLE `eh_stat_event_task_logs` (
  `id` BIGINT NOT NULL,
  `task_date` DATE NOT NULL,
  `step_name` VARCHAR(256) NOT NULL,
  `status` VARCHAR(32) NOT NULL,
  `task_meta` MEDIUMTEXT,
  `exception_stacktrace` TEXT,
  `duration_seconds` INTEGER,
  `update_Time` DATETIME(3),
  `create_time` DATETIME(3),
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- By lei.lv
-- 关系表建表脚本
DROP TABLE IF EXISTS `eh_community_default`;
CREATE TABLE `eh_community_default` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `origin_community_id` bigint(20) NOT NULL,
  `target_community_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for eh_namespace_masks
-- ----------------------------
DROP TABLE IF EXISTS `eh_namespace_masks`;
CREATE TABLE `eh_namespace_masks` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL,
  `item_name` varchar(64) NOT NULL,
  `image_type` tinyint(4) DEFAULT NULL,
  `tips` varchar(255) DEFAULT NULL,
  `scene_type` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
