ALTER TABLE eh_parking_lots ADD COLUMN `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card';

ALTER TABLE eh_parking_lots ADD COLUMN `tempfee_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is support temp fee';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: monthly, 2: temporary';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `order_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from 3rd system';

