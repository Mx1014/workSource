-- 资源预订工作流模板，add by wh, 20161219
SET @id := (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
VALUES (@id:=@id+1, 'rental.flow', 2, 'zh_CN', '自定义字段', '请等待${offlinePayeeName}（${offlinePayeeContact}）上门收费，或者到${offlineCashierAddress}去支付', 0);

-- 资源预订短信模板，add by wh, 20161219
SET @id =(SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','28','zh_CN','资申成-正中会','38570','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','29','zh_CN','资申败-正中会','38572','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','30','zh_CN','资付成-正中会','38573','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','31','zh_CN','资预败-正中会','38574','999983');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id:=@id+1),'sms.default.yzx','32','zh_CN','资取消-正中会','38575','999983');

-- 文案早上改成上午
UPDATE eh_locale_strings SET TEXT = '上午' WHERE scope ='rental.notification' AND CODE = 0 AND locale = 'zh_CN';

-- 推送模板
SET @id = (SELECT MAX(id) FROM eh_locale_templates);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','3','zh_CN','申请成功','您申请预约的${useTime}的${resourceName}已通过审批，为确保您成功预约，请尽快完成支付，支付方式支持：1. 请联系${offlinePayeeName}（${offlinePayeeContact}）上门收费，2. 到${offlineCashierAddress}付款；感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','4','zh_CN','申请失败','您申请预约的${useTime}的${resourceName}没有通过审批，您可以申请预约其他空闲资源，由此给您造成的不便，敬请谅解，感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','5','zh_CN','支付成功','您已完成支付，成功预约${useTime}的${resourceName}，请按照预约的时段使用资源，感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','6','zh_CN','预约失败','您申请预约的${useTime}的${resourceName}已经被其他客户抢先预约成功，您可以继续申请预约其他时段，由此给您造成的不便，敬请谅解，感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','7','zh_CN','取消短信','您申请预约的${useTime}的${resourceName}由于超时未支付或被其他客户抢先预约，已自动取消，由此给您造成的不便，敬请谅解，感谢您的使用。','0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES((@id := @id+1),'rental.notification','8','zh_CN','催办','客户（${userName}${userPhone}）提交资源预约的线下支付申请，预约${resourceName}，使用时间：${useTime}，订单金额${price}，请尽快联系客户完成支付','0');

-- 工作流信息 janson
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10007, 'zh_CN', '${applierName} 已取消任务', '${applierName} 已取消任务');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10008, 'zh_CN', '发起人已取消任务', '发起人已取消任务');
INSERT INTO `eh_locale_templates`(`scope`, `code`,`locale`, `description`, `text`) VALUES
( 'flow', 10009, 'zh_CN', '任务已完成', '任务已完成');

-- 更新科技园物业报修oa 地址 add by sw 20170315
UPDATE `eh_configurations` SET `value`='http://oa.ssipc.com.cn:7890/oa/service/WorkflowAppDraftWebService?wsdl' WHERE `name`='techpark.oa.url';



-- 更新资源类型:特别注意如果这里更改rows = 0 说明id不对,就不能执行下一句
UPDATE eh_rentalv2_resource_types SET pay_mode = 1 WHERE namespace_id = 999983 AND NAME = '会议室预订' AND id =10505;
-- 前一条执行好了执行这一条
UPDATE eh_launch_pad_items  SET action_data ='{"resourceTypeId":10505,"pageType":0,"payMode":1}' WHERE namespace_id = 999983 AND item_label='会议室预订'; 

-- 添加资源预约工作流的菜单
SET @id = (SELECT MAX(id) FROM eh_web_menu_scopes );
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES((@id:=@id+1),'40450','','EhNamespaces','999983','2');

-- 俱乐部3.0  add by xq.tian  2017/03/01
UPDATE `eh_locale_templates` SET `text`='俱乐部推荐：${groupName}' WHERE `scope`='group.notification' AND `code`=33;

-- 更新深业物业报修 工作流 add by sw 20170317
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) 
	VALUES ('pmtask.handler-999992', 'flow', '', '0', NULL);
