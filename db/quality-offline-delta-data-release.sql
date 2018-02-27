-- 日志增加项目 jiarui
UPDATE  `eh_quality_inspection_logs`
SET scope_id =(SELECT eh_quality_inspection_standards.target_id FROM  eh_quality_inspection_standards where eh_quality_inspection_standards.id = eh_quality_inspection_logs.target_id);
-- 日志增加项目 jiarui


--  重命名品质核查权限项名称
UPDATE eh_service_modules
SET name = '计划管理'
WHERE id = 20630;
UPDATE eh_service_modules
SET name = '计划审批'
WHERE id = 20640;
UPDATE eh_service_modules
SET name = '日志管理'
WHERE id = 20670;
UPDATE  eh_service_modules
SET name ='任务管理'
WHERE id = 20650;
UPDATE  eh_service_modules
SET name ='统计分析'
WHERE id = 20660;