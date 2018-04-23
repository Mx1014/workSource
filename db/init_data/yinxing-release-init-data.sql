INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2072,'app.agreements.url','https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999950','the relative path for 智汇银星 app agreements',999950,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2073,'business.url','https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix','biz access url for 智汇银星',999950,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2074,'pmtask.handler-999950','flow','0',999950,'物业报修工作流');
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (2075,'pay.platform','1','支付类型',999950,NULL);

INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (609,'Android_ZhiHuiYinXing',NULL,UTC_TIMESTAMP(),999950);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (610,'iOS_ZhiHuiYinXing',NULL,UTC_TIMESTAMP(),999950);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (969,609,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999950);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`,`namespace_id`) 
	VALUES (970,610,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP(),999950);

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999950,'智汇银星');

INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1603,999950,'community_mix',UTC_TIMESTAMP());

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) 
	VALUES (1035,'sms.default.sign',0,'zh_CN','智汇银星','【智汇银星】',999950);

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18564,0,'龙华新区','LONGHUAXINQU','LHXQ','/广东/深圳市/龙华新区',3,3,NULL,'0755',2,0,999950);

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060198,UUID(),0,'深圳市',18564,'龙华新区','银星科技大厦','','观澜街道观光路1301号','龙华国家级孵化器基地',0,1,2,UTC_TIMESTAMP(),1,192873,192874,UTC_TIMESTAMP(),999950);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060199,UUID(),0,'深圳市',18564,'龙华新区','银星科技大厦公寓','','观澜街道观光路1301号','银星科技大厦人才配套公寓',0,1,2,UTC_TIMESTAMP(),0,192873,192874,UTC_TIMESTAMP(),999950);
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332060200,UUID(),0,'深圳市',18564,'龙华新区','银星智界','','观澜街道观光路1301号','138项目总建筑面积12万平米，由四栋建筑组成，其中三栋（1-3号楼）作为智能产业研发办公基地，完美满足研发办公需求；一栋为综合配套楼，配置人才公寓，满足入驻企业员工住宿需求，本项目产城围绕企业“创业孵化—成长加速—总部基地”的全周期规划涵盖商务办公、研发中试、生产加工、住宿餐饮、休闲生活等多功能为一体的全生态产城。',0,1,2,UTC_TIMESTAMP(),1,192873,192874,UTC_TIMESTAMP(),999950);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092971,240111044332060198,'',114.05414,22.73466,'ws1176ewypfc');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092972,240111044332060199,'',114.05414,22.73466,'ws1176ewypfc');
INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092973,240111044332060200,'',114.05414,22.73466,'ws1176ewypfc');

INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20308,999950,'COMMUNITY',240111044332060198,UTC_TIMESTAMP());
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20309,999950,'COMMUNITY',240111044332060199,UTC_TIMESTAMP());
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20310,999950,'COMMUNITY',240111044332060200,UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1041514,0,'PM','银星投资集团有限公司','银星集团是一家以实业投资、产业地产开发与运营为主营业务的跨行业综合性高科技企业集团。
集团以深圳为运营总部，业务辐射至北京、上海、武汉、惠州、贵州等地，集团旗下首家公司成立于1993年，目前子公司已发展至16家，拥有员工2000余人，资产规模超100亿元，行业涉及人工智能、信息技术、生物制药、产业地产等领域。','/1041514',1,2,'ENTERPRISE',999950,1059404);

INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1156456,240111044332060198,'organization',1041514,3,0,UTC_TIMESTAMP());

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37680,'EhOrganizations',1041514,1,10,484215,0,1,UTC_TIMESTAMP(),999950,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37681,'EhOrganizations',1041514,1,10,484216,0,1,UTC_TIMESTAMP(),999950,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (37682,'EhOrganizations',1041514,1,10,484217,0,1,UTC_TIMESTAMP(),999950,'EhUsers','admin');

INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1059404,UUID(),'银星投资集团有限公司','银星投资集团有限公司',1,1,1041514,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192872,1,999950);

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192872,UUID(),999950,2,'EhGroups',1059404,'银星投资集团有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192873,UUID(),999950,2,'',0,'智汇银星社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192874,UUID(),999950,2,'',0,'智汇银星意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_forum_categories` (`id`, `uuid`, `namespace_id`, `forum_id`, `entry_id`, `name`, `activity_entry_id`, `create_time`, `update_time`) 
	VALUES (256647,UUID(),999950,192873,0,'默认入口',0,UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (484215,UUID(),'19267011011','王华荣',NULL,1,45,'1','1','zh_CN','5c8a24b222ee2f2abafa5e83e5318317','a69ca61b4074c12125484b5deff4d4f74ce954cb06c6012eb798d4ed62586f5d',UTC_TIMESTAMP(),999950);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (484216,UUID(),'19267011012','张琪',NULL,1,45,'1','2','zh_CN','dd547ae632b5be38cd04acd3f12aab9f','65c0412a5d14403aa9e5301867ac1144a65b9d0a162c7087041267b172d51fb4',UTC_TIMESTAMP(),999950);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (484217,UUID(),'19267011013','欧阳兴祥',NULL,1,45,'1','1','zh_CN','82d4c494a9e3d6e22b11182c6d42056d','19b406fd1e1ca63479a3159be1c9216fa8bc66527b47c1820ff7f5915a43943e',UTC_TIMESTAMP(),999950);

INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32046,1041514,'USER',484215,'王华荣',0,'13922475284',UTC_TIMESTAMP(),999950);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32047,1041514,'USER',484216,'张琪',0,'17688995279',UTC_TIMESTAMP(),999950);
INSERT INTO `eh_organization_member_details` (`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`) 
	VALUES (32048,1041514,'USER',484217,'欧阳兴祥',0,'18503079502',UTC_TIMESTAMP(),999950);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (455221,484215,0,'13922475284',NULL,3,UTC_TIMESTAMP(),999950);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (455222,484216,0,'17688995279',NULL,3,UTC_TIMESTAMP(),999950);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (455223,484217,0,'18503079502',NULL,3,UTC_TIMESTAMP(),999950);

INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184960,1041514,'/1041514','USER',484215,'manager','王华荣',0,'13922475284',3,999950,'ENTERPRISE',0,32046);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184961,1041514,'/1041514','USER',484216,'manager','张琪',0,'17688995279',3,999950,'ENTERPRISE',0,32047);
INSERT INTO `eh_organization_members` (id, organization_id, group_path, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`,`group_type`,`visible_flag`,detail_id) 
	VALUES (2184962,1041514,'/1041514','USER',484217,'manager','欧阳兴祥',0,'18503079502',3,999950,'ENTERPRISE',0,32048);

INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31685,484215,1041514,'/1041514','ENTERPRISE',3,999950,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31686,484216,1041514,'/1041514','ENTERPRISE',3,999950,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`) 
	VALUES (31687,484217,1041514,'/1041514','ENTERPRISE',3,999950,UTC_TIMESTAMP(),0,UTC_TIMESTAMP());

INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115561,'EhOrganizations',1041514,'EhUsers',484215,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115562,'EhOrganizations',1041514,'EhUsers',484216,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (115563,'EhOrganizations',1041514,'EhUsers',484217,1001,1,UTC_TIMESTAMP());

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971548,240111044332060200,'银星智界138项目3号楼','',0,18688726933,'龙华新区观澜街道观光路1301号银星智界',121904.0,'114.05414','22.73466','ws1176ewypfc','138项目总建筑面积12万平米，由四栋建筑组成，其中三栋（1-3号楼）作为智能产业研发办公基地，完美满足研发办公需求；一栋为综合配套楼，配置人才公寓，满足入驻企业员工住宿需求，本项目产城围绕企业“创业孵化—成长加速—总部基地”的全周期规划涵盖商务办公、研发中试、生产加工、住宿餐饮、休闲生活等多功能为一体的全生态产城。


',NULL,2,0,NULL,1,NOW(),999950,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971549,240111044332060200,'银星智界138项目综合楼','',0,18688726933,'龙华新区观澜街道观光路1301号银星智界',121904.0,'114.05414','22.73466','ws1176ewypfc','138项目总建筑面积12万平米，由四栋建筑组成，其中三栋（1-3号楼）作为智能产业研发办公基地，完美满足研发办公需求；一栋为综合配套楼，配置人才公寓，满足入驻企业员工住宿需求，本项目产城围绕企业“创业孵化—成长加速—总部基地”的全周期规划涵盖商务办公、研发中试、生产加工、住宿餐饮、休闲生活等多功能为一体的全生态产城。


',NULL,2,0,NULL,1,NOW(),999950,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971544,240111044332060198,'银星科技大厦','科技大厦',0,13714233339,'龙华新区观澜街道观光路1301号',90000.0,'114.05414','22.73466','ws1176ewypfc','国家级一级孵化器',NULL,2,0,NULL,1,NOW(),999950,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971545,240111044332060199,'银星科技大厦公寓','',0,13714233339,'龙华新区观澜街道观光路1301号',7500.0,'114.05414','22.73466','ws1176ewypfc','科技大厦配套人才公寓',NULL,2,0,NULL,1,NOW(),999950,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971546,240111044332060200,'银星智界138项目1号楼','',0,18688726933,'龙华新区观澜街道观光路1301号银星智界',121904.0,'114.05414','22.73466','ws1176ewypfc','138项目总建筑面积12万平米，由四栋建筑组成，其中三栋（1-3号楼）作为智能产业研发办公基地，完美满足研发办公需求；一栋为综合配套楼，配置人才公寓，满足入驻企业员工住宿需求，本项目产城围绕企业“创业孵化—成长加速—总部基地”的全周期规划涵盖商务办公、研发中试、生产加工、住宿餐饮、休闲生活等多功能为一体的全生态产城。


',NULL,2,0,NULL,1,NOW(),999950,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1971547,240111044332060200,'银星智界138项目2号楼','',0,18688726933,'龙华新区观澜街道观光路1301号银星智界',121904.0,'114.05414','22.73466','ws1176ewypfc','138项目总建筑面积12万平米，由四栋建筑组成，其中三栋（1-3号楼）作为智能产业研发办公基地，完美满足研发办公需求；一栋为综合配套楼，配置人才公寓，满足入驻企业员工住宿需求，本项目产城围绕企业“创业孵化—成长加速—总部基地”的全周期规划涵盖商务办公、研发中试、生产加工、住宿餐饮、休闲生活等多功能为一体的全生态产城。


