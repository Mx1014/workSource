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

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('195', 'pmtask.notification', '5', 'zh_CN', '任务操作模版', '您于${day}日${hour}时发起的服务已由 ${operatorName} ${operatorPhone} 完成，快去评价打分吧~', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('196', 'pmtask.notification', '6', 'zh_CN', '任务操作模版', '业主 ${creatorName} ${creatorPhone} 发起的服务单已由 ${operatorName} ${operatorPhone} 完成', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('205', 'pmtask.notification', '7', 'zh_CN', '任务操作模版', '${creatorName} ${creatorPhone}已发起一个任务，请尽快处理', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('197', 'sms.default.yzx', '11', 'zh_CN', '任务操作模版', '29479', '999992');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('198', 'sms.default.yzx', '10', 'zh_CN', '任务操作模版', '29478', '999992');


-- 物业报修2.0
-- 删除1.0版本 items
delete from eh_launch_pad_items where id in (10053, 10020);
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修', '物业报修', 'cs://1/image/aW1hZ2UvTVRwaVpqazBOVEE1T1dRNE5XSTRNekF6WW1Fek5qZ3lPREExT1dWak1qWmtPUQ', 1, 1, 14, '{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
 
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', '物业报修', '物业报修', 'cs://1/image/aW1hZ2UvTVRwaVpqazBOVEE1T1dRNE5XSTRNekF6WW1Fek5qZ3lPREExT1dWak1qWmtPUQ', 1, 1, 14, '{"url":"http://core.zuolin.com/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'default');   


 INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
   VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999992, 0, 0, 0, '/home', 'Bizs', 'PM_TASK', '任务', 'cs://1/image/aW1hZ2UvTVRvME9UWTJNemszT0RRd1l6WmtNell6TXprMVpEVTNPV1UzWkdObE1UbG1OUQ', 1, 1, 51, '', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');  



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
	VALUES(239825274387111751,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0101','深业中心大厦','0101','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111752,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0102','深业中心大厦','0102','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111753,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0103','深业中心大厦','0103','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111754,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0104','深业中心大厦','0104','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111755,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0105','深业中心大厦','0105','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111756,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0106','深业中心大厦','0106','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111757,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0107','深业中心大厦','0107','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111758,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0108','深业中心大厦','0108','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111759,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0109','深业中心大厦','0109','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111760,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0110','深业中心大厦','0110','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111761,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0111','深业中心大厦','0111','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111762,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0112','深业中心大厦','0112','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111763,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0113','深业中心大厦','0113','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111764,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-0201-1901','深业中心大厦','0201-1901','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111765,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-20F','深业中心大厦','20F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111766,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2201','深业中心大厦','2201','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111767,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2202','深业中心大厦','2202','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111768,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2203','深业中心大厦','2203','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111769,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2204','深业中心大厦','2204','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111770,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2205','深业中心大厦','2205','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111771,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2206','深业中心大厦','2206','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111772,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2207','深业中心大厦','2207','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111773,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2208-2211','深业中心大厦','2208-2211','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111774,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2212','深业中心大厦','2212','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111775,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2213','深业中心大厦','2213','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111776,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2215','深业中心大厦','2215','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111777,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2216','深业中心大厦','2216','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111778,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2218','深业中心大厦','2218','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111779,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2301','深业中心大厦','2301','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111780,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2314','深业中心大厦','2314','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111781,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2315','深业中心大厦','2315','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111782,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2316','深业中心大厦','2316','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111783,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2302-2313','深业中心大厦','2302-2313','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111784,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2404-2411','深业中心大厦','2404-2411','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111785,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2401-03','深业中心大厦','2401-03','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111786,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-12-16','深业中心大厦','12-16','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111787,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2501','深业中心大厦','2501','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111788,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2516','深业中心大厦','2516','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111789,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2502-2503','深业中心大厦','2502-2503','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111790,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2505-2511','深业中心大厦','2505-2511','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111791,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2512-2513','深业中心大厦','2512-2513','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111792,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2514-2515','深业中心大厦','2514-2515','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111793,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-26F','深业中心大厦','26F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111794,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-27F','深业中心大厦','27F','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111795,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2901A-2901B','深业中心大厦','2901A-2901B','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111796,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2902-2903','深业中心大厦','2902-2903','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111797,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2904','深业中心大厦','2904','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111798,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-2906','深业中心大厦','2906','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111799,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3002','深业中心大厦','3002','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111800,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3003','深业中心大厦','3003','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111801,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3005','深业中心大厦','3005','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111802,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3006','深业中心大厦','3006','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111803,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3007-3008','深业中心大厦','3007-3008','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111804,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3101','深业中心大厦','3101','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111805,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3105A','深业中心大厦','3105A','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111806,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3106','深业中心大厦','3106','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111807,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-3107','深业中心大厦','3107','2','0',UTC_TIMESTAMP(), 999992);
INSERT INTO `eh_addresses` (`id`, `uuid`, `community_id`, `city_id`, `city_name`, `area_id`, `area_name`, `address`, `building_name`, `apartment_name`, `status`, `operator_uid`, `create_time`, `namespace_id`)
	VALUES(239825274387111808,UUID(),240111044331051304, 14956, '深圳市',  14958, '罗湖区' ,'深业中心大厦-32F','深业中心大厦','32F','2','0',UTC_TIMESTAMP(), 999992);


INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20683, 1000750, 240111044331051304, 239825274387111751, '深业中心大厦-0101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20684, 1000750, 240111044331051304, 239825274387111752, '深业中心大厦-0102', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20685, 1000750, 240111044331051304, 239825274387111753, '深业中心大厦-0103', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20686, 1000750, 240111044331051304, 239825274387111754, '深业中心大厦-0104', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20687, 1000750, 240111044331051304, 239825274387111755, '深业中心大厦-0105', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20688, 1000750, 240111044331051304, 239825274387111756, '深业中心大厦-0106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20689, 1000750, 240111044331051304, 239825274387111757, '深业中心大厦-0107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20690, 1000750, 240111044331051304, 239825274387111758, '深业中心大厦-0108', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20691, 1000750, 240111044331051304, 239825274387111759, '深业中心大厦-0109', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20692, 1000750, 240111044331051304, 239825274387111760, '深业中心大厦-0110', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20693, 1000750, 240111044331051304, 239825274387111761, '深业中心大厦-0111', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20694, 1000750, 240111044331051304, 239825274387111762, '深业中心大厦-0112', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20695, 1000750, 240111044331051304, 239825274387111763, '深业中心大厦-0113', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20696, 1000750, 240111044331051304, 239825274387111764, '深业中心大厦-0201-1901', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20697, 1000750, 240111044331051304, 239825274387111765, '深业中心大厦-20F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20698, 1000750, 240111044331051304, 239825274387111766, '深业中心大厦-2201', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20699, 1000750, 240111044331051304, 239825274387111767, '深业中心大厦-2202', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20700, 1000750, 240111044331051304, 239825274387111768, '深业中心大厦-2203', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20701, 1000750, 240111044331051304, 239825274387111769, '深业中心大厦-2204', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20702, 1000750, 240111044331051304, 239825274387111770, '深业中心大厦-2205', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20703, 1000750, 240111044331051304, 239825274387111771, '深业中心大厦-2206', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20704, 1000750, 240111044331051304, 239825274387111772, '深业中心大厦-2207', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20705, 1000750, 240111044331051304, 239825274387111773, '深业中心大厦-2208-2211', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20706, 1000750, 240111044331051304, 239825274387111774, '深业中心大厦-2212', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20707, 1000750, 240111044331051304, 239825274387111775, '深业中心大厦-2213', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20708, 1000750, 240111044331051304, 239825274387111776, '深业中心大厦-2215', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20709, 1000750, 240111044331051304, 239825274387111777, '深业中心大厦-2216', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20710, 1000750, 240111044331051304, 239825274387111778, '深业中心大厦-2218', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20711, 1000750, 240111044331051304, 239825274387111779, '深业中心大厦-2301', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20712, 1000750, 240111044331051304, 239825274387111780, '深业中心大厦-2314', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20713, 1000750, 240111044331051304, 239825274387111781, '深业中心大厦-2315', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20714, 1000750, 240111044331051304, 239825274387111782, '深业中心大厦-2316', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20715, 1000750, 240111044331051304, 239825274387111783, '深业中心大厦-2302-2313', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20716, 1000750, 240111044331051304, 239825274387111784, '深业中心大厦-2404-2411', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20717, 1000750, 240111044331051304, 239825274387111785, '深业中心大厦-2401-03', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20718, 1000750, 240111044331051304, 239825274387111786, '深业中心大厦-12-16', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20719, 1000750, 240111044331051304, 239825274387111787, '深业中心大厦-2501', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20720, 1000750, 240111044331051304, 239825274387111788, '深业中心大厦-2516', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20721, 1000750, 240111044331051304, 239825274387111789, '深业中心大厦-2502-2503', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20722, 1000750, 240111044331051304, 239825274387111790, '深业中心大厦-2505-2511', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20723, 1000750, 240111044331051304, 239825274387111791, '深业中心大厦-2512-2513', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20724, 1000750, 240111044331051304, 239825274387111792, '深业中心大厦-2514-2515', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20725, 1000750, 240111044331051304, 239825274387111793, '深业中心大厦-26F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20726, 1000750, 240111044331051304, 239825274387111794, '深业中心大厦-27F', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20727, 1000750, 240111044331051304, 239825274387111795, '深业中心大厦-2901A-2901B', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20728, 1000750, 240111044331051304, 239825274387111796, '深业中心大厦-2902-2903', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20729, 1000750, 240111044331051304, 239825274387111797, '深业中心大厦-2904', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20730, 1000750, 240111044331051304, 239825274387111798, '深业中心大厦-2906', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20731, 1000750, 240111044331051304, 239825274387111799, '深业中心大厦-3002', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20732, 1000750, 240111044331051304, 239825274387111800, '深业中心大厦-3003', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20733, 1000750, 240111044331051304, 239825274387111801, '深业中心大厦-3005', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20734, 1000750, 240111044331051304, 239825274387111802, '深业中心大厦-3006', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20735, 1000750, 240111044331051304, 239825274387111803, '深业中心大厦-3007-3008', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20736, 1000750, 240111044331051304, 239825274387111804, '深业中心大厦-3101', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20737, 1000750, 240111044331051304, 239825274387111805, '深业中心大厦-3105A', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20738, 1000750, 240111044331051304, 239825274387111806, '深业中心大厦-3106', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20739, 1000750, 240111044331051304, 239825274387111807, '深业中心大厦-3107', '0');
INSERT INTO `eh_organization_address_mappings` (`id`, `organization_id`, `community_id`, `address_id`, `organization_address`, `living_status`)
	VALUES (20740, 1000750, 240111044331051304, 239825274387111808, '深业中心大厦-32F', '0');


INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (2, 999992, 'EhOrganizations', 1000750, 'parking_recharge','停车充值', 1, now());
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (3, 999992, 'EhOrganizations', 1000750, 'pmsy','物业缴费', 1, now());

INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (4, 999990, 'EhOrganizations', 1001080, 'payment_card','一卡通', 1, now());

INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (5, 999993, 'EhOrganizations', 1000631, 'parking_recharge','停车充值', 1, now());
INSERT INTO `eh_stat_service` (`id`,`namespace_id`,`owner_type`,`owner_id`,`service_type`,`service_name`,`status`,`create_time`) values (6, 999993, 'EhOrganizations', 1000631, 'pmsy','物业缴费', 1, now());


-- 任务设备和二维码不对应错误提示 add by xiongying20160913
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('equipment', '10019', 'zh_CN', '二维码和任务设备不对应');

-- 打开物业报修 web菜单
delete from eh_web_menu_scopes where owner_id = 999992 and menu_id in (24000, 25000, 26000, 27000);
-- 屏蔽物业1.0菜单
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1), 21000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1), 22000,'', 'EhNamespaces', 999992 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1), 23000,'', 'EhNamespaces', 999992 , 0);

