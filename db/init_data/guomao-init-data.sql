-- 国贸圈
SET FOREIGN_KEY_CHECKS = 0;

SET @core_url = 'https://core.zuolin.com'; -- 取具体环境连接core server的链接
SET @biz_url = 'biz.zuolin.com'; -- 取具体环境的电商服务器连接，注意有两个域名要修改

SET @namespace_id = 999968;

-- OK
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('app.agreements.url', CONCAT(@core_url, '/mobile/static/app_agreements/agreements.html?ns=', @namespace_id), 'the relative path for guomao app agreements', @namespace_id, NULL);
# INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
# 	VALUES ((@max_conf_id := @max_conf_id + 1), 'business.url', CONCAT('https://', @biz_url, '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2F', @biz_url, '%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix'), 'biz access url for guomao', @namespace_id, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (CONCAT('pmtask.handler-', @namespace_id), 'flow', '', '0', NULL);

-- OK
SELECT max(id) FROM `eh_version_realm` INTO @ver_rea_id;
SELECT max(id) FROM `eh_version_upgrade_rules` INTO @ver_upg_id;

INSERT INTO `eh_version_realm` VALUES ((@ver_rea_id := @ver_rea_id + 1), 'Android_GuoMao', null, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
	VALUES((@ver_upg_id := @ver_upg_id + 1), @ver_rea_id,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());

INSERT INTO `eh_version_realm` VALUES ((@ver_rea_id := @ver_rea_id + 1), 'iOS_GuoMao', null, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
	VALUES((@ver_upg_id := @ver_upg_id + 1), @ver_rea_id,'-0.1','1048576','0','1.0.0','0',UTC_TIMESTAMP());


INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(@namespace_id, '国贸圈');

-- OK
SELECT max(id) FROM `eh_namespace_details` INTO @max_ns_detail_id;
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`)
	VALUES((@max_ns_detail_id := @max_ns_detail_id + 1), @namespace_id, 'community_commercial', UTC_TIMESTAMP());

-- OK
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (14837, '0', '北京', 'BEIJING', 'BJ', '/北京', '1', '1', '', '', '2', '2', @namespace_id);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (14838, 14837, '北京市', 'BEIJINGSHI', 'BJS', '/北京/北京市', '2', '2', NULL, '010', '2', '1', @namespace_id);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
	VALUES (14839, 14838, '朝阳区', 'CHAOYANGQU', 'CYQ', '/北京/北京市/朝阳区', '3', '3', NULL, '010', '2', '0', @namespace_id);


-- OK
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(240111044331050363, UUID(), 14838, '北京',  14839, '朝阳区', '中国国际贸易中心', '中国国际贸易中心', '建国门外大街1号', NULL, '',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 1, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,1, 190692, 190693, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)
	VALUES(240111044331072756, 240111044331050363, '', 116.464925, 39.915126, 'wx4g44r4mqy3');
INSERT INTO `eh_organization_communities`(organization_id, community_id)
	VALUES(1035718, 240111044331050363);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`)
	VALUES(19927, @namespace_id, 'COMMUNITY', 240111044331050363, UTC_TIMESTAMP());


-- OK
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
	VALUES(1035718, 0, 'PM', '国贸物业管理有限公司', '', '/1035718', 1, 2, 'ENTERPRISE', @namespace_id, 1041998);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
	VALUES(1143614, 240111044331050363, 'organization', 1035718, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag2`, `integral_tag4`, `creator_uid`, `namespace_id`)
	VALUES(1041998, UUID(), '国贸物业管理有限公司', '国贸物业管理有限公司', 1, 1, 1035718, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 240111044331050363, 190694, 1, @namespace_id);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(190694, UUID(), @namespace_id, 2, 'EhGroups', 1041998,'国贸物业管理有限公司论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());


-- OK
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(190692, UUID(), @namespace_id, 2, 'EhGroups', 0,'国贸圈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
	VALUES(190693, UUID(), @namespace_id, 2, 'EhGroups', 0,'国贸圈意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());


-- OK
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (316710, UUID(), '19132843825', '杨雷', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (289470, 316710 ,  '0',  '13910755049',  null,  3, UTC_TIMESTAMP(), @namespace_id);
SET @organization_members_id = IFNULL((SELECT MAX(id) FROM `eh_organization_members`), 1);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, `status`, `namespace_id`)
	VALUES ((@organization_members_id := @organization_members_id + 1), 1035718, 'USER', 316710  , 'manager', '杨雷', 0, '13910755049', 3, @namespace_id);

SELECT MAX(id) FROM `eh_user_organizations` INTO @user_organizations_id;
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`)
VALUES ((@user_organizations_id := @user_organizations_id + 1), 316710, 1035718, '/1035718', 'ENTERPRISE', 3, @namespace_id, NULL, 0, NOW());

SELECT MAX(id) FROM `eh_group_members` INTO @group_members_id;
INSERT INTO `eh_group_members` (`id`, `uuid`, `group_id`, `member_type`, `member_id`, `member_role`, `member_avatar`, `member_nick_name`, `member_status`, `create_time`, `creator_uid`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `requestor_comment`, `operation_type`, `inviter_uid`, `invite_time`, `update_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`)
VALUES ((@group_members_id := @group_members_id + 1), UUID(), 1041998, 'EhUsers', 316710, 5, '', '杨雷', 3, NOW(), 1, 1, NULL, NULL, NULL, NOW(), NULL, NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 1, 0, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES (18814, 'EhOrganizations', 1035718, 'EhUsers', 316710  , 1001, 1, UTC_TIMESTAMP());
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_type`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `scope`)
  VALUES((@acl_id := @acl_id + 1),'EhOrganizations', 1035718, 1, 10, 'EhUsers', 316710, 0, 0, NOW(), @namespace_id,'admin');


-- OK
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
	VALUES (316711, UUID(), '19132843826', '孙振华', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
	VALUES (289471, 316711 ,  '0',  '15010499864',  null,  3, UTC_TIMESTAMP(), @namespace_id);
SET @organization_members_id = IFNULL((SELECT MAX(id) FROM `eh_organization_members`), 1);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, `status`, `namespace_id`)
	VALUES((@organization_members_id := @organization_members_id + 1), 1035718, 'USER', 316711  , 'manager', '孙振华', 0, '15010499864', 3, @namespace_id);

SELECT MAX(id) FROM `eh_user_organizations` INTO @user_organizations_id;
INSERT INTO `eh_user_organizations` (`id`, `user_id`, `organization_id`, `group_path`, `group_type`, `status`, `namespace_id`, `create_time`, `visible_flag`, `update_time`)
VALUES ((@user_organizations_id := @user_organizations_id + 1), 316711, 1035718, '/1035718', 'ENTERPRISE', 3, @namespace_id, NULL, 0, NOW());

SELECT MAX(id) FROM `eh_group_members` INTO @group_members_id;
INSERT INTO `eh_group_members` (`id`, `uuid`, `group_id`, `member_type`, `member_id`, `member_role`, `member_avatar`, `member_nick_name`, `member_status`, `create_time`, `creator_uid`, `operator_uid`, `process_code`, `process_details`, `proof_resource_uri`, `approve_time`, `requestor_comment`, `operation_type`, `inviter_uid`, `invite_time`, `update_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`)
VALUES ((@group_members_id := @group_members_id + 1), UUID(), 1041998, 'EhUsers', 316711, 5, '', '孙振华', 3, NOW(), 1, 1, NULL, NULL, NULL, NOW(), NULL, NULL, NULL, NOW(), NOW(), NULL, NULL, NULL, 1, 0, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
	VALUES(18815, 'EhOrganizations', 1035718, 'EhUsers', 316711  , 1001, 1, UTC_TIMESTAMP());
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_type`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `scope`)
  VALUES((@acl_id := @acl_id + 1),'EhOrganizations', 1035718, 1, 10, 'EhUsers', 316711, 0, 0, NOW(), @namespace_id,'admin');



-- 楼栋信息 OK
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
	VALUES (1960621, 240111044331050363, '国贸大厦A座', '国贸大厦A座', 0, '010-65053868', '建国门外大街1号', 0, 116.464925, 39.915126, 'wx4g44r4mqy3', '甲级写字楼', null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id, 1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
	VALUES (1960622, 240111044331050363, '国贸大厦B座', '国贸大厦B座', 0, '010-65053868', '建国门外大街1号', 0, 116.464925, 39.915126, 'wx4g44r4mqy3', '甲级写字楼', null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id, 2);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
	VALUES (1960623, 240111044331050363, '国贸写字楼1座', '国贸写字楼1座', 0, '010-65053868', '建国门外大街1号', 0, 116.464925, 39.915126, 'wx4g44r4mqy3', '甲级写字楼', null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id, 3);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
	VALUES (1960624, 240111044331050363, '国贸写字楼2座', '国贸写字楼2座', 0, '010-65053868', '建国门外大街1号', 0, 116.464925, 39.915126, 'wx4g44r4mqy3', '甲级写字楼', null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id, 4);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`, default_order)
	VALUES (1960625, 240111044331050363, '国贸西楼', '国贸西楼', 0, '010-65053868', '国贸西楼', 0, 116.464925, 39.915126, 'wx4g44r4mqy3', '甲级写字楼', null, '2', '1', '2017-07-04 17:01:31', '311028', '2017-07-04 17:01:31', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, @namespace_id, 5);

-- 门牌信息 OK
SET @addresses_id = IFNULL((SELECT MAX(id) FROM `eh_addresses`), 1);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES((@addresses_id := @addresses_id + 1) ,UUID(),240111044331050363, 14838, '北京市',  14839, '朝阳区' ,'国贸大厦A座-710','国贸大厦A座','710','2','0',UTC_TIMESTAMP(), @namespace_id);
SET @organization_address_mappings_id = IFNULL((SELECT MAX(id) FROM `eh_organization_address_mappings`), 1);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES ((@organization_address_mappings_id := @organization_address_mappings_id + 1), 1035718, 240111044331050363, @addresses_id, '建国门外大街1号-710', '0');

SELECT max(id) FROM `eh_rentalv2_resource_types` INTO @max_ren_t_id;
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES ((@max_ren_t_id := @max_ren_t_id + 1), '会议室预订', 0, NULL, 2, @namespace_id);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES ((@max_ren_t_id := @max_ren_t_id + 1), '电子屏预订', 0, NULL, 2, @namespace_id);

SELECT max(id) FROM `eh_web_menu_scopes` INTO @menu_sc_id;
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40400, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40410, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40420, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40430, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40440, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40450, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40500, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40510, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40520, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40530, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40541, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40542, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40700, '', 'EhNamespaces', @namespace_id, 2);

SELECT max(id) FROM `eh_service_alliance_categories` INTO @ser_all_cate_id;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
  VALUES ((@ser_all_cate_id := @ser_all_cate_id + 1), 'community', '240111044331050363', '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
  VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331050363', '服务联盟', '服务联盟', @ser_all_cate_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES(@namespace_id, 'sms.default.yzx', 1, 'zh_CN', '验证-国贸圈', '111154');

-- NEW --- 08/04 17:59
SELECT MAX(id) FROM `eh_organization_details` INTO @organization_details_id;
INSERT INTO `eh_organization_details` (`id`, `organization_id`, `description`, `contact`, `address`, `create_time`, `longitude`, `latitude`, `geohash`, `display_name`)
VALUES ((@organization_details_id := @organization_details_id + 1), 1035718, NULL, '', '建国门外大街1号', NOW(), NULL, NULL, NULL, '国贸物业管理有限公司');

# SELECT MAX(id) FROM `eh_enterprise_community_map` INTO @enterprise_community_map_id;
# INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`, `creator_uid`, `create_time`, `update_time`)
# VALUES ((@enterprise_community_map_id := @enterprise_community_map_id + 1), 240111044331050363, 'enterprise', 1035718, 3, NULL, NOW(), NOW());
-- NEW END--- 08/04 17:59

SET FOREIGN_KEY_CHECKS = 1;

-- by dengs 国贸圈的菜单，加快递管理和快递设置 2017.08.29
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40710, '', 'EhNamespaces', 999968, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40720, '', 'EhNamespaces', 999968, 2);