
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境



-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:

-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'https://oa.zuolin.com', 'the home url', 0, NULL, 1);

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
-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'https://zijing.lihekefu.com', 'the home url', 0, NULL, 1);
-- --------------------- SECTION END ---------------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guangda
-- DESCRIPTION: 此SECTION只在光大-999979执行的脚本
-- AUTHOR:
-- REMARK:

-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'https://core.gd-we.com', 'the home url', 0, NULL, 1);

-- --------------------- SECTION END guangda -------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: szbay
-- DESCRIPTION: 此SECTION只在深圳湾-999966执行的脚本
-- AUTHOR:
-- REMARK:
-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'https://park.szbay.com', 'the home url', 0, NULL, 1);
-- --------------------- SECTION END szbay ---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: chuangyechang
-- DESCRIPTION: 此SECTION只在成都创业场-999964执行的脚本
-- AUTHOR:
-- REMARK:
-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'http://park.chuangyechang.com', 'the home url', 0, NULL, 1);
-- --------------------- SECTION END chuangyechang -------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: anbang
-- DESCRIPTION: 此SECTION只在安邦物业-999949执行的脚本
-- AUTHOR:
-- REMARK:
-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'http://park.chuangyechang.com', 'the home url', 0, NULL, 1);
-- --------------------- SECTION END anbang---------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: nanshanquzhengfu
-- DESCRIPTION: 此SECTION只在南山区政府-999931执行的脚本
-- 圳智慧beta
-- --------------------- SECTION END nanshanquzhengfu ----------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: guanzhouyuekongjian
-- DESCRIPTION: 此SECTION只在广州越空间-999930执行的脚本
-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'http://park.yuespace.com.cn', 'the home url', 0, NULL, 1);
-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'http://inno.xintiandi.com', 'the home url', 0, NULL, 1);
-- --------------------- SECTION END ruianxintiandi ------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本




-- --------------------- SECTION END wanzhihui ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: shanghaijinmao
-- DESCRIPTION: 此SECTION只在上海金茂-999925执行的脚本
-- AUTHOR: 缪洲
-- REMARK: 发票跳转链接
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('invoice.home.url', 'http://jinmao.everhomes.com', 'the home url', 0, NULL, 1);
-- --------------------- SECTION END shanghaijinmao ------------------------------------------