delete  from eh_launch_pad_items where item_label = '任务管理' and namespace_id = 999992;
delete  from eh_launch_pad_items where item_label = '物业服务' and namespace_id = 999992;



-- merge from dev-banner-delta-data.sql by lqs 20160914
-- 增加广告管理菜单 by xq.tian 20160825
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (49600,'广告管理',40000,null,null,0,2,'/40000/49600','park',458);

-- 添加权限
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (830,0,'广告管理','广告管理',null);

-- 添加菜单的权限
set @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),830,49600,'广告管理',1,1,'广告管理  全部权限',577);

-- 添加菜单与权限的关联关系    role_id: 1001为物业公司超级管理员
set @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%49600%');


-- 添加banner激活数量配置
set @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'banner.max.active.count', '8', '一个域空间下激活banner的最多个数', '0', NULL);

-- 添加banner提示
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'banner', '10003', 'zh_CN', '广告激活数量超过最大值啦!');

-- 修改banner的关闭状态从0变为3
UPDATE `eh_banners` SET `status` = 3 WHERE `status` = 0;

-- 修改场景类型的display_name
UPDATE `eh_scene_types` SET `display_name`='普通用户场景' WHERE (`name`='default');
UPDATE `eh_scene_types` SET `display_name`='管理公司场景' WHERE (`name`='pm_admin');

