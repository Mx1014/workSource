INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2010,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999963','the relative path for 路福联合广场 app agreements',999963,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2011,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for 路福联合广场',999963,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2012,'pmtask.handler-999963','flow','0',999963,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2013,'pay.platform','1','支付类型',999963,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (553,'Android_LuFu
',NULL,UTC_TIMESTAMP(),999963);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (554,'iOS_LuFu
',NULL,UTC_TIMESTAMP(),999963);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (909,553,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999963);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (910,554,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999963);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999963,'路福联合广场');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1550,999963,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (982,'sms.default.sign',0,'zh_CN','路福联合广场','【路福联合广场】',999963);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18505,0,'广东','GUANGDONG','GD','/广东',1,1,NULL,NULL,2,0,999963);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18506,18505,'广州市','GUANGZHOUSHI','GZS','/广东/广州市',2,2,NULL,'020',2,1,999963);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18507,18506,'番禺区','FANYUQU','FYQ','/广东/广州市/番禺区',3,3,NULL,'020',2,0,999963);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060139,UUID(),18506,'广州市',18507,'番禺区','路福联合广场','路福联合广场','广州市番禺区汉溪大道西233号','路福联合广场属于广州南站片区国际智能化综合体项目，项目涵盖有5A智能写字楼、时尚商场、国际性酒店，为项目用户提供一站式便捷服务。',0,1,2,UTC_TIMESTAMP(),1,192792,192793,UTC_TIMESTAMP(),999963);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092912,240111044332060139,'',113.281423,22.9931828704,'ws0dccc0brzn');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20249,999963,'COMMUNITY',240111044332060139,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1040957,0,'PM','北京安信行物业管理有限公司广州分公司','北京安信行物业管理有限公司（以下简称：安信行）是国家批准的具有一级资质的物业管理服务企业。安信行自成立以来，锐意进取，不断挑战，凭借雄厚的企业实力、高素质的管理人才、全新的经营理念、丰富的行业经验、先进的服务模式，已树立起优良的物业管理形象，在不同类型的物业及设施运作方面，为客户提供着高标准、高质量的物业管理服务，是一家在国内物业管理行业中具有实力和成功管理经验的服务商，被中国质量检验协会首批认证为“全国重质量讲信誉物业管理企业”。','/1040957',1,2,'ENTERPRISE',999963,1058976);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1156005,240111044332060139,'organization',1040957,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37421,'EhOrganizations',1040957,1,10,477116,0,1,UTC_TIMESTAMP(),999963,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37422,'EhOrganizations',1040957,1,10,477117,0,1,UTC_TIMESTAMP(),999963,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1058976,UUID(),'北京安信行物业管理有限公司广州分公司','北京安信行物业管理有限公司广州分公司',1,1,1040957,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192791,1,999963);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192791,UUID(),999963,2,'EhGroups',1058976,'北京安信行物业管理有限公司广州分公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192792,UUID(),999963,2,'',0,'路福联合广场社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192793,UUID(),999963,2,'',0,'路福联合广场意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256594,UUID(),999963,192792,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (477116,UUID(),'19244003946','李玉娟',NULL,1,45,'1','2','zh_CN','98d7b61cd5de708869f9305716250c66','9fce49d60a9343db5017dad7b1f457647cb141f8e1856f44789452220af7c73a',UTC_TIMESTAMP(),999963);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (477117,UUID(),'19244003947','吴孝利',NULL,1,45,'1','1','zh_CN','1362ed3fe229cc9ad4599df1a830331d','67059ece886b6b2e100131615e20a4335c820f57668e03c683af5d41e0addbd7',UTC_TIMESTAMP(),999963);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (30645,1040957,'USER',477116,'李玉娟',0,'13702945369',UTC_TIMESTAMP(),999963);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (30646,1040957,'USER',477117,'吴孝利',0,'13318809050',UTC_TIMESTAMP(),999963);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (448303,477116,0,'13702945369',NULL,3,UTC_TIMESTAMP(),999963);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (448304,477117,0,'13318809050',NULL,3,UTC_TIMESTAMP(),999963);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2181935,1040957,'/1040957','USER',477116,'manager','李玉娟',0,'13702945369',3,999963,'ENTERPRISE',0,30645);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2181936,1040957,'/1040957','USER',477117,'manager','吴孝利',0,'13318809050',3,999963,'ENTERPRISE',0,30646);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (30431,477116,1040957,'/1040957','ENTERPRISE',3,999963,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (30432,477117,1040957,'/1040957','ENTERPRISE',3,999963,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115373,'EhOrganizations',1040957,'EhUsers',477116,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115374,'EhOrganizations',1040957,'EhUsers',477117,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971196,240111044332060139,'A栋','',0,13318809050,'广州市番禺区汉溪大道西233号',NULL,'113.281423','22.993179','ws0dccc0brpp','',NULL,2,0,NULL,1,NOW(),999963,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464120,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2206','A栋','2206','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464152,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2316','A栋','2316','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464184,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2504','A栋','2504','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464121,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2207','A栋','2207','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464153,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2317','A栋','2317','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464185,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2505','A栋','2505','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464122,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2208','A栋','2208','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464154,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2318','A栋','2318','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464186,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2506','A栋','2506','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464123,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2209','A栋','2209','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464155,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2319','A栋','2319','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464187,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2507','A栋','2507','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464124,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2210','A栋','2210','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464156,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2320','A栋','2320','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464188,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2508','A栋','2508','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464125,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2211','A栋','2211','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464157,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2321','A栋','2321','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464189,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2509','A栋','2509','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464126,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2212','A栋','2212','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464158,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2322','A栋','2322','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464190,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2510','A栋','2510','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464127,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2213','A栋','2213','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464159,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2401','A栋','2401','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464191,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2511','A栋','2511','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464128,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2214','A栋','2214','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464160,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2402','A栋','2402','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464192,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2512','A栋','2512','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464129,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2215','A栋','2215','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464161,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2403','A栋','2403','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464193,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2513','A栋','2513','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464130,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2216','A栋','2216','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464162,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2404','A栋','2404','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464194,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2514','A栋','2514','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464131,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2217','A栋','2217','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464163,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2405','A栋','2405','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464132,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2218','A栋','2218','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464164,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2406','A栋','2406','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464133,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2219','A栋','2219','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464165,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2407','A栋','2407','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464134,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2220','A栋','2220','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464166,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2408','A栋','2408','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464135,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2221','A栋','2221','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464167,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2409','A栋','2409','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464136,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2222','A栋','2222','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464168,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2410','A栋','2410','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464137,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2301','A栋','2301','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464169,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2411','A栋','2411','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464138,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2302','A栋','2302','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464170,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2412','A栋','2412','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464139,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2303','A栋','2303','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464171,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2413','A栋','2413','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464140,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2304','A栋','2304','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464172,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2414','A栋','2414','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464141,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2305','A栋','2305','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464173,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2415','A栋','2415','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464142,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2306','A栋','2306','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464174,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2416','A栋','2416','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464143,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2307','A栋','2307','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464175,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2417','A栋','2417','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464144,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2308','A栋','2308','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464176,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2418','A栋','2418','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464145,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2309','A栋','2309','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464177,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2419','A栋','2419','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464146,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2310','A栋','2310','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464178,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2420','A栋','2420','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464147,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2311','A栋','2311','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464179,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2421','A栋','2421','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464148,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2312','A栋','2312','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464180,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2422','A栋','2422','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464149,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2313','A栋','2313','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464181,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2501','A栋','2501','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464118,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2204','A栋','2204','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464150,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2314','A栋','2314','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464182,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2502','A栋','2502','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464119,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2205','A栋','2205','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464151,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2315','A栋','2315','2','0',UTC_TIMESTAMP(),999963,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464183,UUID(),240111044332060139,18506,'广州市',18507,'番禺区','A栋-2503','A栋','2503','2','0',UTC_TIMESTAMP(),999963,NULL);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87237,1040957,240111044332060139,239825274387464120,'A栋-2206',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87269,1040957,240111044332060139,239825274387464152,'A栋-2316',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87301,1040957,240111044332060139,239825274387464184,'A栋-2504',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87238,1040957,240111044332060139,239825274387464121,'A栋-2207',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87270,1040957,240111044332060139,239825274387464153,'A栋-2317',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87302,1040957,240111044332060139,239825274387464185,'A栋-2505',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87239,1040957,240111044332060139,239825274387464122,'A栋-2208',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87271,1040957,240111044332060139,239825274387464154,'A栋-2318',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87303,1040957,240111044332060139,239825274387464186,'A栋-2506',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87240,1040957,240111044332060139,239825274387464123,'A栋-2209',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87272,1040957,240111044332060139,239825274387464155,'A栋-2319',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87304,1040957,240111044332060139,239825274387464187,'A栋-2507',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87241,1040957,240111044332060139,239825274387464124,'A栋-2210',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87273,1040957,240111044332060139,239825274387464156,'A栋-2320',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87305,1040957,240111044332060139,239825274387464188,'A栋-2508',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87242,1040957,240111044332060139,239825274387464125,'A栋-2211',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87274,1040957,240111044332060139,239825274387464157,'A栋-2321',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87306,1040957,240111044332060139,239825274387464189,'A栋-2509',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87243,1040957,240111044332060139,239825274387464126,'A栋-2212',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87275,1040957,240111044332060139,239825274387464158,'A栋-2322',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87307,1040957,240111044332060139,239825274387464190,'A栋-2510',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87244,1040957,240111044332060139,239825274387464127,'A栋-2213',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87276,1040957,240111044332060139,239825274387464159,'A栋-2401',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87308,1040957,240111044332060139,239825274387464191,'A栋-2511',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87245,1040957,240111044332060139,239825274387464128,'A栋-2214',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87277,1040957,240111044332060139,239825274387464160,'A栋-2402',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87309,1040957,240111044332060139,239825274387464192,'A栋-2512',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87246,1040957,240111044332060139,239825274387464129,'A栋-2215',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87278,1040957,240111044332060139,239825274387464161,'A栋-2403',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87310,1040957,240111044332060139,239825274387464193,'A栋-2513',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87247,1040957,240111044332060139,239825274387464130,'A栋-2216',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87279,1040957,240111044332060139,239825274387464162,'A栋-2404',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87311,1040957,240111044332060139,239825274387464194,'A栋-2514',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87248,1040957,240111044332060139,239825274387464131,'A栋-2217',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87280,1040957,240111044332060139,239825274387464163,'A栋-2405',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87249,1040957,240111044332060139,239825274387464132,'A栋-2218',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87281,1040957,240111044332060139,239825274387464164,'A栋-2406',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87250,1040957,240111044332060139,239825274387464133,'A栋-2219',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87282,1040957,240111044332060139,239825274387464165,'A栋-2407',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87251,1040957,240111044332060139,239825274387464134,'A栋-2220',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87283,1040957,240111044332060139,239825274387464166,'A栋-2408',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87252,1040957,240111044332060139,239825274387464135,'A栋-2221',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87284,1040957,240111044332060139,239825274387464167,'A栋-2409',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87253,1040957,240111044332060139,239825274387464136,'A栋-2222',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87285,1040957,240111044332060139,239825274387464168,'A栋-2410',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87254,1040957,240111044332060139,239825274387464137,'A栋-2301',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87286,1040957,240111044332060139,239825274387464169,'A栋-2411',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87255,1040957,240111044332060139,239825274387464138,'A栋-2302',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87287,1040957,240111044332060139,239825274387464170,'A栋-2412',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87256,1040957,240111044332060139,239825274387464139,'A栋-2303',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87288,1040957,240111044332060139,239825274387464171,'A栋-2413',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87257,1040957,240111044332060139,239825274387464140,'A栋-2304',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87289,1040957,240111044332060139,239825274387464172,'A栋-2414',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87258,1040957,240111044332060139,239825274387464141,'A栋-2305',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87290,1040957,240111044332060139,239825274387464173,'A栋-2415',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87259,1040957,240111044332060139,239825274387464142,'A栋-2306',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87291,1040957,240111044332060139,239825274387464174,'A栋-2416',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87260,1040957,240111044332060139,239825274387464143,'A栋-2307',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87292,1040957,240111044332060139,239825274387464175,'A栋-2417',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87261,1040957,240111044332060139,239825274387464144,'A栋-2308',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87293,1040957,240111044332060139,239825274387464176,'A栋-2418',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87262,1040957,240111044332060139,239825274387464145,'A栋-2309',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87294,1040957,240111044332060139,239825274387464177,'A栋-2419',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87263,1040957,240111044332060139,239825274387464146,'A栋-2310',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87295,1040957,240111044332060139,239825274387464178,'A栋-2420',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87264,1040957,240111044332060139,239825274387464147,'A栋-2311',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87296,1040957,240111044332060139,239825274387464179,'A栋-2421',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87265,1040957,240111044332060139,239825274387464148,'A栋-2312',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87297,1040957,240111044332060139,239825274387464180,'A栋-2422',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87266,1040957,240111044332060139,239825274387464149,'A栋-2313',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87298,1040957,240111044332060139,239825274387464181,'A栋-2501',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87235,1040957,240111044332060139,239825274387464118,'A栋-2204',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87267,1040957,240111044332060139,239825274387464150,'A栋-2314',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87299,1040957,240111044332060139,239825274387464182,'A栋-2502',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87236,1040957,240111044332060139,239825274387464119,'A栋-2205',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87268,1040957,240111044332060139,239825274387464151,'A栋-2315',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87300,1040957,240111044332060139,239825274387464183,'A栋-2503',0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128978,10000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128979,10100,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128980,10400,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128981,10200,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128982,10600,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128983,10750,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128984,10751,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128985,10752,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128986,10800,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128987,10850,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128988,10851,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128989,10852,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128990,11000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128991,20000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128992,20100,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128993,20110,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128994,20120,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128995,20130,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128996,20140,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128997,20150,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128998,20155,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128999,20170,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129000,20180,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129001,20190,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129002,20191,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129003,20192,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129004,20400,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129005,20410,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129006,20420,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129007,20800,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129008,20810,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129009,20811,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129010,20812,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129011,20820,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129012,20821,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129013,20822,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129014,20830,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129015,20831,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129016,20840,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129017,20841,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129018,40000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129019,40100,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129020,40110,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129021,40120,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129022,40130,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129023,40200,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129024,40210,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129025,40220,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129026,40300,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129027,40400,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129028,40410,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129029,40420,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129030,40430,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129031,40440,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129032,40450,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129033,40500,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129034,40510,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129035,40520,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129036,40530,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129037,41300,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129038,40750,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129039,40700,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129040,40800,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129041,40810,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129042,40830,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129043,40840,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129044,40850,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129045,41100,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129046,40150,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129047,30000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129048,30500,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129049,31000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129050,32000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129051,33000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129052,34000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129053,35000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129054,30600,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129055,37000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129056,38000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129057,50000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129058,50100,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129059,50110,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129060,50200,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129061,50210,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129062,50220,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129063,50300,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129064,50400,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129065,50500,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129066,60000,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129067,60100,NULL,'EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (129068,60200,NULL,'EhNamespaces',999963,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001341,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999963,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001342,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999963,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001343,'',0,1001343,1,'活动管理-默认子分类','/1/1001343',0,2,1,NOW(),0,NULL,999963,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001344,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999963,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001345,'',0,1001345,2,'活动管理二-默认子分类','/2/1001345',0,2,1,NOW(),0,NULL,999963,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001346,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999963,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001347,'',0,1001347,3,'活动管理三-默认子分类','/3/1001347',0,2,1,NOW(),0,NULL,999963,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (469,999963,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (470,999963,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (471,999963,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (472,999963,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (473,999963,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (474,999963,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (475,999963,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (476,999963,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (477,999963,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1040957,240111044332060139);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1043,999963,'AssociationLayout','{"versionCode":"2018011201","layoutName":"AssociationLayout","displayName":"活动","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018011201,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1044,999963,'AssociationLayout','{"versionCode":"2018011201","layoutName":"AssociationLayout","displayName":"活动","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018011201,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1045,999963,'HomeWuyeLayout','{"versionCode":"2018011201","layoutName":"HomeWuyeLayout","displayName":"物业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2018011201,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1046,999963,'HomeWuyeLayout','{"versionCode":"2018011201","layoutName":"HomeWuyeLayout","displayName":"物业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2018011201,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1047,999963,'HomeResourcebookLayout','{"versionCode":"2018011201","layoutName":"HomeResourcebookLayout","displayName":"资源预订","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018011201,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1048,999963,'HomeResourcebookLayout','{"versionCode":"2018011201","layoutName":"HomeResourcebookLayout","displayName":"资源预订","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018011201,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1049,999963,'ServiceMarketLayout','{"versionCode":"2018011201","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":1,"separatorHeight":16,"columnCount":5},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018011201,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1050,999963,'ServiceMarketLayout','{"versionCode":"2018011201","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":1,"separatorHeight":16,"columnCount":5},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018011201,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216876,999963,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRvMU56Y3hPREF4TWpBeFptUmpNVEZrTVRWaU1XWmxNemMwTURWaE5qZ3dNdw',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216877,999963,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRvMU56Y3hPREF4TWpBeFptUmpNVEZrTVRWaU1XWmxNemMwTURWaE5qZ3dNdw',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149686,999963,0,0,0,'/home','NavigatorGroup4','更多','更多','cs://1/image/aW1hZ2UvTVRveU9ETmxZbUptTnpKbE9UUm1NbUppTWpRME9HTTBNamMwWWpkbU5UQTFNQQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup4"}',130,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149687,999963,0,0,0,'/home','NavigatorGroup4','更多','更多','cs://1/image/aW1hZ2UvTVRveU9ETmxZbUptTnpKbE9UUm1NbUppTWpRME9HTTBNamMwWWpkbU5UQTFNQQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup4"}',130,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149688,999963,0,0,0,'/home','NavigatorGroup4','项目简介','项目简介','cs://1/image/aW1hZ2UvTVRveE1XWmlaRFUyTURZNU16STFZelpqTUdFNE9EQTFZVE13WTJNMU5UY3lOZw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149689,999963,0,0,0,'/home','NavigatorGroup4','项目简介','项目简介','cs://1/image/aW1hZ2UvTVRveE1XWmlaRFUyTURZNU16STFZelpqTUdFNE9EQTFZVE13WTJNMU5UY3lOZw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149690,999963,0,0,0,'/home','NavigatorGroup4','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwaE16QTJNVFptTWpBMVlUWmtZalEyTlRFMU9UUTBNR1ZoWldGbFlqQXlNZw',1,1,33,'{"type":212988,"parentId":212988,"displayType": "list"}',20,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149691,999963,0,0,0,'/home','NavigatorGroup4','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwaE16QTJNVFptTWpBMVlUWmtZalEyTlRFMU9UUTBNR1ZoWldGbFlqQXlNZw',1,1,33,'{"type":212988,"parentId":212988,"displayType": "list"}',20,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149692,999963,0,0,0,'/home','NavigatorGroup4','招租管理','招租管理','cs://1/image/aW1hZ2UvTVRwaFpUVXlZVE15WkRnMFlqUmhZems1TlRCak9XTTBPR1F4WlRVeU1qUXlOdw',1,1,28,'',30,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149693,999963,0,0,0,'/home','NavigatorGroup4','招租管理','招租管理','cs://1/image/aW1hZ2UvTVRwaFpUVXlZVE15WkRnMFlqUmhZems1TlRCak9XTTBPR1F4WlRVeU1qUXlOdw',1,1,28,'',30,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149694,999963,0,0,0,'/home','NavigatorGroup4','企业管理','企业管理','cs://1/image/aW1hZ2UvTVRwa056QmlNemRpTlRoa01tRTJOVEU0TkRReE1Ua3pPRFl5WWpVNVlqazNNUQ',1,1,34,'',40,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149695,999963,0,0,0,'/home','NavigatorGroup4','企业管理','企业管理','cs://1/image/aW1hZ2UvTVRwa056QmlNemRpTlRoa01tRTJOVEU0TkRReE1Ua3pPRFl5WWpVNVlqazNNUQ',1,1,34,'',40,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149696,999963,0,0,0,'/home','NavigatorGroup4','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRvM09HSmhNbUZqWkRBMU5qRXpZakpqWkRCa09USmlaREV4TmpSbE9UUTJOZw',1,1,2,'{"itemLocation":"/home/wuye","layoutName":"HomeWuyeLayout","title":"物业服务","entityTag":"PM"}',50,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149697,999963,0,0,0,'/home','NavigatorGroup4','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRvM09HSmhNbUZqWkRBMU5qRXpZakpqWkRCa09USmlaREV4TmpSbE9UUTJOZw',1,1,2,'{"itemLocation":"/home/wuye","layoutName":"HomeWuyeLayout","title":"物业服务","entityTag":"PM"}',50,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149698,999963,0,0,0,'/home','NavigatorGroup4','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRvelpERTBNRFU1WVRVeU9HWmxaV1kxTVdZME5qSTRNamcyTlRJek5EVXlaUQ',1,1,14,'{"url":"${home.url}/property-management/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',60,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149699,999963,0,0,0,'/home','NavigatorGroup4','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRvelpERTBNRFU1WVRVeU9HWmxaV1kxTVdZME5qSTRNamcyTlRJek5EVXlaUQ',1,1,14,'{"url":"${home.url}/property-management/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',60,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149700,999963,0,0,0,'/home','NavigatorGroup4','新闻管理','新闻管理','cs://1/image/aW1hZ2UvTVRwalpUVXpNV0V3WldRM1lUSXhNakkwTnpZME16aG1ORFk0TTJWa01UazVZdw',1,1,43,'',70,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149701,999963,0,0,0,'/home','NavigatorGroup4','新闻管理','新闻管理','cs://1/image/aW1hZ2UvTVRwalpUVXpNV0V3WldRM1lUSXhNakkwTnpZME16aG1ORFk0TTJWa01UazVZdw',1,1,43,'',70,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149702,999963,0,0,0,'/home','NavigatorGroup4','资源预订','资源预订','cs://1/image/aW1hZ2UvTVRvNE1tSTFZekUxTTJRNVpEQXlaVFF4WmpNellUZ3haakprTWpkak5EbGpaUQ',1,1,60,'{"url":"zl://association/main?layoutName=HomeResourcebookLayout&itemLocation=/home/resourcebook&versionCode=2018011201&displayName=资源预订"}',80,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149703,999963,0,0,0,'/home','NavigatorGroup4','资源预订','资源预订','cs://1/image/aW1hZ2UvTVRvNE1tSTFZekUxTTJRNVpEQXlaVFF4WmpNellUZ3haakprTWpkak5EbGpaUQ',1,1,60,'{"url":"zl://association/main?layoutName=HomeResourcebookLayout&itemLocation=/home/resourcebook&versionCode=2018011201&displayName=资源预订"}',80,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149704,999963,0,0,0,'/home','NavigatorGroup4','考勤管理','考勤管理','cs://1/image/aW1hZ2UvTVRwaU16QmtPRE0zWVRnMllqUTJOall6TWpJM1lUQXdaakZqTldRek5XRXpOZw',1,1,23,'',90,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149705,999963,0,0,0,'/home','NavigatorGroup4','考勤管理','考勤管理','cs://1/image/aW1hZ2UvTVRwaU16QmtPRE0zWVRnMllqUTJOall6TWpJM1lUQXdaakZqTldRek5XRXpOZw',1,1,23,'',90,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149706,999963,0,0,0,'/home','NavigatorGroup4','停车收费','停车收费','cs://1/image/aW1hZ2UvTVRvelpEUmtNVGd4Wm1VME16TTROVFUwT0dRd1pEazBPR0V6TUdNNFptVXpPUQ',1,1,30,'',100,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149707,999963,0,0,0,'/home','NavigatorGroup4','停车收费','停车收费','cs://1/image/aW1hZ2UvTVRvelpEUmtNVGd4Wm1VME16TTROVFUwT0dRd1pEazBPR0V6TUdNNFptVXpPUQ',1,1,30,'',100,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149708,999963,0,0,0,'/home','NavigatorGroup4','加班申请','加班申请','cs://1/image/aW1hZ2UvTVRvME1UWmhNelJtWWpCaE5qQmhObU0wWkdNMlptTm1NbVE1T1dNeVlXTmxZZw',1,1,65,'',110,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149709,999963,0,0,0,'/home','NavigatorGroup4','加班申请','加班申请','cs://1/image/aW1hZ2UvTVRvME1UWmhNelJtWWpCaE5qQmhObU0wWkdNMlptTm1NbVE1T1dNeVlXTmxZZw',1,1,65,'',110,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149710,999963,0,0,0,'/home','NavigatorGroup4','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRveE1USm1aVEJqTTJRNE4yVTJaV1prWkRkak9UYzFPVFUxTTJOaE1EaGlZZw',1,1,45,'',120,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149711,999963,0,0,0,'/home','NavigatorGroup4','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRveE1USm1aVEJqTTJRNE4yVTJaV1prWkRkak9UYzFPVFUxTTJOaE1EaGlZZw',1,1,45,'',120,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149712,999963,0,0,0,'/home','NavigatorGroup5','项目快讯','项目快讯','cs://1/image/aW1hZ2UvTVRwak1HWmpZMk5tTVdZNVlUTTNaVGMxT0RsbFkyWXpZbVUzTXpJeE1HSmhOQQ',1,1,0,'',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149713,999963,0,0,0,'/home','NavigatorGroup5','项目快讯','项目快讯','cs://1/image/aW1hZ2UvTVRwak1HWmpZMk5tTVdZNVlUTTNaVGMxT0RsbFkyWXpZbVUzTXpJeE1HSmhOQQ',1,1,0,'',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149714,999963,0,0,0,'/home/wuye','NavigatorGroup1','设备巡检','设备巡检','cs://1/image/aW1hZ2UvTVRvNVlqVXhNelJtWkdOaE4ySTJaak01WVRZek4yVmpNbUpqTldVd016QXlaQQ',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149715,999963,0,0,0,'/home/wuye','NavigatorGroup1','设备巡检','设备巡检','cs://1/image/aW1hZ2UvTVRvNVlqVXhNelJtWkdOaE4ySTJaak01WVRZek4yVmpNbUpqTldVd016QXlaQQ',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149716,999963,0,0,0,'/home/wuye','NavigatorGroup1','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvMFpEUmhNalptWVdaalpqYzFaRGRrWVRsbU16aGtNR1kxWWpKak9EaGpPQQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149717,999963,0,0,0,'/home/wuye','NavigatorGroup1','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvMFpEUmhNalptWVdaalpqYzFaRGRrWVRsbU16aGtNR1kxWWpKak9EaGpPQQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149718,999963,0,0,0,'/home/resourcebook','TabGroup','会议室','会议室','',2,1,49,'{"resourceTypeId":12262,"pageType":0}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149719,999963,0,0,0,'/home/resourcebook','TabGroup','会议室','会议室','',2,1,49,'{"resourceTypeId":12262,"pageType":0}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149720,999963,0,0,0,'/home/resourcebook','TabGroup','VIP车位','VIP车位','',2,1,49,'{"resourceTypeId":12263,"pageType":0}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149721,999963,0,0,0,'/home/resourcebook','TabGroup','VIP车位','VIP车位','',2,1,49,'{"resourceTypeId":12263,"pageType":0}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149722,999963,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRveU9ETmxZbUptTnpKbE9UUm1NbUppTWpRME9HTTBNamMwWWpkbU5UQTFNQQ',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149723,999963,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRveU9ETmxZbUptTnpKbE9UUm1NbUppTWpRME9HTTBNamMwWWpkbU5UQTFNQQ',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149724,999963,0,0,0,'/association','TabGroup','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRvME1UWmhNelJtWWpCaE5qQmhObU0wWkdNMlptTm1NbVE1T1dNeVlXTmxZZw',1,1,36,'{"privateFlag": 0}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149725,999963,0,0,0,'/association','TabGroup','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRvME1UWmhNelJtWWpCaE5qQmhObU0wWkdNMlptTm1NbVE1T1dNeVlXTmxZZw',1,1,36,'{"privateFlag": 0}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149726,999963,0,0,0,'/association','TabGroup','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwaE16QTJNVFptTWpBMVlUWmtZalEyTlRFMU9UUTBNR1ZoWldGbFlqQXlNZw',1,1,33,'{"type":212989,"parentId":212989,"displayType": "list"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (149727,999963,0,0,0,'/association','TabGroup','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwaE16QTJNVFptTWpBMVlUWmtZalEyTlRFMU9UUTBNR1ZoWldGbFlqQXlNZw',1,1,33,'{"type":212989,"parentId":212989,"displayType": "list"}',30,0,1,1,0,0,'park_tourist',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212988,'community','240111044331055940',0,'服务联盟','服务联盟',0,2,1,UTC_TIMESTAMP(),999963,1);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212989,'community','240111044331055940',0,'服务联盟','服务联盟',0,2,1,UTC_TIMESTAMP(),999963,2);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211637,0,'organaization',NULL,'all','服务联盟','服务联盟',212988,2,211637,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211638,0,'organaization',NULL,'all','服务联盟','服务联盟',212989,2,211638,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128962,41700,'服务联盟','EhNamespaces',999963,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128963,41710,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128964,41720,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128965,41730,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128966,41740,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128967,41750,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128968,41760,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128969,41800,'服务联盟','EhNamespaces',999963,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128970,41810,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128971,41820,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128972,41830,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128973,41840,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128974,41850,'','EhNamespaces',999963,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (128975,41860,'','EhNamespaces',999963,2);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (204024,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999963);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12262,'会议室',0,2,999963,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12263,'VIP车位',0,2,999963,0,0);

SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1),999963,0,0,0,'/home','Default','园区快讯','园区快讯','',2,1,48,'{"categoryId":0,"timeWidgetStyle":"datetime"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1),999963,0,0,0,'/home','Default','园区快讯','园区快讯','',2,1,48,'{"categoryId":0,"timeWidgetStyle":"datetime"}',20,0,1,1,0,0,'park_tourist',0,0,'');


set @eh_categories_id = (select max(id) FROM eh_categories);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'亲子与教育','兴趣/亲子与教育', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'运动与音乐','兴趣/运动与音乐', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'美食与厨艺','兴趣/美食与厨艺', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'美容化妆','兴趣/美容化妆', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'家庭装饰','兴趣/家庭装饰', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'名牌汇','兴趣/名牌汇', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'宠物汇','兴趣/宠物汇', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'旅游摄影','兴趣/旅游摄影', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'老乡群','兴趣/老乡群', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'同事群','兴趣/同事群', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'同学群','兴趣/同学群', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'二手交易','兴趣/二手交易', 0, 2, UTC_TIMESTAMP(), 999963);
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`) VALUES((@eh_categories_id := @eh_categories_id+1), 2, 0,'其他','兴趣/其他', 0, 2, UTC_TIMESTAMP(), 999963);