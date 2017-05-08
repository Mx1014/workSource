-- 添加中文字段 add by yanjun 20170502
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10020','zh_CN','活动报名缴费');