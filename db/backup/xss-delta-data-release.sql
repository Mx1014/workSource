-- 用户名或密码错误提示 add by xq.tian  2018/10/11
SET @eh_locale_strings_id = (SELECT MAX(id) from `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'user', '100020', 'zh_CN', '用户名或密码错误');

-- 越空间独立部署的 root 用户的密码修改为: eh#1802
UPDATE eh_users SET password_hash='4eaded9b566765a1e70e2e0dc45204c14c4b9df41507a6b72c7cc7fe91d85341', salt='3023538e14053565b98fdfb2050c7709'
WHERE account_name='root' AND namespace_id=0;