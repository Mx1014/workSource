update eh_service_modules set level = 3 where id = 20811;
update eh_service_modules set path = '/20000/20800/20811' where id = 20811;

SET @template_id = (SELECT MAX(id) FROM `eh_locale_templates`); 
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('163', 'equipment.notification', '1', 'zh_CN', '生成核查任务', '您有新的巡检任务，请及时处理，截止日期为：“${deadline}”', '0');
