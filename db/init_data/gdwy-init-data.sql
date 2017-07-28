
-- 互联网产业园脚本
SET FOREIGN_KEY_CHECKS = 0;
-- SET @core_server_url = "core.zuolin.com"; -- 取具体环境连接core server的链接
-- SET @biz_url = 'biz.zuolin.com'; -- 取具体环境的电商服务器连接，注意有两个域名要修改
-- SET @namespace_id=1;
-- SET @user_id = 311028;  -- 需要取现网eh_users的ID的最大值再加一定余量
-- SET @account_name='19129838143'; -- 需要取现网eh_users的acount_name的6位的最大值再加一定余量
-- SET @community_id = 240111044331050367; -- 需要取现网eh_communities的ID的最大值再加一定余量
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
	VALUES (1704, 'app.agreements.url', 'https://core.zuolin.com/mobile/static/app_agreements/agreements.html?ns=999970', 'the relative path for gdwy app agreements', 999970, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES (1705, 'business.url', 'https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix', 'biz access url for gdwy', 999970, NULL);

INSERT INTO `eh_version_realm` VALUES (148, 'Android_GDWY', null, UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_version_realm` VALUES (149, 'iOS_GDWY', null, UTC_TIMESTAMP(), 999970);

INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(571,148,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`) VALUES(572,149,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());
	
	
INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(999970, '东莞互联网产业园');
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`) 
	VALUES(1160, 999970, 'community_commercial', UTC_TIMESTAMP());

   
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14847, '0', '广东省', 'GUANGDONGSHENG', 'GDS', '/广东省', '1', '1', '', '', '2', '2', 999970);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14848, 14847, '东莞市', 'DONGGUANSHI', 'DGS', '/广东省/东莞市', '2', '2', NULL, '0769', '2', '1', 999970);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (14849, 14848, '松山湖区', 'SONGSHANHUQU', 'SSHQ', '/广东省/东莞市/松山湖区', '3', '3', NULL, '0769', '2', '0', 999970);

-- 广东网游科技是物业管理公司
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1041934, UUID(), '广东网游科技公司圈', '广东网游科技公司圈', 1, 1, 1024527, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 190687, 1, 999970);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(190687, UUID(), 999970, 2, 'EhGroups', 1041934,'广东网游科技公司论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1024527, 0, 'PM', '广东网游科技发展有限公司', '广东网游网络科技有限公司是互联网产业园开发商，拥有成熟的产业园运营体系，以及专业的运营团队。也是东莞网络文化协会会长，群聚东莞地区乃至全国影响力最大的知名网站、论坛、新媒体渠道的行业协会。', '/1024527', 1, 2, 'ENTERPRISE', 999970, 1041934);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time) 
	VALUES(1142545, 240111044331050367, 'organization', 1024527, 3, 0, UTC_TIMESTAMP());
	

INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (316703, UUID(), '19132843815', '林毓群', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (289307, 316703 ,  '0',  '13751356670',  null,  3, UTC_TIMESTAMP(), 999970);
-- 增加用户机构关联信息
SET @organization_member_details_id = (SELECT MAX(id) FROM `eh_organization_member_details`);
INSERT INTO `eh_organization_member_details` (`id`, `namespace_id`, `target_type`, `target_id`, `birthday`, `organization_id`, `contact_name`, `contact_type`, `contact_token`, `contact_description`, `employee_no`, `avatar`, `gender`, `marital_flag`, `political_status`, `native_place`, `en_name`, `reg_residence`, `id_number`, `email`, `wechat`, `qq`, `emergency_name`, `emergency_contact`, `address`, `employee_type`, `employee_status`, `employment_time`, `dimission_time`, `salary_card_number`, `social_security_number`, `provident_fund_number`, `profile_integrity`, `check_in_time`)
VALUES((@organization_member_details_id := @organization_member_details_id + 1),'999970','USER','316703',NULL,'1024527','林毓群','0','13751356670',NULL,NULL,NULL,'0','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'0',NULL,NULL,NULL,NULL,NULL,'0','2017-07-27');
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`, detail_id, group_type, group_path)
	VALUES(2166723, 1024527, 'USER', 316703  , 'manager', '林毓群', 0, '13751356670', 3, 999970, @organization_member_details_id, 'ENTERPRISE', '/1024527');

INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(18705, 'EhOrganizations', 1024527, 'EhUsers', 316703  , 1001, 1, UTC_TIMESTAMP());
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `role_type`, `scope`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `comment_tag1`, `comment_tag2`, `comment_tag3`, `comment_tag4`, `comment_tag5`) VALUES((@acl_id := @acl_id + 1),'EhOrganizations','1024527','1','10','316703','0','316703','2017-07-27 10:04:09','999970','EhUsers','admin',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

SET @eh_user_organizations_id = (SELECT MAX(id) FROM `eh_user_organizations`);
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`)
VALUES((@eh_user_organizations_id := @eh_user_organizations_id + 1),'316703','1024527','/1024527','ENTERPRISE','3','999970',NULL,'0',NULL);


INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(190689, UUID(), 999970, 2, 'EhGroups', 0,'互联网产业园论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`) 
	VALUES(190691, UUID(), 999970, 2, 'EhGroups', 0,'互联网产业园意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331050367, UUID(), 14848, '东莞市',  14849, '松山湖区', '互联网产业园', '互联网产业园', '东莞市松山湖工业西路14号', NULL, '在互联网+的国家战略和城市加速转型升级背景下，以互联网创客孵化器、互联网孵化器链条为核心的互联网园区，是东莞首个以互联网+为主题的服务平台园区规模5.6万㎡，位于松山湖国家高新技术产业开发区，不仅加速了东莞传统行业的技术革新与产业升级，更促进了东莞可的持续发展，让其综合竞争力不断攀升。',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 682, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, 190689, 190691, UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331092656, 240111044331050367, '', 113.894763, 22.958744, 'ws0fxex8kk4d');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1024527, 240111044331050367);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(19937, 999970, 'COMMUNITY', 240111044331050367, UTC_TIMESTAMP());

-- 楼栋信息
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order) 
    VALUES (1970521, 240111044331050367, '1栋', '1栋', 0, '13415910977', '东莞市松山湖工业西路14号', 17068.29, 113.894763, 22.958744, 'ws0fxex8kk4d', '高层写字楼', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970521);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970522, 240111044331050367, '2栋', '2栋', 0, '13415910977', '东莞市松山湖工业西路14号', 9882.25, 113.894763, 22.958744, 'ws0fxex8kk4d', '多层公寓及写字楼', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970522);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970523, 240111044331050367, '3栋', '3栋', 0, '13415910977', '东莞市松山湖工业西路14号', 6628.21, 113.894763, 22.958744, 'ws0fxex8kk4d', '高层写字楼', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970523);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970524, 240111044331050367, '4栋', '4栋', 0, '13925558885', '东莞市松山湖工业西路14号', 2298.6, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970524);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970525, 240111044331050367, '5栋', '5栋', 0, '13922991126', '东莞市松山湖工业西路14号', 752.77, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970525);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970526, 240111044331050367, '6栋', '6栋', 0, '13922991126', '东莞市松山湖工业西路14号', 803.67, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970526);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970527, 240111044331050367, '7栋', '7栋', 0, '13751321269', '东莞市松山湖工业西路14号', 774.22, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970527);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970528, 240111044331050367, '8栋', '8栋', 0, null, '东莞市松山湖工业西路14号', 738.37, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970528);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970529, 240111044331050367, '9栋', '9栋', 0, null, '东莞市松山湖工业西路14号', 803.25, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970529);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970530, 240111044331050367, '10栋', '10栋', 0, null, '东莞市松山湖工业西路14号', 805.97, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970530);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970531, 240111044331050367, '11栋', '11栋', 0, '0755-25520098', '东莞市松山湖工业西路14号', 1021.03, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970531);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970532, 240111044331050367, '12栋', '12栋', 0, '0769-83216688', '东莞市松山湖工业西路14号', 836.14, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970532);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970533, 240111044331050367, '13栋', '13栋', 0, '13669834003', '东莞市松山湖工业西路14号', 836.14, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970533);

INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
    VALUES (1970534, 240111044331050367, '14栋', '14栋', 0, '18098239777', '东莞市松山湖工业西路14号', 770.89, 113.894763, 22.958744, 'ws0fxex8kk4d', '独栋企业总部', null, '2', '1', '2017-07-27 17:01:31', '316703', '2017-07-27 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999970, 1970534);


-- 门牌信息
SET @eh_addresses_id = (SELECT MAX(id) FROM `eh_addresses`);
SET @mappings_id = (SELECT MAX(id) FROM `eh_organization_address_mappings`);

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-101','1栋','101','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-101', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-102','1栋','102','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-102', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-201','1栋','201','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-201', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-301','1栋','301','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-301', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-401','1栋','401','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-401', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-402','1栋','402','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-402', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-403','1栋','403','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-403', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-405','1栋','405','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-405', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-406','1栋','406','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-406', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-407','1栋','407','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-407', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-408','1栋','408','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-408', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-409','1栋','409','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-409', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-501','1栋','501','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-501', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-502','1栋','502','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-502', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-503','1栋','503','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-503', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-505','1栋','505','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-505', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-506','1栋','506','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-506', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-507','1栋','507','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-507', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-508','1栋','508','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-508', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-509','1栋','509','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-509', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-601','1栋','601','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-601', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-602','1栋','602','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-602', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-603','1栋','603','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-603', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-605','1栋','605','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-605', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-606','1栋','606','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-606', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-607','1栋','607','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-607', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-608','1栋','608','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-608', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-609','1栋','609','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-609', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-701','1栋','701','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-701', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-702','1栋','702','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-702', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-703','1栋','703','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-703', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-705','1栋','705','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-705', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-706','1栋','706','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-706', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-707','1栋','707','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-707', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-708','1栋','708','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-708', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-709','1栋','709','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-709', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-801','1栋','801','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-801', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-802','1栋','802','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-802', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-803','1栋','803','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-803', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-805','1栋','805','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-805', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-806','1栋','806','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-806', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-807','1栋','807','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-807', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-808','1栋','808','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-808', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-809','1栋','809','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-809', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-901','1栋','901','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-901', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-902','1栋','902','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-902', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-903','1栋','903','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-903', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-905','1栋','905','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-905', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-906','1栋','906','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-906', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-907','1栋','907','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-907', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-908','1栋','908','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-908', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-909','1栋','909','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-909', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1001','1栋','1001','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1001', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1002','1栋','1002','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1002', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1003','1栋','1003','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1003', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1005','1栋','1005','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1005', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1006','1栋','1006','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1006', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1007','1栋','1007','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1007', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1008','1栋','1008','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1008', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1009','1栋','1009','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1009', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1101','1栋','1101','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1101', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1102','1栋','1102','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1102', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1103','1栋','1103','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1103', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1105','1栋','1105','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1105', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1106','1栋','1106','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1106', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1107','1栋','1107','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1107', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1108','1栋','1108','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1108', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1109','1栋','1109','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1109', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1201','1栋','1201','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1201', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1202','1栋','1202','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1202', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1203','1栋','1203','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1203', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1205','1栋','1205','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1205', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1206','1栋','1206','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1206', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1207','1栋','1207','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1207', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1208','1栋','1208','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1208', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1209','1栋','1209','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1209', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1301','1栋','1301','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1301', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1302','1栋','1302','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1302', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1303','1栋','1303','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1303', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1305','1栋','1305','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1305', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1306','1栋','1306','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1306', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1307','1栋','1307','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1307', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1308','1栋','1308','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1308', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1309','1栋','1309','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1309', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1401','1栋','1401','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1401', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1402','1栋','1402','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1402', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1403','1栋','1403','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1403', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1405','1栋','1405','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1405', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1406','1栋','1406','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1406', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1407','1栋','1407','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1407', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1408','1栋','1408','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1408', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1409','1栋','1409','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1409', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1501','1栋','1501','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1501', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1502','1栋','1502','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1502', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1503','1栋','1503','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1503', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1505','1栋','1505','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1505', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1506','1栋','1506','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1506', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1507','1栋','1507','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1507', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1508','1栋','1508','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1508', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1509','1栋','1509','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1509', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1601','1栋','1601','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1601', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1602','1栋','1602','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1602', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1603','1栋','1603','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1603', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1605','1栋','1605','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1605', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1606','1栋','1606','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1606', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1607','1栋','1607','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1607', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1608','1栋','1608','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1608', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1609','1栋','1609','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1609', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1701','1栋','1701','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1701', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1702','1栋','1702','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1702', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1703','1栋','1703','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1703', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1705','1栋','1705','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1705', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1706','1栋','1706','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1706', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1707','1栋','1707','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1707', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1708','1栋','1708','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1708', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'1栋-1709','1栋','1709','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '1栋-1709', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-101','2栋','101','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-101', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-102','2栋','102','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-102', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-103','2栋','103','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-103', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-106','2栋','106','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-106', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-107','2栋','107','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-107', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-110','2栋','110','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-110', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-201','2栋','201','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-201', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-301','2栋','301','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-301', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-302','2栋','302','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-302', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-303','2栋','303','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-303', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-305','2栋','305','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-305', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-306','2栋','306','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-306', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-307','2栋','307','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-307', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-308','2栋','308','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-308', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-309','2栋','309','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-309', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-310','2栋','310','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-310', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-401','2栋','401','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-401', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-402','2栋','402','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-402', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-403','2栋','403','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-403', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-405','2栋','405','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-405', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-406','2栋','406','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-406', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-407','2栋','407','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-407', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-408','2栋','408','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-408', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-409','2栋','409','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-409', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-410','2栋','410','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-410', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-501','2栋','501','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-501', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-502','2栋','502','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-502', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-503','2栋','503','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-503', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-505','2栋','505','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-505', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-506','2栋','506','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-506', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-507','2栋','507','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-507', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-508','2栋','508','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-508', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-509','2栋','509','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-509', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-510','2栋','510','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-510', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-601','2栋','601','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-601', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-602','2栋','602','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-602', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-603','2栋','603','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-603', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-604','2栋','604','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-604', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-605','2栋','605','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-605', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-606','2栋','606','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-606', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-607','2栋','607','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-607', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-608','2栋','608','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-608', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-609','2栋','609','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-609', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-610','2栋','610','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-610', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-611','2栋','611','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-611', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-612','2栋','612','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-612', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-613','2栋','613','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-613', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-614','2栋','614','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-614', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-615','2栋','615','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-615', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-616','2栋','616','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-616', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-617','2栋','617','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-617', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-618','2栋','618','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-618', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-701','2栋','701','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-701', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-702','2栋','702','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-702', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-703','2栋','703','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-703', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-704','2栋','704','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-704', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-705','2栋','705','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-705', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-706','2栋','706','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-706', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-707','2栋','707','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-707', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-708','2栋','708','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-708', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-709','2栋','709','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-709', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-710','2栋','710','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-710', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-711','2栋','711','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-711', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-712','2栋','712','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-712', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-713','2栋','713','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-713', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-714','2栋','714','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-714', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-715','2栋','715','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-715', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-716','2栋','716','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-716', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-717','2栋','717','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-717', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-718','2栋','718','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-718', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-801','2栋','801','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-801', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-802','2栋','802','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-802', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-803','2栋','803','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-803', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-804','2栋','804','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-804', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-805','2栋','805','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-805', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-806','2栋','806','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-806', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-807','2栋','807','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-807', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-808','2栋','808','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-808', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-809','2栋','809','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-809', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-810','2栋','810','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-810', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-811','2栋','811','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-811', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-812','2栋','812','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-812', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-813','2栋','813','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-813', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-814','2栋','814','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-814', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-815','2栋','815','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-815', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-816','2栋','816','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-816', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-817','2栋','817','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-817', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-818','2栋','818','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-818', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-901','2栋','901','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-901', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-902','2栋','902','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-902', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-903','2栋','903','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-903', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-904','2栋','904','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-904', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-905','2栋','905','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-905', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-906','2栋','906','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-906', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-907','2栋','907','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-907', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-908','2栋','908','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-908', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-909','2栋','909','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-909', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-910','2栋','910','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-910', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-911','2栋','911','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-911', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-912','2栋','912','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-912', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-913','2栋','913','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-913', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-914','2栋','914','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-914', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-915','2栋','915','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-915', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-916','2栋','916','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-916', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-917','2栋','917','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-917', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-918','2栋','918','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-918', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1001','2栋','1001','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1001', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1002','2栋','1002','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1002', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1003','2栋','1003','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1003', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1004','2栋','1004','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1004', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1005','2栋','1005','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1005', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1006','2栋','1006','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1006', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1007','2栋','1007','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1007', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1008','2栋','1008','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1008', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1009','2栋','1009','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1009', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1010','2栋','1010','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1010', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1011','2栋','1011','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1011', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1012','2栋','1012','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1012', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1013','2栋','1013','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1013', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1014','2栋','1014','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1014', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1015','2栋','1015','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1015', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1016','2栋','1016','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1016', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1017','2栋','1017','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1017', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'2栋-1018','2栋','1018','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '2栋-1018', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-101','3栋','101','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-101', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-102','3栋','102','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-102', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-103','3栋','103','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-103', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-105','3栋','105','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-105', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-106','3栋','106','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-106', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-107','3栋','107','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-107', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-108','3栋','108','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-108', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-109A','3栋','109A','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-109A', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-109B','3栋','109B','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-109B', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-110A','3栋','110A','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-110A', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-110B','3栋','110B','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-110B', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-111A','3栋','111A','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-111A', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-111B','3栋','111B','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-111B', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-112A','3栋','112A','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-112A', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-112B','3栋','112B','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-112B', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-113A','3栋','113A','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-113A', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-113B','3栋','113B','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-113B', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-A201','3栋','A201','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-A201', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B201','3栋','B201','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B201', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B202','3栋','B202','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B202', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B203','3栋','B203','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B203', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B205','3栋','B205','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B205', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B206','3栋','B206','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B206', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B207','3栋','B207','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B207', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B208','3栋','B208','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B208', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B209-210','3栋','B209-210','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B209-210', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B211-212','3栋','B211-212','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B211-212', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B213-215','3栋','B213-215','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B213-215', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B216-217','3栋','B216-217','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B216-217', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B218-219','3栋','B218-219','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B218-219', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B220','3栋','B220','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B220', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B221','3栋','B221','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B221', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B222','3栋','B222','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B222', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B223','3栋','B223','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B223', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-A301','3栋','A301','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-A301', '4');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B301','3栋','B301','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B301', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B302','3栋','B302','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B302', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B303','3栋','B303','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B303', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B305','3栋','B305','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B305', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B306','3栋','B306','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B306', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B307','3栋','B307','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B307', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B308','3栋','B308','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B308', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B309-310','3栋','B309-310','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B309-310', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B311','3栋','B311','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B311', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B312','3栋','B312','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B312', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B313','3栋','B313','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B313', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B315','3栋','B315','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B315', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B316','3栋','B316','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B316', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B317','3栋','B317','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B317', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B318','3栋','B318','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B318', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-A401','3栋','A401','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-A401', '3');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B401','3栋','B401','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B401', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B402','3栋','B402','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B402', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B403','3栋','B403','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B403', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B405','3栋','B405','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B405', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B406','3栋','B406','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B406', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B407','3栋','B407','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B407', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B408','3栋','B408','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B408', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B409','3栋','B409','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B409', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B410','3栋','B410','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B410', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B411-412','3栋','B411-412','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B411-412', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B421','3栋','B421','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B421', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B422','3栋','B422','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B422', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B423-425','3栋','B423-425','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B423-425', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B426','3栋','B426','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B426', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B427','3栋','B427','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B427', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'3栋-B428','3栋','B428','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '3栋-B428', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'4栋-101','4栋','101','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '4栋-101', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'4栋-102','4栋','102','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '4栋-102', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'4栋-103','4栋','103','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '4栋-103', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'5栋-5栋','5栋','5栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '5栋-5栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'6栋-6栋','6栋','6栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '6栋-6栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'7栋-7栋','7栋','7栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '7栋-7栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'8栋-8栋','8栋','8栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '8栋-8栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'9栋-9栋','9栋','9栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '9栋-9栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'10栋-10栋','10栋','10栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '10栋-10栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'11栋-11栋','11栋','11栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '11栋-11栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'12栋-12栋','12栋','12栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '12栋-12栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'13栋-13栋','13栋','13栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '13栋-13栋', '2');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
    VALUES((@eh_addresses_id := @eh_addresses_id + 1),UUID(),240111044331050367, 14848, '东莞市',  14849, '松山湖区' ,'14栋-14栋','14栋','14栋','2','0',UTC_TIMESTAMP(), 999970);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
    VALUES ((@mappings_id := @mappings_id + 1), 1024527, 240111044331050367, @eh_addresses_id, '14栋-14栋', '2');

-- 缺省item和layout，等UI和项目经理定好之后再配

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10100,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10400,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10200,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10600,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10800,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999970,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20100,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20140,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20150,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20155,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20170,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20180,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20158,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20190,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20191,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20192,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20400,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20410,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20420,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20800,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20810,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20811,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20812,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20820,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20821,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20822,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20830,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20831,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20840,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20841,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20600,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20610,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20620,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20630,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20640,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20650,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20670,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20671,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20672,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20673,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20680,'', 'EhNamespaces', 999970,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),49100,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),49110,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),49120,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),49130,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),49140,'', 'EhNamespaces', 999970,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40300,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40400,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40410,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40420,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40430,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40440,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40450,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40500,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40510,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40520,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40530,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40541,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40542,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41310,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41320,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40800,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40810,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40830,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40840,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40850,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41010,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41020,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41030,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41040,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41050,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41060,'', 'EhNamespaces', 999970,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30500,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30600,'', 'EhNamespaces', 999970,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50100,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50110,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50200,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50210,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50220,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50300,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50400,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50500,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50600,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50630,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50631,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50632,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50633,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50640,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50650,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50651,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50652,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50653,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50700,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50710,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50720,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50730,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50900,'', 'EhNamespaces', 999970,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60000,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60100,'', 'EhNamespaces', 999970,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),60200,'', 'EhNamespaces', 999970,2);


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 1, 'zh_CN', '验证码-互产', '108292');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 4, 'zh_CN', '派单-互产', '108293');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 5, 'zh_CN', '任务-互产', '108294');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 6, 'zh_CN', '任务2-互产', '108297');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 7, 'zh_CN', '新报修-互产', '108301');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 15, 'zh_CN', '物业任务3-互产', '108302');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 51, 'zh_CN', '视频会-互产', '108303');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 52, 'zh_CN', '视测会-互产', '108306');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`) VALUES(999970, 'sms.default.yzx', 53, 'zh_CN', '申诉-互产', '108307');



--  园区入驻、招租管理需要这条数据
SET @eh_lease_configs_id = (SELECT MAX(id) FROM `eh_lease_configs`);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`, `area_search_flag`, `display_name_str`, `display_order_str`) VALUES((@eh_lease_configs_id := @eh_lease_configs_id + 1),'999970','1','1','1','1','1','1','园区介绍, 虚位以待', '1,2');


