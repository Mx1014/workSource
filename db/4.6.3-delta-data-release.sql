
-- 增加“真实姓名无效”等的错误提示   add by yanjun 20170628
SET @id = (SELECT MAX(id) FROM eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id := @id + 1, 'activity','10028','zh_CN','呃，真实姓名无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id := @id + 1, 'activity','10029','zh_CN','呃，手机号无效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id := @id + 1, 'activity','10030','zh_CN','呃，无效的报名信息');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES(@id := @id + 1, 'activity','10031','zh_CN','呃，报名信息已经存在，请勿重新报名');
