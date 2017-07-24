
-- 保集e智谷脚本
SET FOREIGN_KEY_CHECKS = 0;
-- SET @core_server_url = "core.zuolin.com"; -- 取具体环境连接core server的链接
-- SET @biz_url = 'biz.zuolin.com'; -- 取具体环境的电商服务器连接，注意有两个域名要修改
-- SET @namespace_id=1;
-- SET @user_id = 311028;  -- 需要取现网eh_users的ID的最大值再加一定余量
-- SET @account_name='19129838143'; -- 需要取现网eh_users的acount_name的6位的最大值再加一定余量
-- SET @community_id = 240111044331050393; -- 需要取现网eh_communities的ID的最大值再加一定余量
-- SET @organization_id = 1010579; 	-- 需要取eh_organizations的ID最大值并加一定余量，如果修改此值则其path也要改
-- SET @community_geopoint_id = (SELECT MAX(id) FROM `eh_community_geopoints`);  
-- SET @org_group_id = 1012469; -- 需要取eh_groups的ID的最大值再加一定余量
-- SET @org_forum_id = 190412;   -- @community_forum_id+2
-- SET @feedback_forum_id = 190411;   -- @community_forum_id+1
-- SET @community_forum_id = 190410;   -- 取eh_forums的ID最大值再加一定余量
-- SET @building_id = 196046;   -- 取eh_buildings的ID最大值再加一定余量
-- SET @sheng_id = 14814;  -- 取eh_regions的ID最大值再加一定余量
-- SET @shi_id = 14815;  -- 在@sheng_id上加1
-- SET @qu_id = 14816;    -- 在@shi_id上加1
-- SET @address_id = 239825274387253717; -- 需要取现网eh_addresses的ID的最大值再加一定余量
-- SET @rentalv_type_id = 10612; -- 需要取现网eh_rentalv2_resource_types的ID的最大值再加一定余量
-- SET @layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`); 
-- SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`); 
-- SET @banner_id = (SELECT MAX(id) FROM `eh_banners`);
-- SET @org_member_id = (SELECT MAX(id) FROM `eh_organization_members`); 
-- SET @role_assignment_id = (SELECT MAX(id) FROM `eh_acl_role_assignments`); 
-- SET @user_identifier_id = (SELECT max(id) FROM `eh_user_identifiers`);
-- SET @organization_address_mapping_id = (SELECT max(id) FROM `eh_organization_address_mappings`);
-- SET @namespace_detail_id = (SELECT max(id) FROM `eh_namespace_details`);
-- SET @org_cmnty_request_id = (SELECT max(id) FROM `eh_organization_community_requests`);
-- SET @namespace_resource_id = (SELECT max(id) FROM `eh_namespace_resources`);
-- SET @configuration_id = 1599;

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (1599, 'app.agreements.url', 'https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=1', 'the relative path for volgo app agreements', 999973, NULL);	   
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (1600, 'business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix', 'biz access url for volgo', 999973, NULL);	   

