INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10006', 'zh_CN', '左邻');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'videoConf', '10007', 'zh_CN', '现在还没有召开会议');


INSERT INTO `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('15','Android_Videoconf',NULL,'2015-12-14 14:15:29');
INSERT INTO `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('16','iOS_Videoconf',NULL,'2015-12-14 14:15:29');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(19,15,'1048575.9','1049600','0','1.1.0','0','2015-12-14 14:15:29');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) values(20,16,'1048575.9','1049600','0','1.1.0','0','2015-12-14 14:15:29');
INSERT INTO `eh_version_urls` VALUES (5, 15, '1.0.0', 'http://fir.im/zlConfArd', 'http://fir.im/zlConfArd', 0);
INSERT INTO `eh_version_urls` VALUES (6, 16, '1.0.0', 'http://fir.im/zlConf', 'http://fir.im/zlConf', 0);