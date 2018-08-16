-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.1（展示能耗数据）
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `energy_consume` VARCHAR(1024) COMMENT '能耗用量';

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.1 新增账单上传附件
CREATE TABLE `eh_payment_bill_attachments` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `category_id` BIGINT NOT NULL DEFAULT 0 COMMENT '多入口应用id，对应应用的origin_id',
  `owner_id` BIGINT,
  `owner_type` VARCHAR(64),
  `bill_id` BIGINT NOT NULL DEFAULT 0,
  `bill_group_id` BIGINT,
  `filename` VARCHAR(1024) COMMENT '附件名称',
  `content_type` VARCHAR(64) COMMENT '附件类型：word/pdf/png...',
  `content_uri` VARCHAR(1024) COMMENT '附件uri',
  `content_url` VARCHAR(1024) COMMENT '附件url',
  `creator_uid` BIGINT COMMENT '创建者ID',
  `create_time` DATETIME,
  `update_time` DATETIME ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;


-- 通用脚本
-- add by yanlong.liang 20180713
-- 帖子和活动表增加最低限制人数
ALTER TABLE `eh_forum_posts` ADD COLUMN `min_quantity` INT(11) COMMENT '最低限制人数';
ALTER TABLE `eh_activities` ADD COLUMN `min_quantity` INT(11) COMMENT '最低限制人数';
-- END BY yanlong.liang

-- 通用脚本
-- add by yanlong.liang 20180719
-- 导出中心增加阅读状态和下载次数
ALTER TABLE `eh_tasks` ADD COLUMN `read_status` TINYINT(4) COMMENT '阅读状态';
ALTER TABLE `eh_tasks` ADD COLUMN `download_times` INT(11) COMMENT '下载次数';
-- end

