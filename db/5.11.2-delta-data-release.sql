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
update eh_rentalv2_orders set source = 1 where source is null;
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


-- AUTHOR: 缪洲 20181211
-- REMARK: 工位预定V2.4
DELETE FROM eh_office_cubicle_spaces;
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
UPDATE eh_service_modules SET `host` = 'station-booking-web' WHERE id = 40200;

INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES (40230, '工位管理', 40200, '/200/110000/40200/40230', 1, 4, 2, 0, '2018-03-31 22:18:45', NULL, NULL, '2018-03-31 22:18:45', 0, 1, '1', NULL, '', 1, 1, 'subModule', 1, 0, NULL, NULL, NULL, NULL);
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`, `access_control_type`, `menu_auth_flag`, `category`, `app_type`, `client_handler_type`, `system_app_flag`, `icon_uri`, `host`, `enable_enterprise_pay_flag`) VALUES (40240, '订单记录', 40200, '/200/110000/40200/40240', 1, 4, 2, 0, '2018-03-31 22:18:45', NULL, NULL, '2018-03-31 22:18:45', 0, 1, '1', NULL, '', 1, 1, 'subModule', 1, 0, NULL, NULL, NULL, NULL);

SET @id = (SELECT MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1,40230, 0, 4920049500, '工位管理权限', 0, '2018-03-31 22:18:45');
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1,40240, 0, 4920049600, '订单记录权限', 0, '2018-03-31 22:18:45');

UPDATE eh_service_modules SET name = '预约参观记录' WHERE id = 40220;
UPDATE eh_service_module_privileges SET remark = '预约参观记录权限' WHERE privilege_id = '4020040220';

-- AUTHOR: 马世亨 20181212
-- REMARK: 访客v1.4 访客短信优化 url修改
UPDATE `eh_configurations` SET `value`='/visitor-appointment/build/invitation.html?visitorToken=', `is_readonly`='0' WHERE `name`='visitorsys.invitation.link';

-- AUTHOR: xq.tian 20181213
-- REMARK: 工作流 2.8.1 旧的数据适应新的代码
UPDATE eh_flows SET form_status = 1,form_relation_type=1,form_relation_data=CONCAT('{"formOriginId":', form_origin_id,',"formVersion":', form_version, '}') WHERE form_origin_id<>0;
UPDATE eh_flow_nodes SET form_relation_type=1,form_relation_data=CONCAT('{"formOriginId":', form_origin_id,',"formVersion":', form_version, '}') WHERE form_origin_id<>0;


-- AUTHOR: 黄明波
-- REMARK: 更新打印机归属项目
update eh_siyin_print_printers set owner_type = 'community', owner_id = 240111044331058733 where reader_name = 'TC101154727022' ;
update eh_siyin_print_printers set owner_type = 'community', owner_id = 240111044331050362  where reader_name = 'TC101154727294';
update eh_siyin_print_printers set owner_type = 'community', owner_id = 240111044332063596 where reader_name = 'TC100887870538';

-- AUTHOR: 吴寒
-- REMARK: 修改bug:之前的菜单表和模块表id搞错了
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`, `scene_type`) VALUES('79881000','企业支付授权','70000010',NULL,'enterprise-payment-auth','1','2','/70000010/79881000','zuolin','8','271000','3','system','module','2','1');
UPDATE eh_web_menus SET id = 79880000 , module_id =271000 WHERE id = 271000;
UPDATE eh_service_modules SET id = 270000 , path ='/100/270000' WHERE id = 79870000;
UPDATE eh_service_modules SET id = 271000 , parent_id = 270000 , path ='/100/270000/271000' WHERE id = 79880000;
UPDATE eh_service_modules SET id = 271000 , parent_id = 270000 , path ='/100/270000/271000' WHERE id = 79880000;
UPDATE eh_service_module_apps SET module_id = 271000 WHERE module_id = 79880000;
UPDATE eh_service_module_entries SET module_id = 271000 WHERE module_id = 79880000;
UPDATE eh_service_module_functions SET module_id = 271000 WHERE module_id = 79880000;
UPDATE eh_service_module_privileges SET module_id = 271000 WHERE module_id = 79880000;
UPDATE eh_service_module_scopes SET module_id = 271000 WHERE module_id = 79880000;
UPDATE eh_service_module_securities SET module_id = 271000 WHERE module_id = 79880000; 


-- AUTHOR:孟千翔
-- REMARK:设置减免金额均为删除
UPDATE eh_payment_exemption_items SET delete_flag=1;

