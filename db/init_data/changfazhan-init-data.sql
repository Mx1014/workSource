-- 昌发展
SET FOREIGN_KEY_CHECKS = 0;

SET @core_url = 'https://core.zuolin.com'; -- 取具体环境连接core server的链接
SET @biz_url = 'biz.zuolin.com'; -- 取具体环境的电商服务器连接，注意有两个域名要修改

SET @namespace_id = 999969;

-- OK
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ('app.agreements.url', CONCAT(@core_url, '/mobile/static/app_agreements/agreements.html?ns=', @namespace_id), 'the relative path for changfazhan app agreements', @namespace_id, NULL);
-- INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
-- VALUES (1661, 'business.url', CONCAT('https://', @biz_url, '/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2F', @biz_url, '%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix'), 'biz access url for changfazhan', @namespace_id, NULL);
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


INSERT INTO `eh_namespaces`(`id`, `name`) VALUES(@namespace_id, '昌发展');

-- OK
SELECT max(id) FROM `eh_namespace_details` INTO @max_ns_detail_id;
INSERT INTO `eh_namespace_details` (`id`, `namespace_id`, `resource_type`, `create_time`)
VALUES((@max_ns_detail_id := @max_ns_detail_id + 1), @namespace_id, 'community_commercial', UTC_TIMESTAMP());

-- OK
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
VALUES (14840, '0', '北京', 'BEIJING', 'BJ', '/北京', '1', '1', '', '', '2', '2', @namespace_id);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
VALUES (14841, 14840, '北京市', 'BEIJINGSHI', 'BJS', '/北京/北京市', '2', '2', NULL, '010', '2', '1', @namespace_id);
INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`)
VALUES (14842, 14841, '昌平区', 'CHANGPINGQU', 'CPQ', '/北京/北京市/昌平区', '3', '3', NULL, '010', '2', '0', @namespace_id);

-- OK
INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `description`, `apt_count`, `creator_uid`, `status`, `create_time`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
VALUES(240111044331050362, UUID(), 14841, '北京市',  14842, '昌平区', '昌平科技园', '龙域中心', '北京市昌平区回龙观龙域中街一号院', '', 0, 1,'2',UTC_TIMESTAMP(), 0, 190696, 190697, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`)
VALUES(240111044331072757, 240111044331050362, '', 116.323584, 40.07254, 'wx4eznbpryyu');
INSERT INTO `eh_organization_communities`(organization_id, community_id)
VALUES(1035719, 240111044331050362);
INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`)
VALUES(19928, @namespace_id, 'COMMUNITY', 240111044331050362, UTC_TIMESTAMP());

-- OK
INSERT INTO `eh_organizations` (`id`, `parent_id`, `organization_type`, `name`, `description`, `path`, `level`, `status`, `group_type`, `namespace_id`, `group_id`)
VALUES(1035719, 0, 'PM', '北京均豪物业管理有限公司', '', '/1035719', 1, 2, 'ENTERPRISE', @namespace_id, 1041998);
INSERT INTO `eh_organization_community_requests` (id, community_id, member_type, member_id, member_status, creator_uid, create_time)
VALUES(1143615, 240111044331050361, 'organization', 1035719, 3, 0, UTC_TIMESTAMP());
INSERT INTO `eh_groups` (`id`, `uuid`, `name`, `display_name`, `status`, `visible_region_type`, `visible_region_id`,`discriminator`, `private_flag`, `join_policy`, `update_time`, `create_time`, `integral_tag4`, `creator_uid`, `namespace_id`)
VALUES(1041998, UUID(), '北京均豪物业管理有限公司', '北京均豪物业管理有限公司', 1, 1, 1035719, 'enterprise',  1, 1, UTC_TIMESTAMP(), UTC_TIMESTAMP(), 190695, 1, @namespace_id);
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
VALUES(190695, UUID(), @namespace_id, 2, 'EhGroups', 1041998,'北京均豪物业管理有限公司论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

-- OK
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
VALUES(190696, UUID(), @namespace_id, 2, 'EhGroups', 0,'昌发展论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());
INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
VALUES(190697, UUID(), @namespace_id, 2, 'EhGroups', 0,'昌发展意见反馈论坛','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());


-- OK
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
VALUES (316712, UUID(), '19132843827', '丁磊', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
VALUES (289472, 316712 ,  '0',  '18610943566',  null,  3, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
VALUES(2157939, 1035719, 'USER', 316712  , 'manager', '丁磊', 0, '18610943566', 3, @namespace_id);
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
VALUES(18816, 'EhOrganizations', 1035719, 'EhUsers', 316712  , 1001, 1, UTC_TIMESTAMP());
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_type`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `scope`)
VALUES((@acl_id := @acl_id + 1),'EhOrganizations', 1035719, 1, 10, 'EhUsers', 316712, 0, 0, NOW(), @namespace_id,'admin');


-- OK
INSERT INTO `eh_users` (`id`,  `uuid`,  `account_name`,  `nick_name`, `avatar`, `status`, `points`, `level`, `gender`, `locale`, `salt`, `password_hash`, `create_time`, `namespace_id`)
VALUES (316713, UUID(), '19132843828', '高力奇', '', 1, 45, '1', '1',  'zh_CN',  '3023538e14053565b98fdfb2050c7709', '3f2d9e5202de37dab7deea632f915a6adc206583b3f228ad7e101e5cb9c4b199', UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_user_identifiers` (`id`,  `owner_uid`,  `identifier_type`,  `identifier_token`,  `verification_code`,  `claim_status`, `create_time`, `namespace_id`)
VALUES (289473, 316713 ,  '0',  ' ',  null,  3, UTC_TIMESTAMP(), @namespace_id);
INSERT INTO `eh_organization_members`(id, organization_id, target_type, target_id, member_group, contact_name, contact_type, contact_token, status, `namespace_id`)
VALUES(2157940, 1010579, 'USER', 316713  , 'manager', '高力奇', 0, '13601388991', 3, @namespace_id);
INSERT INTO `eh_acl_role_assignments`(id, owner_type, owner_id, target_type, target_id, role_id, creator_uid, create_time)
VALUES(18817, 'EhOrganizations', 1035719, 'EhUsers', 316713  , 1001, 1, UTC_TIMESTAMP());
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_type`, `role_id`, `order_seq`, `creator_uid`, `create_time`, `namespace_id`, `scope`)
VALUES((@acl_id := @acl_id + 1),'EhOrganizations', 1035719, 1, 10, 'EhUsers', 316713, 0, 0, NOW(), @namespace_id,'admin');


