-- 物业缴费模块，独立部署项目时需要的初始化脚本 by wentian 2018/3/30

-- handler的默认地址
INSERT INTO `eh_asset_vendor` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `status`, `namespace_id`) VALUES ('1', 'community', '1', 'zuolin物业缴费', 'ZUOLIN', '2', '0');

-- 初始化收费项目
TRUNCATE `eh_payment_charging_items`;
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('1', '租金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('2', '物业费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('3', '押金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('4', '水费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('5', '电费', '0', UTC_TIMESTAMP(), NULL, NULL, '1');
INSERT INTO `eh_payment_charging_items`(`id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `default_order`)VALUES('6', '滞纳金', '0', UTC_TIMESTAMP(), NULL, NULL, '1');

-- 初始化公式变量
TRUNCATE `eh_payment_variables`;
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES ('1', NULL, NULL , '单价', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'dj');
set @eh_payment_variables_id = (SELECT MAX(id) from `eh_payment_variables`);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,null, '面积', 0, UTC_TIMESTAMP(), NULL,UTC_TIMESTAMP(), 'mj');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,null, '固定金额', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'gdje');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,null, '用量', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'yl');
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@eh_payment_variables_id:=@eh_payment_variables_id+1,  null,null, '欠费', 0, UTC_TIMESTAMP(), NULL, UTC_TIMESTAMP(), 'qf');



