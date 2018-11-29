-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等

-- AUTHOR: xq.tian 20181116
-- REMARK: 替换最新的 contentserver 二进制 #40547 contentserver/release/server/contentserver

-- AUTHOR: xq.tian 20181108
-- REMARK: 修改配置文件 config.yml
-- REMARK: 网关路由转发去掉用户申诉的路由, 把以下的几个配置从网关配置文件中删掉
-- REMARK: - Path=/evh/user/createResetIdentifierAppeal
-- REMARK: - Path=/evh/user/sendVerificationCodeByResetIdentifier
-- REMARK: - Path=/evh/user/listResetIdentifierCode
-- REMARK: - Path=/evh/user/verifyResetIdentifierCode


-- AUTHOR: 黄鹏宇
-- REMARK: 请在执行release前备份eh_var_field_scopes表

-- AUTHOR: xq.tian 20181129
-- REMARK: 在 /user/api 界面调用 api /fixUserInfoOnceTime 修复原来的用户数据同步状态


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
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES('enterprise_payment_auth','7','zh_CN','企业支付');

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
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'service_module_security', '5', 'zh_CN', '验证码已过期');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'service_module_security', '6', 'zh_CN', '需要安全验证才能访问');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '506', 'zh_CN', '接口参数异常');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '100', 'zh_CN', '员工不存在');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '101', 'zh_CN', '员工已离职');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '102', 'zh_CN', 'token错误');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'enterprise_payment_auth_error', '103', 'zh_CN', '鉴权失败');

-- AUTHOR: 张智伟
-- REMARK: 企业支付授权
UPDATE eh_locale_strings SET text='验证码已过期' WHERE scope='user' AND code='10003';

-- AUTHOR: 黄鹏宇
-- REMARK: fixbug #42303
update eh_var_field_scopes set status = 0 where field_display_name in ('客户级别','资质客户') and module_name = 'enterprise_customer';
-- END

