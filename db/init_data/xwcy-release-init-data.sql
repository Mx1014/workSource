-- xwcy by ljs
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1944,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999954','the relative path for XinWeiChuangYuan app agreements',999954,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1945,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999954,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1946,'pmtask.handler-999954','flow','0',999954,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1947,'pay.platform','1','支付类型',999954,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (495,'Android_XinWeiChuangYuan',NULL,UTC_TIMESTAMP(),999954);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (496,'iOS_XinWeiChuangYuan',NULL,UTC_TIMESTAMP(),999954);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (836,495,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999954);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (837,496,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999954);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999954,'新微创源');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1496,999954,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (924,'sms.default.sign',0,'zh_CN','新微创源','【新微创源】',999954);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18443,0,'上海','SHANGHAI','SH','/上海',1,1,NULL,NULL,2,0,999954);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18444,18443,'上海市','SHANGHAISHI','SHS','/上海/上海市',2,2,NULL,'021',2,1,999954);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18445,18444,'嘉定区','JIADINGQU','JDQ','/上海/上海市/嘉定区',3,3,NULL,'021',2,0,999954);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060084,UUID(),18444,'上海市',18445,'嘉定区','新微创源孵化器','','平城路1455号新微大厦B座','新微创源是以物联网与先进传感器产业为核心方向的专业孵化器，是中科院微系统与信 息技术研究所联合InnoSpring、菊园物联网、达泰资本、中科院创新孵化投资等战略合作 伙伴，融合了科研院所的技术产业资源、InnoSpring的国际孵化网络资源、菊园新区的政 策扶持资源、达泰资本的投融资资源为一体的科技创新型孵化加速平台。',0,1,2,UTC_TIMESTAMP(),1,192719,192720,UTC_TIMESTAMP(),999954);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092857,240111044332060084,'',121.242193,31.395455,'wtw4wgmggbgx');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20194,999954,'COMMUNITY',240111044332060084,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1040187,0,'PM','新微创源孵化器','新微创源是以物联网与先进传感器产业为核心方向的专业孵化器，是中科院微系统与信 息技术研究所联合InnoSpring、菊园物联网、达泰资本、中科院创新孵化投资等战略合作 伙伴，融合了科研院所的技术产业资源、InnoSpring的国际孵化网络资源、菊园新区的政 策扶持资源、达泰资本的投融资资源为一体的科技创新型孵化加速平台。','/1040187',1,2,'ENTERPRISE',999954,1058264);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1155286,240111044332060084,'organization',1040187,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (25125,'EhOrganizations',1040187,1,10,468767,0,1,UTC_TIMESTAMP(),999954,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (25126,'EhOrganizations',1040187,1,10,468768,0,1,UTC_TIMESTAMP(),999954,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (25127,'EhOrganizations',1040187,1,10,468769,0,1,UTC_TIMESTAMP(),999954,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1058264,UUID(),'新微创源孵化器','新微创源孵化器',1,1,1040187,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192718,1,999954);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192718,UUID(),999954,2,'EhGroups',1058264,'新微创源孵化器论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192719,UUID(),999954,2,'',0,'新微创源社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192720,UUID(),999954,2,'',0,'新微创源意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256540,UUID(),999954,192719,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (468767,UUID(),'19224995617','猫姐',NULL,1,45,'1','2','zh_CN','dc88c26d086b94cc46e45d7aef95af77','5eadc5a122c88c314e81f5daa29189ab7569585b47c840d44537a1ed3afbaafa',UTC_TIMESTAMP(),999954);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (468768,UUID(),'19224995618','马颖蕾',NULL,1,45,'1','1','zh_CN','e1f28da974a06568dcfdfe079b57a1b8','6ea58835a47119ad28f826e04cc6b2fe8cb26201a1684392e7650202a06ed9fd',UTC_TIMESTAMP(),999954);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (468769,UUID(),'19224995619','左邻官方账号',NULL,1,45,'1','1','zh_CN','d727c0f33441d9a778a3b14c802873b4','92f489915943b96cc137f68f88c7a6f3bd3cd7ac04cabfbbb994464104e15ba6',UTC_TIMESTAMP(),999954);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (29847,1040187,'USER',468767,'猫姐',0,'13810286036',UTC_TIMESTAMP(),999954);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (29848,1040187,'USER',468768,'马颖蕾',0,'13916745520',UTC_TIMESTAMP(),999954);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (29849,1040187,'USER',468769,'左邻官方账号',0,'12000001802',UTC_TIMESTAMP(),999954);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (440067,468767,0,'13810286036',NULL,3,UTC_TIMESTAMP(),999954);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (440068,468768,0,'13916745520',NULL,3,UTC_TIMESTAMP(),999954);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (440069,468769,0,'12000001802',NULL,3,UTC_TIMESTAMP(),999954);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2180510,1040187,'/1040187','USER',468767,'manager','猫姐',0,'13810286036',3,999954,'ENTERPRISE',0,29847);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2180511,1040187,'/1040187','USER',468768,'manager','马颖蕾',0,'13916745520',3,999954,'ENTERPRISE',0,29848);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2180512,1040187,'/1040187','USER',468769,'manager','左邻官方账号',0,'12000001802',3,999954,'ENTERPRISE',0,29849);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (29558,468767,1040187,'/1040187','ENTERPRISE',3,999954,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (29559,468768,1040187,'/1040187','ENTERPRISE',3,999954,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (29560,468769,1040187,'/1040187','ENTERPRISE',3,999954,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115191,'EhOrganizations',1040187,'EhUsers',468767,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115192,'EhOrganizations',1040187,'EhUsers',468768,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115193,'EhOrganizations',1040187,'EhUsers',468769,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971003,240111044332060084,'新微创源孵化器','',0,13916745520,'平城路1455号新微大厦B座',15000.0,'121.242193','31.395455','wtw4wgmggbgx','',NULL,2,0,NULL,1,NOW(),999954,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462893,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼10层','新微创源孵化器','B楼10层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462894,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼11层','新微创源孵化器','B楼11层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462895,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼12层','新微创源孵化器','B楼12层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462896,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼13层','新微创源孵化器','B楼13层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462897,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-A楼副楼1层','新微创源孵化器','A楼副楼1层','2','0',UTC_TIMESTAMP(),999954,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462898,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-A楼副楼2层','新微创源孵化器','A楼副楼2层','2','0',UTC_TIMESTAMP(),999954,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462884,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼1层','新微创源孵化器','B楼1层','2','0',UTC_TIMESTAMP(),999954,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462885,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼2层','新微创源孵化器','B楼2层','2','0',UTC_TIMESTAMP(),999954,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462886,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼3层','新微创源孵化器','B楼3层','2','0',UTC_TIMESTAMP(),999954,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462887,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼4层','新微创源孵化器','B楼4层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462888,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼5层','新微创源孵化器','B楼5层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462889,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼6层','新微创源孵化器','B楼6层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462890,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼7层','新微创源孵化器','B楼7层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462891,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼8层','新微创源孵化器','B楼8层','2','0',UTC_TIMESTAMP(),999954,1200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462892,UUID(),240111044332060084,18444,'上海市',18445,'嘉定区','新微创源孵化器-B楼9层','新微创源孵化器','B楼9层','2','0',UTC_TIMESTAMP(),999954,1200.0);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85980,1040187,240111044332060084,239825274387462893,'新微创源孵化器-B楼10层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85981,1040187,240111044332060084,239825274387462894,'新微创源孵化器-B楼11层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85982,1040187,240111044332060084,239825274387462895,'新微创源孵化器-B楼12层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85983,1040187,240111044332060084,239825274387462896,'新微创源孵化器-B楼13层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85984,1040187,240111044332060084,239825274387462897,'新微创源孵化器-A楼副楼1层',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85985,1040187,240111044332060084,239825274387462898,'新微创源孵化器-A楼副楼2层',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85971,1040187,240111044332060084,239825274387462884,'新微创源孵化器-B楼1层',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85972,1040187,240111044332060084,239825274387462885,'新微创源孵化器-B楼2层',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85973,1040187,240111044332060084,239825274387462886,'新微创源孵化器-B楼3层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85974,1040187,240111044332060084,239825274387462887,'新微创源孵化器-B楼4层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85975,1040187,240111044332060084,239825274387462888,'新微创源孵化器-B楼5层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85976,1040187,240111044332060084,239825274387462889,'新微创源孵化器-B楼6层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85977,1040187,240111044332060084,239825274387462890,'新微创源孵化器-B楼7层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85978,1040187,240111044332060084,239825274387462891,'新微创源孵化器-B楼8层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85979,1040187,240111044332060084,239825274387462892,'新微创源孵化器-B楼9层',2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121178,10000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121179,10100,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121180,10400,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121181,10200,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121182,10600,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121183,10800,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121184,11000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121185,20000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121186,20400,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121187,204011,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121188,204021,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121189,20430,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121190,20422,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121191,21100,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121192,21110,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121193,21120,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121194,21200,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121195,21210,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121196,21220,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121197,21230,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121198,40000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121199,40300,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121200,40400,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121201,40410,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121202,40420,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121203,40430,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121204,40440,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121205,40450,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121206,40500,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121207,40800,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121208,40830,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121209,40810,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121210,40840,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121211,40850,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121212,41000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121213,41010,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121214,41020,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121215,41030,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121216,41040,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121217,41050,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121218,41060,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121219,41400,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121220,41410,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121221,41420,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121222,41430,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121223,30000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121224,30500,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121225,31000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121226,32000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121227,33000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121228,34000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121229,35000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121230,30600,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121231,50000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121232,50100,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121233,50300,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121234,50500,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121235,50700,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121236,50710,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121237,50720,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121238,50730,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121239,50900,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121240,51000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121241,60000,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121242,60100,NULL,'EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121243,60200,NULL,'EhNamespaces',999954,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001260,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999954,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001261,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999954,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001262,'',0,1001262,1,'活动管理-默认子分类','/1/1001262',0,2,1,NOW(),0,NULL,999954,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001263,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999954,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001264,'',0,1001264,2,'活动管理二-默认子分类','/2/1001264',0,2,1,NOW(),0,NULL,999954,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001265,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999954,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001266,'',0,1001266,3,'活动管理三-默认子分类','/3/1001266',0,2,1,NOW(),0,NULL,999954,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (380,999954,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (381,999954,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (382,999954,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (383,999954,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (384,999954,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (385,999954,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (386,999954,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (387,999954,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (388,999954,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1040187,240111044332060084);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (916,999954,'HomeZhfwLayout','{"versionCode":"2017121901","layoutName":"HomeZhfwLayout","displayName":"综合服务","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2017121901,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (917,999954,'HomeZhfwLayout','{"versionCode":"2017121901","layoutName":"HomeZhfwLayout","displayName":"综合服务","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2017121901,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (918,999954,'SecondServiceMarketLayout','{"versionCode":"2017121901","layoutName":"SecondServiceMarketLayout","displayName":"服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017121901,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (919,999954,'SecondServiceMarketLayout','{"versionCode":"2017121901","layoutName":"SecondServiceMarketLayout","displayName":"服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017121901,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (920,999954,'HomeResourceLayout','{"versionCode":"2017121901","layoutName":"HomeResourceLayout","displayName":"资源预订","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2017121901,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (921,999954,'HomeResourceLayout','{"versionCode":"2017121901","layoutName":"HomeResourceLayout","displayName":"资源预订","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2017121901,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (922,999954,'HomeZyfwLayout','{"versionCode":"2017121901","layoutName":"HomeZyfwLayout","displayName":"专业服务","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017121901,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (923,999954,'HomeZyfwLayout','{"versionCode":"2017121901","layoutName":"HomeZyfwLayout","displayName":"专业服务","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017121901,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (924,999954,'ServiceMarketLayout','{"versionCode":"2017121901","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":5},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2017121901,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (925,999954,'ServiceMarketLayout','{"versionCode":"2017121901","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":5},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2017121901,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216754,999954,0,'/home/zhfw','Default',0,0,'/home/zhfw','Default','cs://1/image/aW1hZ2UvTVRwak1HUmxZMlV5TjJObU5UazBZVE0xT0RsaFpEZGlNbUppT0dRd01HRXdOZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216755,999954,0,'/home/zhfw','Default',0,0,'/home/zhfw','Default','cs://1/image/aW1hZ2UvTVRwak1HUmxZMlV5TjJObU5UazBZVE0xT0RsaFpEZGlNbUppT0dRd01HRXdOZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216756,999954,0,'/home/zyfw','Default',0,0,'/home/zyfw','Default','cs://1/image/aW1hZ2UvTVRwak1HUmxZMlV5TjJObU5UazBZVE0xT0RsaFpEZGlNbUppT0dRd01HRXdOZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216757,999954,0,'/home/zyfw','Default',0,0,'/home/zyfw','Default','cs://1/image/aW1hZ2UvTVRwak1HUmxZMlV5TjJObU5UazBZVE0xT0RsaFpEZGlNbUppT0dRd01HRXdOZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216758,999954,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRwak1HUmxZMlV5TjJObU5UazBZVE0xT0RsaFpEZGlNbUppT0dRd01HRXdOZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216759,999954,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRwak1HUmxZMlV5TjJObU5UazBZVE0xT0RsaFpEZGlNbUppT0dRd01HRXdOZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141505,999954,0,0,0,'/home/zyfw','NavigatorGroup2','测试工程','测试工程','cs://1/image/aW1hZ2UvTVRvME1tUTFOalUzTmpWbE1tUTBORGs1TlRCaE9XRTBPVGcwTnpBME9HTmxNQQ',1,1,33,'{"type":212853,"parentId":212853,"displayType": "list"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141506,999954,0,0,0,'/home/zyfw','NavigatorGroup2','测试工程','测试工程','cs://1/image/aW1hZ2UvTVRvME1tUTFOalUzTmpWbE1tUTBORGs1TlRCaE9XRTBPVGcwTnpBME9HTmxNQQ',1,1,33,'{"type":212853,"parentId":212853,"displayType": "list"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141507,999954,0,0,0,'/home/zyfw','NavigatorGroup2','其他需求','其他需求','cs://1/image/aW1hZ2UvTVRveFpUWTNZamd4WVdSaU1tVXdZelZtT0RaaVl6bG1ZekJrTWpCak5XRTFZUQ',1,1,33,'{"type":212854,"parentId":212854,"displayType": "list"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141508,999954,0,0,0,'/home/zyfw','NavigatorGroup2','其他需求','其他需求','cs://1/image/aW1hZ2UvTVRveFpUWTNZamd4WVdSaU1tVXdZelZtT0RaaVl6bG1ZekJrTWpCak5XRTFZUQ',1,1,33,'{"type":212854,"parentId":212854,"displayType": "list"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141509,999954,0,0,0,'/home/zhfw','NavigatorGroup2','培训服务','培训服务','cs://1/image/aW1hZ2UvTVRwaVl6ZGlaR0UyWmpJMVpXSmpZakV4T0RZeE5EWmhNV1EwWmpJMU5UQTNaUQ',1,1,33,'{"type":212855,"parentId":212855,"displayType": "list"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141510,999954,0,0,0,'/home/zhfw','NavigatorGroup2','培训服务','培训服务','cs://1/image/aW1hZ2UvTVRwaVl6ZGlaR0UyWmpJMVpXSmpZakV4T0RZeE5EWmhNV1EwWmpJMU5UQTNaUQ',1,1,33,'{"type":212855,"parentId":212855,"displayType": "list"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141511,999954,0,0,0,'/home/zhfw','NavigatorGroup2','金融服务','金融服务','cs://1/image/aW1hZ2UvTVRvMVlXUXdNMlJpTUdSbFpHWTBZelZrTkdSaVl6RTFOVEE1TnpnMVl6SXhOUQ',1,1,33,'{"type":212856,"parentId":212856,"displayType": "list"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141512,999954,0,0,0,'/home/zhfw','NavigatorGroup2','金融服务','金融服务','cs://1/image/aW1hZ2UvTVRvMVlXUXdNMlJpTUdSbFpHWTBZelZrTkdSaVl6RTFOVEE1TnpnMVl6SXhOUQ',1,1,33,'{"type":212856,"parentId":212856,"displayType": "list"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141513,999954,0,0,0,'/home/zhfw','NavigatorGroup2','知识产权','知识产权','cs://1/image/aW1hZ2UvTVRvM1pqVmxZalU0TkdFNE9EQTFObU00TkdKaE9EZzBPV0ZsTWpBMFpXWm1OQQ',1,1,33,'{"type":212857,"parentId":212857,"displayType": "list"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141514,999954,0,0,0,'/home/zhfw','NavigatorGroup2','知识产权','知识产权','cs://1/image/aW1hZ2UvTVRvM1pqVmxZalU0TkdFNE9EQTFObU00TkdKaE9EZzBPV0ZsTWpBMFpXWm1OQQ',1,1,33,'{"type":212857,"parentId":212857,"displayType": "list"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141515,999954,0,0,0,'/home/zhfw','NavigatorGroup2','法律服务','法律服务','cs://1/image/aW1hZ2UvTVRwallUZGxOR05pWmpFM056QTRaamM0TmpZM016Sm1OMkUxT1RVMllqZ3dOdw',1,1,33,'{"type":212858,"parentId":212858,"displayType": "list"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141516,999954,0,0,0,'/home/zhfw','NavigatorGroup2','法律服务','法律服务','cs://1/image/aW1hZ2UvTVRwallUZGxOR05pWmpFM056QTRaamM0TmpZM016Sm1OMkUxT1RVMllqZ3dOdw',1,1,33,'{"type":212858,"parentId":212858,"displayType": "list"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141517,999954,0,0,0,'/secondhome','NavigatorGroup1','资源对接','资源对接','cs://1/image/aW1hZ2UvTVRvNVlXTm1ZbUZsTlROaU5EUTNZV1l5Wm1KbU5UZGxaV1V5TlRWak9XTTBNQQ',1,1,33,'{"type":212851,"parentId":212851,"displayType": "list"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141518,999954,0,0,0,'/secondhome','NavigatorGroup1','资源对接','资源对接','cs://1/image/aW1hZ2UvTVRvNVlXTm1ZbUZsTlROaU5EUTNZV1l5Wm1KbU5UZGxaV1V5TlRWak9XTTBNQQ',1,1,33,'{"type":212851,"parentId":212851,"displayType": "list"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141519,999954,0,0,0,'/secondhome','NavigatorGroup1','技术转移','技术转移','cs://1/image/aW1hZ2UvTVRwak1UaGxaV1kyWTJNd1pXSTFZemM1WWpNMlpEZzNOekEyTTJSaU9HRTJPUQ',1,1,33,'{"type":212852,"parentId":212852,"displayType": "list"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141520,999954,0,0,0,'/secondhome','NavigatorGroup1','技术转移','技术转移','cs://1/image/aW1hZ2UvTVRwak1UaGxaV1kyWTJNd1pXSTFZemM1WWpNMlpEZzNOekEyTTJSaU9HRTJPUQ',1,1,33,'{"type":212852,"parentId":212852,"displayType": "list"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141521,999954,0,0,0,'/secondhome','NavigatorGroup1','测试工程','测试工程','cs://1/image/aW1hZ2UvTVRvME1tUTFOalUzTmpWbE1tUTBORGs1TlRCaE9XRTBPVGcwTnpBME9HTmxNQQ',1,1,33,'{"type":212853,"parentId":212853,"displayType": "list"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141522,999954,0,0,0,'/secondhome','NavigatorGroup1','测试工程','测试工程','cs://1/image/aW1hZ2UvTVRvME1tUTFOalUzTmpWbE1tUTBORGs1TlRCaE9XRTBPVGcwTnpBME9HTmxNQQ',1,1,33,'{"type":212853,"parentId":212853,"displayType": "list"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141523,999954,0,0,0,'/secondhome','NavigatorGroup1','其他需求','其他需求','cs://1/image/aW1hZ2UvTVRveFpUWTNZamd4WVdSaU1tVXdZelZtT0RaaVl6bG1ZekJrTWpCak5XRTFZUQ',1,1,33,'{"type":212854,"parentId":212854,"displayType": "list"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141524,999954,0,0,0,'/secondhome','NavigatorGroup1','其他需求','其他需求','cs://1/image/aW1hZ2UvTVRveFpUWTNZamd4WVdSaU1tVXdZelZtT0RaaVl6bG1ZekJrTWpCak5XRTFZUQ',1,1,33,'{"type":212854,"parentId":212854,"displayType": "list"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141525,999954,0,0,0,'/secondhome','NavigatorGroup1','培训服务','培训服务','cs://1/image/aW1hZ2UvTVRwaVl6ZGlaR0UyWmpJMVpXSmpZakV4T0RZeE5EWmhNV1EwWmpJMU5UQTNaUQ',1,1,33,'{"type":212855,"parentId":212855,"displayType": "list"}',50,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141526,999954,0,0,0,'/secondhome','NavigatorGroup1','培训服务','培训服务','cs://1/image/aW1hZ2UvTVRwaVl6ZGlaR0UyWmpJMVpXSmpZakV4T0RZeE5EWmhNV1EwWmpJMU5UQTNaUQ',1,1,33,'{"type":212855,"parentId":212855,"displayType": "list"}',50,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141527,999954,0,0,0,'/secondhome','NavigatorGroup1','金融服务','金融服务','cs://1/image/aW1hZ2UvTVRvMVlXUXdNMlJpTUdSbFpHWTBZelZrTkdSaVl6RTFOVEE1TnpnMVl6SXhOUQ',1,1,33,'{"type":212856,"parentId":212856,"displayType": "list"}',60,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141528,999954,0,0,0,'/secondhome','NavigatorGroup1','金融服务','金融服务','cs://1/image/aW1hZ2UvTVRvMVlXUXdNMlJpTUdSbFpHWTBZelZrTkdSaVl6RTFOVEE1TnpnMVl6SXhOUQ',1,1,33,'{"type":212856,"parentId":212856,"displayType": "list"}',60,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141529,999954,0,0,0,'/secondhome','NavigatorGroup1','知识产权','知识产权','cs://1/image/aW1hZ2UvTVRvM1pqVmxZalU0TkdFNE9EQTFObU00TkdKaE9EZzBPV0ZsTWpBMFpXWm1OQQ',1,1,33,'{"type":212857,"parentId":212857,"displayType": "list"}',70,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141530,999954,0,0,0,'/secondhome','NavigatorGroup1','知识产权','知识产权','cs://1/image/aW1hZ2UvTVRvM1pqVmxZalU0TkdFNE9EQTFObU00TkdKaE9EZzBPV0ZsTWpBMFpXWm1OQQ',1,1,33,'{"type":212857,"parentId":212857,"displayType": "list"}',70,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141531,999954,0,0,0,'/secondhome','NavigatorGroup1','法律服务','法律服务','cs://1/image/aW1hZ2UvTVRwallUZGxOR05pWmpFM056QTRaamM0TmpZM016Sm1OMkUxT1RVMllqZ3dOdw',1,1,33,'{"type":212858,"parentId":212858,"displayType": "list"}',80,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141532,999954,0,0,0,'/secondhome','NavigatorGroup1','法律服务','法律服务','cs://1/image/aW1hZ2UvTVRwallUZGxOR05pWmpFM056QTRaamM0TmpZM016Sm1OMkUxT1RVMllqZ3dOdw',1,1,33,'{"type":212858,"parentId":212858,"displayType": "list"}',80,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141477,999954,0,0,0,'/home','NavigatorGroup2','智能门禁','智能门禁','cs://1/image/aW1hZ2UvTVRvNU5ETTBNRE15WkRRNFpXRXlaamxoTUdOaE5XTmlNRFl6TTJOaU5UQmtPQQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141478,999954,0,0,0,'/home','NavigatorGroup2','智能门禁','智能门禁','cs://1/image/aW1hZ2UvTVRvNU5ETTBNRE15WkRRNFpXRXlaamxoTUdOaE5XTmlNRFl6TTJOaU5UQmtPQQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141479,999954,0,0,0,'/home','NavigatorGroup2','停车充值','停车充值','cs://1/image/aW1hZ2UvTVRvelpHWmpNMkl4TTJWaE5qZGpZV1ppT0RRek5UYzVNelJqTURjd1pUVmpNZw',1,1,30,'',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141480,999954,0,0,0,'/home','NavigatorGroup2','停车充值','停车充值','cs://1/image/aW1hZ2UvTVRvelpHWmpNMkl4TTJWaE5qZGpZV1ppT0RRek5UYzVNelJqTURjd1pUVmpNZw',1,1,30,'',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141481,999954,0,0,0,'/home','NavigatorGroup2','资源预订','资源预订','cs://1/image/aW1hZ2UvTVRwbU0yUXdaalZoTUdRNU9XTXpZakkwWlRBME9UQTJNMlF6WVRrME9XWXlNUQ',1,1,60,'{"url":"zl://association/main?layoutName=HomeResourceLayout&itemLocation=/home/resource&versionCode=2017121901&displayName=资源预订"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141482,999954,0,0,0,'/home','NavigatorGroup2','资源预订','资源预订','cs://1/image/aW1hZ2UvTVRwbU0yUXdaalZoTUdRNU9XTXpZakkwWlRBME9UQTJNMlF6WVRrME9XWXlNUQ',1,1,60,'{"url":"zl://association/main?layoutName=HomeResourceLayout&itemLocation=/home/resource&versionCode=2017121901&displayName=资源预订"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141483,999954,0,0,0,'/home/resource','TabGroup','会议室预订','会议室预订','',1,1,49,'{"resourceTypeId":12209,"pageType":0}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141484,999954,0,0,0,'/home/resource','TabGroup','会议室预订','会议室预订','',1,1,49,'{"resourceTypeId":12209,"pageType":0}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141485,999954,0,0,0,'/home/resource','TabGroup','场地预订','场地预订','',1,1,49,'{"resourceTypeId":12210,"pageType":0}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141486,999954,0,0,0,'/home/resource','TabGroup','场地预订','场地预订','',1,1,49,'{"resourceTypeId":12210,"pageType":0}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141487,999954,0,0,0,'/home','NavigatorGroup2','账单查询','账单查询','cs://1/image/aW1hZ2UvTVRveU1qQTJOekF3TTJNNU1XTm1NalUzTURFNU5qbG1aRGxpWlRBMFkySmtOUQ',1,1,14,'{"url":"https://core.zuolin.com/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141488,999954,0,0,0,'/home','NavigatorGroup2','账单查询','账单查询','cs://1/image/aW1hZ2UvTVRveU1qQTJOekF3TTJNNU1XTm1NalUzTURFNU5qbG1aRGxpWlRBMFkySmtOUQ',1,1,14,'{"url":"https://core.zuolin.com/property-management/build/index.html?hideNavigationBar=1&name=1#/verify_account#sign_suffix"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141489,999954,0,0,0,'/home','NavigatorGroup2','园区热线','园区热线','cs://1/image/aW1hZ2UvTVRvd1l6UTBZbVl5WldRellqQXlOamhrWmpjNE1tVmpZemt4TmpsbVpESm1PQQ',1,1,45,'',50,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141490,999954,0,0,0,'/home','NavigatorGroup2','园区热线','园区热线','cs://1/image/aW1hZ2UvTVRvd1l6UTBZbVl5WldRellqQXlOamhrWmpjNE1tVmpZemt4TmpsbVpESm1PQQ',1,1,45,'',50,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141491,999954,0,0,0,'/home','NavigatorGroup2','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRwbE1EY3laalUzTXpCaFltTTVaR0V6T1dabU1tWXpabVJsTmpRNFpXTm1aZw',1,1,56,'',60,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141492,999954,0,0,0,'/home','NavigatorGroup2','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRwbE1EY3laalUzTXpCaFltTTVaR0V6T1dabU1tWXpabVJsTmpRNFpXTm1aZw',1,1,56,'',60,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141493,999954,0,0,0,'/home','NavigatorGroup4','关于我们','关于我们','cs://1/image/aW1hZ2UvTVRwak1qQXpOVFUwT1dFMk5XUTBZVGMwTTJabVpqQTVORGRsTnpobE1Ea3haZw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141494,999954,0,0,0,'/home','NavigatorGroup4','关于我们','关于我们','cs://1/image/aW1hZ2UvTVRwak1qQXpOVFUwT1dFMk5XUTBZVGMwTTJabVpqQTVORGRsTnpobE1Ea3haZw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141495,999954,0,0,0,'/home','NavigatorGroup4','企业风采','企业风采','cs://1/image/aW1hZ2UvTVRvNE1qSm1ZakpoWVRVeVpqbGlaRFF4TTJJek9UY3pOMlppTVRVMVkyWmpaUQ',1,1,34,'',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141496,999954,0,0,0,'/home','NavigatorGroup4','企业风采','企业风采','cs://1/image/aW1hZ2UvTVRvNE1qSm1ZakpoWVRVeVpqbGlaRFF4TTJJek9UY3pOMlppTVRVMVkyWmpaUQ',1,1,34,'',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141497,999954,0,0,0,'/home','NavigatorGroup4','专业服务','专业服务','cs://1/image/aW1hZ2UvTVRvNU1HSXpabUpoTldReFpUbG1OR1ZtT1RFMk1qTXhOV1EzTkRKa09HWTVZUQ',1,1,2,'{"itemLocation":"/home/zyfw","layoutName":"HomeZyfwLayout","title":"专业服务","entityTag":"PM"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141498,999954,0,0,0,'/home','NavigatorGroup4','专业服务','专业服务','cs://1/image/aW1hZ2UvTVRvNU1HSXpabUpoTldReFpUbG1OR1ZtT1RFMk1qTXhOV1EzTkRKa09HWTVZUQ',1,1,2,'{"itemLocation":"/home/zyfw","layoutName":"HomeZyfwLayout","title":"专业服务","entityTag":"PM"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141499,999954,0,0,0,'/home','NavigatorGroup4','综合服务','综合服务','cs://1/image/aW1hZ2UvTVRwbU1qZzFOV0prTmpNek5qaG1ZVGsyWldKa1pqazBORGhtWkRJMFlUQTVaQQ',1,1,2,'{"itemLocation":"/home/zhfw","layoutName":"HomeZhfwLayout","title":"综合服务","entityTag":"PM"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141500,999954,0,0,0,'/home','NavigatorGroup4','综合服务','综合服务','cs://1/image/aW1hZ2UvTVRwbU1qZzFOV0prTmpNek5qaG1ZVGsyWldKa1pqazBORGhtWkRJMFlUQTVaQQ',1,1,2,'{"itemLocation":"/home/zhfw","layoutName":"HomeZhfwLayout","title":"综合服务","entityTag":"PM"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141501,999954,0,0,0,'/home/zyfw','NavigatorGroup2','资源对接','资源对接','cs://1/image/aW1hZ2UvTVRvNVlXTm1ZbUZsTlROaU5EUTNZV1l5Wm1KbU5UZGxaV1V5TlRWak9XTTBNQQ',1,1,33,'{"type":212851,"parentId":212851,"displayType": "list"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141502,999954,0,0,0,'/home/zyfw','NavigatorGroup2','资源对接','资源对接','cs://1/image/aW1hZ2UvTVRvNVlXTm1ZbUZsTlROaU5EUTNZV1l5Wm1KbU5UZGxaV1V5TlRWak9XTTBNQQ',1,1,33,'{"type":212851,"parentId":212851,"displayType": "list"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141503,999954,0,0,0,'/home/zyfw','NavigatorGroup2','技术转移','技术转移','cs://1/image/aW1hZ2UvTVRwak1UaGxaV1kyWTJNd1pXSTFZemM1WWpNMlpEZzNOekEyTTJSaU9HRTJPUQ',1,1,33,'{"type":212852,"parentId":212852,"displayType": "list"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (141504,999954,0,0,0,'/home/zyfw','NavigatorGroup2','技术转移','技术转移','cs://1/image/aW1hZ2UvTVRwak1UaGxaV1kyWTJNd1pXSTFZemM1WWpNMlpEZzNOekEyTTJSaU9HRTJPUQ',1,1,33,'{"type":212852,"parentId":212852,"displayType": "list"}',20,0,1,1,0,0,'park_tourist',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212854,'community','240111044331055940',0,'其他需求','其他需求',0,2,1,UTC_TIMESTAMP(),999954,4);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212855,'community','240111044331055940',0,'培训服务','培训服务',0,2,1,UTC_TIMESTAMP(),999954,5);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212856,'community','240111044331055940',0,'金融服务','金融服务',0,2,1,UTC_TIMESTAMP(),999954,6);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212857,'community','240111044331055940',0,'知识产权','知识产权',0,2,1,UTC_TIMESTAMP(),999954,7);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212858,'community','240111044331055940',0,'法律服务','法律服务',0,2,1,UTC_TIMESTAMP(),999954,8);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212851,'community','240111044331055940',0,'资源对接','资源对接',0,2,1,UTC_TIMESTAMP(),999954,1);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212852,'community','240111044331055940',0,'技术转移','技术转移',0,2,1,UTC_TIMESTAMP(),999954,2);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212853,'community','240111044331055940',0,'测试工程','测试工程',0,2,1,UTC_TIMESTAMP(),999954,3);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211457,0,'organaization',1040187,'all','其他需求','其他需求',212854,2,211457,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211458,0,'organaization',1040187,'all','培训服务','培训服务',212855,2,211458,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211459,0,'organaization',1040187,'all','金融服务','金融服务',212856,2,211459,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211460,0,'organaization',1040187,'all','知识产权','知识产权',212857,2,211460,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211461,0,'organaization',1040187,'all','法律服务','法律服务',212858,2,211461,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211454,0,'organaization',1040187,'all','资源对接','资源对接',212851,2,211454,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211455,0,'organaization',1040187,'all','技术转移','技术转移',212852,2,211455,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211456,0,'organaization',1040187,'all','测试工程','测试工程',212853,2,211456,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121266,41960,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121267,42000,'其他需求','EhNamespaces',999954,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121268,42010,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121269,42020,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121270,42030,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121271,42040,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121272,42050,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121273,42060,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121274,42100,'培训服务','EhNamespaces',999954,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121275,42110,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121276,42120,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121277,42130,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121278,42140,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121279,42150,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121280,42160,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121281,42200,'金融服务','EhNamespaces',999954,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121282,42210,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121283,42220,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121284,42230,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121285,42240,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121286,42250,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121287,42260,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121288,42300,'知识产权','EhNamespaces',999954,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121289,42310,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121290,42320,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121291,42330,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121292,42340,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121293,42350,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121294,42360,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121295,42400,'法律服务','EhNamespaces',999954,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121296,42410,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121297,42420,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121298,42430,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121299,42440,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121300,42450,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121301,42460,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121246,41700,'资源对接','EhNamespaces',999954,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121247,41710,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121248,41720,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121249,41730,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121250,41740,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121251,41750,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121252,41760,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121253,41800,'技术转移','EhNamespaces',999954,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121254,41810,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121255,41820,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121256,41830,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121257,41840,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121258,41850,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121259,41860,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121260,41900,'测试工程','EhNamespaces',999954,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121261,41910,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121262,41920,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121263,41930,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121264,41940,'','EhNamespaces',999954,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (121265,41950,'','EhNamespaces',999954,2);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12209,'会议室预订',0,2,999954,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12210,'场地预订',0,2,999954,0,0);

SET @eh_service_alliance_skip_rule_id = (SELECT MAX(id) FROM `eh_service_alliance_skip_rule`);
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
	VALUES ((@eh_service_alliance_skip_rule_id := @eh_service_alliance_skip_rule_id +1), '999954', 212851);   
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
	VALUES ((@eh_service_alliance_skip_rule_id := @eh_service_alliance_skip_rule_id +1), '999954', 212852);  
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
	VALUES ((@eh_service_alliance_skip_rule_id := @eh_service_alliance_skip_rule_id +1), '999954', 212853);   
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
	VALUES ((@eh_service_alliance_skip_rule_id := @eh_service_alliance_skip_rule_id +1), '999954', 212854);
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
	VALUES ((@eh_service_alliance_skip_rule_id := @eh_service_alliance_skip_rule_id +1), '999954', 212855);   
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
	VALUES ((@eh_service_alliance_skip_rule_id := @eh_service_alliance_skip_rule_id +1), '999954', 212856);  
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
	VALUES ((@eh_service_alliance_skip_rule_id := @eh_service_alliance_skip_rule_id +1), '999954', 212857);   
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
	VALUES ((@eh_service_alliance_skip_rule_id := @eh_service_alliance_skip_rule_id +1), '999954', 212858);    