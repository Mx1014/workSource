--
-- 表记   add by xq.tian  2016/10/25
--
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
);

--
-- 抄表记录
--
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
);

--
-- 换表记录
--
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
);

--
-- 表记分类属性
--
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
);

--
-- 表记属性 费率表
--
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
);

--
-- 默认显示的属性值
--
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
);

--
-- 表记计算公式
--
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
);

--
-- 公式变量
--
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
);

--
-- 每日水电报表
--
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
);

--
--  每月水电报表
--
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
);
--
-- 总量记录表
--
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
);

--
-- 各项目月水电能耗情况-同比
--
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
);

