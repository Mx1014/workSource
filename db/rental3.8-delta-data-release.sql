INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('1', '电视', '电视', 'cs://1/image/aW1hZ2UvTVRvd1ltUmlOREl6TXpkaFptTmlZVEV5TkROaFlqZ3lObVl5WmpRM01qQTVOZw', '1');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('2', '投屏', '投屏', 'cs://1/image/aW1hZ2UvTVRvd05Ea3dPVFl3TVRsaVpHSTVOVGMyTURsbU1HWmtPVFkzTmpreU0ySTVNUQ', '2');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('3', '白板', '白板', 'cs://1/image/aW1hZ2UvTVRvd01EWXpNV000WVRJd00yVmpOR1k1TkdaaFpqazJPVEprT0dKak1EVXpaQQ', '3');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('4', '投影仪', '投影仪', 'cs://1/image/aW1hZ2UvTVRveVpqRXdZMk0wT0RkbVpHSTNZek5tT1RRd01XTTFORFJpTkdJelptRTJaZw', '4');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('5', '电话会议', '电话会议', 'cs://1/image/aW1hZ2UvTVRveE0yTXdNRGd5WmpjNU5EWXpaakJqWVdWa04yWXlaV1F5TTJNNU4ySTBZdw', '5');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('6', '视频会议', '视频会议', 'cs://1/image/aW1hZ2UvTVRwbE9UaGhNamcxWkdGbE0yVTNPR1l3WlRZd1ptRmlZV1ptTjJVM1pUQXhPQQ', '6');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('7', '音响', '音响', 'cs://1/image/aW1hZ2UvTVRvNFpETXhOamRpWlRkaVpXTTJOMkU1WlRWbU1XUXlNakZpTjJJeE5qWTBNdw', '7');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('8', '麦克风', '麦克风', 'cs://1/image/aW1hZ2UvTVRwbU1XRXpaRE14WVdVNVpqUXpNRE5pTURZNFlURTROemhtTmpZM09UVmpOdw', '8');

