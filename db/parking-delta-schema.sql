
CREATE TABLE `eh_parking_spaces` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `namespace_id` int(11) NOT NULL DEFAULT '0',
  `owner_type` varchar(32) NOT NULL DEFAULT '' COMMENT 'the type of who own the standard, community, etc',
  `owner_id` bigint(20) NOT NULL DEFAULT '0',
  `parking_lot_id` bigint(20) NOT NULL DEFAULT '0' COMMENT 'reference to id of eh_parking_lots',
  `space_no` varchar(64) NOT NULL DEFAULT '',
  `space_address` varchar(64) NOT NULL DEFAULT '',
  `lock_id` varchar(128) DEFAULT NULL,
  `lock_status` varchar(128) DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: inactive, 1: waitingForApproval, 2: active',
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `eh_parking_space_logs` (
  `id` bigint(20) NOT NULL COMMENT 'id of the record',
  `space_no` varchar(64) NOT NULL,
  `lock_id` varchar(128) DEFAULT NULL,
  `contact_phone` varchar(64) DEFAULT NULL,
  `contact_name` varchar(64) DEFAULT NULL,
  `contact_enterprise_name` varchar(128) DEFAULT NULL,
  `operate_type` tinyint(4) NOT NULL COMMENT '1: up, 2: down',
  `user_type` tinyint(4) NOT NULL COMMENT '1: booking person, 2: plate owner',
  `operate_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `eh_rentalv2_resource_types`
ADD COLUMN `menu_type` tinyint(4) DEFAULT 1 COMMENT '1: 通用 2:公司会议室',
ADD COLUMN `identify` varchar(64) DEFAULT NULL COMMENT '类型标识';





CREATE TABLE `eh_rentalv2_order_rules` (
  `id` bigint(20) NOT NULL DEFAULT '0',
  `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型',
  `owner_type` varchar(255) DEFAULT NULL COMMENT 'default_rule, resource_rule',
  `owner_id` bigint(20) DEFAULT NULL,
  `handle_type` tinyint(4) DEFAULT NULL COMMENT '1: 退款, 2: 加收',
  `duration_type` tinyint(4) DEFAULT NULL COMMENT '1: 时长内, 2: 时长外',
  `duration_unit` tinyint(4) DEFAULT NULL COMMENT '时长单位，比如 天，小时',
  `duration` double DEFAULT NULL COMMENT '时长',
  `factor` double DEFAULT NULL COMMENT '价格系数',

  `status` tinyint(4) DEFAULT NULL,
  `creator_uid` bigint(20) NOT NULL DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_uid` bigint(20) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



ALTER TABLE `eh_rentalv2_default_rules`
ADD COLUMN `source_type` varchar(255) DEFAULT NULL COMMENT 'default_rule, resource_rule',
ADD COLUMN `source_id` bigint(20) DEFAULT NULL,
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型',
ADD COLUMN `holiday_open_flag` tinyint(4) DEFAULT NULL COMMENT '节假日是否开放预约: 1-是, 0-否',
ADD COLUMN `holiday_type` tinyint(4) DEFAULT NULL COMMENT '1-普通双休, 2-同步中国节假日',
ADD COLUMN `refund_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full',
ADD COLUMN `overtime_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full';

ALTER TABLE `eh_rentalv2_default_rules`
drop column pay_start_time,
drop column pay_end_time,
drop column payment_ratio,
drop column contact_num,
drop column overtime_time,
drop column unit,
drop column rental_step,
drop column time_step,
drop column cancel_time,
drop column cancel_flag,
drop column workday_price,
drop column weekend_price,
drop column org_member_workday_price,
drop column org_member_weekend_price,
drop column approving_user_workday_price,
drop column approving_user_weekend_price,
drop column rental_type,
drop column exclusive_flag,
drop column auto_assign,
drop column multi_unit,
drop column resource_counts;

UPDATE eh_rentalv2_default_rules set source_type = 'default_rule' where source_type IS NULL;
-- 资源表中规则信息迁移到规则表中
SET @id = (SELECT MAX(id) FROM `eh_rentalv2_default_rules`);
INSERT INTO `eh_rentalv2_default_rules` (`id`, `owner_type`, `owner_id`, `resource_type_id`, `rental_start_time`, `rental_end_time`, `refund_flag`, `refund_ratio`, `creator_uid`, `create_time`, `multi_time_interval`, `need_pay`, `begin_date`, `end_date`, `day_open_time`, `day_close_time`, `open_weekday`, `rental_start_time_flag`, `rental_end_time_flag`, `source_type`, `source_id`)
SELECT (@id := @id + 1), 'organization', organization_id, resource_type_id, rental_start_time, rental_end_time, refund_flag, refund_ratio, creator_uid, create_time, multi_time_interval, need_pay, begin_date, end_date, day_open_time, day_close_time, open_weekday, rental_start_time_flag, rental_end_time_flag, 'resource_rule', id  from eh_rentalv2_resources;


ALTER TABLE `eh_rentalv2_resources`
drop column unit,
drop column time_step,
drop column cancel_time,
drop column cancel_flag,
drop column exclusive_flag;

-- 资源表中规则信息迁移到规则表中
ALTER TABLE `eh_rentalv2_resources`
drop column rental_start_time_flag,
drop column rental_end_time_flag,
drop column open_weekday,
drop column begin_date,
drop column end_date,
drop column rental_start_time,
drop column rental_end_time,
drop column refund_flag,
drop column refund_ratio,
drop column multi_time_interval,
drop column day_open_time,
drop column day_close_time,
drop column day_begin_time,
drop column day_end_time,
drop column need_pay,
drop column resource_type2;

ALTER TABLE `eh_rentalv2_resources`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';


-- 资源单元格表
ALTER TABLE `eh_rentalv2_cells`
drop column unit,
drop column exclusive_flag,
drop column rental_step,
drop column halfresource_price,
drop column halfresource_original_price,
drop column half_org_member_original_price,
drop column half_org_member_price,
drop column half_approving_user_original_price,
drop column half_approving_user_price;

ALTER TABLE `eh_rentalv2_cells`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_price_rules`
drop column weekend_price,
drop column org_member_weekend_price,
drop column approving_user_weekend_price;

ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `user_price_type` tinyint(4) DEFAULT NULL COMMENT '用户价格类型, 1:统一价格 2：用户类型价格';
ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `user_price_type` tinyint(4) DEFAULT NULL COMMENT '用户价格类型, 1:统一价格 2：用户类型价格';


ALTER TABLE eh_rentalv2_orders CHANGE requestor_organization_id user_enterprise_id bigint(20) DEFAULT NULL COMMENT '申请人公司ID';

ALTER TABLE `eh_rentalv2_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型',
ADD COLUMN `custom_object` text,
ADD COLUMN `user_enterprise_name` varchar(64) DEFAULT NULL COMMENT '申请人公司名称',
ADD COLUMN `user_phone` varchar(20) DEFAULT NULL COMMENT '申请人手机',
ADD COLUMN `user_name` varchar(20) DEFAULT NULL COMMENT '申请人姓名',
ADD COLUMN `address_id` bigint(20) DEFAULT NULL COMMENT '楼栋门牌ID',
ADD COLUMN `refund_amount` decimal(10,2) DEFAULT NULL,
ADD COLUMN `actual_start_time` datetime DEFAULT NULL COMMENT '实际使用开始时间',
ADD COLUMN `actual_end_time` datetime DEFAULT NULL COMMENT '实际使用结束时间',
ADD COLUMN `string_tag1` varchar(128) DEFAULT NULL,
ADD COLUMN `string_tag2` varchar(128) DEFAULT NULL,
ADD COLUMN `scene` varchar(64) DEFAULT NULL COMMENT '下单时场景信息，用来计算价格',
ADD COLUMN `refund_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full',
ADD COLUMN `overtime_strategy` tinyint(4) DEFAULT NULL COMMENT '1-custom, 2-full',
ADD COLUMN `old_end_time` datetime DEFAULT NULL COMMENT '延长订单时，存老的使用结束时间',
ADD COLUMN `old_custom_object` text;


ALTER TABLE `eh_rentalv2_orders`
drop column reserve_money,
drop column pay_start_time,
drop column pay_end_time;

ALTER TABLE `eh_rentalv2_resource_orders`
drop column rental_step,
drop column exclusive_flag;

UPDATE eh_rentalv2_default_rules set resource_type = 'default';
UPDATE eh_rentalv2_resources set resource_type = 'default';
UPDATE eh_rentalv2_cells set resource_type = 'default';
UPDATE eh_rentalv2_orders set resource_type = 'default';

ALTER TABLE `eh_rentalv2_close_dates`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_config_attachments`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_items`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_items_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_refund_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_order_attachments`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_price_packages`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_price_rules`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_time_interval`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_resource_orders`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';

ALTER TABLE `eh_rentalv2_resource_numbers`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_resource_pics`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';
ALTER TABLE `eh_rentalv2_resource_ranges`
ADD COLUMN `resource_type` varchar(64) DEFAULT NULL COMMENT '资源类型';


UPDATE eh_rentalv2_close_dates set resource_type = 'default';
UPDATE eh_rentalv2_config_attachments set resource_type = 'default';
UPDATE eh_rentalv2_items set resource_type = 'default';
UPDATE eh_rentalv2_items_orders set resource_type = 'default';
UPDATE eh_rentalv2_resource_orders set resource_type = 'default';
UPDATE eh_rentalv2_order_attachments set resource_type = 'default';
UPDATE eh_rentalv2_price_packages set resource_type = 'default';
UPDATE eh_rentalv2_price_rules set resource_type = 'default';
UPDATE eh_rentalv2_time_interval set resource_type = 'default';
UPDATE eh_rentalv2_resource_orders set resource_type = 'default';

UPDATE eh_rentalv2_resource_numbers set resource_type = 'default';
UPDATE eh_rentalv2_resource_ranges set resource_type = 'default';
UPDATE eh_rentalv2_resource_pics set resource_type = 'default';


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
  VALUES ('parking', '10022', 'zh_CN', '升起车锁失败');
INSERT INTO `eh_locale_strings` (`scope`, `code`, `locale`, `text`)
  VALUES ('parking', '10023', 'zh_CN', '降下车锁失败');

INSERT INTO `ehcore`.`eh_configurations`(`name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ('parking.dingding.url', 'https://public.dingdingtingche.com', NULL, 0, NULL);
INSERT INTO `ehcore`.`eh_configurations`(`name`, `value`, `description`, `namespace_id`, `display_name`)
  VALUES ('parking.dingding.hubMac', 'CC:1B:E0:E0:09:F8', NULL, 0, NULL);











