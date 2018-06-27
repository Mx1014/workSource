-- 通用脚本  
-- ADD BY 丁建民 
-- #28874 合同管理（多应用） 产品功能（不同的合同支持不同的部门可见，同时支持一个资源签多份合同）
UPDATE `eh_service_modules` SET `multiple_flag` = 1 WHERE `id` = 20400;
UPDATE `eh_service_modules` SET `instance_config` = NULL WHERE `id` = 20400;

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

-- 更新企业管理客户级别其他--》历史客户
UPDATE eh_var_field_items SET display_name='历史客户' WHERE module_name='enterprise_customer' and field_id=5 and display_name='其他';
-- 界面显示历史客户
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_var_field_item_scopes`),0);
INSERT INTO `eh_var_field_item_scopes` (`id`, `namespace_id`, `module_name`, `field_id`, `item_id`, `item_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `business_value`, `category_id`) VALUES 
(@id:=@id+1, '0', 'enterprise_customer', '5', '7', '历史客户', '7', '2', '1', '2018-06-08 17:37:13', NULL, NULL, NULL, NULL, NULL);


-- 合同基础参数配置 工作流配置 权限
SELECT id from eh_service_modules WHERE path IN ('/110000/21200/21230','/110000/21200/21220');
SELECT id from EH_SERVICE_MODULE_PRIVILEGES  WHERE module_id='21210' AND privilege_id=21213 AND privilege_type=0;
-- 对接第三方 权限
SELECT id from EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS  WHERE namespace_id IN(999971,1000000) AND module_id IN (21100,21200)AND function_id IN(98,99);
-- 免租期字段删除 权限
SELECT id from  EH_VAR_FIELDS WHERE module_name='contract' AND group_id=15 AND group_path='/13/15/' AND `name`='freeDays';
-- 客户管理 同步客户权限
SELECT id from  EH_SERVICE_MODULE_PRIVILEGES WHERE module_id=21110 and privilege_id=21104;

-- 合同基础参数配置 工作流配置 权限
DELETE FROM EH_SERVICE_MODULES WHERE id IN(SELECT id FROM (SELECT id from eh_service_modules WHERE path IN ('/110000/21200/21230','/110000/21200/21220')) sm);
DELETE FROM EH_SERVICE_MODULE_PRIVILEGES WHERE id IN(SELECT id FROM (SELECT id from EH_SERVICE_MODULE_PRIVILEGES  WHERE module_id='21210' AND privilege_id=21213 AND privilege_type=0) smp);
-- 对接第三方 权限
DELETE FROM EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS WHERE id IN(SELECT id FROM (SELECT id from EH_SERVICE_MODULE_EXCLUDE_FUNCTIONS  WHERE namespace_id IN(999971,1000000) AND module_id IN (21100,21200)AND function_id IN(98,99))smef);
-- 免租期字段删除 权限
DELETE FROM EH_VAR_FIELDS WHERE id IN(SELECT id FROM (SELECT id from  EH_VAR_FIELDS WHERE module_name='contract' AND group_id=15 AND group_path='/13/15/' AND `name`='freeDays')vf);
-- --客户管理 同步客户权限
DELETE FROM EH_SERVICE_MODULE_PRIVILEGES WHERE id IN(SELECT id FROM (SELECT id from  EH_SERVICE_MODULE_PRIVILEGES WHERE module_id=21110 and privilege_id=21104)smp);

-- 更新合同列表为收款合同
UPDATE EH_SERVICE_MODULES SET `name`='收款合同' WHERE `level`=3 and parent_id=21200 and path='/110000/21200/21210';

-- 更新合同列表为付款合同 下的相关权限
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '签约、修改' ,default_order=1 WHERE module_id = 21210 AND privilege_id = 21201;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '发起审批' ,default_order=2 WHERE module_id = 21210 AND privilege_id = 21202;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '删除' ,default_order=3 WHERE module_id = 21210 AND privilege_id = 21204;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '作废' ,default_order=4 WHERE module_id = 21210 AND privilege_id = 21205;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '入场' ,default_order=5 WHERE module_id = 21210 AND privilege_id = 21206;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '查看' ,default_order=0 WHERE module_id = 21210 AND privilege_id = 21207;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '续约' ,default_order=6 WHERE module_id = 21210 AND privilege_id = 21208;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '变更' ,default_order=7 WHERE module_id = 21210 AND privilege_id = 21209;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '退约' ,default_order=8 WHERE module_id = 21210 AND privilege_id = 21214;

-- 更新付款合同下的相关权限显示
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '新增' ,default_order=5 WHERE module_id = 21215 AND privilege_id = 21215;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '签约、发起审批',default_order=1 WHERE module_id = 21215 AND privilege_id = 21216;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '修改' ,default_order=2 WHERE module_id = 21215 AND privilege_id = 21217;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '删除' ,default_order=3 WHERE module_id = 21215 AND privilege_id = 21218;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '作废' ,default_order=4 WHERE module_id = 21215 AND privilege_id = 21219;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '查看' ,default_order=0 WHERE module_id = 21215 AND privilege_id = 21220;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '续约' ,default_order=6 WHERE module_id = 21215 AND privilege_id = 21221;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '变更' ,default_order=7 WHERE module_id = 21215 AND privilege_id = 21222;
UPDATE EH_SERVICE_MODULE_PRIVILEGES SET remark = '退约' ,default_order=8 WHERE module_id = 21215 AND privilege_id = 21223;

-- END BY 丁建民 