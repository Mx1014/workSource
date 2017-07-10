-- 企业人才申请的发消息，add by tt, 20170710
select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id+1, 'talent.notification', 1, 'zh_CN', '企业人才|收到一条企业人才的申请单', '企业人才申请时发消息', 0);
