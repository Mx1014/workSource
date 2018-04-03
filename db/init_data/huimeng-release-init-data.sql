INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2064,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999949','the relative path for 慧盟物业助手 app agreements',999949,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2065,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for 慧盟物业助手',999949,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2066,'pmtask.handler-999949','flow','0',999949,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2067,'pay.platform','1','支付类型',999949,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (603,'Android_HuiMengWuYe',NULL,UTC_TIMESTAMP(),999949);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (604,'iOS_HuiMengWuYe',NULL,UTC_TIMESTAMP(),999949);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (963,603,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999949);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (964,604,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999949);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999949,'慧盟物业助手');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1598,999949,'community_residential',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (1030,'sms.default.sign',0,'zh_CN','慧盟物业助手','【慧盟物业助手】',999949);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18557,0,'江苏','JIANGSU','JS','/江苏',1,1,NULL,NULL,2,0,999949);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18558,18557,'苏州市','SUZHOUSHI','SZS','/江苏/苏州市',2,2,NULL,'0512',2,0,999949);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18559,18558,'相城区','XIANGCHENGQU','XCQ','/江苏/苏州市/相城区',3,3,NULL,'0512',2,0,999949);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060193,UUID(),18558,'苏州市',18559,'相城区','丽岛别墅','','黄埭镇春秋路','春申湖-丽岛别墅位于相城区黄埭镇南侧，择2000亩春申湖西北而居，右与17.87公里西塘河一脉相承，真正稀缺半岛生态资源。',0,1,2,UTC_TIMESTAMP(),0,192865,192866,UTC_TIMESTAMP(),999949);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092966,240111044332060193,'',120.554734,31.430672,'wttdz3c2bvuv');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20303,999949,'COMMUNITY',240111044332060193,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1041508,0,'PM','苏州安邦物业服务股份有限公司','苏州安邦物业服务股份有限公司成立于2005年，是一家专业从事物业管理及其相关配套业务的国资控股企业。公司于2010年获批国家一级物业管理资质，历任中国物业管理协会常务理事单位、苏州物业管理协会副会长单位等。   
    安邦物业先后在重庆、上海、佛山、常州、徐州等地设立了分公司，积极推动跨区域规模化发展；关联设立的云翼方信息科技、安捷电梯等，更实现了企业现代化专业细分，强化专业技术支持和保障。2015年6月，公司获得天使投资龙翌资本、北京金慧丰投资及华九资本的入股投资，搭建企业资本资源平台，加速企业股权多元化；同年年底，公司启动上市进程，正式步入产业资本化道路，目标成为全国一流的上市物管集团；2017年9月，公司获得佛山建设投资、佛山铁路投资的增资入股，整合资本、人才、项目、机制、政策等发展要素，增强企业资本实力的同时，进一步优化了企业基因，重构产业价值链，加速产业资本化及上市进程。','/1041508',1,2,'ENTERPRISE',999949,1059396);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1156451,240111044332060193,'organization',1041508,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37655,'EhOrganizations',1041508,1,10,483886,0,1,UTC_TIMESTAMP(),999949,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37653,'EhOrganizations',1041508,1,10,483884,0,1,UTC_TIMESTAMP(),999949,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37654,'EhOrganizations',1041508,1,10,483885,0,1,UTC_TIMESTAMP(),999949,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1059396,UUID(),'苏州安邦物业服务股份有限公司','苏州安邦物业服务股份有限公司',1,1,1041508,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192864,1,999949);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192864,UUID(),999949,2,'EhGroups',1059396,'苏州安邦物业服务股份有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192865,UUID(),999949,2,'',0,'慧盟物业管家社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192866,UUID(),999949,2,'',0,'慧盟物业管家意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256642,UUID(),999949,192865,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (483884,UUID(),'19265010682','曹黎明',NULL,1,45,'1','1','zh_CN','8baec7c249883b0784950aa944c048ef','fdb09ea7a6d50d0c42eefdf649747550cc7cdeed8775636ec999782d7065f396',UTC_TIMESTAMP(),999949);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (483885,UUID(),'19265010683','杨传琦',NULL,1,45,'1','1','zh_CN','309db1fa102efa1fb9129a72b592a6ed','173ac1083c5611f773fbd7b5ce3c871962f708778694e945d27e2612b3964f74',UTC_TIMESTAMP(),999949);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (483886,UUID(),'19265010684','左邻官方账号',NULL,1,45,'1','1','zh_CN','d86e34ac04041b06ab09643af7e722f6','b26545c290359ca5e8adbb86038bd9c899d6fb8d1005e2cc2ad5d39e68236746',UTC_TIMESTAMP(),999949);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32021,1041508,'USER',483886,'左邻官方账号',0,'12000001802',UTC_TIMESTAMP(),999949);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32019,1041508,'USER',483884,'曹黎明',0,'18112581326',UTC_TIMESTAMP(),999949);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32020,1041508,'USER',483885,'杨传琦',0,'15150404924',UTC_TIMESTAMP(),999949);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (454895,483884,0,'18112581326',NULL,3,UTC_TIMESTAMP(),999949);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (454896,483885,0,'15150404924',NULL,3,UTC_TIMESTAMP(),999949);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (454897,483886,0,'12000001802',NULL,3,UTC_TIMESTAMP(),999949);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184924,1041508,'/1041508','USER',483884,'manager','曹黎明',0,'18112581326',3,999949,'ENTERPRISE',0,32019);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184925,1041508,'/1041508','USER',483885,'manager','杨传琦',0,'15150404924',3,999949,'ENTERPRISE',0,32020);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184926,1041508,'/1041508','USER',483886,'manager','左邻官方账号',0,'12000001802',3,999949,'ENTERPRISE',0,32021);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31659,483886,1041508,'/1041508','ENTERPRISE',3,999949,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31657,483884,1041508,'/1041508','ENTERPRISE',3,999949,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31658,483885,1041508,'/1041508','ENTERPRISE',3,999949,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115545,'EhOrganizations',1041508,'EhUsers',483884,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115546,'EhOrganizations',1041508,'EhUsers',483885,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115547,'EhOrganizations',1041508,'EhUsers',483886,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971465,240111044332060193,'142幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971466,240111044332060193,'143幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971467,240111044332060193,'144幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971468,240111044332060193,'145幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971469,240111044332060193,'146幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971470,240111044332060193,'147幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971471,240111044332060193,'148幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971472,240111044332060193,'149幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971473,240111044332060193,'150幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971474,240111044332060193,'151幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971475,240111044332060193,'152幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971476,240111044332060193,'153幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971477,240111044332060193,'154幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971478,240111044332060193,'155幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971479,240111044332060193,'156幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971480,240111044332060193,'157幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971481,240111044332060193,'158幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971482,240111044332060193,'159幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971483,240111044332060193,'160幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971484,240111044332060193,'161幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971485,240111044332060193,'162幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971486,240111044332060193,'163幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971487,240111044332060193,'164幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971488,240111044332060193,'165幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971489,240111044332060193,'166幢','',0,15006131761,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971490,240111044332060193,'167幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971491,240111044332060193,'168幢','',0,15006131761,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971492,240111044332060193,'169幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971493,240111044332060193,'170幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971494,240111044332060193,'171幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971495,240111044332060193,'172幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971496,240111044332060193,'173幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971497,240111044332060193,'174幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971498,240111044332060193,'175幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971499,240111044332060193,'176幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971500,240111044332060193,'177幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971501,240111044332060193,'178幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971502,240111044332060193,'179幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971503,240111044332060193,'180幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971504,240111044332060193,'181幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971505,240111044332060193,'182幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971506,240111044332060193,'183幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971507,240111044332060193,'184幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971508,240111044332060193,'185幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971509,240111044332060193,'186幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971510,240111044332060193,'187幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971511,240111044332060193,'188幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971512,240111044332060193,'189幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971513,240111044332060193,'190幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971514,240111044332060193,'191幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971515,240111044332060193,'192幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971516,240111044332060193,'193幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971517,240111044332060193,'194幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971518,240111044332060193,'195幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971519,240111044332060193,'196幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971520,240111044332060193,'197幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971521,240111044332060193,'198幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971522,240111044332060193,'199幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971523,240111044332060193,'200幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971524,240111044332060193,'201幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971525,240111044332060193,'202幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971526,240111044332060193,'203幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971527,240111044332060193,'204幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971528,240111044332060193,'205幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971529,240111044332060193,'206幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971530,240111044332060193,'207幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971531,240111044332060193,'208幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971532,240111044332060193,'209幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971533,240111044332060193,'210幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971534,240111044332060193,'211幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971535,240111044332060193,'212幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971536,240111044332060193,'213幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971537,240111044332060193,'214幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971538,240111044332060193,'215幢','',0,15006131760,'黄埭镇春秋路',NULL,'120.556013','31.433009','wttdz64nw9wd','',NULL,2,0,NULL,1,NOW(),999949,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466675,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','145幢-102','145幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466707,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','152幢-101','152幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466739,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','158幢-105','158幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466771,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','164幢-103','164幢','103','2','0',UTC_TIMESTAMP(),999949,267.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466803,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','173幢-105','173幢','105','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466835,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','181幢-102','181幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466867,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-101','167幢','101','2','0',UTC_TIMESTAMP(),999949,261.55);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466899,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-101','188幢','101','2','0',UTC_TIMESTAMP(),999949,250.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466931,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-105','195幢','105','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466963,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-201','190幢','201','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466995,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','193幢-301','193幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467027,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','198幢-301','198幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467059,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','202幢-301','202幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467091,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','206幢-301','206幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467123,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','210幢-301','210幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467155,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','214幢-301','214幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466676,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','145幢-103','145幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466708,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','152幢-102','152幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466740,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','158幢-106','158幢','106','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466772,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','164幢-104','164幢','104','2','0',UTC_TIMESTAMP(),999949,276.83);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466804,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','173幢-106','173幢','106','2','0',UTC_TIMESTAMP(),999949,275.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466836,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','181幢-104','181幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466868,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-102','167幢','102','2','0',UTC_TIMESTAMP(),999949,251.02);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466900,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-102','188幢','102','2','0',UTC_TIMESTAMP(),999949,245.77);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466932,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-106','195幢','106','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466964,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-202','190幢','202','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466996,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','193幢-302','193幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467028,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','198幢-302','198幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467060,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','202幢-302','202幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467092,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','206幢-302','206幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467124,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','210幢-302','210幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467156,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','214幢-302','214幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466677,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','145幢-104','145幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466709,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','152幢-103','152幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466741,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','159幢-101','159幢','101','2','0',UTC_TIMESTAMP(),999949,275.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466773,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','165幢-101','165幢','101','2','0',UTC_TIMESTAMP(),999949,276.83);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466805,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','174幢-101','174幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466837,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','182幢-101','182幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466869,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-103','167幢','103','2','0',UTC_TIMESTAMP(),999949,256.96);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466901,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-103','188幢','103','2','0',UTC_TIMESTAMP(),999949,245.77);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466933,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-107','195幢','107','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466965,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-203','190幢','203','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466997,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','193幢-401','193幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467029,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','198幢-401','198幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467061,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','202幢-401','202幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467093,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','206幢-401','206幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467125,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','210幢-401','210幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467157,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','214幢-401','214幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466678,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','145幢-105','145幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466710,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','152幢-104','152幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466742,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','159幢-102','159幢','102','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466774,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','165幢-102','165幢','102','2','0',UTC_TIMESTAMP(),999949,267.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466806,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','174幢-102','174幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466838,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','182幢-102','182幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466870,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-104','167幢','104','2','0',UTC_TIMESTAMP(),999949,251.02);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466902,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-104','188幢','104','2','0',UTC_TIMESTAMP(),999949,245.77);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466934,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-108','195幢','108','2','0',UTC_TIMESTAMP(),999949,249.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466966,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-204','190幢','204','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466998,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','193幢-402','193幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467030,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','198幢-402','198幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467062,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','202幢-402','202幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467094,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','206幢-402','206幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467126,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','210幢-402','210幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467158,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','214幢-402','214幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466679,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','145幢-106','145幢','106','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466711,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','153幢-101','153幢','101','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466743,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','159幢-103','159幢','103','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466775,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','165幢-103','165幢','103','2','0',UTC_TIMESTAMP(),999949,267.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466807,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','174幢-103','174幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466839,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','182幢-103','182幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466871,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-105','167幢','105','2','0',UTC_TIMESTAMP(),999949,256.96);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466903,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-105','188幢','105','2','0',UTC_TIMESTAMP(),999949,245.77);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466935,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-301','195幢','301','2','0',UTC_TIMESTAMP(),999949,301.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466967,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-301','190幢','301','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466999,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','194幢-101','194幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467031,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','199幢-101','199幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467063,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','203幢-101','203幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467095,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','207幢-101','207幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467127,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','211幢-101','211幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467159,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','215幢-101','215幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466680,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','146幢-101','146幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466712,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','153幢-102','153幢','102','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466744,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','159幢-104','159幢','104','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466776,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','165幢-104','165幢','104','2','0',UTC_TIMESTAMP(),999949,276.83);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466808,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','174幢-104','174幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466840,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','182幢-104','182幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466872,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-106','167幢','106','2','0',UTC_TIMESTAMP(),999949,253.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466904,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-106','188幢','106','2','0',UTC_TIMESTAMP(),999949,250.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466936,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-302','195幢','302','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466968,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-302','190幢','302','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467000,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','194幢-102','194幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467032,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','199幢-102','199幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467064,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','203幢-102','203幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467096,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','207幢-102','207幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467128,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','211幢-102','211幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467160,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','215幢-102','215幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466681,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','146幢-103','146幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466713,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','153幢-103','153幢','103','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466745,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','159幢-105','159幢','105','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466777,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','169幢-101','169幢','101','2','0',UTC_TIMESTAMP(),999949,261.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466809,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','175幢-101','175幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466841,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','182幢-105','182幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466873,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-301','167幢','301','2','0',UTC_TIMESTAMP(),999949,295.81);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466905,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-301','188幢','301','2','0',UTC_TIMESTAMP(),999949,302.26);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466937,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-303','195幢','303','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466969,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-303','190幢','303','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467001,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','194幢-201','194幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467033,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','199幢-201','199幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467065,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','203幢-201','203幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467097,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','207幢-201','207幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467129,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','211幢-201','211幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467161,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','215幢-201','215幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466682,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','146幢-104','146幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466714,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','153幢-104','153幢','104','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466746,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','159幢-106','159幢','106','2','0',UTC_TIMESTAMP(),999949,275.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466778,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','169幢-102','169幢','102','2','0',UTC_TIMESTAMP(),999949,258.73);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466810,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','175幢-102','175幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466842,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','182幢-106','182幢','106','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466874,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-302','167幢','302','2','0',UTC_TIMESTAMP(),999949,297.43);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466906,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-302','188幢','302','2','0',UTC_TIMESTAMP(),999949,293.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466938,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-304','195幢','304','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466970,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-304','190幢','304','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467002,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','194幢-202','194幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467034,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','199幢-202','199幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467066,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','203幢-202','203幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467098,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','207幢-202','207幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467130,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','211幢-202','211幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467162,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','215幢-202','215幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466683,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','147幢-101','147幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466715,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','154幢-101','154幢','101','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466747,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','160幢-101','160幢','101','2','0',UTC_TIMESTAMP(),999949,275.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466779,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','169幢-103','169幢','103','2','0',UTC_TIMESTAMP(),999949,258.73);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466811,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','175幢-103','175幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466843,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','183幢-101','183幢','101','2','0',UTC_TIMESTAMP(),999949,275.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466875,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-303','167幢','303','2','0',UTC_TIMESTAMP(),999949,291.49);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466907,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-303','188幢','303','2','0',UTC_TIMESTAMP(),999949,293.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466939,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-305','195幢','305','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466971,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-401','190幢','401','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467003,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','194幢-301','194幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467035,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','199幢-301','199幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467067,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','203幢-301','203幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467099,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','207幢-301','207幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467131,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','211幢-301','211幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467163,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','215幢-301','215幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466684,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','147幢-102','147幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466716,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','154幢-102','154幢','102','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466748,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','160幢-102','160幢','102','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466780,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','169幢-104','169幢','104','2','0',UTC_TIMESTAMP(),999949,258.73);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466812,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','175幢-104','175幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466844,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','183幢-102','183幢','102','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466876,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-304','167幢','304','2','0',UTC_TIMESTAMP(),999949,297.43);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466908,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-304','188幢','304','2','0',UTC_TIMESTAMP(),999949,293.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466940,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-306','195幢','306','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466972,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-402','190幢','402','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467004,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','194幢-302','194幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467036,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','199幢-302','199幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467068,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','203幢-302','203幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467100,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','207幢-302','207幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467132,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','211幢-302','211幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467164,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','215幢-302','215幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466685,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','147幢-103','147幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466717,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','154幢-103','154幢','103','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466749,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','160幢-103','160幢','103','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466781,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','169幢-105','169幢','105','2','0',UTC_TIMESTAMP(),999949,258.73);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466813,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','176幢-101','176幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466845,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','183幢-103','183幢','103','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466877,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-305','167幢','305','2','0',UTC_TIMESTAMP(),999949,291.49);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466909,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-305','188幢','305','2','0',UTC_TIMESTAMP(),999949,293.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466941,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-307','195幢','307','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466973,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-403','190幢','403','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467005,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','194幢-401','194幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467037,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','199幢-401','199幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467069,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','203幢-401','203幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467101,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','207幢-401','207幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467133,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','211幢-401','211幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467165,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','215幢-401','215幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466686,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','147幢-104','147幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466718,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','154幢-104','154幢','104','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466750,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','160幢-104','160幢','104','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466782,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','169幢-106','169幢','106','2','0',UTC_TIMESTAMP(),999949,261.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466814,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','176幢-102','176幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466846,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','183幢-104','183幢','104','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466878,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','167幢-306','167幢','306','2','0',UTC_TIMESTAMP(),999949,303.43);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466910,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','188幢-306','188幢','306','2','0',UTC_TIMESTAMP(),999949,302.26);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466942,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-308','195幢','308','2','0',UTC_TIMESTAMP(),999949,301.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466974,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-404','190幢','404','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467006,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','194幢-402','194幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467038,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','199幢-402','199幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467070,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','203幢-402','203幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467102,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','207幢-402','207幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467134,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','211幢-402','211幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467166,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','215幢-402','215幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466687,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','147幢-105','147幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466719,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','155幢-101','155幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466751,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','160幢-105','160幢','105','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466783,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','170幢-101','170幢','101','2','0',UTC_TIMESTAMP(),999949,275.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466815,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','176幢-103','176幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466847,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','183幢-105','183幢','105','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466879,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-101','168幢','101','2','0',UTC_TIMESTAMP(),999949,261.55);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466911,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-101','189幢','101','2','0',UTC_TIMESTAMP(),999949,249.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466943,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-101','196幢','101','2','0',UTC_TIMESTAMP(),999949,249.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466975,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','191幢-101','191幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467007,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-101','197幢','101','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467039,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','200幢-101','200幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467071,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','204幢-101','204幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467103,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','208幢-101','208幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467135,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','212幢-101','212幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466688,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','147幢-106','147幢','106','2','0',UTC_TIMESTAMP(),999949,261.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466720,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','155幢-102','155幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466752,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','160幢-106','160幢','106','2','0',UTC_TIMESTAMP(),999949,275.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466784,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','170幢-102','170幢','102','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466816,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','177幢-101','177幢','101','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466848,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','183幢-106','183幢','106','2','0',UTC_TIMESTAMP(),999949,275.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466880,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-102','168幢','102','2','0',UTC_TIMESTAMP(),999949,251.02);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466912,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-102','189幢','102','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466944,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-102','196幢','102','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466976,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','191幢-102','191幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467008,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-102','197幢','102','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467040,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','200幢-102','200幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467072,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','204幢-102','204幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467104,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','208幢-102','208幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467136,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','212幢-102','212幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466689,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','148幢-101','148幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466721,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','155幢-103','155幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466753,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','161幢-101','161幢','101','2','0',UTC_TIMESTAMP(),999949,275.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466785,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','170幢-103','170幢','103','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466817,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','177幢-102','177幢','102','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466849,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','184幢-101','184幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466881,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-103','168幢','103','2','0',UTC_TIMESTAMP(),999949,256.96);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466913,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-103','189幢','103','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466945,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-103','196幢','103','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466977,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','191幢-201','191幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467009,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-103','197幢','103','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467041,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','200幢-201','200幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467073,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','204幢-201','204幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467105,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','208幢-201','208幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467137,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','212幢-201','212幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466690,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','148幢-102','148幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466722,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','155幢-104','155幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466754,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','161幢-102','161幢','102','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466786,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','170幢-104','170幢','104','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466818,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','177幢-103','177幢','103','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466850,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','184幢-102','184幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466882,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-104','168幢','104','2','0',UTC_TIMESTAMP(),999949,251.02);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466914,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-104','189幢','104','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466946,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-104','196幢','104','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466978,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','191幢-202','191幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467010,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-104','197幢','104','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467042,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','200幢-202','200幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467074,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','204幢-202','204幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467106,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','208幢-202','208幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467138,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','212幢-202','212幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466691,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','148幢-103','148幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466723,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','155幢-105','155幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466755,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','161幢-103','161幢','103','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466787,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','170幢-105','170幢','105','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466819,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','177幢-104','177幢','104','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466851,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','184幢-103','184幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466883,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-105','168幢','105','2','0',UTC_TIMESTAMP(),999949,256.96);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466915,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-105','189幢','105','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466947,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-105','196幢','105','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466979,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','191幢-301','191幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467011,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-201','197幢','201','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467043,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','200幢-301','200幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467075,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','204幢-301','204幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467107,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','208幢-301','208幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467139,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','212幢-301','212幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466692,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','148幢-104','148幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466724,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','155幢-106','155幢','106','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466756,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','161幢-104','161幢','104','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466788,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','170幢-106','170幢','106','2','0',UTC_TIMESTAMP(),999949,275.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466820,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','178幢-101','178幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466852,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','184幢-104','184幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466884,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-106','168幢','106','2','0',UTC_TIMESTAMP(),999949,253.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466916,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-106','189幢','106','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466948,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-106','196幢','106','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466980,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','191幢-302','191幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467012,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-202','197幢','202','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467044,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','200幢-302','200幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467076,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','204幢-302','204幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467108,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','208幢-302','208幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467140,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','212幢-302','212幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466693,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','149幢-101','149幢','101','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466725,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','156幢-101','156幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466757,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','161幢-105','161幢','105','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466789,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','171幢-101','171幢','101','2','0',UTC_TIMESTAMP(),999949,261.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466821,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','178幢-102','178幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466853,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','185幢-101','185幢','101','2','0',UTC_TIMESTAMP(),999949,275.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466885,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-301','168幢','301','2','0',UTC_TIMESTAMP(),999949,295.81);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466917,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-107','189幢','107','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466949,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-107','196幢','107','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466981,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','191幢-401','191幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467013,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-203','197幢','203','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467045,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','200幢-401','200幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467077,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','204幢-401','204幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467109,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','208幢-401','208幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467141,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','212幢-401','212幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466662,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','142幢-101','142幢','101','2','0',UTC_TIMESTAMP(),999949,276.83);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466694,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','149幢-102','149幢','102','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466726,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','156幢-102','156幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466758,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','161幢-106','161幢','106','2','0',UTC_TIMESTAMP(),999949,275.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466790,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','171幢-102','171幢','102','2','0',UTC_TIMESTAMP(),999949,258.73);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466822,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','178幢-103','178幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466854,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','185幢-102','185幢','102','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466886,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-302','168幢','302','2','0',UTC_TIMESTAMP(),999949,297.43);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466918,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-108','189幢','108','2','0',UTC_TIMESTAMP(),999949,249.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466950,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-108','196幢','108','2','0',UTC_TIMESTAMP(),999949,249.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466982,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','191幢-402','191幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467014,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-204','197幢','204','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467046,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','200幢-402','200幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467078,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','204幢-402','204幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467110,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','208幢-402','208幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467142,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','212幢-402','212幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466663,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','142幢-102','142幢','102','2','0',UTC_TIMESTAMP(),999949,267.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466695,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','149幢-103','149幢','103','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466727,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','156幢-103','156幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466759,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','162幢-101','162幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466791,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','171幢-103','171幢','103','2','0',UTC_TIMESTAMP(),999949,258.73);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466823,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','178幢-104','178幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466855,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','185幢-103','185幢','103','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466887,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-303','168幢','303','2','0',UTC_TIMESTAMP(),999949,291.49);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466919,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-301','189幢','301','2','0',UTC_TIMESTAMP(),999949,301.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466951,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-301','196幢','301','2','0',UTC_TIMESTAMP(),999949,301.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466983,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','192幢-101','192幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467015,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-301','197幢','301','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467047,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','201幢-101','201幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467079,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','205幢-101','205幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467111,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','209幢-101','209幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467143,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','213幢-101','213幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466664,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','142幢-103','142幢','103','2','0',UTC_TIMESTAMP(),999949,267.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466696,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','149幢-104','149幢','104','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466728,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','156幢-104','156幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466760,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','162幢-102','162幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466792,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','171幢-104','171幢','104','2','0',UTC_TIMESTAMP(),999949,258.73);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466824,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','179幢-101','179幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466856,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','185幢-104','185幢','104','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466888,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-304','168幢','304','2','0',UTC_TIMESTAMP(),999949,297.43);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466920,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-302','189幢','302','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466952,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-302','196幢','302','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466984,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','192幢-102','192幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467016,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-302','197幢','302','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467048,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','201幢-102','201幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467080,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','205幢-102','205幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467112,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','209幢-102','209幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467144,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','213幢-102','213幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466665,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','142幢-104','142幢','104','2','0',UTC_TIMESTAMP(),999949,276.83);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466697,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','150幢-101','150幢','101','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466729,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','157幢-101','157幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466761,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','162幢-103','162幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466793,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','171幢-105','171幢','105','2','0',UTC_TIMESTAMP(),999949,258.73);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466825,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','179幢-102','179幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466857,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','185幢-105','185幢','105','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466889,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-305','168幢','305','2','0',UTC_TIMESTAMP(),999949,291.49);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466921,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-303','189幢','303','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466953,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-303','196幢','303','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466985,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','192幢-201','192幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467017,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-303','197幢','303','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467049,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','201幢-201','201幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467081,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','205幢-201','205幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467113,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','209幢-201','209幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467145,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','213幢-201','213幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466666,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','143幢-101','143幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466698,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','150幢-102','150幢','102','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466730,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','157幢-102','157幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466762,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','162幢-104','162幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466794,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','171幢-106','171幢','106','2','0',UTC_TIMESTAMP(),999949,261.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466826,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','179幢-103','179幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466858,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','185幢-106','185幢','106','2','0',UTC_TIMESTAMP(),999949,275.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466890,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','168幢-306','168幢','306','2','0',UTC_TIMESTAMP(),999949,303.43);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466922,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-304','189幢','304','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466954,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-304','196幢','304','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466986,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','192幢-202','192幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467018,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-304','197幢','304','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467050,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','201幢-202','201幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467082,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','205幢-202','205幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467114,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','209幢-202','209幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467146,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','213幢-202','213幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466667,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','143幢-102','143幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466699,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','150幢-103','150幢','103','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466731,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','157幢-103','157幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466763,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','162幢-105','162幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466795,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','172幢-101','172幢','101','2','0',UTC_TIMESTAMP(),999949,276.83);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466827,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','179幢-104','179幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466859,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','166幢-101','166幢','101','2','0',UTC_TIMESTAMP(),999949,262.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466891,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','187幢-101','187幢','101','2','0',UTC_TIMESTAMP(),999949,262.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466923,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-305','189幢','305','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466955,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-305','196幢','305','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466987,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','192幢-301','192幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467019,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-401','197幢','401','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467051,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','201幢-301','201幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467083,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','205幢-301','205幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467115,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','209幢-301','209幢','301','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467147,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','213幢-301','213幢','301','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466668,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','143幢-103','143幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466700,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','150幢-104','150幢','104','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466732,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','157幢-104','157幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466764,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','162幢-106','162幢','106','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466796,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','172幢-102','172幢','102','2','0',UTC_TIMESTAMP(),999949,267.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466828,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','179幢-105','179幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466860,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','166幢-102','166幢','102','2','0',UTC_TIMESTAMP(),999949,251.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466892,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','187幢-102','187幢','102','2','0',UTC_TIMESTAMP(),999949,251.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466924,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-306','189幢','306','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466956,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-306','196幢','306','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466988,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','192幢-302','192幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467020,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-402','197幢','402','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467052,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','201幢-302','201幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467084,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','205幢-302','205幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467116,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','209幢-302','209幢','302','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467148,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','213幢-302','213幢','302','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466669,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','143幢-104','143幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466701,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','151幢-101','151幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466733,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','157幢-105','157幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466765,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','163幢-101','163幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466797,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','172幢-103','172幢','103','2','0',UTC_TIMESTAMP(),999949,267.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466829,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','179幢-106','179幢','106','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466861,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','166幢-103','166幢','103','2','0',UTC_TIMESTAMP(),999949,257.63);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466893,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','187幢-103','187幢','103','2','0',UTC_TIMESTAMP(),999949,257.63);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466925,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-307','189幢','307','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466957,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-307','196幢','307','2','0',UTC_TIMESTAMP(),999949,292.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466989,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','192幢-401','192幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467021,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-403','197幢','403','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467053,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','201幢-401','201幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467085,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','205幢-401','205幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467117,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','209幢-401','209幢','401','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467149,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','213幢-401','213幢','401','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466670,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','144幢-101','144幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466702,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','151幢-102','151幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466734,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','157幢-106','157幢','106','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466766,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','163幢-102','163幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466798,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','172幢-104','172幢','104','2','0',UTC_TIMESTAMP(),999949,276.83);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466830,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','180幢-101','180幢','101','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466862,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','166幢-104','166幢','104','2','0',UTC_TIMESTAMP(),999949,254.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466894,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','187幢-104','187幢','104','2','0',UTC_TIMESTAMP(),999949,254.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466926,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','189幢-308','189幢','308','2','0',UTC_TIMESTAMP(),999949,301.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466958,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','196幢-308','196幢','308','2','0',UTC_TIMESTAMP(),999949,301.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466990,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','192幢-402','192幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467022,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','197幢-404','197幢','404','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467054,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','201幢-402','201幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467086,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','205幢-402','205幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467118,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','209幢-402','209幢','402','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467150,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','213幢-402','213幢','402','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466671,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','144幢-102','144幢','102','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466703,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','151幢-103','151幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466735,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','158幢-101','158幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466767,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','163幢-103','163幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466799,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','173幢-101','173幢','101','2','0',UTC_TIMESTAMP(),999949,275.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466831,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','180幢-102','180幢','102','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466863,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','166幢-301','166幢','301','2','0',UTC_TIMESTAMP(),999949,296.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466895,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','187幢-301','187幢','301','2','0',UTC_TIMESTAMP(),999949,296.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466927,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-101','195幢','101','2','0',UTC_TIMESTAMP(),999949,249.89);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466959,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-101','190幢','101','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466991,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','193幢-101','193幢','101','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467023,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','198幢-101','198幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467055,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','202幢-101','202幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467087,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','206幢-101','206幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467119,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','210幢-101','210幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467151,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','214幢-101','214幢','101','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466672,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','144幢-103','144幢','103','2','0',UTC_TIMESTAMP(),999949,259.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466704,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','151幢-104','151幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466736,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','158幢-102','158幢','102','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466768,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','163幢-104','163幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466800,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','173幢-102','173幢','102','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466832,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','180幢-103','180幢','103','2','0',UTC_TIMESTAMP(),999949,283.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466864,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','166幢-302','166幢','302','2','0',UTC_TIMESTAMP(),999949,298.16);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466896,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','187幢-302','187幢','302','2','0',UTC_TIMESTAMP(),999949,298.16);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466928,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-102','195幢','102','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466960,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-102','190幢','102','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466992,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','193幢-102','193幢','102','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467024,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','198幢-102','198幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467056,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','202幢-102','202幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467088,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','206幢-102','206幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467120,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','210幢-102','210幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467152,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','214幢-102','214幢','102','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466673,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','144幢-104','144幢','104','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466705,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','151幢-105','151幢','105','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466737,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','158幢-103','158幢','103','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466769,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','164幢-101','164幢','101','2','0',UTC_TIMESTAMP(),999949,276.83);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466801,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','173幢-103','173幢','103','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466833,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','180幢-104','180幢','104','2','0',UTC_TIMESTAMP(),999949,297.57);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466865,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','166幢-303','166幢','303','2','0',UTC_TIMESTAMP(),999949,292.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466897,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','187幢-303','187幢','303','2','0',UTC_TIMESTAMP(),999949,292.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466929,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-103','195幢','103','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466961,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-103','190幢','103','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466993,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','193幢-201','193幢','201','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467025,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','198幢-201','198幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467057,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','202幢-201','202幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467089,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','206幢-201','206幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467121,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','210幢-201','210幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467153,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','214幢-201','214幢','201','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466674,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','145幢-101','145幢','101','2','0',UTC_TIMESTAMP(),999949,261.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466706,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','151幢-106','151幢','106','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466738,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','158幢-104','158幢','104','2','0',UTC_TIMESTAMP(),999949,258.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466770,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','164幢-102','164幢','102','2','0',UTC_TIMESTAMP(),999949,267.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466802,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','173幢-104','173幢','104','2','0',UTC_TIMESTAMP(),999949,266.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466834,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','181幢-101','181幢','101','2','0',UTC_TIMESTAMP(),999949,262.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466866,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','166幢-304','166幢','304','2','0',UTC_TIMESTAMP(),999949,304.18);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466898,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','187幢-304','187幢','304','2','0',UTC_TIMESTAMP(),999949,304.18);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466930,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','195幢-104','195幢','104','2','0',UTC_TIMESTAMP(),999949,245.51);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466962,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','190幢-104','190幢','104','2','0',UTC_TIMESTAMP(),999949,89.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387466994,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','193幢-202','193幢','202','2','0',UTC_TIMESTAMP(),999949,89.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467026,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','198幢-202','198幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467058,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','202幢-202','202幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467090,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','206幢-202','206幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467122,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','210幢-202','210幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467154,UUID(),240111044332060193,18558,'苏州市',18559,'相城区','214幢-202','214幢','202','2','0',UTC_TIMESTAMP(),999949,90.6);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90060,1041508,240111044332060193,239825274387466674,'145幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90092,1041508,240111044332060193,239825274387466706,'151幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90124,1041508,240111044332060193,239825274387466738,'158幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90156,1041508,240111044332060193,239825274387466770,'164幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90188,1041508,240111044332060193,239825274387466802,'173幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90220,1041508,240111044332060193,239825274387466834,'181幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90252,1041508,240111044332060193,239825274387466866,'166幢-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90284,1041508,240111044332060193,239825274387466898,'187幢-304',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90316,1041508,240111044332060193,239825274387466930,'195幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90348,1041508,240111044332060193,239825274387466962,'190幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90380,1041508,240111044332060193,239825274387466994,'193幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90412,1041508,240111044332060193,239825274387467026,'198幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90444,1041508,240111044332060193,239825274387467058,'202幢-202',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90476,1041508,240111044332060193,239825274387467090,'206幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90508,1041508,240111044332060193,239825274387467122,'210幢-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90540,1041508,240111044332060193,239825274387467154,'214幢-202',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90061,1041508,240111044332060193,239825274387466675,'145幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90093,1041508,240111044332060193,239825274387466707,'152幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90125,1041508,240111044332060193,239825274387466739,'158幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90157,1041508,240111044332060193,239825274387466771,'164幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90189,1041508,240111044332060193,239825274387466803,'173幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90221,1041508,240111044332060193,239825274387466835,'181幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90253,1041508,240111044332060193,239825274387466867,'167幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90285,1041508,240111044332060193,239825274387466899,'188幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90317,1041508,240111044332060193,239825274387466931,'195幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90349,1041508,240111044332060193,239825274387466963,'190幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90381,1041508,240111044332060193,239825274387466995,'193幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90413,1041508,240111044332060193,239825274387467027,'198幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90445,1041508,240111044332060193,239825274387467059,'202幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90477,1041508,240111044332060193,239825274387467091,'206幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90509,1041508,240111044332060193,239825274387467123,'210幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90541,1041508,240111044332060193,239825274387467155,'214幢-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90062,1041508,240111044332060193,239825274387466676,'145幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90094,1041508,240111044332060193,239825274387466708,'152幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90126,1041508,240111044332060193,239825274387466740,'158幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90158,1041508,240111044332060193,239825274387466772,'164幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90190,1041508,240111044332060193,239825274387466804,'173幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90222,1041508,240111044332060193,239825274387466836,'181幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90254,1041508,240111044332060193,239825274387466868,'167幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90286,1041508,240111044332060193,239825274387466900,'188幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90318,1041508,240111044332060193,239825274387466932,'195幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90350,1041508,240111044332060193,239825274387466964,'190幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90382,1041508,240111044332060193,239825274387466996,'193幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90414,1041508,240111044332060193,239825274387467028,'198幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90446,1041508,240111044332060193,239825274387467060,'202幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90478,1041508,240111044332060193,239825274387467092,'206幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90510,1041508,240111044332060193,239825274387467124,'210幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90542,1041508,240111044332060193,239825274387467156,'214幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90063,1041508,240111044332060193,239825274387466677,'145幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90095,1041508,240111044332060193,239825274387466709,'152幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90127,1041508,240111044332060193,239825274387466741,'159幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90159,1041508,240111044332060193,239825274387466773,'165幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90191,1041508,240111044332060193,239825274387466805,'174幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90223,1041508,240111044332060193,239825274387466837,'182幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90255,1041508,240111044332060193,239825274387466869,'167幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90287,1041508,240111044332060193,239825274387466901,'188幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90319,1041508,240111044332060193,239825274387466933,'195幢-107',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90351,1041508,240111044332060193,239825274387466965,'190幢-203',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90383,1041508,240111044332060193,239825274387466997,'193幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90415,1041508,240111044332060193,239825274387467029,'198幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90447,1041508,240111044332060193,239825274387467061,'202幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90479,1041508,240111044332060193,239825274387467093,'206幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90511,1041508,240111044332060193,239825274387467125,'210幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90543,1041508,240111044332060193,239825274387467157,'214幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90064,1041508,240111044332060193,239825274387466678,'145幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90096,1041508,240111044332060193,239825274387466710,'152幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90128,1041508,240111044332060193,239825274387466742,'159幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90160,1041508,240111044332060193,239825274387466774,'165幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90192,1041508,240111044332060193,239825274387466806,'174幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90224,1041508,240111044332060193,239825274387466838,'182幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90256,1041508,240111044332060193,239825274387466870,'167幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90288,1041508,240111044332060193,239825274387466902,'188幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90320,1041508,240111044332060193,239825274387466934,'195幢-108',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90352,1041508,240111044332060193,239825274387466966,'190幢-204',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90384,1041508,240111044332060193,239825274387466998,'193幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90416,1041508,240111044332060193,239825274387467030,'198幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90448,1041508,240111044332060193,239825274387467062,'202幢-402',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90480,1041508,240111044332060193,239825274387467094,'206幢-402',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90512,1041508,240111044332060193,239825274387467126,'210幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90544,1041508,240111044332060193,239825274387467158,'214幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90065,1041508,240111044332060193,239825274387466679,'145幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90097,1041508,240111044332060193,239825274387466711,'153幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90129,1041508,240111044332060193,239825274387466743,'159幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90161,1041508,240111044332060193,239825274387466775,'165幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90193,1041508,240111044332060193,239825274387466807,'174幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90225,1041508,240111044332060193,239825274387466839,'182幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90257,1041508,240111044332060193,239825274387466871,'167幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90289,1041508,240111044332060193,239825274387466903,'188幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90321,1041508,240111044332060193,239825274387466935,'195幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90353,1041508,240111044332060193,239825274387466967,'190幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90385,1041508,240111044332060193,239825274387466999,'194幢-101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90417,1041508,240111044332060193,239825274387467031,'199幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90449,1041508,240111044332060193,239825274387467063,'203幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90481,1041508,240111044332060193,239825274387467095,'207幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90513,1041508,240111044332060193,239825274387467127,'211幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90545,1041508,240111044332060193,239825274387467159,'215幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90066,1041508,240111044332060193,239825274387466680,'146幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90098,1041508,240111044332060193,239825274387466712,'153幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90130,1041508,240111044332060193,239825274387466744,'159幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90162,1041508,240111044332060193,239825274387466776,'165幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90194,1041508,240111044332060193,239825274387466808,'174幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90226,1041508,240111044332060193,239825274387466840,'182幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90258,1041508,240111044332060193,239825274387466872,'167幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90290,1041508,240111044332060193,239825274387466904,'188幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90322,1041508,240111044332060193,239825274387466936,'195幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90354,1041508,240111044332060193,239825274387466968,'190幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90386,1041508,240111044332060193,239825274387467000,'194幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90418,1041508,240111044332060193,239825274387467032,'199幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90450,1041508,240111044332060193,239825274387467064,'203幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90482,1041508,240111044332060193,239825274387467096,'207幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90514,1041508,240111044332060193,239825274387467128,'211幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90546,1041508,240111044332060193,239825274387467160,'215幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90067,1041508,240111044332060193,239825274387466681,'146幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90099,1041508,240111044332060193,239825274387466713,'153幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90131,1041508,240111044332060193,239825274387466745,'159幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90163,1041508,240111044332060193,239825274387466777,'169幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90195,1041508,240111044332060193,239825274387466809,'175幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90227,1041508,240111044332060193,239825274387466841,'182幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90259,1041508,240111044332060193,239825274387466873,'167幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90291,1041508,240111044332060193,239825274387466905,'188幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90323,1041508,240111044332060193,239825274387466937,'195幢-303',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90355,1041508,240111044332060193,239825274387466969,'190幢-303',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90387,1041508,240111044332060193,239825274387467001,'194幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90419,1041508,240111044332060193,239825274387467033,'199幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90451,1041508,240111044332060193,239825274387467065,'203幢-201',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90483,1041508,240111044332060193,239825274387467097,'207幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90515,1041508,240111044332060193,239825274387467129,'211幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90547,1041508,240111044332060193,239825274387467161,'215幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90068,1041508,240111044332060193,239825274387466682,'146幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90100,1041508,240111044332060193,239825274387466714,'153幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90132,1041508,240111044332060193,239825274387466746,'159幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90164,1041508,240111044332060193,239825274387466778,'169幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90196,1041508,240111044332060193,239825274387466810,'175幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90228,1041508,240111044332060193,239825274387466842,'182幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90260,1041508,240111044332060193,239825274387466874,'167幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90292,1041508,240111044332060193,239825274387466906,'188幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90324,1041508,240111044332060193,239825274387466938,'195幢-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90356,1041508,240111044332060193,239825274387466970,'190幢-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90388,1041508,240111044332060193,239825274387467002,'194幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90420,1041508,240111044332060193,239825274387467034,'199幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90452,1041508,240111044332060193,239825274387467066,'203幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90484,1041508,240111044332060193,239825274387467098,'207幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90516,1041508,240111044332060193,239825274387467130,'211幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90548,1041508,240111044332060193,239825274387467162,'215幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90069,1041508,240111044332060193,239825274387466683,'147幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90101,1041508,240111044332060193,239825274387466715,'154幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90133,1041508,240111044332060193,239825274387466747,'160幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90165,1041508,240111044332060193,239825274387466779,'169幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90197,1041508,240111044332060193,239825274387466811,'175幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90229,1041508,240111044332060193,239825274387466843,'183幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90261,1041508,240111044332060193,239825274387466875,'167幢-303',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90293,1041508,240111044332060193,239825274387466907,'188幢-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90325,1041508,240111044332060193,239825274387466939,'195幢-305',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90357,1041508,240111044332060193,239825274387466971,'190幢-401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90389,1041508,240111044332060193,239825274387467003,'194幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90421,1041508,240111044332060193,239825274387467035,'199幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90453,1041508,240111044332060193,239825274387467067,'203幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90485,1041508,240111044332060193,239825274387467099,'207幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90517,1041508,240111044332060193,239825274387467131,'211幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90549,1041508,240111044332060193,239825274387467163,'215幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90070,1041508,240111044332060193,239825274387466684,'147幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90102,1041508,240111044332060193,239825274387466716,'154幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90134,1041508,240111044332060193,239825274387466748,'160幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90166,1041508,240111044332060193,239825274387466780,'169幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90198,1041508,240111044332060193,239825274387466812,'175幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90230,1041508,240111044332060193,239825274387466844,'183幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90262,1041508,240111044332060193,239825274387466876,'167幢-304',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90294,1041508,240111044332060193,239825274387466908,'188幢-304',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90326,1041508,240111044332060193,239825274387466940,'195幢-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90358,1041508,240111044332060193,239825274387466972,'190幢-402',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90390,1041508,240111044332060193,239825274387467004,'194幢-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90422,1041508,240111044332060193,239825274387467036,'199幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90454,1041508,240111044332060193,239825274387467068,'203幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90486,1041508,240111044332060193,239825274387467100,'207幢-302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90518,1041508,240111044332060193,239825274387467132,'211幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90550,1041508,240111044332060193,239825274387467164,'215幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90071,1041508,240111044332060193,239825274387466685,'147幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90103,1041508,240111044332060193,239825274387466717,'154幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90135,1041508,240111044332060193,239825274387466749,'160幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90167,1041508,240111044332060193,239825274387466781,'169幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90199,1041508,240111044332060193,239825274387466813,'176幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90231,1041508,240111044332060193,239825274387466845,'183幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90263,1041508,240111044332060193,239825274387466877,'167幢-305',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90295,1041508,240111044332060193,239825274387466909,'188幢-305',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90327,1041508,240111044332060193,239825274387466941,'195幢-307',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90359,1041508,240111044332060193,239825274387466973,'190幢-403',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90391,1041508,240111044332060193,239825274387467005,'194幢-401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90423,1041508,240111044332060193,239825274387467037,'199幢-401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90455,1041508,240111044332060193,239825274387467069,'203幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90487,1041508,240111044332060193,239825274387467101,'207幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90519,1041508,240111044332060193,239825274387467133,'211幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90551,1041508,240111044332060193,239825274387467165,'215幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90072,1041508,240111044332060193,239825274387466686,'147幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90104,1041508,240111044332060193,239825274387466718,'154幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90136,1041508,240111044332060193,239825274387466750,'160幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90168,1041508,240111044332060193,239825274387466782,'169幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90200,1041508,240111044332060193,239825274387466814,'176幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90232,1041508,240111044332060193,239825274387466846,'183幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90264,1041508,240111044332060193,239825274387466878,'167幢-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90296,1041508,240111044332060193,239825274387466910,'188幢-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90328,1041508,240111044332060193,239825274387466942,'195幢-308',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90360,1041508,240111044332060193,239825274387466974,'190幢-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90392,1041508,240111044332060193,239825274387467006,'194幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90424,1041508,240111044332060193,239825274387467038,'199幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90456,1041508,240111044332060193,239825274387467070,'203幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90488,1041508,240111044332060193,239825274387467102,'207幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90520,1041508,240111044332060193,239825274387467134,'211幢-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90552,1041508,240111044332060193,239825274387467166,'215幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90073,1041508,240111044332060193,239825274387466687,'147幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90105,1041508,240111044332060193,239825274387466719,'155幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90137,1041508,240111044332060193,239825274387466751,'160幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90169,1041508,240111044332060193,239825274387466783,'170幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90201,1041508,240111044332060193,239825274387466815,'176幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90233,1041508,240111044332060193,239825274387466847,'183幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90265,1041508,240111044332060193,239825274387466879,'168幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90297,1041508,240111044332060193,239825274387466911,'189幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90329,1041508,240111044332060193,239825274387466943,'196幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90361,1041508,240111044332060193,239825274387466975,'191幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90393,1041508,240111044332060193,239825274387467007,'197幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90425,1041508,240111044332060193,239825274387467039,'200幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90457,1041508,240111044332060193,239825274387467071,'204幢-101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90489,1041508,240111044332060193,239825274387467103,'208幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90521,1041508,240111044332060193,239825274387467135,'212幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90074,1041508,240111044332060193,239825274387466688,'147幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90106,1041508,240111044332060193,239825274387466720,'155幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90138,1041508,240111044332060193,239825274387466752,'160幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90170,1041508,240111044332060193,239825274387466784,'170幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90202,1041508,240111044332060193,239825274387466816,'177幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90234,1041508,240111044332060193,239825274387466848,'183幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90266,1041508,240111044332060193,239825274387466880,'168幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90298,1041508,240111044332060193,239825274387466912,'189幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90330,1041508,240111044332060193,239825274387466944,'196幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90362,1041508,240111044332060193,239825274387466976,'191幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90394,1041508,240111044332060193,239825274387467008,'197幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90426,1041508,240111044332060193,239825274387467040,'200幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90458,1041508,240111044332060193,239825274387467072,'204幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90490,1041508,240111044332060193,239825274387467104,'208幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90522,1041508,240111044332060193,239825274387467136,'212幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90075,1041508,240111044332060193,239825274387466689,'148幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90107,1041508,240111044332060193,239825274387466721,'155幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90139,1041508,240111044332060193,239825274387466753,'161幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90171,1041508,240111044332060193,239825274387466785,'170幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90203,1041508,240111044332060193,239825274387466817,'177幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90235,1041508,240111044332060193,239825274387466849,'184幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90267,1041508,240111044332060193,239825274387466881,'168幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90299,1041508,240111044332060193,239825274387466913,'189幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90331,1041508,240111044332060193,239825274387466945,'196幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90363,1041508,240111044332060193,239825274387466977,'191幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90395,1041508,240111044332060193,239825274387467009,'197幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90427,1041508,240111044332060193,239825274387467041,'200幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90459,1041508,240111044332060193,239825274387467073,'204幢-201',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90491,1041508,240111044332060193,239825274387467105,'208幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90523,1041508,240111044332060193,239825274387467137,'212幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90076,1041508,240111044332060193,239825274387466690,'148幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90108,1041508,240111044332060193,239825274387466722,'155幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90140,1041508,240111044332060193,239825274387466754,'161幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90172,1041508,240111044332060193,239825274387466786,'170幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90204,1041508,240111044332060193,239825274387466818,'177幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90236,1041508,240111044332060193,239825274387466850,'184幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90268,1041508,240111044332060193,239825274387466882,'168幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90300,1041508,240111044332060193,239825274387466914,'189幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90332,1041508,240111044332060193,239825274387466946,'196幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90364,1041508,240111044332060193,239825274387466978,'191幢-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90396,1041508,240111044332060193,239825274387467010,'197幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90428,1041508,240111044332060193,239825274387467042,'200幢-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90460,1041508,240111044332060193,239825274387467074,'204幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90492,1041508,240111044332060193,239825274387467106,'208幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90524,1041508,240111044332060193,239825274387467138,'212幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90077,1041508,240111044332060193,239825274387466691,'148幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90109,1041508,240111044332060193,239825274387466723,'155幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90141,1041508,240111044332060193,239825274387466755,'161幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90173,1041508,240111044332060193,239825274387466787,'170幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90205,1041508,240111044332060193,239825274387466819,'177幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90237,1041508,240111044332060193,239825274387466851,'184幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90269,1041508,240111044332060193,239825274387466883,'168幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90301,1041508,240111044332060193,239825274387466915,'189幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90333,1041508,240111044332060193,239825274387466947,'196幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90365,1041508,240111044332060193,239825274387466979,'191幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90397,1041508,240111044332060193,239825274387467011,'197幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90429,1041508,240111044332060193,239825274387467043,'200幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90461,1041508,240111044332060193,239825274387467075,'204幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90493,1041508,240111044332060193,239825274387467107,'208幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90525,1041508,240111044332060193,239825274387467139,'212幢-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90078,1041508,240111044332060193,239825274387466692,'148幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90110,1041508,240111044332060193,239825274387466724,'155幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90142,1041508,240111044332060193,239825274387466756,'161幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90174,1041508,240111044332060193,239825274387466788,'170幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90206,1041508,240111044332060193,239825274387466820,'178幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90238,1041508,240111044332060193,239825274387466852,'184幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90270,1041508,240111044332060193,239825274387466884,'168幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90302,1041508,240111044332060193,239825274387466916,'189幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90334,1041508,240111044332060193,239825274387466948,'196幢-106',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90366,1041508,240111044332060193,239825274387466980,'191幢-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90398,1041508,240111044332060193,239825274387467012,'197幢-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90430,1041508,240111044332060193,239825274387467044,'200幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90462,1041508,240111044332060193,239825274387467076,'204幢-302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90494,1041508,240111044332060193,239825274387467108,'208幢-302',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90526,1041508,240111044332060193,239825274387467140,'212幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90079,1041508,240111044332060193,239825274387466693,'149幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90111,1041508,240111044332060193,239825274387466725,'156幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90143,1041508,240111044332060193,239825274387466757,'161幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90175,1041508,240111044332060193,239825274387466789,'171幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90207,1041508,240111044332060193,239825274387466821,'178幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90239,1041508,240111044332060193,239825274387466853,'185幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90271,1041508,240111044332060193,239825274387466885,'168幢-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90303,1041508,240111044332060193,239825274387466917,'189幢-107',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90335,1041508,240111044332060193,239825274387466949,'196幢-107',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90367,1041508,240111044332060193,239825274387466981,'191幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90399,1041508,240111044332060193,239825274387467013,'197幢-203',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90431,1041508,240111044332060193,239825274387467045,'200幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90463,1041508,240111044332060193,239825274387467077,'204幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90495,1041508,240111044332060193,239825274387467109,'208幢-401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90527,1041508,240111044332060193,239825274387467141,'212幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90048,1041508,240111044332060193,239825274387466662,'142幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90080,1041508,240111044332060193,239825274387466694,'149幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90112,1041508,240111044332060193,239825274387466726,'156幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90144,1041508,240111044332060193,239825274387466758,'161幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90176,1041508,240111044332060193,239825274387466790,'171幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90208,1041508,240111044332060193,239825274387466822,'178幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90240,1041508,240111044332060193,239825274387466854,'185幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90272,1041508,240111044332060193,239825274387466886,'168幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90304,1041508,240111044332060193,239825274387466918,'189幢-108',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90336,1041508,240111044332060193,239825274387466950,'196幢-108',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90368,1041508,240111044332060193,239825274387466982,'191幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90400,1041508,240111044332060193,239825274387467014,'197幢-204',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90432,1041508,240111044332060193,239825274387467046,'200幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90464,1041508,240111044332060193,239825274387467078,'204幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90496,1041508,240111044332060193,239825274387467110,'208幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90528,1041508,240111044332060193,239825274387467142,'212幢-402',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90049,1041508,240111044332060193,239825274387466663,'142幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90081,1041508,240111044332060193,239825274387466695,'149幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90113,1041508,240111044332060193,239825274387466727,'156幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90145,1041508,240111044332060193,239825274387466759,'162幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90177,1041508,240111044332060193,239825274387466791,'171幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90209,1041508,240111044332060193,239825274387466823,'178幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90241,1041508,240111044332060193,239825274387466855,'185幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90273,1041508,240111044332060193,239825274387466887,'168幢-303',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90305,1041508,240111044332060193,239825274387466919,'189幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90337,1041508,240111044332060193,239825274387466951,'196幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90369,1041508,240111044332060193,239825274387466983,'192幢-101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90401,1041508,240111044332060193,239825274387467015,'197幢-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90433,1041508,240111044332060193,239825274387467047,'201幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90465,1041508,240111044332060193,239825274387467079,'205幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90497,1041508,240111044332060193,239825274387467111,'209幢-101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90529,1041508,240111044332060193,239825274387467143,'213幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90050,1041508,240111044332060193,239825274387466664,'142幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90082,1041508,240111044332060193,239825274387466696,'149幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90114,1041508,240111044332060193,239825274387466728,'156幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90146,1041508,240111044332060193,239825274387466760,'162幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90178,1041508,240111044332060193,239825274387466792,'171幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90210,1041508,240111044332060193,239825274387466824,'179幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90242,1041508,240111044332060193,239825274387466856,'185幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90274,1041508,240111044332060193,239825274387466888,'168幢-304',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90306,1041508,240111044332060193,239825274387466920,'189幢-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90338,1041508,240111044332060193,239825274387466952,'196幢-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90370,1041508,240111044332060193,239825274387466984,'192幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90402,1041508,240111044332060193,239825274387467016,'197幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90434,1041508,240111044332060193,239825274387467048,'201幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90466,1041508,240111044332060193,239825274387467080,'205幢-102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90498,1041508,240111044332060193,239825274387467112,'209幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90530,1041508,240111044332060193,239825274387467144,'213幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90051,1041508,240111044332060193,239825274387466665,'142幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90083,1041508,240111044332060193,239825274387466697,'150幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90115,1041508,240111044332060193,239825274387466729,'157幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90147,1041508,240111044332060193,239825274387466761,'162幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90179,1041508,240111044332060193,239825274387466793,'171幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90211,1041508,240111044332060193,239825274387466825,'179幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90243,1041508,240111044332060193,239825274387466857,'185幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90275,1041508,240111044332060193,239825274387466889,'168幢-305',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90307,1041508,240111044332060193,239825274387466921,'189幢-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90339,1041508,240111044332060193,239825274387466953,'196幢-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90371,1041508,240111044332060193,239825274387466985,'192幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90403,1041508,240111044332060193,239825274387467017,'197幢-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90435,1041508,240111044332060193,239825274387467049,'201幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90467,1041508,240111044332060193,239825274387467081,'205幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90499,1041508,240111044332060193,239825274387467113,'209幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90531,1041508,240111044332060193,239825274387467145,'213幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90052,1041508,240111044332060193,239825274387466666,'143幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90084,1041508,240111044332060193,239825274387466698,'150幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90116,1041508,240111044332060193,239825274387466730,'157幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90148,1041508,240111044332060193,239825274387466762,'162幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90180,1041508,240111044332060193,239825274387466794,'171幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90212,1041508,240111044332060193,239825274387466826,'179幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90244,1041508,240111044332060193,239825274387466858,'185幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90276,1041508,240111044332060193,239825274387466890,'168幢-306',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90308,1041508,240111044332060193,239825274387466922,'189幢-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90340,1041508,240111044332060193,239825274387466954,'196幢-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90372,1041508,240111044332060193,239825274387466986,'192幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90404,1041508,240111044332060193,239825274387467018,'197幢-304',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90436,1041508,240111044332060193,239825274387467050,'201幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90468,1041508,240111044332060193,239825274387467082,'205幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90500,1041508,240111044332060193,239825274387467114,'209幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90532,1041508,240111044332060193,239825274387467146,'213幢-202',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90053,1041508,240111044332060193,239825274387466667,'143幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90085,1041508,240111044332060193,239825274387466699,'150幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90117,1041508,240111044332060193,239825274387466731,'157幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90149,1041508,240111044332060193,239825274387466763,'162幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90181,1041508,240111044332060193,239825274387466795,'172幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90213,1041508,240111044332060193,239825274387466827,'179幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90245,1041508,240111044332060193,239825274387466859,'166幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90277,1041508,240111044332060193,239825274387466891,'187幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90309,1041508,240111044332060193,239825274387466923,'189幢-305',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90341,1041508,240111044332060193,239825274387466955,'196幢-305',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90373,1041508,240111044332060193,239825274387466987,'192幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90405,1041508,240111044332060193,239825274387467019,'197幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90437,1041508,240111044332060193,239825274387467051,'201幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90469,1041508,240111044332060193,239825274387467083,'205幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90501,1041508,240111044332060193,239825274387467115,'209幢-301',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90533,1041508,240111044332060193,239825274387467147,'213幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90054,1041508,240111044332060193,239825274387466668,'143幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90086,1041508,240111044332060193,239825274387466700,'150幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90118,1041508,240111044332060193,239825274387466732,'157幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90150,1041508,240111044332060193,239825274387466764,'162幢-106',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90182,1041508,240111044332060193,239825274387466796,'172幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90214,1041508,240111044332060193,239825274387466828,'179幢-105',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90246,1041508,240111044332060193,239825274387466860,'166幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90278,1041508,240111044332060193,239825274387466892,'187幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90310,1041508,240111044332060193,239825274387466924,'189幢-306',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90342,1041508,240111044332060193,239825274387466956,'196幢-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90374,1041508,240111044332060193,239825274387466988,'192幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90406,1041508,240111044332060193,239825274387467020,'197幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90438,1041508,240111044332060193,239825274387467052,'201幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90470,1041508,240111044332060193,239825274387467084,'205幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90502,1041508,240111044332060193,239825274387467116,'209幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90534,1041508,240111044332060193,239825274387467148,'213幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90055,1041508,240111044332060193,239825274387466669,'143幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90087,1041508,240111044332060193,239825274387466701,'151幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90119,1041508,240111044332060193,239825274387466733,'157幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90151,1041508,240111044332060193,239825274387466765,'163幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90183,1041508,240111044332060193,239825274387466797,'172幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90215,1041508,240111044332060193,239825274387466829,'179幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90247,1041508,240111044332060193,239825274387466861,'166幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90279,1041508,240111044332060193,239825274387466893,'187幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90311,1041508,240111044332060193,239825274387466925,'189幢-307',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90343,1041508,240111044332060193,239825274387466957,'196幢-307',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90375,1041508,240111044332060193,239825274387466989,'192幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90407,1041508,240111044332060193,239825274387467021,'197幢-403',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90439,1041508,240111044332060193,239825274387467053,'201幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90471,1041508,240111044332060193,239825274387467085,'205幢-401',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90503,1041508,240111044332060193,239825274387467117,'209幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90535,1041508,240111044332060193,239825274387467149,'213幢-401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90056,1041508,240111044332060193,239825274387466670,'144幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90088,1041508,240111044332060193,239825274387466702,'151幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90120,1041508,240111044332060193,239825274387466734,'157幢-106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90152,1041508,240111044332060193,239825274387466766,'163幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90184,1041508,240111044332060193,239825274387466798,'172幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90216,1041508,240111044332060193,239825274387466830,'180幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90248,1041508,240111044332060193,239825274387466862,'166幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90280,1041508,240111044332060193,239825274387466894,'187幢-104',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90312,1041508,240111044332060193,239825274387466926,'189幢-308',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90344,1041508,240111044332060193,239825274387466958,'196幢-308',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90376,1041508,240111044332060193,239825274387466990,'192幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90408,1041508,240111044332060193,239825274387467022,'197幢-404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90440,1041508,240111044332060193,239825274387467054,'201幢-402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90472,1041508,240111044332060193,239825274387467086,'205幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90504,1041508,240111044332060193,239825274387467118,'209幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90536,1041508,240111044332060193,239825274387467150,'213幢-402',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90057,1041508,240111044332060193,239825274387466671,'144幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90089,1041508,240111044332060193,239825274387466703,'151幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90121,1041508,240111044332060193,239825274387466735,'158幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90153,1041508,240111044332060193,239825274387466767,'163幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90185,1041508,240111044332060193,239825274387466799,'173幢-101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90217,1041508,240111044332060193,239825274387466831,'180幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90249,1041508,240111044332060193,239825274387466863,'166幢-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90281,1041508,240111044332060193,239825274387466895,'187幢-301',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90313,1041508,240111044332060193,239825274387466927,'195幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90345,1041508,240111044332060193,239825274387466959,'190幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90377,1041508,240111044332060193,239825274387466991,'193幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90409,1041508,240111044332060193,239825274387467023,'198幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90441,1041508,240111044332060193,239825274387467055,'202幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90473,1041508,240111044332060193,239825274387467087,'206幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90505,1041508,240111044332060193,239825274387467119,'210幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90537,1041508,240111044332060193,239825274387467151,'214幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90058,1041508,240111044332060193,239825274387466672,'144幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90090,1041508,240111044332060193,239825274387466704,'151幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90122,1041508,240111044332060193,239825274387466736,'158幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90154,1041508,240111044332060193,239825274387466768,'163幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90186,1041508,240111044332060193,239825274387466800,'173幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90218,1041508,240111044332060193,239825274387466832,'180幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90250,1041508,240111044332060193,239825274387466864,'166幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90282,1041508,240111044332060193,239825274387466896,'187幢-302',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90314,1041508,240111044332060193,239825274387466928,'195幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90346,1041508,240111044332060193,239825274387466960,'190幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90378,1041508,240111044332060193,239825274387466992,'193幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90410,1041508,240111044332060193,239825274387467024,'198幢-102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90442,1041508,240111044332060193,239825274387467056,'202幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90474,1041508,240111044332060193,239825274387467088,'206幢-102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90506,1041508,240111044332060193,239825274387467120,'210幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90538,1041508,240111044332060193,239825274387467152,'214幢-102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90059,1041508,240111044332060193,239825274387466673,'144幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90091,1041508,240111044332060193,239825274387466705,'151幢-105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90123,1041508,240111044332060193,239825274387466737,'158幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90155,1041508,240111044332060193,239825274387466769,'164幢-101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90187,1041508,240111044332060193,239825274387466801,'173幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90219,1041508,240111044332060193,239825274387466833,'180幢-104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90251,1041508,240111044332060193,239825274387466865,'166幢-303',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90283,1041508,240111044332060193,239825274387466897,'187幢-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90315,1041508,240111044332060193,239825274387466929,'195幢-103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90347,1041508,240111044332060193,239825274387466961,'190幢-103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90379,1041508,240111044332060193,239825274387466993,'193幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90411,1041508,240111044332060193,239825274387467025,'198幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90443,1041508,240111044332060193,239825274387467057,'202幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90475,1041508,240111044332060193,239825274387467089,'206幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90507,1041508,240111044332060193,239825274387467121,'210幢-201',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90539,1041508,240111044332060193,239825274387467153,'214幢-201',1);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141161,10000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141162,10100,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141163,10400,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141164,10600,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141165,10750,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141166,10751,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141167,10752,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141168,10800,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141169,13000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141170,11000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141171,12200,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141172,20000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141173,20100,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141174,20140,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141175,20150,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141176,20155,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141177,20158,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141178,20170,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141179,20180,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141180,20190,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141181,20191,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141182,20230,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141183,20220,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141184,20240,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141185,20250,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141186,20255,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141187,20258,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141188,20280,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141189,20290,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141190,20291,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141191,20400,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141192,20422,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141193,204011,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141194,204021,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141195,20430,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141196,20800,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141197,20810,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141198,20811,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141199,20812,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141200,20820,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141201,20821,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141202,20822,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141203,20830,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141204,20831,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141205,20840,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141206,20841,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141207,20850,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141208,20851,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141209,20852,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141210,20860,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141211,20600,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141212,20610,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141213,20620,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141214,20630,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141215,20640,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141216,20650,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141217,20655,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141218,20670,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141219,20671,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141220,20672,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141221,20673,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141222,20680,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141223,49100,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141224,49110,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141225,49150,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141226,49120,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141227,49130,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141228,49140,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141229,21000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141230,21010,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141231,21020,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141232,21022,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141233,21024,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141234,21030,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141235,21032,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141236,21034,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141237,21040,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141238,21042,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141239,21044,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141240,21050,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141241,21052,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141242,21054,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141243,21100,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141244,21110,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141245,21120,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141246,21200,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141247,21210,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141248,21220,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141249,21230,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141250,40000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141251,40300,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141252,40800,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141253,40830,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141254,40835,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141255,40810,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141256,40840,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141257,40850,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141258,40900,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141259,41000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141260,41010,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141261,41020,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141262,41030,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141263,41040,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141264,41050,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141265,41060,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141266,30000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141267,30500,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141268,31000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141269,32000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141270,34000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141271,35000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141272,30600,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141273,37000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141274,50000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141275,50100,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141276,50300,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141277,50500,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141278,50600,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141279,50700,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141280,50710,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141281,50720,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141282,50730,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141283,50800,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141284,50810,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141285,50820,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141286,50830,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141287,50840,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141288,50850,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141289,50860,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141290,50900,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141291,52000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141292,52010,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141293,52020,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141294,52030,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141295,70000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141296,70300,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141297,70100,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141298,70200,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141299,60000,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141300,60100,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141301,60200,NULL,'EhNamespaces',999949,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141302,80000,NULL,'EhNamespaces',999949,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001407,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999949,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001408,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999949,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001409,'',0,1001409,1,'活动管理-默认子分类','/1/1001409',0,2,1,NOW(),0,NULL,999949,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001410,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999949,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001411,'',0,1001411,2,'活动管理二-默认子分类','/2/1001411',0,2,1,NOW(),0,NULL,999949,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001412,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999949,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001413,'',0,1001413,3,'活动管理三-默认子分类','/3/1001413',0,2,1,NOW(),0,NULL,999949,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (554,999949,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (555,999949,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (556,999949,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (557,999949,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (558,999949,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (559,999949,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (560,999949,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (561,999949,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (562,999949,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1041508,240111044332060193);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1125,999949,'ServiceMarketLayout','{"versionCode":"2018020701","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018020701,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1126,999949,'ServiceMarketLayout','{"versionCode":"2018020701","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":20,"paddingBottom":20,"paddingLeft":20,"paddingRight":20,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018020701,0,2,UTC_TIMESTAMP(),'default',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217049,999949,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRveE16Tm1aR00xTkdFM1kyVXhaVGRoWWpKaE9UQmtaRE15WkRkall6ZzRaQQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217050,999949,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRveE16Tm1aR00xTkdFM1kyVXhaVGRoWWpKaE9UQmtaRE15WkRkall6ZzRaQQ',0,NULL,2,10,0,UTC_TIMESTAMP(),'default');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161016,999949,0,0,0,'/home','NavigatorGroup2','移动考勤','移动考勤','cs://1/image/aW1hZ2UvTVRvd09XTXdPV0kxWVRWa1pESmlPVEkwTWpSa01UUXdaVEpqTTJRME9HVXpOZw',1,1,23,'',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161017,999949,0,0,0,'/home','NavigatorGroup2','移动考勤','移动考勤','cs://1/image/aW1hZ2UvTVRvd09XTXdPV0kxWVRWa1pESmlPVEkwTWpSa01UUXdaVEpqTTJRME9HVXpOZw',1,1,23,'',10,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161018,999949,0,0,0,'/home','NavigatorGroup2','智能门禁','智能门禁','cs://1/image/aW1hZ2UvTVRwaE5qa3hNalZsWVRVNE5qQm1NV0poTnpabVlURXlZbVEyWmpNMU9UVXpaUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161019,999949,0,0,0,'/home','NavigatorGroup2','智能门禁','智能门禁','cs://1/image/aW1hZ2UvTVRwaE5qa3hNalZsWVRVNE5qQm1NV0poTnpabVlURXlZbVEyWmpNMU9UVXpaUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',20,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161020,999949,0,0,0,'/home','NavigatorGroup2','停车充值','停车充值','cs://1/image/aW1hZ2UvTVRvNU9ERmlNalkxTWpkbU1UUmhOREJrTldVNE5USmxNRE5oTkdaak1HWXpNZw',1,1,30,'',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161021,999949,0,0,0,'/home','NavigatorGroup2','停车充值','停车充值','cs://1/image/aW1hZ2UvTVRvNU9ERmlNalkxTWpkbU1UUmhOREJrTldVNE5USmxNRE5oTkdaak1HWXpNZw',1,1,30,'',30,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161022,999949,0,0,0,'/home','NavigatorGroup2','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRwa1pERTVOamt6TTJJNVkyRmhOak5sWlRBNU16WXlZak16TlRWaVlqWTNZZw',1,1,36,'{"privateFlag": 0}',40,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161023,999949,0,0,0,'/home','NavigatorGroup2','俱乐部','俱乐部','cs://1/image/aW1hZ2UvTVRwa1pERTVOamt6TTJJNVkyRmhOak5sWlRBNU16WXlZak16TlRWaVlqWTNZZw',1,1,36,'{"privateFlag": 0}',40,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161024,999949,0,0,0,'/home','NavigatorGroup4','物业巡检','物业巡检','cs://1/image/aW1hZ2UvTVRvM09USXlaVEUyTnpBME0yVmtZVFF5TUdRellXVXlNalF3TWpreVpqTm1Zdw',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',10,0,1,1,0,1,'pm_admin',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161025,999949,0,0,0,'/home','NavigatorGroup4','物业巡检','物业巡检','cs://1/image/aW1hZ2UvTVRvM09USXlaVEUyTnpBME0yVmtZVFF5TUdRellXVXlNalF3TWpreVpqTm1Zdw',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',10,0,1,1,0,1,'default',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161026,999949,0,0,0,'/home','NavigatorGroup4','品质核查','品质核查','cs://1/image/aW1hZ2UvTVRveVlqVmhOV1kxT0RGaE9UWm1PVE5oTURJd09EY3lZbVU1TldJME9UZGpaZw',1,1,44,'{"realm":"quality","entryUrl":"${home.url}/nar/quality/index.html?hideNavigationBar=1#/select_community#sign_suffix"}',20,0,1,1,0,1,'pm_admin',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161027,999949,0,0,0,'/home','NavigatorGroup4','品质核查','品质核查','cs://1/image/aW1hZ2UvTVRveVlqVmhOV1kxT0RGaE9UWm1PVE5oTURJd09EY3lZbVU1TldJME9UZGpaZw',1,1,44,'{"realm":"quality","entryUrl":"${home.url}/nar/quality/index.html?hideNavigationBar=1#/select_community#sign_suffix"}',20,0,1,1,0,1,'default',0,20,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161028,999949,0,0,0,'/home','NavigatorGroup4','能耗管理','能耗管理','cs://1/image/aW1hZ2UvTVRwaU16Rm1NekF5TjJNMFlqTmpNRGsxTVRReFlXSTNNbU5oWkdZNFl6RXpaZw',1,1,13,'{"url":"${home.url}/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}',30,0,1,1,0,1,'pm_admin',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161029,999949,0,0,0,'/home','NavigatorGroup4','能耗管理','能耗管理','cs://1/image/aW1hZ2UvTVRwaU16Rm1NekF5TjJNMFlqTmpNRGsxTVRReFlXSTNNbU5oWkdZNFl6RXpaZw',1,1,13,'{"url":"${home.url}/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}',30,0,1,1,0,1,'default',0,30,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161030,999949,0,0,0,'/home','NavigatorGroup4','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRwa1pUVXdNbVExT1dVek56ZG1ZamRpTnpReFpETXpPV1JtTVdJeU5tVTNPUQ',1,1,56,'',40,0,1,1,0,1,'pm_admin',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161031,999949,0,0,0,'/home','NavigatorGroup4','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRwa1pUVXdNbVExT1dVek56ZG1ZamRpTnpReFpETXpPV1JtTVdJeU5tVTNPUQ',1,1,56,'',40,0,1,1,0,1,'default',0,40,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161032,999949,0,0,0,'/home','NavigatorGroup4','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRwbE1XSmhZVE5tTkdVeFlqaGxZell5WTJObU9UUmpZbUUxT1RjeFpXSm1Zdw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',50,0,1,1,0,1,'pm_admin',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161033,999949,0,0,0,'/home','NavigatorGroup4','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRwbE1XSmhZVE5tTkdVeFlqaGxZell5WTJObU9UUmpZbUUxT1RjeFpXSm1Zdw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',50,0,1,1,0,1,'default',0,50,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161034,999949,0,0,0,'/home','NavigatorGroup4','审批申请','审批申请','cs://1/image/aW1hZ2UvTVRveE9ESTVNemM0TldSa00yRXpZekJqTlRFellXRXhNVFZpTVdRM1pUWXlZdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',60,0,1,1,0,1,'pm_admin',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161035,999949,0,0,0,'/home','NavigatorGroup4','审批申请','审批申请','cs://1/image/aW1hZ2UvTVRveE9ESTVNemM0TldSa00yRXpZekJqTlRFellXRXhNVFZpTVdRM1pUWXlZdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',60,0,1,1,0,1,'default',0,60,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161036,999949,0,0,0,'/home','NavigatorGroup4','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRvM1pqZGxZemMwWXpWa09UUTJNamhtTldGa1lXUmxOVGxqWWpneFpHUmxaUQ',1,1,46,'',70,0,1,1,0,1,'pm_admin',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161037,999949,0,0,0,'/home','NavigatorGroup4','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRvM1pqZGxZemMwWXpWa09UUTJNamhtTldGa1lXUmxOVGxqWWpneFpHUmxaUQ',1,1,46,'',70,0,1,1,0,1,'default',0,70,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161038,999949,0,0,0,'/home','NavigatorGroup4','更多','更多','cs://1/image/aW1hZ2UvTVRvd01USXlOelU0WWpFMFpUWTNZMkk1TkdRME5ESXhOMlprWVdFNVlqSmlZZw',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup4"}',1000,0,1,1,0,1,'pm_admin',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161039,999949,0,0,0,'/home','NavigatorGroup4','更多','更多','cs://1/image/aW1hZ2UvTVRvd01USXlOelU0WWpFMFpUWTNZMkk1TkdRME5ESXhOMlprWVdFNVlqSmlZZw',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup4"}',1000,0,1,1,0,1,'default',0,80,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161040,999949,0,0,0,'/home','NavigatorGroup4','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvNE5HSXpPREZpT0RobFl6Y3pNMlZoTlRobU9HVTRPV0kzTldJME1tVTJPUQ',1,1,45,'',90,0,1,0,0,1,'pm_admin',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161041,999949,0,0,0,'/home','NavigatorGroup4','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvNE5HSXpPREZpT0RobFl6Y3pNMlZoTlRobU9HVTRPV0kzTldJME1tVTJPUQ',1,1,45,'',90,0,1,0,0,1,'default',0,90,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161042,999949,0,0,0,'/home','NavigatorGroup4','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRvMU1qWTNaamc1TWpoaFpEWXlNbVF4TW1RMU1EY3pNMk0xWmpSa05qbG1aUQ',1,1,27,'',100,0,1,0,0,1,'pm_admin',0,100,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161043,999949,0,0,0,'/home','NavigatorGroup4','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRvMU1qWTNaamc1TWpoaFpEWXlNbVF4TW1RMU1EY3pNMk0xWmpSa05qbG1aUQ',1,1,27,'',100,0,1,0,0,1,'default',0,100,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161044,999949,0,0,0,'/home','NavigatorGroup4','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRwaVpXUTJZbVZqWldKaVpEa3hNV0kxWlRGbFlqVmhPV0prTnpneU5qRmtNZw',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',110,0,1,0,0,1,'pm_admin',0,110,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161045,999949,0,0,0,'/home','NavigatorGroup4','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRwaVpXUTJZbVZqWldKaVpEa3hNV0kxWlRGbFlqVmhPV0prTnpneU5qRmtNZw',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',110,0,1,0,0,1,'default',0,110,'');

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (204142,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999949);

set @namespaceId = 999949;
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

