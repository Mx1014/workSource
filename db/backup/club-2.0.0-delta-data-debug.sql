-- 俱乐部相关配置， add by tt, 20161102
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10031', 'zh_CN', '名称已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10032', 'zh_CN', '今天广播发送次数已用完');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10033', 'zh_CN', '标题不能超过10个字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10034', 'zh_CN', '内容不能超过200个字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'group', '10035', 'zh_CN', '不可小于10个字');

select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 36, 'zh_CN', '俱乐部发消息', '${newCreator}已成为“${groupName}”的创建者', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 37, 'zh_CN', '俱乐部发消息', '你已成为“${groupName}”的创建者', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 38, 'zh_CN', '俱乐部发消息', '你创建“俱乐部A”的申请已通过', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 39, 'zh_CN', '俱乐部发消息', '你已成功创建“${groupName}”', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 40, 'zh_CN', '俱乐部发消息', '你提交了创建“${groupName}”的申请，需要人工审核，请耐心等候', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 41, 'zh_CN', '俱乐部发消息', '你创建“${groupName}”的申请被拒绝', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 42, 'zh_CN', '俱乐部发消息', '你加入的“${groupName}”已解散', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 43, 'zh_CN', '俱乐部发消息', '不允许创建${clubPlaceholderName}', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 44, 'zh_CN', '俱乐部发消息', '${userName}申请加入“${groupName}”，理由：${reason}', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 45, 'zh_CN', '俱乐部发消息', '你加入“${groupName}”的申请已通过', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'group.notification', 46, 'zh_CN', '俱乐部发消息', '你加入“${groupName}”的申请被拒绝', 0);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (402, 'club.placeholder.name', '俱乐部', 'club placeholder name', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (403, 'club.share.url', '/mobile/static/group_share/index.html', 'club share url', 0, NULL);