-- biz version from 2.4.4 to 2.4.5
set @version_upgrade_rules_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES ((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),49,'2101251.9','2101253','0','2.4.5','0',UTC_TIMESTAMP());
set @version_urls_id = (SELECT MAX(id) FROM `eh_version_urls`);
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '49', '2.4.5', 'https://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-4-5-tag.zip', 'https://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-4-5-tag.zip', '0');