-- 修改物业报修2.0 消息 短信模版
delete from eh_locale_templates where id in (195, 196 , 197 , 198, 205);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('195', 'pmtask.notification', '5', 'zh_CN', '任务操作模版', '您于${day}日${hour}时发起的服务已由 ${operatorName} ${operatorPhone} 完成，快去评价打分吧~', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('196', 'pmtask.notification', '6', 'zh_CN', '任务操作模版', '业主 ${creatorName} ${creatorPhone} 发起的服务单已由 ${operatorName} ${operatorPhone} 完成', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('205', 'pmtask.notification', '7', 'zh_CN', '任务操作模版', '${creatorName} ${creatorPhone}已发起一个任务，请尽快处理', '0');

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('197', 'sms.default.yzx', '10', 'zh_CN', '任务操作模版', '<{operatorName}><{operatorPhone}>已将一个<{categoryName}>单派发给你，请尽快处理', '999992');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('198', 'sms.default.yzx', '11', 'zh_CN', '任务操作模版', '<{creatorName}><{creatorPhone}>已发起一个任务，请尽快处理', '999992');

-- 左邻 海岸 ibase 深业物业 讯美 add by xiongying20160914
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(95,2,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (60, 2, '3.9.0', '', '${homeurl}/web/download/apk/iOS-everhomes-3-9-0.html', '0');


INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(96,28,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (61, 28, '3.9.0', '', '${homeurl}/web/download/apk/iOS-haian-3-9-0.html', '0');


INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(97,39,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (62, 39, '3.9.0', '', '${homeurl}/web/download/apk/iOS-ibase-3-9-0.html', '0');


INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(98,31,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (63, 31, '3.9.0', '', '${homeurl}/web/download/apk/iOS-sywy-3-9-0.html', '0');


INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(99,6,'-0.1','3154944','0','3.9.0','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (64, 6, '3.9.0', '', '${homeurl}/web/download/apk/iOS-zz-3-9-0.html', '0');

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
-- 屏蔽爱特家物业报修2.0菜单：
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 24000,'', 'EhNamespaces', 999988 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 25000,'', 'EhNamespaces', 999988 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 26000,'', 'EhNamespaces', 999988 , 0);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 27000,'', 'EhNamespaces', 999988 , 0);
-- 屏蔽深业场所预定菜单：
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1), 42000,'', 'EhNamespaces', 999992 , 0);

-- 升级规则ibase 深业 讯美 Android & ibase ios  add by xiongying20160922
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(105,38,'-0.1','3154946.0','0','3.9.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (70, 38, '3.9.2', 'http://apk.zuolin.com/apk/IBase-3.9.2.2016091412-release.apk', '${homeurl}/download/apk/andriod-ibase-3-9-2.html ', '0');


INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(106,30,'-0.1','3154946.0','0','3.9.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (71, 30, '3.9.2', 'http://apk.zuolin.com/apk/ShenyeProperty-3.9.2.2016091412-release.apk', '${homeurl}/download/apk/andriod-sywy-3-9-2.html', '0');


INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(107,5,'-0.1','3154946.0','0','3.9.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (72, 5, '3.9.2', 'http://apk.zuolin.com/apk/XmTecPark-3.9.2.2016091912-release.apk', '${homeurl}/download/apk/andriod-xunmei-3-9-2.html', '0');


INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`)
    VALUES(108,39,'-0.1','3154946.0','0','3.9.2','0',UTC_TIMESTAMP());
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `namespace_id`) VALUES (73, 39, '3.9.2', '', '${homeurl}/download/apk/iOS-ibase-3-9-2.html', '0');



-- 第二份文件合并过来
-- effect for linging visitor
INSERT INTO `eh_configurations` (`namespace_id`,  `name`, `value`, `description`) VALUES (0, 'aclink.qr_visitor_cnt', '6', 'effect time for lingling effect');


-- 微信link 保修短信接收人调整
SET @organization_task_target_id = (SELECT MAX(id) FROM `eh_organization_task_targets`);
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',230275,'REPAIRS','push');
INSERT INTO `eh_organization_task_targets` (`id`,`owner_type`,`owner_id`,`target_type`,`target_id`,`task_type`,`message_type`) VALUES((@organization_task_target_id := @organization_task_target_id + 1),'EhCommunities',240111044331053517,'EhUsers',230275,'REPAIRS','sms');
-- 微信link 删除短信接收人
DELETE FROM `eh_organization_task_targets` WHERE `target_type` = 'EhUsers' AND `target_id` = 1002757;

-- 菜单调整 by sfyan 20160919
 SET @menu_scope_id = 1000;
 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),`id`,'EhNamespaces',999993,2 FROM `eh_web_menus` WHERE `id` NOT IN (SELECT `menu_id` FROM `eh_web_menu_scopes` WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999993);
 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),`id`,'EhNamespaces',1000000,2 FROM `eh_web_menus` WHERE `id` NOT IN (SELECT `menu_id` FROM `eh_web_menu_scopes` WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 1000000);
 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),`id`,'EhNamespaces',999992,2 FROM `eh_web_menus` WHERE `id` NOT IN (SELECT `menu_id` FROM `eh_web_menu_scopes` WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999992);
 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),`id`,'EhNamespaces',999999,2 FROM `eh_web_menus` WHERE `id` NOT IN (SELECT `menu_id` FROM `eh_web_menu_scopes` WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999999);
 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),`id`,'EhNamespaces',999990,2 FROM `eh_web_menus` WHERE `id` NOT IN (SELECT `menu_id` FROM `eh_web_menu_scopes` WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999990);
 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),`id`,'EhNamespaces',999989,2 FROM `eh_web_menus` WHERE `id` NOT IN (SELECT `menu_id` FROM `eh_web_menu_scopes` WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999989);
 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),`id`,'EhNamespaces',999991,2 FROM `eh_web_menus` WHERE `id` NOT IN (SELECT `menu_id` FROM `eh_web_menu_scopes` WHERE `owner_type` = 'EhNamespaces' AND `owner_id` = 999991);
 INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`, `owner_type`, `owner_id`, `apply_policy`) SELECT (@menu_scope_id := @menu_scope_id + 1),`id`,'EhOrganizations',1002756,2 FROM `eh_web_menus` WHERE `id` NOT IN (SELECT `menu_id` FROM `eh_web_menu_scopes` WHERE `owner_type` = 'EhOrganizations' AND `owner_id` = 1002756);
 delete from `eh_web_menu_scopes` where id < 1000;
 
-- 爱特家迷你居 菜单整理 by sfyan 20160919 
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),10000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),11200,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),12000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),20000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),21000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),22000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),23000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),24000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),25000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),26000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),27000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),30500,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),31000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),32000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),33000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),34000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),35000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),36000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),40000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41200,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),41400,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43400,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43410,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43420,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43430,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43440,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43500,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43501,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),43502,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),46000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47150,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47160,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47200,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),47300,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),50000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),51000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),51100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52200,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52300,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),52400,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),53000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),53100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56105,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56106,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56181,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56107,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56186,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56108,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56191,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56200,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56210,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56220,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56230,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58110,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58111,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58112,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58113,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58120,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58121,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58122,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58123,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58130,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58131,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58132,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58140,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58200,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58210,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58211,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58212,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58220,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58221,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58222,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58230,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),58231,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59000,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59100,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59150,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59160,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59200,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59300,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),59400,'', 'EhNamespaces', 999988,2);

