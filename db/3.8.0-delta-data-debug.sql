-- 金地威新升级规则
set @version_upgrade_rules_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),40,'1048575.9','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),41,'1048575.9','3153920','0','3.8.0','0',UTC_TIMESTAMP());

set @version_urls_id = (SELECT MAX(id) FROM `eh_version_urls`);
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '40', '3.8.0', '', '', '0');
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '41', '3.8.0', 'https://itunes.apple.com/us/app/jin-de-wei-xin/id1112433619?l=zh&ls=1&mt=8', '', '0');

-- 电商2.3.2版升级入口url修改
update eh_configurations set `value`=replace(value,'store%2Fdetails%2F14646085111657370488%3F_k%3Dzlbiz#sign_suffix','store%2Fdefault%3F_k%3Dzlbiz#sign_suffix') where `name`='business.url';
update eh_launch_pad_items set action_data=replace(action_data,'store%2Fdetails%2F14646085111657370488%3F_k%3Dzlbiz#sign_suffix','store%2Fdefault%3F_k%3Dzlbiz#sign_suffix') where action_data like '%store%2Fdetails%2F14646085111657370488%3F_k%3Dzlbiz#sign_suffix%';
update eh_banners set action_data=replace(action_data,'store%2Fdetails%2F14646085111657370488%3F_k%3Dzlbiz#sign_suffix','store%2Fdefault%3F_k%3Dzlbiz#sign_suffix') where action_data like '%store%2Fdetails%2F14646085111657370488%3F_k%3Dzlbiz#sign_suffix%';
-- 电商2.3.2版升级,增加版本管理
set @version_upgrade_rules_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES ((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),49,'2100223.9','2100226','0','2.3.2','0',UTC_TIMESTAMP());
set @version_urls_id = (SELECT MAX(id) FROM `eh_version_urls`);
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '49', '2.3.2', 'http://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-3-2-tag.zip', 'http://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-3-2-tag.zip', '0');