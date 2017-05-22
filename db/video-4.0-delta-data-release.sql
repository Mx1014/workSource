
-- 资源预订短信模板，add by wh, 20161219
SET @id =(SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','28','zh_CN','资申成-正中会','38570','999983');