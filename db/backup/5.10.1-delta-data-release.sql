-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: OPERATION
-- DESCRIPTION: 此SECTION放升级相关的操作要求，如调接口、查询数据确认、修改配置文件、更新特殊程序等
-- AUTHOR:
-- REMARK:

-- AUTHOR:刘一麟 20181107
-- REMARK:borderServer更新到5.10.0
-- DESCRIPTION：borderServer更新到5.10.0,现网,基线,独立部署都需要更新

-- AUTHOR: xq.tian
-- REMARK: ehuser, 网关, 注册中心 更新, 见：http://s.a.com/docs/server_update_tips/baseline-171541650534

-- AUTHOR:丁建民 20181114
-- REMARK:智谷汇修复自然季日期错误
-- 数据备份：
-- select * from eh_payment_bills where namespace_id=999945;
-- select * from eh_payment_bill_items where namespace_id=999945;

-- 调用接口： /contract/autoGeneratingBill 填写一下参数
-- 域空间：999945
-- contractIds：5954,6251,6252,6352,6353,6448,6458,6717,7148,7223,7226,7227,7228,7229,7230,7231,7232,7233,7238,7239,7240,7242,7243,7244,7245,7246,7247,7250,7251,7289,7290,7291,7292,7293,7294,7295,7296,7297,7298,7299,7300,7301,7302,7303,7304,7305,7306,7307,7308,7309,7310,7311,7312,7313,7314,7315,7320,7321,7322,7323,7324,7325,7326,7327,7328,7329,7330,7339,7343,7348,7351,7353,7355,7358,7359,7361,7363,7364,7365,7370,7371,7372,7374,7375,7417,7418,7419,7420,7423,7424,7425,7428,7429,7430,7431,7432,7433,8790,8791,8794,8795,8797,8800,8801,8802,8803,8806,8807,8808,9316,9317,9318,9319,9320,9321,9322,9323,9324,9325,9326,9327,9328,9329,9330,9331,9332,9333,9334,9335,9336,9337,9339,9340,9342,9343,9344,9345,9346,9347,9348,9349,9350,9413,9440,9441,9442,9443,9444,9445,9448,9451,9452,9453,9454,9455,9458,9459,9460,9461,9462,9463,9490,9491,9498,9501,9504,9507,9510,9511,9514,9517,9518,9519,9522,9523,9568,9571,9574,9575,9576,9577,9582,9585,9586,9591,9594,9597,9604,9606,9619,9647,9710,10004,10005,10407,10473,10474,10507,10516,10578,10582,10583,10584,10587,10701,10702,10703,10704,10705,10706,10707,10708,10709,10715,10716,10717,10718,10719,10721,10726,10758,10915,11026,11027,11028,11031,11032,11035,11036,11037,11038,11039,11041,11042,11044,11045,11046,11048,11049,11050,11051,11052,11053,11054,11056,11057,11060,11063,11064,11065,11066,11068,11069,11070,11071,11072,11073,11074,11098,11099,11100,11101,11102,11103,11104,11105,11106,11107,11108,11109,11110,11111,11112,11165,11167,11189,11191,11194,11228,11229,11233,11298,11299,11300,11303,11304,11306,11307,11317,11319,11321,11323,11325,11327,11384,11424,11425,11457,11467,11719,11749,11812,11813,11821,11828,11909,11925,11928,11975,11976,11993,12047,12060,12118,14000,14178,14179,14181,14182,14200,14201,14281,14408,14415,14438,14439,14440,14441,14442,14443,14444,14445,14446,14447,14448,14467,14468,14539,14540,14541,14542,14543,14544,14545,14546,14547,14552,14553,14558,14559,14693,14734,14736,14834,14838,14854,14855,14856




-- --------------------- SECTION END OPERATION------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ALL
-- DESCRIPTION: 此SECTION放所有域空间都需要执行的脚本，包含基线、独立部署、研发数据等环境

-- AUTHOR: 梁燕龙
-- REMARK: issue-38802
INSERT INTO `eh_locale_strings` (scope, code, locale, text)
    VALUES ('activity', 10033,'zh_CN','请到活动详情页扫码签到');




-- AUTHOR: 严军 20181106
-- REMARK: 更新部分模块的路由和客户端处理方式
UPDATE eh_service_modules SET client_handler_type = 2 WHERE id = 92200;
UPDATE eh_service_modules SET `host` = 'openWXMiniApp',client_handler_type=2 WHERE id = 190000;
UPDATE eh_service_modules SET `host` = 'openApp' WHERE id = 180000;
UPDATE eh_service_modules SET `host` = 'community-bus' WHERE id = 41015;
UPDATE eh_service_modules SET client_handler_type = 2 WHERE id = 41900;

INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('1', '电视', '电视', 'cs://1/image/aW1hZ2UvTVRvd1ltUmlOREl6TXpkaFptTmlZVEV5TkROaFlqZ3lObVl5WmpRM01qQTVOZw', '1');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('2', '投屏', '投屏', 'cs://1/image/aW1hZ2UvTVRvd05Ea3dPVFl3TVRsaVpHSTVOVGMyTURsbU1HWmtPVFkzTmpreU0ySTVNUQ', '2');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('3', '白板', '白板', 'cs://1/image/aW1hZ2UvTVRvd01EWXpNV000WVRJd00yVmpOR1k1TkdaaFpqazJPVEprT0dKak1EVXpaQQ', '3');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('4', '投影仪', '投影仪', 'cs://1/image/aW1hZ2UvTVRveVpqRXdZMk0wT0RkbVpHSTNZek5tT1RRd01XTTFORFJpTkdJelptRTJaZw', '4');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('5', '电话会议', '电话会议', 'cs://1/image/aW1hZ2UvTVRveE0yTXdNRGd5WmpjNU5EWXpaakJqWVdWa04yWXlaV1F5TTJNNU4ySTBZdw', '5');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('6', '视频会议', '视频会议', 'cs://1/image/aW1hZ2UvTVRwbE9UaGhNamcxWkdGbE0yVTNPR1l3WlRZd1ptRmlZV1ptTjJVM1pUQXhPQQ', '6');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('7', '音响', '音响', 'cs://1/image/aW1hZ2UvTVRvNFpETXhOamRpWlRkaVpXTTJOMkU1WlRWbU1XUXlNakZpTjJJeE5qWTBNdw', '7');
INSERT INTO `eh_rentalv2_structure_template` (`id`, `name`, `display_name`, `icon_uri`, `default_order`) VALUES ('8', '麦克风', '麦克风', 'cs://1/image/aW1hZ2UvTVRwbU1XRXpaRE14WVdVNVpqUXpNRE5pTURZNFlURTROemhtTmpZM09UVmpOdw', '8');

-- AUTHOR: 郑思挺 20181106
-- REMARK: 数据初始化
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
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '84', 'zh_CN', '退款成功', '尊敬的用户，您预约的${useDetail}已退款成功，订单编号：${orderNum}，订单金额：${totalAmount}元，退款金额：${refundAmount}元，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental.notification', '26', 'zh_CN','取消订单', '尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：0元，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '85', 'zh_CN', '取消订单', '尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：0元，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'rental.notification', '27', 'zh_CN', '取消订单 需要退款', '尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：${refundAmount}元，退款将在3个工作日内退至您的原支付账户，期待下次为您服务。', '0');
INSERT INTO `eh_locale_templates` ( `scope`, `code`, `locale`, `description`, `text`, `namespace_id`) VALUES ( 'sms.default', '86', 'zh_CN', '取消订单 需要退款','尊敬的用户，您预约的${useDetail}已成功取消，订单金额：${totalAmount}元，退款金额：${refundAmount}元，退款将在3个工作日内退至您的原支付账户，期待下次为您服务。', '0');

-- AUTHOR: 缪洲
-- REMARK: 停车缴费收款账号迁移
update eh_parking_business_payee_accounts ac set ac.merchant_id = ac.payee_id ;

-- AUTHOR: xq.tian
-- REMARK: clientAppKeyApi,需要特殊放过的API列表
INSERT INTO eh_configurations (name, value, description, namespace_id, display_name, is_readonly)
  VALUES ('client.appKeyApi', '/stat/event/postDevice,/pusher/registDevice,/user/syncActivity,/user/signupByAppKey,/user/signup,/user/logoff,/community/findDefaultCommunity,/user/systemInfo,/user/resendVerificationCodeByAppKey,/user/resendVerificationCodeByIdentifierAndAppKey', 'clientAppKeyApi', 0, null, 0);


-- --------------------- SECTION END ALL -----------------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: zuolin-base
-- DESCRIPTION: 此SECTION只在左邻基线（非独立署部）执行的脚本
-- AUTHOR:
-- REMARK:

-- AUTHOR:黄明波
-- REMARK:删除现网有误数据
DELETE FROM `eh_service_alliance_categories` WHERE  `id`=212660 and namespace_id = 999961;
DELETE FROM `eh_service_alliance_categories` WHERE  `id`=217590 and namespace_id = 999961;
DELETE FROM `eh_service_alliance_categories` WHERE  `id`=212984 and namespace_id = 999961;
DELETE FROM `eh_service_alliance_categories` WHERE  `id`=217589 and namespace_id = 999961;


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

-- AUTHOR:黄明波 20181112
-- REMARK: #41990【光大we谷】独立部署，停车对接调整。A区停车场前期对接百世，现替换成捷顺，请调整正式环境参数。B区停车场暂时不变
update eh_parking_lots set vendor_name = 'JIESHUN_GQY1' where vendor_name = 'GUANG_DA_WE_GU';


INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY1.cid', '000000008002520', '客户号', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY1.usr', '000000008002520', '捷顺登录账户名', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY1.psw', '000000008002520', '捷顺登录密码', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY1.signKey', '24e7f28eb0b6044beb3a3ac8afd6402c', 'signKey', 0, NULL, 1);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('parking.jieshun.JIESHUN_GQY1.parkCode', 'p180905025', '小区编号', 0, NULL, 1);


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
-- --------------------- SECTION END guanzhouyuekongjian -------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: ruianxintiandi
-- DESCRIPTION: 此SECTION只在上海瑞安新天地-999929执行的脚本
-- --------------------- SECTION END ruianxintiandi ------------------------------------------
-- --------------------- SECTION BEGIN -------------------------------------------------------
-- ENV: wanzhihui
-- DESCRIPTION: 此SECTION只在万智汇-999953执行的脚本

-- AUTHOR:黄明波
-- REMARK:离线包配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`, `is_readonly`) VALUES ('serviceAlliance.offline.flag', 'true', '是否开启服务联盟离线包模式', 999953, NULL, 1);

update eh_service_modules set client_handler_type = 3 where id = 40500;


-- --------------------- SECTION END wanzhihui ------------------------------------------