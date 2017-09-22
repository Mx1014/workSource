-- 大沙河建投
SET FOREIGN_KEY_CHECKS = 0;

SET @core_url = 'https://core.zuolin.com'; -- 取具体环境连接core server的链接
SET @biz_url = 'https://biz.zuolin.com'; -- 取具体环境的电商服务器连接，注意有两个域名要修改
-- SET @core_url = 'http://beta.zuolin.com'; -- 取具体环境连接core server的链接
-- SET @biz_url = 'http://biz-beta.zuolin.com'; -- 取具体环境的电商服务器连接，注意有两个域名要修改

SET @namespace_id = 999967;
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1781,'app.agreements.url', CONCAT(@core_url, '/mobile/static/app_agreements/agreements.html?ns=999967'),'the relative path for dashahe app agreements',999967,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1782,'business.url', CONCAT(@biz_url, '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz-beta.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix'),'biz access url for changfazhan',999967,NULL);
INSERT INTO `eh_configurations` ( `id`, `name`, `value`, `description`, `namespace_id`, `display_name` ) 
	VALUES (1783,'pmtask.handler-999967','flow','0',999967,'物业报修工作流');

INSERT INTO `eh_namespaces` (`id`, `name`) 
	VALUES (999967,'大沙河建投');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES (1181,999967,'community_commercial',UTC_TIMESTAMP());
INSERT INTO `eh_namespace_resources` (`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES (20044,999967,'COMMUNITY',240111044332059932,UTC_TIMESTAMP());


INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (161,'Android_DaShaHeJianTou',NULL,UTC_TIMESTAMP(),999967);
INSERT INTO `eh_version_realm` (  `id`, `realm`, `description`, `create_time`, `namespace_id` ) 
	VALUES (162,'iOS_DaShaHeJianTou',NULL,UTC_TIMESTAMP(),999967);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	VALUES (591,161,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) 
	VALUES (592,162,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES (1037001,0,'PM','深圳市大沙河建设投资有限公司','深圳市大沙河创新走廊建设投资管理有限公司（以下简称“大沙河建投公司”）是深圳市南山区为实现大沙河创新走廊区域范围的产业布局、城市更新等规划和建设任务而创立的国有独资企业，注册资本2.5亿元人民币。
　　大沙河建投公司的使命是结合南山区经济社会发展的整体部署，为产业转型升级提供战略空间，为人才引进营造优越环境，努力推进项目开发建设，打造创新型企业总部办公、研发基地和人才公寓，实现大沙河创新走廊的整体规划。
　　公司下设建设工程部、经营发展部、行政部、财务部四个部门。现阶段重点工作是全力推进创新大厦和茶光研发总部园区两个项目的建设，为高科技企业、创新型企业和上市企业提供总部办公和研发用房，为建设宜居宜业的国际化滨海城区贡献力量。','/1037001',1,2,'ENTERPRISE',999967,1057051);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES (1153501,240111044332059932,'organization',1037001,3,0,UTC_TIMESTAMP());
INSERT INTO `eh_organization_communities` (`organization_id`, `community_id`) 
	VALUES (1037001,240111044332059932);

INSERT INTO `eh_organization_members` (id, `namespace_id`, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `group_type`, `group_path`, `detail_id`) 
	VALUES (2169591, 999967, 1037001,'USER',316755,'manager','张守柏',0,'13538020878',3, 'ENTERPRISE', '/1037001', 23501);
INSERT INTO `eh_organization_member_details`(`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`)
	VALUES (23501, 1037001, 'USER', 316755, '张守柏', 0, '13538020878', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`)
	VALUES (24301, 316755, 1037001, '/1037001', 'ENTERPRISE', 3, @namespace_id, UTC_TIMESTAMP(), 0, UTC_TIMESTAMP());

INSERT INTO `eh_organization_members` (id, `namespace_id`, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `group_type`, `group_path`, `detail_id`) 
	VALUES (2169592, 999967,1037001,'USER',316756,'manager','罗伟祥',0,'18998998669',3, 'ENTERPRISE', '/1037001', 23502);
INSERT INTO `eh_organization_member_details`(`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`)
	VALUES (23502, 1037001, 'USER', 316756, '罗伟祥', 0, '18998998669', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`)
	VALUES (24302, 316756, 1037001, '/1037001', 'ENTERPRISE', 3, @namespace_id, UTC_TIMESTAMP(), 0, UTC_TIMESTAMP());
	
INSERT INTO `eh_organization_members` (id, `namespace_id`, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `group_type`, `group_path`, `detail_id`) 
	VALUES (2169593, 999967,1037001,'USER',316757,'manager','冯小勇',0,'13560749929',3, 'ENTERPRISE', '/1037001', 23503);
INSERT INTO `eh_organization_member_details`(`id`, `organization_id`, `target_type`, `target_id`, `contact_name`, `contact_type`, `contact_token`, `check_in_time`, `namespace_id`)
	VALUES (23503, 1037001, 'USER', 316757, '冯小勇', 0, '13560749929', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`)
	VALUES (24303, 316757, 1037001, '/1037001', 'ENTERPRISE', 3, @namespace_id, UTC_TIMESTAMP(), 0, UTC_TIMESTAMP());	

	
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192501,UUID(),999967,2,'EhGroups',1057051,'深圳市大沙河建设投资有限公司论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`) 
	VALUES (1057051,UUID(),'深圳市大沙河建设投资有限公司','深圳市大沙河建设投资有限公司',1,1,1037001,'enterprise',1,1,UTC_TIMESTAMP(),UTC_TIMESTAMP(),192501,1,999967);

	
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (316755,UUID(),'19132843868','张守柏',NULL,1,45,'1','1','zh_CN','707f93b0e9dd9733548b0e05e739a4db','f5839211ae90889e02c1eac3be3a25a5f2c6bb8ff54cd71b37e9b4683a9b1627',UTC_TIMESTAMP(),999967);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (316756,UUID(),'19132843869','罗伟祥',NULL,1,45,'1','1','zh_CN','2683228e9f2f6b5d01bd1e6a44bb87ae','19ac08d2417d6288b63653f8b921a52458cd88c9f3b97d39cafa9cecb89774f0',UTC_TIMESTAMP(),999967);
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`) 
	VALUES (316757,UUID(),'19132843870','冯小勇',NULL,1,45,'1','1','zh_CN','3620513f931767eff08770f4a6e7df09','bdca182713086a5927f088b59b31a9c4347f143576ba2d18ca9a3c2eddedc604',UTC_TIMESTAMP(),999967);

INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (289360,316755,0,'13538020878',NULL,3,UTC_TIMESTAMP(),999967);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (289361,316756,0,'18998998669',NULL,3,UTC_TIMESTAMP(),999967);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`) 
	VALUES (289362,316757,0,'13560749929',NULL,3,UTC_TIMESTAMP(),999967);


INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (23151,'EhOrganizations',1037001,1,10,316755,0,1,UTC_TIMESTAMP(),999967,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (23152,'EhOrganizations',1037001,1,10,316756,0,1,UTC_TIMESTAMP(),999967,'EhUsers','admin');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`) 
	VALUES (23153,'EhOrganizations',1037001,1,10,316757,0,1,UTC_TIMESTAMP(),999967,'EhUsers','admin');
		
	
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (19601,'EhOrganizations',1037001,'EhUsers',316755,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (19602,'EhOrganizations',1037001,'EhUsers',316756,1001,1,UTC_TIMESTAMP());
INSERT INTO `eh_acl_role_assignments` (id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time) 
	VALUES (19603,'EhOrganizations',1037001,'EhUsers',316757,1001,1,UTC_TIMESTAMP());


INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18301,0,'广东','GUANGDONG','GD','/广东',1,1,NULL,NULL,2,0,999967);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18302,18301,'深圳市','SHENZHENSHI','SZS','/广东/深圳市',2,2,NULL,'0755',2,1,999967);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (18303,18302,'南山区','NANSHANQU','NSQ','/广东/深圳市/南山区',3,3,NULL,'0755',2,0,999967);


INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192502,UUID(),999967,2,'',0,'大沙河建投社区论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES (192503,UUID(),999967,2,'',0,'大沙河建投意见反馈论坛',NULL,'0','0',UTC_TIMESTAMP(),UTC_TIMESTAMP());

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`) 
	VALUES (240111044332059932,UUID(),1,'深圳市',2,'南山区','创新大厦','','大新路198号','创新大厦是由马家龙旧工业区以拆除重建方式实施改造的代表项目，是南山区政府为优质创新型企业提供保障性产业用房的第一个项目。工业区占地面积15156平方米，重建后的创新大厦总建筑面积71233平方米，由国有独资企业深圳市大沙河创新走廊建设投资管理有限公司负责开发，于2012年建成供企业入驻使用。',0,1,2,UTC_TIMESTAMP(),1,192502,192503,UTC_TIMESTAMP(),999967);

