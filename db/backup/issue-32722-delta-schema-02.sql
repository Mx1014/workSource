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

-- AUTHOR: 黄良铭
-- REMARK: #31347 #33785  保存用户当前所在场景
CREATE TABLE `eh_user_current_scene` (
  `id` BIGINT(32) NOT NULL COMMENT '主键',
  `uid` BIGINT(32) NOT NULL COMMENT '用户ID',
  `namespace_id` INT(11) DEFAULT NULL COMMENT '域空间ID',
  `community_id` BIGINT(32) DEFAULT NULL COMMENT '园区ID',
  `community_type` TINYINT(4) DEFAULT NULL COMMENT '园区类型',
  `create_time` DATETIME DEFAULT NULL ,
  `update_time` DATETIME DEFAULT NULL ,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;
-- END


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


-- AUTHOR: dengs 20180816
-- REMARK: ISSUE-33347: 新增国贸快递类型，对接新支付
CREATE TABLE `eh_express_payee_accounts` (
  `id` bigint NOT NULL,
  `namespace_id` int NOT NULL,
  `owner_type` varchar(32) NOT NULL COMMENT 'community 园区或者其他类型',
  `owner_id` bigint NOT NULL COMMENT '园区id或者其他id',
  `payee_id` bigint NOT NULL COMMENT '支付帐号id',
  `payee_user_type` varchar(128) NOT NULL COMMENT '帐号类型，1-个人帐号、2-企业帐号',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '0: inactive, 2: active',
  `creator_uid` bigint DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `operator_uid` bigint DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT '快递收款账户表';

ALTER TABLE `eh_express_orders`	ADD COLUMN `flow_case_id` BIGINT COMMENT '工作流id 国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `express_type` TINYINT COMMENT '0:物品 1:文件 2:其他  国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `express_way` TINYINT COMMENT '0:陆运 1:空运  国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `express_target` TINYINT COMMENT '0:同城 1:外埠  国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `express_remark` TEXT COMMENT '备注 国贸快递专用';
ALTER TABLE `eh_express_orders`	ADD COLUMN `pay_dto` TEXT COMMENT '支付2.0下单详情';

-- AUTHOR: 唐岑 2018年8月17日10:37:50
-- REMARK: ISSUE-31926: 资产V3.1，资产管理重构
ALTER TABLE `eh_buildings` ADD COLUMN `floor_number` int(11) DEFAULT NULL COMMENT '该楼栋的楼层数目';
ALTER TABLE `eh_buildings` DROP INDEX `u_eh_community_id_name`;
ALTER TABLE `eh_buildings` ADD INDEX `u_eh_community_id_name` (`community_id`, `name`) USING BTREE ;
ALTER TABLE `eh_buildings` MODIFY COLUMN `building_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '楼栋编号';

-- AUTHOR: 唐岑 2018年8月17日10:37:50
-- REMARK: ISSUE-30697: 资产V3.2，新增拆分合并功能
CREATE TABLE `eh_address_arrangement` (
  `id` bigint(20) NOT NULL COMMENT '主键id',
  `address_id` bigint(20) DEFAULT NULL COMMENT '要执行拆分/合并计划的房源id',
  `original_id` varchar(2048) DEFAULT NULL COMMENT '被拆分的房源id或者被合并的房源id（以json数组方式存储）',
  `target_id` varchar(2048) DEFAULT NULL COMMENT '拆分后产生的房源id或者合并后产生的房源id（以json数组方式存储）',
  `operation_type` tinyint(4) DEFAULT NULL COMMENT '操作类型：拆分（0），合并（1）',
  `date_begin` datetime NOT NULL COMMENT '拆分合并计划的生效日期',
  `operation_flag` tinyint(4) DEFAULT NULL COMMENT '计划是否执行标志（0：否，1：是）',
  `status` tinyint(255) DEFAULT NULL COMMENT '计划状态',
  `namespace_id` int(11) NOT NULL DEFAULT '0' COMMENT '域空间id',
  `creator_uid` bigint(20) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_uid` bigint(20) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房源拆分/合并计划表';

ALTER TABLE `eh_addresses` ADD COLUMN `free_area` double NULL DEFAULT NULL COMMENT '可招租面积';
ALTER TABLE `eh_buildings` ADD COLUMN `free_area` double NULL DEFAULT NULL COMMENT '可招租面积';
ALTER TABLE `eh_communities` ADD COLUMN `free_area` double NULL DEFAULT NULL COMMENT '可招租面积';
ALTER TABLE `eh_addresses` ADD COLUMN `is_future_apartment` tinyint NULL DEFAULT 0 COMMENT '未来房源标记（0：否，1：是）';

-- AUTHOR: 杨崇鑫  20180724
-- REMARK: 物业缴费V6.5所需新增的字段
-- REMARK: 修改域空间发布保存相关应用配置
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `contract_originId` BIGINT(20) COMMENT '合同管理应用的originId';
ALTER TABLE `eh_asset_module_app_mappings` ADD COLUMN `contract_changeFlag` TINYINT COMMENT '是否走合同变更，1、0';
-- REMARK: 修改域空间发布保存相关应用配置
ALTER TABLE `eh_payment_charging_item_scopes` ADD COLUMN `tax_rate` DECIMAL(10,2) COMMENT '税率';
-- REMARK: 账单表：增加应收不含税字段,税额字段：tax_amount
ALTER TABLE `eh_payment_bills` ADD COLUMN `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）' after amount_receivable;
ALTER TABLE `eh_payment_bills` ADD COLUMN `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）' after amount_received;
ALTER TABLE `eh_payment_bills` ADD COLUMN `amount_owed_without_tax` DECIMAL(10,2) COMMENT '待收（不含税）' after amount_owed;
ALTER TABLE `eh_payment_bills` ADD COLUMN `tax_amount` DECIMAL(10,2) COMMENT '税额' after amount_receivable_without_tax;

ALTER TABLE `eh_payment_bill_items` ADD COLUMN `amount_receivable_without_tax` DECIMAL(10,2) COMMENT '应收（不含税）' after amount_receivable;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `amount_received_without_tax` DECIMAL(10,2) COMMENT '已收（不含税）' after amount_received;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `amount_owed_without_tax` DECIMAL(10,2) COMMENT '待收（不含税）' after amount_owed;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `tax_amount` DECIMAL(10,2) COMMENT '税额' after amount_receivable_without_tax;
ALTER TABLE `eh_payment_bill_items` ADD COLUMN `tax_rate` DECIMAL(10,2) COMMENT '税率' after tax_amount;

-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费V6.3 （欠费时间筛选、费项进多账单组）
ALTER TABLE `eh_payment_bills` ADD COLUMN `due_day_count` BIGINT COMMENT '欠费天数' after due_day_deadline;
-- REMARK: 因为新增计价条款时先选组再选费项，故查看合同概览时在收费项目前多加一列字段。概览中前4个Tab均需要在收费项目前多加一列账单组。
ALTER TABLE `eh_contract_charging_items` ADD COLUMN `bill_group_id` BIGINT COMMENT '账单组ID';
ALTER TABLE `eh_contract_charging_changes` ADD COLUMN `bill_group_id` BIGINT COMMENT '账单组ID';

-- AUTHOR: 丁建民
-- REMARK: 合同2.8
ALTER TABLE `eh_contracts` ADD COLUMN `cost_generation_method`  tinyint DEFAULT NULL COMMENT '合同截断时的费用收取方式，0：按计费周期，1：按实际天数';


-- ------------------------------
-- 每日统计表     add by mingbo.huang  2018/07/25
-- ------------------------------
CREATE TABLE `eh_alliance_stat` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `type` bigint(20) NOT NULL COMMENT '服务联盟类型id',
  `owner_id` bigint(20) NOT NULL COMMENT '所属项目id',
  `category_id` bigint(20) NOT NULL COMMENT '服务类型id',
  `service_id` bigint(20) DEFAULT NULL COMMENT '服务id',
  `click_type` tinyint(4) NOT NULL COMMENT '点击类型： 3-进入详情 4-点击提交 5-点击咨询 6-点击分享 20-提交申请',
  `click_count` bigint(20) NOT NULL DEFAULT '0' COMMENT '点击总数/提交申请次数',
  `click_date` date NOT NULL COMMENT '点击日期',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '该记录创建时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_click_date` (`click_date`),
  KEY `i_eh_service_id` (`service_id`),
  KEY `i_eh_category_id` (`category_id`),
  KEY `i_eh_owner_id` (`owner_id`),
  KEY `i_eh_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='统计各个服务每天的各类型用户行为点击数。';

-- ------------------------------
-- 用户点击明细表     add by mingbo.huang  2018/07/25
-- ------------------------------
CREATE TABLE `eh_alliance_stat_details` (
  `id` bigint(20) NOT NULL,
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `type` bigint(20) NOT NULL COMMENT '服务联盟类型id',
  `owner_id` bigint(20) NOT NULL COMMENT '所属项目id',
  `category_id` bigint(20) NOT NULL COMMENT '服务类型id',
  `service_id` bigint(20) DEFAULT NULL COMMENT '服务id',
  `user_id` bigint(20) NOT NULL,
  `user_name` varchar(64) DEFAULT NULL,
  `user_phone` varchar(20) DEFAULT NULL,
  `click_type` tinyint(4) NOT NULL COMMENT '点击类型：1-首页点击服务 3-进入详情 4-点击提交 5-点击咨询 6-点击分享',
  `click_time` bigint(20) NOT NULL COMMENT '点击时间戳',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录生成时间',
  PRIMARY KEY (`id`),
  KEY `i_eh_service_id` (`service_id`),
  KEY `i_eh_category_id` (`category_id`),
  KEY `i_eh_click_time` (`click_time`),
  KEY `i_eh_owner_id` (`owner_id`),
  KEY `i_eh_type` (`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户的点击明细';

-- AUTHOR: huangpengyu  20180709
-- REMARK: 此功能将所有不需要走审批的表单字段关联起来
create table `eh_general_form_val_requests`
(
   `id`                   bigint not null,
   `organization_id`      bigint comment 'organization_id',
   `owner_id`             bigint comment 'owner_id',
   `owner_type`           varchar(64) comment 'owner_type',
   `namespace_id`         int comment 'namespace_id',
   `module_id`            bigint comment 'module_id',
   `module_type`          varchar(64) comment 'module_type',
   `source_id`            bigint comment 'source_id',
   `source_type`          varchar(64) comment 'source_type',
	 `approval_status`      tinyint comment '该表单的审批状态,0-待发起，1-审批中，2-审批通过，3-审批终止' default 0,
	 `status`               tinyint comment '该表单的状态，0-删除，1-生效' default 1,
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_general_form_val_requests in dev mode';

-- AUTHOR: huangpengyu  20180717
-- REMARK: 将表单筛选字段与客户关联起来
create table `eh_general_form_filter_user_map`
(
   `id`                   bigint not null,
   `owner_id`             bigint comment 'owner_id' not null,
   `owner_type`           varchar(64) comment 'owner_type',
   `namespace_id`         int comment 'namespace_id' not null,
   `module_id`            bigint comment 'module_id' not null,
   `module_type`          varchar(64) comment 'module_type' ,
	 `form_origin_id`				bigint comment '关联的表id' not null,
	 `form_version`					bigint comment '关联的表version' not null,
   `field_name`           varchar(64) comment '被选中的字段名' not null,
	 `user_uuid`						varchar(128) comment '当前登录的用户用于获取字段' not null,
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_general_form_filter_user_map in dev mode';


alter table eh_enterprise_customers add aptitude_flag_item_id BIGINT null comment '0-无资质，1-有资质' default 0;

alter table eh_general_forms add operator_name varchar(64) null comment '修改人';
alter table eh_general_forms add creater_name varchar(64) null comment '新增人';


alter table eh_general_approvals add creater_name varchar(64) null comment '新增人';

-- AUTHOR: huangpengyu  20180811
-- REMARK: 增加合同过滤客户配置项
create table `eh_enterprise_customer_aptitude_flag`
(
   `id`                   bigint not null,
   `value`             		TINYINT not null comment '是否筛选，1-筛选，0-不筛选' default 0,
	 `owner_id`           	bigint not null comment 'communityId',
   `owner_type`           varchar(64) comment 'owner_type',
   `namespace_id`         int comment 'namespace_id',
   primary key (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT 'eh_enterprise_customer_aptitude_flag in dev mode';


-- AUTHOR: 吴寒
-- REMARK: ISSUE-33577 增加update_time 给punch_logs表(为金蝶对接接口提供)
ALTER TABLE eh_punch_logs ADD COLUMN `update_date` DATE ;


-- AUTHOR: 黄鹏宇 2018-8-13
-- REMARK: 增加导入错误日志表
CREATE TABLE `eh_sync_data_errors` (
  `id` BIGINT NOT NULL,
  `namespace_id` INTEGER,
  `module_id` BIGINT,
  `sync_type` VARCHAR(64) NOT NULL COMMENT '同步类型，对应同步的任务类型，如sync_contract',
  `owner_id` BIGINT NOT NULL COMMENT '错误对应的id，如：contractId，对应同步的任务类型',
  `owner_type` VARCHAR(64),
  `error_message` VARCHAR(512) NOT NULL COMMENT '发生错误的信息',
  `task_id` BIGINT NOT NULL COMMENT '同步版本',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- AUTHOR: 郑思挺
-- REMARK: 资源预约3.6.2
ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `remark_flag`  tinyint(4) NULL COMMENT '备注是否必填 0否 1是' AFTER `overtime_strategy`,
ADD COLUMN `remark`  varchar(255) NULL COMMENT '备注显示文案' AFTER `remark_flag`;

ALTER TABLE `eh_rentalv2_time_interval`
ADD COLUMN `amorpm`  tinyint(4) NULL AFTER `time_step`,
ADD COLUMN `name`  varchar(10) NULL AFTER `amorpm`;


CREATE TABLE `eh_rentalv2_refund_tips` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int NOT NULL ,
`source_type`  varchar(20) NULL ,
`source_id`  bigint(20) NULL ,
`refund_strategy`  tinyint(4) NULL ,
`tips`  varchar(255) NULL ,
`resource_type`  varchar(20) NULL ,
PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

-- AUTHOR: 郑思挺
-- REMARK: 资源预约3.6
ALTER TABLE eh_rentalv2_resource_pics rename eh_rentalv2_site_resources;
ALTER TABLE `eh_rentalv2_site_resources`
ADD COLUMN `type`  varchar(64) NULL DEFAULT 'pic' AFTER `owner_type`;
ALTER TABLE `eh_rentalv2_site_resources`
ADD COLUMN `name`  varchar(64) NULL  AFTER `type`;
ALTER TABLE `eh_rentalv2_site_resources`
ADD COLUMN `size`  varchar(64) NULL  AFTER `name`;

ALTER TABLE `eh_rentalv2_cells`
ADD COLUMN `cell_id`  bigint(20) NULL AFTER `id`;

-- AUTHOR: 吴寒
-- REMARK: 锁掌柜门禁对接
ALTER TABLE `eh_aclinks` CHANGE `string_tag1` `string_tag1` VARCHAR(1024) ;


-- AUTHOR: 邓爽 2018年7月5日22:04:25
-- REMARK: issue-26616 停车缴费V6.6（UE优化）
ALTER TABLE `eh_parking_lots` ADD COLUMN `func_list` TEXT COMMENT '此停车场对接的功能列表Json,如["tempfee","monthRecharge"]';

-- AUTHOR: 郑思挺
-- REMARK: 装修1.0
CREATE TABLE `eh_decoration_requests` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`community_id`  bigint(20) NOT NULL ,
`create_time`  datetime NULL  ,
`start_time`  datetime NULL  ,
`end_time`  datetime NULL  ,
`apply_uid`  bigint(20) NULL ,
`apply_name`  varchar(64) NULL ,
`apply_phone`  varchar(64) NULL ,
`apply_company`  varchar(255) NULL ,
`address`  varchar(255) NULL ,
`decorator_uid`  bigint(20) NULL ,
`decorator_name`  varchar(64) NULL ,
`decorator_phone`  varchar(64) NULL ,
`decorator_company_id`  bigint(20) NULL ,
`decorator_company`  varchar(255) NULL ,
`decorator_qrid`  varchar(255) NULL COMMENT '二维码id' ,
`status`  tinyint NULL ,
`cancel_flag`  tinyint NULL COMMENT '0未取消 1工作流取消 2后台取消' ,
`cancel_reason`  varchar(1024) NULL ,
`refound_amount`  DECIMAL(18,2) NULL COMMENT '退款金额' ,
`refound_comment`  varchar(1024) NULL COMMENT '退款备注',
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_workers` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`request_id`  bigint(20) NOT NULL ,
`worker_type`  varchar(64) NULL ,
`uid`  bigint(20) NULL ,
`name`  varchar(64) NULL ,
`phone`  varchar(64) NULL ,
`image`  varchar(255) NULL ,
`qrid`  varchar(255) NULL COMMENT '二维码id' ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_setting` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`community_id`  bigint(20) NOT NULL ,
`owner_type`  varchar(64) NOT NULL COMMENT '\'basic\' 基础设置 \'file\'装修资料 \'fee\'缴费 \'apply\'施工申请 \'complete\'竣工验收 \'refound\'押金退回' ,
`owner_id`  bigint(20) NULL COMMENT '当owner_type为apply 时 表示审批id' ,
`content`  text NULL ,
`address`  varchar(255) NULL COMMENT '收款地址或资料提交地址' ,
`longitude` DOUBLE,
`latitude` DOUBLE,
`phone`  varchar(64) NULL COMMENT '咨询电话' ,
`create_time`  datetime NULL  ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_atttachment` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`setting_id`  bigint(20) NOT NULL ,
`name`  varchar(64) NULL ,
`attachment_type`  varchar(64) NULL COMMENT '\'file\'文件 \'fee\'费用' ,
`size`  varchar(32) NULL ,
`file_uri`  varchar(255) NULL ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_fee` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`request_id`  bigint(20) NOT NULL ,
`fee_name`  varchar(64) NULL ,
`fee_price`  varchar(64) NULL ,
`amount`  varchar(64) NULL ,
`total_price`  decimal(20,2) NULL ,
`create_time`  datetime NULL  ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_companies` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`organization_id`  bigint(20) NULL ,
`name`  varchar(64) NULL ,
`create_time`  datetime NULL  ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_company_chiefs` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`company_id`  bigint(20) NOT NULL COMMENT '装修公司的id',
`name`  varchar(64) NULL ,
`phone`  varchar(64) NULL ,
`uid`   bigint(20) NULL ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_decoration_approval_vals` (
`id`  bigint(20) NOT NULL ,
`namespace_id`  int(11) NOT NULL ,
`request_id`  bigint(20) NULL ,
`approval_id`  bigint(20) NULL ,
`approval_name`  varchar(64) NULL ,
`flow_case_id`  bigint(20) NULL ,
`form_origin_id`  bigint(20) NULL ,
`form_version`  bigint(20) NULL ,
`delete_flag`  tinyint NULL COMMENT '0未取消 1取消' ,
`create_time`  datetime NULL ON UPDATE CURRENT_TIMESTAMP ,
PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- AUTHOR: 严军 2018年08月17日19:22:25
-- REMARK: issue-31049 域空间配置V1.4
ALTER TABLE `eh_portal_layout_templates` ADD COLUMN `type`  tinyint(4) NULL COMMENT '1-渐变导航栏（服务广场），2-自定义门户（二级门户），3-分页签门户（社群Tab）';

ALTER TABLE `eh_portal_layouts` ADD COLUMN `type`  tinyint(4) NULL COMMENT '1-渐变导航栏（服务广场），2-自定义门户（二级门户），3-分页签门户（社群Tab）';

ALTER TABLE `eh_portal_layouts` ADD COLUMN `index_flag`  tinyint(4) NULL DEFAULT NULL COMMENT 'index flag, 0-no, 1-yes';

-- AUTHOR: 唐岑
-- REMARK: 资产管理V3.1 2018年8月23日16:22:55
-- REMARK: 解决issue-36278，房源管理查询速度慢的问题，同时向eh_addresses表中补充building_id、community_name两个字段
ALTER TABLE `eh_addresses` ADD COLUMN `building_id` bigint(20) NULL DEFAULT NULL COMMENT '房源所在楼宇id' AFTER `community_id`;

ALTER TABLE `eh_addresses` ADD COLUMN `community_name` varchar(64) NULL DEFAULT NULL COMMENT '房源所在园区名称' AFTER `community_id`;

-- AUTHOR: 黄良铭 2018年08月23日
-- REMARK: #36462
ALTER TABLE eh_app_urls MODIFY COLUMN logo_url VARCHAR(1024) ;
-- --------------------- SECTION END ---------------------------------------------------------