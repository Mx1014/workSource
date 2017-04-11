-- 增加一个“活动时间：”中文字符串 2017-03-31 19:17 add by yanjun
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('activity','11','zh_CN','活动时间：');

-- 更新报修与工作流状态不同步 add by sw 20170206
UPDATE eh_pm_tasks join (SELECT refer_id from eh_flow_cases where id in (SELECT flow_case_id from eh_pm_tasks where flow_case_id != 0) and `status` = 4) t on t.refer_id = id set `status` = 4;

-- 配置ufine会议室预订，并把app上快递改成会议室add by tt, 20170407
update eh_rentalv2_resource_types set pay_mode = 1 where id = 61;
select id into @id from eh_launch_pad_items where namespace_id = 999990 and item_label = '快递' and scene_type = 'park_tourist';
delete from eh_launch_pad_items where id = @id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id, 999990, 0, 0, 0, '/home', 'Bizs', 'MEETINGROOM', '会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', 1, 1, 49, '{"resourceTypeId":60,"pageType":0,"payMode":1}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);
select id into @id from eh_launch_pad_items where namespace_id = 999990 and item_label = '快递' and scene_type = 'pm_admin';
delete from eh_launch_pad_items where id = @id;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id, 999990, 0, 0, 0, '/home', 'Bizs', 'MEETINGROOM', '会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', 1, 1, 49, '{"resourceTypeId":60,"pageType":0,"payMode":1}', 0, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);

update eh_launch_pad_items set icon_uri = 'cs://1/image/aW1hZ2UvTVRwaE9EUTJOV1k1WXpBME1EY3pOR0V3TXpJM1l6QmhaakpqT1dVM1lUTTRNUQ' where namespace_id = 999990 and action_type = 49;
update eh_launch_pad_items set action_data='{"resourceTypeId":61,"pageType":0,"payMode":1}' where namespace_id = 999990 and action_type = 49 and scope_code = 0;

-- 处理设备-标准关联关系脏数据 add by xiongying20170406
update eh_equipment_inspection_equipment_standard_map set review_status = 4 where target_id in(select id from eh_equipment_inspection_equipments where status = 0);

-- 科技园报修 add by sw 20170406
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'sms.default.yzx', 34, 'zh_CN', '物业任务分配人员', '40775');
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (1000000, 'flow:20100', 34, 'zh_CN', '物业任务分配人员', '40775');

-- 更新华润 园区入驻 add by sw 20170406
DELETE from eh_lease_configs where id =3;
INSERT INTO `eh_lease_configs` (`id`, `namespace_id`, `rent_amount_flag`, `issuing_lease_flag`, `issuer_manage_flag`, `park_indroduce_flag`, `renew_flag`) 
	VALUES ('3', '999985', '1', '1', '1', '0', '0');

-- 更新深业优惠券 add by sw 20170407
update eh_launch_pad_layouts set layout_json = '{"versionCode":"2017040701","versionName":"4.4.0","layoutName":"ServiceMarketLayout","displayName":"服务市场","groups":[{"groupName":"","widget":"Banners","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":0,"separatorHeight":0},{"groupName":"","widget":"Navigator","instanceConfig":{"itemGroup":"Coupons"},"style":"Gallery","defaultOrder":3,"separatorFlag":1,"separatorHeight":16,"columnCount":2},{"groupName":"","widget":"Bulletins","instanceConfig":{"itemGroup":"Default"},"style":"Default","defaultOrder":1,"separatorFlag":1,"separatorHeight":16},{"groupName":"商家服务","widget":"Navigator","instanceConfig":{"itemGroup":"Bizs"},"style":"Default","defaultOrder":5,"separatorFlag":0,"separatorHeight":0}]}', version_code = '2017040701' where namespace_id = 999992 and id in (34, 36);
UPDATE eh_launch_pad_items set item_width = 1,icon_uri='cs://1/image/aW1hZ2UvTVRvMllUSXpaakkxTkdNMlpUVmpOekkxTlRBMllXVXhabVV3WVRBeFpXTmhNQQ' where item_label = '优惠券' and namespace_id = 999992;
UPDATE eh_launch_pad_items set item_width = 1,icon_uri='cs://1/image/aW1hZ2UvTVRvM056WXlOVGM0WkRFNE9UQTNOVGczWW1Jd1pERXdORFkwT1RjME1ETmhaQQ' where item_label = '优选商城' and namespace_id = 999992;

	
-- 更新储能icon顺序  add by sw 20170407
SET @order_ = 0;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '上网' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '门禁' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '钱包' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '停车' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '送水' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '班车' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '旅游' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '会议室' and namespace_id = 999990;
UPDATE eh_launch_pad_items set default_order = (@order_ := @order_ + 1) where item_label = '嗒嗒会议室' and namespace_id = 999990;