',NULL,2,0,NULL,1,NOW(),999950,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467172,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-1楼','银星科技大厦','1楼','2','0',UTC_TIMESTAMP(),999950,3655.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467204,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B608','银星科技大厦','B608','2','0',UTC_TIMESTAMP(),999950,164.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467236,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A702-2','银星科技大厦','A702-2','2','0',UTC_TIMESTAMP(),999950,145.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467268,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B711-1','银星科技大厦','B711-1','2','0',UTC_TIMESTAMP(),999950,80.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467300,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A802','银星科技大厦','A802','2','0',UTC_TIMESTAMP(),999950,226.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467332,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D802-3','银星科技大厦','D802-3','2','0',UTC_TIMESTAMP(),999950,173.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467364,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C902','银星科技大厦','C902','2','0',UTC_TIMESTAMP(),999950,240.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467396,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1102','银星科技大厦','A1102','2','0',UTC_TIMESTAMP(),999950,363.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467428,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1106','银星科技大厦','D1106','2','0',UTC_TIMESTAMP(),999950,304.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467460,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B505','银星科技大厦公寓','B505','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467492,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C519','银星科技大厦公寓','C519','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467524,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D514','银星科技大厦公寓','D514','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467556,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第2层','银星智界138项目2号楼','第2层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467588,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第17层','银星智界138项目3号楼','第17层','2','0',UTC_TIMESTAMP(),999950,682.49);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467173,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-2楼','银星科技大厦','2楼','2','0',UTC_TIMESTAMP(),999950,373.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467205,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B609-1','银星科技大厦','B609-1','2','0',UTC_TIMESTAMP(),999950,69.35);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467237,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A703-1','银星科技大厦','A703-1','2','0',UTC_TIMESTAMP(),999950,71.46);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467269,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B712','银星科技大厦','B712','2','0',UTC_TIMESTAMP(),999950,100.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467301,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A803','银星科技大厦','A803','2','0',UTC_TIMESTAMP(),999950,358.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467333,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D804','银星科技大厦','D804','2','0',UTC_TIMESTAMP(),999950,166.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467365,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C902-1','银星科技大厦','C902-1','2','0',UTC_TIMESTAMP(),999950,80.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467397,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1103-1','银星科技大厦','A1103-1','2','0',UTC_TIMESTAMP(),999950,290.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467429,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1107','银星科技大厦','D1107','2','0',UTC_TIMESTAMP(),999950,267.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467461,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B506','银星科技大厦公寓','B506','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467493,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C520','银星科技大厦公寓','C520','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467525,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D515','银星科技大厦公寓','D515','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467557,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第3层','银星智界138项目2号楼','第3层','2','0',UTC_TIMESTAMP(),999950,1635.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467589,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-首层','银星智界138项目综合楼','首层','2','0',UTC_TIMESTAMP(),999950,1708.32);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467174,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-3楼D','银星科技大厦','3楼D','2','0',UTC_TIMESTAMP(),999950,1920.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467206,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B609-2','银星科技大厦','B609-2','2','0',UTC_TIMESTAMP(),999950,69.35);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467238,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A703-2','银星科技大厦','A703-2','2','0',UTC_TIMESTAMP(),999950,26.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467270,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B713','银星科技大厦','B713','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467302,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A804','银星科技大厦','A804','2','0',UTC_TIMESTAMP(),999950,375.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467334,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A901','银星科技大厦','A901','2','0',UTC_TIMESTAMP(),999950,176.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467366,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C902-2','银星科技大厦','C902-2','2','0',UTC_TIMESTAMP(),999950,55.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467398,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1103-2','银星科技大厦','A1103-2','2','0',UTC_TIMESTAMP(),999950,264.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467430,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1108','银星科技大厦','D1108','2','0',UTC_TIMESTAMP(),999950,45.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467462,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B507','银星科技大厦公寓','B507','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467494,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C521','银星科技大厦公寓','C521','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467526,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D516','银星科技大厦公寓','D516','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467558,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第4层','银星智界138项目2号楼','第4层','2','0',UTC_TIMESTAMP(),999950,1635.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467590,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第2层','银星智界138项目综合楼','第2层','2','0',UTC_TIMESTAMP(),999950,1665.16);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467175,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-3楼ABC','银星科技大厦','3楼ABC','2','0',UTC_TIMESTAMP(),999950,7080.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467207,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B609-3','银星科技大厦','B609-3','2','0',UTC_TIMESTAMP(),999950,69.35);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467239,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A703-3','银星科技大厦','A703-3','2','0',UTC_TIMESTAMP(),999950,53.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467271,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B714','银星科技大厦','B714','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467303,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A805','银星科技大厦','A805','2','0',UTC_TIMESTAMP(),999950,370.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467335,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A902','银星科技大厦','A902','2','0',UTC_TIMESTAMP(),999950,256.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467367,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C902-3','银星科技大厦','C902-3','2','0',UTC_TIMESTAMP(),999950,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467399,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1104','银星科技大厦','A1104','2','0',UTC_TIMESTAMP(),999950,183.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467431,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1109','银星科技大厦','D1109','2','0',UTC_TIMESTAMP(),999950,161.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467463,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B508','银星科技大厦公寓','B508','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467495,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C522','银星科技大厦公寓','C522','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467527,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D517','银星科技大厦公寓','D517','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467559,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第5层','银星智界138项目2号楼','第5层','2','0',UTC_TIMESTAMP(),999950,1635.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467591,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第3层','银星智界138项目综合楼','第3层','2','0',UTC_TIMESTAMP(),999950,1665.16);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467176,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-4楼B区','银星科技大厦','4楼B区','2','0',UTC_TIMESTAMP(),999950,3665.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467208,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B609-4','银星科技大厦','B609-4','2','0',UTC_TIMESTAMP(),999950,69.35);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467240,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A703-4','银星科技大厦','A703-4','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467272,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B715','银星科技大厦','B715','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467304,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A806','银星科技大厦','A806','2','0',UTC_TIMESTAMP(),999950,21.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467336,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A903','银星科技大厦','A903','2','0',UTC_TIMESTAMP(),999950,124.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467368,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C902-4','银星科技大厦','C902-4','2','0',UTC_TIMESTAMP(),999950,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467400,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1105','银星科技大厦','A1105','2','0',UTC_TIMESTAMP(),999950,140.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467432,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A501','银星科技大厦公寓','A501','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467464,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B509','银星科技大厦公寓','B509','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467496,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C523','银星科技大厦公寓','C523','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467528,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D518','银星科技大厦公寓','D518','2','0',UTC_TIMESTAMP(),999950,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467560,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第6层','银星智界138项目2号楼','第6层','2','0',UTC_TIMESTAMP(),999950,1635.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467592,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第4层','银星智界138项目综合楼','第4层','2','0',UTC_TIMESTAMP(),999950,1610.738);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467177,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-4楼C区','银星科技大厦','4楼C区','2','0',UTC_TIMESTAMP(),999950,3669.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467209,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C601','银星科技大厦','C601','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467241,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A704-1','银星科技大厦','A704-1','2','0',UTC_TIMESTAMP(),999950,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467273,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B716','银星科技大厦','B716','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467305,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A806-1','银星科技大厦','A806-1','2','0',UTC_TIMESTAMP(),999950,48.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467337,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A90e','银星科技大厦','A90e','2','0',UTC_TIMESTAMP(),999950,286.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467369,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C903-1','银星科技大厦','C903-1','2','0',UTC_TIMESTAMP(),999950,131.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467401,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1106','银星科技大厦','A1106','2','0',UTC_TIMESTAMP(),999950,41.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467433,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A502','银星科技大厦公寓','A502','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467465,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B510','银星科技大厦公寓','B510','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467497,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C524','银星科技大厦公寓','C524','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467529,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D519','银星科技大厦公寓','D519','2','0',UTC_TIMESTAMP(),999950,80.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467561,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第7层','银星智界138项目2号楼','第7层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467593,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第5层','银星智界138项目综合楼','第5层','2','0',UTC_TIMESTAMP(),999950,1610.738);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467178,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A601','银星科技大厦','A601','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467210,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C601-1','银星科技大厦','C601-1','2','0',UTC_TIMESTAMP(),999950,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467242,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A704-2','银星科技大厦','A704-2','2','0',UTC_TIMESTAMP(),999950,57.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467274,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C702','银星科技大厦','C702','2','0',UTC_TIMESTAMP(),999950,332.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467306,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A807','银星科技大厦','A807','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467338,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A905','银星科技大厦','A905','2','0',UTC_TIMESTAMP(),999950,230.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467370,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C903-2','银星科技大厦','C903-2','2','0',UTC_TIMESTAMP(),999950,242.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467402,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1108','银星科技大厦','A1108','2','0',UTC_TIMESTAMP(),999950,214.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467434,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A503','银星科技大厦公寓','A503','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467466,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B511','银星科技大厦公寓','B511','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467498,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C525','银星科技大厦公寓','C525','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467530,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D520','银星科技大厦公寓','D520','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467562,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第8层','银星智界138项目2号楼','第8层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467594,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第6层','银星智界138项目综合楼','第6层','2','0',UTC_TIMESTAMP(),999950,1610.738);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467179,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A602','银星科技大厦','A602','2','0',UTC_TIMESTAMP(),999950,95.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467211,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C602-1','银星科技大厦','C602-1','2','0',UTC_TIMESTAMP(),999950,356.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467243,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A705','银星科技大厦','A705','2','0',UTC_TIMESTAMP(),999950,78.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467275,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C703','银星科技大厦','C703','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467307,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A809','银星科技大厦','A809','2','0',UTC_TIMESTAMP(),999950,21.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467339,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A906','银星科技大厦','A906','2','0',UTC_TIMESTAMP(),999950,183.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467371,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C905','银星科技大厦','C905','2','0',UTC_TIMESTAMP(),999950,130.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467403,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1109','银星科技大厦','A1109','2','0',UTC_TIMESTAMP(),999950,20.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467435,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A504','银星科技大厦公寓','A504','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467467,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B512','银星科技大厦公寓','B512','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467499,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C526','银星科技大厦公寓','C526','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467531,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D521','银星科技大厦公寓','D521','2','0',UTC_TIMESTAMP(),999950,80.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467563,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第9层','银星智界138项目2号楼','第9层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467595,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第7层','银星智界138项目综合楼','第7层','2','0',UTC_TIMESTAMP(),999950,1610.738);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467180,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A603-1','银星科技大厦','A603-1','2','0',UTC_TIMESTAMP(),999950,119.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467212,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C603-1','银星科技大厦','C603-1','2','0',UTC_TIMESTAMP(),999950,71.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467244,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A706','银星科技大厦','A706','2','0',UTC_TIMESTAMP(),999950,103.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467276,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C701','银星科技大厦','C701','2','0',UTC_TIMESTAMP(),999950,114.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467308,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A810','银星科技大厦','A810','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467340,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A907','银星科技大厦','A907','2','0',UTC_TIMESTAMP(),999950,24.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467372,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C906','银星科技大厦','C906','2','0',UTC_TIMESTAMP(),999950,139.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467404,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1109-1','银星科技大厦','A1109-1','2','0',UTC_TIMESTAMP(),999950,19.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467436,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A505','银星科技大厦公寓','A505','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467468,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B513','银星科技大厦公寓','B513','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467500,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C527','银星科技大厦公寓','C527','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467532,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D522','银星科技大厦公寓','D522','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467564,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第10层','银星智界138项目2号楼','第10层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467596,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第8层','银星智界138项目综合楼','第8层','2','0',UTC_TIMESTAMP(),999950,1610.738);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467181,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A603-2','银星科技大厦','A603-2','2','0',UTC_TIMESTAMP(),999950,119.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467213,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C603','银星科技大厦','C603','2','0',UTC_TIMESTAMP(),999950,51.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467245,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A707','银星科技大厦','A707','2','0',UTC_TIMESTAMP(),999950,92.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467277,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C704','银星科技大厦','C704','2','0',UTC_TIMESTAMP(),999950,370.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467309,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A810-1','银星科技大厦','A810-1','2','0',UTC_TIMESTAMP(),999950,75.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467341,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A908','银星科技大厦','A908','2','0',UTC_TIMESTAMP(),999950,69.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467373,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C907','银星科技大厦','C907','2','0',UTC_TIMESTAMP(),999950,121.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467405,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1110','银星科技大厦','A1110','2','0',UTC_TIMESTAMP(),999950,103.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467437,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A506','银星科技大厦公寓','A506','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467469,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B514','银星科技大厦公寓','B514','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467501,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C528','银星科技大厦公寓','C528','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467533,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D523','银星科技大厦公寓','D523','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467565,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第11层','银星智界138项目2号楼','第11层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467597,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第9层','银星智界138项目综合楼','第9层','2','0',UTC_TIMESTAMP(),999950,1804.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467182,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A604','银星科技大厦','A604','2','0',UTC_TIMESTAMP(),999950,122.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467214,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C603-2','银星科技大厦','C603-2','2','0',UTC_TIMESTAMP(),999950,75.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467246,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A708','银星科技大厦','A708','2','0',UTC_TIMESTAMP(),999950,80.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467278,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C705','银星科技大厦','C705','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467310,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A811','银星科技大厦','A811','2','0',UTC_TIMESTAMP(),999950,162.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467342,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A909','银星科技大厦','A909','2','0',UTC_TIMESTAMP(),999950,74.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467374,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D901','银星科技大厦','D901','2','0',UTC_TIMESTAMP(),999950,118.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467406,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1111','银星科技大厦','A1111','2','0',UTC_TIMESTAMP(),999950,154.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467438,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A507','银星科技大厦公寓','A507','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467470,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B515','银星科技大厦公寓','B515','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467502,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C529','银星科技大厦公寓','C529','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467534,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D524','银星科技大厦公寓','D524','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467566,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第12层','银星智界138项目2号楼','第12层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467598,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第10层','银星智界138项目综合楼','第10层','2','0',UTC_TIMESTAMP(),999950,1804.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467183,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A605-2','银星科技大厦','A605-2','2','0',UTC_TIMESTAMP(),999950,68.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467215,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C604','银星科技大厦','C604','2','0',UTC_TIMESTAMP(),999950,185.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467247,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A709','银星科技大厦','A709','2','0',UTC_TIMESTAMP(),999950,82.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467279,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C706','银星科技大厦','C706','2','0',UTC_TIMESTAMP(),999950,128.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467311,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B808','银星科技大厦','B808','2','0',UTC_TIMESTAMP(),999950,166.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467343,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A910','银星科技大厦','A910','2','0',UTC_TIMESTAMP(),999950,57.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467375,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D902','银星科技大厦','D902','2','0',UTC_TIMESTAMP(),999950,183.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467407,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1112','银星科技大厦','A1112','2','0',UTC_TIMESTAMP(),999950,164.53);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467439,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A508','银星科技大厦公寓','A508','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467471,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B516','银星科技大厦公寓','B516','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467503,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C530','银星科技大厦公寓','C530','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467535,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D525','银星科技大厦公寓','D525','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467567,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第13层','银星智界138项目2号楼','第13层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467599,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第11层','银星智界138项目综合楼','第11层','2','0',UTC_TIMESTAMP(),999950,1503.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467184,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A605','银星科技大厦','A605','2','0',UTC_TIMESTAMP(),999950,200.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467216,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C605','银星科技大厦','C605','2','0',UTC_TIMESTAMP(),999950,131.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467248,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A710','银星科技大厦','A710','2','0',UTC_TIMESTAMP(),999950,82.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467280,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C707','银星科技大厦','C707','2','0',UTC_TIMESTAMP(),999950,311.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467312,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B809','银星科技大厦','B809','2','0',UTC_TIMESTAMP(),999950,299.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467344,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A911','银星科技大厦','A911','2','0',UTC_TIMESTAMP(),999950,128.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467376,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D903','银星科技大厦','D903','2','0',UTC_TIMESTAMP(),999950,136.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467408,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B1101','银星科技大厦','B1101','2','0',UTC_TIMESTAMP(),999950,334.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467440,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A509','银星科技大厦公寓','A509','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467472,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B517','银星科技大厦公寓','B517','2','0',UTC_TIMESTAMP(),999950,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467504,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C531','银星科技大厦公寓','C531','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467536,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D526','银星科技大厦公寓','D526','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467568,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第14层','银星智界138项目2号楼','第14层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467600,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第12层','银星智界138项目综合楼','第12层','2','0',UTC_TIMESTAMP(),999950,1503.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467185,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A605-1','银星科技大厦','A605-1','2','0',UTC_TIMESTAMP(),999950,76.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467217,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C606','银星科技大厦','C606','2','0',UTC_TIMESTAMP(),999950,190.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467249,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A711','银星科技大厦','A711','2','0',UTC_TIMESTAMP(),999950,19.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467281,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C708','银星科技大厦','C708','2','0',UTC_TIMESTAMP(),999950,117.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467313,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B区','银星科技大厦','B区','2','0',UTC_TIMESTAMP(),999950,684.42);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467345,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-图书室','银星科技大厦','图书室','2','0',UTC_TIMESTAMP(),999950,78.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467377,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D90e','银星科技大厦','D90e','2','0',UTC_TIMESTAMP(),999950,20.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467409,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B1102','银星科技大厦','B1102','2','0',UTC_TIMESTAMP(),999950,42.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467441,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A510','银星科技大厦公寓','A510','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467473,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B518','银星科技大厦公寓','B518','2','0',UTC_TIMESTAMP(),999950,80.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467505,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C532','银星科技大厦公寓','C532','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467537,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D527','银星科技大厦公寓','D527','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467569,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第15层','银星智界138项目2号楼','第15层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467601,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第13层','银星智界138项目综合楼','第13层','2','0',UTC_TIMESTAMP(),999950,1503.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467186,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A606','银星科技大厦','A606','2','0',UTC_TIMESTAMP(),999950,97.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467218,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C607','银星科技大厦','C607','2','0',UTC_TIMESTAMP(),999950,190.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467250,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A712','银星科技大厦','A712','2','0',UTC_TIMESTAMP(),999950,123.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467282,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C709','银星科技大厦','C709','2','0',UTC_TIMESTAMP(),999950,140.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467314,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B807','银星科技大厦','B807','2','0',UTC_TIMESTAMP(),999950,185.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467346,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-培训室','银星科技大厦','培训室','2','0',UTC_TIMESTAMP(),999950,121.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467378,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D90e-1','银星科技大厦','D90e-1','2','0',UTC_TIMESTAMP(),999950,71.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467410,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B1102-1','银星科技大厦','B1102-1','2','0',UTC_TIMESTAMP(),999950,42.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467442,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A511','银星科技大厦公寓','A511','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467474,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C501','银星科技大厦公寓','C501','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467506,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C533','银星科技大厦公寓','C533','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467538,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-首层','银星智界138项目1号楼','首层','2','0',UTC_TIMESTAMP(),999950,1415.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467570,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第16层','银星智界138项目2号楼','第16层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467602,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第14层','银星智界138项目综合楼','第14层','2','0',UTC_TIMESTAMP(),999950,1503.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467187,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A607','银星科技大厦','A607','2','0',UTC_TIMESTAMP(),999950,69.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467219,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C608','银星科技大厦','C608','2','0',UTC_TIMESTAMP(),999950,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467251,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A713','银星科技大厦','A713','2','0',UTC_TIMESTAMP(),999950,41.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467283,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C710','银星科技大厦','C710','2','0',UTC_TIMESTAMP(),999950,111.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467315,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C802','银星科技大厦','C802','2','0',UTC_TIMESTAMP(),999950,172.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467347,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-大会议室','银星科技大厦','大会议室','2','0',UTC_TIMESTAMP(),999950,142.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467379,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D905','银星科技大厦','D905','2','0',UTC_TIMESTAMP(),999950,92.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467411,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B1103','银星科技大厦','B1103','2','0',UTC_TIMESTAMP(),999950,464.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467443,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A512','银星科技大厦公寓','A512','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467475,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C502','银星科技大厦公寓','C502','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467507,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C534','银星科技大厦公寓','C534','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467539,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第2层','银星智界138项目1号楼','第2层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467571,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-第17层','银星智界138项目2号楼','第17层','2','0',UTC_TIMESTAMP(),999950,1622.39);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467603,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第15层','银星智界138项目综合楼','第15层','2','0',UTC_TIMESTAMP(),999950,1503.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467188,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A608','银星科技大厦','A608','2','0',UTC_TIMESTAMP(),999950,108.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467220,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C608-1','银星科技大厦','C608-1','2','0',UTC_TIMESTAMP(),999950,38.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467252,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A714','银星科技大厦','A714','2','0',UTC_TIMESTAMP(),999950,113.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467284,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D701','银星科技大厦','D701','2','0',UTC_TIMESTAMP(),999950,88.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467316,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C803','银星科技大厦','C803','2','0',UTC_TIMESTAMP(),999950,483.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467348,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-小会议室','银星科技大厦','小会议室','2','0',UTC_TIMESTAMP(),999950,107.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467380,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D906','银星科技大厦','D906','2','0',UTC_TIMESTAMP(),999950,292.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467412,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B1104','银星科技大厦','B1104','2','0',UTC_TIMESTAMP(),999950,510.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467444,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A513','银星科技大厦公寓','A513','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467476,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C503','银星科技大厦公寓','C503','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467508,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C535','银星科技大厦公寓','C535','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467540,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第3层','银星智界138项目1号楼','第3层','2','0',UTC_TIMESTAMP(),999950,1666.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467572,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-首层','银星智界138项目3号楼','首层','2','0',UTC_TIMESTAMP(),999950,810.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467604,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第16层','银星智界138项目综合楼','第16层','2','0',UTC_TIMESTAMP(),999950,1503.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467189,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A608-1','银星科技大厦','A608-1','2','0',UTC_TIMESTAMP(),999950,56.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467221,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C609','银星科技大厦','C609','2','0',UTC_TIMESTAMP(),999950,244.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467253,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A715','银星科技大厦','A715','2','0',UTC_TIMESTAMP(),999950,127.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467285,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D702','银星科技大厦','D702','2','0',UTC_TIMESTAMP(),999950,107.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467317,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C801','银星科技大厦','C801','2','0',UTC_TIMESTAMP(),999950,193.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467349,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A913','银星科技大厦','A913','2','0',UTC_TIMESTAMP(),999950,56.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467381,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D907','银星科技大厦','D907','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467413,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B1105','银星科技大厦','B1105','2','0',UTC_TIMESTAMP(),999950,293.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467445,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A514','银星科技大厦公寓','A514','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467477,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C504','银星科技大厦公寓','C504','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467509,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C536','银星科技大厦公寓','C536','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467541,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第4层','银星智界138项目1号楼','第4层','2','0',UTC_TIMESTAMP(),999950,1666.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467573,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第2层','银星智界138项目3号楼','第2层','2','0',UTC_TIMESTAMP(),999950,810.13);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467605,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第17层','银星智界138项目综合楼','第17层','2','0',UTC_TIMESTAMP(),999950,1503.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467190,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A609','银星科技大厦','A609','2','0',UTC_TIMESTAMP(),999950,150.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467222,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D601','银星科技大厦','D601','2','0',UTC_TIMESTAMP(),999950,114.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467254,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A716','银星科技大厦','A716','2','0',UTC_TIMESTAMP(),999950,121.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467286,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D703','银星科技大厦','D703','2','0',UTC_TIMESTAMP(),999950,74.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467318,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C806','银星科技大厦','C806','2','0',UTC_TIMESTAMP(),999950,119.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467350,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A91e','银星科技大厦','A91e','2','0',UTC_TIMESTAMP(),999950,24.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467382,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D908','银星科技大厦','D908','2','0',UTC_TIMESTAMP(),999950,233.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467414,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1101','银星科技大厦','C1101','2','0',UTC_TIMESTAMP(),999950,112.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467446,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A515','银星科技大厦公寓','A515','2','0',UTC_TIMESTAMP(),999950,80.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467478,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C505','银星科技大厦公寓','C505','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467510,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C537','银星科技大厦公寓','C537','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467542,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第5层','银星智界138项目1号楼','第5层','2','0',UTC_TIMESTAMP(),999950,1415.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467574,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第3层','银星智界138项目3号楼','第3层','2','0',UTC_TIMESTAMP(),999950,810.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467606,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第18层','银星智界138项目综合楼','第18层','2','0',UTC_TIMESTAMP(),999950,1503.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467191,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A610','银星科技大厦','A610','2','0',UTC_TIMESTAMP(),999950,41.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467223,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D602-1','银星科技大厦','D602-1','2','0',UTC_TIMESTAMP(),999950,68.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467255,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A717','银星科技大厦','A717','2','0',UTC_TIMESTAMP(),999950,47.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467287,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D704','银星科技大厦','D704','2','0',UTC_TIMESTAMP(),999950,132.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467319,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C807','银星科技大厦','C807','2','0',UTC_TIMESTAMP(),999950,98.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467351,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B901','银星科技大厦','B901','2','0',UTC_TIMESTAMP(),999950,170.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467383,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D910','银星科技大厦','D910','2','0',UTC_TIMESTAMP(),999950,218.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467415,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1102-1','银星科技大厦','C1102-1','2','0',UTC_TIMESTAMP(),999950,120.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467447,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A516','银星科技大厦公寓','A516','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467479,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C506','银星科技大厦公寓','C506','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467511,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D501','银星科技大厦公寓','D501','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467543,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第6层','银星智界138项目1号楼','第6层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467575,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第4层','银星智界138项目3号楼','第4层','2','0',UTC_TIMESTAMP(),999950,810.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467607,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目综合楼-第19层','银星智界138项目综合楼','第19层','2','0',UTC_TIMESTAMP(),999950,1119.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467192,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A611','银星科技大厦','A611','2','0',UTC_TIMESTAMP(),999950,128.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467224,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D602','银星科技大厦','D602','2','0',UTC_TIMESTAMP(),999950,220.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467256,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B701','银星科技大厦','B701','2','0',UTC_TIMESTAMP(),999950,95.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467288,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D705','银星科技大厦','D705','2','0',UTC_TIMESTAMP(),999950,90.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467320,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C807-1','银星科技大厦','C807-1','2','0',UTC_TIMESTAMP(),999950,30.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467352,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B901-1','银星科技大厦','B901-1','2','0',UTC_TIMESTAMP(),999950,168.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467384,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D909','银星科技大厦','D909','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467416,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1102-2','银星科技大厦','C1102-2','2','0',UTC_TIMESTAMP(),999950,190.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467448,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A517','银星科技大厦公寓','A517','2','0',UTC_TIMESTAMP(),999950,80.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467480,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C507','银星科技大厦公寓','C507','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467512,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D502','银星科技大厦公寓','D502','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467544,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第7层','银星智界138项目1号楼','第7层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467576,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第5层','银星智界138项目3号楼','第5层','2','0',UTC_TIMESTAMP(),999950,810.16);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467193,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A612','银星科技大厦','A612','2','0',UTC_TIMESTAMP(),999950,118.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467225,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D603','银星科技大厦','D603','2','0',UTC_TIMESTAMP(),999950,133.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467257,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B702','银星科技大厦','B702','2','0',UTC_TIMESTAMP(),999950,117.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467289,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D706','银星科技大厦','D706','2','0',UTC_TIMESTAMP(),999950,125.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467321,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D800','银星科技大厦','D800','2','0',UTC_TIMESTAMP(),999950,204.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467353,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B902','银星科技大厦','B902','2','0',UTC_TIMESTAMP(),999950,128.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467385,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D911','银星科技大厦','D911','2','0',UTC_TIMESTAMP(),999950,181.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467417,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1103-1','银星科技大厦','C1103-1','2','0',UTC_TIMESTAMP(),999950,225.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467449,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A518','银星科技大厦公寓','A518','2','0',UTC_TIMESTAMP(),999950,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467481,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C508','银星科技大厦公寓','C508','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467513,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D503','银星科技大厦公寓','D503','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467545,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第8层','银星智界138项目1号楼','第8层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467577,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第6层','银星智界138项目3号楼','第6层','2','0',UTC_TIMESTAMP(),999950,810.17);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467194,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A613','银星科技大厦','A613','2','0',UTC_TIMESTAMP(),999950,40.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467226,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D604','银星科技大厦','D604','2','0',UTC_TIMESTAMP(),999950,70.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467258,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B702-1','银星科技大厦','B702-1','2','0',UTC_TIMESTAMP(),999950,105.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467290,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D707','银星科技大厦','D707','2','0',UTC_TIMESTAMP(),999950,115.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467322,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C808','银星科技大厦','C808','2','0',UTC_TIMESTAMP(),999950,105.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467354,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B903','银星科技大厦','B903','2','0',UTC_TIMESTAMP(),999950,157.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467386,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D912','银星科技大厦','D912','2','0',UTC_TIMESTAMP(),999950,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467418,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1104','银星科技大厦','C1104','2','0',UTC_TIMESTAMP(),999950,112.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467450,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A519','银星科技大厦公寓','A519','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467482,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C509','银星科技大厦公寓','C509','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467514,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D504','银星科技大厦公寓','D504','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467546,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第9层','银星智界138项目1号楼','第9层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467578,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第7层','银星智界138项目3号楼','第7层','2','0',UTC_TIMESTAMP(),999950,810.18);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467195,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B601-1','银星科技大厦','B601-1','2','0',UTC_TIMESTAMP(),999950,163.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467227,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D605-1','银星科技大厦','D605-1','2','0',UTC_TIMESTAMP(),999950,165.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467259,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B703','银星科技大厦','B703','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467291,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D708','银星科技大厦','D708','2','0',UTC_TIMESTAMP(),999950,121.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467323,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C810','银星科技大厦','C810','2','0',UTC_TIMESTAMP(),999950,222.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467355,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B90e','银星科技大厦','B90e','2','0',UTC_TIMESTAMP(),999950,165.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467387,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A区','银星科技大厦','A区','2','0',UTC_TIMESTAMP(),999950,3024.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467419,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1105','银星科技大厦','C1105','2','0',UTC_TIMESTAMP(),999950,385.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467451,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A520','银星科技大厦公寓','A520','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467483,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C510','银星科技大厦公寓','C510','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467515,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D505','银星科技大厦公寓','D505','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467547,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第10层','银星智界138项目1号楼','第10层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467579,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第8层','银星智界138项目3号楼','第8层','2','0',UTC_TIMESTAMP(),999950,810.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467196,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B601-2','银星科技大厦','B601-2','2','0',UTC_TIMESTAMP(),999950,163.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467228,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D605-2','银星科技大厦','D605-2','2','0',UTC_TIMESTAMP(),999950,178.98);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467260,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B704','银星科技大厦','B704','2','0',UTC_TIMESTAMP(),999950,84.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467292,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D709','银星科技大厦','D709','2','0',UTC_TIMESTAMP(),999950,140.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467324,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C811','银星科技大厦','C811','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467356,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B905','银星科技大厦','B905','2','0',UTC_TIMESTAMP(),999950,120.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467388,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B1001','银星科技大厦','B1001','2','0',UTC_TIMESTAMP(),999950,292.68);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467420,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1106','银星科技大厦','C1106','2','0',UTC_TIMESTAMP(),999950,190.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467452,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A521','银星科技大厦公寓','A521','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467484,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C511','银星科技大厦公寓','C511','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467516,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D506','银星科技大厦公寓','D506','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467548,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第11层','银星智界138项目1号楼','第11层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467580,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第9层','银星智界138项目3号楼','第9层','2','0',UTC_TIMESTAMP(),999950,810.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467197,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B602','银星科技大厦','B602','2','0',UTC_TIMESTAMP(),999950,87.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467229,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D605-3','银星科技大厦','D605-3','2','0',UTC_TIMESTAMP(),999950,63.02);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467261,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B705','银星科技大厦','B705','2','0',UTC_TIMESTAMP(),999950,197.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467293,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D710','银星科技大厦','D710','2','0',UTC_TIMESTAMP(),999950,118.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467325,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D801','银星科技大厦','D801','2','0',UTC_TIMESTAMP(),999950,383.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467357,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B906','银星科技大厦','B906','2','0',UTC_TIMESTAMP(),999950,261.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467389,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-10楼B区','银星科技大厦','10楼B区','2','0',UTC_TIMESTAMP(),999950,800.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467421,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1106-1','银星科技大厦','C1106-1','2','0',UTC_TIMESTAMP(),999950,123.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467453,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A522','银星科技大厦公寓','A522','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467485,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C512','银星科技大厦公寓','C512','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467517,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D507','银星科技大厦公寓','D507','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467549,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第12层','银星智界138项目1号楼','第12层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467581,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第10层','银星智界138项目3号楼','第10层','2','0',UTC_TIMESTAMP(),999950,810.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467198,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B603','银星科技大厦','B603','2','0',UTC_TIMESTAMP(),999950,210.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467230,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D606','银星科技大厦','D606','2','0',UTC_TIMESTAMP(),999950,188.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467262,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B706','银星科技大厦','B706','2','0',UTC_TIMESTAMP(),999950,800.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467294,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D711','银星科技大厦','D711','2','0',UTC_TIMESTAMP(),999950,135.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467326,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C805','银星科技大厦','C805','2','0',UTC_TIMESTAMP(),999950,125.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467358,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B907-1','银星科技大厦','B907-1','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467390,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1001','银星科技大厦','C1001','2','0',UTC_TIMESTAMP(),999950,374.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467422,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1106-2','银星科技大厦','C1106-2','2','0',UTC_TIMESTAMP(),999950,89.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467454,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A523','银星科技大厦公寓','A523','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467486,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C513','银星科技大厦公寓','C513','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467518,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D508','银星科技大厦公寓','D508','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467550,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第13层','银星智界138项目1号楼','第13层','2','0',UTC_TIMESTAMP(),999950,1433.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467582,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第11层','银星智界138项目3号楼','第11层','2','0',UTC_TIMESTAMP(),999950,810.22);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467199,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B604-1','银星科技大厦','B604-1','2','0',UTC_TIMESTAMP(),999950,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467231,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D607','银星科技大厦','D607','2','0',UTC_TIMESTAMP(),999950,244.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467263,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B707','银星科技大厦','B707','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467295,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D712','银星科技大厦','D712','2','0',UTC_TIMESTAMP(),999950,135.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467327,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C809','银星科技大厦','C809','2','0',UTC_TIMESTAMP(),999950,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467359,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B908','银星科技大厦','B908','2','0',UTC_TIMESTAMP(),999950,140.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467391,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1002','银星科技大厦','C1002','2','0',UTC_TIMESTAMP(),999950,100.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467423,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1101','银星科技大厦','D1101','2','0',UTC_TIMESTAMP(),999950,106.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467455,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-A524','银星科技大厦公寓','A524','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467487,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C514','银星科技大厦公寓','C514','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467519,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D509','银星科技大厦公寓','D509','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467551,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第14层','银星智界138项目1号楼','第14层','2','0',UTC_TIMESTAMP(),999950,1433.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467583,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第12层','银星智界138项目3号楼','第12层','2','0',UTC_TIMESTAMP(),999950,810.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467200,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B604-2','银星科技大厦','B604-2','2','0',UTC_TIMESTAMP(),999950,88.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467232,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D608','银星科技大厦','D608','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467264,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B708','银星科技大厦','B708','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467296,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D713','银星科技大厦','D713','2','0',UTC_TIMESTAMP(),999950,145.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467328,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C809-1','银星科技大厦','C809-1','2','0',UTC_TIMESTAMP(),999950,60.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467360,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B908-1','银星科技大厦','B908-1','2','0',UTC_TIMESTAMP(),999950,22.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467392,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1003','银星科技大厦','C1003','2','0',UTC_TIMESTAMP(),999950,400.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467424,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1102','银星科技大厦','D1102','2','0',UTC_TIMESTAMP(),999950,190.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467456,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B501','银星科技大厦公寓','B501','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467488,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C515','银星科技大厦公寓','C515','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467520,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D510','银星科技大厦公寓','D510','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467552,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第15层','银星智界138项目1号楼','第15层','2','0',UTC_TIMESTAMP(),999950,1433.08);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467584,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第13层','银星智界138项目3号楼','第13层','2','0',UTC_TIMESTAMP(),999950,810.24);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467201,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B605','银星科技大厦','B605','2','0',UTC_TIMESTAMP(),999950,121.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467233,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D609','银星科技大厦','D609','2','0',UTC_TIMESTAMP(),999950,104.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467265,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B709','银星科技大厦','B709','2','0',UTC_TIMESTAMP(),999950,NULL);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467297,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D71E','银星科技大厦','D71E','2','0',UTC_TIMESTAMP(),999950,23.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467329,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D802-1','银星科技大厦','D802-1','2','0',UTC_TIMESTAMP(),999950,305.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467361,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B909','银星科技大厦','B909','2','0',UTC_TIMESTAMP(),999950,313.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467393,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C1004','银星科技大厦','C1004','2','0',UTC_TIMESTAMP(),999950,500.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467425,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1103','银星科技大厦','D1103','2','0',UTC_TIMESTAMP(),999950,139.7);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467457,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B502','银星科技大厦公寓','B502','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467489,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C516','银星科技大厦公寓','C516','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467521,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D511','银星科技大厦公寓','D511','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467553,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第16层','银星智界138项目1号楼','第16层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467585,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第14层','银星智界138项目3号楼','第14层','2','0',UTC_TIMESTAMP(),999950,810.25);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467202,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B606','银星科技大厦','B606','2','0',UTC_TIMESTAMP(),999950,175.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467234,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A701','银星科技大厦','A701','2','0',UTC_TIMESTAMP(),999950,82.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467266,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B710','银星科技大厦','B710','2','0',UTC_TIMESTAMP(),999950,106.2);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467298,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A801-1','银星科技大厦','A801-1','2','0',UTC_TIMESTAMP(),999950,112.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467330,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D802-2','银星科技大厦','D802-2','2','0',UTC_TIMESTAMP(),999950,10.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467362,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C901','银星科技大厦','C901','2','0',UTC_TIMESTAMP(),999950,287.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467394,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D区','银星科技大厦','D区','2','0',UTC_TIMESTAMP(),999950,1380.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467426,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1104','银星科技大厦','D1104','2','0',UTC_TIMESTAMP(),999950,175.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467458,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B503','银星科技大厦公寓','B503','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467490,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C517','银星科技大厦公寓','C517','2','0',UTC_TIMESTAMP(),999950,65.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467522,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D512','银星科技大厦公寓','D512','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467554,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目1号楼-第17层','银星智界138项目1号楼','第17层','2','0',UTC_TIMESTAMP(),999950,1415.6);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467586,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第15层','银星智界138项目3号楼','第15层','2','0',UTC_TIMESTAMP(),999950,810.26);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467203,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B607','银星科技大厦','B607','2','0',UTC_TIMESTAMP(),999950,174.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467235,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A702-1','银星科技大厦','A702-1','2','0',UTC_TIMESTAMP(),999950,78.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467267,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-B711','银星科技大厦','B711','2','0',UTC_TIMESTAMP(),999950,44.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467299,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A801-2','银星科技大厦','A801-2','2','0',UTC_TIMESTAMP(),999950,115.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467331,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D802','银星科技大厦','D802','2','0',UTC_TIMESTAMP(),999950,160.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467363,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-C901-1','银星科技大厦','C901-1','2','0',UTC_TIMESTAMP(),999950,214.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467395,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-A1101','银星科技大厦','A1101','2','0',UTC_TIMESTAMP(),999950,180.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467427,UUID(),240111044332060198,0,'深圳市',18564,'龙华新区','银星科技大厦-D1105','银星科技大厦','D1105','2','0',UTC_TIMESTAMP(),999950,175.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467459,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-B504','银星科技大厦公寓','B504','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467491,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-C518','银星科技大厦公寓','C518','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467523,UUID(),240111044332060199,0,'深圳市',18564,'龙华新区','银星科技大厦公寓-D513','银星科技大厦公寓','D513','2','0',UTC_TIMESTAMP(),999950,50.0);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467555,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目2号楼-首层','银星智界138项目2号楼','首层','2','0',UTC_TIMESTAMP(),999950,1622.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387467587,UUID(),240111044332060200,0,'深圳市',18564,'龙华新区','银星智界138项目3号楼-第16层','银星智界138项目3号楼','第16层','2','0',UTC_TIMESTAMP(),999950,810.27);

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90588,1041514,240111044332060198,239825274387467203,'银星科技大厦-B607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90620,1041514,240111044332060198,239825274387467235,'银星科技大厦-A702-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90652,1041514,240111044332060198,239825274387467267,'银星科技大厦-B711',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90684,1041514,240111044332060198,239825274387467299,'银星科技大厦-A801-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90716,1041514,240111044332060198,239825274387467331,'银星科技大厦-D802',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90748,1041514,240111044332060198,239825274387467363,'银星科技大厦-C901-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90780,1041514,240111044332060198,239825274387467395,'银星科技大厦-A1101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90812,1041514,240111044332060198,239825274387467427,'银星科技大厦-D1105',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90844,1041514,240111044332060199,239825274387467459,'银星科技大厦公寓-B504',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90876,1041514,240111044332060199,239825274387467491,'银星科技大厦公寓-C518',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90908,1041514,240111044332060199,239825274387467523,'银星科技大厦公寓-D513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90940,1041514,240111044332060200,239825274387467555,'银星智界138项目2号楼-首层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90972,1041514,240111044332060200,239825274387467587,'银星智界138项目3号楼-第16层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90557,1041514,240111044332060198,239825274387467172,'银星科技大厦-1楼',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90589,1041514,240111044332060198,239825274387467204,'银星科技大厦-B608',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90621,1041514,240111044332060198,239825274387467236,'银星科技大厦-A702-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90653,1041514,240111044332060198,239825274387467268,'银星科技大厦-B711-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90685,1041514,240111044332060198,239825274387467300,'银星科技大厦-A802',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90717,1041514,240111044332060198,239825274387467332,'银星科技大厦-D802-3',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90749,1041514,240111044332060198,239825274387467364,'银星科技大厦-C902',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90781,1041514,240111044332060198,239825274387467396,'银星科技大厦-A1102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90813,1041514,240111044332060198,239825274387467428,'银星科技大厦-D1106',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90845,1041514,240111044332060199,239825274387467460,'银星科技大厦公寓-B505',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90877,1041514,240111044332060199,239825274387467492,'银星科技大厦公寓-C519',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90909,1041514,240111044332060199,239825274387467524,'银星科技大厦公寓-D514',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90941,1041514,240111044332060200,239825274387467556,'银星智界138项目2号楼-第2层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90973,1041514,240111044332060200,239825274387467588,'银星智界138项目3号楼-第17层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90558,1041514,240111044332060198,239825274387467173,'银星科技大厦-2楼',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90590,1041514,240111044332060198,239825274387467205,'银星科技大厦-B609-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90622,1041514,240111044332060198,239825274387467237,'银星科技大厦-A703-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90654,1041514,240111044332060198,239825274387467269,'银星科技大厦-B712',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90686,1041514,240111044332060198,239825274387467301,'银星科技大厦-A803',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90718,1041514,240111044332060198,239825274387467333,'银星科技大厦-D804',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90750,1041514,240111044332060198,239825274387467365,'银星科技大厦-C902-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90782,1041514,240111044332060198,239825274387467397,'银星科技大厦-A1103-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90814,1041514,240111044332060198,239825274387467429,'银星科技大厦-D1107',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90846,1041514,240111044332060199,239825274387467461,'银星科技大厦公寓-B506',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90878,1041514,240111044332060199,239825274387467493,'银星科技大厦公寓-C520',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90910,1041514,240111044332060199,239825274387467525,'银星科技大厦公寓-D515',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90942,1041514,240111044332060200,239825274387467557,'银星智界138项目2号楼-第3层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90974,1041514,240111044332060200,239825274387467589,'银星智界138项目综合楼-首层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90559,1041514,240111044332060198,239825274387467174,'银星科技大厦-3楼D',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90591,1041514,240111044332060198,239825274387467206,'银星科技大厦-B609-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90623,1041514,240111044332060198,239825274387467238,'银星科技大厦-A703-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90655,1041514,240111044332060198,239825274387467270,'银星科技大厦-B713',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90687,1041514,240111044332060198,239825274387467302,'银星科技大厦-A804',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90719,1041514,240111044332060198,239825274387467334,'银星科技大厦-A901',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90751,1041514,240111044332060198,239825274387467366,'银星科技大厦-C902-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90783,1041514,240111044332060198,239825274387467398,'银星科技大厦-A1103-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90815,1041514,240111044332060198,239825274387467430,'银星科技大厦-D1108',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90847,1041514,240111044332060199,239825274387467462,'银星科技大厦公寓-B507',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90879,1041514,240111044332060199,239825274387467494,'银星科技大厦公寓-C521',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90911,1041514,240111044332060199,239825274387467526,'银星科技大厦公寓-D516',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90943,1041514,240111044332060200,239825274387467558,'银星智界138项目2号楼-第4层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90975,1041514,240111044332060200,239825274387467590,'银星智界138项目综合楼-第2层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90560,1041514,240111044332060198,239825274387467175,'银星科技大厦-3楼ABC',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90592,1041514,240111044332060198,239825274387467207,'银星科技大厦-B609-3',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90624,1041514,240111044332060198,239825274387467239,'银星科技大厦-A703-3',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90656,1041514,240111044332060198,239825274387467271,'银星科技大厦-B714',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90688,1041514,240111044332060198,239825274387467303,'银星科技大厦-A805',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90720,1041514,240111044332060198,239825274387467335,'银星科技大厦-A902',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90752,1041514,240111044332060198,239825274387467367,'银星科技大厦-C902-3',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90784,1041514,240111044332060198,239825274387467399,'银星科技大厦-A1104',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90816,1041514,240111044332060198,239825274387467431,'银星科技大厦-D1109',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90848,1041514,240111044332060199,239825274387467463,'银星科技大厦公寓-B508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90880,1041514,240111044332060199,239825274387467495,'银星科技大厦公寓-C522',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90912,1041514,240111044332060199,239825274387467527,'银星科技大厦公寓-D517',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90944,1041514,240111044332060200,239825274387467559,'银星智界138项目2号楼-第5层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90976,1041514,240111044332060200,239825274387467591,'银星智界138项目综合楼-第3层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90561,1041514,240111044332060198,239825274387467176,'银星科技大厦-4楼B区',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90593,1041514,240111044332060198,239825274387467208,'银星科技大厦-B609-4',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90625,1041514,240111044332060198,239825274387467240,'银星科技大厦-A703-4',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90657,1041514,240111044332060198,239825274387467272,'银星科技大厦-B715',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90689,1041514,240111044332060198,239825274387467304,'银星科技大厦-A806',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90721,1041514,240111044332060198,239825274387467336,'银星科技大厦-A903',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90753,1041514,240111044332060198,239825274387467368,'银星科技大厦-C902-4',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90785,1041514,240111044332060198,239825274387467400,'银星科技大厦-A1105',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90817,1041514,240111044332060199,239825274387467432,'银星科技大厦公寓-A501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90849,1041514,240111044332060199,239825274387467464,'银星科技大厦公寓-B509',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90881,1041514,240111044332060199,239825274387467496,'银星科技大厦公寓-C523',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90913,1041514,240111044332060199,239825274387467528,'银星科技大厦公寓-D518',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90945,1041514,240111044332060200,239825274387467560,'银星智界138项目2号楼-第6层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90977,1041514,240111044332060200,239825274387467592,'银星智界138项目综合楼-第4层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90562,1041514,240111044332060198,239825274387467177,'银星科技大厦-4楼C区',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90594,1041514,240111044332060198,239825274387467209,'银星科技大厦-C601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90626,1041514,240111044332060198,239825274387467241,'银星科技大厦-A704-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90658,1041514,240111044332060198,239825274387467273,'银星科技大厦-B716',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90690,1041514,240111044332060198,239825274387467305,'银星科技大厦-A806-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90722,1041514,240111044332060198,239825274387467337,'银星科技大厦-A90e',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90754,1041514,240111044332060198,239825274387467369,'银星科技大厦-C903-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90786,1041514,240111044332060198,239825274387467401,'银星科技大厦-A1106',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90818,1041514,240111044332060199,239825274387467433,'银星科技大厦公寓-A502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90850,1041514,240111044332060199,239825274387467465,'银星科技大厦公寓-B510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90882,1041514,240111044332060199,239825274387467497,'银星科技大厦公寓-C524',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90914,1041514,240111044332060199,239825274387467529,'银星科技大厦公寓-D519',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90946,1041514,240111044332060200,239825274387467561,'银星智界138项目2号楼-第7层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90978,1041514,240111044332060200,239825274387467593,'银星智界138项目综合楼-第5层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90563,1041514,240111044332060198,239825274387467178,'银星科技大厦-A601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90595,1041514,240111044332060198,239825274387467210,'银星科技大厦-C601-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90627,1041514,240111044332060198,239825274387467242,'银星科技大厦-A704-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90659,1041514,240111044332060198,239825274387467274,'银星科技大厦-C702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90691,1041514,240111044332060198,239825274387467306,'银星科技大厦-A807',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90723,1041514,240111044332060198,239825274387467338,'银星科技大厦-A905',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90755,1041514,240111044332060198,239825274387467370,'银星科技大厦-C903-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90787,1041514,240111044332060198,239825274387467402,'银星科技大厦-A1108',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90819,1041514,240111044332060199,239825274387467434,'银星科技大厦公寓-A503',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90851,1041514,240111044332060199,239825274387467466,'银星科技大厦公寓-B511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90883,1041514,240111044332060199,239825274387467498,'银星科技大厦公寓-C525',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90915,1041514,240111044332060199,239825274387467530,'银星科技大厦公寓-D520',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90947,1041514,240111044332060200,239825274387467562,'银星智界138项目2号楼-第8层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90979,1041514,240111044332060200,239825274387467594,'银星智界138项目综合楼-第6层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90564,1041514,240111044332060198,239825274387467179,'银星科技大厦-A602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90596,1041514,240111044332060198,239825274387467211,'银星科技大厦-C602-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90628,1041514,240111044332060198,239825274387467243,'银星科技大厦-A705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90660,1041514,240111044332060198,239825274387467275,'银星科技大厦-C703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90692,1041514,240111044332060198,239825274387467307,'银星科技大厦-A809',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90724,1041514,240111044332060198,239825274387467339,'银星科技大厦-A906',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90756,1041514,240111044332060198,239825274387467371,'银星科技大厦-C905',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90788,1041514,240111044332060198,239825274387467403,'银星科技大厦-A1109',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90820,1041514,240111044332060199,239825274387467435,'银星科技大厦公寓-A504',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90852,1041514,240111044332060199,239825274387467467,'银星科技大厦公寓-B512',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90884,1041514,240111044332060199,239825274387467499,'银星科技大厦公寓-C526',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90916,1041514,240111044332060199,239825274387467531,'银星科技大厦公寓-D521',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90948,1041514,240111044332060200,239825274387467563,'银星智界138项目2号楼-第9层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90980,1041514,240111044332060200,239825274387467595,'银星智界138项目综合楼-第7层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90565,1041514,240111044332060198,239825274387467180,'银星科技大厦-A603-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90597,1041514,240111044332060198,239825274387467212,'银星科技大厦-C603-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90629,1041514,240111044332060198,239825274387467244,'银星科技大厦-A706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90661,1041514,240111044332060198,239825274387467276,'银星科技大厦-C701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90693,1041514,240111044332060198,239825274387467308,'银星科技大厦-A810',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90725,1041514,240111044332060198,239825274387467340,'银星科技大厦-A907',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90757,1041514,240111044332060198,239825274387467372,'银星科技大厦-C906',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90789,1041514,240111044332060198,239825274387467404,'银星科技大厦-A1109-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90821,1041514,240111044332060199,239825274387467436,'银星科技大厦公寓-A505',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90853,1041514,240111044332060199,239825274387467468,'银星科技大厦公寓-B513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90885,1041514,240111044332060199,239825274387467500,'银星科技大厦公寓-C527',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90917,1041514,240111044332060199,239825274387467532,'银星科技大厦公寓-D522',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90949,1041514,240111044332060200,239825274387467564,'银星智界138项目2号楼-第10层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90981,1041514,240111044332060200,239825274387467596,'银星智界138项目综合楼-第8层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90566,1041514,240111044332060198,239825274387467181,'银星科技大厦-A603-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90598,1041514,240111044332060198,239825274387467213,'银星科技大厦-C603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90630,1041514,240111044332060198,239825274387467245,'银星科技大厦-A707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90662,1041514,240111044332060198,239825274387467277,'银星科技大厦-C704',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90694,1041514,240111044332060198,239825274387467309,'银星科技大厦-A810-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90726,1041514,240111044332060198,239825274387467341,'银星科技大厦-A908',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90758,1041514,240111044332060198,239825274387467373,'银星科技大厦-C907',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90790,1041514,240111044332060198,239825274387467405,'银星科技大厦-A1110',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90822,1041514,240111044332060199,239825274387467437,'银星科技大厦公寓-A506',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90854,1041514,240111044332060199,239825274387467469,'银星科技大厦公寓-B514',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90886,1041514,240111044332060199,239825274387467501,'银星科技大厦公寓-C528',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90918,1041514,240111044332060199,239825274387467533,'银星科技大厦公寓-D523',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90950,1041514,240111044332060200,239825274387467565,'银星智界138项目2号楼-第11层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90982,1041514,240111044332060200,239825274387467597,'银星智界138项目综合楼-第9层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90567,1041514,240111044332060198,239825274387467182,'银星科技大厦-A604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90599,1041514,240111044332060198,239825274387467214,'银星科技大厦-C603-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90631,1041514,240111044332060198,239825274387467246,'银星科技大厦-A708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90663,1041514,240111044332060198,239825274387467278,'银星科技大厦-C705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90695,1041514,240111044332060198,239825274387467310,'银星科技大厦-A811',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90727,1041514,240111044332060198,239825274387467342,'银星科技大厦-A909',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90759,1041514,240111044332060198,239825274387467374,'银星科技大厦-D901',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90791,1041514,240111044332060198,239825274387467406,'银星科技大厦-A1111',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90823,1041514,240111044332060199,239825274387467438,'银星科技大厦公寓-A507',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90855,1041514,240111044332060199,239825274387467470,'银星科技大厦公寓-B515',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90887,1041514,240111044332060199,239825274387467502,'银星科技大厦公寓-C529',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90919,1041514,240111044332060199,239825274387467534,'银星科技大厦公寓-D524',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90951,1041514,240111044332060200,239825274387467566,'银星智界138项目2号楼-第12层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90983,1041514,240111044332060200,239825274387467598,'银星智界138项目综合楼-第10层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90568,1041514,240111044332060198,239825274387467183,'银星科技大厦-A605-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90600,1041514,240111044332060198,239825274387467215,'银星科技大厦-C604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90632,1041514,240111044332060198,239825274387467247,'银星科技大厦-A709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90664,1041514,240111044332060198,239825274387467279,'银星科技大厦-C706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90696,1041514,240111044332060198,239825274387467311,'银星科技大厦-B808',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90728,1041514,240111044332060198,239825274387467343,'银星科技大厦-A910',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90760,1041514,240111044332060198,239825274387467375,'银星科技大厦-D902',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90792,1041514,240111044332060198,239825274387467407,'银星科技大厦-A1112',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90824,1041514,240111044332060199,239825274387467439,'银星科技大厦公寓-A508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90856,1041514,240111044332060199,239825274387467471,'银星科技大厦公寓-B516',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90888,1041514,240111044332060199,239825274387467503,'银星科技大厦公寓-C530',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90920,1041514,240111044332060199,239825274387467535,'银星科技大厦公寓-D525',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90952,1041514,240111044332060200,239825274387467567,'银星智界138项目2号楼-第13层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90984,1041514,240111044332060200,239825274387467599,'银星智界138项目综合楼-第11层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90569,1041514,240111044332060198,239825274387467184,'银星科技大厦-A605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90601,1041514,240111044332060198,239825274387467216,'银星科技大厦-C605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90633,1041514,240111044332060198,239825274387467248,'银星科技大厦-A710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90665,1041514,240111044332060198,239825274387467280,'银星科技大厦-C707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90697,1041514,240111044332060198,239825274387467312,'银星科技大厦-B809',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90729,1041514,240111044332060198,239825274387467344,'银星科技大厦-A911',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90761,1041514,240111044332060198,239825274387467376,'银星科技大厦-D903',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90793,1041514,240111044332060198,239825274387467408,'银星科技大厦-B1101',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90825,1041514,240111044332060199,239825274387467440,'银星科技大厦公寓-A509',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90857,1041514,240111044332060199,239825274387467472,'银星科技大厦公寓-B517',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90889,1041514,240111044332060199,239825274387467504,'银星科技大厦公寓-C531',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90921,1041514,240111044332060199,239825274387467536,'银星科技大厦公寓-D526',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90953,1041514,240111044332060200,239825274387467568,'银星智界138项目2号楼-第14层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90985,1041514,240111044332060200,239825274387467600,'银星智界138项目综合楼-第12层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90570,1041514,240111044332060198,239825274387467185,'银星科技大厦-A605-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90602,1041514,240111044332060198,239825274387467217,'银星科技大厦-C606',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90634,1041514,240111044332060198,239825274387467249,'银星科技大厦-A711',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90666,1041514,240111044332060198,239825274387467281,'银星科技大厦-C708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90698,1041514,240111044332060198,239825274387467313,'银星科技大厦-B区',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90730,1041514,240111044332060198,239825274387467345,'银星科技大厦-图书室',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90762,1041514,240111044332060198,239825274387467377,'银星科技大厦-D90e',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90794,1041514,240111044332060198,239825274387467409,'银星科技大厦-B1102',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90826,1041514,240111044332060199,239825274387467441,'银星科技大厦公寓-A510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90858,1041514,240111044332060199,239825274387467473,'银星科技大厦公寓-B518',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90890,1041514,240111044332060199,239825274387467505,'银星科技大厦公寓-C532',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90922,1041514,240111044332060199,239825274387467537,'银星科技大厦公寓-D527',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90954,1041514,240111044332060200,239825274387467569,'银星智界138项目2号楼-第15层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90986,1041514,240111044332060200,239825274387467601,'银星智界138项目综合楼-第13层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90571,1041514,240111044332060198,239825274387467186,'银星科技大厦-A606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90603,1041514,240111044332060198,239825274387467218,'银星科技大厦-C607',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90635,1041514,240111044332060198,239825274387467250,'银星科技大厦-A712',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90667,1041514,240111044332060198,239825274387467282,'银星科技大厦-C709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90699,1041514,240111044332060198,239825274387467314,'银星科技大厦-B807',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90731,1041514,240111044332060198,239825274387467346,'银星科技大厦-培训室',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90763,1041514,240111044332060198,239825274387467378,'银星科技大厦-D90e-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90795,1041514,240111044332060198,239825274387467410,'银星科技大厦-B1102-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90827,1041514,240111044332060199,239825274387467442,'银星科技大厦公寓-A511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90859,1041514,240111044332060199,239825274387467474,'银星科技大厦公寓-C501',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90891,1041514,240111044332060199,239825274387467506,'银星科技大厦公寓-C533',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90923,1041514,240111044332060200,239825274387467538,'银星智界138项目1号楼-首层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90955,1041514,240111044332060200,239825274387467570,'银星智界138项目2号楼-第16层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90987,1041514,240111044332060200,239825274387467602,'银星智界138项目综合楼-第14层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90572,1041514,240111044332060198,239825274387467187,'银星科技大厦-A607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90604,1041514,240111044332060198,239825274387467219,'银星科技大厦-C608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90636,1041514,240111044332060198,239825274387467251,'银星科技大厦-A713',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90668,1041514,240111044332060198,239825274387467283,'银星科技大厦-C710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90700,1041514,240111044332060198,239825274387467315,'银星科技大厦-C802',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90732,1041514,240111044332060198,239825274387467347,'银星科技大厦-大会议室',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90764,1041514,240111044332060198,239825274387467379,'银星科技大厦-D905',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90796,1041514,240111044332060198,239825274387467411,'银星科技大厦-B1103',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90828,1041514,240111044332060199,239825274387467443,'银星科技大厦公寓-A512',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90860,1041514,240111044332060199,239825274387467475,'银星科技大厦公寓-C502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90892,1041514,240111044332060199,239825274387467507,'银星科技大厦公寓-C534',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90924,1041514,240111044332060200,239825274387467539,'银星智界138项目1号楼-第2层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90956,1041514,240111044332060200,239825274387467571,'银星智界138项目2号楼-第17层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90988,1041514,240111044332060200,239825274387467603,'银星智界138项目综合楼-第15层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90573,1041514,240111044332060198,239825274387467188,'银星科技大厦-A608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90605,1041514,240111044332060198,239825274387467220,'银星科技大厦-C608-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90637,1041514,240111044332060198,239825274387467252,'银星科技大厦-A714',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90669,1041514,240111044332060198,239825274387467284,'银星科技大厦-D701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90701,1041514,240111044332060198,239825274387467316,'银星科技大厦-C803',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90733,1041514,240111044332060198,239825274387467348,'银星科技大厦-小会议室',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90765,1041514,240111044332060198,239825274387467380,'银星科技大厦-D906',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90797,1041514,240111044332060198,239825274387467412,'银星科技大厦-B1104',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90829,1041514,240111044332060199,239825274387467444,'银星科技大厦公寓-A513',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90861,1041514,240111044332060199,239825274387467476,'银星科技大厦公寓-C503',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90893,1041514,240111044332060199,239825274387467508,'银星科技大厦公寓-C535',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90925,1041514,240111044332060200,239825274387467540,'银星智界138项目1号楼-第3层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90957,1041514,240111044332060200,239825274387467572,'银星智界138项目3号楼-首层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90989,1041514,240111044332060200,239825274387467604,'银星智界138项目综合楼-第16层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90574,1041514,240111044332060198,239825274387467189,'银星科技大厦-A608-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90606,1041514,240111044332060198,239825274387467221,'银星科技大厦-C609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90638,1041514,240111044332060198,239825274387467253,'银星科技大厦-A715',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90670,1041514,240111044332060198,239825274387467285,'银星科技大厦-D702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90702,1041514,240111044332060198,239825274387467317,'银星科技大厦-C801',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90734,1041514,240111044332060198,239825274387467349,'银星科技大厦-A913',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90766,1041514,240111044332060198,239825274387467381,'银星科技大厦-D907',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90798,1041514,240111044332060198,239825274387467413,'银星科技大厦-B1105',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90830,1041514,240111044332060199,239825274387467445,'银星科技大厦公寓-A514',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90862,1041514,240111044332060199,239825274387467477,'银星科技大厦公寓-C504',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90894,1041514,240111044332060199,239825274387467509,'银星科技大厦公寓-C536',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90926,1041514,240111044332060200,239825274387467541,'银星智界138项目1号楼-第4层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90958,1041514,240111044332060200,239825274387467573,'银星智界138项目3号楼-第2层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90990,1041514,240111044332060200,239825274387467605,'银星智界138项目综合楼-第17层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90575,1041514,240111044332060198,239825274387467190,'银星科技大厦-A609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90607,1041514,240111044332060198,239825274387467222,'银星科技大厦-D601',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90639,1041514,240111044332060198,239825274387467254,'银星科技大厦-A716',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90671,1041514,240111044332060198,239825274387467286,'银星科技大厦-D703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90703,1041514,240111044332060198,239825274387467318,'银星科技大厦-C806',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90735,1041514,240111044332060198,239825274387467350,'银星科技大厦-A91e',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90767,1041514,240111044332060198,239825274387467382,'银星科技大厦-D908',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90799,1041514,240111044332060198,239825274387467414,'银星科技大厦-C1101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90831,1041514,240111044332060199,239825274387467446,'银星科技大厦公寓-A515',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90863,1041514,240111044332060199,239825274387467478,'银星科技大厦公寓-C505',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90895,1041514,240111044332060199,239825274387467510,'银星科技大厦公寓-C537',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90927,1041514,240111044332060200,239825274387467542,'银星智界138项目1号楼-第5层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90959,1041514,240111044332060200,239825274387467574,'银星智界138项目3号楼-第3层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90991,1041514,240111044332060200,239825274387467606,'银星智界138项目综合楼-第18层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90576,1041514,240111044332060198,239825274387467191,'银星科技大厦-A610',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90608,1041514,240111044332060198,239825274387467223,'银星科技大厦-D602-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90640,1041514,240111044332060198,239825274387467255,'银星科技大厦-A717',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90672,1041514,240111044332060198,239825274387467287,'银星科技大厦-D704',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90704,1041514,240111044332060198,239825274387467319,'银星科技大厦-C807',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90736,1041514,240111044332060198,239825274387467351,'银星科技大厦-B901',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90768,1041514,240111044332060198,239825274387467383,'银星科技大厦-D910',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90800,1041514,240111044332060198,239825274387467415,'银星科技大厦-C1102-1',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90832,1041514,240111044332060199,239825274387467447,'银星科技大厦公寓-A516',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90864,1041514,240111044332060199,239825274387467479,'银星科技大厦公寓-C506',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90896,1041514,240111044332060199,239825274387467511,'银星科技大厦公寓-D501',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90928,1041514,240111044332060200,239825274387467543,'银星智界138项目1号楼-第6层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90960,1041514,240111044332060200,239825274387467575,'银星智界138项目3号楼-第4层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90992,1041514,240111044332060200,239825274387467607,'银星智界138项目综合楼-第19层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90577,1041514,240111044332060198,239825274387467192,'银星科技大厦-A611',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90609,1041514,240111044332060198,239825274387467224,'银星科技大厦-D602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90641,1041514,240111044332060198,239825274387467256,'银星科技大厦-B701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90673,1041514,240111044332060198,239825274387467288,'银星科技大厦-D705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90705,1041514,240111044332060198,239825274387467320,'银星科技大厦-C807-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90737,1041514,240111044332060198,239825274387467352,'银星科技大厦-B901-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90769,1041514,240111044332060198,239825274387467384,'银星科技大厦-D909',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90801,1041514,240111044332060198,239825274387467416,'银星科技大厦-C1102-2',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90833,1041514,240111044332060199,239825274387467448,'银星科技大厦公寓-A517',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90865,1041514,240111044332060199,239825274387467480,'银星科技大厦公寓-C507',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90897,1041514,240111044332060199,239825274387467512,'银星科技大厦公寓-D502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90929,1041514,240111044332060200,239825274387467544,'银星智界138项目1号楼-第7层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90961,1041514,240111044332060200,239825274387467576,'银星智界138项目3号楼-第5层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90578,1041514,240111044332060198,239825274387467193,'银星科技大厦-A612',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90610,1041514,240111044332060198,239825274387467225,'银星科技大厦-D603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90642,1041514,240111044332060198,239825274387467257,'银星科技大厦-B702',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90674,1041514,240111044332060198,239825274387467289,'银星科技大厦-D706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90706,1041514,240111044332060198,239825274387467321,'银星科技大厦-D800',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90738,1041514,240111044332060198,239825274387467353,'银星科技大厦-B902',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90770,1041514,240111044332060198,239825274387467385,'银星科技大厦-D911',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90802,1041514,240111044332060198,239825274387467417,'银星科技大厦-C1103-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90834,1041514,240111044332060199,239825274387467449,'银星科技大厦公寓-A518',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90866,1041514,240111044332060199,239825274387467481,'银星科技大厦公寓-C508',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90898,1041514,240111044332060199,239825274387467513,'银星科技大厦公寓-D503',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90930,1041514,240111044332060200,239825274387467545,'银星智界138项目1号楼-第8层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90962,1041514,240111044332060200,239825274387467577,'银星智界138项目3号楼-第6层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90579,1041514,240111044332060198,239825274387467194,'银星科技大厦-A613',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90611,1041514,240111044332060198,239825274387467226,'银星科技大厦-D604',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90643,1041514,240111044332060198,239825274387467258,'银星科技大厦-B702-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90675,1041514,240111044332060198,239825274387467290,'银星科技大厦-D707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90707,1041514,240111044332060198,239825274387467322,'银星科技大厦-C808',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90739,1041514,240111044332060198,239825274387467354,'银星科技大厦-B903',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90771,1041514,240111044332060198,239825274387467386,'银星科技大厦-D912',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90803,1041514,240111044332060198,239825274387467418,'银星科技大厦-C1104',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90835,1041514,240111044332060199,239825274387467450,'银星科技大厦公寓-A519',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90867,1041514,240111044332060199,239825274387467482,'银星科技大厦公寓-C509',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90899,1041514,240111044332060199,239825274387467514,'银星科技大厦公寓-D504',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90931,1041514,240111044332060200,239825274387467546,'银星智界138项目1号楼-第9层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90963,1041514,240111044332060200,239825274387467578,'银星智界138项目3号楼-第7层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90580,1041514,240111044332060198,239825274387467195,'银星科技大厦-B601-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90612,1041514,240111044332060198,239825274387467227,'银星科技大厦-D605-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90644,1041514,240111044332060198,239825274387467259,'银星科技大厦-B703',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90676,1041514,240111044332060198,239825274387467291,'银星科技大厦-D708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90708,1041514,240111044332060198,239825274387467323,'银星科技大厦-C810',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90740,1041514,240111044332060198,239825274387467355,'银星科技大厦-B90e',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90772,1041514,240111044332060198,239825274387467387,'银星科技大厦-A区',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90804,1041514,240111044332060198,239825274387467419,'银星科技大厦-C1105',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90836,1041514,240111044332060199,239825274387467451,'银星科技大厦公寓-A520',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90868,1041514,240111044332060199,239825274387467483,'银星科技大厦公寓-C510',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90900,1041514,240111044332060199,239825274387467515,'银星科技大厦公寓-D505',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90932,1041514,240111044332060200,239825274387467547,'银星智界138项目1号楼-第10层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90964,1041514,240111044332060200,239825274387467579,'银星智界138项目3号楼-第8层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90581,1041514,240111044332060198,239825274387467196,'银星科技大厦-B601-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90613,1041514,240111044332060198,239825274387467228,'银星科技大厦-D605-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90645,1041514,240111044332060198,239825274387467260,'银星科技大厦-B704',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90677,1041514,240111044332060198,239825274387467292,'银星科技大厦-D709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90709,1041514,240111044332060198,239825274387467324,'银星科技大厦-C811',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90741,1041514,240111044332060198,239825274387467356,'银星科技大厦-B905',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90773,1041514,240111044332060198,239825274387467388,'银星科技大厦-B1001',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90805,1041514,240111044332060198,239825274387467420,'银星科技大厦-C1106',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90837,1041514,240111044332060199,239825274387467452,'银星科技大厦公寓-A521',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90869,1041514,240111044332060199,239825274387467484,'银星科技大厦公寓-C511',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90901,1041514,240111044332060199,239825274387467516,'银星科技大厦公寓-D506',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90933,1041514,240111044332060200,239825274387467548,'银星智界138项目1号楼-第11层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90965,1041514,240111044332060200,239825274387467580,'银星智界138项目3号楼-第9层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90582,1041514,240111044332060198,239825274387467197,'银星科技大厦-B602',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90614,1041514,240111044332060198,239825274387467229,'银星科技大厦-D605-3',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90646,1041514,240111044332060198,239825274387467261,'银星科技大厦-B705',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90678,1041514,240111044332060198,239825274387467293,'银星科技大厦-D710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90710,1041514,240111044332060198,239825274387467325,'银星科技大厦-D801',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90742,1041514,240111044332060198,239825274387467357,'银星科技大厦-B906',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90774,1041514,240111044332060198,239825274387467389,'银星科技大厦-10楼B区',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90806,1041514,240111044332060198,239825274387467421,'银星科技大厦-C1106-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90838,1041514,240111044332060199,239825274387467453,'银星科技大厦公寓-A522',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90870,1041514,240111044332060199,239825274387467485,'银星科技大厦公寓-C512',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90902,1041514,240111044332060199,239825274387467517,'银星科技大厦公寓-D507',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90934,1041514,240111044332060200,239825274387467549,'银星智界138项目1号楼-第12层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90966,1041514,240111044332060200,239825274387467581,'银星智界138项目3号楼-第10层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90583,1041514,240111044332060198,239825274387467198,'银星科技大厦-B603',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90615,1041514,240111044332060198,239825274387467230,'银星科技大厦-D606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90647,1041514,240111044332060198,239825274387467262,'银星科技大厦-B706',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90679,1041514,240111044332060198,239825274387467294,'银星科技大厦-D711',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90711,1041514,240111044332060198,239825274387467326,'银星科技大厦-C805',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90743,1041514,240111044332060198,239825274387467358,'银星科技大厦-B907-1',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90775,1041514,240111044332060198,239825274387467390,'银星科技大厦-C1001',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90807,1041514,240111044332060198,239825274387467422,'银星科技大厦-C1106-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90839,1041514,240111044332060199,239825274387467454,'银星科技大厦公寓-A523',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90871,1041514,240111044332060199,239825274387467486,'银星科技大厦公寓-C513',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90903,1041514,240111044332060199,239825274387467518,'银星科技大厦公寓-D508',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90935,1041514,240111044332060200,239825274387467550,'银星智界138项目1号楼-第13层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90967,1041514,240111044332060200,239825274387467582,'银星智界138项目3号楼-第11层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90584,1041514,240111044332060198,239825274387467199,'银星科技大厦-B604-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90616,1041514,240111044332060198,239825274387467231,'银星科技大厦-D607',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90648,1041514,240111044332060198,239825274387467263,'银星科技大厦-B707',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90680,1041514,240111044332060198,239825274387467295,'银星科技大厦-D712',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90712,1041514,240111044332060198,239825274387467327,'银星科技大厦-C809',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90744,1041514,240111044332060198,239825274387467359,'银星科技大厦-B908',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90776,1041514,240111044332060198,239825274387467391,'银星科技大厦-C1002',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90808,1041514,240111044332060198,239825274387467423,'银星科技大厦-D1101',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90840,1041514,240111044332060199,239825274387467455,'银星科技大厦公寓-A524',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90872,1041514,240111044332060199,239825274387467487,'银星科技大厦公寓-C514',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90904,1041514,240111044332060199,239825274387467519,'银星科技大厦公寓-D509',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90936,1041514,240111044332060200,239825274387467551,'银星智界138项目1号楼-第14层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90968,1041514,240111044332060200,239825274387467583,'银星智界138项目3号楼-第12层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90585,1041514,240111044332060198,239825274387467200,'银星科技大厦-B604-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90617,1041514,240111044332060198,239825274387467232,'银星科技大厦-D608',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90649,1041514,240111044332060198,239825274387467264,'银星科技大厦-B708',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90681,1041514,240111044332060198,239825274387467296,'银星科技大厦-D713',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90713,1041514,240111044332060198,239825274387467328,'银星科技大厦-C809-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90745,1041514,240111044332060198,239825274387467360,'银星科技大厦-B908-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90777,1041514,240111044332060198,239825274387467392,'银星科技大厦-C1003',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90809,1041514,240111044332060198,239825274387467424,'银星科技大厦-D1102',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90841,1041514,240111044332060199,239825274387467456,'银星科技大厦公寓-B501',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90873,1041514,240111044332060199,239825274387467488,'银星科技大厦公寓-C515',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90905,1041514,240111044332060199,239825274387467520,'银星科技大厦公寓-D510',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90937,1041514,240111044332060200,239825274387467552,'银星智界138项目1号楼-第15层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90969,1041514,240111044332060200,239825274387467584,'银星智界138项目3号楼-第13层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90586,1041514,240111044332060198,239825274387467201,'银星科技大厦-B605',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90618,1041514,240111044332060198,239825274387467233,'银星科技大厦-D609',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90650,1041514,240111044332060198,239825274387467265,'银星科技大厦-B709',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90682,1041514,240111044332060198,239825274387467297,'银星科技大厦-D71E',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90714,1041514,240111044332060198,239825274387467329,'银星科技大厦-D802-1',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90746,1041514,240111044332060198,239825274387467361,'银星科技大厦-B909',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90778,1041514,240111044332060198,239825274387467393,'银星科技大厦-C1004',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90810,1041514,240111044332060198,239825274387467425,'银星科技大厦-D1103',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90842,1041514,240111044332060199,239825274387467457,'银星科技大厦公寓-B502',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90874,1041514,240111044332060199,239825274387467489,'银星科技大厦公寓-C516',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90906,1041514,240111044332060199,239825274387467521,'银星科技大厦公寓-D511',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90938,1041514,240111044332060200,239825274387467553,'银星智界138项目1号楼-第16层',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90970,1041514,240111044332060200,239825274387467585,'银星智界138项目3号楼-第14层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90587,1041514,240111044332060198,239825274387467202,'银星科技大厦-B606',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90619,1041514,240111044332060198,239825274387467234,'银星科技大厦-A701',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90651,1041514,240111044332060198,239825274387467266,'银星科技大厦-B710',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90683,1041514,240111044332060198,239825274387467298,'银星科技大厦-A801-1',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90715,1041514,240111044332060198,239825274387467330,'银星科技大厦-D802-2',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90747,1041514,240111044332060198,239825274387467362,'银星科技大厦-C901',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90779,1041514,240111044332060198,239825274387467394,'银星科技大厦-D区',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90811,1041514,240111044332060198,239825274387467426,'银星科技大厦-D1104',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90843,1041514,240111044332060199,239825274387467458,'银星科技大厦公寓-B503',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90875,1041514,240111044332060199,239825274387467490,'银星科技大厦公寓-C517',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90907,1041514,240111044332060199,239825274387467522,'银星科技大厦公寓-D512',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90939,1041514,240111044332060200,239825274387467554,'银星智界138项目1号楼-第17层',2);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (90971,1041514,240111044332060200,239825274387467586,'银星智界138项目3号楼-第15层',2);

INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141399,41310,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141400,41330,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141401,41320,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141402,30500,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141403,31000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141404,32000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141405,33000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141406,34000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141407,35000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141408,30600,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141409,50000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141410,50100,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141411,50300,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141412,50500,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141413,50600,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141414,50700,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141415,50710,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141416,50720,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141417,50730,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141418,52000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141419,52010,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141420,52020,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141421,52030,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141422,60100,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141423,60200,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141307,10100,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141308,10400,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141309,10200,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141310,10600,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141311,11000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141312,20100,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141313,20140,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141314,20150,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141315,20155,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141316,20158,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141317,20170,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141318,20180,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141319,20190,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141320,20191,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141321,20230,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141322,20220,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141323,20240,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141324,20250,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141325,20255,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141326,20258,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141327,20280,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141328,20290,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141329,20291,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141330,20400,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141331,20422,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141332,20410,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141333,20420,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141334,20430,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141335,20800,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141336,20810,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141337,20811,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141338,20812,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141339,20820,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141340,20821,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141341,20822,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141342,20830,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141343,20831,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141344,20840,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141345,20841,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141346,20850,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141347,20851,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141348,20852,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141349,20860,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141350,49100,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141351,49110,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141352,49150,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141353,49120,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141354,49130,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141355,49140,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141356,21000,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141357,21010,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141358,21020,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141359,21022,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141360,21024,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141361,21030,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141362,21032,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141363,21034,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141364,21040,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141365,21042,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141366,21044,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141367,21050,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141368,21052,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141369,21054,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141370,21100,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141371,21110,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141372,21120,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141373,21200,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141374,21210,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141375,21220,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141376,21230,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141377,40300,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141378,40400,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141379,40410,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141380,40420,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141381,40430,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141382,40440,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141383,40450,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141384,40500,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141385,41700,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141386,41710,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141387,41720,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141388,41730,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141389,41740,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141390,41750,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141391,41760,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141392,40800,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141393,40830,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141394,40835,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141395,40810,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141396,40840,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141397,40850,NULL,'EhNamespaces',999950,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (141398,41300,NULL,'EhNamespaces',999950,2);

INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001418,'',0,0,-1,'论坛','/0',0,2,1,NOW(),0,NULL,999950,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001419,'',0,1,-1,'活动管理','/1',0,2,1,NOW(),0,NULL,999950,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001420,'',0,1001420,1,'活动管理-默认子分类','/1/1001420',0,2,1,NOW(),0,NULL,999950,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001421,'',0,2,-1,'活动管理二','/2',0,2,1,NOW(),0,NULL,999950,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001422,'',0,1001422,2,'活动管理二-默认子分类','/2/1001422',0,2,1,NOW(),0,NULL,999950,0,1,NULL,NULL,NULL,1);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001423,'',0,3,-1,'活动管理三','/3',0,2,1,NOW(),0,NULL,999950,0,1,NULL,NULL,NULL,0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`) 
	VALUES (1001424,'',0,1001424,3,'活动管理三-默认子分类','/3/1001424',0,2,1,NOW(),0,NULL,999950,0,1,NULL,NULL,NULL,1);

INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (567,999950,1,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (568,999950,4,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (569,999950,5,0,'topic','话题',0,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (570,999950,1,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (571,999950,4,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (572,999950,5,0,'activity','活动',1,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (573,999950,1,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (574,999950,4,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_forum_service_types` (`id`, `namespace_id`, `module_type`, `category_id`, `service_type`, `name`, `sort_num`, `create_time`) 
	VALUES (575,999950,5,0,'poll','投票',2,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1041514,240111044332060198);
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1041514,240111044332060199);
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1041514,240111044332060200);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1131,999950,'AssociationLayout','{"versionCode":"2018021201","layoutName":"AssociationLayout","displayName":"交流大厅","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018021201,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1132,999950,'AssociationLayout','{"versionCode":"2018021201","layoutName":"AssociationLayout","displayName":"交流大厅","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018021201,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1133,999950,'AssociationLayout','{"versionCode":"2018021201","layoutName":"AssociationLayout","displayName":"交流大厅","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":10,"separatorFlag":0,"separatorHeight":0}]}',2018021201,0,2,UTC_TIMESTAMP(),'default',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1134,999950,'ServiceMarketLayout','{"versionCode":"2018021201","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":32,"paddingBottom":32,"paddingLeft":32,"paddingRight":32,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":32,"paddingBottom":32,"paddingLeft":32,"paddingRight":32,"lineSpacing":32,"columnSpacing":32},"style":"Gallery","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018021201,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1135,999950,'ServiceMarketLayout','{"versionCode":"2018021201","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":32,"paddingBottom":32,"paddingLeft":32,"paddingRight":32,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":32,"paddingBottom":32,"paddingLeft":32,"paddingRight":32,"lineSpacing":32,"columnSpacing":32},"style":"Gallery","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018021201,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES (1136,999950,'ServiceMarketLayout','{"versionCode":"2018021201","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"Banner","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"separatorFlag":0,"separatorHeight":0,"defaultOrder":10},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup2","cssStyleFlag":1,"paddingTop":32,"paddingBottom":32,"paddingLeft":32,"paddingRight":32,"lineSpacing":0,"columnSpacing":0},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount":1},"style":"Default","defaultOrder":30,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup4","cssStyleFlag":1,"paddingTop":32,"paddingBottom":32,"paddingLeft":32,"paddingRight":32,"lineSpacing":32,"columnSpacing":32},"style":"Gallery","defaultOrder":40,"separatorFlag":0,"separatorHeight":0,"columnCount":2},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup5","cssStyleFlag":1,"paddingTop":0,"paddingBottom":0,"paddingLeft":0,"paddingRight":0,"lineSpacing":0,"columnSpacing":0},"style":"Gallery","defaultOrder":50,"separatorFlag":0,"separatorHeight":0,"columnCount":1},{"groupName":"","widget":"NewsFlash","instanceConfig":{"timeWidgetStyle":"datetime","itemGroup":"Default","categoryId":0,"newsSize":3},"defaultOrder":60,"separatorFlag":0,"separatorHeight":0}]}',2018021201,0,2,UTC_TIMESTAMP(),'default',0,0,0);

INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217081,999950,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRveU4yTXdaVE5tWVdSaE0yWmlORGhpWkdZNU1XRmxNek5tTURjNFpXTTBZZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217082,999950,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRveU4yTXdaVE5tWVdSaE0yWmlORGhpWkdZNU1XRmxNek5tTURjNFpXTTBZZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `status`, `order`, `creator_uid`, `create_time`, `scene_type`) 
	VALUES (217083,999950,0,'/home','Default',0,0,'/home','Default','cs://1/image/aW1hZ2UvTVRveU4yTXdaVE5tWVdSaE0yWmlORGhpWkdZNU1XRmxNek5tTURjNFpXTTBZZw',0,NULL,2,10,0,UTC_TIMESTAMP(),'default');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161074,999950,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRvMllUZ3pNR1l6TURNM05URTJOMkUwWkRsbU9HWTJNMlJsWWpZeE5tSmxZdw',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161075,999950,0,0,0,'/home','NavigatorGroup4','个人服务','个人服务','cs://1/image/aW1hZ2UvTVRveU9URXhOR0kxWkdNd1l6TmhaV0prWkRJNU9XWTFaREk0TjJOaVpEQmtOdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161076,999950,0,0,0,'/home','NavigatorGroup4','个人服务','个人服务','cs://1/image/aW1hZ2UvTVRveU9URXhOR0kxWkdNd1l6TmhaV0prWkRJNU9XWTFaREk0TjJOaVpEQmtOdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161077,999950,0,0,0,'/home','NavigatorGroup4','个人服务','个人服务','cs://1/image/aW1hZ2UvTVRveU9URXhOR0kxWkdNd1l6TmhaV0prWkRJNU9XWTFaREk0TjJOaVpEQmtOdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161078,999950,0,0,0,'/home','NavigatorGroup4','企业服务','企业服务','cs://1/image/aW1hZ2UvTVRveVl6WXlOVE13WkRGaU9Ua3lPRFUzWXpGbE9XUXdPRFpsTWpBNVlqZzJZUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161079,999950,0,0,0,'/home','NavigatorGroup4','企业服务','企业服务','cs://1/image/aW1hZ2UvTVRveVl6WXlOVE13WkRGaU9Ua3lPRFUzWXpGbE9XUXdPRFpsTWpBNVlqZzJZUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161080,999950,0,0,0,'/home','NavigatorGroup4','企业服务','企业服务','cs://1/image/aW1hZ2UvTVRveVl6WXlOVE13WkRGaU9Ua3lPRFUzWXpGbE9XUXdPRFpsTWpBNVlqZzJZUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',20,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161081,999950,0,0,0,'/home','NavigatorGroup4','场地租赁','场地租赁','cs://1/image/aW1hZ2UvTVRwaE4yTTRNV1UwWVdJeVpqSm1OVGsxTWpCaFlXSXpOVGhpWTJSaFlUSmxaQQ',2,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',30,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161082,999950,0,0,0,'/home','NavigatorGroup4','场地租赁','场地租赁','cs://1/image/aW1hZ2UvTVRwaE4yTTRNV1UwWVdJeVpqSm1OVGsxTWpCaFlXSXpOVGhpWTJSaFlUSmxaQQ',2,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',30,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161083,999950,0,0,0,'/home','NavigatorGroup4','场地租赁','场地租赁','cs://1/image/aW1hZ2UvTVRwaE4yTTRNV1UwWVdJeVpqSm1OVGsxTWpCaFlXSXpOVGhpWTJSaFlUSmxaQQ',2,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',30,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161084,999950,0,0,0,'/home','NavigatorGroup5','园区快讯','园区快讯','cs://1/image/aW1hZ2UvTVRvM1l6a3pPVE0wWlRBMk16YzRNV1kwWWpnNFlqbGtabU00TnpVNE16QTBNUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161085,999950,0,0,0,'/home','NavigatorGroup5','园区快讯','园区快讯','cs://1/image/aW1hZ2UvTVRvM1l6a3pPVE0wWlRBMk16YzRNV1kwWWpnNFlqbGtabU00TnpVNE16QTBNUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161086,999950,0,0,0,'/home','NavigatorGroup5','园区快讯','园区快讯','cs://1/image/aW1hZ2UvTVRvM1l6a3pPVE0wWlRBMk16YzRNV1kwWWpnNFlqbGtabU00TnpVNE16QTBNUQ',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',10,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161087,999950,0,0,0,'/association','TabGroup','论坛','论坛','cs://1/image/aW1hZ2UvTVRwa05qSmlOVFZoTnprNU9Ua3pOekl3TVdWaU5EQmxOemcyWmpReE56UTFOdw',1,1,62,'{"tag":"论坛"}',10,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161088,999950,0,0,0,'/association','TabGroup','论坛','论坛','cs://1/image/aW1hZ2UvTVRwa05qSmlOVFZoTnprNU9Ua3pOekl3TVdWaU5EQmxOemcyWmpReE56UTFOdw',1,1,62,'{"tag":"论坛"}',10,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161089,999950,0,0,0,'/association','TabGroup','论坛','论坛','cs://1/image/aW1hZ2UvTVRwa05qSmlOVFZoTnprNU9Ua3pOekl3TVdWaU5EQmxOemcyWmpReE56UTFOdw',1,1,62,'{"tag":"论坛"}',10,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161090,999950,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRwaVpHWTBNR1JqT0dReFpHWTFPREUyWkRRMU9UUmpZMlUyTm1FelltRTFNdw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',20,0,1,1,0,0,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161091,999950,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRwaVpHWTBNR1JqT0dReFpHWTFPREUyWkRRMU9UUmpZMlUyTm1FelltRTFNdw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',20,0,1,1,0,0,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161092,999950,0,0,0,'/association','TabGroup','活动','活动','cs://1/image/aW1hZ2UvTVRwaVpHWTBNR1JqT0dReFpHWTFPREUyWkRRMU9UUmpZMlUyTm1FelltRTFNdw',1,1,61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":2,"scope":3,"style":4,"title": "活动"}',20,0,1,1,0,0,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161051,999950,0,0,0,'/home','NavigatorGroup2','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRwa05qSmlOVFZoTnprNU9Ua3pOekl3TVdWaU5EQmxOemcyWmpReE56UTFOdw',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',10,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161052,999950,0,0,0,'/home','NavigatorGroup2','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRwa05qSmlOVFZoTnprNU9Ua3pOekl3TVdWaU5EQmxOemcyWmpReE56UTFOdw',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',10,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161053,999950,0,0,0,'/home','NavigatorGroup2','物业缴费','物业缴费','cs://1/image/aW1hZ2UvTVRwa05qSmlOVFZoTnprNU9Ua3pOekl3TVdWaU5EQmxOemcyWmpReE56UTFOdw',1,1,14,'{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&name=1#/login#sign_suffix"}',10,0,1,1,0,1,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161054,999950,0,0,0,'/home','NavigatorGroup2','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvd1ptWmpZemxqWkRNeE56RXlOemczT0RReE56RTNaREl5WkRObE5EUTRZZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',20,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161055,999950,0,0,0,'/home','NavigatorGroup2','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvd1ptWmpZemxqWkRNeE56RXlOemczT0RReE56RTNaREl5WkRObE5EUTRZZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',20,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161056,999950,0,0,0,'/home','NavigatorGroup2','物业报修','物业报修','cs://1/image/aW1hZ2UvTVRvd1ptWmpZemxqWkRNeE56RXlOemczT0RReE56RTNaREl5WkRObE5EUTRZZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修"}',20,0,1,1,0,1,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161057,999950,0,0,0,'/home','NavigatorGroup2','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvNVpHVXlaamRrTURFek56WXpPV0l3WXpOa09EazNaRGRqWVdVM01HRXhZdw',1,1,45,'',30,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161058,999950,0,0,0,'/home','NavigatorGroup2','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvNVpHVXlaamRrTURFek56WXpPV0l3WXpOa09EazNaRGRqWVdVM01HRXhZdw',1,1,45,'',30,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161059,999950,0,0,0,'/home','NavigatorGroup2','服务热线','服务热线','cs://1/image/aW1hZ2UvTVRvNVpHVXlaamRrTURFek56WXpPV0l3WXpOa09EazNaRGRqWVdVM01HRXhZdw',1,1,45,'',30,0,1,1,0,1,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161060,999950,0,0,0,'/home','NavigatorGroup2','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRwaVpHWTBNR1JqT0dReFpHWTFPREUyWkRRMU9UUmpZMlUyTm1FelltRTFNdw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',40,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161061,999950,0,0,0,'/home','NavigatorGroup2','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRwaVpHWTBNR1JqT0dReFpHWTFPREUyWkRRMU9UUmpZMlUyTm1FelltRTFNdw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',40,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161062,999950,0,0,0,'/home','NavigatorGroup2','园区介绍','园区介绍','cs://1/image/aW1hZ2UvTVRwaVpHWTBNR1JqT0dReFpHWTFPREUyWkRRMU9UUmpZMlUyTm1FelltRTFNdw',1,1,13,'{"url":"${home.url}/park-introduction/index.html?hideNavigationBar=1#/#sign_suffix"}',40,0,1,1,0,1,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161063,999950,0,0,0,'/home','NavigatorGroup2','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRvNE1UUTFOakJpTTJNNFl6UTBaak0yTjJRelltTXdOR1l6T0RVME1tRm1Zdw',1,1,23,'',50,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161064,999950,0,0,0,'/home','NavigatorGroup2','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRvNE1UUTFOakJpTTJNNFl6UTBaak0yTjJRelltTXdOR1l6T0RVME1tRm1Zdw',1,1,23,'',50,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161065,999950,0,0,0,'/home','NavigatorGroup2','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRvNE1UUTFOakJpTTJNNFl6UTBaak0yTjJRelltTXdOR1l6T0RVME1tRm1Zdw',1,1,23,'',50,0,1,1,0,1,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161066,999950,0,0,0,'/home','NavigatorGroup2','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwbVl6UmhPVFZrT1RZelpqaGhabVV6TnpVME5UaGpaR0l5WVdZMU9UVmtOZw',1,1,46,'',60,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161067,999950,0,0,0,'/home','NavigatorGroup2','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwbVl6UmhPVFZrT1RZelpqaGhabVV6TnpVME5UaGpaR0l5WVdZMU9UVmtOZw',1,1,46,'',60,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161068,999950,0,0,0,'/home','NavigatorGroup2','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwbVl6UmhPVFZrT1RZelpqaGhabVV6TnpVME5UaGpaR0l5WVdZMU9UVmtOZw',1,1,46,'',60,0,1,1,0,1,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161069,999950,0,0,0,'/home','NavigatorGroup2','会议室','会议室','cs://1/image/aW1hZ2UvTVRvelpqZG1NVGRpWW1VelpXUXhOREpsTW1Jd1pHTmtPRFptTVdOak5qaGlOdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',70,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161070,999950,0,0,0,'/home','NavigatorGroup2','会议室','会议室','cs://1/image/aW1hZ2UvTVRvelpqZG1NVGRpWW1VelpXUXhOREpsTW1Jd1pHTmtPRFptTVdOak5qaGlOdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',70,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161071,999950,0,0,0,'/home','NavigatorGroup2','会议室','会议室','cs://1/image/aW1hZ2UvTVRvelpqZG1NVGRpWW1VelpXUXhOREpsTW1Jd1pHTmtPRFptTVdOak5qaGlOdw',1,1,14,'{"url":"${home.url}/mobile/static/coming_soon/index.html"}',70,0,1,1,0,1,'default',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161072,999950,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRvMllUZ3pNR1l6TURNM05URTJOMkUwWkRsbU9HWTJNMlJsWWpZeE5tSmxZdw',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES (161073,999950,0,0,0,'/home','NavigatorGroup2','更多','更多','cs://1/image/aW1hZ2UvTVRvMllUZ3pNR1l6TURNM05URTJOMkUwWkRsbU9HWTJNMlJsWWpZeE5tSmxZdw',1,1,1,'{"itemLocation":"/home","itemGroup":"NavigatorGroup2"}',1000,0,1,1,0,1,'park_tourist',0,0,'');

INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES (204147,6,0,'物业报修','任务/物业报修',1,2,UTC_TIMESTAMP(),999950);
    
    
set @namespaceId = 999950;
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

   
-- 添加服务市场
-- 新增个人服务，企业服务，场地租赁二层入口
SET @eh_launch_pad_layouts_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'PersonalServerLayout','{"versionCode":"2018022501","layoutName":"PersonalServerLayout","displayName":"个人服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'PersonalServerLayout','{"versionCode":"2018022501","layoutName":"PersonalServerLayout","displayName":"个人服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'PersonalServerLayout','{"versionCode":"2018022501","layoutName":"PersonalServerLayout","displayName":"个人服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'default',0,0,0);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'EnterpriseServerLayout','{"versionCode":"2018022501","layoutName":"EnterpriseServerLayout","displayName":"企业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'EnterpriseServerLayout','{"versionCode":"2018022501","layoutName":"EnterpriseServerLayout","displayName":"企业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'EnterpriseServerLayout','{"versionCode":"2018022501","layoutName":"EnterpriseServerLayout","displayName":"企业服务","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'default',0,0,0);

INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'RentLayout','{"versionCode":"2018022501","layoutName":"RentLayout","displayName":"场地租赁","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'pm_admin',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'RentLayout','{"versionCode":"2018022501","layoutName":"RentLayout","displayName":"场地租赁","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'park_tourist',0,0,0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`) 
	VALUES ((@eh_launch_pad_layouts_id := @eh_launch_pad_layouts_id + 1),999950,'RentLayout','{"versionCode":"2018022501","layoutName":"RentLayout","displayName":"场地租赁","groups":[{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"NavigatorGroup1"},"style":"Default","defaultOrder":20,"separatorFlag":0,"separatorHeight":0,"columnCount":4}]}',2018022501,0,2,UTC_TIMESTAMP(),'default',0,0,0);

-- 更新首页个人服务,企业服务,场地租赁为二层入口
update `eh_launch_pad_items` set action_type = '2',action_data = '{"itemLocation":"/home/PersonalServer","layoutName":"PersonalServerLayout","title":"个人服务","entityTag":"PM"}' where item_name ='个人服务' and namespace_id = 999950;
update `eh_launch_pad_items` set action_type = '2',action_data = '{"itemLocation":"/home/EnterpriseServer","layoutName":"EnterpriseServerLayout","title":"企业服务","entityTag":"PM"}' where item_name ='企业服务' and namespace_id = 999950;
update `eh_launch_pad_items` set action_type = '2',action_data = '{"itemLocation":"/home/Rent","layoutName":"RentLayout","title":"场地租赁","entityTag":"PM"}' where item_name ='场地租赁' and namespace_id = 999950;

-- 更新更多类型
update `eh_launch_pad_items` set action_type = '53' where item_name ='更多' and namespace_id = 999950;

-- 增加更多的分类
-- 1，个人服务
SET @eh_item_service_categries_id = (SELECT MAX(id) FROM `eh_item_service_categries`);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup1','',10,0,1,999950,'pm_admin','个人服务','/home','NavigatorGroup2',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup1','',10,0,1,999950,'park_tourist','个人服务','/home','NavigatorGroup2',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup1','',10,0,1,999950,'default','个人服务','/home','NavigatorGroup2',0,0,6,0);
-- 2，物业服务
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup2','',20,0,1,999950,'pm_admin','物业服务','/home','NavigatorGroup2',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup2','',20,0,1,999950,'park_tourist','物业服务','/home','NavigatorGroup2',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup2','',20,0,1,999950,'default','物业服务','/home','NavigatorGroup2',0,0,6,0);
-- 3，场地租赁
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup3','',30,0,1,999950,'pm_admin','场地租赁','/home','NavigatorGroup2',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup3','',30,0,1,999950,'park_tourist','场地租赁','/home','NavigatorGroup2',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup3','',30,0,1,999950,'default','场地租赁','/home','NavigatorGroup2',0,0,6,0);
    
-- 4，智能食堂
-- INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
-- 	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup4','',40,0,1,999950,'pm_admin','智能食堂','/home','NavigatorGroup2',0,0,5,0);
-- INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
-- 	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup4','',40,0,1,999950,'park_tourist','智能食堂','/home','NavigatorGroup2',0,0,1,0);
-- INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
-- 	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup4','',40,0,1,999950,'default','智能食堂','/home','NavigatorGroup2',0,0,6,0);
-- 5，园区商家
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup5','',50,0,1,999950,'pm_admin','园区商家','/home','NavigatorGroup2',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup5','',50,0,1,999950,'park_tourist','园区商家','/home','NavigatorGroup2',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup5','',50,0,1,999950,'default','园区商家','/home','NavigatorGroup2',0,0,6,0);
-- 6，园区党建
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup6','',60,0,1,999950,'pm_admin','园区党建','/home','NavigatorGroup2',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup6','',60,0,1,999950,'park_tourist','园区党建','/home','NavigatorGroup2',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup6','',60,0,1,999950,'default','园区党建','/home','NavigatorGroup2',0,0,6,0);
-- 7，缴费服务
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup7','',70,0,1,999950,'pm_admin','缴费服务','/home','NavigatorGroup2',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup7','',70,0,1,999950,'park_tourist','缴费服务','/home','NavigatorGroup2',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup7','',70,0,1,999950,'default','缴费服务','/home','NavigatorGroup2',0,0,6,0);
-- 8，园区服务
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup8','',80,0,1,999950,'pm_admin','园区服务','/home','NavigatorGroup2',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup8','',80,0,1,999950,'park_tourist','园区服务','/home','NavigatorGroup2',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup8','',80,0,1,999950,'default','园区服务','/home','NavigatorGroup2',0,0,6,0);
-- 9，企业服务
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup9','',90,0,1,999950,'pm_admin','企业服务','/home','NavigatorGroup2',0,0,5,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup9','',90,0,1,999950,'park_tourist','企业服务','/home','NavigatorGroup2',0,0,1,0);
INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup9','',90,0,1,999950,'default','企业服务','/home','NavigatorGroup2',0,0,6,0);
-- 10，OA服务
-- INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
-- 	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup10','',100,0,1,999950,'pm_admin','OA服务','/home','NavigatorGroup2',0,0,5,0);
-- INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
-- 	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup10','',100,0,1,999950,'park_tourist','OA服务','/home','NavigatorGroup2',0,0,1,0);
-- INSERT INTO `eh_item_service_categries` (`id`, `name`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, `scene_type`, `label`, `item_location`, `item_group`, `operator_uid`, `creator_uid`,`scope_code`, `scope_id`) 
-- 	VALUES ((@eh_item_service_categries_id := @eh_item_service_categries_id + 1),'moreGroup10','',100,0,1,999950,'default','OA服务','/home','NavigatorGroup2',0,0,6,0);

