-- 停车充值月卡申请是否填写身份证号码.bydengs,20180118
update eh_parking_lots SET config_json = REPLACE(config_json,'}',',"identityCardFlag":0}') WHERE owner_id <> 240111044331055940;
update eh_parking_lots SET config_json = REPLACE(config_json,'}',',"identityCardFlag":1}') WHERE owner_id = 240111044331055940;


INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.zijing.marketid', '075500012201801220001', '紫荆车辆放行markid', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.zijing.userid', '0755000120180122000000000001', '紫荆车辆放行userid', 0, NULL);


-- 更新所有域空间的模块类型
update eh_service_modules set module_control_type = 'unlimit_control' where id = 41300;
update eh_service_modules set module_control_type = 'unlimit_control' where id = 49200;