-- 帖子评论时给创建者或父评论者发送消息模板，add by tt, 20170316
select max(id) into @id from `eh_locale_templates`;
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'forum.notification', 2, 'zh_CN', '帖子评论给创建者发消息', '有人评论了你的帖子\t${userName} 评论了你的帖子 ${postName}。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'forum.notification', 3, 'zh_CN', '帖子评论给父评论者发消息', '有人回复了你的评论\t${userName} 回复了你在帖子 ${postName} 的评论。', 0);

-- 更改帖子删除提示，add by tt, 20170316
UPDATE `eh_locale_strings` SET `text`='该帖子已被主人删除' WHERE  `id`=44;