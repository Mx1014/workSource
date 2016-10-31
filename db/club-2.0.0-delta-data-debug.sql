select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10031', 'zh_CN', '名称已存在');

select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 36, 'zh_CN', '转移创建者，给新创建者发消息', '${newCreator}已成为“${groupName}”的创建者', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 37, 'zh_CN', '转移创建者，给新创建者发消息', '你已成为“${groupName}”的创建者', 0);