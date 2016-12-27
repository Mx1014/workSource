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
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@max_id := @max_id + 1), 'pmkexing', '0', 'zh_CN', '未缴纳');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@max_id := @max_id + 1), 'pmkexing', '1', 'zh_CN', '已缴纳');

SELECT max(id) FROM `eh_locale_strings` INTO @max_id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
VALUES ((@max_id := @max_id + 1), '999983', '0', '0', '0', '/home', 'Bizs', 'PM_KEXING', '费用查询', 'cs://1/image/aW1hZ2UvTVRvME5tTXpZVEEwWlRKa1lqQXlaVFEwTmpkaU5XRTJORGN5WVdJM056QmpZUQ', '1', '1', '13', '{"url":"http://beta.zuolin.com/property-bill/index.html?hideNavigationBar=1#verify_account#sign_suffix"}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);
