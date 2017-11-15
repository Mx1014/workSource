-- 加表单关联字段
ALTER TABLE eh_flows ADD COLUMN `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form id';
ALTER TABLE eh_flows ADD COLUMN `form_version` BIGINT NOT NULL DEFAULT 1 COMMENT 'flow ref form version';
ALTER TABLE eh_flows ADD COLUMN `form_update_time` DATETIME COMMENT 'form update time';

ALTER TABLE eh_flow_event_logs ADD COLUMN `extra` TEXT COMMENT 'extra data, json format';

ALTER TABLE eh_flow_condition_expressions ADD COLUMN `variable_extra1` VARCHAR(256) COMMENT 'variable 1 extra';
ALTER TABLE eh_flow_condition_expressions ADD COLUMN `variable_extra2` VARCHAR(256) COMMENT 'variable 2 extra';