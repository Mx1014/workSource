-- version 1 的手动复制出来
SET @conf_id := (SELECT MAX(id) FROM eh_uniongroup_configures);
SET @member_id := (SELECT MAX(id) FROM eh_uniongroup_configures);
;
INSERT INTO eh_uniongroup_configures 

SELECT
  @conf_id + id id,
  `namespace_id`,
  `enterprise_id`,
  `group_type`,
  `group_id`,
  `current_id`,
  `current_type`,
  `current_name`,
  `operator_uid`,
  `update_time`,
  1 version_code
FROM `eh_uniongroup_configures`
 ;
 INSERT INTO eh_uniongroup_member_details 
 SELECT
  @member_id  + `id` id,
  `namespace_id`,
  `group_type`,
  `group_id`,
  `detail_id`,
  `target_type`,
  `target_id`,
  `enterprise_id`,
  `contact_name`,
  `contact_token`,
  `update_time`,
  `operator_uid`,
  1 version_code
FROM `eh_uniongroup_member_details`;

-- debug 的入口
SET @homeurl = (SELECT `value` FROM eh_configurations WHERE `name` = 'home.url' LIMIT 1);
SET @item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
SET @namespace_id = 1000000;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) VALUES ((@item_id:=@item_id+1), @namespace_id, '0', '0', '0', '/home', 'Bizs', '问卷调查', '问卷调查', '', '1', '1', '13', CONCAT('{\"url\":\"',@homeurl,'/questionnaire-survey/build/index.html#/home#sign_suffix\"}'), '3', '0', '1', '1', '1', '0', NULL, NULL, NULL, '0', 'pm_admin', '1', NULL, NULL, '0', NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) VALUES ((@item_id:=@item_id+1), @namespace_id, '0', '0', '0', '/home', 'Bizs', '问卷调查', '问卷调查', '', '1', '1', '13', CONCAT('{\"url\":\"',@homeurl,'/questionnaire-survey/build/index.html#/home#sign_suffix\"}'), '3', '0', '1', '1', '1', '0', NULL, NULL, NULL, '0', 'park_tourist', '1', NULL, NULL, '0', NULL, NULL);