DELETE from eh_web_menu_scopes where menu_id = 20160 and owner_id = 999992;
DELETE from eh_web_menu_scopes where menu_id = 20192 and owner_id = 999992;

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 20158, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70000, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70100, '', 'EhNamespaces', 999992, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`)
	VALUES ((@menu_scope_id := @menu_scope_id + 1), 70200, '', 'EhNamespaces', 999992, 2);
	
SET @eh_launch_pad_items = (SELECT MAX(id) FROM `eh_launch_pad_items`);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`)
	VALUES ((@eh_launch_pad_items := @eh_launch_pad_items + 1), '999992', '0', '0', '0', '/home', 'Bizs', 'FLOW_TASKS', '任务管理', 'cs://1/image/aW1hZ2UvTVRvd09HUTNNRGs1TURZeVpUTmhNbU5pWkdWbVpXUmhNMlU1T1RSaE4yTTFNZw', '1', '1', '56', '', '0', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '1', NULL);	
update eh_launch_pad_items set action_type = 60, action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=提交服务"}' where item_label = '物业报修' and namespace_id = 999992;
update eh_launch_pad_items set action_type = 60, action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=提交服务"}' where namespace_id = 999992 and action_type = 51;


-- 物业缴费3.0 by xiongying 20170320
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('1', '0', '1', '1', 'PM', '0', 'community', '0', 'accountPeriod', '账期', NULL, NULL, '0', 'Timestamp');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('2', '0', '1', '1', 'PM', '0', 'community', '0', 'buildingName', '楼栋', NULL, NULL, '0', 'String');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('3', '0', '1', '1', 'PM', '0', 'community', '0', 'apartmentName', '门牌号', NULL, NULL, '0', 'String');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('4', '0', '1', '1', 'PM', '0', 'community', '0', 'contactNo', '催缴手机号', NULL, NULL, '0', 'String');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('5', '0', '0', '1', 'PM', '0', 'community', '0', 'rental', '租金', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('6', '0', '0', '1', 'PM', '0', 'community', '0', 'propertyManagementFee', '物业管理费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('7', '0', '0', '1', 'PM', '0', 'community', '0', 'unitMaintenanceFund', '本体维修基金', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('8', '0', '0', '1', 'PM', '0', 'community', '0', 'lateFee', '滞纳金', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('9', '0', '0', '1', 'PM', '0', 'community', '0', 'privateWaterFee', '自用水费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('10', '0', '0', '1', 'PM', '0', 'community', '0', 'privateElectricityFee', '自用电费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('11', '0', '0', '1', 'PM', '0', 'community', '0', 'publicWaterFee', '公共部分水费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('12', '0', '0', '1', 'PM', '0', 'community', '0', 'publicElectricityFee', '公共部分电费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('13', '0', '0', '1', 'PM', '0', 'community', '0', 'wasteDisposalFee', '垃圾处理费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('14', '0', '0', '1', 'PM', '0', 'community', '0', 'pollutionDischargeFee', '排污处理费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('15', '0', '0', '1', 'PM', '0', 'community', '0', 'extraAirConditionFee', '加时空调费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('16', '0', '0', '1', 'PM', '0', 'community', '0', 'coolingWaterFee', '冷却水使用费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('17', '0', '0', '1', 'PM', '0', 'community', '0', 'weakCurrentSlotFee', '弱电线槽使用费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('18', '0', '0', '1', 'PM', '0', 'community', '0', 'depositFromLease', '租赁保证金', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('19', '0', '0', '1', 'PM', '0', 'community', '0', 'maintenanceFee', '维修费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('20', '0', '0', '1', 'PM', '0', 'community', '0', 'gasOilProcessFee', '燃气燃油加工费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('21', '0', '0', '1', 'PM', '0', 'community', '0', 'hatchServiceFee', '孵化服务费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('22', '0', '0', '1', 'PM', '0', 'community', '0', 'pressurizedFee', '加压费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('23', '0', '0', '1', 'PM', '0', 'community', '0', 'parkingFee', '停车费', NULL, NULL, '0', 'BigDecimal');
INSERT INTO `eh_asset_bill_template_fields` (`id`, `namespace_id`, `required_flag`, `selected_flag`, `owner_type`, `owner_id`, `target_type`, `target_id`, `field_name`, `field_display_name`, `field_custom_name`, `default_order`, `template_version`, `field_type`) VALUES ('24', '0', '0', '1', 'PM', '0', 'community', '0', 'other', '其他', NULL, NULL, '0', 'BigDecimal');


 
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('asset', '10001', 'zh_CN', '账单不存在');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('asset', '10002', 'zh_CN', '生成excel信息有问题');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('asset', 'asset.notify.fee', 'zh_CN', '您有待缴的物业账单，请尽快完成缴费（点击查看账单详情）。');


-- 添加菜单
SET @menu_id = (SELECT MAX(id) FROM `eh_web_menus`);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`) 
VALUES ((@menu_id := @menu_id + 1), '缴费管理', '20000', NULL, 'react:/property-service/payment-management', '1', '2', '/20000/', 'park', '458', NULL);

SET @acl_privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) 
VALUES ((@acl_privilege_id := @acl_privilege_id + 1), 0, '缴费管理', '缴费管理 全部权限', NULL);

SET @web_menu_privilege_id = (SELECT MAX(id) FROM `eh_web_menu_privileges`);
INSERT INTO `eh_web_menu_privileges` (`id`, `privilege_id`, `menu_id`, `name`, `show_flag`, `status`, `discription`, `sort_num`) 
VALUES ((@web_menu_privilege_id := @web_menu_privilege_id + 1), @acl_privilege_id, @menu_id, '缴费管理', 1, 1, '缴费管理  全部权限', 458); 

SET @menu_scope_id = (SELECT MAX(id) FROM `eh_web_menu_scopes`);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) 
VALUES ((@menu_scope_id := @menu_scope_id + 1), @menu_id, '', 'EhNamespaces', 999985, 2);

SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @acl_privilege_id, 1005, 'EhAclRoles', 0, 1, NOW()); 
SET @acl_id = (SELECT MAX(id) FROM `eh_acls`);
INSERT INTO `eh_acls` (`id`, `namespace_id`, `owner_type`, `owner_id`, `grant_type`, `privilege_id`, `role_id`, `role_type`, `order_seq`, `creator_uid`, `create_time`)
VALUES ((@acl_id := @acl_id + 1), 0, 'EhOrganizations', NULL, 1, @acl_privilege_id, 1001, 'EhAclRoles', 0, 1, NOW()); 





-- 更新星尚汇服务市场投诉建议为物业服务，关联任务7649，add by tt, 20170320 
update eh_launch_pad_items set item_name = '物业服务', item_label = '物业服务', action_data = '{"url":"zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=物业服务"}' where namespace_id = 999981 and item_label = '投诉建议';

-- 服务联盟2.2 add by sw 20170320
SET @id = (SELECT MAX(id) FROM `eh_service_alliance_jump_module`);
INSERT INTO `eh_service_alliance_jump_module` (`id`, `namespace_id`, `module_name`, `module_url`, `parent_id`) 
	VALUES ((@id := @id + 1), '999985', '电商', 'BIZS', '0');
	
-- 威新短信模版 add by sw 20170320
INSERT INTO `eh_locale_templates`(`namespace_id`, `scope`, `code`,`locale`, `description`, `text`)
	VALUES (999991, 'sms.default.yzx', 34, 'zh_CN', '物业报修短信', '38833');

    
-- 去掉华润菜单物业缴费 add by xiongying 20170320
delete from eh_web_menu_scopes where menu_id = 20400 and owner_id = 999985 and owner_type = 'EhNamespaces';

-- 添加科技园大堂门禁菜单，add by tt, 20170321
select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41000, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41010, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41020, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41030, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41040, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41050, '', 'EhNamespaces', 1000000, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 41060, '', 'EhNamespaces', 1000000, 2);

select max(id) into @id from `eh_launch_pad_items`;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 0, 0, '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 4, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 1, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 0, 0, '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 4, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'default', 1, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 1000000, 0, 0, 0, '/home', 'Bizs', '门禁', '门禁', 'cs://1/image/aW1hZ2UvTVRwalptSm1PRFE1T1dObVpqTXdZelZqWVRNME56azBNelk1TkdNNU1UUm1Zdw', 1, 1, 40, '{"isSupportQR":1,"isSupportSmart":1}', 4, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 1, NULL, NULL);


-- 配置ufine会议室预订，add by tt, 20170321
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (61, '会议室预订', 0, NULL, 0, 999990);
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`) VALUES (60, '嗒嗒会议室', 0, NULL, 0, 999990);

select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40400, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40410, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40420, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40430, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40440, '', 'EhNamespaces', 999990, 2);
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id:=@id+1, 40450, '', 'EhNamespaces', 999990, 2);

select max(id) into @id from `eh_launch_pad_items`;
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 4, 1006090, '/home', 'Bizs', 'MEETINGROOM', '嗒嗒会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', 1, 1, 49, '{"resourceTypeId":60,"pageType":0}', 3, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'park_tourist', 0, NULL, NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`) VALUES (@id:=@id+1, 999990, 0, 4, 1006090, '/home', 'Bizs', 'MEETINGROOM', '嗒嗒会议室', 'cs://1/image/aW1hZ2UvTVRvME5HVTNZVEZsTXpNeU16VXhNbVF3Wm1GbU9UUTBPV0ZoTUdRNFpUSmpaQQ', 1, 1, 49, '{"resourceTypeId":60,"pageType":0}', 3, 0, 1, 1, '', 0, NULL, NULL, NULL, 1, 'pm_admin', 0, NULL, NULL);

-- 添加新的服务类型，并更改banner跳转链接（现网已执行）, add by tt, 20170322
SET @eh_service_alliance_categories = (SELECT max(id) FROM eh_service_alliance_categories);
INSERT INTO `eh_service_alliance_categories` (`id`, `owner_type`, `owner_id`, `parent_id`, `name`, `path`, `default_order`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`, `namespace_id`, `logo_url`)
    VALUES ((@eh_service_alliance_categories := @eh_service_alliance_categories + 1), 'community', '240111044331051500', '0', '十乐生活合作介绍', '十乐生活合作介绍', '0', '2', '1', UTC_TIMESTAMP(), '0', NULL, '999990', '');
SET @sa_id = (SELECT max(id) FROM `eh_service_alliances`);    
INSERT INTO `eh_service_alliances` (`id`, `parent_id`, `owner_type`, `owner_id`, `name`, `display_name`, `type`, `address`, `contact`, `description`, `poster_uri`, `status`, `default_order`, `longitude`, `latitude`, `geohash`, `discount`, `category_id`, `contact_name`, `contact_mobile`, `service_type`, `service_url`, `discount_desc`, `integral_tag1`, `integral_tag2`, `integral_tag3`, `integral_tag4`, `integral_tag5`, `string_tag1`, `string_tag2`, `string_tag3`, `string_tag4`, `string_tag5`, `creator_uid`, `create_time`) 
    VALUES ((@sa_id := @sa_id + 1), '0', 'community', '240111044331051500', '十乐生活合作介绍', '十乐生活合作介绍', @eh_service_alliance_categories, '', NULL, '', '', '2', NULL, NULL, NULL, '', NULL, NULL, '', '', '', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);


SET @eh_service_alliance_skip_rule = (SELECT max(id) FROM `eh_service_alliance_skip_rule`);
INSERT INTO `eh_service_alliance_skip_rule` (`id`, `namespace_id`, `service_alliance_category_id`) VALUES ((@eh_service_alliance_skip_rule := @eh_service_alliance_skip_rule + 1), '999990', @eh_service_alliance_categories);
update eh_banners set action_type = 33 where name in("十乐生活合作预告") and namespace_id = 999990;
update eh_banners set action_data = concat('{"type":',@eh_service_alliance_categories,',"parentId":',@eh_service_alliance_categories,',"displayType": "list"}') where name = "十乐生活合作预告" and namespace_id = 999990;



-- 修改正中会"餐饮"为跳转链接形式
update eh_launch_pad_items set action_type=14, action_data='{"url":"https://biz.zuolin.com/zl-ec/rest/service/front/logon?hideNavigationBar=1&sourceUrl=https%3A%2F%2Fbiz.zuolin.com%2Fnar%2Fbiz%2Fweb%2Fapp%2Fuser%2Findex.html%3Fisfromindex%3D0%23%2Fmicroshop%2Fhome%3F_k%3Dzlbiz#sign_suffix"}' where namespace_id=999983 and item_name='餐饮';

-- 服务联盟 add by sw 20170321
update eh_configurations set `value` = '/service-alliance/index.html?hideNavigationBar=1#/service_detail/%s/%s?_k=%s&ownerType=%s&ownerId=%s' where name = 'serviceAlliance.serviceDetail.url';

-- 服务联盟 表单 add by sw 20170322
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
	VALUES (20, 'Golf', '高尔夫', '高尔夫', 1, 1, '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"userName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"mobile","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业","fieldType":"string","fieldContentType":"text","fieldDesc":"organizationName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationFloor","fieldDisplayName":"公司楼层","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入公司所在楼层","requiredFlag":"1"}]}', 1, 1, UTC_TIMESTAMP(), 0, NULL);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
	VALUES (21, 'Gym', '健身会所', '健身会所', 1, 1, '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"userName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"mobile","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业","fieldType":"string","fieldContentType":"text","fieldDesc":"organizationName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"profession","fieldDisplayName":"职称","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入职称名","requiredFlag":"1"}]}', 1, 1, UTC_TIMESTAMP(), 0, NULL);
INSERT INTO `eh_request_templates` (`id`, `template_type`, `name`, `button_title`, `email_flag`, `msg_flag`, `fields_json`, `status`, `creator_uid`, `create_time`, `delete_uid`, `delete_time`) 
	VALUES (22, 'Server', '企业服务', '企业服务', 1, 1, '{"fields":[{"fieldName":"name","fieldDisplayName":"姓名","fieldType":"string","fieldContentType":"text","fieldDesc":"userName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"mobile","fieldDisplayName":"联系电话","fieldType":"string","fieldContentType":"text","fieldDesc":"mobile","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"organizationName","fieldDisplayName":"企业","fieldType":"string","fieldContentType":"text","fieldDesc":"organizationName","requiredFlag":"1","dynamicFlag":"1"},{"fieldName":"email","fieldDisplayName":"电子邮箱","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入邮箱地址","requiredFlag":"1"},{"fieldName":"destination","fieldDisplayName":"目的地","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入需前往目的地","requiredFlag":"1"},{"fieldName":"departureCity","fieldDisplayName":"出发城市","fieldType":"string","fieldContentType":"text","fieldDesc":"请输入出发城市","requiredFlag":"1"},{"fieldName":"departureDate","fieldDisplayName":"出行日期","fieldType":"number","fieldContentType":"text","fieldDesc":"例20170808","requiredFlag":"1"},{"fieldName":"departureDays","fieldDisplayName":"出行天数","fieldType":"number","fieldContentType":"text","fieldDesc":"请输入出行天数","requiredFlag":"1"},{"fieldName":"estimatedCost","fieldDisplayName":"预算费用","fieldType":"number","fieldContentType":"text","fieldDesc":"单位：人名币","requiredFlag":"1"}]}', 1, 1, UTC_TIMESTAMP(), 0, NULL);

--
-- 短信发送次数校验配置   add by xq.tian  2017/03/22
--
UPDATE `eh_configurations` SET `value`='60' WHERE `name`='sms.verify.minDuration.seconds';
UPDATE `eh_configurations` SET `value`='10' WHERE `name`='sms.verify.device.timesForAnHour';
UPDATE `eh_configurations` SET `value`='20' WHERE `name`='sms.verify.device.timesForADay';


-- 更新现网菜单“我的申请”的data_type（现网已执行）, add by tt, 20170323
update eh_web_menus set data_type = replace(data_type, 'service', 'apply') where id in (80120, 80220, 80320, 80420, 80520);


-- 
-- 删除正中会菜单（现网已执行）, add by tt, 20170323
-- 1，删除运营服务及子菜单
-- 2，删除内部管理的岗位管理和职级管理
-- 3，系统管理下新增子菜单：管理员管理
-- 4，删除园区服务下的厂房出租、公寓出租、医疗
-- 5，删除园区服务-企业服务下的我的申请子菜单
-- 
delete from eh_acls where role_id = 1005 and privilege_id = 10145;

delete from eh_web_menu_scopes where owner_type = 'EhNamespaces' and owner_id = 999983 and menu_id in (50200, 50210, 50220, 50300, 80300, 80310, 80320, 80400, 80410,80420, 80500, 80510,80520, 80220);

select max(id) into @id from `eh_web_menu_scopes`;
INSERT INTO `eh_web_menu_scopes` (`id`, `menu_id`, `menu_name`, `owner_type`, `owner_id`, `apply_policy`) VALUES (@id+1, 60400, '', 'EhNamespaces', 999983, 2);

-- 更新物业报修工作流 add by sw 20170323
UPDATE eh_flow_cases set owner_id = project_id where owner_type = 'PMTASK';
UPDATE eh_flow_evaluates set owner_id = project_id where owner_type = 'PMTASK';
UPDATE eh_flows set owner_id = project_id where owner_type = 'PMTASK';