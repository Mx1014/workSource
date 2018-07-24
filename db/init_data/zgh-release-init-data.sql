INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2309,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999945','the relative path for 智谷汇 app agreements',999945,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2310,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for 智谷汇',999945,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2311,'pmtask.handler-999945','flow','0',999945,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2312,'pay.platform','1','支付类型',999945,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (833,'Android_ZhiGuHui',NULL,UTC_TIMESTAMP(),999945);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (834,'iOS_ZhiGuHui',NULL,UTC_TIMESTAMP(),999945);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (1207,833,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999945);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (1208,834,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999945);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999945,'智谷汇');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1821,999945,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (1262,'sms.default.sign',0,'zh_CN','智谷汇','【智谷汇】',999945);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18793,0,'四川','SICHUAN','SC','/四川',1,1,NULL,NULL,2,0,999945);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18794,18793,'成都市','CHENGDUSHI','CDS','/四川/成都市',2,2,NULL,'028',2,1,999945);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18795,18794,'武侯区','WUHOUQU','WHQ','/四川/成都市/武侯区',3,3,NULL,'028',2,0,999945);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060423,UUID(),18794,'成都市',18795,'武侯区','西部智谷','西部智谷','四川省成都市武侯区武兴四路','西部智谷',0,1,2,UTC_TIMESTAMP(),1,193142,193143,UTC_TIMESTAMP(),999945);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331093196,240111044332060423,'',104.120174,30.622351,'wm6n1q9upguy');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20533,999945,'COMMUNITY',240111044332060423,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1043219,0,'PM','成都新创创业孵化器服务有限公司','新创公司是西部一流产业运营商','/1043219',1,2,'ENTERPRISE',999945,1060026);

