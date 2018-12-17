
-- 迁移旧的数据适应新的代码
UPDATE eh_flows SET form_status = 1 WHERE form_origin_id<>0;

UPDATE eh_flow_nodes SET form_relation_type=1 WHERE form_origin_id<>0;