-- 楼栋信息 OK
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order)
VALUES (1960626, 240111044331050362, '昌发展龙域中心一单元', '龙域东塔', 0, '', '北京市昌平区回龙观龙域中街一号院', 21747.53, 116.323584, 40.07254, 'wx4eznbpryyu', '龙域中心东塔', null, '2', '1', NOW(), '1', NOW(), @namespace_id, 1);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `namespace_id`, default_order)
VALUES (1960627, 240111044331050362, '昌发展龙域中心二单元', '龙域西塔', 0, '', '北京市昌平区回龙观龙域中街一号院', 9768.35, 116.323584, 40.07254, 'wx4eznbpryyu', '龙域中心西塔', null, '2', '1', NOW(), '1', NOW(), @namespace_id, 2);

-- 门牌信息 OK
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262769, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-501','龙域中心一单元','501','2','0',UTC_TIMESTAMP(), @namespace_id, 494.99);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37065, 1035719, 240111044331050362, 239825274387262769, '龙域中心一单元-501', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262770, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-502','龙域中心一单元','502','2','0',UTC_TIMESTAMP(), @namespace_id, 550.02);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37066, 1035719, 240111044331050362, 239825274387262770, '龙域中心一单元-502', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262771, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-503','龙域中心一单元','503','2','0',UTC_TIMESTAMP(), @namespace_id, 367.25);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37067, 1035719, 240111044331050362, 239825274387262771, '龙域中心一单元-503', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262772, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-504','龙域中心一单元','504','2','0',UTC_TIMESTAMP(), @namespace_id, 231.13);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37068, 1035719, 240111044331050362, 239825274387262772, '龙域中心一单元-504', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262773, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-505','龙域中心一单元','505','2','0',UTC_TIMESTAMP(), @namespace_id, 334.02);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37069, 1035719, 240111044331050362, 239825274387262773, '龙域中心一单元-505', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262774, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-506','龙域中心一单元','506','2','0',UTC_TIMESTAMP(), @namespace_id, 243.47);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37070, 1035719, 240111044331050362, 239825274387262774, '龙域中心一单元-506', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262775, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-507','龙域中心一单元','507','2','0',UTC_TIMESTAMP(), @namespace_id, 250.69);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37071, 1035719, 240111044331050362, 239825274387262775, '龙域中心一单元-507', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262776, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-508','龙域中心一单元','508','2','0',UTC_TIMESTAMP(), @namespace_id, 250.35);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37072, 1035719, 240111044331050362, 239825274387262776, '龙域中心一单元-508', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262777, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-509','龙域中心一单元','509','2','0',UTC_TIMESTAMP(), @namespace_id, 81.18);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37073, 1035719, 240111044331050362, 239825274387262777, '龙域中心一单元-509', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262778, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-510','龙域中心一单元','510','2','0',UTC_TIMESTAMP(), @namespace_id, 82.87);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37074, 1035719, 240111044331050362, 239825274387262778, '龙域中心一单元-510', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262779, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-511','龙域中心一单元','511','2','0',UTC_TIMESTAMP(), @namespace_id, 82.27);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37075, 1035719, 240111044331050362, 239825274387262779, '龙域中心一单元-511', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262780, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-601','龙域中心一单元','601','2','0',UTC_TIMESTAMP(), @namespace_id, 490.92);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37076, 1035719, 240111044331050362, 239825274387262780, '龙域中心一单元-601', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262781, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-602','龙域中心一单元','602','2','0',UTC_TIMESTAMP(), @namespace_id, 558.34);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37077, 1035719, 240111044331050362, 239825274387262781, '龙域中心一单元-602', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262782, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-603','龙域中心一单元','603','2','0',UTC_TIMESTAMP(), @namespace_id, 364.32);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37078, 1035719, 240111044331050362, 239825274387262782, '龙域中心一单元-603', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262783, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-604','龙域中心一单元','604','2','0',UTC_TIMESTAMP(), @namespace_id, 232.27);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37079, 1035719, 240111044331050362, 239825274387262783, '龙域中心一单元-604', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262784, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-605','龙域中心一单元','605','2','0',UTC_TIMESTAMP(), @namespace_id, 336.7);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37080, 1035719, 240111044331050362, 239825274387262784, '龙域中心一单元-605', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262785, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-606','龙域中心一单元','606','2','0',UTC_TIMESTAMP(), @namespace_id, 416.59);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37081, 1035719, 240111044331050362, 239825274387262785, '龙域中心一单元-606', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262786, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-607','龙域中心一单元','607','2','0',UTC_TIMESTAMP(), @namespace_id, 248.32);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37082, 1035719, 240111044331050362, 239825274387262786, '龙域中心一单元-607', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262787, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-608','龙域中心一单元','608','2','0',UTC_TIMESTAMP(), @namespace_id, 250.63);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37083, 1035719, 240111044331050362, 239825274387262787, '龙域中心一单元-608', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262788, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-609','龙域中心一单元','609','2','0',UTC_TIMESTAMP(), @namespace_id, 247.64);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37084, 1035719, 240111044331050362, 239825274387262788, '龙域中心一单元-609', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262789, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-610','龙域中心一单元','610','2','0',UTC_TIMESTAMP(), @namespace_id, 80.46);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37085, 1035719, 240111044331050362, 239825274387262789, '龙域中心一单元-610', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262790, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-611','龙域中心一单元','611','2','0',UTC_TIMESTAMP(), @namespace_id, 84.22);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37086, 1035719, 240111044331050362, 239825274387262790, '龙域中心一单元-611', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262791, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-612','龙域中心一单元','612','2','0',UTC_TIMESTAMP(), @namespace_id, 82.04);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37087, 1035719, 240111044331050362, 239825274387262791, '龙域中心一单元-612', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262792, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-701','龙域中心一单元','701','2','0',UTC_TIMESTAMP(), @namespace_id, 321.5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37088, 1035719, 240111044331050362, 239825274387262792, '龙域中心一单元-701', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262793, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-702','龙域中心一单元','702','2','0',UTC_TIMESTAMP(), @namespace_id, 341.13);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37089, 1035719, 240111044331050362, 239825274387262793, '龙域中心一单元-702', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262794, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-703','龙域中心一单元','703','2','0',UTC_TIMESTAMP(), @namespace_id, 247.13);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37090, 1035719, 240111044331050362, 239825274387262794, '龙域中心一单元-703', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262795, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-704','龙域中心一单元','704','2','0',UTC_TIMESTAMP(), @namespace_id, 120.75);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37091, 1035719, 240111044331050362, 239825274387262795, '龙域中心一单元-704', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262796, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-705','龙域中心一单元','705','2','0',UTC_TIMESTAMP(), @namespace_id, 328.3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37092, 1035719, 240111044331050362, 239825274387262796, '龙域中心一单元-705', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262797, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-706','龙域中心一单元','706','2','0',UTC_TIMESTAMP(), @namespace_id, 418.09);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37093, 1035719, 240111044331050362, 239825274387262797, '龙域中心一单元-706', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262798, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-707','龙域中心一单元','707','2','0',UTC_TIMESTAMP(), @namespace_id, 46.07);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37094, 1035719, 240111044331050362, 239825274387262798, '龙域中心一单元-707', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262799, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-708','龙域中心一单元','708','2','0',UTC_TIMESTAMP(), @namespace_id, 250.19);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37095, 1035719, 240111044331050362, 239825274387262799, '龙域中心一单元-708', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262800, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-709','龙域中心一单元','709','2','0',UTC_TIMESTAMP(), @namespace_id, 256.54);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37096, 1035719, 240111044331050362, 239825274387262800, '龙域中心一单元-709', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262801, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-710','龙域中心一单元','710','2','0',UTC_TIMESTAMP(), @namespace_id, 187.44);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37097, 1035719, 240111044331050362, 239825274387262801, '龙域中心一单元-710', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262802, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-711','龙域中心一单元','711','2','0',UTC_TIMESTAMP(), @namespace_id, 164.03);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37098, 1035719, 240111044331050362, 239825274387262802, '龙域中心一单元-711', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262803, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-712','龙域中心一单元','712','2','0',UTC_TIMESTAMP(), @namespace_id, 82.04);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37099, 1035719, 240111044331050362, 239825274387262803, '龙域中心一单元-712', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262804, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-801','龙域中心一单元','801','2','0',UTC_TIMESTAMP(), @namespace_id, 321.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37100, 1035719, 240111044331050362, 239825274387262804, '龙域中心一单元-801', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262805, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-802','龙域中心一单元','802','2','0',UTC_TIMESTAMP(), @namespace_id, 341.52);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37101, 1035719, 240111044331050362, 239825274387262805, '龙域中心一单元-802', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262806, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-803','龙域中心一单元','803','2','0',UTC_TIMESTAMP(), @namespace_id, 247.67);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37102, 1035719, 240111044331050362, 239825274387262806, '龙域中心一单元-803', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262807, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-804','龙域中心一单元','804','2','0',UTC_TIMESTAMP(), @namespace_id, 121.06);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37103, 1035719, 240111044331050362, 239825274387262807, '龙域中心一单元-804', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262808, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-805','龙域中心一单元','805','2','0',UTC_TIMESTAMP(), @namespace_id, 328.3);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37104, 1035719, 240111044331050362, 239825274387262808, '龙域中心一单元-805', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262809, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-806','龙域中心一单元','806','2','0',UTC_TIMESTAMP(), @namespace_id, 417.71);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37105, 1035719, 240111044331050362, 239825274387262809, '龙域中心一单元-806', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262810, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-807','龙域中心一单元','807','2','0',UTC_TIMESTAMP(), @namespace_id, 45.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37106, 1035719, 240111044331050362, 239825274387262810, '龙域中心一单元-807', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262811, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-808','龙域中心一单元','808','2','0',UTC_TIMESTAMP(), @namespace_id, 247.68);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37107, 1035719, 240111044331050362, 239825274387262811, '龙域中心一单元-808', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262812, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-809','龙域中心一单元','809','2','0',UTC_TIMESTAMP(), @namespace_id, 256.17);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37108, 1035719, 240111044331050362, 239825274387262812, '龙域中心一单元-809', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262813, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-810','龙域中心一单元','810','2','0',UTC_TIMESTAMP(), @namespace_id, 189.59);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37109, 1035719, 240111044331050362, 239825274387262813, '龙域中心一单元-810', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262814, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-811','龙域中心一单元','811','2','0',UTC_TIMESTAMP(), @namespace_id, 164.03);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37110, 1035719, 240111044331050362, 239825274387262814, '龙域中心一单元-811', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262815, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-812','龙域中心一单元','812','2','0',UTC_TIMESTAMP(), @namespace_id, 81.94);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37111, 1035719, 240111044331050362, 239825274387262815, '龙域中心一单元-812', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262816, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-901','龙域中心一单元','901','2','0',UTC_TIMESTAMP(), @namespace_id, 321.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37112, 1035719, 240111044331050362, 239825274387262816, '龙域中心一单元-901', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262817, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-902','龙域中心一单元','902','2','0',UTC_TIMESTAMP(), @namespace_id, 341.59);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37113, 1035719, 240111044331050362, 239825274387262817, '龙域中心一单元-902', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262818, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-903','龙域中心一单元','903','2','0',UTC_TIMESTAMP(), @namespace_id, 247.67);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37114, 1035719, 240111044331050362, 239825274387262818, '龙域中心一单元-903', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262819, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-904','龙域中心一单元','904','2','0',UTC_TIMESTAMP(), @namespace_id, 708.14);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37115, 1035719, 240111044331050362, 239825274387262819, '龙域中心一单元-904', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262820, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-905','龙域中心一单元','905','2','0',UTC_TIMESTAMP(), @namespace_id, 263.58);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37116, 1035719, 240111044331050362, 239825274387262820, '龙域中心一单元-905', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262821, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-906','龙域中心一单元','906','2','0',UTC_TIMESTAMP(), @namespace_id, 297.87);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37117, 1035719, 240111044331050362, 239825274387262821, '龙域中心一单元-906', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262822, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-907','龙域中心一单元','907','2','0',UTC_TIMESTAMP(), @namespace_id, 45.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37118, 1035719, 240111044331050362, 239825274387262822, '龙域中心一单元-907', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262823, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-908','龙域中心一单元','908','2','0',UTC_TIMESTAMP(), @namespace_id, 299.27);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37119, 1035719, 240111044331050362, 239825274387262823, '龙域中心一单元-908', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262824, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-909','龙域中心一单元','909','2','0',UTC_TIMESTAMP(), @namespace_id, 340.06);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37120, 1035719, 240111044331050362, 239825274387262824, '龙域中心一单元-909', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262825, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-910','龙域中心一单元','910','2','0',UTC_TIMESTAMP(), @namespace_id, 231.39);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37121, 1035719, 240111044331050362, 239825274387262825, '龙域中心一单元-910', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262826, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-911','龙域中心一单元','911','2','0',UTC_TIMESTAMP(), @namespace_id, 162.6);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37122, 1035719, 240111044331050362, 239825274387262826, '龙域中心一单元-911', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262827, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-912','龙域中心一单元','912','2','0',UTC_TIMESTAMP(), @namespace_id, 81.34);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37123, 1035719, 240111044331050362, 239825274387262827, '龙域中心一单元-912', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262828, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1001','龙域中心一单元','1001','2','0',UTC_TIMESTAMP(), @namespace_id, 321.79);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37124, 1035719, 240111044331050362, 239825274387262828, '龙域中心一单元-1001', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262829, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1002','龙域中心一单元','1002','2','0',UTC_TIMESTAMP(), @namespace_id, 339.66);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37125, 1035719, 240111044331050362, 239825274387262829, '龙域中心一单元-1002', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262830, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1003','龙域中心一单元','1003','2','0',UTC_TIMESTAMP(), @namespace_id, 247.5);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37126, 1035719, 240111044331050362, 239825274387262830, '龙域中心一单元-1003', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262831, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1004','龙域中心一单元','1004','2','0',UTC_TIMESTAMP(), @namespace_id, 701.36);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37127, 1035719, 240111044331050362, 239825274387262831, '龙域中心一单元-1004', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262832, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1005','龙域中心一单元','1005','2','0',UTC_TIMESTAMP(), @namespace_id, 269.22);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37128, 1035719, 240111044331050362, 239825274387262832, '龙域中心一单元-1005', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262833, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1006','龙域中心一单元','1006','2','0',UTC_TIMESTAMP(), @namespace_id, 298.58);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37129, 1035719, 240111044331050362, 239825274387262833, '龙域中心一单元-1006', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262834, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1007','龙域中心一单元','1007','2','0',UTC_TIMESTAMP(), @namespace_id, 45.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37130, 1035719, 240111044331050362, 239825274387262834, '龙域中心一单元-1007', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262835, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1008','龙域中心一单元','1008','2','0',UTC_TIMESTAMP(), @namespace_id, 298.76);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37131, 1035719, 240111044331050362, 239825274387262835, '龙域中心一单元-1008', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262836, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1009','龙域中心一单元','1009','2','0',UTC_TIMESTAMP(), @namespace_id, 341.84);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37132, 1035719, 240111044331050362, 239825274387262836, '龙域中心一单元-1009', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262837, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1010','龙域中心一单元','1010','2','0',UTC_TIMESTAMP(), @namespace_id, 227.84);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37133, 1035719, 240111044331050362, 239825274387262837, '龙域中心一单元-1010', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262838, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1011','龙域中心一单元','1011','2','0',UTC_TIMESTAMP(), @namespace_id, 162.6);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37134, 1035719, 240111044331050362, 239825274387262838, '龙域中心一单元-1011', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262839, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1012','龙域中心一单元','1012','2','0',UTC_TIMESTAMP(), @namespace_id, 81.34);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37135, 1035719, 240111044331050362, 239825274387262839, '龙域中心一单元-1012', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262840, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1101','龙域中心一单元','1101','2','0',UTC_TIMESTAMP(), @namespace_id, 321.97);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37136, 1035719, 240111044331050362, 239825274387262840, '龙域中心一单元-1101', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262841, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1102','龙域中心一单元','1102','2','0',UTC_TIMESTAMP(), @namespace_id, 341.52);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37137, 1035719, 240111044331050362, 239825274387262841, '龙域中心一单元-1102', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262842, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1103','龙域中心一单元','1103','2','0',UTC_TIMESTAMP(), @namespace_id, 246.65);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37138, 1035719, 240111044331050362, 239825274387262842, '龙域中心一单元-1103', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262843, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1104','龙域中心一单元','1104','2','0',UTC_TIMESTAMP(), @namespace_id, 705.64);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37139, 1035719, 240111044331050362, 239825274387262843, '龙域中心一单元-1104', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262844, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1105','龙域中心一单元','1105','2','0',UTC_TIMESTAMP(), @namespace_id, 263.14);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37140, 1035719, 240111044331050362, 239825274387262844, '龙域中心一单元-1105', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262845, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1106','龙域中心一单元','1106','2','0',UTC_TIMESTAMP(), @namespace_id, 297.96);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37141, 1035719, 240111044331050362, 239825274387262845, '龙域中心一单元-1106', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262846, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1107','龙域中心一单元','1107','2','0',UTC_TIMESTAMP(), @namespace_id, 46.28);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37142, 1035719, 240111044331050362, 239825274387262846, '龙域中心一单元-1107', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262847, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1108','龙域中心一单元','1108','2','0',UTC_TIMESTAMP(), @namespace_id, 300.17);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37143, 1035719, 240111044331050362, 239825274387262847, '龙域中心一单元-1108', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262848, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1109','龙域中心一单元','1109','2','0',UTC_TIMESTAMP(), @namespace_id, 240.47);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37144, 1035719, 240111044331050362, 239825274387262848, '龙域中心一单元-1109', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262849, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1110','龙域中心一单元','1110','2','0',UTC_TIMESTAMP(), @namespace_id, 230.07);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37145, 1035719, 240111044331050362, 239825274387262849, '龙域中心一单元-1110', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262850, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1111','龙域中心一单元','1111','2','0',UTC_TIMESTAMP(), @namespace_id, 163.83);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37146, 1035719, 240111044331050362, 239825274387262850, '龙域中心一单元-1111', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262851, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心一单元-1112','龙域中心一单元','1112','2','0',UTC_TIMESTAMP(), @namespace_id, 81.94);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37147, 1035719, 240111044331050362, 239825274387262851, '龙域中心一单元-1112', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262852, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-401','龙域中心二单元','401','2','0',UTC_TIMESTAMP(), @namespace_id, 140.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37148, 1035719, 240111044331050362, 239825274387262852, '龙域中心二单元-401', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262853, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-402','龙域中心二单元','402','2','0',UTC_TIMESTAMP(), @namespace_id, 145.17);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37149, 1035719, 240111044331050362, 239825274387262853, '龙域中心二单元-402', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262854, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-403','龙域中心二单元','403','2','0',UTC_TIMESTAMP(), @namespace_id, 168.45);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37150, 1035719, 240111044331050362, 239825274387262854, '龙域中心二单元-403', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262855, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-404','龙域中心二单元','404','2','0',UTC_TIMESTAMP(), @namespace_id, 168.55);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37151, 1035719, 240111044331050362, 239825274387262855, '龙域中心二单元-404', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262856, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-405','龙域中心二单元','405','2','0',UTC_TIMESTAMP(), @namespace_id, 158.94);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37152, 1035719, 240111044331050362, 239825274387262856, '龙域中心二单元-405', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262857, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-406','龙域中心二单元','406','2','0',UTC_TIMESTAMP(), @namespace_id, 140.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37153, 1035719, 240111044331050362, 239825274387262857, '龙域中心二单元-406', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262858, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-501','龙域中心二单元','501','2','0',UTC_TIMESTAMP(), @namespace_id, 244.27);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37154, 1035719, 240111044331050362, 239825274387262858, '龙域中心二单元-501', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262859, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-502','龙域中心二单元','502','2','0',UTC_TIMESTAMP(), @namespace_id, 171.97);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37155, 1035719, 240111044331050362, 239825274387262859, '龙域中心二单元-502', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262860, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-503','龙域中心二单元','503','2','0',UTC_TIMESTAMP(), @namespace_id, 227.44);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37156, 1035719, 240111044331050362, 239825274387262860, '龙域中心二单元-503', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262861, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-504','龙域中心二单元','504','2','0',UTC_TIMESTAMP(), @namespace_id, 252.11);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37157, 1035719, 240111044331050362, 239825274387262861, '龙域中心二单元-504', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262862, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-505','龙域中心二单元','505','2','0',UTC_TIMESTAMP(), @namespace_id, 229.98);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37158, 1035719, 240111044331050362, 239825274387262862, '龙域中心二单元-505', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262863, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-506','龙域中心二单元','506','2','0',UTC_TIMESTAMP(), @namespace_id, 273.89);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37159, 1035719, 240111044331050362, 239825274387262863, '龙域中心二单元-506', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262864, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-601','龙域中心二单元','601','2','0',UTC_TIMESTAMP(), @namespace_id, 247.63);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37160, 1035719, 240111044331050362, 239825274387262864, '龙域中心二单元-601', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262865, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-602','龙域中心二单元','602','2','0',UTC_TIMESTAMP(), @namespace_id, 173.07);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37161, 1035719, 240111044331050362, 239825274387262865, '龙域中心二单元-602', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262866, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-603','龙域中心二单元','603','2','0',UTC_TIMESTAMP(), @namespace_id, 224.98);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37162, 1035719, 240111044331050362, 239825274387262866, '龙域中心二单元-603', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262867, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-604','龙域中心二单元','604','2','0',UTC_TIMESTAMP(), @namespace_id, 250.02);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37163, 1035719, 240111044331050362, 239825274387262867, '龙域中心二单元-604', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262868, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-605','龙域中心二单元','605','2','0',UTC_TIMESTAMP(), @namespace_id, 229.62);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37164, 1035719, 240111044331050362, 239825274387262868, '龙域中心二单元-605', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262869, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-606','龙域中心二单元','606','2','0',UTC_TIMESTAMP(), @namespace_id, 274.42);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37165, 1035719, 240111044331050362, 239825274387262869, '龙域中心二单元-606', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262870, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-701','龙域中心二单元','701','2','0',UTC_TIMESTAMP(), @namespace_id, 139.44);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37166, 1035719, 240111044331050362, 239825274387262870, '龙域中心二单元-701', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262871, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-702','龙域中心二单元','702','2','0',UTC_TIMESTAMP(), @namespace_id, 145.06);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37167, 1035719, 240111044331050362, 239825274387262871, '龙域中心二单元-702', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262872, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-703','龙域中心二单元','703','2','0',UTC_TIMESTAMP(), @namespace_id, 168.46);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37168, 1035719, 240111044331050362, 239825274387262872, '龙域中心二单元-703', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262873, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-704','龙域中心二单元','704','2','0',UTC_TIMESTAMP(), @namespace_id, 168.55);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37169, 1035719, 240111044331050362, 239825274387262873, '龙域中心二单元-704', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262874, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-705','龙域中心二单元','705','2','0',UTC_TIMESTAMP(), @namespace_id, 158.81);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37170, 1035719, 240111044331050362, 239825274387262874, '龙域中心二单元-705', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262875, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-706','龙域中心二单元','706','2','0',UTC_TIMESTAMP(), @namespace_id, 139.44);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37171, 1035719, 240111044331050362, 239825274387262875, '龙域中心二单元-706', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262876, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-801','龙域中心二单元','801','2','0',UTC_TIMESTAMP(), @namespace_id, 140.34);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37172, 1035719, 240111044331050362, 239825274387262876, '龙域中心二单元-801', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262877, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-802','龙域中心二单元','802','2','0',UTC_TIMESTAMP(), @namespace_id, 145.39);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37173, 1035719, 240111044331050362, 239825274387262877, '龙域中心二单元-802', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262878, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-803','龙域中心二单元','803','2','0',UTC_TIMESTAMP(), @namespace_id, 169.47);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37174, 1035719, 240111044331050362, 239825274387262878, '龙域中心二单元-803', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262879, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-804','龙域中心二单元','804','2','0',UTC_TIMESTAMP(), @namespace_id, 169.56);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37175, 1035719, 240111044331050362, 239825274387262879, '龙域中心二单元-804', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262880, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-805','龙域中心二单元','805','2','0',UTC_TIMESTAMP(), @namespace_id, 159.52);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37176, 1035719, 240111044331050362, 239825274387262880, '龙域中心二单元-805', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262881, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-806','龙域中心二单元','806','2','0',UTC_TIMESTAMP(), @namespace_id, 140.34);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37177, 1035719, 240111044331050362, 239825274387262881, '龙域中心二单元-806', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262882, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-901','龙域中心二单元','901','2','0',UTC_TIMESTAMP(), @namespace_id, 212.94);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37178, 1035719, 240111044331050362, 239825274387262882, '龙域中心二单元-901', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262883, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-902','龙域中心二单元','902','2','0',UTC_TIMESTAMP(), @namespace_id, 215.08);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37179, 1035719, 240111044331050362, 239825274387262883, '龙域中心二单元-902', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262884, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-903','龙域中心二单元','903','2','0',UTC_TIMESTAMP(), @namespace_id, 313.38);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37180, 1035719, 240111044331050362, 239825274387262884, '龙域中心二单元-903', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262885, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-904','龙域中心二单元','904','2','0',UTC_TIMESTAMP(), @namespace_id, 275.83);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37181, 1035719, 240111044331050362, 239825274387262885, '龙域中心二单元-904', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262886, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-905','龙域中心二单元','905','2','0',UTC_TIMESTAMP(), @namespace_id, 189.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37182, 1035719, 240111044331050362, 239825274387262886, '龙域中心二单元-905', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262887, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-906','龙域中心二单元','906','2','0',UTC_TIMESTAMP(), @namespace_id, 191.27);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37183, 1035719, 240111044331050362, 239825274387262887, '龙域中心二单元-906', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262888, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1001','龙域中心二单元','1001','2','0',UTC_TIMESTAMP(), @namespace_id, 209);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37184, 1035719, 240111044331050362, 239825274387262888, '龙域中心二单元-1001', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262889, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1002','龙域中心二单元','1002','2','0',UTC_TIMESTAMP(), @namespace_id, 214);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37185, 1035719, 240111044331050362, 239825274387262889, '龙域中心二单元-1002', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262890, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1003','龙域中心二单元','1003','2','0',UTC_TIMESTAMP(), @namespace_id, 311.45);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37186, 1035719, 240111044331050362, 239825274387262890, '龙域中心二单元-1003', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262891, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1004','龙域中心二单元','1004','2','0',UTC_TIMESTAMP(), @namespace_id, 281.31);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37187, 1035719, 240111044331050362, 239825274387262891, '龙域中心二单元-1004', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262892, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1005','龙域中心二单元','1005','2','0',UTC_TIMESTAMP(), @namespace_id, 188.35);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37188, 1035719, 240111044331050362, 239825274387262892, '龙域中心二单元-1005', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262893, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1006','龙域中心二单元','1006','2','0',UTC_TIMESTAMP(), @namespace_id, 192.08);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37189, 1035719, 240111044331050362, 239825274387262893, '龙域中心二单元-1006', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262894, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1101','龙域中心二单元','1101','2','0',UTC_TIMESTAMP(), @namespace_id, 212.94);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37190, 1035719, 240111044331050362, 239825274387262894, '龙域中心二单元-1101', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262895, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1102','龙域中心二单元','1102','2','0',UTC_TIMESTAMP(), @namespace_id, 215.08);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37191, 1035719, 240111044331050362, 239825274387262895, '龙域中心二单元-1102', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262896, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1103','龙域中心二单元','1103','2','0',UTC_TIMESTAMP(), @namespace_id, 313.38);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37192, 1035719, 240111044331050362, 239825274387262896, '龙域中心二单元-1103', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262897, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1104','龙域中心二单元','1104','2','0',UTC_TIMESTAMP(), @namespace_id, 275.83);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37193, 1035719, 240111044331050362, 239825274387262897, '龙域中心二单元-1104', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262898, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1105','龙域中心二单元','1105','2','0',UTC_TIMESTAMP(), @namespace_id, 189.43);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37194, 1035719, 240111044331050362, 239825274387262898, '龙域中心二单元-1105', '0');

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`, `area_size`)
VALUES(239825274387262899, UUID(), 240111044331050362, 14841, '北京市', 14842, '昌平区' ,'龙域中心二单元-1106','龙域中心二单元','1106','2','0',UTC_TIMESTAMP(), @namespace_id, 191.27);
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
VALUES (37195, 1035719, 240111044331050362, 239825274387262899, '龙域中心二单元-1106', '0');


-- OK
SELECT max(id) FROM `eh_web_menu_scopes` INTO @menu_sc_id;
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 10000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 10100, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 10400, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 10600, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 10750, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 10751, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 10752, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 10800, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 11000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 12200, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20100, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20140, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20150, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20155, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20160, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20170, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20180, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20158, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20190, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20191, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 20192, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40100, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40110, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40120, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40130, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40200, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40210, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40220, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40300, '', 'EhNamespaces', @namespace_id, 2);
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
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 40600, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41300, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41310, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41320, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41010, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41020, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41030, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41040, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41050, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41060, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 41100, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 30000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 31000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 32000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 32500, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 33000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 34000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50100, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50110, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50200, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50210, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50220, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50300, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50400, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50500, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50600, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50630, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50631, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50632, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50633, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50640, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50650, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50651, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50652, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50653, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 56161, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50700, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50710, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50720, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50730, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50800, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50810, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50820, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50830, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50840, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50850, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50860, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 50900, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 60000, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 60100, '', 'EhNamespaces', @namespace_id, 2);
INSERT INTO `eh_web_menu_scopes` (id, menu_id, menu_name, owner_type, owner_id, apply_policy) VALUES ((@menu_sc_id := @menu_sc_id + 1), 60200, '', 'EhNamespaces', @namespace_id, 2);


-- OK
SET @launch_pad_layout_id = (SELECT MAX(id) FROM `eh_launch_pad_layouts`);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`)
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"versionCode": "2017080101","versionName": "1.0.0","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 10,"separatorFlag": 0,"separatorHeight": 0}, {"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "GovAgencies"},"style": "Default","defaultOrder": 20,"separatorFlag": 1,"separatorHeight": 16,"columnCount": 4}, {"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default","rowCount": 2},"style": "Default","defaultOrder": 30,"separatorFlag": 1,"separatorHeight": 16}, {"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Default","defaultOrder": 40,"separatorFlag": 0,"separatorHeight": 0}, {"groupName": "","widget": "NewsFlash","instanceConfig": {"timeWidgetStyle": "datetime","categoryId": 0,"itemGroup": "Default","newsSize": 2},"style": "Default","defaultOrder": 50,"separatorFlag": 0,"separatorHeight": 0}]}', 2017080101, 0, 2, NOW(), 'pm_admin', 0, 0, 0);
INSERT INTO `eh_launch_pad_layouts` (`id`, `namespace_id`, `name`, `layout_json`, `version_code`, `min_version_code`, `status`, `create_time`, `scene_type`, `scope_code`, `scope_id`, `apply_policy`)
VALUES ((@launch_pad_layout_id := @launch_pad_layout_id + 1), @namespace_id, 'ServiceMarketLayout', '{"versionCode": "2017080101","versionName": "1.0.0","layoutName": "ServiceMarketLayout","displayName": "服务市场","groups": [{"groupName": "","widget": "Banners","instanceConfig": {"itemGroup": "Default"},"style": "Default","defaultOrder": 10,"separatorFlag": 0,"separatorHeight": 0}, {"groupName": "","widget": "Navigator","instanceConfig": {"itemGroup": "GovAgencies"},"style": "Default","defaultOrder": 20,"separatorFlag": 1,"separatorHeight": 16,"columnCount": 4}, {"groupName": "","widget": "Bulletins","instanceConfig": {"itemGroup": "Default","rowCount": 2},"style": "Default","defaultOrder": 30,"separatorFlag": 1,"separatorHeight": 16}, {"groupName": "商家服务","widget": "Navigator","instanceConfig": {"itemGroup": "Bizs"},"style": "Default","defaultOrder": 40,"separatorFlag": 0,"separatorHeight": 0}, {"groupName": "","widget": "NewsFlash","instanceConfig": {"timeWidgetStyle": "datetime","categoryId": 0,"itemGroup": "Default","newsSize": 2},"style": "Default","defaultOrder": 50,"separatorFlag": 0,"separatorHeight": 0}]}', 2017080101, 0, 2, NOW(), 'park_tourist', 0, 0, 0);