-- 通用脚本
-- AUTHOR: dengs
-- REMARK: 访客管理 管理者消息接受表 , add by dengs, 20180425
CREATE TABLE `eh_visitor_sys_message_receivers` (
  `id` BIGINT NOT NULL COMMENT 'id of the record',
  `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT 'namespace id',
  `owner_type` VARCHAR(64) NOT NULL COMMENT 'community or organization',
  `owner_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'ownerType为community时候，为园区id;ownerType为organization时候，为公司id',
  `creator_uid` BIGINT COMMENT '创建者/访客管理者id',
  `create_time` DATETIME COMMENT '事件发生时间',
  `operator_uid` BIGINT,
  `operate_time` DATETIME,

  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT '访客管理 管理者消息接受表';

ALTER TABLE eh_visitor_sys_visitors ADD COLUMN source TINYINT DEFAULT '0' COMMENT '访客来源，0:内部录入 1:外部对接';
ALTER TABLE eh_visitor_sys_visitors ADD COLUMN notify_third_success_flag TINYINT DEFAULT '0' COMMENT '访客来源为外部对接，0：未回调到第三方 1：回调失败 2:回调成功';
-- end
-- 通用脚本
-- AUTHOR: dengs
-- REMARK: issue-34945 添加字段存储支付信息 add by dengs, 20180808
ALTER TABLE `eh_siyin_print_orders` ADD COLUMN `pay_dto` text COMMENT '支付系统返回预付单信息';

-- AUTHOR: xq.tian
-- REMARK: 工作流节点添加表单字段  ADD BY xq.tian  2018/07/11
ALTER TABLE eh_flow_nodes ADD COLUMN form_status TINYINT NOT NULL DEFAULT 0 COMMENT '0: disable, 1: enable';
ALTER TABLE eh_flow_nodes ADD COLUMN form_origin_id BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_origin_id';
ALTER TABLE eh_flow_nodes ADD COLUMN form_version BIGINT DEFAULT 0 COMMENT 'ref eh_general_forms form_version';
ALTER TABLE eh_flow_nodes ADD COLUMN form_update_time DATETIME;

ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_type1 VARCHAR(32);
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_id1 BIGINT DEFAULT 0;
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_type2 VARCHAR(32);
ALTER TABLE eh_flow_condition_expressions ADD COLUMN entity_id2 BIGINT DEFAULT 0;

-- AUTHOR: xq.tian
-- REMARK: 工作流和业务映射表  add by xq.tian  20180724
-- DROP TABLE `eh_flow_service_mappings`;
CREATE TABLE `eh_flow_service_mappings` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER NOT NULL DEFAULT 0,
  `project_type` VARCHAR(64) NOT NULL DEFAULT '',
  `project_id` BIGINT NOT NULL DEFAULT 0,
  `module_type` VARCHAR(64) NOT NULL,
  `module_id` BIGINT NOT NULL COMMENT 'the module id',
  `owner_type` VARCHAR(64) NOT NULL,
  `owner_id` BIGINT NOT NULL,
  `display_name` VARCHAR(64) NOT NULL,
  `flow_main_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'the real flow id for all copy, the first flow_main_id=0',
  `flow_version` INTEGER NOT NULL DEFAULT 0 COMMENT 'current flow version',
  `status` TINYINT NOT NULL DEFAULT 2 COMMENT '0: invalid, 1:waiting_for_approval, 2: valid',
  `create_time` DATETIME(3) NOT NULL COMMENT 'record create time',
  `creator_uid` BIGINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_goto_node_id BIGINT COMMENT 'only sub flow node, when sub flow absort go to node id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_step_type VARCHAR(32) COMMENT 'only sub flow node, when sub flow absort step type';

ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_project_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings project_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_project_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings project_id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_module_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings module_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_module_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings module_id';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_owner_type VARCHAR(32) NOT NULL DEFAULT '' COMMENT 'ref eh_flow_service_mappings owner_type';
ALTER TABLE eh_flow_nodes ADD COLUMN sub_flow_owner_id BIGINT NOT NULL DEFAULT 0 COMMENT 'ref eh_flow_service_mappings owenr_id';

ALTER TABLE eh_flow_cases ADD COLUMN sub_flow_parent_id BIGINT NOT NULL DEFAULT 0 ;
ALTER TABLE eh_flow_cases ADD COLUMN sub_flow_path VARCHAR(128) NOT NULL DEFAULT '';

ALTER TABLE eh_flows ADD COLUMN config_status TINYINT NOT NULL DEFAULT 0 COMMENT 'config status, 2: config, 3: snapshot';
-- END

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假类型 请假类型新增最小单位等字段
ALTER TABLE eh_approval_categories ADD COLUMN time_unit VARCHAR(10) NOT NULL DEFAULT 'DAY' COMMENT '请假单位，DAY:天，HOUR:小时' AFTER category_name;
ALTER TABLE eh_approval_categories ADD COLUMN time_step DECIMAL(6,2) NOT NULL DEFAULT 0.5 COMMENT '最小请假时长' AFTER time_unit;
ALTER TABLE eh_approval_categories ADD COLUMN remainder_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否启用余额设置，0不支持，1未启用，2启用' AFTER time_step;
ALTER TABLE eh_approval_categories ADD COLUMN origin_id BIGINT COMMENT '旧版本的请假类型是公共的,即共用namespace_id=0，该字段用于兼容这部分数据，将旧数据的id关联过来' AFTER remainder_flag;
ALTER TABLE eh_approval_categories CHANGE COLUMN STATUS STATUS TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-已删除 1 不启用 2 启用 3 不可禁用';
ALTER TABLE eh_approval_categories ADD COLUMN default_order INTEGER NOT NULL DEFAULT 0 COMMENT '排序' AFTER STATUS;
ALTER TABLE eh_approval_categories ADD COLUMN hander_type VARCHAR(32) COMMENT '和处理逻辑对应的名称' AFTER STATUS;
ALTER TABLE eh_approval_categories ADD INDEX i_eh_namespace_owner_id(`namespace_id`,`owner_type`,`owner_id`);

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假类型
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

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 请假类型 新增历史已请年假总和、调休总和（上线时做一次数据初始化）
ALTER TABLE eh_punch_vacation_balances ADD COLUMN annual_leave_history_count DECIMAL(10,4) NOT NULL DEFAULT 0 COMMENT '已请年假总和，单位天' AFTER annual_leave_balance;
ALTER TABLE eh_punch_vacation_balances ADD COLUMN overtime_compensation_history_count DECIMAL(10,4) NOT NULL DEFAULT 0 COMMENT '已请调休总和，单位天' AFTER overtime_compensation_balance;

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 报表归档
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

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 报表归档
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

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 报表归档
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

-- AUTHOR: 吴寒 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 打卡表加一个设备异常的字段
ALTER TABLE `eh_punch_logs` ADD COLUMN `device_change_flag` TINYINT DEFAULT '0' COMMENT '0- unchange 1-changed';
ALTER TABLE `eh_punch_log_files` ADD COLUMN `device_change_flag` TINYINT DEFAULT '0' COMMENT '0- unchange 1-changed';

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-29760: 考勤5.0 - 新增当日迟到时长，早退时长时间总计
ALTER TABLE eh_punch_day_logs ADD COLUMN belate_time_total BIGINT NOT NULL DEFAULT 0 COMMENT '当天迟到时长总计，单位毫秒';
ALTER TABLE eh_punch_day_logs ADD COLUMN leave_early_time_total BIGINT NOT NULL DEFAULT 0 COMMENT '当天早退时长总计，单位毫秒';
ALTER TABLE eh_punch_day_log_files ADD COLUMN belate_time_total BIGINT NOT NULL DEFAULT 0 COMMENT '当天迟到时长总计，单位毫秒';
ALTER TABLE eh_punch_day_log_files ADD COLUMN leave_early_time_total BIGINT NOT NULL DEFAULT 0 COMMENT '当天早退时长总计，单位毫秒';

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-32471: 考勤6.0 - 考勤加班规则表
CREATE TABLE `eh_punch_overtime_rules` (
    `id` BIGINT NOT NULL COMMENT '主键',
    `namespace_id` INTEGER NOT NULL DEFAULT 0 COMMENT '域空间ID',
    `punch_rule_id` BIGINT NOT NULL COMMENT '考勤规则Id',
    `type` VARCHAR(24) NOT NULL DEFAULT 'WORKDAY' COMMENT 'WORKDAY:工作日加班，HOLIDAY:非工作日加班',
    `name` VARCHAR(64) NOT NULL COMMENT '加班类型名称',
    `overtime_approval_type` TINYINT NOT NULL DEFAULT 1 COMMENT '类型：1、需审批，时长以打卡为准，但不能超过审批单时长；2、 需审批，时长以审批单为准；3、 无需审批，时长以打卡为准',
    `valid_interval` INTEGER NOT NULL DEFAULT 0 COMMENT '加班起算时间（有效间隔），单位分钟',
    `overtime_min_unit` INTEGER NOT NULL DEFAULT 0 COMMENT '最小加班单位（步长），单位分钟',
    `overtime_max` INTEGER NOT NULL DEFAULT 0 COMMENT '最大加班时长，单位分钟',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态:1-已删除 2-正常 3-次日更新 4-新规则次日生效',
    `creator_uid` BIGINT NOT NULL COMMENT '记录创建人userId',
    `create_time` DATETIME NOT NULL COMMENT '记录创建时间',
    `update_time` DATETIME NULL COMMENT '记录更新时间',
    `operator_uid` BIGINT NULL COMMENT '记录更新人userId',
    PRIMARY KEY (`id`),
    INDEX `i_eh_punch_rule_id` (`punch_rule_id`)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4 COMMENT='考勤加班规则';

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-32471: 考勤6.0 - 新增 工作日加班时长、休息日加班时长、节假日加班时长
ALTER TABLE eh_punch_day_logs ADD COLUMN overtime_total_workday BIGINT NOT NULL DEFAULT 0 COMMENT '工作日加班时长，单位毫秒数';
ALTER TABLE eh_punch_day_logs ADD COLUMN overtime_total_restday BIGINT NOT NULL DEFAULT 0 COMMENT '休息日加班时长，单位毫秒数';
ALTER TABLE eh_punch_day_logs ADD COLUMN overtime_total_legal_holiday BIGINT NOT NULL DEFAULT 0 COMMENT '节假日加班时长，单位毫秒数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN overtime_total_workday BIGINT NOT NULL DEFAULT 0 COMMENT '工作日加班时长，单位毫秒数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN overtime_total_restday BIGINT NOT NULL DEFAULT 0 COMMENT '休息日加班时长，单位毫秒数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN overtime_total_legal_holiday BIGINT NOT NULL DEFAULT 0 COMMENT '节假日加班时长，单位毫秒数';
ALTER TABLE eh_punch_statistics ADD COLUMN overtime_total_workday BIGINT NOT NULL DEFAULT 0 COMMENT '工作日加班时长，单位毫秒数';
ALTER TABLE eh_punch_statistics ADD COLUMN overtime_total_restday BIGINT NOT NULL DEFAULT 0 COMMENT '休息日加班时长，单位毫秒数';
ALTER TABLE eh_punch_statistics ADD COLUMN overtime_total_legal_holiday BIGINT NOT NULL DEFAULT 0 COMMENT '节假日加班时长，单位毫秒数';
ALTER TABLE eh_punch_statistic_files ADD COLUMN overtime_total_workday BIGINT NOT NULL DEFAULT 0 COMMENT '工作日加班时长，单位毫秒数';
ALTER TABLE eh_punch_statistic_files ADD COLUMN overtime_total_restday BIGINT NOT NULL DEFAULT 0 COMMENT '休息日加班时长，单位毫秒数';
ALTER TABLE eh_punch_statistic_files ADD COLUMN overtime_total_legal_holiday BIGINT NOT NULL DEFAULT 0 COMMENT '节假日加班时长，单位毫秒数';

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-32471: 考勤6.0 - 新增 节假日表增加法定假日标识
ALTER TABLE eh_punch_holidays ADD COLUMN legal_flag TINYINT DEFAULT '0' COMMENT '是否法定假日:1-是 0-否';

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 考勤异常申请新增字段按天或按分钟为单位保存申请时长
ALTER TABLE eh_punch_exception_requests CHANGE COLUMN duration duration_day DECIMAL(10,4) DEFAULT 0 COMMENT '申请时长-单位天';
ALTER TABLE eh_punch_exception_requests ADD COLUMN duration_minute BIGINT DEFAULT 0 COMMENT '申请时长-单位分钟';

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 - 新增上班缺卡次数、下班缺卡次数统计
UPDATE eh_punch_statistics SET forgot_count=0 WHERE forgot_count IS NULL;
ALTER TABLE eh_punch_statistics CHANGE COLUMN forgot_count forgot_punch_count_off_duty INTEGER NOT NULL DEFAULT 0 COMMENT '下班缺卡次数';
ALTER TABLE eh_punch_statistics ADD COLUMN forgot_punch_count_on_duty INTEGER NOT NULL DEFAULT 0 COMMENT '上班缺卡次数' AFTER forgot_punch_count_off_duty;
ALTER TABLE eh_punch_statistic_files CHANGE COLUMN forgot_count forgot_punch_count_off_duty INTEGER NOT NULL DEFAULT 0 COMMENT '下班缺卡次数';
ALTER TABLE eh_punch_statistic_files ADD COLUMN forgot_punch_count_on_duty INTEGER NOT NULL DEFAULT 0 COMMENT '上班缺卡次数' AFTER forgot_punch_count_off_duty;


-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 考勤7.0 数据统计
ALTER TABLE eh_punch_day_logs ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;
ALTER TABLE eh_punch_day_logs ADD COLUMN dept_id BIGINT COMMENT '所属部门Id' AFTER enterprise_id;
ALTER TABLE eh_punch_day_logs ADD COLUMN rest_flag TINYINT COMMENT '是否是休息日，1：是 0：否';
ALTER TABLE eh_punch_day_logs ADD COLUMN absent_flag TINYINT COMMENT '是否全天旷工，1：是 0：否';
ALTER TABLE eh_punch_day_logs ADD COLUMN normal_flag TINYINT COMMENT '全天是否出勤正常，1：是 0：否';
ALTER TABLE eh_punch_day_logs ADD COLUMN belate_count INTEGER COMMENT '当天迟到次数';
ALTER TABLE eh_punch_day_logs ADD COLUMN leave_early_count INTEGER COMMENT '早退次数';
ALTER TABLE eh_punch_day_logs ADD COLUMN forgot_punch_count_on_duty INTEGER NOT NULL DEFAULT 0 COMMENT '上班缺卡次数' AFTER leave_early_count;
ALTER TABLE eh_punch_day_logs ADD COLUMN forgot_punch_count_off_duty INTEGER NOT NULL DEFAULT 0 COMMENT '下班缺卡次数' AFTER forgot_punch_count_on_duty;
ALTER TABLE eh_punch_day_logs ADD COLUMN ask_for_leave_request_count INTEGER COMMENT '当天请假申请次数';
ALTER TABLE eh_punch_day_logs ADD COLUMN go_out_request_count INTEGER COMMENT '当天外出申请次数';
ALTER TABLE eh_punch_day_logs ADD COLUMN business_trip_request_count INTEGER COMMENT '当天出差申请次数';
ALTER TABLE eh_punch_day_logs ADD COLUMN overtime_request_count INTEGER COMMENT '当天加班申请次数';
ALTER TABLE eh_punch_day_logs ADD COLUMN punch_exception_request_count INTEGER COMMENT '当天异常打卡申请次数';
ALTER TABLE eh_punch_day_logs ADD COLUMN split_date_time DATETIME COMMENT '当天考勤时间的分界点';

ALTER TABLE eh_punch_day_log_files ADD COLUMN detail_id BIGINT COMMENT '员工 的detail Id' AFTER user_id;
ALTER TABLE eh_punch_day_log_files ADD COLUMN dept_id BIGINT COMMENT '所属部门Id' AFTER enterprise_id;
ALTER TABLE eh_punch_day_log_files ADD COLUMN rest_flag TINYINT COMMENT '是否是休息日，1：是 0：否';
ALTER TABLE eh_punch_day_log_files ADD COLUMN absent_flag TINYINT COMMENT '是否全天旷工，1：是 0：否';
ALTER TABLE eh_punch_day_log_files ADD COLUMN normal_flag TINYINT COMMENT '全天是否出勤正常，1：是 0：否';
ALTER TABLE eh_punch_day_log_files ADD COLUMN belate_count INTEGER COMMENT '当天迟到次数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN leave_early_count INTEGER COMMENT '早退次数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN forgot_punch_count_on_duty INTEGER NOT NULL  DEFAULT 0 COMMENT '上班缺卡次数' AFTER leave_early_count;
ALTER TABLE eh_punch_day_log_files ADD COLUMN forgot_punch_count_off_duty INTEGER NOT NULL DEFAULT 0 COMMENT '下班缺卡次数' AFTER forgot_punch_count_on_duty;
ALTER TABLE eh_punch_day_log_files ADD COLUMN ask_for_leave_request_count INTEGER COMMENT '当天请假申请次数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN go_out_request_count INTEGER COMMENT '当天外出申请次数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN business_trip_request_count INTEGER COMMENT '当天出差申请次数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN overtime_request_count INTEGER COMMENT '当天加班申请次数';
ALTER TABLE eh_punch_day_log_files ADD COLUMN punch_exception_request_count INTEGER COMMENT '当天异常打卡申请次数';

ALTER TABLE eh_punch_statistics ADD COLUMN rest_day_count INTEGER COMMENT '应休息天数' AFTER outwork_count;
ALTER TABLE eh_punch_statistics ADD COLUMN full_normal_flag TINYINT COMMENT '当月是否全勤' AFTER rest_day_count;
ALTER TABLE eh_punch_statistics ADD COLUMN ask_for_leave_request_count INTEGER COMMENT '当月请假申请次数' AFTER full_normal_flag;
ALTER TABLE eh_punch_statistics ADD COLUMN go_out_request_count INTEGER COMMENT '当月外出申请次数' AFTER ask_for_leave_request_count;
ALTER TABLE eh_punch_statistics ADD COLUMN business_trip_request_count INTEGER COMMENT '当月出差申请次数' AFTER go_out_request_count;
ALTER TABLE eh_punch_statistics ADD COLUMN overtime_request_count INTEGER COMMENT '当月加班申请次数' AFTER business_trip_request_count;
ALTER TABLE eh_punch_statistics ADD COLUMN punch_exception_request_count INTEGER COMMENT '当月异常打卡申请次数' AFTER overtime_request_count;

ALTER TABLE eh_punch_statistic_files ADD COLUMN rest_day_count INTEGER COMMENT '应休息天数' AFTER outwork_count;
ALTER TABLE eh_punch_statistic_files ADD COLUMN full_normal_flag TINYINT COMMENT '当月是否全勤' AFTER rest_day_count;
ALTER TABLE eh_punch_statistic_files ADD COLUMN ask_for_leave_request_count INTEGER COMMENT '当月请假申请次数' AFTER full_normal_flag;
ALTER TABLE eh_punch_statistic_files ADD COLUMN go_out_request_count INTEGER COMMENT '当月外出申请次数' AFTER ask_for_leave_request_count;
ALTER TABLE eh_punch_statistic_files ADD COLUMN business_trip_request_count INTEGER COMMENT '当月出差申请次数' AFTER go_out_request_count;
ALTER TABLE eh_punch_statistic_files ADD COLUMN overtime_request_count INTEGER COMMENT '当月加班申请次数' AFTER business_trip_request_count;
ALTER TABLE eh_punch_statistic_files ADD COLUMN punch_exception_request_count INTEGER COMMENT '当月异常打卡申请次数' AFTER overtime_request_count;

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 考勤7.0 数据统计  性能优化
ALTER TABLE eh_punch_statistics ADD INDEX i_eh_punch_month_detail_id(`punch_month`,`owner_type`,`owner_id`,`detail_id`);
ALTER TABLE eh_punch_day_logs DROP INDEX i_eh_enterprise_user_punch_date;
ALTER TABLE eh_punch_day_logs ADD INDEX i_eh_enterprise_punch_date_user_id(`enterprise_id`,`punch_date`,`user_id`);
ALTER TABLE eh_punch_day_logs ADD INDEX i_eh_enterprise_punch_date_detail_id(`enterprise_id`,`punch_date`,`detail_id`);
ALTER TABLE eh_uniongroup_member_details ADD INDEX i_eh_namespace_detail_id_version_code(`namespace_id`,`group_type`,`detail_id`,`version_code`);

-- AUTHOR: 张智伟 20180813
-- REMARK: ISSUE-33645: 考勤7.0 考勤7.0 数据统计  新增字段，记录打卡当天设置的考勤规则
ALTER TABLE eh_punch_logs ADD COLUMN `punch_organization_id` BIGINT AFTER punch_date;



-- --------------------- SECTION END ---------------------------------------------------------