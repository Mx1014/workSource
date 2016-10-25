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


-- merge from customer-manage-1.1-delta-data-release.sql by lqs 20161025
--
-- 车辆停车类型     add by xq.tian 2016/10/11
--
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('1', '0', NULL, NULL, '1', '临时卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('2', '0', NULL, NULL, '2', '月临时', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('3', '0', NULL, NULL, '3', '充值卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('4', '0', NULL, NULL, '4', '固定卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('5', '0', NULL, NULL, '5', '免费卡', '1', NULL, NULL);
INSERT INTO `eh_parking_card_categories` (`id`, `namespace_id`, `owner_type`, `owner_id`, `card_type`, `category_name`, `status`, `creator_uid`, `create_time`)
  VALUES ('6', '0', NULL, NULL, '6', '其它', '1', NULL, NULL);

--
-- 客户资料分类     add by xq.tian 2016/10/13
--
INSERT INTO `eh_organization_owner_type` (`id`, `namespace_id`, `name`, `display_name`, `status`, `create_time`, `update_time`, `update_uid`)
  VALUES ('8', '0', 'none', '无', '1', NULL, NULL, NULL);

SET @locale_string_id = (SELECT MAX (id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
  VALUES ((@locale_string_id := @locale_string_id + 1), 'pm', '18001', 'zh_CN', '该记录已经处于未认证状态');
  
  