-- 服务联盟初始化数据    
SET @parent_id = (SELECT MAX(id) FROM `eh_service_alliance_categories`);
SET @parent_id = @parent_id + 1;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES (@parent_id, 'community', 240111044331050367, '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, 999970, '');

SET @service_alliance_id = (SELECT MAX(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`, `module_url`, `contact_memid`, `support_type`, `button_title`)
VALUES ((@service_alliance_id := @service_alliance_id + 1), 0, 'community', 240111044331050367, '服务联盟', '服务联盟首页', @parent_id, '', '', '', 'cs://1/image/aW1hZ2UvTVRvMU56TXpOV0l3T1RKaFlqQTRNVFJpWmpSaVlUazFNall5WldRNVlUZ3dZUQ', 2, NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, NULL);

SET @eh_app_urls_id = (SELECT MAX(id) FROM eh_app_urls);
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES((@eh_app_urls_id := @eh_app_urls_id + 1),'999970','互联网产业园','1','http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.haian.park','cs://1/image/aW1hZ2UvTVRwa09HVTNOekpoTVRkak5EYzNZVGd3TTJKbVl6VTFPR0kyTVdZd01qZzRNZw','移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) VALUES((@eh_app_urls_id := @eh_app_urls_id + 1),'999970','互联网产业园','2','http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.haian.park','cs://1/image/aW1hZ2UvTVRwa09HVTNOekpoTVRkak5EYzNZVGd3TTJKbVl6VTFPR0kyTVdZd01qZzRNZw','移动平台聚合服务，助力园区效能提升');

-- 服务联盟模块-加审批表单
SET @jump_module_id = (SELECT MAX(id) FROM `eh_service_alliance_jump_module`);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) VALUES ((@jump_module_id := @jump_module_id + 1), 999970, '审批', 'zl://approval/create?approvalId={}&sourceId={}', 0);


SET FOREIGN_KEY_CHECKS = 1;


