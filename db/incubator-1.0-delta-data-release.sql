
-- 微信绑定手机页面url   add by yanjun 20170901
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.bind.phone.url','wx5db5ebf00a251407','微信用户绑定手机页面','0',NULL);

