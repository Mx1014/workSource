/*
  物业报修 应用配置数据迁移
*/
update eh_service_module_apps set instance_config='{"taskCategoryId":6,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId_u003d6%';
update eh_service_module_apps set instance_config='{"taskCategoryId":6,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId=6%';
update eh_service_module_apps set instance_config='{"taskCategoryId":9,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId_u003d9%';
update eh_service_module_apps set instance_config='{"taskCategoryId":9,"agentSwitch":1}' where module_id='20100' and instance_config like '%taskCategoryId=9%';