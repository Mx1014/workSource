-- 添加中文字段 add by yanjun 20170502
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10020','zh_CN','活动报名缴费');

insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10021','zh_CN','活动待确认');

insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10022','zh_CN','活动已确认');

insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10023','zh_CN','活动待支付');

-- 增加通知支付的消息模板 add by yanjun 20170513
-- 增加通知支付的消息模板 add by yanjun 20170513
SET @eh_locale_templates_id = (SELECT MAX(id) FROM eh_locale_templates); 
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@eh_locale_templates_id := @eh_locale_templates_id + 1, 'activity.notification','8','zh_CN','活动待确认，通知活动创建者进行确认','“${userName}”报名了活动「${postName}」，请尽快确认。','0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@eh_locale_templates_id := @eh_locale_templates_id + 1, 'activity.notification','9','zh_CN','活动被管理员同意，并且不需要支付，通知活动报名者','你报名的活动「${postName}」已被确认。','0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES(@eh_locale_templates_id := @eh_locale_templates_id + 1, 'activity.notification','10','zh_CN','活动被管理员同意，通知活动报名者进行支付','你报名的活动「${postName}」已被确认，请在${payTimeDays}天${payTimeHours}小时之内尽快完成支付。','0');


