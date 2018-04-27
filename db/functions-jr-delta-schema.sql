-- 能耗增加分摊比例  by jiarui 20180416
ALTER TABLE `eh_energy_meter_addresses`
  ADD COLUMN `burden_rate`  decimal(10,2) NULL DEFAULT NULL AFTER `status`;
-- 能耗增表计是否远程自动抄表 by jiarui 20180416
ALTER TABLE `eh_energy_meters`
  ADD COLUMN `auto_flag`  tinyint(4) NOT NULL DEFAULT 0 AFTER `status`;

-- 客户事件设备类型  by jiarui
ALTER TABLE `eh_customer_events`
  ADD COLUMN `device_type`  tinyint(4) NOT NULL DEFAULT 0 AFTER `content`;

-- 客户的跟进人  by jiarui
ALTER TABLE `eh_enterprise_customers`
  MODIFY COLUMN `tracking_uid`  bigint(20) NULL DEFAULT NULL COMMENT 'tracking uid' AFTER `update_time`;

UPDATE eh_enterprise_customers SET tracking_uid = NULL WHERE tracking_uid = '-1';

-- 客户增加的权限  by jiarui
UPDATE `eh_service_module_privileges` SET  default_order = 1, `remark`='新增客户' WHERE `module_id`='21110' and  `privilege_id`='21101';
UPDATE `eh_service_module_privileges` SET  default_order = 2,`remark`='修改客户' WHERE `module_id`='21110' and  `privilege_id`='21102';
UPDATE `eh_service_module_privileges` SET  default_order = 4,`remark`='导入客户' WHERE `module_id`='21110' and  `privilege_id`='21103';
UPDATE `eh_service_module_privileges` SET  default_order = 6,`remark`='同步客户' WHERE `module_id`='21110' and  `privilege_id`='21104';
UPDATE `eh_service_module_privileges` SET  default_order = 3,`remark`='删除客户' WHERE `module_id`='21110' and  `privilege_id`='21105';
UPDATE `eh_service_module_privileges` SET  default_order = 0 , `remark`='查看客户' WHERE `module_id`='21110' and  `privilege_id`='21106';
UPDATE `eh_service_module_privileges` SET  default_order = 7,`remark`='查看管理信息'WHERE `module_id`='21110' and  `privilege_id`='21107';
UPDATE `eh_service_module_privileges` SET  default_order = 8,`remark`='新增管理信息'WHERE `module_id`='21110' and  `privilege_id`='21108';
UPDATE `eh_service_module_privileges` SET  default_order = 9,`remark`='修改管理信息'WHERE `module_id`='21110' and  `privilege_id`='21109';
UPDATE `eh_service_module_privileges` SET  default_order = 10,`remark`='删除管理信息'WHERE `module_id`='21110' and  `privilege_id`='21110';
UPDATE `eh_service_module_privileges` SET  default_order = 11,`remark`='导入管理信息'WHERE `module_id`='21110' and  `privilege_id`='21111';
UPDATE `eh_service_module_privileges` SET  default_order = 12,`remark`='导出管理信息'WHERE `module_id`='21110' and  `privilege_id`='21112';
UPDATE `eh_service_module_privileges` SET  default_order = 0,`remark`='查看' WHERE `module_id`='21110' and  `privilege_id`='21113';


INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES ('21114', '0', '客户管理导出权限', '客户管理 业务模块权限', NULL);

set @id =(select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@id:=@id+1) ,'21110', '0', '21114', '导出客户', '5', NOW());