delete from eh_web_menu_scopes where owner_id = 999991 and menu_id in (SELECT `id`  FROM `eh_web_menus` WHERE `path` LIKE '%44000/%');

delete from eh_web_menu_scopes where owner_id not in (999992, 999988) and menu_id in (24000,25000,26000,27000);

delete from eh_web_menu_scopes where owner_id in (999988) and menu_id in (21000,22000,23000);

delete from eh_web_menu_scopes where owner_id = 999991 and menu_id = 44000;

-- 爱特家迷你居 加上广告管理  by sfyan 20160922
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),49600,'', 'EhNamespaces', 999988,2);

-- 考勤管理的菜单整理 by sfyan 20160922
-- 删除之前的考勤管理菜单数据
DELETE FROM `eh_web_menus` WHERE `path` LIKE '%56100/%';

DELETE FROM `eh_acl_privileges` WHERE `id` IN (790,791,792,793,794,795,796,797,798,799,820,821,822,823);

DELETE FROM `eh_web_menu_privileges` WHERE `menu_id` IN (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');

-- 考勤规则设置

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56110,'考勤规则设置',56100,NULL,NULL,1,2,'/50000/56000/56100/56110','park',100);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56111,'规则管理',56110,NULL,'punch_rule',0,2,'/50000/56000/56100/56110/56111','park',110);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56112,'时间管理',56110,NULL,'punch_time',0,2,'/50000/56000/56100/56110/56112','park',120);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56113,'地点管理',56110,NULL,'punch_location',0,2,'/50000/56000/56100/56110/56113','park',130);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56114,'wifi管理',56110,NULL,'punch_wifi',0,2,'/50000/56000/56100/56110/56114','park',567);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56115,'排班管理',56110,NULL,'punch_scheduling',0,2,'/50000/56000/56100/56110/56115','park',140);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (790,0,'规则管理','规则管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (791,0,'时间管理','时间管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (792,0,'地点管理','地点管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (793,0,'wifi管理','wifi管理',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (794,0,'排班管理','排班管理',NULL);


INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),790,56111,'规则管理',1,1,'规则管理  全部权限',100);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),791,56112,'时间管理',1,1,'时间管理 全部权限',110);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),792,56113,'地点管理',1,1,'地点管理 全部权限',120);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),793,56114,'wifi管理',1,1,'wifi管理 全部权限',130);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),794,56115,'排班管理',1,1,'排班管理 全部权限',140);

