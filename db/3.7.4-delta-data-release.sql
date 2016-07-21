delete from eh_launch_pad_items where target_type='biz' and scope_code = 0;

INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.realm', 'biz', 'business realm');
INSERT INTO `ehcore`.`eh_configurations` (`name`, `value`, `description`) VALUES ('business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14477417463124576784%3F_k%3Dzlbiz#sign_suffix', 'business url');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('group', '10030', 'zh_CN', '您不是群主，无权操作');

#电商离线包配置
INSERT INTO `eh_version_realm` VALUES ('49', 'biz', null, UTC_TIMESTAMP(), '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(52,49,'-0.1','2100224','0','2.3.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES ('24', '49', '2.3.0', 'http://biz.zuolin.com/nar/web/app/dist/biz-2-3-0-tag.zip', 'http://biz.zuolin.com/nar/web/app/dist/biz-2-3-0-tag.zip', '0');