INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1988,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999956','the relative path for chengdufenghuangshequ app agreements',999956,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1989,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999956,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1990,'pmtask.handler-999956','flow','0',999956,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1991,'pay.platform','1','支付类型',999956,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (533,'Android_HuiShenghuo',NULL,UTC_TIMESTAMP(),999956);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (534,'iOS_HuiShenghuo',NULL,UTC_TIMESTAMP(),999956);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (886,533,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999956);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (887,534,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999956);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999956,'慧生活');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1531,999956,'community_residential',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (959,'sms.default.sign',0,'zh_CN','慧生活','【慧生活】',999956);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18484,0,'四川','SICHUAN','SC','/四川',1,1,NULL,NULL,2,0,999956);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18485,18484,'成都市','CHENGDUSHI','CDS','/四川/成都市',2,2,NULL,'028',2,1,999956);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18486,18485,'金牛区','JINNIUQU','JNQ','/四川/成都市/金牛区',3,3,NULL,'028',2,0,999956);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060120,UUID(),18485,'成都市',18486,'金牛区','凤翔云庭','凤凰山三期安置房','四川省成都市金牛区韦这碾三路','本建筑区划规划设计的物业类型为： 住宅 
“凤凰山三期安置房项目”位于成都市金牛区凤凰山韦家碾一路。规划总建筑面积235393.27平方米。安置房2324套
地上计入容积率建筑面积162277.87平方米；其中住宅建筑面积158746.75平方米 ；商业用房建筑面积2057.16平方米；社区居委会用房建筑面积690.78平方米；物管用房（含业主委员会活动室）建筑面积752.36平方米；门卫室建筑面积30.82平方米。
地上不计入容积率的建筑面积1126.16平方米。
地下建筑面积71989.24平方米； 其中，地下机动车库建筑面积57122.83平方米； 地下设备用房建筑面积14866.41平方米。
绿地面积10164.22平方米。
本建筑区划容积率：3.99；绿地率：25%；建筑密度：25%。
机动停车位1622个，全部为地下停车位。其中住宅停车位1590个，商业停车位17个，配套用房停车位15个
物业管理用房建筑面积为地下722.36平方米，位于2#楼1层；业主委员会议事活动用房建筑面积30平方米，位于2号楼1层。',0,1,2,UTC_TIMESTAMP(),0,192769,192770,UTC_TIMESTAMP(),999956);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092893,240111044332060120,'',104.082792,30.724587,'wm6nb3nz8y4u');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20230,999956,'COMMUNITY',240111044332060120,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1040330,0,'PM','成都市金港物业管理有限责任公司凤翔云庭物业服务中心','成都市金港物业管理有限责任公司成立于 1995 年，注册资金 500 万元，具有独立法人资格的国家物业管理一级资质的企业，是成都市首批成立的专业化物业管理企业。公司是成都市物业管理协会及金牛区物业管理协会“副会长单位”，四川省房地产业协会“常务理事单位”，中国物业管理协会“常务理事单位”。
公司核心团队由行业内的物业管理高级人才组成，管理团队有 70 余名管理人员及专业技术人员，有省、市级物业管理专家、建筑工程师、电气工程师、园林工程师、注册会计师、经济师、物业管理师等 30 多名中高级综合性管理专业技术人才。其中取得全国注册物业管理师资格有 14 人。
公司总部现设有市场拓展部、财务部、品质管理控制中心、行政人事部。
每个项目都设置一个物业服务中心，下设客户服务部、秩序维护部、环境维护部、绿化维护部等部门。
目前，公司签约项目 40 多个，面积超 500 万平方米，项目类型有大型步行商业街、办公写字楼、工业物业、学校、多层及高层住宅小区等多种物业类型。截止2017 年 8 月，公司已成立了 2 家分公司，2 家子公司。
经过金港人二十余年的努力奋斗，“金港”这个响当当的品牌已经深深的树立在了成都市民的心目中。','/1040330',1,2,'ENTERPRISE',999956,1058381);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1155404,240111044332060120,'organization',1040330,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (27176,'EhOrganizations',1040330,1,10,475347,0,1,UTC_TIMESTAMP(),999956,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (27177,'EhOrganizations',1040330,1,10,475348,0,1,UTC_TIMESTAMP(),999956,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1058381,UUID(),'成都市金港物业管理有限责任公司凤翔云庭物业服务中心','成都市金港物业管理有限责任公司凤翔云庭物业服务中心',1,1,1040330,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192768,1,999956);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192768,UUID(),999956,2,'EhGroups',1058381,'成都市金港物业管理有限责任公司凤翔云庭物业服务中心论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192769,UUID(),999956,2,'',0,'慧生活社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192770,UUID(),999956,2,'',0,'慧生活意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256575,UUID(),999956,192769,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (475347,UUID(),'19238002184','漆枫',NULL,1,45,'1','1','zh_CN','c971fa582028997880b35270bfe10021','d7645545c3af0052584a573b1e204b6e340fba8cefe3c08455327e26f8d13d6d',UTC_TIMESTAMP(),999956);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (475348,UUID(),'19238002185','左邻官方账号',NULL,1,45,'1','1','zh_CN','99099ec673e19395bd3b69d0c1591af1','0989ded50fca02c9c9a2b46af15adc2eb5e25bdc59277f6eea0135b8e4cff761',UTC_TIMESTAMP(),999956);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (30395,1040330,'USER',475347,'漆枫',0,'15882098877',UTC_TIMESTAMP(),999956);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (30396,1040330,'USER',475348,'左邻官方账号',0,'12000001802',UTC_TIMESTAMP(),999956);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (446554,475347,0,'15882098877',NULL,3,UTC_TIMESTAMP(),999956);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (446555,475348,0,'12000001802',NULL,3,UTC_TIMESTAMP(),999956);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2181461,1040330,'/1040330','USER',475347,'manager','漆枫',0,'15882098877',3,999956,'ENTERPRISE',0,30395);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2181462,1040330,'/1040330','USER',475348,'manager','左邻官方账号',0,'12000001802',3,999956,'ENTERPRISE',0,30396);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (30164,475347,1040330,'/1040330','ENTERPRISE',3,999956,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (30165,475348,1040330,'/1040330','ENTERPRISE',3,999956,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115335,'EhOrganizations',1040330,'EhUsers',475347,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115336,'EhOrganizations',1040330,'EhUsers',475348,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971126,240111044332060120,'1栋','1',0,15882098877,'成都市金牛区韦家碾三路340号',12336.35,'104.082792','30.724587','wm6nb3nz8y4u','住宅',NULL,2,0,NULL,1,NOW(),999956,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971127,240111044332060120,'2栋','2',0,15882098877,'成都市金牛区韦家碾三路340号',158.3,'104.082792','30.724587','wm6nb3nz8y4u','商用',NULL,2,0,NULL,1,NOW(),999956,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463880,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-404','1栋','1-1-404','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463912,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-906','1栋','1-1-906','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463944,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1502','1栋','1-1-1502','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463976,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-2004','1栋','1-1-2004','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464008,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-606','1栋','1-2-606','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464040,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1202','1栋','1-2-1202','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464072,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1704','1栋','1-2-1704','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463881,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-405','1栋','1-1-405','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463913,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1001','1栋','1-1-1001','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463945,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1503','1栋','1-1-1503','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463977,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-2005','1栋','1-1-2005','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464009,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-701','1栋','1-2-701','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464041,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1203','1栋','1-2-1203','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464073,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1705','1栋','1-2-1705','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463882,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-406','1栋','1-1-406','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463914,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1002','1栋','1-1-1002','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463946,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1504','1栋','1-1-1504','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463978,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-2006','1栋','1-1-2006','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464010,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-702','1栋','1-2-702','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464042,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1204','1栋','1-2-1204','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464074,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1706','1栋','1-2-1706','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463883,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-501','1栋','1-1-501','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463915,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1003','1栋','1-1-1003','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463947,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1505','1栋','1-1-1505','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463979,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-201','1栋','1-2-201','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464011,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-703','1栋','1-2-703','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464043,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1205','1栋','1-2-1205','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464075,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1801','1栋','1-2-1801','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463884,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-502','1栋','1-1-502','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463916,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1004','1栋','1-1-1004','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463948,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1506','1栋','1-1-1506','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463980,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-202','1栋','1-2-202','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464012,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-704','1栋','1-2-704','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464044,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1206','1栋','1-2-1206','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464076,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1802','1栋','1-2-1802','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463885,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-503','1栋','1-1-503','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463917,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1005','1栋','1-1-1005','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463949,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1601','1栋','1-1-1601','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463981,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-203','1栋','1-2-203','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464013,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-705','1栋','1-2-705','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464045,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1301','1栋','1-2-1301','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464077,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1803','1栋','1-2-1803','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463886,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-504','1栋','1-1-504','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463918,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1006','1栋','1-1-1006','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463950,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1602','1栋','1-1-1602','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463982,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-204','1栋','1-2-204','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464014,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-706','1栋','1-2-706','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464046,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1302','1栋','1-2-1302','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464078,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1804','1栋','1-2-1804','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463887,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-505','1栋','1-1-505','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463919,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1101','1栋','1-1-1101','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463951,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1603','1栋','1-1-1603','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463983,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-205','1栋','1-2-205','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464015,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-801','1栋','1-2-801','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464047,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1303','1栋','1-2-1303','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464079,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1805','1栋','1-2-1805','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463888,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-506','1栋','1-1-506','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463920,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1102','1栋','1-1-1102','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463952,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1604','1栋','1-1-1604','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463984,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-206','1栋','1-2-206','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464016,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-802','1栋','1-2-802','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464048,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1304','1栋','1-2-1304','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464080,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1806','1栋','1-2-1806','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463889,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-601','1栋','1-1-601','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463921,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1103','1栋','1-1-1103','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463953,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1605','1栋','1-1-1605','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463985,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-301','1栋','1-2-301','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464017,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-803','1栋','1-2-803','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464049,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1305','1栋','1-2-1305','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464081,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1901','1栋','1-2-1901','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463890,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-602','1栋','1-1-602','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463922,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1104','1栋','1-1-1104','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463954,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1606','1栋','1-1-1606','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463986,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-302','1栋','1-2-302','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464018,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-804','1栋','1-2-804','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464050,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1306','1栋','1-2-1306','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464082,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1902','1栋','1-2-1902','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463891,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-603','1栋','1-1-603','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463923,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1105','1栋','1-1-1105','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463955,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1701','1栋','1-1-1701','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463987,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-303','1栋','1-2-303','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464019,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-805','1栋','1-2-805','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464051,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1401','1栋','1-2-1401','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464083,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1903','1栋','1-2-1903','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463892,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-604','1栋','1-1-604','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463924,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1106','1栋','1-1-1106','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463956,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1702','1栋','1-1-1702','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463988,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-304','1栋','1-2-304','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464020,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-806','1栋','1-2-806','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464052,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1402','1栋','1-2-1402','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464084,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1904','1栋','1-2-1904','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463893,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-605','1栋','1-1-605','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463925,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1201','1栋','1-1-1201','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463957,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1703','1栋','1-1-1703','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463989,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-305','1栋','1-2-305','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464021,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-901','1栋','1-2-901','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464053,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1403','1栋','1-2-1403','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464085,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1905','1栋','1-2-1905','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463894,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-606','1栋','1-1-606','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463926,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1202','1栋','1-1-1202','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463958,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1704','1栋','1-1-1704','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463990,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-306','1栋','1-2-306','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464022,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-902','1栋','1-2-902','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464054,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1404','1栋','1-2-1404','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464086,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1906','1栋','1-2-1906','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463895,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-701','1栋','1-1-701','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463927,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1203','1栋','1-1-1203','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463959,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1705','1栋','1-1-1705','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463991,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-401','1栋','1-2-401','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464023,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-903','1栋','1-2-903','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464055,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1405','1栋','1-2-1405','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464087,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-2001','1栋','1-2-2001','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463896,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-702','1栋','1-1-702','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463928,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1204','1栋','1-1-1204','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463960,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1706','1栋','1-1-1706','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463992,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-402','1栋','1-2-402','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464024,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-904','1栋','1-2-904','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464056,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1406','1栋','1-2-1406','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464088,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-2002','1栋','1-2-2002','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463865,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-201','1栋','1-1-201','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463897,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-703','1栋','1-1-703','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463929,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1205','1栋','1-1-1205','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463961,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1801','1栋','1-1-1801','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463993,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-403','1栋','1-2-403','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464025,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-905','1栋','1-2-905','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464057,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1501','1栋','1-2-1501','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464089,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-2003','1栋','1-2-2003','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463866,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-202','1栋','1-1-202','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463898,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-704','1栋','1-1-704','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463930,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1206','1栋','1-1-1206','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463962,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1802','1栋','1-1-1802','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463994,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-404','1栋','1-2-404','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464026,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-906','1栋','1-2-906','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464058,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1502','1栋','1-2-1502','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464090,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-2004','1栋','1-2-2004','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463867,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-203','1栋','1-1-203','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463899,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-705','1栋','1-1-705','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463931,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1301','1栋','1-1-1301','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463963,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1803','1栋','1-1-1803','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463995,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-405','1栋','1-2-405','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464027,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1001','1栋','1-2-1001','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464059,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1503','1栋','1-2-1503','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464091,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-2005','1栋','1-2-2005','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463868,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-204','1栋','1-1-204','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463900,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-706','1栋','1-1-706','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463932,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1302','1栋','1-1-1302','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463964,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1804','1栋','1-1-1804','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463996,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-406','1栋','1-2-406','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464028,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1002','1栋','1-2-1002','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464060,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1504','1栋','1-2-1504','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464092,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-2006','1栋','1-2-2006','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463869,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-205','1栋','1-1-205','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463901,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-801','1栋','1-1-801','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463933,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1303','1栋','1-1-1303','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463965,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1805','1栋','1-1-1805','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463997,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-501','1栋','1-2-501','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464029,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1003','1栋','1-2-1003','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464061,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1505','1栋','1-2-1505','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463870,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-206','1栋','1-1-206','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463902,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-802','1栋','1-1-802','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463934,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1304','1栋','1-1-1304','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463966,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1806','1栋','1-1-1806','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463998,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-502','1栋','1-2-502','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464030,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1004','1栋','1-2-1004','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464062,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1506','1栋','1-2-1506','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463871,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-301','1栋','1-1-301','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463903,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-803','1栋','1-1-803','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463935,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1305','1栋','1-1-1305','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463967,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1901','1栋','1-1-1901','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463999,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-503','1栋','1-2-503','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464031,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1005','1栋','1-2-1005','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464063,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1601','1栋','1-2-1601','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463872,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-302','1栋','1-1-302','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463904,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-804','1栋','1-1-804','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463936,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1306','1栋','1-1-1306','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463968,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1902','1栋','1-1-1902','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464000,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-504','1栋','1-2-504','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464032,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1006','1栋','1-2-1006','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464064,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1602','1栋','1-2-1602','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463873,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-303','1栋','1-1-303','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463905,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-805','1栋','1-1-805','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463937,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1401','1栋','1-1-1401','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463969,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1903','1栋','1-1-1903','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464001,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-505','1栋','1-2-505','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464033,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1101','1栋','1-2-1101','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464065,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1603','1栋','1-2-1603','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463874,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-304','1栋','1-1-304','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463906,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-806','1栋','1-1-806','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463938,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1402','1栋','1-1-1402','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463970,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1904','1栋','1-1-1904','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464002,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-506','1栋','1-2-506','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464034,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1102','1栋','1-2-1102','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464066,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1604','1栋','1-2-1604','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463875,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-305','1栋','1-1-305','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463907,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-901','1栋','1-1-901','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463939,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1403','1栋','1-1-1403','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463971,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1905','1栋','1-1-1905','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464003,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-601','1栋','1-2-601','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464035,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1103','1栋','1-2-1103','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464067,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1605','1栋','1-2-1605','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463876,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-306','1栋','1-1-306','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463908,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-902','1栋','1-1-902','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463940,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1404','1栋','1-1-1404','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463972,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1906','1栋','1-1-1906','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464004,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-602','1栋','1-2-602','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464036,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1104','1栋','1-2-1104','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464068,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1606','1栋','1-2-1606','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463877,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-401','1栋','1-1-401','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463909,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-903','1栋','1-1-903','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463941,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1405','1栋','1-1-1405','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463973,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-2001','1栋','1-1-2001','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464005,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-603','1栋','1-2-603','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464037,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1105','1栋','1-2-1105','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464069,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1701','1栋','1-2-1701','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463878,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-402','1栋','1-1-402','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463910,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-904','1栋','1-1-904','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463942,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1406','1栋','1-1-1406','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463974,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-2002','1栋','1-1-2002','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464006,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-604','1栋','1-2-604','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464038,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1106','1栋','1-2-1106','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464070,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1702','1栋','1-2-1702','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463879,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-403','1栋','1-1-403','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463911,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-905','1栋','1-1-905','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463943,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-1501','1栋','1-1-1501','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387463975,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-1-2003','1栋','1-1-2003','2','0',UTC_TIMESTAMP(),999956,68.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464007,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-605','1栋','1-2-605','2','0',UTC_TIMESTAMP(),999956,42.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464039,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1201','1栋','1-2-1201','2','0',UTC_TIMESTAMP(),999956,41.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387464071,UUID(),240111044332060120,18485,'成都市',18486,'金牛区','1栋-1-2-1703','1栋','1-2-1703','2','0',UTC_TIMESTAMP(),999956,68.15);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86985,1040330,240111044332060120,239825274387463880,'1栋-1-1-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87017,1040330,240111044332060120,239825274387463912,'1栋-1-1-906',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87049,1040330,240111044332060120,239825274387463944,'1栋-1-1-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87081,1040330,240111044332060120,239825274387463976,'1栋-1-1-2004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87113,1040330,240111044332060120,239825274387464008,'1栋-1-2-606',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87145,1040330,240111044332060120,239825274387464040,'1栋-1-2-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87177,1040330,240111044332060120,239825274387464072,'1栋-1-2-1704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86986,1040330,240111044332060120,239825274387463881,'1栋-1-1-405',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87018,1040330,240111044332060120,239825274387463913,'1栋-1-1-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87050,1040330,240111044332060120,239825274387463945,'1栋-1-1-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87082,1040330,240111044332060120,239825274387463977,'1栋-1-1-2005',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87114,1040330,240111044332060120,239825274387464009,'1栋-1-2-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87146,1040330,240111044332060120,239825274387464041,'1栋-1-2-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87178,1040330,240111044332060120,239825274387464073,'1栋-1-2-1705',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86987,1040330,240111044332060120,239825274387463882,'1栋-1-1-406',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87019,1040330,240111044332060120,239825274387463914,'1栋-1-1-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87051,1040330,240111044332060120,239825274387463946,'1栋-1-1-1504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87083,1040330,240111044332060120,239825274387463978,'1栋-1-1-2006',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87115,1040330,240111044332060120,239825274387464010,'1栋-1-2-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87147,1040330,240111044332060120,239825274387464042,'1栋-1-2-1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87179,1040330,240111044332060120,239825274387464074,'1栋-1-2-1706',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86988,1040330,240111044332060120,239825274387463883,'1栋-1-1-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87020,1040330,240111044332060120,239825274387463915,'1栋-1-1-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87052,1040330,240111044332060120,239825274387463947,'1栋-1-1-1505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87084,1040330,240111044332060120,239825274387463979,'1栋-1-2-201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87116,1040330,240111044332060120,239825274387464011,'1栋-1-2-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87148,1040330,240111044332060120,239825274387464043,'1栋-1-2-1205',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87180,1040330,240111044332060120,239825274387464075,'1栋-1-2-1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86989,1040330,240111044332060120,239825274387463884,'1栋-1-1-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87021,1040330,240111044332060120,239825274387463916,'1栋-1-1-1004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87053,1040330,240111044332060120,239825274387463948,'1栋-1-1-1506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87085,1040330,240111044332060120,239825274387463980,'1栋-1-2-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87117,1040330,240111044332060120,239825274387464012,'1栋-1-2-704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87149,1040330,240111044332060120,239825274387464044,'1栋-1-2-1206',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87181,1040330,240111044332060120,239825274387464076,'1栋-1-2-1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86990,1040330,240111044332060120,239825274387463885,'1栋-1-1-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87022,1040330,240111044332060120,239825274387463917,'1栋-1-1-1005',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87054,1040330,240111044332060120,239825274387463949,'1栋-1-1-1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87086,1040330,240111044332060120,239825274387463981,'1栋-1-2-203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87118,1040330,240111044332060120,239825274387464013,'1栋-1-2-705',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87150,1040330,240111044332060120,239825274387464045,'1栋-1-2-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87182,1040330,240111044332060120,239825274387464077,'1栋-1-2-1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86991,1040330,240111044332060120,239825274387463886,'1栋-1-1-504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87023,1040330,240111044332060120,239825274387463918,'1栋-1-1-1006',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87055,1040330,240111044332060120,239825274387463950,'1栋-1-1-1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87087,1040330,240111044332060120,239825274387463982,'1栋-1-2-204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87119,1040330,240111044332060120,239825274387464014,'1栋-1-2-706',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87151,1040330,240111044332060120,239825274387464046,'1栋-1-2-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87183,1040330,240111044332060120,239825274387464078,'1栋-1-2-1804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86992,1040330,240111044332060120,239825274387463887,'1栋-1-1-505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87024,1040330,240111044332060120,239825274387463919,'1栋-1-1-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87056,1040330,240111044332060120,239825274387463951,'1栋-1-1-1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87088,1040330,240111044332060120,239825274387463983,'1栋-1-2-205',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87120,1040330,240111044332060120,239825274387464015,'1栋-1-2-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87152,1040330,240111044332060120,239825274387464047,'1栋-1-2-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87184,1040330,240111044332060120,239825274387464079,'1栋-1-2-1805',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86993,1040330,240111044332060120,239825274387463888,'1栋-1-1-506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87025,1040330,240111044332060120,239825274387463920,'1栋-1-1-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87057,1040330,240111044332060120,239825274387463952,'1栋-1-1-1604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87089,1040330,240111044332060120,239825274387463984,'1栋-1-2-206',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87121,1040330,240111044332060120,239825274387464016,'1栋-1-2-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87153,1040330,240111044332060120,239825274387464048,'1栋-1-2-1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87185,1040330,240111044332060120,239825274387464080,'1栋-1-2-1806',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86994,1040330,240111044332060120,239825274387463889,'1栋-1-1-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87026,1040330,240111044332060120,239825274387463921,'1栋-1-1-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87058,1040330,240111044332060120,239825274387463953,'1栋-1-1-1605',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87090,1040330,240111044332060120,239825274387463985,'1栋-1-2-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87122,1040330,240111044332060120,239825274387464017,'1栋-1-2-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87154,1040330,240111044332060120,239825274387464049,'1栋-1-2-1305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87186,1040330,240111044332060120,239825274387464081,'1栋-1-2-1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86995,1040330,240111044332060120,239825274387463890,'1栋-1-1-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87027,1040330,240111044332060120,239825274387463922,'1栋-1-1-1104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87059,1040330,240111044332060120,239825274387463954,'1栋-1-1-1606',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87091,1040330,240111044332060120,239825274387463986,'1栋-1-2-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87123,1040330,240111044332060120,239825274387464018,'1栋-1-2-804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87155,1040330,240111044332060120,239825274387464050,'1栋-1-2-1306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87187,1040330,240111044332060120,239825274387464082,'1栋-1-2-1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86996,1040330,240111044332060120,239825274387463891,'1栋-1-1-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87028,1040330,240111044332060120,239825274387463923,'1栋-1-1-1105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87060,1040330,240111044332060120,239825274387463955,'1栋-1-1-1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87092,1040330,240111044332060120,239825274387463987,'1栋-1-2-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87124,1040330,240111044332060120,239825274387464019,'1栋-1-2-805',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87156,1040330,240111044332060120,239825274387464051,'1栋-1-2-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87188,1040330,240111044332060120,239825274387464083,'1栋-1-2-1903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86997,1040330,240111044332060120,239825274387463892,'1栋-1-1-604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87029,1040330,240111044332060120,239825274387463924,'1栋-1-1-1106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87061,1040330,240111044332060120,239825274387463956,'1栋-1-1-1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87093,1040330,240111044332060120,239825274387463988,'1栋-1-2-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87125,1040330,240111044332060120,239825274387464020,'1栋-1-2-806',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87157,1040330,240111044332060120,239825274387464052,'1栋-1-2-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87189,1040330,240111044332060120,239825274387464084,'1栋-1-2-1904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86998,1040330,240111044332060120,239825274387463893,'1栋-1-1-605',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87030,1040330,240111044332060120,239825274387463925,'1栋-1-1-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87062,1040330,240111044332060120,239825274387463957,'1栋-1-1-1703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87094,1040330,240111044332060120,239825274387463989,'1栋-1-2-305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87126,1040330,240111044332060120,239825274387464021,'1栋-1-2-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87158,1040330,240111044332060120,239825274387464053,'1栋-1-2-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87190,1040330,240111044332060120,239825274387464085,'1栋-1-2-1905',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86999,1040330,240111044332060120,239825274387463894,'1栋-1-1-606',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87031,1040330,240111044332060120,239825274387463926,'1栋-1-1-1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87063,1040330,240111044332060120,239825274387463958,'1栋-1-1-1704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87095,1040330,240111044332060120,239825274387463990,'1栋-1-2-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87127,1040330,240111044332060120,239825274387464022,'1栋-1-2-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87159,1040330,240111044332060120,239825274387464054,'1栋-1-2-1404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87191,1040330,240111044332060120,239825274387464086,'1栋-1-2-1906',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87000,1040330,240111044332060120,239825274387463895,'1栋-1-1-701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87032,1040330,240111044332060120,239825274387463927,'1栋-1-1-1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87064,1040330,240111044332060120,239825274387463959,'1栋-1-1-1705',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87096,1040330,240111044332060120,239825274387463991,'1栋-1-2-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87128,1040330,240111044332060120,239825274387464023,'1栋-1-2-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87160,1040330,240111044332060120,239825274387464055,'1栋-1-2-1405',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87192,1040330,240111044332060120,239825274387464087,'1栋-1-2-2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87001,1040330,240111044332060120,239825274387463896,'1栋-1-1-702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87033,1040330,240111044332060120,239825274387463928,'1栋-1-1-1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87065,1040330,240111044332060120,239825274387463960,'1栋-1-1-1706',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87097,1040330,240111044332060120,239825274387463992,'1栋-1-2-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87129,1040330,240111044332060120,239825274387464024,'1栋-1-2-904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87161,1040330,240111044332060120,239825274387464056,'1栋-1-2-1406',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87193,1040330,240111044332060120,239825274387464088,'1栋-1-2-2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86970,1040330,240111044332060120,239825274387463865,'1栋-1-1-201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87002,1040330,240111044332060120,239825274387463897,'1栋-1-1-703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87034,1040330,240111044332060120,239825274387463929,'1栋-1-1-1205',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87066,1040330,240111044332060120,239825274387463961,'1栋-1-1-1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87098,1040330,240111044332060120,239825274387463993,'1栋-1-2-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87130,1040330,240111044332060120,239825274387464025,'1栋-1-2-905',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87162,1040330,240111044332060120,239825274387464057,'1栋-1-2-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87194,1040330,240111044332060120,239825274387464089,'1栋-1-2-2003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86971,1040330,240111044332060120,239825274387463866,'1栋-1-1-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87003,1040330,240111044332060120,239825274387463898,'1栋-1-1-704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87035,1040330,240111044332060120,239825274387463930,'1栋-1-1-1206',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87067,1040330,240111044332060120,239825274387463962,'1栋-1-1-1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87099,1040330,240111044332060120,239825274387463994,'1栋-1-2-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87131,1040330,240111044332060120,239825274387464026,'1栋-1-2-906',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87163,1040330,240111044332060120,239825274387464058,'1栋-1-2-1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87195,1040330,240111044332060120,239825274387464090,'1栋-1-2-2004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86972,1040330,240111044332060120,239825274387463867,'1栋-1-1-203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87004,1040330,240111044332060120,239825274387463899,'1栋-1-1-705',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87036,1040330,240111044332060120,239825274387463931,'1栋-1-1-1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87068,1040330,240111044332060120,239825274387463963,'1栋-1-1-1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87100,1040330,240111044332060120,239825274387463995,'1栋-1-2-405',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87132,1040330,240111044332060120,239825274387464027,'1栋-1-2-1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87164,1040330,240111044332060120,239825274387464059,'1栋-1-2-1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87196,1040330,240111044332060120,239825274387464091,'1栋-1-2-2005',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86973,1040330,240111044332060120,239825274387463868,'1栋-1-1-204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87005,1040330,240111044332060120,239825274387463900,'1栋-1-1-706',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87037,1040330,240111044332060120,239825274387463932,'1栋-1-1-1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87069,1040330,240111044332060120,239825274387463964,'1栋-1-1-1804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87101,1040330,240111044332060120,239825274387463996,'1栋-1-2-406',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87133,1040330,240111044332060120,239825274387464028,'1栋-1-2-1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87165,1040330,240111044332060120,239825274387464060,'1栋-1-2-1504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87197,1040330,240111044332060120,239825274387464092,'1栋-1-2-2006',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86974,1040330,240111044332060120,239825274387463869,'1栋-1-1-205',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87006,1040330,240111044332060120,239825274387463901,'1栋-1-1-801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87038,1040330,240111044332060120,239825274387463933,'1栋-1-1-1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87070,1040330,240111044332060120,239825274387463965,'1栋-1-1-1805',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87102,1040330,240111044332060120,239825274387463997,'1栋-1-2-501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87134,1040330,240111044332060120,239825274387464029,'1栋-1-2-1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87166,1040330,240111044332060120,239825274387464061,'1栋-1-2-1505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86975,1040330,240111044332060120,239825274387463870,'1栋-1-1-206',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87007,1040330,240111044332060120,239825274387463902,'1栋-1-1-802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87039,1040330,240111044332060120,239825274387463934,'1栋-1-1-1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87071,1040330,240111044332060120,239825274387463966,'1栋-1-1-1806',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87103,1040330,240111044332060120,239825274387463998,'1栋-1-2-502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87135,1040330,240111044332060120,239825274387464030,'1栋-1-2-1004',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87167,1040330,240111044332060120,239825274387464062,'1栋-1-2-1506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86976,1040330,240111044332060120,239825274387463871,'1栋-1-1-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87008,1040330,240111044332060120,239825274387463903,'1栋-1-1-803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87040,1040330,240111044332060120,239825274387463935,'1栋-1-1-1305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87072,1040330,240111044332060120,239825274387463967,'1栋-1-1-1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87104,1040330,240111044332060120,239825274387463999,'1栋-1-2-503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87136,1040330,240111044332060120,239825274387464031,'1栋-1-2-1005',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87168,1040330,240111044332060120,239825274387464063,'1栋-1-2-1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86977,1040330,240111044332060120,239825274387463872,'1栋-1-1-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87009,1040330,240111044332060120,239825274387463904,'1栋-1-1-804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87041,1040330,240111044332060120,239825274387463936,'1栋-1-1-1306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87073,1040330,240111044332060120,239825274387463968,'1栋-1-1-1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87105,1040330,240111044332060120,239825274387464000,'1栋-1-2-504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87137,1040330,240111044332060120,239825274387464032,'1栋-1-2-1006',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87169,1040330,240111044332060120,239825274387464064,'1栋-1-2-1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86978,1040330,240111044332060120,239825274387463873,'1栋-1-1-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87010,1040330,240111044332060120,239825274387463905,'1栋-1-1-805',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87042,1040330,240111044332060120,239825274387463937,'1栋-1-1-1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87074,1040330,240111044332060120,239825274387463969,'1栋-1-1-1903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87106,1040330,240111044332060120,239825274387464001,'1栋-1-2-505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87138,1040330,240111044332060120,239825274387464033,'1栋-1-2-1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87170,1040330,240111044332060120,239825274387464065,'1栋-1-2-1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86979,1040330,240111044332060120,239825274387463874,'1栋-1-1-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87011,1040330,240111044332060120,239825274387463906,'1栋-1-1-806',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87043,1040330,240111044332060120,239825274387463938,'1栋-1-1-1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87075,1040330,240111044332060120,239825274387463970,'1栋-1-1-1904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87107,1040330,240111044332060120,239825274387464002,'1栋-1-2-506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87139,1040330,240111044332060120,239825274387464034,'1栋-1-2-1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87171,1040330,240111044332060120,239825274387464066,'1栋-1-2-1604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86980,1040330,240111044332060120,239825274387463875,'1栋-1-1-305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87012,1040330,240111044332060120,239825274387463907,'1栋-1-1-901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87044,1040330,240111044332060120,239825274387463939,'1栋-1-1-1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87076,1040330,240111044332060120,239825274387463971,'1栋-1-1-1905',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87108,1040330,240111044332060120,239825274387464003,'1栋-1-2-601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87140,1040330,240111044332060120,239825274387464035,'1栋-1-2-1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87172,1040330,240111044332060120,239825274387464067,'1栋-1-2-1605',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86981,1040330,240111044332060120,239825274387463876,'1栋-1-1-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87013,1040330,240111044332060120,239825274387463908,'1栋-1-1-902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87045,1040330,240111044332060120,239825274387463940,'1栋-1-1-1404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87077,1040330,240111044332060120,239825274387463972,'1栋-1-1-1906',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87109,1040330,240111044332060120,239825274387464004,'1栋-1-2-602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87141,1040330,240111044332060120,239825274387464036,'1栋-1-2-1104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87173,1040330,240111044332060120,239825274387464068,'1栋-1-2-1606',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86982,1040330,240111044332060120,239825274387463877,'1栋-1-1-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87014,1040330,240111044332060120,239825274387463909,'1栋-1-1-903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87046,1040330,240111044332060120,239825274387463941,'1栋-1-1-1405',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87078,1040330,240111044332060120,239825274387463973,'1栋-1-1-2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87110,1040330,240111044332060120,239825274387464005,'1栋-1-2-603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87142,1040330,240111044332060120,239825274387464037,'1栋-1-2-1105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87174,1040330,240111044332060120,239825274387464069,'1栋-1-2-1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86983,1040330,240111044332060120,239825274387463878,'1栋-1-1-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87015,1040330,240111044332060120,239825274387463910,'1栋-1-1-904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87047,1040330,240111044332060120,239825274387463942,'1栋-1-1-1406',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87079,1040330,240111044332060120,239825274387463974,'1栋-1-1-2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87111,1040330,240111044332060120,239825274387464006,'1栋-1-2-604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87143,1040330,240111044332060120,239825274387464038,'1栋-1-2-1106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87175,1040330,240111044332060120,239825274387464070,'1栋-1-2-1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (86984,1040330,240111044332060120,239825274387463879,'1栋-1-1-403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87016,1040330,240111044332060120,239825274387463911,'1栋-1-1-905',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87048,1040330,240111044332060120,239825274387463943,'1栋-1-1-1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87080,1040330,240111044332060120,239825274387463975,'1栋-1-1-2003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87112,1040330,240111044332060120,239825274387464007,'1栋-1-2-605',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87144,1040330,240111044332060120,239825274387464039,'1栋-1-2-1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (87176,1040330,240111044332060120,239825274387464071,'1栋-1-2-1703',4);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125297,60100,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125298,60200,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125209,10000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125210,10100,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125211,10400,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125212,10200,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125213,10600,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125214,10800,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125215,13000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125216,10850,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125217,10900,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125218,11000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125219,12200,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125220,20000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125221,20100,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125222,20140,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125223,20150,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125224,20155,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125225,20158,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125226,20170,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125227,20180,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125228,20190,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125229,20191,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125230,20230,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125231,20220,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125232,20240,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125233,20250,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125234,20255,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125235,20258,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125236,20280,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125237,20290,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125238,20291,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125239,40000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125240,40050,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125241,40100,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125242,40103,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125243,40105,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125244,40110,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125245,40120,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125246,40130,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125247,40300,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125248,40400,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125249,40410,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125250,40420,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125251,40430,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125252,40440,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125253,40450,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125254,40500,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125255,40700,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125256,40710,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125257,40720,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125258,40800,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125259,40830,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125260,40835,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125261,40810,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125262,40840,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125263,40850,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125264,40900,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125265,41000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125266,41010,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125267,41020,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125268,41030,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125269,41040,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125270,41050,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125271,41060,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125272,41100,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125273,41500,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125274,41300,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125275,41310,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125276,41330,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125277,41320,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125278,30000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125279,30500,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125280,30550,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125281,31000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125282,32000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125283,34000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125284,35000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125285,30600,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125286,37000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125287,50000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125288,50100,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125289,50300,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125290,50400,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125291,50500,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125292,70000,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125293,70300,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125294,70100,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125295,70200,NULL,'EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125296,60000,NULL,'EhNamespaces',999956,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001316,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999956,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001317,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999956,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001318,'',0,1001318,1,'活动管理-默认子分类','/1/1001318',0,2,1,NOW(),0,NULL,999956,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001319,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999956,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001320,'',0,1001320,2,'活动管理二-默认子分类','/2/1001320',0,2,1,NOW(),0,NULL,999956,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001321,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999956,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001322,'',0,1001322,3,'活动管理三-默认子分类','/3/1001322',0,2,1,NOW(),0,NULL,999956,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (439,999956,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (440,999956,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (441,999956,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1040330,240111044332060120);

    
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1014,999956,'ServiceMarketLayout','{"versionCode":"2017120801","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":40,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2017120801,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1015,999956,'ServiceMarketLayout','{"versionCode":"2017120801","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":40,"separatorFlag":1,"separatorHeight":16,"columnCount":4},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2017120801,0,2,UTC_TIMESTAMP(),'default',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216860,999956,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRwaVl6WmpaV1ZrWmpjMk1EVXlaVEkwWmpSbVltTmlNR016WkRaa05qWmpZdw',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216861,999956,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRwaVl6WmpaV1ZrWmpjMk1EVXlaVEkwWmpSbVltTmlNR016WkRaa05qWmpZdw',0,NULL,2,10,0,UTC_TIMESTAMP(),'default');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147301,999956,0,0,0,'/home','NavigatorGroup2','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRwbVl6WTBNMkk0TmpFNU0yUTJOREpqTlRGbFpUYzRNek5pWTJGbVptRTNaUQ',1,1,45,'',10,0,1,1,0,1,'pm_admin',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147302,999956,0,0,0,'/home','NavigatorGroup2','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRwbVl6WTBNMkk0TmpFNU0yUTJOREpqTlRGbFpUYzRNek5pWTJGbVptRTNaUQ',1,1,45,'',10,0,1,1,0,1,'default',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147303,999956,0,0,0,'/home','NavigatorGroup2','报修','报修','cs://1/image/aW1hZ2UvTVRwak16bGtaV1JoWkRGaU9EWTVOelV5TW1ReE9HTTFaVFpoWVRjNVpUbGlaUQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=报修"}',20,0,1,1,0,1,'pm_admin',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147304,999956,0,0,0,'/home','NavigatorGroup2','报修','报修','cs://1/image/aW1hZ2UvTVRwak16bGtaV1JoWkRGaU9EWTVOelV5TW1ReE9HTTFaVFpoWVRjNVpUbGlaUQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=报修"}',20,0,1,1,0,1,'default',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147305,999956,0,0,0,'/home','NavigatorGroup2','社区介绍','社区介绍','cs://1/image/aW1hZ2UvTVRvMU9EY3daRE5qTldGaU4ySTNPREZsWTJKak9XTmpOV0V5TW1Gak4yVXpNQQ',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',30,0,1,1,0,1,'pm_admin',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147306,999956,0,0,0,'/home','NavigatorGroup2','社区介绍','社区介绍','cs://1/image/aW1hZ2UvTVRvMU9EY3daRE5qTldGaU4ySTNPREZsWTJKak9XTmpOV0V5TW1Gak4yVXpNQQ',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',30,0,1,1,0,1,'default',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147307,999956,0,0,0,'/home','NavigatorGroup2','快递','快递','cs://1/image/aW1hZ2UvTVRveVpUZ3dOVGhrTnpVME5qVm1NR0l3TkRRNE1qZ3dPRFkwWWpBME16RmxaZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',40,0,1,1,0,1,'pm_admin',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147308,999956,0,0,0,'/home','NavigatorGroup2','快递','快递','cs://1/image/aW1hZ2UvTVRveVpUZ3dOVGhrTnpVME5qVm1NR0l3TkRRNE1qZ3dPRFkwWWpBME16RmxaZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',40,0,1,1,0,1,'default',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147309,999956,0,0,0,'/home','NavigatorGroup2','家政服务','家政服务','cs://1/image/aW1hZ2UvTVRvd09HSXhZamMyTm1VNE1tUTNaalV6TnpJMk5UbGpZbVk0WVRZMk5qVXhZZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',50,0,1,1,0,1,'pm_admin',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147310,999956,0,0,0,'/home','NavigatorGroup2','家政服务','家政服务','cs://1/image/aW1hZ2UvTVRvd09HSXhZamMyTm1VNE1tUTNaalV6TnpJMk5UbGpZbVk0WVRZMk5qVXhZZw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',50,0,1,1,0,1,'default',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147311,999956,0,0,0,'/home','NavigatorGroup2','房屋租赁','房屋租赁','cs://1/image/aW1hZ2UvTVRwalpUTTFZakEwWVRCbFl6QTFObUppWW1SbFlXUXhPVGhsTTJSa1pqVXlNZw',1,1,28,'',60,0,1,1,0,1,'pm_admin',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147312,999956,0,0,0,'/home','NavigatorGroup2','房屋租赁','房屋租赁','cs://1/image/aW1hZ2UvTVRwalpUTTFZakEwWVRCbFl6QTFObUppWW1SbFlXUXhPVGhsTTJSa1pqVXlNZw',1,1,28,'',60,0,1,1,0,1,'default',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147313,999956,0,0,0,'/home','NavigatorGroup2','办事指南','办事指南','cs://1/image/aW1hZ2UvTVRvMlpUWmtZelkxTlRKbVpqUTNNamczTVdReVl6azRZbUl4WXpFNU16QTVaUQ',1,1,33,'{"type":212950,"parentId":212950,"displayType": "list"}',70,0,1,1,0,1,'pm_admin',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147314,999956,0,0,0,'/home','NavigatorGroup2','办事指南','办事指南','cs://1/image/aW1hZ2UvTVRvMlpUWmtZelkxTlRKbVpqUTNNamczTVdReVl6azRZbUl4WXpFNU16QTVaUQ',1,1,33,'{"type":212950,"parentId":212950,"displayType": "list"}',70,0,1,1,0,1,'default',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147315,999956,0,0,0,'/home','NavigatorGroup2','活动','活动','cs://1/image/aW1hZ2UvTVRvMFpUTXhOVGc1TWprME1UZzRObVF4TW1JME0yTTFNak0zWmpZNE0ySXhNZw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":2,"style":4,"title": "活动"}',80,0,1,0,0,1,'pm_admin',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147316,999956,0,0,0,'/home','NavigatorGroup2','活动','活动','cs://1/image/aW1hZ2UvTVRvMFpUTXhOVGc1TWprME1UZzRObVF4TW1JME0yTTFNak0zWmpZNE0ySXhNZw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":2,"style":4,"title": "活动"}',80,0,1,0,0,1,'default',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147317,999956,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRwaE9XVXpOREUzTW1Wa1lUUm1PV1JtTXpoaU1UQmlaR1V4T1RNNFpqTmtOUQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'pm_admin',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147318,999956,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRwaE9XVXpOREUzTW1Wa1lUUm1PV1JtTXpoaU1UQmlaR1V4T1RNNFpqTmtOUQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'default',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147319,999956,0,0,0,'/home','NavigatorGroup5','党群管理','党群管理','cs://1/image/aW1hZ2UvTVRvM05HSmlNRGN3TlRreVpqWmxaV0V6T1RBek16UmpNV00wTldJNE5HWTNOQQ',1,1,33,'{"type":212951,"parentId":212951,"displayType": "grid"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147320,999956,0,0,0,'/home','NavigatorGroup5','党群管理','党群管理','cs://1/image/aW1hZ2UvTVRvM05HSmlNRGN3TlRreVpqWmxaV0V6T1RBek16UmpNV00wTldJNE5HWTNOQQ',1,1,33,'{"type":212951,"parentId":212951,"displayType": "grid"}',10,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147321,999956,0,0,0,'/home','NavigatorGroup5','城市管理','城市管理','cs://1/image/aW1hZ2UvTVRvNFpXWTVZVGcwWWpRME1EUXhOV1prWmpSbU5UWmxaakUzWm1VMk5EZGhaUQ',1,1,33,'{"type":212952,"parentId":212952,"displayType": "grid"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147322,999956,0,0,0,'/home','NavigatorGroup5','城市管理','城市管理','cs://1/image/aW1hZ2UvTVRvNFpXWTVZVGcwWWpRME1EUXhOV1prWmpSbU5UWmxaakUzWm1VMk5EZGhaUQ',1,1,33,'{"type":212952,"parentId":212952,"displayType": "grid"}',20,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147323,999956,0,0,0,'/home','NavigatorGroup5','教育','教育','cs://1/image/aW1hZ2UvTVRvNU1UazFOVFJtTlRCa00ySTFNVFZsTjJNMk4yWmlPVGRsWldWa01qTXhNdw',1,1,33,'{"type":212953,"parentId":212953,"displayType": "grid"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147324,999956,0,0,0,'/home','NavigatorGroup5','教育','教育','cs://1/image/aW1hZ2UvTVRvNU1UazFOVFJtTlRCa00ySTFNVFZsTjJNMk4yWmlPVGRsWldWa01qTXhNdw',1,1,33,'{"type":212953,"parentId":212953,"displayType": "grid"}',40,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147325,999956,0,0,0,'/home','NavigatorGroup5','民生','民生','cs://1/image/aW1hZ2UvTVRwaE5qYzFOVGt4WXprMk5HWTVNelV3TlRVd1kyVmpNVFE0TW1ReVpUVmxOUQ',1,1,33,'{"type":212954,"parentId":212954,"displayType": "grid"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (147326,999956,0,0,0,'/home','NavigatorGroup5','民生','民生','cs://1/image/aW1hZ2UvTVRwaE5qYzFOVGt4WXprMk5HWTVNelV3TlRVd1kyVmpNVFE0TW1ReVpUVmxOUQ',1,1,33,'{"type":212954,"parentId":212954,"displayType": "grid"}',30,0,1,1,0,0,'default',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212950,'community','240111044331055940',0,'办事指南','办事指南',0,2,1,UTC_TIMESTAMP(),999956,1);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212951,'community','240111044331055940',0,'党群管理','党群管理',0,2,1,UTC_TIMESTAMP(),999956,2);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212952,'community','240111044331055940',0,'城市管理','城市管理',0,2,1,UTC_TIMESTAMP(),999956,3);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212953,'community','240111044331055940',0,'教育','教育',0,2,1,UTC_TIMESTAMP(),999956,4);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212954,'community','240111044331055940',0,'民生','民生',0,2,1,UTC_TIMESTAMP(),999956,5);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211590,0,'organaization',1040330,'all','办事指南','办事指南',212950,2,211590,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211591,0,'organaization',1040330,'all','党群管理','党群管理',212951,2,211591,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211592,0,'organaization',1040330,'all','城市管理','城市管理',212952,2,211592,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211593,0,'organaization',1040330,'all','教育','教育',212953,2,211593,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211594,0,'organaization',1040330,'all','民生','民生',212954,2,211594,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125338,41700,'办事指南','EhNamespaces',999956,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125339,41710,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125340,41720,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125341,41730,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125342,41740,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125343,41750,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125344,41760,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125345,41800,'党群管理','EhNamespaces',999956,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125346,41810,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125347,41820,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125348,41830,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125349,41840,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125350,41850,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125351,41860,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125352,41900,'城市管理','EhNamespaces',999956,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125353,41910,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125354,41920,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125355,41930,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125356,41940,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125357,41950,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125358,41960,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125359,42000,'教育','EhNamespaces',999956,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125360,42010,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125361,42020,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125362,42030,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125363,42040,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125364,42050,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125365,42060,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125366,42100,'民生','EhNamespaces',999956,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125367,42110,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125368,42120,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125369,42130,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125370,42140,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125371,42150,'','EhNamespaces',999956,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (125372,42160,'','EhNamespaces',999956,2);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (203992,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999956);
