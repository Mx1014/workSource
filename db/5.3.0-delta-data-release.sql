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

UPDATE eh_equipment_inspection_task_logs
SET standard_id = (SELECT  standard_id FROM eh_equipment_inspection_tasks WHERE id = task_id);

-- 巡检任务状态统一 end by jiarui 20180105

-- 增加经纬度动态表单  jiarui   20180122
INSERT  INTO  `eh_var_fields` VALUES (11999, 'equipment_inspection', 'geohash', '经纬度', 'Long', 10000, CONCAT('/',10000,'/'), 1, NULL, 2, 1, NOW(),NULL ,NULL,'{\"fieldParamType\": \"map\", \"length\": 32}');
-- 增加经纬度动态表单  jiarui  20180122

-- offline  by jiarui  {home.url}换成域名
-- 巡检离线
SET  @id = (SELECT  MAX(id) FROM eh_version_realm);
INSERT INTO `eh_version_realm` VALUES (@id:=@id+1, 'equipmentInspection', NULL, NOW(), '0');

SET  @vId = (SELECT  MAX(id) FROM eh_version_urls);
INSERT INTO `eh_version_urls` VALUES (@vId:=@vId+1, @id, '1.0.0', 'http://{home.url}/nar/equipmentInspection/inspectionOffLine/equipmentInspection-1-0-0.zip', 'http://{home.url}/nar/equipmentInspection/inspectionOffLine/equipmentInspection-1-0-0.zip', '物业巡检巡检离线', '0', '物业巡检', NOW(), NULL, '0');

UPDATE eh_launch_pad_items
SET action_data = '{\"realm\":\"equipmentInspection\",\"entryUrl\":\"http://{home.url}/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}'
WHERE item_label LIKE '%巡检%';

UPDATE eh_service_module_apps
SET instance_config = '{\"realm\":\"equipmentInspection\",\"entryUrl\":\"http://{home.url}/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}'
WHERE module_id = 20800;

UPDATE eh_service_modules
SET instance_config = '{\"realm\":\"equipmentInspection\",\"entryUrl\":\"http://{home.url}/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}'
WHERE id = 20800;

UPDATE eh_service_modules
SET action_type = 44
WHERE id = 20800;

UPDATE eh_service_module_apps
SET action_type = 44
WHERE module_id = 20800;

UPDATE eh_launch_pad_items
SET action_type = 44
WHERE item_label LIKE '%巡检%';


-- 品质核查离线  {home.url}换成域名  by jiarui

SET  @id = (SELECT  MAX(id) FROM eh_version_realm);
INSERT INTO `eh_version_realm` VALUES (@id:=@id+1, 'qualityInspection', NULL, NOW(), '0');

SET  @vId = (SELECT  MAX(id) FROM eh_version_urls);
INSERT INTO `eh_version_urls` VALUES (@vId:=@vId+1, @id, '1.0.0', 'http://{home.url}/nar/qualityInspection/offline/qualityInspection-1-0-0.zip', 'http://{home.url}/nar/qualityInspection/offline/qualityInspection-1-0-0.zip', '品质核查离线', '0', '品质核查', NOW(), NULL, '0');

UPDATE eh_launch_pad_items
SET action_data = '{\"realm\":\"qualityInspection\",\"entryUrl\":\"http://{home.url}/nar/qualityInspection/build/index.html?hideNavigationBar=1#/home#sign_suffix\"}'
WHERE item_label LIKE '%品质%';

UPDATE eh_service_module_apps
SET instance_config = '{\"realm\":\"qualityInspection\",\"entryUrl\":\"http://{home.url}/nar/qualityInspection/build/index.html?hideNavigationBar=1#/home#sign_suffix\"}'
WHERE module_id = 20600;

UPDATE eh_service_modules
SET instance_config = '{\"realm\":\"qualityInspection\",\"entryUrl\":\"http://{home.url}/nar/qualityInspection/build/index.html?hideNavigationBar=1#/home#sign_suffix\"}'
WHERE id = 20600;

UPDATE eh_service_modules
SET action_type = 44
WHERE id = 20600;

UPDATE eh_service_module_apps
SET action_type = 44
WHERE module_id = 20600;

UPDATE eh_launch_pad_items
SET action_type = 44
WHERE item_label LIKE '%品质%';
-- offline  end  by jiarui

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

-- 品质权限项顺序调整  by jiarui 20180319

UPDATE `eh_service_modules` SET `default_order`='0' WHERE `id`=20650;

UPDATE `eh_service_modules` SET `default_order`='1' WHERE `id`=20630;

UPDATE `eh_service_modules` SET `default_order`='2' WHERE `id`=20610;
UPDATE `eh_service_modules` SET `default_order`='3' WHERE `id`=20620;

UPDATE `eh_service_modules` SET `default_order`='4' WHERE `id`=20660;
UPDATE `eh_service_modules` SET `default_order`='5' WHERE `id`=20670;

UPDATE `eh_service_module_privileges` SET  `module_id`=20630 WHERE `id`=125;

-- 品质权限项顺序调整  by jiarui 20180319


-- 品质核查操作日志 jiarui
UPDATE  `eh_quality_inspection_logs`
SET scope_id =(SELECT eh_quality_inspection_standards.target_id FROM  eh_quality_inspection_standards WHERE eh_quality_inspection_standards.id = eh_quality_inspection_logs.target_id);

-- 品质核查操作日志 jiarui

--  重命名品质核查权限项名称  jiarui
UPDATE eh_service_modules
SET NAME = '计划管理'
WHERE id = 20630;
UPDATE eh_service_modules
SET NAME = '计划审批'
WHERE id = 20640;
UPDATE eh_service_modules
SET NAME = '日志管理'
WHERE id = 20670;
UPDATE  eh_service_modules
SET NAME ='任务管理'
WHERE id = 20650;
UPDATE  eh_service_modules
SET NAME ='统计分析'
WHERE id = 20660;

UPDATE eh_service_module_privileges
SET remark = REPLACE(remark,'标准','计划')
WHERE id IN (120,121,122,123,125);

UPDATE  eh_acl_privileges
SET NAME = '品质核查 考核统计查看权限'
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
  VALUES ('rental.notification', '12', 'zh_CN', '亲爱的用户，为保障资源使用效益，现在取消订单，系统将不予退款，恳请您谅解。确认要取消订单吗？');

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`)
  VALUES ('parking', '10022', 'zh_CN', '升起车锁失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`)
  VALUES ('parking', '10023', 'zh_CN', '降下车锁失败');

