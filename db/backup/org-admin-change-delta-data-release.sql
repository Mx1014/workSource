-- 企业管理员变更消息模板  add by xq.tian  2017/11/1
SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 1);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'organization.notification', 18, 'zh_CN', '添加企业管理员给其他管理员发送的消息模板', '${userName}（${contactToken}）已被添加为${organizationName}的企业管理员。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'organization.notification', 19, 'zh_CN', '添加企业管理员给当前管理员发送的消息模板', '您已被添加为${organizationName}的企业管理员。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'organization.notification', 20, 'zh_CN', '删除企业管理员给其他管理员发送的消息模板', '${userName}（${contactToken}）的${organizationName}企业管理员身份已被移除。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'organization.notification', 21, 'zh_CN', '删除企业管理员给当前管理员发送的消息模板', '您的${organizationName}企业管理员身份已被移除。', 0);
