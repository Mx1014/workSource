INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`)
	VALUES (126996,'EhOrganizations',1035992,1,10,385649,0,1,UTC_TIMESTAMP(),999964,'EhUsers','admin');

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES (114855,'EhOrganizations',1035992,'EhUsers',385649,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
	VALUES (239825274387459023,UUID(),240111044332059913,79,'成都市',80,'武侯区','创业场-1','创业场','1','2','0',UTC_TIMESTAMP(),999964,35000.0);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order)
	VALUES (1970686,240111044332059913,'创业场','创业场',0,18981182106,'四川省成都市天府大道南段天府软件园D区',35000.0,'104.081476','30.545423','wm6jb2tgfpt9','',NULL,2,0,NULL,1,NOW(),999964,1);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES (240111044332059913,UUID(),79,'成都市',80,'武侯区','创业场','创业场','四川省成都市天府软件园D区','创业场现有孵化场地3.5万平方米，累计孵化项目1000余个，现孵化团队250家。目前，已建成“创业苗圃-孵化器-加速器-产业园”的多层级孵化模式，提供包括孵化工位、联合创业空间、独立空间等多形式众创空间，形成覆盖资金、人才、圈子、市场、创业辅导等方面的全方位“5C”培育计划。 截止目前，创业场已成功孵化Tap4fun、Camera360、TestBird、咕咚、狮之吼（LionMobi）、极米、物流QQ(货车帮)、医联(Medlinker)、麦子学院、鲁大师等众多国内外领先的企业和手机应用，以及银河帝国、王者帝国、斯巴达战争、三剑豪、帝国塔防3、花千骨、忍者萌剑传、战地风暴等月流水过千万的手机游戏产品。',0,1,2,UTC_TIMESTAMP(),1,191739,191740,UTC_TIMESTAMP(),999964);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)
	VALUES (240111044331092720,240111044332059913,'',104.081476,30.545423,'wm6jb2tgfpt9');

INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` )
	VALUES (1839,'app.agreements.url','http://alpha.lab.everhomes.com/mobile/static/app_agreements/agreements.html?ns=999964','the relative path for chuangyechang app agreements',999964,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` )
	VALUES (1840,'business.url','http://biz-alpha.lab.everhomes.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz-alpha.lab.everhomes.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for changfazhan',999964,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` )
	VALUES (1841,'pmtask.handler-999964','flow','0',999964,'物业报修工作流');

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES (191738,UUID(),999964,2,'EhGroups',1042143,'成都天府软件园有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES (191739,UUID(),999964,2,'',0,'创业场社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES (191740,UUID(),999964,2,'',0,'创业场意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES (1042143,UUID(),'成都天府软件园有限公司','成都天府软件园有限公司',1,1,1035992,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),191738,1,999964);

INSERT INTO `eh_namespaces` (`id`, `name`)
	VALUES (999964,'创业场');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`)
	VALUES (1365,999964,'community_commercial',UTC_TIMESTAMP());

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`)
	VALUES (19995,999964,'COMMUNITY',240111044332059913,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES (1035992,0,'PM','成都天府软件园有限公司','创业场现有孵化场地3.5万平方米，累计孵化项目1000余个，现孵化团队250家。目前，已建成“创业苗圃-孵化器-加速器-产业园”的多层级孵化模式，提供包括孵化工位、联合创业空间、独立空间等多形式众创空间，形成覆盖资金、人才、圈子、市场、创业辅导等方面的全方位“5C”培育计划。 截止目前，创业场已成功孵化Tap4fun、Camera360、TestBird、咕咚、狮之吼（LionMobi）、极米、物流QQ(货车帮)、医联(Medlinker)、麦子学院、鲁大师等众多国内外领先的企业和手机应用，以及银河帝国、王者帝国、斯巴达战争、三剑豪、帝国塔防3、花千骨、忍者萌剑传、战地风暴等月流水过千万的手机游戏产品。','/1035992',1,2,'ENTERPRISE',999964,1042143);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (41024,1035992,240111044332059913,239825274387459023,'创业场-1',2);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES (1152697,240111044332059913,'organization',1035992,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_organization_members` (id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES (2167094,1035992,'USER',385649,'manager','李杨',0,'18981182106',3,999964);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (78,0,'四川','SICHUAN','SC','/四川',1,1,NULL,NULL,2,0,999964);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (79,78,'成都市','CHENGDUSHI','CDS','/四川/成都市',2,2,NULL,'028',2,1,999964);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (80,79,'武侯区','WUHOUQU','WHQ','/四川/成都市/武侯区',3,3,NULL,'028',2,0,999964);

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (385649,UUID(),'19161795890','李杨',NULL,1,45,'1','1','zh_CN','b962a4ddd17d6423aaa6d3722a0048c9','a697fceb78d79d6d362f956a3b8d04b162e51122cc9859a1ba8e7dadbfb1d094',UTC_TIMESTAMP(),999964);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (323299,385649,0,'18981182106',NULL,3,UTC_TIMESTAMP(),999964);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` )
	VALUES (360,'Android_ChuangYeChang',NULL,UTC_TIMESTAMP(),999964);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` )
	VALUES (361,'iOS_ChuangYeChang',NULL,UTC_TIMESTAMP(),999964);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
	VALUES (648,360,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
	VALUES (649,361,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3723,10000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3724,10100,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3725,10400,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3726,10200,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3727,10600,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3728,10750,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3729,10800,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3730,11000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3731,12200,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3732,40500,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3733,40510,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3734,40520,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3735,40530,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3736,40541,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3737,40542,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3738,41300,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3739,40750,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3740,40751,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3741,40700,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3742,30000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3743,30500,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3744,31000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3745,32000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3746,33000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3747,34000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3748,35000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3749,30600,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3750,50000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3751,50100,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3752,50110,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3753,50200,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3754,50210,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3755,50220,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3756,50300,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3757,50400,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3758,50500,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3759,60000,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3760,60100,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES (3761,60200,NULL,'EhNamespaces',999964,2);
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`)
	VALUES (1035992,240111044332059913);


set @community_id = 240111044332059913;
-- 园区管理员
-- banner
set @eh_banners_id = (select max(id) from eh_banners);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES ((@eh_banners_id := @eh_banners_id + 1), 999964, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRveU5UVTBZelkxWWpVNFpESXhaRFJoWW1ReU5tRm1PREF5TkdGaFlUYzFaQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES ((@eh_banners_id := @eh_banners_id + 1), 999964, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRveU5UVTBZelkxWWpVNFpESXhaRFJoWW1ReU5tRm1PREF5TkdGaFlUYzFaQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES ((@eh_banners_id := @eh_banners_id + 1), 999964, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRveU5UVTBZelkxWWpVNFpESXhaRFJoWW1ReU5tRm1PREF5TkdGaFlUYzFaQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`)
    VALUES ((@eh_banners_id := @eh_banners_id + 1), 999964, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRveU5UVTBZelkxWWpVNFpESXhaRFJoWW1ReU5tRm1PREF5TkdGaFlUYzFaQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
	-- layout
set @layouts_id = (select max(id) from eh_launch_pad_layouts);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`)
    VALUES ((@layouts_id := @layouts_id + 1), 999964, 'ServiceMarketLayout', '{"versionCode":"2017091301","versionName": "4.7.4","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 0,"separatorHeight": 16}, {"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Metro","defaultOrder": 3,"separatorFlag": 1,"separatorHeight":16,"columnCount":2}, {"groupName":"新闻快讯","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"date","categoryId":0,"itemGroup":"Default","newsSize":2},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2017091301', '2017091301', '2', NOW(), 'pm_admin');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`)
    VALUES ((@layouts_id := @layouts_id + 1), 999964, 'ServiceMarketLayout', '{"versionCode":"2017091301","versionName": "4.7.4","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 0,"separatorHeight": 16}, {"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Metro","defaultOrder": 3,"separatorFlag": 1,"separatorHeight":16,"columnCount":2}, {"groupName":"新闻快讯","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"date","categoryId":0,"itemGroup":"Default","newsSize":2},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', '2017091301', '2017091301', '2', NOW(), 'park_tourist');

set @pad_items_id := (select max(id) from eh_launch_pad_items);
-- 入园申请
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, '0', '0', '0', '/home', 'Bizs', 'ENTER_PARK', '入园申请', 'cs://1/image/aW1hZ2UvTVRvMk1EVmhNbVZoWW1Oa05URXpOamczWTJKbE9URTFObVUxT0dRMFpURmlPUQ', '1', '1', '28', '', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, '0', '0', '0', '/home', 'Bizs', 'ENTER_PARK', '入园申请', 'cs://1/image/aW1hZ2UvTVRvMk1EVmhNbVZoWW1Oa05URXpOamczWTJKbE9URTFObVUxT0dRMFpURmlPUQ', '1', '1', '28', '', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

-- 活动
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, '0', '0', '0', '/home', 'Bizs', 'ACTIVITY', '活动', 'cs://1/image/aW1hZ2UvTVRvek1EQTJOR0l4TURZellUTmhObU5sWmprM09EazFPVGt3TVdFM1pqUm1OZw', '1', '1', '61', '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, '0', '0', '0', '/home', 'Bizs', 'ACTIVITY', '活动', 'cs://1/image/aW1hZ2UvTVRvek1EQTJOR0l4TURZellUTmhObU5sWmprM09EazFPVGt3TVdFM1pqUm1OZw', '1', '1', '61', '{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

-- 增加活动入口
SET @activity_categories_id = (SELECT MAX(id) from eh_activity_categories);
INSERT INTO `eh_activity_categories`(`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
	VALUES((@activity_categories_id := @activity_categories_id + 1),'','0','1','-1','活动管理','/1','0','2','1',NOW(),'0',NULL,999964,'0','1','cs://1/image/aW1hZ2UvTVRvek1EQTJOR0l4TURZellUTmhObU5sWmprM09EazFPVGt3TVdFM1pqUm1OZw',NULL,NULL,'0');
-- 增加活动默认子分类
SET @activity_categories_id = @activity_categories_id + 1;
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES (@activity_categories_id, '', '0', @activity_categories_id, '1', 'all', CONCAT('/1/',@activity_categories_id), '0', '2', '1', NOW(), '0', NULL, 999964, '0', '1', 'cs://1/image/aW1hZ2UvTVRvek1EQTJOR0l4TURZellUTmhObU5sWmprM09EazFPVGt3TVdFM1pqUm1OZw', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');


SET @categories_id = (SELECT MAX(id) from eh_service_alliances);
SET @alliances_id = (SELECT MAX(id) from eh_service_alliances);
SET @skip_rule_id = (SELECT MAX(id) from eh_service_alliance_skip_rule);
-- 投融资对接
set @categories_id = @categories_id + 1;
set @alliances_id = @alliances_id + 1;
set @skip_rule_id = @skip_rule_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@categories_id, 'community', @community_id, '0', '投融资对接', '投融资对接', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999964, '');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
    VALUES (@alliances_id, 0, 'community', @community_id, '投融资对接', '投融资对接首页', @categories_id, '', '', '', 'cs://1/image/aW1hZ2UvTVRvNE16azVOVEE1WTJVMVltTXpZV00zWlRneFpUQTFNelJrTVdKak1qWmhPUQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '投融资对接', '投融资对接', 'cs://1/image/aW1hZ2UvTVRvNE16azVOVEE1WTJVMVltTXpZV00zWlRneFpUQTFNelJrTVdKak1qWmhPUQ', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 30, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, 30, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '投融资对接', '投融资对接', 'cs://1/image/aW1hZ2UvTVRvNE16azVOVEE1WTJVMVltTXpZV00zWlRneFpUQTFNelJrTVdKak1qWmhPUQ', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 30, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, 30, NULL);

-- 企业综合服务
set @categories_id = @categories_id + 1;
set @alliances_id = @alliances_id + 1;
set @skip_rule_id = @skip_rule_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@categories_id, 'community', @community_id, '0', '企业综合服务', '企业综合服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999964, '');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
    VALUES (@alliances_id, 0, 'community', @community_id, '企业综合服务', '企业综合服务首页', @categories_id, '', '', '', 'cs://1/image/aW1hZ2UvTVRwaU9HRTJORFJsTWpobU5qVmhaalF6WlRZek5UVTROak16WWpZeE1UWXhOQQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '企业综合服务', '企业综合服务', 'cs://1/image/aW1hZ2UvTVRwaU9HRTJORFJsTWpobU5qVmhaalF6WlRZek5UVTROak16WWpZeE1UWXhOQQ', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 40, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, 30, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '企业综合服务', '企业综合服务', 'cs://1/image/aW1hZ2UvTVRwaU9HRTJORFJsTWpobU5qVmhaalF6WlRZek5UVTROak16WWpZeE1UWXhOQQ', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 40, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, 30, NULL);

-- 孵化器项目信息
set @categories_id = @categories_id + 1;
set @alliances_id = @alliances_id + 1;
set @skip_rule_id = @skip_rule_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@categories_id, 'community', @community_id, '0', '孵化器项目信息', '孵化器项目信息', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999964, '');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
    VALUES (@alliances_id, 0, 'community', @community_id, '孵化器项目信息', '孵化器项目信息首页', @categories_id, '', '', '', 'cs://1/image/aW1hZ2UvTVRvMk9HVXdNalpoTTJFd05qWmhZelF5T1dVMlpXUTFOV0UxTkdGaVkyTXhaZw', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '孵化器项目信息', '孵化器项目信息', 'cs://1/image/aW1hZ2UvTVRvMk9HVXdNalpoTTJFd05qWmhZelF5T1dVMlpXUTFOV0UxTkdGaVkyTXhaZw', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 50, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, 30, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '孵化器项目信息', '孵化器项目信息', 'cs://1/image/aW1hZ2UvTVRvMk9HVXdNalpoTTJFd05qWmhZelF5T1dVMlpXUTFOV0UxTkdGaVkyTXhaZw', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 50, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, 30, NULL);

-- 更多
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRveE1qbGtOR1l6WVRobFpEZGlaVEprTWpsaU1ERTFNR0l5TkRabU9ETTBNQQ', 1, 1, 1, '{"itemLocation":"/home","itemGroup":"Bizs"}', 40, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, 60, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '更多', '更多', 'cs://1/image/aW1hZ2UvTVRveE1qbGtOR1l6WVRobFpEZGlaVEprTWpsaU1ERTFNR0l5TkRabU9ETTBNQQ', 1, 1, 1, '{"itemLocation":"/home","itemGroup":"Bizs"}', 40, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, 60, NULL);


-- 产品信息发布
set @categories_id = @categories_id + 1;
set @alliances_id = @alliances_id + 1;
set @skip_rule_id = @skip_rule_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@categories_id, 'community', @community_id, '0', '产品信息发布', '产品信息发布', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999964, '');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
    VALUES (@alliances_id, 0, 'community', @community_id, '产品信息发布', '产品信息发布首页', @categories_id, '', '', '', 'cs://1/image/aW1hZ2UvTVRwa01XTXpaRFkzTjJGa1pEQTRNelF5WlRJd05ESTJNVGxtWkRrME1HTmpaUQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '产品信息发布', '产品信息发布', 'cs://1/image/aW1hZ2UvTVRwa01XTXpaRFkzTjJGa1pEQTRNelF5WlRJd05ESTJNVGxtWkRrME1HTmpaUQ', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 70, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, 30, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '产品信息发布', '产品信息发布', 'cs://1/image/aW1hZ2UvTVRwa01XTXpaRFkzTjJGa1pEQTRNelF5WlRJd05ESTJNVGxtWkRrME1HTmpaUQ', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 70, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, 30, NULL);

-- 机构信息发布
set @categories_id = @categories_id + 1;
set @alliances_id = @alliances_id + 1;
set @skip_rule_id = @skip_rule_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@categories_id, 'community', @community_id, '0', '机构信息发布', '机构信息发布', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999964, '');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
    VALUES (@alliances_id, 0, 'community', @community_id, '机构信息发布', '机构信息发布首页', @categories_id, '', '', '', 'cs://1/image/aW1hZ2UvTVRveU16STJOekprTURGalpUWmlZVFJtWWpsaVpqaG1OV05pTmpBek1tSXhNUQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '机构信息发布', '机构信息发布', 'cs://1/image/aW1hZ2UvTVRveU16STJOekprTURGalpUWmlZVFJtWWpsaVpqaG1OV05pTmpBek1tSXhNUQ', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 80, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, 30, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@pad_items_id := @pad_items_id + 1), 999964, 0, 0, 0, '/home', 'Bizs', '机构信息发布', '机构信息发布', 'cs://1/image/aW1hZ2UvTVRveU16STJOekprTURGalpUWmlZVFJtWWpsaVpqaG1OV05pTmpBek1tSXhNUQ', 1, 1, 33, CONCAT('{"type":',@categories_id,',"parentId":',@categories_id,',"displayType": "list"}'), 80, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, 30, NULL);