-- 数据初始化
set @id = 0;
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,1,source_type,source_id,'电视','电视','cs://1/image/aW1hZ2UvTVRvd1ltUmlOREl6TXpkaFptTmlZVEV5TkROaFlqZ3lObVl5WmpRM01qQTVOZw',0,1,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,1,source_type,id,'电视','电视','cs://1/image/aW1hZ2UvTVRvd1ltUmlOREl6TXpkaFptTmlZVEV5TkROaFlqZ3lObVl5WmpRM01qQTVOZw',0,1,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,2,source_type,source_id,'投屏','投屏','cs://1/image/aW1hZ2UvTVRvd05Ea3dPVFl3TVRsaVpHSTVOVGMyTURsbU1HWmtPVFkzTmpreU0ySTVNUQ',0,2,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,2,source_type,id,'投屏','投屏','cs://1/image/aW1hZ2UvTVRvd05Ea3dPVFl3TVRsaVpHSTVOVGMyTURsbU1HWmtPVFkzTmpreU0ySTVNUQ',0,2,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,3,source_type,source_id,'白板','白板','cs://1/image/aW1hZ2UvTVRvd01EWXpNV000WVRJd00yVmpOR1k1TkdaaFpqazJPVEprT0dKak1EVXpaQQ',0,3,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,3,source_type,id,'白板','白板','cs://1/image/aW1hZ2UvTVRvd01EWXpNV000WVRJd00yVmpOR1k1TkdaaFpqazJPVEprT0dKak1EVXpaQQ',0,3,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,4,source_type,source_id,'投影仪','投影仪','cs://1/image/aW1hZ2UvTVRveVpqRXdZMk0wT0RkbVpHSTNZek5tT1RRd01XTTFORFJpTkdJelptRTJaZw',0,4,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,4,source_type,id,'投影仪','投影仪','cs://1/image/aW1hZ2UvTVRveVpqRXdZMk0wT0RkbVpHSTNZek5tT1RRd01XTTFORFJpTkdJelptRTJaZw',0,4,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,5,source_type,source_id,'电话会议','电话会议','cs://1/image/aW1hZ2UvTVRveE0yTXdNRGd5WmpjNU5EWXpaakJqWVdWa04yWXlaV1F5TTJNNU4ySTBZdw',0,5,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,5,source_type,id,'电话会议','电话会议','cs://1/image/aW1hZ2UvTVRveE0yTXdNRGd5WmpjNU5EWXpaakJqWVdWa04yWXlaV1F5TTJNNU4ySTBZdw',0,5,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,6,source_type,source_id,'视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwbE9UaGhNamcxWkdGbE0yVTNPR1l3WlRZd1ptRmlZV1ptTjJVM1pUQXhPQQ',0,6,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,6,source_type,id,'视频会议','视频会议','cs://1/image/aW1hZ2UvTVRwbE9UaGhNamcxWkdGbE0yVTNPR1l3WlRZd1ptRmlZV1ptTjJVM1pUQXhPQQ',0,6,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,7,source_type,source_id,'音响','音响','cs://1/image/aW1hZ2UvTVRvNFpETXhOamRpWlRkaVpXTTJOMkU1WlRWbU1XUXlNakZpTjJJeE5qWTBNdw',0,7,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,7,source_type,id,'音响','音响','cs://1/image/aW1hZ2UvTVRvNFpETXhOamRpWlRkaVpXTTJOMkU1WlRWbU1XUXlNakZpTjJJeE5qWTBNdw',0,7,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,8,source_type,source_id,'麦克风','麦克风','cs://1/image/aW1hZ2UvTVRwbU1XRXpaRE14WVdVNVpqUXpNRE5pTURZNFlURTROemhtTmpZM09UVmpOdw',0,8,'default' from  eh_rentalv2_default_rules where source_type = 'resource_rule' and resource_type = 'default';
INSERT INTO `eh_rentalv2_structures` (`id`, `template_id`, `source_type`, `source_id`, `name`, `display_name`, `icon_uri`, `is_surport`, `default_order`, `resource_type`)
select @id := @id + 1,8,source_type,id,'麦克风','麦克风','cs://1/image/aW1hZ2UvTVRwbU1XRXpaRE14WVdVNVpqUXpNRE5pTURZNFlURTROemhtTmpZM09UVmpOdw',0,8,'default' from  eh_rentalv2_default_rules where source_type = 'default_rule' and resource_type = 'default';

update eh_locale_templates set text = '您已成功预约了${resourceName}，使用时间：${useTime}。如需取消，请在预订开始时间前取消，感谢您的使用。' where `scope` = 'rental.notification' and `code` = 5;
update eh_locale_templates set text = '您已成功预约了${resourceName}，预订时间：${useTime}，订单编号：${orderNum}。如需取消，请在预订开始时间前取消，感谢您的使用。${aclink}' where `scope` = 'sms.default' and `code` = 30;
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental.notification', '24', 'zh_CN', '用户取消订单推送消息', '您预约的${useDetail}已成功取消，期待下次为您服务。', '0');
update eh_locale_templates set text = '由于您未在规定时间内完成支付，您预约的${useDetail}已自动取消，期待下次为您服务。' where `scope` = 'rental.notification' and `code` = 14;
update eh_locale_templates set text = '由于您未在规定时间内完成支付，您预约的${useDetail}已自动取消，期待下次为您服务。' where `scope` = 'sms.default' and `code` = 58;
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental.notification', '25', 'zh_CN', '退款成功', '尊敬的用户，您预约的${useDetail}已退款成功，订单编号：${orderNum}，订单金额：${totalAmount}元，退款金额：${refundAmount}元，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '83', 'zh_CN', '退款成功', '尊敬的用户，您预约的${useDetail}已退款成功，订单编号：${orderNum}，订单金额：${totalAmount}元，退款金额：${refundAmount}元，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental.notification', '26', 'zh_CN','取消订单', '尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：0元，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '84', 'zh_CN', '取消订单', '尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：0元，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental.notification', '27', 'zh_CN', '取消订单 需要退款', '尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：${refundAmount}元，退款将在3个工作日内退至您的原支付账户，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '85', 'zh_CN', '取消订单 需要退款','尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：${refundAmount}元，退款将在3个工作日内退至您的原支付账户，期待下次为您服务。', '0');

