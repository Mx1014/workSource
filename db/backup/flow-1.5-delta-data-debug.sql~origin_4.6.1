-- 工作流字符串模板 add by xq.tian  2017/06/05
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('flow', 10010, 'zh_CN', '工作流多条评价时显示的摘要', '用户已评价，查看详情', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('flow', 10011, 'zh_CN', '工作流带评价内容时显示的摘要', '用户评价：${score}分，${content}', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('flow', 10012, 'zh_CN', '非发起人附言上传图片', '${processorName}：上传了${imageCount}张图片', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('flow', 10013, 'zh_CN', '发起人附言上传图片', '发起人：上传了${imageCount}张图片', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('flow', 10014, 'zh_CN', '非发起人附言', '${processorName}：${content}', 0);
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ('flow', 10015, 'zh_CN', '发起人附言', '发起人：${content}', 0);

UPDATE `eh_locale_templates` SET `text` = '任务已被${processorName}驳回' WHERE `scope` = 'flow' AND `code` = 10002;

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('flow', '10011', 'zh_CN', '评价项数量需在1-5个之间');

-- 把任务管理菜单放到内部管理下   add by xq.tian  2017/06/05
DELETE FROM `eh_web_menus` WHERE `id` IN (70000, 70100, 70200);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
  VALUES (70000, '任务管理', 50000, 'fa fa-group', NULL, 1, 2, '/50000/70000', 'park', 600, 70000);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
  VALUES (70100, '任务汇总', 70000, NULL, 'react:/task-management/task-list/70100', 0, 2, '/50000/70000/70100', 'park', 610, 70000);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
  VALUES (70200, '业务授权视图', 70000, NULL, 'flow_view', 0, 2, '/50000/70000/70200', 'park', 620, 70000);

-- 给普通公司加上任务管理菜单  add by xq.tian  2017/06/05
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10078, 1005, 'EhAclRoles', 0, 1, NOW());

-- 给普通公司场景加任务管理icon   add by xq.tian  2017/06/05
SET @launch_pad_item_id = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`)
  VALUES ((@launch_pad_item_id := @launch_pad_item_id + 1), 1000000, 0, 0, 0, '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRwa1ltWmlPRGN5TTJObFlUYzFNV1l3WmpZNU9UVmlZMlEyTkdRd1pUUmxNUQ', 1, 1, 56, '', 13, 0, 1, 1, NULL, 0, NULL, NULL, NULL, 0, 'park_tourist', 0, NULL, NULL, 0, NULL);
