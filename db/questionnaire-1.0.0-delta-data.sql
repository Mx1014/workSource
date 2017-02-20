-- 问卷调查的错误提示， add by tt, 20170220
select max(id) into @id from eh_locale_strings;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '1', 'zh_CN', '问卷名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '2', 'zh_CN', '问卷名称不能超过50个字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '3', 'zh_CN', '题目名称不能为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '4', 'zh_CN', '至少需要有一个题目');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '5', 'zh_CN', '至少需要有一个选项');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'questionnaire', '6', 'zh_CN', '选项名称不能为空');