SELECT max(id) FROM `eh_yellow_pages` INTO @yellow_page_id;
INSERT INTO `eh_yellow_pages` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `nick_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
VALUES((@yellow_page_id := @yellow_page_id + 1), 0,'community','240111044331050362','创客空间','创客空间','1','','','','cs://1/image/aW1hZ2UvTVRvek1qQXpNbVZpTmpVMU5tSXhNekZqTWpOaE5USmpNVFprTXpWaFlqazFNQQ','2',NULL, 116.323584, 40.07254,'wx4eznbpryyu',NULL,NULL,NULL,NULL,NULL,'','',NULL,NULL,NULL,NULL,NULL);

-- SELECT max(id) FROM `eh_yellow_pages` INTO @yellow_page_at_id;
-- INSERT INTO `eh_yellow_page_attachments` (`id`, `owner_id`, `content_type`, `content_uri`, `creator_uid`, `create_time`)
--  VALUES((@yellow_page_at_id := @yellow_page_at_id + 1), @yellow_page_id,'image','cs://1/image/aW1hZ2UvTVRvek1qQXpNbVZpTmpVMU5tSXhNekZqTWpOaE5USmpNVFprTXpWaFlqazFNQQ','0',UTC_TIMESTAMP());

INSERT INTO `eh_forums` (`id`, `uuid`, `namespace_id`, `app_id`, `owner_type`, `owner_id`, `name`, `description`, `post_count`, `modify_seq`, `update_time`, `create_time`)
VALUES(190698 , UUID(), @namespace_id, 2, 'EhGroups', 0,'创客空间','','0','0', UTC_TIMESTAMP(), UTC_TIMESTAMP());

SELECT max(id) FROM `eh_service_alliance_categories` INTO @ser_all_cate_id;
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
VALUES ((@ser_all_cate_id := @ser_all_cate_id + 1), 'community', '240111044331050362', '0', '服务联盟', '服务联盟', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, @namespace_id, '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`)
VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331050362', '服务联盟', '服务联盟', @ser_all_cate_id, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

