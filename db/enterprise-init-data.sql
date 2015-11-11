INSERT INTO `ehcore`.`eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`,  `avatar`,  `status`,  `points`,  `level`,  `gender`,  `locale`,  `salt`,  `password_hash`)
VALUES ('38',UUID(), '13632650699', '徐明晓', 'cs://1/image/aW1hZ2UvTVRvMU1EQTVZVEZrTkdVek9EQXhZbVE0WlRZd1l6UXdOVE0zWVdJNFkyTmlNUQ', '1', '45', '1', '0',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199');
INSERT INTO `ehcore`.`eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`)
VALUES ('38',  '38',  '0',  '13632650699',  NULL,  '3');

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `address_id`, `description`, `path`, `level`, `status`) VALUES('44','0','GANC','科技园物业','0',NULL,'/科技园物业','0','2');


INSERT INTO `eh_forums`(`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(3, UUID(), 0, 2, '', 0, '科技园论坛', '', 0, 0, UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums`(`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(4, UUID(), 0, 2, '', 0, '科技园意见反馈论坛', '', 0, 0, UTC_TIMESTAMP(), UTC_TIMESTAMP());	
	
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`) VALUES('24210150826969742','','5636851','深圳市','4151','南山区','科技园','科技工业园','深圳市南山区科技园',NULL,'深圳科技工业园是我国大陆第一个高新科技产业园','深圳科技工业园是我国大陆第一个高新科技产业园，于1985年由深圳市政府和中国科学院共同创办。1991年，经国务院批准，深圳科技工业园成为首批国家级高新技术产业园区。\r\n\r\n园区占地面积1.15平方公里，经过不断的开发建设，深圳科技工业园已成为投资环境优越、高新技术企业云集、科技开发实力雄厚、人才济济的科技园区。\r\n\r\n近几年来，公司紧紧抓住深圳市产业结构调整的机遇，重点推进深圳市金融服务技术创新基地、深港动漫及网游产业孵化基地、深港生产力基地的建设工作。走出了一条金融产业、文化产业与科技产业相互融合、共同发展的新型道路，力将科技园打造成为具有高科技含量的金融创新区与文化创意园。科技园金融基地作为深圳市率先启用的金融基地于2009年3月投入使用，目前已吸引大批知名的金融企业、金融服务类企业入驻，品牌效应初步树立。',NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1000087',NULL,'2','2015-11-05 14:43:25',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1','3','4',NULL);


INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) VALUES('16','24210150826969742','金融基地\r\n','金融基地\r\n','38','13632650699','深圳市南山区科技园科苑路6号\r\n',NULL,NULL,NULL,NULL,NULL,'http://10.1.1.90:5005/image/aW1hZ2UvTVRwbE1tVmlPRFV3WkRZeFlXUTJORGN5Wmpka1pXWmxPRGcxTVRrNU4yTXdZZw?token=0Y1WmdqibLa2mY5rQCm7Osm','2','100002',NULL,'100002','2015-11-05 14:56:31',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) VALUES('19','24210150826969742','金融科技大厦\r\n','金融科技\r\n','38','13632650699','南山区高新中区科苑大道16号\r\n',NULL,NULL,NULL,NULL,NULL,'http://10.1.1.90:5005/image/aW1hZ2UvTVRvM04yVmhORE00WVRrM04yTmlaVGRsWVdJME56a3hZak5pTnpNNE5qVXlPQQ?token=0Y1WmdqibLa2mY5rQCm7Osm','2','100002',NULL,'100002','2015-11-05 14:56:31',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) VALUES('20','24210150826969742','科技工业园大厦\r\n','科技园\r\n','38','13632650699','深圳市南山区科技园科苑路6号\r\n',NULL,NULL,NULL,NULL,NULL,'http://10.1.1.90:5005/image/aW1hZ2UvTVRwbE1URmtPVE0wWmpGaFptUTFaVEUwTldJNFpqZGlZV0UxTURFd1l6UXpOQQ?token=0Y1WmdqibLa2mY5rQCm7Osm','2','100002',NULL,'100002','2015-11-05 14:56:31',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`) VALUES('21','24210150826969742','生产力大楼\r\n','生产力大楼\r\n','38','13632650699','广东省深圳市高新中二道5号\r\n',NULL,NULL,NULL,NULL,NULL,'http://10.1.1.90:5005/image/aW1hZ2UvTVRvMFkyVmxZakF4WTJNd1ltSmlabU5tTlRabFltWTVNR1kzTUdRM01URTRaUQ?token=0Y1WmdqibLa2mY5rQCm7Osm','2','100002',NULL,'100002','2015-11-05 14:56:31',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1000',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','二层东','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1001',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','二层西','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1002',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','二楼中','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1003',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','大厦202','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1004',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','三楼','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1005',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','四层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1006',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东401','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1007',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东501','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1008',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东502','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1009',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东503','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1010',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东508','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1011',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东509','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1012',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东510','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1013',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东518','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1014',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西502A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1015',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西503','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1016',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西506','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1017',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西508','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1018',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西510','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1019',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西518','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1020',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西588','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1021',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西598','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1022',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东605','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1023',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东606','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1024',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东606A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1025',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','中602','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1026',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东608','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1027',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西602','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1028',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西603','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1029',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西606','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1030',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西608','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1031',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西609','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1032',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西610','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1033',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西612','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1034',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西616','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1035',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东702','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1036',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东703','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1037',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东705','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1038',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东706','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1039',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东708','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1040',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','701','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1041',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东702','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1042',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东704','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1043',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东802','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1044',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东805','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1045',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东806','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1046',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东808','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1047',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东809','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1048',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东810','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1049',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西801','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1050',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西802','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1051',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西803','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1052',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','九层西','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1053',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','9层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1054',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','10层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1055',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东1101','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1056',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东1102','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1057',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西1101','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1058',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西1102','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1059',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','东1201','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1060',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西1201','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1061',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西1202','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1062',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西1203','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1063',UUID(),'24210150826969742','5636851','4151','科技工业园大厦-',F2,'科技工业园大厦','西1203','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1064',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5D2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1065',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-1A02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1066',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-1B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1067',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-2A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1068',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-2B1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1069',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-6C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1070',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-2F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1071',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3A1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1072',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1073',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-7A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1074',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3B2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1075',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1076',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-11A2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1077',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-10A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1078',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-10B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1079',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-10C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1080',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-10E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1081',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-10F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1082',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-10D1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1083',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-10D3','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1084',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-10D2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1085',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-2D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1086',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-2C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1087',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3A2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1088',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4E2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1089',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-1C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1090',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4C3','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1091',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4D2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1092',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1093',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-7C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1094',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-7E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1095',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-7F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1096',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1097',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4F3','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1098',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3C5','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1099',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1100',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-7B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1101',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1102',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-9A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1103',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-9B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1104',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-9E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1105',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-7D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1106',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1107',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1108',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-8A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1109',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-8B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1110',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-8C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1111',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-8D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1112',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-8E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1113',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-8F1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1114',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-8F2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1115',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5D1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1116',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-6F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1117',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-11C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1118',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-11D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1119',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-11E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1120',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-11F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1121',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3B1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1122',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-6B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1123',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-6A ','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1124',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3C1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1125',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-11B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1126',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5C2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1127',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4F2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1128',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-6E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1129',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4D1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1130',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-9F1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1131',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-9F2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1132',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3C2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1133',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5C1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1134',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-5D1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1135',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-4E1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1136',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','1-3C4','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1137',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1138',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1139',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1140',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1141',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1142',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1143',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1144',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1145',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1146',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1147',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1148',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-8F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1149',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-6A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1150',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-5E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1151',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-5F2','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1152',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-7D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1153',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-7C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1154',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-7A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1155',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-7B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1156',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-5F1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1157',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-1A02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1158',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-1B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1159',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-3A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1160',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-3B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1161',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-3C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1162',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-3D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1163',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-3E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1164',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-3F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1165',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-1C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1166',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-1D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1167',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-10C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1168',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-10D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1169',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-6B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1170',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-6C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1171',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-6D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1172',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-9E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1173',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-9F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1174',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-4A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1175',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-4B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1176',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-4C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1177',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-4D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1178',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-4E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1179',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-4F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1180',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-7E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1181',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-7F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1182',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-11C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1183',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-11D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1184',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-11A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1185',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-11B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1186',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-11E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1187',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-11F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1188',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-10A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1189',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-10B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1190',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-9C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1191',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-9D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1192',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-9A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1193',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-9B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1194',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-12A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1195',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-12B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1196',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-12C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1197',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-12D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1198',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-12E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1199',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-12F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1200',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-10E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1201',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-10F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1202',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-5A1','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1203',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-2C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1204',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-2D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1205',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-2A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1206',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-2B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1207',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-5B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1208',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-5C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1209',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-5D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1210',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-6E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1211',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','2-6F','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1212',UUID(),'24210150826969742','5636851','4151','金融基地-',F2,'金融基地','3-3栋整','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1213',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-15A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1214',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-15B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1215',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-14A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1216',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-14B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1217',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24D01','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1218',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24D02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1219',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24D03','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1220',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24A01(D)','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1221',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-25A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1222',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-25B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1223',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-25C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1224',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-25D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1225',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-26A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1226',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-26B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1227',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-26C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1228',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-26D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1229',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-18C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1230',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-18D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1231',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-11A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1232',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-11B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1233',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-11C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1234',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-11D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1235',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-12A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1236',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-12B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1237',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-12C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1238',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-12D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1239',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-20A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1240',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-20B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1241',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-20D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1242',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-20C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1243',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-21A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1244',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-21B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1245',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-21C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1246',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-21D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1247',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-22A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1248',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-22B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1249',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-22C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1250',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-22D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1251',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-16A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1252',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-16B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1253',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-16C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1254',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-16D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1255',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-8A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1256',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-8B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1257',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-8C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1258',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-8D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1259',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-7A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1260',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-7B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1261',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-7A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1262',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-7B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1263',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-1A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1264',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-3A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1265',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-3B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1266',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-9C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1267',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-9D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1268',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-13C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1269',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-13D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1270',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-1B ','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1271',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-2A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1272',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-2B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1273',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24C01','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1274',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24C02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1275',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24C03','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1276',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-9A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1277',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-9B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1278',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-14C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1279',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-14D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1280',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-2A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1281',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-2B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1282',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-2C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1283',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-2D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1284',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-7C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1285',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-7D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1286',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-13A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1287',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-13B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1288',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23A04','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1289',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-2C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1290',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-2D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1291',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-10A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1292',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-10B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1293',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-10C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1294',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-10D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1295',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-7A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1296',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-7B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1297',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-6A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1298',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-6B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1299',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-6C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1300',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-6D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1301',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-B6A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1302',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-B6B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1303',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-B6C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1304',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-B6D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1305',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23B01','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1306',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23B02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1307',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24A02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1308',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24A03','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1309',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24A04','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1310',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-24B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1311',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-4A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1312',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-4B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1313',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-4C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1314',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-4D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1315',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-5A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1316',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-5B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1317',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-5C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1318',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-5D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1319',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-3A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1320',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-3B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1321',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-3C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1322',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','B-3D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1323',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-3C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1324',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-3D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1325',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23D02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1326',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23D01','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1327',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-1D ','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1328',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23C03','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1329',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-5D01','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1330',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23A01','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1331',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23D03','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1332',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-1C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1333',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-5A01','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1334',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-5A02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1335',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-4A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1336',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-4B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1337',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-4C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1338',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-4D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1339',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-4E','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1340',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-17A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1341',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-17B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1342',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-17C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1343',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-17D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1344',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23C01','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1345',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23C02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1346',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23B03','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1347',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23B04','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1348',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-18A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1349',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-18B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1350',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-19A','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1351',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-19B','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1352',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-19D','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1353',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-19C','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1354',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23A02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1355',UUID(),'24210150826969742','5636851','4151','金融科技大厦-',F2,'金融科技大厦','A-23A02','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1356',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','A-1层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1357',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','A-2层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1358',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','A-3层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1359',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','A-4层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1360',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','A-5层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1361',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-1层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1362',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-301','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1363',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','C-1层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1364',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','C-302','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1365',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','D-4层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1366',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','D-5层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1367',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','D-2层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1368',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','D-3层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1369',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-301','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1370',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','多功能会议中心101','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1371',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','多功能会议中心102','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1372',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','多功能会议中心103','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1373',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-2楼东侧','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1374',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','C-301','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1375',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-502','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1376',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-501','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1377',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','C-501','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1378',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','C-502','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1379',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','多功能会议中心301','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1380',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','D-1层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1381',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-2层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1382',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','C-2层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1383',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','C-6层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1384',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-6层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1385',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','B-6层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1386',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','A-601','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1387',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','D-1层','2');
INSERT INTO `ehcore`.`eh_addresses` (`id`,`uuid`,`community_id`,`city_id`,`area_id`,`address`,`building_name`,`apartment_name`,`status`)
VALUES ('1388',UUID(),'24210150826969742','5636851','4151','生产力大楼-',F2,'生产力大楼','D-1层','2');

 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100000,UUID(),'深圳市宇轩网络技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100001,UUID(),'深圳市捷时行科技服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100002,UUID(),'深圳市沿海世纪地产有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100003,UUID(),'深圳市爱倍多科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100004,UUID(),'浙江宇视科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100005,UUID(),'网宿科技股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100006,UUID(),'深圳福江科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100007,UUID(),'深圳市睿智专利事务所','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100008,UUID(),'无锡盈达聚力科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100009,UUID(),'深圳市海润鑫文化传播有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100010,UUID(),'中联认证中心广东分中心','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100011,UUID(),'深圳大德飞天科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100012,UUID(),'深圳市文字传媒有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100013,UUID(),'无锡盈达聚力科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100014,UUID(),'费斯托(中国)有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100015,UUID(),'中正国际认证（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100016,UUID(),'深圳市龙晟光电有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100017,UUID(),'王才梅','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100018,UUID(),'深圳市清源铸造材料有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100019,UUID(),'费斯托(中国)有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100020,UUID(),'郭皓','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100021,UUID(),'深圳市启智有声科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100022,UUID(),'苏州工业园区艾思科技公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100023,UUID(),'深圳今日文化发展有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100024,UUID(),'深圳市车音网科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100025,UUID(),'深圳今日文化发展有限公司办公用品中心','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100026,UUID(),'深圳市炫彩科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100027,UUID(),'深圳市爱倍多科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100028,UUID(),'深圳市爱倍多科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100029,UUID(),'深圳财富支付有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100030,UUID(),'深圳桑蒲通信设备有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100031,UUID(),'深圳市精玻仪器有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100032,UUID(),'新盛合绿科技（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100033,UUID(),'深圳联智科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100034,UUID(),'深圳易莱特科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100035,UUID(),'深圳市华美视科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100036,UUID(),'深圳市拓展信息咨询有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100037,UUID(),'深圳市创飞领域科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100038,UUID(),'深圳市创飞领域科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100039,UUID(),'深圳市创飞领域科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100040,UUID(),'深圳市视显光电技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100041,UUID(),'深圳市视显光电技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100042,UUID(),'深圳市碧水蓝天科技发展有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100043,UUID(),'上海芯旺微电子技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100044,UUID(),'深圳市诺恒管理策划有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100045,UUID(),'深圳市万通食品有限责任公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100046,UUID(),'上海芯旺电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100047,UUID(),'深圳财富支付有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100048,UUID(),'深圳市天智未来科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100049,UUID(),'聚辰半导体（上海）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100050,UUID(),'聚辰半导体（上海）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100051,UUID(),'华瑞昇电子(深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100052,UUID(),'网宿科技股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100053,UUID(),'福瑞博德软件开发深圳有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100054,UUID(),'福瑞博德软件开发深圳有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100055,UUID(),'深圳市银证通云网科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100056,UUID(),'深圳市银证通云网科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100057,UUID(),'湖州明芯微电子设计有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100058,UUID(),'珠海奔图电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100059,UUID(),'深圳市巨龙城投资有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100060,UUID(),'深圳市富泓电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100061,UUID(),'深圳市安睿立电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100062,UUID(),'浙江宇视科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100063,UUID(),'广东乙纬电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100064,UUID(),'通联支付网络服务股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100065,UUID(),'中国建设银行股份有限公司深圳市分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100066,UUID(),'中国建设银行股份有限公司深圳市分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100067,UUID(),'中国建设银行股份有限公司深圳市分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100068,UUID(),'中国建设银行股份有限公司深圳市分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100069,UUID(),'安富利物流(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100070,UUID(),'神州数码系统集成服务有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100071,UUID(),'深圳市汇杰投资有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100072,UUID(),'深圳市深商创投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100073,UUID(),'阿尔科斯科技（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100074,UUID(),'深圳容德投资有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100075,UUID(),'深圳市晶科迪电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100076,UUID(),'深圳市易联科软件研发有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100077,UUID(),'广州中慧电子有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100078,UUID(),'广州中慧电子有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100079,UUID(),'广州中慧电子有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100080,UUID(),'广州中慧电子有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100081,UUID(),'广州中慧电子有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100082,UUID(),'广州中慧电子有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100083,UUID(),'爱康科商贸（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100084,UUID(),'新趣品商贸(深圳)有限公司(李继邦)','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100085,UUID(),'安信证券股份有限公司深圳科发路证券营业部','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100086,UUID(),'安信证券股份有限公司深圳科发路证券营业部','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100087,UUID(),'深圳市高科金信净化科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100088,UUID(),'深圳市康迈科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100089,UUID(),'深圳市四季轩餐饮有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100090,UUID(),'深圳市融泰衡实业有公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100091,UUID(),'深圳多多益善电子商务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100092,UUID(),'深圳市小牛普惠投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100093,UUID(),'君丰创业投资基金管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100094,UUID(),'深圳证券通信有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100095,UUID(),'深圳证券通信有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100096,UUID(),'欣旺达电子股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100097,UUID(),'深圳市瓷爱谷文化有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100098,UUID(),'神州数码信息服务股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100099,UUID(),'北京三浦教育投资有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100100,UUID(),'深圳市独尊科技开发有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100101,UUID(),'深圳市信息大成网络有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100102,UUID(),'深圳通联金融网络科技服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100103,UUID(),'深圳通联金融网络科技服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100104,UUID(),'通联支付网络服务股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100105,UUID(),'深圳市软晶科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100106,UUID(),'深圳市江波龙电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100107,UUID(),'深圳市江波龙电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100108,UUID(),'深圳市江波龙电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100109,UUID(),'深圳市江波龙电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100110,UUID(),'深圳市江波龙电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100111,UUID(),'深圳市江波龙电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100112,UUID(),'深圳市江波龙电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100113,UUID(),'深圳市江波龙电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100114,UUID(),'深圳市江波龙商用设备有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100115,UUID(),'深圳市志鼎科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100116,UUID(),'北京银联金卡科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100117,UUID(),'深圳神州数码信息技术服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100118,UUID(),'神州数码(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100119,UUID(),'神州数码(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100120,UUID(),'上海神州数码有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100121,UUID(),'深圳市中天普创投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100122,UUID(),'深圳市安卓信创业投资有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100123,UUID(),'太平人寿保险有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100124,UUID(),'和阳（深圳）投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100125,UUID(),'深圳市昆鹏股权投资基金管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100126,UUID(),'深圳市盘龙环境技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100127,UUID(),'深圳壹昊金融控股股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100128,UUID(),'深圳市贷帮投资担保有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100129,UUID(),'深圳市人和投资集团有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100130,UUID(),'川奇光电科技（扬州）有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100131,UUID(),'川元电子（扬州）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100132,UUID(),'深圳市志鼎科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100133,UUID(),'深圳市惠邦知识产权代理事务所','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100134,UUID(),'通联支付网络服务股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100135,UUID(),'深圳嘉德瑞碳资产股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100136,UUID(),'深圳前海南方增长资产管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100137,UUID(),'安富利物流(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100138,UUID(),'安富利物流(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100139,UUID(),'安富利物流(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100140,UUID(),'安富利物流(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100141,UUID(),'安富利物流(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100142,UUID(),'安富利物流(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100143,UUID(),'萃冠电子贸易（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100144,UUID(),'萃冠电子贸易（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100145,UUID(),'萃冠电子贸易（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100146,UUID(),'萃冠电子贸易（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100147,UUID(),'萃冠电子贸易（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100148,UUID(),'萃冠电子贸易（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100149,UUID(),'安富利物流(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100150,UUID(),'安霸半导体技术(上海)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100151,UUID(),'安霸半导体技术(上海)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100152,UUID(),'深圳中泽明芯集团有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100153,UUID(),'深圳市漫步者科技股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100154,UUID(),'是德科技（中国）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100155,UUID(),'是德科技（中国）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100156,UUID(),'博尔科通讯系统（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100157,UUID(),'招商银行股份有限公司深圳车公庙支行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100158,UUID(),'招商银行股份有限公司深圳车公庙支行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100159,UUID(),'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100160,UUID(),'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100161,UUID(),'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100162,UUID(),'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100163,UUID(),'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100164,UUID(),'深圳市润利丰实业发展有限公司（老绍兴酒楼）','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100165,UUID(),'深圳市广源餐饮管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100166,UUID(),'深圳市广源餐饮管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100167,UUID(),'上海联广认证有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100168,UUID(),'上海联广认证有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100169,UUID(),'广东万诺律师事务所(李亚光)','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100170,UUID(),'杭州华三通信技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100171,UUID(),'杭州华三通信技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100172,UUID(),'深圳市恒源昊资产管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100173,UUID(),'深圳市恒源昊资产管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100174,UUID(),'深圳前海星润股权投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100175,UUID(),'深圳前海星润股权投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100176,UUID(),'深圳前海星润股权投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100177,UUID(),'深圳前海星润股权投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100178,UUID(),'深圳前海星润股权投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100179,UUID(),'深圳前海星润股权投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100180,UUID(),'塔塔信息技术(中国)股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100181,UUID(),'塔塔信息技术(中国)股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100182,UUID(),'深圳格兰泰克科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100183,UUID(),'深圳格兰泰克科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100184,UUID(),'深圳市民声科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100185,UUID(),'深圳市民声科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100186,UUID(),'深圳市民声科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100187,UUID(),'深圳市民声科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100188,UUID(),'深圳证券通信有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100189,UUID(),'深圳证券通信有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100190,UUID(),'深圳五维微品金融信息服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100191,UUID(),'深圳五维微品金融信息服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100192,UUID(),'深圳新为软件股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100193,UUID(),'深圳新为软件股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100194,UUID(),'深圳市瑞格尔健康管理科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100195,UUID(),'深圳市瑞格尔健康管理科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100196,UUID(),'深圳市瑞格尔健康管理科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100197,UUID(),'深圳市瑞格尔健康管理科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100198,UUID(),'深圳市瑞格尔健康管理科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100199,UUID(),'深圳市瑞格尔健康管理科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100200,UUID(),'深圳市瑞格尔健康管理科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100201,UUID(),'深圳市瑞格尔健康管理科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100202,UUID(),'深圳格兰泰克汽车电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100203,UUID(),'华美优科网络技术（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100204,UUID(),'华美优科网络技术（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100205,UUID(),'华美优科网络技术（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100206,UUID(),'华美优科网络技术（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100207,UUID(),'华美优科网络技术（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100208,UUID(),'华美优科网络技术（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100209,UUID(),'华美优科网络技术（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100210,UUID(),'安霸半导体技术(上海)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100211,UUID(),'安霸半导体技术(上海)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100212,UUID(),'吴炎雄(深圳市南山区南风舍餐馆)','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100213,UUID(),'深圳集群壹家股权投资基金管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100214,UUID(),'深圳集群壹家股权投资基金管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100215,UUID(),'深圳华智融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100216,UUID(),'深圳华智融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100217,UUID(),'深圳华智融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100218,UUID(),'深圳华智融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100219,UUID(),'深圳华智融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100220,UUID(),'深圳华智融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100221,UUID(),'深圳市智美达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100222,UUID(),'深圳市智美达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100223,UUID(),'深圳市智美达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100224,UUID(),'深圳市智美达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100225,UUID(),'深圳市智美达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100226,UUID(),'深圳市智美达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100227,UUID(),'深圳市智美达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100228,UUID(),'深圳市智美达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100229,UUID(),'东吴证券股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100230,UUID(),'东吴证券股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100231,UUID(),'湖南南方稀贵金属交易所股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100232,UUID(),'湖南南方稀贵金属交易所股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100233,UUID(),'湖南南方稀贵金属交易所股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100234,UUID(),'湖南南方稀贵金属交易所股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100235,UUID(),'深圳市佳信捷技术股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100236,UUID(),'深圳市佳信捷技术股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100237,UUID(),'深圳市佳信捷技术股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100238,UUID(),'深圳市佳信捷技术股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100239,UUID(),'深圳市中兴小额贷款有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100240,UUID(),'深圳市中兴小额贷款有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100241,UUID(),'深圳市中兴小额贷款有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100242,UUID(),'深圳市研信科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100243,UUID(),'深圳中兴飞贷金融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100244,UUID(),'深圳中兴飞贷金融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100245,UUID(),'深圳中兴飞贷金融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100246,UUID(),'深圳中兴飞贷金融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100247,UUID(),'深圳中兴飞贷金融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100248,UUID(),'深圳中兴飞贷金融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100249,UUID(),'深圳中兴飞贷金融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100250,UUID(),'深圳中兴飞贷金融科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100251,UUID(),'深圳市芯智科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100252,UUID(),'深圳市芯智科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100253,UUID(),'深圳市芯智科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100254,UUID(),'深圳市芯智科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100255,UUID(),'珠海华润银行股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100256,UUID(),'珠海华润银行股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100257,UUID(),'珠海华润银行股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100258,UUID(),'珠海华润银行股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100259,UUID(),'珠海华润银行股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100260,UUID(),'珠海华润银行股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100261,UUID(),'珠海华润银行股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100262,UUID(),'珠海华润银行股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100263,UUID(),'中国银行股份有限公司深圳市分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100264,UUID(),'中国银行股份有限公司深圳市分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100265,UUID(),'中国银行股份有限公司深圳市分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100266,UUID(),'众华会计师事务所(特殊普通合伙)深圳分所','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100267,UUID(),'众华会计师事务所(特殊普通合伙)深圳分所','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100268,UUID(),'同方(深圳)云计算技术股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100269,UUID(),'同方(深圳)云计算技术股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100270,UUID(),'杭州银行股份有限公司深圳分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100271,UUID(),'杭州银行股份有限公司深圳分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100272,UUID(),'杭州银行股份有限公司深圳分行','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100273,UUID(),'泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100274,UUID(),'泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100275,UUID(),'泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100276,UUID(),'深圳太东资本管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100277,UUID(),'深圳太东资本管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100278,UUID(),'中国人民健康保险公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100279,UUID(),'中国人民健康保险公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100280,UUID(),'深圳排放权交易所有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100281,UUID(),'深圳排放权交易所有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100282,UUID(),'深圳排放权交易所有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100283,UUID(),'深圳排放权交易所有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100284,UUID(),'深圳市慧峰高科产业园投资发展有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100285,UUID(),'深圳市慧峰高科产业园投资发展有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100286,UUID(),'丰益(上海)信息技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100287,UUID(),'丰益(上海)信息技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100288,UUID(),'深圳市鼎恒瑞投资有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100289,UUID(),'惠州硕贝德无线科技股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100290,UUID(),'惠州硕贝德无线科技股份有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100291,UUID(),'深圳市前海德融资本管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100292,UUID(),'深圳市前海德融资本管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100293,UUID(),'深圳市前海德融资本管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100294,UUID(),'深圳市前海德融资本管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100295,UUID(),'深圳市新天域文化产业有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100296,UUID(),'深圳市新天域文化产业有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100297,UUID(),'深圳前海厚诚敏投资控股有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100298,UUID(),'深圳前海厚诚敏投资控股有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100299,UUID(),'深圳前海厚诚敏投资控股有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100300,UUID(),'深圳前海厚诚敏投资控股有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100301,UUID(),'深圳市多元世纪信息技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100302,UUID(),'深圳市多元世纪信息技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100303,UUID(),'深圳市多元世纪信息技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100304,UUID(),'深圳市多元世纪信息技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100305,UUID(),'深圳市工夫百味投资有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100306,UUID(),'深圳市工夫百味投资有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100307,UUID(),'北京集创北方科技有限公司深圳办事处','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100308,UUID(),'北京集创北方科技有限公司深圳办事处','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100309,UUID(),'北京集创北方科技有限公司深圳办事处','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100310,UUID(),'北京集创北方科技有限公司深圳办事处','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100311,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100312,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100313,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100314,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100315,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100316,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100317,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100318,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100319,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100320,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100321,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100322,UUID(),'普联技术有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100323,UUID(),'上海赫丝蒂化妆品有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100324,UUID(),'上海赫丝蒂化妆品有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100325,UUID(),'深圳市恒顺合鑫科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100326,UUID(),'深圳市华讯万通科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100327,UUID(),'中信证券股份有限公司深圳科技园科苑路证券营业部','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100328,UUID(),'深圳市百益亚太电效工程有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100329,UUID(),'移动财经软件（深圳）有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100330,UUID(),'深圳金桥融付科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100331,UUID(),'深圳金桥融付科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100332,UUID(),'深圳市浅葱小唱音乐餐厅有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100333,UUID(),'深圳市城道通环保科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100334,UUID(),'深圳市城道通环保科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100335,UUID(),'北京玖富时代投资顾问有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100336,UUID(),'深圳易兰德金融服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100337,UUID(),'深圳易兰德金融服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100338,UUID(),'深圳易兰德金融服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100339,UUID(),'深圳易兰德金融服务有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100340,UUID(),'深圳铂睿智恒科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100341,UUID(),'深圳铂睿智恒科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100342,UUID(),'深圳铂睿智恒科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100343,UUID(),'深圳铂睿智恒科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100344,UUID(),'深圳市宽域智联科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100345,UUID(),'深圳市宽域智联科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100346,UUID(),'深圳前海方舟资本管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100347,UUID(),'深圳前海方舟资本管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100348,UUID(),'前海东方金德资产管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100349,UUID(),'前海东方金德资产管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100350,UUID(),'中民保险经纪股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100351,UUID(),'中民保险经纪股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100352,UUID(),'中民保险经纪股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100353,UUID(),'中民电子商务股份有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100354,UUID(),'深圳市前海野文投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100355,UUID(),'深圳市前海野文投资管理有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100356,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100357,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100358,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100359,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100360,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100361,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100362,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100363,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100364,UUID(),'英伟达半导体(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100365,UUID(),'百度(中国)有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100366,UUID(),'百度(中国)有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100367,UUID(),'北京百度网讯科技有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100368,UUID(),'北京百度网讯科技有限公司深圳分公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100369,UUID(),'深圳市福尔科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100370,UUID(),'深圳市德维莱科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100371,UUID(),'深圳市宝富利科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100372,UUID(),'深圳市大维纳米科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100373,UUID(),'金宝通电子(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100374,UUID(),'深圳市亮信科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100375,UUID(),'汎达科技(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100376,UUID(),'群锋电子(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100377,UUID(),'诺华达电子(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100378,UUID(),'新纬科技(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100379,UUID(),'深圳市领平科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100380,UUID(),'北京美餐巧达科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100381,UUID(),'镭射谷科(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100382,UUID(),'镭射谷科(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100383,UUID(),'镭射谷科(深圳)有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100384,UUID(),'深圳市安普盛科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100385,UUID(),'深圳市安特讯科技有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100386,UUID(),'深圳市开拓汽车电子有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100387,UUID(),'深圳深港生产力基地有限公司','','1','0','24210150826969742','enterprise'); 
 INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`) 