INSERT INTO `eh_community_geopoints` (`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES (240111044331092694,240111044332059932,'',113.92617,22.5493,'ws1022yx0njm');


INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970635,240111044332059932,'裙楼','Q',0,13538020878,'大新路198号',NULL,'113.92617','22.5493','ws1022yx0njm','创新大厦分为总部大楼和企业中心楼两部分，两栋大楼之间由连廊相接为一体。大厦外观现代时尚，功能配套齐全，设有三层高的地下室,总建筑面积为107,726平方米',NULL,2,0,NULL,1,NOW(),999967,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970636,240111044332059932,'A座','A',0,13538020878,'大新路198号',NULL,'113.92617','22.5493','ws1022yx0njm','创新大厦分为总部大楼和企业中心楼两部分，两栋大楼之间由连廊相接为一体。大厦外观现代时尚，功能配套齐全，设有三层高的地下室,总建筑面积为107,726平方米',NULL,2,0,NULL,1,NOW(),999967,1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order) 
	VALUES (1970637,240111044332059932,'B座','B',0,13538020878,'大新路198号',NULL,'113.92617','22.5493','ws1022yx0njm','创新大厦分为总部大楼和企业中心楼两部分，两栋大楼之间由连廊相接为一体。大厦外观现代时尚，功能配套齐全，设有三层高的地下室,总建筑面积为107,726平方米',NULL,2,0,NULL,1,NOW(),999967,1);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266268,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-302','裙楼','302','2','0',UTC_TIMESTAMP(),999967,455.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266300,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A701','A座','A701','2','0',UTC_TIMESTAMP(),999967,552.81);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266332,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B701','B座','B701','2','0',UTC_TIMESTAMP(),999967,555.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266364,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1601','B座','B1601','2','0',UTC_TIMESTAMP(),999967,375.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266269,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-303','裙楼','303','2','0',UTC_TIMESTAMP(),999967,668.58);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266301,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A702','A座','A702','2','0',UTC_TIMESTAMP(),999967,299.45);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266333,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B702','B座','B702','2','0',UTC_TIMESTAMP(),999967,758.21);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266365,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1602','B座','B1602','2','0',UTC_TIMESTAMP(),999967,205.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266270,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-304','裙楼','304','2','0',UTC_TIMESTAMP(),999967,684.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266302,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A703','A座','A703','2','0',UTC_TIMESTAMP(),999967,882.17);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266334,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B703','B座','B703','2','0',UTC_TIMESTAMP(),999967,328.19);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266366,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1603','B座','B1603','2','0',UTC_TIMESTAMP(),999967,521.81);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266271,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-305','裙楼','305','2','0',UTC_TIMESTAMP(),999967,360.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266303,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A704','A座','A704','2','0',UTC_TIMESTAMP(),999967,868.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266335,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B801','B座','B801','2','0',UTC_TIMESTAMP(),999967,599.72);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266367,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1604','B座','B1604','2','0',UTC_TIMESTAMP(),999967,348.84);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266272,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-306','裙楼','306','2','0',UTC_TIMESTAMP(),999967,303.97);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266304,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A801','A座','A801','2','0',UTC_TIMESTAMP(),999967,540.14);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266336,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B802','B座','B802','2','0',UTC_TIMESTAMP(),999967,320.52);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266368,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1701','B座','B1701','2','0',UTC_TIMESTAMP(),999967,528.15);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266273,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-307','裙楼','307','2','0',UTC_TIMESTAMP(),999967,575.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266305,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A802','A座','A802','2','0',UTC_TIMESTAMP(),999967,292.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266337,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B803','B座','B803','2','0',UTC_TIMESTAMP(),999967,601.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266369,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1702','B座','B1702','2','0',UTC_TIMESTAMP(),999967,338.82);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266274,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-308','裙楼','308','2','0',UTC_TIMESTAMP(),999967,312.02);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266306,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A803','A座','A803','2','0',UTC_TIMESTAMP(),999967,958.85);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266338,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B804','B座','B804','2','0',UTC_TIMESTAMP(),999967,318.92);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266370,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1703','B座','B1703','2','0',UTC_TIMESTAMP(),999967,539.86);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266275,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-401','裙楼','401','2','0',UTC_TIMESTAMP(),999967,185.63);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266307,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A804','A座','A804','2','0',UTC_TIMESTAMP(),999967,814.93);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266339,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B901','B座','B901','2','0',UTC_TIMESTAMP(),999967,570.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266371,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1704','B座','B1704','2','0',UTC_TIMESTAMP(),999967,335.43);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266276,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-402','裙楼','402','2','0',UTC_TIMESTAMP(),999967,260.96);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266308,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A901','A座','A901','2','0',UTC_TIMESTAMP(),999967,718.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266340,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B902','B座','B902','2','0',UTC_TIMESTAMP(),999967,330.34);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266372,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1801','B座','B1801','2','0',UTC_TIMESTAMP(),999967,507.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266277,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-403','裙楼','403','2','0',UTC_TIMESTAMP(),999967,787.3);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266309,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A902','A座','A902','2','0',UTC_TIMESTAMP(),999967,454.34);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266341,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B903','B座','B903','2','0',UTC_TIMESTAMP(),999967,602.04);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266373,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1802','B座','B1802','2','0',UTC_TIMESTAMP(),999967,491.26);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266278,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-404','裙楼','404','2','0',UTC_TIMESTAMP(),999967,437.61);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266310,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A903','A座','A903','2','0',UTC_TIMESTAMP(),999967,1022.98);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266342,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1001','B座','B1001','2','0',UTC_TIMESTAMP(),999967,570.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266374,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1803','B座','B1803','2','0',UTC_TIMESTAMP(),999967,404.63);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266279,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-405','裙楼','405','2','0',UTC_TIMESTAMP(),999967,600.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266311,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A904','A座','A904','2','0',UTC_TIMESTAMP(),999967,828.35);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266343,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1002','B座','B1002','2','0',UTC_TIMESTAMP(),999967,330.34);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266375,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1901','B座','B1901','2','0',UTC_TIMESTAMP(),999967,492.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266280,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-406','裙楼','406','2','0',UTC_TIMESTAMP(),999967,2105.04);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266312,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1001','A座','A1001','2','0',UTC_TIMESTAMP(),999967,647.81);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266344,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1003','B座','B1003','2','0',UTC_TIMESTAMP(),999967,602.04);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266376,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1902','B座','B1902','2','0',UTC_TIMESTAMP(),999967,910.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266281,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-407','裙楼','407','2','0',UTC_TIMESTAMP(),999967,472.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266313,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1002','A座','A1002','2','0',UTC_TIMESTAMP(),999967,467.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266345,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1101','B座','B1101','2','0',UTC_TIMESTAMP(),999967,570.03);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266377,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2001','B座','B2001','2','0',UTC_TIMESTAMP(),999967,492.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266282,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-408','裙楼','408','2','0',UTC_TIMESTAMP(),999967,299.38);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266314,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1003','A座','A1003','2','0',UTC_TIMESTAMP(),999967,670.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266346,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1102','B座','B1102','2','0',UTC_TIMESTAMP(),999967,330.34);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266378,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2002','B座','B2002','2','0',UTC_TIMESTAMP(),999967,910.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266283,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-409','裙楼','409','2','0',UTC_TIMESTAMP(),999967,399.9);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266315,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1004','A座','A1004','2','0',UTC_TIMESTAMP(),999967,655.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266347,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1103','B座','B1103','2','0',UTC_TIMESTAMP(),999967,602.04);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266379,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2101','B座','B2101','2','0',UTC_TIMESTAMP(),999967,492.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266284,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-501','裙楼','501','2','0',UTC_TIMESTAMP(),999967,463.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266316,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1101','A座','A1101','2','0',UTC_TIMESTAMP(),999967,647.81);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266348,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1201','B座','B1201','2','0',UTC_TIMESTAMP(),999967,605.43);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266380,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2102','B座','B2102','2','0',UTC_TIMESTAMP(),999967,910.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266285,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-502','裙楼','502','2','0',UTC_TIMESTAMP(),999967,636.47);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266317,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1102','A座','A1102','2','0',UTC_TIMESTAMP(),999967,467.12);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266349,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1202','B座','B1202','2','0',UTC_TIMESTAMP(),999967,321.72);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266381,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2201','B座','B2201','2','0',UTC_TIMESTAMP(),999967,492.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266286,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-503','裙楼','503','2','0',UTC_TIMESTAMP(),999967,2883.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266318,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1103','A座','A1103','2','0',UTC_TIMESTAMP(),999967,670.99);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266350,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1203','B座','B1203','2','0',UTC_TIMESTAMP(),999967,528.67);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266382,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2202','B座','B2202','2','0',UTC_TIMESTAMP(),999967,910.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266255,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-101','裙楼','101','2','0',UTC_TIMESTAMP(),999967,94.98);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266287,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-504','裙楼','504','2','0',UTC_TIMESTAMP(),999967,500.95);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266319,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1104','A座','A1104','2','0',UTC_TIMESTAMP(),999967,655.54);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266351,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1204','B座','B1204','2','0',UTC_TIMESTAMP(),999967,334.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266383,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2301','B座','B2301','2','0',UTC_TIMESTAMP(),999967,492.28);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266256,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-102','裙楼','102','2','0',UTC_TIMESTAMP(),999967,65.85);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266288,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-505','裙楼','505','2','0',UTC_TIMESTAMP(),999967,317.25);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266320,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1201','A座','A1201','2','0',UTC_TIMESTAMP(),999967,675.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266352,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1301','B座','B1301','2','0',UTC_TIMESTAMP(),999967,369.01);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266384,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2302','B座','B2302','2','0',UTC_TIMESTAMP(),999967,910.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266257,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-103','裙楼','103','2','0',UTC_TIMESTAMP(),999967,523.4);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266289,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-506','裙楼','506','2','0',UTC_TIMESTAMP(),999967,731.59);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266321,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1202','A座','A1202','2','0',UTC_TIMESTAMP(),999967,462.94);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266353,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1302','B座','B1302','2','0',UTC_TIMESTAMP(),999967,233.74);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266385,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B2401','B座','B2401','2','0',UTC_TIMESTAMP(),999967,1411.82);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266258,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-104','裙楼','104','2','0',UTC_TIMESTAMP(),999967,100.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266290,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-601','裙楼','601','2','0',UTC_TIMESTAMP(),999967,200.13);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266322,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1203','A座','A1203','2','0',UTC_TIMESTAMP(),999967,730.04);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266354,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1303','B座','B1303','2','0',UTC_TIMESTAMP(),999967,513.34);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266259,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-105','裙楼','105','2','0',UTC_TIMESTAMP(),999967,82.5);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266291,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-602','裙楼','602','2','0',UTC_TIMESTAMP(),999967,304.44);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266323,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1204','A座','A1204','2','0',UTC_TIMESTAMP(),999967,733.69);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266355,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1304','B座','B1304','2','0',UTC_TIMESTAMP(),999967,343.18);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266260,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-106','裙楼','106','2','0',UTC_TIMESTAMP(),999967,53.78);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266292,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-603','裙楼','603','2','0',UTC_TIMESTAMP(),999967,905.23);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266324,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1301','A座','A1301','2','0',UTC_TIMESTAMP(),999967,498.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266356,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1401','B座','B1401','2','0',UTC_TIMESTAMP(),999967,375.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266261,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-107','裙楼','107','2','0',UTC_TIMESTAMP(),999967,257.52);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266293,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-604','裙楼','604','2','0',UTC_TIMESTAMP(),999967,471.82);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266325,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1302','A座','A1302','2','0',UTC_TIMESTAMP(),999967,300.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266357,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1402','B座','B1402','2','0',UTC_TIMESTAMP(),999967,205.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266262,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-201','裙楼','201','2','0',UTC_TIMESTAMP(),999967,470.07);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266294,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-605','裙楼','605','2','0',UTC_TIMESTAMP(),999967,843.29);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266326,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1303','A座','A1303','2','0',UTC_TIMESTAMP(),999967,517.82);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266358,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1403','B座','B1403','2','0',UTC_TIMESTAMP(),999967,521.81);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266263,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-202','裙楼','202','2','0',UTC_TIMESTAMP(),999967,387.41);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266295,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-606','裙楼','606','2','0',UTC_TIMESTAMP(),999967,393.97);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266327,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1304','A座','A1304','2','0',UTC_TIMESTAMP(),999967,727.68);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266359,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1404','B座','B1404','2','0',UTC_TIMESTAMP(),999967,348.84);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266264,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-203','裙楼','203','2','0',UTC_TIMESTAMP(),999967,690.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266296,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-607','裙楼','607','2','0',UTC_TIMESTAMP(),999967,1087.52);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266328,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1401','A座','A1401','2','0',UTC_TIMESTAMP(),999967,498.71);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266360,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1501','B座','B1501','2','0',UTC_TIMESTAMP(),999967,375.1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266265,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-204','裙楼','204','2','0',UTC_TIMESTAMP(),999967,243.84);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266297,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-608','裙楼','608','2','0',UTC_TIMESTAMP(),999967,515.76);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266329,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1402','A座','A1402','2','0',UTC_TIMESTAMP(),999967,300.56);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266361,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1502','B座','B1502','2','0',UTC_TIMESTAMP(),999967,205.37);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266266,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-205','裙楼','205','2','0',UTC_TIMESTAMP(),999967,356.65);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266298,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-609','裙楼','609','2','0',UTC_TIMESTAMP(),999967,322.8);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266330,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1403','A座','A1403','2','0',UTC_TIMESTAMP(),999967,517.82);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266362,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1503','B座','B1503','2','0',UTC_TIMESTAMP(),999967,521.81);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266267,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-301','裙楼','301','2','0',UTC_TIMESTAMP(),999967,1046.07);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266299,UUID(),240111044332059932,1,'深圳市',2,'南山区','裙楼-610','裙楼','610','2','0',UTC_TIMESTAMP(),999967,729.09);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266331,UUID(),240111044332059932,1,'深圳市',2,'南山区','A座-A1404','A座','A1404','2','0',UTC_TIMESTAMP(),999967,727.68);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`) 
	VALUES (239825274387266363,UUID(),240111044332059932,1,'深圳市',2,'南山区','B座-B1504','B座','B1504','2','0',UTC_TIMESTAMP(),999967,348.84);

	

INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81806,1037001,240111044332059932,239825274387266268,'裙楼-302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81838,1037001,240111044332059932,239825274387266300,'A座-A701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81870,1037001,240111044332059932,239825274387266332,'B座-B701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81902,1037001,240111044332059932,239825274387266364,'B座-B1601',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81807,1037001,240111044332059932,239825274387266269,'裙楼-303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81839,1037001,240111044332059932,239825274387266301,'A座-A702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81871,1037001,240111044332059932,239825274387266333,'B座-B702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81903,1037001,240111044332059932,239825274387266365,'B座-B1602',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81808,1037001,240111044332059932,239825274387266270,'裙楼-304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81840,1037001,240111044332059932,239825274387266302,'A座-A703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81872,1037001,240111044332059932,239825274387266334,'B座-B703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81904,1037001,240111044332059932,239825274387266366,'B座-B1603',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81809,1037001,240111044332059932,239825274387266271,'裙楼-305',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81841,1037001,240111044332059932,239825274387266303,'A座-A704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81873,1037001,240111044332059932,239825274387266335,'B座-B801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81905,1037001,240111044332059932,239825274387266367,'B座-B1604',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81810,1037001,240111044332059932,239825274387266272,'裙楼-306',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81842,1037001,240111044332059932,239825274387266304,'A座-A801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81874,1037001,240111044332059932,239825274387266336,'B座-B802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81906,1037001,240111044332059932,239825274387266368,'B座-B1701',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81811,1037001,240111044332059932,239825274387266273,'裙楼-307',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81843,1037001,240111044332059932,239825274387266305,'A座-A802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81875,1037001,240111044332059932,239825274387266337,'B座-B803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81907,1037001,240111044332059932,239825274387266369,'B座-B1702',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81812,1037001,240111044332059932,239825274387266274,'裙楼-308',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81844,1037001,240111044332059932,239825274387266306,'A座-A803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81876,1037001,240111044332059932,239825274387266338,'B座-B804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81908,1037001,240111044332059932,239825274387266370,'B座-B1703',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81813,1037001,240111044332059932,239825274387266275,'裙楼-401',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81845,1037001,240111044332059932,239825274387266307,'A座-A804',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81877,1037001,240111044332059932,239825274387266339,'B座-B901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81909,1037001,240111044332059932,239825274387266371,'B座-B1704',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81814,1037001,240111044332059932,239825274387266276,'裙楼-402',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81846,1037001,240111044332059932,239825274387266308,'A座-A901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81878,1037001,240111044332059932,239825274387266340,'B座-B902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81910,1037001,240111044332059932,239825274387266372,'B座-B1801',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81815,1037001,240111044332059932,239825274387266277,'裙楼-403',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81847,1037001,240111044332059932,239825274387266309,'A座-A902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81879,1037001,240111044332059932,239825274387266341,'B座-B903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81911,1037001,240111044332059932,239825274387266373,'B座-B1802',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81816,1037001,240111044332059932,239825274387266278,'裙楼-404',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81848,1037001,240111044332059932,239825274387266310,'A座-A903',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81880,1037001,240111044332059932,239825274387266342,'B座-B1001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81912,1037001,240111044332059932,239825274387266374,'B座-B1803',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81817,1037001,240111044332059932,239825274387266279,'裙楼-405',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81849,1037001,240111044332059932,239825274387266311,'A座-A904',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81881,1037001,240111044332059932,239825274387266343,'B座-B1002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81913,1037001,240111044332059932,239825274387266375,'B座-B1901',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81818,1037001,240111044332059932,239825274387266280,'裙楼-406',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81850,1037001,240111044332059932,239825274387266312,'A座-A1001',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81882,1037001,240111044332059932,239825274387266344,'B座-B1003',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81914,1037001,240111044332059932,239825274387266376,'B座-B1902',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81819,1037001,240111044332059932,239825274387266281,'裙楼-407',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81851,1037001,240111044332059932,239825274387266313,'A座-A1002',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81883,1037001,240111044332059932,239825274387266345,'B座-B1101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81915,1037001,240111044332059932,239825274387266377,'B座-B2001',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81820,1037001,240111044332059932,239825274387266282,'裙楼-408',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81852,1037001,240111044332059932,239825274387266314,'A座-A1003',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81884,1037001,240111044332059932,239825274387266346,'B座-B1102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81916,1037001,240111044332059932,239825274387266378,'B座-B2002',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81821,1037001,240111044332059932,239825274387266283,'裙楼-409',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81853,1037001,240111044332059932,239825274387266315,'A座-A1004',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81885,1037001,240111044332059932,239825274387266347,'B座-B1103',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81917,1037001,240111044332059932,239825274387266379,'B座-B2101',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81822,1037001,240111044332059932,239825274387266284,'裙楼-501',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81854,1037001,240111044332059932,239825274387266316,'A座-A1101',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81886,1037001,240111044332059932,239825274387266348,'B座-B1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81918,1037001,240111044332059932,239825274387266380,'B座-B2102',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81823,1037001,240111044332059932,239825274387266285,'裙楼-502',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81855,1037001,240111044332059932,239825274387266317,'A座-A1102',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81887,1037001,240111044332059932,239825274387266349,'B座-B1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81919,1037001,240111044332059932,239825274387266381,'B座-B2201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81824,1037001,240111044332059932,239825274387266286,'裙楼-503',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81856,1037001,240111044332059932,239825274387266318,'A座-A1103',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81888,1037001,240111044332059932,239825274387266350,'B座-B1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81920,1037001,240111044332059932,239825274387266382,'B座-B2202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81793,1037001,240111044332059932,239825274387266255,'裙楼-101',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81825,1037001,240111044332059932,239825274387266287,'裙楼-504',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81857,1037001,240111044332059932,239825274387266319,'A座-A1104',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81889,1037001,240111044332059932,239825274387266351,'B座-B1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81921,1037001,240111044332059932,239825274387266383,'B座-B2301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81794,1037001,240111044332059932,239825274387266256,'裙楼-102',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81826,1037001,240111044332059932,239825274387266288,'裙楼-505',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81858,1037001,240111044332059932,239825274387266320,'A座-A1201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81890,1037001,240111044332059932,239825274387266352,'B座-B1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81922,1037001,240111044332059932,239825274387266384,'B座-B2302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81795,1037001,240111044332059932,239825274387266257,'裙楼-103',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81827,1037001,240111044332059932,239825274387266289,'裙楼-506',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81859,1037001,240111044332059932,239825274387266321,'A座-A1202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81891,1037001,240111044332059932,239825274387266353,'B座-B1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81923,1037001,240111044332059932,239825274387266385,'B座-B2401',1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81796,1037001,240111044332059932,239825274387266258,'裙楼-104',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81828,1037001,240111044332059932,239825274387266290,'裙楼-601',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81860,1037001,240111044332059932,239825274387266322,'A座-A1203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81892,1037001,240111044332059932,239825274387266354,'B座-B1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81797,1037001,240111044332059932,239825274387266259,'裙楼-105',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81829,1037001,240111044332059932,239825274387266291,'裙楼-602',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81861,1037001,240111044332059932,239825274387266323,'A座-A1204',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81893,1037001,240111044332059932,239825274387266355,'B座-B1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81798,1037001,240111044332059932,239825274387266260,'裙楼-106',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81830,1037001,240111044332059932,239825274387266292,'裙楼-603',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81862,1037001,240111044332059932,239825274387266324,'A座-A1301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81894,1037001,240111044332059932,239825274387266356,'B座-B1401',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81799,1037001,240111044332059932,239825274387266261,'裙楼-107',3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81831,1037001,240111044332059932,239825274387266293,'裙楼-604',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81863,1037001,240111044332059932,239825274387266325,'A座-A1302',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81895,1037001,240111044332059932,239825274387266357,'B座-B1402',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81800,1037001,240111044332059932,239825274387266262,'裙楼-201',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81832,1037001,240111044332059932,239825274387266294,'裙楼-605',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81864,1037001,240111044332059932,239825274387266326,'A座-A1303',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81896,1037001,240111044332059932,239825274387266358,'B座-B1403',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81801,1037001,240111044332059932,239825274387266263,'裙楼-202',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81833,1037001,240111044332059932,239825274387266295,'裙楼-606',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81865,1037001,240111044332059932,239825274387266327,'A座-A1304',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81897,1037001,240111044332059932,239825274387266359,'B座-B1404',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81802,1037001,240111044332059932,239825274387266264,'裙楼-203',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81834,1037001,240111044332059932,239825274387266296,'裙楼-607',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81866,1037001,240111044332059932,239825274387266328,'A座-A1401',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81898,1037001,240111044332059932,239825274387266360,'B座-B1501',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81803,1037001,240111044332059932,239825274387266265,'裙楼-204',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81835,1037001,240111044332059932,239825274387266297,'裙楼-608',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81867,1037001,240111044332059932,239825274387266329,'A座-A1402',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81899,1037001,240111044332059932,239825274387266361,'B座-B1502',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81804,1037001,240111044332059932,239825274387266266,'裙楼-205',0);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81836,1037001,240111044332059932,239825274387266298,'裙楼-609',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81868,1037001,240111044332059932,239825274387266330,'A座-A1403',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81900,1037001,240111044332059932,239825274387266362,'B座-B1503',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81805,1037001,240111044332059932,239825274387266267,'裙楼-301',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81837,1037001,240111044332059932,239825274387266299,'裙楼-610',4);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81869,1037001,240111044332059932,239825274387266331,'A座-A1404',5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`) 
	VALUES (81901,1037001,240111044332059932,239825274387266363,'B座-B1504',4);


-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
--	VALUES (116201,40700,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116202,40800,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116203,40810,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116204,40830,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116205,40840,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116206,40850,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116207,41000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116208,41010,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116209,41020,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116210,41030,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116211,41040,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116212,41050,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116213,41060,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116214,41100,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116215,40150,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116216,30000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116217,30500,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116218,31000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116219,32000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116220,33000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116221,34000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116222,35000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116223,30600,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116224,50000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116225,50100,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116226,50110,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116227,50200,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116228,50210,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116229,50220,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116230,50300,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116231,50400,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116232,50500,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116233,50600,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116234,50630,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116235,50631,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116236,50632,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116237,50633,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116238,50640,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116239,50650,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116240,50651,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116241,50652,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116242,50653,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116243,56161,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116244,50700,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116245,50710,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116246,50720,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116247,50730,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116248,50900,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116249,60000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116250,60100,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116251,60200,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116252,10000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116253,10100,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116254,10400,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116255,10200,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116256,10600,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116257,10750,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116258,10751,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116259,10752,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116260,10800,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116261,11000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116262,20000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116263,20100,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116264,20140,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116265,20150,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116266,20155,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116267,20170,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116268,20180,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116269,20158,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116270,20190,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116271,20191,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116272,20192,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116273,20400,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116274,20410,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116275,20420,NULL,'EhNamespaces',999967,2);
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
-- 	VALUES (116276,49100,NULL,'EhNamespaces',999967,2);
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
-- 	VALUES (116277,49110,NULL,'EhNamespaces',999967,2);
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
-- 	VALUES (116278,49120,NULL,'EhNamespaces',999967,2);
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
-- 	VALUES (116279,49130,NULL,'EhNamespaces',999967,2);
-- INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
-- 	VALUES (116280,49140,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116281,40000,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116282,40100,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116283,40110,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116284,40120,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116285,40130,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116286,40300,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116287,40400,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116288,40410,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116289,40420,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116290,40430,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116291,40440,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116292,40450,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116293,40500,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116294,40510,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116295,40520,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116296,40530,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116297,40541,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116298,40542,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116299,41300,NULL,'EhNamespaces',999967,2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES (116300,40750,NULL,'EhNamespaces',999967,2);



-- 用于配广场item
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203531, 6, 0, '物业报修', '任务/物业报修', 1, 2, UTC_TIMESTAMP(), @namespace_id);    
INSERT INTO `eh_categories`(`id`, `parent_id`, `link_id`, `name`, `path`, `default_order`, `status`, `create_time`, `namespace_id`)
    VALUES(203532, 6, 0, '投诉建议', '任务/投诉建议', 1, 2, UTC_TIMESTAMP(), @namespace_id); 
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES (11101, '会议室预订', 0, NULL, 2, @namespace_id);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES (11102, '场地预订', 0, NULL, 2, @namespace_id);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES (11103, '电子屏', 0, NULL, 2, @namespace_id);

-- 用于配置广场的多个活动入口
SET @activity_id = (SELECT MAX(id) FROM `eh_activity_categories`);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES ((@activity_id := @activity_id +1), '', '0', 1, -1, '活动', '/1', '0', '2', '1', '2017-08-31 10:24:50', '0', NULL, @namespace_id, '0', '1', NULL, NULL, NULL, 0);
INSERT INTO `eh_activity_categories` (`id`, `owner_type`, `owner_id`, `entry_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `default_flag`, `enabled`, `icon_uri`, `selected_icon_uri`, `show_name`, `all_flag`)
    VALUES ((@activity_id := @activity_id +1), '', '0', @activity_id, '1', 'all', CONCAT('/1/', @activity_id), '0', '2', '1', '2017-08-31 10:24:50', '0', NULL, @namespace_id, '0', '1', 'cs://1/image/aW1hZ2UvTVRvM09HWmxNRFptWldNM1lqQm1aakEyWVdRMVpEZ3dNelEzTVRrMk1XRmpPUQ', 'cs://1/image/aW1hZ2UvTVRvd016YzVZVE5tT1dFeU9XUTRPRGcxTkdNME5HUTFabVE1T0RBd00yWmpZdw', NULL, '1');
	
	
