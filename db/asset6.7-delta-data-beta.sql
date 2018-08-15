-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR: 杨崇鑫  20180815
-- REMARK: 海岸是定制的，所以需要release：正式环境，beta：测试环境，以此来判断是否需要为海岸造测试数据
update `eh_configurations` set value = 'beta' WHERE `name`='pay.v2.asset.haian_environment';


-- --------------------- SECTION END ---------------------------------------------------------