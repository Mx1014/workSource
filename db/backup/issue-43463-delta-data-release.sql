
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
-- REMARK: 创科谷停车场配置
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.chuangkegu.url', 'http://api.parkbees.com/system/data', '创科谷园区停车场系统地址', 0, NULL, 0);
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'parking.chuangkegu.privatekey', 'D3029C73406221B02026B684BB00579C', '创科谷园区停车场私钥', 0, NULL, 0);
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'parking.chuangkegu.ploid', '20181127115353842701630638320640', '创科谷园区停车场id', 0, NULL, 0);
INSERT INTO `eh_configurations`( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'parking.chuangkegu.comid', '000001084', '创科谷园区停车场物业id', 0, NULL, 0);
INSERT INTO `eh_parking_lots`(`owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`, `order_tag`, `order_code`, `id_hash`, `func_list`, `notice_contact`, `summary`, `default_data`, `default_plate`) VALUES ( 'community', 240111044332061061, '创科谷停车场', 'BEE_CHUANGKEGU', NULL, 2, 1, '2018-06-29 11:15:02', 999944, '{\"contact\":\"0769-22980386\",\"expiredRechargeFlag\":1,\"expiredRechargeMonthCount\":1,\"expiredRechargeType\":1,\"maxExpiredDay\":100,\"monthlyDiscountFlag\":0,\"tempFeeDiscountFlag\":0}', '{\"tempfeeFlag\":1,\"rateFlag\":1,\"lockCarFlag\":0,\"searchCarFlag\":0,\"currentInfoType\":0,\"contact\":\"0\",\"invoiceFlag\":1,\"businessLicenseFlag\":0,\"vipParkingFlag\":1,\"monthRechargeFlag\":1,\"identityCardFlag\":0,\"monthCardFlag\":1,\"flowMode\":3,\"noticeFlag\":1,\"invoiceTypeFlag\":0}', '034', '', '', '[\"vipParking\",\"invoiceApply\",\"userNotice\",\"tempfee\",\"monthRecharge\",\"monthCardApply\"]', '18898729170', '', '', '粤,B');

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
-- 圳智慧beta
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
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: shanghaijinmao
-- DESCRIPTION: 此SECTION只在上海金茂-999925执行的脚本

-- --------------------- SECTION END shanghaijinmao ------------------------------------------