# GovAgencies iten_gorup
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'EXCHANGE_HALL', '交流大厅', 'cs://1/image/aW1hZ2UvTVRvMk9EWmtOemMxTnpRd1pUYzBOamN5WXpJek5ETXlOemt3WkRWa00yTTRaQQ', 1, 1, 50, '', 100, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'EXCHANGE_HALL', '交流大厅', 'cs://1/image/aW1hZ2UvTVRvMk9EWmtOemMxTnpRd1pUYzBOamN5WXpJek5ETXlOemt3WkRWa00yTTRaQQ', 1, 1, 50, '', 100, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'MAKER_SPACE', '创客空间', 'cs://1/image/aW1hZ2UvTVRvM05UQTVZVEkyWmpjMk9XRm1NekE1TnpSbE5qVTBNV1l6WmpFME1XTXpaQQ', 1, 1, 32, '{"type":1,"forumId":190698,"categoryId":1010,"parentId":110001,"tag":"创客"}', 200, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'MAKER_SPACE', '创客空间', 'cs://1/image/aW1hZ2UvTVRvM05UQTVZVEkyWmpjMk9XRm1NekE1TnpSbE5qVTBNV1l6WmpFME1XTXpaQQ', 1, 1, 32, '{"type":1,"forumId":190698,"categoryId":1010,"parentId":110001,"tag":"创客"}', 200, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'APPLY_ENTER', '园区招商', 'cs://1/image/aW1hZ2UvTVRvMk9EWmtOemMxTnpRd1pUYzBOamN5WXpJek5ETXlOemt3WkRWa00yTTRaQQ', 1, 1, 28, '', 300, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'APPLY_ENTER', '园区招商', 'cs://1/image/aW1hZ2UvTVRvMk9EWmtOemMxTnpRd1pUYzBOamN5WXpJek5ETXlOemt3WkRWa00yTTRaQQ', 1, 1, 28, '', 300, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'SERVICE_ALLIANCE', '服务联盟', 'cs://1/image/aW1hZ2UvTVRwbFpqUmxObVUwWlRObFltUTJZVFE1WkRJd01XWTJZemRoWW1ObE5qSTJZZw', 1, 1, 33, CONCAT('{"type":', @ser_all_cate_id, ',"parentId":',@ser_all_cate_id,',"displayType": "grid"}'), 400, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'SERVICE_ALLIANCE', '服务联盟', 'cs://1/image/aW1hZ2UvTVRwbFpqUmxObVUwWlRObFltUTJZVFE1WkRJd01XWTJZemRoWW1ObE5qSTJZZw', 1, 1, 33, CONCAT('{"type":', @ser_all_cate_id, ',"parentId":',@ser_all_cate_id,',"displayType": "grid"}'), 400, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

