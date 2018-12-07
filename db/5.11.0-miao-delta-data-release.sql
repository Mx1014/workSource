
SET @id = (SELECT MAX(id) from eh_locale_templates);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1,'sms.default', 94, 'zh_CN', '工会预定成功付款短信', '您已成功预定了${spaceName}短租工位，预定时间：${reserveTime}，订单编号：${orderNo}.如需取消，请在预定开始时间前取消，感谢您的使用。', 0);
INSERT INTO `eh_locale_templates`(`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1,'sms.default', 95, 'zh_CN', '工会预定成功退款短信', '尊敬的用户，您预定的${spaceName}短租工位已退款成功，订单编号：${orderNo}，订单金额${price}元，退款金额：${refundPrice}元，期待下次为您服务。', 0);

SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10001', 'zh_CN', '城市已存在');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10002', 'zh_CN', '城市id不存在');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10003', 'zh_CN', '城市名称为空');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10004', 'zh_CN', '城市名称为空');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10005', 'zh_CN', '工作流未启用');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10006', 'zh_CN', '已经为自定义配置');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10007', 'zh_CN', '已经为通用配置');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10008', 'zh_CN', '参数错误');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10009', 'zh_CN', '未设置收款方账号');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10010', 'zh_CN', '重复账号');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10011', 'zh_CN', '参数错误');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10012', 'zh_CN', '退款失败');
INSERT INTO `eh_locale_strings`(`id`,`scope`, `code`, `locale`, `text`) VALUES (@id:=@id+1,'officecubicle', '10013', 'zh_CN', '工位数量不足');


UPDATE eh_service_modules SET instance_config = '{"url":"${home.url}/station-booking-web/build/index.html?ns=%s#/home#sign_suffix","currentProjectOnly":0}' WHERE id = 40200;
UPDATE eh_service_modules SET `host` = 'station-booking-web' WHERE module_id = 40200;

INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES (40230, '工位管理', 40200, '/200/110000/40200/40230', 1, 4, 2, 0, '2018-03-31 22:18:45', NULL, NULL, '2018-03-31 22:18:45', 0, 1, '1', NULL, '', 1, 1, 'subModule', 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES (40240, '订单记录', 40200, '/200/110000/40200/40240', 1, 4, 2, 0, '2018-03-31 22:18:45', NULL, NULL, '2018-03-31 22:18:45', 0, 1, '1', NULL, '', 1, 1, 'subModule', 1, 0, NULL, NULL, NULL, NULL);

SET @id = (SELECT MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1,40230, 0, 4920049500, '工位管理权限', 0, '2018-03-31 22:18:45');
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1,40240, 0, 4920049600, '订单记录权限', 0, '2018-03-31 22:18:45');

UPDATE eh_service_modules SET name = '预约参观记录' WHERE id = 40220;
UPDATE eh_service_module_privileges SET remark = '预约参观记录权限' WHERE privilege_id = '4020040220';