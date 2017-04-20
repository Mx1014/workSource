-- 资源预订工作流模板，add by wh, 20161219
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES (@id:=@id+1, 'rental.flow', 2, 'zh_CN', '自定义字段', '请等待${offlinePayeeName}（${offlinePayeeContact}）上门收费，或者到${offlineCashierAddress}去支付', 0);

-- 资源预订短信模板，add by wh, 20161219
SET @id =(SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','28','zh_CN','资申成-正中会','38570','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','29','zh_CN','资申败-正中会','38572','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','30','zh_CN','资付成-正中会','38573','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','31','zh_CN','资预败-正中会','38574','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','32','zh_CN','资取消-正中会','38575','999983');

-- 文案早上改成上午
UPDATE eh_locale_strings SET TEXT = '上午' WHERE scope ='rental.notification' AND CODE = 0 AND locale = 'zh_CN';

-- 推送模板
SET @id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','3','zh_CN','申请成功','您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快完成支付，支付方式支持：1. 请联系${offlinePayeeName}（${offlinePayeeContact}）上门收费，2. 到${offlineCashierAddress}付款；感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','4','zh_CN','申请失败','您申请预约的${useTime}的${resourceName}没有通过审批，您可以申请预约其他空闲资源，由此给您造成的不便，敬请谅解，感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','5','zh_CN','支付成功','您已完成支付，成功预约${useTime}的${resourceName}，请按照预约的时段使用资源，感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','6','zh_CN','预约失败','您申请预约的${useTime}的${resourceName}已经被其他客户抢先预约成功，您可以继续申请预约其他时段，由此给您造成的不便，敬请谅解，感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','7','zh_CN','取消短信','您申请预约的${useTime}的${resourceName}由于超时未支付或被其他客户抢先预约，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','8','zh_CN','催办','客户（${userName}${userPhone}）提交资源预约的线下支付申请，预约${resourceName}，使用时间：${useTime}，订单金额${price}，请尽快联系客户完成支付','0');

-- 2017年3月16日10:21:02 添加 by wh

-- 更新资源类型:特别注意如果这里更改rows = 0 说明id不对,就不能执行下一句
UPDATE eh_rentalv2_resource_types SET pay_mode = 1 WHERE namespace_id = 999983 AND NAME = '会议室预订' AND id =10505;
-- 前一条执行好了执行这一条
UPDATE eh_launch_pad_items  SET action_data ='{"resourceTypeId":10505,"pageType":0,"payMode":1}' WHERE namespace_id = 999983 AND item_label='会议室预订';

-- 添加资源预约工作流的菜单
SET @id = (SELECT MAX(id) FROM eh_web_menu_scopes );
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'40450','','EhNamespaces','999983','2');

-- 俱乐部3.0  add by xq.tian  2017/03/01
UPDATE `eh_locale_templates` SET `text`='俱乐部推荐：${groupName}' WHERE `scope`='group.notification' AND `code`=33;

-- 工作流信息 janson
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10007, 'zh_CN', '${applierName} 已取消任务', '${applierName} 已取消任务');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10008, 'zh_CN', '发起人已取消任务', '发起人已取消任务');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10009, 'zh_CN', '任务已完成', '任务已完成');



-- 更新星尚汇服务市场投诉建议为物业服务，关联任务7649，add by tt, 20170320 
update eh_launch_pad_items set item_name = '物业服务', item_label = '物业服务', action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业服务"}' where namespace_id = 999981 and item_lable = '投诉建议';


-- 添加科技园大堂门禁菜单，add by tt, 20170321
select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41000, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41010, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41020, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41030, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41040, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41050, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41060, '', 'EhNamespaces', 1000000, 2);

select max(id) into @id from `eh_launch_pad_items`;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 0, 0, '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 4, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 0, 0, '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 4, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'default', 1, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 0, 0, '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 4, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL);


-- 配置ufine会议室预订，add by tt, 20170321
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (61, '会议室预订', 0, NULL, 0, 999990);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (60, '嗒嗒会议室', 0, NULL, 0, 999990);

select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40400, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40410, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40420, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40430, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40440, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40450, '', 'EhNamespaces', 999990, 2);

select max(id) into @id from `eh_launch_pad_items`;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 4, 1006090, '/home', 'Bizs', 'MEETINGROOM', '嗒嗒会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', 1, 1, 49, '{"resourceTypeId":60,"pageType":0}', 3, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 4, 1006090, '/home', 'Bizs', 'MEETINGROOM', '嗒嗒会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', 1, 1, 49, '{"resourceTypeId":60,"pageType":0}', 3, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);


-- 添加新的服务类型，并更改banner跳转链接（现网已执行）, add by tt, 20170322
SET @eh_service_alliance_categories = (SELECT max(id) FROM eh_service_alliance_categories);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@eh_service_alliance_categories := @eh_service_alliance_categories + 1), 'community', '240111044331051500', '0', '十乐生活合作介绍', '十乐生活合作介绍', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999990', '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);    
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331051500', '十乐生活合作介绍', '十乐生活合作介绍', @eh_service_alliance_categories, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


SET @eh_service_alliance_skip_rule = (SELECT max(id) FROM `eh_service_alliance_skip_rule`);
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@eh_service_alliance_skip_rule := @eh_service_alliance_skip_rule + 1), '999990', @eh_service_alliance_categories);
update eh_banners set action_type = 33 where name in("十乐生活合作预告") and namespace_id = 999990;
update eh_banners set action_data = concat('{"type":',@eh_service_alliance_categories,',"parentId":',@eh_service_alliance_categories,',"displayType": "list"}') where name = "十乐生活合作预告" and namespace_id = 999990;



-- 更新现网菜单“我的申请”的data_type（现网已执行）, add by tt, 20170323
update eh_web_menus set data_type = replace(data_type, 'service', 'apply') where id in (80120, 80220, 80320, 80420, 80520);


-- 
-- 删除正中会菜单（现网已执行）, add by tt, 20170323
-- 1，删除运营服务及子菜单
-- 2，删除内部管理的岗位管理和职级管理
-- 3，系统管理下新增子菜单：管理员管理
-- 4，删除园区服务下的厂房出租、公寓出租、医疗
-- 5，删除园区服务-企业服务下的我的申请子菜单
-- 
delete from eh_acls where role_id = 1005 and privilege_id = 10145;

delete from eh_web_menu_scopes where owner_type = 'EhNamespaces' and owner_id = 999983 and menu_id in (50200, 50210, 50220, 50300, 80300, 80310, 80320, 80400, 80410,80420, 80500, 80510,80520, 80220);

select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id+1, 60400, '', 'EhNamespaces', 999983, 2);
