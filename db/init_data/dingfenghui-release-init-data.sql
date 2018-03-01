INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2056,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999951','the relative path for 鼎峰汇 app agreements',999951,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2057,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for 鼎峰汇',999951,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2058,'pmtask.handler-999951','flow','0',999951,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2059,'pay.platform','1','支付类型',999951,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (597,'Android_DingFengHui',NULL,UTC_TIMESTAMP(),999951);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (598,'iOS_DingFengHui',NULL,UTC_TIMESTAMP(),999951);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (956,597,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999951);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (957,598,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999951);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999951,'鼎峰汇');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1593,999951,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (1025,'sms.default.sign',0,'zh_CN','鼎峰汇','【鼎峰汇】',999951);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18550,0,'广东','GUANGDONG','GD','/广东',1,1,NULL,NULL,2,0,999951);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18551,18550,'东莞市','DONGGUANSHI','DGS','/广东/东莞市',2,2,NULL,'0769',2,0,999951);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18552,18551,'寮步镇','LIAOBUZHEN','LBZ','/广东/东莞市/寮步镇',3,3,NULL,'0769',2,0,999951);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060188,UUID(),18551,'东莞市',18552,'寮步镇','领御豪园','花漫里','富竹山村，香市路和金富路两条主干道交汇处
','    鼎峰花漫里为寮步镇新城中心片区高档住宅校区，项目物业为高层，包括二房二厅、三房两厅、四房两厅。面积从88㎡至167㎡之间，极大地满足了不同置业要求的客户需要。小区提倡人性化居住，注重建筑的每一个细节，户型方正实用，坐北朝南，通风、采光良好，各功能房布局合理，结构紧凑，动静分区。

项目规划采用院式围合、点式分布，满足最大采光、通风、私密性的规则，使入住的每个客户都有最佳的舒适生活。小区配有商业街区和各种娱乐休闲设施，最大程度满足人们日常生活娱乐需要，配备齐全，生活便捷。

