-- 手机号校验正则  add by xq.tian  2018/01/31
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.strictPhoneRegex', '^(?:13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\\d{8}$', '手机号正则', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.testPhoneRegex', '^(?:1[0125])\\d{9}$', '测试手机号正则', 0, NULL);
