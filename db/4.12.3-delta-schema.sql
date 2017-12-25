-- 工作流日志加会签标志字段 add by xq.tian 2017/12/25
ALTER TABLE eh_flow_event_logs ADD enter_log_complete_flag TINYINT NOT NULL DEFAULT 0;


-- 意见反馈增加一个字段“业务自定义参数”  add by yanjun 20171220
ALTER TABLE `eh_feedbacks` ADD COLUMN `target_param`  text NULL AFTER `target_id`;