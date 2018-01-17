-- 广兴源停车场基本配置 ，修改联系人
INSERT INTO `eh_parking_lots` (`id`, `owner_type`, `owner_id`, `name`, `vendor_name`, `vendor_lot_token`, `status`, `creator_uid`, `create_time`, `namespace_id`, `recharge_json`, `config_json`) VALUES ('10024', 'community', '240111044332060016', '中百畅停车场', 'ZHONG_BAI_CHANG', '', '2', '1', now(), '999958', null, '{\r\n  \"tempfeeFlag\": 0,\r\n  \"rateFlag\": 1,\r\n  \"lockCarFlag\": 0,\r\n  \"searchCarFlag\": 0,\r\n  \"currentInfoType\": 0,\r\n  \"contact\": \"12345\"\r\n}');
-- 接口配置，trade_code,秘钥配置
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangxinyuan.url', 'http://187k01282j.iask.in', '停车广兴源url', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangxinyuan.trade_code', 'YYZZ_GXYS4672_GXYT7374', '停车广兴源trade_code', 0, NULL);
INSERT INTO `eh_configurations` (`name`, `value`, `description`, `namespace_id`, `display_name`) VALUES ('parking.guangxinyuan.secretKey', 'YYZZ_GXYS4672_GXYT7374', '停车广兴源秘钥', 0, NULL);
