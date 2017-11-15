-- 更新园区控制的模块
UPDATE eh_service_modules set module_control_type = 'community_control' where id in (10100,10200,10400,10600,10700,10800,10850,11000,12200,20100,20400,20600,20800,20900,21100,21200,30500,31000,32000,32500,33000,37000,40100,40200,40300,40400,40500,40600,40700,40800,40900,41000,41100,41200,41400,41500,41600,41700,49100);
-- 更新OA控制的模块
UPDATE eh_service_modules set module_control_type = 'org_control' where id in( 50100,50400,50600,50700,52000);
-- 更新不受限制控制的模块
UPDATE eh_service_modules set module_control_type = 'unlimit_control' where id in(10750,13000,21000,30600,34000,35000,40750,41330,50300,50500,50900,51000);


-- 更新园区控制的模块关联的应用
update eh_service_module_apps set module_control_type = 'community_control' where module_id in (10100,10200,10400,10600,10700,10800,10850,11000,12200,20100,20400,20600,20800,20900,21100,21200,30500,31000,32000,32500,33000,37000,40100,40200,40300,40400,40500,40600,40700,40800,40900,41000,41100,41200,41400,41500,41600,41700,49100);
-- 更新OA控制的模块
UPDATE eh_service_module_apps set module_control_type = 'org_control' where module_id in( 50100,50400,50600,50700,52000);
-- 更新不受限制控制的模块
UPDATE eh_service_module_apps set module_control_type = 'unlimit_control' where module_id in(10750,13000,21000,30600,34000,35000,40750,41330,50300,50500,50900,51000);