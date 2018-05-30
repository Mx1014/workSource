UPDATE `eh_service_modules` SET `multiple_flag` = 1 WHERE `id` = 20400;
UPDATE `eh_service_modules` SET `instance_config` = NULL WHERE `id` = 20400;


-- from testByDingjianminThree
-- 增加付款合同菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES
  ('16051300', '付款合同', '16050000', NULL, 'payment-contract', '1', '2', '/16000000/16050000/16051300', 'zuolin', '13', '21200', '3', 'system', 'module', NULL);

-- 付款合同应用
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`)
VALUES ('21215', '付款合同', '110000', '/110000/21215', '1', '2', '2', '100', '2018-01-24 14:27:33', NULL, '13', NULL, '1', '1', NULL, NULL, 'community_control');

-- 合同管理点击加号的出现收款合同付款合同分开：
UPDATE eh_var_field_groups SET module_name='paymentcontract' WHERE module_name='contract' AND path='/30';