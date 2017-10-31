-- flow 加校验状态字段   add by xq.tian  2017/10/31
ALTER TABLE eh_flows ADD COLUMN `validation_status` TINYINT NOT NULL DEFAULT 1 COMMENT 'flow validation status';


-- 加表单关联字段
ALTER TABLE eh_flows ADD COLUMN `form_origin_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'flow ref form id';
ALTER TABLE eh_flows ADD COLUMN `form_version` BIGINT NOT NULL DEFAULT 1 COMMENT 'flow ref form version';