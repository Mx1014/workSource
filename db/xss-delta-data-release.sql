-- 用户名或密码错误提示 add by xq.tian  2018/10/11
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'user', '100020', 'zh_CN', '用户名或密码错误');