--  园区入驻、招租管理需要这条数据
SET @eh_lease_configs_id = (SELECT MAX(id) FROM `eh_lease_configs`);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`, `area_search_flag`, `display_name_str`, `display_order_str`) 
	VALUES((@eh_lease_configs_id := @eh_lease_configs_id + 1),@namespace_id,'1','1','1','1','1','1','园区介绍, 虚位以待', '1,2');

-- 服务联盟
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (201228, 'community', 240111044332059932, '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);    
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', 240111044332059932, '服务联盟', '服务联盟', 201228, '', NULL, '企业服务联盟旨在聚集优质服务资源，为园区企业提供专业、高效、有保障的服务。', 'cs://1/image/aW1hZ2UvTVRvMVpEVTFPVGs1Tm1OaU1tVTFNek0zWXpkak1EZG1aVFUyWWpJMU5EUTFOUQ', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
	

SET @banner_id = (SELECT MAX(id) FROM `eh_banners`);
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRveU0yRTBObUkzTmpJeU1ERXdNV0kyWVRsbVlXTTVPVFk0WWpObU5XRmpOQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES ((@banner_id := @banner_id + 1), @namespace_id, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRveU0yRTBObUkzTmpJeU1ERXdNV0kyWVRsbVlXTTVPVFk0WWpObU5XRmpOQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
	
-- OK
SET @launch_pad_layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`)
	VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"versionCode": "2017083101","versionName": "1.0.0","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 1,"separatorHeight": 16}, {"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default","rowCount": 1},"style": "Default","defaultOrder": 2,"separatorFlag": 1,"separatorHeight": 16}, {"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "GovAgencies"},"style": "Default","defaultOrder": 3,"separatorFlag": 1,"separatorHeight": 16,"columnCount": 4}, {"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs","cssStyleFlag":1,"paddingLeft":0,"paddingTop":0,"paddingRight":0,"paddingBottom":0,"lineSpacing":2,"columnSpacing":2,"backgroundColor":"EFEFF4"},"style": "Gallery","defaultOrder": 4,"separatorFlag": 1,"separatorHeight": 16,"columnCount": 2}, {"groupName": "","widget": "NewsFlash","instanceConfig": {"timeWidgetStyle": "date","categoryId": 0,"itemGroup": "Default","newsSize": 3},"style": "Default","defaultOrder": 5,"separatorFlag": 0,"separatorHeight": 0}]}', 2017083101, 0, 2, NOW(), 'pm_admin', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`)
	VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"versionCode": "2017083101","versionName": "1.0.0","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 1,"separatorFlag": 1,"separatorHeight": 16}, {"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default","rowCount": 1},"style": "Default","defaultOrder": 2,"separatorFlag": 1,"separatorHeight": 16}, {"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "GovAgencies"},"style": "Default","defaultOrder": 3,"separatorFlag": 1,"separatorHeight": 16,"columnCount": 4}, {"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs","cssStyleFlag":1,"paddingLeft":0,"paddingTop":0,"paddingRight":0,"paddingBottom":0,"lineSpacing":2,"columnSpacing":2,"backgroundColor":"EFEFF4"},"style": "Gallery","defaultOrder": 4,"separatorFlag": 1,"separatorHeight": 16,"columnCount": 2}, {"groupName": "","widget": "NewsFlash","instanceConfig": {"timeWidgetStyle": "date","categoryId": 0,"itemGroup": "Default","newsSize": 3},"style": "Default","defaultOrder": 5,"separatorFlag": 0,"separatorHeight": 0}]}', 2017083101, 0, 2, NOW(), 'park_tourist', 0, 0, 0);

	
