-- 左邻
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(56,1,'-0.1','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (28, 1, '3.8.0', 'http://apk.zuolin.com/apk/Zuolin-3.8.0.2016080818-release.apk', 'andriod-everhomes-3-8-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(57,2,'-0.1','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (29, 2, '3.8.0', '', 'iOS-everhomes-3-8-0.html', '0');

-- 左邻会议
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(58,15,'-0.1','2097154','0','2.0.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (30, 15, '2.0.2', 'http://apk.zuolin.com/apk/Videoconf-2.0.2.2016072101-release.apk', 'andriod-meeting-2-0-2.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(59,16,'-0.1','1049600','0','1.1.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (31, 16, '1.1.0', '', 'iOS-meeting-1-1-0.html', '0');

-- 科技园
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(60,3,'-0.1','3151872','0','3.6.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (32, 3, '3.6.0', 'http://apk.zuolin.com/apk/TechPark-3.6.0.2016060301-release.apk', 'andriod-techpark-3-6-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(61,4,'-0.1','3151872','0','3.6.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (33, 4, '3.6.0', '', 'iOS-techpark-3-6-0.html', '0');

-- 深业物业
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(62,30,'-0.1','3152896','0','3.7.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (34, 30, '3.7.0', 'http://apk.zuolin.com/apk/ShenyeProperty-3.7.0.2016071405-release.apk', 'andriod-sywy-3-7-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(63,31,'-0.1','3152897','0','3.7.1','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (35, 31, '3.7.1', '', 'iOS-sywy-3-7-1.html', '0');

-- 威新LINK+
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(64,40,'-0.1','3153922','0','3.8.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (36, 40, '3.8.2', 'http://apk.zuolin.com/apk/Android-3.8.2.2016081606-release.apk', '', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(65,41,'-0.1','3153922','0','3.8.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (37, 41, '3.8.2', '', '', '0');

-- ibase
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(66,38,'-0.1','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (38, 38, '3.8.0', 'http://apk.zuolin.com/apk/IBase-3.8.0.2016080906-release.apk', 'andriod-ibase-3-8-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(67,39,'-0.1','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (39, 39, '3.8.0', '', 'iOS-ibase-3-8-0.html', '0');

-- Ufine
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(68,34,'-0.1','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (40, 34, '3.8.0', 'http://apk.zuolin.com/apk/UFinePark-3.8.0.2016080302-release.apk', 'andriod-UfinePark-3-8-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(69,35,'-0.1','3153920','0','3.8.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (41, 35, '3.8.0', '', 'iOS-UfinePark-3-8-0.html', '0');

-- 海岸
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(70,27,'-0.1','3151874','0','3.6.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (42, 27, '3.6.2', 'http://apk.zuolin.com/apk/HaianPark-3.6.2.2016062703-release.apk', 'andriod-haian-3-6-2.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(71,28,'-0.1','3151872','0','3.6.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (43, 28, '3.6.0', '', 'iOS-haian-3-6-0.html', '0');

-- 中洲
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(72,5,'-0.1','3152896','0','3.7.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (44, 5, '3.7.0', 'http://apk.zuolin.com/apk/XmTecPark-3.7.0.2016071405-release.apk', 'andriod-zz-3-7-0.html', '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
    VALUES(73,6,'-0.1','3152896','0','3.7.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (45, 6, '3.7.0', '', 'iOS-zz-3-7-0.html', '0');