INSERT INTO `eh_version_realm` VALUES (127, 'Android_BaoJiEZhiGu', null, UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_version_realm` VALUES (128, 'iOS_BaoJiEZhiGu', null, UTC_TIMESTAMP(), 999973);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(437,127,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(438,128,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
	
	
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999973, 'E-BOILL');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1148, 999973, 'community_commercial', UTC_TIMESTAMP());

   
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14814, '0', '上海', 'SHANGHAI', 'SH', '/上海', '1', '1', '', '', '2', '2', 999973);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14815, 14814, '上海市', 'SHANGHAISHI', 'SHS', '/上海/上海市', '2', '2', NULL, '021', '2', '1', 999973);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14816, 14815, '宝山区', 'BAOSHANQU', 'BSQ', '/上海/上海市/宝山区', '3', '3', NULL, '021', '2', '0', 999973);

-- 左邻是物业管理公司
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1041239, UUID(), '智能科技公司圈', '智能科技公司圈', 1, 1, 1010579, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 190598, 1, 999973); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(190598, UUID(), 999973, 2, 'EhGroups', 1041239,'智能科技公司论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());       
	
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`) 
	VALUES(1010579, 0, 'PM', '上海保集智能科技发展有限公司', '智能科技是保集控股集团下属全资子公司，负责产业园区的全面运营工作，以智能制造产业园投资开发为基础，以孵化器建设为核心，向产业链两延伸，建立和完善产业、运营、金融三大生态链。', '/1010579', 1, 2, 'ENTERPRISE', 999973, 1041239);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1141889, 240111044331050393, 'organization', 1010579, 3, 0, UTC_TIMESTAMP()); 
	

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (311028, UUID(), '19129838143', '沈颖慧', '', 1, 45, '1', '2',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', 'd4756718ddddf655ba8d1b397899549b2925b3833a0058a8268aeb203528bb1e', UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (283637, 311028 ,  '0',  '15900788890',  null,  3, UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
	VALUES(2155060, 1010579, 'USER', 311028  , 'manager', '沈颖慧', 0, '15900788890', 3, 999973);	
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(18597, 'EhOrganizations', 1010579, 'EhUsers', 311028  , 1001, 1, UTC_TIMESTAMP());

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES((@acl_id := @acl_id + 1),'EhOrganizations','1010579','1','10','311028','0','311028','2017-07-05 16:04:09','999973','EhUsers','admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);


INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(190575, UUID(), 999973, 2, 'EhGroups', 0,'E-BOILL论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(190597, UUID(), 999973, 2, 'EhGroups', 0,'E-BOILL意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP()); 


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331050393, UUID(), 14815, '上海市',  14816, '宝山区', '保集e智谷', '保集', '富联二路99号', NULL, '保集e智谷项目是以智能制造为产业核心的28万方智能科技产业新城，园区集投资孵化、产品研发、金融商务服务为一体，以智能制造为产业旗帜，以服务机器人、系统集成、智能硬件为扩展内涵，发挥智能制造产业优势的同时打造智能制造人才培训基地，科技研发中心，科技创新活动中心等高附加值和科技型的现代服务业平台，也为智能制造类企业量身打造了集研发、办公、中试与一体的专属空间。园区已建成“上海市院士专家服务中心”，获批“上海智能制造生产性服务业功能区”、“中国产业互联网创新实践区称号”及“宝山区军民融合创新示范基地”。',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 682, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, 190575, 190597, UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331072642, 240111044331050393, '', 121.242, 31.2234, 'wtw1wuj9rree');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1010579, 240111044331050393);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(19913, 1, 'COMMUNITY', 240111044331050393, UTC_TIMESTAMP());	

