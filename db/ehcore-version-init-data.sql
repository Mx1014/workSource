USE `ehcore`;

insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('1','Android','3.0.0-user','2015-08-31 15:01:36');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('2','iOS','3.0.0-0','2015-08-31 15:01:36');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('3','Android_Techpark',NULL,'2015-11-26 16:10:58');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('4','iOS_Techpark',NULL,'2015-11-26 16:10:59');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('5','Android_Xunmei',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('6','iOS_Xunmei',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('7','Android_Hwpark',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('8','iOS_Hwpark',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('9','Android_IService',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('10','iOS_IService',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('11','Android_ShUnicom',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('12','iOS_ShUnicom',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('13','Android_JYJY',NULL,'2015-11-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values('14','iOS_JYJY',NULL,'2015-11-26 16:15:29');

insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(1,1,'1048575.9','3145728','0','3.0.0-user','0','2015-08-31 15:01:36');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(2,2,'1048575.9','3145728','0','3.0.0-0','0','2015-08-31 15:04:24');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(3,1,'3145727.9','3145728.1','0','3.0.0-user','0','2015-09-08 11:44:55');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(4,2,'3145727.9','3145728.1','0','3.0.0-0','0','2015-09-08 11:44:55');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(5,3,'-0.1','1048576','0','1.0.0','0','2015-11-26 16:10:59');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(6,4,'-0.1','1048576','0','1.0.0','0','2015-11-26 16:10:59');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(7,5,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(8,6,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(9,7,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(10,8,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(11,9,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(12,10,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(13,11,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(14,12,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(15,13,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(16,14,'-0.1','3145728','0','3.0.0','0','2015-11-26 16:15:29');

insert into `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`) 
	values(1, 1, '3.0.0-user', '${homeurl}/web/download/apk/android_v3.0.0_2015092201_release.apk', '${homeurl}/web/download/apk/andriod-3-0-0.html');
insert into `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`) 
	values(2, 2, '3.0.0-0', '${homeurl}/web/download/apk/android_v3.0.0_2015092201_release.apk', '${homeurl}/web/download/apk/iOS-3-0-0.html');

insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values(15,'Android_Longgang',NULL,'2015-12-26 16:15:29');
insert into `eh_version_realm` (`id`, `realm`, `description`, `create_time`) values(16,'iOS_Longgang',NULL,'2015-12-26 16:15:29');

-- zuolin app force upgrade to 3.2.2 (only andriod 20160107)
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(17,1,'-0.1','3147778.1','0','3.2.2',1,'2016-01-07 16:15:29');
insert into `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`) 
	values(3, 1, '3.2.2', '${homeurl}/web/download/apk/zuolin_v3.2.2_2016010604_release.apk', '${homeurl}/web/download/apk/andriod-zuolin-3-2-2.html');

-- techpark app upgrade to 1.0.2 (only andriod 20160107)
insert into `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	values(18,1,'-0.1','1048578.1','0','1.0.2',0,'2016-01-07 16:15:29');
insert into `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`) 
	values(4, 1, '1.0.2', '${homeurl}/web/download/apk/techpark_v1.0.1_2015123001_release.apk', '${homeurl}/web/download/apk/android-techpark-1-0-2.html');

	