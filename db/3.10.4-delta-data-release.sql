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

-- 储能停车充值 add by sunwen 20161025
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
	VALUES ('400', 'parking', '10010', 'zh_CN', '费用已过期，请重新查询费用');

-- 华润OE配search type add by xiongying 20161026
SET @search_type_id = (SELECT MAX(id) FROM `eh_search_types`);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999985', '', '0', '投票', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999985', '', '0', '活动', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999985', '', '0', '话题', 'topic', '1', NULL, NULL);

-- 华润OE配置企业定制服务联盟入口   add by xq.tian  2016/10/27
SET @alliance_category_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
INSERT INTO `eh_service_alliance_categories` (`id`, `parent_id`, `name`, `path`, `status`, `creator_uid`, `create_time`, `namespace_id`, `display_mode`)
VALUES ((@alliance_category_id := @alliance_category_id + 1), '0', '企业定制', '企业定制', '2', '1', UTC_TIMESTAMP(), '999985', '1');