-- 审批规则设置
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56120,'审批规则设置',56100,NULL,NULL,1,2,'/50000/56000/56100/56120','park',200);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56121,'审批规则',56120,NULL,'approval_rule',0,2,'/50000/56000/56100/56120/56121','park',210);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56122,'审批人设置',56120,NULL,'approval_personal',0,2,'/50000/56000/56100/56120/56122','park',220);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (822,0,'审批规则','审批规则 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (823,0,'审批人设置','审批人设置 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),822,56121,'审批规则',1,1,'审批规则 全部权限',210);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),823,56122,'审批人设置',1,1,'审批人设置 全部权限',220);

-- 考勤规则配置
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56130,'考勤规则配置',56100,NULL,NULL,1,2,'/50000/56000/56100/56130','park',300);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56131,'通用设置',56130,NULL,'punch_setting',0,2,'/50000/56000/56100/56130/56131','park',310);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56132,'个人设置',56130,NULL,'punch_personal_setting',0,2,'/50000/56000/56100/56130/56132','park',320);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (795,0,'通用设置','通用设置 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (796,0,'个人设置','个人设置 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),795,56131,'通用设置',1,1,'通用设置 全部权限',310);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),796,56132,'个人设置',1,1,'个人设置 全部权限',320);

-- 打卡详情
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56140,'打卡详情',56100,null,null,1,2,'/50000/56000/56100/56140','park',400);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56141,'打卡详情',56140,null,'punch_detail',0,2,'/50000/56000/56100/56140/56141','park',410);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (797,0,'打卡详情','打卡详情 全部权限',null);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),797,56141,'打卡详情',1,1,'打卡详情 全部权限',410);


