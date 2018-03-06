-- 滞纳金变量 by wentian
SET @var_id = (SELECT MAX(`id`) FROM `eh_payment_variables`);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@var_id:=@var_id+1, NULL, '6', '欠费', '0', '2017-10-16 09:31:00', NULL, '2017-10-16 09:31:00', 'qf');

--  标准数据增加周期类型及关系表状态start by jiarui 20180105
UPDATE eh_equipment_inspection_standards
SET repeat_type = (SELECT repeat_type FROM eh_repeat_settings WHERE id = eh_equipment_inspection_standards.repeat_setting_id)
WHERE STATUS =2  AND repeat_type =0;

-- 上版未置状态数据修改
UPDATE eh_equipment_inspection_equipment_standard_map
SET `status` = 0
WHERE review_status IN (0, 3 ,4) OR review_result = 2;
UPDATE eh_equipment_inspection_equipment_standard_map
SET `status` = 0
WHERE id NOT IN (SELECT id FROM (SELECT MIN(id)  AS id, COUNT(target_id) AS COUNT FROM eh_equipment_inspection_equipment_standard_map WHERE `status` = 1 GROUP BY target_id, standard_id HAVING COUNT(target_id) >= 1) tmp);
-- 上版bug数据修改

--  标准数据增加周期类型 及关系表状态 end by jiarui 20180105
-- 巡检任务状态统一 start by jiarui 20180105
UPDATE eh_equipment_inspection_tasks
SET `status` = 6
WHERE `status` = 4 AND review_result = 1;

UPDATE eh_equipment_inspection_tasks
SET `status` = 7
WHERE `status` = 4 AND review_result = 4;

UPDATE eh_equipment_inspection_task_logs
SET equipment_id = (SELECT  equipment_id FROM eh_equipment_inspection_tasks WHERE id = task_id);

-- 巡检任务状态统一 end by jiarui 20180105

