-- 接口配置
SET @configuration_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO eh_configurations(`id`,`name`,`value`,`description`,`namespace_id`)VALUES((@configuration_id := @configuration_id + 1), 'get.ware.info.api','zl-ec/rest/openapi/model/listByCondition','获取商品信息',0);

-- 物业报修 模版

DELETE FROM eh_locale_templates where id in (180,181,182,183);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('180', 'pmtask.notification', '1', 'zh_CN', '任务操作模版', '任务已生成，${operatorName} ${operatorPhone}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('181', 'pmtask.notification', '2', 'zh_CN', '任务操作模版', '已派单，${operatorName} ${operatorPhone} 已将任务分配给了 ${targetName} ${targetPhone}，将会很快联系您。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('182', 'pmtask.notification', '3', 'zh_CN', '任务操作模版', '已完成，${operatorName} ${operatorPhone} 已完成该单，稍后我们将进行回访。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('183', 'pmtask.notification', '4', 'zh_CN', '任务操作模版', '您的任务已被 ${operatorName} ${operatorPhone} 关闭', '0');

-- 物业报修2.0
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);


INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://beta.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'default');   


INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (280, 'pmtask', '10005', 'zh_CN', '服务类型已存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
  VALUES (281, 'pmtask', '10006', 'zh_CN', '服务类型不存在');
  
  
-- 增加 门禁日志 和 官网会议菜单 by sfyan 20160912
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (59400,'门禁日志',59000,null,'access_log_inside',0,2,'/50000/59000/59400','park',711);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56230,'会议官网',56200,null,'zlMeeting',0,2,'/50000/56000/56200/56230','park',570);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (730,0,'内部 门禁日志','内部 门禁日志 全部功能',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (560,0,'会议官网','会议官网',null);

set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),730,59400,'门禁日志',1,1,'门禁日志',801);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),560,56230,'会议官网',1,1,'查看会议官网',641);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),59400,`menu_name`,`owner_type`,`owner_id`,`apply_policy`  FROM `eh_web_menu_scopes` WHERE `menu_id` = 59000;
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),56230,`menu_name`,`owner_type`,`owner_id`,`apply_policy`  FROM `eh_web_menu_scopes` WHERE `menu_id` = 56200;

set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1,730, 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1,730, 1002,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1,730, 1005,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1,730, 1006,0,1,now());

INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1,560, 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES((@acl_id := @acl_id + 1), 'EhOrganizations', 1,560, 1002,0,1,now());

-- 结算服务配置 by sfyan 20160913
TRUNCATE TABLE `eh_stat_service`;
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (1, 1000000, 'EhOrganizations', 1000001, 'parking_recharge','停车充值', 1, now());


-- 深业

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES(12,'服务预约','0',NULL,'0', 999992);

delete from eh_launch_pad_items where id = 10026;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10026', '999992', '0', '0', '0', '/home', 'Bizs', '服务预约', '服务预约', 'cs://1/image/aW1hZ2UvTVRvek5USmpZV1JtTm1FMll6UXdZVFpoWlRNeFlqVXdObU0xTXpRME16WmlPUQ', '1', '1', '49', '{\"resourceTypeId\":12,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default', '1');
UPDATE eh_launch_pad_items SET action_data = '{\"resourceTypeId\":12,\"pageType\":0}',action_type = '49' where id = 10123;


INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES( 240111044331051304, UUID(), 14956, '深圳市',  14958, '罗湖区', '深业中心大厦', '深业中心大厦', '深圳市罗湖区深南东路5045号', NULL, '深业中心大厦地处深南大道的金融中心区，是由深业中心发展（深圳）有限公司投资开发的5A智能化高级写字楼。大厦总建筑面积73542M2，总高33层 155米，深圳证券交易所入驻其中，决定其具有极其特殊的地位和影响。大厦建立了准军事化的保安队伍，人防、技防相结合的防范网络，以及与派出所和其它小区保安队形成的快速反应体系。根据《全国城市物业管理大厦考核标准》，结合金融机构的运作特点和星级酒店服务要求，形成了独具特色的金融商厦管理模式。1998年被建设部授予"全国物业管理示范大厦"称号，1999年"全国物业管理工作会议"指定为高层写字楼参观点，建设部部长俞振声高度评价为"点睛之作"。', NULL, NULL, NULL, NULL, NULL, NULL,NULL, 682, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'0', 179900, 179901, UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES(240111044331047904, 240111044331051304, '', 114.11492, 22.54703, 'ws10k8xcyr58');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(1000750, 240111044331051304);
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(177109, 240111044331051304, '深业中心大厦', '深业中心大厦', 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999992);

INSERT INTO `eh_namespace_resources`(`id`, `namespace_id`, `resource_type`, `resource_id`, `create_time`) 
	VALUES(1256, 999992, 'COMMUNITY', 240111044331051304, UTC_TIMESTAMP());	

INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101751,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0101','深业中心大厦','0101','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101752,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0102','深业中心大厦','0102','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101753,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0103','深业中心大厦','0103','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101754,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0104','深业中心大厦','0104','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101755,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0105','深业中心大厦','0105','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101756,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0106','深业中心大厦','0106','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101757,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0107','深业中心大厦','0107','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101758,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0108','深业中心大厦','0108','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101759,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0109','深业中心大厦','0109','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101760,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0110','深业中心大厦','0110','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101761,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0111','深业中心大厦','0111','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101762,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0112','深业中心大厦','0112','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101763,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0113','深业中心大厦','0113','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101764,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0201-1901','深业中心大厦','0201-1901','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101765,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-20F','深业中心大厦','20F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101766,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2201','深业中心大厦','2201','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101767,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2202','深业中心大厦','2202','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101768,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2203','深业中心大厦','2203','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101769,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2204','深业中心大厦','2204','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101770,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2205','深业中心大厦','2205','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101771,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2206','深业中心大厦','2206','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101772,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2207','深业中心大厦','2207','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101773,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2208-2211','深业中心大厦','2208-2211','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101774,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2212','深业中心大厦','2212','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101775,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2213','深业中心大厦','2213','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101776,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2215','深业中心大厦','2215','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101777,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2216','深业中心大厦','2216','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101778,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2218','深业中心大厦','2218','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101779,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2301','深业中心大厦','2301','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101780,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2314','深业中心大厦','2314','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101781,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2315','深业中心大厦','2315','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101782,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2316','深业中心大厦','2316','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101783,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2302-2313','深业中心大厦','2302-2313','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101784,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2404-2411','深业中心大厦','2404-2411','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101785,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2401-03','深业中心大厦','2401-03','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101786,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-12-16','深业中心大厦','12-16','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101787,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2501','深业中心大厦','2501','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101788,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2516','深业中心大厦','2516','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101789,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2502-2503','深业中心大厦','2502-2503','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101790,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2505-2511','深业中心大厦','2505-2511','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101791,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2512-2513','深业中心大厦','2512-2513','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101792,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2514-2515','深业中心大厦','2514-2515','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101793,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-26F','深业中心大厦','26F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101794,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-27F','深业中心大厦','27F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101795,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2901A-2901B','深业中心大厦','2901A-2901B','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101796,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2902-2903','深业中心大厦','2902-2903','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101797,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2904','深业中心大厦','2904','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101798,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2906','深业中心大厦','2906','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101799,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3002','深业中心大厦','3002','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101800,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3003','深业中心大厦','3003','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101801,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3005','深业中心大厦','3005','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101802,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3006','深业中心大厦','3006','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101803,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3007-3008','深业中心大厦','3007-3008','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101804,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3101','深业中心大厦','3101','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101805,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3105A','深业中心大厦','3105A','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101806,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3106','深业中心大厦','3106','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101807,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3107','深业中心大厦','3107','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387101808,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-32F','深业中心大厦','32F','2','0',UTC_TIMESTAMP(), 999992);


INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20483, 1000750, 240111044331051304, 239825274387101751, '深业中心大厦-0101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20484, 1000750, 240111044331051304, 239825274387101752, '深业中心大厦-0102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20485, 1000750, 240111044331051304, 239825274387101753, '深业中心大厦-0103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20486, 1000750, 240111044331051304, 239825274387101754, '深业中心大厦-0104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20487, 1000750, 240111044331051304, 239825274387101755, '深业中心大厦-0105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20488, 1000750, 240111044331051304, 239825274387101756, '深业中心大厦-0106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20489, 1000750, 240111044331051304, 239825274387101757, '深业中心大厦-0107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20490, 1000750, 240111044331051304, 239825274387101758, '深业中心大厦-0108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20491, 1000750, 240111044331051304, 239825274387101759, '深业中心大厦-0109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20492, 1000750, 240111044331051304, 239825274387101760, '深业中心大厦-0110', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20493, 1000750, 240111044331051304, 239825274387101761, '深业中心大厦-0111', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20494, 1000750, 240111044331051304, 239825274387101762, '深业中心大厦-0112', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20495, 1000750, 240111044331051304, 239825274387101763, '深业中心大厦-0113', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20496, 1000750, 240111044331051304, 239825274387101764, '深业中心大厦-0201-1901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20497, 1000750, 240111044331051304, 239825274387101765, '深业中心大厦-20F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20498, 1000750, 240111044331051304, 239825274387101766, '深业中心大厦-2201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20499, 1000750, 240111044331051304, 239825274387101767, '深业中心大厦-2202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20500, 1000750, 240111044331051304, 239825274387101768, '深业中心大厦-2203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20501, 1000750, 240111044331051304, 239825274387101769, '深业中心大厦-2204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20502, 1000750, 240111044331051304, 239825274387101770, '深业中心大厦-2205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20503, 1000750, 240111044331051304, 239825274387101771, '深业中心大厦-2206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20504, 1000750, 240111044331051304, 239825274387101772, '深业中心大厦-2207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20505, 1000750, 240111044331051304, 239825274387101773, '深业中心大厦-2208-2211', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20506, 1000750, 240111044331051304, 239825274387101774, '深业中心大厦-2212', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20507, 1000750, 240111044331051304, 239825274387101775, '深业中心大厦-2213', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20508, 1000750, 240111044331051304, 239825274387101776, '深业中心大厦-2215', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20509, 1000750, 240111044331051304, 239825274387101777, '深业中心大厦-2216', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20510, 1000750, 240111044331051304, 239825274387101778, '深业中心大厦-2218', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20511, 1000750, 240111044331051304, 239825274387101779, '深业中心大厦-2301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20512, 1000750, 240111044331051304, 239825274387101780, '深业中心大厦-2314', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20513, 1000750, 240111044331051304, 239825274387101781, '深业中心大厦-2315', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20514, 1000750, 240111044331051304, 239825274387101782, '深业中心大厦-2316', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20515, 1000750, 240111044331051304, 239825274387101783, '深业中心大厦-2302-2313', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20516, 1000750, 240111044331051304, 239825274387101784, '深业中心大厦-2404-2411', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20517, 1000750, 240111044331051304, 239825274387101785, '深业中心大厦-2401-03', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20518, 1000750, 240111044331051304, 239825274387101786, '深业中心大厦-12-16', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20519, 1000750, 240111044331051304, 239825274387101787, '深业中心大厦-2501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20520, 1000750, 240111044331051304, 239825274387101788, '深业中心大厦-2516', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20521, 1000750, 240111044331051304, 239825274387101789, '深业中心大厦-2502-2503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20522, 1000750, 240111044331051304, 239825274387101790, '深业中心大厦-2505-2511', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20523, 1000750, 240111044331051304, 239825274387101791, '深业中心大厦-2512-2513', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20524, 1000750, 240111044331051304, 239825274387101792, '深业中心大厦-2514-2515', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20525, 1000750, 240111044331051304, 239825274387101793, '深业中心大厦-26F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20526, 1000750, 240111044331051304, 239825274387101794, '深业中心大厦-27F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20527, 1000750, 240111044331051304, 239825274387101795, '深业中心大厦-2901A-2901B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20528, 1000750, 240111044331051304, 239825274387101796, '深业中心大厦-2902-2903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20529, 1000750, 240111044331051304, 239825274387101797, '深业中心大厦-2904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20530, 1000750, 240111044331051304, 239825274387101798, '深业中心大厦-2906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20531, 1000750, 240111044331051304, 239825274387101799, '深业中心大厦-3002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20532, 1000750, 240111044331051304, 239825274387101800, '深业中心大厦-3003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20533, 1000750, 240111044331051304, 239825274387101801, '深业中心大厦-3005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20534, 1000750, 240111044331051304, 239825274387101802, '深业中心大厦-3006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20535, 1000750, 240111044331051304, 239825274387101803, '深业中心大厦-3007-3008', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20536, 1000750, 240111044331051304, 239825274387101804, '深业中心大厦-3101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20537, 1000750, 240111044331051304, 239825274387101805, '深业中心大厦-3105A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20538, 1000750, 240111044331051304, 239825274387101806, '深业中心大厦-3106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20539, 1000750, 240111044331051304, 239825274387101807, '深业中心大厦-3107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20540, 1000750, 240111044331051304, 239825274387101808, '深业中心大厦-32F', '0');

INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (2, 999992, 'EhOrganizations', 1000750, 'parking_recharge','停车充值', 1, now());
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (3, 999992, 'EhOrganizations', 1000750, 'pmsy','物业缴费', 1, now());

INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (4, 999990, 'EhOrganizations', 1001080, 'payment_card','一卡通', 1, now());

INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (5, 999993, 'EhOrganizations', 1000631, 'parking_recharge','停车充值', 1, now());
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (6, 999993, 'EhOrganizations', 1000631, 'pmsy','物业缴费', 1, now());


-- 任务设备和二维码不对应错误提示 add by xiongying20160913
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10019', 'zh_CN', '二维码和任务设备不对应');