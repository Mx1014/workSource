SELECT max(id) FROM eh_configurations INTO @max_id;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@max_id := @max_id + 1), 'createTopic.blacklist', '18161013995,13550817426,13128939429', '发帖黑名单', '999986', NULL);

SELECT max(id) FROM eh_locale_strings INTO @max_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@max_id := @max_id + 1), 'forum', '10021', 'zh_CN', '对不起,您已被禁止发帖');