-- 楼栋信息
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196046, 240111044331050393, '富联二路177弄41幢1号', '富联二路177弄41幢1号', 0, null, '富联二路177弄41幢1号', 3223.27, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196046);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196047, 240111044331050393, '富联二路177弄42幢2号', '富联二路177弄42幢2号', 0, null, '富联二路177弄42幢2号', 3223.27, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196047);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196048, 240111044331050393, '富联二路177弄44幢3号', '富联二路177弄44幢3号', 0, null, '富联二路177弄44幢3号', 2790.91, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196048);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196049, 240111044331050393, '富联二路177弄40幢4号', '富联二路177弄40幢4号', 0, null, '富联二路177弄40幢4号', 19544.53, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196049);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196050, 240111044331050393, '富联二路177弄40幢5号', '富联二路177弄40幢5号', 0, null, '富联二路177弄40幢5号', 0.1, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196050);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196051, 240111044331050393, '富联二路177弄43幢6号', '富联二路177弄43幢6号', 0, null, '富联二路177弄43幢6号', 4335.44 , 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196051);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196052, 240111044331050393, '富联二路177弄45幢7号', '富联二路177弄45幢7号', 0, null, '富联二路177弄45幢7号', 4342.04, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196052);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196053, 240111044331050393, '富联二路177弄46幢8号', '富联二路177弄46幢8号', 0, null, '富联二路177弄46幢8号', 912.7, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196053);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196054, 240111044331050393, '富联二路177弄48幢9号', '富联二路177弄48幢9号', 0, null, '富联二路177弄48幢9号', 1133.91, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196054);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196055, 240111044331050393, '富联二路177弄50幢10号', '富联二路177弄50幢10号', 0, null, '富联二路177弄50幢10号', 1133.91, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196055);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196056, 240111044331050393, '富联二路177弄51幢11号', '富联二路177弄51幢11号', 0, null, '富联二路177弄51幢11号', 912.7, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196056);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196057, 240111044331050393, '富联二路177弄52幢12号', '富联二路177弄52幢12号', 0, null, '富联二路177弄52幢12号', 912.7, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196057);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196058, 240111044331050393, '富联二路177弄47幢13号', '富联二路177弄47幢13号', 0, null, '富联二路177弄47幢13号', 735.65, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196058);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196059, 240111044331050393, '富联二路177弄49幢14号', '富联二路177弄49幢14号', 0, null, '富联二路177弄49幢14号', 735.65, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196059);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196060, 240111044331050393, '富联二路177弄53幢15号', '富联二路177弄53幢15号', 0, null, '富联二路177弄53幢15号', 735.65, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196060);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196061, 240111044331050393, '富联二路177弄58幢16号', '富联二路177弄58幢16号', 0, null, '富联二路177弄58幢16号', 735.65, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196061);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196062, 240111044331050393, '富联二路177弄56幢17号', '富联二路177弄56幢17号', 0, null, '富联二路177弄56幢17号', 924.98, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196062);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196063, 240111044331050393, '富联二路177弄61幢18号', '富联二路177弄61幢18号', 0, null, '富联二路177弄61幢18号', 735.65, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196063);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196064, 240111044331050393, '富联二路177弄60幢19号', '富联二路177弄60幢19号', 0, null, '富联二路177弄60幢19号', 924.98, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196064);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196065, 240111044331050393, '富联二路177弄59幢20号', '富联二路177弄59幢20号', 0, null, '富联二路177弄59幢20号', 735.65, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196065);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196066, 240111044331050393, '富联二路177弄57幢21号', '富联二路177弄57幢21号', 0, null, '富联二路177弄57幢21号', 924.98, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196066);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (196067, 240111044331050393, '富联二路177弄41幢22号', '富联二路177弄41幢22号', 0, null, '富联二路177弄41幢22号', 735.65, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196067);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
	VALUES (1960468, 240111044331050393, '富联二路177弄41幢23号', '富联二路177弄41幢23号', 0, null, '富联二路177弄41幢23号', 735.65, 121.242, 31.2234, 'wtw1wuj9rree', null, null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999973, 196068);
	
-- 门牌信息
SET @eh_addresses_id = (SELECT MAX(id) FROM `eh_addresses`);
SET @mappings_id = (SELECT MAX(id) FROM `eh_organization_address_mappings`);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-1层','富联二路177弄40幢4号','1层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-1层', '0');


INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-2层','富联二路177弄40幢4号','2层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-2层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-3层','富联二路177弄40幢4号','3层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-3层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-4层','富联二路177弄40幢4号','4层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-4层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-5层','富联二路177弄40幢4号','5层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-5层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-6层','富联二路177弄40幢4号','6层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-6层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-7层','富联二路177弄40幢4号','7层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-7层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-8层','富联二路177弄40幢4号','8层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-8层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-9层','富联二路177弄40幢4号','9层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-9层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-10层','富联二路177弄40幢4号','10层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-10层', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050393, 14815, '上海市',  14816, '宝山区' ,'富联二路177弄40幢4号-11层','富联二路177弄40幢4号','11层','2','0',UTC_TIMESTAMP(), 999973);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@mappings_id := @mappings_id + 1), 1010579, 240111044331050393, @eh_addresses_id, '富联二路177弄40幢4号-11层', '0');


INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (11029, '会议室预订', 0, NULL, 0, 999973);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (11030, '电子屏预订', 0, NULL, 0, 999973);


	
-- 园区管理员  
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (204075, 999973, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvek1USXhNbVkyTURsalptVXlNemc0T1RFMllqUTJPR1ZqWXpsa016Y3haQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (204076, 999973, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvM1lXTXhNVEV4TVRSa01HTm1PV016WmpObE9UYzNZalpqTW1Gak1tVTJZdw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'pm_admin');
  
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`) 
    VALUES (596, 999973, 'ServiceMarketLayout', '{"versionCode":"2017070401","versionName":"4.7.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Metro","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":0,"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16}]}', '2017070401', '2017070401', '2', '2017-07-04 16:09:30', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116455, 999973, '0', '0', '0', '/home', 'Bizs', 'SERVICE_HOT_LINE', '园区热线', 'cs://1/image/aW1hZ2UvTVRvMlkySTBNRE0xWkRjek16TmhZek0zTVRSa016TXhZV015TXprd01EazJNUQ', '1', '1', '45', '', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116456, 999973, '0', '0', '0', '/home', 'Bizs', 'ENTER_PARK', '园区入驻', 'cs://1/image/aW1hZ2UvTVRwbE1ESTRNREV6WkRnMFpEUTJZV0ZtWWpoaE56UmtZbUZrWlRreE56STJPQQ', '1', '1', '28', '', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116457, 999973, '0', '0', '0', '/home', 'Bizs', 'PARKENTERPRISE', '园区企业', 'cs://1/image/aW1hZ2UvTVRwa1pUbGxZbU0xTW1Fek16bGhObU5pTVdGaFptWmxaVE01TVRBd1lqUm1PUQ', '1', '1', '34', '{"type":3}', 30, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116458, 999973, '0', '0', '0', '/home', 'Bizs', 'PROPERTY_REPAIR', '物业报修', 'cs://1/image/aW1hZ2UvTVRwaFpESXpaRGxpTWpBNVpURXpaVEF3TXpJellUa3pNakEwWkRFeU16VmlPQQ', '1', '1', '60', '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}', 40, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116459, 999973, '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRveE1tRmlPVEJqTW1ZMU1URmtOREZtTkRZMk0yTTVNakJsT1RZNFl6Qm1OUQ', '1', '1', '23', '', 50, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116460, 999973, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '企业通讯录', '', '1', '1', '46', '', 60, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116461, 999973, '0', '0', '0', '/home', 'OPPushBiz', 'RENTAL', '会议室预定', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '49', '{"resourceTypeId":11029,"pageType":0}', 70, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116462, 999973, '0', '0', '0', '/home', 'Bizs', 'MORE_BUTTON', '更多', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '1', '{"itemLocation":"/home","itemGroup":"Bizs"}', 80, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116463, 999973, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwaFpESXpaRGxpTWpBNVpURXpaVEF3TXpJellUa3pNakEwWkRFeU16VmlPQQ', '1', '1', '27', '', 10, '0', '1', '0', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116464, 999973, '0', '0', '0', '/home', 'Bizs', 'STATION_RESERVATION', '工位预定', 'cs://1/image/aW1hZ2UvTVRveE1tRmlPVEJqTW1ZMU1URmtOREZtTkRZMk0yTTVNakJsT1RZNFl6Qm1OUQ', '1', '1', '13', '{"url":"http://core.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116465, 999973, '0', '0', '0', '/home', 'OPPushBiz', 'RENTAL', '电子屏预定', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '49', '{"resourceTypeId":11030,"pageType":0}', 30, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116466, 999973, '0', '0', '0', '/home', 'Bizs', 'SERVICEALLIANCE', '服务联盟', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '49', '{"type":200977,"parentId":200977,"displayType": "list"}', 40, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

	
	
	
-- 园区游客
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (204077, 999973, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvek1USXhNbVkyTURsalptVXlNemc0T1RFMllqUTJPR1ZqWXpsa016Y3haQQ', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
INSERT INTO `eh_banners` (`id`, `namespace_id`, `appId`, `banner_location`, `banner_group`, `scope_code`, `scope_id`, `name`, `vendor_tag`, `poster_path`, `action_type`, `action_data`, `start_time`, `end_time`, `status`, `order`, `creator_uid`, `create_time`, `delete_time`, `scene_type`) 
    VALUES (204078, 999973, 0, '/home', 'Default', '0', '0', '/home', 'Default', 'cs://1/image/aW1hZ2UvTVRvM1lXTXhNVEV4TVRSa01HTm1PV016WmpObE9UYzNZalpqTW1Gak1tVTJZdw', '0', '', NULL, NULL, '2', '10', '0', UTC_TIMESTAMP(), NULL, 'park_tourist');
  
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`) 
    VALUES (597, 999973, 'ServiceMarketLayout', '{"versionCode":"2017070401","versionName":"4.7.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Metro","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"datetime","categoryId":0,"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16}]}', '2017070401', '2017070401', '2', '2017-07-04 16:09:30', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116467, 999973, '0', '0', '0', '/home', 'Bizs', 'SERVICE_HOT_LINE', '园区热线', 'cs://1/image/aW1hZ2UvTVRvMlkySTBNRE0xWkRjek16TmhZek0zTVRSa016TXhZV015TXprd01EazJNUQ', '1', '1', '45', '', 10, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116468, 999973, '0', '0', '0', '/home', 'Bizs', 'ENTER_PARK', '园区入驻', 'cs://1/image/aW1hZ2UvTVRwbE1ESTRNREV6WkRnMFpEUTJZV0ZtWWpoaE56UmtZbUZrWlRreE56STJPQQ', '1', '1', '28', '', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116469, 999973, '0', '0', '0', '/home', 'Bizs', 'PARKENTERPRISE', '园区企业', 'cs://1/image/aW1hZ2UvTVRwa1pUbGxZbU0xTW1Fek16bGhObU5pTVdGaFptWmxaVE01TVRBd1lqUm1PUQ', '1', '1', '34', '{"type":3}', 30, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116470, 999973, '0', '0', '0', '/home', 'Bizs', 'PROPERTY_REPAIR', '物业报修', 'cs://1/image/aW1hZ2UvTVRwaFpESXpaRGxpTWpBNVpURXpaVEF3TXpJellUa3pNakEwWkRFeU16VmlPQQ', '1', '1', '60', '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}', 40, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116471, 999973, '0', '0', '0', '/home', 'Bizs', 'PUNCH', '打卡考勤', 'cs://1/image/aW1hZ2UvTVRveE1tRmlPVEJqTW1ZMU1URmtOREZtTkRZMk0yTTVNakJsT1RZNFl6Qm1OUQ', '1', '1', '23', '', 50, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116472, 999973, '0', '0', '0', '/home', 'Bizs', 'CONTACTS', '企业通讯录', '', '1', '1', '46', '', 60, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116473, 999973, '0', '0', '0', '/home', 'OPPushBiz', 'RENTAL', '会议室预定', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '49', '{"resourceTypeId":11029,"pageType":0}', 70, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116474, 999973, '0', '0', '0', '/home', 'Bizs', 'MORE_BUTTON', '更多', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '1', '{"itemLocation":"/home","itemGroup":"Bizs"}', 80, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116475, 999973, '0', '0', '0', '/home', 'Bizs', 'VIDEO_MEETING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwaFpESXpaRGxpTWpBNVpURXpaVEF3TXpJellUa3pNakEwWkRFeU16VmlPQQ', '1', '1', '27', '', 10, '0', '1', '0', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116476, 999973, '0', '0', '0', '/home', 'Bizs', 'STATION_RESERVATION', '工位预定', 'cs://1/image/aW1hZ2UvTVRveE1tRmlPVEJqTW1ZMU1URmtOREZtTkRZMk0yTTVNakJsT1RZNFl6Qm1OUQ', '1', '1', '13', '{"url":"http://core.zuolin.com/station-booking/index.html?hideNavigationBar=1#/station_booking#sign_suffix"}', 20, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116477, 999973, '0', '0', '0', '/home', 'OPPushBiz', 'RENTAL', '电子屏预定', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '49', '{"resourceTypeId":11030,"pageType":0}', 30, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
    VALUES (116478, 999973, '0', '0', '0', '/home', 'Bizs', 'SERVICEALLIANCE', '服务联盟', 'cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ', '1', '1', '49', '{"type":200977,"parentId":200977,"displayType": "list"}', 40, '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10100,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10400,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10200,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10600,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10800,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),12200,'', 'EhNamespaces', 999973,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20100,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20140,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20150,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20155,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20160,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20170,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20180,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20158,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20190,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20191,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20192,'', 'EhNamespaces', 999973,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40100,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40110,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40120,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40200,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40210,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40220,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40300,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40400,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40410,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40420,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40430,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40440,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40500,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40510,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40520,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40530,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40541,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40542,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40750,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41010,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41020,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41030,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41040,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41050,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41060,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41100,'', 'EhNamespaces', 999973,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30500,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30600,'', 'EhNamespaces', 999973,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50100,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50110,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50200,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50210,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50220,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50300,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50400,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50500,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50600,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50630,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50631,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50632,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50633,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50640,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50650,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50651,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50652,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50653,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50700,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50710,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50720,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50730,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50800,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50810,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50820,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50830,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50840,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50850,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50860,'', 'EhNamespaces', 999973,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60000,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60100,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60200,'', 'EhNamespaces', 999973,2);


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 1, 'zh_CN', '验证-保集e智谷', '90034');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 4, 'zh_CN', '派单-保集', '90044');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 5, 'zh_CN', '任务-保集', '90047');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 6, 'zh_CN', '任务2-保集', '90052');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 7, 'zh_CN', '新报修-保集', '90053');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 15, 'zh_CN', '物业任务3-保集', '90061');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 12, 'zh_CN', '预定1-保集', '90062');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 13, 'zh_CN', '预定2-保集', '90066');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 14, 'zh_CN', '预定3-保集', '90075');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 51, 'zh_CN', '视频会-保集', '90069');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 52, 'zh_CN', '视测会-保集', '90070');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999973, 'sms.default.yzx', 53, 'zh_CN', '申诉-保集', '90076');

