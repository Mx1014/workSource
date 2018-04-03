-- 解决邮箱验证不通过的问题，bug号#10051，吕磊
UPDATE eh_configurations set `value` = 'webmail.zuolin.com' WHERE `name` = 'mail.smtp.address' and `namespace_id` = 1000000;
UPDATE eh_configurations set `value` = '465' WHERE `name` = 'mail.smtp.port' and `namespace_id` = 1000000;

-- 楼栋名称修改 光大We谷产业园 add by sfyan 20170522
UPDATE `eh_buildings` SET `name` = '光大We谷产业园2栋4号楼' WHERE `name` = '光大We谷产业园2栋2号楼' AND `community_id` = 240111044331056800;
UPDATE `eh_buildings` SET `name` = '光大We谷产业园2栋3号楼' WHERE `name` = '光大We谷产业园2栋1号楼' AND `community_id` = 240111044331056800;

UPDATE `eh_addresses` SET `building_name` = '光大We谷产业园2栋4号楼' WHERE `building_name` = '光大We谷产业园2栋2号楼' AND `community_id` = 240111044331056800;
UPDATE `eh_addresses` SET `building_name` = '光大We谷产业园2栋3号楼' WHERE `building_name` = '光大We谷产业园2栋1号楼' AND `community_id` = 240111044331056800;

update `eh_web_menus` set data_type = 'user--user_identify' where id = 35000;

UPDATE eh_launch_pad_items SET `action_data` = '{\"url\":\"https://biz.zuolin.com/zl-ec/rest/service/front/logon?mallId=1999978&hideNavigationBar=1&sourceUrl=https%3a%2f%2fbiz.zuolin.com%2fnar%2fbiz%2fweb%2fapp%2fuser%2findex.html%23%2fmicroshop%2fhome%3fisfromindex%3d0%26_k%3dzlbiz#sign_suffix"}' WHERE namespace_id = 999979 and item_label = 'we生活';

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 35000, '', 'EhNamespaces', 1000000, 2); 
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
VALUES ((@menu_scope_id := @menu_scope_id + 1), 30600, '', 'EhNamespaces', 999983, 2); 

SET @eh_service_module_scopes_id = (SELECT MAX(id) FROM `eh_service_module_scopes`);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 1000000, 35000, null, NULL, 2);
INSERT INTO `eh_service_module_scopes` (`id`, `namespace_id`, `module_id`, `module_name`, `default_order`, `apply_policy`)
VALUES ((@eh_service_module_scopes_id := @eh_service_module_scopes_id + 1), 999983, 30600, null, NULL, 2);


-- 分期乐能量加油站
SET @orgnaization_id = (select id from `eh_organizations` where namespace_id = 999990 and name = '深圳市分期乐网络科技有限公司' and status = 2);
SET @launch_pad_item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'999990','0','4',@orgnaization_id,'/home','Bizs','分期乐能量加油站','分期乐能量加油站','cs://1/image/aW1hZ2UvTVRwbU4yRXhNek0xTldVek1EaGhPV1ZrT0RVM1pEUmtPR1kzWXpKbU5qUXlOZw','1','1','14','{\"url\":\"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https://biz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%23%2Fstore%2Fdetails%2F14950180711887532177%3F_k%3Dzlbiz#sign_suffix"}','0','0','1','1','','0',NULL,NULL,NULL,'1','park_tourist','0',NULL,NULL,'0',NULL);

SET @orgnaization_id = (select id from `eh_organizations` where namespace_id = 999990 and name = '深圳市分期乐网络科技有限公司' and status = 2);
UPDATE `eh_launch_pad_items` SET item_label = '乐信能量加油站', icon_uri = 'cs://1/image/aW1hZ2UvTVRveFptTTJOVEF5TkdNeVkySXdNREZoWXpBMU9HSm1PRGRpTWpJeE56aGhPUQ' WHERE namespace_id = '999990' AND scope_id = @orgnaization_id;

-- Volgo域空间 华润网络（深圳）有限公司 资源预订增加
SET @orgnaization_id = (select id from `eh_organizations` where namespace_id = 1 and name = '华润网络（深圳）有限公司' and status = 2);
SET @launch_pad_item_id = (SELECT max(id) FROM `eh_launch_pad_items`);
SET @rentalv2_resource_type_id = (SELECT max(id) FROM `eh_rentalv2_resource_types`);

INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES((@rentalv2_resource_type_id := @rentalv2_resource_type_id + 1), '华润通会议室', 0, NULL, 0, 1);
insert into `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`) values((@launch_pad_item_id := @launch_pad_item_id + 1),'1','0','4',@orgnaization_id,'/home','Bizs','华润通会议室','华润通会议室','cs://1/image/aW1hZ2UvTVRwbE5UQXhNVGhsTXpJMlkyVmxObU13TjJRM05XVTRNbUk0T0RRNU1qa3hOUQ','1','1','49',CONCAT('{"resourceTypeId":', @rentalv2_resource_type_id, ',"pageType":0}'),'0','0','1','1','','0',NULL,NULL,NULL,'1','park_tourist','0',NULL,NULL,'0',NULL);

-- 华润 服务广场快递排序
update `eh_launch_pad_items` set default_order = 80 where item_group = 'Bizs' and namespace_id = 999985 and item_name = '快递';