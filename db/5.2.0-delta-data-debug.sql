-- 光大停车场基本配置 ，支持临时车，月卡充值
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`) VALUES ('10026', 'community', '240111044331056800', '光大we谷停车场', 'GUANG_DA_WE_GU', '', '2', '1', now(), '999979', null, '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 0,"identityCardFlag":0}');
-- 其他配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangdawegu.url', 'http://120.25.238.52', '光大url', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangdawegu.parkingCode', '4d398d36-5e63-4e46-807c-2bb2ebd4ad38', '光大parkingCode', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangdawegu.appKey', 'wwwbsznsmartcom20180130', '光大appkey', 0, NULL);

INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`) VALUES ('10027', 'community', '240111044331056805', '光大we谷停车场', 'GUANG_DA_WE_GU', '', '2', '1', now(), '999979', null, '{"tempfeeFlag": 1, "rateFlag": 0, "lockCarFlag": 0, "searchCarFlag": 0, "currentInfoType": 0,"identityCardFlag":0}');
