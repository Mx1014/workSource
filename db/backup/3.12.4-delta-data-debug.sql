INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ('155', 'community', '240111044331055940', '0', '办事指南', '办事指南', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999983', '');

SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);   
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331055940', '办事指南', '办事指南', '155', '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

    
update eh_launch_pad_items set action_type = 33 where id in(112845, 112875) and namespace_id = 999983;
update eh_launch_pad_items set action_data = '{"type":155,"parentId":155,"displayType": "list"}' where id in(112845, 112875) and namespace_id = 999983;

--
-- 科兴物业缴费2.0  add by xq.tian  2016/12/28
--
SELECT max(id) FROM `eh_configurations` INTO @max_id;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@max_id := @max_id + 1), 'kexing.pmbill.api.host', 'http://120.24.88.192:15902', 'kexing pm bill api host', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@max_id := @max_id + 1), 'kexing.pmbill.api.billlist', '/tss/rest/chargeRecordInfo/billListForZhenzhong', 'kexin pm bill api', '999983', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@max_id := @max_id + 1), 'kexing.pmbill.api.billcount', '/tss/rest/chargeRecordInfo/billCountForZhenzhong', 'kexing pm bill api', '999983', NULL);

--
-- 服务联盟详情页面配置   add by xq.tian  2016/12/28
--
UPDATE `eh_configurations` SET `value` = '/service-alliance/index.html#/service_detail/%s/%s?_k=%s' WHERE `name` = 'serviceAlliance.serviceDetail.url';

SELECT max(id) FROM `eh_locale_strings` INTO @max_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@max_id := @max_id + 1), 'organization', '500004', 'zh_CN', '公司不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@max_id := @max_id + 1), 'pmkexing', '0', 'zh_CN', '待缴');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@max_id := @max_id + 1), 'pmkexing', '1', 'zh_CN', '已缴');

--
-- 更新费用查询的icon
--
UPDATE `eh_launch_pad_items` SET `action_data` = '{"url":"http://beta.zuolin.com/property-bill/index.html?hideNavigationBar=1#verify_account#sign_suffix"}' WHERE `namespace_id` = '999983' AND `item_name` = '费用查询';

-- 修改localstring add by xq.tian  2017/01/06
UPDATE `eh_locale_strings` SET `text`='待缴' WHERE (`scope`='pmkexing' AND `code` = '0');
UPDATE `eh_locale_strings` SET `text`='已缴' WHERE (`scope`='pmkexing' AND `code` = '1');
