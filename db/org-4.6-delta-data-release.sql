SET @locale_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100015', 'zh_CN', '账号重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100016', 'zh_CN', '账号长度不对或格式错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@locale_id := @locale_id + 1, 'archives', '100017', 'zh_CN', '账号一经设定，无法修改');