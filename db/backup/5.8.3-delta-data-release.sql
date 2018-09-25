-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR: 梁家声
-- REMARK: 基线更新ssl证书

-- --------------------- SECTION END OPERATION------------------------------------------------



-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END ALL -----------------------------------------------------




-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:
-- --------------------- SECTION END zuolin-base ---------------------------------------------
-- AUTHOR: 郑思挺
-- REMARK: 住总钱包
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'pay.v2.callback.url.paymentCard', '/payment/payNotify', '一卡通新支付回调接口', '0', NULL, NULL);
INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'pay.v2.refund.url.paymentCard', '/payment/refundNotify', '一卡通新支付退款回调接口', '0', NULL, NULL);
INSERT INTO `eh_payment_card_issuers` (`id`, `name`, `description`, `pay_url`, `alipay_recharge_account`, `weixin_recharge_account`, `vendor_name`,  `create_time`, `status`) VALUES ('2', 'zhuzong', 'zhuzong', 'adfasdf', 'adfasdf', 'asdfsdfasdf', 'ZHUZONG',now(), '1');
INSERT INTO `eh_payment_card_issuer_communities` (`id`, `owner_type`, `owner_id`, `issuer_id`, `hotline`, `create_time`) VALUES ('100002', 'community', '240111044332060075', '2', NULL, now());

update eh_service_module_privileges set remark = '用户管理权限' where privilege_id = 4120041210;
update eh_service_module_privileges set module_id = 41210 where privilege_id = 4120041220;
update eh_service_module_privileges set module_id = 41230 where privilege_id = 4120041240;
update eh_service_modules set name = '用户管理' where id = 41210;
update eh_service_modules set name = '交易记录' where id = 41230;
delete from eh_service_modules where id in (41220,41240);

INSERT INTO `eh_configurations` ( `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ( 'paymentCard.zhuzong.url', 'http://111.207.114.165:9050', '住总一卡通地址', '0', NULL, NULL);

update `eh_payment_cards` set update_time = create_time where update_time is null;
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