-- 异常处理
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56150,'异常处理',56100,null,null,1,2,'/50000/56000/56100/56150','park',500);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56151,'异常申请',56150,null,'abnormal_apply',0,2,'/50000/56000/56100/56150/56151','park',510);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56152,'请假申请',56150,null,'leave_apply',0,2,'/50000/56000/56100/56150/56152','park',520);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (798,0,'异常申请','异常申请 全部权限',NULL);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (799,0,'请假申请','请假申请 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),798,56151,'异常申请',1,1,'异常申请 全部权限',510);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),799,56152,'请假申请',1,1,'请假申请 全部权限',520);


-- 考勤统计
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56160,'考勤统计',56100,null,null,1,2,'/50000/56000/56100/56160','park',600);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56161,'考勤统计',56160,null,'punch_statistics',0,2,'/50000/56000/56100/56160/56161','park',610);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (820,0,'考勤统计','考勤统计 全部权限',null);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),820,56161,'考勤统计',1,1,'考勤统计  全部权限',610);

-- 请假类型设置
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56170,'请假类型设置',56100,null,null,1,2,'/50000/56000/56100/56170','park',700);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (56171,'类型设置',56170,null,'leave_setting',0,2,'/50000/56000/56100/56170/56171','park',710);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (821,0,'请假类型设置','请假类型设置 全部权限',NULL);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),821,56171,'类型设置',1,1,'类型设置 全部权限',710);


