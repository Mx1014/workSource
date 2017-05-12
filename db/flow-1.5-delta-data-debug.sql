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

-- 把任务管理菜单放到内部管理下
DELETE FROM `eh_web_menus` WHERE `id` IN (70000, 70100, 70200);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
  VALUES (70000, '任务管理', 50000, 'fa fa-group', NULL, 1, 2, '/50000/70000', 'park', 600, 70000);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
  VALUES (70100, '任务列表', 70000, NULL, 'react:/task-management/task-list/70100', 0, 2, '/50000/70000/70100', 'park', 610, 70000);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`)
  VALUES (70200, '业务授权视图', 70000, NULL, 'flow_view', 0, 2, '/50000/70000/70200', 'park', 620, 70000);

-- 给普通公司加上任务管理菜单
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, 10078, 1005, 'EhAclRoles', 0, 1, NOW());