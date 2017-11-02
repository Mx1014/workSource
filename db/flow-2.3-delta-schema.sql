-- flow 加校验状态字段   add by xq.tian  2017/10/31
ALTER TABLE ehcore.eh_flows ADD COLUMN `validation_status` TINYINT NOT NULL DEFAULT 2 COMMENT 'flow validation status';