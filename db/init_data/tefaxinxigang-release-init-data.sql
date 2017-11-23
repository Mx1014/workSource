-- tefaxinxigang by ljs
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1882,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999962','the relative path for tefaxinxigang app agreements',999962,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1883,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999962,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1884,'pmtask.handler-999962','flow','0',999962,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1885,'pay.platform','1','支付类型',999962,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (439,'Android_TeFaXinXiGang',NULL,UTC_TIMESTAMP(),999962);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (440,'iOS_TeFaXinXiGang',NULL,UTC_TIMESTAMP(),999962);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (768,439,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999962);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (769,440,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999962);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999962,'特发信息港');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1443,999962,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (871,'sms.default.sign',0,'zh_CN','特发信息港','【特发信息港】',999962);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18384,0,'广东','GUANGDONG','GD','/广东',1,1,NULL,NULL,2,0,999962);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18385,18384,'深圳市','SHENZHENSHI','SZS','/广东/深圳市',2,2,NULL,'0755',2,1,999962);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18386,18385,'南山区','NANSHANQU','NSQ','/广东/深圳市/南山区',3,3,NULL,'0755',2,0,999962);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060031,UUID(),18385,'深圳市',18386,'南山区','特发信息港','特发信息港','深圳市南山区高新区中区科丰路2号','属国有上市企业资源性资产，楼宇品质及物业服务信赖有保障。特发信息港位于南山科技园科丰路，紧邻北环大道，交通便利；对面为科苑公园，办公环境极佳，性价比居科技园之首，为企业科研办公之首选。',0,1,2,UTC_TIMESTAMP(),1,192631,192632,UTC_TIMESTAMP(),999962);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092804,240111044332060031,'',113.955982,22.555837,'ws1034hjqbfb');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20141,999962,'COMMUNITY',240111044332060031,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1039194,0,'PM','深圳市特发物业管理有限公司','深圳市特发物业管理有限公司是特发集团控股企业,成立于1993年，注册资本1538万元，第一批获得国家一级物业管理企业资质。在行业中较早推行ISO9001质量管理体系、ISO14001环境管理体系、OHSAS18001职业健康安全管理体系，并获评“深圳知名品牌”。2014年和2015年荣登 “中国物业服务百强企业”榜，2015年荣获“中国园区物业服务优秀企业”称号，2017年再次荣膺“中国物业服务百强企业”，排名39位。特发物业2015年底完成混合所有制改革，截止2016年底，是“十八大”后深圳市国资委系统混合所有制改革唯一成功试点单位。','/1039194',1,2,'ENTERPRISE',999962,1057689);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1154388,240111044332060031,'organization',1039194,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (24608,'EhOrganizations',1039194,1,10,456805,0,1,UTC_TIMESTAMP(),999962,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (24609,'EhOrganizations',1039194,1,10,456806,0,1,UTC_TIMESTAMP(),999962,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (24610,'EhOrganizations',1039194,1,10,456807,0,1,UTC_TIMESTAMP(),999962,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1057689,UUID(),'深圳市特发物业管理有限公司','深圳市特发物业管理有限公司',1,1,1039194,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192630,1,999962);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192630,UUID(),999962,2,'EhGroups',1057689,'深圳市特发物业管理有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192631,UUID(),999962,2,'',0,'特发信息港社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192632,UUID(),999962,2,'',0,'特发信息港意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (456805,UUID(),'19201983703','冯志波',NULL,1,45,'1','1','zh_CN','ee1481bc4cc9f90c7919b6875a8d90fc','497ec69c8dd2073e785e82adb90cab288c4c2fae6b83cb14215094f8f26381da',UTC_TIMESTAMP(),999962);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (456806,UUID(),'19201983704','王小华',NULL,1,45,'1','1','zh_CN','bf3b8e394debebee92a05f1f5a049a4a','3ba55320caec94901fe955dc3d3e9266d8e63b8743b27ec13c484cf00bc75920',UTC_TIMESTAMP(),999962);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (456807,UUID(),'19201983705','梁文娟',NULL,1,45,'1','2','zh_CN','ba873976500fe8bcc96800ff094e78aa','2e195de63750131761171efe75296033a2591e262201eb136405a676b430c73f',UTC_TIMESTAMP(),999962);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (28734,1039194,'USER',456805,'冯志波',0,'13825267170',UTC_TIMESTAMP(),999962);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (28735,1039194,'USER',456806,'王小华',0,'13590256976',UTC_TIMESTAMP(),999962);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (28736,1039194,'USER',456807,'梁文娟',0,'13808804536',UTC_TIMESTAMP(),999962);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (428346,456805,0,'13825267170',NULL,3,UTC_TIMESTAMP(),999962);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (428347,456806,0,'13590256976',NULL,3,UTC_TIMESTAMP(),999962);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (428348,456807,0,'13808804536',NULL,3,UTC_TIMESTAMP(),999962);

INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`) 
	VALUES (2178492,1039194,'USER',456805,'manager','冯志波',0,'13825267170',3,999962,'ENTERPRISE',0);
INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`) 
	VALUES (2178493,1039194,'USER',456806,'manager','王小华',0,'13590256976',3,999962,'ENTERPRISE',0);
INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`) 
	VALUES (2178494,1039194,'USER',456807,'manager','梁文娟',0,'13808804536',3,999962,'ENTERPRISE',0);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (28547,456805,1039194,'/1039194','ENTERPRISE',3,999962,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (28548,456806,1039194,'/1039194','ENTERPRISE',3,999962,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (28549,456807,1039194,'/1039194','ENTERPRISE',3,999962,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115043,'EhOrganizations',1039194,'EhUsers',456805,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115044,'EhOrganizations',1039194,'EhUsers',456806,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115045,'EhOrganizations',1039194,'EhUsers',456807,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970943,240111044332060031,'A栋','A栋',0,075526544662,'科丰路2号',NULL,'113.957455','22.556211','ws1034jr3nmp','属国有上市企业资源性资产，楼宇品质及物业服务信赖有保障。特发信息港大厦位于南山科技园科丰路，紧邻北环大道，交通便利；对面为科苑公园，办公环境极佳，性价比居科技园之首，为企业科研办公之首选。',NULL,2,0,NULL,1,NOW(),999962,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970944,240111044332060031,'B栋','B栋',0,075526544662,'科丰路2号',NULL,'113.955982','22.555837','ws1034hjqbfb','',NULL,2,0,NULL,1,NOW(),999962,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970945,240111044332060031,'C栋','C栋',0,075526544662,'科丰路2号',NULL,'113.955234','22.556364','ws103478m7j0','',NULL,2,0,NULL,1,NOW(),999962,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970946,240111044332060031,'D栋','D栋',0,075526719326,'科丰路2号',NULL,'113.956304','22.556379','ws1034k2qmfc','',NULL,2,0,NULL,1,NOW(),999962,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970947,240111044332060031,'E栋','E栋',0,075526719326,'科丰路2号',NULL,'113.957079','22.556509','ws1034m10qhc','',NULL,2,0,NULL,1,NOW(),999962,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970948,240111044332060031,'特发信息科技大厦','特发科技大厦',0,075526544662,'科丰路2号',NULL,'113.957079','22.556509','ws1034m10qhc','特发信息科技大厦与特发信息港大厦相连，位于南山科技园琼宇路，可俯瞰深圳湾，属北环大道标志性建筑。特发信息港大厦位于南山科技园琼宇路，紧邻北环大道，交通便利，是高科技企业办公之首选。',NULL,2,0,NULL,1,NOW(),999962,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462141,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋3、4层','特发信息科技大厦','F栋3、4层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462173,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2320','A栋','2320','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462205,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2508','A栋','2508','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462142,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋5层','特发信息科技大厦','F栋5层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462174,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2321','A栋','2321','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462206,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2509','A栋','2509','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462143,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋6层','特发信息科技大厦','F栋6层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462175,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2322','A栋','2322','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462207,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2510','A栋','2510','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462144,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋7层-8层','特发信息科技大厦','F栋7层-8层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462176,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2401','A栋','2401','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462208,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2511','A栋','2511','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462145,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋9层-11层','特发信息科技大厦','F栋9层-11层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462177,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2402','A栋','2402','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462209,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2512','A栋','2512','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462146,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋12层-15层','特发信息科技大厦','F栋12层-15层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462178,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2403','A栋','2403','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462210,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2513','A栋','2513','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462147,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋16层','特发信息科技大厦','F栋16层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462179,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2404','A栋','2404','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462211,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2514','A栋','2514','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462148,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋17层-20层','特发信息科技大厦','F栋17层-20层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462180,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2405','A栋','2405','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462149,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋21层-25层','特发信息科技大厦','F栋21层-25层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462181,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2406','A栋','2406','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462150,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-负一层东面停车场出入口旁房间','特发信息科技大厦','负一层东面停车场出入口旁房间','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462182,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2407','A栋','2407','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462151,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2220','A栋','2220','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462183,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2408','A栋','2408','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462152,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2221','A栋','2221','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462184,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2409','A栋','2409','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462153,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2222','A栋','2222','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462185,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2410','A栋','2410','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462154,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2301','A栋','2301','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462186,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2411','A栋','2411','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462155,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2302','A栋','2302','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462187,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2412','A栋','2412','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462156,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2303','A栋','2303','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462188,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2413','A栋','2413','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462157,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2304','A栋','2304','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462189,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2414','A栋','2414','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462158,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2305','A栋','2305','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462190,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2415','A栋','2415','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462159,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2306','A栋','2306','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462191,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2416','A栋','2416','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462160,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2307','A栋','2307','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462192,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2417','A栋','2417','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462161,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2308','A栋','2308','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462193,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2418','A栋','2418','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462162,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2309','A栋','2309','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462194,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2419','A栋','2419','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462163,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2310','A栋','2310','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462195,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2420','A栋','2420','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462164,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2311','A栋','2311','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462196,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2421','A栋','2421','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462165,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2312','A栋','2312','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462197,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2422','A栋','2422','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462166,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2313','A栋','2313','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462198,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2501','A栋','2501','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462167,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2314','A栋','2314','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462199,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2502','A栋','2502','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462136,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','E栋-E栋七楼东侧二号','E栋','E栋七楼东侧二号','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462168,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2315','A栋','2315','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462200,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2503','A栋','2503','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462137,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','E栋-E栋七楼东侧3号','E栋','E栋七楼东侧3号','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462169,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2316','A栋','2316','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462201,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2504','A栋','2504','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462138,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','E栋-E栋八楼','E栋','E栋八楼','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462170,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2317','A栋','2317','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462202,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2505','A栋','2505','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462139,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋1层—2层','特发信息科技大厦','F栋1层—2层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462171,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2318','A栋','2318','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462203,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2506','A栋','2506','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462140,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','特发信息科技大厦-F栋2层','特发信息科技大厦','F栋2层','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462172,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2319','A栋','2319','2','0',UTC_TIMESTAMP(),999962,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462204,UUID(),240111044332060031,18385,'深圳市',18386,'南山区','A栋-2507','A栋','2507','2','0',UTC_TIMESTAMP(),999962,NULL);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85215,1039194,240111044332060031,239825274387462140,'特发信息科技大厦-F栋2层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85247,1039194,240111044332060031,239825274387462172,'A栋-2319',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85279,1039194,240111044332060031,239825274387462204,'A栋-2507',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85216,1039194,240111044332060031,239825274387462141,'特发信息科技大厦-F栋3、4层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85248,1039194,240111044332060031,239825274387462173,'A栋-2320',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85280,1039194,240111044332060031,239825274387462205,'A栋-2508',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85217,1039194,240111044332060031,239825274387462142,'特发信息科技大厦-F栋5层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85249,1039194,240111044332060031,239825274387462174,'A栋-2321',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85281,1039194,240111044332060031,239825274387462206,'A栋-2509',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85218,1039194,240111044332060031,239825274387462143,'特发信息科技大厦-F栋6层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85250,1039194,240111044332060031,239825274387462175,'A栋-2322',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85282,1039194,240111044332060031,239825274387462207,'A栋-2510',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85219,1039194,240111044332060031,239825274387462144,'特发信息科技大厦-F栋7层-8层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85251,1039194,240111044332060031,239825274387462176,'A栋-2401',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85283,1039194,240111044332060031,239825274387462208,'A栋-2511',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85220,1039194,240111044332060031,239825274387462145,'特发信息科技大厦-F栋9层-11层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85252,1039194,240111044332060031,239825274387462177,'A栋-2402',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85284,1039194,240111044332060031,239825274387462209,'A栋-2512',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85221,1039194,240111044332060031,239825274387462146,'特发信息科技大厦-F栋12层-15层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85253,1039194,240111044332060031,239825274387462178,'A栋-2403',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85285,1039194,240111044332060031,239825274387462210,'A栋-2513',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85222,1039194,240111044332060031,239825274387462147,'特发信息科技大厦-F栋16层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85254,1039194,240111044332060031,239825274387462179,'A栋-2404',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85286,1039194,240111044332060031,239825274387462211,'A栋-2514',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85223,1039194,240111044332060031,239825274387462148,'特发信息科技大厦-F栋17层-20层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85255,1039194,240111044332060031,239825274387462180,'A栋-2405',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85224,1039194,240111044332060031,239825274387462149,'特发信息科技大厦-F栋21层-25层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85256,1039194,240111044332060031,239825274387462181,'A栋-2406',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85225,1039194,240111044332060031,239825274387462150,'特发信息科技大厦-负一层东面停车场出入口旁房间',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85257,1039194,240111044332060031,239825274387462182,'A栋-2407',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85226,1039194,240111044332060031,239825274387462151,'A栋-2220',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85258,1039194,240111044332060031,239825274387462183,'A栋-2408',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85227,1039194,240111044332060031,239825274387462152,'A栋-2221',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85259,1039194,240111044332060031,239825274387462184,'A栋-2409',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85228,1039194,240111044332060031,239825274387462153,'A栋-2222',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85260,1039194,240111044332060031,239825274387462185,'A栋-2410',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85229,1039194,240111044332060031,239825274387462154,'A栋-2301',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85261,1039194,240111044332060031,239825274387462186,'A栋-2411',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85230,1039194,240111044332060031,239825274387462155,'A栋-2302',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85262,1039194,240111044332060031,239825274387462187,'A栋-2412',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85231,1039194,240111044332060031,239825274387462156,'A栋-2303',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85263,1039194,240111044332060031,239825274387462188,'A栋-2413',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85232,1039194,240111044332060031,239825274387462157,'A栋-2304',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85264,1039194,240111044332060031,239825274387462189,'A栋-2414',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85233,1039194,240111044332060031,239825274387462158,'A栋-2305',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85265,1039194,240111044332060031,239825274387462190,'A栋-2415',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85234,1039194,240111044332060031,239825274387462159,'A栋-2306',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85266,1039194,240111044332060031,239825274387462191,'A栋-2416',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85235,1039194,240111044332060031,239825274387462160,'A栋-2307',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85267,1039194,240111044332060031,239825274387462192,'A栋-2417',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85236,1039194,240111044332060031,239825274387462161,'A栋-2308',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85268,1039194,240111044332060031,239825274387462193,'A栋-2418',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85237,1039194,240111044332060031,239825274387462162,'A栋-2309',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85269,1039194,240111044332060031,239825274387462194,'A栋-2419',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85238,1039194,240111044332060031,239825274387462163,'A栋-2310',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85270,1039194,240111044332060031,239825274387462195,'A栋-2420',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85239,1039194,240111044332060031,239825274387462164,'A栋-2311',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85271,1039194,240111044332060031,239825274387462196,'A栋-2421',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85240,1039194,240111044332060031,239825274387462165,'A栋-2312',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85272,1039194,240111044332060031,239825274387462197,'A栋-2422',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85241,1039194,240111044332060031,239825274387462166,'A栋-2313',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85273,1039194,240111044332060031,239825274387462198,'A栋-2501',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85242,1039194,240111044332060031,239825274387462167,'A栋-2314',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85274,1039194,240111044332060031,239825274387462199,'A栋-2502',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85211,1039194,240111044332060031,239825274387462136,'E栋-E栋七楼东侧二号',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85243,1039194,240111044332060031,239825274387462168,'A栋-2315',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85275,1039194,240111044332060031,239825274387462200,'A栋-2503',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85212,1039194,240111044332060031,239825274387462137,'E栋-E栋七楼东侧3号',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85244,1039194,240111044332060031,239825274387462169,'A栋-2316',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85276,1039194,240111044332060031,239825274387462201,'A栋-2504',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85213,1039194,240111044332060031,239825274387462138,'E栋-E栋八楼',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85245,1039194,240111044332060031,239825274387462170,'A栋-2317',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85277,1039194,240111044332060031,239825274387462202,'A栋-2505',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85214,1039194,240111044332060031,239825274387462139,'特发信息科技大厦-F栋1层—2层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85246,1039194,240111044332060031,239825274387462171,'A栋-2318',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85278,1039194,240111044332060031,239825274387462203,'A栋-2506',0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117600,10000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117601,10100,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117602,10400,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117603,10200,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117604,10600,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117605,10750,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117606,10751,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117607,10752,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117608,10850,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117609,10851,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117610,10852,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117611,20000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117612,20100,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117613,20110,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117614,20120,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117615,20130,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117616,20140,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117617,20150,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117618,20155,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117619,20160,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117620,20158,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117621,20170,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117622,20180,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117623,20190,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117624,20191,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117625,20192,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117626,20400,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117627,20410,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117628,20420,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117629,20430,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117630,20800,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117631,20810,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117632,20811,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117633,20812,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117634,20821,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117635,20822,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117636,20830,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117637,20831,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117638,20840,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117639,20841,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117640,20600,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117641,40000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117642,40100,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117643,40110,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117644,40120,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117645,40130,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117646,40200,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117647,40210,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117648,40220,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117649,40300,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117650,40400,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117651,40410,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117652,40420,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117653,40430,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117654,40440,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117655,40450,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117656,41300,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117657,40750,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117658,41330,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117659,40700,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117660,40800,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117661,40810,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117662,40830,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117663,40840,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117664,40850,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117665,41000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117666,41010,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117667,41020,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117668,41030,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117669,41040,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117670,41050,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117671,41060,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117672,41100,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117673,41200,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117674,41210,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117675,41220,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117676,41230,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117677,40150,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117678,30000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117679,30500,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117680,31000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117681,21200,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117682,21210,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117683,21220,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117684,33000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117685,34000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117686,35000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117687,30600,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117688,21100,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117689,38000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117690,50000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117691,50100,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117692,50110,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117693,50200,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117694,50210,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117695,50220,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117696,50300,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117697,50400,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117698,50500,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117699,50600,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117700,50630,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117701,50631,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117702,50632,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117703,50633,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117704,50640,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117705,50650,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117706,50651,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117707,50652,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117708,50653,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117709,56161,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117710,50700,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117711,50710,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117712,50720,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117713,50730,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117714,50800,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117715,50810,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117716,50820,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117717,50830,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117718,50840,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117719,50850,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117720,50860,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117721,60000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117722,60100,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117723,60200,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117724,800000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117725,801000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117726,808000,NULL,'EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117727,806000,NULL,'EhNamespaces',999962,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001188,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999962,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001189,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999962,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001190,'',0,1001190,1,'活动管理-默认子分类','/1/1001190',0,2,1,NOW(),0,NULL,999962,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001191,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999962,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001192,'',0,1001192,2,'活动管理二-默认子分类','/2/1001192',0,2,1,NOW(),0,NULL,999962,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001193,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999962,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001194,'',0,1001194,3,'活动管理三-默认子分类','/3/1001194',0,2,1,NOW(),0,NULL,999962,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1039194,240111044332060031);

    

SET @namespace_id=999962;
SET @banner_id = (SELECT MAX(id) FROM `eh_banners`);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvM1ptTTBaVGhqWVRWak1tVTFNbU00T0daa1l6azJNVEJtTURVeU1HUTNPUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvM1ptTTBaVGhqWVRWak1tVTFNbU00T0daa1l6azJNVEJtTURVeU1HUTNPUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRwa05HRXlNVEppTnpZM09UZ3lObVk1Tm1JMlpUVXpNV1UxTURNMlpUazBaZw', '0', '', NULL, NULL, '2', '20', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRwa05HRXlNVEppTnpZM09UZ3lObVk1Tm1JMlpUVXpNV1UxTURNMlpUazBaZw', '0', '', NULL, NULL, '2', '20', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvd1kyUTBaamt6TkdFM00yUTRNVGRpWWprd1lqUTVZV0ptTWpCbFpHWm1NUQ', '0', '', NULL, NULL, '2', '30', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvd1kyUTBaamt6TkdFM00yUTRNVGRpWWprd1lqUTVZV0ptTWpCbFpHWm1NUQ', '0', '', NULL, NULL, '2', '30', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');


INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (790,999962,'HomeWuyeLayout','{"versionCode":"2017112301","layoutName":"HomeWuyeLayout","displayName":"物业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2017112301,0,2,NULL,'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (791,999962,'HomeWuyeLayout','{"versionCode":"2017112301","layoutName":"HomeWuyeLayout","displayName":"物业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2017112301,0,2,NULL,'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (792,999962,'HomeResourcebookLayout','{"versionCode":"2017112301","layoutName":"HomeResourcebookLayout","displayName":"资源预定","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2017112301,0,2,NULL,'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (793,999962,'HomeResourcebookLayout','{"versionCode":"2017112301","layoutName":"HomeResourcebookLayout","displayName":"资源预定","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2017112301,0,2,NULL,'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (794,999962,'ServiceMarketLayout','{"versionCode":"2017112301","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":1,"separatorHeight":16},{"title":"园区快讯","groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":2},"defaultOrder":40,"separatorFlag":0,"separatorHeight":0}]}',2017112301,0,2,NULL,'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (795,999962,'ServiceMarketLayout','{"versionCode":"2017112301","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":1,"separatorHeight":16},{"title":"园区快讯","groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":2},"defaultOrder":40,"separatorFlag":0,"separatorHeight":0}]}',2017112301,0,2,NULL,'park_tourist',0,0,0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132971,999962,0,0,0,'/home','NavigatorGroup2','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRvd05EVTJaR0UwTTJWaFlqQTBaRFl3TnpsaFpqSXhaV1k0T0RSbVpUVTBZdw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,1,'pm_admin',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132972,999962,0,0,0,'/home','NavigatorGroup2','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRvd05EVTJaR0UwTTJWaFlqQTBaRFl3TnpsaFpqSXhaV1k0T0RSbVpUVTBZdw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,1,'park_tourist',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132973,999962,0,0,0,'/home','NavigatorGroup2','园区办事','园区办事','cs://1/image/aW1hZ2UvTVRvM1ptWXhZalUyTnpBM1lUTXdPV1l3TlRBNU1qRmtZMlJsTnpobVpqa3pZUQ',1,1,33,'{"type":212698,"parentId":212698,"displayType": "grid"}',20,0,1,1,0,1,'pm_admin',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132974,999962,0,0,0,'/home','NavigatorGroup2','园区办事','园区办事','cs://1/image/aW1hZ2UvTVRvM1ptWXhZalUyTnpBM1lUTXdPV1l3TlRBNU1qRmtZMlJsTnpobVpqa3pZUQ',1,1,33,'{"type":212698,"parentId":212698,"displayType": "grid"}',20,0,1,1,0,1,'park_tourist',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132975,999962,0,0,0,'/home','NavigatorGroup2','园区招商','园区招商','cs://1/image/aW1hZ2UvTVRvMk1UTmhOalU1TW1OaFpXWTFOR1F6WXpKaFpqY3hPREJqTmpRMk1qQTJNZw',1,1,28,'',30,0,1,1,0,1,'pm_admin',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132976,999962,0,0,0,'/home','NavigatorGroup2','园区招商','园区招商','cs://1/image/aW1hZ2UvTVRvMk1UTmhOalU1TW1OaFpXWTFOR1F6WXpKaFpqY3hPREJqTmpRMk1qQTJNZw',1,1,28,'',30,0,1,1,0,1,'park_tourist',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132977,999962,0,0,0,'/home','NavigatorGroup2','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRvNVl6azBZVFk0T0RVd01HTm1ORFExT0dRMk1EUmhORGt5TW1Jd1lqUTVaUQ',1,1,2,'{"itemLocation":"/home/wuye","layoutName":"HomeWuyeLayout","title":"物业服务","entityTag":"PM"}',40,0,1,1,0,1,'pm_admin',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132978,999962,0,0,0,'/home','NavigatorGroup2','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRvNVl6azBZVFk0T0RVd01HTm1ORFExT0dRMk1EUmhORGt5TW1Jd1lqUTVaUQ',1,1,2,'{"itemLocation":"/home/wuye","layoutName":"HomeWuyeLayout","title":"物业服务","entityTag":"PM"}',40,0,1,1,0,1,'park_tourist',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132979,999962,0,0,0,'/home','NavigatorGroup2','园区企业','园区企业','cs://1/image/aW1hZ2UvTVRwa1lUa3dPR1F5WlRnMU1ERXpPVGxoTm1ZNU5UQXlOemt3T1Rjd1pHSXpNUQ',1,1,34,'',50,0,1,1,0,1,'pm_admin',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132980,999962,0,0,0,'/home','NavigatorGroup2','园区企业','园区企业','cs://1/image/aW1hZ2UvTVRwa1lUa3dPR1F5WlRnMU1ERXpPVGxoTm1ZNU5UQXlOemt3T1Rjd1pHSXpNUQ',1,1,34,'',50,0,1,1,0,1,'park_tourist',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132981,999962,0,0,0,'/home','NavigatorGroup2','资源预定','资源预定','cs://1/image/aW1hZ2UvTVRveFpHVXdNREpqTTJJeE1qRXhZalUyTURnMFl6SmlOalE1WXpCbU1XRmpOUQ',1,1,2,'{"itemLocation":"/home/resourcebook","layoutName":"HomeResourcebookLayout","title":"资源预定","entityTag":"PM"}',60,0,1,1,0,1,'pm_admin',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132982,999962,0,0,0,'/home','NavigatorGroup2','资源预定','资源预定','cs://1/image/aW1hZ2UvTVRveFpHVXdNREpqTTJJeE1qRXhZalUyTURnMFl6SmlOalE1WXpCbU1XRmpOUQ',1,1,2,'{"itemLocation":"/home/resourcebook","layoutName":"HomeResourcebookLayout","title":"资源预定","entityTag":"PM"}',60,0,1,1,0,1,'park_tourist',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132983,999962,0,0,0,'/home','NavigatorGroup2','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRwbE0yRTJZVFJrTUdRelpqVTJOemRoTkRCallqa3lZemswWmpKbFl6VXlPUQ',1,1,23,'',70,0,1,1,0,1,'pm_admin',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132984,999962,0,0,0,'/home','NavigatorGroup2','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRwbE0yRTJZVFJrTUdRelpqVTJOemRoTkRCallqa3lZemswWmpKbFl6VXlPUQ',1,1,23,'',70,0,1,1,0,1,'park_tourist',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132985,999962,0,0,0,'/home','NavigatorGroup2','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRvNU1ESTNORGN5TlRsbVlUbGlNbVZrWm1NeE56a3lNbVpoTVRNeU5XTXlaUQ',1,1,36,'{"privateFlag": 0}',80,0,1,0,0,1,'pm_admin',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132986,999962,0,0,0,'/home','NavigatorGroup2','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRvNU1ESTNORGN5TlRsbVlUbGlNbVZrWm1NeE56a3lNbVpoTVRNeU5XTXlaUQ',1,1,36,'{"privateFlag": 0}',80,0,1,0,0,1,'park_tourist',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132987,999962,0,0,0,'/home','NavigatorGroup2','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwaE9EWm1PRGxrTmpnMll6UTVZV0ZrTXpFNFl6VTBZemd6WWpFeE1tTmlOUQ',1,1,27,'',90,0,1,0,0,1,'pm_admin',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132988,999962,0,0,0,'/home','NavigatorGroup2','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwaE9EWm1PRGxrTmpnMll6UTVZV0ZrTXpFNFl6VTBZemd6WWpFeE1tTmlOUQ',1,1,27,'',90,0,1,0,0,1,'park_tourist',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132989,999962,0,0,0,'/home','NavigatorGroup2','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRwa1lUVTNaRE5pTm1Sak5qSXpNVE15Wm1VM056aGpaR1JqWmpZNE5tSXhaUQ',1,1,56,'',100,0,1,0,0,1,'pm_admin',0,100,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132990,999962,0,0,0,'/home','NavigatorGroup2','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRwa1lUVTNaRE5pTm1Sak5qSXpNVE15Wm1VM056aGpaR1JqWmpZNE5tSXhaUQ',1,1,56,'',100,0,1,0,0,1,'park_tourist',0,100,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132991,999962,0,0,0,'/home','NavigatorGroup2','商铺管理','商铺管理','cs://1/image/aW1hZ2UvTVRwaU56YzNNekppTnpZMk9UVm1OV0ZrTXpVM016STBOVGN3WVRjM05EVTRNQQ',1,1,14,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fmicroshop%2fhome%3fisfromindex%3d0%26_k%3dzlbiz#sign_suffix"}',110,0,1,0,0,1,'pm_admin',0,110,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132992,999962,0,0,0,'/home','NavigatorGroup2','商铺管理','商铺管理','cs://1/image/aW1hZ2UvTVRwaU56YzNNekppTnpZMk9UVm1OV0ZrTXpVM016STBOVGN3WVRjM05EVTRNQQ',1,1,14,'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fmicroshop%2fhome%3fisfromindex%3d0%26_k%3dzlbiz#sign_suffix"}',110,0,1,0,0,1,'park_tourist',0,110,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132993,999962,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRvNE9EazBPRGRrT1RabFkyRmlNV1E0TURWaE1tRTJOV1kwWXpGaVpEYzRZUQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'pm_admin',0,120,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132994,999962,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRvNE9EazBPRGRrT1RabFkyRmlNV1E0TURWaE1tRTJOV1kwWXpGaVpEYzRZUQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'park_tourist',0,120,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132995,999962,0,0,0,'/home/wuye','NavigatorGroup1','投诉保修','投诉保修','cs://1/image/aW1hZ2UvTVRvM1pqWmhabVEwTVRFNVlUSXlPR1UxTmpnME1ESTNNakUxTkdZNFlqUTBNZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203753&displayName=投诉保修"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132996,999962,0,0,0,'/home/wuye','NavigatorGroup1','投诉保修','投诉保修','cs://1/image/aW1hZ2UvTVRvM1pqWmhabVEwTVRFNVlUSXlPR1UxTmpnME1ESTNNakUxTkdZNFlqUTBNZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203753&displayName=投诉保修"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132997,999962,0,0,0,'/home/wuye','NavigatorGroup1','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRveE1XUXhaakZrTm1Zell6ZzNNalppWm1Vek56ZGpPR0kzTVRZd09EQTFNQQ',1,1,14,'{"url":"${home.url}/property_fee/index.html?hideNavigationBar=1#/bill_query?sign_suffix"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132998,999962,0,0,0,'/home/wuye','NavigatorGroup1','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRveE1XUXhaakZrTm1Zell6ZzNNalppWm1Vek56ZGpPR0kzTVRZd09EQTFNQQ',1,1,14,'{"url":"${home.url}/property_fee/index.html?hideNavigationBar=1#/bill_query?sign_suffix"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (132999,999962,0,0,0,'/home/wuye','NavigatorGroup1','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRwa04yVmtNR0l6TVRJd01EWXdNemRtWXpobVlXRTVNamhpTWpFelpUVTVPQQ',1,1,45,'',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (133000,999962,0,0,0,'/home/wuye','NavigatorGroup1','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRwa04yVmtNR0l6TVRJd01EWXdNemRtWXpobVlXRTVNamhpTWpFelpUVTVPQQ',1,1,45,'',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (133001,999962,0,0,0,'/home/wuye','NavigatorGroup1','物业巡检','物业巡检','cs://1/image/aW1hZ2UvTVRwak9EWTJZelV6TW1ReE16ZG1NREJpWVRBMU1UVXpZV1F3WkRGbE5EWmpaZw',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (133002,999962,0,0,0,'/home/wuye','NavigatorGroup1','物业巡检','物业巡检','cs://1/image/aW1hZ2UvTVRwak9EWTJZelV6TW1ReE16ZG1NREJpWVRBMU1UVXpZV1F3WkRGbE5EWmpaZw',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (133003,999962,0,0,0,'/home/resourcebook','NavigatorGroup1','会议室','会议室','cs://1/image/aW1hZ2UvTVRwaE5qRTRNelUwTXpBM1lqTmlNMkUzTlRRek1XUTJOakk0TVRZeVltTXhZUQ',1,1,49,'{"resourceTypeId":12144,"pageType":0}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (133004,999962,0,0,0,'/home/resourcebook','NavigatorGroup1','会议室','会议室','cs://1/image/aW1hZ2UvTVRwaE5qRTRNelUwTXpBM1lqTmlNMkUzTlRRek1XUTJOakk0TVRZeVltTXhZUQ',1,1,49,'{"resourceTypeId":12144,"pageType":0}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (133005,999962,0,0,0,'/home/resourcebook','NavigatorGroup1','电子屏','电子屏','cs://1/image/aW1hZ2UvTVRvMU5UVTNNekl3WVRKbVltRTNNVEEyWWpVNE5UTXlNMlE0T0RFM1lXUXlOQQ',1,1,49,'{"resourceTypeId":12145,"pageType":0}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (133006,999962,0,0,0,'/home/resourcebook','NavigatorGroup1','电子屏','电子屏','cs://1/image/aW1hZ2UvTVRvMU5UVTNNekl3WVRKbVltRTNNVEEyWWpVNE5UTXlNMlE0T0RFM1lXUXlOQQ',1,1,49,'{"resourceTypeId":12145,"pageType":0}',20,0,1,1,0,0,'park_tourist',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
	VALUES (212698,'community','240111044331055940',0,'园区办事','园区办事',0,2,1,NULL,999962);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211194,0,'organaization',1039194,'all','园区办事','园区办事',212698,2,211194,NULL,2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117728,41700,'园区办事','EhNamespaces',999962,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117729,41710,'','EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117730,41720,'','EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117731,41730,'','EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117732,41740,'','EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117733,41750,'','EhNamespaces',999962,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117734,41760,'','EhNamespaces',999962,2);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (203753,6,0,'物业报修','任务/物业报修',1,2,NULL,999962);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12144,'会议室',0,2,999962,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12145,'电子屏',0,2,999962,0,0);
