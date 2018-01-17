SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);   
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@string_id := @string_id + 1), 'address', '20007', 'zh_CN', '建筑面积只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@string_id := @string_id + 1), 'address', '20008', 'zh_CN', '公摊面积只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@string_id := @string_id + 1), 'address', '20009', 'zh_CN', '收费面积只能为数字');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@string_id := @string_id + 1), 'address', '20010', 'zh_CN', '出租面积只能为数字');

-- 短信签名 add by xq.tian  2018/01/17
UPDATE eh_locale_templates SET `text` = '【力合科服】' WHERE scope = 'sms.default.sign' AND namespace_id = 999984;
UPDATE eh_locale_templates SET `text` = '【Officeasy】' WHERE scope = 'sms.default.sign' AND namespace_id = 999985;

-- 短信配置 add by xq.tian  2018/01/17
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.handlerResolverName', 'ROLL_POLLING', '短信handler解析器', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.JianMi.accountName', '4629', '茧米云账号', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.JianMi.password', 'yjtc7777', '茧米云密码', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.JianMi.server', 'http://sms.53api.com/sdk/SMS', '茧米云server', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.YunPian.apiKey', 'a010af7f5e94f889c010ed38a7fc3a05', '云片ApiKey', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.YunPian.server', 'https://sms.yunpian.com/v2/sms/batch_send.json', '云片server', 0, NULL);
