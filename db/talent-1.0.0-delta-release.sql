select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id+1, 'talent', '1', 'zh_CN', '本功能仅对企业管理员开放，如有需要请联系企业管理员');
