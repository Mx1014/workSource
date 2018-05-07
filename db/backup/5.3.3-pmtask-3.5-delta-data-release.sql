/*
  物业报修 应用配置数据迁移
*/
update eh_service_module_apps set instance_config='{"taskCategoryId":6,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId_u003d6%';
update eh_service_module_apps set instance_config='{"taskCategoryId":6,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId=6%';
update eh_service_module_apps set instance_config='{"taskCategoryId":9,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId_u003d9%';
update eh_service_module_apps set instance_config='{"taskCategoryId":9,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId=9%';
/*
  物业报修 权限配置页面信息迁移
*/
update eh_service_modules set name='统计信息' where id = 20190 and parent_id = 20100;
update eh_service_module_privileges set remark = '全部权限' where module_id = 20140 and privilege_id = 2010020140;
update eh_service_module_privileges set remark = '全部权限' where module_id = 20190 and privilege_id = 2010020190;