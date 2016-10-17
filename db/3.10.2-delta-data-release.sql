-- ∏¸–¬∫£∞∂∂´◊˘¬€Ã≥id
update eh_communities set default_forum_id = 179511, feedback_forum_id = 179512 where namespace_id = 999993 and id = 240111044331054835;
update eh_forum_posts set forum_id = 179511 where forum_id = 183102;

-- ∏¸–¬∫£∞∂∂´◊˘π‹¿Ì¥¶√˚◊÷
update eh_organizations set name = '∫£∞∂¥Ûœ√∂´◊˘ŒÔ“µ∑˛ŒÒ÷––ƒ' where id = 1004937 and namespace_id = 999993;

-- ∏¸–¬Õ£≥µ≥‰÷µ∂©µ•Œ™‘¬ø®≥‰÷µ∂©µ• 
update eh_parking_recharge_orders set recharge_type = 1;

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('351', 'parking.chuneng.url', 'http://113.108.41.29:8099', '¥¢ƒ‹Õ£≥µ≥‰÷µkey', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('352', 'parking.chuneng.key', 'F7A0B971B199FD2A52468575', '¥¢ƒ‹Õ£≥µ≥‰÷µkey', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('353', 'parking.chuneng.user', 'ktapi', '¥¢ƒ‹Õ£≥µ≥‰÷µ”√ªß√˚', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('354', 'parking.chuneng.pwd', '0306C3', '¥¢ƒ‹Õ£≥µ≥‰÷µ√‹¬Î', '0', NULL);
	
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`, `tempfee_flag`) 
	VALUES ('10004', 'community', '240111044331051500', '÷–π˙¥¢ƒ‹¥Ûœ√Õ£≥µ≥°', 'KETUO', NULL, '1', '2', '1025', '2016-08-29 17:28:10', '1', '1');

	
-- ¥¢ƒ‹Õ£≥µ≥‰÷µπ„≥°
delete from eh_launch_pad_items where id in (10604, 10622);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10604', '999990', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', 'Õ£≥µ', 'cs://1/image/aW1hZ2UvTVRwaVpUWTVNemc0Wm1Fd01qSTRNVEpqTmpsaU1tWTROVFExTW1FMU1qZ3paZw', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'pm_admin', '1');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`) 
	VALUES ('10622', '999990', '0', '0', '0', '/home', 'Bizs', 'PARKING_RECHARGE', 'Õ£≥µ', 'cs://1/image/aW1hZ2UvTVRwaVpUWTVNemc0Wm1Fd01qSTRNVEpqTmpsaU1tWTROVFExTW1FMU1qZ3paZw', '1', '1', '30', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '0', 'park_tourist', '1');

-- Õ£≥µ≥‰÷µ≤Àµ•

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41000,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41100,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41300,'', 'EhNamespaces', 999990,2);
INSERT INTO `eh_web_menu_scopes`(`id`, `menu_id`,`menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
	VALUES((@menu_scope_id := @menu_scope_id + 1),41400,'', 'EhNamespaces', 999990,2);

UPDATE `eh_web_menu_privileges` SET `privilege_id`='556', `menu_id`='41100', `name`='≥‰÷µœÓπ‹¿Ì¡–±Ì',  `discription`='≤È—Ø≥‰÷µœÓπ‹¿Ì¡–±Ì' WHERE (`id`='17');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='512', `menu_id`='41100', `name`='‘ˆº”Õ£≥µ≥‰÷µœÓ',  `discription`='‘ˆº”Õ£≥µ≥‰÷µœÓ' WHERE (`id`='18');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='513', `menu_id`='41100', `name`='…æ≥˝Õ£≥µ≥‰÷µœÓ',  `discription`='…æ≥˝Õ£≥µ≥‰÷µœÓ' WHERE (`id`='19');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='557', `menu_id`='41200', `name`='…Ë÷√ªÓ∂ØπÊ‘Ú',  `discription`='…Ë÷√ªÓ∂ØπÊ‘Ú' WHERE (`id`='20');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='515', `menu_id`='41400', `name`='Ω…∑—º«¬º',  `discription`='≤È—ØΩ…∑—º«¬º' WHERE (`id`='21');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='516', `menu_id`='41300', `name`='‘¬ø®…Í«Îº«¬º',  `discription`='≤È—Ø‘¬ø®…Í«Îº«¬º' WHERE (`id`='22');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='514', `menu_id`='41300', `name`='…Ë÷√‘¬ø®…Í«Î≤Œ ˝',  `discription`='…Ë÷√‘¬ø®…Í«Î≤Œ ˝' WHERE (`id`='23');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='517', `menu_id`='41300', `name`='∑¢∑≈‘¬ø®',  `discription`='∑¢∑≈‘¬ø®' WHERE (`id`='24');
UPDATE `eh_web_menu_privileges` SET `privilege_id`='518', `menu_id`='41300', `name`='¡Ï»°‘¬ø®',  `discription`='¡Ï»°‘¬ø®' WHERE (`id`='25');

UPDATE `eh_web_menus` SET `name`='Õ£≥µΩ…∑—' WHERE (`id`='41000');

UPDATE `eh_web_menus` SET `name`='≥‰÷µœÓπ‹¿Ì' WHERE (`id`='41100');
UPDATE `eh_web_menus` SET `name`='ªÓ∂ØπÊ‘Ú' WHERE (`id`='41200');
UPDATE `eh_web_menus` SET `name`='‘¬ø®…Í«Î' WHERE (`id`='41300');
UPDATE `eh_web_menus` SET `name`='Ω…∑—º«¬º' WHERE (`id`='41400');

DELETE from eh_web_menu_scopes where menu_id = 41200;

UPDATE `eh_acl_privileges` SET `name`='‘ˆº”Õ£≥µ≥‰÷µœÓ', `description`='‘ˆº”Õ£≥µ≥‰÷µœÓ' WHERE (`id`='512');
UPDATE `eh_acl_privileges` SET `name`='…æ≥˝Õ£≥µ≥‰÷µœÓ', `description`='…æ≥˝Õ£≥µ≥‰÷µœÓ' WHERE (`id`='513');
UPDATE `eh_acl_privileges` SET `name`='…Ë÷√‘¬ø®…Í«Î≤Œ ˝', `description`='…Ë÷√‘¬ø®…Í«Î≤Œ ˝' WHERE (`id`='514');
UPDATE `eh_acl_privileges` SET `name`='≤È—ØΩ…∑—º«¬º', `description`='≤È—ØΩ…∑—º«¬º' WHERE (`id`='515');
UPDATE `eh_acl_privileges` SET `name`='≤È—Ø‘¬ø®…Í«Îº«¬º', `description`='≤È—Ø‘¬ø®…Í«Îº«¬º' WHERE (`id`='516');
UPDATE `eh_acl_privileges` SET `name`='∑¢∑≈‘¬ø®', `description`='∑¢∑≈‘¬ø®' WHERE (`id`='517');
UPDATE `eh_acl_privileges` SET `name`='¡Ï»°‘¬ø®', `description`='¡Ï»°‘¬ø®' WHERE (`id`='518');
UPDATE `eh_acl_privileges` SET `name`='≤È—Ø≥‰÷µœÓπ‹¿Ì¡–±Ì', `description`='≤È—Ø≥‰÷µœÓπ‹¿Ì¡–±Ì' WHERE (`id`='556');
UPDATE `eh_acl_privileges` SET `name`='…Ë÷√ªÓ∂ØπÊ‘Ú', `description`='…Ë÷√ªÓ∂ØπÊ‘Ú' WHERE (`id`='557');

-- À—À˜¿‡–Õ≈‰÷√ by xiongying20161017

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES (1, '0', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES (2, '0', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES (3, '0', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);

SET @search_type_id = (SELECT MAX(id) FROM `eh_search_types`);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '1000000', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '1000000', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '1000000', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999992', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999992', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999992', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999991', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999991', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999991', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999989', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999989', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999989', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999989', '', '0', 'øÏ—∂', 'news', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999990', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999990', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999990', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999993', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999993', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999993', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999999', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999999', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999999', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999988', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999988', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999988', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999987', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999987', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999987', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999987', '', '0', 'øÏ—∂', 'news', '1', NULL, NULL);

INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999986', '', '0', 'Õ∂∆±', 'poll', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999986', '', '0', 'ªÓ∂Ø', 'activity', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999986', '', '0', 'ª∞Ã‚', 'topic', '1', NULL, NULL);
INSERT INTO `eh_search_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `content_type`, `status`, `create_time`, `delete_time`) VALUES ((@search_type_id := @search_type_id + 1), '999986', '', '0', 'øÏ—∂', 'news', '1', NULL, NULL);

-- ø∆ºº‘∞ÃÌº”◊Û¡⁄ª·“È “
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) 
	VALUES('50','◊Û¡⁄ª·“È “','0',NULL,'0','1000000');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (112372, '1000000', '0', '4', '178945', '/home', 'Bizs', 'MEETINGROOM', '◊Û¡⁄ª·“È “', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', '1', '1', '49', '{\"resourceTypeId\":50,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0');
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`)
	VALUES (112373, '1000000', '0', '4', '178945', '/home', 'Bizs', 'MEETINGROOM', '◊Û¡⁄ª·“È “', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', '1', '1', '49', '{\"resourceTypeId\":50,\"pageType\":0}', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0');

-- Ë∏¢Âá∫ÊèêÁ§∫ by janson 20161017
UPDATE `eh_locale_strings` SET `text`='‰Ω†ÁöÑË¥¶Âè∑Â∑≤Âú®Âè¶‰∏ÄÂè∞ËÆæÂ§áÁôªÂΩïÔºå‰Ω†Ë¢´Ëø´‰∏ãÁ∫øÔºåËã•ÈùûÊú¨‰∫∫Êìç‰ΩúÔºåËØ∑Á´ãÂç≥‰øÆÊîπÂØÜÁ†Å„ÄÇ' WHERE `scope`='user' AND `code`='100018' AND `locale`='zh_CN';


