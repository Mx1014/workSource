-- 启迪香山停车场基本配置，月卡充值、临时车缴费、车辆放行、月卡申请、锁车、车场信息查询
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`) VALUES ('10031', 'community', '240111044332060091', '启迪香山停车场', 'QIDI_DAODING', '', '2', '1', now(), '999959', null, '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 1, "searchCarFlag": 0, "currentInfoType": 2,"identityCardFlag":0}');
-- 其他配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.qididaoding.url', 'http://test.daodingtech.com:9999/xdrpark-app', '启迪香山url', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.qididaoding.code', 'qdxs001', '启迪香山token请求编码', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.qididaoding.secret', 'ed62d4335a294932849415a4cc171e8c', '启迪香山token请求秘钥', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.qididaoding.key', 'TdBMEZBxeRGQIRrN', '启迪香山获取token参数签名秘钥', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.qididaoding.paramKey', '0fe30d4e6f4a4508', '启迪香山通用参数签名秘钥', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.qididaoding.parkingId', '20170104000000000002', '启迪香山停车场标识', 0, NULL);