VALUES(100388,UUID(),'深圳市商业联合会','','1','0','24210150826969742','enterprise'); 


INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100000','100000','1000','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100001','100001','1001','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100002','100002','1002','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100003','100003','1003','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100004','100004','1004','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100005','100005','1005','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100006','100006','1006','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100007','100007','1007','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100008','100008','1008','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100009','100009','1009','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100010','100010','1010','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100011','100011','1011','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100012','100012','1012','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100013','100013','1013','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100014','100014','1014','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100015','100015','1015','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100016','100016','1016','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100017','100017','1017','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100018','100018','1018','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100019','100019','1019','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100020','100020','1020','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100021','100021','1021','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100022','100022','1022','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100023','100023','1023','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100024','100024','1024','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100025','100025','1025','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100026','100026','1026','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100027','100027','1027','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100028','100028','1028','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100029','100029','1029','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100030','100030','1030','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100031','100031','1031','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100032','100032','1032','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100033','100033','1033','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100034','100034','1034','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100035','100035','1035','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100036','100036','1036','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100037','100037','1037','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100038','100038','1038','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100039','100039','1039','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100040','100040','1040','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100041','100041','1041','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100042','100042','1042','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100043','100043','1043','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100044','100044','1044','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100045','100045','1045','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100046','100046','1046','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100047','100047','1047','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100048','100048','1048','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100049','100049','1049','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100050','100050','1050','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100051','100051','1051','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100052','100052','1052','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100053','100053','1053','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100054','100054','1054','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100055','100055','1055','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100056','100056','1056','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100057','100057','1057','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100058','100058','1058','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100059','100059','1059','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100060','100060','1060','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100061','100061','1061','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100062','100062','1062','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100063','100063','1063','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100064','100064','1064','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100065','100065','1065','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100066','100066','1066','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100067','100067','1067','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100068','100068','1068','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100069','100069','1069','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100070','100070','1070','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100071','100071','1071','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100072','100072','1072','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100073','100073','1073','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100074','100074','1074','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100075','100075','1075','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100076','100076','1076','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100077','100077','1077','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100078','100078','1078','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100079','100079','1079','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100080','100080','1080','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100081','100081','1081','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100082','100082','1082','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100083','100083','1083','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100084','100084','1084','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100085','100085','1085','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100086','100086','1086','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100087','100087','1087','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100088','100088','1088','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100089','100089','1089','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100090','100090','1090','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100091','100091','1091','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100092','100092','1092','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100093','100093','1093','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100094','100094','1094','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100095','100095','1095','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100096','100096','1096','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100097','100097','1097','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100098','100098','1098','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100099','100099','1099','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100100','100100','1100','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100101','100101','1101','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100102','100102','1102','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100103','100103','1103','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100104','100104','1104','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100105','100105','1105','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100106','100106','1106','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100107','100107','1107','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100108','100108','1108','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100109','100109','1109','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100110','100110','1110','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100111','100111','1111','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100112','100112','1112','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100113','100113','1113','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100114','100114','1114','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100115','100115','1115','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100116','100116','1116','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100117','100117','1117','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100118','100118','1118','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100119','100119','1119','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100120','100120','1120','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100121','100121','1121','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100122','100122','1122','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100123','100123','1123','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100124','100124','1124','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100125','100125','1125','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100126','100126','1126','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100127','100127','1127','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100128','100128','1128','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100129','100129','1129','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100130','100130','1130','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100131','100131','1131','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100132','100132','1132','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100133','100133','1133','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100134','100134','1134','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100135','100135','1135','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100136','100136','1136','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100137','100137','1137','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100138','100138','1138','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100139','100139','1139','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100140','100140','1140','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100141','100141','1141','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100142','100142','1142','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100143','100143','1143','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100144','100144','1144','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100145','100145','1145','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100146','100146','1146','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100147','100147','1147','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100148','100148','1148','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100149','100149','1149','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100150','100150','1150','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100151','100151','1151','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100152','100152','1152','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100153','100153','1153','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100154','100154','1154','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100155','100155','1155','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100156','100156','1156','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100157','100157','1157','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100158','100158','1158','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100159','100159','1159','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100160','100160','1160','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100161','100161','1161','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100162','100162','1162','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100163','100163','1163','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100164','100164','1164','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100165','100165','1165','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100166','100166','1166','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100167','100167','1167','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100168','100168','1168','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100169','100169','1169','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100170','100170','1170','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100171','100171','1171','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100172','100172','1172','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100173','100173','1173','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100174','100174','1174','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100175','100175','1175','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100176','100176','1176','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100177','100177','1177','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100178','100178','1178','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100179','100179','1179','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100180','100180','1180','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100181','100181','1181','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100182','100182','1182','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100183','100183','1183','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100184','100184','1184','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100185','100185','1185','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100186','100186','1186','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100187','100187','1187','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100188','100188','1188','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100189','100189','1189','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100190','100190','1190','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100191','100191','1191','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100192','100192','1192','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100193','100193','1193','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100194','100194','1194','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100195','100195','1195','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100196','100196','1196','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100197','100197','1197','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100198','100198','1198','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100199','100199','1199','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100200','100200','1200','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100201','100201','1201','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100202','100202','1202','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100203','100203','1203','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100204','100204','1204','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100205','100205','1205','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100206','100206','1206','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100207','100207','1207','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100208','100208','1208','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100209','100209','1209','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100210','100210','1210','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100211','100211','1211','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100212','100212','1212','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100213','100213','1213','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100214','100214','1214','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100215','100215','1215','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100216','100216','1216','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100217','100217','1217','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100218','100218','1218','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100219','100219','1219','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100220','100220','1220','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100221','100221','1221','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100222','100222','1222','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100223','100223','1223','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100224','100224','1224','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100225','100225','1225','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100226','100226','1226','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100227','100227','1227','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100228','100228','1228','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100229','100229','1229','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100230','100230','1230','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100231','100231','1231','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100232','100232','1232','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100233','100233','1233','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100234','100234','1234','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100235','100235','1235','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100236','100236','1236','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100237','100237','1237','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100238','100238','1238','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100239','100239','1239','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100240','100240','1240','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100241','100241','1241','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100242','100242','1242','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100243','100243','1243','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100244','100244','1244','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100245','100245','1245','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100246','100246','1246','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100247','100247','1247','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100248','100248','1248','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100249','100249','1249','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100250','100250','1250','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100251','100251','1251','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100252','100252','1252','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100253','100253','1253','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100254','100254','1254','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100255','100255','1255','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100256','100256','1256','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100257','100257','1257','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100258','100258','1258','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100259','100259','1259','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100260','100260','1260','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100261','100261','1261','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100262','100262','1262','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100263','100263','1263','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100264','100264','1264','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100265','100265','1265','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100266','100266','1266','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100267','100267','1267','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100268','100268','1268','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100269','100269','1269','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100270','100270','1270','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100271','100271','1271','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100272','100272','1272','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100273','100273','1273','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100274','100274','1274','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100275','100275','1275','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100276','100276','1276','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100277','100277','1277','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100278','100278','1278','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100279','100279','1279','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100280','100280','1280','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100281','100281','1281','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100282','100282','1282','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100283','100283','1283','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100284','100284','1284','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100285','100285','1285','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100286','100286','1286','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100287','100287','1287','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100288','100288','1288','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100289','100289','1289','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100290','100290','1290','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100291','100291','1291','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100292','100292','1292','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100293','100293','1293','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100294','100294','1294','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100295','100295','1295','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100296','100296','1296','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100297','100297','1297','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100298','100298','1298','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100299','100299','1299','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100300','100300','1300','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100301','100301','1301','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100302','100302','1302','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100303','100303','1303','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100304','100304','1304','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100305','100305','1305','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100306','100306','1306','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100307','100307','1307','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100308','100308','1308','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100309','100309','1309','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100310','100310','1310','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100311','100311','1311','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100312','100312','1312','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100313','100313','1313','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100314','100314','1314','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100315','100315','1315','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100316','100316','1316','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100317','100317','1317','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100318','100318','1318','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100319','100319','1319','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100320','100320','1320','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100321','100321','1321','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100322','100322','1322','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100323','100323','1323','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100324','100324','1324','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100325','100325','1325','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100326','100326','1326','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100327','100327','1327','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100328','100328','1328','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100329','100329','1329','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100330','100330','1330','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100331','100331','1331','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100332','100332','1332','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100333','100333','1333','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100334','100334','1334','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100335','100335','1335','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100336','100336','1336','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100337','100337','1337','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100338','100338','1338','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100339','100339','1339','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100340','100340','1340','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100341','100341','1341','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100342','100342','1342','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100343','100343','1343','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100344','100344','1344','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100345','100345','1345','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100346','100346','1346','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100347','100347','1347','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100348','100348','1348','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100349','100349','1349','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100350','100350','1350','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100351','100351','1351','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100352','100352','1352','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100353','100353','1353','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100354','100354','1354','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100355','100355','1355','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100356','100356','1356','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100357','100357','1357','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100358','100358','1358','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100359','100359','1359','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100360','100360','1360','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100361','100361','1361','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100362','100362','1362','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100363','100363','1363','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100364','100364','1364','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100365','100365','1365','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100366','100366','1366','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100367','100367','1367','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100368','100368','1368','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100369','100369','1369','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100370','100370','1370','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100371','100371','1371','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100372','100372','1372','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100373','100373','1373','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100374','100374','1374','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100375','100375','1375','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100376','100376','1376','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100377','100377','1377','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100378','100378','1378','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100379','100379','1379','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100380','100380','1380','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100381','100381','1381','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100382','100382','1382','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100383','100383','1383','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100384','100384','1384','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100385','100385','1385','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100386','100386','1386','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100387','100387','1387','2');
INSERT INTO `eh_enterprise_addresses` (`id`, `enterprise_id`, `address_id`, `status`) VALUES('100388','100388','1388','2');

INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100000','24210150826969742','enterprise','100000','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100001','24210150826969742','enterprise','100001','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100002','24210150826969742','enterprise','100002','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100003','24210150826969742','enterprise','100003','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100004','24210150826969742','enterprise','100004','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100005','24210150826969742','enterprise','100005','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100006','24210150826969742','enterprise','100006','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100007','24210150826969742','enterprise','100007','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100008','24210150826969742','enterprise','100008','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100009','24210150826969742','enterprise','100009','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100010','24210150826969742','enterprise','100010','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100011','24210150826969742','enterprise','100011','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100012','24210150826969742','enterprise','100012','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100013','24210150826969742','enterprise','100013','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100014','24210150826969742','enterprise','100014','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100015','24210150826969742','enterprise','100015','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100016','24210150826969742','enterprise','100016','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100017','24210150826969742','enterprise','100017','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100018','24210150826969742','enterprise','100018','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100019','24210150826969742','enterprise','100019','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100020','24210150826969742','enterprise','100020','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100021','24210150826969742','enterprise','100021','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100022','24210150826969742','enterprise','100022','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100023','24210150826969742','enterprise','100023','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100024','24210150826969742','enterprise','100024','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100025','24210150826969742','enterprise','100025','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100026','24210150826969742','enterprise','100026','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100027','24210150826969742','enterprise','100027','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100028','24210150826969742','enterprise','100028','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100029','24210150826969742','enterprise','100029','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100030','24210150826969742','enterprise','100030','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100031','24210150826969742','enterprise','100031','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100032','24210150826969742','enterprise','100032','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100033','24210150826969742','enterprise','100033','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100034','24210150826969742','enterprise','100034','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100035','24210150826969742','enterprise','100035','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100036','24210150826969742','enterprise','100036','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100037','24210150826969742','enterprise','100037','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100038','24210150826969742','enterprise','100038','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100039','24210150826969742','enterprise','100039','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100040','24210150826969742','enterprise','100040','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100041','24210150826969742','enterprise','100041','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100042','24210150826969742','enterprise','100042','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100043','24210150826969742','enterprise','100043','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100044','24210150826969742','enterprise','100044','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100045','24210150826969742','enterprise','100045','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100046','24210150826969742','enterprise','100046','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100047','24210150826969742','enterprise','100047','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100048','24210150826969742','enterprise','100048','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100049','24210150826969742','enterprise','100049','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100050','24210150826969742','enterprise','100050','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100051','24210150826969742','enterprise','100051','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100052','24210150826969742','enterprise','100052','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100053','24210150826969742','enterprise','100053','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100054','24210150826969742','enterprise','100054','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100055','24210150826969742','enterprise','100055','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100056','24210150826969742','enterprise','100056','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100057','24210150826969742','enterprise','100057','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100058','24210150826969742','enterprise','100058','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100059','24210150826969742','enterprise','100059','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100060','24210150826969742','enterprise','100060','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100061','24210150826969742','enterprise','100061','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100062','24210150826969742','enterprise','100062','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100063','24210150826969742','enterprise','100063','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100064','24210150826969742','enterprise','100064','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100065','24210150826969742','enterprise','100065','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100066','24210150826969742','enterprise','100066','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100067','24210150826969742','enterprise','100067','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100068','24210150826969742','enterprise','100068','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100069','24210150826969742','enterprise','100069','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100070','24210150826969742','enterprise','100070','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100071','24210150826969742','enterprise','100071','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100072','24210150826969742','enterprise','100072','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100073','24210150826969742','enterprise','100073','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100074','24210150826969742','enterprise','100074','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100075','24210150826969742','enterprise','100075','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100076','24210150826969742','enterprise','100076','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100077','24210150826969742','enterprise','100077','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100078','24210150826969742','enterprise','100078','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100079','24210150826969742','enterprise','100079','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100080','24210150826969742','enterprise','100080','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100081','24210150826969742','enterprise','100081','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100082','24210150826969742','enterprise','100082','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100083','24210150826969742','enterprise','100083','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100084','24210150826969742','enterprise','100084','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100085','24210150826969742','enterprise','100085','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100086','24210150826969742','enterprise','100086','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100087','24210150826969742','enterprise','100087','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100088','24210150826969742','enterprise','100088','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100089','24210150826969742','enterprise','100089','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100090','24210150826969742','enterprise','100090','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100091','24210150826969742','enterprise','100091','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100092','24210150826969742','enterprise','100092','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100093','24210150826969742','enterprise','100093','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100094','24210150826969742','enterprise','100094','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100095','24210150826969742','enterprise','100095','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100096','24210150826969742','enterprise','100096','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100097','24210150826969742','enterprise','100097','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100098','24210150826969742','enterprise','100098','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100099','24210150826969742','enterprise','100099','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100100','24210150826969742','enterprise','100100','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100101','24210150826969742','enterprise','100101','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100102','24210150826969742','enterprise','100102','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100103','24210150826969742','enterprise','100103','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100104','24210150826969742','enterprise','100104','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100105','24210150826969742','enterprise','100105','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100106','24210150826969742','enterprise','100106','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100107','24210150826969742','enterprise','100107','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100108','24210150826969742','enterprise','100108','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100109','24210150826969742','enterprise','100109','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100110','24210150826969742','enterprise','100110','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100111','24210150826969742','enterprise','100111','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100112','24210150826969742','enterprise','100112','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100113','24210150826969742','enterprise','100113','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100114','24210150826969742','enterprise','100114','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100115','24210150826969742','enterprise','100115','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100116','24210150826969742','enterprise','100116','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100117','24210150826969742','enterprise','100117','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100118','24210150826969742','enterprise','100118','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100119','24210150826969742','enterprise','100119','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100120','24210150826969742','enterprise','100120','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100121','24210150826969742','enterprise','100121','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100122','24210150826969742','enterprise','100122','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100123','24210150826969742','enterprise','100123','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100124','24210150826969742','enterprise','100124','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100125','24210150826969742','enterprise','100125','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100126','24210150826969742','enterprise','100126','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100127','24210150826969742','enterprise','100127','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100128','24210150826969742','enterprise','100128','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100129','24210150826969742','enterprise','100129','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100130','24210150826969742','enterprise','100130','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100131','24210150826969742','enterprise','100131','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100132','24210150826969742','enterprise','100132','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100133','24210150826969742','enterprise','100133','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100134','24210150826969742','enterprise','100134','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100135','24210150826969742','enterprise','100135','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100136','24210150826969742','enterprise','100136','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100137','24210150826969742','enterprise','100137','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100138','24210150826969742','enterprise','100138','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100139','24210150826969742','enterprise','100139','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100140','24210150826969742','enterprise','100140','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100141','24210150826969742','enterprise','100141','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100142','24210150826969742','enterprise','100142','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100143','24210150826969742','enterprise','100143','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100144','24210150826969742','enterprise','100144','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100145','24210150826969742','enterprise','100145','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100146','24210150826969742','enterprise','100146','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100147','24210150826969742','enterprise','100147','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100148','24210150826969742','enterprise','100148','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100149','24210150826969742','enterprise','100149','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100150','24210150826969742','enterprise','100150','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100151','24210150826969742','enterprise','100151','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100152','24210150826969742','enterprise','100152','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100153','24210150826969742','enterprise','100153','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100154','24210150826969742','enterprise','100154','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100155','24210150826969742','enterprise','100155','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100156','24210150826969742','enterprise','100156','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100157','24210150826969742','enterprise','100157','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100158','24210150826969742','enterprise','100158','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100159','24210150826969742','enterprise','100159','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100160','24210150826969742','enterprise','100160','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100161','24210150826969742','enterprise','100161','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100162','24210150826969742','enterprise','100162','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100163','24210150826969742','enterprise','100163','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100164','24210150826969742','enterprise','100164','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100165','24210150826969742','enterprise','100165','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100166','24210150826969742','enterprise','100166','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100167','24210150826969742','enterprise','100167','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100168','24210150826969742','enterprise','100168','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100169','24210150826969742','enterprise','100169','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100170','24210150826969742','enterprise','100170','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100171','24210150826969742','enterprise','100171','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100172','24210150826969742','enterprise','100172','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100173','24210150826969742','enterprise','100173','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100174','24210150826969742','enterprise','100174','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100175','24210150826969742','enterprise','100175','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100176','24210150826969742','enterprise','100176','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100177','24210150826969742','enterprise','100177','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100178','24210150826969742','enterprise','100178','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100179','24210150826969742','enterprise','100179','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100180','24210150826969742','enterprise','100180','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100181','24210150826969742','enterprise','100181','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100182','24210150826969742','enterprise','100182','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100183','24210150826969742','enterprise','100183','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100184','24210150826969742','enterprise','100184','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100185','24210150826969742','enterprise','100185','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100186','24210150826969742','enterprise','100186','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100187','24210150826969742','enterprise','100187','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100188','24210150826969742','enterprise','100188','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100189','24210150826969742','enterprise','100189','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100190','24210150826969742','enterprise','100190','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100191','24210150826969742','enterprise','100191','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100192','24210150826969742','enterprise','100192','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100193','24210150826969742','enterprise','100193','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100194','24210150826969742','enterprise','100194','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100195','24210150826969742','enterprise','100195','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100196','24210150826969742','enterprise','100196','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100197','24210150826969742','enterprise','100197','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100198','24210150826969742','enterprise','100198','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100199','24210150826969742','enterprise','100199','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100200','24210150826969742','enterprise','100200','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100201','24210150826969742','enterprise','100201','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100202','24210150826969742','enterprise','100202','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100203','24210150826969742','enterprise','100203','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100204','24210150826969742','enterprise','100204','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100205','24210150826969742','enterprise','100205','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100206','24210150826969742','enterprise','100206','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100207','24210150826969742','enterprise','100207','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100208','24210150826969742','enterprise','100208','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100209','24210150826969742','enterprise','100209','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100210','24210150826969742','enterprise','100210','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100211','24210150826969742','enterprise','100211','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100212','24210150826969742','enterprise','100212','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100213','24210150826969742','enterprise','100213','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100214','24210150826969742','enterprise','100214','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100215','24210150826969742','enterprise','100215','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100216','24210150826969742','enterprise','100216','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100217','24210150826969742','enterprise','100217','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100218','24210150826969742','enterprise','100218','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100219','24210150826969742','enterprise','100219','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100220','24210150826969742','enterprise','100220','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100221','24210150826969742','enterprise','100221','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100222','24210150826969742','enterprise','100222','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100223','24210150826969742','enterprise','100223','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100224','24210150826969742','enterprise','100224','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100225','24210150826969742','enterprise','100225','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100226','24210150826969742','enterprise','100226','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100227','24210150826969742','enterprise','100227','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100228','24210150826969742','enterprise','100228','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100229','24210150826969742','enterprise','100229','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100230','24210150826969742','enterprise','100230','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100231','24210150826969742','enterprise','100231','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100232','24210150826969742','enterprise','100232','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100233','24210150826969742','enterprise','100233','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100234','24210150826969742','enterprise','100234','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100235','24210150826969742','enterprise','100235','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100236','24210150826969742','enterprise','100236','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100237','24210150826969742','enterprise','100237','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100238','24210150826969742','enterprise','100238','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100239','24210150826969742','enterprise','100239','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100240','24210150826969742','enterprise','100240','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100241','24210150826969742','enterprise','100241','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100242','24210150826969742','enterprise','100242','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100243','24210150826969742','enterprise','100243','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100244','24210150826969742','enterprise','100244','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100245','24210150826969742','enterprise','100245','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100246','24210150826969742','enterprise','100246','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100247','24210150826969742','enterprise','100247','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100248','24210150826969742','enterprise','100248','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100249','24210150826969742','enterprise','100249','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100250','24210150826969742','enterprise','100250','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100251','24210150826969742','enterprise','100251','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100252','24210150826969742','enterprise','100252','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100253','24210150826969742','enterprise','100253','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100254','24210150826969742','enterprise','100254','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100255','24210150826969742','enterprise','100255','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100256','24210150826969742','enterprise','100256','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100257','24210150826969742','enterprise','100257','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100258','24210150826969742','enterprise','100258','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100259','24210150826969742','enterprise','100259','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100260','24210150826969742','enterprise','100260','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100261','24210150826969742','enterprise','100261','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100262','24210150826969742','enterprise','100262','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100263','24210150826969742','enterprise','100263','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100264','24210150826969742','enterprise','100264','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100265','24210150826969742','enterprise','100265','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100266','24210150826969742','enterprise','100266','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100267','24210150826969742','enterprise','100267','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100268','24210150826969742','enterprise','100268','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100269','24210150826969742','enterprise','100269','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100270','24210150826969742','enterprise','100270','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100271','24210150826969742','enterprise','100271','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100272','24210150826969742','enterprise','100272','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100273','24210150826969742','enterprise','100273','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100274','24210150826969742','enterprise','100274','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100275','24210150826969742','enterprise','100275','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100276','24210150826969742','enterprise','100276','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100277','24210150826969742','enterprise','100277','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100278','24210150826969742','enterprise','100278','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100279','24210150826969742','enterprise','100279','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100280','24210150826969742','enterprise','100280','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100281','24210150826969742','enterprise','100281','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100282','24210150826969742','enterprise','100282','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100283','24210150826969742','enterprise','100283','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100284','24210150826969742','enterprise','100284','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100285','24210150826969742','enterprise','100285','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100286','24210150826969742','enterprise','100286','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100287','24210150826969742','enterprise','100287','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100288','24210150826969742','enterprise','100288','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100289','24210150826969742','enterprise','100289','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100290','24210150826969742','enterprise','100290','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100291','24210150826969742','enterprise','100291','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100292','24210150826969742','enterprise','100292','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100293','24210150826969742','enterprise','100293','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100294','24210150826969742','enterprise','100294','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100295','24210150826969742','enterprise','100295','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100296','24210150826969742','enterprise','100296','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100297','24210150826969742','enterprise','100297','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100298','24210150826969742','enterprise','100298','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100299','24210150826969742','enterprise','100299','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100300','24210150826969742','enterprise','100300','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100301','24210150826969742','enterprise','100301','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100302','24210150826969742','enterprise','100302','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100303','24210150826969742','enterprise','100303','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100304','24210150826969742','enterprise','100304','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100305','24210150826969742','enterprise','100305','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100306','24210150826969742','enterprise','100306','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100307','24210150826969742','enterprise','100307','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100308','24210150826969742','enterprise','100308','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100309','24210150826969742','enterprise','100309','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100310','24210150826969742','enterprise','100310','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100311','24210150826969742','enterprise','100311','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100312','24210150826969742','enterprise','100312','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100313','24210150826969742','enterprise','100313','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100314','24210150826969742','enterprise','100314','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100315','24210150826969742','enterprise','100315','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100316','24210150826969742','enterprise','100316','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100317','24210150826969742','enterprise','100317','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100318','24210150826969742','enterprise','100318','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100319','24210150826969742','enterprise','100319','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100320','24210150826969742','enterprise','100320','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100321','24210150826969742','enterprise','100321','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100322','24210150826969742','enterprise','100322','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100323','24210150826969742','enterprise','100323','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100324','24210150826969742','enterprise','100324','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100325','24210150826969742','enterprise','100325','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100326','24210150826969742','enterprise','100326','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100327','24210150826969742','enterprise','100327','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100328','24210150826969742','enterprise','100328','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100329','24210150826969742','enterprise','100329','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100330','24210150826969742','enterprise','100330','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100331','24210150826969742','enterprise','100331','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100332','24210150826969742','enterprise','100332','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100333','24210150826969742','enterprise','100333','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100334','24210150826969742','enterprise','100334','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100335','24210150826969742','enterprise','100335','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100336','24210150826969742','enterprise','100336','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100337','24210150826969742','enterprise','100337','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100338','24210150826969742','enterprise','100338','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100339','24210150826969742','enterprise','100339','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100340','24210150826969742','enterprise','100340','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100341','24210150826969742','enterprise','100341','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100342','24210150826969742','enterprise','100342','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100343','24210150826969742','enterprise','100343','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100344','24210150826969742','enterprise','100344','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100345','24210150826969742','enterprise','100345','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100346','24210150826969742','enterprise','100346','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100347','24210150826969742','enterprise','100347','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100348','24210150826969742','enterprise','100348','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100349','24210150826969742','enterprise','100349','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100350','24210150826969742','enterprise','100350','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100351','24210150826969742','enterprise','100351','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100352','24210150826969742','enterprise','100352','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100353','24210150826969742','enterprise','100353','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100354','24210150826969742','enterprise','100354','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100355','24210150826969742','enterprise','100355','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100356','24210150826969742','enterprise','100356','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100357','24210150826969742','enterprise','100357','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100358','24210150826969742','enterprise','100358','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100359','24210150826969742','enterprise','100359','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100360','24210150826969742','enterprise','100360','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100361','24210150826969742','enterprise','100361','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100362','24210150826969742','enterprise','100362','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100363','24210150826969742','enterprise','100363','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100364','24210150826969742','enterprise','100364','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100365','24210150826969742','enterprise','100365','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100366','24210150826969742','enterprise','100366','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100367','24210150826969742','enterprise','100367','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100368','24210150826969742','enterprise','100368','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100369','24210150826969742','enterprise','100369','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100370','24210150826969742','enterprise','100370','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100371','24210150826969742','enterprise','100371','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100372','24210150826969742','enterprise','100372','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100373','24210150826969742','enterprise','100373','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100374','24210150826969742','enterprise','100374','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100375','24210150826969742','enterprise','100375','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100376','24210150826969742','enterprise','100376','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100377','24210150826969742','enterprise','100377','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100378','24210150826969742','enterprise','100378','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100379','24210150826969742','enterprise','100379','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100380','24210150826969742','enterprise','100380','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100381','24210150826969742','enterprise','100381','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100382','24210150826969742','enterprise','100382','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100383','24210150826969742','enterprise','100383','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100384','24210150826969742','enterprise','100384','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100385','24210150826969742','enterprise','100385','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100386','24210150826969742','enterprise','100386','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100387','24210150826969742','enterprise','100387','2');
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`) VALUES('100388','24210150826969742','enterprise','100388','2');


INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100000',UUID(),'0','2','EhGroups','100000','深圳市宇轩网络技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100001',UUID(),'0','2','EhGroups','100001','深圳市捷时行科技服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100002',UUID(),'0','2','EhGroups','100002','深圳市沿海世纪地产有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100003',UUID(),'0','2','EhGroups','100003','深圳市爱倍多科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100004',UUID(),'0','2','EhGroups','100004','浙江宇视科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100005',UUID(),'0','2','EhGroups','100005','网宿科技股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100006',UUID(),'0','2','EhGroups','100006','深圳福江科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100007',UUID(),'0','2','EhGroups','100007','深圳市睿智专利事务所','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100008',UUID(),'0','2','EhGroups','100008','无锡盈达聚力科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100009',UUID(),'0','2','EhGroups','100009','深圳市海润鑫文化传播有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100010',UUID(),'0','2','EhGroups','100010','中联认证中心广东分中心','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100011',UUID(),'0','2','EhGroups','100011','深圳大德飞天科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100012',UUID(),'0','2','EhGroups','100012','深圳市文字传媒有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100013',UUID(),'0','2','EhGroups','100013','无锡盈达聚力科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100014',UUID(),'0','2','EhGroups','100014','费斯托(中国)有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100015',UUID(),'0','2','EhGroups','100015','中正国际认证（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100016',UUID(),'0','2','EhGroups','100016','深圳市龙晟光电有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100017',UUID(),'0','2','EhGroups','100017','王才梅','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100018',UUID(),'0','2','EhGroups','100018','深圳市清源铸造材料有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100019',UUID(),'0','2','EhGroups','100019','费斯托(中国)有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100020',UUID(),'0','2','EhGroups','100020','郭皓','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100021',UUID(),'0','2','EhGroups','100021','深圳市启智有声科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100022',UUID(),'0','2','EhGroups','100022','苏州工业园区艾思科技公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100023',UUID(),'0','2','EhGroups','100023','深圳今日文化发展有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100024',UUID(),'0','2','EhGroups','100024','深圳市车音网科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100025',UUID(),'0','2','EhGroups','100025','深圳今日文化发展有限公司办公用品中心','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100026',UUID(),'0','2','EhGroups','100026','深圳市炫彩科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100027',UUID(),'0','2','EhGroups','100027','深圳市爱倍多科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100028',UUID(),'0','2','EhGroups','100028','深圳市爱倍多科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100029',UUID(),'0','2','EhGroups','100029','深圳财富支付有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100030',UUID(),'0','2','EhGroups','100030','深圳桑蒲通信设备有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100031',UUID(),'0','2','EhGroups','100031','深圳市精玻仪器有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100032',UUID(),'0','2','EhGroups','100032','新盛合绿科技（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100033',UUID(),'0','2','EhGroups','100033','深圳联智科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100034',UUID(),'0','2','EhGroups','100034','深圳易莱特科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100035',UUID(),'0','2','EhGroups','100035','深圳市华美视科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100036',UUID(),'0','2','EhGroups','100036','深圳市拓展信息咨询有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100037',UUID(),'0','2','EhGroups','100037','深圳市创飞领域科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100038',UUID(),'0','2','EhGroups','100038','深圳市创飞领域科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100039',UUID(),'0','2','EhGroups','100039','深圳市创飞领域科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100040',UUID(),'0','2','EhGroups','100040','深圳市视显光电技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100041',UUID(),'0','2','EhGroups','100041','深圳市视显光电技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100042',UUID(),'0','2','EhGroups','100042','深圳市碧水蓝天科技发展有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100043',UUID(),'0','2','EhGroups','100043','上海芯旺微电子技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100044',UUID(),'0','2','EhGroups','100044','深圳市诺恒管理策划有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100045',UUID(),'0','2','EhGroups','100045','深圳市万通食品有限责任公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100046',UUID(),'0','2','EhGroups','100046','上海芯旺电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100047',UUID(),'0','2','EhGroups','100047','深圳财富支付有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100048',UUID(),'0','2','EhGroups','100048','深圳市天智未来科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100049',UUID(),'0','2','EhGroups','100049','聚辰半导体（上海）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100050',UUID(),'0','2','EhGroups','100050','聚辰半导体（上海）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100051',UUID(),'0','2','EhGroups','100051','华瑞昇电子(深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100052',UUID(),'0','2','EhGroups','100052','网宿科技股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100053',UUID(),'0','2','EhGroups','100053','福瑞博德软件开发深圳有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100054',UUID(),'0','2','EhGroups','100054','福瑞博德软件开发深圳有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100055',UUID(),'0','2','EhGroups','100055','深圳市银证通云网科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100056',UUID(),'0','2','EhGroups','100056','深圳市银证通云网科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100057',UUID(),'0','2','EhGroups','100057','湖州明芯微电子设计有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100058',UUID(),'0','2','EhGroups','100058','珠海奔图电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100059',UUID(),'0','2','EhGroups','100059','深圳市巨龙城投资有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100060',UUID(),'0','2','EhGroups','100060','深圳市富泓电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100061',UUID(),'0','2','EhGroups','100061','深圳市安睿立电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100062',UUID(),'0','2','EhGroups','100062','浙江宇视科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100063',UUID(),'0','2','EhGroups','100063','广东乙纬电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100064',UUID(),'0','2','EhGroups','100064','通联支付网络服务股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100065',UUID(),'0','2','EhGroups','100065','中国建设银行股份有限公司深圳市分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100066',UUID(),'0','2','EhGroups','100066','中国建设银行股份有限公司深圳市分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100067',UUID(),'0','2','EhGroups','100067','中国建设银行股份有限公司深圳市分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100068',UUID(),'0','2','EhGroups','100068','中国建设银行股份有限公司深圳市分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100069',UUID(),'0','2','EhGroups','100069','安富利物流(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100070',UUID(),'0','2','EhGroups','100070','神州数码系统集成服务有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100071',UUID(),'0','2','EhGroups','100071','深圳市汇杰投资有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100072',UUID(),'0','2','EhGroups','100072','深圳市深商创投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100073',UUID(),'0','2','EhGroups','100073','阿尔科斯科技（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100074',UUID(),'0','2','EhGroups','100074','深圳容德投资有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100075',UUID(),'0','2','EhGroups','100075','深圳市晶科迪电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100076',UUID(),'0','2','EhGroups','100076','深圳市易联科软件研发有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100077',UUID(),'0','2','EhGroups','100077','广州中慧电子有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100078',UUID(),'0','2','EhGroups','100078','广州中慧电子有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100079',UUID(),'0','2','EhGroups','100079','广州中慧电子有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100080',UUID(),'0','2','EhGroups','100080','广州中慧电子有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100081',UUID(),'0','2','EhGroups','100081','广州中慧电子有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100082',UUID(),'0','2','EhGroups','100082','广州中慧电子有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100083',UUID(),'0','2','EhGroups','100083','爱康科商贸（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100084',UUID(),'0','2','EhGroups','100084','新趣品商贸(深圳)有限公司(李继邦)','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100085',UUID(),'0','2','EhGroups','100085','安信证券股份有限公司深圳科发路证券营业部','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100086',UUID(),'0','2','EhGroups','100086','安信证券股份有限公司深圳科发路证券营业部','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100087',UUID(),'0','2','EhGroups','100087','深圳市高科金信净化科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100088',UUID(),'0','2','EhGroups','100088','深圳市康迈科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100089',UUID(),'0','2','EhGroups','100089','深圳市四季轩餐饮有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100090',UUID(),'0','2','EhGroups','100090','深圳市融泰衡实业有公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100091',UUID(),'0','2','EhGroups','100091','深圳多多益善电子商务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100092',UUID(),'0','2','EhGroups','100092','深圳市小牛普惠投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100093',UUID(),'0','2','EhGroups','100093','君丰创业投资基金管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100094',UUID(),'0','2','EhGroups','100094','深圳证券通信有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100095',UUID(),'0','2','EhGroups','100095','深圳证券通信有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100096',UUID(),'0','2','EhGroups','100096','欣旺达电子股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100097',UUID(),'0','2','EhGroups','100097','深圳市瓷爱谷文化有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100098',UUID(),'0','2','EhGroups','100098','神州数码信息服务股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100099',UUID(),'0','2','EhGroups','100099','北京三浦教育投资有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100100',UUID(),'0','2','EhGroups','100100','深圳市独尊科技开发有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100101',UUID(),'0','2','EhGroups','100101','深圳市信息大成网络有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100102',UUID(),'0','2','EhGroups','100102','深圳通联金融网络科技服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100103',UUID(),'0','2','EhGroups','100103','深圳通联金融网络科技服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100104',UUID(),'0','2','EhGroups','100104','通联支付网络服务股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100105',UUID(),'0','2','EhGroups','100105','深圳市软晶科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100106',UUID(),'0','2','EhGroups','100106','深圳市江波龙电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100107',UUID(),'0','2','EhGroups','100107','深圳市江波龙电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100108',UUID(),'0','2','EhGroups','100108','深圳市江波龙电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100109',UUID(),'0','2','EhGroups','100109','深圳市江波龙电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100110',UUID(),'0','2','EhGroups','100110','深圳市江波龙电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100111',UUID(),'0','2','EhGroups','100111','深圳市江波龙电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100112',UUID(),'0','2','EhGroups','100112','深圳市江波龙电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100113',UUID(),'0','2','EhGroups','100113','深圳市江波龙电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100114',UUID(),'0','2','EhGroups','100114','深圳市江波龙商用设备有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100115',UUID(),'0','2','EhGroups','100115','深圳市志鼎科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100116',UUID(),'0','2','EhGroups','100116','北京银联金卡科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100117',UUID(),'0','2','EhGroups','100117','深圳神州数码信息技术服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100118',UUID(),'0','2','EhGroups','100118','神州数码(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100119',UUID(),'0','2','EhGroups','100119','神州数码(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100120',UUID(),'0','2','EhGroups','100120','上海神州数码有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100121',UUID(),'0','2','EhGroups','100121','深圳市中天普创投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100122',UUID(),'0','2','EhGroups','100122','深圳市安卓信创业投资有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100123',UUID(),'0','2','EhGroups','100123','太平人寿保险有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100124',UUID(),'0','2','EhGroups','100124','和阳（深圳）投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100125',UUID(),'0','2','EhGroups','100125','深圳市昆鹏股权投资基金管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100126',UUID(),'0','2','EhGroups','100126','深圳市盘龙环境技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100127',UUID(),'0','2','EhGroups','100127','深圳壹昊金融控股股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100128',UUID(),'0','2','EhGroups','100128','深圳市贷帮投资担保有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100129',UUID(),'0','2','EhGroups','100129','深圳市人和投资集团有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100130',UUID(),'0','2','EhGroups','100130','川奇光电科技（扬州）有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100131',UUID(),'0','2','EhGroups','100131','川元电子（扬州）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100132',UUID(),'0','2','EhGroups','100132','深圳市志鼎科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100133',UUID(),'0','2','EhGroups','100133','深圳市惠邦知识产权代理事务所','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100134',UUID(),'0','2','EhGroups','100134','通联支付网络服务股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100135',UUID(),'0','2','EhGroups','100135','深圳嘉德瑞碳资产股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100136',UUID(),'0','2','EhGroups','100136','深圳前海南方增长资产管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100137',UUID(),'0','2','EhGroups','100137','安富利物流(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100138',UUID(),'0','2','EhGroups','100138','安富利物流(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100139',UUID(),'0','2','EhGroups','100139','安富利物流(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100140',UUID(),'0','2','EhGroups','100140','安富利物流(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100141',UUID(),'0','2','EhGroups','100141','安富利物流(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100142',UUID(),'0','2','EhGroups','100142','安富利物流(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100143',UUID(),'0','2','EhGroups','100143','萃冠电子贸易（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100144',UUID(),'0','2','EhGroups','100144','萃冠电子贸易（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100145',UUID(),'0','2','EhGroups','100145','萃冠电子贸易（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100146',UUID(),'0','2','EhGroups','100146','萃冠电子贸易（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100147',UUID(),'0','2','EhGroups','100147','萃冠电子贸易（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100148',UUID(),'0','2','EhGroups','100148','萃冠电子贸易（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100149',UUID(),'0','2','EhGroups','100149','安富利物流(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100150',UUID(),'0','2','EhGroups','100150','安霸半导体技术(上海)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100151',UUID(),'0','2','EhGroups','100151','安霸半导体技术(上海)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100152',UUID(),'0','2','EhGroups','100152','深圳中泽明芯集团有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100153',UUID(),'0','2','EhGroups','100153','深圳市漫步者科技股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100154',UUID(),'0','2','EhGroups','100154','是德科技（中国）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100155',UUID(),'0','2','EhGroups','100155','是德科技（中国）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100156',UUID(),'0','2','EhGroups','100156','博尔科通讯系统（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100157',UUID(),'0','2','EhGroups','100157','招商银行股份有限公司深圳车公庙支行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100158',UUID(),'0','2','EhGroups','100158','招商银行股份有限公司深圳车公庙支行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100159',UUID(),'0','2','EhGroups','100159','深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100160',UUID(),'0','2','EhGroups','100160','深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100161',UUID(),'0','2','EhGroups','100161','深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100162',UUID(),'0','2','EhGroups','100162','深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100163',UUID(),'0','2','EhGroups','100163','深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100164',UUID(),'0','2','EhGroups','100164','深圳市润利丰实业发展有限公司（老绍兴酒楼）','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100165',UUID(),'0','2','EhGroups','100165','深圳市广源餐饮管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100166',UUID(),'0','2','EhGroups','100166','深圳市广源餐饮管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100167',UUID(),'0','2','EhGroups','100167','上海联广认证有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100168',UUID(),'0','2','EhGroups','100168','上海联广认证有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100169',UUID(),'0','2','EhGroups','100169','广东万诺律师事务所(李亚光)','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100170',UUID(),'0','2','EhGroups','100170','杭州华三通信技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100171',UUID(),'0','2','EhGroups','100171','杭州华三通信技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100172',UUID(),'0','2','EhGroups','100172','深圳市恒源昊资产管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100173',UUID(),'0','2','EhGroups','100173','深圳市恒源昊资产管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100174',UUID(),'0','2','EhGroups','100174','深圳前海星润股权投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100175',UUID(),'0','2','EhGroups','100175','深圳前海星润股权投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100176',UUID(),'0','2','EhGroups','100176','深圳前海星润股权投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100177',UUID(),'0','2','EhGroups','100177','深圳前海星润股权投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100178',UUID(),'0','2','EhGroups','100178','深圳前海星润股权投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100179',UUID(),'0','2','EhGroups','100179','深圳前海星润股权投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100180',UUID(),'0','2','EhGroups','100180','塔塔信息技术(中国)股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100181',UUID(),'0','2','EhGroups','100181','塔塔信息技术(中国)股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100182',UUID(),'0','2','EhGroups','100182','深圳格兰泰克科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100183',UUID(),'0','2','EhGroups','100183','深圳格兰泰克科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100184',UUID(),'0','2','EhGroups','100184','深圳市民声科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100185',UUID(),'0','2','EhGroups','100185','深圳市民声科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100186',UUID(),'0','2','EhGroups','100186','深圳市民声科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100187',UUID(),'0','2','EhGroups','100187','深圳市民声科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100188',UUID(),'0','2','EhGroups','100188','深圳证券通信有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100189',UUID(),'0','2','EhGroups','100189','深圳证券通信有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100190',UUID(),'0','2','EhGroups','100190','深圳五维微品金融信息服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100191',UUID(),'0','2','EhGroups','100191','深圳五维微品金融信息服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100192',UUID(),'0','2','EhGroups','100192','深圳新为软件股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100193',UUID(),'0','2','EhGroups','100193','深圳新为软件股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100194',UUID(),'0','2','EhGroups','100194','深圳市瑞格尔健康管理科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100195',UUID(),'0','2','EhGroups','100195','深圳市瑞格尔健康管理科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100196',UUID(),'0','2','EhGroups','100196','深圳市瑞格尔健康管理科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100197',UUID(),'0','2','EhGroups','100197','深圳市瑞格尔健康管理科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100198',UUID(),'0','2','EhGroups','100198','深圳市瑞格尔健康管理科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100199',UUID(),'0','2','EhGroups','100199','深圳市瑞格尔健康管理科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100200',UUID(),'0','2','EhGroups','100200','深圳市瑞格尔健康管理科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100201',UUID(),'0','2','EhGroups','100201','深圳市瑞格尔健康管理科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100202',UUID(),'0','2','EhGroups','100202','深圳格兰泰克汽车电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100203',UUID(),'0','2','EhGroups','100203','华美优科网络技术（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100204',UUID(),'0','2','EhGroups','100204','华美优科网络技术（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100205',UUID(),'0','2','EhGroups','100205','华美优科网络技术（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100206',UUID(),'0','2','EhGroups','100206','华美优科网络技术（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100207',UUID(),'0','2','EhGroups','100207','华美优科网络技术（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100208',UUID(),'0','2','EhGroups','100208','华美优科网络技术（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100209',UUID(),'0','2','EhGroups','100209','华美优科网络技术（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100210',UUID(),'0','2','EhGroups','100210','安霸半导体技术(上海)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100211',UUID(),'0','2','EhGroups','100211','安霸半导体技术(上海)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100212',UUID(),'0','2','EhGroups','100212','吴炎雄(深圳市南山区南风舍餐馆)','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100213',UUID(),'0','2','EhGroups','100213','深圳集群壹家股权投资基金管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100214',UUID(),'0','2','EhGroups','100214','深圳集群壹家股权投资基金管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100215',UUID(),'0','2','EhGroups','100215','深圳华智融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100216',UUID(),'0','2','EhGroups','100216','深圳华智融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100217',UUID(),'0','2','EhGroups','100217','深圳华智融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100218',UUID(),'0','2','EhGroups','100218','深圳华智融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100219',UUID(),'0','2','EhGroups','100219','深圳华智融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100220',UUID(),'0','2','EhGroups','100220','深圳华智融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100221',UUID(),'0','2','EhGroups','100221','深圳市智美达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100222',UUID(),'0','2','EhGroups','100222','深圳市智美达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100223',UUID(),'0','2','EhGroups','100223','深圳市智美达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100224',UUID(),'0','2','EhGroups','100224','深圳市智美达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100225',UUID(),'0','2','EhGroups','100225','深圳市智美达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100226',UUID(),'0','2','EhGroups','100226','深圳市智美达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100227',UUID(),'0','2','EhGroups','100227','深圳市智美达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100228',UUID(),'0','2','EhGroups','100228','深圳市智美达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100229',UUID(),'0','2','EhGroups','100229','东吴证券股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100230',UUID(),'0','2','EhGroups','100230','东吴证券股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100231',UUID(),'0','2','EhGroups','100231','湖南南方稀贵金属交易所股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100232',UUID(),'0','2','EhGroups','100232','湖南南方稀贵金属交易所股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100233',UUID(),'0','2','EhGroups','100233','湖南南方稀贵金属交易所股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100234',UUID(),'0','2','EhGroups','100234','湖南南方稀贵金属交易所股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100235',UUID(),'0','2','EhGroups','100235','深圳市佳信捷技术股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100236',UUID(),'0','2','EhGroups','100236','深圳市佳信捷技术股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100237',UUID(),'0','2','EhGroups','100237','深圳市佳信捷技术股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100238',UUID(),'0','2','EhGroups','100238','深圳市佳信捷技术股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100239',UUID(),'0','2','EhGroups','100239','深圳市中兴小额贷款有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100240',UUID(),'0','2','EhGroups','100240','深圳市中兴小额贷款有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100241',UUID(),'0','2','EhGroups','100241','深圳市中兴小额贷款有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100242',UUID(),'0','2','EhGroups','100242','深圳市研信科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100243',UUID(),'0','2','EhGroups','100243','深圳中兴飞贷金融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100244',UUID(),'0','2','EhGroups','100244','深圳中兴飞贷金融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100245',UUID(),'0','2','EhGroups','100245','深圳中兴飞贷金融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100246',UUID(),'0','2','EhGroups','100246','深圳中兴飞贷金融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100247',UUID(),'0','2','EhGroups','100247','深圳中兴飞贷金融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100248',UUID(),'0','2','EhGroups','100248','深圳中兴飞贷金融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100249',UUID(),'0','2','EhGroups','100249','深圳中兴飞贷金融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100250',UUID(),'0','2','EhGroups','100250','深圳中兴飞贷金融科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100251',UUID(),'0','2','EhGroups','100251','深圳市芯智科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100252',UUID(),'0','2','EhGroups','100252','深圳市芯智科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100253',UUID(),'0','2','EhGroups','100253','深圳市芯智科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100254',UUID(),'0','2','EhGroups','100254','深圳市芯智科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100255',UUID(),'0','2','EhGroups','100255','珠海华润银行股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100256',UUID(),'0','2','EhGroups','100256','珠海华润银行股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100257',UUID(),'0','2','EhGroups','100257','珠海华润银行股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100258',UUID(),'0','2','EhGroups','100258','珠海华润银行股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100259',UUID(),'0','2','EhGroups','100259','珠海华润银行股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100260',UUID(),'0','2','EhGroups','100260','珠海华润银行股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100261',UUID(),'0','2','EhGroups','100261','珠海华润银行股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100262',UUID(),'0','2','EhGroups','100262','珠海华润银行股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100263',UUID(),'0','2','EhGroups','100263','中国银行股份有限公司深圳市分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100264',UUID(),'0','2','EhGroups','100264','中国银行股份有限公司深圳市分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100265',UUID(),'0','2','EhGroups','100265','中国银行股份有限公司深圳市分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100266',UUID(),'0','2','EhGroups','100266','众华会计师事务所(特殊普通合伙)深圳分所','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100267',UUID(),'0','2','EhGroups','100267','众华会计师事务所(特殊普通合伙)深圳分所','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100268',UUID(),'0','2','EhGroups','100268','同方(深圳)云计算技术股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100269',UUID(),'0','2','EhGroups','100269','同方(深圳)云计算技术股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100270',UUID(),'0','2','EhGroups','100270','杭州银行股份有限公司深圳分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100271',UUID(),'0','2','EhGroups','100271','杭州银行股份有限公司深圳分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100272',UUID(),'0','2','EhGroups','100272','杭州银行股份有限公司深圳分行','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100273',UUID(),'0','2','EhGroups','100273','泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100274',UUID(),'0','2','EhGroups','100274','泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100275',UUID(),'0','2','EhGroups','100275','泰诺风保泰（苏州）隔热材料有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100276',UUID(),'0','2','EhGroups','100276','深圳太东资本管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100277',UUID(),'0','2','EhGroups','100277','深圳太东资本管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100278',UUID(),'0','2','EhGroups','100278','中国人民健康保险公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100279',UUID(),'0','2','EhGroups','100279','中国人民健康保险公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100280',UUID(),'0','2','EhGroups','100280','深圳排放权交易所有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100281',UUID(),'0','2','EhGroups','100281','深圳排放权交易所有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100282',UUID(),'0','2','EhGroups','100282','深圳排放权交易所有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100283',UUID(),'0','2','EhGroups','100283','深圳排放权交易所有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100284',UUID(),'0','2','EhGroups','100284','深圳市慧峰高科产业园投资发展有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100285',UUID(),'0','2','EhGroups','100285','深圳市慧峰高科产业园投资发展有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100286',UUID(),'0','2','EhGroups','100286','丰益(上海)信息技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100287',UUID(),'0','2','EhGroups','100287','丰益(上海)信息技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100288',UUID(),'0','2','EhGroups','100288','深圳市鼎恒瑞投资有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100289',UUID(),'0','2','EhGroups','100289','惠州硕贝德无线科技股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100290',UUID(),'0','2','EhGroups','100290','惠州硕贝德无线科技股份有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100291',UUID(),'0','2','EhGroups','100291','深圳市前海德融资本管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100292',UUID(),'0','2','EhGroups','100292','深圳市前海德融资本管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100293',UUID(),'0','2','EhGroups','100293','深圳市前海德融资本管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100294',UUID(),'0','2','EhGroups','100294','深圳市前海德融资本管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100295',UUID(),'0','2','EhGroups','100295','深圳市新天域文化产业有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100296',UUID(),'0','2','EhGroups','100296','深圳市新天域文化产业有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100297',UUID(),'0','2','EhGroups','100297','深圳前海厚诚敏投资控股有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100298',UUID(),'0','2','EhGroups','100298','深圳前海厚诚敏投资控股有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100299',UUID(),'0','2','EhGroups','100299','深圳前海厚诚敏投资控股有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100300',UUID(),'0','2','EhGroups','100300','深圳前海厚诚敏投资控股有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100301',UUID(),'0','2','EhGroups','100301','深圳市多元世纪信息技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100302',UUID(),'0','2','EhGroups','100302','深圳市多元世纪信息技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100303',UUID(),'0','2','EhGroups','100303','深圳市多元世纪信息技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100304',UUID(),'0','2','EhGroups','100304','深圳市多元世纪信息技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100305',UUID(),'0','2','EhGroups','100305','深圳市工夫百味投资有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100306',UUID(),'0','2','EhGroups','100306','深圳市工夫百味投资有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100307',UUID(),'0','2','EhGroups','100307','北京集创北方科技有限公司深圳办事处','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100308',UUID(),'0','2','EhGroups','100308','北京集创北方科技有限公司深圳办事处','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100309',UUID(),'0','2','EhGroups','100309','北京集创北方科技有限公司深圳办事处','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100310',UUID(),'0','2','EhGroups','100310','北京集创北方科技有限公司深圳办事处','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100311',UUID(),'0','2','EhGroups','100311','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100312',UUID(),'0','2','EhGroups','100312','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100313',UUID(),'0','2','EhGroups','100313','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100314',UUID(),'0','2','EhGroups','100314','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100315',UUID(),'0','2','EhGroups','100315','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100316',UUID(),'0','2','EhGroups','100316','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100317',UUID(),'0','2','EhGroups','100317','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100318',UUID(),'0','2','EhGroups','100318','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100319',UUID(),'0','2','EhGroups','100319','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100320',UUID(),'0','2','EhGroups','100320','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100321',UUID(),'0','2','EhGroups','100321','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100322',UUID(),'0','2','EhGroups','100322','普联技术有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100323',UUID(),'0','2','EhGroups','100323','上海赫丝蒂化妆品有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100324',UUID(),'0','2','EhGroups','100324','上海赫丝蒂化妆品有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100325',UUID(),'0','2','EhGroups','100325','深圳市恒顺合鑫科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100326',UUID(),'0','2','EhGroups','100326','深圳市华讯万通科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100327',UUID(),'0','2','EhGroups','100327','中信证券股份有限公司深圳科技园科苑路证券营业部','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100328',UUID(),'0','2','EhGroups','100328','深圳市百益亚太电效工程有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100329',UUID(),'0','2','EhGroups','100329','移动财经软件（深圳）有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100330',UUID(),'0','2','EhGroups','100330','深圳金桥融付科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100331',UUID(),'0','2','EhGroups','100331','深圳金桥融付科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100332',UUID(),'0','2','EhGroups','100332','深圳市浅葱小唱音乐餐厅有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100333',UUID(),'0','2','EhGroups','100333','深圳市城道通环保科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100334',UUID(),'0','2','EhGroups','100334','深圳市城道通环保科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100335',UUID(),'0','2','EhGroups','100335','北京玖富时代投资顾问有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100336',UUID(),'0','2','EhGroups','100336','深圳易兰德金融服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100337',UUID(),'0','2','EhGroups','100337','深圳易兰德金融服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100338',UUID(),'0','2','EhGroups','100338','深圳易兰德金融服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100339',UUID(),'0','2','EhGroups','100339','深圳易兰德金融服务有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100340',UUID(),'0','2','EhGroups','100340','深圳铂睿智恒科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100341',UUID(),'0','2','EhGroups','100341','深圳铂睿智恒科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100342',UUID(),'0','2','EhGroups','100342','深圳铂睿智恒科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100343',UUID(),'0','2','EhGroups','100343','深圳铂睿智恒科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100344',UUID(),'0','2','EhGroups','100344','深圳市宽域智联科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100345',UUID(),'0','2','EhGroups','100345','深圳市宽域智联科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100346',UUID(),'0','2','EhGroups','100346','深圳前海方舟资本管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100347',UUID(),'0','2','EhGroups','100347','深圳前海方舟资本管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100348',UUID(),'0','2','EhGroups','100348','前海东方金德资产管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100349',UUID(),'0','2','EhGroups','100349','前海东方金德资产管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100350',UUID(),'0','2','EhGroups','100350','中民保险经纪股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100351',UUID(),'0','2','EhGroups','100351','中民保险经纪股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100352',UUID(),'0','2','EhGroups','100352','中民保险经纪股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100353',UUID(),'0','2','EhGroups','100353','中民电子商务股份有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100354',UUID(),'0','2','EhGroups','100354','深圳市前海野文投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100355',UUID(),'0','2','EhGroups','100355','深圳市前海野文投资管理有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100356',UUID(),'0','2','EhGroups','100356','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100357',UUID(),'0','2','EhGroups','100357','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100358',UUID(),'0','2','EhGroups','100358','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100359',UUID(),'0','2','EhGroups','100359','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100360',UUID(),'0','2','EhGroups','100360','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100361',UUID(),'0','2','EhGroups','100361','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100362',UUID(),'0','2','EhGroups','100362','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100363',UUID(),'0','2','EhGroups','100363','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100364',UUID(),'0','2','EhGroups','100364','英伟达半导体(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100365',UUID(),'0','2','EhGroups','100365','百度(中国)有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100366',UUID(),'0','2','EhGroups','100366','百度(中国)有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100367',UUID(),'0','2','EhGroups','100367','北京百度网讯科技有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100368',UUID(),'0','2','EhGroups','100368','北京百度网讯科技有限公司深圳分公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100369',UUID(),'0','2','EhGroups','100369','深圳市福尔科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100370',UUID(),'0','2','EhGroups','100370','深圳市德维莱科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100371',UUID(),'0','2','EhGroups','100371','深圳市宝富利科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100372',UUID(),'0','2','EhGroups','100372','深圳市大维纳米科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100373',UUID(),'0','2','EhGroups','100373','金宝通电子(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100374',UUID(),'0','2','EhGroups','100374','深圳市亮信科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100375',UUID(),'0','2','EhGroups','100375','汎达科技(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100376',UUID(),'0','2','EhGroups','100376','群锋电子(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100377',UUID(),'0','2','EhGroups','100377','诺华达电子(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100378',UUID(),'0','2','EhGroups','100378','新纬科技(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100379',UUID(),'0','2','EhGroups','100379','深圳市领平科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100380',UUID(),'0','2','EhGroups','100380','北京美餐巧达科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100381',UUID(),'0','2','EhGroups','100381','镭射谷科(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100382',UUID(),'0','2','EhGroups','100382','镭射谷科(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100383',UUID(),'0','2','EhGroups','100383','镭射谷科(深圳)有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100384',UUID(),'0','2','EhGroups','100384','深圳市安普盛科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100385',UUID(),'0','2','EhGroups','100385','深圳市安特讯科技有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100386',UUID(),'0','2','EhGroups','100386','深圳市开拓汽车电子有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100387',UUID(),'0','2','EhGroups','100387','深圳深港生产力基地有限公司','','0','0');
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`) VALUES('100388',UUID(),'0','2','EhGroups','100388','深圳市商业联合会','','0','0');





