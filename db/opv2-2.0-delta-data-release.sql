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