-- GovAgencies iten_gorup
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
-- {"namespaceId":999967,"ownerId":240111044332059001,"ownerType":"community","resourceType":"introduction"}
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','园区简介','园区简介','cs://1/image/aW1hZ2UvTVRvd01EVXpNekZoTW1NMlpUUTJaRGRsT0RCaE5qaGxNVGRqWlRsa1lURmhNUQ','1','1',13,'{"url":"http://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1&rtToken=hWSzn3doJ6_e63mZYCLlP5mastXzHbcObCnDX-T4k4bldRIx0sYCQBBcejQ3UYgfP66cwBnE9XIFMSICI4b1CwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-flUIy8QPY_kfYuTL2u5dHRE"}',10,'0','1','1','','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','园区简介','园区简介','cs://1/image/aW1hZ2UvTVRvd01EVXpNekZoTW1NMlpUUTJaRGRsT0RCaE5qaGxNVGRqWlRsa1lURmhNUQ','1','1',13,'{"url":"http://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1&rtToken=hWSzn3doJ6_e63mZYCLlP5mastXzHbcObCnDX-T4k4bldRIx0sYCQBBcejQ3UYgfP66cwBnE9XIFMSICI4b1CwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-flUIy8QPY_kfYuTL2u5dHRE"}',10,'0','1','1','','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','CONTACTS','通讯录','cs://1/image/aW1hZ2UvTVRveU9XRXhNemd6WVRjMk1tRTNORFUyTW1WbVpqYzNPR00zTkRFM1l6SmhPQQ','1','1',46,'',20,'0','1','1','','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','CONTACTS','通讯录','cs://1/image/aW1hZ2UvTVRveU9XRXhNemd6WVRjMk1tRTNORFUyTW1WbVpqYzNPR00zTkRFM1l6SmhPQQ','1','1',46,'',20,'0','1','1','','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','SERVICE_HOT_LINE','服务热线','cs://1/image/aW1hZ2UvTVRvME16WmtOVEZoWm1VelltRm1ZMkV5T0RRMU9ESmpOMkppT1RSaFl6RTFOdw','1','1',45,'',30,'0','1','1','','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','SERVICE_HOT_LINE','服务热线','cs://1/image/aW1hZ2UvTVRvME16WmtOVEZoWm1VelltRm1ZMkV5T0RRMU9ESmpOMkppT1RSaFl6RTFOdw','1','1',45,'',30,'0','1','1','','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','园区巴士','园区巴士','cs://1/image/aW1hZ2UvTVRvellqbGxOR00yT0RWa1lqVXhNak5tWWpFNE5UQTRNV0ZqWkdGalpUbGlNZw','1','1',14,'{"url":"http://wx.dudubashi.com/index.php/zuolin/Webauth/auth_login"}',40,'0','1','1','','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','园区巴士','园区巴士','cs://1/image/aW1hZ2UvTVRvellqbGxOR00yT0RWa1lqVXhNak5tWWpFNE5UQTRNV0ZqWkdGalpUbGlNZw','1','1',14,'{"url":"http://wx.dudubashi.com/index.php/zuolin/Webauth/auth_login"}',40,'0','1','1','','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','DoorManagement','门禁','cs://1/image/aW1hZ2UvTVRvNVpqY3hPVGN3WVRabU9EbGhPR013TW1VMU4yRTFaV1ptTlRrd09HVXhNQQ','1','1',40,'{"isSupportQR":1,"isSupportSmart":1}',50,'0','1','1','','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','DoorManagement','门禁','cs://1/image/aW1hZ2UvTVRvNVpqY3hPVGN3WVRabU9EbGhPR013TW1VMU4yRTFaV1ptTlRrd09HVXhNQQ','1','1',40,'{"isSupportQR":1,"isSupportSmart":1}',50,'0','1','1','','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','PROPERTY_REPAIR','物业报修','cs://1/image/aW1hZ2UvTVRwaE16TTRObVEyWTJabFpqWmxOamhrTkdKa01qRmpPV1l3TURrd09HTTBZUQ','1','1',60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203531&displayName=物业报修"}',60,'0','1','1','','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','PROPERTY_REPAIR','物业报修','cs://1/image/aW1hZ2UvTVRwaE16TTRObVEyWTJabFpqWmxOamhrTkdKa01qRmpPV1l3TURrd09HTTBZUQ','1','1',60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203531&displayName=物业报修"}',60,'0','1','1','','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','报修投诉','投诉建议','cs://1/image/aW1hZ2UvTVRwa05qY3lPVFF6TVRObFltVXpaVE5sT1RGak9EZGhaREl5T0dSaFpqZzNaZw','1','1',60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203532&displayName=投诉建议"}',70,'0','1','1','','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,'0',NULL);
-- INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
--     VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','报修投诉','投诉建议','cs://1/image/aW1hZ2UvTVRwa05qY3lPVFF6TVRObFltVXpaVE5sT1RGak9EZGhaREl5T0dSaFpqZzNaZw','1','1',60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203532&displayName=投诉建议"}',70,'0','1','1','','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,'0',NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','MORE_BUTTON','更多','cs://1/image/aW1hZ2UvTVRvMU1UVTNNMkk0T0RkbU9HWm1OR0ZpTjJObVkyRXpOemsyT0dWbFl6VmlOQQ','1','1',53,'{"itemLocation":"/home","itemGroup":"GovAgencies"}',999,'0','1','1','','0',NULL,NULL,NULL,'0','park_tourist','0',NULL,NULL,'0',NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','MORE_BUTTON','更多','cs://1/image/aW1hZ2UvTVRvMU1UVTNNMkk0T0RkbU9HWm1OR0ZpTjJObVkyRXpOemsyT0dWbFl6VmlOQQ','1','1',53,'{"itemLocation":"/home","itemGroup":"GovAgencies"}',999,'0','1','1','','0',NULL,NULL,NULL,'0','pm_admin','0',NULL,NULL,'0',NULL);