-- 删除之前的角色 考勤权限
DELETE FROM `eh_acls` WHERE `privilege_id` IN (SELECT `privilege_id` FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%'));

-- 给管理员赋考勤管理权限
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1001,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1002,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1005,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `privilege_id`, 1006,0,1,now() FROM `eh_web_menu_privileges` WHERE `menu_id` in (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');

-- 重整开放给域或机构 by sfyan 20160922
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);

DELETE FROM `eh_web_menu_scopes` WHERE `menu_id` IN (SELECT id FROM `eh_web_menus` WHERE `path` LIKE '%56100/%');

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999988,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999988,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999989,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999989,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999990,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999991,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999991,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999992,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999993,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999993,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 1000000,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhNamespaces', 999999,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhNamespaces', 999999,2);

INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhOrganizations', 1002756,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhOrganizations', 1002756,2);


INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56110,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56111,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56112,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56113,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56114,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56115,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56120,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56121,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56122,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56130,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56131,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56132,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56140,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56141,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56150,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56151,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56152,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56160,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56161,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56170,'', 'EhOrganizations', 1004005,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),56171,'', 'EhOrganizations', 1004005,2);


-- 物业保修2.0新增权限 by sunwen 20160923
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
  VALUES (805,0,'查看我的任务','任务管理 查看我的任务',NULL);
  
