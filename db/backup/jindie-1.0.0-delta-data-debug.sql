-- 初始化数据, add by tt, 20161117
INSERT INTO `eh_app_namespace_mappings` (`id`, `namespace_id`, `app_key`, `community_id`) VALUES (1, 1000000, '7757a75f-b79a-42fd-896e-107f4bfedd59', 240111044331048623);
INSERT INTO `eh_apps` (`id`, `creator_uid`, `app_key`, `secret_key`, `name`, `description`, `status`, `create_time`, `update_uid`, `update_time`) VALUES (5002, 1, '7757a75f-b79a-42fd-896e-107f4bfedd59', 'nM9PpqGaV2Qe5QqmNSHfWEJyvJjyo0r0f1wJgRadN9zWqcIwdU08FZYjyRSpa2vKmC/Mblh535WMKLiG/Ymr2Q==', 'jin die', 'kingdee', 1, '2016-11-09 11:49:16', NULL, NULL);
-- 短信模板，text后面需要改成实际的templateId, add by tt, 20161117
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 16, 'zh_CN', '发送短信给业务联系人和管理员', '18077', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 17, 'zh_CN', '合同到期前两个月发送短信（有客服人员）', '18077', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 18, 'zh_CN', '合同到期前两个月发送短信（无客服人员）', '18077', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 19, 'zh_CN', '合同到期前一个月发送短信（有客服人员）', '18077', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 20, 'zh_CN', '合同到期前一个月发送短信（无客服人员）', '18077', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 21, 'zh_CN', '发送短信给新企业（有客服人员）', '18077', 1000000);
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default.yzx', 22, 'zh_CN', '发送短信给新企业（无客服人员）', '18077', 1000000);

