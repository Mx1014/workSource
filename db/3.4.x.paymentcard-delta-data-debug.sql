INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (105, 'taotaogu.keystore', 'taotaogu.keystore', 'the keystore for taotaogu(chuneng)', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (106, 'taotaogu.pin3.crt', 'taotaogu.pin3.crt', 'the pin3.crt for taotaogu(chuneng)', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (107, 'taotaogu.server.cer', 'taotaogu.server.cer', 'the server.cer for taotaogu(chuneng)', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (108, 'taotaogu.client.pfx', 'taotaogu.client.pfx', 'the client.pfx for taotaogu(chuneng)', 0, NULL);

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (109, 'taotaogu.card.url', 'http://test.ippit.cn:30821/iccard/service', 'the card url for taotaogu(chuneng)', 0, NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
    VALUES (110, 'taotaogu.order.url', 'http://test.ippit.cn:8010/orderform', 'the order url for taotaogu(chuneng)', 0, NULL);

INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (198, 'paymentCard', '10000', 'zh_CN', '服务器通讯失败，请检查网络连接并重试！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (199, 'paymentCard', '10001', 'zh_CN', '您输的旧密码有误，请重新输入！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (200, 'paymentCard', '10002', 'zh_CN', '验证码错误，请重新输入！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (201, 'paymentCard', '10003', 'zh_CN', '当前卡不存在！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) 
    VALUES (202, 'paymentCard', '10004', 'zh_CN', '二维码获取失败！');

INSERT INTO `eh_payment_card_issuer_communities` (`id`, `owner_type`, `owner_id`, `issuer_id`, `create_time`) 
    VALUES (100001, 'community', '240111044331051500', 100001, '2016-06-14 17:06:44');

INSERT INTO `eh_payment_card_issuers` (`id`, `name`, `description`, `pay_url`, `alipay_recharge_account`, `weixin_recharge_account`, `vendor_name`, `vendor_data`, `create_time`, `status`) 
    VALUES (100001, 'chuneng', 'chuneng', 'adfasdf', 'adfasdf', 'asdfsdfasdf', 'TAOTAOGU', '{"branchCode":"10002900","appName":"ICCard","version":"V0.01","dstId":"00000000","cardPatternid":"887093","chnlType":"WEB","chnlId":"12345679","merchId":"862900000000001","termnlId":"00011071","initPassword":"111111"}', '2016-06-14 17:07:20', '1');