SET FOREIGN_KEY_CHECKS = 1;


-- 更新banner和item图片   20170707  add by yanjun
update eh_banners set poster_path = 'cs://1/image/aW1hZ2UvTVRwa01XUXdNRGMwWldSaE0yVmhaakptTldWaE9UTmhNVFF3WkdWaU1UYzBOQQ' where id in (204075, 204076, 204077, 204078) and namespace_id = 999973;

update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvNVlURXpPVGhqTnpFMll6TTRNVFEwTWprME16Vm1ObU5qWW1WalkyWmxOUQ' where id in (116460, 116472) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvM04yUXpNVGRqWldNM1ltVm1ZalU0TnpobFkyRTRNV1ZrT0RFMk9UQTJZdw' where id in (116461, 116473) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMU1EaGhNV1JrWVRsbFlqbGhNMk5sTkRRMk9UUTROalZoTkRFek5HWXpZZw' where id in (116457, 116469) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvNU16QmlaR1JsWW1aaE16Z3dNek00Wm1Ka09EQTBOemcwWXpBM05UbGhZdw' where id in (116456, 116468) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMk56aGxaV013WmpObFlqWTBaRGhrTmpobU9EbGxOVGc1TUdSa1pqWTNNdw' where id in (116455, 116467) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvNU9UUXhPV015Wm1NM01qUmhNMk0xWkRCaVl6QTRORGMxWlRFd1l6QTRPQQ' where id in (116464, 116476) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwaE5XTTVOelZqT1dJMVpUVmpOek14WWpWaVlqUXhPR0ZsWkRNeVkySmpPQQ' where id in (116459, 116471) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwaE1XSXdZelZoTlRjek9UZGhOVFZtT1RoaE9UVXpOVGczTkdRNU5USTFNUQ' where id in (116462, 116474) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvd05XSm1aRE14Tm1RMlptVXhPR05pT1dFNFl6WmxObVV3TVRFMVpXRTBOZw' where id in (116466, 116478) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwalpqRXhaR1V5T1dVNE1UVTBOekUxT0RKak5qTmtabU16T1RnMFlqazFaQQ' where id in (116458, 116470) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMllUYzRPVGswT0RBd01qQmlPRGxrTlRjMU56STFZelJoTlRReU5URTFZUQ' where id in (116465, 116477) and namespace_id = 999973;
update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRvMk16STJZekU1TjJRME9URTBaakkwTnpRd01XRTNaREJqTkdFNFl6TmpaQQ' where id in (116463, 116475) and namespace_id = 999973;

