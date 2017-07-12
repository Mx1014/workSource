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


-- 活动报名表新增organization_id后，刷新数据  add by yanjun 20170517
UPDATE eh_activity_roster r
SET r.organization_id = (SELECT
                           a.id
                         FROM eh_organizations a,
                           eh_organization_members b,
                           eh_activities c
                         WHERE a.id = b.organization_id
                             AND a.namespace_id = c.namespace_id
                             AND c.id = r.activity_id
                             AND r.uid = b.target_id
                             AND b.target_type = 'USER'
                         LIMIT 1 );
                         
-- 刷新活动tag，将null和''的刷成'其他'，方便统计  add by yanjun 20170518
UPDATE eh_activities ac SET ac.tag = '其他' WHERE ac.tag IS NULL OR ac.tag = '';

-- 添加中文字段，当取消报名时用于提示超过截止日期  add by yanjun 20170519
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
insert into `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) values(@eh_locale_strings_id := @eh_locale_strings_id + 1,'activity','10025','zh_CN','报名已截止，不可取消报名');

