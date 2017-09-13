-- beta 园区快讯
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wx.offical.account.appid', 'wxda4ca555d76459c1', 'IBase左邻测试公众号开发者AppId', 999989, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wx.offical.account.secret', 'fdd5c93c0e89602aefa54817717d0da8', 'IBase左邻测试公众号开发者AppId', 999989, NULL);

-- beta 服务联盟
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wx.offical.account.appid', 'wxe408ab29107aee4f', '华润邓爽测试公众号开发者AppId', 999985, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ((@eh_configurations_id := @eh_configurations_id + 1), 'wx.offical.account.secret', 'ec8a63f57e64b723cefaa254de3f7d78', '华润邓爽测试公众号开发者AppId', 999985, NULL);