-- 更新服务广场，item显示问题 20170710 add by yanjun
update eh_launch_pad_items set display_flag = '0' where id in (116464, 116465, 116466, 116476, 116477, 116478) and namespace_id = 999973;
update eh_launch_pad_items set item_group = 'Bizs' where id in (116461, 116465, 116473, 116477) and namespace_id = 999973;

-- 更新新闻显示问题，只显示月日，不显示时间  20170710 add by yanjun
update eh_launch_pad_layouts set layout_json = '{"versionCode":"2017070401","versionName":"4.7.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Metro","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default","rowCount": 2},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"","widget":"News","instanceConfig":{"timeWidgetStyle":"date","categoryId":0,"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16}]}' where  id in (596, 597) and namespace_id = 999973;

-- 更新短信模板 20170710 add by yanjun
update  eh_locale_templates set text = 90043 where namespace_id = 999973 and scope = 'sms.default.yzx' and code = 1;

-- 缺少数据导致 园区入驻、招租管理出错。20170710 add by yanjun
SET @eh_lease_configs_id = (SELECT MAX(id) FROM `eh_lease_configs`);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`, `area_search_flag`, `display_name_str`, `display_order_str`) VALUES((@eh_lease_configs_id := @eh_lease_configs_id + 1),'999973','1','1','1','1','1','1','园区介绍, 虚位以待', '1,2');

-- 修改资源预定脚本错误，将状态更新为2  20170711 add by yanjun
UPDATE  eh_rentalv2_resource_types set status = 2 where id in (11029, 11030) and namespace_id = 999973;

-- 服务联盟初始化数据     20170711 add by yanjun
SET @parent_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', 240111044331050393, '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999973, '');

-- 更新服务联盟 item  20170711 add by yanjun
UPDATE  eh_launch_pad_items  SET action_type = '33',  action_data = CONCAT( '{"type":',  @parent_id, ',"parentId":', @parent_id, ',"displayType": "list"}') WHERE id IN (116466, 116478)  AND namespace_id = 999973;

SET @service_alliance_id = (SELECT MAX(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
VALUES ((@service_alliance_id := @service_alliance_id + 1), 0, 'community', 240111044331050393, '服务联盟', '服务联盟首页', @parent_id, '', '', '', 'cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

-- 增加统计分析子菜单  20170711 add by yanjun
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41310,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41320,'', 'EhNamespaces', 999973,2);

-- 增加用户机构关联信息  20170711 add by yanjun
SET @organization_member_details_id = (SELECT MAX(id) FROM `eh_organization_member_details`);
INSERT INTO `eh_organization_member_details` (`id`, `namespace_id`, `target_type`, `target_id`, `birthday`, `organization_id`, `contact_name`, `contact_type`, `contact_token`, `contact_description`, `employee_no`, `avatar`, `gender`, `marital_flag`, `political_status`, `native_place`, `en_name`, `reg_residence`, `id_number`, `email`, `wechat`, `qq`, `emergency_name`, `emergency_contact`, `address`, `employee_type`, `employee_status`, `employment_time`, `dimission_time`, `salary_card_number`, `social_security_number`, `provident_fund_number`, `profile_integrity`, `check_in_time`)
VALUES((@organization_member_details_id := @organization_member_details_id + 1),'999973','USER','311028',NULL,'1010579','沈颖慧','0','15900788890',NULL,NULL,NULL,'0','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,'0','2017-07-11');

UPDATE  eh_organization_members set detail_id = @organization_member_details_id, group_type = 'ENTERPRISE', group_path = '/1010579' where id = 2155060  and namespace_id = 999973;

SET @eh_user_organizations_id = (SELECT MAX(id) FROM `eh_user_organizations`);
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`)
VALUES((@eh_user_organizations_id := @eh_user_organizations_id + 1),'311028','1010579','/1010579','ENTERPRISE','3','999973',NULL,'0',NULL);