# Bizs iten_gorup
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'WIFI', '一键上网', 'cs://1/image/aW1hZ2UvTVRvd09EaGhObVV3TURZek5EazJZbUkxTTJZek5ERmhNelJrWW1VeU16UTBOZw', 1, 1, 47, '', 100, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'WIFI', '一键上网', 'cs://1/image/aW1hZ2UvTVRvd09EaGhObVV3TURZek5EazJZbUkxTTJZek5ERmhNelJrWW1VeU16UTBOZw', 1, 1, 47, '', 100, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'DOOR_AUTH', '门禁', 'cs://1/image/aW1hZ2UvTVRveU1EZGpZekkwWkRNeE5qRXpORFJrTW1NME9UWTFNbVE0WlRFMlpXVmpNdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 200, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'park_tourist', 1, NULL, NULL, 0, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), @namespace_id, 0, 0, 0, '/home', 'GovAgencies', 'DOOR_AUTH', '门禁', 'cs://1/image/aW1hZ2UvTVRveU1EZGpZekkwWkRNeE5qRXpORFJrTW1NME9UWTFNbVE0WlRFMlpXVmpNdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 200, 0, 1, 1, '', 0, NULL, NULL, NULL, 0, 'pm_admin', 1, NULL, NULL, 0, NULL);

