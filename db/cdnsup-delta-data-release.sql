SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'content.url.vendor', 'AliCDN', '资源URL解析器, 注意：这里的配置改了，对应的contentServer的配置也要改', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'content.alicdn.privateKey', 'ZUOLINCDNKEY', 'AliCDN URL鉴权 privateKey', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'content.alicdn.domain', 'cdn.tianxq.zuolin.com', 'AliCDN 域名', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'content.alicdn.expireSeconds', '60', 'AliCDN 资源链接过期时间', 0, NULL);