-- 更新首页应用的分组
-- 物业缴费
update eh_launch_pad_items set categry_name = 'moreGroup7' ,more_order = '10',action_data='{"url":"${home.url}/property-payment/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0&name=1#/home_page#sign_suffix"}' where item_name = '物业缴费' and namespace_id= 999950;
-- 物业报修
update eh_launch_pad_items set categry_name = 'moreGroup2' ,more_order = '10' where item_name = '物业报修' and namespace_id= 999950;
-- 服务热线
update eh_launch_pad_items set categry_name = 'moreGroup8' ,more_order = '50' where item_name = '服务热线' and namespace_id= 999950;
-- 园区介绍
update eh_launch_pad_items set categry_name = 'moreGroup8' ,more_order = '70' where item_name = '园区介绍' and namespace_id= 999950;
-- 打卡考勤
update eh_launch_pad_items set categry_name = 'moreGroup1' ,more_order = '10' where item_name = '打卡考勤' and namespace_id= 999950;
-- 通讯录
update eh_launch_pad_items set categry_name = 'moreGroup1' ,more_order = '10' where item_name = '通讯录' and namespace_id= 999950;
-- 会议室
update eh_launch_pad_items set categry_name = 'moreGroup8' ,more_order = '10' where item_name = '会议室' and namespace_id= 999950;

-- 添加其它应用
-- 打卡考勤添加到个人服务（二层入口）
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRvNE1UUTFOakJpTTJNNFl6UTBaak0yTjJRelltTXdOR1l6T0RVME1tRm1Zdw',1,1,23,'',10,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRvNE1UUTFOakJpTTJNNFl6UTBaak0yTjJRelltTXdOR1l6T0RVME1tRm1Zdw',1,1,23,'',10,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','打卡考勤','打卡考勤','cs://1/image/aW1hZ2UvTVRvNE1UUTFOakJpTTJNNFl6UTBaak0yTjJRelltTXdOR1l6T0RVME1tRm1Zdw',1,1,23,'',10,0,1,1,0,1,'default',0,0,'');


-- 我的任务
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRvNVpHRmlZek00WkdWaE5XVXhPR1pqTW1NMU9HVmhaV1F5TVRrMFltUXlaUQ',1,1,56,'',90,0,1,0,0,1,'pm_admin',0,20,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRvNVpHRmlZek00WkdWaE5XVXhPR1pqTW1NMU9HVmhaV1F5TVRrMFltUXlaUQ',1,1,56,'',90,0,1,0,0,1,'park_tourist',0,20,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRvNVpHRmlZek00WkdWaE5XVXhPR1pqTW1NMU9HVmhaV1F5TVRrMFltUXlaUQ',1,1,56,'',90,0,1,0,0,1,'default',0,20,'moreGroup1');

-- 我的任务加到个人服务（二层入口）
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRvNVpHRmlZek00WkdWaE5XVXhPR1pqTW1NMU9HVmhaV1F5TVRrMFltUXlaUQ',1,1,56,'',20,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRvNVpHRmlZek00WkdWaE5XVXhPR1pqTW1NMU9HVmhaV1F5TVRrMFltUXlaUQ',1,1,56,'',20,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','我的任务','我的任务','cs://1/image/aW1hZ2UvTVRvNVpHRmlZek00WkdWaE5XVXhPR1pqTW1NMU9HVmhaV1F5TVRrMFltUXlaUQ',1,1,56,'',20,0,1,1,0,1,'default',0,0,'');

-- 通讯录加到个人服务（二层入口）
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwbVl6UmhPVFZrT1RZelpqaGhabVV6TnpVME5UaGpaR0l5WVdZMU9UVmtOZw',1,1,46,'',30,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwbVl6UmhPVFZrT1RZelpqaGhabVV6TnpVME5UaGpaR0l5WVdZMU9UVmtOZw',1,1,46,'',30,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','通讯录','通讯录','cs://1/image/aW1hZ2UvTVRwbVl6UmhPVFZrT1RZelpqaGhabVV6TnpVME5UaGpaR0l5WVdZMU9UVmtOZw',1,1,46,'',30,0,1,1,0,1,'default',0,0,'');
    
-- 我的店铺
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','我的店铺','我的店铺','cs://1/image/aW1hZ2UvTVRvek5ETmpOMlpsTUdObE56ZzVNR013TVRBek1qUTFaRFF4TURaaU1HUmlaQQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',100,0,1,0,0,1,'pm_admin',0,40,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','我的店铺','我的店铺','cs://1/image/aW1hZ2UvTVRvek5ETmpOMlpsTUdObE56ZzVNR013TVRBek1qUTFaRFF4TURaaU1HUmlaQQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',100,0,1,0,0,1,'park_tourist',0,40,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','我的店铺','我的店铺','cs://1/image/aW1hZ2UvTVRvek5ETmpOMlpsTUdObE56ZzVNR013TVRBek1qUTFaRFF4TURaaU1HUmlaQQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',100,0,1,0,0,1,'default',0,40,'moreGroup1');
-- 我的店铺加到个人服务（二层入口）  
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','我的店铺','我的店铺','cs://1/image/aW1hZ2UvTVRvek5ETmpOMlpsTUdObE56ZzVNR013TVRBek1qUTFaRFF4TURaaU1HUmlaQQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',40,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','我的店铺','我的店铺','cs://1/image/aW1hZ2UvTVRvek5ETmpOMlpsTUdObE56ZzVNR013TVRBek1qUTFaRFF4TURaaU1HUmlaQQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',40,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','我的店铺','我的店铺','cs://1/image/aW1hZ2UvTVRvek5ETmpOMlpsTUdObE56ZzVNR013TVRBek1qUTFaRFF4TURaaU1HUmlaQQ',1,1,13,'{"url":"${prefix.url}${business.detail.url}"}',40,0,1,1,0,1,'default',0,0,'');
  
-- 门禁
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','门禁','门禁','cs://1/image/aW1hZ2UvTVRwaE9UVXdNV1k0T1RGaU5EY3dPVFV6TWpjM01UQTJNakExWlRjM01HWTJPUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',110,0,1,0,0,1,'pm_admin',0,50,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','门禁','门禁','cs://1/image/aW1hZ2UvTVRwaE9UVXdNV1k0T1RGaU5EY3dPVFV6TWpjM01UQTJNakExWlRjM01HWTJPUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',110,0,1,0,0,1,'park_tourist',0,50,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','门禁','门禁','cs://1/image/aW1hZ2UvTVRwaE9UVXdNV1k0T1RGaU5EY3dPVFV6TWpjM01UQTJNakExWlRjM01HWTJPUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',110,0,1,0,0,1,'default',0,50,'moreGroup1');
    
-- 门禁到个人服务（二层入口）
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','门禁','门禁','cs://1/image/aW1hZ2UvTVRwaE9UVXdNV1k0T1RGaU5EY3dPVFV6TWpjM01UQTJNakExWlRjM01HWTJPUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',50,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','门禁','门禁','cs://1/image/aW1hZ2UvTVRwaE9UVXdNV1k0T1RGaU5EY3dPVFV6TWpjM01UQTJNakExWlRjM01HWTJPUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',50,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/PersonalServer','NavigatorGroup1','门禁','门禁','cs://1/image/aW1hZ2UvTVRwaE9UVXdNV1k0T1RGaU5EY3dPVFV6TWpjM01UQTJNakExWlRjM01HWTJPUQ',1,1,40,'{"isSupportQR":1,"isSupportSmart":1,"isHighlight":1}',50,0,1,1,0,1,'default',0,0,'');

    
-- 保洁服务
set @eh_service_alliance_categories_id=(SELECT MAX(id) FROM `eh_service_alliance_categories`); 
set @eh_service_alliances_id=(SELECT MAX(id) FROM `eh_service_alliances`); 
set @eh_service_alliance_skip_rule_id = (select max(id) from eh_service_alliance_skip_rule);
set @eh_web_menu_scopes_id = (select max(id) from eh_web_menu_scopes);

INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`, `display_mode`, `display_destination`, `selected_logo_url`, `entry_id`) 
    VALUES ((@eh_service_alliance_categories_id := @eh_service_alliance_categories_id + 1), 'community', '240111044331055940', '0', '保洁服务', '保洁服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999950, '', '1', '0', '', '1');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`, `summary_description`, `enable_comment`) 
    VALUES ((@eh_service_alliances_id := @eh_service_alliances_id + 1), '0', 'organaization', 1041514, 'all', '保洁服务', '保洁服务', @eh_service_alliance_categories_id, '', NULL, '', '','2', @eh_service_alliances_id, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', NULL, '2', '1', '', '0');
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
    VALUES ((@eh_service_alliance_skip_rule_id:=@eh_service_alliance_skip_rule_id+1), 999950, @eh_service_alliance_categories_id);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41700', '保洁服务', 'EhNamespaces', 999950, '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41710', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41720', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41730', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41740', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41750', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41760', '', 'EhNamespaces', 999950, '2');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','保洁服务','保洁服务','cs://1/image/aW1hZ2UvTVRvd09EbGhaamMzTkdSa01ETmpOVFF3WlRBeE56Y3paV00wT0RSbU56QmpNZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),110,0,1,0,0,1,'pm_admin',0,50,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','保洁服务','保洁服务','cs://1/image/aW1hZ2UvTVRvd09EbGhaamMzTkdSa01ETmpOVFF3WlRBeE56Y3paV00wT0RSbU56QmpNZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),110,0,1,0,0,1,'park_tourist',0,50,'moreGroup1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','保洁服务','保洁服务','cs://1/image/aW1hZ2UvTVRvd09EbGhaamMzTkdSa01ETmpOVFF3WlRBeE56Y3paV00wT0RSbU56QmpNZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),110,0,1,0,0,1,'default',0,50,'moreGroup1');  
-- 装修入伙
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`, `display_mode`, `display_destination`, `selected_logo_url`, `entry_id`) 
    VALUES ((@eh_service_alliance_categories_id := @eh_service_alliance_categories_id + 1), 'community', '240111044331055940', '0', '装修入伙', '装修入伙', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999950, '', '1', '0', '', '2');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`, `summary_description`, `enable_comment`) 
    VALUES ((@eh_service_alliances_id := @eh_service_alliances_id + 1), '0', 'organaization', 1041514, 'all', '装修入伙', '装修入伙', @eh_service_alliance_categories_id, '', NULL, '', '','2', @eh_service_alliances_id, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', NULL, '2', '1', '', '0');
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) 
    VALUES ((@eh_service_alliance_skip_rule_id:=@eh_service_alliance_skip_rule_id+1), 999950, @eh_service_alliance_categories_id);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41800', '装修入伙', 'EhNamespaces', 999950, '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41810', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41820', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41830', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41840', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41850', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41860', '', 'EhNamespaces', 999950, '2');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','装修入伙','装修入伙','cs://1/image/aW1hZ2UvTVRvellUUTJNalpoT1dObFpURTNZalpoTmpJeU1UWTNabUkzTWpVMk5qRTBPUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),120,0,1,0,0,1,'pm_admin',0,30,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','装修入伙','装修入伙','cs://1/image/aW1hZ2UvTVRvellUUTJNalpoT1dObFpURTNZalpoTmpJeU1UWTNabUkzTWpVMk5qRTBPUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),120,0,1,0,0,1,'park_tourist',0,30,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','装修入伙','装修入伙','cs://1/image/aW1hZ2UvTVRvellUUTJNalpoT1dObFpURTNZalpoTmpJeU1UWTNabUkzTWpVMk5qRTBPUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),120,0,1,0,0,1,'default',0,30,'moreGroup2');  
-- 设备巡检
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','设备巡检','设备巡检','cs://1/image/aW1hZ2UvTVRvM1lqQXdNekV4TldabE5qVTNaVGxtTVRCallqUTVOMkUwWXpFeE1EVmhaZw',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',130,0,1,0,0,1,'pm_admin',0,40,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','设备巡检','设备巡检','cs://1/image/aW1hZ2UvTVRvM1lqQXdNekV4TldabE5qVTNaVGxtTVRCallqUTVOMkUwWXpFeE1EVmhaZw',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',130,0,1,0,0,1,'park_tourist',0,40,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','设备巡检','设备巡检','cs://1/image/aW1hZ2UvTVRvM1lqQXdNekV4TldabE5qVTNaVGxtTVRCallqUTVOMkUwWXpFeE1EVmhaZw',1,1,14,'{"url":"${home.url}/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}',130,0,1,0,0,1,'default',0,40,'moreGroup2');  
-- 能耗管理
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','能耗管理','能耗管理','cs://1/image/aW1hZ2UvTVRvd1l6RTRNbVV5T1RBNE56VmpOVEk0WW1GbE1qRXdaRGRrT0RRM1pEVTFNZw',1,1,13,'{"url":"${home.url}/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}',140,0,1,0,0,1,'pm_admin',0,50,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','能耗管理','能耗管理','cs://1/image/aW1hZ2UvTVRvd1l6RTRNbVV5T1RBNE56VmpOVEk0WW1GbE1qRXdaRGRrT0RRM1pEVTFNZw',1,1,13,'{"url":"${home.url}/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}',140,0,1,0,0,1,'park_tourist',0,50,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','能耗管理','能耗管理','cs://1/image/aW1hZ2UvTVRvd1l6RTRNbVV5T1RBNE56VmpOVEk0WW1GbE1qRXdaRGRrT0RRM1pEVTFNZw',1,1,13,'{"url":"${home.url}/energy-management/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}',140,0,1,0,0,1,'default',0,50,'moreGroup2');      
-- 投诉建议
set @eh_categories_id = (select max(id) from eh_categories);
INSERT INTO `eh_categories` ( `id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`,`namespace_id`) 
	VALUES ((@eh_categories_id := @eh_categories_id + 1),9,0,'投诉建议','任务/投诉建议',1,2,UTC_TIMESTAMP(),999950);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','投诉建议','投诉建议','cs://1/image/aW1hZ2UvTVRvM05EZG1OVFF3WmpsallUSTNOREE0WWpRNE9UYzBZMlprTnpGbE1UTTVNZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉建议"}',150,0,1,0,0,1,'pm_admin',0,60,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','投诉建议','投诉建议','cs://1/image/aW1hZ2UvTVRvM05EZG1OVFF3WmpsallUSTNOREE0WWpRNE9UYzBZMlprTnpGbE1UTTVNZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉建议"}',150,0,1,0,0,1,'park_tourist',0,60,'moreGroup2');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','投诉建议','投诉建议','cs://1/image/aW1hZ2UvTVRvM05EZG1OVFF3WmpsallUSTNOREE0WWpRNE9UYzBZMlprTnpGbE1UTTVNZw',1,1,60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=9&displayName=投诉建议"}',150,0,1,0,0,1,'default',0,60,'moreGroup2');  
-- 公寓出租
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','公寓出租','公寓出租','cs://1/image/aW1hZ2UvTVRvelpqQXdZMlV5TURJNE1XVXdPVE15TWpkaE1HUTNObVprTVRBNE5UY3lPQQ',1,1,28,'',160,0,1,0,0,1,'pm_admin',0,10,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','公寓出租','公寓出租','cs://1/image/aW1hZ2UvTVRvelpqQXdZMlV5TURJNE1XVXdPVE15TWpkaE1HUTNObVprTVRBNE5UY3lPQQ',1,1,28,'',160,0,1,0,0,1,'park_tourist',0,10,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','公寓出租','公寓出租','cs://1/image/aW1hZ2UvTVRvelpqQXdZMlV5TURJNE1XVXdPVE15TWpkaE1HUTNObVprTVRBNE5UY3lPQQ',1,1,28,'',160,0,1,0,0,1,'default',0,10,'moreGroup3');  
-- 厂房出租
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','厂房出租','厂房出租','cs://1/image/aW1hZ2UvTVRvMFl6aGpaVGMwTkdZMU9ERXpaR1kxTVdVME56YzJOVEpoWVdFd056TTRZZw',1,1,28,'{"categoryId":2}',170,0,1,0,0,1,'pm_admin',0,10,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','厂房出租','厂房出租','cs://1/image/aW1hZ2UvTVRvMFl6aGpaVGMwTkdZMU9ERXpaR1kxTVdVME56YzJOVEpoWVdFd056TTRZZw',1,1,28,'{"categoryId":2}',170,0,1,0,0,1,'park_tourist',0,10,'moreGroup3');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','厂房出租','厂房出租','cs://1/image/aW1hZ2UvTVRvMFl6aGpaVGMwTkdZMU9ERXpaR1kxTVdVME56YzJOVEpoWVdFd056TTRZZw',1,1,28,'{"categoryId":2}',170,0,1,0,0,1,'default',0,10,'moreGroup3');  

