-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR: xq.tian 20181116
-- REMARK: 替换最新的 contentserver 二进制 #40547 contentserver/release/server/contentserver


-- AUTHOR: 黄鹏宇
-- REMARK: 请在执行release前备份eh_var_field_scopes表



-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR: xq.tian  20181116
-- REMARK: 报错提示模板
INSERT INTO eh_locale_strings (scope, code, locale, text) VALUES ('flow', '10013', 'zh_CN', '任务状态已经改变，请刷新重试');

-- AUTHOR: 张智伟 20181115
-- REMARK: issue-37602 审批单支持编辑
SET @string_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@string_id := @string_id + 1, 'enterpriseApproval.error', '30003', 'zh_CN', '该节点任务已被处理');


-- AUTHOR: 吴寒
-- REMARK: 企业支付授权
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('enterprise_payment_auth_operate_log','1','zh_CN','授权日志主要','设置 ${employeeCount} 名员工（${employeeName}）\n每人额度： ${limitAmount} 元/月\n支付场景：${sceneString}','0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('enterprise_payment_auth_operate_log','2','zh_CN','授权日志场景','${sceneName}（${limitAmount} 元/月）','0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('enterprise_payment_auth_operate_log','3','zh_CN','授权记录场景','${sceneName}：${changeAmount} 元，当前额度为 ${limitAmount} 元','0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('enterprise_payment_auth_msg','1','zh_CN','设置授权推送消息','${operator}调整了你的企业支付额度，点击查看。','0');
INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES('enterprise_payment_auth_msg','2','zh_CN','支付后，可用余额扣除推送消息','你在 ${payTime} 使用企业账号支付了一笔 ${payAmount?string(",##0.00")} 的 ${sceneName} 订单，点击查看。','0');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth_operate_source','1','zh_CN','每月额度');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth','1','zh_CN','无');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth','2','zh_CN','增加');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth','3','zh_CN','减少');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth','4','zh_CN','订单编号;使用人;使用人手机;支付场景;支付时间;支付金额');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth','5','zh_CN','企业支付记录');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth','6','zh_CN','支付时间:');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth','7','zh_CN','企业支付额度');

-- AUTHOR: 吴寒
-- REMARK: 企业支付授权   支付授权的菜单
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES('79870000','支付管理','100','/100/79870000','1','2','2','50','2018-09-26 16:51:46',NULL,NULL,'2018-09-26 16:51:46','0','0','0',NULL,'org_control','1','1','classify','0','0',NULL,NULL,NULL,NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES('79880000','支付授权','79870000','/100/79870000/79880000','1','3','2','10','2018-09-26 16:51:46',NULL,NULL,'2018-09-26 16:51:46','0','0','0',NULL,'org_control','1','1','module','0','0',NULL,NULL,NULL,NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('271000','企业支付授权','70000010',NULL,'enterprise-payment-auth','1','2','/70000010/271000','park','8','79880000','3','system','module','2','1');

-- AUTHOR: 张智伟
-- REMARK: 企业支付授权
SET @locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 1);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'service_module_security', '1', 'zh_CN', '已经设置了安全密码，不能重复设置');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'service_module_security', '2', 'zh_CN', '未设置安全密码');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'service_module_security', '3', 'zh_CN', '安全密码错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'service_module_security', '4', 'zh_CN', '验证码错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'service_module_security', '5', 'zh_CN', '验证码失效');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'service_module_security', '6', 'zh_CN', '需要安全验证才能访问');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '506', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '100', 'zh_CN', '员工不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '101', 'zh_CN', '员工已离职');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '102', 'zh_CN', 'token错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '103', 'zh_CN', '鉴权失败');

-- AUTHOR: 张智伟
-- REMARK: 企业支付授权
SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_configurations`),1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'service.module.security.time.out-79880000', '5', '', '0', NULL);

-- AUTHOR: 黄鹏宇
-- REMARK: fixbug #42303
update eh_var_field_scopes set status = 0 where field_display_name in ('客户级别','资质客户') and module_name = 'enterprise_customer';
-- END

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
-- 圳智慧beta
-- addy yanlong.liang 20180814
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('zhenzhihui.url', 'http://120.132.117.22:8016/ZHYQ/restservices/LEAPAuthorize/attributes/query?TICKET=', '圳智慧认证url', '999931', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('zhenzhihui.appkey', 'c00f02aac30d0822', '圳智慧APPKEY', '999931', NULL, '1');
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`)
VALUES ('zhenzhihui.redirect.url', 'http://120.132.117.22:8016/ZHYQ/restservices/common/zhyq_zlgetUserinfo/query?code=', '圳智慧对接跳转url', '999931', NULL, '1');
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