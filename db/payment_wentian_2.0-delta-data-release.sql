-- 缴费2.0的收费项目init data  by wentian
TRUNCATE `eh_payment_charging_items`;
INSERT INTO `eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
('1', '租金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');

INSERT INTO `eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
('2', '物业费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');

INSERT INTO `eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
('3', '押金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');

INSERT INTO `eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
('4', '水费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');

INSERT INTO `eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
('5', '电费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');

INSERT INTO `eh_payment_charging_items`
(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)
VALUES
('6', '滞纳金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');