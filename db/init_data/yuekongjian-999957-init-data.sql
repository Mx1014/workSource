INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1912,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999957','the relative path for yuespace app agreements',999957,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1913,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999957,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1914,'pmtask.handler-999957','flow','0',999957,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1915,'pay.platform','1','支付类型',999957,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (467,'Android_HangZhouYueSpace',NULL,UTC_TIMESTAMP(),999957);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (468,'iOS_HangZhouYueSpace',NULL,UTC_TIMESTAMP(),999957);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (803,467,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999957);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (804,468,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999957);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999957,'杭州越空间');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1470,999957,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (898,'sms.default.sign',0,'zh_CN','杭州越空间','【杭州越空间】',999957);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18413,0,'浙江','ZHEJIANG','ZJ','/浙江',1,1,NULL,NULL,2,0,999957);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18414,18413,'杭州市','HANGZHOUSHI','HZS','/浙江/杭州市',2,2,NULL,'0571',2,1,999957);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18415,18414,'临安区','LINANQU','LAQ','/浙江/杭州市/临安区',3,3,NULL,'0571',2,0,999957);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060058,UUID(),18414,'杭州市',18415,'临安区','越空间','','青山湖街道大园路123号','越空间孵化器成立于2017年3月，是越秀集团首个科创企业孵化摇篮，由越创慧科技有限公司运营管理。
越空间位于临安区青山湖街道未来科技城内，运营面积达5000m2 ，是促进科技型中小企业孵化发展的空间载体，嫁接龙头企业与上下游创新创业企业，致力于帮助区域企业走向全球。
园区专门为科创企业者提供研究开发场地、种子资金、政策扶持和相关服务，促进科技成果的迅速转化，打造全产业链科技企业孵化器，苗圃-孵化器-加速器-产业园。园区软硬件支撑，实现空间载体+企业孵化器+创新创业社群。
',0,1,2,UTC_TIMESTAMP(),1,192682,192683,UTC_TIMESTAMP(),999957);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092831,240111044332060058,'',119.4925,30.155,'wtkgx1ec953v');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20168,999957,'COMMUNITY',240111044332060058,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1039387,0,'PM','广州怡城物业管理有限公司临安分公司','广州怡城物业管理有限公司是越秀集团旗下从事写字楼、商业广场等商业物业经营管理的专业公司，直接经营管理的物业总面积达60 多万平方米，是广州地区规模最大的高端商业物业经营管理企业之一。怡城物业负责基金旗下维多利广场、财富广场、城建大厦、越秀新都会大厦等项目的租赁经营和物业、物业管理和市场推广业务。
','/1039387',1,2,'ENTERPRISE',999957,1057845);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1154533,240111044332060058,'organization',1039387,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (24827,'EhOrganizations',1039387,1,10,462089,0,1,UTC_TIMESTAMP(),999957,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (24828,'EhOrganizations',1039387,1,10,462090,0,1,UTC_TIMESTAMP(),999957,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1057845,UUID(),'广州怡城物业管理有限公司临安分公司','广州怡城物业管理有限公司临安分公司',1,1,1039387,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192681,1,999957);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192681,UUID(),999957,2,'EhGroups',1057845,'广州怡城物业管理有限公司临安分公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192682,UUID(),999957,2,'',0,'杭州越空间社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192683,UUID(),999957,2,'',0,'杭州越空间意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (462089,UUID(),'19213988962','张三',NULL,1,45,'1','1','zh_CN','56e4c1fc1e7d53192d7ac0e214dd4da2','23180a40fcae09c919a9a24a75c93dfacdb1775d7e9532b79de71ad8f5dd866d',UTC_TIMESTAMP(),999957);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (462090,UUID(),'19213988963','邵丹',NULL,1,45,'1','1','zh_CN','aa74e790f4ae9799f8ab90a755a03fa0','f0d01b7c49de8b14fd8548c83b63bbe378e5f40b3c41c9b95e3fb4cbc67a917e',UTC_TIMESTAMP(),999957);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (29241,1039387,'USER',462089,'张三',0,'13812345678',UTC_TIMESTAMP(),999957);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (29242,1039387,'USER',462090,'邵丹',0,'18005711288',UTC_TIMESTAMP(),999957);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (433591,462089,0,'13812345678',NULL,3,UTC_TIMESTAMP(),999957);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (433592,462090,0,'18005711288',NULL,3,UTC_TIMESTAMP(),999957);

INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`) 
	VALUES (2179535,1039387,'USER',462089,'manager','张三',0,'13812345678',3,999957,'ENTERPRISE',0);
INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`) 
	VALUES (2179536,1039387,'USER',462090,'manager','邵丹',0,'18005711288',3,999957,'ENTERPRISE',0);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (29039,462089,1039387,'/1039387','ENTERPRISE',3,999957,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (29040,462090,1039387,'/1039387','ENTERPRISE',3,999957,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115111,'EhOrganizations',1039387,'EhUsers',462089,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115112,'EhOrganizations',1039387,'EhUsers',462090,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970975,240111044332060058,'越秀星汇中心','',0,18005711288,'临安区青山湖街道大园路123号',NULL,'119.4925','30.155','wtkgx1ec953v','',NULL,2,0,NULL,1,NOW(),999957,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387462256,UUID(),240111044332060058,18414,'杭州市',18415,'临安区','越秀星汇中心-723','越秀星汇中心','723','2','0',UTC_TIMESTAMP(),999957,44462.19);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (85343,1039387,240111044332060058,239825274387462256,'越秀星汇中心-723',0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118372,10000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118373,10100,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118374,10400,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118375,10600,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118376,11000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118377,12200,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118378,20000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118379,20100,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118380,20140,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118381,20150,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118382,20155,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118383,20158,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118384,20170,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118385,20180,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118386,20190,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118387,20191,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118388,40000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118389,40200,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118390,40210,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118391,40220,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118392,40400,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118393,40410,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118394,40420,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118395,40430,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118396,40440,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118397,40450,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118398,40500,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118399,41000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118400,41010,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118401,41020,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118402,41030,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118403,41040,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118404,41050,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118405,41060,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118406,30000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118407,30500,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118408,30550,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118409,31000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118410,32000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118411,21200,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118412,21210,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118413,21220,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118414,21230,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118415,33000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118416,34000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118417,35000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118418,50000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118419,50100,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118420,50110,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118421,50300,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118422,50500,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118423,50800,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118424,50810,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118425,50820,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118426,50830,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118427,50840,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118428,50850,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118429,50860,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118430,60000,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118431,60100,NULL,'EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118432,60200,NULL,'EhNamespaces',999957,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001222,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999957,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001223,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999957,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001224,'',0,1001224,1,'活动管理-默认子分类','/1/1001224',0,2,1,NOW(),0,NULL,999957,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001225,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999957,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001226,'',0,1001226,2,'活动管理二-默认子分类','/2/1001226',0,2,1,NOW(),0,NULL,999957,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001227,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999957,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001228,'',0,1001228,3,'活动管理三-默认子分类','/3/1001228',0,2,1,NOW(),0,NULL,999957,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1039387,240111044332060058);

	
	
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`, `bg_image_uri`) 
	VALUES (876,999957,'HomeYuespaceLayout','{"versionCode":"2017120101","layoutName":"HomeYuespaceLayout","displayName":"越空间","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017120101,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0, 'cs://1/image/aW1hZ2UvTVRvMVptRm1ZV1JtTnpRek9EYzBZamcyWmpGbFpUZzRPR1l5WXprNFlXRmxNQQ');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`, `bg_image_uri`) 
	VALUES (877,999957,'HomeYuespaceLayout','{"versionCode":"2017120101","layoutName":"HomeYuespaceLayout","displayName":"越空间","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017120101,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0, 'cs://1/image/aW1hZ2UvTVRvMVptRm1ZV1JtTnpRek9EYzBZamcyWmpGbFpUZzRPR1l5WXprNFlXRmxNQQ');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`, `bg_image_uri`) 
	VALUES (878,999957,'HomeWuyeLayout','{"versionCode":"2017120101","layoutName":"HomeWuyeLayout","displayName":"物业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017120101,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0, 'cs://1/image/aW1hZ2UvTVRvMVpqTXdObU5sTkRReFkyWXlOVGszT1RSbE1ETTRPREkyWVRZd01UVXpNQQ');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`, `bg_image_uri`) 
	VALUES (879,999957,'HomeWuyeLayout','{"versionCode":"2017120101","layoutName":"HomeWuyeLayout","displayName":"物业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017120101,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0, 'cs://1/image/aW1hZ2UvTVRvMVpqTXdObU5sTkRReFkyWXlOVGszT1RSbE1ETTRPREkyWVRZd01UVXpNQQ');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`, `bg_image_uri`) 
	VALUES (880,999957,'HomeBusinessLayout','{"versionCode":"2017120101","layoutName":"HomeBusinessLayout","displayName":"商务服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017120101,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0, 'cs://1/image/aW1hZ2UvTVRwaE1tSTBZekUxT0RVNE9USXpaRFEzTkRkak5tTTFNVGd6WWpFd09HVXpZdw');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`, `bg_image_uri`) 
	VALUES (881,999957,'HomeBusinessLayout','{"versionCode":"2017120101","layoutName":"HomeBusinessLayout","displayName":"商务服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":10,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017120101,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0, 'cs://1/image/aW1hZ2UvTVRwaE1tSTBZekUxT0RVNE9USXpaRFEzTkRkak5tTTFNVGd6WWpFd09HVXpZdw');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (882,999957,'ServiceMarketLayout','{"versionCode":"2017120101","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017120101,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (883,999957,'ServiceMarketLayout','{"versionCode":"2017120101","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":20,"columnSpacing":20},"style":"Gallery","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":2}]}',2017120101,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216674,999957,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRveE1HTm1ZbUk0WldRNU1HWXhZMkZqTlRRd09XVXpPVEl3WVRVME9EWXhNQQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (216675,999957,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRveE1HTm1ZbUk0WldRNU1HWXhZMkZqTlRRd09XVXpPVEl3WVRVME9EWXhNQQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138188,999957,0,0,0,'/home','NavigatorGroup3','门禁','门禁','cs://1/image/aW1hZ2UvTVRwalpHSXhaV1l4TVRBd09EVTJZV05tTVdObU16WXpZV1l5TnpobE1EZGlaQQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138189,999957,0,0,0,'/home','NavigatorGroup3','门禁','门禁','cs://1/image/aW1hZ2UvTVRwalpHSXhaV1l4TVRBd09EVTJZV05tTVdObU16WXpZV1l5TnpobE1EZGlaQQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138190,999957,0,0,0,'/home','NavigatorGroup3','快递收发','快递收发','cs://1/image/aW1hZ2UvTVRvMk9XVmxPRGd5WWprNU1qWmlNakU0WmpZeU1tUmtZbUU1TURRMU1XVTVOUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1001?namespaceId=999957"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138191,999957,0,0,0,'/home','NavigatorGroup3','快递收发','快递收发','cs://1/image/aW1hZ2UvTVRvMk9XVmxPRGd5WWprNU1qWmlNakU0WmpZeU1tUmtZbUU1TURRMU1XVTVOUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1001?namespaceId=999957"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138192,999957,0,0,0,'/home','NavigatorGroup3','打印服务','打印服务','cs://1/image/aW1hZ2UvTVRvMk56STRZMlprTUdVMFl6Z3dNekUyT1dZeU9UTTNZVGt6TXpFeU0yWTVOUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1002?namespaceId=999957"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138193,999957,0,0,0,'/home','NavigatorGroup3','打印服务','打印服务','cs://1/image/aW1hZ2UvTVRvMk56STRZMlprTUdVMFl6Z3dNekUyT1dZeU9UTTNZVGt6TXpFeU0yWTVOUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1002?namespaceId=999957"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138194,999957,0,0,0,'/home','NavigatorGroup3','越空间','越空间','cs://1/image/aW1hZ2UvTVRvNU1HWmlNR1kyT0RNeVlUVTFaV001T1dabU1tRTJNbVJtTlRBd05EUXhOdw',1,1,2,'{"itemLocation":"/home/yuespace","layoutName":"HomeYuespaceLayout","title":"越空间","entityTag":"PM"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138195,999957,0,0,0,'/home','NavigatorGroup3','越空间','越空间','cs://1/image/aW1hZ2UvTVRvNU1HWmlNR1kyT0RNeVlUVTFaV001T1dabU1tRTJNbVJtTlRBd05EUXhOdw',1,1,2,'{"itemLocation":"/home/yuespace","layoutName":"HomeYuespaceLayout","title":"越空间","entityTag":"PM"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138196,999957,0,0,0,'/home','NavigatorGroup3','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRvNFpHSmhaV0ZpWVdFd1pHTTNPR1ZsTUdKbU0yWTNNVFUyWlRJeE9XWXhPQQ',1,1,2,'{"itemLocation":"/home/wuye","layoutName":"HomeWuyeLayout","title":"物业服务","entityTag":"PM"}',50,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138197,999957,0,0,0,'/home','NavigatorGroup3','物业服务','物业服务','cs://1/image/aW1hZ2UvTVRvNFpHSmhaV0ZpWVdFd1pHTTNPR1ZsTUdKbU0yWTNNVFUyWlRJeE9XWXhPQQ',1,1,2,'{"itemLocation":"/home/wuye","layoutName":"HomeWuyeLayout","title":"物业服务","entityTag":"PM"}',50,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138198,999957,0,0,0,'/home','NavigatorGroup3','商务服务','商务服务','cs://1/image/aW1hZ2UvTVRwaE1qUXhOekpoWlRSak9UUXhZV1l5T0RBd01EaGpORGhqTkRnd05EY3hOQQ',1,1,2,'{"itemLocation":"/home/business","layoutName":"HomeBusinessLayout","title":"商务服务","entityTag":"PM"}',60,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138199,999957,0,0,0,'/home','NavigatorGroup3','商务服务','商务服务','cs://1/image/aW1hZ2UvTVRwaE1qUXhOekpoWlRSak9UUXhZV1l5T0RBd01EaGpORGhqTkRnd05EY3hOQQ',1,1,2,'{"itemLocation":"/home/business","layoutName":"HomeBusinessLayout","title":"商务服务","entityTag":"PM"}',60,0,1,1,0,0,'park_tourist',0,0,'');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
-- 	VALUES (138200,999957,0,0,0,'/home','NavigatorGroup3','睡眠仓预定','睡眠仓预定','cs://1/image/aW1hZ2UvTVRwa1lXTTBPV1ZpWlRZM09XWTRabVpqWmpBd09XUTVOakppTkRFNFptWXdNUQ',1,1,49,'{"resourceTypeId":12175,"pageType":0}',70,0,1,1,0,0,'pm_admin',0,0,'');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
-- 	VALUES (138201,999957,0,0,0,'/home','NavigatorGroup3','睡眠仓预定','睡眠仓预定','cs://1/image/aW1hZ2UvTVRwa1lXTTBPV1ZpWlRZM09XWTRabVpqWmpBd09XUTVOakppTkRFNFptWXdNUQ',1,1,49,'{"resourceTypeId":12175,"pageType":0}',70,0,1,1,0,0,'park_tourist',0,0,'');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
-- 	VALUES (138202,999957,0,0,0,'/home','NavigatorGroup3','会议室','会议室','cs://1/image/aW1hZ2UvTVRvek16SXhOVFF5TXpkbU5qTTJOR0V4WmpCaVpUazRZalF5TUdFM1lUVTRaUQ',1,1,49,'{"resourceTypeId":12176,"pageType":0}',80,0,1,1,0,0,'pm_admin',0,0,'');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
-- 	VALUES (138203,999957,0,0,0,'/home','NavigatorGroup3','会议室','会议室','cs://1/image/aW1hZ2UvTVRvek16SXhOVFF5TXpkbU5qTTJOR0V4WmpCaVpUazRZalF5TUdFM1lUVTRaUQ',1,1,49,'{"resourceTypeId":12176,"pageType":0}',80,0,1,1,0,0,'park_tourist',0,0,'');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
-- 	VALUES (138204,999957,0,0,0,'/home','NavigatorGroup3','独立办公室','独立办公室','cs://1/image/aW1hZ2UvTVRwaE1qUXhOekpoWlRSak9UUXhZV1l5T0RBd01EaGpORGhqTkRnd05EY3hOQQ',1,1,49,'{"resourceTypeId":12177,"pageType":0}',90,0,1,1,0,0,'pm_admin',0,0,'');
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
-- 	VALUES (138205,999957,0,0,0,'/home','NavigatorGroup3','独立办公室','独立办公室','cs://1/image/aW1hZ2UvTVRwaE1qUXhOekpoWlRSak9UUXhZV1l5T0RBd01EaGpORGhqTkRnd05EY3hOQQ',1,1,49,'{"resourceTypeId":12177,"pageType":0}',90,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138206,999957,0,0,0,'/home/yuespace','NavigatorGroup1','越秀集团','越秀集团','cs://1/image/aW1hZ2UvTVRvd1pUaGlaalptWldGbVpEZ3paVFZpTVdJNU0yTTBaR0V3T1dOak5qVmpOdw',1,1,33,'{"type":212774,"parentId":212774,"displayType": "grid"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138207,999957,0,0,0,'/home/yuespace','NavigatorGroup1','越秀集团','越秀集团','cs://1/image/aW1hZ2UvTVRvd1pUaGlaalptWldGbVpEZ3paVFZpTVdJNU0yTTBaR0V3T1dOak5qVmpOdw',1,1,33,'{"type":212774,"parentId":212774,"displayType": "grid"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138208,999957,0,0,0,'/home/yuespace','NavigatorGroup1','八大服务体系','八大服务体系','cs://1/image/aW1hZ2UvTVRvNU1UVTNZamhsWVRJMllqQTVPRFk1TkRVd1l6WmlZelZqWWpoa1pHUTVaUQ',1,1,33,'{"type":212775,"parentId":212775,"displayType": "grid"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138209,999957,0,0,0,'/home/yuespace','NavigatorGroup1','八大服务体系','八大服务体系','cs://1/image/aW1hZ2UvTVRvNU1UVTNZamhsWVRJMllqQTVPRFk1TkRVd1l6WmlZelZqWWpoa1pHUTVaUQ',1,1,33,'{"type":212775,"parentId":212775,"displayType": "grid"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138210,999957,0,0,0,'/home/yuespace','NavigatorGroup1','物业百项服务','物业百项服务','cs://1/image/aW1hZ2UvTVRwak9HTmpaVGxrWlRRMFlXUTROekJpWTJKak9HWTRaR0U0WVRWaFlqUTJZdw',1,1,33,'{"type":212776,"parentId":212776,"displayType": "grid"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138211,999957,0,0,0,'/home/yuespace','NavigatorGroup1','物业百项服务','物业百项服务','cs://1/image/aW1hZ2UvTVRwak9HTmpaVGxrWlRRMFlXUTROekJpWTJKak9HWTRaR0U0WVRWaFlqUTJZdw',1,1,33,'{"type":212776,"parentId":212776,"displayType": "grid"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138212,999957,0,0,0,'/home/yuespace','NavigatorGroup1','越空间1','越空间1','cs://1/image/aW1hZ2UvTVRvNU1HWmlNR1kyT0RNeVlUVTFaV001T1dabU1tRTJNbVJtTlRBd05EUXhOdw',1,1,33,'{"type":212777,"parentId":212777,"displayType": "grid"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138213,999957,0,0,0,'/home/yuespace','NavigatorGroup1','越空间1','越空间1','cs://1/image/aW1hZ2UvTVRvNU1HWmlNR1kyT0RNeVlUVTFaV001T1dabU1tRTJNbVJtTlRBd05EUXhOdw',1,1,33,'{"type":212777,"parentId":212777,"displayType": "grid"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138214,999957,0,0,0,'/home/yuespace','NavigatorGroup1','入驻团队','入驻团队','cs://1/image/aW1hZ2UvTVRwa1lXTTBPV1ZpWlRZM09XWTRabVpqWmpBd09XUTVOakppTkRFNFptWXdNUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1003?namespaceId=999957"}',50,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138215,999957,0,0,0,'/home/yuespace','NavigatorGroup1','入驻团队','入驻团队','cs://1/image/aW1hZ2UvTVRwa1lXTTBPV1ZpWlRZM09XWTRabVpqWmpBd09XUTVOakppTkRFNFptWXdNUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1003?namespaceId=999957"}',50,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138216,999957,0,0,0,'/home/yuespace','NavigatorGroup1','入孵申请','入孵申请','cs://1/image/aW1hZ2UvTVRveVlUUXpZek0yT0dFM05HRXpORFl5WlRrNE1tWXhZamRtTnpBelpHSTFNQQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1004?namespaceId=999957"}',60,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138217,999957,0,0,0,'/home/yuespace','NavigatorGroup1','入孵申请','入孵申请','cs://1/image/aW1hZ2UvTVRveVlUUXpZek0yT0dFM05HRXpORFl5WlRrNE1tWXhZamRtTnpBelpHSTFNQQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1004?namespaceId=999957"}',60,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138218,999957,0,0,0,'/home/yuespace','NavigatorGroup1','客服服务','客服服务','cs://1/image/aW1hZ2UvTVRvd01EZGpPR0U0T1dZMlpUZzRNbVkxTTJJNFpERTBZbUpsWkRSaU16VTFNUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1005?namespaceId=999957"}',70,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138219,999957,0,0,0,'/home/yuespace','NavigatorGroup1','客服服务','客服服务','cs://1/image/aW1hZ2UvTVRvd01EZGpPR0U0T1dZMlpUZzRNbVkxTTJJNFpERTBZbUpsWkRSaU16VTFNUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1005?namespaceId=999957"}',70,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138220,999957,0,0,0,'/home/wuye','NavigatorGroup1','设备报修','设备报修','cs://1/image/aW1hZ2UvTVRvNFpHWXhOalk0WkRVeVlXVTVZVFUyT1RVd09USmhaREl5TlRRNE0ySXlNQQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203825&displayName=设备报修"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138221,999957,0,0,0,'/home/wuye','NavigatorGroup1','设备报修','设备报修','cs://1/image/aW1hZ2UvTVRvNFpHWXhOalk0WkRVeVlXVTVZVFUyT1RVd09USmhaREl5TlRRNE0ySXlNQQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203825&displayName=设备报修"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138222,999957,0,0,0,'/home/wuye','NavigatorGroup1','停车缴费','停车缴费','cs://1/image/aW1hZ2UvTVRvNU5UYzBaRFk1TmpJM05HTTFNVFV4T1RFNU9ETmhPR1k1T1RjME56SmtOZw',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1006?namespaceId=999957"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138223,999957,0,0,0,'/home/wuye','NavigatorGroup1','停车缴费','停车缴费','cs://1/image/aW1hZ2UvTVRvNU5UYzBaRFk1TmpJM05HTTFNVFV4T1RFNU9ETmhPR1k1T1RjME56SmtOZw',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1006?namespaceId=999957"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138224,999957,0,0,0,'/home/wuye','NavigatorGroup1','服务预定','服务预定','cs://1/image/aW1hZ2UvTVRvME5tUXpaalJrT0RRME9UVmhZbU0yWTJSaU5EaGxPRE0wTkdRek1qUTRNZw',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1007?namespaceId=999957"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138225,999957,0,0,0,'/home/wuye','NavigatorGroup1','服务预定','服务预定','cs://1/image/aW1hZ2UvTVRvME5tUXpaalJrT0RRME9UVmhZbU0yWTJSaU5EaGxPRE0wTkdRek1qUTRNZw',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1007?namespaceId=999957"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138226,999957,0,0,0,'/home/wuye','NavigatorGroup1','车位预定','车位预定','cs://1/image/aW1hZ2UvTVRvek16SXhOVFF5TXpkbU5qTTJOR0V4WmpCaVpUazRZalF5TUdFM1lUVTRaUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1008?namespaceId=999957"}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138227,999957,0,0,0,'/home/wuye','NavigatorGroup1','车位预定','车位预定','cs://1/image/aW1hZ2UvTVRvek16SXhOVFF5TXpkbU5qTTJOR0V4WmpCaVpUazRZalF5TUdFM1lUVTRaUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1008?namespaceId=999957"}',40,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138228,999957,0,0,0,'/home/wuye','NavigatorGroup1','日常物品租赁','日常物品租赁','cs://1/image/aW1hZ2UvTVRvMFlXSm1NamRsWTJSa05XRTVPRE5qWVdRNU9UWXhNRE5pWldJMVpESmpZdw',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1009?namespaceId=999957"}',50,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138229,999957,0,0,0,'/home/wuye','NavigatorGroup1','日常物品租赁','日常物品租赁','cs://1/image/aW1hZ2UvTVRvMFlXSm1NamRsWTJSa05XRTVPRE5qWVdRNU9UWXhNRE5pWldJMVpESmpZdw',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1009?namespaceId=999957"}',50,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138230,999957,0,0,0,'/home/wuye','NavigatorGroup1','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRwbVpETTJZVEU0T0dGak5EZzBPR1kzTURjMk1UZGxNV1l6T1RRMk5tWmlZdw',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1010?namespaceId=999957"}',60,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138231,999957,0,0,0,'/home/wuye','NavigatorGroup1','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRwbVpETTJZVEU0T0dGak5EZzBPR1kzTURjMk1UZGxNV1l6T1RRMk5tWmlZdw',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1010?namespaceId=999957"}',60,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138232,999957,0,0,0,'/home/wuye','NavigatorGroup1','账单信息','账单信息','cs://1/image/aW1hZ2UvTVRvd1lXWm1NVGhtWlRVM01XUmtNV0UzTldJMllUQTNOVFl5Tm1RNFlUTXpOUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1011?namespaceId=999957"}',70,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138233,999957,0,0,0,'/home/wuye','NavigatorGroup1','账单信息','账单信息','cs://1/image/aW1hZ2UvTVRvd1lXWm1NVGhtWlRVM01XUmtNV0UzTldJMllUQTNOVFl5Tm1RNFlUTXpOUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1011?namespaceId=999957"}',70,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138234,999957,0,0,0,'/home/business','NavigatorGroup1','生活配套','生活配套','cs://1/image/aW1hZ2UvTVRvek16SXhOVFF5TXpkbU5qTTJOR0V4WmpCaVpUazRZalF5TUdFM1lUVTRaUQ',1,1,14,'{"url":"https://biz.zuolin.com/nar/biz/web/app/user/index.html?mallId=4000000#/store/details/15119397810605945049"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138235,999957,0,0,0,'/home/business','NavigatorGroup1','生活配套','生活配套','cs://1/image/aW1hZ2UvTVRvek16SXhOVFF5TXpkbU5qTTJOR0V4WmpCaVpUazRZalF5TUdFM1lUVTRaUQ',1,1,14,'{"url":"https://biz.zuolin.com/nar/biz/web/app/user/index.html?mallId=4000000#/store/details/15119397810605945049"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138236,999957,0,0,0,'/home/business','NavigatorGroup1','企业服务','企业服务','cs://1/image/aW1hZ2UvTVRwaFlUbGhNV0poWkROak4yTXpOelU0Tm1ZeVpERTROekU0TnpobE5URmhPQQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1012?namespaceId=999957"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138237,999957,0,0,0,'/home/business','NavigatorGroup1','企业服务','企业服务','cs://1/image/aW1hZ2UvTVRwaFlUbGhNV0poWkROak4yTXpOelU0Tm1ZeVpERTROekU0TnpobE5URmhPQQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1012?namespaceId=999957"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138238,999957,0,0,0,'/home/business','NavigatorGroup1','访客预约','访客预约','cs://1/image/aW1hZ2UvTVRvME5UZzBPR0l3TjJNNE1USXhPV0pqTmpVeU1HSTFNalV3T0RRMU16TmtNUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1013?namespaceId=999957"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (138239,999957,0,0,0,'/home/business','NavigatorGroup1','访客预约','访客预约','cs://1/image/aW1hZ2UvTVRvME5UZzBPR0l3TjJNNE1USXhPV0pqTmpVeU1HSTFNalV3T0RRMU16TmtNUQ',1,1,14,'{"url":"${home.url}/service-hub/build/index.html#/waiting/1013?namespaceId=999957"}',30,0,1,1,0,0,'park_tourist',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212774,'community','240111044331055940',0,'越秀集团','越秀集团',0,2,1,UTC_TIMESTAMP(),999957,1);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212775,'community','240111044331055940',0,'八大服务体系','八大服务体系',0,2,1,UTC_TIMESTAMP(),999957,2);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212776,'community','240111044331055940',0,'物业百项服务','物业百项服务',0,2,1,UTC_TIMESTAMP(),999957,3);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (212777,'community','240111044331055940',0,'越空间','越空间',0,2,1,UTC_TIMESTAMP(),999957,4);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211324,0,'organaization',1039387,'all','越秀集团','越秀集团',212774,2,211324,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211325,0,'organaization',1039387,'all','八大服务体系','八大服务体系',212775,2,211325,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211326,0,'organaization',1039387,'all','物业百项服务','物业百项服务',212776,2,211326,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211327,0,'organaization',1039387,'all','越空间','越空间',212777,2,211327,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118436,41700,'越秀集团','EhNamespaces',999957,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118437,41710,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118438,41720,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118439,41730,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118440,41740,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118441,41750,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118442,41760,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118443,41800,'八大服务体系','EhNamespaces',999957,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118444,41810,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118445,41820,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118446,41830,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118447,41840,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118448,41850,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118449,41860,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118450,41900,'物业百项服务','EhNamespaces',999957,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118451,41910,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118452,41920,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118453,41930,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118454,41940,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118455,41950,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118456,41960,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118457,42000,'越空间1','EhNamespaces',999957,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118458,42010,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118459,42020,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118460,42030,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118461,42040,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118462,42050,'','EhNamespaces',999957,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (118463,42060,'','EhNamespaces',999957,2);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (203825,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999957);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12175,'睡眠仓预定',0,2,999957,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12176,'会议室',0,2,999957,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12177,'独立办公室',0,2,999957,0,0);
