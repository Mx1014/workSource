-- 广告管理 v1.4    add by xq.tian  2018/03/07
SET @eh_locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 0);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
    VALUES ((@eh_locale_strings_id := @eh_locale_strings_id + 1), 'banner', '10004', 'zh_CN', '跳转数据处理失败');
