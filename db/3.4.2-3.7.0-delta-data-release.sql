-- merge from 3.4.x.paymentcard-delta-data-release.sql 20160628
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
    VALUES (100001, 'chuneng', 'chuneng', 'adfasdf', 'adfasdf', 'asdfsdfasdf', 'TAOTAOGU', '{\"BranchCode\":\"10002900\",\"AppName\":\"ICCard\",\"Version\":\"V0.01\",\"DstId\":\"00000000\",\"CardPatternid\":\"887093\",\"chnl_type\":\"WEB\",\"chnl_id\":\"12345679\",\"merch_id\":\"862900000000001\",\"termnl_id\":\"00011071\",\"init_password\":\"111111\"}', '2016-06-14 17:07:20', '1');

	
	
	
-- merge from 3.4.x.sfyan-delta-data-release.sql 20160628
-- INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) 
--     VALUES( 'user.notification', 2, 'zh_CN', '注册天数描述', '我已加入左邻“${days}”天', 0);

-- INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'hottag', '10001', 'zh_CN', '该标签已是热门标签');

-- merge from 3.4.x.sfyan-delta-data-release.sql 20160630 update
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`, `namespace_id`) 
    VALUES( 'user.notification', 2, 'zh_CN', '注册天数描述', '我已加入Ibase“${days}”天', 999989);

INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'activity.share.url', '/mobile/static/share_activity/index.html', 'the relative path for sharing activity');
INSERT INTO `eh_configurations` (`namespace_id`, `name`, `value`, `description`) VALUES (0, 'activity.poster.default.url', 'cs://1/image/aW1hZ2UvTVRwaE1EVmhPVGM0WTJFM1lUSTRaREpqWkRsa1l6VmtNakE1TUdVM01UWTVPUQ', '默认活动封面');
INSERT INTO `eh_locale_strings`(`scope`, `code`,`locale`, `text`) VALUES( 'hottag', '10001', 'zh_CN', '该标签已是热门标签');


INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('1', '0', '左邻', '1', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.yjtc.everhomes', 'cs://1/image/aW1hZ2UvTVRvMk0yRTVPR1JsTWpsaFkyRmxOMlZoWm1NME5EZGlPVGxtTjJFd01UQm1NUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('2', '1000000', '深圳科技园', '1', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.park.tec_park', 'cs://1/image/aW1hZ2UvTVRvMFpUSTVZak15TWpBd056VTJNRGd3T1RCak1XVXlNV013T0Rnd09UaGpOdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('3', '999994', '龙岗智慧社区', '1', '', 'cs://1/image/aW1hZ2UvTVRvMk5UQXdaR013TlRZeE5qVmlOakl6WlRRNE9UazBPRGRoTldFek0yUTFZZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('4', '999993', '海岸馨服务', '1', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.haian.park', 'cs://1/image/aW1hZ2UvTVRwa09HVTNOekpoTVRkak5EYzNZVGd3TTJKbVl6VTFPR0kyTVdZd01qZzRNZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('5', '999992', '深业物业', '1', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.shenye', 'cs://1/image/aW1hZ2UvTVRvMk56QXhNV1JtT0RZeE1XTm1ZamxsWW1JMVpHWmxaak14TVRJM01XUXdNdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('6', '999999', '中洲智邦', '1', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.park.xmtec', 'cs://1/image/aW1hZ2UvTVRvME9EaGhZMlpqTkRZd1pUSXlNR000WmpVNFlUWTFORGM1TTJZeE5EYzROZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('7', '999996', '邻里+', '1', '', 'cs://1/image/aW1hZ2UvTVRvMU9HUTJNRGt3TXpFME9HUTFZelZrWldOaE9ESTVaamswTVRsbU4yUTFOZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('8', '999990', 'ufine', '1', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.chuneng.park', 'cs://1/image/aW1hZ2UvTVRvNE1URXhPREkyTXpaaE5HSm1aR1V5TmpSaE5EVXdOekUxWW1NMU5qSmxOUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('9', '0', '左邻', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.yjtc.everhomes', 'cs://1/image/aW1hZ2UvTVRvMk0yRTVPR1JsTWpsaFkyRmxOMlZoWm1NME5EZGlPVGxtTjJFd01UQm1NUQ', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('10', '1000000', '深圳科技园', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.park.tec_park', 'cs://1/image/aW1hZ2UvTVRvMFpUSTVZak15TWpBd056VTJNRGd3T1RCak1XVXlNV013T0Rnd09UaGpOdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('11', '999994', '龙岗智慧社区', '2', '', 'cs://1/image/aW1hZ2UvTVRvMk5UQXdaR013TlRZeE5qVmlOakl6WlRRNE9UazBPRGRoTldFek0yUTFZZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('12', '999993', '海岸馨服务', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.haian.park', 'cs://1/image/aW1hZ2UvTVRwa09HVTNOekpoTVRkak5EYzNZVGd3TTJKbVl6VTFPR0kyTVdZd01qZzRNZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('13', '999992', '深业物业', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.shenye', 'cs://1/image/aW1hZ2UvTVRvMk56QXhNV1JtT0RZeE1XTm1ZamxsWW1JMVpHWmxaak14TVRJM01XUXdNdw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('14', '999999', '中洲智邦', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.park.xmtec', 'cs://1/image/aW1hZ2UvTVRvME9EaGhZMlpqTkRZd1pUSXlNR000WmpVNFlUWTFORGM1TTJZeE5EYzROZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('15', '999996', '邻里+', '2', '', 'cs://1/image/aW1hZ2UvTVRvMU9HUTJNRGt3TXpFME9HUTFZelZrWldOaE9ESTVaamswTVRsbU4yUTFOZw', '移动平台聚合服务，助力园区效能提升');
INSERT INTO `eh_app_urls` (`id`, `namespace_id`, `name`, `os_type`, `download_url`, `logo_url`, `description`) 
    VALUES ('16', '999990', 'ufine', '2', 'http://a.app.qq.com/o/simple.jsp?pkgname=com.everhomes.android.chuneng.park', 'cs://1/image/aW1hZ2UvTVRvNE1URXhPREkyTXpaaE5HSm1aR1V5TmpSaE5EVXdOekUxWW1NMU5qSmxOUQ', '移动平台聚合服务，助力园区效能提升');
