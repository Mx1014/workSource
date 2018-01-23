-- 滞纳金变量 by wentian
SET @var_id = (SELECT max(`id`) from `eh_payment_variables`);
INSERT INTO `eh_payment_variables` (`id`, `charging_standard_id`, `charging_items_id`, `name`, `creator_uid`, `create_time`, `operator_uid`, `update_time`, `identifier`) VALUES (@var_id:=@var_id+1, NULL, '6', '欠费', '0', '2017-10-16 09:31:00', NULL, '2017-10-16 09:31:00', 'qf');

--  标准数据增加周期类型及关系表状态start by jiarui 20180105
UPDATE eh_equipment_inspection_standards
SET repeat_type = (SELECT repeat_type FROM eh_repeat_settings WHERE id = eh_equipment_inspection_standards.repeat_setting_id)
WHERE STATUS =2  AND repeat_type =0;

-- 上版未置状态数据修改
UPDATE eh_equipment_inspection_equipment_standard_map
SET `status` = 0
WHERE review_status IN (0, 3 ,4) OR review_result = 2;
-- 上版bug数据修改

--  标准数据增加周期类型 及关系表状态 end by jiarui 20180105
-- 巡检任务状态统一 start by jiarui 20180105
UPDATE eh_equipment_inspection_tasks
SET `status` = 6
WHERE `status` = 4 AND review_result = 2;

UPDATE eh_equipment_inspection_tasks
SET `status` = 7
WHERE `status` = 4 AND review_result = 4;

-- 巡检任务状态统一 end by jiarui 20180105


-- add by sw 20180122 vip车位
INSERT INTO `eh_rentalv2_resource_types` (`id`, `name`, `page_type`, `icon_uri`, `status`, `namespace_id`, `pay_mode`, `unauth_visible`, `menu_type`, `identify`)
	VALUES ('12500', 'VIP车位预约', '0', NULL, '2', '1000000', '0', '0', '1', 'vip_parking');

UPDATE eh_rentalv2_price_rules set user_price_type = 1;

UPDATE eh_rentalv2_price_packages set user_price_type = 1;

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

UPDATE eh_parking_lots join eh_communities on eh_communities.id = eh_parking_lots.owner_id set eh_parking_lots.namespace_id = eh_communities.namespace_id;

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
select * from eh_service_alliance_jump_module WHERE module_url like '%zl://approval/create%' and module_name='审批';
DELETE FROM eh_service_alliance_jump_module WHERE module_url like '%zl://approval/create%' and module_name='审批';