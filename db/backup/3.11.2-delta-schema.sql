
-- added by wh  2016-11-14  增加平均价格字段

ALTER TABLE `eh_rentalv2_resources` ADD COLUMN `avg_price_str` VARCHAR(1024) COMMENT '平均价格计算好的字符串';

-- added by wh  2016-10-19  申请表添加时长和生效日期  

ALTER TABLE `eh_approval_requests` ADD COLUMN `effective_date` DATE COMMENT 'when the request approval effective';
ALTER TABLE `eh_approval_requests` ADD COLUMN `hour_length` DOUBLE COMMENT 'how long (hours) does the request effective'; 

-- added by wh 2016-11-09 打卡计算结果还要对异常的设备进行计算

ALTER TABLE `eh_punch_day_logs` ADD COLUMN `device_change_flag` TINYINT DEFAULT 0 COMMENT '0- unchange 1-changed' ;


-- merge from energy-delta-schema.sql   by xq.tian  2016/11/11
--
-- 表记   add by xq.tian  2016/10/25
--
-- DROP TABLE IF EXISTS `eh_energy_meters`;
CREATE TABLE `eh_energy_meters` (
  `id`                  BIGINT  NOT NULL,
  `namespace_id`        INTEGER NOT NULL DEFAULT 0,
  `community_id`        BIGINT,
  `name`                VARCHAR(100),
  `meter_number`        VARCHAR(50) COMMENT 'meter number',
  `meter_type`          TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `bill_category_id`    BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `max_reading`         DECIMAL(10, 1) COMMENT 'The maximum range of the meter',
  `start_reading`       DECIMAL(10, 1) COMMENT 'The initial reading of the meter',
  `rate`                DECIMAL(10, 2) COMMENT 'Calculate magnification',
  `price`               DECIMAL(10, 2),
  `cost_formula_id`     BIGINT COMMENT 'Cost calculation formula',
  `amount_formula_id`   BIGINT COMMENT 'Amount calculation formula',
  `last_read_time`      DATETIME,
  `last_reading`        DECIMAL(10, 1),
  `status`              TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`         BIGINT,
  `create_time`         DATETIME,
  `update_uid`          BIGINT,
  `update_time`         DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 抄表记录
--
-- DROP TABLE IF EXISTS `eh_energy_meter_reading_logs`;
CREATE TABLE `eh_energy_meter_reading_logs` (
  `id`                BIGINT  NOT NULL,
  `namespace_id`      INTEGER NOT NULL DEFAULT 0,
  `community_id`      BIGINT,
  `meter_id`          BIGINT,
  `reading`           DECIMAL(10, 1),
  `operator_id`       BIGINT,
  `operate_time`      DATETIME,
  `reset_meter_flag`  TINYINT DEFAULT 0 COMMENT '0: normal, 1: reset',
  `change_meter_flag` TINYINT DEFAULT 0 COMMENT '0: normal, 1: change',
  `status`            TINYINT DEFAULT 2 COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`       BIGINT,
  `create_time`       DATETIME,
  `update_uid`        BIGINT,
  `update_time`       DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 换表记录
--
-- DROP TABLE IF EXISTS `eh_energy_meter_change_logs`;
CREATE TABLE `eh_energy_meter_change_logs` (
  `id`             BIGINT  NOT NULL,
  `namespace_id`   INTEGER NOT NULL DEFAULT 0,
  `meter_id`       BIGINT,
  `reading_log_id` BIGINT COMMENT 'eh_energy_meter_reading_logs id',
  `old_reading`    DECIMAL(10, 1) COMMENT 'The reading of the old meter',
  `new_reading`    DECIMAL(10, 1) COMMENT 'The initial reading of the new meter',
  `max_reading`    DECIMAL(10, 1) COMMENT 'The maximum range of the new meter',
  `operator_id`    BIGINT,
  `operate_time`   DATETIME,
  `status`         TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`    BIGINT,
  `create_time`    DATETIME,
  `update_uid`     BIGINT,
  `update_time`    DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 表记分类属性
--
-- DROP TABLE IF EXISTS `eh_energy_meter_categories`;
CREATE TABLE `eh_energy_meter_categories` (
  `id`            BIGINT  NOT NULL,
  `namespace_id`  INTEGER NOT NULL DEFAULT 0,
  `name`          VARCHAR(255) COMMENT 'name',
  `status`        TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `category_type` TINYINT COMMENT '1: bill, 2: service',
  `delete_flag`   TINYINT COMMENT '0: can delete, 1: can not delete',
  `creator_uid`   BIGINT,
  `create_time`   DATETIME,
  `update_uid`    BIGINT,
  `update_time`   DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 表记属性
--
-- DROP TABLE IF EXISTS `eh_energy_meter_setting_logs`;
CREATE TABLE `eh_energy_meter_setting_logs` (
  `id`            BIGINT  NOT NULL,
  `namespace_id`  INTEGER NOT NULL DEFAULT 0,
  `meter_id`      BIGINT,
  `setting_type`  TINYINT COMMENT '1: price, 2: rate, 3: amountFormula, 4: costFormula',
  `setting_value` DECIMAL(10, 2) COMMENT 'if setting_type is price or rate have this value',
  `formula_id`    BIGINT COMMENT 'if setting_type is amountFormula or costFormula have this value',
  `start_time`    DATETIME COMMENT 'The start of the time period',
  `end_time`      DATETIME COMMENT 'The end of the time period',
  `status`        TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`   BIGINT,
  `create_time`   DATETIME,
  `update_uid`    BIGINT,
  `update_time`   DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 默认显示的属性值
--
-- DROP TABLE IF EXISTS `eh_energy_meter_default_settings`;
CREATE TABLE `eh_energy_meter_default_settings` (
  `id`            BIGINT  NOT NULL,
  `namespace_id`  INTEGER NOT NULL DEFAULT 0,
  `meter_type`    TINYINT COMMENT '1:WATER, 2: ELECTRIC, 3: ALL',
  `setting_type`  TINYINT COMMENT '1: price, 2: rate, 3: amountFormula, 4: costFormula, 5: dayPrompt, 6: monthPrompt etc',
  `name`          VARCHAR(255) COMMENT 'setting name',
  `setting_value` DECIMAL(10, 2) COMMENT 'if setting_type is price or rate have this value',
  `formula_id`    BIGINT COMMENT 'if setting_type is amountFormula or costFormula have this value',
  `status`        TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active, 3: disabled',
  `creator_uid`   BIGINT,
  `create_time`   DATETIME,
  `update_uid`    BIGINT,
  `update_time`   DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 表记计算公式
--
-- DROP TABLE IF EXISTS `eh_energy_meter_formulas`;
CREATE TABLE `eh_energy_meter_formulas` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name`         VARCHAR(255) COMMENT 'formula name',
  `expression`   VARCHAR(255) COMMENT 'expression',
  `display_expression`   VARCHAR(255) COMMENT 'user input expression',
  `formula_type` TINYINT COMMENT '1: amount, 2: cost',
  `status`       TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`  BIGINT,
  `create_time`  DATETIME,
  `update_uid`   BIGINT,
  `update_time`  DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 公式变量
--
-- DROP TABLE IF EXISTS `eh_energy_meter_formula_variables`;
CREATE TABLE `eh_energy_meter_formula_variables` (
  `id`           BIGINT  NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `name`         VARCHAR(255) COMMENT 'variable name',
  `display_name` VARCHAR(255) COMMENT 'display name',
  `description`  VARCHAR(255) COMMENT 'description',
  `status`       TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`  BIGINT,
  `create_time`  DATETIME,
  `update_uid`   BIGINT,
  `update_time`  DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 每日水电报表
--
-- DROP TABLE IF EXISTS `eh_energy_date_statistics`;
CREATE TABLE `eh_energy_date_statistics` (
  `id`                  BIGINT  NOT NULL,
  `namespace_id`        INTEGER NOT NULL DEFAULT 0,
  `community_id`        BIGINT,
  `meter_id`            BIGINT  NOT NULL COMMENT '水表id',
  `meter_type`          TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `stat_date`            DATE COMMENT '改成用日期存,方便过滤和计算',
  `bill_category_id`    BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `formula_id`          TINYINT COMMENT 'eh_energy_meter_formulas id',
  `meter_name`          VARCHAR(255),
  `meter_number`        VARCHAR(255),
  `meter_bill`          VARCHAR(255) COMMENT 'meter bill category name',
  `meter_service`       VARCHAR(255) COMMENT 'meter service category name',
  `meter_rate`          DECIMAL(10, 2) COMMENT '表的倍率',
  `meter_price`         DECIMAL(10, 2) COMMENT '表的单价',
  `last_reading`        DECIMAL(10, 1) COMMENT '上次读数',
  `current_reading`     DECIMAL(10, 1) COMMENT '这次读数',
  `current_amount`      DECIMAL(10, 1) COMMENT '用量',
  `current_cost`        DECIMAL(10, 1) COMMENT '费用',
  `reset_meter_flag`    TINYINT COMMENT '0: normal, 1: reset',
  `change_meter_flag`   TINYINT COMMENT '0: normal, 1: change',
  `status`              TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`         BIGINT,
  `create_time`         DATETIME,
  `update_uid`          BIGINT,
  `update_time`         DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
--  每月水电报表
--
-- DROP TABLE IF EXISTS `eh_energy_month_statistics`;
CREATE TABLE `eh_energy_month_statistics` (
  `id`                  BIGINT  NOT NULL,
  `namespace_id`        INTEGER NOT NULL DEFAULT 0,
  `community_id`        BIGINT,
  `meter_id`            BIGINT  NOT NULL COMMENT '水表id',
  `meter_type`          TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `date_str`            VARCHAR(20)COMMENT  'YYYMM 例如 201608' ,
  `bill_category_id`    BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id` BIGINT COMMENT 'eh_energy_meter_categories id',
  `formula_id`          TINYINT COMMENT 'eh_energy_meter_formulas id',
  `meter_name`          VARCHAR(255),
  `meter_number`        VARCHAR(255),
  `meter_bill`          VARCHAR(255) COMMENT 'meter bill category name',
  `meter_service`       VARCHAR(255) COMMENT 'meter service category name',
  `meter_rate`          DECIMAL(10, 2),
  `meter_price`         DECIMAL(10, 2),
  `last_reading`        DECIMAL(10, 1),
  `current_reading`     DECIMAL(10, 1),
  `current_amount`      DECIMAL(10, 1),
  `current_cost`        DECIMAL(10, 1),
  `reset_meter_flag`    TINYINT COMMENT '0: normal, 1: reset',
  `change_meter_flag`   TINYINT COMMENT '0: normal, 1: change',
  `status`              TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`         BIGINT,
  `create_time`         DATETIME,
  `update_uid`          BIGINT,
  `update_time`         DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
--
-- 总量记录表
--
-- DROP TABLE IF EXISTS `eh_energy_count_statistics`;
CREATE TABLE `eh_energy_count_statistics` (
  `id`                    BIGINT  NOT NULL,
  `namespace_id`          INTEGER NOT NULL DEFAULT 0,
  `community_id`          BIGINT,
  `meter_type`            TINYINT COMMENT '1:WATER, 2: ELECTRIC',
  `date_str`              VARCHAR(20),
  `statistic_type`        TINYINT COMMENT '1:bill, 2: service, 3:burden',
  `bill_category_id`      BIGINT COMMENT 'eh_energy_meter_categories id',
  `service_category_id`   BIGINT COMMENT 'eh_energy_meter_categories id',
  `bill_category_name`    VARCHAR(255),
  `service_category_name` VARCHAR(255),
  `amount`                DECIMAL(10, 1),
  `cost`                  DECIMAL(10, 1),
  `status`                TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`           BIGINT,
  `create_time`           DATETIME,
  `update_uid`            BIGINT,
  `update_time`           DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

--
-- 各项目月水电能耗情况-同比
--
-- DROP TABLE IF EXISTS `eh_energy_yoy_statistics`;
CREATE TABLE `eh_energy_yoy_statistics` (
  `id`                BIGINT  NOT NULL,
  `namespace_id`      INTEGER NOT NULL DEFAULT 0,
  `community_id`      BIGINT,
  `date_str`          VARCHAR(20),
  `area_size`         DOUBLE COMMENT 'Community area size',
  `water_receivable_amount` DECIMAL(10, 1) COMMENT '水表本月应收用量',
  `water_payable_amount`    DECIMAL(10, 1) COMMENT '水表本月应付用量',
  `water_burden_amount`     DECIMAL(10, 1) COMMENT '水表本月负担公共用量',
  `water_average_amount`    DECIMAL(10, 1) COMMENT '水表本月每平米平均用量',
  `water_last_receivable_amount` DECIMAL(10, 1) COMMENT '水表去年同期应收用量',
  `water_last_payable_amount`    DECIMAL(10, 1) COMMENT '水表去年同期应付用量',
  `water_last_burden_amount`     DECIMAL(10, 1) COMMENT '水表去年同期负担公共用量',
  `water_last_average_amount`    DECIMAL(10, 1) COMMENT '水表去年同期每平米平均用量',
  `electric_receivable_amount` DECIMAL(10, 1) COMMENT '电表应收用量',
  `electric_payable_amount`    DECIMAL(10, 1) COMMENT '电表应付用量',
  `electric_burden_amount`     DECIMAL(10, 1) COMMENT '电表负担公共用量',
  `electric_average_amount`    DECIMAL(10, 1) COMMENT '电表每平米平均用量',
  `electric_last_receivable_amount` DECIMAL(10, 1) COMMENT '电表去年同期应收用量',
  `electric_last_payable_amount`    DECIMAL(10, 1) COMMENT '电表去年同期应付用量',
  `electric_last_burden_amount`     DECIMAL(10, 1) COMMENT '电表去年同期负担公共用量',
  `electric_last_average_amount`    DECIMAL(10, 1) COMMENT '电表去年同期每平米平均用量',
  `status`            TINYINT COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid`       BIGINT,
  `create_time`       DATETIME,
  `update_uid`        BIGINT,
  `update_time`       DATETIME,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 圈添加审核状态，add by tt, 20161028
ALTER TABLE `eh_groups` ADD COLUMN `approval_status` TINYINT COMMENT 'approval status';
ALTER TABLE `eh_groups` ADD COLUMN `operator_uid` BIGINT;

-- 设置圈（俱乐部）参数，add by tt, 20161028
-- DROP TABLE IF EXISTS `eh_group_settings`;
CREATE TABLE `eh_group_settings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `create_flag` TINYINT COMMENT 'whether allow create club',
  `verify_flag` TINYINT COMMENT 'whether need verify',
  `member_post_flag` TINYINT COMMENT 'whether allow members create post',
  `member_comment_flag` TINYINT COMMENT 'whether allow members comment on the post',
  `admin_broadcast_flag` TINYINT COMMENT 'whether allow admin broadcast',
  `broadcast_count` INTEGER COMMENT 'how many broadcasts can be created per day',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'news creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 广播消息，add by tt, 20161028
-- DROP TABLE IF EXISTS `eh_broadcasts`;
CREATE TABLE `eh_broadcasts` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(32) COMMENT 'group',
  `owner_id` BIGINT DEFAULT 0 COMMENT 'group_id',
  `title` VARCHAR(1024) COMMENT 'title',
  `content_type` VARCHAR(32) COMMENT 'object content type: text、rich text',
  `content` LONGTEXT COMMENT 'content data, depends on value of content_type',
  `content_abstract` TEXT COMMENT 'abstract of content data',
  `creator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'creator uid',
  `create_time` DATETIME COMMENT 'create time',
  `operator_uid` BIGINT NOT NULL DEFAULT 0 COMMENT 'operator uid',
  `update_time` DATETIME COMMENT 'update time',
  
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- 版本url表添加图标的链接地址, add by tt, 20161107
ALTER TABLE `eh_version_urls`	ADD COLUMN `icon_url` VARCHAR(50);





