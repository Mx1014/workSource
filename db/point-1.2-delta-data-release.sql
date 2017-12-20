-- 积分使用消息
SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'point', 10000, 'zh_CN', '您的积分将于12月31日过期，请及时使用。', '您的积分将于12月31日过期，请及时使用。', 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
  VALUES ((@locale_templates_id := @locale_templates_id + 1), 'point', 10001, 'zh_CN', '积分通用的模板', '{"messageTitle":"积分消息","resetPointDesc":"积分重置","resetPointCate":"系统"}', 0);