UPDATE eh_acl_privileges SET name = '查看所有任务', description = '任务管理 查看所有任务' WHERE id = 904;

UPDATE eh_web_menu_privileges SET name = '查看所有任务', discription = '查看所有任务 全部权限' where privilege_id = 904 and menu_id = 24000;

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);

INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
	VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),805,24000,'查看所有任务',1,1,'查看所有任务 全部权限',603);
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '805', 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`)
	VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, '805', 1002,0,1,now());
	
	
-- 新增客户资料和车辆管理菜单 by sfyan 20160923
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (37000,'客户资料管理',30000,null,'customer_management',0,2,'/30000/37000','park',370);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (38000,'车辆管理',30000,null,'car_management',0,2,'/50000/38000','park',380);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (420,0,'客户资料管理','客户资料管理 全部权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (421,0,'车辆管理','车辆管理 全部权限',null);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),420,37000,'客户资料管理',1,1,'客户资料管理  全部权限',200);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),421,38000,'车辆管理',1,1,'车辆管理  全部权限',201);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),37000,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),38000,'', 'EhNamespaces', 999992,2);


-- 深业停车充值 by sw 20160923
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('160', 'parking.shenye.projectId', '4403000043', '深业停车充值项目ID', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('161', 'parking.default.nickname', '默认昵称', '停车充值默认昵称', '0', NULL);

INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`) 
VALUES ('10003', 'community', '240111044331051302', '深发花园停车场', 'BOSIGAO2', NULL, '1', '2', '1025', '2016-08-29 17:28:10');

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10022', '999992', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'default', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10023', '999992', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', '停车充值', 'cs://1/image/aW1hZ2UvTVRwaFpXRmtZek5qTWpobE1UWTRaVE5qWlRjek4yWTFaRFU1WlRJeVlqUXlNQQ', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1');
SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41100,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41200,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999992,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41400,'', 'EhNamespaces', 999992,2);

-- 新增消息推送和服务联盟 by sfyan 20160923
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44300,'消息推送设置',44000,null,'message_push_setting',0,2,'/44000/44300','park',458);
INSERT INTO `eh_web_menus` (`id`,`name`,`parent_id`,`icon_url`,`data_type`,`leaf_flag`,`status`,`path`,`type`,`sort_num`)
VALUES (44400,'申请记录',44000,null,'apply_record',0,2,'/44000/44400','park',459);

INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (450,0,'消息推送设置','消息推送设置 全部权限',null);
INSERT INTO `eh_acl_privileges` (`id`,`app_id`,`name`,`description`,`tag`)
VALUES (451,0,'申请记录','申请记录 全部权限',null);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),450,44300,'消息推送设置',1,1,'消息推送设置  全部权限',606);
INSERT INTO `eh_web_menu_privileges` (`id`,`privilege_id`,`menu_id`,`name`,`show_flag`,`status`,`discription`,`sort_num`)
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1),451,44400,'申请记录',1,1,'申请记录  全部权限',607);

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44300,'', 'EhNamespaces', 1000000,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@menu_scope_id := @menu_scope_id + 1),44400,'', 'EhNamespaces', 1000000,2);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 450, 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 451, 1001,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 450, 1002,0,1,now());
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`) VALUES ((@acl_id := @acl_id + 1), 'EhOrganizations', 1, 451, 1002,0,1,now());