-- Bizs iten_gorup
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'METTING_ROOM', '会议室预订', 'cs://1/image/aW1hZ2UvTVRwaVptWXhOVFJpTkRreVl6azVPV0kxWmpRME1qRTVOalprWTJSbVpUYzVNUQ', 1, 1, 49, '{"resourceTypeId":11101,"pageType":0}', 10, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'METTING_ROOM', '会议室预订', 'cs://1/image/aW1hZ2UvTVRwaVptWXhOVFJpTkRreVl6azVPV0kxWmpRME1qRTVOalprWTJSbVpUYzVNUQ', 1, 1, 49, '{"resourceTypeId":11101,"pageType":0}', 10, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', '场地预订', '场地预订', 'cs://1/image/aW1hZ2UvTVRvMU9EUmlOREV5T1RreU1qaGhPV05qWm1GbFlqRm1aRE0zTVdRME1qZzJNdw', 1, 1, 49, '{"resourceTypeId":11102,"pageType":0}', 20, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', '场地预订', '场地预订', 'cs://1/image/aW1hZ2UvTVRvMU9EUmlOREV5T1RreU1qaGhPV05qWm1GbFlqRm1aRE0zTVdRME1qZzJNdw', 1, 1, 49, '{"resourceTypeId":11102,"pageType":0}', 20, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ELECSCREEN', '电子屏', 'cs://1/image/aW1hZ2UvTVRveE16VTBNMlEzTkROaVpUYzJNelJpTmpSa01HRmpPR1V5TkRSaU1qZzVNQQ', 1, 1, 49, '{"resourceTypeId":11103,"pageType":0}', 30, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ELECSCREEN', '电子屏', 'cs://1/image/aW1hZ2UvTVRveE16VTBNMlEzTkROaVpUYzJNelJpTmpSa01HRmpPR1V5TkRSaU1qZzVNQQ', 1, 1, 49, '{"resourceTypeId":11103,"pageType":0}', 30, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'WIFI', '一键上网', 'cs://1/image/aW1hZ2UvTVRveE5UZGtPVFUwT0RoaE16VmhaVGN6WWprME9EVTJaV1V3TWpoaU1EUmtaQQ', 1, 1, 47, '', 40, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'WIFI', '一键上网', 'cs://1/image/aW1hZ2UvTVRveE5UZGtPVFUwT0RoaE16VmhaVGN6WWprME9EVTJaV1V3TWpoaU1EUmtaQQ', 1, 1, 47, '', 40, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CLOUD_PRINT', '共享打印', 'cs://1/image/aW1hZ2UvTVRwaVlUWmpZakppTkRreE9ETmhNV0l3WmpZeE9UVmhNREptWkRGalpqQXhZUQ', 1, 1, 13, CONCAT('{"url": "', @core_url,'/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}'), 50, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CLOUD_PRINT', '共享打印', 'cs://1/image/aW1hZ2UvTVRwaVlUWmpZakppTkRreE9ETmhNV0l3WmpZeE9UVmhNREptWkRGalpqQXhZUQ', 1, 1, 13, CONCAT('{"url": "', @core_url,'/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}'), 50, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,0, 0, 0,'/home','Bizs','MORE_BUTTON','更多','cs://1/image/aW1hZ2UvTVRvM1pqVTRNelprWkRZMk9HRTBPR1F3TlRrd1pUZGpaalJqWW1VellUZzVOZw',1, 1, 1, '{"itemLocation":"/home","itemGroup":"Bizs"}',999,'0','1','1','','0',NULL,NULL,NULL,'0','park_tourist','0',NULL,NULL,'0',NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
	VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,0, 0, 0,'/home','Bizs','MORE_BUTTON','更多','cs://1/image/aW1hZ2UvTVRvM1pqVTRNelprWkRZMk9HRTBPR1F3TlRrd1pUZGpaalJqWW1VellUZzVOZw',1, 1, 1, '{"itemLocation":"/home","itemGroup":"Bizs"}',999,'0','1','1','','0',NULL,NULL,NULL,'0','pm_admin','0',NULL,NULL,'0',NULL);


SET @launch_pad_layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`)
    VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'AssociationLayout', '{"versionCode":"2017083102","versionName":"4.1.2","layoutName":"AssociationLayout","displayName":"发现","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}', '2017083102', '2017083101', '2', '2017-08-31 10:18:31', 'park_tourist', '0', '0', '0');
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`)
    VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'AssociationLayout', '{"versionCode":"2017083102","versionName":"4.1.2","layoutName":"AssociationLayout","displayName":"发现","groups":[{"groupName":"","widget":"Tab","instanceConfig":{"itemGroup":"TabGroup"},"style":"1","defaultOrder":1,"separatorFlag":0,"separatorHeight":0}]}', '2017083102', '2017083101', '2', '2017-08-31 10:18:31', 'pm_admin', '0', '0', '0');

SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `more_order`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/association', 'TabGroup', '活动', '活动', NULL, '1', '1', 61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":1,"scope":3,"style":4,"title": "活动"}', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', 10);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `more_order`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/association', 'TabGroup', '论坛', '论坛', NULL, '1', '1', 62,'{}', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', 20);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `more_order`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/association', 'TabGroup', '俱乐部', '俱乐部', NULL, '1', '1', 36,'{"privateFlag": 0}', 30, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', 30);

	
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `more_order`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/association', 'TabGroup', '活动', '活动', NULL, '1', '1', 61,'{"categoryId":1,"publishPrivilege":1,"livePrivilege":2,"listStyle":1,"scope":3,"style":4,"title": "活动"}', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', 10);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `more_order`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/association', 'TabGroup', '论坛', '论坛', NULL, '1', '1', 62,'{}', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', 20);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `more_order`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/association', 'TabGroup', '俱乐部', '俱乐部', NULL, '1', '1', 36,'{"privateFlag": 0}', 30, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', 30);

