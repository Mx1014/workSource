-- by st.zheng 一卡通
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'pay.v2.callback.url.paymentCard', '/payment/payNotify', '一卡通新支付回调接口', '0', NULL, NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'pay.v2.refund.url.paymentCard', '/payment/refundNotify', '一卡通新支付退款回调接口', '0', NULL, NULL);
INSERT INTO `eh_payment_card_issuers` (`id`, `name`, `description`, `pay_url`, `alipay_recharge_account`, `weixin_recharge_account`, `vendor_name`,  `create_time`, `status`) VALUES ('2', 'zhuzong', 'zhuzong', 'adfasdf', 'adfasdf', 'asdfsdfasdf', 'ZHUZONG',now(), '1');
INSERT INTO `eh_payment_card_issuer_communities` (`id`, `owner_type`, `owner_id`, `issuer_id`, `hotline`, `create_time`) VALUES ('100002', 'community', '240111044332060075', '2', NULL, now());

update eh_service_module_privileges set remark = '用户管理权限' where privilege_id = 4120041210;
update eh_service_module_privileges set module_id = 41210 where privilege_id = 4120041220;
update eh_service_module_privileges set module_id = 41230 where privilege_id = 4120041240;
update eh_service_modules set name = '用户管理' where id = 41210;
update eh_service_modules set name = '交易记录' where id = 41230;
delete from eh_service_modules where id in (41220,41240);

INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'paymentCard.zhuzong.url', '111.207.114.167:9010', '住总一卡通地址', '0', NULL, NULL);

update `eh_payment_cards` set update_time = create_time where update_time is null;
