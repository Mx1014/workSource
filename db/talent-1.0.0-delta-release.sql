select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'talent', '1', 'zh_CN', '本功能仅对企业管理员开放，如有需要请联系企业管理员');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'parameters.error', '10002', 'zh_CN', '参数长度不足');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'parameters.error', '10003', 'zh_CN', '参数不能为空');