-- 更多全部
SET @item_service_categry_id = (SELECT MAX(id) FROM `eh_item_service_categries`);
INSERT INTO `eh_item_service_categries`(`id`, `scope_code`, `scope_id`, `name`, `label`, `item_location`, `item_group`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, scene_type, operator_uid, creator_uid)
    VALUES((@item_service_categry_id := @item_service_categry_id +1), 0, 0, 'CmntyServices', '园区服务', '/home','GovAgencies', 'cs://1/image/aW1hZ2UvTVRwbE5EVTBaamd3TmpWak9UWTRNV0ZrTkRVek1UZG1PVGRrT1RReU5tRmhPQQ', 1, 0, 1, @namespace_id, 'park_tourist', 0, 0);
INSERT INTO `eh_item_service_categries`(`id`, `scope_code`, `scope_id`, `name`, `label`, `item_location`, `item_group`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, scene_type, operator_uid, creator_uid)
    VALUES((@item_service_categry_id := @item_service_categry_id +1), 0, 0, 'PmServices', '物业服务', '/home','GovAgencies', 'cs://1/image/aW1hZ2UvTVRwbE5EVTBaamd3TmpWak9UWTRNV0ZrTkRVek1UZG1PVGRrT1RReU5tRmhPQQ', 2, 0, 1, @namespace_id, 'park_tourist', 0, 0);
INSERT INTO `eh_item_service_categries`(`id`, `scope_code`, `scope_id`, `name`, `label`, `item_location`, `item_group`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, scene_type, operator_uid, creator_uid)
    VALUES((@item_service_categry_id := @item_service_categry_id +1), 0, 0, 'OrgServices', '企业服务', '/home','GovAgencies', 'cs://1/image/aW1hZ2UvTVRwbE5EVTBaamd3TmpWak9UWTRNV0ZrTkRVek1UZG1PVGRrT1RReU5tRmhPQQ', 3, 0, 1, @namespace_id, 'park_tourist', 0, 0);
INSERT INTO `eh_item_service_categries`(`id`, `scope_code`, `scope_id`, `name`, `label`, `item_location`, `item_group`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, scene_type, operator_uid, creator_uid)
    VALUES((@item_service_categry_id := @item_service_categry_id +1), 5, 0, 'CmntyServices', '园区服务', '/home','GovAgencies', 'cs://1/image/aW1hZ2UvTVRwbE5EVTBaamd3TmpWak9UWTRNV0ZrTkRVek1UZG1PVGRrT1RReU5tRmhPQQ', 1, 0, 1, @namespace_id, 'pm_admin', 0, 0);
INSERT INTO `eh_item_service_categries`(`id`, `scope_code`, `scope_id`, `name`, `label`, `item_location`, `item_group`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, scene_type, operator_uid, creator_uid)
    VALUES((@item_service_categry_id := @item_service_categry_id +1), 5, 0, 'PmServices', '物业服务', '/home','GovAgencies', 'cs://1/image/aW1hZ2UvTVRwbE5EVTBaamd3TmpWak9UWTRNV0ZrTkRVek1UZG1PVGRrT1RReU5tRmhPQQ', 2, 0, 1, @namespace_id, 'pm_admin', 0, 0);
INSERT INTO `eh_item_service_categries`(`id`, `scope_code`, `scope_id`, `name`, `label`, `item_location`, `item_group`, `icon_uri`, `order`, `align`, `status`, `namespace_id`, scene_type, operator_uid, creator_uid)
    VALUES((@item_service_categry_id := @item_service_categry_id +1), 5, 0, 'OrgServices', '企业服务', '/home','GovAgencies', 'cs://1/image/aW1hZ2UvTVRwbE5EVTBaamd3TmpWak9UWTRNV0ZrTkRVek1UZG1PVGRrT1RReU5tRmhPQQ', 3, 0, 1, @namespace_id, 'pm_admin', 0, 0);

SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
-- 园区服务
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','园区简介','园区简介','cs://1/image/aW1hZ2UvTVRvd01EVXpNekZoTW1NMlpUUTJaRGRsT0RCaE5qaGxNVGRqWlRsa1lURmhNUQ','1','1',13,CONCAT('{"url": "', @core_url, '/park-introduction/index.html?hideNavigationBar=1&rtToken=hWSzn3doJ6_e63mZYCLlP5mastXzHbcObCnDX-T4k4bldRIx0sYCQBBcejQ3UYgfP66cwBnE9XIFMSICI4b1CwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-flUIy8QPY_kfYuTL2u5dHRE"}'),10,'0','1',1,'','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,110,NULL, 'CmntyServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','园区简介','园区简介','cs://1/image/aW1hZ2UvTVRvd01EVXpNekZoTW1NMlpUUTJaRGRsT0RCaE5qaGxNVGRqWlRsa1lURmhNUQ','1','1',13,CONCAT('{"url": "', @core_url, '/park-introduction/index.html?hideNavigationBar=1&rtToken=hWSzn3doJ6_e63mZYCLlP5mastXzHbcObCnDX-T4k4bldRIx0sYCQBBcejQ3UYgfP66cwBnE9XIFMSICI4b1CwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-flUIy8QPY_kfYuTL2u5dHRE"}'),10,'0','1',1,'','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,110,NULL, 'CmntyServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','SERVICE_HOT_LINE','服务热线','cs://1/image/aW1hZ2UvTVRvME16WmtOVEZoWm1VelltRm1ZMkV5T0RRMU9ESmpOMkppT1RSaFl6RTFOdw','1','1',45,'',30,'0','1',1,'','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,120,NULL, 'CmntyServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','SERVICE_HOT_LINE','服务热线','cs://1/image/aW1hZ2UvTVRvME16WmtOVEZoWm1VelltRm1ZMkV5T0RRMU9ESmpOMkppT1RSaFl6RTFOdw','1','1',45,'',30,'0','1',1,'','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,120,NULL, 'CmntyServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','园区巴士','园区巴士','cs://1/image/aW1hZ2UvTVRvellqbGxOR00yT0RWa1lqVXhNak5tWWpFNE5UQTRNV0ZqWkdGalpUbGlNZw','1','1',14,'{"url":"http://wx.dudubashi.com/index.php/zuolin/Webauth/auth_login"}',40,'0','1',1,'','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,130,NULL, 'CmntyServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','园区巴士','园区巴士','cs://1/image/aW1hZ2UvTVRvellqbGxOR00yT0RWa1lqVXhNak5tWWpFNE5UQTRNV0ZqWkdGalpUbGlNZw','1','1',14,'{"url":"http://wx.dudubashi.com/index.php/zuolin/Webauth/auth_login"}',40,'0','1',1,'','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,130,NULL, 'CmntyServices');
-- 物业服务
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','DoorManagement','门禁','cs://1/image/aW1hZ2UvTVRvNVpqY3hPVGN3WVRabU9EbGhPR013TW1VMU4yRTFaV1ptTlRrd09HVXhNQQ','1','1',40,'{"isSupportQR":1,"isSupportSmart":1}',50,'0','1',1,'','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,140,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','DoorManagement','门禁','cs://1/image/aW1hZ2UvTVRvNVpqY3hPVGN3WVRabU9EbGhPR013TW1VMU4yRTFaV1ptTlRrd09HVXhNQQ','1','1',40,'{"isSupportQR":1,"isSupportSmart":1}',50,'0','1',1,'','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,140,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) 
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies', 'ENTER_PARK', '招商租凭', 'cs://1/image/aW1hZ2UvTVRvd01HSmlNRFJtTm1KbVltRTJOVFJoWW1Ga09UTmpaamN6Tm1aaU1EVTFPQQ', '1', '1', '28', '', 150, '0', '1', 0, '', '0', NULL, NULL, NULL, '0', 'park_tourist','0',NULL,NULL,150,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) 
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies', 'ENTER_PARK', '招商租凭', 'cs://1/image/aW1hZ2UvTVRvd01HSmlNRFJtTm1KbVltRTJOVFJoWW1Ga09UTmpaamN6Tm1aaU1EVTFPQQ', '1', '1', '28', '', 150, '0', '1', 0, '', '0', NULL, NULL, NULL, '0', 'pm_admin','0',NULL,NULL,150,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies', '物业缴费', '费用查询', 'cs://1/image/aW1hZ2UvTVRvMVpXVTNaVFEyTWpZNU1USTBPRE13WkRRME5qVmxPVEJsTVRSaE56TmxZUQ', 1, 1, 14, CONCAT('{"url": "', @core_url, '/property-bill/index.html?hideNavigationBar=1&name=物业查费#/verify_account#sign_suffix"}'), 160, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, 0, NULL, 160, NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies', '物业缴费', '费用查询', 'cs://1/image/aW1hZ2UvTVRvMVpXVTNaVFEyTWpZNU1USTBPRE13WkRRME5qVmxPVEJsTVRSaE56TmxZUQ', 1, 1, 14, CONCAT('{"url": "', @core_url, '/property-bill/index.html?hideNavigationBar=1&name=物业查费#/verify_account#sign_suffix"}'), 160, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, 0, NULL, 160, NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','PROPERTY_REPAIR','物业报修','cs://1/image/aW1hZ2UvTVRwaE16TTRObVEyWTJabFpqWmxOamhrTkdKa01qRmpPV1l3TURrd09HTTBZUQ','1','1',60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203531&displayName=物业报修"}',60,'0','1',1,'','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,170,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','PROPERTY_REPAIR','物业报修','cs://1/image/aW1hZ2UvTVRwaE16TTRObVEyWTJabFpqWmxOamhrTkdKa01qRmpPV1l3TURrd09HTTBZUQ','1','1',60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203531&displayName=物业报修"}',60,'0','1',1,'','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,170,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies', '设备巡检', '设备巡检', 'cs://1/image/aW1hZ2UvTVRwaU1qZzFaV1ZpWldVNE1XRXdNR1kzTkRJM1pqYzVZVEV5WXpaaE5XTmxNUQ', 1, 1, 14, CONCAT('{"url": "', @core_url, '/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}'), 180, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist','0',NULL,NULL,180,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies', '设备巡检', '设备巡检', 'cs://1/image/aW1hZ2UvTVRwaU1qZzFaV1ZpWldVNE1XRXdNR1kzTkRJM1pqYzVZVEV5WXpaaE5XTmxNUQ', 1, 1, 14, CONCAT('{"url": "', @core_url, '/equipment-inspection/dist/index.html?hideNavigationBar=1#sign_suffix"}'), 180, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin','0',NULL,NULL,180,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies', '停车缴费', '停车缴费', 'cs://1/image/aW1hZ2UvTVRwaVptRTVNRGMyWTJNMk5XUm1PR1F5WmpjMlpEQmtZbVE1WWpOa1ptRTVOUQ', 1, 1, 30, '', 190, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist','0',NULL,NULL,190,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies', '停车缴费', '停车缴费', 'cs://1/image/aW1hZ2UvTVRwaVptRTVNRGMyWTJNMk5XUm1PR1F5WmpjMlpEQmtZbVE1WWpOa1ptRTVOUQ', 1, 1, 30, '', 190, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin','0',NULL,NULL,190,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','报修投诉','投诉建议','cs://1/image/aW1hZ2UvTVRwa05qY3lPVFF6TVRObFltVXpaVE5sT1RGak9EZGhaREl5T0dSaFpqZzNaZw','1','1',60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203532&displayName=投诉建议"}',70,'0','1',1,'','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,200,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','报修投诉','投诉建议','cs://1/image/aW1hZ2UvTVRwa05qY3lPVFF6TVRObFltVXpaVE5sT1RGak9EZGhaREl5T0dSaFpqZzNaZw','1','1',60,'{"url":"zl://propertyrepair/create?type=user&taskCategoryId=203532&displayName=投诉建议"}',70,'0','1',1,'','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,200,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRvd1pUVXlNek5pT0dJMU5XSmlaVEpoTjJObE5tSm1OamRqWmpJeU5tSmhNQQ', 1, 1, 56, '', 210, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist','0',NULL,NULL,210,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRvd1pUVXlNek5pT0dJMU5XSmlaVEpoTjJObE5tSm1OamRqWmpJeU5tSmhNQQ', 1, 1, 56, '', 210, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin','0',NULL,NULL,210,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', '店铺管理', '店铺管理', 'cs://1/image/aW1hZ2UvTVRvMFkyVm1NRFEwT0dRM056WXhZVGswWW1WaU0yTTFNekl3TkdFNFltWXpOZw', 1, 1, 14, CONCAT('{"url": "', @biz_url, '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp_ng%2Fshop%2Findex.html%3F_k%3Dzlbiz#sign_suffix"}'), 220, 0, 1, 0, NULL, 0, NULL, '', '', 1, 'park_tourist','0',NULL,NULL,220,NULL, 'PmServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', '店铺管理', '店铺管理', 'cs://1/image/aW1hZ2UvTVRvMFkyVm1NRFEwT0dRM056WXhZVGswWW1WaU0yTTFNekl3TkdFNFltWXpOZw', 1, 1, 14, CONCAT('{"url": "', @biz_url, '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp_ng%2Fshop%2Findex.html%3F_k%3Dzlbiz#sign_suffix"}'), 220, 0, 1, 0, NULL, 0, NULL, '', '', 1, 'pm_admin','0',NULL,NULL,220,NULL, 'PmServices');
-- 企业服务
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','CONTACTS','通讯录','cs://1/image/aW1hZ2UvTVRveU9XRXhNemd6WVRjMk1tRTNORFUyTW1WbVpqYzNPR00zTkRFM1l6SmhPQQ','1','1',46,'',20,'0','1',1,'','0',NULL,NULL,NULL,1,'park_tourist','0',NULL,NULL,230,NULL, 'OrgServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home','GovAgencies','CONTACTS','通讯录','cs://1/image/aW1hZ2UvTVRveU9XRXhNemd6WVRjMk1tRTNORFUyTW1WbVpqYzNPR00zTkRFM1l6SmhPQQ','1','1',46,'',20,'0','1',1,'','0',NULL,NULL,NULL,1,'pm_admin','0',NULL,NULL,230,NULL, 'OrgServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRvM016WmlNREkxWVRKall6RmhObVl3WW1SbFlUSm1ORE5oTW1JM01HUmtPUQ', 1, 1, 23, '', 240, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL, 240, NULL, 'OrgServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRvM016WmlNREkxWVRKall6RmhObVl3WW1SbFlUSm1ORE5oTW1JM01HUmtPUQ', 1, 1, 23, '', 240, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL, 240, NULL, 'OrgServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', 'SERVICEALLIANCE', '服务联盟', 'cs://1/image/aW1hZ2UvTVRwak9XRmpaR05sWm1Oak5HVXlaREZoWkdSak9EUmtOREE0WW1aa09UY3daUQ', 1, 1, 33, '{"type":201228,"parentId":201228,"displayType": "grid"}', 250, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL, 250, NULL, 'OrgServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', 'SERVICEALLIANCE', '服务联盟', 'cs://1/image/aW1hZ2UvTVRwak9XRmpaR05sWm1Oak5HVXlaREZoWkdSak9EUmtOREE0WW1aa09UY3daUQ', 1, 1, 33, '{"type":201228,"parentId":201228,"displayType": "grid"}', 250, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL, 250, NULL, 'OrgServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', '视频会议', '视频会议', 'cs://1/image/aW1hZ2UvTVRwbU5HVXlOakJoTnpkbVpUWTJNakExTm1FNE16azJNelUyWWpnMFpXSXpZZw', 1, 1, 27, '', 260, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL, 260, NULL, 'OrgServices');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`)
    VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1),@namespace_id,'0','0','0','/home', 'GovAgencies', '视频会议', '视频会议', 'cs://1/image/aW1hZ2UvTVRwbU5HVXlOakJoTnpkbVpUWTJNakExTm1FNE16azJNelUyWWpnMFpXSXpZZw', 1, 1, 27, '', 260, 0, 1, 0, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL, 260, NULL, 'OrgServices');


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 1, 'zh_CN', '验证-大沙河建投', '113079');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 4, 'zh_CN', '派单-大沙河建投', '113081');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 5, 'zh_CN', '任务-大沙河建投', '113082');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 6, 'zh_CN', '任务2-大沙河建投', '113087');	
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 7, 'zh_CN', '新报修-大沙河建投', '113088');	
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 15, 'zh_CN', '物业任务3-大沙河建投', '113089');	
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 51, 'zh_CN', '视频会议一周到期-大沙河建投', '113090');	
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 52, 'zh_CN', '视频会议3天到期-大沙河建投', '113091');	

SET @app_url_id = (SELECT MAX(id) FROM `eh_app_urls`); 
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES ((@app_url_id := @app_url_id + 1), @namespace_id, '大沙河建投', '2', '', 'cs://1/image/aW1hZ2UvTVRvM05EQXpNakkzTUdFNU5qTTJOekZoWVdObU1tWTJPRFUyT0dRNVptRXhPUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES ((@app_url_id := @app_url_id + 1), @namespace_id, '大沙河建投', '1', '', 'cs://1/image/aW1hZ2UvTVRvM05EQXpNakkzTUdFNU5qTTJOekZoWVdObU1tWTJPRFUyT0dRNVptRXhPUQ', '移动平台聚合服务，助力园区效能提升');	


SET FOREIGN_KEY_CHECKS = 1;	
