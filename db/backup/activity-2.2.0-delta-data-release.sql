-- 活动截止报名时间错误提示，add by tt, 20170228
select max(id) into @id from `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10018', 'zh_CN', '报名截止时间应早于或等于活动结束时间!');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10019', 'zh_CN', '活动报名已截止!');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10020', 'zh_CN', '手机号码错误!');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10021', 'zh_CN', '该活动还没有报名信息哦!');