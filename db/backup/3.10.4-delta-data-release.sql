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


-- 配置活动提醒消息, add by tt, 20161027
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'activity.notification', 5, 'zh_CN', '创建者删除活动', '很抱歉通知您：您报名的活动<${tag} 丨 ${title}>因故取消。\n更多活动敬请继续关注。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'activity.notification', 6, 'zh_CN', '活动提醒', '您报名的活动 <${tag} 丨 ${title}> 还有 ${time}就要开始了 >>', 0);

SELECT @id:=MAX(id) FROM `eh_locale_strings`;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '6', 'zh_CN', '开始时间：');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '7', 'zh_CN', '结束时间：');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '8', 'zh_CN', '活动地点：');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '9', 'zh_CN', '活动嘉宾：');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10', 'zh_CN', '抱歉，您的APP版本不支持查看该内容！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10013', 'zh_CN', '活动报名人数已满，感谢关注');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10014', 'zh_CN', '报名人数上限应大于0！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1, 'activity', '10015', 'zh_CN', '报名为数上限不能大于1万！');

-- 配置活动详情里面的内容的链接, add by tt, 20161027
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (178, 'activity.content.url', '/web/lib/html/activity_text_review.html', 'activity content url', 0, NULL);


-- 新增服务联盟模板  add by xiongying 20161027
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('3', 'ReserveArt', '艺术教室', '我要预约', '1', '1', '{"fields":[{"fieldName":"reserveType","fieldDisplayName":"预约类型","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入类型为插花沙龙或咖啡教室或音乐会","requiredFlag":"1"},{"fieldName":"reserveOrganization","fieldDisplayName":"预约机构","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入公司名称","requiredFlag":"1"},{"fieldName":"reserveTime","fieldDisplayName":"预约时间","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入预约时间段","requiredFlag":"1"},{"fieldName":"contact","fieldDisplayName":"联系人","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系人姓名","requiredFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系电话","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"选填，若还有其他要求，可在此填写","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('4', 'ReserveExercise', '运动健身', '我要预约', '1', '1', '{"fields":[{"fieldName":"reserveType","fieldDisplayName":"预约类型","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入类型为办公室健身操或会所健身操","requiredFlag":"1"},{"fieldName":"reserveOrganization","fieldDisplayName":"预约机构","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入公司名称","requiredFlag":"1"},{"fieldName":"reserveTime","fieldDisplayName":"预约时间","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入预约时间段","requiredFlag":"1"},{"fieldName":"contact","fieldDisplayName":"联系人","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系人姓名","requiredFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系电话","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"选填，若还有其他要求，可在此填写","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('5', 'ReservePhysiotherapy', '员工理疗', '我要预约', '1', '1', '{"fields":[{"fieldName":"reserveType","fieldDisplayName":"预约类型","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入类型为肩颈按摩或健康咨询或企业体验","requiredFlag":"1"},{"fieldName":"reserveOrganization","fieldDisplayName":"预约机构","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入公司名称","requiredFlag":"1"},{"fieldName":"reserveTime","fieldDisplayName":"预约时间","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入预约时间段","requiredFlag":"1"},{"fieldName":"contact","fieldDisplayName":"联系人","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系人姓名","requiredFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系电话","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"选填，若还有其他要求，可在此填写","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('6', 'ReserveTrainAndHoliday', '培训度假', '我要预约', '1', '1', '{"fields":[{"fieldName":"reserveType","fieldDisplayName":"预约类型","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入类型为会议或培训或度假","requiredFlag":"1"},{"fieldName":"reserveOrganization","fieldDisplayName":"预约机构","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入公司名称","requiredFlag":"1"},{"fieldName":"reserveTime","fieldDisplayName":"预约时间","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入预约时间段","requiredFlag":"1"},{"fieldName":"contact","fieldDisplayName":"联系人","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系人姓名","requiredFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系电话","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"选填，若还有其他要求，可在此填写","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP());
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`)
    VALUES ('7', 'ReserveSocial', '企业社交', '我要预约', '1', '1', '{"fields":[{"fieldName":"reserveType","fieldDisplayName":"预约类型","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入类型为主题酒会或圈层社交","requiredFlag":"1"},{"fieldName":"reserveOrganization","fieldDisplayName":"预约机构","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入公司名称","requiredFlag":"1"},{"fieldName":"reserveTime","fieldDisplayName":"预约时间","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入预约时间段","requiredFlag":"1"},{"fieldName":"contact","fieldDisplayName":"联系人","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系人姓名","requiredFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入联系电话","requiredFlag":"1"},{"fieldName":"remarks","fieldDisplayName":"备注","fieldType":"string","fieldContentType":"text","fieldDesc":"选填，若还有其他要求，可在此填写","requiredFlag":"0"}]}', '1', '1', UTC_TIMESTAMP());

INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (1, '999985', '3');
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (2, '999985', '4');
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (3, '999985', '5');
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (4, '999985', '6');
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (5, '999985', '7');
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (6, '999987', '1');
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (7, '999987', '2');

-- 去掉创源的物业菜单 by sfyan 20161027
DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` IN (SELECT id FROM `eh_web_menus` WHERE `path` like '/50000/58000%') AND `owner_type` = 'EhNamespaces' AND `owner_id` = 999986;

INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (6, '999987', '1');
INSERT INTO `eh_request_templates_namespace_mapping` (`id`, `namespace_id`, `template_id`) VALUES (7, '999987', '2');
