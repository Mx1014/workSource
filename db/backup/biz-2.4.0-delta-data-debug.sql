-- biz version package:2.4.0
set @version_upgrade_rules_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES ((@version_upgrade_rules_id := @version_upgrade_rules_id + 1),49,'2100225.9','2101248','0','2.4.0','0',UTC_TIMESTAMP());
set @version_urls_id = (SELECT MAX(id) FROM `eh_version_urls`);
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ((@version_urls_id := @version_urls_id + 1), '49', '2.4.0', 'http://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-4-0-tag.zip', 'http://biz-beta.zuolin.com/nar/biz/web/app/dist/biz-2-4-0-tag.zip', '0');
-- update : business.url
update eh_configurations set value='http://biz-beta.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=http://biz-beta.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%3Fpos%3D1%23%2Fstore%2Fdefault%3F_k%3Dzlbiz#sign_suffix' where name = 'business.url';