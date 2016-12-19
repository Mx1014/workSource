
-- 资源预订工作流模板，add by wh, 20161219
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
VALUES (@id:=@id+1, 'approval.flow', 1, 'zh_CN', '工作流列表内容', '资源名称：${resourceName}使用时间：${useDetail}', 0);
