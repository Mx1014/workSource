-- eh_flow_event_logs 加索引 add by xq.tian  2017/10/19
ALTER TABLE eh_flow_event_logs ALGORITHM=inplace, LOCK=none, ADD INDEX i_eh_namespace_id (namespace_id);
ALTER TABLE eh_flow_event_logs ALGORITHM=inplace, LOCK=none, ADD INDEX i_eh_flow_main_id (flow_main_id);
ALTER TABLE eh_flow_event_logs ALGORITHM=inplace, LOCK=none, ADD INDEX i_eh_flow_version (flow_version);
ALTER TABLE eh_flow_event_logs ALGORITHM=inplace, LOCK=none, ADD INDEX i_eh_flow_case_id (flow_case_id);
ALTER TABLE eh_flow_event_logs ALGORITHM=inplace, LOCK=none, ADD INDEX i_eh_flow_user_id (flow_user_id);
ALTER TABLE eh_flow_event_logs ALGORITHM=inplace, LOCK=none, ADD INDEX i_eh_step_count (step_count);
ALTER TABLE eh_flow_event_logs ALGORITHM=inplace, LOCK=none, ADD INDEX i_eh_log_type (log_type);