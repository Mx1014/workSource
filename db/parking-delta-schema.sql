ALTER TABLE eh_parking_lots ADD COLUMN `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: monthly, 2: temporary';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `order_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from 3rd system';

INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('249', 'parking.chuneng.url', 'http://220.160.111.114:9099', '储能停车充值key', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('250', 'parking.chuneng.key', 'F7A0B971B199FD2A1017CEC5', '储能停车充值key', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('251', 'parking.chuneng.user', 'ktapi', '储能停车充值用户名', '0', NULL);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('252', 'parking.chuneng.pwd', '0306A9', '储能停车充值密码', '0', NULL);
	
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `card_reserve_days`, `status`, `creator_uid`, `create_time`, `max_request_num`) 
	VALUES ('10004', 'community', '240111044331051500', '中国储能大厦停车场', 'KETUO', NULL, '1', '2', '1025', '2016-08-29 17:28:10', '1');

