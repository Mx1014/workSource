-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR: 杨崇鑫
-- REMARK: 新支付的配置
UPDATE `eh_configurations` set value='http://payv2-alpha.zuolin.com/pay' where name='pay.v2.payHomeUrl';
-- --------------------- SECTION END ---------------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR: 杨崇鑫 
-- REMARK: 新支付数据迁移
UPDATE `eh_configurations` set value='http://payv2-alpha.zuolin.com/pay' where name='pay.v2.payHomeUrl';
-- --------------------- SECTION END ---------------------------------------------------------



-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR: 杨崇鑫 
-- REMARK: 新支付数据迁移
UPDATE `eh_configurations` set value='http://payv2-alpha.zuolin.com/pay' where name='pay.v2.payHomeUrl';
-- --------------------- SECTION END ---------------------------------------------------------



-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR: 杨崇鑫
-- REMARK: 物业缴费新支付数据迁移
UPDATE `eh_configurations` set value='http://payv2-alpha.zuolin.com/pay' where name='pay.v2.payHomeUrl';
-- --------------------- SECTION END ---------------------------------------------------------