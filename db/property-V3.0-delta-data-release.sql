-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR:tangcen 2018年11月12日14:31:01
-- REMARK:修改楼宇资产管理模块配置
UPDATE eh_service_modules SET instance_config='{\"url\":\"${home.url}/assets-web/build/index.html?hideNavigationBar=1&ehnavigatorstyle=0}' WHERE id=38000;

UPDATE eh_service_modules SET action_type=14 WHERE id=38000;

UPDATE eh_service_modules SET client_handler_type=2 WHERE id=38000;

-- --------------------- SECTION END ALL -----------------------------------------------------