SELECT max(id) FROM `eh_rentalv2_resource_types` INTO @max_ren_t_id;
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES ((@max_ren_t_id := @max_ren_t_id + 1), '会议室预订', 0, NULL, 2, @namespace_id);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'METTING_ROOM', '会议室预订', 'cs://1/image/aW1hZ2UvTVRvM1kyTTRZemhpWWpka01qY3dOamRqTVdKa1pEZGxPRGsxTkdVNVlqUmtOZw', 1, 1, 49, CONCAT('{"resourceTypeId":', @max_ren_t_id, ',"pageType":0}'), 300, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'METTING_ROOM', '会议室预订', 'cs://1/image/aW1hZ2UvTVRvM1kyTTRZemhpWWpka01qY3dOamRqTVdKa1pEZGxPRGsxTkdVNVlqUmtOZw', 1, 1, 49, CONCAT('{"resourceTypeId":', @max_ren_t_id, ',"pageType":0}'), 300, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'VIDEO_METTING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwbVpqUXpaVGRpWTJZNFl6SmpaR0U0TkRSaE1HSmpORFUxT0Rjd1lUUTNaZw', 1, 1, 27, '', 400, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'VIDEO_METTING', '视频会议', 'cs://1/image/aW1hZ2UvTVRwbVpqUXpaVGRpWTJZNFl6SmpaR0U0TkRSaE1HSmpORFUxT0Rjd1lUUTNaZw', 1, 1, 27, '', 400, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ENTERPRISE', '园区企业', 'cs://1/image/aW1hZ2UvTVRvek1EbGlNVGN6T1Raa1lqZzBZamcwTlRJMU56a3lZV0UyTVRGaFpHRm1NUQ', 1, 1, 34, '{"type":3}', 500, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'ENTERPRISE', '园区企业', 'cs://1/image/aW1hZ2UvTVRvek1EbGlNVGN6T1Raa1lqZzBZamcwTlRJMU56a3lZV0UyTVRGaFpHRm1NUQ', 1, 1, 34, '{"type":3}', 500, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'SERVICE_HOT_LINE', '园区热线', 'cs://1/image/aW1hZ2UvTVRveU9EazRNelUzWkdWbFltTmtPRFV4T1RCak1tWTVNV1EzTmpjMk5tWTRZdw', 1, 1, 45, '', 600, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'SERVICE_HOT_LINE', '园区热线', 'cs://1/image/aW1hZ2UvTVRveU9EazRNelUzWkdWbFltTmtPRFV4T1RCak1tWTVNV1EzTmpjMk5tWTRZdw', 1, 1, 45, '', 600, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRwbU9USXdOMk01WTJRd1pqQXlNRGN5WVdSbU1tRXlNMlV4WkRoak5EZzNNdw', 1, 1, 36, '{"privateFlag": 0}', 700, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CLUB', '俱乐部', 'cs://1/image/aW1hZ2UvTVRwbU9USXdOMk01WTJRd1pqQXlNRGN5WVdSbU1tRXlNMlV4WkRoak5EZzNNdw', 1, 1, 36, '{"privateFlag": 0}', 700, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'MORE', '更多', 'cs://1/image/aW1hZ2UvTVRwaU1UQm1aR1k1TmpRMk9EZGtORFptWVdJeVpEVmlaakl3WkdObFlXVmpNdw', 1, 1, 1, '{"itemLocation":"/home","itemGroup":"Bizs"}', 1000, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'MORE', '更多', 'cs://1/image/aW1hZ2UvTVRwaU1UQm1aR1k1TmpRMk9EZGtORFptWVdJeVpEVmlaakl3WkdObFlXVmpNdw', 1, 1, 1, '{"itemLocation":"/home","itemGroup":"Bizs"}', 1000, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

