
-- 活动公众号报名使用新的页面，并且增加默认公众号   add by yanjun 20170620
UPDATE eh_configurations SET VALUE = '/share-activity/build/index.html#/detail' WHERE NAME = 'activity.share.url';
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.offical.account.default.appid','wxc9793912e431c111','默认测试公众号开发者AppId','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.offical.account.default.secret','c39f9f48b56d419984a98d3acd26ead4','默认测试公众号开发者AppId','0',NULL);
