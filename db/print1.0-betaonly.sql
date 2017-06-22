-- 以下sql只在beta做测试使用
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('beta.print.order.amount', 'true', '用于支付测试', '0', NULL);
UPDATE `eh_configurations` SET `value`='http://printtest.zuolin.com/evh/siyinprint/informPrint?identifierToken=', `description`='二维码url地址', `namespace_id`='0' WHERE `name`='print.inform.url';