-- AUTHOR:杨崇鑫 20181212
-- REMARK:物业缴费V8.0（账单对接卡券） -44680
SET @id = ifnull((SELECT MAX(id) FROM `eh_configurations`),0);
INSERT INTO `eh_configurations`(`id`, `name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) 
	VALUES (@id := @id + 1, 'pay.v2.callback.url.assetV2', '/asset/payNotifyV2', '物业缴费新支付回调接口对接最新统一订单', 0, NULL, 1);



-- AUTHOR: 丁建民 20181210
-- REMARK: issue-43782 合同套打权限
UPDATE `eh_service_module_functions` SET  `explain`='打印' WHERE `id`='21216';
UPDATE `eh_service_module_privileges` SET  `remark`='打印' WHERE `privilege_id`='21216';

SET @id = (SELECT MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 21210, 0, 21226, '生成合同文档', 12, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (21226, 0, '合同管理 生成合同文档', '合同管理生成合同文档权限', NULL);
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES (21226, '21200', '21226', '生成合同文档');

INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 21210, 0, 21227, '查看合同文档', 13, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (21227, 0, '合同管理 查看合同文档', '合同管理 查看合同文档权限', NULL);
INSERT INTO `eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES (21227, '21200', '21227', '查看合同文档');


-- AUTHOR: 黄鹏宇 2018年12月11日
-- REMARK: 新建字段
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12120, 'enterprise_customer', 'legalAddress', '法定地址', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 512}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12121, 'enterprise_customer', 'legalAddressZip', '法定地址邮编', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12122, 'enterprise_customer', 'postalAddress', '通讯地址', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 512}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12123, 'enterprise_customer', 'postalAddressZip', '通讯地址邮编', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12124, 'enterprise_customer', 'taxpayerIdentificationCode', '纳税人识别号', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12125, 'enterprise_customer', 'identifyCardNumber', '身份证号码', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12126, 'enterprise_customer', 'openingBank', '开户行', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12127, 'enterprise_customer', 'openingName', '开户名', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12128, 'enterprise_customer', 'openingAccount', '开户行账号', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12129, 'enterprise_customer', 'stringTag17', '预留字段17', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12130, 'enterprise_customer', 'stringTag18', '预留字段18', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12131, 'enterprise_customer', 'stringTag19', '预留字段19', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12132, 'enterprise_customer', 'stringTag20', '预留字段20', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12133, 'enterprise_customer', 'stringTag21', '预留字段21', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 128}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12134, 'enterprise_customer', 'corpLegalPersonDuty', '法人代表职务', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields`(`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES (12135, 'enterprise_customer', 'corpLegalPersonToken', '法人代表职务', 'String', 11, '/1/11/', 0, NULL, 2, 1, SYSDATE(), 1, SYSDATE(), '{\"fieldParamType\": \"text\", \"length\": 32}');


SET @id = IFNULL((select max(id) from eh_var_field_ranges), 1);
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12120,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12121,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12122,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12123,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12124,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12125,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12126,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12127,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12128,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12134,'enterprise_customer','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12135,'enterprise_customer','enterprise_customer');


INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12120,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12121,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12122,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12123,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12124,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12125,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12126,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12127,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12128,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12129,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12130,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12131,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12132,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12133,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12134,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',12135,'investment_promotion','enterprise_customer');


INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',14,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',16,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',19,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',20,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',3,'investment_promotion','enterprise_customer');
INSERT INTO eh_var_field_ranges VALUES(@id:=@id+1,'/1/11/',28,'investment_promotion','enterprise_customer');


INSERT INTO `eh_var_field_group_ranges`(`id`, `group_id`, `module_name`, `module_type`) VALUES (29, 11, 'investment_promotion', 'enterprise_customer');
set @id = (select max(id) from eh_var_field_group_scopes);
INSERT INTO `eh_var_field_group_scopes`(`id`, `namespace_id`, `owner_id`, `owner_type`, `module_name`, `group_id`, `group_display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `community_id`, `group_parent_id`, `category_id`) VALUES (@id:= @id + 1, 0, 1, 'EhOrganizations', 'investment_promotion', 11, '企业情况', 2, 2, 1, SYSDATE(), NULL, NULL, NULL, NULL, NULL);


-- AUTHOR: djm 20181206
-- REMARK: 合同套打添加动态字段
SET @id = (SELECT MAX(id) from eh_var_fields);
-- 固定添加的动态字段
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'apartmentDeliveryTime', '房屋交付日期', 'Long', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'downPaymentRentTime', '首期租金开始日期', 'Long', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'monthlyMargin', '保证金月数', 'Integer', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'marginAmount', '保证金金额', 'BigDecimal', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'monthlyServiceCharge', '月服务费金额', 'BigDecimal', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'preAmount', '预付金额', 'BigDecimal', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'contractingPlace', '签约地点', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
-- 预留字段
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag1', '预留字段1', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag2', '预留字段2', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag3', '预留字段3', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag4', '预留字段4', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag5', '预留字段5', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag6', '预留字段6', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag7', '预留字段7', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag8', '预留字段8', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag9', '预留字段9', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');
INSERT INTO eh_var_fields (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@id:=@id+1), 'contract', 'stringTag10', '预留字段10', 'String', '15', '/13/15/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 64}');


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

-- AUTHOR: 黄明波
-- REMARK: 更新打印机归属项目
update eh_siyin_print_printers set owner_type = 'community', owner_id = 240111044332063562 where reader_name = 'TC101152723634';
update eh_siyin_print_printers set owner_type = 'community', owner_id = 240111044332063563 where reader_name = 'TC101152723636';


-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本

-- AUTHOR: 黄明波
-- REMARK: 更新打印机归属项目
update eh_siyin_print_printers set owner_type = 'community', owner_id = 240111044332063578  where reader_name = 'TC101152723470';
update eh_siyin_print_printers set owner_type = 'community', owner_id = 240111044332063578  where reader_name = 'TC101152723540';
update eh_siyin_print_printers set owner_type = 'community', owner_id = 240111044332063578  where reader_name = 'TC101152723478';



-- --------------------- SECTION END ruianxintiandi ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本
-- --------------------- SECTION END wanzhihui ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: jinmao
-- DESCRIPTION: 此SECTION只在上海金茂-智臻生活 -999925执行的脚本
-- --------------------- SECTION END jinmao ------------------------------------------