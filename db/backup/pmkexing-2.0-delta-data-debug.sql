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