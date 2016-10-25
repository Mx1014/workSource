--
-- 服务联盟4.5.0   add by xq.tian  2016/10/18
--
SET @locale_string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'serviceAlliance.category.display', '1', 'zh_CN', '列表-介绍');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_string_id := @locale_string_id + 1), 'serviceAlliance.category.display', '2', 'zh_CN', '列表-大图');

--
-- 服务联盟详情页面配置   add by xq.tian  2016/10/19
--
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@configuration_id := @configuration_id + 1), 'serviceAlliance.serviceDetail.url', 'http://core.zuolin.com/service-alliance/index.html#/service_detail/%s/%s?_k=%s', '服务联盟详情页面URL', '0', NULL);