INSERT INTO `eh_organization_details` (`id`, `organization_id`,`contact`, `address`, `create_time`, `longitude`, `latitude`, `geohash`, `display_name`, `contactor`, `member_count`) 
	VALUES (19230,1043219,NULL,NULL,NOW(),NULL,NULL,NULL,NULL,NULL,0);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1158050,240111044332060423,'organization',1043219,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (40662,'EhOrganizations',1043219,1,10,497199,0,1,UTC_TIMESTAMP(),999945,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (40663,'EhOrganizations',1043219,1,10,497200,0,1,UTC_TIMESTAMP(),999945,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1060026,UUID(),'成都新创创业孵化器服务有限公司','成都新创创业孵化器服务有限公司',1,1,1043219,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),193141,1,999945);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (193141,UUID(),999945,2,'EhGroups',1060026,'成都新创创业孵化器服务有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (193142,UUID(),999945,2,'',0,'智谷汇社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (193143,UUID(),999945,2,'',0,'智谷汇意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256892,UUID(),999945,193142,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (497199,UUID(),'19347023911','魏亮',NULL,1,45,'1','1','zh_CN','e15ab7d2fcff2325b550199badf7339f','18109032ec0794e2289db7b47c8ee5d2d86f723a2babcc258fe688ff1361a0f6',UTC_TIMESTAMP(),999945);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (497200,UUID(),'19347023912','左邻官方账号',NULL,1,45,'1','1','zh_CN','3c752885bf8654052f4340ce6d070965','940919ed8fe27b55a4f5aec71804cd0ca15543674053672edff3ce83b1b24e70',UTC_TIMESTAMP(),999945);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (36192,1043219,'USER',497199,'魏亮',0,'18682756100',UTC_TIMESTAMP(),999945);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (36193,1043219,'USER',497200,'左邻官方账号',0,'12000001802',UTC_TIMESTAMP(),999945);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (467635,497199,0,'18682756100',NULL,3,UTC_TIMESTAMP(),999945);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (467636,497200,0,'12000001802',NULL,3,UTC_TIMESTAMP(),999945);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2191468,1043219,'/1043219','USER',497199,'manager','魏亮',0,'18682756100',3,999945,'ENTERPRISE',0,36192);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2191469,1043219,'/1043219','USER',497200,'manager','左邻官方账号',0,'12000001802',3,999945,'ENTERPRISE',0,36193);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (34901,497199,1043219,'/1043219','ENTERPRISE',3,999945,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (34902,497200,1043219,'/1043219','ENTERPRISE',3,999945,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115992,'EhOrganizations',1043219,'EhUsers',497199,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115993,'EhOrganizations',1043219,'EhUsers',497200,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971852,240111044332060423,'C栋','C',0,18782176776,'武侯区武清思路西部智谷',1000.0,'104.120174','30.622351','wm6n1q9upguy','商业',NULL,2,0,NULL,1,NOW(),999945,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387470945,UUID(),240111044332060423,18794,'成都市',18795,'武侯区','C栋-101','C栋','101','2','0',UTC_TIMESTAMP(),999945,1000.0);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (94617,1043219,240111044332060423,239825274387470945,'C栋-101',1);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001762,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999945,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001763,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999945,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001764,'',0,1001764,1,'活动管理-默认子分类','/1/1001764',0,2,1,NOW(),0,NULL,999945,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001765,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999945,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001766,'',0,1001766,2,'活动管理二-默认子分类','/2/1001766',0,2,1,NOW(),0,NULL,999945,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001767,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999945,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001768,'',0,1001768,3,'活动管理三-默认子分类','/3/1001768',0,2,1,NOW(),0,NULL,999945,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (859,999945,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (860,999945,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (861,999945,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (862,999945,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (863,999945,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (864,999945,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (865,999945,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (866,999945,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (858,999945,1,0,'topic','话题',0,UTC_TIMESTAMP());

INSERT INTO `eh_domains` (`id`, `namespace_id`, `name`, `portal_type`, `portal_id`, `domain`, `create_uid`,`create_time`) 
	VALUES (279,999945,'智谷汇','EhOrganizations',0,'zgh.zuolin.com',0,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1043219,240111044332060423);
    
    
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1395,999945,'AssociationLayout','{"versionCode":"2018041301","layoutName":"AssociationLayout","displayName":"交流大厅","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018041301,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1396,999945,'AssociationLayout','{"versionCode":"2018041301","layoutName":"AssociationLayout","displayName":"交流大厅","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018041301,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1397,999945,'SecondServiceMarketLayout','{"versionCode":"2018041301","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup7","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":10,"separatorFlag":1,"separatorHeight":16,"columnCount":1},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup8","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018041301,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1398,999945,'SecondServiceMarketLayout','{"versionCode":"2018041301","layoutName":"SecondServiceMarketLayout","displayName":"资产管理","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup7","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":10,"separatorFlag":1,"separatorHeight":16,"columnCount":1},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup8","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018041301,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1399,999945,'ServiceMarketLayout','{"versionCode":"2018041301","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":40,"separatorFlag":1,"separatorHeight":16,"columnCount":2},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018041301,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1400,999945,'ServiceMarketLayout','{"versionCode":"2018041301","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup3","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":40,"separatorFlag":1,"separatorHeight":16,"columnCount":2},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018041301,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`,`target_type`) 
	VALUES (217500,999945,0,'/home','Default',0,0,'/home','Default','',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin','');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`,`target_type`) 
	VALUES (217501,999945,0,'/home','Default',0,0,'/home','Default','',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist','');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198650,999945,0,0,0,'/home','NavigatorGroup3','门禁','门禁','',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,1,'pm_admin',0,0,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198651,999945,0,0,0,'/home','NavigatorGroup3','门禁','门禁','',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',10,0,1,1,0,1,'park_tourist',0,0,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198652,999945,0,0,0,'/home','NavigatorGroup3','云打印','云打印','',1,1,14,'{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}',20,0,1,1,0,1,'pm_admin',0,0,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198653,999945,0,0,0,'/home','NavigatorGroup3','云打印','云打印','',1,1,14,'{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}',20,0,1,1,0,1,'park_tourist',0,0,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198654,999945,0,0,0,'/home','NavigatorGroup3','外卖','外卖','',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',30,0,1,1,0,1,'pm_admin',0,0,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198655,999945,0,0,0,'/home','NavigatorGroup3','外卖','外卖','',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',30,0,1,1,0,1,'park_tourist',0,0,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198656,999945,0,0,0,'/home','NavigatorGroup3','服务热线','服务热线','',1,1,45,'',40,0,1,1,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198657,999945,0,0,0,'/home','NavigatorGroup3','服务热线','服务热线','',1,1,45,'',40,0,1,1,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198658,999945,0,0,0,'/home','NavigatorGroup3','停车','停车','',1,1,30,'',50,0,1,1,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198659,999945,0,0,0,'/home','NavigatorGroup3','停车','停车','',1,1,30,'',50,0,1,1,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198660,999945,0,0,0,'/home','NavigatorGroup3','办事文件','办事文件','',1,1,33,'{"type":213547,"parentId":213547,"displayType": "grid"}',60,0,1,1,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198661,999945,0,0,0,'/home','NavigatorGroup3','办事文件','办事文件','',1,1,33,'{"type":213547,"parentId":213547,"displayType": "grid"}',60,0,1,1,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198662,999945,0,0,0,'/home','NavigatorGroup3','物业报修','物业报修','',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',70,0,1,1,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198663,999945,0,0,0,'/home','NavigatorGroup3','物业报修','物业报修','',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',70,0,1,1,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198664,999945,0,0,0,'/home','NavigatorGroup3','全部','全部','',1,1,53,'{"itemLocation":"/home","itemGroup":"NavigatorGroup3"}',80,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198665,999945,0,0,0,'/home','NavigatorGroup3','全部','全部','',1,1,53,'{"itemLocation":"/home","itemGroup":"NavigatorGroup3"}',80,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198666,999945,0,0,0,'/home','NavigatorGroup3','球场预订','球场预订','',1,1,49,'{"resourceTypeId":12729,"pageType":0}',90,0,1,0,0,1,'pm_admin',0,0,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198667,999945,0,0,0,'/home','NavigatorGroup3','球场预订','球场预订','',1,1,49,'{"resourceTypeId":12729,"pageType":0}',90,0,1,0,0,1,'park_tourist',0,0,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198668,999945,0,0,0,'/home','NavigatorGroup3','物业客服','物业客服','',1,1,33,'{"type":213548,"parentId":213548,"displayType": "grid"}',100,0,1,0,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198669,999945,0,0,0,'/home','NavigatorGroup3','物业客服','物业客服','',1,1,33,'{"type":213548,"parentId":213548,"displayType": "grid"}',100,0,1,0,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198670,999945,0,0,0,'/home','NavigatorGroup3','物品放行','物品放行','',1,1,33,'{"type":213549,"parentId":213549,"displayType": "grid"}',110,0,1,0,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198671,999945,0,0,0,'/home','NavigatorGroup3','物品放行','物品放行','',1,1,33,'{"type":213549,"parentId":213549,"displayType": "grid"}',110,0,1,0,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198672,999945,0,0,0,'/home','NavigatorGroup3','月卡申请','月卡申请','',1,1,33,'{"type":213550,"parentId":213550,"displayType": "grid"}',120,0,1,0,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198673,999945,0,0,0,'/home','NavigatorGroup3','月卡申请','月卡申请','',1,1,33,'{"type":213550,"parentId":213550,"displayType": "grid"}',120,0,1,0,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198674,999945,0,0,0,'/home','NavigatorGroup3','企业账单','企业账单','',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}',130,0,1,0,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198675,999945,0,0,0,'/home','NavigatorGroup3','企业账单','企业账单','',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}',130,0,1,0,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198676,999945,0,0,0,'/home','NavigatorGroup3','场地预定','场地预定','',1,1,49,'{"resourceTypeId":12730,"pageType":0}',140,0,1,0,0,1,'pm_admin',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198677,999945,0,0,0,'/home','NavigatorGroup3','场地预定','场地预定','',1,1,49,'{"resourceTypeId":12730,"pageType":0}',140,0,1,0,0,1,'park_tourist',0,0,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198678,999945,0,0,0,'/home','NavigatorGroup3','企业信息','企业信息','',1,1,33,'{"type":213551,"parentId":213551,"displayType": "grid"}',150,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198679,999945,0,0,0,'/home','NavigatorGroup3','企业信息','企业信息','',1,1,33,'{"type":213551,"parentId":213551,"displayType": "grid"}',150,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198680,999945,0,0,0,'/home','NavigatorGroup3','办公采集','办公采集','',1,1,14,NULL,160,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198681,999945,0,0,0,'/home','NavigatorGroup3','办公采集','办公采集','',1,1,14,NULL,160,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198682,999945,0,0,0,'/home','NavigatorGroup3','科技金融','科技金融','',1,1,33,'{"type":213552,"parentId":213552,"displayType": "grid"}',170,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198683,999945,0,0,0,'/home','NavigatorGroup3','科技金融','科技金融','',1,1,33,'{"type":213552,"parentId":213552,"displayType": "grid"}',170,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198684,999945,0,0,0,'/home','NavigatorGroup3','政策申报','政策申报','',1,1,33,'{"type":213553,"parentId":213553,"displayType": "grid"}',180,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198685,999945,0,0,0,'/home','NavigatorGroup3','政策申报','政策申报','',1,1,33,'{"type":213553,"parentId":213553,"displayType": "grid"}',180,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198686,999945,0,0,0,'/home','NavigatorGroup3','工商财税','工商财税','',1,1,33,'{"type":213554,"parentId":213554,"displayType": "grid"}',190,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198687,999945,0,0,0,'/home','NavigatorGroup3','工商财税','工商财税','',1,1,33,'{"type":213554,"parentId":213554,"displayType": "grid"}',190,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198688,999945,0,0,0,'/home','NavigatorGroup3','知识产权','知识产权','',1,1,33,'{"type":213555,"parentId":213555,"displayType": "grid"}',200,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198689,999945,0,0,0,'/home','NavigatorGroup3','知识产权','知识产权','',1,1,33,'{"type":213555,"parentId":213555,"displayType": "grid"}',200,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198690,999945,0,0,0,'/home','NavigatorGroup3','法务服务','法务服务','',1,1,33,'{"type":213556,"parentId":213556,"displayType": "grid"}',210,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198691,999945,0,0,0,'/home','NavigatorGroup3','法务服务','法务服务','',1,1,33,'{"type":213556,"parentId":213556,"displayType": "grid"}',210,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198692,999945,0,0,0,'/home','NavigatorGroup3','人才服务','人才服务','',1,1,33,'{"type":213557,"parentId":213557,"displayType": "grid"}',220,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198693,999945,0,0,0,'/home','NavigatorGroup3','人才服务','人才服务','',1,1,33,'{"type":213557,"parentId":213557,"displayType": "grid"}',220,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198694,999945,0,0,0,'/home','NavigatorGroup3','管理咨询','管理咨询','',1,1,33,'{"type":213558,"parentId":213558,"displayType": "grid"}',230,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198695,999945,0,0,0,'/home','NavigatorGroup3','管理咨询','管理咨询','',1,1,33,'{"type":213558,"parentId":213558,"displayType": "grid"}',230,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198696,999945,0,0,0,'/home','NavigatorGroup3','信用体系','信用体系','',1,1,33,'{"type":213559,"parentId":213559,"displayType": "grid"}',240,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198697,999945,0,0,0,'/home','NavigatorGroup3','信用体系','信用体系','',1,1,33,'{"type":213559,"parentId":213559,"displayType": "grid"}',240,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198698,999945,0,0,0,'/home','NavigatorGroup3','检验检测','检验检测','',1,1,33,'{"type":213560,"parentId":213560,"displayType": "grid"}',250,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198699,999945,0,0,0,'/home','NavigatorGroup3','检验检测','检验检测','',1,1,33,'{"type":213560,"parentId":213560,"displayType": "grid"}',250,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198700,999945,0,0,0,'/home','NavigatorGroup3','宣传会务','宣传会务','',1,1,33,'{"type":213561,"parentId":213561,"displayType": "grid"}',260,0,1,0,0,1,'pm_admin',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198701,999945,0,0,0,'/home','NavigatorGroup3','宣传会务','宣传会务','',1,1,33,'{"type":213561,"parentId":213561,"displayType": "grid"}',260,0,1,0,0,1,'park_tourist',0,0,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198702,999945,0,0,0,'/home','NavigatorGroup4','园区介绍1','园区介绍1','',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',270,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198703,999945,0,0,0,'/home','NavigatorGroup4','园区介绍1','园区介绍1','',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',270,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198704,999945,0,0,0,'/home','NavigatorGroup4','企业服务','企业服务','',1,1,2,'{"itemLocation":"/secondhome","layoutName":"SecondServiceMarketLayout","title":"企业服务","entityTag":"PM"}',280,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198705,999945,0,0,0,'/home','NavigatorGroup4','企业服务','企业服务','',1,1,2,'{"itemLocation":"/secondhome","layoutName":"SecondServiceMarketLayout","title":"企业服务","entityTag":"PM"}',280,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198706,999945,0,0,0,'/home','NavigatorGroup4','园区介绍2','园区介绍2','',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',290,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198707,999945,0,0,0,'/home','NavigatorGroup4','园区介绍2','园区介绍2','',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',290,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198708,999945,0,0,0,'/home','NavigatorGroup4','社群互动','社群互动','',1,1,60,'{"url":"zl://association/main?layoutName=AssociationLayout&itemLocation=/association&versionCode=2018041301&displayName=社群互动"}',300,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198709,999945,0,0,0,'/home','NavigatorGroup4','社群互动','社群互动','',1,1,60,'{"url":"zl://association/main?layoutName=AssociationLayout&itemLocation=/association&versionCode=2018041301&displayName=社群互动"}',300,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198710,999945,0,0,0,'/home','NavigatorGroup5','园区快讯','园区快讯','',1,1,0,'',310,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198711,999945,0,0,0,'/home','NavigatorGroup5','园区快讯','园区快讯','',1,1,0,'',310,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198712,999945,0,0,0,'/secondhome','NavigatorGroup7','企业服务','企业服务','',1,1,0,'',320,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198713,999945,0,0,0,'/secondhome','NavigatorGroup7','企业服务','企业服务','',1,1,0,'',320,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198714,999945,0,0,0,'/secondhome','NavigatorGroup7','园区招商','园区招商','',1,1,13,'{"url":"${home.url}/customer/build/index.html?hideNavigationBar=1#/home#sign_suffix"}',330,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198715,999945,0,0,0,'/secondhome','NavigatorGroup7','园区招商','园区招商','',1,1,13,'{"url":"${home.url}/customer/build/index.html?hideNavigationBar=1#/home#sign_suffix"}',330,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198716,999945,0,0,0,'/secondhome','NavigatorGroup8','物业客服','物业客服','',1,1,33,'{"type":213548,"parentId":213548,"displayType": "grid"}',340,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198717,999945,0,0,0,'/secondhome','NavigatorGroup8','物业客服','物业客服','',1,1,33,'{"type":213548,"parentId":213548,"displayType": "grid"}',340,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198718,999945,0,0,0,'/secondhome','NavigatorGroup8','物业报修','物业报修','',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',350,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198719,999945,0,0,0,'/secondhome','NavigatorGroup8','物业报修','物业报修','',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',350,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198720,999945,0,0,0,'/secondhome','NavigatorGroup8','物品放行','物品放行','',1,1,33,'{"type":213549,"parentId":213549,"displayType": "grid"}',360,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198721,999945,0,0,0,'/secondhome','NavigatorGroup8','物品放行','物品放行','',1,1,33,'{"type":213549,"parentId":213549,"displayType": "grid"}',360,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198722,999945,0,0,0,'/secondhome','NavigatorGroup8','建议投诉','建议投诉','',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=建议投诉"}',370,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198723,999945,0,0,0,'/secondhome','NavigatorGroup8','建议投诉','建议投诉','',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=建议投诉"}',370,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198724,999945,0,0,0,'/secondhome','NavigatorGroup8','企业账单','企业账单','',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}',380,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198725,999945,0,0,0,'/secondhome','NavigatorGroup8','企业账单','企业账单','',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}',380,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198726,999945,0,0,0,'/secondhome','NavigatorGroup8','服务热线','服务热线','',1,1,45,'',390,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198727,999945,0,0,0,'/secondhome','NavigatorGroup8','服务热线','服务热线','',1,1,45,'',390,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198728,999945,0,0,0,'/secondhome','NavigatorGroup8','办事文件','办事文件','',1,1,33,'{"type":213547,"parentId":213547,"displayType": "grid"}',400,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198729,999945,0,0,0,'/secondhome','NavigatorGroup8','办事文件','办事文件','',1,1,33,'{"type":213547,"parentId":213547,"displayType": "grid"}',400,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198730,999945,0,0,0,'/secondhome','NavigatorGroup8','品质核查','品质核查','',1,1,44,'{"realm":"quality","entryUrl":"http://core.zuolin.com/nar/quality/index.html?hideNavigationBar=1#/select_community#sign_suffix"}',410,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198731,999945,0,0,0,'/secondhome','NavigatorGroup8','品质核查','品质核查','',1,1,44,'{"realm":"quality","entryUrl":"http://core.zuolin.com/nar/quality/index.html?hideNavigationBar=1#/select_community#sign_suffix"}',410,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198732,999945,0,0,0,'/secondhome','NavigatorGroup8','场地预定','场地预定','',1,1,49,'{"resourceTypeId":12730,"pageType":0}',420,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198733,999945,0,0,0,'/secondhome','NavigatorGroup8','场地预定','场地预定','',1,1,49,'{"resourceTypeId":12730,"pageType":0}',420,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198734,999945,0,0,0,'/secondhome','NavigatorGroup8','月卡申请','月卡申请','',1,1,33,'{"type":213550,"parentId":213550,"displayType": "grid"}',430,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198735,999945,0,0,0,'/secondhome','NavigatorGroup8','月卡申请','月卡申请','',1,1,33,'{"type":213550,"parentId":213550,"displayType": "grid"}',430,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198736,999945,0,0,0,'/secondhome','NavigatorGroup8','问卷调查','问卷调查','',1,1,13,'{"url":"${home.url}/questionnaire-survey/build/index.html#/home#sign_suffix"}"}',440,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198737,999945,0,0,0,'/secondhome','NavigatorGroup8','问卷调查','问卷调查','',1,1,13,'{"url":"${home.url}/questionnaire-survey/build/index.html#/home#sign_suffix"}"}',440,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198738,999945,0,0,0,'/association','TabGroup','活动','活动','',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',450,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198739,999945,0,0,0,'/association','TabGroup','活动','活动','',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',450,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198740,999945,0,0,0,'/association','TabGroup','俱乐部','俱乐部','',1,1,36,'{"privateFlag": 0}',460,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198741,999945,0,0,0,'/association','TabGroup','俱乐部','俱乐部','',1,1,36,'{"privateFlag": 0}',460,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198742,999945,0,0,0,'/home','Default','园区快讯','园区快讯','',1,1,48,'{"categoryId":0,"timeWidgetStyle":"date"}',10,0,1,1,0,0,'park_tourist',0,1,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (198743,999945,0,0,0,'/home','Default','园区快讯','园区快讯','',1,1,48,'{"categoryId":0,"timeWidgetStyle":"date"}',10,0,1,1,0,0,'park_tourist',0,1,'');

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213547,'community','240111044331055940',0,'办事文件','办事文件',0,2,1,UTC_TIMESTAMP(),999945,1);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213548,'community','240111044331055940',0,'物业客服','物业客服',0,2,1,UTC_TIMESTAMP(),999945,2);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213549,'community','240111044331055940',0,'物品放行','物品放行',0,2,1,UTC_TIMESTAMP(),999945,3);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213550,'community','240111044331055940',0,'月卡申请','月卡申请',0,2,1,UTC_TIMESTAMP(),999945,4);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213551,'community','240111044331055940',0,'企业信息','企业信息',0,2,1,UTC_TIMESTAMP(),999945,5);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213552,'community','240111044331055940',0,'科技金融','科技金融',0,2,1,UTC_TIMESTAMP(),999945,6);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213553,'community','240111044331055940',0,'政策申报','政策申报',0,2,1,UTC_TIMESTAMP(),999945,7);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213554,'community','240111044331055940',0,'工商财税','工商财税',0,2,1,UTC_TIMESTAMP(),999945,8);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213555,'community','240111044331055940',0,'知识产权','知识产权',0,2,1,UTC_TIMESTAMP(),999945,9);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213556,'community','240111044331055940',0,'法务服务','法务服务',0,2,1,UTC_TIMESTAMP(),999945,10);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213557,'community','240111044331055940',0,'人才服务','人才服务',0,2,1,UTC_TIMESTAMP(),999945,11);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213558,'community','240111044331055940',0,'管理咨询','管理咨询',0,2,1,UTC_TIMESTAMP(),999945,12);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213559,'community','240111044331055940',0,'信用体系','信用体系',0,2,1,UTC_TIMESTAMP(),999945,13);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213560,'community','240111044331055940',0,'检验检测','检验检测',0,2,1,UTC_TIMESTAMP(),999945,14);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `namespace_id`,`entry_id`) 
	VALUES (213561,'community','240111044331055940',0,'宣传会务','宣传会务',0,2,1,UTC_TIMESTAMP(),999945,15);

INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212381,0,'organaization',1043219,'all','办事文件','办事文件',213547,2,212381,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212382,0,'organaization',1043219,'all','物业客服','物业客服',213548,2,212382,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212383,0,'organaization',1043219,'all','物品放行','物品放行',213549,2,212383,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212384,0,'organaization',1043219,'all','月卡申请','月卡申请',213550,2,212384,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212385,0,'organaization',1043219,'all','企业信息','企业信息',213551,2,212385,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212386,0,'organaization',1043219,'all','科技金融','科技金融',213552,2,212386,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212387,0,'organaization',1043219,'all','政策申报','政策申报',213553,2,212387,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212388,0,'organaization',1043219,'all','工商财税','工商财税',213554,2,212388,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212389,0,'organaization',1043219,'all','知识产权','知识产权',213555,2,212389,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212390,0,'organaization',1043219,'all','法务服务','法务服务',213556,2,212390,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212391,0,'organaization',1043219,'all','人才服务','人才服务',213557,2,212391,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212392,0,'organaization',1043219,'all','管理咨询','管理咨询',213558,2,212392,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212393,0,'organaization',1043219,'all','信用体系','信用体系',213559,2,212393,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212394,0,'organaization',1043219,'all','检验检测','检验检测',213560,2,212394,UTC_TIMESTAMP(),2,2,1,0);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `status`, `default_order`,`create_time`, `support_type`, `description_height`, `display_flag`, `enable_comment`) 
	VALUES (212395,0,'organaization',1043219,'all','宣传会务','宣传会务',213561,2,212395,UTC_TIMESTAMP(),2,2,1,0);

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (208551,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999945);
INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (208552,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999945);
INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (208553,9,0,'投诉建议','任务/投诉建议',1,2,UTC_TIMESTAMP(),999945);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12729,'球场预订',0,2,999945,0,0);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`) 
	VALUES (12730,'场地预定',0,2,999945,0,0);

INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES (3549,'moreGroup1','',10,0,1,999945,'pm_admin','配套服务','/home','NavigatorGroup3',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES (3550,'moreGroup1','',10,0,1,999945,'park_tourist','配套服务','/home','NavigatorGroup3',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES (3551,'moreGroup2','',20,0,1,999945,'pm_admin','物业服务','/home','NavigatorGroup3',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES (3552,'moreGroup2','',20,0,1,999945,'park_tourist','物业服务','/home','NavigatorGroup3',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES (3553,'moreGroup3','',30,0,1,999945,'pm_admin','企业服务','/home','NavigatorGroup3',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES (3554,'moreGroup3','',30,0,1,999945,'park_tourist','企业服务','/home','NavigatorGroup3',0,0,1,0);


