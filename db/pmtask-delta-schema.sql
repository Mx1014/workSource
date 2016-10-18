ALTER TABLE eh_pm_tasks ADD COLUMN `task_category_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'task category id';
ALTER TABLE eh_pm_tasks ADD COLUMN `reserve_time` DATETIME;
ALTER TABLE eh_pm_tasks ADD COLUMN `priority` TINYINT NOT NULL DEFAULT 0 COMMENT 'task rank of request';
ALTER TABLE eh_pm_tasks ADD COLUMN `source_type` TINYINT NOT NULL DEFAULT 0 COMMENT 'task come from ,such as app ,email';
ALTER TABLE eh_pm_tasks ADD COLUMN `organization_id` BIGINT NOT NULL DEFAULT 0;
ALTER TABLE eh_pm_tasks ADD COLUMN `requestor_name` VARCHAR(64) COMMENT 'the name of requestor';
ALTER TABLE eh_pm_tasks ADD COLUMN `requestor_phone` VARCHAR(64) COMMENT 'the phone of requestor';
ALTER TABLE eh_pm_tasks ADD COLUMN `address_id` BIGINT NOT NULL DEFAULT 0 COMMENT 'address id';
ALTER TABLE eh_pm_tasks ADD COLUMN `revisit_status` TINYINT NOT NULL DEFAULT 0 COMMENT '0: not 1: callbacked ';
ALTER TABLE eh_pm_tasks ADD COLUMN `revisit_content` TEXT COMMENT 'revisit content';


ALTER TABLE eh_parking_lots ADD COLUMN `max_request_num` INTEGER NOT NULL DEFAULT 1 COMMENT 'the max num of the request card';

ALTER TABLE eh_parking_lots ADD COLUMN `tempfee_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is support temp fee';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `recharge_type` TINYINT NOT NULL DEFAULT 0 COMMENT '1: monthly, 2: temporary';

ALTER TABLE eh_parking_recharge_orders ADD COLUMN `order_token` VARCHAR(128) NOT NULL DEFAULT '' COMMENT 'it may be from 3rd system';

ALTER TABLE eh_parking_lots ADD COLUMN `rate_flag` TINYINT NOT NULL DEFAULT 0 COMMENT 'is support add or delete rate';
