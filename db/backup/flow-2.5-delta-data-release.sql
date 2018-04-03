-- 二维码加路由   add by xq.tian  2017/11/15
SET @locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 1);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_strings_id := @locale_strings_id + 1), 'flow', '100019', 'zh_CN', '对不起，您无权查看此任务');
