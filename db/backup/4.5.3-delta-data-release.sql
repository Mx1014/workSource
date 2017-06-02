-- 解决邮箱验证不通过的问题，bug号#10051，吕磊
UPDATE eh_configurations set `value` = 'webmail.zuolin.com' WHERE `name` = 'mail.smtp.address' and `namespace_id` = 1000000;
UPDATE eh_configurations set `value` = '465' WHERE `name` = 'mail.smtp.port' and `namespace_id` = 1000000;
