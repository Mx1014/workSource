SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'activity.notification', 5, 'zh_CN', '创建者删除活动', '很抱歉通知您：您报名的活动<${tag} 丨 ${title}>因故取消。\n更多活动敬请继续关注。', 0);