-- 公寓出租添加到场地租赁（二层）
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/Rent','NavigatorGroup1','公寓出租','公寓出租','cs://1/image/aW1hZ2UvTVRvelpqQXdZMlV5TURJNE1XVXdPVE15TWpkaE1HUTNObVprTVRBNE5UY3lPQQ',1,1,28,'',10,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/Rent','NavigatorGroup1','公寓出租','公寓出租','cs://1/image/aW1hZ2UvTVRvelpqQXdZMlV5TURJNE1XVXdPVE15TWpkaE1HUTNObVprTVRBNE5UY3lPQQ',1,1,28,'',10,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/Rent','NavigatorGroup1','公寓出租','公寓出租','cs://1/image/aW1hZ2UvTVRvelpqQXdZMlV5TURJNE1XVXdPVE15TWpkaE1HUTNObVprTVRBNE5UY3lPQQ',1,1,28,'',10,0,1,1,0,1,'default',0,0,'');  
-- 厂房出租    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/Rent','NavigatorGroup1','厂房出租','厂房出租','cs://1/image/aW1hZ2UvTVRvMFl6aGpaVGMwTkdZMU9ERXpaR1kxTVdVME56YzJOVEpoWVdFd056TTRZZw',1,1,28,'{"categoryId":2}',20,0,1,0,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/Rent','NavigatorGroup1','厂房出租','厂房出租','cs://1/image/aW1hZ2UvTVRvMFl6aGpaVGMwTkdZMU9ERXpaR1kxTVdVME56YzJOVEpoWVdFd056TTRZZw',1,1,28,'{"categoryId":2}',20,0,1,0,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/Rent','NavigatorGroup1','厂房出租','厂房出租','cs://1/image/aW1hZ2UvTVRvMFl6aGpaVGMwTkdZMU9ERXpaR1kxTVdVME56YzJOVEpoWVdFd056TTRZZw',1,1,28,'{"categoryId":2}',20,0,1,0,0,1,'default',0,0,'');  
    
-- 商城
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','商城','商城','cs://1/image/aW1hZ2UvTVRvek1EWXlZbVV4TkRjNE1HVTVNVE0wT1dKaU56TTJNR1JsTnpZNU5EWmtPQQ',1,1,13,'{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix"}',200,0,1,0,0,1,'pm_admin',0,10,'moreGroup5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','商城','商城','cs://1/image/aW1hZ2UvTVRvek1EWXlZbVV4TkRjNE1HVTVNVE0wT1dKaU56TTJNR1JsTnpZNU5EWmtPQQ',1,1,13,'{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix"}',200,0,1,0,0,1,'park_tourist',0,10,'moreGroup5');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','商城','商城','cs://1/image/aW1hZ2UvTVRvek1EWXlZbVV4TkRjNE1HVTVNVE0wT1dKaU56TTJNR1JsTnpZNU5EWmtPQQ',1,1,13,'{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix"}',200,0,1,0,0,1,'default',0,10,'moreGroup5');  
-- 党群服务
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`, `display_mode`, `display_destination`, `selected_logo_url`, `entry_id`) 
    VALUES ((@eh_service_alliance_categories_id := @eh_service_alliance_categories_id + 1), 'community', '240111044331055940', '0', '党群服务', '党群服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999950, '', '1', '0', '', '3');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`, `summary_description`, `enable_comment`) 
    VALUES ((@eh_service_alliances_id := @eh_service_alliances_id + 1), '0', 'organaization', 1041514, 'all', '党群服务', '党群服务', @eh_service_alliance_categories_id, '', NULL, '', '','2', @eh_service_alliances_id, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', NULL, '2', '1', '', '0');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41900', '党群服务', 'EhNamespaces', 999950, '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41910', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41920', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41930', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41940', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41950', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '41960', '', 'EhNamespaces', 999950, '2');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','党群服务','党群服务','cs://1/image/aW1hZ2UvTVRveVpqRTNNek5sWTJVNU9HTXdOREZsT1RkbE1HUXdZamhpTURVelpqY3lNQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),210,0,1,0,0,1,'pm_admin',0,10,'moreGroup6');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','党群服务','党群服务','cs://1/image/aW1hZ2UvTVRveVpqRTNNek5sWTJVNU9HTXdOREZsT1RkbE1HUXdZamhpTURVelpqY3lNQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),210,0,1,0,0,1,'park_tourist',0,10,'moreGroup6');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','党群服务','党群服务','cs://1/image/aW1hZ2UvTVRveVpqRTNNek5sWTJVNU9HTXdOREZsT1RkbE1HUXdZamhpTURVelpqY3lNQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),210,0,1,0,0,1,'default',0,10,'moreGroup6');  

-- 场地预订
set @eh_rentalv2_resource_types_id = (select max(id) from eh_rentalv2_resource_types);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES ((@eh_rentalv2_resource_types_id := @eh_rentalv2_resource_types_id +1), '场地预订', 0, NULL, 2, 999950);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','场地预订','场地预订','cs://1/image/aW1hZ2UvTVRvM05EZG1OVFF3WmpsallUSTNOREE0WWpRNE9UYzBZMlprTnpGbE1UTTVNZw',1,1,49,CONCAT('{"resourceTypeId":',@eh_rentalv2_resource_types_id,',"pageType":0}'),220,0,1,0,0,1,'pm_admin',0,20,'moreGroup8');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','场地预订','场地预订','cs://1/image/aW1hZ2UvTVRvM05EZG1OVFF3WmpsallUSTNOREE0WWpRNE9UYzBZMlprTnpGbE1UTTVNZw',1,1,49,CONCAT('{"resourceTypeId":',@eh_rentalv2_resource_types_id,',"pageType":0}'),220,0,1,0,0,1,'park_tourist',0,20,'moreGroup8');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','场地预订','场地预订','cs://1/image/aW1hZ2UvTVRvM05EZG1OVFF3WmpsallUSTNOREE0WWpRNE9UYzBZMlprTnpGbE1UTTVNZw',1,1,49,CONCAT('{"resourceTypeId":',@eh_rentalv2_resource_types_id,',"pageType":0}'),220,0,1,0,0,1,'default',0,20,'moreGroup8');  
-- 工位预订
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','工位预订','工位预订','cs://1/image/aW1hZ2UvTVRvMFpqazBPVE5sWWpJNVlUWXpNekZrTWpRME1qSmpNamRpTmpVNFlURXdZZw',1,1,13,'{"url":"${home.url}/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}',230,0,1,0,0,1,'pm_admin',0,30,'moreGroup8');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','工位预订','工位预订','cs://1/image/aW1hZ2UvTVRvMFpqazBPVE5sWWpJNVlUWXpNekZrTWpRME1qSmpNamRpTmpVNFlURXdZZw',1,1,13,'{"url":"${home.url}/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}',230,0,1,0,0,1,'park_tourist',0,30,'moreGroup8');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','工位预订','工位预订','cs://1/image/aW1hZ2UvTVRvMFpqazBPVE5sWWpJNVlUWXpNekZrTWpRME1qSmpNamRpTmpVNFlURXdZZw',1,1,13,'{"url":"${home.url}/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}',230,0,1,0,0,1,'default',0,30,'moreGroup8');  
-- 新增菜单
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '40200', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '40500', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '10800', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '40210', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '40220', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '50600', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '50601', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '50602', '', 'EhNamespaces', 999950, '2');

        
-- 咨询求助
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`, `display_mode`, `display_destination`, `selected_logo_url`, `entry_id`) 
    VALUES ((@eh_service_alliance_categories_id := @eh_service_alliance_categories_id + 1), 'community', '240111044331055940', '0', '咨询求助', '咨询求助', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999950, '', '1', '0', '', '4');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`, `summary_description`, `enable_comment`) 
    VALUES ((@eh_service_alliances_id := @eh_service_alliances_id + 1), '0', 'organaization', 1041514, 'all', '咨询求助', '咨询求助', @eh_service_alliance_categories_id, '', NULL, '', '','2', @eh_service_alliances_id, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', NULL, '2', '1', '', '0');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42000', '咨询求助', 'EhNamespaces', 999950, '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42010', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42020', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42030', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42040', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42050', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42060', '', 'EhNamespaces', 999950, '2');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','咨询求助','咨询求助','cs://1/image/aW1hZ2UvTVRvMll6QmtOekUyWkRaaU1ESXdOakE1TVdNeE5qbGpObUUxTURNek56RTFaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),240,0,1,0,0,1,'pm_admin',0,60,'moreGroup8');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','咨询求助','咨询求助','cs://1/image/aW1hZ2UvTVRvMll6QmtOekUyWkRaaU1ESXdOakE1TVdNeE5qbGpObUUxTURNek56RTFaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),240,0,1,0,0,1,'park_tourist',0,60,'moreGroup8');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','咨询求助','咨询求助','cs://1/image/aW1hZ2UvTVRvMll6QmtOekUyWkRaaU1ESXdOakE1TVdNeE5qbGpObUUxTURNek56RTFaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),240,0,1,0,0,1,'default',0,60,'moreGroup8');  
-- 视频会议
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwaU16SXhOemxrTVRJeVpHTTNNRFk0TTJRME9USmhOamxrWmpCbE5HVXlNUQ',1,1,27,'',250,0,1,0,0,1,'pm_admin',0,10,'moreGroup9');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwaU16SXhOemxrTVRJeVpHTTNNRFk0TTJRME9USmhOamxrWmpCbE5HVXlNUQ',1,1,27,'',250,0,1,0,0,1,'park_tourist',0,10,'moreGroup9');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwaU16SXhOemxrTVRJeVpHTTNNRFk0TTJRME9USmhOamxrWmpCbE5HVXlNUQ',1,1,27,'',250,0,1,0,0,1,'default',0,10,'moreGroup9');  
-- 视频会议添加到企业服务（二层）
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwaU16SXhOemxrTVRJeVpHTTNNRFk0TTJRME9USmhOamxrWmpCbE5HVXlNUQ',1,1,27,'',10,0,1,1,0,1,'pm_admin',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwaU16SXhOemxrTVRJeVpHTTNNRFk0TTJRME9USmhOamxrWmpCbE5HVXlNUQ',1,1,27,'',10,0,1,1,0,1,'park_tourist',0,10,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwaU16SXhOemxrTVRJeVpHTTNNRFk0TTJRME9USmhOamxrWmpCbE5HVXlNUQ',1,1,27,'',10,0,1,1,0,1,'default',0,10,'moreGroup9');  

-- 服务机构
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`, `display_mode`, `display_destination`, `selected_logo_url`, `entry_id`) 
    VALUES ((@eh_service_alliance_categories_id := @eh_service_alliance_categories_id + 1), 'community', '240111044331055940', '0', '服务机构', '服务机构', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999950, '', '1', '0', '', '5');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`, `summary_description`, `enable_comment`) 
    VALUES ((@eh_service_alliances_id := @eh_service_alliances_id + 1), '0', 'organaization', 1041514, 'all', '服务机构', '服务机构', @eh_service_alliance_categories_id, '', NULL, '', '','2', @eh_service_alliances_id, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', NULL, '2', '1', '', '0');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42100', '服务机构', 'EhNamespaces', 999950, '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42110', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42120', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42130', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42140', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42150', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42160', '', 'EhNamespaces', 999950, '2');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','服务机构','服务机构','cs://1/image/aW1hZ2UvTVRwbE0yWXpZVEk1Tm1Rek5EUXdOV013TjJOak9UVXdabVpsTTJWa1ptVmhaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),250,0,1,0,0,1,'pm_admin',0,20,'moreGroup9');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','服务机构','服务机构','cs://1/image/aW1hZ2UvTVRwbE0yWXpZVEk1Tm1Rek5EUXdOV013TjJOak9UVXdabVpsTTJWa1ptVmhaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),250,0,1,0,0,1,'park_tourist',0,20,'moreGroup9');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','服务机构','服务机构','cs://1/image/aW1hZ2UvTVRwbE0yWXpZVEk1Tm1Rek5EUXdOV013TjJOak9UVXdabVpsTTJWa1ptVmhaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),250,0,1,0,0,1,'default',0,20,'moreGroup9');  
-- 服务机构添加到企业服务（二层）
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','服务机构','服务机构','cs://1/image/aW1hZ2UvTVRwbE0yWXpZVEk1Tm1Rek5EUXdOV013TjJOak9UVXdabVpsTTJWa1ptVmhaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),20,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','服务机构','服务机构','cs://1/image/aW1hZ2UvTVRwbE0yWXpZVEk1Tm1Rek5EUXdOV013TjJOak9UVXdabVpsTTJWa1ptVmhaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),20,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','服务机构','服务机构','cs://1/image/aW1hZ2UvTVRwbE0yWXpZVEk1Tm1Rek5EUXdOV013TjJOak9UVXdabVpsTTJWa1ptVmhaZw',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "grid"}'),20,0,1,1,0,1,'default',0,0,'');  

-- 政策服务
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`, `display_mode`, `display_destination`, `selected_logo_url`, `entry_id`) 
    VALUES ((@eh_service_alliance_categories_id := @eh_service_alliance_categories_id + 1), 'community', '240111044331055940', '0', '政策服务', '政策服务', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999950, '', '1', '0', '', '6');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`, `summary_description`, `enable_comment`) 
    VALUES ((@eh_service_alliances_id := @eh_service_alliances_id + 1), '0', 'organaization', 1041514, 'all', '政策服务', '政策服务', @eh_service_alliance_categories_id, '', NULL, '', '','2', @eh_service_alliances_id, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', NULL, '2', '1', '', '0');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42200', '政策服务', 'EhNamespaces', 999950, '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42210', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42220', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42230', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42240', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42250', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42260', '', 'EhNamespaces', 999950, '2');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','政策服务','政策服务','cs://1/image/aW1hZ2UvTVRvM05qRmtaRGN5Wm1KaE1tUmpaR05pWXpaa01XUTVaakUzT0RGbFlXVmlOQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),260,0,1,0,0,1,'pm_admin',0,30,'moreGroup9');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','政策服务','政策服务','cs://1/image/aW1hZ2UvTVRvM05qRmtaRGN5Wm1KaE1tUmpaR05pWXpaa01XUTVaakUzT0RGbFlXVmlOQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),260,0,1,0,0,1,'park_tourist',0,30,'moreGroup9');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','政策服务','政策服务','cs://1/image/aW1hZ2UvTVRvM05qRmtaRGN5Wm1KaE1tUmpaR05pWXpaa01XUTVaakUzT0RGbFlXVmlOQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),260,0,1,0,0,1,'default',0,30,'moreGroup9');  
-- 政策服务添加到企业服务（二层）
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','政策服务','政策服务','cs://1/image/aW1hZ2UvTVRvM05qRmtaRGN5Wm1KaE1tUmpaR05pWXpaa01XUTVaakUzT0RGbFlXVmlOQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),30,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','政策服务','政策服务','cs://1/image/aW1hZ2UvTVRvM05qRmtaRGN5Wm1KaE1tUmpaR05pWXpaa01XUTVaakUzT0RGbFlXVmlOQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),30,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','政策服务','政策服务','cs://1/image/aW1hZ2UvTVRvM05qRmtaRGN5Wm1KaE1tUmpaR05pWXpaa01XUTVaakUzT0RGbFlXVmlOQQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),30,0,1,1,0,1,'default',0,0,'');  

-- 服务联盟
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`, `display_mode`, `display_destination`, `selected_logo_url`, `entry_id`) 
    VALUES ((@eh_service_alliance_categories_id := @eh_service_alliance_categories_id + 1), 'community', '240111044331055940', '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999950, '', '1', '0', '', '7');
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `range`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`, `description_height`, `display_flag`, `summary_description`, `enable_comment`) 
    VALUES ((@eh_service_alliances_id := @eh_service_alliances_id + 1), '0', 'organaization', 1041514, 'all', '服务联盟', '服务联盟', @eh_service_alliance_categories_id, '', NULL, '', '','2', @eh_service_alliances_id, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '2', NULL, '2', '1', '', '0');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42300', '服务联盟', 'EhNamespaces', 999950, '1');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42310', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42320', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42330', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42340', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42350', '', 'EhNamespaces', 999950, '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES ((@eh_web_menu_scopes_id := @eh_web_menu_scopes_id + 1), '42360', '', 'EhNamespaces', 999950, '2');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwbE5HSXpaakF3TkRnM09EQTFZalk0WWpJM01Ea3dObVE1TWpSbU5UaGtZUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),270,0,1,0,0,1,'pm_admin',0,40,'moreGroup9');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwbE5HSXpaakF3TkRnM09EQTFZalk0WWpJM01Ea3dObVE1TWpSbU5UaGtZUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),270,0,1,0,0,1,'park_tourist',0,40,'moreGroup9');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home','NavigatorGroup2','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwbE5HSXpaakF3TkRnM09EQTFZalk0WWpJM01Ea3dObVE1TWpSbU5UaGtZUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),270,0,1,0,0,1,'default',0,40,'moreGroup9');  
-- 服务联盟添加到企业服务（二层）
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwbE5HSXpaakF3TkRnM09EQTFZalk0WWpJM01Ea3dObVE1TWpSbU5UaGtZUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),40,0,1,1,0,1,'pm_admin',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwbE5HSXpaakF3TkRnM09EQTFZalk0WWpJM01Ea3dObVE1TWpSbU5UaGtZUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),40,0,1,1,0,1,'park_tourist',0,0,'');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `bgcolor`, `delete_flag`, `scene_type`, `scale_type`, `more_order`, `categry_name`) 
	VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1),999950,0,0,0,'/home/EnterpriseServer','NavigatorGroup1','服务联盟','服务联盟','cs://1/image/aW1hZ2UvTVRwbE5HSXpaakF3TkRnM09EQTFZalk0WWpJM01Ea3dObVE1TWpSbU5UaGtZUQ',1,1,33,CONCAT('{"type":',@eh_service_alliance_categories_id,',"parentId":',@eh_service_alliance_categories_id,',"displayType": "list"}'),40,0,1,1,0,1,'default',0,0,'');  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
     
