
-- 活动公众号报名使用新的页面，并且增加默认公众号。生成公众号支付的订单接口   add by yanjun 20170620
UPDATE eh_configurations SET VALUE = '/share-activity/build/index.html#/detail' WHERE NAME = 'activity.share.url';
SET @eh_configurations_id = (SELECT MAX(id) FROM `eh_configurations`);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.offical.account.appid','wx5db5ebf00a251407','默认公众号开发者AppId-左邻平台','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'wx.offical.account.secret','cc0a483e3f50a14ed795d7ebea947f4c','默认公众号开发者AppId-左邻平台','0',NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'pay.zuolin.wechatJs','POST /EDS_PAY/rest/pay_common/payInfo_record/createWechatJsPayOrder','生成公众号订单的api','0',NULL);

-- 生成公众号订单异常信息
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES((@eh_configurations_id := @eh_configurations_id + 1),'activity','10026','zh_CN','生成公众号订单异常');