项目规划设计始终融入了人性化的思想，打造以院式围合、点式布局，ARTDECO建筑环合景致，
形成独立于新城轴心喧嚣之外的私密社区；独有的建筑院落布局，拉阔100米的超大楼间距，舒缓与邻居对望的距离，在进退有度中，又恰到好处的围合出邻里的亲密，从庭院到家的路，途径无遮挡的阳光与清风，亲悦烂漫花海，让每一次拜谒芳邻，成为一场前所未有的邻里社交文化之旅。',0,1,2,UTC_TIMESTAMP(),1,192858,192859,UTC_TIMESTAMP(),999951);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092961,240111044332060188,'',113.5133,23.03,'ws0ep03z7x5j');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20298,999951,'COMMUNITY',240111044332060188,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1041502,0,'PM','东莞市鸿泰物业管理有限公司','   鼎峰花漫里建筑立面为ARTDECO艺术风格，一期占地3万平米，总建筑面积约11万平米，绿地率达35%，由东莞市鼎峰房地产开发有限公司开发建设。鼎峰花漫里一期地处东莞市寮步镇新城中心区的核心位置，香市路与金富路两条主干道交汇处，周边有广深惠城际快速干线、莞樟路、东部快速干线、松山湖大道、石大路，项目邻近莞深高速、五环路，道路设施和交通网络完善。
','/1041502',1,2,'ENTERPRISE',999951,1059390);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1156445,240111044332060188,'organization',1041502,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37645,'EhOrganizations',1041502,1,10,483751,0,1,UTC_TIMESTAMP(),999951,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1059390,UUID(),'东莞市鸿泰物业管理有限公司','东莞市鸿泰物业管理有限公司',1,1,1041502,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192857,1,999951);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192857,UUID(),999951,2,'EhGroups',1059390,'东莞市鸿泰物业管理有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192858,UUID(),999951,2,'',0,'鼎峰汇社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192859,UUID(),999951,2,'',0,'鼎峰汇意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256637,UUID(),999951,192858,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (483751,UUID(),'19264010550','左邻黄莹',NULL,1,45,'1','2','zh_CN','7589955d715487bafdbc3617c6266722','f9a0794e0b021140033b7f41ccff6b99fd5982a8dc9660bb3cb3847891d86f3f',UTC_TIMESTAMP(),999951);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32005,1041502,'USER',483751,'左邻黄莹',0,'13924261226',UTC_TIMESTAMP(),999951);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (454768,483751,0,'13924261226',NULL,3,UTC_TIMESTAMP(),999951);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184668,1041502,'/1041502','USER',483751,'manager','左邻黄莹',0,'13924261226',3,999951,'ENTERPRISE',0,32005);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31642,483751,1041502,'/1041502','ENTERPRISE',3,999951,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115537,'EhOrganizations',1041502,'EhUsers',483751,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971457,240111044332060188,'1栋','1栋1单元  1栋2单元',0,15913705777,'富竹山村，香市路和金富路两条主干道交汇处',NULL,'113.5135','23.03','ws0ep06p2r0m','',NULL,2,0,NULL,1,NOW(),999951,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971458,240111044332060188,'2栋','2栋1单元  2栋2单元',0,15913705777,'富竹山村，香市路和金富路两条主干道交汇处',NULL,'113.5135','23.031','ws0ep0dj2386','',NULL,2,0,NULL,1,NOW(),999951,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971459,240111044332060188,'3栋','3栋1单元  3栋2单元',0,15913705777,'富竹山村，香市路和金富路两条主干道交汇处',NULL,'113.5136','23.031','ws0ep0dj69df','',NULL,2,0,NULL,1,NOW(),999951,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971460,240111044332060188,'4栋','4栋1单元 4栋1单元',0,15913705777,'富竹山村，香市路和金富路两条主干道交汇处',NULL,'113.5138','23.029','ws0ep061xk8p','',NULL,2,0,NULL,1,NOW(),999951,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465942,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-603','1栋','1-1-603','2','0',UTC_TIMESTAMP(),999951,86.55);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465974,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1403','1栋','1-1-1403','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466006,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2203','1栋','1-1-2203','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466038,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-603','1栋','1-2-603','2','0',UTC_TIMESTAMP(),999951,86.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466070,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1403','1栋','1-2-1403','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466102,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2203','1栋','1-2-2203','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466134,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-315','2栋','2-315','2','0',UTC_TIMESTAMP(),999951,92.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466166,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-515','2栋','2-515','2','0',UTC_TIMESTAMP(),999951,92.42);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466198,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-715','2栋','2-715','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466230,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-915','2栋','2-915','2','0',UTC_TIMESTAMP(),999951,72.53);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466262,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1115','2栋','2-1115','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466294,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1315','2栋','2-1315','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466326,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1515','2栋','2-1515','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466358,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1201','3栋','3-1-1201','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466390,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2203','3栋','3-1-2203','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466422,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-602','3栋','3-2-602','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466454,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1701','3栋','3-2-1701','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466486,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2703','3栋','3-2-2703','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466518,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1002','4栋','4-1-1002','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466550,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2101','4栋','4-1-2101','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466582,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-401','4栋','4-2-401','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466614,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1403','4栋','4-2-1403','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466646,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2502','4栋','4-2-2502','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465943,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-604','1栋','1-1-604','2','0',UTC_TIMESTAMP(),999951,87.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465975,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1404','1栋','1-1-1404','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466007,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2204','1栋','1-1-2204','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466039,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-604','1栋','1-2-604','2','0',UTC_TIMESTAMP(),999951,85.36);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466071,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1404','1栋','1-2-1404','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466103,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2204','1栋','1-2-2204','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466135,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-316','2栋','2-316','2','0',UTC_TIMESTAMP(),999951,93.11);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466167,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-516','2栋','2-516','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466199,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-716','2栋','2-716','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466231,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-916','2栋','2-916','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466263,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1116','2栋','2-1116','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466295,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1316','2栋','2-1316','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466327,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1516','2栋','2-1516','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466359,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1202','3栋','3-1-1202','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466391,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2301','3栋','3-1-2301','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466423,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-603','3栋','3-2-603','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466455,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1702','3栋','3-2-1702','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466487,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2801','3栋','3-2-2801','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466519,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1003','4栋','4-1-1003','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466551,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2102','4栋','4-1-2102','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466583,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-402','4栋','4-2-402','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466615,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1501','4栋','4-2-1501','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466647,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2503','4栋','4-2-2503','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465944,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-701','1栋','1-1-701','2','0',UTC_TIMESTAMP(),999951,88.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465976,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1501','1栋','1-1-1501','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466008,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2301','1栋','1-1-2301','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466040,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-701','1栋','1-2-701','2','0',UTC_TIMESTAMP(),999951,116.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466072,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1501','1栋','1-2-1501','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466104,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2301','1栋','1-2-2301','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466136,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-401','2栋','2-401','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466168,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-601','2栋','2-601','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466200,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-801','2栋','2-801','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466232,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1001','2栋','2-1001','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466264,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1201','2栋','2-1201','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466296,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1401','2栋','2-1401','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466328,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-201','3栋','3-1-201','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466360,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1203','3栋','3-1-1203','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466392,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2302','3栋','3-1-2302','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466424,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-701','3栋','3-2-701','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466456,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1703','3栋','3-2-1703','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466488,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2802','3栋','3-2-2802','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466520,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1101','4栋','4-1-1101','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466552,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2103','4栋','4-1-2103','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466584,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-403','4栋','4-2-403','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466616,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1502','4栋','4-2-1502','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466648,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2601','4栋','4-2-2601','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465945,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-702','1栋','1-1-702','2','0',UTC_TIMESTAMP(),999951,117.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465977,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1502','1栋','1-1-1502','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466009,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2302','1栋','1-1-2302','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466041,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-702','1栋','1-2-702','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466073,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1502','1栋','1-2-1502','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466105,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2302','1栋','1-2-2302','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466137,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-402','2栋','2-402','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466169,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-602','2栋','2-602','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466201,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-802','2栋','2-802','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466233,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1002','2栋','2-1002','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466265,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1202','2栋','2-1202','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466297,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1402','2栋','2-1402','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466329,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-202','3栋','3-1-202','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466361,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1301','3栋','3-1-1301','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466393,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2303','3栋','3-1-2303','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466425,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-702','3栋','3-2-702','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466457,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1801','3栋','3-2-1801','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466489,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2803','3栋','3-2-2803','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466521,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1102','4栋','4-1-1102','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466553,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2201','4栋','4-1-2201','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466585,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-501','4栋','4-2-501','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466617,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1503','4栋','4-2-1503','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466649,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2602','4栋','4-2-2602','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465946,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-703','1栋','1-1-703','2','0',UTC_TIMESTAMP(),999951,86.55);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465978,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1503','1栋','1-1-1503','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466010,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2303','1栋','1-1-2303','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466042,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-703','1栋','1-2-703','2','0',UTC_TIMESTAMP(),999951,86.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466074,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1503','1栋','1-2-1503','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466106,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2303','1栋','1-2-2303','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466138,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-403','2栋','2-403','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466170,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-603','2栋','2-603','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466202,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-803','2栋','2-803','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466234,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1003','2栋','2-1003','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466266,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1203','2栋','2-1203','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466298,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1403','2栋','2-1403','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466330,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-203','3栋','3-1-203','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466362,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1302','3栋','3-1-1302','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466394,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2401','3栋','3-1-2401','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466426,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-703','3栋','3-2-703','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466458,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1802','3栋','3-2-1802','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466490,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-101','4栋','4-1-101','2','0',UTC_TIMESTAMP(),999951,118.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466522,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1103','4栋','4-1-1103','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466554,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2202','4栋','4-1-2202','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466586,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-502','4栋','4-2-502','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466618,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1601','4栋','4-2-1601','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466650,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2603','4栋','4-2-2603','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465947,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-704','1栋','1-1-704','2','0',UTC_TIMESTAMP(),999951,87.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465979,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1504','1栋','1-1-1504','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466011,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2304','1栋','1-1-2304','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466043,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-704','1栋','1-2-704','2','0',UTC_TIMESTAMP(),999951,85.36);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466075,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1504','1栋','1-2-1504','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466107,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2304','1栋','1-2-2304','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466139,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-404','2栋','2-404','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466171,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-604','2栋','2-604','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466203,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-804','2栋','2-804','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466235,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1004','2栋','2-1004','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466267,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1204','2栋','2-1204','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466299,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1404','2栋','2-1404','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466331,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-301','3栋','3-1-301','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466363,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1303','3栋','3-1-1303','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466395,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2402','3栋','3-1-2402','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466427,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-801','3栋','3-2-801','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466459,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1803','3栋','3-2-1803','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466491,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-102','4栋','4-1-102','2','0',UTC_TIMESTAMP(),999951,118.16);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466523,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1201','4栋','4-1-1201','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466555,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2203','4栋','4-1-2203','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466587,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-503','4栋','4-2-503','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466619,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1602','4栋','4-2-1602','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466651,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2701','4栋','4-2-2701','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465948,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-801','1栋','1-1-801','2','0',UTC_TIMESTAMP(),999951,88.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465980,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1601','1栋','1-1-1601','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466012,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2401','1栋','1-1-2401','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466044,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-801','1栋','1-2-801','2','0',UTC_TIMESTAMP(),999951,116.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466076,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1601','1栋','1-2-1601','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466108,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2401','1栋','1-2-2401','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466140,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-405','2栋','2-405','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466172,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-605','2栋','2-605','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466204,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-805','2栋','2-805','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466236,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1005','2栋','2-1005','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466268,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1205','2栋','2-1205','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466300,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1405','2栋','2-1405','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466332,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-302','3栋','3-1-302','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466364,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1401','3栋','3-1-1401','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466396,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2403','3栋','3-1-2403','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466428,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-802','3栋','3-2-802','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466460,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1901','3栋','3-2-1901','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466492,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-201','4栋','4-1-201','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466524,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1202','4栋','4-1-1202','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466556,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2301','4栋','4-1-2301','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466588,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-601','4栋','4-2-601','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466620,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1603','4栋','4-2-1603','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466652,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2702','4栋','4-2-2702','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465949,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-802','1栋','1-1-802','2','0',UTC_TIMESTAMP(),999951,117.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465981,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1602','1栋','1-1-1602','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466013,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2402','1栋','1-1-2402','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466045,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-802','1栋','1-2-802','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466077,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1602','1栋','1-2-1602','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466109,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2402','1栋','1-2-2402','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466141,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-406','2栋','2-406','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466173,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-606','2栋','2-606','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466205,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-806','2栋','2-806','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466237,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1006','2栋','2-1006','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466269,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1206','2栋','2-1206','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466301,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1406','2栋','2-1406','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466333,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-303','3栋','3-1-303','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466365,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1402','3栋','3-1-1402','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466397,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2501','3栋','3-1-2501','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466429,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-803','3栋','3-2-803','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466461,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1902','3栋','3-2-1902','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466493,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-202','4栋','4-1-202','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466525,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1203','4栋','4-1-1203','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466557,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2302','4栋','4-1-2302','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466589,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-602','4栋','4-2-602','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466621,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1701','4栋','4-2-1701','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466653,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2703','4栋','4-2-2703','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465950,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-803','1栋','1-1-803','2','0',UTC_TIMESTAMP(),999951,86.55);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465982,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1603','1栋','1-1-1603','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466014,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2403','1栋','1-1-2403','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466046,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-803','1栋','1-2-803','2','0',UTC_TIMESTAMP(),999951,86.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466078,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1603','1栋','1-2-1603','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466110,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2403','1栋','1-2-2403','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466142,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-407','2栋','2-407','2','0',UTC_TIMESTAMP(),999951,89.06);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466174,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-607','2栋','2-607','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466206,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-807','2栋','2-807','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466238,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1007','2栋','2-1007','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466270,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1207','2栋','2-1207','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466302,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1407','2栋','2-1407','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466334,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-401','3栋','3-1-401','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466366,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1403','3栋','3-1-1403','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466398,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2502','3栋','3-1-2502','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466430,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-901','3栋','3-2-901','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466462,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1903','3栋','3-2-1903','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466494,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-203','4栋','4-1-203','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466526,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1301','4栋','4-1-1301','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466558,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2303','4栋','4-1-2303','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466590,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-603','4栋','4-2-603','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466622,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1702','4栋','4-2-1702','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466654,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2801','4栋','4-2-2801','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465951,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-804','1栋','1-1-804','2','0',UTC_TIMESTAMP(),999951,87.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465983,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1604','1栋','1-1-1604','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466015,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2404','1栋','1-1-2404','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466047,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-804','1栋','1-2-804','2','0',UTC_TIMESTAMP(),999951,85.36);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466079,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1604','1栋','1-2-1604','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466111,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2404','1栋','1-2-2404','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466143,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-408','2栋','2-408','2','0',UTC_TIMESTAMP(),999951,89.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466175,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-608','2栋','2-608','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466207,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-808','2栋','2-808','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466239,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1008','2栋','2-1008','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466271,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1208','2栋','2-1208','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466303,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1408','2栋','2-1408','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466335,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-402','3栋','3-1-402','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466367,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1501','3栋','3-1-1501','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466399,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2503','3栋','3-1-2503','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466431,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-902','3栋','3-2-902','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466463,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2001','3栋','3-2-2001','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466495,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-301','4栋','4-1-301','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466527,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1302','4栋','4-1-1302','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466559,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2401','4栋','4-1-2401','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466591,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-701','4栋','4-2-701','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466623,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1703','4栋','4-2-1703','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466655,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2802','4栋','4-2-2802','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465952,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-901','1栋','1-1-901','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465984,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1701','1栋','1-1-1701','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466016,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2501','1栋','1-1-2501','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466048,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-901','1栋','1-2-901','2','0',UTC_TIMESTAMP(),999951,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466080,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1701','1栋','1-2-1701','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466112,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2501','1栋','1-2-2501','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466144,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-409','2栋','2-409','2','0',UTC_TIMESTAMP(),999951,81.05);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466176,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-609','2栋','2-609','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466208,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-809','2栋','2-809','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466240,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1009','2栋','2-1009','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466272,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1209','2栋','2-1209','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466304,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1409','2栋','2-1409','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466336,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-403','3栋','3-1-403','2','0',UTC_TIMESTAMP(),999951,167.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466368,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1502','3栋','3-1-1502','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466400,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2601','3栋','3-1-2601','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466432,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-903','3栋','3-2-903','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466464,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2002','3栋','3-2-2002','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466496,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-302','4栋','4-1-302','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466528,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1303','4栋','4-1-1303','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466560,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2402','4栋','4-1-2402','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466592,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-702','4栋','4-2-702','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466624,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1801','4栋','4-2-1801','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466656,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2803','4栋','4-2-2803','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465953,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-902','1栋','1-1-902','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465985,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1702','1栋','1-1-1702','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466017,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2502','1栋','1-1-2502','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466049,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-902','1栋','1-2-902','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466081,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1702','1栋','1-2-1702','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466113,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2502','1栋','1-2-2502','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466145,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-410','2栋','2-410','2','0',UTC_TIMESTAMP(),999951,89.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466177,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-610','2栋','2-610','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466209,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-810','2栋','2-810','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466241,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1010','2栋','2-1010','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466273,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1210','2栋','2-1210','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466305,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1410','2栋','2-1410','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466337,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-501','3栋','3-1-501','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466369,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1503','3栋','3-1-1503','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466401,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2602','3栋','3-1-2602','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466433,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1001','3栋','3-2-1001','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466465,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2003','3栋','3-2-2003','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466497,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-303','4栋','4-1-303','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466529,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1401','4栋','4-1-1401','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466561,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2403','4栋','4-1-2403','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466593,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-703','4栋','4-2-703','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466625,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1802','4栋','4-2-1802','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465954,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-903','1栋','1-1-903','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465986,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1703','1栋','1-1-1703','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466018,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2503','1栋','1-1-2503','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466050,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-903','1栋','1-2-903','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466082,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1703','1栋','1-2-1703','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466114,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2503','1栋','1-2-2503','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466146,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-411','2栋','2-411','2','0',UTC_TIMESTAMP(),999951,93.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466178,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-611','2栋','2-611','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466210,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-811','2栋','2-811','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466242,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1011','2栋','2-1011','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466274,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1211','2栋','2-1211','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466306,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1411','2栋','2-1411','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466338,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-502','3栋','3-1-502','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466370,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1601','3栋','3-1-1601','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466402,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2603','3栋','3-1-2603','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466434,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1002','3栋','3-2-1002','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466466,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2101','3栋','3-2-2101','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466498,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-401','4栋','4-1-401','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466530,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1402','4栋','4-1-1402','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466562,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2501','4栋','4-1-2501','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466594,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-801','4栋','4-2-801','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466626,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1803','4栋','4-2-1803','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465955,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-904','1栋','1-1-904','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465987,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1704','1栋','1-1-1704','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466019,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2504','1栋','1-1-2504','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466051,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-904','1栋','1-2-904','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466083,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1704','1栋','1-2-1704','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466115,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2504','1栋','1-2-2504','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466147,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-412','2栋','2-412','2','0',UTC_TIMESTAMP(),999951,89.24);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466179,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-612','2栋','2-612','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466211,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-812','2栋','2-812','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466243,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1012','2栋','2-1012','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466275,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1212','2栋','2-1212','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466307,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1412','2栋','2-1412','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466339,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-503','3栋','3-1-503','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466371,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1602','3栋','3-1-1602','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466403,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2701','3栋','3-1-2701','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466435,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1003','3栋','3-2-1003','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466467,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2102','3栋','3-2-2102','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466499,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-402','4栋','4-1-402','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466531,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1403','4栋','4-1-1403','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466563,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2502','4栋','4-1-2502','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466595,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-802','4栋','4-2-802','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466627,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1901','4栋','4-2-1901','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465956,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1001','1栋','1-1-1001','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465988,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1801','1栋','1-1-1801','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466020,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2601','1栋','1-1-2601','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466052,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1001','1栋','1-2-1001','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466084,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1801','1栋','1-2-1801','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466116,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2601','1栋','1-2-2601','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466148,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-413','2栋','2-413','2','0',UTC_TIMESTAMP(),999951,76.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466180,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-613','2栋','2-613','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466212,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-813','2栋','2-813','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466244,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1013','2栋','2-1013','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466276,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1213','2栋','2-1213','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466308,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1413','2栋','2-1413','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466340,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-601','3栋','3-1-601','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466372,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1603','3栋','3-1-1603','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466404,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2702','3栋','3-1-2702','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466436,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1101','3栋','3-2-1101','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466468,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2103','3栋','3-2-2103','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466500,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-403/404','4栋','4-1-403/404','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466532,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1501','4栋','4-1-1501','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466564,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2503','4栋','4-1-2503','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466596,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-803','4栋','4-2-803','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466628,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1902','4栋','4-2-1902','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465957,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1002','1栋','1-1-1002','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465989,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1802','1栋','1-1-1802','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466021,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2602','1栋','1-1-2602','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466053,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1002','1栋','1-2-1002','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466085,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1802','1栋','1-2-1802','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466117,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2602','1栋','1-2-2602','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466149,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-414','2栋','2-414','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466181,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-614','2栋','2-614','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466213,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-814','2栋','2-814','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466245,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1014','2栋','2-1014','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466277,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1214','2栋','2-1214','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466309,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1414','2栋','2-1414','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466341,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-602','3栋','3-1-602','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466373,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1701','3栋','3-1-1701','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466405,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2703','3栋','3-1-2703','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466437,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1102','3栋','3-2-1102','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466469,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2201','3栋','3-2-2201','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466501,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-501','4栋','4-1-501','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466533,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1502','4栋','4-1-1502','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466565,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2601','4栋','4-1-2601','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466597,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-901','4栋','4-2-901','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466629,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1903','4栋','4-2-1903','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465958,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1003','1栋','1-1-1003','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465990,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1803','1栋','1-1-1803','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466022,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2603','1栋','1-1-2603','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466054,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1003','1栋','1-2-1003','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466086,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1803','1栋','1-2-1803','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466118,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2603','1栋','1-2-2603','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466150,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-415','2栋','2-415','2','0',UTC_TIMESTAMP(),999951,92.42);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466182,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-615','2栋','2-615','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466214,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-815','2栋','2-815','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466246,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1015','2栋','2-1015','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466278,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1215','2栋','2-1215','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466310,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1415','2栋','2-1415','2','0',UTC_TIMESTAMP(),999951,92.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466342,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-603','3栋','3-1-603','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466374,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1702','3栋','3-1-1702','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466406,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2801','3栋','3-1-2801','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466438,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1103','3栋','3-2-1103','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466470,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2202','3栋','3-2-2202','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466502,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-502','4栋','4-1-502','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466534,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1503','4栋','4-1-1503','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466566,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2602','4栋','4-1-2602','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466598,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-902','4栋','4-2-902','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466630,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2001','4栋','4-2-2001','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465959,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1004','1栋','1-1-1004','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465991,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1804','1栋','1-1-1804','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466023,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2604','1栋','1-1-2604','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466055,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1004','1栋','1-2-1004','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466087,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1804','1栋','1-2-1804','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466119,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2604','1栋','1-2-2604','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466151,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-416','2栋','2-416','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466183,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-616','2栋','2-616','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466215,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-816','2栋','2-816','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466247,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1016','2栋','2-1016','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466279,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1216','2栋','2-1216','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466311,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1416','2栋','2-1416','2','0',UTC_TIMESTAMP(),999951,93.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466343,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-701','3栋','3-1-701','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466375,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1703','3栋','3-1-1703','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466407,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2802','3栋','3-1-2802','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466439,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1201','3栋','3-2-1201','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466471,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2203','3栋','3-2-2203','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466503,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-503/504','4栋','4-1-503/504','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466535,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1601','4栋','4-1-1601','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466567,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2603','4栋','4-1-2603','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466599,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-903','4栋','4-2-903','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466631,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2002','4栋','4-2-2002','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465928,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-301','1栋','1-1-301','2','0',UTC_TIMESTAMP(),999951,88.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465960,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1101','1栋','1-1-1101','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465992,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1901','1栋','1-1-1901','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466024,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-301','1栋','1-2-301','2','0',UTC_TIMESTAMP(),999951,116.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466056,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1101','1栋','1-2-1101','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466088,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1901','1栋','1-2-1901','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466120,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-301','2栋','2-301','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466152,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-501','2栋','2-501','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466184,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-701','2栋','2-701','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466216,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-901','2栋','2-901','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466248,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1101','2栋','2-1101','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466280,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1301','2栋','2-1301','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466312,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1501','2栋','2-1501','2','0',UTC_TIMESTAMP(),999951,93.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466344,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-702','3栋','3-1-702','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466376,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1801','3栋','3-1-1801','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466408,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2803','3栋','3-1-2803','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466440,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1202','3栋','3-2-1202','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466472,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2301','3栋','3-2-2301','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466504,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-601','4栋','4-1-601','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466536,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1602','4栋','4-1-1602','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466568,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2701','4栋','4-1-2701','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466600,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1001','4栋','4-2-1001','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466632,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2003','4栋','4-2-2003','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465929,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-302','1栋','1-1-302','2','0',UTC_TIMESTAMP(),999951,117.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465961,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1102','1栋','1-1-1102','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465993,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1902','1栋','1-1-1902','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466025,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-302','1栋','1-2-302','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466057,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1102','1栋','1-2-1102','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466089,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1902','1栋','1-2-1902','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466121,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-302','2栋','2-302','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466153,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-502','2栋','2-502','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466185,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-702','2栋','2-702','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466217,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-902','2栋','2-902','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466249,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1102','2栋','2-1102','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466281,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1302','2栋','2-1302','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466313,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1502','2栋','2-1502','2','0',UTC_TIMESTAMP(),999951,93.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466345,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-703','3栋','3-1-703','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466377,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1802','3栋','3-1-1802','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466409,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-201','3栋','3-2-201','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466441,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1203','3栋','3-2-1203','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466473,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2302','3栋','3-2-2302','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466505,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-602','4栋','4-1-602','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466537,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1603','4栋','4-1-1603','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466569,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2702','4栋','4-1-2702','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466601,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1002','4栋','4-2-1002','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466633,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2101','4栋','4-2-2101','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465930,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-303','1栋','1-1-303','2','0',UTC_TIMESTAMP(),999951,86.55);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465962,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1103','1栋','1-1-1103','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465994,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1903','1栋','1-1-1903','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466026,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-303','1栋','1-2-303','2','0',UTC_TIMESTAMP(),999951,86.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466058,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1103','1栋','1-2-1103','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466090,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1903','1栋','1-2-1903','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466122,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-303','2栋','2-303','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466154,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-503','2栋','2-503','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466186,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-703','2栋','2-703','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466218,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-903','2栋','2-903','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466250,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1103','2栋','2-1103','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466282,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1303','2栋','2-1303','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466314,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1503','2栋','2-1503','2','0',UTC_TIMESTAMP(),999951,89.31);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466346,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-801','3栋','3-1-801','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466378,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1803','3栋','3-1-1803','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466410,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-202','3栋','3-2-202','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466442,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1301','3栋','3-2-1301','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466474,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2303','3栋','3-2-2303','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466506,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-603/604','4栋','4-1-603/604','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466538,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1701','4栋','4-1-1701','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466570,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2703','4栋','4-1-2703','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466602,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1003','4栋','4-2-1003','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466634,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2102','4栋','4-2-2102','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465931,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-304','1栋','1-1-304','2','0',UTC_TIMESTAMP(),999951,87.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465963,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1104','1栋','1-1-1104','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465995,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1904','1栋','1-1-1904','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466027,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-304','1栋','1-2-304','2','0',UTC_TIMESTAMP(),999951,85.36);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466059,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1104','1栋','1-2-1104','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466091,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1904','1栋','1-2-1904','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466123,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-304','2栋','2-304','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466155,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-504','2栋','2-504','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466187,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-704','2栋','2-704','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466219,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-904','2栋','2-904','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466251,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1104','2栋','2-1104','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466283,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1304','2栋','2-1304','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466315,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1504','2栋','2-1504','2','0',UTC_TIMESTAMP(),999951,88.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466347,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-802','3栋','3-1-802','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466379,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1901','3栋','3-1-1901','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466411,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-203','3栋','3-2-203','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466443,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1302','3栋','3-2-1302','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466475,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2401','3栋','3-2-2401','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466507,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-701','4栋','4-1-701','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466539,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1702','4栋','4-1-1702','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466571,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2801','4栋','4-1-2801','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466603,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1101','4栋','4-2-1101','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466635,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2103','4栋','4-2-2103','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465932,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-401','1栋','1-1-401','2','0',UTC_TIMESTAMP(),999951,1882.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465964,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1201','1栋','1-1-1201','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465996,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2001','1栋','1-1-2001','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466028,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-401','1栋','1-2-401','2','0',UTC_TIMESTAMP(),999951,116.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466060,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1201','1栋','1-2-1201','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466092,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2001','1栋','1-2-2001','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466124,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-305','2栋','2-305','2','0',UTC_TIMESTAMP(),999951,89.24);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466156,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-505','2栋','2-505','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466188,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-705','2栋','2-705','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466220,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-905','2栋','2-905','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466252,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1105','2栋','2-1105','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466284,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1305','2栋','2-1305','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466316,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1505','2栋','2-1505','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466348,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-803','3栋','3-1-803','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466380,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1902','3栋','3-1-1902','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466412,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-301','3栋','3-2-301','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466444,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1303','3栋','3-2-1303','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466476,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2402','3栋','3-2-2402','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466508,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-702','4栋','4-1-702','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466540,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1703','4栋','4-1-1703','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466572,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2802','4栋','4-1-2802','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466604,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1102','4栋','4-2-1102','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466636,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2201','4栋','4-2-2201','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465933,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-402','1栋','1-1-402','2','0',UTC_TIMESTAMP(),999951,117.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465965,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1202','1栋','1-1-1202','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465997,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2002','1栋','1-1-2002','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466029,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-402','1栋','1-2-402','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466061,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1202','1栋','1-2-1202','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466093,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2002','1栋','1-2-2002','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466125,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-306','2栋','2-306','2','0',UTC_TIMESTAMP(),999951,89.24);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466157,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-506','2栋','2-506','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466189,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-706','2栋','2-706','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466221,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-906','2栋','2-906','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466253,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1106','2栋','2-1106','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466285,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1306','2栋','2-1306','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466317,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1506','2栋','2-1506','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466349,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-901','3栋','3-1-901','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466381,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1903','3栋','3-1-1903','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466413,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-302','3栋','3-2-302','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466445,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1401','3栋','3-2-1401','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466477,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2403','3栋','3-2-2403','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466509,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-703','4栋','4-1-703','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466541,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1801','4栋','4-1-1801','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466573,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2803','4栋','4-1-2803','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466605,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1103','4栋','4-2-1103','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466637,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2202','4栋','4-2-2202','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465934,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-403','1栋','1-1-403','2','0',UTC_TIMESTAMP(),999951,86.55);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465966,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1203','1栋','1-1-1203','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465998,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2003','1栋','1-1-2003','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466030,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-403','1栋','1-2-403','2','0',UTC_TIMESTAMP(),999951,86.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466062,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1203','1栋','1-2-1203','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466094,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2003','1栋','1-2-2003','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466126,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-307','2栋','2-307','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466158,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-507','2栋','2-507','2','0',UTC_TIMESTAMP(),999951,89.06);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466190,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-707','2栋','2-707','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466222,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-907','2栋','2-907','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466254,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1107','2栋','2-1107','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466286,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1307','2栋','2-1307','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466318,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1507','2栋','2-1507','2','0',UTC_TIMESTAMP(),999951,88.91);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466350,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-902','3栋','3-1-902','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466382,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2001','3栋','3-1-2001','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466414,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-303','3栋','3-2-303','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466446,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1402','3栋','3-2-1402','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466478,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2501','3栋','3-2-2501','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466510,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-704','4栋','4-1-704','2','0',UTC_TIMESTAMP(),999951,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466542,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1802','4栋','4-1-1802','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466574,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-101','4栋','4-2-101','2','0',UTC_TIMESTAMP(),999951,117.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466606,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1201','4栋','4-2-1201','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466638,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2203','4栋','4-2-2203','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465935,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-404','1栋','1-1-404','2','0',UTC_TIMESTAMP(),999951,87.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465967,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1204','1栋','1-1-1204','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465999,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2004','1栋','1-1-2004','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466031,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-404','1栋','1-2-404','2','0',UTC_TIMESTAMP(),999951,85.36);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466063,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1204','1栋','1-2-1204','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466095,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2004','1栋','1-2-2004','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466127,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-308','2栋','2-308','2','0',UTC_TIMESTAMP(),999951,89.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466159,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-508','2栋','2-508','2','0',UTC_TIMESTAMP(),999951,89.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466191,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-708','2栋','2-708','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466223,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-908','2栋','2-908','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466255,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1108','2栋','2-1108','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466287,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1308','2栋','2-1308','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466319,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1508','2栋','2-1508','2','0',UTC_TIMESTAMP(),999951,89.48);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466351,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-903','3栋','3-1-903','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466383,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2002','3栋','3-1-2002','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466415,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-401','3栋','3-2-401','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466447,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1403','3栋','3-2-1403','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466479,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2502','3栋','3-2-2502','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466511,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-801','4栋','4-1-801','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466543,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1803','4栋','4-1-1803','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466575,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-102','4栋','4-2-102','2','0',UTC_TIMESTAMP(),999951,118.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466607,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1202','4栋','4-2-1202','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466639,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2301','4栋','4-2-2301','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465936,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-501','1栋','1-1-501','2','0',UTC_TIMESTAMP(),999951,88.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465968,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1301','1栋','1-1-1301','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466000,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2101','1栋','1-1-2101','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466032,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-501','1栋','1-2-501','2','0',UTC_TIMESTAMP(),999951,116.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466064,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1301','1栋','1-2-1301','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466096,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2101','1栋','1-2-2101','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466128,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-309','2栋','2-309','2','0',UTC_TIMESTAMP(),999951,80.94);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466160,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-509','2栋','2-509','2','0',UTC_TIMESTAMP(),999951,81.05);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466192,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-709','2栋','2-709','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466224,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-909','2栋','2-909','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466256,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1109','2栋','2-1109','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466288,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1309','2栋','2-1309','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466320,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1509','2栋','2-1509','2','0',UTC_TIMESTAMP(),999951,81.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466352,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1001','3栋','3-1-1001','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466384,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2003','3栋','3-1-2003','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466416,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-402','3栋','3-2-402','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466448,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1501','3栋','3-2-1501','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466480,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2503','3栋','3-2-2503','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466512,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-802','4栋','4-1-802','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466544,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1901','4栋','4-1-1901','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466576,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-201','4栋','4-2-201','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466608,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1203','4栋','4-2-1203','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466640,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2302','4栋','4-2-2302','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465937,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-502','1栋','1-1-502','2','0',UTC_TIMESTAMP(),999951,117.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465969,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1302','1栋','1-1-1302','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466001,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2102','1栋','1-1-2102','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466033,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-502','1栋','1-2-502','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466065,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1302','1栋','1-2-1302','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466097,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2102','1栋','1-2-2102','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466129,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-310','2栋','2-310','2','0',UTC_TIMESTAMP(),999951,89.07);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466161,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-510','2栋','2-510','2','0',UTC_TIMESTAMP(),999951,89.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466193,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-710','2栋','2-710','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466225,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-910','2栋','2-910','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466257,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1110','2栋','2-1110','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466289,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1310','2栋','2-1310','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466321,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1510','2栋','2-1510','2','0',UTC_TIMESTAMP(),999951,89.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466353,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1002','3栋','3-1-1002','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466385,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2101','3栋','3-1-2101','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466417,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-403','3栋','3-2-403','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466449,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1502','3栋','3-2-1502','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466481,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2601','3栋','3-2-2601','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466513,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-803','4栋','4-1-803','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466545,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1902','4栋','4-1-1902','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466577,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-202','4栋','4-2-202','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466609,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1301','4栋','4-2-1301','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466641,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2303','4栋','4-2-2303','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465938,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-503','1栋','1-1-503','2','0',UTC_TIMESTAMP(),999951,86.55);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465970,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1303','1栋','1-1-1303','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466002,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2103','1栋','1-1-2103','2','0',UTC_TIMESTAMP(),999951,86.64);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466034,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-503','1栋','1-2-503','2','0',UTC_TIMESTAMP(),999951,86.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466066,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1303','1栋','1-2-1303','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466098,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2103','1栋','1-2-2103','2','0',UTC_TIMESTAMP(),999951,86.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466130,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-311','2栋','2-311','2','0',UTC_TIMESTAMP(),999951,93.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466162,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-511','2栋','2-511','2','0',UTC_TIMESTAMP(),999951,93.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466194,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-711','2栋','2-711','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466226,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-911','2栋','2-911','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466258,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1111','2栋','2-1111','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466290,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1311','2栋','2-1311','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466322,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1511','2栋','2-1511','2','0',UTC_TIMESTAMP(),999951,93.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466354,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1003','3栋','3-1-1003','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466386,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2102','3栋','3-1-2102','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466418,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-501','3栋','3-2-501','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466450,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1503','3栋','3-2-1503','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466482,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2602','3栋','3-2-2602','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466514,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-901','4栋','4-1-901','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466546,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1903','4栋','4-1-1903','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466578,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-203','4栋','4-2-203','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466610,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1302','4栋','4-2-1302','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466642,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2401','4栋','4-2-2401','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465939,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-504','1栋','1-1-504','2','0',UTC_TIMESTAMP(),999951,87.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465971,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1304','1栋','1-1-1304','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466003,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2104','1栋','1-1-2104','2','0',UTC_TIMESTAMP(),999951,87.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466035,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-504','1栋','1-2-504','2','0',UTC_TIMESTAMP(),999951,85.36);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466067,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1304','1栋','1-2-1304','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466099,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2104','1栋','1-2-2104','2','0',UTC_TIMESTAMP(),999951,85.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466131,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-312','2栋','2-312','2','0',UTC_TIMESTAMP(),999951,89.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466163,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-512','2栋','2-512','2','0',UTC_TIMESTAMP(),999951,89.24);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466195,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-712','2栋','2-712','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466227,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-912','2栋','2-912','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466259,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1112','2栋','2-1112','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466291,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1312','2栋','2-1312','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466323,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1512','2栋','2-1512','2','0',UTC_TIMESTAMP(),999951,89.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466355,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1101','3栋','3-1-1101','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466387,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2103','3栋','3-1-2103','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466419,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-502','3栋','3-2-502','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466451,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1601','3栋','3-2-1601','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466483,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2603','3栋','3-2-2603','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466515,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-902','4栋','4-1-902','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466547,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2001','4栋','4-1-2001','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466579,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-301','4栋','4-2-301','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466611,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1303','4栋','4-2-1303','2','0',UTC_TIMESTAMP(),999951,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466643,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2402','4栋','4-2-2402','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465940,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-601','1栋','1-1-601','2','0',UTC_TIMESTAMP(),999951,88.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465972,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1401','1栋','1-1-1401','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466004,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2201','1栋','1-1-2201','2','0',UTC_TIMESTAMP(),999951,88.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466036,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-601','1栋','1-2-601','2','0',UTC_TIMESTAMP(),999951,116.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466068,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1401','1栋','1-2-1401','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466100,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2201','1栋','1-2-2201','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466132,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-313','2栋','2-313','2','0',UTC_TIMESTAMP(),999951,76.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466164,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-513','2栋','2-513','2','0',UTC_TIMESTAMP(),999951,76.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466196,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-713','2栋','2-713','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466228,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-913','2栋','2-913','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466260,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1113','2栋','2-1113','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466292,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1313','2栋','2-1313','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466324,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1513','2栋','2-1513','2','0',UTC_TIMESTAMP(),999951,76.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466356,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1102','3栋','3-1-1102','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466388,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2201','3栋','3-1-2201','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466420,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-503','3栋','3-2-503','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466452,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1602','3栋','3-2-1602','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466484,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2701','3栋','3-2-2701','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466516,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-903/904','4栋','4-1-903/904','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466548,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2002','4栋','4-1-2002','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466580,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-302','4栋','4-2-302','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466612,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1401','4栋','4-2-1401','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466644,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2403','4栋','4-2-2403','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465941,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-602','1栋','1-1-602','2','0',UTC_TIMESTAMP(),999951,117.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387465973,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-1402','1栋','1-1-1402','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466005,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-1-2202','1栋','1-1-2202','2','0',UTC_TIMESTAMP(),999951,118.33);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466037,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-602','1栋','1-2-602','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466069,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-1402','1栋','1-2-1402','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466101,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','1栋-1-2-2202','1栋','1-2-2202','2','0',UTC_TIMESTAMP(),999951,116.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466133,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-314','2栋','2-314','2','0',UTC_TIMESTAMP(),999951,89.24);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466165,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-514','2栋','2-514','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466197,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-714','2栋','2-714','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466229,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-914','2栋','2-914','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466261,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1114','2栋','2-1114','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466293,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1314','2栋','2-1314','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466325,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','2栋-2-1514','2栋','2-1514','2','0',UTC_TIMESTAMP(),999951,89.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466357,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-1103','3栋','3-1-1103','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466389,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-1-2202','3栋','3-1-2202','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466421,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-601','3栋','3-2-601','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466453,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-1603','3栋','3-2-1603','2','0',UTC_TIMESTAMP(),999951,167.62);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466485,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','3栋-3-2-2702','3栋','3-2-2702','2','0',UTC_TIMESTAMP(),999951,118.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466517,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-1001','4栋','4-1-1001','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466549,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-1-2003','4栋','4-1-2003','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466581,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-303','4栋','4-2-303','2','0',UTC_TIMESTAMP(),999951,150.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466613,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-1402','4栋','4-2-1402','2','0',UTC_TIMESTAMP(),999951,119.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466645,UUID(),240111044332060188,18551,'东莞市',18552,'寮步镇','4栋-4-2-2501','4栋','4-2-2501','2','0',UTC_TIMESTAMP(),999951,119.28);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89328,1041502,240111044332060188,239825274387465942,'1栋-1-1-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89360,1041502,240111044332060188,239825274387465974,'1栋-1-1-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89392,1041502,240111044332060188,239825274387466006,'1栋-1-1-2203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89424,1041502,240111044332060188,239825274387466038,'1栋-1-2-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89456,1041502,240111044332060188,239825274387466070,'1栋-1-2-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89488,1041502,240111044332060188,239825274387466102,'1栋-1-2-2203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89520,1041502,240111044332060188,239825274387466134,'2栋-2-315',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89552,1041502,240111044332060188,239825274387466166,'2栋-2-515',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89584,1041502,240111044332060188,239825274387466198,'2栋-2-715',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89616,1041502,240111044332060188,239825274387466230,'2栋-2-915',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89648,1041502,240111044332060188,239825274387466262,'2栋-2-1115',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89680,1041502,240111044332060188,239825274387466294,'2栋-2-1315',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89712,1041502,240111044332060188,239825274387466326,'2栋-2-1515',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89744,1041502,240111044332060188,239825274387466358,'3栋-3-1-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89776,1041502,240111044332060188,239825274387466390,'3栋-3-1-2203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89808,1041502,240111044332060188,239825274387466422,'3栋-3-2-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89840,1041502,240111044332060188,239825274387466454,'3栋-3-2-1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89872,1041502,240111044332060188,239825274387466486,'3栋-3-2-2703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89904,1041502,240111044332060188,239825274387466518,'4栋-4-1-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89936,1041502,240111044332060188,239825274387466550,'4栋-4-1-2101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89968,1041502,240111044332060188,239825274387466582,'4栋-4-2-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90000,1041502,240111044332060188,239825274387466614,'4栋-4-2-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90032,1041502,240111044332060188,239825274387466646,'4栋-4-2-2502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89329,1041502,240111044332060188,239825274387465943,'1栋-1-1-604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89361,1041502,240111044332060188,239825274387465975,'1栋-1-1-1404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89393,1041502,240111044332060188,239825274387466007,'1栋-1-1-2204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89425,1041502,240111044332060188,239825274387466039,'1栋-1-2-604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89457,1041502,240111044332060188,239825274387466071,'1栋-1-2-1404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89489,1041502,240111044332060188,239825274387466103,'1栋-1-2-2204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89521,1041502,240111044332060188,239825274387466135,'2栋-2-316',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89553,1041502,240111044332060188,239825274387466167,'2栋-2-516',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89585,1041502,240111044332060188,239825274387466199,'2栋-2-716',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89617,1041502,240111044332060188,239825274387466231,'2栋-2-916',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89649,1041502,240111044332060188,239825274387466263,'2栋-2-1116',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89681,1041502,240111044332060188,239825274387466295,'2栋-2-1316',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89713,1041502,240111044332060188,239825274387466327,'2栋-2-1516',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89745,1041502,240111044332060188,239825274387466359,'3栋-3-1-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89777,1041502,240111044332060188,239825274387466391,'3栋-3-1-2301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89809,1041502,240111044332060188,239825274387466423,'3栋-3-2-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89841,1041502,240111044332060188,239825274387466455,'3栋-3-2-1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89873,1041502,240111044332060188,239825274387466487,'3栋-3-2-2801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89905,1041502,240111044332060188,239825274387466519,'4栋-4-1-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89937,1041502,240111044332060188,239825274387466551,'4栋-4-1-2102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89969,1041502,240111044332060188,239825274387466583,'4栋-4-2-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90001,1041502,240111044332060188,239825274387466615,'4栋-4-2-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90033,1041502,240111044332060188,239825274387466647,'4栋-4-2-2503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89330,1041502,240111044332060188,239825274387465944,'1栋-1-1-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89362,1041502,240111044332060188,239825274387465976,'1栋-1-1-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89394,1041502,240111044332060188,239825274387466008,'1栋-1-1-2301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89426,1041502,240111044332060188,239825274387466040,'1栋-1-2-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89458,1041502,240111044332060188,239825274387466072,'1栋-1-2-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89490,1041502,240111044332060188,239825274387466104,'1栋-1-2-2301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89522,1041502,240111044332060188,239825274387466136,'2栋-2-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89554,1041502,240111044332060188,239825274387466168,'2栋-2-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89586,1041502,240111044332060188,239825274387466200,'2栋-2-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89618,1041502,240111044332060188,239825274387466232,'2栋-2-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89650,1041502,240111044332060188,239825274387466264,'2栋-2-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89682,1041502,240111044332060188,239825274387466296,'2栋-2-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89714,1041502,240111044332060188,239825274387466328,'3栋-3-1-201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89746,1041502,240111044332060188,239825274387466360,'3栋-3-1-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89778,1041502,240111044332060188,239825274387466392,'3栋-3-1-2302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89810,1041502,240111044332060188,239825274387466424,'3栋-3-2-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89842,1041502,240111044332060188,239825274387466456,'3栋-3-2-1703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89874,1041502,240111044332060188,239825274387466488,'3栋-3-2-2802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89906,1041502,240111044332060188,239825274387466520,'4栋-4-1-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89938,1041502,240111044332060188,239825274387466552,'4栋-4-1-2103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89970,1041502,240111044332060188,239825274387466584,'4栋-4-2-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90002,1041502,240111044332060188,239825274387466616,'4栋-4-2-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90034,1041502,240111044332060188,239825274387466648,'4栋-4-2-2601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89331,1041502,240111044332060188,239825274387465945,'1栋-1-1-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89363,1041502,240111044332060188,239825274387465977,'1栋-1-1-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89395,1041502,240111044332060188,239825274387466009,'1栋-1-1-2302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89427,1041502,240111044332060188,239825274387466041,'1栋-1-2-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89459,1041502,240111044332060188,239825274387466073,'1栋-1-2-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89491,1041502,240111044332060188,239825274387466105,'1栋-1-2-2302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89523,1041502,240111044332060188,239825274387466137,'2栋-2-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89555,1041502,240111044332060188,239825274387466169,'2栋-2-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89587,1041502,240111044332060188,239825274387466201,'2栋-2-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89619,1041502,240111044332060188,239825274387466233,'2栋-2-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89651,1041502,240111044332060188,239825274387466265,'2栋-2-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89683,1041502,240111044332060188,239825274387466297,'2栋-2-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89715,1041502,240111044332060188,239825274387466329,'3栋-3-1-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89747,1041502,240111044332060188,239825274387466361,'3栋-3-1-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89779,1041502,240111044332060188,239825274387466393,'3栋-3-1-2303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89811,1041502,240111044332060188,239825274387466425,'3栋-3-2-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89843,1041502,240111044332060188,239825274387466457,'3栋-3-2-1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89875,1041502,240111044332060188,239825274387466489,'3栋-3-2-2803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89907,1041502,240111044332060188,239825274387466521,'4栋-4-1-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89939,1041502,240111044332060188,239825274387466553,'4栋-4-1-2201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89971,1041502,240111044332060188,239825274387466585,'4栋-4-2-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90003,1041502,240111044332060188,239825274387466617,'4栋-4-2-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90035,1041502,240111044332060188,239825274387466649,'4栋-4-2-2602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89332,1041502,240111044332060188,239825274387465946,'1栋-1-1-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89364,1041502,240111044332060188,239825274387465978,'1栋-1-1-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89396,1041502,240111044332060188,239825274387466010,'1栋-1-1-2303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89428,1041502,240111044332060188,239825274387466042,'1栋-1-2-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89460,1041502,240111044332060188,239825274387466074,'1栋-1-2-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89492,1041502,240111044332060188,239825274387466106,'1栋-1-2-2303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89524,1041502,240111044332060188,239825274387466138,'2栋-2-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89556,1041502,240111044332060188,239825274387466170,'2栋-2-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89588,1041502,240111044332060188,239825274387466202,'2栋-2-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89620,1041502,240111044332060188,239825274387466234,'2栋-2-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89652,1041502,240111044332060188,239825274387466266,'2栋-2-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89684,1041502,240111044332060188,239825274387466298,'2栋-2-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89716,1041502,240111044332060188,239825274387466330,'3栋-3-1-203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89748,1041502,240111044332060188,239825274387466362,'3栋-3-1-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89780,1041502,240111044332060188,239825274387466394,'3栋-3-1-2401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89812,1041502,240111044332060188,239825274387466426,'3栋-3-2-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89844,1041502,240111044332060188,239825274387466458,'3栋-3-2-1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89876,1041502,240111044332060188,239825274387466490,'4栋-4-1-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89908,1041502,240111044332060188,239825274387466522,'4栋-4-1-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89940,1041502,240111044332060188,239825274387466554,'4栋-4-1-2202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89972,1041502,240111044332060188,239825274387466586,'4栋-4-2-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90004,1041502,240111044332060188,239825274387466618,'4栋-4-2-1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90036,1041502,240111044332060188,239825274387466650,'4栋-4-2-2603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89333,1041502,240111044332060188,239825274387465947,'1栋-1-1-704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89365,1041502,240111044332060188,239825274387465979,'1栋-1-1-1504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89397,1041502,240111044332060188,239825274387466011,'1栋-1-1-2304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89429,1041502,240111044332060188,239825274387466043,'1栋-1-2-704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89461,1041502,240111044332060188,239825274387466075,'1栋-1-2-1504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89493,1041502,240111044332060188,239825274387466107,'1栋-1-2-2304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89525,1041502,240111044332060188,239825274387466139,'2栋-2-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89557,1041502,240111044332060188,239825274387466171,'2栋-2-604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89589,1041502,240111044332060188,239825274387466203,'2栋-2-804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89621,1041502,240111044332060188,239825274387466235,'2栋-2-1004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89653,1041502,240111044332060188,239825274387466267,'2栋-2-1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89685,1041502,240111044332060188,239825274387466299,'2栋-2-1404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89717,1041502,240111044332060188,239825274387466331,'3栋-3-1-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89749,1041502,240111044332060188,239825274387466363,'3栋-3-1-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89781,1041502,240111044332060188,239825274387466395,'3栋-3-1-2402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89813,1041502,240111044332060188,239825274387466427,'3栋-3-2-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89845,1041502,240111044332060188,239825274387466459,'3栋-3-2-1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89877,1041502,240111044332060188,239825274387466491,'4栋-4-1-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89909,1041502,240111044332060188,239825274387466523,'4栋-4-1-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89941,1041502,240111044332060188,239825274387466555,'4栋-4-1-2203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89973,1041502,240111044332060188,239825274387466587,'4栋-4-2-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90005,1041502,240111044332060188,239825274387466619,'4栋-4-2-1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90037,1041502,240111044332060188,239825274387466651,'4栋-4-2-2701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89334,1041502,240111044332060188,239825274387465948,'1栋-1-1-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89366,1041502,240111044332060188,239825274387465980,'1栋-1-1-1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89398,1041502,240111044332060188,239825274387466012,'1栋-1-1-2401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89430,1041502,240111044332060188,239825274387466044,'1栋-1-2-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89462,1041502,240111044332060188,239825274387466076,'1栋-1-2-1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89494,1041502,240111044332060188,239825274387466108,'1栋-1-2-2401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89526,1041502,240111044332060188,239825274387466140,'2栋-2-405',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89558,1041502,240111044332060188,239825274387466172,'2栋-2-605',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89590,1041502,240111044332060188,239825274387466204,'2栋-2-805',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89622,1041502,240111044332060188,239825274387466236,'2栋-2-1005',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89654,1041502,240111044332060188,239825274387466268,'2栋-2-1205',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89686,1041502,240111044332060188,239825274387466300,'2栋-2-1405',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89718,1041502,240111044332060188,239825274387466332,'3栋-3-1-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89750,1041502,240111044332060188,239825274387466364,'3栋-3-1-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89782,1041502,240111044332060188,239825274387466396,'3栋-3-1-2403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89814,1041502,240111044332060188,239825274387466428,'3栋-3-2-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89846,1041502,240111044332060188,239825274387466460,'3栋-3-2-1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89878,1041502,240111044332060188,239825274387466492,'4栋-4-1-201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89910,1041502,240111044332060188,239825274387466524,'4栋-4-1-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89942,1041502,240111044332060188,239825274387466556,'4栋-4-1-2301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89974,1041502,240111044332060188,239825274387466588,'4栋-4-2-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90006,1041502,240111044332060188,239825274387466620,'4栋-4-2-1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90038,1041502,240111044332060188,239825274387466652,'4栋-4-2-2702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89335,1041502,240111044332060188,239825274387465949,'1栋-1-1-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89367,1041502,240111044332060188,239825274387465981,'1栋-1-1-1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89399,1041502,240111044332060188,239825274387466013,'1栋-1-1-2402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89431,1041502,240111044332060188,239825274387466045,'1栋-1-2-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89463,1041502,240111044332060188,239825274387466077,'1栋-1-2-1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89495,1041502,240111044332060188,239825274387466109,'1栋-1-2-2402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89527,1041502,240111044332060188,239825274387466141,'2栋-2-406',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89559,1041502,240111044332060188,239825274387466173,'2栋-2-606',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89591,1041502,240111044332060188,239825274387466205,'2栋-2-806',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89623,1041502,240111044332060188,239825274387466237,'2栋-2-1006',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89655,1041502,240111044332060188,239825274387466269,'2栋-2-1206',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89687,1041502,240111044332060188,239825274387466301,'2栋-2-1406',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89719,1041502,240111044332060188,239825274387466333,'3栋-3-1-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89751,1041502,240111044332060188,239825274387466365,'3栋-3-1-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89783,1041502,240111044332060188,239825274387466397,'3栋-3-1-2501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89815,1041502,240111044332060188,239825274387466429,'3栋-3-2-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89847,1041502,240111044332060188,239825274387466461,'3栋-3-2-1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89879,1041502,240111044332060188,239825274387466493,'4栋-4-1-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89911,1041502,240111044332060188,239825274387466525,'4栋-4-1-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89943,1041502,240111044332060188,239825274387466557,'4栋-4-1-2302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89975,1041502,240111044332060188,239825274387466589,'4栋-4-2-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90007,1041502,240111044332060188,239825274387466621,'4栋-4-2-1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90039,1041502,240111044332060188,239825274387466653,'4栋-4-2-2703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89336,1041502,240111044332060188,239825274387465950,'1栋-1-1-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89368,1041502,240111044332060188,239825274387465982,'1栋-1-1-1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89400,1041502,240111044332060188,239825274387466014,'1栋-1-1-2403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89432,1041502,240111044332060188,239825274387466046,'1栋-1-2-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89464,1041502,240111044332060188,239825274387466078,'1栋-1-2-1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89496,1041502,240111044332060188,239825274387466110,'1栋-1-2-2403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89528,1041502,240111044332060188,239825274387466142,'2栋-2-407',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89560,1041502,240111044332060188,239825274387466174,'2栋-2-607',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89592,1041502,240111044332060188,239825274387466206,'2栋-2-807',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89624,1041502,240111044332060188,239825274387466238,'2栋-2-1007',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89656,1041502,240111044332060188,239825274387466270,'2栋-2-1207',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89688,1041502,240111044332060188,239825274387466302,'2栋-2-1407',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89720,1041502,240111044332060188,239825274387466334,'3栋-3-1-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89752,1041502,240111044332060188,239825274387466366,'3栋-3-1-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89784,1041502,240111044332060188,239825274387466398,'3栋-3-1-2502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89816,1041502,240111044332060188,239825274387466430,'3栋-3-2-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89848,1041502,240111044332060188,239825274387466462,'3栋-3-2-1903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89880,1041502,240111044332060188,239825274387466494,'4栋-4-1-203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89912,1041502,240111044332060188,239825274387466526,'4栋-4-1-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89944,1041502,240111044332060188,239825274387466558,'4栋-4-1-2303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89976,1041502,240111044332060188,239825274387466590,'4栋-4-2-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90008,1041502,240111044332060188,239825274387466622,'4栋-4-2-1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90040,1041502,240111044332060188,239825274387466654,'4栋-4-2-2801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89337,1041502,240111044332060188,239825274387465951,'1栋-1-1-804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89369,1041502,240111044332060188,239825274387465983,'1栋-1-1-1604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89401,1041502,240111044332060188,239825274387466015,'1栋-1-1-2404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89433,1041502,240111044332060188,239825274387466047,'1栋-1-2-804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89465,1041502,240111044332060188,239825274387466079,'1栋-1-2-1604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89497,1041502,240111044332060188,239825274387466111,'1栋-1-2-2404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89529,1041502,240111044332060188,239825274387466143,'2栋-2-408',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89561,1041502,240111044332060188,239825274387466175,'2栋-2-608',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89593,1041502,240111044332060188,239825274387466207,'2栋-2-808',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89625,1041502,240111044332060188,239825274387466239,'2栋-2-1008',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89657,1041502,240111044332060188,239825274387466271,'2栋-2-1208',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89689,1041502,240111044332060188,239825274387466303,'2栋-2-1408',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89721,1041502,240111044332060188,239825274387466335,'3栋-3-1-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89753,1041502,240111044332060188,239825274387466367,'3栋-3-1-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89785,1041502,240111044332060188,239825274387466399,'3栋-3-1-2503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89817,1041502,240111044332060188,239825274387466431,'3栋-3-2-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89849,1041502,240111044332060188,239825274387466463,'3栋-3-2-2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89881,1041502,240111044332060188,239825274387466495,'4栋-4-1-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89913,1041502,240111044332060188,239825274387466527,'4栋-4-1-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89945,1041502,240111044332060188,239825274387466559,'4栋-4-1-2401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89977,1041502,240111044332060188,239825274387466591,'4栋-4-2-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90009,1041502,240111044332060188,239825274387466623,'4栋-4-2-1703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90041,1041502,240111044332060188,239825274387466655,'4栋-4-2-2802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89338,1041502,240111044332060188,239825274387465952,'1栋-1-1-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89370,1041502,240111044332060188,239825274387465984,'1栋-1-1-1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89402,1041502,240111044332060188,239825274387466016,'1栋-1-1-2501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89434,1041502,240111044332060188,239825274387466048,'1栋-1-2-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89466,1041502,240111044332060188,239825274387466080,'1栋-1-2-1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89498,1041502,240111044332060188,239825274387466112,'1栋-1-2-2501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89530,1041502,240111044332060188,239825274387466144,'2栋-2-409',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89562,1041502,240111044332060188,239825274387466176,'2栋-2-609',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89594,1041502,240111044332060188,239825274387466208,'2栋-2-809',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89626,1041502,240111044332060188,239825274387466240,'2栋-2-1009',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89658,1041502,240111044332060188,239825274387466272,'2栋-2-1209',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89690,1041502,240111044332060188,239825274387466304,'2栋-2-1409',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89722,1041502,240111044332060188,239825274387466336,'3栋-3-1-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89754,1041502,240111044332060188,239825274387466368,'3栋-3-1-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89786,1041502,240111044332060188,239825274387466400,'3栋-3-1-2601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89818,1041502,240111044332060188,239825274387466432,'3栋-3-2-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89850,1041502,240111044332060188,239825274387466464,'3栋-3-2-2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89882,1041502,240111044332060188,239825274387466496,'4栋-4-1-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89914,1041502,240111044332060188,239825274387466528,'4栋-4-1-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89946,1041502,240111044332060188,239825274387466560,'4栋-4-1-2402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89978,1041502,240111044332060188,239825274387466592,'4栋-4-2-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90010,1041502,240111044332060188,239825274387466624,'4栋-4-2-1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90042,1041502,240111044332060188,239825274387466656,'4栋-4-2-2803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89339,1041502,240111044332060188,239825274387465953,'1栋-1-1-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89371,1041502,240111044332060188,239825274387465985,'1栋-1-1-1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89403,1041502,240111044332060188,239825274387466017,'1栋-1-1-2502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89435,1041502,240111044332060188,239825274387466049,'1栋-1-2-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89467,1041502,240111044332060188,239825274387466081,'1栋-1-2-1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89499,1041502,240111044332060188,239825274387466113,'1栋-1-2-2502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89531,1041502,240111044332060188,239825274387466145,'2栋-2-410',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89563,1041502,240111044332060188,239825274387466177,'2栋-2-610',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89595,1041502,240111044332060188,239825274387466209,'2栋-2-810',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89627,1041502,240111044332060188,239825274387466241,'2栋-2-1010',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89659,1041502,240111044332060188,239825274387466273,'2栋-2-1210',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89691,1041502,240111044332060188,239825274387466305,'2栋-2-1410',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89723,1041502,240111044332060188,239825274387466337,'3栋-3-1-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89755,1041502,240111044332060188,239825274387466369,'3栋-3-1-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89787,1041502,240111044332060188,239825274387466401,'3栋-3-1-2602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89819,1041502,240111044332060188,239825274387466433,'3栋-3-2-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89851,1041502,240111044332060188,239825274387466465,'3栋-3-2-2003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89883,1041502,240111044332060188,239825274387466497,'4栋-4-1-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89915,1041502,240111044332060188,239825274387466529,'4栋-4-1-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89947,1041502,240111044332060188,239825274387466561,'4栋-4-1-2403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89979,1041502,240111044332060188,239825274387466593,'4栋-4-2-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90011,1041502,240111044332060188,239825274387466625,'4栋-4-2-1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89340,1041502,240111044332060188,239825274387465954,'1栋-1-1-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89372,1041502,240111044332060188,239825274387465986,'1栋-1-1-1703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89404,1041502,240111044332060188,239825274387466018,'1栋-1-1-2503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89436,1041502,240111044332060188,239825274387466050,'1栋-1-2-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89468,1041502,240111044332060188,239825274387466082,'1栋-1-2-1703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89500,1041502,240111044332060188,239825274387466114,'1栋-1-2-2503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89532,1041502,240111044332060188,239825274387466146,'2栋-2-411',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89564,1041502,240111044332060188,239825274387466178,'2栋-2-611',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89596,1041502,240111044332060188,239825274387466210,'2栋-2-811',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89628,1041502,240111044332060188,239825274387466242,'2栋-2-1011',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89660,1041502,240111044332060188,239825274387466274,'2栋-2-1211',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89692,1041502,240111044332060188,239825274387466306,'2栋-2-1411',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89724,1041502,240111044332060188,239825274387466338,'3栋-3-1-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89756,1041502,240111044332060188,239825274387466370,'3栋-3-1-1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89788,1041502,240111044332060188,239825274387466402,'3栋-3-1-2603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89820,1041502,240111044332060188,239825274387466434,'3栋-3-2-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89852,1041502,240111044332060188,239825274387466466,'3栋-3-2-2101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89884,1041502,240111044332060188,239825274387466498,'4栋-4-1-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89916,1041502,240111044332060188,239825274387466530,'4栋-4-1-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89948,1041502,240111044332060188,239825274387466562,'4栋-4-1-2501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89980,1041502,240111044332060188,239825274387466594,'4栋-4-2-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90012,1041502,240111044332060188,239825274387466626,'4栋-4-2-1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89341,1041502,240111044332060188,239825274387465955,'1栋-1-1-904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89373,1041502,240111044332060188,239825274387465987,'1栋-1-1-1704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89405,1041502,240111044332060188,239825274387466019,'1栋-1-1-2504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89437,1041502,240111044332060188,239825274387466051,'1栋-1-2-904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89469,1041502,240111044332060188,239825274387466083,'1栋-1-2-1704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89501,1041502,240111044332060188,239825274387466115,'1栋-1-2-2504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89533,1041502,240111044332060188,239825274387466147,'2栋-2-412',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89565,1041502,240111044332060188,239825274387466179,'2栋-2-612',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89597,1041502,240111044332060188,239825274387466211,'2栋-2-812',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89629,1041502,240111044332060188,239825274387466243,'2栋-2-1012',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89661,1041502,240111044332060188,239825274387466275,'2栋-2-1212',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89693,1041502,240111044332060188,239825274387466307,'2栋-2-1412',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89725,1041502,240111044332060188,239825274387466339,'3栋-3-1-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89757,1041502,240111044332060188,239825274387466371,'3栋-3-1-1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89789,1041502,240111044332060188,239825274387466403,'3栋-3-1-2701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89821,1041502,240111044332060188,239825274387466435,'3栋-3-2-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89853,1041502,240111044332060188,239825274387466467,'3栋-3-2-2102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89885,1041502,240111044332060188,239825274387466499,'4栋-4-1-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89917,1041502,240111044332060188,239825274387466531,'4栋-4-1-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89949,1041502,240111044332060188,239825274387466563,'4栋-4-1-2502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89981,1041502,240111044332060188,239825274387466595,'4栋-4-2-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90013,1041502,240111044332060188,239825274387466627,'4栋-4-2-1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89342,1041502,240111044332060188,239825274387465956,'1栋-1-1-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89374,1041502,240111044332060188,239825274387465988,'1栋-1-1-1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89406,1041502,240111044332060188,239825274387466020,'1栋-1-1-2601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89438,1041502,240111044332060188,239825274387466052,'1栋-1-2-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89470,1041502,240111044332060188,239825274387466084,'1栋-1-2-1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89502,1041502,240111044332060188,239825274387466116,'1栋-1-2-2601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89534,1041502,240111044332060188,239825274387466148,'2栋-2-413',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89566,1041502,240111044332060188,239825274387466180,'2栋-2-613',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89598,1041502,240111044332060188,239825274387466212,'2栋-2-813',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89630,1041502,240111044332060188,239825274387466244,'2栋-2-1013',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89662,1041502,240111044332060188,239825274387466276,'2栋-2-1213',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89694,1041502,240111044332060188,239825274387466308,'2栋-2-1413',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89726,1041502,240111044332060188,239825274387466340,'3栋-3-1-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89758,1041502,240111044332060188,239825274387466372,'3栋-3-1-1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89790,1041502,240111044332060188,239825274387466404,'3栋-3-1-2702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89822,1041502,240111044332060188,239825274387466436,'3栋-3-2-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89854,1041502,240111044332060188,239825274387466468,'3栋-3-2-2103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89886,1041502,240111044332060188,239825274387466500,'4栋-4-1-403/404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89918,1041502,240111044332060188,239825274387466532,'4栋-4-1-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89950,1041502,240111044332060188,239825274387466564,'4栋-4-1-2503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89982,1041502,240111044332060188,239825274387466596,'4栋-4-2-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90014,1041502,240111044332060188,239825274387466628,'4栋-4-2-1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89343,1041502,240111044332060188,239825274387465957,'1栋-1-1-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89375,1041502,240111044332060188,239825274387465989,'1栋-1-1-1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89407,1041502,240111044332060188,239825274387466021,'1栋-1-1-2602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89439,1041502,240111044332060188,239825274387466053,'1栋-1-2-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89471,1041502,240111044332060188,239825274387466085,'1栋-1-2-1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89503,1041502,240111044332060188,239825274387466117,'1栋-1-2-2602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89535,1041502,240111044332060188,239825274387466149,'2栋-2-414',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89567,1041502,240111044332060188,239825274387466181,'2栋-2-614',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89599,1041502,240111044332060188,239825274387466213,'2栋-2-814',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89631,1041502,240111044332060188,239825274387466245,'2栋-2-1014',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89663,1041502,240111044332060188,239825274387466277,'2栋-2-1214',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89695,1041502,240111044332060188,239825274387466309,'2栋-2-1414',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89727,1041502,240111044332060188,239825274387466341,'3栋-3-1-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89759,1041502,240111044332060188,239825274387466373,'3栋-3-1-1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89791,1041502,240111044332060188,239825274387466405,'3栋-3-1-2703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89823,1041502,240111044332060188,239825274387466437,'3栋-3-2-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89855,1041502,240111044332060188,239825274387466469,'3栋-3-2-2201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89887,1041502,240111044332060188,239825274387466501,'4栋-4-1-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89919,1041502,240111044332060188,239825274387466533,'4栋-4-1-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89951,1041502,240111044332060188,239825274387466565,'4栋-4-1-2601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89983,1041502,240111044332060188,239825274387466597,'4栋-4-2-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90015,1041502,240111044332060188,239825274387466629,'4栋-4-2-1903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89344,1041502,240111044332060188,239825274387465958,'1栋-1-1-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89376,1041502,240111044332060188,239825274387465990,'1栋-1-1-1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89408,1041502,240111044332060188,239825274387466022,'1栋-1-1-2603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89440,1041502,240111044332060188,239825274387466054,'1栋-1-2-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89472,1041502,240111044332060188,239825274387466086,'1栋-1-2-1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89504,1041502,240111044332060188,239825274387466118,'1栋-1-2-2603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89536,1041502,240111044332060188,239825274387466150,'2栋-2-415',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89568,1041502,240111044332060188,239825274387466182,'2栋-2-615',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89600,1041502,240111044332060188,239825274387466214,'2栋-2-815',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89632,1041502,240111044332060188,239825274387466246,'2栋-2-1015',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89664,1041502,240111044332060188,239825274387466278,'2栋-2-1215',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89696,1041502,240111044332060188,239825274387466310,'2栋-2-1415',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89728,1041502,240111044332060188,239825274387466342,'3栋-3-1-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89760,1041502,240111044332060188,239825274387466374,'3栋-3-1-1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89792,1041502,240111044332060188,239825274387466406,'3栋-3-1-2801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89824,1041502,240111044332060188,239825274387466438,'3栋-3-2-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89856,1041502,240111044332060188,239825274387466470,'3栋-3-2-2202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89888,1041502,240111044332060188,239825274387466502,'4栋-4-1-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89920,1041502,240111044332060188,239825274387466534,'4栋-4-1-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89952,1041502,240111044332060188,239825274387466566,'4栋-4-1-2602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89984,1041502,240111044332060188,239825274387466598,'4栋-4-2-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90016,1041502,240111044332060188,239825274387466630,'4栋-4-2-2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89345,1041502,240111044332060188,239825274387465959,'1栋-1-1-1004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89377,1041502,240111044332060188,239825274387465991,'1栋-1-1-1804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89409,1041502,240111044332060188,239825274387466023,'1栋-1-1-2604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89441,1041502,240111044332060188,239825274387466055,'1栋-1-2-1004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89473,1041502,240111044332060188,239825274387466087,'1栋-1-2-1804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89505,1041502,240111044332060188,239825274387466119,'1栋-1-2-2604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89537,1041502,240111044332060188,239825274387466151,'2栋-2-416',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89569,1041502,240111044332060188,239825274387466183,'2栋-2-616',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89601,1041502,240111044332060188,239825274387466215,'2栋-2-816',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89633,1041502,240111044332060188,239825274387466247,'2栋-2-1016',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89665,1041502,240111044332060188,239825274387466279,'2栋-2-1216',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89697,1041502,240111044332060188,239825274387466311,'2栋-2-1416',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89729,1041502,240111044332060188,239825274387466343,'3栋-3-1-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89761,1041502,240111044332060188,239825274387466375,'3栋-3-1-1703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89793,1041502,240111044332060188,239825274387466407,'3栋-3-1-2802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89825,1041502,240111044332060188,239825274387466439,'3栋-3-2-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89857,1041502,240111044332060188,239825274387466471,'3栋-3-2-2203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89889,1041502,240111044332060188,239825274387466503,'4栋-4-1-503/504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89921,1041502,240111044332060188,239825274387466535,'4栋-4-1-1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89953,1041502,240111044332060188,239825274387466567,'4栋-4-1-2603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89985,1041502,240111044332060188,239825274387466599,'4栋-4-2-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90017,1041502,240111044332060188,239825274387466631,'4栋-4-2-2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89314,1041502,240111044332060188,239825274387465928,'1栋-1-1-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89346,1041502,240111044332060188,239825274387465960,'1栋-1-1-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89378,1041502,240111044332060188,239825274387465992,'1栋-1-1-1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89410,1041502,240111044332060188,239825274387466024,'1栋-1-2-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89442,1041502,240111044332060188,239825274387466056,'1栋-1-2-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89474,1041502,240111044332060188,239825274387466088,'1栋-1-2-1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89506,1041502,240111044332060188,239825274387466120,'2栋-2-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89538,1041502,240111044332060188,239825274387466152,'2栋-2-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89570,1041502,240111044332060188,239825274387466184,'2栋-2-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89602,1041502,240111044332060188,239825274387466216,'2栋-2-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89634,1041502,240111044332060188,239825274387466248,'2栋-2-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89666,1041502,240111044332060188,239825274387466280,'2栋-2-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89698,1041502,240111044332060188,239825274387466312,'2栋-2-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89730,1041502,240111044332060188,239825274387466344,'3栋-3-1-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89762,1041502,240111044332060188,239825274387466376,'3栋-3-1-1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89794,1041502,240111044332060188,239825274387466408,'3栋-3-1-2803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89826,1041502,240111044332060188,239825274387466440,'3栋-3-2-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89858,1041502,240111044332060188,239825274387466472,'3栋-3-2-2301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89890,1041502,240111044332060188,239825274387466504,'4栋-4-1-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89922,1041502,240111044332060188,239825274387466536,'4栋-4-1-1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89954,1041502,240111044332060188,239825274387466568,'4栋-4-1-2701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89986,1041502,240111044332060188,239825274387466600,'4栋-4-2-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90018,1041502,240111044332060188,239825274387466632,'4栋-4-2-2003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89315,1041502,240111044332060188,239825274387465929,'1栋-1-1-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89347,1041502,240111044332060188,239825274387465961,'1栋-1-1-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89379,1041502,240111044332060188,239825274387465993,'1栋-1-1-1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89411,1041502,240111044332060188,239825274387466025,'1栋-1-2-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89443,1041502,240111044332060188,239825274387466057,'1栋-1-2-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89475,1041502,240111044332060188,239825274387466089,'1栋-1-2-1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89507,1041502,240111044332060188,239825274387466121,'2栋-2-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89539,1041502,240111044332060188,239825274387466153,'2栋-2-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89571,1041502,240111044332060188,239825274387466185,'2栋-2-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89603,1041502,240111044332060188,239825274387466217,'2栋-2-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89635,1041502,240111044332060188,239825274387466249,'2栋-2-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89667,1041502,240111044332060188,239825274387466281,'2栋-2-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89699,1041502,240111044332060188,239825274387466313,'2栋-2-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89731,1041502,240111044332060188,239825274387466345,'3栋-3-1-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89763,1041502,240111044332060188,239825274387466377,'3栋-3-1-1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89795,1041502,240111044332060188,239825274387466409,'3栋-3-2-201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89827,1041502,240111044332060188,239825274387466441,'3栋-3-2-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89859,1041502,240111044332060188,239825274387466473,'3栋-3-2-2302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89891,1041502,240111044332060188,239825274387466505,'4栋-4-1-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89923,1041502,240111044332060188,239825274387466537,'4栋-4-1-1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89955,1041502,240111044332060188,239825274387466569,'4栋-4-1-2702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89987,1041502,240111044332060188,239825274387466601,'4栋-4-2-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90019,1041502,240111044332060188,239825274387466633,'4栋-4-2-2101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89316,1041502,240111044332060188,239825274387465930,'1栋-1-1-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89348,1041502,240111044332060188,239825274387465962,'1栋-1-1-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89380,1041502,240111044332060188,239825274387465994,'1栋-1-1-1903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89412,1041502,240111044332060188,239825274387466026,'1栋-1-2-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89444,1041502,240111044332060188,239825274387466058,'1栋-1-2-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89476,1041502,240111044332060188,239825274387466090,'1栋-1-2-1903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89508,1041502,240111044332060188,239825274387466122,'2栋-2-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89540,1041502,240111044332060188,239825274387466154,'2栋-2-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89572,1041502,240111044332060188,239825274387466186,'2栋-2-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89604,1041502,240111044332060188,239825274387466218,'2栋-2-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89636,1041502,240111044332060188,239825274387466250,'2栋-2-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89668,1041502,240111044332060188,239825274387466282,'2栋-2-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89700,1041502,240111044332060188,239825274387466314,'2栋-2-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89732,1041502,240111044332060188,239825274387466346,'3栋-3-1-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89764,1041502,240111044332060188,239825274387466378,'3栋-3-1-1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89796,1041502,240111044332060188,239825274387466410,'3栋-3-2-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89828,1041502,240111044332060188,239825274387466442,'3栋-3-2-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89860,1041502,240111044332060188,239825274387466474,'3栋-3-2-2303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89892,1041502,240111044332060188,239825274387466506,'4栋-4-1-603/604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89924,1041502,240111044332060188,239825274387466538,'4栋-4-1-1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89956,1041502,240111044332060188,239825274387466570,'4栋-4-1-2703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89988,1041502,240111044332060188,239825274387466602,'4栋-4-2-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90020,1041502,240111044332060188,239825274387466634,'4栋-4-2-2102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89317,1041502,240111044332060188,239825274387465931,'1栋-1-1-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89349,1041502,240111044332060188,239825274387465963,'1栋-1-1-1104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89381,1041502,240111044332060188,239825274387465995,'1栋-1-1-1904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89413,1041502,240111044332060188,239825274387466027,'1栋-1-2-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89445,1041502,240111044332060188,239825274387466059,'1栋-1-2-1104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89477,1041502,240111044332060188,239825274387466091,'1栋-1-2-1904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89509,1041502,240111044332060188,239825274387466123,'2栋-2-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89541,1041502,240111044332060188,239825274387466155,'2栋-2-504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89573,1041502,240111044332060188,239825274387466187,'2栋-2-704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89605,1041502,240111044332060188,239825274387466219,'2栋-2-904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89637,1041502,240111044332060188,239825274387466251,'2栋-2-1104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89669,1041502,240111044332060188,239825274387466283,'2栋-2-1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89701,1041502,240111044332060188,239825274387466315,'2栋-2-1504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89733,1041502,240111044332060188,239825274387466347,'3栋-3-1-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89765,1041502,240111044332060188,239825274387466379,'3栋-3-1-1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89797,1041502,240111044332060188,239825274387466411,'3栋-3-2-203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89829,1041502,240111044332060188,239825274387466443,'3栋-3-2-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89861,1041502,240111044332060188,239825274387466475,'3栋-3-2-2401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89893,1041502,240111044332060188,239825274387466507,'4栋-4-1-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89925,1041502,240111044332060188,239825274387466539,'4栋-4-1-1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89957,1041502,240111044332060188,239825274387466571,'4栋-4-1-2801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89989,1041502,240111044332060188,239825274387466603,'4栋-4-2-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90021,1041502,240111044332060188,239825274387466635,'4栋-4-2-2103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89318,1041502,240111044332060188,239825274387465932,'1栋-1-1-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89350,1041502,240111044332060188,239825274387465964,'1栋-1-1-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89382,1041502,240111044332060188,239825274387465996,'1栋-1-1-2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89414,1041502,240111044332060188,239825274387466028,'1栋-1-2-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89446,1041502,240111044332060188,239825274387466060,'1栋-1-2-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89478,1041502,240111044332060188,239825274387466092,'1栋-1-2-2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89510,1041502,240111044332060188,239825274387466124,'2栋-2-305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89542,1041502,240111044332060188,239825274387466156,'2栋-2-505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89574,1041502,240111044332060188,239825274387466188,'2栋-2-705',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89606,1041502,240111044332060188,239825274387466220,'2栋-2-905',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89638,1041502,240111044332060188,239825274387466252,'2栋-2-1105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89670,1041502,240111044332060188,239825274387466284,'2栋-2-1305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89702,1041502,240111044332060188,239825274387466316,'2栋-2-1505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89734,1041502,240111044332060188,239825274387466348,'3栋-3-1-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89766,1041502,240111044332060188,239825274387466380,'3栋-3-1-1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89798,1041502,240111044332060188,239825274387466412,'3栋-3-2-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89830,1041502,240111044332060188,239825274387466444,'3栋-3-2-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89862,1041502,240111044332060188,239825274387466476,'3栋-3-2-2402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89894,1041502,240111044332060188,239825274387466508,'4栋-4-1-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89926,1041502,240111044332060188,239825274387466540,'4栋-4-1-1703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89958,1041502,240111044332060188,239825274387466572,'4栋-4-1-2802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89990,1041502,240111044332060188,239825274387466604,'4栋-4-2-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90022,1041502,240111044332060188,239825274387466636,'4栋-4-2-2201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89319,1041502,240111044332060188,239825274387465933,'1栋-1-1-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89351,1041502,240111044332060188,239825274387465965,'1栋-1-1-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89383,1041502,240111044332060188,239825274387465997,'1栋-1-1-2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89415,1041502,240111044332060188,239825274387466029,'1栋-1-2-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89447,1041502,240111044332060188,239825274387466061,'1栋-1-2-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89479,1041502,240111044332060188,239825274387466093,'1栋-1-2-2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89511,1041502,240111044332060188,239825274387466125,'2栋-2-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89543,1041502,240111044332060188,239825274387466157,'2栋-2-506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89575,1041502,240111044332060188,239825274387466189,'2栋-2-706',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89607,1041502,240111044332060188,239825274387466221,'2栋-2-906',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89639,1041502,240111044332060188,239825274387466253,'2栋-2-1106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89671,1041502,240111044332060188,239825274387466285,'2栋-2-1306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89703,1041502,240111044332060188,239825274387466317,'2栋-2-1506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89735,1041502,240111044332060188,239825274387466349,'3栋-3-1-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89767,1041502,240111044332060188,239825274387466381,'3栋-3-1-1903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89799,1041502,240111044332060188,239825274387466413,'3栋-3-2-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89831,1041502,240111044332060188,239825274387466445,'3栋-3-2-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89863,1041502,240111044332060188,239825274387466477,'3栋-3-2-2403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89895,1041502,240111044332060188,239825274387466509,'4栋-4-1-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89927,1041502,240111044332060188,239825274387466541,'4栋-4-1-1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89959,1041502,240111044332060188,239825274387466573,'4栋-4-1-2803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89991,1041502,240111044332060188,239825274387466605,'4栋-4-2-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90023,1041502,240111044332060188,239825274387466637,'4栋-4-2-2202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89320,1041502,240111044332060188,239825274387465934,'1栋-1-1-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89352,1041502,240111044332060188,239825274387465966,'1栋-1-1-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89384,1041502,240111044332060188,239825274387465998,'1栋-1-1-2003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89416,1041502,240111044332060188,239825274387466030,'1栋-1-2-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89448,1041502,240111044332060188,239825274387466062,'1栋-1-2-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89480,1041502,240111044332060188,239825274387466094,'1栋-1-2-2003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89512,1041502,240111044332060188,239825274387466126,'2栋-2-307',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89544,1041502,240111044332060188,239825274387466158,'2栋-2-507',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89576,1041502,240111044332060188,239825274387466190,'2栋-2-707',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89608,1041502,240111044332060188,239825274387466222,'2栋-2-907',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89640,1041502,240111044332060188,239825274387466254,'2栋-2-1107',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89672,1041502,240111044332060188,239825274387466286,'2栋-2-1307',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89704,1041502,240111044332060188,239825274387466318,'2栋-2-1507',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89736,1041502,240111044332060188,239825274387466350,'3栋-3-1-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89768,1041502,240111044332060188,239825274387466382,'3栋-3-1-2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89800,1041502,240111044332060188,239825274387466414,'3栋-3-2-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89832,1041502,240111044332060188,239825274387466446,'3栋-3-2-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89864,1041502,240111044332060188,239825274387466478,'3栋-3-2-2501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89896,1041502,240111044332060188,239825274387466510,'4栋-4-1-704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89928,1041502,240111044332060188,239825274387466542,'4栋-4-1-1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89960,1041502,240111044332060188,239825274387466574,'4栋-4-2-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89992,1041502,240111044332060188,239825274387466606,'4栋-4-2-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90024,1041502,240111044332060188,239825274387466638,'4栋-4-2-2203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89321,1041502,240111044332060188,239825274387465935,'1栋-1-1-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89353,1041502,240111044332060188,239825274387465967,'1栋-1-1-1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89385,1041502,240111044332060188,239825274387465999,'1栋-1-1-2004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89417,1041502,240111044332060188,239825274387466031,'1栋-1-2-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89449,1041502,240111044332060188,239825274387466063,'1栋-1-2-1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89481,1041502,240111044332060188,239825274387466095,'1栋-1-2-2004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89513,1041502,240111044332060188,239825274387466127,'2栋-2-308',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89545,1041502,240111044332060188,239825274387466159,'2栋-2-508',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89577,1041502,240111044332060188,239825274387466191,'2栋-2-708',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89609,1041502,240111044332060188,239825274387466223,'2栋-2-908',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89641,1041502,240111044332060188,239825274387466255,'2栋-2-1108',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89673,1041502,240111044332060188,239825274387466287,'2栋-2-1308',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89705,1041502,240111044332060188,239825274387466319,'2栋-2-1508',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89737,1041502,240111044332060188,239825274387466351,'3栋-3-1-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89769,1041502,240111044332060188,239825274387466383,'3栋-3-1-2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89801,1041502,240111044332060188,239825274387466415,'3栋-3-2-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89833,1041502,240111044332060188,239825274387466447,'3栋-3-2-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89865,1041502,240111044332060188,239825274387466479,'3栋-3-2-2502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89897,1041502,240111044332060188,239825274387466511,'4栋-4-1-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89929,1041502,240111044332060188,239825274387466543,'4栋-4-1-1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89961,1041502,240111044332060188,239825274387466575,'4栋-4-2-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89993,1041502,240111044332060188,239825274387466607,'4栋-4-2-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90025,1041502,240111044332060188,239825274387466639,'4栋-4-2-2301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89322,1041502,240111044332060188,239825274387465936,'1栋-1-1-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89354,1041502,240111044332060188,239825274387465968,'1栋-1-1-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89386,1041502,240111044332060188,239825274387466000,'1栋-1-1-2101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89418,1041502,240111044332060188,239825274387466032,'1栋-1-2-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89450,1041502,240111044332060188,239825274387466064,'1栋-1-2-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89482,1041502,240111044332060188,239825274387466096,'1栋-1-2-2101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89514,1041502,240111044332060188,239825274387466128,'2栋-2-309',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89546,1041502,240111044332060188,239825274387466160,'2栋-2-509',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89578,1041502,240111044332060188,239825274387466192,'2栋-2-709',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89610,1041502,240111044332060188,239825274387466224,'2栋-2-909',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89642,1041502,240111044332060188,239825274387466256,'2栋-2-1109',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89674,1041502,240111044332060188,239825274387466288,'2栋-2-1309',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89706,1041502,240111044332060188,239825274387466320,'2栋-2-1509',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89738,1041502,240111044332060188,239825274387466352,'3栋-3-1-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89770,1041502,240111044332060188,239825274387466384,'3栋-3-1-2003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89802,1041502,240111044332060188,239825274387466416,'3栋-3-2-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89834,1041502,240111044332060188,239825274387466448,'3栋-3-2-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89866,1041502,240111044332060188,239825274387466480,'3栋-3-2-2503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89898,1041502,240111044332060188,239825274387466512,'4栋-4-1-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89930,1041502,240111044332060188,239825274387466544,'4栋-4-1-1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89962,1041502,240111044332060188,239825274387466576,'4栋-4-2-201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89994,1041502,240111044332060188,239825274387466608,'4栋-4-2-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90026,1041502,240111044332060188,239825274387466640,'4栋-4-2-2302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89323,1041502,240111044332060188,239825274387465937,'1栋-1-1-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89355,1041502,240111044332060188,239825274387465969,'1栋-1-1-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89387,1041502,240111044332060188,239825274387466001,'1栋-1-1-2102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89419,1041502,240111044332060188,239825274387466033,'1栋-1-2-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89451,1041502,240111044332060188,239825274387466065,'1栋-1-2-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89483,1041502,240111044332060188,239825274387466097,'1栋-1-2-2102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89515,1041502,240111044332060188,239825274387466129,'2栋-2-310',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89547,1041502,240111044332060188,239825274387466161,'2栋-2-510',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89579,1041502,240111044332060188,239825274387466193,'2栋-2-710',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89611,1041502,240111044332060188,239825274387466225,'2栋-2-910',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89643,1041502,240111044332060188,239825274387466257,'2栋-2-1110',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89675,1041502,240111044332060188,239825274387466289,'2栋-2-1310',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89707,1041502,240111044332060188,239825274387466321,'2栋-2-1510',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89739,1041502,240111044332060188,239825274387466353,'3栋-3-1-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89771,1041502,240111044332060188,239825274387466385,'3栋-3-1-2101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89803,1041502,240111044332060188,239825274387466417,'3栋-3-2-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89835,1041502,240111044332060188,239825274387466449,'3栋-3-2-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89867,1041502,240111044332060188,239825274387466481,'3栋-3-2-2601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89899,1041502,240111044332060188,239825274387466513,'4栋-4-1-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89931,1041502,240111044332060188,239825274387466545,'4栋-4-1-1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89963,1041502,240111044332060188,239825274387466577,'4栋-4-2-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89995,1041502,240111044332060188,239825274387466609,'4栋-4-2-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90027,1041502,240111044332060188,239825274387466641,'4栋-4-2-2303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89324,1041502,240111044332060188,239825274387465938,'1栋-1-1-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89356,1041502,240111044332060188,239825274387465970,'1栋-1-1-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89388,1041502,240111044332060188,239825274387466002,'1栋-1-1-2103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89420,1041502,240111044332060188,239825274387466034,'1栋-1-2-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89452,1041502,240111044332060188,239825274387466066,'1栋-1-2-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89484,1041502,240111044332060188,239825274387466098,'1栋-1-2-2103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89516,1041502,240111044332060188,239825274387466130,'2栋-2-311',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89548,1041502,240111044332060188,239825274387466162,'2栋-2-511',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89580,1041502,240111044332060188,239825274387466194,'2栋-2-711',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89612,1041502,240111044332060188,239825274387466226,'2栋-2-911',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89644,1041502,240111044332060188,239825274387466258,'2栋-2-1111',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89676,1041502,240111044332060188,239825274387466290,'2栋-2-1311',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89708,1041502,240111044332060188,239825274387466322,'2栋-2-1511',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89740,1041502,240111044332060188,239825274387466354,'3栋-3-1-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89772,1041502,240111044332060188,239825274387466386,'3栋-3-1-2102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89804,1041502,240111044332060188,239825274387466418,'3栋-3-2-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89836,1041502,240111044332060188,239825274387466450,'3栋-3-2-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89868,1041502,240111044332060188,239825274387466482,'3栋-3-2-2602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89900,1041502,240111044332060188,239825274387466514,'4栋-4-1-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89932,1041502,240111044332060188,239825274387466546,'4栋-4-1-1903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89964,1041502,240111044332060188,239825274387466578,'4栋-4-2-203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89996,1041502,240111044332060188,239825274387466610,'4栋-4-2-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90028,1041502,240111044332060188,239825274387466642,'4栋-4-2-2401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89325,1041502,240111044332060188,239825274387465939,'1栋-1-1-504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89357,1041502,240111044332060188,239825274387465971,'1栋-1-1-1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89389,1041502,240111044332060188,239825274387466003,'1栋-1-1-2104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89421,1041502,240111044332060188,239825274387466035,'1栋-1-2-504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89453,1041502,240111044332060188,239825274387466067,'1栋-1-2-1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89485,1041502,240111044332060188,239825274387466099,'1栋-1-2-2104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89517,1041502,240111044332060188,239825274387466131,'2栋-2-312',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89549,1041502,240111044332060188,239825274387466163,'2栋-2-512',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89581,1041502,240111044332060188,239825274387466195,'2栋-2-712',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89613,1041502,240111044332060188,239825274387466227,'2栋-2-912',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89645,1041502,240111044332060188,239825274387466259,'2栋-2-1112',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89677,1041502,240111044332060188,239825274387466291,'2栋-2-1312',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89709,1041502,240111044332060188,239825274387466323,'2栋-2-1512',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89741,1041502,240111044332060188,239825274387466355,'3栋-3-1-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89773,1041502,240111044332060188,239825274387466387,'3栋-3-1-2103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89805,1041502,240111044332060188,239825274387466419,'3栋-3-2-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89837,1041502,240111044332060188,239825274387466451,'3栋-3-2-1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89869,1041502,240111044332060188,239825274387466483,'3栋-3-2-2603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89901,1041502,240111044332060188,239825274387466515,'4栋-4-1-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89933,1041502,240111044332060188,239825274387466547,'4栋-4-1-2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89965,1041502,240111044332060188,239825274387466579,'4栋-4-2-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89997,1041502,240111044332060188,239825274387466611,'4栋-4-2-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90029,1041502,240111044332060188,239825274387466643,'4栋-4-2-2402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89326,1041502,240111044332060188,239825274387465940,'1栋-1-1-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89358,1041502,240111044332060188,239825274387465972,'1栋-1-1-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89390,1041502,240111044332060188,239825274387466004,'1栋-1-1-2201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89422,1041502,240111044332060188,239825274387466036,'1栋-1-2-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89454,1041502,240111044332060188,239825274387466068,'1栋-1-2-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89486,1041502,240111044332060188,239825274387466100,'1栋-1-2-2201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89518,1041502,240111044332060188,239825274387466132,'2栋-2-313',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89550,1041502,240111044332060188,239825274387466164,'2栋-2-513',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89582,1041502,240111044332060188,239825274387466196,'2栋-2-713',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89614,1041502,240111044332060188,239825274387466228,'2栋-2-913',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89646,1041502,240111044332060188,239825274387466260,'2栋-2-1113',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89678,1041502,240111044332060188,239825274387466292,'2栋-2-1313',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89710,1041502,240111044332060188,239825274387466324,'2栋-2-1513',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89742,1041502,240111044332060188,239825274387466356,'3栋-3-1-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89774,1041502,240111044332060188,239825274387466388,'3栋-3-1-2201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89806,1041502,240111044332060188,239825274387466420,'3栋-3-2-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89838,1041502,240111044332060188,239825274387466452,'3栋-3-2-1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89870,1041502,240111044332060188,239825274387466484,'3栋-3-2-2701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89902,1041502,240111044332060188,239825274387466516,'4栋-4-1-903/904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89934,1041502,240111044332060188,239825274387466548,'4栋-4-1-2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89966,1041502,240111044332060188,239825274387466580,'4栋-4-2-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89998,1041502,240111044332060188,239825274387466612,'4栋-4-2-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90030,1041502,240111044332060188,239825274387466644,'4栋-4-2-2403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89327,1041502,240111044332060188,239825274387465941,'1栋-1-1-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89359,1041502,240111044332060188,239825274387465973,'1栋-1-1-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89391,1041502,240111044332060188,239825274387466005,'1栋-1-1-2202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89423,1041502,240111044332060188,239825274387466037,'1栋-1-2-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89455,1041502,240111044332060188,239825274387466069,'1栋-1-2-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89487,1041502,240111044332060188,239825274387466101,'1栋-1-2-2202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89519,1041502,240111044332060188,239825274387466133,'2栋-2-314',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89551,1041502,240111044332060188,239825274387466165,'2栋-2-514',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89583,1041502,240111044332060188,239825274387466197,'2栋-2-714',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89615,1041502,240111044332060188,239825274387466229,'2栋-2-914',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89647,1041502,240111044332060188,239825274387466261,'2栋-2-1114',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89679,1041502,240111044332060188,239825274387466293,'2栋-2-1314',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89711,1041502,240111044332060188,239825274387466325,'2栋-2-1514',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89743,1041502,240111044332060188,239825274387466357,'3栋-3-1-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89775,1041502,240111044332060188,239825274387466389,'3栋-3-1-2202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89807,1041502,240111044332060188,239825274387466421,'3栋-3-2-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89839,1041502,240111044332060188,239825274387466453,'3栋-3-2-1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89871,1041502,240111044332060188,239825274387466485,'3栋-3-2-2702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89903,1041502,240111044332060188,239825274387466517,'4栋-4-1-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89935,1041502,240111044332060188,239825274387466549,'4栋-4-1-2003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89967,1041502,240111044332060188,239825274387466581,'4栋-4-2-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (89999,1041502,240111044332060188,239825274387466613,'4栋-4-2-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90031,1041502,240111044332060188,239825274387466645,'4栋-4-2-2501',4);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140850,10000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140851,10100,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140852,10400,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140853,10600,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140854,10800,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140855,11000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140856,20100,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140857,20140,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140858,20150,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140859,20155,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140860,20158,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140861,20170,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140862,20180,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140863,20190,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140864,20191,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140865,20400,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140866,20422,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140867,204011,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140868,204021,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140869,20430,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140870,21100,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140871,21110,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140872,21120,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140873,40100,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140874,40103,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140875,40105,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140876,40110,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140877,40120,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140878,40130,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140879,40300,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140880,40500,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140881,40800,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140882,40830,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140883,40835,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140884,40810,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140885,40840,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140886,40850,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140887,41000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140888,41010,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140889,41020,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140890,41030,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140891,41040,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140892,41050,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140893,41060,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140894,41300,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140895,41310,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140896,41330,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140897,41320,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140898,30000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140899,30500,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140900,30550,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140901,31000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140902,32000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140903,33000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140904,34000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140905,35000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140906,30600,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140907,37000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140908,50000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140909,50100,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140910,50300,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140911,50500,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140912,70000,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140913,70300,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140914,70100,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140915,70200,NULL,'EhNamespaces',999951,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (140916,60100,NULL,'EhNamespaces',999951,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001396,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999951,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001397,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999951,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001398,'',0,1001398,1,'活动管理-默认子分类','/1/1001398',0,2,1,NOW(),0,NULL,999951,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001399,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999951,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001400,'',0,1001400,2,'活动管理二-默认子分类','/2/1001400',0,2,1,NOW(),0,NULL,999951,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001401,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999951,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001402,'',0,1001402,3,'活动管理三-默认子分类','/3/1001402',0,2,1,NOW(),0,NULL,999951,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (541,999951,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (542,999951,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (543,999951,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (544,999951,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (545,999951,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (546,999951,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (547,999951,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (548,999951,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (549,999951,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1041502,240111044332060188);

set @namespaceId = 999951;
set @id = (select Max(id) from  eh_service_module_scopes) +1;
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10100', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10400', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10600', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '11000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '20000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '20100', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40100', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40300', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40500', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40600', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40800', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '30000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '31000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '32000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '33000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '34000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '10750', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '32500', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '12200', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '41300', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '41000', '', 'EhNamespaces', @namespaceId, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '35000', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50000', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50100', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50300', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50500', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50600', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50700', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50800', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '50900', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '40400', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '51000', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '60000', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '60100', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '60200', NULL, NULL, NULL, NULL, '2');
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`) VALUES (@id:=@id+1, @namespaceId, '41500', '用户行为统计', 'EhNamespaces', @namespaceId, NULL, '2');