-- 增加经纬度动态表单  jiarui   20180122
INSERT  INTO  `eh_var_fields` VALUES (11999, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10000, CONCAT('/',10000,'/'), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"map\", \"length\": 32}');
-- 增加经纬度动态表单  jiarui  20180122

-- offline  by jiarui
SET  @id = (SELECT  MAX(id) FROM eh_version_realm);
INSERT INTO `eh_version_realm` VALUES (@id:=@id+1, 'equipmentInspection', NULL, NOW(), '0');

SET  @vId = (SELECT  MAX(id) FROM eh_version_urls);
INSERT INTO `eh_version_urls` VALUES (@vId:=@vId+1, @id, '1.0.0', 'http://opv2-test.zuolin.com/nar/equipmentInspection/inspectionOffLine/equipmentInspection-1-0-0.zip', 'http://opv2-test.zuolin.com/nar/equipmentInspection/inspectionOffLine/equipmentInspection-1-0-0.zip', '物业巡检巡检离线', '0', '物业巡检', NOW(), NULL, '0');

UPDATE eh_launch_pad_items
SET action_data = '{\"realm\":\"equipmentInspection\",\"entryUrl\":\"http://opv2-test.zuolin.com/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}'
WHERE item_label LIKE '%巡检%';
-- offline  by jiarui

UPDATE eh_launch_pad_items
SET action_type = 44
WHERE item_label LIKE '%巡检%';

-- 新增权限  by jiarui 20180205

DELETE FROM  eh_service_module_privileges
WHERE  privilege_id IN (30070,30076,30071,30077,30078,30079,30082);

UPDATE eh_service_modules
SET NAME = '计划管理'
WHERE id = 20840;
UPDATE eh_service_modules
SET NAME = '台帐管理'
WHERE id = 20820;
UPDATE eh_service_modules
SET NAME = '任务管理'
WHERE id = 20830;
UPDATE eh_service_modules
SET NAME = '统计分析'
WHERE id = 20850;
UPDATE eh_service_modules
SET NAME = '标准管理'
WHERE id = 20810;

INSERT INTO `eh_acl_privileges` VALUES ('30083', '0', '设备巡检 巡检计划创建', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` VALUES ('30084', '0', '设备巡检 巡检计划修改', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` VALUES ('30085', '0', '设备巡检 巡检计划查看', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` VALUES ('30086', '0', '设备巡检 巡检计划删除', '设备巡检 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` VALUES ('30087', '0', '设备巡检 巡检计划审批', '设备巡检 业务模块权限', NULL);

SET @mp_id = (SELECT MAX(id) FROM eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30083, '设备巡检 巡检计划创建', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30084, '设备巡检 巡检计划修改', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30085, '设备巡检 巡检计划查看', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30086, '设备巡检 巡检计划删除', '0', NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES (@mp_id:=@mp_id+1, '20840', '0', 30087, '设备巡检 巡检计划审批', '0', NOW());

-- 新增权限  by jiarui 20180205


-- 品质核查操作日志 jiarui
UPDATE  `eh_quality_inspection_logs`
SET scope_id =(SELECT eh_quality_inspection_standards.target_id FROM  eh_quality_inspection_standards WHERE eh_quality_inspection_standards.id = eh_quality_inspection_logs.target_id);

-- 品质核查操作日志 jiarui

--  重命名品质核查权限项名称  jiarui
UPDATE eh_service_modules
SET name = '计划管理'
WHERE id = 20630;
UPDATE eh_service_modules
SET name = '计划审批'
WHERE id = 20640;
UPDATE eh_service_modules
SET name = '日志管理'
WHERE id = 20670;
UPDATE  eh_service_modules
SET name ='任务管理'
WHERE id = 20650;
UPDATE  eh_service_modules
SET name ='统计分析'
WHERE id = 20660;

UPDATE eh_service_module_privileges
SET remark = REPLACE(remark,'标准','计划')
where id in (120,121,122,123,125);

UPDATE  eh_acl_privileges
SET name = '品质核查 考核统计查看权限'
WHERE id = 30066;

UPDATE   eh_service_module_privileges
SET remark = '品质核查 考核统计查看权限'
WHERE  privilege_id = 30066;

-- add by sw 20180122 vip车位
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`, `menu_type`, `identify`)
	VALUES ('12500', 'VIP车位预约', '0', NULL, '2', '1000000', '0', '0', '1', 'vip_parking');

UPDATE eh_rentalv2_price_rules SET user_price_type = 1;

UPDATE eh_rentalv2_price_packages SET user_price_type = 1;

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '13', 'zh_CN', '用户取消订单推送消息', '订单取消通知：您的${resourceTypeName}订单已成功取消。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '14', 'zh_CN', '订单超时取消通知', '由于您未在15分钟内完成支付，您预约的${useDetail}已自动取消，期待下次为您服务。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '15', 'zh_CN', '订单支付通知', '尊敬的用户，您预约的${useDetail}已成功提交，您可以在预约时间内控制车位锁以使用车位（地址：${spaceAddress}），如需延时，请在预约结束时间前提交申请，否则超时将产生额外费用，感谢您的谅解。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '16', 'zh_CN', '用户取消订单退款推送消息', '尊敬的用户，您预约的${useDetail}已退款成功，订单金额：${totalAmount}元，退款金额：${refundAmount}元，期待下次为您服务。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '17', 'zh_CN', '延时成功 ', '尊敬的用户，您预约的${useDetail}已成功延时到${newEndTime}，感谢您的使用。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '18', 'zh_CN', '订单欠费通知 ', '尊敬的用户，您预约的${useDetail}由于超时使用产生欠费，欠费金额：${unPaidAmount}元，请前往订单详情完成支付，否则将影响下次使用。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '19', 'zh_CN', '订单完成通知 ', '尊敬的用户，您预约的${useDetail}已完成，本次服务金额：${totalAmount}元，感谢您的使用。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '20', 'zh_CN', '系统自动取消订单 ', '尊敬的用户，您预约的${useDetail}由于前序订单使用超时，且无其他空闲车位可更换，已自动取消并全额退款，为此我们深感抱歉，期待下次为您服务。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '21', 'zh_CN', '订单变更通知 ', '尊敬的用户，您预约的${useDetail}由于前序订单使用超时，系统自动为您更换至${spaceNo}车位，给您带来的不便我们深感抱歉，感谢您的使用。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('rental.notification', '22', 'zh_CN', '资源订单使用详情 ', 'VIP车位（${parkingLotName}${spaceNo}车位：${startTime} - ${endTime}）', '0');


INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '58', 'zh_CN', '订单超时取消通知', '由于您未在15分钟内完成支付，您预约的${useDetail}已自动取消，期待下次为您服务。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '59', 'zh_CN', '订单支付通知', '尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）已为您成功预约${useDetail}，请在该时间内前往指定车位（地址：${spaceAddress}），并点击以下链接使用：${orderDetailUrl}谢谢。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '60', 'zh_CN', '订单取消车主通知', '尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）已取消为您预约的${useDetail}，期待下次为您服务。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '61', 'zh_CN', 'vip车位预约用户车锁升起', '尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）已取消为您预约的${useDetail}，期待下次为您服务。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '62', 'zh_CN', '延时成功', '尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）已为您将预约的${useDetail}延时到${newEndTime}，请点击以下链接使用：${orderDetailUrl}，感谢您的使用。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '63', 'zh_CN', '即将超时', '尊敬的用户，您预约的${useDetail}剩余使用时长：15分钟，如需延时，请前往APP进行操作，否则超时系统将继续计时计费，感谢您的使用。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '64', 'zh_CN', '系统自动取消订单', '尊敬的用户，您预约的${useDetail}由于前序订单使用超时，且无其他空闲车位可更换，已自动取消并全额退款，为此我们深感抱歉，期待下次为您服务。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '65', 'zh_CN', '订单变更通知', '尊敬的用户，您预约的${useDetail}由于前序订单使用超时，系统自动为您更换至${spaceNo}车位，给您带来的不便我们深感抱歉，感谢您的使用。', '0');

INSERT INTO `eh_locale_templates` (`scope`, `code`, `locale`, `description`, `text`, `namespace_id`)
	VALUES ('sms.default', '66', 'zh_CN', '订单变更通知', '尊敬的${plateOwnerName}，用户（${userName}：${userPhone}）为您预约的${useDetail}由于前序订单使用超时，系统自动为您更换至${spaceNo}车位，请点击以下链接使用：${orderDetailUrl}，给您带来的不便我们深感抱歉，感谢您的使用。
', '0');

INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`)
	VALUES ('rental.order.detail.url', '/vip-parking/build/index.html#/intro?namespaceId=1000000&resourceType=%s&resourceTypeId=%s&sourceType=%s&sourceId=%s', '', '0', NULL);

UPDATE eh_parking_lots JOIN eh_communities ON eh_communities.id = eh_parking_lots.owner_id SET eh_parking_lots.namespace_id = eh_communities.namespace_id;

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`)
  VALUES ('rental.notification', '12', 'zh_CN', '亲爱的用户，为保障资源使用效益，现在取消订单，系统将不予退款，恳请您谅解。\r\n\r\n确认要取消订单吗？');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`)
  VALUES ('parking', '10022', 'zh_CN', '升起车锁失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`)
  VALUES ('parking', '10023', 'zh_CN', '降下车锁失败');

INSERT INTO `ehcore`.`eh_configurations`(`name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ('parking.dingding.url', 'https://public.dingdingtingche.com', NULL, 0, NULL);
INSERT INTO `ehcore`.`eh_configurations`(`name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ('parking.dingding.hubMac', 'CC:1B:E0:E0:09:F8', NULL, 0, NULL);


-- 删除审批这种服务联盟跳转模块 by dengs,2018/1/12
SELECT * FROM eh_service_alliance_jump_module WHERE module_url LIKE '%zl://approval/create%' AND module_name='审批';
DELETE FROM eh_service_alliance_jump_module WHERE module_url LIKE '%zl://approval/create%' AND module_name='审批';

-- merge from payment-contract by xiongying 20180124
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21300,'付款管理',20000,'/20000/21300','1','2','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21310,'付款申请单',21300,'/20000/21300/21310','1','3','2','0',NOW(),1,1);
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21320,'工作流设置',21300,'/20000/21300/21320','1','3','2','0',NOW(),1,1);

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `creator_uid`, `operator_uid`) VALUES(21215,'付款合同',21200,'/20000/21200/21215','1','3','2','0',NOW(),1,1);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21300, '0', '付款管理 管理员', '付款管理 业务模块权限', NULL);

SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21300', '1', '21300', '付款管理管理权限', '0', NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES ((@module_privilege_id := @module_privilege_id + 1), '21300', '2', '21300', '付款管理全部权限', '0', NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21301, '0', '付款管理 新增权限', '付款管理 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21302, '0', '付款管理 查看权限', '付款管理 业务模块权限', NULL);

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21215, '0', '付款合同 新增付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21216, '0', '付款合同 签约 发起 付款审批', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21217, '0', '付款合同 修改 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21218, '0', '付款合同 删除 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21219, '0', '付款合同 作废 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21220, '0', '付款合同 查看 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21221, '0', '付款合同 续约 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21222, '0', '付款合同 变更 付款合同', '付款合同 业务模块权限', NULL);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (21223, '0', '付款合同 退约 付款合同', '付款合同 业务模块权限', NULL);

    
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21310','0',21301,'付款管理 新增权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21310','0',21302,'付款管理 查看权限','0',NOW());

INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21215,'付款合同 新增付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21216,'付款合同 签约 发起 付款审批权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21217,'付款合同 修改 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21218,'付款合同 删除 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21219,'付款合同 作废 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21220,'付款合同 查看 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21221,'付款合同 续约 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21222,'付款合同 变更 付款合同权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'21215','0',21223,'付款合同 退约 付款合同权限','0',NOW());

SET @template_id = (SELECT MAX(id) FROM `eh_locale_templates`);
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@template_id := @template_id + 1), 'contract.notification', '1', 'zh_CN', '通知合同即将过期', '有一份合同为${contractName}将在${time}到期，请尽快处理。', '0');
INSERT INTO `eh_locale_templates` (`id`, `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ((@template_id := @template_id + 1), 'contract.notification', '2', 'zh_CN', '通知付款', '${contractName}有一笔付款金额为${amount}将在${time}需付款，请尽快处理。', '0');


SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);
SET @item_id = (SELECT MAX(id) FROM `eh_var_field_items`);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (30, 'contract', '0', '/30', '付款合同', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (31, 'contract', '30', '/30/31', '合同主体信息', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (32, 'contract', '30', '/30/32', '合同信息', '', '0', NULL, '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_groups` (`id`, `module_name`, `parent_id`, `path`, `title`, `name`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES (33, 'contract', '30', '/30/33', '付款计划', '', '0', NULL, '2', '1', NOW(), NULL, NULL);


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'contractNumber', '合同编号', 'String', '31', '/30/31/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'name', '合同名称', 'String', '31', '/30/31/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'categoryItemId', '合同类型', 'Long', '31', '/30/31/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"customizationSelect\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'customerId', '乙方', 'Long', '31', '/30/31/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'partyAId', '甲方', 'Long', '31', '/30/31/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'contractStartDate', '合同开始时间', 'Long', '31', '/30/31/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'contractEndDate', '合同截止时间', 'Long', '31', '/30/31/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'rent', '合同总额', 'BigDecimal', '31', '/30/31/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'status', '合同状态', 'Byte', '31', '/30/31/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{"fieldParamType": "select", "length": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '待发起', '1', '2', '1', NOW(), NULL, NULL, 1);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '正常合同', '2', '2', '1', NOW(), NULL, NULL, 2);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '审批中', '3', '2', '1', NOW(), NULL, NULL, 3);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '审批通过', '4', '2', '1', NOW(), NULL, NULL, 4);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '审批不通过', '5', '2', '1', NOW(), NULL, NULL, 5);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '即将到期', '6', '2', '1', NOW(), NULL, NULL, 6);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '已过期', '7', '2', '1', NOW(), NULL, NULL, 7);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '历史合同', '8', '2', '1', NOW(), NULL, NULL, 8);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '作废合同', '9', '2', '1', NOW(), NULL, NULL, 9);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '退约合同', '10', '2', '1', NOW(), NULL, NULL, 10);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '草稿', '11', '2', '1', NOW(), NULL, NULL, 11);


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'contractType', '合同属性', 'Byte', '31', '/30/31/', '1', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '新签合同', '1', '2', '1', NOW(), NULL, NULL, 0);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '续约合同', '2', '2', '1', NOW(), NULL, NULL, 1);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '变更合同', '3', '2', '1', NOW(), NULL, NULL, 2);


INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'remainingAmount', '剩余金额', 'BigDecimal', '31', '/30/31/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'signedTime', '签约日期', 'Long', '31', '/30/31/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'bidItemId', '是否通过招投标', 'Long', '31', '/30/31/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '否', '1', '2', '1', NOW(), NULL, NULL, 1);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '是', '1', '2', '1', NOW(), NULL, NULL, 2);

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'createUid', '经办人', 'Long', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'createOrgId', '经办部门', 'Long', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'createPositionId', '岗位', 'Long', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'ourLegalRepresentative', '我方法人代表', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'taxpayerIdentificationCode', '纳税人识别码', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'registeredAddress', '注册地址', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'registeredPhone', '注册电话', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'payee', '收款单位', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'payer', '付款单位', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'dueBank', '收款银行', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'bankAccount', '银行账号', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'exchangeRate', '兑换汇率', 'BigDecimal', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'ageLimit', '年限', 'Integer', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'applicationId', '关联请示', 'Long', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'paymentModeItemId', '预计付款方式', 'Long', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '一次性付款', '1', '2', '1', NOW(), NULL, NULL);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES ((@item_id := @item_id + 1), 'contract', @field_id, '分批付款', '2', '2', '1', NOW(), NULL, NULL);

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'lumpSumPayment', '一次性付款金额', 'BigDecimal', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'paidTime', '预计付款时间', 'Long', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'treatyParticulars', '合同摘要', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"richText\", \"length\": 1024}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'remark', '备注', 'String', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"richText\", \"length\": 1024}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'attachments', '附件', 'List<ContractAttachmentDTO>', '32', '/30/32/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"file\", \"length\": 9}');






-- 薪酬结构基础数据

INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('1',NULL,NULL,NULL,'固定工资',NULL,'1','0','2','1','2018-01-19 15:21:33','2018-01-19 15:21:37','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('2',NULL,NULL,NULL,'浮动工资',NULL,'1','0','2','1','2018-01-19 15:23:19','2018-01-19 15:23:21','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('3',NULL,NULL,NULL,'考勤工资','在「津贴设置」中自动同步考勤数据，在「出勤扣款」中设置方案','0',NULL,'2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('4',NULL,NULL,NULL,'社保公积金代扣','自动同步社保数据','0',NULL,'2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('5',NULL,NULL,NULL,'个税代扣','根据国家法律自动扣减个税','0',NULL,'2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('6',NULL,NULL,NULL,'成本项','不计入工资，计入报表「企业人工成本」中','1','2','2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('7',NULL,NULL,NULL,'其他',NULL,'1','3','2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');


-- 薪酬字段基础数据


INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('1','0','0','0','0','0','0','1','固定工资','基本工资',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('2','0','0','0','0','0','0','1','固定工资','岗位工资',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('3','0','0','0','0','0','0','1','固定工资','绩效工资',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('4','0','0','0','1','0','1','2','浮动工资','年终奖','自动按照「全年一次性奖金收入」计税','1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('5','0','0','0','1','0','0','2','浮动工资','代通知金','依法提前一个月通知的，以给付一个月工资作为代替。','1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('6','0','0','0','1','0','0','2','浮动工资','补偿金','人工计算后可归到「其他税后扣款」项','0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('7','1','0','0','1','0','0','2','浮动工资','个人年金',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('8','1','0','0','1','0','0','2','浮动工资','个人商保',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('9','1','0','0','1','0','0','2','浮动工资','工会会费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('10','1','0','0','1','1','0','2','浮动工资','其他税后补发',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('11','1','0','0','1','0','0','2','浮动工资','其他税前补发',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('12','1','0','1','1','0','0','2','浮动工资','其他税前扣款',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('13','1','0','1','1','1','0','2','浮动工资','其他税后扣款',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('14','1','0','0','1','0','0','2','浮动工资','补上月差额',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('15','0','0','0','1','0','0','3','考勤工资','加班费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('16','0','0','0','1','0','0','3','考勤工资','全勤奖',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('17','0','0','1','1','0','0','3','考勤工资','迟到扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('18','0','0','1','1','0','0','3','考勤工资','早退扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('19','0','0','1','1','0','0','3','考勤工资','请假扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('20','0','0','1','1','0','0','3','考勤工资','旷工扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('21','0','0','1','1','0','0','3','考勤工资','缺卡扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('22','0','0','1','1','0','0','3','考勤工资','缺勤扣款',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('23','0','0','1','1','0','0','4','社保公积金代扣','社保缴纳（个人）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('24','0','0','1','1','0','0','4','社保公积金代扣','公积金缴纳（个人）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('25','0','0','1','1','0','0','4','社保公积金代扣','社保补缴（个人）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('26','0','0','1','1','0','0','4','社保公积金代扣','公积金补缴（个人）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('27','-1','0','1','1',NULL,NULL,'5','个税代扣','年终奖扣个税',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('28','-1','0','1','1',NULL,NULL,'5','个税代扣','工资扣个税',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('29','0','0','2','1',NULL,NULL,'6','成本项','社保缴纳（企业）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('30','0','0','2','1',NULL,NULL,'6','成本项','公积金缴纳（企业）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('31','0','0','2','1',NULL,NULL,'6','成本项','社保补缴（企业）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('32','0','0','2','1',NULL,NULL,'6','成本项','公积金补缴（企业）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('33','0','0','2','1',NULL,NULL,'6','成本项','商业保险',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('34','0','0','2','1',NULL,NULL,'6','成本项','残障金',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('35','1','0','2','1',NULL,NULL,'6','成本项','企业年金',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('36','1','0','2','1',NULL,NULL,'6','成本项','工会经费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('37','1','0','2','1',NULL,NULL,'6','成本项','体检费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('38','1','0','2','1',NULL,NULL,'6','成本项','日常报销',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('39','1','0','2','1',NULL,NULL,'6','成本项','培训费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('40','1','0','2','1',NULL,NULL,'6','成本项','其他企业成本',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('41','1','0','3','1',NULL,NULL,'7','其他','备注',NULL,'1');




-- 文档图标 add by nan.rong 01/25/2018

INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('1', 'category', 'category.png', 'cs://1/image/aW1hZ2UvTVRvNFpUaG1ZelZoWTJZd1pHSXlOalJtT0RZek1tTTBObVE1TXpaaU1qbGlOUQ', '2018-01-18 20:32:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('2', 'folder', 'folder.png', 'cs://1/image/aW1hZ2UvTVRveVpUUXdOemhsTmpBME5UQTNZVFExT0dWak9UaG1OV0kzWW1Rek1qbGtOQQ', '2018-01-18 20:32:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('3', 'rar', 'package.png', 'cs://1/image/aW1hZ2UvTVRvME4yUTBNemt6WkdZeU1HSXdNamxoWm1FNU16RTRNMk15T1dVMU1UWmhaZw', '2018-01-18 20:25:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('4', 'zip', 'package.png', 'cs://1/image/aW1hZ2UvTVRvME4yUTBNemt6WkdZeU1HSXdNamxoWm1FNU16RTRNMk15T1dVMU1UWmhaZw', '2018-01-18 20:25:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('5', 'tar', 'package.png', 'cs://1/image/aW1hZ2UvTVRvME4yUTBNemt6WkdZeU1HSXdNamxoWm1FNU16RTRNMk15T1dVMU1UWmhaZw', '2018-01-18 20:25:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('6', 'bz2', 'package.png', 'cs://1/image/aW1hZ2UvTVRvME4yUTBNemt6WkdZeU1HSXdNamxoWm1FNU16RTRNMk15T1dVMU1UWmhaZw', '2018-01-18 20:25:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('7', 'gz', 'package.png', 'cs://1/image/aW1hZ2UvTVRvME4yUTBNemt6WkdZeU1HSXdNamxoWm1FNU16RTRNMk15T1dVMU1UWmhaZw', '2018-01-18 20:25:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('8', '7z', 'package.png', 'cs://1/image/aW1hZ2UvTVRvME4yUTBNemt6WkdZeU1HSXdNamxoWm1FNU16RTRNMk15T1dVMU1UWmhaZw', '2018-01-18 20:25:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('9', 'doc', 'word.png', 'cs://1/image/aW1hZ2UvTVRvMk56TmlPRGsxTkRjMk9XUmlaVGc0TkRreE1HRTROams0T0RGaU9UZGtOUQ', '2018-01-18 20:27:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('10', 'docx', 'word.png', 'cs://1/image/aW1hZ2UvTVRvMk56TmlPRGsxTkRjMk9XUmlaVGc0TkRreE1HRTROams0T0RGaU9UZGtOUQ', '2018-01-18 20:27:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('11', 'pages', 'pages.png', 'cs://1/image/aW1hZ2UvTVRvNE1ETmhNR1ExTWpsa01qSTFNak5rTXpVNVl6TTVOR1ZoTmpCbFlUVmlaQQ', '2018-01-18 20:27:54');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('12', 'ppt', 'ppt.png', 'cs://1/image/aW1hZ2UvTVRwbE1EVXlZMlJpTURoak1UTTNaalU1WXpnMVpqaGpNRGhtWkRRNU5qWTFNUQ', '2018-01-18 20:28:27');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('13', 'pptx', 'ppt.png', 'cs://1/image/aW1hZ2UvTVRwbE1EVXlZMlJpTURoak1UTTNaalU1WXpnMVpqaGpNRGhtWkRRNU5qWTFNUQ', '2018-01-18 20:28:27');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('14', 'key', 'key.png', 'cs://1/image/aW1hZ2UvTVRvMU16YzVPR1l6WVdGbE56bGlNMlkyWlRFMk1UbGtNVGM0TnpVNE1qZzVNZw', '2018-01-18 20:28:27');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('15', 'xls', 'excel.png', 'cs://1/image/aW1hZ2UvTVRwbU1Ua3lNalkxWW1ReU1EZGpNVGt4WVdNMk5XSXdZalU1TVdOaU1UUTVZdw', '2018-01-18 20:28:27');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('16', 'xlsx', 'excel.png', 'cs://1/image/aW1hZ2UvTVRwbU1Ua3lNalkxWW1ReU1EZGpNVGt4WVdNMk5XSXdZalU1TVdOaU1UUTVZdw', '2018-01-18 20:28:27');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('17', 'numbers', 'number.png', 'cs://1/image/aW1hZ2UvTVRwbVpEQXhOakkzT0RJMk5qSTVNV0ZtTnpsa01XUXpNbVZoWWpkaVlXWXlOdw', '2018-01-18 20:28:27');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('18', 'pdf', 'pdf.png', 'cs://1/image/aW1hZ2UvTVRvMFptSmhOemxsTVRreVl6VTBNRFUyWVdObU5qUXpPVFEzWXpjNFpUSTROUQ', '2018-01-18 20:30:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('19', 'txt', 'txt.png', 'cs://1/image/aW1hZ2UvTVRwalpEZzRZV0ppT1RabE0ySTVaREkxTmpRek1HWTJaVEE1T1RBNE9HWTVOdw', '2018-01-18 20:30:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('20', 'rtf', 'rtf.png', 'cs://1/image/aW1hZ2UvTVRwa05UTXpNalZsWXpnek1qaGhNV1JtWXpZeU5qWTRZelJoWXpsbE1qVTFOQQ', '2018-01-18 20:30:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('21', 'png', 'image.png', 'cs://1/image/aW1hZ2UvTVRveE56VTBOR0kwWW1JNFltVm1ZV1U0T0Rsak1EWTVaVFU0WVdRd05qVXlNdw', '2018-01-18 20:30:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('22', 'jpg', 'image.png', 'cs://1/image/aW1hZ2UvTVRveE56VTBOR0kwWW1JNFltVm1ZV1U0T0Rsak1EWTVaVFU0WVdRd05qVXlNdw', '2018-01-18 20:30:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('23', 'jpeg', 'image.png', 'cs://1/image/aW1hZ2UvTVRveE56VTBOR0kwWW1JNFltVm1ZV1U0T0Rsak1EWTVaVFU0WVdRd05qVXlNdw', '2018-01-18 20:30:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('24', 'gif', 'image.png', 'cs://1/image/aW1hZ2UvTVRveE56VTBOR0kwWW1JNFltVm1ZV1U0T0Rsak1EWTVaVFU0WVdRd05qVXlNdw', '2018-01-18 20:30:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('25', 'mp3', 'music.png', 'cs://1/image/aW1hZ2UvTVRvMk5tRXdaVEF6Wm1Ka1lUazRNMk5oTnpVd1l6bGlZVFJrWmpVM05XWTRPUQ', '2018-01-18 20:32:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('26', 'mp4', 'moive.png', 'cs://1/image/aW1hZ2UvTVRvd1l6VXhaak0zWVdZNE0ySTRNelJtWW1KaU1qRmpaRFk1TjJGaU16SmhPQQ', '2018-01-18 20:32:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('27', 'mov', 'moive.png', 'cs://1/image/aW1hZ2UvTVRvd1l6VXhaak0zWVdZNE0ySTRNelJtWW1KaU1qRmpaRFk1TjJGaU16SmhPQQ', '2018-01-18 20:32:44');
INSERT INTO `eh_file_icons` (`id`, `file_type`, `icon_name`, `icon_uri`, `create_time`) VALUES ('28', 'other', 'other.png', 'cs://1/image/aW1hZ2UvTVRveU5EQXpaakU1TXpObU1UVXhZbU15TnpNeU9EZzJPR0l6WlRKaFlqazFZZw', '2018-01-18 20:32:44');

-- volgo 添加文档管理图标.
SET @item_id = (SELECT MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) VALUES (@item_id := @item_id + 1, '1', '0', '0', '0', '/home', 'Bizs', '文档管理', '文档管理', 'cs://1/image/aW1hZ2UvTVRvNE5XWmpNakV4TW1VNFlUbG1aR0ppWWpoaU16RmxNekUxWWpFMk1XRXlZUQ', '1', '1', '69', '{"title":"文档管理"}', '9', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL, NULL, '0', '企业服务', NULL);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) VALUES (@item_id := @item_id + 1, '1', '0', '0', '0', '/home', 'Bizs', '文档管理', '文档管理', 'cs://1/image/aW1hZ2UvTVRvNE5XWmpNakV4TW1VNFlUbG1aR0ppWWpoaU16RmxNekUxWWpFMk1XRXlZUQ', '1', '1', '69', '{"title":"文档管理"}', '9', '0', '1', '0', '', '0', NULL, NULL, NULL, '1', 'park_tourist', '0', NULL, NULL, '0', '企业服务', NULL);

-- end by nan.rong


-- 更新“论坛/公告”为“论坛” add by yanjun 20180126
UPDATE eh_service_modules SET `name` = '论坛' WHERE id = 10100;

DELETE FROM eh_service_module_privileges WHERE module_id = 10600;


-- 薪酬结构基础数据

INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('1',NULL,NULL,NULL,'固定工资',NULL,'1','1','2','1','2018-01-19 15:21:33','2018-01-19 15:21:37','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('2',NULL,NULL,NULL,'浮动工资',NULL,'1','1','2','1','2018-01-19 15:23:19','2018-01-19 15:23:21','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('3',NULL,NULL,NULL,'考勤工资','在「津贴设置」中自动同步考勤数据，在「出勤扣款」中设置方案','0',NULL,'2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('4',NULL,NULL,NULL,'社保公积金代扣','自动同步社保数据','0',NULL,'2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('5',NULL,NULL,NULL,'个税代扣','根据国家法律自动扣减个税','0',NULL,'2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('6',NULL,NULL,NULL,'成本项','不计入工资，计入报表「企业人工成本」中','1','2','2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');
INSERT INTO `eh_salary_entity_categories` (`id`, `owner_type`, `owner_id`, `namespace_id`, `category_name`, `description`, `custom_flag`, `custom_type`, `status`, `creator_uid`, `create_time`, `update_time`, `operator_uid`) VALUES('7',NULL,NULL,NULL,'其他',NULL,'1','3','2','1','2018-01-19 15:23:19','2018-01-19 15:23:19','1');


-- 薪酬字段基础数据


INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('1','0','0','0','0','0','0','1','固定工资','基本工资',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('2','0','0','0','0','0','0','1','固定工资','岗位工资',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('3','0','0','0','0','0','0','1','固定工资','绩效工资',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('4','0','0','0','1','0','1','2','浮动工资','年终奖','自动按照「全年一次性奖金收入」计税','1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('5','0','0','0','1','0','0','2','浮动工资','代通知金','依法提前一个月通知的，以给付一个月工资作为代替。','1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('6','0','0','0','1','0','0','2','浮动工资','补偿金','人工计算后可归到「其他税后扣款」项','0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('7','1','0','0','1','0','0','2','浮动工资','个人年金',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('8','1','0','0','1','0','0','2','浮动工资','个人商保',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('9','1','0','0','1','0','0','2','浮动工资','工会会费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('10','1','0','0','1','1','0','2','浮动工资','其他税后补发',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('11','1','0','0','1','0','0','2','浮动工资','其他税前补发',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('12','1','0','1','1','0','0','2','浮动工资','其他税前扣款',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('13','1','0','1','1','1','0','2','浮动工资','其他税后扣款',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('14','1','0','0','1','0','0','2','浮动工资','补上月差额',NULL,'1');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('15','0','0','0','1','0','0','3','考勤工资','加班费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('16','0','0','0','1','0','0','3','考勤工资','全勤奖',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('17','0','0','1','1','0','0','3','考勤工资','迟到扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('18','0','0','1','1','0','0','3','考勤工资','早退扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('19','0','0','1','1','0','0','3','考勤工资','请假扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('20','0','0','1','1','0','0','3','考勤工资','旷工扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('21','0','0','1','1','0','0','3','考勤工资','缺卡扣款',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('22','0','0','1','1','0','0','3','考勤工资','缺勤扣款',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('23','0','0','1','1','0','0','4','社保公积金代扣','社保缴纳（个人）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('24','0','0','1','1','0','0','4','社保公积金代扣','公积金缴纳（个人）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('25','0','0','1','1','0','0','4','社保公积金代扣','社保补缴（个人）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('26','0','0','1','1','0','0','4','社保公积金代扣','公积金补缴（个人）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('27','-1','0','1','1',NULL,NULL,'5','个税代扣','年终奖扣个税',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('28','-1','0','1','1',NULL,NULL,'5','个税代扣','工资扣个税',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('29','0','0','2','1',NULL,NULL,'6','成本项','社保缴纳（企业）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('30','0','0','2','1',NULL,NULL,'6','成本项','公积金缴纳（企业）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('31','0','0','2','1',NULL,NULL,'6','成本项','社保补缴（企业）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('32','0','0','2','1',NULL,NULL,'6','成本项','公积金补缴（企业）',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('33','0','0','2','1',NULL,NULL,'6','成本项','商业保险',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('34','0','0','2','1',NULL,NULL,'6','成本项','残障金',NULL,'2');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('35','1','0','2','1',NULL,NULL,'6','成本项','企业年金',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('36','1','0','2','1',NULL,NULL,'6','成本项','工会经费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('37','1','0','2','1',NULL,NULL,'6','成本项','体检费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('38','1','0','2','1',NULL,NULL,'6','成本项','日常报销',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('39','1','0','2','1',NULL,NULL,'6','成本项','培训费',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('40','1','0','2','1',NULL,NULL,'6','成本项','其他企业成本',NULL,'0');
INSERT INTO `eh_salary_default_entities` (`id`, `editable_flag`, `delete_flag`, `type`, `data_policy`, `grant_policy`, `tax_policy`, `category_id`, `category_name`, `name`, `description`, `status`) VALUES('41','1','0','3','1',NULL,NULL,'7','其他','备注',NULL,'1');

-- 更新入孵申请的actionType为71 add by yanjun 20180130
UPDATE eh_launch_pad_items SET action_type = 71 WHERE namespace_id = 999964 AND action_type = 68;


-- initially added four kinds of requisiton types to schema eh_requistion_types by wentian
INSERT INTO `eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (1, '0', 'EhNamespaces', '0', '采购申请', NOW(), '0', NULL, NULL, 1);
SET @type_id = (SELECT MAX(`id`) FROM `eh_requisition_types`);
SET @order_id = (SELECT MAX(`default_order`) FROM `eh_requisition_types`);
INSERT INTO `eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '领用申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);
INSERT INTO `eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '付款申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);
INSERT INTO `eh_requisition_types` (`id`, `namespace_id`, `owner_type`, `owner_id`, `name`, `create_time`, `create_uid`, `update_time`, `update_uid`, `default_order`) VALUES (@type_id:=@type_id+1, '0', 'EhNamespaces', '0', '合同申请', NOW(), '0', NULL, NULL, @order_id:=@order_id+10);

-- end of script by wentian



INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('1','13905','全部',NULL,'2030.00','37400.00','500','1200','2030.00','37400.00','500','1200','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'2','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('2','13905','本市','养老','2130.00','22440.00','1400','1400','2130.00','22440.00','800','800','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('3','13905','本市','医疗','4488.00','22440.00','600','600','4488.00','22440.00','200','200','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('4','13905','本市','生育','2130.00','22440.00','100','100','2130.00','22440.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('5','13905','本市','失业','2130.00','2130.00','80','100','2130.00','2130.00','100','100','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('6','13905','本市','工伤','2130.00','22440.00','14','171','2130.00','22440.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00','[14,28,49,63,66,78,96,114]','1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('7','13905','外埠一档','养老','2130.00','22440.00','1300','1300','2130.00','22440.00','800','800','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('8','13905','外埠一档','医疗','4488.00','22440.00','600','600','4488.00','22440.00','200','200','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('9','13905','外埠一档','生育','2130.00','22440.00','100','100','2130.00','22440.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('10','13905','外埠一档','失业','2130.00','2130.00','80','100','2130.00','2130.00','100','100','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('11','13905','外埠一档','工伤','2130.00','22440.00','14','171','2130.00','22440.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00','[14,28,49,63,66,78,96,114]','1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('12','13905','外埠二档','养老','2130.00','22440.00','1300','1300','2130.00','22440.00','800','800','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('13','13905','外埠二档','医疗','7480.00','7480.00','100','100','7480.00','7480.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('14','13905','外埠二档','生育','2130.00','22440.00','100','100','2130.00','22440.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('15','13905','外埠二档','失业','2130.00','2130.00','80','100','2130.00','2130.00','100','100','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('16','13905','外埠二档','工伤','2130.00','22440.00','14','171','2130.00','22440.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00','[14,28,49,63,66,78,96,114]','1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('17','13905','外埠三档','养老','2130.00','22440.00','1300','1300','2130.00','22440.00','800','800','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('18','13905','外埠三档','医疗','7480.00','7480.00','0','0','7480.00','7480.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('19','13905','外埠三档','生育','2130.00','22440.00','100','100','2130.00','22440.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('20','13905','外埠三档','失业','2130.00','2130.00','80','100','2130.00','2130.00','100','100','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00',NULL,'1','0','2017-12-28 13:53:41',NULL,NULL);
INSERT INTO `eh_social_security_bases` (`id`, `city_id`, `household_type`, `pay_item`, `company_radix_min`, `company_radix_max`, `company_ratio_min`, `company_ratio_max`, `employee_radix_min`, `employee_radix_max`, `employee_ratio_min`, `employee_ratio_max`, `editable_flag`, `is_default`, `effect_time_begin`, `effect_time_end`, `ratio_options`, `accum_or_socail`, `creator_uid`, `create_time`, `operator_uid`, `update_time`) VALUES('21','13905','外埠三档','工伤','2130.00','22440.00','14','171','2130.00','22440.00','0','0','0','1','2017-07-01 00:00:00','2018-06-30 00:00:00','[14,28,49,63,66,78,96,114]','1','0','2017-12-28 13:53:41',NULL,NULL);

-- 暂时取消ratio options
UPDATE eh_social_security_bases SET ratio_options = NULL ;
 


-- 公告管理 add by zhiwei.zhang
-- 域空间volgo
-- 增加公告管理模块信息
INSERT INTO eh_service_modules(id,NAME,parent_id,path,TYPE,LEVEL,STATUS,create_time,creator_uid,operator_uid,action_type,multiple_flag,module_control_type,default_order)
VALUE(57000,'公告管理',50000,'/50000/57000',1,2,2,NOW(),0,0,70,0,'org_control',0);

-- 增加公告模块与域空间的关联信息
SET @id = (SELECT MAX(id) FROM eh_service_module_scopes);
INSERT INTO eh_service_module_scopes(id,namespace_id,module_id,module_name,owner_type,owner_id,apply_policy)
VALUE(@id+1,1,57000,'公告管理','EhNamespaces',1,2);

-- volgo 添加公告图标.
SET @item_id = (SELECT MAX(id) FROM eh_launch_pad_items);
INSERT INTO `eh_launch_pad_items` (`id`, `namespace_id`, `app_id`, `scope_code`, `scope_id`, `item_location`, `item_group`, `item_name`, `item_label`, `icon_uri`, `item_width`, `item_height`, `action_type`, `action_data`, `default_order`, `apply_policy`, `min_version`, `display_flag`, `display_layout`, `bgcolor`, `tag`, `target_type`, `target_id`, `delete_flag`, `scene_type`, `scale_type`, `service_categry_id`, `selected_icon_uri`, `more_order`, `alias_icon_uri`, `categry_name`) VALUES (@item_id := @item_id + 1, '1', '0', '0', '0', '/home', 'Bizs', '公告', '公告', 'cs://1/image/aW1hZ2UvTVRvNE5XWXdPVFl6Wm1SaVpqWmxaVFJpT0RkaE56WXpNR1EyTnpsa1ptSmpZZw', '1', '1', '70', '{"title":"公告管理"}', '10', '0', '1', '1', '', '0', NULL, NULL, NULL, '1', 'pm_admin', '0', NULL, NULL, '0', NULL,NULL);

-- end by zhiwei.zhang

-- error codes added by wentian
SET @eh_locale_strings_id = (SELECT MAX(id) FROM `eh_locale_strings`);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'requisition', '1001', 'zh_CN', '未找到请示单工作流');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'purchase', '1001', 'zh_CN', '该采购单未完成或者已取消,不能进行入库操作');
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES (@eh_locale_strings_id:=@eh_locale_strings_id+1, 'purchase', '1002', 'zh_CN', '未找到用户的采购模块工作流');

-- by dengs.
UPDATE eh_office_cubicle_categories SET STATUS=2;

-- 付款合同主体信息加上原合同和初始合同 by xiongying
SET @field_id = (SELECT MAX(id) FROM eh_var_fields);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'parentId', '原合同', 'Long', '31', '/30/31/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'contract', 'rootParentId', '初始合同', 'Long', '31', '/30/31/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');

-- bY zheng
UPDATE eh_rentalv2_default_rules SET rental_start_time = 7776000000 WHERE rental_start_time = 0;
UPDATE eh_rentalv2_default_rules SET rental_start_time_flag = 1;

UPDATE eh_rentalv2_resource_types SET identify = 'conference' WHERE NAME LIKE '%会议室%';
UPDATE eh_rentalv2_resource_types SET identify = 'screen' WHERE NAME LIKE '%电子屏%';
UPDATE eh_rentalv2_resource_types SET identify = 'area' WHERE NAME LIKE '%场地%';
-- beta 和 现网执行
UPDATE eh_rentalv2_resource_types SET identify = 'conference' WHERE id IN (10819,12030);
UPDATE eh_rentalv2_resource_types SET identify = 'screen' WHERE id IN (11,12168);
UPDATE eh_rentalv2_resource_types SET identify = 'area' WHERE id IN (10012,10062,10715,10716,10717,10814,12044,12078,12081,12082,12131,12175);

-- fix 24613 by xiongying
update eh_var_fields set display_name = '离场时间(月)' where display_name = '离场时间';

-- fix 24288 by xiongying
SET @group_id = (select id from eh_var_field_groups where title = '客户合同');
SET @group_path = (select path from eh_var_field_groups where title = '客户合同');
SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);
SET @item_id = (SELECT MAX(id) FROM `eh_var_field_items`);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'contractNumber', '合同编号', 'String', @group_id, CONCAT(@group_path,'/'), 1, NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'name', '合同名称', 'String', @group_id, CONCAT(@group_path,'/'), 1, NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'contractType', '合同属性', 'Byte', @group_id, CONCAT(@group_path,'/'), 1, NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '新签合同', '1', '2', '1', NOW(), NULL, NULL, 0);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '续约合同', '2', '2', '1', NOW(), NULL, NULL, 1);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '变更合同', '3', '2', '1', NOW(), NULL, NULL, 2);

INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'contractStartDate', '开始日期', 'Long', @group_id, CONCAT(@group_path,'/'), 1, NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'contractEndDate', '结束日期', 'Long', @group_id, CONCAT(@group_path,'/'), 1, NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"datetime\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'rent', '租赁总额', 'BigDecimal', @group_id, CONCAT(@group_path,'/'), 1, NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'status', '合同状态', 'Byte', @group_id, CONCAT(@group_path,'/'), 1, NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"select\", \"length\": 32}');
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '待发起', '1', '2', '1', NOW(), NULL, NULL, 1);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '正常合同', '2', '2', '1', NOW(), NULL, NULL, 2);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '审批中', '3', '2', '1', NOW(), NULL, NULL, 3);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '审批通过', '4', '2', '1', NOW(), NULL, NULL, 4);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '审批不通过', '5', '2', '1', NOW(), NULL, NULL, 5);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '即将到期', '6', '2', '1', NOW(), NULL, NULL, 6);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '已过期', '7', '2', '1', NOW(), NULL, NULL, 7);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '历史合同', '8', '2', '1', NOW(), NULL, NULL, 8);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '作废合同', '9', '2', '1', NOW(), NULL, NULL, 9);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '退约合同', '10', '2', '1', NOW(), NULL, NULL, 10);
INSERT INTO `eh_var_field_items` (`id`, `module_name`, `field_id`, `display_name`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `business_value`) VALUES ((@item_id := @item_id + 1), 'enterprise_customer', @field_id, '草稿', '11', '2', '1', NOW(), NULL, NULL, 11);


-- fix 24623 by xiongying
update eh_var_field_groups set name = 'com.everhomes.customer.CustomerTax' where title = '税务信息';
update eh_var_field_groups set name = 'com.everhomes.customer.CustomerAccount' where title = '银行账号';

-- by dengs,20180301,园区快讯权限项添加
set @id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1, '10800', '0', '10005', '全部权限', '0', now());

-- by dengs,20180302,服务联盟权限项添加
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40510', '样式设置', '40500', '/40000/40500/40510', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40520', '服务管理', '40500', '/40000/40500/40520', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40530', '消息通知', '40500', '/40000/40500/40530', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40540', '申请记录', '40500', '/40000/40500/40540', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40500', '0', '10024', '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040510, '0', '服务联盟 样式设置权限', '服务联盟 样式设置权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40510', '0', 4050040510, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040520, '0', '服务联盟 服务管理权限', '服务联盟 服务管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40520', '0', 4050040520, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040530, '0', '服务联盟 消息通知权限', '服务联盟 消息通知权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40530', '0', 4050040530, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040540, '0', '服务联盟 申请记录权限', '服务联盟 申请记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40540', '0', 4050040540, '全部权限', '0', now());

-- fix 24668 xiongying20180302
update eh_var_field_groups set title = '客户信息' where id = 1;


-- 供应商，采购，请示单的规则的细化 by wentian
-- 采购的权限细化
set @module_id = 26000;
set @p_id = 260001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '查看详情', '查看详情', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
	(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看详情', '0', NOW());

set @module_id = 26000;
set @p_id = 260001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '新增、修改、删除申请', '新增、修改、删除申请', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
	(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增、修改、删除申请', '0', NOW());

set @module_id = 26000;
set @p_id = 260001003;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '入库', '入库', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
	(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '入库', '0', NOW());


-- 供应商的权限细化规则
set @module_id = 27000;
set @p_id = 270001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '查看详情', '查看详情', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
	(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看详情', '0', NOW());

set @module_id = 27000;
set @p_id = 270001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '新增、修改、删除供应商', '新增、修改、删除供应商', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
	(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增、修改、删除供应商', '0', NOW());

-- 请示单管理
set @module_id = 25000;
set @p_id = 250001001;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '查看详情', '查看详情', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
	(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '查看详情', '0', NOW());

set @module_id = 25000;
set @p_id = 250001002;
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (@p_id, null, '新增请示', '新增请示', NULL);
set @mp_id = (select MAX(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`)
VALUES
	(@mp_id:=@mp_id+1, @module_id, '0', @p_id, '新增请示', '0', NOW());

-- dengs 2018.03.05文件管理的模块id添加。
update eh_web_menus SET module_id=41500 WHERE NAME='文件管理';


-- 修改三级菜单的排序
update eh_service_modules set default_order = 1 where id = 49110;
update eh_service_modules set default_order = 3 where id = 49120;
update eh_service_modules set default_order = 4 where id = 49130;
update eh_service_modules set default_order = 5 where id = 49140;
update eh_service_modules set default_order = 2 where id = 49150;

-- 要求修改严军负责的三个模块的类型 by lei.lv
update eh_service_modules set module_control_type = 'unlimit_control' where id in(10100,10300,10600);
update eh_reflection_service_module_apps set module_control_type = 'unlimit_control' where module_id in(10100,10300,10600);