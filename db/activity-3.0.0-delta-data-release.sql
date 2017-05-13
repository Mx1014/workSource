-- 添加中文字段 add by yanjun 20170502
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10020','zh_CN','活动报名缴费');

-- 增加通知支付的消息模板 add by yanjun 20170513
SET @eh_locale_templates_id = (SELECT MAX(id) FROM eh_locale_templates); 
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@eh_locale_templates_id := @eh_locale_templates_id + 1, 'activity.notification','8','zh_CN','活动被管理员同意，通知活动报名者进行支付','您报名参加的活动“${postName}”已被管理员通过，请在${payTimeDays}天${payTimeHours}小时之内尽快完成支付。','0');