# MORE
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'PM_TASK', '物业报修', 'cs://1/image/aW1hZ2UvTVRwbE1qSmpORGhoWVRObE4yTTJNVGRtWVRJNVpHVTNPV1V3TkdJelkyWmpOQQ', 1, 1, 60, '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}', 750, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'PM_TASK', '物业报修', 'cs://1/image/aW1hZ2UvTVRwbE1qSmpORGhoWVRObE4yTTJNVGRtWVRJNVpHVTNPV1V3TkdJelkyWmpOQQ', 1, 1, 60, '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业报修"}', 750, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES ((@max_ren_t_id := @max_ren_t_id + 1), 'VIP车位预订', 0, NULL, 2, @namespace_id);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'VIP_PARK', 'VIP车位预订', 'cs://1/image/aW1hZ2UvTVRwaE5URmhNelZpWlRsaE5HUmhZalEzT0RaaU5HWmlaVGM0TUdRNE1UZG1Ndw', 1, 1, 49, CONCAT('{"resourceTypeId":', @max_ren_t_id, ',"pageType":0}'), 760, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'VIP_PARK', 'VIP车位预订', 'cs://1/image/aW1hZ2UvTVRwaE5URmhNelZpWlRsaE5HUmhZalEzT0RaaU5HWmlaVGM0TUdRNE1UZG1Ndw', 1, 1, 49, CONCAT('{"resourceTypeId":', @max_ren_t_id, ',"pageType":0}'), 760, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CLOUD_PRINT', '云打印', 'cs://1/image/aW1hZ2UvTVRvM1lUTTBNelE1TW1ZNFpUTmhPRFk0TTJZeU5XRTJPR0U1TmpReE5HSXlOUQ', 1, 1, 13, CONCAT('{"url": "https://', @core_url,'/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}'), 770, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'CLOUD_PRINT', '云打印', 'cs://1/image/aW1hZ2UvTVRvM1lUTTBNelE1TW1ZNFpUTmhPRFk0TTJZeU5XRTJPR0U1TmpReE5HSXlOUQ', 1, 1, 13, CONCAT('{"url": "https://', @core_url,'/cloud-print/build/index.html?hideNavigationBar=1#/home#sign_suffix"}'), 770, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'RR_LAB', '人人实验', 'cs://1/image/aW1hZ2UvTVRvNU5ERmlOV1poTnpSaVlXUTVZelUzT1RJMk5ESTJaak14WldNeE16QmhNdw', 1, 1, 13, '{"url": "http://zuolin.com/mobile/static/coming_soon/index.html"}', 780, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`)
VALUES ((@launch_pad_item_id := @launch_pad_item_id +1), @namespace_id, 0, 0, 0, '/home', 'Bizs', 'RR_LAB', '人人实验', 'cs://1/image/aW1hZ2UvTVRvNU5ERmlOV1poTnpSaVlXUTVZelUzT1RJMk5ESTJaak14WldNeE16QmhNdw', 1, 1, 13, '{"url": "http://zuolin.com/mobile/static/coming_soon/index.html"}', 780, 0, 1, 0, NULL, 0, NULL, NULL, NULL, 0, 'pm_admin', 0, NULL, NULL, 0);


INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
VALUES(@namespace_id, 'sms.default.yzx', 1, 'zh_CN', '验证-昌发展', '111153');

SET @lease_config_id = (SELECT MAX(id) FROM `eh_lease_configs`);
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`)
VALUES ((@lease_config_id := @lease_config_id + 1), @namespace_id, 1, 1, 1, 1, 1);

-- NEW --- 08/04 17:59
SELECT MAX(id) FROM `eh_organization_details` INTO @organization_details_id;
INSERT INTO `eh_organization_details` (`id`, `organization_id`, `description`, `contact`, `address`, `create_time`, `longitude`, `latitude`, `geohash`, `display_name`)
  VALUES ((@organization_details_id := @organization_details_id + 1), 1035719, NULL, '13611204200', '北京市昌平区回龙观龙域中街一号院', NOW(), NULL, NULL, NULL, '均豪物业');

SELECT MAX(id) FROM `eh_enterprise_community_map` INTO @enterprise_community_map_id;
INSERT INTO `eh_enterprise_community_map` (`id`, `community_id`, `member_type`, `member_id`, `member_status`, `creator_uid`, `create_time`, `update_time`)
VALUES ((@enterprise_community_map_id := @enterprise_community_map_id + 1), 240111044331050362, 'enterprise', 1035719, 3, NULL, NOW(), NOW());
-- NEW END--- 08/04 17:59

SET FOREIGN_KEY_CHECKS = 1;