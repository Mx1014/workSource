UPDATE `eh_service_modules` SET `multiple_flag` = 1 WHERE `id` = 20400;
UPDATE `eh_service_modules` SET `instance_config` = NULL WHERE `id` = 20400;


-- from testByDingjianminThree
-- 增加付款合同菜单
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES
  ('16051300', '付款合同', '16050000', NULL, 'payment-contract', '1', '2', '/16000000/16050000/16051300', 'zuolin', '13', '21200', '3', 'system', 'module', NULL);

-- 添加付款合同应用
UPDATE eh_service_modules SET parent_id='110000',path='/110000/21215',`level`='2',default_order='100',action_type='13',module_control_type='community_control' WHERE id='21215';

-- 合同管理点击加号的出现收款合同付款合同分开：
UPDATE eh_var_field_groups SET module_name='paymentContract' WHERE  path like '%/30%';

-- 设置合同为多入口
UPDATE eh_service_modules SET action_type=13,multiple_flag=1 WHERE NAME='合同管理';

-- App请求地址配置
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_configurations`),0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'contract.app.url', '${home.url}/property-management/build/index.html?hideNavigationBar=1&categoryId=${categoryId}#/contract#sign_suffix', 'contract app url', '0', NULL);
