-- 短信黑名单  add by xq.tian  2017/07/04
SET @max_locale_id = (SELECT max(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@max_locale_id := @max_locale_id + 1), 'user', '300004', 'zh_CN', '对不起，您的手机号在我们的黑名单列表');