-- 更新服务联盟 item  20170713 add by yanjun
UPDATE  eh_launch_pad_items  SET action_data = REPLACE(action_data, '"displayType": "list"', '"displayType": "tab"') WHERE id IN (116466, 116478)  AND namespace_id = 999973;

-- 更新服务联盟 item  20170714 add by yanjun
SET @eh_app_urls_id = (SELECT MAX(id) FROM eh_app_urls);
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES((@eh_app_urls_id := @eh_app_urls_id + 1),'999973','保集E智谷','1','http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.haian.park','cs://1/image/aW1hZ2UvTVRwa09HVTNOekpoTVRkak5EYzNZVGd3TTJKbVl6VTFPR0kyTVdZd01qZzRNZw','移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES((@eh_app_urls_id := @eh_app_urls_id + 1),'999973','保集E智谷','2','http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.haian.park','cs://1/image/aW1hZ2UvTVRwa09HVTNOekpoTVRkak5EYzNZVGd3TTJKbVl6VTFPR0kyTVdZd01qZzRNZw','移动平台聚合服务，助力园区效能提升');


-- 招租管理-工作流菜单   20170714 add by yanjun
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40130,'', 'EhNamespaces', 999973,2);

-- 修复脚本问题  20170714 add by yanjun
UPDATE eh_namespace_resources SET namespace_id = 999973 where id = 19913;

