SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);   
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@string_id := @string_id + 1), 'address', '20007', 'zh_CN', '建筑面积只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@string_id := @string_id + 1), 'address', '20008', 'zh_CN', '公摊面积只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@string_id := @string_id + 1), 'address', '20009', 'zh_CN', '收费面积只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@string_id := @string_id + 1), 'address', '20010', 'zh_CN', '出租面积只能为数字');
