INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2082,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999952','the relative path for 创集合 app agreements',999952,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2083,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for 创集合',999952,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2084,'pmtask.handler-999952','flow','0',999952,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2085,'pay.platform','1','支付类型',999952,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (617,'Android_ChuanJiHe',NULL,UTC_TIMESTAMP(),999952);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (618,'iOS_ChuanJiHe',NULL,UTC_TIMESTAMP(),999952);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (977,617,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999952);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (978,618,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999952);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999952,'创集合');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1610,999952,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (1042,'sms.default.sign',0,'zh_CN','CJH','【CJH】',999952);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18571,0,'北京','BEIJING','BJ','/北京',1,1,NULL,NULL,2,0,999952);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18572,18571,'北京市','BEIJINGSHI','BJS','/北京/北京市',2,2,NULL,'010',2,1,999952);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18573,18572,'昌平区','CHANGPINGQU','CPQ','/北京/北京市/昌平区',3,3,NULL,'010',2,0,999952);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060207,UUID(),18572,'北京市',18573,'昌平区','创集合创新创业扶持平台','创集合','龙域北街10号院','创集合是一家以市场为导向的创新创业扶持平台，致力于为入驻企业提供BBC产业链服务，全面负责创客群体从企业生态管理到业绩指数型提升的战略家服务。',0,1,2,UTC_TIMESTAMP(),1,192882,192883,UTC_TIMESTAMP(),999952);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092980,240111044332060207,'',116.3193,40.0713,'wx4eyyv0hpsr');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20317,999952,'COMMUNITY',240111044332060207,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1041521,0,'PM','创集合物业部','创集合是一家以市场为导向的创新创业扶持平台，提供从前端到后端的全产业链服务。','/1041521',1,2,'ENTERPRISE',999952,1059411);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1156463,240111044332060207,'organization',1041521,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37689,'EhOrganizations',1041521,1,10,484230,0,1,UTC_TIMESTAMP(),999952,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37690,'EhOrganizations',1041521,1,10,484231,0,1,UTC_TIMESTAMP(),999952,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37691,'EhOrganizations',1041521,1,10,484232,0,1,UTC_TIMESTAMP(),999952,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37692,'EhOrganizations',1041521,1,10,484233,0,1,UTC_TIMESTAMP(),999952,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1059411,UUID(),'创集合物业部','创集合物业部',1,1,1041521,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192881,1,999952);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192881,UUID(),999952,2,'EhGroups',1059411,'创集合物业部论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192882,UUID(),999952,2,'',0,'CJH社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192883,UUID(),999952,2,'',0,'CJH意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256654,UUID(),999952,192882,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (484230,UUID(),'19269011024','张佳瑞',NULL,1,45,'1','2','zh_CN','aa0f1f300e423ed62caa47e5598950be','72141e25544d7057f7d7cd403e390cc4f5215effca422f9b9f7b2e21d752eb38',UTC_TIMESTAMP(),999952);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (484231,UUID(),'19269011025','唐红',NULL,1,45,'1','2','zh_CN','41024a481b8cf67914988196020c6bd3','4c1fccd805f3b49c54988cafd5b224f3dbba6116a8c93872dbaa82660da310dc',UTC_TIMESTAMP(),999952);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (484232,UUID(),'19269011026','李强',NULL,1,45,'1','1','zh_CN','bfcbfc1750e5c4a42b7084cb920ecd23','68183f6959cb9e6051cb28da1b24d63f8821bab788348e1f08e070bec17e883d',UTC_TIMESTAMP(),999952);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (484233,UUID(),'19269011027','左邻官方账号',NULL,1,45,'1','1','zh_CN','c73d7fae4e50b108b9b78fb979d50ad6','aa3dee2919c756f5a79fb2312424d3b6f42a3aafccb108404c626246fa4cd492',UTC_TIMESTAMP(),999952);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32055,1041521,'USER',484230,'张佳瑞',0,'13811323207',UTC_TIMESTAMP(),999952);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32056,1041521,'USER',484231,'唐红',0,'15650723221',UTC_TIMESTAMP(),999952);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32057,1041521,'USER',484232,'李强',0,'18042684464',UTC_TIMESTAMP(),999952);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32058,1041521,'USER',484233,'左邻官方账号',0,'12000001802',UTC_TIMESTAMP(),999952);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (455236,484230,0,'13811323207',NULL,3,UTC_TIMESTAMP(),999952);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (455237,484231,0,'15650723221',NULL,3,UTC_TIMESTAMP(),999952);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (455238,484232,0,'18042684464',NULL,3,UTC_TIMESTAMP(),999952);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (455239,484233,0,'12000001802',NULL,3,UTC_TIMESTAMP(),999952);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184969,1041521,'/1041521','USER',484230,'manager','张佳瑞',0,'13811323207',3,999952,'ENTERPRISE',0,32055);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184970,1041521,'/1041521','USER',484231,'manager','唐红',0,'15650723221',3,999952,'ENTERPRISE',0,32056);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184971,1041521,'/1041521','USER',484232,'manager','李强',0,'18042684464',3,999952,'ENTERPRISE',0,32057);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184972,1041521,'/1041521','USER',484233,'manager','左邻官方账号',0,'12000001802',3,999952,'ENTERPRISE',0,32058);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31694,484230,1041521,'/1041521','ENTERPRISE',3,999952,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31695,484231,1041521,'/1041521','ENTERPRISE',3,999952,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31696,484232,1041521,'/1041521','ENTERPRISE',3,999952,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31697,484233,1041521,'/1041521','ENTERPRISE',3,999952,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115570,'EhOrganizations',1041521,'EhUsers',484230,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115571,'EhOrganizations',1041521,'EhUsers',484231,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115572,'EhOrganizations',1041521,'EhUsers',484232,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115573,'EhOrganizations',1041521,'EhUsers',484233,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971556,240111044332060207,'创集合','C',0,13811323207,'北京市昌平区龙域北街10号院',10000.0,'116.3193','40.0713','wx4eyyv0hpsr','',NULL,2,0,NULL,1,NOW(),999952,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467614,UUID(),240111044332060207,18572,'北京市',18573,'昌平区','创集合-101','创集合','101','2','0',UTC_TIMESTAMP(),999952,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467615,UUID(),240111044332060207,18572,'北京市',18573,'昌平区','创集合-102','创集合','102','2','0',UTC_TIMESTAMP(),999952,NULL);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90999,1041521,240111044332060207,239825274387467614,'创集合-101',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (91000,1041521,240111044332060207,239825274387467615,'创集合-102',0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141513,60000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141514,60100,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141515,60200,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141430,10000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141431,10100,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141432,10400,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141433,10200,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141434,10600,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141435,10750,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141436,10751,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141437,10752,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141438,10800,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141439,20000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141440,20100,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141441,20140,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141442,20150,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141443,20155,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141444,20158,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141445,20170,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141446,20180,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141447,20190,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141448,20191,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141449,21200,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141450,21210,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141451,21220,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141452,21230,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141453,20400,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141454,204011,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141455,204021,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141456,20430,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141457,20422,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141458,40000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141459,40100,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141460,40103,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141461,40105,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141462,40110,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141463,40120,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141464,40130,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141465,40200,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141466,40210,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141467,40220,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141468,40300,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141469,40400,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141470,40410,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141471,40420,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141472,40430,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141473,40440,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141474,40450,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141475,40500,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141476,41000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141477,41010,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141478,41020,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141479,41030,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141480,41040,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141481,41050,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141482,41060,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141483,30000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141484,30500,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141485,30550,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141486,31000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141487,32000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141488,33000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141489,34000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141490,35000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141491,21100,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141492,21110,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141493,21120,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141494,50000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141495,50100,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141496,50300,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141497,50400,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141498,50500,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141499,50600,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141500,50700,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141501,50710,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141502,50720,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141503,50730,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141504,50900,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141505,52000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141506,52010,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141507,52020,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141508,52030,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141509,70000,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141510,70300,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141511,70100,NULL,'EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141512,70200,NULL,'EhNamespaces',999952,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001431,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999952,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001432,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999952,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001433,'',0,1001433,1,'活动管理-默认子分类','/1/1001433',0,2,1,NOW(),0,NULL,999952,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001434,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999952,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001435,'',0,1001435,2,'活动管理二-默认子分类','/2/1001435',0,2,1,NOW(),0,NULL,999952,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001436,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999952,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001437,'',0,1001437,3,'活动管理三-默认子分类','/3/1001437',0,2,1,NOW(),0,NULL,999952,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (582,999952,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (583,999952,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (584,999952,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (585,999952,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (586,999952,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (587,999952,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (588,999952,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (589,999952,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (590,999952,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1041521,240111044332060207);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1143,999952,'SecondServiceMarketLayout','{"versionCode":"2018020801","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize":3,"entityCount":3,"subjectHeight":1,"descriptionHeight":1},"style": "ListView","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018020801,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1144,999952,'SecondServiceMarketLayout','{"versionCode":"2018020801","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize":3,"entityCount":3,"subjectHeight":1,"descriptionHeight":1},"style": "ListView","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018020801,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1145,999952,'ServiceMarketLayout','{"versionCode":"2018020801","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":42,"paddingBottom":42,"paddingLeft":32,"paddingRight":32,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"title":"空间活动","iconUrl": "https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize":3,"entityCount":3,"subjectHeight":1,"descriptionHeight":1},"style": "ListView","defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2018020801,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1146,999952,'ServiceMarketLayout','{"versionCode":"2018020801","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":42,"paddingBottom":42,"paddingLeft":32,"paddingRight":32,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"title":"空间活动","iconUrl": "https://content-1.zuolin.com:443/image/aW1hZ2UvTVRveU5UVmtNVFpqTnpBek1URXpOREkzTkRnMU1qWTFOMlZrWVdKa1lXTTNZdw?token=XlYdOjlDVEVb4XyQO4_dd5RI37zTkV3jCKm_-XbRyLIGVUorWGnyRCwLAgMGV86baX30BnQW4nqzF9nlXGe4M0DbZxWBVTqnL019xazIDuhE6A0OXiMQwRqGX84_1HHv","groupName": "","widget": "OPPush","instanceConfig": {"itemGroup": "OPPushActivity","newsSize":3,"entityCount":3,"subjectHeight":1,"descriptionHeight":1},"style": "ListView","defaultOrder":50,"separatorFlag":0,"separatorHeight":0}]}',2018020801,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217091,999952,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRwaVl6SXdOR000WmpNeVl6RmhNek01TmpZMU9URm1aV1ptTkRCak5UTXhPUQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217092,999952,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRwaVl6SXdOR000WmpNeVl6RmhNek01TmpZMU9URm1aV1ptTkRCak5UTXhPUQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161099,999952,0,0,0,'/home','NavigatorGroup2','免费工位预订','免费工位预订','cs://1/image/aW1hZ2UvTVRveE5UbGtaRFEzWXprNFpHRm1ZV05sWldFNE16QXpZalpsTmpnMU1HUXlPQQ',1,1,49,'{"resourceTypeId":12326,"pageType":0}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161100,999952,0,0,0,'/home','NavigatorGroup2','免费工位预订','免费工位预订','cs://1/image/aW1hZ2UvTVRveE5UbGtaRFEzWXprNFpHRm1ZV05sWldFNE16QXpZalpsTmpnMU1HUXlPQQ',1,1,49,'{"resourceTypeId":12326,"pageType":0}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161101,999952,0,0,0,'/home','NavigatorGroup2','商城','商城','cs://1/image/aW1hZ2UvTVRveU1qQmxPVFl6Wm1ZME9EZzVNV05rTlRBNU9URXhPRGczTkdRMU1qazFNQQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161102,999952,0,0,0,'/home','NavigatorGroup2','商城','商城','cs://1/image/aW1hZ2UvTVRveU1qQmxPVFl6Wm1ZME9EZzVNV05rTlRBNU9URXhPRGczTkdRMU1qazFNQQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161103,999952,0,0,0,'/home','NavigatorGroup4','智能门禁','智能门禁','cs://1/image/aW1hZ2UvTVRveU1HSTRNekU0WVdGaU9HWTROMkprWkdNNVpUUTFNRGxoTmpNeVlUQTFZUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161104,999952,0,0,0,'/home','NavigatorGroup4','智能门禁','智能门禁','cs://1/image/aW1hZ2UvTVRveU1HSTRNekU0WVdGaU9HWTROMkprWkdNNVpUUTFNRGxoTmpNeVlUQTFZUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161105,999952,0,0,0,'/home','NavigatorGroup4','考勤打卡','考勤打卡','cs://1/image/aW1hZ2UvTVRvM1ptSmpaVGRqTURFNU5XTXpPR0kyTWpSak5qZGpabVptTm1VNVl6VTBZZw',1,1,23,'',20,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161106,999952,0,0,0,'/home','NavigatorGroup4','考勤打卡','考勤打卡','cs://1/image/aW1hZ2UvTVRvM1ptSmpaVGRqTURFNU5XTXpPR0kyTWpSak5qZGpabVptTm1VNVl6VTBZZw',1,1,23,'',20,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161107,999952,0,0,0,'/home','NavigatorGroup4','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvME5XWmpORGhoTnpGa1pEUmxZakZoTW1OaU1UazVPV1l5WlRreE9XRm1ZUQ',1,1,45,'',30,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161108,999952,0,0,0,'/home','NavigatorGroup4','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvME5XWmpORGhoTnpGa1pEUmxZakZoTW1OaU1UazVPV1l5WlRreE9XRm1ZUQ',1,1,45,'',30,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161109,999952,0,0,0,'/home','NavigatorGroup4','招商入驻','招商入驻','cs://1/image/aW1hZ2UvTVRvMVltUm1aVGxoTldNNU56azNaV000TTJJMk9ERmlNV1ZsTmpFeE56QXpaUQ',1,1,28,'',40,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161110,999952,0,0,0,'/home','NavigatorGroup4','招商入驻','招商入驻','cs://1/image/aW1hZ2UvTVRvMVltUm1aVGxoTldNNU56azNaV000TTJJMk9ERmlNV1ZsTmpFeE56QXpaUQ',1,1,28,'',40,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161111,999952,0,0,0,'/home','NavigatorGroup4','会议室预定','会议室预定','cs://1/image/aW1hZ2UvTVRwa09XSTVOakF4T0RnMll6Sm1NemRpWVRJMU56RXlOR1UyTmpJNE5tTXhNQQ',1,1,49,'{"resourceTypeId":12327,"pageType":0}',50,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161112,999952,0,0,0,'/home','NavigatorGroup4','会议室预定','会议室预定','cs://1/image/aW1hZ2UvTVRwa09XSTVOakF4T0RnMll6Sm1NemRpWVRJMU56RXlOR1UyTmpJNE5tTXhNQQ',1,1,49,'{"resourceTypeId":12327,"pageType":0}',50,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161113,999952,0,0,0,'/home','NavigatorGroup4','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvek5XTmhObUkzWVdGbU5ETTFNbVF3WWpWaE9XVTVOakl5Wm1RNU5HRTFPQQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',60,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161114,999952,0,0,0,'/home','NavigatorGroup4','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvek5XTmhObUkzWVdGbU5ETTFNbVF3WWpWaE9XVTVOakl5Wm1RNU5HRTFPQQ',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',60,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161115,999952,0,0,0,'/home','NavigatorGroup4','企业展厅','企业展厅','cs://1/image/aW1hZ2UvTVRvd1pUZGlOalUyTlRObU9EQXhOelF4TkdJek5tUXpOR0U0WVRFM05HSmxNdw',1,1,34,'',70,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161116,999952,0,0,0,'/home','NavigatorGroup4','企业展厅','企业展厅','cs://1/image/aW1hZ2UvTVRvd1pUZGlOalUyTlRObU9EQXhOelF4TkdJek5tUXpOR0U0WVRFM05HSmxNdw',1,1,34,'',70,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161117,999952,0,0,0,'/home','NavigatorGroup4','更多','更多','cs://1/image/aW1hZ2UvTVRvME5tWmhOREZoT1dKaE5tWmxPRGc0WVRFeE1UWXlZbUZqWkRsak56VmxNZw',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup4"}',1000,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161118,999952,0,0,0,'/home','NavigatorGroup4','更多','更多','cs://1/image/aW1hZ2UvTVRvME5tWmhOREZoT1dKaE5tWmxPRGc0WVRFeE1UWXlZbUZqWkRsak56VmxNZw',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup4"}',1000,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161119,999952,0,0,0,'/home','NavigatorGroup4','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRvMFpUQXdOV1JqTVdFd01tVTNaRFl6TkRnME16YzBZakptT0RJME1ETmlNQQ',1,1,33,'{"type":213106,"parentId":213106,"displayType": "tab"}',90,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161120,999952,0,0,0,'/home','NavigatorGroup4','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRvMFpUQXdOV1JqTVdFd01tVTNaRFl6TkRnME16YzBZakptT0RJME1ETmlNQQ',1,1,33,'{"type":213106,"parentId":213106,"displayType": "tab"}',90,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161121,999952,0,0,0,'/home','NavigatorGroup4','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRvd1lXSmhaREZrWkdZNU0yRmlOMkl4TjJNeU1tSmpPRFU1TkdWbFl6QXdaZw',1,1,46,'',100,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161122,999952,0,0,0,'/home','NavigatorGroup4','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRvd1lXSmhaREZrWkdZNU0yRmlOMkl4TjJNeU1tSmpPRFU1TkdWbFl6QXdaZw',1,1,46,'',100,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161123,999952,0,0,0,'/home','NavigatorGroup4','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRveE1UTXlOalUwTnpFMU9URmpZek00TlRGak5XVXlZV1kyT0RkbU5tTmtaZw',1,1,27,'',110,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161124,999952,0,0,0,'/home','NavigatorGroup4','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRveE1UTXlOalUwTnpFMU9URmpZek00TlRGak5XVXlZV1kyT0RkbU5tTmtaZw',1,1,27,'',110,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161125,999952,0,0,0,'/home','NavigatorGroup4','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRwa05XUTBNRFppTlRRNU5UVmpNREZpT0dZMlpUZGpPR0kwTVRRd056WTJaQQ',1,1,36,'{"privateFlag": 0}',120,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161126,999952,0,0,0,'/home','NavigatorGroup4','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRwa05XUTBNRFppTlRRNU5UVmpNREZpT0dZMlpUZGpPR0kwTVRRd056WTJaQQ',1,1,36,'{"privateFlag": 0}',120,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161127,999952,0,0,0,'/home','NavigatorGroup4','店铺管理','店铺管理','cs://1/image/aW1hZ2UvTVRvME1HUmxNVE5oTnpVeU5qSXdPRE13TUdabE5USTRPV1l5WVdVeE9ESTBNdw',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',130,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161128,999952,0,0,0,'/home','NavigatorGroup4','店铺管理','店铺管理','cs://1/image/aW1hZ2UvTVRvME1HUmxNVE5oTnpVeU5qSXdPRE13TUdabE5USTRPV1l5WVdVeE9ESTBNdw',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',130,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161129,999952,0,0,0,'/home','Default','空间快讯','空间快讯','cs://1/image/aW1hZ2UvTVRwaE5tTTROVGs0WlRrNE1tWm1ORGxtWVdFMll6ZzNZbU0xTXpoaE9HRTBZZw',1,1,48,NULL,140,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161130,999952,0,0,0,'/home','Default','空间快讯','空间快讯','cs://1/image/aW1hZ2UvTVRwaE5tTTROVGs0WlRrNE1tWm1ORGxtWVdFMll6ZzNZbU0xTXpoaE9HRTBZZw',1,1,48,NULL,140,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161131,999952,0,0,0,'/home','NavigatorGroup4','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRvNU1qazJaVEZoTmpJMFpUQmxaak5tTnpreE5EaG1OR0ppWWpkaU5XTXlZUQ',1,1,56,'',150,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161132,999952,0,0,0,'/home','NavigatorGroup4','任务管理','任务管理','cs://1/image/aW1hZ2UvTVRvNU1qazJaVEZoTmpJMFpUQmxaak5tTnpreE5EaG1OR0ppWWpkaU5XTXlZUQ',1,1,56,'',150,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161133,999952,0,0,0,'/home','NavigatorGroup4','空间活动','空间活动','cs://1/image/aW1hZ2UvTVRvMk5XWmxaVEE0WmpjMk9ERmtOVGt5TnpKaFkyVmhObUl5TmpoaE4yRmtNUQ',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "空间活动"}',160,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161134,999952,0,0,0,'/home','NavigatorGroup4','空间活动','空间活动','cs://1/image/aW1hZ2UvTVRvMk5XWmxaVEE0WmpjMk9ERmtOVGt5TnpKaFkyVmhObUl5TmpoaE4yRmtNUQ',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "空间活动"}',160,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161135,999952,0,0,0,'/home','NavigatorGroup2','空间介绍','空间介绍','cs://1/image/aW1hZ2UvTVRvMk5XWmxaVEE0WmpjMk9ERmtOVGt5TnpKaFkyVmhObUl5TmpoaE4yRmtNUQ',1,1,33,'{"type":213107,"parentId":213107,"displayType": "list"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161136,999952,0,0,0,'/home','NavigatorGroup2','空间介绍','空间介绍','cs://1/image/aW1hZ2UvTVRvMk5XWmxaVEE0WmpjMk9ERmtOVGt5TnpKaFkyVmhObUl5TmpoaE4yRmtNUQ',1,1,33,'{"type":213107,"parentId":213107,"displayType": "list"}',30,0,1,1,0,0,'park_tourist',0,0,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213106,'community','240111044331055940',0,'服务联盟','服务联盟',0,2,1,UTC_TIMESTAMP(),999952,1);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213107,'community','240111044331055940',0,'空间介绍','空间介绍',0,2,1,UTC_TIMESTAMP(),999952,2);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211811,0,'organaization',1041521,'all','服务联盟','服务联盟',213106,2,211811,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (211812,0,'organaization',1041521,'all','空间介绍','空间介绍',213107,2,211812,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141518,41700,'服务联盟','EhNamespaces',999952,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141519,41710,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141520,41720,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141521,41730,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141522,41740,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141523,41750,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141524,41760,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141525,41800,'空间介绍','EhNamespaces',999952,1);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141526,41810,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141527,41820,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141528,41830,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141529,41840,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141530,41850,'','EhNamespaces',999952,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141531,41860,'','EhNamespaces',999952,2);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (204154,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999952);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12326,'免费工位预订',0,2,999952,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12327,'会议室预定',0,2,999952,0,0);
    
set @namespaceId = 999952;
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