-- 缺少字段  20170714 add by yanjun
UPDATE `eh_lease_configs` SET display_name_str = '园区介绍, 虚位以待', display_order_str = '1,2' WHERE namespace_id = 999973;

-- 增加资源预定工作流，表单管理菜单  20170714 add by yanjun
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40450,'', 'EhNamespaces', 999973,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50900,'', 'EhNamespaces', 999973,2);

-- 服务联盟模块-加审批表单  20170714 add by yanjun
SET @jump_module_id = (SELECT MAX(id) FROM `eh_service_alliance_jump_module`);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@jump_module_id := @jump_module_id + 1), 999973, '审批', 'zl://approval/create?approvalId={}&sourceId={}', 0);

-- 增加园区简介和任务管理item   20170719 add by yanjun
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES (116545,999973,'0','0','0','/home','Bizs','园区简介','园区简介','cs://1/image/aW1hZ2UvTVRwaVpXTmxNR0prTW1FMllqSXhOekF3TW1ReE5HUXpNakkzTTJFNE1ETXlOUQ','1','1','13','{\"url\":\"http://core.zuolin.com/web/lib/html/rich_text_review.html?id=56&banner=1\"}',50,'0','1','1','','0',NULL,NULL,NULL,'0','park_tourist','0',NULL,NULL,'0',NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
    VALUES (116546,999973,'0','0','0','/home','Bizs','园区简介','园区简介','cs://1/image/aW1hZ2UvTVRwaVpXTmxNR0prTW1FMllqSXhOekF3TW1ReE5HUXpNakkzTTJFNE1ETXlOUQ','1','1','13','{\"url\":\"http://core.zuolin.com/web/lib/html/rich_text_review.html?id=56&banner=1\"}',50,'0','1','1','','0',NULL,NULL,NULL,'0','pm_admin','0',NULL,NULL,'0',NULL);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (116547, 999973, '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwbU5tSXlZekl6TldGaVl6YzJOMkpsWmpsa05EQmlZbU5q', '1', '1', '56', '', 40, '0', '1', '0', '', '0', NULL, NULL, NULL, '0', 'park_tourist');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`)
    VALUES (116548, 999973, '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwbU5tSXlZekl6TldGaVl6YzJOMkpsWmpsa05EQmlZbU5q', '1', '1', '56', '', 40, '0', '1', '0', '', '0', NULL, NULL, NULL, '0', 'pm_admin');

-- 更新排序问题   20170719 add by yanjun
update eh_launch_pad_items set display_flag = '1', default_order = 60 where id in (116466, 116478) and namespace_id = 999973;
update eh_launch_pad_items set display_flag = '0' where id in (116471, 116459) and namespace_id = 999973;
update eh_launch_pad_items set display_flag = '0' where id in (116472, 116460) and namespace_id = 999973;

-- 园区简介icon 问题显示和action——data问题   20170719 add by yanjun
UPDATE  eh_launch_pad_items SET item_width = '1', action_data = CONCAT('{\"url\":\"http://core.zuolin.com/park-introduction/index.html?hideNavigationBar=1&rtToken=hWSzn3doJ6_e63mZYCLlP5mastXzHbcObCnDX-T4k4bldRIx0sYCQBBcejQ3UYgfP66cwBnE9XIFMSICI4b1CwHDH1S8kVcMvXj-Kfdu9NXbAUNs_omn50T_XT2pP9gI7J5NSA1U4WOE7QAbRsS-flUIy8QPY_kfYuTL2u5dHRE"}')  WHERE  id in (116545, 116546) and namespace_id = 999973;

-- 更新 任务管理icon图标  20170719 add by yanjun
UPDATE eh_launch_pad_items SET icon_uri = 'cs://1/image/aW1hZ2UvTVRwbU5tSXlZekl6TldGaVl6YzJOMkpsWmpsa05EQmlZbU5qTXprMU4yUXhaQQ' WHERE id IN (116547, 116548) AND namespace_id = 999973;




