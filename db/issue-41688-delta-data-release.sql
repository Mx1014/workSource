-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:




-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境



-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:




-- --------------------- SECTION END zuolin-base ---------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-standard
-- DESCRIPTION: 此SECTION只在左邻标准版执行的脚本

-- AUTHOR:梁燕龙 20181120
-- REMARK: 将移动端工作台的通讯录合我的任务移动到企业办公中
SET @id = (SELECT IFNULL(MAX(id),0) from eh_service_module_entries);
SET @categoryId = (SELECT IFNULL(id,0) from eh_app_categories WHERE name = '企业办公' AND location_type = 1);
INSERT INTO eh_service_module_entries (id,module_id, module_name, entry_name, terminal_type, location_type, scene_type, app_category_id, default_order)
VALUES (@id := @id+1,50100,'组织架构','通讯录',1,1,1,@categoryId,1);
INSERT INTO eh_service_module_entries (id,module_id, module_name, entry_name, terminal_type, location_type, scene_type, app_category_id, default_order)
VALUES (@id := @id+1,13000,'任务管理','我的任务',1,1,1,@categoryId,2);

-- AUTHOR: 梁燕龙 20181123
-- REMARK: 删除原来的用户编辑数据
DELETE from eh_user_apps WHERE location_type = 1 OR location_type = 2;
DELETE from eh_user_app_flags WHERE location_type = 1 OR location_type = 2;
-- --------------------- SECTION END zuolin-base ---------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: dev
-- DESCRIPTION: 此SECTION只在开发库中执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END dev -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zijing
-- DESCRIPTION: 此SECTION只在清华信息港(紫荆)-999984执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:



-- --------------------- SECTION END guangda -------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END szbay ---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END chuangyechang -------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END anbang---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本
-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本
-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- --------------------- SECTION END ruianxintiandi ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本




-- --------------------- SECTION END wanzhihui ------------------------------------------