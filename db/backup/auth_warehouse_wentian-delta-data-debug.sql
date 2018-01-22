-- 增加模块到999992 by wentian.V.Britannia
set @service_module_id = (select MAX(`id`) from eh_service_module_scopes);
INSERT INTO `eh_service_module_scopes`
(`id`, `namespace_id`, `module_id`, `module_name`, `owner_type`, `owner_id`, `default_order`, `apply_policy`)
VALUES
(@service_module_id:=@service_module_id+1, '999992', '21000', '', NULL, NULL, NULL, '2');