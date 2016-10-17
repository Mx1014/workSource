-- 配置活动提醒消息
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'activity.notification', 5, 'zh_CN', '创建者删除活动', '很抱歉通知您：您报名的活动<${tag} 丨 ${title}>因故取消。\n更多活动敬请继续关注。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'activity.notification', 6, 'zh_CN', '活动提醒', '您报名的活动 <${tab} 丨 ${title}> 还有 ${time}就要开始了 >>', 0);

-- 配置活动详情里面的内容的链接
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (178, 'activity.content.url', '/web/lib/html/activity_text_review.html', 'activity content url', 0, NULL);
