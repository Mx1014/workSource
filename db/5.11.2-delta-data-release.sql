-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:
-- AUTHOR:马世亨 20181211
-- REMARK:刷新es映射 visitorsys.sh 调用 /evh/visitorsys/syncVisitor同步数据
-- REMARK:刷新es映射 freqvisitor.sh 调用 /evh/visitorsys/syncFreqVisitors同步数据

-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境
-- AUTHOR:
-- REMARK:
-- AUTHOR:郑思挺
-- REMARK:资源预约消息推送
update eh_locale_templates set text = '${userName}（${phone}）预约了${resourceName}，使用时间：${useDetail}${freeGoods}${paidGoods}' where scope = 'rental.notification' and `code` = 2;

-- AUTHOR: 马世亨 20181211
-- REMARK: 访客v1.4 添加住宅小区到访类型
update eh_visitor_sys_visit_reason set reason_type = 1 , visit_reason_code = id;
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `reason_type`, `visit_reason_code`)
VALUES ('11', '0', 'community', '0', '朋友', '2', '0', '2018-06-21 16:15:40', '0', '2018-06-21 16:15:40', 0, 0);
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `reason_type`, `visit_reason_code`)
VALUES ('12', '0', 'community', '0', '外卖', '2', '0', '2018-06-21 16:15:40', '0', '2018-06-21 16:15:40', 0, 1);
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `reason_type`, `visit_reason_code`)
VALUES ('13', '0', 'community', '0', '快递', '2', '0', '2018-06-21 16:15:40', '0', '2018-06-21 16:15:40', 0, 2);
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `reason_type`, `visit_reason_code`)
VALUES ('14', '0', 'community', '0', '家政', '2', '0', '2018-06-21 16:15:40', '0', '2018-06-21 16:15:40', 0, 3);
INSERT INTO `eh_visitor_sys_visit_reason` (`id`, `namespace_id`, `owner_type`, `owner_id`, `visit_reason`, `status`, `creator_uid`, `create_time`, `operator_uid`, `operate_time`, `reason_type`, `visit_reason_code`)
VALUES ('15', '0', 'community', '0', '其它', '2', '0', '2018-06-21 16:15:40', '0', '2018-06-21 16:15:40', 0, 4);

-- AUTHOR: 马世亨 20181211
-- REMARK: 访客v1.4 访客鼎峰汇对接配置项
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('visitorsys.dingfenghui.url.interface', 'http://syx.jslife.com.cn/jsaims/as', '访客鼎峰汇对接接口地址', '0', NULL, '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('visitorsys.dingfenghui.cid', '880075500000001', '访客鼎峰汇对接客户号', '0', NULL, '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('visitorsys.dingfenghui.version', '2', '访客鼎峰汇对接版本', '0', NULL, '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('visitorsys.dingfenghui.signKey', '7ac3e2ee1075bf4bb6b816c1e80126c0', '访客鼎峰汇对接秘钥', '0', NULL, '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('visitorsys.dingfenghui.url.login', 'http://syx.jslife.com.cn/jsaims/login', '访客鼎峰汇对接登录地址', '0', NULL, '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('visitorsys.dingfenghui.usr', '880075500000001', '访客鼎峰汇对接客户号', '0', NULL, '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('visitorsys.dingfenghui.psw', '888888', '访客鼎峰汇对接帐号', '0', NULL, '0');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('visitorsys.dingfenghui.parkCode', '0010028888', '访客鼎峰汇对接密码', '0', NULL, '0');

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
-- --------------------- SECTION END wanzhihui ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: jinmao
-- DESCRIPTION: 此SECTION只在上海金茂-智臻生活 -999925执行的脚本
-- --------------------- SECTION END jinmao ------------------------------------------