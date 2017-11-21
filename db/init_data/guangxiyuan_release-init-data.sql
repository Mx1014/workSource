-- guangxingyuan by ljs
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1864,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999958','the relative path for guangxiyuan app agreements',999958,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1865,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999958,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1866,'pmtask.handler-999958','flow','0',999958,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1867,'pay.platform','1','支付类型',999958,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (423,'Android_GuangXingYuanPark',NULL,UTC_TIMESTAMP(),999958);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (424,'IOS_GuangXingYuanPark',NULL,UTC_TIMESTAMP(),999958);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (746,423,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999958);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (747,424,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999958);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999958,'创意园');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1428,999958,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (856,'sms.default.sign',0,'zh_CN','创意园','【创意园】',999958);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18367,0,'广东','GUANGDONG','GD','/广东',1,1,NULL,NULL,2,0,999958);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18368,18367,'深圳市','SHENZHENSHI','SZS','/广东/深圳市',2,2,NULL,'0755',2,1,999958);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18369,18368,'宝安区','BAOANQU','BAQ','/广东/深圳市/宝安区',3,3,NULL,'0755',2,0,999958);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060016,UUID(),18368,'深圳市',18369,'宝安区','创意园','','兴业路2005号','广兴源互联网创意园位于深圳市宝安区西乡街道兴业路2005号，周边配套设施完善。园区毗邻前海自贸区，沿江高速公路、大铲湾港口和宝安国际机场，地理位置十分优越。
园区孵化场地面积约50000多平方米，主要致力于打造成集聚互联网、创意产业链关联企业的专业孵化培育基地。企业孵化服务管理团队共计16人，其中，6人拥有科技企业孵化器从业人员资格证，主要负责引入科技服务资源、协调各项孵化服务活动的开展和提供科技咨询辅导和创业辅导等服务。
我们从单一的办公功能扩展到综合性的集办公、文化、生活为一体的创业孵化基地。园区内共有停车位三百多个，有都市快餐、便利店、桌球室等休闲场地及配套酒店等商务接待场所。另设有面向全部入孵企业提供配套服务的公共孵化服务场地，包括大型多功能商务会议厅、培训活动室等。
园区于2015年被区科创委授予了宝安区科技创新园互联网创意专业园区称号，又于2016年被市科创委认定为市级科技孵化器。',0,1,2,UTC_TIMESTAMP(),1,192609,192610,UTC_TIMESTAMP(),999958);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092789,240111044332060016,'',113.873748,22.564906,'ws0br7b64ekg');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20126,999958,'COMMUNITY',240111044332060016,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1039143,0,'PM','深圳市国汇通物业管理有限公司','深圳市国汇通物业管理有限公司成立于2013年,公司主营业务有物业管理、自由物业租赁、房地产经纪等，目前共有员工300多人。','/1039143',1,2,'ENTERPRISE',999958,1057636);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1154343,240111044332060016,'organization',1039143,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (24423,'EhOrganizations',1039143,1,10,452765,0,1,UTC_TIMESTAMP(),999958,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (24424,'EhOrganizations',1039143,1,10,452766,0,1,UTC_TIMESTAMP(),999958,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (24425,'EhOrganizations',1039143,1,10,452767,0,1,UTC_TIMESTAMP(),999958,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1057636,UUID(),'深圳市国汇通物业管理有限公司','深圳市国汇通物业管理有限公司',1,1,1039143,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192608,1,999958);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192608,UUID(),999958,2,'EhGroups',1057636,'深圳市国汇通物业管理有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192609,UUID(),999958,2,'',0,'创意园社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192610,UUID(),999958,2,'',0,'创意园意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (452765,UUID(),'19194979670','何金辉',NULL,1,45,'1','2','zh_CN','43f9b1803f6e828ec87dbc2721ff177f','fec1c6c47842c4ccd738411fe513cb40acc8ae27742e7d4e67dce00c644cb3b6',UTC_TIMESTAMP(),999958);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (452766,UUID(),'19194979671','马舒琪',NULL,1,45,'1','2','zh_CN','952b79818465ffea1562933f233720b8','71882cc0b48f1ecec62cbd0353265c2003c45319435cb99988168119b99f89e6',UTC_TIMESTAMP(),999958);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (452767,UUID(),'19194979672','钟伟群',NULL,1,45,'1','2','zh_CN','97d5a6b56c8fbde299b2434361c3a785','e32564bc9e9216179d09f9582dbd632997a389ef3d8373b92451aa9127cf49e9',UTC_TIMESTAMP(),999958);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (28369,1039143,'USER',452765,'何金辉',0,'13570809346',UTC_TIMESTAMP(),999958);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (28370,1039143,'USER',452766,'马舒琪',0,'15118052011',UTC_TIMESTAMP(),999958);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (28371,1039143,'USER',452767,'钟伟群',0,'18279137220',UTC_TIMESTAMP(),999958);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (424373,452765,0,'13570809346',NULL,3,UTC_TIMESTAMP(),999958);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (424374,452766,0,'15118052011',NULL,3,UTC_TIMESTAMP(),999958);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (424375,452767,0,'18279137220',NULL,3,UTC_TIMESTAMP(),999958);

INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`) 
	VALUES (2177937,1039143,'USER',452765,'manager','何金辉',0,'13570809346',3,999958,'ENTERPRISE',0);
INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`) 
	VALUES (2177938,1039143,'USER',452766,'manager','马舒琪',0,'15118052011',3,999958,'ENTERPRISE',0);
INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`) 
	VALUES (2177939,1039143,'USER',452767,'manager','钟伟群',0,'18279137220',3,999958,'ENTERPRISE',0);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (28221,452765,1039143,'/1039143','ENTERPRISE',3,999958,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (28222,452766,1039143,'/1039143','ENTERPRISE',3,999958,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (28223,452767,1039143,'/1039143','ENTERPRISE',3,999958,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (114992,'EhOrganizations',1039143,'EhUsers',452765,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (114993,'EhOrganizations',1039143,'EhUsers',452766,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (114994,'EhOrganizations',1039143,'EhUsers',452767,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970872,240111044332060016,'A栋','',0,13570809346,'兴业路2005号',NULL,'113.873628','22.56516','ws0br7b5xfeq','',NULL,2,0,NULL,1,NOW(),999958,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970873,240111044332060016,'B栋','',0,13570809346,'兴业路2005号',NULL,'113.873853','22.56448','ws0br78rt5gn','',NULL,2,0,NULL,1,NOW(),999958,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970874,240111044332060016,'C栋','',0,13570809346,'兴业路2005号',NULL,'113.873671','22.564675','ws0br7b2bb73','',NULL,2,0,NULL,1,NOW(),999958,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460466,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-209-1','A栋','209-1','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460498,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-317','A栋','317','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460530,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-501','A栋','501','2','0',UTC_TIMESTAMP(),999958,227.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460562,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-610','A栋','610','2','0',UTC_TIMESTAMP(),999958,107.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460594,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-715','A栋','715','2','0',UTC_TIMESTAMP(),999958,158.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460626,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-219','C栋','219','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460658,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-325','C栋','325','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460690,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-516','C栋','516','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460467,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-209-2','A栋','209-2','2','0',UTC_TIMESTAMP(),999958,172.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460499,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-318','A栋','318','2','0',UTC_TIMESTAMP(),999958,241.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460531,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-502','A栋','502','2','0',UTC_TIMESTAMP(),999958,240.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460563,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-611','A栋','611','2','0',UTC_TIMESTAMP(),999958,168.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460595,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-716','A栋','716','2','0',UTC_TIMESTAMP(),999958,238.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460627,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-220','C栋','220','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460659,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-326','C栋','326','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460691,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-518','C栋','518','2','0',UTC_TIMESTAMP(),999958,48.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460468,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-210','A栋','210','2','0',UTC_TIMESTAMP(),999958,240.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460500,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-319','A栋','319','2','0',UTC_TIMESTAMP(),999958,269.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460532,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-503','A栋','503','2','0',UTC_TIMESTAMP(),999958,297.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460564,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-612','A栋','612','2','0',UTC_TIMESTAMP(),999958,136.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460596,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-717','A栋','717','2','0',UTC_TIMESTAMP(),999958,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460628,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-221','C栋','221','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460660,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-327','C栋','327','2','0',UTC_TIMESTAMP(),999958,59.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460692,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-601','C栋','601','2','0',UTC_TIMESTAMP(),999958,75.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460469,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-211-1','A栋','211-1','2','0',UTC_TIMESTAMP(),999958,245.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460501,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-320','A栋','320','2','0',UTC_TIMESTAMP(),999958,324.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460533,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-505','A栋','505','2','0',UTC_TIMESTAMP(),999958,305.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460565,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-613','A栋','613','2','0',UTC_TIMESTAMP(),999958,295.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460597,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-718','A栋','718','2','0',UTC_TIMESTAMP(),999958,341.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460629,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-222','C栋','222','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460661,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-328','C栋','328','2','0',UTC_TIMESTAMP(),999958,49.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460693,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-602-1','C栋','602-1','2','0',UTC_TIMESTAMP(),999958,48.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460470,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-211-2','A栋','211-2','2','0',UTC_TIMESTAMP(),999958,172.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460502,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-321','A栋','321','2','0',UTC_TIMESTAMP(),999958,297.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460534,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-506','A栋','506','2','0',UTC_TIMESTAMP(),999958,338.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460566,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-615','A栋','615','2','0',UTC_TIMESTAMP(),999958,172.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460598,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-719-1','A栋','719-1','2','0',UTC_TIMESTAMP(),999958,103.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460630,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-223','C栋','223','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460662,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-330','C栋','330','2','0',UTC_TIMESTAMP(),999958,49.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460694,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-602-2','C栋','602-2','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460471,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-212','A栋','212','2','0',UTC_TIMESTAMP(),999958,237.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460503,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-322','A栋','322','2','0',UTC_TIMESTAMP(),999958,250.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460535,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-507','A栋','507','2','0',UTC_TIMESTAMP(),999958,153.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460567,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-616','A栋','616','2','0',UTC_TIMESTAMP(),999958,133.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460599,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-719-2','A栋','719-2','2','0',UTC_TIMESTAMP(),999958,103.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460631,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-225','C栋','225','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460663,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-332','C栋','332','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460695,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-603','C栋','603','2','0',UTC_TIMESTAMP(),999958,149.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460472,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-213','A栋','213','2','0',UTC_TIMESTAMP(),999958,178.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460504,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-323','A栋','323','2','0',UTC_TIMESTAMP(),999958,199.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460536,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-508','A栋','508','2','0',UTC_TIMESTAMP(),999958,243.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460568,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-617','A栋','617','2','0',UTC_TIMESTAMP(),999958,178.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460600,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-720','A栋','720','2','0',UTC_TIMESTAMP(),999958,423.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460632,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-226','C栋','226','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460664,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-401','C栋','401','2','0',UTC_TIMESTAMP(),999958,75.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460696,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-604','C栋','604','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460473,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-215','A栋','215','2','0',UTC_TIMESTAMP(),999958,158.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460505,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-401','A栋','401','2','0',UTC_TIMESTAMP(),999958,227.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460537,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-509-1','A栋','509-1','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460569,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-618','A栋','618','2','0',UTC_TIMESTAMP(),999958,235.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460601,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-721','A栋','721','2','0',UTC_TIMESTAMP(),999958,397.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460633,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-227','C栋','227','2','0',UTC_TIMESTAMP(),999958,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460665,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-402','C栋','402','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460697,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-605','C栋','605','2','0',UTC_TIMESTAMP(),999958,142.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460474,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-216','A栋','216','2','0',UTC_TIMESTAMP(),999958,238.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460506,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-402','A栋','402','2','0',UTC_TIMESTAMP(),999958,185.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460538,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-509-2','A栋','509-2','2','0',UTC_TIMESTAMP(),999958,172.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460570,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-619','A栋','619','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460602,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-722','A栋','722','2','0',UTC_TIMESTAMP(),999958,195.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460634,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-228','C栋','228','2','0',UTC_TIMESTAMP(),999958,47.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460666,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-403','C栋','403','2','0',UTC_TIMESTAMP(),999958,105.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460698,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-606','C栋','606','2','0',UTC_TIMESTAMP(),999958,138.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460475,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-217','A栋','217','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460507,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-403','A栋','403','2','0',UTC_TIMESTAMP(),999958,297.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460539,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-510','A栋','510','2','0',UTC_TIMESTAMP(),999958,240.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460571,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-620','A栋','620','2','0',UTC_TIMESTAMP(),999958,422.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460603,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-723','A栋','723','2','0',UTC_TIMESTAMP(),999958,199.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460635,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-230','C栋','230','2','0',UTC_TIMESTAMP(),999958,47.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460667,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-405','C栋','405','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460699,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-607','C栋','607','2','0',UTC_TIMESTAMP(),999958,142.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460476,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-218','A栋','218','2','0',UTC_TIMESTAMP(),999958,241.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460508,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-405','A栋','405','2','0',UTC_TIMESTAMP(),999958,305.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460540,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-511','A栋','511','2','0',UTC_TIMESTAMP(),999958,295.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460572,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-621','A栋','621','2','0',UTC_TIMESTAMP(),999958,152.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460604,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-101','C栋','101','2','0',UTC_TIMESTAMP(),999958,179.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460636,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-232','C栋','232','2','0',UTC_TIMESTAMP(),999958,47.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460668,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-406','C栋','406','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460700,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-608','C栋','608','2','0',UTC_TIMESTAMP(),999958,138.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460477,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-219','A栋','219','2','0',UTC_TIMESTAMP(),999958,269.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460509,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-406','A栋','406','2','0',UTC_TIMESTAMP(),999958,238.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460541,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-512-1','A栋','512-1','2','0',UTC_TIMESTAMP(),999958,137.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460573,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-622','A栋','622','2','0',UTC_TIMESTAMP(),999958,330.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460605,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-102','C栋','102','2','0',UTC_TIMESTAMP(),999958,145.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460637,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-301','C栋','301','2','0',UTC_TIMESTAMP(),999958,75.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460669,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-407','C栋','407','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460701,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-609','C栋','609','2','0',UTC_TIMESTAMP(),999958,197.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460446,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-101','A栋','101','2','0',UTC_TIMESTAMP(),999958,429.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460478,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-220','A栋','220','2','0',UTC_TIMESTAMP(),999958,324.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460510,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-407','A栋','407','2','0',UTC_TIMESTAMP(),999958,153.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460542,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-512-2','A栋','512-2','2','0',UTC_TIMESTAMP(),999958,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460574,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-623','A栋','623','2','0',UTC_TIMESTAMP(),999958,105.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460606,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-103','C栋','103','2','0',UTC_TIMESTAMP(),999958,145.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460638,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-302','C栋','302','2','0',UTC_TIMESTAMP(),999958,49.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460670,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-408','C栋','408','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460702,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-610','C栋','610','2','0',UTC_TIMESTAMP(),999958,138.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460447,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-102','A栋','102','2','0',UTC_TIMESTAMP(),999958,432.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460479,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-221','A栋','221','2','0',UTC_TIMESTAMP(),999958,297.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460511,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-408','A栋','408','2','0',UTC_TIMESTAMP(),999958,143.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460543,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-513','A栋','513','2','0',UTC_TIMESTAMP(),999958,358.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460575,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-625','A栋','625','2','0',UTC_TIMESTAMP(),999958,141.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460607,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-104','C栋','104','2','0',UTC_TIMESTAMP(),999958,217.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460639,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-303','C栋','303','2','0',UTC_TIMESTAMP(),999958,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460671,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-409','C栋','409','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460703,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-612','C栋','612','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460448,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-103','A栋','103','2','0',UTC_TIMESTAMP(),999958,437.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460480,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-222','A栋','222','2','0',UTC_TIMESTAMP(),999958,250.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460512,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-409-1','A栋','409-1','2','0',UTC_TIMESTAMP(),999958,152.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460544,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-515','A栋','515','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460576,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-626','A栋','626','2','0',UTC_TIMESTAMP(),999958,221.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460608,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-105','C栋','105','2','0',UTC_TIMESTAMP(),999958,145.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460640,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-305','C栋','305','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460672,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-410','C栋','410','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460704,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-701','C栋','701','2','0',UTC_TIMESTAMP(),999958,75.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460449,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-104-1','A栋','104-1','2','0',UTC_TIMESTAMP(),999958,432.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460481,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-223','A栋','223','2','0',UTC_TIMESTAMP(),999958,199.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460513,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-409-2','A栋','409-2','2','0',UTC_TIMESTAMP(),999958,168.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460545,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-516','A栋','516','2','0',UTC_TIMESTAMP(),999958,218.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460577,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-627','A栋','627','2','0',UTC_TIMESTAMP(),999958,145.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460609,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-106','C栋','106','2','0',UTC_TIMESTAMP(),999958,178.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460641,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-306','C栋','306','2','0',UTC_TIMESTAMP(),999958,49.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460673,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-411','C栋','411','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460705,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-702','C栋','702','2','0',UTC_TIMESTAMP(),999958,138.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460450,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-104-2','A栋','104-2','2','0',UTC_TIMESTAMP(),999958,288.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460482,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-301','A栋','301','2','0',UTC_TIMESTAMP(),999958,230.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460514,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-410','A栋','410','2','0',UTC_TIMESTAMP(),999958,137.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460546,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-517','A栋','517','2','0',UTC_TIMESTAMP(),999958,152.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460578,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-629','A栋','629','2','0',UTC_TIMESTAMP(),999958,141.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460610,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-201','C栋','201','2','0',UTC_TIMESTAMP(),999958,75.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460642,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-307','C栋','307','2','0',UTC_TIMESTAMP(),999958,49.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460674,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-412','C栋','412','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460706,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-703','C栋','703','2','0',UTC_TIMESTAMP(),999958,149.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460451,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-105-1','A栋','105-1','2','0',UTC_TIMESTAMP(),999958,216.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460483,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-302','A栋','302','2','0',UTC_TIMESTAMP(),999958,216.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460515,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-411','A栋','411','2','0',UTC_TIMESTAMP(),999958,325.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460547,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-518','A栋','518','2','0',UTC_TIMESTAMP(),999958,238.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460579,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-631','A栋','631','2','0',UTC_TIMESTAMP(),999958,154.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460611,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-202','C栋','202','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460643,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-308','C栋','308','2','0',UTC_TIMESTAMP(),999958,49.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460675,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-413','C栋','413','2','0',UTC_TIMESTAMP(),999958,149.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460707,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-705','C栋','705','2','0',UTC_TIMESTAMP(),999958,142.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460452,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-105-2','A栋','105-2','2','0',UTC_TIMESTAMP(),999958,432.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460484,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-303','A栋','303','2','0',UTC_TIMESTAMP(),999958,300.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460516,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-411-2','A栋','411-2','2','0',UTC_TIMESTAMP(),999958,172.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460548,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-519','A栋','519','2','0',UTC_TIMESTAMP(),999958,219.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460580,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-701','A栋','701','2','0',UTC_TIMESTAMP(),999958,230.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460612,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-203','C栋','203','2','0',UTC_TIMESTAMP(),999958,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460644,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-309','C栋','309','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460676,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-416','C栋','416','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460708,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-706','C栋','706','2','0',UTC_TIMESTAMP(),999958,138.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460453,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-106-1','A栋','106-1','2','0',UTC_TIMESTAMP(),999958,400.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460485,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-305','A栋','305','2','0',UTC_TIMESTAMP(),999958,310.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460517,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-412','A栋','412','2','0',UTC_TIMESTAMP(),999958,233.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460549,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-520','A栋','520','2','0',UTC_TIMESTAMP(),999958,317.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460581,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-702','A栋','702','2','0',UTC_TIMESTAMP(),999958,415.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460613,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-205','C栋','205','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460645,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-310','C栋','310','2','0',UTC_TIMESTAMP(),999958,49.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460677,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-418','C栋','418','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460709,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-707','C栋','707','2','0',UTC_TIMESTAMP(),999958,142.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460454,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-106-2','A栋','106-2','2','0',UTC_TIMESTAMP(),999958,240.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460486,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-306','A栋','306','2','0',UTC_TIMESTAMP(),999958,343.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460518,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-413','A栋','413','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460550,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-521','A栋','521','2','0',UTC_TIMESTAMP(),999958,297.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460582,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-703','A栋','703','2','0',UTC_TIMESTAMP(),999958,455.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460614,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-206','C栋','206','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460646,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-311','C栋','311','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460678,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-501','C栋','501','2','0',UTC_TIMESTAMP(),999958,75.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460710,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-708','C栋','708','2','0',UTC_TIMESTAMP(),999958,138.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460455,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-107-1','A栋','107-1','2','0',UTC_TIMESTAMP(),999958,288.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460487,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-307','A栋','307','2','0',UTC_TIMESTAMP(),999958,156.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460519,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-415','A栋','415','2','0',UTC_TIMESTAMP(),999958,125.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460551,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-522','A栋','522','2','0',UTC_TIMESTAMP(),999958,250.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460583,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-705','A栋','705','2','0',UTC_TIMESTAMP(),999958,116.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460615,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-207','C栋','207','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460647,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-312','C栋','312','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460679,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-502','C栋','502','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460711,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-709','C栋','709','2','0',UTC_TIMESTAMP(),999958,197.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460456,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-107-2','A栋','107-2','2','0',UTC_TIMESTAMP(),999958,288.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460488,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-308','A栋','308','2','0',UTC_TIMESTAMP(),999958,220.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460520,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-416','A栋','416','2','0',UTC_TIMESTAMP(),999958,235.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460552,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-523','A栋','523','2','0',UTC_TIMESTAMP(),999958,199.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460584,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-706','A栋','706','2','0',UTC_TIMESTAMP(),999958,102.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460616,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-208','C栋','208','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460648,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-313','C栋','313','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460680,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-503','C栋','503','2','0',UTC_TIMESTAMP(),999958,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460712,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-710','C栋','710','2','0',UTC_TIMESTAMP(),999958,138.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460457,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-108','A栋','108','2','0',UTC_TIMESTAMP(),999958,584.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460489,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-309-1','A栋','309-1','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460521,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-417','A栋','417','2','0',UTC_TIMESTAMP(),999958,152.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460553,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-601','A栋','601','2','0',UTC_TIMESTAMP(),999958,701.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460585,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-707','A栋','707','2','0',UTC_TIMESTAMP(),999958,116.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460617,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-209','C栋','209','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460649,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-315','C栋','315','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460681,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-505','C栋','505','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460713,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-712','C栋','712','2','0',UTC_TIMESTAMP(),999958,47.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460458,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-109','A栋','109','2','0',UTC_TIMESTAMP(),999958,839.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460490,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-309-2','A栋','309-2','2','0',UTC_TIMESTAMP(),999958,172.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460522,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-418','A栋','418','2','0',UTC_TIMESTAMP(),999958,335.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460554,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-602','A栋','602','2','0',UTC_TIMESTAMP(),999958,550.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460586,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-708','A栋','708','2','0',UTC_TIMESTAMP(),999958,147.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460618,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-210','C栋','210','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460650,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-316','C栋','316','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460682,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-506','C栋','506','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460459,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-201','A栋','201','2','0',UTC_TIMESTAMP(),999958,230.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460491,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-310','A栋','310','2','0',UTC_TIMESTAMP(),999958,240.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460523,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-419','A栋','419','2','0',UTC_TIMESTAMP(),999958,155.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460555,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-603','A栋','603','2','0',UTC_TIMESTAMP(),999958,213.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460587,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-709','A栋','709','2','0',UTC_TIMESTAMP(),999958,326.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460619,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-211','C栋','211','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460651,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-317','C栋','317','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460683,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-507','C栋','507','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460460,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-202','A栋','202','2','0',UTC_TIMESTAMP(),999958,216.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460492,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-311-1','A栋','311-1','2','0',UTC_TIMESTAMP(),999958,245.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460524,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-420','A栋','420','2','0',UTC_TIMESTAMP(),999958,417.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460556,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-604','A栋','604','2','0',UTC_TIMESTAMP(),999958,30.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460588,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-710-1','A栋','710-1','2','0',UTC_TIMESTAMP(),999958,145.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460620,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-212','C栋','212','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460652,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-318','C栋','318','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460684,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-508','C栋','508','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460461,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-203','A栋','203','2','0',UTC_TIMESTAMP(),999958,300.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460493,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-311-2','A栋','311-2','2','0',UTC_TIMESTAMP(),999958,172.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460525,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-421','A栋','421','2','0',UTC_TIMESTAMP(),999958,156.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460557,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-605','A栋','605','2','0',UTC_TIMESTAMP(),999958,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460589,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-710-2','A栋','710-2','2','0',UTC_TIMESTAMP(),999958,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460621,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-213','C栋','213','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460653,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-319','C栋','319','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460685,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-509','C栋','509','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460462,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-205','A栋','205','2','0',UTC_TIMESTAMP(),999958,310.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460494,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-312','A栋','312','2','0',UTC_TIMESTAMP(),999958,237.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460526,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-422-1','A栋','422-1','2','0',UTC_TIMESTAMP(),999958,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460558,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-606','A栋','606','2','0',UTC_TIMESTAMP(),999958,38.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460590,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-710-3','A栋','710-3','2','0',UTC_TIMESTAMP(),999958,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460622,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-215','C栋','215','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460654,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-320','C栋','320','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460686,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-510','C栋','510','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460463,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-206','A栋','206','2','0',UTC_TIMESTAMP(),999958,343.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460495,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-313','A栋','313','2','0',UTC_TIMESTAMP(),999958,178.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460527,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-422-2','A栋','422-2','2','0',UTC_TIMESTAMP(),999958,150.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460559,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-607','A栋','607','2','0',UTC_TIMESTAMP(),999958,108.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460591,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-711','A栋','711','2','0',UTC_TIMESTAMP(),999958,349.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460623,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-216','C栋','216','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460655,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-321','C栋','321','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460687,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-511','C栋','511','2','0',UTC_TIMESTAMP(),999958,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460464,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-207','A栋','207','2','0',UTC_TIMESTAMP(),999958,156.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460496,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-315','A栋','315','2','0',UTC_TIMESTAMP(),999958,158.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460528,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-423','A栋','423','2','0',UTC_TIMESTAMP(),999958,392.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460560,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-608','A栋','608','2','0',UTC_TIMESTAMP(),999958,34.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460592,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-712','A栋','712','2','0',UTC_TIMESTAMP(),999958,327.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460624,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-217','C栋','217','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460656,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-322','C栋','322','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460688,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-512','C栋','512','2','0',UTC_TIMESTAMP(),999958,94.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460465,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-208','A栋','208','2','0',UTC_TIMESTAMP(),999958,220.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460497,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-316','A栋','316','2','0',UTC_TIMESTAMP(),999958,238.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460529,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-425','A栋','425','2','0',UTC_TIMESTAMP(),999958,198.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460561,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-609','A栋','609','2','0',UTC_TIMESTAMP(),999958,139.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460593,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','A栋-713','A栋','713','2','0',UTC_TIMESTAMP(),999958,310.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460625,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-218','C栋','218','2','0',UTC_TIMESTAMP(),999958,46.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460657,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-323','C栋','323','2','0',UTC_TIMESTAMP(),999958,45.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387460689,UUID(),240111044332060016,18368,'深圳市',18369,'宝安区','C栋-513','C栋','513','2','0',UTC_TIMESTAMP(),999958,149.0);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83537,1039143,240111044332060016,239825274387460465,'A栋-208',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83569,1039143,240111044332060016,239825274387460497,'A栋-316',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83601,1039143,240111044332060016,239825274387460529,'A栋-425',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83633,1039143,240111044332060016,239825274387460561,'A栋-609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83665,1039143,240111044332060016,239825274387460593,'A栋-713',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83697,1039143,240111044332060016,239825274387460625,'C栋-218',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83729,1039143,240111044332060016,239825274387460657,'C栋-323',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83761,1039143,240111044332060016,239825274387460689,'C栋-513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83538,1039143,240111044332060016,239825274387460466,'A栋-209-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83570,1039143,240111044332060016,239825274387460498,'A栋-317',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83602,1039143,240111044332060016,239825274387460530,'A栋-501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83634,1039143,240111044332060016,239825274387460562,'A栋-610',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83666,1039143,240111044332060016,239825274387460594,'A栋-715',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83698,1039143,240111044332060016,239825274387460626,'C栋-219',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83730,1039143,240111044332060016,239825274387460658,'C栋-325',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83762,1039143,240111044332060016,239825274387460690,'C栋-516',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83539,1039143,240111044332060016,239825274387460467,'A栋-209-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83571,1039143,240111044332060016,239825274387460499,'A栋-318',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83603,1039143,240111044332060016,239825274387460531,'A栋-502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83635,1039143,240111044332060016,239825274387460563,'A栋-611',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83667,1039143,240111044332060016,239825274387460595,'A栋-716',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83699,1039143,240111044332060016,239825274387460627,'C栋-220',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83731,1039143,240111044332060016,239825274387460659,'C栋-326',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83763,1039143,240111044332060016,239825274387460691,'C栋-518',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83540,1039143,240111044332060016,239825274387460468,'A栋-210',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83572,1039143,240111044332060016,239825274387460500,'A栋-319',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83604,1039143,240111044332060016,239825274387460532,'A栋-503',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83636,1039143,240111044332060016,239825274387460564,'A栋-612',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83668,1039143,240111044332060016,239825274387460596,'A栋-717',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83700,1039143,240111044332060016,239825274387460628,'C栋-221',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83732,1039143,240111044332060016,239825274387460660,'C栋-327',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83764,1039143,240111044332060016,239825274387460692,'C栋-601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83541,1039143,240111044332060016,239825274387460469,'A栋-211-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83573,1039143,240111044332060016,239825274387460501,'A栋-320',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83605,1039143,240111044332060016,239825274387460533,'A栋-505',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83637,1039143,240111044332060016,239825274387460565,'A栋-613',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83669,1039143,240111044332060016,239825274387460597,'A栋-718',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83701,1039143,240111044332060016,239825274387460629,'C栋-222',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83733,1039143,240111044332060016,239825274387460661,'C栋-328',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83765,1039143,240111044332060016,239825274387460693,'C栋-602-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83542,1039143,240111044332060016,239825274387460470,'A栋-211-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83574,1039143,240111044332060016,239825274387460502,'A栋-321',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83606,1039143,240111044332060016,239825274387460534,'A栋-506',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83638,1039143,240111044332060016,239825274387460566,'A栋-615',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83670,1039143,240111044332060016,239825274387460598,'A栋-719-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83702,1039143,240111044332060016,239825274387460630,'C栋-223',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83734,1039143,240111044332060016,239825274387460662,'C栋-330',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83766,1039143,240111044332060016,239825274387460694,'C栋-602-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83543,1039143,240111044332060016,239825274387460471,'A栋-212',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83575,1039143,240111044332060016,239825274387460503,'A栋-322',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83607,1039143,240111044332060016,239825274387460535,'A栋-507',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83639,1039143,240111044332060016,239825274387460567,'A栋-616',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83671,1039143,240111044332060016,239825274387460599,'A栋-719-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83703,1039143,240111044332060016,239825274387460631,'C栋-225',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83735,1039143,240111044332060016,239825274387460663,'C栋-332',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83767,1039143,240111044332060016,239825274387460695,'C栋-603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83544,1039143,240111044332060016,239825274387460472,'A栋-213',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83576,1039143,240111044332060016,239825274387460504,'A栋-323',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83608,1039143,240111044332060016,239825274387460536,'A栋-508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83640,1039143,240111044332060016,239825274387460568,'A栋-617',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83672,1039143,240111044332060016,239825274387460600,'A栋-720',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83704,1039143,240111044332060016,239825274387460632,'C栋-226',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83736,1039143,240111044332060016,239825274387460664,'C栋-401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83768,1039143,240111044332060016,239825274387460696,'C栋-604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83545,1039143,240111044332060016,239825274387460473,'A栋-215',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83577,1039143,240111044332060016,239825274387460505,'A栋-401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83609,1039143,240111044332060016,239825274387460537,'A栋-509-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83641,1039143,240111044332060016,239825274387460569,'A栋-618',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83673,1039143,240111044332060016,239825274387460601,'A栋-721',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83705,1039143,240111044332060016,239825274387460633,'C栋-227',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83737,1039143,240111044332060016,239825274387460665,'C栋-402',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83769,1039143,240111044332060016,239825274387460697,'C栋-605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83546,1039143,240111044332060016,239825274387460474,'A栋-216',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83578,1039143,240111044332060016,239825274387460506,'A栋-402',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83610,1039143,240111044332060016,239825274387460538,'A栋-509-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83642,1039143,240111044332060016,239825274387460570,'A栋-619',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83674,1039143,240111044332060016,239825274387460602,'A栋-722',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83706,1039143,240111044332060016,239825274387460634,'C栋-228',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83738,1039143,240111044332060016,239825274387460666,'C栋-403',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83770,1039143,240111044332060016,239825274387460698,'C栋-606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83547,1039143,240111044332060016,239825274387460475,'A栋-217',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83579,1039143,240111044332060016,239825274387460507,'A栋-403',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83611,1039143,240111044332060016,239825274387460539,'A栋-510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83643,1039143,240111044332060016,239825274387460571,'A栋-620',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83675,1039143,240111044332060016,239825274387460603,'A栋-723',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83707,1039143,240111044332060016,239825274387460635,'C栋-230',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83739,1039143,240111044332060016,239825274387460667,'C栋-405',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83771,1039143,240111044332060016,239825274387460699,'C栋-607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83548,1039143,240111044332060016,239825274387460476,'A栋-218',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83580,1039143,240111044332060016,239825274387460508,'A栋-405',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83612,1039143,240111044332060016,239825274387460540,'A栋-511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83644,1039143,240111044332060016,239825274387460572,'A栋-621',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83676,1039143,240111044332060016,239825274387460604,'C栋-101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83708,1039143,240111044332060016,239825274387460636,'C栋-232',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83740,1039143,240111044332060016,239825274387460668,'C栋-406',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83772,1039143,240111044332060016,239825274387460700,'C栋-608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83549,1039143,240111044332060016,239825274387460477,'A栋-219',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83581,1039143,240111044332060016,239825274387460509,'A栋-406',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83613,1039143,240111044332060016,239825274387460541,'A栋-512-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83645,1039143,240111044332060016,239825274387460573,'A栋-622',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83677,1039143,240111044332060016,239825274387460605,'C栋-102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83709,1039143,240111044332060016,239825274387460637,'C栋-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83741,1039143,240111044332060016,239825274387460669,'C栋-407',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83773,1039143,240111044332060016,239825274387460701,'C栋-609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83518,1039143,240111044332060016,239825274387460446,'A栋-101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83550,1039143,240111044332060016,239825274387460478,'A栋-220',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83582,1039143,240111044332060016,239825274387460510,'A栋-407',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83614,1039143,240111044332060016,239825274387460542,'A栋-512-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83646,1039143,240111044332060016,239825274387460574,'A栋-623',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83678,1039143,240111044332060016,239825274387460606,'C栋-103',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83710,1039143,240111044332060016,239825274387460638,'C栋-302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83742,1039143,240111044332060016,239825274387460670,'C栋-408',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83774,1039143,240111044332060016,239825274387460702,'C栋-610',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83519,1039143,240111044332060016,239825274387460447,'A栋-102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83551,1039143,240111044332060016,239825274387460479,'A栋-221',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83583,1039143,240111044332060016,239825274387460511,'A栋-408',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83615,1039143,240111044332060016,239825274387460543,'A栋-513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83647,1039143,240111044332060016,239825274387460575,'A栋-625',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83679,1039143,240111044332060016,239825274387460607,'C栋-104',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83711,1039143,240111044332060016,239825274387460639,'C栋-303',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83743,1039143,240111044332060016,239825274387460671,'C栋-409',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83775,1039143,240111044332060016,239825274387460703,'C栋-612',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83520,1039143,240111044332060016,239825274387460448,'A栋-103',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83552,1039143,240111044332060016,239825274387460480,'A栋-222',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83584,1039143,240111044332060016,239825274387460512,'A栋-409-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83616,1039143,240111044332060016,239825274387460544,'A栋-515',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83648,1039143,240111044332060016,239825274387460576,'A栋-626',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83680,1039143,240111044332060016,239825274387460608,'C栋-105',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83712,1039143,240111044332060016,239825274387460640,'C栋-305',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83744,1039143,240111044332060016,239825274387460672,'C栋-410',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83776,1039143,240111044332060016,239825274387460704,'C栋-701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83521,1039143,240111044332060016,239825274387460449,'A栋-104-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83553,1039143,240111044332060016,239825274387460481,'A栋-223',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83585,1039143,240111044332060016,239825274387460513,'A栋-409-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83617,1039143,240111044332060016,239825274387460545,'A栋-516',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83649,1039143,240111044332060016,239825274387460577,'A栋-627',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83681,1039143,240111044332060016,239825274387460609,'C栋-106',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83713,1039143,240111044332060016,239825274387460641,'C栋-306',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83745,1039143,240111044332060016,239825274387460673,'C栋-411',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83777,1039143,240111044332060016,239825274387460705,'C栋-702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83522,1039143,240111044332060016,239825274387460450,'A栋-104-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83554,1039143,240111044332060016,239825274387460482,'A栋-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83586,1039143,240111044332060016,239825274387460514,'A栋-410',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83618,1039143,240111044332060016,239825274387460546,'A栋-517',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83650,1039143,240111044332060016,239825274387460578,'A栋-629',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83682,1039143,240111044332060016,239825274387460610,'C栋-201',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83714,1039143,240111044332060016,239825274387460642,'C栋-307',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83746,1039143,240111044332060016,239825274387460674,'C栋-412',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83778,1039143,240111044332060016,239825274387460706,'C栋-703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83523,1039143,240111044332060016,239825274387460451,'A栋-105-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83555,1039143,240111044332060016,239825274387460483,'A栋-302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83587,1039143,240111044332060016,239825274387460515,'A栋-411',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83619,1039143,240111044332060016,239825274387460547,'A栋-518',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83651,1039143,240111044332060016,239825274387460579,'A栋-631',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83683,1039143,240111044332060016,239825274387460611,'C栋-202',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83715,1039143,240111044332060016,239825274387460643,'C栋-308',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83747,1039143,240111044332060016,239825274387460675,'C栋-413',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83779,1039143,240111044332060016,239825274387460707,'C栋-705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83524,1039143,240111044332060016,239825274387460452,'A栋-105-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83556,1039143,240111044332060016,239825274387460484,'A栋-303',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83588,1039143,240111044332060016,239825274387460516,'A栋-411-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83620,1039143,240111044332060016,239825274387460548,'A栋-519',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83652,1039143,240111044332060016,239825274387460580,'A栋-701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83684,1039143,240111044332060016,239825274387460612,'C栋-203',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83716,1039143,240111044332060016,239825274387460644,'C栋-309',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83748,1039143,240111044332060016,239825274387460676,'C栋-416',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83780,1039143,240111044332060016,239825274387460708,'C栋-706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83525,1039143,240111044332060016,239825274387460453,'A栋-106-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83557,1039143,240111044332060016,239825274387460485,'A栋-305',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83589,1039143,240111044332060016,239825274387460517,'A栋-412',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83621,1039143,240111044332060016,239825274387460549,'A栋-520',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83653,1039143,240111044332060016,239825274387460581,'A栋-702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83685,1039143,240111044332060016,239825274387460613,'C栋-205',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83717,1039143,240111044332060016,239825274387460645,'C栋-310',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83749,1039143,240111044332060016,239825274387460677,'C栋-418',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83781,1039143,240111044332060016,239825274387460709,'C栋-707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83526,1039143,240111044332060016,239825274387460454,'A栋-106-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83558,1039143,240111044332060016,239825274387460486,'A栋-306',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83590,1039143,240111044332060016,239825274387460518,'A栋-413',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83622,1039143,240111044332060016,239825274387460550,'A栋-521',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83654,1039143,240111044332060016,239825274387460582,'A栋-703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83686,1039143,240111044332060016,239825274387460614,'C栋-206',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83718,1039143,240111044332060016,239825274387460646,'C栋-311',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83750,1039143,240111044332060016,239825274387460678,'C栋-501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83782,1039143,240111044332060016,239825274387460710,'C栋-708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83527,1039143,240111044332060016,239825274387460455,'A栋-107-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83559,1039143,240111044332060016,239825274387460487,'A栋-307',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83591,1039143,240111044332060016,239825274387460519,'A栋-415',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83623,1039143,240111044332060016,239825274387460551,'A栋-522',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83655,1039143,240111044332060016,239825274387460583,'A栋-705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83687,1039143,240111044332060016,239825274387460615,'C栋-207',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83719,1039143,240111044332060016,239825274387460647,'C栋-312',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83751,1039143,240111044332060016,239825274387460679,'C栋-502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83783,1039143,240111044332060016,239825274387460711,'C栋-709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83528,1039143,240111044332060016,239825274387460456,'A栋-107-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83560,1039143,240111044332060016,239825274387460488,'A栋-308',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83592,1039143,240111044332060016,239825274387460520,'A栋-416',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83624,1039143,240111044332060016,239825274387460552,'A栋-523',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83656,1039143,240111044332060016,239825274387460584,'A栋-706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83688,1039143,240111044332060016,239825274387460616,'C栋-208',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83720,1039143,240111044332060016,239825274387460648,'C栋-313',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83752,1039143,240111044332060016,239825274387460680,'C栋-503',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83784,1039143,240111044332060016,239825274387460712,'C栋-710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83529,1039143,240111044332060016,239825274387460457,'A栋-108',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83561,1039143,240111044332060016,239825274387460489,'A栋-309-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83593,1039143,240111044332060016,239825274387460521,'A栋-417',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83625,1039143,240111044332060016,239825274387460553,'A栋-601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83657,1039143,240111044332060016,239825274387460585,'A栋-707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83689,1039143,240111044332060016,239825274387460617,'C栋-209',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83721,1039143,240111044332060016,239825274387460649,'C栋-315',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83753,1039143,240111044332060016,239825274387460681,'C栋-505',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83785,1039143,240111044332060016,239825274387460713,'C栋-712',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83530,1039143,240111044332060016,239825274387460458,'A栋-109',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83562,1039143,240111044332060016,239825274387460490,'A栋-309-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83594,1039143,240111044332060016,239825274387460522,'A栋-418',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83626,1039143,240111044332060016,239825274387460554,'A栋-602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83658,1039143,240111044332060016,239825274387460586,'A栋-708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83690,1039143,240111044332060016,239825274387460618,'C栋-210',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83722,1039143,240111044332060016,239825274387460650,'C栋-316',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83754,1039143,240111044332060016,239825274387460682,'C栋-506',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83531,1039143,240111044332060016,239825274387460459,'A栋-201',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83563,1039143,240111044332060016,239825274387460491,'A栋-310',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83595,1039143,240111044332060016,239825274387460523,'A栋-419',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83627,1039143,240111044332060016,239825274387460555,'A栋-603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83659,1039143,240111044332060016,239825274387460587,'A栋-709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83691,1039143,240111044332060016,239825274387460619,'C栋-211',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83723,1039143,240111044332060016,239825274387460651,'C栋-317',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83755,1039143,240111044332060016,239825274387460683,'C栋-507',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83532,1039143,240111044332060016,239825274387460460,'A栋-202',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83564,1039143,240111044332060016,239825274387460492,'A栋-311-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83596,1039143,240111044332060016,239825274387460524,'A栋-420',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83628,1039143,240111044332060016,239825274387460556,'A栋-604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83660,1039143,240111044332060016,239825274387460588,'A栋-710-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83692,1039143,240111044332060016,239825274387460620,'C栋-212',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83724,1039143,240111044332060016,239825274387460652,'C栋-318',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83756,1039143,240111044332060016,239825274387460684,'C栋-508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83533,1039143,240111044332060016,239825274387460461,'A栋-203',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83565,1039143,240111044332060016,239825274387460493,'A栋-311-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83597,1039143,240111044332060016,239825274387460525,'A栋-421',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83629,1039143,240111044332060016,239825274387460557,'A栋-605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83661,1039143,240111044332060016,239825274387460589,'A栋-710-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83693,1039143,240111044332060016,239825274387460621,'C栋-213',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83725,1039143,240111044332060016,239825274387460653,'C栋-319',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83757,1039143,240111044332060016,239825274387460685,'C栋-509',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83534,1039143,240111044332060016,239825274387460462,'A栋-205',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83566,1039143,240111044332060016,239825274387460494,'A栋-312',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83598,1039143,240111044332060016,239825274387460526,'A栋-422-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83630,1039143,240111044332060016,239825274387460558,'A栋-606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83662,1039143,240111044332060016,239825274387460590,'A栋-710-3',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83694,1039143,240111044332060016,239825274387460622,'C栋-215',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83726,1039143,240111044332060016,239825274387460654,'C栋-320',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83758,1039143,240111044332060016,239825274387460686,'C栋-510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83535,1039143,240111044332060016,239825274387460463,'A栋-206',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83567,1039143,240111044332060016,239825274387460495,'A栋-313',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83599,1039143,240111044332060016,239825274387460527,'A栋-422-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83631,1039143,240111044332060016,239825274387460559,'A栋-607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83663,1039143,240111044332060016,239825274387460591,'A栋-711',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83695,1039143,240111044332060016,239825274387460623,'C栋-216',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83727,1039143,240111044332060016,239825274387460655,'C栋-321',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83759,1039143,240111044332060016,239825274387460687,'C栋-511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83536,1039143,240111044332060016,239825274387460464,'A栋-207',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83568,1039143,240111044332060016,239825274387460496,'A栋-315',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83600,1039143,240111044332060016,239825274387460528,'A栋-423',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83632,1039143,240111044332060016,239825274387460560,'A栋-608',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83664,1039143,240111044332060016,239825274387460592,'A栋-712',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83696,1039143,240111044332060016,239825274387460624,'C栋-217',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83728,1039143,240111044332060016,239825274387460656,'C栋-322',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (83760,1039143,240111044332060016,239825274387460688,'C栋-512',2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117418,20930,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117419,21200,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117420,21210,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117421,21220,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117422,21230,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117423,40000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117424,40100,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117425,40110,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117426,40120,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117427,40130,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117428,40200,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117429,40210,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117430,40220,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117431,40300,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117432,40400,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117433,40410,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117434,40420,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117435,40430,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117436,40440,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117437,40450,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117438,40500,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117439,40510,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117440,40520,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117441,40530,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117442,40541,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117443,40542,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117444,40750,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117445,40700,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117446,40800,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117447,40810,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117448,40830,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117449,40840,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117450,40850,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117451,40900,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117452,41000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117453,41010,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117454,41020,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117455,41030,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117456,41040,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117457,41050,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117458,41060,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117459,41100,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117460,41200,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117461,41210,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117462,41220,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117463,41230,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117464,30000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117465,30500,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117466,31000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117467,32000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117468,33000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117469,34000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117470,35000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117471,30600,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117472,37000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117473,38000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117474,50000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117475,50100,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117476,50110,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117477,50200,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117478,50210,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117479,50220,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117480,50300,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117481,50400,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117482,50500,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117483,50600,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117484,50630,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117485,50631,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117486,50632,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117487,50633,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117488,50640,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117489,50650,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117490,50651,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117491,50652,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117492,50653,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117493,56161,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117494,50800,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117495,50810,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117496,50820,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117497,50830,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117498,50840,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117499,50850,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117500,50860,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117501,60000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117502,60100,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117503,60200,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117504,70000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117505,70100,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117506,70200,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117361,10000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117362,10100,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117363,10400,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117364,10200,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117365,10600,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117366,10610,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117367,10620,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117368,10750,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117369,10751,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117370,10752,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117371,10800,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117372,10900,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117373,11000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117374,20000,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117375,20100,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117376,20110,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117377,20120,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117378,20130,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117379,20140,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117380,20150,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117381,20155,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117382,20160,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117383,20170,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117384,20180,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117385,20158,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117386,20190,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117387,20191,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117388,20192,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117389,20400,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117390,20410,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117391,20420,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117392,20800,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117393,20810,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117394,20811,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117395,20812,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117396,20820,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117397,20821,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117398,20822,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117399,20830,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117400,20831,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117401,20840,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117402,20841,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117403,20600,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117404,20610,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117405,20620,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117406,20630,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117407,20640,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117408,20650,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117409,20660,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117410,20670,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117411,20671,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117412,20672,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117413,20673,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117414,20680,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117415,20900,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117416,20910,NULL,'EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117417,20920,NULL,'EhNamespaces',999958,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001167,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999958,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001168,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999958,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001169,'',0,1001169,1,'活动管理-默认子分类','/1/1001169',0,2,1,NOW(),0,NULL,999958,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001170,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999958,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001171,'',0,1001171,2,'活动管理二-默认子分类','/2/1001171',0,2,1,NOW(),0,NULL,999958,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001172,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999958,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001173,'',0,1001173,3,'活动管理三-默认子分类','/3/1001173',0,2,1,NOW(),0,NULL,999958,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1039143,240111044332060016);

    
-- guangxingyuan server by ljs
SET @namespace_id=999958;
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @banner_id = (SELECT MAX(id) FROM `eh_banners`);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvMk16QTBZbUk0TlRBMU5EVTBNakppTWpVM01HUm1OVEkyTURZeU56SXhOUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvMk16QTBZbUk0TlRBMU5EVTBNakppTWpVM01HUm1OVEkyTURZeU56SXhOUQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');


INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (769,999958,'HomeWuyeLayout','{"versionCode":"2017111501","layoutName":"HomeWuyeLayout","displayName":"物业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2017111501,0,2,NULL,'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (770,999958,'HomeWuyeLayout','{"versionCode":"2017111501","layoutName":"HomeWuyeLayout","displayName":"物业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2017111501,0,2,NULL,'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (771,999958,'HomeResourcebookLayout','{"versionCode":"2017111501","layoutName":"HomeResourcebookLayout","displayName":"资源预定","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2017111501,0,2,NULL,'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (772,999958,'HomeResourcebookLayout','{"versionCode":"2017111501","layoutName":"HomeResourcebookLayout","displayName":"资源预定","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2017111501,0,2,NULL,'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (773,999958,'ServiceMarketLayout','{"versionCode":"2017111501","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":1},"defaultOrder":40,"separatorFlag":1,"separatorHeight":16}]}',2017111501,0,2,NULL,'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (774,999958,'ServiceMarketLayout','{"versionCode":"2017111501","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":1},"defaultOrder":40,"separatorFlag":1,"separatorHeight":16}]}',2017111501,0,2,NULL,'park_tourist',0,0,0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129204,999958,0,0,0,'/home','NavigatorGroup2','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRvMU4yWTRNR1JtTVRJNU1UTmhNbU5qTXpsaU5qRXpNMlJoWXpJMU5EWXhZUQ',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,1,'pm_admin',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129205,999958,0,0,0,'/home','NavigatorGroup2','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRvMU4yWTRNR1JtTVRJNU1UTmhNbU5qTXpsaU5qRXpNMlJoWXpJMU5EWXhZUQ',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',10,0,1,1,0,1,'park_tourist',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129206,999958,0,0,0,'/home','NavigatorGroup2','园区办事','园区办事','cs://1/image/aW1hZ2UvTVRvek9UVXdOREptWmpOa016Z3pNRFZqWkRFeVlURTRaamczWldOaFl6bGpNQQ',1,1,33,'{"type":212652,"parentId":212652,"displayType": "grid"}',20,0,1,1,0,1,'pm_admin',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129207,999958,0,0,0,'/home','NavigatorGroup2','园区办事','园区办事','cs://1/image/aW1hZ2UvTVRvek9UVXdOREptWmpOa016Z3pNRFZqWkRFeVlURTRaamczWldOaFl6bGpNQQ',1,1,33,'{"type":212652,"parentId":212652,"displayType": "grid"}',20,0,1,1,0,1,'park_tourist',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129208,999958,0,0,0,'/home','NavigatorGroup2','园区招商','园区招商','cs://1/image/aW1hZ2UvTVRwbE1XSmhZakZtWkRZME0ySXpNRE5tT0RsaE5URTJNV1JoTnpZNFl6azBaQQ',1,1,28,'',30,0,1,1,0,1,'pm_admin',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129209,999958,0,0,0,'/home','NavigatorGroup2','园区招商','园区招商','cs://1/image/aW1hZ2UvTVRwbE1XSmhZakZtWkRZME0ySXpNRE5tT0RsaE5URTJNV1JoTnpZNFl6azBaQQ',1,1,28,'',30,0,1,1,0,1,'park_tourist',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129210,999958,0,0,0,'/home','NavigatorGroup2','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRveU9EWmpZek0zTm1NM01tUTVZVGRsTkRnMVkyVTJNMlUxTm1Sa05HTXhZdw',1,1,2,'{"itemLocation":"/home/wuye","layoutName":"HomeWuyeLayout","title":"物业服务","entityTag":"PM"}',40,0,1,1,0,1,'pm_admin',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129211,999958,0,0,0,'/home','NavigatorGroup2','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRveU9EWmpZek0zTm1NM01tUTVZVGRsTkRnMVkyVTJNMlUxTm1Sa05HTXhZdw',1,1,2,'{"itemLocation":"/home/wuye","layoutName":"HomeWuyeLayout","title":"物业服务","entityTag":"PM"}',40,0,1,1,0,1,'park_tourist',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129212,999958,0,0,0,'/home','NavigatorGroup2','停车收费','停车收费','cs://1/image/aW1hZ2UvTVRwbU5HVmtNR05rTm1FMlpqRTBNbVpoWlRaaVpqTmxaakE1WWpWbE1EWTJNZw',1,1,30,'',50,0,1,1,0,1,'pm_admin',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129213,999958,0,0,0,'/home','NavigatorGroup2','停车收费','停车收费','cs://1/image/aW1hZ2UvTVRwbU5HVmtNR05rTm1FMlpqRTBNbVpoWlRaaVpqTmxaakE1WWpWbE1EWTJNZw',1,1,30,'',50,0,1,1,0,1,'park_tourist',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129214,999958,0,0,0,'/home','NavigatorGroup2','资源预定','资源预定','cs://1/image/aW1hZ2UvTVRvM09HVXdPRGd6WXpZMFkyVTROelV5TURRMU5UbGlaV0pqWVRBMk4yWmtZUQ',1,1,60,'{"url":"zl://association/main?layoutName=HomeResourcebookLayout&itemLocation=/rental&versionCode=2017111501&displayName=资源预定"}',60,0,1,1,0,1,'pm_admin',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129215,999958,0,0,0,'/home','NavigatorGroup2','资源预定','资源预定','cs://1/image/aW1hZ2UvTVRvM09HVXdPRGd6WXpZMFkyVTROelV5TURRMU5UbGlaV0pqWVRBMk4yWmtZUQ',1,1,60,'{"url":"zl://association/main?layoutName=HomeResourcebookLayout&itemLocation=/rental&versionCode=2017111501&displayName=资源预定"}',60,0,1,1,0,1,'park_tourist',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129216,999958,0,0,0,'/home','NavigatorGroup2','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRwbE5HTTJPV1EyT0RjNU5UVXdaR0k0TURobE9XTXpabU0wTnpZM01EaGxZUQ',1,1,23,'',70,0,1,1,0,1,'pm_admin',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129217,999958,0,0,0,'/home','NavigatorGroup2','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRwbE5HTTJPV1EyT0RjNU5UVXdaR0k0TURobE9XTXpabU0wTnpZM01EaGxZUQ',1,1,23,'',70,0,1,1,0,1,'park_tourist',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129218,999958,0,0,0,'/home','NavigatorGroup2','租金缴纳','租金缴纳','cs://1/image/aW1hZ2UvTVRwa05qRTVaVFptT1RZeE16Vm1OREk1TVdJM05EazNZVE15WkdFMFlXSTBNQQ',1,1,14,'{"url":"${home.url}/property_fee/index.html?hideNavigationBar=1#/bill_query?sign_suffix"}',80,0,1,1,0,1,'pm_admin',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129219,999958,0,0,0,'/home','NavigatorGroup2','租金缴纳','租金缴纳','cs://1/image/aW1hZ2UvTVRwa05qRTVaVFptT1RZeE16Vm1OREk1TVdJM05EazNZVE15WkdFMFlXSTBNQQ',1,1,14,'{"url":"${home.url}/property_fee/index.html?hideNavigationBar=1#/bill_query?sign_suffix"}',80,0,1,1,0,1,'park_tourist',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129220,999958,0,0,0,'/home','NavigatorGroup2','园区企业','园区企业','cs://1/image/aW1hZ2UvTVRvNFlqWTFOemcyWm1GaVlqVXdZekEwWm1NMk4yRTBNVGszTmpjMU5EWXpOUQ',1,1,34,'',90,0,1,1,0,1,'pm_admin',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129221,999958,0,0,0,'/home','NavigatorGroup2','园区企业','园区企业','cs://1/image/aW1hZ2UvTVRvNFlqWTFOemcyWm1GaVlqVXdZekEwWm1NMk4yRTBNVGszTmpjMU5EWXpOUQ',1,1,34,'',90,0,1,1,0,1,'park_tourist',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129222,999958,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRvMVpEQTRPVGczWW1RNE16VmxZamxrTmpVek1UWmhObU0xWWpkaFpqVmhOQQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'pm_admin',0,100,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129223,999958,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRvMVpEQTRPVGczWW1RNE16VmxZamxrTmpVek1UWmhObU0xWWpkaFpqVmhOQQ',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'park_tourist',0,100,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129224,999958,0,0,0,'/home','NavigatorGroup2','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRwaU5qRXhNakExWWpVMk1ESTFNRGM0WkRNd09UY3hOR0k0Wm1KaU1UZzFZZw',1,1,36,'{"privateFlag": 0}',100,0,1,0,0,1,'pm_admin',0,110,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129225,999958,0,0,0,'/home','NavigatorGroup2','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRwaU5qRXhNakExWWpVMk1ESTFNRGM0WkRNd09UY3hOR0k0Wm1KaU1UZzFZZw',1,1,36,'{"privateFlag": 0}',100,0,1,0,0,1,'park_tourist',0,110,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129226,999958,0,0,0,'/home','NavigatorGroup2','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRvMFpUQTRNamhpTVRSall6ZzFNbVk0TkRWaVpXTTVOalExWkdVell6UTJNQQ',1,1,56,'',110,0,1,0,0,1,'pm_admin',0,120,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129227,999958,0,0,0,'/home','NavigatorGroup2','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRvMFpUQTRNamhpTVRSall6ZzFNbVk0TkRWaVpXTTVOalExWkdVell6UTJNQQ',1,1,56,'',110,0,1,0,0,1,'park_tourist',0,120,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129228,999958,0,0,0,'/rental','TabGroup','会议室','会议室','',1,1,49,'{"resourceTypeId":12130,"pageType":0}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129229,999958,0,0,0,'/rental','TabGroup','会议室','会议室','',1,1,49,'{"resourceTypeId":12130,"pageType":0}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129230,999958,0,0,0,'/rental','TabGroup','电子屏','电子屏','',1,1,49,'{"resourceTypeId":12131,"pageType":0}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129231,999958,0,0,0,'/rental','TabGroup','电子屏','电子屏','',1,1,49,'{"resourceTypeId":12131,"pageType":0}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129232,999958,0,0,0,'/home/wuye','NavigatorGroup1','物业报修','物业报修','',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203719&displayName=物业报修"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129233,999958,0,0,0,'/home/wuye','NavigatorGroup1','物业报修','物业报修','',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203719&displayName=物业报修"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129234,999958,0,0,0,'/home/wuye','NavigatorGroup1','物业巡检','物业巡检','',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (129235,999958,0,0,0,'/home/wuye','NavigatorGroup1','物业巡检','物业巡检','',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',20,0,1,1,0,0,'park_tourist',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`) 
	VALUES (212652,'community','240111044331055940',0,'园区办事','园区办事',0,2,1,NULL,999958);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211140,0,'organaization',1039143,'all','园区办事','园区办事',212652,2,211140,NULL,2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117509,41700,'园区办事','EhNamespaces',999958,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117510,41710,'','EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117511,41720,'','EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117512,41730,'','EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117513,41740,'','EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117514,41750,'','EhNamespaces',999958,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (117515,41760,'','EhNamespaces',999958,2);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (203719,6,0,'物业报修','任务/物业报修',1,2,NULL,999958);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12130,'会议室',0,2,999958,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12131,'电子屏',0,2,999958,0,0);
