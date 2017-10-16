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

-- 物业缴费2.0的变量 by wentian
TRUNCATE `eh_payment_variables`;
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES ('1', NULL, '1', '单价', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'dj');
set @eh_payment_variables_id = (SELECT MAX(id) from `eh_payment_variables`);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'2', '单价', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'dj');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'3', '单价', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'dj');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'4', '单价', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'dj');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'5', '单价', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'dj');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'6', '单价', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'dj');


INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'1', '面积', 0, UTC_TIMESTAMP(), NULL,UTC_TIMESTAMP(), 'mj');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'3', '面积', 0, UTC_TIMESTAMP(), NULL,UTC_TIMESTAMP(), 'mj');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'6', '面积', 0, UTC_TIMESTAMP(), NULL,UTC_TIMESTAMP(), 'mj');

INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'1', '固定金额', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'gdje');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'2', '固定金额', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'gdje');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'3', '固定金额', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'gdje');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'4', '固定金额', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'gdje');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'5', '固定金额', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'gdje');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'6', '固定金额', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'gdje');

INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'4', '用量', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'yl');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,'5', '用量', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'yl');