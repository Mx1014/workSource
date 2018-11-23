
-- AUTHOR: 吴寒
-- REMARK: 支付授权 
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
-- 支付授权的菜单

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES('79870000','支付管理','100','/100/79870000','1','2','2','50','2018-09-26 16:51:46',NULL,NULL,'2018-09-26 16:51:46','0','0','0',NULL,'org_control','1','1','classify','0','0',NULL,NULL,NULL,NULL);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES('79880000','支付授权','79870000','/100/79870000/79880000','1','3','2','10','2018-09-26 16:51:46',NULL,NULL,'2018-09-26 16:51:46','0','0','0',NULL,'org_control','1','1','module','0','0',NULL,NULL,NULL,NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('271000','企业支付授权','70000010',NULL,'enterprise-payment-auth','1','2','/70000010/271000','park','8','79880000','3','system','module','2','1');


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


SET @id = IFNULL((SELECT MAX(`id`) FROM `eh_configurations`),1);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES (@id:=@id+1, 'service.module.security.time.out-79880000', '5', '', '0', NULL);
