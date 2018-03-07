-- 手机号校验正则  add by xq.tian  2018/01/31
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.strictPhoneRegex', '^(?:13[0-9]|14[579]|15[0-3,5-9]|17[0135678]|18[0-9])\\d{8}$', '手机号正则', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'sms.testPhoneRegex', '^(?:1[012])\\d{9}$', '测试手机号正则', 0, NULL);

-- 滞纳金变量 by wentian
SET @var_id = (SELECT max(`id`) from `eh_payment_variables`);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@var_id:=@var_id+1, NULL, '6', '欠费', '0', '2017-10-16 09:31:00', NULL, '2017-10-16 09:31:00', 'qf');

-- by dengs,新闻分享连接修改

update eh_configurations SET `value`='/park-news-web/build/index.html?ns=%s&isFS=1&widget=News&timeWidgetStyle=time/#/newsDetail?newsToken=%s' WHERE `name` in ('news.web.url','news.url');


-- by xiongying 加上对象名
update eh_var_field_groups set name = 'com.everhomes.customer.CustomerTax' where title = '税务信息';
update eh_var_field_groups set name = 'com.everhomes.customer.CustomerAccount' where title = '银行账号';