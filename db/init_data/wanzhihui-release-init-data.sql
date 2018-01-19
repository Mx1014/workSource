INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1980,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999953','the relative path for wanzhihui app agreements',999953,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1981,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999953,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1982,'pmtask.handler-999953','flow','0',999953,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1983,'pay.platform','1','支付类型',999953,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (527,'Android_WanZhiHui',NULL,UTC_TIMESTAMP(),999953);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (528,'iOS_WanZhiHui',NULL,UTC_TIMESTAMP(),999953);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (880,527,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999953);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (881,528,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999953);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999953,'万智汇');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1526,999953,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (954,'sms.default.sign',0,'zh_CN','万智汇','【万智汇】',999953);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18477,0,'四川','SICHUAN','SC','/四川',1,1,NULL,NULL,2,0,999953);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18478,18477,'成都市','CHENGDUSHI','CDS','/四川/成都市',2,2,NULL,'028',2,1,999953);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18479,18478,'锦江区','JINJIANGQU','JJQ','/四川/成都市/锦江区',3,3,NULL,'028',2,0,999953);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060115,UUID(),18478,'成都市',18479,'锦江区','万科大厦','万科大厦','四川省成都市锦江区虎园路','万科大厦是万科中西部区域总部',0,1,2,UTC_TIMESTAMP(),1,192762,192763,UTC_TIMESTAMP(),999953);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092888,240111044332060115,'',104.120174,30.622351,'wm6n1q9upguy');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20225,999953,'COMMUNITY',240111044332060115,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1040325,0,'PM','万科中西部城镇建设发展有限公司','万科是全球排名第一的地产公司，致力于成为城市运营商','/1040325',1,2,'ENTERPRISE',999953,1058376);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1155399,240111044332060115,'organization',1040325,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (27170,'EhOrganizations',1040325,1,10,475337,0,1,UTC_TIMESTAMP(),999953,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (27171,'EhOrganizations',1040325,1,10,475338,0,1,UTC_TIMESTAMP(),999953,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1058376,UUID(),'万科中西部城镇建设发展有限公司','万科中西部城镇建设发展有限公司',1,1,1040325,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192761,1,999953);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192761,UUID(),999953,2,'EhGroups',1058376,'万科中西部城镇建设发展有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192762,UUID(),999953,2,'',0,'万智汇社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192763,UUID(),999953,2,'',0,'万智汇意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256570,UUID(),999953,192762,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (475338,UUID(),'19236002177','左邻官方账号',NULL,1,45,'1','1','zh_CN','827854bfbca26a543419d20c13cd67d3','6ea72285dd2ab4fe20fe2677d9f1e73f06e9cdaf516ae71f296a3489bfd74ef5',UTC_TIMESTAMP(),999953);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (475337,UUID(),'19236002176','刘向东',NULL,1,45,'1','1','zh_CN','d26d544feffd0974230d00f8d98778de','55cae1b764e59f1ba03eb772e30c522c8efddceb95c754313f24eefbc61a20b6',UTC_TIMESTAMP(),999953);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (30389,1040325,'USER',475337,'刘向东',0,'18899776649',UTC_TIMESTAMP(),999953);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (30390,1040325,'USER',475338,'左邻官方账号',0,'12000001802',UTC_TIMESTAMP(),999953);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (446544,475337,0,'18899776649',NULL,3,UTC_TIMESTAMP(),999953);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (446545,475338,0,'12000001802',NULL,3,UTC_TIMESTAMP(),999953);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2181451,1040325,'/1040325','USER',475337,'manager','刘向东',0,'18899776649',3,999953,'ENTERPRISE',0,30389);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2181452,1040325,'/1040325','USER',475338,'manager','左邻官方账号',0,'12000001802',3,999953,'ENTERPRISE',0,30390);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (30158,475337,1040325,'/1040325','ENTERPRISE',3,999953,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (30159,475338,1040325,'/1040325','ENTERPRISE',3,999953,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115329,'EhOrganizations',1040325,'EhUsers',475337,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115330,'EhOrganizations',1040325,'EhUsers',475338,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971121,240111044332060115,'A栋','A栋',0,18899776649,'成都市锦江区花园街万科大厦',1000.0,'104.120174','30.622351','wm6n1q9upguy','商业',NULL,2,0,NULL,1,NOW(),999953,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463860,UUID(),240111044332060115,18478,'成都市',18479,'锦江区','A栋-101','A栋','101','2','0',UTC_TIMESTAMP(),999953,1000.0);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86961,1040325,240111044332060115,239825274387463860,'A栋-101',1);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125076,10000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125077,10100,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125078,10400,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125079,10600,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125080,11000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125081,12200,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125082,20000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125083,20100,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125084,20140,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125085,20150,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125086,20155,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125087,20158,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125088,20170,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125089,20180,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125090,20190,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125091,20191,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125092,20230,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125093,20220,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125094,20240,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125095,20250,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125096,20255,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125097,20258,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125098,20280,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125099,20290,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125100,20291,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125101,20400,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125102,20422,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125103,204011,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125104,204021,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125105,20430,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125106,21100,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125107,21110,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125108,21120,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125109,40050,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125110,40100,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125111,40103,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125112,40105,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125113,40110,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125114,40120,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125115,40130,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125116,40200,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125117,40210,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125118,40220,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125119,40300,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125120,40400,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125121,40410,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125122,40420,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125123,40430,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125124,40440,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125125,40450,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125126,40500,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125127,40700,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125128,40710,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125129,40720,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125130,40800,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125131,40830,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125132,40835,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125133,40810,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125134,40840,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125135,40850,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125136,41000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125137,41010,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125138,41020,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125139,41030,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125140,41040,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125141,41050,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125142,41060,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125143,41100,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125144,41200,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125145,41210,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125146,41220,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125147,41230,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125148,41500,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125149,41600,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125150,41610,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125151,41620,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125152,41630,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125153,41300,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125154,41310,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125155,41330,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125156,41320,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125157,30000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125158,30500,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125159,30550,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125160,31000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125161,32000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125162,34000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125163,30600,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125164,50000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125165,50100,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125166,50300,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125167,50400,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125168,50500,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125169,70000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125170,70300,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125171,70100,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125172,70200,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125173,60000,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125174,60100,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125175,60200,NULL,'EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125176,80000,NULL,'EhNamespaces',999953,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001305,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999953,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001306,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999953,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001307,'',0,1001307,1,'活动管理-默认子分类','/1/1001307',0,2,1,NOW(),0,NULL,999953,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001308,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999953,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001309,'',0,1001309,2,'活动管理二-默认子分类','/2/1001309',0,2,1,NOW(),0,NULL,999953,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001310,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999953,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001311,'',0,1001311,3,'活动管理三-默认子分类','/3/1001311',0,2,1,NOW(),0,NULL,999953,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (426,999953,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (427,999953,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (428,999953,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (429,999953,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (430,999953,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (431,999953,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (432,999953,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (433,999953,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (434,999953,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1040325,240111044332060115);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1002,999953,'AssociationLayout','{"versionCode":"2018010201","layoutName":"AssociationLayout","displayName":"社群","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018010201,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1003,999953,'AssociationLayout','{"versionCode":"2018010201","layoutName":"AssociationLayout","displayName":"社群","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018010201,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1004,999953,'ServiceMarketLayout','{"versionCode":"2018010201","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":1}]}',2018010201,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1005,999953,'ServiceMarketLayout','{"versionCode":"2018010201","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":2},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":1}]}',2018010201,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216850,999953,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRvM056azJPRFE0TnpBNE9URmhNREJqTm1NeVkyRTVNV0UxWWpJMVlXSmlNdw',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216851,999953,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRvM056azJPRFE0TnpBNE9URmhNREJqTm1NeVkyRTVNV0UxWWpJMVlXSmlNdw',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147258,999953,0,0,0,'/home','NavigatorGroup4','产业服务','产业服务','cs://1/image/aW1hZ2UvTVRveE9UUmpOamczWlROak9UazVPREU0TlRoa1lUaGlOR1kyWW1VNVpXRmxaUQ',1,1,33,'{"type":212935,"parentId":212935,"displayType": "list"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147259,999953,0,0,0,'/home','NavigatorGroup4','产业服务','产业服务','cs://1/image/aW1hZ2UvTVRveE9UUmpOamczWlROak9UazVPREU0TlRoa1lUaGlOR1kyWW1VNVpXRmxaUQ',1,1,33,'{"type":212935,"parentId":212935,"displayType": "list"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147260,999953,0,0,0,'/home','NavigatorGroup4','招商服务','招商服务','cs://1/image/aW1hZ2UvTVRveU9ESXdZelU0Tm1Gak1tRm1NemcxTkdFM1l6a3paRGd5Tm1SbVpUaG1OZw',1,1,28,'',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147261,999953,0,0,0,'/home','NavigatorGroup4','招商服务','招商服务','cs://1/image/aW1hZ2UvTVRveU9ESXdZelU0Tm1Gak1tRm1NemcxTkdFM1l6a3paRGd5Tm1SbVpUaG1OZw',1,1,28,'',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147262,999953,0,0,0,'/home','NavigatorGroup4','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRveVpHTTRaR0l4Wm1GbE1qZzBOemRrTm1JeFltUm1ZbU5tTkRjM1pURmxZUQ',1,1,33,'{"type":212936,"parentId":212936,"displayType": "list"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147263,999953,0,0,0,'/home','NavigatorGroup4','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRveVpHTTRaR0l4Wm1GbE1qZzBOemRrTm1JeFltUm1ZbU5tTkRjM1pURmxZUQ',1,1,33,'{"type":212936,"parentId":212936,"displayType": "list"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147264,999953,0,0,0,'/home','NavigatorGroup4','WE创客空间','WE创客空间','cs://1/image/aW1hZ2UvTVRvME5XVmlOVFk0TW1Vek9HRXlNMkprWW1Ga016TmxOelUzWXprNE1UQmhOUQ',1,1,33,'{"type":212937,"parentId":212937,"displayType": "list"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147265,999953,0,0,0,'/home','NavigatorGroup4','WE创客空间','WE创客空间','cs://1/image/aW1hZ2UvTVRvME5XVmlOVFk0TW1Vek9HRXlNMkprWW1Ga016TmxOelUzWXprNE1UQmhOUQ',1,1,33,'{"type":212937,"parentId":212937,"displayType": "list"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147266,999953,0,0,0,'/home','NavigatorGroup4','星商联盟','星商联盟','cs://1/image/aW1hZ2UvTVRwbU9XWXdaR0V5TURFMVlqYzVZakF6T1RnMFpUTmlNRFZrWm1JeFlqWmtaUQ',1,1,33,'{"type":212938,"parentId":212938,"displayType": "list"}',50,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147267,999953,0,0,0,'/home','NavigatorGroup4','星商联盟','星商联盟','cs://1/image/aW1hZ2UvTVRwbU9XWXdaR0V5TURFMVlqYzVZakF6T1RnMFpUTmlNRFZrWm1JeFlqWmtaUQ',1,1,33,'{"type":212938,"parentId":212938,"displayType": "list"}',50,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147224,999953,0,0,0,'/home','NavigatorGroup2','门禁','门禁','cs://1/image/aW1hZ2UvTVRvMU9HWmtOMkZtWTJRell6STVObVEwTkRFeE56SmhOR0k1TVRVMU4yTXhNZw',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147225,999953,0,0,0,'/home','NavigatorGroup2','门禁','门禁','cs://1/image/aW1hZ2UvTVRvMU9HWmtOMkZtWTJRell6STVObVEwTkRFeE56SmhOR0k1TVRVMU4yTXhNZw',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147226,999953,0,0,0,'/home','NavigatorGroup2','停车缴费','停车缴费','cs://1/image/aW1hZ2UvTVRvMVltRTFZbU00WXpFd09UYzJOV0V5WldGbVpqZzNZV1psWm1Zd05qZzRaUQ',1,1,30,'',20,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147227,999953,0,0,0,'/home','NavigatorGroup2','停车缴费','停车缴费','cs://1/image/aW1hZ2UvTVRvMVltRTFZbU00WXpFd09UYzJOV0V5WldGbVpqZzNZV1psWm1Zd05qZzRaUQ',1,1,30,'',20,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147228,999953,0,0,0,'/home','NavigatorGroup2','会议室预订','会议室预订','cs://1/image/aW1hZ2UvTVRvelpqTmpORFkxWlRkbE0yTmlZbVF6TldNMk16VmxPRGxrWmpCaE5XRmhNZw',1,1,49,'{"resourceTypeId":12243,"pageType":0}',30,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147229,999953,0,0,0,'/home','NavigatorGroup2','会议室预订','会议室预订','cs://1/image/aW1hZ2UvTVRvelpqTmpORFkxWlRkbE0yTmlZbVF6TldNMk16VmxPRGxrWmpCaE5XRmhNZw',1,1,49,'{"resourceTypeId":12243,"pageType":0}',30,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147230,999953,0,0,0,'/home','NavigatorGroup2','食堂','食堂','cs://1/image/aW1hZ2UvTVRveE9HTXlNVEEyWTJRelkyVTBObUUxTmpnMU1XVXdOVGRrTlRVd04ySmtNdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',40,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147231,999953,0,0,0,'/home','NavigatorGroup2','食堂','食堂','cs://1/image/aW1hZ2UvTVRveE9HTXlNVEEyWTJRelkyVTBObUUxTmpnMU1XVXdOVGRrTlRVd04ySmtNdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',40,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147232,999953,0,0,0,'/home','NavigatorGroup2','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRveVltUmxObVl3TkdNd05HVXpOamczTUdVeVpEUmxZbU0wWWpBME1UWTNOUQ',1,1,14,'{"url":"${home.url}/property-management/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',50,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147233,999953,0,0,0,'/home','NavigatorGroup2','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRveVltUmxObVl3TkdNd05HVXpOamczTUdVeVpEUmxZbU0wWWpBME1UWTNOUQ',1,1,14,'{"url":"${home.url}/property-management/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',50,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147234,999953,0,0,0,'/home','NavigatorGroup2','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvd1pHSmxPREkzTlRjek9HRmxNekl3WkRneU5UUmhOamsxWmpSa04yVmlOUQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',60,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147235,999953,0,0,0,'/home','NavigatorGroup2','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvd1pHSmxPREkzTlRjek9HRmxNekl3WkRneU5UUmhOamsxWmpSa04yVmlOUQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',60,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147236,999953,0,0,0,'/home','NavigatorGroup2','参观预约','参观预约','cs://1/image/aW1hZ2UvTVRvMU5UWm1ZekZqTkRKaE5tRTFNbU5rT0RGa01qWmlZak00TldJelpXRmlPQQ',1,1,28,'',70,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147237,999953,0,0,0,'/home','NavigatorGroup2','参观预约','参观预约','cs://1/image/aW1hZ2UvTVRvMU5UWm1ZekZqTkRKaE5tRTFNbU5rT0RGa01qWmlZak00TldJelpXRmlPQQ',1,1,28,'',70,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147238,999953,0,0,0,'/home','NavigatorGroup2','全部','全部','cs://1/image/aW1hZ2UvTVRwaFpqWXhZVGxrTXpRMk5XSXhObVExWmpWaE1XWm1Nemt4TlRFeU9XSTVPUQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147239,999953,0,0,0,'/home','NavigatorGroup2','全部','全部','cs://1/image/aW1hZ2UvTVRwaFpqWXhZVGxrTXpRMk5XSXhObVExWmpWaE1XWm1Nemt4TlRFeU9XSTVPUQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147240,999953,0,0,0,'/home','NavigatorGroup2','泊寓','泊寓','cs://1/image/aW1hZ2UvTVRvNFpHRTROakJqTVRreFlUQmlaamxpWW1KbU1qQXdZV1V5T0dRNFpUQmtPUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',80,0,1,0,0,1,'pm_admin',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147241,999953,0,0,0,'/home','NavigatorGroup2','泊寓','泊寓','cs://1/image/aW1hZ2UvTVRvNFpHRTROakJqTVRreFlUQmlaamxpWW1KbU1qQXdZV1V5T0dRNFpUQmtPUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',80,0,1,0,0,1,'park_tourist',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147242,999953,0,0,0,'/home','NavigatorGroup2','万客会','万客会','cs://1/image/aW1hZ2UvTVRvNVl6QmtNemc0T0RBMVkyRTRZVEl6WmpZMllUTXdZVEF4WWprNVlXRXdaUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',90,0,1,0,0,1,'pm_admin',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147243,999953,0,0,0,'/home','NavigatorGroup2','万客会','万客会','cs://1/image/aW1hZ2UvTVRvNVl6QmtNemc0T0RBMVkyRTRZVEl6WmpZMllUTXdZVEF4WWprNVlXRXdaUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',90,0,1,0,0,1,'park_tourist',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147244,999953,0,0,0,'/home','NavigatorGroup2','住这儿','住这儿','cs://1/image/aW1hZ2UvTVRveU1tVXlNRGM0Tm1Zek1tWXhaRE13TW1FeVpqWXpNVFV4T1dSbFl6VmxZZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',100,0,1,0,0,1,'pm_admin',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147245,999953,0,0,0,'/home','NavigatorGroup2','住这儿','住这儿','cs://1/image/aW1hZ2UvTVRveU1tVXlNRGM0Tm1Zek1tWXhaRE13TW1FeVpqWXpNVFV4T1dSbFl6VmxZZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',100,0,1,0,0,1,'park_tourist',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147246,999953,0,0,0,'/home','NavigatorGroup2','分享会','分享会','cs://1/image/aW1hZ2UvTVRwalpXTTNPV1JpT1dabE5EQXhZamszTjJWaFptWXpZbUl6T0RReU56QTFOUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',110,0,1,0,0,1,'pm_admin',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147247,999953,0,0,0,'/home','NavigatorGroup2','分享会','分享会','cs://1/image/aW1hZ2UvTVRwalpXTTNPV1JpT1dabE5EQXhZamszTjJWaFptWXpZbUl6T0RReU56QTFOUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',110,0,1,0,0,1,'park_tourist',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147248,999953,0,0,0,'/home','NavigatorGroup2','便民号码','便民号码','cs://1/image/aW1hZ2UvTVRwak9EZ3hNelUwTnpobU1ETmpOekZsWVRZM05HTmxNVGN6WldVeE5tWmlNQQ',1,1,45,'',120,0,1,0,0,1,'pm_admin',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147249,999953,0,0,0,'/home','NavigatorGroup2','便民号码','便民号码','cs://1/image/aW1hZ2UvTVRwak9EZ3hNelUwTnpobU1ETmpOekZsWVRZM05HTmxNVGN6WldVeE5tWmlNQQ',1,1,45,'',120,0,1,0,0,1,'park_tourist',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147250,999953,0,0,0,'/home','NavigatorGroup2','酒店','酒店','cs://1/image/aW1hZ2UvTVRvMk5qRm1PR00wTXpobVpqZGxaalpsTldWbE5XRm1OakkzTnpKaVl6VTFZUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',130,0,1,0,0,1,'pm_admin',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147251,999953,0,0,0,'/home','NavigatorGroup2','酒店','酒店','cs://1/image/aW1hZ2UvTVRvMk5qRm1PR00wTXpobVpqZGxaalpsTldWbE5XRm1OakkzTnpKaVl6VTFZUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',130,0,1,0,0,1,'park_tourist',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147252,999953,0,0,0,'/home','NavigatorGroup2','出行','出行','cs://1/image/aW1hZ2UvTVRwa09UUXpaV0kwTXpWbU9ERmhNRE13WmpNd1pESTFOak0yWkRWak1EUXhPQQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',140,0,1,0,0,1,'pm_admin',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147253,999953,0,0,0,'/home','NavigatorGroup2','出行','出行','cs://1/image/aW1hZ2UvTVRwa09UUXpaV0kwTXpWbU9ERmhNRE13WmpNd1pESTFOak0yWkRWak1EUXhPQQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',140,0,1,0,0,1,'park_tourist',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147254,999953,0,0,0,'/home','NavigatorGroup2','班车','班车','cs://1/image/aW1hZ2UvTVRvNE5XWmpPRE0xTUdKallqRmpPREJsTmpNM01qQm1Nekl3TVRWbE1EYzRaQQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',150,0,1,0,0,1,'pm_admin',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147255,999953,0,0,0,'/home','NavigatorGroup2','班车','班车','cs://1/image/aW1hZ2UvTVRvNE5XWmpPRE0xTUdKallqRmpPREJsTmpNM01qQm1Nekl3TVRWbE1EYzRaQQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',150,0,1,0,0,1,'park_tourist',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147256,999953,0,0,0,'/home','NavigatorGroup2','咨询服务','咨询服务','cs://1/image/aW1hZ2UvTVRveU5qazRPRGMxTldZNE9XVmxPV1U0TkRVME1UazNOalE1TnpnMFlUZ3lOUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',160,0,1,0,0,1,'pm_admin',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147257,999953,0,0,0,'/home','NavigatorGroup2','咨询服务','咨询服务','cs://1/image/aW1hZ2UvTVRveU5qazRPRGMxTldZNE9XVmxPV1U0TkRVME1UazNOalE1TnpnMFlUZ3lOUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',160,0,1,0,0,1,'park_tourist',0,90,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212936,'community','240111044331055940',0,'物业服务','物业服务',0,2,1,UTC_TIMESTAMP(),999953,2);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212937,'community','240111044331055940',0,'WE创客空间','WE创客空间',0,2,1,UTC_TIMESTAMP(),999953,3);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212938,'community','240111044331055940',0,'星商联盟','星商联盟',0,2,1,UTC_TIMESTAMP(),999953,4);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212935,'community','240111044331055940',0,'产业服务','产业服务',0,2,1,UTC_TIMESTAMP(),999953,1);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211576,0,'organaization',1040325,'all','物业服务','物业服务',212936,2,211576,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211577,0,'organaization',1040325,'all','WE创客空间','WE创客空间',212937,2,211577,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211578,0,'organaization',1040325,'all','星商联盟','星商联盟',212938,2,211578,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211575,0,'organaization',1040325,'all','产业服务','产业服务',212935,2,211575,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125185,41760,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125186,41800,'物业服务','EhNamespaces',999953,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125187,41810,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125188,41820,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125189,41830,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125190,41840,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125191,41850,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125192,41860,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125193,41900,'WE创客空间','EhNamespaces',999953,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125194,41910,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125195,41920,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125196,41930,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125197,41940,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125198,41950,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125199,41960,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125200,42000,'星商联盟','EhNamespaces',999953,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125201,42010,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125202,42020,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125203,42030,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125204,42040,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125205,42050,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125206,42060,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125179,41700,'产业服务','EhNamespaces',999953,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125180,41710,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125181,41720,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125182,41730,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125183,41740,'','EhNamespaces',999953,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125184,41750,'','EhNamespaces',999953,2);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (203978,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999953);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12243,'会议室预订',0,2,999953,0,0);

    
set @eh_launch_pad_items_id = (select max(id) from eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (@eh_launch_pad_items_id:=@eh_launch_pad_items_id+1,999953,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRwaU5qZGhZV1F5TmpReE1tSTJaalE1TlRBNU9UYzRZVEV4TVRVMFpUSTRPUQ',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (@eh_launch_pad_items_id:=@eh_launch_pad_items_id+1,999953,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRwaU5qZGhZV1F5TmpReE1tSTJaalE1TlRBNU9UYzRZVEV4TVRVMFpUSTRPUQ',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',10,0,1,1,0,0,'park_tourist',0,0,'');
