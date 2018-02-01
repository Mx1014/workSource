-- 日志增加项目 jiarui
UPDATE  `eh_quality_inspection_logs`
SET scope_id =(SELECT eh_quality_inspection_standards.target_id FROM  eh_quality_inspection_standards where eh_quality_inspection_standards.id = eh_quality_inspection_logs.target_id);
-- 日志增加项目 jiarui