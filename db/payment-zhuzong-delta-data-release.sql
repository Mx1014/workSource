-- by st.zheng 一卡通
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'pay.v2.callback.url.paymentCard', '/payment/payNotify', '一卡通新支付回调接口', '0', NULL, NULL);
INSERT INTO `eh_payment_card_issuers` (`id`, `name`, `description`, `pay_url`, `alipay_recharge_account`, `weixin_recharge_account`, `vendor_name`,  `create_time`, `status`) VALUES ('2', 'zhuzong', 'zhuzong', 'adfasdf', 'adfasdf', 'asdfsdfasdf', 'ZHUZONG',now(), '1');
INSERT INTO `eh_payment_card_issuer_communities` (`id`, `owner_type`, `owner_id`, `issuer_id`, `hotline`, `create_time`) VALUES ('100002', 'community', '240111044332060075', '2', NULL, now());
