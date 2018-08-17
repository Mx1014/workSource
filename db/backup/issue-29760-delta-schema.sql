-- Designer: zhang zhiwei
-- Description: ISSUE#29760: 考勤5.0 - 请假规则

ALTER TABLE eh_approval_categories ADD COLUMN time_unit VARCHAR(10) NOT NULL DEFAULT 'DAY' COMMENT '请假单位，DAY:天，HOUR:小时' AFTER category_name;
ALTER TABLE eh_approval_categories ADD COLUMN time_step DECIMAL(6,2) NOT NULL DEFAULT 0.5 COMMENT '最小请假时长' AFTER time_unit;
ALTER TABLE eh_approval_categories ADD COLUMN remainder_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用余额设置，0不支持，1未启用，2启用' AFTER time_step;
ALTER TABLE eh_approval_categories ADD COLUMN origin_id BIGINT COMMENT '旧版本的请假类型是公共的,即共用namespace_id=0，该字段用于兼容这部分数据，将旧数据的id关联过来' AFTER remainder_flag;
ALTER TABLE eh_approval_categories CHANGE COLUMN STATUS STATUS TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-已删除 1 不启用 2 启用 3 不可禁用';
ALTER TABLE eh_approval_categories ADD COLUMN default_order INTEGER NOT NULL DEFAULT 0 COMMENT '排序' AFTER STATUS;
ALTER TABLE eh_approval_categories ADD COLUMN hander_type VARCHAR(32) COMMENT '和处理逻辑对应的名称' AFTER STATUS;

ALTER TABLE eh_approval_categories ADD INDEX i_eh_namespace_owner_id(`namespace_id`,`owner_type`,`owner_id`);



