-- 储能升级规则
set @version_upgrade_rules_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),34,'3152895.9','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),35,'3152895.9','3153920','0','3.8.0','0',UTC_TIMESTAMP());

set @version_urls_id = (SELECT MAX(id) FROM `eh_version_urls`);
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '34', '3.8.0', '${homeurl}/web/download/apk/UFinePark-3.8.0.2016080302-release.apk', '${homeurl}/web/download/apk/andriod-UFinePark-3-8-0.html', '0');
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '35', '3.8.0', '', '${homeurl}/web/download/apk/iOS-UFinePark-3-8-0.html', '0');

-- 电商2.3.2版升级,入口url修改
update eh_configurations set `value`=replace(value,'store%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix','store%2Fdefault%3F_k%3Dzlbiz#sign_suffix') where `name`='business.url';
update eh_launch_pad_items set action_data=replace(action_data,'store%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix','store%2Fdefault%3F_k%3Dzlbiz#sign_suffix') where action_data like '%store%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix%';
update eh_banners set action_data=replace(action_data,'store%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix','store%2Fdefault%3F_k%3Dzlbiz#sign_suffix') where action_data like '%store%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix%';
-- 电商2.3.2版升级,增加版本管理
set @version_upgrade_rules_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES ((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),49,'2100223.9','2100226','0','2.3.2','0',UTC_TIMESTAMP());
set @version_urls_id = (SELECT MAX(id) FROM `eh_version_urls`);
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '49', '2.3.2', 'https://biz.zuolin.com/nar/biz/web/app/dist/biz-2-3-2-tag.zip', 'https://biz.zuolin.com/nar/biz/web/app/dist/biz-2-3-2-tag.zip', '0');



-- 修改文字  by sfyan 20160809
UPDATE `eh_web_menus` SET `name` = '场所预订' WHERE `id` = 42000;
UPDATE `eh_web_menus` SET `name` = '预订详情' WHERE `id` = 42300;