UPDATE eh_launch_pad_items set delete_flag = 0,icon_uri = 'cs://1/image/aW1hZ2UvTVRwaU1qWTNZVEl3TVRBeE5tTXhNVEl3WmpKaE1EZ3dZVE13WlRRNU9UUXhNQQ' where item_label = '会议室' and namespace_id = 999990;

-- 储能加任务管理菜单 add by sw 20170407
SET @eh_web_menu_scopes = (SELECT max(id) FROM eh_web_menu_scopes);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@eh_web_menu_scopes := @eh_web_menu_scopes + 1), '70000', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@eh_web_menu_scopes := @eh_web_menu_scopes + 1), '70100', '', 'EhNamespaces', '999990', '2');
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@eh_web_menu_scopes := @eh_web_menu_scopes + 1), '70200', '', 'EhNamespaces', '999990', '2');
	
-- 任务管理下面的菜单 整理 add by sfyan 20170410 已执行
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (10079, '0', '业务授权视图 管理员', '业务授权视图 业务模块权限', NULL);
UPDATE eh_acl_privileges SET name = '任务列表 管理员', description = '任务列表 业务模块权限' WHERE id = 10078;

DELETE FROM eh_web_menu_privileges WHERE privilege_id = 10078;

SET @eh_web_menu_privileges = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10078', '70100', '任务列表', '1', '1', '任务列表 全部权限', '711');
INSERT INTO `eh_web_menu_privileges` VALUES ((@eh_web_menu_privileges := @eh_web_menu_privileges + 1), '10079', '70200', '业务授权视图', '1', '1', '业务授权视图 全部权限', '712');

UPDATE eh_service_modules SET level = 1 WHERE id = 70000;
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('70100', '任务列表', '70000', '/70000/70100', '0', '2', '2', '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`) VALUES ('70200', '业务授权视图', '70000', '/70000/70200', '0', '2', '2', '0', UTC_TIMESTAMP());

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '70100', '1', '10078', NULL, '0', UTC_TIMESTAMP());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '70200', '1', '10079', NULL, '0', UTC_TIMESTAMP());


SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`,`owner_type`,`grant_type`,`privilege_id`,`role_id`,`order_seq`,`creator_uid`,`create_time`, `role_type`)
SELECT (@acl_id := @acl_id + 1), 'EhOrganizations', 1, `id`, 1001,0,1,NOW(), 'EhAclRoles' FROM `eh_acl_privileges` WHERE id = 10079 ;

-- 海岸新增园区楼栋 add by sfyan 20170410 已执行

SET @community_id = 240111044331054836; 
SET @organization_id = 1000631;  	
SET @community_geopoint_id = (SELECT MAX(id) FROM `eh_community_geopoints`);  
SET @forum_id = 179511;
SET @feedback_forum_id = 179512; 
SET @building1_id = 182755;
SET @shi_id = 14953; 
SET @qu_id = 16090; 

INSERT INTO `eh_regions` (`id`, `parent_id`, `name`, `pinyin_name`, `pinyin_prefix`, `path`, `level`, `scope_code`, `iso_code`, `tel_code`, `status`, `hot_flag`, `namespace_id`) 
	VALUES (@qu_id, @shi_id, '福田区', 'FUTIANQU', 'NSQ', '/广东/深圳市/福田区', '3', '3', NULL, '0755', '2', '0', '999993');

INSERT INTO `eh_communities` (`id`, `uuid`, `city_id`, `city_name`, `area_id`, `area_name`, `name`, `alias_name`, `address`, `zipcode`, `description`, `detail_description`, `apt_segment1`, `apt_segment2`, `apt_segment3`, `apt_seg1_sample`, `apt_seg2_sample`, `apt_seg3_sample`, `apt_count`, `creator_uid`, `operator_uid`, `status`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `community_type`, `default_forum_id`, `feedback_forum_id`, `update_time`, `namespace_id`)
	VALUES(@community_id, UUID(), @shi_id, '深圳市',  @qu_id, '福田区', '海岸环庆大厦', '海岸环庆大厦', '福田路24号', NULL, '',NULL, NULL, NULL, NULL, NULL, NULL,NULL, 214, 1,NULL,'2',UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL,NULL,'1', @forum_id, @feedback_forum_id, UTC_TIMESTAMP(), 999993);
INSERT INTO `eh_community_geopoints`(`id`, `community_id`, `description`, `longitude`, `latitude`, `geohash`) 
	VALUES((@community_geopoint_id := @community_geopoint_id + 1), @community_id, '', 114.084319, 22.540574, 'uxbpbzvxcryp');
INSERT INTO `eh_organization_communities`(organization_id, community_id) 
	VALUES(@organization_id, @community_id);
	
INSERT INTO `eh_buildings` (`id`, `community_id`, `name`, `alias_name`, `manager_uid`, `contact`, `address`, `area_size`, `longitude`, `latitude`, `geohash`, `description`, `poster_uri`, `status`, `operator_uid`, `operate_time`, `creator_uid`, `create_time`, `delete_time`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `namespace_id`)
	VALUES(@building1_id, @community_id, '海岸环庆大厦', '海岸环庆大厦', 0, '0755-82738680', '深圳市福田区福田路24号', 103587.01, 114.084319, 22.540574, 'uxbpbzvxcryp', '海岸环庆大厦作为福田CBD国际甲级写字楼，是全深圳唯一的70年办公产权的写字楼，享受全深圳所有最顶级最成熟的配套资源。项目位于福田中心区福田南路24号，以中心公园为全景，由深圳市海岸融通投资有限公司开发，江苏建工集团承建。海岸城的缔造者，11年写字楼开发运营成熟经验，少数成功开发超过5座写字楼的地产企业，精工铸造超过同行的商务品质，海岸物业管理为资产保值增值提供保障。 
海岸环庆大厦占地面积7343.11平方米，总建筑面积103587.01平方米，楼高225米，其中办公面积69657.59平方米；商业面积4613.59平方米。地面停车位133个，地下停车位335个，电梯采用日本三菱品牌，客梯12部（高中低区各4部），转乘梯3部，消防电梯2部，低区电梯速度为4M/秒，中高区电梯速度为6M/秒，电梯轿厢规格为2000*1700*3200.大堂面积580平方米，层高13.4米，地面采用浅灰色砂岩石，墙面采用米黄大理石，天花为玻璃天窗配采光遮阳百叶。外立面采用LOW-E玻璃幕墙，室内空调采用美国约克水冷中央空调。竣工验收已在2016年11月18日完成，计划于2016年12月28日入伙。世界500强企业进驻，全球金融企业总部聚集，共赢中心未来。 ', NULL, 2, 1, UTC_TIMESTAMP(), 1, UTC_TIMESTAMP(), NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 999993);

-- 上海星商汇 layout问题 add by sfyan
update `eh_launch_pad_layouts` set version_code = 2017041101, `layout_json` = replace(`layout_json`, 'NewsFlash', 'News'), `layout_json` = replace(`layout_json`, '"versionCode":"2017011302"', '"versionCode":"2017041101"') where namespace_id = 999981 and `name` = 'ServiceMarketLayout';

