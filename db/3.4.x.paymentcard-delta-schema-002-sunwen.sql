
ALTER TABLE `eh_payment_card_transactions` ADD COLUMN `comsume_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'the type of consume';
ALTER TABLE `eh_payment_card_transactions` ADD COLUMN `token` VARCHAR(512) NOT NULL COMMENT 'the token of card token to pay';
ALTER TABLE `eh_payment_card_transactions` ADD COLUMN `card_no` VARCHAR(256) NOT NULL COMMENT 'the number of card';
ALTER TABLE `eh_payment_card_transactions` ADD COLUMN `order_no` BIGINT(30) NOT NULL DEFAULT '0' COMMENT 'order no';


INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES 
('91', 'taotaogu.keystore', 'taotaogu.keystore', 'the keystore for taotaogu(chuneng)', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES 
('92', 'taotaogu.pin3.crt', 'taotaogu.pin3.crt', 'the pin3.crt for taotaogu(chuneng)', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES 
('93', 'taotaogu.server.cer', 'taotaogu.server.cer', 'the server.cer for taotaogu(chuneng)', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES 
('94', 'taotaogu.client.pfx', 'taotaogu.client.pfx', 'the client.pfx for taotaogu(chuneng)', '0', NULL);


INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
('178', 'paymentCard', '10000', 'zh_CN', '服务器通讯失败，请检查网络连接并重试！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
('179', 'paymentCard', '10001', 'zh_CN', '您输的旧密码有误，请重新输入！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
('180', 'paymentCard', '10002', 'zh_CN', '验证码错误，请重新输入！');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES 
('181', 'paymentCard', '10003', 'zh_CN', '当前卡不存在！');




