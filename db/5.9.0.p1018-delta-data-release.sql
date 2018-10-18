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

-- AUTHOR:黄明波
-- REMARK:服务联盟离线应用化 issue-39394
set @max_id := (select ifnull(max(id), 0) from eh_version_realm);
set @namespace_id := 999953;
set @module_name := '服务联盟';
set @version = '1.0.0';
INSERT INTO `eh_version_realm` (`id`, `realm`, `description`, `create_time`, `namespace_id`) VALUES ((@max_id:=@max_id+1), 'serviceAlliance', NULL, now(), @namespace_id);

set @max_id_2 := (select ifnull(max(id), 0) from eh_version_urls);
set @home_url := (select `value` from eh_configurations where name = 'home.url');
set @zip_url := concat(@home_url,'/nar/serviceAlliance/offline/serviceAlliance-1-0-0.zip');
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `upgrade_description`, `namespace_id`, `app_name`, `publish_time`, `icon_url`, `version_encoded_value`) VALUES (@max_id_2+1, @max_id, @version, @zip_url, @zip_url, NULL, @namespace_id, @module_name, now(), NULL, 0);


update eh_service_modules set action_type = 44, instance_config = replace(instance_config, '}', ',"realm":"serviceAlliance"}') where id = 40500 and instance_config not like '%"realm":%';

update eh_service_module_apps set action_type = 44 where module_id = 40500 and instance_config not like '%"appType":"native"%';

update eh_launch_pad_items set 
action_data  = replace(action_data, '{"url":"${home.url}/service-alliance-web', concat('{"realm":"serviceAlliance","entryUrl":"', @home_url,'/nar/serviceAlliance')), action_type = 44
where action_data like '{"url":"${home.url}/service-alliance-web/build/index.html#/home/%' and action_type  = 14;

-- --------------------- SECTION END wanzhihui ------------------------------------------