-- AUTHOR: 丁建民
-- REMARK: 合同4.0
SET @id = (SELECT MAX(id) from eh_locale_strings);
INSERT INTO  `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@id:=@id+1), 'contract', '10015', 'zh_CN', '该房源不是待租状态');

-- AUTHOR: 丁建民
-- REMARK: 合同复制权限
SET @id = (SELECT MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges`(`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1 , 21210, 0, 21224, '复制', 0, SYSDATE());
INSERT INTO `eh_acl_privileges`(`id`, `app_id`, `name`, `description`, `tag`) VALUES (21224, 0, '合同管理 合同复制', '合同管理 合同复制权限', NULL);

INSERT INTO `ehcore`.`eh_service_module_functions` (`id`, `module_id`, `privilege_id`, `explain`) VALUES (21224, '21200', '21224', '复制');

-- AUTHOR: 丁建民
-- 合同记录日志
SET @id = (SELECT MAX(id) from eh_locale_templates);
INSERT INTO eh_locale_templates (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '20', 'zh_CN', '合同复制事件', '合同复制，该合同复制于${contractName}', '0');
INSERT INTO eh_locale_templates (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '21', 'zh_CN', '合同初始化事件', '该合同进行了初始化', '0');
INSERT INTO eh_locale_templates (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES (@id:=@id+1, 'contract.tracking', '22', 'zh_CN', '合同免批事件', '该合同已免批', '0');

-- AUTHOR: 杨崇鑫 20181122
-- REMARK: 物业缴费V7.3(账单组规则定义) 新增以下权限：“批量减免”权限；
SET @p_id = 204001011;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, NULL, '批量删除', '账单管理 批量删除', NULL);
SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '204011', '0', @p_id, '批量删除', '0', NOW());

-- AUTHOR: 梁燕龙 20181123
-- REMARK: 个人中心修改可编辑性
UPDATE eh_personal_center_settings SET editable = 1 WHERE name = '订单';
UPDATE eh_personal_center_settings SET editable = 1 WHERE name = '卡券';

-- AUTHOR: 黄明波  20181121
-- REMARK: 添加服务联盟短信模板
SET @module_id = 40500;  -- 模块id
SET @sms_code = 87;      -- sms code, 对应于`com.everhomes.rest.sms.SmsTemplateCode`中的 code
SET @description = '模板1';
SET @display_name = '【app名称】$发起人姓名$（$发起人手机号$）提交了$服务名称$申请，请及时处理';
SET @namespace_id = 0; -- 域空间id, 如果为0, 则相当于配置给所有域空间, 不为0, 则只给特定的域空间配置
SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@locale_templates_id := @locale_templates_id + 1), CONCAT('flow:', @module_id), @sms_code, 'zh_CN', @description, @display_name, @namespace_id);


SET @module_id = 40500;  -- 模块id
SET @sms_code = 88;      -- sms code, 对应于`com.everhomes.rest.sms.SmsTemplateCode`中的 code
SET @description = '模板2';
SET @display_name = '【app名称】你提交的$服务名称$申请正在处理，可在app“我”-“我的申请”中查看处理进度';
SET @namespace_id = 0; -- 域空间id, 如果为0, 则相当于配置给所有域空间, 不为0, 则只给特定的域空间配置
SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@locale_templates_id := @locale_templates_id + 1), CONCAT('flow:', @module_id), @sms_code, 'zh_CN', @description, @display_name, @namespace_id);


INSERT INTO eh_locale_templates (scope, code, locale, description, text, namespace_id) VALUES ( 'sms.default', '87', 'zh_CN', '服务申请推送', '${applierName}（${applierPhone}）提交了${serviceName}申请，请及时处理', '0');    
INSERT INTO eh_locale_templates (scope, code, locale, description, text, namespace_id) VALUES ( 'sms.default', '88', 'zh_CN', '服务申请提醒', '你提交的${serviceName}申请正在处理，可在app“我”-“我的申请”中查看处理进度', '0');


set @classification_id = 0;
-- 普通公司用户价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PriceRules',2,'enterprise',workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type from eh_rentalv2_price_rules;
-- 管理公司员工价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PriceRules',2,'pm_admin',org_member_workday_price,org_member_original_price,org_member_initiate_price,org_member_discount_type,org_member_full_price,org_member_cut_price,org_member_discount_ratio,resource_type from eh_rentalv2_price_rules;
-- 非认证用户价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
 SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PriceRules',2,'park_tourist',approving_user_workday_price,approving_user_original_price,approving_user_initiate_price,approving_user_discount_type,approving_user_full_price,approving_user_cut_price,approving_user_discount_ratio,resource_type from eh_rentalv2_price_rules ;
-- 单元格价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,resource_type)
SELECT @classification_id := @classification_id + 1,rental_resource_id,'resource',id,'EhRentalv2Cells',2,'enterprise',price,original_price,initiate_price,resource_type from eh_rentalv2_cells ;
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,resource_type)
SELECT @classification_id := @classification_id + 1,rental_resource_id,'resource',id,'EhRentalv2Cells',2,'pm_admin',org_member_price,org_member_original_price,org_member_initiate_price,resource_type from eh_rentalv2_cells ;
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,resource_type)
SELECT @classification_id := @classification_id + 1,rental_resource_id,'resource',id,'EhRentalv2Cells',2,'park_tourist',approving_user_price,approving_user_original_price,approving_user_initiate_price,resource_type from eh_rentalv2_cells ;

-- 套餐价格迁移
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PricePackages',2,'enterprise',price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type  from eh_rentalv2_price_packages where owner_type != 'cell' ;
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PricePackages',2,'pm_admin',org_member_price,org_member_original_price,org_member_initiate_price,org_member_discount_type,org_member_full_price,org_member_cut_price,org_member_discount_ratio,resource_type from eh_rentalv2_price_packages where owner_type != 'cell' ;
INSERT INTO eh_rentalv2_price_classification (id,source_id,source_type,owner_id,owner_type,user_price_type,classification,workday_price,original_price,initiate_price,discount_type,full_price,cut_price,discount_ratio,resource_type)
SELECT @classification_id := @classification_id + 1,owner_id,owner_type,id,'EhRentalv2PricePackages',2,'park_tourist',approving_user_price,approving_user_original_price,approving_user_initiate_price,approving_user_discount_type,approving_user_full_price,approving_user_cut_price,approving_user_discount_ratio,resource_type from eh_rentalv2_price_packages where owner_type != 'cell' ;

update eh_rentalv2_holiday set close_date = '1543593600000,1543680000000,1544198400000,1544284800000,1544803200000,1544889600000,1545408000000,1545494400000,1546012800000,1546099200000,1546617600000,1546704000000,1547222400000,1547308800000,1547827200000,1547913600000,1548432000000,1548518400000,1549036800000,1549123200000,1549641600000,1549728000000,1550246400000,1550332800000,1550851200000,1550937600000,1551456000000,1551542400000,1552060800000,1552147200000,1552665600000,1552752000000,1553270400000,1553356800000,1553875200000,1553961600000,1554480000000,1554566400000,1555084800000,1555171200000,1555689600000,1555776000000,1556294400000,1556380800000,1556899200000,1556985600000,1557504000000,1557590400000,1558108800000,1558195200000,1558713600000,1558800000000,1559318400000,1559404800000,1559923200000,1560009600000,1560528000000,1560614400000,1561132800000,1561219200000,1561737600000,1561824000000,1562342400000,1562428800000,1562947200000,1563033600000,1563552000000,1563638400000,1564156800000,1564243200000,1564761600000,1564848000000,1565366400000,1565452800000,1565971200000,1566057600000,1566576000000,1566662400000,1567180800000,1567267200000,1567785600000,1567872000000,1568390400000,1568476800000,1568995200000,1569081600000,1569600000000,1569686400000,1570204800000,1570291200000,1570809600000,1570896000000,1571414400000,1571500800000,1572019200000,1572105600000,1572624000000,1572710400000,1573228800000,1573315200000,1573833600000,1573920000000,1574438400000,1574524800000,1575043200000,1575129600000,1575648000000,1575734400000,1576252800000,1576339200000,1576857600000,1576944000000,1577462400000,1577548800000' where holiday_type = 1;
update eh_rentalv2_holiday set close_date = '1543593600000,1543680000000,1544198400000,1544284800000,1544803200000,1544889600000,1545408000000,1545494400000,1546012800000,1546099200000,1546617600000,1546704000000,1547222400000,1547308800000,1547827200000,1547913600000,1548432000000,1548518400000,1549209600000,1549296000000,1549382400000,1549468800000,1549555200000,1549641600000,1549728000000,1550246400000,1550332800000,1550851200000,1550937600000,1551456000000,1551542400000,1552060800000,1552147200000,1552665600000,1552752000000,1553270400000,1553356800000,1553875200000,1553961600000,1554393600000,1554480000000,1554566400000,1555084800000,1555171200000,1555689600000,1555776000000,1556467200000,1556553600000,1556640000000,1556899200000,1556985600000,1557504000000,1557590400000,1558108800000,1558195200000,1558713600000,1558800000000,1559318400000,1559404800000,1559836800000,1559923200000,1560009600000,1560528000000,1560614400000,1561132800000,1561219200000,1561737600000,1561824000000,1562342400000,1562428800000,1562947200000,1563033600000,1563552000000,1563638400000,1564156800000,1564243200000,1564761600000,1564848000000,1565366400000,1565452800000,1565971200000,1566057600000,1566576000000,1566662400000,1567180800000,1567267200000,1567785600000,1567872000000,1568304000000,1568390400000,1568476800000,1568995200000,1569081600000,1569600000000,1569859200000,1569945600000,1570032000000,1570118400000,1570204800000,1570291200000,1570377600000,1570896000000,1571414400000,1571500800000,1572019200000,1572105600000,1572624000000,1572710400000,1573228800000,1573315200000,1573833600000,1573920000000,1574438400000,1574524800000,1575043200000,1575129600000,1575648000000,1575734400000,1576252800000,1576339200000,1576857600000,1576944000000,1577462400000,1577548800000' where holiday_type = 1;
update eh_locale_templates set text = '您已成功预约了${resourceName}，预订时间：${useTime}，订单编号：${orderNum}。如日程有变，请在预订开始时间前取消订单，感谢您的使用。${aclink}' where `scope` = 'sms.default' and `code` = 30;
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '92', 'zh_CN','工作流 发起申请', '${userName}（${phone}）预约了${resourceName}，使用时间：${useDetail}${freeGoods}${paidGoods}', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '91', 'zh_CN', '临近使用','抱歉，由于您未在规定时间内完成支付，您预约的${resourceName}（${useDetail}）已自动取消，订单编号：${orderNum}，期待下次为您服务', '0');
update eh_locale_templates set text = '您预约的${resourceName}(${useDetail})已成功取消，期待下次为您服务。' where `scope` = 'rental.notification' and `code` = 24;
update eh_locale_templates set text = '尊敬的用户，您预约的${resourceName}(${useDetail})已成功取消，订单金额：${totalAmount}元，退款金额：0元，期待下次为您服务。' where `scope` = 'rental.notification' and `code` = 26;
update eh_locale_templates set text = '尊敬的用户，您预约的${resourceName}(${useDetail})已成功取消，订单编号：${orderNum}，订单金额：${totalAmount}元，退款金额：0元，期待下次为您服务。' where `scope` = 'sms.default' and `code` = 85;
update eh_locale_templates set text = '尊敬的用户，您预约的${resourceName}(${useDetail})已成功取消，订单金额：${totalAmount}元，退款金额：${refundAmount}元，退款将在3个工作日内退至您的原支付账户，期待下次为您服务。' where `scope` = 'rental.notification' and `code` = 27;
update eh_locale_templates set text = '尊敬的用户，您预约的${resourceName}(${useDetail})已成功取消，订单编号：${orderNum}，订单金额：${totalAmount}元，退款金额：${refundAmount}元，退款将在3个工作日内退至您的原支付账户，期待下次为您服务。' where `scope` = 'sms.default' and `code` = 86;
update eh_locale_templates set text = '尊敬的用户，您预约的${resourceName}(${useDetail})已退款成功，订单金额：${totalAmount}元，退款金额：${refundAmount}元，期待下次为您服务。' where `scope` = 'rental.notification' and `code` = 25;
update eh_locale_templates set text = '尊敬的用户，您预约的${resourceName}(${useDetail})已退款成功，订单编号：${orderNum}，订单金额：${totalAmount}元，退款金额：${refundAmount}元，期待下次为您服务。' where `scope` = 'sms.default' and `code` = 84;
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '93', 'zh_CN', '临近使用','您预约的${resourceName}已临近使用时间，预订时间为${useDetail}', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental.notification', '28', 'zh_CN', '临近结束', '您预约的${resourceName}已临近结束时间，预订时间为${useDetail}', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '89', 'zh_CN', '临近结束','您预约的${resourceName}已临近结束时间，预订时间为${useDetail}', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental.notification', '29', 'zh_CN', '临近使用', '${requestorName}(${requestorPhone})预约的${resourceName}已临近使用时间，预订时间为${useDetail}，请做好会前准备', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '90', 'zh_CN', '资源预约发起工作流','${requestorName}(${requestorPhone})发起了${resourceName}的预约申请，请及时核查订单并处理', '0');
SET @module_id = 40400;  -- 模块id
SET @sms_code = 90;      -- sms code, 对应于`com.everhomes.rest.sms.SmsTemplateCode`中的 code
SET @description = '用户发起申请';
SET @display_name = '用户发起申请';
SET @namespace_id = 0; -- 域空间id, 如果为0, 则相当于配置给所有域空间, 不为0, 则只给特定的域空间配置
SET @locale_templates_id = IFNULL((SELECT MAX(id) FROM `eh_locale_templates`), 0);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES ((@locale_templates_id := @locale_templates_id + 1), CONCAT('flow:', @module_id), @sms_code, 'zh_CN', @description, @display_name, @namespace_id);

-- AUTHOR:tangcen 2018年11月12日14:31:01
-- REMARK:修改楼宇资产管理模块配置
UPDATE eh_service_modules SET instance_config='{\"url\":\"${home.url}/assets-web/build/index.html?hideNavigationBar=1#/home/#sign_suffix\"}' WHERE id=38000;

UPDATE eh_service_modules SET action_type=14 WHERE id=38000;

UPDATE eh_service_modules SET client_handler_type=2 WHERE id=38000;

-- AUTHOR: 孟千翔 2018-11-25
-- REMARK: issue-42274 添加交易明细导出表头及数据标签信息
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","14001","zh_CN","账单时间");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12002","zh_CN","账单组");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12003","zh_CN","收费项信息");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12004","zh_CN","客户名称");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12005","zh_CN","客户类型");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12006","zh_CN","订单状态");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12007","zh_CN","支付方式");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12008","zh_CN","实收金额");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12009","zh_CN","应收金额");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12010","zh_CN","减免");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12011","zh_CN","增收");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","13012","zh_CN","订单编号");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12013","zh_CN","缴费时间");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12014","zh_CN","缴费人电话");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","12015","zh_CN","缴费人");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","14016","zh_CN","楼栋门牌");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","20001","zh_CN","个人客户");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","20002","zh_CN","企业客户");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","20003","zh_CN","已完成");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","20004","zh_CN","微信");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","20005","zh_CN","支付宝");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","20006","zh_CN","对公转账");
INSERT INTO eh_locale_strings (`scope`,`code`,`locale`,`text`) VALUES("export.paymentbill","30001","zh_CN","交易明细");


-- AUTHOR: 张智伟
-- REMARK: issue-37379 工作汇报时间格式化
SET @locale_strings_id = IFNULL((SELECT MAX(id) FROM `eh_locale_strings`), 1);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '1', 'zh_CN', '今天');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '2', 'zh_CN', '明天');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '11', 'zh_CN', '本周一');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '12', 'zh_CN', '本周二');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '13', 'zh_CN', '本周三');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '14', 'zh_CN', '本周四');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '15', 'zh_CN', '本周五');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '16', 'zh_CN', '本周六');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '17', 'zh_CN', '本周日');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '101', 'zh_CN', '下周一');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '102', 'zh_CN', '下周二');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '103', 'zh_CN', '下周三');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '104', 'zh_CN', '下周四');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '105', 'zh_CN', '下周五');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '106', 'zh_CN', '下周六');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@locale_strings_id := @locale_strings_id + 1), 'time_display_format', '107', 'zh_CN', '下周日');

-- AUTHOR: xq.tian
-- REMARK: 新增跟踪变量
SET @eh_flow_variables_id = (SELECT MAX(id) FROM eh_flow_variables);
INSERT INTO eh_flow_variables (id, namespace_id, owner_id, owner_type, module_id, module_type, name, label, var_type, script_type, script_cls, status)
  VALUES ((@eh_flow_variables_id := @eh_flow_variables_id + 1), 0, 0, '', 0, '', 'text_button_tracker_input_content', '文本备注内容', 'text_tracker', 'bean_id', 'flow-variable-text-button-msg-user-input-content', 1);

update eh_locale_strings set text='当前流程未经过预设待驳回节点。' where scope='flow' and code='100015';



-- AUTHOR: 黄明波
-- REMARK: 修复云打印有误链接
update eh_service_modules set instance_config = '{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}' where id = 41400 ;

update eh_service_module_apps set instance_config = replace (instance_config, '#/home#sign_suffix', concat('?namespaceId=', namespace_id, '#/home#sign_suffix')) where module_id = 41400 and  instance_config not like '%?namespaceId=%';

update eh_launch_pad_items set action_data = replace (action_data, '#/home#sign_suffix', concat('?namespaceId=', namespace_id, '#/home#sign_suffix')) where action_data like '%/cloud-print/build/index.html%' and  action_data not like '%?namespaceId=%';

-- AUTHOR: 杨崇鑫
-- REMARK: 缺陷 #42424 【智谷汇】保证金设置为固定金额，但是实际会以合同签约门牌的数量计价。实际上保证金是按照合同收费，不是按照门牌的数量进行重复计费。 给智谷汇的权限
INSERT INTO `eh_service_module_functions`(`id`, `module_id`, `privilege_id`, `explain`) VALUES (21225, 21200, 21225, '保证金一次性产生一笔费用');
-- AUTHOR: 杨崇鑫
-- REMARK: 缺陷 #42424 只是临时开给智谷汇这个域空间使用
set @id=(select ifnull((SELECT max(id) from eh_service_module_include_functions),1));
INSERT INTO `eh_service_module_include_functions`(`id`, `namespace_id`, `module_id`, `community_id`, `function_id`) VALUES (@id:= @id +1, 999945, 21200, NULL, 21225);


-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:

-- AUTHOR:黄明波
-- REMARK:添加打印机域空间id
update eh_siyin_print_printers set namespace_id = 2 where reader_name = 'TC101154727022';
update eh_siyin_print_printers set namespace_id = 999969 where reader_name = 'TC101154727294';
update eh_siyin_print_printers set namespace_id = 11 where reader_name = 'TC101157736913';
update eh_siyin_print_printers set namespace_id = 999981 where reader_name = 'TC100887870538';



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

-- AUTHOR: 黄明波
-- REMARK: 商品对接
update eh_siyin_print_printers set namespace_id = 999930 ;

-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本

-- AUTHOR: st.zheng 20181127
-- REMARK: 会员等级
SET @id = (SELECT IFNULL(MIN(id),0) from `eh_vip_priority`);
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,1,'银卡',10 );
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,2,'金卡',20 );
INSERT INTO eh_vip_priority(id, namespace_id, vip_level, vip_level_text, priority)
VALUES (@id := @id + 1,999929,3,'白金卡',30 );

-- AUTHOR: 黄明波
-- REMARK: 商品对接
update eh_siyin_print_printers set namespace_id = 999929 ;


-- --------------------- SECTION END ruianxintiandi ------------------------------------------

-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本




-- --------------------- SECTION END wanzhihui ------------------------------------------
