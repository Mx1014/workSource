--
-- 短信发送次数校验配置   add by xq.tian  2017/02/28
--
SELECT max(id) FROM eh_configurations INTO @max_id;
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.minDuration.seconds', '60', '注册时最小短信间隔时间', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.device.timesForAnHour', '10', '每个设备每小时发送短信最大次数', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.device.timesForADay', '20', '每个设备每天发送短信最大次数', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.phone.timesForAnHour', '3', '每个手机号每小时发送短信最大次数', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES (@max_id := @max_id + 1, 'sms.verify.phone.timesForADay', '5', '每个手机号每天发送短信最大次数', 0, NULL);

SELECT max(id) FROM eh_locale_strings INTO @max_id;
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES (@max_id := @max_id + 1, 'user', '300001', 'zh_CN', '发送验证码时间不得小于60s');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES (@max_id := @max_id + 1, 'user', '300002', 'zh_CN', '验证码请求过于频繁，请1小时后重试');

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`)
VALUES (@max_id := @max_id + 1, 'user', '300003', 'zh_CN', '验证码请求过于频繁，请明天重试');

UPDATE `eh_locale_strings` SET `text` = '签名过期，请重新获取' WHERE `scope` = 'user' AND `code` = '10000' AND `locale` = 'zh_CN';
