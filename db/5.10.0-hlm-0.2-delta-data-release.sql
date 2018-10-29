-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR:xq.tian 20181016
-- REMARK:网关及注册中心部署.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539677844

-- AUTHOR:梁燕龙 20181016
-- REMARK:统一用户上线操作.
-- DESCRIPTION：http://s.a.com/docs/faq/baseline-21539678631

-- AUTHOR:杨崇鑫 20181018
-- REMARK:解决缺陷 #39352: 【全量标准版】【物业缴费】新增收费标准前端提示成功，但实际未新增成功，无相关数据，后台提示“应用开小差”
-- REMARK：备份eh_payment_variables表
-- select * from eh_payment_variables;



-- --------------------- SECTION END OPERATION------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- --------------------- SECTION END ALL -----------------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本


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
-- AUTHOR:
-- REMARK:


-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- AUTHOR:黄良铭  20181025
-- REMARK: 瑞安活动对接配置项
-- 默认
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey', 'd2NP2Z','publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.privatekey', 'a6cfff2c4aa370f8','privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid', '5b5046c988ce7e5ad49c9b10','appid','999929','');

-- 上海新天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.1', 'd2NP2Z','上海新天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.1', 'a6cfff2c4aa370f8','上海新天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.1', '5b5046c988ce7e5ad49c9b10','上海新天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.1', '10764','上海新天地mallid','999929','');


-- 重庆天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.publickey.2', 'o7Oep_','重庆天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.2', '7d57f43738f546e2','重庆天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.2', '5b505c8688ce7e238c3c3a2a','重庆天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.2', '10782','重庆天地mallid','999929','');

-- 岭南天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.3', 'bOT1fy','岭南天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.3', 'ef94c7e11445aebd','岭南天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.3', '5b505b8e3ae74e465c93447b','岭南天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.3', '10778','岭南天地mallid','999929','');


-- 虹桥天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'mall.ruian.publickey.4', '6pqMSA','虹桥天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.4', '761604f49636c418','虹桥天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.4', '5b505ac188ce7e238c3c3a28','虹桥天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.4', '10743','虹桥天地mallid','999929','');

-- 创智天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.5', 'XWLzKN','创智天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.5', 'cfeb935979f50825','创智天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.5', '5b5048333ae74e58743209f7','创智天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.5', '10776','创智天地mallid','999929','');

-- 瑞虹天地
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.publickey.6', 'ydVQ7f','瑞虹天地publickey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.privatekey.6', '24f36ef07865a906','瑞虹天地privatekey','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.appid.6', '5b5047103ae74e58743209f3','瑞虹天地appid','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'ruian.mall.id.6', '10775','瑞虹天地mallid','999929','');



INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'activity.butt.url.getcategorylist', 'https://openapi10.mallcoo.cn/Event/Activity/V1/GetCategoryList/','获取活动分类','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'activity.butt.url.getactivitylist', 'https://openapi10.mallcoo.cn/Event/Activity/V1/GetList/','获取活动列表','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`)
VALUES ( 'activity.butt.url.getactivity', ' https://openapi10.mallcoo.cn/Event/Activity/V1/GetDetail/','获取活动详情','999929','');
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name` )
VALUES ( 'mall.ruian.url.activity', 'https://m.mallcoo.cn/a/custom/10764/xtd/activitylist','瑞安活动列表面URL','999929','');
-- end

-- --------------------- SECTION END ruianxintiandi ------------------------------------------


-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
