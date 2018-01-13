-- 滞纳金变量 by wentian
SET @var_id = (SELECT max(`id`) from `eh_payment_variables`);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@var_id:=@var_id+1, NULL, '6', '欠费', '0', '2017-10-16 09:31:00', NULL, '2017-10-16 09:31:00', 'qf');