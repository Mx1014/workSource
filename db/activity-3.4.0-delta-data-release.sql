-- 因为后面会出现clone帖的功能，现在现将原有帖子设置为正常帖  add by yanjun 20170809
UPDATE eh_forum_posts set clone_flag = 2 where clone_flag is null;
UPDATE eh_activities set clone_flag = 2 where clone_flag is null;

-- 活动报名导入异常信息
set @id = (select MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '20', 'zh_CN', '手机号为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '21', 'zh_CN', '手机号无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '22', 'zh_CN', '真实姓名为空');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '23', 'zh_CN', '报名信息已经存在，可在报名详情页修改该用户信息');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '24', 'zh_CN', '该用户在Excel表重复');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES
	((@id := @id + 1), 'activity', '25', 'zh_CN', '报名信息存在，现已更新');

UPDATE eh_locale_strings SET text = '呃，报名信息已经存在，可在报名详情页修改该用户信息' WHERE scope = 'activity' AND code = 10031;