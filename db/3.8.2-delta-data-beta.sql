-- 升级使用的url
INSERT INTO `eh_configurations` VALUES (141, 'upgrade.url', '/management/views/upgrade.html', 'upgrade url', 0, NULL);


-- 物业报修2.0 
SET @eh_launch_pad_items_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);

INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999991, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://10.1.10.102/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'park_tourist');    
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`) 
  VALUES ((@eh_launch_pad_items_id := @eh_launch_pad_items_id + 1), 999991, 0, 0, 0, '/home', 'Bizs', '物业报修2.0', '物业报修2.0', 'cs://1/image/aW1hZ2UvTVRwak5ETXlaakJsWkRJNFlqQmhabVJpTmpnd1lXSTFOelF4TjJGbU0yUXpOQQ', 1, 1, 14, '{"url":"http://10.1.10.102/property_service/index.html?hideNavigationBar=1#/my_service#sign_suffix"}', 0, 0, 1, 1,'','0',NULL,NULL,NULL, '1', 'pm_admin');  

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('331', '0', '分派任务', '分派任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('332', '0', '完成任务', '完成任务', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('333', '0', '关闭任务', '关闭任务', NULL);

INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) VALUES ('760', 'EhOrganizations', NULL, '1', '331', '1001', '0', '1', '2016-08-05 13:29:01');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) VALUES ('761', 'EhOrganizations', NULL, '1', '332', '1001', '0', '1', '2016-08-05 13:29:01');
INSERT INTO `eh_acls` (`id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `order_seq`, `creator_uid`, `create_time`) VALUES ('762', 'EhOrganizations', NULL, '1', '333', '1001', '0', '1', '2016-08-05 13:29:01');

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('122', 'pmtask.category.ancestor', '任务', '任务分类名称', '0', NULL);

INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('152', 'pmtask.notification', '1', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone}', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('153', 'pmtask.notification', '2', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone} 已将任务分配给了 ${targetName} ${targetPhone}，将会很快联系您。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('154', 'pmtask.notification', '3', 'zh_CN', '任务操作模版', '${operatorName} ${operatorPhone} 已完成该单，稍后我们将进行回访。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ('155', 'pmtask.notification', '4', 'zh_CN', '任务操作模版', '您的任务已被 ${operatorName} ${operatorPhone} 关闭', '0');
