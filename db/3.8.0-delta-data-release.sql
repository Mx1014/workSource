-- 储能升级规则
set @version_upgrade_rules_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),34,'3152895.9','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),35,'3152895.9','3153920','0','3.8.0','0',UTC_TIMESTAMP());

set @version_urls_id = (SELECT MAX(id) FROM `eh_version_urls`);
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '34', '3.8.0', '${homeurl}/web/download/apk/UFinePark-3.8.0.2016080302-release.apk', '${homeurl}/web/download/apk/andriod-UFinePark-3-8-0.html', '0');
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '35', '3.8.0', '', '${homeurl}/web/download/apk/iOS-UFinePark-3-8-0.html', '0');