INSERT INTO `eh_configurations`(`name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ('parking.dingding.url', 'https://public.dingdingtingche.com', NULL, 0, NULL);
INSERT INTO `eh_configurations`(`name`, `value`, `description`, `namespace_id`, `display_name`)
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
-- set @id = (select max(id) from eh_service_module_privileges);
-- INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@id:=@id+1, '10800', '0', '10005', '全部权限', '0', now());

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
update eh_var_field_groups set name = 'com.everhomes.customer.EnterpriseCustomer' where id = 1;


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

-- dengs,新闻分享的url调整。2018.03.06
update eh_configurations set `value`='/park-news-web/build/index.html?ns=%s&isFS=1&widget=News&timeWidgetStyle=time/#/newsDetail?newsToken=%s' WHERE `name`='news.url';
update eh_configurations SET `value`='/html/news_text_review.html' WHERE `name`='news.content.url';

-- dengs,工位预定的url修改，2018.03.07
SET @home_url=(SELECT `value` from eh_configurations WHERE `NAME`='home.url' and namespace_id=0);
update eh_launch_pad_items SET action_data=CONCAT('{"url":"',@home_url,'/station-booking-web/build/index.html#/home#sign_suffix"}') WHERE action_type in (13,14) AND action_data LIKE '%station-booking/index.html%';
update eh_service_modules SET instance_config=CONCAT('{"url":"',@home_url,'/station-booking-web/build/index.html#/home#sign_suffix"}') WHERE id=40200;
update eh_service_module_apps SET instance_config=CONCAT('{"url":"',@home_url,'/station-booking-web/build/index.html#/home#sign_suffix"}') WHERE module_id=40200;

-- 文件管理添加菜单，dengs，2018.03.07
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('74060000', '文件管理', '74000000', NULL, 'file-management', '1', '2', '/74000000/74060000', 'organization', '6', '41500', '2', 'system', 'module', '2');


-- st.zheng 服务联盟物业报修跳转调整
update eh_service_alliance_jump_module set  module_url = REPLACE(module_url,'taskCategoryId=0', 'taskCategoryId=6') where module_url like '%zl://propertyrepair/create?type=user&taskCategoryId=0&displayName=%';


-- 更新活动详情页页面路径。 add by yanjun 201803091110
UPDATE eh_configurations SET `value` = '/html/activity_text_review.html' WHERE `name` = 'activity.content.url';

-- 富文本链接路径  add by yanjun 201803121105
SET @id = (SELECT MAX(id) from eh_configurations);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ((@id := @id + 1), 'rich.text.content.url', '/mobile/static/rich_text_view/index.html', 'rich.text.content.url', '0', NULL);

-- 更新模块及菜单  add by yanjun 201803131611
DELETE  from eh_service_modules;
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10000, '信息发布', 0, '/10000', 1, 1, 2, 0, '2016-12-06 11:40:50', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10100, '论坛', 10000, '/10000/10100', 1, 2, 2, 0, '2016-12-06 11:40:50', NULL, 62, '2017-09-08 18:59:10', 0, 0, '0', 1, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10200, '园区简介', 10000, '/10000/10200', 1, 2, 2, 0, '2016-12-06 11:40:50', NULL, 13, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10300, '公告', 10000, '/10000/10300', 1, 2, 2, 0, '2018-01-29 10:14:25', NULL, NULL, '2018-01-29 10:14:25', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10400, '广告管理', 10000, '/10000/10400', 1, 2, 2, 0, '2016-12-06 11:40:50', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10500, '园区电子报', 10000, '/10000/10400', 1, 2, 2, 0, '2018-01-29 10:14:25', '{\"url\":\"${home.url}/park-paper/index.html?hideNavigationBar=1#/epaper_index#sign_suffix\"}', 13, '2018-01-29 10:14:25', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10600, '活动管理', 10000, '/10000/10600', 1, 2, 2, 0, '2016-12-06 11:40:50', '{\"publishPrivilege\":1,\"livePrivilege\":0,\"listStyle\":2,\"scope\":3,\"style\":4,\"title\": \"活动管理\"}', 61, '2017-09-08 18:59:10', 0, 0, '0', 1, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10700, '路演直播', 10000, '/10000/10700', 1, 2, 0, 0, '2016-12-06 11:40:50', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10750, '俱乐部', 10000, '/10000/10750', 1, 2, 2, 0, '2016-12-06 11:43:04', NULL, 36, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10760, '行业协会', 10000, '/10000/10760', 1, 2, 2, 0, '2017-11-24 14:40:35', NULL, 38, '2017-11-24 14:40:35', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10800, '园区快讯', 10000, '/10000/10800', 1, 2, 2, 0, '2016-12-06 11:40:50', '{\"timeWidgetStyle\":\"date\",\"entityCount\":0,\"subjectHeight\":0,\"descriptionHeight\":0}', 48, '2017-09-08 18:59:10', 0, 0, '0', 1, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (10850, '园区报', 10000, '/10000/10850', 1, 2, 2, 0, '2016-12-06 11:40:50', '{\"url\":\"${home.url}/energy-management/index.html?hideNavigationBar=1#/address_choose#sign_suffix\"}', 13, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (11000, '一键推送', 10000, '/10000/11000', 1, 2, 2, 0, '2016-12-06 11:40:50', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (12200, '短信推送', 10000, '/10000/12200', 1, 2, 2, 0, '2016-12-06 11:43:45', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (13000, '任务管理', 50000, '/50000/13000', 1, 2, 2, 0, '2017-04-10 10:54:08', NULL, 56, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20000, '物业服务', 0, '/20000', 1, 1, 2, 0, '2016-12-06 11:40:50', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20100, '物业报修', 20000, '/20000/20100', 1, 2, 2, 0, '2016-12-06 11:40:50', '{\"url\":\"zl://propertyrepair/create?type=user&taskCategoryId=6&displayName=物业报修\"}', 60, '2017-09-08 18:59:10', 0, 0, '0', 1, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20140, '任务列表', 20100, '/20000/20100/20140', 1, 3, 2, 0, '2017-08-25 14:46:49', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20150, '服务录入', 20100, '/20000/20100/20150', 1, 3, 2, 0, '2017-08-25 14:46:49', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20155, '设置', 20100, '/20000/20100/20155', 1, 3, 0, 0, '2017-08-25 14:46:49', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20190, '统计', 20100, '/20000/20100/20190', 1, 3, 2, 0, '2017-08-25 14:46:49', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20400, '物业缴费', 20000, '/20000/20400', 1, 2, 2, 0, '2017-12-18 21:31:24', NULL, 13, '2017-12-18 21:31:24', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20430, '交易明细', 20400, '/20000/20400/20430', 1, 3, 2, 0, '2017-12-18 21:31:24', NULL, NULL, '2017-12-18 21:31:24', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20600, '品质核查', 20000, '/20000/20600', 1, 2, 2, 0, '2016-12-06 11:40:51', '{\"realm\":\"qualityInspection\",\"entryUrl\":\"https://core.zuolin.com/nar/qualityInspection/build/index.html?hideNavigationBar=1#/home#sign_suffix\"}', 44, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20610, '类型管理', 20600, '/20000/20600/20610', 1, 3, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20620, '规范管理', 20600, '/20000/20600/20620', 1, 3, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20630, '计划管理', 20600, '/20000/20600/20630', 1, 3, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20640, '计划审批', 20600, '/20000/20600/20640', 1, 3, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20650, '任务管理', 20600, '/20000/20600/20650', 1, 3, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20655, '绩效考核', 20600, '/20000/20600/20655', 1, 3, 2, 0, '2017-07-06 20:16:18', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20660, '统计分析', 20600, '/20000/20600/20660', 1, 3, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20661, '分数统计', 20600, '/20000/20600/20660/20661', 1, 4, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20662, '任务数统计', 20600, '/20000/20600/20660/20662', 1, 4, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20663, '检查统计', 20600, '/20000/20600/20660/20663', 1, 4, 2, 0, '2017-08-25 10:05:19', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20670, '日志管理', 20600, '/20000/20600/20670', 1, 3, 2, 0, '2017-07-06 20:16:18', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20800, '设备巡检', 20000, '/20000/20800', 1, 2, 2, 0, '2016-12-06 11:40:51', '{\"realm\":\"equipmentInspection\",\"entryUrl\":\"https://core.zuolin.com/nar/equipmentInspection/dist/index.html?hideNavigationBar=1#sign_suffix\"}', 44, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20810, '标准管理', 20800, '/20000/20800/20810', 1, 3, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20811, '标准列表', 20800, '/20000/20800/20810/20811', 1, 4, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20812, '巡检关联审批', 20800, '/20000/20800/20810/20812', 1, 4, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20820, '台帐管理', 20800, '/20000/20800/20820', 1, 3, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20821, '巡检对象', 20800, '/20000/20800/20820/20821', 1, 4, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20822, '备品备件', 20800, '/20000/20800/20820/20822', 1, 4, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20830, '任务管理', 20800, '/20000/20800/20830', 1, 3, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20831, '任务列表', 20800, '/20000/20800/20830/20831', 1, 4, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20840, '计划管理', 20800, '/20000/20800/20840', 1, 3, 2, 0, '2017-07-06 20:16:25', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20841, '巡检项设置', 20800, '/20000/20800/20840/20841', 1, 4, 2, 0, '2017-07-06 20:16:25', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20850, '统计分析', 20800, '/20000/20800/20850', 1, 3, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20851, '总览', 20800, '/20000/20800/20850/20851', 1, 4, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20852, '查看所有任务', 20800, '/20000/20800/20850/20852', 1, 4, 2, 0, '2017-08-25 10:05:25', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (20900, '车辆放行', 40000, '/40000/20900', 1, 2, 2, 0, '2017-01-19 12:12:53', '', 57, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21000, '仓库管理', 20000, '/20000/21000', 1, 2, 2, 0, '2017-05-27 10:16:28', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21010, '仓库维护', 21000, '/20000/21000/21010', 1, 3, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21020, '物品维护', 21000, '/20000/21000/21020', 1, 3, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21022, '物品信息', 21000, '/20000/21000/21020/21022', 1, 4, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21024, '物品分类', 21000, '/20000/21000/21020/21024', 1, 4, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21030, '库存维护', 21000, '/20000/21000/21030', 1, 3, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21032, '库存查询', 21000, '/20000/21000/21030/21032', 1, 4, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21034, '库存日志', 21000, '/20000/21000/21030/21034', 1, 4, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21040, '领用管理', 21000, '/20000/21000/21040', 1, 3, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21042, '领用管理', 21000, '/20000/21000/21040/21042', 1, 4, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21044, '我的领用', 21000, '/20000/21000/21040/21044', 1, 4, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21050, '参数配置', 21000, '/20000/21000/21050', 1, 3, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21052, '工作流设置', 21000, '/20000/21000/21050/21052', 1, 4, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21054, '参数配置', 21000, '/20000/21000/21050/21054', 1, 4, 2, 0, '2017-08-25 10:05:31', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21100, '企业客户管理', 30000, '/30000/21100', 1, 2, 2, 0, '2017-09-07 13:16:34', NULL, 13, '2017-09-08 18:59:10', 0, 1, '1', NULL, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21110, '客户列表', 21100, '/30000/21100/21110', 1, 3, 2, 0, '2017-12-28 19:50:06', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21120, '统计分析', 21100, '/30000/21100/21120', 1, 3, 2, 0, '2017-12-28 19:50:06', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21200, '合同管理', 20000, '/20000/21200', 1, 2, 2, 0, '2017-09-07 13:16:34', NULL, NULL, '2017-09-08 18:59:10', 0, 1, '1', NULL, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21210, '合同列表', 21200, '/20000/21200/21210', 1, 3, 2, 0, '2017-12-28 19:50:07', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21215, '付款合同', 21200, '/20000/21200/21215', 1, 3, 2, 0, '2018-01-24 14:27:33', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21220, '合同基础参数配置', 21200, '/20000/21200/21220', 1, 3, 2, 0, '2017-12-28 19:50:07', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21230, '工作流配置', 21200, '/20000/21200/21230', 1, 3, 2, 0, '2017-12-28 19:50:07', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21300, '付款管理', 20000, '/20000/21300', 1, 2, 2, 0, '2018-01-24 14:27:33', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21310, '付款申请单', 21300, '/20000/21300/21310', 1, 3, 2, 0, '2018-01-24 14:27:33', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (21320, '工作流设置', 21300, '/20000/21300/21320', 1, 3, 2, 0, '2018-01-24 14:27:33', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (22000, '装修申请', 20000, '/20000/22000', 1, 2, 2, 0, '2018-01-29 13:45:41', NULL, NULL, '2018-01-29 13:45:41', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (23000, '迁入申请', 20000, '/20000/23000', 1, 2, 2, 0, '2018-01-29 13:45:42', NULL, NULL, '2018-01-29 13:45:42', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (24000, '迁出申请', 20000, '/20000/24000', 1, 2, 2, 0, '2018-01-29 13:45:42', NULL, NULL, '2018-01-29 13:45:42', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (25000, '请示单管理', 20000, '/20000/25000', 1, 2, 2, 0, '2018-02-27 16:43:04', NULL, NULL, '2018-02-27 16:43:04', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (26000, '采购管理', 20000, '/20000/26000', 1, 2, 2, 0, '2018-01-29 13:45:42', NULL, NULL, '2018-01-29 13:45:42', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (27000, '供应商管理', 20000, '/20000/27000', 1, 2, 2, 0, '2018-01-29 13:45:42', NULL, NULL, '2018-01-29 13:45:42', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (30000, '项目管理', 0, '/30000', 1, 1, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (30500, '项目列表', 30000, '/30000/30500', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (30600, '黑名单管理', 30000, '/30000/30600', 1, 2, 2, 0, '2016-12-06 11:46:41', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (31000, '楼栋管理', 30000, '/30000/31000', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (32000, '门牌管理', 30000, '/30000/32000', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (32500, '合同管理（科技园）', 20000, '/20000/32500', 1, 2, 2, 0, '2016-12-06 11:43:19', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (33000, '企业管理', 30000, '/30000/33000', 1, 2, 2, 0, '2016-12-06 11:40:51', '{\"type\":3}', 34, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (34000, '用户管理', 30000, '/30000/34000', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (35000, '用户认证', 30000, '/30000/35000', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (36000, '入驻申请', 30000, '/30000/36000', 1, 2, 2, 0, '2018-01-29 13:54:30', NULL, 71, '2018-01-29 13:54:30', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (37000, '个人客户管理', 30000, '/30000/37000', 1, 2, 2, 0, '2017-05-11 20:08:38', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', NULL, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (38000, '资产管理', 30000, '/30000/38000', 1, 2, 2, 0, '2018-01-29 13:55:30', NULL, NULL, '2018-01-29 13:55:30', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40000, '运营服务', 0, '/40000', 1, 1, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40050, '位置预订', 40000, '/40000/40050', 1, 2, 0, 0, '2017-03-07 11:27:15', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40070, '园区地图', 40000, '/40000/40070', 1, 2, 2, 0, '2018-01-29 13:57:26', NULL, 67, '2018-01-29 13:57:26', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40100, '招租管理', 40000, '/40000/40100', 1, 2, 2, 0, '2016-12-06 11:40:51', '', 28, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40200, '工位预订', 40000, '/40000/40200', 1, 2, 2, 0, '2016-12-06 11:40:51', '{"url":"${home.url}/station-booking-web/build/index.html#/home#sign_suffix"}', 13, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40300, '服务热线', 40000, '/40000/40300', 1, 2, 2, 0, '2016-12-06 11:40:51', '', 45, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40400, '资源预订', 40000, '/40000/40400', 1, 2, 2, 0, '2016-12-06 11:40:51', '{\"pageType\":0}', 49, '2017-09-08 18:59:10', 0, 0, '0', 1, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40500, '服务联盟', 40000, '/40000/40500', 1, 2, 2, 0, '2016-12-06 11:40:51', '{\"displayType\":\"grid\",\"detailFlag\":1}', 33, '2017-09-08 18:59:10', 0, 0, '0', 1, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40510, '样式设置', 40500, '/40000/40500/40510', 1, 3, 2, 0, '2018-03-02 11:00:50', NULL, NULL, '2018-03-02 11:00:50', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40520, '服务管理', 40500, '/40000/40500/40520', 1, 3, 2, 0, '2018-03-02 11:00:50', NULL, NULL, '2018-03-02 11:00:50', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40530, '消息通知', 40500, '/40000/40500/40530', 1, 3, 2, 0, '2018-03-02 11:00:50', NULL, NULL, '2018-03-02 11:00:50', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40540, '申请记录', 40500, '/40000/40500/40540', 1, 3, 2, 0, '2018-03-02 11:00:50', NULL, NULL, '2018-03-02 11:00:50', 0, 1, '1', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40600, '创客空间', 40000, '/40000/40600', 1, 2, 2, 0, '2016-12-06 11:40:51', '{\"type\":1,\"forumId\":177000,\"categoryId\":1003,\"parentId\":110001,\"tag\":\"创客\"}', 32, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40700, '快递服务', 40000, '/40000/40700', 1, 2, 2, 0, '2017-07-18 03:51:18', '{"url":"${home.url}/deliver/dist/index.html#/home_page#sign_suffix"}', 13, '2017-09-08 18:59:10', 2017, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40730, '企业人才', 40000, '/40000/40730', 1, 2, 2, 0, '2018-01-29 13:57:26', '{"url":"${home.url}/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}', 13, '2018-01-29 13:57:26', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40750, '运营统计', 40000, '/40000/40750', 1, 2, 0, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40800, '停车缴费', 40000, '/40000/40800', 1, 2, 2, 0, '2016-12-06 11:40:51', '', 30, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (40900, '车辆管理', 40000, '/40000/40900', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41000, '门禁', 40000, '/10000/41000', 1, 2, 2, 0, '2016-12-06 11:40:51', '{\"isSupportQR\":1,\"isSupportSmart\":0}', 40, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41010, '大堂门禁', 40000, '/10000/41010', 1, 2, 2, 0, '2018-01-29 10:09:15', '{\"isSupportQR\":1,\"isSupportSmart\":0}', NULL, '2018-01-29 10:09:15', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41020, '公司门禁', 40000, '/10000/41020', 1, 2, 2, 0, '2018-01-29 10:09:15', '{\"isSupportQR\":1,\"isSupportSmart\":0}', NULL, '2018-01-29 10:09:15', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41100, 'Wifi热点', 40000, '/40000/41100', 1, 2, 2, 0, '2016-12-06 11:40:51', '', 47, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41200, '一卡通', 40000, '/40000/43500', 1, 2, 2, 0, '2016-12-06 11:40:51', '{"url":"${home.url}/metro_card/index.html?hideNavigationBar=1#/wallet?sign_suffix"}', 13, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41300, '统计分析', 40000, '/40000/41300', 1, 2, 2, 0, '2016-12-28 10:45:07', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41330, '用户行为统计', 41300, '/40000/41300/41330', 0, 2, 2, 0, '2017-09-12 13:54:34', NULL, NULL, NULL, 0, 0, NULL, NULL, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41400, '云打印', 40000, '/40000/41400', 1, 2, 2, 0, '2017-05-11 08:51:49', '{"url":"${home.url}/cloud-print/build/index.html#/home#sign_suffix"}', 13, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41500, '文件管理', 40000, '/40000/41500', 1, 2, 2, 0, '2017-05-11 08:51:49', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41600, '园区审批', 40000, '/40000/41600', 1, 2, 2, 0, '2017-05-11 08:51:49', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (41700, '问卷调查', 40000, '/40000/41700', 1, 2, 2, 0, '2017-05-11 08:51:49', '{"url":"${home.url}/questionnaire-survey/build/index.html#/home#sign_suffix "}', 13, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (47000, '积分管理', 40000, '/40000/47000', 0, 2, 2, 0, '2018-01-05 12:17:18', NULL, NULL, NULL, 0, 0, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (49100, '能耗管理', 20000, '/20000/49100', 1, 2, 2, 0, '2016-12-06 11:46:41', '{"realm":"energyManagement","entryUrl":"https://core.zuolin.com/nar/energyManagement/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}', 44, '2017-09-08 18:59:10', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (49110, '表计管理', 49100, '/20000/49100/49110', 1, 3, 2, 1, '2017-11-16 19:09:05', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (49120, '抄表记录', 49100, '/20000/49100/49120', 1, 3, 2, 3, '2017-11-16 19:09:05', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (49130, '统计信息', 49100, '/20000/49100/49130', 1, 3, 2, 4, '2017-11-16 19:09:05', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (49140, '参数设置', 49100, '/20000/49100/49140', 1, 3, 2, 5, '2017-11-16 19:09:05', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (49150, '计划管理', 49100, '/20000/49100/49150', 1, 3, 2, 2, '2017-11-16 19:09:05', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (49200, '物品搬迁', 40000, '/40000/49200', 1, 2, 2, 0, '2017-12-18 13:29:26', NULL, NULL, '2017-12-18 13:29:26', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50000, '内部管理', 0, '/50000', 1, 1, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50100, '组织架构', 50000, '/50000/50100', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, 46, '2017-09-08 18:59:10', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50200, '岗位管理', 50000, '/50000/50200', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50300, '职级管理', 50000, '/50000/50300', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50400, '人事档案', 50000, '/50000/50400', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50500, '员工认证', 50000, '/50000/50500', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50600, '考勤管理', 50000, '/50000/50600', 1, 2, 2, 0, '2016-12-06 11:40:51', '', 23, '2017-09-08 18:59:10', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50700, '视频会议', 50000, '/50000/50700', 1, 2, 2, 0, '2016-12-06 11:40:51', '', 27, '2017-09-08 18:59:10', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (50900, '表单管理', 50000, '/50000/50900', 1, 2, 2, 0, '2016-12-06 11:40:51', '', NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (51000, '举报管理', 50000, '/50000/51000', 1, 2, 2, 0, '2017-05-10 06:05:34', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, 'unlimit_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (51300, '社保管理', 50000, '/50000/51300', 1, 2, 2, 0, '2018-01-29 14:08:34', NULL, NULL, '2018-01-29 14:08:34', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (51400, '薪酬管理', 50000, '/50000/51400', 1, 2, 2, 0, '2018-01-29 14:08:34', NULL, NULL, '2018-01-29 14:08:34', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (52000, '审批管理', 50000, '/50000/52000', 1, 2, 2, 0, '2017-01-19 11:50:20', NULL, 65, '2017-09-08 18:59:10', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (54000, '工作汇报', 50000, '/50000/54000', 1, 2, 2, 0, '2017-12-20 11:50:20', NULL, 68, '2017-12-20 10:45:46', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (55000, '文档管理', 50000, '/50000/55000', 1, 2, 2, 0, '2018-01-29 14:08:34', NULL, NULL, '2018-01-29 14:08:34', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (56100, '绩效管理', 50000, '/50000/56100', 1, 2, 2, 0, '2018-01-29 14:08:34', NULL, NULL, '2018-01-29 14:08:34', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (56200, '公文管理', 50000, '/50000/56200', 1, 2, 2, 0, '2018-01-29 14:08:34', NULL, NULL, '2018-01-29 14:08:34', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (56300, '待办事项', 50000, '/50000/56300', 1, 2, 2, 0, '2018-01-29 14:08:35', NULL, NULL, '2018-01-29 14:08:35', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (57000, '公告管理', 50000, '/50000/57000', 1, 2, 2, 0, '2018-01-31 10:30:45', NULL, NULL, '2018-01-31 10:30:45', 0, 0, '0', 0, 'org_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (60000, '系统管理', 0, '/60000', 1, 1, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (60100, '管理员管理', 60000, '/60000/60100', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (60110, '超级管理员', 60100, '/60000/60100/60110', 1, 3, 2, 0, '2017-09-04 17:09:12', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (60120, '模块管理员', 60100, '/60000/60100/60120', 1, 3, 2, 0, '2017-09-04 17:09:12', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (60200, '责任部门配置', 60000, '/60000/60200', 1, 2, 2, 0, '2016-12-06 11:40:51', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (60210, '责任部门配置', 60200, '/60000/60200/60210', 1, 3, 2, 0, '2017-09-04 17:09:12', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (61000, '文件下载中心', 60000, '/60000/61000', 1, 2, 2, 0, '2017-12-28 11:48:55', NULL, NULL, NULL, 1, 1, NULL, NULL, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (70000, '域空间管理', 0, '/70000', 1, 1, 2, 0, '2016-12-28 10:47:42', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (70100, '业务应用配置', 70000, '/70000/70100', 1, 2, 2, 0, '2017-04-10 10:54:08', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (70200, 'App配置', 70000, '/70000/70200', 1, 2, 2, 0, '2017-04-10 10:54:08', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (70300, '菜单配置', 70000, '/70000/70300', 1, 2, 2, 0, '2017-11-27 19:35:38', NULL, NULL, '2017-11-27 19:35:38', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (90000, '第三方服务模块', 0, '/90000', 3, 1, 2, 0, '2017-07-04 15:55:50', NULL, NULL, '2017-09-08 18:59:10', 0, 0, '0', 0, '');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (204011, '账单管理', 20400, '/20000/20400/204011', 1, 3, 2, 0, '2017-12-18 21:31:24', NULL, NULL, '2017-12-18 21:31:24', 0, 0, '0', 0, 'community_control');
INSERT INTO `eh_service_modules`(`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES (204021, '账单统计', 20400, '/20000/20400/204021', 1, 3, 2, 0, '2017-12-18 21:31:24', NULL, NULL, '2017-12-18 21:31:24', 0, 0, '0', 0, 'community_control');

-- 更新模块及菜单  add by yanjun 201803131611
DELETE  from eh_web_menus;
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (11000000, '系统管理', 0, 'system-management', NULL, 1, 2, '/11000000', 'zuolin', 1, 0, 1, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (11010000, '管理员管理', 11000000, NULL, 'zladmin-management', 1, 2, '/11000000/11010000', 'zuolin', 1, 60100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (11020000, '短信管理', 11000000, NULL, 'sms-management', 0, 2, '/11000000/11020000', 'zuolin', 2, 12200, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (11030000, '文件下载中心', 11000000, NULL, 'file-center', 1, 2, '/11000000/11030000', 'zuolin', 3, 61000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (12010000, '城市与区县管理', 12000000, NULL, NULL, 0, 2, '/12000000/12010000', 'zuolin', 1, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (12020000, '基础分类管理', 12000000, NULL, NULL, 0, 2, '/12000000/12020000', 'zuolin', 2, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (12030000, '消息模板配置', 12000000, NULL, NULL, 0, 2, '/12000000/12030000', 'zuolin', 3, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (12040000, '业务模块管理', 12000000, NULL, NULL, 0, 2, '/12000000/12040000', 'zuolin', 4, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (13010000, '系统升级配置', 13000000, NULL, NULL, 1, 2, '/13000000/13010000', 'zuolin', 1, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (13020000, '服务器配置', 13000000, NULL, NULL, 1, 2, '/13000000/13020000', 'zuolin', 2, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (14000000, '域空间管理', 0, 'space-management', '', 1, 2, '/14000000', 'zuolin', 4, NULL, 1, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (14010000, '域空间配置', 14000000, NULL, 'app-config', 1, 2, '/14000000/14010000', 'zuolin', 1, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (14020000, '主题配置', 14000000, NULL, 'theme-setting', 1, 2, '/14000000/14020000', 'zuolin', 2, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (15000000, '基础数据管理', 0, 'basic-data', NULL, 1, 2, '/15000000', 'zuolin', 5, NULL, 1, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (15020000, '用户管理', 15000000, NULL, 'user-management', 1, 2, '/15000000/15020000', 'zuolin', 2, 34000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (15030100, '社区管理公司管理', 15030000, NULL, NULL, 0, 2, '/15000000/15300000/15030100', 'zuolin', 1, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (15030200, '物业管理公司', 15030000, NULL, NULL, 0, 2, '/15000000/15300000/15030200', 'zuolin', 2, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (15030300, '普通企业管理', 15030000, NULL, NULL, 0, 2, '/15000000/15300000/15030300', 'zuolin', 3, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16000000, '业务模块管理', 0, 'operation-business', NULL, 1, 2, '/16000000', 'zuolin', 6, NULL, 1, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16010000, '基础信息模块', 16000000, NULL, NULL, 1, 2, '/16000000/16010000', 'zuolin', 1, NULL, 2, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16010100, '资产管理', 16010000, NULL, 'asset-management', 1, 2, '/16000000/16010000/16010100', 'zuolin', 1, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16010200, '用户认证', 16010000, NULL, 'user-identification', 1, 2, '/16000000/16010000/16010200', 'zuolin', 2, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16010300, '企业管理', 16010000, NULL, 'enterprise-management', 1, 2, '/16000000/16010000/16010300', 'zuolin', 3, 33000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16010400, '企业客户管理', 16010000, NULL, 'customer-management', 1, 2, '/16000000/16010000/16010400', 'zuolin', 4, 21100, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16010500, '个人客户管理', 16010000, NULL, 'individual-customer', 1, 2, '/16000000/16010000/16010500', 'zuolin', 5, 37000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16010600, '车辆管理', 16010000, NULL, 'car-management', 1, 2, '/16000000/16010000/16010600', 'zuolin', 6, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16010700, '责任部门配置', 16010000, NULL, 'bussiness-authorization', 1, 2, '/16000000/16010000/16010700', 'zuolin', 7, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020000, '社群运营模块', 16000000, '', NULL, 1, 2, '/16000000/16020000', 'zuolin', 2, NULL, 2, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020100, '公告', 16020000, NULL, 'notice-management', 1, 2, '/16000000/16020000/16020100', 'zuolin', 1, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020200, '广告管理', 16020000, NULL, 'advert-management', 1, 2, '/16000000/16020000/16020200', 'zuolin', 2, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020300, '一键推送', 16020000, NULL, 'message-push', 1, 2, '/16000000/16020000/16020300', 'zuolin', 3, 11000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020400, '短信推送', 16020000, NULL, 'sms-push', 1, 2, '/16000000/16020000/16020400', 'zuolin', 4, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020500, '积分管理', 16020000, NULL, 'integral-management', 1, 2, '/16000000/16020000/16020500', 'zuolin', 5, 47000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020600, '活动', 16020000, NULL, 'activity-application', 1, 2, '/16000000/16020000/16020600', 'zuolin', 6, 10600, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020700, '论坛', 16020000, NULL, 'forum-management', 1, 2, '/16000000/16020000/16020700', 'zuolin', 7, 10100, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020800, '俱乐部', 16020000, NULL, 'club-management', 1, 2, '/16000000/16020000/16020800', 'zuolin', 8, 10750, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16020900, '行业协会', 16020000, NULL, 'guild-management', 1, 2, '/16000000/16020000/16020900', 'zuolin', 9, 10760, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16021000, '园区快讯', 16020000, NULL, 'park-news', 1, 2, '/16000000/16020000/16021000', 'zuolin', 10, 10800, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16021100, '园区电子报', 16020000, NULL, 'park-newspaper', 1, 2, '/16000000/16020000/16021100', 'zuolin', 11, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16021200, '问卷调查', 16020000, NULL, 'questionnaire-survey', 1, 2, '/16000000/16020000/16021200', 'zuolin', 12, 41700, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16021300, '举报管理', 16020000, NULL, 'report-management', 1, 2, '/16000000/16020000/16021300', 'zuolin', 13, 51000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16021400, '黑名单管理', 16020000, NULL, 'blacklist-management', 1, 2, '/16000000/16020000/16021400', 'zuolin', 14, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16030000, '园区服务模块', 16000000, NULL, NULL, 1, 2, '/16000000/16030000', 'zuolin', 3, NULL, 2, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16030100, '园区入驻', 16030000, NULL, 'rent-management', 1, 2, '/16000000/16030000/16030100', 'zuolin', 2, 40100, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16030200, '入驻申请', 16030000, NULL, 'enter-apply', 1, 2, '/16000000/16030000/16030200', 'zuolin', 2, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16030600, '大堂门禁', 16030000, NULL, 'access-control', 1, 2, '/16000000/16030000/16030600', 'zuolin', 6, 41010, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16030700, '服务联盟', 16030000, NULL, 'service-alliance', 1, 2, '/16000000/16030000/16030700', 'zuolin', 7, 40500, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16030800, '资源预订', 16030000, NULL, 'resource-booking', 1, 2, '/16000000/16030000/16030800', 'zuolin', 8, 40400, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16030900, '工位预订', 16030000, NULL, 'station-booking', 1, 2, '/16000000/16030000/16030900', 'zuolin', 9, 40200, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031000, '停车缴费', 16030000, NULL, 'parking-payment', 1, 2, '/16000000/16030000/16031000', 'zuolin', 10, 40800, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031100, '车辆放行', 16030000, NULL, 'vehicle-release', 1, 2, '/16000000/16030000/16031100', 'zuolin', 11, 20900, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031200, '物品搬迁', 16030000, NULL, 'goods-move', 1, 2, '/16000000/16030000/16031200', 'zuolin', 12, 49200, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031300, '服务热线', 16030000, NULL, 'service-online', 1, 2, '/16000000/16030000/16031300', 'zuolin', 13, 40300, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031400, '云打印', 16030000, NULL, 'cloud-print', 1, 2, '/16000000/16030000/16031400', 'zuolin', 14, 41400, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031500, '快递服务', 16030000, NULL, 'deliver-management', 1, 2, '/16000000/16030000/16031500', 'zuolin', 15, 40700, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031600, '文件管理', 16030000, NULL, 'file-management', 1, 2, '/16000000/16030000/16031600', 'zuolin', 16, 41500, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031700, 'Wifi热点', 16030000, NULL, 'wifi-hotspot', 1, 2, '/16000000/16030000/16031700', 'zuolin', 17, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031800, '园区地图', 16030000, NULL, 'community-map', 1, 2, '/16000000/16030000/16031800', 'zuolin', 18, 40070, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16031900, '企业人才', 16030000, NULL, 'enterprise-talent', 1, 2, '/16000000/16030000/16031900', 'zuolin', 19, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16032000, '园区审批', 16030000, NULL, 'park-approval', 1, 2, '/16000000/16030000/16032000', 'zuolin', 20, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16032100, '一卡通', 16030000, NULL, 'one-card', 1, 2, '/16000000/16030000/16032100', 'zuolin', 21, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040000, '企业OA模块', 16000000, NULL, NULL, 1, 2, '/16000000/16040000', 'zuolin', 4, NULL, 2, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040100, '组织架构', 16040000, NULL, 'organization-structure', 1, 2, '/16000000/16040000/16040100', 'zuolin', 1, 50100, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040200, '职级管理', 16040000, NULL, 'level-management', 1, 2, '/16000000/16040000/16040200', 'zuolin', 2, 50300, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040300, '员工认证', 16040000, NULL, 'employee-identification', 1, 2, '/16000000/16040000/16040300', 'zuolin', 3, NULL, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040400, '人事档案', 16040000, NULL, 'employee-record', 1, 2, '/16000000/16040000/16040400', 'zuolin', 4, 50400, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040500, '考勤管理', 16040000, NULL, 'attendance-management', 1, 2, '/16000000/16040000/16040500', 'zuolin', 5, 50600, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040600, '审批管理', 16040000, NULL, 'approval-management', 1, 2, '/16000000/16040000/16040600', 'zuolin', 6, 52000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040700, '工作汇报', 16040000, NULL, 'work-report', 1, 2, '/16000000/16040000/16040700', 'zuolin', 7, 54000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040800, '社保管理', 16040000, NULL, 'social-security', 1, 2, '/16000000/16040000/16040800', 'zuolin', 8, 51300, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16040900, '薪酬管理', 16040000, NULL, 'salary-management', 1, 2, '/16000000/16040000/16040900', 'zuolin', 9, 51400, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16041000, '文档管理', 16040000, NULL, 'document-management', 1, 2, '/16000000/16040000/16041000', 'zuolin', 10, 55000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16041400, '公司门禁', 16040000, NULL, 'access-control', 1, 2, '/16000000/16040000/16041400', 'zuolin', 14, 41020, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16041600, '公告管理', 16040000, NULL, 'enterprise-notice', 1, 2, '/16000000/16040000/16041600', 'zuolin', 16, 57000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050000, '物业管控模块', 16000000, NULL, NULL, 1, 2, '/16000000/16050000', 'zuolin', 5, NULL, 2, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050100, '物业报修', 16050000, NULL, 'property-repair', 1, 2, '/16000000/16050000/16050100', 'zuolin', 1, 20100, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050200, '合同管理（科技园）', 16050000, NULL, 'contract-list', 1, 2, '/16000000/16050000/16050200', 'zuolin', 2, 32500, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050300, '合同管理', 16050000, NULL, 'contract-management', 1, 2, '/16000000/16050000/16050300', 'zuolin', 3, 21200, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050400, '付款管理', 16050000, NULL, 'pay-management', 1, 2, '/16000000/16050000/16050400', 'zuolin', 4, 21300, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050500, '物业缴费', 16050000, NULL, 'payment-management', 1, 2, '/16000000/16050000/16050500', 'zuolin', 5, 20400, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050600, '物业巡检', 16050000, NULL, 'equipment-management', 1, 2, '/16000000/16050000/16050600', 'zuolin', 6, 20800, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050700, '品质核查', 16050000, NULL, 'quality-inspect', 1, 2, '/16000000/16050000/16050700', 'zuolin', 7, 20600, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050800, '能耗管理', 16050000, NULL, 'energy-management', 1, 2, '/16000000/16050000/16050800', 'zuolin', 8, 49100, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16050900, '仓库管理', 16050000, NULL, 'store-management', 1, 2, '/16000000/16050000/16050900', 'zuolin', 9, 21000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16051000, '采购管理', 16050000, NULL, 'purchase-management', 1, 2, '/16000000/16050000/16051000', 'zuolin', 10, 26000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16051100, '供应商管理', 16050000, NULL, 'supplier-management', 1, 2, '/16000000/16050000/16051100', 'zuolin', 11, 27000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16051200, '请示单管理', 16050000, NULL, 'requisition-management', 1, 2, '/16000000/16050000/16051200', 'zuolin', 12, 25000, 3, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (16060000, '第三方服务模块', 16000000, NULL, 'other-service-modules', 1, 2, '/16000000/16060000', 'zuolin', 6, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (17000000, '运营统计', 0, 'operation-statistics', NULL, 1, 2, '/17000000', 'zuolin', 7, NULL, 1, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (17010000, '应用活跃统计', 17000000, NULL, 'application-statistic', 1, 2, '/17000000/17010000', 'zuolin', 1, 41300, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (17020000, '用户行为统计', 17000000, NULL, 'user-behavior', 1, 2, '/17000000/17020000', 'zuolin', 2, 41300, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (18010000, '电商管理', 18000000, NULL, NULL, 1, 2, '/18000000/18010000', 'zuolin', 1, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (19010000, '对账结算系统', 19000000, NULL, NULL, 1, 2, '/19000000/19010000', 'zuolin', 1, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (20000000, '日志管理', 0, 'daily-record', NULL, 1, 2, '/20000000', 'zuolin', 10, NULL, 1, 'system', 'classify', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (20010000, '服务器性能日志', 20000000, NULL, 'log-parse', 1, 2, '/20000000/20010000', 'zuolin', 1, NULL, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (20020000, '消息状态信息', 20000000, NULL, 'message-status', 1, 2, '/20000000/20020000', 'zuolin', NULL, NULL, 0, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (41000000, '信息发布', 0, 'info_release', NULL, 1, 2, '/41000000', 'park', 1, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (41010000, '公告', 41000000, NULL, 'notice-management', 1, 2, '/41000000/41010000', 'park', 1, 10300, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (41020000, '广告管理', 41000000, NULL, 'advert-management', 1, 2, '/41000000/41020000', 'park', 2, 10400, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (41030000, '园区快讯', 41000000, NULL, 'park-news', 1, 2, '/41000000/41030000', 'park', 3, 10800, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (41040000, '园区电子报', 41000000, NULL, 'park-newspaper', 1, 2, '/41000000/41040000', 'park', 4, 10500, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (41050000, '一键推送', 41000000, NULL, 'message-push', 1, 2, '/41000000/41050000', 'park', 5, 11000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (41060000, '短信推送【科技园定制】', 41000000, NULL, 'sms-push', 1, 2, '/41000000/41060000', 'park', 6, 12200, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42000000, '社群运营', 0, 'operation_community', NULL, 1, 2, '/42000000', 'park', 2, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42010000, '用户管理', 42000000, NULL, 'user-management', 1, 2, '/42000000/42010000', 'park', 1, 34000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42020000, '用户认证', 42000000, NULL, 'user-identification', 1, 2, '/42000000/42020000', 'park', 2, 35000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42030000, '积分管理', 42000000, NULL, 'integral-management', 1, 2, '/42000000/42030000', 'park', 3, 47000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42040000, '活动', 42000000, NULL, 'activity-application', 1, 2, '/42000000/42040000', 'park', 4, 10600, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42050000, '论坛', 42000000, NULL, 'forum-management', 1, 2, '/42000000/42050000', 'park', 5, 10100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42060000, '俱乐部', 42000000, NULL, 'club-management', 1, 2, '/42000000/42060000', 'park', 6, 10750, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42070000, '行业协会', 42000000, NULL, 'guild-management', 1, 2, '/42000000/42070000', 'park', 7, 10760, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42080000, '问卷调查', 42000000, NULL, 'questionnaire-survey', 1, 2, '/42000000/42080000', 'park', 8, 41700, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42090000, '黑名单管理', 42000000, NULL, 'blacklist-management', 1, 2, '/42000000/42090000', 'park', 9, 30600, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (42100000, '举报管理', 42000000, NULL, 'report-management', 1, 2, '/42000000/42100000', 'park', 10, 51000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (43000000, '招商管理', 0, 'business_management', NULL, 1, 2, '/43000000', 'park', 3, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (43010000, '招租管理', 43000000, NULL, 'rent-management', 1, 2, '/43000000/43010000', 'park', 1, 40100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (43020000, '入驻申请【成都项目定制】', 43000000, NULL, NULL, 1, 2, '/43000000/43020000', 'park', 2, 36000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (43030000, '工位预订', 43000000, NULL, 'station-booking', 1, 2, '/43000000/43030000', 'park', 3, 40200, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (43040000, '装修申请【设计中】', 43000000, NULL, NULL, 1, 2, '/43000000/43040000', 'park', 4, 22000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (43050000, '迁入申请【待规划】', 43000000, NULL, NULL, 1, 2, '/43000000/43050000', 'park', 5, 23000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (43060000, '迁出申请【待规划】', 43000000, NULL, NULL, 1, 2, '/43000000/43060000', 'park', 6, 24000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44000000, '租赁管理', 0, 'rent_management', NULL, 1, 2, '/44000000', 'park', 4, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44010000, '资产管理', 44000000, NULL, 'asset-management', 1, 2, '/44000000/44010000', 'park', 1, 38000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44020000, '企业管理【迭代后合并至企业客户管理】', 44000000, NULL, 'enterprise-management', 1, 2, '/44000000/44020000', 'park', 2, 33000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44030000, '企业客户管理', 44000000, NULL, 'customer-management', 1, 2, '/44000000/44030000', 'park', 3, 21100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44040000, '个人客户管理', 44000000, NULL, 'individual-customer', 1, 2, '/44000000/44040000', 'park', 4, 37000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44050000, '合同管理（科技园）', 44000000, NULL, 'contract-list', 1, 2, '/44000000/44050000', 'park', 5, 32500, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44060000, '合同管理', 44000000, NULL, 'contract-management', 1, 2, '/44000000/44060000', 'park', 6, 21200, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44070000, '物业缴费', 44000000, NULL, 'payment-management', 1, 2, '/44000000/44070000', 'park', 7, 20400, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44080000, '付款管理【开发中】', 44000000, NULL, 'pay-management', 1, 2, '/44000000/44080000', 'park', 8, 21300, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (44090000, '车辆管理', 44000000, NULL, 'car-management', 1, 2, '/44000000/44090000', 'park', 9, 40900, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45000000, '运营服务', 0, 'operation_service', NULL, 1, 2, '/45000000', 'park', 2, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45010000, '大堂门禁', 45000000, NULL, 'access-control', 1, 2, '/45000000/45010000', 'park', 1, 41010, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45020000, '资源预订', 45000000, NULL, 'resource-booking', 1, 2, '/45000000/45020000', 'park', 2, 40400, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45030000, '停车缴费', 45000000, NULL, 'parking-payment', 1, 2, '/45000000/45030000', 'park', 3, 40800, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45040000, '车辆放行', 45000000, NULL, 'vehicle-release', 1, 2, '/45000000/45040000', 'park', 4, 20900, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45050000, '物品搬迁', 45000000, NULL, 'goods-move', 1, 2, '/45000000/45050000', 'park', 5, 49200, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45060000, '服务热线', 45000000, NULL, 'service-online', 1, 2, '/45000000/45060000', 'park', 6, 40300, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45070000, '云打印', 45000000, NULL, 'cloud-print', 1, 2, '/45000000/45070000', 'park', 7, 41400, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45080000, '快递服务', 45000000, NULL, 'deliver-management', 1, 2, '/45000000/45080000', 'park', 8, 40700, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45090000, '文件管理', 45000000, NULL, 'file-management', 1, 2, '/45000000/45090000', 'park', 9, 41500, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45100000, 'Wifi热点', 45000000, NULL, 'wifi-hotspot', 1, 2, '/45000000/45100000', 'park', 10, 41100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45110000, '园区地图', 45000000, NULL, 'community-map', 1, 2, '/45000000/45110000', 'park', 11, 40070, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45120000, '企业人才', 45000000, NULL, 'enterprise-talent', 1, 2, '/45000000/45120000', 'park', 12, 40730, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45130000, '园区审批【定制】', 45000000, NULL, 'park-approval', 1, 2, '/45000000/45130000', 'park', 13, 41600, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (45140000, '一卡通【储能对接】', 45000000, NULL, 'one-card', 1, 2, '/45000000/45140000', 'park', 14, 41200, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (46000000, '服务联盟', 0, 'space-management', 'service-alliance', 1, 2, '/46000000', 'park', 6, NULL, 1, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (46010000, '服务联盟', 46000000, NULL, 'service-alliance', 1, 2, '/46000000/46010000', 'park', 1, 40500, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47000000, '物业服务', 0, 'property', NULL, 1, 2, '/47000000', 'park', 7, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47010000, '物业服务', 47000000, NULL, 'property-repair', 1, 2, '/47000000/47010000', 'park', 1, 20100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47020000, '物业巡检', 47000000, NULL, 'equipment-management', 1, 2, '/47000000/47020000', 'park', 2, 20800, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47030000, '品质核查', 47000000, NULL, 'quality-inspect', 1, 2, '/47000000/47030000', 'park', 3, 20600, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47040000, '能耗管理', 47000000, NULL, 'energy-management', 1, 2, '/47000000/47040000', 'park', 4, 49100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47050000, '仓库管理', 47000000, NULL, 'store-management', 1, 2, '/47000000/47050000', 'park', 5, 21000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47060000, '采购管理', 47000000, NULL, 'purchase-management', 1, 2, '/47000000/47060000', 'park', 6, 26000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47070000, '供应商管理', 47000000, NULL, 'supplier-management', 1, 2, '/47000000/47070000', 'park', 7, 27000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (47080000, '请示单管理', 47000000, NULL, 'requisition-management', 1, 2, '/47000000/47080000', 'park', 8, 25000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48000000, '企业OA', 0, 'campus-oa', NULL, 1, 2, '/48000000', 'park', 8, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48010000, '组织架构', 48000000, NULL, 'organization-structure', 1, 2, '/48000000/48010000', 'park', 1, 50100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48020000, '职级管理', 48000000, NULL, 'level-management', 1, 2, '/48000000/48020000', 'park', 2, 50300, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48030000, '员工认证', 48000000, NULL, 'employee-identification', 1, 2, '/48000000/48030000', 'park', 3, 50500, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48040000, '人事档案', 48000000, NULL, 'employee-record', 1, 2, '/48000000/48040000', 'park', 4, 50400, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48050000, '考勤管理', 48000000, NULL, 'attendance-management', 1, 2, '/48000000/48050000', 'park', 5, 50600, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48060000, '审批管理', 48000000, NULL, 'approval-management', 1, 2, '/48000000/48060000', 'park', 6, 52000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48070000, '工作汇报', 48000000, NULL, 'work-report', 1, 2, '/48000000/48070000', 'park', 7, 54000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48080000, '社保管理', 48000000, NULL, 'social-security', 1, 2, '/48000000/48080000', 'park', 8, 51300, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48090000, '薪酬管理', 48000000, NULL, 'salary-management', 1, 2, '/48000000/48090000', 'park', 9, 51400, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48100000, '文档管理', 48000000, NULL, 'document-management', 1, 2, '/48000000/48100000', 'park', 10, 55000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48110000, '绩效管理', 48000000, NULL, NULL, 1, 2, '/48000000/48110000', 'park', 11, 56100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48120000, '公文管理', 48000000, NULL, NULL, 1, 2, '/48000000/48120000', 'park', 12, 56200, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48130000, '待办事项', 48000000, NULL, NULL, 1, 2, '/48000000/48130000', 'park', 13, 56300, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48140000, '公司门禁', 48000000, NULL, 'access-control', 1, 2, '/48000000/48140000', 'park', 14, 41020, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (48160000, '公告管理', 48000000, NULL, 'enterprise-notice', 1, 2, '/48000000/48160000', 'park', 16, 57000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (49000000, '运营统计', 0, 'operation-statistics', NULL, 1, 2, '/49000000', 'park', 9, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (49010000, '应用活跃统计', 49000000, NULL, 'application-statistic', 1, 2, '/49000000/49010000', 'park', 1, 41300, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (49020000, '用户行为统计', 49000000, NULL, 'user-behavior', 1, 2, '/49000000/49020000', 'park', 2, 41300, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (50000000, '任务管理', 0, 'mission_management', 'task-management', 0, 2, '/50000000', 'park', 10, NULL, 1, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (50010000, '任务管理', 50000000, 'mission_management', 'task-management', 1, 2, '/50000000/50010000', 'park', 1, 13000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (51000000, '系统管理', 0, 'system-management', NULL, 1, 2, '/51000000', 'park', 11, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (51010000, '管理员管理', 51000000, NULL, 'admin-management', 1, 2, '/51000000/51010000', 'park', 1, 60100, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (51020000, '责任部门配置', 51000000, NULL, 'bussiness-authorization', 1, 2, '/51000000/51020000', 'park', 2, 60200, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (51030000, '文件下载中心', 51000000, NULL, 'file-center', 1, 2, '/51000000/51030000', 'park', 3, 61000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (71000000, '信息发布', 0, 'info_release', NULL, 1, 2, '/71000000', 'organization', 1, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (71010000, '公告', 71000000, NULL, 'notice-management', 1, 2, '/71000000/71010000', 'organization', 1, 10300, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (71020000, '论坛', 71000000, NULL, 'advert-management', 1, 2, '/71000000/71020000', 'organization', 2, 10100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (71030000, '活动管理', 71000000, NULL, 'park-news', 1, 2, '/71000000/71030000', 'organization', 3, 10600, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (71040000, '一键推送', 71000000, NULL, 'message-push', 1, 2, '/71000000/71040000', 'organization', 4, 11000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72000000, '企业OA', 0, 'campus-oa', NULL, 1, 2, '/72000000', 'organization', 2, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72010000, '组织架构', 72000000, NULL, 'organization-structure', 1, 2, '/72000000/72010000', 'organization', 1, 50100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72020000, '职级管理', 72000000, NULL, 'level-management', 1, 2, '/72000000/72020000', 'organization', 2, 50300, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72030000, '员工认证', 72000000, NULL, 'employee-identification', 1, 2, '/72000000/72030000', 'organization', 3, 50500, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72040000, '人事档案', 72000000, NULL, 'employee-record', 1, 2, '/72000000/72040000', 'organization', 4, 50400, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72050000, '考勤管理', 72000000, NULL, 'attendance-management', 1, 2, '/72000000/72050000', 'organization', 5, 50600, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72060000, '审批管理', 72000000, NULL, 'approval-management', 1, 2, '/72000000/72060000', 'organization', 6, 52000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72070000, '工作汇报', 72000000, NULL, 'work-report', 1, 2, '/72000000/72070000', 'organization', 7, 54000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72080000, '社保管理', 72000000, NULL, 'social-security', 1, 2, '/72000000/72080000', 'organization', 8, 51300, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72090000, '薪酬管理', 72000000, NULL, 'salary-management', 1, 2, '/72000000/72090000', 'organization', 9, 51400, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72100000, '文档管理', 72000000, NULL, 'document-management', 1, 2, '/72000000/72100000', 'organization', 10, 55000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72110000, '绩效管理', 72000000, NULL, NULL, 1, 2, '/72000000/72110000', 'organization', 11, 56100, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72120000, '公文管理', 72000000, NULL, NULL, 1, 2, '/72000000/72120000', 'organization', 12, 56200, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72130000, '待办事项', 72000000, NULL, NULL, 1, 2, '/72000000/72130000', 'organization', 13, 56300, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72140000, '公司门禁', 72000000, NULL, 'access-control', 1, 2, '/72000000/72140000', 'organization', 14, 41020, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (72160000, '公告管理', 72000000, NULL, 'enterprise-notice', 1, 2, '/72000000/72160000', 'organization', 16, 57000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (73000000, '任务管理', 0, 'mission_management', NULL, 1, 2, '/73000000', 'organization', 3, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (73010000, '任务管理', 73000000, 'mission_management', 'task-management', 1, 2, '/73000000/73010000', 'organization', 1, 13000, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (74000000, '园区服务', 0, 'space-management', NULL, 1, 2, '/74000000', 'organization', 2, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (74010000, '服务联盟', 74000000, NULL, 'service-alliance', 1, 2, '/74000000/74010000', 'organization', 1, 40500, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (74020000, '问卷调查', 74000000, NULL, 'questionnaire-survey', 1, 2, '/74000000/74020000', 'organization', 2, 41700, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (74030000, '文件下载', 74000000, NULL, 'employee-identification', 1, 2, '/74000000/74030000', 'organization', 3, 61000, 2, 'system', 'module', NULL);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (74040000, '企业服务', 74000000, NULL, 'employee-record', 1, 2, '/74000000/74040000', 'organization', 4, 40500, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (74050000, '办事指南', 74000000, NULL, 'attendance-management', 1, 2, '/74000000/74050000', 'organization', 5, 40500, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (74060000, '文件管理', 74000000, NULL, 'file-management', 1, 2, '/74000000/74060000', 'organization', 6, 41500, 2, 'system', 'module', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (75000000, '系统管理', 0, 'system-management', NULL, 1, 2, '/75000000', 'organization', 5, NULL, 1, 'system', 'classify', 2);
INSERT INTO `eh_web_menus`(`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES (75010000, '系统管理', 75000000, 'system-management', NULL, 1, 2, '/75000000/75010000', 'organization', 1, 60100, 2, 'system', 'module', 2);


-- 企业管理对接权限 25065 add by xiongying20180313
SET @module_privilege_id = (SELECT MAX(id) FROM `eh_service_module_privileges`);
SET @privilege_id = (SELECT MAX(id) FROM `eh_acl_privileges`);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (33000, '0', '企业管理 全部权限', '企业管理 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','1','33000','企业管理管理权限','0',NOW());
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','2','33000','企业管理全部权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (33001, '0', '企业管理 新增权限', '企业管理 新增权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','0',33001,'企业管理 新增权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (33002, '0', '企业管理 修改权限', '企业管理 修改权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','0',33002,'企业管理 修改权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (33003, '0', '企业管理 删除权限', '企业管理 删除权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','0',33003,'企业管理 删除权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (33004, '0', '企业管理 查看权限', '企业管理 查看权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','0',33004,'企业管理 查看权限','0',NOW());    

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (33005, '0', '企业管理 导入权限', '企业管理 导入权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','0',33005,'企业管理 导入权限','0',NOW());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (33006, '0', '企业管理 导出权限', '企业管理 导出权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','0',33006,'企业管理 导出权限','0',NOW());
    
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (33007, '0', '企业管理 管理员设置权限', '企业管理 管理员设置权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) 
    VALUES((@module_privilege_id := @module_privilege_id + 1),'33000','0',33007,'企业管理 管理员设置权限','0',NOW());    
    
delete from eh_reflection_service_module_apps where module_id = 33000;
DROP PROCEDURE IF EXISTS create_app;
DELIMITER //
CREATE PROCEDURE `create_app` ()
BEGIN
  DECLARE ns INTEGER;
  DECLARE moduleId LONG;
  DECLARE done INT DEFAULT FALSE;
  DECLARE cur CURSOR FOR SELECT id FROM eh_namespaces;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
  OPEN cur;
  read_loop: LOOP
                FETCH cur INTO ns;
                IF done THEN
                    LEAVE read_loop;
                END IF;

        SET @app_id = (SELECT MAX(id) FROM `eh_reflection_service_module_apps`);   
        INSERT INTO `eh_reflection_service_module_apps` (`id`, `active_app_id`, `namespace_id`, `name`, `module_id`, `instance_config`, `status`, `action_type`, `action_data`, `update_time`, `module_control_type`, `multiple_flag`, `custom_tag`, `custom_path`, `menu_id`) VALUES ((@app_id := @app_id + 1), @app_id, ns, '', 33000, NULL, '2', '13', 'organization', NOW(), 'community_control', '0', '', NULL, 33000);

  END LOOP;
  CLOSE cur;
END
//
DELIMITER ;
CALL create_app;
DROP PROCEDURE IF EXISTS create_app;    

-- fix 25656 by xiongying20180315
update eh_var_fields set field_type = 'String' where name = 'buildingRename';

-- 更新普通公司论坛、活动的菜单路由  add by yanjun 201803141806
update eh_web_menus set data_type = 'forum-management' where id = 71020000;
update eh_web_menus set data_type = 'activity-application' where id = 71030000;

-- 园区客户线，权限对接开始 by dengs.
-- by dengs,20180302,服务联盟权限项添加 和 yan.jun sql冲突，我的屏蔽
DELETE from eh_service_modules WHERE id in (40510,40520,40530,40540);
DELETE from eh_acl_privileges WHERE id in (4050040510,4050040520,4050040530,4050040540);
DELETE from eh_service_module_privileges WHERE module_id in (40510,40520,40530,40540); 

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40510', '样式设置', '40500', '/40000/40500/40510', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40520', '服务管理', '40500', '/40000/40500/40520', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40530', '消息通知', '40500', '/40000/40500/40530', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40540', '申请记录', '40500', '/40000/40500/40540', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40500', '0', '10024', '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040510, '0', '服务联盟 样式设置权限', '服务联盟 样式设置权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40510', '0', 4050040510, '样式设置权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040520, '0', '服务联盟 服务管理权限', '服务联盟 服务管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40520', '0', 4050040520, '服务管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040530, '0', '服务联盟 消息通知权限', '服务联盟 消息通知权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40530', '0', 4050040530, '消息通知权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4050040540, '0', '服务联盟 申请记录权限', '服务联盟 申请记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40540', '0', 4050040540, '申请记录权限', '0', now());

-- 20100 物业报修 模块已有，重新给权限--------------------------------------------------------------------------------------------
-- 删除物业报修老权限，启用新权限
DELETE from eh_service_module_privileges WHERE module_id in (20140,20150,20190);

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (2010020140, '0', '物业报修 任务列表权限', '物业报修 任务列表权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '20140', '0', 2010020140, '任务列表权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (2010020150, '0', '物业报修 服务录入权限', '物业报修 服务录入权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '20150', '0', 2010020150, '服务录入权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (2010020190, '0', '物业报修 统计权限', '物业报修 统计权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '20190', '0', 2010020190, '统计权限', '0', now());


-- 40800 停车缴费 模块已有，重新给权限--------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40810', '月卡申请', '40800', '/40000/40800/40810', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40820', '车辆认证申请', '40800', '/40000/40800/40820', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40830', 'VIP车位管理', '40800', '/40000/40800/40830', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40840', '订单记录', '40800', '/40000/40800/40840', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040800, '0', '停车缴费 全部权限', '停车缴费 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40800', '0', 4080040800, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040810, '0', '停车缴费 月卡申请权限', '停车缴费 月卡申请权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40810', '0', 4080040810, '月卡申请权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040820, '0', '停车缴费 车辆认证申请权限', '停车缴费 车辆认证申请权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40820', '0', 4080040820, '车辆认证申请权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040830, '0', '停车缴费 VIP车位管理权限', '停车缴费 VIP车位管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40830', '0', 4080040830, 'VIP车位管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4080040840, '0', '停车缴费 订单记录权限', '停车缴费 订单记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40840', '0', 4080040840, '订单记录权限', '0', now());


-- 40400 资源预约 --------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40410', '资源管理', '40400', '/40000/40400/40410', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40420', '订单记录', '40400', '/40000/40400/40420', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4040040400, '0', '资源预约 全部权限', '资源预约 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40400', '0', 4040040400, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4040040410, '0', '资源预约 资源管理权限', '资源预约 资源管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40410', '0', 4040040410, '资源管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4040040420, '0', '资源预约 订单记录权限', '资源预约 订单记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40420', '0', 4040040420, '订单记录权限', '0', now());

-- 40400 车辆放行 --------------------------------------------------------------------------------------------
set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (2090020900, '0', '车辆放行 全部权限', '车辆放行 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '20900', '0', 2090020900, '全部权限', '0', now());

-- 10800 园区快讯 --------------------------------------------------------------------------------------------
DELETE from eh_service_module_privileges WHERE module_id = 10800 and privilege_id=10005;

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1080010800, '0', '园区快讯 全部权限', '园区快讯 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '10800', '0', 1080010800, '全部权限', '0', now());

-- 40100 园区入驻（招租管理） --------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40110', '项目介绍', '40100', '/40000/40100/40410', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40120', '楼栋介绍', '40100', '/40000/40100/40420', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40130', '房源招租', '40100', '/40000/40100/40430', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40140', '申请记录', '40100', '/40000/40100/40440', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4010040100, '0', '园区入驻 全部权限', '园区入驻 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40100', '0', 4010040100, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4010040110, '0', '园区入驻 项目介绍权限', '园区入驻 项目介绍权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40110', '0', 4010040110, '项目介绍权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4010040120, '0', '园区入驻 楼栋介绍权限', '园区入驻 楼栋介绍权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40120', '0', 4010040120, '楼栋介绍权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4010040130, '0', '园区入驻 房源招租权限', '园区入驻 房源招租权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40130', '0', 4010040130, '房源招租权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4010040140, '0', '园区入驻 申请记录权限', '园区入驻 申请记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40140', '0', 4010040140, '申请记录权限', '0', now());

-- 40200 工位预订 --------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40210', '空间管理', '40200', '/40000/40200/40210', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40220', '预定详情', '40200', '/40000/40200/40220', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4020040200, '0', '工位预订 全部权限', '工位预订 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40200', '0', 4020040200, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4020040210, '0', '工位预订 空间管理权限', '工位预订 空间管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40210', '0', 4020040210, '空间管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4020040220, '0', '工位预订 预定详情权限', '工位预订 预定详情权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40220', '0', 4020040220, '预定详情权限', '0', now());

-- 41400 云打印 --------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41410', '打印记录', '41400', '/40000/41400/41410', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41420', '打印统计', '41400', '/40000/41400/41420', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41430', '打印设置', '41400', '/40000/41400/41430', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4140041400, '0', '云打印 全部权限', '云打印 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41400', '0', 4140041400, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4140041410, '0', '云打印 打印记录权限', '云打印 打印记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41410', '0', 4140041410, '打印记录权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4140041420, '0', '云打印 打印统计权限', '云打印 打印统计权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41420', '0', 4140041420, '打印统计权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4140041430, '0', '云打印 打印设置权限', '云打印 打印设置权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41430', '0', 4140041430, '打印设置权限', '0', now());

-- 49200 物品搬迁 --------------------------------------------------------------------------------------------
set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4920049200, '0', '物品搬迁 全部权限', '物品搬迁 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '49200', '0', 4920049200, '全部权限', '0', now());

-- 41700 问卷调查 --------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41710', '已发布', '41700', '/40000/41700/41710', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41720', '草稿箱', '41700', '/40000/41700/41720', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4170041700, '0', '问卷调查 全部权限', '问卷调查 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41700', '0', 4170041700, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4170041710, '0', '问卷调查 已发布权限', '问卷调查 已发布权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41710', '0', 4170041710, '已发布权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4170041720, '0', '问卷调查 草稿箱权限', '问卷调查 草稿箱权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41720', '0', 4170041720, '草稿箱权限', '0', now());


-- 40300 服务热线--------------------------------------------------------------------------------------------
set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4030040300, '0', '服务热线 全部权限', '服务热线 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40300', '0', 4030040300, '全部权限', '0', now());


-- 10500 园区电子报 --------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('10510', '园区报管理', '10500', '/10000/10500/10510', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('10520', '约稿须知', '10500', '/10000/10500/10520', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1050010500, '0', '园区电子报 全部权限', '园区电子报 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '10500', '0', 1050010500, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1050010510, '0', '园区电子报 园区报管理权限', '园区电子报 园区报管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '10510', '0', 1050010510, '园区报管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (1050010520, '0', '园区电子报 约稿须知权限', '园区电子报 约稿须知权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '10520', '0', 1050010520, '约稿须知权限', '0', now());

update eh_web_menus SET module_id=10500 WHERE id=16021100 AND `name`='园区电子报';
update eh_service_modules SET path='/10000/10500' WHERE id=10500;


-- 40070 园区地图 web没有接入--------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40071', '商户管理', '40070', '/40000/40070/40071', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4007040070, '0', '园区地图 全部权限', '园区地图 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40070', '0', 4007040070, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4007040071, '0', '园区地图 商户管理权限', '园区地图 商户管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40071', '0', 4007040071, '商户管理权限', '0', now());


-- 40730 企业人才 --------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40731', '人才管理', '40730', '/40000/40730/40731', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40732', '消息推送', '40730', '/40000/40730/40732', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40733', '申请记录', '40730', '/40000/40730/40733', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4073040730, '0', '企业人才 全部权限', '企业人才 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40730', '0', 4073040730, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4073040731, '0', '企业人才 人才管理权限', '企业人才 人才管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40731', '0', 4073040731, '人才管理权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4073040732, '0', '企业人才 消息推送权限', '企业人才 消息推送权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40732', '0', 4073040732, '消息推送权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4073040733, '0', '企业人才 申请记录权限', '企业人才 申请记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40733', '0', 4073040733, '申请记录权限', '0', now());

update eh_web_menus SET module_id=40730 WHERE id=16031900 AND `name`='企业人才';
set @homeurl = (select `value` from eh_configurations WHERE `name`='home.url' LIMIT 1);
UPDATE eh_service_modules SET action_type=13,instance_config=CONCAT('{"url":"',@homeurl,'/enterprise-talent/build/index.html?hideNavigationBar=1#/home_page#sign_suffix"}') WHERE id=40730;

-- 41200 一卡通 web没有接入--------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41210', '开卡用户', '41200', '/40000/41200/41210', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41220', '导出用户', '41200', '/40000/41200/41220', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41230', '充值记录', '41200', '/40000/41200/41230', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('41240', '消费记录', '41200', '/40000/41200/41240', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4120041200, '0', '一卡通 全部权限', '一卡通 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41200', '0', 4120041200, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4120041210, '0', '一卡通 开卡用户权限', '一卡通 开卡用户权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41210', '0', 4120041210, '开卡用户权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4120041220, '0', '一卡通 导出用户权限', '一卡通 导出用户权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41220', '0', 4120041220, '导出用户权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4120041230, '0', '一卡通 充值记录权限', '一卡通 充值记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41230', '0', 4120041230, '充值记录权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4120041240, '0', '一卡通 消费记录权限', '一卡通 消费记录权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41240', '0', 4120041240, '消费记录权限', '0', now());

update eh_service_modules SET path='/40000/41200' WHERE id=41200;
update eh_web_menus SET module_id=41200 WHERE id=16032100;

-- 41100 一键上网 web没有接入--------------------------------------------------------------------------------------------
set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4110041100, '0', 'Wifi热点 全部权限', 'Wifi热点 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41100', '0', 4110041100, '全部权限', '0', now());

update eh_web_menus SET module_id=41100 WHERE id=16031700 or id=45100000;

-- 40700 快递服务 --------------------------------------------------------------------------------------------
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40710', '参数设置', '40700', '/40000/40700/40710', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('40720', '订单管理', '40700', '/40000/40700/40720', '1', '3', '2', '0', now(), NULL, NULL, now(), '0', '1', '1', NULL, '');

set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4070040700, '0', '快递服务 全部权限', '快递服务 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40700', '0', 4070040700, '全部权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4070040710, '0', '快递服务 参数设置权限', '快递服务 参数设置权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40710', '0', 4070040710, '参数设置权限', '0', now());

INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4070040720, '0', '快递服务 订单管理权限', '快递服务 订单管理权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '40720', '0', 4070040720, '订单管理权限', '0', now());

-- 41100 文件管理 web有点问题--------------------------------------------------------------------------------------------
set @privilege_id = (select max(id) from eh_service_module_privileges);
INSERT INTO `eh_acl_privileges` (`id`, `app_id`, `name`, `description`, `tag`) VALUES (4150041500, '0', '文件管理 全部权限', '文件管理 全部权限', NULL);
INSERT INTO `eh_service_module_privileges` (`id`, `module_id`, `privilege_type`, `privilege_id`, `remark`, `default_order`, `create_time`) VALUES (@privilege_id:=@privilege_id+1, '41500', '0', 4150041500, '全部权限', '0', now());

-- 更新模块的权限方式 --------------------------------------------------------------------------------------------------------------------------
update eh_service_modules SET module_control_type='community_control' WHERE id in (20100,40800,40400,40400,40200,41400,49200,40300,40070,40730,41200,41100,40700,41100);
update eh_service_modules SET module_control_type='unlimit_control' WHERE id in (40500,10800,40100,41700,10500);

update eh_service_module_apps SET module_control_type='community_control' WHERE module_id in (20100,40800,40400,40400,40200,41400,49200,40300,40070,40730,41200,41100,40700,41100);
update eh_service_module_apps SET module_control_type='unlimit_control' WHERE module_id in (40500,10800,40100,41700,10500);

-- service_module app加入module_id
update eh_service_module_apps SET module_id=41400 WHERE action_type=13 AND instance_config LIKE '%/cloud-print/build/index.htm%';
update eh_service_module_apps SET module_id=10200 WHERE action_type=13 AND instance_config LIKE '%/park-introduction/index.html%';
update eh_service_module_apps SET module_id=10800 WHERE action_type=13 AND instance_config LIKE '%/park-news-web/build/index.html%';
update eh_service_module_apps SET module_id=41700 WHERE action_type=13 AND instance_config LIKE '%/questionnaire-survey/build/index.htm%';
update eh_service_module_apps SET module_id=40200 WHERE action_type=13 AND instance_config LIKE '%/station-booking/index.html%';
update eh_service_module_apps SET module_id=49200 WHERE action_type=13 AND instance_config LIKE '%/goods-move/build/index.html%';
update eh_service_module_apps SET module_id=40700 WHERE action_type=13 AND instance_config LIKE '%/deliver/dist/index.html%';
update eh_service_module_apps SET module_id=41200 WHERE action_type=13 AND instance_config LIKE '%/metro_card/index.html%';
update eh_service_module_apps SET module_id=20100 WHERE action_type=13 AND instance_config LIKE '%/property-repair-web/build/index.html%';
update eh_service_module_apps SET module_id=20100 WHERE action_type=13 AND instance_config LIKE '%/property-repair-web/build/index.html%';

-- 园区客户线，权限对接结束 by dengs. 
-- 更新
update eh_service_module_apps set module_id=41700 where module_id = 40150;
update eh_reflection_service_module_apps set module_id=41700 where module_id = 40150;

-- add by yanjun 201803151646 能耗管理改配置
update eh_service_modules set action_type = 44, instance_config = '{"realm":"energyManagement","entryUrl":"https://core.zuolin.com/nar/energyManagement/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' where id = 49100;


-- CDN 配置   add by xq.tian  2018/03/19
SET @configurations_id = IFNULL((SELECT MAX(id) FROM `eh_configurations`), 0);
INSERT INTO `eh_configurations` (`id`, `name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ((@configurations_id := @configurations_id + 1), 'content.cdn.separation_version', '5.3.0', '新旧版本客户端cdn支持的分界版本', 0, NULL);
  
-- fix 25724 by xiongying 20180319
SET @field_id = IFNULL((SELECT MAX(id) FROM `eh_var_fields`), 0);
INSERT INTO `eh_var_fields` (`id`, `module_name`, `name`, `display_name`, `field_type`, `group_id`, `group_path`, `mandatory_flag`, `default_order`, `status`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `field_param`) VALUES ((@field_id := @field_id + 1), 'enterprise_customer', 'buildingId', '楼栋', 'Long', '28', '/28/', '0', NULL, '2', '1', NOW(), NULL, NULL, '{\"fieldParamType\": \"text\", \"length\": 32}');
update eh_var_fields set display_name = '门牌名称' where group_id = 28 and name = 'addressId';
update eh_var_fields set mandatory_flag = 1 where group_id = 28 and name = 'addressId';
update eh_var_fields set mandatory_flag = 1 where group_id = 28 and name = 'buildingId';

-- 增加企业用户模块，add by yanjun 201803191834
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('58000', '企业账户', '50000', '/50000/58000', '1', '2', '2', '0', '2018-03-19 17:52:57', NULL, NULL, '2018-03-19 17:53:11', '0', '0', '0', '0', 'org_control');
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('16070000', '企业账户模块', '16000000', NULL, 'enterprise-account', '1', '2', '/16000000/16070000', 'zuolin', '7', '58000', '2', 'system', 'module', NULL);
INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('52000000', '企业账户', '0', '', 'enterprise-account', '0', '2', '/52000000', 'park', '12', '58000', '1', 'system', 'module', '2');

-- 应彬哥要求删除"文件下载中心"菜单 add by yanjun 201803211012
DELETE from eh_web_menus WHERE data_type = 'file-center' and id in (11030000, 51030000);

-- dengs,20180321.车位，车锁id已存在提示

INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking', '10124', 'zh_CN', '您输入的车位编号已存在，请重新输入');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`) VALUES ('parking', '10125', 'zh_CN', '您输入的车锁ID已存在，请重新输入');

-- 能耗离线包 by xiongying20180321
SET @realm_id = (SELECT MAX(id) FROM `eh_version_realm`);
SET @url_id = (SELECT MAX(id) FROM `eh_version_urls`);
SET @upgrade_id = (SELECT MAX(id) FROM `eh_version_upgrade_rules`);
INSERT INTO `eh_version_realm` (`id`, `realm`, `description`, `create_time`, `namespace_id`) VALUES ((@realm_id := @realm_id + 1), 'energyManagement', NULL, NOW(), '0');
INSERT INTO `eh_version_urls` (`id`, `realm_id`, `target_version`, `download_url`, `info_url`, `upgrade_description`, `namespace_id`, `app_name`, `publish_time`, `icon_url`, `version_encoded_value`) VALUES ((@url_id := @url_id + 1), @realm_id, '1.0.0', 'http://core.zuolin.com/nar/energyManagement/offline/energyManagement-1-0-0-tag.zip', 'http://core.zuolin.com/nar/energyManagement/offline/energyManagement-1-0-0-tag.zip', NULL, '0', NULL, NULL, NULL, '0');
INSERT INTO `eh_version_upgrade_rules` (`id`, `realm_id`, `matching_lower_bound`, `matching_upper_bound`, `order`, `target_version`, `force_upgrade`, `create_time`, `namespace_id`) VALUES ((@upgrade_id := @upgrade_id + 1), @realm_id, '-0.1', '1048576', '0', '1.0.0', '0', NOW(), '0');

-- fix 26107 by xiongying
update eh_service_modules set module_control_type = 'community_control' where id = 21300;

-- 删除项目 楼栋 门牌 fix 26203
delete from eh_reflection_service_module_apps where module_id in(30500,31000,32000);
delete from eh_service_module_apps where module_id in(30500,31000,32000);
delete from eh_service_modules where id in(30500,31000,32000);


-- 增加仓库错误码 by wentian
set @eh_locale_strings_id = (select max(id) from eh_locale_strings);
INSERT INTO `eh_locale_strings` (`id`, `scope`, `code`, `locale`, `text`) VALUES ((@eh_locale_strings_id:=@eh_locale_strings_id+1), 'warehouse', '10026', 'zh_CN', '改仓库正在运行中，不能删除');

-- fix 24787
update eh_var_field_groups set title = '客户信息' where id = 1;
update eh_var_field_group_scopes set group_display_name = '客户信息' where group_id = 1;

-- fix 26123 by xiongying20180323
SET @field_id = (SELECT MAX(id) FROM `eh_var_fields`);
SET @building_id = (select id from eh_var_fields where group_id = 28 and name = 'buildingId');
SET @address_id = ((select id from eh_var_fields where group_id = 28 and name = 'addressId')-1);

update eh_var_fields set id = (@field_id := @field_id + 1) where id = @address_id;
update eh_var_fields set id = @address_id where group_id = 28 and name = 'buildingId';
update eh_var_fields set id = @building_id where id = @field_id;

update eh_service_modules set action_type = 44, instance_config = '{"realm":"energyManagement","entryUrl":"https://core.zuolin.com/nar/energyManagement/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' where id = 49100;
update eh_launch_pad_items set action_type = 44, action_data = '{"realm":"energyManagement","entryUrl":"https://core.zuolin.com/nar/energyManagement/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' where item_label = '能耗管理';
update eh_reflection_service_module_apps set action_type = 44, instance_config = '{"realm":"energyManagement","entryUrl":"https://core.zuolin.com/nar/energyManagement/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}', action_data = '{"realm":"energyManagement","entryUrl":"https://core.zuolin.com/nar/energyManagement/build/index.html?hideNavigationBar=1#/address_choose#sign_suffix"}' where module_id = 49100;


-- 重新布局模块分类  add by yanjun 201803262114 start

INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('80000', '社群运营', '0', '/80000', '1', '1', '2', 20, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('100000', '招商管理', '0', '/100000', '1', '1', '2', 30, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('110000', '租赁管理', '0', '/110000', '1', '1', '2', 40, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('120000', '服务联盟', '0', '/120000', '1', '1', '2', 60, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('130000', '运营统计', '0', '/130000', '1', '1', '2', 90, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('140000', '任务管理', '0', '/140000', '1', '1', '2', 100, NOW(), NULL, NULL, NOW(), '0', '0', '0', '0', '');

UPDATE eh_service_modules set `name` = '企业OA', default_order = 80 where id = 50000;
UPDATE eh_service_modules set default_order = 10  where id = 10000;
UPDATE eh_service_modules set default_order = 50  where id = 40000;
UPDATE eh_service_modules set default_order = 70  where id = 20000;
UPDATE eh_service_modules set default_order = 110  where id = 60000;
UPDATE eh_service_modules set default_order = 130  where id = 90000;

-- 信息发布
UPDATE eh_service_modules SET default_order = 10 WHERE id = 10300;
UPDATE eh_service_modules SET default_order = 20 WHERE id = 10400;
UPDATE eh_service_modules SET default_order = 30 WHERE id = 10800;
UPDATE eh_service_modules SET default_order = 40 WHERE id = 10500;
UPDATE eh_service_modules SET default_order = 50 WHERE id = 10850;
UPDATE eh_service_modules SET default_order = 60 WHERE id = 11000;
UPDATE eh_service_modules SET default_order = 70, `name` = '短信推送【科技园定制】' WHERE id = 12200;

-- 社群运营
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/34000', default_order = 10 WHERE id = 34000;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/35000', default_order = 20 WHERE id = 35000;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/47000', default_order = 30, type = 1 WHERE id = 47000;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/10600', NAME = '活动', default_order = 40 WHERE id = 10600;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/10100', default_order = 50 WHERE id = 10100;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/10750', default_order = 60 WHERE id = 10750;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/10760', default_order = 70 WHERE id = 10760;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/41700', default_order = 80 WHERE id = 41700;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/30600', default_order = 90 WHERE id = 30600;
UPDATE eh_service_modules SET parent_id = 80000 , path = '/80000/51000', default_order = 100 WHERE id = 51000;

-- 招商管理
UPDATE eh_service_modules SET parent_id = 100000 , path = '/100000/40100', default_order = 10 WHERE id = 40100;
UPDATE eh_service_modules SET parent_id = 100000 , path = '/100000/36000', `name` = '入驻申请【创业场定制】', default_order = 20 WHERE id = 36000;
UPDATE eh_service_modules SET parent_id = 100000 , path = '/100000/40200', default_order = 30 WHERE id = 40200;
UPDATE eh_service_modules SET parent_id = 100000 , path = '/100000/22000', default_order = 40 WHERE id = 22000;
UPDATE eh_service_modules SET parent_id = 100000 , path = '/100000/23000', default_order = 50 WHERE id = 23000;
UPDATE eh_service_modules SET parent_id = 100000 , path = '/100000/24000', default_order = 60 WHERE id = 24000;

-- 租赁管理
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/38000', default_order = 10 WHERE id = 38000;
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/33000', default_order = 20 WHERE id = 33000;
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/21100', default_order = 30 WHERE id = 21100;
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/37000', default_order = 40 WHERE id = 37000;
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/21200', default_order = 50 WHERE id = 21200;
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/32500',  default_order = 60, `name`= '合同管理【科技园对接】' WHERE id = 32500;
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/20400', default_order = 70 WHERE id = 20400;
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/21300', default_order = 80 WHERE id = 21300;
UPDATE eh_service_modules SET parent_id = 110000 , path = '/110000/40900', default_order = 90, `name`= '车辆管理【定制】' WHERE id = 40900;

-- 运营服务
UPDATE eh_service_modules SET default_order = 10 WHERE id = 41010;
UPDATE eh_service_modules SET default_order = 20 WHERE id = 40400;
UPDATE eh_service_modules SET default_order = 30 WHERE id = 40800;
UPDATE eh_service_modules SET default_order = 40 WHERE id = 20900;
UPDATE eh_service_modules SET default_order = 50 WHERE id = 49200;
UPDATE eh_service_modules SET default_order = 60 WHERE id = 40300;
UPDATE eh_service_modules SET default_order = 70 WHERE id = 41400;
UPDATE eh_service_modules SET default_order = 80 WHERE id = 40700;
UPDATE eh_service_modules SET default_order = 90 WHERE id = 41500;
UPDATE eh_service_modules SET default_order = 100 WHERE id = 41100;
UPDATE eh_service_modules SET default_order = 110 WHERE id = 40070;
UPDATE eh_service_modules SET default_order = 120 WHERE id = 40730;
UPDATE eh_service_modules SET default_order = 130, `name` = '园区审批【定制】' WHERE id = 41600;
UPDATE eh_service_modules SET default_order = 140, `name` = '一卡通【储能对接】' WHERE id = 41200;

-- 服务联盟
UPDATE eh_service_modules SET parent_id = 120000 , path = '/120000/40500', default_order = 10 WHERE id = 40500;

-- 物业服务
UPDATE eh_service_modules SET default_order = 10, `name` = '物业报修/投诉建议' WHERE id = 20100;
UPDATE eh_service_modules SET default_order = 20, `name` = '物业巡检' WHERE id = 20800;
UPDATE eh_service_modules SET default_order = 30 WHERE id = 20600;
UPDATE eh_service_modules SET default_order = 40 WHERE id = 49100;
UPDATE eh_service_modules SET default_order = 50 WHERE id = 21000;
UPDATE eh_service_modules SET default_order = 60 WHERE id = 26000;
UPDATE eh_service_modules SET default_order = 70 WHERE id = 27000;

-- 企业OA
UPDATE eh_service_modules SET default_order = 10 WHERE id = 50100;
UPDATE eh_service_modules SET default_order = 20 WHERE id = 50300;
UPDATE eh_service_modules SET default_order = 30 WHERE id = 50500;
UPDATE eh_service_modules SET default_order = 40 WHERE id = 50400;
UPDATE eh_service_modules SET default_order = 50 WHERE id = 50600;
UPDATE eh_service_modules SET default_order = 60 WHERE id = 52000;
UPDATE eh_service_modules SET default_order = 70 WHERE id = 54000;
UPDATE eh_service_modules SET default_order = 80 WHERE id = 51300;
UPDATE eh_service_modules SET default_order = 90 WHERE id = 51400;
UPDATE eh_service_modules SET default_order = 100 WHERE id = 55000;
UPDATE eh_service_modules SET default_order = 110 WHERE id = 56100;
UPDATE eh_service_modules SET default_order = 120 WHERE id = 56200;
UPDATE eh_service_modules SET default_order = 130, `name` = '待办事项【纯APP】' WHERE id = 56300;
UPDATE eh_service_modules SET default_order = 140, parent_id = 50000 , path = '/50000/41020' WHERE id = 41020;
UPDATE eh_service_modules SET default_order = 150 WHERE id = 50700;
UPDATE eh_service_modules SET default_order = 160 WHERE id = 58000;
UPDATE eh_service_modules SET default_order = 170, `name` = '企业公告' WHERE id = 57000;

-- 运营统计
UPDATE eh_service_modules SET parent_id = 130000 , path = '/130000/41300', default_order = 10, `name` = '应用活跃统计' WHERE id = 41300;
UPDATE eh_service_modules SET parent_id = 130000 , path = '/130000/41330', default_order = 20, type = 1, `level` = 2 WHERE id = 41330;

-- 任务管理
UPDATE eh_service_modules SET parent_id = 140000 , path = '/140000/13000', default_order = 10 WHERE id = 13000;

-- 系统管理
UPDATE eh_service_modules SET default_order = 10 WHERE id = 60100;
UPDATE eh_service_modules SET default_order = 20 WHERE id = 60200;

-- 一些模块的应用不允许配置在园区或者已废弃旧模块，例如：“App配置”, “菜单配置”,“业务应用配置”,“园区简介”,“文件下载中心”
UPDATE eh_service_modules set type = 0 where id in (70000, 70200, 70300, 70100, 10200, 61000, 30000, 50200, 40600);

-- 重新布局模块分类  add by yanjun 201803262114 end

-- 更新"入驻申请"菜单的路由 add by yanjun 201803281414
UPDATE eh_web_menus set data_type = 'enter-apply' where id = 43020000;

-- 更新成都创业场的“入驻申请”的actionType为71
UPDATE eh_launch_pad_items set action_type = 71 where action_type = 68  and namespace_id = 999964;

-- 增加一个模块“企业信息”及其菜单 add by yanjun 201803281519
INSERT INTO `eh_service_modules` (`id`, `name`, `parent_id`, `path`, `type`, `level`, `status`, `default_order`, `create_time`, `instance_config`, `action_type`, `update_time`, `operator_uid`, `creator_uid`, `description`, `multiple_flag`, `module_control_type`) VALUES ('21400', '企业信息【企业后台】', '110000', '/110000/21400', '1', '2', '2', '35', '2018-03-28 14:49:34', NULL, '13', '2018-03-28 14:49:45', '0', '0', '0', '0', 'community_control');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('76000000', '基础信息', '0', NULL, NULL, '1', '2', '/76000000', 'organization', '6', NULL, '1', 'system', 'classify', '2');

INSERT INTO `eh_web_menus` (`id`, `name`, `parent_id`, `icon_url`, `data_type`, `leaf_flag`, `status`, `path`, `type`, `sort_num`, `module_id`, `level`, `condition_type`, `category`, `config_type`) VALUES ('76010000', '企业信息', '76000000', NULL, 'customer-management', '1', '2', '/76000000/76010000', 'organization', '10', '21400', '2', 'system', 'module', '2');

-- 部分企业管理应用的名称为空导致菜单名称为空。add by yanjun 201803281555
UPDATE eh_reflection_service_module_apps set `name` = '企业管理' WHERE module_id = 33000 and (`name` is NULL or `name` = '');
UPDATE eh_service_module_apps set `name` = '企业管理' WHERE module_id = 33000 and (`name` is NULL or `name` = '');