CREATE TABLE `eh_approval_category_init_logs` (
  `id` BIGINT NOT NULL COMMENT '主键',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间',
  `owner_type` VARCHAR(64) NOT NULL COMMENT '默认organization',
  `owner_id` BIGINT NOT NULL COMMENT 'owner_type对应的ID',
  `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
  `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_owner_id` (`namespace_id`,`owner_type`,`owner_id`)
) ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='记录每个公司是否已经初始化了请假列表，避免重复初始化';

-- 新增历史已请年假总和、调休总和（上线时做一次数据初始化）
ALTER TABLE eh_punch_vacation_balances ADD COLUMN annual_leave_history_count DECIMAL(10,4) NOT NULL DEFAULT 0 COMMENT '已请年假总和，单位天' AFTER annual_leave_balance;
ALTER TABLE eh_punch_vacation_balances ADD COLUMN overtime_compensation_history_count DECIMAL(10,4) NOT NULL DEFAULT 0 COMMENT '已请调休总和，单位天' AFTER overtime_compensation_balance;

CREATE TABLE `eh_punch_log_files` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user''s id',
  `enterprise_id` BIGINT DEFAULT NULL COMMENT 'compay id',
  `longitude` DOUBLE DEFAULT NULL,
  `latitude` DOUBLE DEFAULT NULL,
  `punch_date` DATE DEFAULT NULL COMMENT 'user punch date',
  `punch_time` DATETIME DEFAULT NULL COMMENT 'user check time',
  `punch_status` TINYINT DEFAULT NULL COMMENT '1:Normal, 0:Not in punch area',
  `identification` VARCHAR(255) DEFAULT NULL COMMENT 'unique identification for a phone',
  `punch_type` TINYINT DEFAULT '0' COMMENT '0- 上班打卡 ; 1- 下班打卡',
  `punch_interval_no` INT DEFAULT '1' COMMENT '第几次排班的打卡',
  `rule_time` BIGINT DEFAULT NULL COMMENT '规则设置的该次打卡时间',
  `status` TINYINT DEFAULT NULL COMMENT '打卡状态 0-正常 1-迟到 2-早退 3-缺勤 14-缺卡',
  `approval_status` TINYINT DEFAULT NULL COMMENT '校正后的打卡状态 0-正常 null-没有异常校准',
  `smart_alignment` TINYINT DEFAULT '0' COMMENT '只能校准状态 0-非校准 1-校准',
  `wifi_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的WiFi信息',
  `location_info` VARCHAR(1024) DEFAULT NULL COMMENT '打卡用到的地址定位',
  `should_punch_time` BIGINT DEFAULT NULL COMMENT '应该打卡时间(用以计算早退迟到时长)',
  `month_report_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_month_reports id',
  `filer_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `file_time` DATETIME DEFAULT NULL,
  `filer_Name` VARCHAR(128) DEFAULT NULL COMMENT '归档者姓名',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT'打卡的归档表';


CREATE TABLE `eh_punch_day_log_files` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user''s id',
  `enterprise_id` BIGINT DEFAULT NULL COMMENT 'compay id',
  `punch_date` DATE DEFAULT NULL COMMENT 'user punch date',
  `arrive_time` TIME DEFAULT NULL,
  `leave_time` TIME DEFAULT NULL,
  `work_time` TIME DEFAULT NULL COMMENT 'how long did employee work',
  `status` TINYINT DEFAULT '1' COMMENT 'NORMAL(0)BELATE(1)LEAVEEARLY(2)UNPUNCH(3)BLANDLE(4)ABSENCE(5)SICK(6)EXCHANGE(7)',
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `view_flag` TINYINT NOT NULL DEFAULT '1' COMMENT 'is view(0) not view(1)',
  `morning_status` TINYINT DEFAULT '1' COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `afternoon_status` TINYINT DEFAULT '1' COMMENT 'NORMAL(0), BELATE(1), LEAVEEARLY(2), UNPUNCH(3), BLANDLE(4), ABSENCE(5), SICK(6), EXCHANGE(7)',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT '2' COMMENT '2 or 4 times',
  `noon_leave_time` TIME DEFAULT NULL,
  `afternoon_arrive_time` TIME DEFAULT NULL,
  `exception_status` TINYINT DEFAULT NULL COMMENT '异常状态: 0-正常;1-异常',
  `device_change_flag` TINYINT DEFAULT '0' COMMENT '0- unchange 1-changed',
  `status_list` VARCHAR(120) DEFAULT NULL COMMENT '多次打卡的状态用/分隔 example: 1 ; 1/13 ; 13/3/4 ',
  `punch_count` INT DEFAULT NULL COMMENT '打卡次数',
  `punch_organization_id` BIGINT DEFAULT NULL,
  `rule_type` TINYINT DEFAULT '0' COMMENT '0- 排班制 ; 1- 固定班次',
  `time_rule_name` VARCHAR(64) DEFAULT NULL COMMENT '排班规则名称',
  `time_rule_id` BIGINT DEFAULT NULL COMMENT '排班规则id',
  `approval_status_list` VARCHAR(120) DEFAULT NULL COMMENT '1-未审批 0-审批正常 例如:0/1;1/1/0/1',
  `smart_alignment` VARCHAR(128) DEFAULT NULL COMMENT '智能校准状态:1-未智能校准 0-未校准 例如:0;1/0;1/1/0/1',
  `month_report_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_month_reports id',
  `filer_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `file_time` DATETIME DEFAULT NULL,
  `filer_Name` VARCHAR(128) DEFAULT NULL COMMENT '归档者姓名',
  PRIMARY KEY (`id`),
  KEY `i_eh_user_id` (`user_id`),
  KEY `i_eh_enterprise_user_punch_date` (`enterprise_id`,`user_id`,`punch_date`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT'打卡日统计的归档表';


CREATE TABLE `eh_punch_statistic_files` (
  `id` BIGINT NOT NULL COMMENT 'id',
  `punch_month` VARCHAR(8) DEFAULT NULL COMMENT 'yyyymm',
  `owner_type` VARCHAR(128) DEFAULT NULL COMMENT 'owner resource(user/organization) type',
  `owner_id` BIGINT DEFAULT NULL COMMENT 'owner resource(user/organization) id',
  `user_id` BIGINT DEFAULT NULL COMMENT 'user id',
  `user_name` VARCHAR(128) DEFAULT NULL COMMENT 'user name',
  `dept_id` BIGINT DEFAULT NULL COMMENT 'user department id',
  `dept_name` VARCHAR(128) DEFAULT NULL COMMENT 'user department name',
  `work_day_count` INT DEFAULT NULL COMMENT '应上班天数',
  `work_count` DOUBLE DEFAULT NULL COMMENT '实际上班天数',
  `belate_count` INT DEFAULT NULL COMMENT '迟到次数',
  `leave_early_count` INT DEFAULT NULL COMMENT '早退次数',
  `blandle_count` INT DEFAULT NULL COMMENT '迟到且早退次数',
  `unpunch_count` DOUBLE DEFAULT NULL COMMENT '缺勤天数',
  `absence_count` DOUBLE DEFAULT NULL COMMENT '事假天数',
  `sick_count` DOUBLE DEFAULT NULL COMMENT '病假天数',
  `exchange_count` DOUBLE DEFAULT NULL COMMENT '调休天数',
  `outwork_count` DOUBLE DEFAULT NULL COMMENT '公出天数',
  `over_time_sum` BIGINT DEFAULT NULL COMMENT '加班累计(非工作日上班)',
  `punch_times_per_day` TINYINT NOT NULL DEFAULT '2' COMMENT 'how many times should punch everyday :2/4',
  `exception_status` TINYINT DEFAULT NULL COMMENT '异常状态: 0-正常;1-异常',
  `description` VARCHAR(256) DEFAULT NULL,
  `creator_uid` BIGINT DEFAULT NULL,
  `create_time` DATETIME DEFAULT NULL,
  `exts` VARCHAR(1024) DEFAULT NULL COMMENT 'json string exts:eq[{"name":"事假","timeCount":"1天2小时"},{"name":"丧假","timeCount":"3天2小时30分钟"}]',
  `user_status` TINYINT DEFAULT '0' COMMENT '0:normal普通 1:NONENTRY未入职 2:RESIGNED已离职',
  `punch_org_name` VARCHAR(64) DEFAULT NULL COMMENT '所属规则-考勤组',
  `detail_id` BIGINT DEFAULT NULL COMMENT '用户detailId',
  `exception_day_count` INT DEFAULT NULL COMMENT '异常天数',
  `annual_leave_balance` DOUBLE DEFAULT NULL COMMENT '年假余额',
  `overtime_compensation_balance` DOUBLE DEFAULT NULL COMMENT '调休余额',
  `device_change_counts` INT DEFAULT NULL COMMENT '设备异常次数',
  `exception_request_counts` INT DEFAULT NULL COMMENT '异常申报次数',
  `belate_time` BIGINT DEFAULT NULL COMMENT '迟到时长(毫秒数)',
  `leave_early_time` BIGINT DEFAULT NULL COMMENT '早退时长(毫秒数)',
  `forgot_count` INT DEFAULT NULL COMMENT '下班缺卡次数',
  `status_list` TEXT COMMENT '校正后状态列表(月初到月末)',
  `month_report_id` BIGINT DEFAULT NULL COMMENT 'eh_punch_month_reports id',
  `filer_uid` BIGINT DEFAULT NULL COMMENT '创建者',
  `file_time` DATETIME DEFAULT NULL,
  `filer_Name` VARCHAR(128) DEFAULT NULL COMMENT '归档者姓名',
  PRIMARY KEY (`id`),
  KEY `i_eh_report_id` (`month_report_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT'打卡月统计的归档表';

-- 打卡表加一个设备异常的字段
ALTER TABLE `eh_punch_logs` ADD COLUMN `device_change_flag` TINYINT DEFAULT '0' COMMENT '0- unchange 1-changed';
ALTER TABLE `eh_punch_log_files` ADD COLUMN `device_change_flag` TINYINT DEFAULT '0' COMMENT '0- unchange 1-changed';

-- 新增当日迟到时长，早退时长时间总计
ALTER TABLE eh_punch_day_logs ADD COLUMN belate_time_total BIGINT NOT NULL DEFAULT 0 COMMENT '当天迟到时长总计，单位毫秒';
ALTER TABLE eh_punch_day_logs ADD COLUMN leave_early_time_total BIGINT NOT NULL DEFAULT 0 COMMENT '当天早退时长总计，单位毫秒';
ALTER TABLE eh_punch_day_log_files ADD COLUMN belate_time_total BIGINT NOT NULL DEFAULT 0 COMMENT '当天迟到时长总计，单位毫秒';
ALTER TABLE eh_punch_day_log_files ADD COLUMN leave_early_time_total BIGINT NOT NULL DEFAULT 0 COMMENT '当天早退时长总计，单位毫秒';

